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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import org.hl7.fhir.instance.model.api.*;
/**
 * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
 */
@ResourceDef(name="ExplanationOfBenefit", profile="http://hl7.org/fhir/Profile/ExplanationOfBenefit")
public class ExplanationOfBenefit extends DomainResource {

    @Block()
    public static class PayeeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Party to be reimbursed: Subscriber, provider, other.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Party to be paid any benefits payable", formalDefinition="Party to be reimbursed: Subscriber, provider, other." )
        protected Coding type;

        /**
         * The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).
         */
        @Child(name = "provider", type = {Practitioner.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Provider who is the payee", formalDefinition="The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned)." )
        protected Reference provider;

        /**
         * The actual object that is the target of the reference (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        protected Practitioner providerTarget;

        /**
         * The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).
         */
        @Child(name = "organization", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Organization who is the payee", formalDefinition="The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned)." )
        protected Reference organization;

        /**
         * The actual object that is the target of the reference (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        protected Organization organizationTarget;

        /**
         * The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).
         */
        @Child(name = "person", type = {Patient.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Other person who is the payee", formalDefinition="The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned)." )
        protected Reference person;

        /**
         * The actual object that is the target of the reference (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        protected Patient personTarget;

        private static final long serialVersionUID = -503108488L;

    /**
     * Constructor
     */
      public PayeeComponent() {
        super();
      }

        /**
         * @return {@link #type} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public PayeeComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #provider} (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Reference getProvider() { 
          if (this.provider == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.provider");
            else if (Configuration.doAutoCreate())
              this.provider = new Reference(); // cc
          return this.provider;
        }

        public boolean hasProvider() { 
          return this.provider != null && !this.provider.isEmpty();
        }

        /**
         * @param value {@link #provider} (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setProvider(Reference value) { 
          this.provider = value;
          return this;
        }

        /**
         * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Practitioner getProviderTarget() { 
          if (this.providerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.provider");
            else if (Configuration.doAutoCreate())
              this.providerTarget = new Practitioner(); // aa
          return this.providerTarget;
        }

        /**
         * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setProviderTarget(Practitioner value) { 
          this.providerTarget = value;
          return this;
        }

        /**
         * @return {@link #organization} (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Reference getOrganization() { 
          if (this.organization == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organization = new Reference(); // cc
          return this.organization;
        }

        public boolean hasOrganization() { 
          return this.organization != null && !this.organization.isEmpty();
        }

        /**
         * @param value {@link #organization} (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setOrganization(Reference value) { 
          this.organization = value;
          return this;
        }

        /**
         * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Organization getOrganizationTarget() { 
          if (this.organizationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organizationTarget = new Organization(); // aa
          return this.organizationTarget;
        }

        /**
         * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setOrganizationTarget(Organization value) { 
          this.organizationTarget = value;
          return this;
        }

        /**
         * @return {@link #person} (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Reference getPerson() { 
          if (this.person == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.person");
            else if (Configuration.doAutoCreate())
              this.person = new Reference(); // cc
          return this.person;
        }

        public boolean hasPerson() { 
          return this.person != null && !this.person.isEmpty();
        }

        /**
         * @param value {@link #person} (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setPerson(Reference value) { 
          this.person = value;
          return this;
        }

        /**
         * @return {@link #person} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Patient getPersonTarget() { 
          if (this.personTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.person");
            else if (Configuration.doAutoCreate())
              this.personTarget = new Patient(); // aa
          return this.personTarget;
        }

        /**
         * @param value {@link #person} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setPersonTarget(Patient value) { 
          this.personTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "Party to be reimbursed: Subscriber, provider, other.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("provider", "Reference(Practitioner)", "The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).", 0, java.lang.Integer.MAX_VALUE, provider));
          childrenList.add(new Property("organization", "Reference(Organization)", "The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).", 0, java.lang.Integer.MAX_VALUE, organization));
          childrenList.add(new Property("person", "Reference(Patient)", "The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).", 0, java.lang.Integer.MAX_VALUE, person));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("provider"))
          this.provider = castToReference(value); // Reference
        else if (name.equals("organization"))
          this.organization = castToReference(value); // Reference
        else if (name.equals("person"))
          this.person = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("person")) {
          this.person = new Reference();
          return this.person;
        }
        else
          return super.addChild(name);
      }

      public PayeeComponent copy() {
        PayeeComponent dst = new PayeeComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.person = person == null ? null : person.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PayeeComponent))
          return false;
        PayeeComponent o = (PayeeComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(provider, o.provider, true) && compareDeep(organization, o.organization, true)
           && compareDeep(person, o.person, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PayeeComponent))
          return false;
        PayeeComponent o = (PayeeComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (provider == null || provider.isEmpty())
           && (organization == null || organization.isEmpty()) && (person == null || person.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExplanationOfBenefit.payee";

  }

  }

    @Block()
    public static class DiagnosisComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Sequence of diagnosis which serves to order and provide a link.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Sequence of diagnosis", formalDefinition="Sequence of diagnosis which serves to order and provide a link." )
        protected PositiveIntType sequence;

        /**
         * The diagnosis.
         */
        @Child(name = "diagnosis", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Patient's list of diagnosis", formalDefinition="The diagnosis." )
        protected Coding diagnosis;

        private static final long serialVersionUID = -795010186L;

    /**
     * Constructor
     */
      public DiagnosisComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DiagnosisComponent(PositiveIntType sequence, Coding diagnosis) {
        super();
        this.sequence = sequence;
        this.diagnosis = diagnosis;
      }

        /**
         * @return {@link #sequence} (Sequence of diagnosis which serves to order and provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosisComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new PositiveIntType(); // bb
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (Sequence of diagnosis which serves to order and provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public DiagnosisComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Sequence of diagnosis which serves to order and provide a link.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value Sequence of diagnosis which serves to order and provide a link.
         */
        public DiagnosisComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #diagnosis} (The diagnosis.)
         */
        public Coding getDiagnosis() { 
          if (this.diagnosis == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosisComponent.diagnosis");
            else if (Configuration.doAutoCreate())
              this.diagnosis = new Coding(); // cc
          return this.diagnosis;
        }

        public boolean hasDiagnosis() { 
          return this.diagnosis != null && !this.diagnosis.isEmpty();
        }

        /**
         * @param value {@link #diagnosis} (The diagnosis.)
         */
        public DiagnosisComponent setDiagnosis(Coding value) { 
          this.diagnosis = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "Sequence of diagnosis which serves to order and provide a link.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("diagnosis", "Coding", "The diagnosis.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("diagnosis"))
          this.diagnosis = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("diagnosis")) {
          this.diagnosis = new Coding();
          return this.diagnosis;
        }
        else
          return super.addChild(name);
      }

      public DiagnosisComponent copy() {
        DiagnosisComponent dst = new DiagnosisComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.diagnosis = diagnosis == null ? null : diagnosis.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DiagnosisComponent))
          return false;
        DiagnosisComponent o = (DiagnosisComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(diagnosis, o.diagnosis, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DiagnosisComponent))
          return false;
        DiagnosisComponent o = (DiagnosisComponent) other;
        return compareValues(sequence, o.sequence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (diagnosis == null || diagnosis.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExplanationOfBenefit.diagnosis";

  }

  }

    @Block()
    public static class CoverageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reference to the program or plan identification, underwriter or payor.
         */
        @Child(name = "coverage", type = {Coverage.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the program or plan identification, underwriter or payor." )
        protected Reference coverage;

        /**
         * The actual object that is the target of the reference (Reference to the program or plan identification, underwriter or payor.)
         */
        protected Coverage coverageTarget;

        /**
         * The relationship of the patient to the subscriber.
         */
        @Child(name = "relationship", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Patient relationship to subscriber", formalDefinition="The relationship of the patient to the subscriber." )
        protected Coding relationship;

        /**
         * A list of references from the Insurer to which these services pertain.
         */
        @Child(name = "preAuthRef", type = {StringType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Pre-Authorization/Determination Reference", formalDefinition="A list of references from the Insurer to which these services pertain." )
        protected List<StringType> preAuthRef;

        private static final long serialVersionUID = -59672683L;

    /**
     * Constructor
     */
      public CoverageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CoverageComponent(Reference coverage, Coding relationship) {
        super();
        this.coverage = coverage;
        this.relationship = relationship;
      }

        /**
         * @return {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
         */
        public Reference getCoverage() { 
          if (this.coverage == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.coverage");
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
        public CoverageComponent setCoverage(Reference value) { 
          this.coverage = value;
          return this;
        }

        /**
         * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
         */
        public Coverage getCoverageTarget() { 
          if (this.coverageTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.coverage");
            else if (Configuration.doAutoCreate())
              this.coverageTarget = new Coverage(); // aa
          return this.coverageTarget;
        }

        /**
         * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
         */
        public CoverageComponent setCoverageTarget(Coverage value) { 
          this.coverageTarget = value;
          return this;
        }

        /**
         * @return {@link #relationship} (The relationship of the patient to the subscriber.)
         */
        public Coding getRelationship() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new Coding(); // cc
          return this.relationship;
        }

        public boolean hasRelationship() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        /**
         * @param value {@link #relationship} (The relationship of the patient to the subscriber.)
         */
        public CoverageComponent setRelationship(Coding value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return {@link #preAuthRef} (A list of references from the Insurer to which these services pertain.)
         */
        public List<StringType> getPreAuthRef() { 
          if (this.preAuthRef == null)
            this.preAuthRef = new ArrayList<StringType>();
          return this.preAuthRef;
        }

        public boolean hasPreAuthRef() { 
          if (this.preAuthRef == null)
            return false;
          for (StringType item : this.preAuthRef)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #preAuthRef} (A list of references from the Insurer to which these services pertain.)
         */
    // syntactic sugar
        public StringType addPreAuthRefElement() {//2 
          StringType t = new StringType();
          if (this.preAuthRef == null)
            this.preAuthRef = new ArrayList<StringType>();
          this.preAuthRef.add(t);
          return t;
        }

        /**
         * @param value {@link #preAuthRef} (A list of references from the Insurer to which these services pertain.)
         */
        public CoverageComponent addPreAuthRef(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.preAuthRef == null)
            this.preAuthRef = new ArrayList<StringType>();
          this.preAuthRef.add(t);
          return this;
        }

        /**
         * @param value {@link #preAuthRef} (A list of references from the Insurer to which these services pertain.)
         */
        public boolean hasPreAuthRef(String value) { 
          if (this.preAuthRef == null)
            return false;
          for (StringType v : this.preAuthRef)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("coverage", "Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, coverage));
          childrenList.add(new Property("relationship", "Coding", "The relationship of the patient to the subscriber.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("preAuthRef", "string", "A list of references from the Insurer to which these services pertain.", 0, java.lang.Integer.MAX_VALUE, preAuthRef));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("coverage"))
          this.coverage = castToReference(value); // Reference
        else if (name.equals("relationship"))
          this.relationship = castToCoding(value); // Coding
        else if (name.equals("preAuthRef"))
          this.getPreAuthRef().add(castToString(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("relationship")) {
          this.relationship = new Coding();
          return this.relationship;
        }
        else if (name.equals("preAuthRef")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.preAuthRef");
        }
        else
          return super.addChild(name);
      }

      public CoverageComponent copy() {
        CoverageComponent dst = new CoverageComponent();
        copyValues(dst);
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        if (preAuthRef != null) {
          dst.preAuthRef = new ArrayList<StringType>();
          for (StringType i : preAuthRef)
            dst.preAuthRef.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CoverageComponent))
          return false;
        CoverageComponent o = (CoverageComponent) other;
        return compareDeep(coverage, o.coverage, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(preAuthRef, o.preAuthRef, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CoverageComponent))
          return false;
        CoverageComponent o = (CoverageComponent) other;
        return compareValues(preAuthRef, o.preAuthRef, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (coverage == null || coverage.isEmpty()) && (relationship == null || relationship.isEmpty())
           && (preAuthRef == null || preAuthRef.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.coverage";

  }

  }

    @Block()
    public static class ItemsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line number.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType sequence;

        /**
         * The type of product or service.
         */
        @Child(name = "type", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Group or type of product or service", formalDefinition="The type of product or service." )
        protected Coding type;

        /**
         * The practitioner who is responsible for the services rendered to the patient.
         */
        @Child(name = "provider", type = {Practitioner.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
        protected Reference provider;

        /**
         * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
         */
        protected Practitioner providerTarget;

        /**
         * Diagnosis applicable for this service or product line.
         */
        @Child(name = "diagnosisLinkId", type = {PositiveIntType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Diagnosis Link", formalDefinition="Diagnosis applicable for this service or product line." )
        protected List<PositiveIntType> diagnosisLinkId;

        /**
         * If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.
         */
        @Child(name = "service", type = {Coding.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Item Code", formalDefinition="If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied." )
        protected Coding service;

        /**
         * The date or dates when the enclosed suite of services were performed or completed.
         */
        @Child(name = "serviced", type = {DateType.class, Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date or dates of Service", formalDefinition="The date or dates when the enclosed suite of services were performed or completed." )
        protected Type serviced;

        /**
         * Where the service was provided.
         */
        @Child(name = "place", type = {Coding.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Place of service", formalDefinition="Where the service was provided." )
        protected Coding place;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=10, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=11, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=12, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Coding.class}, order=13, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected Coding udi;

        /**
         * Physical service site on the patient (limb, tooth, etc).
         */
        @Child(name = "bodySite", type = {Coding.class}, order=14, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service Location", formalDefinition="Physical service site on the patient (limb, tooth, etc)." )
        protected Coding bodySite;

        /**
         * A region or surface of the site, eg. limb region or tooth surface(s).
         */
        @Child(name = "subSite", type = {Coding.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Service Sub-location", formalDefinition="A region or surface of the site, eg. limb region or tooth surface(s)." )
        protected List<Coding> subSite;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.
         */
        @Child(name = "modifier", type = {Coding.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen." )
        protected List<Coding> modifier;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication details", formalDefinition="The adjudications results." )
        protected List<ItemAdjudicationComponent> adjudication;

        /**
         * Second tier of goods and services.
         */
        @Child(name = "detail", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Additional items", formalDefinition="Second tier of goods and services." )
        protected List<DetailComponent> detail;

        /**
         * The materials and placement date of prior fixed prosthesis.
         */
        @Child(name = "prosthesis", type = {}, order=20, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Prosthetic details", formalDefinition="The materials and placement date of prior fixed prosthesis." )
        protected ProsthesisComponent prosthesis;

        private static final long serialVersionUID = 1380825950L;

    /**
     * Constructor
     */
      public ItemsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemsComponent(PositiveIntType sequence, Coding type, Coding service) {
        super();
        this.sequence = sequence;
        this.type = type;
        this.service = service;
      }

        /**
         * @return {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new PositiveIntType(); // bb
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public ItemsComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A service line number.
         */
        public ItemsComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of product or service.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of product or service.)
         */
        public ItemsComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
         */
        public Reference getProvider() { 
          if (this.provider == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.provider");
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
        public ItemsComponent setProvider(Reference value) { 
          this.provider = value;
          return this;
        }

        /**
         * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
         */
        public Practitioner getProviderTarget() { 
          if (this.providerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.provider");
            else if (Configuration.doAutoCreate())
              this.providerTarget = new Practitioner(); // aa
          return this.providerTarget;
        }

        /**
         * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
         */
        public ItemsComponent setProviderTarget(Practitioner value) { 
          this.providerTarget = value;
          return this;
        }

        /**
         * @return {@link #diagnosisLinkId} (Diagnosis applicable for this service or product line.)
         */
        public List<PositiveIntType> getDiagnosisLinkId() { 
          if (this.diagnosisLinkId == null)
            this.diagnosisLinkId = new ArrayList<PositiveIntType>();
          return this.diagnosisLinkId;
        }

        public boolean hasDiagnosisLinkId() { 
          if (this.diagnosisLinkId == null)
            return false;
          for (PositiveIntType item : this.diagnosisLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #diagnosisLinkId} (Diagnosis applicable for this service or product line.)
         */
    // syntactic sugar
        public PositiveIntType addDiagnosisLinkIdElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.diagnosisLinkId == null)
            this.diagnosisLinkId = new ArrayList<PositiveIntType>();
          this.diagnosisLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #diagnosisLinkId} (Diagnosis applicable for this service or product line.)
         */
        public ItemsComponent addDiagnosisLinkId(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.diagnosisLinkId == null)
            this.diagnosisLinkId = new ArrayList<PositiveIntType>();
          this.diagnosisLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #diagnosisLinkId} (Diagnosis applicable for this service or product line.)
         */
        public boolean hasDiagnosisLinkId(int value) { 
          if (this.diagnosisLinkId == null)
            return false;
          for (PositiveIntType v : this.diagnosisLinkId)
            if (v.equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #service} (If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.)
         */
        public Coding getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new Coding(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.)
         */
        public ItemsComponent setService(Coding value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
         */
        public Type getServiced() { 
          return this.serviced;
        }

        /**
         * @return {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
         */
        public DateType getServicedDateType() throws FHIRException { 
          if (!(this.serviced instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.serviced.getClass().getName()+" was encountered");
          return (DateType) this.serviced;
        }

        public boolean hasServicedDateType() { 
          return this.serviced instanceof DateType;
        }

        /**
         * @return {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
         */
        public Period getServicedPeriod() throws FHIRException { 
          if (!(this.serviced instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.serviced.getClass().getName()+" was encountered");
          return (Period) this.serviced;
        }

        public boolean hasServicedPeriod() { 
          return this.serviced instanceof Period;
        }

        public boolean hasServiced() { 
          return this.serviced != null && !this.serviced.isEmpty();
        }

        /**
         * @param value {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
         */
        public ItemsComponent setServiced(Type value) { 
          this.serviced = value;
          return this;
        }

        /**
         * @return {@link #place} (Where the service was provided.)
         */
        public Coding getPlace() { 
          if (this.place == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.place");
            else if (Configuration.doAutoCreate())
              this.place = new Coding(); // cc
          return this.place;
        }

        public boolean hasPlace() { 
          return this.place != null && !this.place.isEmpty();
        }

        /**
         * @param value {@link #place} (Where the service was provided.)
         */
        public ItemsComponent setPlace(Coding value) { 
          this.place = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public ItemsComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.)
         */
        public ItemsComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType(); // bb
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public ItemsComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ItemsComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ItemsComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ItemsComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType(); // bb
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public ItemsComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public ItemsComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public ItemsComponent setPoints(long value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public ItemsComponent setPoints(double value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public ItemsComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public Coding getUdi() { 
          if (this.udi == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.udi");
            else if (Configuration.doAutoCreate())
              this.udi = new Coding(); // cc
          return this.udi;
        }

        public boolean hasUdi() { 
          return this.udi != null && !this.udi.isEmpty();
        }

        /**
         * @param value {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public ItemsComponent setUdi(Coding value) { 
          this.udi = value;
          return this;
        }

        /**
         * @return {@link #bodySite} (Physical service site on the patient (limb, tooth, etc).)
         */
        public Coding getBodySite() { 
          if (this.bodySite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.bodySite");
            else if (Configuration.doAutoCreate())
              this.bodySite = new Coding(); // cc
          return this.bodySite;
        }

        public boolean hasBodySite() { 
          return this.bodySite != null && !this.bodySite.isEmpty();
        }

        /**
         * @param value {@link #bodySite} (Physical service site on the patient (limb, tooth, etc).)
         */
        public ItemsComponent setBodySite(Coding value) { 
          this.bodySite = value;
          return this;
        }

        /**
         * @return {@link #subSite} (A region or surface of the site, eg. limb region or tooth surface(s).)
         */
        public List<Coding> getSubSite() { 
          if (this.subSite == null)
            this.subSite = new ArrayList<Coding>();
          return this.subSite;
        }

        public boolean hasSubSite() { 
          if (this.subSite == null)
            return false;
          for (Coding item : this.subSite)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #subSite} (A region or surface of the site, eg. limb region or tooth surface(s).)
         */
    // syntactic sugar
        public Coding addSubSite() { //3
          Coding t = new Coding();
          if (this.subSite == null)
            this.subSite = new ArrayList<Coding>();
          this.subSite.add(t);
          return t;
        }

    // syntactic sugar
        public ItemsComponent addSubSite(Coding t) { //3
          if (t == null)
            return this;
          if (this.subSite == null)
            this.subSite = new ArrayList<Coding>();
          this.subSite.add(t);
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.)
         */
        public List<Coding> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<Coding>();
          return this.modifier;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (Coding item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.)
         */
    // syntactic sugar
        public Coding addModifier() { //3
          Coding t = new Coding();
          if (this.modifier == null)
            this.modifier = new ArrayList<Coding>();
          this.modifier.add(t);
          return t;
        }

    // syntactic sugar
        public ItemsComponent addModifier(Coding t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<Coding>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
        }

        public boolean hasNoteNumber() { 
          if (this.noteNumber == null)
            return false;
          for (PositiveIntType item : this.noteNumber)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
    // syntactic sugar
        public PositiveIntType addNoteNumberElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #noteNumber} (A list of note references to the notes provided below.)
         */
        public ItemsComponent addNoteNumber(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #noteNumber} (A list of note references to the notes provided below.)
         */
        public boolean hasNoteNumber(int value) { 
          if (this.noteNumber == null)
            return false;
          for (PositiveIntType v : this.noteNumber)
            if (v.equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
        public List<ItemAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<ItemAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (ItemAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public ItemAdjudicationComponent addAdjudication() { //3
          ItemAdjudicationComponent t = new ItemAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<ItemAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public ItemsComponent addAdjudication(ItemAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<ItemAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return {@link #detail} (Second tier of goods and services.)
         */
        public List<DetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<DetailComponent>();
          return this.detail;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (DetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detail} (Second tier of goods and services.)
         */
    // syntactic sugar
        public DetailComponent addDetail() { //3
          DetailComponent t = new DetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<DetailComponent>();
          this.detail.add(t);
          return t;
        }

    // syntactic sugar
        public ItemsComponent addDetail(DetailComponent t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<DetailComponent>();
          this.detail.add(t);
          return this;
        }

        /**
         * @return {@link #prosthesis} (The materials and placement date of prior fixed prosthesis.)
         */
        public ProsthesisComponent getProsthesis() { 
          if (this.prosthesis == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.prosthesis");
            else if (Configuration.doAutoCreate())
              this.prosthesis = new ProsthesisComponent(); // cc
          return this.prosthesis;
        }

        public boolean hasProsthesis() { 
          return this.prosthesis != null && !this.prosthesis.isEmpty();
        }

        /**
         * @param value {@link #prosthesis} (The materials and placement date of prior fixed prosthesis.)
         */
        public ItemsComponent setProsthesis(ProsthesisComponent value) { 
          this.prosthesis = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("provider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
          childrenList.add(new Property("diagnosisLinkId", "positiveInt", "Diagnosis applicable for this service or product line.", 0, java.lang.Integer.MAX_VALUE, diagnosisLinkId));
          childrenList.add(new Property("service", "Coding", "If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, serviced));
          childrenList.add(new Property("place", "Coding", "Where the service was provided.", 0, java.lang.Integer.MAX_VALUE, place));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Coding", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("bodySite", "Coding", "Physical service site on the patient (limb, tooth, etc).", 0, java.lang.Integer.MAX_VALUE, bodySite));
          childrenList.add(new Property("subSite", "Coding", "A region or surface of the site, eg. limb region or tooth surface(s).", 0, java.lang.Integer.MAX_VALUE, subSite));
          childrenList.add(new Property("modifier", "Coding", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("detail", "", "Second tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, detail));
          childrenList.add(new Property("prosthesis", "", "The materials and placement date of prior fixed prosthesis.", 0, java.lang.Integer.MAX_VALUE, prosthesis));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("provider"))
          this.provider = castToReference(value); // Reference
        else if (name.equals("diagnosisLinkId"))
          this.getDiagnosisLinkId().add(castToPositiveInt(value));
        else if (name.equals("service"))
          this.service = castToCoding(value); // Coding
        else if (name.equals("serviced[x]"))
          this.serviced = (Type) value; // Type
        else if (name.equals("place"))
          this.place = castToCoding(value); // Coding
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("unitPrice"))
          this.unitPrice = castToMoney(value); // Money
        else if (name.equals("factor"))
          this.factor = castToDecimal(value); // DecimalType
        else if (name.equals("points"))
          this.points = castToDecimal(value); // DecimalType
        else if (name.equals("net"))
          this.net = castToMoney(value); // Money
        else if (name.equals("udi"))
          this.udi = castToCoding(value); // Coding
        else if (name.equals("bodySite"))
          this.bodySite = castToCoding(value); // Coding
        else if (name.equals("subSite"))
          this.getSubSite().add(castToCoding(value));
        else if (name.equals("modifier"))
          this.getModifier().add(castToCoding(value));
        else if (name.equals("noteNumber"))
          this.getNoteNumber().add(castToPositiveInt(value));
        else if (name.equals("adjudication"))
          this.getAdjudication().add((ItemAdjudicationComponent) value);
        else if (name.equals("detail"))
          this.getDetail().add((DetailComponent) value);
        else if (name.equals("prosthesis"))
          this.prosthesis = (ProsthesisComponent) value; // ProsthesisComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("diagnosisLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.diagnosisLinkId");
        }
        else if (name.equals("service")) {
          this.service = new Coding();
          return this.service;
        }
        else if (name.equals("servicedDate")) {
          this.serviced = new DateType();
          return this.serviced;
        }
        else if (name.equals("servicedPeriod")) {
          this.serviced = new Period();
          return this.serviced;
        }
        else if (name.equals("place")) {
          this.place = new Coding();
          return this.place;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          this.udi = new Coding();
          return this.udi;
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new Coding();
          return this.bodySite;
        }
        else if (name.equals("subSite")) {
          return addSubSite();
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("noteNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.noteNumber");
        }
        else if (name.equals("adjudication")) {
          return addAdjudication();
        }
        else if (name.equals("detail")) {
          return addDetail();
        }
        else if (name.equals("prosthesis")) {
          this.prosthesis = new ProsthesisComponent();
          return this.prosthesis;
        }
        else
          return super.addChild(name);
      }

      public ItemsComponent copy() {
        ItemsComponent dst = new ItemsComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.type = type == null ? null : type.copy();
        dst.provider = provider == null ? null : provider.copy();
        if (diagnosisLinkId != null) {
          dst.diagnosisLinkId = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : diagnosisLinkId)
            dst.diagnosisLinkId.add(i.copy());
        };
        dst.service = service == null ? null : service.copy();
        dst.serviced = serviced == null ? null : serviced.copy();
        dst.place = place == null ? null : place.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        dst.udi = udi == null ? null : udi.copy();
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        if (subSite != null) {
          dst.subSite = new ArrayList<Coding>();
          for (Coding i : subSite)
            dst.subSite.add(i.copy());
        };
        if (modifier != null) {
          dst.modifier = new ArrayList<Coding>();
          for (Coding i : modifier)
            dst.modifier.add(i.copy());
        };
        if (noteNumber != null) {
          dst.noteNumber = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : noteNumber)
            dst.noteNumber.add(i.copy());
        };
        if (adjudication != null) {
          dst.adjudication = new ArrayList<ItemAdjudicationComponent>();
          for (ItemAdjudicationComponent i : adjudication)
            dst.adjudication.add(i.copy());
        };
        if (detail != null) {
          dst.detail = new ArrayList<DetailComponent>();
          for (DetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        dst.prosthesis = prosthesis == null ? null : prosthesis.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ItemsComponent))
          return false;
        ItemsComponent o = (ItemsComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(provider, o.provider, true)
           && compareDeep(diagnosisLinkId, o.diagnosisLinkId, true) && compareDeep(service, o.service, true)
           && compareDeep(serviced, o.serviced, true) && compareDeep(place, o.place, true) && compareDeep(quantity, o.quantity, true)
           && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true) && compareDeep(points, o.points, true)
           && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true) && compareDeep(bodySite, o.bodySite, true)
           && compareDeep(subSite, o.subSite, true) && compareDeep(modifier, o.modifier, true) && compareDeep(noteNumber, o.noteNumber, true)
           && compareDeep(adjudication, o.adjudication, true) && compareDeep(detail, o.detail, true) && compareDeep(prosthesis, o.prosthesis, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ItemsComponent))
          return false;
        ItemsComponent o = (ItemsComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(diagnosisLinkId, o.diagnosisLinkId, true)
           && compareValues(factor, o.factor, true) && compareValues(points, o.points, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (type == null || type.isEmpty())
           && (provider == null || provider.isEmpty()) && (diagnosisLinkId == null || diagnosisLinkId.isEmpty())
           && (service == null || service.isEmpty()) && (serviced == null || serviced.isEmpty()) && (place == null || place.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
           && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty()) && (net == null || net.isEmpty())
           && (udi == null || udi.isEmpty()) && (bodySite == null || bodySite.isEmpty()) && (subSite == null || subSite.isEmpty())
           && (modifier == null || modifier.isEmpty()) && (noteNumber == null || noteNumber.isEmpty())
           && (adjudication == null || adjudication.isEmpty()) && (detail == null || detail.isEmpty())
           && (prosthesis == null || prosthesis.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item";

  }

  }

    @Block()
    public static class ItemAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.
         */
        @Child(name = "category", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc." )
        protected Coding category;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication reason", formalDefinition="Adjudication reason such as limit reached." )
        protected Coding reason;

        /**
         * Monitory amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monitory amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monitory value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = -1926987562L;

    /**
     * Constructor
     */
      public ItemAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemAdjudicationComponent(Coding category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public Coding getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemAdjudicationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Coding(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public ItemAdjudicationComponent setCategory(Coding value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemAdjudicationComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new Coding(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Adjudication reason such as limit reached.)
         */
        public ItemAdjudicationComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monitory amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monitory amount associated with the code.)
         */
        public ItemAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemAdjudicationComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new DecimalType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ItemAdjudicationComponent setValueElement(DecimalType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public BigDecimal getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public ItemAdjudicationComponent setValue(BigDecimal value) { 
          if (value == null)
            this.value = null;
          else {
            if (this.value == null)
              this.value = new DecimalType();
            this.value.setValue(value);
          }
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public ItemAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public ItemAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "Coding", "Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("reason", "Coding", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monitory amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
          childrenList.add(new Property("value", "decimal", "A non-monetary value for example a percentage. Mutually exclusive to the amount element above.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category"))
          this.category = castToCoding(value); // Coding
        else if (name.equals("reason"))
          this.reason = castToCoding(value); // Coding
        else if (name.equals("amount"))
          this.amount = castToMoney(value); // Money
        else if (name.equals("value"))
          this.value = castToDecimal(value); // DecimalType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new Coding();
          return this.category;
        }
        else if (name.equals("reason")) {
          this.reason = new Coding();
          return this.reason;
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.value");
        }
        else
          return super.addChild(name);
      }

      public ItemAdjudicationComponent copy() {
        ItemAdjudicationComponent dst = new ItemAdjudicationComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ItemAdjudicationComponent))
          return false;
        ItemAdjudicationComponent o = (ItemAdjudicationComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ItemAdjudicationComponent))
          return false;
        ItemAdjudicationComponent o = (ItemAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (reason == null || reason.isEmpty())
           && (amount == null || amount.isEmpty()) && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.adjudication";

  }

  }

    @Block()
    public static class DetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line number.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType sequence;

        /**
         * The type of product or service.
         */
        @Child(name = "type", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Group or type of product or service", formalDefinition="The type of product or service." )
        protected Coding type;

        /**
         * If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.
         */
        @Child(name = "service", type = {Coding.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Additional item codes", formalDefinition="If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied." )
        protected Coding service;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Total additional item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Coding.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected Coding udi;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Detail adjudication", formalDefinition="The adjudications results." )
        protected List<DetailAdjudicationComponent> adjudication;

        /**
         * Third tier of goods and services.
         */
        @Child(name = "subDetail", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Additional items", formalDefinition="Third tier of goods and services." )
        protected List<SubDetailComponent> subDetail;

        private static final long serialVersionUID = 157913723L;

    /**
     * Constructor
     */
      public DetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DetailComponent(PositiveIntType sequence, Coding type, Coding service) {
        super();
        this.sequence = sequence;
        this.type = type;
        this.service = service;
      }

        /**
         * @return {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new PositiveIntType(); // bb
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public DetailComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A service line number.
         */
        public DetailComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of product or service.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of product or service.)
         */
        public DetailComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #service} (If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.)
         */
        public Coding getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new Coding(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.)
         */
        public DetailComponent setService(Coding value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public DetailComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.)
         */
        public DetailComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType(); // bb
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DetailComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public DetailComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public DetailComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public DetailComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType(); // bb
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DetailComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public DetailComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public DetailComponent setPoints(long value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public DetailComponent setPoints(double value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public DetailComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public Coding getUdi() { 
          if (this.udi == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.udi");
            else if (Configuration.doAutoCreate())
              this.udi = new Coding(); // cc
          return this.udi;
        }

        public boolean hasUdi() { 
          return this.udi != null && !this.udi.isEmpty();
        }

        /**
         * @param value {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public DetailComponent setUdi(Coding value) { 
          this.udi = value;
          return this;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
        public List<DetailAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<DetailAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (DetailAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public DetailAdjudicationComponent addAdjudication() { //3
          DetailAdjudicationComponent t = new DetailAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<DetailAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public DetailComponent addAdjudication(DetailAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<DetailAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return {@link #subDetail} (Third tier of goods and services.)
         */
        public List<SubDetailComponent> getSubDetail() { 
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          return this.subDetail;
        }

        public boolean hasSubDetail() { 
          if (this.subDetail == null)
            return false;
          for (SubDetailComponent item : this.subDetail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #subDetail} (Third tier of goods and services.)
         */
    // syntactic sugar
        public SubDetailComponent addSubDetail() { //3
          SubDetailComponent t = new SubDetailComponent();
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          this.subDetail.add(t);
          return t;
        }

    // syntactic sugar
        public DetailComponent addSubDetail(SubDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          this.subDetail.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("service", "Coding", "If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Coding", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("subDetail", "", "Third tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, subDetail));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("service"))
          this.service = castToCoding(value); // Coding
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("unitPrice"))
          this.unitPrice = castToMoney(value); // Money
        else if (name.equals("factor"))
          this.factor = castToDecimal(value); // DecimalType
        else if (name.equals("points"))
          this.points = castToDecimal(value); // DecimalType
        else if (name.equals("net"))
          this.net = castToMoney(value); // Money
        else if (name.equals("udi"))
          this.udi = castToCoding(value); // Coding
        else if (name.equals("adjudication"))
          this.getAdjudication().add((DetailAdjudicationComponent) value);
        else if (name.equals("subDetail"))
          this.getSubDetail().add((SubDetailComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("service")) {
          this.service = new Coding();
          return this.service;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          this.udi = new Coding();
          return this.udi;
        }
        else if (name.equals("adjudication")) {
          return addAdjudication();
        }
        else if (name.equals("subDetail")) {
          return addSubDetail();
        }
        else
          return super.addChild(name);
      }

      public DetailComponent copy() {
        DetailComponent dst = new DetailComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.type = type == null ? null : type.copy();
        dst.service = service == null ? null : service.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        dst.udi = udi == null ? null : udi.copy();
        if (adjudication != null) {
          dst.adjudication = new ArrayList<DetailAdjudicationComponent>();
          for (DetailAdjudicationComponent i : adjudication)
            dst.adjudication.add(i.copy());
        };
        if (subDetail != null) {
          dst.subDetail = new ArrayList<SubDetailComponent>();
          for (SubDetailComponent i : subDetail)
            dst.subDetail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DetailComponent))
          return false;
        DetailComponent o = (DetailComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(service, o.service, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(points, o.points, true) && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true)
           && compareDeep(adjudication, o.adjudication, true) && compareDeep(subDetail, o.subDetail, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DetailComponent))
          return false;
        DetailComponent o = (DetailComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(factor, o.factor, true) && compareValues(points, o.points, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (type == null || type.isEmpty())
           && (service == null || service.isEmpty()) && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
           && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty()) && (net == null || net.isEmpty())
           && (udi == null || udi.isEmpty()) && (adjudication == null || adjudication.isEmpty()) && (subDetail == null || subDetail.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.detail";

  }

  }

    @Block()
    public static class DetailAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.
         */
        @Child(name = "code", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc." )
        protected Coding code;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication reason", formalDefinition="Adjudication reason such as limit reached." )
        protected Coding reason;

        /**
         * Monitory amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monitory amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monitory value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = 746807621L;

    /**
     * Constructor
     */
      public DetailAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DetailAdjudicationComponent(Coding code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public Coding getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailAdjudicationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Coding(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public DetailAdjudicationComponent setCode(Coding value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailAdjudicationComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new Coding(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Adjudication reason such as limit reached.)
         */
        public DetailAdjudicationComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monitory amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monitory amount associated with the code.)
         */
        public DetailAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailAdjudicationComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new DecimalType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DetailAdjudicationComponent setValueElement(DecimalType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public BigDecimal getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public DetailAdjudicationComponent setValue(BigDecimal value) { 
          if (value == null)
            this.value = null;
          else {
            if (this.value == null)
              this.value = new DecimalType();
            this.value.setValue(value);
          }
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public DetailAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public DetailAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "Coding", "Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("reason", "Coding", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monitory amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
          childrenList.add(new Property("value", "decimal", "A non-monetary value for example a percentage. Mutually exclusive to the amount element above.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = castToCoding(value); // Coding
        else if (name.equals("reason"))
          this.reason = castToCoding(value); // Coding
        else if (name.equals("amount"))
          this.amount = castToMoney(value); // Money
        else if (name.equals("value"))
          this.value = castToDecimal(value); // DecimalType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new Coding();
          return this.code;
        }
        else if (name.equals("reason")) {
          this.reason = new Coding();
          return this.reason;
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.value");
        }
        else
          return super.addChild(name);
      }

      public DetailAdjudicationComponent copy() {
        DetailAdjudicationComponent dst = new DetailAdjudicationComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DetailAdjudicationComponent))
          return false;
        DetailAdjudicationComponent o = (DetailAdjudicationComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DetailAdjudicationComponent))
          return false;
        DetailAdjudicationComponent o = (DetailAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (reason == null || reason.isEmpty())
           && (amount == null || amount.isEmpty()) && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.detail.adjudication";

  }

  }

    @Block()
    public static class SubDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line number.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType sequence;

        /**
         * The type of product or service.
         */
        @Child(name = "type", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of product or service", formalDefinition="The type of product or service." )
        protected Coding type;

        /**
         * The fee for an addittional service or product or charge.
         */
        @Child(name = "service", type = {Coding.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Additional item codes", formalDefinition="The fee for an addittional service or product or charge." )
        protected Coding service;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * The fee for an addittional service or product or charge.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="The fee for an addittional service or product or charge." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Net additional item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Coding.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected Coding udi;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="SubDetail adjudication", formalDefinition="The adjudications results." )
        protected List<SubDetailAdjudicationComponent> adjudication;

        private static final long serialVersionUID = 465609870L;

    /**
     * Constructor
     */
      public SubDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubDetailComponent(PositiveIntType sequence, Coding type, Coding service) {
        super();
        this.sequence = sequence;
        this.type = type;
        this.service = service;
      }

        /**
         * @return {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new PositiveIntType(); // bb
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public SubDetailComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A service line number.
         */
        public SubDetailComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of product or service.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of product or service.)
         */
        public SubDetailComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #service} (The fee for an addittional service or product or charge.)
         */
        public Coding getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new Coding(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (The fee for an addittional service or product or charge.)
         */
        public SubDetailComponent setService(Coding value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public SubDetailComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (The fee for an addittional service or product or charge.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (The fee for an addittional service or product or charge.)
         */
        public SubDetailComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType(); // bb
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public SubDetailComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public SubDetailComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public SubDetailComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public SubDetailComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType(); // bb
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public SubDetailComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public SubDetailComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public SubDetailComponent setPoints(long value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public SubDetailComponent setPoints(double value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public SubDetailComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public Coding getUdi() { 
          if (this.udi == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.udi");
            else if (Configuration.doAutoCreate())
              this.udi = new Coding(); // cc
          return this.udi;
        }

        public boolean hasUdi() { 
          return this.udi != null && !this.udi.isEmpty();
        }

        /**
         * @param value {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public SubDetailComponent setUdi(Coding value) { 
          this.udi = value;
          return this;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
        public List<SubDetailAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<SubDetailAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (SubDetailAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public SubDetailAdjudicationComponent addAdjudication() { //3
          SubDetailAdjudicationComponent t = new SubDetailAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<SubDetailAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public SubDetailComponent addAdjudication(SubDetailAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<SubDetailAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("service", "Coding", "The fee for an addittional service or product or charge.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "The fee for an addittional service or product or charge.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Coding", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("service"))
          this.service = castToCoding(value); // Coding
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("unitPrice"))
          this.unitPrice = castToMoney(value); // Money
        else if (name.equals("factor"))
          this.factor = castToDecimal(value); // DecimalType
        else if (name.equals("points"))
          this.points = castToDecimal(value); // DecimalType
        else if (name.equals("net"))
          this.net = castToMoney(value); // Money
        else if (name.equals("udi"))
          this.udi = castToCoding(value); // Coding
        else if (name.equals("adjudication"))
          this.getAdjudication().add((SubDetailAdjudicationComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("service")) {
          this.service = new Coding();
          return this.service;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          this.udi = new Coding();
          return this.udi;
        }
        else if (name.equals("adjudication")) {
          return addAdjudication();
        }
        else
          return super.addChild(name);
      }

      public SubDetailComponent copy() {
        SubDetailComponent dst = new SubDetailComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.type = type == null ? null : type.copy();
        dst.service = service == null ? null : service.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        dst.udi = udi == null ? null : udi.copy();
        if (adjudication != null) {
          dst.adjudication = new ArrayList<SubDetailAdjudicationComponent>();
          for (SubDetailAdjudicationComponent i : adjudication)
            dst.adjudication.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SubDetailComponent))
          return false;
        SubDetailComponent o = (SubDetailComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(service, o.service, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(points, o.points, true) && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true)
           && compareDeep(adjudication, o.adjudication, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SubDetailComponent))
          return false;
        SubDetailComponent o = (SubDetailComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(factor, o.factor, true) && compareValues(points, o.points, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (type == null || type.isEmpty())
           && (service == null || service.isEmpty()) && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
           && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty()) && (net == null || net.isEmpty())
           && (udi == null || udi.isEmpty()) && (adjudication == null || adjudication.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.detail.subDetail";

  }

  }

    @Block()
    public static class SubDetailAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.
         */
        @Child(name = "code", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc." )
        protected Coding code;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication reason", formalDefinition="Adjudication reason such as limit reached." )
        protected Coding reason;

        /**
         * Monitory amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monitory amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monitory value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = 746807621L;

    /**
     * Constructor
     */
      public SubDetailAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubDetailAdjudicationComponent(Coding code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public Coding getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailAdjudicationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Coding(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public SubDetailAdjudicationComponent setCode(Coding value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailAdjudicationComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new Coding(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Adjudication reason such as limit reached.)
         */
        public SubDetailAdjudicationComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monitory amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monitory amount associated with the code.)
         */
        public SubDetailAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailAdjudicationComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new DecimalType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public SubDetailAdjudicationComponent setValueElement(DecimalType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public BigDecimal getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public SubDetailAdjudicationComponent setValue(BigDecimal value) { 
          if (value == null)
            this.value = null;
          else {
            if (this.value == null)
              this.value = new DecimalType();
            this.value.setValue(value);
          }
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public SubDetailAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public SubDetailAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "Coding", "Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("reason", "Coding", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monitory amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
          childrenList.add(new Property("value", "decimal", "A non-monetary value for example a percentage. Mutually exclusive to the amount element above.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = castToCoding(value); // Coding
        else if (name.equals("reason"))
          this.reason = castToCoding(value); // Coding
        else if (name.equals("amount"))
          this.amount = castToMoney(value); // Money
        else if (name.equals("value"))
          this.value = castToDecimal(value); // DecimalType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new Coding();
          return this.code;
        }
        else if (name.equals("reason")) {
          this.reason = new Coding();
          return this.reason;
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.value");
        }
        else
          return super.addChild(name);
      }

      public SubDetailAdjudicationComponent copy() {
        SubDetailAdjudicationComponent dst = new SubDetailAdjudicationComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SubDetailAdjudicationComponent))
          return false;
        SubDetailAdjudicationComponent o = (SubDetailAdjudicationComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SubDetailAdjudicationComponent))
          return false;
        SubDetailAdjudicationComponent o = (SubDetailAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (reason == null || reason.isEmpty())
           && (amount == null || amount.isEmpty()) && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.detail.subDetail.adjudication";

  }

  }

    @Block()
    public static class ProsthesisComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates whether this is the initial placement of a fixed prosthesis.
         */
        @Child(name = "initial", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Is this the initial service", formalDefinition="Indicates whether this is the initial placement of a fixed prosthesis." )
        protected BooleanType initial;

        /**
         * Date of the initial placement.
         */
        @Child(name = "priorDate", type = {DateType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Initial service Date", formalDefinition="Date of the initial placement." )
        protected DateType priorDate;

        /**
         * Material of the prior denture or bridge prosthesis. (Oral).
         */
        @Child(name = "priorMaterial", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Prosthetic Material", formalDefinition="Material of the prior denture or bridge prosthesis. (Oral)." )
        protected Coding priorMaterial;

        private static final long serialVersionUID = 1739349641L;

    /**
     * Constructor
     */
      public ProsthesisComponent() {
        super();
      }

        /**
         * @return {@link #initial} (Indicates whether this is the initial placement of a fixed prosthesis.). This is the underlying object with id, value and extensions. The accessor "getInitial" gives direct access to the value
         */
        public BooleanType getInitialElement() { 
          if (this.initial == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProsthesisComponent.initial");
            else if (Configuration.doAutoCreate())
              this.initial = new BooleanType(); // bb
          return this.initial;
        }

        public boolean hasInitialElement() { 
          return this.initial != null && !this.initial.isEmpty();
        }

        public boolean hasInitial() { 
          return this.initial != null && !this.initial.isEmpty();
        }

        /**
         * @param value {@link #initial} (Indicates whether this is the initial placement of a fixed prosthesis.). This is the underlying object with id, value and extensions. The accessor "getInitial" gives direct access to the value
         */
        public ProsthesisComponent setInitialElement(BooleanType value) { 
          this.initial = value;
          return this;
        }

        /**
         * @return Indicates whether this is the initial placement of a fixed prosthesis.
         */
        public boolean getInitial() { 
          return this.initial == null || this.initial.isEmpty() ? false : this.initial.getValue();
        }

        /**
         * @param value Indicates whether this is the initial placement of a fixed prosthesis.
         */
        public ProsthesisComponent setInitial(boolean value) { 
            if (this.initial == null)
              this.initial = new BooleanType();
            this.initial.setValue(value);
          return this;
        }

        /**
         * @return {@link #priorDate} (Date of the initial placement.). This is the underlying object with id, value and extensions. The accessor "getPriorDate" gives direct access to the value
         */
        public DateType getPriorDateElement() { 
          if (this.priorDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProsthesisComponent.priorDate");
            else if (Configuration.doAutoCreate())
              this.priorDate = new DateType(); // bb
          return this.priorDate;
        }

        public boolean hasPriorDateElement() { 
          return this.priorDate != null && !this.priorDate.isEmpty();
        }

        public boolean hasPriorDate() { 
          return this.priorDate != null && !this.priorDate.isEmpty();
        }

        /**
         * @param value {@link #priorDate} (Date of the initial placement.). This is the underlying object with id, value and extensions. The accessor "getPriorDate" gives direct access to the value
         */
        public ProsthesisComponent setPriorDateElement(DateType value) { 
          this.priorDate = value;
          return this;
        }

        /**
         * @return Date of the initial placement.
         */
        public Date getPriorDate() { 
          return this.priorDate == null ? null : this.priorDate.getValue();
        }

        /**
         * @param value Date of the initial placement.
         */
        public ProsthesisComponent setPriorDate(Date value) { 
          if (value == null)
            this.priorDate = null;
          else {
            if (this.priorDate == null)
              this.priorDate = new DateType();
            this.priorDate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #priorMaterial} (Material of the prior denture or bridge prosthesis. (Oral).)
         */
        public Coding getPriorMaterial() { 
          if (this.priorMaterial == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProsthesisComponent.priorMaterial");
            else if (Configuration.doAutoCreate())
              this.priorMaterial = new Coding(); // cc
          return this.priorMaterial;
        }

        public boolean hasPriorMaterial() { 
          return this.priorMaterial != null && !this.priorMaterial.isEmpty();
        }

        /**
         * @param value {@link #priorMaterial} (Material of the prior denture or bridge prosthesis. (Oral).)
         */
        public ProsthesisComponent setPriorMaterial(Coding value) { 
          this.priorMaterial = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("initial", "boolean", "Indicates whether this is the initial placement of a fixed prosthesis.", 0, java.lang.Integer.MAX_VALUE, initial));
          childrenList.add(new Property("priorDate", "date", "Date of the initial placement.", 0, java.lang.Integer.MAX_VALUE, priorDate));
          childrenList.add(new Property("priorMaterial", "Coding", "Material of the prior denture or bridge prosthesis. (Oral).", 0, java.lang.Integer.MAX_VALUE, priorMaterial));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("initial"))
          this.initial = castToBoolean(value); // BooleanType
        else if (name.equals("priorDate"))
          this.priorDate = castToDate(value); // DateType
        else if (name.equals("priorMaterial"))
          this.priorMaterial = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("initial")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.initial");
        }
        else if (name.equals("priorDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.priorDate");
        }
        else if (name.equals("priorMaterial")) {
          this.priorMaterial = new Coding();
          return this.priorMaterial;
        }
        else
          return super.addChild(name);
      }

      public ProsthesisComponent copy() {
        ProsthesisComponent dst = new ProsthesisComponent();
        copyValues(dst);
        dst.initial = initial == null ? null : initial.copy();
        dst.priorDate = priorDate == null ? null : priorDate.copy();
        dst.priorMaterial = priorMaterial == null ? null : priorMaterial.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProsthesisComponent))
          return false;
        ProsthesisComponent o = (ProsthesisComponent) other;
        return compareDeep(initial, o.initial, true) && compareDeep(priorDate, o.priorDate, true) && compareDeep(priorMaterial, o.priorMaterial, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProsthesisComponent))
          return false;
        ProsthesisComponent o = (ProsthesisComponent) other;
        return compareValues(initial, o.initial, true) && compareValues(priorDate, o.priorDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (initial == null || initial.isEmpty()) && (priorDate == null || priorDate.isEmpty())
           && (priorMaterial == null || priorMaterial.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.prosthesis";

  }

  }

    @Block()
    public static class AddedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * List of input service items which this service line is intended to replace.
         */
        @Child(name = "sequenceLinkId", type = {PositiveIntType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Service instances", formalDefinition="List of input service items which this service line is intended to replace." )
        protected List<PositiveIntType> sequenceLinkId;

        /**
         * A code to indicate the Professional Service or Product supplied.
         */
        @Child(name = "service", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Group, Service or Product", formalDefinition="A code to indicate the Professional Service or Product supplied." )
        protected Coding service;

        /**
         * The fee charged for the professional service or product..
         */
        @Child(name = "fee", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Professional fee or Product charge", formalDefinition="The fee charged for the professional service or product.." )
        protected Money fee;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumberLinkId", type = {PositiveIntType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumberLinkId;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Added items adjudication", formalDefinition="The adjudications results." )
        protected List<AddedItemAdjudicationComponent> adjudication;

        /**
         * The second tier service adjudications for payor added services.
         */
        @Child(name = "detail", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Added items details", formalDefinition="The second tier service adjudications for payor added services." )
        protected List<AddedItemsDetailComponent> detail;

        private static final long serialVersionUID = -1675935854L;

    /**
     * Constructor
     */
      public AddedItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AddedItemComponent(Coding service) {
        super();
        this.service = service;
      }

        /**
         * @return {@link #sequenceLinkId} (List of input service items which this service line is intended to replace.)
         */
        public List<PositiveIntType> getSequenceLinkId() { 
          if (this.sequenceLinkId == null)
            this.sequenceLinkId = new ArrayList<PositiveIntType>();
          return this.sequenceLinkId;
        }

        public boolean hasSequenceLinkId() { 
          if (this.sequenceLinkId == null)
            return false;
          for (PositiveIntType item : this.sequenceLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #sequenceLinkId} (List of input service items which this service line is intended to replace.)
         */
    // syntactic sugar
        public PositiveIntType addSequenceLinkIdElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.sequenceLinkId == null)
            this.sequenceLinkId = new ArrayList<PositiveIntType>();
          this.sequenceLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #sequenceLinkId} (List of input service items which this service line is intended to replace.)
         */
        public AddedItemComponent addSequenceLinkId(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.sequenceLinkId == null)
            this.sequenceLinkId = new ArrayList<PositiveIntType>();
          this.sequenceLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #sequenceLinkId} (List of input service items which this service line is intended to replace.)
         */
        public boolean hasSequenceLinkId(int value) { 
          if (this.sequenceLinkId == null)
            return false;
          for (PositiveIntType v : this.sequenceLinkId)
            if (v.equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #service} (A code to indicate the Professional Service or Product supplied.)
         */
        public Coding getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new Coding(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (A code to indicate the Professional Service or Product supplied.)
         */
        public AddedItemComponent setService(Coding value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #fee} (The fee charged for the professional service or product..)
         */
        public Money getFee() { 
          if (this.fee == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.fee");
            else if (Configuration.doAutoCreate())
              this.fee = new Money(); // cc
          return this.fee;
        }

        public boolean hasFee() { 
          return this.fee != null && !this.fee.isEmpty();
        }

        /**
         * @param value {@link #fee} (The fee charged for the professional service or product..)
         */
        public AddedItemComponent setFee(Money value) { 
          this.fee = value;
          return this;
        }

        /**
         * @return {@link #noteNumberLinkId} (A list of note references to the notes provided below.)
         */
        public List<PositiveIntType> getNoteNumberLinkId() { 
          if (this.noteNumberLinkId == null)
            this.noteNumberLinkId = new ArrayList<PositiveIntType>();
          return this.noteNumberLinkId;
        }

        public boolean hasNoteNumberLinkId() { 
          if (this.noteNumberLinkId == null)
            return false;
          for (PositiveIntType item : this.noteNumberLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #noteNumberLinkId} (A list of note references to the notes provided below.)
         */
    // syntactic sugar
        public PositiveIntType addNoteNumberLinkIdElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.noteNumberLinkId == null)
            this.noteNumberLinkId = new ArrayList<PositiveIntType>();
          this.noteNumberLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #noteNumberLinkId} (A list of note references to the notes provided below.)
         */
        public AddedItemComponent addNoteNumberLinkId(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.noteNumberLinkId == null)
            this.noteNumberLinkId = new ArrayList<PositiveIntType>();
          this.noteNumberLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #noteNumberLinkId} (A list of note references to the notes provided below.)
         */
        public boolean hasNoteNumberLinkId(int value) { 
          if (this.noteNumberLinkId == null)
            return false;
          for (PositiveIntType v : this.noteNumberLinkId)
            if (v.equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
        public List<AddedItemAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (AddedItemAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public AddedItemAdjudicationComponent addAdjudication() { //3
          AddedItemAdjudicationComponent t = new AddedItemAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public AddedItemComponent addAdjudication(AddedItemAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return {@link #detail} (The second tier service adjudications for payor added services.)
         */
        public List<AddedItemsDetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemsDetailComponent>();
          return this.detail;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (AddedItemsDetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detail} (The second tier service adjudications for payor added services.)
         */
    // syntactic sugar
        public AddedItemsDetailComponent addDetail() { //3
          AddedItemsDetailComponent t = new AddedItemsDetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemsDetailComponent>();
          this.detail.add(t);
          return t;
        }

    // syntactic sugar
        public AddedItemComponent addDetail(AddedItemsDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemsDetailComponent>();
          this.detail.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequenceLinkId", "positiveInt", "List of input service items which this service line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, sequenceLinkId));
          childrenList.add(new Property("service", "Coding", "A code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("fee", "Money", "The fee charged for the professional service or product..", 0, java.lang.Integer.MAX_VALUE, fee));
          childrenList.add(new Property("noteNumberLinkId", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumberLinkId));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("detail", "", "The second tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequenceLinkId"))
          this.getSequenceLinkId().add(castToPositiveInt(value));
        else if (name.equals("service"))
          this.service = castToCoding(value); // Coding
        else if (name.equals("fee"))
          this.fee = castToMoney(value); // Money
        else if (name.equals("noteNumberLinkId"))
          this.getNoteNumberLinkId().add(castToPositiveInt(value));
        else if (name.equals("adjudication"))
          this.getAdjudication().add((AddedItemAdjudicationComponent) value);
        else if (name.equals("detail"))
          this.getDetail().add((AddedItemsDetailComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequenceLinkId");
        }
        else if (name.equals("service")) {
          this.service = new Coding();
          return this.service;
        }
        else if (name.equals("fee")) {
          this.fee = new Money();
          return this.fee;
        }
        else if (name.equals("noteNumberLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.noteNumberLinkId");
        }
        else if (name.equals("adjudication")) {
          return addAdjudication();
        }
        else if (name.equals("detail")) {
          return addDetail();
        }
        else
          return super.addChild(name);
      }

      public AddedItemComponent copy() {
        AddedItemComponent dst = new AddedItemComponent();
        copyValues(dst);
        if (sequenceLinkId != null) {
          dst.sequenceLinkId = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : sequenceLinkId)
            dst.sequenceLinkId.add(i.copy());
        };
        dst.service = service == null ? null : service.copy();
        dst.fee = fee == null ? null : fee.copy();
        if (noteNumberLinkId != null) {
          dst.noteNumberLinkId = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : noteNumberLinkId)
            dst.noteNumberLinkId.add(i.copy());
        };
        if (adjudication != null) {
          dst.adjudication = new ArrayList<AddedItemAdjudicationComponent>();
          for (AddedItemAdjudicationComponent i : adjudication)
            dst.adjudication.add(i.copy());
        };
        if (detail != null) {
          dst.detail = new ArrayList<AddedItemsDetailComponent>();
          for (AddedItemsDetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AddedItemComponent))
          return false;
        AddedItemComponent o = (AddedItemComponent) other;
        return compareDeep(sequenceLinkId, o.sequenceLinkId, true) && compareDeep(service, o.service, true)
           && compareDeep(fee, o.fee, true) && compareDeep(noteNumberLinkId, o.noteNumberLinkId, true) && compareDeep(adjudication, o.adjudication, true)
           && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AddedItemComponent))
          return false;
        AddedItemComponent o = (AddedItemComponent) other;
        return compareValues(sequenceLinkId, o.sequenceLinkId, true) && compareValues(noteNumberLinkId, o.noteNumberLinkId, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequenceLinkId == null || sequenceLinkId.isEmpty()) && (service == null || service.isEmpty())
           && (fee == null || fee.isEmpty()) && (noteNumberLinkId == null || noteNumberLinkId.isEmpty())
           && (adjudication == null || adjudication.isEmpty()) && (detail == null || detail.isEmpty())
          ;
      }

  public String fhirType() {
    return "ExplanationOfBenefit.addItem";

  }

  }

    @Block()
    public static class AddedItemAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.
         */
        @Child(name = "code", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc." )
        protected Coding code;

        /**
         * Monitory amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monitory amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monitory value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = -949880587L;

    /**
     * Constructor
     */
      public AddedItemAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AddedItemAdjudicationComponent(Coding code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public Coding getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemAdjudicationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Coding(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public AddedItemAdjudicationComponent setCode(Coding value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monitory amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monitory amount associated with the code.)
         */
        public AddedItemAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemAdjudicationComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new DecimalType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public AddedItemAdjudicationComponent setValueElement(DecimalType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public BigDecimal getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AddedItemAdjudicationComponent setValue(BigDecimal value) { 
          if (value == null)
            this.value = null;
          else {
            if (this.value == null)
              this.value = new DecimalType();
            this.value.setValue(value);
          }
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AddedItemAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AddedItemAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "Coding", "Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("amount", "Money", "Monitory amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
          childrenList.add(new Property("value", "decimal", "A non-monetary value for example a percentage. Mutually exclusive to the amount element above.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = castToCoding(value); // Coding
        else if (name.equals("amount"))
          this.amount = castToMoney(value); // Money
        else if (name.equals("value"))
          this.value = castToDecimal(value); // DecimalType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new Coding();
          return this.code;
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.value");
        }
        else
          return super.addChild(name);
      }

      public AddedItemAdjudicationComponent copy() {
        AddedItemAdjudicationComponent dst = new AddedItemAdjudicationComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AddedItemAdjudicationComponent))
          return false;
        AddedItemAdjudicationComponent o = (AddedItemAdjudicationComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(amount, o.amount, true) && compareDeep(value, o.value, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AddedItemAdjudicationComponent))
          return false;
        AddedItemAdjudicationComponent o = (AddedItemAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (amount == null || amount.isEmpty())
           && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.addItem.adjudication";

  }

  }

    @Block()
    public static class AddedItemsDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code to indicate the Professional Service or Product supplied.
         */
        @Child(name = "service", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service or Product", formalDefinition="A code to indicate the Professional Service or Product supplied." )
        protected Coding service;

        /**
         * The fee charged for the professional service or product..
         */
        @Child(name = "fee", type = {Money.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Professional fee or Product charge", formalDefinition="The fee charged for the professional service or product.." )
        protected Money fee;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Added items detail adjudication", formalDefinition="The adjudications results." )
        protected List<AddedItemDetailAdjudicationComponent> adjudication;

        private static final long serialVersionUID = -2104242020L;

    /**
     * Constructor
     */
      public AddedItemsDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AddedItemsDetailComponent(Coding service) {
        super();
        this.service = service;
      }

        /**
         * @return {@link #service} (A code to indicate the Professional Service or Product supplied.)
         */
        public Coding getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemsDetailComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new Coding(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (A code to indicate the Professional Service or Product supplied.)
         */
        public AddedItemsDetailComponent setService(Coding value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #fee} (The fee charged for the professional service or product..)
         */
        public Money getFee() { 
          if (this.fee == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemsDetailComponent.fee");
            else if (Configuration.doAutoCreate())
              this.fee = new Money(); // cc
          return this.fee;
        }

        public boolean hasFee() { 
          return this.fee != null && !this.fee.isEmpty();
        }

        /**
         * @param value {@link #fee} (The fee charged for the professional service or product..)
         */
        public AddedItemsDetailComponent setFee(Money value) { 
          this.fee = value;
          return this;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
        public List<AddedItemDetailAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemDetailAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (AddedItemDetailAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public AddedItemDetailAdjudicationComponent addAdjudication() { //3
          AddedItemDetailAdjudicationComponent t = new AddedItemDetailAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemDetailAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public AddedItemsDetailComponent addAdjudication(AddedItemDetailAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemDetailAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("service", "Coding", "A code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("fee", "Money", "The fee charged for the professional service or product..", 0, java.lang.Integer.MAX_VALUE, fee));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("service"))
          this.service = castToCoding(value); // Coding
        else if (name.equals("fee"))
          this.fee = castToMoney(value); // Money
        else if (name.equals("adjudication"))
          this.getAdjudication().add((AddedItemDetailAdjudicationComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("service")) {
          this.service = new Coding();
          return this.service;
        }
        else if (name.equals("fee")) {
          this.fee = new Money();
          return this.fee;
        }
        else if (name.equals("adjudication")) {
          return addAdjudication();
        }
        else
          return super.addChild(name);
      }

      public AddedItemsDetailComponent copy() {
        AddedItemsDetailComponent dst = new AddedItemsDetailComponent();
        copyValues(dst);
        dst.service = service == null ? null : service.copy();
        dst.fee = fee == null ? null : fee.copy();
        if (adjudication != null) {
          dst.adjudication = new ArrayList<AddedItemDetailAdjudicationComponent>();
          for (AddedItemDetailAdjudicationComponent i : adjudication)
            dst.adjudication.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AddedItemsDetailComponent))
          return false;
        AddedItemsDetailComponent o = (AddedItemsDetailComponent) other;
        return compareDeep(service, o.service, true) && compareDeep(fee, o.fee, true) && compareDeep(adjudication, o.adjudication, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AddedItemsDetailComponent))
          return false;
        AddedItemsDetailComponent o = (AddedItemsDetailComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (service == null || service.isEmpty()) && (fee == null || fee.isEmpty())
           && (adjudication == null || adjudication.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.addItem.detail";

  }

  }

    @Block()
    public static class AddedItemDetailAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.
         */
        @Child(name = "code", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc." )
        protected Coding code;

        /**
         * Monitory amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monitory amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monitory value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = -949880587L;

    /**
     * Constructor
     */
      public AddedItemDetailAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AddedItemDetailAdjudicationComponent(Coding code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public Coding getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailAdjudicationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Coding(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public AddedItemDetailAdjudicationComponent setCode(Coding value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monitory amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monitory amount associated with the code.)
         */
        public AddedItemDetailAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailAdjudicationComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new DecimalType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public AddedItemDetailAdjudicationComponent setValueElement(DecimalType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public BigDecimal getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AddedItemDetailAdjudicationComponent setValue(BigDecimal value) { 
          if (value == null)
            this.value = null;
          else {
            if (this.value == null)
              this.value = new DecimalType();
            this.value.setValue(value);
          }
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AddedItemDetailAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AddedItemDetailAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "Coding", "Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("amount", "Money", "Monitory amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
          childrenList.add(new Property("value", "decimal", "A non-monetary value for example a percentage. Mutually exclusive to the amount element above.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = castToCoding(value); // Coding
        else if (name.equals("amount"))
          this.amount = castToMoney(value); // Money
        else if (name.equals("value"))
          this.value = castToDecimal(value); // DecimalType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new Coding();
          return this.code;
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.value");
        }
        else
          return super.addChild(name);
      }

      public AddedItemDetailAdjudicationComponent copy() {
        AddedItemDetailAdjudicationComponent dst = new AddedItemDetailAdjudicationComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AddedItemDetailAdjudicationComponent))
          return false;
        AddedItemDetailAdjudicationComponent o = (AddedItemDetailAdjudicationComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(amount, o.amount, true) && compareDeep(value, o.value, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AddedItemDetailAdjudicationComponent))
          return false;
        AddedItemDetailAdjudicationComponent o = (AddedItemDetailAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (amount == null || amount.isEmpty())
           && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.addItem.detail.adjudication";

  }

  }

    @Block()
    public static class MissingTeethComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The code identifying which tooth is missing.
         */
        @Child(name = "tooth", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Tooth Code", formalDefinition="The code identifying which tooth is missing." )
        protected Coding tooth;

        /**
         * Missing reason may be: E-extraction, O-other.
         */
        @Child(name = "reason", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reason for missing", formalDefinition="Missing reason may be: E-extraction, O-other." )
        protected Coding reason;

        /**
         * The date of the extraction either known from records or patient reported estimate.
         */
        @Child(name = "extractionDate", type = {DateType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date of Extraction", formalDefinition="The date of the extraction either known from records or patient reported estimate." )
        protected DateType extractionDate;

        private static final long serialVersionUID = 352913313L;

    /**
     * Constructor
     */
      public MissingTeethComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MissingTeethComponent(Coding tooth) {
        super();
        this.tooth = tooth;
      }

        /**
         * @return {@link #tooth} (The code identifying which tooth is missing.)
         */
        public Coding getTooth() { 
          if (this.tooth == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MissingTeethComponent.tooth");
            else if (Configuration.doAutoCreate())
              this.tooth = new Coding(); // cc
          return this.tooth;
        }

        public boolean hasTooth() { 
          return this.tooth != null && !this.tooth.isEmpty();
        }

        /**
         * @param value {@link #tooth} (The code identifying which tooth is missing.)
         */
        public MissingTeethComponent setTooth(Coding value) { 
          this.tooth = value;
          return this;
        }

        /**
         * @return {@link #reason} (Missing reason may be: E-extraction, O-other.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MissingTeethComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new Coding(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Missing reason may be: E-extraction, O-other.)
         */
        public MissingTeethComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #extractionDate} (The date of the extraction either known from records or patient reported estimate.). This is the underlying object with id, value and extensions. The accessor "getExtractionDate" gives direct access to the value
         */
        public DateType getExtractionDateElement() { 
          if (this.extractionDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MissingTeethComponent.extractionDate");
            else if (Configuration.doAutoCreate())
              this.extractionDate = new DateType(); // bb
          return this.extractionDate;
        }

        public boolean hasExtractionDateElement() { 
          return this.extractionDate != null && !this.extractionDate.isEmpty();
        }

        public boolean hasExtractionDate() { 
          return this.extractionDate != null && !this.extractionDate.isEmpty();
        }

        /**
         * @param value {@link #extractionDate} (The date of the extraction either known from records or patient reported estimate.). This is the underlying object with id, value and extensions. The accessor "getExtractionDate" gives direct access to the value
         */
        public MissingTeethComponent setExtractionDateElement(DateType value) { 
          this.extractionDate = value;
          return this;
        }

        /**
         * @return The date of the extraction either known from records or patient reported estimate.
         */
        public Date getExtractionDate() { 
          return this.extractionDate == null ? null : this.extractionDate.getValue();
        }

        /**
         * @param value The date of the extraction either known from records or patient reported estimate.
         */
        public MissingTeethComponent setExtractionDate(Date value) { 
          if (value == null)
            this.extractionDate = null;
          else {
            if (this.extractionDate == null)
              this.extractionDate = new DateType();
            this.extractionDate.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("tooth", "Coding", "The code identifying which tooth is missing.", 0, java.lang.Integer.MAX_VALUE, tooth));
          childrenList.add(new Property("reason", "Coding", "Missing reason may be: E-extraction, O-other.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("extractionDate", "date", "The date of the extraction either known from records or patient reported estimate.", 0, java.lang.Integer.MAX_VALUE, extractionDate));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("tooth"))
          this.tooth = castToCoding(value); // Coding
        else if (name.equals("reason"))
          this.reason = castToCoding(value); // Coding
        else if (name.equals("extractionDate"))
          this.extractionDate = castToDate(value); // DateType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("tooth")) {
          this.tooth = new Coding();
          return this.tooth;
        }
        else if (name.equals("reason")) {
          this.reason = new Coding();
          return this.reason;
        }
        else if (name.equals("extractionDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.extractionDate");
        }
        else
          return super.addChild(name);
      }

      public MissingTeethComponent copy() {
        MissingTeethComponent dst = new MissingTeethComponent();
        copyValues(dst);
        dst.tooth = tooth == null ? null : tooth.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.extractionDate = extractionDate == null ? null : extractionDate.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MissingTeethComponent))
          return false;
        MissingTeethComponent o = (MissingTeethComponent) other;
        return compareDeep(tooth, o.tooth, true) && compareDeep(reason, o.reason, true) && compareDeep(extractionDate, o.extractionDate, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MissingTeethComponent))
          return false;
        MissingTeethComponent o = (MissingTeethComponent) other;
        return compareValues(extractionDate, o.extractionDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (tooth == null || tooth.isEmpty()) && (reason == null || reason.isEmpty())
           && (extractionDate == null || extractionDate.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.missingTeeth";

  }

  }

    @Block()
    public static class NotesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An integer associated with each note which may be referred to from each service line item.
         */
        @Child(name = "number", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Note Number for this note", formalDefinition="An integer associated with each note which may be referred to from each service line item." )
        protected PositiveIntType number;

        /**
         * The note purpose: Print/Display.
         */
        @Child(name = "type", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The note purpose: Print/Display." )
        protected Coding type;

        /**
         * The note text.
         */
        @Child(name = "text", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Note explanitory text", formalDefinition="The note text." )
        protected StringType text;

        private static final long serialVersionUID = 1768923951L;

    /**
     * Constructor
     */
      public NotesComponent() {
        super();
      }

        /**
         * @return {@link #number} (An integer associated with each note which may be referred to from each service line item.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public PositiveIntType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NotesComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new PositiveIntType(); // bb
          return this.number;
        }

        public boolean hasNumberElement() { 
          return this.number != null && !this.number.isEmpty();
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (An integer associated with each note which may be referred to from each service line item.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public NotesComponent setNumberElement(PositiveIntType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return An integer associated with each note which may be referred to from each service line item.
         */
        public int getNumber() { 
          return this.number == null || this.number.isEmpty() ? 0 : this.number.getValue();
        }

        /**
         * @param value An integer associated with each note which may be referred to from each service line item.
         */
        public NotesComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new PositiveIntType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The note purpose: Print/Display.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NotesComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The note purpose: Print/Display.)
         */
        public NotesComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #text} (The note text.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NotesComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (The note text.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public NotesComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return The note text.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value The note text.
         */
        public NotesComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("number", "positiveInt", "An integer associated with each note which may be referred to from each service line item.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("type", "Coding", "The note purpose: Print/Display.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("text", "string", "The note text.", 0, java.lang.Integer.MAX_VALUE, text));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("number"))
          this.number = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("text"))
          this.text = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.number");
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.text");
        }
        else
          return super.addChild(name);
      }

      public NotesComponent copy() {
        NotesComponent dst = new NotesComponent();
        copyValues(dst);
        dst.number = number == null ? null : number.copy();
        dst.type = type == null ? null : type.copy();
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NotesComponent))
          return false;
        NotesComponent o = (NotesComponent) other;
        return compareDeep(number, o.number, true) && compareDeep(type, o.type, true) && compareDeep(text, o.text, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NotesComponent))
          return false;
        NotesComponent o = (NotesComponent) other;
        return compareValues(number, o.number, true) && compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (number == null || number.isEmpty()) && (type == null || type.isEmpty())
           && (text == null || text.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.note";

  }

  }

    @Block()
    public static class BenefitBalanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Dental, Vision, Medical, Pharmacy, Rehab etc.
         */
        @Child(name = "category", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Benefit Category", formalDefinition="Dental, Vision, Medical, Pharmacy, Rehab etc." )
        protected Coding category;

        /**
         * Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.
         */
        @Child(name = "subCategory", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Benefit SubCategory", formalDefinition="Dental: basic, major, ortho; Vision exam, glasses, contacts; etc." )
        protected Coding subCategory;

        /**
         * Network designation.
         */
        @Child(name = "network", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="In or out of network", formalDefinition="Network designation." )
        protected Coding network;

        /**
         * Unit designation: individual or family.
         */
        @Child(name = "unit", type = {Coding.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Individual or family", formalDefinition="Unit designation: individual or family." )
        protected Coding unit;

        /**
         * The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.
         */
        @Child(name = "term", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Annual or lifetime", formalDefinition="The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'." )
        protected Coding term;

        /**
         * Benefits Used to date.
         */
        @Child(name = "financial", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Benefit Summary", formalDefinition="Benefits Used to date." )
        protected List<BenefitComponent> financial;

        private static final long serialVersionUID = 1708176773L;

    /**
     * Constructor
     */
      public BenefitBalanceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public BenefitBalanceComponent(Coding category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
         */
        public Coding getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Coding(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
         */
        public BenefitBalanceComponent setCategory(Coding value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #subCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
         */
        public Coding getSubCategory() { 
          if (this.subCategory == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.subCategory");
            else if (Configuration.doAutoCreate())
              this.subCategory = new Coding(); // cc
          return this.subCategory;
        }

        public boolean hasSubCategory() { 
          return this.subCategory != null && !this.subCategory.isEmpty();
        }

        /**
         * @param value {@link #subCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
         */
        public BenefitBalanceComponent setSubCategory(Coding value) { 
          this.subCategory = value;
          return this;
        }

        /**
         * @return {@link #network} (Network designation.)
         */
        public Coding getNetwork() { 
          if (this.network == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.network");
            else if (Configuration.doAutoCreate())
              this.network = new Coding(); // cc
          return this.network;
        }

        public boolean hasNetwork() { 
          return this.network != null && !this.network.isEmpty();
        }

        /**
         * @param value {@link #network} (Network designation.)
         */
        public BenefitBalanceComponent setNetwork(Coding value) { 
          this.network = value;
          return this;
        }

        /**
         * @return {@link #unit} (Unit designation: individual or family.)
         */
        public Coding getUnit() { 
          if (this.unit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.unit");
            else if (Configuration.doAutoCreate())
              this.unit = new Coding(); // cc
          return this.unit;
        }

        public boolean hasUnit() { 
          return this.unit != null && !this.unit.isEmpty();
        }

        /**
         * @param value {@link #unit} (Unit designation: individual or family.)
         */
        public BenefitBalanceComponent setUnit(Coding value) { 
          this.unit = value;
          return this;
        }

        /**
         * @return {@link #term} (The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.)
         */
        public Coding getTerm() { 
          if (this.term == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.term");
            else if (Configuration.doAutoCreate())
              this.term = new Coding(); // cc
          return this.term;
        }

        public boolean hasTerm() { 
          return this.term != null && !this.term.isEmpty();
        }

        /**
         * @param value {@link #term} (The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.)
         */
        public BenefitBalanceComponent setTerm(Coding value) { 
          this.term = value;
          return this;
        }

        /**
         * @return {@link #financial} (Benefits Used to date.)
         */
        public List<BenefitComponent> getFinancial() { 
          if (this.financial == null)
            this.financial = new ArrayList<BenefitComponent>();
          return this.financial;
        }

        public boolean hasFinancial() { 
          if (this.financial == null)
            return false;
          for (BenefitComponent item : this.financial)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #financial} (Benefits Used to date.)
         */
    // syntactic sugar
        public BenefitComponent addFinancial() { //3
          BenefitComponent t = new BenefitComponent();
          if (this.financial == null)
            this.financial = new ArrayList<BenefitComponent>();
          this.financial.add(t);
          return t;
        }

    // syntactic sugar
        public BenefitBalanceComponent addFinancial(BenefitComponent t) { //3
          if (t == null)
            return this;
          if (this.financial == null)
            this.financial = new ArrayList<BenefitComponent>();
          this.financial.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "Coding", "Dental, Vision, Medical, Pharmacy, Rehab etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("subCategory", "Coding", "Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.", 0, java.lang.Integer.MAX_VALUE, subCategory));
          childrenList.add(new Property("network", "Coding", "Network designation.", 0, java.lang.Integer.MAX_VALUE, network));
          childrenList.add(new Property("unit", "Coding", "Unit designation: individual or family.", 0, java.lang.Integer.MAX_VALUE, unit));
          childrenList.add(new Property("term", "Coding", "The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.", 0, java.lang.Integer.MAX_VALUE, term));
          childrenList.add(new Property("financial", "", "Benefits Used to date.", 0, java.lang.Integer.MAX_VALUE, financial));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category"))
          this.category = castToCoding(value); // Coding
        else if (name.equals("subCategory"))
          this.subCategory = castToCoding(value); // Coding
        else if (name.equals("network"))
          this.network = castToCoding(value); // Coding
        else if (name.equals("unit"))
          this.unit = castToCoding(value); // Coding
        else if (name.equals("term"))
          this.term = castToCoding(value); // Coding
        else if (name.equals("financial"))
          this.getFinancial().add((BenefitComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new Coding();
          return this.category;
        }
        else if (name.equals("subCategory")) {
          this.subCategory = new Coding();
          return this.subCategory;
        }
        else if (name.equals("network")) {
          this.network = new Coding();
          return this.network;
        }
        else if (name.equals("unit")) {
          this.unit = new Coding();
          return this.unit;
        }
        else if (name.equals("term")) {
          this.term = new Coding();
          return this.term;
        }
        else if (name.equals("financial")) {
          return addFinancial();
        }
        else
          return super.addChild(name);
      }

      public BenefitBalanceComponent copy() {
        BenefitBalanceComponent dst = new BenefitBalanceComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.subCategory = subCategory == null ? null : subCategory.copy();
        dst.network = network == null ? null : network.copy();
        dst.unit = unit == null ? null : unit.copy();
        dst.term = term == null ? null : term.copy();
        if (financial != null) {
          dst.financial = new ArrayList<BenefitComponent>();
          for (BenefitComponent i : financial)
            dst.financial.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BenefitBalanceComponent))
          return false;
        BenefitBalanceComponent o = (BenefitBalanceComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(subCategory, o.subCategory, true)
           && compareDeep(network, o.network, true) && compareDeep(unit, o.unit, true) && compareDeep(term, o.term, true)
           && compareDeep(financial, o.financial, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BenefitBalanceComponent))
          return false;
        BenefitBalanceComponent o = (BenefitBalanceComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (subCategory == null || subCategory.isEmpty())
           && (network == null || network.isEmpty()) && (unit == null || unit.isEmpty()) && (term == null || term.isEmpty())
           && (financial == null || financial.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.benefitBalance";

  }

  }

    @Block()
    public static class BenefitComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Deductable, visits, benefit amount.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Deductable, visits, benefit amount", formalDefinition="Deductable, visits, benefit amount." )
        protected Coding type;

        /**
         * Benefits allowed.
         */
        @Child(name = "benefit", type = {UnsignedIntType.class, Money.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Benefits allowed", formalDefinition="Benefits allowed." )
        protected Type benefit;

        /**
         * Benefits used.
         */
        @Child(name = "benefitUsed", type = {UnsignedIntType.class, Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Benefits used", formalDefinition="Benefits used." )
        protected Type benefitUsed;

        private static final long serialVersionUID = 1742418909L;

    /**
     * Constructor
     */
      public BenefitComponent() {
        super();
      }

    /**
     * Constructor
     */
      public BenefitComponent(Coding type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Deductable, visits, benefit amount.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Deductable, visits, benefit amount.)
         */
        public BenefitComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #benefit} (Benefits allowed.)
         */
        public Type getBenefit() { 
          return this.benefit;
        }

        /**
         * @return {@link #benefit} (Benefits allowed.)
         */
        public UnsignedIntType getBenefitUnsignedIntType() throws FHIRException { 
          if (!(this.benefit instanceof UnsignedIntType))
            throw new FHIRException("Type mismatch: the type UnsignedIntType was expected, but "+this.benefit.getClass().getName()+" was encountered");
          return (UnsignedIntType) this.benefit;
        }

        public boolean hasBenefitUnsignedIntType() { 
          return this.benefit instanceof UnsignedIntType;
        }

        /**
         * @return {@link #benefit} (Benefits allowed.)
         */
        public Money getBenefitMoney() throws FHIRException { 
          if (!(this.benefit instanceof Money))
            throw new FHIRException("Type mismatch: the type Money was expected, but "+this.benefit.getClass().getName()+" was encountered");
          return (Money) this.benefit;
        }

        public boolean hasBenefitMoney() { 
          return this.benefit instanceof Money;
        }

        public boolean hasBenefit() { 
          return this.benefit != null && !this.benefit.isEmpty();
        }

        /**
         * @param value {@link #benefit} (Benefits allowed.)
         */
        public BenefitComponent setBenefit(Type value) { 
          this.benefit = value;
          return this;
        }

        /**
         * @return {@link #benefitUsed} (Benefits used.)
         */
        public Type getBenefitUsed() { 
          return this.benefitUsed;
        }

        /**
         * @return {@link #benefitUsed} (Benefits used.)
         */
        public UnsignedIntType getBenefitUsedUnsignedIntType() throws FHIRException { 
          if (!(this.benefitUsed instanceof UnsignedIntType))
            throw new FHIRException("Type mismatch: the type UnsignedIntType was expected, but "+this.benefitUsed.getClass().getName()+" was encountered");
          return (UnsignedIntType) this.benefitUsed;
        }

        public boolean hasBenefitUsedUnsignedIntType() { 
          return this.benefitUsed instanceof UnsignedIntType;
        }

        /**
         * @return {@link #benefitUsed} (Benefits used.)
         */
        public Money getBenefitUsedMoney() throws FHIRException { 
          if (!(this.benefitUsed instanceof Money))
            throw new FHIRException("Type mismatch: the type Money was expected, but "+this.benefitUsed.getClass().getName()+" was encountered");
          return (Money) this.benefitUsed;
        }

        public boolean hasBenefitUsedMoney() { 
          return this.benefitUsed instanceof Money;
        }

        public boolean hasBenefitUsed() { 
          return this.benefitUsed != null && !this.benefitUsed.isEmpty();
        }

        /**
         * @param value {@link #benefitUsed} (Benefits used.)
         */
        public BenefitComponent setBenefitUsed(Type value) { 
          this.benefitUsed = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "Deductable, visits, benefit amount.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("benefit[x]", "unsignedInt|Money", "Benefits allowed.", 0, java.lang.Integer.MAX_VALUE, benefit));
          childrenList.add(new Property("benefitUsed[x]", "unsignedInt|Money", "Benefits used.", 0, java.lang.Integer.MAX_VALUE, benefitUsed));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("benefit[x]"))
          this.benefit = (Type) value; // Type
        else if (name.equals("benefitUsed[x]"))
          this.benefitUsed = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("benefitUnsignedInt")) {
          this.benefit = new UnsignedIntType();
          return this.benefit;
        }
        else if (name.equals("benefitMoney")) {
          this.benefit = new Money();
          return this.benefit;
        }
        else if (name.equals("benefitUsedUnsignedInt")) {
          this.benefitUsed = new UnsignedIntType();
          return this.benefitUsed;
        }
        else if (name.equals("benefitUsedMoney")) {
          this.benefitUsed = new Money();
          return this.benefitUsed;
        }
        else
          return super.addChild(name);
      }

      public BenefitComponent copy() {
        BenefitComponent dst = new BenefitComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.benefit = benefit == null ? null : benefit.copy();
        dst.benefitUsed = benefitUsed == null ? null : benefitUsed.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BenefitComponent))
          return false;
        BenefitComponent o = (BenefitComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(benefit, o.benefit, true) && compareDeep(benefitUsed, o.benefitUsed, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BenefitComponent))
          return false;
        BenefitComponent o = (BenefitComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (benefit == null || benefit.isEmpty())
           && (benefitUsed == null || benefitUsed.isEmpty());
      }

  public String fhirType() {
    return "ExplanationOfBenefit.benefitBalance.financial";

  }

  }

    /**
     * The Response Business Identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response Business Identifier." )
    protected List<Identifier> identifier;

    /**
     * The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.
     */
    @Child(name = "claim", type = {Claim.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Claim reference", formalDefinition="The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number." )
    protected Reference claim;

    /**
     * The actual object that is the target of the reference (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    protected Claim claimTarget;

    /**
     * The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.
     */
    @Child(name = "claimResponse", type = {ClaimResponse.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Claim response reference", formalDefinition="The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number." )
    protected Reference claimResponse;

    /**
     * The actual object that is the target of the reference (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    protected ClaimResponse claimResponseTarget;

    /**
     * The version of the specification on which this instance relies.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Current specification followed", formalDefinition="The version of the specification on which this instance relies." )
    protected Coding ruleset;

    /**
     * The version of the specification from which the original instance was created.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original specification followed", formalDefinition="The version of the specification from which the original instance was created." )
    protected Coding originalRuleset;

    /**
     * The date when the EOB was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the EOB was created." )
    protected DateTimeType created;

    /**
     * The billable period for which charges are being submitted.
     */
    @Child(name = "billablePeriod", type = {Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period for charge submission", formalDefinition="The billable period for which charges are being submitted." )
    protected Period billablePeriod;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * The provider which is responsible for the claim.
     */
    @Child(name = "provider", type = {Practitioner.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible provider for the claim", formalDefinition="The provider which is responsible for the claim." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The provider which is responsible for the claim.)
     */
    protected Practitioner providerTarget;

    /**
     * The provider which is responsible for the claim.
     */
    @Child(name = "organization", type = {Organization.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization for the claim", formalDefinition="The provider which is responsible for the claim." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The provider which is responsible for the claim.)
     */
    protected Organization organizationTarget;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Location.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Reference facility;

    /**
     * The actual object that is the target of the reference (Facility where the services were provided.)
     */
    protected Location facilityTarget;

    /**
     * Other claims which are related to this claim such as prior claim versions or for related services.
     */
    @Child(name = "relatedClaim", type = {Claim.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Related Claims", formalDefinition="Other claims which are related to this claim such as prior claim versions or for related services." )
    protected List<Reference> relatedClaim;
    /**
     * The actual objects that are the target of the reference (Other claims which are related to this claim such as prior claim versions or for related services.)
     */
    protected List<Claim> relatedClaimTarget;


    /**
     * Prescription to support the dispensing of Pharmacy or Vision products.
     */
    @Child(name = "prescription", type = {MedicationOrder.class, VisionPrescription.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Prescription", formalDefinition="Prescription to support the dispensing of Pharmacy or Vision products." )
    protected Reference prescription;

    /**
     * The actual object that is the target of the reference (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    protected Resource prescriptionTarget;

    /**
     * Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.
     */
    @Child(name = "originalPrescription", type = {MedicationOrder.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original Prescription", formalDefinition="Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products." )
    protected Reference originalPrescription;

    /**
     * The actual object that is the target of the reference (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    protected MedicationOrder originalPrescriptionTarget;

    /**
     * The party to be reimbursed for the services.
     */
    @Child(name = "payee", type = {}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payee", formalDefinition="The party to be reimbursed for the services." )
    protected PayeeComponent payee;

    /**
     * The referral resource which lists the date, practitioner, reason and other supporting information.
     */
    @Child(name = "referral", type = {ReferralRequest.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Treatment Referral", formalDefinition="The referral resource which lists the date, practitioner, reason and other supporting information." )
    protected Reference referral;

    /**
     * The actual object that is the target of the reference (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    protected ReferralRequest referralTarget;

    /**
     * Ordered list of patient diagnosis for which care is sought.
     */
    @Child(name = "diagnosis", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Diagnosis", formalDefinition="Ordered list of patient diagnosis for which care is sought." )
    protected List<DiagnosisComponent> diagnosis;

    /**
     * List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.
     */
    @Child(name = "specialCondition", type = {Coding.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="List of special Conditions", formalDefinition="List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication." )
    protected List<Coding> specialCondition;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Patient.class}, order=18, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient Resource.)
     */
    protected Patient patientTarget;

    /**
     * Precedence (primary, secondary, etc.).
     */
    @Child(name = "precedence", type = {PositiveIntType.class}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Precedence (primary, secondary, etc.)", formalDefinition="Precedence (primary, secondary, etc.)." )
    protected PositiveIntType precedence;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "coverage", type = {}, order=20, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected CoverageComponent coverage;

    /**
     * Factors which may influence the applicability of coverage.
     */
    @Child(name = "exception", type = {Coding.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Eligibility exceptions", formalDefinition="Factors which may influence the applicability of coverage." )
    protected List<Coding> exception;

    /**
     * Name of school for over-aged dependants.
     */
    @Child(name = "school", type = {StringType.class}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of School", formalDefinition="Name of school for over-aged dependants." )
    protected StringType school;

    /**
     * Date of an accident which these services are addressing.
     */
    @Child(name = "accidentDate", type = {DateType.class}, order=23, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Accident Date", formalDefinition="Date of an accident which these services are addressing." )
    protected DateType accidentDate;

    /**
     * Type of accident: work, auto, etc.
     */
    @Child(name = "accidentType", type = {Coding.class}, order=24, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Accident Type", formalDefinition="Type of accident: work, auto, etc." )
    protected Coding accidentType;

    /**
     * Accident Place.
     */
    @Child(name = "accidentLocation", type = {StringType.class, Address.class, Location.class}, order=25, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Accident Place", formalDefinition="Accident Place." )
    protected Type accidentLocation;

    /**
     * A list of intervention and exception codes which may influence the adjudication of the claim.
     */
    @Child(name = "interventionException", type = {Coding.class}, order=26, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Intervention and exception code (Pharma)", formalDefinition="A list of intervention and exception codes which may influence the adjudication of the claim." )
    protected List<Coding> interventionException;

    /**
     * The start or start and end dates for the treatable condition.
     */
    @Child(name = "onset", type = {DateType.class, Period.class}, order=27, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Illness, injury or treatable condition date", formalDefinition="The start or start and end dates for the treatable condition." )
    protected Type onset;

    /**
     * The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).
     */
    @Child(name = "employmentImpacted", type = {Period.class}, order=28, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period unable to work", formalDefinition="The start and optional end dates of when the patient was precluded from working due to the treatable condition(s)." )
    protected Period employmentImpacted;

    /**
     * The start and optional end dates of when the patient was confined to a treatment center.
     */
    @Child(name = "hospitalization", type = {Period.class}, order=29, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period in hospital", formalDefinition="The start and optional end dates of when the patient was confined to a treatment center." )
    protected Period hospitalization;

    /**
     * First tier of goods and services.
     */
    @Child(name = "item", type = {}, order=30, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Goods and Services", formalDefinition="First tier of goods and services." )
    protected List<ItemsComponent> item;

    /**
     * The first tier service adjudications for payor added services.
     */
    @Child(name = "addItem", type = {}, order=31, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Insurer added line items", formalDefinition="The first tier service adjudications for payor added services." )
    protected List<AddedItemComponent> addItem;

    /**
     * The total value of the claim.
     */
    @Child(name = "claimTotal", type = {Money.class}, order=32, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total claim cost", formalDefinition="The total value of the claim." )
    protected Money claimTotal;

    /**
     * A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.
     */
    @Child(name = "missingTeeth", type = {}, order=33, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Only if type = oral", formalDefinition="A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons." )
    protected List<MissingTeethComponent> missingTeeth;

    /**
     * The amount of deductable applied which was not allocated to any particular service line.
     */
    @Child(name = "unallocDeductable", type = {Money.class}, order=34, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unallocated deductable", formalDefinition="The amount of deductable applied which was not allocated to any particular service line." )
    protected Money unallocDeductable;

    /**
     * Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable).
     */
    @Child(name = "totalBenefit", type = {Money.class}, order=35, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total benefit payable for the Claim", formalDefinition="Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable)." )
    protected Money totalBenefit;

    /**
     * Adjustment to the payment of this transaction which is not related to adjudication of this transaction.
     */
    @Child(name = "paymentAdjustment", type = {Money.class}, order=36, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment adjustment for non-Claim issues", formalDefinition="Adjustment to the payment of this transaction which is not related to adjudication of this transaction." )
    protected Money paymentAdjustment;

    /**
     * Reason for the payment adjustment.
     */
    @Child(name = "paymentAdjustmentReason", type = {Coding.class}, order=37, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for Payment adjustment", formalDefinition="Reason for the payment adjustment." )
    protected Coding paymentAdjustmentReason;

    /**
     * Estimated payment data.
     */
    @Child(name = "paymentDate", type = {DateType.class}, order=38, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Expected data of Payment", formalDefinition="Estimated payment data." )
    protected DateType paymentDate;

    /**
     * Payable less any payment adjustment.
     */
    @Child(name = "paymentAmount", type = {Money.class}, order=39, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment amount", formalDefinition="Payable less any payment adjustment." )
    protected Money paymentAmount;

    /**
     * Payment identifer.
     */
    @Child(name = "paymentRef", type = {Identifier.class}, order=40, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment identifier", formalDefinition="Payment identifer." )
    protected Identifier paymentRef;

    /**
     * Status of funds reservation (For provider, for Patient, None).
     */
    @Child(name = "reserved", type = {Coding.class}, order=41, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Funds reserved status", formalDefinition="Status of funds reservation (For provider, for Patient, None)." )
    protected Coding reserved;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {Coding.class}, order=42, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    protected Coding form;

    /**
     * Note text.
     */
    @Child(name = "note", type = {}, order=43, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Processing notes", formalDefinition="Note text." )
    protected List<NotesComponent> note;

    /**
     * Balance by Benefit Category.
     */
    @Child(name = "benefitBalance", type = {}, order=44, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Balance by Benefit Category", formalDefinition="Balance by Benefit Category." )
    protected List<BenefitBalanceComponent> benefitBalance;

    private static final long serialVersionUID = -1647900445L;

  /**
   * Constructor
   */
    public ExplanationOfBenefit() {
      super();
    }

  /**
   * Constructor
   */
    public ExplanationOfBenefit(Reference patient, CoverageComponent coverage) {
      super();
      this.patient = patient;
      this.coverage = coverage;
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

    // syntactic sugar
    public ExplanationOfBenefit addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #claim} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public Reference getClaim() { 
      if (this.claim == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.claim");
        else if (Configuration.doAutoCreate())
          this.claim = new Reference(); // cc
      return this.claim;
    }

    public boolean hasClaim() { 
      return this.claim != null && !this.claim.isEmpty();
    }

    /**
     * @param value {@link #claim} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public ExplanationOfBenefit setClaim(Reference value) { 
      this.claim = value;
      return this;
    }

    /**
     * @return {@link #claim} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public Claim getClaimTarget() { 
      if (this.claimTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.claim");
        else if (Configuration.doAutoCreate())
          this.claimTarget = new Claim(); // aa
      return this.claimTarget;
    }

    /**
     * @param value {@link #claim} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public ExplanationOfBenefit setClaimTarget(Claim value) { 
      this.claimTarget = value;
      return this;
    }

    /**
     * @return {@link #claimResponse} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public Reference getClaimResponse() { 
      if (this.claimResponse == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.claimResponse");
        else if (Configuration.doAutoCreate())
          this.claimResponse = new Reference(); // cc
      return this.claimResponse;
    }

    public boolean hasClaimResponse() { 
      return this.claimResponse != null && !this.claimResponse.isEmpty();
    }

    /**
     * @param value {@link #claimResponse} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public ExplanationOfBenefit setClaimResponse(Reference value) { 
      this.claimResponse = value;
      return this;
    }

    /**
     * @return {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public ClaimResponse getClaimResponseTarget() { 
      if (this.claimResponseTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.claimResponse");
        else if (Configuration.doAutoCreate())
          this.claimResponseTarget = new ClaimResponse(); // aa
      return this.claimResponseTarget;
    }

    /**
     * @param value {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public ExplanationOfBenefit setClaimResponseTarget(ClaimResponse value) { 
      this.claimResponseTarget = value;
      return this;
    }

    /**
     * @return {@link #ruleset} (The version of the specification on which this instance relies.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.ruleset");
        else if (Configuration.doAutoCreate())
          this.ruleset = new Coding(); // cc
      return this.ruleset;
    }

    public boolean hasRuleset() { 
      return this.ruleset != null && !this.ruleset.isEmpty();
    }

    /**
     * @param value {@link #ruleset} (The version of the specification on which this instance relies.)
     */
    public ExplanationOfBenefit setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The version of the specification from which the original instance was created.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.originalRuleset");
        else if (Configuration.doAutoCreate())
          this.originalRuleset = new Coding(); // cc
      return this.originalRuleset;
    }

    public boolean hasOriginalRuleset() { 
      return this.originalRuleset != null && !this.originalRuleset.isEmpty();
    }

    /**
     * @param value {@link #originalRuleset} (The version of the specification from which the original instance was created.)
     */
    public ExplanationOfBenefit setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when the EOB was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.created");
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
     * @param value {@link #created} (The date when the EOB was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public ExplanationOfBenefit setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when the EOB was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when the EOB was created.
     */
    public ExplanationOfBenefit setCreated(Date value) { 
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
     * @return {@link #billablePeriod} (The billable period for which charges are being submitted.)
     */
    public Period getBillablePeriod() { 
      if (this.billablePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.billablePeriod");
        else if (Configuration.doAutoCreate())
          this.billablePeriod = new Period(); // cc
      return this.billablePeriod;
    }

    public boolean hasBillablePeriod() { 
      return this.billablePeriod != null && !this.billablePeriod.isEmpty();
    }

    /**
     * @param value {@link #billablePeriod} (The billable period for which charges are being submitted.)
     */
    public ExplanationOfBenefit setBillablePeriod(Period value) { 
      this.billablePeriod = value;
      return this;
    }

    /**
     * @return {@link #disposition} (A description of the status of the adjudication.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public StringType getDispositionElement() { 
      if (this.disposition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.disposition");
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
    public ExplanationOfBenefit setDispositionElement(StringType value) { 
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
    public ExplanationOfBenefit setDisposition(String value) { 
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
     * @return {@link #provider} (The provider which is responsible for the claim.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.provider");
        else if (Configuration.doAutoCreate())
          this.provider = new Reference(); // cc
      return this.provider;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The provider which is responsible for the claim.)
     */
    public ExplanationOfBenefit setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the claim.)
     */
    public Practitioner getProviderTarget() { 
      if (this.providerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.provider");
        else if (Configuration.doAutoCreate())
          this.providerTarget = new Practitioner(); // aa
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the claim.)
     */
    public ExplanationOfBenefit setProviderTarget(Practitioner value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #organization} (The provider which is responsible for the claim.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The provider which is responsible for the claim.)
     */
    public ExplanationOfBenefit setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the claim.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the claim.)
     */
    public ExplanationOfBenefit setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Reference getFacility() { 
      if (this.facility == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.facility");
        else if (Configuration.doAutoCreate())
          this.facility = new Reference(); // cc
      return this.facility;
    }

    public boolean hasFacility() { 
      return this.facility != null && !this.facility.isEmpty();
    }

    /**
     * @param value {@link #facility} (Facility where the services were provided.)
     */
    public ExplanationOfBenefit setFacility(Reference value) { 
      this.facility = value;
      return this;
    }

    /**
     * @return {@link #facility} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public Location getFacilityTarget() { 
      if (this.facilityTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.facility");
        else if (Configuration.doAutoCreate())
          this.facilityTarget = new Location(); // aa
      return this.facilityTarget;
    }

    /**
     * @param value {@link #facility} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public ExplanationOfBenefit setFacilityTarget(Location value) { 
      this.facilityTarget = value;
      return this;
    }

    /**
     * @return {@link #relatedClaim} (Other claims which are related to this claim such as prior claim versions or for related services.)
     */
    public List<Reference> getRelatedClaim() { 
      if (this.relatedClaim == null)
        this.relatedClaim = new ArrayList<Reference>();
      return this.relatedClaim;
    }

    public boolean hasRelatedClaim() { 
      if (this.relatedClaim == null)
        return false;
      for (Reference item : this.relatedClaim)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #relatedClaim} (Other claims which are related to this claim such as prior claim versions or for related services.)
     */
    // syntactic sugar
    public Reference addRelatedClaim() { //3
      Reference t = new Reference();
      if (this.relatedClaim == null)
        this.relatedClaim = new ArrayList<Reference>();
      this.relatedClaim.add(t);
      return t;
    }

    // syntactic sugar
    public ExplanationOfBenefit addRelatedClaim(Reference t) { //3
      if (t == null)
        return this;
      if (this.relatedClaim == null)
        this.relatedClaim = new ArrayList<Reference>();
      this.relatedClaim.add(t);
      return this;
    }

    /**
     * @return {@link #relatedClaim} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Other claims which are related to this claim such as prior claim versions or for related services.)
     */
    public List<Claim> getRelatedClaimTarget() { 
      if (this.relatedClaimTarget == null)
        this.relatedClaimTarget = new ArrayList<Claim>();
      return this.relatedClaimTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #relatedClaim} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Other claims which are related to this claim such as prior claim versions or for related services.)
     */
    public Claim addRelatedClaimTarget() { 
      Claim r = new Claim();
      if (this.relatedClaimTarget == null)
        this.relatedClaimTarget = new ArrayList<Claim>();
      this.relatedClaimTarget.add(r);
      return r;
    }

    /**
     * @return {@link #prescription} (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    public Reference getPrescription() { 
      if (this.prescription == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.prescription");
        else if (Configuration.doAutoCreate())
          this.prescription = new Reference(); // cc
      return this.prescription;
    }

    public boolean hasPrescription() { 
      return this.prescription != null && !this.prescription.isEmpty();
    }

    /**
     * @param value {@link #prescription} (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    public ExplanationOfBenefit setPrescription(Reference value) { 
      this.prescription = value;
      return this;
    }

    /**
     * @return {@link #prescription} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    public Resource getPrescriptionTarget() { 
      return this.prescriptionTarget;
    }

    /**
     * @param value {@link #prescription} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    public ExplanationOfBenefit setPrescriptionTarget(Resource value) { 
      this.prescriptionTarget = value;
      return this;
    }

    /**
     * @return {@link #originalPrescription} (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Reference getOriginalPrescription() { 
      if (this.originalPrescription == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.originalPrescription");
        else if (Configuration.doAutoCreate())
          this.originalPrescription = new Reference(); // cc
      return this.originalPrescription;
    }

    public boolean hasOriginalPrescription() { 
      return this.originalPrescription != null && !this.originalPrescription.isEmpty();
    }

    /**
     * @param value {@link #originalPrescription} (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public ExplanationOfBenefit setOriginalPrescription(Reference value) { 
      this.originalPrescription = value;
      return this;
    }

    /**
     * @return {@link #originalPrescription} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public MedicationOrder getOriginalPrescriptionTarget() { 
      if (this.originalPrescriptionTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.originalPrescription");
        else if (Configuration.doAutoCreate())
          this.originalPrescriptionTarget = new MedicationOrder(); // aa
      return this.originalPrescriptionTarget;
    }

    /**
     * @param value {@link #originalPrescription} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public ExplanationOfBenefit setOriginalPrescriptionTarget(MedicationOrder value) { 
      this.originalPrescriptionTarget = value;
      return this;
    }

    /**
     * @return {@link #payee} (The party to be reimbursed for the services.)
     */
    public PayeeComponent getPayee() { 
      if (this.payee == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.payee");
        else if (Configuration.doAutoCreate())
          this.payee = new PayeeComponent(); // cc
      return this.payee;
    }

    public boolean hasPayee() { 
      return this.payee != null && !this.payee.isEmpty();
    }

    /**
     * @param value {@link #payee} (The party to be reimbursed for the services.)
     */
    public ExplanationOfBenefit setPayee(PayeeComponent value) { 
      this.payee = value;
      return this;
    }

    /**
     * @return {@link #referral} (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public Reference getReferral() { 
      if (this.referral == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.referral");
        else if (Configuration.doAutoCreate())
          this.referral = new Reference(); // cc
      return this.referral;
    }

    public boolean hasReferral() { 
      return this.referral != null && !this.referral.isEmpty();
    }

    /**
     * @param value {@link #referral} (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public ExplanationOfBenefit setReferral(Reference value) { 
      this.referral = value;
      return this;
    }

    /**
     * @return {@link #referral} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public ReferralRequest getReferralTarget() { 
      if (this.referralTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.referral");
        else if (Configuration.doAutoCreate())
          this.referralTarget = new ReferralRequest(); // aa
      return this.referralTarget;
    }

    /**
     * @param value {@link #referral} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public ExplanationOfBenefit setReferralTarget(ReferralRequest value) { 
      this.referralTarget = value;
      return this;
    }

    /**
     * @return {@link #diagnosis} (Ordered list of patient diagnosis for which care is sought.)
     */
    public List<DiagnosisComponent> getDiagnosis() { 
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      return this.diagnosis;
    }

    public boolean hasDiagnosis() { 
      if (this.diagnosis == null)
        return false;
      for (DiagnosisComponent item : this.diagnosis)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #diagnosis} (Ordered list of patient diagnosis for which care is sought.)
     */
    // syntactic sugar
    public DiagnosisComponent addDiagnosis() { //3
      DiagnosisComponent t = new DiagnosisComponent();
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      this.diagnosis.add(t);
      return t;
    }

    // syntactic sugar
    public ExplanationOfBenefit addDiagnosis(DiagnosisComponent t) { //3
      if (t == null)
        return this;
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      this.diagnosis.add(t);
      return this;
    }

    /**
     * @return {@link #specialCondition} (List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.)
     */
    public List<Coding> getSpecialCondition() { 
      if (this.specialCondition == null)
        this.specialCondition = new ArrayList<Coding>();
      return this.specialCondition;
    }

    public boolean hasSpecialCondition() { 
      if (this.specialCondition == null)
        return false;
      for (Coding item : this.specialCondition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #specialCondition} (List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.)
     */
    // syntactic sugar
    public Coding addSpecialCondition() { //3
      Coding t = new Coding();
      if (this.specialCondition == null)
        this.specialCondition = new ArrayList<Coding>();
      this.specialCondition.add(t);
      return t;
    }

    // syntactic sugar
    public ExplanationOfBenefit addSpecialCondition(Coding t) { //3
      if (t == null)
        return this;
      if (this.specialCondition == null)
        this.specialCondition = new ArrayList<Coding>();
      this.specialCondition.add(t);
      return this;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Patient Resource.)
     */
    public ExplanationOfBenefit setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public ExplanationOfBenefit setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #precedence} (Precedence (primary, secondary, etc.).). This is the underlying object with id, value and extensions. The accessor "getPrecedence" gives direct access to the value
     */
    public PositiveIntType getPrecedenceElement() { 
      if (this.precedence == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.precedence");
        else if (Configuration.doAutoCreate())
          this.precedence = new PositiveIntType(); // bb
      return this.precedence;
    }

    public boolean hasPrecedenceElement() { 
      return this.precedence != null && !this.precedence.isEmpty();
    }

    public boolean hasPrecedence() { 
      return this.precedence != null && !this.precedence.isEmpty();
    }

    /**
     * @param value {@link #precedence} (Precedence (primary, secondary, etc.).). This is the underlying object with id, value and extensions. The accessor "getPrecedence" gives direct access to the value
     */
    public ExplanationOfBenefit setPrecedenceElement(PositiveIntType value) { 
      this.precedence = value;
      return this;
    }

    /**
     * @return Precedence (primary, secondary, etc.).
     */
    public int getPrecedence() { 
      return this.precedence == null || this.precedence.isEmpty() ? 0 : this.precedence.getValue();
    }

    /**
     * @param value Precedence (primary, secondary, etc.).
     */
    public ExplanationOfBenefit setPrecedence(int value) { 
        if (this.precedence == null)
          this.precedence = new PositiveIntType();
        this.precedence.setValue(value);
      return this;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public CoverageComponent getCoverage() { 
      if (this.coverage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.coverage");
        else if (Configuration.doAutoCreate())
          this.coverage = new CoverageComponent(); // cc
      return this.coverage;
    }

    public boolean hasCoverage() { 
      return this.coverage != null && !this.coverage.isEmpty();
    }

    /**
     * @param value {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public ExplanationOfBenefit setCoverage(CoverageComponent value) { 
      this.coverage = value;
      return this;
    }

    /**
     * @return {@link #exception} (Factors which may influence the applicability of coverage.)
     */
    public List<Coding> getException() { 
      if (this.exception == null)
        this.exception = new ArrayList<Coding>();
      return this.exception;
    }

    public boolean hasException() { 
      if (this.exception == null)
        return false;
      for (Coding item : this.exception)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #exception} (Factors which may influence the applicability of coverage.)
     */
    // syntactic sugar
    public Coding addException() { //3
      Coding t = new Coding();
      if (this.exception == null)
        this.exception = new ArrayList<Coding>();
      this.exception.add(t);
      return t;
    }

    // syntactic sugar
    public ExplanationOfBenefit addException(Coding t) { //3
      if (t == null)
        return this;
      if (this.exception == null)
        this.exception = new ArrayList<Coding>();
      this.exception.add(t);
      return this;
    }

    /**
     * @return {@link #school} (Name of school for over-aged dependants.). This is the underlying object with id, value and extensions. The accessor "getSchool" gives direct access to the value
     */
    public StringType getSchoolElement() { 
      if (this.school == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.school");
        else if (Configuration.doAutoCreate())
          this.school = new StringType(); // bb
      return this.school;
    }

    public boolean hasSchoolElement() { 
      return this.school != null && !this.school.isEmpty();
    }

    public boolean hasSchool() { 
      return this.school != null && !this.school.isEmpty();
    }

    /**
     * @param value {@link #school} (Name of school for over-aged dependants.). This is the underlying object with id, value and extensions. The accessor "getSchool" gives direct access to the value
     */
    public ExplanationOfBenefit setSchoolElement(StringType value) { 
      this.school = value;
      return this;
    }

    /**
     * @return Name of school for over-aged dependants.
     */
    public String getSchool() { 
      return this.school == null ? null : this.school.getValue();
    }

    /**
     * @param value Name of school for over-aged dependants.
     */
    public ExplanationOfBenefit setSchool(String value) { 
      if (Utilities.noString(value))
        this.school = null;
      else {
        if (this.school == null)
          this.school = new StringType();
        this.school.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #accidentDate} (Date of an accident which these services are addressing.). This is the underlying object with id, value and extensions. The accessor "getAccidentDate" gives direct access to the value
     */
    public DateType getAccidentDateElement() { 
      if (this.accidentDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.accidentDate");
        else if (Configuration.doAutoCreate())
          this.accidentDate = new DateType(); // bb
      return this.accidentDate;
    }

    public boolean hasAccidentDateElement() { 
      return this.accidentDate != null && !this.accidentDate.isEmpty();
    }

    public boolean hasAccidentDate() { 
      return this.accidentDate != null && !this.accidentDate.isEmpty();
    }

    /**
     * @param value {@link #accidentDate} (Date of an accident which these services are addressing.). This is the underlying object with id, value and extensions. The accessor "getAccidentDate" gives direct access to the value
     */
    public ExplanationOfBenefit setAccidentDateElement(DateType value) { 
      this.accidentDate = value;
      return this;
    }

    /**
     * @return Date of an accident which these services are addressing.
     */
    public Date getAccidentDate() { 
      return this.accidentDate == null ? null : this.accidentDate.getValue();
    }

    /**
     * @param value Date of an accident which these services are addressing.
     */
    public ExplanationOfBenefit setAccidentDate(Date value) { 
      if (value == null)
        this.accidentDate = null;
      else {
        if (this.accidentDate == null)
          this.accidentDate = new DateType();
        this.accidentDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #accidentType} (Type of accident: work, auto, etc.)
     */
    public Coding getAccidentType() { 
      if (this.accidentType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.accidentType");
        else if (Configuration.doAutoCreate())
          this.accidentType = new Coding(); // cc
      return this.accidentType;
    }

    public boolean hasAccidentType() { 
      return this.accidentType != null && !this.accidentType.isEmpty();
    }

    /**
     * @param value {@link #accidentType} (Type of accident: work, auto, etc.)
     */
    public ExplanationOfBenefit setAccidentType(Coding value) { 
      this.accidentType = value;
      return this;
    }

    /**
     * @return {@link #accidentLocation} (Accident Place.)
     */
    public Type getAccidentLocation() { 
      return this.accidentLocation;
    }

    /**
     * @return {@link #accidentLocation} (Accident Place.)
     */
    public StringType getAccidentLocationStringType() throws FHIRException { 
      if (!(this.accidentLocation instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.accidentLocation.getClass().getName()+" was encountered");
      return (StringType) this.accidentLocation;
    }

    public boolean hasAccidentLocationStringType() { 
      return this.accidentLocation instanceof StringType;
    }

    /**
     * @return {@link #accidentLocation} (Accident Place.)
     */
    public Address getAccidentLocationAddress() throws FHIRException { 
      if (!(this.accidentLocation instanceof Address))
        throw new FHIRException("Type mismatch: the type Address was expected, but "+this.accidentLocation.getClass().getName()+" was encountered");
      return (Address) this.accidentLocation;
    }

    public boolean hasAccidentLocationAddress() { 
      return this.accidentLocation instanceof Address;
    }

    /**
     * @return {@link #accidentLocation} (Accident Place.)
     */
    public Reference getAccidentLocationReference() throws FHIRException { 
      if (!(this.accidentLocation instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.accidentLocation.getClass().getName()+" was encountered");
      return (Reference) this.accidentLocation;
    }

    public boolean hasAccidentLocationReference() { 
      return this.accidentLocation instanceof Reference;
    }

    public boolean hasAccidentLocation() { 
      return this.accidentLocation != null && !this.accidentLocation.isEmpty();
    }

    /**
     * @param value {@link #accidentLocation} (Accident Place.)
     */
    public ExplanationOfBenefit setAccidentLocation(Type value) { 
      this.accidentLocation = value;
      return this;
    }

    /**
     * @return {@link #interventionException} (A list of intervention and exception codes which may influence the adjudication of the claim.)
     */
    public List<Coding> getInterventionException() { 
      if (this.interventionException == null)
        this.interventionException = new ArrayList<Coding>();
      return this.interventionException;
    }

    public boolean hasInterventionException() { 
      if (this.interventionException == null)
        return false;
      for (Coding item : this.interventionException)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #interventionException} (A list of intervention and exception codes which may influence the adjudication of the claim.)
     */
    // syntactic sugar
    public Coding addInterventionException() { //3
      Coding t = new Coding();
      if (this.interventionException == null)
        this.interventionException = new ArrayList<Coding>();
      this.interventionException.add(t);
      return t;
    }

    // syntactic sugar
    public ExplanationOfBenefit addInterventionException(Coding t) { //3
      if (t == null)
        return this;
      if (this.interventionException == null)
        this.interventionException = new ArrayList<Coding>();
      this.interventionException.add(t);
      return this;
    }

    /**
     * @return {@link #onset} (The start or start and end dates for the treatable condition.)
     */
    public Type getOnset() { 
      return this.onset;
    }

    /**
     * @return {@link #onset} (The start or start and end dates for the treatable condition.)
     */
    public DateType getOnsetDateType() throws FHIRException { 
      if (!(this.onset instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (DateType) this.onset;
    }

    public boolean hasOnsetDateType() { 
      return this.onset instanceof DateType;
    }

    /**
     * @return {@link #onset} (The start or start and end dates for the treatable condition.)
     */
    public Period getOnsetPeriod() throws FHIRException { 
      if (!(this.onset instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Period) this.onset;
    }

    public boolean hasOnsetPeriod() { 
      return this.onset instanceof Period;
    }

    public boolean hasOnset() { 
      return this.onset != null && !this.onset.isEmpty();
    }

    /**
     * @param value {@link #onset} (The start or start and end dates for the treatable condition.)
     */
    public ExplanationOfBenefit setOnset(Type value) { 
      this.onset = value;
      return this;
    }

    /**
     * @return {@link #employmentImpacted} (The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).)
     */
    public Period getEmploymentImpacted() { 
      if (this.employmentImpacted == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.employmentImpacted");
        else if (Configuration.doAutoCreate())
          this.employmentImpacted = new Period(); // cc
      return this.employmentImpacted;
    }

    public boolean hasEmploymentImpacted() { 
      return this.employmentImpacted != null && !this.employmentImpacted.isEmpty();
    }

    /**
     * @param value {@link #employmentImpacted} (The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).)
     */
    public ExplanationOfBenefit setEmploymentImpacted(Period value) { 
      this.employmentImpacted = value;
      return this;
    }

    /**
     * @return {@link #hospitalization} (The start and optional end dates of when the patient was confined to a treatment center.)
     */
    public Period getHospitalization() { 
      if (this.hospitalization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.hospitalization");
        else if (Configuration.doAutoCreate())
          this.hospitalization = new Period(); // cc
      return this.hospitalization;
    }

    public boolean hasHospitalization() { 
      return this.hospitalization != null && !this.hospitalization.isEmpty();
    }

    /**
     * @param value {@link #hospitalization} (The start and optional end dates of when the patient was confined to a treatment center.)
     */
    public ExplanationOfBenefit setHospitalization(Period value) { 
      this.hospitalization = value;
      return this;
    }

    /**
     * @return {@link #item} (First tier of goods and services.)
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
     * @return {@link #item} (First tier of goods and services.)
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
    public ExplanationOfBenefit addItem(ItemsComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return {@link #addItem} (The first tier service adjudications for payor added services.)
     */
    public List<AddedItemComponent> getAddItem() { 
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      return this.addItem;
    }

    public boolean hasAddItem() { 
      if (this.addItem == null)
        return false;
      for (AddedItemComponent item : this.addItem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #addItem} (The first tier service adjudications for payor added services.)
     */
    // syntactic sugar
    public AddedItemComponent addAddItem() { //3
      AddedItemComponent t = new AddedItemComponent();
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      this.addItem.add(t);
      return t;
    }

    // syntactic sugar
    public ExplanationOfBenefit addAddItem(AddedItemComponent t) { //3
      if (t == null)
        return this;
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      this.addItem.add(t);
      return this;
    }

    /**
     * @return {@link #claimTotal} (The total value of the claim.)
     */
    public Money getClaimTotal() { 
      if (this.claimTotal == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.claimTotal");
        else if (Configuration.doAutoCreate())
          this.claimTotal = new Money(); // cc
      return this.claimTotal;
    }

    public boolean hasClaimTotal() { 
      return this.claimTotal != null && !this.claimTotal.isEmpty();
    }

    /**
     * @param value {@link #claimTotal} (The total value of the claim.)
     */
    public ExplanationOfBenefit setClaimTotal(Money value) { 
      this.claimTotal = value;
      return this;
    }

    /**
     * @return {@link #missingTeeth} (A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.)
     */
    public List<MissingTeethComponent> getMissingTeeth() { 
      if (this.missingTeeth == null)
        this.missingTeeth = new ArrayList<MissingTeethComponent>();
      return this.missingTeeth;
    }

    public boolean hasMissingTeeth() { 
      if (this.missingTeeth == null)
        return false;
      for (MissingTeethComponent item : this.missingTeeth)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #missingTeeth} (A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.)
     */
    // syntactic sugar
    public MissingTeethComponent addMissingTeeth() { //3
      MissingTeethComponent t = new MissingTeethComponent();
      if (this.missingTeeth == null)
        this.missingTeeth = new ArrayList<MissingTeethComponent>();
      this.missingTeeth.add(t);
      return t;
    }

    // syntactic sugar
    public ExplanationOfBenefit addMissingTeeth(MissingTeethComponent t) { //3
      if (t == null)
        return this;
      if (this.missingTeeth == null)
        this.missingTeeth = new ArrayList<MissingTeethComponent>();
      this.missingTeeth.add(t);
      return this;
    }

    /**
     * @return {@link #unallocDeductable} (The amount of deductable applied which was not allocated to any particular service line.)
     */
    public Money getUnallocDeductable() { 
      if (this.unallocDeductable == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.unallocDeductable");
        else if (Configuration.doAutoCreate())
          this.unallocDeductable = new Money(); // cc
      return this.unallocDeductable;
    }

    public boolean hasUnallocDeductable() { 
      return this.unallocDeductable != null && !this.unallocDeductable.isEmpty();
    }

    /**
     * @param value {@link #unallocDeductable} (The amount of deductable applied which was not allocated to any particular service line.)
     */
    public ExplanationOfBenefit setUnallocDeductable(Money value) { 
      this.unallocDeductable = value;
      return this;
    }

    /**
     * @return {@link #totalBenefit} (Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable).)
     */
    public Money getTotalBenefit() { 
      if (this.totalBenefit == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.totalBenefit");
        else if (Configuration.doAutoCreate())
          this.totalBenefit = new Money(); // cc
      return this.totalBenefit;
    }

    public boolean hasTotalBenefit() { 
      return this.totalBenefit != null && !this.totalBenefit.isEmpty();
    }

    /**
     * @param value {@link #totalBenefit} (Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable).)
     */
    public ExplanationOfBenefit setTotalBenefit(Money value) { 
      this.totalBenefit = value;
      return this;
    }

    /**
     * @return {@link #paymentAdjustment} (Adjustment to the payment of this transaction which is not related to adjudication of this transaction.)
     */
    public Money getPaymentAdjustment() { 
      if (this.paymentAdjustment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.paymentAdjustment");
        else if (Configuration.doAutoCreate())
          this.paymentAdjustment = new Money(); // cc
      return this.paymentAdjustment;
    }

    public boolean hasPaymentAdjustment() { 
      return this.paymentAdjustment != null && !this.paymentAdjustment.isEmpty();
    }

    /**
     * @param value {@link #paymentAdjustment} (Adjustment to the payment of this transaction which is not related to adjudication of this transaction.)
     */
    public ExplanationOfBenefit setPaymentAdjustment(Money value) { 
      this.paymentAdjustment = value;
      return this;
    }

    /**
     * @return {@link #paymentAdjustmentReason} (Reason for the payment adjustment.)
     */
    public Coding getPaymentAdjustmentReason() { 
      if (this.paymentAdjustmentReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.paymentAdjustmentReason");
        else if (Configuration.doAutoCreate())
          this.paymentAdjustmentReason = new Coding(); // cc
      return this.paymentAdjustmentReason;
    }

    public boolean hasPaymentAdjustmentReason() { 
      return this.paymentAdjustmentReason != null && !this.paymentAdjustmentReason.isEmpty();
    }

    /**
     * @param value {@link #paymentAdjustmentReason} (Reason for the payment adjustment.)
     */
    public ExplanationOfBenefit setPaymentAdjustmentReason(Coding value) { 
      this.paymentAdjustmentReason = value;
      return this;
    }

    /**
     * @return {@link #paymentDate} (Estimated payment data.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
     */
    public DateType getPaymentDateElement() { 
      if (this.paymentDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.paymentDate");
        else if (Configuration.doAutoCreate())
          this.paymentDate = new DateType(); // bb
      return this.paymentDate;
    }

    public boolean hasPaymentDateElement() { 
      return this.paymentDate != null && !this.paymentDate.isEmpty();
    }

    public boolean hasPaymentDate() { 
      return this.paymentDate != null && !this.paymentDate.isEmpty();
    }

    /**
     * @param value {@link #paymentDate} (Estimated payment data.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
     */
    public ExplanationOfBenefit setPaymentDateElement(DateType value) { 
      this.paymentDate = value;
      return this;
    }

    /**
     * @return Estimated payment data.
     */
    public Date getPaymentDate() { 
      return this.paymentDate == null ? null : this.paymentDate.getValue();
    }

    /**
     * @param value Estimated payment data.
     */
    public ExplanationOfBenefit setPaymentDate(Date value) { 
      if (value == null)
        this.paymentDate = null;
      else {
        if (this.paymentDate == null)
          this.paymentDate = new DateType();
        this.paymentDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #paymentAmount} (Payable less any payment adjustment.)
     */
    public Money getPaymentAmount() { 
      if (this.paymentAmount == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.paymentAmount");
        else if (Configuration.doAutoCreate())
          this.paymentAmount = new Money(); // cc
      return this.paymentAmount;
    }

    public boolean hasPaymentAmount() { 
      return this.paymentAmount != null && !this.paymentAmount.isEmpty();
    }

    /**
     * @param value {@link #paymentAmount} (Payable less any payment adjustment.)
     */
    public ExplanationOfBenefit setPaymentAmount(Money value) { 
      this.paymentAmount = value;
      return this;
    }

    /**
     * @return {@link #paymentRef} (Payment identifer.)
     */
    public Identifier getPaymentRef() { 
      if (this.paymentRef == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.paymentRef");
        else if (Configuration.doAutoCreate())
          this.paymentRef = new Identifier(); // cc
      return this.paymentRef;
    }

    public boolean hasPaymentRef() { 
      return this.paymentRef != null && !this.paymentRef.isEmpty();
    }

    /**
     * @param value {@link #paymentRef} (Payment identifer.)
     */
    public ExplanationOfBenefit setPaymentRef(Identifier value) { 
      this.paymentRef = value;
      return this;
    }

    /**
     * @return {@link #reserved} (Status of funds reservation (For provider, for Patient, None).)
     */
    public Coding getReserved() { 
      if (this.reserved == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.reserved");
        else if (Configuration.doAutoCreate())
          this.reserved = new Coding(); // cc
      return this.reserved;
    }

    public boolean hasReserved() { 
      return this.reserved != null && !this.reserved.isEmpty();
    }

    /**
     * @param value {@link #reserved} (Status of funds reservation (For provider, for Patient, None).)
     */
    public ExplanationOfBenefit setReserved(Coding value) { 
      this.reserved = value;
      return this;
    }

    /**
     * @return {@link #form} (The form to be used for printing the content.)
     */
    public Coding getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.form");
        else if (Configuration.doAutoCreate())
          this.form = new Coding(); // cc
      return this.form;
    }

    public boolean hasForm() { 
      return this.form != null && !this.form.isEmpty();
    }

    /**
     * @param value {@link #form} (The form to be used for printing the content.)
     */
    public ExplanationOfBenefit setForm(Coding value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #note} (Note text.)
     */
    public List<NotesComponent> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<NotesComponent>();
      return this.note;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (NotesComponent item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #note} (Note text.)
     */
    // syntactic sugar
    public NotesComponent addNote() { //3
      NotesComponent t = new NotesComponent();
      if (this.note == null)
        this.note = new ArrayList<NotesComponent>();
      this.note.add(t);
      return t;
    }

    // syntactic sugar
    public ExplanationOfBenefit addNote(NotesComponent t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<NotesComponent>();
      this.note.add(t);
      return this;
    }

    /**
     * @return {@link #benefitBalance} (Balance by Benefit Category.)
     */
    public List<BenefitBalanceComponent> getBenefitBalance() { 
      if (this.benefitBalance == null)
        this.benefitBalance = new ArrayList<BenefitBalanceComponent>();
      return this.benefitBalance;
    }

    public boolean hasBenefitBalance() { 
      if (this.benefitBalance == null)
        return false;
      for (BenefitBalanceComponent item : this.benefitBalance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #benefitBalance} (Balance by Benefit Category.)
     */
    // syntactic sugar
    public BenefitBalanceComponent addBenefitBalance() { //3
      BenefitBalanceComponent t = new BenefitBalanceComponent();
      if (this.benefitBalance == null)
        this.benefitBalance = new ArrayList<BenefitBalanceComponent>();
      this.benefitBalance.add(t);
      return t;
    }

    // syntactic sugar
    public ExplanationOfBenefit addBenefitBalance(BenefitBalanceComponent t) { //3
      if (t == null)
        return this;
      if (this.benefitBalance == null)
        this.benefitBalance = new ArrayList<BenefitBalanceComponent>();
      this.benefitBalance.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response Business Identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("claim", "Reference(Claim)", "The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.", 0, java.lang.Integer.MAX_VALUE, claim));
        childrenList.add(new Property("claimResponse", "Reference(ClaimResponse)", "The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.", 0, java.lang.Integer.MAX_VALUE, claimResponse));
        childrenList.add(new Property("ruleset", "Coding", "The version of the specification on which this instance relies.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The version of the specification from which the original instance was created.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the EOB was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("billablePeriod", "Period", "The billable period for which charges are being submitted.", 0, java.lang.Integer.MAX_VALUE, billablePeriod));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The provider which is responsible for the claim.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The provider which is responsible for the claim.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, java.lang.Integer.MAX_VALUE, facility));
        childrenList.add(new Property("relatedClaim", "Reference(Claim)", "Other claims which are related to this claim such as prior claim versions or for related services.", 0, java.lang.Integer.MAX_VALUE, relatedClaim));
        childrenList.add(new Property("prescription", "Reference(MedicationOrder|VisionPrescription)", "Prescription to support the dispensing of Pharmacy or Vision products.", 0, java.lang.Integer.MAX_VALUE, prescription));
        childrenList.add(new Property("originalPrescription", "Reference(MedicationOrder)", "Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.", 0, java.lang.Integer.MAX_VALUE, originalPrescription));
        childrenList.add(new Property("payee", "", "The party to be reimbursed for the services.", 0, java.lang.Integer.MAX_VALUE, payee));
        childrenList.add(new Property("referral", "Reference(ReferralRequest)", "The referral resource which lists the date, practitioner, reason and other supporting information.", 0, java.lang.Integer.MAX_VALUE, referral));
        childrenList.add(new Property("diagnosis", "", "Ordered list of patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        childrenList.add(new Property("specialCondition", "Coding", "List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.", 0, java.lang.Integer.MAX_VALUE, specialCondition));
        childrenList.add(new Property("patient", "Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("precedence", "positiveInt", "Precedence (primary, secondary, etc.).", 0, java.lang.Integer.MAX_VALUE, precedence));
        childrenList.add(new Property("coverage", "", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("exception", "Coding", "Factors which may influence the applicability of coverage.", 0, java.lang.Integer.MAX_VALUE, exception));
        childrenList.add(new Property("school", "string", "Name of school for over-aged dependants.", 0, java.lang.Integer.MAX_VALUE, school));
        childrenList.add(new Property("accidentDate", "date", "Date of an accident which these services are addressing.", 0, java.lang.Integer.MAX_VALUE, accidentDate));
        childrenList.add(new Property("accidentType", "Coding", "Type of accident: work, auto, etc.", 0, java.lang.Integer.MAX_VALUE, accidentType));
        childrenList.add(new Property("accidentLocation[x]", "string|Address|Reference(Location)", "Accident Place.", 0, java.lang.Integer.MAX_VALUE, accidentLocation));
        childrenList.add(new Property("interventionException", "Coding", "A list of intervention and exception codes which may influence the adjudication of the claim.", 0, java.lang.Integer.MAX_VALUE, interventionException));
        childrenList.add(new Property("onset[x]", "date|Period", "The start or start and end dates for the treatable condition.", 0, java.lang.Integer.MAX_VALUE, onset));
        childrenList.add(new Property("employmentImpacted", "Period", "The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).", 0, java.lang.Integer.MAX_VALUE, employmentImpacted));
        childrenList.add(new Property("hospitalization", "Period", "The start and optional end dates of when the patient was confined to a treatment center.", 0, java.lang.Integer.MAX_VALUE, hospitalization));
        childrenList.add(new Property("item", "", "First tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, item));
        childrenList.add(new Property("addItem", "", "The first tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, addItem));
        childrenList.add(new Property("claimTotal", "Money", "The total value of the claim.", 0, java.lang.Integer.MAX_VALUE, claimTotal));
        childrenList.add(new Property("missingTeeth", "", "A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.", 0, java.lang.Integer.MAX_VALUE, missingTeeth));
        childrenList.add(new Property("unallocDeductable", "Money", "The amount of deductable applied which was not allocated to any particular service line.", 0, java.lang.Integer.MAX_VALUE, unallocDeductable));
        childrenList.add(new Property("totalBenefit", "Money", "Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable).", 0, java.lang.Integer.MAX_VALUE, totalBenefit));
        childrenList.add(new Property("paymentAdjustment", "Money", "Adjustment to the payment of this transaction which is not related to adjudication of this transaction.", 0, java.lang.Integer.MAX_VALUE, paymentAdjustment));
        childrenList.add(new Property("paymentAdjustmentReason", "Coding", "Reason for the payment adjustment.", 0, java.lang.Integer.MAX_VALUE, paymentAdjustmentReason));
        childrenList.add(new Property("paymentDate", "date", "Estimated payment data.", 0, java.lang.Integer.MAX_VALUE, paymentDate));
        childrenList.add(new Property("paymentAmount", "Money", "Payable less any payment adjustment.", 0, java.lang.Integer.MAX_VALUE, paymentAmount));
        childrenList.add(new Property("paymentRef", "Identifier", "Payment identifer.", 0, java.lang.Integer.MAX_VALUE, paymentRef));
        childrenList.add(new Property("reserved", "Coding", "Status of funds reservation (For provider, for Patient, None).", 0, java.lang.Integer.MAX_VALUE, reserved));
        childrenList.add(new Property("form", "Coding", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("note", "", "Note text.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("benefitBalance", "", "Balance by Benefit Category.", 0, java.lang.Integer.MAX_VALUE, benefitBalance));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("claim"))
          this.claim = castToReference(value); // Reference
        else if (name.equals("claimResponse"))
          this.claimResponse = castToReference(value); // Reference
        else if (name.equals("ruleset"))
          this.ruleset = castToCoding(value); // Coding
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("billablePeriod"))
          this.billablePeriod = castToPeriod(value); // Period
        else if (name.equals("disposition"))
          this.disposition = castToString(value); // StringType
        else if (name.equals("provider"))
          this.provider = castToReference(value); // Reference
        else if (name.equals("organization"))
          this.organization = castToReference(value); // Reference
        else if (name.equals("facility"))
          this.facility = castToReference(value); // Reference
        else if (name.equals("relatedClaim"))
          this.getRelatedClaim().add(castToReference(value));
        else if (name.equals("prescription"))
          this.prescription = castToReference(value); // Reference
        else if (name.equals("originalPrescription"))
          this.originalPrescription = castToReference(value); // Reference
        else if (name.equals("payee"))
          this.payee = (PayeeComponent) value; // PayeeComponent
        else if (name.equals("referral"))
          this.referral = castToReference(value); // Reference
        else if (name.equals("diagnosis"))
          this.getDiagnosis().add((DiagnosisComponent) value);
        else if (name.equals("specialCondition"))
          this.getSpecialCondition().add(castToCoding(value));
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("precedence"))
          this.precedence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("coverage"))
          this.coverage = (CoverageComponent) value; // CoverageComponent
        else if (name.equals("exception"))
          this.getException().add(castToCoding(value));
        else if (name.equals("school"))
          this.school = castToString(value); // StringType
        else if (name.equals("accidentDate"))
          this.accidentDate = castToDate(value); // DateType
        else if (name.equals("accidentType"))
          this.accidentType = castToCoding(value); // Coding
        else if (name.equals("accidentLocation[x]"))
          this.accidentLocation = (Type) value; // Type
        else if (name.equals("interventionException"))
          this.getInterventionException().add(castToCoding(value));
        else if (name.equals("onset[x]"))
          this.onset = (Type) value; // Type
        else if (name.equals("employmentImpacted"))
          this.employmentImpacted = castToPeriod(value); // Period
        else if (name.equals("hospitalization"))
          this.hospitalization = castToPeriod(value); // Period
        else if (name.equals("item"))
          this.getItem().add((ItemsComponent) value);
        else if (name.equals("addItem"))
          this.getAddItem().add((AddedItemComponent) value);
        else if (name.equals("claimTotal"))
          this.claimTotal = castToMoney(value); // Money
        else if (name.equals("missingTeeth"))
          this.getMissingTeeth().add((MissingTeethComponent) value);
        else if (name.equals("unallocDeductable"))
          this.unallocDeductable = castToMoney(value); // Money
        else if (name.equals("totalBenefit"))
          this.totalBenefit = castToMoney(value); // Money
        else if (name.equals("paymentAdjustment"))
          this.paymentAdjustment = castToMoney(value); // Money
        else if (name.equals("paymentAdjustmentReason"))
          this.paymentAdjustmentReason = castToCoding(value); // Coding
        else if (name.equals("paymentDate"))
          this.paymentDate = castToDate(value); // DateType
        else if (name.equals("paymentAmount"))
          this.paymentAmount = castToMoney(value); // Money
        else if (name.equals("paymentRef"))
          this.paymentRef = castToIdentifier(value); // Identifier
        else if (name.equals("reserved"))
          this.reserved = castToCoding(value); // Coding
        else if (name.equals("form"))
          this.form = castToCoding(value); // Coding
        else if (name.equals("note"))
          this.getNote().add((NotesComponent) value);
        else if (name.equals("benefitBalance"))
          this.getBenefitBalance().add((BenefitBalanceComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("claim")) {
          this.claim = new Reference();
          return this.claim;
        }
        else if (name.equals("claimResponse")) {
          this.claimResponse = new Reference();
          return this.claimResponse;
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
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.created");
        }
        else if (name.equals("billablePeriod")) {
          this.billablePeriod = new Period();
          return this.billablePeriod;
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.disposition");
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("facility")) {
          this.facility = new Reference();
          return this.facility;
        }
        else if (name.equals("relatedClaim")) {
          return addRelatedClaim();
        }
        else if (name.equals("prescription")) {
          this.prescription = new Reference();
          return this.prescription;
        }
        else if (name.equals("originalPrescription")) {
          this.originalPrescription = new Reference();
          return this.originalPrescription;
        }
        else if (name.equals("payee")) {
          this.payee = new PayeeComponent();
          return this.payee;
        }
        else if (name.equals("referral")) {
          this.referral = new Reference();
          return this.referral;
        }
        else if (name.equals("diagnosis")) {
          return addDiagnosis();
        }
        else if (name.equals("specialCondition")) {
          return addSpecialCondition();
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("precedence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.precedence");
        }
        else if (name.equals("coverage")) {
          this.coverage = new CoverageComponent();
          return this.coverage;
        }
        else if (name.equals("exception")) {
          return addException();
        }
        else if (name.equals("school")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.school");
        }
        else if (name.equals("accidentDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.accidentDate");
        }
        else if (name.equals("accidentType")) {
          this.accidentType = new Coding();
          return this.accidentType;
        }
        else if (name.equals("accidentLocationString")) {
          this.accidentLocation = new StringType();
          return this.accidentLocation;
        }
        else if (name.equals("accidentLocationAddress")) {
          this.accidentLocation = new Address();
          return this.accidentLocation;
        }
        else if (name.equals("accidentLocationReference")) {
          this.accidentLocation = new Reference();
          return this.accidentLocation;
        }
        else if (name.equals("interventionException")) {
          return addInterventionException();
        }
        else if (name.equals("onsetDate")) {
          this.onset = new DateType();
          return this.onset;
        }
        else if (name.equals("onsetPeriod")) {
          this.onset = new Period();
          return this.onset;
        }
        else if (name.equals("employmentImpacted")) {
          this.employmentImpacted = new Period();
          return this.employmentImpacted;
        }
        else if (name.equals("hospitalization")) {
          this.hospitalization = new Period();
          return this.hospitalization;
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else if (name.equals("addItem")) {
          return addAddItem();
        }
        else if (name.equals("claimTotal")) {
          this.claimTotal = new Money();
          return this.claimTotal;
        }
        else if (name.equals("missingTeeth")) {
          return addMissingTeeth();
        }
        else if (name.equals("unallocDeductable")) {
          this.unallocDeductable = new Money();
          return this.unallocDeductable;
        }
        else if (name.equals("totalBenefit")) {
          this.totalBenefit = new Money();
          return this.totalBenefit;
        }
        else if (name.equals("paymentAdjustment")) {
          this.paymentAdjustment = new Money();
          return this.paymentAdjustment;
        }
        else if (name.equals("paymentAdjustmentReason")) {
          this.paymentAdjustmentReason = new Coding();
          return this.paymentAdjustmentReason;
        }
        else if (name.equals("paymentDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.paymentDate");
        }
        else if (name.equals("paymentAmount")) {
          this.paymentAmount = new Money();
          return this.paymentAmount;
        }
        else if (name.equals("paymentRef")) {
          this.paymentRef = new Identifier();
          return this.paymentRef;
        }
        else if (name.equals("reserved")) {
          this.reserved = new Coding();
          return this.reserved;
        }
        else if (name.equals("form")) {
          this.form = new Coding();
          return this.form;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("benefitBalance")) {
          return addBenefitBalance();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ExplanationOfBenefit";

  }

      public ExplanationOfBenefit copy() {
        ExplanationOfBenefit dst = new ExplanationOfBenefit();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.claim = claim == null ? null : claim.copy();
        dst.claimResponse = claimResponse == null ? null : claimResponse.copy();
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.billablePeriod = billablePeriod == null ? null : billablePeriod.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.facility = facility == null ? null : facility.copy();
        if (relatedClaim != null) {
          dst.relatedClaim = new ArrayList<Reference>();
          for (Reference i : relatedClaim)
            dst.relatedClaim.add(i.copy());
        };
        dst.prescription = prescription == null ? null : prescription.copy();
        dst.originalPrescription = originalPrescription == null ? null : originalPrescription.copy();
        dst.payee = payee == null ? null : payee.copy();
        dst.referral = referral == null ? null : referral.copy();
        if (diagnosis != null) {
          dst.diagnosis = new ArrayList<DiagnosisComponent>();
          for (DiagnosisComponent i : diagnosis)
            dst.diagnosis.add(i.copy());
        };
        if (specialCondition != null) {
          dst.specialCondition = new ArrayList<Coding>();
          for (Coding i : specialCondition)
            dst.specialCondition.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.precedence = precedence == null ? null : precedence.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        if (exception != null) {
          dst.exception = new ArrayList<Coding>();
          for (Coding i : exception)
            dst.exception.add(i.copy());
        };
        dst.school = school == null ? null : school.copy();
        dst.accidentDate = accidentDate == null ? null : accidentDate.copy();
        dst.accidentType = accidentType == null ? null : accidentType.copy();
        dst.accidentLocation = accidentLocation == null ? null : accidentLocation.copy();
        if (interventionException != null) {
          dst.interventionException = new ArrayList<Coding>();
          for (Coding i : interventionException)
            dst.interventionException.add(i.copy());
        };
        dst.onset = onset == null ? null : onset.copy();
        dst.employmentImpacted = employmentImpacted == null ? null : employmentImpacted.copy();
        dst.hospitalization = hospitalization == null ? null : hospitalization.copy();
        if (item != null) {
          dst.item = new ArrayList<ItemsComponent>();
          for (ItemsComponent i : item)
            dst.item.add(i.copy());
        };
        if (addItem != null) {
          dst.addItem = new ArrayList<AddedItemComponent>();
          for (AddedItemComponent i : addItem)
            dst.addItem.add(i.copy());
        };
        dst.claimTotal = claimTotal == null ? null : claimTotal.copy();
        if (missingTeeth != null) {
          dst.missingTeeth = new ArrayList<MissingTeethComponent>();
          for (MissingTeethComponent i : missingTeeth)
            dst.missingTeeth.add(i.copy());
        };
        dst.unallocDeductable = unallocDeductable == null ? null : unallocDeductable.copy();
        dst.totalBenefit = totalBenefit == null ? null : totalBenefit.copy();
        dst.paymentAdjustment = paymentAdjustment == null ? null : paymentAdjustment.copy();
        dst.paymentAdjustmentReason = paymentAdjustmentReason == null ? null : paymentAdjustmentReason.copy();
        dst.paymentDate = paymentDate == null ? null : paymentDate.copy();
        dst.paymentAmount = paymentAmount == null ? null : paymentAmount.copy();
        dst.paymentRef = paymentRef == null ? null : paymentRef.copy();
        dst.reserved = reserved == null ? null : reserved.copy();
        dst.form = form == null ? null : form.copy();
        if (note != null) {
          dst.note = new ArrayList<NotesComponent>();
          for (NotesComponent i : note)
            dst.note.add(i.copy());
        };
        if (benefitBalance != null) {
          dst.benefitBalance = new ArrayList<BenefitBalanceComponent>();
          for (BenefitBalanceComponent i : benefitBalance)
            dst.benefitBalance.add(i.copy());
        };
        return dst;
      }

      protected ExplanationOfBenefit typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExplanationOfBenefit))
          return false;
        ExplanationOfBenefit o = (ExplanationOfBenefit) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(claim, o.claim, true) && compareDeep(claimResponse, o.claimResponse, true)
           && compareDeep(ruleset, o.ruleset, true) && compareDeep(originalRuleset, o.originalRuleset, true)
           && compareDeep(created, o.created, true) && compareDeep(billablePeriod, o.billablePeriod, true)
           && compareDeep(disposition, o.disposition, true) && compareDeep(provider, o.provider, true) && compareDeep(organization, o.organization, true)
           && compareDeep(facility, o.facility, true) && compareDeep(relatedClaim, o.relatedClaim, true) && compareDeep(prescription, o.prescription, true)
           && compareDeep(originalPrescription, o.originalPrescription, true) && compareDeep(payee, o.payee, true)
           && compareDeep(referral, o.referral, true) && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(specialCondition, o.specialCondition, true)
           && compareDeep(patient, o.patient, true) && compareDeep(precedence, o.precedence, true) && compareDeep(coverage, o.coverage, true)
           && compareDeep(exception, o.exception, true) && compareDeep(school, o.school, true) && compareDeep(accidentDate, o.accidentDate, true)
           && compareDeep(accidentType, o.accidentType, true) && compareDeep(accidentLocation, o.accidentLocation, true)
           && compareDeep(interventionException, o.interventionException, true) && compareDeep(onset, o.onset, true)
           && compareDeep(employmentImpacted, o.employmentImpacted, true) && compareDeep(hospitalization, o.hospitalization, true)
           && compareDeep(item, o.item, true) && compareDeep(addItem, o.addItem, true) && compareDeep(claimTotal, o.claimTotal, true)
           && compareDeep(missingTeeth, o.missingTeeth, true) && compareDeep(unallocDeductable, o.unallocDeductable, true)
           && compareDeep(totalBenefit, o.totalBenefit, true) && compareDeep(paymentAdjustment, o.paymentAdjustment, true)
           && compareDeep(paymentAdjustmentReason, o.paymentAdjustmentReason, true) && compareDeep(paymentDate, o.paymentDate, true)
           && compareDeep(paymentAmount, o.paymentAmount, true) && compareDeep(paymentRef, o.paymentRef, true)
           && compareDeep(reserved, o.reserved, true) && compareDeep(form, o.form, true) && compareDeep(note, o.note, true)
           && compareDeep(benefitBalance, o.benefitBalance, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExplanationOfBenefit))
          return false;
        ExplanationOfBenefit o = (ExplanationOfBenefit) other;
        return compareValues(created, o.created, true) && compareValues(disposition, o.disposition, true) && compareValues(precedence, o.precedence, true)
           && compareValues(school, o.school, true) && compareValues(accidentDate, o.accidentDate, true) && compareValues(paymentDate, o.paymentDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (claim == null || claim.isEmpty())
           && (claimResponse == null || claimResponse.isEmpty()) && (ruleset == null || ruleset.isEmpty())
           && (originalRuleset == null || originalRuleset.isEmpty()) && (created == null || created.isEmpty())
           && (billablePeriod == null || billablePeriod.isEmpty()) && (disposition == null || disposition.isEmpty())
           && (provider == null || provider.isEmpty()) && (organization == null || organization.isEmpty())
           && (facility == null || facility.isEmpty()) && (relatedClaim == null || relatedClaim.isEmpty())
           && (prescription == null || prescription.isEmpty()) && (originalPrescription == null || originalPrescription.isEmpty())
           && (payee == null || payee.isEmpty()) && (referral == null || referral.isEmpty()) && (diagnosis == null || diagnosis.isEmpty())
           && (specialCondition == null || specialCondition.isEmpty()) && (patient == null || patient.isEmpty())
           && (precedence == null || precedence.isEmpty()) && (coverage == null || coverage.isEmpty())
           && (exception == null || exception.isEmpty()) && (school == null || school.isEmpty()) && (accidentDate == null || accidentDate.isEmpty())
           && (accidentType == null || accidentType.isEmpty()) && (accidentLocation == null || accidentLocation.isEmpty())
           && (interventionException == null || interventionException.isEmpty()) && (onset == null || onset.isEmpty())
           && (employmentImpacted == null || employmentImpacted.isEmpty()) && (hospitalization == null || hospitalization.isEmpty())
           && (item == null || item.isEmpty()) && (addItem == null || addItem.isEmpty()) && (claimTotal == null || claimTotal.isEmpty())
           && (missingTeeth == null || missingTeeth.isEmpty()) && (unallocDeductable == null || unallocDeductable.isEmpty())
           && (totalBenefit == null || totalBenefit.isEmpty()) && (paymentAdjustment == null || paymentAdjustment.isEmpty())
           && (paymentAdjustmentReason == null || paymentAdjustmentReason.isEmpty()) && (paymentDate == null || paymentDate.isEmpty())
           && (paymentAmount == null || paymentAmount.isEmpty()) && (paymentRef == null || paymentRef.isEmpty())
           && (reserved == null || reserved.isEmpty()) && (form == null || form.isEmpty()) && (note == null || note.isEmpty())
           && (benefitBalance == null || benefitBalance.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ExplanationOfBenefit;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Explanation of Benefit</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ExplanationOfBenefit.identifier", description="The business identifier of the Explanation of Benefit", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Explanation of Benefit</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>disposition</b>
   * <p>
   * Description: <b>The contents of the disposition message</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExplanationOfBenefit.disposition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="disposition", path="ExplanationOfBenefit.disposition", description="The contents of the disposition message", type="string" )
  public static final String SP_DISPOSITION = "disposition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>disposition</b>
   * <p>
   * Description: <b>The contents of the disposition message</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ExplanationOfBenefit.disposition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DISPOSITION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DISPOSITION);

 /**
   * Search parameter: <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provider", path="ExplanationOfBenefit.provider", description="The reference to the provider", type="reference" )
  public static final String SP_PROVIDER = "provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.provider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDER = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:provider").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ExplanationOfBenefit.patient", description="The reference to the patient", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:patient").toLocked();

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>The creation date for the EOB</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ExplanationOfBenefit.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="ExplanationOfBenefit.created", description="The creation date for the EOB", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>The creation date for the EOB</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ExplanationOfBenefit.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="ExplanationOfBenefit.organization", description="The reference to the providing organization", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:organization").toLocked();

 /**
   * Search parameter: <b>claim</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.claim</b><br>
   * </p>
   */
  @SearchParamDefinition(name="claim", path="ExplanationOfBenefit.claim", description="The reference to the claim", type="reference" )
  public static final String SP_CLAIM = "claim";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>claim</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.claim</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CLAIM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CLAIM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:claim</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CLAIM = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:claim").toLocked();

 /**
   * Search parameter: <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.facility</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facility", path="ExplanationOfBenefit.facility", description="Facility responsible for the goods and services", type="reference" )
  public static final String SP_FACILITY = "facility";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.facility</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FACILITY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FACILITY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:facility</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FACILITY = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:facility").toLocked();


}

