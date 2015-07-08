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

// Generated on Wed, Jul 8, 2015 17:35-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * A supply - a  request for something, and provision of what is supplied.
 */
@ResourceDef(name="Supply", profile="http://hl7.org/fhir/Profile/Supply")
public class Supply extends DomainResource {

    public enum SupplyStatus {
        /**
         * Supply has been requested, but not dispensed
         */
        REQUESTED, 
        /**
         * Supply is part of a pharmacy order and has been dispensed
         */
        DISPENSED, 
        /**
         * Supply has been received by the requestor
         */
        RECEIVED, 
        /**
         * The supply will not be completed because the supplier was unable or unwilling to supply the item
         */
        FAILED, 
        /**
         * The orderer of the supply cancelled the request
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SupplyStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("dispensed".equals(codeString))
          return DISPENSED;
        if ("received".equals(codeString))
          return RECEIVED;
        if ("failed".equals(codeString))
          return FAILED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new Exception("Unknown SupplyStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REQUESTED: return "requested";
            case DISPENSED: return "dispensed";
            case RECEIVED: return "received";
            case FAILED: return "failed";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REQUESTED: return "http://hl7.org/fhir/valueset-supply-status";
            case DISPENSED: return "http://hl7.org/fhir/valueset-supply-status";
            case RECEIVED: return "http://hl7.org/fhir/valueset-supply-status";
            case FAILED: return "http://hl7.org/fhir/valueset-supply-status";
            case CANCELLED: return "http://hl7.org/fhir/valueset-supply-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REQUESTED: return "Supply has been requested, but not dispensed";
            case DISPENSED: return "Supply is part of a pharmacy order and has been dispensed";
            case RECEIVED: return "Supply has been received by the requestor";
            case FAILED: return "The supply will not be completed because the supplier was unable or unwilling to supply the item";
            case CANCELLED: return "The orderer of the supply cancelled the request";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUESTED: return "Requested";
            case DISPENSED: return "Dispensed";
            case RECEIVED: return "Received";
            case FAILED: return "Failed";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
        }
    }

  public static class SupplyStatusEnumFactory implements EnumFactory<SupplyStatus> {
    public SupplyStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return SupplyStatus.REQUESTED;
        if ("dispensed".equals(codeString))
          return SupplyStatus.DISPENSED;
        if ("received".equals(codeString))
          return SupplyStatus.RECEIVED;
        if ("failed".equals(codeString))
          return SupplyStatus.FAILED;
        if ("cancelled".equals(codeString))
          return SupplyStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown SupplyStatus code '"+codeString+"'");
        }
    public String toCode(SupplyStatus code) {
      if (code == SupplyStatus.REQUESTED)
        return "requested";
      if (code == SupplyStatus.DISPENSED)
        return "dispensed";
      if (code == SupplyStatus.RECEIVED)
        return "received";
      if (code == SupplyStatus.FAILED)
        return "failed";
      if (code == SupplyStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    }

    public enum SupplyDispenseStatus {
        /**
         * Supply has been requested, but not dispensed
         */
        INPROGRESS, 
        /**
         * Supply is part of a pharmacy order and has been dispensed
         */
        DISPENSED, 
        /**
         * Dispensing was not completed
         */
        ABANDONED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SupplyDispenseStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("dispensed".equals(codeString))
          return DISPENSED;
        if ("abandoned".equals(codeString))
          return ABANDONED;
        throw new Exception("Unknown SupplyDispenseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case DISPENSED: return "dispensed";
            case ABANDONED: return "abandoned";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "http://hl7.org/fhir/valueset-supply-dispense-status";
            case DISPENSED: return "http://hl7.org/fhir/valueset-supply-dispense-status";
            case ABANDONED: return "http://hl7.org/fhir/valueset-supply-dispense-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "Supply has been requested, but not dispensed";
            case DISPENSED: return "Supply is part of a pharmacy order and has been dispensed";
            case ABANDONED: return "Dispensing was not completed";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case DISPENSED: return "Dispensed";
            case ABANDONED: return "Abandoned";
            default: return "?";
          }
        }
    }

  public static class SupplyDispenseStatusEnumFactory implements EnumFactory<SupplyDispenseStatus> {
    public SupplyDispenseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return SupplyDispenseStatus.INPROGRESS;
        if ("dispensed".equals(codeString))
          return SupplyDispenseStatus.DISPENSED;
        if ("abandoned".equals(codeString))
          return SupplyDispenseStatus.ABANDONED;
        throw new IllegalArgumentException("Unknown SupplyDispenseStatus code '"+codeString+"'");
        }
    public String toCode(SupplyDispenseStatus code) {
      if (code == SupplyDispenseStatus.INPROGRESS)
        return "in-progress";
      if (code == SupplyDispenseStatus.DISPENSED)
        return "dispensed";
      if (code == SupplyDispenseStatus.ABANDONED)
        return "abandoned";
      return "?";
      }
    }

    @Block()
    public static class SupplyDispenseComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifier assigned by the dispensing facility when the item(s) is dispensed.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1)
        @Description(shortDefinition="External identifier", formalDefinition="Identifier assigned by the dispensing facility when the item(s) is dispensed." )
        protected Identifier identifier;

        /**
         * A code specifying the state of the dispense event.
         */
        @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="in-progress | dispensed | abandoned", formalDefinition="A code specifying the state of the dispense event." )
        protected Enumeration<SupplyDispenseStatus> status;

        /**
         * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Category of dispense event", formalDefinition="Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc." )
        protected CodeableConcept type;

        /**
         * The amount of supply that has been dispensed. Includes unit of measure.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Amount dispensed", formalDefinition="The amount of supply that has been dispensed. Includes unit of measure." )
        protected Quantity quantity;

        /**
         * Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.
         */
        @Child(name = "suppliedItem", type = {Medication.class, Substance.class, Device.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Medication, Substance, or Device supplied", formalDefinition="Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list." )
        protected Reference suppliedItem;

        /**
         * The actual object that is the target of the reference (Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
         */
        protected Resource suppliedItemTarget;

        /**
         * The individual responsible for dispensing the medication, supplier or device.
         */
        @Child(name = "supplier", type = {Practitioner.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Dispenser", formalDefinition="The individual responsible for dispensing the medication, supplier or device." )
        protected Reference supplier;

        /**
         * The actual object that is the target of the reference (The individual responsible for dispensing the medication, supplier or device.)
         */
        protected Practitioner supplierTarget;

        /**
         * The time the dispense event occurred.
         */
        @Child(name = "whenPrepared", type = {Period.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Dispensing time", formalDefinition="The time the dispense event occurred." )
        protected Period whenPrepared;

        /**
         * The time the dispensed item was sent or handed to the patient (or agent).
         */
        @Child(name = "whenHandedOver", type = {DateTimeType.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Handover time", formalDefinition="The time the dispensed item was sent or handed to the patient (or agent)." )
        protected DateTimeType whenHandedOver;

        /**
         * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
         */
        @Child(name = "destination", type = {Location.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Where the Supply was sent", formalDefinition="Identification of the facility/location where the Supply was shipped to, as part of the dispense event." )
        protected Reference destination;

        /**
         * The actual object that is the target of the reference (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
         */
        protected Location destinationTarget;

        /**
         * Identifies the person who picked up the Supply.
         */
        @Child(name = "receiver", type = {Practitioner.class}, order=10, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Who collected the Supply", formalDefinition="Identifies the person who picked up the Supply." )
        protected List<Reference> receiver;
        /**
         * The actual objects that are the target of the reference (Identifies the person who picked up the Supply.)
         */
        protected List<Practitioner> receiverTarget;


        private static final long serialVersionUID = 315252678L;

    /*
     * Constructor
     */
      public SupplyDispenseComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Identifier assigned by the dispensing facility when the item(s) is dispensed.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifier assigned by the dispensing facility when the item(s) is dispensed.)
         */
        public SupplyDispenseComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #status} (A code specifying the state of the dispense event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<SupplyDispenseStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<SupplyDispenseStatus>(new SupplyDispenseStatusEnumFactory()); // bb
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (A code specifying the state of the dispense event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public SupplyDispenseComponent setStatusElement(Enumeration<SupplyDispenseStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return A code specifying the state of the dispense event.
         */
        public SupplyDispenseStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value A code specifying the state of the dispense event.
         */
        public SupplyDispenseComponent setStatus(SupplyDispenseStatus value) { 
          if (value == null)
            this.status = null;
          else {
            if (this.status == null)
              this.status = new Enumeration<SupplyDispenseStatus>(new SupplyDispenseStatusEnumFactory());
            this.status.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.)
         */
        public SupplyDispenseComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The amount of supply that has been dispensed. Includes unit of measure.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of supply that has been dispensed. Includes unit of measure.)
         */
        public SupplyDispenseComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #suppliedItem} (Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
         */
        public Reference getSuppliedItem() { 
          if (this.suppliedItem == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.suppliedItem");
            else if (Configuration.doAutoCreate())
              this.suppliedItem = new Reference(); // cc
          return this.suppliedItem;
        }

        public boolean hasSuppliedItem() { 
          return this.suppliedItem != null && !this.suppliedItem.isEmpty();
        }

        /**
         * @param value {@link #suppliedItem} (Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
         */
        public SupplyDispenseComponent setSuppliedItem(Reference value) { 
          this.suppliedItem = value;
          return this;
        }

        /**
         * @return {@link #suppliedItem} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
         */
        public Resource getSuppliedItemTarget() { 
          return this.suppliedItemTarget;
        }

        /**
         * @param value {@link #suppliedItem} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
         */
        public SupplyDispenseComponent setSuppliedItemTarget(Resource value) { 
          this.suppliedItemTarget = value;
          return this;
        }

        /**
         * @return {@link #supplier} (The individual responsible for dispensing the medication, supplier or device.)
         */
        public Reference getSupplier() { 
          if (this.supplier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.supplier");
            else if (Configuration.doAutoCreate())
              this.supplier = new Reference(); // cc
          return this.supplier;
        }

        public boolean hasSupplier() { 
          return this.supplier != null && !this.supplier.isEmpty();
        }

        /**
         * @param value {@link #supplier} (The individual responsible for dispensing the medication, supplier or device.)
         */
        public SupplyDispenseComponent setSupplier(Reference value) { 
          this.supplier = value;
          return this;
        }

        /**
         * @return {@link #supplier} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual responsible for dispensing the medication, supplier or device.)
         */
        public Practitioner getSupplierTarget() { 
          if (this.supplierTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.supplier");
            else if (Configuration.doAutoCreate())
              this.supplierTarget = new Practitioner(); // aa
          return this.supplierTarget;
        }

        /**
         * @param value {@link #supplier} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual responsible for dispensing the medication, supplier or device.)
         */
        public SupplyDispenseComponent setSupplierTarget(Practitioner value) { 
          this.supplierTarget = value;
          return this;
        }

        /**
         * @return {@link #whenPrepared} (The time the dispense event occurred.)
         */
        public Period getWhenPrepared() { 
          if (this.whenPrepared == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.whenPrepared");
            else if (Configuration.doAutoCreate())
              this.whenPrepared = new Period(); // cc
          return this.whenPrepared;
        }

        public boolean hasWhenPrepared() { 
          return this.whenPrepared != null && !this.whenPrepared.isEmpty();
        }

        /**
         * @param value {@link #whenPrepared} (The time the dispense event occurred.)
         */
        public SupplyDispenseComponent setWhenPrepared(Period value) { 
          this.whenPrepared = value;
          return this;
        }

        /**
         * @return {@link #whenHandedOver} (The time the dispensed item was sent or handed to the patient (or agent).). This is the underlying object with id, value and extensions. The accessor "getWhenHandedOver" gives direct access to the value
         */
        public DateTimeType getWhenHandedOverElement() { 
          if (this.whenHandedOver == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.whenHandedOver");
            else if (Configuration.doAutoCreate())
              this.whenHandedOver = new DateTimeType(); // bb
          return this.whenHandedOver;
        }

        public boolean hasWhenHandedOverElement() { 
          return this.whenHandedOver != null && !this.whenHandedOver.isEmpty();
        }

        public boolean hasWhenHandedOver() { 
          return this.whenHandedOver != null && !this.whenHandedOver.isEmpty();
        }

        /**
         * @param value {@link #whenHandedOver} (The time the dispensed item was sent or handed to the patient (or agent).). This is the underlying object with id, value and extensions. The accessor "getWhenHandedOver" gives direct access to the value
         */
        public SupplyDispenseComponent setWhenHandedOverElement(DateTimeType value) { 
          this.whenHandedOver = value;
          return this;
        }

        /**
         * @return The time the dispensed item was sent or handed to the patient (or agent).
         */
        public Date getWhenHandedOver() { 
          return this.whenHandedOver == null ? null : this.whenHandedOver.getValue();
        }

        /**
         * @param value The time the dispensed item was sent or handed to the patient (or agent).
         */
        public SupplyDispenseComponent setWhenHandedOver(Date value) { 
          if (value == null)
            this.whenHandedOver = null;
          else {
            if (this.whenHandedOver == null)
              this.whenHandedOver = new DateTimeType();
            this.whenHandedOver.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #destination} (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
         */
        public Reference getDestination() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destination = new Reference(); // cc
          return this.destination;
        }

        public boolean hasDestination() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        /**
         * @param value {@link #destination} (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
         */
        public SupplyDispenseComponent setDestination(Reference value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return {@link #destination} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
         */
        public Location getDestinationTarget() { 
          if (this.destinationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDispenseComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destinationTarget = new Location(); // aa
          return this.destinationTarget;
        }

        /**
         * @param value {@link #destination} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
         */
        public SupplyDispenseComponent setDestinationTarget(Location value) { 
          this.destinationTarget = value;
          return this;
        }

        /**
         * @return {@link #receiver} (Identifies the person who picked up the Supply.)
         */
        public List<Reference> getReceiver() { 
          if (this.receiver == null)
            this.receiver = new ArrayList<Reference>();
          return this.receiver;
        }

        public boolean hasReceiver() { 
          if (this.receiver == null)
            return false;
          for (Reference item : this.receiver)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #receiver} (Identifies the person who picked up the Supply.)
         */
    // syntactic sugar
        public Reference addReceiver() { //3
          Reference t = new Reference();
          if (this.receiver == null)
            this.receiver = new ArrayList<Reference>();
          this.receiver.add(t);
          return t;
        }

    // syntactic sugar
        public SupplyDispenseComponent addReceiver(Reference t) { //3
          if (t == null)
            return this;
          if (this.receiver == null)
            this.receiver = new ArrayList<Reference>();
          this.receiver.add(t);
          return this;
        }

        /**
         * @return {@link #receiver} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies the person who picked up the Supply.)
         */
        public List<Practitioner> getReceiverTarget() { 
          if (this.receiverTarget == null)
            this.receiverTarget = new ArrayList<Practitioner>();
          return this.receiverTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #receiver} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Identifies the person who picked up the Supply.)
         */
        public Practitioner addReceiverTarget() { 
          Practitioner r = new Practitioner();
          if (this.receiverTarget == null)
            this.receiverTarget = new ArrayList<Practitioner>();
          this.receiverTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Identifier assigned by the dispensing facility when the item(s) is dispensed.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("status", "code", "A code specifying the state of the dispense event.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("quantity", "Quantity", "The amount of supply that has been dispensed. Includes unit of measure.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("suppliedItem", "Reference(Medication|Substance|Device)", "Identifies the medication or substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.", 0, java.lang.Integer.MAX_VALUE, suppliedItem));
          childrenList.add(new Property("supplier", "Reference(Practitioner)", "The individual responsible for dispensing the medication, supplier or device.", 0, java.lang.Integer.MAX_VALUE, supplier));
          childrenList.add(new Property("whenPrepared", "Period", "The time the dispense event occurred.", 0, java.lang.Integer.MAX_VALUE, whenPrepared));
          childrenList.add(new Property("whenHandedOver", "dateTime", "The time the dispensed item was sent or handed to the patient (or agent).", 0, java.lang.Integer.MAX_VALUE, whenHandedOver));
          childrenList.add(new Property("destination", "Reference(Location)", "Identification of the facility/location where the Supply was shipped to, as part of the dispense event.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("receiver", "Reference(Practitioner)", "Identifies the person who picked up the Supply.", 0, java.lang.Integer.MAX_VALUE, receiver));
        }

      public SupplyDispenseComponent copy() {
        SupplyDispenseComponent dst = new SupplyDispenseComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.suppliedItem = suppliedItem == null ? null : suppliedItem.copy();
        dst.supplier = supplier == null ? null : supplier.copy();
        dst.whenPrepared = whenPrepared == null ? null : whenPrepared.copy();
        dst.whenHandedOver = whenHandedOver == null ? null : whenHandedOver.copy();
        dst.destination = destination == null ? null : destination.copy();
        if (receiver != null) {
          dst.receiver = new ArrayList<Reference>();
          for (Reference i : receiver)
            dst.receiver.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SupplyDispenseComponent))
          return false;
        SupplyDispenseComponent o = (SupplyDispenseComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(suppliedItem, o.suppliedItem, true) && compareDeep(supplier, o.supplier, true)
           && compareDeep(whenPrepared, o.whenPrepared, true) && compareDeep(whenHandedOver, o.whenHandedOver, true)
           && compareDeep(destination, o.destination, true) && compareDeep(receiver, o.receiver, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupplyDispenseComponent))
          return false;
        SupplyDispenseComponent o = (SupplyDispenseComponent) other;
        return compareValues(status, o.status, true) && compareValues(whenHandedOver, o.whenHandedOver, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (type == null || type.isEmpty()) && (quantity == null || quantity.isEmpty()) && (suppliedItem == null || suppliedItem.isEmpty())
           && (supplier == null || supplier.isEmpty()) && (whenPrepared == null || whenPrepared.isEmpty())
           && (whenHandedOver == null || whenHandedOver.isEmpty()) && (destination == null || destination.isEmpty())
           && (receiver == null || receiver.isEmpty());
      }

  }

    /**
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.
     */
    @Child(name = "kind", type = {CodeableConcept.class}, order=0, min=0, max=1)
    @Description(shortDefinition="The kind of supply (central, non-stock, etc)", formalDefinition="Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process." )
    protected CodeableConcept kind;

    /**
     * Unique identifier for this supply request.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Unique identifier", formalDefinition="Unique identifier for this supply request." )
    protected Identifier identifier;

    /**
     * Status of the supply request.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="requested | dispensed | received | failed | cancelled", formalDefinition="Status of the supply request." )
    protected Enumeration<SupplyStatus> status;

    /**
     * The item that is requested to be supplied.
     */
    @Child(name = "orderedItem", type = {Medication.class, Substance.class, Device.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Medication, Substance, or Device requested to be supplied", formalDefinition="The item that is requested to be supplied." )
    protected Reference orderedItem;

    /**
     * The actual object that is the target of the reference (The item that is requested to be supplied.)
     */
    protected Resource orderedItemTarget;

    /**
     * A link to a resource representing the person whom the ordered item is for.
     */
    @Child(name = "patient", type = {Patient.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Patient for whom the item is supplied", formalDefinition="A link to a resource representing the person whom the ordered item is for." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person whom the ordered item is for.)
     */
    protected Patient patientTarget;

    /**
     * Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.
     */
    @Child(name = "dispense", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Supply details", formalDefinition="Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed." )
    protected List<SupplyDispenseComponent> dispense;

    private static final long serialVersionUID = -1441571262L;

  /*
   * Constructor
   */
    public Supply() {
      super();
    }

    /**
     * @return {@link #kind} (Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.)
     */
    public CodeableConcept getKind() { 
      if (this.kind == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Supply.kind");
        else if (Configuration.doAutoCreate())
          this.kind = new CodeableConcept(); // cc
      return this.kind;
    }

    public boolean hasKind() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    /**
     * @param value {@link #kind} (Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.)
     */
    public Supply setKind(CodeableConcept value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Unique identifier for this supply request.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Supply.identifier");
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
    public Supply setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #status} (Status of the supply request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<SupplyStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Supply.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<SupplyStatus>(new SupplyStatusEnumFactory()); // bb
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
    public Supply setStatusElement(Enumeration<SupplyStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Status of the supply request.
     */
    public SupplyStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Status of the supply request.
     */
    public Supply setStatus(SupplyStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<SupplyStatus>(new SupplyStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #orderedItem} (The item that is requested to be supplied.)
     */
    public Reference getOrderedItem() { 
      if (this.orderedItem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Supply.orderedItem");
        else if (Configuration.doAutoCreate())
          this.orderedItem = new Reference(); // cc
      return this.orderedItem;
    }

    public boolean hasOrderedItem() { 
      return this.orderedItem != null && !this.orderedItem.isEmpty();
    }

    /**
     * @param value {@link #orderedItem} (The item that is requested to be supplied.)
     */
    public Supply setOrderedItem(Reference value) { 
      this.orderedItem = value;
      return this;
    }

    /**
     * @return {@link #orderedItem} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The item that is requested to be supplied.)
     */
    public Resource getOrderedItemTarget() { 
      return this.orderedItemTarget;
    }

    /**
     * @param value {@link #orderedItem} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The item that is requested to be supplied.)
     */
    public Supply setOrderedItemTarget(Resource value) { 
      this.orderedItemTarget = value;
      return this;
    }

    /**
     * @return {@link #patient} (A link to a resource representing the person whom the ordered item is for.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Supply.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (A link to a resource representing the person whom the ordered item is for.)
     */
    public Supply setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person whom the ordered item is for.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Supply.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person whom the ordered item is for.)
     */
    public Supply setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #dispense} (Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.)
     */
    public List<SupplyDispenseComponent> getDispense() { 
      if (this.dispense == null)
        this.dispense = new ArrayList<SupplyDispenseComponent>();
      return this.dispense;
    }

    public boolean hasDispense() { 
      if (this.dispense == null)
        return false;
      for (SupplyDispenseComponent item : this.dispense)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dispense} (Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.)
     */
    // syntactic sugar
    public SupplyDispenseComponent addDispense() { //3
      SupplyDispenseComponent t = new SupplyDispenseComponent();
      if (this.dispense == null)
        this.dispense = new ArrayList<SupplyDispenseComponent>();
      this.dispense.add(t);
      return t;
    }

    // syntactic sugar
    public Supply addDispense(SupplyDispenseComponent t) { //3
      if (t == null)
        return this;
      if (this.dispense == null)
        this.dispense = new ArrayList<SupplyDispenseComponent>();
      this.dispense.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("kind", "CodeableConcept", "Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("identifier", "Identifier", "Unique identifier for this supply request.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Status of the supply request.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("orderedItem", "Reference(Medication|Substance|Device)", "The item that is requested to be supplied.", 0, java.lang.Integer.MAX_VALUE, orderedItem));
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person whom the ordered item is for.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("dispense", "", "Indicates the details of the dispense event such as the days supply and quantity of a supply dispensed.", 0, java.lang.Integer.MAX_VALUE, dispense));
      }

      public Supply copy() {
        Supply dst = new Supply();
        copyValues(dst);
        dst.kind = kind == null ? null : kind.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.orderedItem = orderedItem == null ? null : orderedItem.copy();
        dst.patient = patient == null ? null : patient.copy();
        if (dispense != null) {
          dst.dispense = new ArrayList<SupplyDispenseComponent>();
          for (SupplyDispenseComponent i : dispense)
            dst.dispense.add(i.copy());
        };
        return dst;
      }

      protected Supply typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Supply))
          return false;
        Supply o = (Supply) other;
        return compareDeep(kind, o.kind, true) && compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true)
           && compareDeep(orderedItem, o.orderedItem, true) && compareDeep(patient, o.patient, true) && compareDeep(dispense, o.dispense, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Supply))
          return false;
        Supply o = (Supply) other;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (kind == null || kind.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (status == null || status.isEmpty()) && (orderedItem == null || orderedItem.isEmpty())
           && (patient == null || patient.isEmpty()) && (dispense == null || dispense.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Supply;
   }

  @SearchParamDefinition(name="identifier", path="Supply.identifier", description="Unique identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="kind", path="Supply.kind", description="The kind of supply (central, non-stock, etc)", type="token" )
  public static final String SP_KIND = "kind";
  @SearchParamDefinition(name="patient", path="Supply.patient", description="Patient for whom the item is supplied", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="supplier", path="Supply.dispense.supplier", description="Dispenser", type="reference" )
  public static final String SP_SUPPLIER = "supplier";
  @SearchParamDefinition(name="dispenseid", path="Supply.dispense.identifier", description="External identifier", type="token" )
  public static final String SP_DISPENSEID = "dispenseid";
  @SearchParamDefinition(name="dispensestatus", path="Supply.dispense.status", description="in-progress | dispensed | abandoned", type="token" )
  public static final String SP_DISPENSESTATUS = "dispensestatus";
  @SearchParamDefinition(name="status", path="Supply.status", description="requested | dispensed | received | failed | cancelled", type="token" )
  public static final String SP_STATUS = "status";

}

