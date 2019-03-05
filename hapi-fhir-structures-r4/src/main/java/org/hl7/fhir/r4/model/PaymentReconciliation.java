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
 * This resource provides the details including amount of a payment and allocates the payment items being paid.
 */
@ResourceDef(name="PaymentReconciliation", profile="http://hl7.org/fhir/StructureDefinition/PaymentReconciliation")
public class PaymentReconciliation extends DomainResource {

    public enum PaymentReconciliationStatus {
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
        public static PaymentReconciliationStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown PaymentReconciliationStatus code '"+codeString+"'");
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

  public static class PaymentReconciliationStatusEnumFactory implements EnumFactory<PaymentReconciliationStatus> {
    public PaymentReconciliationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return PaymentReconciliationStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return PaymentReconciliationStatus.CANCELLED;
        if ("draft".equals(codeString))
          return PaymentReconciliationStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return PaymentReconciliationStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown PaymentReconciliationStatus code '"+codeString+"'");
        }
        public Enumeration<PaymentReconciliationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<PaymentReconciliationStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<PaymentReconciliationStatus>(this, PaymentReconciliationStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<PaymentReconciliationStatus>(this, PaymentReconciliationStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<PaymentReconciliationStatus>(this, PaymentReconciliationStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<PaymentReconciliationStatus>(this, PaymentReconciliationStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown PaymentReconciliationStatus code '"+codeString+"'");
        }
    public String toCode(PaymentReconciliationStatus code) {
      if (code == PaymentReconciliationStatus.ACTIVE)
        return "active";
      if (code == PaymentReconciliationStatus.CANCELLED)
        return "cancelled";
      if (code == PaymentReconciliationStatus.DRAFT)
        return "draft";
      if (code == PaymentReconciliationStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(PaymentReconciliationStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DetailsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Unique identifier for the current payment item for the referenced payable.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Business identifier of the payment detail", formalDefinition="Unique identifier for the current payment item for the referenced payable." )
        protected Identifier identifier;

        /**
         * Unique identifier for the prior payment item for the referenced payable.
         */
        @Child(name = "predecessor", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Business identifier of the prior payment detail", formalDefinition="Unique identifier for the prior payment item for the referenced payable." )
        protected Identifier predecessor;

        /**
         * Code to indicate the nature of the payment.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Category of payment", formalDefinition="Code to indicate the nature of the payment." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payment-type")
        protected CodeableConcept type;

        /**
         * A resource, such as a Claim, the evaluation of which could lead to payment.
         */
        @Child(name = "request", type = {Reference.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Request giving rise to the payment", formalDefinition="A resource, such as a Claim, the evaluation of which could lead to payment." )
        protected Reference request;

        /**
         * The actual object that is the target of the reference (A resource, such as a Claim, the evaluation of which could lead to payment.)
         */
        protected Resource requestTarget;

        /**
         * The party which submitted the claim or financial transaction.
         */
        @Child(name = "submitter", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Submitter of the request", formalDefinition="The party which submitted the claim or financial transaction." )
        protected Reference submitter;

        /**
         * The actual object that is the target of the reference (The party which submitted the claim or financial transaction.)
         */
        protected Resource submitterTarget;

        /**
         * A resource, such as a ClaimResponse, which contains a commitment to payment.
         */
        @Child(name = "response", type = {Reference.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Response committing to a payment", formalDefinition="A resource, such as a ClaimResponse, which contains a commitment to payment." )
        protected Reference response;

        /**
         * The actual object that is the target of the reference (A resource, such as a ClaimResponse, which contains a commitment to payment.)
         */
        protected Resource responseTarget;

        /**
         * The date from the response resource containing a commitment to pay.
         */
        @Child(name = "date", type = {DateType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Date of commitment to pay", formalDefinition="The date from the response resource containing a commitment to pay." )
        protected DateType date;

        /**
         * A reference to the individual who is responsible for inquiries regarding the response and its payment.
         */
        @Child(name = "responsible", type = {PractitionerRole.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contact for the response", formalDefinition="A reference to the individual who is responsible for inquiries regarding the response and its payment." )
        protected Reference responsible;

        /**
         * The actual object that is the target of the reference (A reference to the individual who is responsible for inquiries regarding the response and its payment.)
         */
        protected PractitionerRole responsibleTarget;

        /**
         * The party which is receiving the payment.
         */
        @Child(name = "payee", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Recipient of the payment", formalDefinition="The party which is receiving the payment." )
        protected Reference payee;

        /**
         * The actual object that is the target of the reference (The party which is receiving the payment.)
         */
        protected Resource payeeTarget;

        /**
         * The monetary amount allocated from the total payment to the payable.
         */
        @Child(name = "amount", type = {Money.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Amount allocated to this payable", formalDefinition="The monetary amount allocated from the total payment to the payable." )
        protected Money amount;

        private static final long serialVersionUID = -1361848619L;

    /**
     * Constructor
     */
      public DetailsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DetailsComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #identifier} (Unique identifier for the current payment item for the referenced payable.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Unique identifier for the current payment item for the referenced payable.)
         */
        public DetailsComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #predecessor} (Unique identifier for the prior payment item for the referenced payable.)
         */
        public Identifier getPredecessor() { 
          if (this.predecessor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.predecessor");
            else if (Configuration.doAutoCreate())
              this.predecessor = new Identifier(); // cc
          return this.predecessor;
        }

        public boolean hasPredecessor() { 
          return this.predecessor != null && !this.predecessor.isEmpty();
        }

        /**
         * @param value {@link #predecessor} (Unique identifier for the prior payment item for the referenced payable.)
         */
        public DetailsComponent setPredecessor(Identifier value) { 
          this.predecessor = value;
          return this;
        }

        /**
         * @return {@link #type} (Code to indicate the nature of the payment.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Code to indicate the nature of the payment.)
         */
        public DetailsComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #request} (A resource, such as a Claim, the evaluation of which could lead to payment.)
         */
        public Reference getRequest() { 
          if (this.request == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.request");
            else if (Configuration.doAutoCreate())
              this.request = new Reference(); // cc
          return this.request;
        }

        public boolean hasRequest() { 
          return this.request != null && !this.request.isEmpty();
        }

        /**
         * @param value {@link #request} (A resource, such as a Claim, the evaluation of which could lead to payment.)
         */
        public DetailsComponent setRequest(Reference value) { 
          this.request = value;
          return this;
        }

        /**
         * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A resource, such as a Claim, the evaluation of which could lead to payment.)
         */
        public Resource getRequestTarget() { 
          return this.requestTarget;
        }

        /**
         * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A resource, such as a Claim, the evaluation of which could lead to payment.)
         */
        public DetailsComponent setRequestTarget(Resource value) { 
          this.requestTarget = value;
          return this;
        }

        /**
         * @return {@link #submitter} (The party which submitted the claim or financial transaction.)
         */
        public Reference getSubmitter() { 
          if (this.submitter == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.submitter");
            else if (Configuration.doAutoCreate())
              this.submitter = new Reference(); // cc
          return this.submitter;
        }

        public boolean hasSubmitter() { 
          return this.submitter != null && !this.submitter.isEmpty();
        }

        /**
         * @param value {@link #submitter} (The party which submitted the claim or financial transaction.)
         */
        public DetailsComponent setSubmitter(Reference value) { 
          this.submitter = value;
          return this;
        }

        /**
         * @return {@link #submitter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party which submitted the claim or financial transaction.)
         */
        public Resource getSubmitterTarget() { 
          return this.submitterTarget;
        }

        /**
         * @param value {@link #submitter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party which submitted the claim or financial transaction.)
         */
        public DetailsComponent setSubmitterTarget(Resource value) { 
          this.submitterTarget = value;
          return this;
        }

        /**
         * @return {@link #response} (A resource, such as a ClaimResponse, which contains a commitment to payment.)
         */
        public Reference getResponse() { 
          if (this.response == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.response");
            else if (Configuration.doAutoCreate())
              this.response = new Reference(); // cc
          return this.response;
        }

        public boolean hasResponse() { 
          return this.response != null && !this.response.isEmpty();
        }

        /**
         * @param value {@link #response} (A resource, such as a ClaimResponse, which contains a commitment to payment.)
         */
        public DetailsComponent setResponse(Reference value) { 
          this.response = value;
          return this;
        }

        /**
         * @return {@link #response} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A resource, such as a ClaimResponse, which contains a commitment to payment.)
         */
        public Resource getResponseTarget() { 
          return this.responseTarget;
        }

        /**
         * @param value {@link #response} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A resource, such as a ClaimResponse, which contains a commitment to payment.)
         */
        public DetailsComponent setResponseTarget(Resource value) { 
          this.responseTarget = value;
          return this;
        }

        /**
         * @return {@link #date} (The date from the response resource containing a commitment to pay.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.date");
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
         * @param value {@link #date} (The date from the response resource containing a commitment to pay.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DetailsComponent setDateElement(DateType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return The date from the response resource containing a commitment to pay.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value The date from the response resource containing a commitment to pay.
         */
        public DetailsComponent setDate(Date value) { 
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
         * @return {@link #responsible} (A reference to the individual who is responsible for inquiries regarding the response and its payment.)
         */
        public Reference getResponsible() { 
          if (this.responsible == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.responsible");
            else if (Configuration.doAutoCreate())
              this.responsible = new Reference(); // cc
          return this.responsible;
        }

        public boolean hasResponsible() { 
          return this.responsible != null && !this.responsible.isEmpty();
        }

        /**
         * @param value {@link #responsible} (A reference to the individual who is responsible for inquiries regarding the response and its payment.)
         */
        public DetailsComponent setResponsible(Reference value) { 
          this.responsible = value;
          return this;
        }

        /**
         * @return {@link #responsible} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the individual who is responsible for inquiries regarding the response and its payment.)
         */
        public PractitionerRole getResponsibleTarget() { 
          if (this.responsibleTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.responsible");
            else if (Configuration.doAutoCreate())
              this.responsibleTarget = new PractitionerRole(); // aa
          return this.responsibleTarget;
        }

        /**
         * @param value {@link #responsible} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the individual who is responsible for inquiries regarding the response and its payment.)
         */
        public DetailsComponent setResponsibleTarget(PractitionerRole value) { 
          this.responsibleTarget = value;
          return this;
        }

        /**
         * @return {@link #payee} (The party which is receiving the payment.)
         */
        public Reference getPayee() { 
          if (this.payee == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.payee");
            else if (Configuration.doAutoCreate())
              this.payee = new Reference(); // cc
          return this.payee;
        }

        public boolean hasPayee() { 
          return this.payee != null && !this.payee.isEmpty();
        }

        /**
         * @param value {@link #payee} (The party which is receiving the payment.)
         */
        public DetailsComponent setPayee(Reference value) { 
          this.payee = value;
          return this;
        }

        /**
         * @return {@link #payee} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party which is receiving the payment.)
         */
        public Resource getPayeeTarget() { 
          return this.payeeTarget;
        }

        /**
         * @param value {@link #payee} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party which is receiving the payment.)
         */
        public DetailsComponent setPayeeTarget(Resource value) { 
          this.payeeTarget = value;
          return this;
        }

        /**
         * @return {@link #amount} (The monetary amount allocated from the total payment to the payable.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (The monetary amount allocated from the total payment to the payable.)
         */
        public DetailsComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("identifier", "Identifier", "Unique identifier for the current payment item for the referenced payable.", 0, 1, identifier));
          children.add(new Property("predecessor", "Identifier", "Unique identifier for the prior payment item for the referenced payable.", 0, 1, predecessor));
          children.add(new Property("type", "CodeableConcept", "Code to indicate the nature of the payment.", 0, 1, type));
          children.add(new Property("request", "Reference(Any)", "A resource, such as a Claim, the evaluation of which could lead to payment.", 0, 1, request));
          children.add(new Property("submitter", "Reference(Practitioner|PractitionerRole|Organization)", "The party which submitted the claim or financial transaction.", 0, 1, submitter));
          children.add(new Property("response", "Reference(Any)", "A resource, such as a ClaimResponse, which contains a commitment to payment.", 0, 1, response));
          children.add(new Property("date", "date", "The date from the response resource containing a commitment to pay.", 0, 1, date));
          children.add(new Property("responsible", "Reference(PractitionerRole)", "A reference to the individual who is responsible for inquiries regarding the response and its payment.", 0, 1, responsible));
          children.add(new Property("payee", "Reference(Practitioner|PractitionerRole|Organization)", "The party which is receiving the payment.", 0, 1, payee));
          children.add(new Property("amount", "Money", "The monetary amount allocated from the total payment to the payable.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier for the current payment item for the referenced payable.", 0, 1, identifier);
          case -1925032183: /*predecessor*/  return new Property("predecessor", "Identifier", "Unique identifier for the prior payment item for the referenced payable.", 0, 1, predecessor);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Code to indicate the nature of the payment.", 0, 1, type);
          case 1095692943: /*request*/  return new Property("request", "Reference(Any)", "A resource, such as a Claim, the evaluation of which could lead to payment.", 0, 1, request);
          case 348678409: /*submitter*/  return new Property("submitter", "Reference(Practitioner|PractitionerRole|Organization)", "The party which submitted the claim or financial transaction.", 0, 1, submitter);
          case -340323263: /*response*/  return new Property("response", "Reference(Any)", "A resource, such as a ClaimResponse, which contains a commitment to payment.", 0, 1, response);
          case 3076014: /*date*/  return new Property("date", "date", "The date from the response resource containing a commitment to pay.", 0, 1, date);
          case 1847674614: /*responsible*/  return new Property("responsible", "Reference(PractitionerRole)", "A reference to the individual who is responsible for inquiries regarding the response and its payment.", 0, 1, responsible);
          case 106443592: /*payee*/  return new Property("payee", "Reference(Practitioner|PractitionerRole|Organization)", "The party which is receiving the payment.", 0, 1, payee);
          case -1413853096: /*amount*/  return new Property("amount", "Money", "The monetary amount allocated from the total payment to the payable.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -1925032183: /*predecessor*/ return this.predecessor == null ? new Base[0] : new Base[] {this.predecessor}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case 348678409: /*submitter*/ return this.submitter == null ? new Base[0] : new Base[] {this.submitter}; // Reference
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateType
        case 1847674614: /*responsible*/ return this.responsible == null ? new Base[0] : new Base[] {this.responsible}; // Reference
        case 106443592: /*payee*/ return this.payee == null ? new Base[0] : new Base[] {this.payee}; // Reference
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -1925032183: // predecessor
          this.predecessor = castToIdentifier(value); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case 348678409: // submitter
          this.submitter = castToReference(value); // Reference
          return value;
        case -340323263: // response
          this.response = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDate(value); // DateType
          return value;
        case 1847674614: // responsible
          this.responsible = castToReference(value); // Reference
          return value;
        case 106443592: // payee
          this.payee = castToReference(value); // Reference
          return value;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("predecessor")) {
          this.predecessor = castToIdentifier(value); // Identifier
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("submitter")) {
          this.submitter = castToReference(value); // Reference
        } else if (name.equals("response")) {
          this.response = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDate(value); // DateType
        } else if (name.equals("responsible")) {
          this.responsible = castToReference(value); // Reference
        } else if (name.equals("payee")) {
          this.payee = castToReference(value); // Reference
        } else if (name.equals("amount")) {
          this.amount = castToMoney(value); // Money
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -1925032183:  return getPredecessor(); 
        case 3575610:  return getType(); 
        case 1095692943:  return getRequest(); 
        case 348678409:  return getSubmitter(); 
        case -340323263:  return getResponse(); 
        case 3076014:  return getDateElement();
        case 1847674614:  return getResponsible(); 
        case 106443592:  return getPayee(); 
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1925032183: /*predecessor*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case 348678409: /*submitter*/ return new String[] {"Reference"};
        case -340323263: /*response*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"date"};
        case 1847674614: /*responsible*/ return new String[] {"Reference"};
        case 106443592: /*payee*/ return new String[] {"Reference"};
        case -1413853096: /*amount*/ return new String[] {"Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("predecessor")) {
          this.predecessor = new Identifier();
          return this.predecessor;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("submitter")) {
          this.submitter = new Reference();
          return this.submitter;
        }
        else if (name.equals("response")) {
          this.response = new Reference();
          return this.response;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.date");
        }
        else if (name.equals("responsible")) {
          this.responsible = new Reference();
          return this.responsible;
        }
        else if (name.equals("payee")) {
          this.payee = new Reference();
          return this.payee;
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public DetailsComponent copy() {
        DetailsComponent dst = new DetailsComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.predecessor = predecessor == null ? null : predecessor.copy();
        dst.type = type == null ? null : type.copy();
        dst.request = request == null ? null : request.copy();
        dst.submitter = submitter == null ? null : submitter.copy();
        dst.response = response == null ? null : response.copy();
        dst.date = date == null ? null : date.copy();
        dst.responsible = responsible == null ? null : responsible.copy();
        dst.payee = payee == null ? null : payee.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DetailsComponent))
          return false;
        DetailsComponent o = (DetailsComponent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(predecessor, o.predecessor, true)
           && compareDeep(type, o.type, true) && compareDeep(request, o.request, true) && compareDeep(submitter, o.submitter, true)
           && compareDeep(response, o.response, true) && compareDeep(date, o.date, true) && compareDeep(responsible, o.responsible, true)
           && compareDeep(payee, o.payee, true) && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DetailsComponent))
          return false;
        DetailsComponent o = (DetailsComponent) other_;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, predecessor, type
          , request, submitter, response, date, responsible, payee, amount);
      }

  public String fhirType() {
    return "PaymentReconciliation.detail";

  }

  }

    @Block()
    public static class NotesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The business purpose of the note text.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The business purpose of the note text." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/note-type")
        protected Enumeration<NoteType> type;

        /**
         * The explanation or description associated with the processing.
         */
        @Child(name = "text", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Note explanatory text", formalDefinition="The explanation or description associated with the processing." )
        protected StringType text;

        private static final long serialVersionUID = 529250161L;

    /**
     * Constructor
     */
      public NotesComponent() {
        super();
      }

        /**
         * @return {@link #type} (The business purpose of the note text.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<NoteType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NotesComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<NoteType>(new NoteTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The business purpose of the note text.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public NotesComponent setTypeElement(Enumeration<NoteType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The business purpose of the note text.
         */
        public NoteType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The business purpose of the note text.
         */
        public NotesComponent setType(NoteType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<NoteType>(new NoteTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #text} (The explanation or description associated with the processing.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
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
         * @param value {@link #text} (The explanation or description associated with the processing.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public NotesComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return The explanation or description associated with the processing.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value The explanation or description associated with the processing.
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "The business purpose of the note text.", 0, 1, type));
          children.add(new Property("text", "string", "The explanation or description associated with the processing.", 0, 1, text));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "The business purpose of the note text.", 0, 1, type);
          case 3556653: /*text*/  return new Property("text", "string", "The explanation or description associated with the processing.", 0, 1, text);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<NoteType>
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new NoteTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<NoteType>
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new NoteTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<NoteType>
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 3556653:  return getTextElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 3556653: /*text*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.type");
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.text");
        }
        else
          return super.addChild(name);
      }

      public NotesComponent copy() {
        NotesComponent dst = new NotesComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof NotesComponent))
          return false;
        NotesComponent o = (NotesComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(text, o.text, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof NotesComponent))
          return false;
        NotesComponent o = (NotesComponent) other_;
        return compareValues(type, o.type, true) && compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, text);
      }

  public String fhirType() {
    return "PaymentReconciliation.processNote";

  }

  }

    /**
     * A unique identifier assigned to this payment reconciliation.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier for a payment reconciliation", formalDefinition="A unique identifier assigned to this payment reconciliation." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<PaymentReconciliationStatus> status;

    /**
     * The period of time for which payments have been gathered into this bulk payment for settlement.
     */
    @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period covered", formalDefinition="The period of time for which payments have been gathered into this bulk payment for settlement." )
    protected Period period;

    /**
     * The date when the resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the resource was created." )
    protected DateTimeType created;

    /**
     * The party who generated the payment.
     */
    @Child(name = "paymentIssuer", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Party generating payment", formalDefinition="The party who generated the payment." )
    protected Reference paymentIssuer;

    /**
     * The actual object that is the target of the reference (The party who generated the payment.)
     */
    protected Organization paymentIssuerTarget;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {Task.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reference to requesting resource", formalDefinition="Original request resource reference." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Original request resource reference.)
     */
    protected Task requestTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestor", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference requestor;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Resource requestorTarget;

    /**
     * The outcome of a request for a reconciliation.
     */
    @Child(name = "outcome", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="queued | complete | error | partial", formalDefinition="The outcome of a request for a reconciliation." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/remittance-outcome")
    protected Enumeration<RemittanceOutcome> outcome;

    /**
     * A human readable description of the status of the request for the reconciliation.
     */
    @Child(name = "disposition", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disposition message", formalDefinition="A human readable description of the status of the request for the reconciliation." )
    protected StringType disposition;

    /**
     * The date of payment as indicated on the financial instrument.
     */
    @Child(name = "paymentDate", type = {DateType.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When payment issued", formalDefinition="The date of payment as indicated on the financial instrument." )
    protected DateType paymentDate;

    /**
     * Total payment amount as indicated on the financial instrument.
     */
    @Child(name = "paymentAmount", type = {Money.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total amount of Payment", formalDefinition="Total payment amount as indicated on the financial instrument." )
    protected Money paymentAmount;

    /**
     * Issuer's unique identifier for the payment instrument.
     */
    @Child(name = "paymentIdentifier", type = {Identifier.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Business identifier for the payment", formalDefinition="Issuer's unique identifier for the payment instrument." )
    protected Identifier paymentIdentifier;

    /**
     * Distribution of the payment amount for a previously acknowledged payable.
     */
    @Child(name = "detail", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Settlement particulars", formalDefinition="Distribution of the payment amount for a previously acknowledged payable." )
    protected List<DetailsComponent> detail;

    /**
     * A code for the form to be used for printing the content.
     */
    @Child(name = "formCode", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Printed form identifier", formalDefinition="A code for the form to be used for printing the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/forms")
    protected CodeableConcept formCode;

    /**
     * A note that describes or explains the processing in a human readable form.
     */
    @Child(name = "processNote", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Note concerning processing", formalDefinition="A note that describes or explains the processing in a human readable form." )
    protected List<NotesComponent> processNote;

    private static final long serialVersionUID = -1620965037L;

  /**
   * Constructor
   */
    public PaymentReconciliation() {
      super();
    }

  /**
   * Constructor
   */
    public PaymentReconciliation(Enumeration<PaymentReconciliationStatus> status, DateTimeType created, DateType paymentDate, Money paymentAmount) {
      super();
      this.status = status;
      this.created = created;
      this.paymentDate = paymentDate;
      this.paymentAmount = paymentAmount;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this payment reconciliation.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PaymentReconciliation setIdentifier(List<Identifier> theIdentifier) { 
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

    public PaymentReconciliation addIdentifier(Identifier t) { //3
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
    public Enumeration<PaymentReconciliationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PaymentReconciliationStatus>(new PaymentReconciliationStatusEnumFactory()); // bb
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
    public PaymentReconciliation setStatusElement(Enumeration<PaymentReconciliationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public PaymentReconciliationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public PaymentReconciliation setStatus(PaymentReconciliationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PaymentReconciliationStatus>(new PaymentReconciliationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #period} (The period of time for which payments have been gathered into this bulk payment for settlement.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The period of time for which payments have been gathered into this bulk payment for settlement.)
     */
    public PaymentReconciliation setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when the resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.created");
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
     * @param value {@link #created} (The date when the resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public PaymentReconciliation setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when the resource was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when the resource was created.
     */
    public PaymentReconciliation setCreated(Date value) { 
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      return this;
    }

    /**
     * @return {@link #paymentIssuer} (The party who generated the payment.)
     */
    public Reference getPaymentIssuer() { 
      if (this.paymentIssuer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.paymentIssuer");
        else if (Configuration.doAutoCreate())
          this.paymentIssuer = new Reference(); // cc
      return this.paymentIssuer;
    }

    public boolean hasPaymentIssuer() { 
      return this.paymentIssuer != null && !this.paymentIssuer.isEmpty();
    }

    /**
     * @param value {@link #paymentIssuer} (The party who generated the payment.)
     */
    public PaymentReconciliation setPaymentIssuer(Reference value) { 
      this.paymentIssuer = value;
      return this;
    }

    /**
     * @return {@link #paymentIssuer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party who generated the payment.)
     */
    public Organization getPaymentIssuerTarget() { 
      if (this.paymentIssuerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.paymentIssuer");
        else if (Configuration.doAutoCreate())
          this.paymentIssuerTarget = new Organization(); // aa
      return this.paymentIssuerTarget;
    }

    /**
     * @param value {@link #paymentIssuer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party who generated the payment.)
     */
    public PaymentReconciliation setPaymentIssuerTarget(Organization value) { 
      this.paymentIssuerTarget = value;
      return this;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Reference getRequest() { 
      if (this.request == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.request");
        else if (Configuration.doAutoCreate())
          this.request = new Reference(); // cc
      return this.request;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Original request resource reference.)
     */
    public PaymentReconciliation setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public Task getRequestTarget() { 
      if (this.requestTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.request");
        else if (Configuration.doAutoCreate())
          this.requestTarget = new Task(); // aa
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public PaymentReconciliation setRequestTarget(Task value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #requestor} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getRequestor() { 
      if (this.requestor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.requestor");
        else if (Configuration.doAutoCreate())
          this.requestor = new Reference(); // cc
      return this.requestor;
    }

    public boolean hasRequestor() { 
      return this.requestor != null && !this.requestor.isEmpty();
    }

    /**
     * @param value {@link #requestor} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public PaymentReconciliation setRequestor(Reference value) { 
      this.requestor = value;
      return this;
    }

    /**
     * @return {@link #requestor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Resource getRequestorTarget() { 
      return this.requestorTarget;
    }

    /**
     * @param value {@link #requestor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public PaymentReconciliation setRequestorTarget(Resource value) { 
      this.requestorTarget = value;
      return this;
    }

    /**
     * @return {@link #outcome} (The outcome of a request for a reconciliation.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public Enumeration<RemittanceOutcome> getOutcomeElement() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new Enumeration<RemittanceOutcome>(new RemittanceOutcomeEnumFactory()); // bb
      return this.outcome;
    }

    public boolean hasOutcomeElement() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (The outcome of a request for a reconciliation.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public PaymentReconciliation setOutcomeElement(Enumeration<RemittanceOutcome> value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return The outcome of a request for a reconciliation.
     */
    public RemittanceOutcome getOutcome() { 
      return this.outcome == null ? null : this.outcome.getValue();
    }

    /**
     * @param value The outcome of a request for a reconciliation.
     */
    public PaymentReconciliation setOutcome(RemittanceOutcome value) { 
      if (value == null)
        this.outcome = null;
      else {
        if (this.outcome == null)
          this.outcome = new Enumeration<RemittanceOutcome>(new RemittanceOutcomeEnumFactory());
        this.outcome.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #disposition} (A human readable description of the status of the request for the reconciliation.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public StringType getDispositionElement() { 
      if (this.disposition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.disposition");
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
     * @param value {@link #disposition} (A human readable description of the status of the request for the reconciliation.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public PaymentReconciliation setDispositionElement(StringType value) { 
      this.disposition = value;
      return this;
    }

    /**
     * @return A human readable description of the status of the request for the reconciliation.
     */
    public String getDisposition() { 
      return this.disposition == null ? null : this.disposition.getValue();
    }

    /**
     * @param value A human readable description of the status of the request for the reconciliation.
     */
    public PaymentReconciliation setDisposition(String value) { 
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
     * @return {@link #paymentDate} (The date of payment as indicated on the financial instrument.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
     */
    public DateType getPaymentDateElement() { 
      if (this.paymentDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.paymentDate");
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
     * @param value {@link #paymentDate} (The date of payment as indicated on the financial instrument.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
     */
    public PaymentReconciliation setPaymentDateElement(DateType value) { 
      this.paymentDate = value;
      return this;
    }

    /**
     * @return The date of payment as indicated on the financial instrument.
     */
    public Date getPaymentDate() { 
      return this.paymentDate == null ? null : this.paymentDate.getValue();
    }

    /**
     * @param value The date of payment as indicated on the financial instrument.
     */
    public PaymentReconciliation setPaymentDate(Date value) { 
        if (this.paymentDate == null)
          this.paymentDate = new DateType();
        this.paymentDate.setValue(value);
      return this;
    }

    /**
     * @return {@link #paymentAmount} (Total payment amount as indicated on the financial instrument.)
     */
    public Money getPaymentAmount() { 
      if (this.paymentAmount == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.paymentAmount");
        else if (Configuration.doAutoCreate())
          this.paymentAmount = new Money(); // cc
      return this.paymentAmount;
    }

    public boolean hasPaymentAmount() { 
      return this.paymentAmount != null && !this.paymentAmount.isEmpty();
    }

    /**
     * @param value {@link #paymentAmount} (Total payment amount as indicated on the financial instrument.)
     */
    public PaymentReconciliation setPaymentAmount(Money value) { 
      this.paymentAmount = value;
      return this;
    }

    /**
     * @return {@link #paymentIdentifier} (Issuer's unique identifier for the payment instrument.)
     */
    public Identifier getPaymentIdentifier() { 
      if (this.paymentIdentifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.paymentIdentifier");
        else if (Configuration.doAutoCreate())
          this.paymentIdentifier = new Identifier(); // cc
      return this.paymentIdentifier;
    }

    public boolean hasPaymentIdentifier() { 
      return this.paymentIdentifier != null && !this.paymentIdentifier.isEmpty();
    }

    /**
     * @param value {@link #paymentIdentifier} (Issuer's unique identifier for the payment instrument.)
     */
    public PaymentReconciliation setPaymentIdentifier(Identifier value) { 
      this.paymentIdentifier = value;
      return this;
    }

    /**
     * @return {@link #detail} (Distribution of the payment amount for a previously acknowledged payable.)
     */
    public List<DetailsComponent> getDetail() { 
      if (this.detail == null)
        this.detail = new ArrayList<DetailsComponent>();
      return this.detail;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PaymentReconciliation setDetail(List<DetailsComponent> theDetail) { 
      this.detail = theDetail;
      return this;
    }

    public boolean hasDetail() { 
      if (this.detail == null)
        return false;
      for (DetailsComponent item : this.detail)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DetailsComponent addDetail() { //3
      DetailsComponent t = new DetailsComponent();
      if (this.detail == null)
        this.detail = new ArrayList<DetailsComponent>();
      this.detail.add(t);
      return t;
    }

    public PaymentReconciliation addDetail(DetailsComponent t) { //3
      if (t == null)
        return this;
      if (this.detail == null)
        this.detail = new ArrayList<DetailsComponent>();
      this.detail.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #detail}, creating it if it does not already exist
     */
    public DetailsComponent getDetailFirstRep() { 
      if (getDetail().isEmpty()) {
        addDetail();
      }
      return getDetail().get(0);
    }

    /**
     * @return {@link #formCode} (A code for the form to be used for printing the content.)
     */
    public CodeableConcept getFormCode() { 
      if (this.formCode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.formCode");
        else if (Configuration.doAutoCreate())
          this.formCode = new CodeableConcept(); // cc
      return this.formCode;
    }

    public boolean hasFormCode() { 
      return this.formCode != null && !this.formCode.isEmpty();
    }

    /**
     * @param value {@link #formCode} (A code for the form to be used for printing the content.)
     */
    public PaymentReconciliation setFormCode(CodeableConcept value) { 
      this.formCode = value;
      return this;
    }

    /**
     * @return {@link #processNote} (A note that describes or explains the processing in a human readable form.)
     */
    public List<NotesComponent> getProcessNote() { 
      if (this.processNote == null)
        this.processNote = new ArrayList<NotesComponent>();
      return this.processNote;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PaymentReconciliation setProcessNote(List<NotesComponent> theProcessNote) { 
      this.processNote = theProcessNote;
      return this;
    }

    public boolean hasProcessNote() { 
      if (this.processNote == null)
        return false;
      for (NotesComponent item : this.processNote)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public NotesComponent addProcessNote() { //3
      NotesComponent t = new NotesComponent();
      if (this.processNote == null)
        this.processNote = new ArrayList<NotesComponent>();
      this.processNote.add(t);
      return t;
    }

    public PaymentReconciliation addProcessNote(NotesComponent t) { //3
      if (t == null)
        return this;
      if (this.processNote == null)
        this.processNote = new ArrayList<NotesComponent>();
      this.processNote.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #processNote}, creating it if it does not already exist
     */
    public NotesComponent getProcessNoteFirstRep() { 
      if (getProcessNote().isEmpty()) {
        addProcessNote();
      }
      return getProcessNote().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A unique identifier assigned to this payment reconciliation.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The status of the resource instance.", 0, 1, status));
        children.add(new Property("period", "Period", "The period of time for which payments have been gathered into this bulk payment for settlement.", 0, 1, period));
        children.add(new Property("created", "dateTime", "The date when the resource was created.", 0, 1, created));
        children.add(new Property("paymentIssuer", "Reference(Organization)", "The party who generated the payment.", 0, 1, paymentIssuer));
        children.add(new Property("request", "Reference(Task)", "Original request resource reference.", 0, 1, request));
        children.add(new Property("requestor", "Reference(Practitioner|PractitionerRole|Organization)", "The practitioner who is responsible for the services rendered to the patient.", 0, 1, requestor));
        children.add(new Property("outcome", "code", "The outcome of a request for a reconciliation.", 0, 1, outcome));
        children.add(new Property("disposition", "string", "A human readable description of the status of the request for the reconciliation.", 0, 1, disposition));
        children.add(new Property("paymentDate", "date", "The date of payment as indicated on the financial instrument.", 0, 1, paymentDate));
        children.add(new Property("paymentAmount", "Money", "Total payment amount as indicated on the financial instrument.", 0, 1, paymentAmount));
        children.add(new Property("paymentIdentifier", "Identifier", "Issuer's unique identifier for the payment instrument.", 0, 1, paymentIdentifier));
        children.add(new Property("detail", "", "Distribution of the payment amount for a previously acknowledged payable.", 0, java.lang.Integer.MAX_VALUE, detail));
        children.add(new Property("formCode", "CodeableConcept", "A code for the form to be used for printing the content.", 0, 1, formCode));
        children.add(new Property("processNote", "", "A note that describes or explains the processing in a human readable form.", 0, java.lang.Integer.MAX_VALUE, processNote));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A unique identifier assigned to this payment reconciliation.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the resource instance.", 0, 1, status);
        case -991726143: /*period*/  return new Property("period", "Period", "The period of time for which payments have been gathered into this bulk payment for settlement.", 0, 1, period);
        case 1028554472: /*created*/  return new Property("created", "dateTime", "The date when the resource was created.", 0, 1, created);
        case 1144026207: /*paymentIssuer*/  return new Property("paymentIssuer", "Reference(Organization)", "The party who generated the payment.", 0, 1, paymentIssuer);
        case 1095692943: /*request*/  return new Property("request", "Reference(Task)", "Original request resource reference.", 0, 1, request);
        case 693934258: /*requestor*/  return new Property("requestor", "Reference(Practitioner|PractitionerRole|Organization)", "The practitioner who is responsible for the services rendered to the patient.", 0, 1, requestor);
        case -1106507950: /*outcome*/  return new Property("outcome", "code", "The outcome of a request for a reconciliation.", 0, 1, outcome);
        case 583380919: /*disposition*/  return new Property("disposition", "string", "A human readable description of the status of the request for the reconciliation.", 0, 1, disposition);
        case -1540873516: /*paymentDate*/  return new Property("paymentDate", "date", "The date of payment as indicated on the financial instrument.", 0, 1, paymentDate);
        case 909332990: /*paymentAmount*/  return new Property("paymentAmount", "Money", "Total payment amount as indicated on the financial instrument.", 0, 1, paymentAmount);
        case 1555852111: /*paymentIdentifier*/  return new Property("paymentIdentifier", "Identifier", "Issuer's unique identifier for the payment instrument.", 0, 1, paymentIdentifier);
        case -1335224239: /*detail*/  return new Property("detail", "", "Distribution of the payment amount for a previously acknowledged payable.", 0, java.lang.Integer.MAX_VALUE, detail);
        case 473181393: /*formCode*/  return new Property("formCode", "CodeableConcept", "A code for the form to be used for printing the content.", 0, 1, formCode);
        case 202339073: /*processNote*/  return new Property("processNote", "", "A note that describes or explains the processing in a human readable form.", 0, java.lang.Integer.MAX_VALUE, processNote);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PaymentReconciliationStatus>
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1144026207: /*paymentIssuer*/ return this.paymentIssuer == null ? new Base[0] : new Base[] {this.paymentIssuer}; // Reference
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case 693934258: /*requestor*/ return this.requestor == null ? new Base[0] : new Base[] {this.requestor}; // Reference
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // Enumeration<RemittanceOutcome>
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case -1540873516: /*paymentDate*/ return this.paymentDate == null ? new Base[0] : new Base[] {this.paymentDate}; // DateType
        case 909332990: /*paymentAmount*/ return this.paymentAmount == null ? new Base[0] : new Base[] {this.paymentAmount}; // Money
        case 1555852111: /*paymentIdentifier*/ return this.paymentIdentifier == null ? new Base[0] : new Base[] {this.paymentIdentifier}; // Identifier
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // DetailsComponent
        case 473181393: /*formCode*/ return this.formCode == null ? new Base[0] : new Base[] {this.formCode}; // CodeableConcept
        case 202339073: /*processNote*/ return this.processNote == null ? new Base[0] : this.processNote.toArray(new Base[this.processNote.size()]); // NotesComponent
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
          value = new PaymentReconciliationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PaymentReconciliationStatus>
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case 1144026207: // paymentIssuer
          this.paymentIssuer = castToReference(value); // Reference
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case 693934258: // requestor
          this.requestor = castToReference(value); // Reference
          return value;
        case -1106507950: // outcome
          value = new RemittanceOutcomeEnumFactory().fromType(castToCode(value));
          this.outcome = (Enumeration) value; // Enumeration<RemittanceOutcome>
          return value;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
          return value;
        case -1540873516: // paymentDate
          this.paymentDate = castToDate(value); // DateType
          return value;
        case 909332990: // paymentAmount
          this.paymentAmount = castToMoney(value); // Money
          return value;
        case 1555852111: // paymentIdentifier
          this.paymentIdentifier = castToIdentifier(value); // Identifier
          return value;
        case -1335224239: // detail
          this.getDetail().add((DetailsComponent) value); // DetailsComponent
          return value;
        case 473181393: // formCode
          this.formCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 202339073: // processNote
          this.getProcessNote().add((NotesComponent) value); // NotesComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new PaymentReconciliationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PaymentReconciliationStatus>
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("paymentIssuer")) {
          this.paymentIssuer = castToReference(value); // Reference
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("requestor")) {
          this.requestor = castToReference(value); // Reference
        } else if (name.equals("outcome")) {
          value = new RemittanceOutcomeEnumFactory().fromType(castToCode(value));
          this.outcome = (Enumeration) value; // Enumeration<RemittanceOutcome>
        } else if (name.equals("disposition")) {
          this.disposition = castToString(value); // StringType
        } else if (name.equals("paymentDate")) {
          this.paymentDate = castToDate(value); // DateType
        } else if (name.equals("paymentAmount")) {
          this.paymentAmount = castToMoney(value); // Money
        } else if (name.equals("paymentIdentifier")) {
          this.paymentIdentifier = castToIdentifier(value); // Identifier
        } else if (name.equals("detail")) {
          this.getDetail().add((DetailsComponent) value);
        } else if (name.equals("formCode")) {
          this.formCode = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("processNote")) {
          this.getProcessNote().add((NotesComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -991726143:  return getPeriod(); 
        case 1028554472:  return getCreatedElement();
        case 1144026207:  return getPaymentIssuer(); 
        case 1095692943:  return getRequest(); 
        case 693934258:  return getRequestor(); 
        case -1106507950:  return getOutcomeElement();
        case 583380919:  return getDispositionElement();
        case -1540873516:  return getPaymentDateElement();
        case 909332990:  return getPaymentAmount(); 
        case 1555852111:  return getPaymentIdentifier(); 
        case -1335224239:  return addDetail(); 
        case 473181393:  return getFormCode(); 
        case 202339073:  return addProcessNote(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case 1144026207: /*paymentIssuer*/ return new String[] {"Reference"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case 693934258: /*requestor*/ return new String[] {"Reference"};
        case -1106507950: /*outcome*/ return new String[] {"code"};
        case 583380919: /*disposition*/ return new String[] {"string"};
        case -1540873516: /*paymentDate*/ return new String[] {"date"};
        case 909332990: /*paymentAmount*/ return new String[] {"Money"};
        case 1555852111: /*paymentIdentifier*/ return new String[] {"Identifier"};
        case -1335224239: /*detail*/ return new String[] {};
        case 473181393: /*formCode*/ return new String[] {"CodeableConcept"};
        case 202339073: /*processNote*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.status");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.created");
        }
        else if (name.equals("paymentIssuer")) {
          this.paymentIssuer = new Reference();
          return this.paymentIssuer;
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("requestor")) {
          this.requestor = new Reference();
          return this.requestor;
        }
        else if (name.equals("outcome")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.outcome");
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.disposition");
        }
        else if (name.equals("paymentDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.paymentDate");
        }
        else if (name.equals("paymentAmount")) {
          this.paymentAmount = new Money();
          return this.paymentAmount;
        }
        else if (name.equals("paymentIdentifier")) {
          this.paymentIdentifier = new Identifier();
          return this.paymentIdentifier;
        }
        else if (name.equals("detail")) {
          return addDetail();
        }
        else if (name.equals("formCode")) {
          this.formCode = new CodeableConcept();
          return this.formCode;
        }
        else if (name.equals("processNote")) {
          return addProcessNote();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "PaymentReconciliation";

  }

      public PaymentReconciliation copy() {
        PaymentReconciliation dst = new PaymentReconciliation();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.period = period == null ? null : period.copy();
        dst.created = created == null ? null : created.copy();
        dst.paymentIssuer = paymentIssuer == null ? null : paymentIssuer.copy();
        dst.request = request == null ? null : request.copy();
        dst.requestor = requestor == null ? null : requestor.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.paymentDate = paymentDate == null ? null : paymentDate.copy();
        dst.paymentAmount = paymentAmount == null ? null : paymentAmount.copy();
        dst.paymentIdentifier = paymentIdentifier == null ? null : paymentIdentifier.copy();
        if (detail != null) {
          dst.detail = new ArrayList<DetailsComponent>();
          for (DetailsComponent i : detail)
            dst.detail.add(i.copy());
        };
        dst.formCode = formCode == null ? null : formCode.copy();
        if (processNote != null) {
          dst.processNote = new ArrayList<NotesComponent>();
          for (NotesComponent i : processNote)
            dst.processNote.add(i.copy());
        };
        return dst;
      }

      protected PaymentReconciliation typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof PaymentReconciliation))
          return false;
        PaymentReconciliation o = (PaymentReconciliation) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(period, o.period, true)
           && compareDeep(created, o.created, true) && compareDeep(paymentIssuer, o.paymentIssuer, true) && compareDeep(request, o.request, true)
           && compareDeep(requestor, o.requestor, true) && compareDeep(outcome, o.outcome, true) && compareDeep(disposition, o.disposition, true)
           && compareDeep(paymentDate, o.paymentDate, true) && compareDeep(paymentAmount, o.paymentAmount, true)
           && compareDeep(paymentIdentifier, o.paymentIdentifier, true) && compareDeep(detail, o.detail, true)
           && compareDeep(formCode, o.formCode, true) && compareDeep(processNote, o.processNote, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof PaymentReconciliation))
          return false;
        PaymentReconciliation o = (PaymentReconciliation) other_;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true) && compareValues(outcome, o.outcome, true)
           && compareValues(disposition, o.disposition, true) && compareValues(paymentDate, o.paymentDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, period
          , created, paymentIssuer, request, requestor, outcome, disposition, paymentDate
          , paymentAmount, paymentIdentifier, detail, formCode, processNote);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.PaymentReconciliation;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the ExplanationOfBenefit</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="PaymentReconciliation.identifier", description="The business identifier of the ExplanationOfBenefit", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the ExplanationOfBenefit</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>request</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.request</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request", path="PaymentReconciliation.request", description="The reference to the claim", type="reference", target={Task.class } )
  public static final String SP_REQUEST = "request";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.request</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:request</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:request").toLocked();

 /**
   * Search parameter: <b>disposition</b>
   * <p>
   * Description: <b>The contents of the disposition message</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PaymentReconciliation.disposition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="disposition", path="PaymentReconciliation.disposition", description="The contents of the disposition message", type="string" )
  public static final String SP_DISPOSITION = "disposition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>disposition</b>
   * <p>
   * Description: <b>The contents of the disposition message</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PaymentReconciliation.disposition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DISPOSITION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DISPOSITION);

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>The creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PaymentReconciliation.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="PaymentReconciliation.created", description="The creation date", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>The creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PaymentReconciliation.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>payment-issuer</b>
   * <p>
   * Description: <b>The organization which generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.paymentIssuer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="payment-issuer", path="PaymentReconciliation.paymentIssuer", description="The organization which generated this resource", type="reference", target={Organization.class } )
  public static final String SP_PAYMENT_ISSUER = "payment-issuer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>payment-issuer</b>
   * <p>
   * Description: <b>The organization which generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.paymentIssuer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PAYMENT_ISSUER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PAYMENT_ISSUER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:payment-issuer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PAYMENT_ISSUER = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:payment-issuer").toLocked();

 /**
   * Search parameter: <b>outcome</b>
   * <p>
   * Description: <b>The processing outcome</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.outcome</b><br>
   * </p>
   */
  @SearchParamDefinition(name="outcome", path="PaymentReconciliation.outcome", description="The processing outcome", type="token" )
  public static final String SP_OUTCOME = "outcome";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>outcome</b>
   * <p>
   * Description: <b>The processing outcome</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.outcome</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam OUTCOME = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_OUTCOME);

 /**
   * Search parameter: <b>requestor</b>
   * <p>
   * Description: <b>The reference to the provider who submitted the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestor", path="PaymentReconciliation.requestor", description="The reference to the provider who submitted the claim", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class, PractitionerRole.class } )
  public static final String SP_REQUESTOR = "requestor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestor</b>
   * <p>
   * Description: <b>The reference to the provider who submitted the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:requestor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTOR = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:requestor").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the payment reconciliation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="PaymentReconciliation.status", description="The status of the payment reconciliation", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the payment reconciliation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

