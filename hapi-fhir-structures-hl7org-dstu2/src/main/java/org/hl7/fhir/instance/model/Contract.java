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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

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
        @Child(name="type", type={Coding.class}, order=1, min=1, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Signer Type", formalDefinition="Party or role who is signing." )
        protected List<Coding> type;

        /**
         * The DSIG signature contents in Base64.
         */
        @Child(name="signature", type={StringType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Documentation Signature", formalDefinition="The DSIG signature contents in Base64." )
        protected StringType signature;

        private static final long serialVersionUID = 584509693L;

      public ContractSignerComponent() {
        super();
      }

      public ContractSignerComponent(StringType signature) {
        super();
        this.signature = signature;
      }

        /**
         * @return {@link #type} (Party or role who is signing.)
         */
        public List<Coding> getType() { 
          if (this.type == null)
            this.type = new ArrayList<Coding>();
          return this.type;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (Coding item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #type} (Party or role who is signing.)
         */
    // syntactic sugar
        public Coding addType() { //3
          Coding t = new Coding();
          if (this.type == null)
            this.type = new ArrayList<Coding>();
          this.type.add(t);
          return t;
        }

        /**
         * @return {@link #signature} (The DSIG signature contents in Base64.). This is the underlying object with id, value and extensions. The accessor "getSignature" gives direct access to the value
         */
        public StringType getSignatureElement() { 
          if (this.signature == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractSignerComponent.signature");
            else if (Configuration.doAutoCreate())
              this.signature = new StringType(); // bb
          return this.signature;
        }

        public boolean hasSignatureElement() { 
          return this.signature != null && !this.signature.isEmpty();
        }

        public boolean hasSignature() { 
          return this.signature != null && !this.signature.isEmpty();
        }

        /**
         * @param value {@link #signature} (The DSIG signature contents in Base64.). This is the underlying object with id, value and extensions. The accessor "getSignature" gives direct access to the value
         */
        public ContractSignerComponent setSignatureElement(StringType value) { 
          this.signature = value;
          return this;
        }

        /**
         * @return The DSIG signature contents in Base64.
         */
        public String getSignature() { 
          return this.signature == null ? null : this.signature.getValue();
        }

        /**
         * @param value The DSIG signature contents in Base64.
         */
        public ContractSignerComponent setSignature(String value) { 
            if (this.signature == null)
              this.signature = new StringType();
            this.signature.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "Party or role who is signing.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("signature", "string", "The DSIG signature contents in Base64.", 0, java.lang.Integer.MAX_VALUE, signature));
        }

      public ContractSignerComponent copy() {
        ContractSignerComponent dst = new ContractSignerComponent();
        copyValues(dst);
        if (type != null) {
          dst.type = new ArrayList<Coding>();
          for (Coding i : type)
            dst.type.add(i.copy());
        };
        dst.signature = signature == null ? null : signature.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ContractSignerComponent))
          return false;
        ContractSignerComponent o = (ContractSignerComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(signature, o.signature, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ContractSignerComponent))
          return false;
        ContractSignerComponent o = (ContractSignerComponent) other;
        return compareValues(signature, o.signature, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (signature == null || signature.isEmpty())
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
         * The subtype of the term which is appropriate to the term type.
         */
        @Child(name="subtype", type={CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Term subtype", formalDefinition="The subtype of the term which is appropriate to the term type." )
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
        @Description(shortDefinition="Human readable Term text", formalDefinition="Human readable form of the term of the contract." )
        protected StringType text;

        /**
         * When this term was issued.
         */
        @Child(name="issued", type={DateTimeType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="When issued", formalDefinition="When this term was issued." )
        protected DateTimeType issued;

        /**
         * Relevant time/time-period when the term is applicable.
         */
        @Child(name="applies", type={Period.class}, order=7, min=0, max=1)
        @Description(shortDefinition="When effective", formalDefinition="Relevant time/time-period when the term is applicable." )
        protected Period applies;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name="quantity", type={Quantity.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * The unit price product.
         */
        @Child(name="unitPrice", type={Money.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="The unit price product." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name="factor", type={DecimalType.class}, order=10, min=0, max=1)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name="points", type={DecimalType.class}, order=11, min=0, max=1)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name="net", type={Money.class}, order=12, min=0, max=1)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        private static final long serialVersionUID = -1958595473L;

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
              this.identifier = new Identifier(); // cc
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
              this.type = new CodeableConcept(); // cc
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
         * @return {@link #subtype} (The subtype of the term which is appropriate to the term type.)
         */
        public CodeableConcept getSubtype() { 
          if (this.subtype == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.subtype");
            else if (Configuration.doAutoCreate())
              this.subtype = new CodeableConcept(); // cc
          return this.subtype;
        }

        public boolean hasSubtype() { 
          return this.subtype != null && !this.subtype.isEmpty();
        }

        /**
         * @param value {@link #subtype} (The subtype of the term which is appropriate to the term type.)
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
              this.subject = new Reference(); // cc
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

        /**
         * @return {@link #issued} (When this term was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
         */
        public DateTimeType getIssuedElement() { 
          if (this.issued == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.issued");
            else if (Configuration.doAutoCreate())
              this.issued = new DateTimeType(); // bb
          return this.issued;
        }

        public boolean hasIssuedElement() { 
          return this.issued != null && !this.issued.isEmpty();
        }

        public boolean hasIssued() { 
          return this.issued != null && !this.issued.isEmpty();
        }

        /**
         * @param value {@link #issued} (When this term was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
         */
        public ContractTermComponent setIssuedElement(DateTimeType value) { 
          this.issued = value;
          return this;
        }

        /**
         * @return When this term was issued.
         */
        public Date getIssued() { 
          return this.issued == null ? null : this.issued.getValue();
        }

        /**
         * @param value When this term was issued.
         */
        public ContractTermComponent setIssued(Date value) { 
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
         * @return {@link #applies} (Relevant time/time-period when the term is applicable.)
         */
        public Period getApplies() { 
          if (this.applies == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.applies");
            else if (Configuration.doAutoCreate())
              this.applies = new Period(); // cc
          return this.applies;
        }

        public boolean hasApplies() { 
          return this.applies != null && !this.applies.isEmpty();
        }

        /**
         * @param value {@link #applies} (Relevant time/time-period when the term is applicable.)
         */
        public ContractTermComponent setApplies(Period value) { 
          this.applies = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.quantity");
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
        public ContractTermComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (The unit price product.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (The unit price product.)
         */
        public ContractTermComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.factor");
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
        public ContractTermComponent setFactorElement(DecimalType value) { 
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
        public ContractTermComponent setFactor(BigDecimal value) { 
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
              throw new Error("Attempt to auto-create ContractTermComponent.points");
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
        public ContractTermComponent setPointsElement(DecimalType value) { 
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
        public ContractTermComponent setPoints(BigDecimal value) { 
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
         * @return {@link #net} (The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractTermComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public ContractTermComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Unique Id for this particular term.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("type", "CodeableConcept", "The type of the term.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("subtype", "CodeableConcept", "The subtype of the term which is appropriate to the term type.", 0, java.lang.Integer.MAX_VALUE, subtype));
          childrenList.add(new Property("subject", "Reference(Any)", "Who or what the contract term is about.", 0, java.lang.Integer.MAX_VALUE, subject));
          childrenList.add(new Property("text", "string", "Human readable form of the term of the contract.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("issued", "dateTime", "When this term was issued.", 0, java.lang.Integer.MAX_VALUE, issued));
          childrenList.add(new Property("applies", "Period", "Relevant time/time-period when the term is applicable.", 0, java.lang.Integer.MAX_VALUE, applies));
          childrenList.add(new Property("quantity", "Quantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "The unit price product.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
        }

      public ContractTermComponent copy() {
        ContractTermComponent dst = new ContractTermComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        dst.subtype = subtype == null ? null : subtype.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.text = text == null ? null : text.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.applies = applies == null ? null : applies.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ContractTermComponent))
          return false;
        ContractTermComponent o = (ContractTermComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(subtype, o.subtype, true)
           && compareDeep(subject, o.subject, true) && compareDeep(text, o.text, true) && compareDeep(issued, o.issued, true)
           && compareDeep(applies, o.applies, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(factor, o.factor, true) && compareDeep(points, o.points, true) && compareDeep(net, o.net, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ContractTermComponent))
          return false;
        ContractTermComponent o = (ContractTermComponent) other;
        return compareValues(text, o.text, true) && compareValues(issued, o.issued, true) && compareValues(factor, o.factor, true)
           && compareValues(points, o.points, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (subtype == null || subtype.isEmpty()) && (subject == null || subject.isEmpty()) && (text == null || text.isEmpty())
           && (issued == null || issued.isEmpty()) && (applies == null || applies.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (unitPrice == null || unitPrice.isEmpty()) && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty())
           && (net == null || net.isEmpty());
      }

  }

    /**
     * Unique Id for this contract.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contract identifier", formalDefinition="Unique Id for this contract." )
    protected List<Identifier> identifier;

    /**
     * Who and/or what this is about: typically Patient, Organization, property.
     */
    @Child(name = "subject", type = {}, order = 1, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Subject", formalDefinition="Who and/or what this is about: typically Patient, Organization, property." )
    protected List<Reference> subject;
    /**
     * The actual objects that are the target of the reference (Who and/or what this is about: typically Patient, Organization, property.)
     */
    protected List<Resource> subjectTarget;


    /**
     * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
     */
    @Child(name = "authority", type = {Organization.class}, order = 2, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Authority", formalDefinition="A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc." )
    protected List<Reference> authority;
    /**
     * The actual objects that are the target of the reference (A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.)
     */
    protected List<Organization> authorityTarget;


    /**
     * A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.
     */
    @Child(name = "domain", type = {Location.class}, order = 3, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Domain", formalDefinition="A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations." )
    protected List<Reference> domain;
    /**
     * The actual objects that are the target of the reference (A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.)
     */
    protected List<Location> domainTarget;


    /**
     * Type of contract (Privacy-Security, Agreement, Insurance).
     */
    @Child(name = "type", type = {CodeableConcept.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Type of contract", formalDefinition="Type of contract (Privacy-Security, Agreement, Insurance)." )
    protected CodeableConcept type;

    /**
     * More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat).
     */
    @Child(name = "subtype", type = {CodeableConcept.class}, order = 5, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Subtype of contract", formalDefinition="More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat)." )
    protected List<CodeableConcept> subtype;

    /**
     * When this was issued.
     */
    @Child(name = "issued", type = {DateTimeType.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="When this was issued", formalDefinition="When this was issued." )
    protected DateTimeType issued;

    /**
     * Relevant time/time-period when applicable.
     */
    @Child(name = "applies", type = {Period.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="Effective time", formalDefinition="Relevant time/time-period when applicable." )
    protected Period applies;

    /**
     * The number of repetitions of a service or product.
     */
    @Child(name = "quantity", type = {Quantity.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
    protected Quantity quantity;

    /**
     * The unit price product.
     */
    @Child(name = "unitPrice", type = {Money.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="The unit price product." )
    protected Money unitPrice;

    /**
     * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
     */
    @Child(name = "factor", type = {DecimalType.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
    protected DecimalType factor;

    /**
     * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
     */
    @Child(name = "points", type = {DecimalType.class}, order = 11, min = 0, max = 1)
    @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
    protected DecimalType points;

    /**
     * The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
     */
    @Child(name = "net", type = {Money.class}, order = 12, min = 0, max = 1)
    @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
    protected Money net;

    /**
     * Contract author or responsible party.
     */
    @Child(name = "author", type = {Practitioner.class, RelatedPerson.class, Organization.class}, order = 13, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contract author or responsible party", formalDefinition="Contract author or responsible party." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Contract author or responsible party.)
     */
    protected List<Resource> authorTarget;


    /**
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     */
    @Child(name = "grantor", type = {Practitioner.class, RelatedPerson.class, Organization.class, Patient.class}, order = 14, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="First Party or delegator", formalDefinition="First Party to the contract, may be the party who confers or delegates the rights defined in the contract." )
    protected List<Reference> grantor;
    /**
     * The actual objects that are the target of the reference (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    protected List<Resource> grantorTarget;


    /**
     * The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.
     */
    @Child(name = "grantee", type = {Practitioner.class, RelatedPerson.class, Organization.class, Patient.class}, order = 15, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Second Party or delegatee", formalDefinition="The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated." )
    protected List<Reference> grantee;
    /**
     * The actual objects that are the target of the reference (The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.)
     */
    protected List<Resource> granteeTarget;


    /**
     * Who witnesses the contract.
     */
    @Child(name = "witness", type = {Practitioner.class, RelatedPerson.class, Patient.class}, order = 16, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Witness to the contract", formalDefinition="Who witnesses the contract." )
    protected List<Reference> witness;
    /**
     * The actual objects that are the target of the reference (Who witnesses the contract.)
     */
    protected List<Resource> witnessTarget;


    /**
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     */
    @Child(name = "executor", type = {Practitioner.class, RelatedPerson.class, Organization.class, Patient.class}, order = 17, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Trustee", formalDefinition="First Party to the contract, may be the party who confers or delegates the rights defined in the contract." )
    protected List<Reference> executor;
    /**
     * The actual objects that are the target of the reference (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    protected List<Resource> executorTarget;


    /**
     * First Party to the contract, may be the party who confers or delegates the rights defined in the contract.
     */
    @Child(name = "notary", type = {Practitioner.class, RelatedPerson.class, Organization.class, Patient.class}, order = 18, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Notary Public", formalDefinition="First Party to the contract, may be the party who confers or delegates the rights defined in the contract." )
    protected List<Reference> notary;
    /**
     * The actual objects that are the target of the reference (First Party to the contract, may be the party who confers or delegates the rights defined in the contract.)
     */
    protected List<Resource> notaryTarget;


    /**
     * List or contract signatures.
     */
    @Child(name = "signer", type = {}, order = 19, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Signer", formalDefinition="List or contract signatures." )
    protected List<ContractSignerComponent> signer;

    /**
     * The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time.
     */
    @Child(name = "term", type = {}, order = 20, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="The terms of the Contract", formalDefinition="The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time." )
    protected List<ContractTermComponent> term;

    /**
     * Legally binding contract.
     */
    @Child(name = "binding", type = {AttachmentType.class}, order = 21, min = 0, max = 1)
    @Description(shortDefinition="Binding Contract", formalDefinition="Legally binding contract." )
    protected AttachmentType binding;

    /**
     * Relevant time/time-period when applicable.
     */
    @Child(name = "bindingDateTime", type = {DateTimeType.class}, order = 22, min = 0, max = 1)
    @Description(shortDefinition="Binding Contract effective time", formalDefinition="Relevant time/time-period when applicable." )
    protected DateTimeType bindingDateTime;

    /**
     * Friendly Human readable form (might be a reference to the UI used to capture the contract).
     */
    @Child(name = "friendly", type = {AttachmentType.class}, order = 23, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Human readable contract text", formalDefinition="Friendly Human readable form (might be a reference to the UI used to capture the contract)." )
    protected List<AttachmentType> friendly;

    /**
     * Relevant time/time-period when applicable.
     */
    @Child(name = "friendlyDateTime", type = {DateTimeType.class}, order = 24, min = 0, max = 1)
    @Description(shortDefinition="Human readable contract text effective time", formalDefinition="Relevant time/time-period when applicable." )
    protected DateTimeType friendlyDateTime;

    /**
     * Legal text in Human readable form.
     */
    @Child(name = "legal", type = {AttachmentType.class}, order = 25, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Legal contract text", formalDefinition="Legal text in Human readable form." )
    protected List<AttachmentType> legal;

    /**
     * Relevant time/time-period when applicable.
     */
    @Child(name = "legalDateTime", type = {DateTimeType.class}, order = 26, min = 0, max = 1)
    @Description(shortDefinition="Legal contract text date time", formalDefinition="Relevant time/time-period when applicable." )
    protected DateTimeType legalDateTime;

    /**
     * Computable Policy rules (e.g. XACML, DKAL, SecPal).
     */
    @Child(name = "rule", type = {AttachmentType.class}, order = 27, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Computable contract text", formalDefinition="Computable Policy rules (e.g. XACML, DKAL, SecPal)." )
    protected List<AttachmentType> rule;

    /**
     * Relevant time/time-period when applicable.
     */
    @Child(name = "ruleDateTime", type = {DateTimeType.class}, order = 28, min = 0, max = 1)
    @Description(shortDefinition="Computable contract text effect time", formalDefinition="Relevant time/time-period when applicable." )
    protected DateTimeType ruleDateTime;

    private static final long serialVersionUID = -467568093L;

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
     * @return {@link #authority} (A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.)
     */
    public List<Reference> getAuthority() { 
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      return this.authority;
    }

    public boolean hasAuthority() { 
      if (this.authority == null)
        return false;
      for (Reference item : this.authority)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #authority} (A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.)
     */
    // syntactic sugar
    public Reference addAuthority() { //3
      Reference t = new Reference();
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      this.authority.add(t);
      return t;
    }

    /**
     * @return {@link #authority} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.)
     */
    public List<Organization> getAuthorityTarget() { 
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      return this.authorityTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #authority} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.)
     */
    public Organization addAuthorityTarget() { 
      Organization r = new Organization();
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      this.authorityTarget.add(r);
      return r;
    }

    /**
     * @return {@link #domain} (A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.)
     */
    public List<Reference> getDomain() { 
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      return this.domain;
    }

    public boolean hasDomain() { 
      if (this.domain == null)
        return false;
      for (Reference item : this.domain)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #domain} (A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.)
     */
    // syntactic sugar
    public Reference addDomain() { //3
      Reference t = new Reference();
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      this.domain.add(t);
      return t;
    }

    /**
     * @return {@link #domain} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.)
     */
    public List<Location> getDomainTarget() { 
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      return this.domainTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #domain} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.)
     */
    public Location addDomainTarget() { 
      Location r = new Location();
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      this.domainTarget.add(r);
      return r;
    }

    /**
     * @return {@link #type} (Type of contract (Privacy-Security, Agreement, Insurance).)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
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
          this.issued = new DateTimeType(); // bb
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
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value When this was issued.
     */
    public Contract setIssued(Date value) { 
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
          this.applies = new Period(); // cc
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
          this.quantity = new Quantity(); // cc
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
          this.unitPrice = new Money(); // cc
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
     * @return {@link #net} (The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
     */
    public Money getNet() { 
      if (this.net == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.net");
        else if (Configuration.doAutoCreate())
          this.net = new Money(); // cc
      return this.net;
    }

    public boolean hasNet() { 
      return this.net != null && !this.net.isEmpty();
    }

    /**
     * @param value {@link #net} (The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
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
     * @return {@link #term} (The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time.)
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
     * @return {@link #term} (The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time.)
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
     * @return {@link #binding} (Legally binding contract.)
     */
    public AttachmentType getBinding() { 
      if (this.binding == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.binding");
        else if (Configuration.doAutoCreate())
          this.binding = new AttachmentType(); // cc
      return this.binding;
    }

    public boolean hasBinding() { 
      return this.binding != null && !this.binding.isEmpty();
    }

    /**
     * @param value {@link #binding} (Legally binding contract.)
     */
    public Contract setBinding(AttachmentType value) { 
      this.binding = value;
      return this;
    }

    /**
     * @return {@link #bindingDateTime} (Relevant time/time-period when applicable.). This is the underlying object with id, value and extensions. The accessor "getBindingDateTime" gives direct access to the value
     */
    public DateTimeType getBindingDateTimeElement() { 
      if (this.bindingDateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.bindingDateTime");
        else if (Configuration.doAutoCreate())
          this.bindingDateTime = new DateTimeType(); // bb
      return this.bindingDateTime;
    }

    public boolean hasBindingDateTimeElement() { 
      return this.bindingDateTime != null && !this.bindingDateTime.isEmpty();
    }

    public boolean hasBindingDateTime() { 
      return this.bindingDateTime != null && !this.bindingDateTime.isEmpty();
    }

    /**
     * @param value {@link #bindingDateTime} (Relevant time/time-period when applicable.). This is the underlying object with id, value and extensions. The accessor "getBindingDateTime" gives direct access to the value
     */
    public Contract setBindingDateTimeElement(DateTimeType value) { 
      this.bindingDateTime = value;
      return this;
    }

    /**
     * @return Relevant time/time-period when applicable.
     */
    public Date getBindingDateTime() { 
      return this.bindingDateTime == null ? null : this.bindingDateTime.getValue();
    }

    /**
     * @param value Relevant time/time-period when applicable.
     */
    public Contract setBindingDateTime(Date value) { 
      if (value == null)
        this.bindingDateTime = null;
      else {
        if (this.bindingDateTime == null)
          this.bindingDateTime = new DateTimeType();
        this.bindingDateTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #friendly} (Friendly Human readable form (might be a reference to the UI used to capture the contract).)
     */
    public List<AttachmentType> getFriendly() { 
      if (this.friendly == null)
        this.friendly = new ArrayList<AttachmentType>();
      return this.friendly;
    }

    public boolean hasFriendly() { 
      if (this.friendly == null)
        return false;
      for (AttachmentType item : this.friendly)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #friendly} (Friendly Human readable form (might be a reference to the UI used to capture the contract).)
     */
    // syntactic sugar
    public AttachmentType addFriendly() { //3
      AttachmentType t = new AttachmentType();
      if (this.friendly == null)
        this.friendly = new ArrayList<AttachmentType>();
      this.friendly.add(t);
      return t;
    }

    /**
     * @return {@link #friendlyDateTime} (Relevant time/time-period when applicable.). This is the underlying object with id, value and extensions. The accessor "getFriendlyDateTime" gives direct access to the value
     */
    public DateTimeType getFriendlyDateTimeElement() { 
      if (this.friendlyDateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.friendlyDateTime");
        else if (Configuration.doAutoCreate())
          this.friendlyDateTime = new DateTimeType(); // bb
      return this.friendlyDateTime;
    }

    public boolean hasFriendlyDateTimeElement() { 
      return this.friendlyDateTime != null && !this.friendlyDateTime.isEmpty();
    }

    public boolean hasFriendlyDateTime() { 
      return this.friendlyDateTime != null && !this.friendlyDateTime.isEmpty();
    }

    /**
     * @param value {@link #friendlyDateTime} (Relevant time/time-period when applicable.). This is the underlying object with id, value and extensions. The accessor "getFriendlyDateTime" gives direct access to the value
     */
    public Contract setFriendlyDateTimeElement(DateTimeType value) { 
      this.friendlyDateTime = value;
      return this;
    }

    /**
     * @return Relevant time/time-period when applicable.
     */
    public Date getFriendlyDateTime() { 
      return this.friendlyDateTime == null ? null : this.friendlyDateTime.getValue();
    }

    /**
     * @param value Relevant time/time-period when applicable.
     */
    public Contract setFriendlyDateTime(Date value) { 
      if (value == null)
        this.friendlyDateTime = null;
      else {
        if (this.friendlyDateTime == null)
          this.friendlyDateTime = new DateTimeType();
        this.friendlyDateTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #legal} (Legal text in Human readable form.)
     */
    public List<AttachmentType> getLegal() { 
      if (this.legal == null)
        this.legal = new ArrayList<AttachmentType>();
      return this.legal;
    }

    public boolean hasLegal() { 
      if (this.legal == null)
        return false;
      for (AttachmentType item : this.legal)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #legal} (Legal text in Human readable form.)
     */
    // syntactic sugar
    public AttachmentType addLegal() { //3
      AttachmentType t = new AttachmentType();
      if (this.legal == null)
        this.legal = new ArrayList<AttachmentType>();
      this.legal.add(t);
      return t;
    }

    /**
     * @return {@link #legalDateTime} (Relevant time/time-period when applicable.). This is the underlying object with id, value and extensions. The accessor "getLegalDateTime" gives direct access to the value
     */
    public DateTimeType getLegalDateTimeElement() { 
      if (this.legalDateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.legalDateTime");
        else if (Configuration.doAutoCreate())
          this.legalDateTime = new DateTimeType(); // bb
      return this.legalDateTime;
    }

    public boolean hasLegalDateTimeElement() { 
      return this.legalDateTime != null && !this.legalDateTime.isEmpty();
    }

    public boolean hasLegalDateTime() { 
      return this.legalDateTime != null && !this.legalDateTime.isEmpty();
    }

    /**
     * @param value {@link #legalDateTime} (Relevant time/time-period when applicable.). This is the underlying object with id, value and extensions. The accessor "getLegalDateTime" gives direct access to the value
     */
    public Contract setLegalDateTimeElement(DateTimeType value) { 
      this.legalDateTime = value;
      return this;
    }

    /**
     * @return Relevant time/time-period when applicable.
     */
    public Date getLegalDateTime() { 
      return this.legalDateTime == null ? null : this.legalDateTime.getValue();
    }

    /**
     * @param value Relevant time/time-period when applicable.
     */
    public Contract setLegalDateTime(Date value) { 
      if (value == null)
        this.legalDateTime = null;
      else {
        if (this.legalDateTime == null)
          this.legalDateTime = new DateTimeType();
        this.legalDateTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #rule} (Computable Policy rules (e.g. XACML, DKAL, SecPal).)
     */
    public List<AttachmentType> getRule() { 
      if (this.rule == null)
        this.rule = new ArrayList<AttachmentType>();
      return this.rule;
    }

    public boolean hasRule() { 
      if (this.rule == null)
        return false;
      for (AttachmentType item : this.rule)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #rule} (Computable Policy rules (e.g. XACML, DKAL, SecPal).)
     */
    // syntactic sugar
    public AttachmentType addRule() { //3
      AttachmentType t = new AttachmentType();
      if (this.rule == null)
        this.rule = new ArrayList<AttachmentType>();
      this.rule.add(t);
      return t;
    }

    /**
     * @return {@link #ruleDateTime} (Relevant time/time-period when applicable.). This is the underlying object with id, value and extensions. The accessor "getRuleDateTime" gives direct access to the value
     */
    public DateTimeType getRuleDateTimeElement() { 
      if (this.ruleDateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.ruleDateTime");
        else if (Configuration.doAutoCreate())
          this.ruleDateTime = new DateTimeType(); // bb
      return this.ruleDateTime;
    }

    public boolean hasRuleDateTimeElement() { 
      return this.ruleDateTime != null && !this.ruleDateTime.isEmpty();
    }

    public boolean hasRuleDateTime() { 
      return this.ruleDateTime != null && !this.ruleDateTime.isEmpty();
    }

    /**
     * @param value {@link #ruleDateTime} (Relevant time/time-period when applicable.). This is the underlying object with id, value and extensions. The accessor "getRuleDateTime" gives direct access to the value
     */
    public Contract setRuleDateTimeElement(DateTimeType value) { 
      this.ruleDateTime = value;
      return this;
    }

    /**
     * @return Relevant time/time-period when applicable.
     */
    public Date getRuleDateTime() { 
      return this.ruleDateTime == null ? null : this.ruleDateTime.getValue();
    }

    /**
     * @param value Relevant time/time-period when applicable.
     */
    public Contract setRuleDateTime(Date value) { 
      if (value == null)
        this.ruleDateTime = null;
      else {
        if (this.ruleDateTime == null)
          this.ruleDateTime = new DateTimeType();
        this.ruleDateTime.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique Id for this contract.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Any)", "Who and/or what this is about: typically Patient, Organization, property.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("authority", "Reference(Organization)", "A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.", 0, java.lang.Integer.MAX_VALUE, authority));
        childrenList.add(new Property("domain", "Reference(Location)", "A Location includes both incidental locations (a place which is used for healthcare without prior designation or authorization) and dedicated, formally appointed locations.", 0, java.lang.Integer.MAX_VALUE, domain));
        childrenList.add(new Property("type", "CodeableConcept", "Type of contract (Privacy-Security, Agreement, Insurance).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subtype", "CodeableConcept", "More specific type of contract (Privacy, Disclosure-Authorization, Advanced-Directive, DNR, Authorization-to-Treat).", 0, java.lang.Integer.MAX_VALUE, subtype));
        childrenList.add(new Property("issued", "dateTime", "When this was issued.", 0, java.lang.Integer.MAX_VALUE, issued));
        childrenList.add(new Property("applies", "Period", "Relevant time/time-period when applicable.", 0, java.lang.Integer.MAX_VALUE, applies));
        childrenList.add(new Property("quantity", "Quantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("unitPrice", "Money", "The unit price product.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
        childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
        childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
        childrenList.add(new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
        childrenList.add(new Property("author", "Reference(Practitioner|RelatedPerson|Organization)", "Contract author or responsible party.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("grantor", "Reference(Practitioner|RelatedPerson|Organization|Patient)", "First Party to the contract, may be the party who confers or delegates the rights defined in the contract.", 0, java.lang.Integer.MAX_VALUE, grantor));
        childrenList.add(new Property("grantee", "Reference(Practitioner|RelatedPerson|Organization|Patient)", "The Second party to the contract, may be the party who accepts obligations or be that to which rights are delegated.", 0, java.lang.Integer.MAX_VALUE, grantee));
        childrenList.add(new Property("witness", "Reference(Practitioner|RelatedPerson|Patient)", "Who witnesses the contract.", 0, java.lang.Integer.MAX_VALUE, witness));
        childrenList.add(new Property("executor", "Reference(Practitioner|RelatedPerson|Organization|Patient)", "First Party to the contract, may be the party who confers or delegates the rights defined in the contract.", 0, java.lang.Integer.MAX_VALUE, executor));
        childrenList.add(new Property("notary", "Reference(Practitioner|RelatedPerson|Organization|Patient)", "First Party to the contract, may be the party who confers or delegates the rights defined in the contract.", 0, java.lang.Integer.MAX_VALUE, notary));
        childrenList.add(new Property("signer", "", "List or contract signatures.", 0, java.lang.Integer.MAX_VALUE, signer));
        childrenList.add(new Property("term", "", "The itemized terms of the contract. The legal clause or conditions of the Contract that requires or prevents either one or both parties to perform a particular requirement by some specified time.", 0, java.lang.Integer.MAX_VALUE, term));
        childrenList.add(new Property("binding", "Attachment", "Legally binding contract.", 0, java.lang.Integer.MAX_VALUE, binding));
        childrenList.add(new Property("bindingDateTime", "dateTime", "Relevant time/time-period when applicable.", 0, java.lang.Integer.MAX_VALUE, bindingDateTime));
        childrenList.add(new Property("friendly", "Attachment", "Friendly Human readable form (might be a reference to the UI used to capture the contract).", 0, java.lang.Integer.MAX_VALUE, friendly));
        childrenList.add(new Property("friendlyDateTime", "dateTime", "Relevant time/time-period when applicable.", 0, java.lang.Integer.MAX_VALUE, friendlyDateTime));
        childrenList.add(new Property("legal", "Attachment", "Legal text in Human readable form.", 0, java.lang.Integer.MAX_VALUE, legal));
        childrenList.add(new Property("legalDateTime", "dateTime", "Relevant time/time-period when applicable.", 0, java.lang.Integer.MAX_VALUE, legalDateTime));
        childrenList.add(new Property("rule", "Attachment", "Computable Policy rules (e.g. XACML, DKAL, SecPal).", 0, java.lang.Integer.MAX_VALUE, rule));
        childrenList.add(new Property("ruleDateTime", "dateTime", "Relevant time/time-period when applicable.", 0, java.lang.Integer.MAX_VALUE, ruleDateTime));
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
        if (authority != null) {
          dst.authority = new ArrayList<Reference>();
          for (Reference i : authority)
            dst.authority.add(i.copy());
        };
        if (domain != null) {
          dst.domain = new ArrayList<Reference>();
          for (Reference i : domain)
            dst.domain.add(i.copy());
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
        dst.binding = binding == null ? null : binding.copy();
        dst.bindingDateTime = bindingDateTime == null ? null : bindingDateTime.copy();
        if (friendly != null) {
          dst.friendly = new ArrayList<AttachmentType>();
          for (AttachmentType i : friendly)
            dst.friendly.add(i.copy());
        };
        dst.friendlyDateTime = friendlyDateTime == null ? null : friendlyDateTime.copy();
        if (legal != null) {
          dst.legal = new ArrayList<AttachmentType>();
          for (AttachmentType i : legal)
            dst.legal.add(i.copy());
        };
        dst.legalDateTime = legalDateTime == null ? null : legalDateTime.copy();
        if (rule != null) {
          dst.rule = new ArrayList<AttachmentType>();
          for (AttachmentType i : rule)
            dst.rule.add(i.copy());
        };
        dst.ruleDateTime = ruleDateTime == null ? null : ruleDateTime.copy();
        return dst;
      }

      protected Contract typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Contract))
          return false;
        Contract o = (Contract) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(subject, o.subject, true) && compareDeep(authority, o.authority, true)
           && compareDeep(domain, o.domain, true) && compareDeep(type, o.type, true) && compareDeep(subtype, o.subtype, true)
           && compareDeep(issued, o.issued, true) && compareDeep(applies, o.applies, true) && compareDeep(quantity, o.quantity, true)
           && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true) && compareDeep(points, o.points, true)
           && compareDeep(net, o.net, true) && compareDeep(author, o.author, true) && compareDeep(grantor, o.grantor, true)
           && compareDeep(grantee, o.grantee, true) && compareDeep(witness, o.witness, true) && compareDeep(executor, o.executor, true)
           && compareDeep(notary, o.notary, true) && compareDeep(signer, o.signer, true) && compareDeep(term, o.term, true)
           && compareDeep(binding, o.binding, true) && compareDeep(bindingDateTime, o.bindingDateTime, true)
           && compareDeep(friendly, o.friendly, true) && compareDeep(friendlyDateTime, o.friendlyDateTime, true)
           && compareDeep(legal, o.legal, true) && compareDeep(legalDateTime, o.legalDateTime, true) && compareDeep(rule, o.rule, true)
           && compareDeep(ruleDateTime, o.ruleDateTime, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Contract))
          return false;
        Contract o = (Contract) other;
        return compareValues(issued, o.issued, true) && compareValues(factor, o.factor, true) && compareValues(points, o.points, true)
           && compareValues(bindingDateTime, o.bindingDateTime, true) && compareValues(friendlyDateTime, o.friendlyDateTime, true)
           && compareValues(legalDateTime, o.legalDateTime, true) && compareValues(ruleDateTime, o.ruleDateTime, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (subject == null || subject.isEmpty())
           && (authority == null || authority.isEmpty()) && (domain == null || domain.isEmpty()) && (type == null || type.isEmpty())
           && (subtype == null || subtype.isEmpty()) && (issued == null || issued.isEmpty()) && (applies == null || applies.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
           && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty()) && (net == null || net.isEmpty())
           && (author == null || author.isEmpty()) && (grantor == null || grantor.isEmpty()) && (grantee == null || grantee.isEmpty())
           && (witness == null || witness.isEmpty()) && (executor == null || executor.isEmpty()) && (notary == null || notary.isEmpty())
           && (signer == null || signer.isEmpty()) && (term == null || term.isEmpty()) && (binding == null || binding.isEmpty())
           && (bindingDateTime == null || bindingDateTime.isEmpty()) && (friendly == null || friendly.isEmpty())
           && (friendlyDateTime == null || friendlyDateTime.isEmpty()) && (legal == null || legal.isEmpty())
           && (legalDateTime == null || legalDateTime.isEmpty()) && (rule == null || rule.isEmpty())
           && (ruleDateTime == null || ruleDateTime.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Contract;
   }

  @SearchParamDefinition(name="subject", path="Contract.subject", description="The identity of the target of the contract", type="reference" )
  public static final String SP_SUBJECT = "subject";
    @SearchParamDefinition(name = "patient", path = "Contract.subject", description = "The identity of the target of the contract (if a patient)", type = "reference")
    public static final String SP_PATIENT = "patient";

}

