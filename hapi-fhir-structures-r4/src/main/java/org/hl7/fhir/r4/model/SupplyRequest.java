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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

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
 * A record of a request for a medication, substance or device used in the healthcare setting.
 */
@ResourceDef(name="SupplyRequest", profile="http://hl7.org/fhir/Profile/SupplyRequest")
public class SupplyRequest extends DomainResource {

    public enum SupplyRequestStatus {
        /**
         * The request has been created but is not yet complete or ready for action.
         */
        DRAFT, 
        /**
         * The request is ready to be acted upon.
         */
        ACTIVE, 
        /**
         * The authorization/request to act has been temporarily withdrawn but is expected to resume in the future.
         */
        SUSPENDED, 
        /**
         * The authorization/request to act has been terminated prior to the full completion of the intended actions.  No further activity should occur.
         */
        CANCELLED, 
        /**
         * Activity against the request has been sufficiently completed to the satisfaction of the requester.
         */
        COMPLETED, 
        /**
         * This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".).
         */
        ENTEREDINERROR, 
        /**
         * The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SupplyRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SupplyRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case CANCELLED: return "cancelled";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/supplyrequest-status";
            case ACTIVE: return "http://hl7.org/fhir/supplyrequest-status";
            case SUSPENDED: return "http://hl7.org/fhir/supplyrequest-status";
            case CANCELLED: return "http://hl7.org/fhir/supplyrequest-status";
            case COMPLETED: return "http://hl7.org/fhir/supplyrequest-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/supplyrequest-status";
            case UNKNOWN: return "http://hl7.org/fhir/supplyrequest-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The request has been created but is not yet complete or ready for action.";
            case ACTIVE: return "The request is ready to be acted upon.";
            case SUSPENDED: return "The authorization/request to act has been temporarily withdrawn but is expected to resume in the future.";
            case CANCELLED: return "The authorization/request to act has been terminated prior to the full completion of the intended actions.  No further activity should occur.";
            case COMPLETED: return "Activity against the request has been sufficiently completed to the satisfaction of the requester.";
            case ENTEREDINERROR: return "This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".).";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, it's just not known which one.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case CANCELLED: return "Cancelled";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class SupplyRequestStatusEnumFactory implements EnumFactory<SupplyRequestStatus> {
    public SupplyRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return SupplyRequestStatus.DRAFT;
        if ("active".equals(codeString))
          return SupplyRequestStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return SupplyRequestStatus.SUSPENDED;
        if ("cancelled".equals(codeString))
          return SupplyRequestStatus.CANCELLED;
        if ("completed".equals(codeString))
          return SupplyRequestStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return SupplyRequestStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return SupplyRequestStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown SupplyRequestStatus code '"+codeString+"'");
        }
        public Enumeration<SupplyRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SupplyRequestStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<SupplyRequestStatus>(this, SupplyRequestStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<SupplyRequestStatus>(this, SupplyRequestStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<SupplyRequestStatus>(this, SupplyRequestStatus.SUSPENDED);
        if ("cancelled".equals(codeString))
          return new Enumeration<SupplyRequestStatus>(this, SupplyRequestStatus.CANCELLED);
        if ("completed".equals(codeString))
          return new Enumeration<SupplyRequestStatus>(this, SupplyRequestStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<SupplyRequestStatus>(this, SupplyRequestStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<SupplyRequestStatus>(this, SupplyRequestStatus.UNKNOWN);
        throw new FHIRException("Unknown SupplyRequestStatus code '"+codeString+"'");
        }
    public String toCode(SupplyRequestStatus code) {
      if (code == SupplyRequestStatus.DRAFT)
        return "draft";
      if (code == SupplyRequestStatus.ACTIVE)
        return "active";
      if (code == SupplyRequestStatus.SUSPENDED)
        return "suspended";
      if (code == SupplyRequestStatus.CANCELLED)
        return "cancelled";
      if (code == SupplyRequestStatus.COMPLETED)
        return "completed";
      if (code == SupplyRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == SupplyRequestStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(SupplyRequestStatus code) {
      return code.getSystem();
      }
    }

    public enum RequestPriority {
        /**
         * The request has normal priority.
         */
        ROUTINE, 
        /**
         * The request should be actioned promptly - higher priority than routine.
         */
        URGENT, 
        /**
         * The request should be actioned as soon as possible - higher priority than urgent.
         */
        ASAP, 
        /**
         * The request should be actioned immediately - highest possible priority.  E.g. an emergency.
         */
        STAT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static RequestPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return ROUTINE;
        if ("urgent".equals(codeString))
          return URGENT;
        if ("asap".equals(codeString))
          return ASAP;
        if ("stat".equals(codeString))
          return STAT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown RequestPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ROUTINE: return "routine";
            case URGENT: return "urgent";
            case ASAP: return "asap";
            case STAT: return "stat";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ROUTINE: return "http://hl7.org/fhir/request-priority";
            case URGENT: return "http://hl7.org/fhir/request-priority";
            case ASAP: return "http://hl7.org/fhir/request-priority";
            case STAT: return "http://hl7.org/fhir/request-priority";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ROUTINE: return "The request has normal priority.";
            case URGENT: return "The request should be actioned promptly - higher priority than routine.";
            case ASAP: return "The request should be actioned as soon as possible - higher priority than urgent.";
            case STAT: return "The request should be actioned immediately - highest possible priority.  E.g. an emergency.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ROUTINE: return "Routine";
            case URGENT: return "Urgent";
            case ASAP: return "ASAP";
            case STAT: return "STAT";
            default: return "?";
          }
        }
    }

  public static class RequestPriorityEnumFactory implements EnumFactory<RequestPriority> {
    public RequestPriority fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return RequestPriority.ROUTINE;
        if ("urgent".equals(codeString))
          return RequestPriority.URGENT;
        if ("asap".equals(codeString))
          return RequestPriority.ASAP;
        if ("stat".equals(codeString))
          return RequestPriority.STAT;
        throw new IllegalArgumentException("Unknown RequestPriority code '"+codeString+"'");
        }
        public Enumeration<RequestPriority> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<RequestPriority>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("routine".equals(codeString))
          return new Enumeration<RequestPriority>(this, RequestPriority.ROUTINE);
        if ("urgent".equals(codeString))
          return new Enumeration<RequestPriority>(this, RequestPriority.URGENT);
        if ("asap".equals(codeString))
          return new Enumeration<RequestPriority>(this, RequestPriority.ASAP);
        if ("stat".equals(codeString))
          return new Enumeration<RequestPriority>(this, RequestPriority.STAT);
        throw new FHIRException("Unknown RequestPriority code '"+codeString+"'");
        }
    public String toCode(RequestPriority code) {
      if (code == RequestPriority.ROUTINE)
        return "routine";
      if (code == RequestPriority.URGENT)
        return "urgent";
      if (code == RequestPriority.ASAP)
        return "asap";
      if (code == RequestPriority.STAT)
        return "stat";
      return "?";
      }
    public String toSystem(RequestPriority code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class SupplyRequestParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code or string that identifies the device detail being asserted.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Item detail", formalDefinition="A code or string that identifies the device detail being asserted." )
        protected CodeableConcept code;

        /**
         * The value of the device detail.
         */
        @Child(name = "value", type = {CodeableConcept.class, Quantity.class, Range.class, BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value of detail", formalDefinition="The value of the device detail." )
        protected Type value;

        private static final long serialVersionUID = 884525025L;

    /**
     * Constructor
     */
      public SupplyRequestParameterComponent() {
        super();
      }

        /**
         * @return {@link #code} (A code or string that identifies the device detail being asserted.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyRequestParameterComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code or string that identifies the device detail being asserted.)
         */
        public SupplyRequestParameterComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #value} (The value of the device detail.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The value of the device detail.)
         */
        public CodeableConcept getValueCodeableConcept() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.value.getClass().getName()+" was encountered");
          return (CodeableConcept) this.value;
        }

        public boolean hasValueCodeableConcept() { 
          return this != null && this.value instanceof CodeableConcept;
        }

        /**
         * @return {@link #value} (The value of the device detail.)
         */
        public Quantity getValueQuantity() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        public boolean hasValueQuantity() { 
          return this != null && this.value instanceof Quantity;
        }

        /**
         * @return {@link #value} (The value of the device detail.)
         */
        public Range getValueRange() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Range) this.value;
        }

        public boolean hasValueRange() { 
          return this != null && this.value instanceof Range;
        }

        /**
         * @return {@link #value} (The value of the device detail.)
         */
        public BooleanType getValueBooleanType() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        public boolean hasValueBooleanType() { 
          return this != null && this.value instanceof BooleanType;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the device detail.)
         */
        public SupplyRequestParameterComponent setValue(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Quantity || value instanceof Range || value instanceof BooleanType))
            throw new Error("Not the right type for SupplyRequest.parameter.value[x]: "+value.fhirType());
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "A code or string that identifies the device detail being asserted.", 0, 1, code));
          children.add(new Property("value[x]", "CodeableConcept|Quantity|Range|boolean", "The value of the device detail.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A code or string that identifies the device detail being asserted.", 0, 1, code);
          case -1410166417: /*value[x]*/  return new Property("value[x]", "CodeableConcept|Quantity|Range|boolean", "The value of the device detail.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "CodeableConcept|Quantity|Range|boolean", "The value of the device detail.", 0, 1, value);
          case 924902896: /*valueCodeableConcept*/  return new Property("value[x]", "CodeableConcept|Quantity|Range|boolean", "The value of the device detail.", 0, 1, value);
          case -2029823716: /*valueQuantity*/  return new Property("value[x]", "CodeableConcept|Quantity|Range|boolean", "The value of the device detail.", 0, 1, value);
          case 2030761548: /*valueRange*/  return new Property("value[x]", "CodeableConcept|Quantity|Range|boolean", "The value of the device detail.", 0, 1, value);
          case 733421943: /*valueBoolean*/  return new Property("value[x]", "CodeableConcept|Quantity|Range|boolean", "The value of the device detail.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 111972721: /*value*/ return new String[] {"CodeableConcept", "Quantity", "Range", "boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueRange")) {
          this.value = new Range();
          return this.value;
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public SupplyRequestParameterComponent copy() {
        SupplyRequestParameterComponent dst = new SupplyRequestParameterComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SupplyRequestParameterComponent))
          return false;
        SupplyRequestParameterComponent o = (SupplyRequestParameterComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SupplyRequestParameterComponent))
          return false;
        SupplyRequestParameterComponent o = (SupplyRequestParameterComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, value);
      }

  public String fhirType() {
    return "SupplyRequest.parameter";

  }

  }

    /**
     * Unique identifier for this supply request.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier", formalDefinition="Unique identifier for this supply request." )
    protected Identifier identifier;

    /**
     * Status of the supply request.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | suspended +", formalDefinition="Status of the supply request." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supplyrequest-status")
    protected Enumeration<SupplyRequestStatus> status;

    /**
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The kind of supply (central, non-stock, etc.)", formalDefinition="Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supplyrequest-kind")
    protected CodeableConcept category;

    /**
     * Indicates how quickly this SupplyRequest should be addressed with respect to other requests.
     */
    @Child(name = "priority", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="routine | urgent | asap | stat", formalDefinition="Indicates how quickly this SupplyRequest should be addressed with respect to other requests." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-priority")
    protected Enumeration<RequestPriority> priority;

    /**
     * The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.
     */
    @Child(name = "item", type = {CodeableConcept.class, Medication.class, Substance.class, Device.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Medication, Substance, or Device requested to be supplied", formalDefinition="The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supply-item")
    protected Type item;

    /**
     * The amount that is being ordered of the indicated item.
     */
    @Child(name = "quantity", type = {Quantity.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The requested amount of the item indicated", formalDefinition="The amount that is being ordered of the indicated item." )
    protected Quantity quantity;

    /**
     * Specific parameters for the ordered item.  For example, the size of the indicated item.
     */
    @Child(name = "parameter", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Ordered item details", formalDefinition="Specific parameters for the ordered item.  For example, the size of the indicated item." )
    protected List<SupplyRequestParameterComponent> parameter;

    /**
     * When the request should be fulfilled.
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class, Timing.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the request should be fulfilled", formalDefinition="When the request should be fulfilled." )
    protected Type occurrence;

    /**
     * When the request was made.
     */
    @Child(name = "authoredOn", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the request was made", formalDefinition="When the request was made." )
    protected DateTimeType authoredOn;

    /**
     * The device, practitioner, etc. who initiated the request.
     */
    @Child(name = "requester", type = {Practitioner.class, PractitionerRole.class, Organization.class, Patient.class, RelatedPerson.class, Device.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Individual making the request", formalDefinition="The device, practitioner, etc. who initiated the request." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (The device, practitioner, etc. who initiated the request.)
     */
    protected Resource requesterTarget;

    /**
     * Who is intended to fulfill the request.
     */
    @Child(name = "supplier", type = {Organization.class, HealthcareService.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who is intended to fulfill the request", formalDefinition="Who is intended to fulfill the request." )
    protected List<Reference> supplier;
    /**
     * The actual objects that are the target of the reference (Who is intended to fulfill the request.)
     */
    protected List<Resource> supplierTarget;


    /**
     * The reason why the supply item was requested.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The reason why the supply item was requested", formalDefinition="The reason why the supply item was requested." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supplyrequest-reason")
    protected List<CodeableConcept> reasonCode;

    /**
     * The reason why the supply item was requested.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class, DiagnosticReport.class, DocumentReference.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The reason why the supply item was requested", formalDefinition="The reason why the supply item was requested." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (The reason why the supply item was requested.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * Where the supply is expected to come from.
     */
    @Child(name = "deliverFrom", type = {Organization.class, Location.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The origin of the supply", formalDefinition="Where the supply is expected to come from." )
    protected Reference deliverFrom;

    /**
     * The actual object that is the target of the reference (Where the supply is expected to come from.)
     */
    protected Resource deliverFromTarget;

    /**
     * Where the supply is destined to go.
     */
    @Child(name = "deliverTo", type = {Organization.class, Location.class, Patient.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The destination of the supply", formalDefinition="Where the supply is destined to go." )
    protected Reference deliverTo;

    /**
     * The actual object that is the target of the reference (Where the supply is destined to go.)
     */
    protected Resource deliverToTarget;

    private static final long serialVersionUID = -1922364955L;

  /**
   * Constructor
   */
    public SupplyRequest() {
      super();
    }

  /**
   * Constructor
   */
    public SupplyRequest(Type item, Quantity quantity) {
      super();
      this.item = item;
      this.quantity = quantity;
    }

    /**
     * @return {@link #identifier} (Unique identifier for this supply request.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Unique identifier for this supply request.)
     */
    public SupplyRequest setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #status} (Status of the supply request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<SupplyRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<SupplyRequestStatus>(new SupplyRequestStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Status of the supply request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public SupplyRequest setStatusElement(Enumeration<SupplyRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Status of the supply request.
     */
    public SupplyRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Status of the supply request.
     */
    public SupplyRequest setStatus(SupplyRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<SupplyRequestStatus>(new SupplyRequestStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #category} (Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.)
     */
    public SupplyRequest setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #priority} (Indicates how quickly this SupplyRequest should be addressed with respect to other requests.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Enumeration<RequestPriority> getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Enumeration<RequestPriority>(new RequestPriorityEnumFactory()); // bb
      return this.priority;
    }

    public boolean hasPriorityElement() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Indicates how quickly this SupplyRequest should be addressed with respect to other requests.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public SupplyRequest setPriorityElement(Enumeration<RequestPriority> value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return Indicates how quickly this SupplyRequest should be addressed with respect to other requests.
     */
    public RequestPriority getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value Indicates how quickly this SupplyRequest should be addressed with respect to other requests.
     */
    public SupplyRequest setPriority(RequestPriority value) { 
      if (value == null)
        this.priority = null;
      else {
        if (this.priority == null)
          this.priority = new Enumeration<RequestPriority>(new RequestPriorityEnumFactory());
        this.priority.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #item} (The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
     */
    public Type getItem() { 
      return this.item;
    }

    /**
     * @return {@link #item} (The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
     */
    public CodeableConcept getItemCodeableConcept() throws FHIRException { 
      if (this.item == null)
        return null;
      if (!(this.item instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.item.getClass().getName()+" was encountered");
      return (CodeableConcept) this.item;
    }

    public boolean hasItemCodeableConcept() { 
      return this != null && this.item instanceof CodeableConcept;
    }

    /**
     * @return {@link #item} (The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
     */
    public Reference getItemReference() throws FHIRException { 
      if (this.item == null)
        return null;
      if (!(this.item instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.item.getClass().getName()+" was encountered");
      return (Reference) this.item;
    }

    public boolean hasItemReference() { 
      return this != null && this.item instanceof Reference;
    }

    public boolean hasItem() { 
      return this.item != null && !this.item.isEmpty();
    }

    /**
     * @param value {@link #item} (The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
     */
    public SupplyRequest setItem(Type value) { 
      if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
        throw new Error("Not the right type for SupplyRequest.item[x]: "+value.fhirType());
      this.item = value;
      return this;
    }

    /**
     * @return {@link #quantity} (The amount that is being ordered of the indicated item.)
     */
    public Quantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new Quantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (The amount that is being ordered of the indicated item.)
     */
    public SupplyRequest setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #parameter} (Specific parameters for the ordered item.  For example, the size of the indicated item.)
     */
    public List<SupplyRequestParameterComponent> getParameter() { 
      if (this.parameter == null)
        this.parameter = new ArrayList<SupplyRequestParameterComponent>();
      return this.parameter;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SupplyRequest setParameter(List<SupplyRequestParameterComponent> theParameter) { 
      this.parameter = theParameter;
      return this;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (SupplyRequestParameterComponent item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SupplyRequestParameterComponent addParameter() { //3
      SupplyRequestParameterComponent t = new SupplyRequestParameterComponent();
      if (this.parameter == null)
        this.parameter = new ArrayList<SupplyRequestParameterComponent>();
      this.parameter.add(t);
      return t;
    }

    public SupplyRequest addParameter(SupplyRequestParameterComponent t) { //3
      if (t == null)
        return this;
      if (this.parameter == null)
        this.parameter = new ArrayList<SupplyRequestParameterComponent>();
      this.parameter.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #parameter}, creating it if it does not already exist
     */
    public SupplyRequestParameterComponent getParameterFirstRep() { 
      if (getParameter().isEmpty()) {
        addParameter();
      }
      return getParameter().get(0);
    }

    /**
     * @return {@link #occurrence} (When the request should be fulfilled.)
     */
    public Type getOccurrence() { 
      return this.occurrence;
    }

    /**
     * @return {@link #occurrence} (When the request should be fulfilled.)
     */
    public DateTimeType getOccurrenceDateTimeType() throws FHIRException { 
      if (this.occurrence == null)
        return null;
      if (!(this.occurrence instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (DateTimeType) this.occurrence;
    }

    public boolean hasOccurrenceDateTimeType() { 
      return this != null && this.occurrence instanceof DateTimeType;
    }

    /**
     * @return {@link #occurrence} (When the request should be fulfilled.)
     */
    public Period getOccurrencePeriod() throws FHIRException { 
      if (this.occurrence == null)
        return null;
      if (!(this.occurrence instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Period) this.occurrence;
    }

    public boolean hasOccurrencePeriod() { 
      return this != null && this.occurrence instanceof Period;
    }

    /**
     * @return {@link #occurrence} (When the request should be fulfilled.)
     */
    public Timing getOccurrenceTiming() throws FHIRException { 
      if (this.occurrence == null)
        return null;
      if (!(this.occurrence instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Timing) this.occurrence;
    }

    public boolean hasOccurrenceTiming() { 
      return this != null && this.occurrence instanceof Timing;
    }

    public boolean hasOccurrence() { 
      return this.occurrence != null && !this.occurrence.isEmpty();
    }

    /**
     * @param value {@link #occurrence} (When the request should be fulfilled.)
     */
    public SupplyRequest setOccurrence(Type value) { 
      if (value != null && !(value instanceof DateTimeType || value instanceof Period || value instanceof Timing))
        throw new Error("Not the right type for SupplyRequest.occurrence[x]: "+value.fhirType());
      this.occurrence = value;
      return this;
    }

    /**
     * @return {@link #authoredOn} (When the request was made.). This is the underlying object with id, value and extensions. The accessor "getAuthoredOn" gives direct access to the value
     */
    public DateTimeType getAuthoredOnElement() { 
      if (this.authoredOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.authoredOn");
        else if (Configuration.doAutoCreate())
          this.authoredOn = new DateTimeType(); // bb
      return this.authoredOn;
    }

    public boolean hasAuthoredOnElement() { 
      return this.authoredOn != null && !this.authoredOn.isEmpty();
    }

    public boolean hasAuthoredOn() { 
      return this.authoredOn != null && !this.authoredOn.isEmpty();
    }

    /**
     * @param value {@link #authoredOn} (When the request was made.). This is the underlying object with id, value and extensions. The accessor "getAuthoredOn" gives direct access to the value
     */
    public SupplyRequest setAuthoredOnElement(DateTimeType value) { 
      this.authoredOn = value;
      return this;
    }

    /**
     * @return When the request was made.
     */
    public Date getAuthoredOn() { 
      return this.authoredOn == null ? null : this.authoredOn.getValue();
    }

    /**
     * @param value When the request was made.
     */
    public SupplyRequest setAuthoredOn(Date value) { 
      if (value == null)
        this.authoredOn = null;
      else {
        if (this.authoredOn == null)
          this.authoredOn = new DateTimeType();
        this.authoredOn.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #requester} (The device, practitioner, etc. who initiated the request.)
     */
    public Reference getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new Reference(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (The device, practitioner, etc. who initiated the request.)
     */
    public SupplyRequest setRequester(Reference value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #requester} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who initiated the request.)
     */
    public Resource getRequesterTarget() { 
      return this.requesterTarget;
    }

    /**
     * @param value {@link #requester} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who initiated the request.)
     */
    public SupplyRequest setRequesterTarget(Resource value) { 
      this.requesterTarget = value;
      return this;
    }

    /**
     * @return {@link #supplier} (Who is intended to fulfill the request.)
     */
    public List<Reference> getSupplier() { 
      if (this.supplier == null)
        this.supplier = new ArrayList<Reference>();
      return this.supplier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SupplyRequest setSupplier(List<Reference> theSupplier) { 
      this.supplier = theSupplier;
      return this;
    }

    public boolean hasSupplier() { 
      if (this.supplier == null)
        return false;
      for (Reference item : this.supplier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSupplier() { //3
      Reference t = new Reference();
      if (this.supplier == null)
        this.supplier = new ArrayList<Reference>();
      this.supplier.add(t);
      return t;
    }

    public SupplyRequest addSupplier(Reference t) { //3
      if (t == null)
        return this;
      if (this.supplier == null)
        this.supplier = new ArrayList<Reference>();
      this.supplier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supplier}, creating it if it does not already exist
     */
    public Reference getSupplierFirstRep() { 
      if (getSupplier().isEmpty()) {
        addSupplier();
      }
      return getSupplier().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSupplierTarget() { 
      if (this.supplierTarget == null)
        this.supplierTarget = new ArrayList<Resource>();
      return this.supplierTarget;
    }

    /**
     * @return {@link #reasonCode} (The reason why the supply item was requested.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SupplyRequest setReasonCode(List<CodeableConcept> theReasonCode) { 
      this.reasonCode = theReasonCode;
      return this;
    }

    public boolean hasReasonCode() { 
      if (this.reasonCode == null)
        return false;
      for (CodeableConcept item : this.reasonCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return t;
    }

    public SupplyRequest addReasonCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonCode}, creating it if it does not already exist
     */
    public CodeableConcept getReasonCodeFirstRep() { 
      if (getReasonCode().isEmpty()) {
        addReasonCode();
      }
      return getReasonCode().get(0);
    }

    /**
     * @return {@link #reasonReference} (The reason why the supply item was requested.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SupplyRequest setReasonReference(List<Reference> theReasonReference) { 
      this.reasonReference = theReasonReference;
      return this;
    }

    public boolean hasReasonReference() { 
      if (this.reasonReference == null)
        return false;
      for (Reference item : this.reasonReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReasonReference() { //3
      Reference t = new Reference();
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return t;
    }

    public SupplyRequest addReasonReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonReference}, creating it if it does not already exist
     */
    public Reference getReasonReferenceFirstRep() { 
      if (getReasonReference().isEmpty()) {
        addReasonReference();
      }
      return getReasonReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getReasonReferenceTarget() { 
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Resource>();
      return this.reasonReferenceTarget;
    }

    /**
     * @return {@link #deliverFrom} (Where the supply is expected to come from.)
     */
    public Reference getDeliverFrom() { 
      if (this.deliverFrom == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.deliverFrom");
        else if (Configuration.doAutoCreate())
          this.deliverFrom = new Reference(); // cc
      return this.deliverFrom;
    }

    public boolean hasDeliverFrom() { 
      return this.deliverFrom != null && !this.deliverFrom.isEmpty();
    }

    /**
     * @param value {@link #deliverFrom} (Where the supply is expected to come from.)
     */
    public SupplyRequest setDeliverFrom(Reference value) { 
      this.deliverFrom = value;
      return this;
    }

    /**
     * @return {@link #deliverFrom} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where the supply is expected to come from.)
     */
    public Resource getDeliverFromTarget() { 
      return this.deliverFromTarget;
    }

    /**
     * @param value {@link #deliverFrom} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where the supply is expected to come from.)
     */
    public SupplyRequest setDeliverFromTarget(Resource value) { 
      this.deliverFromTarget = value;
      return this;
    }

    /**
     * @return {@link #deliverTo} (Where the supply is destined to go.)
     */
    public Reference getDeliverTo() { 
      if (this.deliverTo == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.deliverTo");
        else if (Configuration.doAutoCreate())
          this.deliverTo = new Reference(); // cc
      return this.deliverTo;
    }

    public boolean hasDeliverTo() { 
      return this.deliverTo != null && !this.deliverTo.isEmpty();
    }

    /**
     * @param value {@link #deliverTo} (Where the supply is destined to go.)
     */
    public SupplyRequest setDeliverTo(Reference value) { 
      this.deliverTo = value;
      return this;
    }

    /**
     * @return {@link #deliverTo} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where the supply is destined to go.)
     */
    public Resource getDeliverToTarget() { 
      return this.deliverToTarget;
    }

    /**
     * @param value {@link #deliverTo} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where the supply is destined to go.)
     */
    public SupplyRequest setDeliverToTarget(Resource value) { 
      this.deliverToTarget = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Unique identifier for this supply request.", 0, 1, identifier));
        children.add(new Property("status", "code", "Status of the supply request.", 0, 1, status));
        children.add(new Property("category", "CodeableConcept", "Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.", 0, 1, category));
        children.add(new Property("priority", "code", "Indicates how quickly this SupplyRequest should be addressed with respect to other requests.", 0, 1, priority));
        children.add(new Property("item[x]", "CodeableConcept|Reference(Medication|Substance|Device)", "The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.", 0, 1, item));
        children.add(new Property("quantity", "Quantity", "The amount that is being ordered of the indicated item.", 0, 1, quantity));
        children.add(new Property("parameter", "", "Specific parameters for the ordered item.  For example, the size of the indicated item.", 0, java.lang.Integer.MAX_VALUE, parameter));
        children.add(new Property("occurrence[x]", "dateTime|Period|Timing", "When the request should be fulfilled.", 0, 1, occurrence));
        children.add(new Property("authoredOn", "dateTime", "When the request was made.", 0, 1, authoredOn));
        children.add(new Property("requester", "Reference(Practitioner|PractitionerRole|Organization|Patient|RelatedPerson|Device)", "The device, practitioner, etc. who initiated the request.", 0, 1, requester));
        children.add(new Property("supplier", "Reference(Organization|HealthcareService)", "Who is intended to fulfill the request.", 0, java.lang.Integer.MAX_VALUE, supplier));
        children.add(new Property("reasonCode", "CodeableConcept", "The reason why the supply item was requested.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        children.add(new Property("reasonReference", "Reference(Condition|Observation|DiagnosticReport|DocumentReference)", "The reason why the supply item was requested.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        children.add(new Property("deliverFrom", "Reference(Organization|Location)", "Where the supply is expected to come from.", 0, 1, deliverFrom));
        children.add(new Property("deliverTo", "Reference(Organization|Location|Patient)", "Where the supply is destined to go.", 0, 1, deliverTo));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier for this supply request.", 0, 1, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "Status of the supply request.", 0, 1, status);
        case 50511102: /*category*/  return new Property("category", "CodeableConcept", "Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.", 0, 1, category);
        case -1165461084: /*priority*/  return new Property("priority", "code", "Indicates how quickly this SupplyRequest should be addressed with respect to other requests.", 0, 1, priority);
        case 2116201613: /*item[x]*/  return new Property("item[x]", "CodeableConcept|Reference(Medication|Substance|Device)", "The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.", 0, 1, item);
        case 3242771: /*item*/  return new Property("item[x]", "CodeableConcept|Reference(Medication|Substance|Device)", "The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.", 0, 1, item);
        case 106644494: /*itemCodeableConcept*/  return new Property("item[x]", "CodeableConcept|Reference(Medication|Substance|Device)", "The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.", 0, 1, item);
        case 1376364920: /*itemReference*/  return new Property("item[x]", "CodeableConcept|Reference(Medication|Substance|Device)", "The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.", 0, 1, item);
        case -1285004149: /*quantity*/  return new Property("quantity", "Quantity", "The amount that is being ordered of the indicated item.", 0, 1, quantity);
        case 1954460585: /*parameter*/  return new Property("parameter", "", "Specific parameters for the ordered item.  For example, the size of the indicated item.", 0, java.lang.Integer.MAX_VALUE, parameter);
        case -2022646513: /*occurrence[x]*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When the request should be fulfilled.", 0, 1, occurrence);
        case 1687874001: /*occurrence*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When the request should be fulfilled.", 0, 1, occurrence);
        case -298443636: /*occurrenceDateTime*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When the request should be fulfilled.", 0, 1, occurrence);
        case 1397156594: /*occurrencePeriod*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When the request should be fulfilled.", 0, 1, occurrence);
        case 1515218299: /*occurrenceTiming*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When the request should be fulfilled.", 0, 1, occurrence);
        case -1500852503: /*authoredOn*/  return new Property("authoredOn", "dateTime", "When the request was made.", 0, 1, authoredOn);
        case 693933948: /*requester*/  return new Property("requester", "Reference(Practitioner|PractitionerRole|Organization|Patient|RelatedPerson|Device)", "The device, practitioner, etc. who initiated the request.", 0, 1, requester);
        case -1663305268: /*supplier*/  return new Property("supplier", "Reference(Organization|HealthcareService)", "Who is intended to fulfill the request.", 0, java.lang.Integer.MAX_VALUE, supplier);
        case 722137681: /*reasonCode*/  return new Property("reasonCode", "CodeableConcept", "The reason why the supply item was requested.", 0, java.lang.Integer.MAX_VALUE, reasonCode);
        case -1146218137: /*reasonReference*/  return new Property("reasonReference", "Reference(Condition|Observation|DiagnosticReport|DocumentReference)", "The reason why the supply item was requested.", 0, java.lang.Integer.MAX_VALUE, reasonReference);
        case -949323153: /*deliverFrom*/  return new Property("deliverFrom", "Reference(Organization|Location)", "Where the supply is expected to come from.", 0, 1, deliverFrom);
        case -242327936: /*deliverTo*/  return new Property("deliverTo", "Reference(Organization|Location|Patient)", "Where the supply is destined to go.", 0, 1, deliverTo);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<SupplyRequestStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Enumeration<RequestPriority>
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Type
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case 1954460585: /*parameter*/ return this.parameter == null ? new Base[0] : this.parameter.toArray(new Base[this.parameter.size()]); // SupplyRequestParameterComponent
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case -1500852503: /*authoredOn*/ return this.authoredOn == null ? new Base[0] : new Base[] {this.authoredOn}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // Reference
        case -1663305268: /*supplier*/ return this.supplier == null ? new Base[0] : this.supplier.toArray(new Base[this.supplier.size()]); // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case -949323153: /*deliverFrom*/ return this.deliverFrom == null ? new Base[0] : new Base[] {this.deliverFrom}; // Reference
        case -242327936: /*deliverTo*/ return this.deliverTo == null ? new Base[0] : new Base[] {this.deliverTo}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -892481550: // status
          value = new SupplyRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<SupplyRequestStatus>
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1165461084: // priority
          value = new RequestPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<RequestPriority>
          return value;
        case 3242771: // item
          this.item = castToType(value); // Type
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case 1954460585: // parameter
          this.getParameter().add((SupplyRequestParameterComponent) value); // SupplyRequestParameterComponent
          return value;
        case 1687874001: // occurrence
          this.occurrence = castToType(value); // Type
          return value;
        case -1500852503: // authoredOn
          this.authoredOn = castToDateTime(value); // DateTimeType
          return value;
        case 693933948: // requester
          this.requester = castToReference(value); // Reference
          return value;
        case -1663305268: // supplier
          this.getSupplier().add(castToReference(value)); // Reference
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case -949323153: // deliverFrom
          this.deliverFrom = castToReference(value); // Reference
          return value;
        case -242327936: // deliverTo
          this.deliverTo = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("status")) {
          value = new SupplyRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<SupplyRequestStatus>
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("priority")) {
          value = new RequestPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<RequestPriority>
        } else if (name.equals("item[x]")) {
          this.item = castToType(value); // Type
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("parameter")) {
          this.getParameter().add((SupplyRequestParameterComponent) value);
        } else if (name.equals("occurrence[x]")) {
          this.occurrence = castToType(value); // Type
        } else if (name.equals("authoredOn")) {
          this.authoredOn = castToDateTime(value); // DateTimeType
        } else if (name.equals("requester")) {
          this.requester = castToReference(value); // Reference
        } else if (name.equals("supplier")) {
          this.getSupplier().add(castToReference(value));
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("deliverFrom")) {
          this.deliverFrom = castToReference(value); // Reference
        } else if (name.equals("deliverTo")) {
          this.deliverTo = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -892481550:  return getStatusElement();
        case 50511102:  return getCategory(); 
        case -1165461084:  return getPriorityElement();
        case 2116201613:  return getItem(); 
        case 3242771:  return getItem(); 
        case -1285004149:  return getQuantity(); 
        case 1954460585:  return addParameter(); 
        case -2022646513:  return getOccurrence(); 
        case 1687874001:  return getOccurrence(); 
        case -1500852503:  return getAuthoredOnElement();
        case 693933948:  return getRequester(); 
        case -1663305268:  return addSupplier(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case -949323153:  return getDeliverFrom(); 
        case -242327936:  return getDeliverTo(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case -1165461084: /*priority*/ return new String[] {"code"};
        case 3242771: /*item*/ return new String[] {"CodeableConcept", "Reference"};
        case -1285004149: /*quantity*/ return new String[] {"Quantity"};
        case 1954460585: /*parameter*/ return new String[] {};
        case 1687874001: /*occurrence*/ return new String[] {"dateTime", "Period", "Timing"};
        case -1500852503: /*authoredOn*/ return new String[] {"dateTime"};
        case 693933948: /*requester*/ return new String[] {"Reference"};
        case -1663305268: /*supplier*/ return new String[] {"Reference"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case -949323153: /*deliverFrom*/ return new String[] {"Reference"};
        case -242327936: /*deliverTo*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type SupplyRequest.status");
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type SupplyRequest.priority");
        }
        else if (name.equals("itemCodeableConcept")) {
          this.item = new CodeableConcept();
          return this.item;
        }
        else if (name.equals("itemReference")) {
          this.item = new Reference();
          return this.item;
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("parameter")) {
          return addParameter();
        }
        else if (name.equals("occurrenceDateTime")) {
          this.occurrence = new DateTimeType();
          return this.occurrence;
        }
        else if (name.equals("occurrencePeriod")) {
          this.occurrence = new Period();
          return this.occurrence;
        }
        else if (name.equals("occurrenceTiming")) {
          this.occurrence = new Timing();
          return this.occurrence;
        }
        else if (name.equals("authoredOn")) {
          throw new FHIRException("Cannot call addChild on a primitive type SupplyRequest.authoredOn");
        }
        else if (name.equals("requester")) {
          this.requester = new Reference();
          return this.requester;
        }
        else if (name.equals("supplier")) {
          return addSupplier();
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("deliverFrom")) {
          this.deliverFrom = new Reference();
          return this.deliverFrom;
        }
        else if (name.equals("deliverTo")) {
          this.deliverTo = new Reference();
          return this.deliverTo;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SupplyRequest";

  }

      public SupplyRequest copy() {
        SupplyRequest dst = new SupplyRequest();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.category = category == null ? null : category.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.item = item == null ? null : item.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<SupplyRequestParameterComponent>();
          for (SupplyRequestParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        dst.authoredOn = authoredOn == null ? null : authoredOn.copy();
        dst.requester = requester == null ? null : requester.copy();
        if (supplier != null) {
          dst.supplier = new ArrayList<Reference>();
          for (Reference i : supplier)
            dst.supplier.add(i.copy());
        };
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        if (reasonReference != null) {
          dst.reasonReference = new ArrayList<Reference>();
          for (Reference i : reasonReference)
            dst.reasonReference.add(i.copy());
        };
        dst.deliverFrom = deliverFrom == null ? null : deliverFrom.copy();
        dst.deliverTo = deliverTo == null ? null : deliverTo.copy();
        return dst;
      }

      protected SupplyRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SupplyRequest))
          return false;
        SupplyRequest o = (SupplyRequest) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(priority, o.priority, true) && compareDeep(item, o.item, true) && compareDeep(quantity, o.quantity, true)
           && compareDeep(parameter, o.parameter, true) && compareDeep(occurrence, o.occurrence, true) && compareDeep(authoredOn, o.authoredOn, true)
           && compareDeep(requester, o.requester, true) && compareDeep(supplier, o.supplier, true) && compareDeep(reasonCode, o.reasonCode, true)
           && compareDeep(reasonReference, o.reasonReference, true) && compareDeep(deliverFrom, o.deliverFrom, true)
           && compareDeep(deliverTo, o.deliverTo, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SupplyRequest))
          return false;
        SupplyRequest o = (SupplyRequest) other_;
        return compareValues(status, o.status, true) && compareValues(priority, o.priority, true) && compareValues(authoredOn, o.authoredOn, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, category
          , priority, item, quantity, parameter, occurrence, authoredOn, requester, supplier
          , reasonCode, reasonReference, deliverFrom, deliverTo);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SupplyRequest;
   }

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>Individual making the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyRequest.requester</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="SupplyRequest.requester", description="Individual making the request", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>Individual making the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyRequest.requester</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>SupplyRequest:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("SupplyRequest:requester").toLocked();

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When the request was made</b><br>
   * Type: <b>date</b><br>
   * Path: <b>SupplyRequest.authoredOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="SupplyRequest.authoredOn", description="When the request was made", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When the request was made</b><br>
   * Type: <b>date</b><br>
   * Path: <b>SupplyRequest.authoredOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="SupplyRequest.identifier", description="Unique identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>supplier</b>
   * <p>
   * Description: <b>Who is intended to fulfill the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyRequest.supplier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="supplier", path="SupplyRequest.supplier", description="Who is intended to fulfill the request", type="reference", target={HealthcareService.class, Organization.class } )
  public static final String SP_SUPPLIER = "supplier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>supplier</b>
   * <p>
   * Description: <b>Who is intended to fulfill the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyRequest.supplier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUPPLIER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUPPLIER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>SupplyRequest:supplier</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUPPLIER = new ca.uhn.fhir.model.api.Include("SupplyRequest:supplier").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>The kind of supply (central, non-stock, etc.)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyRequest.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="SupplyRequest.category", description="The kind of supply (central, non-stock, etc.)", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>The kind of supply (central, non-stock, etc.)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyRequest.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | active | suspended +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyRequest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="SupplyRequest.status", description="draft | active | suspended +", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | active | suspended +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyRequest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

