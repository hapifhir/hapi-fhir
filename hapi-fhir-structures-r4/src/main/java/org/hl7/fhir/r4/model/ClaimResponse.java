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

import java.math.*;
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
 * This resource provides the adjudication details from the processing of a Claim resource.
 */
@ResourceDef(name="ClaimResponse", profile="http://hl7.org/fhir/StructureDefinition/ClaimResponse")
public class ClaimResponse extends DomainResource {

    public enum ClaimResponseStatus {
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
        public static ClaimResponseStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown ClaimResponseStatus code '"+codeString+"'");
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

  public static class ClaimResponseStatusEnumFactory implements EnumFactory<ClaimResponseStatus> {
    public ClaimResponseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ClaimResponseStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return ClaimResponseStatus.CANCELLED;
        if ("draft".equals(codeString))
          return ClaimResponseStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return ClaimResponseStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ClaimResponseStatus code '"+codeString+"'");
        }
        public Enumeration<ClaimResponseStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ClaimResponseStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<ClaimResponseStatus>(this, ClaimResponseStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<ClaimResponseStatus>(this, ClaimResponseStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<ClaimResponseStatus>(this, ClaimResponseStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ClaimResponseStatus>(this, ClaimResponseStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ClaimResponseStatus code '"+codeString+"'");
        }
    public String toCode(ClaimResponseStatus code) {
      if (code == ClaimResponseStatus.ACTIVE)
        return "active";
      if (code == ClaimResponseStatus.CANCELLED)
        return "cancelled";
      if (code == ClaimResponseStatus.DRAFT)
        return "draft";
      if (code == ClaimResponseStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ClaimResponseStatus code) {
      return code.getSystem();
      }
    }

    public enum Use {
        /**
         * The treatment is complete and this represents a Claim for the services.
         */
        CLAIM, 
        /**
         * The treatment is proposed and this represents a Pre-authorization for the services.
         */
        PREAUTHORIZATION, 
        /**
         * The treatment is proposed and this represents a Pre-determination for the services.
         */
        PREDETERMINATION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static Use fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("claim".equals(codeString))
          return CLAIM;
        if ("preauthorization".equals(codeString))
          return PREAUTHORIZATION;
        if ("predetermination".equals(codeString))
          return PREDETERMINATION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown Use code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CLAIM: return "claim";
            case PREAUTHORIZATION: return "preauthorization";
            case PREDETERMINATION: return "predetermination";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CLAIM: return "http://hl7.org/fhir/claim-use";
            case PREAUTHORIZATION: return "http://hl7.org/fhir/claim-use";
            case PREDETERMINATION: return "http://hl7.org/fhir/claim-use";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CLAIM: return "The treatment is complete and this represents a Claim for the services.";
            case PREAUTHORIZATION: return "The treatment is proposed and this represents a Pre-authorization for the services.";
            case PREDETERMINATION: return "The treatment is proposed and this represents a Pre-determination for the services.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CLAIM: return "Claim";
            case PREAUTHORIZATION: return "Preauthorization";
            case PREDETERMINATION: return "Predetermination";
            default: return "?";
          }
        }
    }

  public static class UseEnumFactory implements EnumFactory<Use> {
    public Use fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("claim".equals(codeString))
          return Use.CLAIM;
        if ("preauthorization".equals(codeString))
          return Use.PREAUTHORIZATION;
        if ("predetermination".equals(codeString))
          return Use.PREDETERMINATION;
        throw new IllegalArgumentException("Unknown Use code '"+codeString+"'");
        }
        public Enumeration<Use> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<Use>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("claim".equals(codeString))
          return new Enumeration<Use>(this, Use.CLAIM);
        if ("preauthorization".equals(codeString))
          return new Enumeration<Use>(this, Use.PREAUTHORIZATION);
        if ("predetermination".equals(codeString))
          return new Enumeration<Use>(this, Use.PREDETERMINATION);
        throw new FHIRException("Unknown Use code '"+codeString+"'");
        }
    public String toCode(Use code) {
      if (code == Use.CLAIM)
        return "claim";
      if (code == Use.PREAUTHORIZATION)
        return "preauthorization";
      if (code == Use.PREDETERMINATION)
        return "predetermination";
      return "?";
      }
    public String toSystem(Use code) {
      return code.getSystem();
      }
    }

