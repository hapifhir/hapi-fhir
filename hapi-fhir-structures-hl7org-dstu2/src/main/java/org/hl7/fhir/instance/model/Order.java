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

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A request to perform an action.
 */
@ResourceDef(name="Order", profile="http://hl7.org/fhir/Profile/Order")
public class Order extends DomainResource {

    @Block()
    public static class OrderWhenComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code specifies when request should be done. The code may simply be a priority code.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Code specifies when request should be done. The code may simply be a priority code", formalDefinition="Code specifies when request should be done. The code may simply be a priority code." )
        protected CodeableConcept code;

        /**
         * A formal schedule.
         */
        @Child(name = "schedule", type = {Timing.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A formal schedule", formalDefinition="A formal schedule." )
        protected Timing schedule;

        private static final long serialVersionUID = 307115287L;

    /*
     * Constructor
     */
      public OrderWhenComponent() {
        super();
      }

        /**
         * @return {@link #code} (Code specifies when request should be done. The code may simply be a priority code.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderWhenComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Code specifies when request should be done. The code may simply be a priority code.)
         */
        public OrderWhenComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #schedule} (A formal schedule.)
         */
        public Timing getSchedule() { 
          if (this.schedule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrderWhenComponent.schedule");
            else if (Configuration.doAutoCreate())
              this.schedule = new Timing(); // cc
          return this.schedule;
        }

        public boolean hasSchedule() { 
          return this.schedule != null && !this.schedule.isEmpty();
        }

        /**
         * @param value {@link #schedule} (A formal schedule.)
         */
        public OrderWhenComponent setSchedule(Timing value) { 
          this.schedule = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "Code specifies when request should be done. The code may simply be a priority code.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("schedule", "Timing", "A formal schedule.", 0, java.lang.Integer.MAX_VALUE, schedule));
        }

      public OrderWhenComponent copy() {
        OrderWhenComponent dst = new OrderWhenComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.schedule = schedule == null ? null : schedule.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OrderWhenComponent))
          return false;
        OrderWhenComponent o = (OrderWhenComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(schedule, o.schedule, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OrderWhenComponent))
          return false;
        OrderWhenComponent o = (OrderWhenComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (schedule == null || schedule.isEmpty())
          ;
      }

  }

    /**
     * Identifiers assigned to this order by the orderer or by the receiver.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifiers assigned to this order by the orderer or by the receiver", formalDefinition="Identifiers assigned to this order by the orderer or by the receiver." )
    protected List<Identifier> identifier;

    /**
     * When the order was made.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the order was made", formalDefinition="When the order was made." )
    protected DateTimeType date;

    /**
     * Patient this order is about.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Device.class, Substance.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient this order is about", formalDefinition="Patient this order is about." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Patient this order is about.)
     */
    protected Resource subjectTarget;

    /**
     * Who initiated the order.
     */
    @Child(name = "source", type = {Practitioner.class, Organization.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who initiated the order", formalDefinition="Who initiated the order." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (Who initiated the order.)
     */
    protected Resource sourceTarget;

    /**
     * Who is intended to fulfill the order.
     */
    @Child(name = "target", type = {Organization.class, Device.class, Practitioner.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who is intended to fulfill the order", formalDefinition="Who is intended to fulfill the order." )
    protected Reference target;

    /**
     * The actual object that is the target of the reference (Who is intended to fulfill the order.)
     */
    protected Resource targetTarget;

    /**
     * Text - why the order was made.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Text - why the order was made", formalDefinition="Text - why the order was made." )
    protected Type reason;

    /**
     * When order should be fulfilled.
     */
    @Child(name = "when", type = {}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When order should be fulfilled", formalDefinition="When order should be fulfilled." )
    protected OrderWhenComponent when;

    /**
     * What action is being ordered.
     */
    @Child(name = "detail", type = {}, order=7, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What action is being ordered", formalDefinition="What action is being ordered." )
    protected List<Reference> detail;
    /**
     * The actual objects that are the target of the reference (What action is being ordered.)
     */
    protected List<Resource> detailTarget;


    private static final long serialVersionUID = -1392311096L;

  /*
   * Constructor
   */
    public Order() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this order by the orderer or by the receiver.)
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
     * @return {@link #identifier} (Identifiers assigned to this order by the orderer or by the receiver.)
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
    public Order addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #date} (When the order was made.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Order.date");
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
     * @param value {@link #date} (When the order was made.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Order setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return When the order was made.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value When the order was made.
     */
    public Order setDate(Date value) { 
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
     * @return {@link #subject} (Patient this order is about.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Order.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Patient this order is about.)
     */
    public Order setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient this order is about.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient this order is about.)
     */
    public Order setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #source} (Who initiated the order.)
     */
    public Reference getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Order.source");
        else if (Configuration.doAutoCreate())
          this.source = new Reference(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (Who initiated the order.)
     */
    public Order setSource(Reference value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who initiated the order.)
     */
    public Resource getSourceTarget() { 
      return this.sourceTarget;
    }

    /**
     * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who initiated the order.)
     */
    public Order setSourceTarget(Resource value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #target} (Who is intended to fulfill the order.)
     */
    public Reference getTarget() { 
      if (this.target == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Order.target");
        else if (Configuration.doAutoCreate())
          this.target = new Reference(); // cc
      return this.target;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (Who is intended to fulfill the order.)
     */
    public Order setTarget(Reference value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who is intended to fulfill the order.)
     */
    public Resource getTargetTarget() { 
      return this.targetTarget;
    }

    /**
     * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who is intended to fulfill the order.)
     */
    public Order setTargetTarget(Resource value) { 
      this.targetTarget = value;
      return this;
    }

    /**
     * @return {@link #reason} (Text - why the order was made.)
     */
    public Type getReason() { 
      return this.reason;
    }

    /**
     * @return {@link #reason} (Text - why the order was made.)
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
     * @return {@link #reason} (Text - why the order was made.)
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
     * @param value {@link #reason} (Text - why the order was made.)
     */
    public Order setReason(Type value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #when} (When order should be fulfilled.)
     */
    public OrderWhenComponent getWhen() { 
      if (this.when == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Order.when");
        else if (Configuration.doAutoCreate())
          this.when = new OrderWhenComponent(); // cc
      return this.when;
    }

    public boolean hasWhen() { 
      return this.when != null && !this.when.isEmpty();
    }

    /**
     * @param value {@link #when} (When order should be fulfilled.)
     */
    public Order setWhen(OrderWhenComponent value) { 
      this.when = value;
      return this;
    }

    /**
     * @return {@link #detail} (What action is being ordered.)
     */
    public List<Reference> getDetail() { 
      if (this.detail == null)
        this.detail = new ArrayList<Reference>();
      return this.detail;
    }

    public boolean hasDetail() { 
      if (this.detail == null)
        return false;
      for (Reference item : this.detail)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #detail} (What action is being ordered.)
     */
    // syntactic sugar
    public Reference addDetail() { //3
      Reference t = new Reference();
      if (this.detail == null)
        this.detail = new ArrayList<Reference>();
      this.detail.add(t);
      return t;
    }

    // syntactic sugar
    public Order addDetail(Reference t) { //3
      if (t == null)
        return this;
      if (this.detail == null)
        this.detail = new ArrayList<Reference>();
      this.detail.add(t);
      return this;
    }

    /**
     * @return {@link #detail} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. What action is being ordered.)
     */
    public List<Resource> getDetailTarget() { 
      if (this.detailTarget == null)
        this.detailTarget = new ArrayList<Resource>();
      return this.detailTarget;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order by the orderer or by the receiver.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("date", "dateTime", "When the order was made.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Substance)", "Patient this order is about.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("source", "Reference(Practitioner|Organization)", "Who initiated the order.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("target", "Reference(Organization|Device|Practitioner)", "Who is intended to fulfill the order.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("reason[x]", "CodeableConcept|Reference(Any)", "Text - why the order was made.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("when", "", "When order should be fulfilled.", 0, java.lang.Integer.MAX_VALUE, when));
        childrenList.add(new Property("detail", "Reference(Any)", "What action is being ordered.", 0, java.lang.Integer.MAX_VALUE, detail));
      }

      public Order copy() {
        Order dst = new Order();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.source = source == null ? null : source.copy();
        dst.target = target == null ? null : target.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.when = when == null ? null : when.copy();
        if (detail != null) {
          dst.detail = new ArrayList<Reference>();
          for (Reference i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      protected Order typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Order))
          return false;
        Order o = (Order) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(date, o.date, true) && compareDeep(subject, o.subject, true)
           && compareDeep(source, o.source, true) && compareDeep(target, o.target, true) && compareDeep(reason, o.reason, true)
           && compareDeep(when, o.when, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Order))
          return false;
        Order o = (Order) other;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (date == null || date.isEmpty())
           && (subject == null || subject.isEmpty()) && (source == null || source.isEmpty()) && (target == null || target.isEmpty())
           && (reason == null || reason.isEmpty()) && (when == null || when.isEmpty()) && (detail == null || detail.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Order;
   }

  @SearchParamDefinition(name="date", path="Order.date", description="When the order was made", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="Order.identifier", description="Instance id from source, target, and/or  others", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="subject", path="Order.subject", description="Patient this order is about", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="patient", path="Order.subject", description="Patient this order is about", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="source", path="Order.source", description="Who initiated the order", type="reference" )
  public static final String SP_SOURCE = "source";
  @SearchParamDefinition(name="detail", path="Order.detail", description="What action is being ordered", type="reference" )
  public static final String SP_DETAIL = "detail";
  @SearchParamDefinition(name="when", path="Order.when.schedule", description="A formal schedule", type="date" )
  public static final String SP_WHEN = "when";
  @SearchParamDefinition(name="target", path="Order.target", description="Who is intended to fulfill the order", type="reference" )
  public static final String SP_TARGET = "target";
  @SearchParamDefinition(name="when_code", path="Order.when.code", description="Code specifies when request should be done. The code may simply be a priority code", type="token" )
  public static final String SP_WHENCODE = "when_code";

}

