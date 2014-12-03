package org.hl7.fhir.instance.model;

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

// Generated on Tue, Dec 2, 2014 21:09+1100 for FHIR v0.3.0

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A formal agreement between parties regarding the conduct of business, exchange of information or other matters.
 */
@ResourceDef(name="Contract", profile="http://hl7.org/fhir/Profile/Contract")
public class Contract extends DomainResource {

    @Block()
    public static class ContractSignerComponent extends BackboneElement {
        /**
         * Party or role who is signing.
         */
        @Child(name="type", type={Coding.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Signer Type", formalDefinition="Party or role who is signing." )
        protected Coding type;

        /**
         * The DSIG signature contents in Base64.
         */
        @Child(name="singnature", type={StringType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Documentation Signature", formalDefinition="The DSIG signature contents in Base64." )
        protected StringType singnature;

        private static final long serialVersionUID = -825583495L;

      public ContractSignerComponent() {
        super();
      }

      public ContractSignerComponent(Coding type, StringType singnature) {
        super();
        this.type = type;
        this.singnature = singnature;
      }

        /**
         * @return {@link #type} (Party or role who is signing.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractSignerComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Party or role who is signing.)
         */
        public ContractSignerComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #singnature} (The DSIG signature contents in Base64.). This is the underlying object with id, value and extensions. The accessor "getSingnature" gives direct access to the value
         */
        public StringType getSingnatureElement() { 
          if (this.singnature == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractSignerComponent.singnature");
            else if (Configuration.doAutoCreate())
              this.singnature = new StringType();
          return this.singnature;
        }

        public boolean hasSingnatureElement() { 
          return this.singnature != null && !this.singnature.isEmpty();
        }

        public boolean hasSingnature() { 
          return this.singnature != null && !this.singnature.isEmpty();
        }

        /**
         * @param value {@link #singnature} (The DSIG signature contents in Base64.). This is the underlying object with id, value and extensions. The accessor "getSingnature" gives direct access to the value
         */
        public ContractSignerComponent setSingnatureElement(StringType value) { 
          this.singnature = value;
          return this;
        }

        /**
         * @return The DSIG signature contents in Base64.
         */
        public String getSingnature() { 
          return this.singnature == null ? null : this.singnature.getValue();
        }

        /**
         * @param value The DSIG signature contents in Base64.
         */
        public ContractSignerComponent setSingnature(String value) { 
            if (this.singnature == null)
              this.singnature = new StringType();
            this.singnature.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "Party or role who is signing.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("singnature", "string", "The DSIG signature contents in Base64.", 0, java.lang.Integer.MAX_VALUE, singnature));
        }

      public ContractSignerComponent copy() {
        ContractSignerComponent dst = new ContractSignerComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.singnature = singnature == null ? null : singnature.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (singnature == null || singnature.isEmpty())
          ;
      }

  }

    @Block()
    public static class ContractTermComponent extends BackboneElement {
        /**
         * Unique Id for this particular term.
         */
        @Child(name="identifier", type={Identifier.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Term identifier", formalDefinition="Unique Id for this particular term." )
        protected Identifier identifier;

        /**
         * The type of the term.
         */
        @Child(name="type", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Term type", formalDefinition="The type of the term." )
        protected CodeableConcept type;

        /**
         * The subttype of the term which is appropriate to the term type.
         */
        @Child(name="subtype", type={CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Term subtype", formalDefinition="The subttype of the term which is appropriate to the term type." )
        protected CodeableConcept subtype;

        /**
         * Who or what the contract term is about.
         */
        @Child(name="subject", type={}, order=4, min=0, max=1)
        @Description(shortDefinition="Subject for the Term", formalDefinition="Who or what the contract term is about." )
        protected Reference subject;

        /**
         * The actual object that is the target of the reference (Who or what the contract term is about.)
         */
        protected Resource subjectTarget;

        /**
         * Human readable form of the term of the contract.
         */
        @Child(name="text", type={StringType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Human readable term text", formalDefinition="Human readable form of the term of the contract." )
        protected StringType text;

        private static final long serialVersionUID = -697165954L;

      public ContractTermComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Unique Id for this particular term.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier();
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Unique Id for this particular term.)
         */
        public ContractTermComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #type} (The type of the term.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the term.)
         */
        public ContractTermComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #subtype} (The subttype of the term which is appropriate to the term type.)
         */
        public CodeableConcept getSubtype() { 
          if (this.subtype == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.subtype");
            else if (Configuration.doAutoCreate())
              this.subtype = new CodeableConcept();
          return this.subtype;
        }

        public boolean hasSubtype() { 
          return this.subtype != null && !this.subtype.isEmpty();
        }

        /**
         * @param value {@link #subtype} (The subttype of the term which is appropriate to the term type.)
         */
        public ContractTermComponent setSubtype(CodeableConcept value) { 
          this.subtype = value;
          return this;
        }

        /**
         * @return {@link #subject} (Who or what the contract term is about.)
         */
        public Reference getSubject() { 
          if (this.subject == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.subject");
            else if (Configuration.doAutoCreate())
              this.subject = new Reference();
          return this.subject;
        }

        public boolean hasSubject() { 
          return this.subject != null && !this.subject.isEmpty();
        }

        /**
         * @param value {@link #subject} (Who or what the contract term is about.)
         */
        public ContractTermComponent setSubject(Reference value) { 
          this.subject = value;
          return this;
        }

        /**
         * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what the contract term is about.)
         */
        public Resource getSubjectTarget() { 
          return this.subjectTarget;
        }

        /**
         * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what the contract term is about.)
         */
        public ContractTermComponent setSubjectTarget(Resource value) { 
          this.subjectTarget = value;
          return this;
        }

        /**
         * @return {@link #text} (Human readable form of the term of the contract.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType();
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Human readable form of the term of the contract.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public ContractTermComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Human readable form of the term of the contract.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Human readable form of the term of the contract.
         */
        public ContractTermComponent setText(String value) { 
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
          childrenList.add(new Property("identifier", "Identifier", "Unique Id for this particular term.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("type", "CodeableConcept", "The type of the term.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("subtype", "CodeableConcept", "The subttype of the term which is appropriate to the term type.", 0, java.lang.Integer.MAX_VALUE, subtype));
          childrenList.add(new Property("subject", "Reference(Any)", "Who or what the contract term is about.", 0, java.lang.Integer.MAX_VALUE, subject));
          childrenList.add(new Property("text", "string", "Human readable form of the term of the contract.", 0, java.lang.Integer.MAX_VALUE, text));
        }

      public ContractTermComponent copy() {
        ContractTermComponent dst = new ContractTermComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        dst.subtype = subtype == null ? null : subtype.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (subtype == null || subtype.isEmpty()) && (subject == null || subject.isEmpty()) && (text == null || text.isEmpty())
          ;
      }

  }

    /**
     * Unique Id for this contract.
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contract identifier", formalDefinition="Unique Id for this contract." )
    protected List<Identifier> identifier;

    /**
     * Who and/or what this is about: typically Patient, Organization, property.
     */
    @Child(name="subject", type={}, order=0, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Subject", formalDefinition="Who and/or what this is about: typically Patient, Organization, property." )
    protected List<Reference> subject;
    /**
     * The actual objects that are the target of the reference (Who and/or what this is about: typically Patient, Organization, property.)
     */
    protected List<Resource> subjectTarget;


    /**
     * Type of contract (Privacy-Security, Agreement, Insurance).
     */
    @Child(name="type", type={CodeableConcept.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Type of contract", formalDefinition="Type of contract (Privacy-Security, Agreement, Insurance)." )
    protected CodeableConcept type;

    /**
     * More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat).
     */
    @Child(name="subtype", type={CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Subtype of contract", formalDefinition="More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat)." )
    protected List<CodeableConcept> subtype;

    /**
     * When this was issued.
     */
    @Child(name="issued", type={DateTimeType.class}, order=3, min=0, max=1)
    @Description(shortDefinition="When this was issued", formalDefinition="When this was issued." )
    protected DateTimeType issued;

    /**
     * Relevant time/time-period when applicable.
     */
    @Child(name="applies", type={Period.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Effective time", formalDefinition="Relevant time/time-period when applicable." )
    protected Period applies;

    /**
     * The number of repetitions of a service or product.
     */
    @Child(name="quantity", type={Quantity.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
    protected Quantity quantity;

    /**
     * The unit price product.
     */
    @Child(name="unitPrice", type={Money.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="The unit price product." )
    protected Money unitPrice;

    /**
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     */
    @Child(name="factor", type={DecimalType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
    protected DecimalType factor;

    /**
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     */
    @Child(name="points", type={DecimalType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
    protected DecimalType points;

    /**
     * The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
     */
    @Child(name="net", type={Money.class}, order=9, min=0, max=1)
    @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
    protected Money net;

    /**
     * Contract author or responsible party.
     */
    @Child(name="author", type={Practitioner.class, RelatedPerson.class, Organization.class}, order=10, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contract author or responsible party", formalDefinition="Contract author or responsible party." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Contract author or responsible party.)
     */
    protected List<Resource> authorTarget;


    /**
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     */
    @Child(name="grantor", type={Practitioner.class, RelatedPerson.class, Organization.class, Patient.class}, order=11, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="First Party or delegator", formalDefinition="First Party to the contract, may be the party who confers or delegates the rights defined in the contract." )
    protected List<Reference> grantor;
    /**
     * The actual objects that are the target of the reference (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    protected List<Resource> grantorTarget;


    /**
     * The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.
     */
    @Child(name="grantee", type={Practitioner.class, RelatedPerson.class, Organization.class, Patient.class}, order=12, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Second Party or delegatee", formalDefinition="The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated." )
    protected List<Reference> grantee;
    /**
     * The actual objects that are the target of the reference (The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.)
     */
    protected List<Resource> granteeTarget;


    /**
     * Who witnesses the contract.
     */
    @Child(name="witness", type={Practitioner.class, RelatedPerson.class, Patient.class}, order=13, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Witness to the contract", formalDefinition="Who witnesses the contract." )
    protected List<Reference> witness;
    /**
     * The actual objects that are the target of the reference (Who witnesses the contract.)
     */
    protected List<Resource> witnessTarget;


    /**
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     */
    @Child(name="executor", type={Practitioner.class, RelatedPerson.class, Organization.class, Patient.class}, order=14, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Trustee", formalDefinition="First Party to the contract, may be the party who confers or delegates the rights defined in the contract." )
    protected List<Reference> executor;
    /**
     * The actual objects that are the target of the reference (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    protected List<Resource> executorTarget;


    /**
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     */
    @Child(name="notary", type={Practitioner.class, RelatedPerson.class, Organization.class, Patient.class}, order=15, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Notary Public", formalDefinition="First Party to the contract, may be the party who confers or delegates the rights defined in the contract." )
    protected List<Reference> notary;
    /**
     * The actual objects that are the target of the reference (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    protected List<Resource> notaryTarget;


    /**
     * List or contract signatures.
     */
    @Child(name="signer", type={}, order=16, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Signer", formalDefinition="List or contract signatures." )
    protected List<ContractSignerComponent> signer;

    /**
     * A contract provision.
     */
    @Child(name="term", type={}, order=17, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contract provisions", formalDefinition="A contract provision." )
    protected List<ContractTermComponent> term;

    /**
     * Friendly Human readable form (might be a reference to the UI used to capture the contract).
     */
    @Child(name="friendly", type={Attachment.class}, order=18, min=0, max=1)
    @Description(shortDefinition="Human readable contract text", formalDefinition="Friendly Human readable form (might be a reference to the UI used to capture the contract)." )
    protected Attachment friendly;

    /**
     * Legal text in Human readable form.
     */
    @Child(name="legal", type={Attachment.class}, order=19, min=0, max=1)
    @Description(shortDefinition="Legal contract text", formalDefinition="Legal text in Human readable form." )
    protected Attachment legal;

    /**
     * Computable Policy rules (e.g. XACML, DKAL, SecPal).
     */
    @Child(name="rule", type={Attachment.class}, order=20, min=0, max=1)
    @Description(shortDefinition="Computable contract text", formalDefinition="Computable Policy rules (e.g. XACML, DKAL, SecPal)." )
    protected Attachment rule;

    private static final long serialVersionUID = -1421847454L;

    public Contract() {
      super();
    }

    /**
     * @return {@link #identifier} (Unique Id for this contract.)
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
     * @return {@link #identifier} (Unique Id for this contract.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #subject} (Who and/or what this is about: typically Patient, Organization, property.)
     */
    public List<Reference> getSubject() { 
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      return this.subject;
    }

    public boolean hasSubject() { 
      if (this.subject == null)
        return false;
      for (Reference item : this.subject)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #subject} (Who and/or what this is about: typically Patient, Organization, property.)
     */
    // syntactic sugar
    public Reference addSubject() { //3
      Reference t = new Reference();
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return t;
    }

    /**
     * @return {@link #subject} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Who and/or what this is about: typically Patient, Organization, property.)
     */
    public List<Resource> getSubjectTarget() { 
      if (this.subjectTarget == null)
        this.subjectTarget = new ArrayList<Resource>();
      return this.subjectTarget;
    }

    /**
     * @return {@link #type} (Type of contract (Privacy-Security, Agreement, Insurance).)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept();
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Type of contract (Privacy-Security, Agreement, Insurance).)
     */
    public Contract setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subtype} (More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat).)
     */
    public List<CodeableConcept> getSubtype() { 
      if (this.subtype == null)
        this.subtype = new ArrayList<CodeableConcept>();
      return this.subtype;
    }

    public boolean hasSubtype() { 
      if (this.subtype == null)
        return false;
      for (CodeableConcept item : this.subtype)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #subtype} (More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat).)
     */
    // syntactic sugar
    public CodeableConcept addSubtype() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.subtype == null)
        this.subtype = new ArrayList<CodeableConcept>();
      this.subtype.add(t);
      return t;
    }

    /**
     * @return {@link #issued} (When this was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public DateTimeType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new DateTimeType();
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (When this was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public Contract setIssuedElement(DateTimeType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return When this was issued.
     */
    public DateAndTime getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value When this was issued.
     */
    public Contract setIssued(DateAndTime value) { 
      if (value == null)
        this.issued = null;
      else {
        if (this.issued == null)
          this.issued = new DateTimeType();
        this.issued.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #applies} (Relevant time/time-period when applicable.)
     */
    public Period getApplies() { 
      if (this.applies == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.applies");
        else if (Configuration.doAutoCreate())
          this.applies = new Period();
      return this.applies;
    }

    public boolean hasApplies() { 
      return this.applies != null && !this.applies.isEmpty();
    }

    /**
     * @param value {@link #applies} (Relevant time/time-period when applicable.)
     */
    public Contract setApplies(Period value) { 
      this.applies = value;
      return this;
    }

    /**
     * @return {@link #quantity} (The number of repetitions of a service or product.)
     */
    public Quantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new Quantity();
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (The number of repetitions of a service or product.)
     */
    public Contract setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #unitPrice} (The unit price product.)
     */
    public Money getUnitPrice() { 
      if (this.unitPrice == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.unitPrice");
        else if (Configuration.doAutoCreate())
          this.unitPrice = new Money();
      return this.unitPrice;
    }

    public boolean hasUnitPrice() { 
      return this.unitPrice != null && !this.unitPrice.isEmpty();
    }

    /**
     * @param value {@link #unitPrice} (The unit price product.)
     */
    public Contract setUnitPrice(Money value) { 
      this.unitPrice = value;
      return this;
    }

    /**
     * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
     */
    public DecimalType getFactorElement() { 
      if (this.factor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.factor");
        else if (Configuration.doAutoCreate())
          this.factor = new DecimalType();
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
    public Contract setFactorElement(DecimalType value) { 
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
    public Contract setFactor(BigDecimal value) { 
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
     * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
     */
    public DecimalType getPointsElement() { 
      if (this.points == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.points");
        else if (Configuration.doAutoCreate())
          this.points = new DecimalType();
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
    public Contract setPointsElement(DecimalType value) { 
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
    public Contract setPoints(BigDecimal value) { 
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
     * @return {@link #net} (The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
     */
    public Money getNet() { 
      if (this.net == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.net");
        else if (Configuration.doAutoCreate())
          this.net = new Money();
      return this.net;
    }

    public boolean hasNet() { 
      return this.net != null && !this.net.isEmpty();
    }

    /**
     * @param value {@link #net} (The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
     */
    public Contract setNet(Money value) { 
      this.net = value;
      return this;
    }

    /**
     * @return {@link #author} (Contract author or responsible party.)
     */
    public List<Reference> getAuthor() { 
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      return this.author;
    }

    public boolean hasAuthor() { 
      if (this.author == null)
        return false;
      for (Reference item : this.author)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #author} (Contract author or responsible party.)
     */
    // syntactic sugar
    public Reference addAuthor() { //3
      Reference t = new Reference();
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return t;
    }

    /**
     * @return {@link #author} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Contract author or responsible party.)
     */
    public List<Resource> getAuthorTarget() { 
      if (this.authorTarget == null)
        this.authorTarget = new ArrayList<Resource>();
      return this.authorTarget;
    }

    /**
     * @return {@link #grantor} (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    public List<Reference> getGrantor() { 
      if (this.grantor == null)
        this.grantor = new ArrayList<Reference>();
      return this.grantor;
    }

    public boolean hasGrantor() { 
      if (this.grantor == null)
        return false;
      for (Reference item : this.grantor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #grantor} (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    // syntactic sugar
    public Reference addGrantor() { //3
      Reference t = new Reference();
      if (this.grantor == null)
        this.grantor = new ArrayList<Reference>();
      this.grantor.add(t);
      return t;
    }

    /**
     * @return {@link #grantor} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    public List<Resource> getGrantorTarget() { 
      if (this.grantorTarget == null)
        this.grantorTarget = new ArrayList<Resource>();
      return this.grantorTarget;
    }

    /**
     * @return {@link #grantee} (The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.)
     */
    public List<Reference> getGrantee() { 
      if (this.grantee == null)
        this.grantee = new ArrayList<Reference>();
      return this.grantee;
    }

    public boolean hasGrantee() { 
      if (this.grantee == null)
        return false;
      for (Reference item : this.grantee)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #grantee} (The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.)
     */
    // syntactic sugar
    public Reference addGrantee() { //3
      Reference t = new Reference();
      if (this.grantee == null)
        this.grantee = new ArrayList<Reference>();
      this.grantee.add(t);
      return t;
    }

    /**
     * @return {@link #grantee} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.)
     */
    public List<Resource> getGranteeTarget() { 
      if (this.granteeTarget == null)
        this.granteeTarget = new ArrayList<Resource>();
      return this.granteeTarget;
    }

    /**
     * @return {@link #witness} (Who witnesses the contract.)
     */
    public List<Reference> getWitness() { 
      if (this.witness == null)
        this.witness = new ArrayList<Reference>();
      return this.witness;
    }

    public boolean hasWitness() { 
      if (this.witness == null)
        return false;
      for (Reference item : this.witness)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #witness} (Who witnesses the contract.)
     */
    // syntactic sugar
    public Reference addWitness() { //3
      Reference t = new Reference();
      if (this.witness == null)
        this.witness = new ArrayList<Reference>();
      this.witness.add(t);
      return t;
    }

    /**
     * @return {@link #witness} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Who witnesses the contract.)
     */
    public List<Resource> getWitnessTarget() { 
      if (this.witnessTarget == null)
        this.witnessTarget = new ArrayList<Resource>();
      return this.witnessTarget;
    }

    /**
     * @return {@link #executor} (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    public List<Reference> getExecutor() { 
      if (this.executor == null)
        this.executor = new ArrayList<Reference>();
      return this.executor;
    }

    public boolean hasExecutor() { 
      if (this.executor == null)
        return false;
      for (Reference item : this.executor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #executor} (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    // syntactic sugar
    public Reference addExecutor() { //3
      Reference t = new Reference();
      if (this.executor == null)
        this.executor = new ArrayList<Reference>();
      this.executor.add(t);
      return t;
    }

    /**
     * @return {@link #executor} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    public List<Resource> getExecutorTarget() { 
      if (this.executorTarget == null)
        this.executorTarget = new ArrayList<Resource>();
      return this.executorTarget;
    }

    /**
     * @return {@link #notary} (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    public List<Reference> getNotary() { 
      if (this.notary == null)
        this.notary = new ArrayList<Reference>();
      return this.notary;
    }

    public boolean hasNotary() { 
      if (this.notary == null)
        return false;
      for (Reference item : this.notary)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #notary} (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    // syntactic sugar
    public Reference addNotary() { //3
      Reference t = new Reference();
      if (this.notary == null)
        this.notary = new ArrayList<Reference>();
      this.notary.add(t);
      return t;
    }

    /**
     * @return {@link #notary} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    public List<Resource> getNotaryTarget() { 
      if (this.notaryTarget == null)
        this.notaryTarget = new ArrayList<Resource>();
      return this.notaryTarget;
    }

    /**
     * @return {@link #signer} (List or contract signatures.)
     */
    public List<ContractSignerComponent> getSigner() { 
      if (this.signer == null)
        this.signer = new ArrayList<ContractSignerComponent>();
      return this.signer;
    }

    public boolean hasSigner() { 
      if (this.signer == null)
        return false;
      for (ContractSignerComponent item : this.signer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #signer} (List or contract signatures.)
     */
    // syntactic sugar
    public ContractSignerComponent addSigner() { //3
      ContractSignerComponent t = new ContractSignerComponent();
      if (this.signer == null)
        this.signer = new ArrayList<ContractSignerComponent>();
      this.signer.add(t);
      return t;
    }

    /**
     * @return {@link #term} (A contract provision.)
     */
    public List<ContractTermComponent> getTerm() { 
      if (this.term == null)
        this.term = new ArrayList<ContractTermComponent>();
      return this.term;
    }

    public boolean hasTerm() { 
      if (this.term == null)
        return false;
      for (ContractTermComponent item : this.term)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #term} (A contract provision.)
     */
    // syntactic sugar
    public ContractTermComponent addTerm() { //3
      ContractTermComponent t = new ContractTermComponent();
      if (this.term == null)
        this.term = new ArrayList<ContractTermComponent>();
      this.term.add(t);
      return t;
    }

    /**
     * @return {@link #friendly} (Friendly Human readable form (might be a reference to the UI used to capture the contract).)
     */
    public Attachment getFriendly() { 
      if (this.friendly == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.friendly");
        else if (Configuration.doAutoCreate())
          this.friendly = new Attachment();
      return this.friendly;
    }

    public boolean hasFriendly() { 
      return this.friendly != null && !this.friendly.isEmpty();
    }

    /**
     * @param value {@link #friendly} (Friendly Human readable form (might be a reference to the UI used to capture the contract).)
     */
    public Contract setFriendly(Attachment value) { 
      this.friendly = value;
      return this;
    }

    /**
     * @return {@link #legal} (Legal text in Human readable form.)
     */
    public Attachment getLegal() { 
      if (this.legal == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.legal");
        else if (Configuration.doAutoCreate())
          this.legal = new Attachment();
      return this.legal;
    }

    public boolean hasLegal() { 
      return this.legal != null && !this.legal.isEmpty();
    }

    /**
     * @param value {@link #legal} (Legal text in Human readable form.)
     */
    public Contract setLegal(Attachment value) { 
      this.legal = value;
      return this;
    }

    /**
     * @return {@link #rule} (Computable Policy rules (e.g. XACML, DKAL, SecPal).)
     */
    public Attachment getRule() { 
      if (this.rule == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.rule");
        else if (Configuration.doAutoCreate())
          this.rule = new Attachment();
      return this.rule;
    }

    public boolean hasRule() { 
      return this.rule != null && !this.rule.isEmpty();
    }

    /**
     * @param value {@link #rule} (Computable Policy rules (e.g. XACML, DKAL, SecPal).)
     */
    public Contract setRule(Attachment value) { 
      this.rule = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique Id for this contract.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Any)", "Who and/or what this is about: typically Patient, Organization, property.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("type", "CodeableConcept", "Type of contract (Privacy-Security, Agreement, Insurance).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subtype", "CodeableConcept", "More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat).", 0, java.lang.Integer.MAX_VALUE, subtype));
        childrenList.add(new Property("issued", "dateTime", "When this was issued.", 0, java.lang.Integer.MAX_VALUE, issued));
        childrenList.add(new Property("applies", "Period", "Relevant time/time-period when applicable.", 0, java.lang.Integer.MAX_VALUE, applies));
        childrenList.add(new Property("quantity", "Quantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("unitPrice", "Money", "The unit price product.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
        childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
        childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
        childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
        childrenList.add(new Property("author", "Reference(Practitioner|RelatedPerson|Organization)", "Contract author or responsible party.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("grantor", "Reference(Practitioner|RelatedPerson|Organization|Patient)", "First Party to the contract, may be the party who confers or delegates the rights defined in the contract.", 0, java.lang.Integer.MAX_VALUE, grantor));
        childrenList.add(new Property("grantee", "Reference(Practitioner|RelatedPerson|Organization|Patient)", "The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.", 0, java.lang.Integer.MAX_VALUE, grantee));
        childrenList.add(new Property("witness", "Reference(Practitioner|RelatedPerson|Patient)", "Who witnesses the contract.", 0, java.lang.Integer.MAX_VALUE, witness));
        childrenList.add(new Property("executor", "Reference(Practitioner|RelatedPerson|Organization|Patient)", "First Party to the contract, may be the party who confers or delegates the rights defined in the contract.", 0, java.lang.Integer.MAX_VALUE, executor));
        childrenList.add(new Property("notary", "Reference(Practitioner|RelatedPerson|Organization|Patient)", "First Party to the contract, may be the party who confers or delegates the rights defined in the contract.", 0, java.lang.Integer.MAX_VALUE, notary));
        childrenList.add(new Property("signer", "", "List or contract signatures.", 0, java.lang.Integer.MAX_VALUE, signer));
        childrenList.add(new Property("term", "", "A contract provision.", 0, java.lang.Integer.MAX_VALUE, term));
        childrenList.add(new Property("friendly", "Attachment", "Friendly Human readable form (might be a reference to the UI used to capture the contract).", 0, java.lang.Integer.MAX_VALUE, friendly));
        childrenList.add(new Property("legal", "Attachment", "Legal text in Human readable form.", 0, java.lang.Integer.MAX_VALUE, legal));
        childrenList.add(new Property("rule", "Attachment", "Computable Policy rules (e.g. XACML, DKAL, SecPal).", 0, java.lang.Integer.MAX_VALUE, rule));
      }

      public Contract copy() {
        Contract dst = new Contract();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (subject != null) {
          dst.subject = new ArrayList<Reference>();
          for (Reference i : subject)
            dst.subject.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        if (subtype != null) {
          dst.subtype = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subtype)
            dst.subtype.add(i.copy());
        };
        dst.issued = issued == null ? null : issued.copy();
        dst.applies = applies == null ? null : applies.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        if (author != null) {
          dst.author = new ArrayList<Reference>();
          for (Reference i : author)
            dst.author.add(i.copy());
        };
        if (grantor != null) {
          dst.grantor = new ArrayList<Reference>();
          for (Reference i : grantor)
            dst.grantor.add(i.copy());
        };
        if (grantee != null) {
          dst.grantee = new ArrayList<Reference>();
          for (Reference i : grantee)
            dst.grantee.add(i.copy());
        };
        if (witness != null) {
          dst.witness = new ArrayList<Reference>();
          for (Reference i : witness)
            dst.witness.add(i.copy());
        };
        if (executor != null) {
          dst.executor = new ArrayList<Reference>();
          for (Reference i : executor)
            dst.executor.add(i.copy());
        };
        if (notary != null) {
          dst.notary = new ArrayList<Reference>();
          for (Reference i : notary)
            dst.notary.add(i.copy());
        };
        if (signer != null) {
          dst.signer = new ArrayList<ContractSignerComponent>();
          for (ContractSignerComponent i : signer)
            dst.signer.add(i.copy());
        };
        if (term != null) {
          dst.term = new ArrayList<ContractTermComponent>();
          for (ContractTermComponent i : term)
            dst.term.add(i.copy());
        };
        dst.friendly = friendly == null ? null : friendly.copy();
        dst.legal = legal == null ? null : legal.copy();
        dst.rule = rule == null ? null : rule.copy();
        return dst;
      }

      protected Contract typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (subject == null || subject.isEmpty())
           && (type == null || type.isEmpty()) && (subtype == null || subtype.isEmpty()) && (issued == null || issued.isEmpty())
           && (applies == null || applies.isEmpty()) && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
           && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty()) && (net == null || net.isEmpty())
           && (author == null || author.isEmpty()) && (grantor == null || grantor.isEmpty()) && (grantee == null || grantee.isEmpty())
           && (witness == null || witness.isEmpty()) && (executor == null || executor.isEmpty()) && (notary == null || notary.isEmpty())
           && (signer == null || signer.isEmpty()) && (term == null || term.isEmpty()) && (friendly == null || friendly.isEmpty())
           && (legal == null || legal.isEmpty()) && (rule == null || rule.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Contract;
   }

  @SearchParamDefinition(name="patient", path="Contract.subject", description="The identity of the target of the contract (if a patient)", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="subject", path="Contract.subject", description="The identity of the target of the contract", type="reference" )
  public static final String SP_SUBJECT = "subject";

}

