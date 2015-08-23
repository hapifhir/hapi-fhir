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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * A record of a request for a medication, substance or device used in the healthcare setting.
 */
@ResourceDef(name="SupplyRequest", profile="http://hl7.org/fhir/Profile/SupplyRequest")
public class SupplyRequest extends DomainResource {

    public enum SupplyRequestStatus {
        /**
         * Supply has been requested, but not dispensed
         */
        REQUESTED, 
        /**
         * Supply has been received by the requestor
         */
        COMPLETED, 
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
        public static SupplyRequestStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("failed".equals(codeString))
          return FAILED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new Exception("Unknown SupplyRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REQUESTED: return "requested";
            case COMPLETED: return "completed";
            case FAILED: return "failed";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REQUESTED: return "http://hl7.org/fhir/supplyrequest-status";
            case COMPLETED: return "http://hl7.org/fhir/supplyrequest-status";
            case FAILED: return "http://hl7.org/fhir/supplyrequest-status";
            case CANCELLED: return "http://hl7.org/fhir/supplyrequest-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REQUESTED: return "Supply has been requested, but not dispensed";
            case COMPLETED: return "Supply has been received by the requestor";
            case FAILED: return "The supply will not be completed because the supplier was unable or unwilling to supply the item";
            case CANCELLED: return "The orderer of the supply cancelled the request";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUESTED: return "Requested";
            case COMPLETED: return "Received";
            case FAILED: return "Failed";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
        }
    }

