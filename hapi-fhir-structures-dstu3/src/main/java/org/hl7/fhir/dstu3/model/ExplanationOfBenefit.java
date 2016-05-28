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

// Generated on Sat, May 28, 2016 10:02-0400 for FHIR v1.4.0

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
import org.hl7.fhir.dstu3.exceptions.FHIRException;
/**
 * This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally account balance information, for informing the subscriber of the benefits provided.
 */
@ResourceDef(name="ExplanationOfBenefit", profile="http://hl7.org/fhir/Profile/ExplanationOfBenefit")
public class ExplanationOfBenefit extends DomainResource {

    public enum ClaimType2 {
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
         * added to help the parsers with the generic types
         */
        NULL;
        public static ClaimType2 fromCode(String codeString) throws FHIRException {
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ClaimType2 code '"+codeString+"'");
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
            case INSTITUTIONAL: return "http://hl7.org/fhir/claim-type-link2";
            case ORAL: return "http://hl7.org/fhir/claim-type-link2";
            case PHARMACY: return "http://hl7.org/fhir/claim-type-link2";
            case PROFESSIONAL: return "http://hl7.org/fhir/claim-type-link2";
            case VISION: return "http://hl7.org/fhir/claim-type-link2";
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

  public static class ClaimType2EnumFactory implements EnumFactory<ClaimType2> {
    public ClaimType2 fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("institutional".equals(codeString))
          return ClaimType2.INSTITUTIONAL;
        if ("oral".equals(codeString))
          return ClaimType2.ORAL;
        if ("pharmacy".equals(codeString))
          return ClaimType2.PHARMACY;
        if ("professional".equals(codeString))
          return ClaimType2.PROFESSIONAL;
        if ("vision".equals(codeString))
          return ClaimType2.VISION;
        throw new IllegalArgumentException("Unknown ClaimType2 code '"+codeString+"'");
        }
        public Enumeration<ClaimType2> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("institutional".equals(codeString))
          return new Enumeration<ClaimType2>(this, ClaimType2.INSTITUTIONAL);
        if ("oral".equals(codeString))
          return new Enumeration<ClaimType2>(this, ClaimType2.ORAL);
        if ("pharmacy".equals(codeString))
          return new Enumeration<ClaimType2>(this, ClaimType2.PHARMACY);
        if ("professional".equals(codeString))
          return new Enumeration<ClaimType2>(this, ClaimType2.PROFESSIONAL);
        if ("vision".equals(codeString))
          return new Enumeration<ClaimType2>(this, ClaimType2.VISION);
        throw new FHIRException("Unknown ClaimType2 code '"+codeString+"'");
        }
    public String toCode(ClaimType2 code) {
      if (code == ClaimType2.INSTITUTIONAL)
        return "institutional";
      if (code == ClaimType2.ORAL)
        return "oral";
      if (code == ClaimType2.PHARMACY)
        return "pharmacy";
      if (code == ClaimType2.PROFESSIONAL)
        return "professional";
      if (code == ClaimType2.VISION)
        return "vision";
      return "?";
      }
    public String toSystem(ClaimType2 code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class RelatedClaimComponent extends BackboneElement implements IBaseBackboneElement {
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
      public RelatedClaimComponent() {
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
        public RelatedClaimComponent setClaim(Type value) { 
          this.claim = value;
          return this;
        }

        /**
         * @return {@link #relationship} (For example prior or umbrella.)
         */
        public Coding getRelationship() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RelatedClaimComponent.relationship");
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
        public RelatedClaimComponent setRelationship(Coding value) { 
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
        @Child(name = "type", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=true)
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, party);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.payee";

  }

  }

    @Block()
    public static class InformationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The general class of the information supplied: information; exception; accident, employment; onset, etc.
         */
        @Child(name = "category", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Category of information", formalDefinition="The general class of the information supplied: information; exception; accident, employment; onset, etc." )
        protected Coding category;

        /**
         * System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.
         */
        @Child(name = "code", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of information", formalDefinition="System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication." )
        protected Coding code;

        /**
         * A reason code which supports the reporting of the information code or provides further details for the information code.
         */
        @Child(name = "reason", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Supporting Reason", formalDefinition="A reason code which supports the reporting of the information code or provides further details for the information code." )
        protected Coding reason;

        /**
         * The date when or period to which this information refers.
         */
        @Child(name = "timing", type = {DateType.class, Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When it occurred", formalDefinition="The date when or period to which this information refers." )
        protected Type timing;

        /**
         * Additional data.
         */
        @Child(name = "value", type = {IntegerType.class, PositiveIntType.class, StringType.class, Address.class, Coding.class, Identifier.class, Quantity.class, Location.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Additional Data", formalDefinition="Additional data." )
        protected Type value;

        private static final long serialVersionUID = 1767888500L;

    /**
     * Constructor
     */
      public InformationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InformationComponent(Coding category, Coding code) {
        super();
        this.category = category;
        this.code = code;
      }

        /**
         * @return {@link #category} (The general class of the information supplied: information; exception; accident, employment; onset, etc.)
         */
        public Coding getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InformationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Coding(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (The general class of the information supplied: information; exception; accident, employment; onset, etc.)
         */
        public InformationComponent setCategory(Coding value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #code} (System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.)
         */
        public Coding getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InformationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Coding(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.)
         */
        public InformationComponent setCode(Coding value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #reason} (A reason code which supports the reporting of the information code or provides further details for the information code.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InformationComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new Coding(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (A reason code which supports the reporting of the information code or provides further details for the information code.)
         */
        public InformationComponent setReason(Coding value) { 
          this.reason = value;
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
        public InformationComponent setTiming(Type value) { 
          this.timing = value;
          return this;
        }

        /**
         * @return {@link #value} (Additional data.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (Additional data.)
         */
        public IntegerType getValueIntegerType() throws FHIRException { 
          if (!(this.value instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IntegerType) this.value;
        }

        public boolean hasValueIntegerType() { 
          return this.value instanceof IntegerType;
        }

        /**
         * @return {@link #value} (Additional data.)
         */
        public PositiveIntType getValuePositiveIntType() throws FHIRException { 
          if (!(this.value instanceof PositiveIntType))
            throw new FHIRException("Type mismatch: the type PositiveIntType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (PositiveIntType) this.value;
        }

        public boolean hasValuePositiveIntType() { 
          return this.value instanceof PositiveIntType;
        }

        /**
         * @return {@link #value} (Additional data.)
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
         * @return {@link #value} (Additional data.)
         */
        public Address getValueAddress() throws FHIRException { 
          if (!(this.value instanceof Address))
            throw new FHIRException("Type mismatch: the type Address was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Address) this.value;
        }

        public boolean hasValueAddress() { 
          return this.value instanceof Address;
        }

        /**
         * @return {@link #value} (Additional data.)
         */
        public Coding getValueCoding() throws FHIRException { 
          if (!(this.value instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Coding) this.value;
        }

        public boolean hasValueCoding() { 
          return this.value instanceof Coding;
        }

        /**
         * @return {@link #value} (Additional data.)
         */
        public Identifier getValueIdentifier() throws FHIRException { 
          if (!(this.value instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Identifier) this.value;
        }

        public boolean hasValueIdentifier() { 
          return this.value instanceof Identifier;
        }

        /**
         * @return {@link #value} (Additional data.)
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
         * @return {@link #value} (Additional data.)
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
         * @param value {@link #value} (Additional data.)
         */
        public InformationComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "Coding", "The general class of the information supplied: information; exception; accident, employment; onset, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("code", "Coding", "System and code pertaining to the specific information regarding special conditions relating to the setting, treatment or patient  for which care is sought which may influence the adjudication.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("reason", "Coding", "A reason code which supports the reporting of the information code or provides further details for the information code.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("timing[x]", "date|Period", "The date when or period to which this information refers.", 0, java.lang.Integer.MAX_VALUE, timing));
          childrenList.add(new Property("value[x]", "integer|positiveInt|string|Address|Coding|Identifier|Quantity|Reference(Location)", "Additional data.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Coding
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Coding
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // Coding
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCoding(value); // Coding
          break;
        case 3059181: // code
          this.code = castToCoding(value); // Coding
          break;
        case -934964668: // reason
          this.reason = castToCoding(value); // Coding
          break;
        case -873664438: // timing
          this.timing = (Type) value; // Type
          break;
        case 111972721: // value
          this.value = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category"))
          this.category = castToCoding(value); // Coding
        else if (name.equals("code"))
          this.code = castToCoding(value); // Coding
        else if (name.equals("reason"))
          this.reason = castToCoding(value); // Coding
        else if (name.equals("timing[x]"))
          this.timing = (Type) value; // Type
        else if (name.equals("value[x]"))
          this.value = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); // Coding
        case 3059181:  return getCode(); // Coding
        case -934964668:  return getReason(); // Coding
        case 164632566:  return getTiming(); // Type
        case -1410166417:  return getValue(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new Coding();
          return this.category;
        }
        else if (name.equals("code")) {
          this.code = new Coding();
          return this.code;
        }
        else if (name.equals("reason")) {
          this.reason = new Coding();
          return this.reason;
        }
        else if (name.equals("timingDate")) {
          this.timing = new DateType();
          return this.timing;
        }
        else if (name.equals("timingPeriod")) {
          this.timing = new Period();
          return this.timing;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valuePositiveInt")) {
          this.value = new PositiveIntType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueAddress")) {
          this.value = new Address();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueIdentifier")) {
          this.value = new Identifier();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public InformationComponent copy() {
        InformationComponent dst = new InformationComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.timing = timing == null ? null : timing.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof InformationComponent))
          return false;
        InformationComponent o = (InformationComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(reason, o.reason, true)
           && compareDeep(timing, o.timing, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof InformationComponent))
          return false;
        InformationComponent o = (InformationComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, code, reason, timing
          , value);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.information";

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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, diagnosis);
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
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.sequence");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.date");
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, date, procedure
          );
      }

  public String fhirType() {
    return "ExplanationOfBenefit.procedure";

  }

  }

    @Block()
    public static class CoverageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reference to the program or plan identification, underwriter or payor.
         */
        @Child(name = "coverage", type = {Identifier.class, Coverage.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the program or plan identification, underwriter or payor." )
        protected Type coverage;

        /**
         * A list of references from the Insurer to which these services pertain.
         */
        @Child(name = "preAuthRef", type = {StringType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Pre-Authorization/Determination Reference", formalDefinition="A list of references from the Insurer to which these services pertain." )
        protected List<StringType> preAuthRef;

        private static final long serialVersionUID = -21571213L;

    /**
     * Constructor
     */
      public CoverageComponent() {
        super();
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
        public CoverageComponent setPreAuthRef(List<StringType> thePreAuthRef) { 
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
          childrenList.add(new Property("coverage[x]", "Identifier|Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, coverage));
          childrenList.add(new Property("preAuthRef", "string", "A list of references from the Insurer to which these services pertain.", 0, java.lang.Integer.MAX_VALUE, preAuthRef));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Type
        case 522246568: /*preAuthRef*/ return this.preAuthRef == null ? new Base[0] : this.preAuthRef.toArray(new Base[this.preAuthRef.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -351767064: // coverage
          this.coverage = (Type) value; // Type
          break;
        case 522246568: // preAuthRef
          this.getPreAuthRef().add(castToString(value)); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("coverage[x]"))
          this.coverage = (Type) value; // Type
        else if (name.equals("preAuthRef"))
          this.getPreAuthRef().add(castToString(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 227689880:  return getCoverage(); // Type
        case 522246568: throw new FHIRException("Cannot make property preAuthRef as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("coverageIdentifier")) {
          this.coverage = new Identifier();
          return this.coverage;
        }
        else if (name.equals("coverageReference")) {
          this.coverage = new Reference();
          return this.coverage;
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
        return compareDeep(coverage, o.coverage, true) && compareDeep(preAuthRef, o.preAuthRef, true);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(coverage, preAuthRef);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.coverage";

  }

  }

    @Block()
    public static class ItemComponent extends BackboneElement implements IBaseBackboneElement {
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
         * The members of the team who provided the overall service as well as their role and whether responsible and qualifications.
         */
        @Child(name = "careTeam", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="", formalDefinition="The members of the team who provided the overall service as well as their role and whether responsible and qualifications." )
        protected List<CareTeamComponent> careTeam;

        /**
         * Diagnosis applicable for this service or product line.
         */
        @Child(name = "diagnosisLinkId", type = {PositiveIntType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Applicable diagnoses", formalDefinition="Diagnosis applicable for this service or product line." )
        protected List<PositiveIntType> diagnosisLinkId;

        /**
         * If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.
         */
        @Child(name = "service", type = {Coding.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Item Code", formalDefinition="If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied." )
        protected Coding service;

        /**
         * Unusual circumstances which may influence adjudication.
         */
        @Child(name = "serviceModifier", type = {Coding.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Service/Product modifiers", formalDefinition="Unusual circumstances which may influence adjudication." )
        protected List<Coding> serviceModifier;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.
         */
        @Child(name = "modifier", type = {Coding.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen." )
        protected List<Coding> modifier;

        /**
         * For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.
         */
        @Child(name = "programCode", type = {Coding.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Program specific reason for item inclusion", formalDefinition="For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program." )
        protected List<Coding> programCode;

        /**
         * The date or dates when the enclosed suite of services were performed or completed.
         */
        @Child(name = "serviced", type = {DateType.class, Period.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date or dates of Service", formalDefinition="The date or dates when the enclosed suite of services were performed or completed." )
        protected Type serviced;

        /**
         * Where the service was provided.
         */
        @Child(name = "place", type = {Coding.class}, order=10, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Place of service", formalDefinition="Where the service was provided." )
        protected Coding place;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=11, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=12, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=13, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=14, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=15, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name = "udi", type = {Device.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected List<Reference> udi;
        /**
         * The actual objects that are the target of the reference (List of Unique Device Identifiers associated with this line item.)
         */
        protected List<Device> udiTarget;


        /**
         * Physical service site on the patient (limb, tooth, etc).
         */
        @Child(name = "bodySite", type = {Coding.class}, order=17, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service Location", formalDefinition="Physical service site on the patient (limb, tooth, etc)." )
        protected Coding bodySite;

        /**
         * A region or surface of the site, eg. limb region or tooth surface(s).
         */
        @Child(name = "subSite", type = {Coding.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Service Sub-location", formalDefinition="A region or surface of the site, eg. limb region or tooth surface(s)." )
        protected List<Coding> subSite;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication details", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

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

        private static final long serialVersionUID = 730508899L;

    /**
     * Constructor
     */
      public ItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemComponent(PositiveIntType sequence, Coding type, Coding service) {
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
         * @return {@link #type} (The type of product or service.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.type");
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
        public ItemComponent setType(Coding value) { 
          this.type = value;
          return this;
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
        public ItemComponent setCareTeam(List<CareTeamComponent> theCareTeam) { 
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

        public ItemComponent addCareTeam(CareTeamComponent t) { //3
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
         * @return {@link #service} (If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.)
         */
        public Coding getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.service");
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
        public ItemComponent setService(Coding value) { 
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setServiceModifier(List<Coding> theServiceModifier) { 
          this.serviceModifier = theServiceModifier;
          return this;
        }

        public boolean hasServiceModifier() { 
          if (this.serviceModifier == null)
            return false;
          for (Coding item : this.serviceModifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addServiceModifier() { //3
          Coding t = new Coding();
          if (this.serviceModifier == null)
            this.serviceModifier = new ArrayList<Coding>();
          this.serviceModifier.add(t);
          return t;
        }

        public ItemComponent addServiceModifier(Coding t) { //3
          if (t == null)
            return this;
          if (this.serviceModifier == null)
            this.serviceModifier = new ArrayList<Coding>();
          this.serviceModifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #serviceModifier}, creating it if it does not already exist
         */
        public Coding getServiceModifierFirstRep() { 
          if (getServiceModifier().isEmpty()) {
            addServiceModifier();
          }
          return getServiceModifier().get(0);
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.)
         */
        public List<Coding> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<Coding>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setModifier(List<Coding> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (Coding item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addModifier() { //3
          Coding t = new Coding();
          if (this.modifier == null)
            this.modifier = new ArrayList<Coding>();
          this.modifier.add(t);
          return t;
        }

        public ItemComponent addModifier(Coding t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<Coding>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public Coding getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #programCode} (For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.)
         */
        public List<Coding> getProgramCode() { 
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          return this.programCode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setProgramCode(List<Coding> theProgramCode) { 
          this.programCode = theProgramCode;
          return this;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (Coding item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addProgramCode() { //3
          Coding t = new Coding();
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
          return t;
        }

        public ItemComponent addProgramCode(Coding t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #programCode}, creating it if it does not already exist
         */
        public Coding getProgramCodeFirstRep() { 
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
         * @return {@link #place} (Where the service was provided.)
         */
        public Coding getPlace() { 
          if (this.place == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.place");
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
        public ItemComponent setPlace(Coding value) { 
          this.place = value;
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
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.points");
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
        public ItemComponent setPointsElement(DecimalType value) { 
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
        public ItemComponent setPoints(BigDecimal value) { 
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
        public ItemComponent setPoints(long value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public ItemComponent setPoints(double value) { 
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
        public Coding getBodySite() { 
          if (this.bodySite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.bodySite");
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
        public ItemComponent setBodySite(Coding value) { 
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setSubSite(List<Coding> theSubSite) { 
          this.subSite = theSubSite;
          return this;
        }

        public boolean hasSubSite() { 
          if (this.subSite == null)
            return false;
          for (Coding item : this.subSite)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addSubSite() { //3
          Coding t = new Coding();
          if (this.subSite == null)
            this.subSite = new ArrayList<Coding>();
          this.subSite.add(t);
          return t;
        }

        public ItemComponent addSubSite(Coding t) { //3
          if (t == null)
            return this;
          if (this.subSite == null)
            this.subSite = new ArrayList<Coding>();
          this.subSite.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #subSite}, creating it if it does not already exist
         */
        public Coding getSubSiteFirstRep() { 
          if (getSubSite().isEmpty()) {
            addSubSite();
          }
          return getSubSite().get(0);
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

        /**
         * @return {@link #prosthesis} (The materials and placement date of prior fixed prosthesis.)
         */
        public ProsthesisComponent getProsthesis() { 
          if (this.prosthesis == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.prosthesis");
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
        public ItemComponent setProsthesis(ProsthesisComponent value) { 
          this.prosthesis = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("careTeam", "", "The members of the team who provided the overall service as well as their role and whether responsible and qualifications.", 0, java.lang.Integer.MAX_VALUE, careTeam));
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
          childrenList.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("detail", "", "Second tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, detail));
          childrenList.add(new Property("prosthesis", "", "The materials and placement date of prior fixed prosthesis.", 0, java.lang.Integer.MAX_VALUE, prosthesis));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case -7323378: /*careTeam*/ return this.careTeam == null ? new Base[0] : this.careTeam.toArray(new Base[this.careTeam.size()]); // CareTeamComponent
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
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
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
        case -7323378: // careTeam
          this.getCareTeam().add((CareTeamComponent) value); // CareTeamComponent
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
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          break;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
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
        else if (name.equals("careTeam"))
          this.getCareTeam().add((CareTeamComponent) value);
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
        else if (name.equals("noteNumber"))
          this.getNoteNumber().add(castToPositiveInt(value));
        else if (name.equals("adjudication"))
          this.getAdjudication().add((AdjudicationComponent) value);
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
        case -7323378:  return addCareTeam(); // CareTeamComponent
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
        case -1110033957: throw new FHIRException("Cannot make property noteNumber as it is not a complex type"); // PositiveIntType
        case -231349275:  return addAdjudication(); // AdjudicationComponent
        case -1335224239:  return addDetail(); // DetailComponent
        case -2138744398:  return getProsthesis(); // ProsthesisComponent
        default: return super.makeProperty(hash, name);
        }

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
        else if (name.equals("careTeam")) {
          return addCareTeam();
        }
        else if (name.equals("diagnosisLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.diagnosisLinkId");
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
          return addUdi();
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new Coding();
          return this.bodySite;
        }
        else if (name.equals("subSite")) {
          return addSubSite();
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

      public ItemComponent copy() {
        ItemComponent dst = new ItemComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.type = type == null ? null : type.copy();
        if (careTeam != null) {
          dst.careTeam = new ArrayList<CareTeamComponent>();
          for (CareTeamComponent i : careTeam)
            dst.careTeam.add(i.copy());
        };
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
        dst.prosthesis = prosthesis == null ? null : prosthesis.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ItemComponent))
          return false;
        ItemComponent o = (ItemComponent) other;
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(careTeam, o.careTeam, true)
           && compareDeep(diagnosisLinkId, o.diagnosisLinkId, true) && compareDeep(service, o.service, true)
           && compareDeep(serviceModifier, o.serviceModifier, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(serviced, o.serviced, true) && compareDeep(place, o.place, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(points, o.points, true) && compareDeep(net, o.net, true) && compareDeep(udi, o.udi, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(subSite, o.subSite, true) && compareDeep(noteNumber, o.noteNumber, true)
           && compareDeep(adjudication, o.adjudication, true) && compareDeep(detail, o.detail, true) && compareDeep(prosthesis, o.prosthesis, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ItemComponent))
          return false;
        ItemComponent o = (ItemComponent) other;
        return compareValues(sequence, o.sequence, true) && compareValues(diagnosisLinkId, o.diagnosisLinkId, true)
           && compareValues(factor, o.factor, true) && compareValues(points, o.points, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, type, careTeam
          , diagnosisLinkId, service, serviceModifier, modifier, programCode, serviced, place
          , quantity, unitPrice, factor, points, net, udi, bodySite, subSite, noteNumber
          , adjudication, detail, prosthesis);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item";

  }

  }

    @Block()
    public static class CareTeamComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The members of the team who provided the overall service.
         */
        @Child(name = "provider", type = {Identifier.class, Practitioner.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="", formalDefinition="The members of the team who provided the overall service." )
        protected Type provider;

        /**
         * The practitioner who is billing and responsible for the claimed services rendered to the patient.
         */
        @Child(name = "responsible", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Billing practitioner", formalDefinition="The practitioner who is billing and responsible for the claimed services rendered to the patient." )
        protected BooleanType responsible;

        /**
         * The lead, assisting or supervising practitioner and their discipline if a multidisiplinary team.
         */
        @Child(name = "role", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Role on the team", formalDefinition="The lead, assisting or supervising practitioner and their discipline if a multidisiplinary team." )
        protected Coding role;

        /**
         * The qualification which is applicable for this service.
         */
        @Child(name = "qualification", type = {Coding.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type, classification or Specialization", formalDefinition="The qualification which is applicable for this service." )
        protected Coding qualification;

        private static final long serialVersionUID = 31082634L;

    /**
     * Constructor
     */
      public CareTeamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CareTeamComponent(Type provider) {
        super();
        this.provider = provider;
      }

        /**
         * @return {@link #provider} (The members of the team who provided the overall service.)
         */
        public Type getProvider() { 
          return this.provider;
        }

        /**
         * @return {@link #provider} (The members of the team who provided the overall service.)
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
         * @return {@link #provider} (The members of the team who provided the overall service.)
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
         * @param value {@link #provider} (The members of the team who provided the overall service.)
         */
        public CareTeamComponent setProvider(Type value) { 
          this.provider = value;
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
        public Coding getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new Coding(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (The lead, assisting or supervising practitioner and their discipline if a multidisiplinary team.)
         */
        public CareTeamComponent setRole(Coding value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #qualification} (The qualification which is applicable for this service.)
         */
        public Coding getQualification() { 
          if (this.qualification == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamComponent.qualification");
            else if (Configuration.doAutoCreate())
              this.qualification = new Coding(); // cc
          return this.qualification;
        }

        public boolean hasQualification() { 
          return this.qualification != null && !this.qualification.isEmpty();
        }

        /**
         * @param value {@link #qualification} (The qualification which is applicable for this service.)
         */
        public CareTeamComponent setQualification(Coding value) { 
          this.qualification = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("provider[x]", "Identifier|Reference(Practitioner)", "The members of the team who provided the overall service.", 0, java.lang.Integer.MAX_VALUE, provider));
          childrenList.add(new Property("responsible", "boolean", "The practitioner who is billing and responsible for the claimed services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, responsible));
          childrenList.add(new Property("role", "Coding", "The lead, assisting or supervising practitioner and their discipline if a multidisiplinary team.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("qualification", "Coding", "The qualification which is applicable for this service.", 0, java.lang.Integer.MAX_VALUE, qualification));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Type
        case 1847674614: /*responsible*/ return this.responsible == null ? new Base[0] : new Base[] {this.responsible}; // BooleanType
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // Coding
        case -631333393: /*qualification*/ return this.qualification == null ? new Base[0] : new Base[] {this.qualification}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -987494927: // provider
          this.provider = (Type) value; // Type
          break;
        case 1847674614: // responsible
          this.responsible = castToBoolean(value); // BooleanType
          break;
        case 3506294: // role
          this.role = castToCoding(value); // Coding
          break;
        case -631333393: // qualification
          this.qualification = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("provider[x]"))
          this.provider = (Type) value; // Type
        else if (name.equals("responsible"))
          this.responsible = castToBoolean(value); // BooleanType
        else if (name.equals("role"))
          this.role = castToCoding(value); // Coding
        else if (name.equals("qualification"))
          this.qualification = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 2064698607:  return getProvider(); // Type
        case 1847674614: throw new FHIRException("Cannot make property responsible as it is not a complex type"); // BooleanType
        case 3506294:  return getRole(); // Coding
        case -631333393:  return getQualification(); // Coding
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("providerIdentifier")) {
          this.provider = new Identifier();
          return this.provider;
        }
        else if (name.equals("providerReference")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("responsible")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.responsible");
        }
        else if (name.equals("role")) {
          this.role = new Coding();
          return this.role;
        }
        else if (name.equals("qualification")) {
          this.qualification = new Coding();
          return this.qualification;
        }
        else
          return super.addChild(name);
      }

      public CareTeamComponent copy() {
        CareTeamComponent dst = new CareTeamComponent();
        copyValues(dst);
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
        return compareDeep(provider, o.provider, true) && compareDeep(responsible, o.responsible, true)
           && compareDeep(role, o.role, true) && compareDeep(qualification, o.qualification, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CareTeamComponent))
          return false;
        CareTeamComponent o = (CareTeamComponent) other;
        return compareValues(responsible, o.responsible, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(provider, responsible, role
          , qualification);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.careTeam";

  }

  }

    @Block()
    public static class AdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
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
      public AdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AdjudicationComponent(Coding category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public Coding getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdjudicationComponent.category");
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
        public AdjudicationComponent setCategory(Coding value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdjudicationComponent.reason");
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
        public AdjudicationComponent setReason(Coding value) { 
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
          childrenList.add(new Property("category", "Coding", "Code indicating: Co-Pay, deductable, elegible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("reason", "Coding", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monitory amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
          childrenList.add(new Property("value", "decimal", "A non-monetary value for example a percentage. Mutually exclusive to the amount element above.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Coding
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // Coding
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // DecimalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCoding(value); // Coding
          break;
        case -934964668: // reason
          this.reason = castToCoding(value); // Coding
          break;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          break;
        case 111972721: // value
          this.value = castToDecimal(value); // DecimalType
          break;
        default: super.setProperty(hash, name, value);
        }

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
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); // Coding
        case -934964668:  return getReason(); // Coding
        case -1413853096:  return getAmount(); // Money
        case 111972721: throw new FHIRException("Cannot make property value as it is not a complex type"); // DecimalType
        default: return super.makeProperty(hash, name);
        }

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
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Detail adjudication", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * Third tier of goods and services.
         */
        @Child(name = "subDetail", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Additional items", formalDefinition="Third tier of goods and services." )
        protected List<SubDetailComponent> subDetail;

        private static final long serialVersionUID = 2082735277L;

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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailComponent setProgramCode(List<Coding> theProgramCode) { 
          this.programCode = theProgramCode;
          return this;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (Coding item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addProgramCode() { //3
          Coding t = new Coding();
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
          return t;
        }

        public DetailComponent addProgramCode(Coding t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #programCode}, creating it if it does not already exist
         */
        public Coding getProgramCodeFirstRep() { 
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
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("service", "Coding", "If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("programCode", "Coding", "For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.", 0, java.lang.Integer.MAX_VALUE, programCode));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Reference(Device)", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("adjudication", "@ExplanationOfBenefit.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
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
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
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
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
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
        else if (name.equals("adjudication"))
          this.getAdjudication().add((AdjudicationComponent) value);
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
        case -231349275:  return addAdjudication(); // AdjudicationComponent
        case -828829007:  return addSubDetail(); // SubDetailComponent
        default: return super.makeProperty(hash, name);
        }

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
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          return addUdi();
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
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(service, o.service, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(points, o.points, true) && compareDeep(net, o.net, true)
           && compareDeep(udi, o.udi, true) && compareDeep(adjudication, o.adjudication, true) && compareDeep(subDetail, o.subDetail, true)
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, type, service
          , programCode, quantity, unitPrice, factor, points, net, udi, adjudication, subDetail
          );
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


        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="SubDetail adjudication", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        private static final long serialVersionUID = -1376270092L;

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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubDetailComponent setProgramCode(List<Coding> theProgramCode) { 
          this.programCode = theProgramCode;
          return this;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (Coding item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addProgramCode() { //3
          Coding t = new Coding();
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
          return t;
        }

        public SubDetailComponent addProgramCode(Coding t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<Coding>();
          this.programCode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #programCode}, creating it if it does not already exist
         */
        public Coding getProgramCodeFirstRep() { 
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
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("service", "Coding", "The fee for an addittional service or product or charge.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("programCode", "Coding", "For programs which require reson codes for the inclusion, covering, of this billed item under the program or sub-program.", 0, java.lang.Integer.MAX_VALUE, programCode));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "The fee for an addittional service or product or charge.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addittional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Reference(Device)", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("adjudication", "@ExplanationOfBenefit.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
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
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
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
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
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
        else if (name.equals("adjudication"))
          this.getAdjudication().add((AdjudicationComponent) value);
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
        case -231349275:  return addAdjudication(); // AdjudicationComponent
        default: return super.makeProperty(hash, name);
        }

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
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("udi")) {
          return addUdi();
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
        return compareDeep(sequence, o.sequence, true) && compareDeep(type, o.type, true) && compareDeep(service, o.service, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(points, o.points, true) && compareDeep(net, o.net, true)
           && compareDeep(udi, o.udi, true) && compareDeep(adjudication, o.adjudication, true);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, type, service
          , programCode, quantity, unitPrice, factor, points, net, udi, adjudication);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.item.detail.subDetail";

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
         * Material of the prior denture or bridge prosthesis (Oral).
         */
        @Child(name = "priorMaterial", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Prosthetic Material", formalDefinition="Material of the prior denture or bridge prosthesis (Oral)." )
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
         * @return {@link #priorMaterial} (Material of the prior denture or bridge prosthesis (Oral).)
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
         * @param value {@link #priorMaterial} (Material of the prior denture or bridge prosthesis (Oral).)
         */
        public ProsthesisComponent setPriorMaterial(Coding value) { 
          this.priorMaterial = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("initial", "boolean", "Indicates whether this is the initial placement of a fixed prosthesis.", 0, java.lang.Integer.MAX_VALUE, initial));
          childrenList.add(new Property("priorDate", "date", "Date of the initial placement.", 0, java.lang.Integer.MAX_VALUE, priorDate));
          childrenList.add(new Property("priorMaterial", "Coding", "Material of the prior denture or bridge prosthesis (Oral).", 0, java.lang.Integer.MAX_VALUE, priorMaterial));
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(initial, priorDate, priorMaterial
          );
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
         * The fee charged for the professional service or product.
         */
        @Child(name = "fee", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Professional fee or Product charge", formalDefinition="The fee charged for the professional service or product." )
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
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Added items adjudication", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * The second tier service adjudications for payor added services.
         */
        @Child(name = "detail", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Added items details", formalDefinition="The second tier service adjudications for payor added services." )
        protected List<AddedItemsDetailComponent> detail;

        private static final long serialVersionUID = -110151821L;

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
         * @return {@link #noteNumberLinkId} (A list of note references to the notes provided below.)
         */
        public List<PositiveIntType> getNoteNumberLinkId() { 
          if (this.noteNumberLinkId == null)
            this.noteNumberLinkId = new ArrayList<PositiveIntType>();
          return this.noteNumberLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setNoteNumberLinkId(List<PositiveIntType> theNoteNumberLinkId) { 
          this.noteNumberLinkId = theNoteNumberLinkId;
          return this;
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
          childrenList.add(new Property("service", "Coding", "A code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("fee", "Money", "The fee charged for the professional service or product.", 0, java.lang.Integer.MAX_VALUE, fee));
          childrenList.add(new Property("noteNumberLinkId", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumberLinkId));
          childrenList.add(new Property("adjudication", "@ExplanationOfBenefit.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("detail", "", "The second tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return this.sequenceLinkId == null ? new Base[0] : this.sequenceLinkId.toArray(new Base[this.sequenceLinkId.size()]); // PositiveIntType
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // Coding
        case 101254: /*fee*/ return this.fee == null ? new Base[0] : new Base[] {this.fee}; // Money
        case -1859667856: /*noteNumberLinkId*/ return this.noteNumberLinkId == null ? new Base[0] : this.noteNumberLinkId.toArray(new Base[this.noteNumberLinkId.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // AddedItemsDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422298666: // sequenceLinkId
          this.getSequenceLinkId().add(castToPositiveInt(value)); // PositiveIntType
          break;
        case 1984153269: // service
          this.service = castToCoding(value); // Coding
          break;
        case 101254: // fee
          this.fee = castToMoney(value); // Money
          break;
        case -1859667856: // noteNumberLinkId
          this.getNoteNumberLinkId().add(castToPositiveInt(value)); // PositiveIntType
          break;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          break;
        case -1335224239: // detail
          this.getDetail().add((AddedItemsDetailComponent) value); // AddedItemsDetailComponent
          break;
        default: super.setProperty(hash, name, value);
        }

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
          this.getAdjudication().add((AdjudicationComponent) value);
        else if (name.equals("detail"))
          this.getDetail().add((AddedItemsDetailComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666: throw new FHIRException("Cannot make property sequenceLinkId as it is not a complex type"); // PositiveIntType
        case 1984153269:  return getService(); // Coding
        case 101254:  return getFee(); // Money
        case -1859667856: throw new FHIRException("Cannot make property noteNumberLinkId as it is not a complex type"); // PositiveIntType
        case -231349275:  return addAdjudication(); // AdjudicationComponent
        case -1335224239:  return addDetail(); // AddedItemsDetailComponent
        default: return super.makeProperty(hash, name);
        }

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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequenceLinkId, service, fee
          , noteNumberLinkId, adjudication, detail);
      }

  public String fhirType() {
    return "ExplanationOfBenefit.addItem";

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
         * The fee charged for the professional service or product.
         */
        @Child(name = "fee", type = {Money.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Professional fee or Product charge", formalDefinition="The fee charged for the professional service or product." )
        protected Money fee;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Added items detail adjudication", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        private static final long serialVersionUID = 2021670906L;

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
          childrenList.add(new Property("service", "Coding", "A code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("fee", "Money", "The fee charged for the professional service or product.", 0, java.lang.Integer.MAX_VALUE, fee));
          childrenList.add(new Property("adjudication", "@ExplanationOfBenefit.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // Coding
        case 101254: /*fee*/ return this.fee == null ? new Base[0] : new Base[] {this.fee}; // Money
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1984153269: // service
          this.service = castToCoding(value); // Coding
          break;
        case 101254: // fee
          this.fee = castToMoney(value); // Money
          break;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("service"))
          this.service = castToCoding(value); // Coding
        else if (name.equals("fee"))
          this.fee = castToMoney(value); // Money
        else if (name.equals("adjudication"))
          this.getAdjudication().add((AdjudicationComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1984153269:  return getService(); // Coding
        case 101254:  return getFee(); // Money
        case -231349275:  return addAdjudication(); // AdjudicationComponent
        default: return super.makeProperty(hash, name);
        }

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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(service, fee, adjudication
          );
      }

  public String fhirType() {
    return "ExplanationOfBenefit.addItem.detail";

  }

  }

    @Block()
    public static class NoteComponent extends BackboneElement implements IBaseBackboneElement {
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
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NoteComponent.type");
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
        public NoteComponent setType(Coding value) { 
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("number", "positiveInt", "An integer associated with each note which may be referred to from each service line item.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("type", "Coding", "The note purpose: Print/Display.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("text", "string", "The note text.", 0, java.lang.Integer.MAX_VALUE, text));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : new Base[] {this.number}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1034364087: // number
          this.number = castToPositiveInt(value); // PositiveIntType
          break;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 3556653: // text
          this.text = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

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
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087: throw new FHIRException("Cannot make property number as it is not a complex type"); // PositiveIntType
        case 3575610:  return getType(); // Coding
        case 3556653: throw new FHIRException("Cannot make property text as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

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

      public NoteComponent copy() {
        NoteComponent dst = new NoteComponent();
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
        if (!(other instanceof NoteComponent))
          return false;
        NoteComponent o = (NoteComponent) other;
        return compareDeep(number, o.number, true) && compareDeep(type, o.type, true) && compareDeep(text, o.text, true)
          ;
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(number, type, text);
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
          childrenList.add(new Property("category", "Coding", "Dental, Vision, Medical, Pharmacy, Rehab etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("subCategory", "Coding", "Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.", 0, java.lang.Integer.MAX_VALUE, subCategory));
          childrenList.add(new Property("network", "Coding", "Network designation.", 0, java.lang.Integer.MAX_VALUE, network));
          childrenList.add(new Property("unit", "Coding", "Unit designation: individual or family.", 0, java.lang.Integer.MAX_VALUE, unit));
          childrenList.add(new Property("term", "Coding", "The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.", 0, java.lang.Integer.MAX_VALUE, term));
          childrenList.add(new Property("financial", "", "Benefits Used to date.", 0, java.lang.Integer.MAX_VALUE, financial));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Coding
        case 1365024606: /*subCategory*/ return this.subCategory == null ? new Base[0] : new Base[] {this.subCategory}; // Coding
        case 1843485230: /*network*/ return this.network == null ? new Base[0] : new Base[] {this.network}; // Coding
        case 3594628: /*unit*/ return this.unit == null ? new Base[0] : new Base[] {this.unit}; // Coding
        case 3556460: /*term*/ return this.term == null ? new Base[0] : new Base[] {this.term}; // Coding
        case 357555337: /*financial*/ return this.financial == null ? new Base[0] : this.financial.toArray(new Base[this.financial.size()]); // BenefitComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCoding(value); // Coding
          break;
        case 1365024606: // subCategory
          this.subCategory = castToCoding(value); // Coding
          break;
        case 1843485230: // network
          this.network = castToCoding(value); // Coding
          break;
        case 3594628: // unit
          this.unit = castToCoding(value); // Coding
          break;
        case 3556460: // term
          this.term = castToCoding(value); // Coding
          break;
        case 357555337: // financial
          this.getFinancial().add((BenefitComponent) value); // BenefitComponent
          break;
        default: super.setProperty(hash, name, value);
        }

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
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); // Coding
        case 1365024606:  return getSubCategory(); // Coding
        case 1843485230:  return getNetwork(); // Coding
        case 3594628:  return getUnit(); // Coding
        case 3556460:  return getTerm(); // Coding
        case 357555337:  return addFinancial(); // BenefitComponent
        default: return super.makeProperty(hash, name);
        }

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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, subCategory, network
          , unit, term, financial);
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
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case -222710633: /*benefit*/ return this.benefit == null ? new Base[0] : new Base[] {this.benefit}; // Type
        case -549981964: /*benefitUsed*/ return this.benefitUsed == null ? new Base[0] : new Base[] {this.benefitUsed}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case -222710633: // benefit
          this.benefit = (Type) value; // Type
          break;
        case -549981964: // benefitUsed
          this.benefitUsed = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

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
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // Coding
        case 952095881:  return getBenefit(); // Type
        case 787635980:  return getBenefitUsed(); // Type
        default: return super.makeProperty(hash, name);
        }

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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, benefit, benefitUsed
          );
      }

  public String fhirType() {
    return "ExplanationOfBenefit.benefitBalance.financial";

  }

  }

    /**
     * The EOB Business Identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier", formalDefinition="The EOB Business Identifier." )
    protected List<Identifier> identifier;

    /**
     * The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.
     */
    @Child(name = "claim", type = {Identifier.class, Claim.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Claim reference", formalDefinition="The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number." )
    protected Type claim;

    /**
     * The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.
     */
    @Child(name = "claimResponse", type = {Identifier.class, ClaimResponse.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Claim response reference", formalDefinition="The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number." )
    protected Type claimResponse;

    /**
     * The category of claim.
     */
    @Child(name = "type", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="institutional | oral | pharmacy | professional | vision", formalDefinition="The category of claim." )
    protected Enumeration<ClaimType2> type;

    /**
     * A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.
     */
    @Child(name = "subType", type = {Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Finer grained claim type information", formalDefinition="A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType." )
    protected List<Coding> subType;

    /**
     * The version of the specification on which this instance relies.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Current specification followed", formalDefinition="The version of the specification on which this instance relies." )
    protected Coding ruleset;

    /**
     * The version of the specification from which the original instance was created.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original specification followed", formalDefinition="The version of the specification from which the original instance was created." )
    protected Coding originalRuleset;

    /**
     * The date when the EOB was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the EOB was created." )
    protected DateTimeType created;

    /**
     * The billable period for which charges are being submitted.
     */
    @Child(name = "billablePeriod", type = {Period.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period for charge submission", formalDefinition="The billable period for which charges are being submitted." )
    protected Period billablePeriod;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * The provider which is responsible for the claim.
     */
    @Child(name = "provider", type = {Identifier.class, Practitioner.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible provider for the claim", formalDefinition="The provider which is responsible for the claim." )
    protected Type provider;

    /**
     * The provider which is responsible for the claim.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization for the claim", formalDefinition="The provider which is responsible for the claim." )
    protected Type organization;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Identifier.class, Location.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Type facility;

    /**
     * Other claims which are related to this claim such as prior claim versions or for related services.
     */
    @Child(name = "related", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Related Claims which may be revelant to processing this claimn", formalDefinition="Other claims which are related to this claim such as prior claim versions or for related services." )
    protected List<RelatedClaimComponent> related;

    /**
     * Prescription to support the dispensing of Pharmacy or Vision products.
     */
    @Child(name = "prescription", type = {Identifier.class, MedicationOrder.class, VisionPrescription.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Prescription", formalDefinition="Prescription to support the dispensing of Pharmacy or Vision products." )
    protected Type prescription;

    /**
     * Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.
     */
    @Child(name = "originalPrescription", type = {Identifier.class, MedicationOrder.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original Prescription", formalDefinition="Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products." )
    protected Type originalPrescription;

    /**
     * The party to be reimbursed for the services.
     */
    @Child(name = "payee", type = {}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payee", formalDefinition="The party to be reimbursed for the services." )
    protected PayeeComponent payee;

    /**
     * The referral resource which lists the date, practitioner, reason and other supporting information.
     */
    @Child(name = "referral", type = {Identifier.class, ReferralRequest.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Treatment Referral", formalDefinition="The referral resource which lists the date, practitioner, reason and other supporting information." )
    protected Type referral;

    /**
     * Additional information codes regarding the condition, situation, prior or concurrent issues. Often there are mutiple jurisdiction specific valuesets which are required.
     */
    @Child(name = "information", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="Additional information codes regarding the condition, situation, prior or concurrent issues. Often there are mutiple jurisdiction specific valuesets which are required." )
    protected List<InformationComponent> information;

    /**
     * Ordered list of patient diagnosis for which care is sought.
     */
    @Child(name = "diagnosis", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Diagnosis", formalDefinition="Ordered list of patient diagnosis for which care is sought." )
    protected List<DiagnosisComponent> diagnosis;

    /**
     * Ordered list of patient procedures performed to support the adjudication.
     */
    @Child(name = "procedure", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Procedures performed", formalDefinition="Ordered list of patient procedures performed to support the adjudication." )
    protected List<ProcedureComponent> procedure;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Identifier.class, Patient.class}, order=21, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Type patient;

    /**
     * Precedence (primary, secondary, etc.).
     */
    @Child(name = "precedence", type = {PositiveIntType.class}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Precedence (primary, secondary, etc.)", formalDefinition="Precedence (primary, secondary, etc.)." )
    protected PositiveIntType precedence;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "coverage", type = {}, order=23, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected CoverageComponent coverage;

    /**
     * First tier of goods and services.
     */
    @Child(name = "item", type = {}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Goods and Services", formalDefinition="First tier of goods and services." )
    protected List<ItemComponent> item;

    /**
     * The first tier service adjudications for payor added services.
     */
    @Child(name = "addItem", type = {}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Insurer added line items", formalDefinition="The first tier service adjudications for payor added services." )
    protected List<AddedItemComponent> addItem;

    /**
     * The total cost of the services reported.
     */
    @Child(name = "totalCost", type = {Money.class}, order=26, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total Cost of service from the Claim", formalDefinition="The total cost of the services reported." )
    protected Money totalCost;

    /**
     * The amount of deductable applied which was not allocated to any particular service line.
     */
    @Child(name = "unallocDeductable", type = {Money.class}, order=27, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unallocated deductable", formalDefinition="The amount of deductable applied which was not allocated to any particular service line." )
    protected Money unallocDeductable;

    /**
     * Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable).
     */
    @Child(name = "totalBenefit", type = {Money.class}, order=28, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total benefit payable for the Claim", formalDefinition="Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable)." )
    protected Money totalBenefit;

    /**
     * Adjustment to the payment of this transaction which is not related to adjudication of this transaction.
     */
    @Child(name = "paymentAdjustment", type = {Money.class}, order=29, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment adjustment for non-Claim issues", formalDefinition="Adjustment to the payment of this transaction which is not related to adjudication of this transaction." )
    protected Money paymentAdjustment;

    /**
     * Reason for the payment adjustment.
     */
    @Child(name = "paymentAdjustmentReason", type = {Coding.class}, order=30, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for Payment adjustment", formalDefinition="Reason for the payment adjustment." )
    protected Coding paymentAdjustmentReason;

    /**
     * Estimated payment date.
     */
    @Child(name = "paymentDate", type = {DateType.class}, order=31, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Expected date of Payment", formalDefinition="Estimated payment date." )
    protected DateType paymentDate;

    /**
     * Payable less any payment adjustment.
     */
    @Child(name = "paymentAmount", type = {Money.class}, order=32, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment amount", formalDefinition="Payable less any payment adjustment." )
    protected Money paymentAmount;

    /**
     * Payment identifer.
     */
    @Child(name = "paymentRef", type = {Identifier.class}, order=33, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment identifier", formalDefinition="Payment identifer." )
    protected Identifier paymentRef;

    /**
     * Status of funds reservation (For provider, for Patient, None).
     */
    @Child(name = "reserved", type = {Coding.class}, order=34, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Funds reserved status", formalDefinition="Status of funds reservation (For provider, for Patient, None)." )
    protected Coding reserved;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {Coding.class}, order=35, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    protected Coding form;

    /**
     * Note text.
     */
    @Child(name = "note", type = {}, order=36, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Processing notes", formalDefinition="Note text." )
    protected List<NoteComponent> note;

    /**
     * Balance by Benefit Category.
     */
    @Child(name = "benefitBalance", type = {}, order=37, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Balance by Benefit Category", formalDefinition="Balance by Benefit Category." )
    protected List<BenefitBalanceComponent> benefitBalance;

    private static final long serialVersionUID = 1889346579L;

  /**
   * Constructor
   */
    public ExplanationOfBenefit() {
      super();
    }

  /**
   * Constructor
   */
    public ExplanationOfBenefit(Enumeration<ClaimType2> type, Type patient, CoverageComponent coverage) {
      super();
      this.type = type;
      this.patient = patient;
      this.coverage = coverage;
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
     * @return {@link #claim} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public Type getClaim() { 
      return this.claim;
    }

    /**
     * @return {@link #claim} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
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
     * @return {@link #claim} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
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
     * @param value {@link #claim} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public ExplanationOfBenefit setClaim(Type value) { 
      this.claim = value;
      return this;
    }

    /**
     * @return {@link #claimResponse} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public Type getClaimResponse() { 
      return this.claimResponse;
    }

    /**
     * @return {@link #claimResponse} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public Identifier getClaimResponseIdentifier() throws FHIRException { 
      if (!(this.claimResponse instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.claimResponse.getClass().getName()+" was encountered");
      return (Identifier) this.claimResponse;
    }

    public boolean hasClaimResponseIdentifier() { 
      return this.claimResponse instanceof Identifier;
    }

    /**
     * @return {@link #claimResponse} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public Reference getClaimResponseReference() throws FHIRException { 
      if (!(this.claimResponse instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.claimResponse.getClass().getName()+" was encountered");
      return (Reference) this.claimResponse;
    }

    public boolean hasClaimResponseReference() { 
      return this.claimResponse instanceof Reference;
    }

    public boolean hasClaimResponse() { 
      return this.claimResponse != null && !this.claimResponse.isEmpty();
    }

    /**
     * @param value {@link #claimResponse} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    public ExplanationOfBenefit setClaimResponse(Type value) { 
      this.claimResponse = value;
      return this;
    }

    /**
     * @return {@link #type} (The category of claim.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<ClaimType2> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ExplanationOfBenefit.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<ClaimType2>(new ClaimType2EnumFactory()); // bb
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
    public ExplanationOfBenefit setTypeElement(Enumeration<ClaimType2> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The category of claim.
     */
    public ClaimType2 getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The category of claim.
     */
    public ExplanationOfBenefit setType(ClaimType2 value) { 
        if (this.type == null)
          this.type = new Enumeration<ClaimType2>(new ClaimType2EnumFactory());
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setSubType(List<Coding> theSubType) { 
      this.subType = theSubType;
      return this;
    }

    public boolean hasSubType() { 
      if (this.subType == null)
        return false;
      for (Coding item : this.subType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Coding addSubType() { //3
      Coding t = new Coding();
      if (this.subType == null)
        this.subType = new ArrayList<Coding>();
      this.subType.add(t);
      return t;
    }

    public ExplanationOfBenefit addSubType(Coding t) { //3
      if (t == null)
        return this;
      if (this.subType == null)
        this.subType = new ArrayList<Coding>();
      this.subType.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subType}, creating it if it does not already exist
     */
    public Coding getSubTypeFirstRep() { 
      if (getSubType().isEmpty()) {
        addSubType();
      }
      return getSubType().get(0);
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
    public Type getProvider() { 
      return this.provider;
    }

    /**
     * @return {@link #provider} (The provider which is responsible for the claim.)
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
     * @return {@link #provider} (The provider which is responsible for the claim.)
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
     * @param value {@link #provider} (The provider which is responsible for the claim.)
     */
    public ExplanationOfBenefit setProvider(Type value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #organization} (The provider which is responsible for the claim.)
     */
    public Type getOrganization() { 
      return this.organization;
    }

    /**
     * @return {@link #organization} (The provider which is responsible for the claim.)
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
     * @return {@link #organization} (The provider which is responsible for the claim.)
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
     * @param value {@link #organization} (The provider which is responsible for the claim.)
     */
    public ExplanationOfBenefit setOrganization(Type value) { 
      this.organization = value;
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
    public ExplanationOfBenefit setFacility(Type value) { 
      this.facility = value;
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
    public ExplanationOfBenefit setPrescription(Type value) { 
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
    public ExplanationOfBenefit setOriginalPrescription(Type value) { 
      this.originalPrescription = value;
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
    public ExplanationOfBenefit setReferral(Type value) { 
      this.referral = value;
      return this;
    }

    /**
     * @return {@link #information} (Additional information codes regarding the condition, situation, prior or concurrent issues. Often there are mutiple jurisdiction specific valuesets which are required.)
     */
    public List<InformationComponent> getInformation() { 
      if (this.information == null)
        this.information = new ArrayList<InformationComponent>();
      return this.information;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setInformation(List<InformationComponent> theInformation) { 
      this.information = theInformation;
      return this;
    }

    public boolean hasInformation() { 
      if (this.information == null)
        return false;
      for (InformationComponent item : this.information)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public InformationComponent addInformation() { //3
      InformationComponent t = new InformationComponent();
      if (this.information == null)
        this.information = new ArrayList<InformationComponent>();
      this.information.add(t);
      return t;
    }

    public ExplanationOfBenefit addInformation(InformationComponent t) { //3
      if (t == null)
        return this;
      if (this.information == null)
        this.information = new ArrayList<InformationComponent>();
      this.information.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #information}, creating it if it does not already exist
     */
    public InformationComponent getInformationFirstRep() { 
      if (getInformation().isEmpty()) {
        addInformation();
      }
      return getInformation().get(0);
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
    public ExplanationOfBenefit setPatient(Type value) { 
      this.patient = value;
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
     * @return {@link #paymentDate} (Estimated payment date.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
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
     * @param value {@link #paymentDate} (Estimated payment date.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
     */
    public ExplanationOfBenefit setPaymentDateElement(DateType value) { 
      this.paymentDate = value;
      return this;
    }

    /**
     * @return Estimated payment date.
     */
    public Date getPaymentDate() { 
      return this.paymentDate == null ? null : this.paymentDate.getValue();
    }

    /**
     * @param value Estimated payment date.
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
    public List<NoteComponent> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<NoteComponent>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ExplanationOfBenefit setNote(List<NoteComponent> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (NoteComponent item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public NoteComponent addNote() { //3
      NoteComponent t = new NoteComponent();
      if (this.note == null)
        this.note = new ArrayList<NoteComponent>();
      this.note.add(t);
      return t;
    }

    public ExplanationOfBenefit addNote(NoteComponent t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<NoteComponent>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public NoteComponent getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
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
        childrenList.add(new Property("claim[x]", "Identifier|Reference(Claim)", "The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.", 0, java.lang.Integer.MAX_VALUE, claim));
        childrenList.add(new Property("claimResponse[x]", "Identifier|Reference(ClaimResponse)", "The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.", 0, java.lang.Integer.MAX_VALUE, claimResponse));
        childrenList.add(new Property("type", "code", "The category of claim.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subType", "Coding", "A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.", 0, java.lang.Integer.MAX_VALUE, subType));
        childrenList.add(new Property("ruleset", "Coding", "The version of the specification on which this instance relies.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The version of the specification from which the original instance was created.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the EOB was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("billablePeriod", "Period", "The billable period for which charges are being submitted.", 0, java.lang.Integer.MAX_VALUE, billablePeriod));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("provider[x]", "Identifier|Reference(Practitioner)", "The provider which is responsible for the claim.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The provider which is responsible for the claim.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("facility[x]", "Identifier|Reference(Location)", "Facility where the services were provided.", 0, java.lang.Integer.MAX_VALUE, facility));
        childrenList.add(new Property("related", "", "Other claims which are related to this claim such as prior claim versions or for related services.", 0, java.lang.Integer.MAX_VALUE, related));
        childrenList.add(new Property("prescription[x]", "Identifier|Reference(MedicationOrder|VisionPrescription)", "Prescription to support the dispensing of Pharmacy or Vision products.", 0, java.lang.Integer.MAX_VALUE, prescription));
        childrenList.add(new Property("originalPrescription[x]", "Identifier|Reference(MedicationOrder)", "Original prescription which has been superceded by this prescription to support the dispensing of pharmacy services, medications or products.", 0, java.lang.Integer.MAX_VALUE, originalPrescription));
        childrenList.add(new Property("payee", "", "The party to be reimbursed for the services.", 0, java.lang.Integer.MAX_VALUE, payee));
        childrenList.add(new Property("referral[x]", "Identifier|Reference(ReferralRequest)", "The referral resource which lists the date, practitioner, reason and other supporting information.", 0, java.lang.Integer.MAX_VALUE, referral));
        childrenList.add(new Property("information", "", "Additional information codes regarding the condition, situation, prior or concurrent issues. Often there are mutiple jurisdiction specific valuesets which are required.", 0, java.lang.Integer.MAX_VALUE, information));
        childrenList.add(new Property("diagnosis", "", "Ordered list of patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        childrenList.add(new Property("procedure", "", "Ordered list of patient procedures performed to support the adjudication.", 0, java.lang.Integer.MAX_VALUE, procedure));
        childrenList.add(new Property("patient[x]", "Identifier|Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("precedence", "positiveInt", "Precedence (primary, secondary, etc.).", 0, java.lang.Integer.MAX_VALUE, precedence));
        childrenList.add(new Property("coverage", "", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("item", "", "First tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, item));
        childrenList.add(new Property("addItem", "", "The first tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, addItem));
        childrenList.add(new Property("totalCost", "Money", "The total cost of the services reported.", 0, java.lang.Integer.MAX_VALUE, totalCost));
        childrenList.add(new Property("unallocDeductable", "Money", "The amount of deductable applied which was not allocated to any particular service line.", 0, java.lang.Integer.MAX_VALUE, unallocDeductable));
        childrenList.add(new Property("totalBenefit", "Money", "Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductable).", 0, java.lang.Integer.MAX_VALUE, totalBenefit));
        childrenList.add(new Property("paymentAdjustment", "Money", "Adjustment to the payment of this transaction which is not related to adjudication of this transaction.", 0, java.lang.Integer.MAX_VALUE, paymentAdjustment));
        childrenList.add(new Property("paymentAdjustmentReason", "Coding", "Reason for the payment adjustment.", 0, java.lang.Integer.MAX_VALUE, paymentAdjustmentReason));
        childrenList.add(new Property("paymentDate", "date", "Estimated payment date.", 0, java.lang.Integer.MAX_VALUE, paymentDate));
        childrenList.add(new Property("paymentAmount", "Money", "Payable less any payment adjustment.", 0, java.lang.Integer.MAX_VALUE, paymentAmount));
        childrenList.add(new Property("paymentRef", "Identifier", "Payment identifer.", 0, java.lang.Integer.MAX_VALUE, paymentRef));
        childrenList.add(new Property("reserved", "Coding", "Status of funds reservation (For provider, for Patient, None).", 0, java.lang.Integer.MAX_VALUE, reserved));
        childrenList.add(new Property("form", "Coding", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("note", "", "Note text.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("benefitBalance", "", "Balance by Benefit Category.", 0, java.lang.Integer.MAX_VALUE, benefitBalance));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 94742588: /*claim*/ return this.claim == null ? new Base[0] : new Base[] {this.claim}; // Type
        case 689513629: /*claimResponse*/ return this.claimResponse == null ? new Base[0] : new Base[] {this.claimResponse}; // Type
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ClaimType2>
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : this.subType.toArray(new Base[this.subType.size()]); // Coding
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -332066046: /*billablePeriod*/ return this.billablePeriod == null ? new Base[0] : new Base[] {this.billablePeriod}; // Period
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Type
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Type
        case 1090493483: /*related*/ return this.related == null ? new Base[0] : this.related.toArray(new Base[this.related.size()]); // RelatedClaimComponent
        case 460301338: /*prescription*/ return this.prescription == null ? new Base[0] : new Base[] {this.prescription}; // Type
        case -1814015861: /*originalPrescription*/ return this.originalPrescription == null ? new Base[0] : new Base[] {this.originalPrescription}; // Type
        case 106443592: /*payee*/ return this.payee == null ? new Base[0] : new Base[] {this.payee}; // PayeeComponent
        case -722568291: /*referral*/ return this.referral == null ? new Base[0] : new Base[] {this.referral}; // Type
        case 1968600364: /*information*/ return this.information == null ? new Base[0] : this.information.toArray(new Base[this.information.size()]); // InformationComponent
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : this.diagnosis.toArray(new Base[this.diagnosis.size()]); // DiagnosisComponent
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : this.procedure.toArray(new Base[this.procedure.size()]); // ProcedureComponent
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Type
        case 159695370: /*precedence*/ return this.precedence == null ? new Base[0] : new Base[] {this.precedence}; // PositiveIntType
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // CoverageComponent
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // ItemComponent
        case -1148899500: /*addItem*/ return this.addItem == null ? new Base[0] : this.addItem.toArray(new Base[this.addItem.size()]); // AddedItemComponent
        case -577782479: /*totalCost*/ return this.totalCost == null ? new Base[0] : new Base[] {this.totalCost}; // Money
        case 2096309753: /*unallocDeductable*/ return this.unallocDeductable == null ? new Base[0] : new Base[] {this.unallocDeductable}; // Money
        case 332332211: /*totalBenefit*/ return this.totalBenefit == null ? new Base[0] : new Base[] {this.totalBenefit}; // Money
        case 856402963: /*paymentAdjustment*/ return this.paymentAdjustment == null ? new Base[0] : new Base[] {this.paymentAdjustment}; // Money
        case -1386508233: /*paymentAdjustmentReason*/ return this.paymentAdjustmentReason == null ? new Base[0] : new Base[] {this.paymentAdjustmentReason}; // Coding
        case -1540873516: /*paymentDate*/ return this.paymentDate == null ? new Base[0] : new Base[] {this.paymentDate}; // DateType
        case 909332990: /*paymentAmount*/ return this.paymentAmount == null ? new Base[0] : new Base[] {this.paymentAmount}; // Money
        case 1612875949: /*paymentRef*/ return this.paymentRef == null ? new Base[0] : new Base[] {this.paymentRef}; // Identifier
        case -350385368: /*reserved*/ return this.reserved == null ? new Base[0] : new Base[] {this.reserved}; // Coding
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // Coding
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // NoteComponent
        case 596003397: /*benefitBalance*/ return this.benefitBalance == null ? new Base[0] : this.benefitBalance.toArray(new Base[this.benefitBalance.size()]); // BenefitBalanceComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 94742588: // claim
          this.claim = (Type) value; // Type
          break;
        case 689513629: // claimResponse
          this.claimResponse = (Type) value; // Type
          break;
        case 3575610: // type
          this.type = new ClaimType2EnumFactory().fromType(value); // Enumeration<ClaimType2>
          break;
        case -1868521062: // subType
          this.getSubType().add(castToCoding(value)); // Coding
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
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
          break;
        case -987494927: // provider
          this.provider = (Type) value; // Type
          break;
        case 1178922291: // organization
          this.organization = (Type) value; // Type
          break;
        case 501116579: // facility
          this.facility = (Type) value; // Type
          break;
        case 1090493483: // related
          this.getRelated().add((RelatedClaimComponent) value); // RelatedClaimComponent
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
        case 1968600364: // information
          this.getInformation().add((InformationComponent) value); // InformationComponent
          break;
        case 1196993265: // diagnosis
          this.getDiagnosis().add((DiagnosisComponent) value); // DiagnosisComponent
          break;
        case -1095204141: // procedure
          this.getProcedure().add((ProcedureComponent) value); // ProcedureComponent
          break;
        case -791418107: // patient
          this.patient = (Type) value; // Type
          break;
        case 159695370: // precedence
          this.precedence = castToPositiveInt(value); // PositiveIntType
          break;
        case -351767064: // coverage
          this.coverage = (CoverageComponent) value; // CoverageComponent
          break;
        case 3242771: // item
          this.getItem().add((ItemComponent) value); // ItemComponent
          break;
        case -1148899500: // addItem
          this.getAddItem().add((AddedItemComponent) value); // AddedItemComponent
          break;
        case -577782479: // totalCost
          this.totalCost = castToMoney(value); // Money
          break;
        case 2096309753: // unallocDeductable
          this.unallocDeductable = castToMoney(value); // Money
          break;
        case 332332211: // totalBenefit
          this.totalBenefit = castToMoney(value); // Money
          break;
        case 856402963: // paymentAdjustment
          this.paymentAdjustment = castToMoney(value); // Money
          break;
        case -1386508233: // paymentAdjustmentReason
          this.paymentAdjustmentReason = castToCoding(value); // Coding
          break;
        case -1540873516: // paymentDate
          this.paymentDate = castToDate(value); // DateType
          break;
        case 909332990: // paymentAmount
          this.paymentAmount = castToMoney(value); // Money
          break;
        case 1612875949: // paymentRef
          this.paymentRef = castToIdentifier(value); // Identifier
          break;
        case -350385368: // reserved
          this.reserved = castToCoding(value); // Coding
          break;
        case 3148996: // form
          this.form = castToCoding(value); // Coding
          break;
        case 3387378: // note
          this.getNote().add((NoteComponent) value); // NoteComponent
          break;
        case 596003397: // benefitBalance
          this.getBenefitBalance().add((BenefitBalanceComponent) value); // BenefitBalanceComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("claim[x]"))
          this.claim = (Type) value; // Type
        else if (name.equals("claimResponse[x]"))
          this.claimResponse = (Type) value; // Type
        else if (name.equals("type"))
          this.type = new ClaimType2EnumFactory().fromType(value); // Enumeration<ClaimType2>
        else if (name.equals("subType"))
          this.getSubType().add(castToCoding(value));
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
        else if (name.equals("provider[x]"))
          this.provider = (Type) value; // Type
        else if (name.equals("organization[x]"))
          this.organization = (Type) value; // Type
        else if (name.equals("facility[x]"))
          this.facility = (Type) value; // Type
        else if (name.equals("related"))
          this.getRelated().add((RelatedClaimComponent) value);
        else if (name.equals("prescription[x]"))
          this.prescription = (Type) value; // Type
        else if (name.equals("originalPrescription[x]"))
          this.originalPrescription = (Type) value; // Type
        else if (name.equals("payee"))
          this.payee = (PayeeComponent) value; // PayeeComponent
        else if (name.equals("referral[x]"))
          this.referral = (Type) value; // Type
        else if (name.equals("information"))
          this.getInformation().add((InformationComponent) value);
        else if (name.equals("diagnosis"))
          this.getDiagnosis().add((DiagnosisComponent) value);
        else if (name.equals("procedure"))
          this.getProcedure().add((ProcedureComponent) value);
        else if (name.equals("patient[x]"))
          this.patient = (Type) value; // Type
        else if (name.equals("precedence"))
          this.precedence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("coverage"))
          this.coverage = (CoverageComponent) value; // CoverageComponent
        else if (name.equals("item"))
          this.getItem().add((ItemComponent) value);
        else if (name.equals("addItem"))
          this.getAddItem().add((AddedItemComponent) value);
        else if (name.equals("totalCost"))
          this.totalCost = castToMoney(value); // Money
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
          this.getNote().add((NoteComponent) value);
        else if (name.equals("benefitBalance"))
          this.getBenefitBalance().add((BenefitBalanceComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 683016900:  return getClaim(); // Type
        case -1527963965:  return getClaimResponse(); // Type
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<ClaimType2>
        case -1868521062:  return addSubType(); // Coding
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case -332066046:  return getBillablePeriod(); // Period
        case 583380919: throw new FHIRException("Cannot make property disposition as it is not a complex type"); // StringType
        case 2064698607:  return getProvider(); // Type
        case 1326483053:  return getOrganization(); // Type
        case -542224643:  return getFacility(); // Type
        case 1090493483:  return addRelated(); // RelatedClaimComponent
        case -993324506:  return getPrescription(); // Type
        case -2067905515:  return getOriginalPrescription(); // Type
        case 106443592:  return getPayee(); // PayeeComponent
        case 344221635:  return getReferral(); // Type
        case 1968600364:  return addInformation(); // InformationComponent
        case 1196993265:  return addDiagnosis(); // DiagnosisComponent
        case -1095204141:  return addProcedure(); // ProcedureComponent
        case -2061246629:  return getPatient(); // Type
        case 159695370: throw new FHIRException("Cannot make property precedence as it is not a complex type"); // PositiveIntType
        case -351767064:  return getCoverage(); // CoverageComponent
        case 3242771:  return addItem(); // ItemComponent
        case -1148899500:  return addAddItem(); // AddedItemComponent
        case -577782479:  return getTotalCost(); // Money
        case 2096309753:  return getUnallocDeductable(); // Money
        case 332332211:  return getTotalBenefit(); // Money
        case 856402963:  return getPaymentAdjustment(); // Money
        case -1386508233:  return getPaymentAdjustmentReason(); // Coding
        case -1540873516: throw new FHIRException("Cannot make property paymentDate as it is not a complex type"); // DateType
        case 909332990:  return getPaymentAmount(); // Money
        case 1612875949:  return getPaymentRef(); // Identifier
        case -350385368:  return getReserved(); // Coding
        case 3148996:  return getForm(); // Coding
        case 3387378:  return addNote(); // NoteComponent
        case 596003397:  return addBenefitBalance(); // BenefitBalanceComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("claimIdentifier")) {
          this.claim = new Identifier();
          return this.claim;
        }
        else if (name.equals("claimReference")) {
          this.claim = new Reference();
          return this.claim;
        }
        else if (name.equals("claimResponseIdentifier")) {
          this.claimResponse = new Identifier();
          return this.claimResponse;
        }
        else if (name.equals("claimResponseReference")) {
          this.claimResponse = new Reference();
          return this.claimResponse;
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ExplanationOfBenefit.type");
        }
        else if (name.equals("subType")) {
          return addSubType();
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
        else if (name.equals("information")) {
          return addInformation();
        }
        else if (name.equals("diagnosis")) {
          return addDiagnosis();
        }
        else if (name.equals("procedure")) {
          return addProcedure();
        }
        else if (name.equals("patientIdentifier")) {
          this.patient = new Identifier();
          return this.patient;
        }
        else if (name.equals("patientReference")) {
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
        dst.type = type == null ? null : type.copy();
        if (subType != null) {
          dst.subType = new ArrayList<Coding>();
          for (Coding i : subType)
            dst.subType.add(i.copy());
        };
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.billablePeriod = billablePeriod == null ? null : billablePeriod.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.facility = facility == null ? null : facility.copy();
        if (related != null) {
          dst.related = new ArrayList<RelatedClaimComponent>();
          for (RelatedClaimComponent i : related)
            dst.related.add(i.copy());
        };
        dst.prescription = prescription == null ? null : prescription.copy();
        dst.originalPrescription = originalPrescription == null ? null : originalPrescription.copy();
        dst.payee = payee == null ? null : payee.copy();
        dst.referral = referral == null ? null : referral.copy();
        if (information != null) {
          dst.information = new ArrayList<InformationComponent>();
          for (InformationComponent i : information)
            dst.information.add(i.copy());
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
        dst.patient = patient == null ? null : patient.copy();
        dst.precedence = precedence == null ? null : precedence.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
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
        dst.paymentAdjustment = paymentAdjustment == null ? null : paymentAdjustment.copy();
        dst.paymentAdjustmentReason = paymentAdjustmentReason == null ? null : paymentAdjustmentReason.copy();
        dst.paymentDate = paymentDate == null ? null : paymentDate.copy();
        dst.paymentAmount = paymentAmount == null ? null : paymentAmount.copy();
        dst.paymentRef = paymentRef == null ? null : paymentRef.copy();
        dst.reserved = reserved == null ? null : reserved.copy();
        dst.form = form == null ? null : form.copy();
        if (note != null) {
          dst.note = new ArrayList<NoteComponent>();
          for (NoteComponent i : note)
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
           && compareDeep(type, o.type, true) && compareDeep(subType, o.subType, true) && compareDeep(ruleset, o.ruleset, true)
           && compareDeep(originalRuleset, o.originalRuleset, true) && compareDeep(created, o.created, true)
           && compareDeep(billablePeriod, o.billablePeriod, true) && compareDeep(disposition, o.disposition, true)
           && compareDeep(provider, o.provider, true) && compareDeep(organization, o.organization, true) && compareDeep(facility, o.facility, true)
           && compareDeep(related, o.related, true) && compareDeep(prescription, o.prescription, true) && compareDeep(originalPrescription, o.originalPrescription, true)
           && compareDeep(payee, o.payee, true) && compareDeep(referral, o.referral, true) && compareDeep(information, o.information, true)
           && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(procedure, o.procedure, true) && compareDeep(patient, o.patient, true)
           && compareDeep(precedence, o.precedence, true) && compareDeep(coverage, o.coverage, true) && compareDeep(item, o.item, true)
           && compareDeep(addItem, o.addItem, true) && compareDeep(totalCost, o.totalCost, true) && compareDeep(unallocDeductable, o.unallocDeductable, true)
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
        return compareValues(type, o.type, true) && compareValues(created, o.created, true) && compareValues(disposition, o.disposition, true)
           && compareValues(precedence, o.precedence, true) && compareValues(paymentDate, o.paymentDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, claim, claimResponse
          , type, subType, ruleset, originalRuleset, created, billablePeriod, disposition
          , provider, organization, facility, related, prescription, originalPrescription
          , payee, referral, information, diagnosis, procedure, patient, precedence, coverage
          , item, addItem, totalCost, unallocDeductable, totalBenefit, paymentAdjustment, paymentAdjustmentReason
          , paymentDate, paymentAmount, paymentRef, reserved, form, note, benefitBalance
          );
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
   * Search parameter: <b>patientidentifier</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.patientIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patientidentifier", path="ExplanationOfBenefit.patient.as(Identifier)", description="The reference to the patient", type="token" )
  public static final String SP_PATIENTIDENTIFIER = "patientidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patientidentifier</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.patientIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PATIENTIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PATIENTIDENTIFIER);

 /**
   * Search parameter: <b>organizationidentifier</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.organizationIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationidentifier", path="ExplanationOfBenefit.organization.as(Identifier)", description="The reference to the providing organization", type="token" )
  public static final String SP_ORGANIZATIONIDENTIFIER = "organizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationidentifier</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.organizationIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>claimreference</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.claimReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="claimreference", path="ExplanationOfBenefit.claim.as(Reference)", description="The reference to the claim", type="reference" )
  public static final String SP_CLAIMREFERENCE = "claimreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>claimreference</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.claimReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CLAIMREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CLAIMREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:claimreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CLAIMREFERENCE = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:claimreference").toLocked();

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
   * Search parameter: <b>patientreference</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.patientReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patientreference", path="ExplanationOfBenefit.patient.as(Reference)", description="The reference to the patient", type="reference" )
  public static final String SP_PATIENTREFERENCE = "patientreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patientreference</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.patientReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENTREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENTREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:patientreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENTREFERENCE = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:patientreference").toLocked();

 /**
   * Search parameter: <b>providerreference</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.providerReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="providerreference", path="ExplanationOfBenefit.provider.as(Reference)", description="The reference to the provider", type="reference" )
  public static final String SP_PROVIDERREFERENCE = "providerreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>providerreference</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.providerReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDERREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDERREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:providerreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDERREFERENCE = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:providerreference").toLocked();

 /**
   * Search parameter: <b>organizationreference</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.organizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationreference", path="ExplanationOfBenefit.organization.as(Reference)", description="The reference to the providing organization", type="reference" )
  public static final String SP_ORGANIZATIONREFERENCE = "organizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationreference</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.organizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:organizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:organizationreference").toLocked();

 /**
   * Search parameter: <b>provideridentifier</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.providerIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provideridentifier", path="ExplanationOfBenefit.provider.as(Identifier)", description="The reference to the provider", type="token" )
  public static final String SP_PROVIDERIDENTIFIER = "provideridentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provideridentifier</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.providerIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PROVIDERIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PROVIDERIDENTIFIER);

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
   * Search parameter: <b>facilityreference</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.facilityReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facilityreference", path="ExplanationOfBenefit.facility.as(Reference)", description="Facility responsible for the goods and services", type="reference" )
  public static final String SP_FACILITYREFERENCE = "facilityreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facilityreference</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ExplanationOfBenefit.facilityReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FACILITYREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FACILITYREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ExplanationOfBenefit:facilityreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FACILITYREFERENCE = new ca.uhn.fhir.model.api.Include("ExplanationOfBenefit:facilityreference").toLocked();

 /**
   * Search parameter: <b>claimindentifier</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.claimIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="claimindentifier", path="ExplanationOfBenefit.claim.as(Identifier)", description="The reference to the claim", type="token" )
  public static final String SP_CLAIMINDENTIFIER = "claimindentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>claimindentifier</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.claimIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CLAIMINDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CLAIMINDENTIFIER);

 /**
   * Search parameter: <b>facilityidentifier</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.facilityIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facilityidentifier", path="ExplanationOfBenefit.facility.as(Identifier)", description="Facility responsible for the goods and services", type="token" )
  public static final String SP_FACILITYIDENTIFIER = "facilityidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facilityidentifier</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ExplanationOfBenefit.facilityIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FACILITYIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FACILITYIDENTIFIER);


}

