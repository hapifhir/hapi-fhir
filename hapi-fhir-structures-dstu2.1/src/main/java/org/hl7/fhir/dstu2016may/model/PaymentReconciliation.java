package org.hl7.fhir.dstu2016may.model;

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

import org.hl7.fhir.dstu2016may.model.Enumerations.RemittanceOutcome;
import org.hl7.fhir.dstu2016may.model.Enumerations.RemittanceOutcomeEnumFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * This resource provides payment details and claim references supporting a bulk payment.
 */
@ResourceDef(name="PaymentReconciliation", profile="http://hl7.org/fhir/Profile/PaymentReconciliation")
public class PaymentReconciliation extends DomainResource {

    @Block()
    public static class DetailsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code to indicate the nature of the payment, adjustment, funds advance, etc.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type code", formalDefinition="Code to indicate the nature of the payment, adjustment, funds advance, etc." )
        protected Coding type;

        /**
         * The claim or financial resource.
         */
        @Child(name = "request", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Claim", formalDefinition="The claim or financial resource." )
        protected Type request;

        /**
         * The claim response resource.
         */
        @Child(name = "responce", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Claim Response", formalDefinition="The claim response resource." )
        protected Type responce;

        /**
         * The Organization which submitted the invoice or financial transaction.
         */
        @Child(name = "submitter", type = {Identifier.class, Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Submitter", formalDefinition="The Organization which submitted the invoice or financial transaction." )
        protected Type submitter;

        /**
         * The organization which is receiving the payment.
         */
        @Child(name = "payee", type = {Identifier.class, Organization.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Payee", formalDefinition="The organization which is receiving the payment." )
        protected Type payee;

        /**
         * The date of the invoice or financial resource.
         */
        @Child(name = "date", type = {DateType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Invoice date", formalDefinition="The date of the invoice or financial resource." )
        protected DateType date;

        /**
         * Amount paid for this detail.
         */
        @Child(name = "amount", type = {Money.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Detail amount", formalDefinition="Amount paid for this detail." )
        protected Money amount;

        private static final long serialVersionUID = 1706468339L;

    /**
     * Constructor
     */
      public DetailsComponent() {
        super();
      }

    /**
     * Constructor
     */
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
        public Type getRequest() { 
          return this.request;
        }

        /**
         * @return {@link #request} (The claim or financial resource.)
         */
        public Identifier getRequestIdentifier() throws FHIRException { 
          if (!(this.request instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.request.getClass().getName()+" was encountered");
          return (Identifier) this.request;
        }

        public boolean hasRequestIdentifier() { 
          return this.request instanceof Identifier;
        }

        /**
         * @return {@link #request} (The claim or financial resource.)
         */
        public Reference getRequestReference() throws FHIRException { 
          if (!(this.request instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.request.getClass().getName()+" was encountered");
          return (Reference) this.request;
        }

        public boolean hasRequestReference() { 
          return this.request instanceof Reference;
        }

        public boolean hasRequest() { 
          return this.request != null && !this.request.isEmpty();
        }

        /**
         * @param value {@link #request} (The claim or financial resource.)
         */
        public DetailsComponent setRequest(Type value) { 
          this.request = value;
          return this;
        }

        /**
         * @return {@link #responce} (The claim response resource.)
         */
        public Type getResponce() { 
          return this.responce;
        }

        /**
         * @return {@link #responce} (The claim response resource.)
         */
        public Identifier getResponceIdentifier() throws FHIRException { 
          if (!(this.responce instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.responce.getClass().getName()+" was encountered");
          return (Identifier) this.responce;
        }

        public boolean hasResponceIdentifier() { 
          return this.responce instanceof Identifier;
        }

        /**
         * @return {@link #responce} (The claim response resource.)
         */
        public Reference getResponceReference() throws FHIRException { 
          if (!(this.responce instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.responce.getClass().getName()+" was encountered");
          return (Reference) this.responce;
        }

        public boolean hasResponceReference() { 
          return this.responce instanceof Reference;
        }

        public boolean hasResponce() { 
          return this.responce != null && !this.responce.isEmpty();
        }

        /**
         * @param value {@link #responce} (The claim response resource.)
         */
        public DetailsComponent setResponce(Type value) { 
          this.responce = value;
          return this;
        }

        /**
         * @return {@link #submitter} (The Organization which submitted the invoice or financial transaction.)
         */
        public Type getSubmitter() { 
          return this.submitter;
        }

        /**
         * @return {@link #submitter} (The Organization which submitted the invoice or financial transaction.)
         */
        public Identifier getSubmitterIdentifier() throws FHIRException { 
          if (!(this.submitter instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.submitter.getClass().getName()+" was encountered");
          return (Identifier) this.submitter;
        }

        public boolean hasSubmitterIdentifier() { 
          return this.submitter instanceof Identifier;
        }

        /**
         * @return {@link #submitter} (The Organization which submitted the invoice or financial transaction.)
         */
        public Reference getSubmitterReference() throws FHIRException { 
          if (!(this.submitter instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.submitter.getClass().getName()+" was encountered");
          return (Reference) this.submitter;
        }

        public boolean hasSubmitterReference() { 
          return this.submitter instanceof Reference;
        }

        public boolean hasSubmitter() { 
          return this.submitter != null && !this.submitter.isEmpty();
        }

        /**
         * @param value {@link #submitter} (The Organization which submitted the invoice or financial transaction.)
         */
        public DetailsComponent setSubmitter(Type value) { 
          this.submitter = value;
          return this;
        }

        /**
         * @return {@link #payee} (The organization which is receiving the payment.)
         */
        public Type getPayee() { 
          return this.payee;
        }

        /**
         * @return {@link #payee} (The organization which is receiving the payment.)
         */
        public Identifier getPayeeIdentifier() throws FHIRException { 
          if (!(this.payee instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.payee.getClass().getName()+" was encountered");
          return (Identifier) this.payee;
        }

        public boolean hasPayeeIdentifier() { 
          return this.payee instanceof Identifier;
        }

        /**
         * @return {@link #payee} (The organization which is receiving the payment.)
         */
        public Reference getPayeeReference() throws FHIRException { 
          if (!(this.payee instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.payee.getClass().getName()+" was encountered");
          return (Reference) this.payee;
        }

        public boolean hasPayeeReference() { 
          return this.payee instanceof Reference;
        }

        public boolean hasPayee() { 
          return this.payee != null && !this.payee.isEmpty();
        }

        /**
         * @param value {@link #payee} (The organization which is receiving the payment.)
         */
        public DetailsComponent setPayee(Type value) { 
          this.payee = value;
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
          childrenList.add(new Property("request[x]", "Identifier|Reference(Any)", "The claim or financial resource.", 0, java.lang.Integer.MAX_VALUE, request));
          childrenList.add(new Property("responce[x]", "Identifier|Reference(Any)", "The claim response resource.", 0, java.lang.Integer.MAX_VALUE, responce));
          childrenList.add(new Property("submitter[x]", "Identifier|Reference(Organization)", "The Organization which submitted the invoice or financial transaction.", 0, java.lang.Integer.MAX_VALUE, submitter));
          childrenList.add(new Property("payee[x]", "Identifier|Reference(Organization)", "The organization which is receiving the payment.", 0, java.lang.Integer.MAX_VALUE, payee));
          childrenList.add(new Property("date", "date", "The date of the invoice or financial resource.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("amount", "Money", "Amount paid for this detail.", 0, java.lang.Integer.MAX_VALUE, amount));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Type
        case -340323759: /*responce*/ return this.responce == null ? new Base[0] : new Base[] {this.responce}; // Type
        case 348678409: /*submitter*/ return this.submitter == null ? new Base[0] : new Base[] {this.submitter}; // Type
        case 106443592: /*payee*/ return this.payee == null ? new Base[0] : new Base[] {this.payee}; // Type
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 1095692943: // request
          this.request = (Type) value; // Type
          break;
        case -340323759: // responce
          this.responce = (Type) value; // Type
          break;
        case 348678409: // submitter
          this.submitter = (Type) value; // Type
          break;
        case 106443592: // payee
          this.payee = (Type) value; // Type
          break;
        case 3076014: // date
          this.date = castToDate(value); // DateType
          break;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("request[x]"))
          this.request = (Type) value; // Type
        else if (name.equals("responce[x]"))
          this.responce = (Type) value; // Type
        else if (name.equals("submitter[x]"))
          this.submitter = (Type) value; // Type
        else if (name.equals("payee[x]"))
          this.payee = (Type) value; // Type
        else if (name.equals("date"))
          this.date = castToDate(value); // DateType
        else if (name.equals("amount"))
          this.amount = castToMoney(value); // Money
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // Coding
        case 37106577:  return getRequest(); // Type
        case 1832772751:  return getResponce(); // Type
        case -2047315241:  return getSubmitter(); // Type
        case 1375276088:  return getPayee(); // Type
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateType
        case -1413853096:  return getAmount(); // Money
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("requestIdentifier")) {
          this.request = new Identifier();
          return this.request;
        }
        else if (name.equals("requestReference")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("responceIdentifier")) {
          this.responce = new Identifier();
          return this.responce;
        }
        else if (name.equals("responceReference")) {
          this.responce = new Reference();
          return this.responce;
        }
        else if (name.equals("submitterIdentifier")) {
          this.submitter = new Identifier();
          return this.submitter;
        }
        else if (name.equals("submitterReference")) {
          this.submitter = new Reference();
          return this.submitter;
        }
        else if (name.equals("payeeIdentifier")) {
          this.payee = new Identifier();
          return this.payee;
        }
        else if (name.equals("payeeReference")) {
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

  public String fhirType() {
    return "PaymentReconciliation.detail";

  }

  }

    @Block()
    public static class NotesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The note purpose: Print/Display.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The note purpose: Print/Display." )
        protected Coding type;

        /**
         * The note text.
         */
        @Child(name = "text", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Notes text", formalDefinition="The note text." )
        protected StringType text;

        private static final long serialVersionUID = 129959202L;

    /**
     * Constructor
     */
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
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
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("text"))
          this.text = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // Coding
        case 3556653: throw new FHIRException("Cannot make property text as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
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
        return super.isEmpty() && (type == null || type.isEmpty()) && (text == null || text.isEmpty())
          ;
      }

  public String fhirType() {
    return "PaymentReconciliation.note";

  }

  }

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {Identifier.class, ProcessRequest.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Claim reference", formalDefinition="Original request resource reference." )
    protected Type request;

    /**
     * Transaction status: error, complete.
     */
    @Child(name = "outcome", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="complete | error", formalDefinition="Transaction status: error, complete." )
    protected Enumeration<RemittanceOutcome> outcome;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    protected Coding originalRuleset;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The period of time for which payments have been gathered into this bulk payment for settlement.
     */
    @Child(name = "period", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period covered", formalDefinition="The period of time for which payments have been gathered into this bulk payment for settlement." )
    protected Period period;

    /**
     * The Insurer who produced this adjudicated response.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurer", formalDefinition="The Insurer who produced this adjudicated response." )
    protected Type organization;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Identifier.class, Practitioner.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Type requestProvider;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "requestOrganization", type = {Identifier.class, Organization.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Type requestOrganization;

    /**
     * List of individual settlement amounts and the corresponding transaction.
     */
    @Child(name = "detail", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Details", formalDefinition="List of individual settlement amounts and the corresponding transaction." )
    protected List<DetailsComponent> detail;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {Coding.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    protected Coding form;

    /**
     * Total payment amount.
     */
    @Child(name = "total", type = {Money.class}, order=13, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total amount of Payment", formalDefinition="Total payment amount." )
    protected Money total;

    /**
     * Suite of notes.
     */
    @Child(name = "note", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Note text", formalDefinition="Suite of notes." )
    protected List<NotesComponent> note;

    private static final long serialVersionUID = -293306995L;

  /**
   * Constructor
   */
    public PaymentReconciliation() {
      super();
    }

  /**
   * Constructor
   */
    public PaymentReconciliation(Money total) {
      super();
      this.total = total;
    }

    /**
     * @return {@link #identifier} (The Response business identifier.)
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
     * @return {@link #identifier} (The Response business identifier.)
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
    public PaymentReconciliation addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Type getRequest() { 
      return this.request;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Identifier getRequestIdentifier() throws FHIRException { 
      if (!(this.request instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.request.getClass().getName()+" was encountered");
      return (Identifier) this.request;
    }

    public boolean hasRequestIdentifier() { 
      return this.request instanceof Identifier;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Reference getRequestReference() throws FHIRException { 
      if (!(this.request instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.request.getClass().getName()+" was encountered");
      return (Reference) this.request;
    }

    public boolean hasRequestReference() { 
      return this.request instanceof Reference;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Original request resource reference.)
     */
    public PaymentReconciliation setRequest(Type value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #outcome} (Transaction status: error, complete.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
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
     * @param value {@link #outcome} (Transaction status: error, complete.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public PaymentReconciliation setOutcomeElement(Enumeration<RemittanceOutcome> value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return Transaction status: error, complete.
     */
    public RemittanceOutcome getOutcome() { 
      return this.outcome == null ? null : this.outcome.getValue();
    }

    /**
     * @param value Transaction status: error, complete.
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
    public Type getOrganization() { 
      return this.organization;
    }

    /**
     * @return {@link #organization} (The Insurer who produced this adjudicated response.)
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
     * @return {@link #organization} (The Insurer who produced this adjudicated response.)
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
     * @param value {@link #organization} (The Insurer who produced this adjudicated response.)
     */
    public PaymentReconciliation setOrganization(Type value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Type getRequestProvider() { 
      return this.requestProvider;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Identifier getRequestProviderIdentifier() throws FHIRException { 
      if (!(this.requestProvider instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.requestProvider.getClass().getName()+" was encountered");
      return (Identifier) this.requestProvider;
    }

    public boolean hasRequestProviderIdentifier() { 
      return this.requestProvider instanceof Identifier;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getRequestProviderReference() throws FHIRException { 
      if (!(this.requestProvider instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.requestProvider.getClass().getName()+" was encountered");
      return (Reference) this.requestProvider;
    }

    public boolean hasRequestProviderReference() { 
      return this.requestProvider instanceof Reference;
    }

    public boolean hasRequestProvider() { 
      return this.requestProvider != null && !this.requestProvider.isEmpty();
    }

    /**
     * @param value {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public PaymentReconciliation setRequestProvider(Type value) { 
      this.requestProvider = value;
      return this;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Type getRequestOrganization() { 
      return this.requestOrganization;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Identifier getRequestOrganizationIdentifier() throws FHIRException { 
      if (!(this.requestOrganization instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.requestOrganization.getClass().getName()+" was encountered");
      return (Identifier) this.requestOrganization;
    }

    public boolean hasRequestOrganizationIdentifier() { 
      return this.requestOrganization instanceof Identifier;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getRequestOrganizationReference() throws FHIRException { 
      if (!(this.requestOrganization instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.requestOrganization.getClass().getName()+" was encountered");
      return (Reference) this.requestOrganization;
    }

    public boolean hasRequestOrganizationReference() { 
      return this.requestOrganization instanceof Reference;
    }

    public boolean hasRequestOrganization() { 
      return this.requestOrganization != null && !this.requestOrganization.isEmpty();
    }

    /**
     * @param value {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public PaymentReconciliation setRequestOrganization(Type value) { 
      this.requestOrganization = value;
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

    // syntactic sugar
    public PaymentReconciliation addDetail(DetailsComponent t) { //3
      if (t == null)
        return this;
      if (this.detail == null)
        this.detail = new ArrayList<DetailsComponent>();
      this.detail.add(t);
      return this;
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

    // syntactic sugar
    public PaymentReconciliation addNote(NotesComponent t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<NotesComponent>();
      this.note.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("request[x]", "Identifier|Reference(ProcessRequest)", "Original request resource reference.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("outcome", "code", "Transaction status: error, complete.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("period", "Period", "The period of time for which payments have been gathered into this bulk payment for settlement.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The Insurer who produced this adjudicated response.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("requestProvider[x]", "Identifier|Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestProvider));
        childrenList.add(new Property("requestOrganization[x]", "Identifier|Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestOrganization));
        childrenList.add(new Property("detail", "", "List of individual settlement amounts and the corresponding transaction.", 0, java.lang.Integer.MAX_VALUE, detail));
        childrenList.add(new Property("form", "Coding", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("total", "Money", "Total payment amount.", 0, java.lang.Integer.MAX_VALUE, total));
        childrenList.add(new Property("note", "", "Suite of notes.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Type
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // Enumeration<RemittanceOutcome>
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case 1601527200: /*requestProvider*/ return this.requestProvider == null ? new Base[0] : new Base[] {this.requestProvider}; // Type
        case 599053666: /*requestOrganization*/ return this.requestOrganization == null ? new Base[0] : new Base[] {this.requestOrganization}; // Type
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // DetailsComponent
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // Coding
        case 110549828: /*total*/ return this.total == null ? new Base[0] : new Base[] {this.total}; // Money
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // NotesComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 1095692943: // request
          this.request = (Type) value; // Type
          break;
        case -1106507950: // outcome
          this.outcome = new RemittanceOutcomeEnumFactory().fromType(value); // Enumeration<RemittanceOutcome>
          break;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
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
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case 1178922291: // organization
          this.organization = (Type) value; // Type
          break;
        case 1601527200: // requestProvider
          this.requestProvider = (Type) value; // Type
          break;
        case 599053666: // requestOrganization
          this.requestOrganization = (Type) value; // Type
          break;
        case -1335224239: // detail
          this.getDetail().add((DetailsComponent) value); // DetailsComponent
          break;
        case 3148996: // form
          this.form = castToCoding(value); // Coding
          break;
        case 110549828: // total
          this.total = castToMoney(value); // Money
          break;
        case 3387378: // note
          this.getNote().add((NotesComponent) value); // NotesComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("request[x]"))
          this.request = (Type) value; // Type
        else if (name.equals("outcome"))
          this.outcome = new RemittanceOutcomeEnumFactory().fromType(value); // Enumeration<RemittanceOutcome>
        else if (name.equals("disposition"))
          this.disposition = castToString(value); // StringType
        else if (name.equals("ruleset"))
          this.ruleset = castToCoding(value); // Coding
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("organization[x]"))
          this.organization = (Type) value; // Type
        else if (name.equals("requestProvider[x]"))
          this.requestProvider = (Type) value; // Type
        else if (name.equals("requestOrganization[x]"))
          this.requestOrganization = (Type) value; // Type
        else if (name.equals("detail"))
          this.getDetail().add((DetailsComponent) value);
        else if (name.equals("form"))
          this.form = castToCoding(value); // Coding
        else if (name.equals("total"))
          this.total = castToMoney(value); // Money
        else if (name.equals("note"))
          this.getNote().add((NotesComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 37106577:  return getRequest(); // Type
        case -1106507950: throw new FHIRException("Cannot make property outcome as it is not a complex type"); // Enumeration<RemittanceOutcome>
        case 583380919: throw new FHIRException("Cannot make property disposition as it is not a complex type"); // StringType
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case -991726143:  return getPeriod(); // Period
        case 1326483053:  return getOrganization(); // Type
        case -1694784800:  return getRequestProvider(); // Type
        case 818740190:  return getRequestOrganization(); // Type
        case -1335224239:  return addDetail(); // DetailsComponent
        case 3148996:  return getForm(); // Coding
        case 110549828:  return getTotal(); // Money
        case 3387378:  return addNote(); // NotesComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("requestIdentifier")) {
          this.request = new Identifier();
          return this.request;
        }
        else if (name.equals("requestReference")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("outcome")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.outcome");
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.disposition");
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
          throw new FHIRException("Cannot call addChild on a primitive type PaymentReconciliation.created");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("organizationIdentifier")) {
          this.organization = new Identifier();
          return this.organization;
        }
        else if (name.equals("organizationReference")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("requestProviderIdentifier")) {
          this.requestProvider = new Identifier();
          return this.requestProvider;
        }
        else if (name.equals("requestProviderReference")) {
          this.requestProvider = new Reference();
          return this.requestProvider;
        }
        else if (name.equals("requestOrganizationIdentifier")) {
          this.requestOrganization = new Identifier();
          return this.requestOrganization;
        }
        else if (name.equals("requestOrganizationReference")) {
          this.requestOrganization = new Reference();
          return this.requestOrganization;
        }
        else if (name.equals("detail")) {
          return addDetail();
        }
        else if (name.equals("form")) {
          this.form = new Coding();
          return this.form;
        }
        else if (name.equals("total")) {
          this.total = new Money();
          return this.total;
        }
        else if (name.equals("note")) {
          return addNote();
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

 /**
   * Search parameter: <b>requestorganizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestOrganizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestorganizationreference", path="PaymentReconciliation.requestOrganization.as(Reference)", description="The organization who generated this resource", type="reference" )
  public static final String SP_REQUESTORGANIZATIONREFERENCE = "requestorganizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestorganizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestOrganizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:requestorganizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:requestorganizationreference").toLocked();

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
   * Search parameter: <b>requestorganizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.requestOrganizationIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestorganizationidentifier", path="PaymentReconciliation.requestOrganization.as(Identifier)", description="The organization who generated this resource", type="token" )
  public static final String SP_REQUESTORGANIZATIONIDENTIFIER = "requestorganizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestorganizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.requestOrganizationIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUESTORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUESTORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>requestprovideridentifier</b>
   * <p>
   * Description: <b>The reference to the provider who sumbitted the claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.requestProviderIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestprovideridentifier", path="PaymentReconciliation.requestProvider.as(Identifier)", description="The reference to the provider who sumbitted the claim", type="token" )
  public static final String SP_REQUESTPROVIDERIDENTIFIER = "requestprovideridentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestprovideridentifier</b>
   * <p>
   * Description: <b>The reference to the provider who sumbitted the claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.requestProviderIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUESTPROVIDERIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUESTPROVIDERIDENTIFIER);

 /**
   * Search parameter: <b>requestidentifier</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.requestIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestidentifier", path="PaymentReconciliation.request.as(Identifier)", description="The reference to the claim", type="token" )
  public static final String SP_REQUESTIDENTIFIER = "requestidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestidentifier</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.requestIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUESTIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUESTIDENTIFIER);

 /**
   * Search parameter: <b>requestreference</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestreference", path="PaymentReconciliation.request.as(Reference)", description="The reference to the claim", type="reference" )
  public static final String SP_REQUESTREFERENCE = "requestreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestreference</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:requestreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTREFERENCE = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:requestreference").toLocked();

 /**
   * Search parameter: <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.organizationIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationidentifier", path="PaymentReconciliation.organization.as(Identifier)", description="The organization who generated this resource", type="token" )
  public static final String SP_ORGANIZATIONIDENTIFIER = "organizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PaymentReconciliation.organizationIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>requestproviderreference</b>
   * <p>
   * Description: <b>The reference to the provider who sumbitted the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestProviderReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestproviderreference", path="PaymentReconciliation.requestProvider.as(Reference)", description="The reference to the provider who sumbitted the claim", type="reference" )
  public static final String SP_REQUESTPROVIDERREFERENCE = "requestproviderreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestproviderreference</b>
   * <p>
   * Description: <b>The reference to the provider who sumbitted the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.requestProviderReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTPROVIDERREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTPROVIDERREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:requestproviderreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTPROVIDERREFERENCE = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:requestproviderreference").toLocked();

 /**
   * Search parameter: <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.organizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationreference", path="PaymentReconciliation.organization.as(Reference)", description="The organization who generated this resource", type="reference" )
  public static final String SP_ORGANIZATIONREFERENCE = "organizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PaymentReconciliation.organizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PaymentReconciliation:organizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("PaymentReconciliation:organizationreference").toLocked();

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


}

