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

import java.math.*;
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
 * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
 */
@ResourceDef(name="ExplanationOfBenefit", profile="http://hl7.org/fhir/Profile/ExplanationOfBenefit")
public class ExplanationOfBenefit extends DomainResource {

    public enum ExplanationOfBenefitStatus {
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
        public static ExplanationOfBenefitStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown ExplanationOfBenefitStatus code '"+codeString+"'");
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
            case ACTIVE: return "http://hl7.org/fhir/explanationofbenefit-status";
            case CANCELLED: return "http://hl7.org/fhir/explanationofbenefit-status";
            case DRAFT: return "http://hl7.org/fhir/explanationofbenefit-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/explanationofbenefit-status";
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

  public static class ExplanationOfBenefitStatusEnumFactory implements EnumFactory<ExplanationOfBenefitStatus> {
    public ExplanationOfBenefitStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ExplanationOfBenefitStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return ExplanationOfBenefitStatus.CANCELLED;
        if ("draft".equals(codeString))
          return ExplanationOfBenefitStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return ExplanationOfBenefitStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ExplanationOfBenefitStatus code '"+codeString+"'");
        }
        public Enumeration<ExplanationOfBenefitStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ExplanationOfBenefitStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<ExplanationOfBenefitStatus>(this, ExplanationOfBenefitStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<ExplanationOfBenefitStatus>(this, ExplanationOfBenefitStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<ExplanationOfBenefitStatus>(this, ExplanationOfBenefitStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ExplanationOfBenefitStatus>(this, ExplanationOfBenefitStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ExplanationOfBenefitStatus code '"+codeString+"'");
        }
    public String toCode(ExplanationOfBenefitStatus code) {
      if (code == ExplanationOfBenefitStatus.ACTIVE)
        return "active";
      if (code == ExplanationOfBenefitStatus.CANCELLED)
        return "cancelled";
      if (code == ExplanationOfBenefitStatus.DRAFT)
        return "draft";
      if (code == ExplanationOfBenefitStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ExplanationOfBenefitStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class RelatedClaimComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Other claims which are related to this claim such as prior claim versions or for related services.
         */
        @Child(name = "claim", type = {Claim.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference to the related claim", formalDefinition="Other claims which are related to this claim such as prior claim versions or for related services." )
        protected Reference claim;

        /**
         * The actual object that is the target of the reference (Other claims which are related to this claim such as prior claim versions or for related services.)
         */
        protected Claim claimTarget;

        /**
         * For example prior or umbrella.
         */
        @Child(name = "relationship", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How the reference claim is related", formalDefinition="For example prior or umbrella." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/related-claim-relationship")
        protected CodeableConcept relationship;

        /**
         * An alternate organizational reference to the case or file to which this particular claim pertains - eg Property/Casualy insurer claim # or Workers Compensation case # .
         */
        @Child(name = "reference", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Related file or case reference", formalDefinition="An alternate organizational reference to the case or file to which this particular claim pertains - eg Property/Casualy insurer claim # or Workers Compensation case # ." )
        protected Identifier reference;

        private static final long serialVersionUID = -379338905L;

    /**
     * Constructor
     */
      public RelatedClaimComponent() {
        super();
      }

        /**
         * @return {@link #claim} (Other claims which are related to this claim such as prior claim versions or for related services.)
         */
        public Reference getClaim() { 
          if (this.claim == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RelatedClaimComponent.claim");
            else if (Configuration.doAutoCreate())
              this.claim = new Reference(); // cc
          return this.claim;
        }

        public boolean hasClaim() { 
          return this.claim != null && !this.claim.isEmpty();
        }

        /**
         * @param value {@link #claim} (Other claims which are related to this claim such as prior claim versions or for related services.)
         */
        public RelatedClaimComponent setClaim(Reference value) { 
          this.claim = value;
          return this;
        }

        /**
         * @return {@link #claim} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Other claims which are related to this claim such as prior claim versions or for related services.)
         */
        public Claim getClaimTarget() { 
          if (this.claimTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RelatedClaimComponent.claim");
            else if (Configuration.doAutoCreate())
              this.claimTarget = new Claim(); // aa
          return this.claimTarget;
        }

        /**
         * @param value {@link #claim} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Other claims which are related to this claim such as prior claim versions or for related services.)
         */
        public RelatedClaimComponent setClaimTarget(Claim value) { 
          this.claimTarget = value;
          return this;
        }

        /**
         * @return {@link #relationship} (For example prior or umbrella.)
         */
        public CodeableConcept getRelationship() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RelatedClaimComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new CodeableConcept(); // cc
          return this.relationship;
        }

        public boolean hasRelationship() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        /**
         * @param value {@link #relationship} (For example prior or umbrella.)
         */
        public RelatedClaimComponent setRelationship(CodeableConcept value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return {@link #reference} (An alternate organizational reference to the case or file to which this particular claim pertains - eg Property/Casualy insurer claim # or Workers Compensation case # .)
         */
        public Identifier getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RelatedClaimComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Identifier(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (An alternate organizational reference to the case or file to which this particular claim pertains - eg Property/Casualy insurer claim # or Workers Compensation case # .)
         */
        public RelatedClaimComponent setReference(Identifier value) { 
          this.reference = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("claim", "Reference(Claim)", "Other claims which are related to this claim such as prior claim versions or for related services.", 0, java.lang.Integer.MAX_VALUE, claim));
          childrenList.add(new Property("relationship", "CodeableConcept", "For example prior or umbrella.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("reference", "Identifier", "An alternate organizational reference to the case or file to which this particular claim pertains - eg Property/Casualy insurer claim # or Workers Compensation case # .", 0, java.lang.Integer.MAX_VALUE, reference));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 94742588: /*claim*/ return this.claim == null ? new Base[0] : new Base[] {this.claim}; // Reference
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // CodeableConcept
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Identifier
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 94742588: // claim
          this.claim = castToReference(value); // Reference
          return value;
        case -261851592: // relationship
          this.relationship = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -925155509: // reference
          this.reference = castToIdentifier(value); // Identifier
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("claim")) {
          this.claim = castToReference(value); // Reference
        } else if (name.equals("relationship")) {
          this.relationship = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("reference")) {
          this.reference = castToIdentifier(value); // Identifier
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94742588:  return getClaim(); 
        case -261851592:  return getRelationship(); 
        case -925155509:  return getReference(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94742588: /*claim*/ return new String[] {"Reference"};
        case -261851592: /*relationship*/ return new String[] {"CodeableConcept"};
        case -925155509: /*reference*/ return new String[] {"Identifier"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("claim")) {
          this.claim = new Reference();
          return this.claim;
        }
        else if (name.equals("relationship")) {
          this.relationship = new CodeableConcept();
          return this.relationship;
        }
        else if (name.equals("reference")) {
          this.reference = new Identifier();
          return this.reference;
        }
        else
          return super.addChild(name);
      }

      public RelatedClaimComponent copy() {
        RelatedClaimComponent dst = new RelatedClaimComponent();
        copyValues(dst);
        dst.claim = claim == null ? null : claim.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        dst.reference = reference == null ? null : reference.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RelatedClaimComponent))
          return false;
        RelatedClaimComponent o = (RelatedClaimComponent) other;
        return compareDeep(claim, o.claim, true) && compareDeep(relationship, o.relationship, true) && compareDeep(reference, o.reference, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RelatedClaimComponent))
          return false;
        RelatedClaimComponent o = (RelatedClaimComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(claim, relationship, reference
          );
      }

  public String fhirType() {
    return "ExplanationOfBenefit.related";

  }

  }

    @Block()
    public static class PayeeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of Party to be reimbursed: Subscriber, provider, other.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of party: Subscriber, Provider, other", formalDefinition="Type of Party to be reimbursed: Subscriber, provider, other." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payeetype")
        protected CodeableConcept type;

        /**
         * organization | patient | practitioner | relatedperson.
         */
        @Child(name = "resourceType", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="organization | patient | practitioner | relatedperson", formalDefinition="organization | patient | practitioner | relatedperson." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-type-link")
        protected CodeableConcept resourceType;

        /**
         * Party to be reimbursed: Subscriber, provider, other.
         */
        @Child(name = "party", type = {Practitioner.class, Organization.class, Patient.class, RelatedPerson.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Party to receive the payable", formalDefinition="Party to be reimbursed: Subscriber, provider, other." )
        protected Reference party;

        /**
         * The actual object that is the target of the reference (Party to be reimbursed: Subscriber, provider, other.)
         */
        protected Resource partyTarget;

        private static final long serialVersionUID = 1146157718L;

    /**
     * Constructor
     */
      public PayeeComponent() {
        super();
      }

        /**
         * @return {@link #type} (Type of Party to be reimbursed: Subscriber, provider, other.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of Party to be reimbursed: Subscriber, provider, other.)
         */
        public PayeeComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #resourceType} (organization | patient | practitioner | relatedperson.)
         */
        public CodeableConcept getResourceType() { 
          if (this.resourceType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.resourceType");
            else if (Configuration.doAutoCreate())
              this.resourceType = new CodeableConcept(); // cc
          return this.resourceType;
        }

        public boolean hasResourceType() { 
          return this.resourceType != null && !this.resourceType.isEmpty();
        }

        /**
         * @param value {@link #resourceType} (organization | patient | practitioner | relatedperson.)
         */
        public PayeeComponent setResourceType(CodeableConcept value) { 
          this.resourceType = value;
          return this;
        }

        /**
         * @return {@link #party} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public Reference getParty() { 
          if (this.party == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.party");
            else if (Configuration.doAutoCreate())
              this.party = new Reference(); // cc
          return this.party;
        }

        public boolean hasParty() { 
          return this.party != null && !this.party.isEmpty();
        }

        /**
         * @param value {@link #party} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public PayeeComponent setParty(Reference value) { 
          this.party = value;
          return this;
        }

        /**
         * @return {@link #party} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Party to be reimbursed: Subscriber, provider, other.)
         */
        public Resource getPartyTarget() { 
          return this.partyTarget;
        }

        /**
         * @param value {@link #party} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Party to be reimbursed: Subscriber, provider, other.)
         */
        public PayeeComponent setPartyTarget(Resource value) { 
          this.partyTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Type of Party to be reimbursed: Subscriber, provider, other.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("resourceType", "CodeableConcept", "organization | patient | practitioner | relatedperson.", 0, java.lang.Integer.MAX_VALUE, resourceType));
          childrenList.add(new Property("party", "Reference(Practitioner|Organization|Patient|RelatedPerson)", "Party to be reimbursed: Subscriber, provider, other.", 0, java.lang.Integer.MAX_VALUE, party));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -384364440: /*resourceType*/ return this.resourceType == null ? new Base[0] : new Base[] {this.resourceType}; // CodeableConcept
        case 106437350: /*party*/ return this.party == null ? new Base[0] : new Base[] {this.party}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -384364440: // resourceType
          this.resourceType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 106437350: // party
          this.party = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("resourceType")) {
          this.resourceType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("party")) {
          this.party = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -384364440:  return getResourceType(); 
        case 106437350:  return getParty(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -384364440: /*resourceType*/ return new String[] {"CodeableConcept"};
        case 106437350: /*party*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("resourceType")) {
          this.resourceType = new CodeableConcept();
          return this.resourceType;
        }
        else if (name.equals("party")) {
          this.party = new Reference();
          return this.party;
        }
        else
          return super.addChild(name);
      }

      public PayeeComponent copy() {
        PayeeComponent dst = new PayeeComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.resourceType = resourceType == null ? null : resourceType.copy();
        dst.party = party == null ? null : party.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PayeeComponent))
          return false;
        PayeeComponent o = (PayeeComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(resourceType, o.resourceType, true) && compareDeep(party, o.party, true)
          ;
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, resourceType, party
          );
      }

  public String fhirType() {
    return "ExplanationOfBenefit.payee";

  }

  }

    @Block()
    public static class SupportingInformationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Sequence of the information element which serves to provide a link.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Information instance identifier", formalDefinition="Sequence of the information element which serves to provide a link." )
        protected PositiveIntType sequence;

        /**
         * The general class of the information supplied: information; exception; accident, employment; onset, etc.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="General class of information", formalDefinition="The general class of the information supplied: information; exception; accident, employment; onset, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-informationcategory")
        protected CodeableConcept category;

        /**
         * System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of information", formalDefinition="System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-exception")
        protected CodeableConcept code;

        /**
         * The date when or period to which this information refers.
         */
        @Child(name = "timing", type = {DateType.class, Period.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When it occurred", formalDefinition="The date when or period to which this information refers." )
        protected Type timing;

        /**
         * Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.
         */
        @Child(name = "value", type = {StringType.class, Quantity.class, Attachment.class, Reference.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional Data or supporting information", formalDefinition="Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data." )
        protected Type value;

        /**
         * For example, provides the reason for: the additional stay, or missing tooth or any other situation where a reason code is required in addition to the content.
         */
        @Child(name = "reason", type = {Coding.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason associated with the information", formalDefinition="For example, provides the reason for: the additional stay, or missing tooth or any other situation where a reason code is required in addition to the content." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/missing-tooth-reason")
        protected Coding reason;

        private static final long serialVersionUID = -410136661L;

    /**
     * Constructor
     */
      public SupportingInformationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SupportingInformationComponent(PositiveIntType sequence, CodeableConcept category) {
        super();
        this.sequence = sequence;
        this.category = category;
      }

        /**
         * @return {@link #sequence} (Sequence of the information element which serves to provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingInformationComponent.sequence");
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
         * @param value {@link #sequence} (Sequence of the information element which serves to provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public SupportingInformationComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Sequence of the information element which serves to provide a link.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value Sequence of the information element which serves to provide a link.
         */
        public SupportingInformationComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #category} (The general class of the information supplied: information; exception; accident, employment; onset, etc.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingInformationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (The general class of the information supplied: information; exception; accident, employment; onset, etc.)
         */
        public SupportingInformationComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #code} (System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingInformationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.)
         */
        public SupportingInformationComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #timing} (The date when or period to which this information refers.)
         */
        public Type getTiming() { 
          return this.timing;
        }

        /**
         * @return {@link #timing} (The date when or period to which this information refers.)
         */
        public DateType getTimingDateType() throws FHIRException { 
          if (!(this.timing instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (DateType) this.timing;
        }

        public boolean hasTimingDateType() { 
          return this.timing instanceof DateType;
        }

        /**
         * @return {@link #timing} (The date when or period to which this information refers.)
         */
        public Period getTimingPeriod() throws FHIRException { 
          if (!(this.timing instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (Period) this.timing;
        }

        public boolean hasTimingPeriod() { 
          return this.timing instanceof Period;
        }

        public boolean hasTiming() { 
          return this.timing != null && !this.timing.isEmpty();
        }

        /**
         * @param value {@link #timing} (The date when or period to which this information refers.)
         */
        public SupportingInformationComponent setTiming(Type value) { 
          this.timing = value;
          return this;
        }

        /**
         * @return {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public Quantity getValueQuantity() throws FHIRException { 
          if (!(this.value instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        public boolean hasValueQuantity() { 
          return this.value instanceof Quantity;
        }

        /**
         * @return {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public Attachment getValueAttachment() throws FHIRException { 
          if (!(this.value instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Attachment) this.value;
        }

        public boolean hasValueAttachment() { 
          return this.value instanceof Attachment;
        }

        /**
         * @return {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public Reference getValueReference() throws FHIRException { 
          if (!(this.value instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Reference) this.value;
        }

        public boolean hasValueReference() { 
          return this.value instanceof Reference;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public SupportingInformationComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        /**
         * @return {@link #reason} (For example, provides the reason for: the additional stay, or missing tooth or any other situation where a reason code is required in addition to the content.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingInformationComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new Coding(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (For example, provides the reason for: the additional stay, or missing tooth or any other situation where a reason code is required in addition to the content.)
         */
        public SupportingInformationComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "Sequence of the information element which serves to provide a link.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("category", "CodeableConcept", "The general class of the information supplied: information; exception; accident, employment; onset, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("code", "CodeableConcept", "System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("timing[x]", "date|Period", "The date when or period to which this information refers.", 0, java.lang.Integer.MAX_VALUE, timing));
          childrenList.add(new Property("value[x]", "string|Quantity|Attachment|Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("reason", "Coding", "For example, provides the reason for: the additional stay, or missing tooth or any other situation where a reason code is required in addition to the content.", 0, java.lang.Integer.MAX_VALUE, reason));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -873664438: // timing
          this.timing = castToType(value); // Type
          return value;
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        case -934964668: // reason
          this.reason = castToCoding(value); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("timing[x]")) {
          this.timing = castToType(value); // Type
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else if (name.equals("reason")) {
          this.reason = castToCoding(value); // Coding
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 50511102:  return getCategory(); 
        case 3059181:  return getCode(); 
        case 164632566:  return getTiming(); 
        case -873664438:  return getTiming(); 
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        case -934964668:  return getReason(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -873664438: /*timing*/ return new String[] {"date", "Period"};
        case 111972721: /*value*/ return new String[] {"string", "Quantity", "Attachment", "Reference"};
        case -934964668: /*reason*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("timingDate")) {
          this.timing = new DateType();
          return this.timing;
        }
        else if (name.equals("timingPeriod")) {
          this.timing = new Period();
          return this.timing;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else if (name.equals("reason")) {
          this.reason = new Coding();
          return this.reason;
        }
        else
          return super.addChild(name);
      }

      public SupportingInformationComponent copy() {
        SupportingInformationComponent dst = new SupportingInformationComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.timing = timing == null ? null : timing.copy();
        dst.value = value == null ? null : value.copy();
        dst.reason = reason == null ? null : reason.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SupportingInformationComponent))
          return false;
        SupportingInformationComponent o = (SupportingInformationComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(category, o.category, true) && compareDeep(code, o.code, true)
           && compareDeep(timing, o.timing, true) && compareDeep(value, o.value, true) && compareDeep(reason, o.reason, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupportingInformationComponent))
          return false;
        SupportingInformationComponent o = (SupportingInformationComponent) other;
        return compareValues(sequence, o.sequence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, category, code
          , timing, value, reason);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.information";

  }

  }

    @Block()
    public static class CareTeamComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Sequence of careteam which serves to order and provide a link.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Number to covey order of careteam", formalDefinition="Sequence of careteam which serves to order and provide a link." )
        protected PositiveIntType sequence;

        /**
         * The members of the team who provided the overall service.
         */
        @Child(name = "provider", type = {Practitioner.class, Organization.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Member of the Care Team", formalDefinition="The members of the team who provided the overall service." )
        protected Reference provider;

        /**
         * The actual object that is the target of the reference (The members of the team who provided the overall service.)
         */
        protected Resource providerTarget;

        /**
         * The practitioner who is billing and responsible for the claimed services rendered to the patient.
         */
        @Child(name = "responsible", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing practitioner", formalDefinition="The practitioner who is billing and responsible for the claimed services rendered to the patient." )
        protected BooleanType responsible;

        /**
         * The lead, assisting or supervising practitioner and their discipline if a multidisiplinary team.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Role on the team", formalDefinition="The lead, assisting or supervising practitioner and their discipline if a multidisiplinary team." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-careteamrole")
        protected CodeableConcept role;

        /**
         * The qualification which is applicable for this service.
         */
        @Child(name = "qualification", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type, classification or Specialization", formalDefinition="The qualification which is applicable for this service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/provider-qualification")
        protected CodeableConcept qualification;

        private static final long serialVersionUID = 1758966968L;

    /**
     * Constructor
     */
      public CareTeamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CareTeamComponent(PositiveIntType sequence, Reference provider) {
        super();
        this.sequence = sequence;
        this.provider = provider;
      }

        /**
         * @return {@link #sequence} (Sequence of careteam which serves to order and provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamComponent.sequence");
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
         * @param value {@link #sequence} (Sequence of careteam which serves to order and provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public CareTeamComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Sequence of careteam which serves to order and provide a link.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value Sequence of careteam which serves to order and provide a link.
         */
        public CareTeamComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #provider} (The members of the team who provided the overall service.)
         */
        public Reference getProvider() { 
          if (this.provider == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamComponent.provider");
            else if (Configuration.doAutoCreate())
              this.provider = new Reference(); // cc
          return this.provider;
        }

        public boolean hasProvider() { 
          return this.provider != null && !this.provider.isEmpty();
        }

        /**
         * @param value {@link #provider} (The members of the team who provided the overall service.)
         */
        public CareTeamComponent setProvider(Reference value) { 
          this.provider = value;
          return this;
        }

        /**
         * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The members of the team who provided the overall service.)
         */
        public Resource getProviderTarget() { 
          return this.providerTarget;
        }

        /**
         * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The members of the team who provided the overall service.)
         */
        public CareTeamComponent setProviderTarget(Resource value) { 
          this.providerTarget = value;
          return this;
        }

        /**
         * @return {@link #responsible} (The practitioner who is billing and responsible for the claimed services rendered to the patient.). This is the underlying object with id, value and extensions. The accessor "getResponsible" gives direct access to the value
         */
        public BooleanType getResponsibleElement() { 
          if (this.responsible == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamComponent.responsible");
            else if (Configuration.doAutoCreate())
              this.responsible = new BooleanType(); // bb
          return this.responsible;
        }

        public boolean hasResponsibleElement() { 
          return this.responsible != null && !this.responsible.isEmpty();
        }

        public boolean hasResponsible() { 
          return this.responsible != null && !this.responsible.isEmpty();
        }

        /**
         * @param value {@link #responsible} (The practitioner who is billing and responsible for the claimed services rendered to the patient.). This is the underlying object with id, value and extensions. The accessor "getResponsible" gives direct access to the value
         */
        public CareTeamComponent setResponsibleElement(BooleanType value) { 
          this.responsible = value;
          return this;
        }

        /**
         * @return The practitioner who is billing and responsible for the claimed services rendered to the patient.
         */
        public boolean getResponsible() { 
          return this.responsible == null || this.responsible.isEmpty() ? false : this.responsible.getValue();
        }

        /**
         * @param value The practitioner who is billing and responsible for the claimed services rendered to the patient.
         */
        public CareTeamComponent setResponsible(boolean value) { 
            if (this.responsible == null)
              this.responsible = new BooleanType();
            this.responsible.setValue(value);
          return this;
        }

        /**
         * @return {@link #role} (The lead, assisting or supervising practitioner and their discipline if a multidisiplinary team.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (The lead, assisting or supervising practitioner and their discipline if a multidisiplinary team.)
         */
        public CareTeamComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #qualification} (The qualification which is applicable for this service.)
         */
        public CodeableConcept getQualification() { 
          if (this.qualification == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamComponent.qualification");
            else if (Configuration.doAutoCreate())
              this.qualification = new CodeableConcept(); // cc
          return this.qualification;
        }

        public boolean hasQualification() { 
          return this.qualification != null && !this.qualification.isEmpty();
        }

        /**
         * @param value {@link #qualification} (The qualification which is applicable for this service.)
         */
        public CareTeamComponent setQualification(CodeableConcept value) { 
          this.qualification = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "Sequence of careteam which serves to order and provide a link.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("provider", "Reference(Practitioner|Organization)", "The members of the team who provided the overall service.", 0, java.lang.Integer.MAX_VALUE, provider));
          childrenList.add(new Property("responsible", "boolean", "The practitioner who is billing and responsible for the claimed services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, responsible));
          childrenList.add(new Property("role", "CodeableConcept", "The lead, assisting or supervising practitioner and their discipline if a multidisiplinary team.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("qualification", "CodeableConcept", "The qualification which is applicable for this service.", 0, java.lang.Integer.MAX_VALUE, qualification));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case 1847674614: /*responsible*/ return this.responsible == null ? new Base[0] : new Base[] {this.responsible}; // BooleanType
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case -631333393: /*qualification*/ return this.qualification == null ? new Base[0] : new Base[] {this.qualification}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case -987494927: // provider
          this.provider = castToReference(value); // Reference
          return value;
        case 1847674614: // responsible
          this.responsible = castToBoolean(value); // BooleanType
          return value;
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -631333393: // qualification
          this.qualification = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("provider")) {
          this.provider = castToReference(value); // Reference
        } else if (name.equals("responsible")) {
          this.responsible = castToBoolean(value); // BooleanType
        } else if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("qualification")) {
          this.qualification = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case -987494927:  return getProvider(); 
        case 1847674614:  return getResponsibleElement();
        case 3506294:  return getRole(); 
        case -631333393:  return getQualification(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case 1847674614: /*responsible*/ return new String[] {"boolean"};
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case -631333393: /*qualification*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("responsible")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.responsible");
        }
        else if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("qualification")) {
          this.qualification = new CodeableConcept();
          return this.qualification;
        }
        else
          return super.addChild(name);
      }

      public CareTeamComponent copy() {
        CareTeamComponent dst = new CareTeamComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.responsible = responsible == null ? null : responsible.copy();
        dst.role = role == null ? null : role.copy();
        dst.qualification = qualification == null ? null : qualification.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CareTeamComponent))
          return false;
        CareTeamComponent o = (CareTeamComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(provider, o.provider, true) && compareDeep(responsible, o.responsible, true)
           && compareDeep(role, o.role, true) && compareDeep(qualification, o.qualification, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CareTeamComponent))
          return false;
        CareTeamComponent o = (CareTeamComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(responsible, o.responsible, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, provider, responsible
          , role, qualification);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.careTeam";

  }

  }

    @Block()
    public static class DiagnosisComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Sequence of diagnosis which serves to provide a link.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Number to covey order of diagnosis", formalDefinition="Sequence of diagnosis which serves to provide a link." )
        protected PositiveIntType sequence;

        /**
         * The diagnosis.
         */
        @Child(name = "diagnosis", type = {CodeableConcept.class, Condition.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Patient's diagnosis", formalDefinition="The diagnosis." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/icd-10")
        protected Type diagnosis;

        /**
         * The type of the Diagnosis, for example: admitting, primary, secondary, discharge.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Timing or nature of the diagnosis", formalDefinition="The type of the Diagnosis, for example: admitting, primary, secondary, discharge." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-diagnosistype")
        protected List<CodeableConcept> type;

        /**
         * The package billing code, for example DRG, based on the assigned grouping code system.
         */
        @Child(name = "packageCode", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Package billing code", formalDefinition="The package billing code, for example DRG, based on the assigned grouping code system." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-diagnosisrelatedgroup")
        protected CodeableConcept packageCode;

        private static final long serialVersionUID = -350960873L;

    /**
     * Constructor
     */
      public DiagnosisComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DiagnosisComponent(PositiveIntType sequence, Type diagnosis) {
        super();
        this.sequence = sequence;
        this.diagnosis = diagnosis;
      }

        /**
         * @return {@link #sequence} (Sequence of diagnosis which serves to provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
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
         * @param value {@link #sequence} (Sequence of diagnosis which serves to provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public DiagnosisComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Sequence of diagnosis which serves to provide a link.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value Sequence of diagnosis which serves to provide a link.
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
        public Type getDiagnosis() { 
          return this.diagnosis;
        }

        /**
         * @return {@link #diagnosis} (The diagnosis.)
         */
        public CodeableConcept getDiagnosisCodeableConcept() throws FHIRException { 
          if (!(this.diagnosis instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.diagnosis.getClass().getName()+" was encountered");
          return (CodeableConcept) this.diagnosis;
        }

        public boolean hasDiagnosisCodeableConcept() { 
          return this.diagnosis instanceof CodeableConcept;
        }

        /**
         * @return {@link #diagnosis} (The diagnosis.)
         */
        public Reference getDiagnosisReference() throws FHIRException { 
          if (!(this.diagnosis instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.diagnosis.getClass().getName()+" was encountered");
          return (Reference) this.diagnosis;
        }

        public boolean hasDiagnosisReference() { 
          return this.diagnosis instanceof Reference;
        }

        public boolean hasDiagnosis() { 
          return this.diagnosis != null && !this.diagnosis.isEmpty();
        }

        /**
         * @param value {@link #diagnosis} (The diagnosis.)
         */
        public DiagnosisComponent setDiagnosis(Type value) { 
          this.diagnosis = value;
          return this;
        }

        /**
         * @return {@link #type} (The type of the Diagnosis, for example: admitting, primary, secondary, discharge.)
         */
        public List<CodeableConcept> getType() { 
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          return this.type;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DiagnosisComponent setType(List<CodeableConcept> theType) { 
          this.type = theType;
          return this;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (CodeableConcept item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return t;
        }

        public DiagnosisComponent addType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #type}, creating it if it does not already exist
         */
        public CodeableConcept getTypeFirstRep() { 
          if (getType().isEmpty()) {
            addType();
          }
          return getType().get(0);
        }

        /**
         * @return {@link #packageCode} (The package billing code, for example DRG, based on the assigned grouping code system.)
         */
        public CodeableConcept getPackageCode() { 
          if (this.packageCode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosisComponent.packageCode");
            else if (Configuration.doAutoCreate())
              this.packageCode = new CodeableConcept(); // cc
          return this.packageCode;
        }

        public boolean hasPackageCode() { 
          return this.packageCode != null && !this.packageCode.isEmpty();
        }

        /**
         * @param value {@link #packageCode} (The package billing code, for example DRG, based on the assigned grouping code system.)
         */
        public DiagnosisComponent setPackageCode(CodeableConcept value) { 
          this.packageCode = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "Sequence of diagnosis which serves to provide a link.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The diagnosis.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
          childrenList.add(new Property("type", "CodeableConcept", "The type of the Diagnosis, for example: admitting, primary, secondary, discharge.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("packageCode", "CodeableConcept", "The package billing code, for example DRG, based on the assigned grouping code system.", 0, java.lang.Integer.MAX_VALUE, packageCode));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : new Base[] {this.diagnosis}; // Type
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case 908444499: /*packageCode*/ return this.packageCode == null ? new Base[0] : new Base[] {this.packageCode}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 1196993265: // diagnosis
          this.diagnosis = castToType(value); // Type
          return value;
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 908444499: // packageCode
          this.packageCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("diagnosis[x]")) {
          this.diagnosis = castToType(value); // Type
        } else if (name.equals("type")) {
          this.getType().add(castToCodeableConcept(value));
        } else if (name.equals("packageCode")) {
          this.packageCode = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case -1487009809:  return getDiagnosis(); 
        case 1196993265:  return getDiagnosis(); 
        case 3575610:  return addType(); 
        case 908444499:  return getPackageCode(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 1196993265: /*diagnosis*/ return new String[] {"CodeableConcept", "Reference"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 908444499: /*packageCode*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("diagnosisCodeableConcept")) {
          this.diagnosis = new CodeableConcept();
          return this.diagnosis;
        }
        else if (name.equals("diagnosisReference")) {
          this.diagnosis = new Reference();
          return this.diagnosis;
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("packageCode")) {
          this.packageCode = new CodeableConcept();
          return this.packageCode;
        }
        else
          return super.addChild(name);
      }

      public DiagnosisComponent copy() {
        DiagnosisComponent dst = new DiagnosisComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.diagnosis = diagnosis == null ? null : diagnosis.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.packageCode = packageCode == null ? null : packageCode.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DiagnosisComponent))
          return false;
        DiagnosisComponent o = (DiagnosisComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(type, o.type, true)
           && compareDeep(packageCode, o.packageCode, true);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, diagnosis, type
          , packageCode);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.diagnosis";

  }

  }

    @Block()
    public static class ProcedureComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Sequence of procedures which serves to order and provide a link.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Procedure sequence for reference", formalDefinition="Sequence of procedures which serves to order and provide a link." )
        protected PositiveIntType sequence;

        /**
         * Date and optionally time the procedure was performed .
         */
        @Child(name = "date", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When the procedure was performed", formalDefinition="Date and optionally time the procedure was performed ." )
        protected DateTimeType date;

        /**
         * The procedure code.
         */
        @Child(name = "procedure", type = {CodeableConcept.class, Procedure.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Patient's list of procedures performed", formalDefinition="The procedure code." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/icd-10-procedures")
        protected Type procedure;

        private static final long serialVersionUID = 864307347L;

    /**
     * Constructor
     */
      public ProcedureComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProcedureComponent(PositiveIntType sequence, Type procedure) {
        super();
        this.sequence = sequence;
        this.procedure = procedure;
      }

        /**
         * @return {@link #sequence} (Sequence of procedures which serves to order and provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureComponent.sequence");
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
         * @param value {@link #sequence} (Sequence of procedures which serves to order and provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public ProcedureComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Sequence of procedures which serves to order and provide a link.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value Sequence of procedures which serves to order and provide a link.
         */
        public ProcedureComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #date} (Date and optionally time the procedure was performed .). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateTimeType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureComponent.date");
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
         * @param value {@link #date} (Date and optionally time the procedure was performed .). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public ProcedureComponent setDateElement(DateTimeType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Date and optionally time the procedure was performed .
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Date and optionally time the procedure was performed .
         */
        public ProcedureComponent setDate(Date value) { 
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
         * @return {@link #procedure} (The procedure code.)
         */
        public Type getProcedure() { 
          return this.procedure;
        }

        /**
         * @return {@link #procedure} (The procedure code.)
         */
        public CodeableConcept getProcedureCodeableConcept() throws FHIRException { 
          if (!(this.procedure instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.procedure.getClass().getName()+" was encountered");
          return (CodeableConcept) this.procedure;
        }

        public boolean hasProcedureCodeableConcept() { 
          return this.procedure instanceof CodeableConcept;
        }

        /**
         * @return {@link #procedure} (The procedure code.)
         */
        public Reference getProcedureReference() throws FHIRException { 
          if (!(this.procedure instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.procedure.getClass().getName()+" was encountered");
          return (Reference) this.procedure;
        }

        public boolean hasProcedureReference() { 
          return this.procedure instanceof Reference;
        }

        public boolean hasProcedure() { 
          return this.procedure != null && !this.procedure.isEmpty();
        }

        /**
         * @param value {@link #procedure} (The procedure code.)
         */
        public ProcedureComponent setProcedure(Type value) { 
          this.procedure = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "Sequence of procedures which serves to order and provide a link.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("date", "dateTime", "Date and optionally time the procedure was performed .", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("procedure[x]", "CodeableConcept|Reference(Procedure)", "The procedure code.", 0, java.lang.Integer.MAX_VALUE, procedure));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : new Base[] {this.procedure}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case -1095204141: // procedure
          this.procedure = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("procedure[x]")) {
          this.procedure = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 3076014:  return getDateElement();
        case 1640074445:  return getProcedure(); 
        case -1095204141:  return getProcedure(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case -1095204141: /*procedure*/ return new String[] {"CodeableConcept", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.date");
        }
        else if (name.equals("procedureCodeableConcept")) {
          this.procedure = new CodeableConcept();
          return this.procedure;
        }
        else if (name.equals("procedureReference")) {
          this.procedure = new Reference();
          return this.procedure;
        }
        else
          return super.addChild(name);
      }

      public ProcedureComponent copy() {
        ProcedureComponent dst = new ProcedureComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.date = date == null ? null : date.copy();
        dst.procedure = procedure == null ? null : procedure.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedureComponent))
          return false;
        ProcedureComponent o = (ProcedureComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(date, o.date, true) && compareDeep(procedure, o.procedure, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedureComponent))
          return false;
        ProcedureComponent o = (ProcedureComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, date, procedure
          );
      }

  public String fhirType() {
    return "ExplanationOfBenefit.procedure";

  }

  }

    @Block()
    public static class InsuranceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reference to the program or plan identification, underwriter or payor.
         */
        @Child(name = "coverage", type = {Coverage.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the program or plan identification, underwriter or payor." )
        protected Reference coverage;

        /**
         * The actual object that is the target of the reference (Reference to the program or plan identification, underwriter or payor.)
         */
        protected Coverage coverageTarget;

        /**
         * A list of references from the Insurer to which these services pertain.
         */
        @Child(name = "preAuthRef", type = {StringType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pre-Authorization/Determination Reference", formalDefinition="A list of references from the Insurer to which these services pertain." )
        protected List<StringType> preAuthRef;

        private static final long serialVersionUID = -870298727L;

    /**
     * Constructor
     */
      public InsuranceComponent() {
        super();
      }

        /**
         * @return {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
         */
        public Reference getCoverage() { 
          if (this.coverage == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.coverage");
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
        public InsuranceComponent setCoverage(Reference value) { 
          this.coverage = value;
          return this;
        }

        /**
         * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
         */
        public Coverage getCoverageTarget() { 
          if (this.coverageTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.coverage");
            else if (Configuration.doAutoCreate())
              this.coverageTarget = new Coverage(); // aa
          return this.coverageTarget;
        }

        /**
         * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
         */
        public InsuranceComponent setCoverageTarget(Coverage value) { 
          this.coverageTarget = value;
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public InsuranceComponent setPreAuthRef(List<StringType> thePreAuthRef) { 
          this.preAuthRef = thePreAuthRef;
          return this;
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
        public InsuranceComponent addPreAuthRef(String value) { //1
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
          childrenList.add(new Property("preAuthRef", "string", "A list of references from the Insurer to which these services pertain.", 0, java.lang.Integer.MAX_VALUE, preAuthRef));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Reference
        case 522246568: /*preAuthRef*/ return this.preAuthRef == null ? new Base[0] : this.preAuthRef.toArray(new Base[this.preAuthRef.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -351767064: // coverage
          this.coverage = castToReference(value); // Reference
          return value;
        case 522246568: // preAuthRef
          this.getPreAuthRef().add(castToString(value)); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("coverage")) {
          this.coverage = castToReference(value); // Reference
        } else if (name.equals("preAuthRef")) {
          this.getPreAuthRef().add(castToString(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -351767064:  return getCoverage(); 
        case 522246568:  return addPreAuthRefElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -351767064: /*coverage*/ return new String[] {"Reference"};
        case 522246568: /*preAuthRef*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("preAuthRef")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.preAuthRef");
        }
        else
          return super.addChild(name);
      }

      public InsuranceComponent copy() {
        InsuranceComponent dst = new InsuranceComponent();
        copyValues(dst);
        dst.coverage = coverage == null ? null : coverage.copy();
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
        if (!(other instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other;
        return compareDeep(coverage, o.coverage, true) && compareDeep(preAuthRef, o.preAuthRef, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other;
        return compareValues(preAuthRef, o.preAuthRef, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(coverage, preAuthRef);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.insurance";

  }

  }

    @Block()
    public static class AccidentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Date of an accident which these services are addressing.
         */
        @Child(name = "date", type = {DateType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When the accident occurred", formalDefinition="Date of an accident which these services are addressing." )
        protected DateType date;

        /**
         * Type of accident: work, auto, etc.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The nature of the accident", formalDefinition="Type of accident: work, auto, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ActIncidentCode")
        protected CodeableConcept type;

        /**
         * Where the accident occurred.
         */
        @Child(name = "location", type = {Address.class, Location.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Accident Place", formalDefinition="Where the accident occurred." )
        protected Type location;

        private static final long serialVersionUID = 622904984L;

    /**
     * Constructor
     */
      public AccidentComponent() {
        super();
      }

        /**
         * @return {@link #date} (Date of an accident which these services are addressing.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AccidentComponent.date");
            else if (Configuration.doAutoCreate())
              this.date = new DateType(); // bb
          return this.date;
        }

        public boolean hasDateElement() { 
          return this.date != null && !this.date.isEmpty();
        }

        public boolean hasDate() { 
          return this.date != null && !this.date.isEmpty();
        }

        /**
         * @param value {@link #date} (Date of an accident which these services are addressing.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public AccidentComponent setDateElement(DateType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Date of an accident which these services are addressing.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Date of an accident which these services are addressing.
         */
        public AccidentComponent setDate(Date value) { 
          if (value == null)
            this.date = null;
          else {
            if (this.date == null)
              this.date = new DateType();
            this.date.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (Type of accident: work, auto, etc.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AccidentComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of accident: work, auto, etc.)
         */
        public AccidentComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #location} (Where the accident occurred.)
         */
        public Type getLocation() { 
          return this.location;
        }

        /**
         * @return {@link #location} (Where the accident occurred.)
         */
        public Address getLocationAddress() throws FHIRException { 
          if (!(this.location instanceof Address))
            throw new FHIRException("Type mismatch: the type Address was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Address) this.location;
        }

        public boolean hasLocationAddress() { 
          return this.location instanceof Address;
        }

        /**
         * @return {@link #location} (Where the accident occurred.)
         */
        public Reference getLocationReference() throws FHIRException { 
          if (!(this.location instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Reference) this.location;
        }

        public boolean hasLocationReference() { 
          return this.location instanceof Reference;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (Where the accident occurred.)
         */
        public AccidentComponent setLocation(Type value) { 
          this.location = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("date", "date", "Date of an accident which these services are addressing.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("type", "CodeableConcept", "Type of accident: work, auto, etc.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("location[x]", "Address|Reference(Location)", "Where the accident occurred.", 0, java.lang.Integer.MAX_VALUE, location));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3076014: // date
          this.date = castToDate(value); // DateType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1901043637: // location
          this.location = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("date")) {
          this.date = castToDate(value); // DateType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("location[x]")) {
          this.location = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3076014:  return getDateElement();
        case 3575610:  return getType(); 
        case 552316075:  return getLocation(); 
        case 1901043637:  return getLocation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3076014: /*date*/ return new String[] {"date"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1901043637: /*location*/ return new String[] {"Address", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.date");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("locationAddress")) {
          this.location = new Address();
          return this.location;
        }
        else if (name.equals("locationReference")) {
          this.location = new Reference();
          return this.location;
        }
        else
          return super.addChild(name);
      }

      public AccidentComponent copy() {
        AccidentComponent dst = new AccidentComponent();
        copyValues(dst);
        dst.date = date == null ? null : date.copy();
        dst.type = type == null ? null : type.copy();
        dst.location = location == null ? null : location.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AccidentComponent))
          return false;
        AccidentComponent o = (AccidentComponent) other;
        return compareDeep(date, o.date, true) && compareDeep(type, o.type, true) && compareDeep(location, o.location, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AccidentComponent))
          return false;
        AccidentComponent o = (AccidentComponent) other;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(date, type, location);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.accident";

  }

  }

    @Block()
    public static class ItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line number.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType sequence;

        /**
         * Careteam applicable for this service or product line.
         */
        @Child(name = "careTeamLinkId", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable careteam members", formalDefinition="Careteam applicable for this service or product line." )
        protected List<PositiveIntType> careTeamLinkId;

        /**
         * Diagnosis applicable for this service or product line.
         */
        @Child(name = "diagnosisLinkId", type = {PositiveIntType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable diagnoses", formalDefinition="Diagnosis applicable for this service or product line." )
        protected List<PositiveIntType> diagnosisLinkId;

        /**
         * Procedures applicable for this service or product line.
         */
        @Child(name = "procedureLinkId", type = {PositiveIntType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable procedures", formalDefinition="Procedures applicable for this service or product line." )
        protected List<PositiveIntType> procedureLinkId;

        /**
         * Exceptions, special conditions and supporting information pplicable for this service or product line.
         */
        @Child(name = "informationLinkId", type = {PositiveIntType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable exception and supporting information", formalDefinition="Exceptions, special conditions and supporting information pplicable for this service or product line." )
        protected List<PositiveIntType> informationLinkId;

        /**
         * The type of reveneu or cost center providing the product and/or service.
         */
        @Child(name = "revenue", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Revenue or cost center code", formalDefinition="The type of reveneu or cost center providing the product and/or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-revenue-center")
        protected CodeableConcept revenue;

        /**
         * Health Care Service Type Codes  to identify the classification of service or benefits.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of service or product", formalDefinition="Health Care Service Type Codes  to identify the classification of service or benefits." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-subcategory")
        protected CodeableConcept category;

        /**
         * If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.
         */
        @Child(name = "service", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing Code", formalDefinition="If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept service;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.
         */
        @Child(name = "programCode", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Program specific reason for item inclusion", formalDefinition="For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-program-code")
        protected List<CodeableConcept> programCode;

        /**
         * The date or dates when the enclosed suite of services were performed or completed.
         */
        @Child(name = "serviced", type = {DateType.class, Period.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Date or dates of Service", formalDefinition="The date or dates when the enclosed suite of services were performed or completed." )
        protected Type serviced;

        /**
         * Where the service was provided.
         */
        @Child(name = "location", type = {CodeableConcept.class, Address.class, Location.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Place of service", formalDefinition="Where the service was provided." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-place")
        protected Type location;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (List of Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        /**
         * Physical service site on the patient (limb, tooth, etc).
         */
        @Child(name = "bodySite", type = {CodeableConcept.class}, order=18, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service Location", formalDefinition="Physical service site on the patient (limb, tooth, etc)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/tooth")
        protected CodeableConcept bodySite;

        /**
         * A region or surface of the site, eg. limb region or tooth surface(s).
         */
        @Child(name = "subSite", type = {CodeableConcept.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service Sub-location", formalDefinition="A region or surface of the site, eg. limb region or tooth surface(s)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/surface")
        protected List<CodeableConcept> subSite;

        /**
         * A billed item may include goods or services provided in multiple encounters.
         */
        @Child(name = "encounter", type = {Encounter.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Encounters related to this billed item", formalDefinition="A billed item may include goods or services provided in multiple encounters." )
        protected List<Reference> encounter;
        /**
         * The actual objects that are the target of the reference (A billed item may include goods or services provided in multiple encounters.)
         */
        protected List<Encounter> encounterTarget;


        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication details", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * Second tier of goods and services.
         */
        @Child(name = "detail", type = {}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additional items", formalDefinition="Second tier of goods and services." )
        protected List<DetailComponent> detail;

        private static final long serialVersionUID = -1567825229L;

    /**
     * Constructor
     */
      public ItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemComponent(PositiveIntType sequence) {
        super();
        this.sequence = sequence;
      }

        /**
         * @return {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.sequence");
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
        public ItemComponent setSequenceElement(PositiveIntType value) { 
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
        public ItemComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #careTeamLinkId} (Careteam applicable for this service or product line.)
         */
        public List<PositiveIntType> getCareTeamLinkId() { 
          if (this.careTeamLinkId == null)
            this.careTeamLinkId = new ArrayList<PositiveIntType>();
          return this.careTeamLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setCareTeamLinkId(List<PositiveIntType> theCareTeamLinkId) { 
          this.careTeamLinkId = theCareTeamLinkId;
          return this;
        }

        public boolean hasCareTeamLinkId() { 
          if (this.careTeamLinkId == null)
            return false;
          for (PositiveIntType item : this.careTeamLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #careTeamLinkId} (Careteam applicable for this service or product line.)
         */
        public PositiveIntType addCareTeamLinkIdElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.careTeamLinkId == null)
            this.careTeamLinkId = new ArrayList<PositiveIntType>();
          this.careTeamLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #careTeamLinkId} (Careteam applicable for this service or product line.)
         */
        public ItemComponent addCareTeamLinkId(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.careTeamLinkId == null)
            this.careTeamLinkId = new ArrayList<PositiveIntType>();
          this.careTeamLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #careTeamLinkId} (Careteam applicable for this service or product line.)
         */
        public boolean hasCareTeamLinkId(int value) { 
          if (this.careTeamLinkId == null)
            return false;
          for (PositiveIntType v : this.careTeamLinkId)
            if (v.equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #diagnosisLinkId} (Diagnosis applicable for this service or product line.)
         */
        public List<PositiveIntType> getDiagnosisLinkId() { 
          if (this.diagnosisLinkId == null)
            this.diagnosisLinkId = new ArrayList<PositiveIntType>();
          return this.diagnosisLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setDiagnosisLinkId(List<PositiveIntType> theDiagnosisLinkId) { 
          this.diagnosisLinkId = theDiagnosisLinkId;
          return this;
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
        public ItemComponent addDiagnosisLinkId(int value) { //1
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
         * @return {@link #procedureLinkId} (Procedures applicable for this service or product line.)
         */
        public List<PositiveIntType> getProcedureLinkId() { 
          if (this.procedureLinkId == null)
            this.procedureLinkId = new ArrayList<PositiveIntType>();
          return this.procedureLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setProcedureLinkId(List<PositiveIntType> theProcedureLinkId) { 
          this.procedureLinkId = theProcedureLinkId;
          return this;
        }

        public boolean hasProcedureLinkId() { 
          if (this.procedureLinkId == null)
            return false;
          for (PositiveIntType item : this.procedureLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #procedureLinkId} (Procedures applicable for this service or product line.)
         */
        public PositiveIntType addProcedureLinkIdElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.procedureLinkId == null)
            this.procedureLinkId = new ArrayList<PositiveIntType>();
          this.procedureLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #procedureLinkId} (Procedures applicable for this service or product line.)
         */
        public ItemComponent addProcedureLinkId(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.procedureLinkId == null)
            this.procedureLinkId = new ArrayList<PositiveIntType>();
          this.procedureLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #procedureLinkId} (Procedures applicable for this service or product line.)
         */
        public boolean hasProcedureLinkId(int value) { 
          if (this.procedureLinkId == null)
            return false;
          for (PositiveIntType v : this.procedureLinkId)
            if (v.equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #informationLinkId} (Exceptions, special conditions and supporting information pplicable for this service or product line.)
         */
        public List<PositiveIntType> getInformationLinkId() { 
          if (this.informationLinkId == null)
            this.informationLinkId = new ArrayList<PositiveIntType>();
          return this.informationLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setInformationLinkId(List<PositiveIntType> theInformationLinkId) { 
          this.informationLinkId = theInformationLinkId;
          return this;
        }

        public boolean hasInformationLinkId() { 
          if (this.informationLinkId == null)
            return false;
          for (PositiveIntType item : this.informationLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #informationLinkId} (Exceptions, special conditions and supporting information pplicable for this service or product line.)
         */
        public PositiveIntType addInformationLinkIdElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.informationLinkId == null)
            this.informationLinkId = new ArrayList<PositiveIntType>();
          this.informationLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #informationLinkId} (Exceptions, special conditions and supporting information pplicable for this service or product line.)
         */
        public ItemComponent addInformationLinkId(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.informationLinkId == null)
            this.informationLinkId = new ArrayList<PositiveIntType>();
          this.informationLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #informationLinkId} (Exceptions, special conditions and supporting information pplicable for this service or product line.)
         */
        public boolean hasInformationLinkId(int value) { 
          if (this.informationLinkId == null)
            return false;
          for (PositiveIntType v : this.informationLinkId)
            if (v.equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public CodeableConcept getRevenue() { 
          if (this.revenue == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.revenue");
            else if (Configuration.doAutoCreate())
              this.revenue = new CodeableConcept(); // cc
          return this.revenue;
        }

        public boolean hasRevenue() { 
          return this.revenue != null && !this.revenue.isEmpty();
        }

        /**
         * @param value {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public ItemComponent setRevenue(CodeableConcept value) { 
          this.revenue = value;
          return this;
        }

        /**
         * @return {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public ItemComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #service} (If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.)
         */
        public CodeableConcept getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new CodeableConcept(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.)
         */
        public ItemComponent setService(CodeableConcept value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setModifier(List<CodeableConcept> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (CodeableConcept item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addModifier() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return t;
        }

        public ItemComponent addModifier(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public CodeableConcept getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
        public List<CodeableConcept> getProgramCode() { 
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          return this.programCode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setProgramCode(List<CodeableConcept> theProgramCode) { 
          this.programCode = theProgramCode;
          return this;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (CodeableConcept item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addProgramCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          this.programCode.add(t);
          return t;
        }

        public ItemComponent addProgramCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          this.programCode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #programCode}, creating it if it does not already exist
         */
        public CodeableConcept getProgramCodeFirstRep() { 
          if (getProgramCode().isEmpty()) {
            addProgramCode();
          }
          return getProgramCode().get(0);
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
        public ItemComponent setServiced(Type value) { 
          this.serviced = value;
          return this;
        }

        /**
         * @return {@link #location} (Where the service was provided.)
         */
        public Type getLocation() { 
          return this.location;
        }

        /**
         * @return {@link #location} (Where the service was provided.)
         */
        public CodeableConcept getLocationCodeableConcept() throws FHIRException { 
          if (!(this.location instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.location.getClass().getName()+" was encountered");
          return (CodeableConcept) this.location;
        }

        public boolean hasLocationCodeableConcept() { 
          return this.location instanceof CodeableConcept;
        }

        /**
         * @return {@link #location} (Where the service was provided.)
         */
        public Address getLocationAddress() throws FHIRException { 
          if (!(this.location instanceof Address))
            throw new FHIRException("Type mismatch: the type Address was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Address) this.location;
        }

        public boolean hasLocationAddress() { 
          return this.location instanceof Address;
        }

        /**
         * @return {@link #location} (Where the service was provided.)
         */
        public Reference getLocationReference() throws FHIRException { 
          if (!(this.location instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Reference) this.location;
        }

        public boolean hasLocationReference() { 
          return this.location instanceof Reference;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (Where the service was provided.)
         */
        public ItemComponent setLocation(Type value) { 
          this.location = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.quantity");
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
        public ItemComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.unitPrice");
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
        public ItemComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.factor");
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
        public ItemComponent setFactorElement(DecimalType value) { 
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
        public ItemComponent setFactor(BigDecimal value) { 
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
        public ItemComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ItemComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.net");
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
        public ItemComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public List<Reference> getUdi() { 
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          return this.udi;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setUdi(List<Reference> theUdi) { 
          this.udi = theUdi;
          return this;
        }

        public boolean hasUdi() { 
          if (this.udi == null)
            return false;
          for (Reference item : this.udi)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addUdi() { //3
          Reference t = new Reference();
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return t;
        }

        public ItemComponent addUdi(Reference t) { //3
          if (t == null)
            return this;
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #udi}, creating it if it does not already exist
         */
        public Reference getUdiFirstRep() { 
          if (getUdi().isEmpty()) {
            addUdi();
          }
          return getUdi().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Device> getUdiTarget() { 
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          return this.udiTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Device addUdiTarget() { 
          Device r = new Device();
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          this.udiTarget.add(r);
          return r;
        }

        /**
         * @return {@link #bodySite} (Physical service site on the patient (limb, tooth, etc).)
         */
        public CodeableConcept getBodySite() { 
          if (this.bodySite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.bodySite");
            else if (Configuration.doAutoCreate())
              this.bodySite = new CodeableConcept(); // cc
          return this.bodySite;
        }

        public boolean hasBodySite() { 
          return this.bodySite != null && !this.bodySite.isEmpty();
        }

        /**
         * @param value {@link #bodySite} (Physical service site on the patient (limb, tooth, etc).)
         */
        public ItemComponent setBodySite(CodeableConcept value) { 
          this.bodySite = value;
          return this;
        }

        /**
         * @return {@link #subSite} (A region or surface of the site, eg. limb region or tooth surface(s).)
         */
        public List<CodeableConcept> getSubSite() { 
          if (this.subSite == null)
            this.subSite = new ArrayList<CodeableConcept>();
          return this.subSite;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setSubSite(List<CodeableConcept> theSubSite) { 
          this.subSite = theSubSite;
          return this;
        }

        public boolean hasSubSite() { 
          if (this.subSite == null)
            return false;
          for (CodeableConcept item : this.subSite)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addSubSite() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.subSite == null)
            this.subSite = new ArrayList<CodeableConcept>();
          this.subSite.add(t);
          return t;
        }

        public ItemComponent addSubSite(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.subSite == null)
            this.subSite = new ArrayList<CodeableConcept>();
          this.subSite.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #subSite}, creating it if it does not already exist
         */
        public CodeableConcept getSubSiteFirstRep() { 
          if (getSubSite().isEmpty()) {
            addSubSite();
          }
          return getSubSite().get(0);
        }

        /**
         * @return {@link #encounter} (A billed item may include goods or services provided in multiple encounters.)
         */
        public List<Reference> getEncounter() { 
          if (this.encounter == null)
            this.encounter = new ArrayList<Reference>();
          return this.encounter;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setEncounter(List<Reference> theEncounter) { 
          this.encounter = theEncounter;
          return this;
        }

        public boolean hasEncounter() { 
          if (this.encounter == null)
            return false;
          for (Reference item : this.encounter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addEncounter() { //3
          Reference t = new Reference();
          if (this.encounter == null)
            this.encounter = new ArrayList<Reference>();
          this.encounter.add(t);
          return t;
        }

        public ItemComponent addEncounter(Reference t) { //3
          if (t == null)
            return this;
          if (this.encounter == null)
            this.encounter = new ArrayList<Reference>();
          this.encounter.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #encounter}, creating it if it does not already exist
         */
        public Reference getEncounterFirstRep() { 
          if (getEncounter().isEmpty()) {
            addEncounter();
          }
          return getEncounter().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Encounter> getEncounterTarget() { 
          if (this.encounterTarget == null)
            this.encounterTarget = new ArrayList<Encounter>();
          return this.encounterTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Encounter addEncounterTarget() { 
          Encounter r = new Encounter();
          if (this.encounterTarget == null)
            this.encounterTarget = new ArrayList<Encounter>();
          this.encounterTarget.add(r);
          return r;
        }

        /**
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
          this.noteNumber = theNoteNumber;
          return this;
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
        public ItemComponent addNoteNumber(int value) { //1
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
        public List<AdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          return this.adjudication;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setAdjudication(List<AdjudicationComponent> theAdjudication) { 
          this.adjudication = theAdjudication;
          return this;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (AdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AdjudicationComponent addAdjudication() { //3
          AdjudicationComponent t = new AdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

        public ItemComponent addAdjudication(AdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #adjudication}, creating it if it does not already exist
         */
        public AdjudicationComponent getAdjudicationFirstRep() { 
          if (getAdjudication().isEmpty()) {
            addAdjudication();
          }
          return getAdjudication().get(0);
        }

        /**
         * @return {@link #detail} (Second tier of goods and services.)
         */
        public List<DetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<DetailComponent>();
          return this.detail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setDetail(List<DetailComponent> theDetail) { 
          this.detail = theDetail;
          return this;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (DetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public DetailComponent addDetail() { //3
          DetailComponent t = new DetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<DetailComponent>();
          this.detail.add(t);
          return t;
        }

        public ItemComponent addDetail(DetailComponent t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<DetailComponent>();
          this.detail.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #detail}, creating it if it does not already exist
         */
        public DetailComponent getDetailFirstRep() { 
          if (getDetail().isEmpty()) {
            addDetail();
          }
          return getDetail().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("careTeamLinkId", "positiveInt", "Careteam applicable for this service or product line.", 0, java.lang.Integer.MAX_VALUE, careTeamLinkId));
          childrenList.add(new Property("diagnosisLinkId", "positiveInt", "Diagnosis applicable for this service or product line.", 0, java.lang.Integer.MAX_VALUE, diagnosisLinkId));
          childrenList.add(new Property("procedureLinkId", "positiveInt", "Procedures applicable for this service or product line.", 0, java.lang.Integer.MAX_VALUE, procedureLinkId));
          childrenList.add(new Property("informationLinkId", "positiveInt", "Exceptions, special conditions and supporting information pplicable for this service or product line.", 0, java.lang.Integer.MAX_VALUE, informationLinkId));
          childrenList.add(new Property("revenue", "CodeableConcept", "The type of reveneu or cost center providing the product and/or service.", 0, java.lang.Integer.MAX_VALUE, revenue));
          childrenList.add(new Property("category", "CodeableConcept", "Health Care Service Type Codes  to identify the classification of service or benefits.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("service", "CodeableConcept", "If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("programCode", "CodeableConcept", "For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.", 0, java.lang.Integer.MAX_VALUE, programCode));
          childrenList.add(new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, serviced));
          childrenList.add(new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the service was provided.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Reference(Device)", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("bodySite", "CodeableConcept", "Physical service site on the patient (limb, tooth, etc).", 0, java.lang.Integer.MAX_VALUE, bodySite));
          childrenList.add(new Property("subSite", "CodeableConcept", "A region or surface of the site, eg. limb region or tooth surface(s).", 0, java.lang.Integer.MAX_VALUE, subSite));
          childrenList.add(new Property("encounter", "Reference(Encounter)", "A billed item may include goods or services provided in multiple encounters.", 0, java.lang.Integer.MAX_VALUE, encounter));
          childrenList.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("detail", "", "Second tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case -186757789: /*careTeamLinkId*/ return this.careTeamLinkId == null ? new Base[0] : this.careTeamLinkId.toArray(new Base[this.careTeamLinkId.size()]); // PositiveIntType
        case -1659207418: /*diagnosisLinkId*/ return this.diagnosisLinkId == null ? new Base[0] : this.diagnosisLinkId.toArray(new Base[this.diagnosisLinkId.size()]); // PositiveIntType
        case -532846744: /*procedureLinkId*/ return this.procedureLinkId == null ? new Base[0] : this.procedureLinkId.toArray(new Base[this.procedureLinkId.size()]); // PositiveIntType
        case 1965585153: /*informationLinkId*/ return this.informationLinkId == null ? new Base[0] : this.informationLinkId.toArray(new Base[this.informationLinkId.size()]); // PositiveIntType
        case 1099842588: /*revenue*/ return this.revenue == null ? new Base[0] : new Base[] {this.revenue}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // CodeableConcept
        case 1379209295: /*serviced*/ return this.serviced == null ? new Base[0] : new Base[] {this.serviced}; // Type
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Type
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // CodeableConcept
        case -1868566105: /*subSite*/ return this.subSite == null ? new Base[0] : this.subSite.toArray(new Base[this.subSite.size()]); // CodeableConcept
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : this.encounter.toArray(new Base[this.encounter.size()]); // Reference
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // DetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case -186757789: // careTeamLinkId
          this.getCareTeamLinkId().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -1659207418: // diagnosisLinkId
          this.getDiagnosisLinkId().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -532846744: // procedureLinkId
          this.getProcedureLinkId().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case 1965585153: // informationLinkId
          this.getInformationLinkId().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case 1099842588: // revenue
          this.revenue = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1984153269: // service
          this.service = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1010065041: // programCode
          this.getProgramCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1379209295: // serviced
          this.serviced = castToType(value); // Type
          return value;
        case 1901043637: // location
          this.location = castToType(value); // Type
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case 108957: // net
          this.net = castToMoney(value); // Money
          return value;
        case 115642: // udi
          this.getUdi().add(castToReference(value)); // Reference
          return value;
        case 1702620169: // bodySite
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868566105: // subSite
          this.getSubSite().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1524132147: // encounter
          this.getEncounter().add(castToReference(value)); // Reference
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        case -1335224239: // detail
          this.getDetail().add((DetailComponent) value); // DetailComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("careTeamLinkId")) {
          this.getCareTeamLinkId().add(castToPositiveInt(value));
        } else if (name.equals("diagnosisLinkId")) {
          this.getDiagnosisLinkId().add(castToPositiveInt(value));
        } else if (name.equals("procedureLinkId")) {
          this.getProcedureLinkId().add(castToPositiveInt(value));
        } else if (name.equals("informationLinkId")) {
          this.getInformationLinkId().add(castToPositiveInt(value));
        } else if (name.equals("revenue")) {
          this.revenue = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("service")) {
          this.service = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("programCode")) {
          this.getProgramCode().add(castToCodeableConcept(value));
        } else if (name.equals("serviced[x]")) {
          this.serviced = castToType(value); // Type
        } else if (name.equals("location[x]")) {
          this.location = castToType(value); // Type
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else if (name.equals("udi")) {
          this.getUdi().add(castToReference(value));
        } else if (name.equals("bodySite")) {
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subSite")) {
          this.getSubSite().add(castToCodeableConcept(value));
        } else if (name.equals("encounter")) {
          this.getEncounter().add(castToReference(value));
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else if (name.equals("detail")) {
          this.getDetail().add((DetailComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case -186757789:  return addCareTeamLinkIdElement();
        case -1659207418:  return addDiagnosisLinkIdElement();
        case -532846744:  return addProcedureLinkIdElement();
        case 1965585153:  return addInformationLinkIdElement();
        case 1099842588:  return getRevenue(); 
        case 50511102:  return getCategory(); 
        case 1984153269:  return getService(); 
        case -615513385:  return addModifier(); 
        case 1010065041:  return addProgramCode(); 
        case -1927922223:  return getServiced(); 
        case 1379209295:  return getServiced(); 
        case 552316075:  return getLocation(); 
        case 1901043637:  return getLocation(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case 108957:  return getNet(); 
        case 115642:  return addUdi(); 
        case 1702620169:  return getBodySite(); 
        case -1868566105:  return addSubSite(); 
        case 1524132147:  return addEncounter(); 
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        case -1335224239:  return addDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case -186757789: /*careTeamLinkId*/ return new String[] {"positiveInt"};
        case -1659207418: /*diagnosisLinkId*/ return new String[] {"positiveInt"};
        case -532846744: /*procedureLinkId*/ return new String[] {"positiveInt"};
        case 1965585153: /*informationLinkId*/ return new String[] {"positiveInt"};
        case 1099842588: /*revenue*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1984153269: /*service*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 1010065041: /*programCode*/ return new String[] {"CodeableConcept"};
        case 1379209295: /*serviced*/ return new String[] {"date", "Period"};
        case 1901043637: /*location*/ return new String[] {"CodeableConcept", "Address", "Reference"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        case 115642: /*udi*/ return new String[] {"Reference"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case -1868566105: /*subSite*/ return new String[] {"CodeableConcept"};
        case 1524132147: /*encounter*/ return new String[] {"Reference"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {};
        case -1335224239: /*detail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("careTeamLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.careTeamLinkId");
        }
        else if (name.equals("diagnosisLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.diagnosisLinkId");
        }
        else if (name.equals("procedureLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.procedureLinkId");
        }
        else if (name.equals("informationLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.informationLinkId");
        }
        else if (name.equals("revenue")) {
          this.revenue = new CodeableConcept();
          return this.revenue;
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("service")) {
          this.service = new CodeableConcept();
          return this.service;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("programCode")) {
          return addProgramCode();
        }
        else if (name.equals("servicedDate")) {
          this.serviced = new DateType();
          return this.serviced;
        }
        else if (name.equals("servicedPeriod")) {
          this.serviced = new Period();
          return this.serviced;
        }
        else if (name.equals("locationCodeableConcept")) {
          this.location = new CodeableConcept();
          return this.location;
        }
        else if (name.equals("locationAddress")) {
          this.location = new Address();
          return this.location;
        }
        else if (name.equals("locationReference")) {
          this.location = new Reference();
          return this.location;
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
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          return addUdi();
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new CodeableConcept();
          return this.bodySite;
        }
        else if (name.equals("subSite")) {
          return addSubSite();
        }
        else if (name.equals("encounter")) {
          return addEncounter();
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
        else
          return super.addChild(name);
      }

      public ItemComponent copy() {
        ItemComponent dst = new ItemComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        if (careTeamLinkId != null) {
          dst.careTeamLinkId = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : careTeamLinkId)
            dst.careTeamLinkId.add(i.copy());
        };
        if (diagnosisLinkId != null) {
          dst.diagnosisLinkId = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : diagnosisLinkId)
            dst.diagnosisLinkId.add(i.copy());
        };
        if (procedureLinkId != null) {
          dst.procedureLinkId = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : procedureLinkId)
            dst.procedureLinkId.add(i.copy());
        };
        if (informationLinkId != null) {
          dst.informationLinkId = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : informationLinkId)
            dst.informationLinkId.add(i.copy());
        };
        dst.revenue = revenue == null ? null : revenue.copy();
        dst.category = category == null ? null : category.copy();
        dst.service = service == null ? null : service.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        if (programCode != null) {
          dst.programCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : programCode)
            dst.programCode.add(i.copy());
        };
        dst.serviced = serviced == null ? null : serviced.copy();
        dst.location = location == null ? null : location.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.net = net == null ? null : net.copy();
        if (udi != null) {
          dst.udi = new ArrayList<Reference>();
          for (Reference i : udi)
            dst.udi.add(i.copy());
        };
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        if (subSite != null) {
          dst.subSite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subSite)
            dst.subSite.add(i.copy());
        };
        if (encounter != null) {
          dst.encounter = new ArrayList<Reference>();
          for (Reference i : encounter)
            dst.encounter.add(i.copy());
        };
        if (noteNumber != null) {
          dst.noteNumber = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : noteNumber)
            dst.noteNumber.add(i.copy());
        };
        if (adjudication != null) {
          dst.adjudication = new ArrayList<AdjudicationComponent>();
          for (AdjudicationComponent i : adjudication)
            dst.adjudication.add(i.copy());
        };
        if (detail != null) {
          dst.detail = new ArrayList<DetailComponent>();
          for (DetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ItemComponent))
          return false;
        ItemComponent o = (ItemComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(careTeamLinkId, o.careTeamLinkId, true)
           && compareDeep(diagnosisLinkId, o.diagnosisLinkId, true) && compareDeep(procedureLinkId, o.procedureLinkId, true)
           && compareDeep(informationLinkId, o.informationLinkId, true) && compareDeep(revenue, o.revenue, true)
           && compareDeep(category, o.category, true) && compareDeep(service, o.service, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(serviced, o.serviced, true) && compareDeep(location, o.location, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true) && compareDeep(bodySite, o.bodySite, true)
           && compareDeep(subSite, o.subSite, true) && compareDeep(encounter, o.encounter, true) && compareDeep(noteNumber, o.noteNumber, true)
           && compareDeep(adjudication, o.adjudication, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ItemComponent))
          return false;
        ItemComponent o = (ItemComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(careTeamLinkId, o.careTeamLinkId, true)
           && compareValues(diagnosisLinkId, o.diagnosisLinkId, true) && compareValues(procedureLinkId, o.procedureLinkId, true)
           && compareValues(informationLinkId, o.informationLinkId, true) && compareValues(factor, o.factor, true)
           && compareValues(noteNumber, o.noteNumber, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, careTeamLinkId, diagnosisLinkId
          , procedureLinkId, informationLinkId, revenue, category, service, modifier, programCode
          , serviced, location, quantity, unitPrice, factor, net, udi, bodySite, subSite
          , encounter, noteNumber, adjudication, detail);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item";

  }

  }

    @Block()
    public static class AdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication")
        protected CodeableConcept category;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Explanation of Adjudication outcome", formalDefinition="Adjudication reason such as limit reached." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication-reason")
        protected CodeableConcept reason;

        /**
         * Monitory amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monitory amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Non-monitory value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = 1559898786L;

    /**
     * Constructor
     */
      public AdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AdjudicationComponent(CodeableConcept category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdjudicationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public AdjudicationComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public CodeableConcept getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdjudicationComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new CodeableConcept(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Adjudication reason such as limit reached.)
         */
        public AdjudicationComponent setReason(CodeableConcept value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monitory amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdjudicationComponent.amount");
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
        public AdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdjudicationComponent.value");
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
        public AdjudicationComponent setValueElement(DecimalType value) { 
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
        public AdjudicationComponent setValue(BigDecimal value) { 
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
        public AdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "CodeableConcept", "Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("reason", "CodeableConcept", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monitory amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
          childrenList.add(new Property("value", "decimal", "A non-monetary value for example a percentage. Mutually exclusive to the amount element above.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // DecimalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -934964668: // reason
          this.reason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          return value;
        case 111972721: // value
          this.value = castToDecimal(value); // DecimalType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("reason")) {
          this.reason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount")) {
          this.amount = castToMoney(value); // Money
        } else if (name.equals("value")) {
          this.value = castToDecimal(value); // DecimalType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case -934964668:  return getReason(); 
        case -1413853096:  return getAmount(); 
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case -934964668: /*reason*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"Money"};
        case 111972721: /*value*/ return new String[] {"decimal"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("reason")) {
          this.reason = new CodeableConcept();
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

      public AdjudicationComponent copy() {
        AdjudicationComponent dst = new AdjudicationComponent();
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
        if (!(other instanceof AdjudicationComponent))
          return false;
        AdjudicationComponent o = (AdjudicationComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AdjudicationComponent))
          return false;
        AdjudicationComponent o = (AdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, reason, amount
          , value);
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
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType sequence;

        /**
         * The type of product or service.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Group or type of product or service", formalDefinition="The type of product or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ActInvoiceGroupCode")
        protected CodeableConcept type;

        /**
         * The type of reveneu or cost center providing the product and/or service.
         */
        @Child(name = "revenue", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Revenue or cost center code", formalDefinition="The type of reveneu or cost center providing the product and/or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-revenue-center")
        protected CodeableConcept revenue;

        /**
         * Health Care Service Type Codes  to identify the classification of service or benefits.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of service or product", formalDefinition="Health Care Service Type Codes  to identify the classification of service or benefits." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-subcategory")
        protected CodeableConcept category;

        /**
         * If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.
         */
        @Child(name = "service", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing Code", formalDefinition="If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept service;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.
         */
        @Child(name = "programCode", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Program specific reason for item inclusion", formalDefinition="For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-program-code")
        protected List<CodeableConcept> programCode;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total additional item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (List of Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Detail level adjudication details", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * Third tier of goods and services.
         */
        @Child(name = "subDetail", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additional items", formalDefinition="Third tier of goods and services." )
        protected List<SubDetailComponent> subDetail;

        private static final long serialVersionUID = -276371489L;

    /**
     * Constructor
     */
      public DetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DetailComponent(PositiveIntType sequence, CodeableConcept type) {
        super();
        this.sequence = sequence;
        this.type = type;
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
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of product or service.)
         */
        public DetailComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public CodeableConcept getRevenue() { 
          if (this.revenue == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.revenue");
            else if (Configuration.doAutoCreate())
              this.revenue = new CodeableConcept(); // cc
          return this.revenue;
        }

        public boolean hasRevenue() { 
          return this.revenue != null && !this.revenue.isEmpty();
        }

        /**
         * @param value {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public DetailComponent setRevenue(CodeableConcept value) { 
          this.revenue = value;
          return this;
        }

        /**
         * @return {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public DetailComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #service} (If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.)
         */
        public CodeableConcept getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new CodeableConcept(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.)
         */
        public DetailComponent setService(CodeableConcept value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailComponent setModifier(List<CodeableConcept> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (CodeableConcept item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addModifier() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return t;
        }

        public DetailComponent addModifier(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public CodeableConcept getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
        public List<CodeableConcept> getProgramCode() { 
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          return this.programCode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailComponent setProgramCode(List<CodeableConcept> theProgramCode) { 
          this.programCode = theProgramCode;
          return this;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (CodeableConcept item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addProgramCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          this.programCode.add(t);
          return t;
        }

        public DetailComponent addProgramCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          this.programCode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #programCode}, creating it if it does not already exist
         */
        public CodeableConcept getProgramCodeFirstRep() { 
          if (getProgramCode().isEmpty()) {
            addProgramCode();
          }
          return getProgramCode().get(0);
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
        public List<Reference> getUdi() { 
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          return this.udi;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailComponent setUdi(List<Reference> theUdi) { 
          this.udi = theUdi;
          return this;
        }

        public boolean hasUdi() { 
          if (this.udi == null)
            return false;
          for (Reference item : this.udi)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addUdi() { //3
          Reference t = new Reference();
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return t;
        }

        public DetailComponent addUdi(Reference t) { //3
          if (t == null)
            return this;
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #udi}, creating it if it does not already exist
         */
        public Reference getUdiFirstRep() { 
          if (getUdi().isEmpty()) {
            addUdi();
          }
          return getUdi().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Device> getUdiTarget() { 
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          return this.udiTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Device addUdiTarget() { 
          Device r = new Device();
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          this.udiTarget.add(r);
          return r;
        }

        /**
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
          this.noteNumber = theNoteNumber;
          return this;
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
        public DetailComponent addNoteNumber(int value) { //1
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
        public List<AdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          return this.adjudication;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailComponent setAdjudication(List<AdjudicationComponent> theAdjudication) { 
          this.adjudication = theAdjudication;
          return this;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (AdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AdjudicationComponent addAdjudication() { //3
          AdjudicationComponent t = new AdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

        public DetailComponent addAdjudication(AdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #adjudication}, creating it if it does not already exist
         */
        public AdjudicationComponent getAdjudicationFirstRep() { 
          if (getAdjudication().isEmpty()) {
            addAdjudication();
          }
          return getAdjudication().get(0);
        }

        /**
         * @return {@link #subDetail} (Third tier of goods and services.)
         */
        public List<SubDetailComponent> getSubDetail() { 
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          return this.subDetail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailComponent setSubDetail(List<SubDetailComponent> theSubDetail) { 
          this.subDetail = theSubDetail;
          return this;
        }

        public boolean hasSubDetail() { 
          if (this.subDetail == null)
            return false;
          for (SubDetailComponent item : this.subDetail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubDetailComponent addSubDetail() { //3
          SubDetailComponent t = new SubDetailComponent();
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          this.subDetail.add(t);
          return t;
        }

        public DetailComponent addSubDetail(SubDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          this.subDetail.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #subDetail}, creating it if it does not already exist
         */
        public SubDetailComponent getSubDetailFirstRep() { 
          if (getSubDetail().isEmpty()) {
            addSubDetail();
          }
          return getSubDetail().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "CodeableConcept", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("revenue", "CodeableConcept", "The type of reveneu or cost center providing the product and/or service.", 0, java.lang.Integer.MAX_VALUE, revenue));
          childrenList.add(new Property("category", "CodeableConcept", "Health Care Service Type Codes  to identify the classification of service or benefits.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("service", "CodeableConcept", "If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("programCode", "CodeableConcept", "For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.", 0, java.lang.Integer.MAX_VALUE, programCode));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Reference(Device)", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          childrenList.add(new Property("adjudication", "@ExplanationOfBenefit.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("subDetail", "", "Third tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, subDetail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1099842588: /*revenue*/ return this.revenue == null ? new Base[0] : new Base[] {this.revenue}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        case -828829007: /*subDetail*/ return this.subDetail == null ? new Base[0] : this.subDetail.toArray(new Base[this.subDetail.size()]); // SubDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1099842588: // revenue
          this.revenue = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1984153269: // service
          this.service = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1010065041: // programCode
          this.getProgramCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case 108957: // net
          this.net = castToMoney(value); // Money
          return value;
        case 115642: // udi
          this.getUdi().add(castToReference(value)); // Reference
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        case -828829007: // subDetail
          this.getSubDetail().add((SubDetailComponent) value); // SubDetailComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("revenue")) {
          this.revenue = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("service")) {
          this.service = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("programCode")) {
          this.getProgramCode().add(castToCodeableConcept(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else if (name.equals("udi")) {
          this.getUdi().add(castToReference(value));
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else if (name.equals("subDetail")) {
          this.getSubDetail().add((SubDetailComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 3575610:  return getType(); 
        case 1099842588:  return getRevenue(); 
        case 50511102:  return getCategory(); 
        case 1984153269:  return getService(); 
        case -615513385:  return addModifier(); 
        case 1010065041:  return addProgramCode(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case 108957:  return getNet(); 
        case 115642:  return addUdi(); 
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        case -828829007:  return addSubDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1099842588: /*revenue*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1984153269: /*service*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 1010065041: /*programCode*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        case 115642: /*udi*/ return new String[] {"Reference"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ExplanationOfBenefit.item.adjudication"};
        case -828829007: /*subDetail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("revenue")) {
          this.revenue = new CodeableConcept();
          return this.revenue;
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("service")) {
          this.service = new CodeableConcept();
          return this.service;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("programCode")) {
          return addProgramCode();
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
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          return addUdi();
        }
        else if (name.equals("noteNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.noteNumber");
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
        dst.revenue = revenue == null ? null : revenue.copy();
        dst.category = category == null ? null : category.copy();
        dst.service = service == null ? null : service.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        if (programCode != null) {
          dst.programCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : programCode)
            dst.programCode.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.net = net == null ? null : net.copy();
        if (udi != null) {
          dst.udi = new ArrayList<Reference>();
          for (Reference i : udi)
            dst.udi.add(i.copy());
        };
        if (noteNumber != null) {
          dst.noteNumber = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : noteNumber)
            dst.noteNumber.add(i.copy());
        };
        if (adjudication != null) {
          dst.adjudication = new ArrayList<AdjudicationComponent>();
          for (AdjudicationComponent i : adjudication)
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
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(revenue, o.revenue, true)
           && compareDeep(category, o.category, true) && compareDeep(service, o.service, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true)
           && compareDeep(noteNumber, o.noteNumber, true) && compareDeep(adjudication, o.adjudication, true)
           && compareDeep(subDetail, o.subDetail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DetailComponent))
          return false;
        DetailComponent o = (DetailComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(factor, o.factor, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, type, revenue
          , category, service, modifier, programCode, quantity, unitPrice, factor, net
          , udi, noteNumber, adjudication, subDetail);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.detail";

  }

  }

    @Block()
    public static class SubDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line number.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType sequence;

        /**
         * The type of product or service.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of product or service", formalDefinition="The type of product or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ActInvoiceGroupCode")
        protected CodeableConcept type;

        /**
         * The type of reveneu or cost center providing the product and/or service.
         */
        @Child(name = "revenue", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Revenue or cost center code", formalDefinition="The type of reveneu or cost center providing the product and/or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-revenue-center")
        protected CodeableConcept revenue;

        /**
         * Health Care Service Type Codes  to identify the classification of service or benefits.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of service or product", formalDefinition="Health Care Service Type Codes  to identify the classification of service or benefits." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-subcategory")
        protected CodeableConcept category;

        /**
         * A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).
         */
        @Child(name = "service", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing Code", formalDefinition="A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept service;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.
         */
        @Child(name = "programCode", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Program specific reason for item inclusion", formalDefinition="For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-program-code")
        protected List<CodeableConcept> programCode;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * The fee for an addittional service or product or charge.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="The fee for an addittional service or product or charge." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Net additional item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (List of Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Language if different from the resource", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        private static final long serialVersionUID = 1621872130L;

    /**
     * Constructor
     */
      public SubDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubDetailComponent(PositiveIntType sequence, CodeableConcept type) {
        super();
        this.sequence = sequence;
        this.type = type;
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
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of product or service.)
         */
        public SubDetailComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public CodeableConcept getRevenue() { 
          if (this.revenue == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.revenue");
            else if (Configuration.doAutoCreate())
              this.revenue = new CodeableConcept(); // cc
          return this.revenue;
        }

        public boolean hasRevenue() { 
          return this.revenue != null && !this.revenue.isEmpty();
        }

        /**
         * @param value {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public SubDetailComponent setRevenue(CodeableConcept value) { 
          this.revenue = value;
          return this;
        }

        /**
         * @return {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public SubDetailComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #service} (A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).)
         */
        public CodeableConcept getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new CodeableConcept(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).)
         */
        public SubDetailComponent setService(CodeableConcept value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubDetailComponent setModifier(List<CodeableConcept> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (CodeableConcept item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addModifier() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return t;
        }

        public SubDetailComponent addModifier(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public CodeableConcept getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
        public List<CodeableConcept> getProgramCode() { 
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          return this.programCode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubDetailComponent setProgramCode(List<CodeableConcept> theProgramCode) { 
          this.programCode = theProgramCode;
          return this;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (CodeableConcept item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addProgramCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          this.programCode.add(t);
          return t;
        }

        public SubDetailComponent addProgramCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          this.programCode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #programCode}, creating it if it does not already exist
         */
        public CodeableConcept getProgramCodeFirstRep() { 
          if (getProgramCode().isEmpty()) {
            addProgramCode();
          }
          return getProgramCode().get(0);
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
        public List<Reference> getUdi() { 
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          return this.udi;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubDetailComponent setUdi(List<Reference> theUdi) { 
          this.udi = theUdi;
          return this;
        }

        public boolean hasUdi() { 
          if (this.udi == null)
            return false;
          for (Reference item : this.udi)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addUdi() { //3
          Reference t = new Reference();
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return t;
        }

        public SubDetailComponent addUdi(Reference t) { //3
          if (t == null)
            return this;
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #udi}, creating it if it does not already exist
         */
        public Reference getUdiFirstRep() { 
          if (getUdi().isEmpty()) {
            addUdi();
          }
          return getUdi().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Device> getUdiTarget() { 
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          return this.udiTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Device addUdiTarget() { 
          Device r = new Device();
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          this.udiTarget.add(r);
          return r;
        }

        /**
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubDetailComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
          this.noteNumber = theNoteNumber;
          return this;
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
        public SubDetailComponent addNoteNumber(int value) { //1
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
        public List<AdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          return this.adjudication;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubDetailComponent setAdjudication(List<AdjudicationComponent> theAdjudication) { 
          this.adjudication = theAdjudication;
          return this;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (AdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AdjudicationComponent addAdjudication() { //3
          AdjudicationComponent t = new AdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

        public SubDetailComponent addAdjudication(AdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #adjudication}, creating it if it does not already exist
         */
        public AdjudicationComponent getAdjudicationFirstRep() { 
          if (getAdjudication().isEmpty()) {
            addAdjudication();
          }
          return getAdjudication().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "CodeableConcept", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("revenue", "CodeableConcept", "The type of reveneu or cost center providing the product and/or service.", 0, java.lang.Integer.MAX_VALUE, revenue));
          childrenList.add(new Property("category", "CodeableConcept", "Health Care Service Type Codes  to identify the classification of service or benefits.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("service", "CodeableConcept", "A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("programCode", "CodeableConcept", "For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.", 0, java.lang.Integer.MAX_VALUE, programCode));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "The fee for an addittional service or product or charge.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Reference(Device)", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          childrenList.add(new Property("adjudication", "@ExplanationOfBenefit.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1099842588: /*revenue*/ return this.revenue == null ? new Base[0] : new Base[] {this.revenue}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1099842588: // revenue
          this.revenue = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1984153269: // service
          this.service = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1010065041: // programCode
          this.getProgramCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case 108957: // net
          this.net = castToMoney(value); // Money
          return value;
        case 115642: // udi
          this.getUdi().add(castToReference(value)); // Reference
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("revenue")) {
          this.revenue = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("service")) {
          this.service = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("programCode")) {
          this.getProgramCode().add(castToCodeableConcept(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else if (name.equals("udi")) {
          this.getUdi().add(castToReference(value));
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 3575610:  return getType(); 
        case 1099842588:  return getRevenue(); 
        case 50511102:  return getCategory(); 
        case 1984153269:  return getService(); 
        case -615513385:  return addModifier(); 
        case 1010065041:  return addProgramCode(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case 108957:  return getNet(); 
        case 115642:  return addUdi(); 
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1099842588: /*revenue*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1984153269: /*service*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 1010065041: /*programCode*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        case 115642: /*udi*/ return new String[] {"Reference"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ExplanationOfBenefit.item.adjudication"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("revenue")) {
          this.revenue = new CodeableConcept();
          return this.revenue;
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("service")) {
          this.service = new CodeableConcept();
          return this.service;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("programCode")) {
          return addProgramCode();
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
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          return addUdi();
        }
        else if (name.equals("noteNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.noteNumber");
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
        dst.revenue = revenue == null ? null : revenue.copy();
        dst.category = category == null ? null : category.copy();
        dst.service = service == null ? null : service.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        if (programCode != null) {
          dst.programCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : programCode)
            dst.programCode.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.net = net == null ? null : net.copy();
        if (udi != null) {
          dst.udi = new ArrayList<Reference>();
          for (Reference i : udi)
            dst.udi.add(i.copy());
        };
        if (noteNumber != null) {
          dst.noteNumber = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : noteNumber)
            dst.noteNumber.add(i.copy());
        };
        if (adjudication != null) {
          dst.adjudication = new ArrayList<AdjudicationComponent>();
          for (AdjudicationComponent i : adjudication)
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
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(revenue, o.revenue, true)
           && compareDeep(category, o.category, true) && compareDeep(service, o.service, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true)
           && compareDeep(noteNumber, o.noteNumber, true) && compareDeep(adjudication, o.adjudication, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SubDetailComponent))
          return false;
        SubDetailComponent o = (SubDetailComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(factor, o.factor, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, type, revenue
          , category, service, modifier, programCode, quantity, unitPrice, factor, net
          , udi, noteNumber, adjudication);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.detail.subDetail";

  }

  }

    @Block()
    public static class AddedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * List of input service items which this service line is intended to replace.
         */
        @Child(name = "sequenceLinkId", type = {PositiveIntType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service instances", formalDefinition="List of input service items which this service line is intended to replace." )
        protected List<PositiveIntType> sequenceLinkId;

        /**
         * The type of reveneu or cost center providing the product and/or service.
         */
        @Child(name = "revenue", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Revenue or cost center code", formalDefinition="The type of reveneu or cost center providing the product and/or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-revenue-center")
        protected CodeableConcept revenue;

        /**
         * Health Care Service Type Codes  to identify the classification of service or benefits.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of service or product", formalDefinition="Health Care Service Type Codes  to identify the classification of service or benefits." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-subcategory")
        protected CodeableConcept category;

        /**
         * If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.
         */
        @Child(name = "service", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing Code", formalDefinition="If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept service;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * The fee charged for the professional service or product.
         */
        @Child(name = "fee", type = {Money.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Professional fee or Product charge", formalDefinition="The fee charged for the professional service or product." )
        protected Money fee;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Added items adjudication", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * The second tier service adjudications for payor added services.
         */
        @Child(name = "detail", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Added items details", formalDefinition="The second tier service adjudications for payor added services." )
        protected List<AddedItemsDetailComponent> detail;

        private static final long serialVersionUID = 1969703165L;

    /**
     * Constructor
     */
      public AddedItemComponent() {
        super();
      }

        /**
         * @return {@link #sequenceLinkId} (List of input service items which this service line is intended to replace.)
         */
        public List<PositiveIntType> getSequenceLinkId() { 
          if (this.sequenceLinkId == null)
            this.sequenceLinkId = new ArrayList<PositiveIntType>();
          return this.sequenceLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setSequenceLinkId(List<PositiveIntType> theSequenceLinkId) { 
          this.sequenceLinkId = theSequenceLinkId;
          return this;
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
         * @return {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public CodeableConcept getRevenue() { 
          if (this.revenue == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.revenue");
            else if (Configuration.doAutoCreate())
              this.revenue = new CodeableConcept(); // cc
          return this.revenue;
        }

        public boolean hasRevenue() { 
          return this.revenue != null && !this.revenue.isEmpty();
        }

        /**
         * @param value {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public AddedItemComponent setRevenue(CodeableConcept value) { 
          this.revenue = value;
          return this;
        }

        /**
         * @return {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public AddedItemComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #service} (If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.)
         */
        public CodeableConcept getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new CodeableConcept(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.)
         */
        public AddedItemComponent setService(CodeableConcept value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setModifier(List<CodeableConcept> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (CodeableConcept item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addModifier() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return t;
        }

        public AddedItemComponent addModifier(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public CodeableConcept getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #fee} (The fee charged for the professional service or product.)
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
         * @param value {@link #fee} (The fee charged for the professional service or product.)
         */
        public AddedItemComponent setFee(Money value) { 
          this.fee = value;
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
          this.noteNumber = theNoteNumber;
          return this;
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
        public AddedItemComponent addNoteNumber(int value) { //1
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
        public List<AdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          return this.adjudication;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setAdjudication(List<AdjudicationComponent> theAdjudication) { 
          this.adjudication = theAdjudication;
          return this;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (AdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AdjudicationComponent addAdjudication() { //3
          AdjudicationComponent t = new AdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

        public AddedItemComponent addAdjudication(AdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #adjudication}, creating it if it does not already exist
         */
        public AdjudicationComponent getAdjudicationFirstRep() { 
          if (getAdjudication().isEmpty()) {
            addAdjudication();
          }
          return getAdjudication().get(0);
        }

        /**
         * @return {@link #detail} (The second tier service adjudications for payor added services.)
         */
        public List<AddedItemsDetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemsDetailComponent>();
          return this.detail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setDetail(List<AddedItemsDetailComponent> theDetail) { 
          this.detail = theDetail;
          return this;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (AddedItemsDetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AddedItemsDetailComponent addDetail() { //3
          AddedItemsDetailComponent t = new AddedItemsDetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemsDetailComponent>();
          this.detail.add(t);
          return t;
        }

        public AddedItemComponent addDetail(AddedItemsDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemsDetailComponent>();
          this.detail.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #detail}, creating it if it does not already exist
         */
        public AddedItemsDetailComponent getDetailFirstRep() { 
          if (getDetail().isEmpty()) {
            addDetail();
          }
          return getDetail().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequenceLinkId", "positiveInt", "List of input service items which this service line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, sequenceLinkId));
          childrenList.add(new Property("revenue", "CodeableConcept", "The type of reveneu or cost center providing the product and/or service.", 0, java.lang.Integer.MAX_VALUE, revenue));
          childrenList.add(new Property("category", "CodeableConcept", "Health Care Service Type Codes  to identify the classification of service or benefits.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("service", "CodeableConcept", "If this is an actual service or product line, ie. not a Group, then use code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI). If a grouping item then use a group code to indicate the type of thing being grouped eg. 'glasses' or 'compound'.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("fee", "Money", "The fee charged for the professional service or product.", 0, java.lang.Integer.MAX_VALUE, fee));
          childrenList.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          childrenList.add(new Property("adjudication", "@ExplanationOfBenefit.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("detail", "", "The second tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return this.sequenceLinkId == null ? new Base[0] : this.sequenceLinkId.toArray(new Base[this.sequenceLinkId.size()]); // PositiveIntType
        case 1099842588: /*revenue*/ return this.revenue == null ? new Base[0] : new Base[] {this.revenue}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 101254: /*fee*/ return this.fee == null ? new Base[0] : new Base[] {this.fee}; // Money
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // AddedItemsDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422298666: // sequenceLinkId
          this.getSequenceLinkId().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case 1099842588: // revenue
          this.revenue = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1984153269: // service
          this.service = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 101254: // fee
          this.fee = castToMoney(value); // Money
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        case -1335224239: // detail
          this.getDetail().add((AddedItemsDetailComponent) value); // AddedItemsDetailComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          this.getSequenceLinkId().add(castToPositiveInt(value));
        } else if (name.equals("revenue")) {
          this.revenue = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("service")) {
          this.service = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("fee")) {
          this.fee = castToMoney(value); // Money
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else if (name.equals("detail")) {
          this.getDetail().add((AddedItemsDetailComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666:  return addSequenceLinkIdElement();
        case 1099842588:  return getRevenue(); 
        case 50511102:  return getCategory(); 
        case 1984153269:  return getService(); 
        case -615513385:  return addModifier(); 
        case 101254:  return getFee(); 
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        case -1335224239:  return addDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return new String[] {"positiveInt"};
        case 1099842588: /*revenue*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1984153269: /*service*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 101254: /*fee*/ return new String[] {"Money"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ExplanationOfBenefit.item.adjudication"};
        case -1335224239: /*detail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequenceLinkId");
        }
        else if (name.equals("revenue")) {
          this.revenue = new CodeableConcept();
          return this.revenue;
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("service")) {
          this.service = new CodeableConcept();
          return this.service;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("fee")) {
          this.fee = new Money();
          return this.fee;
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
        dst.revenue = revenue == null ? null : revenue.copy();
        dst.category = category == null ? null : category.copy();
        dst.service = service == null ? null : service.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        dst.fee = fee == null ? null : fee.copy();
        if (noteNumber != null) {
          dst.noteNumber = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : noteNumber)
            dst.noteNumber.add(i.copy());
        };
        if (adjudication != null) {
          dst.adjudication = new ArrayList<AdjudicationComponent>();
          for (AdjudicationComponent i : adjudication)
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
        return compareDeep(sequenceLinkId, o.sequenceLinkId, true) && compareDeep(revenue, o.revenue, true)
           && compareDeep(category, o.category, true) && compareDeep(service, o.service, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(fee, o.fee, true) && compareDeep(noteNumber, o.noteNumber, true) && compareDeep(adjudication, o.adjudication, true)
           && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AddedItemComponent))
          return false;
        AddedItemComponent o = (AddedItemComponent) other;
        return compareValues(sequenceLinkId, o.sequenceLinkId, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequenceLinkId, revenue, category
          , service, modifier, fee, noteNumber, adjudication, detail);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.addItem";

  }

  }

    @Block()
    public static class AddedItemsDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of reveneu or cost center providing the product and/or service.
         */
        @Child(name = "revenue", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Revenue or cost center code", formalDefinition="The type of reveneu or cost center providing the product and/or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-revenue-center")
        protected CodeableConcept revenue;

        /**
         * Health Care Service Type Codes  to identify the classification of service or benefits.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of service or product", formalDefinition="Health Care Service Type Codes  to identify the classification of service or benefits." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-subcategory")
        protected CodeableConcept category;

        /**
         * A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).
         */
        @Child(name = "service", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing Code", formalDefinition="A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept service;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * The fee charged for the professional service or product.
         */
        @Child(name = "fee", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Professional fee or Product charge", formalDefinition="The fee charged for the professional service or product." )
        protected Money fee;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Added items detail adjudication", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        private static final long serialVersionUID = -311484980L;

    /**
     * Constructor
     */
      public AddedItemsDetailComponent() {
        super();
      }

        /**
         * @return {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public CodeableConcept getRevenue() { 
          if (this.revenue == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemsDetailComponent.revenue");
            else if (Configuration.doAutoCreate())
              this.revenue = new CodeableConcept(); // cc
          return this.revenue;
        }

        public boolean hasRevenue() { 
          return this.revenue != null && !this.revenue.isEmpty();
        }

        /**
         * @param value {@link #revenue} (The type of reveneu or cost center providing the product and/or service.)
         */
        public AddedItemsDetailComponent setRevenue(CodeableConcept value) { 
          this.revenue = value;
          return this;
        }

        /**
         * @return {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemsDetailComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Health Care Service Type Codes  to identify the classification of service or benefits.)
         */
        public AddedItemsDetailComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #service} (A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).)
         */
        public CodeableConcept getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemsDetailComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new CodeableConcept(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).)
         */
        public AddedItemsDetailComponent setService(CodeableConcept value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemsDetailComponent setModifier(List<CodeableConcept> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (CodeableConcept item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addModifier() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return t;
        }

        public AddedItemsDetailComponent addModifier(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public CodeableConcept getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #fee} (The fee charged for the professional service or product.)
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
         * @param value {@link #fee} (The fee charged for the professional service or product.)
         */
        public AddedItemsDetailComponent setFee(Money value) { 
          this.fee = value;
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemsDetailComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
          this.noteNumber = theNoteNumber;
          return this;
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
        public AddedItemsDetailComponent addNoteNumber(int value) { //1
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
        public List<AdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          return this.adjudication;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemsDetailComponent setAdjudication(List<AdjudicationComponent> theAdjudication) { 
          this.adjudication = theAdjudication;
          return this;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (AdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AdjudicationComponent addAdjudication() { //3
          AdjudicationComponent t = new AdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

        public AddedItemsDetailComponent addAdjudication(AdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #adjudication}, creating it if it does not already exist
         */
        public AdjudicationComponent getAdjudicationFirstRep() { 
          if (getAdjudication().isEmpty()) {
            addAdjudication();
          }
          return getAdjudication().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("revenue", "CodeableConcept", "The type of reveneu or cost center providing the product and/or service.", 0, java.lang.Integer.MAX_VALUE, revenue));
          childrenList.add(new Property("category", "CodeableConcept", "Health Care Service Type Codes  to identify the classification of service or benefits.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("service", "CodeableConcept", "A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("fee", "Money", "The fee charged for the professional service or product.", 0, java.lang.Integer.MAX_VALUE, fee));
          childrenList.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          childrenList.add(new Property("adjudication", "@ExplanationOfBenefit.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1099842588: /*revenue*/ return this.revenue == null ? new Base[0] : new Base[] {this.revenue}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 101254: /*fee*/ return this.fee == null ? new Base[0] : new Base[] {this.fee}; // Money
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1099842588: // revenue
          this.revenue = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1984153269: // service
          this.service = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 101254: // fee
          this.fee = castToMoney(value); // Money
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("revenue")) {
          this.revenue = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("service")) {
          this.service = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("fee")) {
          this.fee = castToMoney(value); // Money
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1099842588:  return getRevenue(); 
        case 50511102:  return getCategory(); 
        case 1984153269:  return getService(); 
        case -615513385:  return addModifier(); 
        case 101254:  return getFee(); 
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1099842588: /*revenue*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1984153269: /*service*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 101254: /*fee*/ return new String[] {"Money"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ExplanationOfBenefit.item.adjudication"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("revenue")) {
          this.revenue = new CodeableConcept();
          return this.revenue;
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("service")) {
          this.service = new CodeableConcept();
          return this.service;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("fee")) {
          this.fee = new Money();
          return this.fee;
        }
        else if (name.equals("noteNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.noteNumber");
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
        dst.revenue = revenue == null ? null : revenue.copy();
        dst.category = category == null ? null : category.copy();
        dst.service = service == null ? null : service.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        dst.fee = fee == null ? null : fee.copy();
        if (noteNumber != null) {
          dst.noteNumber = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : noteNumber)
            dst.noteNumber.add(i.copy());
        };
        if (adjudication != null) {
          dst.adjudication = new ArrayList<AdjudicationComponent>();
          for (AdjudicationComponent i : adjudication)
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
        return compareDeep(revenue, o.revenue, true) && compareDeep(category, o.category, true) && compareDeep(service, o.service, true)
           && compareDeep(modifier, o.modifier, true) && compareDeep(fee, o.fee, true) && compareDeep(noteNumber, o.noteNumber, true)
           && compareDeep(adjudication, o.adjudication, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AddedItemsDetailComponent))
          return false;
        AddedItemsDetailComponent o = (AddedItemsDetailComponent) other;
        return compareValues(noteNumber, o.noteNumber, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(revenue, category, service
          , modifier, fee, noteNumber, adjudication);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.addItem.detail";

  }

  }

    @Block()
    public static class PaymentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Whether this represents partial or complete payment of the claim.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Partial or Complete", formalDefinition="Whether this represents partial or complete payment of the claim." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-paymenttype")
        protected CodeableConcept type;

        /**
         * Adjustment to the payment of this transaction which is not related to adjudication of this transaction.
         */
        @Child(name = "adjustment", type = {Money.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Payment adjustment for non-Claim issues", formalDefinition="Adjustment to the payment of this transaction which is not related to adjudication of this transaction." )
        protected Money adjustment;

        /**
         * Reason for the payment adjustment.
         */
        @Child(name = "adjustmentReason", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Explanation for the non-claim adjustment", formalDefinition="Reason for the payment adjustment." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payment-adjustment-reason")
        protected CodeableConcept adjustmentReason;

        /**
         * Estimated payment date.
         */
        @Child(name = "date", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Expected date of Payment", formalDefinition="Estimated payment date." )
        protected DateType date;

        /**
         * Payable less any payment adjustment.
         */
        @Child(name = "amount", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Payable amount after adjustment", formalDefinition="Payable less any payment adjustment." )
        protected Money amount;

        /**
         * Payment identifer.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifier of the payment instrument", formalDefinition="Payment identifer." )
        protected Identifier identifier;

        private static final long serialVersionUID = 1539906026L;

    /**
     * Constructor
     */
      public PaymentComponent() {
        super();
      }

        /**
         * @return {@link #type} (Whether this represents partial or complete payment of the claim.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Whether this represents partial or complete payment of the claim.)
         */
        public PaymentComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #adjustment} (Adjustment to the payment of this transaction which is not related to adjudication of this transaction.)
         */
        public Money getAdjustment() { 
          if (this.adjustment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.adjustment");
            else if (Configuration.doAutoCreate())
              this.adjustment = new Money(); // cc
          return this.adjustment;
        }

        public boolean hasAdjustment() { 
          return this.adjustment != null && !this.adjustment.isEmpty();
        }

        /**
         * @param value {@link #adjustment} (Adjustment to the payment of this transaction which is not related to adjudication of this transaction.)
         */
        public PaymentComponent setAdjustment(Money value) { 
          this.adjustment = value;
          return this;
        }

        /**
         * @return {@link #adjustmentReason} (Reason for the payment adjustment.)
         */
        public CodeableConcept getAdjustmentReason() { 
          if (this.adjustmentReason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.adjustmentReason");
            else if (Configuration.doAutoCreate())
              this.adjustmentReason = new CodeableConcept(); // cc
          return this.adjustmentReason;
        }

        public boolean hasAdjustmentReason() { 
          return this.adjustmentReason != null && !this.adjustmentReason.isEmpty();
        }

        /**
         * @param value {@link #adjustmentReason} (Reason for the payment adjustment.)
         */
        public PaymentComponent setAdjustmentReason(CodeableConcept value) { 
          this.adjustmentReason = value;
          return this;
        }

        /**
         * @return {@link #date} (Estimated payment date.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.date");
            else if (Configuration.doAutoCreate())
              this.date = new DateType(); // bb
          return this.date;
        }

        public boolean hasDateElement() { 
          return this.date != null && !this.date.isEmpty();
        }

        public boolean hasDate() { 
          return this.date != null && !this.date.isEmpty();
        }

        /**
         * @param value {@link #date} (Estimated payment date.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public PaymentComponent setDateElement(DateType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Estimated payment date.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Estimated payment date.
         */
        public PaymentComponent setDate(Date value) { 
          if (value == null)
            this.date = null;
          else {
            if (this.date == null)
              this.date = new DateType();
            this.date.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #amount} (Payable less any payment adjustment.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Payable less any payment adjustment.)
         */
        public PaymentComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #identifier} (Payment identifer.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Payment identifer.)
         */
        public PaymentComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Whether this represents partial or complete payment of the claim.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("adjustment", "Money", "Adjustment to the payment of this transaction which is not related to adjudication of this transaction.", 0, java.lang.Integer.MAX_VALUE, adjustment));
          childrenList.add(new Property("adjustmentReason", "CodeableConcept", "Reason for the payment adjustment.", 0, java.lang.Integer.MAX_VALUE, adjustmentReason));
          childrenList.add(new Property("date", "date", "Estimated payment date.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("amount", "Money", "Payable less any payment adjustment.", 0, java.lang.Integer.MAX_VALUE, amount));
          childrenList.add(new Property("identifier", "Identifier", "Payment identifer.", 0, java.lang.Integer.MAX_VALUE, identifier));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1977085293: /*adjustment*/ return this.adjustment == null ? new Base[0] : new Base[] {this.adjustment}; // Money
        case -1255938543: /*adjustmentReason*/ return this.adjustmentReason == null ? new Base[0] : new Base[] {this.adjustmentReason}; // CodeableConcept
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1977085293: // adjustment
          this.adjustment = castToMoney(value); // Money
          return value;
        case -1255938543: // adjustmentReason
          this.adjustmentReason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3076014: // date
          this.date = castToDate(value); // DateType
          return value;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("adjustment")) {
          this.adjustment = castToMoney(value); // Money
        } else if (name.equals("adjustmentReason")) {
          this.adjustmentReason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("date")) {
          this.date = castToDate(value); // DateType
        } else if (name.equals("amount")) {
          this.amount = castToMoney(value); // Money
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 1977085293:  return getAdjustment(); 
        case -1255938543:  return getAdjustmentReason(); 
        case 3076014:  return getDateElement();
        case -1413853096:  return getAmount(); 
        case -1618432855:  return getIdentifier(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1977085293: /*adjustment*/ return new String[] {"Money"};
        case -1255938543: /*adjustmentReason*/ return new String[] {"CodeableConcept"};
        case 3076014: /*date*/ return new String[] {"date"};
        case -1413853096: /*amount*/ return new String[] {"Money"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("adjustment")) {
          this.adjustment = new Money();
          return this.adjustment;
        }
        else if (name.equals("adjustmentReason")) {
          this.adjustmentReason = new CodeableConcept();
          return this.adjustmentReason;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.date");
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else
          return super.addChild(name);
      }

      public PaymentComponent copy() {
        PaymentComponent dst = new PaymentComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.adjustment = adjustment == null ? null : adjustment.copy();
        dst.adjustmentReason = adjustmentReason == null ? null : adjustmentReason.copy();
        dst.date = date == null ? null : date.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PaymentComponent))
          return false;
        PaymentComponent o = (PaymentComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(adjustment, o.adjustment, true) && compareDeep(adjustmentReason, o.adjustmentReason, true)
           && compareDeep(date, o.date, true) && compareDeep(amount, o.amount, true) && compareDeep(identifier, o.identifier, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PaymentComponent))
          return false;
        PaymentComponent o = (PaymentComponent) other;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, adjustment, adjustmentReason
          , date, amount, identifier);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.payment";

  }

  }

    @Block()
    public static class NoteComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An integer associated with each note which may be referred to from each service line item.
         */
        @Child(name = "number", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Sequence number for this note", formalDefinition="An integer associated with each note which may be referred to from each service line item." )
        protected PositiveIntType number;

        /**
         * The note purpose: Print/Display.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The note purpose: Print/Display." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/note-type")
        protected CodeableConcept type;

        /**
         * The note text.
         */
        @Child(name = "text", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Note explanitory text", formalDefinition="The note text." )
        protected StringType text;

        /**
         * The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.
         */
        @Child(name = "language", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language if different from the resource", formalDefinition="The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/languages")
        protected CodeableConcept language;

        private static final long serialVersionUID = -944255449L;

    /**
     * Constructor
     */
      public NoteComponent() {
        super();
      }

        /**
         * @return {@link #number} (An integer associated with each note which may be referred to from each service line item.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public PositiveIntType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NoteComponent.number");
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
        public NoteComponent setNumberElement(PositiveIntType value) { 
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
        public NoteComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new PositiveIntType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The note purpose: Print/Display.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NoteComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The note purpose: Print/Display.)
         */
        public NoteComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #text} (The note text.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NoteComponent.text");
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
        public NoteComponent setTextElement(StringType value) { 
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
        public NoteComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #language} (The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.)
         */
        public CodeableConcept getLanguage() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NoteComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new CodeableConcept(); // cc
          return this.language;
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.)
         */
        public NoteComponent setLanguage(CodeableConcept value) { 
          this.language = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("number", "positiveInt", "An integer associated with each note which may be referred to from each service line item.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("type", "CodeableConcept", "The note purpose: Print/Display.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("text", "string", "The note text.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("language", "CodeableConcept", "The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English.", 0, java.lang.Integer.MAX_VALUE, language));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : new Base[] {this.number}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1034364087: // number
          this.number = castToPositiveInt(value); // PositiveIntType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case -1613589672: // language
          this.language = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("number")) {
          this.number = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("language")) {
          this.language = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087:  return getNumberElement();
        case 3575610:  return getType(); 
        case 3556653:  return getTextElement();
        case -1613589672:  return getLanguage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return new String[] {"positiveInt"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3556653: /*text*/ return new String[] {"string"};
        case -1613589672: /*language*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.number");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.text");
        }
        else if (name.equals("language")) {
          this.language = new CodeableConcept();
          return this.language;
        }
        else
          return super.addChild(name);
      }

      public NoteComponent copy() {
        NoteComponent dst = new NoteComponent();
        copyValues(dst);
        dst.number = number == null ? null : number.copy();
        dst.type = type == null ? null : type.copy();
        dst.text = text == null ? null : text.copy();
        dst.language = language == null ? null : language.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NoteComponent))
          return false;
        NoteComponent o = (NoteComponent) other;
        return compareDeep(number, o.number, true) && compareDeep(type, o.type, true) && compareDeep(text, o.text, true)
           && compareDeep(language, o.language, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NoteComponent))
          return false;
        NoteComponent o = (NoteComponent) other;
        return compareValues(number, o.number, true) && compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(number, type, text, language
          );
      }

  public String fhirType() {
    return "ExplanationOfBenefit.processNote";

  }

  }

    @Block()
    public static class BenefitBalanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Dental, Vision, Medical, Pharmacy, Rehab etc.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of services covered", formalDefinition="Dental, Vision, Medical, Pharmacy, Rehab etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-category")
        protected CodeableConcept category;

        /**
         * Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.
         */
        @Child(name = "subCategory", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Detailed services covered within the type", formalDefinition="Dental: basic, major, ortho; Vision exam, glasses, contacts; etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-subcategory")
        protected CodeableConcept subCategory;

        /**
         * True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.
         */
        @Child(name = "excluded", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Excluded from the plan", formalDefinition="True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage." )
        protected BooleanType excluded;

        /**
         * A short name or tag for the benefit, for example MED01, or DENT2.
         */
        @Child(name = "name", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short name for the benefit", formalDefinition="A short name or tag for the benefit, for example MED01, or DENT2." )
        protected StringType name;

        /**
         * A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.
         */
        @Child(name = "description", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of the benefit or services covered", formalDefinition="A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'." )
        protected StringType description;

        /**
         * Network designation.
         */
        @Child(name = "network", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="In or out of network", formalDefinition="Network designation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-network")
        protected CodeableConcept network;

        /**
         * Unit designation: individual or family.
         */
        @Child(name = "unit", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Individual or family", formalDefinition="Unit designation: individual or family." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-unit")
        protected CodeableConcept unit;

        /**
         * The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.
         */
        @Child(name = "term", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Annual or lifetime", formalDefinition="The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-term")
        protected CodeableConcept term;

        /**
         * Benefits Used to date.
         */
        @Child(name = "financial", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Benefit Summary", formalDefinition="Benefits Used to date." )
        protected List<BenefitComponent> financial;

        private static final long serialVersionUID = 833826021L;

    /**
     * Constructor
     */
      public BenefitBalanceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public BenefitBalanceComponent(CodeableConcept category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
         */
        public BenefitBalanceComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #subCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
         */
        public CodeableConcept getSubCategory() { 
          if (this.subCategory == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.subCategory");
            else if (Configuration.doAutoCreate())
              this.subCategory = new CodeableConcept(); // cc
          return this.subCategory;
        }

        public boolean hasSubCategory() { 
          return this.subCategory != null && !this.subCategory.isEmpty();
        }

        /**
         * @param value {@link #subCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
         */
        public BenefitBalanceComponent setSubCategory(CodeableConcept value) { 
          this.subCategory = value;
          return this;
        }

        /**
         * @return {@link #excluded} (True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.). This is the underlying object with id, value and extensions. The accessor "getExcluded" gives direct access to the value
         */
        public BooleanType getExcludedElement() { 
          if (this.excluded == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.excluded");
            else if (Configuration.doAutoCreate())
              this.excluded = new BooleanType(); // bb
          return this.excluded;
        }

        public boolean hasExcludedElement() { 
          return this.excluded != null && !this.excluded.isEmpty();
        }

        public boolean hasExcluded() { 
          return this.excluded != null && !this.excluded.isEmpty();
        }

        /**
         * @param value {@link #excluded} (True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.). This is the underlying object with id, value and extensions. The accessor "getExcluded" gives direct access to the value
         */
        public BenefitBalanceComponent setExcludedElement(BooleanType value) { 
          this.excluded = value;
          return this;
        }

        /**
         * @return True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.
         */
        public boolean getExcluded() { 
          return this.excluded == null || this.excluded.isEmpty() ? false : this.excluded.getValue();
        }

        /**
         * @param value True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.
         */
        public BenefitBalanceComponent setExcluded(boolean value) { 
            if (this.excluded == null)
              this.excluded = new BooleanType();
            this.excluded.setValue(value);
          return this;
        }

        /**
         * @return {@link #name} (A short name or tag for the benefit, for example MED01, or DENT2.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (A short name or tag for the benefit, for example MED01, or DENT2.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public BenefitBalanceComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A short name or tag for the benefit, for example MED01, or DENT2.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A short name or tag for the benefit, for example MED01, or DENT2.
         */
        public BenefitBalanceComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public BenefitBalanceComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.
         */
        public BenefitBalanceComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #network} (Network designation.)
         */
        public CodeableConcept getNetwork() { 
          if (this.network == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.network");
            else if (Configuration.doAutoCreate())
              this.network = new CodeableConcept(); // cc
          return this.network;
        }

        public boolean hasNetwork() { 
          return this.network != null && !this.network.isEmpty();
        }

        /**
         * @param value {@link #network} (Network designation.)
         */
        public BenefitBalanceComponent setNetwork(CodeableConcept value) { 
          this.network = value;
          return this;
        }

        /**
         * @return {@link #unit} (Unit designation: individual or family.)
         */
        public CodeableConcept getUnit() { 
          if (this.unit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.unit");
            else if (Configuration.doAutoCreate())
              this.unit = new CodeableConcept(); // cc
          return this.unit;
        }

        public boolean hasUnit() { 
          return this.unit != null && !this.unit.isEmpty();
        }

        /**
         * @param value {@link #unit} (Unit designation: individual or family.)
         */
        public BenefitBalanceComponent setUnit(CodeableConcept value) { 
          this.unit = value;
          return this;
        }

        /**
         * @return {@link #term} (The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.)
         */
        public CodeableConcept getTerm() { 
          if (this.term == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitBalanceComponent.term");
            else if (Configuration.doAutoCreate())
              this.term = new CodeableConcept(); // cc
          return this.term;
        }

        public boolean hasTerm() { 
          return this.term != null && !this.term.isEmpty();
        }

        /**
         * @param value {@link #term} (The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.)
         */
        public BenefitBalanceComponent setTerm(CodeableConcept value) { 
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public BenefitBalanceComponent setFinancial(List<BenefitComponent> theFinancial) { 
          this.financial = theFinancial;
          return this;
        }

        public boolean hasFinancial() { 
          if (this.financial == null)
            return false;
          for (BenefitComponent item : this.financial)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public BenefitComponent addFinancial() { //3
          BenefitComponent t = new BenefitComponent();
          if (this.financial == null)
            this.financial = new ArrayList<BenefitComponent>();
          this.financial.add(t);
          return t;
        }

        public BenefitBalanceComponent addFinancial(BenefitComponent t) { //3
          if (t == null)
            return this;
          if (this.financial == null)
            this.financial = new ArrayList<BenefitComponent>();
          this.financial.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #financial}, creating it if it does not already exist
         */
        public BenefitComponent getFinancialFirstRep() { 
          if (getFinancial().isEmpty()) {
            addFinancial();
          }
          return getFinancial().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "CodeableConcept", "Dental, Vision, Medical, Pharmacy, Rehab etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("subCategory", "CodeableConcept", "Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.", 0, java.lang.Integer.MAX_VALUE, subCategory));
          childrenList.add(new Property("excluded", "boolean", "True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.", 0, java.lang.Integer.MAX_VALUE, excluded));
          childrenList.add(new Property("name", "string", "A short name or tag for the benefit, for example MED01, or DENT2.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("network", "CodeableConcept", "Network designation.", 0, java.lang.Integer.MAX_VALUE, network));
          childrenList.add(new Property("unit", "CodeableConcept", "Unit designation: individual or family.", 0, java.lang.Integer.MAX_VALUE, unit));
          childrenList.add(new Property("term", "CodeableConcept", "The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.", 0, java.lang.Integer.MAX_VALUE, term));
          childrenList.add(new Property("financial", "", "Benefits Used to date.", 0, java.lang.Integer.MAX_VALUE, financial));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1365024606: /*subCategory*/ return this.subCategory == null ? new Base[0] : new Base[] {this.subCategory}; // CodeableConcept
        case 1994055114: /*excluded*/ return this.excluded == null ? new Base[0] : new Base[] {this.excluded}; // BooleanType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 1843485230: /*network*/ return this.network == null ? new Base[0] : new Base[] {this.network}; // CodeableConcept
        case 3594628: /*unit*/ return this.unit == null ? new Base[0] : new Base[] {this.unit}; // CodeableConcept
        case 3556460: /*term*/ return this.term == null ? new Base[0] : new Base[] {this.term}; // CodeableConcept
        case 357555337: /*financial*/ return this.financial == null ? new Base[0] : this.financial.toArray(new Base[this.financial.size()]); // BenefitComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1365024606: // subCategory
          this.subCategory = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1994055114: // excluded
          this.excluded = castToBoolean(value); // BooleanType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 1843485230: // network
          this.network = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3594628: // unit
          this.unit = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3556460: // term
          this.term = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 357555337: // financial
          this.getFinancial().add((BenefitComponent) value); // BenefitComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subCategory")) {
          this.subCategory = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("excluded")) {
          this.excluded = castToBoolean(value); // BooleanType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("network")) {
          this.network = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("unit")) {
          this.unit = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("term")) {
          this.term = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("financial")) {
          this.getFinancial().add((BenefitComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case 1365024606:  return getSubCategory(); 
        case 1994055114:  return getExcludedElement();
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case 1843485230:  return getNetwork(); 
        case 3594628:  return getUnit(); 
        case 3556460:  return getTerm(); 
        case 357555337:  return addFinancial(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1365024606: /*subCategory*/ return new String[] {"CodeableConcept"};
        case 1994055114: /*excluded*/ return new String[] {"boolean"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 1843485230: /*network*/ return new String[] {"CodeableConcept"};
        case 3594628: /*unit*/ return new String[] {"CodeableConcept"};
        case 3556460: /*term*/ return new String[] {"CodeableConcept"};
        case 357555337: /*financial*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("subCategory")) {
          this.subCategory = new CodeableConcept();
          return this.subCategory;
        }
        else if (name.equals("excluded")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.excluded");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.description");
        }
        else if (name.equals("network")) {
          this.network = new CodeableConcept();
          return this.network;
        }
        else if (name.equals("unit")) {
          this.unit = new CodeableConcept();
          return this.unit;
        }
        else if (name.equals("term")) {
          this.term = new CodeableConcept();
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
        dst.excluded = excluded == null ? null : excluded.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
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
           && compareDeep(excluded, o.excluded, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
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
        return compareValues(excluded, o.excluded, true) && compareValues(name, o.name, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, subCategory, excluded
          , name, description, network, unit, term, financial);
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
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Deductable, visits, benefit amount", formalDefinition="Deductable, visits, benefit amount." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-type")
        protected CodeableConcept type;

        /**
         * Benefits allowed.
         */
        @Child(name = "allowed", type = {UnsignedIntType.class, StringType.class, Money.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Benefits allowed", formalDefinition="Benefits allowed." )
        protected Type allowed;

        /**
         * Benefits used.
         */
        @Child(name = "used", type = {UnsignedIntType.class, Money.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Benefits used", formalDefinition="Benefits used." )
        protected Type used;

        private static final long serialVersionUID = -1506285314L;

    /**
     * Constructor
     */
      public BenefitComponent() {
        super();
      }

    /**
     * Constructor
     */
      public BenefitComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Deductable, visits, benefit amount.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Deductable, visits, benefit amount.)
         */
        public BenefitComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #allowed} (Benefits allowed.)
         */
        public Type getAllowed() { 
          return this.allowed;
        }

        /**
         * @return {@link #allowed} (Benefits allowed.)
         */
        public UnsignedIntType getAllowedUnsignedIntType() throws FHIRException { 
          if (!(this.allowed instanceof UnsignedIntType))
            throw new FHIRException("Type mismatch: the type UnsignedIntType was expected, but "+this.allowed.getClass().getName()+" was encountered");
          return (UnsignedIntType) this.allowed;
        }

        public boolean hasAllowedUnsignedIntType() { 
          return this.allowed instanceof UnsignedIntType;
        }

        /**
         * @return {@link #allowed} (Benefits allowed.)
         */
        public StringType getAllowedStringType() throws FHIRException { 
          if (!(this.allowed instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.allowed.getClass().getName()+" was encountered");
          return (StringType) this.allowed;
        }

        public boolean hasAllowedStringType() { 
          return this.allowed instanceof StringType;
        }

        /**
         * @return {@link #allowed} (Benefits allowed.)
         */
        public Money getAllowedMoney() throws FHIRException { 
          if (!(this.allowed instanceof Money))
            throw new FHIRException("Type mismatch: the type Money was expected, but "+this.allowed.getClass().getName()+" was encountered");
          return (Money) this.allowed;
        }

        public boolean hasAllowedMoney() { 
          return this.allowed instanceof Money;
        }

        public boolean hasAllowed() { 
          return this.allowed != null && !this.allowed.isEmpty();
        }

        /**
         * @param value {@link #allowed} (Benefits allowed.)
         */
        public BenefitComponent setAllowed(Type value) { 
          this.allowed = value;
          return this;
        }

        /**
         * @return {@link #used} (Benefits used.)
         */
        public Type getUsed() { 
          return this.used;
        }

        /**
         * @return {@link #used} (Benefits used.)
         */
        public UnsignedIntType getUsedUnsignedIntType() throws FHIRException { 
          if (!(this.used instanceof UnsignedIntType))
            throw new FHIRException("Type mismatch: the type UnsignedIntType was expected, but "+this.used.getClass().getName()+" was encountered");
          return (UnsignedIntType) this.used;
        }

        public boolean hasUsedUnsignedIntType() { 
          return this.used instanceof UnsignedIntType;
        }

        /**
         * @return {@link #used} (Benefits used.)
         */
        public Money getUsedMoney() throws FHIRException { 
          if (!(this.used instanceof Money))
            throw new FHIRException("Type mismatch: the type Money was expected, but "+this.used.getClass().getName()+" was encountered");
          return (Money) this.used;
        }

        public boolean hasUsedMoney() { 
          return this.used instanceof Money;
        }

        public boolean hasUsed() { 
          return this.used != null && !this.used.isEmpty();
        }

        /**
         * @param value {@link #used} (Benefits used.)
         */
        public BenefitComponent setUsed(Type value) { 
          this.used = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Deductable, visits, benefit amount.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("allowed[x]", "unsignedInt|string|Money", "Benefits allowed.", 0, java.lang.Integer.MAX_VALUE, allowed));
          childrenList.add(new Property("used[x]", "unsignedInt|Money", "Benefits used.", 0, java.lang.Integer.MAX_VALUE, used));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -911343192: /*allowed*/ return this.allowed == null ? new Base[0] : new Base[] {this.allowed}; // Type
        case 3599293: /*used*/ return this.used == null ? new Base[0] : new Base[] {this.used}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -911343192: // allowed
          this.allowed = castToType(value); // Type
          return value;
        case 3599293: // used
          this.used = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("allowed[x]")) {
          this.allowed = castToType(value); // Type
        } else if (name.equals("used[x]")) {
          this.used = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1336663592:  return getAllowed(); 
        case -911343192:  return getAllowed(); 
        case -147553373:  return getUsed(); 
        case 3599293:  return getUsed(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -911343192: /*allowed*/ return new String[] {"unsignedInt", "string", "Money"};
        case 3599293: /*used*/ return new String[] {"unsignedInt", "Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("allowedUnsignedInt")) {
          this.allowed = new UnsignedIntType();
          return this.allowed;
        }
        else if (name.equals("allowedString")) {
          this.allowed = new StringType();
          return this.allowed;
        }
        else if (name.equals("allowedMoney")) {
          this.allowed = new Money();
          return this.allowed;
        }
        else if (name.equals("usedUnsignedInt")) {
          this.used = new UnsignedIntType();
          return this.used;
        }
        else if (name.equals("usedMoney")) {
          this.used = new Money();
          return this.used;
        }
        else
          return super.addChild(name);
      }

      public BenefitComponent copy() {
        BenefitComponent dst = new BenefitComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.allowed = allowed == null ? null : allowed.copy();
        dst.used = used == null ? null : used.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BenefitComponent))
          return false;
        BenefitComponent o = (BenefitComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(allowed, o.allowed, true) && compareDeep(used, o.used, true)
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, allowed, used);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.benefitBalance.financial";

  }

  }

    /**
     * The EOB Business Identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier", formalDefinition="The EOB Business Identifier." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/explanationofbenefit-status")
    protected Enumeration<ExplanationOfBenefitStatus> status;

    /**
     * The category of claim, eg, oral, pharmacy, vision, insitutional, professional.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Type or discipline", formalDefinition="The category of claim, eg, oral, pharmacy, vision, insitutional, professional." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-type")
    protected CodeableConcept type;

    /**
     * A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.
     */
    @Child(name = "subType", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Finer grained claim type information", formalDefinition="A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-subtype")
    protected List<CodeableConcept> subType;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Patient.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient Resource.)
     */
    protected Patient patientTarget;

    /**
     * The billable period for which charges are being submitted.
     */
    @Child(name = "billablePeriod", type = {Period.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Period for charge submission", formalDefinition="The billable period for which charges are being submitted." )
    protected Period billablePeriod;

    /**
     * The date when the EOB was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the EOB was created." )
    protected DateTimeType created;

    /**
     * The person who created the explanation of benefit.
     */
    @Child(name = "enterer", type = {Practitioner.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Author", formalDefinition="The person who created the explanation of benefit." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (The person who created the explanation of benefit.)
     */
    protected Practitioner entererTarget;

    /**
     * The insurer which is responsible for the explanation of benefit.
     */
    @Child(name = "insurer", type = {Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurer responsible for the EOB", formalDefinition="The insurer which is responsible for the explanation of benefit." )
    protected Reference insurer;

    /**
     * The actual object that is the target of the reference (The insurer which is responsible for the explanation of benefit.)
     */
    protected Organization insurerTarget;

    /**
     * The provider which is responsible for the claim.
     */
    @Child(name = "provider", type = {Practitioner.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible provider for the claim", formalDefinition="The provider which is responsible for the claim." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The provider which is responsible for the claim.)
     */
    protected Practitioner providerTarget;

    /**
     * The provider which is responsible for the claim.
     */
    @Child(name = "organization", type = {Organization.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization for the claim", formalDefinition="The provider which is responsible for the claim." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The provider which is responsible for the claim.)
     */
    protected Organization organizationTarget;

    /**
     * The referral resource which lists the date, practitioner, reason and other supporting information.
     */
    @Child(name = "referral", type = {ReferralRequest.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Treatment Referral", formalDefinition="The referral resource which lists the date, practitioner, reason and other supporting information." )
    protected Reference referral;

    /**
     * The actual object that is the target of the reference (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    protected ReferralRequest referralTarget;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Location.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Reference facility;

    /**
     * The actual object that is the target of the reference (Facility where the services were provided.)
     */
    protected Location facilityTarget;

    /**
     * The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.
     */
    @Child(name = "claim", type = {Claim.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Claim reference", formalDefinition="The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number." )
    protected Reference claim;

    /**
     * The actual object that is the target of the reference (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    protected Claim claimTarget;

    /**
     * The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.
     */
    @Child(name = "claimResponse", type = {ClaimResponse.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Claim response reference", formalDefinition="The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number." )
    protected Reference claimResponse;

    /**
     * The actual object that is the target of the reference (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    protected ClaimResponse claimResponseTarget;

    /**
     * Processing outcome errror, partial or complete processing.
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="complete | error | partial", formalDefinition="Processing outcome errror, partial or complete processing." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/remittance-outcome")
    protected CodeableConcept outcome;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * Other claims which are related to this claim such as prior claim versions or for related services.
     */
    @Child(name = "related", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Related Claims which may be revelant to processing this claim", formalDefinition="Other claims which are related to this claim such as prior claim versions or for related services." )
    protected List<RelatedClaimComponent> related;

    /**
     * Prescription to support the dispensing of Pharmacy or Vision products.
     */
    @Child(name = "prescription", type = {MedicationRequest.class, VisionPrescription.class}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Prescription authorizing services or products", formalDefinition="Prescription to support the dispensing of Pharmacy or Vision products." )
    protected Reference prescription;

    /**
     * The actual object that is the target of the reference (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    protected Resource prescriptionTarget;

    /**
     * Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products. For example, a physician may prescribe a medication which the pharmacy determines is contraindicated, or for which the patient has an intolerance, and therefor issues a new precription for an alternate medication which has the same theraputic intent. The prescription from the pharmacy becomes the 'prescription' and that from the physician becomes the 'original prescription'.
     */
    @Child(name = "originalPrescription", type = {MedicationRequest.class}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Original prescription if superceded by fulfiller", formalDefinition="Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products. For example, a physician may prescribe a medication which the pharmacy determines is contraindicated, or for which the patient has an intolerance, and therefor issues a new precription for an alternate medication which has the same theraputic intent. The prescription from the pharmacy becomes the 'prescription' and that from the physician becomes the 'original prescription'." )
    protected Reference originalPrescription;

    /**
     * The actual object that is the target of the reference (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products. For example, a physician may prescribe a medication which the pharmacy determines is contraindicated, or for which the patient has an intolerance, and therefor issues a new precription for an alternate medication which has the same theraputic intent. The prescription from the pharmacy becomes the 'prescription' and that from the physician becomes the 'original prescription'.)
     */
    protected MedicationRequest originalPrescriptionTarget;

    /**
     * The party to be reimbursed for the services.
     */
    @Child(name = "payee", type = {}, order=20, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Party to be paid any benefits payable", formalDefinition="The party to be reimbursed for the services." )
    protected PayeeComponent payee;

    /**
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues. Often there are mutiple jurisdiction specific valuesets which are required.
     */
    @Child(name = "information", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Exceptions, special considerations, the condition, situation, prior or concurrent issues", formalDefinition="Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues. Often there are mutiple jurisdiction specific valuesets which are required." )
    protected List<SupportingInformationComponent> information;

    /**
     * The members of the team who provided the overall service as well as their role and whether responsible and qualifications.
     */
    @Child(name = "careTeam", type = {}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Care Team members", formalDefinition="The members of the team who provided the overall service as well as their role and whether responsible and qualifications." )
    protected List<CareTeamComponent> careTeam;

    /**
     * Ordered list of patient diagnosis for which care is sought.
     */
    @Child(name = "diagnosis", type = {}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="List of Diagnosis", formalDefinition="Ordered list of patient diagnosis for which care is sought." )
    protected List<DiagnosisComponent> diagnosis;

    /**
     * Ordered list of patient procedures performed to support the adjudication.
     */
    @Child(name = "procedure", type = {}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Procedures performed", formalDefinition="Ordered list of patient procedures performed to support the adjudication." )
    protected List<ProcedureComponent> procedure;

    /**
     * Precedence (primary, secondary, etc.).
     */
    @Child(name = "precedence", type = {PositiveIntType.class}, order=25, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Precedence (primary, secondary, etc.)", formalDefinition="Precedence (primary, secondary, etc.)." )
    protected PositiveIntType precedence;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "insurance", type = {}, order=26, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected InsuranceComponent insurance;

    /**
     * An accident which resulted in the need for healthcare services.
     */
    @Child(name = "accident", type = {}, order=27, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details of an accident", formalDefinition="An accident which resulted in the need for healthcare services." )
    protected AccidentComponent accident;

    /**
     * The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).
     */
    @Child(name = "employmentImpacted", type = {Period.class}, order=28, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Period unable to work", formalDefinition="The start and optional end dates of when the patient was precluded from working due to the treatable condition(s)." )
    protected Period employmentImpacted;

    /**
     * The start and optional end dates of when the patient was confined to a treatment center.
     */
    @Child(name = "hospitalization", type = {Period.class}, order=29, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Period in hospital", formalDefinition="The start and optional end dates of when the patient was confined to a treatment center." )
    protected Period hospitalization;

    /**
     * First tier of goods and services.
     */
    @Child(name = "item", type = {}, order=30, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Goods and Services", formalDefinition="First tier of goods and services." )
    protected List<ItemComponent> item;

    /**
     * The first tier service adjudications for payor added services.
     */
    @Child(name = "addItem", type = {}, order=31, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Insurer added line items", formalDefinition="The first tier service adjudications for payor added services." )
    protected List<AddedItemComponent> addItem;

    /**
     * The total cost of the services reported.
     */
    @Child(name = "totalCost", type = {Money.class}, order=32, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Total Cost of service from the Claim", formalDefinition="The total cost of the services reported." )
    protected Money totalCost;

    /**
     * The amount of deductable applied which was not allocated to any particular service line.
     */
    @Child(name = "unallocDeductable", type = {Money.class}, order=33, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Unallocated deductable", formalDefinition="The amount of deductable applied which was not allocated to any particular service line." )
    protected Money unallocDeductable;

    /**
     * Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable).
     */
    @Child(name = "totalBenefit", type = {Money.class}, order=34, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Total benefit payable for the Claim", formalDefinition="Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable)." )
    protected Money totalBenefit;

    /**
     * Payment details for the claim if the claim has been paid.
     */
    @Child(name = "payment", type = {}, order=35, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Payment (if paid)", formalDefinition="Payment details for the claim if the claim has been paid." )
    protected PaymentComponent payment;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {CodeableConcept.class}, order=36, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/forms")
    protected CodeableConcept form;

    /**
     * Note text.
     */
    @Child(name = "processNote", type = {}, order=37, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Processing notes", formalDefinition="Note text." )
    protected List<NoteComponent> processNote;

    /**
     * Balance by Benefit Category.
     */
    @Child(name = "benefitBalance", type = {}, order=38, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Balance by Benefit Category", formalDefinition="Balance by Benefit Category." )
    protected List<BenefitBalanceComponent> benefitBalance;

    private static final long serialVersionUID = -1301056913L;

  /**
   * Constructor
   */
    public ExplanationOfBenefit() {
      super();
    }

    /**
     * @return {@link #identifier} (The EOB Business Identifier.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setIdentifier(List<Identifier> theIdentifier) { 
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

    public ExplanationOfBenefit addIdentifier(Identifier t) { //3
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
    public Enumeration<ExplanationOfBenefitStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ExplanationOfBenefitStatus>(new ExplanationOfBenefitStatusEnumFactory()); // bb
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
    public ExplanationOfBenefit setStatusElement(Enumeration<ExplanationOfBenefitStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public ExplanationOfBenefitStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public ExplanationOfBenefit setStatus(ExplanationOfBenefitStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ExplanationOfBenefitStatus>(new ExplanationOfBenefitStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (The category of claim, eg, oral, pharmacy, vision, insitutional, professional.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The category of claim, eg, oral, pharmacy, vision, insitutional, professional.)
     */
    public ExplanationOfBenefit setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subType} (A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.)
     */
    public List<CodeableConcept> getSubType() { 
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      return this.subType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setSubType(List<CodeableConcept> theSubType) { 
      this.subType = theSubType;
      return this;
    }

    public boolean hasSubType() { 
      if (this.subType == null)
        return false;
      for (CodeableConcept item : this.subType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addSubType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return t;
    }

    public ExplanationOfBenefit addSubType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subType}, creating it if it does not already exist
     */
    public CodeableConcept getSubTypeFirstRep() { 
      if (getSubType().isEmpty()) {
        addSubType();
      }
      return getSubType().get(0);
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
     * @return {@link #enterer} (The person who created the explanation of benefit.)
     */
    public Reference getEnterer() { 
      if (this.enterer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.enterer");
        else if (Configuration.doAutoCreate())
          this.enterer = new Reference(); // cc
      return this.enterer;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (The person who created the explanation of benefit.)
     */
    public ExplanationOfBenefit setEnterer(Reference value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #enterer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person who created the explanation of benefit.)
     */
    public Practitioner getEntererTarget() { 
      if (this.entererTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.enterer");
        else if (Configuration.doAutoCreate())
          this.entererTarget = new Practitioner(); // aa
      return this.entererTarget;
    }

    /**
     * @param value {@link #enterer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person who created the explanation of benefit.)
     */
    public ExplanationOfBenefit setEntererTarget(Practitioner value) { 
      this.entererTarget = value;
      return this;
    }

    /**
     * @return {@link #insurer} (The insurer which is responsible for the explanation of benefit.)
     */
    public Reference getInsurer() { 
      if (this.insurer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.insurer");
        else if (Configuration.doAutoCreate())
          this.insurer = new Reference(); // cc
      return this.insurer;
    }

    public boolean hasInsurer() { 
      return this.insurer != null && !this.insurer.isEmpty();
    }

    /**
     * @param value {@link #insurer} (The insurer which is responsible for the explanation of benefit.)
     */
    public ExplanationOfBenefit setInsurer(Reference value) { 
      this.insurer = value;
      return this;
    }

    /**
     * @return {@link #insurer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The insurer which is responsible for the explanation of benefit.)
     */
    public Organization getInsurerTarget() { 
      if (this.insurerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.insurer");
        else if (Configuration.doAutoCreate())
          this.insurerTarget = new Organization(); // aa
      return this.insurerTarget;
    }

    /**
     * @param value {@link #insurer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The insurer which is responsible for the explanation of benefit.)
     */
    public ExplanationOfBenefit setInsurerTarget(Organization value) { 
      this.insurerTarget = value;
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
     * @return {@link #outcome} (Processing outcome errror, partial or complete processing.)
     */
    public CodeableConcept getOutcome() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new CodeableConcept(); // cc
      return this.outcome;
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (Processing outcome errror, partial or complete processing.)
     */
    public ExplanationOfBenefit setOutcome(CodeableConcept value) { 
      this.outcome = value;
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
     * @return {@link #related} (Other claims which are related to this claim such as prior claim versions or for related services.)
     */
    public List<RelatedClaimComponent> getRelated() { 
      if (this.related == null)
        this.related = new ArrayList<RelatedClaimComponent>();
      return this.related;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setRelated(List<RelatedClaimComponent> theRelated) { 
      this.related = theRelated;
      return this;
    }

    public boolean hasRelated() { 
      if (this.related == null)
        return false;
      for (RelatedClaimComponent item : this.related)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RelatedClaimComponent addRelated() { //3
      RelatedClaimComponent t = new RelatedClaimComponent();
      if (this.related == null)
        this.related = new ArrayList<RelatedClaimComponent>();
      this.related.add(t);
      return t;
    }

    public ExplanationOfBenefit addRelated(RelatedClaimComponent t) { //3
      if (t == null)
        return this;
      if (this.related == null)
        this.related = new ArrayList<RelatedClaimComponent>();
      this.related.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #related}, creating it if it does not already exist
     */
    public RelatedClaimComponent getRelatedFirstRep() { 
      if (getRelated().isEmpty()) {
        addRelated();
      }
      return getRelated().get(0);
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
     * @return {@link #originalPrescription} (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products. For example, a physician may prescribe a medication which the pharmacy determines is contraindicated, or for which the patient has an intolerance, and therefor issues a new precription for an alternate medication which has the same theraputic intent. The prescription from the pharmacy becomes the 'prescription' and that from the physician becomes the 'original prescription'.)
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
     * @param value {@link #originalPrescription} (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products. For example, a physician may prescribe a medication which the pharmacy determines is contraindicated, or for which the patient has an intolerance, and therefor issues a new precription for an alternate medication which has the same theraputic intent. The prescription from the pharmacy becomes the 'prescription' and that from the physician becomes the 'original prescription'.)
     */
    public ExplanationOfBenefit setOriginalPrescription(Reference value) { 
      this.originalPrescription = value;
      return this;
    }

    /**
     * @return {@link #originalPrescription} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products. For example, a physician may prescribe a medication which the pharmacy determines is contraindicated, or for which the patient has an intolerance, and therefor issues a new precription for an alternate medication which has the same theraputic intent. The prescription from the pharmacy becomes the 'prescription' and that from the physician becomes the 'original prescription'.)
     */
    public MedicationRequest getOriginalPrescriptionTarget() { 
      if (this.originalPrescriptionTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.originalPrescription");
        else if (Configuration.doAutoCreate())
          this.originalPrescriptionTarget = new MedicationRequest(); // aa
      return this.originalPrescriptionTarget;
    }

    /**
     * @param value {@link #originalPrescription} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products. For example, a physician may prescribe a medication which the pharmacy determines is contraindicated, or for which the patient has an intolerance, and therefor issues a new precription for an alternate medication which has the same theraputic intent. The prescription from the pharmacy becomes the 'prescription' and that from the physician becomes the 'original prescription'.)
     */
    public ExplanationOfBenefit setOriginalPrescriptionTarget(MedicationRequest value) { 
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
     * @return {@link #information} (Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues. Often there are mutiple jurisdiction specific valuesets which are required.)
     */
    public List<SupportingInformationComponent> getInformation() { 
      if (this.information == null)
        this.information = new ArrayList<SupportingInformationComponent>();
      return this.information;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setInformation(List<SupportingInformationComponent> theInformation) { 
      this.information = theInformation;
      return this;
    }

    public boolean hasInformation() { 
      if (this.information == null)
        return false;
      for (SupportingInformationComponent item : this.information)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SupportingInformationComponent addInformation() { //3
      SupportingInformationComponent t = new SupportingInformationComponent();
      if (this.information == null)
        this.information = new ArrayList<SupportingInformationComponent>();
      this.information.add(t);
      return t;
    }

    public ExplanationOfBenefit addInformation(SupportingInformationComponent t) { //3
      if (t == null)
        return this;
      if (this.information == null)
        this.information = new ArrayList<SupportingInformationComponent>();
      this.information.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #information}, creating it if it does not already exist
     */
    public SupportingInformationComponent getInformationFirstRep() { 
      if (getInformation().isEmpty()) {
        addInformation();
      }
      return getInformation().get(0);
    }

    /**
     * @return {@link #careTeam} (The members of the team who provided the overall service as well as their role and whether responsible and qualifications.)
     */
    public List<CareTeamComponent> getCareTeam() { 
      if (this.careTeam == null)
        this.careTeam = new ArrayList<CareTeamComponent>();
      return this.careTeam;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setCareTeam(List<CareTeamComponent> theCareTeam) { 
      this.careTeam = theCareTeam;
      return this;
    }

    public boolean hasCareTeam() { 
      if (this.careTeam == null)
        return false;
      for (CareTeamComponent item : this.careTeam)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CareTeamComponent addCareTeam() { //3
      CareTeamComponent t = new CareTeamComponent();
      if (this.careTeam == null)
        this.careTeam = new ArrayList<CareTeamComponent>();
      this.careTeam.add(t);
      return t;
    }

    public ExplanationOfBenefit addCareTeam(CareTeamComponent t) { //3
      if (t == null)
        return this;
      if (this.careTeam == null)
        this.careTeam = new ArrayList<CareTeamComponent>();
      this.careTeam.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #careTeam}, creating it if it does not already exist
     */
    public CareTeamComponent getCareTeamFirstRep() { 
      if (getCareTeam().isEmpty()) {
        addCareTeam();
      }
      return getCareTeam().get(0);
    }

    /**
     * @return {@link #diagnosis} (Ordered list of patient diagnosis for which care is sought.)
     */
    public List<DiagnosisComponent> getDiagnosis() { 
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      return this.diagnosis;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setDiagnosis(List<DiagnosisComponent> theDiagnosis) { 
      this.diagnosis = theDiagnosis;
      return this;
    }

    public boolean hasDiagnosis() { 
      if (this.diagnosis == null)
        return false;
      for (DiagnosisComponent item : this.diagnosis)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DiagnosisComponent addDiagnosis() { //3
      DiagnosisComponent t = new DiagnosisComponent();
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      this.diagnosis.add(t);
      return t;
    }

    public ExplanationOfBenefit addDiagnosis(DiagnosisComponent t) { //3
      if (t == null)
        return this;
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      this.diagnosis.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #diagnosis}, creating it if it does not already exist
     */
    public DiagnosisComponent getDiagnosisFirstRep() { 
      if (getDiagnosis().isEmpty()) {
        addDiagnosis();
      }
      return getDiagnosis().get(0);
    }

    /**
     * @return {@link #procedure} (Ordered list of patient procedures performed to support the adjudication.)
     */
    public List<ProcedureComponent> getProcedure() { 
      if (this.procedure == null)
        this.procedure = new ArrayList<ProcedureComponent>();
      return this.procedure;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setProcedure(List<ProcedureComponent> theProcedure) { 
      this.procedure = theProcedure;
      return this;
    }

    public boolean hasProcedure() { 
      if (this.procedure == null)
        return false;
      for (ProcedureComponent item : this.procedure)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProcedureComponent addProcedure() { //3
      ProcedureComponent t = new ProcedureComponent();
      if (this.procedure == null)
        this.procedure = new ArrayList<ProcedureComponent>();
      this.procedure.add(t);
      return t;
    }

    public ExplanationOfBenefit addProcedure(ProcedureComponent t) { //3
      if (t == null)
        return this;
      if (this.procedure == null)
        this.procedure = new ArrayList<ProcedureComponent>();
      this.procedure.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #procedure}, creating it if it does not already exist
     */
    public ProcedureComponent getProcedureFirstRep() { 
      if (getProcedure().isEmpty()) {
        addProcedure();
      }
      return getProcedure().get(0);
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
     * @return {@link #insurance} (Financial instrument by which payment information for health care.)
     */
    public InsuranceComponent getInsurance() { 
      if (this.insurance == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.insurance");
        else if (Configuration.doAutoCreate())
          this.insurance = new InsuranceComponent(); // cc
      return this.insurance;
    }

    public boolean hasInsurance() { 
      return this.insurance != null && !this.insurance.isEmpty();
    }

    /**
     * @param value {@link #insurance} (Financial instrument by which payment information for health care.)
     */
    public ExplanationOfBenefit setInsurance(InsuranceComponent value) { 
      this.insurance = value;
      return this;
    }

    /**
     * @return {@link #accident} (An accident which resulted in the need for healthcare services.)
     */
    public AccidentComponent getAccident() { 
      if (this.accident == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.accident");
        else if (Configuration.doAutoCreate())
          this.accident = new AccidentComponent(); // cc
      return this.accident;
    }

    public boolean hasAccident() { 
      return this.accident != null && !this.accident.isEmpty();
    }

    /**
     * @param value {@link #accident} (An accident which resulted in the need for healthcare services.)
     */
    public ExplanationOfBenefit setAccident(AccidentComponent value) { 
      this.accident = value;
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
    public List<ItemComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<ItemComponent>();
      return this.item;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setItem(List<ItemComponent> theItem) { 
      this.item = theItem;
      return this;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (ItemComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ItemComponent addItem() { //3
      ItemComponent t = new ItemComponent();
      if (this.item == null)
        this.item = new ArrayList<ItemComponent>();
      this.item.add(t);
      return t;
    }

    public ExplanationOfBenefit addItem(ItemComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<ItemComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
     */
    public ItemComponent getItemFirstRep() { 
      if (getItem().isEmpty()) {
        addItem();
      }
      return getItem().get(0);
    }

    /**
     * @return {@link #addItem} (The first tier service adjudications for payor added services.)
     */
    public List<AddedItemComponent> getAddItem() { 
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      return this.addItem;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setAddItem(List<AddedItemComponent> theAddItem) { 
      this.addItem = theAddItem;
      return this;
    }

    public boolean hasAddItem() { 
      if (this.addItem == null)
        return false;
      for (AddedItemComponent item : this.addItem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public AddedItemComponent addAddItem() { //3
      AddedItemComponent t = new AddedItemComponent();
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      this.addItem.add(t);
      return t;
    }

    public ExplanationOfBenefit addAddItem(AddedItemComponent t) { //3
      if (t == null)
        return this;
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      this.addItem.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #addItem}, creating it if it does not already exist
     */
    public AddedItemComponent getAddItemFirstRep() { 
      if (getAddItem().isEmpty()) {
        addAddItem();
      }
      return getAddItem().get(0);
    }

    /**
     * @return {@link #totalCost} (The total cost of the services reported.)
     */
    public Money getTotalCost() { 
      if (this.totalCost == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.totalCost");
        else if (Configuration.doAutoCreate())
          this.totalCost = new Money(); // cc
      return this.totalCost;
    }

    public boolean hasTotalCost() { 
      return this.totalCost != null && !this.totalCost.isEmpty();
    }

    /**
     * @param value {@link #totalCost} (The total cost of the services reported.)
     */
    public ExplanationOfBenefit setTotalCost(Money value) { 
      this.totalCost = value;
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
     * @return {@link #payment} (Payment details for the claim if the claim has been paid.)
     */
    public PaymentComponent getPayment() { 
      if (this.payment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.payment");
        else if (Configuration.doAutoCreate())
          this.payment = new PaymentComponent(); // cc
      return this.payment;
    }

    public boolean hasPayment() { 
      return this.payment != null && !this.payment.isEmpty();
    }

    /**
     * @param value {@link #payment} (Payment details for the claim if the claim has been paid.)
     */
    public ExplanationOfBenefit setPayment(PaymentComponent value) { 
      this.payment = value;
      return this;
    }

    /**
     * @return {@link #form} (The form to be used for printing the content.)
     */
    public CodeableConcept getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.form");
        else if (Configuration.doAutoCreate())
          this.form = new CodeableConcept(); // cc
      return this.form;
    }

    public boolean hasForm() { 
      return this.form != null && !this.form.isEmpty();
    }

    /**
     * @param value {@link #form} (The form to be used for printing the content.)
     */
    public ExplanationOfBenefit setForm(CodeableConcept value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #processNote} (Note text.)
     */
    public List<NoteComponent> getProcessNote() { 
      if (this.processNote == null)
        this.processNote = new ArrayList<NoteComponent>();
      return this.processNote;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setProcessNote(List<NoteComponent> theProcessNote) { 
      this.processNote = theProcessNote;
      return this;
    }

    public boolean hasProcessNote() { 
      if (this.processNote == null)
        return false;
      for (NoteComponent item : this.processNote)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public NoteComponent addProcessNote() { //3
      NoteComponent t = new NoteComponent();
      if (this.processNote == null)
        this.processNote = new ArrayList<NoteComponent>();
      this.processNote.add(t);
      return t;
    }

    public ExplanationOfBenefit addProcessNote(NoteComponent t) { //3
      if (t == null)
        return this;
      if (this.processNote == null)
        this.processNote = new ArrayList<NoteComponent>();
      this.processNote.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #processNote}, creating it if it does not already exist
     */
    public NoteComponent getProcessNoteFirstRep() { 
      if (getProcessNote().isEmpty()) {
        addProcessNote();
      }
      return getProcessNote().get(0);
    }

    /**
     * @return {@link #benefitBalance} (Balance by Benefit Category.)
     */
    public List<BenefitBalanceComponent> getBenefitBalance() { 
      if (this.benefitBalance == null)
        this.benefitBalance = new ArrayList<BenefitBalanceComponent>();
      return this.benefitBalance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setBenefitBalance(List<BenefitBalanceComponent> theBenefitBalance) { 
      this.benefitBalance = theBenefitBalance;
      return this;
    }

    public boolean hasBenefitBalance() { 
      if (this.benefitBalance == null)
        return false;
      for (BenefitBalanceComponent item : this.benefitBalance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public BenefitBalanceComponent addBenefitBalance() { //3
      BenefitBalanceComponent t = new BenefitBalanceComponent();
      if (this.benefitBalance == null)
        this.benefitBalance = new ArrayList<BenefitBalanceComponent>();
      this.benefitBalance.add(t);
      return t;
    }

    public ExplanationOfBenefit addBenefitBalance(BenefitBalanceComponent t) { //3
      if (t == null)
        return this;
      if (this.benefitBalance == null)
        this.benefitBalance = new ArrayList<BenefitBalanceComponent>();
      this.benefitBalance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #benefitBalance}, creating it if it does not already exist
     */
    public BenefitBalanceComponent getBenefitBalanceFirstRep() { 
      if (getBenefitBalance().isEmpty()) {
        addBenefitBalance();
      }
      return getBenefitBalance().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The EOB Business Identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the resource instance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "CodeableConcept", "The category of claim, eg, oral, pharmacy, vision, insitutional, professional.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subType", "CodeableConcept", "A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.", 0, java.lang.Integer.MAX_VALUE, subType));
        childrenList.add(new Property("patient", "Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("billablePeriod", "Period", "The billable period for which charges are being submitted.", 0, java.lang.Integer.MAX_VALUE, billablePeriod));
        childrenList.add(new Property("created", "dateTime", "The date when the EOB was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("enterer", "Reference(Practitioner)", "The person who created the explanation of benefit.", 0, java.lang.Integer.MAX_VALUE, enterer));
        childrenList.add(new Property("insurer", "Reference(Organization)", "The insurer which is responsible for the explanation of benefit.", 0, java.lang.Integer.MAX_VALUE, insurer));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The provider which is responsible for the claim.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The provider which is responsible for the claim.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("referral", "Reference(ReferralRequest)", "The referral resource which lists the date, practitioner, reason and other supporting information.", 0, java.lang.Integer.MAX_VALUE, referral));
        childrenList.add(new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, java.lang.Integer.MAX_VALUE, facility));
        childrenList.add(new Property("claim", "Reference(Claim)", "The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.", 0, java.lang.Integer.MAX_VALUE, claim));
        childrenList.add(new Property("claimResponse", "Reference(ClaimResponse)", "The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.", 0, java.lang.Integer.MAX_VALUE, claimResponse));
        childrenList.add(new Property("outcome", "CodeableConcept", "Processing outcome errror, partial or complete processing.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("related", "", "Other claims which are related to this claim such as prior claim versions or for related services.", 0, java.lang.Integer.MAX_VALUE, related));
        childrenList.add(new Property("prescription", "Reference(MedicationRequest|VisionPrescription)", "Prescription to support the dispensing of Pharmacy or Vision products.", 0, java.lang.Integer.MAX_VALUE, prescription));
        childrenList.add(new Property("originalPrescription", "Reference(MedicationRequest)", "Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products. For example, a physician may prescribe a medication which the pharmacy determines is contraindicated, or for which the patient has an intolerance, and therefor issues a new precription for an alternate medication which has the same theraputic intent. The prescription from the pharmacy becomes the 'prescription' and that from the physician becomes the 'original prescription'.", 0, java.lang.Integer.MAX_VALUE, originalPrescription));
        childrenList.add(new Property("payee", "", "The party to be reimbursed for the services.", 0, java.lang.Integer.MAX_VALUE, payee));
        childrenList.add(new Property("information", "", "Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues. Often there are mutiple jurisdiction specific valuesets which are required.", 0, java.lang.Integer.MAX_VALUE, information));
        childrenList.add(new Property("careTeam", "", "The members of the team who provided the overall service as well as their role and whether responsible and qualifications.", 0, java.lang.Integer.MAX_VALUE, careTeam));
        childrenList.add(new Property("diagnosis", "", "Ordered list of patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        childrenList.add(new Property("procedure", "", "Ordered list of patient procedures performed to support the adjudication.", 0, java.lang.Integer.MAX_VALUE, procedure));
        childrenList.add(new Property("precedence", "positiveInt", "Precedence (primary, secondary, etc.).", 0, java.lang.Integer.MAX_VALUE, precedence));
        childrenList.add(new Property("insurance", "", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, insurance));
        childrenList.add(new Property("accident", "", "An accident which resulted in the need for healthcare services.", 0, java.lang.Integer.MAX_VALUE, accident));
        childrenList.add(new Property("employmentImpacted", "Period", "The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).", 0, java.lang.Integer.MAX_VALUE, employmentImpacted));
        childrenList.add(new Property("hospitalization", "Period", "The start and optional end dates of when the patient was confined to a treatment center.", 0, java.lang.Integer.MAX_VALUE, hospitalization));
        childrenList.add(new Property("item", "", "First tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, item));
        childrenList.add(new Property("addItem", "", "The first tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, addItem));
        childrenList.add(new Property("totalCost", "Money", "The total cost of the services reported.", 0, java.lang.Integer.MAX_VALUE, totalCost));
        childrenList.add(new Property("unallocDeductable", "Money", "The amount of deductable applied which was not allocated to any particular service line.", 0, java.lang.Integer.MAX_VALUE, unallocDeductable));
        childrenList.add(new Property("totalBenefit", "Money", "Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable).", 0, java.lang.Integer.MAX_VALUE, totalBenefit));
        childrenList.add(new Property("payment", "", "Payment details for the claim if the claim has been paid.", 0, java.lang.Integer.MAX_VALUE, payment));
        childrenList.add(new Property("form", "CodeableConcept", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("processNote", "", "Note text.", 0, java.lang.Integer.MAX_VALUE, processNote));
        childrenList.add(new Property("benefitBalance", "", "Balance by Benefit Category.", 0, java.lang.Integer.MAX_VALUE, benefitBalance));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ExplanationOfBenefitStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : this.subType.toArray(new Base[this.subType.size()]); // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -332066046: /*billablePeriod*/ return this.billablePeriod == null ? new Base[0] : new Base[] {this.billablePeriod}; // Period
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -1591951995: /*enterer*/ return this.enterer == null ? new Base[0] : new Base[] {this.enterer}; // Reference
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Reference
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case -722568291: /*referral*/ return this.referral == null ? new Base[0] : new Base[] {this.referral}; // Reference
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Reference
        case 94742588: /*claim*/ return this.claim == null ? new Base[0] : new Base[] {this.claim}; // Reference
        case 689513629: /*claimResponse*/ return this.claimResponse == null ? new Base[0] : new Base[] {this.claimResponse}; // Reference
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // CodeableConcept
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case 1090493483: /*related*/ return this.related == null ? new Base[0] : this.related.toArray(new Base[this.related.size()]); // RelatedClaimComponent
        case 460301338: /*prescription*/ return this.prescription == null ? new Base[0] : new Base[] {this.prescription}; // Reference
        case -1814015861: /*originalPrescription*/ return this.originalPrescription == null ? new Base[0] : new Base[] {this.originalPrescription}; // Reference
        case 106443592: /*payee*/ return this.payee == null ? new Base[0] : new Base[] {this.payee}; // PayeeComponent
        case 1968600364: /*information*/ return this.information == null ? new Base[0] : this.information.toArray(new Base[this.information.size()]); // SupportingInformationComponent
        case -7323378: /*careTeam*/ return this.careTeam == null ? new Base[0] : this.careTeam.toArray(new Base[this.careTeam.size()]); // CareTeamComponent
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : this.diagnosis.toArray(new Base[this.diagnosis.size()]); // DiagnosisComponent
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : this.procedure.toArray(new Base[this.procedure.size()]); // ProcedureComponent
        case 159695370: /*precedence*/ return this.precedence == null ? new Base[0] : new Base[] {this.precedence}; // PositiveIntType
        case 73049818: /*insurance*/ return this.insurance == null ? new Base[0] : new Base[] {this.insurance}; // InsuranceComponent
        case -2143202801: /*accident*/ return this.accident == null ? new Base[0] : new Base[] {this.accident}; // AccidentComponent
        case 1051487345: /*employmentImpacted*/ return this.employmentImpacted == null ? new Base[0] : new Base[] {this.employmentImpacted}; // Period
        case 1057894634: /*hospitalization*/ return this.hospitalization == null ? new Base[0] : new Base[] {this.hospitalization}; // Period
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // ItemComponent
        case -1148899500: /*addItem*/ return this.addItem == null ? new Base[0] : this.addItem.toArray(new Base[this.addItem.size()]); // AddedItemComponent
        case -577782479: /*totalCost*/ return this.totalCost == null ? new Base[0] : new Base[] {this.totalCost}; // Money
        case 2096309753: /*unallocDeductable*/ return this.unallocDeductable == null ? new Base[0] : new Base[] {this.unallocDeductable}; // Money
        case 332332211: /*totalBenefit*/ return this.totalBenefit == null ? new Base[0] : new Base[] {this.totalBenefit}; // Money
        case -786681338: /*payment*/ return this.payment == null ? new Base[0] : new Base[] {this.payment}; // PaymentComponent
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // CodeableConcept
        case 202339073: /*processNote*/ return this.processNote == null ? new Base[0] : this.processNote.toArray(new Base[this.processNote.size()]); // NoteComponent
        case 596003397: /*benefitBalance*/ return this.benefitBalance == null ? new Base[0] : this.benefitBalance.toArray(new Base[this.benefitBalance.size()]); // BenefitBalanceComponent
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
          value = new ExplanationOfBenefitStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ExplanationOfBenefitStatus>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868521062: // subType
          this.getSubType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case -332066046: // billablePeriod
          this.billablePeriod = castToPeriod(value); // Period
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case -1591951995: // enterer
          this.enterer = castToReference(value); // Reference
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
        case -722568291: // referral
          this.referral = castToReference(value); // Reference
          return value;
        case 501116579: // facility
          this.facility = castToReference(value); // Reference
          return value;
        case 94742588: // claim
          this.claim = castToReference(value); // Reference
          return value;
        case 689513629: // claimResponse
          this.claimResponse = castToReference(value); // Reference
          return value;
        case -1106507950: // outcome
          this.outcome = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
          return value;
        case 1090493483: // related
          this.getRelated().add((RelatedClaimComponent) value); // RelatedClaimComponent
          return value;
        case 460301338: // prescription
          this.prescription = castToReference(value); // Reference
          return value;
        case -1814015861: // originalPrescription
          this.originalPrescription = castToReference(value); // Reference
          return value;
        case 106443592: // payee
          this.payee = (PayeeComponent) value; // PayeeComponent
          return value;
        case 1968600364: // information
          this.getInformation().add((SupportingInformationComponent) value); // SupportingInformationComponent
          return value;
        case -7323378: // careTeam
          this.getCareTeam().add((CareTeamComponent) value); // CareTeamComponent
          return value;
        case 1196993265: // diagnosis
          this.getDiagnosis().add((DiagnosisComponent) value); // DiagnosisComponent
          return value;
        case -1095204141: // procedure
          this.getProcedure().add((ProcedureComponent) value); // ProcedureComponent
          return value;
        case 159695370: // precedence
          this.precedence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 73049818: // insurance
          this.insurance = (InsuranceComponent) value; // InsuranceComponent
          return value;
        case -2143202801: // accident
          this.accident = (AccidentComponent) value; // AccidentComponent
          return value;
        case 1051487345: // employmentImpacted
          this.employmentImpacted = castToPeriod(value); // Period
          return value;
        case 1057894634: // hospitalization
          this.hospitalization = castToPeriod(value); // Period
          return value;
        case 3242771: // item
          this.getItem().add((ItemComponent) value); // ItemComponent
          return value;
        case -1148899500: // addItem
          this.getAddItem().add((AddedItemComponent) value); // AddedItemComponent
          return value;
        case -577782479: // totalCost
          this.totalCost = castToMoney(value); // Money
          return value;
        case 2096309753: // unallocDeductable
          this.unallocDeductable = castToMoney(value); // Money
          return value;
        case 332332211: // totalBenefit
          this.totalBenefit = castToMoney(value); // Money
          return value;
        case -786681338: // payment
          this.payment = (PaymentComponent) value; // PaymentComponent
          return value;
        case 3148996: // form
          this.form = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 202339073: // processNote
          this.getProcessNote().add((NoteComponent) value); // NoteComponent
          return value;
        case 596003397: // benefitBalance
          this.getBenefitBalance().add((BenefitBalanceComponent) value); // BenefitBalanceComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ExplanationOfBenefitStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ExplanationOfBenefitStatus>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subType")) {
          this.getSubType().add(castToCodeableConcept(value));
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("billablePeriod")) {
          this.billablePeriod = castToPeriod(value); // Period
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("enterer")) {
          this.enterer = castToReference(value); // Reference
        } else if (name.equals("insurer")) {
          this.insurer = castToReference(value); // Reference
        } else if (name.equals("provider")) {
          this.provider = castToReference(value); // Reference
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("referral")) {
          this.referral = castToReference(value); // Reference
        } else if (name.equals("facility")) {
          this.facility = castToReference(value); // Reference
        } else if (name.equals("claim")) {
          this.claim = castToReference(value); // Reference
        } else if (name.equals("claimResponse")) {
          this.claimResponse = castToReference(value); // Reference
        } else if (name.equals("outcome")) {
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("disposition")) {
          this.disposition = castToString(value); // StringType
        } else if (name.equals("related")) {
          this.getRelated().add((RelatedClaimComponent) value);
        } else if (name.equals("prescription")) {
          this.prescription = castToReference(value); // Reference
        } else if (name.equals("originalPrescription")) {
          this.originalPrescription = castToReference(value); // Reference
        } else if (name.equals("payee")) {
          this.payee = (PayeeComponent) value; // PayeeComponent
        } else if (name.equals("information")) {
          this.getInformation().add((SupportingInformationComponent) value);
        } else if (name.equals("careTeam")) {
          this.getCareTeam().add((CareTeamComponent) value);
        } else if (name.equals("diagnosis")) {
          this.getDiagnosis().add((DiagnosisComponent) value);
        } else if (name.equals("procedure")) {
          this.getProcedure().add((ProcedureComponent) value);
        } else if (name.equals("precedence")) {
          this.precedence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("insurance")) {
          this.insurance = (InsuranceComponent) value; // InsuranceComponent
        } else if (name.equals("accident")) {
          this.accident = (AccidentComponent) value; // AccidentComponent
        } else if (name.equals("employmentImpacted")) {
          this.employmentImpacted = castToPeriod(value); // Period
        } else if (name.equals("hospitalization")) {
          this.hospitalization = castToPeriod(value); // Period
        } else if (name.equals("item")) {
          this.getItem().add((ItemComponent) value);
        } else if (name.equals("addItem")) {
          this.getAddItem().add((AddedItemComponent) value);
        } else if (name.equals("totalCost")) {
          this.totalCost = castToMoney(value); // Money
        } else if (name.equals("unallocDeductable")) {
          this.unallocDeductable = castToMoney(value); // Money
        } else if (name.equals("totalBenefit")) {
          this.totalBenefit = castToMoney(value); // Money
        } else if (name.equals("payment")) {
          this.payment = (PaymentComponent) value; // PaymentComponent
        } else if (name.equals("form")) {
          this.form = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("processNote")) {
          this.getProcessNote().add((NoteComponent) value);
        } else if (name.equals("benefitBalance")) {
          this.getBenefitBalance().add((BenefitBalanceComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 3575610:  return getType(); 
        case -1868521062:  return addSubType(); 
        case -791418107:  return getPatient(); 
        case -332066046:  return getBillablePeriod(); 
        case 1028554472:  return getCreatedElement();
        case -1591951995:  return getEnterer(); 
        case 1957615864:  return getInsurer(); 
        case -987494927:  return getProvider(); 
        case 1178922291:  return getOrganization(); 
        case -722568291:  return getReferral(); 
        case 501116579:  return getFacility(); 
        case 94742588:  return getClaim(); 
        case 689513629:  return getClaimResponse(); 
        case -1106507950:  return getOutcome(); 
        case 583380919:  return getDispositionElement();
        case 1090493483:  return addRelated(); 
        case 460301338:  return getPrescription(); 
        case -1814015861:  return getOriginalPrescription(); 
        case 106443592:  return getPayee(); 
        case 1968600364:  return addInformation(); 
        case -7323378:  return addCareTeam(); 
        case 1196993265:  return addDiagnosis(); 
        case -1095204141:  return addProcedure(); 
        case 159695370:  return getPrecedenceElement();
        case 73049818:  return getInsurance(); 
        case -2143202801:  return getAccident(); 
        case 1051487345:  return getEmploymentImpacted(); 
        case 1057894634:  return getHospitalization(); 
        case 3242771:  return addItem(); 
        case -1148899500:  return addAddItem(); 
        case -577782479:  return getTotalCost(); 
        case 2096309753:  return getUnallocDeductable(); 
        case 332332211:  return getTotalBenefit(); 
        case -786681338:  return getPayment(); 
        case 3148996:  return getForm(); 
        case 202339073:  return addProcessNote(); 
        case 596003397:  return addBenefitBalance(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1868521062: /*subType*/ return new String[] {"CodeableConcept"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case -332066046: /*billablePeriod*/ return new String[] {"Period"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case -1591951995: /*enterer*/ return new String[] {"Reference"};
        case 1957615864: /*insurer*/ return new String[] {"Reference"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case -722568291: /*referral*/ return new String[] {"Reference"};
        case 501116579: /*facility*/ return new String[] {"Reference"};
        case 94742588: /*claim*/ return new String[] {"Reference"};
        case 689513629: /*claimResponse*/ return new String[] {"Reference"};
        case -1106507950: /*outcome*/ return new String[] {"CodeableConcept"};
        case 583380919: /*disposition*/ return new String[] {"string"};
        case 1090493483: /*related*/ return new String[] {};
        case 460301338: /*prescription*/ return new String[] {"Reference"};
        case -1814015861: /*originalPrescription*/ return new String[] {"Reference"};
        case 106443592: /*payee*/ return new String[] {};
        case 1968600364: /*information*/ return new String[] {};
        case -7323378: /*careTeam*/ return new String[] {};
        case 1196993265: /*diagnosis*/ return new String[] {};
        case -1095204141: /*procedure*/ return new String[] {};
        case 159695370: /*precedence*/ return new String[] {"positiveInt"};
        case 73049818: /*insurance*/ return new String[] {};
        case -2143202801: /*accident*/ return new String[] {};
        case 1051487345: /*employmentImpacted*/ return new String[] {"Period"};
        case 1057894634: /*hospitalization*/ return new String[] {"Period"};
        case 3242771: /*item*/ return new String[] {};
        case -1148899500: /*addItem*/ return new String[] {};
        case -577782479: /*totalCost*/ return new String[] {"Money"};
        case 2096309753: /*unallocDeductable*/ return new String[] {"Money"};
        case 332332211: /*totalBenefit*/ return new String[] {"Money"};
        case -786681338: /*payment*/ return new String[] {};
        case 3148996: /*form*/ return new String[] {"CodeableConcept"};
        case 202339073: /*processNote*/ return new String[] {};
        case 596003397: /*benefitBalance*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.status");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          return addSubType();
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("billablePeriod")) {
          this.billablePeriod = new Period();
          return this.billablePeriod;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.created");
        }
        else if (name.equals("enterer")) {
          this.enterer = new Reference();
          return this.enterer;
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
        else if (name.equals("referral")) {
          this.referral = new Reference();
          return this.referral;
        }
        else if (name.equals("facility")) {
          this.facility = new Reference();
          return this.facility;
        }
        else if (name.equals("claim")) {
          this.claim = new Reference();
          return this.claim;
        }
        else if (name.equals("claimResponse")) {
          this.claimResponse = new Reference();
          return this.claimResponse;
        }
        else if (name.equals("outcome")) {
          this.outcome = new CodeableConcept();
          return this.outcome;
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.disposition");
        }
        else if (name.equals("related")) {
          return addRelated();
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
        else if (name.equals("information")) {
          return addInformation();
        }
        else if (name.equals("careTeam")) {
          return addCareTeam();
        }
        else if (name.equals("diagnosis")) {
          return addDiagnosis();
        }
        else if (name.equals("procedure")) {
          return addProcedure();
        }
        else if (name.equals("precedence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.precedence");
        }
        else if (name.equals("insurance")) {
          this.insurance = new InsuranceComponent();
          return this.insurance;
        }
        else if (name.equals("accident")) {
          this.accident = new AccidentComponent();
          return this.accident;
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
        else if (name.equals("totalCost")) {
          this.totalCost = new Money();
          return this.totalCost;
        }
        else if (name.equals("unallocDeductable")) {
          this.unallocDeductable = new Money();
          return this.unallocDeductable;
        }
        else if (name.equals("totalBenefit")) {
          this.totalBenefit = new Money();
          return this.totalBenefit;
        }
        else if (name.equals("payment")) {
          this.payment = new PaymentComponent();
          return this.payment;
        }
        else if (name.equals("form")) {
          this.form = new CodeableConcept();
          return this.form;
        }
        else if (name.equals("processNote")) {
          return addProcessNote();
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
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        if (subType != null) {
          dst.subType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subType)
            dst.subType.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.billablePeriod = billablePeriod == null ? null : billablePeriod.copy();
        dst.created = created == null ? null : created.copy();
        dst.enterer = enterer == null ? null : enterer.copy();
        dst.insurer = insurer == null ? null : insurer.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.referral = referral == null ? null : referral.copy();
        dst.facility = facility == null ? null : facility.copy();
        dst.claim = claim == null ? null : claim.copy();
        dst.claimResponse = claimResponse == null ? null : claimResponse.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        if (related != null) {
          dst.related = new ArrayList<RelatedClaimComponent>();
          for (RelatedClaimComponent i : related)
            dst.related.add(i.copy());
        };
        dst.prescription = prescription == null ? null : prescription.copy();
        dst.originalPrescription = originalPrescription == null ? null : originalPrescription.copy();
        dst.payee = payee == null ? null : payee.copy();
        if (information != null) {
          dst.information = new ArrayList<SupportingInformationComponent>();
          for (SupportingInformationComponent i : information)
            dst.information.add(i.copy());
        };
        if (careTeam != null) {
          dst.careTeam = new ArrayList<CareTeamComponent>();
          for (CareTeamComponent i : careTeam)
            dst.careTeam.add(i.copy());
        };
        if (diagnosis != null) {
          dst.diagnosis = new ArrayList<DiagnosisComponent>();
          for (DiagnosisComponent i : diagnosis)
            dst.diagnosis.add(i.copy());
        };
        if (procedure != null) {
          dst.procedure = new ArrayList<ProcedureComponent>();
          for (ProcedureComponent i : procedure)
            dst.procedure.add(i.copy());
        };
        dst.precedence = precedence == null ? null : precedence.copy();
        dst.insurance = insurance == null ? null : insurance.copy();
        dst.accident = accident == null ? null : accident.copy();
        dst.employmentImpacted = employmentImpacted == null ? null : employmentImpacted.copy();
        dst.hospitalization = hospitalization == null ? null : hospitalization.copy();
        if (item != null) {
          dst.item = new ArrayList<ItemComponent>();
          for (ItemComponent i : item)
            dst.item.add(i.copy());
        };
        if (addItem != null) {
          dst.addItem = new ArrayList<AddedItemComponent>();
          for (AddedItemComponent i : addItem)
            dst.addItem.add(i.copy());
        };
        dst.totalCost = totalCost == null ? null : totalCost.copy();
        dst.unallocDeductable = unallocDeductable == null ? null : unallocDeductable.copy();
        dst.totalBenefit = totalBenefit == null ? null : totalBenefit.copy();
        dst.payment = payment == null ? null : payment.copy();
        dst.form = form == null ? null : form.copy();
        if (processNote != null) {
          dst.processNote = new ArrayList<NoteComponent>();
          for (NoteComponent i : processNote)
            dst.processNote.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(subType, o.subType, true) && compareDeep(patient, o.patient, true) && compareDeep(billablePeriod, o.billablePeriod, true)
           && compareDeep(created, o.created, true) && compareDeep(enterer, o.enterer, true) && compareDeep(insurer, o.insurer, true)
           && compareDeep(provider, o.provider, true) && compareDeep(organization, o.organization, true) && compareDeep(referral, o.referral, true)
           && compareDeep(facility, o.facility, true) && compareDeep(claim, o.claim, true) && compareDeep(claimResponse, o.claimResponse, true)
           && compareDeep(outcome, o.outcome, true) && compareDeep(disposition, o.disposition, true) && compareDeep(related, o.related, true)
           && compareDeep(prescription, o.prescription, true) && compareDeep(originalPrescription, o.originalPrescription, true)
           && compareDeep(payee, o.payee, true) && compareDeep(information, o.information, true) && compareDeep(careTeam, o.careTeam, true)
           && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(procedure, o.procedure, true) && compareDeep(precedence, o.precedence, true)
           && compareDeep(insurance, o.insurance, true) && compareDeep(accident, o.accident, true) && compareDeep(employmentImpacted, o.employmentImpacted, true)
           && compareDeep(hospitalization, o.hospitalization, true) && compareDeep(item, o.item, true) && compareDeep(addItem, o.addItem, true)
           && compareDeep(totalCost, o.totalCost, true) && compareDeep(unallocDeductable, o.unallocDeductable, true)
           && compareDeep(totalBenefit, o.totalBenefit, true) && compareDeep(payment, o.payment, true) && compareDeep(form, o.form, true)
           && compareDeep(processNote, o.processNote, true) && compareDeep(benefitBalance, o.benefitBalance, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExplanationOfBenefit))
          return false;
        ExplanationOfBenefit o = (ExplanationOfBenefit) other;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true) && compareValues(disposition, o.disposition, true)
           && compareValues(precedence, o.precedence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, type
          , subType, patient, billablePeriod, created, enterer, insurer, provider, organization
          , referral, facility, claim, claimResponse, outcome, disposition, related, prescription
          , originalPrescription, payee, information, careTeam, diagnosis, procedure, precedence
          , insurance, accident, employmentImpacted, hospitalization, item, addItem, totalCost
          , unallocDeductable, totalBenefit, payment, form, processNote, benefitBalance);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ExplanationOfBenefit;
   }

 /**
   * Search parameter: <b>coverage</b>
   * <p>
   * Description: <b>The plan under which the claim was adjudicated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.insurance.coverage</b><br>
   * </p>
   */
  @SearchParamDefinition(name="coverage", path="ExplanationOfBenefit.insurance.coverage", description="The plan under which the claim was adjudicated", type="reference", target={Coverage.class } )
  public static final String SP_COVERAGE = "coverage";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>coverage</b>
   * <p>
   * Description: <b>The plan under which the claim was adjudicated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.insurance.coverage</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam COVERAGE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_COVERAGE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:coverage</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_COVERAGE = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:coverage").toLocked();

 /**
   * Search parameter: <b>care-team</b>
   * <p>
   * Description: <b>Member of the CareTeam</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.careTeam.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="care-team", path="ExplanationOfBenefit.careTeam.provider", description="Member of the CareTeam", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class } )
  public static final String SP_CARE_TEAM = "care-team";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>care-team</b>
   * <p>
   * Description: <b>Member of the CareTeam</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.careTeam.provider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CARE_TEAM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CARE_TEAM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:care-team</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CARE_TEAM = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:care-team").toLocked();

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
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Encounters associated with a billed line item</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.item.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="ExplanationOfBenefit.item.encounter", description="Encounters associated with a billed line item", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounters associated with a billed line item</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.item.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:encounter").toLocked();

 /**
   * Search parameter: <b>payee</b>
   * <p>
   * Description: <b>The party receiving any payment for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.payee.party</b><br>
   * </p>
   */
  @SearchParamDefinition(name="payee", path="ExplanationOfBenefit.payee.party", description="The party receiving any payment for the Claim", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PAYEE = "payee";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>payee</b>
   * <p>
   * Description: <b>The party receiving any payment for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.payee.party</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PAYEE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PAYEE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:payee</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PAYEE = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:payee").toLocked();

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
  @SearchParamDefinition(name="provider", path="ExplanationOfBenefit.provider", description="The reference to the provider", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
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
  @SearchParamDefinition(name="patient", path="ExplanationOfBenefit.patient", description="The reference to the patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
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
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="ExplanationOfBenefit.organization", description="The reference to the providing organization", type="reference", target={Organization.class } )
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
  @SearchParamDefinition(name="claim", path="ExplanationOfBenefit.claim", description="The reference to the claim", type="reference", target={Claim.class } )
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
   * Search parameter: <b>enterer</b>
   * <p>
   * Description: <b>The party responsible for the entry of the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.enterer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="enterer", path="ExplanationOfBenefit.enterer", description="The party responsible for the entry of the Claim", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_ENTERER = "enterer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>enterer</b>
   * <p>
   * Description: <b>The party responsible for the entry of the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.enterer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENTERER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENTERER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:enterer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENTERER = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:enterer").toLocked();

 /**
   * Search parameter: <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.facility</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facility", path="ExplanationOfBenefit.facility", description="Facility responsible for the goods and services", type="reference", target={Location.class } )
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

