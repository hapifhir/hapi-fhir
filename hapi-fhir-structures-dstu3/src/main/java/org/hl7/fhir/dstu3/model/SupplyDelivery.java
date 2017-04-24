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
 * Record of delivery of what is supplied.
 */
@ResourceDef(name="SupplyDelivery", profile="http://hl7.org/fhir/Profile/SupplyDelivery")
public class SupplyDelivery extends DomainResource {

    public enum SupplyDeliveryStatus {
        /**
         * Supply has been requested, but not delivered.
         */
        INPROGRESS, 
        /**
         * Supply has been delivered ("completed").
         */
        COMPLETED, 
        /**
         * Delivery was not completed.
         */
        ABANDONED, 
        /**
         * This electronic record should never have existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".)
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SupplyDeliveryStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("abandoned".equals(codeString))
          return ABANDONED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SupplyDeliveryStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case ABANDONED: return "abandoned";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "http://hl7.org/fhir/supplydelivery-status";
            case COMPLETED: return "http://hl7.org/fhir/supplydelivery-status";
            case ABANDONED: return "http://hl7.org/fhir/supplydelivery-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/supplydelivery-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "Supply has been requested, but not delivered.";
            case COMPLETED: return "Supply has been delivered (\"completed\").";
            case ABANDONED: return "Delivery was not completed.";
            case ENTEREDINERROR: return "This electronic record should never have existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case COMPLETED: return "Delivered";
            case ABANDONED: return "Abandoned";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class SupplyDeliveryStatusEnumFactory implements EnumFactory<SupplyDeliveryStatus> {
    public SupplyDeliveryStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return SupplyDeliveryStatus.INPROGRESS;
        if ("completed".equals(codeString))
          return SupplyDeliveryStatus.COMPLETED;
        if ("abandoned".equals(codeString))
          return SupplyDeliveryStatus.ABANDONED;
        if ("entered-in-error".equals(codeString))
          return SupplyDeliveryStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown SupplyDeliveryStatus code '"+codeString+"'");
        }
        public Enumeration<SupplyDeliveryStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SupplyDeliveryStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("in-progress".equals(codeString))
          return new Enumeration<SupplyDeliveryStatus>(this, SupplyDeliveryStatus.INPROGRESS);
        if ("completed".equals(codeString))
          return new Enumeration<SupplyDeliveryStatus>(this, SupplyDeliveryStatus.COMPLETED);
        if ("abandoned".equals(codeString))
          return new Enumeration<SupplyDeliveryStatus>(this, SupplyDeliveryStatus.ABANDONED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<SupplyDeliveryStatus>(this, SupplyDeliveryStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown SupplyDeliveryStatus code '"+codeString+"'");
        }
    public String toCode(SupplyDeliveryStatus code) {
      if (code == SupplyDeliveryStatus.INPROGRESS)
        return "in-progress";
      if (code == SupplyDeliveryStatus.COMPLETED)
        return "completed";
      if (code == SupplyDeliveryStatus.ABANDONED)
        return "abandoned";
      if (code == SupplyDeliveryStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(SupplyDeliveryStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class SupplyDeliverySuppliedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The amount of supply that has been dispensed. Includes unit of measure.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Amount dispensed", formalDefinition="The amount of supply that has been dispensed. Includes unit of measure." )
        protected SimpleQuantity quantity;

        /**
         * Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.
         */
        @Child(name = "item", type = {CodeableConcept.class, Medication.class, Substance.class, Device.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Medication, Substance, or Device supplied", formalDefinition="Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supply-item")
        protected Type item;

        private static final long serialVersionUID = 80196045L;

    /**
     * Constructor
     */
      public SupplyDeliverySuppliedItemComponent() {
        super();
      }

        /**
         * @return {@link #quantity} (The amount of supply that has been dispensed. Includes unit of measure.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyDeliverySuppliedItemComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of supply that has been dispensed. Includes unit of measure.)
         */
        public SupplyDeliverySuppliedItemComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #item} (Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
         */
        public Type getItem() { 
          return this.item;
        }

        /**
         * @return {@link #item} (Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
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
         * @return {@link #item} (Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
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
         * @param value {@link #item} (Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.)
         */
        public SupplyDeliverySuppliedItemComponent setItem(Type value) { 
          this.item = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("quantity", "SimpleQuantity", "The amount of supply that has been dispensed. Includes unit of measure.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("item[x]", "CodeableConcept|Reference(Medication|Substance|Device)", "Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a code that identifies the item from a known list.", 0, java.lang.Integer.MAX_VALUE, item));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
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
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
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
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case 3242771: /*item*/ return new String[] {"CodeableConcept", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
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

      public SupplyDeliverySuppliedItemComponent copy() {
        SupplyDeliverySuppliedItemComponent dst = new SupplyDeliverySuppliedItemComponent();
        copyValues(dst);
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.item = item == null ? null : item.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SupplyDeliverySuppliedItemComponent))
          return false;
        SupplyDeliverySuppliedItemComponent o = (SupplyDeliverySuppliedItemComponent) other;
        return compareDeep(quantity, o.quantity, true) && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupplyDeliverySuppliedItemComponent))
          return false;
        SupplyDeliverySuppliedItemComponent o = (SupplyDeliverySuppliedItemComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(quantity, item);
      }

  public String fhirType() {
    return "SupplyDelivery.suppliedItem";

  }

  }

    /**
     * Identifier assigned by the dispensing facility when the item(s) is dispensed.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="External identifier", formalDefinition="Identifier assigned by the dispensing facility when the item(s) is dispensed." )
    protected Identifier identifier;

    /**
     * A plan, proposal or order that is fulfilled in whole or in part by this event.
     */
    @Child(name = "basedOn", type = {SupplyRequest.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Fulfills plan, proposal or order", formalDefinition="A plan, proposal or order that is fulfilled in whole or in part by this event." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (A plan, proposal or order that is fulfilled in whole or in part by this event.)
     */
    protected List<SupplyRequest> basedOnTarget;


    /**
     * A larger event of which this particular event is a component or step.
     */
    @Child(name = "partOf", type = {SupplyDelivery.class, Contract.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of referenced event", formalDefinition="A larger event of which this particular event is a component or step." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (A larger event of which this particular event is a component or step.)
     */
    protected List<Resource> partOfTarget;


    /**
     * A code specifying the state of the dispense event.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | completed | abandoned | entered-in-error", formalDefinition="A code specifying the state of the dispense event." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supplydelivery-status")
    protected Enumeration<SupplyDeliveryStatus> status;

    /**
     * A link to a resource representing the person whom the delivered item is for.
     */
    @Child(name = "patient", type = {Patient.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Patient for whom the item is supplied", formalDefinition="A link to a resource representing the person whom the delivered item is for." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person whom the delivered item is for.)
     */
    protected Patient patientTarget;

    /**
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Category of dispense event", formalDefinition="Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supplydelivery-type")
    protected CodeableConcept type;

    /**
     * The item that is being delivered or has been supplied.
     */
    @Child(name = "suppliedItem", type = {}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The item that is delivered or supplied", formalDefinition="The item that is being delivered or has been supplied." )
    protected SupplyDeliverySuppliedItemComponent suppliedItem;

    /**
     * The date or time(s) the activity occurred.
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class, Timing.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When event occurred", formalDefinition="The date or time(s) the activity occurred." )
    protected Type occurrence;

    /**
     * The individual responsible for dispensing the medication, supplier or device.
     */
    @Child(name = "supplier", type = {Practitioner.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Dispenser", formalDefinition="The individual responsible for dispensing the medication, supplier or device." )
    protected Reference supplier;

    /**
     * The actual object that is the target of the reference (The individual responsible for dispensing the medication, supplier or device.)
     */
    protected Resource supplierTarget;

    /**
     * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
     */
    @Child(name = "destination", type = {Location.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Where the Supply was sent", formalDefinition="Identification of the facility/location where the Supply was shipped to, as part of the dispense event." )
    protected Reference destination;

    /**
     * The actual object that is the target of the reference (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
     */
    protected Location destinationTarget;

    /**
     * Identifies the person who picked up the Supply.
     */
    @Child(name = "receiver", type = {Practitioner.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who collected the Supply", formalDefinition="Identifies the person who picked up the Supply." )
    protected List<Reference> receiver;
    /**
     * The actual objects that are the target of the reference (Identifies the person who picked up the Supply.)
     */
    protected List<Practitioner> receiverTarget;


    private static final long serialVersionUID = 2033462996L;

  /**
   * Constructor
   */
    public SupplyDelivery() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifier assigned by the dispensing facility when the item(s) is dispensed.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.identifier");
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
    public SupplyDelivery setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #basedOn} (A plan, proposal or order that is fulfilled in whole or in part by this event.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SupplyDelivery setBasedOn(List<Reference> theBasedOn) { 
      this.basedOn = theBasedOn;
      return this;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    public SupplyDelivery addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #basedOn}, creating it if it does not already exist
     */
    public Reference getBasedOnFirstRep() { 
      if (getBasedOn().isEmpty()) {
        addBasedOn();
      }
      return getBasedOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<SupplyRequest> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<SupplyRequest>();
      return this.basedOnTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public SupplyRequest addBasedOnTarget() { 
      SupplyRequest r = new SupplyRequest();
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<SupplyRequest>();
      this.basedOnTarget.add(r);
      return r;
    }

    /**
     * @return {@link #partOf} (A larger event of which this particular event is a component or step.)
     */
    public List<Reference> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SupplyDelivery setPartOf(List<Reference> thePartOf) { 
      this.partOf = thePartOf;
      return this;
    }

    public boolean hasPartOf() { 
      if (this.partOf == null)
        return false;
      for (Reference item : this.partOf)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPartOf() { //3
      Reference t = new Reference();
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return t;
    }

    public SupplyDelivery addPartOf(Reference t) { //3
      if (t == null)
        return this;
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #partOf}, creating it if it does not already exist
     */
    public Reference getPartOfFirstRep() { 
      if (getPartOf().isEmpty()) {
        addPartOf();
      }
      return getPartOf().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getPartOfTarget() { 
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<Resource>();
      return this.partOfTarget;
    }

    /**
     * @return {@link #status} (A code specifying the state of the dispense event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<SupplyDeliveryStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<SupplyDeliveryStatus>(new SupplyDeliveryStatusEnumFactory()); // bb
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
    public SupplyDelivery setStatusElement(Enumeration<SupplyDeliveryStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the state of the dispense event.
     */
    public SupplyDeliveryStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the state of the dispense event.
     */
    public SupplyDelivery setStatus(SupplyDeliveryStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<SupplyDeliveryStatus>(new SupplyDeliveryStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #patient} (A link to a resource representing the person whom the delivered item is for.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (A link to a resource representing the person whom the delivered item is for.)
     */
    public SupplyDelivery setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person whom the delivered item is for.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person whom the delivered item is for.)
     */
    public SupplyDelivery setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #type} (Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.type");
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
    public SupplyDelivery setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #suppliedItem} (The item that is being delivered or has been supplied.)
     */
    public SupplyDeliverySuppliedItemComponent getSuppliedItem() { 
      if (this.suppliedItem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.suppliedItem");
        else if (Configuration.doAutoCreate())
          this.suppliedItem = new SupplyDeliverySuppliedItemComponent(); // cc
      return this.suppliedItem;
    }

    public boolean hasSuppliedItem() { 
      return this.suppliedItem != null && !this.suppliedItem.isEmpty();
    }

    /**
     * @param value {@link #suppliedItem} (The item that is being delivered or has been supplied.)
     */
    public SupplyDelivery setSuppliedItem(SupplyDeliverySuppliedItemComponent value) { 
      this.suppliedItem = value;
      return this;
    }

    /**
     * @return {@link #occurrence} (The date or time(s) the activity occurred.)
     */
    public Type getOccurrence() { 
      return this.occurrence;
    }

    /**
     * @return {@link #occurrence} (The date or time(s) the activity occurred.)
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
     * @return {@link #occurrence} (The date or time(s) the activity occurred.)
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
     * @return {@link #occurrence} (The date or time(s) the activity occurred.)
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
     * @param value {@link #occurrence} (The date or time(s) the activity occurred.)
     */
    public SupplyDelivery setOccurrence(Type value) { 
      this.occurrence = value;
      return this;
    }

    /**
     * @return {@link #supplier} (The individual responsible for dispensing the medication, supplier or device.)
     */
    public Reference getSupplier() { 
      if (this.supplier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.supplier");
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
    public SupplyDelivery setSupplier(Reference value) { 
      this.supplier = value;
      return this;
    }

    /**
     * @return {@link #supplier} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual responsible for dispensing the medication, supplier or device.)
     */
    public Resource getSupplierTarget() { 
      return this.supplierTarget;
    }

    /**
     * @param value {@link #supplier} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual responsible for dispensing the medication, supplier or device.)
     */
    public SupplyDelivery setSupplierTarget(Resource value) { 
      this.supplierTarget = value;
      return this;
    }

    /**
     * @return {@link #destination} (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
     */
    public Reference getDestination() { 
      if (this.destination == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.destination");
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
    public SupplyDelivery setDestination(Reference value) { 
      this.destination = value;
      return this;
    }

    /**
     * @return {@link #destination} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
     */
    public Location getDestinationTarget() { 
      if (this.destinationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.destination");
        else if (Configuration.doAutoCreate())
          this.destinationTarget = new Location(); // aa
      return this.destinationTarget;
    }

    /**
     * @param value {@link #destination} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
     */
    public SupplyDelivery setDestinationTarget(Location value) { 
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SupplyDelivery setReceiver(List<Reference> theReceiver) { 
      this.receiver = theReceiver;
      return this;
    }

    public boolean hasReceiver() { 
      if (this.receiver == null)
        return false;
      for (Reference item : this.receiver)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReceiver() { //3
      Reference t = new Reference();
      if (this.receiver == null)
        this.receiver = new ArrayList<Reference>();
      this.receiver.add(t);
      return t;
    }

    public SupplyDelivery addReceiver(Reference t) { //3
      if (t == null)
        return this;
      if (this.receiver == null)
        this.receiver = new ArrayList<Reference>();
      this.receiver.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #receiver}, creating it if it does not already exist
     */
    public Reference getReceiverFirstRep() { 
      if (getReceiver().isEmpty()) {
        addReceiver();
      }
      return getReceiver().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Practitioner> getReceiverTarget() { 
      if (this.receiverTarget == null)
        this.receiverTarget = new ArrayList<Practitioner>();
      return this.receiverTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
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
        childrenList.add(new Property("basedOn", "Reference(SupplyRequest)", "A plan, proposal or order that is fulfilled in whole or in part by this event.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("partOf", "Reference(SupplyDelivery|Contract)", "A larger event of which this particular event is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        childrenList.add(new Property("status", "code", "A code specifying the state of the dispense event.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person whom the delivered item is for.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("suppliedItem", "", "The item that is being delivered or has been supplied.", 0, java.lang.Integer.MAX_VALUE, suppliedItem));
        childrenList.add(new Property("occurrence[x]", "dateTime|Period|Timing", "The date or time(s) the activity occurred.", 0, java.lang.Integer.MAX_VALUE, occurrence));
        childrenList.add(new Property("supplier", "Reference(Practitioner|Organization)", "The individual responsible for dispensing the medication, supplier or device.", 0, java.lang.Integer.MAX_VALUE, supplier));
        childrenList.add(new Property("destination", "Reference(Location)", "Identification of the facility/location where the Supply was shipped to, as part of the dispense event.", 0, java.lang.Integer.MAX_VALUE, destination));
        childrenList.add(new Property("receiver", "Reference(Practitioner)", "Identifies the person who picked up the Supply.", 0, java.lang.Integer.MAX_VALUE, receiver));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<SupplyDeliveryStatus>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1993333233: /*suppliedItem*/ return this.suppliedItem == null ? new Base[0] : new Base[] {this.suppliedItem}; // SupplyDeliverySuppliedItemComponent
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case -1663305268: /*supplier*/ return this.supplier == null ? new Base[0] : new Base[] {this.supplier}; // Reference
        case -1429847026: /*destination*/ return this.destination == null ? new Base[0] : new Base[] {this.destination}; // Reference
        case -808719889: /*receiver*/ return this.receiver == null ? new Base[0] : this.receiver.toArray(new Base[this.receiver.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new SupplyDeliveryStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<SupplyDeliveryStatus>
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1993333233: // suppliedItem
          this.suppliedItem = (SupplyDeliverySuppliedItemComponent) value; // SupplyDeliverySuppliedItemComponent
          return value;
        case 1687874001: // occurrence
          this.occurrence = castToType(value); // Type
          return value;
        case -1663305268: // supplier
          this.supplier = castToReference(value); // Reference
          return value;
        case -1429847026: // destination
          this.destination = castToReference(value); // Reference
          return value;
        case -808719889: // receiver
          this.getReceiver().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new SupplyDeliveryStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<SupplyDeliveryStatus>
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("suppliedItem")) {
          this.suppliedItem = (SupplyDeliverySuppliedItemComponent) value; // SupplyDeliverySuppliedItemComponent
        } else if (name.equals("occurrence[x]")) {
          this.occurrence = castToType(value); // Type
        } else if (name.equals("supplier")) {
          this.supplier = castToReference(value); // Reference
        } else if (name.equals("destination")) {
          this.destination = castToReference(value); // Reference
        } else if (name.equals("receiver")) {
          this.getReceiver().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -332612366:  return addBasedOn(); 
        case -995410646:  return addPartOf(); 
        case -892481550:  return getStatusElement();
        case -791418107:  return getPatient(); 
        case 3575610:  return getType(); 
        case 1993333233:  return getSuppliedItem(); 
        case -2022646513:  return getOccurrence(); 
        case 1687874001:  return getOccurrence(); 
        case -1663305268:  return getSupplier(); 
        case -1429847026:  return getDestination(); 
        case -808719889:  return addReceiver(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1993333233: /*suppliedItem*/ return new String[] {};
        case 1687874001: /*occurrence*/ return new String[] {"dateTime", "Period", "Timing"};
        case -1663305268: /*supplier*/ return new String[] {"Reference"};
        case -1429847026: /*destination*/ return new String[] {"Reference"};
        case -808719889: /*receiver*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("partOf")) {
          return addPartOf();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type SupplyDelivery.status");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("suppliedItem")) {
          this.suppliedItem = new SupplyDeliverySuppliedItemComponent();
          return this.suppliedItem;
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
        else if (name.equals("supplier")) {
          this.supplier = new Reference();
          return this.supplier;
        }
        else if (name.equals("destination")) {
          this.destination = new Reference();
          return this.destination;
        }
        else if (name.equals("receiver")) {
          return addReceiver();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SupplyDelivery";

  }

      public SupplyDelivery copy() {
        SupplyDelivery dst = new SupplyDelivery();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        if (partOf != null) {
          dst.partOf = new ArrayList<Reference>();
          for (Reference i : partOf)
            dst.partOf.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.type = type == null ? null : type.copy();
        dst.suppliedItem = suppliedItem == null ? null : suppliedItem.copy();
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        dst.supplier = supplier == null ? null : supplier.copy();
        dst.destination = destination == null ? null : destination.copy();
        if (receiver != null) {
          dst.receiver = new ArrayList<Reference>();
          for (Reference i : receiver)
            dst.receiver.add(i.copy());
        };
        return dst;
      }

      protected SupplyDelivery typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SupplyDelivery))
          return false;
        SupplyDelivery o = (SupplyDelivery) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(basedOn, o.basedOn, true) && compareDeep(partOf, o.partOf, true)
           && compareDeep(status, o.status, true) && compareDeep(patient, o.patient, true) && compareDeep(type, o.type, true)
           && compareDeep(suppliedItem, o.suppliedItem, true) && compareDeep(occurrence, o.occurrence, true)
           && compareDeep(supplier, o.supplier, true) && compareDeep(destination, o.destination, true) && compareDeep(receiver, o.receiver, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupplyDelivery))
          return false;
        SupplyDelivery o = (SupplyDelivery) other;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, basedOn, partOf
          , status, patient, type, suppliedItem, occurrence, supplier, destination, receiver
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SupplyDelivery;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyDelivery.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="SupplyDelivery.identifier", description="External identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyDelivery.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>receiver</b>
   * <p>
   * Description: <b>Who collected the Supply</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyDelivery.receiver</b><br>
   * </p>
   */
  @SearchParamDefinition(name="receiver", path="SupplyDelivery.receiver", description="Who collected the Supply", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_RECEIVER = "receiver";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>receiver</b>
   * <p>
   * Description: <b>Who collected the Supply</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyDelivery.receiver</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECEIVER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECEIVER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>SupplyDelivery:receiver</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECEIVER = new ca.uhn.fhir.model.api.Include("SupplyDelivery:receiver").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Patient for whom the item is supplied</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyDelivery.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="SupplyDelivery.patient", description="Patient for whom the item is supplied", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Patient for whom the item is supplied</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyDelivery.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>SupplyDelivery:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("SupplyDelivery:patient").toLocked();

 /**
   * Search parameter: <b>supplier</b>
   * <p>
   * Description: <b>Dispenser</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyDelivery.supplier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="supplier", path="SupplyDelivery.supplier", description="Dispenser", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class } )
  public static final String SP_SUPPLIER = "supplier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>supplier</b>
   * <p>
   * Description: <b>Dispenser</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SupplyDelivery.supplier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUPPLIER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUPPLIER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>SupplyDelivery:supplier</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUPPLIER = new ca.uhn.fhir.model.api.Include("SupplyDelivery:supplier").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>in-progress | completed | abandoned | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyDelivery.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="SupplyDelivery.status", description="in-progress | completed | abandoned | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>in-progress | completed | abandoned | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SupplyDelivery.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

