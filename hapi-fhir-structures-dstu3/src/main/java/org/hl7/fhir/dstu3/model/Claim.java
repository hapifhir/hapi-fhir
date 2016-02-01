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
 * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
 */
@ResourceDef(name="Claim", profile="http://hl7.org/fhir/Profile/Claim")
public class Claim extends DomainResource {

    public enum ClaimType {
        /**
         * A claim for Institution based, typically in-patient, goods and services.
         */
        INSTITUTIONAL, 
        /**
         * A claim for Oral Health (Dentist, Denturist, Hygienist) goods and services.
         */
        ORAL, 
        /**
         * A claim for Pharmacy based goods and services.
         */
        PHARMACY, 
        /**
         * A claim for Professional, typically out-patient, goods and services.
         */
        PROFESSIONAL, 
        /**
         * A claim for Vision (Opthamologist, Optometrist and Optician) goods and services.
         */
        VISION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ClaimType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("institutional".equals(codeString))
          return INSTITUTIONAL;
        if ("oral".equals(codeString))
          return ORAL;
        if ("pharmacy".equals(codeString))
          return PHARMACY;
        if ("professional".equals(codeString))
          return PROFESSIONAL;
        if ("vision".equals(codeString))
          return VISION;
        throw new FHIRException("Unknown ClaimType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTITUTIONAL: return "institutional";
            case ORAL: return "oral";
            case PHARMACY: return "pharmacy";
            case PROFESSIONAL: return "professional";
            case VISION: return "vision";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INSTITUTIONAL: return "http://hl7.org/fhir/claim-type-link";
            case ORAL: return "http://hl7.org/fhir/claim-type-link";
            case PHARMACY: return "http://hl7.org/fhir/claim-type-link";
            case PROFESSIONAL: return "http://hl7.org/fhir/claim-type-link";
            case VISION: return "http://hl7.org/fhir/claim-type-link";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INSTITUTIONAL: return "A claim for Institution based, typically in-patient, goods and services.";
            case ORAL: return "A claim for Oral Health (Dentist, Denturist, Hygienist) goods and services.";
            case PHARMACY: return "A claim for Pharmacy based goods and services.";
            case PROFESSIONAL: return "A claim for Professional, typically out-patient, goods and services.";
            case VISION: return "A claim for Vision (Opthamologist, Optometrist and Optician) goods and services.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTITUTIONAL: return "Institutional";
            case ORAL: return "Oral Health";
            case PHARMACY: return "Pharmacy";
            case PROFESSIONAL: return "Professional";
            case VISION: return "Vision";
            default: return "?";
          }
        }
    }

  public static class ClaimTypeEnumFactory implements EnumFactory<ClaimType> {
    public ClaimType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("institutional".equals(codeString))
          return ClaimType.INSTITUTIONAL;
        if ("oral".equals(codeString))
          return ClaimType.ORAL;
        if ("pharmacy".equals(codeString))
          return ClaimType.PHARMACY;
        if ("professional".equals(codeString))
          return ClaimType.PROFESSIONAL;
        if ("vision".equals(codeString))
          return ClaimType.VISION;
        throw new IllegalArgumentException("Unknown ClaimType code '"+codeString+"'");
        }
        public Enumeration<ClaimType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("institutional".equals(codeString))
          return new Enumeration<ClaimType>(this, ClaimType.INSTITUTIONAL);
        if ("oral".equals(codeString))
          return new Enumeration<ClaimType>(this, ClaimType.ORAL);
        if ("pharmacy".equals(codeString))
          return new Enumeration<ClaimType>(this, ClaimType.PHARMACY);
        if ("professional".equals(codeString))
          return new Enumeration<ClaimType>(this, ClaimType.PROFESSIONAL);
        if ("vision".equals(codeString))
          return new Enumeration<ClaimType>(this, ClaimType.VISION);
        throw new FHIRException("Unknown ClaimType code '"+codeString+"'");
        }
    public String toCode(ClaimType code) {
      if (code == ClaimType.INSTITUTIONAL)
        return "institutional";
      if (code == ClaimType.ORAL)
        return "oral";
      if (code == ClaimType.PHARMACY)
        return "pharmacy";
      if (code == ClaimType.PROFESSIONAL)
        return "professional";
      if (code == ClaimType.VISION)
        return "vision";
      return "?";
      }
    public String toSystem(ClaimType code) {
      return code.getSystem();
      }
    }

    public enum Use {
        /**
         * The treatment is complete and this represents a Claim for the services.
         */
        COMPLETE, 
        /**
         * The treatment is proposed and this represents a Pre-authorization for the services.
         */
        PROPOSED, 
        /**
         * The treatment is proposed and this represents a Pre-determination for the services.
         */
        EXPLORATORY, 
        /**
         * A locally defined or otherwise resolved status.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Use fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("exploratory".equals(codeString))
          return EXPLORATORY;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown Use code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLETE: return "complete";
            case PROPOSED: return "proposed";
            case EXPLORATORY: return "exploratory";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPLETE: return "http://hl7.org/fhir/claim-use-link";
            case PROPOSED: return "http://hl7.org/fhir/claim-use-link";
            case EXPLORATORY: return "http://hl7.org/fhir/claim-use-link";
            case OTHER: return "http://hl7.org/fhir/claim-use-link";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPLETE: return "The treatment is complete and this represents a Claim for the services.";
            case PROPOSED: return "The treatment is proposed and this represents a Pre-authorization for the services.";
            case EXPLORATORY: return "The treatment is proposed and this represents a Pre-determination for the services.";
            case OTHER: return "A locally defined or otherwise resolved status.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLETE: return "Complete";
            case PROPOSED: return "Proposed";
            case EXPLORATORY: return "Exploratory";
            case OTHER: return "Other";
            default: return "?";
          }
        }
    }

  public static class UseEnumFactory implements EnumFactory<Use> {
    public Use fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return Use.COMPLETE;
        if ("proposed".equals(codeString))
          return Use.PROPOSED;
        if ("exploratory".equals(codeString))
          return Use.EXPLORATORY;
        if ("other".equals(codeString))
          return Use.OTHER;
        throw new IllegalArgumentException("Unknown Use code '"+codeString+"'");
        }
        public Enumeration<Use> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("complete".equals(codeString))
          return new Enumeration<Use>(this, Use.COMPLETE);
        if ("proposed".equals(codeString))
          return new Enumeration<Use>(this, Use.PROPOSED);
        if ("exploratory".equals(codeString))
          return new Enumeration<Use>(this, Use.EXPLORATORY);
        if ("other".equals(codeString))
          return new Enumeration<Use>(this, Use.OTHER);
        throw new FHIRException("Unknown Use code '"+codeString+"'");
        }
    public String toCode(Use code) {
      if (code == Use.COMPLETE)
        return "complete";
      if (code == Use.PROPOSED)
        return "proposed";
      if (code == Use.EXPLORATORY)
        return "exploratory";
      if (code == Use.OTHER)
        return "other";
      return "?";
      }
    public String toSystem(Use code) {
      return code.getSystem();
      }
    }

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
    return "Claim.payee";

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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
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
    return "Claim.diagnosis";

  }

  }

    @Block()
    public static class CoverageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line item.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service instance identifier", formalDefinition="A service line item." )
        protected PositiveIntType sequence;

        /**
         * The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.
         */
        @Child(name = "focal", type = {BooleanType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Is the focal Coverage", formalDefinition="The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated." )
        protected BooleanType focal;

        /**
         * Reference to the program or plan identification, underwriter or payor.
         */
        @Child(name = "coverage", type = {Coverage.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the program or plan identification, underwriter or payor." )
        protected Reference coverage;

        /**
         * The actual object that is the target of the reference (Reference to the program or plan identification, underwriter or payor.)
         */
        protected Coverage coverageTarget;

        /**
         * The contract number of a business agreement which describes the terms and conditions.
         */
        @Child(name = "businessArrangement", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Business agreement", formalDefinition="The contract number of a business agreement which describes the terms and conditions." )
        protected StringType businessArrangement;

        /**
         * The relationship of the patient to the subscriber.
         */
        @Child(name = "relationship", type = {Coding.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Patient relationship to subscriber", formalDefinition="The relationship of the patient to the subscriber." )
        protected Coding relationship;

        /**
         * A list of references from the Insurer to which these services pertain.
         */
        @Child(name = "preAuthRef", type = {StringType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Pre-Authorization/Determination Reference", formalDefinition="A list of references from the Insurer to which these services pertain." )
        protected List<StringType> preAuthRef;

        /**
         * The Coverages adjudication details.
         */
        @Child(name = "claimResponse", type = {ClaimResponse.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication results", formalDefinition="The Coverages adjudication details." )
        protected Reference claimResponse;

        /**
         * The actual object that is the target of the reference (The Coverages adjudication details.)
         */
        protected ClaimResponse claimResponseTarget;

        /**
         * The style (standard) and version of the original material which was converted into this resource.
         */
        @Child(name = "originalRuleset", type = {Coding.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
        protected Coding originalRuleset;

        private static final long serialVersionUID = 621250924L;

    /**
     * Constructor
     */
      public CoverageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CoverageComponent(PositiveIntType sequence, BooleanType focal, Reference coverage, Coding relationship) {
        super();
        this.sequence = sequence;
        this.focal = focal;
        this.coverage = coverage;
        this.relationship = relationship;
      }

        /**
         * @return {@link #sequence} (A service line item.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.sequence");
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
         * @param value {@link #sequence} (A service line item.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public CoverageComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A service line item.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A service line item.
         */
        public CoverageComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #focal} (The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public BooleanType getFocalElement() { 
          if (this.focal == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.focal");
            else if (Configuration.doAutoCreate())
              this.focal = new BooleanType(); // bb
          return this.focal;
        }

        public boolean hasFocalElement() { 
          return this.focal != null && !this.focal.isEmpty();
        }

        public boolean hasFocal() { 
          return this.focal != null && !this.focal.isEmpty();
        }

        /**
         * @param value {@link #focal} (The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public CoverageComponent setFocalElement(BooleanType value) { 
          this.focal = value;
          return this;
        }

        /**
         * @return The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.
         */
        public boolean getFocal() { 
          return this.focal == null || this.focal.isEmpty() ? false : this.focal.getValue();
        }

        /**
         * @param value The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.
         */
        public CoverageComponent setFocal(boolean value) { 
            if (this.focal == null)
              this.focal = new BooleanType();
            this.focal.setValue(value);
          return this;
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
         * @return {@link #businessArrangement} (The contract number of a business agreement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public StringType getBusinessArrangementElement() { 
          if (this.businessArrangement == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.businessArrangement");
            else if (Configuration.doAutoCreate())
              this.businessArrangement = new StringType(); // bb
          return this.businessArrangement;
        }

        public boolean hasBusinessArrangementElement() { 
          return this.businessArrangement != null && !this.businessArrangement.isEmpty();
        }

        public boolean hasBusinessArrangement() { 
          return this.businessArrangement != null && !this.businessArrangement.isEmpty();
        }

        /**
         * @param value {@link #businessArrangement} (The contract number of a business agreement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public CoverageComponent setBusinessArrangementElement(StringType value) { 
          this.businessArrangement = value;
          return this;
        }

        /**
         * @return The contract number of a business agreement which describes the terms and conditions.
         */
        public String getBusinessArrangement() { 
          return this.businessArrangement == null ? null : this.businessArrangement.getValue();
        }

        /**
         * @param value The contract number of a business agreement which describes the terms and conditions.
         */
        public CoverageComponent setBusinessArrangement(String value) { 
          if (Utilities.noString(value))
            this.businessArrangement = null;
          else {
            if (this.businessArrangement == null)
              this.businessArrangement = new StringType();
            this.businessArrangement.setValue(value);
          }
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

        /**
         * @return {@link #claimResponse} (The Coverages adjudication details.)
         */
        public Reference getClaimResponse() { 
          if (this.claimResponse == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.claimResponse");
            else if (Configuration.doAutoCreate())
              this.claimResponse = new Reference(); // cc
          return this.claimResponse;
        }

        public boolean hasClaimResponse() { 
          return this.claimResponse != null && !this.claimResponse.isEmpty();
        }

        /**
         * @param value {@link #claimResponse} (The Coverages adjudication details.)
         */
        public CoverageComponent setClaimResponse(Reference value) { 
          this.claimResponse = value;
          return this;
        }

        /**
         * @return {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Coverages adjudication details.)
         */
        public ClaimResponse getClaimResponseTarget() { 
          if (this.claimResponseTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.claimResponse");
            else if (Configuration.doAutoCreate())
              this.claimResponseTarget = new ClaimResponse(); // aa
          return this.claimResponseTarget;
        }

        /**
         * @param value {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Coverages adjudication details.)
         */
        public CoverageComponent setClaimResponseTarget(ClaimResponse value) { 
          this.claimResponseTarget = value;
          return this;
        }

        /**
         * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
         */
        public Coding getOriginalRuleset() { 
          if (this.originalRuleset == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.originalRuleset");
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
        public CoverageComponent setOriginalRuleset(Coding value) { 
          this.originalRuleset = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line item.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("focal", "boolean", "The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.", 0, java.lang.Integer.MAX_VALUE, focal));
          childrenList.add(new Property("coverage", "Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, coverage));
          childrenList.add(new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, java.lang.Integer.MAX_VALUE, businessArrangement));
          childrenList.add(new Property("relationship", "Coding", "The relationship of the patient to the subscriber.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("preAuthRef", "string", "A list of references from the Insurer to which these services pertain.", 0, java.lang.Integer.MAX_VALUE, preAuthRef));
          childrenList.add(new Property("claimResponse", "Reference(ClaimResponse)", "The Coverages adjudication details.", 0, java.lang.Integer.MAX_VALUE, claimResponse));
          childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("focal"))
          this.focal = castToBoolean(value); // BooleanType
        else if (name.equals("coverage"))
          this.coverage = castToReference(value); // Reference
        else if (name.equals("businessArrangement"))
          this.businessArrangement = castToString(value); // StringType
        else if (name.equals("relationship"))
          this.relationship = castToCoding(value); // Coding
        else if (name.equals("preAuthRef"))
          this.getPreAuthRef().add(castToString(value));
        else if (name.equals("claimResponse"))
          this.claimResponse = castToReference(value); // Reference
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
        }
        else if (name.equals("focal")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.focal");
        }
        else if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("businessArrangement")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.businessArrangement");
        }
        else if (name.equals("relationship")) {
          this.relationship = new Coding();
          return this.relationship;
        }
        else if (name.equals("preAuthRef")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.preAuthRef");
        }
        else if (name.equals("claimResponse")) {
          this.claimResponse = new Reference();
          return this.claimResponse;
        }
        else if (name.equals("originalRuleset")) {
          this.originalRuleset = new Coding();
          return this.originalRuleset;
        }
        else
          return super.addChild(name);
      }

      public CoverageComponent copy() {
        CoverageComponent dst = new CoverageComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.focal = focal == null ? null : focal.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.businessArrangement = businessArrangement == null ? null : businessArrangement.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        if (preAuthRef != null) {
          dst.preAuthRef = new ArrayList<StringType>();
          for (StringType i : preAuthRef)
            dst.preAuthRef.add(i.copy());
        };
        dst.claimResponse = claimResponse == null ? null : claimResponse.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CoverageComponent))
          return false;
        CoverageComponent o = (CoverageComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(focal, o.focal, true) && compareDeep(coverage, o.coverage, true)
           && compareDeep(businessArrangement, o.businessArrangement, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(preAuthRef, o.preAuthRef, true) && compareDeep(claimResponse, o.claimResponse, true)
           && compareDeep(originalRuleset, o.originalRuleset, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CoverageComponent))
          return false;
        CoverageComponent o = (CoverageComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(focal, o.focal, true) && compareValues(businessArrangement, o.businessArrangement, true)
           && compareValues(preAuthRef, o.preAuthRef, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (focal == null || focal.isEmpty())
           && (coverage == null || coverage.isEmpty()) && (businessArrangement == null || businessArrangement.isEmpty())
           && (relationship == null || relationship.isEmpty()) && (preAuthRef == null || preAuthRef.isEmpty())
           && (claimResponse == null || claimResponse.isEmpty()) && (originalRuleset == null || originalRuleset.isEmpty())
          ;
      }

  public String fhirType() {
    return "Claim.coverage";

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
         * Second tier of goods and services.
         */
        @Child(name = "detail", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Additional items", formalDefinition="Second tier of goods and services." )
        protected List<DetailComponent> detail;

        /**
         * The materials and placement date of prior fixed prosthesis.
         */
        @Child(name = "prosthesis", type = {}, order=18, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Prosthetic details", formalDefinition="The materials and placement date of prior fixed prosthesis." )
        protected ProsthesisComponent prosthesis;

        private static final long serialVersionUID = -1914609123L;

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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.diagnosisLinkId");
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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.points");
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
           && compareDeep(subSite, o.subSite, true) && compareDeep(modifier, o.modifier, true) && compareDeep(detail, o.detail, true)
           && compareDeep(prosthesis, o.prosthesis, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ItemsComponent))
          return false;
        ItemsComponent o = (ItemsComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(diagnosisLinkId, o.diagnosisLinkId, true)
           && compareValues(factor, o.factor, true) && compareValues(points, o.points, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (type == null || type.isEmpty())
           && (provider == null || provider.isEmpty()) && (diagnosisLinkId == null || diagnosisLinkId.isEmpty())
           && (service == null || service.isEmpty()) && (serviced == null || serviced.isEmpty()) && (place == null || place.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
           && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty()) && (net == null || net.isEmpty())
           && (udi == null || udi.isEmpty()) && (bodySite == null || bodySite.isEmpty()) && (subSite == null || subSite.isEmpty())
           && (modifier == null || modifier.isEmpty()) && (detail == null || detail.isEmpty()) && (prosthesis == null || prosthesis.isEmpty())
          ;
      }

  public String fhirType() {
    return "Claim.item";

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
         * Third tier of goods and services.
         */
        @Child(name = "subDetail", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Additional items", formalDefinition="Third tier of goods and services." )
        protected List<SubDetailComponent> subDetail;

        private static final long serialVersionUID = 5768017L;

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
        else if (name.equals("subDetail"))
          this.getSubDetail().add((SubDetailComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          this.udi = new Coding();
          return this.udi;
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
           && compareDeep(subDetail, o.subDetail, true);
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
           && (udi == null || udi.isEmpty()) && (subDetail == null || subDetail.isEmpty());
      }

  public String fhirType() {
    return "Claim.item.detail";

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

        private static final long serialVersionUID = 623567568L;

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
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          this.udi = new Coding();
          return this.udi;
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
          ;
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
           && (udi == null || udi.isEmpty());
      }

  public String fhirType() {
    return "Claim.item.detail.subDetail";

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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.initial");
        }
        else if (name.equals("priorDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.priorDate");
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
    return "Claim.item.prosthesis";

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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.extractionDate");
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
    return "Claim.missingTeeth";

  }

  }

    /**
     * The category of claim this is.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="institutional | oral | pharmacy | professional | vision", formalDefinition="The category of claim this is." )
    protected Enumeration<ClaimType> type;

    /**
     * The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Claim number", formalDefinition="The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number." )
    protected List<Identifier> identifier;

    /**
     * The version of the specification on which this instance relies.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Current specification followed", formalDefinition="The version of the specification on which this instance relies." )
    protected Coding ruleset;

    /**
     * The version of the specification from which the original instance was created.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original specification followed", formalDefinition="The version of the specification from which the original instance was created." )
    protected Coding originalRuleset;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The billable period for which charges are being submitted.
     */
    @Child(name = "billablePeriod", type = {Period.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period for charge submission", formalDefinition="The billable period for which charges are being submitted." )
    protected Period billablePeriod;

    /**
     * Insurer Identifier, typical BIN number (6 digit).
     */
    @Child(name = "target", type = {Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurer", formalDefinition="Insurer Identifier, typical BIN number (6 digit)." )
    protected Reference target;

    /**
     * The actual object that is the target of the reference (Insurer Identifier, typical BIN number (6 digit).)
     */
    protected Organization targetTarget;

    /**
     * The provider which is responsible for the bill, claim pre-determination, pre-authorization.
     */
    @Child(name = "provider", type = {Practitioner.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible provider", formalDefinition="The provider which is responsible for the bill, claim pre-determination, pre-authorization." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    protected Practitioner providerTarget;

    /**
     * The organization which is responsible for the bill, claim pre-determination, pre-authorization.
     */
    @Child(name = "organization", type = {Organization.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the bill, claim pre-determination, pre-authorization." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    protected Organization organizationTarget;

    /**
     * Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    @Child(name = "use", type = {CodeType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="complete | proposed | exploratory | other", formalDefinition="Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination)." )
    protected Enumeration<Use> use;

    /**
     * Immediate (STAT), best effort (NORMAL), deferred (DEFER).
     */
    @Child(name = "priority", type = {Coding.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Desired processing priority", formalDefinition="Immediate (STAT), best effort (NORMAL), deferred (DEFER)." )
    protected Coding priority;

    /**
     * In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.
     */
    @Child(name = "fundsReserve", type = {Coding.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Funds requested to be reserved", formalDefinition="In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested." )
    protected Coding fundsReserve;

    /**
     * Person who created the invoice/claim/pre-determination or pre-authorization.
     */
    @Child(name = "enterer", type = {Practitioner.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Author", formalDefinition="Person who created the invoice/claim/pre-determination or pre-authorization." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    protected Practitioner entererTarget;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Location.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Reference facility;

    /**
     * The actual object that is the target of the reference (Facility where the services were provided.)
     */
    protected Location facilityTarget;

    /**
     * Other claims which are related to this claim such as prior claim versions or for related services.
     */
    @Child(name = "relatedClaim", type = {Claim.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Related Claims", formalDefinition="Other claims which are related to this claim such as prior claim versions or for related services." )
    protected List<Reference> relatedClaim;
    /**
     * The actual objects that are the target of the reference (Other claims which are related to this claim such as prior claim versions or for related services.)
     */
    protected List<Claim> relatedClaimTarget;


    /**
     * Prescription to support the dispensing of Pharmacy or Vision products.
     */
    @Child(name = "prescription", type = {MedicationOrder.class, VisionPrescription.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Prescription", formalDefinition="Prescription to support the dispensing of Pharmacy or Vision products." )
    protected Reference prescription;

    /**
     * The actual object that is the target of the reference (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    protected Resource prescriptionTarget;

    /**
     * Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.
     */
    @Child(name = "originalPrescription", type = {MedicationOrder.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original Prescription", formalDefinition="Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products." )
    protected Reference originalPrescription;

    /**
     * The actual object that is the target of the reference (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    protected MedicationOrder originalPrescriptionTarget;

    /**
     * The party to be reimbursed for the services.
     */
    @Child(name = "payee", type = {}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payee", formalDefinition="The party to be reimbursed for the services." )
    protected PayeeComponent payee;

    /**
     * The referral resource which lists the date, practitioner, reason and other supporting information.
     */
    @Child(name = "referral", type = {ReferralRequest.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Treatment Referral", formalDefinition="The referral resource which lists the date, practitioner, reason and other supporting information." )
    protected Reference referral;

    /**
     * The actual object that is the target of the reference (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    protected ReferralRequest referralTarget;

    /**
     * Ordered list of patient diagnosis for which care is sought.
     */
    @Child(name = "diagnosis", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Diagnosis", formalDefinition="Ordered list of patient diagnosis for which care is sought." )
    protected List<DiagnosisComponent> diagnosis;

    /**
     * List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.
     */
    @Child(name = "specialCondition", type = {Coding.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="List of special Conditions", formalDefinition="List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication." )
    protected List<Coding> specialCondition;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Patient.class}, order=21, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient Resource.)
     */
    protected Patient patientTarget;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "coverage", type = {}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected List<CoverageComponent> coverage;

    /**
     * Factors which may influence the applicability of coverage.
     */
    @Child(name = "exception", type = {Coding.class}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Eligibility exceptions", formalDefinition="Factors which may influence the applicability of coverage." )
    protected List<Coding> exception;

    /**
     * Name of school for over-aged dependants.
     */
    @Child(name = "school", type = {StringType.class}, order=24, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of School", formalDefinition="Name of school for over-aged dependants." )
    protected StringType school;

    /**
     * Date of an accident which these services are addressing.
     */
    @Child(name = "accidentDate", type = {DateType.class}, order=25, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Accident Date", formalDefinition="Date of an accident which these services are addressing." )
    protected DateType accidentDate;

    /**
     * Type of accident: work, auto, etc.
     */
    @Child(name = "accidentType", type = {Coding.class}, order=26, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Accident Type", formalDefinition="Type of accident: work, auto, etc." )
    protected Coding accidentType;

    /**
     * Accident Place.
     */
    @Child(name = "accidentLocation", type = {StringType.class, Address.class, Location.class}, order=27, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Accident Place", formalDefinition="Accident Place." )
    protected Type accidentLocation;

    /**
     * A list of intervention and exception codes which may influence the adjudication of the claim.
     */
    @Child(name = "interventionException", type = {Coding.class}, order=28, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Intervention and exception code (Pharma)", formalDefinition="A list of intervention and exception codes which may influence the adjudication of the claim." )
    protected List<Coding> interventionException;

    /**
     * The start or start and end dates for the treatable condition.
     */
    @Child(name = "onset", type = {DateType.class, Period.class}, order=29, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Illness, injury or treatable condition date", formalDefinition="The start or start and end dates for the treatable condition." )
    protected Type onset;

    /**
     * The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).
     */
    @Child(name = "employmentImpacted", type = {Period.class}, order=30, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period unable to work", formalDefinition="The start and optional end dates of when the patient was precluded from working due to the treatable condition(s)." )
    protected Period employmentImpacted;

    /**
     * The start and optional end dates of when the patient was confined to a treatment center.
     */
    @Child(name = "hospitalization", type = {Period.class}, order=31, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period in hospital", formalDefinition="The start and optional end dates of when the patient was confined to a treatment center." )
    protected Period hospitalization;

    /**
     * First tier of goods and services.
     */
    @Child(name = "item", type = {}, order=32, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Goods and Services", formalDefinition="First tier of goods and services." )
    protected List<ItemsComponent> item;

    /**
     * The total value of the claim.
     */
    @Child(name = "total", type = {Money.class}, order=33, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total claim cost", formalDefinition="The total value of the claim." )
    protected Money total;

    /**
     * Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.
     */
    @Child(name = "additionalMaterials", type = {Coding.class}, order=34, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional materials, documents, etc.", formalDefinition="Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission." )
    protected List<Coding> additionalMaterials;

    /**
     * A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.
     */
    @Child(name = "missingTeeth", type = {}, order=35, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Only if type = oral", formalDefinition="A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons." )
    protected List<MissingTeethComponent> missingTeeth;

    private static final long serialVersionUID = -763997683L;

  /**
   * Constructor
   */
    public Claim() {
      super();
    }

  /**
   * Constructor
   */
    public Claim(Enumeration<ClaimType> type, Reference patient) {
      super();
      this.type = type;
      this.patient = patient;
    }

    /**
     * @return {@link #type} (The category of claim this is.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<ClaimType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<ClaimType>(new ClaimTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The category of claim this is.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Claim setTypeElement(Enumeration<ClaimType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The category of claim this is.
     */
    public ClaimType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The category of claim this is.
     */
    public Claim setType(ClaimType value) { 
        if (this.type == null)
          this.type = new Enumeration<ClaimType>(new ClaimTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #identifier} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
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
     * @return {@link #identifier} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
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
    public Claim addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #ruleset} (The version of the specification on which this instance relies.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.ruleset");
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
    public Claim setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The version of the specification from which the original instance was created.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.originalRuleset");
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
    public Claim setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.created");
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
    public Claim setCreatedElement(DateTimeType value) { 
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
    public Claim setCreated(Date value) { 
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
          throw new Error("Attempt to auto-create Claim.billablePeriod");
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
    public Claim setBillablePeriod(Period value) { 
      this.billablePeriod = value;
      return this;
    }

    /**
     * @return {@link #target} (Insurer Identifier, typical BIN number (6 digit).)
     */
    public Reference getTarget() { 
      if (this.target == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.target");
        else if (Configuration.doAutoCreate())
          this.target = new Reference(); // cc
      return this.target;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (Insurer Identifier, typical BIN number (6 digit).)
     */
    public Claim setTarget(Reference value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Insurer Identifier, typical BIN number (6 digit).)
     */
    public Organization getTargetTarget() { 
      if (this.targetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.target");
        else if (Configuration.doAutoCreate())
          this.targetTarget = new Organization(); // aa
      return this.targetTarget;
    }

    /**
     * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Insurer Identifier, typical BIN number (6 digit).)
     */
    public Claim setTargetTarget(Organization value) { 
      this.targetTarget = value;
      return this;
    }

    /**
     * @return {@link #provider} (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.provider");
        else if (Configuration.doAutoCreate())
          this.provider = new Reference(); // cc
      return this.provider;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Claim setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Practitioner getProviderTarget() { 
      if (this.providerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.provider");
        else if (Configuration.doAutoCreate())
          this.providerTarget = new Practitioner(); // aa
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Claim setProviderTarget(Practitioner value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Claim setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Claim setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #use} (Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Enumeration<Use> getUseElement() { 
      if (this.use == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.use");
        else if (Configuration.doAutoCreate())
          this.use = new Enumeration<Use>(new UseEnumFactory()); // bb
      return this.use;
    }

    public boolean hasUseElement() { 
      return this.use != null && !this.use.isEmpty();
    }

    public boolean hasUse() { 
      return this.use != null && !this.use.isEmpty();
    }

    /**
     * @param value {@link #use} (Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Claim setUseElement(Enumeration<Use> value) { 
      this.use = value;
      return this;
    }

    /**
     * @return Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    public Use getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    public Claim setUse(Use value) { 
      if (value == null)
        this.use = null;
      else {
        if (this.use == null)
          this.use = new Enumeration<Use>(new UseEnumFactory());
        this.use.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public Coding getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Coding(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public Claim setPriority(Coding value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #fundsReserve} (In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.)
     */
    public Coding getFundsReserve() { 
      if (this.fundsReserve == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.fundsReserve");
        else if (Configuration.doAutoCreate())
          this.fundsReserve = new Coding(); // cc
      return this.fundsReserve;
    }

    public boolean hasFundsReserve() { 
      return this.fundsReserve != null && !this.fundsReserve.isEmpty();
    }

    /**
     * @param value {@link #fundsReserve} (In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.)
     */
    public Claim setFundsReserve(Coding value) { 
      this.fundsReserve = value;
      return this;
    }

    /**
     * @return {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Reference getEnterer() { 
      if (this.enterer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.enterer");
        else if (Configuration.doAutoCreate())
          this.enterer = new Reference(); // cc
      return this.enterer;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Claim setEnterer(Reference value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #enterer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Practitioner getEntererTarget() { 
      if (this.entererTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.enterer");
        else if (Configuration.doAutoCreate())
          this.entererTarget = new Practitioner(); // aa
      return this.entererTarget;
    }

    /**
     * @param value {@link #enterer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Claim setEntererTarget(Practitioner value) { 
      this.entererTarget = value;
      return this;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Reference getFacility() { 
      if (this.facility == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.facility");
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
    public Claim setFacility(Reference value) { 
      this.facility = value;
      return this;
    }

    /**
     * @return {@link #facility} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public Location getFacilityTarget() { 
      if (this.facilityTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.facility");
        else if (Configuration.doAutoCreate())
          this.facilityTarget = new Location(); // aa
      return this.facilityTarget;
    }

    /**
     * @param value {@link #facility} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public Claim setFacilityTarget(Location value) { 
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
    public Claim addRelatedClaim(Reference t) { //3
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
          throw new Error("Attempt to auto-create Claim.prescription");
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
    public Claim setPrescription(Reference value) { 
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
    public Claim setPrescriptionTarget(Resource value) { 
      this.prescriptionTarget = value;
      return this;
    }

    /**
     * @return {@link #originalPrescription} (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Reference getOriginalPrescription() { 
      if (this.originalPrescription == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.originalPrescription");
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
    public Claim setOriginalPrescription(Reference value) { 
      this.originalPrescription = value;
      return this;
    }

    /**
     * @return {@link #originalPrescription} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public MedicationOrder getOriginalPrescriptionTarget() { 
      if (this.originalPrescriptionTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.originalPrescription");
        else if (Configuration.doAutoCreate())
          this.originalPrescriptionTarget = new MedicationOrder(); // aa
      return this.originalPrescriptionTarget;
    }

    /**
     * @param value {@link #originalPrescription} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Claim setOriginalPrescriptionTarget(MedicationOrder value) { 
      this.originalPrescriptionTarget = value;
      return this;
    }

    /**
     * @return {@link #payee} (The party to be reimbursed for the services.)
     */
    public PayeeComponent getPayee() { 
      if (this.payee == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.payee");
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
    public Claim setPayee(PayeeComponent value) { 
      this.payee = value;
      return this;
    }

    /**
     * @return {@link #referral} (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public Reference getReferral() { 
      if (this.referral == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.referral");
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
    public Claim setReferral(Reference value) { 
      this.referral = value;
      return this;
    }

    /**
     * @return {@link #referral} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public ReferralRequest getReferralTarget() { 
      if (this.referralTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.referral");
        else if (Configuration.doAutoCreate())
          this.referralTarget = new ReferralRequest(); // aa
      return this.referralTarget;
    }

    /**
     * @param value {@link #referral} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public Claim setReferralTarget(ReferralRequest value) { 
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
    public Claim addDiagnosis(DiagnosisComponent t) { //3
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
    public Claim addSpecialCondition(Coding t) { //3
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
          throw new Error("Attempt to auto-create Claim.patient");
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
    public Claim setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public Claim setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public List<CoverageComponent> getCoverage() { 
      if (this.coverage == null)
        this.coverage = new ArrayList<CoverageComponent>();
      return this.coverage;
    }

    public boolean hasCoverage() { 
      if (this.coverage == null)
        return false;
      for (CoverageComponent item : this.coverage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    // syntactic sugar
    public CoverageComponent addCoverage() { //3
      CoverageComponent t = new CoverageComponent();
      if (this.coverage == null)
        this.coverage = new ArrayList<CoverageComponent>();
      this.coverage.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addCoverage(CoverageComponent t) { //3
      if (t == null)
        return this;
      if (this.coverage == null)
        this.coverage = new ArrayList<CoverageComponent>();
      this.coverage.add(t);
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
    public Claim addException(Coding t) { //3
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
          throw new Error("Attempt to auto-create Claim.school");
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
    public Claim setSchoolElement(StringType value) { 
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
    public Claim setSchool(String value) { 
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
          throw new Error("Attempt to auto-create Claim.accidentDate");
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
    public Claim setAccidentDateElement(DateType value) { 
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
    public Claim setAccidentDate(Date value) { 
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
          throw new Error("Attempt to auto-create Claim.accidentType");
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
    public Claim setAccidentType(Coding value) { 
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
    public Claim setAccidentLocation(Type value) { 
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
    public Claim addInterventionException(Coding t) { //3
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
    public Claim setOnset(Type value) { 
      this.onset = value;
      return this;
    }

    /**
     * @return {@link #employmentImpacted} (The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).)
     */
    public Period getEmploymentImpacted() { 
      if (this.employmentImpacted == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.employmentImpacted");
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
    public Claim setEmploymentImpacted(Period value) { 
      this.employmentImpacted = value;
      return this;
    }

    /**
     * @return {@link #hospitalization} (The start and optional end dates of when the patient was confined to a treatment center.)
     */
    public Period getHospitalization() { 
      if (this.hospitalization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.hospitalization");
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
    public Claim setHospitalization(Period value) { 
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
    public Claim addItem(ItemsComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return {@link #total} (The total value of the claim.)
     */
    public Money getTotal() { 
      if (this.total == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.total");
        else if (Configuration.doAutoCreate())
          this.total = new Money(); // cc
      return this.total;
    }

    public boolean hasTotal() { 
      return this.total != null && !this.total.isEmpty();
    }

    /**
     * @param value {@link #total} (The total value of the claim.)
     */
    public Claim setTotal(Money value) { 
      this.total = value;
      return this;
    }

    /**
     * @return {@link #additionalMaterials} (Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.)
     */
    public List<Coding> getAdditionalMaterials() { 
      if (this.additionalMaterials == null)
        this.additionalMaterials = new ArrayList<Coding>();
      return this.additionalMaterials;
    }

    public boolean hasAdditionalMaterials() { 
      if (this.additionalMaterials == null)
        return false;
      for (Coding item : this.additionalMaterials)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #additionalMaterials} (Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.)
     */
    // syntactic sugar
    public Coding addAdditionalMaterials() { //3
      Coding t = new Coding();
      if (this.additionalMaterials == null)
        this.additionalMaterials = new ArrayList<Coding>();
      this.additionalMaterials.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addAdditionalMaterials(Coding t) { //3
      if (t == null)
        return this;
      if (this.additionalMaterials == null)
        this.additionalMaterials = new ArrayList<Coding>();
      this.additionalMaterials.add(t);
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
    public Claim addMissingTeeth(MissingTeethComponent t) { //3
      if (t == null)
        return this;
      if (this.missingTeeth == null)
        this.missingTeeth = new ArrayList<MissingTeethComponent>();
      this.missingTeeth.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "The category of claim this is.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("identifier", "Identifier", "The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("ruleset", "Coding", "The version of the specification on which this instance relies.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The version of the specification from which the original instance was created.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("billablePeriod", "Period", "The billable period for which charges are being submitted.", 0, java.lang.Integer.MAX_VALUE, billablePeriod));
        childrenList.add(new Property("target", "Reference(Organization)", "Insurer Identifier, typical BIN number (6 digit).", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The provider which is responsible for the bill, claim pre-determination, pre-authorization.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization which is responsible for the bill, claim pre-determination, pre-authorization.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("use", "code", "Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).", 0, java.lang.Integer.MAX_VALUE, use));
        childrenList.add(new Property("priority", "Coding", "Immediate (STAT), best effort (NORMAL), deferred (DEFER).", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("fundsReserve", "Coding", "In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.", 0, java.lang.Integer.MAX_VALUE, fundsReserve));
        childrenList.add(new Property("enterer", "Reference(Practitioner)", "Person who created the invoice/claim/pre-determination or pre-authorization.", 0, java.lang.Integer.MAX_VALUE, enterer));
        childrenList.add(new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, java.lang.Integer.MAX_VALUE, facility));
        childrenList.add(new Property("relatedClaim", "Reference(Claim)", "Other claims which are related to this claim such as prior claim versions or for related services.", 0, java.lang.Integer.MAX_VALUE, relatedClaim));
        childrenList.add(new Property("prescription", "Reference(MedicationOrder|VisionPrescription)", "Prescription to support the dispensing of Pharmacy or Vision products.", 0, java.lang.Integer.MAX_VALUE, prescription));
        childrenList.add(new Property("originalPrescription", "Reference(MedicationOrder)", "Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.", 0, java.lang.Integer.MAX_VALUE, originalPrescription));
        childrenList.add(new Property("payee", "", "The party to be reimbursed for the services.", 0, java.lang.Integer.MAX_VALUE, payee));
        childrenList.add(new Property("referral", "Reference(ReferralRequest)", "The referral resource which lists the date, practitioner, reason and other supporting information.", 0, java.lang.Integer.MAX_VALUE, referral));
        childrenList.add(new Property("diagnosis", "", "Ordered list of patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        childrenList.add(new Property("specialCondition", "Coding", "List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.", 0, java.lang.Integer.MAX_VALUE, specialCondition));
        childrenList.add(new Property("patient", "Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, patient));
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
        childrenList.add(new Property("total", "Money", "The total value of the claim.", 0, java.lang.Integer.MAX_VALUE, total));
        childrenList.add(new Property("additionalMaterials", "Coding", "Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.", 0, java.lang.Integer.MAX_VALUE, additionalMaterials));
        childrenList.add(new Property("missingTeeth", "", "A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.", 0, java.lang.Integer.MAX_VALUE, missingTeeth));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new ClaimTypeEnumFactory().fromType(value); // Enumeration<ClaimType>
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("ruleset"))
          this.ruleset = castToCoding(value); // Coding
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("billablePeriod"))
          this.billablePeriod = castToPeriod(value); // Period
        else if (name.equals("target"))
          this.target = castToReference(value); // Reference
        else if (name.equals("provider"))
          this.provider = castToReference(value); // Reference
        else if (name.equals("organization"))
          this.organization = castToReference(value); // Reference
        else if (name.equals("use"))
          this.use = new UseEnumFactory().fromType(value); // Enumeration<Use>
        else if (name.equals("priority"))
          this.priority = castToCoding(value); // Coding
        else if (name.equals("fundsReserve"))
          this.fundsReserve = castToCoding(value); // Coding
        else if (name.equals("enterer"))
          this.enterer = castToReference(value); // Reference
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
        else if (name.equals("coverage"))
          this.getCoverage().add((CoverageComponent) value);
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
        else if (name.equals("total"))
          this.total = castToMoney(value); // Money
        else if (name.equals("additionalMaterials"))
          this.getAdditionalMaterials().add(castToCoding(value));
        else if (name.equals("missingTeeth"))
          this.getMissingTeeth().add((MissingTeethComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.type");
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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.created");
        }
        else if (name.equals("billablePeriod")) {
          this.billablePeriod = new Period();
          return this.billablePeriod;
        }
        else if (name.equals("target")) {
          this.target = new Reference();
          return this.target;
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("use")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.use");
        }
        else if (name.equals("priority")) {
          this.priority = new Coding();
          return this.priority;
        }
        else if (name.equals("fundsReserve")) {
          this.fundsReserve = new Coding();
          return this.fundsReserve;
        }
        else if (name.equals("enterer")) {
          this.enterer = new Reference();
          return this.enterer;
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
        else if (name.equals("coverage")) {
          return addCoverage();
        }
        else if (name.equals("exception")) {
          return addException();
        }
        else if (name.equals("school")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.school");
        }
        else if (name.equals("accidentDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.accidentDate");
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
        else if (name.equals("total")) {
          this.total = new Money();
          return this.total;
        }
        else if (name.equals("additionalMaterials")) {
          return addAdditionalMaterials();
        }
        else if (name.equals("missingTeeth")) {
          return addMissingTeeth();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Claim";

  }

      public Claim copy() {
        Claim dst = new Claim();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.billablePeriod = billablePeriod == null ? null : billablePeriod.copy();
        dst.target = target == null ? null : target.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.use = use == null ? null : use.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.fundsReserve = fundsReserve == null ? null : fundsReserve.copy();
        dst.enterer = enterer == null ? null : enterer.copy();
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
        if (coverage != null) {
          dst.coverage = new ArrayList<CoverageComponent>();
          for (CoverageComponent i : coverage)
            dst.coverage.add(i.copy());
        };
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
        dst.total = total == null ? null : total.copy();
        if (additionalMaterials != null) {
          dst.additionalMaterials = new ArrayList<Coding>();
          for (Coding i : additionalMaterials)
            dst.additionalMaterials.add(i.copy());
        };
        if (missingTeeth != null) {
          dst.missingTeeth = new ArrayList<MissingTeethComponent>();
          for (MissingTeethComponent i : missingTeeth)
            dst.missingTeeth.add(i.copy());
        };
        return dst;
      }

      protected Claim typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Claim))
          return false;
        Claim o = (Claim) other;
        return compareDeep(type, o.type, true) && compareDeep(identifier, o.identifier, true) && compareDeep(ruleset, o.ruleset, true)
           && compareDeep(originalRuleset, o.originalRuleset, true) && compareDeep(created, o.created, true)
           && compareDeep(billablePeriod, o.billablePeriod, true) && compareDeep(target, o.target, true) && compareDeep(provider, o.provider, true)
           && compareDeep(organization, o.organization, true) && compareDeep(use, o.use, true) && compareDeep(priority, o.priority, true)
           && compareDeep(fundsReserve, o.fundsReserve, true) && compareDeep(enterer, o.enterer, true) && compareDeep(facility, o.facility, true)
           && compareDeep(relatedClaim, o.relatedClaim, true) && compareDeep(prescription, o.prescription, true)
           && compareDeep(originalPrescription, o.originalPrescription, true) && compareDeep(payee, o.payee, true)
           && compareDeep(referral, o.referral, true) && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(specialCondition, o.specialCondition, true)
           && compareDeep(patient, o.patient, true) && compareDeep(coverage, o.coverage, true) && compareDeep(exception, o.exception, true)
           && compareDeep(school, o.school, true) && compareDeep(accidentDate, o.accidentDate, true) && compareDeep(accidentType, o.accidentType, true)
           && compareDeep(accidentLocation, o.accidentLocation, true) && compareDeep(interventionException, o.interventionException, true)
           && compareDeep(onset, o.onset, true) && compareDeep(employmentImpacted, o.employmentImpacted, true)
           && compareDeep(hospitalization, o.hospitalization, true) && compareDeep(item, o.item, true) && compareDeep(total, o.total, true)
           && compareDeep(additionalMaterials, o.additionalMaterials, true) && compareDeep(missingTeeth, o.missingTeeth, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Claim))
          return false;
        Claim o = (Claim) other;
        return compareValues(type, o.type, true) && compareValues(created, o.created, true) && compareValues(use, o.use, true)
           && compareValues(school, o.school, true) && compareValues(accidentDate, o.accidentDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (ruleset == null || ruleset.isEmpty()) && (originalRuleset == null || originalRuleset.isEmpty())
           && (created == null || created.isEmpty()) && (billablePeriod == null || billablePeriod.isEmpty())
           && (target == null || target.isEmpty()) && (provider == null || provider.isEmpty()) && (organization == null || organization.isEmpty())
           && (use == null || use.isEmpty()) && (priority == null || priority.isEmpty()) && (fundsReserve == null || fundsReserve.isEmpty())
           && (enterer == null || enterer.isEmpty()) && (facility == null || facility.isEmpty()) && (relatedClaim == null || relatedClaim.isEmpty())
           && (prescription == null || prescription.isEmpty()) && (originalPrescription == null || originalPrescription.isEmpty())
           && (payee == null || payee.isEmpty()) && (referral == null || referral.isEmpty()) && (diagnosis == null || diagnosis.isEmpty())
           && (specialCondition == null || specialCondition.isEmpty()) && (patient == null || patient.isEmpty())
           && (coverage == null || coverage.isEmpty()) && (exception == null || exception.isEmpty())
           && (school == null || school.isEmpty()) && (accidentDate == null || accidentDate.isEmpty())
           && (accidentType == null || accidentType.isEmpty()) && (accidentLocation == null || accidentLocation.isEmpty())
           && (interventionException == null || interventionException.isEmpty()) && (onset == null || onset.isEmpty())
           && (employmentImpacted == null || employmentImpacted.isEmpty()) && (hospitalization == null || hospitalization.isEmpty())
           && (item == null || item.isEmpty()) && (total == null || total.isEmpty()) && (additionalMaterials == null || additionalMaterials.isEmpty())
           && (missingTeeth == null || missingTeeth.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Claim;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The primary identifier of the financial resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Claim.identifier", description="The primary identifier of the financial resource", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The primary identifier of the financial resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>provider</b>
   * <p>
   * Description: <b>Provider responsible for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provider", path="Claim.provider", description="Provider responsible for the Claim", type="reference" )
  public static final String SP_PROVIDER = "provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provider</b>
   * <p>
   * Description: <b>Provider responsible for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.provider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDER = new ca.uhn.fhir.model.api.Include("Claim:provider").toLocked();

 /**
   * Search parameter: <b>use</b>
   * <p>
   * Description: <b>The kind of financial resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.use</b><br>
   * </p>
   */
  @SearchParamDefinition(name="use", path="Claim.use", description="The kind of financial resource", type="token" )
  public static final String SP_USE = "use";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>use</b>
   * <p>
   * Description: <b>The kind of financial resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.use</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam USE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_USE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Patient receiveing the services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Claim.patient", description="Patient receiveing the services", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Patient receiveing the services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Claim:patient").toLocked();

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>The creation date for the Claim</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Claim.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="Claim.created", description="The creation date for the Claim", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>The creation date for the Claim</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Claim.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Claim.organization", description="The reference to the providing organization", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("Claim:organization").toLocked();

 /**
   * Search parameter: <b>priority</b>
   * <p>
   * Description: <b>Processing priority requested</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.priority</b><br>
   * </p>
   */
  @SearchParamDefinition(name="priority", path="Claim.priority", description="Processing priority requested", type="token" )
  public static final String SP_PRIORITY = "priority";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>priority</b>
   * <p>
   * Description: <b>Processing priority requested</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.priority</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PRIORITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PRIORITY);

 /**
   * Search parameter: <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.facility</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facility", path="Claim.facility", description="Facility responsible for the goods and services", type="reference" )
  public static final String SP_FACILITY = "facility";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.facility</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FACILITY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FACILITY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:facility</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FACILITY = new ca.uhn.fhir.model.api.Include("Claim:facility").toLocked();

 /**
   * Search parameter: <b>target</b>
   * <p>
   * Description: <b>The target payor/insurer for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.target</b><br>
   * </p>
   */
  @SearchParamDefinition(name="target", path="Claim.target", description="The target payor/insurer for the Claim", type="reference" )
  public static final String SP_TARGET = "target";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>target</b>
   * <p>
   * Description: <b>The target payor/insurer for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.target</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam TARGET = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_TARGET);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:target</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_TARGET = new ca.uhn.fhir.model.api.Include("Claim:target").toLocked();


}

