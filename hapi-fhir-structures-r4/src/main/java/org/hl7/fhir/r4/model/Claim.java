package org.hl7.fhir.r4.model;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A provider issued list of professional services and products which have been provided, or are to be provided, to a patient which is sent to an insurer for reimbursement.
 */
@ResourceDef(name="Claim", profile="http://hl7.org/fhir/StructureDefinition/Claim")
public class Claim extends DomainResource {

    public enum ClaimStatus {
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
        public static ClaimStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown ClaimStatus code '"+codeString+"'");
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

  public static class ClaimStatusEnumFactory implements EnumFactory<ClaimStatus> {
    public ClaimStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ClaimStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return ClaimStatus.CANCELLED;
        if ("draft".equals(codeString))
          return ClaimStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return ClaimStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ClaimStatus code '"+codeString+"'");
        }
        public Enumeration<ClaimStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ClaimStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<ClaimStatus>(this, ClaimStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<ClaimStatus>(this, ClaimStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<ClaimStatus>(this, ClaimStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ClaimStatus>(this, ClaimStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ClaimStatus code '"+codeString+"'");
        }
    public String toCode(ClaimStatus code) {
      if (code == ClaimStatus.ACTIVE)
        return "active";
      if (code == ClaimStatus.CANCELLED)
        return "cancelled";
      if (code == ClaimStatus.DRAFT)
        return "draft";
      if (code == ClaimStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ClaimStatus code) {
      return code.getSystem();
      }
    }

    public enum Use {
        /**
         * The treatment is complete and this represents a Claim for the services.
         */
        CLAIM, 
        /**
         * The treatment is proposed and this represents a Pre-authorization for the services.
         */
        PREAUTHORIZATION, 
        /**
         * The treatment is proposed and this represents a Pre-determination for the services.
         */
        PREDETERMINATION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static Use fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("claim".equals(codeString))
          return CLAIM;
        if ("preauthorization".equals(codeString))
          return PREAUTHORIZATION;
        if ("predetermination".equals(codeString))
          return PREDETERMINATION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown Use code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CLAIM: return "claim";
            case PREAUTHORIZATION: return "preauthorization";
            case PREDETERMINATION: return "predetermination";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CLAIM: return "http://hl7.org/fhir/claim-use";
            case PREAUTHORIZATION: return "http://hl7.org/fhir/claim-use";
            case PREDETERMINATION: return "http://hl7.org/fhir/claim-use";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CLAIM: return "The treatment is complete and this represents a Claim for the services.";
            case PREAUTHORIZATION: return "The treatment is proposed and this represents a Pre-authorization for the services.";
            case PREDETERMINATION: return "The treatment is proposed and this represents a Pre-determination for the services.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CLAIM: return "Claim";
            case PREAUTHORIZATION: return "Preauthorization";
            case PREDETERMINATION: return "Predetermination";
            default: return "?";
          }
        }
    }

  public static class UseEnumFactory implements EnumFactory<Use> {
    public Use fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("claim".equals(codeString))
          return Use.CLAIM;
        if ("preauthorization".equals(codeString))
          return Use.PREAUTHORIZATION;
        if ("predetermination".equals(codeString))
          return Use.PREDETERMINATION;
        throw new IllegalArgumentException("Unknown Use code '"+codeString+"'");
        }
        public Enumeration<Use> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<Use>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("claim".equals(codeString))
          return new Enumeration<Use>(this, Use.CLAIM);
        if ("preauthorization".equals(codeString))
          return new Enumeration<Use>(this, Use.PREAUTHORIZATION);
        if ("predetermination".equals(codeString))
          return new Enumeration<Use>(this, Use.PREDETERMINATION);
        throw new FHIRException("Unknown Use code '"+codeString+"'");
        }
    public String toCode(Use code) {
      if (code == Use.CLAIM)
        return "claim";
      if (code == Use.PREAUTHORIZATION)
        return "preauthorization";
      if (code == Use.PREDETERMINATION)
        return "predetermination";
      return "?";
      }
    public String toSystem(Use code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class RelatedClaimComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reference to a related claim.
         */
        @Child(name = "claim", type = {Claim.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference to the related claim", formalDefinition="Reference to a related claim." )
        protected Reference claim;

        /**
         * The actual object that is the target of the reference (Reference to a related claim.)
         */
        protected Claim claimTarget;

        /**
         * A code to convey how the claims are related.
         */
        @Child(name = "relationship", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How the reference claim is related", formalDefinition="A code to convey how the claims are related." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/related-claim-relationship")
        protected CodeableConcept relationship;

        /**
         * An alternate organizational reference to the case or file to which this particular claim pertains.
         */
        @Child(name = "reference", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="File or case reference", formalDefinition="An alternate organizational reference to the case or file to which this particular claim pertains." )
        protected Identifier reference;

        private static final long serialVersionUID = -379338905L;

    /**
     * Constructor
     */
      public RelatedClaimComponent() {
        super();
      }

        /**
         * @return {@link #claim} (Reference to a related claim.)
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
         * @param value {@link #claim} (Reference to a related claim.)
         */
        public RelatedClaimComponent setClaim(Reference value) { 
          this.claim = value;
          return this;
        }

        /**
         * @return {@link #claim} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to a related claim.)
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
         * @param value {@link #claim} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to a related claim.)
         */
        public RelatedClaimComponent setClaimTarget(Claim value) { 
          this.claimTarget = value;
          return this;
        }

        /**
         * @return {@link #relationship} (A code to convey how the claims are related.)
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
         * @param value {@link #relationship} (A code to convey how the claims are related.)
         */
        public RelatedClaimComponent setRelationship(CodeableConcept value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return {@link #reference} (An alternate organizational reference to the case or file to which this particular claim pertains.)
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
         * @param value {@link #reference} (An alternate organizational reference to the case or file to which this particular claim pertains.)
         */
        public RelatedClaimComponent setReference(Identifier value) { 
          this.reference = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("claim", "Reference(Claim)", "Reference to a related claim.", 0, 1, claim));
          children.add(new Property("relationship", "CodeableConcept", "A code to convey how the claims are related.", 0, 1, relationship));
          children.add(new Property("reference", "Identifier", "An alternate organizational reference to the case or file to which this particular claim pertains.", 0, 1, reference));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 94742588: /*claim*/  return new Property("claim", "Reference(Claim)", "Reference to a related claim.", 0, 1, claim);
          case -261851592: /*relationship*/  return new Property("relationship", "CodeableConcept", "A code to convey how the claims are related.", 0, 1, relationship);
          case -925155509: /*reference*/  return new Property("reference", "Identifier", "An alternate organizational reference to the case or file to which this particular claim pertains.", 0, 1, reference);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof RelatedClaimComponent))
          return false;
        RelatedClaimComponent o = (RelatedClaimComponent) other_;
        return compareDeep(claim, o.claim, true) && compareDeep(relationship, o.relationship, true) && compareDeep(reference, o.reference, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof RelatedClaimComponent))
          return false;
        RelatedClaimComponent o = (RelatedClaimComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(claim, relationship, reference
          );
      }

  public String fhirType() {
    return "Claim.related";

  }

  }

    @Block()
    public static class PayeeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of Party to be reimbursed: subscriber, provider, other.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Category of recipient", formalDefinition="Type of Party to be reimbursed: subscriber, provider, other." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payeetype")
        protected CodeableConcept type;

        /**
         * Reference to the individual or organization to whom any payment will be made.
         */
        @Child(name = "party", type = {Practitioner.class, PractitionerRole.class, Organization.class, Patient.class, RelatedPerson.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Recipient reference", formalDefinition="Reference to the individual or organization to whom any payment will be made." )
        protected Reference party;

        /**
         * The actual object that is the target of the reference (Reference to the individual or organization to whom any payment will be made.)
         */
        protected Resource partyTarget;

        private static final long serialVersionUID = 1609484699L;

    /**
     * Constructor
     */
      public PayeeComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PayeeComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Type of Party to be reimbursed: subscriber, provider, other.)
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
         * @param value {@link #type} (Type of Party to be reimbursed: subscriber, provider, other.)
         */
        public PayeeComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #party} (Reference to the individual or organization to whom any payment will be made.)
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
         * @param value {@link #party} (Reference to the individual or organization to whom any payment will be made.)
         */
        public PayeeComponent setParty(Reference value) { 
          this.party = value;
          return this;
        }

        /**
         * @return {@link #party} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the individual or organization to whom any payment will be made.)
         */
        public Resource getPartyTarget() { 
          return this.partyTarget;
        }

        /**
         * @param value {@link #party} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the individual or organization to whom any payment will be made.)
         */
        public PayeeComponent setPartyTarget(Resource value) { 
          this.partyTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Type of Party to be reimbursed: subscriber, provider, other.", 0, 1, type));
          children.add(new Property("party", "Reference(Practitioner|PractitionerRole|Organization|Patient|RelatedPerson)", "Reference to the individual or organization to whom any payment will be made.", 0, 1, party));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of Party to be reimbursed: subscriber, provider, other.", 0, 1, type);
          case 106437350: /*party*/  return new Property("party", "Reference(Practitioner|PractitionerRole|Organization|Patient|RelatedPerson)", "Reference to the individual or organization to whom any payment will be made.", 0, 1, party);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
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
        case 106437350:  return getParty(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
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
        dst.party = party == null ? null : party.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof PayeeComponent))
          return false;
        PayeeComponent o = (PayeeComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(party, o.party, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof PayeeComponent))
          return false;
        PayeeComponent o = (PayeeComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, party);
      }

  public String fhirType() {
    return "Claim.payee";

  }

  }

    @Block()
    public static class CareTeamComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify care team entries.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Order of care team", formalDefinition="A number to uniquely identify care team entries." )
        protected PositiveIntType sequence;

        /**
         * Member of the team who provided the product or service.
         */
        @Child(name = "provider", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Practitioner or organization", formalDefinition="Member of the team who provided the product or service." )
        protected Reference provider;

        /**
         * The actual object that is the target of the reference (Member of the team who provided the product or service.)
         */
        protected Resource providerTarget;

        /**
         * The party who is billing and/or responsible for the claimed products or services.
         */
        @Child(name = "responsible", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Indicator of the lead practitioner", formalDefinition="The party who is billing and/or responsible for the claimed products or services." )
        protected BooleanType responsible;

        /**
         * The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Function within the team", formalDefinition="The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-careteamrole")
        protected CodeableConcept role;

        /**
         * The qualification of the practitioner which is applicable for this service.
         */
        @Child(name = "qualification", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Practitioner credential or specialization", formalDefinition="The qualification of the practitioner which is applicable for this service." )
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
         * @return {@link #sequence} (A number to uniquely identify care team entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
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
         * @param value {@link #sequence} (A number to uniquely identify care team entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public CareTeamComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify care team entries.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify care team entries.
         */
        public CareTeamComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #provider} (Member of the team who provided the product or service.)
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
         * @param value {@link #provider} (Member of the team who provided the product or service.)
         */
        public CareTeamComponent setProvider(Reference value) { 
          this.provider = value;
          return this;
        }

        /**
         * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Member of the team who provided the product or service.)
         */
        public Resource getProviderTarget() { 
          return this.providerTarget;
        }

        /**
         * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Member of the team who provided the product or service.)
         */
        public CareTeamComponent setProviderTarget(Resource value) { 
          this.providerTarget = value;
          return this;
        }

        /**
         * @return {@link #responsible} (The party who is billing and/or responsible for the claimed products or services.). This is the underlying object with id, value and extensions. The accessor "getResponsible" gives direct access to the value
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
         * @param value {@link #responsible} (The party who is billing and/or responsible for the claimed products or services.). This is the underlying object with id, value and extensions. The accessor "getResponsible" gives direct access to the value
         */
        public CareTeamComponent setResponsibleElement(BooleanType value) { 
          this.responsible = value;
          return this;
        }

        /**
         * @return The party who is billing and/or responsible for the claimed products or services.
         */
        public boolean getResponsible() { 
          return this.responsible == null || this.responsible.isEmpty() ? false : this.responsible.getValue();
        }

        /**
         * @param value The party who is billing and/or responsible for the claimed products or services.
         */
        public CareTeamComponent setResponsible(boolean value) { 
            if (this.responsible == null)
              this.responsible = new BooleanType();
            this.responsible.setValue(value);
          return this;
        }

        /**
         * @return {@link #role} (The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team.)
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
         * @param value {@link #role} (The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team.)
         */
        public CareTeamComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #qualification} (The qualification of the practitioner which is applicable for this service.)
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
         * @param value {@link #qualification} (The qualification of the practitioner which is applicable for this service.)
         */
        public CareTeamComponent setQualification(CodeableConcept value) { 
          this.qualification = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify care team entries.", 0, 1, sequence));
          children.add(new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "Member of the team who provided the product or service.", 0, 1, provider));
          children.add(new Property("responsible", "boolean", "The party who is billing and/or responsible for the claimed products or services.", 0, 1, responsible));
          children.add(new Property("role", "CodeableConcept", "The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team.", 0, 1, role));
          children.add(new Property("qualification", "CodeableConcept", "The qualification of the practitioner which is applicable for this service.", 0, 1, qualification));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify care team entries.", 0, 1, sequence);
          case -987494927: /*provider*/  return new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "Member of the team who provided the product or service.", 0, 1, provider);
          case 1847674614: /*responsible*/  return new Property("responsible", "boolean", "The party who is billing and/or responsible for the claimed products or services.", 0, 1, responsible);
          case 3506294: /*role*/  return new Property("role", "CodeableConcept", "The lead, assisting or supervising practitioner and their discipline if a multidisciplinary team.", 0, 1, role);
          case -631333393: /*qualification*/  return new Property("qualification", "CodeableConcept", "The qualification of the practitioner which is applicable for this service.", 0, 1, qualification);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("responsible")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.responsible");
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof CareTeamComponent))
          return false;
        CareTeamComponent o = (CareTeamComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(provider, o.provider, true) && compareDeep(responsible, o.responsible, true)
           && compareDeep(role, o.role, true) && compareDeep(qualification, o.qualification, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof CareTeamComponent))
          return false;
        CareTeamComponent o = (CareTeamComponent) other_;
        return compareValues(sequence, o.sequence, true) && compareValues(responsible, o.responsible, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, provider, responsible
          , role, qualification);
      }

  public String fhirType() {
    return "Claim.careTeam";

  }

  }

    @Block()
    public static class SupportingInformationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify supporting information entries.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Information instance identifier", formalDefinition="A number to uniquely identify supporting information entries." )
        protected PositiveIntType sequence;

        /**
         * The general class of the information supplied: information; exception; accident, employment; onset, etc.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Classification of the supplied information", formalDefinition="The general class of the information supplied: information; exception; accident, employment; onset, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-informationcategory")
        protected CodeableConcept category;

        /**
         * System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of information", formalDefinition="System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought." )
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
        @Child(name = "value", type = {BooleanType.class, StringType.class, Quantity.class, Attachment.class, Reference.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Data to be provided", formalDefinition="Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data." )
        protected Type value;

        /**
         * Provides the reason in the situation where a reason code is required in addition to the content.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Explanation for the information", formalDefinition="Provides the reason in the situation where a reason code is required in addition to the content." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/missing-tooth-reason")
        protected CodeableConcept reason;

        private static final long serialVersionUID = -518630232L;

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
         * @return {@link #sequence} (A number to uniquely identify supporting information entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
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
         * @param value {@link #sequence} (A number to uniquely identify supporting information entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public SupportingInformationComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify supporting information entries.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify supporting information entries.
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
         * @return {@link #code} (System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought.)
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
         * @param value {@link #code} (System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought.)
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
          if (this.timing == null)
            this.timing = new DateType();
          if (!(this.timing instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (DateType) this.timing;
        }

        public boolean hasTimingDateType() { 
          return this != null && this.timing instanceof DateType;
        }

        /**
         * @return {@link #timing} (The date when or period to which this information refers.)
         */
        public Period getTimingPeriod() throws FHIRException { 
          if (this.timing == null)
            this.timing = new Period();
          if (!(this.timing instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.timing.getClass().getName()+" was encountered");
          return (Period) this.timing;
        }

        public boolean hasTimingPeriod() { 
          return this != null && this.timing instanceof Period;
        }

        public boolean hasTiming() { 
          return this.timing != null && !this.timing.isEmpty();
        }

        /**
         * @param value {@link #timing} (The date when or period to which this information refers.)
         */
        public SupportingInformationComponent setTiming(Type value) { 
          if (value != null && !(value instanceof DateType || value instanceof Period))
            throw new Error("Not the right type for Claim.supportingInfo.timing[x]: "+value.fhirType());
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
        public BooleanType getValueBooleanType() throws FHIRException { 
          if (this.value == null)
            this.value = new BooleanType();
          if (!(this.value instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        public boolean hasValueBooleanType() { 
          return this != null && this.value instanceof BooleanType;
        }

        /**
         * @return {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (this.value == null)
            this.value = new StringType();
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this != null && this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public Quantity getValueQuantity() throws FHIRException { 
          if (this.value == null)
            this.value = new Quantity();
          if (!(this.value instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        public boolean hasValueQuantity() { 
          return this != null && this.value instanceof Quantity;
        }

        /**
         * @return {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public Attachment getValueAttachment() throws FHIRException { 
          if (this.value == null)
            this.value = new Attachment();
          if (!(this.value instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Attachment) this.value;
        }

        public boolean hasValueAttachment() { 
          return this != null && this.value instanceof Attachment;
        }

        /**
         * @return {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public Reference getValueReference() throws FHIRException { 
          if (this.value == null)
            this.value = new Reference();
          if (!(this.value instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Reference) this.value;
        }

        public boolean hasValueReference() { 
          return this != null && this.value instanceof Reference;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public SupportingInformationComponent setValue(Type value) { 
          if (value != null && !(value instanceof BooleanType || value instanceof StringType || value instanceof Quantity || value instanceof Attachment || value instanceof Reference))
            throw new Error("Not the right type for Claim.supportingInfo.value[x]: "+value.fhirType());
          this.value = value;
          return this;
        }

        /**
         * @return {@link #reason} (Provides the reason in the situation where a reason code is required in addition to the content.)
         */
        public CodeableConcept getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingInformationComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new CodeableConcept(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Provides the reason in the situation where a reason code is required in addition to the content.)
         */
        public SupportingInformationComponent setReason(CodeableConcept value) { 
          this.reason = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify supporting information entries.", 0, 1, sequence));
          children.add(new Property("category", "CodeableConcept", "The general class of the information supplied: information; exception; accident, employment; onset, etc.", 0, 1, category));
          children.add(new Property("code", "CodeableConcept", "System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought.", 0, 1, code));
          children.add(new Property("timing[x]", "date|Period", "The date when or period to which this information refers.", 0, 1, timing));
          children.add(new Property("value[x]", "boolean|string|Quantity|Attachment|Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, value));
          children.add(new Property("reason", "CodeableConcept", "Provides the reason in the situation where a reason code is required in addition to the content.", 0, 1, reason));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify supporting information entries.", 0, 1, sequence);
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "The general class of the information supplied: information; exception; accident, employment; onset, etc.", 0, 1, category);
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought.", 0, 1, code);
          case 164632566: /*timing[x]*/  return new Property("timing[x]", "date|Period", "The date when or period to which this information refers.", 0, 1, timing);
          case -873664438: /*timing*/  return new Property("timing[x]", "date|Period", "The date when or period to which this information refers.", 0, 1, timing);
          case 807935768: /*timingDate*/  return new Property("timing[x]", "date|Period", "The date when or period to which this information refers.", 0, 1, timing);
          case -615615829: /*timingPeriod*/  return new Property("timing[x]", "date|Period", "The date when or period to which this information refers.", 0, 1, timing);
          case -1410166417: /*value[x]*/  return new Property("value[x]", "boolean|string|Quantity|Attachment|Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "boolean|string|Quantity|Attachment|Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, value);
          case 733421943: /*valueBoolean*/  return new Property("value[x]", "boolean|string|Quantity|Attachment|Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, value);
          case -1424603934: /*valueString*/  return new Property("value[x]", "boolean|string|Quantity|Attachment|Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, value);
          case -2029823716: /*valueQuantity*/  return new Property("value[x]", "boolean|string|Quantity|Attachment|Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, value);
          case -475566732: /*valueAttachment*/  return new Property("value[x]", "boolean|string|Quantity|Attachment|Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, value);
          case 1755241690: /*valueReference*/  return new Property("value[x]", "boolean|string|Quantity|Attachment|Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, value);
          case -934964668: /*reason*/  return new Property("reason", "CodeableConcept", "Provides the reason in the situation where a reason code is required in addition to the content.", 0, 1, reason);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // CodeableConcept
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
          this.reason = castToCodeableConcept(value); // CodeableConcept
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
          this.reason = castToCodeableConcept(value); // CodeableConcept
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
        case 111972721: /*value*/ return new String[] {"boolean", "string", "Quantity", "Attachment", "Reference"};
        case -934964668: /*reason*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
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
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
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
          this.reason = new CodeableConcept();
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SupportingInformationComponent))
          return false;
        SupportingInformationComponent o = (SupportingInformationComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(category, o.category, true) && compareDeep(code, o.code, true)
           && compareDeep(timing, o.timing, true) && compareDeep(value, o.value, true) && compareDeep(reason, o.reason, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SupportingInformationComponent))
          return false;
        SupportingInformationComponent o = (SupportingInformationComponent) other_;
        return compareValues(sequence, o.sequence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, category, code
          , timing, value, reason);
      }

  public String fhirType() {
    return "Claim.supportingInfo";

  }

  }

    @Block()
    public static class DiagnosisComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify diagnosis entries.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Diagnosis instance identifier", formalDefinition="A number to uniquely identify diagnosis entries." )
        protected PositiveIntType sequence;

        /**
         * The nature of illness or problem in a coded form or as a reference to an external defined Condition.
         */
        @Child(name = "diagnosis", type = {CodeableConcept.class, Condition.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Nature of illness or problem", formalDefinition="The nature of illness or problem in a coded form or as a reference to an external defined Condition." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/icd-10")
        protected Type diagnosis;

        /**
         * When the condition was observed or the relative ranking.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Timing or nature of the diagnosis", formalDefinition="When the condition was observed or the relative ranking." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-diagnosistype")
        protected List<CodeableConcept> type;

        /**
         * Indication of whether the diagnosis was present on admission to a facility.
         */
        @Child(name = "onAdmission", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Present on admission", formalDefinition="Indication of whether the diagnosis was present on admission to a facility." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-diagnosis-on-admission")
        protected CodeableConcept onAdmission;

        /**
         * A package billing code or bundle code used to group products and services to a particular health condition (such as heart attack) which is based on a predetermined grouping code system.
         */
        @Child(name = "packageCode", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Package billing code", formalDefinition="A package billing code or bundle code used to group products and services to a particular health condition (such as heart attack) which is based on a predetermined grouping code system." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-diagnosisrelatedgroup")
        protected CodeableConcept packageCode;

        private static final long serialVersionUID = 2120593974L;

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
         * @return {@link #sequence} (A number to uniquely identify diagnosis entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
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
         * @param value {@link #sequence} (A number to uniquely identify diagnosis entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public DiagnosisComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify diagnosis entries.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify diagnosis entries.
         */
        public DiagnosisComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #diagnosis} (The nature of illness or problem in a coded form or as a reference to an external defined Condition.)
         */
        public Type getDiagnosis() { 
          return this.diagnosis;
        }

        /**
         * @return {@link #diagnosis} (The nature of illness or problem in a coded form or as a reference to an external defined Condition.)
         */
        public CodeableConcept getDiagnosisCodeableConcept() throws FHIRException { 
          if (this.diagnosis == null)
            this.diagnosis = new CodeableConcept();
          if (!(this.diagnosis instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.diagnosis.getClass().getName()+" was encountered");
          return (CodeableConcept) this.diagnosis;
        }

        public boolean hasDiagnosisCodeableConcept() { 
          return this != null && this.diagnosis instanceof CodeableConcept;
        }

        /**
         * @return {@link #diagnosis} (The nature of illness or problem in a coded form or as a reference to an external defined Condition.)
         */
        public Reference getDiagnosisReference() throws FHIRException { 
          if (this.diagnosis == null)
            this.diagnosis = new Reference();
          if (!(this.diagnosis instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.diagnosis.getClass().getName()+" was encountered");
          return (Reference) this.diagnosis;
        }

        public boolean hasDiagnosisReference() { 
          return this != null && this.diagnosis instanceof Reference;
        }

        public boolean hasDiagnosis() { 
          return this.diagnosis != null && !this.diagnosis.isEmpty();
        }

        /**
         * @param value {@link #diagnosis} (The nature of illness or problem in a coded form or as a reference to an external defined Condition.)
         */
        public DiagnosisComponent setDiagnosis(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for Claim.diagnosis.diagnosis[x]: "+value.fhirType());
          this.diagnosis = value;
          return this;
        }

        /**
         * @return {@link #type} (When the condition was observed or the relative ranking.)
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
         * @return {@link #onAdmission} (Indication of whether the diagnosis was present on admission to a facility.)
         */
        public CodeableConcept getOnAdmission() { 
          if (this.onAdmission == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosisComponent.onAdmission");
            else if (Configuration.doAutoCreate())
              this.onAdmission = new CodeableConcept(); // cc
          return this.onAdmission;
        }

        public boolean hasOnAdmission() { 
          return this.onAdmission != null && !this.onAdmission.isEmpty();
        }

        /**
         * @param value {@link #onAdmission} (Indication of whether the diagnosis was present on admission to a facility.)
         */
        public DiagnosisComponent setOnAdmission(CodeableConcept value) { 
          this.onAdmission = value;
          return this;
        }

        /**
         * @return {@link #packageCode} (A package billing code or bundle code used to group products and services to a particular health condition (such as heart attack) which is based on a predetermined grouping code system.)
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
         * @param value {@link #packageCode} (A package billing code or bundle code used to group products and services to a particular health condition (such as heart attack) which is based on a predetermined grouping code system.)
         */
        public DiagnosisComponent setPackageCode(CodeableConcept value) { 
          this.packageCode = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify diagnosis entries.", 0, 1, sequence));
          children.add(new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis));
          children.add(new Property("type", "CodeableConcept", "When the condition was observed or the relative ranking.", 0, java.lang.Integer.MAX_VALUE, type));
          children.add(new Property("onAdmission", "CodeableConcept", "Indication of whether the diagnosis was present on admission to a facility.", 0, 1, onAdmission));
          children.add(new Property("packageCode", "CodeableConcept", "A package billing code or bundle code used to group products and services to a particular health condition (such as heart attack) which is based on a predetermined grouping code system.", 0, 1, packageCode));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify diagnosis entries.", 0, 1, sequence);
          case -1487009809: /*diagnosis[x]*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis);
          case 1196993265: /*diagnosis*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis);
          case 277781616: /*diagnosisCodeableConcept*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis);
          case 2050454362: /*diagnosisReference*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "When the condition was observed or the relative ranking.", 0, java.lang.Integer.MAX_VALUE, type);
          case -3386134: /*onAdmission*/  return new Property("onAdmission", "CodeableConcept", "Indication of whether the diagnosis was present on admission to a facility.", 0, 1, onAdmission);
          case 908444499: /*packageCode*/  return new Property("packageCode", "CodeableConcept", "A package billing code or bundle code used to group products and services to a particular health condition (such as heart attack) which is based on a predetermined grouping code system.", 0, 1, packageCode);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : new Base[] {this.diagnosis}; // Type
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case -3386134: /*onAdmission*/ return this.onAdmission == null ? new Base[0] : new Base[] {this.onAdmission}; // CodeableConcept
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
        case -3386134: // onAdmission
          this.onAdmission = castToCodeableConcept(value); // CodeableConcept
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
        } else if (name.equals("onAdmission")) {
          this.onAdmission = castToCodeableConcept(value); // CodeableConcept
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
        case -3386134:  return getOnAdmission(); 
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
        case -3386134: /*onAdmission*/ return new String[] {"CodeableConcept"};
        case 908444499: /*packageCode*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
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
        else if (name.equals("onAdmission")) {
          this.onAdmission = new CodeableConcept();
          return this.onAdmission;
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
        dst.onAdmission = onAdmission == null ? null : onAdmission.copy();
        dst.packageCode = packageCode == null ? null : packageCode.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DiagnosisComponent))
          return false;
        DiagnosisComponent o = (DiagnosisComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(type, o.type, true)
           && compareDeep(onAdmission, o.onAdmission, true) && compareDeep(packageCode, o.packageCode, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DiagnosisComponent))
          return false;
        DiagnosisComponent o = (DiagnosisComponent) other_;
        return compareValues(sequence, o.sequence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, diagnosis, type
          , onAdmission, packageCode);
      }

  public String fhirType() {
    return "Claim.diagnosis";

  }

  }

    @Block()
    public static class ProcedureComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify procedure entries.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Procedure instance identifier", formalDefinition="A number to uniquely identify procedure entries." )
        protected PositiveIntType sequence;

        /**
         * When the condition was observed or the relative ranking.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Category of Procedure", formalDefinition="When the condition was observed or the relative ranking." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-procedure-type")
        protected List<CodeableConcept> type;

        /**
         * Date and optionally time the procedure was performed.
         */
        @Child(name = "date", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When the procedure was performed", formalDefinition="Date and optionally time the procedure was performed." )
        protected DateTimeType date;

        /**
         * The code or reference to a Procedure resource which identifies the clinical intervention performed.
         */
        @Child(name = "procedure", type = {CodeableConcept.class, Procedure.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specific clinical procedure", formalDefinition="The code or reference to a Procedure resource which identifies the clinical intervention performed." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/icd-10-procedures")
        protected Type procedure;

        /**
         * Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Unique device identifier", formalDefinition="Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        private static final long serialVersionUID = 935341852L;

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
         * @return {@link #sequence} (A number to uniquely identify procedure entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
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
         * @param value {@link #sequence} (A number to uniquely identify procedure entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public ProcedureComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify procedure entries.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify procedure entries.
         */
        public ProcedureComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (When the condition was observed or the relative ranking.)
         */
        public List<CodeableConcept> getType() { 
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          return this.type;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProcedureComponent setType(List<CodeableConcept> theType) { 
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

        public ProcedureComponent addType(CodeableConcept t) { //3
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
         * @return {@link #date} (Date and optionally time the procedure was performed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
         * @param value {@link #date} (Date and optionally time the procedure was performed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public ProcedureComponent setDateElement(DateTimeType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Date and optionally time the procedure was performed.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Date and optionally time the procedure was performed.
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
         * @return {@link #procedure} (The code or reference to a Procedure resource which identifies the clinical intervention performed.)
         */
        public Type getProcedure() { 
          return this.procedure;
        }

        /**
         * @return {@link #procedure} (The code or reference to a Procedure resource which identifies the clinical intervention performed.)
         */
        public CodeableConcept getProcedureCodeableConcept() throws FHIRException { 
          if (this.procedure == null)
            this.procedure = new CodeableConcept();
          if (!(this.procedure instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.procedure.getClass().getName()+" was encountered");
          return (CodeableConcept) this.procedure;
        }

        public boolean hasProcedureCodeableConcept() { 
          return this != null && this.procedure instanceof CodeableConcept;
        }

        /**
         * @return {@link #procedure} (The code or reference to a Procedure resource which identifies the clinical intervention performed.)
         */
        public Reference getProcedureReference() throws FHIRException { 
          if (this.procedure == null)
            this.procedure = new Reference();
          if (!(this.procedure instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.procedure.getClass().getName()+" was encountered");
          return (Reference) this.procedure;
        }

        public boolean hasProcedureReference() { 
          return this != null && this.procedure instanceof Reference;
        }

        public boolean hasProcedure() { 
          return this.procedure != null && !this.procedure.isEmpty();
        }

        /**
         * @param value {@link #procedure} (The code or reference to a Procedure resource which identifies the clinical intervention performed.)
         */
        public ProcedureComponent setProcedure(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for Claim.procedure.procedure[x]: "+value.fhirType());
          this.procedure = value;
          return this;
        }

        /**
         * @return {@link #udi} (Unique Device Identifiers associated with this line item.)
         */
        public List<Reference> getUdi() { 
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          return this.udi;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProcedureComponent setUdi(List<Reference> theUdi) { 
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

        public ProcedureComponent addUdi(Reference t) { //3
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify procedure entries.", 0, 1, sequence));
          children.add(new Property("type", "CodeableConcept", "When the condition was observed or the relative ranking.", 0, java.lang.Integer.MAX_VALUE, type));
          children.add(new Property("date", "dateTime", "Date and optionally time the procedure was performed.", 0, 1, date));
          children.add(new Property("procedure[x]", "CodeableConcept|Reference(Procedure)", "The code or reference to a Procedure resource which identifies the clinical intervention performed.", 0, 1, procedure));
          children.add(new Property("udi", "Reference(Device)", "Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify procedure entries.", 0, 1, sequence);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "When the condition was observed or the relative ranking.", 0, java.lang.Integer.MAX_VALUE, type);
          case 3076014: /*date*/  return new Property("date", "dateTime", "Date and optionally time the procedure was performed.", 0, 1, date);
          case 1640074445: /*procedure[x]*/  return new Property("procedure[x]", "CodeableConcept|Reference(Procedure)", "The code or reference to a Procedure resource which identifies the clinical intervention performed.", 0, 1, procedure);
          case -1095204141: /*procedure*/  return new Property("procedure[x]", "CodeableConcept|Reference(Procedure)", "The code or reference to a Procedure resource which identifies the clinical intervention performed.", 0, 1, procedure);
          case -1284783026: /*procedureCodeableConcept*/  return new Property("procedure[x]", "CodeableConcept|Reference(Procedure)", "The code or reference to a Procedure resource which identifies the clinical intervention performed.", 0, 1, procedure);
          case 881809848: /*procedureReference*/  return new Property("procedure[x]", "CodeableConcept|Reference(Procedure)", "The code or reference to a Procedure resource which identifies the clinical intervention performed.", 0, 1, procedure);
          case 115642: /*udi*/  return new Property("udi", "Reference(Device)", "Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : new Base[] {this.procedure}; // Type
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
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
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case -1095204141: // procedure
          this.procedure = castToType(value); // Type
          return value;
        case 115642: // udi
          this.getUdi().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("type")) {
          this.getType().add(castToCodeableConcept(value));
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("procedure[x]")) {
          this.procedure = castToType(value); // Type
        } else if (name.equals("udi")) {
          this.getUdi().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 3575610:  return addType(); 
        case 3076014:  return getDateElement();
        case 1640074445:  return getProcedure(); 
        case -1095204141:  return getProcedure(); 
        case 115642:  return addUdi(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case -1095204141: /*procedure*/ return new String[] {"CodeableConcept", "Reference"};
        case 115642: /*udi*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.date");
        }
        else if (name.equals("procedureCodeableConcept")) {
          this.procedure = new CodeableConcept();
          return this.procedure;
        }
        else if (name.equals("procedureReference")) {
          this.procedure = new Reference();
          return this.procedure;
        }
        else if (name.equals("udi")) {
          return addUdi();
        }
        else
          return super.addChild(name);
      }

      public ProcedureComponent copy() {
        ProcedureComponent dst = new ProcedureComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.procedure = procedure == null ? null : procedure.copy();
        if (udi != null) {
          dst.udi = new ArrayList<Reference>();
          for (Reference i : udi)
            dst.udi.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProcedureComponent))
          return false;
        ProcedureComponent o = (ProcedureComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(date, o.date, true)
           && compareDeep(procedure, o.procedure, true) && compareDeep(udi, o.udi, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProcedureComponent))
          return false;
        ProcedureComponent o = (ProcedureComponent) other_;
        return compareValues(sequence, o.sequence, true) && compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, type, date, procedure
          , udi);
      }

  public String fhirType() {
    return "Claim.procedure";

  }

  }

    @Block()
    public static class InsuranceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Insurance instance identifier", formalDefinition="A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order." )
        protected PositiveIntType sequence;

        /**
         * A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
         */
        @Child(name = "focal", type = {BooleanType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Coverage to be used for adjudication", formalDefinition="A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true." )
        protected BooleanType focal;

        /**
         * The business identifier to be used when the claim is sent for adjudication against this insurance policy.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Pre-assigned Claim number", formalDefinition="The business identifier to be used when the claim is sent for adjudication against this insurance policy." )
        protected Identifier identifier;

        /**
         * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.
         */
        @Child(name = "coverage", type = {Coverage.class}, order=4, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system." )
        protected Reference coverage;

        /**
         * The actual object that is the target of the reference (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        protected Coverage coverageTarget;

        /**
         * A business agreement number established between the provider and the insurer for special business processing purposes.
         */
        @Child(name = "businessArrangement", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional provider contract number", formalDefinition="A business agreement number established between the provider and the insurer for special business processing purposes." )
        protected StringType businessArrangement;

        /**
         * Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing services or products related to the prior authorization.
         */
        @Child(name = "preAuthRef", type = {StringType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Prior authorization reference number", formalDefinition="Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing services or products related to the prior authorization." )
        protected List<StringType> preAuthRef;

        /**
         * The result of the adjudication of the line items for the Coverage specified in this insurance.
         */
        @Child(name = "claimResponse", type = {ClaimResponse.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication results", formalDefinition="The result of the adjudication of the line items for the Coverage specified in this insurance." )
        protected Reference claimResponse;

        /**
         * The actual object that is the target of the reference (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        protected ClaimResponse claimResponseTarget;

        private static final long serialVersionUID = -1711744215L;

    /**
     * Constructor
     */
      public InsuranceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InsuranceComponent(PositiveIntType sequence, BooleanType focal, Reference coverage) {
        super();
        this.sequence = sequence;
        this.focal = focal;
        this.coverage = coverage;
      }

        /**
         * @return {@link #sequence} (A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.sequence");
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
         * @param value {@link #sequence} (A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public InsuranceComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.
         */
        public InsuranceComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #focal} (A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public BooleanType getFocalElement() { 
          if (this.focal == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.focal");
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
         * @param value {@link #focal} (A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public InsuranceComponent setFocalElement(BooleanType value) { 
          this.focal = value;
          return this;
        }

        /**
         * @return A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
         */
        public boolean getFocal() { 
          return this.focal == null || this.focal.isEmpty() ? false : this.focal.getValue();
        }

        /**
         * @param value A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
         */
        public InsuranceComponent setFocal(boolean value) { 
            if (this.focal == null)
              this.focal = new BooleanType();
            this.focal.setValue(value);
          return this;
        }

        /**
         * @return {@link #identifier} (The business identifier to be used when the claim is sent for adjudication against this insurance policy.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The business identifier to be used when the claim is sent for adjudication against this insurance policy.)
         */
        public InsuranceComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #coverage} (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
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
         * @param value {@link #coverage} (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public InsuranceComponent setCoverage(Reference value) { 
          this.coverage = value;
          return this;
        }

        /**
         * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
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
         * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public InsuranceComponent setCoverageTarget(Coverage value) { 
          this.coverageTarget = value;
          return this;
        }

        /**
         * @return {@link #businessArrangement} (A business agreement number established between the provider and the insurer for special business processing purposes.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public StringType getBusinessArrangementElement() { 
          if (this.businessArrangement == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.businessArrangement");
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
         * @param value {@link #businessArrangement} (A business agreement number established between the provider and the insurer for special business processing purposes.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public InsuranceComponent setBusinessArrangementElement(StringType value) { 
          this.businessArrangement = value;
          return this;
        }

        /**
         * @return A business agreement number established between the provider and the insurer for special business processing purposes.
         */
        public String getBusinessArrangement() { 
          return this.businessArrangement == null ? null : this.businessArrangement.getValue();
        }

        /**
         * @param value A business agreement number established between the provider and the insurer for special business processing purposes.
         */
        public InsuranceComponent setBusinessArrangement(String value) { 
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
         * @return {@link #preAuthRef} (Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing services or products related to the prior authorization.)
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
         * @return {@link #preAuthRef} (Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing services or products related to the prior authorization.)
         */
        public StringType addPreAuthRefElement() {//2 
          StringType t = new StringType();
          if (this.preAuthRef == null)
            this.preAuthRef = new ArrayList<StringType>();
          this.preAuthRef.add(t);
          return t;
        }

        /**
         * @param value {@link #preAuthRef} (Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing services or products related to the prior authorization.)
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
         * @param value {@link #preAuthRef} (Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing services or products related to the prior authorization.)
         */
        public boolean hasPreAuthRef(String value) { 
          if (this.preAuthRef == null)
            return false;
          for (StringType v : this.preAuthRef)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #claimResponse} (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        public Reference getClaimResponse() { 
          if (this.claimResponse == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.claimResponse");
            else if (Configuration.doAutoCreate())
              this.claimResponse = new Reference(); // cc
          return this.claimResponse;
        }

        public boolean hasClaimResponse() { 
          return this.claimResponse != null && !this.claimResponse.isEmpty();
        }

        /**
         * @param value {@link #claimResponse} (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        public InsuranceComponent setClaimResponse(Reference value) { 
          this.claimResponse = value;
          return this;
        }

        /**
         * @return {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        public ClaimResponse getClaimResponseTarget() { 
          if (this.claimResponseTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.claimResponse");
            else if (Configuration.doAutoCreate())
              this.claimResponseTarget = new ClaimResponse(); // aa
          return this.claimResponseTarget;
        }

        /**
         * @param value {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        public InsuranceComponent setClaimResponseTarget(ClaimResponse value) { 
          this.claimResponseTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.", 0, 1, sequence));
          children.add(new Property("focal", "boolean", "A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.", 0, 1, focal));
          children.add(new Property("identifier", "Identifier", "The business identifier to be used when the claim is sent for adjudication against this insurance policy.", 0, 1, identifier));
          children.add(new Property("coverage", "Reference(Coverage)", "Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.", 0, 1, coverage));
          children.add(new Property("businessArrangement", "string", "A business agreement number established between the provider and the insurer for special business processing purposes.", 0, 1, businessArrangement));
          children.add(new Property("preAuthRef", "string", "Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing services or products related to the prior authorization.", 0, java.lang.Integer.MAX_VALUE, preAuthRef));
          children.add(new Property("claimResponse", "Reference(ClaimResponse)", "The result of the adjudication of the line items for the Coverage specified in this insurance.", 0, 1, claimResponse));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.", 0, 1, sequence);
          case 97604197: /*focal*/  return new Property("focal", "boolean", "A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.", 0, 1, focal);
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "The business identifier to be used when the claim is sent for adjudication against this insurance policy.", 0, 1, identifier);
          case -351767064: /*coverage*/  return new Property("coverage", "Reference(Coverage)", "Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.", 0, 1, coverage);
          case 259920682: /*businessArrangement*/  return new Property("businessArrangement", "string", "A business agreement number established between the provider and the insurer for special business processing purposes.", 0, 1, businessArrangement);
          case 522246568: /*preAuthRef*/  return new Property("preAuthRef", "string", "Reference numbers previously provided by the insurer to the provider to be quoted on subsequent claims containing services or products related to the prior authorization.", 0, java.lang.Integer.MAX_VALUE, preAuthRef);
          case 689513629: /*claimResponse*/  return new Property("claimResponse", "Reference(ClaimResponse)", "The result of the adjudication of the line items for the Coverage specified in this insurance.", 0, 1, claimResponse);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 97604197: /*focal*/ return this.focal == null ? new Base[0] : new Base[] {this.focal}; // BooleanType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Reference
        case 259920682: /*businessArrangement*/ return this.businessArrangement == null ? new Base[0] : new Base[] {this.businessArrangement}; // StringType
        case 522246568: /*preAuthRef*/ return this.preAuthRef == null ? new Base[0] : this.preAuthRef.toArray(new Base[this.preAuthRef.size()]); // StringType
        case 689513629: /*claimResponse*/ return this.claimResponse == null ? new Base[0] : new Base[] {this.claimResponse}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 97604197: // focal
          this.focal = castToBoolean(value); // BooleanType
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -351767064: // coverage
          this.coverage = castToReference(value); // Reference
          return value;
        case 259920682: // businessArrangement
          this.businessArrangement = castToString(value); // StringType
          return value;
        case 522246568: // preAuthRef
          this.getPreAuthRef().add(castToString(value)); // StringType
          return value;
        case 689513629: // claimResponse
          this.claimResponse = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("focal")) {
          this.focal = castToBoolean(value); // BooleanType
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("coverage")) {
          this.coverage = castToReference(value); // Reference
        } else if (name.equals("businessArrangement")) {
          this.businessArrangement = castToString(value); // StringType
        } else if (name.equals("preAuthRef")) {
          this.getPreAuthRef().add(castToString(value));
        } else if (name.equals("claimResponse")) {
          this.claimResponse = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 97604197:  return getFocalElement();
        case -1618432855:  return getIdentifier(); 
        case -351767064:  return getCoverage(); 
        case 259920682:  return getBusinessArrangementElement();
        case 522246568:  return addPreAuthRefElement();
        case 689513629:  return getClaimResponse(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 97604197: /*focal*/ return new String[] {"boolean"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -351767064: /*coverage*/ return new String[] {"Reference"};
        case 259920682: /*businessArrangement*/ return new String[] {"string"};
        case 522246568: /*preAuthRef*/ return new String[] {"string"};
        case 689513629: /*claimResponse*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
        }
        else if (name.equals("focal")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.focal");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("businessArrangement")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.businessArrangement");
        }
        else if (name.equals("preAuthRef")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.preAuthRef");
        }
        else if (name.equals("claimResponse")) {
          this.claimResponse = new Reference();
          return this.claimResponse;
        }
        else
          return super.addChild(name);
      }

      public InsuranceComponent copy() {
        InsuranceComponent dst = new InsuranceComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.focal = focal == null ? null : focal.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.businessArrangement = businessArrangement == null ? null : businessArrangement.copy();
        if (preAuthRef != null) {
          dst.preAuthRef = new ArrayList<StringType>();
          for (StringType i : preAuthRef)
            dst.preAuthRef.add(i.copy());
        };
        dst.claimResponse = claimResponse == null ? null : claimResponse.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(focal, o.focal, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(coverage, o.coverage, true) && compareDeep(businessArrangement, o.businessArrangement, true)
           && compareDeep(preAuthRef, o.preAuthRef, true) && compareDeep(claimResponse, o.claimResponse, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other_;
        return compareValues(sequence, o.sequence, true) && compareValues(focal, o.focal, true) && compareValues(businessArrangement, o.businessArrangement, true)
           && compareValues(preAuthRef, o.preAuthRef, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, focal, identifier
          , coverage, businessArrangement, preAuthRef, claimResponse);
      }

  public String fhirType() {
    return "Claim.insurance";

  }

  }

    @Block()
    public static class AccidentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Date of an accident event  related to the products and services contained in the claim.
         */
        @Child(name = "date", type = {DateType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When the incident occurred", formalDefinition="Date of an accident event  related to the products and services contained in the claim." )
        protected DateType date;

        /**
         * The type or context of the accident event for the purposes of selection of potential insurance coverages and determination of coordination between insurers.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The nature of the accident", formalDefinition="The type or context of the accident event for the purposes of selection of potential insurance coverages and determination of coordination between insurers." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://terminology.hl7.org/ValueSet/v3-ActIncidentCode")
        protected CodeableConcept type;

        /**
         * The physical location of the accident event.
         */
        @Child(name = "location", type = {Address.class, Location.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Where the event occurred", formalDefinition="The physical location of the accident event." )
        protected Type location;

        private static final long serialVersionUID = 622904984L;

    /**
     * Constructor
     */
      public AccidentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AccidentComponent(DateType date) {
        super();
        this.date = date;
      }

        /**
         * @return {@link #date} (Date of an accident event  related to the products and services contained in the claim.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
         * @param value {@link #date} (Date of an accident event  related to the products and services contained in the claim.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public AccidentComponent setDateElement(DateType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Date of an accident event  related to the products and services contained in the claim.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Date of an accident event  related to the products and services contained in the claim.
         */
        public AccidentComponent setDate(Date value) { 
            if (this.date == null)
              this.date = new DateType();
            this.date.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type or context of the accident event for the purposes of selection of potential insurance coverages and determination of coordination between insurers.)
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
         * @param value {@link #type} (The type or context of the accident event for the purposes of selection of potential insurance coverages and determination of coordination between insurers.)
         */
        public AccidentComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #location} (The physical location of the accident event.)
         */
        public Type getLocation() { 
          return this.location;
        }

        /**
         * @return {@link #location} (The physical location of the accident event.)
         */
        public Address getLocationAddress() throws FHIRException { 
          if (this.location == null)
            this.location = new Address();
          if (!(this.location instanceof Address))
            throw new FHIRException("Type mismatch: the type Address was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Address) this.location;
        }

        public boolean hasLocationAddress() { 
          return this != null && this.location instanceof Address;
        }

        /**
         * @return {@link #location} (The physical location of the accident event.)
         */
        public Reference getLocationReference() throws FHIRException { 
          if (this.location == null)
            this.location = new Reference();
          if (!(this.location instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Reference) this.location;
        }

        public boolean hasLocationReference() { 
          return this != null && this.location instanceof Reference;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (The physical location of the accident event.)
         */
        public AccidentComponent setLocation(Type value) { 
          if (value != null && !(value instanceof Address || value instanceof Reference))
            throw new Error("Not the right type for Claim.accident.location[x]: "+value.fhirType());
          this.location = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("date", "date", "Date of an accident event  related to the products and services contained in the claim.", 0, 1, date));
          children.add(new Property("type", "CodeableConcept", "The type or context of the accident event for the purposes of selection of potential insurance coverages and determination of coordination between insurers.", 0, 1, type));
          children.add(new Property("location[x]", "Address|Reference(Location)", "The physical location of the accident event.", 0, 1, location));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3076014: /*date*/  return new Property("date", "date", "Date of an accident event  related to the products and services contained in the claim.", 0, 1, date);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type or context of the accident event for the purposes of selection of potential insurance coverages and determination of coordination between insurers.", 0, 1, type);
          case 552316075: /*location[x]*/  return new Property("location[x]", "Address|Reference(Location)", "The physical location of the accident event.", 0, 1, location);
          case 1901043637: /*location*/  return new Property("location[x]", "Address|Reference(Location)", "The physical location of the accident event.", 0, 1, location);
          case -1280020865: /*locationAddress*/  return new Property("location[x]", "Address|Reference(Location)", "The physical location of the accident event.", 0, 1, location);
          case 755866390: /*locationReference*/  return new Property("location[x]", "Address|Reference(Location)", "The physical location of the accident event.", 0, 1, location);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.date");
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AccidentComponent))
          return false;
        AccidentComponent o = (AccidentComponent) other_;
        return compareDeep(date, o.date, true) && compareDeep(type, o.type, true) && compareDeep(location, o.location, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AccidentComponent))
          return false;
        AccidentComponent o = (AccidentComponent) other_;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(date, type, location);
      }

  public String fhirType() {
    return "Claim.accident";

  }

  }

    @Block()
    public static class ItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify item entries.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Item instance identifier", formalDefinition="A number to uniquely identify item entries." )
        protected PositiveIntType sequence;

        /**
         * CareTeam members related to this service or product.
         */
        @Child(name = "careTeamSequence", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable careTeam members", formalDefinition="CareTeam members related to this service or product." )
        protected List<PositiveIntType> careTeamSequence;

        /**
         * Diagnosis applicable for this service or product.
         */
        @Child(name = "diagnosisSequence", type = {PositiveIntType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable diagnoses", formalDefinition="Diagnosis applicable for this service or product." )
        protected List<PositiveIntType> diagnosisSequence;

        /**
         * Procedures applicable for this service or product.
         */
        @Child(name = "procedureSequence", type = {PositiveIntType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable procedures", formalDefinition="Procedures applicable for this service or product." )
        protected List<PositiveIntType> procedureSequence;

        /**
         * Exceptions, special conditions and supporting information applicable for this service or product.
         */
        @Child(name = "informationSequence", type = {PositiveIntType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable exception and supporting information", formalDefinition="Exceptions, special conditions and supporting information applicable for this service or product." )
        protected List<PositiveIntType> informationSequence;

        /**
         * The type of revenue or cost center providing the product and/or service.
         */
        @Child(name = "revenue", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Revenue or cost center code", formalDefinition="The type of revenue or cost center providing the product and/or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-revenue-center")
        protected CodeableConcept revenue;

        /**
         * Code to identify the general type of benefits under which products and services are provided.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Benefit classification", formalDefinition="Code to identify the general type of benefits under which products and services are provided." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-benefitcategory")
        protected CodeableConcept category;

        /**
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.
         */
        @Child(name = "productOrService", type = {CodeableConcept.class}, order=8, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing, service, product, or drug code", formalDefinition="When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept productOrService;

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Product or service billing modifiers", formalDefinition="Item typification or modifiers codes to convey additional context for the product or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * Identifies the program under which this may be recovered.
         */
        @Child(name = "programCode", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Program the product or service is provided under", formalDefinition="Identifies the program under which this may be recovered." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-program-code")
        protected List<CodeableConcept> programCode;

        /**
         * The date or dates when the service or product was supplied, performed or completed.
         */
        @Child(name = "serviced", type = {DateType.class, Period.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Date or dates of service or product delivery", formalDefinition="The date or dates when the service or product was supplied, performed or completed." )
        protected Type serviced;

        /**
         * Where the product or service was provided.
         */
        @Child(name = "location", type = {CodeableConcept.class, Address.class, Location.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Place of service or where product was supplied", formalDefinition="Where the product or service was provided." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-place")
        protected Type location;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of products or services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per item", formalDefinition="If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * The quantity times the unit price for an additional service or product or charge.
         */
        @Child(name = "net", type = {Money.class}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an additional service or product or charge." )
        protected Money net;

        /**
         * Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Unique device identifier", formalDefinition="Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        /**
         * Physical service site on the patient (limb, tooth, etc.).
         */
        @Child(name = "bodySite", type = {CodeableConcept.class}, order=18, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Anatomical location", formalDefinition="Physical service site on the patient (limb, tooth, etc.)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/tooth")
        protected CodeableConcept bodySite;

        /**
         * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
         */
        @Child(name = "subSite", type = {CodeableConcept.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Anatomical sub-location", formalDefinition="A region or surface of the bodySite, e.g. limb region or tooth surface(s)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/surface")
        protected List<CodeableConcept> subSite;

        /**
         * The Encounters during which this Claim was created or to which the creation of this record is tightly associated.
         */
        @Child(name = "encounter", type = {Encounter.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Encounters related to this billed item", formalDefinition="The Encounters during which this Claim was created or to which the creation of this record is tightly associated." )
        protected List<Reference> encounter;
        /**
         * The actual objects that are the target of the reference (The Encounters during which this Claim was created or to which the creation of this record is tightly associated.)
         */
        protected List<Encounter> encounterTarget;


        /**
         * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
         */
        @Child(name = "detail", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Product or service provided", formalDefinition="A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items." )
        protected List<DetailComponent> detail;

        private static final long serialVersionUID = -329028323L;

    /**
     * Constructor
     */
      public ItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemComponent(PositiveIntType sequence, CodeableConcept productOrService) {
        super();
        this.sequence = sequence;
        this.productOrService = productOrService;
      }

        /**
         * @return {@link #sequence} (A number to uniquely identify item entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
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
         * @param value {@link #sequence} (A number to uniquely identify item entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public ItemComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify item entries.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify item entries.
         */
        public ItemComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #careTeamSequence} (CareTeam members related to this service or product.)
         */
        public List<PositiveIntType> getCareTeamSequence() { 
          if (this.careTeamSequence == null)
            this.careTeamSequence = new ArrayList<PositiveIntType>();
          return this.careTeamSequence;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setCareTeamSequence(List<PositiveIntType> theCareTeamSequence) { 
          this.careTeamSequence = theCareTeamSequence;
          return this;
        }

        public boolean hasCareTeamSequence() { 
          if (this.careTeamSequence == null)
            return false;
          for (PositiveIntType item : this.careTeamSequence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #careTeamSequence} (CareTeam members related to this service or product.)
         */
        public PositiveIntType addCareTeamSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.careTeamSequence == null)
            this.careTeamSequence = new ArrayList<PositiveIntType>();
          this.careTeamSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #careTeamSequence} (CareTeam members related to this service or product.)
         */
        public ItemComponent addCareTeamSequence(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.careTeamSequence == null)
            this.careTeamSequence = new ArrayList<PositiveIntType>();
          this.careTeamSequence.add(t);
          return this;
        }

        /**
         * @param value {@link #careTeamSequence} (CareTeam members related to this service or product.)
         */
        public boolean hasCareTeamSequence(int value) { 
          if (this.careTeamSequence == null)
            return false;
          for (PositiveIntType v : this.careTeamSequence)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #diagnosisSequence} (Diagnosis applicable for this service or product.)
         */
        public List<PositiveIntType> getDiagnosisSequence() { 
          if (this.diagnosisSequence == null)
            this.diagnosisSequence = new ArrayList<PositiveIntType>();
          return this.diagnosisSequence;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setDiagnosisSequence(List<PositiveIntType> theDiagnosisSequence) { 
          this.diagnosisSequence = theDiagnosisSequence;
          return this;
        }

        public boolean hasDiagnosisSequence() { 
          if (this.diagnosisSequence == null)
            return false;
          for (PositiveIntType item : this.diagnosisSequence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #diagnosisSequence} (Diagnosis applicable for this service or product.)
         */
        public PositiveIntType addDiagnosisSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.diagnosisSequence == null)
            this.diagnosisSequence = new ArrayList<PositiveIntType>();
          this.diagnosisSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #diagnosisSequence} (Diagnosis applicable for this service or product.)
         */
        public ItemComponent addDiagnosisSequence(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.diagnosisSequence == null)
            this.diagnosisSequence = new ArrayList<PositiveIntType>();
          this.diagnosisSequence.add(t);
          return this;
        }

        /**
         * @param value {@link #diagnosisSequence} (Diagnosis applicable for this service or product.)
         */
        public boolean hasDiagnosisSequence(int value) { 
          if (this.diagnosisSequence == null)
            return false;
          for (PositiveIntType v : this.diagnosisSequence)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #procedureSequence} (Procedures applicable for this service or product.)
         */
        public List<PositiveIntType> getProcedureSequence() { 
          if (this.procedureSequence == null)
            this.procedureSequence = new ArrayList<PositiveIntType>();
          return this.procedureSequence;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setProcedureSequence(List<PositiveIntType> theProcedureSequence) { 
          this.procedureSequence = theProcedureSequence;
          return this;
        }

        public boolean hasProcedureSequence() { 
          if (this.procedureSequence == null)
            return false;
          for (PositiveIntType item : this.procedureSequence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #procedureSequence} (Procedures applicable for this service or product.)
         */
        public PositiveIntType addProcedureSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.procedureSequence == null)
            this.procedureSequence = new ArrayList<PositiveIntType>();
          this.procedureSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #procedureSequence} (Procedures applicable for this service or product.)
         */
        public ItemComponent addProcedureSequence(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.procedureSequence == null)
            this.procedureSequence = new ArrayList<PositiveIntType>();
          this.procedureSequence.add(t);
          return this;
        }

        /**
         * @param value {@link #procedureSequence} (Procedures applicable for this service or product.)
         */
        public boolean hasProcedureSequence(int value) { 
          if (this.procedureSequence == null)
            return false;
          for (PositiveIntType v : this.procedureSequence)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #informationSequence} (Exceptions, special conditions and supporting information applicable for this service or product.)
         */
        public List<PositiveIntType> getInformationSequence() { 
          if (this.informationSequence == null)
            this.informationSequence = new ArrayList<PositiveIntType>();
          return this.informationSequence;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setInformationSequence(List<PositiveIntType> theInformationSequence) { 
          this.informationSequence = theInformationSequence;
          return this;
        }

        public boolean hasInformationSequence() { 
          if (this.informationSequence == null)
            return false;
          for (PositiveIntType item : this.informationSequence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #informationSequence} (Exceptions, special conditions and supporting information applicable for this service or product.)
         */
        public PositiveIntType addInformationSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.informationSequence == null)
            this.informationSequence = new ArrayList<PositiveIntType>();
          this.informationSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #informationSequence} (Exceptions, special conditions and supporting information applicable for this service or product.)
         */
        public ItemComponent addInformationSequence(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.informationSequence == null)
            this.informationSequence = new ArrayList<PositiveIntType>();
          this.informationSequence.add(t);
          return this;
        }

        /**
         * @param value {@link #informationSequence} (Exceptions, special conditions and supporting information applicable for this service or product.)
         */
        public boolean hasInformationSequence(int value) { 
          if (this.informationSequence == null)
            return false;
          for (PositiveIntType v : this.informationSequence)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #revenue} (The type of revenue or cost center providing the product and/or service.)
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
         * @param value {@link #revenue} (The type of revenue or cost center providing the product and/or service.)
         */
        public ItemComponent setRevenue(CodeableConcept value) { 
          this.revenue = value;
          return this;
        }

        /**
         * @return {@link #category} (Code to identify the general type of benefits under which products and services are provided.)
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
         * @param value {@link #category} (Code to identify the general type of benefits under which products and services are provided.)
         */
        public ItemComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public CodeableConcept getProductOrService() { 
          if (this.productOrService == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.productOrService");
            else if (Configuration.doAutoCreate())
              this.productOrService = new CodeableConcept(); // cc
          return this.productOrService;
        }

        public boolean hasProductOrService() { 
          return this.productOrService != null && !this.productOrService.isEmpty();
        }

        /**
         * @param value {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public ItemComponent setProductOrService(CodeableConcept value) { 
          this.productOrService = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes to convey additional context for the product or service.)
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
         * @return {@link #programCode} (Identifies the program under which this may be recovered.)
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
         * @return {@link #serviced} (The date or dates when the service or product was supplied, performed or completed.)
         */
        public Type getServiced() { 
          return this.serviced;
        }

        /**
         * @return {@link #serviced} (The date or dates when the service or product was supplied, performed or completed.)
         */
        public DateType getServicedDateType() throws FHIRException { 
          if (this.serviced == null)
            this.serviced = new DateType();
          if (!(this.serviced instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.serviced.getClass().getName()+" was encountered");
          return (DateType) this.serviced;
        }

        public boolean hasServicedDateType() { 
          return this != null && this.serviced instanceof DateType;
        }

        /**
         * @return {@link #serviced} (The date or dates when the service or product was supplied, performed or completed.)
         */
        public Period getServicedPeriod() throws FHIRException { 
          if (this.serviced == null)
            this.serviced = new Period();
          if (!(this.serviced instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.serviced.getClass().getName()+" was encountered");
          return (Period) this.serviced;
        }

        public boolean hasServicedPeriod() { 
          return this != null && this.serviced instanceof Period;
        }

        public boolean hasServiced() { 
          return this.serviced != null && !this.serviced.isEmpty();
        }

        /**
         * @param value {@link #serviced} (The date or dates when the service or product was supplied, performed or completed.)
         */
        public ItemComponent setServiced(Type value) { 
          if (value != null && !(value instanceof DateType || value instanceof Period))
            throw new Error("Not the right type for Claim.item.serviced[x]: "+value.fhirType());
          this.serviced = value;
          return this;
        }

        /**
         * @return {@link #location} (Where the product or service was provided.)
         */
        public Type getLocation() { 
          return this.location;
        }

        /**
         * @return {@link #location} (Where the product or service was provided.)
         */
        public CodeableConcept getLocationCodeableConcept() throws FHIRException { 
          if (this.location == null)
            this.location = new CodeableConcept();
          if (!(this.location instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.location.getClass().getName()+" was encountered");
          return (CodeableConcept) this.location;
        }

        public boolean hasLocationCodeableConcept() { 
          return this != null && this.location instanceof CodeableConcept;
        }

        /**
         * @return {@link #location} (Where the product or service was provided.)
         */
        public Address getLocationAddress() throws FHIRException { 
          if (this.location == null)
            this.location = new Address();
          if (!(this.location instanceof Address))
            throw new FHIRException("Type mismatch: the type Address was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Address) this.location;
        }

        public boolean hasLocationAddress() { 
          return this != null && this.location instanceof Address;
        }

        /**
         * @return {@link #location} (Where the product or service was provided.)
         */
        public Reference getLocationReference() throws FHIRException { 
          if (this.location == null)
            this.location = new Reference();
          if (!(this.location instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Reference) this.location;
        }

        public boolean hasLocationReference() { 
          return this != null && this.location instanceof Reference;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (Where the product or service was provided.)
         */
        public ItemComponent setLocation(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Address || value instanceof Reference))
            throw new Error("Not the right type for Claim.item.location[x]: "+value.fhirType());
          this.location = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public ItemComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
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
         * @param value {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
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
         * @return {@link #net} (The quantity times the unit price for an additional service or product or charge.)
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
         * @param value {@link #net} (The quantity times the unit price for an additional service or product or charge.)
         */
        public ItemComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (Unique Device Identifiers associated with this line item.)
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
         * @return {@link #bodySite} (Physical service site on the patient (limb, tooth, etc.).)
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
         * @param value {@link #bodySite} (Physical service site on the patient (limb, tooth, etc.).)
         */
        public ItemComponent setBodySite(CodeableConcept value) { 
          this.bodySite = value;
          return this;
        }

        /**
         * @return {@link #subSite} (A region or surface of the bodySite, e.g. limb region or tooth surface(s).)
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
         * @return {@link #encounter} (The Encounters during which this Claim was created or to which the creation of this record is tightly associated.)
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
         * @return {@link #detail} (A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.)
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify item entries.", 0, 1, sequence));
          children.add(new Property("careTeamSequence", "positiveInt", "CareTeam members related to this service or product.", 0, java.lang.Integer.MAX_VALUE, careTeamSequence));
          children.add(new Property("diagnosisSequence", "positiveInt", "Diagnosis applicable for this service or product.", 0, java.lang.Integer.MAX_VALUE, diagnosisSequence));
          children.add(new Property("procedureSequence", "positiveInt", "Procedures applicable for this service or product.", 0, java.lang.Integer.MAX_VALUE, procedureSequence));
          children.add(new Property("informationSequence", "positiveInt", "Exceptions, special conditions and supporting information applicable for this service or product.", 0, java.lang.Integer.MAX_VALUE, informationSequence));
          children.add(new Property("revenue", "CodeableConcept", "The type of revenue or cost center providing the product and/or service.", 0, 1, revenue));
          children.add(new Property("category", "CodeableConcept", "Code to identify the general type of benefits under which products and services are provided.", 0, 1, category));
          children.add(new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService));
          children.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier));
          children.add(new Property("programCode", "CodeableConcept", "Identifies the program under which this may be recovered.", 0, java.lang.Integer.MAX_VALUE, programCode));
          children.add(new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced));
          children.add(new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location));
          children.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice));
          children.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor));
          children.add(new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net));
          children.add(new Property("udi", "Reference(Device)", "Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          children.add(new Property("bodySite", "CodeableConcept", "Physical service site on the patient (limb, tooth, etc.).", 0, 1, bodySite));
          children.add(new Property("subSite", "CodeableConcept", "A region or surface of the bodySite, e.g. limb region or tooth surface(s).", 0, java.lang.Integer.MAX_VALUE, subSite));
          children.add(new Property("encounter", "Reference(Encounter)", "The Encounters during which this Claim was created or to which the creation of this record is tightly associated.", 0, java.lang.Integer.MAX_VALUE, encounter));
          children.add(new Property("detail", "", "A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify item entries.", 0, 1, sequence);
          case 1070083823: /*careTeamSequence*/  return new Property("careTeamSequence", "positiveInt", "CareTeam members related to this service or product.", 0, java.lang.Integer.MAX_VALUE, careTeamSequence);
          case -909769262: /*diagnosisSequence*/  return new Property("diagnosisSequence", "positiveInt", "Diagnosis applicable for this service or product.", 0, java.lang.Integer.MAX_VALUE, diagnosisSequence);
          case -808920140: /*procedureSequence*/  return new Property("procedureSequence", "positiveInt", "Procedures applicable for this service or product.", 0, java.lang.Integer.MAX_VALUE, procedureSequence);
          case -702585587: /*informationSequence*/  return new Property("informationSequence", "positiveInt", "Exceptions, special conditions and supporting information applicable for this service or product.", 0, java.lang.Integer.MAX_VALUE, informationSequence);
          case 1099842588: /*revenue*/  return new Property("revenue", "CodeableConcept", "The type of revenue or cost center providing the product and/or service.", 0, 1, revenue);
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "Code to identify the general type of benefits under which products and services are provided.", 0, 1, category);
          case 1957227299: /*productOrService*/  return new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService);
          case -615513385: /*modifier*/  return new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier);
          case 1010065041: /*programCode*/  return new Property("programCode", "CodeableConcept", "Identifies the program under which this may be recovered.", 0, java.lang.Integer.MAX_VALUE, programCode);
          case -1927922223: /*serviced[x]*/  return new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced);
          case 1379209295: /*serviced*/  return new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced);
          case 363246749: /*servicedDate*/  return new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced);
          case 1534966512: /*servicedPeriod*/  return new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced);
          case 552316075: /*location[x]*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case 1901043637: /*location*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case -1224800468: /*locationCodeableConcept*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case -1280020865: /*locationAddress*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case 755866390: /*locationReference*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice);
          case -1282148017: /*factor*/  return new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor);
          case 108957: /*net*/  return new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net);
          case 115642: /*udi*/  return new Property("udi", "Reference(Device)", "Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi);
          case 1702620169: /*bodySite*/  return new Property("bodySite", "CodeableConcept", "Physical service site on the patient (limb, tooth, etc.).", 0, 1, bodySite);
          case -1868566105: /*subSite*/  return new Property("subSite", "CodeableConcept", "A region or surface of the bodySite, e.g. limb region or tooth surface(s).", 0, java.lang.Integer.MAX_VALUE, subSite);
          case 1524132147: /*encounter*/  return new Property("encounter", "Reference(Encounter)", "The Encounters during which this Claim was created or to which the creation of this record is tightly associated.", 0, java.lang.Integer.MAX_VALUE, encounter);
          case -1335224239: /*detail*/  return new Property("detail", "", "A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.", 0, java.lang.Integer.MAX_VALUE, detail);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1070083823: /*careTeamSequence*/ return this.careTeamSequence == null ? new Base[0] : this.careTeamSequence.toArray(new Base[this.careTeamSequence.size()]); // PositiveIntType
        case -909769262: /*diagnosisSequence*/ return this.diagnosisSequence == null ? new Base[0] : this.diagnosisSequence.toArray(new Base[this.diagnosisSequence.size()]); // PositiveIntType
        case -808920140: /*procedureSequence*/ return this.procedureSequence == null ? new Base[0] : this.procedureSequence.toArray(new Base[this.procedureSequence.size()]); // PositiveIntType
        case -702585587: /*informationSequence*/ return this.informationSequence == null ? new Base[0] : this.informationSequence.toArray(new Base[this.informationSequence.size()]); // PositiveIntType
        case 1099842588: /*revenue*/ return this.revenue == null ? new Base[0] : new Base[] {this.revenue}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1957227299: /*productOrService*/ return this.productOrService == null ? new Base[0] : new Base[] {this.productOrService}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // CodeableConcept
        case 1379209295: /*serviced*/ return this.serviced == null ? new Base[0] : new Base[] {this.serviced}; // Type
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Type
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // CodeableConcept
        case -1868566105: /*subSite*/ return this.subSite == null ? new Base[0] : this.subSite.toArray(new Base[this.subSite.size()]); // CodeableConcept
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : this.encounter.toArray(new Base[this.encounter.size()]); // Reference
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
        case 1070083823: // careTeamSequence
          this.getCareTeamSequence().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -909769262: // diagnosisSequence
          this.getDiagnosisSequence().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -808920140: // procedureSequence
          this.getProcedureSequence().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -702585587: // informationSequence
          this.getInformationSequence().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case 1099842588: // revenue
          this.revenue = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1957227299: // productOrService
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
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
          this.quantity = castToQuantity(value); // Quantity
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
        } else if (name.equals("careTeamSequence")) {
          this.getCareTeamSequence().add(castToPositiveInt(value));
        } else if (name.equals("diagnosisSequence")) {
          this.getDiagnosisSequence().add(castToPositiveInt(value));
        } else if (name.equals("procedureSequence")) {
          this.getProcedureSequence().add(castToPositiveInt(value));
        } else if (name.equals("informationSequence")) {
          this.getInformationSequence().add(castToPositiveInt(value));
        } else if (name.equals("revenue")) {
          this.revenue = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("productOrService")) {
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("programCode")) {
          this.getProgramCode().add(castToCodeableConcept(value));
        } else if (name.equals("serviced[x]")) {
          this.serviced = castToType(value); // Type
        } else if (name.equals("location[x]")) {
          this.location = castToType(value); // Type
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
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
        case 1070083823:  return addCareTeamSequenceElement();
        case -909769262:  return addDiagnosisSequenceElement();
        case -808920140:  return addProcedureSequenceElement();
        case -702585587:  return addInformationSequenceElement();
        case 1099842588:  return getRevenue(); 
        case 50511102:  return getCategory(); 
        case 1957227299:  return getProductOrService(); 
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
        case -1335224239:  return addDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 1070083823: /*careTeamSequence*/ return new String[] {"positiveInt"};
        case -909769262: /*diagnosisSequence*/ return new String[] {"positiveInt"};
        case -808920140: /*procedureSequence*/ return new String[] {"positiveInt"};
        case -702585587: /*informationSequence*/ return new String[] {"positiveInt"};
        case 1099842588: /*revenue*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1957227299: /*productOrService*/ return new String[] {"CodeableConcept"};
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
        case -1335224239: /*detail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
        }
        else if (name.equals("careTeamSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.careTeamSequence");
        }
        else if (name.equals("diagnosisSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.diagnosisSequence");
        }
        else if (name.equals("procedureSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.procedureSequence");
        }
        else if (name.equals("informationSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.informationSequence");
        }
        else if (name.equals("revenue")) {
          this.revenue = new CodeableConcept();
          return this.revenue;
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("productOrService")) {
          this.productOrService = new CodeableConcept();
          return this.productOrService;
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
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.factor");
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
        if (careTeamSequence != null) {
          dst.careTeamSequence = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : careTeamSequence)
            dst.careTeamSequence.add(i.copy());
        };
        if (diagnosisSequence != null) {
          dst.diagnosisSequence = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : diagnosisSequence)
            dst.diagnosisSequence.add(i.copy());
        };
        if (procedureSequence != null) {
          dst.procedureSequence = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : procedureSequence)
            dst.procedureSequence.add(i.copy());
        };
        if (informationSequence != null) {
          dst.informationSequence = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : informationSequence)
            dst.informationSequence.add(i.copy());
        };
        dst.revenue = revenue == null ? null : revenue.copy();
        dst.category = category == null ? null : category.copy();
        dst.productOrService = productOrService == null ? null : productOrService.copy();
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
        if (detail != null) {
          dst.detail = new ArrayList<DetailComponent>();
          for (DetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ItemComponent))
          return false;
        ItemComponent o = (ItemComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(careTeamSequence, o.careTeamSequence, true)
           && compareDeep(diagnosisSequence, o.diagnosisSequence, true) && compareDeep(procedureSequence, o.procedureSequence, true)
           && compareDeep(informationSequence, o.informationSequence, true) && compareDeep(revenue, o.revenue, true)
           && compareDeep(category, o.category, true) && compareDeep(productOrService, o.productOrService, true)
           && compareDeep(modifier, o.modifier, true) && compareDeep(programCode, o.programCode, true) && compareDeep(serviced, o.serviced, true)
           && compareDeep(location, o.location, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(subSite, o.subSite, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ItemComponent))
          return false;
        ItemComponent o = (ItemComponent) other_;
        return compareValues(sequence, o.sequence, true) && compareValues(careTeamSequence, o.careTeamSequence, true)
           && compareValues(diagnosisSequence, o.diagnosisSequence, true) && compareValues(procedureSequence, o.procedureSequence, true)
           && compareValues(informationSequence, o.informationSequence, true) && compareValues(factor, o.factor, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, careTeamSequence
          , diagnosisSequence, procedureSequence, informationSequence, revenue, category, productOrService
          , modifier, programCode, serviced, location, quantity, unitPrice, factor, net
          , udi, bodySite, subSite, encounter, detail);
      }

  public String fhirType() {
    return "Claim.item";

  }

  }

    @Block()
    public static class DetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify item entries.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Item instance identifier", formalDefinition="A number to uniquely identify item entries." )
        protected PositiveIntType sequence;

        /**
         * The type of revenue or cost center providing the product and/or service.
         */
        @Child(name = "revenue", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Revenue or cost center code", formalDefinition="The type of revenue or cost center providing the product and/or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-revenue-center")
        protected CodeableConcept revenue;

        /**
         * Code to identify the general type of benefits under which products and services are provided.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Benefit classification", formalDefinition="Code to identify the general type of benefits under which products and services are provided." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-benefitcategory")
        protected CodeableConcept category;

        /**
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.
         */
        @Child(name = "productOrService", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing, service, product, or drug code", formalDefinition="When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept productOrService;

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes to convey additional context for the product or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * Identifies the program under which this may be recovered.
         */
        @Child(name = "programCode", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Program the product or service is provided under", formalDefinition="Identifies the program under which this may be recovered." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-program-code")
        protected List<CodeableConcept> programCode;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of products or services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per item", formalDefinition="If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * The quantity times the unit price for an additional service or product or charge.
         */
        @Child(name = "net", type = {Money.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an additional service or product or charge." )
        protected Money net;

        /**
         * Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Unique device identifier", formalDefinition="Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        /**
         * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
         */
        @Child(name = "subDetail", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Product or service provided", formalDefinition="A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items." )
        protected List<SubDetailComponent> subDetail;

        private static final long serialVersionUID = -1245004924L;

    /**
     * Constructor
     */
      public DetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DetailComponent(PositiveIntType sequence, CodeableConcept productOrService) {
        super();
        this.sequence = sequence;
        this.productOrService = productOrService;
      }

        /**
         * @return {@link #sequence} (A number to uniquely identify item entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
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
         * @param value {@link #sequence} (A number to uniquely identify item entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public DetailComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify item entries.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify item entries.
         */
        public DetailComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #revenue} (The type of revenue or cost center providing the product and/or service.)
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
         * @param value {@link #revenue} (The type of revenue or cost center providing the product and/or service.)
         */
        public DetailComponent setRevenue(CodeableConcept value) { 
          this.revenue = value;
          return this;
        }

        /**
         * @return {@link #category} (Code to identify the general type of benefits under which products and services are provided.)
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
         * @param value {@link #category} (Code to identify the general type of benefits under which products and services are provided.)
         */
        public DetailComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public CodeableConcept getProductOrService() { 
          if (this.productOrService == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.productOrService");
            else if (Configuration.doAutoCreate())
              this.productOrService = new CodeableConcept(); // cc
          return this.productOrService;
        }

        public boolean hasProductOrService() { 
          return this.productOrService != null && !this.productOrService.isEmpty();
        }

        /**
         * @param value {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public DetailComponent setProductOrService(CodeableConcept value) { 
          this.productOrService = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes to convey additional context for the product or service.)
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
         * @return {@link #programCode} (Identifies the program under which this may be recovered.)
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
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public DetailComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
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
         * @param value {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
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
         * @return {@link #net} (The quantity times the unit price for an additional service or product or charge.)
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
         * @param value {@link #net} (The quantity times the unit price for an additional service or product or charge.)
         */
        public DetailComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (Unique Device Identifiers associated with this line item.)
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
         * @return {@link #subDetail} (A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.)
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify item entries.", 0, 1, sequence));
          children.add(new Property("revenue", "CodeableConcept", "The type of revenue or cost center providing the product and/or service.", 0, 1, revenue));
          children.add(new Property("category", "CodeableConcept", "Code to identify the general type of benefits under which products and services are provided.", 0, 1, category));
          children.add(new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService));
          children.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier));
          children.add(new Property("programCode", "CodeableConcept", "Identifies the program under which this may be recovered.", 0, java.lang.Integer.MAX_VALUE, programCode));
          children.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice));
          children.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor));
          children.add(new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net));
          children.add(new Property("udi", "Reference(Device)", "Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          children.add(new Property("subDetail", "", "A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.", 0, java.lang.Integer.MAX_VALUE, subDetail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify item entries.", 0, 1, sequence);
          case 1099842588: /*revenue*/  return new Property("revenue", "CodeableConcept", "The type of revenue or cost center providing the product and/or service.", 0, 1, revenue);
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "Code to identify the general type of benefits under which products and services are provided.", 0, 1, category);
          case 1957227299: /*productOrService*/  return new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService);
          case -615513385: /*modifier*/  return new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier);
          case 1010065041: /*programCode*/  return new Property("programCode", "CodeableConcept", "Identifies the program under which this may be recovered.", 0, java.lang.Integer.MAX_VALUE, programCode);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice);
          case -1282148017: /*factor*/  return new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor);
          case 108957: /*net*/  return new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net);
          case 115642: /*udi*/  return new Property("udi", "Reference(Device)", "Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi);
          case -828829007: /*subDetail*/  return new Property("subDetail", "", "A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are simple items.", 0, java.lang.Integer.MAX_VALUE, subDetail);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1099842588: /*revenue*/ return this.revenue == null ? new Base[0] : new Base[] {this.revenue}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1957227299: /*productOrService*/ return this.productOrService == null ? new Base[0] : new Base[] {this.productOrService}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
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
        case 1099842588: // revenue
          this.revenue = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1957227299: // productOrService
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1010065041: // programCode
          this.getProgramCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
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
        } else if (name.equals("revenue")) {
          this.revenue = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("productOrService")) {
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("programCode")) {
          this.getProgramCode().add(castToCodeableConcept(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else if (name.equals("udi")) {
          this.getUdi().add(castToReference(value));
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
        case 1099842588:  return getRevenue(); 
        case 50511102:  return getCategory(); 
        case 1957227299:  return getProductOrService(); 
        case -615513385:  return addModifier(); 
        case 1010065041:  return addProgramCode(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case 108957:  return getNet(); 
        case 115642:  return addUdi(); 
        case -828829007:  return addSubDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 1099842588: /*revenue*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1957227299: /*productOrService*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 1010065041: /*programCode*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        case 115642: /*udi*/ return new String[] {"Reference"};
        case -828829007: /*subDetail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
        }
        else if (name.equals("revenue")) {
          this.revenue = new CodeableConcept();
          return this.revenue;
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("productOrService")) {
          this.productOrService = new CodeableConcept();
          return this.productOrService;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("programCode")) {
          return addProgramCode();
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.factor");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          return addUdi();
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
        dst.revenue = revenue == null ? null : revenue.copy();
        dst.category = category == null ? null : category.copy();
        dst.productOrService = productOrService == null ? null : productOrService.copy();
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
        if (subDetail != null) {
          dst.subDetail = new ArrayList<SubDetailComponent>();
          for (SubDetailComponent i : subDetail)
            dst.subDetail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DetailComponent))
          return false;
        DetailComponent o = (DetailComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(revenue, o.revenue, true) && compareDeep(category, o.category, true)
           && compareDeep(productOrService, o.productOrService, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true)
           && compareDeep(subDetail, o.subDetail, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DetailComponent))
          return false;
        DetailComponent o = (DetailComponent) other_;
        return compareValues(sequence, o.sequence, true) && compareValues(factor, o.factor, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, revenue, category
          , productOrService, modifier, programCode, quantity, unitPrice, factor, net, udi
          , subDetail);
      }

  public String fhirType() {
    return "Claim.item.detail";

  }

  }

    @Block()
    public static class SubDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify item entries.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Item instance identifier", formalDefinition="A number to uniquely identify item entries." )
        protected PositiveIntType sequence;

        /**
         * The type of revenue or cost center providing the product and/or service.
         */
        @Child(name = "revenue", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Revenue or cost center code", formalDefinition="The type of revenue or cost center providing the product and/or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-revenue-center")
        protected CodeableConcept revenue;

        /**
         * Code to identify the general type of benefits under which products and services are provided.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Benefit classification", formalDefinition="Code to identify the general type of benefits under which products and services are provided." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-benefitcategory")
        protected CodeableConcept category;

        /**
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.
         */
        @Child(name = "productOrService", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing, service, product, or drug code", formalDefinition="When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept productOrService;

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes to convey additional context for the product or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * Identifies the program under which this may be recovered.
         */
        @Child(name = "programCode", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Program the product or service is provided under", formalDefinition="Identifies the program under which this may be recovered." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-program-code")
        protected List<CodeableConcept> programCode;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of products or services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per item", formalDefinition="If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * The quantity times the unit price for an additional service or product or charge.
         */
        @Child(name = "net", type = {Money.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an additional service or product or charge." )
        protected Money net;

        /**
         * Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Unique device identifier", formalDefinition="Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        private static final long serialVersionUID = 1133026301L;

    /**
     * Constructor
     */
      public SubDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubDetailComponent(PositiveIntType sequence, CodeableConcept productOrService) {
        super();
        this.sequence = sequence;
        this.productOrService = productOrService;
      }

        /**
         * @return {@link #sequence} (A number to uniquely identify item entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
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
         * @param value {@link #sequence} (A number to uniquely identify item entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public SubDetailComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify item entries.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify item entries.
         */
        public SubDetailComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #revenue} (The type of revenue or cost center providing the product and/or service.)
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
         * @param value {@link #revenue} (The type of revenue or cost center providing the product and/or service.)
         */
        public SubDetailComponent setRevenue(CodeableConcept value) { 
          this.revenue = value;
          return this;
        }

        /**
         * @return {@link #category} (Code to identify the general type of benefits under which products and services are provided.)
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
         * @param value {@link #category} (Code to identify the general type of benefits under which products and services are provided.)
         */
        public SubDetailComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public CodeableConcept getProductOrService() { 
          if (this.productOrService == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.productOrService");
            else if (Configuration.doAutoCreate())
              this.productOrService = new CodeableConcept(); // cc
          return this.productOrService;
        }

        public boolean hasProductOrService() { 
          return this.productOrService != null && !this.productOrService.isEmpty();
        }

        /**
         * @param value {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public SubDetailComponent setProductOrService(CodeableConcept value) { 
          this.productOrService = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes to convey additional context for the product or service.)
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
         * @return {@link #programCode} (Identifies the program under which this may be recovered.)
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
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public SubDetailComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
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
         * @param value {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
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
         * @return {@link #net} (The quantity times the unit price for an additional service or product or charge.)
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
         * @param value {@link #net} (The quantity times the unit price for an additional service or product or charge.)
         */
        public SubDetailComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (Unique Device Identifiers associated with this line item.)
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify item entries.", 0, 1, sequence));
          children.add(new Property("revenue", "CodeableConcept", "The type of revenue or cost center providing the product and/or service.", 0, 1, revenue));
          children.add(new Property("category", "CodeableConcept", "Code to identify the general type of benefits under which products and services are provided.", 0, 1, category));
          children.add(new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService));
          children.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier));
          children.add(new Property("programCode", "CodeableConcept", "Identifies the program under which this may be recovered.", 0, java.lang.Integer.MAX_VALUE, programCode));
          children.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice));
          children.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor));
          children.add(new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net));
          children.add(new Property("udi", "Reference(Device)", "Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify item entries.", 0, 1, sequence);
          case 1099842588: /*revenue*/  return new Property("revenue", "CodeableConcept", "The type of revenue or cost center providing the product and/or service.", 0, 1, revenue);
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "Code to identify the general type of benefits under which products and services are provided.", 0, 1, category);
          case 1957227299: /*productOrService*/  return new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService);
          case -615513385: /*modifier*/  return new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier);
          case 1010065041: /*programCode*/  return new Property("programCode", "CodeableConcept", "Identifies the program under which this may be recovered.", 0, java.lang.Integer.MAX_VALUE, programCode);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice);
          case -1282148017: /*factor*/  return new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor);
          case 108957: /*net*/  return new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net);
          case 115642: /*udi*/  return new Property("udi", "Reference(Device)", "Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1099842588: /*revenue*/ return this.revenue == null ? new Base[0] : new Base[] {this.revenue}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1957227299: /*productOrService*/ return this.productOrService == null ? new Base[0] : new Base[] {this.productOrService}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 1099842588: // revenue
          this.revenue = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1957227299: // productOrService
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1010065041: // programCode
          this.getProgramCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
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
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("revenue")) {
          this.revenue = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("productOrService")) {
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("programCode")) {
          this.getProgramCode().add(castToCodeableConcept(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else if (name.equals("udi")) {
          this.getUdi().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 1099842588:  return getRevenue(); 
        case 50511102:  return getCategory(); 
        case 1957227299:  return getProductOrService(); 
        case -615513385:  return addModifier(); 
        case 1010065041:  return addProgramCode(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case 108957:  return getNet(); 
        case 115642:  return addUdi(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 1099842588: /*revenue*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1957227299: /*productOrService*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 1010065041: /*programCode*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        case 115642: /*udi*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
        }
        else if (name.equals("revenue")) {
          this.revenue = new CodeableConcept();
          return this.revenue;
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("productOrService")) {
          this.productOrService = new CodeableConcept();
          return this.productOrService;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("programCode")) {
          return addProgramCode();
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.factor");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          return addUdi();
        }
        else
          return super.addChild(name);
      }

      public SubDetailComponent copy() {
        SubDetailComponent dst = new SubDetailComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.revenue = revenue == null ? null : revenue.copy();
        dst.category = category == null ? null : category.copy();
        dst.productOrService = productOrService == null ? null : productOrService.copy();
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
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubDetailComponent))
          return false;
        SubDetailComponent o = (SubDetailComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(revenue, o.revenue, true) && compareDeep(category, o.category, true)
           && compareDeep(productOrService, o.productOrService, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubDetailComponent))
          return false;
        SubDetailComponent o = (SubDetailComponent) other_;
        return compareValues(sequence, o.sequence, true) && compareValues(factor, o.factor, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, revenue, category
          , productOrService, modifier, programCode, quantity, unitPrice, factor, net, udi
          );
      }

  public String fhirType() {
    return "Claim.item.detail.subDetail";

  }

  }

    /**
     * A unique identifier assigned to this claim.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier for claim", formalDefinition="A unique identifier assigned to this claim." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<ClaimStatus> status;

    /**
     * The category of claim, e.g. oral, pharmacy, vision, institutional, professional.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Category or discipline", formalDefinition="The category of claim, e.g. oral, pharmacy, vision, institutional, professional." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-type")
    protected CodeableConcept type;

    /**
     * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.
     */
    @Child(name = "subType", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="More granular claim type", formalDefinition="A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-subtype")
    protected CodeableConcept subType;

    /**
     * A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.
     */
    @Child(name = "use", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="claim | preauthorization | predetermination", formalDefinition="A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-use")
    protected Enumeration<Use> use;

    /**
     * The party to whom the professional services and/or products have been supplied or are being considered and for whom actual or forecast reimbursement is sought.
     */
    @Child(name = "patient", type = {Patient.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The recipient of the products and services", formalDefinition="The party to whom the professional services and/or products have been supplied or are being considered and for whom actual or forecast reimbursement is sought." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual or forecast reimbursement is sought.)
     */
    protected Patient patientTarget;

    /**
     * The period for which charges are being submitted.
     */
    @Child(name = "billablePeriod", type = {Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Relevant time frame for the claim", formalDefinition="The period for which charges are being submitted." )
    protected Period billablePeriod;

    /**
     * The date this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Resource creation date", formalDefinition="The date this resource was created." )
    protected DateTimeType created;

    /**
     * Individual who created the claim, predetermination or preauthorization.
     */
    @Child(name = "enterer", type = {Practitioner.class, PractitionerRole.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Author of the claim", formalDefinition="Individual who created the claim, predetermination or preauthorization." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (Individual who created the claim, predetermination or preauthorization.)
     */
    protected Resource entererTarget;

    /**
     * The Insurer who is target of the request.
     */
    @Child(name = "insurer", type = {Organization.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Target", formalDefinition="The Insurer who is target of the request." )
    protected Reference insurer;

    /**
     * The actual object that is the target of the reference (The Insurer who is target of the request.)
     */
    protected Organization insurerTarget;

    /**
     * The provider which is responsible for the claim, predetermination or preauthorization.
     */
    @Child(name = "provider", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Party responsible for the claim", formalDefinition="The provider which is responsible for the claim, predetermination or preauthorization." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The provider which is responsible for the claim, predetermination or preauthorization.)
     */
    protected Resource providerTarget;

    /**
     * The provider-required urgency of processing the request. Typical values include: stat, routine deferred.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=11, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Desired processing ugency", formalDefinition="The provider-required urgency of processing the request. Typical values include: stat, routine deferred." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/process-priority")
    protected CodeableConcept priority;

    /**
     * A code to indicate whether and for whom funds are to be reserved for future claims.
     */
    @Child(name = "fundsReserve", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="For whom to reserve funds", formalDefinition="A code to indicate whether and for whom funds are to be reserved for future claims." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fundsreserve")
    protected CodeableConcept fundsReserve;

    /**
     * Other claims which are related to this claim such as prior submissions or claims for related services or for the same event.
     */
    @Child(name = "related", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Prior or corollary claims", formalDefinition="Other claims which are related to this claim such as prior submissions or claims for related services or for the same event." )
    protected List<RelatedClaimComponent> related;

    /**
     * Prescription to support the dispensing of pharmacy, device or vision products.
     */
    @Child(name = "prescription", type = {DeviceRequest.class, MedicationRequest.class, VisionPrescription.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Prescription authorizing services and products", formalDefinition="Prescription to support the dispensing of pharmacy, device or vision products." )
    protected Reference prescription;

    /**
     * The actual object that is the target of the reference (Prescription to support the dispensing of pharmacy, device or vision products.)
     */
    protected Resource prescriptionTarget;

    /**
     * Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, medications or products.
     */
    @Child(name = "originalPrescription", type = {DeviceRequest.class, MedicationRequest.class, VisionPrescription.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Original prescription if superseded by fulfiller", formalDefinition="Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, medications or products." )
    protected Reference originalPrescription;

    /**
     * The actual object that is the target of the reference (Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    protected Resource originalPrescriptionTarget;

    /**
     * The party to be reimbursed for cost of the products and services according to the terms of the policy.
     */
    @Child(name = "payee", type = {}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Recipient of benefits payable", formalDefinition="The party to be reimbursed for cost of the products and services according to the terms of the policy." )
    protected PayeeComponent payee;

    /**
     * A reference to a referral resource.
     */
    @Child(name = "referral", type = {ServiceRequest.class}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Treatment referral", formalDefinition="A reference to a referral resource." )
    protected Reference referral;

    /**
     * The actual object that is the target of the reference (A reference to a referral resource.)
     */
    protected ServiceRequest referralTarget;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Location.class}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Servicing facility", formalDefinition="Facility where the services were provided." )
    protected Reference facility;

    /**
     * The actual object that is the target of the reference (Facility where the services were provided.)
     */
    protected Location facilityTarget;

    /**
     * The members of the team who provided the products and services.
     */
    @Child(name = "careTeam", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Members of the care team", formalDefinition="The members of the team who provided the products and services." )
    protected List<CareTeamComponent> careTeam;

    /**
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues.
     */
    @Child(name = "supportingInfo", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Supporting information", formalDefinition="Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues." )
    protected List<SupportingInformationComponent> supportingInfo;

    /**
     * Information about diagnoses relevant to the claim items.
     */
    @Child(name = "diagnosis", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Pertinent diagnosis information", formalDefinition="Information about diagnoses relevant to the claim items." )
    protected List<DiagnosisComponent> diagnosis;

    /**
     * Procedures performed on the patient relevant to the billing items with the claim.
     */
    @Child(name = "procedure", type = {}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Clinical procedures performed", formalDefinition="Procedures performed on the patient relevant to the billing items with the claim." )
    protected List<ProcedureComponent> procedure;

    /**
     * Financial instruments for reimbursement for the health care products and services specified on the claim.
     */
    @Child(name = "insurance", type = {}, order=23, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Patient insurance information", formalDefinition="Financial instruments for reimbursement for the health care products and services specified on the claim." )
    protected List<InsuranceComponent> insurance;

    /**
     * Details of an accident which resulted in injuries which required the products and services listed in the claim.
     */
    @Child(name = "accident", type = {}, order=24, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details of the event", formalDefinition="Details of an accident which resulted in injuries which required the products and services listed in the claim." )
    protected AccidentComponent accident;

    /**
     * A claim line. Either a simple  product or service or a 'group' of details which can each be a simple items or groups of sub-details.
     */
    @Child(name = "item", type = {}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Product or service provided", formalDefinition="A claim line. Either a simple  product or service or a 'group' of details which can each be a simple items or groups of sub-details." )
    protected List<ItemComponent> item;

    /**
     * The total value of the all the items in the claim.
     */
    @Child(name = "total", type = {Money.class}, order=26, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Total claim cost", formalDefinition="The total value of the all the items in the claim." )
    protected Money total;

    private static final long serialVersionUID = -80376108L;

  /**
   * Constructor
   */
    public Claim() {
      super();
    }

  /**
   * Constructor
   */
    public Claim(Enumeration<ClaimStatus> status, CodeableConcept type, Enumeration<Use> use, Reference patient, DateTimeType created, Reference provider, CodeableConcept priority) {
      super();
      this.status = status;
      this.type = type;
      this.use = use;
      this.patient = patient;
      this.created = created;
      this.provider = provider;
      this.priority = priority;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this claim.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Claim setIdentifier(List<Identifier> theIdentifier) { 
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

    public Claim addIdentifier(Identifier t) { //3
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
    public Enumeration<ClaimStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ClaimStatus>(new ClaimStatusEnumFactory()); // bb
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
    public Claim setStatusElement(Enumeration<ClaimStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public ClaimStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public Claim setStatus(ClaimStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ClaimStatus>(new ClaimStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (The category of claim, e.g. oral, pharmacy, vision, institutional, professional.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The category of claim, e.g. oral, pharmacy, vision, institutional, professional.)
     */
    public Claim setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subType} (A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.)
     */
    public CodeableConcept getSubType() { 
      if (this.subType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.subType");
        else if (Configuration.doAutoCreate())
          this.subType = new CodeableConcept(); // cc
      return this.subType;
    }

    public boolean hasSubType() { 
      return this.subType != null && !this.subType.isEmpty();
    }

    /**
     * @param value {@link #subType} (A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.)
     */
    public Claim setSubType(CodeableConcept value) { 
      this.subType = value;
      return this;
    }

    /**
     * @return {@link #use} (A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
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
     * @param value {@link #use} (A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Claim setUseElement(Enumeration<Use> value) { 
      this.use = value;
      return this;
    }

    /**
     * @return A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.
     */
    public Use getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.
     */
    public Claim setUse(Use value) { 
        if (this.use == null)
          this.use = new Enumeration<Use>(new UseEnumFactory());
        this.use.setValue(value);
      return this;
    }

    /**
     * @return {@link #patient} (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual or forecast reimbursement is sought.)
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
     * @param value {@link #patient} (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual or forecast reimbursement is sought.)
     */
    public Claim setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual or forecast reimbursement is sought.)
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
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual or forecast reimbursement is sought.)
     */
    public Claim setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #billablePeriod} (The period for which charges are being submitted.)
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
     * @param value {@link #billablePeriod} (The period for which charges are being submitted.)
     */
    public Claim setBillablePeriod(Period value) { 
      this.billablePeriod = value;
      return this;
    }

    /**
     * @return {@link #created} (The date this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
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
     * @param value {@link #created} (The date this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public Claim setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date this resource was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date this resource was created.
     */
    public Claim setCreated(Date value) { 
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      return this;
    }

    /**
     * @return {@link #enterer} (Individual who created the claim, predetermination or preauthorization.)
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
     * @param value {@link #enterer} (Individual who created the claim, predetermination or preauthorization.)
     */
    public Claim setEnterer(Reference value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #enterer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Individual who created the claim, predetermination or preauthorization.)
     */
    public Resource getEntererTarget() { 
      return this.entererTarget;
    }

    /**
     * @param value {@link #enterer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Individual who created the claim, predetermination or preauthorization.)
     */
    public Claim setEntererTarget(Resource value) { 
      this.entererTarget = value;
      return this;
    }

    /**
     * @return {@link #insurer} (The Insurer who is target of the request.)
     */
    public Reference getInsurer() { 
      if (this.insurer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.insurer");
        else if (Configuration.doAutoCreate())
          this.insurer = new Reference(); // cc
      return this.insurer;
    }

    public boolean hasInsurer() { 
      return this.insurer != null && !this.insurer.isEmpty();
    }

    /**
     * @param value {@link #insurer} (The Insurer who is target of the request.)
     */
    public Claim setInsurer(Reference value) { 
      this.insurer = value;
      return this;
    }

    /**
     * @return {@link #insurer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who is target of the request.)
     */
    public Organization getInsurerTarget() { 
      if (this.insurerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.insurer");
        else if (Configuration.doAutoCreate())
          this.insurerTarget = new Organization(); // aa
      return this.insurerTarget;
    }

    /**
     * @param value {@link #insurer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who is target of the request.)
     */
    public Claim setInsurerTarget(Organization value) { 
      this.insurerTarget = value;
      return this;
    }

    /**
     * @return {@link #provider} (The provider which is responsible for the claim, predetermination or preauthorization.)
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
     * @param value {@link #provider} (The provider which is responsible for the claim, predetermination or preauthorization.)
     */
    public Claim setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the claim, predetermination or preauthorization.)
     */
    public Resource getProviderTarget() { 
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the claim, predetermination or preauthorization.)
     */
    public Claim setProviderTarget(Resource value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #priority} (The provider-required urgency of processing the request. Typical values include: stat, routine deferred.)
     */
    public CodeableConcept getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new CodeableConcept(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (The provider-required urgency of processing the request. Typical values include: stat, routine deferred.)
     */
    public Claim setPriority(CodeableConcept value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #fundsReserve} (A code to indicate whether and for whom funds are to be reserved for future claims.)
     */
    public CodeableConcept getFundsReserve() { 
      if (this.fundsReserve == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.fundsReserve");
        else if (Configuration.doAutoCreate())
          this.fundsReserve = new CodeableConcept(); // cc
      return this.fundsReserve;
    }

    public boolean hasFundsReserve() { 
      return this.fundsReserve != null && !this.fundsReserve.isEmpty();
    }

    /**
     * @param value {@link #fundsReserve} (A code to indicate whether and for whom funds are to be reserved for future claims.)
     */
    public Claim setFundsReserve(CodeableConcept value) { 
      this.fundsReserve = value;
      return this;
    }

    /**
     * @return {@link #related} (Other claims which are related to this claim such as prior submissions or claims for related services or for the same event.)
     */
    public List<RelatedClaimComponent> getRelated() { 
      if (this.related == null)
        this.related = new ArrayList<RelatedClaimComponent>();
      return this.related;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Claim setRelated(List<RelatedClaimComponent> theRelated) { 
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

    public Claim addRelated(RelatedClaimComponent t) { //3
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
     * @return {@link #prescription} (Prescription to support the dispensing of pharmacy, device or vision products.)
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
     * @param value {@link #prescription} (Prescription to support the dispensing of pharmacy, device or vision products.)
     */
    public Claim setPrescription(Reference value) { 
      this.prescription = value;
      return this;
    }

    /**
     * @return {@link #prescription} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Prescription to support the dispensing of pharmacy, device or vision products.)
     */
    public Resource getPrescriptionTarget() { 
      return this.prescriptionTarget;
    }

    /**
     * @param value {@link #prescription} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Prescription to support the dispensing of pharmacy, device or vision products.)
     */
    public Claim setPrescriptionTarget(Resource value) { 
      this.prescriptionTarget = value;
      return this;
    }

    /**
     * @return {@link #originalPrescription} (Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, medications or products.)
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
     * @param value {@link #originalPrescription} (Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Claim setOriginalPrescription(Reference value) { 
      this.originalPrescription = value;
      return this;
    }

    /**
     * @return {@link #originalPrescription} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Resource getOriginalPrescriptionTarget() { 
      return this.originalPrescriptionTarget;
    }

    /**
     * @param value {@link #originalPrescription} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Claim setOriginalPrescriptionTarget(Resource value) { 
      this.originalPrescriptionTarget = value;
      return this;
    }

    /**
     * @return {@link #payee} (The party to be reimbursed for cost of the products and services according to the terms of the policy.)
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
     * @param value {@link #payee} (The party to be reimbursed for cost of the products and services according to the terms of the policy.)
     */
    public Claim setPayee(PayeeComponent value) { 
      this.payee = value;
      return this;
    }

    /**
     * @return {@link #referral} (A reference to a referral resource.)
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
     * @param value {@link #referral} (A reference to a referral resource.)
     */
    public Claim setReferral(Reference value) { 
      this.referral = value;
      return this;
    }

    /**
     * @return {@link #referral} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a referral resource.)
     */
    public ServiceRequest getReferralTarget() { 
      if (this.referralTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.referral");
        else if (Configuration.doAutoCreate())
          this.referralTarget = new ServiceRequest(); // aa
      return this.referralTarget;
    }

    /**
     * @param value {@link #referral} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a referral resource.)
     */
    public Claim setReferralTarget(ServiceRequest value) { 
      this.referralTarget = value;
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
     * @return {@link #careTeam} (The members of the team who provided the products and services.)
     */
    public List<CareTeamComponent> getCareTeam() { 
      if (this.careTeam == null)
        this.careTeam = new ArrayList<CareTeamComponent>();
      return this.careTeam;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Claim setCareTeam(List<CareTeamComponent> theCareTeam) { 
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

    public Claim addCareTeam(CareTeamComponent t) { //3
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
     * @return {@link #supportingInfo} (Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues.)
     */
    public List<SupportingInformationComponent> getSupportingInfo() { 
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<SupportingInformationComponent>();
      return this.supportingInfo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Claim setSupportingInfo(List<SupportingInformationComponent> theSupportingInfo) { 
      this.supportingInfo = theSupportingInfo;
      return this;
    }

    public boolean hasSupportingInfo() { 
      if (this.supportingInfo == null)
        return false;
      for (SupportingInformationComponent item : this.supportingInfo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SupportingInformationComponent addSupportingInfo() { //3
      SupportingInformationComponent t = new SupportingInformationComponent();
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<SupportingInformationComponent>();
      this.supportingInfo.add(t);
      return t;
    }

    public Claim addSupportingInfo(SupportingInformationComponent t) { //3
      if (t == null)
        return this;
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<SupportingInformationComponent>();
      this.supportingInfo.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supportingInfo}, creating it if it does not already exist
     */
    public SupportingInformationComponent getSupportingInfoFirstRep() { 
      if (getSupportingInfo().isEmpty()) {
        addSupportingInfo();
      }
      return getSupportingInfo().get(0);
    }

    /**
     * @return {@link #diagnosis} (Information about diagnoses relevant to the claim items.)
     */
    public List<DiagnosisComponent> getDiagnosis() { 
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      return this.diagnosis;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Claim setDiagnosis(List<DiagnosisComponent> theDiagnosis) { 
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

    public Claim addDiagnosis(DiagnosisComponent t) { //3
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
     * @return {@link #procedure} (Procedures performed on the patient relevant to the billing items with the claim.)
     */
    public List<ProcedureComponent> getProcedure() { 
      if (this.procedure == null)
        this.procedure = new ArrayList<ProcedureComponent>();
      return this.procedure;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Claim setProcedure(List<ProcedureComponent> theProcedure) { 
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

    public Claim addProcedure(ProcedureComponent t) { //3
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
     * @return {@link #insurance} (Financial instruments for reimbursement for the health care products and services specified on the claim.)
     */
    public List<InsuranceComponent> getInsurance() { 
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      return this.insurance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Claim setInsurance(List<InsuranceComponent> theInsurance) { 
      this.insurance = theInsurance;
      return this;
    }

    public boolean hasInsurance() { 
      if (this.insurance == null)
        return false;
      for (InsuranceComponent item : this.insurance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public InsuranceComponent addInsurance() { //3
      InsuranceComponent t = new InsuranceComponent();
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      this.insurance.add(t);
      return t;
    }

    public Claim addInsurance(InsuranceComponent t) { //3
      if (t == null)
        return this;
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      this.insurance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #insurance}, creating it if it does not already exist
     */
    public InsuranceComponent getInsuranceFirstRep() { 
      if (getInsurance().isEmpty()) {
        addInsurance();
      }
      return getInsurance().get(0);
    }

    /**
     * @return {@link #accident} (Details of an accident which resulted in injuries which required the products and services listed in the claim.)
     */
    public AccidentComponent getAccident() { 
      if (this.accident == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Claim.accident");
        else if (Configuration.doAutoCreate())
          this.accident = new AccidentComponent(); // cc
      return this.accident;
    }

    public boolean hasAccident() { 
      return this.accident != null && !this.accident.isEmpty();
    }

    /**
     * @param value {@link #accident} (Details of an accident which resulted in injuries which required the products and services listed in the claim.)
     */
    public Claim setAccident(AccidentComponent value) { 
      this.accident = value;
      return this;
    }

    /**
     * @return {@link #item} (A claim line. Either a simple  product or service or a 'group' of details which can each be a simple items or groups of sub-details.)
     */
    public List<ItemComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<ItemComponent>();
      return this.item;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Claim setItem(List<ItemComponent> theItem) { 
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

    public Claim addItem(ItemComponent t) { //3
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
     * @return {@link #total} (The total value of the all the items in the claim.)
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
     * @param value {@link #total} (The total value of the all the items in the claim.)
     */
    public Claim setTotal(Money value) { 
      this.total = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A unique identifier assigned to this claim.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The status of the resource instance.", 0, 1, status));
        children.add(new Property("type", "CodeableConcept", "The category of claim, e.g. oral, pharmacy, vision, institutional, professional.", 0, 1, type));
        children.add(new Property("subType", "CodeableConcept", "A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.", 0, 1, subType));
        children.add(new Property("use", "code", "A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.", 0, 1, use));
        children.add(new Property("patient", "Reference(Patient)", "The party to whom the professional services and/or products have been supplied or are being considered and for whom actual or forecast reimbursement is sought.", 0, 1, patient));
        children.add(new Property("billablePeriod", "Period", "The period for which charges are being submitted.", 0, 1, billablePeriod));
        children.add(new Property("created", "dateTime", "The date this resource was created.", 0, 1, created));
        children.add(new Property("enterer", "Reference(Practitioner|PractitionerRole)", "Individual who created the claim, predetermination or preauthorization.", 0, 1, enterer));
        children.add(new Property("insurer", "Reference(Organization)", "The Insurer who is target of the request.", 0, 1, insurer));
        children.add(new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The provider which is responsible for the claim, predetermination or preauthorization.", 0, 1, provider));
        children.add(new Property("priority", "CodeableConcept", "The provider-required urgency of processing the request. Typical values include: stat, routine deferred.", 0, 1, priority));
        children.add(new Property("fundsReserve", "CodeableConcept", "A code to indicate whether and for whom funds are to be reserved for future claims.", 0, 1, fundsReserve));
        children.add(new Property("related", "", "Other claims which are related to this claim such as prior submissions or claims for related services or for the same event.", 0, java.lang.Integer.MAX_VALUE, related));
        children.add(new Property("prescription", "Reference(DeviceRequest|MedicationRequest|VisionPrescription)", "Prescription to support the dispensing of pharmacy, device or vision products.", 0, 1, prescription));
        children.add(new Property("originalPrescription", "Reference(DeviceRequest|MedicationRequest|VisionPrescription)", "Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, medications or products.", 0, 1, originalPrescription));
        children.add(new Property("payee", "", "The party to be reimbursed for cost of the products and services according to the terms of the policy.", 0, 1, payee));
        children.add(new Property("referral", "Reference(ServiceRequest)", "A reference to a referral resource.", 0, 1, referral));
        children.add(new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, 1, facility));
        children.add(new Property("careTeam", "", "The members of the team who provided the products and services.", 0, java.lang.Integer.MAX_VALUE, careTeam));
        children.add(new Property("supportingInfo", "", "Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues.", 0, java.lang.Integer.MAX_VALUE, supportingInfo));
        children.add(new Property("diagnosis", "", "Information about diagnoses relevant to the claim items.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        children.add(new Property("procedure", "", "Procedures performed on the patient relevant to the billing items with the claim.", 0, java.lang.Integer.MAX_VALUE, procedure));
        children.add(new Property("insurance", "", "Financial instruments for reimbursement for the health care products and services specified on the claim.", 0, java.lang.Integer.MAX_VALUE, insurance));
        children.add(new Property("accident", "", "Details of an accident which resulted in injuries which required the products and services listed in the claim.", 0, 1, accident));
        children.add(new Property("item", "", "A claim line. Either a simple  product or service or a 'group' of details which can each be a simple items or groups of sub-details.", 0, java.lang.Integer.MAX_VALUE, item));
        children.add(new Property("total", "Money", "The total value of the all the items in the claim.", 0, 1, total));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A unique identifier assigned to this claim.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the resource instance.", 0, 1, status);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The category of claim, e.g. oral, pharmacy, vision, institutional, professional.", 0, 1, type);
        case -1868521062: /*subType*/  return new Property("subType", "CodeableConcept", "A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.", 0, 1, subType);
        case 116103: /*use*/  return new Property("use", "code", "A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.", 0, 1, use);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "The party to whom the professional services and/or products have been supplied or are being considered and for whom actual or forecast reimbursement is sought.", 0, 1, patient);
        case -332066046: /*billablePeriod*/  return new Property("billablePeriod", "Period", "The period for which charges are being submitted.", 0, 1, billablePeriod);
        case 1028554472: /*created*/  return new Property("created", "dateTime", "The date this resource was created.", 0, 1, created);
        case -1591951995: /*enterer*/  return new Property("enterer", "Reference(Practitioner|PractitionerRole)", "Individual who created the claim, predetermination or preauthorization.", 0, 1, enterer);
        case 1957615864: /*insurer*/  return new Property("insurer", "Reference(Organization)", "The Insurer who is target of the request.", 0, 1, insurer);
        case -987494927: /*provider*/  return new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The provider which is responsible for the claim, predetermination or preauthorization.", 0, 1, provider);
        case -1165461084: /*priority*/  return new Property("priority", "CodeableConcept", "The provider-required urgency of processing the request. Typical values include: stat, routine deferred.", 0, 1, priority);
        case 1314609806: /*fundsReserve*/  return new Property("fundsReserve", "CodeableConcept", "A code to indicate whether and for whom funds are to be reserved for future claims.", 0, 1, fundsReserve);
        case 1090493483: /*related*/  return new Property("related", "", "Other claims which are related to this claim such as prior submissions or claims for related services or for the same event.", 0, java.lang.Integer.MAX_VALUE, related);
        case 460301338: /*prescription*/  return new Property("prescription", "Reference(DeviceRequest|MedicationRequest|VisionPrescription)", "Prescription to support the dispensing of pharmacy, device or vision products.", 0, 1, prescription);
        case -1814015861: /*originalPrescription*/  return new Property("originalPrescription", "Reference(DeviceRequest|MedicationRequest|VisionPrescription)", "Original prescription which has been superseded by this prescription to support the dispensing of pharmacy services, medications or products.", 0, 1, originalPrescription);
        case 106443592: /*payee*/  return new Property("payee", "", "The party to be reimbursed for cost of the products and services according to the terms of the policy.", 0, 1, payee);
        case -722568291: /*referral*/  return new Property("referral", "Reference(ServiceRequest)", "A reference to a referral resource.", 0, 1, referral);
        case 501116579: /*facility*/  return new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, 1, facility);
        case -7323378: /*careTeam*/  return new Property("careTeam", "", "The members of the team who provided the products and services.", 0, java.lang.Integer.MAX_VALUE, careTeam);
        case 1922406657: /*supportingInfo*/  return new Property("supportingInfo", "", "Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues.", 0, java.lang.Integer.MAX_VALUE, supportingInfo);
        case 1196993265: /*diagnosis*/  return new Property("diagnosis", "", "Information about diagnoses relevant to the claim items.", 0, java.lang.Integer.MAX_VALUE, diagnosis);
        case -1095204141: /*procedure*/  return new Property("procedure", "", "Procedures performed on the patient relevant to the billing items with the claim.", 0, java.lang.Integer.MAX_VALUE, procedure);
        case 73049818: /*insurance*/  return new Property("insurance", "", "Financial instruments for reimbursement for the health care products and services specified on the claim.", 0, java.lang.Integer.MAX_VALUE, insurance);
        case -2143202801: /*accident*/  return new Property("accident", "", "Details of an accident which resulted in injuries which required the products and services listed in the claim.", 0, 1, accident);
        case 3242771: /*item*/  return new Property("item", "", "A claim line. Either a simple  product or service or a 'group' of details which can each be a simple items or groups of sub-details.", 0, java.lang.Integer.MAX_VALUE, item);
        case 110549828: /*total*/  return new Property("total", "Money", "The total value of the all the items in the claim.", 0, 1, total);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ClaimStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : new Base[] {this.subType}; // CodeableConcept
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // Enumeration<Use>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -332066046: /*billablePeriod*/ return this.billablePeriod == null ? new Base[0] : new Base[] {this.billablePeriod}; // Period
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -1591951995: /*enterer*/ return this.enterer == null ? new Base[0] : new Base[] {this.enterer}; // Reference
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Reference
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case 1314609806: /*fundsReserve*/ return this.fundsReserve == null ? new Base[0] : new Base[] {this.fundsReserve}; // CodeableConcept
        case 1090493483: /*related*/ return this.related == null ? new Base[0] : this.related.toArray(new Base[this.related.size()]); // RelatedClaimComponent
        case 460301338: /*prescription*/ return this.prescription == null ? new Base[0] : new Base[] {this.prescription}; // Reference
        case -1814015861: /*originalPrescription*/ return this.originalPrescription == null ? new Base[0] : new Base[] {this.originalPrescription}; // Reference
        case 106443592: /*payee*/ return this.payee == null ? new Base[0] : new Base[] {this.payee}; // PayeeComponent
        case -722568291: /*referral*/ return this.referral == null ? new Base[0] : new Base[] {this.referral}; // Reference
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Reference
        case -7323378: /*careTeam*/ return this.careTeam == null ? new Base[0] : this.careTeam.toArray(new Base[this.careTeam.size()]); // CareTeamComponent
        case 1922406657: /*supportingInfo*/ return this.supportingInfo == null ? new Base[0] : this.supportingInfo.toArray(new Base[this.supportingInfo.size()]); // SupportingInformationComponent
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : this.diagnosis.toArray(new Base[this.diagnosis.size()]); // DiagnosisComponent
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : this.procedure.toArray(new Base[this.procedure.size()]); // ProcedureComponent
        case 73049818: /*insurance*/ return this.insurance == null ? new Base[0] : this.insurance.toArray(new Base[this.insurance.size()]); // InsuranceComponent
        case -2143202801: /*accident*/ return this.accident == null ? new Base[0] : new Base[] {this.accident}; // AccidentComponent
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // ItemComponent
        case 110549828: /*total*/ return this.total == null ? new Base[0] : new Base[] {this.total}; // Money
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
          value = new ClaimStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ClaimStatus>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868521062: // subType
          this.subType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 116103: // use
          value = new UseEnumFactory().fromType(castToCode(value));
          this.use = (Enumeration) value; // Enumeration<Use>
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
        case -1165461084: // priority
          this.priority = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1314609806: // fundsReserve
          this.fundsReserve = castToCodeableConcept(value); // CodeableConcept
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
        case -722568291: // referral
          this.referral = castToReference(value); // Reference
          return value;
        case 501116579: // facility
          this.facility = castToReference(value); // Reference
          return value;
        case -7323378: // careTeam
          this.getCareTeam().add((CareTeamComponent) value); // CareTeamComponent
          return value;
        case 1922406657: // supportingInfo
          this.getSupportingInfo().add((SupportingInformationComponent) value); // SupportingInformationComponent
          return value;
        case 1196993265: // diagnosis
          this.getDiagnosis().add((DiagnosisComponent) value); // DiagnosisComponent
          return value;
        case -1095204141: // procedure
          this.getProcedure().add((ProcedureComponent) value); // ProcedureComponent
          return value;
        case 73049818: // insurance
          this.getInsurance().add((InsuranceComponent) value); // InsuranceComponent
          return value;
        case -2143202801: // accident
          this.accident = (AccidentComponent) value; // AccidentComponent
          return value;
        case 3242771: // item
          this.getItem().add((ItemComponent) value); // ItemComponent
          return value;
        case 110549828: // total
          this.total = castToMoney(value); // Money
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ClaimStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ClaimStatus>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subType")) {
          this.subType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("use")) {
          value = new UseEnumFactory().fromType(castToCode(value));
          this.use = (Enumeration) value; // Enumeration<Use>
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
        } else if (name.equals("priority")) {
          this.priority = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("fundsReserve")) {
          this.fundsReserve = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("related")) {
          this.getRelated().add((RelatedClaimComponent) value);
        } else if (name.equals("prescription")) {
          this.prescription = castToReference(value); // Reference
        } else if (name.equals("originalPrescription")) {
          this.originalPrescription = castToReference(value); // Reference
        } else if (name.equals("payee")) {
          this.payee = (PayeeComponent) value; // PayeeComponent
        } else if (name.equals("referral")) {
          this.referral = castToReference(value); // Reference
        } else if (name.equals("facility")) {
          this.facility = castToReference(value); // Reference
        } else if (name.equals("careTeam")) {
          this.getCareTeam().add((CareTeamComponent) value);
        } else if (name.equals("supportingInfo")) {
          this.getSupportingInfo().add((SupportingInformationComponent) value);
        } else if (name.equals("diagnosis")) {
          this.getDiagnosis().add((DiagnosisComponent) value);
        } else if (name.equals("procedure")) {
          this.getProcedure().add((ProcedureComponent) value);
        } else if (name.equals("insurance")) {
          this.getInsurance().add((InsuranceComponent) value);
        } else if (name.equals("accident")) {
          this.accident = (AccidentComponent) value; // AccidentComponent
        } else if (name.equals("item")) {
          this.getItem().add((ItemComponent) value);
        } else if (name.equals("total")) {
          this.total = castToMoney(value); // Money
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
        case -1868521062:  return getSubType(); 
        case 116103:  return getUseElement();
        case -791418107:  return getPatient(); 
        case -332066046:  return getBillablePeriod(); 
        case 1028554472:  return getCreatedElement();
        case -1591951995:  return getEnterer(); 
        case 1957615864:  return getInsurer(); 
        case -987494927:  return getProvider(); 
        case -1165461084:  return getPriority(); 
        case 1314609806:  return getFundsReserve(); 
        case 1090493483:  return addRelated(); 
        case 460301338:  return getPrescription(); 
        case -1814015861:  return getOriginalPrescription(); 
        case 106443592:  return getPayee(); 
        case -722568291:  return getReferral(); 
        case 501116579:  return getFacility(); 
        case -7323378:  return addCareTeam(); 
        case 1922406657:  return addSupportingInfo(); 
        case 1196993265:  return addDiagnosis(); 
        case -1095204141:  return addProcedure(); 
        case 73049818:  return addInsurance(); 
        case -2143202801:  return getAccident(); 
        case 3242771:  return addItem(); 
        case 110549828:  return getTotal(); 
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
        case 116103: /*use*/ return new String[] {"code"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case -332066046: /*billablePeriod*/ return new String[] {"Period"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case -1591951995: /*enterer*/ return new String[] {"Reference"};
        case 1957615864: /*insurer*/ return new String[] {"Reference"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case -1165461084: /*priority*/ return new String[] {"CodeableConcept"};
        case 1314609806: /*fundsReserve*/ return new String[] {"CodeableConcept"};
        case 1090493483: /*related*/ return new String[] {};
        case 460301338: /*prescription*/ return new String[] {"Reference"};
        case -1814015861: /*originalPrescription*/ return new String[] {"Reference"};
        case 106443592: /*payee*/ return new String[] {};
        case -722568291: /*referral*/ return new String[] {"Reference"};
        case 501116579: /*facility*/ return new String[] {"Reference"};
        case -7323378: /*careTeam*/ return new String[] {};
        case 1922406657: /*supportingInfo*/ return new String[] {};
        case 1196993265: /*diagnosis*/ return new String[] {};
        case -1095204141: /*procedure*/ return new String[] {};
        case 73049818: /*insurance*/ return new String[] {};
        case -2143202801: /*accident*/ return new String[] {};
        case 3242771: /*item*/ return new String[] {};
        case 110549828: /*total*/ return new String[] {"Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.status");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          this.subType = new CodeableConcept();
          return this.subType;
        }
        else if (name.equals("use")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.use");
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
          throw new FHIRException("Cannot call addChild on a primitive type Claim.created");
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
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("fundsReserve")) {
          this.fundsReserve = new CodeableConcept();
          return this.fundsReserve;
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
        else if (name.equals("referral")) {
          this.referral = new Reference();
          return this.referral;
        }
        else if (name.equals("facility")) {
          this.facility = new Reference();
          return this.facility;
        }
        else if (name.equals("careTeam")) {
          return addCareTeam();
        }
        else if (name.equals("supportingInfo")) {
          return addSupportingInfo();
        }
        else if (name.equals("diagnosis")) {
          return addDiagnosis();
        }
        else if (name.equals("procedure")) {
          return addProcedure();
        }
        else if (name.equals("insurance")) {
          return addInsurance();
        }
        else if (name.equals("accident")) {
          this.accident = new AccidentComponent();
          return this.accident;
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else if (name.equals("total")) {
          this.total = new Money();
          return this.total;
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
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.subType = subType == null ? null : subType.copy();
        dst.use = use == null ? null : use.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.billablePeriod = billablePeriod == null ? null : billablePeriod.copy();
        dst.created = created == null ? null : created.copy();
        dst.enterer = enterer == null ? null : enterer.copy();
        dst.insurer = insurer == null ? null : insurer.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.fundsReserve = fundsReserve == null ? null : fundsReserve.copy();
        if (related != null) {
          dst.related = new ArrayList<RelatedClaimComponent>();
          for (RelatedClaimComponent i : related)
            dst.related.add(i.copy());
        };
        dst.prescription = prescription == null ? null : prescription.copy();
        dst.originalPrescription = originalPrescription == null ? null : originalPrescription.copy();
        dst.payee = payee == null ? null : payee.copy();
        dst.referral = referral == null ? null : referral.copy();
        dst.facility = facility == null ? null : facility.copy();
        if (careTeam != null) {
          dst.careTeam = new ArrayList<CareTeamComponent>();
          for (CareTeamComponent i : careTeam)
            dst.careTeam.add(i.copy());
        };
        if (supportingInfo != null) {
          dst.supportingInfo = new ArrayList<SupportingInformationComponent>();
          for (SupportingInformationComponent i : supportingInfo)
            dst.supportingInfo.add(i.copy());
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
        if (insurance != null) {
          dst.insurance = new ArrayList<InsuranceComponent>();
          for (InsuranceComponent i : insurance)
            dst.insurance.add(i.copy());
        };
        dst.accident = accident == null ? null : accident.copy();
        if (item != null) {
          dst.item = new ArrayList<ItemComponent>();
          for (ItemComponent i : item)
            dst.item.add(i.copy());
        };
        dst.total = total == null ? null : total.copy();
        return dst;
      }

      protected Claim typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Claim))
          return false;
        Claim o = (Claim) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(subType, o.subType, true) && compareDeep(use, o.use, true) && compareDeep(patient, o.patient, true)
           && compareDeep(billablePeriod, o.billablePeriod, true) && compareDeep(created, o.created, true)
           && compareDeep(enterer, o.enterer, true) && compareDeep(insurer, o.insurer, true) && compareDeep(provider, o.provider, true)
           && compareDeep(priority, o.priority, true) && compareDeep(fundsReserve, o.fundsReserve, true) && compareDeep(related, o.related, true)
           && compareDeep(prescription, o.prescription, true) && compareDeep(originalPrescription, o.originalPrescription, true)
           && compareDeep(payee, o.payee, true) && compareDeep(referral, o.referral, true) && compareDeep(facility, o.facility, true)
           && compareDeep(careTeam, o.careTeam, true) && compareDeep(supportingInfo, o.supportingInfo, true)
           && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(procedure, o.procedure, true) && compareDeep(insurance, o.insurance, true)
           && compareDeep(accident, o.accident, true) && compareDeep(item, o.item, true) && compareDeep(total, o.total, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Claim))
          return false;
        Claim o = (Claim) other_;
        return compareValues(status, o.status, true) && compareValues(use, o.use, true) && compareValues(created, o.created, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, type
          , subType, use, patient, billablePeriod, created, enterer, insurer, provider
          , priority, fundsReserve, related, prescription, originalPrescription, payee, referral
          , facility, careTeam, supportingInfo, diagnosis, procedure, insurance, accident
          , item, total);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Claim;
   }

 /**
   * Search parameter: <b>care-team</b>
   * <p>
   * Description: <b>Member of the CareTeam</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.careTeam.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="care-team", path="Claim.careTeam.provider", description="Member of the CareTeam", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class, PractitionerRole.class } )
  public static final String SP_CARE_TEAM = "care-team";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>care-team</b>
   * <p>
   * Description: <b>Member of the CareTeam</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.careTeam.provider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CARE_TEAM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CARE_TEAM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:care-team</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CARE_TEAM = new ca.uhn.fhir.model.api.Include("Claim:care-team").toLocked();

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
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Encounters associated with a billed line item</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.item.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="Claim.item.encounter", description="Encounters associated with a billed line item", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounters associated with a billed line item</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.item.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("Claim:encounter").toLocked();

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
   * Search parameter: <b>payee</b>
   * <p>
   * Description: <b>The party receiving any payment for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.payee.party</b><br>
   * </p>
   */
  @SearchParamDefinition(name="payee", path="Claim.payee.party", description="The party receiving any payment for the Claim", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_PAYEE = "payee";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>payee</b>
   * <p>
   * Description: <b>The party receiving any payment for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.payee.party</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PAYEE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PAYEE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:payee</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PAYEE = new ca.uhn.fhir.model.api.Include("Claim:payee").toLocked();

 /**
   * Search parameter: <b>provider</b>
   * <p>
   * Description: <b>Provider responsible for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provider", path="Claim.provider", description="Provider responsible for the Claim", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class, PractitionerRole.class } )
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
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Patient receiving the products or services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Claim.patient", description="Patient receiving the products or services", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Patient receiving the products or services</b><br>
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
   * Search parameter: <b>insurer</b>
   * <p>
   * Description: <b>The target payor/insurer for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.insurer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="insurer", path="Claim.insurer", description="The target payor/insurer for the Claim", type="reference", target={Organization.class } )
  public static final String SP_INSURER = "insurer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>insurer</b>
   * <p>
   * Description: <b>The target payor/insurer for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.insurer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INSURER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INSURER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:insurer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INSURER = new ca.uhn.fhir.model.api.Include("Claim:insurer").toLocked();

 /**
   * Search parameter: <b>detail-udi</b>
   * <p>
   * Description: <b>UDI associated with a line item, detail product or service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.item.detail.udi</b><br>
   * </p>
   */
  @SearchParamDefinition(name="detail-udi", path="Claim.item.detail.udi", description="UDI associated with a line item, detail product or service", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device") }, target={Device.class } )
  public static final String SP_DETAIL_UDI = "detail-udi";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>detail-udi</b>
   * <p>
   * Description: <b>UDI associated with a line item, detail product or service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.item.detail.udi</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DETAIL_UDI = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DETAIL_UDI);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:detail-udi</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DETAIL_UDI = new ca.uhn.fhir.model.api.Include("Claim:detail-udi").toLocked();

 /**
   * Search parameter: <b>enterer</b>
   * <p>
   * Description: <b>The party responsible for the entry of the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.enterer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="enterer", path="Claim.enterer", description="The party responsible for the entry of the Claim", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class, PractitionerRole.class } )
  public static final String SP_ENTERER = "enterer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>enterer</b>
   * <p>
   * Description: <b>The party responsible for the entry of the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.enterer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENTERER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENTERER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:enterer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENTERER = new ca.uhn.fhir.model.api.Include("Claim:enterer").toLocked();

 /**
   * Search parameter: <b>procedure-udi</b>
   * <p>
   * Description: <b>UDI associated with a procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.procedure.udi</b><br>
   * </p>
   */
  @SearchParamDefinition(name="procedure-udi", path="Claim.procedure.udi", description="UDI associated with a procedure", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device") }, target={Device.class } )
  public static final String SP_PROCEDURE_UDI = "procedure-udi";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>procedure-udi</b>
   * <p>
   * Description: <b>UDI associated with a procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.procedure.udi</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROCEDURE_UDI = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROCEDURE_UDI);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:procedure-udi</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROCEDURE_UDI = new ca.uhn.fhir.model.api.Include("Claim:procedure-udi").toLocked();

 /**
   * Search parameter: <b>subdetail-udi</b>
   * <p>
   * Description: <b>UDI associated with a line item, detail, subdetail product or service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.item.detail.subDetail.udi</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subdetail-udi", path="Claim.item.detail.subDetail.udi", description="UDI associated with a line item, detail, subdetail product or service", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device") }, target={Device.class } )
  public static final String SP_SUBDETAIL_UDI = "subdetail-udi";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subdetail-udi</b>
   * <p>
   * Description: <b>UDI associated with a line item, detail, subdetail product or service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.item.detail.subDetail.udi</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBDETAIL_UDI = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBDETAIL_UDI);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:subdetail-udi</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBDETAIL_UDI = new ca.uhn.fhir.model.api.Include("Claim:subdetail-udi").toLocked();

 /**
   * Search parameter: <b>facility</b>
   * <p>
   * Description: <b>Facility where the products or services have been or will be provided</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.facility</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facility", path="Claim.facility", description="Facility where the products or services have been or will be provided", type="reference", target={Location.class } )
  public static final String SP_FACILITY = "facility";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facility</b>
   * <p>
   * Description: <b>Facility where the products or services have been or will be provided</b><br>
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
   * Search parameter: <b>item-udi</b>
   * <p>
   * Description: <b>UDI associated with a line item product or service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.item.udi</b><br>
   * </p>
   */
  @SearchParamDefinition(name="item-udi", path="Claim.item.udi", description="UDI associated with a line item product or service", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device") }, target={Device.class } )
  public static final String SP_ITEM_UDI = "item-udi";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>item-udi</b>
   * <p>
   * Description: <b>UDI associated with a line item product or service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.item.udi</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ITEM_UDI = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ITEM_UDI);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:item-udi</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ITEM_UDI = new ca.uhn.fhir.model.api.Include("Claim:item-udi").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the Claim instance.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Claim.status", description="The status of the Claim instance.", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the Claim instance.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

