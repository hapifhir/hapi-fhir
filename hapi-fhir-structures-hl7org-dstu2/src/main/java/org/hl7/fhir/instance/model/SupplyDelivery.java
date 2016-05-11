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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
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
         * Dispensing was not completed.
         */
        ABANDONED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SupplyDeliveryStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("abandoned".equals(codeString))
          return ABANDONED;
        throw new Exception("Unknown SupplyDeliveryStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case ABANDONED: return "abandoned";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "http://hl7.org/fhir/supplydelivery-status";
            case COMPLETED: return "http://hl7.org/fhir/supplydelivery-status";
            case ABANDONED: return "http://hl7.org/fhir/supplydelivery-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "Supply has been requested, but not delivered.";
            case COMPLETED: return "Supply has been delivered (\"completed\").";
            case ABANDONED: return "Dispensing was not completed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case COMPLETED: return "Delivered";
            case ABANDONED: return "Abandoned";
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
        throw new IllegalArgumentException("Unknown SupplyDeliveryStatus code '"+codeString+"'");
        }
    public String toCode(SupplyDeliveryStatus code) {
      if (code == SupplyDeliveryStatus.INPROGRESS)
        return "in-progress";
      if (code == SupplyDeliveryStatus.COMPLETED)
        return "completed";
      if (code == SupplyDeliveryStatus.ABANDONED)
        return "abandoned";
      return "?";
      }
    }

    /**
     * Identifier assigned by the dispensing facility when the item(s) is dispensed.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="External identifier", formalDefinition="Identifier assigned by the dispensing facility when the item(s) is dispensed." )
    protected Identifier identifier;

    /**
     * A code specifying the state of the dispense event.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | completed | abandoned", formalDefinition="A code specifying the state of the dispense event." )
    protected Enumeration<SupplyDeliveryStatus> status;

    /**
     * A link to a resource representing the person whom the delivered item is for.
     */
    @Child(name = "patient", type = {Patient.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient for whom the item is supplied", formalDefinition="A link to a resource representing the person whom the delivered item is for." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person whom the delivered item is for.)
     */
    protected Patient patientTarget;

    /**
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Category of dispense event", formalDefinition="Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc." )
    protected CodeableConcept type;

    /**
     * The amount of supply that has been dispensed. Includes unit of measure.
     */
    @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Amount dispensed", formalDefinition="The amount of supply that has been dispensed. Includes unit of measure." )
    protected SimpleQuantity quantity;

    /**
     * Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.
     */
    @Child(name = "suppliedItem", type = {Medication.class, Substance.class, Device.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Medication, Substance, or Device supplied", formalDefinition="Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list." )
    protected Reference suppliedItem;

    /**
     * The actual object that is the target of the reference (Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
     */
    protected Resource suppliedItemTarget;

    /**
     * The individual responsible for dispensing the medication, supplier or device.
     */
    @Child(name = "supplier", type = {Practitioner.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Dispenser", formalDefinition="The individual responsible for dispensing the medication, supplier or device." )
    protected Reference supplier;

    /**
     * The actual object that is the target of the reference (The individual responsible for dispensing the medication, supplier or device.)
     */
    protected Practitioner supplierTarget;

    /**
     * The time the dispense event occurred.
     */
    @Child(name = "whenPrepared", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Dispensing time", formalDefinition="The time the dispense event occurred." )
    protected Period whenPrepared;

    /**
     * The time the dispensed item was sent or handed to the patient (or agent).
     */
    @Child(name = "time", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Handover time", formalDefinition="The time the dispensed item was sent or handed to the patient (or agent)." )
    protected DateTimeType time;

    /**
     * Identification of the facility/location where the Supply was shipped to, as part of the dispense event.
     */
    @Child(name = "destination", type = {Location.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the Supply was sent", formalDefinition="Identification of the facility/location where the Supply was shipped to, as part of the dispense event." )
    protected Reference destination;

    /**
     * The actual object that is the target of the reference (Identification of the facility/location where the Supply was shipped to, as part of the dispense event.)
     */
    protected Location destinationTarget;

    /**
     * Identifies the person who picked up the Supply.
     */
    @Child(name = "receiver", type = {Practitioner.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who collected the Supply", formalDefinition="Identifies the person who picked up the Supply." )
    protected List<Reference> receiver;
    /**
     * The actual objects that are the target of the reference (Identifies the person who picked up the Supply.)
     */
    protected List<Practitioner> receiverTarget;


    private static final long serialVersionUID = -1520129707L;

  /*
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
     * @return {@link #quantity} (The amount of supply that has been dispensed. Includes unit of measure.)
     */
    public SimpleQuantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.quantity");
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
    public SupplyDelivery setQuantity(SimpleQuantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #suppliedItem} (Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
     */
    public Reference getSuppliedItem() { 
      if (this.suppliedItem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.suppliedItem");
        else if (Configuration.doAutoCreate())
          this.suppliedItem = new Reference(); // cc
      return this.suppliedItem;
    }

    public boolean hasSuppliedItem() { 
      return this.suppliedItem != null && !this.suppliedItem.isEmpty();
    }

    /**
     * @param value {@link #suppliedItem} (Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
     */
    public SupplyDelivery setSuppliedItem(Reference value) { 
      this.suppliedItem = value;
      return this;
    }

    /**
     * @return {@link #suppliedItem} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
     */
    public Resource getSuppliedItemTarget() { 
      return this.suppliedItemTarget;
    }

    /**
     * @param value {@link #suppliedItem} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.)
     */
    public SupplyDelivery setSuppliedItemTarget(Resource value) { 
      this.suppliedItemTarget = value;
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
    public Practitioner getSupplierTarget() { 
      if (this.supplierTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.supplier");
        else if (Configuration.doAutoCreate())
          this.supplierTarget = new Practitioner(); // aa
      return this.supplierTarget;
    }

    /**
     * @param value {@link #supplier} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual responsible for dispensing the medication, supplier or device.)
     */
    public SupplyDelivery setSupplierTarget(Practitioner value) { 
      this.supplierTarget = value;
      return this;
    }

    /**
     * @return {@link #whenPrepared} (The time the dispense event occurred.)
     */
    public Period getWhenPrepared() { 
      if (this.whenPrepared == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.whenPrepared");
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
    public SupplyDelivery setWhenPrepared(Period value) { 
      this.whenPrepared = value;
      return this;
    }

    /**
     * @return {@link #time} (The time the dispensed item was sent or handed to the patient (or agent).). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
     */
    public DateTimeType getTimeElement() { 
      if (this.time == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyDelivery.time");
        else if (Configuration.doAutoCreate())
          this.time = new DateTimeType(); // bb
      return this.time;
    }

    public boolean hasTimeElement() { 
      return this.time != null && !this.time.isEmpty();
    }

    public boolean hasTime() { 
      return this.time != null && !this.time.isEmpty();
    }

    /**
     * @param value {@link #time} (The time the dispensed item was sent or handed to the patient (or agent).). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
     */
    public SupplyDelivery setTimeElement(DateTimeType value) { 
      this.time = value;
      return this;
    }

    /**
     * @return The time the dispensed item was sent or handed to the patient (or agent).
     */
    public Date getTime() { 
      return this.time == null ? null : this.time.getValue();
    }

    /**
     * @param value The time the dispensed item was sent or handed to the patient (or agent).
     */
    public SupplyDelivery setTime(Date value) { 
      if (value == null)
        this.time = null;
      else {
        if (this.time == null)
          this.time = new DateTimeType();
        this.time.setValue(value);
      }
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
    public SupplyDelivery addReceiver(Reference t) { //3
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
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person whom the delivered item is for.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("quantity", "SimpleQuantity", "The amount of supply that has been dispensed. Includes unit of measure.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("suppliedItem", "Reference(Medication|Substance|Device)", "Identifies the medication, substance or device being dispensed. This is either a link to a resource representing the details of the item or a simple attribute carrying a code that identifies the item from a known list.", 0, java.lang.Integer.MAX_VALUE, suppliedItem));
        childrenList.add(new Property("supplier", "Reference(Practitioner)", "The individual responsible for dispensing the medication, supplier or device.", 0, java.lang.Integer.MAX_VALUE, supplier));
        childrenList.add(new Property("whenPrepared", "Period", "The time the dispense event occurred.", 0, java.lang.Integer.MAX_VALUE, whenPrepared));
        childrenList.add(new Property("time", "dateTime", "The time the dispensed item was sent or handed to the patient (or agent).", 0, java.lang.Integer.MAX_VALUE, time));
        childrenList.add(new Property("destination", "Reference(Location)", "Identification of the facility/location where the Supply was shipped to, as part of the dispense event.", 0, java.lang.Integer.MAX_VALUE, destination));
        childrenList.add(new Property("receiver", "Reference(Practitioner)", "Identifies the person who picked up the Supply.", 0, java.lang.Integer.MAX_VALUE, receiver));
      }

      public SupplyDelivery copy() {
        SupplyDelivery dst = new SupplyDelivery();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.type = type == null ? null : type.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.suppliedItem = suppliedItem == null ? null : suppliedItem.copy();
        dst.supplier = supplier == null ? null : supplier.copy();
        dst.whenPrepared = whenPrepared == null ? null : whenPrepared.copy();
        dst.time = time == null ? null : time.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(patient, o.patient, true)
           && compareDeep(type, o.type, true) && compareDeep(quantity, o.quantity, true) && compareDeep(suppliedItem, o.suppliedItem, true)
           && compareDeep(supplier, o.supplier, true) && compareDeep(whenPrepared, o.whenPrepared, true) && compareDeep(time, o.time, true)
           && compareDeep(destination, o.destination, true) && compareDeep(receiver, o.receiver, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupplyDelivery))
          return false;
        SupplyDelivery o = (SupplyDelivery) other;
        return compareValues(status, o.status, true) && compareValues(time, o.time, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (patient == null || patient.isEmpty()) && (type == null || type.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (suppliedItem == null || suppliedItem.isEmpty()) && (supplier == null || supplier.isEmpty())
           && (whenPrepared == null || whenPrepared.isEmpty()) && (time == null || time.isEmpty()) && (destination == null || destination.isEmpty())
           && (receiver == null || receiver.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SupplyDelivery;
   }

  @SearchParamDefinition(name="identifier", path="SupplyDelivery.identifier", description="External identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="receiver", path="SupplyDelivery.receiver", description="Who collected the Supply", type="reference" )
  public static final String SP_RECEIVER = "receiver";
  @SearchParamDefinition(name="patient", path="SupplyDelivery.patient", description="Patient for whom the item is supplied", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="supplier", path="SupplyDelivery.supplier", description="Dispenser", type="reference" )
  public static final String SP_SUPPLIER = "supplier";
  @SearchParamDefinition(name="status", path="SupplyDelivery.status", description="in-progress | completed | abandoned", type="token" )
  public static final String SP_STATUS = "status";

}

