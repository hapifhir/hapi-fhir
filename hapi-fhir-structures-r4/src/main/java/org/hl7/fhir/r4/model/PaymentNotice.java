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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * This resource provides the status of the payment for goods and services rendered, and the request and response resource references.
 */
@ResourceDef(name="PaymentNotice", profile="http://hl7.org/fhir/StructureDefinition/PaymentNotice")
public class PaymentNotice extends DomainResource {

    public enum PaymentNoticeStatus {
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
        public static PaymentNoticeStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown PaymentNoticeStatus code '"+codeString+"'");
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

  public static class PaymentNoticeStatusEnumFactory implements EnumFactory<PaymentNoticeStatus> {
    public PaymentNoticeStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return PaymentNoticeStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return PaymentNoticeStatus.CANCELLED;
        if ("draft".equals(codeString))
          return PaymentNoticeStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return PaymentNoticeStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown PaymentNoticeStatus code '"+codeString+"'");
        }
        public Enumeration<PaymentNoticeStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<PaymentNoticeStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<PaymentNoticeStatus>(this, PaymentNoticeStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<PaymentNoticeStatus>(this, PaymentNoticeStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<PaymentNoticeStatus>(this, PaymentNoticeStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<PaymentNoticeStatus>(this, PaymentNoticeStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown PaymentNoticeStatus code '"+codeString+"'");
        }
    public String toCode(PaymentNoticeStatus code) {
      if (code == PaymentNoticeStatus.ACTIVE)
        return "active";
      if (code == PaymentNoticeStatus.CANCELLED)
        return "cancelled";
      if (code == PaymentNoticeStatus.DRAFT)
        return "draft";
      if (code == PaymentNoticeStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(PaymentNoticeStatus code) {
      return code.getSystem();
      }
    }

    /**
     * A unique identifier assigned to this payment notice.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier for the payment noctice", formalDefinition="A unique identifier assigned to this payment notice." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<PaymentNoticeStatus> status;

    /**
     * Reference of resource for which payment is being made.
     */
    @Child(name = "request", type = {Reference.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Request reference", formalDefinition="Reference of resource for which payment is being made." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Reference of resource for which payment is being made.)
     */
    protected Resource requestTarget;

    /**
     * Reference of response to resource for which payment is being made.
     */
    @Child(name = "response", type = {Reference.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Response reference", formalDefinition="Reference of response to resource for which payment is being made." )
    protected Reference response;

    /**
     * The actual object that is the target of the reference (Reference of response to resource for which payment is being made.)
     */
    protected Resource responseTarget;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "provider", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Resource providerTarget;

    /**
     * A reference to the payment which is the subject of this notice.
     */
    @Child(name = "payment", type = {PaymentReconciliation.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment reference", formalDefinition="A reference to the payment which is the subject of this notice." )
    protected Reference payment;

    /**
     * The actual object that is the target of the reference (A reference to the payment which is the subject of this notice.)
     */
    protected PaymentReconciliation paymentTarget;

    /**
     * The date when the above payment action occurred.
     */
    @Child(name = "paymentDate", type = {DateType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Payment or clearing date", formalDefinition="The date when the above payment action occurred." )
    protected DateType paymentDate;

    /**
     * The party who will receive or has received payment that is the subject of this notification.
     */
    @Child(name = "payee", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Party being paid", formalDefinition="The party who will receive or has received payment that is the subject of this notification." )
    protected Reference payee;

    /**
     * The actual object that is the target of the reference (The party who will receive or has received payment that is the subject of this notification.)
     */
    protected Resource payeeTarget;

    /**
     * The party who is notified of the payment status.
     */
    @Child(name = "recipient", type = {Organization.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Party being notified", formalDefinition="The party who is notified of the payment status." )
    protected Reference recipient;

    /**
     * The actual object that is the target of the reference (The party who is notified of the payment status.)
     */
    protected Organization recipientTarget;

    /**
     * The amount sent to the payee.
     */
    @Child(name = "amount", type = {Money.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Monetary amount of the payment", formalDefinition="The amount sent to the payee." )
    protected Money amount;

    /**
     * A code indicating whether payment has been sent or cleared.
     */
    @Child(name = "paymentStatus", type = {CodeableConcept.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Issued or cleared Status of the payment", formalDefinition="A code indicating whether payment has been sent or cleared." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payment-status")
    protected CodeableConcept paymentStatus;

    private static final long serialVersionUID = -545198613L;

  /**
   * Constructor
   */
    public PaymentNotice() {
      super();
    }

  /**
   * Constructor
   */
    public PaymentNotice(Enumeration<PaymentNoticeStatus> status, DateTimeType created, Reference payment, Reference recipient, Money amount) {
      super();
      this.status = status;
      this.created = created;
      this.payment = payment;
      this.recipient = recipient;
      this.amount = amount;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this payment notice.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PaymentNotice setIdentifier(List<Identifier> theIdentifier) { 
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

    public PaymentNotice addIdentifier(Identifier t) { //3
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
    public Enumeration<PaymentNoticeStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PaymentNoticeStatus>(new PaymentNoticeStatusEnumFactory()); // bb
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
    public PaymentNotice setStatusElement(Enumeration<PaymentNoticeStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public PaymentNoticeStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public PaymentNotice setStatus(PaymentNoticeStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PaymentNoticeStatus>(new PaymentNoticeStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #request} (Reference of resource for which payment is being made.)
     */
    public Reference getRequest() { 
      if (this.request == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.request");
        else if (Configuration.doAutoCreate())
          this.request = new Reference(); // cc
      return this.request;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Reference of resource for which payment is being made.)
     */
    public PaymentNotice setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference of resource for which payment is being made.)
     */
    public Resource getRequestTarget() { 
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference of resource for which payment is being made.)
     */
    public PaymentNotice setRequestTarget(Resource value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #response} (Reference of response to resource for which payment is being made.)
     */
    public Reference getResponse() { 
      if (this.response == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.response");
        else if (Configuration.doAutoCreate())
          this.response = new Reference(); // cc
      return this.response;
    }

    public boolean hasResponse() { 
      return this.response != null && !this.response.isEmpty();
    }

    /**
     * @param value {@link #response} (Reference of response to resource for which payment is being made.)
     */
    public PaymentNotice setResponse(Reference value) { 
      this.response = value;
      return this;
    }

    /**
     * @return {@link #response} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference of response to resource for which payment is being made.)
     */
    public Resource getResponseTarget() { 
      return this.responseTarget;
    }

    /**
     * @param value {@link #response} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference of response to resource for which payment is being made.)
     */
    public PaymentNotice setResponseTarget(Resource value) { 
      this.responseTarget = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.created");
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
     * @param value {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public PaymentNotice setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when this resource was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when this resource was created.
     */
    public PaymentNotice setCreated(Date value) { 
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.provider");
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
    public PaymentNotice setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Resource getProviderTarget() { 
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public PaymentNotice setProviderTarget(Resource value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #payment} (A reference to the payment which is the subject of this notice.)
     */
    public Reference getPayment() { 
      if (this.payment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.payment");
        else if (Configuration.doAutoCreate())
          this.payment = new Reference(); // cc
      return this.payment;
    }

    public boolean hasPayment() { 
      return this.payment != null && !this.payment.isEmpty();
    }

    /**
     * @param value {@link #payment} (A reference to the payment which is the subject of this notice.)
     */
    public PaymentNotice setPayment(Reference value) { 
      this.payment = value;
      return this;
    }

    /**
     * @return {@link #payment} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the payment which is the subject of this notice.)
     */
    public PaymentReconciliation getPaymentTarget() { 
      if (this.paymentTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.payment");
        else if (Configuration.doAutoCreate())
          this.paymentTarget = new PaymentReconciliation(); // aa
      return this.paymentTarget;
    }

    /**
     * @param value {@link #payment} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the payment which is the subject of this notice.)
     */
    public PaymentNotice setPaymentTarget(PaymentReconciliation value) { 
      this.paymentTarget = value;
      return this;
    }

    /**
     * @return {@link #paymentDate} (The date when the above payment action occurred.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
     */
    public DateType getPaymentDateElement() { 
      if (this.paymentDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.paymentDate");
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
     * @param value {@link #paymentDate} (The date when the above payment action occurred.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
     */
    public PaymentNotice setPaymentDateElement(DateType value) { 
      this.paymentDate = value;
      return this;
    }

    /**
     * @return The date when the above payment action occurred.
     */
    public Date getPaymentDate() { 
      return this.paymentDate == null ? null : this.paymentDate.getValue();
    }

    /**
     * @param value The date when the above payment action occurred.
     */
    public PaymentNotice setPaymentDate(Date value) { 
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
     * @return {@link #payee} (The party who will receive or has received payment that is the subject of this notification.)
     */
    public Reference getPayee() { 
      if (this.payee == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.payee");
        else if (Configuration.doAutoCreate())
          this.payee = new Reference(); // cc
      return this.payee;
    }

    public boolean hasPayee() { 
      return this.payee != null && !this.payee.isEmpty();
    }

    /**
     * @param value {@link #payee} (The party who will receive or has received payment that is the subject of this notification.)
     */
    public PaymentNotice setPayee(Reference value) { 
      this.payee = value;
      return this;
    }

    /**
     * @return {@link #payee} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party who will receive or has received payment that is the subject of this notification.)
     */
    public Resource getPayeeTarget() { 
      return this.payeeTarget;
    }

    /**
     * @param value {@link #payee} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party who will receive or has received payment that is the subject of this notification.)
     */
    public PaymentNotice setPayeeTarget(Resource value) { 
      this.payeeTarget = value;
      return this;
    }

    /**
     * @return {@link #recipient} (The party who is notified of the payment status.)
     */
    public Reference getRecipient() { 
      if (this.recipient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.recipient");
        else if (Configuration.doAutoCreate())
          this.recipient = new Reference(); // cc
      return this.recipient;
    }

    public boolean hasRecipient() { 
      return this.recipient != null && !this.recipient.isEmpty();
    }

    /**
     * @param value {@link #recipient} (The party who is notified of the payment status.)
     */
    public PaymentNotice setRecipient(Reference value) { 
      this.recipient = value;
      return this;
    }

    /**
     * @return {@link #recipient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party who is notified of the payment status.)
     */
    public Organization getRecipientTarget() { 
      if (this.recipientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.recipient");
        else if (Configuration.doAutoCreate())
          this.recipientTarget = new Organization(); // aa
      return this.recipientTarget;
    }

    /**
     * @param value {@link #recipient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party who is notified of the payment status.)
     */
    public PaymentNotice setRecipientTarget(Organization value) { 
      this.recipientTarget = value;
      return this;
    }

    /**
     * @return {@link #amount} (The amount sent to the payee.)
     */
    public Money getAmount() { 
      if (this.amount == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.amount");
        else if (Configuration.doAutoCreate())
          this.amount = new Money(); // cc
      return this.amount;
    }

    public boolean hasAmount() { 
      return this.amount != null && !this.amount.isEmpty();
    }

    /**
     * @param value {@link #amount} (The amount sent to the payee.)
     */
    public PaymentNotice setAmount(Money value) { 
      this.amount = value;
      return this;
    }

    /**
     * @return {@link #paymentStatus} (A code indicating whether payment has been sent or cleared.)
     */
    public CodeableConcept getPaymentStatus() { 
      if (this.paymentStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentNotice.paymentStatus");
        else if (Configuration.doAutoCreate())
          this.paymentStatus = new CodeableConcept(); // cc
      return this.paymentStatus;
    }

    public boolean hasPaymentStatus() { 
      return this.paymentStatus != null && !this.paymentStatus.isEmpty();
    }

    /**
     * @param value {@link #paymentStatus} (A code indicating whether payment has been sent or cleared.)
     */
    public PaymentNotice setPaymentStatus(CodeableConcept value) { 
      this.paymentStatus = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A unique identifier assigned to this payment notice.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The status of the resource instance.", 0, 1, status));
        children.add(new Property("request", "Reference(Any)", "Reference of resource for which payment is being made.", 0, 1, request));
        children.add(new Property("response", "Reference(Any)", "Reference of response to resource for which payment is being made.", 0, 1, response));
        children.add(new Property("created", "dateTime", "The date when this resource was created.", 0, 1, created));
        children.add(new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The practitioner who is responsible for the services rendered to the patient.", 0, 1, provider));
        children.add(new Property("payment", "Reference(PaymentReconciliation)", "A reference to the payment which is the subject of this notice.", 0, 1, payment));
        children.add(new Property("paymentDate", "date", "The date when the above payment action occurred.", 0, 1, paymentDate));
        children.add(new Property("payee", "Reference(Practitioner|PractitionerRole|Organization)", "The party who will receive or has received payment that is the subject of this notification.", 0, 1, payee));
        children.add(new Property("recipient", "Reference(Organization)", "The party who is notified of the payment status.", 0, 1, recipient));
        children.add(new Property("amount", "Money", "The amount sent to the payee.", 0, 1, amount));
        children.add(new Property("paymentStatus", "CodeableConcept", "A code indicating whether payment has been sent or cleared.", 0, 1, paymentStatus));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A unique identifier assigned to this payment notice.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the resource instance.", 0, 1, status);
        case 1095692943: /*request*/  return new Property("request", "Reference(Any)", "Reference of resource for which payment is being made.", 0, 1, request);
        case -340323263: /*response*/  return new Property("response", "Reference(Any)", "Reference of response to resource for which payment is being made.", 0, 1, response);
        case 1028554472: /*created*/  return new Property("created", "dateTime", "The date when this resource was created.", 0, 1, created);
        case -987494927: /*provider*/  return new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The practitioner who is responsible for the services rendered to the patient.", 0, 1, provider);
        case -786681338: /*payment*/  return new Property("payment", "Reference(PaymentReconciliation)", "A reference to the payment which is the subject of this notice.", 0, 1, payment);
        case -1540873516: /*paymentDate*/  return new Property("paymentDate", "date", "The date when the above payment action occurred.", 0, 1, paymentDate);
        case 106443592: /*payee*/  return new Property("payee", "Reference(Practitioner|PractitionerRole|Organization)", "The party who will receive or has received payment that is the subject of this notification.", 0, 1, payee);
        case 820081177: /*recipient*/  return new Property("recipient", "Reference(Organization)", "The party who is notified of the payment status.", 0, 1, recipient);
        case -1413853096: /*amount*/  return new Property("amount", "Money", "The amount sent to the payee.", 0, 1, amount);
        case 1430704536: /*paymentStatus*/  return new Property("paymentStatus", "CodeableConcept", "A code indicating whether payment has been sent or cleared.", 0, 1, paymentStatus);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PaymentNoticeStatus>
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // Reference
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case -786681338: /*payment*/ return this.payment == null ? new Base[0] : new Base[] {this.payment}; // Reference
        case -1540873516: /*paymentDate*/ return this.paymentDate == null ? new Base[0] : new Base[] {this.paymentDate}; // DateType
        case 106443592: /*payee*/ return this.payee == null ? new Base[0] : new Base[] {this.payee}; // Reference
        case 820081177: /*recipient*/ return this.recipient == null ? new Base[0] : new Base[] {this.recipient}; // Reference
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        case 1430704536: /*paymentStatus*/ return this.paymentStatus == null ? new Base[0] : new Base[] {this.paymentStatus}; // CodeableConcept
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
          value = new PaymentNoticeStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PaymentNoticeStatus>
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case -340323263: // response
          this.response = castToReference(value); // Reference
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case -987494927: // provider
          this.provider = castToReference(value); // Reference
          return value;
        case -786681338: // payment
          this.payment = castToReference(value); // Reference
          return value;
        case -1540873516: // paymentDate
          this.paymentDate = castToDate(value); // DateType
          return value;
        case 106443592: // payee
          this.payee = castToReference(value); // Reference
          return value;
        case 820081177: // recipient
          this.recipient = castToReference(value); // Reference
          return value;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          return value;
        case 1430704536: // paymentStatus
          this.paymentStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new PaymentNoticeStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PaymentNoticeStatus>
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("response")) {
          this.response = castToReference(value); // Reference
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("provider")) {
          this.provider = castToReference(value); // Reference
        } else if (name.equals("payment")) {
          this.payment = castToReference(value); // Reference
        } else if (name.equals("paymentDate")) {
          this.paymentDate = castToDate(value); // DateType
        } else if (name.equals("payee")) {
          this.payee = castToReference(value); // Reference
        } else if (name.equals("recipient")) {
          this.recipient = castToReference(value); // Reference
        } else if (name.equals("amount")) {
          this.amount = castToMoney(value); // Money
        } else if (name.equals("paymentStatus")) {
          this.paymentStatus = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 1095692943:  return getRequest(); 
        case -340323263:  return getResponse(); 
        case 1028554472:  return getCreatedElement();
        case -987494927:  return getProvider(); 
        case -786681338:  return getPayment(); 
        case -1540873516:  return getPaymentDateElement();
        case 106443592:  return getPayee(); 
        case 820081177:  return getRecipient(); 
        case -1413853096:  return getAmount(); 
        case 1430704536:  return getPaymentStatus(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -340323263: /*response*/ return new String[] {"Reference"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case -786681338: /*payment*/ return new String[] {"Reference"};
        case -1540873516: /*paymentDate*/ return new String[] {"date"};
        case 106443592: /*payee*/ return new String[] {"Reference"};
        case 820081177: /*recipient*/ return new String[] {"Reference"};
        case -1413853096: /*amount*/ return new String[] {"Money"};
        case 1430704536: /*paymentStatus*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentNotice.status");
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("response")) {
          this.response = new Reference();
          return this.response;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentNotice.created");
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("payment")) {
          this.payment = new Reference();
          return this.payment;
        }
        else if (name.equals("paymentDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentNotice.paymentDate");
        }
        else if (name.equals("payee")) {
          this.payee = new Reference();
          return this.payee;
        }
        else if (name.equals("recipient")) {
          this.recipient = new Reference();
          return this.recipient;
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else if (name.equals("paymentStatus")) {
          this.paymentStatus = new CodeableConcept();
          return this.paymentStatus;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "PaymentNotice";

  }

      public PaymentNotice copy() {
        PaymentNotice dst = new PaymentNotice();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.request = request == null ? null : request.copy();
        dst.response = response == null ? null : response.copy();
        dst.created = created == null ? null : created.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.payment = payment == null ? null : payment.copy();
        dst.paymentDate = paymentDate == null ? null : paymentDate.copy();
        dst.payee = payee == null ? null : payee.copy();
        dst.recipient = recipient == null ? null : recipient.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.paymentStatus = paymentStatus == null ? null : paymentStatus.copy();
        return dst;
      }

      protected PaymentNotice typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof PaymentNotice))
          return false;
        PaymentNotice o = (PaymentNotice) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(request, o.request, true)
           && compareDeep(response, o.response, true) && compareDeep(created, o.created, true) && compareDeep(provider, o.provider, true)
           && compareDeep(payment, o.payment, true) && compareDeep(paymentDate, o.paymentDate, true) && compareDeep(payee, o.payee, true)
           && compareDeep(recipient, o.recipient, true) && compareDeep(amount, o.amount, true) && compareDeep(paymentStatus, o.paymentStatus, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof PaymentNotice))
          return false;
        PaymentNotice o = (PaymentNotice) other_;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true) && compareValues(paymentDate, o.paymentDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, request
          , response, created, provider, payment, paymentDate, payee, recipient, amount
          , paymentStatus);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.PaymentNotice;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="PaymentNotice.identifier", description="The business identifier of the notice", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>request</b>
   * <p>
   * Description: <b>The Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.request</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request", path="PaymentNotice.request", description="The Claim", type="reference" )
  public static final String SP_REQUEST = "request";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request</b>
   * <p>
   * Description: <b>The Claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.request</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentNotice:request</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST = new ca.uhn.fhir.model.api.Include("PaymentNotice:request").toLocked();

 /**
   * Search parameter: <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provider", path="PaymentNotice.provider", description="The reference to the provider", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class, PractitionerRole.class } )
  public static final String SP_PROVIDER = "provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.provider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentNotice:provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDER = new ca.uhn.fhir.model.api.Include("PaymentNotice:provider").toLocked();

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>Creation date fro the notice</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PaymentNotice.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="PaymentNotice.created", description="Creation date fro the notice", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>Creation date fro the notice</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PaymentNotice.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>response</b>
   * <p>
   * Description: <b>The ClaimResponse</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.response</b><br>
   * </p>
   */
  @SearchParamDefinition(name="response", path="PaymentNotice.response", description="The ClaimResponse", type="reference" )
  public static final String SP_RESPONSE = "response";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>response</b>
   * <p>
   * Description: <b>The ClaimResponse</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentNotice.response</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESPONSE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESPONSE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentNotice:response</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESPONSE = new ca.uhn.fhir.model.api.Include("PaymentNotice:response").toLocked();

 /**
   * Search parameter: <b>payment-status</b>
   * <p>
   * Description: <b>The type of payment notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.paymentStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="payment-status", path="PaymentNotice.paymentStatus", description="The type of payment notice", type="token" )
  public static final String SP_PAYMENT_STATUS = "payment-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>payment-status</b>
   * <p>
   * Description: <b>The type of payment notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.paymentStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PAYMENT_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PAYMENT_STATUS);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the payment notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="PaymentNotice.status", description="The status of the payment notice", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the payment notice</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentNotice.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

