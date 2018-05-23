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
@ResourceDef(name="ClaimResponse", profile="http://hl7.org/fhir/Profile/ClaimResponse")
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
        COMPLETE, 
        /**
         * The treatment is proposed and this represents a Pre-authorization for the services.
         */
        PROPOSED, 
        /**
         * The treatment is proposed and this represents a Pre-determination for the services.
         */
        EXPLORATORY, 
        /**
         * A locally defined or otherwise resolved status.
         */
        OTHER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static Use fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("exploratory".equals(codeString))
          return EXPLORATORY;
        if ("other".equals(codeString))
          return OTHER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown Use code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLETE: return "complete";
            case PROPOSED: return "proposed";
            case EXPLORATORY: return "exploratory";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPLETE: return "http://hl7.org/fhir/claim-use";
            case PROPOSED: return "http://hl7.org/fhir/claim-use";
            case EXPLORATORY: return "http://hl7.org/fhir/claim-use";
            case OTHER: return "http://hl7.org/fhir/claim-use";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPLETE: return "The treatment is complete and this represents a Claim for the services.";
            case PROPOSED: return "The treatment is proposed and this represents a Pre-authorization for the services.";
            case EXPLORATORY: return "The treatment is proposed and this represents a Pre-determination for the services.";
            case OTHER: return "A locally defined or otherwise resolved status.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLETE: return "Complete";
            case PROPOSED: return "Proposed";
            case EXPLORATORY: return "Exploratory";
            case OTHER: return "Other";
            default: return "?";
          }
        }
    }

  public static class UseEnumFactory implements EnumFactory<Use> {
    public Use fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return Use.COMPLETE;
        if ("proposed".equals(codeString))
          return Use.PROPOSED;
        if ("exploratory".equals(codeString))
          return Use.EXPLORATORY;
        if ("other".equals(codeString))
          return Use.OTHER;
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
        if ("complete".equals(codeString))
          return new Enumeration<Use>(this, Use.COMPLETE);
        if ("proposed".equals(codeString))
          return new Enumeration<Use>(this, Use.PROPOSED);
        if ("exploratory".equals(codeString))
          return new Enumeration<Use>(this, Use.EXPLORATORY);
        if ("other".equals(codeString))
          return new Enumeration<Use>(this, Use.OTHER);
        throw new FHIRException("Unknown Use code '"+codeString+"'");
        }
    public String toCode(Use code) {
      if (code == Use.COMPLETE)
        return "complete";
      if (code == Use.PROPOSED)
        return "proposed";
      if (code == Use.EXPLORATORY)
        return "exploratory";
      if (code == Use.OTHER)
        return "other";
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
         * A service line number.
         */
        @Child(name = "itemSequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType itemSequence;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudication results.
         */
        @Child(name = "adjudication", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication details", formalDefinition="The adjudication results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * The second tier service adjudications for submitted services.
         */
        @Child(name = "detail", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Detail line items", formalDefinition="The second tier service adjudications for submitted services." )
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
         * @return {@link #itemSequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getItemSequence" gives direct access to the value
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
         * @param value {@link #itemSequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getItemSequence" gives direct access to the value
         */
        public ItemComponent setItemSequenceElement(PositiveIntType value) { 
          this.itemSequence = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getItemSequence() { 
          return this.itemSequence == null || this.itemSequence.isEmpty() ? 0 : this.itemSequence.getValue();
        }

        /**
         * @param value A service line number.
         */
        public ItemComponent setItemSequence(int value) { 
            if (this.itemSequence == null)
              this.itemSequence = new PositiveIntType();
            this.itemSequence.setValue(value);
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
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
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
        public ItemComponent addNoteNumber(int value) { //1
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
         * @return {@link #detail} (The second tier service adjudications for submitted services.)
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
          children.add(new Property("itemSequence", "positiveInt", "A service line number.", 0, 1, itemSequence));
          children.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          children.add(new Property("detail", "", "The second tier service adjudications for submitted services.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1977979892: /*itemSequence*/  return new Property("itemSequence", "positiveInt", "A service line number.", 0, 1, itemSequence);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "", "The adjudication results.", 0, java.lang.Integer.MAX_VALUE, adjudication);
          case -1335224239: /*detail*/  return new Property("detail", "", "The second tier service adjudications for submitted services.", 0, java.lang.Integer.MAX_VALUE, detail);
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
         * Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication category such as co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication")
        protected CodeableConcept category;

        /**
         * Adjudication reason such as limit reached.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Explanation of Adjudication outcome", formalDefinition="Adjudication reason such as limit reached." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication-reason")
        protected CodeableConcept reason;

        /**
         * Monetary amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monetary amount associated with the code." )
        protected Money amount;

        /**
         * A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        @Child(name = "value", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Non-monetary value", formalDefinition="A non-monetary value for example a percentage. Mutually exclusive to the amount element above." )
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
         * @return {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
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
         * @param value {@link #category} (Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.)
         */
        public AdjudicationComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #reason} (Adjudication reason such as limit reached.)
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
         * @param value {@link #reason} (Adjudication reason such as limit reached.)
         */
        public AdjudicationComponent setReason(CodeableConcept value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monetary amount associated with the code.)
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
         * @param value {@link #amount} (Monetary amount associated with the code.)
         */
        public AdjudicationComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
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
         * @param value {@link #value} (A non-monetary value for example a percentage. Mutually exclusive to the amount element above.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public AdjudicationComponent setValueElement(DecimalType value) { 
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
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AdjudicationComponent setValue(long value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        /**
         * @param value A non-monetary value for example a percentage. Mutually exclusive to the amount element above.
         */
        public AdjudicationComponent setValue(double value) { 
              this.value = new DecimalType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("category", "CodeableConcept", "Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.", 0, 1, category));
          children.add(new Property("reason", "CodeableConcept", "Adjudication reason such as limit reached.", 0, 1, reason));
          children.add(new Property("amount", "Money", "Monetary amount associated with the code.", 0, 1, amount));
          children.add(new Property("value", "decimal", "A non-monetary value for example a percentage. Mutually exclusive to the amount element above.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "Code indicating: Co-Pay, deductible, eligible, benefit, tax, etc.", 0, 1, category);
          case -934964668: /*reason*/  return new Property("reason", "CodeableConcept", "Adjudication reason such as limit reached.", 0, 1, reason);
          case -1413853096: /*amount*/  return new Property("amount", "Money", "Monetary amount associated with the code.", 0, 1, amount);
          case 111972721: /*value*/  return new Property("value", "decimal", "A non-monetary value for example a percentage. Mutually exclusive to the amount element above.", 0, 1, value);
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
         * A service line number.
         */
        @Child(name = "detailSequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType detailSequence;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Detail level adjudication details", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        /**
         * The third tier service adjudications for submitted services.
         */
        @Child(name = "subDetail", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Subdetail line items", formalDefinition="The third tier service adjudications for submitted services." )
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
         * @return {@link #detailSequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getDetailSequence" gives direct access to the value
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
         * @param value {@link #detailSequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getDetailSequence" gives direct access to the value
         */
        public ItemDetailComponent setDetailSequenceElement(PositiveIntType value) { 
          this.detailSequence = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getDetailSequence() { 
          return this.detailSequence == null || this.detailSequence.isEmpty() ? 0 : this.detailSequence.getValue();
        }

        /**
         * @param value A service line number.
         */
        public ItemDetailComponent setDetailSequence(int value) { 
            if (this.detailSequence == null)
              this.detailSequence = new PositiveIntType();
            this.detailSequence.setValue(value);
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
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
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
        public ItemDetailComponent addNoteNumber(int value) { //1
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
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
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
         * @return {@link #subDetail} (The third tier service adjudications for submitted services.)
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
          children.add(new Property("detailSequence", "positiveInt", "A service line number.", 0, 1, detailSequence));
          children.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
          children.add(new Property("subDetail", "", "The third tier service adjudications for submitted services.", 0, java.lang.Integer.MAX_VALUE, subDetail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1321472818: /*detailSequence*/  return new Property("detailSequence", "positiveInt", "A service line number.", 0, 1, detailSequence);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication);
          case -828829007: /*subDetail*/  return new Property("subDetail", "", "The third tier service adjudications for submitted services.", 0, java.lang.Integer.MAX_VALUE, subDetail);
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
         * A service line number.
         */
        @Child(name = "subDetailSequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected PositiveIntType subDetailSequence;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Subdetail level adjudication details", formalDefinition="The adjudications results." )
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
         * @return {@link #subDetailSequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSubDetailSequence" gives direct access to the value
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
         * @param value {@link #subDetailSequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSubDetailSequence" gives direct access to the value
         */
        public SubDetailComponent setSubDetailSequenceElement(PositiveIntType value) { 
          this.subDetailSequence = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSubDetailSequence() { 
          return this.subDetailSequence == null || this.subDetailSequence.isEmpty() ? 0 : this.subDetailSequence.getValue();
        }

        /**
         * @param value A service line number.
         */
        public SubDetailComponent setSubDetailSequence(int value) { 
            if (this.subDetailSequence == null)
              this.subDetailSequence = new PositiveIntType();
            this.subDetailSequence.setValue(value);
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
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
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
        public SubDetailComponent addNoteNumber(int value) { //1
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
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
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
          children.add(new Property("subDetailSequence", "positiveInt", "A service line number.", 0, 1, subDetailSequence));
          children.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -855462510: /*subDetailSequence*/  return new Property("subDetailSequence", "positiveInt", "A service line number.", 0, 1, subDetailSequence);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication);
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
         * List of input service items which this service line is intended to replace.
         */
        @Child(name = "itemSequence", type = {PositiveIntType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service instances", formalDefinition="List of input service items which this service line is intended to replace." )
        protected List<PositiveIntType> itemSequence;

        /**
         * The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        @Child(name = "detailSequence", type = {PositiveIntType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Detail sequence number", formalDefinition="The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition." )
        protected List<PositiveIntType> detailSequence;

        /**
         * The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        @Child(name = "subdetailSequence", type = {PositiveIntType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Subdetail sequence number", formalDefinition="The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition." )
        protected List<PositiveIntType> subdetailSequence;

        /**
         * A code to indicate the Professional Service or Product supplied.
         */
        @Child(name = "service", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Group, Service or Product", formalDefinition="A code to indicate the Professional Service or Product supplied." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept service;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * The fee charged for the professional service or product..
         */
        @Child(name = "fee", type = {Money.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Professional fee or Product charge", formalDefinition="The fee charged for the professional service or product.." )
        protected Money fee;

        /**
         * A list of note references to the notes provided below.
         */
        @Child(name = "noteNumber", type = {PositiveIntType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of note numbers which apply", formalDefinition="A list of note references to the notes provided below." )
        protected List<PositiveIntType> noteNumber;

        /**
         * The adjudications results.
         */
        @Child(name = "adjudication", type = {AdjudicationComponent.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Added items adjudication", formalDefinition="The adjudications results." )
        protected List<AdjudicationComponent> adjudication;

        private static final long serialVersionUID = -245636774L;

    /**
     * Constructor
     */
      public AddedItemComponent() {
        super();
      }

        /**
         * @return {@link #itemSequence} (List of input service items which this service line is intended to replace.)
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
         * @return {@link #itemSequence} (List of input service items which this service line is intended to replace.)
         */
        public PositiveIntType addItemSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.itemSequence == null)
            this.itemSequence = new ArrayList<PositiveIntType>();
          this.itemSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #itemSequence} (List of input service items which this service line is intended to replace.)
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
         * @param value {@link #itemSequence} (List of input service items which this service line is intended to replace.)
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
         * @return {@link #detailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.)
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
         * @return {@link #detailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.)
         */
        public PositiveIntType addDetailSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.detailSequence == null)
            this.detailSequence = new ArrayList<PositiveIntType>();
          this.detailSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #detailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.)
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
         * @param value {@link #detailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.)
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
         * @return {@link #subdetailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.)
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
         * @return {@link #subdetailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.)
         */
        public PositiveIntType addSubdetailSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.subdetailSequence == null)
            this.subdetailSequence = new ArrayList<PositiveIntType>();
          this.subdetailSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #subdetailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.)
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
         * @param value {@link #subdetailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.)
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
         * @return {@link #service} (A code to indicate the Professional Service or Product supplied.)
         */
        public CodeableConcept getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AddedItemComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new CodeableConcept(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (A code to indicate the Professional Service or Product supplied.)
         */
        public AddedItemComponent setService(CodeableConcept value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.)
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
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
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
         * @return {@link #noteNumber} (A list of note references to the notes provided below.)
         */
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
        public AddedItemComponent addNoteNumber(int value) { //1
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
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #adjudication} (The adjudications results.)
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("itemSequence", "positiveInt", "List of input service items which this service line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, itemSequence));
          children.add(new Property("detailSequence", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, java.lang.Integer.MAX_VALUE, detailSequence));
          children.add(new Property("subdetailSequence", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, java.lang.Integer.MAX_VALUE, subdetailSequence));
          children.add(new Property("service", "CodeableConcept", "A code to indicate the Professional Service or Product supplied.", 0, 1, service));
          children.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.", 0, java.lang.Integer.MAX_VALUE, modifier));
          children.add(new Property("fee", "Money", "The fee charged for the professional service or product..", 0, 1, fee));
          children.add(new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber));
          children.add(new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1977979892: /*itemSequence*/  return new Property("itemSequence", "positiveInt", "List of input service items which this service line is intended to replace.", 0, java.lang.Integer.MAX_VALUE, itemSequence);
          case 1321472818: /*detailSequence*/  return new Property("detailSequence", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, java.lang.Integer.MAX_VALUE, detailSequence);
          case 146530674: /*subdetailSequence*/  return new Property("subdetailSequence", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, java.lang.Integer.MAX_VALUE, subdetailSequence);
          case 1984153269: /*service*/  return new Property("service", "CodeableConcept", "A code to indicate the Professional Service or Product supplied.", 0, 1, service);
          case -615513385: /*modifier*/  return new Property("modifier", "CodeableConcept", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.", 0, java.lang.Integer.MAX_VALUE, modifier);
          case 101254: /*fee*/  return new Property("fee", "Money", "The fee charged for the professional service or product..", 0, 1, fee);
          case -1110033957: /*noteNumber*/  return new Property("noteNumber", "positiveInt", "A list of note references to the notes provided below.", 0, java.lang.Integer.MAX_VALUE, noteNumber);
          case -231349275: /*adjudication*/  return new Property("adjudication", "@ClaimResponse.item.adjudication", "The adjudications results.", 0, java.lang.Integer.MAX_VALUE, adjudication);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1977979892: /*itemSequence*/ return this.itemSequence == null ? new Base[0] : this.itemSequence.toArray(new Base[this.itemSequence.size()]); // PositiveIntType
        case 1321472818: /*detailSequence*/ return this.detailSequence == null ? new Base[0] : this.detailSequence.toArray(new Base[this.detailSequence.size()]); // PositiveIntType
        case 146530674: /*subdetailSequence*/ return this.subdetailSequence == null ? new Base[0] : this.subdetailSequence.toArray(new Base[this.subdetailSequence.size()]); // PositiveIntType
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case 101254: /*fee*/ return this.fee == null ? new Base[0] : new Base[] {this.fee}; // Money
        case -1110033957: /*noteNumber*/ return this.noteNumber == null ? new Base[0] : this.noteNumber.toArray(new Base[this.noteNumber.size()]); // PositiveIntType
        case -231349275: /*adjudication*/ return this.adjudication == null ? new Base[0] : this.adjudication.toArray(new Base[this.adjudication.size()]); // AdjudicationComponent
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
        case 1984153269: // service
          this.service = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 101254: // fee
          this.fee = castToMoney(value); // Money
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
        if (name.equals("itemSequence")) {
          this.getItemSequence().add(castToPositiveInt(value));
        } else if (name.equals("detailSequence")) {
          this.getDetailSequence().add(castToPositiveInt(value));
        } else if (name.equals("subdetailSequence")) {
          this.getSubdetailSequence().add(castToPositiveInt(value));
        } else if (name.equals("service")) {
          this.service = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("fee")) {
          this.fee = castToMoney(value); // Money
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
        case 1977979892:  return addItemSequenceElement();
        case 1321472818:  return addDetailSequenceElement();
        case 146530674:  return addSubdetailSequenceElement();
        case 1984153269:  return getService(); 
        case -615513385:  return addModifier(); 
        case 101254:  return getFee(); 
        case -1110033957:  return addNoteNumberElement();
        case -231349275:  return addAdjudication(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1977979892: /*itemSequence*/ return new String[] {"positiveInt"};
        case 1321472818: /*detailSequence*/ return new String[] {"positiveInt"};
        case 146530674: /*subdetailSequence*/ return new String[] {"positiveInt"};
        case 1984153269: /*service*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 101254: /*fee*/ return new String[] {"Money"};
        case -1110033957: /*noteNumber*/ return new String[] {"positiveInt"};
        case -231349275: /*adjudication*/ return new String[] {"@ClaimResponse.item.adjudication"};
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
        else if (name.equals("service")) {
          this.service = new CodeableConcept();
          return this.service;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("fee")) {
          this.fee = new Money();
          return this.fee;
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
        dst.service = service == null ? null : service.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        dst.fee = fee == null ? null : fee.copy();
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
        if (!(other_ instanceof AddedItemComponent))
          return false;
        AddedItemComponent o = (AddedItemComponent) other_;
        return compareDeep(itemSequence, o.itemSequence, true) && compareDeep(detailSequence, o.detailSequence, true)
           && compareDeep(subdetailSequence, o.subdetailSequence, true) && compareDeep(service, o.service, true)
           && compareDeep(modifier, o.modifier, true) && compareDeep(fee, o.fee, true) && compareDeep(noteNumber, o.noteNumber, true)
           && compareDeep(adjudication, o.adjudication, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AddedItemComponent))
          return false;
        AddedItemComponent o = (AddedItemComponent) other_;
        return compareValues(itemSequence, o.itemSequence, true) && compareValues(detailSequence, o.detailSequence, true)
           && compareValues(subdetailSequence, o.subdetailSequence, true) && compareValues(noteNumber, o.noteNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(itemSequence, detailSequence
          , subdetailSequence, service, modifier, fee, noteNumber, adjudication);
      }

  public String fhirType() {
    return "ClaimResponse.addItem";

  }

  }

    @Block()
    public static class ErrorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.
         */
        @Child(name = "itemSequence", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Item sequence number", formalDefinition="The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere." )
        protected PositiveIntType itemSequence;

        /**
         * The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        @Child(name = "detailSequence", type = {PositiveIntType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Detail sequence number", formalDefinition="The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition." )
        protected PositiveIntType detailSequence;

        /**
         * The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        @Child(name = "subDetailSequence", type = {PositiveIntType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Subdetail sequence number", formalDefinition="The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition." )
        protected PositiveIntType subDetailSequence;

        /**
         * An error code,from a specified code system, which details why the claim could not be adjudicated.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Error code detailing processing issues", formalDefinition="An error code,from a specified code system, which details why the claim could not be adjudicated." )
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
         * @return {@link #itemSequence} (The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.). This is the underlying object with id, value and extensions. The accessor "getItemSequence" gives direct access to the value
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
         * @param value {@link #itemSequence} (The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.). This is the underlying object with id, value and extensions. The accessor "getItemSequence" gives direct access to the value
         */
        public ErrorComponent setItemSequenceElement(PositiveIntType value) { 
          this.itemSequence = value;
          return this;
        }

        /**
         * @return The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.
         */
        public int getItemSequence() { 
          return this.itemSequence == null || this.itemSequence.isEmpty() ? 0 : this.itemSequence.getValue();
        }

        /**
         * @param value The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.
         */
        public ErrorComponent setItemSequence(int value) { 
            if (this.itemSequence == null)
              this.itemSequence = new PositiveIntType();
            this.itemSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #detailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.). This is the underlying object with id, value and extensions. The accessor "getDetailSequence" gives direct access to the value
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
         * @param value {@link #detailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.). This is the underlying object with id, value and extensions. The accessor "getDetailSequence" gives direct access to the value
         */
        public ErrorComponent setDetailSequenceElement(PositiveIntType value) { 
          this.detailSequence = value;
          return this;
        }

        /**
         * @return The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        public int getDetailSequence() { 
          return this.detailSequence == null || this.detailSequence.isEmpty() ? 0 : this.detailSequence.getValue();
        }

        /**
         * @param value The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        public ErrorComponent setDetailSequence(int value) { 
            if (this.detailSequence == null)
              this.detailSequence = new PositiveIntType();
            this.detailSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #subDetailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.). This is the underlying object with id, value and extensions. The accessor "getSubDetailSequence" gives direct access to the value
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
         * @param value {@link #subDetailSequence} (The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.). This is the underlying object with id, value and extensions. The accessor "getSubDetailSequence" gives direct access to the value
         */
        public ErrorComponent setSubDetailSequenceElement(PositiveIntType value) { 
          this.subDetailSequence = value;
          return this;
        }

        /**
         * @return The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        public int getSubDetailSequence() { 
          return this.subDetailSequence == null || this.subDetailSequence.isEmpty() ? 0 : this.subDetailSequence.getValue();
        }

        /**
         * @param value The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.
         */
        public ErrorComponent setSubDetailSequence(int value) { 
            if (this.subDetailSequence == null)
              this.subDetailSequence = new PositiveIntType();
            this.subDetailSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (An error code,from a specified code system, which details why the claim could not be adjudicated.)
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
         * @param value {@link #code} (An error code,from a specified code system, which details why the claim could not be adjudicated.)
         */
        public ErrorComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("itemSequence", "positiveInt", "The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.", 0, 1, itemSequence));
          children.add(new Property("detailSequence", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, 1, detailSequence));
          children.add(new Property("subDetailSequence", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, 1, subDetailSequence));
          children.add(new Property("code", "CodeableConcept", "An error code,from a specified code system, which details why the claim could not be adjudicated.", 0, 1, code));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1977979892: /*itemSequence*/  return new Property("itemSequence", "positiveInt", "The sequence number of the line item submitted which contains the error. This value is omitted when the error is elsewhere.", 0, 1, itemSequence);
          case 1321472818: /*detailSequence*/  return new Property("detailSequence", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, 1, detailSequence);
          case -855462510: /*subDetailSequence*/  return new Property("subDetailSequence", "positiveInt", "The sequence number of the addition within the line item submitted which contains the error. This value is omitted when the error is not related to an Addition.", 0, 1, subDetailSequence);
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "An error code,from a specified code system, which details why the claim could not be adjudicated.", 0, 1, code);
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

    @Block()
    public static class TotalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code indicating: Submitted, Co-Pay, deductable, elegible, benefit, tax, etc.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication category such as submitted, co-pay, eligible, benefit, etc.", formalDefinition="Code indicating: Submitted, Co-Pay, deductable, elegible, benefit, tax, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication")
        protected CodeableConcept category;

        /**
         * Monitory amount associated with the code.
         */
        @Child(name = "amount", type = {Money.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Monetary amount", formalDefinition="Monitory amount associated with the code." )
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
         * @return {@link #category} (Code indicating: Submitted, Co-Pay, deductable, elegible, benefit, tax, etc.)
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
         * @param value {@link #category} (Code indicating: Submitted, Co-Pay, deductable, elegible, benefit, tax, etc.)
         */
        public TotalComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #amount} (Monitory amount associated with the code.)
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
         * @param value {@link #amount} (Monitory amount associated with the code.)
         */
        public TotalComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("category", "CodeableConcept", "Code indicating: Submitted, Co-Pay, deductable, elegible, benefit, tax, etc.", 0, 1, category));
          children.add(new Property("amount", "Money", "Monitory amount associated with the code.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "Code indicating: Submitted, Co-Pay, deductable, elegible, benefit, tax, etc.", 0, 1, category);
          case -1413853096: /*amount*/  return new Property("amount", "Money", "Monitory amount associated with the code.", 0, 1, amount);
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
         * Whether this represents partial or complete payment of the claim.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Partial or Complete", formalDefinition="Whether this represents partial or complete payment of the claim." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-paymenttype")
        protected CodeableConcept type;

        /**
         * Adjustment to the payment of this transaction which is not related to adjudication of this transaction.
         */
        @Child(name = "adjustment", type = {Money.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Payment adjustment for non-Claim issues", formalDefinition="Adjustment to the payment of this transaction which is not related to adjudication of this transaction." )
        protected Money adjustment;

        /**
         * Reason for the payment adjustment.
         */
        @Child(name = "adjustmentReason", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Explanation for the non-claim adjustment", formalDefinition="Reason for the payment adjustment." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payment-adjustment-reason")
        protected CodeableConcept adjustmentReason;

        /**
         * Estimated payment data.
         */
        @Child(name = "date", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Expected data of Payment", formalDefinition="Estimated payment data." )
        protected DateType date;

        /**
         * Payable less any payment adjustment.
         */
        @Child(name = "amount", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Payable amount after adjustment", formalDefinition="Payable less any payment adjustment." )
        protected Money amount;

        /**
         * Payment identifier.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifier of the payment instrument", formalDefinition="Payment identifier." )
        protected Identifier identifier;

        private static final long serialVersionUID = 1539906026L;

    /**
     * Constructor
     */
      public PaymentComponent() {
        super();
      }

        /**
         * @return {@link #type} (Whether this represents partial or complete payment of the claim.)
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
         * @param value {@link #type} (Whether this represents partial or complete payment of the claim.)
         */
        public PaymentComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #adjustment} (Adjustment to the payment of this transaction which is not related to adjudication of this transaction.)
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
         * @param value {@link #adjustment} (Adjustment to the payment of this transaction which is not related to adjudication of this transaction.)
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
         * @return {@link #date} (Estimated payment data.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
         * @param value {@link #date} (Estimated payment data.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public PaymentComponent setDateElement(DateType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Estimated payment data.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Estimated payment data.
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
         * @return {@link #amount} (Payable less any payment adjustment.)
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
         * @param value {@link #amount} (Payable less any payment adjustment.)
         */
        public PaymentComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #identifier} (Payment identifier.)
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
         * @param value {@link #identifier} (Payment identifier.)
         */
        public PaymentComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Whether this represents partial or complete payment of the claim.", 0, 1, type));
          children.add(new Property("adjustment", "Money", "Adjustment to the payment of this transaction which is not related to adjudication of this transaction.", 0, 1, adjustment));
          children.add(new Property("adjustmentReason", "CodeableConcept", "Reason for the payment adjustment.", 0, 1, adjustmentReason));
          children.add(new Property("date", "date", "Estimated payment data.", 0, 1, date));
          children.add(new Property("amount", "Money", "Payable less any payment adjustment.", 0, 1, amount));
          children.add(new Property("identifier", "Identifier", "Payment identifier.", 0, 1, identifier));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Whether this represents partial or complete payment of the claim.", 0, 1, type);
          case 1977085293: /*adjustment*/  return new Property("adjustment", "Money", "Adjustment to the payment of this transaction which is not related to adjudication of this transaction.", 0, 1, adjustment);
          case -1255938543: /*adjustmentReason*/  return new Property("adjustmentReason", "CodeableConcept", "Reason for the payment adjustment.", 0, 1, adjustmentReason);
          case 3076014: /*date*/  return new Property("date", "date", "Estimated payment data.", 0, 1, date);
          case -1413853096: /*amount*/  return new Property("amount", "Money", "Payable less any payment adjustment.", 0, 1, amount);
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Payment identifier.", 0, 1, identifier);
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
         * An integer associated with each note which may be referred to from each service line item.
         */
        @Child(name = "number", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Sequence Number for this note", formalDefinition="An integer associated with each note which may be referred to from each service line item." )
        protected PositiveIntType number;

        /**
         * The note purpose: Print/Display.
         */
        @Child(name = "type", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The note purpose: Print/Display." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/note-type")
        protected Enumeration<NoteType> type;

        /**
         * The note text.
         */
        @Child(name = "text", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Note explanatory text", formalDefinition="The note text." )
        protected StringType text;

        /**
         * The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.
         */
        @Child(name = "language", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language if different from the resource", formalDefinition="The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English." )
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
         * @return {@link #number} (An integer associated with each note which may be referred to from each service line item.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
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
         * @param value {@link #number} (An integer associated with each note which may be referred to from each service line item.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public NoteComponent setNumberElement(PositiveIntType value) { 
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
        public NoteComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new PositiveIntType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The note purpose: Print/Display.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
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
         * @param value {@link #type} (The note purpose: Print/Display.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public NoteComponent setTypeElement(Enumeration<NoteType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The note purpose: Print/Display.
         */
        public NoteType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The note purpose: Print/Display.
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
         * @return {@link #text} (The note text.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
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
         * @param value {@link #text} (The note text.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public NoteComponent setTextElement(StringType value) { 
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
        public NoteComponent setText(String value) { 
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
         * @return {@link #language} (The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.)
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
         * @param value {@link #language} (The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.)
         */
        public NoteComponent setLanguage(CodeableConcept value) { 
          this.language = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("number", "positiveInt", "An integer associated with each note which may be referred to from each service line item.", 0, 1, number));
          children.add(new Property("type", "code", "The note purpose: Print/Display.", 0, 1, type));
          children.add(new Property("text", "string", "The note text.", 0, 1, text));
          children.add(new Property("language", "CodeableConcept", "The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English.", 0, 1, language));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1034364087: /*number*/  return new Property("number", "positiveInt", "An integer associated with each note which may be referred to from each service line item.", 0, 1, number);
          case 3575610: /*type*/  return new Property("type", "code", "The note purpose: Print/Display.", 0, 1, type);
          case 3556653: /*text*/  return new Property("text", "string", "The note text.", 0, 1, text);
          case -1613589672: /*language*/  return new Property("language", "CodeableConcept", "The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English.", 0, 1, language);
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
         * A service line item.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service instance identifier", formalDefinition="A service line item." )
        protected PositiveIntType sequence;

        /**
         * The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.
         */
        @Child(name = "focal", type = {BooleanType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Is the focal Coverage", formalDefinition="The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated." )
        protected BooleanType focal;

        /**
         * Reference to the program or plan identification, underwriter or payor.
         */
        @Child(name = "coverage", type = {Coverage.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the program or plan identification, underwriter or payor." )
        protected Reference coverage;

        /**
         * The actual object that is the target of the reference (Reference to the program or plan identification, underwriter or payor.)
         */
        protected Coverage coverageTarget;

        /**
         * The contract number of a business agreement which describes the terms and conditions.
         */
        @Child(name = "businessArrangement", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Business agreement", formalDefinition="The contract number of a business agreement which describes the terms and conditions." )
        protected StringType businessArrangement;

        /**
         * A list of references from the Insurer to which these services pertain.
         */
        @Child(name = "preAuthRef", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pre-Authorization/Determination Reference", formalDefinition="A list of references from the Insurer to which these services pertain." )
        protected List<StringType> preAuthRef;

        /**
         * The Coverages adjudication details.
         */
        @Child(name = "claimResponse", type = {ClaimResponse.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Adjudication results", formalDefinition="The Coverages adjudication details." )
        protected Reference claimResponse;

        /**
         * The actual object that is the target of the reference (The Coverages adjudication details.)
         */
        protected ClaimResponse claimResponseTarget;

        private static final long serialVersionUID = -1216535489L;

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
         * @return {@link #sequence} (A service line item.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
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
         * @param value {@link #sequence} (A service line item.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public InsuranceComponent setSequenceElement(PositiveIntType value) { 
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
        public InsuranceComponent setSequence(int value) { 
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
         * @param value {@link #focal} (The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public InsuranceComponent setFocalElement(BooleanType value) { 
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
        public InsuranceComponent setFocal(boolean value) { 
            if (this.focal == null)
              this.focal = new BooleanType();
            this.focal.setValue(value);
          return this;
        }

        /**
         * @return {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
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
         * @param value {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
         */
        public InsuranceComponent setCoverage(Reference value) { 
          this.coverage = value;
          return this;
        }

        /**
         * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
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
         * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
         */
        public InsuranceComponent setCoverageTarget(Coverage value) { 
          this.coverageTarget = value;
          return this;
        }

        /**
         * @return {@link #businessArrangement} (The contract number of a business agreement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
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
         * @param value {@link #businessArrangement} (The contract number of a business agreement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public InsuranceComponent setBusinessArrangementElement(StringType value) { 
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
         * @return {@link #preAuthRef} (A list of references from the Insurer to which these services pertain.)
         */
        public List<StringType> getPreAuthRef() { 
          if (this.preAuthRef == null)
            this.preAuthRef = new ArrayList<StringType>();
          return this.preAuthRef;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public InsuranceComponent setPreAuthRef(List<StringType> thePreAuthRef) { 
          this.preAuthRef = thePreAuthRef;
          return this;
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
        public InsuranceComponent addPreAuthRef(String value) { //1
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
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #claimResponse} (The Coverages adjudication details.)
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
         * @param value {@link #claimResponse} (The Coverages adjudication details.)
         */
        public InsuranceComponent setClaimResponse(Reference value) { 
          this.claimResponse = value;
          return this;
        }

        /**
         * @return {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Coverages adjudication details.)
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
         * @param value {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Coverages adjudication details.)
         */
        public InsuranceComponent setClaimResponseTarget(ClaimResponse value) { 
          this.claimResponseTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A service line item.", 0, 1, sequence));
          children.add(new Property("focal", "boolean", "The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.", 0, 1, focal));
          children.add(new Property("coverage", "Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, 1, coverage));
          children.add(new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, 1, businessArrangement));
          children.add(new Property("preAuthRef", "string", "A list of references from the Insurer to which these services pertain.", 0, java.lang.Integer.MAX_VALUE, preAuthRef));
          children.add(new Property("claimResponse", "Reference(ClaimResponse)", "The Coverages adjudication details.", 0, 1, claimResponse));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A service line item.", 0, 1, sequence);
          case 97604197: /*focal*/  return new Property("focal", "boolean", "The instance number of the Coverage which is the focus for adjudication. The Coverage against which the claim is to be adjudicated.", 0, 1, focal);
          case -351767064: /*coverage*/  return new Property("coverage", "Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, 1, coverage);
          case 259920682: /*businessArrangement*/  return new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, 1, businessArrangement);
          case 522246568: /*preAuthRef*/  return new Property("preAuthRef", "string", "A list of references from the Insurer to which these services pertain.", 0, java.lang.Integer.MAX_VALUE, preAuthRef);
          case 689513629: /*claimResponse*/  return new Property("claimResponse", "Reference(ClaimResponse)", "The Coverages adjudication details.", 0, 1, claimResponse);
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
        case 522246568: /*preAuthRef*/ return this.preAuthRef == null ? new Base[0] : this.preAuthRef.toArray(new Base[this.preAuthRef.size()]); // StringType
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
        case 522246568: // preAuthRef
          this.getPreAuthRef().add(castToString(value)); // StringType
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
        } else if (name.equals("preAuthRef")) {
          this.getPreAuthRef().add(castToString(value));
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
        case 522246568:  return addPreAuthRefElement();
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
        case 522246568: /*preAuthRef*/ return new String[] {"string"};
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

      public InsuranceComponent copy() {
        InsuranceComponent dst = new InsuranceComponent();
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(focal, o.focal, true) && compareDeep(coverage, o.coverage, true)
           && compareDeep(businessArrangement, o.businessArrangement, true) && compareDeep(preAuthRef, o.preAuthRef, true)
           && compareDeep(claimResponse, o.claimResponse, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other_;
        return compareValues(sequence, o.sequence, true) && compareValues(focal, o.focal, true) && compareValues(businessArrangement, o.businessArrangement, true)
           && compareValues(preAuthRef, o.preAuthRef, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, focal, coverage
          , businessArrangement, preAuthRef, claimResponse);
      }

  public String fhirType() {
    return "ClaimResponse.insurance";

  }

  }

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Response  number", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<ClaimResponseStatus> status;

    /**
     * The category of claim, eg, oral, pharmacy, vision, insitutional, professional.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Type or discipline", formalDefinition="The category of claim, eg, oral, pharmacy, vision, insitutional, professional." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-type")
    protected CodeableConcept type;

    /**
     * A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.
     */
    @Child(name = "subType", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Finer grained claim type information", formalDefinition="A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-subtype")
    protected List<CodeableConcept> subType;

    /**
     * Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    @Child(name = "use", type = {CodeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="complete | proposed | exploratory | other", formalDefinition="Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-use")
    protected Enumeration<Use> use;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Patient.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient Resource.)
     */
    protected Patient patientTarget;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The Insurer who produced this adjudicated response.
     */
    @Child(name = "insurer", type = {Organization.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurance issuing organization", formalDefinition="The Insurer who produced this adjudicated response." )
    protected Reference insurer;

    /**
     * The actual object that is the target of the reference (The Insurer who produced this adjudicated response.)
     */
    protected Organization insurerTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference requestProvider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Resource requestProviderTarget;

    /**
     * Original request resource referrence.
     */
    @Child(name = "request", type = {Claim.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Id of resource triggering adjudication", formalDefinition="Original request resource referrence." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Original request resource referrence.)
     */
    protected Claim requestTarget;

    /**
     * Transaction: error, complete, partial processing.
     */
    @Child(name = "outcome", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="queued | complete | error | partial", formalDefinition="Transaction: error, complete, partial processing." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/remittance-outcome")
    protected Enumeration<RemittanceOutcome> outcome;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * Party to be reimbursed: Subscriber, provider, other.
     */
    @Child(name = "payeeType", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Party to be paid any benefits payable", formalDefinition="Party to be reimbursed: Subscriber, provider, other." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/payeetype")
    protected CodeableConcept payeeType;

    /**
     * The first tier service adjudications for submitted services.
     */
    @Child(name = "item", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Line items", formalDefinition="The first tier service adjudications for submitted services." )
    protected List<ItemComponent> item;

    /**
     * The first tier service adjudications for payor added services.
     */
    @Child(name = "addItem", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Insurer added line items", formalDefinition="The first tier service adjudications for payor added services." )
    protected List<AddedItemComponent> addItem;

    /**
     * Mutually exclusive with Services Provided (Item).
     */
    @Child(name = "error", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Processing errors", formalDefinition="Mutually exclusive with Services Provided (Item)." )
    protected List<ErrorComponent> error;

    /**
     * Totals for amounts submitted, co-pays, benefits payable etc.
     */
    @Child(name = "total", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Adjudication totals", formalDefinition="Totals for amounts submitted, co-pays, benefits payable etc." )
    protected List<TotalComponent> total;

    /**
     * Payment details for the claim if the claim has been paid.
     */
    @Child(name = "payment", type = {}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Payment details, if paid", formalDefinition="Payment details for the claim if the claim has been paid." )
    protected PaymentComponent payment;

    /**
     * Status of funds reservation (For provider, for Patient, None).
     */
    @Child(name = "reserved", type = {Coding.class}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Funds reserved status", formalDefinition="Status of funds reservation (For provider, for Patient, None)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fundsreserve")
    protected Coding reserved;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {CodeableConcept.class}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/forms")
    protected CodeableConcept form;

    /**
     * Note text.
     */
    @Child(name = "processNote", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Processing notes", formalDefinition="Note text." )
    protected List<NoteComponent> processNote;

    /**
     * Request for additional supporting or authorizing information, such as: documents, images or resources.
     */
    @Child(name = "communicationRequest", type = {CommunicationRequest.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Request for additional information", formalDefinition="Request for additional supporting or authorizing information, such as: documents, images or resources." )
    protected List<Reference> communicationRequest;
    /**
     * The actual objects that are the target of the reference (Request for additional supporting or authorizing information, such as: documents, images or resources.)
     */
    protected List<CommunicationRequest> communicationRequestTarget;


    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "insurance", type = {}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected List<InsuranceComponent> insurance;

    private static final long serialVersionUID = 2049245539L;

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
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ClaimResponseStatus>(new ClaimResponseStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (The category of claim, eg, oral, pharmacy, vision, insitutional, professional.)
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
     * @param value {@link #type} (The category of claim, eg, oral, pharmacy, vision, insitutional, professional.)
     */
    public ClaimResponse setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subType} (A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.)
     */
    public List<CodeableConcept> getSubType() { 
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      return this.subType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClaimResponse setSubType(List<CodeableConcept> theSubType) { 
      this.subType = theSubType;
      return this;
    }

    public boolean hasSubType() { 
      if (this.subType == null)
        return false;
      for (CodeableConcept item : this.subType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addSubType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return t;
    }

    public ClaimResponse addSubType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subType}, creating it if it does not already exist
     */
    public CodeableConcept getSubTypeFirstRep() { 
      if (getSubType().isEmpty()) {
        addSubType();
      }
      return getSubType().get(0);
    }

    /**
     * @return {@link #use} (Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
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
     * @param value {@link #use} (Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public ClaimResponse setUseElement(Enumeration<Use> value) { 
      this.use = value;
      return this;
    }

    /**
     * @return Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    public Use getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    public ClaimResponse setUse(Use value) { 
      if (value == null)
        this.use = null;
      else {
        if (this.use == null)
          this.use = new Enumeration<Use>(new UseEnumFactory());
        this.use.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
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
     * @param value {@link #patient} (Patient Resource.)
     */
    public ClaimResponse setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient Resource.)
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
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public ClaimResponse setPatientTarget(Patient value) { 
      this.patientTarget = value;
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
     * @return {@link #insurer} (The Insurer who produced this adjudicated response.)
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
     * @param value {@link #insurer} (The Insurer who produced this adjudicated response.)
     */
    public ClaimResponse setInsurer(Reference value) { 
      this.insurer = value;
      return this;
    }

    /**
     * @return {@link #insurer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who produced this adjudicated response.)
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
     * @param value {@link #insurer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who produced this adjudicated response.)
     */
    public ClaimResponse setInsurerTarget(Organization value) { 
      this.insurerTarget = value;
      return this;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getRequestProvider() { 
      if (this.requestProvider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.requestProvider");
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
    public ClaimResponse setRequestProvider(Reference value) { 
      this.requestProvider = value;
      return this;
    }

    /**
     * @return {@link #requestProvider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Resource getRequestProviderTarget() { 
      return this.requestProviderTarget;
    }

    /**
     * @param value {@link #requestProvider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public ClaimResponse setRequestProviderTarget(Resource value) { 
      this.requestProviderTarget = value;
      return this;
    }

    /**
     * @return {@link #request} (Original request resource referrence.)
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
     * @param value {@link #request} (Original request resource referrence.)
     */
    public ClaimResponse setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original request resource referrence.)
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
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original request resource referrence.)
     */
    public ClaimResponse setRequestTarget(Claim value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #outcome} (Transaction: error, complete, partial processing.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
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
     * @param value {@link #outcome} (Transaction: error, complete, partial processing.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public ClaimResponse setOutcomeElement(Enumeration<RemittanceOutcome> value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return Transaction: error, complete, partial processing.
     */
    public RemittanceOutcome getOutcome() { 
      return this.outcome == null ? null : this.outcome.getValue();
    }

    /**
     * @param value Transaction: error, complete, partial processing.
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
     * @param value {@link #payeeType} (Party to be reimbursed: Subscriber, provider, other.)
     */
    public ClaimResponse setPayeeType(CodeableConcept value) { 
      this.payeeType = value;
      return this;
    }

    /**
     * @return {@link #item} (The first tier service adjudications for submitted services.)
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
     * @return {@link #addItem} (The first tier service adjudications for payor added services.)
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
     * @return {@link #error} (Mutually exclusive with Services Provided (Item).)
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

    /**
     * @return {@link #total} (Totals for amounts submitted, co-pays, benefits payable etc.)
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
     * @return {@link #payment} (Payment details for the claim if the claim has been paid.)
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
     * @param value {@link #payment} (Payment details for the claim if the claim has been paid.)
     */
    public ClaimResponse setPayment(PaymentComponent value) { 
      this.payment = value;
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
    public CodeableConcept getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClaimResponse.form");
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
    public ClaimResponse setForm(CodeableConcept value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #processNote} (Note text.)
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
     * @return {@link #communicationRequest} (Request for additional supporting or authorizing information, such as: documents, images or resources.)
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
     * @return {@link #insurance} (Financial instrument by which payment information for health care.)
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

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The status of the resource instance.", 0, 1, status));
        children.add(new Property("type", "CodeableConcept", "The category of claim, eg, oral, pharmacy, vision, insitutional, professional.", 0, 1, type));
        children.add(new Property("subType", "CodeableConcept", "A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.", 0, java.lang.Integer.MAX_VALUE, subType));
        children.add(new Property("use", "code", "Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).", 0, 1, use));
        children.add(new Property("patient", "Reference(Patient)", "Patient Resource.", 0, 1, patient));
        children.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, 1, created));
        children.add(new Property("insurer", "Reference(Organization)", "The Insurer who produced this adjudicated response.", 0, 1, insurer));
        children.add(new Property("requestProvider", "Reference(Practitioner|PractitionerRole|Organization)", "The practitioner who is responsible for the services rendered to the patient.", 0, 1, requestProvider));
        children.add(new Property("request", "Reference(Claim)", "Original request resource referrence.", 0, 1, request));
        children.add(new Property("outcome", "code", "Transaction: error, complete, partial processing.", 0, 1, outcome));
        children.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, 1, disposition));
        children.add(new Property("payeeType", "CodeableConcept", "Party to be reimbursed: Subscriber, provider, other.", 0, 1, payeeType));
        children.add(new Property("item", "", "The first tier service adjudications for submitted services.", 0, java.lang.Integer.MAX_VALUE, item));
        children.add(new Property("addItem", "", "The first tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, addItem));
        children.add(new Property("error", "", "Mutually exclusive with Services Provided (Item).", 0, java.lang.Integer.MAX_VALUE, error));
        children.add(new Property("total", "", "Totals for amounts submitted, co-pays, benefits payable etc.", 0, java.lang.Integer.MAX_VALUE, total));
        children.add(new Property("payment", "", "Payment details for the claim if the claim has been paid.", 0, 1, payment));
        children.add(new Property("reserved", "Coding", "Status of funds reservation (For provider, for Patient, None).", 0, 1, reserved));
        children.add(new Property("form", "CodeableConcept", "The form to be used for printing the content.", 0, 1, form));
        children.add(new Property("processNote", "", "Note text.", 0, java.lang.Integer.MAX_VALUE, processNote));
        children.add(new Property("communicationRequest", "Reference(CommunicationRequest)", "Request for additional supporting or authorizing information, such as: documents, images or resources.", 0, java.lang.Integer.MAX_VALUE, communicationRequest));
        children.add(new Property("insurance", "", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, insurance));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the resource instance.", 0, 1, status);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The category of claim, eg, oral, pharmacy, vision, insitutional, professional.", 0, 1, type);
        case -1868521062: /*subType*/  return new Property("subType", "CodeableConcept", "A finer grained suite of claim subtype codes which may convey Inpatient vs Outpatient and/or a specialty service. In the US the BillType.", 0, java.lang.Integer.MAX_VALUE, subType);
        case 116103: /*use*/  return new Property("use", "code", "Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).", 0, 1, use);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "Patient Resource.", 0, 1, patient);
        case 1028554472: /*created*/  return new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, 1, created);
        case 1957615864: /*insurer*/  return new Property("insurer", "Reference(Organization)", "The Insurer who produced this adjudicated response.", 0, 1, insurer);
        case 1601527200: /*requestProvider*/  return new Property("requestProvider", "Reference(Practitioner|PractitionerRole|Organization)", "The practitioner who is responsible for the services rendered to the patient.", 0, 1, requestProvider);
        case 1095692943: /*request*/  return new Property("request", "Reference(Claim)", "Original request resource referrence.", 0, 1, request);
        case -1106507950: /*outcome*/  return new Property("outcome", "code", "Transaction: error, complete, partial processing.", 0, 1, outcome);
        case 583380919: /*disposition*/  return new Property("disposition", "string", "A description of the status of the adjudication.", 0, 1, disposition);
        case -316321118: /*payeeType*/  return new Property("payeeType", "CodeableConcept", "Party to be reimbursed: Subscriber, provider, other.", 0, 1, payeeType);
        case 3242771: /*item*/  return new Property("item", "", "The first tier service adjudications for submitted services.", 0, java.lang.Integer.MAX_VALUE, item);
        case -1148899500: /*addItem*/  return new Property("addItem", "", "The first tier service adjudications for payor added services.", 0, java.lang.Integer.MAX_VALUE, addItem);
        case 96784904: /*error*/  return new Property("error", "", "Mutually exclusive with Services Provided (Item).", 0, java.lang.Integer.MAX_VALUE, error);
        case 110549828: /*total*/  return new Property("total", "", "Totals for amounts submitted, co-pays, benefits payable etc.", 0, java.lang.Integer.MAX_VALUE, total);
        case -786681338: /*payment*/  return new Property("payment", "", "Payment details for the claim if the claim has been paid.", 0, 1, payment);
        case -350385368: /*reserved*/  return new Property("reserved", "Coding", "Status of funds reservation (For provider, for Patient, None).", 0, 1, reserved);
        case 3148996: /*form*/  return new Property("form", "CodeableConcept", "The form to be used for printing the content.", 0, 1, form);
        case 202339073: /*processNote*/  return new Property("processNote", "", "Note text.", 0, java.lang.Integer.MAX_VALUE, processNote);
        case -2071896615: /*communicationRequest*/  return new Property("communicationRequest", "Reference(CommunicationRequest)", "Request for additional supporting or authorizing information, such as: documents, images or resources.", 0, java.lang.Integer.MAX_VALUE, communicationRequest);
        case 73049818: /*insurance*/  return new Property("insurance", "", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, insurance);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ClaimResponseStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : this.subType.toArray(new Base[this.subType.size()]); // CodeableConcept
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // Enumeration<Use>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Reference
        case 1601527200: /*requestProvider*/ return this.requestProvider == null ? new Base[0] : new Base[] {this.requestProvider}; // Reference
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // Enumeration<RemittanceOutcome>
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case -316321118: /*payeeType*/ return this.payeeType == null ? new Base[0] : new Base[] {this.payeeType}; // CodeableConcept
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // ItemComponent
        case -1148899500: /*addItem*/ return this.addItem == null ? new Base[0] : this.addItem.toArray(new Base[this.addItem.size()]); // AddedItemComponent
        case 96784904: /*error*/ return this.error == null ? new Base[0] : this.error.toArray(new Base[this.error.size()]); // ErrorComponent
        case 110549828: /*total*/ return this.total == null ? new Base[0] : this.total.toArray(new Base[this.total.size()]); // TotalComponent
        case -786681338: /*payment*/ return this.payment == null ? new Base[0] : new Base[] {this.payment}; // PaymentComponent
        case -350385368: /*reserved*/ return this.reserved == null ? new Base[0] : new Base[] {this.reserved}; // Coding
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // CodeableConcept
        case 202339073: /*processNote*/ return this.processNote == null ? new Base[0] : this.processNote.toArray(new Base[this.processNote.size()]); // NoteComponent
        case -2071896615: /*communicationRequest*/ return this.communicationRequest == null ? new Base[0] : this.communicationRequest.toArray(new Base[this.communicationRequest.size()]); // Reference
        case 73049818: /*insurance*/ return this.insurance == null ? new Base[0] : this.insurance.toArray(new Base[this.insurance.size()]); // InsuranceComponent
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
          this.getSubType().add(castToCodeableConcept(value)); // CodeableConcept
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
        case 1601527200: // requestProvider
          this.requestProvider = castToReference(value); // Reference
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
        case -316321118: // payeeType
          this.payeeType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3242771: // item
          this.getItem().add((ItemComponent) value); // ItemComponent
          return value;
        case -1148899500: // addItem
          this.getAddItem().add((AddedItemComponent) value); // AddedItemComponent
          return value;
        case 96784904: // error
          this.getError().add((ErrorComponent) value); // ErrorComponent
          return value;
        case 110549828: // total
          this.getTotal().add((TotalComponent) value); // TotalComponent
          return value;
        case -786681338: // payment
          this.payment = (PaymentComponent) value; // PaymentComponent
          return value;
        case -350385368: // reserved
          this.reserved = castToCoding(value); // Coding
          return value;
        case 3148996: // form
          this.form = castToCodeableConcept(value); // CodeableConcept
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
          this.getSubType().add(castToCodeableConcept(value));
        } else if (name.equals("use")) {
          value = new UseEnumFactory().fromType(castToCode(value));
          this.use = (Enumeration) value; // Enumeration<Use>
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("insurer")) {
          this.insurer = castToReference(value); // Reference
        } else if (name.equals("requestProvider")) {
          this.requestProvider = castToReference(value); // Reference
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("outcome")) {
          value = new RemittanceOutcomeEnumFactory().fromType(castToCode(value));
          this.outcome = (Enumeration) value; // Enumeration<RemittanceOutcome>
        } else if (name.equals("disposition")) {
          this.disposition = castToString(value); // StringType
        } else if (name.equals("payeeType")) {
          this.payeeType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("item")) {
          this.getItem().add((ItemComponent) value);
        } else if (name.equals("addItem")) {
          this.getAddItem().add((AddedItemComponent) value);
        } else if (name.equals("error")) {
          this.getError().add((ErrorComponent) value);
        } else if (name.equals("total")) {
          this.getTotal().add((TotalComponent) value);
        } else if (name.equals("payment")) {
          this.payment = (PaymentComponent) value; // PaymentComponent
        } else if (name.equals("reserved")) {
          this.reserved = castToCoding(value); // Coding
        } else if (name.equals("form")) {
          this.form = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("processNote")) {
          this.getProcessNote().add((NoteComponent) value);
        } else if (name.equals("communicationRequest")) {
          this.getCommunicationRequest().add(castToReference(value));
        } else if (name.equals("insurance")) {
          this.getInsurance().add((InsuranceComponent) value);
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
        case -1868521062:  return addSubType(); 
        case 116103:  return getUseElement();
        case -791418107:  return getPatient(); 
        case 1028554472:  return getCreatedElement();
        case 1957615864:  return getInsurer(); 
        case 1601527200:  return getRequestProvider(); 
        case 1095692943:  return getRequest(); 
        case -1106507950:  return getOutcomeElement();
        case 583380919:  return getDispositionElement();
        case -316321118:  return getPayeeType(); 
        case 3242771:  return addItem(); 
        case -1148899500:  return addAddItem(); 
        case 96784904:  return addError(); 
        case 110549828:  return addTotal(); 
        case -786681338:  return getPayment(); 
        case -350385368:  return getReserved(); 
        case 3148996:  return getForm(); 
        case 202339073:  return addProcessNote(); 
        case -2071896615:  return addCommunicationRequest(); 
        case 73049818:  return addInsurance(); 
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
        case 1601527200: /*requestProvider*/ return new String[] {"Reference"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -1106507950: /*outcome*/ return new String[] {"code"};
        case 583380919: /*disposition*/ return new String[] {"string"};
        case -316321118: /*payeeType*/ return new String[] {"CodeableConcept"};
        case 3242771: /*item*/ return new String[] {};
        case -1148899500: /*addItem*/ return new String[] {};
        case 96784904: /*error*/ return new String[] {};
        case 110549828: /*total*/ return new String[] {};
        case -786681338: /*payment*/ return new String[] {};
        case -350385368: /*reserved*/ return new String[] {"Coding"};
        case 3148996: /*form*/ return new String[] {"CodeableConcept"};
        case 202339073: /*processNote*/ return new String[] {};
        case -2071896615: /*communicationRequest*/ return new String[] {"Reference"};
        case 73049818: /*insurance*/ return new String[] {};
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
          return addSubType();
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
        else if (name.equals("requestProvider")) {
          this.requestProvider = new Reference();
          return this.requestProvider;
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
        else if (name.equals("error")) {
          return addError();
        }
        else if (name.equals("total")) {
          return addTotal();
        }
        else if (name.equals("payment")) {
          this.payment = new PaymentComponent();
          return this.payment;
        }
        else if (name.equals("reserved")) {
          this.reserved = new Coding();
          return this.reserved;
        }
        else if (name.equals("form")) {
          this.form = new CodeableConcept();
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
        if (subType != null) {
          dst.subType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subType)
            dst.subType.add(i.copy());
        };
        dst.use = use == null ? null : use.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.created = created == null ? null : created.copy();
        dst.insurer = insurer == null ? null : insurer.copy();
        dst.requestProvider = requestProvider == null ? null : requestProvider.copy();
        dst.request = request == null ? null : request.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
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
        if (error != null) {
          dst.error = new ArrayList<ErrorComponent>();
          for (ErrorComponent i : error)
            dst.error.add(i.copy());
        };
        if (total != null) {
          dst.total = new ArrayList<TotalComponent>();
          for (TotalComponent i : total)
            dst.total.add(i.copy());
        };
        dst.payment = payment == null ? null : payment.copy();
        dst.reserved = reserved == null ? null : reserved.copy();
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
           && compareDeep(created, o.created, true) && compareDeep(insurer, o.insurer, true) && compareDeep(requestProvider, o.requestProvider, true)
           && compareDeep(request, o.request, true) && compareDeep(outcome, o.outcome, true) && compareDeep(disposition, o.disposition, true)
           && compareDeep(payeeType, o.payeeType, true) && compareDeep(item, o.item, true) && compareDeep(addItem, o.addItem, true)
           && compareDeep(error, o.error, true) && compareDeep(total, o.total, true) && compareDeep(payment, o.payment, true)
           && compareDeep(reserved, o.reserved, true) && compareDeep(form, o.form, true) && compareDeep(processNote, o.processNote, true)
           && compareDeep(communicationRequest, o.communicationRequest, true) && compareDeep(insurance, o.insurance, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ClaimResponse))
          return false;
        ClaimResponse o = (ClaimResponse) other_;
        return compareValues(status, o.status, true) && compareValues(use, o.use, true) && compareValues(created, o.created, true)
           && compareValues(outcome, o.outcome, true) && compareValues(disposition, o.disposition, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, type
          , subType, use, patient, created, insurer, requestProvider, request, outcome
          , disposition, payeeType, item, addItem, error, total, payment, reserved, form
          , processNote, communicationRequest, insurance);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ClaimResponse;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identity of the claimresponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ClaimResponse.identifier", description="The identity of the claimresponse", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identity of the claimresponse</b><br>
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
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.insurer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="insurer", path="ClaimResponse.insurer", description="The organization who generated this resource", type="reference", target={Organization.class } )
  public static final String SP_INSURER = "insurer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>insurer</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
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
   * Description: <b>The subject of care.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ClaimResponse.patient", description="The subject of care.", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The subject of care.</b><br>
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
   * Search parameter: <b>payment-date</b>
   * <p>
   * Description: <b>The expected paymentDate</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClaimResponse.payment.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="payment-date", path="ClaimResponse.payment.date", description="The expected paymentDate", type="date" )
  public static final String SP_PAYMENT_DATE = "payment-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>payment-date</b>
   * <p>
   * Description: <b>The expected paymentDate</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClaimResponse.payment.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PAYMENT_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PAYMENT_DATE);

 /**
   * Search parameter: <b>request-provider</b>
   * <p>
   * Description: <b>The Provider of the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.requestProvider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request-provider", path="ClaimResponse.requestProvider", description="The Provider of the claim", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class, PractitionerRole.class } )
  public static final String SP_REQUEST_PROVIDER = "request-provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request-provider</b>
   * <p>
   * Description: <b>The Provider of the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClaimResponse.requestProvider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST_PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClaimResponse:request-provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST_PROVIDER = new ca.uhn.fhir.model.api.Include("ClaimResponse:request-provider").toLocked();

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
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the claim response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ClaimResponse.status", description="The status of the claim response", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the claim response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClaimResponse.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

