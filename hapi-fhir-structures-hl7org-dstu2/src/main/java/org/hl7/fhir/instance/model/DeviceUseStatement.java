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
import ca.uhn.fhir.parser.DataFormatException;
/**
 * A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.
 */
@ResourceDef(name="DeviceUseStatement", profile="http://hl7.org/fhir/Profile/DeviceUseStatement")
public class DeviceUseStatement extends DomainResource {

    /**
     * Indicates the site on the subject's body where the device was used ( i.e. the target site).
     */
    @Child(name = "bodySite", type = {CodeableConcept.class, BodySite.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Target body site", formalDefinition="Indicates the site on the subject's body where the device was used ( i.e. the target site)." )
    protected Type bodySite;

    /**
     * The time period over which the device was used.
     */
    @Child(name = "whenUsed", type = {Period.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="The time period over which the device was used." )
    protected Period whenUsed;

    /**
     * The details of the device used.
     */
    @Child(name = "device", type = {Device.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="The details of the device used." )
    protected Reference device;

    /**
     * The actual object that is the target of the reference (The details of the device used.)
     */
    protected Device deviceTarget;

    /**
     * An external identifier for this statement such as an IRI.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="An external identifier for this statement such as an IRI." )
    protected List<Identifier> identifier;

    /**
     * Reason or justification for the use of the device.
     */
    @Child(name = "indication", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="Reason or justification for the use of the device." )
    protected List<CodeableConcept> indication;

    /**
     * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.
     */
    @Child(name = "notes", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement." )
    protected List<StringType> notes;

    /**
     * The time at which the statement was made/recorded.
     */
    @Child(name = "recordedOn", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="The time at which the statement was made/recorded." )
    protected DateTimeType recordedOn;

    /**
     * The patient who used the device.
     */
    @Child(name = "subject", type = {Patient.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="The patient who used the device." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who used the device.)
     */
    protected Patient subjectTarget;

    /**
     * How often the device was used.
     */
    @Child(name = "timing", type = {Timing.class, Period.class, DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="How often the device was used." )
    protected Type timing;

    private static final long serialVersionUID = -1668571635L;

  /*
   * Constructor
   */
    public DeviceUseStatement() {
      super();
    }

  /*
   * Constructor
   */
    public DeviceUseStatement(Reference device, Reference subject) {
      super();
      this.device = device;
      this.subject = subject;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the device was used ( i.e. the target site).)
     */
    public Type getBodySite() { 
      return this.bodySite;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the device was used ( i.e. the target site).)
     */
    public CodeableConcept getBodySiteCodeableConcept() { 
      if (!(this.bodySite instanceof CodeableConcept))
        throw new DataFormatException("Type mismatch: the type CodeableConcept was expected, but "+this.bodySite.getClass().getName()+" was encountered");
      return (CodeableConcept) this.bodySite;
    }

    public boolean hasBodySiteCodeableConcept() { 
      return this.bodySite instanceof CodeableConcept;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the device was used ( i.e. the target site).)
     */
    public Reference getBodySiteReference() throws Exception { 
      if (!(this.bodySite instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.bodySite.getClass().getName()+" was encountered");
      return (Reference) this.bodySite;
    }

    public boolean hasBodySiteReference() throws Exception { 
      return this.bodySite instanceof Reference;
    }

    public boolean hasBodySite() { 
      return this.bodySite != null && !this.bodySite.isEmpty();
    }

    /**
     * @param value {@link #bodySite} (Indicates the site on the subject's body where the device was used ( i.e. the target site).)
     */
    public DeviceUseStatement setBodySite(Type value) { 
      this.bodySite = value;
      return this;
    }

    /**
     * @return {@link #whenUsed} (The time period over which the device was used.)
     */
    public Period getWhenUsed() { 
      if (this.whenUsed == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseStatement.whenUsed");
        else if (Configuration.doAutoCreate())
          this.whenUsed = new Period(); // cc
      return this.whenUsed;
    }

    public boolean hasWhenUsed() { 
      return this.whenUsed != null && !this.whenUsed.isEmpty();
    }

    /**
     * @param value {@link #whenUsed} (The time period over which the device was used.)
     */
    public DeviceUseStatement setWhenUsed(Period value) { 
      this.whenUsed = value;
      return this;
    }

    /**
     * @return {@link #device} (The details of the device used.)
     */
    public Reference getDevice() { 
      if (this.device == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseStatement.device");
        else if (Configuration.doAutoCreate())
          this.device = new Reference(); // cc
      return this.device;
    }

    public boolean hasDevice() { 
      return this.device != null && !this.device.isEmpty();
    }

    /**
     * @param value {@link #device} (The details of the device used.)
     */
    public DeviceUseStatement setDevice(Reference value) { 
      this.device = value;
      return this;
    }

    /**
     * @return {@link #device} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The details of the device used.)
     */
    public Device getDeviceTarget() { 
      if (this.deviceTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseStatement.device");
        else if (Configuration.doAutoCreate())
          this.deviceTarget = new Device(); // aa
      return this.deviceTarget;
    }

    /**
     * @param value {@link #device} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The details of the device used.)
     */
    public DeviceUseStatement setDeviceTarget(Device value) { 
      this.deviceTarget = value;
      return this;
    }

    /**
     * @return {@link #identifier} (An external identifier for this statement such as an IRI.)
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
     * @return {@link #identifier} (An external identifier for this statement such as an IRI.)
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
    public DeviceUseStatement addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #indication} (Reason or justification for the use of the device.)
     */
    public List<CodeableConcept> getIndication() { 
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      return this.indication;
    }

    public boolean hasIndication() { 
      if (this.indication == null)
        return false;
      for (CodeableConcept item : this.indication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #indication} (Reason or justification for the use of the device.)
     */
    // syntactic sugar
    public CodeableConcept addIndication() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return t;
    }

    // syntactic sugar
    public DeviceUseStatement addIndication(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return this;
    }

    /**
     * @return {@link #notes} (Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
     */
    public List<StringType> getNotes() { 
      if (this.notes == null)
        this.notes = new ArrayList<StringType>();
      return this.notes;
    }

    public boolean hasNotes() { 
      if (this.notes == null)
        return false;
      for (StringType item : this.notes)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #notes} (Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
     */
    // syntactic sugar
    public StringType addNotesElement() {//2 
      StringType t = new StringType();
      if (this.notes == null)
        this.notes = new ArrayList<StringType>();
      this.notes.add(t);
      return t;
    }

    /**
     * @param value {@link #notes} (Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
     */
    public DeviceUseStatement addNotes(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.notes == null)
        this.notes = new ArrayList<StringType>();
      this.notes.add(t);
      return this;
    }

    /**
     * @param value {@link #notes} (Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
     */
    public boolean hasNotes(String value) { 
      if (this.notes == null)
        return false;
      for (StringType v : this.notes)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #recordedOn} (The time at which the statement was made/recorded.). This is the underlying object with id, value and extensions. The accessor "getRecordedOn" gives direct access to the value
     */
    public DateTimeType getRecordedOnElement() { 
      if (this.recordedOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseStatement.recordedOn");
        else if (Configuration.doAutoCreate())
          this.recordedOn = new DateTimeType(); // bb
      return this.recordedOn;
    }

    public boolean hasRecordedOnElement() { 
      return this.recordedOn != null && !this.recordedOn.isEmpty();
    }

    public boolean hasRecordedOn() { 
      return this.recordedOn != null && !this.recordedOn.isEmpty();
    }

    /**
     * @param value {@link #recordedOn} (The time at which the statement was made/recorded.). This is the underlying object with id, value and extensions. The accessor "getRecordedOn" gives direct access to the value
     */
    public DeviceUseStatement setRecordedOnElement(DateTimeType value) { 
      this.recordedOn = value;
      return this;
    }

    /**
     * @return The time at which the statement was made/recorded.
     */
    public Date getRecordedOn() { 
      return this.recordedOn == null ? null : this.recordedOn.getValue();
    }

    /**
     * @param value The time at which the statement was made/recorded.
     */
    public DeviceUseStatement setRecordedOn(Date value) { 
      if (value == null)
        this.recordedOn = null;
      else {
        if (this.recordedOn == null)
          this.recordedOn = new DateTimeType();
        this.recordedOn.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #subject} (The patient who used the device.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseStatement.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient who used the device.)
     */
    public DeviceUseStatement setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who used the device.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseStatement.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient(); // aa
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who used the device.)
     */
    public DeviceUseStatement setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #timing} (How often the device was used.)
     */
    public Type getTiming() { 
      return this.timing;
    }

    /**
     * @return {@link #timing} (How often the device was used.)
     */
    public Timing getTimingTiming() throws Exception { 
      if (!(this.timing instanceof Timing))
        throw new Exception("Type mismatch: the type Timing was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Timing) this.timing;
    }

    public boolean hasTimingTiming() throws Exception { 
      return this.timing instanceof Timing;
    }

    /**
     * @return {@link #timing} (How often the device was used.)
     */
    public Period getTimingPeriod() throws Exception { 
      if (!(this.timing instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Period) this.timing;
    }

    public boolean hasTimingPeriod() throws Exception { 
      return this.timing instanceof Period;
    }

    /**
     * @return {@link #timing} (How often the device was used.)
     */
    public DateTimeType getTimingDateTimeType() throws Exception { 
      if (!(this.timing instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (DateTimeType) this.timing;
    }

    public boolean hasTimingDateTimeType() throws Exception { 
      return this.timing instanceof DateTimeType;
    }

    public boolean hasTiming() { 
      return this.timing != null && !this.timing.isEmpty();
    }

    /**
     * @param value {@link #timing} (How often the device was used.)
     */
    public DeviceUseStatement setTiming(Type value) { 
      this.timing = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("bodySite[x]", "CodeableConcept|Reference(BodySite)", "Indicates the site on the subject's body where the device was used ( i.e. the target site).", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("whenUsed", "Period", "The time period over which the device was used.", 0, java.lang.Integer.MAX_VALUE, whenUsed));
        childrenList.add(new Property("device", "Reference(Device)", "The details of the device used.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("identifier", "Identifier", "An external identifier for this statement such as an IRI.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("indication", "CodeableConcept", "Reason or justification for the use of the device.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("notes", "string", "Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("recordedOn", "dateTime", "The time at which the statement was made/recorded.", 0, java.lang.Integer.MAX_VALUE, recordedOn));
        childrenList.add(new Property("subject", "Reference(Patient)", "The patient who used the device.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("timing[x]", "Timing|Period|dateTime", "How often the device was used.", 0, java.lang.Integer.MAX_VALUE, timing));
      }

      public DeviceUseStatement copy() {
        DeviceUseStatement dst = new DeviceUseStatement();
        copyValues(dst);
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        dst.whenUsed = whenUsed == null ? null : whenUsed.copy();
        dst.device = device == null ? null : device.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (indication != null) {
          dst.indication = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : indication)
            dst.indication.add(i.copy());
        };
        if (notes != null) {
          dst.notes = new ArrayList<StringType>();
          for (StringType i : notes)
            dst.notes.add(i.copy());
        };
        dst.recordedOn = recordedOn == null ? null : recordedOn.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.timing = timing == null ? null : timing.copy();
        return dst;
      }

      protected DeviceUseStatement typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DeviceUseStatement))
          return false;
        DeviceUseStatement o = (DeviceUseStatement) other;
        return compareDeep(bodySite, o.bodySite, true) && compareDeep(whenUsed, o.whenUsed, true) && compareDeep(device, o.device, true)
           && compareDeep(identifier, o.identifier, true) && compareDeep(indication, o.indication, true) && compareDeep(notes, o.notes, true)
           && compareDeep(recordedOn, o.recordedOn, true) && compareDeep(subject, o.subject, true) && compareDeep(timing, o.timing, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DeviceUseStatement))
          return false;
        DeviceUseStatement o = (DeviceUseStatement) other;
        return compareValues(notes, o.notes, true) && compareValues(recordedOn, o.recordedOn, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (bodySite == null || bodySite.isEmpty()) && (whenUsed == null || whenUsed.isEmpty())
           && (device == null || device.isEmpty()) && (identifier == null || identifier.isEmpty()) && (indication == null || indication.isEmpty())
           && (notes == null || notes.isEmpty()) && (recordedOn == null || recordedOn.isEmpty()) && (subject == null || subject.isEmpty())
           && (timing == null || timing.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DeviceUseStatement;
   }

  @SearchParamDefinition(name="subject", path="DeviceUseStatement.subject", description="Search by subject", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="patient", path="DeviceUseStatement.subject", description="Search by subject - a patient", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="device", path="DeviceUseStatement.device", description="Search by device", type="reference" )
  public static final String SP_DEVICE = "device";

}

