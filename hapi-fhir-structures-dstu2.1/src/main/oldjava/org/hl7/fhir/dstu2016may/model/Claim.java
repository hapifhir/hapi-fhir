package org.hl7.fhir.dstu2016may.model;

import java.math.BigDecimal;

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
    public static class RelatedClaimsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Other claims which are related to this claim such as prior claim versions or for related services.
         */
        @Child(name = "claim", type = {Identifier.class, Claim.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to the related claim", formalDefinition="Other claims which are related to this claim such as prior claim versions or for related services." )
        protected Type claim;

        /**
         * For example prior or umbrella.
         */
        @Child(name = "relationship", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How the reference claim is related", formalDefinition="For example prior or umbrella." )
        protected Coding relationship;

        /**
         * An alternate organizational reference to the case or file to which this particular claim pertains - eg Property/Casualy insurer claim # or Workers Compensation case # .
         */
        @Child(name = "reference", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Related file or case reference", formalDefinition="An alternate organizational reference to the case or file to which this particular claim pertains - eg Property/Casualy insurer claim # or Workers Compensation case # ." )
        protected Identifier reference;

        private static final long serialVersionUID = -2033217402L;

    /**
     * Constructor
     */
      public RelatedClaimsComponent() {
        super();
      }

        /**
         * @return {@link #claim} (Other claims which are related to this claim such as prior claim versions or for related services.)
         */
        public Type getClaim() { 
          return this.claim;
        }

        /**
         * @return {@link #claim} (Other claims which are related to this claim such as prior claim versions or for related services.)
         */
        public Identifier getClaimIdentifier() throws FHIRException { 
          if (!(this.claim instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.claim.getClass().getName()+" was encountered");
          return (Identifier) this.claim;
        }

        public boolean hasClaimIdentifier() { 
          return this.claim instanceof Identifier;
        }

        /**
         * @return {@link #claim} (Other claims which are related to this claim such as prior claim versions or for related services.)
         */
        public Reference getClaimReference() throws FHIRException { 
          if (!(this.claim instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.claim.getClass().getName()+" was encountered");
          return (Reference) this.claim;
        }

        public boolean hasClaimReference() { 
          return this.claim instanceof Reference;
        }

        public boolean hasClaim() { 
          return this.claim != null && !this.claim.isEmpty();
        }

        /**
         * @param value {@link #claim} (Other claims which are related to this claim such as prior claim versions or for related services.)
         */
        public RelatedClaimsComponent setClaim(Type value) { 
          this.claim = value;
          return this;
        }

        /**
         * @return {@link #relationship} (For example prior or umbrella.)
         */
        public Coding getRelationship() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RelatedClaimsComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new Coding(); // cc
          return this.relationship;
        }

        public boolean hasRelationship() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        /**
         * @param value {@link #relationship} (For example prior or umbrella.)
         */
        public RelatedClaimsComponent setRelationship(Coding value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return {@link #reference} (An alternate organizational reference to the case or file to which this particular claim pertains - eg Property/Casualy insurer claim # or Workers Compensation case # .)
         */
        public Identifier getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RelatedClaimsComponent.reference");
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
        public RelatedClaimsComponent setReference(Identifier value) { 
          this.reference = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("claim[x]", "Identifier|Reference(Claim)", "Other claims which are related to this claim such as prior claim versions or for related services.", 0, java.lang.Integer.MAX_VALUE, claim));
          childrenList.add(new Property("relationship", "Coding", "For example prior or umbrella.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("reference", "Identifier", "An alternate organizational reference to the case or file to which this particular claim pertains - eg Property/Casualy insurer claim # or Workers Compensation case # .", 0, java.lang.Integer.MAX_VALUE, reference));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 94742588: /*claim*/ return this.claim == null ? new Base[0] : new Base[] {this.claim}; // Type
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // Coding
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Identifier
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 94742588: // claim
          this.claim = (Type) value; // Type
          break;
        case -261851592: // relationship
          this.relationship = castToCoding(value); // Coding
          break;
        case -925155509: // reference
          this.reference = castToIdentifier(value); // Identifier
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("claim[x]"))
          this.claim = (Type) value; // Type
        else if (name.equals("relationship"))
          this.relationship = castToCoding(value); // Coding
        else if (name.equals("reference"))
          this.reference = castToIdentifier(value); // Identifier
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 683016900:  return getClaim(); // Type
        case -261851592:  return getRelationship(); // Coding
        case -925155509:  return getReference(); // Identifier
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("claimIdentifier")) {
          this.claim = new Identifier();
          return this.claim;
        }
        else if (name.equals("claimReference")) {
          this.claim = new Reference();
          return this.claim;
        }
        else if (name.equals("relationship")) {
          this.relationship = new Coding();
          return this.relationship;
        }
        else if (name.equals("reference")) {
          this.reference = new Identifier();
          return this.reference;
        }
        else
          return super.addChild(name);
      }

      public RelatedClaimsComponent copy() {
        RelatedClaimsComponent dst = new RelatedClaimsComponent();
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
        if (!(other instanceof RelatedClaimsComponent))
          return false;
        RelatedClaimsComponent o = (RelatedClaimsComponent) other;
        return compareDeep(claim, o.claim, true) && compareDeep(relationship, o.relationship, true) && compareDeep(reference, o.reference, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RelatedClaimsComponent))
          return false;
        RelatedClaimsComponent o = (RelatedClaimsComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (claim == null || claim.isEmpty()) && (relationship == null || relationship.isEmpty())
           && (reference == null || reference.isEmpty());
      }

  public String fhirType() {
    return "Claim.related";

  }

  }

    @Block()
    public static class PayeeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of Party to be reimbursed: Subscriber, provider, other.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of party: Subscriber, Provider, other", formalDefinition="Type of Party to be reimbursed: Subscriber, provider, other." )
        protected Coding type;

        /**
         * Party to be reimbursed: Subscriber, provider, other.
         */
        @Child(name = "party", type = {Identifier.class, Practitioner.class, Organization.class, Patient.class, RelatedPerson.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Party to receive the payable", formalDefinition="Party to be reimbursed: Subscriber, provider, other." )
        protected Type party;

        private static final long serialVersionUID = 1304353420L;

    /**
     * Constructor
     */
      public PayeeComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PayeeComponent(Coding type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Type of Party to be reimbursed: Subscriber, provider, other.)
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
         * @param value {@link #type} (Type of Party to be reimbursed: Subscriber, provider, other.)
         */
        public PayeeComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #party} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public Type getParty() { 
          return this.party;
        }

        /**
         * @return {@link #party} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public Identifier getPartyIdentifier() throws FHIRException { 
          if (!(this.party instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.party.getClass().getName()+" was encountered");
          return (Identifier) this.party;
        }

        public boolean hasPartyIdentifier() { 
          return this.party instanceof Identifier;
        }

        /**
         * @return {@link #party} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public Reference getPartyReference() throws FHIRException { 
          if (!(this.party instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.party.getClass().getName()+" was encountered");
          return (Reference) this.party;
        }

        public boolean hasPartyReference() { 
          return this.party instanceof Reference;
        }

        public boolean hasParty() { 
          return this.party != null && !this.party.isEmpty();
        }

        /**
         * @param value {@link #party} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public PayeeComponent setParty(Type value) { 
          this.party = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "Type of Party to be reimbursed: Subscriber, provider, other.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("party[x]", "Identifier|Reference(Practitioner|Organization|Patient|RelatedPerson)", "Party to be reimbursed: Subscriber, provider, other.", 0, java.lang.Integer.MAX_VALUE, party));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 106437350: /*party*/ return this.party == null ? new Base[0] : new Base[] {this.party}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 106437350: // party
          this.party = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("party[x]"))
          this.party = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // Coding
        case 1189320666:  return getParty(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("partyIdentifier")) {
          this.party = new Identifier();
          return this.party;
        }
        else if (name.equals("partyReference")) {
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PayeeComponent))
          return false;
        PayeeComponent o = (PayeeComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(party, o.party, true);
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
        return super.isEmpty() && (type == null || type.isEmpty()) && (party == null || party.isEmpty())
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
        @Description(shortDefinition="Number to covey order of diagnosis", formalDefinition="Sequence of diagnosis which serves to order and provide a link." )
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
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : new Base[] {this.diagnosis}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          break;
        case 1196993265: // diagnosis
          this.diagnosis = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

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
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: throw new FHIRException("Cannot make property sequence as it is not a complex type"); // PositiveIntType
        case 1196993265:  return getDiagnosis(); // Coding
        default: return super.makeProperty(hash, name);
        }

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
    public static class ProcedureComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Sequence of procedures which serves to order and provide a link.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Procedure sequence for reference", formalDefinition="Sequence of procedures which serves to order and provide a link." )
        protected PositiveIntType sequence;

        /**
         * Date and optionally time the procedure was performed .
         */
        @Child(name = "date", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When the procedure was performed", formalDefinition="Date and optionally time the procedure was performed ." )
        protected DateTimeType date;

        /**
         * The procedure code.
         */
        @Child(name = "procedure", type = {Coding.class, Procedure.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Patient's list of procedures performed", formalDefinition="The procedure code." )
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
        public Coding getProcedureCoding() throws FHIRException { 
          if (!(this.procedure instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.procedure.getClass().getName()+" was encountered");
          return (Coding) this.procedure;
        }

        public boolean hasProcedureCoding() { 
          return this.procedure instanceof Coding;
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
          childrenList.add(new Property("procedure[x]", "Coding|Reference(Procedure)", "The procedure code.", 0, java.lang.Integer.MAX_VALUE, procedure));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case -1095204141: // procedure
          this.procedure = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("procedure[x]"))
          this.procedure = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: throw new FHIRException("Cannot make property sequence as it is not a complex type"); // PositiveIntType
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case 1640074445:  return getProcedure(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.sequence");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.date");
        }
        else if (name.equals("procedureCoding")) {
          this.procedure = new Coding();
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
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (date == null || date.isEmpty())
           && (procedure == null || procedure.isEmpty());
      }

  public String fhirType() {
    return "Claim.procedure";

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
        @Child(name = "coverage", type = {Identifier.class, Coverage.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the program or plan identification, underwriter or payor." )
        protected Type coverage;

        /**
         * The contract number of a business agreement which describes the terms and conditions.
         */
        @Child(name = "businessArrangement", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Business agreement", formalDefinition="The contract number of a business agreement which describes the terms and conditions." )
        protected StringType businessArrangement;

        /**
         * A list of references from the Insurer to which these services pertain.
         */
        @Child(name = "preAuthRef", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Pre-Authorization/Determination Reference", formalDefinition="A list of references from the Insurer to which these services pertain." )
        protected List<StringType> preAuthRef;

        /**
         * The Coverages adjudication details.
         */
        @Child(name = "claimResponse", type = {ClaimResponse.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication results", formalDefinition="The Coverages adjudication details." )
        protected Reference claimResponse;

        /**
         * The actual object that is the target of the reference (The Coverages adjudication details.)
         */
        protected ClaimResponse claimResponseTarget;

        /**
         * The style (standard) and version of the original material which was converted into this resource.
         */
        @Child(name = "originalRuleset", type = {Coding.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
        protected Coding originalRuleset;

        private static final long serialVersionUID = 2031704818L;

    /**
     * Constructor
     */
      public CoverageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CoverageComponent(PositiveIntType sequence, BooleanType focal, Type coverage) {
        super();
        this.sequence = sequence;
        this.focal = focal;
        this.coverage = coverage;
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
        public Type getCoverage() { 
          return this.coverage;
        }

        /**
         * @return {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
         */
        public Identifier getCoverageIdentifier() throws FHIRException { 
          if (!(this.coverage instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.coverage.getClass().getName()+" was encountered");
          return (Identifier) this.coverage;
        }

        public boolean hasCoverageIdentifier() { 
          return this.coverage instanceof Identifier;
        }

        /**
         * @return {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
         */
        public Reference getCoverageReference() throws FHIRException { 
          if (!(this.coverage instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.coverage.getClass().getName()+" was encountered");
          return (Reference) this.coverage;
        }

        public boolean hasCoverageReference() { 
          return this.coverage instanceof Reference;
        }

        public boolean hasCoverage() { 
          return this.coverage != null && !this.coverage.isEmpty();
        }

        /**
         * @param value {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
         */
        public CoverageComponent setCoverage(Type value) { 
          this.coverage = value;
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
          childrenList.add(new Property("coverage[x]", "Identifier|Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, coverage));
          childrenList.add(new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, java.lang.Integer.MAX_VALUE, businessArrangement));
          childrenList.add(new Property("preAuthRef", "string", "A list of references from the Insurer to which these services pertain.", 0, java.lang.Integer.MAX_VALUE, preAuthRef));
          childrenList.add(new Property("claimResponse", "Reference(ClaimResponse)", "The Coverages adjudication details.", 0, java.lang.Integer.MAX_VALUE, claimResponse));
          childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 97604197: /*focal*/ return this.focal == null ? new Base[0] : new Base[] {this.focal}; // BooleanType
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Type
        case 259920682: /*businessArrangement*/ return this.businessArrangement == null ? new Base[0] : new Base[] {this.businessArrangement}; // StringType
        case 522246568: /*preAuthRef*/ return this.preAuthRef == null ? new Base[0] : this.preAuthRef.toArray(new Base[this.preAuthRef.size()]); // StringType
        case 689513629: /*claimResponse*/ return this.claimResponse == null ? new Base[0] : new Base[] {this.claimResponse}; // Reference
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          break;
        case 97604197: // focal
          this.focal = castToBoolean(value); // BooleanType
          break;
        case -351767064: // coverage
          this.coverage = (Type) value; // Type
          break;
        case 259920682: // businessArrangement
          this.businessArrangement = castToString(value); // StringType
          break;
        case 522246568: // preAuthRef
          this.getPreAuthRef().add(castToString(value)); // StringType
          break;
        case 689513629: // claimResponse
          this.claimResponse = castToReference(value); // Reference
          break;
        case 1089373397: // originalRuleset
          this.originalRuleset = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("focal"))
          this.focal = castToBoolean(value); // BooleanType
        else if (name.equals("coverage[x]"))
          this.coverage = (Type) value; // Type
        else if (name.equals("businessArrangement"))
          this.businessArrangement = castToString(value); // StringType
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
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: throw new FHIRException("Cannot make property sequence as it is not a complex type"); // PositiveIntType
        case 97604197: throw new FHIRException("Cannot make property focal as it is not a complex type"); // BooleanType
        case 227689880:  return getCoverage(); // Type
        case 259920682: throw new FHIRException("Cannot make property businessArrangement as it is not a complex type"); // StringType
        case 522246568: throw new FHIRException("Cannot make property preAuthRef as it is not a complex type"); // StringType
        case 689513629:  return getClaimResponse(); // Reference
        case 1089373397:  return getOriginalRuleset(); // Coding
        default: return super.makeProperty(hash, name);
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
        else if (name.equals("coverageIdentifier")) {
          this.coverage = new Identifier();
          return this.coverage;
        }
        else if (name.equals("coverageReference")) {
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
           && compareDeep(businessArrangement, o.businessArrangement, true) && compareDeep(preAuthRef, o.preAuthRef, true)
           && compareDeep(claimResponse, o.claimResponse, true) && compareDeep(originalRuleset, o.originalRuleset, true)
          ;
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
           && (preAuthRef == null || preAuthRef.isEmpty()) && (claimResponse == null || claimResponse.isEmpty())
           && (originalRuleset == null || originalRuleset.isEmpty());
      }

  public String fhirType() {
    return "Claim.coverage";

  }

  }

    @Block()
    public static class OnsetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The start or start and end dates for the treatable condition.
         */
        @Child(name = "time", type = {DateType.class, Period.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Illness, injury or treatable condition date", formalDefinition="The start or start and end dates for the treatable condition." )
        protected Type time;

        /**
         * Onset typifications eg. Start of pregnancy, start of illnes, etc.
         */
        @Child(name = "type", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Onset of what", formalDefinition="Onset typifications eg. Start of pregnancy, start of illnes, etc." )
        protected Coding type;

        private static final long serialVersionUID = -560173231L;

    /**
     * Constructor
     */
      public OnsetComponent() {
        super();
      }

        /**
         * @return {@link #time} (The start or start and end dates for the treatable condition.)
         */
        public Type getTime() { 
          return this.time;
        }

        /**
         * @return {@link #time} (The start or start and end dates for the treatable condition.)
         */
        public DateType getTimeDateType() throws FHIRException { 
          if (!(this.time instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.time.getClass().getName()+" was encountered");
          return (DateType) this.time;
        }

        public boolean hasTimeDateType() { 
          return this.time instanceof DateType;
        }

        /**
         * @return {@link #time} (The start or start and end dates for the treatable condition.)
         */
        public Period getTimePeriod() throws FHIRException { 
          if (!(this.time instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.time.getClass().getName()+" was encountered");
          return (Period) this.time;
        }

        public boolean hasTimePeriod() { 
          return this.time instanceof Period;
        }

        public boolean hasTime() { 
          return this.time != null && !this.time.isEmpty();
        }

        /**
         * @param value {@link #time} (The start or start and end dates for the treatable condition.)
         */
        public OnsetComponent setTime(Type value) { 
          this.time = value;
          return this;
        }

        /**
         * @return {@link #type} (Onset typifications eg. Start of pregnancy, start of illnes, etc.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OnsetComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Onset typifications eg. Start of pregnancy, start of illnes, etc.)
         */
        public OnsetComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("time[x]", "date|Period", "The start or start and end dates for the treatable condition.", 0, java.lang.Integer.MAX_VALUE, time));
          childrenList.add(new Property("type", "Coding", "Onset typifications eg. Start of pregnancy, start of illnes, etc.", 0, java.lang.Integer.MAX_VALUE, type));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3560141: /*time*/ return this.time == null ? new Base[0] : new Base[] {this.time}; // Type
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3560141: // time
          this.time = (Type) value; // Type
          break;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("time[x]"))
          this.time = (Type) value; // Type
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1313930605:  return getTime(); // Type
        case 3575610:  return getType(); // Coding
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("timeDate")) {
          this.time = new DateType();
          return this.time;
        }
        else if (name.equals("timePeriod")) {
          this.time = new Period();
          return this.time;
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else
          return super.addChild(name);
      }

      public OnsetComponent copy() {
        OnsetComponent dst = new OnsetComponent();
        copyValues(dst);
        dst.time = time == null ? null : time.copy();
        dst.type = type == null ? null : type.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OnsetComponent))
          return false;
        OnsetComponent o = (OnsetComponent) other;
        return compareDeep(time, o.time, true) && compareDeep(type, o.type, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OnsetComponent))
          return false;
        OnsetComponent o = (OnsetComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (time == null || time.isEmpty()) && (type == null || type.isEmpty())
          ;
      }

  public String fhirType() {
    return "Claim.onset";

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
        @Child(name = "provider", type = {Identifier.class, Practitioner.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
        protected Type provider;

        /**
         * The practitioner who is supervising the work of the servicing provider(s).
         */
        @Child(name = "supervisor", type = {Identifier.class, Practitioner.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Supervising Practitioner", formalDefinition="The practitioner who is supervising the work of the servicing provider(s)." )
        protected Type supervisor;

        /**
         * The qualification which is applicable for this service.
         */
        @Child(name = "providerQualification", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type, classification or Specialization", formalDefinition="The qualification which is applicable for this service." )
        protected Coding providerQualification;

        /**
         * Diagnosis applicable for this service or product line.
         */
        @Child(name = "diagnosisLinkId", type = {PositiveIntType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Applicable diagnoses", formalDefinition="Diagnosis applicable for this service or product line." )
        protected List<PositiveIntType> diagnosisLinkId;

        /**
         * If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.
         */
        @Child(name = "service", type = {Coding.class}, order=7, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Item Code", formalDefinition="If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied." )
        protected Coding service;

        /**
         * Unusual circumstances which may influence adjudication.
         */
        @Child(name = "serviceModifier", type = {Coding.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Service/Product modifiers", formalDefinition="Unusual circumstances which may influence adjudication." )
        protected List<Coding> serviceModifier;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.
         */
        @Child(name = "modifier", type = {Coding.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen." )
        protected List<Coding> modifier;

        /**
         * For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.
         */
        @Child(name = "programCode", type = {Coding.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Program specific reason for item inclusion", formalDefinition="For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program." )
        protected List<Coding> programCode;

        /**
         * The date or dates when the enclosed suite of services were performed or completed.
         */
        @Child(name = "serviced", type = {DateType.class, Period.class}, order=11, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date or dates of Service", formalDefinition="The date or dates when the enclosed suite of services were performed or completed." )
        protected Type serviced;

        /**
         * Where the service was provided.
         */
        @Child(name = "place", type = {Coding.class}, order=12, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Place of service", formalDefinition="Where the service was provided." )
        protected Coding place;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=13, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=14, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=15, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=16, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=17, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (List of Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        /**
         * Physical service site on the patient (limb, tooth, etc).
         */
        @Child(name = "bodySite", type = {Coding.class}, order=19, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service Location", formalDefinition="Physical service site on the patient (limb, tooth, etc)." )
        protected Coding bodySite;

        /**
         * A region or surface of the site, eg. limb region or tooth surface(s).
         */
        @Child(name = "subSite", type = {Coding.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Service Sub-location", formalDefinition="A region or surface of the site, eg. limb region or tooth surface(s)." )
        protected List<Coding> subSite;

        /**
         * Second tier of goods and services.
         */
        @Child(name = "detail", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Additional items", formalDefinition="Second tier of goods and services." )
        protected List<DetailComponent> detail;

        /**
         * The materials and placement date of prior fixed prosthesis.
         */
        @Child(name = "prosthesis", type = {}, order=22, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Prosthetic details", formalDefinition="The materials and placement date of prior fixed prosthesis." )
        protected ProsthesisComponent prosthesis;

        private static final long serialVersionUID = 1007480127L;

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
        public ItemsComponent setProvider(Type value) { 
          this.provider = value;
          return this;
        }

        /**
         * @return {@link #supervisor} (The practitioner who is supervising the work of the servicing provider(s).)
         */
        public Type getSupervisor() { 
          return this.supervisor;
        }

        /**
         * @return {@link #supervisor} (The practitioner who is supervising the work of the servicing provider(s).)
         */
        public Identifier getSupervisorIdentifier() throws FHIRException { 
          if (!(this.supervisor instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.supervisor.getClass().getName()+" was encountered");
          return (Identifier) this.supervisor;
        }

        public boolean hasSupervisorIdentifier() { 
          return this.supervisor instanceof Identifier;
        }

        /**
         * @return {@link #supervisor} (The practitioner who is supervising the work of the servicing provider(s).)
         */
        public Reference getSupervisorReference() throws FHIRException { 
          if (!(this.supervisor instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.supervisor.getClass().getName()+" was encountered");
          return (Reference) this.supervisor;
        }

        public boolean hasSupervisorReference() { 
          return this.supervisor instanceof Reference;
        }

        public boolean hasSupervisor() { 
          return this.supervisor != null && !this.supervisor.isEmpty();
        }

        /**
         * @param value {@link #supervisor} (The practitioner who is supervising the work of the servicing provider(s).)
         */
        public ItemsComponent setSupervisor(Type value) { 
          this.supervisor = value;
          return this;
        }

        /**
         * @return {@link #providerQualification} (The qualification which is applicable for this service.)
         */
        public Coding getProviderQualification() { 
          if (this.providerQualification == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.providerQualification");
            else if (Configuration.doAutoCreate())
              this.providerQualification = new Coding(); // cc
          return this.providerQualification;
        }

        public boolean hasProviderQualification() { 
          return this.providerQualification != null && !this.providerQualification.isEmpty();
        }

        /**
         * @param value {@link #providerQualification} (The qualification which is applicable for this service.)
         */
        public ItemsComponent setProviderQualification(Coding value) { 
          this.providerQualification = value;
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
         * @return {@link #serviceModifier} (Unusual circumstances which may influence adjudication.)
         */
        public List<Coding> getServiceModifier() { 
          if (this.serviceModifier == null)
            this.serviceModifier = new ArrayList<Coding>();
          return this.serviceModifier;
        }

        public boolean hasServiceModifier() { 
          if (this.serviceModifier == null)
            return false;
          for (Coding item : this.serviceModifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #serviceModifier} (Unusual circumstances which may influence adjudication.)
         */
    // syntactic sugar
        public Coding addServiceModifier() { //3
          Coding t = new Coding();
          if (this.serviceModifier == null)
            this.serviceModifier = new ArrayList<Coding>();
          this.serviceModifier.add(t);
          return t;
        }

    // syntactic sugar
        public ItemsComponent addServiceModifier(Coding t) { //3
          if (t == null)
            return this;
          if (this.serviceModifier == null)
            this.serviceModifier = new ArrayList<Coding>();
          this.serviceModifier.add(t);
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
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
        public List<Coding> getProgramCode() { 
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          return this.programCode;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (Coding item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
    // syntactic sugar
        public Coding addProgramCode() { //3
          Coding t = new Coding();
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
          return t;
        }

    // syntactic sugar
        public ItemsComponent addProgramCode(Coding t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
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
        public List<Reference> getUdi() { 
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          return this.udi;
        }

        public boolean hasUdi() { 
          if (this.udi == null)
            return false;
          for (Reference item : this.udi)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
    // syntactic sugar
        public Reference addUdi() { //3
          Reference t = new Reference();
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return t;
        }

    // syntactic sugar
        public ItemsComponent addUdi(Reference t) { //3
          if (t == null)
            return this;
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return this;
        }

        /**
         * @return {@link #udi} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. List of Unique Device Identifiers associated with this line item.)
         */
        public List<Device> getUdiTarget() { 
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          return this.udiTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #udi} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. List of Unique Device Identifiers associated with this line item.)
         */
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
          childrenList.add(new Property("provider[x]", "Identifier|Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
          childrenList.add(new Property("supervisor[x]", "Identifier|Reference(Practitioner)", "The practitioner who is supervising the work of the servicing provider(s).", 0, java.lang.Integer.MAX_VALUE, supervisor));
          childrenList.add(new Property("providerQualification", "Coding", "The qualification which is applicable for this service.", 0, java.lang.Integer.MAX_VALUE, providerQualification));
          childrenList.add(new Property("diagnosisLinkId", "positiveInt", "Diagnosis applicable for this service or product line.", 0, java.lang.Integer.MAX_VALUE, diagnosisLinkId));
          childrenList.add(new Property("service", "Coding", "If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("serviceModifier", "Coding", "Unusual circumstances which may influence adjudication.", 0, java.lang.Integer.MAX_VALUE, serviceModifier));
          childrenList.add(new Property("modifier", "Coding", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("programCode", "Coding", "For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.", 0, java.lang.Integer.MAX_VALUE, programCode));
          childrenList.add(new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, serviced));
          childrenList.add(new Property("place", "Coding", "Where the service was provided.", 0, java.lang.Integer.MAX_VALUE, place));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Reference(Device)", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("bodySite", "Coding", "Physical service site on the patient (limb, tooth, etc).", 0, java.lang.Integer.MAX_VALUE, bodySite));
          childrenList.add(new Property("subSite", "Coding", "A region or surface of the site, eg. limb region or tooth surface(s).", 0, java.lang.Integer.MAX_VALUE, subSite));
          childrenList.add(new Property("detail", "", "Second tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, detail));
          childrenList.add(new Property("prosthesis", "", "The materials and placement date of prior fixed prosthesis.", 0, java.lang.Integer.MAX_VALUE, prosthesis));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Type
        case -1697229976: /*supervisor*/ return this.supervisor == null ? new Base[0] : new Base[] {this.supervisor}; // Type
        case -1240156290: /*providerQualification*/ return this.providerQualification == null ? new Base[0] : new Base[] {this.providerQualification}; // Coding
        case -1659207418: /*diagnosisLinkId*/ return this.diagnosisLinkId == null ? new Base[0] : this.diagnosisLinkId.toArray(new Base[this.diagnosisLinkId.size()]); // PositiveIntType
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // Coding
        case 615164076: /*serviceModifier*/ return this.serviceModifier == null ? new Base[0] : this.serviceModifier.toArray(new Base[this.serviceModifier.size()]); // Coding
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // Coding
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // Coding
        case 1379209295: /*serviced*/ return this.serviced == null ? new Base[0] : new Base[] {this.serviced}; // Type
        case 106748167: /*place*/ return this.place == null ? new Base[0] : new Base[] {this.place}; // Coding
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case -982754077: /*points*/ return this.points == null ? new Base[0] : new Base[] {this.points}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // Coding
        case -1868566105: /*subSite*/ return this.subSite == null ? new Base[0] : this.subSite.toArray(new Base[this.subSite.size()]); // Coding
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // DetailComponent
        case -2138744398: /*prosthesis*/ return this.prosthesis == null ? new Base[0] : new Base[] {this.prosthesis}; // ProsthesisComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          break;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case -987494927: // provider
          this.provider = (Type) value; // Type
          break;
        case -1697229976: // supervisor
          this.supervisor = (Type) value; // Type
          break;
        case -1240156290: // providerQualification
          this.providerQualification = castToCoding(value); // Coding
          break;
        case -1659207418: // diagnosisLinkId
          this.getDiagnosisLinkId().add(castToPositiveInt(value)); // PositiveIntType
          break;
        case 1984153269: // service
          this.service = castToCoding(value); // Coding
          break;
        case 615164076: // serviceModifier
          this.getServiceModifier().add(castToCoding(value)); // Coding
          break;
        case -615513385: // modifier
          this.getModifier().add(castToCoding(value)); // Coding
          break;
        case 1010065041: // programCode
          this.getProgramCode().add(castToCoding(value)); // Coding
          break;
        case 1379209295: // serviced
          this.serviced = (Type) value; // Type
          break;
        case 106748167: // place
          this.place = castToCoding(value); // Coding
          break;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          break;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          break;
        case -982754077: // points
          this.points = castToDecimal(value); // DecimalType
          break;
        case 108957: // net
          this.net = castToMoney(value); // Money
          break;
        case 115642: // udi
          this.getUdi().add(castToReference(value)); // Reference
          break;
        case 1702620169: // bodySite
          this.bodySite = castToCoding(value); // Coding
          break;
        case -1868566105: // subSite
          this.getSubSite().add(castToCoding(value)); // Coding
          break;
        case -1335224239: // detail
          this.getDetail().add((DetailComponent) value); // DetailComponent
          break;
        case -2138744398: // prosthesis
          this.prosthesis = (ProsthesisComponent) value; // ProsthesisComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("provider[x]"))
          this.provider = (Type) value; // Type
        else if (name.equals("supervisor[x]"))
          this.supervisor = (Type) value; // Type
        else if (name.equals("providerQualification"))
          this.providerQualification = castToCoding(value); // Coding
        else if (name.equals("diagnosisLinkId"))
          this.getDiagnosisLinkId().add(castToPositiveInt(value));
        else if (name.equals("service"))
          this.service = castToCoding(value); // Coding
        else if (name.equals("serviceModifier"))
          this.getServiceModifier().add(castToCoding(value));
        else if (name.equals("modifier"))
          this.getModifier().add(castToCoding(value));
        else if (name.equals("programCode"))
          this.getProgramCode().add(castToCoding(value));
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
          this.getUdi().add(castToReference(value));
        else if (name.equals("bodySite"))
          this.bodySite = castToCoding(value); // Coding
        else if (name.equals("subSite"))
          this.getSubSite().add(castToCoding(value));
        else if (name.equals("detail"))
          this.getDetail().add((DetailComponent) value);
        else if (name.equals("prosthesis"))
          this.prosthesis = (ProsthesisComponent) value; // ProsthesisComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: throw new FHIRException("Cannot make property sequence as it is not a complex type"); // PositiveIntType
        case 3575610:  return getType(); // Coding
        case 2064698607:  return getProvider(); // Type
        case -1823115240:  return getSupervisor(); // Type
        case -1240156290:  return getProviderQualification(); // Coding
        case -1659207418: throw new FHIRException("Cannot make property diagnosisLinkId as it is not a complex type"); // PositiveIntType
        case 1984153269:  return getService(); // Coding
        case 615164076:  return addServiceModifier(); // Coding
        case -615513385:  return addModifier(); // Coding
        case 1010065041:  return addProgramCode(); // Coding
        case -1927922223:  return getServiced(); // Type
        case 106748167:  return getPlace(); // Coding
        case -1285004149:  return getQuantity(); // SimpleQuantity
        case -486196699:  return getUnitPrice(); // Money
        case -1282148017: throw new FHIRException("Cannot make property factor as it is not a complex type"); // DecimalType
        case -982754077: throw new FHIRException("Cannot make property points as it is not a complex type"); // DecimalType
        case 108957:  return getNet(); // Money
        case 115642:  return addUdi(); // Reference
        case 1702620169:  return getBodySite(); // Coding
        case -1868566105:  return addSubSite(); // Coding
        case -1335224239:  return addDetail(); // DetailComponent
        case -2138744398:  return getProsthesis(); // ProsthesisComponent
        default: return super.makeProperty(hash, name);
        }

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
        else if (name.equals("providerIdentifier")) {
          this.provider = new Identifier();
          return this.provider;
        }
        else if (name.equals("providerReference")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("supervisorIdentifier")) {
          this.supervisor = new Identifier();
          return this.supervisor;
        }
        else if (name.equals("supervisorReference")) {
          this.supervisor = new Reference();
          return this.supervisor;
        }
        else if (name.equals("providerQualification")) {
          this.providerQualification = new Coding();
          return this.providerQualification;
        }
        else if (name.equals("diagnosisLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.diagnosisLinkId");
        }
        else if (name.equals("service")) {
          this.service = new Coding();
          return this.service;
        }
        else if (name.equals("serviceModifier")) {
          return addServiceModifier();
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
          return addUdi();
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new Coding();
          return this.bodySite;
        }
        else if (name.equals("subSite")) {
          return addSubSite();
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
        dst.supervisor = supervisor == null ? null : supervisor.copy();
        dst.providerQualification = providerQualification == null ? null : providerQualification.copy();
        if (diagnosisLinkId != null) {
          dst.diagnosisLinkId = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : diagnosisLinkId)
            dst.diagnosisLinkId.add(i.copy());
        };
        dst.service = service == null ? null : service.copy();
        if (serviceModifier != null) {
          dst.serviceModifier = new ArrayList<Coding>();
          for (Coding i : serviceModifier)
            dst.serviceModifier.add(i.copy());
        };
        if (modifier != null) {
          dst.modifier = new ArrayList<Coding>();
          for (Coding i : modifier)
            dst.modifier.add(i.copy());
        };
        if (programCode != null) {
          dst.programCode = new ArrayList<Coding>();
          for (Coding i : programCode)
            dst.programCode.add(i.copy());
        };
        dst.serviced = serviced == null ? null : serviced.copy();
        dst.place = place == null ? null : place.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        if (udi != null) {
          dst.udi = new ArrayList<Reference>();
          for (Reference i : udi)
            dst.udi.add(i.copy());
        };
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        if (subSite != null) {
          dst.subSite = new ArrayList<Coding>();
          for (Coding i : subSite)
            dst.subSite.add(i.copy());
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
           && compareDeep(supervisor, o.supervisor, true) && compareDeep(providerQualification, o.providerQualification, true)
           && compareDeep(diagnosisLinkId, o.diagnosisLinkId, true) && compareDeep(service, o.service, true)
           && compareDeep(serviceModifier, o.serviceModifier, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(serviced, o.serviced, true) && compareDeep(place, o.place, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(points, o.points, true) && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(subSite, o.subSite, true) && compareDeep(detail, o.detail, true)
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
           && (provider == null || provider.isEmpty()) && (supervisor == null || supervisor.isEmpty())
           && (providerQualification == null || providerQualification.isEmpty()) && (diagnosisLinkId == null || diagnosisLinkId.isEmpty())
           && (service == null || service.isEmpty()) && (serviceModifier == null || serviceModifier.isEmpty())
           && (modifier == null || modifier.isEmpty()) && (programCode == null || programCode.isEmpty())
           && (serviced == null || serviced.isEmpty()) && (place == null || place.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (unitPrice == null || unitPrice.isEmpty()) && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty())
           && (net == null || net.isEmpty()) && (udi == null || udi.isEmpty()) && (bodySite == null || bodySite.isEmpty())
           && (subSite == null || subSite.isEmpty()) && (detail == null || detail.isEmpty()) && (prosthesis == null || prosthesis.isEmpty())
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
         * For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.
         */
        @Child(name = "programCode", type = {Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Program specific reason for item inclusion", formalDefinition="For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program." )
        protected List<Coding> programCode;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Total additional item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (List of Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        /**
         * Third tier of goods and services.
         */
        @Child(name = "subDetail", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Additional items", formalDefinition="Third tier of goods and services." )
        protected List<SubDetailComponent> subDetail;

        private static final long serialVersionUID = -1099698352L;

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
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
        public List<Coding> getProgramCode() { 
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          return this.programCode;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (Coding item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
    // syntactic sugar
        public Coding addProgramCode() { //3
          Coding t = new Coding();
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
          return t;
        }

    // syntactic sugar
        public DetailComponent addProgramCode(Coding t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
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
        public List<Reference> getUdi() { 
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          return this.udi;
        }

        public boolean hasUdi() { 
          if (this.udi == null)
            return false;
          for (Reference item : this.udi)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
    // syntactic sugar
        public Reference addUdi() { //3
          Reference t = new Reference();
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return t;
        }

    // syntactic sugar
        public DetailComponent addUdi(Reference t) { //3
          if (t == null)
            return this;
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return this;
        }

        /**
         * @return {@link #udi} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. List of Unique Device Identifiers associated with this line item.)
         */
        public List<Device> getUdiTarget() { 
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          return this.udiTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #udi} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. List of Unique Device Identifiers associated with this line item.)
         */
        public Device addUdiTarget() { 
          Device r = new Device();
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          this.udiTarget.add(r);
          return r;
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
          childrenList.add(new Property("programCode", "Coding", "For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.", 0, java.lang.Integer.MAX_VALUE, programCode));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Reference(Device)", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("subDetail", "", "Third tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, subDetail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // Coding
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // Coding
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case -982754077: /*points*/ return this.points == null ? new Base[0] : new Base[] {this.points}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
        case -828829007: /*subDetail*/ return this.subDetail == null ? new Base[0] : this.subDetail.toArray(new Base[this.subDetail.size()]); // SubDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          break;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 1984153269: // service
          this.service = castToCoding(value); // Coding
          break;
        case 1010065041: // programCode
          this.getProgramCode().add(castToCoding(value)); // Coding
          break;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          break;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          break;
        case -982754077: // points
          this.points = castToDecimal(value); // DecimalType
          break;
        case 108957: // net
          this.net = castToMoney(value); // Money
          break;
        case 115642: // udi
          this.getUdi().add(castToReference(value)); // Reference
          break;
        case -828829007: // subDetail
          this.getSubDetail().add((SubDetailComponent) value); // SubDetailComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("service"))
          this.service = castToCoding(value); // Coding
        else if (name.equals("programCode"))
          this.getProgramCode().add(castToCoding(value));
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
          this.getUdi().add(castToReference(value));
        else if (name.equals("subDetail"))
          this.getSubDetail().add((SubDetailComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: throw new FHIRException("Cannot make property sequence as it is not a complex type"); // PositiveIntType
        case 3575610:  return getType(); // Coding
        case 1984153269:  return getService(); // Coding
        case 1010065041:  return addProgramCode(); // Coding
        case -1285004149:  return getQuantity(); // SimpleQuantity
        case -486196699:  return getUnitPrice(); // Money
        case -1282148017: throw new FHIRException("Cannot make property factor as it is not a complex type"); // DecimalType
        case -982754077: throw new FHIRException("Cannot make property points as it is not a complex type"); // DecimalType
        case 108957:  return getNet(); // Money
        case 115642:  return addUdi(); // Reference
        case -828829007:  return addSubDetail(); // SubDetailComponent
        default: return super.makeProperty(hash, name);
        }

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
        dst.type = type == null ? null : type.copy();
        dst.service = service == null ? null : service.copy();
        if (programCode != null) {
          dst.programCode = new ArrayList<Coding>();
          for (Coding i : programCode)
            dst.programCode.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DetailComponent))
          return false;
        DetailComponent o = (DetailComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(service, o.service, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(points, o.points, true) && compareDeep(net, o.net, true)
           && compareDeep(udi, o.udi, true) && compareDeep(subDetail, o.subDetail, true);
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
           && (service == null || service.isEmpty()) && (programCode == null || programCode.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
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
         * For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.
         */
        @Child(name = "programCode", type = {Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Program specific reason for item inclusion", formalDefinition="For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program." )
        protected List<Coding> programCode;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * The fee for an addittional service or product or charge.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="The fee for an addittional service or product or charge." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Net additional item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (List of Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        private static final long serialVersionUID = 846630321L;

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
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
        public List<Coding> getProgramCode() { 
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          return this.programCode;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (Coding item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
    // syntactic sugar
        public Coding addProgramCode() { //3
          Coding t = new Coding();
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
          return t;
        }

    // syntactic sugar
        public SubDetailComponent addProgramCode(Coding t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
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
        public List<Reference> getUdi() { 
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          return this.udi;
        }

        public boolean hasUdi() { 
          if (this.udi == null)
            return false;
          for (Reference item : this.udi)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
    // syntactic sugar
        public Reference addUdi() { //3
          Reference t = new Reference();
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return t;
        }

    // syntactic sugar
        public SubDetailComponent addUdi(Reference t) { //3
          if (t == null)
            return this;
          if (this.udi == null)
            this.udi = new ArrayList<Reference>();
          this.udi.add(t);
          return this;
        }

        /**
         * @return {@link #udi} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. List of Unique Device Identifiers associated with this line item.)
         */
        public List<Device> getUdiTarget() { 
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          return this.udiTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #udi} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. List of Unique Device Identifiers associated with this line item.)
         */
        public Device addUdiTarget() { 
          Device r = new Device();
          if (this.udiTarget == null)
            this.udiTarget = new ArrayList<Device>();
          this.udiTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("service", "Coding", "The fee for an addittional service or product or charge.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("programCode", "Coding", "For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.", 0, java.lang.Integer.MAX_VALUE, programCode));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "The fee for an addittional service or product or charge.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Reference(Device)", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // Coding
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // Coding
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case -982754077: /*points*/ return this.points == null ? new Base[0] : new Base[] {this.points}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 115642: /*udi*/ return this.udi == null ? new Base[0] : this.udi.toArray(new Base[this.udi.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          break;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 1984153269: // service
          this.service = castToCoding(value); // Coding
          break;
        case 1010065041: // programCode
          this.getProgramCode().add(castToCoding(value)); // Coding
          break;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          break;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          break;
        case -982754077: // points
          this.points = castToDecimal(value); // DecimalType
          break;
        case 108957: // net
          this.net = castToMoney(value); // Money
          break;
        case 115642: // udi
          this.getUdi().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("service"))
          this.service = castToCoding(value); // Coding
        else if (name.equals("programCode"))
          this.getProgramCode().add(castToCoding(value));
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
          this.getUdi().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: throw new FHIRException("Cannot make property sequence as it is not a complex type"); // PositiveIntType
        case 3575610:  return getType(); // Coding
        case 1984153269:  return getService(); // Coding
        case 1010065041:  return addProgramCode(); // Coding
        case -1285004149:  return getQuantity(); // SimpleQuantity
        case -486196699:  return getUnitPrice(); // Money
        case -1282148017: throw new FHIRException("Cannot make property factor as it is not a complex type"); // DecimalType
        case -982754077: throw new FHIRException("Cannot make property points as it is not a complex type"); // DecimalType
        case 108957:  return getNet(); // Money
        case 115642:  return addUdi(); // Reference
        default: return super.makeProperty(hash, name);
        }

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
          return addUdi();
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
        if (programCode != null) {
          dst.programCode = new ArrayList<Coding>();
          for (Coding i : programCode)
            dst.programCode.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        if (udi != null) {
          dst.udi = new ArrayList<Reference>();
          for (Reference i : udi)
            dst.udi.add(i.copy());
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
           && compareDeep(programCode, o.programCode, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(points, o.points, true) && compareDeep(net, o.net, true)
           && compareDeep(udi, o.udi, true);
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
           && (service == null || service.isEmpty()) && (programCode == null || programCode.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
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
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1948342084: /*initial*/ return this.initial == null ? new Base[0] : new Base[] {this.initial}; // BooleanType
        case -1770675816: /*priorDate*/ return this.priorDate == null ? new Base[0] : new Base[] {this.priorDate}; // DateType
        case -532999663: /*priorMaterial*/ return this.priorMaterial == null ? new Base[0] : new Base[] {this.priorMaterial}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1948342084: // initial
          this.initial = castToBoolean(value); // BooleanType
          break;
        case -1770675816: // priorDate
          this.priorDate = castToDate(value); // DateType
          break;
        case -532999663: // priorMaterial
          this.priorMaterial = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

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
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1948342084: throw new FHIRException("Cannot make property initial as it is not a complex type"); // BooleanType
        case -1770675816: throw new FHIRException("Cannot make property priorDate as it is not a complex type"); // DateType
        case -532999663:  return getPriorMaterial(); // Coding
        default: return super.makeProperty(hash, name);
        }

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
        @Description(shortDefinition="Indicates whether it was extracted or other reason", formalDefinition="Missing reason may be: E-extraction, O-other." )
        protected Coding reason;

        /**
         * The date of the extraction either known from records or patient reported estimate.
         */
        @Child(name = "extractionDate", type = {DateType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date tooth was extracted if known", formalDefinition="The date of the extraction either known from records or patient reported estimate." )
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
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 110545608: /*tooth*/ return this.tooth == null ? new Base[0] : new Base[] {this.tooth}; // Coding
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // Coding
        case 580646965: /*extractionDate*/ return this.extractionDate == null ? new Base[0] : new Base[] {this.extractionDate}; // DateType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 110545608: // tooth
          this.tooth = castToCoding(value); // Coding
          break;
        case -934964668: // reason
          this.reason = castToCoding(value); // Coding
          break;
        case 580646965: // extractionDate
          this.extractionDate = castToDate(value); // DateType
          break;
        default: super.setProperty(hash, name, value);
        }

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
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 110545608:  return getTooth(); // Coding
        case -934964668:  return getReason(); // Coding
        case 580646965: throw new FHIRException("Cannot make property extractionDate as it is not a complex type"); // DateType
        default: return super.makeProperty(hash, name);
        }

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
     * The category of claim.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="institutional | oral | pharmacy | professional | vision", formalDefinition="The category of claim." )
    protected Enumeration<ClaimType> type;

    /**
     * A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.
     */
    @Child(name = "subType", type = {Coding.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Finer grained claim type information", formalDefinition="A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType." )
    protected List<Coding> subType;

    /**
     * The business identifier for the instance: claim number, pre-determination or pre-authorization number.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Claim number", formalDefinition="The business identifier for the instance: claim number, pre-determination or pre-authorization number." )
    protected List<Identifier> identifier;

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
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The billable period for which charges are being submitted.
     */
    @Child(name = "billablePeriod", type = {Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period for charge submission", formalDefinition="The billable period for which charges are being submitted." )
    protected Period billablePeriod;

    /**
     * Insurer Identifier, typical BIN number (6 digit).
     */
    @Child(name = "target", type = {Identifier.class, Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurer", formalDefinition="Insurer Identifier, typical BIN number (6 digit)." )
    protected Type target;

    /**
     * The provider which is responsible for the bill, claim pre-determination, pre-authorization.
     */
    @Child(name = "provider", type = {Identifier.class, Practitioner.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible provider", formalDefinition="The provider which is responsible for the bill, claim pre-determination, pre-authorization." )
    protected Type provider;

    /**
     * The organization which is responsible for the bill, claim pre-determination, pre-authorization.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the bill, claim pre-determination, pre-authorization." )
    protected Type organization;

    /**
     * Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    @Child(name = "use", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="complete | proposed | exploratory | other", formalDefinition="Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination)." )
    protected Enumeration<Use> use;

    /**
     * Immediate (STAT), best effort (NORMAL), deferred (DEFER).
     */
    @Child(name = "priority", type = {Coding.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Desired processing priority", formalDefinition="Immediate (STAT), best effort (NORMAL), deferred (DEFER)." )
    protected Coding priority;

    /**
     * In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.
     */
    @Child(name = "fundsReserve", type = {Coding.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Funds requested to be reserved", formalDefinition="In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested." )
    protected Coding fundsReserve;

    /**
     * Person who created the invoice/claim/pre-determination or pre-authorization.
     */
    @Child(name = "enterer", type = {Identifier.class, Practitioner.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Author", formalDefinition="Person who created the invoice/claim/pre-determination or pre-authorization." )
    protected Type enterer;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Identifier.class, Location.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Type facility;

    /**
     * Other claims which are related to this claim such as prior claim versions or for related services.
     */
    @Child(name = "related", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Related Claims which may be revelant to processing this claimn", formalDefinition="Other claims which are related to this claim such as prior claim versions or for related services." )
    protected List<RelatedClaimsComponent> related;

    /**
     * Prescription to support the dispensing of Pharmacy or Vision products.
     */
    @Child(name = "prescription", type = {Identifier.class, MedicationOrder.class, VisionPrescription.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Prescription", formalDefinition="Prescription to support the dispensing of Pharmacy or Vision products." )
    protected Type prescription;

    /**
     * Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.
     */
    @Child(name = "originalPrescription", type = {Identifier.class, MedicationOrder.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original Prescription", formalDefinition="Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products." )
    protected Type originalPrescription;

    /**
     * The party to be reimbursed for the services.
     */
    @Child(name = "payee", type = {}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Party to be paid any benefits payable", formalDefinition="The party to be reimbursed for the services." )
    protected PayeeComponent payee;

    /**
     * The referral resource which lists the date, practitioner, reason and other supporting information.
     */
    @Child(name = "referral", type = {Identifier.class, ReferralRequest.class}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Treatment Referral", formalDefinition="The referral resource which lists the date, practitioner, reason and other supporting information." )
    protected Type referral;

    /**
     * **Insert definition of Occurrence codes.
     */
    @Child(name = "occurrenceCode", type = {Coding.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Occurrence Codes", formalDefinition="**Insert definition of Occurrence codes." )
    protected List<Coding> occurrenceCode;

    /**
     * **Insert definition of Occurrence Span codes.
     */
    @Child(name = "occurenceSpanCode", type = {Coding.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Occurrence Span Codes", formalDefinition="**Insert definition of Occurrence Span codes." )
    protected List<Coding> occurenceSpanCode;

    /**
     * **Insert definition of Value codes.
     */
    @Child(name = "valueCode", type = {Coding.class}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Value Codes", formalDefinition="**Insert definition of Value codes." )
    protected List<Coding> valueCode;

    /**
     * Ordered list of patient diagnosis for which care is sought.
     */
    @Child(name = "diagnosis", type = {}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Diagnosis", formalDefinition="Ordered list of patient diagnosis for which care is sought." )
    protected List<DiagnosisComponent> diagnosis;

    /**
     * Ordered list of patient procedures performed to support the adjudication.
     */
    @Child(name = "procedure", type = {}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Procedures performed", formalDefinition="Ordered list of patient procedures performed to support the adjudication." )
    protected List<ProcedureComponent> procedure;

    /**
     * List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.
     */
    @Child(name = "specialCondition", type = {Coding.class}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="List of special Conditions", formalDefinition="List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication." )
    protected List<Coding> specialCondition;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Identifier.class, Patient.class}, order=26, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Type patient;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "coverage", type = {}, order=27, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected List<CoverageComponent> coverage;

    /**
     * Date of an accident which these services are addressing.
     */
    @Child(name = "accidentDate", type = {DateType.class}, order=28, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the accident occurred", formalDefinition="Date of an accident which these services are addressing." )
    protected DateType accidentDate;

    /**
     * Type of accident: work, auto, etc.
     */
    @Child(name = "accidentType", type = {Coding.class}, order=29, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The nature of the accident", formalDefinition="Type of accident: work, auto, etc." )
    protected Coding accidentType;

    /**
     * Accident Place.
     */
    @Child(name = "accidentLocation", type = {Address.class, Location.class}, order=30, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Accident Place", formalDefinition="Accident Place." )
    protected Type accidentLocation;

    /**
     * A list of intervention and exception codes which may influence the adjudication of the claim.
     */
    @Child(name = "interventionException", type = {Coding.class}, order=31, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Intervention and exception code (Pharma)", formalDefinition="A list of intervention and exception codes which may influence the adjudication of the claim." )
    protected List<Coding> interventionException;

    /**
     * Period, start and last dates of aspects of the Condition or related services.
     */
    @Child(name = "onset", type = {}, order=32, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Condition related Onset related dates and codes", formalDefinition="Period, start and last dates of aspects of the Condition or related services." )
    protected List<OnsetComponent> onset;

    /**
     * The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).
     */
    @Child(name = "employmentImpacted", type = {Period.class}, order=33, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period unable to work", formalDefinition="The start and optional end dates of when the patient was precluded from working due to the treatable condition(s)." )
    protected Period employmentImpacted;

    /**
     * The start and optional end dates of when the patient was confined to a treatment center.
     */
    @Child(name = "hospitalization", type = {Period.class}, order=34, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period in hospital", formalDefinition="The start and optional end dates of when the patient was confined to a treatment center." )
    protected Period hospitalization;

    /**
     * First tier of goods and services.
     */
    @Child(name = "item", type = {}, order=35, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Goods and Services", formalDefinition="First tier of goods and services." )
    protected List<ItemsComponent> item;

    /**
     * The total value of the claim.
     */
    @Child(name = "total", type = {Money.class}, order=36, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total claim cost", formalDefinition="The total value of the claim." )
    protected Money total;

    /**
     * Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.
     */
    @Child(name = "additionalMaterial", type = {Coding.class}, order=37, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional supporting materials and documents", formalDefinition="Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission." )
    protected List<Coding> additionalMaterial;

    /**
     * A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.
     */
    @Child(name = "missingTeeth", type = {}, order=38, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Only if type = oral", formalDefinition="A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons." )
    protected List<MissingTeethComponent> missingTeeth;

    private static final long serialVersionUID = 1478562254L;

  /**
   * Constructor
   */
    public Claim() {
      super();
    }

  /**
   * Constructor
   */
    public Claim(Enumeration<ClaimType> type, Type patient) {
      super();
      this.type = type;
      this.patient = patient;
    }

    /**
     * @return {@link #type} (The category of claim.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
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
     * @param value {@link #type} (The category of claim.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Claim setTypeElement(Enumeration<ClaimType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The category of claim.
     */
    public ClaimType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The category of claim.
     */
    public Claim setType(ClaimType value) { 
        if (this.type == null)
          this.type = new Enumeration<ClaimType>(new ClaimTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #subType} (A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.)
     */
    public List<Coding> getSubType() { 
      if (this.subType == null)
        this.subType = new ArrayList<Coding>();
      return this.subType;
    }

    public boolean hasSubType() { 
      if (this.subType == null)
        return false;
      for (Coding item : this.subType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #subType} (A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.)
     */
    // syntactic sugar
    public Coding addSubType() { //3
      Coding t = new Coding();
      if (this.subType == null)
        this.subType = new ArrayList<Coding>();
      this.subType.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addSubType(Coding t) { //3
      if (t == null)
        return this;
      if (this.subType == null)
        this.subType = new ArrayList<Coding>();
      this.subType.add(t);
      return this;
    }

    /**
     * @return {@link #identifier} (The business identifier for the instance: claim number, pre-determination or pre-authorization number.)
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
     * @return {@link #identifier} (The business identifier for the instance: claim number, pre-determination or pre-authorization number.)
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
    public Type getTarget() { 
      return this.target;
    }

    /**
     * @return {@link #target} (Insurer Identifier, typical BIN number (6 digit).)
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
     * @return {@link #target} (Insurer Identifier, typical BIN number (6 digit).)
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
     * @param value {@link #target} (Insurer Identifier, typical BIN number (6 digit).)
     */
    public Claim setTarget(Type value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #provider} (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Type getProvider() { 
      return this.provider;
    }

    /**
     * @return {@link #provider} (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
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
     * @return {@link #provider} (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
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
     * @param value {@link #provider} (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Claim setProvider(Type value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Type getOrganization() { 
      return this.organization;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
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
     * @return {@link #organization} (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
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
     * @param value {@link #organization} (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Claim setOrganization(Type value) { 
      this.organization = value;
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
    public Type getEnterer() { 
      return this.enterer;
    }

    /**
     * @return {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Identifier getEntererIdentifier() throws FHIRException { 
      if (!(this.enterer instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.enterer.getClass().getName()+" was encountered");
      return (Identifier) this.enterer;
    }

    public boolean hasEntererIdentifier() { 
      return this.enterer instanceof Identifier;
    }

    /**
     * @return {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Reference getEntererReference() throws FHIRException { 
      if (!(this.enterer instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.enterer.getClass().getName()+" was encountered");
      return (Reference) this.enterer;
    }

    public boolean hasEntererReference() { 
      return this.enterer instanceof Reference;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Claim setEnterer(Type value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Type getFacility() { 
      return this.facility;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Identifier getFacilityIdentifier() throws FHIRException { 
      if (!(this.facility instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.facility.getClass().getName()+" was encountered");
      return (Identifier) this.facility;
    }

    public boolean hasFacilityIdentifier() { 
      return this.facility instanceof Identifier;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Reference getFacilityReference() throws FHIRException { 
      if (!(this.facility instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.facility.getClass().getName()+" was encountered");
      return (Reference) this.facility;
    }

    public boolean hasFacilityReference() { 
      return this.facility instanceof Reference;
    }

    public boolean hasFacility() { 
      return this.facility != null && !this.facility.isEmpty();
    }

    /**
     * @param value {@link #facility} (Facility where the services were provided.)
     */
    public Claim setFacility(Type value) { 
      this.facility = value;
      return this;
    }

    /**
     * @return {@link #related} (Other claims which are related to this claim such as prior claim versions or for related services.)
     */
    public List<RelatedClaimsComponent> getRelated() { 
      if (this.related == null)
        this.related = new ArrayList<RelatedClaimsComponent>();
      return this.related;
    }

    public boolean hasRelated() { 
      if (this.related == null)
        return false;
      for (RelatedClaimsComponent item : this.related)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #related} (Other claims which are related to this claim such as prior claim versions or for related services.)
     */
    // syntactic sugar
    public RelatedClaimsComponent addRelated() { //3
      RelatedClaimsComponent t = new RelatedClaimsComponent();
      if (this.related == null)
        this.related = new ArrayList<RelatedClaimsComponent>();
      this.related.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addRelated(RelatedClaimsComponent t) { //3
      if (t == null)
        return this;
      if (this.related == null)
        this.related = new ArrayList<RelatedClaimsComponent>();
      this.related.add(t);
      return this;
    }

    /**
     * @return {@link #prescription} (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    public Type getPrescription() { 
      return this.prescription;
    }

    /**
     * @return {@link #prescription} (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    public Identifier getPrescriptionIdentifier() throws FHIRException { 
      if (!(this.prescription instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.prescription.getClass().getName()+" was encountered");
      return (Identifier) this.prescription;
    }

    public boolean hasPrescriptionIdentifier() { 
      return this.prescription instanceof Identifier;
    }

    /**
     * @return {@link #prescription} (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    public Reference getPrescriptionReference() throws FHIRException { 
      if (!(this.prescription instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.prescription.getClass().getName()+" was encountered");
      return (Reference) this.prescription;
    }

    public boolean hasPrescriptionReference() { 
      return this.prescription instanceof Reference;
    }

    public boolean hasPrescription() { 
      return this.prescription != null && !this.prescription.isEmpty();
    }

    /**
     * @param value {@link #prescription} (Prescription to support the dispensing of Pharmacy or Vision products.)
     */
    public Claim setPrescription(Type value) { 
      this.prescription = value;
      return this;
    }

    /**
     * @return {@link #originalPrescription} (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Type getOriginalPrescription() { 
      return this.originalPrescription;
    }

    /**
     * @return {@link #originalPrescription} (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Identifier getOriginalPrescriptionIdentifier() throws FHIRException { 
      if (!(this.originalPrescription instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.originalPrescription.getClass().getName()+" was encountered");
      return (Identifier) this.originalPrescription;
    }

    public boolean hasOriginalPrescriptionIdentifier() { 
      return this.originalPrescription instanceof Identifier;
    }

    /**
     * @return {@link #originalPrescription} (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Reference getOriginalPrescriptionReference() throws FHIRException { 
      if (!(this.originalPrescription instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.originalPrescription.getClass().getName()+" was encountered");
      return (Reference) this.originalPrescription;
    }

    public boolean hasOriginalPrescriptionReference() { 
      return this.originalPrescription instanceof Reference;
    }

    public boolean hasOriginalPrescription() { 
      return this.originalPrescription != null && !this.originalPrescription.isEmpty();
    }

    /**
     * @param value {@link #originalPrescription} (Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.)
     */
    public Claim setOriginalPrescription(Type value) { 
      this.originalPrescription = value;
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
    public Type getReferral() { 
      return this.referral;
    }

    /**
     * @return {@link #referral} (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public Identifier getReferralIdentifier() throws FHIRException { 
      if (!(this.referral instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.referral.getClass().getName()+" was encountered");
      return (Identifier) this.referral;
    }

    public boolean hasReferralIdentifier() { 
      return this.referral instanceof Identifier;
    }

    /**
     * @return {@link #referral} (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public Reference getReferralReference() throws FHIRException { 
      if (!(this.referral instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.referral.getClass().getName()+" was encountered");
      return (Reference) this.referral;
    }

    public boolean hasReferralReference() { 
      return this.referral instanceof Reference;
    }

    public boolean hasReferral() { 
      return this.referral != null && !this.referral.isEmpty();
    }

    /**
     * @param value {@link #referral} (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public Claim setReferral(Type value) { 
      this.referral = value;
      return this;
    }

    /**
     * @return {@link #occurrenceCode} (**Insert definition of Occurrence codes.)
     */
    public List<Coding> getOccurrenceCode() { 
      if (this.occurrenceCode == null)
        this.occurrenceCode = new ArrayList<Coding>();
      return this.occurrenceCode;
    }

    public boolean hasOccurrenceCode() { 
      if (this.occurrenceCode == null)
        return false;
      for (Coding item : this.occurrenceCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #occurrenceCode} (**Insert definition of Occurrence codes.)
     */
    // syntactic sugar
    public Coding addOccurrenceCode() { //3
      Coding t = new Coding();
      if (this.occurrenceCode == null)
        this.occurrenceCode = new ArrayList<Coding>();
      this.occurrenceCode.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addOccurrenceCode(Coding t) { //3
      if (t == null)
        return this;
      if (this.occurrenceCode == null)
        this.occurrenceCode = new ArrayList<Coding>();
      this.occurrenceCode.add(t);
      return this;
    }

    /**
     * @return {@link #occurenceSpanCode} (**Insert definition of Occurrence Span codes.)
     */
    public List<Coding> getOccurenceSpanCode() { 
      if (this.occurenceSpanCode == null)
        this.occurenceSpanCode = new ArrayList<Coding>();
      return this.occurenceSpanCode;
    }

    public boolean hasOccurenceSpanCode() { 
      if (this.occurenceSpanCode == null)
        return false;
      for (Coding item : this.occurenceSpanCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #occurenceSpanCode} (**Insert definition of Occurrence Span codes.)
     */
    // syntactic sugar
    public Coding addOccurenceSpanCode() { //3
      Coding t = new Coding();
      if (this.occurenceSpanCode == null)
        this.occurenceSpanCode = new ArrayList<Coding>();
      this.occurenceSpanCode.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addOccurenceSpanCode(Coding t) { //3
      if (t == null)
        return this;
      if (this.occurenceSpanCode == null)
        this.occurenceSpanCode = new ArrayList<Coding>();
      this.occurenceSpanCode.add(t);
      return this;
    }

    /**
     * @return {@link #valueCode} (**Insert definition of Value codes.)
     */
    public List<Coding> getValueCode() { 
      if (this.valueCode == null)
        this.valueCode = new ArrayList<Coding>();
      return this.valueCode;
    }

    public boolean hasValueCode() { 
      if (this.valueCode == null)
        return false;
      for (Coding item : this.valueCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #valueCode} (**Insert definition of Value codes.)
     */
    // syntactic sugar
    public Coding addValueCode() { //3
      Coding t = new Coding();
      if (this.valueCode == null)
        this.valueCode = new ArrayList<Coding>();
      this.valueCode.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addValueCode(Coding t) { //3
      if (t == null)
        return this;
      if (this.valueCode == null)
        this.valueCode = new ArrayList<Coding>();
      this.valueCode.add(t);
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
     * @return {@link #procedure} (Ordered list of patient procedures performed to support the adjudication.)
     */
    public List<ProcedureComponent> getProcedure() { 
      if (this.procedure == null)
        this.procedure = new ArrayList<ProcedureComponent>();
      return this.procedure;
    }

    public boolean hasProcedure() { 
      if (this.procedure == null)
        return false;
      for (ProcedureComponent item : this.procedure)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #procedure} (Ordered list of patient procedures performed to support the adjudication.)
     */
    // syntactic sugar
    public ProcedureComponent addProcedure() { //3
      ProcedureComponent t = new ProcedureComponent();
      if (this.procedure == null)
        this.procedure = new ArrayList<ProcedureComponent>();
      this.procedure.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addProcedure(ProcedureComponent t) { //3
      if (t == null)
        return this;
      if (this.procedure == null)
        this.procedure = new ArrayList<ProcedureComponent>();
      this.procedure.add(t);
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
    public Type getPatient() { 
      return this.patient;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
     */
    public Identifier getPatientIdentifier() throws FHIRException { 
      if (!(this.patient instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.patient.getClass().getName()+" was encountered");
      return (Identifier) this.patient;
    }

    public boolean hasPatientIdentifier() { 
      return this.patient instanceof Identifier;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
     */
    public Reference getPatientReference() throws FHIRException { 
      if (!(this.patient instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.patient.getClass().getName()+" was encountered");
      return (Reference) this.patient;
    }

    public boolean hasPatientReference() { 
      return this.patient instanceof Reference;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Patient Resource.)
     */
    public Claim setPatient(Type value) { 
      this.patient = value;
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
     * @return {@link #onset} (Period, start and last dates of aspects of the Condition or related services.)
     */
    public List<OnsetComponent> getOnset() { 
      if (this.onset == null)
        this.onset = new ArrayList<OnsetComponent>();
      return this.onset;
    }

    public boolean hasOnset() { 
      if (this.onset == null)
        return false;
      for (OnsetComponent item : this.onset)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #onset} (Period, start and last dates of aspects of the Condition or related services.)
     */
    // syntactic sugar
    public OnsetComponent addOnset() { //3
      OnsetComponent t = new OnsetComponent();
      if (this.onset == null)
        this.onset = new ArrayList<OnsetComponent>();
      this.onset.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addOnset(OnsetComponent t) { //3
      if (t == null)
        return this;
      if (this.onset == null)
        this.onset = new ArrayList<OnsetComponent>();
      this.onset.add(t);
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
     * @return {@link #additionalMaterial} (Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.)
     */
    public List<Coding> getAdditionalMaterial() { 
      if (this.additionalMaterial == null)
        this.additionalMaterial = new ArrayList<Coding>();
      return this.additionalMaterial;
    }

    public boolean hasAdditionalMaterial() { 
      if (this.additionalMaterial == null)
        return false;
      for (Coding item : this.additionalMaterial)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #additionalMaterial} (Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.)
     */
    // syntactic sugar
    public Coding addAdditionalMaterial() { //3
      Coding t = new Coding();
      if (this.additionalMaterial == null)
        this.additionalMaterial = new ArrayList<Coding>();
      this.additionalMaterial.add(t);
      return t;
    }

    // syntactic sugar
    public Claim addAdditionalMaterial(Coding t) { //3
      if (t == null)
        return this;
      if (this.additionalMaterial == null)
        this.additionalMaterial = new ArrayList<Coding>();
      this.additionalMaterial.add(t);
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
        childrenList.add(new Property("type", "code", "The category of claim.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subType", "Coding", "A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.", 0, java.lang.Integer.MAX_VALUE, subType));
        childrenList.add(new Property("identifier", "Identifier", "The business identifier for the instance: claim number, pre-determination or pre-authorization number.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("ruleset", "Coding", "The version of the specification on which this instance relies.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The version of the specification from which the original instance was created.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("billablePeriod", "Period", "The billable period for which charges are being submitted.", 0, java.lang.Integer.MAX_VALUE, billablePeriod));
        childrenList.add(new Property("target[x]", "Identifier|Reference(Organization)", "Insurer Identifier, typical BIN number (6 digit).", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("provider[x]", "Identifier|Reference(Practitioner)", "The provider which is responsible for the bill, claim pre-determination, pre-authorization.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The organization which is responsible for the bill, claim pre-determination, pre-authorization.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("use", "code", "Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).", 0, java.lang.Integer.MAX_VALUE, use));
        childrenList.add(new Property("priority", "Coding", "Immediate (STAT), best effort (NORMAL), deferred (DEFER).", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("fundsReserve", "Coding", "In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.", 0, java.lang.Integer.MAX_VALUE, fundsReserve));
        childrenList.add(new Property("enterer[x]", "Identifier|Reference(Practitioner)", "Person who created the invoice/claim/pre-determination or pre-authorization.", 0, java.lang.Integer.MAX_VALUE, enterer));
        childrenList.add(new Property("facility[x]", "Identifier|Reference(Location)", "Facility where the services were provided.", 0, java.lang.Integer.MAX_VALUE, facility));
        childrenList.add(new Property("related", "", "Other claims which are related to this claim such as prior claim versions or for related services.", 0, java.lang.Integer.MAX_VALUE, related));
        childrenList.add(new Property("prescription[x]", "Identifier|Reference(MedicationOrder|VisionPrescription)", "Prescription to support the dispensing of Pharmacy or Vision products.", 0, java.lang.Integer.MAX_VALUE, prescription));
        childrenList.add(new Property("originalPrescription[x]", "Identifier|Reference(MedicationOrder)", "Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.", 0, java.lang.Integer.MAX_VALUE, originalPrescription));
        childrenList.add(new Property("payee", "", "The party to be reimbursed for the services.", 0, java.lang.Integer.MAX_VALUE, payee));
        childrenList.add(new Property("referral[x]", "Identifier|Reference(ReferralRequest)", "The referral resource which lists the date, practitioner, reason and other supporting information.", 0, java.lang.Integer.MAX_VALUE, referral));
        childrenList.add(new Property("occurrenceCode", "Coding", "**Insert definition of Occurrence codes.", 0, java.lang.Integer.MAX_VALUE, occurrenceCode));
        childrenList.add(new Property("occurenceSpanCode", "Coding", "**Insert definition of Occurrence Span codes.", 0, java.lang.Integer.MAX_VALUE, occurenceSpanCode));
        childrenList.add(new Property("valueCode", "Coding", "**Insert definition of Value codes.", 0, java.lang.Integer.MAX_VALUE, valueCode));
        childrenList.add(new Property("diagnosis", "", "Ordered list of patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        childrenList.add(new Property("procedure", "", "Ordered list of patient procedures performed to support the adjudication.", 0, java.lang.Integer.MAX_VALUE, procedure));
        childrenList.add(new Property("specialCondition", "Coding", "List of special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.", 0, java.lang.Integer.MAX_VALUE, specialCondition));
        childrenList.add(new Property("patient[x]", "Identifier|Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("coverage", "", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("accidentDate", "date", "Date of an accident which these services are addressing.", 0, java.lang.Integer.MAX_VALUE, accidentDate));
        childrenList.add(new Property("accidentType", "Coding", "Type of accident: work, auto, etc.", 0, java.lang.Integer.MAX_VALUE, accidentType));
        childrenList.add(new Property("accidentLocation[x]", "Address|Reference(Location)", "Accident Place.", 0, java.lang.Integer.MAX_VALUE, accidentLocation));
        childrenList.add(new Property("interventionException", "Coding", "A list of intervention and exception codes which may influence the adjudication of the claim.", 0, java.lang.Integer.MAX_VALUE, interventionException));
        childrenList.add(new Property("onset", "", "Period, start and last dates of aspects of the Condition or related services.", 0, java.lang.Integer.MAX_VALUE, onset));
        childrenList.add(new Property("employmentImpacted", "Period", "The start and optional end dates of when the patient was precluded from working due to the treatable condition(s).", 0, java.lang.Integer.MAX_VALUE, employmentImpacted));
        childrenList.add(new Property("hospitalization", "Period", "The start and optional end dates of when the patient was confined to a treatment center.", 0, java.lang.Integer.MAX_VALUE, hospitalization));
        childrenList.add(new Property("item", "", "First tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, item));
        childrenList.add(new Property("total", "Money", "The total value of the claim.", 0, java.lang.Integer.MAX_VALUE, total));
        childrenList.add(new Property("additionalMaterial", "Coding", "Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.", 0, java.lang.Integer.MAX_VALUE, additionalMaterial));
        childrenList.add(new Property("missingTeeth", "", "A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.", 0, java.lang.Integer.MAX_VALUE, missingTeeth));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ClaimType>
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : this.subType.toArray(new Base[this.subType.size()]); // Coding
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -332066046: /*billablePeriod*/ return this.billablePeriod == null ? new Base[0] : new Base[] {this.billablePeriod}; // Period
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // Type
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Type
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // Enumeration<Use>
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Coding
        case 1314609806: /*fundsReserve*/ return this.fundsReserve == null ? new Base[0] : new Base[] {this.fundsReserve}; // Coding
        case -1591951995: /*enterer*/ return this.enterer == null ? new Base[0] : new Base[] {this.enterer}; // Type
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Type
        case 1090493483: /*related*/ return this.related == null ? new Base[0] : this.related.toArray(new Base[this.related.size()]); // RelatedClaimsComponent
        case 460301338: /*prescription*/ return this.prescription == null ? new Base[0] : new Base[] {this.prescription}; // Type
        case -1814015861: /*originalPrescription*/ return this.originalPrescription == null ? new Base[0] : new Base[] {this.originalPrescription}; // Type
        case 106443592: /*payee*/ return this.payee == null ? new Base[0] : new Base[] {this.payee}; // PayeeComponent
        case -722568291: /*referral*/ return this.referral == null ? new Base[0] : new Base[] {this.referral}; // Type
        case 1721744222: /*occurrenceCode*/ return this.occurrenceCode == null ? new Base[0] : this.occurrenceCode.toArray(new Base[this.occurrenceCode.size()]); // Coding
        case -556690898: /*occurenceSpanCode*/ return this.occurenceSpanCode == null ? new Base[0] : this.occurenceSpanCode.toArray(new Base[this.occurenceSpanCode.size()]); // Coding
        case -766209282: /*valueCode*/ return this.valueCode == null ? new Base[0] : this.valueCode.toArray(new Base[this.valueCode.size()]); // Coding
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : this.diagnosis.toArray(new Base[this.diagnosis.size()]); // DiagnosisComponent
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : this.procedure.toArray(new Base[this.procedure.size()]); // ProcedureComponent
        case -481489822: /*specialCondition*/ return this.specialCondition == null ? new Base[0] : this.specialCondition.toArray(new Base[this.specialCondition.size()]); // Coding
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Type
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : this.coverage.toArray(new Base[this.coverage.size()]); // CoverageComponent
        case -63170979: /*accidentDate*/ return this.accidentDate == null ? new Base[0] : new Base[] {this.accidentDate}; // DateType
        case -62671383: /*accidentType*/ return this.accidentType == null ? new Base[0] : new Base[] {this.accidentType}; // Coding
        case -1074014492: /*accidentLocation*/ return this.accidentLocation == null ? new Base[0] : new Base[] {this.accidentLocation}; // Type
        case 1753076536: /*interventionException*/ return this.interventionException == null ? new Base[0] : this.interventionException.toArray(new Base[this.interventionException.size()]); // Coding
        case 105901603: /*onset*/ return this.onset == null ? new Base[0] : this.onset.toArray(new Base[this.onset.size()]); // OnsetComponent
        case 1051487345: /*employmentImpacted*/ return this.employmentImpacted == null ? new Base[0] : new Base[] {this.employmentImpacted}; // Period
        case 1057894634: /*hospitalization*/ return this.hospitalization == null ? new Base[0] : new Base[] {this.hospitalization}; // Period
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // ItemsComponent
        case 110549828: /*total*/ return this.total == null ? new Base[0] : new Base[] {this.total}; // Money
        case -1534875026: /*additionalMaterial*/ return this.additionalMaterial == null ? new Base[0] : this.additionalMaterial.toArray(new Base[this.additionalMaterial.size()]); // Coding
        case -1157130302: /*missingTeeth*/ return this.missingTeeth == null ? new Base[0] : this.missingTeeth.toArray(new Base[this.missingTeeth.size()]); // MissingTeethComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new ClaimTypeEnumFactory().fromType(value); // Enumeration<ClaimType>
          break;
        case -1868521062: // subType
          this.getSubType().add(castToCoding(value)); // Coding
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
        case -332066046: // billablePeriod
          this.billablePeriod = castToPeriod(value); // Period
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
        case 116103: // use
          this.use = new UseEnumFactory().fromType(value); // Enumeration<Use>
          break;
        case -1165461084: // priority
          this.priority = castToCoding(value); // Coding
          break;
        case 1314609806: // fundsReserve
          this.fundsReserve = castToCoding(value); // Coding
          break;
        case -1591951995: // enterer
          this.enterer = (Type) value; // Type
          break;
        case 501116579: // facility
          this.facility = (Type) value; // Type
          break;
        case 1090493483: // related
          this.getRelated().add((RelatedClaimsComponent) value); // RelatedClaimsComponent
          break;
        case 460301338: // prescription
          this.prescription = (Type) value; // Type
          break;
        case -1814015861: // originalPrescription
          this.originalPrescription = (Type) value; // Type
          break;
        case 106443592: // payee
          this.payee = (PayeeComponent) value; // PayeeComponent
          break;
        case -722568291: // referral
          this.referral = (Type) value; // Type
          break;
        case 1721744222: // occurrenceCode
          this.getOccurrenceCode().add(castToCoding(value)); // Coding
          break;
        case -556690898: // occurenceSpanCode
          this.getOccurenceSpanCode().add(castToCoding(value)); // Coding
          break;
        case -766209282: // valueCode
          this.getValueCode().add(castToCoding(value)); // Coding
          break;
        case 1196993265: // diagnosis
          this.getDiagnosis().add((DiagnosisComponent) value); // DiagnosisComponent
          break;
        case -1095204141: // procedure
          this.getProcedure().add((ProcedureComponent) value); // ProcedureComponent
          break;
        case -481489822: // specialCondition
          this.getSpecialCondition().add(castToCoding(value)); // Coding
          break;
        case -791418107: // patient
          this.patient = (Type) value; // Type
          break;
        case -351767064: // coverage
          this.getCoverage().add((CoverageComponent) value); // CoverageComponent
          break;
        case -63170979: // accidentDate
          this.accidentDate = castToDate(value); // DateType
          break;
        case -62671383: // accidentType
          this.accidentType = castToCoding(value); // Coding
          break;
        case -1074014492: // accidentLocation
          this.accidentLocation = (Type) value; // Type
          break;
        case 1753076536: // interventionException
          this.getInterventionException().add(castToCoding(value)); // Coding
          break;
        case 105901603: // onset
          this.getOnset().add((OnsetComponent) value); // OnsetComponent
          break;
        case 1051487345: // employmentImpacted
          this.employmentImpacted = castToPeriod(value); // Period
          break;
        case 1057894634: // hospitalization
          this.hospitalization = castToPeriod(value); // Period
          break;
        case 3242771: // item
          this.getItem().add((ItemsComponent) value); // ItemsComponent
          break;
        case 110549828: // total
          this.total = castToMoney(value); // Money
          break;
        case -1534875026: // additionalMaterial
          this.getAdditionalMaterial().add(castToCoding(value)); // Coding
          break;
        case -1157130302: // missingTeeth
          this.getMissingTeeth().add((MissingTeethComponent) value); // MissingTeethComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new ClaimTypeEnumFactory().fromType(value); // Enumeration<ClaimType>
        else if (name.equals("subType"))
          this.getSubType().add(castToCoding(value));
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
        else if (name.equals("target[x]"))
          this.target = (Type) value; // Type
        else if (name.equals("provider[x]"))
          this.provider = (Type) value; // Type
        else if (name.equals("organization[x]"))
          this.organization = (Type) value; // Type
        else if (name.equals("use"))
          this.use = new UseEnumFactory().fromType(value); // Enumeration<Use>
        else if (name.equals("priority"))
          this.priority = castToCoding(value); // Coding
        else if (name.equals("fundsReserve"))
          this.fundsReserve = castToCoding(value); // Coding
        else if (name.equals("enterer[x]"))
          this.enterer = (Type) value; // Type
        else if (name.equals("facility[x]"))
          this.facility = (Type) value; // Type
        else if (name.equals("related"))
          this.getRelated().add((RelatedClaimsComponent) value);
        else if (name.equals("prescription[x]"))
          this.prescription = (Type) value; // Type
        else if (name.equals("originalPrescription[x]"))
          this.originalPrescription = (Type) value; // Type
        else if (name.equals("payee"))
          this.payee = (PayeeComponent) value; // PayeeComponent
        else if (name.equals("referral[x]"))
          this.referral = (Type) value; // Type
        else if (name.equals("occurrenceCode"))
          this.getOccurrenceCode().add(castToCoding(value));
        else if (name.equals("occurenceSpanCode"))
          this.getOccurenceSpanCode().add(castToCoding(value));
        else if (name.equals("valueCode"))
          this.getValueCode().add(castToCoding(value));
        else if (name.equals("diagnosis"))
          this.getDiagnosis().add((DiagnosisComponent) value);
        else if (name.equals("procedure"))
          this.getProcedure().add((ProcedureComponent) value);
        else if (name.equals("specialCondition"))
          this.getSpecialCondition().add(castToCoding(value));
        else if (name.equals("patient[x]"))
          this.patient = (Type) value; // Type
        else if (name.equals("coverage"))
          this.getCoverage().add((CoverageComponent) value);
        else if (name.equals("accidentDate"))
          this.accidentDate = castToDate(value); // DateType
        else if (name.equals("accidentType"))
          this.accidentType = castToCoding(value); // Coding
        else if (name.equals("accidentLocation[x]"))
          this.accidentLocation = (Type) value; // Type
        else if (name.equals("interventionException"))
          this.getInterventionException().add(castToCoding(value));
        else if (name.equals("onset"))
          this.getOnset().add((OnsetComponent) value);
        else if (name.equals("employmentImpacted"))
          this.employmentImpacted = castToPeriod(value); // Period
        else if (name.equals("hospitalization"))
          this.hospitalization = castToPeriod(value); // Period
        else if (name.equals("item"))
          this.getItem().add((ItemsComponent) value);
        else if (name.equals("total"))
          this.total = castToMoney(value); // Money
        else if (name.equals("additionalMaterial"))
          this.getAdditionalMaterial().add(castToCoding(value));
        else if (name.equals("missingTeeth"))
          this.getMissingTeeth().add((MissingTeethComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<ClaimType>
        case -1868521062:  return addSubType(); // Coding
        case -1618432855:  return addIdentifier(); // Identifier
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case -332066046:  return getBillablePeriod(); // Period
        case -815579825:  return getTarget(); // Type
        case 2064698607:  return getProvider(); // Type
        case 1326483053:  return getOrganization(); // Type
        case 116103: throw new FHIRException("Cannot make property use as it is not a complex type"); // Enumeration<Use>
        case -1165461084:  return getPriority(); // Coding
        case 1314609806:  return getFundsReserve(); // Coding
        case -812909349:  return getEnterer(); // Type
        case -542224643:  return getFacility(); // Type
        case 1090493483:  return addRelated(); // RelatedClaimsComponent
        case -993324506:  return getPrescription(); // Type
        case -2067905515:  return getOriginalPrescription(); // Type
        case 106443592:  return getPayee(); // PayeeComponent
        case 344221635:  return getReferral(); // Type
        case 1721744222:  return addOccurrenceCode(); // Coding
        case -556690898:  return addOccurenceSpanCode(); // Coding
        case -766209282:  return addValueCode(); // Coding
        case 1196993265:  return addDiagnosis(); // DiagnosisComponent
        case -1095204141:  return addProcedure(); // ProcedureComponent
        case -481489822:  return addSpecialCondition(); // Coding
        case -2061246629:  return getPatient(); // Type
        case -351767064:  return addCoverage(); // CoverageComponent
        case -63170979: throw new FHIRException("Cannot make property accidentDate as it is not a complex type"); // DateType
        case -62671383:  return getAccidentType(); // Coding
        case 1540715292:  return getAccidentLocation(); // Type
        case 1753076536:  return addInterventionException(); // Coding
        case 105901603:  return addOnset(); // OnsetComponent
        case 1051487345:  return getEmploymentImpacted(); // Period
        case 1057894634:  return getHospitalization(); // Period
        case 3242771:  return addItem(); // ItemsComponent
        case 110549828:  return getTotal(); // Money
        case -1534875026:  return addAdditionalMaterial(); // Coding
        case -1157130302:  return addMissingTeeth(); // MissingTeethComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.type");
        }
        else if (name.equals("subType")) {
          return addSubType();
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
        else if (name.equals("entererIdentifier")) {
          this.enterer = new Identifier();
          return this.enterer;
        }
        else if (name.equals("entererReference")) {
          this.enterer = new Reference();
          return this.enterer;
        }
        else if (name.equals("facilityIdentifier")) {
          this.facility = new Identifier();
          return this.facility;
        }
        else if (name.equals("facilityReference")) {
          this.facility = new Reference();
          return this.facility;
        }
        else if (name.equals("related")) {
          return addRelated();
        }
        else if (name.equals("prescriptionIdentifier")) {
          this.prescription = new Identifier();
          return this.prescription;
        }
        else if (name.equals("prescriptionReference")) {
          this.prescription = new Reference();
          return this.prescription;
        }
        else if (name.equals("originalPrescriptionIdentifier")) {
          this.originalPrescription = new Identifier();
          return this.originalPrescription;
        }
        else if (name.equals("originalPrescriptionReference")) {
          this.originalPrescription = new Reference();
          return this.originalPrescription;
        }
        else if (name.equals("payee")) {
          this.payee = new PayeeComponent();
          return this.payee;
        }
        else if (name.equals("referralIdentifier")) {
          this.referral = new Identifier();
          return this.referral;
        }
        else if (name.equals("referralReference")) {
          this.referral = new Reference();
          return this.referral;
        }
        else if (name.equals("occurrenceCode")) {
          return addOccurrenceCode();
        }
        else if (name.equals("occurenceSpanCode")) {
          return addOccurenceSpanCode();
        }
        else if (name.equals("valueCode")) {
          return addValueCode();
        }
        else if (name.equals("diagnosis")) {
          return addDiagnosis();
        }
        else if (name.equals("procedure")) {
          return addProcedure();
        }
        else if (name.equals("specialCondition")) {
          return addSpecialCondition();
        }
        else if (name.equals("patientIdentifier")) {
          this.patient = new Identifier();
          return this.patient;
        }
        else if (name.equals("patientReference")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("coverage")) {
          return addCoverage();
        }
        else if (name.equals("accidentDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Claim.accidentDate");
        }
        else if (name.equals("accidentType")) {
          this.accidentType = new Coding();
          return this.accidentType;
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
        else if (name.equals("onset")) {
          return addOnset();
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
        else if (name.equals("additionalMaterial")) {
          return addAdditionalMaterial();
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
        if (subType != null) {
          dst.subType = new ArrayList<Coding>();
          for (Coding i : subType)
            dst.subType.add(i.copy());
        };
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
        if (related != null) {
          dst.related = new ArrayList<RelatedClaimsComponent>();
          for (RelatedClaimsComponent i : related)
            dst.related.add(i.copy());
        };
        dst.prescription = prescription == null ? null : prescription.copy();
        dst.originalPrescription = originalPrescription == null ? null : originalPrescription.copy();
        dst.payee = payee == null ? null : payee.copy();
        dst.referral = referral == null ? null : referral.copy();
        if (occurrenceCode != null) {
          dst.occurrenceCode = new ArrayList<Coding>();
          for (Coding i : occurrenceCode)
            dst.occurrenceCode.add(i.copy());
        };
        if (occurenceSpanCode != null) {
          dst.occurenceSpanCode = new ArrayList<Coding>();
          for (Coding i : occurenceSpanCode)
            dst.occurenceSpanCode.add(i.copy());
        };
        if (valueCode != null) {
          dst.valueCode = new ArrayList<Coding>();
          for (Coding i : valueCode)
            dst.valueCode.add(i.copy());
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
        dst.accidentDate = accidentDate == null ? null : accidentDate.copy();
        dst.accidentType = accidentType == null ? null : accidentType.copy();
        dst.accidentLocation = accidentLocation == null ? null : accidentLocation.copy();
        if (interventionException != null) {
          dst.interventionException = new ArrayList<Coding>();
          for (Coding i : interventionException)
            dst.interventionException.add(i.copy());
        };
        if (onset != null) {
          dst.onset = new ArrayList<OnsetComponent>();
          for (OnsetComponent i : onset)
            dst.onset.add(i.copy());
        };
        dst.employmentImpacted = employmentImpacted == null ? null : employmentImpacted.copy();
        dst.hospitalization = hospitalization == null ? null : hospitalization.copy();
        if (item != null) {
          dst.item = new ArrayList<ItemsComponent>();
          for (ItemsComponent i : item)
            dst.item.add(i.copy());
        };
        dst.total = total == null ? null : total.copy();
        if (additionalMaterial != null) {
          dst.additionalMaterial = new ArrayList<Coding>();
          for (Coding i : additionalMaterial)
            dst.additionalMaterial.add(i.copy());
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
        return compareDeep(type, o.type, true) && compareDeep(subType, o.subType, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(ruleset, o.ruleset, true) && compareDeep(originalRuleset, o.originalRuleset, true)
           && compareDeep(created, o.created, true) && compareDeep(billablePeriod, o.billablePeriod, true)
           && compareDeep(target, o.target, true) && compareDeep(provider, o.provider, true) && compareDeep(organization, o.organization, true)
           && compareDeep(use, o.use, true) && compareDeep(priority, o.priority, true) && compareDeep(fundsReserve, o.fundsReserve, true)
           && compareDeep(enterer, o.enterer, true) && compareDeep(facility, o.facility, true) && compareDeep(related, o.related, true)
           && compareDeep(prescription, o.prescription, true) && compareDeep(originalPrescription, o.originalPrescription, true)
           && compareDeep(payee, o.payee, true) && compareDeep(referral, o.referral, true) && compareDeep(occurrenceCode, o.occurrenceCode, true)
           && compareDeep(occurenceSpanCode, o.occurenceSpanCode, true) && compareDeep(valueCode, o.valueCode, true)
           && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(procedure, o.procedure, true) && compareDeep(specialCondition, o.specialCondition, true)
           && compareDeep(patient, o.patient, true) && compareDeep(coverage, o.coverage, true) && compareDeep(accidentDate, o.accidentDate, true)
           && compareDeep(accidentType, o.accidentType, true) && compareDeep(accidentLocation, o.accidentLocation, true)
           && compareDeep(interventionException, o.interventionException, true) && compareDeep(onset, o.onset, true)
           && compareDeep(employmentImpacted, o.employmentImpacted, true) && compareDeep(hospitalization, o.hospitalization, true)
           && compareDeep(item, o.item, true) && compareDeep(total, o.total, true) && compareDeep(additionalMaterial, o.additionalMaterial, true)
           && compareDeep(missingTeeth, o.missingTeeth, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Claim))
          return false;
        Claim o = (Claim) other;
        return compareValues(type, o.type, true) && compareValues(created, o.created, true) && compareValues(use, o.use, true)
           && compareValues(accidentDate, o.accidentDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (subType == null || subType.isEmpty())
           && (identifier == null || identifier.isEmpty()) && (ruleset == null || ruleset.isEmpty())
           && (originalRuleset == null || originalRuleset.isEmpty()) && (created == null || created.isEmpty())
           && (billablePeriod == null || billablePeriod.isEmpty()) && (target == null || target.isEmpty())
           && (provider == null || provider.isEmpty()) && (organization == null || organization.isEmpty())
           && (use == null || use.isEmpty()) && (priority == null || priority.isEmpty()) && (fundsReserve == null || fundsReserve.isEmpty())
           && (enterer == null || enterer.isEmpty()) && (facility == null || facility.isEmpty()) && (related == null || related.isEmpty())
           && (prescription == null || prescription.isEmpty()) && (originalPrescription == null || originalPrescription.isEmpty())
           && (payee == null || payee.isEmpty()) && (referral == null || referral.isEmpty()) && (occurrenceCode == null || occurrenceCode.isEmpty())
           && (occurenceSpanCode == null || occurenceSpanCode.isEmpty()) && (valueCode == null || valueCode.isEmpty())
           && (diagnosis == null || diagnosis.isEmpty()) && (procedure == null || procedure.isEmpty())
           && (specialCondition == null || specialCondition.isEmpty()) && (patient == null || patient.isEmpty())
           && (coverage == null || coverage.isEmpty()) && (accidentDate == null || accidentDate.isEmpty())
           && (accidentType == null || accidentType.isEmpty()) && (accidentLocation == null || accidentLocation.isEmpty())
           && (interventionException == null || interventionException.isEmpty()) && (onset == null || onset.isEmpty())
           && (employmentImpacted == null || employmentImpacted.isEmpty()) && (hospitalization == null || hospitalization.isEmpty())
           && (item == null || item.isEmpty()) && (total == null || total.isEmpty()) && (additionalMaterial == null || additionalMaterial.isEmpty())
           && (missingTeeth == null || missingTeeth.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Claim;
   }

 /**
   * Search parameter: <b>patientidentifier</b>
   * <p>
   * Description: <b>Patient receiving the services</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.patientIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patientidentifier", path="Claim.patient.as(Identifier)", description="Patient receiving the services", type="token" )
  public static final String SP_PATIENTIDENTIFIER = "patientidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patientidentifier</b>
   * <p>
   * Description: <b>Patient receiving the services</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.patientIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PATIENTIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PATIENTIDENTIFIER);

 /**
   * Search parameter: <b>targetreference</b>
   * <p>
   * Description: <b>The target payor/insurer for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.targetReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="targetreference", path="Claim.target.as(Reference)", description="The target payor/insurer for the Claim", type="reference" )
  public static final String SP_TARGETREFERENCE = "targetreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>targetreference</b>
   * <p>
   * Description: <b>The target payor/insurer for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.targetReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam TARGETREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_TARGETREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:targetreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_TARGETREFERENCE = new ca.uhn.fhir.model.api.Include("Claim:targetreference").toLocked();

 /**
   * Search parameter: <b>facilityidentifier</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.facilityIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facilityidentifier", path="Claim.facility.as(Identifier)", description="Facility responsible for the goods and services", type="token" )
  public static final String SP_FACILITYIDENTIFIER = "facilityidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facilityidentifier</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.facilityIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FACILITYIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FACILITYIDENTIFIER);

 /**
   * Search parameter: <b>facilityreference</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.facilityReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facilityreference", path="Claim.facility.as(Reference)", description="Facility responsible for the goods and services", type="reference" )
  public static final String SP_FACILITYREFERENCE = "facilityreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facilityreference</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.facilityReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FACILITYREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FACILITYREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:facilityreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FACILITYREFERENCE = new ca.uhn.fhir.model.api.Include("Claim:facilityreference").toLocked();

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
   * Search parameter: <b>providerreference</b>
   * <p>
   * Description: <b>Provider responsible for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.providerReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="providerreference", path="Claim.provider.as(Reference)", description="Provider responsible for the Claim", type="reference" )
  public static final String SP_PROVIDERREFERENCE = "providerreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>providerreference</b>
   * <p>
   * Description: <b>Provider responsible for the Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.providerReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDERREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDERREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:providerreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDERREFERENCE = new ca.uhn.fhir.model.api.Include("Claim:providerreference").toLocked();

 /**
   * Search parameter: <b>organizationidentifier</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.organizationIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationidentifier", path="Claim.organization.as(Identifier)", description="The reference to the providing organization", type="token" )
  public static final String SP_ORGANIZATIONIDENTIFIER = "organizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationidentifier</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.organizationIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>patientreference</b>
   * <p>
   * Description: <b>Patient receiving the services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.patientReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patientreference", path="Claim.patient.as(Reference)", description="Patient receiving the services", type="reference" )
  public static final String SP_PATIENTREFERENCE = "patientreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patientreference</b>
   * <p>
   * Description: <b>Patient receiving the services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.patientReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENTREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENTREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:patientreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENTREFERENCE = new ca.uhn.fhir.model.api.Include("Claim:patientreference").toLocked();

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
   * Search parameter: <b>targetidentifier</b>
   * <p>
   * Description: <b>The target payor/insurer for the Claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.targetIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="targetidentifier", path="Claim.target.as(Identifier)", description="The target payor/insurer for the Claim", type="token" )
  public static final String SP_TARGETIDENTIFIER = "targetidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>targetidentifier</b>
   * <p>
   * Description: <b>The target payor/insurer for the Claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.targetIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TARGETIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TARGETIDENTIFIER);

 /**
   * Search parameter: <b>organizationreference</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.organizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationreference", path="Claim.organization.as(Reference)", description="The reference to the providing organization", type="reference" )
  public static final String SP_ORGANIZATIONREFERENCE = "organizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationreference</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Claim.organizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Claim:organizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("Claim:organizationreference").toLocked();

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
   * Search parameter: <b>provideridentifier</b>
   * <p>
   * Description: <b>Provider responsible for the Claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.providerIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provideridentifier", path="Claim.provider.as(Identifier)", description="Provider responsible for the Claim", type="token" )
  public static final String SP_PROVIDERIDENTIFIER = "provideridentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provideridentifier</b>
   * <p>
   * Description: <b>Provider responsible for the Claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Claim.providerIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PROVIDERIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PROVIDERIDENTIFIER);


}

