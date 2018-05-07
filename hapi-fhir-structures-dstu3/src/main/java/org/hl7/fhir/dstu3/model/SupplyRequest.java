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
         * The request has been created but is not yet complete or ready for action
         */
        DRAFT, 
        /**
         * The request is ready to be acted upon
         */
        ACTIVE, 
        /**
         * The authorization/request to act has been temporarily withdrawn but is expected to resume in the future
         */
        SUSPENDED, 
        /**
         * The authorization/request to act has been terminated prior to the full completion of the intended actions.  No further activity should occur.
         */
        CANCELLED, 
        /**
         * Activity against the request has been sufficiently completed to the satisfaction of the requester
         */
        COMPLETED, 
        /**
         * This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".)
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
            case DRAFT: return "The request has been created but is not yet complete or ready for action";
            case ACTIVE: return "The request is ready to be acted upon";
            case SUSPENDED: return "The authorization/request to act has been temporarily withdrawn but is expected to resume in the future";
            case CANCELLED: return "The authorization/request to act has been terminated prior to the full completion of the intended actions.  No further activity should occur.";
            case COMPLETED: return "Activity against the request has been sufficiently completed to the satisfaction of the requester";
            case ENTEREDINERROR: return "This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".)";
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
         * The request has normal priority
         */
        ROUTINE, 
        /**
         * The request should be actioned promptly - higher priority than routine
         */
        URGENT, 
        /**
         * The request should be actioned as soon as possible - higher priority than urgent
         */
        ASAP, 
        /**
         * The request should be actioned immediately - highest possible priority.  E.g. an emergency
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
            case ROUTINE: return "The request has normal priority";
            case URGENT: return "The request should be actioned promptly - higher priority than routine";
            case ASAP: return "The request should be actioned as soon as possible - higher priority than urgent";
            case STAT: return "The request should be actioned immediately - highest possible priority.  E.g. an emergency";
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
    public static class SupplyRequestOrderedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The amount that is being ordered of the indicated item.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The requested amount of the item indicated", formalDefinition="The amount that is being ordered of the indicated item." )
        protected Quantity quantity;

        /**
         * The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.
         */
        @Child(name = "item", type = {CodeableConcept.class, Medication.class, Substance.class, Device.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Medication, Substance, or Device requested to be supplied", formalDefinition="The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supply-item")
        protected Type item;

        private static final long serialVersionUID = 1628109307L;

    /**
     * Constructor
     */
      public SupplyRequestOrderedItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SupplyRequestOrderedItemComponent(Quantity quantity) {
        super();
        this.quantity = quantity;
      }

        /**
         * @return {@link #quantity} (The amount that is being ordered of the indicated item.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyRequestOrderedItemComponent.quantity");
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
        public SupplyRequestOrderedItemComponent setQuantity(Quantity value) { 
          this.quantity = value;
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
          if (!(this.item instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.item.getClass().getName()+" was encountered");
          return (CodeableConcept) this.item;
        }

        public boolean hasItemCodeableConcept() { 
          return this.item instanceof CodeableConcept;
        }

        /**
         * @return {@link #item} (The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
         */
        public Reference getItemReference() throws FHIRException { 
          if (!(this.item instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.item.getClass().getName()+" was encountered");
          return (Reference) this.item;
        }

        public boolean hasItemReference() { 
          return this.item instanceof Reference;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
         */
        public SupplyRequestOrderedItemComponent setItem(Type value) { 
          this.item = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("quantity", "Quantity", "The amount that is being ordered of the indicated item.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("item[x]", "CodeableConcept|Reference(Medication|Substance|Device)", "The item that is requested to be supplied. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.", 0, java.lang.Integer.MAX_VALUE, item));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case 3242771: // item
          this.item = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("item[x]")) {
          this.item = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1285004149:  return getQuantity(); 
        case 2116201613:  return getItem(); 
        case 3242771:  return getItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1285004149: /*quantity*/ return new String[] {"Quantity"};
        case 3242771: /*item*/ return new String[] {"CodeableConcept", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("itemCodeableConcept")) {
          this.item = new CodeableConcept();
          return this.item;
        }
        else if (name.equals("itemReference")) {
          this.item = new Reference();
          return this.item;
        }
        else
          return super.addChild(name);
      }

      public SupplyRequestOrderedItemComponent copy() {
        SupplyRequestOrderedItemComponent dst = new SupplyRequestOrderedItemComponent();
        copyValues(dst);
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.item = item == null ? null : item.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SupplyRequestOrderedItemComponent))
          return false;
        SupplyRequestOrderedItemComponent o = (SupplyRequestOrderedItemComponent) other;
        return compareDeep(quantity, o.quantity, true) && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupplyRequestOrderedItemComponent))
          return false;
        SupplyRequestOrderedItemComponent o = (SupplyRequestOrderedItemComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(quantity, item);
      }

  public String fhirType() {
    return "SupplyRequest.orderedItem";

  }

  }

    @Block()
    public static class SupplyRequestRequesterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The device, practitioner, etc. who initiated the request.
         */
        @Child(name = "agent", type = {Practitioner.class, Organization.class, Patient.class, RelatedPerson.class, Device.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Individual making the request", formalDefinition="The device, practitioner, etc. who initiated the request." )
        protected Reference agent;

        /**
         * The actual object that is the target of the reference (The device, practitioner, etc. who initiated the request.)
         */
        protected Resource agentTarget;

        /**
         * The organization the device or practitioner was acting on behalf of.
         */
        @Child(name = "onBehalfOf", type = {Organization.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Organization agent is acting for", formalDefinition="The organization the device or practitioner was acting on behalf of." )
        protected Reference onBehalfOf;

        /**
         * The actual object that is the target of the reference (The organization the device or practitioner was acting on behalf of.)
         */
        protected Organization onBehalfOfTarget;

        private static final long serialVersionUID = -71453027L;

    /**
     * Constructor
     */
      public SupplyRequestRequesterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SupplyRequestRequesterComponent(Reference agent) {
        super();
        this.agent = agent;
      }

        /**
         * @return {@link #agent} (The device, practitioner, etc. who initiated the request.)
         */
        public Reference getAgent() { 
          if (this.agent == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyRequestRequesterComponent.agent");
            else if (Configuration.doAutoCreate())
              this.agent = new Reference(); // cc
          return this.agent;
        }

        public boolean hasAgent() { 
          return this.agent != null && !this.agent.isEmpty();
        }

        /**
         * @param value {@link #agent} (The device, practitioner, etc. who initiated the request.)
         */
        public SupplyRequestRequesterComponent setAgent(Reference value) { 
          this.agent = value;
          return this;
        }

        /**
         * @return {@link #agent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who initiated the request.)
         */
        public Resource getAgentTarget() { 
          return this.agentTarget;
        }

        /**
         * @param value {@link #agent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who initiated the request.)
         */
        public SupplyRequestRequesterComponent setAgentTarget(Resource value) { 
          this.agentTarget = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} (The organization the device or practitioner was acting on behalf of.)
         */
        public Reference getOnBehalfOf() { 
          if (this.onBehalfOf == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyRequestRequesterComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOf = new Reference(); // cc
          return this.onBehalfOf;
        }

        public boolean hasOnBehalfOf() { 
          return this.onBehalfOf != null && !this.onBehalfOf.isEmpty();
        }

        /**
         * @param value {@link #onBehalfOf} (The organization the device or practitioner was acting on behalf of.)
         */
        public SupplyRequestRequesterComponent setOnBehalfOf(Reference value) { 
          this.onBehalfOf = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public Organization getOnBehalfOfTarget() { 
          if (this.onBehalfOfTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyRequestRequesterComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOfTarget = new Organization(); // aa
          return this.onBehalfOfTarget;
        }

        /**
         * @param value {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public SupplyRequestRequesterComponent setOnBehalfOfTarget(Organization value) { 
          this.onBehalfOfTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("agent", "Reference(Practitioner|Organization|Patient|RelatedPerson|Device)", "The device, practitioner, etc. who initiated the request.", 0, java.lang.Integer.MAX_VALUE, agent));
          childrenList.add(new Property("onBehalfOf", "Reference(Organization)", "The organization the device or practitioner was acting on behalf of.", 0, java.lang.Integer.MAX_VALUE, onBehalfOf));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : new Base[] {this.agent}; // Reference
        case -14402964: /*onBehalfOf*/ return this.onBehalfOf == null ? new Base[0] : new Base[] {this.onBehalfOf}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 92750597: // agent
          this.agent = castToReference(value); // Reference
          return value;
        case -14402964: // onBehalfOf
          this.onBehalfOf = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("agent")) {
          this.agent = castToReference(value); // Reference
        } else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92750597:  return getAgent(); 
        case -14402964:  return getOnBehalfOf(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92750597: /*agent*/ return new String[] {"Reference"};
        case -14402964: /*onBehalfOf*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("agent")) {
          this.agent = new Reference();
          return this.agent;
        }
        else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = new Reference();
          return this.onBehalfOf;
        }
        else
          return super.addChild(name);
      }

      public SupplyRequestRequesterComponent copy() {
        SupplyRequestRequesterComponent dst = new SupplyRequestRequesterComponent();
        copyValues(dst);
        dst.agent = agent == null ? null : agent.copy();
        dst.onBehalfOf = onBehalfOf == null ? null : onBehalfOf.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SupplyRequestRequesterComponent))
          return false;
        SupplyRequestRequesterComponent o = (SupplyRequestRequesterComponent) other;
        return compareDeep(agent, o.agent, true) && compareDeep(onBehalfOf, o.onBehalfOf, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupplyRequestRequesterComponent))
          return false;
        SupplyRequestRequesterComponent o = (SupplyRequestRequesterComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(agent, onBehalfOf);
      }

  public String fhirType() {
    return "SupplyRequest.requester";

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
     * The item being requested.
     */
    @Child(name = "orderedItem", type = {}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The item being requested", formalDefinition="The item being requested." )
    protected SupplyRequestOrderedItemComponent orderedItem;

    /**
     * When the request should be fulfilled.
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class, Timing.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the request should be fulfilled", formalDefinition="When the request should be fulfilled." )
    protected Type occurrence;

    /**
     * When the request was made.
     */
    @Child(name = "authoredOn", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the request was made", formalDefinition="When the request was made." )
    protected DateTimeType authoredOn;

    /**
     * The individual who initiated the request and has responsibility for its activation.
     */
    @Child(name = "requester", type = {}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/what is requesting service", formalDefinition="The individual who initiated the request and has responsibility for its activation." )
    protected SupplyRequestRequesterComponent requester;

    /**
     * Who is intended to fulfill the request.
     */
    @Child(name = "supplier", type = {Organization.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who is intended to fulfill the request", formalDefinition="Who is intended to fulfill the request." )
    protected List<Reference> supplier;
    /**
     * The actual objects that are the target of the reference (Who is intended to fulfill the request.)
     */
    protected List<Organization> supplierTarget;


    /**
     * Why the supply item was requested.
     */
    @Child(name = "reason", type = {CodeableConcept.class, Reference.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why the supply item was requested", formalDefinition="Why the supply item was requested." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supplyrequest-reason")
    protected Type reason;

    /**
     * Where the supply is expected to come from.
     */
    @Child(name = "deliverFrom", type = {Organization.class, Location.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The origin of the supply", formalDefinition="Where the supply is expected to come from." )
    protected Reference deliverFrom;

    /**
     * The actual object that is the target of the reference (Where the supply is expected to come from.)
     */
    protected Resource deliverFromTarget;

    /**
     * Where the supply is destined to go.
     */
    @Child(name = "deliverTo", type = {Organization.class, Location.class, Patient.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The destination of the supply", formalDefinition="Where the supply is destined to go." )
    protected Reference deliverTo;

    /**
     * The actual object that is the target of the reference (Where the supply is destined to go.)
     */
    protected Resource deliverToTarget;

    private static final long serialVersionUID = 43957782L;

  /**
   * Constructor
   */
    public SupplyRequest() {
      super();
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
     * @return {@link #orderedItem} (The item being requested.)
     */
    public SupplyRequestOrderedItemComponent getOrderedItem() { 
      if (this.orderedItem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.orderedItem");
        else if (Configuration.doAutoCreate())
          this.orderedItem = new SupplyRequestOrderedItemComponent(); // cc
      return this.orderedItem;
    }

    public boolean hasOrderedItem() { 
      return this.orderedItem != null && !this.orderedItem.isEmpty();
    }

    /**
     * @param value {@link #orderedItem} (The item being requested.)
     */
    public SupplyRequest setOrderedItem(SupplyRequestOrderedItemComponent value) { 
      this.orderedItem = value;
      return this;
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
      if (!(this.occurrence instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (DateTimeType) this.occurrence;
    }

    public boolean hasOccurrenceDateTimeType() { 
      return this.occurrence instanceof DateTimeType;
    }

    /**
     * @return {@link #occurrence} (When the request should be fulfilled.)
     */
    public Period getOccurrencePeriod() throws FHIRException { 
      if (!(this.occurrence instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Period) this.occurrence;
    }

    public boolean hasOccurrencePeriod() { 
      return this.occurrence instanceof Period;
    }

    /**
     * @return {@link #occurrence} (When the request should be fulfilled.)
     */
    public Timing getOccurrenceTiming() throws FHIRException { 
      if (!(this.occurrence instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Timing) this.occurrence;
    }

    public boolean hasOccurrenceTiming() { 
      return this.occurrence instanceof Timing;
    }

    public boolean hasOccurrence() { 
      return this.occurrence != null && !this.occurrence.isEmpty();
    }

    /**
     * @param value {@link #occurrence} (When the request should be fulfilled.)
     */
    public SupplyRequest setOccurrence(Type value) { 
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
     * @return {@link #requester} (The individual who initiated the request and has responsibility for its activation.)
     */
    public SupplyRequestRequesterComponent getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new SupplyRequestRequesterComponent(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (The individual who initiated the request and has responsibility for its activation.)
     */
    public SupplyRequest setRequester(SupplyRequestRequesterComponent value) { 
      this.requester = value;
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
    public List<Organization> getSupplierTarget() { 
      if (this.supplierTarget == null)
        this.supplierTarget = new ArrayList<Organization>();
      return this.supplierTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Organization addSupplierTarget() { 
      Organization r = new Organization();
      if (this.supplierTarget == null)
        this.supplierTarget = new ArrayList<Organization>();
      this.supplierTarget.add(r);
      return r;
    }

    /**
     * @return {@link #reason} (Why the supply item was requested.)
     */
    public Type getReason() { 
      return this.reason;
    }

    /**
     * @return {@link #reason} (Why the supply item was requested.)
     */
    public CodeableConcept getReasonCodeableConcept() throws FHIRException { 
      if (!(this.reason instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (CodeableConcept) this.reason;
    }

    public boolean hasReasonCodeableConcept() { 
      return this.reason instanceof CodeableConcept;
    }

    /**
     * @return {@link #reason} (Why the supply item was requested.)
     */
    public Reference getReasonReference() throws FHIRException { 
      if (!(this.reason instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (Reference) this.reason;
    }

    public boolean hasReasonReference() { 
      return this.reason instanceof Reference;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (Why the supply item was requested.)
     */
    public SupplyRequest setReason(Type value) { 
      this.reason = value;
      return this;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique identifier for this supply request.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Status of the supply request.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("priority", "code", "Indicates how quickly this SupplyRequest should be addressed with respect to other requests.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("orderedItem", "", "The item being requested.", 0, java.lang.Integer.MAX_VALUE, orderedItem));
        childrenList.add(new Property("occurrence[x]", "dateTime|Period|Timing", "When the request should be fulfilled.", 0, java.lang.Integer.MAX_VALUE, occurrence));
        childrenList.add(new Property("authoredOn", "dateTime", "When the request was made.", 0, java.lang.Integer.MAX_VALUE, authoredOn));
        childrenList.add(new Property("requester", "", "The individual who initiated the request and has responsibility for its activation.", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("supplier", "Reference(Organization)", "Who is intended to fulfill the request.", 0, java.lang.Integer.MAX_VALUE, supplier));
        childrenList.add(new Property("reason[x]", "CodeableConcept|Reference(Any)", "Why the supply item was requested.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("deliverFrom", "Reference(Organization|Location)", "Where the supply is expected to come from.", 0, java.lang.Integer.MAX_VALUE, deliverFrom));
        childrenList.add(new Property("deliverTo", "Reference(Organization|Location|Patient)", "Where the supply is destined to go.", 0, java.lang.Integer.MAX_VALUE, deliverTo));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<SupplyRequestStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Enumeration<RequestPriority>
        case 2129914144: /*orderedItem*/ return this.orderedItem == null ? new Base[0] : new Base[] {this.orderedItem}; // SupplyRequestOrderedItemComponent
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case -1500852503: /*authoredOn*/ return this.authoredOn == null ? new Base[0] : new Base[] {this.authoredOn}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // SupplyRequestRequesterComponent
        case -1663305268: /*supplier*/ return this.supplier == null ? new Base[0] : this.supplier.toArray(new Base[this.supplier.size()]); // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // Type
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
        case 2129914144: // orderedItem
          this.orderedItem = (SupplyRequestOrderedItemComponent) value; // SupplyRequestOrderedItemComponent
          return value;
        case 1687874001: // occurrence
          this.occurrence = castToType(value); // Type
          return value;
        case -1500852503: // authoredOn
          this.authoredOn = castToDateTime(value); // DateTimeType
          return value;
        case 693933948: // requester
          this.requester = (SupplyRequestRequesterComponent) value; // SupplyRequestRequesterComponent
          return value;
        case -1663305268: // supplier
          this.getSupplier().add(castToReference(value)); // Reference
          return value;
        case -934964668: // reason
          this.reason = castToType(value); // Type
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
        } else if (name.equals("orderedItem")) {
          this.orderedItem = (SupplyRequestOrderedItemComponent) value; // SupplyRequestOrderedItemComponent
        } else if (name.equals("occurrence[x]")) {
          this.occurrence = castToType(value); // Type
        } else if (name.equals("authoredOn")) {
          this.authoredOn = castToDateTime(value); // DateTimeType
        } else if (name.equals("requester")) {
          this.requester = (SupplyRequestRequesterComponent) value; // SupplyRequestRequesterComponent
        } else if (name.equals("supplier")) {
          this.getSupplier().add(castToReference(value));
        } else if (name.equals("reason[x]")) {
          this.reason = castToType(value); // Type
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
        case 2129914144:  return getOrderedItem(); 
        case -2022646513:  return getOccurrence(); 
        case 1687874001:  return getOccurrence(); 
        case -1500852503:  return getAuthoredOnElement();
        case 693933948:  return getRequester(); 
        case -1663305268:  return addSupplier(); 
        case -669418564:  return getReason(); 
        case -934964668:  return getReason(); 
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
        case 2129914144: /*orderedItem*/ return new String[] {};
        case 1687874001: /*occurrence*/ return new String[] {"dateTime", "Period", "Timing"};
        case -1500852503: /*authoredOn*/ return new String[] {"dateTime"};
        case 693933948: /*requester*/ return new String[] {};
        case -1663305268: /*supplier*/ return new String[] {"Reference"};
        case -934964668: /*reason*/ return new String[] {"CodeableConcept", "Reference"};
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
        else if (name.equals("orderedItem")) {
          this.orderedItem = new SupplyRequestOrderedItemComponent();
          return this.orderedItem;
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
          this.requester = new SupplyRequestRequesterComponent();
          return this.requester;
        }
        else if (name.equals("supplier")) {
          return addSupplier();
        }
        else if (name.equals("reasonCodeableConcept")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else if (name.equals("reasonReference")) {
          this.reason = new Reference();
          return this.reason;
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
        dst.orderedItem = orderedItem == null ? null : orderedItem.copy();
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        dst.authoredOn = authoredOn == null ? null : authoredOn.copy();
        dst.requester = requester == null ? null : requester.copy();
        if (supplier != null) {
          dst.supplier = new ArrayList<Reference>();
          for (Reference i : supplier)
            dst.supplier.add(i.copy());
        };
        dst.reason = reason == null ? null : reason.copy();
        dst.deliverFrom = deliverFrom == null ? null : deliverFrom.copy();
        dst.deliverTo = deliverTo == null ? null : deliverTo.copy();
        return dst;
      }

      protected SupplyRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SupplyRequest))
          return false;
        SupplyRequest o = (SupplyRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(priority, o.priority, true) && compareDeep(orderedItem, o.orderedItem, true) && compareDeep(occurrence, o.occurrence, true)
           && compareDeep(authoredOn, o.authoredOn, true) && compareDeep(requester, o.requester, true) && compareDeep(supplier, o.supplier, true)
           && compareDeep(reason, o.reason, true) && compareDeep(deliverFrom, o.deliverFrom, true) && compareDeep(deliverTo, o.deliverTo, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupplyRequest))
          return false;
        SupplyRequest o = (SupplyRequest) other;
        return compareValues(status, o.status, true) && compareValues(priority, o.priority, true) && compareValues(authoredOn, o.authoredOn, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, category
          , priority, orderedItem, occurrence, authoredOn, requester, supplier, reason, deliverFrom
          , deliverTo);
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
   * Path: <b>SupplyRequest.requester.agent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="SupplyRequest.requester.agent", description="Individual making the request", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>Individual making the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyRequest.requester.agent</b><br>
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
  @SearchParamDefinition(name="supplier", path="SupplyRequest.supplier", description="Who is intended to fulfill the request", type="reference", target={Organization.class } )
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