  public static class SupplyRequestStatusEnumFactory implements EnumFactory<SupplyRequestStatus> {
    public SupplyRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return SupplyRequestStatus.REQUESTED;
        if ("completed".equals(codeString))
          return SupplyRequestStatus.COMPLETED;
        if ("failed".equals(codeString))
          return SupplyRequestStatus.FAILED;
        if ("cancelled".equals(codeString))
          return SupplyRequestStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown SupplyRequestStatus code '"+codeString+"'");
        }
    public String toCode(SupplyRequestStatus code) {
      if (code == SupplyRequestStatus.REQUESTED)
        return "requested";
      if (code == SupplyRequestStatus.COMPLETED)
        return "completed";
      if (code == SupplyRequestStatus.FAILED)
        return "failed";
      if (code == SupplyRequestStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    }

    @Block()
    public static class SupplyRequestWhenComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Fulfilment code.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Fulfilment code", formalDefinition="Fulfilment code." )
        protected CodeableConcept code;

        /**
         * Formal fulfillment schedule.
         */
        @Child(name = "schedule", type = {Timing.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Formal fulfillment schedule", formalDefinition="Formal fulfillment schedule." )
        protected Timing schedule;

        private static final long serialVersionUID = 307115287L;

    /*
     * Constructor
     */
      public SupplyRequestWhenComponent() {
        super();
      }

        /**
         * @return {@link #code} (Fulfilment code.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyRequestWhenComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Fulfilment code.)
         */
        public SupplyRequestWhenComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #schedule} (Formal fulfillment schedule.)
         */
        public Timing getSchedule() { 
          if (this.schedule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupplyRequestWhenComponent.schedule");
            else if (Configuration.doAutoCreate())
              this.schedule = new Timing(); // cc
          return this.schedule;
        }

        public boolean hasSchedule() { 
          return this.schedule != null && !this.schedule.isEmpty();
        }

        /**
         * @param value {@link #schedule} (Formal fulfillment schedule.)
         */
        public SupplyRequestWhenComponent setSchedule(Timing value) { 
          this.schedule = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "Fulfilment code.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("schedule", "Timing", "Formal fulfillment schedule.", 0, java.lang.Integer.MAX_VALUE, schedule));
        }

      public SupplyRequestWhenComponent copy() {
        SupplyRequestWhenComponent dst = new SupplyRequestWhenComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.schedule = schedule == null ? null : schedule.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SupplyRequestWhenComponent))
          return false;
        SupplyRequestWhenComponent o = (SupplyRequestWhenComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(schedule, o.schedule, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupplyRequestWhenComponent))
          return false;
        SupplyRequestWhenComponent o = (SupplyRequestWhenComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (schedule == null || schedule.isEmpty())
          ;
      }

  }

    /**
     * A link to a resource representing the person whom the ordered item is for.
     */
    @Child(name = "patient", type = {Patient.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient for whom the item is supplied", formalDefinition="A link to a resource representing the person whom the ordered item is for." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person whom the ordered item is for.)
     */
    protected Patient patientTarget;

    /**
     * The Practitioner , Organization or Patient who initiated this order for the Supply.
     */
    @Child(name = "source", type = {Practitioner.class, Organization.class, Patient.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who initiated this order", formalDefinition="The Practitioner , Organization or Patient who initiated this order for the Supply." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (The Practitioner , Organization or Patient who initiated this order for the Supply.)
     */
    protected Resource sourceTarget;

    /**
     * When the request was made.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the request was made", formalDefinition="When the request was made." )
    protected DateTimeType date;

    /**
     * Unique identifier for this supply request.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier", formalDefinition="Unique identifier for this supply request." )
    protected Identifier identifier;

    /**
     * Status of the supply request.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="requested | completed | failed | cancelled", formalDefinition="Status of the supply request." )
    protected Enumeration<SupplyRequestStatus> status;

    /**
     * Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.
     */
    @Child(name = "kind", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The kind of supply (central, non-stock, etc)", formalDefinition="Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process." )
    protected CodeableConcept kind;

    /**
     * The item that is requested to be supplied.
     */
    @Child(name = "orderedItem", type = {Medication.class, Substance.class, Device.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Medication, Substance, or Device requested to be supplied", formalDefinition="The item that is requested to be supplied." )
    protected Reference orderedItem;

    /**
     * The actual object that is the target of the reference (The item that is requested to be supplied.)
     */
    protected Resource orderedItemTarget;

    /**
     * Who is intended to fulfill the request.
     */
    @Child(name = "supplier", type = {Organization.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who is intended to fulfill the request", formalDefinition="Who is intended to fulfill the request." )
    protected List<Reference> supplier;
    /**
     * The actual objects that are the target of the reference (Who is intended to fulfill the request.)
     */
    protected List<Organization> supplierTarget;


    /**
     * Why the supply item was requested.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Why the supply item was requested", formalDefinition="Why the supply item was requested." )
    protected Type reason;

    /**
     * When the request should be fulfilled.
     */
    @Child(name = "when", type = {}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the request should be fulfilled", formalDefinition="When the request should be fulfilled." )
    protected SupplyRequestWhenComponent when;

    private static final long serialVersionUID = 1649766198L;

  /*
   * Constructor
   */
    public SupplyRequest() {
      super();
    }

    /**
     * @return {@link #patient} (A link to a resource representing the person whom the ordered item is for.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.patient");
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
    public SupplyRequest setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person whom the ordered item is for.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person whom the ordered item is for.)
     */
    public SupplyRequest setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #source} (The Practitioner , Organization or Patient who initiated this order for the Supply.)
     */
    public Reference getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.source");
        else if (Configuration.doAutoCreate())
          this.source = new Reference(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (The Practitioner , Organization or Patient who initiated this order for the Supply.)
     */
    public SupplyRequest setSource(Reference value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Practitioner , Organization or Patient who initiated this order for the Supply.)
     */
    public Resource getSourceTarget() { 
      return this.sourceTarget;
    }

    /**
     * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Practitioner , Organization or Patient who initiated this order for the Supply.)
     */
    public SupplyRequest setSourceTarget(Resource value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (When the request was made.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (When the request was made.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public SupplyRequest setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return When the request was made.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value When the request was made.
     */
    public SupplyRequest setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
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
     * @return {@link #kind} (Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.)
     */
    public CodeableConcept getKind() { 
      if (this.kind == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.kind");
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
    public SupplyRequest setKind(CodeableConcept value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return {@link #orderedItem} (The item that is requested to be supplied.)
     */
    public Reference getOrderedItem() { 
      if (this.orderedItem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.orderedItem");
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
    public SupplyRequest setOrderedItem(Reference value) { 
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
    public SupplyRequest setOrderedItemTarget(Resource value) { 
      this.orderedItemTarget = value;
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

    public boolean hasSupplier() { 
      if (this.supplier == null)
        return false;
      for (Reference item : this.supplier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #supplier} (Who is intended to fulfill the request.)
     */
    // syntactic sugar
    public Reference addSupplier() { //3
      Reference t = new Reference();
      if (this.supplier == null)
        this.supplier = new ArrayList<Reference>();
      this.supplier.add(t);
      return t;
    }

    // syntactic sugar
    public SupplyRequest addSupplier(Reference t) { //3
      if (t == null)
        return this;
      if (this.supplier == null)
        this.supplier = new ArrayList<Reference>();
      this.supplier.add(t);
      return this;
    }

    /**
     * @return {@link #supplier} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Who is intended to fulfill the request.)
     */
    public List<Organization> getSupplierTarget() { 
      if (this.supplierTarget == null)
        this.supplierTarget = new ArrayList<Organization>();
      return this.supplierTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #supplier} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Who is intended to fulfill the request.)
     */
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
    public CodeableConcept getReasonCodeableConcept() throws Exception { 
      if (!(this.reason instanceof CodeableConcept))
        throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (CodeableConcept) this.reason;
    }

    public boolean hasReasonCodeableConcept() throws Exception { 
      return this.reason instanceof CodeableConcept;
    }

    /**
     * @return {@link #reason} (Why the supply item was requested.)
     */
    public Reference getReasonReference() throws Exception { 
      if (!(this.reason instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (Reference) this.reason;
    }

    public boolean hasReasonReference() throws Exception { 
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
     * @return {@link #when} (When the request should be fulfilled.)
     */
    public SupplyRequestWhenComponent getWhen() { 
      if (this.when == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SupplyRequest.when");
        else if (Configuration.doAutoCreate())
          this.when = new SupplyRequestWhenComponent(); // cc
      return this.when;
    }

    public boolean hasWhen() { 
      return this.when != null && !this.when.isEmpty();
    }

    /**
     * @param value {@link #when} (When the request should be fulfilled.)
     */
    public SupplyRequest setWhen(SupplyRequestWhenComponent value) { 
      this.when = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person whom the ordered item is for.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("source", "Reference(Practitioner|Organization|Patient)", "The Practitioner , Organization or Patient who initiated this order for the Supply.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("date", "dateTime", "When the request was made.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("identifier", "Identifier", "Unique identifier for this supply request.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Status of the supply request.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("kind", "CodeableConcept", "Category of supply, e.g.  central, non-stock, etc. This is used to support work flows associated with the supply process.", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("orderedItem", "Reference(Medication|Substance|Device)", "The item that is requested to be supplied.", 0, java.lang.Integer.MAX_VALUE, orderedItem));
        childrenList.add(new Property("supplier", "Reference(Organization)", "Who is intended to fulfill the request.", 0, java.lang.Integer.MAX_VALUE, supplier));
        childrenList.add(new Property("reason[x]", "CodeableConcept|Reference(any)", "Why the supply item was requested.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("when", "", "When the request should be fulfilled.", 0, java.lang.Integer.MAX_VALUE, when));
      }

      public SupplyRequest copy() {
        SupplyRequest dst = new SupplyRequest();
        copyValues(dst);
        dst.patient = patient == null ? null : patient.copy();
        dst.source = source == null ? null : source.copy();
        dst.date = date == null ? null : date.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.kind = kind == null ? null : kind.copy();
        dst.orderedItem = orderedItem == null ? null : orderedItem.copy();
        if (supplier != null) {
          dst.supplier = new ArrayList<Reference>();
          for (Reference i : supplier)
            dst.supplier.add(i.copy());
        };
        dst.reason = reason == null ? null : reason.copy();
        dst.when = when == null ? null : when.copy();
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
        return compareDeep(patient, o.patient, true) && compareDeep(source, o.source, true) && compareDeep(date, o.date, true)
           && compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(kind, o.kind, true)
           && compareDeep(orderedItem, o.orderedItem, true) && compareDeep(supplier, o.supplier, true) && compareDeep(reason, o.reason, true)
           && compareDeep(when, o.when, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SupplyRequest))
          return false;
        SupplyRequest o = (SupplyRequest) other;
        return compareValues(date, o.date, true) && compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (patient == null || patient.isEmpty()) && (source == null || source.isEmpty())
           && (date == null || date.isEmpty()) && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (kind == null || kind.isEmpty()) && (orderedItem == null || orderedItem.isEmpty()) && (supplier == null || supplier.isEmpty())
           && (reason == null || reason.isEmpty()) && (when == null || when.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SupplyRequest;
   }

  @SearchParamDefinition(name="date", path="SupplyRequest.date", description="When the request was made", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="SupplyRequest.identifier", description="Unique identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="kind", path="SupplyRequest.kind", description="The kind of supply (central, non-stock, etc)", type="token" )
  public static final String SP_KIND = "kind";
  @SearchParamDefinition(name="patient", path="SupplyRequest.patient", description="Patient for whom the item is supplied", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="supplier", path="SupplyRequest.supplier", description="Who is intended to fulfill the request", type="reference" )
  public static final String SP_SUPPLIER = "supplier";
  @SearchParamDefinition(name="source", path="SupplyRequest.source", description="Who initiated this order", type="reference" )
  public static final String SP_SOURCE = "source";
  @SearchParamDefinition(name="status", path="SupplyRequest.status", description="requested | completed | failed | cancelled", type="token" )
  public static final String SP_STATUS = "status";

}

