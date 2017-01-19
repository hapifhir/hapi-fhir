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
 * This resource provides the adjudication details from the processing of a Claim resource.
 */
@ResourceDef(name="ClaimResponse", profile="http://hl7.org/fhir/Profile/ClaimResponse")
public class ClaimResponse extends DomainResource {

    @Block()
    public static class ItemsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line number.
         */
        @Child(name = "sequenceLinkId", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType sequenceLinkId;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication details", formalDefinition="The adjudications results." )
        protected List<ItemAdjudicationComponent> adjudication;

        /**
         * The second tier service adjudications for submitted services.
         */
        @Child(name = "detail", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Detail line items", formalDefinition="The second tier service adjudications for submitted services." )
        protected List<ItemDetailComponent> detail;

        private static final long serialVersionUID = -1917866697L;

    /**
     * Constructor
     */
      public ItemsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemsComponent(PositiveIntType sequenceLinkId) {
        super();
        this.sequenceLinkId = sequenceLinkId;
      }

        /**
         * @return {@link #sequenceLinkId} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public PositiveIntType getSequenceLinkIdElement() { 
          if (this.sequenceLinkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.sequenceLinkId");
            else if (Configuration.doAutoCreate())
              this.sequenceLinkId = new PositiveIntType(); // bb
          return this.sequenceLinkId;
        }

        public boolean hasSequenceLinkIdElement() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        public boolean hasSequenceLinkId() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        /**
         * @param value {@link #sequenceLinkId} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public ItemsComponent setSequenceLinkIdElement(PositiveIntType value) { 
          this.sequenceLinkId = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequenceLinkId() { 
          return this.sequenceLinkId == null || this.sequenceLinkId.isEmpty() ? 0 : this.sequenceLinkId.getValue();
        }

        /**
         * @param value A service line number.
         */
        public ItemsComponent setSequenceLinkId(int value) { 
            if (this.sequenceLinkId == null)
              this.sequenceLinkId = new PositiveIntType();
            this.sequenceLinkId.setValue(value);
          return this;
        }

        /**
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
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
    // syntactic sugar
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
        public ItemsComponent addNoteNumber(int value) { //1
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
        public List<ItemAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<ItemAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (ItemAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public ItemAdjudicationComponent addAdjudication() { //3
          ItemAdjudicationComponent t = new ItemAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<ItemAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public ItemsComponent addAdjudication(ItemAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<ItemAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return {@link #detail} (The second tier service adjudications for submitted services.)
         */
        public List<ItemDetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<ItemDetailComponent>();
          return this.detail;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (ItemDetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detail} (The second tier service adjudications for submitted services.)
         */
    // syntactic sugar
        public ItemDetailComponent addDetail() { //3
          ItemDetailComponent t = new ItemDetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<ItemDetailComponent>();
          this.detail.add(t);
          return t;
        }

    // syntactic sugar
        public ItemsComponent addDetail(ItemDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<ItemDetailComponent>();
          this.detail.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequenceLinkId", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequenceLinkId));
          childrenList.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("detail", "", "The second tier service adjudications for submitted services.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return this.sequenceLinkId == null ? new Base[0] : new Base[] {this.sequenceLinkId}; // PositiveIntType
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // ItemAdjudicationComponent
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // ItemDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422298666: // sequenceLinkId
          this.sequenceLinkId = castToPositiveInt(value); // PositiveIntType
          break;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          break;
        case -231349275: // adjudication
          this.getAdjudication().add((ItemAdjudicationComponent) value); // ItemAdjudicationComponent
          break;
        case -1335224239: // detail
          this.getDetail().add((ItemDetailComponent) value); // ItemDetailComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequenceLinkId"))
          this.sequenceLinkId = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("noteNumber"))
          this.getNoteNumber().add(castToPositiveInt(value));
        else if (name.equals("adjudication"))
          this.getAdjudication().add((ItemAdjudicationComponent) value);
        else if (name.equals("detail"))
          this.getDetail().add((ItemDetailComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666: throw new FHIRException("Cannot make property sequenceLinkId as it is not a complex type"); // PositiveIntType
        case -1110033957: throw new FHIRException("Cannot make property noteNumber as it is not a complex type"); // PositiveIntType
        case -231349275:  return addAdjudication(); // ItemAdjudicationComponent
        case -1335224239:  return addDetail(); // ItemDetailComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.sequenceLinkId");
        }
        else if (name.equals("noteNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.noteNumber");
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

      public ItemsComponent copy() {
        ItemsComponent dst = new ItemsComponent();
        copyValues(dst);
        dst.sequenceLinkId = sequenceLinkId == null ? null : sequenceLinkId.copy();
        if (noteNumber != null) {
          dst.noteNumber = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : noteNumber)
            dst.noteNumber.add(i.copy());
        };
        if (adjudication != null) {
          dst.adjudication = new ArrayList<ItemAdjudicationComponent>();
          for (ItemAdjudicationComponent i : adjudication)
            dst.adjudication.add(i.copy());
        };
        if (detail != null) {
          dst.detail = new ArrayList<ItemDetailComponent>();
          for (ItemDetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ItemsComponent))
          return false;
        ItemsComponent o = (ItemsComponent) other;
        return compareDeep(sequenceLinkId, o.sequenceLinkId, true) && compareDeep(noteNumber, o.noteNumber, true)
           && compareDeep(adjudication, o.adjudication, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ItemsComponent))
          return false;
        ItemsComponent o = (ItemsComponent) other;
        return compareValues(sequenceLinkId, o.sequenceLinkId, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequenceLinkId == null || sequenceLinkId.isEmpty()) && (noteNumber == null || noteNumber.isEmpty())
           && (adjudication == null || adjudication.isEmpty()) && (detail == null || detail.isEmpty())
          ;
      }

  public String fhirType() {
    return "ClaimResponse.item";

  }

  }

    @Block()
    public static class ItemAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.
         */
        @Child(name = "category", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc." )
        protected Coding category;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication reason", formalDefinition="Adjudication reason such as limit reached." )
        protected Coding reason;

        /**
         * Monetary amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monetary amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monetary value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = -1926987562L;

    /**
     * Constructor
     */
      public ItemAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemAdjudicationComponent(Coding category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public Coding getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemAdjudicationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Coding(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public ItemAdjudicationComponent setCategory(Coding value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemAdjudicationComponent.reason");
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
        public ItemAdjudicationComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monetary amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monetary amount associated with the code.)
         */
        public ItemAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemAdjudicationComponent.value");
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
        public ItemAdjudicationComponent setValueElement(DecimalType value) { 
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
        public ItemAdjudicationComponent setValue(BigDecimal value) { 
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
        public ItemAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public ItemAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "Coding", "Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("reason", "Coding", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monetary amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
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
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.value");
        }
        else
          return super.addChild(name);
      }

      public ItemAdjudicationComponent copy() {
        ItemAdjudicationComponent dst = new ItemAdjudicationComponent();
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
        if (!(other instanceof ItemAdjudicationComponent))
          return false;
        ItemAdjudicationComponent o = (ItemAdjudicationComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ItemAdjudicationComponent))
          return false;
        ItemAdjudicationComponent o = (ItemAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (reason == null || reason.isEmpty())
           && (amount == null || amount.isEmpty()) && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ClaimResponse.item.adjudication";

  }

  }

    @Block()
    public static class ItemDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line number.
         */
        @Child(name = "sequenceLinkId", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType sequenceLinkId;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Detail adjudication", formalDefinition="The adjudications results." )
        protected List<DetailAdjudicationComponent> adjudication;

        /**
         * The third tier service adjudications for submitted services.
         */
        @Child(name = "subDetail", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Subdetail line items", formalDefinition="The third tier service adjudications for submitted services." )
        protected List<SubDetailComponent> subDetail;

        private static final long serialVersionUID = -1751018357L;

    /**
     * Constructor
     */
      public ItemDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemDetailComponent(PositiveIntType sequenceLinkId) {
        super();
        this.sequenceLinkId = sequenceLinkId;
      }

        /**
         * @return {@link #sequenceLinkId} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public PositiveIntType getSequenceLinkIdElement() { 
          if (this.sequenceLinkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemDetailComponent.sequenceLinkId");
            else if (Configuration.doAutoCreate())
              this.sequenceLinkId = new PositiveIntType(); // bb
          return this.sequenceLinkId;
        }

        public boolean hasSequenceLinkIdElement() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        public boolean hasSequenceLinkId() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        /**
         * @param value {@link #sequenceLinkId} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public ItemDetailComponent setSequenceLinkIdElement(PositiveIntType value) { 
          this.sequenceLinkId = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequenceLinkId() { 
          return this.sequenceLinkId == null || this.sequenceLinkId.isEmpty() ? 0 : this.sequenceLinkId.getValue();
        }

        /**
         * @param value A service line number.
         */
        public ItemDetailComponent setSequenceLinkId(int value) { 
            if (this.sequenceLinkId == null)
              this.sequenceLinkId = new PositiveIntType();
            this.sequenceLinkId.setValue(value);
          return this;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
        public List<DetailAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<DetailAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (DetailAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public DetailAdjudicationComponent addAdjudication() { //3
          DetailAdjudicationComponent t = new DetailAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<DetailAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public ItemDetailComponent addAdjudication(DetailAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<DetailAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return {@link #subDetail} (The third tier service adjudications for submitted services.)
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
         * @return {@link #subDetail} (The third tier service adjudications for submitted services.)
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
        public ItemDetailComponent addSubDetail(SubDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          this.subDetail.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequenceLinkId", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequenceLinkId));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("subDetail", "", "The third tier service adjudications for submitted services.", 0, java.lang.Integer.MAX_VALUE, subDetail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return this.sequenceLinkId == null ? new Base[0] : new Base[] {this.sequenceLinkId}; // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // DetailAdjudicationComponent
        case -828829007: /*subDetail*/ return this.subDetail == null ? new Base[0] : this.subDetail.toArray(new Base[this.subDetail.size()]); // SubDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422298666: // sequenceLinkId
          this.sequenceLinkId = castToPositiveInt(value); // PositiveIntType
          break;
        case -231349275: // adjudication
          this.getAdjudication().add((DetailAdjudicationComponent) value); // DetailAdjudicationComponent
          break;
        case -828829007: // subDetail
          this.getSubDetail().add((SubDetailComponent) value); // SubDetailComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequenceLinkId"))
          this.sequenceLinkId = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("adjudication"))
          this.getAdjudication().add((DetailAdjudicationComponent) value);
        else if (name.equals("subDetail"))
          this.getSubDetail().add((SubDetailComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666: throw new FHIRException("Cannot make property sequenceLinkId as it is not a complex type"); // PositiveIntType
        case -231349275:  return addAdjudication(); // DetailAdjudicationComponent
        case -828829007:  return addSubDetail(); // SubDetailComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.sequenceLinkId");
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

      public ItemDetailComponent copy() {
        ItemDetailComponent dst = new ItemDetailComponent();
        copyValues(dst);
        dst.sequenceLinkId = sequenceLinkId == null ? null : sequenceLinkId.copy();
        if (adjudication != null) {
          dst.adjudication = new ArrayList<DetailAdjudicationComponent>();
          for (DetailAdjudicationComponent i : adjudication)
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
        if (!(other instanceof ItemDetailComponent))
          return false;
        ItemDetailComponent o = (ItemDetailComponent) other;
        return compareDeep(sequenceLinkId, o.sequenceLinkId, true) && compareDeep(adjudication, o.adjudication, true)
           && compareDeep(subDetail, o.subDetail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ItemDetailComponent))
          return false;
        ItemDetailComponent o = (ItemDetailComponent) other;
        return compareValues(sequenceLinkId, o.sequenceLinkId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequenceLinkId == null || sequenceLinkId.isEmpty()) && (adjudication == null || adjudication.isEmpty())
           && (subDetail == null || subDetail.isEmpty());
      }

  public String fhirType() {
    return "ClaimResponse.item.detail";

  }

  }

    @Block()
    public static class DetailAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.
         */
        @Child(name = "category", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc." )
        protected Coding category;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication reason", formalDefinition="Adjudication reason such as limit reached." )
        protected Coding reason;

        /**
         * Monetary amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monetary amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monetary value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = -1926987562L;

    /**
     * Constructor
     */
      public DetailAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DetailAdjudicationComponent(Coding category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public Coding getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailAdjudicationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Coding(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public DetailAdjudicationComponent setCategory(Coding value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailAdjudicationComponent.reason");
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
        public DetailAdjudicationComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monetary amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monetary amount associated with the code.)
         */
        public DetailAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailAdjudicationComponent.value");
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
        public DetailAdjudicationComponent setValueElement(DecimalType value) { 
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
        public DetailAdjudicationComponent setValue(BigDecimal value) { 
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
        public DetailAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public DetailAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "Coding", "Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("reason", "Coding", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monetary amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
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
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.value");
        }
        else
          return super.addChild(name);
      }

      public DetailAdjudicationComponent copy() {
        DetailAdjudicationComponent dst = new DetailAdjudicationComponent();
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
        if (!(other instanceof DetailAdjudicationComponent))
          return false;
        DetailAdjudicationComponent o = (DetailAdjudicationComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DetailAdjudicationComponent))
          return false;
        DetailAdjudicationComponent o = (DetailAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (reason == null || reason.isEmpty())
           && (amount == null || amount.isEmpty()) && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ClaimResponse.item.detail.adjudication";

  }

  }

    @Block()
    public static class SubDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A service line number.
         */
        @Child(name = "sequenceLinkId", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType sequenceLinkId;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Subdetail adjudication", formalDefinition="The adjudications results." )
        protected List<SubdetailAdjudicationComponent> adjudication;

        private static final long serialVersionUID = 1780202110L;

    /**
     * Constructor
     */
      public SubDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubDetailComponent(PositiveIntType sequenceLinkId) {
        super();
        this.sequenceLinkId = sequenceLinkId;
      }

        /**
         * @return {@link #sequenceLinkId} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public PositiveIntType getSequenceLinkIdElement() { 
          if (this.sequenceLinkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.sequenceLinkId");
            else if (Configuration.doAutoCreate())
              this.sequenceLinkId = new PositiveIntType(); // bb
          return this.sequenceLinkId;
        }

        public boolean hasSequenceLinkIdElement() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        public boolean hasSequenceLinkId() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        /**
         * @param value {@link #sequenceLinkId} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public SubDetailComponent setSequenceLinkIdElement(PositiveIntType value) { 
          this.sequenceLinkId = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequenceLinkId() { 
          return this.sequenceLinkId == null || this.sequenceLinkId.isEmpty() ? 0 : this.sequenceLinkId.getValue();
        }

        /**
         * @param value A service line number.
         */
        public SubDetailComponent setSequenceLinkId(int value) { 
            if (this.sequenceLinkId == null)
              this.sequenceLinkId = new PositiveIntType();
            this.sequenceLinkId.setValue(value);
          return this;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
        public List<SubdetailAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<SubdetailAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (SubdetailAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public SubdetailAdjudicationComponent addAdjudication() { //3
          SubdetailAdjudicationComponent t = new SubdetailAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<SubdetailAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public SubDetailComponent addAdjudication(SubdetailAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<SubdetailAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequenceLinkId", "positiveInt", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequenceLinkId));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return this.sequenceLinkId == null ? new Base[0] : new Base[] {this.sequenceLinkId}; // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // SubdetailAdjudicationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422298666: // sequenceLinkId
          this.sequenceLinkId = castToPositiveInt(value); // PositiveIntType
          break;
        case -231349275: // adjudication
          this.getAdjudication().add((SubdetailAdjudicationComponent) value); // SubdetailAdjudicationComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequenceLinkId"))
          this.sequenceLinkId = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("adjudication"))
          this.getAdjudication().add((SubdetailAdjudicationComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666: throw new FHIRException("Cannot make property sequenceLinkId as it is not a complex type"); // PositiveIntType
        case -231349275:  return addAdjudication(); // SubdetailAdjudicationComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.sequenceLinkId");
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
        dst.sequenceLinkId = sequenceLinkId == null ? null : sequenceLinkId.copy();
        if (adjudication != null) {
          dst.adjudication = new ArrayList<SubdetailAdjudicationComponent>();
          for (SubdetailAdjudicationComponent i : adjudication)
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
        return compareDeep(sequenceLinkId, o.sequenceLinkId, true) && compareDeep(adjudication, o.adjudication, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SubDetailComponent))
          return false;
        SubDetailComponent o = (SubDetailComponent) other;
        return compareValues(sequenceLinkId, o.sequenceLinkId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequenceLinkId == null || sequenceLinkId.isEmpty()) && (adjudication == null || adjudication.isEmpty())
          ;
      }

  public String fhirType() {
    return "ClaimResponse.item.detail.subDetail";

  }

  }

    @Block()
    public static class SubdetailAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.
         */
        @Child(name = "category", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc." )
        protected Coding category;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication reason", formalDefinition="Adjudication reason such as limit reached." )
        protected Coding reason;

        /**
         * Monetary amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monetary amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monetary value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = -1926987562L;

    /**
     * Constructor
     */
      public SubdetailAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubdetailAdjudicationComponent(Coding category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public Coding getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubdetailAdjudicationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Coding(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public SubdetailAdjudicationComponent setCategory(Coding value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubdetailAdjudicationComponent.reason");
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
        public SubdetailAdjudicationComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monetary amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubdetailAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monetary amount associated with the code.)
         */
        public SubdetailAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubdetailAdjudicationComponent.value");
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
        public SubdetailAdjudicationComponent setValueElement(DecimalType value) { 
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
        public SubdetailAdjudicationComponent setValue(BigDecimal value) { 
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
        public SubdetailAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public SubdetailAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "Coding", "Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("reason", "Coding", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monetary amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
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
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.value");
        }
        else
          return super.addChild(name);
      }

      public SubdetailAdjudicationComponent copy() {
        SubdetailAdjudicationComponent dst = new SubdetailAdjudicationComponent();
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
        if (!(other instanceof SubdetailAdjudicationComponent))
          return false;
        SubdetailAdjudicationComponent o = (SubdetailAdjudicationComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SubdetailAdjudicationComponent))
          return false;
        SubdetailAdjudicationComponent o = (SubdetailAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (reason == null || reason.isEmpty())
           && (amount == null || amount.isEmpty()) && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ClaimResponse.item.detail.subDetail.adjudication";

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
         * The fee charged for the professional service or product..
         */
        @Child(name = "fee", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Professional fee or Product charge", formalDefinition="The fee charged for the professional service or product.." )
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
        @Child(name = "adjudication", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Added items adjudication", formalDefinition="The adjudications results." )
        protected List<AddedItemAdjudicationComponent> adjudication;

        /**
         * The second tier service adjudications for payor added services.
         */
        @Child(name = "detail", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Added items details", formalDefinition="The second tier service adjudications for payor added services." )
        protected List<AddedItemsDetailComponent> detail;

        private static final long serialVersionUID = -1675935854L;

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
    // syntactic sugar
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
         * @return {@link #fee} (The fee charged for the professional service or product..)
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
         * @param value {@link #fee} (The fee charged for the professional service or product..)
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
    // syntactic sugar
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
        public List<AddedItemAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (AddedItemAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public AddedItemAdjudicationComponent addAdjudication() { //3
          AddedItemAdjudicationComponent t = new AddedItemAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public AddedItemComponent addAdjudication(AddedItemAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        /**
         * @return {@link #detail} (The second tier service adjudications for payor added services.)
         */
        public List<AddedItemsDetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemsDetailComponent>();
          return this.detail;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (AddedItemsDetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detail} (The second tier service adjudications for payor added services.)
         */
    // syntactic sugar
        public AddedItemsDetailComponent addDetail() { //3
          AddedItemsDetailComponent t = new AddedItemsDetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemsDetailComponent>();
          this.detail.add(t);
          return t;
        }

    // syntactic sugar
        public AddedItemComponent addDetail(AddedItemsDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemsDetailComponent>();
          this.detail.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequenceLinkId", "positiveInt", "List of input service items which this service line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, sequenceLinkId));
          childrenList.add(new Property("service", "Coding", "A code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("fee", "Money", "The fee charged for the professional service or product..", 0, java.lang.Integer.MAX_VALUE, fee));
          childrenList.add(new Property("noteNumberLinkId", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumberLinkId));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          childrenList.add(new Property("detail", "", "The second tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return this.sequenceLinkId == null ? new Base[0] : this.sequenceLinkId.toArray(new Base[this.sequenceLinkId.size()]); // PositiveIntType
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // Coding
        case 101254: /*fee*/ return this.fee == null ? new Base[0] : new Base[] {this.fee}; // Money
        case -1859667856: /*noteNumberLinkId*/ return this.noteNumberLinkId == null ? new Base[0] : this.noteNumberLinkId.toArray(new Base[this.noteNumberLinkId.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AddedItemAdjudicationComponent
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
          this.getAdjudication().add((AddedItemAdjudicationComponent) value); // AddedItemAdjudicationComponent
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
          this.getAdjudication().add((AddedItemAdjudicationComponent) value);
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
        case -231349275:  return addAdjudication(); // AddedItemAdjudicationComponent
        case -1335224239:  return addDetail(); // AddedItemsDetailComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.sequenceLinkId");
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
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.noteNumberLinkId");
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
          dst.adjudication = new ArrayList<AddedItemAdjudicationComponent>();
          for (AddedItemAdjudicationComponent i : adjudication)
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
        return super.isEmpty() && (sequenceLinkId == null || sequenceLinkId.isEmpty()) && (service == null || service.isEmpty())
           && (fee == null || fee.isEmpty()) && (noteNumberLinkId == null || noteNumberLinkId.isEmpty())
           && (adjudication == null || adjudication.isEmpty()) && (detail == null || detail.isEmpty())
          ;
      }

  public String fhirType() {
    return "ClaimResponse.addItem";

  }

  }

    @Block()
    public static class AddedItemAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.
         */
        @Child(name = "category", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc." )
        protected Coding category;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication reason", formalDefinition="Adjudication reason such as limit reached." )
        protected Coding reason;

        /**
         * Monetary amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monetary amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monetary value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = -1926987562L;

    /**
     * Constructor
     */
      public AddedItemAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AddedItemAdjudicationComponent(Coding category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public Coding getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemAdjudicationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Coding(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public AddedItemAdjudicationComponent setCategory(Coding value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemAdjudicationComponent.reason");
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
        public AddedItemAdjudicationComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monetary amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monetary amount associated with the code.)
         */
        public AddedItemAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemAdjudicationComponent.value");
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
        public AddedItemAdjudicationComponent setValueElement(DecimalType value) { 
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
        public AddedItemAdjudicationComponent setValue(BigDecimal value) { 
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
        public AddedItemAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AddedItemAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "Coding", "Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("reason", "Coding", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monetary amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
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
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.value");
        }
        else
          return super.addChild(name);
      }

      public AddedItemAdjudicationComponent copy() {
        AddedItemAdjudicationComponent dst = new AddedItemAdjudicationComponent();
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
        if (!(other instanceof AddedItemAdjudicationComponent))
          return false;
        AddedItemAdjudicationComponent o = (AddedItemAdjudicationComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AddedItemAdjudicationComponent))
          return false;
        AddedItemAdjudicationComponent o = (AddedItemAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (reason == null || reason.isEmpty())
           && (amount == null || amount.isEmpty()) && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ClaimResponse.addItem.adjudication";

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
         * The fee charged for the professional service or product..
         */
        @Child(name = "fee", type = {Money.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Professional fee or Product charge", formalDefinition="The fee charged for the professional service or product.." )
        protected Money fee;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Added items detail adjudication", formalDefinition="The adjudications results." )
        protected List<AddedItemDetailAdjudicationComponent> adjudication;

        private static final long serialVersionUID = -2104242020L;

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
         * @return {@link #fee} (The fee charged for the professional service or product..)
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
         * @param value {@link #fee} (The fee charged for the professional service or product..)
         */
        public AddedItemsDetailComponent setFee(Money value) { 
          this.fee = value;
          return this;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
        public List<AddedItemDetailAdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemDetailAdjudicationComponent>();
          return this.adjudication;
        }

        public boolean hasAdjudication() { 
          if (this.adjudication == null)
            return false;
          for (AddedItemDetailAdjudicationComponent item : this.adjudication)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
         */
    // syntactic sugar
        public AddedItemDetailAdjudicationComponent addAdjudication() { //3
          AddedItemDetailAdjudicationComponent t = new AddedItemDetailAdjudicationComponent();
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemDetailAdjudicationComponent>();
          this.adjudication.add(t);
          return t;
        }

    // syntactic sugar
        public AddedItemsDetailComponent addAdjudication(AddedItemDetailAdjudicationComponent t) { //3
          if (t == null)
            return this;
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AddedItemDetailAdjudicationComponent>();
          this.adjudication.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("service", "Coding", "A code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("fee", "Money", "The fee charged for the professional service or product..", 0, java.lang.Integer.MAX_VALUE, fee));
          childrenList.add(new Property("adjudication", "", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // Coding
        case 101254: /*fee*/ return this.fee == null ? new Base[0] : new Base[] {this.fee}; // Money
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AddedItemDetailAdjudicationComponent
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
          this.getAdjudication().add((AddedItemDetailAdjudicationComponent) value); // AddedItemDetailAdjudicationComponent
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
          this.getAdjudication().add((AddedItemDetailAdjudicationComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1984153269:  return getService(); // Coding
        case 101254:  return getFee(); // Money
        case -231349275:  return addAdjudication(); // AddedItemDetailAdjudicationComponent
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
          dst.adjudication = new ArrayList<AddedItemDetailAdjudicationComponent>();
          for (AddedItemDetailAdjudicationComponent i : adjudication)
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
        return super.isEmpty() && (service == null || service.isEmpty()) && (fee == null || fee.isEmpty())
           && (adjudication == null || adjudication.isEmpty());
      }

  public String fhirType() {
    return "ClaimResponse.addItem.detail";

  }

  }

    @Block()
    public static class AddedItemDetailAdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.
         */
        @Child(name = "category", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc." )
        protected Coding category;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Adjudication reason", formalDefinition="Adjudication reason such as limit reached." )
        protected Coding reason;

        /**
         * Monetary amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monetary amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Non-monetary value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = -1926987562L;

    /**
     * Constructor
     */
      public AddedItemDetailAdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AddedItemDetailAdjudicationComponent(Coding category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public Coding getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailAdjudicationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Coding(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public AddedItemDetailAdjudicationComponent setCategory(Coding value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailAdjudicationComponent.reason");
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
        public AddedItemDetailAdjudicationComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monetary amount associated with the code.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailAdjudicationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monetary amount associated with the code.)
         */
        public AddedItemDetailAdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DecimalType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailAdjudicationComponent.value");
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
        public AddedItemDetailAdjudicationComponent setValueElement(DecimalType value) { 
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
        public AddedItemDetailAdjudicationComponent setValue(BigDecimal value) { 
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
        public AddedItemDetailAdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AddedItemDetailAdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "Coding", "Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("reason", "Coding", "Adjudication reason such as limit reached.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("amount", "Money", "Monetary amount associated with the code.", 0, java.lang.Integer.MAX_VALUE, amount));
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
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.value");
        }
        else
          return super.addChild(name);
      }

      public AddedItemDetailAdjudicationComponent copy() {
        AddedItemDetailAdjudicationComponent dst = new AddedItemDetailAdjudicationComponent();
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
        if (!(other instanceof AddedItemDetailAdjudicationComponent))
          return false;
        AddedItemDetailAdjudicationComponent o = (AddedItemDetailAdjudicationComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AddedItemDetailAdjudicationComponent))
          return false;
        AddedItemDetailAdjudicationComponent o = (AddedItemDetailAdjudicationComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (reason == null || reason.isEmpty())
           && (amount == null || amount.isEmpty()) && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ClaimResponse.addItem.detail.adjudication";

  }

  }

    @Block()
    public static class ErrorsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.
         */
        @Child(name = "sequenceLinkId", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Item sequence number", formalDefinition="The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere." )
        protected PositiveIntType sequenceLinkId;

        /**
         * The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        @Child(name = "detailSequenceLinkId", type = {PositiveIntType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Detail sequence number", formalDefinition="The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition." )
        protected PositiveIntType detailSequenceLinkId;

        /**
         * The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        @Child(name = "subdetailSequenceLinkId", type = {PositiveIntType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Subdetail sequence number", formalDefinition="The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition." )
        protected PositiveIntType subdetailSequenceLinkId;

        /**
         * An error code,from a specified code system, which details why the claim could not be adjudicated.
         */
        @Child(name = "code", type = {Coding.class}, order=4, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Error code detailing processing issues", formalDefinition="An error code,from a specified code system, which details why the claim could not be adjudicated." )
        protected Coding code;

        private static final long serialVersionUID = -1893641175L;

    /**
     * Constructor
     */
      public ErrorsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ErrorsComponent(Coding code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #sequenceLinkId} (The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public PositiveIntType getSequenceLinkIdElement() { 
          if (this.sequenceLinkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ErrorsComponent.sequenceLinkId");
            else if (Configuration.doAutoCreate())
              this.sequenceLinkId = new PositiveIntType(); // bb
          return this.sequenceLinkId;
        }

        public boolean hasSequenceLinkIdElement() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        public boolean hasSequenceLinkId() { 
          return this.sequenceLinkId != null && !this.sequenceLinkId.isEmpty();
        }

        /**
         * @param value {@link #sequenceLinkId} (The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.). This is the underlying object with id, value and extensions. The accessor "getSequenceLinkId" gives direct access to the value
         */
        public ErrorsComponent setSequenceLinkIdElement(PositiveIntType value) { 
          this.sequenceLinkId = value;
          return this;
        }

        /**
         * @return The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.
         */
        public int getSequenceLinkId() { 
          return this.sequenceLinkId == null || this.sequenceLinkId.isEmpty() ? 0 : this.sequenceLinkId.getValue();
        }

        /**
         * @param value The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.
         */
        public ErrorsComponent setSequenceLinkId(int value) { 
            if (this.sequenceLinkId == null)
              this.sequenceLinkId = new PositiveIntType();
            this.sequenceLinkId.setValue(value);
          return this;
        }

        /**
         * @return {@link #detailSequenceLinkId} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.). This is the underlying object with id, value and extensions. The accessor "getDetailSequenceLinkId" gives direct access to the value
         */
        public PositiveIntType getDetailSequenceLinkIdElement() { 
          if (this.detailSequenceLinkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ErrorsComponent.detailSequenceLinkId");
            else if (Configuration.doAutoCreate())
              this.detailSequenceLinkId = new PositiveIntType(); // bb
          return this.detailSequenceLinkId;
        }

        public boolean hasDetailSequenceLinkIdElement() { 
          return this.detailSequenceLinkId != null && !this.detailSequenceLinkId.isEmpty();
        }

        public boolean hasDetailSequenceLinkId() { 
          return this.detailSequenceLinkId != null && !this.detailSequenceLinkId.isEmpty();
        }

        /**
         * @param value {@link #detailSequenceLinkId} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.). This is the underlying object with id, value and extensions. The accessor "getDetailSequenceLinkId" gives direct access to the value
         */
        public ErrorsComponent setDetailSequenceLinkIdElement(PositiveIntType value) { 
          this.detailSequenceLinkId = value;
          return this;
        }

        /**
         * @return The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        public int getDetailSequenceLinkId() { 
          return this.detailSequenceLinkId == null || this.detailSequenceLinkId.isEmpty() ? 0 : this.detailSequenceLinkId.getValue();
        }

        /**
         * @param value The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        public ErrorsComponent setDetailSequenceLinkId(int value) { 
            if (this.detailSequenceLinkId == null)
              this.detailSequenceLinkId = new PositiveIntType();
            this.detailSequenceLinkId.setValue(value);
          return this;
        }

        /**
         * @return {@link #subdetailSequenceLinkId} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.). This is the underlying object with id, value and extensions. The accessor "getSubdetailSequenceLinkId" gives direct access to the value
         */
        public PositiveIntType getSubdetailSequenceLinkIdElement() { 
          if (this.subdetailSequenceLinkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ErrorsComponent.subdetailSequenceLinkId");
            else if (Configuration.doAutoCreate())
              this.subdetailSequenceLinkId = new PositiveIntType(); // bb
          return this.subdetailSequenceLinkId;
        }

        public boolean hasSubdetailSequenceLinkIdElement() { 
          return this.subdetailSequenceLinkId != null && !this.subdetailSequenceLinkId.isEmpty();
        }

        public boolean hasSubdetailSequenceLinkId() { 
          return this.subdetailSequenceLinkId != null && !this.subdetailSequenceLinkId.isEmpty();
        }

        /**
         * @param value {@link #subdetailSequenceLinkId} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.). This is the underlying object with id, value and extensions. The accessor "getSubdetailSequenceLinkId" gives direct access to the value
         */
        public ErrorsComponent setSubdetailSequenceLinkIdElement(PositiveIntType value) { 
          this.subdetailSequenceLinkId = value;
          return this;
        }

        /**
         * @return The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        public int getSubdetailSequenceLinkId() { 
          return this.subdetailSequenceLinkId == null || this.subdetailSequenceLinkId.isEmpty() ? 0 : this.subdetailSequenceLinkId.getValue();
        }

        /**
         * @param value The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        public ErrorsComponent setSubdetailSequenceLinkId(int value) { 
            if (this.subdetailSequenceLinkId == null)
              this.subdetailSequenceLinkId = new PositiveIntType();
            this.subdetailSequenceLinkId.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (An error code,from a specified code system, which details why the claim could not be adjudicated.)
         */
        public Coding getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ErrorsComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Coding(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (An error code,from a specified code system, which details why the claim could not be adjudicated.)
         */
        public ErrorsComponent setCode(Coding value) { 
          this.code = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequenceLinkId", "positiveInt", "The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.", 0, java.lang.Integer.MAX_VALUE, sequenceLinkId));
          childrenList.add(new Property("detailSequenceLinkId", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, java.lang.Integer.MAX_VALUE, detailSequenceLinkId));
          childrenList.add(new Property("subdetailSequenceLinkId", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, java.lang.Integer.MAX_VALUE, subdetailSequenceLinkId));
          childrenList.add(new Property("code", "Coding", "An error code,from a specified code system, which details why the claim could not be adjudicated.", 0, java.lang.Integer.MAX_VALUE, code));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422298666: /*sequenceLinkId*/ return this.sequenceLinkId == null ? new Base[0] : new Base[] {this.sequenceLinkId}; // PositiveIntType
        case 516748423: /*detailSequenceLinkId*/ return this.detailSequenceLinkId == null ? new Base[0] : new Base[] {this.detailSequenceLinkId}; // PositiveIntType
        case -1061088569: /*subdetailSequenceLinkId*/ return this.subdetailSequenceLinkId == null ? new Base[0] : new Base[] {this.subdetailSequenceLinkId}; // PositiveIntType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422298666: // sequenceLinkId
          this.sequenceLinkId = castToPositiveInt(value); // PositiveIntType
          break;
        case 516748423: // detailSequenceLinkId
          this.detailSequenceLinkId = castToPositiveInt(value); // PositiveIntType
          break;
        case -1061088569: // subdetailSequenceLinkId
          this.subdetailSequenceLinkId = castToPositiveInt(value); // PositiveIntType
          break;
        case 3059181: // code
          this.code = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequenceLinkId"))
          this.sequenceLinkId = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("detailSequenceLinkId"))
          this.detailSequenceLinkId = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("subdetailSequenceLinkId"))
          this.subdetailSequenceLinkId = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("code"))
          this.code = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422298666: throw new FHIRException("Cannot make property sequenceLinkId as it is not a complex type"); // PositiveIntType
        case 516748423: throw new FHIRException("Cannot make property detailSequenceLinkId as it is not a complex type"); // PositiveIntType
        case -1061088569: throw new FHIRException("Cannot make property subdetailSequenceLinkId as it is not a complex type"); // PositiveIntType
        case 3059181:  return getCode(); // Coding
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.sequenceLinkId");
        }
        else if (name.equals("detailSequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.detailSequenceLinkId");
        }
        else if (name.equals("subdetailSequenceLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.subdetailSequenceLinkId");
        }
        else if (name.equals("code")) {
          this.code = new Coding();
          return this.code;
        }
        else
          return super.addChild(name);
      }

      public ErrorsComponent copy() {
        ErrorsComponent dst = new ErrorsComponent();
        copyValues(dst);
        dst.sequenceLinkId = sequenceLinkId == null ? null : sequenceLinkId.copy();
        dst.detailSequenceLinkId = detailSequenceLinkId == null ? null : detailSequenceLinkId.copy();
        dst.subdetailSequenceLinkId = subdetailSequenceLinkId == null ? null : subdetailSequenceLinkId.copy();
        dst.code = code == null ? null : code.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ErrorsComponent))
          return false;
        ErrorsComponent o = (ErrorsComponent) other;
        return compareDeep(sequenceLinkId, o.sequenceLinkId, true) && compareDeep(detailSequenceLinkId, o.detailSequenceLinkId, true)
           && compareDeep(subdetailSequenceLinkId, o.subdetailSequenceLinkId, true) && compareDeep(code, o.code, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ErrorsComponent))
          return false;
        ErrorsComponent o = (ErrorsComponent) other;
        return compareValues(sequenceLinkId, o.sequenceLinkId, true) && compareValues(detailSequenceLinkId, o.detailSequenceLinkId, true)
           && compareValues(subdetailSequenceLinkId, o.subdetailSequenceLinkId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequenceLinkId == null || sequenceLinkId.isEmpty()) && (detailSequenceLinkId == null || detailSequenceLinkId.isEmpty())
           && (subdetailSequenceLinkId == null || subdetailSequenceLinkId.isEmpty()) && (code == null || code.isEmpty())
          ;
      }

  public String fhirType() {
    return "ClaimResponse.error";

  }

  }

    @Block()
    public static class NotesComponent extends BackboneElement implements IBaseBackboneElement {
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
        @Description(shortDefinition="Note explanatory text", formalDefinition="The note text." )
        protected StringType text;

        private static final long serialVersionUID = 1768923951L;

    /**
     * Constructor
     */
      public NotesComponent() {
        super();
      }

        /**
         * @return {@link #number} (An integer associated with each note which may be referred to from each service line item.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public PositiveIntType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NotesComponent.number");
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
        public NotesComponent setNumberElement(PositiveIntType value) { 
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
        public NotesComponent setNumber(int value) { 
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
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.number");
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.text");
        }
        else
          return super.addChild(name);
      }

      public NotesComponent copy() {
        NotesComponent dst = new NotesComponent();
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
        if (!(other instanceof NotesComponent))
          return false;
        NotesComponent o = (NotesComponent) other;
        return compareDeep(number, o.number, true) && compareDeep(type, o.type, true) && compareDeep(text, o.text, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NotesComponent))
          return false;
        NotesComponent o = (NotesComponent) other;
        return compareValues(number, o.number, true) && compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (number == null || number.isEmpty()) && (type == null || type.isEmpty())
           && (text == null || text.isEmpty());
      }

  public String fhirType() {
    return "ClaimResponse.note";

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

        private static final long serialVersionUID = -1151494539L;

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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "positiveInt", "A service line item.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("focal", "boolean", "The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.", 0, java.lang.Integer.MAX_VALUE, focal));
          childrenList.add(new Property("coverage[x]", "Identifier|Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, coverage));
          childrenList.add(new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, java.lang.Integer.MAX_VALUE, businessArrangement));
          childrenList.add(new Property("preAuthRef", "string", "A list of references from the Insurer to which these services pertain.", 0, java.lang.Integer.MAX_VALUE, preAuthRef));
          childrenList.add(new Property("claimResponse", "Reference(ClaimResponse)", "The Coverages adjudication details.", 0, java.lang.Integer.MAX_VALUE, claimResponse));
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
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.sequence");
        }
        else if (name.equals("focal")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.focal");
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
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.businessArrangement");
        }
        else if (name.equals("preAuthRef")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.preAuthRef");
        }
        else if (name.equals("claimResponse")) {
          this.claimResponse = new Reference();
          return this.claimResponse;
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
           && compareDeep(claimResponse, o.claimResponse, true);
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
          ;
      }

  public String fhirType() {
    return "ClaimResponse.coverage";

  }

  }

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Response  number", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * Original request resource referrence.
     */
    @Child(name = "request", type = {Identifier.class, Claim.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Id of resource triggering adjudication", formalDefinition="Original request resource referrence." )
    protected Type request;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    protected Coding originalRuleset;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The Insurer who produced this adjudicated response.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurer", formalDefinition="The Insurer who produced this adjudicated response." )
    protected Type organization;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Identifier.class, Practitioner.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Type requestProvider;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "requestOrganization", type = {Identifier.class, Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Type requestOrganization;

    /**
     * Transaction status: error, complete.
     */
    @Child(name = "outcome", type = {CodeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="complete | error", formalDefinition="Transaction status: error, complete." )
    protected Enumeration<RemittanceOutcome> outcome;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * Party to be reimbursed: Subscriber, provider, other.
     */
    @Child(name = "payeeType", type = {Coding.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Party to be paid any benefits payable", formalDefinition="Party to be reimbursed: Subscriber, provider, other." )
    protected Coding payeeType;

    /**
     * The first tier service adjudications for submitted services.
     */
    @Child(name = "item", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Line items", formalDefinition="The first tier service adjudications for submitted services." )
    protected List<ItemsComponent> item;

    /**
     * The first tier service adjudications for payor added services.
     */
    @Child(name = "addItem", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Insurer added line items", formalDefinition="The first tier service adjudications for payor added services." )
    protected List<AddedItemComponent> addItem;

    /**
     * Mutually exclusive with Services Provided (Item).
     */
    @Child(name = "error", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Processing errors", formalDefinition="Mutually exclusive with Services Provided (Item)." )
    protected List<ErrorsComponent> error;

    /**
     * The total cost of the services reported.
     */
    @Child(name = "totalCost", type = {Money.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total Cost of service from the Claim", formalDefinition="The total cost of the services reported." )
    protected Money totalCost;

    /**
     * The amount of deductible applied which was not allocated to any particular service line.
     */
    @Child(name = "unallocDeductable", type = {Money.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unallocated deductible", formalDefinition="The amount of deductible applied which was not allocated to any particular service line." )
    protected Money unallocDeductable;

    /**
     * Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductible).
     */
    @Child(name = "totalBenefit", type = {Money.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total benefit payable for the Claim", formalDefinition="Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductible)." )
    protected Money totalBenefit;

    /**
     * Adjustment to the payment of this transaction which is not related to adjudication of this transaction.
     */
    @Child(name = "paymentAdjustment", type = {Money.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment adjustment for non-Claim issues", formalDefinition="Adjustment to the payment of this transaction which is not related to adjudication of this transaction." )
    protected Money paymentAdjustment;

    /**
     * Reason for the payment adjustment.
     */
    @Child(name = "paymentAdjustmentReason", type = {Coding.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for Payment adjustment", formalDefinition="Reason for the payment adjustment." )
    protected Coding paymentAdjustmentReason;

    /**
     * Estimated payment data.
     */
    @Child(name = "paymentDate", type = {DateType.class}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Expected data of Payment", formalDefinition="Estimated payment data." )
    protected DateType paymentDate;

    /**
     * Payable less any payment adjustment.
     */
    @Child(name = "paymentAmount", type = {Money.class}, order=20, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment amount", formalDefinition="Payable less any payment adjustment." )
    protected Money paymentAmount;

    /**
     * Payment identifier.
     */
    @Child(name = "paymentRef", type = {Identifier.class}, order=21, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Payment identifier", formalDefinition="Payment identifier." )
    protected Identifier paymentRef;

    /**
     * Status of funds reservation (For provider, for Patient, None).
     */
    @Child(name = "reserved", type = {Coding.class}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Funds reserved status", formalDefinition="Status of funds reservation (For provider, for Patient, None)." )
    protected Coding reserved;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {Coding.class}, order=23, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    protected Coding form;

    /**
     * Note text.
     */
    @Child(name = "note", type = {}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Processing notes", formalDefinition="Note text." )
    protected List<NotesComponent> note;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "coverage", type = {}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected List<CoverageComponent> coverage;

    private static final long serialVersionUID = 1381594471L;

  /**
   * Constructor
   */
    public ClaimResponse() {
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
    public ClaimResponse addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #request} (Original request resource referrence.)
     */
    public Type getRequest() { 
      return this.request;
    }

    /**
     * @return {@link #request} (Original request resource referrence.)
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
     * @return {@link #request} (Original request resource referrence.)
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
     * @param value {@link #request} (Original request resource referrence.)
     */
    public ClaimResponse setRequest(Type value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.ruleset");
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
    public ClaimResponse setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.originalRuleset");
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
    public ClaimResponse setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.created");
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
    public ClaimResponse setCreatedElement(DateTimeType value) { 
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
    public ClaimResponse setCreated(Date value) { 
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
    public ClaimResponse setOrganization(Type value) { 
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
    public ClaimResponse setRequestProvider(Type value) { 
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
    public ClaimResponse setRequestOrganization(Type value) { 
      this.requestOrganization = value;
      return this;
    }

    /**
     * @return {@link #outcome} (Transaction status: error, complete.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public Enumeration<RemittanceOutcome> getOutcomeElement() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.outcome");
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
    public ClaimResponse setOutcomeElement(Enumeration<RemittanceOutcome> value) { 
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
    public ClaimResponse setOutcome(RemittanceOutcome value) { 
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
          throw new Error("Attempt to auto-create ClaimResponse.disposition");
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
    public ClaimResponse setDispositionElement(StringType value) { 
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
    public ClaimResponse setDisposition(String value) { 
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
     * @return {@link #payeeType} (Party to be reimbursed: Subscriber, provider, other.)
     */
    public Coding getPayeeType() { 
      if (this.payeeType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.payeeType");
        else if (Configuration.doAutoCreate())
          this.payeeType = new Coding(); // cc
      return this.payeeType;
    }

    public boolean hasPayeeType() { 
      return this.payeeType != null && !this.payeeType.isEmpty();
    }

    /**
     * @param value {@link #payeeType} (Party to be reimbursed: Subscriber, provider, other.)
     */
    public ClaimResponse setPayeeType(Coding value) { 
      this.payeeType = value;
      return this;
    }

    /**
     * @return {@link #item} (The first tier service adjudications for submitted services.)
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
     * @return {@link #item} (The first tier service adjudications for submitted services.)
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
    public ClaimResponse addItem(ItemsComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return {@link #addItem} (The first tier service adjudications for payor added services.)
     */
    public List<AddedItemComponent> getAddItem() { 
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      return this.addItem;
    }

    public boolean hasAddItem() { 
      if (this.addItem == null)
        return false;
      for (AddedItemComponent item : this.addItem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #addItem} (The first tier service adjudications for payor added services.)
     */
    // syntactic sugar
    public AddedItemComponent addAddItem() { //3
      AddedItemComponent t = new AddedItemComponent();
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      this.addItem.add(t);
      return t;
    }

    // syntactic sugar
    public ClaimResponse addAddItem(AddedItemComponent t) { //3
      if (t == null)
        return this;
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      this.addItem.add(t);
      return this;
    }

    /**
     * @return {@link #error} (Mutually exclusive with Services Provided (Item).)
     */
    public List<ErrorsComponent> getError() { 
      if (this.error == null)
        this.error = new ArrayList<ErrorsComponent>();
      return this.error;
    }

    public boolean hasError() { 
      if (this.error == null)
        return false;
      for (ErrorsComponent item : this.error)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #error} (Mutually exclusive with Services Provided (Item).)
     */
    // syntactic sugar
    public ErrorsComponent addError() { //3
      ErrorsComponent t = new ErrorsComponent();
      if (this.error == null)
        this.error = new ArrayList<ErrorsComponent>();
      this.error.add(t);
      return t;
    }

    // syntactic sugar
    public ClaimResponse addError(ErrorsComponent t) { //3
      if (t == null)
        return this;
      if (this.error == null)
        this.error = new ArrayList<ErrorsComponent>();
      this.error.add(t);
      return this;
    }

    /**
     * @return {@link #totalCost} (The total cost of the services reported.)
     */
    public Money getTotalCost() { 
      if (this.totalCost == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.totalCost");
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
    public ClaimResponse setTotalCost(Money value) { 
      this.totalCost = value;
      return this;
    }

    /**
     * @return {@link #unallocDeductable} (The amount of deductible applied which was not allocated to any particular service line.)
     */
    public Money getUnallocDeductable() { 
      if (this.unallocDeductable == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.unallocDeductable");
        else if (Configuration.doAutoCreate())
          this.unallocDeductable = new Money(); // cc
      return this.unallocDeductable;
    }

    public boolean hasUnallocDeductable() { 
      return this.unallocDeductable != null && !this.unallocDeductable.isEmpty();
    }

    /**
     * @param value {@link #unallocDeductable} (The amount of deductible applied which was not allocated to any particular service line.)
     */
    public ClaimResponse setUnallocDeductable(Money value) { 
      this.unallocDeductable = value;
      return this;
    }

    /**
     * @return {@link #totalBenefit} (Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductible).)
     */
    public Money getTotalBenefit() { 
      if (this.totalBenefit == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.totalBenefit");
        else if (Configuration.doAutoCreate())
          this.totalBenefit = new Money(); // cc
      return this.totalBenefit;
    }

    public boolean hasTotalBenefit() { 
      return this.totalBenefit != null && !this.totalBenefit.isEmpty();
    }

    /**
     * @param value {@link #totalBenefit} (Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductible).)
     */
    public ClaimResponse setTotalBenefit(Money value) { 
      this.totalBenefit = value;
      return this;
    }

    /**
     * @return {@link #paymentAdjustment} (Adjustment to the payment of this transaction which is not related to adjudication of this transaction.)
     */
    public Money getPaymentAdjustment() { 
      if (this.paymentAdjustment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.paymentAdjustment");
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
    public ClaimResponse setPaymentAdjustment(Money value) { 
      this.paymentAdjustment = value;
      return this;
    }

    /**
     * @return {@link #paymentAdjustmentReason} (Reason for the payment adjustment.)
     */
    public Coding getPaymentAdjustmentReason() { 
      if (this.paymentAdjustmentReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.paymentAdjustmentReason");
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
    public ClaimResponse setPaymentAdjustmentReason(Coding value) { 
      this.paymentAdjustmentReason = value;
      return this;
    }

    /**
     * @return {@link #paymentDate} (Estimated payment data.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
     */
    public DateType getPaymentDateElement() { 
      if (this.paymentDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.paymentDate");
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
     * @param value {@link #paymentDate} (Estimated payment data.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
     */
    public ClaimResponse setPaymentDateElement(DateType value) { 
      this.paymentDate = value;
      return this;
    }

    /**
     * @return Estimated payment data.
     */
    public Date getPaymentDate() { 
      return this.paymentDate == null ? null : this.paymentDate.getValue();
    }

    /**
     * @param value Estimated payment data.
     */
    public ClaimResponse setPaymentDate(Date value) { 
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
          throw new Error("Attempt to auto-create ClaimResponse.paymentAmount");
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
    public ClaimResponse setPaymentAmount(Money value) { 
      this.paymentAmount = value;
      return this;
    }

    /**
     * @return {@link #paymentRef} (Payment identifier.)
     */
    public Identifier getPaymentRef() { 
      if (this.paymentRef == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.paymentRef");
        else if (Configuration.doAutoCreate())
          this.paymentRef = new Identifier(); // cc
      return this.paymentRef;
    }

    public boolean hasPaymentRef() { 
      return this.paymentRef != null && !this.paymentRef.isEmpty();
    }

    /**
     * @param value {@link #paymentRef} (Payment identifier.)
     */
    public ClaimResponse setPaymentRef(Identifier value) { 
      this.paymentRef = value;
      return this;
    }

    /**
     * @return {@link #reserved} (Status of funds reservation (For provider, for Patient, None).)
     */
    public Coding getReserved() { 
      if (this.reserved == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.reserved");
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
    public ClaimResponse setReserved(Coding value) { 
      this.reserved = value;
      return this;
    }

    /**
     * @return {@link #form} (The form to be used for printing the content.)
     */
    public Coding getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.form");
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
    public ClaimResponse setForm(Coding value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #note} (Note text.)
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
     * @return {@link #note} (Note text.)
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
    public ClaimResponse addNote(NotesComponent t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<NotesComponent>();
      this.note.add(t);
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
    public ClaimResponse addCoverage(CoverageComponent t) { //3
      if (t == null)
        return this;
      if (this.coverage == null)
        this.coverage = new ArrayList<CoverageComponent>();
      this.coverage.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("request[x]", "Identifier|Reference(Claim)", "Original request resource referrence.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The Insurer who produced this adjudicated response.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("requestProvider[x]", "Identifier|Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestProvider));
        childrenList.add(new Property("requestOrganization[x]", "Identifier|Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestOrganization));
        childrenList.add(new Property("outcome", "code", "Transaction status: error, complete.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("payeeType", "Coding", "Party to be reimbursed: Subscriber, provider, other.", 0, java.lang.Integer.MAX_VALUE, payeeType));
        childrenList.add(new Property("item", "", "The first tier service adjudications for submitted services.", 0, java.lang.Integer.MAX_VALUE, item));
        childrenList.add(new Property("addItem", "", "The first tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, addItem));
        childrenList.add(new Property("error", "", "Mutually exclusive with Services Provided (Item).", 0, java.lang.Integer.MAX_VALUE, error));
        childrenList.add(new Property("totalCost", "Money", "The total cost of the services reported.", 0, java.lang.Integer.MAX_VALUE, totalCost));
        childrenList.add(new Property("unallocDeductable", "Money", "The amount of deductible applied which was not allocated to any particular service line.", 0, java.lang.Integer.MAX_VALUE, unallocDeductable));
        childrenList.add(new Property("totalBenefit", "Money", "Total amount of benefit payable (Equal to sum of the Benefit amounts from all detail lines and additions less the Unallocated Deductible).", 0, java.lang.Integer.MAX_VALUE, totalBenefit));
        childrenList.add(new Property("paymentAdjustment", "Money", "Adjustment to the payment of this transaction which is not related to adjudication of this transaction.", 0, java.lang.Integer.MAX_VALUE, paymentAdjustment));
        childrenList.add(new Property("paymentAdjustmentReason", "Coding", "Reason for the payment adjustment.", 0, java.lang.Integer.MAX_VALUE, paymentAdjustmentReason));
        childrenList.add(new Property("paymentDate", "date", "Estimated payment data.", 0, java.lang.Integer.MAX_VALUE, paymentDate));
        childrenList.add(new Property("paymentAmount", "Money", "Payable less any payment adjustment.", 0, java.lang.Integer.MAX_VALUE, paymentAmount));
        childrenList.add(new Property("paymentRef", "Identifier", "Payment identifier.", 0, java.lang.Integer.MAX_VALUE, paymentRef));
        childrenList.add(new Property("reserved", "Coding", "Status of funds reservation (For provider, for Patient, None).", 0, java.lang.Integer.MAX_VALUE, reserved));
        childrenList.add(new Property("form", "Coding", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("note", "", "Note text.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("coverage", "", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, coverage));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Type
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case 1601527200: /*requestProvider*/ return this.requestProvider == null ? new Base[0] : new Base[] {this.requestProvider}; // Type
        case 599053666: /*requestOrganization*/ return this.requestOrganization == null ? new Base[0] : new Base[] {this.requestOrganization}; // Type
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // Enumeration<RemittanceOutcome>
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case -316321118: /*payeeType*/ return this.payeeType == null ? new Base[0] : new Base[] {this.payeeType}; // Coding
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // ItemsComponent
        case -1148899500: /*addItem*/ return this.addItem == null ? new Base[0] : this.addItem.toArray(new Base[this.addItem.size()]); // AddedItemComponent
        case 96784904: /*error*/ return this.error == null ? new Base[0] : this.error.toArray(new Base[this.error.size()]); // ErrorsComponent
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
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // NotesComponent
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : this.coverage.toArray(new Base[this.coverage.size()]); // CoverageComponent
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
        case 1548678118: // ruleset
          this.ruleset = castToCoding(value); // Coding
          break;
        case 1089373397: // originalRuleset
          this.originalRuleset = castToCoding(value); // Coding
          break;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
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
        case -1106507950: // outcome
          this.outcome = new RemittanceOutcomeEnumFactory().fromType(value); // Enumeration<RemittanceOutcome>
          break;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
          break;
        case -316321118: // payeeType
          this.payeeType = castToCoding(value); // Coding
          break;
        case 3242771: // item
          this.getItem().add((ItemsComponent) value); // ItemsComponent
          break;
        case -1148899500: // addItem
          this.getAddItem().add((AddedItemComponent) value); // AddedItemComponent
          break;
        case 96784904: // error
          this.getError().add((ErrorsComponent) value); // ErrorsComponent
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
          this.getNote().add((NotesComponent) value); // NotesComponent
          break;
        case -351767064: // coverage
          this.getCoverage().add((CoverageComponent) value); // CoverageComponent
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
        else if (name.equals("ruleset"))
          this.ruleset = castToCoding(value); // Coding
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("organization[x]"))
          this.organization = (Type) value; // Type
        else if (name.equals("requestProvider[x]"))
          this.requestProvider = (Type) value; // Type
        else if (name.equals("requestOrganization[x]"))
          this.requestOrganization = (Type) value; // Type
        else if (name.equals("outcome"))
          this.outcome = new RemittanceOutcomeEnumFactory().fromType(value); // Enumeration<RemittanceOutcome>
        else if (name.equals("disposition"))
          this.disposition = castToString(value); // StringType
        else if (name.equals("payeeType"))
          this.payeeType = castToCoding(value); // Coding
        else if (name.equals("item"))
          this.getItem().add((ItemsComponent) value);
        else if (name.equals("addItem"))
          this.getAddItem().add((AddedItemComponent) value);
        else if (name.equals("error"))
          this.getError().add((ErrorsComponent) value);
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
          this.getNote().add((NotesComponent) value);
        else if (name.equals("coverage"))
          this.getCoverage().add((CoverageComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 37106577:  return getRequest(); // Type
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case 1326483053:  return getOrganization(); // Type
        case -1694784800:  return getRequestProvider(); // Type
        case 818740190:  return getRequestOrganization(); // Type
        case -1106507950: throw new FHIRException("Cannot make property outcome as it is not a complex type"); // Enumeration<RemittanceOutcome>
        case 583380919: throw new FHIRException("Cannot make property disposition as it is not a complex type"); // StringType
        case -316321118:  return getPayeeType(); // Coding
        case 3242771:  return addItem(); // ItemsComponent
        case -1148899500:  return addAddItem(); // AddedItemComponent
        case 96784904:  return addError(); // ErrorsComponent
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
        case 3387378:  return addNote(); // NotesComponent
        case -351767064:  return addCoverage(); // CoverageComponent
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
        else if (name.equals("ruleset")) {
          this.ruleset = new Coding();
          return this.ruleset;
        }
        else if (name.equals("originalRuleset")) {
          this.originalRuleset = new Coding();
          return this.originalRuleset;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.created");
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
        else if (name.equals("outcome")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.outcome");
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.disposition");
        }
        else if (name.equals("payeeType")) {
          this.payeeType = new Coding();
          return this.payeeType;
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else if (name.equals("addItem")) {
          return addAddItem();
        }
        else if (name.equals("error")) {
          return addError();
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
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.paymentDate");
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
        else if (name.equals("coverage")) {
          return addCoverage();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ClaimResponse";

  }

      public ClaimResponse copy() {
        ClaimResponse dst = new ClaimResponse();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.request = request == null ? null : request.copy();
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.requestProvider = requestProvider == null ? null : requestProvider.copy();
        dst.requestOrganization = requestOrganization == null ? null : requestOrganization.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.payeeType = payeeType == null ? null : payeeType.copy();
        if (item != null) {
          dst.item = new ArrayList<ItemsComponent>();
          for (ItemsComponent i : item)
            dst.item.add(i.copy());
        };
        if (addItem != null) {
          dst.addItem = new ArrayList<AddedItemComponent>();
          for (AddedItemComponent i : addItem)
            dst.addItem.add(i.copy());
        };
        if (error != null) {
          dst.error = new ArrayList<ErrorsComponent>();
          for (ErrorsComponent i : error)
            dst.error.add(i.copy());
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
          dst.note = new ArrayList<NotesComponent>();
          for (NotesComponent i : note)
            dst.note.add(i.copy());
        };
        if (coverage != null) {
          dst.coverage = new ArrayList<CoverageComponent>();
          for (CoverageComponent i : coverage)
            dst.coverage.add(i.copy());
        };
        return dst;
      }

      protected ClaimResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ClaimResponse))
          return false;
        ClaimResponse o = (ClaimResponse) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(request, o.request, true) && compareDeep(ruleset, o.ruleset, true)
           && compareDeep(originalRuleset, o.originalRuleset, true) && compareDeep(created, o.created, true)
           && compareDeep(organization, o.organization, true) && compareDeep(requestProvider, o.requestProvider, true)
           && compareDeep(requestOrganization, o.requestOrganization, true) && compareDeep(outcome, o.outcome, true)
           && compareDeep(disposition, o.disposition, true) && compareDeep(payeeType, o.payeeType, true) && compareDeep(item, o.item, true)
           && compareDeep(addItem, o.addItem, true) && compareDeep(error, o.error, true) && compareDeep(totalCost, o.totalCost, true)
           && compareDeep(unallocDeductable, o.unallocDeductable, true) && compareDeep(totalBenefit, o.totalBenefit, true)
           && compareDeep(paymentAdjustment, o.paymentAdjustment, true) && compareDeep(paymentAdjustmentReason, o.paymentAdjustmentReason, true)
           && compareDeep(paymentDate, o.paymentDate, true) && compareDeep(paymentAmount, o.paymentAmount, true)
           && compareDeep(paymentRef, o.paymentRef, true) && compareDeep(reserved, o.reserved, true) && compareDeep(form, o.form, true)
           && compareDeep(note, o.note, true) && compareDeep(coverage, o.coverage, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ClaimResponse))
          return false;
        ClaimResponse o = (ClaimResponse) other;
        return compareValues(created, o.created, true) && compareValues(outcome, o.outcome, true) && compareValues(disposition, o.disposition, true)
           && compareValues(paymentDate, o.paymentDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (request == null || request.isEmpty())
           && (ruleset == null || ruleset.isEmpty()) && (originalRuleset == null || originalRuleset.isEmpty())
           && (created == null || created.isEmpty()) && (organization == null || organization.isEmpty())
           && (requestProvider == null || requestProvider.isEmpty()) && (requestOrganization == null || requestOrganization.isEmpty())
           && (outcome == null || outcome.isEmpty()) && (disposition == null || disposition.isEmpty())
           && (payeeType == null || payeeType.isEmpty()) && (item == null || item.isEmpty()) && (addItem == null || addItem.isEmpty())
           && (error == null || error.isEmpty()) && (totalCost == null || totalCost.isEmpty()) && (unallocDeductable == null || unallocDeductable.isEmpty())
           && (totalBenefit == null || totalBenefit.isEmpty()) && (paymentAdjustment == null || paymentAdjustment.isEmpty())
           && (paymentAdjustmentReason == null || paymentAdjustmentReason.isEmpty()) && (paymentDate == null || paymentDate.isEmpty())
           && (paymentAmount == null || paymentAmount.isEmpty()) && (paymentRef == null || paymentRef.isEmpty())
           && (reserved == null || reserved.isEmpty()) && (form == null || form.isEmpty()) && (note == null || note.isEmpty())
           && (coverage == null || coverage.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ClaimResponse;
   }

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>The creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClaimResponse.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="ClaimResponse.created", description="The creation date", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>The creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClaimResponse.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>requestidentifier</b>
   * <p>
   * Description: <b>The claim reference</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.requestIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestidentifier", path="ClaimResponse.request.as(Identifier)", description="The claim reference", type="token" )
  public static final String SP_REQUESTIDENTIFIER = "requestidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestidentifier</b>
   * <p>
   * Description: <b>The claim reference</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.requestIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUESTIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUESTIDENTIFIER);

 /**
   * Search parameter: <b>requestreference</b>
   * <p>
   * Description: <b>The claim reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.requestReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestreference", path="ClaimResponse.request.as(Reference)", description="The claim reference", type="reference" )
  public static final String SP_REQUESTREFERENCE = "requestreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestreference</b>
   * <p>
   * Description: <b>The claim reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.requestReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClaimResponse:requestreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTREFERENCE = new ca.uhn.fhir.model.api.Include("ClaimResponse:requestreference").toLocked();

 /**
   * Search parameter: <b>paymentdate</b>
   * <p>
   * Description: <b>The expected paymentDate</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClaimResponse.paymentDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="paymentdate", path="ClaimResponse.paymentDate", description="The expected paymentDate", type="date" )
  public static final String SP_PAYMENTDATE = "paymentdate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>paymentdate</b>
   * <p>
   * Description: <b>The expected paymentDate</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClaimResponse.paymentDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PAYMENTDATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PAYMENTDATE);

 /**
   * Search parameter: <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.organizationIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationidentifier", path="ClaimResponse.organization.as(Identifier)", description="The organization who generated this resource", type="token" )
  public static final String SP_ORGANIZATIONIDENTIFIER = "organizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.organizationIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.organizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationreference", path="ClaimResponse.organization.as(Reference)", description="The organization who generated this resource", type="reference" )
  public static final String SP_ORGANIZATIONREFERENCE = "organizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.organizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClaimResponse:organizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("ClaimResponse:organizationreference").toLocked();

 /**
   * Search parameter: <b>outcome</b>
   * <p>
   * Description: <b>The processing outcome</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.outcome</b><br>
   * </p>
   */
  @SearchParamDefinition(name="outcome", path="ClaimResponse.outcome", description="The processing outcome", type="token" )
  public static final String SP_OUTCOME = "outcome";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>outcome</b>
   * <p>
   * Description: <b>The processing outcome</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.outcome</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam OUTCOME = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_OUTCOME);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identity of the insurer</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ClaimResponse.identifier", description="The identity of the insurer", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identity of the insurer</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>disposition</b>
   * <p>
   * Description: <b>The contents of the disposition message</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ClaimResponse.disposition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="disposition", path="ClaimResponse.disposition", description="The contents of the disposition message", type="string" )
  public static final String SP_DISPOSITION = "disposition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>disposition</b>
   * <p>
   * Description: <b>The contents of the disposition message</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ClaimResponse.disposition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DISPOSITION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DISPOSITION);


}

