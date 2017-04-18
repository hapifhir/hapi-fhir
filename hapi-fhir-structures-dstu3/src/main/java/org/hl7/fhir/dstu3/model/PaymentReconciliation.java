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
 * This resource provides payment details and claim references supporting a bulk payment.
 */
@ResourceDef(name="PaymentReconciliation", profile="http://hl7.org/fhir/Profile/PaymentReconciliation")
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
         * Code to indicate the nature of the payment, adjustment, funds advance, etc.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type code", formalDefinition="Code to indicate the nature of the payment, adjustment, funds advance, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payment-type")
        protected CodeableConcept type;

        /**
         * The claim or financial resource.
         */
        @Child(name = "request", type = {Reference.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Claim", formalDefinition="The claim or financial resource." )
        protected Reference request;

        /**
         * The actual object that is the target of the reference (The claim or financial resource.)
         */
        protected Resource requestTarget;

        /**
         * The claim response resource.
         */
        @Child(name = "response", type = {Reference.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Claim Response", formalDefinition="The claim response resource." )
        protected Reference response;

        /**
         * The actual object that is the target of the reference (The claim response resource.)
         */
        protected Resource responseTarget;

        /**
         * The Organization which submitted the claim or financial transaction.
         */
        @Child(name = "submitter", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Organization which submitted the claim", formalDefinition="The Organization which submitted the claim or financial transaction." )
        protected Reference submitter;

        /**
         * The actual object that is the target of the reference (The Organization which submitted the claim or financial transaction.)
         */
        protected Organization submitterTarget;

        /**
         * The organization which is receiving the payment.
         */
        @Child(name = "payee", type = {Organization.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Organization which is receiving the payment", formalDefinition="The organization which is receiving the payment." )
        protected Reference payee;

        /**
         * The actual object that is the target of the reference (The organization which is receiving the payment.)
         */
        protected Organization payeeTarget;

        /**
         * The date of the invoice or financial resource.
         */
        @Child(name = "date", type = {DateType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Invoice date", formalDefinition="The date of the invoice or financial resource." )
        protected DateType date;

        /**
         * Amount paid for this detail.
         */
        @Child(name = "amount", type = {Money.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Amount being paid", formalDefinition="Amount paid for this detail." )
        protected Money amount;

        private static final long serialVersionUID = 661095855L;

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
         * @return {@link #type} (Code to indicate the nature of the payment, adjustment, funds advance, etc.)
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
         * @param value {@link #type} (Code to indicate the nature of the payment, adjustment, funds advance, etc.)
         */
        public DetailsComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #request} (The claim or financial resource.)
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
         * @param value {@link #request} (The claim or financial resource.)
         */
        public DetailsComponent setRequest(Reference value) { 
          this.request = value;
          return this;
        }

        /**
         * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The claim or financial resource.)
         */
        public Resource getRequestTarget() { 
          return this.requestTarget;
        }

        /**
         * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The claim or financial resource.)
         */
        public DetailsComponent setRequestTarget(Resource value) { 
          this.requestTarget = value;
          return this;
        }

        /**
         * @return {@link #response} (The claim response resource.)
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
         * @param value {@link #response} (The claim response resource.)
         */
        public DetailsComponent setResponse(Reference value) { 
          this.response = value;
          return this;
        }

        /**
         * @return {@link #response} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The claim response resource.)
         */
        public Resource getResponseTarget() { 
          return this.responseTarget;
        }

        /**
         * @param value {@link #response} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The claim response resource.)
         */
        public DetailsComponent setResponseTarget(Resource value) { 
          this.responseTarget = value;
          return this;
        }

        /**
         * @return {@link #submitter} (The Organization which submitted the claim or financial transaction.)
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
         * @param value {@link #submitter} (The Organization which submitted the claim or financial transaction.)
         */
        public DetailsComponent setSubmitter(Reference value) { 
          this.submitter = value;
          return this;
        }

        /**
         * @return {@link #submitter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Organization which submitted the claim or financial transaction.)
         */
        public Organization getSubmitterTarget() { 
          if (this.submitterTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.submitter");
            else if (Configuration.doAutoCreate())
              this.submitterTarget = new Organization(); // aa
          return this.submitterTarget;
        }

        /**
         * @param value {@link #submitter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Organization which submitted the claim or financial transaction.)
         */
        public DetailsComponent setSubmitterTarget(Organization value) { 
          this.submitterTarget = value;
          return this;
        }

        /**
         * @return {@link #payee} (The organization which is receiving the payment.)
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
         * @param value {@link #payee} (The organization which is receiving the payment.)
         */
        public DetailsComponent setPayee(Reference value) { 
          this.payee = value;
          return this;
        }

        /**
         * @return {@link #payee} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is receiving the payment.)
         */
        public Organization getPayeeTarget() { 
          if (this.payeeTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.payee");
            else if (Configuration.doAutoCreate())
              this.payeeTarget = new Organization(); // aa
          return this.payeeTarget;
        }

        /**
         * @param value {@link #payee} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is receiving the payment.)
         */
        public DetailsComponent setPayeeTarget(Organization value) { 
          this.payeeTarget = value;
          return this;
        }

        /**
         * @return {@link #date} (The date of the invoice or financial resource.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
         * @param value {@link #date} (The date of the invoice or financial resource.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DetailsComponent setDateElement(DateType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return The date of the invoice or financial resource.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value The date of the invoice or financial resource.
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
         * @return {@link #amount} (Amount paid for this detail.)
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
         * @param value {@link #amount} (Amount paid for this detail.)
         */
        public DetailsComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Code to indicate the nature of the payment, adjustment, funds advance, etc.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("request", "Reference(Any)", "The claim or financial resource.", 0, java.lang.Integer.MAX_VALUE, request));
          childrenList.add(new Property("response", "Reference(Any)", "The claim response resource.", 0, java.lang.Integer.MAX_VALUE, response));
          childrenList.add(new Property("submitter", "Reference(Organization)", "The Organization which submitted the claim or financial transaction.", 0, java.lang.Integer.MAX_VALUE, submitter));
          childrenList.add(new Property("payee", "Reference(Organization)", "The organization which is receiving the payment.", 0, java.lang.Integer.MAX_VALUE, payee));
          childrenList.add(new Property("date", "date", "The date of the invoice or financial resource.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("amount", "Money", "Amount paid for this detail.", 0, java.lang.Integer.MAX_VALUE, amount));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // Reference
        case 348678409: /*submitter*/ return this.submitter == null ? new Base[0] : new Base[] {this.submitter}; // Reference
        case 106443592: /*payee*/ return this.payee == null ? new Base[0] : new Base[] {this.payee}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case -340323263: // response
          this.response = castToReference(value); // Reference
          return value;
        case 348678409: // submitter
          this.submitter = castToReference(value); // Reference
          return value;
        case 106443592: // payee
          this.payee = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDate(value); // DateType
          return value;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("response")) {
          this.response = castToReference(value); // Reference
        } else if (name.equals("submitter")) {
          this.submitter = castToReference(value); // Reference
        } else if (name.equals("payee")) {
          this.payee = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDate(value); // DateType
        } else if (name.equals("amount")) {
          this.amount = castToMoney(value); // Money
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 1095692943:  return getRequest(); 
        case -340323263:  return getResponse(); 
        case 348678409:  return getSubmitter(); 
        case 106443592:  return getPayee(); 
        case 3076014:  return getDateElement();
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -340323263: /*response*/ return new String[] {"Reference"};
        case 348678409: /*submitter*/ return new String[] {"Reference"};
        case 106443592: /*payee*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"date"};
        case -1413853096: /*amount*/ return new String[] {"Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("response")) {
          this.response = new Reference();
          return this.response;
        }
        else if (name.equals("submitter")) {
          this.submitter = new Reference();
          return this.submitter;
        }
        else if (name.equals("payee")) {
          this.payee = new Reference();
          return this.payee;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.date");
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
        dst.type = type == null ? null : type.copy();
        dst.request = request == null ? null : request.copy();
        dst.response = response == null ? null : response.copy();
        dst.submitter = submitter == null ? null : submitter.copy();
        dst.payee = payee == null ? null : payee.copy();
        dst.date = date == null ? null : date.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DetailsComponent))
          return false;
        DetailsComponent o = (DetailsComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(request, o.request, true) && compareDeep(response, o.response, true)
           && compareDeep(submitter, o.submitter, true) && compareDeep(payee, o.payee, true) && compareDeep(date, o.date, true)
           && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DetailsComponent))
          return false;
        DetailsComponent o = (DetailsComponent) other;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, request, response
          , submitter, payee, date, amount);
      }

  public String fhirType() {
    return "PaymentReconciliation.detail";

  }

  }

    @Block()
    public static class NotesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The note purpose: Print/Display.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The note purpose: Print/Display." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/note-type")
        protected CodeableConcept type;

        /**
         * The note text.
         */
        @Child(name = "text", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Comment on the processing", formalDefinition="The note text." )
        protected StringType text;

        private static final long serialVersionUID = 874830709L;

    /**
     * Constructor
     */
      public NotesComponent() {
        super();
      }

        /**
         * @return {@link #type} (The note purpose: Print/Display.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NotesComponent.type");
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
        public NotesComponent setType(CodeableConcept value) { 
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
          childrenList.add(new Property("type", "CodeableConcept", "The note purpose: Print/Display.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("text", "string", "The note text.", 0, java.lang.Integer.MAX_VALUE, text));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
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
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 3556653:  return getTextElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3556653: /*text*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NotesComponent))
          return false;
        NotesComponent o = (NotesComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(text, o.text, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NotesComponent))
          return false;
        NotesComponent o = (NotesComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, text);
      }

  public String fhirType() {
    return "PaymentReconciliation.processNote";

  }

  }

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<PaymentReconciliationStatus> status;

    /**
     * The period of time for which payments have been gathered into this bulk payment for settlement.
     */
    @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Period covered", formalDefinition="The period of time for which payments have been gathered into this bulk payment for settlement." )
    protected Period period;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The Insurer who produced this adjudicated response.
     */
    @Child(name = "organization", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurer", formalDefinition="The Insurer who produced this adjudicated response." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The Insurer who produced this adjudicated response.)
     */
    protected Organization organizationTarget;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {ProcessRequest.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Claim reference", formalDefinition="Original request resource reference." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Original request resource reference.)
     */
    protected ProcessRequest requestTarget;

    /**
     * Transaction status: error, complete.
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="complete | error | partial", formalDefinition="Transaction status: error, complete." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/remittance-outcome")
    protected CodeableConcept outcome;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Practitioner.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference requestProvider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner requestProviderTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "requestOrganization", type = {Organization.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference requestOrganization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization requestOrganizationTarget;

    /**
     * List of individual settlement amounts and the corresponding transaction.
     */
    @Child(name = "detail", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="List of settlements", formalDefinition="List of individual settlement amounts and the corresponding transaction." )
    protected List<DetailsComponent> detail;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {CodeableConcept.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/forms")
    protected CodeableConcept form;

    /**
     * Total payment amount.
     */
    @Child(name = "total", type = {Money.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Total amount of Payment", formalDefinition="Total payment amount." )
    protected Money total;

    /**
     * Suite of notes.
     */
    @Child(name = "processNote", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Processing comments", formalDefinition="Suite of notes." )
    protected List<NotesComponent> processNote;

    private static final long serialVersionUID = -665475468L;

  /**
   * Constructor
   */
    public PaymentReconciliation() {
      super();
    }

    /**
     * @return {@link #identifier} (The Response business identifier.)
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
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<PaymentReconciliationStatus>(new PaymentReconciliationStatusEnumFactory());
        this.status.setValue(value);
      }
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
     * @return {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
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
     * @param value {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public PaymentReconciliation setCreatedElement(DateTimeType value) { 
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
    public PaymentReconciliation setCreated(Date value) { 
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
     * @return {@link #organization} (The Insurer who produced this adjudicated response.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The Insurer who produced this adjudicated response.)
     */
    public PaymentReconciliation setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who produced this adjudicated response.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who produced this adjudicated response.)
     */
    public PaymentReconciliation setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
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
    public ProcessRequest getRequestTarget() { 
      if (this.requestTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.request");
        else if (Configuration.doAutoCreate())
          this.requestTarget = new ProcessRequest(); // aa
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public PaymentReconciliation setRequestTarget(ProcessRequest value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #outcome} (Transaction status: error, complete.)
     */
    public CodeableConcept getOutcome() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new CodeableConcept(); // cc
      return this.outcome;
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (Transaction status: error, complete.)
     */
    public PaymentReconciliation setOutcome(CodeableConcept value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return {@link #disposition} (A description of the status of the adjudication.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
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
     * @param value {@link #disposition} (A description of the status of the adjudication.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public PaymentReconciliation setDispositionElement(StringType value) { 
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
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getRequestProvider() { 
      if (this.requestProvider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.requestProvider");
        else if (Configuration.doAutoCreate())
          this.requestProvider = new Reference(); // cc
      return this.requestProvider;
    }

    public boolean hasRequestProvider() { 
      return this.requestProvider != null && !this.requestProvider.isEmpty();
    }

    /**
     * @param value {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public PaymentReconciliation setRequestProvider(Reference value) { 
      this.requestProvider = value;
      return this;
    }

    /**
     * @return {@link #requestProvider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Practitioner getRequestProviderTarget() { 
      if (this.requestProviderTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.requestProvider");
        else if (Configuration.doAutoCreate())
          this.requestProviderTarget = new Practitioner(); // aa
      return this.requestProviderTarget;
    }

    /**
     * @param value {@link #requestProvider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public PaymentReconciliation setRequestProviderTarget(Practitioner value) { 
      this.requestProviderTarget = value;
      return this;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getRequestOrganization() { 
      if (this.requestOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.requestOrganization");
        else if (Configuration.doAutoCreate())
          this.requestOrganization = new Reference(); // cc
      return this.requestOrganization;
    }

    public boolean hasRequestOrganization() { 
      return this.requestOrganization != null && !this.requestOrganization.isEmpty();
    }

    /**
     * @param value {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public PaymentReconciliation setRequestOrganization(Reference value) { 
      this.requestOrganization = value;
      return this;
    }

    /**
     * @return {@link #requestOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public Organization getRequestOrganizationTarget() { 
      if (this.requestOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.requestOrganization");
        else if (Configuration.doAutoCreate())
          this.requestOrganizationTarget = new Organization(); // aa
      return this.requestOrganizationTarget;
    }

    /**
     * @param value {@link #requestOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public PaymentReconciliation setRequestOrganizationTarget(Organization value) { 
      this.requestOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #detail} (List of individual settlement amounts and the corresponding transaction.)
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
     * @return {@link #form} (The form to be used for printing the content.)
     */
    public CodeableConcept getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.form");
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
    public PaymentReconciliation setForm(CodeableConcept value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #total} (Total payment amount.)
     */
    public Money getTotal() { 
      if (this.total == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.total");
        else if (Configuration.doAutoCreate())
          this.total = new Money(); // cc
      return this.total;
    }

    public boolean hasTotal() { 
      return this.total != null && !this.total.isEmpty();
    }

    /**
     * @param value {@link #total} (Total payment amount.)
     */
    public PaymentReconciliation setTotal(Money value) { 
      this.total = value;
      return this;
    }

    /**
     * @return {@link #processNote} (Suite of notes.)
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the resource instance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("period", "Period", "The period of time for which payments have been gathered into this bulk payment for settlement.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("organization", "Reference(Organization)", "The Insurer who produced this adjudicated response.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("request", "Reference(ProcessRequest)", "Original request resource reference.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("outcome", "CodeableConcept", "Transaction status: error, complete.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("requestProvider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestProvider));
        childrenList.add(new Property("requestOrganization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestOrganization));
        childrenList.add(new Property("detail", "", "List of individual settlement amounts and the corresponding transaction.", 0, java.lang.Integer.MAX_VALUE, detail));
        childrenList.add(new Property("form", "CodeableConcept", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("total", "Money", "Total payment amount.", 0, java.lang.Integer.MAX_VALUE, total));
        childrenList.add(new Property("processNote", "", "Suite of notes.", 0, java.lang.Integer.MAX_VALUE, processNote));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PaymentReconciliationStatus>
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // CodeableConcept
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case 1601527200: /*requestProvider*/ return this.requestProvider == null ? new Base[0] : new Base[] {this.requestProvider}; // Reference
        case 599053666: /*requestOrganization*/ return this.requestOrganization == null ? new Base[0] : new Base[] {this.requestOrganization}; // Reference
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // DetailsComponent
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // CodeableConcept
        case 110549828: /*total*/ return this.total == null ? new Base[0] : new Base[] {this.total}; // Money
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
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case -1106507950: // outcome
          this.outcome = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
          return value;
        case 1601527200: // requestProvider
          this.requestProvider = castToReference(value); // Reference
          return value;
        case 599053666: // requestOrganization
          this.requestOrganization = castToReference(value); // Reference
          return value;
        case -1335224239: // detail
          this.getDetail().add((DetailsComponent) value); // DetailsComponent
          return value;
        case 3148996: // form
          this.form = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 110549828: // total
          this.total = castToMoney(value); // Money
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
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("outcome")) {
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("disposition")) {
          this.disposition = castToString(value); // StringType
        } else if (name.equals("requestProvider")) {
          this.requestProvider = castToReference(value); // Reference
        } else if (name.equals("requestOrganization")) {
          this.requestOrganization = castToReference(value); // Reference
        } else if (name.equals("detail")) {
          this.getDetail().add((DetailsComponent) value);
        } else if (name.equals("form")) {
          this.form = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("total")) {
          this.total = castToMoney(value); // Money
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
        case 1178922291:  return getOrganization(); 
        case 1095692943:  return getRequest(); 
        case -1106507950:  return getOutcome(); 
        case 583380919:  return getDispositionElement();
        case 1601527200:  return getRequestProvider(); 
        case 599053666:  return getRequestOrganization(); 
        case -1335224239:  return addDetail(); 
        case 3148996:  return getForm(); 
        case 110549828:  return getTotal(); 
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
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -1106507950: /*outcome*/ return new String[] {"CodeableConcept"};
        case 583380919: /*disposition*/ return new String[] {"string"};
        case 1601527200: /*requestProvider*/ return new String[] {"Reference"};
        case 599053666: /*requestOrganization*/ return new String[] {"Reference"};
        case -1335224239: /*detail*/ return new String[] {};
        case 3148996: /*form*/ return new String[] {"CodeableConcept"};
        case 110549828: /*total*/ return new String[] {"Money"};
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
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("outcome")) {
          this.outcome = new CodeableConcept();
          return this.outcome;
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.disposition");
        }
        else if (name.equals("requestProvider")) {
          this.requestProvider = new Reference();
          return this.requestProvider;
        }
        else if (name.equals("requestOrganization")) {
          this.requestOrganization = new Reference();
          return this.requestOrganization;
        }
        else if (name.equals("detail")) {
          return addDetail();
        }
        else if (name.equals("form")) {
          this.form = new CodeableConcept();
          return this.form;
        }
        else if (name.equals("total")) {
          this.total = new Money();
          return this.total;
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
        dst.organization = organization == null ? null : organization.copy();
        dst.request = request == null ? null : request.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.requestProvider = requestProvider == null ? null : requestProvider.copy();
        dst.requestOrganization = requestOrganization == null ? null : requestOrganization.copy();
        if (detail != null) {
          dst.detail = new ArrayList<DetailsComponent>();
          for (DetailsComponent i : detail)
            dst.detail.add(i.copy());
        };
        dst.form = form == null ? null : form.copy();
        dst.total = total == null ? null : total.copy();
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PaymentReconciliation))
          return false;
        PaymentReconciliation o = (PaymentReconciliation) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(period, o.period, true)
           && compareDeep(created, o.created, true) && compareDeep(organization, o.organization, true) && compareDeep(request, o.request, true)
           && compareDeep(outcome, o.outcome, true) && compareDeep(disposition, o.disposition, true) && compareDeep(requestProvider, o.requestProvider, true)
           && compareDeep(requestOrganization, o.requestOrganization, true) && compareDeep(detail, o.detail, true)
           && compareDeep(form, o.form, true) && compareDeep(total, o.total, true) && compareDeep(processNote, o.processNote, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PaymentReconciliation))
          return false;
        PaymentReconciliation o = (PaymentReconciliation) other;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true) && compareValues(disposition, o.disposition, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, period
          , created, organization, request, outcome, disposition, requestProvider, requestOrganization
          , detail, form, total, processNote);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.PaymentReconciliation;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Explanation of Benefit</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="PaymentReconciliation.identifier", description="The business identifier of the Explanation of Benefit", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Explanation of Benefit</b><br>
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
  @SearchParamDefinition(name="request", path="PaymentReconciliation.request", description="The reference to the claim", type="reference", target={ProcessRequest.class } )
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
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="PaymentReconciliation.organization", description="The organization who generated this resource", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:organization").toLocked();

 /**
   * Search parameter: <b>request-organization</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request-organization", path="PaymentReconciliation.requestOrganization", description="The organization who generated this resource", type="reference", target={Organization.class } )
  public static final String SP_REQUEST_ORGANIZATION = "request-organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request-organization</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST_ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:request-organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST_ORGANIZATION = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:request-organization").toLocked();

 /**
   * Search parameter: <b>request-provider</b>
   * <p>
   * Description: <b>The reference to the provider who sumbitted the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestProvider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request-provider", path="PaymentReconciliation.requestProvider", description="The reference to the provider who sumbitted the claim", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_REQUEST_PROVIDER = "request-provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request-provider</b>
   * <p>
   * Description: <b>The reference to the provider who sumbitted the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestProvider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST_PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:request-provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST_PROVIDER = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:request-provider").toLocked();

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


}

