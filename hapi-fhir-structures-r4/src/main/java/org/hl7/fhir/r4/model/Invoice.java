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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

import java.util.*;

import java.math.*;
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
 * Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing purpose.
 */
@ResourceDef(name="Invoice", profile="http://hl7.org/fhir/Profile/Invoice")
public class Invoice extends DomainResource {

    public enum InvoiceStatus {
        /**
         * the invoice has been prepared but not yet finalized
         */
        DRAFT, 
        /**
         * the invoice has been finalized and sent to the recipient
         */
        ISSUED, 
        /**
         * the invoice has been balaced / completely paid
         */
        BALANCED, 
        /**
         * the invoice was cancelled
         */
        CANCELLED, 
        /**
         * the invoice was determined as entered in error before it was issued
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static InvoiceStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("issued".equals(codeString))
          return ISSUED;
        if ("balanced".equals(codeString))
          return BALANCED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown InvoiceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ISSUED: return "issued";
            case BALANCED: return "balanced";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/invoice-status";
            case ISSUED: return "http://hl7.org/fhir/invoice-status";
            case BALANCED: return "http://hl7.org/fhir/invoice-status";
            case CANCELLED: return "http://hl7.org/fhir/invoice-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/invoice-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "the invoice has been prepared but not yet finalized";
            case ISSUED: return "the invoice has been finalized and sent to the recipient";
            case BALANCED: return "the invoice has been balaced / completely paid";
            case CANCELLED: return "the invoice was cancelled";
            case ENTEREDINERROR: return "the invoice was determined as entered in error before it was issued";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "draft";
            case ISSUED: return "issued";
            case BALANCED: return "balanced";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered in error";
            default: return "?";
          }
        }
    }

  public static class InvoiceStatusEnumFactory implements EnumFactory<InvoiceStatus> {
    public InvoiceStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return InvoiceStatus.DRAFT;
        if ("issued".equals(codeString))
          return InvoiceStatus.ISSUED;
        if ("balanced".equals(codeString))
          return InvoiceStatus.BALANCED;
        if ("cancelled".equals(codeString))
          return InvoiceStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return InvoiceStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown InvoiceStatus code '"+codeString+"'");
        }
        public Enumeration<InvoiceStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<InvoiceStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<InvoiceStatus>(this, InvoiceStatus.DRAFT);
        if ("issued".equals(codeString))
          return new Enumeration<InvoiceStatus>(this, InvoiceStatus.ISSUED);
        if ("balanced".equals(codeString))
          return new Enumeration<InvoiceStatus>(this, InvoiceStatus.BALANCED);
        if ("cancelled".equals(codeString))
          return new Enumeration<InvoiceStatus>(this, InvoiceStatus.CANCELLED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<InvoiceStatus>(this, InvoiceStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown InvoiceStatus code '"+codeString+"'");
        }
    public String toCode(InvoiceStatus code) {
      if (code == InvoiceStatus.DRAFT)
        return "draft";
      if (code == InvoiceStatus.ISSUED)
        return "issued";
      if (code == InvoiceStatus.BALANCED)
        return "balanced";
      if (code == InvoiceStatus.CANCELLED)
        return "cancelled";
      if (code == InvoiceStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(InvoiceStatus code) {
      return code.getSystem();
      }
    }

    public enum InvoicePriceComponentType {
        /**
         * the amount is the base price used for calculating the total price before applying surcharges, discount or taxes
         */
        BASE, 
        /**
         * the amount is a surcharge applied on the base price
         */
        SURCHARGE, 
        /**
         * the amount is a deduction applied on the base price
         */
        DEDUCTION, 
        /**
         * the amount is a discount applied on the base price
         */
        DISCOUNT, 
        /**
         * the amount is the tax component of the total price
         */
        TAX, 
        /**
         * the amount is of informational character, it has not been applied in the calculation of the total price
         */
        INFORMATIONAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static InvoicePriceComponentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("base".equals(codeString))
          return BASE;
        if ("surcharge".equals(codeString))
          return SURCHARGE;
        if ("deduction".equals(codeString))
          return DEDUCTION;
        if ("discount".equals(codeString))
          return DISCOUNT;
        if ("tax".equals(codeString))
          return TAX;
        if ("informational".equals(codeString))
          return INFORMATIONAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown InvoicePriceComponentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BASE: return "base";
            case SURCHARGE: return "surcharge";
            case DEDUCTION: return "deduction";
            case DISCOUNT: return "discount";
            case TAX: return "tax";
            case INFORMATIONAL: return "informational";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case BASE: return "http://hl7.org/fhir/invoice-priceComponentType";
            case SURCHARGE: return "http://hl7.org/fhir/invoice-priceComponentType";
            case DEDUCTION: return "http://hl7.org/fhir/invoice-priceComponentType";
            case DISCOUNT: return "http://hl7.org/fhir/invoice-priceComponentType";
            case TAX: return "http://hl7.org/fhir/invoice-priceComponentType";
            case INFORMATIONAL: return "http://hl7.org/fhir/invoice-priceComponentType";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case BASE: return "the amount is the base price used for calculating the total price before applying surcharges, discount or taxes";
            case SURCHARGE: return "the amount is a surcharge applied on the base price";
            case DEDUCTION: return "the amount is a deduction applied on the base price";
            case DISCOUNT: return "the amount is a discount applied on the base price";
            case TAX: return "the amount is the tax component of the total price";
            case INFORMATIONAL: return "the amount is of informational character, it has not been applied in the calculation of the total price";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BASE: return "base price";
            case SURCHARGE: return "surcharge";
            case DEDUCTION: return "deduction";
            case DISCOUNT: return "discount";
            case TAX: return "tax";
            case INFORMATIONAL: return "informational";
            default: return "?";
          }
        }
    }

  public static class InvoicePriceComponentTypeEnumFactory implements EnumFactory<InvoicePriceComponentType> {
    public InvoicePriceComponentType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("base".equals(codeString))
          return InvoicePriceComponentType.BASE;
        if ("surcharge".equals(codeString))
          return InvoicePriceComponentType.SURCHARGE;
        if ("deduction".equals(codeString))
          return InvoicePriceComponentType.DEDUCTION;
        if ("discount".equals(codeString))
          return InvoicePriceComponentType.DISCOUNT;
        if ("tax".equals(codeString))
          return InvoicePriceComponentType.TAX;
        if ("informational".equals(codeString))
          return InvoicePriceComponentType.INFORMATIONAL;
        throw new IllegalArgumentException("Unknown InvoicePriceComponentType code '"+codeString+"'");
        }
        public Enumeration<InvoicePriceComponentType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<InvoicePriceComponentType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("base".equals(codeString))
          return new Enumeration<InvoicePriceComponentType>(this, InvoicePriceComponentType.BASE);
        if ("surcharge".equals(codeString))
          return new Enumeration<InvoicePriceComponentType>(this, InvoicePriceComponentType.SURCHARGE);
        if ("deduction".equals(codeString))
          return new Enumeration<InvoicePriceComponentType>(this, InvoicePriceComponentType.DEDUCTION);
        if ("discount".equals(codeString))
          return new Enumeration<InvoicePriceComponentType>(this, InvoicePriceComponentType.DISCOUNT);
        if ("tax".equals(codeString))
          return new Enumeration<InvoicePriceComponentType>(this, InvoicePriceComponentType.TAX);
        if ("informational".equals(codeString))
          return new Enumeration<InvoicePriceComponentType>(this, InvoicePriceComponentType.INFORMATIONAL);
        throw new FHIRException("Unknown InvoicePriceComponentType code '"+codeString+"'");
        }
    public String toCode(InvoicePriceComponentType code) {
      if (code == InvoicePriceComponentType.BASE)
        return "base";
      if (code == InvoicePriceComponentType.SURCHARGE)
        return "surcharge";
      if (code == InvoicePriceComponentType.DEDUCTION)
        return "deduction";
      if (code == InvoicePriceComponentType.DISCOUNT)
        return "discount";
      if (code == InvoicePriceComponentType.TAX)
        return "tax";
      if (code == InvoicePriceComponentType.INFORMATIONAL)
        return "informational";
      return "?";
      }
    public String toSystem(InvoicePriceComponentType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class InvoiceParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created automatically, the Participant may be a billing engine or another kind of device.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of involevent in creation of this Invoice", formalDefinition="Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created automatically, the Participant may be a billing engine or another kind of device." )
        protected CodeableConcept role;

        /**
         * The device, practitioner, etc. who performed or participated in the service.
         */
        @Child(name = "actor", type = {Practitioner.class, Organization.class, Patient.class, Device.class, RelatedPerson.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Individual who was involved", formalDefinition="The device, practitioner, etc. who performed or participated in the service." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (The device, practitioner, etc. who performed or participated in the service.)
         */
        protected Resource actorTarget;

        private static final long serialVersionUID = 805521719L;

    /**
     * Constructor
     */
      public InvoiceParticipantComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InvoiceParticipantComponent(Reference actor) {
        super();
        this.actor = actor;
      }

        /**
         * @return {@link #role} (Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created automatically, the Participant may be a billing engine or another kind of device.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InvoiceParticipantComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created automatically, the Participant may be a billing engine or another kind of device.)
         */
        public InvoiceParticipantComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #actor} (The device, practitioner, etc. who performed or participated in the service.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InvoiceParticipantComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (The device, practitioner, etc. who performed or participated in the service.)
         */
        public InvoiceParticipantComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who performed or participated in the service.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who performed or participated in the service.)
         */
        public InvoiceParticipantComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("role", "CodeableConcept", "Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created automatically, the Participant may be a billing engine or another kind of device.", 0, 1, role));
          children.add(new Property("actor", "Reference(Practitioner|Organization|Patient|Device|RelatedPerson)", "The device, practitioner, etc. who performed or participated in the service.", 0, 1, actor));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3506294: /*role*/  return new Property("role", "CodeableConcept", "Describes the type of involvement (e.g. transcriptionist, creator etc.). If the invoice has been created automatically, the Participant may be a billing engine or another kind of device.", 0, 1, role);
          case 92645877: /*actor*/  return new Property("actor", "Reference(Practitioner|Organization|Patient|Device|RelatedPerson)", "The device, practitioner, etc. who performed or participated in the service.", 0, 1, actor);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 92645877: // actor
          this.actor = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("actor")) {
          this.actor = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRole(); 
        case 92645877:  return getActor(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case 92645877: /*actor*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else
          return super.addChild(name);
      }

      public InvoiceParticipantComponent copy() {
        InvoiceParticipantComponent dst = new InvoiceParticipantComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.actor = actor == null ? null : actor.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof InvoiceParticipantComponent))
          return false;
        InvoiceParticipantComponent o = (InvoiceParticipantComponent) other_;
        return compareDeep(role, o.role, true) && compareDeep(actor, o.actor, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof InvoiceParticipantComponent))
          return false;
        InvoiceParticipantComponent o = (InvoiceParticipantComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(role, actor);
      }

  public String fhirType() {
    return "Invoice.participant";

  }

  }

    @Block()
    public static class InvoiceLineItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Sequence in which the items appear on the invoice.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Sequence number of line item", formalDefinition="Sequence in which the items appear on the invoice." )
        protected PositiveIntType sequence;

        /**
         * The ChargeItem contains information such as the billing code, date, amount etc.
         */
        @Child(name = "chargeItem", type = {ChargeItem.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference to ChargeItem containing details of this line item", formalDefinition="The ChargeItem contains information such as the billing code, date, amount etc." )
        protected Reference chargeItem;

        /**
         * The actual object that is the target of the reference (The ChargeItem contains information such as the billing code, date, amount etc.)
         */
        protected ChargeItem chargeItemTarget;

        /**
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated.
         */
        @Child(name = "priceComponent", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Components of total line item price", formalDefinition="The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated." )
        protected List<InvoiceLineItemPriceComponentComponent> priceComponent;

        private static final long serialVersionUID = -1719217937L;

    /**
     * Constructor
     */
      public InvoiceLineItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InvoiceLineItemComponent(Reference chargeItem) {
        super();
        this.chargeItem = chargeItem;
      }

        /**
         * @return {@link #sequence} (Sequence in which the items appear on the invoice.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InvoiceLineItemComponent.sequence");
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
         * @param value {@link #sequence} (Sequence in which the items appear on the invoice.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public InvoiceLineItemComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Sequence in which the items appear on the invoice.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value Sequence in which the items appear on the invoice.
         */
        public InvoiceLineItemComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #chargeItem} (The ChargeItem contains information such as the billing code, date, amount etc.)
         */
        public Reference getChargeItem() { 
          if (this.chargeItem == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InvoiceLineItemComponent.chargeItem");
            else if (Configuration.doAutoCreate())
              this.chargeItem = new Reference(); // cc
          return this.chargeItem;
        }

        public boolean hasChargeItem() { 
          return this.chargeItem != null && !this.chargeItem.isEmpty();
        }

        /**
         * @param value {@link #chargeItem} (The ChargeItem contains information such as the billing code, date, amount etc.)
         */
        public InvoiceLineItemComponent setChargeItem(Reference value) { 
          this.chargeItem = value;
          return this;
        }

        /**
         * @return {@link #chargeItem} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The ChargeItem contains information such as the billing code, date, amount etc.)
         */
        public ChargeItem getChargeItemTarget() { 
          if (this.chargeItemTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InvoiceLineItemComponent.chargeItem");
            else if (Configuration.doAutoCreate())
              this.chargeItemTarget = new ChargeItem(); // aa
          return this.chargeItemTarget;
        }

        /**
         * @param value {@link #chargeItem} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The ChargeItem contains information such as the billing code, date, amount etc.)
         */
        public InvoiceLineItemComponent setChargeItemTarget(ChargeItem value) { 
          this.chargeItemTarget = value;
          return this;
        }

        /**
         * @return {@link #priceComponent} (The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated.)
         */
        public List<InvoiceLineItemPriceComponentComponent> getPriceComponent() { 
          if (this.priceComponent == null)
            this.priceComponent = new ArrayList<InvoiceLineItemPriceComponentComponent>();
          return this.priceComponent;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public InvoiceLineItemComponent setPriceComponent(List<InvoiceLineItemPriceComponentComponent> thePriceComponent) { 
          this.priceComponent = thePriceComponent;
          return this;
        }

        public boolean hasPriceComponent() { 
          if (this.priceComponent == null)
            return false;
          for (InvoiceLineItemPriceComponentComponent item : this.priceComponent)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public InvoiceLineItemPriceComponentComponent addPriceComponent() { //3
          InvoiceLineItemPriceComponentComponent t = new InvoiceLineItemPriceComponentComponent();
          if (this.priceComponent == null)
            this.priceComponent = new ArrayList<InvoiceLineItemPriceComponentComponent>();
          this.priceComponent.add(t);
          return t;
        }

        public InvoiceLineItemComponent addPriceComponent(InvoiceLineItemPriceComponentComponent t) { //3
          if (t == null)
            return this;
          if (this.priceComponent == null)
            this.priceComponent = new ArrayList<InvoiceLineItemPriceComponentComponent>();
          this.priceComponent.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #priceComponent}, creating it if it does not already exist
         */
        public InvoiceLineItemPriceComponentComponent getPriceComponentFirstRep() { 
          if (getPriceComponent().isEmpty()) {
            addPriceComponent();
          }
          return getPriceComponent().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "Sequence in which the items appear on the invoice.", 0, 1, sequence));
          children.add(new Property("chargeItem", "Reference(ChargeItem)", "The ChargeItem contains information such as the billing code, date, amount etc.", 0, 1, chargeItem));
          children.add(new Property("priceComponent", "", "The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated.", 0, java.lang.Integer.MAX_VALUE, priceComponent));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "Sequence in which the items appear on the invoice.", 0, 1, sequence);
          case 1417779175: /*chargeItem*/  return new Property("chargeItem", "Reference(ChargeItem)", "The ChargeItem contains information such as the billing code, date, amount etc.", 0, 1, chargeItem);
          case 1219095988: /*priceComponent*/  return new Property("priceComponent", "", "The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated.", 0, java.lang.Integer.MAX_VALUE, priceComponent);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1417779175: /*chargeItem*/ return this.chargeItem == null ? new Base[0] : new Base[] {this.chargeItem}; // Reference
        case 1219095988: /*priceComponent*/ return this.priceComponent == null ? new Base[0] : this.priceComponent.toArray(new Base[this.priceComponent.size()]); // InvoiceLineItemPriceComponentComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 1417779175: // chargeItem
          this.chargeItem = castToReference(value); // Reference
          return value;
        case 1219095988: // priceComponent
          this.getPriceComponent().add((InvoiceLineItemPriceComponentComponent) value); // InvoiceLineItemPriceComponentComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("chargeItem")) {
          this.chargeItem = castToReference(value); // Reference
        } else if (name.equals("priceComponent")) {
          this.getPriceComponent().add((InvoiceLineItemPriceComponentComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 1417779175:  return getChargeItem(); 
        case 1219095988:  return addPriceComponent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 1417779175: /*chargeItem*/ return new String[] {"Reference"};
        case 1219095988: /*priceComponent*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Invoice.sequence");
        }
        else if (name.equals("chargeItem")) {
          this.chargeItem = new Reference();
          return this.chargeItem;
        }
        else if (name.equals("priceComponent")) {
          return addPriceComponent();
        }
        else
          return super.addChild(name);
      }

      public InvoiceLineItemComponent copy() {
        InvoiceLineItemComponent dst = new InvoiceLineItemComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.chargeItem = chargeItem == null ? null : chargeItem.copy();
        if (priceComponent != null) {
          dst.priceComponent = new ArrayList<InvoiceLineItemPriceComponentComponent>();
          for (InvoiceLineItemPriceComponentComponent i : priceComponent)
            dst.priceComponent.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof InvoiceLineItemComponent))
          return false;
        InvoiceLineItemComponent o = (InvoiceLineItemComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(chargeItem, o.chargeItem, true) && compareDeep(priceComponent, o.priceComponent, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof InvoiceLineItemComponent))
          return false;
        InvoiceLineItemComponent o = (InvoiceLineItemComponent) other_;
        return compareValues(sequence, o.sequence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, chargeItem, priceComponent
          );
      }

  public String fhirType() {
    return "Invoice.lineItem";

  }

  }

    @Block()
    public static class InvoiceLineItemPriceComponentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * This code identifies the type of the component.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="base | surcharge | deduction | discount | tax | informational", formalDefinition="This code identifies the type of the component." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/invoice-priceComponentType")
        protected Enumeration<InvoicePriceComponentType> type;

        /**
         * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code identifying the specific component", formalDefinition="A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc." )
        protected CodeableConcept code;

        /**
         * The amount calculated for this component.
         */
        @Child(name = "factor", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Monetary amount associated with this component", formalDefinition="The amount calculated for this component." )
        protected Money factor;

        /**
         * The factor that has been applied on the base price for calculating this component.
         */
        @Child(name = "amount", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Factor used for calculating this component", formalDefinition="The factor that has been applied on the base price for calculating this component." )
        protected DecimalType amount;

        private static final long serialVersionUID = -39471852L;

    /**
     * Constructor
     */
      public InvoiceLineItemPriceComponentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InvoiceLineItemPriceComponentComponent(Enumeration<InvoicePriceComponentType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (This code identifies the type of the component.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<InvoicePriceComponentType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InvoiceLineItemPriceComponentComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<InvoicePriceComponentType>(new InvoicePriceComponentTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (This code identifies the type of the component.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public InvoiceLineItemPriceComponentComponent setTypeElement(Enumeration<InvoicePriceComponentType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return This code identifies the type of the component.
         */
        public InvoicePriceComponentType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value This code identifies the type of the component.
         */
        public InvoiceLineItemPriceComponentComponent setType(InvoicePriceComponentType value) { 
            if (this.type == null)
              this.type = new Enumeration<InvoicePriceComponentType>(new InvoicePriceComponentTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InvoiceLineItemPriceComponentComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.)
         */
        public InvoiceLineItemPriceComponentComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #factor} (The amount calculated for this component.)
         */
        public Money getFactor() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InvoiceLineItemPriceComponentComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new Money(); // cc
          return this.factor;
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (The amount calculated for this component.)
         */
        public InvoiceLineItemPriceComponentComponent setFactor(Money value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return {@link #amount} (The factor that has been applied on the base price for calculating this component.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public DecimalType getAmountElement() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InvoiceLineItemPriceComponentComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new DecimalType(); // bb
          return this.amount;
        }

        public boolean hasAmountElement() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (The factor that has been applied on the base price for calculating this component.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public InvoiceLineItemPriceComponentComponent setAmountElement(DecimalType value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return The factor that has been applied on the base price for calculating this component.
         */
        public BigDecimal getAmount() { 
          return this.amount == null ? null : this.amount.getValue();
        }

        /**
         * @param value The factor that has been applied on the base price for calculating this component.
         */
        public InvoiceLineItemPriceComponentComponent setAmount(BigDecimal value) { 
          if (value == null)
            this.amount = null;
          else {
            if (this.amount == null)
              this.amount = new DecimalType();
            this.amount.setValue(value);
          }
          return this;
        }

        /**
         * @param value The factor that has been applied on the base price for calculating this component.
         */
        public InvoiceLineItemPriceComponentComponent setAmount(long value) { 
              this.amount = new DecimalType();
            this.amount.setValue(value);
          return this;
        }

        /**
         * @param value The factor that has been applied on the base price for calculating this component.
         */
        public InvoiceLineItemPriceComponentComponent setAmount(double value) { 
              this.amount = new DecimalType();
            this.amount.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "This code identifies the type of the component.", 0, 1, type));
          children.add(new Property("code", "CodeableConcept", "A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.", 0, 1, code));
          children.add(new Property("factor", "Money", "The amount calculated for this component.", 0, 1, factor));
          children.add(new Property("amount", "decimal", "The factor that has been applied on the base price for calculating this component.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "This code identifies the type of the component.", 0, 1, type);
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.", 0, 1, code);
          case -1282148017: /*factor*/  return new Property("factor", "Money", "The amount calculated for this component.", 0, 1, factor);
          case -1413853096: /*amount*/  return new Property("amount", "decimal", "The factor that has been applied on the base price for calculating this component.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<InvoicePriceComponentType>
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // Money
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // DecimalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new InvoicePriceComponentTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<InvoicePriceComponentType>
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1282148017: // factor
          this.factor = castToMoney(value); // Money
          return value;
        case -1413853096: // amount
          this.amount = castToDecimal(value); // DecimalType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new InvoicePriceComponentTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<InvoicePriceComponentType>
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("factor")) {
          this.factor = castToMoney(value); // Money
        } else if (name.equals("amount")) {
          this.amount = castToDecimal(value); // DecimalType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 3059181:  return getCode(); 
        case -1282148017:  return getFactor(); 
        case -1413853096:  return getAmountElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1282148017: /*factor*/ return new String[] {"Money"};
        case -1413853096: /*amount*/ return new String[] {"decimal"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Invoice.type");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("factor")) {
          this.factor = new Money();
          return this.factor;
        }
        else if (name.equals("amount")) {
          throw new FHIRException("Cannot call addChild on a primitive type Invoice.amount");
        }
        else
          return super.addChild(name);
      }

      public InvoiceLineItemPriceComponentComponent copy() {
        InvoiceLineItemPriceComponentComponent dst = new InvoiceLineItemPriceComponentComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.code = code == null ? null : code.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof InvoiceLineItemPriceComponentComponent))
          return false;
        InvoiceLineItemPriceComponentComponent o = (InvoiceLineItemPriceComponentComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(code, o.code, true) && compareDeep(factor, o.factor, true)
           && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof InvoiceLineItemPriceComponentComponent))
          return false;
        InvoiceLineItemPriceComponentComponent o = (InvoiceLineItemPriceComponentComponent) other_;
        return compareValues(type, o.type, true) && compareValues(amount, o.amount, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, code, factor, amount
          );
      }

  public String fhirType() {
    return "Invoice.lineItem.priceComponent";

  }

  }

    /**
     * Identifier of this Invoice, often used for reference in corresponcence about this invoice or for tracking of payments.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier for item", formalDefinition="Identifier of this Invoice, often used for reference in corresponcence about this invoice or for tracking of payments." )
    protected List<Identifier> identifier;

    /**
     * The current state of the Invoice.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | issued | balanced | cancelled | entered-in-error", formalDefinition="The current state of the Invoice." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/invoice-status")
    protected Enumeration<InvoiceStatus> status;

    /**
     * In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).
     */
    @Child(name = "cancelledReason", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reason for cancellation of this Invoice", formalDefinition="In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.)." )
    protected StringType cancelledReason;

    /**
     * Type of Invoice depending on domain, realm an usage (eg. internal/external, dental, preliminary).
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of Invoice", formalDefinition="Type of Invoice depending on domain, realm an usage (eg. internal/external, dental, preliminary)." )
    protected CodeableConcept type;

    /**
     * The individual or set of individuals receiving the goods and services billed in this invoice.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Recipient(s) of goods and services", formalDefinition="The individual or set of individuals receiving the goods and services billed in this invoice." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The individual or set of individuals receiving the goods and services billed in this invoice.)
     */
    protected Resource subjectTarget;

    /**
     * The individual or Organization responsible for balancing of this invoice.
     */
    @Child(name = "recipient", type = {Organization.class, Patient.class, RelatedPerson.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Recipient of this invoice", formalDefinition="The individual or Organization responsible for balancing of this invoice." )
    protected Reference recipient;

    /**
     * The actual object that is the target of the reference (The individual or Organization responsible for balancing of this invoice.)
     */
    protected Resource recipientTarget;

    /**
     * Date/time(s) of when this Invoice was posted.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Invoice date / posting date", formalDefinition="Date/time(s) of when this Invoice was posted." )
    protected DateTimeType date;

    /**
     * Indicates who or what performed or participated in the charged service.
     */
    @Child(name = "participant", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Participant in creation of this Invoice", formalDefinition="Indicates who or what performed or participated in the charged service." )
    protected List<InvoiceParticipantComponent> participant;

    /**
     * The organizationissuing the Invoice.
     */
    @Child(name = "issuer", type = {Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Issuing Organization of Invoice", formalDefinition="The organizationissuing the Invoice." )
    protected Reference issuer;

    /**
     * The actual object that is the target of the reference (The organizationissuing the Invoice.)
     */
    protected Organization issuerTarget;

    /**
     * Account which is supposed to be balanced with this Invoice.
     */
    @Child(name = "account", type = {Account.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Account that is being balanced", formalDefinition="Account which is supposed to be balanced with this Invoice." )
    protected Reference account;

    /**
     * The actual object that is the target of the reference (Account which is supposed to be balanced with this Invoice.)
     */
    protected Account accountTarget;

    /**
     * Each line item represents one charge for goods ond services rendered. Details such as date, code and amount are found in the referenced ChargeItem resource.
     */
    @Child(name = "lineItem", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Line items of this Invoice", formalDefinition="Each line item represents one charge for goods ond services rendered. Details such as date, code and amount are found in the referenced ChargeItem resource." )
    protected List<InvoiceLineItemComponent> lineItem;

    /**
     * The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply in certain conditions.  The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the total price was calculated.
     */
    @Child(name = "totalPriceComponent", type = {InvoiceLineItemPriceComponentComponent.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Components of Invoice total", formalDefinition="The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply in certain conditions.  The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the total price was calculated." )
    protected List<InvoiceLineItemPriceComponentComponent> totalPriceComponent;

    /**
     * Invoice total , taxes excluded.
     */
    @Child(name = "totalNet", type = {Money.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Net total of this Invoice", formalDefinition="Invoice total , taxes excluded." )
    protected Money totalNet;

    /**
     * Invoice total, tax included.
     */
    @Child(name = "totalGross", type = {Money.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Gross toal of this Invoice", formalDefinition="Invoice total, tax included." )
    protected Money totalGross;

    /**
     * Payment details such as banking details, period of payment, deductables, methods of payment.
     */
    @Child(name = "paymentTerms", type = {MarkdownType.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Payment details", formalDefinition="Payment details such as banking details, period of payment, deductables, methods of payment." )
    protected MarkdownType paymentTerms;

    /**
     * Comments made about the invoice by the issuer, subject or other participants.
     */
    @Child(name = "note", type = {Annotation.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the invoice", formalDefinition="Comments made about the invoice by the issuer, subject or other participants." )
    protected List<Annotation> note;

    private static final long serialVersionUID = -62357265L;

  /**
   * Constructor
   */
    public Invoice() {
      super();
    }

  /**
   * Constructor
   */
    public Invoice(Enumeration<InvoiceStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (Identifier of this Invoice, often used for reference in corresponcence about this invoice or for tracking of payments.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Invoice setIdentifier(List<Identifier> theIdentifier) { 
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

    public Invoice addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (The current state of the Invoice.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<InvoiceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<InvoiceStatus>(new InvoiceStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The current state of the Invoice.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Invoice setStatusElement(Enumeration<InvoiceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The current state of the Invoice.
     */
    public InvoiceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The current state of the Invoice.
     */
    public Invoice setStatus(InvoiceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<InvoiceStatus>(new InvoiceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #cancelledReason} (In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).). This is the underlying object with id, value and extensions. The accessor "getCancelledReason" gives direct access to the value
     */
    public StringType getCancelledReasonElement() { 
      if (this.cancelledReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.cancelledReason");
        else if (Configuration.doAutoCreate())
          this.cancelledReason = new StringType(); // bb
      return this.cancelledReason;
    }

    public boolean hasCancelledReasonElement() { 
      return this.cancelledReason != null && !this.cancelledReason.isEmpty();
    }

    public boolean hasCancelledReason() { 
      return this.cancelledReason != null && !this.cancelledReason.isEmpty();
    }

    /**
     * @param value {@link #cancelledReason} (In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).). This is the underlying object with id, value and extensions. The accessor "getCancelledReason" gives direct access to the value
     */
    public Invoice setCancelledReasonElement(StringType value) { 
      this.cancelledReason = value;
      return this;
    }

    /**
     * @return In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).
     */
    public String getCancelledReason() { 
      return this.cancelledReason == null ? null : this.cancelledReason.getValue();
    }

    /**
     * @param value In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).
     */
    public Invoice setCancelledReason(String value) { 
      if (Utilities.noString(value))
        this.cancelledReason = null;
      else {
        if (this.cancelledReason == null)
          this.cancelledReason = new StringType();
        this.cancelledReason.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (Type of Invoice depending on domain, realm an usage (eg. internal/external, dental, preliminary).)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Type of Invoice depending on domain, realm an usage (eg. internal/external, dental, preliminary).)
     */
    public Invoice setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subject} (The individual or set of individuals receiving the goods and services billed in this invoice.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The individual or set of individuals receiving the goods and services billed in this invoice.)
     */
    public Invoice setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual or set of individuals receiving the goods and services billed in this invoice.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual or set of individuals receiving the goods and services billed in this invoice.)
     */
    public Invoice setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #recipient} (The individual or Organization responsible for balancing of this invoice.)
     */
    public Reference getRecipient() { 
      if (this.recipient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.recipient");
        else if (Configuration.doAutoCreate())
          this.recipient = new Reference(); // cc
      return this.recipient;
    }

    public boolean hasRecipient() { 
      return this.recipient != null && !this.recipient.isEmpty();
    }

    /**
     * @param value {@link #recipient} (The individual or Organization responsible for balancing of this invoice.)
     */
    public Invoice setRecipient(Reference value) { 
      this.recipient = value;
      return this;
    }

    /**
     * @return {@link #recipient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual or Organization responsible for balancing of this invoice.)
     */
    public Resource getRecipientTarget() { 
      return this.recipientTarget;
    }

    /**
     * @param value {@link #recipient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual or Organization responsible for balancing of this invoice.)
     */
    public Invoice setRecipientTarget(Resource value) { 
      this.recipientTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (Date/time(s) of when this Invoice was posted.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.date");
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
     * @param value {@link #date} (Date/time(s) of when this Invoice was posted.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Invoice setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return Date/time(s) of when this Invoice was posted.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value Date/time(s) of when this Invoice was posted.
     */
    public Invoice setDate(Date value) { 
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
     * @return {@link #participant} (Indicates who or what performed or participated in the charged service.)
     */
    public List<InvoiceParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<InvoiceParticipantComponent>();
      return this.participant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Invoice setParticipant(List<InvoiceParticipantComponent> theParticipant) { 
      this.participant = theParticipant;
      return this;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (InvoiceParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public InvoiceParticipantComponent addParticipant() { //3
      InvoiceParticipantComponent t = new InvoiceParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<InvoiceParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    public Invoice addParticipant(InvoiceParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<InvoiceParticipantComponent>();
      this.participant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #participant}, creating it if it does not already exist
     */
    public InvoiceParticipantComponent getParticipantFirstRep() { 
      if (getParticipant().isEmpty()) {
        addParticipant();
      }
      return getParticipant().get(0);
    }

    /**
     * @return {@link #issuer} (The organizationissuing the Invoice.)
     */
    public Reference getIssuer() { 
      if (this.issuer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.issuer");
        else if (Configuration.doAutoCreate())
          this.issuer = new Reference(); // cc
      return this.issuer;
    }

    public boolean hasIssuer() { 
      return this.issuer != null && !this.issuer.isEmpty();
    }

    /**
     * @param value {@link #issuer} (The organizationissuing the Invoice.)
     */
    public Invoice setIssuer(Reference value) { 
      this.issuer = value;
      return this;
    }

    /**
     * @return {@link #issuer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organizationissuing the Invoice.)
     */
    public Organization getIssuerTarget() { 
      if (this.issuerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.issuer");
        else if (Configuration.doAutoCreate())
          this.issuerTarget = new Organization(); // aa
      return this.issuerTarget;
    }

    /**
     * @param value {@link #issuer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organizationissuing the Invoice.)
     */
    public Invoice setIssuerTarget(Organization value) { 
      this.issuerTarget = value;
      return this;
    }

    /**
     * @return {@link #account} (Account which is supposed to be balanced with this Invoice.)
     */
    public Reference getAccount() { 
      if (this.account == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.account");
        else if (Configuration.doAutoCreate())
          this.account = new Reference(); // cc
      return this.account;
    }

    public boolean hasAccount() { 
      return this.account != null && !this.account.isEmpty();
    }

    /**
     * @param value {@link #account} (Account which is supposed to be balanced with this Invoice.)
     */
    public Invoice setAccount(Reference value) { 
      this.account = value;
      return this;
    }

    /**
     * @return {@link #account} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Account which is supposed to be balanced with this Invoice.)
     */
    public Account getAccountTarget() { 
      if (this.accountTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.account");
        else if (Configuration.doAutoCreate())
          this.accountTarget = new Account(); // aa
      return this.accountTarget;
    }

    /**
     * @param value {@link #account} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Account which is supposed to be balanced with this Invoice.)
     */
    public Invoice setAccountTarget(Account value) { 
      this.accountTarget = value;
      return this;
    }

    /**
     * @return {@link #lineItem} (Each line item represents one charge for goods ond services rendered. Details such as date, code and amount are found in the referenced ChargeItem resource.)
     */
    public List<InvoiceLineItemComponent> getLineItem() { 
      if (this.lineItem == null)
        this.lineItem = new ArrayList<InvoiceLineItemComponent>();
      return this.lineItem;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Invoice setLineItem(List<InvoiceLineItemComponent> theLineItem) { 
      this.lineItem = theLineItem;
      return this;
    }

    public boolean hasLineItem() { 
      if (this.lineItem == null)
        return false;
      for (InvoiceLineItemComponent item : this.lineItem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public InvoiceLineItemComponent addLineItem() { //3
      InvoiceLineItemComponent t = new InvoiceLineItemComponent();
      if (this.lineItem == null)
        this.lineItem = new ArrayList<InvoiceLineItemComponent>();
      this.lineItem.add(t);
      return t;
    }

    public Invoice addLineItem(InvoiceLineItemComponent t) { //3
      if (t == null)
        return this;
      if (this.lineItem == null)
        this.lineItem = new ArrayList<InvoiceLineItemComponent>();
      this.lineItem.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #lineItem}, creating it if it does not already exist
     */
    public InvoiceLineItemComponent getLineItemFirstRep() { 
      if (getLineItem().isEmpty()) {
        addLineItem();
      }
      return getLineItem().get(0);
    }

    /**
     * @return {@link #totalPriceComponent} (The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply in certain conditions.  The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the total price was calculated.)
     */
    public List<InvoiceLineItemPriceComponentComponent> getTotalPriceComponent() { 
      if (this.totalPriceComponent == null)
        this.totalPriceComponent = new ArrayList<InvoiceLineItemPriceComponentComponent>();
      return this.totalPriceComponent;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Invoice setTotalPriceComponent(List<InvoiceLineItemPriceComponentComponent> theTotalPriceComponent) { 
      this.totalPriceComponent = theTotalPriceComponent;
      return this;
    }

    public boolean hasTotalPriceComponent() { 
      if (this.totalPriceComponent == null)
        return false;
      for (InvoiceLineItemPriceComponentComponent item : this.totalPriceComponent)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public InvoiceLineItemPriceComponentComponent addTotalPriceComponent() { //3
      InvoiceLineItemPriceComponentComponent t = new InvoiceLineItemPriceComponentComponent();
      if (this.totalPriceComponent == null)
        this.totalPriceComponent = new ArrayList<InvoiceLineItemPriceComponentComponent>();
      this.totalPriceComponent.add(t);
      return t;
    }

    public Invoice addTotalPriceComponent(InvoiceLineItemPriceComponentComponent t) { //3
      if (t == null)
        return this;
      if (this.totalPriceComponent == null)
        this.totalPriceComponent = new ArrayList<InvoiceLineItemPriceComponentComponent>();
      this.totalPriceComponent.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #totalPriceComponent}, creating it if it does not already exist
     */
    public InvoiceLineItemPriceComponentComponent getTotalPriceComponentFirstRep() { 
      if (getTotalPriceComponent().isEmpty()) {
        addTotalPriceComponent();
      }
      return getTotalPriceComponent().get(0);
    }

    /**
     * @return {@link #totalNet} (Invoice total , taxes excluded.)
     */
    public Money getTotalNet() { 
      if (this.totalNet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.totalNet");
        else if (Configuration.doAutoCreate())
          this.totalNet = new Money(); // cc
      return this.totalNet;
    }

    public boolean hasTotalNet() { 
      return this.totalNet != null && !this.totalNet.isEmpty();
    }

    /**
     * @param value {@link #totalNet} (Invoice total , taxes excluded.)
     */
    public Invoice setTotalNet(Money value) { 
      this.totalNet = value;
      return this;
    }

    /**
     * @return {@link #totalGross} (Invoice total, tax included.)
     */
    public Money getTotalGross() { 
      if (this.totalGross == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.totalGross");
        else if (Configuration.doAutoCreate())
          this.totalGross = new Money(); // cc
      return this.totalGross;
    }

    public boolean hasTotalGross() { 
      return this.totalGross != null && !this.totalGross.isEmpty();
    }

    /**
     * @param value {@link #totalGross} (Invoice total, tax included.)
     */
    public Invoice setTotalGross(Money value) { 
      this.totalGross = value;
      return this;
    }

    /**
     * @return {@link #paymentTerms} (Payment details such as banking details, period of payment, deductables, methods of payment.). This is the underlying object with id, value and extensions. The accessor "getPaymentTerms" gives direct access to the value
     */
    public MarkdownType getPaymentTermsElement() { 
      if (this.paymentTerms == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Invoice.paymentTerms");
        else if (Configuration.doAutoCreate())
          this.paymentTerms = new MarkdownType(); // bb
      return this.paymentTerms;
    }

    public boolean hasPaymentTermsElement() { 
      return this.paymentTerms != null && !this.paymentTerms.isEmpty();
    }

    public boolean hasPaymentTerms() { 
      return this.paymentTerms != null && !this.paymentTerms.isEmpty();
    }

    /**
     * @param value {@link #paymentTerms} (Payment details such as banking details, period of payment, deductables, methods of payment.). This is the underlying object with id, value and extensions. The accessor "getPaymentTerms" gives direct access to the value
     */
    public Invoice setPaymentTermsElement(MarkdownType value) { 
      this.paymentTerms = value;
      return this;
    }

    /**
     * @return Payment details such as banking details, period of payment, deductables, methods of payment.
     */
    public String getPaymentTerms() { 
      return this.paymentTerms == null ? null : this.paymentTerms.getValue();
    }

    /**
     * @param value Payment details such as banking details, period of payment, deductables, methods of payment.
     */
    public Invoice setPaymentTerms(String value) { 
      if (value == null)
        this.paymentTerms = null;
      else {
        if (this.paymentTerms == null)
          this.paymentTerms = new MarkdownType();
        this.paymentTerms.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #note} (Comments made about the invoice by the issuer, subject or other participants.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Invoice setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public Invoice addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Identifier of this Invoice, often used for reference in corresponcence about this invoice or for tracking of payments.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The current state of the Invoice.", 0, 1, status));
        children.add(new Property("cancelledReason", "string", "In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).", 0, 1, cancelledReason));
        children.add(new Property("type", "CodeableConcept", "Type of Invoice depending on domain, realm an usage (eg. internal/external, dental, preliminary).", 0, 1, type));
        children.add(new Property("subject", "Reference(Patient|Group)", "The individual or set of individuals receiving the goods and services billed in this invoice.", 0, 1, subject));
        children.add(new Property("recipient", "Reference(Organization|Patient|RelatedPerson)", "The individual or Organization responsible for balancing of this invoice.", 0, 1, recipient));
        children.add(new Property("date", "dateTime", "Date/time(s) of when this Invoice was posted.", 0, 1, date));
        children.add(new Property("participant", "", "Indicates who or what performed or participated in the charged service.", 0, java.lang.Integer.MAX_VALUE, participant));
        children.add(new Property("issuer", "Reference(Organization)", "The organizationissuing the Invoice.", 0, 1, issuer));
        children.add(new Property("account", "Reference(Account)", "Account which is supposed to be balanced with this Invoice.", 0, 1, account));
        children.add(new Property("lineItem", "", "Each line item represents one charge for goods ond services rendered. Details such as date, code and amount are found in the referenced ChargeItem resource.", 0, java.lang.Integer.MAX_VALUE, lineItem));
        children.add(new Property("totalPriceComponent", "@Invoice.lineItem.priceComponent", "The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply in certain conditions.  The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the total price was calculated.", 0, java.lang.Integer.MAX_VALUE, totalPriceComponent));
        children.add(new Property("totalNet", "Money", "Invoice total , taxes excluded.", 0, 1, totalNet));
        children.add(new Property("totalGross", "Money", "Invoice total, tax included.", 0, 1, totalGross));
        children.add(new Property("paymentTerms", "markdown", "Payment details such as banking details, period of payment, deductables, methods of payment.", 0, 1, paymentTerms));
        children.add(new Property("note", "Annotation", "Comments made about the invoice by the issuer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifier of this Invoice, often used for reference in corresponcence about this invoice or for tracking of payments.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The current state of the Invoice.", 0, 1, status);
        case 1550362357: /*cancelledReason*/  return new Property("cancelledReason", "string", "In case of Invoice cancellation a reason must be given (entered in error, superseded by corrected invoice etc.).", 0, 1, cancelledReason);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of Invoice depending on domain, realm an usage (eg. internal/external, dental, preliminary).", 0, 1, type);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Group)", "The individual or set of individuals receiving the goods and services billed in this invoice.", 0, 1, subject);
        case 820081177: /*recipient*/  return new Property("recipient", "Reference(Organization|Patient|RelatedPerson)", "The individual or Organization responsible for balancing of this invoice.", 0, 1, recipient);
        case 3076014: /*date*/  return new Property("date", "dateTime", "Date/time(s) of when this Invoice was posted.", 0, 1, date);
        case 767422259: /*participant*/  return new Property("participant", "", "Indicates who or what performed or participated in the charged service.", 0, java.lang.Integer.MAX_VALUE, participant);
        case -1179159879: /*issuer*/  return new Property("issuer", "Reference(Organization)", "The organizationissuing the Invoice.", 0, 1, issuer);
        case -1177318867: /*account*/  return new Property("account", "Reference(Account)", "Account which is supposed to be balanced with this Invoice.", 0, 1, account);
        case 1188332839: /*lineItem*/  return new Property("lineItem", "", "Each line item represents one charge for goods ond services rendered. Details such as date, code and amount are found in the referenced ChargeItem resource.", 0, java.lang.Integer.MAX_VALUE, lineItem);
        case 1731497496: /*totalPriceComponent*/  return new Property("totalPriceComponent", "@Invoice.lineItem.priceComponent", "The total amount for the Invoice may be calculated as the sum of the line items with surcharges/deductions that apply in certain conditions.  The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the total price was calculated.", 0, java.lang.Integer.MAX_VALUE, totalPriceComponent);
        case -849911879: /*totalNet*/  return new Property("totalNet", "Money", "Invoice total , taxes excluded.", 0, 1, totalNet);
        case -727607968: /*totalGross*/  return new Property("totalGross", "Money", "Invoice total, tax included.", 0, 1, totalGross);
        case -507544799: /*paymentTerms*/  return new Property("paymentTerms", "markdown", "Payment details such as banking details, period of payment, deductables, methods of payment.", 0, 1, paymentTerms);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Comments made about the invoice by the issuer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<InvoiceStatus>
        case 1550362357: /*cancelledReason*/ return this.cancelledReason == null ? new Base[0] : new Base[] {this.cancelledReason}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 820081177: /*recipient*/ return this.recipient == null ? new Base[0] : new Base[] {this.recipient}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // InvoiceParticipantComponent
        case -1179159879: /*issuer*/ return this.issuer == null ? new Base[0] : new Base[] {this.issuer}; // Reference
        case -1177318867: /*account*/ return this.account == null ? new Base[0] : new Base[] {this.account}; // Reference
        case 1188332839: /*lineItem*/ return this.lineItem == null ? new Base[0] : this.lineItem.toArray(new Base[this.lineItem.size()]); // InvoiceLineItemComponent
        case 1731497496: /*totalPriceComponent*/ return this.totalPriceComponent == null ? new Base[0] : this.totalPriceComponent.toArray(new Base[this.totalPriceComponent.size()]); // InvoiceLineItemPriceComponentComponent
        case -849911879: /*totalNet*/ return this.totalNet == null ? new Base[0] : new Base[] {this.totalNet}; // Money
        case -727607968: /*totalGross*/ return this.totalGross == null ? new Base[0] : new Base[] {this.totalGross}; // Money
        case -507544799: /*paymentTerms*/ return this.paymentTerms == null ? new Base[0] : new Base[] {this.paymentTerms}; // MarkdownType
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
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
          value = new InvoiceStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<InvoiceStatus>
          return value;
        case 1550362357: // cancelledReason
          this.cancelledReason = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 820081177: // recipient
          this.recipient = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 767422259: // participant
          this.getParticipant().add((InvoiceParticipantComponent) value); // InvoiceParticipantComponent
          return value;
        case -1179159879: // issuer
          this.issuer = castToReference(value); // Reference
          return value;
        case -1177318867: // account
          this.account = castToReference(value); // Reference
          return value;
        case 1188332839: // lineItem
          this.getLineItem().add((InvoiceLineItemComponent) value); // InvoiceLineItemComponent
          return value;
        case 1731497496: // totalPriceComponent
          this.getTotalPriceComponent().add((InvoiceLineItemPriceComponentComponent) value); // InvoiceLineItemPriceComponentComponent
          return value;
        case -849911879: // totalNet
          this.totalNet = castToMoney(value); // Money
          return value;
        case -727607968: // totalGross
          this.totalGross = castToMoney(value); // Money
          return value;
        case -507544799: // paymentTerms
          this.paymentTerms = castToMarkdown(value); // MarkdownType
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new InvoiceStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<InvoiceStatus>
        } else if (name.equals("cancelledReason")) {
          this.cancelledReason = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("recipient")) {
          this.recipient = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("participant")) {
          this.getParticipant().add((InvoiceParticipantComponent) value);
        } else if (name.equals("issuer")) {
          this.issuer = castToReference(value); // Reference
        } else if (name.equals("account")) {
          this.account = castToReference(value); // Reference
        } else if (name.equals("lineItem")) {
          this.getLineItem().add((InvoiceLineItemComponent) value);
        } else if (name.equals("totalPriceComponent")) {
          this.getTotalPriceComponent().add((InvoiceLineItemPriceComponentComponent) value);
        } else if (name.equals("totalNet")) {
          this.totalNet = castToMoney(value); // Money
        } else if (name.equals("totalGross")) {
          this.totalGross = castToMoney(value); // Money
        } else if (name.equals("paymentTerms")) {
          this.paymentTerms = castToMarkdown(value); // MarkdownType
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 1550362357:  return getCancelledReasonElement();
        case 3575610:  return getType(); 
        case -1867885268:  return getSubject(); 
        case 820081177:  return getRecipient(); 
        case 3076014:  return getDateElement();
        case 767422259:  return addParticipant(); 
        case -1179159879:  return getIssuer(); 
        case -1177318867:  return getAccount(); 
        case 1188332839:  return addLineItem(); 
        case 1731497496:  return addTotalPriceComponent(); 
        case -849911879:  return getTotalNet(); 
        case -727607968:  return getTotalGross(); 
        case -507544799:  return getPaymentTermsElement();
        case 3387378:  return addNote(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 1550362357: /*cancelledReason*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 820081177: /*recipient*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 767422259: /*participant*/ return new String[] {};
        case -1179159879: /*issuer*/ return new String[] {"Reference"};
        case -1177318867: /*account*/ return new String[] {"Reference"};
        case 1188332839: /*lineItem*/ return new String[] {};
        case 1731497496: /*totalPriceComponent*/ return new String[] {"@Invoice.lineItem.priceComponent"};
        case -849911879: /*totalNet*/ return new String[] {"Money"};
        case -727607968: /*totalGross*/ return new String[] {"Money"};
        case -507544799: /*paymentTerms*/ return new String[] {"markdown"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Invoice.status");
        }
        else if (name.equals("cancelledReason")) {
          throw new FHIRException("Cannot call addChild on a primitive type Invoice.cancelledReason");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("recipient")) {
          this.recipient = new Reference();
          return this.recipient;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Invoice.date");
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("issuer")) {
          this.issuer = new Reference();
          return this.issuer;
        }
        else if (name.equals("account")) {
          this.account = new Reference();
          return this.account;
        }
        else if (name.equals("lineItem")) {
          return addLineItem();
        }
        else if (name.equals("totalPriceComponent")) {
          return addTotalPriceComponent();
        }
        else if (name.equals("totalNet")) {
          this.totalNet = new Money();
          return this.totalNet;
        }
        else if (name.equals("totalGross")) {
          this.totalGross = new Money();
          return this.totalGross;
        }
        else if (name.equals("paymentTerms")) {
          throw new FHIRException("Cannot call addChild on a primitive type Invoice.paymentTerms");
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Invoice";

  }

      public Invoice copy() {
        Invoice dst = new Invoice();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.cancelledReason = cancelledReason == null ? null : cancelledReason.copy();
        dst.type = type == null ? null : type.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.recipient = recipient == null ? null : recipient.copy();
        dst.date = date == null ? null : date.copy();
        if (participant != null) {
          dst.participant = new ArrayList<InvoiceParticipantComponent>();
          for (InvoiceParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.issuer = issuer == null ? null : issuer.copy();
        dst.account = account == null ? null : account.copy();
        if (lineItem != null) {
          dst.lineItem = new ArrayList<InvoiceLineItemComponent>();
          for (InvoiceLineItemComponent i : lineItem)
            dst.lineItem.add(i.copy());
        };
        if (totalPriceComponent != null) {
          dst.totalPriceComponent = new ArrayList<InvoiceLineItemPriceComponentComponent>();
          for (InvoiceLineItemPriceComponentComponent i : totalPriceComponent)
            dst.totalPriceComponent.add(i.copy());
        };
        dst.totalNet = totalNet == null ? null : totalNet.copy();
        dst.totalGross = totalGross == null ? null : totalGross.copy();
        dst.paymentTerms = paymentTerms == null ? null : paymentTerms.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        return dst;
      }

      protected Invoice typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Invoice))
          return false;
        Invoice o = (Invoice) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(cancelledReason, o.cancelledReason, true)
           && compareDeep(type, o.type, true) && compareDeep(subject, o.subject, true) && compareDeep(recipient, o.recipient, true)
           && compareDeep(date, o.date, true) && compareDeep(participant, o.participant, true) && compareDeep(issuer, o.issuer, true)
           && compareDeep(account, o.account, true) && compareDeep(lineItem, o.lineItem, true) && compareDeep(totalPriceComponent, o.totalPriceComponent, true)
           && compareDeep(totalNet, o.totalNet, true) && compareDeep(totalGross, o.totalGross, true) && compareDeep(paymentTerms, o.paymentTerms, true)
           && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Invoice))
          return false;
        Invoice o = (Invoice) other_;
        return compareValues(status, o.status, true) && compareValues(cancelledReason, o.cancelledReason, true)
           && compareValues(date, o.date, true) && compareValues(paymentTerms, o.paymentTerms, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, cancelledReason
          , type, subject, recipient, date, participant, issuer, account, lineItem, totalPriceComponent
          , totalNet, totalGross, paymentTerms, note);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Invoice;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Invoice date / posting date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Invoice.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Invoice.date", description="Invoice date / posting date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Invoice date / posting date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Invoice.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for item</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Invoice.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Invoice.identifier", description="Business Identifier for item", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for item</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Invoice.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>totalgross</b>
   * <p>
   * Description: <b>Gross toal of this Invoice</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Invoice.totalGross</b><br>
   * </p>
   */
  @SearchParamDefinition(name="totalgross", path="Invoice.totalGross", description="Gross toal of this Invoice", type="quantity" )
  public static final String SP_TOTALGROSS = "totalgross";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>totalgross</b>
   * <p>
   * Description: <b>Gross toal of this Invoice</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Invoice.totalGross</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam TOTALGROSS = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_TOTALGROSS);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Recipient(s) of goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Invoice.subject", description="Recipient(s) of goods and services", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Recipient(s) of goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Invoice:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Invoice:subject").toLocked();

 /**
   * Search parameter: <b>participant-role</b>
   * <p>
   * Description: <b>Type of involevent in creation of this Invoice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Invoice.participant.role</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant-role", path="Invoice.participant.role", description="Type of involevent in creation of this Invoice", type="token" )
  public static final String SP_PARTICIPANT_ROLE = "participant-role";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant-role</b>
   * <p>
   * Description: <b>Type of involevent in creation of this Invoice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Invoice.participant.role</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PARTICIPANT_ROLE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PARTICIPANT_ROLE);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Type of Invoice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Invoice.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Invoice.type", description="Type of Invoice", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Type of Invoice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Invoice.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>issuer</b>
   * <p>
   * Description: <b>Issuing Organization of Invoice</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.issuer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="issuer", path="Invoice.issuer", description="Issuing Organization of Invoice", type="reference", target={Organization.class } )
  public static final String SP_ISSUER = "issuer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>issuer</b>
   * <p>
   * Description: <b>Issuing Organization of Invoice</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.issuer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ISSUER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ISSUER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Invoice:issuer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ISSUER = new ca.uhn.fhir.model.api.Include("Invoice:issuer").toLocked();

 /**
   * Search parameter: <b>participant</b>
   * <p>
   * Description: <b>Individual who was involved</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.participant.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant", path="Invoice.participant.actor", description="Individual who was involved", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PARTICIPANT = "participant";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant</b>
   * <p>
   * Description: <b>Individual who was involved</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.participant.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTICIPANT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTICIPANT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Invoice:participant</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTICIPANT = new ca.uhn.fhir.model.api.Include("Invoice:participant").toLocked();

 /**
   * Search parameter: <b>totalnet</b>
   * <p>
   * Description: <b>Net total of this Invoice</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Invoice.totalNet</b><br>
   * </p>
   */
  @SearchParamDefinition(name="totalnet", path="Invoice.totalNet", description="Net total of this Invoice", type="quantity" )
  public static final String SP_TOTALNET = "totalnet";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>totalnet</b>
   * <p>
   * Description: <b>Net total of this Invoice</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Invoice.totalNet</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam TOTALNET = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_TOTALNET);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Recipient(s) of goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Invoice.subject", description="Recipient(s) of goods and services", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Recipient(s) of goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Invoice:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Invoice:patient").toLocked();

 /**
   * Search parameter: <b>recipient</b>
   * <p>
   * Description: <b>Recipient of this invoice</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.recipient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recipient", path="Invoice.recipient", description="Recipient of this invoice", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Organization.class, Patient.class, RelatedPerson.class } )
  public static final String SP_RECIPIENT = "recipient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>recipient</b>
   * <p>
   * Description: <b>Recipient of this invoice</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.recipient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECIPIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECIPIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Invoice:recipient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECIPIENT = new ca.uhn.fhir.model.api.Include("Invoice:recipient").toLocked();

 /**
   * Search parameter: <b>account</b>
   * <p>
   * Description: <b>Account that is being balanced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.account</b><br>
   * </p>
   */
  @SearchParamDefinition(name="account", path="Invoice.account", description="Account that is being balanced", type="reference", target={Account.class } )
  public static final String SP_ACCOUNT = "account";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>account</b>
   * <p>
   * Description: <b>Account that is being balanced</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Invoice.account</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ACCOUNT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ACCOUNT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Invoice:account</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ACCOUNT = new ca.uhn.fhir.model.api.Include("Invoice:account").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | issued | balanced | cancelled | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Invoice.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Invoice.status", description="draft | issued | balanced | cancelled | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | issued | balanced | cancelled | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Invoice.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