    public enum RemittanceOutcome {
        /**
         * The Claim/Pre-authorization/Pre-determination has been received but processing has not begun.
         */
        QUEUED, 
        /**
         * The processing has completed without errors
         */
        COMPLETE, 
        /**
         * One or more errors have been detected in the Claim
         */
        ERROR, 
        /**
         * No errors have been detected in the Claim and some of the adjudication has been performed.
         */
        PARTIAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static RemittanceOutcome fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("queued".equals(codeString))
          return QUEUED;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("error".equals(codeString))
          return ERROR;
        if ("partial".equals(codeString))
          return PARTIAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown RemittanceOutcome code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case QUEUED: return "queued";
            case COMPLETE: return "complete";
            case ERROR: return "error";
            case PARTIAL: return "partial";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case QUEUED: return "http://hl7.org/fhir/remittance-outcome";
            case COMPLETE: return "http://hl7.org/fhir/remittance-outcome";
            case ERROR: return "http://hl7.org/fhir/remittance-outcome";
            case PARTIAL: return "http://hl7.org/fhir/remittance-outcome";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case QUEUED: return "The Claim/Pre-authorization/Pre-determination has been received but processing has not begun.";
            case COMPLETE: return "The processing has completed without errors";
            case ERROR: return "One or more errors have been detected in the Claim";
            case PARTIAL: return "No errors have been detected in the Claim and some of the adjudication has been performed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case QUEUED: return "Queued";
            case COMPLETE: return "Processing Complete";
            case ERROR: return "Error";
            case PARTIAL: return "Partial Processing";
            default: return "?";
          }
        }
    }

  public static class RemittanceOutcomeEnumFactory implements EnumFactory<RemittanceOutcome> {
    public RemittanceOutcome fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("queued".equals(codeString))
          return RemittanceOutcome.QUEUED;
        if ("complete".equals(codeString))
          return RemittanceOutcome.COMPLETE;
        if ("error".equals(codeString))
          return RemittanceOutcome.ERROR;
        if ("partial".equals(codeString))
          return RemittanceOutcome.PARTIAL;
        throw new IllegalArgumentException("Unknown RemittanceOutcome code '"+codeString+"'");
        }
        public Enumeration<RemittanceOutcome> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<RemittanceOutcome>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("queued".equals(codeString))
          return new Enumeration<RemittanceOutcome>(this, RemittanceOutcome.QUEUED);
        if ("complete".equals(codeString))
          return new Enumeration<RemittanceOutcome>(this, RemittanceOutcome.COMPLETE);
        if ("error".equals(codeString))
          return new Enumeration<RemittanceOutcome>(this, RemittanceOutcome.ERROR);
        if ("partial".equals(codeString))
          return new Enumeration<RemittanceOutcome>(this, RemittanceOutcome.PARTIAL);
        throw new FHIRException("Unknown RemittanceOutcome code '"+codeString+"'");
        }
    public String toCode(RemittanceOutcome code) {
      if (code == RemittanceOutcome.QUEUED)
        return "queued";
      if (code == RemittanceOutcome.COMPLETE)
        return "complete";
      if (code == RemittanceOutcome.ERROR)
        return "error";
      if (code == RemittanceOutcome.PARTIAL)
        return "partial";
      return "?";
      }
    public String toSystem(RemittanceOutcome code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely reference the claim item entries.
         */
        @Child(name = "itemSequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Claim item instance identifier", formalDefinition="A number to uniquely reference the claim item entries." )
        protected PositiveIntType itemSequence;

        /**
         * The numbers associated with notes below which apply to the adjudication of this item.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable note numbers", formalDefinition="The numbers associated with notes below which apply to the adjudication of this item." )
        protected List<PositiveIntType> noteNumber;

        /**
         * If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a simple product or service then this is the result of the adjudication of this item.
         */
        @Child(name = "adjudication", type = {}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication details", formalDefinition="If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a simple product or service then this is the result of the adjudication of this item." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items.
         */
        @Child(name = "detail", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication for claim details", formalDefinition="A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items." )
        protected List<ItemDetailComponent> detail;

        private static final long serialVersionUID = 701277928L;

    /**
     * Constructor
     */
      public ItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemComponent(PositiveIntType itemSequence) {
        super();
        this.itemSequence = itemSequence;
      }

        /**
         * @return {@link #itemSequence} (A number to uniquely reference the claim item entries.). This is the underlying object with id, value and extensions. The accessor "getItemSequence" gives direct access to the value
         */
        public PositiveIntType getItemSequenceElement() { 
          if (this.itemSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemComponent.itemSequence");
            else if (Configuration.doAutoCreate())
              this.itemSequence = new PositiveIntType(); // bb
          return this.itemSequence;
        }

        public boolean hasItemSequenceElement() { 
          return this.itemSequence != null && !this.itemSequence.isEmpty();
        }

        public boolean hasItemSequence() { 
          return this.itemSequence != null && !this.itemSequence.isEmpty();
        }

        /**
         * @param value {@link #itemSequence} (A number to uniquely reference the claim item entries.). This is the underlying object with id, value and extensions. The accessor "getItemSequence" gives direct access to the value
         */
        public ItemComponent setItemSequenceElement(PositiveIntType value) { 
          this.itemSequence = value;
          return this;
        }

        /**
         * @return A number to uniquely reference the claim item entries.
         */
        public int getItemSequence() { 
          return this.itemSequence == null || this.itemSequence.isEmpty() ? 0 : this.itemSequence.getValue();
        }

        /**
         * @param value A number to uniquely reference the claim item entries.
         */
        public ItemComponent setItemSequence(int value) { 
            if (this.itemSequence == null)
              this.itemSequence = new PositiveIntType();
            this.itemSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
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
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public PositiveIntType addNoteNumberElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
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
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public boolean hasNoteNumber(int value) { 
          if (this.noteNumber == null)
            return false;
          for (PositiveIntType v : this.noteNumber)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a simple product or service then this is the result of the adjudication of this item.)
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
         * @return {@link #detail} (A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items.)
         */
        public List<ItemDetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<ItemDetailComponent>();
          return this.detail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemComponent setDetail(List<ItemDetailComponent> theDetail) { 
          this.detail = theDetail;
          return this;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (ItemDetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ItemDetailComponent addDetail() { //3
          ItemDetailComponent t = new ItemDetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<ItemDetailComponent>();
          this.detail.add(t);
          return t;
        }

        public ItemComponent addDetail(ItemDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<ItemDetailComponent>();
          this.detail.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #detail}, creating it if it does not already exist
         */
        public ItemDetailComponent getDetailFirstRep() { 
          if (getDetail().isEmpty()) {
            addDetail();
          }
          return getDetail().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("itemSequence", "positiveInt", "A number to uniquely reference the claim item entries.", 0, 1, itemSequence));
          children.add(new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "", "If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a simple product or service then this is the result of the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          children.add(new Property("detail", "", "A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1977979892: /*itemSequence*/  return new Property("itemSequence", "positiveInt", "A number to uniquely reference the claim item entries.", 0, 1, itemSequence);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "", "If this item is a group then the values here are a summary of the adjudication of the detail items. If this item is a simple product or service then this is the result of the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, adjudication);
          case -1335224239: /*detail*/  return new Property("detail", "", "A claim detail. Either a simple (a product or service) or a 'group' of sub-details which are simple items.", 0, java.lang.Integer.MAX_VALUE, detail);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1977979892: /*itemSequence*/ return this.itemSequence == null ? new Base[0] : new Base[] {this.itemSequence}; // PositiveIntType
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // ItemDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1977979892: // itemSequence
          this.itemSequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        case -1335224239: // detail
          this.getDetail().add((ItemDetailComponent) value); // ItemDetailComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("itemSequence")) {
          this.itemSequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else if (name.equals("detail")) {
          this.getDetail().add((ItemDetailComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1977979892:  return getItemSequenceElement();
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        case -1335224239:  return addDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1977979892: /*itemSequence*/ return new String[] {"positiveInt"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {};
        case -1335224239: /*detail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("itemSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.itemSequence");
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

      public ItemComponent copy() {
        ItemComponent dst = new ItemComponent();
        copyValues(dst);
        dst.itemSequence = itemSequence == null ? null : itemSequence.copy();
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
          dst.detail = new ArrayList<ItemDetailComponent>();
          for (ItemDetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ItemComponent))
          return false;
        ItemComponent o = (ItemComponent) other_;
        return compareDeep(itemSequence, o.itemSequence, true) && compareDeep(noteNumber, o.noteNumber, true)
           && compareDeep(adjudication, o.adjudication, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ItemComponent))
          return false;
        ItemComponent o = (ItemComponent) other_;
        return compareValues(itemSequence, o.itemSequence, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(itemSequence, noteNumber, adjudication
          , detail);
      }

  public String fhirType() {
    return "ClaimResponse.item";

  }

  }

    @Block()
    public static class AdjudicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code to indicate the information type of this adjudication record. Information types may include the value submitted, maximum values or percentages allowed or payable under the plan, amounts that: the patient is responsible for in aggregate or pertaining to this item; amounts paid by other coverages; and, the benefit payable for this item.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of adjudication information", formalDefinition="A code to indicate the information type of this adjudication record. Information types may include the value submitted, maximum values or percentages allowed or payable under the plan, amounts that: the patient is responsible for in aggregate or pertaining to this item; amounts paid by other coverages; and, the benefit payable for this item." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication")
        protected CodeableConcept category;

        /**
         * A code supporting the understanding of the adjudication result and explaining variance from expected amount.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Explanation of adjudication outcome", formalDefinition="A code supporting the understanding of the adjudication result and explaining variance from expected amount." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication-reason")
        protected CodeableConcept reason;

        /**
         * Monetary amount associated with the category.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monetary amount associated with the category." )
        protected Money amount;

        /**
         * A non-monetary value associated with the category. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Non-monetary value", formalDefinition="A non-monetary value associated with the category. Mutually exclusive to the amount element above." )
        protected DecimalType value;

        private static final long serialVersionUID = 1559898786L;

    /**
     * Constructor
     */
      public AdjudicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AdjudicationComponent(CodeableConcept category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (A code to indicate the information type of this adjudication record. Information types may include the value submitted, maximum values or percentages allowed or payable under the plan, amounts that: the patient is responsible for in aggregate or pertaining to this item; amounts paid by other coverages; and, the benefit payable for this item.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdjudicationComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (A code to indicate the information type of this adjudication record. Information types may include the value submitted, maximum values or percentages allowed or payable under the plan, amounts that: the patient is responsible for in aggregate or pertaining to this item; amounts paid by other coverages; and, the benefit payable for this item.)
         */
        public AdjudicationComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (A code supporting the understanding of the adjudication result and explaining variance from expected amount.)
         */
        public CodeableConcept getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdjudicationComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new CodeableConcept(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (A code supporting the understanding of the adjudication result and explaining variance from expected amount.)
         */
        public AdjudicationComponent setReason(CodeableConcept value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monetary amount associated with the category.)
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
         * @param value {@link #amount} (Monetary amount associated with the category.)
         */
        public AdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value associated with the category. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
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
         * @param value {@link #value} (A non-monetary value associated with the category. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public AdjudicationComponent setValueElement(DecimalType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return A non-monetary value associated with the category. Mutually exclusive to the amount element above.
         */
        public BigDecimal getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value A non-monetary value associated with the category. Mutually exclusive to the amount element above.
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
         * @param value A non-monetary value associated with the category. Mutually exclusive to the amount element above.
         */
        public AdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value associated with the category. Mutually exclusive to the amount element above.
         */
        public AdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("category", "CodeableConcept", "A code to indicate the information type of this adjudication record. Information types may include the value submitted, maximum values or percentages allowed or payable under the plan, amounts that: the patient is responsible for in aggregate or pertaining to this item; amounts paid by other coverages; and, the benefit payable for this item.", 0, 1, category));
          children.add(new Property("reason", "CodeableConcept", "A code supporting the understanding of the adjudication result and explaining variance from expected amount.", 0, 1, reason));
          children.add(new Property("amount", "Money", "Monetary amount associated with the category.", 0, 1, amount));
          children.add(new Property("value", "decimal", "A non-monetary value associated with the category. Mutually exclusive to the amount element above.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "A code to indicate the information type of this adjudication record. Information types may include the value submitted, maximum values or percentages allowed or payable under the plan, amounts that: the patient is responsible for in aggregate or pertaining to this item; amounts paid by other coverages; and, the benefit payable for this item.", 0, 1, category);
          case -934964668: /*reason*/  return new Property("reason", "CodeableConcept", "A code supporting the understanding of the adjudication result and explaining variance from expected amount.", 0, 1, reason);
          case -1413853096: /*amount*/  return new Property("amount", "Money", "Monetary amount associated with the category.", 0, 1, amount);
          case 111972721: /*value*/  return new Property("value", "decimal", "A non-monetary value associated with the category. Mutually exclusive to the amount element above.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // DecimalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -934964668: // reason
          this.reason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          return value;
        case 111972721: // value
          this.value = castToDecimal(value); // DecimalType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("reason")) {
          this.reason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount")) {
          this.amount = castToMoney(value); // Money
        } else if (name.equals("value")) {
          this.value = castToDecimal(value); // DecimalType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case -934964668:  return getReason(); 
        case -1413853096:  return getAmount(); 
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case -934964668: /*reason*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"Money"};
        case 111972721: /*value*/ return new String[] {"decimal"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("reason")) {
          this.reason = new CodeableConcept();
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AdjudicationComponent))
          return false;
        AdjudicationComponent o = (AdjudicationComponent) other_;
        return compareDeep(category, o.category, true) && compareDeep(reason, o.reason, true) && compareDeep(amount, o.amount, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AdjudicationComponent))
          return false;
        AdjudicationComponent o = (AdjudicationComponent) other_;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, reason, amount
          , value);
      }

  public String fhirType() {
    return "ClaimResponse.item.adjudication";

  }

  }

    @Block()
    public static class ItemDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely reference the claim detail entry.
         */
        @Child(name = "detailSequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Claim detail instance identifier", formalDefinition="A number to uniquely reference the claim detail entry." )
        protected PositiveIntType detailSequence;

        /**
         * The numbers associated with notes below which apply to the adjudication of this item.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable note numbers", formalDefinition="The numbers associated with notes below which apply to the adjudication of this item." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudication results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Detail level adjudication details", formalDefinition="The adjudication results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * A sub-detail adjudication of a simple product or service.
         */
        @Child(name = "subDetail", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication for claim sub-details", formalDefinition="A sub-detail adjudication of a simple product or service." )
        protected List<SubDetailComponent> subDetail;

        private static final long serialVersionUID = 1066636111L;

    /**
     * Constructor
     */
      public ItemDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ItemDetailComponent(PositiveIntType detailSequence) {
        super();
        this.detailSequence = detailSequence;
      }

        /**
         * @return {@link #detailSequence} (A number to uniquely reference the claim detail entry.). This is the underlying object with id, value and extensions. The accessor "getDetailSequence" gives direct access to the value
         */
        public PositiveIntType getDetailSequenceElement() { 
          if (this.detailSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemDetailComponent.detailSequence");
            else if (Configuration.doAutoCreate())
              this.detailSequence = new PositiveIntType(); // bb
          return this.detailSequence;
        }

        public boolean hasDetailSequenceElement() { 
          return this.detailSequence != null && !this.detailSequence.isEmpty();
        }

        public boolean hasDetailSequence() { 
          return this.detailSequence != null && !this.detailSequence.isEmpty();
        }

        /**
         * @param value {@link #detailSequence} (A number to uniquely reference the claim detail entry.). This is the underlying object with id, value and extensions. The accessor "getDetailSequence" gives direct access to the value
         */
        public ItemDetailComponent setDetailSequenceElement(PositiveIntType value) { 
          this.detailSequence = value;
          return this;
        }

        /**
         * @return A number to uniquely reference the claim detail entry.
         */
        public int getDetailSequence() { 
          return this.detailSequence == null || this.detailSequence.isEmpty() ? 0 : this.detailSequence.getValue();
        }

        /**
         * @param value A number to uniquely reference the claim detail entry.
         */
        public ItemDetailComponent setDetailSequence(int value) { 
            if (this.detailSequence == null)
              this.detailSequence = new PositiveIntType();
            this.detailSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemDetailComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
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
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public PositiveIntType addNoteNumberElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public ItemDetailComponent addNoteNumber(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public boolean hasNoteNumber(int value) { 
          if (this.noteNumber == null)
            return false;
          for (PositiveIntType v : this.noteNumber)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudication results.)
         */
        public List<AdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          return this.adjudication;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemDetailComponent setAdjudication(List<AdjudicationComponent> theAdjudication) { 
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

        public ItemDetailComponent addAdjudication(AdjudicationComponent t) { //3
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
         * @return {@link #subDetail} (A sub-detail adjudication of a simple product or service.)
         */
        public List<SubDetailComponent> getSubDetail() { 
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          return this.subDetail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ItemDetailComponent setSubDetail(List<SubDetailComponent> theSubDetail) { 
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

        public ItemDetailComponent addSubDetail(SubDetailComponent t) { //3
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("detailSequence", "positiveInt", "A number to uniquely reference the claim detail entry.", 0, 1, detailSequence));
          children.add(new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          children.add(new Property("subDetail", "", "A sub-detail adjudication of a simple product or service.", 0, java.lang.Integer.MAX_VALUE, subDetail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1321472818: /*detailSequence*/  return new Property("detailSequence", "positiveInt", "A number to uniquely reference the claim detail entry.", 0, 1, detailSequence);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication);
          case -828829007: /*subDetail*/  return new Property("subDetail", "", "A sub-detail adjudication of a simple product or service.", 0, java.lang.Integer.MAX_VALUE, subDetail);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1321472818: /*detailSequence*/ return this.detailSequence == null ? new Base[0] : new Base[] {this.detailSequence}; // PositiveIntType
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        case -828829007: /*subDetail*/ return this.subDetail == null ? new Base[0] : this.subDetail.toArray(new Base[this.subDetail.size()]); // SubDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1321472818: // detailSequence
          this.detailSequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        case -828829007: // subDetail
          this.getSubDetail().add((SubDetailComponent) value); // SubDetailComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("detailSequence")) {
          this.detailSequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else if (name.equals("subDetail")) {
          this.getSubDetail().add((SubDetailComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1321472818:  return getDetailSequenceElement();
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        case -828829007:  return addSubDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1321472818: /*detailSequence*/ return new String[] {"positiveInt"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ClaimResponse.item.adjudication"};
        case -828829007: /*subDetail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("detailSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.detailSequence");
        }
        else if (name.equals("noteNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.noteNumber");
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
        dst.detailSequence = detailSequence == null ? null : detailSequence.copy();
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
        if (subDetail != null) {
          dst.subDetail = new ArrayList<SubDetailComponent>();
          for (SubDetailComponent i : subDetail)
            dst.subDetail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ItemDetailComponent))
          return false;
        ItemDetailComponent o = (ItemDetailComponent) other_;
        return compareDeep(detailSequence, o.detailSequence, true) && compareDeep(noteNumber, o.noteNumber, true)
           && compareDeep(adjudication, o.adjudication, true) && compareDeep(subDetail, o.subDetail, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ItemDetailComponent))
          return false;
        ItemDetailComponent o = (ItemDetailComponent) other_;
        return compareValues(detailSequence, o.detailSequence, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(detailSequence, noteNumber
          , adjudication, subDetail);
      }

  public String fhirType() {
    return "ClaimResponse.item.detail";

  }

  }

    @Block()
    public static class SubDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely reference the claim sub-detail entry.
         */
        @Child(name = "subDetailSequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Claim sub-detail instance identifier", formalDefinition="A number to uniquely reference the claim sub-detail entry." )
        protected PositiveIntType subDetailSequence;

        /**
         * The numbers associated with notes below which apply to the adjudication of this item.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable note numbers", formalDefinition="The numbers associated with notes below which apply to the adjudication of this item." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudication results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Subdetail level adjudication details", formalDefinition="The adjudication results." )
        protected List<AdjudicationComponent> adjudication;

        private static final long serialVersionUID = -1083724362L;

    /**
     * Constructor
     */
      public SubDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubDetailComponent(PositiveIntType subDetailSequence) {
        super();
        this.subDetailSequence = subDetailSequence;
      }

        /**
         * @return {@link #subDetailSequence} (A number to uniquely reference the claim sub-detail entry.). This is the underlying object with id, value and extensions. The accessor "getSubDetailSequence" gives direct access to the value
         */
        public PositiveIntType getSubDetailSequenceElement() { 
          if (this.subDetailSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.subDetailSequence");
            else if (Configuration.doAutoCreate())
              this.subDetailSequence = new PositiveIntType(); // bb
          return this.subDetailSequence;
        }

        public boolean hasSubDetailSequenceElement() { 
          return this.subDetailSequence != null && !this.subDetailSequence.isEmpty();
        }

        public boolean hasSubDetailSequence() { 
          return this.subDetailSequence != null && !this.subDetailSequence.isEmpty();
        }

        /**
         * @param value {@link #subDetailSequence} (A number to uniquely reference the claim sub-detail entry.). This is the underlying object with id, value and extensions. The accessor "getSubDetailSequence" gives direct access to the value
         */
        public SubDetailComponent setSubDetailSequenceElement(PositiveIntType value) { 
          this.subDetailSequence = value;
          return this;
        }

        /**
         * @return A number to uniquely reference the claim sub-detail entry.
         */
        public int getSubDetailSequence() { 
          return this.subDetailSequence == null || this.subDetailSequence.isEmpty() ? 0 : this.subDetailSequence.getValue();
        }

        /**
         * @param value A number to uniquely reference the claim sub-detail entry.
         */
        public SubDetailComponent setSubDetailSequence(int value) { 
            if (this.subDetailSequence == null)
              this.subDetailSequence = new PositiveIntType();
            this.subDetailSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubDetailComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
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
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public PositiveIntType addNoteNumberElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public SubDetailComponent addNoteNumber(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public boolean hasNoteNumber(int value) { 
          if (this.noteNumber == null)
            return false;
          for (PositiveIntType v : this.noteNumber)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudication results.)
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("subDetailSequence", "positiveInt", "A number to uniquely reference the claim sub-detail entry.", 0, 1, subDetailSequence));
          children.add(new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -855462510: /*subDetailSequence*/  return new Property("subDetailSequence", "positiveInt", "A number to uniquely reference the claim sub-detail entry.", 0, 1, subDetailSequence);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -855462510: /*subDetailSequence*/ return this.subDetailSequence == null ? new Base[0] : new Base[] {this.subDetailSequence}; // PositiveIntType
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -855462510: // subDetailSequence
          this.subDetailSequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("subDetailSequence")) {
          this.subDetailSequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -855462510:  return getSubDetailSequenceElement();
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -855462510: /*subDetailSequence*/ return new String[] {"positiveInt"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ClaimResponse.item.adjudication"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("subDetailSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.subDetailSequence");
        }
        else if (name.equals("noteNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.noteNumber");
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
        dst.subDetailSequence = subDetailSequence == null ? null : subDetailSequence.copy();
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
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubDetailComponent))
          return false;
        SubDetailComponent o = (SubDetailComponent) other_;
        return compareDeep(subDetailSequence, o.subDetailSequence, true) && compareDeep(noteNumber, o.noteNumber, true)
           && compareDeep(adjudication, o.adjudication, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubDetailComponent))
          return false;
        SubDetailComponent o = (SubDetailComponent) other_;
        return compareValues(subDetailSequence, o.subDetailSequence, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(subDetailSequence, noteNumber
          , adjudication);
      }

  public String fhirType() {
    return "ClaimResponse.item.detail.subDetail";

  }

  }

    @Block()
    public static class AddedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Claim items which this service line is intended to replace.
         */
        @Child(name = "itemSequence", type = {PositiveIntType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Item sequence number", formalDefinition="Claim items which this service line is intended to replace." )
        protected List<PositiveIntType> itemSequence;

        /**
         * The sequence number of the details within the claim item which this line is intended to replace.
         */
        @Child(name = "detailSequence", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Detail sequence number", formalDefinition="The sequence number of the details within the claim item which this line is intended to replace." )
        protected List<PositiveIntType> detailSequence;

        /**
         * The sequence number of the sub-details within the details within the claim item which this line is intended to replace.
         */
        @Child(name = "subdetailSequence", type = {PositiveIntType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Subdetail sequence number", formalDefinition="The sequence number of the sub-details within the details within the claim item which this line is intended to replace." )
        protected List<PositiveIntType> subdetailSequence;

        /**
         * The providers who are authorized for the services rendered to the patient.
         */
        @Child(name = "provider", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Authorized providers", formalDefinition="The providers who are authorized for the services rendered to the patient." )
        protected List<Reference> provider;
        /**
         * The actual objects that are the target of the reference (The providers who are authorized for the services rendered to the patient.)
         */
        protected List<Resource> providerTarget;


        /**
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.
         */
        @Child(name = "productOrService", type = {CodeableConcept.class}, order=5, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing, service, product, or drug code", formalDefinition="When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept productOrService;

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes to convey additional context for the product or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * Identifies the program under which this may be recovered.
         */
        @Child(name = "programCode", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Program the product or service is provided under", formalDefinition="Identifies the program under which this may be recovered." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-program-code")
        protected List<CodeableConcept> programCode;

        /**
         * The date or dates when the service or product was supplied, performed or completed.
         */
        @Child(name = "serviced", type = {DateType.class, Period.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Date or dates of service or product delivery", formalDefinition="The date or dates when the service or product was supplied, performed or completed." )
        protected Type serviced;

        /**
         * Where the product or service was provided.
         */
        @Child(name = "location", type = {CodeableConcept.class, Address.class, Location.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Place of service or where product was supplied", formalDefinition="Where the product or service was provided." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-place")
        protected Type location;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of products or services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per item", formalDefinition="If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * The quantity times the unit price for an additional service or product or charge.
         */
        @Child(name = "net", type = {Money.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an additional service or product or charge." )
        protected Money net;

        /**
         * Physical service site on the patient (limb, tooth, etc.).
         */
        @Child(name = "bodySite", type = {CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Anatomical location", formalDefinition="Physical service site on the patient (limb, tooth, etc.)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/tooth")
        protected CodeableConcept bodySite;

        /**
         * A region or surface of the bodySite, e.g. limb region or tooth surface(s).
         */
        @Child(name = "subSite", type = {CodeableConcept.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Anatomical sub-location", formalDefinition="A region or surface of the bodySite, e.g. limb region or tooth surface(s)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/surface")
        protected List<CodeableConcept> subSite;

        /**
         * The numbers associated with notes below which apply to the adjudication of this item.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable note numbers", formalDefinition="The numbers associated with notes below which apply to the adjudication of this item." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudication results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=17, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Added items adjudication", formalDefinition="The adjudication results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * The second-tier service adjudications for payor added services.
         */
        @Child(name = "detail", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Insurer added line details", formalDefinition="The second-tier service adjudications for payor added services." )
        protected List<AddedItemDetailComponent> detail;

        private static final long serialVersionUID = -1193747282L;

    /**
     * Constructor
     */
      public AddedItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AddedItemComponent(CodeableConcept productOrService) {
        super();
        this.productOrService = productOrService;
      }

        /**
         * @return {@link #itemSequence} (Claim items which this service line is intended to replace.)
         */
        public List<PositiveIntType> getItemSequence() { 
          if (this.itemSequence == null)
            this.itemSequence = new ArrayList<PositiveIntType>();
          return this.itemSequence;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setItemSequence(List<PositiveIntType> theItemSequence) { 
          this.itemSequence = theItemSequence;
          return this;
        }

        public boolean hasItemSequence() { 
          if (this.itemSequence == null)
            return false;
          for (PositiveIntType item : this.itemSequence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #itemSequence} (Claim items which this service line is intended to replace.)
         */
        public PositiveIntType addItemSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.itemSequence == null)
            this.itemSequence = new ArrayList<PositiveIntType>();
          this.itemSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #itemSequence} (Claim items which this service line is intended to replace.)
         */
        public AddedItemComponent addItemSequence(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.itemSequence == null)
            this.itemSequence = new ArrayList<PositiveIntType>();
          this.itemSequence.add(t);
          return this;
        }

        /**
         * @param value {@link #itemSequence} (Claim items which this service line is intended to replace.)
         */
        public boolean hasItemSequence(int value) { 
          if (this.itemSequence == null)
            return false;
          for (PositiveIntType v : this.itemSequence)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #detailSequence} (The sequence number of the details within the claim item which this line is intended to replace.)
         */
        public List<PositiveIntType> getDetailSequence() { 
          if (this.detailSequence == null)
            this.detailSequence = new ArrayList<PositiveIntType>();
          return this.detailSequence;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setDetailSequence(List<PositiveIntType> theDetailSequence) { 
          this.detailSequence = theDetailSequence;
          return this;
        }

        public boolean hasDetailSequence() { 
          if (this.detailSequence == null)
            return false;
          for (PositiveIntType item : this.detailSequence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detailSequence} (The sequence number of the details within the claim item which this line is intended to replace.)
         */
        public PositiveIntType addDetailSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.detailSequence == null)
            this.detailSequence = new ArrayList<PositiveIntType>();
          this.detailSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #detailSequence} (The sequence number of the details within the claim item which this line is intended to replace.)
         */
        public AddedItemComponent addDetailSequence(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.detailSequence == null)
            this.detailSequence = new ArrayList<PositiveIntType>();
          this.detailSequence.add(t);
          return this;
        }

        /**
         * @param value {@link #detailSequence} (The sequence number of the details within the claim item which this line is intended to replace.)
         */
        public boolean hasDetailSequence(int value) { 
          if (this.detailSequence == null)
            return false;
          for (PositiveIntType v : this.detailSequence)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #subdetailSequence} (The sequence number of the sub-details within the details within the claim item which this line is intended to replace.)
         */
        public List<PositiveIntType> getSubdetailSequence() { 
          if (this.subdetailSequence == null)
            this.subdetailSequence = new ArrayList<PositiveIntType>();
          return this.subdetailSequence;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setSubdetailSequence(List<PositiveIntType> theSubdetailSequence) { 
          this.subdetailSequence = theSubdetailSequence;
          return this;
        }

        public boolean hasSubdetailSequence() { 
          if (this.subdetailSequence == null)
            return false;
          for (PositiveIntType item : this.subdetailSequence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #subdetailSequence} (The sequence number of the sub-details within the details within the claim item which this line is intended to replace.)
         */
        public PositiveIntType addSubdetailSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.subdetailSequence == null)
            this.subdetailSequence = new ArrayList<PositiveIntType>();
          this.subdetailSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #subdetailSequence} (The sequence number of the sub-details within the details within the claim item which this line is intended to replace.)
         */
        public AddedItemComponent addSubdetailSequence(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.subdetailSequence == null)
            this.subdetailSequence = new ArrayList<PositiveIntType>();
          this.subdetailSequence.add(t);
          return this;
        }

        /**
         * @param value {@link #subdetailSequence} (The sequence number of the sub-details within the details within the claim item which this line is intended to replace.)
         */
        public boolean hasSubdetailSequence(int value) { 
          if (this.subdetailSequence == null)
            return false;
          for (PositiveIntType v : this.subdetailSequence)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #provider} (The providers who are authorized for the services rendered to the patient.)
         */
        public List<Reference> getProvider() { 
          if (this.provider == null)
            this.provider = new ArrayList<Reference>();
          return this.provider;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setProvider(List<Reference> theProvider) { 
          this.provider = theProvider;
          return this;
        }

        public boolean hasProvider() { 
          if (this.provider == null)
            return false;
          for (Reference item : this.provider)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addProvider() { //3
          Reference t = new Reference();
          if (this.provider == null)
            this.provider = new ArrayList<Reference>();
          this.provider.add(t);
          return t;
        }

        public AddedItemComponent addProvider(Reference t) { //3
          if (t == null)
            return this;
          if (this.provider == null)
            this.provider = new ArrayList<Reference>();
          this.provider.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #provider}, creating it if it does not already exist
         */
        public Reference getProviderFirstRep() { 
          if (getProvider().isEmpty()) {
            addProvider();
          }
          return getProvider().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getProviderTarget() { 
          if (this.providerTarget == null)
            this.providerTarget = new ArrayList<Resource>();
          return this.providerTarget;
        }

        /**
         * @return {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public CodeableConcept getProductOrService() { 
          if (this.productOrService == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.productOrService");
            else if (Configuration.doAutoCreate())
              this.productOrService = new CodeableConcept(); // cc
          return this.productOrService;
        }

        public boolean hasProductOrService() { 
          return this.productOrService != null && !this.productOrService.isEmpty();
        }

        /**
         * @param value {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public AddedItemComponent setProductOrService(CodeableConcept value) { 
          this.productOrService = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes to convey additional context for the product or service.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setModifier(List<CodeableConcept> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (CodeableConcept item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addModifier() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return t;
        }

        public AddedItemComponent addModifier(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public CodeableConcept getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #programCode} (Identifies the program under which this may be recovered.)
         */
        public List<CodeableConcept> getProgramCode() { 
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          return this.programCode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setProgramCode(List<CodeableConcept> theProgramCode) { 
          this.programCode = theProgramCode;
          return this;
        }

        public boolean hasProgramCode() { 
          if (this.programCode == null)
            return false;
          for (CodeableConcept item : this.programCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addProgramCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          this.programCode.add(t);
          return t;
        }

        public AddedItemComponent addProgramCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.programCode == null)
            this.programCode = new ArrayList<CodeableConcept>();
          this.programCode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #programCode}, creating it if it does not already exist
         */
        public CodeableConcept getProgramCodeFirstRep() { 
          if (getProgramCode().isEmpty()) {
            addProgramCode();
          }
          return getProgramCode().get(0);
        }

        /**
         * @return {@link #serviced} (The date or dates when the service or product was supplied, performed or completed.)
         */
        public Type getServiced() { 
          return this.serviced;
        }

        /**
         * @return {@link #serviced} (The date or dates when the service or product was supplied, performed or completed.)
         */
        public DateType getServicedDateType() throws FHIRException { 
          if (this.serviced == null)
            this.serviced = new DateType();
          if (!(this.serviced instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.serviced.getClass().getName()+" was encountered");
          return (DateType) this.serviced;
        }

        public boolean hasServicedDateType() { 
          return this != null && this.serviced instanceof DateType;
        }

        /**
         * @return {@link #serviced} (The date or dates when the service or product was supplied, performed or completed.)
         */
        public Period getServicedPeriod() throws FHIRException { 
          if (this.serviced == null)
            this.serviced = new Period();
          if (!(this.serviced instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.serviced.getClass().getName()+" was encountered");
          return (Period) this.serviced;
        }

        public boolean hasServicedPeriod() { 
          return this != null && this.serviced instanceof Period;
        }

        public boolean hasServiced() { 
          return this.serviced != null && !this.serviced.isEmpty();
        }

        /**
         * @param value {@link #serviced} (The date or dates when the service or product was supplied, performed or completed.)
         */
        public AddedItemComponent setServiced(Type value) { 
          if (value != null && !(value instanceof DateType || value instanceof Period))
            throw new Error("Not the right type for ClaimResponse.addItem.serviced[x]: "+value.fhirType());
          this.serviced = value;
          return this;
        }

        /**
         * @return {@link #location} (Where the product or service was provided.)
         */
        public Type getLocation() { 
          return this.location;
        }

        /**
         * @return {@link #location} (Where the product or service was provided.)
         */
        public CodeableConcept getLocationCodeableConcept() throws FHIRException { 
          if (this.location == null)
            this.location = new CodeableConcept();
          if (!(this.location instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.location.getClass().getName()+" was encountered");
          return (CodeableConcept) this.location;
        }

        public boolean hasLocationCodeableConcept() { 
          return this != null && this.location instanceof CodeableConcept;
        }

        /**
         * @return {@link #location} (Where the product or service was provided.)
         */
        public Address getLocationAddress() throws FHIRException { 
          if (this.location == null)
            this.location = new Address();
          if (!(this.location instanceof Address))
            throw new FHIRException("Type mismatch: the type Address was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Address) this.location;
        }

        public boolean hasLocationAddress() { 
          return this != null && this.location instanceof Address;
        }

        /**
         * @return {@link #location} (Where the product or service was provided.)
         */
        public Reference getLocationReference() throws FHIRException { 
          if (this.location == null)
            this.location = new Reference();
          if (!(this.location instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.location.getClass().getName()+" was encountered");
          return (Reference) this.location;
        }

        public boolean hasLocationReference() { 
          return this != null && this.location instanceof Reference;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (Where the product or service was provided.)
         */
        public AddedItemComponent setLocation(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Address || value instanceof Reference))
            throw new Error("Not the right type for ClaimResponse.addItem.location[x]: "+value.fhirType());
          this.location = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.quantity");
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
        public AddedItemComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
         */
        public AddedItemComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.factor");
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
        public AddedItemComponent setFactorElement(DecimalType value) { 
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
        public AddedItemComponent setFactor(BigDecimal value) { 
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
        public AddedItemComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public AddedItemComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an additional service or product or charge.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an additional service or product or charge.)
         */
        public AddedItemComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #bodySite} (Physical service site on the patient (limb, tooth, etc.).)
         */
        public CodeableConcept getBodySite() { 
          if (this.bodySite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.bodySite");
            else if (Configuration.doAutoCreate())
              this.bodySite = new CodeableConcept(); // cc
          return this.bodySite;
        }

        public boolean hasBodySite() { 
          return this.bodySite != null && !this.bodySite.isEmpty();
        }

        /**
         * @param value {@link #bodySite} (Physical service site on the patient (limb, tooth, etc.).)
         */
        public AddedItemComponent setBodySite(CodeableConcept value) { 
          this.bodySite = value;
          return this;
        }

        /**
         * @return {@link #subSite} (A region or surface of the bodySite, e.g. limb region or tooth surface(s).)
         */
        public List<CodeableConcept> getSubSite() { 
          if (this.subSite == null)
            this.subSite = new ArrayList<CodeableConcept>();
          return this.subSite;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setSubSite(List<CodeableConcept> theSubSite) { 
          this.subSite = theSubSite;
          return this;
        }

        public boolean hasSubSite() { 
          if (this.subSite == null)
            return false;
          for (CodeableConcept item : this.subSite)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addSubSite() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.subSite == null)
            this.subSite = new ArrayList<CodeableConcept>();
          this.subSite.add(t);
          return t;
        }

        public AddedItemComponent addSubSite(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.subSite == null)
            this.subSite = new ArrayList<CodeableConcept>();
          this.subSite.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #subSite}, creating it if it does not already exist
         */
        public CodeableConcept getSubSiteFirstRep() { 
          if (getSubSite().isEmpty()) {
            addSubSite();
          }
          return getSubSite().get(0);
        }

        /**
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
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
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public PositiveIntType addNoteNumberElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public AddedItemComponent addNoteNumber(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public boolean hasNoteNumber(int value) { 
          if (this.noteNumber == null)
            return false;
          for (PositiveIntType v : this.noteNumber)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudication results.)
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
         * @return {@link #detail} (The second-tier service adjudications for payor added services.)
         */
        public List<AddedItemDetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemDetailComponent>();
          return this.detail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemComponent setDetail(List<AddedItemDetailComponent> theDetail) { 
          this.detail = theDetail;
          return this;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (AddedItemDetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AddedItemDetailComponent addDetail() { //3
          AddedItemDetailComponent t = new AddedItemDetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemDetailComponent>();
          this.detail.add(t);
          return t;
        }

        public AddedItemComponent addDetail(AddedItemDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<AddedItemDetailComponent>();
          this.detail.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #detail}, creating it if it does not already exist
         */
        public AddedItemDetailComponent getDetailFirstRep() { 
          if (getDetail().isEmpty()) {
            addDetail();
          }
          return getDetail().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("itemSequence", "positiveInt", "Claim items which this service line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, itemSequence));
          children.add(new Property("detailSequence", "positiveInt", "The sequence number of the details within the claim item which this line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, detailSequence));
          children.add(new Property("subdetailSequence", "positiveInt", "The sequence number of the sub-details within the details within the claim item which this line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, subdetailSequence));
          children.add(new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The providers who are authorized for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
          children.add(new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService));
          children.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier));
          children.add(new Property("programCode", "CodeableConcept", "Identifies the program under which this may be recovered.", 0, java.lang.Integer.MAX_VALUE, programCode));
          children.add(new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced));
          children.add(new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location));
          children.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice));
          children.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor));
          children.add(new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net));
          children.add(new Property("bodySite", "CodeableConcept", "Physical service site on the patient (limb, tooth, etc.).", 0, 1, bodySite));
          children.add(new Property("subSite", "CodeableConcept", "A region or surface of the bodySite, e.g. limb region or tooth surface(s).", 0, java.lang.Integer.MAX_VALUE, subSite));
          children.add(new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          children.add(new Property("detail", "", "The second-tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1977979892: /*itemSequence*/  return new Property("itemSequence", "positiveInt", "Claim items which this service line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, itemSequence);
          case 1321472818: /*detailSequence*/  return new Property("detailSequence", "positiveInt", "The sequence number of the details within the claim item which this line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, detailSequence);
          case 146530674: /*subdetailSequence*/  return new Property("subdetailSequence", "positiveInt", "The sequence number of the sub-details within the details within the claim item which this line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, subdetailSequence);
          case -987494927: /*provider*/  return new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The providers who are authorized for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider);
          case 1957227299: /*productOrService*/  return new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService);
          case -615513385: /*modifier*/  return new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier);
          case 1010065041: /*programCode*/  return new Property("programCode", "CodeableConcept", "Identifies the program under which this may be recovered.", 0, java.lang.Integer.MAX_VALUE, programCode);
          case -1927922223: /*serviced[x]*/  return new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced);
          case 1379209295: /*serviced*/  return new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced);
          case 363246749: /*servicedDate*/  return new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced);
          case 1534966512: /*servicedPeriod*/  return new Property("serviced[x]", "date|Period", "The date or dates when the service or product was supplied, performed or completed.", 0, 1, serviced);
          case 552316075: /*location[x]*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case 1901043637: /*location*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case -1224800468: /*locationCodeableConcept*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case -1280020865: /*locationAddress*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case 755866390: /*locationReference*/  return new Property("location[x]", "CodeableConcept|Address|Reference(Location)", "Where the product or service was provided.", 0, 1, location);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice);
          case -1282148017: /*factor*/  return new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor);
          case 108957: /*net*/  return new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net);
          case 1702620169: /*bodySite*/  return new Property("bodySite", "CodeableConcept", "Physical service site on the patient (limb, tooth, etc.).", 0, 1, bodySite);
          case -1868566105: /*subSite*/  return new Property("subSite", "CodeableConcept", "A region or surface of the bodySite, e.g. limb region or tooth surface(s).", 0, java.lang.Integer.MAX_VALUE, subSite);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication);
          case -1335224239: /*detail*/  return new Property("detail", "", "The second-tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, detail);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1977979892: /*itemSequence*/ return this.itemSequence == null ? new Base[0] : this.itemSequence.toArray(new Base[this.itemSequence.size()]); // PositiveIntType
        case 1321472818: /*detailSequence*/ return this.detailSequence == null ? new Base[0] : this.detailSequence.toArray(new Base[this.detailSequence.size()]); // PositiveIntType
        case 146530674: /*subdetailSequence*/ return this.subdetailSequence == null ? new Base[0] : this.subdetailSequence.toArray(new Base[this.subdetailSequence.size()]); // PositiveIntType
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : this.provider.toArray(new Base[this.provider.size()]); // Reference
        case 1957227299: /*productOrService*/ return this.productOrService == null ? new Base[0] : new Base[] {this.productOrService}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 1010065041: /*programCode*/ return this.programCode == null ? new Base[0] : this.programCode.toArray(new Base[this.programCode.size()]); // CodeableConcept
        case 1379209295: /*serviced*/ return this.serviced == null ? new Base[0] : new Base[] {this.serviced}; // Type
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Type
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // CodeableConcept
        case -1868566105: /*subSite*/ return this.subSite == null ? new Base[0] : this.subSite.toArray(new Base[this.subSite.size()]); // CodeableConcept
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // AddedItemDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1977979892: // itemSequence
          this.getItemSequence().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case 1321472818: // detailSequence
          this.getDetailSequence().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case 146530674: // subdetailSequence
          this.getSubdetailSequence().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -987494927: // provider
          this.getProvider().add(castToReference(value)); // Reference
          return value;
        case 1957227299: // productOrService
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1010065041: // programCode
          this.getProgramCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1379209295: // serviced
          this.serviced = castToType(value); // Type
          return value;
        case 1901043637: // location
          this.location = castToType(value); // Type
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case 108957: // net
          this.net = castToMoney(value); // Money
          return value;
        case 1702620169: // bodySite
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868566105: // subSite
          this.getSubSite().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        case -1335224239: // detail
          this.getDetail().add((AddedItemDetailComponent) value); // AddedItemDetailComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("itemSequence")) {
          this.getItemSequence().add(castToPositiveInt(value));
        } else if (name.equals("detailSequence")) {
          this.getDetailSequence().add(castToPositiveInt(value));
        } else if (name.equals("subdetailSequence")) {
          this.getSubdetailSequence().add(castToPositiveInt(value));
        } else if (name.equals("provider")) {
          this.getProvider().add(castToReference(value));
        } else if (name.equals("productOrService")) {
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("programCode")) {
          this.getProgramCode().add(castToCodeableConcept(value));
        } else if (name.equals("serviced[x]")) {
          this.serviced = castToType(value); // Type
        } else if (name.equals("location[x]")) {
          this.location = castToType(value); // Type
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else if (name.equals("bodySite")) {
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subSite")) {
          this.getSubSite().add(castToCodeableConcept(value));
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else if (name.equals("detail")) {
          this.getDetail().add((AddedItemDetailComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1977979892:  return addItemSequenceElement();
        case 1321472818:  return addDetailSequenceElement();
        case 146530674:  return addSubdetailSequenceElement();
        case -987494927:  return addProvider(); 
        case 1957227299:  return getProductOrService(); 
        case -615513385:  return addModifier(); 
        case 1010065041:  return addProgramCode(); 
        case -1927922223:  return getServiced(); 
        case 1379209295:  return getServiced(); 
        case 552316075:  return getLocation(); 
        case 1901043637:  return getLocation(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case 108957:  return getNet(); 
        case 1702620169:  return getBodySite(); 
        case -1868566105:  return addSubSite(); 
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        case -1335224239:  return addDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1977979892: /*itemSequence*/ return new String[] {"positiveInt"};
        case 1321472818: /*detailSequence*/ return new String[] {"positiveInt"};
        case 146530674: /*subdetailSequence*/ return new String[] {"positiveInt"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case 1957227299: /*productOrService*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 1010065041: /*programCode*/ return new String[] {"CodeableConcept"};
        case 1379209295: /*serviced*/ return new String[] {"date", "Period"};
        case 1901043637: /*location*/ return new String[] {"CodeableConcept", "Address", "Reference"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case -1868566105: /*subSite*/ return new String[] {"CodeableConcept"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ClaimResponse.item.adjudication"};
        case -1335224239: /*detail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("itemSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.itemSequence");
        }
        else if (name.equals("detailSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.detailSequence");
        }
        else if (name.equals("subdetailSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.subdetailSequence");
        }
        else if (name.equals("provider")) {
          return addProvider();
        }
        else if (name.equals("productOrService")) {
          this.productOrService = new CodeableConcept();
          return this.productOrService;
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
        else if (name.equals("locationCodeableConcept")) {
          this.location = new CodeableConcept();
          return this.location;
        }
        else if (name.equals("locationAddress")) {
          this.location = new Address();
          return this.location;
        }
        else if (name.equals("locationReference")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.factor");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new CodeableConcept();
          return this.bodySite;
        }
        else if (name.equals("subSite")) {
          return addSubSite();
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

      public AddedItemComponent copy() {
        AddedItemComponent dst = new AddedItemComponent();
        copyValues(dst);
        if (itemSequence != null) {
          dst.itemSequence = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : itemSequence)
            dst.itemSequence.add(i.copy());
        };
        if (detailSequence != null) {
          dst.detailSequence = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : detailSequence)
            dst.detailSequence.add(i.copy());
        };
        if (subdetailSequence != null) {
          dst.subdetailSequence = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : subdetailSequence)
            dst.subdetailSequence.add(i.copy());
        };
        if (provider != null) {
          dst.provider = new ArrayList<Reference>();
          for (Reference i : provider)
            dst.provider.add(i.copy());
        };
        dst.productOrService = productOrService == null ? null : productOrService.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        if (programCode != null) {
          dst.programCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : programCode)
            dst.programCode.add(i.copy());
        };
        dst.serviced = serviced == null ? null : serviced.copy();
        dst.location = location == null ? null : location.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.net = net == null ? null : net.copy();
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        if (subSite != null) {
          dst.subSite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subSite)
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
          dst.detail = new ArrayList<AddedItemDetailComponent>();
          for (AddedItemDetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AddedItemComponent))
          return false;
        AddedItemComponent o = (AddedItemComponent) other_;
        return compareDeep(itemSequence, o.itemSequence, true) && compareDeep(detailSequence, o.detailSequence, true)
           && compareDeep(subdetailSequence, o.subdetailSequence, true) && compareDeep(provider, o.provider, true)
           && compareDeep(productOrService, o.productOrService, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(programCode, o.programCode, true) && compareDeep(serviced, o.serviced, true) && compareDeep(location, o.location, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(net, o.net, true) && compareDeep(bodySite, o.bodySite, true) && compareDeep(subSite, o.subSite, true)
           && compareDeep(noteNumber, o.noteNumber, true) && compareDeep(adjudication, o.adjudication, true)
           && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AddedItemComponent))
          return false;
        AddedItemComponent o = (AddedItemComponent) other_;
        return compareValues(itemSequence, o.itemSequence, true) && compareValues(detailSequence, o.detailSequence, true)
           && compareValues(subdetailSequence, o.subdetailSequence, true) && compareValues(factor, o.factor, true)
           && compareValues(noteNumber, o.noteNumber, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(itemSequence, detailSequence
          , subdetailSequence, provider, productOrService, modifier, programCode, serviced
          , location, quantity, unitPrice, factor, net, bodySite, subSite, noteNumber
          , adjudication, detail);
      }

  public String fhirType() {
    return "ClaimResponse.addItem";

  }

  }

    @Block()
    public static class AddedItemDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.
         */
        @Child(name = "productOrService", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing, service, product, or drug code", formalDefinition="When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept productOrService;

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes to convey additional context for the product or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of products or services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per item", formalDefinition="If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * The quantity times the unit price for an additional service or product or charge.
         */
        @Child(name = "net", type = {Money.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an additional service or product or charge." )
        protected Money net;

        /**
         * The numbers associated with notes below which apply to the adjudication of this item.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable note numbers", formalDefinition="The numbers associated with notes below which apply to the adjudication of this item." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudication results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=8, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Added items detail adjudication", formalDefinition="The adjudication results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * The third-tier service adjudications for payor added services.
         */
        @Child(name = "subDetail", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Insurer added line items", formalDefinition="The third-tier service adjudications for payor added services." )
        protected List<AddedItemSubDetailComponent> subDetail;

        private static final long serialVersionUID = -1436724060L;

    /**
     * Constructor
     */
      public AddedItemDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AddedItemDetailComponent(CodeableConcept productOrService) {
        super();
        this.productOrService = productOrService;
      }

        /**
         * @return {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public CodeableConcept getProductOrService() { 
          if (this.productOrService == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailComponent.productOrService");
            else if (Configuration.doAutoCreate())
              this.productOrService = new CodeableConcept(); // cc
          return this.productOrService;
        }

        public boolean hasProductOrService() { 
          return this.productOrService != null && !this.productOrService.isEmpty();
        }

        /**
         * @param value {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public AddedItemDetailComponent setProductOrService(CodeableConcept value) { 
          this.productOrService = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes to convey additional context for the product or service.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemDetailComponent setModifier(List<CodeableConcept> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (CodeableConcept item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addModifier() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return t;
        }

        public AddedItemDetailComponent addModifier(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public CodeableConcept getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailComponent.quantity");
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
        public AddedItemDetailComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
         */
        public AddedItemDetailComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailComponent.factor");
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
        public AddedItemDetailComponent setFactorElement(DecimalType value) { 
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
        public AddedItemDetailComponent setFactor(BigDecimal value) { 
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
        public AddedItemDetailComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public AddedItemDetailComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an additional service or product or charge.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemDetailComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an additional service or product or charge.)
         */
        public AddedItemDetailComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemDetailComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
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
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public PositiveIntType addNoteNumberElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public AddedItemDetailComponent addNoteNumber(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public boolean hasNoteNumber(int value) { 
          if (this.noteNumber == null)
            return false;
          for (PositiveIntType v : this.noteNumber)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudication results.)
         */
        public List<AdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          return this.adjudication;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemDetailComponent setAdjudication(List<AdjudicationComponent> theAdjudication) { 
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

        public AddedItemDetailComponent addAdjudication(AdjudicationComponent t) { //3
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
         * @return {@link #subDetail} (The third-tier service adjudications for payor added services.)
         */
        public List<AddedItemSubDetailComponent> getSubDetail() { 
          if (this.subDetail == null)
            this.subDetail = new ArrayList<AddedItemSubDetailComponent>();
          return this.subDetail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemDetailComponent setSubDetail(List<AddedItemSubDetailComponent> theSubDetail) { 
          this.subDetail = theSubDetail;
          return this;
        }

        public boolean hasSubDetail() { 
          if (this.subDetail == null)
            return false;
          for (AddedItemSubDetailComponent item : this.subDetail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AddedItemSubDetailComponent addSubDetail() { //3
          AddedItemSubDetailComponent t = new AddedItemSubDetailComponent();
          if (this.subDetail == null)
            this.subDetail = new ArrayList<AddedItemSubDetailComponent>();
          this.subDetail.add(t);
          return t;
        }

        public AddedItemDetailComponent addSubDetail(AddedItemSubDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.subDetail == null)
            this.subDetail = new ArrayList<AddedItemSubDetailComponent>();
          this.subDetail.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #subDetail}, creating it if it does not already exist
         */
        public AddedItemSubDetailComponent getSubDetailFirstRep() { 
          if (getSubDetail().isEmpty()) {
            addSubDetail();
          }
          return getSubDetail().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService));
          children.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier));
          children.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice));
          children.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor));
          children.add(new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net));
          children.add(new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          children.add(new Property("subDetail", "", "The third-tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, subDetail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1957227299: /*productOrService*/  return new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService);
          case -615513385: /*modifier*/  return new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice);
          case -1282148017: /*factor*/  return new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor);
          case 108957: /*net*/  return new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication);
          case -828829007: /*subDetail*/  return new Property("subDetail", "", "The third-tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, subDetail);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1957227299: /*productOrService*/ return this.productOrService == null ? new Base[0] : new Base[] {this.productOrService}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        case -828829007: /*subDetail*/ return this.subDetail == null ? new Base[0] : this.subDetail.toArray(new Base[this.subDetail.size()]); // AddedItemSubDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1957227299: // productOrService
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case 108957: // net
          this.net = castToMoney(value); // Money
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        case -828829007: // subDetail
          this.getSubDetail().add((AddedItemSubDetailComponent) value); // AddedItemSubDetailComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("productOrService")) {
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else if (name.equals("subDetail")) {
          this.getSubDetail().add((AddedItemSubDetailComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1957227299:  return getProductOrService(); 
        case -615513385:  return addModifier(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case 108957:  return getNet(); 
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        case -828829007:  return addSubDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1957227299: /*productOrService*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ClaimResponse.item.adjudication"};
        case -828829007: /*subDetail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("productOrService")) {
          this.productOrService = new CodeableConcept();
          return this.productOrService;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.factor");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("noteNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.noteNumber");
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

      public AddedItemDetailComponent copy() {
        AddedItemDetailComponent dst = new AddedItemDetailComponent();
        copyValues(dst);
        dst.productOrService = productOrService == null ? null : productOrService.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.net = net == null ? null : net.copy();
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
        if (subDetail != null) {
          dst.subDetail = new ArrayList<AddedItemSubDetailComponent>();
          for (AddedItemSubDetailComponent i : subDetail)
            dst.subDetail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AddedItemDetailComponent))
          return false;
        AddedItemDetailComponent o = (AddedItemDetailComponent) other_;
        return compareDeep(productOrService, o.productOrService, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(net, o.net, true) && compareDeep(noteNumber, o.noteNumber, true) && compareDeep(adjudication, o.adjudication, true)
           && compareDeep(subDetail, o.subDetail, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AddedItemDetailComponent))
          return false;
        AddedItemDetailComponent o = (AddedItemDetailComponent) other_;
        return compareValues(factor, o.factor, true) && compareValues(noteNumber, o.noteNumber, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(productOrService, modifier
          , quantity, unitPrice, factor, net, noteNumber, adjudication, subDetail);
      }

  public String fhirType() {
    return "ClaimResponse.addItem.detail";

  }

  }

    @Block()
    public static class AddedItemSubDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.
         */
        @Child(name = "productOrService", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing, service, product, or drug code", formalDefinition="When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept productOrService;

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes to convey additional context for the product or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of products or services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per item", formalDefinition="If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * The quantity times the unit price for an additional service or product or charge.
         */
        @Child(name = "net", type = {Money.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an additional service or product or charge." )
        protected Money net;

        /**
         * The numbers associated with notes below which apply to the adjudication of this item.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable note numbers", formalDefinition="The numbers associated with notes below which apply to the adjudication of this item." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudication results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=8, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Added items detail adjudication", formalDefinition="The adjudication results." )
        protected List<AdjudicationComponent> adjudication;

        private static final long serialVersionUID = 1301363592L;

    /**
     * Constructor
     */
      public AddedItemSubDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AddedItemSubDetailComponent(CodeableConcept productOrService) {
        super();
        this.productOrService = productOrService;
      }

        /**
         * @return {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public CodeableConcept getProductOrService() { 
          if (this.productOrService == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemSubDetailComponent.productOrService");
            else if (Configuration.doAutoCreate())
              this.productOrService = new CodeableConcept(); // cc
          return this.productOrService;
        }

        public boolean hasProductOrService() { 
          return this.productOrService != null && !this.productOrService.isEmpty();
        }

        /**
         * @param value {@link #productOrService} (When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.)
         */
        public AddedItemSubDetailComponent setProductOrService(CodeableConcept value) { 
          this.productOrService = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes to convey additional context for the product or service.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemSubDetailComponent setModifier(List<CodeableConcept> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (CodeableConcept item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addModifier() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return t;
        }

        public AddedItemSubDetailComponent addModifier(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public CodeableConcept getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemSubDetailComponent.quantity");
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
        public AddedItemSubDetailComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemSubDetailComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.)
         */
        public AddedItemSubDetailComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemSubDetailComponent.factor");
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
        public AddedItemSubDetailComponent setFactorElement(DecimalType value) { 
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
        public AddedItemSubDetailComponent setFactor(BigDecimal value) { 
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
        public AddedItemSubDetailComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public AddedItemSubDetailComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an additional service or product or charge.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemSubDetailComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an additional service or product or charge.)
         */
        public AddedItemSubDetailComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public List<PositiveIntType> getNoteNumber() { 
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          return this.noteNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemSubDetailComponent setNoteNumber(List<PositiveIntType> theNoteNumber) { 
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
         * @return {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public PositiveIntType addNoteNumberElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public AddedItemSubDetailComponent addNoteNumber(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.noteNumber == null)
            this.noteNumber = new ArrayList<PositiveIntType>();
          this.noteNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #noteNumber} (The numbers associated with notes below which apply to the adjudication of this item.)
         */
        public boolean hasNoteNumber(int value) { 
          if (this.noteNumber == null)
            return false;
          for (PositiveIntType v : this.noteNumber)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudication results.)
         */
        public List<AdjudicationComponent> getAdjudication() { 
          if (this.adjudication == null)
            this.adjudication = new ArrayList<AdjudicationComponent>();
          return this.adjudication;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AddedItemSubDetailComponent setAdjudication(List<AdjudicationComponent> theAdjudication) { 
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

        public AddedItemSubDetailComponent addAdjudication(AdjudicationComponent t) { //3
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService));
          children.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier));
          children.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice));
          children.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor));
          children.add(new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net));
          children.add(new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1957227299: /*productOrService*/  return new Property("productOrService", "CodeableConcept", "When the value is a group code then this item collects a set of related claim details, otherwise this contains the product, service, drug or other billing code for the item.", 0, 1, productOrService);
          case -615513385: /*modifier*/  return new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "If the item is not a group then this is the fee for the product or service, otherwise this is the total of the fees for the details of the group.", 0, 1, unitPrice);
          case -1282148017: /*factor*/  return new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor);
          case 108957: /*net*/  return new Property("net", "Money", "The quantity times the unit price for an additional service or product or charge.", 0, 1, net);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "The numbers associated with notes below which apply to the adjudication of this item.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1957227299: /*productOrService*/ return this.productOrService == null ? new Base[0] : new Base[] {this.productOrService}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1957227299: // productOrService
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case 108957: // net
          this.net = castToMoney(value); // Money
          return value;
        case -1110033957: // noteNumber
          this.getNoteNumber().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("productOrService")) {
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else if (name.equals("noteNumber")) {
          this.getNoteNumber().add(castToPositiveInt(value));
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1957227299:  return getProductOrService(); 
        case -615513385:  return addModifier(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case 108957:  return getNet(); 
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1957227299: /*productOrService*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ClaimResponse.item.adjudication"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("productOrService")) {
          this.productOrService = new CodeableConcept();
          return this.productOrService;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.factor");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("noteNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.noteNumber");
        }
        else if (name.equals("adjudication")) {
          return addAdjudication();
        }
        else
          return super.addChild(name);
      }

      public AddedItemSubDetailComponent copy() {
        AddedItemSubDetailComponent dst = new AddedItemSubDetailComponent();
        copyValues(dst);
        dst.productOrService = productOrService == null ? null : productOrService.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.net = net == null ? null : net.copy();
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
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AddedItemSubDetailComponent))
          return false;
        AddedItemSubDetailComponent o = (AddedItemSubDetailComponent) other_;
        return compareDeep(productOrService, o.productOrService, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(net, o.net, true) && compareDeep(noteNumber, o.noteNumber, true) && compareDeep(adjudication, o.adjudication, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AddedItemSubDetailComponent))
          return false;
        AddedItemSubDetailComponent o = (AddedItemSubDetailComponent) other_;
        return compareValues(factor, o.factor, true) && compareValues(noteNumber, o.noteNumber, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(productOrService, modifier
          , quantity, unitPrice, factor, net, noteNumber, adjudication);
      }

  public String fhirType() {
    return "ClaimResponse.addItem.detail.subDetail";

  }

  }

    @Block()
    public static class TotalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code to indicate the information type of this adjudication record. Information types may include: the value submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of adjudication information", formalDefinition="A code to indicate the information type of this adjudication record. Information types may include: the value submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication")
        protected CodeableConcept category;

        /**
         * Monetary total amount associated with the category.
         */
        @Child(name = "amount", type = {Money.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Financial total for the category", formalDefinition="Monetary total amount associated with the category." )
        protected Money amount;

        private static final long serialVersionUID = 2012310309L;

    /**
     * Constructor
     */
      public TotalComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TotalComponent(CodeableConcept category, Money amount) {
        super();
        this.category = category;
        this.amount = amount;
      }

        /**
         * @return {@link #category} (A code to indicate the information type of this adjudication record. Information types may include: the value submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TotalComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (A code to indicate the information type of this adjudication record. Information types may include: the value submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.)
         */
        public TotalComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monetary total amount associated with the category.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TotalComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Monetary total amount associated with the category.)
         */
        public TotalComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("category", "CodeableConcept", "A code to indicate the information type of this adjudication record. Information types may include: the value submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.", 0, 1, category));
          children.add(new Property("amount", "Money", "Monetary total amount associated with the category.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "A code to indicate the information type of this adjudication record. Information types may include: the value submitted, maximum values or percentages allowed or payable under the plan, amounts that the patient is responsible for in aggregate or pertaining to this item, amounts paid by other coverages, and the benefit payable for this item.", 0, 1, category);
          case -1413853096: /*amount*/  return new Property("amount", "Money", "Monetary total amount associated with the category.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount")) {
          this.amount = castToMoney(value); // Money
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public TotalComponent copy() {
        TotalComponent dst = new TotalComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof TotalComponent))
          return false;
        TotalComponent o = (TotalComponent) other_;
        return compareDeep(category, o.category, true) && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof TotalComponent))
          return false;
        TotalComponent o = (TotalComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, amount);
      }

  public String fhirType() {
    return "ClaimResponse.total";

  }

  }

    @Block()
    public static class PaymentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Whether this represents partial or complete payment of the benefits payable.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Partial or complete payment", formalDefinition="Whether this represents partial or complete payment of the benefits payable." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-paymenttype")
        protected CodeableConcept type;

        /**
         * Total amount of all adjustments to this payment included in this transaction which are not related to this claim's adjudication.
         */
        @Child(name = "adjustment", type = {Money.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Payment adjustment for non-claim issues", formalDefinition="Total amount of all adjustments to this payment included in this transaction which are not related to this claim's adjudication." )
        protected Money adjustment;

        /**
         * Reason for the payment adjustment.
         */
        @Child(name = "adjustmentReason", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Explanation for the adjustment", formalDefinition="Reason for the payment adjustment." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payment-adjustment-reason")
        protected CodeableConcept adjustmentReason;

        /**
         * Estimated date the payment will be issued or the actual issue date of payment.
         */
        @Child(name = "date", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Expected date of payment", formalDefinition="Estimated date the payment will be issued or the actual issue date of payment." )
        protected DateType date;

        /**
         * Benefits payable less any payment adjustment.
         */
        @Child(name = "amount", type = {Money.class}, order=5, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Payable amount after adjustment", formalDefinition="Benefits payable less any payment adjustment." )
        protected Money amount;

        /**
         * Issuer's unique identifier for the payment instrument.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Business identifier for the payment", formalDefinition="Issuer's unique identifier for the payment instrument." )
        protected Identifier identifier;

        private static final long serialVersionUID = 1539906026L;

    /**
     * Constructor
     */
      public PaymentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PaymentComponent(CodeableConcept type, Money amount) {
        super();
        this.type = type;
        this.amount = amount;
      }

        /**
         * @return {@link #type} (Whether this represents partial or complete payment of the benefits payable.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Whether this represents partial or complete payment of the benefits payable.)
         */
        public PaymentComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #adjustment} (Total amount of all adjustments to this payment included in this transaction which are not related to this claim's adjudication.)
         */
        public Money getAdjustment() { 
          if (this.adjustment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.adjustment");
            else if (Configuration.doAutoCreate())
              this.adjustment = new Money(); // cc
          return this.adjustment;
        }

        public boolean hasAdjustment() { 
          return this.adjustment != null && !this.adjustment.isEmpty();
        }

        /**
         * @param value {@link #adjustment} (Total amount of all adjustments to this payment included in this transaction which are not related to this claim's adjudication.)
         */
        public PaymentComponent setAdjustment(Money value) { 
          this.adjustment = value;
          return this;
        }

        /**
         * @return {@link #adjustmentReason} (Reason for the payment adjustment.)
         */
        public CodeableConcept getAdjustmentReason() { 
          if (this.adjustmentReason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.adjustmentReason");
            else if (Configuration.doAutoCreate())
              this.adjustmentReason = new CodeableConcept(); // cc
          return this.adjustmentReason;
        }

        public boolean hasAdjustmentReason() { 
          return this.adjustmentReason != null && !this.adjustmentReason.isEmpty();
        }

        /**
         * @param value {@link #adjustmentReason} (Reason for the payment adjustment.)
         */
        public PaymentComponent setAdjustmentReason(CodeableConcept value) { 
          this.adjustmentReason = value;
          return this;
        }

        /**
         * @return {@link #date} (Estimated date the payment will be issued or the actual issue date of payment.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.date");
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
         * @param value {@link #date} (Estimated date the payment will be issued or the actual issue date of payment.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public PaymentComponent setDateElement(DateType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Estimated date the payment will be issued or the actual issue date of payment.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Estimated date the payment will be issued or the actual issue date of payment.
         */
        public PaymentComponent setDate(Date value) { 
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
         * @return {@link #amount} (Benefits payable less any payment adjustment.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Benefits payable less any payment adjustment.)
         */
        public PaymentComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #identifier} (Issuer's unique identifier for the payment instrument.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PaymentComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Issuer's unique identifier for the payment instrument.)
         */
        public PaymentComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Whether this represents partial or complete payment of the benefits payable.", 0, 1, type));
          children.add(new Property("adjustment", "Money", "Total amount of all adjustments to this payment included in this transaction which are not related to this claim's adjudication.", 0, 1, adjustment));
          children.add(new Property("adjustmentReason", "CodeableConcept", "Reason for the payment adjustment.", 0, 1, adjustmentReason));
          children.add(new Property("date", "date", "Estimated date the payment will be issued or the actual issue date of payment.", 0, 1, date));
          children.add(new Property("amount", "Money", "Benefits payable less any payment adjustment.", 0, 1, amount));
          children.add(new Property("identifier", "Identifier", "Issuer's unique identifier for the payment instrument.", 0, 1, identifier));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Whether this represents partial or complete payment of the benefits payable.", 0, 1, type);
          case 1977085293: /*adjustment*/  return new Property("adjustment", "Money", "Total amount of all adjustments to this payment included in this transaction which are not related to this claim's adjudication.", 0, 1, adjustment);
          case -1255938543: /*adjustmentReason*/  return new Property("adjustmentReason", "CodeableConcept", "Reason for the payment adjustment.", 0, 1, adjustmentReason);
          case 3076014: /*date*/  return new Property("date", "date", "Estimated date the payment will be issued or the actual issue date of payment.", 0, 1, date);
          case -1413853096: /*amount*/  return new Property("amount", "Money", "Benefits payable less any payment adjustment.", 0, 1, amount);
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Issuer's unique identifier for the payment instrument.", 0, 1, identifier);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1977085293: /*adjustment*/ return this.adjustment == null ? new Base[0] : new Base[] {this.adjustment}; // Money
        case -1255938543: /*adjustmentReason*/ return this.adjustmentReason == null ? new Base[0] : new Base[] {this.adjustmentReason}; // CodeableConcept
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1977085293: // adjustment
          this.adjustment = castToMoney(value); // Money
          return value;
        case -1255938543: // adjustmentReason
          this.adjustmentReason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3076014: // date
          this.date = castToDate(value); // DateType
          return value;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("adjustment")) {
          this.adjustment = castToMoney(value); // Money
        } else if (name.equals("adjustmentReason")) {
          this.adjustmentReason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("date")) {
          this.date = castToDate(value); // DateType
        } else if (name.equals("amount")) {
          this.amount = castToMoney(value); // Money
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 1977085293:  return getAdjustment(); 
        case -1255938543:  return getAdjustmentReason(); 
        case 3076014:  return getDateElement();
        case -1413853096:  return getAmount(); 
        case -1618432855:  return getIdentifier(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1977085293: /*adjustment*/ return new String[] {"Money"};
        case -1255938543: /*adjustmentReason*/ return new String[] {"CodeableConcept"};
        case 3076014: /*date*/ return new String[] {"date"};
        case -1413853096: /*amount*/ return new String[] {"Money"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("adjustment")) {
          this.adjustment = new Money();
          return this.adjustment;
        }
        else if (name.equals("adjustmentReason")) {
          this.adjustmentReason = new CodeableConcept();
          return this.adjustmentReason;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.date");
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else
          return super.addChild(name);
      }

      public PaymentComponent copy() {
        PaymentComponent dst = new PaymentComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.adjustment = adjustment == null ? null : adjustment.copy();
        dst.adjustmentReason = adjustmentReason == null ? null : adjustmentReason.copy();
        dst.date = date == null ? null : date.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof PaymentComponent))
          return false;
        PaymentComponent o = (PaymentComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(adjustment, o.adjustment, true) && compareDeep(adjustmentReason, o.adjustmentReason, true)
           && compareDeep(date, o.date, true) && compareDeep(amount, o.amount, true) && compareDeep(identifier, o.identifier, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof PaymentComponent))
          return false;
        PaymentComponent o = (PaymentComponent) other_;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, adjustment, adjustmentReason
          , date, amount, identifier);
      }

  public String fhirType() {
    return "ClaimResponse.payment";

  }

  }

    @Block()
    public static class NoteComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify a note entry.
         */
        @Child(name = "number", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Note instance identifier", formalDefinition="A number to uniquely identify a note entry." )
        protected PositiveIntType number;

        /**
         * The business purpose of the note text.
         */
        @Child(name = "type", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The business purpose of the note text." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/note-type")
        protected Enumeration<NoteType> type;

        /**
         * The explanation or description associated with the processing.
         */
        @Child(name = "text", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Note explanatory text", formalDefinition="The explanation or description associated with the processing." )
        protected StringType text;

        /**
         * A code to define the language used in the text of the note.
         */
        @Child(name = "language", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language of the text", formalDefinition="A code to define the language used in the text of the note." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/languages")
        protected CodeableConcept language;

        private static final long serialVersionUID = -385184277L;

    /**
     * Constructor
     */
      public NoteComponent() {
        super();
      }

    /**
     * Constructor
     */
      public NoteComponent(StringType text) {
        super();
        this.text = text;
      }

        /**
         * @return {@link #number} (A number to uniquely identify a note entry.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
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
         * @param value {@link #number} (A number to uniquely identify a note entry.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public NoteComponent setNumberElement(PositiveIntType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return A number to uniquely identify a note entry.
         */
        public int getNumber() { 
          return this.number == null || this.number.isEmpty() ? 0 : this.number.getValue();
        }

        /**
         * @param value A number to uniquely identify a note entry.
         */
        public NoteComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new PositiveIntType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The business purpose of the note text.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<NoteType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NoteComponent.type");
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
        public NoteComponent setTypeElement(Enumeration<NoteType> value) { 
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
        public NoteComponent setType(NoteType value) { 
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
         * @param value {@link #text} (The explanation or description associated with the processing.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public NoteComponent setTextElement(StringType value) { 
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
        public NoteComponent setText(String value) { 
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          return this;
        }

        /**
         * @return {@link #language} (A code to define the language used in the text of the note.)
         */
        public CodeableConcept getLanguage() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NoteComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new CodeableConcept(); // cc
          return this.language;
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (A code to define the language used in the text of the note.)
         */
        public NoteComponent setLanguage(CodeableConcept value) { 
          this.language = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("number", "positiveInt", "A number to uniquely identify a note entry.", 0, 1, number));
          children.add(new Property("type", "code", "The business purpose of the note text.", 0, 1, type));
          children.add(new Property("text", "string", "The explanation or description associated with the processing.", 0, 1, text));
          children.add(new Property("language", "CodeableConcept", "A code to define the language used in the text of the note.", 0, 1, language));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1034364087: /*number*/  return new Property("number", "positiveInt", "A number to uniquely identify a note entry.", 0, 1, number);
          case 3575610: /*type*/  return new Property("type", "code", "The business purpose of the note text.", 0, 1, type);
          case 3556653: /*text*/  return new Property("text", "string", "The explanation or description associated with the processing.", 0, 1, text);
          case -1613589672: /*language*/  return new Property("language", "CodeableConcept", "A code to define the language used in the text of the note.", 0, 1, language);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : new Base[] {this.number}; // PositiveIntType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<NoteType>
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1034364087: // number
          this.number = castToPositiveInt(value); // PositiveIntType
          return value;
        case 3575610: // type
          value = new NoteTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<NoteType>
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case -1613589672: // language
          this.language = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("number")) {
          this.number = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("type")) {
          value = new NoteTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<NoteType>
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("language")) {
          this.language = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087:  return getNumberElement();
        case 3575610:  return getTypeElement();
        case 3556653:  return getTextElement();
        case -1613589672:  return getLanguage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return new String[] {"positiveInt"};
        case 3575610: /*type*/ return new String[] {"code"};
        case 3556653: /*text*/ return new String[] {"string"};
        case -1613589672: /*language*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.number");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.type");
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.text");
        }
        else if (name.equals("language")) {
          this.language = new CodeableConcept();
          return this.language;
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
        dst.language = language == null ? null : language.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof NoteComponent))
          return false;
        NoteComponent o = (NoteComponent) other_;
        return compareDeep(number, o.number, true) && compareDeep(type, o.type, true) && compareDeep(text, o.text, true)
           && compareDeep(language, o.language, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof NoteComponent))
          return false;
        NoteComponent o = (NoteComponent) other_;
        return compareValues(number, o.number, true) && compareValues(type, o.type, true) && compareValues(text, o.text, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(number, type, text, language
          );
      }

  public String fhirType() {
    return "ClaimResponse.processNote";

  }

  }

    @Block()
    public static class InsuranceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Insurance instance identifier", formalDefinition="A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order." )
        protected PositiveIntType sequence;

        /**
         * A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
         */
        @Child(name = "focal", type = {BooleanType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Coverage to be used for adjudication", formalDefinition="A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true." )
        protected BooleanType focal;

        /**
         * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.
         */
        @Child(name = "coverage", type = {Coverage.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system." )
        protected Reference coverage;

        /**
         * The actual object that is the target of the reference (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        protected Coverage coverageTarget;

        /**
         * A business agreement number established between the provider and the insurer for special business processing purposes.
         */
        @Child(name = "businessArrangement", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional provider contract number", formalDefinition="A business agreement number established between the provider and the insurer for special business processing purposes." )
        protected StringType businessArrangement;

        /**
         * The result of the adjudication of the line items for the Coverage specified in this insurance.
         */
        @Child(name = "claimResponse", type = {ClaimResponse.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication results", formalDefinition="The result of the adjudication of the line items for the Coverage specified in this insurance." )
        protected Reference claimResponse;

        /**
         * The actual object that is the target of the reference (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        protected ClaimResponse claimResponseTarget;

        private static final long serialVersionUID = 282380584L;

    /**
     * Constructor
     */
      public InsuranceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InsuranceComponent(PositiveIntType sequence, BooleanType focal, Reference coverage) {
        super();
        this.sequence = sequence;
        this.focal = focal;
        this.coverage = coverage;
      }

        /**
         * @return {@link #sequence} (A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.sequence");
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
         * @param value {@link #sequence} (A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public InsuranceComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.
         */
        public InsuranceComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #focal} (A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public BooleanType getFocalElement() { 
          if (this.focal == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.focal");
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
         * @param value {@link #focal} (A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public InsuranceComponent setFocalElement(BooleanType value) { 
          this.focal = value;
          return this;
        }

        /**
         * @return A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
         */
        public boolean getFocal() { 
          return this.focal == null || this.focal.isEmpty() ? false : this.focal.getValue();
        }

        /**
         * @param value A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.
         */
        public InsuranceComponent setFocal(boolean value) { 
            if (this.focal == null)
              this.focal = new BooleanType();
            this.focal.setValue(value);
          return this;
        }

        /**
         * @return {@link #coverage} (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public Reference getCoverage() { 
          if (this.coverage == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.coverage");
            else if (Configuration.doAutoCreate())
              this.coverage = new Reference(); // cc
          return this.coverage;
        }

        public boolean hasCoverage() { 
          return this.coverage != null && !this.coverage.isEmpty();
        }

        /**
         * @param value {@link #coverage} (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public InsuranceComponent setCoverage(Reference value) { 
          this.coverage = value;
          return this;
        }

        /**
         * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public Coverage getCoverageTarget() { 
          if (this.coverageTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.coverage");
            else if (Configuration.doAutoCreate())
              this.coverageTarget = new Coverage(); // aa
          return this.coverageTarget;
        }

        /**
         * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public InsuranceComponent setCoverageTarget(Coverage value) { 
          this.coverageTarget = value;
          return this;
        }

        /**
         * @return {@link #businessArrangement} (A business agreement number established between the provider and the insurer for special business processing purposes.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public StringType getBusinessArrangementElement() { 
          if (this.businessArrangement == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.businessArrangement");
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
         * @param value {@link #businessArrangement} (A business agreement number established between the provider and the insurer for special business processing purposes.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public InsuranceComponent setBusinessArrangementElement(StringType value) { 
          this.businessArrangement = value;
          return this;
        }

        /**
         * @return A business agreement number established between the provider and the insurer for special business processing purposes.
         */
        public String getBusinessArrangement() { 
          return this.businessArrangement == null ? null : this.businessArrangement.getValue();
        }

        /**
         * @param value A business agreement number established between the provider and the insurer for special business processing purposes.
         */
        public InsuranceComponent setBusinessArrangement(String value) { 
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
         * @return {@link #claimResponse} (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        public Reference getClaimResponse() { 
          if (this.claimResponse == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.claimResponse");
            else if (Configuration.doAutoCreate())
              this.claimResponse = new Reference(); // cc
          return this.claimResponse;
        }

        public boolean hasClaimResponse() { 
          return this.claimResponse != null && !this.claimResponse.isEmpty();
        }

        /**
         * @param value {@link #claimResponse} (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        public InsuranceComponent setClaimResponse(Reference value) { 
          this.claimResponse = value;
          return this;
        }

        /**
         * @return {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        public ClaimResponse getClaimResponseTarget() { 
          if (this.claimResponseTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.claimResponse");
            else if (Configuration.doAutoCreate())
              this.claimResponseTarget = new ClaimResponse(); // aa
          return this.claimResponseTarget;
        }

        /**
         * @param value {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The result of the adjudication of the line items for the Coverage specified in this insurance.)
         */
        public InsuranceComponent setClaimResponseTarget(ClaimResponse value) { 
          this.claimResponseTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.", 0, 1, sequence));
          children.add(new Property("focal", "boolean", "A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.", 0, 1, focal));
          children.add(new Property("coverage", "Reference(Coverage)", "Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.", 0, 1, coverage));
          children.add(new Property("businessArrangement", "string", "A business agreement number established between the provider and the insurer for special business processing purposes.", 0, 1, businessArrangement));
          children.add(new Property("claimResponse", "Reference(ClaimResponse)", "The result of the adjudication of the line items for the Coverage specified in this insurance.", 0, 1, claimResponse));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify insurance entries and provide a sequence of coverages to convey coordination of benefit order.", 0, 1, sequence);
          case 97604197: /*focal*/  return new Property("focal", "boolean", "A flag to indicate that this Coverage is to be used for adjudication of this claim when set to true.", 0, 1, focal);
          case -351767064: /*coverage*/  return new Property("coverage", "Reference(Coverage)", "Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.", 0, 1, coverage);
          case 259920682: /*businessArrangement*/  return new Property("businessArrangement", "string", "A business agreement number established between the provider and the insurer for special business processing purposes.", 0, 1, businessArrangement);
          case 689513629: /*claimResponse*/  return new Property("claimResponse", "Reference(ClaimResponse)", "The result of the adjudication of the line items for the Coverage specified in this insurance.", 0, 1, claimResponse);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 97604197: /*focal*/ return this.focal == null ? new Base[0] : new Base[] {this.focal}; // BooleanType
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Reference
        case 259920682: /*businessArrangement*/ return this.businessArrangement == null ? new Base[0] : new Base[] {this.businessArrangement}; // StringType
        case 689513629: /*claimResponse*/ return this.claimResponse == null ? new Base[0] : new Base[] {this.claimResponse}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 97604197: // focal
          this.focal = castToBoolean(value); // BooleanType
          return value;
        case -351767064: // coverage
          this.coverage = castToReference(value); // Reference
          return value;
        case 259920682: // businessArrangement
          this.businessArrangement = castToString(value); // StringType
          return value;
        case 689513629: // claimResponse
          this.claimResponse = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("focal")) {
          this.focal = castToBoolean(value); // BooleanType
        } else if (name.equals("coverage")) {
          this.coverage = castToReference(value); // Reference
        } else if (name.equals("businessArrangement")) {
          this.businessArrangement = castToString(value); // StringType
        } else if (name.equals("claimResponse")) {
          this.claimResponse = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 97604197:  return getFocalElement();
        case -351767064:  return getCoverage(); 
        case 259920682:  return getBusinessArrangementElement();
        case 689513629:  return getClaimResponse(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 97604197: /*focal*/ return new String[] {"boolean"};
        case -351767064: /*coverage*/ return new String[] {"Reference"};
        case 259920682: /*businessArrangement*/ return new String[] {"string"};
        case 689513629: /*claimResponse*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
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
        else if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("businessArrangement")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.businessArrangement");
        }
        else if (name.equals("claimResponse")) {
          this.claimResponse = new Reference();
          return this.claimResponse;
        }
        else
          return super.addChild(name);
      }

      public InsuranceComponent copy() {
        InsuranceComponent dst = new InsuranceComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.focal = focal == null ? null : focal.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.businessArrangement = businessArrangement == null ? null : businessArrangement.copy();
        dst.claimResponse = claimResponse == null ? null : claimResponse.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(focal, o.focal, true) && compareDeep(coverage, o.coverage, true)
           && compareDeep(businessArrangement, o.businessArrangement, true) && compareDeep(claimResponse, o.claimResponse, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other_;
        return compareValues(sequence, o.sequence, true) && compareValues(focal, o.focal, true) && compareValues(businessArrangement, o.businessArrangement, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, focal, coverage
          , businessArrangement, claimResponse);
      }

  public String fhirType() {
    return "ClaimResponse.insurance";

  }

  }

    @Block()
    public static class ErrorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.
         */
        @Child(name = "itemSequence", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Item sequence number", formalDefinition="The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure." )
        protected PositiveIntType itemSequence;

        /**
         * The sequence number of the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.
         */
        @Child(name = "detailSequence", type = {PositiveIntType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Detail sequence number", formalDefinition="The sequence number of the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure." )
        protected PositiveIntType detailSequence;

        /**
         * The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.
         */
        @Child(name = "subDetailSequence", type = {PositiveIntType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Subdetail sequence number", formalDefinition="The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure." )
        protected PositiveIntType subDetailSequence;

        /**
         * An error code, from a specified code system, which details why the claim could not be adjudicated.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Error code detailing processing issues", formalDefinition="An error code, from a specified code system, which details why the claim could not be adjudicated." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication-error")
        protected CodeableConcept code;

        private static final long serialVersionUID = 843818320L;

    /**
     * Constructor
     */
      public ErrorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ErrorComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #itemSequence} (The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.). This is the underlying object with id, value and extensions. The accessor "getItemSequence" gives direct access to the value
         */
        public PositiveIntType getItemSequenceElement() { 
          if (this.itemSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ErrorComponent.itemSequence");
            else if (Configuration.doAutoCreate())
              this.itemSequence = new PositiveIntType(); // bb
          return this.itemSequence;
        }

        public boolean hasItemSequenceElement() { 
          return this.itemSequence != null && !this.itemSequence.isEmpty();
        }

        public boolean hasItemSequence() { 
          return this.itemSequence != null && !this.itemSequence.isEmpty();
        }

        /**
         * @param value {@link #itemSequence} (The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.). This is the underlying object with id, value and extensions. The accessor "getItemSequence" gives direct access to the value
         */
        public ErrorComponent setItemSequenceElement(PositiveIntType value) { 
          this.itemSequence = value;
          return this;
        }

        /**
         * @return The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.
         */
        public int getItemSequence() { 
          return this.itemSequence == null || this.itemSequence.isEmpty() ? 0 : this.itemSequence.getValue();
        }

        /**
         * @param value The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.
         */
        public ErrorComponent setItemSequence(int value) { 
            if (this.itemSequence == null)
              this.itemSequence = new PositiveIntType();
            this.itemSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #detailSequence} (The sequence number of the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.). This is the underlying object with id, value and extensions. The accessor "getDetailSequence" gives direct access to the value
         */
        public PositiveIntType getDetailSequenceElement() { 
          if (this.detailSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ErrorComponent.detailSequence");
            else if (Configuration.doAutoCreate())
              this.detailSequence = new PositiveIntType(); // bb
          return this.detailSequence;
        }

        public boolean hasDetailSequenceElement() { 
          return this.detailSequence != null && !this.detailSequence.isEmpty();
        }

        public boolean hasDetailSequence() { 
          return this.detailSequence != null && !this.detailSequence.isEmpty();
        }

        /**
         * @param value {@link #detailSequence} (The sequence number of the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.). This is the underlying object with id, value and extensions. The accessor "getDetailSequence" gives direct access to the value
         */
        public ErrorComponent setDetailSequenceElement(PositiveIntType value) { 
          this.detailSequence = value;
          return this;
        }

        /**
         * @return The sequence number of the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.
         */
        public int getDetailSequence() { 
          return this.detailSequence == null || this.detailSequence.isEmpty() ? 0 : this.detailSequence.getValue();
        }

        /**
         * @param value The sequence number of the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.
         */
        public ErrorComponent setDetailSequence(int value) { 
            if (this.detailSequence == null)
              this.detailSequence = new PositiveIntType();
            this.detailSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #subDetailSequence} (The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.). This is the underlying object with id, value and extensions. The accessor "getSubDetailSequence" gives direct access to the value
         */
        public PositiveIntType getSubDetailSequenceElement() { 
          if (this.subDetailSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ErrorComponent.subDetailSequence");
            else if (Configuration.doAutoCreate())
              this.subDetailSequence = new PositiveIntType(); // bb
          return this.subDetailSequence;
        }

        public boolean hasSubDetailSequenceElement() { 
          return this.subDetailSequence != null && !this.subDetailSequence.isEmpty();
        }

        public boolean hasSubDetailSequence() { 
          return this.subDetailSequence != null && !this.subDetailSequence.isEmpty();
        }

        /**
         * @param value {@link #subDetailSequence} (The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.). This is the underlying object with id, value and extensions. The accessor "getSubDetailSequence" gives direct access to the value
         */
        public ErrorComponent setSubDetailSequenceElement(PositiveIntType value) { 
          this.subDetailSequence = value;
          return this;
        }

        /**
         * @return The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.
         */
        public int getSubDetailSequence() { 
          return this.subDetailSequence == null || this.subDetailSequence.isEmpty() ? 0 : this.subDetailSequence.getValue();
        }

        /**
         * @param value The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.
         */
        public ErrorComponent setSubDetailSequence(int value) { 
            if (this.subDetailSequence == null)
              this.subDetailSequence = new PositiveIntType();
            this.subDetailSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (An error code, from a specified code system, which details why the claim could not be adjudicated.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ErrorComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (An error code, from a specified code system, which details why the claim could not be adjudicated.)
         */
        public ErrorComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("itemSequence", "positiveInt", "The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.", 0, 1, itemSequence));
          children.add(new Property("detailSequence", "positiveInt", "The sequence number of the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.", 0, 1, detailSequence));
          children.add(new Property("subDetailSequence", "positiveInt", "The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.", 0, 1, subDetailSequence));
          children.add(new Property("code", "CodeableConcept", "An error code, from a specified code system, which details why the claim could not be adjudicated.", 0, 1, code));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1977979892: /*itemSequence*/  return new Property("itemSequence", "positiveInt", "The sequence number of the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.", 0, 1, itemSequence);
          case 1321472818: /*detailSequence*/  return new Property("detailSequence", "positiveInt", "The sequence number of the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.", 0, 1, detailSequence);
          case -855462510: /*subDetailSequence*/  return new Property("subDetailSequence", "positiveInt", "The sequence number of the sub-detail within the detail within the line item submitted which contains the error. This value is omitted when the error occurs outside of the item structure.", 0, 1, subDetailSequence);
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "An error code, from a specified code system, which details why the claim could not be adjudicated.", 0, 1, code);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1977979892: /*itemSequence*/ return this.itemSequence == null ? new Base[0] : new Base[] {this.itemSequence}; // PositiveIntType
        case 1321472818: /*detailSequence*/ return this.detailSequence == null ? new Base[0] : new Base[] {this.detailSequence}; // PositiveIntType
        case -855462510: /*subDetailSequence*/ return this.subDetailSequence == null ? new Base[0] : new Base[] {this.subDetailSequence}; // PositiveIntType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1977979892: // itemSequence
          this.itemSequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 1321472818: // detailSequence
          this.detailSequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case -855462510: // subDetailSequence
          this.subDetailSequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("itemSequence")) {
          this.itemSequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("detailSequence")) {
          this.detailSequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("subDetailSequence")) {
          this.subDetailSequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1977979892:  return getItemSequenceElement();
        case 1321472818:  return getDetailSequenceElement();
        case -855462510:  return getSubDetailSequenceElement();
        case 3059181:  return getCode(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1977979892: /*itemSequence*/ return new String[] {"positiveInt"};
        case 1321472818: /*detailSequence*/ return new String[] {"positiveInt"};
        case -855462510: /*subDetailSequence*/ return new String[] {"positiveInt"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("itemSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.itemSequence");
        }
        else if (name.equals("detailSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.detailSequence");
        }
        else if (name.equals("subDetailSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.subDetailSequence");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else
          return super.addChild(name);
      }

      public ErrorComponent copy() {
        ErrorComponent dst = new ErrorComponent();
        copyValues(dst);
        dst.itemSequence = itemSequence == null ? null : itemSequence.copy();
        dst.detailSequence = detailSequence == null ? null : detailSequence.copy();
        dst.subDetailSequence = subDetailSequence == null ? null : subDetailSequence.copy();
        dst.code = code == null ? null : code.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ErrorComponent))
          return false;
        ErrorComponent o = (ErrorComponent) other_;
        return compareDeep(itemSequence, o.itemSequence, true) && compareDeep(detailSequence, o.detailSequence, true)
           && compareDeep(subDetailSequence, o.subDetailSequence, true) && compareDeep(code, o.code, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ErrorComponent))
          return false;
        ErrorComponent o = (ErrorComponent) other_;
        return compareValues(itemSequence, o.itemSequence, true) && compareValues(detailSequence, o.detailSequence, true)
           && compareValues(subDetailSequence, o.subDetailSequence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(itemSequence, detailSequence
          , subDetailSequence, code);
      }

  public String fhirType() {
    return "ClaimResponse.error";

  }

  }

    /**
     * A unique identifier assigned to this claim response.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier for a claim response", formalDefinition="A unique identifier assigned to this claim response." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<ClaimResponseStatus> status;

    /**
     * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="More granular claim type", formalDefinition="A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-type")
    protected CodeableConcept type;

    /**
     * A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.
     */
    @Child(name = "subType", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="More granular claim type", formalDefinition="A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-subtype")
    protected CodeableConcept subType;

    /**
     * A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.
     */
    @Child(name = "use", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="claim | preauthorization | predetermination", formalDefinition="A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-use")
    protected Enumeration<Use> use;

    /**
     * The party to whom the professional services and/or products have been supplied or are being considered and for whom actual for facast reimbursement is sought.
     */
    @Child(name = "patient", type = {Patient.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The recipient of the products and services", formalDefinition="The party to whom the professional services and/or products have been supplied or are being considered and for whom actual for facast reimbursement is sought." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual for facast reimbursement is sought.)
     */
    protected Patient patientTarget;

    /**
     * The date this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Response creation date", formalDefinition="The date this resource was created." )
    protected DateTimeType created;

    /**
     * The party responsible for authorization, adjudication and reimbursement.
     */
    @Child(name = "insurer", type = {Organization.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Party responsible for reimbursement", formalDefinition="The party responsible for authorization, adjudication and reimbursement." )
    protected Reference insurer;

    /**
     * The actual object that is the target of the reference (The party responsible for authorization, adjudication and reimbursement.)
     */
    protected Organization insurerTarget;

    /**
     * The provider which is responsible for the claim, predetermination or preauthorization.
     */
    @Child(name = "requestor", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Party responsible for the claim", formalDefinition="The provider which is responsible for the claim, predetermination or preauthorization." )
    protected Reference requestor;

    /**
     * The actual object that is the target of the reference (The provider which is responsible for the claim, predetermination or preauthorization.)
     */
    protected Resource requestorTarget;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {Claim.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Id of resource triggering adjudication", formalDefinition="Original request resource reference." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Original request resource reference.)
     */
    protected Claim requestTarget;

    /**
     * The outcome of the claim, predetermination, or preauthorization processing.
     */
    @Child(name = "outcome", type = {CodeType.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="queued | complete | error | partial", formalDefinition="The outcome of the claim, predetermination, or preauthorization processing." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/remittance-outcome")
    protected Enumeration<RemittanceOutcome> outcome;

    /**
     * A human readable description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disposition Message", formalDefinition="A human readable description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * Reference from the Insurer which is used in later communications which refers to this adjudication.
     */
    @Child(name = "preAuthRef", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Preauthorization reference", formalDefinition="Reference from the Insurer which is used in later communications which refers to this adjudication." )
    protected StringType preAuthRef;

    /**
     * The time frame during which this authorization is effective.
     */
    @Child(name = "preAuthPeriod", type = {Period.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Preauthorization reference effective period", formalDefinition="The time frame during which this authorization is effective." )
    protected Period preAuthPeriod;

    /**
     * Type of Party to be reimbursed: subscriber, provider, other.
     */
    @Child(name = "payeeType", type = {CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Party to be paid any benefits payable", formalDefinition="Type of Party to be reimbursed: subscriber, provider, other." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payeetype")
    protected CodeableConcept payeeType;

    /**
     * A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or groups of sub-details.
     */
    @Child(name = "item", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Adjudication for claim line items", formalDefinition="A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or groups of sub-details." )
    protected List<ItemComponent> item;

    /**
     * The first-tier service adjudications for payor added product or service lines.
     */
    @Child(name = "addItem", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Insurer added line items", formalDefinition="The first-tier service adjudications for payor added product or service lines." )
    protected List<AddedItemComponent> addItem;

    /**
     * The adjudication results which are presented at the header level rather than at the line-item or add-item levels.
     */
    @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Header-level adjudication", formalDefinition="The adjudication results which are presented at the header level rather than at the line-item or add-item levels." )
    protected List<AdjudicationComponent> adjudication;

    /**
     * Categorized monetary totals for the adjudication.
     */
    @Child(name = "total", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Adjudication totals", formalDefinition="Categorized monetary totals for the adjudication." )
    protected List<TotalComponent> total;

    /**
     * Payment details for the adjudication of the claim.
     */
    @Child(name = "payment", type = {}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Payment Details", formalDefinition="Payment details for the adjudication of the claim." )
    protected PaymentComponent payment;

    /**
     * A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and for whom.
     */
    @Child(name = "fundsReserve", type = {CodeableConcept.class}, order=20, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Funds reserved status", formalDefinition="A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and for whom." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fundsreserve")
    protected CodeableConcept fundsReserve;

    /**
     * A code for the form to be used for printing the content.
     */
    @Child(name = "formCode", type = {CodeableConcept.class}, order=21, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Printed form identifier", formalDefinition="A code for the form to be used for printing the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/forms")
    protected CodeableConcept formCode;

    /**
     * The actual form, by reference or inclusion, for printing the content or an EOB.
     */
    @Child(name = "form", type = {Attachment.class}, order=22, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Printed reference or actual form", formalDefinition="The actual form, by reference or inclusion, for printing the content or an EOB." )
    protected Attachment form;

    /**
     * A note that describes or explains adjudication results in a human readable form.
     */
    @Child(name = "processNote", type = {}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Note concerning adjudication", formalDefinition="A note that describes or explains adjudication results in a human readable form." )
    protected List<NoteComponent> processNote;

    /**
     * Request for additional supporting or authorizing information.
     */
    @Child(name = "communicationRequest", type = {CommunicationRequest.class}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Request for additional information", formalDefinition="Request for additional supporting or authorizing information." )
    protected List<Reference> communicationRequest;
    /**
     * The actual objects that are the target of the reference (Request for additional supporting or authorizing information.)
     */
    protected List<CommunicationRequest> communicationRequestTarget;


    /**
     * Financial instruments for reimbursement for the health care products and services specified on the claim.
     */
    @Child(name = "insurance", type = {}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Patient insurance information", formalDefinition="Financial instruments for reimbursement for the health care products and services specified on the claim." )
    protected List<InsuranceComponent> insurance;

    /**
     * Errors encountered during the processing of the adjudication.
     */
    @Child(name = "error", type = {}, order=26, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Processing errors", formalDefinition="Errors encountered during the processing of the adjudication." )
    protected List<ErrorComponent> error;

    private static final long serialVersionUID = 731586651L;

  /**
   * Constructor
   */
    public ClaimResponse() {
      super();
    }

  /**
   * Constructor
   */
    public ClaimResponse(Enumeration<ClaimResponseStatus> status, CodeableConcept type, Enumeration<Use> use, Reference patient, DateTimeType created, Reference insurer, Enumeration<RemittanceOutcome> outcome) {
      super();
      this.status = status;
      this.type = type;
      this.use = use;
      this.patient = patient;
      this.created = created;
      this.insurer = insurer;
      this.outcome = outcome;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this claim response.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setIdentifier(List<Identifier> theIdentifier) { 
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

    public ClaimResponse addIdentifier(Identifier t) { //3
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
    public Enumeration<ClaimResponseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ClaimResponseStatus>(new ClaimResponseStatusEnumFactory()); // bb
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
    public ClaimResponse setStatusElement(Enumeration<ClaimResponseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public ClaimResponseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public ClaimResponse setStatus(ClaimResponseStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ClaimResponseStatus>(new ClaimResponseStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.)
     */
    public ClaimResponse setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subType} (A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.)
     */
    public CodeableConcept getSubType() { 
      if (this.subType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.subType");
        else if (Configuration.doAutoCreate())
          this.subType = new CodeableConcept(); // cc
      return this.subType;
    }

    public boolean hasSubType() { 
      return this.subType != null && !this.subType.isEmpty();
    }

    /**
     * @param value {@link #subType} (A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.)
     */
    public ClaimResponse setSubType(CodeableConcept value) { 
      this.subType = value;
      return this;
    }

    /**
     * @return {@link #use} (A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Enumeration<Use> getUseElement() { 
      if (this.use == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.use");
        else if (Configuration.doAutoCreate())
          this.use = new Enumeration<Use>(new UseEnumFactory()); // bb
      return this.use;
    }

    public boolean hasUseElement() { 
      return this.use != null && !this.use.isEmpty();
    }

    public boolean hasUse() { 
      return this.use != null && !this.use.isEmpty();
    }

    /**
     * @param value {@link #use} (A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public ClaimResponse setUseElement(Enumeration<Use> value) { 
      this.use = value;
      return this;
    }

    /**
     * @return A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.
     */
    public Use getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.
     */
    public ClaimResponse setUse(Use value) { 
        if (this.use == null)
          this.use = new Enumeration<Use>(new UseEnumFactory());
        this.use.setValue(value);
      return this;
    }

    /**
     * @return {@link #patient} (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual for facast reimbursement is sought.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual for facast reimbursement is sought.)
     */
    public ClaimResponse setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual for facast reimbursement is sought.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party to whom the professional services and/or products have been supplied or are being considered and for whom actual for facast reimbursement is sought.)
     */
    public ClaimResponse setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #created} (The date this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
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
     * @param value {@link #created} (The date this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public ClaimResponse setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date this resource was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date this resource was created.
     */
    public ClaimResponse setCreated(Date value) { 
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      return this;
    }

    /**
     * @return {@link #insurer} (The party responsible for authorization, adjudication and reimbursement.)
     */
    public Reference getInsurer() { 
      if (this.insurer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.insurer");
        else if (Configuration.doAutoCreate())
          this.insurer = new Reference(); // cc
      return this.insurer;
    }

    public boolean hasInsurer() { 
      return this.insurer != null && !this.insurer.isEmpty();
    }

    /**
     * @param value {@link #insurer} (The party responsible for authorization, adjudication and reimbursement.)
     */
    public ClaimResponse setInsurer(Reference value) { 
      this.insurer = value;
      return this;
    }

    /**
     * @return {@link #insurer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party responsible for authorization, adjudication and reimbursement.)
     */
    public Organization getInsurerTarget() { 
      if (this.insurerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.insurer");
        else if (Configuration.doAutoCreate())
          this.insurerTarget = new Organization(); // aa
      return this.insurerTarget;
    }

    /**
     * @param value {@link #insurer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party responsible for authorization, adjudication and reimbursement.)
     */
    public ClaimResponse setInsurerTarget(Organization value) { 
      this.insurerTarget = value;
      return this;
    }

    /**
     * @return {@link #requestor} (The provider which is responsible for the claim, predetermination or preauthorization.)
     */
    public Reference getRequestor() { 
      if (this.requestor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.requestor");
        else if (Configuration.doAutoCreate())
          this.requestor = new Reference(); // cc
      return this.requestor;
    }

    public boolean hasRequestor() { 
      return this.requestor != null && !this.requestor.isEmpty();
    }

    /**
     * @param value {@link #requestor} (The provider which is responsible for the claim, predetermination or preauthorization.)
     */
    public ClaimResponse setRequestor(Reference value) { 
      this.requestor = value;
      return this;
    }

    /**
     * @return {@link #requestor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the claim, predetermination or preauthorization.)
     */
    public Resource getRequestorTarget() { 
      return this.requestorTarget;
    }

    /**
     * @param value {@link #requestor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the claim, predetermination or preauthorization.)
     */
    public ClaimResponse setRequestorTarget(Resource value) { 
      this.requestorTarget = value;
      return this;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Reference getRequest() { 
      if (this.request == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.request");
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
    public ClaimResponse setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public Claim getRequestTarget() { 
      if (this.requestTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.request");
        else if (Configuration.doAutoCreate())
          this.requestTarget = new Claim(); // aa
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public ClaimResponse setRequestTarget(Claim value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #outcome} (The outcome of the claim, predetermination, or preauthorization processing.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
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
     * @param value {@link #outcome} (The outcome of the claim, predetermination, or preauthorization processing.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public ClaimResponse setOutcomeElement(Enumeration<RemittanceOutcome> value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return The outcome of the claim, predetermination, or preauthorization processing.
     */
    public RemittanceOutcome getOutcome() { 
      return this.outcome == null ? null : this.outcome.getValue();
    }

    /**
     * @param value The outcome of the claim, predetermination, or preauthorization processing.
     */
    public ClaimResponse setOutcome(RemittanceOutcome value) { 
        if (this.outcome == null)
          this.outcome = new Enumeration<RemittanceOutcome>(new RemittanceOutcomeEnumFactory());
        this.outcome.setValue(value);
      return this;
    }

    /**
     * @return {@link #disposition} (A human readable description of the status of the adjudication.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
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
     * @param value {@link #disposition} (A human readable description of the status of the adjudication.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public ClaimResponse setDispositionElement(StringType value) { 
      this.disposition = value;
      return this;
    }

    /**
     * @return A human readable description of the status of the adjudication.
     */
    public String getDisposition() { 
      return this.disposition == null ? null : this.disposition.getValue();
    }

    /**
     * @param value A human readable description of the status of the adjudication.
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
     * @return {@link #preAuthRef} (Reference from the Insurer which is used in later communications which refers to this adjudication.). This is the underlying object with id, value and extensions. The accessor "getPreAuthRef" gives direct access to the value
     */
    public StringType getPreAuthRefElement() { 
      if (this.preAuthRef == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.preAuthRef");
        else if (Configuration.doAutoCreate())
          this.preAuthRef = new StringType(); // bb
      return this.preAuthRef;
    }

    public boolean hasPreAuthRefElement() { 
      return this.preAuthRef != null && !this.preAuthRef.isEmpty();
    }

    public boolean hasPreAuthRef() { 
      return this.preAuthRef != null && !this.preAuthRef.isEmpty();
    }

    /**
     * @param value {@link #preAuthRef} (Reference from the Insurer which is used in later communications which refers to this adjudication.). This is the underlying object with id, value and extensions. The accessor "getPreAuthRef" gives direct access to the value
     */
    public ClaimResponse setPreAuthRefElement(StringType value) { 
      this.preAuthRef = value;
      return this;
    }

    /**
     * @return Reference from the Insurer which is used in later communications which refers to this adjudication.
     */
    public String getPreAuthRef() { 
      return this.preAuthRef == null ? null : this.preAuthRef.getValue();
    }

    /**
     * @param value Reference from the Insurer which is used in later communications which refers to this adjudication.
     */
    public ClaimResponse setPreAuthRef(String value) { 
      if (Utilities.noString(value))
        this.preAuthRef = null;
      else {
        if (this.preAuthRef == null)
          this.preAuthRef = new StringType();
        this.preAuthRef.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #preAuthPeriod} (The time frame during which this authorization is effective.)
     */
    public Period getPreAuthPeriod() { 
      if (this.preAuthPeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.preAuthPeriod");
        else if (Configuration.doAutoCreate())
          this.preAuthPeriod = new Period(); // cc
      return this.preAuthPeriod;
    }

    public boolean hasPreAuthPeriod() { 
      return this.preAuthPeriod != null && !this.preAuthPeriod.isEmpty();
    }

    /**
     * @param value {@link #preAuthPeriod} (The time frame during which this authorization is effective.)
     */
    public ClaimResponse setPreAuthPeriod(Period value) { 
      this.preAuthPeriod = value;
      return this;
    }

    /**
     * @return {@link #payeeType} (Type of Party to be reimbursed: subscriber, provider, other.)
     */
    public CodeableConcept getPayeeType() { 
      if (this.payeeType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.payeeType");
        else if (Configuration.doAutoCreate())
          this.payeeType = new CodeableConcept(); // cc
      return this.payeeType;
    }

    public boolean hasPayeeType() { 
      return this.payeeType != null && !this.payeeType.isEmpty();
    }

    /**
     * @param value {@link #payeeType} (Type of Party to be reimbursed: subscriber, provider, other.)
     */
    public ClaimResponse setPayeeType(CodeableConcept value) { 
      this.payeeType = value;
      return this;
    }

    /**
     * @return {@link #item} (A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or groups of sub-details.)
     */
    public List<ItemComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<ItemComponent>();
      return this.item;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setItem(List<ItemComponent> theItem) { 
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

    public ClaimResponse addItem(ItemComponent t) { //3
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
     * @return {@link #addItem} (The first-tier service adjudications for payor added product or service lines.)
     */
    public List<AddedItemComponent> getAddItem() { 
      if (this.addItem == null)
        this.addItem = new ArrayList<AddedItemComponent>();
      return this.addItem;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setAddItem(List<AddedItemComponent> theAddItem) { 
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

    public ClaimResponse addAddItem(AddedItemComponent t) { //3
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
     * @return {@link #adjudication} (The adjudication results which are presented at the header level rather than at the line-item or add-item levels.)
     */
    public List<AdjudicationComponent> getAdjudication() { 
      if (this.adjudication == null)
        this.adjudication = new ArrayList<AdjudicationComponent>();
      return this.adjudication;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setAdjudication(List<AdjudicationComponent> theAdjudication) { 
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

    public ClaimResponse addAdjudication(AdjudicationComponent t) { //3
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
     * @return {@link #total} (Categorized monetary totals for the adjudication.)
     */
    public List<TotalComponent> getTotal() { 
      if (this.total == null)
        this.total = new ArrayList<TotalComponent>();
      return this.total;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setTotal(List<TotalComponent> theTotal) { 
      this.total = theTotal;
      return this;
    }

    public boolean hasTotal() { 
      if (this.total == null)
        return false;
      for (TotalComponent item : this.total)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TotalComponent addTotal() { //3
      TotalComponent t = new TotalComponent();
      if (this.total == null)
        this.total = new ArrayList<TotalComponent>();
      this.total.add(t);
      return t;
    }

    public ClaimResponse addTotal(TotalComponent t) { //3
      if (t == null)
        return this;
      if (this.total == null)
        this.total = new ArrayList<TotalComponent>();
      this.total.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #total}, creating it if it does not already exist
     */
    public TotalComponent getTotalFirstRep() { 
      if (getTotal().isEmpty()) {
        addTotal();
      }
      return getTotal().get(0);
    }

    /**
     * @return {@link #payment} (Payment details for the adjudication of the claim.)
     */
    public PaymentComponent getPayment() { 
      if (this.payment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.payment");
        else if (Configuration.doAutoCreate())
          this.payment = new PaymentComponent(); // cc
      return this.payment;
    }

    public boolean hasPayment() { 
      return this.payment != null && !this.payment.isEmpty();
    }

    /**
     * @param value {@link #payment} (Payment details for the adjudication of the claim.)
     */
    public ClaimResponse setPayment(PaymentComponent value) { 
      this.payment = value;
      return this;
    }

    /**
     * @return {@link #fundsReserve} (A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and for whom.)
     */
    public CodeableConcept getFundsReserve() { 
      if (this.fundsReserve == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.fundsReserve");
        else if (Configuration.doAutoCreate())
          this.fundsReserve = new CodeableConcept(); // cc
      return this.fundsReserve;
    }

    public boolean hasFundsReserve() { 
      return this.fundsReserve != null && !this.fundsReserve.isEmpty();
    }

    /**
     * @param value {@link #fundsReserve} (A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and for whom.)
     */
    public ClaimResponse setFundsReserve(CodeableConcept value) { 
      this.fundsReserve = value;
      return this;
    }

    /**
     * @return {@link #formCode} (A code for the form to be used for printing the content.)
     */
    public CodeableConcept getFormCode() { 
      if (this.formCode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.formCode");
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
    public ClaimResponse setFormCode(CodeableConcept value) { 
      this.formCode = value;
      return this;
    }

    /**
     * @return {@link #form} (The actual form, by reference or inclusion, for printing the content or an EOB.)
     */
    public Attachment getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.form");
        else if (Configuration.doAutoCreate())
          this.form = new Attachment(); // cc
      return this.form;
    }

    public boolean hasForm() { 
      return this.form != null && !this.form.isEmpty();
    }

    /**
     * @param value {@link #form} (The actual form, by reference or inclusion, for printing the content or an EOB.)
     */
    public ClaimResponse setForm(Attachment value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #processNote} (A note that describes or explains adjudication results in a human readable form.)
     */
    public List<NoteComponent> getProcessNote() { 
      if (this.processNote == null)
        this.processNote = new ArrayList<NoteComponent>();
      return this.processNote;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setProcessNote(List<NoteComponent> theProcessNote) { 
      this.processNote = theProcessNote;
      return this;
    }

    public boolean hasProcessNote() { 
      if (this.processNote == null)
        return false;
      for (NoteComponent item : this.processNote)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public NoteComponent addProcessNote() { //3
      NoteComponent t = new NoteComponent();
      if (this.processNote == null)
        this.processNote = new ArrayList<NoteComponent>();
      this.processNote.add(t);
      return t;
    }

    public ClaimResponse addProcessNote(NoteComponent t) { //3
      if (t == null)
        return this;
      if (this.processNote == null)
        this.processNote = new ArrayList<NoteComponent>();
      this.processNote.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #processNote}, creating it if it does not already exist
     */
    public NoteComponent getProcessNoteFirstRep() { 
      if (getProcessNote().isEmpty()) {
        addProcessNote();
      }
      return getProcessNote().get(0);
    }

    /**
     * @return {@link #communicationRequest} (Request for additional supporting or authorizing information.)
     */
    public List<Reference> getCommunicationRequest() { 
      if (this.communicationRequest == null)
        this.communicationRequest = new ArrayList<Reference>();
      return this.communicationRequest;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setCommunicationRequest(List<Reference> theCommunicationRequest) { 
      this.communicationRequest = theCommunicationRequest;
      return this;
    }

    public boolean hasCommunicationRequest() { 
      if (this.communicationRequest == null)
        return false;
      for (Reference item : this.communicationRequest)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addCommunicationRequest() { //3
      Reference t = new Reference();
      if (this.communicationRequest == null)
        this.communicationRequest = new ArrayList<Reference>();
      this.communicationRequest.add(t);
      return t;
    }

    public ClaimResponse addCommunicationRequest(Reference t) { //3
      if (t == null)
        return this;
      if (this.communicationRequest == null)
        this.communicationRequest = new ArrayList<Reference>();
      this.communicationRequest.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #communicationRequest}, creating it if it does not already exist
     */
    public Reference getCommunicationRequestFirstRep() { 
      if (getCommunicationRequest().isEmpty()) {
        addCommunicationRequest();
      }
      return getCommunicationRequest().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<CommunicationRequest> getCommunicationRequestTarget() { 
      if (this.communicationRequestTarget == null)
        this.communicationRequestTarget = new ArrayList<CommunicationRequest>();
      return this.communicationRequestTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public CommunicationRequest addCommunicationRequestTarget() { 
      CommunicationRequest r = new CommunicationRequest();
      if (this.communicationRequestTarget == null)
        this.communicationRequestTarget = new ArrayList<CommunicationRequest>();
      this.communicationRequestTarget.add(r);
      return r;
    }

    /**
     * @return {@link #insurance} (Financial instruments for reimbursement for the health care products and services specified on the claim.)
     */
    public List<InsuranceComponent> getInsurance() { 
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      return this.insurance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setInsurance(List<InsuranceComponent> theInsurance) { 
      this.insurance = theInsurance;
      return this;
    }

    public boolean hasInsurance() { 
      if (this.insurance == null)
        return false;
      for (InsuranceComponent item : this.insurance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public InsuranceComponent addInsurance() { //3
      InsuranceComponent t = new InsuranceComponent();
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      this.insurance.add(t);
      return t;
    }

    public ClaimResponse addInsurance(InsuranceComponent t) { //3
      if (t == null)
        return this;
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      this.insurance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #insurance}, creating it if it does not already exist
     */
    public InsuranceComponent getInsuranceFirstRep() { 
      if (getInsurance().isEmpty()) {
        addInsurance();
      }
      return getInsurance().get(0);
    }

    /**
     * @return {@link #error} (Errors encountered during the processing of the adjudication.)
     */
    public List<ErrorComponent> getError() { 
      if (this.error == null)
        this.error = new ArrayList<ErrorComponent>();
      return this.error;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setError(List<ErrorComponent> theError) { 
      this.error = theError;
      return this;
    }

    public boolean hasError() { 
      if (this.error == null)
        return false;
      for (ErrorComponent item : this.error)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ErrorComponent addError() { //3
      ErrorComponent t = new ErrorComponent();
      if (this.error == null)
        this.error = new ArrayList<ErrorComponent>();
      this.error.add(t);
      return t;
    }

    public ClaimResponse addError(ErrorComponent t) { //3
      if (t == null)
        return this;
      if (this.error == null)
        this.error = new ArrayList<ErrorComponent>();
      this.error.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #error}, creating it if it does not already exist
     */
    public ErrorComponent getErrorFirstRep() { 
      if (getError().isEmpty()) {
        addError();
      }
      return getError().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A unique identifier assigned to this claim response.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The status of the resource instance.", 0, 1, status));
        children.add(new Property("type", "CodeableConcept", "A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.", 0, 1, type));
        children.add(new Property("subType", "CodeableConcept", "A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.", 0, 1, subType));
        children.add(new Property("use", "code", "A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.", 0, 1, use));
        children.add(new Property("patient", "Reference(Patient)", "The party to whom the professional services and/or products have been supplied or are being considered and for whom actual for facast reimbursement is sought.", 0, 1, patient));
        children.add(new Property("created", "dateTime", "The date this resource was created.", 0, 1, created));
        children.add(new Property("insurer", "Reference(Organization)", "The party responsible for authorization, adjudication and reimbursement.", 0, 1, insurer));
        children.add(new Property("requestor", "Reference(Practitioner|PractitionerRole|Organization)", "The provider which is responsible for the claim, predetermination or preauthorization.", 0, 1, requestor));
        children.add(new Property("request", "Reference(Claim)", "Original request resource reference.", 0, 1, request));
        children.add(new Property("outcome", "code", "The outcome of the claim, predetermination, or preauthorization processing.", 0, 1, outcome));
        children.add(new Property("disposition", "string", "A human readable description of the status of the adjudication.", 0, 1, disposition));
        children.add(new Property("preAuthRef", "string", "Reference from the Insurer which is used in later communications which refers to this adjudication.", 0, 1, preAuthRef));
        children.add(new Property("preAuthPeriod", "Period", "The time frame during which this authorization is effective.", 0, 1, preAuthPeriod));
        children.add(new Property("payeeType", "CodeableConcept", "Type of Party to be reimbursed: subscriber, provider, other.", 0, 1, payeeType));
        children.add(new Property("item", "", "A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or groups of sub-details.", 0, java.lang.Integer.MAX_VALUE, item));
        children.add(new Property("addItem", "", "The first-tier service adjudications for payor added product or service lines.", 0, java.lang.Integer.MAX_VALUE, addItem));
        children.add(new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results which are presented at the header level rather than at the line-item or add-item levels.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        children.add(new Property("total", "", "Categorized monetary totals for the adjudication.", 0, java.lang.Integer.MAX_VALUE, total));
        children.add(new Property("payment", "", "Payment details for the adjudication of the claim.", 0, 1, payment));
        children.add(new Property("fundsReserve", "CodeableConcept", "A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and for whom.", 0, 1, fundsReserve));
        children.add(new Property("formCode", "CodeableConcept", "A code for the form to be used for printing the content.", 0, 1, formCode));
        children.add(new Property("form", "Attachment", "The actual form, by reference or inclusion, for printing the content or an EOB.", 0, 1, form));
        children.add(new Property("processNote", "", "A note that describes or explains adjudication results in a human readable form.", 0, java.lang.Integer.MAX_VALUE, processNote));
        children.add(new Property("communicationRequest", "Reference(CommunicationRequest)", "Request for additional supporting or authorizing information.", 0, java.lang.Integer.MAX_VALUE, communicationRequest));
        children.add(new Property("insurance", "", "Financial instruments for reimbursement for the health care products and services specified on the claim.", 0, java.lang.Integer.MAX_VALUE, insurance));
        children.add(new Property("error", "", "Errors encountered during the processing of the adjudication.", 0, java.lang.Integer.MAX_VALUE, error));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A unique identifier assigned to this claim response.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the resource instance.", 0, 1, status);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.", 0, 1, type);
        case -1868521062: /*subType*/  return new Property("subType", "CodeableConcept", "A finer grained suite of claim type codes which may convey additional information such as Inpatient vs Outpatient and/or a specialty service.", 0, 1, subType);
        case 116103: /*use*/  return new Property("use", "code", "A code to indicate whether the nature of the request is: to request adjudication of products and services previously rendered; or requesting authorization and adjudication for provision in the future; or requesting the non-binding adjudication of the listed products and services which could be provided in the future.", 0, 1, use);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "The party to whom the professional services and/or products have been supplied or are being considered and for whom actual for facast reimbursement is sought.", 0, 1, patient);
        case 1028554472: /*created*/  return new Property("created", "dateTime", "The date this resource was created.", 0, 1, created);
        case 1957615864: /*insurer*/  return new Property("insurer", "Reference(Organization)", "The party responsible for authorization, adjudication and reimbursement.", 0, 1, insurer);
        case 693934258: /*requestor*/  return new Property("requestor", "Reference(Practitioner|PractitionerRole|Organization)", "The provider which is responsible for the claim, predetermination or preauthorization.", 0, 1, requestor);
        case 1095692943: /*request*/  return new Property("request", "Reference(Claim)", "Original request resource reference.", 0, 1, request);
        case -1106507950: /*outcome*/  return new Property("outcome", "code", "The outcome of the claim, predetermination, or preauthorization processing.", 0, 1, outcome);
        case 583380919: /*disposition*/  return new Property("disposition", "string", "A human readable description of the status of the adjudication.", 0, 1, disposition);
        case 522246568: /*preAuthRef*/  return new Property("preAuthRef", "string", "Reference from the Insurer which is used in later communications which refers to this adjudication.", 0, 1, preAuthRef);
        case 1819164812: /*preAuthPeriod*/  return new Property("preAuthPeriod", "Period", "The time frame during which this authorization is effective.", 0, 1, preAuthPeriod);
        case -316321118: /*payeeType*/  return new Property("payeeType", "CodeableConcept", "Type of Party to be reimbursed: subscriber, provider, other.", 0, 1, payeeType);
        case 3242771: /*item*/  return new Property("item", "", "A claim line. Either a simple (a product or service) or a 'group' of details which can also be a simple items or groups of sub-details.", 0, java.lang.Integer.MAX_VALUE, item);
        case -1148899500: /*addItem*/  return new Property("addItem", "", "The first-tier service adjudications for payor added product or service lines.", 0, java.lang.Integer.MAX_VALUE, addItem);
        case -231349275: /*adjudication*/  return new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudication results which are presented at the header level rather than at the line-item or add-item levels.", 0, java.lang.Integer.MAX_VALUE, adjudication);
        case 110549828: /*total*/  return new Property("total", "", "Categorized monetary totals for the adjudication.", 0, java.lang.Integer.MAX_VALUE, total);
        case -786681338: /*payment*/  return new Property("payment", "", "Payment details for the adjudication of the claim.", 0, 1, payment);
        case 1314609806: /*fundsReserve*/  return new Property("fundsReserve", "CodeableConcept", "A code, used only on a response to a preauthorization, to indicate whether the benefits payable have been reserved and for whom.", 0, 1, fundsReserve);
        case 473181393: /*formCode*/  return new Property("formCode", "CodeableConcept", "A code for the form to be used for printing the content.", 0, 1, formCode);
        case 3148996: /*form*/  return new Property("form", "Attachment", "The actual form, by reference or inclusion, for printing the content or an EOB.", 0, 1, form);
        case 202339073: /*processNote*/  return new Property("processNote", "", "A note that describes or explains adjudication results in a human readable form.", 0, java.lang.Integer.MAX_VALUE, processNote);
        case -2071896615: /*communicationRequest*/  return new Property("communicationRequest", "Reference(CommunicationRequest)", "Request for additional supporting or authorizing information.", 0, java.lang.Integer.MAX_VALUE, communicationRequest);
        case 73049818: /*insurance*/  return new Property("insurance", "", "Financial instruments for reimbursement for the health care products and services specified on the claim.", 0, java.lang.Integer.MAX_VALUE, insurance);
        case 96784904: /*error*/  return new Property("error", "", "Errors encountered during the processing of the adjudication.", 0, java.lang.Integer.MAX_VALUE, error);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ClaimResponseStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : new Base[] {this.subType}; // CodeableConcept
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // Enumeration<Use>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Reference
        case 693934258: /*requestor*/ return this.requestor == null ? new Base[0] : new Base[] {this.requestor}; // Reference
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // Enumeration<RemittanceOutcome>
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case 522246568: /*preAuthRef*/ return this.preAuthRef == null ? new Base[0] : new Base[] {this.preAuthRef}; // StringType
        case 1819164812: /*preAuthPeriod*/ return this.preAuthPeriod == null ? new Base[0] : new Base[] {this.preAuthPeriod}; // Period
        case -316321118: /*payeeType*/ return this.payeeType == null ? new Base[0] : new Base[] {this.payeeType}; // CodeableConcept
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // ItemComponent
        case -1148899500: /*addItem*/ return this.addItem == null ? new Base[0] : this.addItem.toArray(new Base[this.addItem.size()]); // AddedItemComponent
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
        case 110549828: /*total*/ return this.total == null ? new Base[0] : this.total.toArray(new Base[this.total.size()]); // TotalComponent
        case -786681338: /*payment*/ return this.payment == null ? new Base[0] : new Base[] {this.payment}; // PaymentComponent
        case 1314609806: /*fundsReserve*/ return this.fundsReserve == null ? new Base[0] : new Base[] {this.fundsReserve}; // CodeableConcept
        case 473181393: /*formCode*/ return this.formCode == null ? new Base[0] : new Base[] {this.formCode}; // CodeableConcept
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // Attachment
        case 202339073: /*processNote*/ return this.processNote == null ? new Base[0] : this.processNote.toArray(new Base[this.processNote.size()]); // NoteComponent
        case -2071896615: /*communicationRequest*/ return this.communicationRequest == null ? new Base[0] : this.communicationRequest.toArray(new Base[this.communicationRequest.size()]); // Reference
        case 73049818: /*insurance*/ return this.insurance == null ? new Base[0] : this.insurance.toArray(new Base[this.insurance.size()]); // InsuranceComponent
        case 96784904: /*error*/ return this.error == null ? new Base[0] : this.error.toArray(new Base[this.error.size()]); // ErrorComponent
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
          value = new ClaimResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ClaimResponseStatus>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868521062: // subType
          this.subType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 116103: // use
          value = new UseEnumFactory().fromType(castToCode(value));
          this.use = (Enumeration) value; // Enumeration<Use>
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case 1957615864: // insurer
          this.insurer = castToReference(value); // Reference
          return value;
        case 693934258: // requestor
          this.requestor = castToReference(value); // Reference
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case -1106507950: // outcome
          value = new RemittanceOutcomeEnumFactory().fromType(castToCode(value));
          this.outcome = (Enumeration) value; // Enumeration<RemittanceOutcome>
          return value;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
          return value;
        case 522246568: // preAuthRef
          this.preAuthRef = castToString(value); // StringType
          return value;
        case 1819164812: // preAuthPeriod
          this.preAuthPeriod = castToPeriod(value); // Period
          return value;
        case -316321118: // payeeType
          this.payeeType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3242771: // item
          this.getItem().add((ItemComponent) value); // ItemComponent
          return value;
        case -1148899500: // addItem
          this.getAddItem().add((AddedItemComponent) value); // AddedItemComponent
          return value;
        case -231349275: // adjudication
          this.getAdjudication().add((AdjudicationComponent) value); // AdjudicationComponent
          return value;
        case 110549828: // total
          this.getTotal().add((TotalComponent) value); // TotalComponent
          return value;
        case -786681338: // payment
          this.payment = (PaymentComponent) value; // PaymentComponent
          return value;
        case 1314609806: // fundsReserve
          this.fundsReserve = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 473181393: // formCode
          this.formCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3148996: // form
          this.form = castToAttachment(value); // Attachment
          return value;
        case 202339073: // processNote
          this.getProcessNote().add((NoteComponent) value); // NoteComponent
          return value;
        case -2071896615: // communicationRequest
          this.getCommunicationRequest().add(castToReference(value)); // Reference
          return value;
        case 73049818: // insurance
          this.getInsurance().add((InsuranceComponent) value); // InsuranceComponent
          return value;
        case 96784904: // error
          this.getError().add((ErrorComponent) value); // ErrorComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ClaimResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ClaimResponseStatus>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subType")) {
          this.subType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("use")) {
          value = new UseEnumFactory().fromType(castToCode(value));
          this.use = (Enumeration) value; // Enumeration<Use>
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("insurer")) {
          this.insurer = castToReference(value); // Reference
        } else if (name.equals("requestor")) {
          this.requestor = castToReference(value); // Reference
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("outcome")) {
          value = new RemittanceOutcomeEnumFactory().fromType(castToCode(value));
          this.outcome = (Enumeration) value; // Enumeration<RemittanceOutcome>
        } else if (name.equals("disposition")) {
          this.disposition = castToString(value); // StringType
        } else if (name.equals("preAuthRef")) {
          this.preAuthRef = castToString(value); // StringType
        } else if (name.equals("preAuthPeriod")) {
          this.preAuthPeriod = castToPeriod(value); // Period
        } else if (name.equals("payeeType")) {
          this.payeeType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("item")) {
          this.getItem().add((ItemComponent) value);
        } else if (name.equals("addItem")) {
          this.getAddItem().add((AddedItemComponent) value);
        } else if (name.equals("adjudication")) {
          this.getAdjudication().add((AdjudicationComponent) value);
        } else if (name.equals("total")) {
          this.getTotal().add((TotalComponent) value);
        } else if (name.equals("payment")) {
          this.payment = (PaymentComponent) value; // PaymentComponent
        } else if (name.equals("fundsReserve")) {
          this.fundsReserve = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("formCode")) {
          this.formCode = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("form")) {
          this.form = castToAttachment(value); // Attachment
        } else if (name.equals("processNote")) {
          this.getProcessNote().add((NoteComponent) value);
        } else if (name.equals("communicationRequest")) {
          this.getCommunicationRequest().add(castToReference(value));
        } else if (name.equals("insurance")) {
          this.getInsurance().add((InsuranceComponent) value);
        } else if (name.equals("error")) {
          this.getError().add((ErrorComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 3575610:  return getType(); 
        case -1868521062:  return getSubType(); 
        case 116103:  return getUseElement();
        case -791418107:  return getPatient(); 
        case 1028554472:  return getCreatedElement();
        case 1957615864:  return getInsurer(); 
        case 693934258:  return getRequestor(); 
        case 1095692943:  return getRequest(); 
        case -1106507950:  return getOutcomeElement();
        case 583380919:  return getDispositionElement();
        case 522246568:  return getPreAuthRefElement();
        case 1819164812:  return getPreAuthPeriod(); 
        case -316321118:  return getPayeeType(); 
        case 3242771:  return addItem(); 
        case -1148899500:  return addAddItem(); 
        case -231349275:  return addAdjudication(); 
        case 110549828:  return addTotal(); 
        case -786681338:  return getPayment(); 
        case 1314609806:  return getFundsReserve(); 
        case 473181393:  return getFormCode(); 
        case 3148996:  return getForm(); 
        case 202339073:  return addProcessNote(); 
        case -2071896615:  return addCommunicationRequest(); 
        case 73049818:  return addInsurance(); 
        case 96784904:  return addError(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1868521062: /*subType*/ return new String[] {"CodeableConcept"};
        case 116103: /*use*/ return new String[] {"code"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case 1957615864: /*insurer*/ return new String[] {"Reference"};
        case 693934258: /*requestor*/ return new String[] {"Reference"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -1106507950: /*outcome*/ return new String[] {"code"};
        case 583380919: /*disposition*/ return new String[] {"string"};
        case 522246568: /*preAuthRef*/ return new String[] {"string"};
        case 1819164812: /*preAuthPeriod*/ return new String[] {"Period"};
        case -316321118: /*payeeType*/ return new String[] {"CodeableConcept"};
        case 3242771: /*item*/ return new String[] {};
        case -1148899500: /*addItem*/ return new String[] {};
        case -231349275: /*adjudication*/ return new String[] {"@ClaimResponse.item.adjudication"};
        case 110549828: /*total*/ return new String[] {};
        case -786681338: /*payment*/ return new String[] {};
        case 1314609806: /*fundsReserve*/ return new String[] {"CodeableConcept"};
        case 473181393: /*formCode*/ return new String[] {"CodeableConcept"};
        case 3148996: /*form*/ return new String[] {"Attachment"};
        case 202339073: /*processNote*/ return new String[] {};
        case -2071896615: /*communicationRequest*/ return new String[] {"Reference"};
        case 73049818: /*insurance*/ return new String[] {};
        case 96784904: /*error*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.status");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          this.subType = new CodeableConcept();
          return this.subType;
        }
        else if (name.equals("use")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.use");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.created");
        }
        else if (name.equals("insurer")) {
          this.insurer = new Reference();
          return this.insurer;
        }
        else if (name.equals("requestor")) {
          this.requestor = new Reference();
          return this.requestor;
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("outcome")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.outcome");
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.disposition");
        }
        else if (name.equals("preAuthRef")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClaimResponse.preAuthRef");
        }
        else if (name.equals("preAuthPeriod")) {
          this.preAuthPeriod = new Period();
          return this.preAuthPeriod;
        }
        else if (name.equals("payeeType")) {
          this.payeeType = new CodeableConcept();
          return this.payeeType;
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else if (name.equals("addItem")) {
          return addAddItem();
        }
        else if (name.equals("adjudication")) {
          return addAdjudication();
        }
        else if (name.equals("total")) {
          return addTotal();
        }
        else if (name.equals("payment")) {
          this.payment = new PaymentComponent();
          return this.payment;
        }
        else if (name.equals("fundsReserve")) {
          this.fundsReserve = new CodeableConcept();
          return this.fundsReserve;
        }
        else if (name.equals("formCode")) {
          this.formCode = new CodeableConcept();
          return this.formCode;
        }
        else if (name.equals("form")) {
          this.form = new Attachment();
          return this.form;
        }
        else if (name.equals("processNote")) {
          return addProcessNote();
        }
        else if (name.equals("communicationRequest")) {
          return addCommunicationRequest();
        }
        else if (name.equals("insurance")) {
          return addInsurance();
        }
        else if (name.equals("error")) {
          return addError();
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
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.subType = subType == null ? null : subType.copy();
        dst.use = use == null ? null : use.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.created = created == null ? null : created.copy();
        dst.insurer = insurer == null ? null : insurer.copy();
        dst.requestor = requestor == null ? null : requestor.copy();
        dst.request = request == null ? null : request.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.preAuthRef = preAuthRef == null ? null : preAuthRef.copy();
        dst.preAuthPeriod = preAuthPeriod == null ? null : preAuthPeriod.copy();
        dst.payeeType = payeeType == null ? null : payeeType.copy();
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
        if (adjudication != null) {
          dst.adjudication = new ArrayList<AdjudicationComponent>();
          for (AdjudicationComponent i : adjudication)
            dst.adjudication.add(i.copy());
        };
        if (total != null) {
          dst.total = new ArrayList<TotalComponent>();
          for (TotalComponent i : total)
            dst.total.add(i.copy());
        };
        dst.payment = payment == null ? null : payment.copy();
        dst.fundsReserve = fundsReserve == null ? null : fundsReserve.copy();
        dst.formCode = formCode == null ? null : formCode.copy();
        dst.form = form == null ? null : form.copy();
        if (processNote != null) {
          dst.processNote = new ArrayList<NoteComponent>();
          for (NoteComponent i : processNote)
            dst.processNote.add(i.copy());
        };
        if (communicationRequest != null) {
          dst.communicationRequest = new ArrayList<Reference>();
          for (Reference i : communicationRequest)
            dst.communicationRequest.add(i.copy());
        };
        if (insurance != null) {
          dst.insurance = new ArrayList<InsuranceComponent>();
          for (InsuranceComponent i : insurance)
            dst.insurance.add(i.copy());
        };
        if (error != null) {
          dst.error = new ArrayList<ErrorComponent>();
          for (ErrorComponent i : error)
            dst.error.add(i.copy());
        };
        return dst;
      }

      protected ClaimResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ClaimResponse))
          return false;
        ClaimResponse o = (ClaimResponse) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(subType, o.subType, true) && compareDeep(use, o.use, true) && compareDeep(patient, o.patient, true)
           && compareDeep(created, o.created, true) && compareDeep(insurer, o.insurer, true) && compareDeep(requestor, o.requestor, true)
           && compareDeep(request, o.request, true) && compareDeep(outcome, o.outcome, true) && compareDeep(disposition, o.disposition, true)
           && compareDeep(preAuthRef, o.preAuthRef, true) && compareDeep(preAuthPeriod, o.preAuthPeriod, true)
           && compareDeep(payeeType, o.payeeType, true) && compareDeep(item, o.item, true) && compareDeep(addItem, o.addItem, true)
           && compareDeep(adjudication, o.adjudication, true) && compareDeep(total, o.total, true) && compareDeep(payment, o.payment, true)
           && compareDeep(fundsReserve, o.fundsReserve, true) && compareDeep(formCode, o.formCode, true) && compareDeep(form, o.form, true)
           && compareDeep(processNote, o.processNote, true) && compareDeep(communicationRequest, o.communicationRequest, true)
           && compareDeep(insurance, o.insurance, true) && compareDeep(error, o.error, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ClaimResponse))
          return false;
        ClaimResponse o = (ClaimResponse) other_;
        return compareValues(status, o.status, true) && compareValues(use, o.use, true) && compareValues(created, o.created, true)
           && compareValues(outcome, o.outcome, true) && compareValues(disposition, o.disposition, true) && compareValues(preAuthRef, o.preAuthRef, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, type
          , subType, use, patient, created, insurer, requestor, request, outcome, disposition
          , preAuthRef, preAuthPeriod, payeeType, item, addItem, adjudication, total, payment
          , fundsReserve, formCode, form, processNote, communicationRequest, insurance, error
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ClaimResponse;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identity of the ClaimResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ClaimResponse.identifier", description="The identity of the ClaimResponse", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identity of the ClaimResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>request</b>
   * <p>
   * Description: <b>The claim reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.request</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request", path="ClaimResponse.request", description="The claim reference", type="reference", target={Claim.class } )
  public static final String SP_REQUEST = "request";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request</b>
   * <p>
   * Description: <b>The claim reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.request</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClaimResponse:request</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST = new ca.uhn.fhir.model.api.Include("ClaimResponse:request").toLocked();

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

 /**
   * Search parameter: <b>insurer</b>
   * <p>
   * Description: <b>The organization which generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.insurer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="insurer", path="ClaimResponse.insurer", description="The organization which generated this resource", type="reference", target={Organization.class } )
  public static final String SP_INSURER = "insurer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>insurer</b>
   * <p>
   * Description: <b>The organization which generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.insurer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INSURER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INSURER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClaimResponse:insurer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INSURER = new ca.uhn.fhir.model.api.Include("ClaimResponse:insurer").toLocked();

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
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The subject of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ClaimResponse.patient", description="The subject of care", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The subject of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClaimResponse:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ClaimResponse:patient").toLocked();

 /**
   * Search parameter: <b>use</b>
   * <p>
   * Description: <b>The type of claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.use</b><br>
   * </p>
   */
  @SearchParamDefinition(name="use", path="ClaimResponse.use", description="The type of claim", type="token" )
  public static final String SP_USE = "use";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>use</b>
   * <p>
   * Description: <b>The type of claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.use</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam USE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_USE);

 /**
   * Search parameter: <b>payment-date</b>
   * <p>
   * Description: <b>The expected payment date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClaimResponse.payment.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="payment-date", path="ClaimResponse.payment.date", description="The expected payment date", type="date" )
  public static final String SP_PAYMENT_DATE = "payment-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>payment-date</b>
   * <p>
   * Description: <b>The expected payment date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClaimResponse.payment.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PAYMENT_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PAYMENT_DATE);

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
   * Search parameter: <b>requestor</b>
   * <p>
   * Description: <b>The Provider of the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.requestor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestor", path="ClaimResponse.requestor", description="The Provider of the claim", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class, PractitionerRole.class } )
  public static final String SP_REQUESTOR = "requestor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestor</b>
   * <p>
   * Description: <b>The Provider of the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.requestor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClaimResponse:requestor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTOR = new ca.uhn.fhir.model.api.Include("ClaimResponse:requestor").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the ClaimResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ClaimResponse.status", description="The status of the ClaimResponse", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the ClaimResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

