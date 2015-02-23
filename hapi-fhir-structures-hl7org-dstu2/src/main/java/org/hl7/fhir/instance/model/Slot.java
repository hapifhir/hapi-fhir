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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A slot of time on a schedule that may be available for booking appointments.
 */
@ResourceDef(name="Slot", profile="http://hl7.org/fhir/Profile/Slot")
public class Slot extends DomainResource {

    public enum Slotstatus {
        /**
         * Indicates that the time interval is busy because one  or more events have been scheduled for that interval.
         */
        BUSY, 
        /**
         * Indicates that the time interval is free for scheduling.
         */
        FREE, 
        /**
         * Indicates that the time interval is busy and that the interval can not be scheduled.
         */
        BUSYUNAVAILABLE, 
        /**
         * Indicates that the time interval is busy because one or more events have been tentatively scheduled for that interval.
         */
        BUSYTENTATIVE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Slotstatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("BUSY".equals(codeString))
          return BUSY;
        if ("FREE".equals(codeString))
          return FREE;
        if ("BUSY-UNAVAILABLE".equals(codeString))
          return BUSYUNAVAILABLE;
        if ("BUSY-TENTATIVE".equals(codeString))
          return BUSYTENTATIVE;
        throw new Exception("Unknown Slotstatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BUSY: return "BUSY";
            case FREE: return "FREE";
            case BUSYUNAVAILABLE: return "BUSY-UNAVAILABLE";
            case BUSYTENTATIVE: return "BUSY-TENTATIVE";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case BUSY: return "";
            case FREE: return "";
            case BUSYUNAVAILABLE: return "";
            case BUSYTENTATIVE: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case BUSY: return "Indicates that the time interval is busy because one  or more events have been scheduled for that interval.";
            case FREE: return "Indicates that the time interval is free for scheduling.";
            case BUSYUNAVAILABLE: return "Indicates that the time interval is busy and that the interval can not be scheduled.";
            case BUSYTENTATIVE: return "Indicates that the time interval is busy because one or more events have been tentatively scheduled for that interval.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BUSY: return "BUSY";
            case FREE: return "FREE";
            case BUSYUNAVAILABLE: return "BUSY-UNAVAILABLE";
            case BUSYTENTATIVE: return "BUSY-TENTATIVE";
            default: return "?";
          }
        }
    }

  public static class SlotstatusEnumFactory implements EnumFactory<Slotstatus> {
    public Slotstatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("BUSY".equals(codeString))
          return Slotstatus.BUSY;
        if ("FREE".equals(codeString))
          return Slotstatus.FREE;
        if ("BUSY-UNAVAILABLE".equals(codeString))
          return Slotstatus.BUSYUNAVAILABLE;
        if ("BUSY-TENTATIVE".equals(codeString))
          return Slotstatus.BUSYTENTATIVE;
        throw new IllegalArgumentException("Unknown Slotstatus code '"+codeString+"'");
        }
    public String toCode(Slotstatus code) {
      if (code == Slotstatus.BUSY)
        return "BUSY";
      if (code == Slotstatus.FREE)
        return "FREE";
      if (code == Slotstatus.BUSYUNAVAILABLE)
        return "BUSY-UNAVAILABLE";
      if (code == Slotstatus.BUSYTENTATIVE)
        return "BUSY-TENTATIVE";
      return "?";
      }
    }

    /**
     * External Ids for this item.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this item", formalDefinition="External Ids for this item." )
    protected List<Identifier> identifier;

    /**
     * The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is at a location, rather than the location itself). If provided then this overrides the value provided on the availability resource.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is at a location, rather than the location itself). If provided then this overrides the value provided on the availability resource", formalDefinition="The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is at a location, rather than the location itself). If provided then this overrides the value provided on the availability resource." )
    protected CodeableConcept type;

    /**
     * The schedule resource that this slot defines an interval of status information.
     */
    @Child(name = "schedule", type = {Schedule.class}, order = 2, min = 1, max = 1)
    @Description(shortDefinition="The schedule resource that this slot defines an interval of status information", formalDefinition="The schedule resource that this slot defines an interval of status information." )
    protected Reference schedule;

    /**
     * The actual object that is the target of the reference (The schedule resource that this slot defines an interval of status information.)
     */
    protected Schedule scheduleTarget;

    /**
     * BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE.
     */
    @Child(name = "freeBusyType", type = {CodeType.class}, order = 3, min = 1, max = 1)
    @Description(shortDefinition="BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE", formalDefinition="BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE." )
    protected Enumeration<Slotstatus> freeBusyType;

    /**
     * Date/Time that the slot is to begin.
     */
    @Child(name = "start", type = {InstantType.class}, order = 4, min = 1, max = 1)
    @Description(shortDefinition="Date/Time that the slot is to begin", formalDefinition="Date/Time that the slot is to begin." )
    protected InstantType start;

    /**
     * Date/Time that the slot is to conclude.
     */
    @Child(name = "end", type = {InstantType.class}, order = 5, min = 1, max = 1)
    @Description(shortDefinition="Date/Time that the slot is to conclude", formalDefinition="Date/Time that the slot is to conclude." )
    protected InstantType end;

    /**
     * This slot has already been overbooked, appointments are unlikely to be accepted for this time.
     */
    @Child(name = "overbooked", type = {BooleanType.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="This slot has already been overbooked, appointments are unlikely to be accepted for this time", formalDefinition="This slot has already been overbooked, appointments are unlikely to be accepted for this time." )
    protected BooleanType overbooked;

    /**
     * Comments on the slot to describe any extended information. Such as custom constraints on the slot.
     */
    @Child(name = "comment", type = {StringType.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="Comments on the slot to describe any extended information. Such as custom constraints on the slot", formalDefinition="Comments on the slot to describe any extended information. Such as custom constraints on the slot." )
    protected StringType comment;

    /**
     * When this slot was created, or last revised.
     */
    @Child(name = "lastModified", type = {DateTimeType.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="When this slot was created, or last revised", formalDefinition="When this slot was created, or last revised." )
    protected DateTimeType lastModified;

    private static final long serialVersionUID = 738744883L;

    public Slot() {
      super();
    }

    public Slot(Reference schedule, Enumeration<Slotstatus> freeBusyType, InstantType start, InstantType end) {
      super();
      this.schedule = schedule;
      this.freeBusyType = freeBusyType;
      this.start = start;
      this.end = end;
    }

    /**
     * @return {@link #identifier} (External Ids for this item.)
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
     * @return {@link #identifier} (External Ids for this item.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #type} (The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is at a location, rather than the location itself). If provided then this overrides the value provided on the availability resource.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Slot.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is at a location, rather than the location itself). If provided then this overrides the value provided on the availability resource.)
     */
    public Slot setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #schedule} (The schedule resource that this slot defines an interval of status information.)
     */
    public Reference getSchedule() { 
      if (this.schedule == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Slot.schedule");
        else if (Configuration.doAutoCreate())
          this.schedule = new Reference(); // cc
      return this.schedule;
    }

    public boolean hasSchedule() { 
      return this.schedule != null && !this.schedule.isEmpty();
    }

    /**
     * @param value {@link #schedule} (The schedule resource that this slot defines an interval of status information.)
     */
    public Slot setSchedule(Reference value) { 
      this.schedule = value;
      return this;
    }

    /**
     * @return {@link #schedule} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The schedule resource that this slot defines an interval of status information.)
     */
    public Schedule getScheduleTarget() { 
      if (this.scheduleTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Slot.schedule");
        else if (Configuration.doAutoCreate())
          this.scheduleTarget = new Schedule(); // aa
      return this.scheduleTarget;
    }

    /**
     * @param value {@link #schedule} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The schedule resource that this slot defines an interval of status information.)
     */
    public Slot setScheduleTarget(Schedule value) { 
      this.scheduleTarget = value;
      return this;
    }

    /**
     * @return {@link #freeBusyType} (BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE.). This is the underlying object with id, value and extensions. The accessor "getFreeBusyType" gives direct access to the value
     */
    public Enumeration<Slotstatus> getFreeBusyTypeElement() { 
      if (this.freeBusyType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Slot.freeBusyType");
        else if (Configuration.doAutoCreate())
          this.freeBusyType = new Enumeration<Slotstatus>(new SlotstatusEnumFactory()); // bb
      return this.freeBusyType;
    }

    public boolean hasFreeBusyTypeElement() { 
      return this.freeBusyType != null && !this.freeBusyType.isEmpty();
    }

    public boolean hasFreeBusyType() { 
      return this.freeBusyType != null && !this.freeBusyType.isEmpty();
    }

    /**
     * @param value {@link #freeBusyType} (BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE.). This is the underlying object with id, value and extensions. The accessor "getFreeBusyType" gives direct access to the value
     */
    public Slot setFreeBusyTypeElement(Enumeration<Slotstatus> value) { 
      this.freeBusyType = value;
      return this;
    }

    /**
     * @return BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE.
     */
    public Slotstatus getFreeBusyType() { 
      return this.freeBusyType == null ? null : this.freeBusyType.getValue();
    }

    /**
     * @param value BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE.
     */
    public Slot setFreeBusyType(Slotstatus value) { 
        if (this.freeBusyType == null)
          this.freeBusyType = new Enumeration<Slotstatus>(new SlotstatusEnumFactory());
        this.freeBusyType.setValue(value);
      return this;
    }

    /**
     * @return {@link #start} (Date/Time that the slot is to begin.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public InstantType getStartElement() { 
      if (this.start == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Slot.start");
        else if (Configuration.doAutoCreate())
          this.start = new InstantType(); // bb
      return this.start;
    }

    public boolean hasStartElement() { 
      return this.start != null && !this.start.isEmpty();
    }

    public boolean hasStart() { 
      return this.start != null && !this.start.isEmpty();
    }

    /**
     * @param value {@link #start} (Date/Time that the slot is to begin.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public Slot setStartElement(InstantType value) { 
      this.start = value;
      return this;
    }

    /**
     * @return Date/Time that the slot is to begin.
     */
    public Date getStart() { 
      return this.start == null ? null : this.start.getValue();
    }

    /**
     * @param value Date/Time that the slot is to begin.
     */
    public Slot setStart(Date value) { 
        if (this.start == null)
          this.start = new InstantType();
        this.start.setValue(value);
      return this;
    }

    /**
     * @return {@link #end} (Date/Time that the slot is to conclude.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public InstantType getEndElement() { 
      if (this.end == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Slot.end");
        else if (Configuration.doAutoCreate())
          this.end = new InstantType(); // bb
      return this.end;
    }

    public boolean hasEndElement() { 
      return this.end != null && !this.end.isEmpty();
    }

    public boolean hasEnd() { 
      return this.end != null && !this.end.isEmpty();
    }

    /**
     * @param value {@link #end} (Date/Time that the slot is to conclude.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public Slot setEndElement(InstantType value) { 
      this.end = value;
      return this;
    }

    /**
     * @return Date/Time that the slot is to conclude.
     */
    public Date getEnd() { 
      return this.end == null ? null : this.end.getValue();
    }

    /**
     * @param value Date/Time that the slot is to conclude.
     */
    public Slot setEnd(Date value) { 
        if (this.end == null)
          this.end = new InstantType();
        this.end.setValue(value);
      return this;
    }

    /**
     * @return {@link #overbooked} (This slot has already been overbooked, appointments are unlikely to be accepted for this time.). This is the underlying object with id, value and extensions. The accessor "getOverbooked" gives direct access to the value
     */
    public BooleanType getOverbookedElement() { 
      if (this.overbooked == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Slot.overbooked");
        else if (Configuration.doAutoCreate())
          this.overbooked = new BooleanType(); // bb
      return this.overbooked;
    }

    public boolean hasOverbookedElement() { 
      return this.overbooked != null && !this.overbooked.isEmpty();
    }

    public boolean hasOverbooked() { 
      return this.overbooked != null && !this.overbooked.isEmpty();
    }

    /**
     * @param value {@link #overbooked} (This slot has already been overbooked, appointments are unlikely to be accepted for this time.). This is the underlying object with id, value and extensions. The accessor "getOverbooked" gives direct access to the value
     */
    public Slot setOverbookedElement(BooleanType value) { 
      this.overbooked = value;
      return this;
    }

    /**
     * @return This slot has already been overbooked, appointments are unlikely to be accepted for this time.
     */
    public boolean getOverbooked() { 
      return this.overbooked == null ? false : this.overbooked.getValue();
    }

    /**
     * @param value This slot has already been overbooked, appointments are unlikely to be accepted for this time.
     */
    public Slot setOverbooked(boolean value) { 
        if (this.overbooked == null)
          this.overbooked = new BooleanType();
        this.overbooked.setValue(value);
      return this;
    }

    /**
     * @return {@link #comment} (Comments on the slot to describe any extended information. Such as custom constraints on the slot.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Slot.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new StringType(); // bb
      return this.comment;
    }

    public boolean hasCommentElement() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    public boolean hasComment() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    /**
     * @param value {@link #comment} (Comments on the slot to describe any extended information. Such as custom constraints on the slot.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public Slot setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Comments on the slot to describe any extended information. Such as custom constraints on the slot.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Comments on the slot to describe any extended information. Such as custom constraints on the slot.
     */
    public Slot setComment(String value) { 
      if (Utilities.noString(value))
        this.comment = null;
      else {
        if (this.comment == null)
          this.comment = new StringType();
        this.comment.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lastModified} (When this slot was created, or last revised.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public DateTimeType getLastModifiedElement() { 
      if (this.lastModified == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Slot.lastModified");
        else if (Configuration.doAutoCreate())
          this.lastModified = new DateTimeType(); // bb
      return this.lastModified;
    }

    public boolean hasLastModifiedElement() { 
      return this.lastModified != null && !this.lastModified.isEmpty();
    }

    public boolean hasLastModified() { 
      return this.lastModified != null && !this.lastModified.isEmpty();
    }

    /**
     * @param value {@link #lastModified} (When this slot was created, or last revised.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public Slot setLastModifiedElement(DateTimeType value) { 
      this.lastModified = value;
      return this;
    }

    /**
     * @return When this slot was created, or last revised.
     */
    public Date getLastModified() { 
      return this.lastModified == null ? null : this.lastModified.getValue();
    }

    /**
     * @param value When this slot was created, or last revised.
     */
    public Slot setLastModified(Date value) { 
      if (value == null)
        this.lastModified = null;
      else {
        if (this.lastModified == null)
          this.lastModified = new DateTimeType();
        this.lastModified.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External Ids for this item.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "CodeableConcept", "The type of appointments that can be booked into this slot (ideally this would be an identifiable service - which is at a location, rather than the location itself). If provided then this overrides the value provided on the availability resource.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("schedule", "Reference(Schedule)", "The schedule resource that this slot defines an interval of status information.", 0, java.lang.Integer.MAX_VALUE, schedule));
        childrenList.add(new Property("freeBusyType", "code", "BUSY | FREE | BUSY-UNAVAILABLE | BUSY-TENTATIVE.", 0, java.lang.Integer.MAX_VALUE, freeBusyType));
        childrenList.add(new Property("start", "instant", "Date/Time that the slot is to begin.", 0, java.lang.Integer.MAX_VALUE, start));
        childrenList.add(new Property("end", "instant", "Date/Time that the slot is to conclude.", 0, java.lang.Integer.MAX_VALUE, end));
        childrenList.add(new Property("overbooked", "boolean", "This slot has already been overbooked, appointments are unlikely to be accepted for this time.", 0, java.lang.Integer.MAX_VALUE, overbooked));
        childrenList.add(new Property("comment", "string", "Comments on the slot to describe any extended information. Such as custom constraints on the slot.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("lastModified", "dateTime", "When this slot was created, or last revised.", 0, java.lang.Integer.MAX_VALUE, lastModified));
      }

      public Slot copy() {
        Slot dst = new Slot();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.schedule = schedule == null ? null : schedule.copy();
        dst.freeBusyType = freeBusyType == null ? null : freeBusyType.copy();
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        dst.overbooked = overbooked == null ? null : overbooked.copy();
        dst.comment = comment == null ? null : comment.copy();
        dst.lastModified = lastModified == null ? null : lastModified.copy();
        return dst;
      }

      protected Slot typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Slot))
          return false;
        Slot o = (Slot) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(schedule, o.schedule, true)
           && compareDeep(freeBusyType, o.freeBusyType, true) && compareDeep(start, o.start, true) && compareDeep(end, o.end, true)
           && compareDeep(overbooked, o.overbooked, true) && compareDeep(comment, o.comment, true) && compareDeep(lastModified, o.lastModified, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Slot))
          return false;
        Slot o = (Slot) other;
        return compareValues(freeBusyType, o.freeBusyType, true) && compareValues(start, o.start, true) && compareValues(end, o.end, true)
           && compareValues(overbooked, o.overbooked, true) && compareValues(comment, o.comment, true) && compareValues(lastModified, o.lastModified, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (schedule == null || schedule.isEmpty()) && (freeBusyType == null || freeBusyType.isEmpty())
           && (start == null || start.isEmpty()) && (end == null || end.isEmpty()) && (overbooked == null || overbooked.isEmpty())
           && (comment == null || comment.isEmpty()) && (lastModified == null || lastModified.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Slot;
   }

  @SearchParamDefinition(name="schedule", path="Slot.schedule", description="The Schedule Resource that we are seeking a slot within", type="reference" )
  public static final String SP_SCHEDULE = "schedule";
  @SearchParamDefinition(name="slottype", path="Slot.type", description="The type of appointments that can be booked into the slot", type="token" )
  public static final String SP_SLOTTYPE = "slottype";
  @SearchParamDefinition(name = "start", path = "Slot.start", description = "Appointment date/time.", type = "date")
  public static final String SP_START = "start";
  @SearchParamDefinition(name="fbtype", path="Slot.freeBusyType", description="The free/busy status of the appointment", type="token" )
  public static final String SP_FBTYPE = "fbtype";

}

