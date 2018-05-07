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
 * A record of a device being used by a patient where the record is the result of a report from the patient or another clinician.
 */
@ResourceDef(name="DeviceUseStatement", profile="http://hl7.org/fhir/Profile/DeviceUseStatement")
public class DeviceUseStatement extends DomainResource {

    public enum DeviceUseStatementStatus {
        /**
         * The device is still being used.
         */
        ACTIVE, 
        /**
         * The device is no longer being used.
         */
        COMPLETED, 
        /**
         * The statement was recorded incorrectly.
         */
        ENTEREDINERROR, 
        /**
         * The device may be used at some time in the future.
         */
        INTENDED, 
        /**
         * Actions implied by the statement have been permanently halted, before all of them occurred.
         */
        STOPPED, 
        /**
         * Actions implied by the statement have been temporarily halted, but are expected to continue later. May also be called "suspended".
         */
        ONHOLD, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DeviceUseStatementStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("intended".equals(codeString))
          return INTENDED;
        if ("stopped".equals(codeString))
          return STOPPED;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DeviceUseStatementStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case INTENDED: return "intended";
            case STOPPED: return "stopped";
            case ONHOLD: return "on-hold";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/device-statement-status";
            case COMPLETED: return "http://hl7.org/fhir/device-statement-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/device-statement-status";
            case INTENDED: return "http://hl7.org/fhir/device-statement-status";
            case STOPPED: return "http://hl7.org/fhir/device-statement-status";
            case ONHOLD: return "http://hl7.org/fhir/device-statement-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The device is still being used.";
            case COMPLETED: return "The device is no longer being used.";
            case ENTEREDINERROR: return "The statement was recorded incorrectly.";
            case INTENDED: return "The device may be used at some time in the future.";
            case STOPPED: return "Actions implied by the statement have been permanently halted, before all of them occurred.";
            case ONHOLD: return "Actions implied by the statement have been temporarily halted, but are expected to continue later. May also be called \"suspended\".";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case INTENDED: return "Intended";
            case STOPPED: return "Stopped";
            case ONHOLD: return "On Hold";
            default: return "?";
          }
        }
    }

  public static class DeviceUseStatementStatusEnumFactory implements EnumFactory<DeviceUseStatementStatus> {
    public DeviceUseStatementStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return DeviceUseStatementStatus.ACTIVE;
        if ("completed".equals(codeString))
          return DeviceUseStatementStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return DeviceUseStatementStatus.ENTEREDINERROR;
        if ("intended".equals(codeString))
          return DeviceUseStatementStatus.INTENDED;
        if ("stopped".equals(codeString))
          return DeviceUseStatementStatus.STOPPED;
        if ("on-hold".equals(codeString))
          return DeviceUseStatementStatus.ONHOLD;
        throw new IllegalArgumentException("Unknown DeviceUseStatementStatus code '"+codeString+"'");
        }
        public Enumeration<DeviceUseStatementStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DeviceUseStatementStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<DeviceUseStatementStatus>(this, DeviceUseStatementStatus.ACTIVE);
        if ("completed".equals(codeString))
          return new Enumeration<DeviceUseStatementStatus>(this, DeviceUseStatementStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<DeviceUseStatementStatus>(this, DeviceUseStatementStatus.ENTEREDINERROR);
        if ("intended".equals(codeString))
          return new Enumeration<DeviceUseStatementStatus>(this, DeviceUseStatementStatus.INTENDED);
        if ("stopped".equals(codeString))
          return new Enumeration<DeviceUseStatementStatus>(this, DeviceUseStatementStatus.STOPPED);
        if ("on-hold".equals(codeString))
          return new Enumeration<DeviceUseStatementStatus>(this, DeviceUseStatementStatus.ONHOLD);
        throw new FHIRException("Unknown DeviceUseStatementStatus code '"+codeString+"'");
        }
    public String toCode(DeviceUseStatementStatus code) {
      if (code == DeviceUseStatementStatus.ACTIVE)
        return "active";
      if (code == DeviceUseStatementStatus.COMPLETED)
        return "completed";
      if (code == DeviceUseStatementStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == DeviceUseStatementStatus.INTENDED)
        return "intended";
      if (code == DeviceUseStatementStatus.STOPPED)
        return "stopped";
      if (code == DeviceUseStatementStatus.ONHOLD)
        return "on-hold";
      return "?";
      }
    public String toSystem(DeviceUseStatementStatus code) {
      return code.getSystem();
      }
    }

    /**
     * An external identifier for this statement such as an IRI.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External identifier for this record", formalDefinition="An external identifier for this statement such as an IRI." )
    protected List<Identifier> identifier;

    /**
     * A code representing the patient or other source's judgment about the state of the device used that this statement is about.  Generally this will be active or completed.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | completed | entered-in-error +", formalDefinition="A code representing the patient or other source's judgment about the state of the device used that this statement is about.  Generally this will be active or completed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-statement-status")
    protected Enumeration<DeviceUseStatementStatus> status;

    /**
     * The patient who used the device.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=2, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Patient using device", formalDefinition="The patient who used the device." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who used the device.)
     */
    protected Resource subjectTarget;

    /**
     * The time period over which the device was used.
     */
    @Child(name = "whenUsed", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Period device was used", formalDefinition="The time period over which the device was used." )
    protected Period whenUsed;

    /**
     * How often the device was used.
     */
    @Child(name = "timing", type = {Timing.class, Period.class, DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How often  the device was used", formalDefinition="How often the device was used." )
    protected Type timing;

    /**
     * The time at which the statement was made/recorded.
     */
    @Child(name = "recordedOn", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When statement was recorded", formalDefinition="The time at which the statement was made/recorded." )
    protected DateTimeType recordedOn;

    /**
     * Who reported the device was being used by the patient.
     */
    @Child(name = "source", type = {Patient.class, Practitioner.class, RelatedPerson.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Who made the statement", formalDefinition="Who reported the device was being used by the patient." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (Who reported the device was being used by the patient.)
     */
    protected Resource sourceTarget;

    /**
     * The details of the device used.
     */
    @Child(name = "device", type = {Device.class}, order=7, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reference to device used", formalDefinition="The details of the device used." )
    protected Reference device;

    /**
     * The actual object that is the target of the reference (The details of the device used.)
     */
    protected Device deviceTarget;

    /**
     * Reason or justification for the use of the device.
     */
    @Child(name = "indication", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Why device was used", formalDefinition="Reason or justification for the use of the device." )
    protected List<CodeableConcept> indication;

    /**
     * Indicates the site on the subject's body where the device was used ( i.e. the target site).
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Target body site", formalDefinition="Indicates the site on the subject's body where the device was used ( i.e. the target site)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected CodeableConcept bodySite;

    /**
     * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.
     */
    @Child(name = "note", type = {Annotation.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Addition details (comments, instructions)", formalDefinition="Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement." )
    protected List<Annotation> note;

    private static final long serialVersionUID = 2144163845L;

  /**
   * Constructor
   */
    public DeviceUseStatement() {
      super();
    }

  /**
   * Constructor
   */
    public DeviceUseStatement(Enumeration<DeviceUseStatementStatus> status, Reference subject, Reference device) {
      super();
      this.status = status;
      this.subject = subject;
      this.device = device;
    }

    /**
     * @return {@link #identifier} (An external identifier for this statement such as an IRI.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseStatement setIdentifier(List<Identifier> theIdentifier) { 
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

    public DeviceUseStatement addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (A code representing the patient or other source's judgment about the state of the device used that this statement is about.  Generally this will be active or completed.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DeviceUseStatementStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseStatement.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DeviceUseStatementStatus>(new DeviceUseStatementStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code representing the patient or other source's judgment about the state of the device used that this statement is about.  Generally this will be active or completed.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DeviceUseStatement setStatusElement(Enumeration<DeviceUseStatementStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code representing the patient or other source's judgment about the state of the device used that this statement is about.  Generally this will be active or completed.
     */
    public DeviceUseStatementStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code representing the patient or other source's judgment about the state of the device used that this statement is about.  Generally this will be active or completed.
     */
    public DeviceUseStatement setStatus(DeviceUseStatementStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<DeviceUseStatementStatus>(new DeviceUseStatementStatusEnumFactory());
        this.status.setValue(value);
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
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who used the device.)
     */
    public DeviceUseStatement setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
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
     * @return {@link #timing} (How often the device was used.)
     */
    public Type getTiming() { 
      return this.timing;
    }

    /**
     * @return {@link #timing} (How often the device was used.)
     */
    public Timing getTimingTiming() throws FHIRException { 
      if (!(this.timing instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Timing) this.timing;
    }

    public boolean hasTimingTiming() { 
      return this.timing instanceof Timing;
    }

    /**
     * @return {@link #timing} (How often the device was used.)
     */
    public Period getTimingPeriod() throws FHIRException { 
      if (!(this.timing instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Period) this.timing;
    }

    public boolean hasTimingPeriod() { 
      return this.timing instanceof Period;
    }

    /**
     * @return {@link #timing} (How often the device was used.)
     */
    public DateTimeType getTimingDateTimeType() throws FHIRException { 
      if (!(this.timing instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (DateTimeType) this.timing;
    }

    public boolean hasTimingDateTimeType() { 
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
     * @return {@link #source} (Who reported the device was being used by the patient.)
     */
    public Reference getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseStatement.source");
        else if (Configuration.doAutoCreate())
          this.source = new Reference(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (Who reported the device was being used by the patient.)
     */
    public DeviceUseStatement setSource(Reference value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who reported the device was being used by the patient.)
     */
    public Resource getSourceTarget() { 
      return this.sourceTarget;
    }

    /**
     * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who reported the device was being used by the patient.)
     */
    public DeviceUseStatement setSourceTarget(Resource value) { 
      this.sourceTarget = value;
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
     * @return {@link #indication} (Reason or justification for the use of the device.)
     */
    public List<CodeableConcept> getIndication() { 
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      return this.indication;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseStatement setIndication(List<CodeableConcept> theIndication) { 
      this.indication = theIndication;
      return this;
    }

    public boolean hasIndication() { 
      if (this.indication == null)
        return false;
      for (CodeableConcept item : this.indication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addIndication() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return t;
    }

    public DeviceUseStatement addIndication(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #indication}, creating it if it does not already exist
     */
    public CodeableConcept getIndicationFirstRep() { 
      if (getIndication().isEmpty()) {
        addIndication();
      }
      return getIndication().get(0);
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the device was used ( i.e. the target site).)
     */
    public CodeableConcept getBodySite() { 
      if (this.bodySite == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseStatement.bodySite");
        else if (Configuration.doAutoCreate())
          this.bodySite = new CodeableConcept(); // cc
      return this.bodySite;
    }

    public boolean hasBodySite() { 
      return this.bodySite != null && !this.bodySite.isEmpty();
    }

    /**
     * @param value {@link #bodySite} (Indicates the site on the subject's body where the device was used ( i.e. the target site).)
     */
    public DeviceUseStatement setBodySite(CodeableConcept value) { 
      this.bodySite = value;
      return this;
    }

    /**
     * @return {@link #note} (Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseStatement setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public DeviceUseStatement addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "An external identifier for this statement such as an IRI.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "A code representing the patient or other source's judgment about the state of the device used that this statement is about.  Generally this will be active or completed.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The patient who used the device.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("whenUsed", "Period", "The time period over which the device was used.", 0, java.lang.Integer.MAX_VALUE, whenUsed));
        childrenList.add(new Property("timing[x]", "Timing|Period|dateTime", "How often the device was used.", 0, java.lang.Integer.MAX_VALUE, timing));
        childrenList.add(new Property("recordedOn", "dateTime", "The time at which the statement was made/recorded.", 0, java.lang.Integer.MAX_VALUE, recordedOn));
        childrenList.add(new Property("source", "Reference(Patient|Practitioner|RelatedPerson)", "Who reported the device was being used by the patient.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("device", "Reference(Device)", "The details of the device used.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("indication", "CodeableConcept", "Reason or justification for the use of the device.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("bodySite", "CodeableConcept", "Indicates the site on the subject's body where the device was used ( i.e. the target site).", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("note", "Annotation", "Details about the device statement that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<DeviceUseStatementStatus>
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 2042879511: /*whenUsed*/ return this.whenUsed == null ? new Base[0] : new Base[] {this.whenUsed}; // Period
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 735397551: /*recordedOn*/ return this.recordedOn == null ? new Base[0] : new Base[] {this.recordedOn}; // DateTimeType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Reference
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : new Base[] {this.device}; // Reference
        case -597168804: /*indication*/ return this.indication == null ? new Base[0] : this.indication.toArray(new Base[this.indication.size()]); // CodeableConcept
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // CodeableConcept
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
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
          value = new DeviceUseStatementStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<DeviceUseStatementStatus>
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 2042879511: // whenUsed
          this.whenUsed = castToPeriod(value); // Period
          return value;
        case -873664438: // timing
          this.timing = castToType(value); // Type
          return value;
        case 735397551: // recordedOn
          this.recordedOn = castToDateTime(value); // DateTimeType
          return value;
        case -896505829: // source
          this.source = castToReference(value); // Reference
          return value;
        case -1335157162: // device
          this.device = castToReference(value); // Reference
          return value;
        case -597168804: // indication
          this.getIndication().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1702620169: // bodySite
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new DeviceUseStatementStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<DeviceUseStatementStatus>
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("whenUsed")) {
          this.whenUsed = castToPeriod(value); // Period
        } else if (name.equals("timing[x]")) {
          this.timing = castToType(value); // Type
        } else if (name.equals("recordedOn")) {
          this.recordedOn = castToDateTime(value); // DateTimeType
        } else if (name.equals("source")) {
          this.source = castToReference(value); // Reference
        } else if (name.equals("device")) {
          this.device = castToReference(value); // Reference
        } else if (name.equals("indication")) {
          this.getIndication().add(castToCodeableConcept(value));
        } else if (name.equals("bodySite")) {
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -1867885268:  return getSubject(); 
        case 2042879511:  return getWhenUsed(); 
        case 164632566:  return getTiming(); 
        case -873664438:  return getTiming(); 
        case 735397551:  return getRecordedOnElement();
        case -896505829:  return getSource(); 
        case -1335157162:  return getDevice(); 
        case -597168804:  return addIndication(); 
        case 1702620169:  return getBodySite(); 
        case 3387378:  return addNote(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 2042879511: /*whenUsed*/ return new String[] {"Period"};
        case -873664438: /*timing*/ return new String[] {"Timing", "Period", "dateTime"};
        case 735397551: /*recordedOn*/ return new String[] {"dateTime"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        case -1335157162: /*device*/ return new String[] {"Reference"};
        case -597168804: /*indication*/ return new String[] {"CodeableConcept"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceUseStatement.status");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("whenUsed")) {
          this.whenUsed = new Period();
          return this.whenUsed;
        }
        else if (name.equals("timingTiming")) {
          this.timing = new Timing();
          return this.timing;
        }
        else if (name.equals("timingPeriod")) {
          this.timing = new Period();
          return this.timing;
        }
        else if (name.equals("timingDateTime")) {
          this.timing = new DateTimeType();
          return this.timing;
        }
        else if (name.equals("recordedOn")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceUseStatement.recordedOn");
        }
        else if (name.equals("source")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("device")) {
          this.device = new Reference();
          return this.device;
        }
        else if (name.equals("indication")) {
          return addIndication();
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new CodeableConcept();
          return this.bodySite;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DeviceUseStatement";

  }

      public DeviceUseStatement copy() {
        DeviceUseStatement dst = new DeviceUseStatement();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.whenUsed = whenUsed == null ? null : whenUsed.copy();
        dst.timing = timing == null ? null : timing.copy();
        dst.recordedOn = recordedOn == null ? null : recordedOn.copy();
        dst.source = source == null ? null : source.copy();
        dst.device = device == null ? null : device.copy();
        if (indication != null) {
          dst.indication = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : indication)
            dst.indication.add(i.copy());
        };
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(subject, o.subject, true)
           && compareDeep(whenUsed, o.whenUsed, true) && compareDeep(timing, o.timing, true) && compareDeep(recordedOn, o.recordedOn, true)
           && compareDeep(source, o.source, true) && compareDeep(device, o.device, true) && compareDeep(indication, o.indication, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DeviceUseStatement))
          return false;
        DeviceUseStatement o = (DeviceUseStatement) other;
        return compareValues(status, o.status, true) && compareValues(recordedOn, o.recordedOn, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, subject
          , whenUsed, timing, recordedOn, source, device, indication, bodySite, note);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DeviceUseStatement;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Search by identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseStatement.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DeviceUseStatement.identifier", description="Search by identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Search by identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseStatement.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseStatement.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="DeviceUseStatement.subject", description="Search by subject", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseStatement.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseStatement:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("DeviceUseStatement:subject").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseStatement.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="DeviceUseStatement.subject", description="Search by subject - a patient", type="reference", target={Group.class, Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseStatement.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseStatement:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("DeviceUseStatement:patient").toLocked();

 /**
   * Search parameter: <b>device</b>
   * <p>
   * Description: <b>Search by device</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseStatement.device</b><br>
   * </p>
   */
  @SearchParamDefinition(name="device", path="DeviceUseStatement.device", description="Search by device", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device") }, target={Device.class } )
  public static final String SP_DEVICE = "device";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>device</b>
   * <p>
   * Description: <b>Search by device</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseStatement.device</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEVICE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEVICE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseStatement:device</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEVICE = new ca.uhn.fhir.model.api.Include("DeviceUseStatement:device").toLocked();


}

