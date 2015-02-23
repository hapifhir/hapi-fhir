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

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * This resource provides payment details and claim references supporting a bulk payment.
 */
@ResourceDef(name="PaymentReconciliation", profile="http://hl7.org/fhir/Profile/PaymentReconciliation")
public class PaymentReconciliation extends DomainResource {

    public enum RSLink {
        /**
         * The processing completed without errors.
         */
        COMPLETE, 
        /**
         * The processing identified with errors.
         */
        ERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RSLink fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("error".equals(codeString))
          return ERROR;
        throw new Exception("Unknown RSLink code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLETE: return "complete";
            case ERROR: return "error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPLETE: return "";
            case ERROR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPLETE: return "The processing completed without errors.";
            case ERROR: return "The processing identified with errors.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLETE: return "complete";
            case ERROR: return "error";
            default: return "?";
          }
        }
    }

  public static class RSLinkEnumFactory implements EnumFactory<RSLink> {
    public RSLink fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return RSLink.COMPLETE;
        if ("error".equals(codeString))
          return RSLink.ERROR;
        throw new IllegalArgumentException("Unknown RSLink code '"+codeString+"'");
        }
    public String toCode(RSLink code) {
      if (code == RSLink.COMPLETE)
        return "complete";
      if (code == RSLink.ERROR)
        return "error";
      return "?";
      }
    }

    @Block()
    public static class DetailsComponent extends BackboneElement {
        /**
         * Code to indicate the nature of the payment, adjustment, funds advance, etc.
         */
        @Child(name="type", type={Coding.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Type code", formalDefinition="Code to indicate the nature of the payment, adjustment, funds advance, etc." )
        protected Coding type;

        /**
         * The claim or financial resource.
         */
        @Child(name="request", type={}, order=2, min=0, max=1)
        @Description(shortDefinition="Claim", formalDefinition="The claim or financial resource." )
        protected Reference request;

        /**
         * The actual object that is the target of the reference (The claim or financial resource.)
         */
        protected Resource requestTarget;

        /**
         * The claim response resource.
         */
        @Child(name="responce", type={}, order=3, min=0, max=1)
        @Description(shortDefinition="Claim Response", formalDefinition="The claim response resource." )
        protected Reference responce;

        /**
         * The actual object that is the target of the reference (The claim response resource.)
         */
        protected Resource responceTarget;

        /**
         * The Organization which submitted the invoice or financial transaction.
         */
        @Child(name="submitter", type={Organization.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Submitter", formalDefinition="The Organization which submitted the invoice or financial transaction." )
        protected Reference submitter;

        /**
         * The actual object that is the target of the reference (The Organization which submitted the invoice or financial transaction.)
         */
        protected Organization submitterTarget;

        /**
         * The organization which is receiving the payment.
         */
        @Child(name="payee", type={Organization.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Payee", formalDefinition="The organization which is receiving the payment." )
        protected Reference payee;

        /**
         * The actual object that is the target of the reference (The organization which is receiving the payment.)
         */
        protected Organization payeeTarget;

        /**
         * The date of the invoice or financial resource.
         */
        @Child(name="date", type={DateType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Invoice date", formalDefinition="The date of the invoice or financial resource." )
        protected DateType date;

        /**
         * Amount paid for this detail.
         */
        @Child(name="amount", type={Money.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Detail amount", formalDefinition="Amount paid for this detail." )
        protected Money amount;

        private static final long serialVersionUID = -1644048062L;

      public DetailsComponent() {
        super();
      }

      public DetailsComponent(Coding type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Code to indicate the nature of the payment, adjustment, funds advance, etc.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Code to indicate the nature of the payment, adjustment, funds advance, etc.)
         */
        public DetailsComponent setType(Coding value) { 
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
         * @return {@link #responce} (The claim response resource.)
         */
        public Reference getResponce() { 
          if (this.responce == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.responce");
            else if (Configuration.doAutoCreate())
              this.responce = new Reference(); // cc
          return this.responce;
        }

        public boolean hasResponce() { 
          return this.responce != null && !this.responce.isEmpty();
        }

        /**
         * @param value {@link #responce} (The claim response resource.)
         */
        public DetailsComponent setResponce(Reference value) { 
          this.responce = value;
          return this;
        }

        /**
         * @return {@link #responce} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The claim response resource.)
         */
        public Resource getResponceTarget() { 
          return this.responceTarget;
        }

        /**
         * @param value {@link #responce} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The claim response resource.)
         */
        public DetailsComponent setResponceTarget(Resource value) { 
          this.responceTarget = value;
          return this;
        }

        /**
         * @return {@link #submitter} (The Organization which submitted the invoice or financial transaction.)
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
         * @param value {@link #submitter} (The Organization which submitted the invoice or financial transaction.)
         */
        public DetailsComponent setSubmitter(Reference value) { 
          this.submitter = value;
          return this;
        }

        /**
         * @return {@link #submitter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Organization which submitted the invoice or financial transaction.)
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
         * @param value {@link #submitter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Organization which submitted the invoice or financial transaction.)
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
          childrenList.add(new Property("type", "Coding", "Code to indicate the nature of the payment, adjustment, funds advance, etc.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("request", "Reference(Any)", "The claim or financial resource.", 0, java.lang.Integer.MAX_VALUE, request));
          childrenList.add(new Property("responce", "Reference(Any)", "The claim response resource.", 0, java.lang.Integer.MAX_VALUE, responce));
          childrenList.add(new Property("submitter", "Reference(Organization)", "The Organization which submitted the invoice or financial transaction.", 0, java.lang.Integer.MAX_VALUE, submitter));
          childrenList.add(new Property("payee", "Reference(Organization)", "The organization which is receiving the payment.", 0, java.lang.Integer.MAX_VALUE, payee));
          childrenList.add(new Property("date", "date", "The date of the invoice or financial resource.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("amount", "Money", "Amount paid for this detail.", 0, java.lang.Integer.MAX_VALUE, amount));
        }

      public DetailsComponent copy() {
        DetailsComponent dst = new DetailsComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.request = request == null ? null : request.copy();
        dst.responce = responce == null ? null : responce.copy();
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
        return compareDeep(type, o.type, true) && compareDeep(request, o.request, true) && compareDeep(responce, o.responce, true)
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
        return super.isEmpty() && (type == null || type.isEmpty()) && (request == null || request.isEmpty())
           && (responce == null || responce.isEmpty()) && (submitter == null || submitter.isEmpty())
           && (payee == null || payee.isEmpty()) && (date == null || date.isEmpty()) && (amount == null || amount.isEmpty())
          ;
      }

  }

    @Block()
    public static class NotesComponent extends BackboneElement {
        /**
         * The note purpose: Print/Display.
         */
        @Child(name="type", type={Coding.class}, order=1, min=0, max=1)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The note purpose: Print/Display." )
        protected Coding type;

        /**
         * The note text.
         */
        @Child(name="text", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Notes text", formalDefinition="The note text." )
        protected StringType text;

        private static final long serialVersionUID = 129959202L;

      public NotesComponent() {
        super();
      }

        /**
         * @return {@link #type} (The note purpose: Print/Display.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NotesComponent.type");
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
        public NotesComponent setType(Coding value) { 
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
          childrenList.add(new Property("type", "Coding", "The note purpose: Print/Display.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("text", "string", "The note text.", 0, java.lang.Integer.MAX_VALUE, text));
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
        return super.isEmpty() && (type == null || type.isEmpty()) && (text == null || text.isEmpty())
          ;
      }

  }

    /**
     * The Response Business Identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response Business Identifier." )
    protected List<Identifier> identifier;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {PendedRequest.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Claim reference", formalDefinition="Original request resource reference." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Original request resource reference.)
     */
    protected PendedRequest requestTarget;

    /**
     * Transaction status: error, complete.
     */
    @Child(name = "outcome", type = {CodeType.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="complete | error", formalDefinition="Transaction status: error, complete." )
    protected Enumeration<RSLink> outcome;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    protected Coding originalRuleset;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The period of time for which payments have been gathered into this bulk payment for settlement.
     */
    @Child(name = "period", type = {Period.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="Period covered", formalDefinition="The period of time for which payments have been gathered into this bulk payment for settlement." )
    protected Period period;

    /**
     * The Insurer who produced this adjudicated response.
     */
    @Child(name = "organization", type = {Organization.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Insurer", formalDefinition="The Insurer who produced this adjudicated response." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The Insurer who produced this adjudicated response.)
     */
    protected Organization organizationTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Practitioner.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference requestProvider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner requestProviderTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "requestOrganization", type = {Organization.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference requestOrganization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization requestOrganizationTarget;

    /**
     * List of individual settlement amounts and the corresponding transaction.
     */
    @Child(name = "detail", type = {}, order = 11, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Details", formalDefinition="List of individual settlement amounts and the corresponding transaction." )
    protected List<DetailsComponent> detail;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {Coding.class}, order = 12, min = 0, max = 1)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    protected Coding form;

    /**
     * Total payment amount.
     */
    @Child(name = "total", type = {Money.class}, order = 13, min = 1, max = 1)
    @Description(shortDefinition="Total amount of Payment", formalDefinition="Total payment amount." )
    protected Money total;

    /**
     * Suite of notes.
     */
    @Child(name = "note", type = {}, order = 14, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Note text", formalDefinition="Suite of notes." )
    protected List<NotesComponent> note;

    private static final long serialVersionUID = 2043460985L;

    public PaymentReconciliation() {
      super();
    }

    public PaymentReconciliation(Money total) {
      super();
      this.total = total;
    }

    /**
     * @return {@link #identifier} (The Response Business Identifier.)
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
     * @return {@link #identifier} (The Response Business Identifier.)
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
    public PendedRequest getRequestTarget() { 
      if (this.requestTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.request");
        else if (Configuration.doAutoCreate())
          this.requestTarget = new PendedRequest(); // aa
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public PaymentReconciliation setRequestTarget(PendedRequest value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #outcome} (Transaction status: error, complete.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public Enumeration<RSLink> getOutcomeElement() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new Enumeration<RSLink>(new RSLinkEnumFactory()); // bb
      return this.outcome;
    }

    public boolean hasOutcomeElement() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (Transaction status: error, complete.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public PaymentReconciliation setOutcomeElement(Enumeration<RSLink> value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return Transaction status: error, complete.
     */
    public RSLink getOutcome() { 
      return this.outcome == null ? null : this.outcome.getValue();
    }

    /**
     * @param value Transaction status: error, complete.
     */
    public PaymentReconciliation setOutcome(RSLink value) { 
      if (value == null)
        this.outcome = null;
      else {
        if (this.outcome == null)
          this.outcome = new Enumeration<RSLink>(new RSLinkEnumFactory());
        this.outcome.setValue(value);
      }
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
     * @return {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.ruleset");
        else if (Configuration.doAutoCreate())
          this.ruleset = new Coding(); // cc
      return this.ruleset;
    }

    public boolean hasRuleset() { 
      return this.ruleset != null && !this.ruleset.isEmpty();
    }

    /**
     * @param value {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public PaymentReconciliation setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.originalRuleset");
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
    public PaymentReconciliation setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
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

    public boolean hasDetail() { 
      if (this.detail == null)
        return false;
      for (DetailsComponent item : this.detail)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #detail} (List of individual settlement amounts and the corresponding transaction.)
     */
    // syntactic sugar
    public DetailsComponent addDetail() { //3
      DetailsComponent t = new DetailsComponent();
      if (this.detail == null)
        this.detail = new ArrayList<DetailsComponent>();
      this.detail.add(t);
      return t;
    }

    /**
     * @return {@link #form} (The form to be used for printing the content.)
     */
    public Coding getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PaymentReconciliation.form");
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
    public PaymentReconciliation setForm(Coding value) { 
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
     * @return {@link #note} (Suite of notes.)
     */
    public List<NotesComponent> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<NotesComponent>();
      return this.note;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (NotesComponent item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #note} (Suite of notes.)
     */
    // syntactic sugar
    public NotesComponent addNote() { //3
      NotesComponent t = new NotesComponent();
      if (this.note == null)
        this.note = new ArrayList<NotesComponent>();
      this.note.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response Business Identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("request", "Reference(PendedRequest)", "Original request resource reference.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("outcome", "code", "Transaction status: error, complete.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("period", "Period", "The period of time for which payments have been gathered into this bulk payment for settlement.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("organization", "Reference(Organization)", "The Insurer who produced this adjudicated response.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("requestProvider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestProvider));
        childrenList.add(new Property("requestOrganization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestOrganization));
        childrenList.add(new Property("detail", "", "List of individual settlement amounts and the corresponding transaction.", 0, java.lang.Integer.MAX_VALUE, detail));
        childrenList.add(new Property("form", "Coding", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("total", "Money", "Total payment amount.", 0, java.lang.Integer.MAX_VALUE, total));
        childrenList.add(new Property("note", "", "Suite of notes.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      public PaymentReconciliation copy() {
        PaymentReconciliation dst = new PaymentReconciliation();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.request = request == null ? null : request.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.period = period == null ? null : period.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.requestProvider = requestProvider == null ? null : requestProvider.copy();
        dst.requestOrganization = requestOrganization == null ? null : requestOrganization.copy();
        if (detail != null) {
          dst.detail = new ArrayList<DetailsComponent>();
          for (DetailsComponent i : detail)
            dst.detail.add(i.copy());
        };
        dst.form = form == null ? null : form.copy();
        dst.total = total == null ? null : total.copy();
        if (note != null) {
          dst.note = new ArrayList<NotesComponent>();
          for (NotesComponent i : note)
            dst.note.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(request, o.request, true) && compareDeep(outcome, o.outcome, true)
           && compareDeep(disposition, o.disposition, true) && compareDeep(ruleset, o.ruleset, true) && compareDeep(originalRuleset, o.originalRuleset, true)
           && compareDeep(created, o.created, true) && compareDeep(period, o.period, true) && compareDeep(organization, o.organization, true)
           && compareDeep(requestProvider, o.requestProvider, true) && compareDeep(requestOrganization, o.requestOrganization, true)
           && compareDeep(detail, o.detail, true) && compareDeep(form, o.form, true) && compareDeep(total, o.total, true)
           && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PaymentReconciliation))
          return false;
        PaymentReconciliation o = (PaymentReconciliation) other;
        return compareValues(outcome, o.outcome, true) && compareValues(disposition, o.disposition, true) && compareValues(created, o.created, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (request == null || request.isEmpty())
           && (outcome == null || outcome.isEmpty()) && (disposition == null || disposition.isEmpty())
           && (ruleset == null || ruleset.isEmpty()) && (originalRuleset == null || originalRuleset.isEmpty())
           && (created == null || created.isEmpty()) && (period == null || period.isEmpty()) && (organization == null || organization.isEmpty())
           && (requestProvider == null || requestProvider.isEmpty()) && (requestOrganization == null || requestOrganization.isEmpty())
           && (detail == null || detail.isEmpty()) && (form == null || form.isEmpty()) && (total == null || total.isEmpty())
           && (note == null || note.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.PaymentReconciliation;
   }

  @SearchParamDefinition(name="identifier", path="PaymentReconciliation.identifier", description="The business identifier of the Explanation of Benefit", type="token" )
  public static final String SP_IDENTIFIER = "identifier";

}

