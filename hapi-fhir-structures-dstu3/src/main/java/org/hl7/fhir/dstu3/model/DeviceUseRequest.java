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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.
 */
@ResourceDef(name="DeviceUseRequest", profile="http://hl7.org/fhir/Profile/DeviceUseRequest")
public class DeviceUseRequest extends DomainResource {

    public enum DeviceUseRequestStatus {
        /**
         * The request has been proposed.
         */
        PROPOSED, 
        /**
         * The request has been planned.
         */
        PLANNED, 
        /**
         * The request has been placed.
         */
        REQUESTED, 
        /**
         * The receiving system has received the request but not yet decided whether it will be performed.
         */
        RECEIVED, 
        /**
         * The receiving system has accepted the request but work has not yet commenced.
         */
        ACCEPTED, 
        /**
         * The work to fulfill the order is happening.
         */
        INPROGRESS, 
        /**
         * The work has been complete, the report(s) released, and no further work is planned.
         */
        COMPLETED, 
        /**
         * The request has been held by originating system/user request.
         */
        SUSPENDED, 
        /**
         * The receiving system has declined to fulfill the request.
         */
        REJECTED, 
        /**
         * The request was attempted, but due to some procedural error, it could not be completed.
         */
        ABORTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceUseRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("received".equals(codeString))
          return RECEIVED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("aborted".equals(codeString))
          return ABORTED;
        throw new FHIRException("Unknown DeviceUseRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case PLANNED: return "planned";
            case REQUESTED: return "requested";
            case RECEIVED: return "received";
            case ACCEPTED: return "accepted";
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case SUSPENDED: return "suspended";
            case REJECTED: return "rejected";
            case ABORTED: return "aborted";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/device-use-request-status";
            case PLANNED: return "http://hl7.org/fhir/device-use-request-status";
            case REQUESTED: return "http://hl7.org/fhir/device-use-request-status";
            case RECEIVED: return "http://hl7.org/fhir/device-use-request-status";
            case ACCEPTED: return "http://hl7.org/fhir/device-use-request-status";
            case INPROGRESS: return "http://hl7.org/fhir/device-use-request-status";
            case COMPLETED: return "http://hl7.org/fhir/device-use-request-status";
            case SUSPENDED: return "http://hl7.org/fhir/device-use-request-status";
            case REJECTED: return "http://hl7.org/fhir/device-use-request-status";
            case ABORTED: return "http://hl7.org/fhir/device-use-request-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "The request has been proposed.";
            case PLANNED: return "The request has been planned.";
            case REQUESTED: return "The request has been placed.";
            case RECEIVED: return "The receiving system has received the request but not yet decided whether it will be performed.";
            case ACCEPTED: return "The receiving system has accepted the request but work has not yet commenced.";
            case INPROGRESS: return "The work to fulfill the order is happening.";
            case COMPLETED: return "The work has been complete, the report(s) released, and no further work is planned.";
            case SUSPENDED: return "The request has been held by originating system/user request.";
            case REJECTED: return "The receiving system has declined to fulfill the request.";
            case ABORTED: return "The request was attempted, but due to some procedural error, it could not be completed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case PLANNED: return "Planned";
            case REQUESTED: return "Requested";
            case RECEIVED: return "Received";
            case ACCEPTED: return "Accepted";
            case INPROGRESS: return "In Progress";
            case COMPLETED: return "Completed";
            case SUSPENDED: return "Suspended";
            case REJECTED: return "Rejected";
            case ABORTED: return "Aborted";
            default: return "?";
          }
        }
    }

  public static class DeviceUseRequestStatusEnumFactory implements EnumFactory<DeviceUseRequestStatus> {
    public DeviceUseRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return DeviceUseRequestStatus.PROPOSED;
        if ("planned".equals(codeString))
          return DeviceUseRequestStatus.PLANNED;
        if ("requested".equals(codeString))
          return DeviceUseRequestStatus.REQUESTED;
        if ("received".equals(codeString))
          return DeviceUseRequestStatus.RECEIVED;
        if ("accepted".equals(codeString))
          return DeviceUseRequestStatus.ACCEPTED;
        if ("in-progress".equals(codeString))
          return DeviceUseRequestStatus.INPROGRESS;
        if ("completed".equals(codeString))
          return DeviceUseRequestStatus.COMPLETED;
        if ("suspended".equals(codeString))
          return DeviceUseRequestStatus.SUSPENDED;
        if ("rejected".equals(codeString))
          return DeviceUseRequestStatus.REJECTED;
        if ("aborted".equals(codeString))
          return DeviceUseRequestStatus.ABORTED;
        throw new IllegalArgumentException("Unknown DeviceUseRequestStatus code '"+codeString+"'");
        }
        public Enumeration<DeviceUseRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.PROPOSED);
        if ("planned".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.PLANNED);
        if ("requested".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.REQUESTED);
        if ("received".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.RECEIVED);
        if ("accepted".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.ACCEPTED);
        if ("in-progress".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.INPROGRESS);
        if ("completed".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.COMPLETED);
        if ("suspended".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.SUSPENDED);
        if ("rejected".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.REJECTED);
        if ("aborted".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.ABORTED);
        throw new FHIRException("Unknown DeviceUseRequestStatus code '"+codeString+"'");
        }
    public String toCode(DeviceUseRequestStatus code) {
      if (code == DeviceUseRequestStatus.PROPOSED)
        return "proposed";
      if (code == DeviceUseRequestStatus.PLANNED)
        return "planned";
      if (code == DeviceUseRequestStatus.REQUESTED)
        return "requested";
      if (code == DeviceUseRequestStatus.RECEIVED)
        return "received";
      if (code == DeviceUseRequestStatus.ACCEPTED)
        return "accepted";
      if (code == DeviceUseRequestStatus.INPROGRESS)
        return "in-progress";
      if (code == DeviceUseRequestStatus.COMPLETED)
        return "completed";
      if (code == DeviceUseRequestStatus.SUSPENDED)
        return "suspended";
      if (code == DeviceUseRequestStatus.REJECTED)
        return "rejected";
      if (code == DeviceUseRequestStatus.ABORTED)
        return "aborted";
      return "?";
      }
    public String toSystem(DeviceUseRequestStatus code) {
      return code.getSystem();
      }
    }

    public enum DeviceUseRequestPriority {
        /**
         * The request has a normal priority.
         */
        ROUTINE, 
        /**
         * The request should be done urgently.
         */
        URGENT, 
        /**
         * The request is time-critical.
         */
        STAT, 
        /**
         * The request should be acted on as soon as possible.
         */
        ASAP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceUseRequestPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return ROUTINE;
        if ("urgent".equals(codeString))
          return URGENT;
        if ("stat".equals(codeString))
          return STAT;
        if ("asap".equals(codeString))
          return ASAP;
        throw new FHIRException("Unknown DeviceUseRequestPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ROUTINE: return "routine";
            case URGENT: return "urgent";
            case STAT: return "stat";
            case ASAP: return "asap";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ROUTINE: return "http://hl7.org/fhir/device-use-request-priority";
            case URGENT: return "http://hl7.org/fhir/device-use-request-priority";
            case STAT: return "http://hl7.org/fhir/device-use-request-priority";
            case ASAP: return "http://hl7.org/fhir/device-use-request-priority";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ROUTINE: return "The request has a normal priority.";
            case URGENT: return "The request should be done urgently.";
            case STAT: return "The request is time-critical.";
            case ASAP: return "The request should be acted on as soon as possible.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ROUTINE: return "Routine";
            case URGENT: return "Urgent";
            case STAT: return "Stat";
            case ASAP: return "ASAP";
            default: return "?";
          }
        }
    }

  public static class DeviceUseRequestPriorityEnumFactory implements EnumFactory<DeviceUseRequestPriority> {
    public DeviceUseRequestPriority fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return DeviceUseRequestPriority.ROUTINE;
        if ("urgent".equals(codeString))
          return DeviceUseRequestPriority.URGENT;
        if ("stat".equals(codeString))
          return DeviceUseRequestPriority.STAT;
        if ("asap".equals(codeString))
          return DeviceUseRequestPriority.ASAP;
        throw new IllegalArgumentException("Unknown DeviceUseRequestPriority code '"+codeString+"'");
        }
        public Enumeration<DeviceUseRequestPriority> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("routine".equals(codeString))
          return new Enumeration<DeviceUseRequestPriority>(this, DeviceUseRequestPriority.ROUTINE);
        if ("urgent".equals(codeString))
          return new Enumeration<DeviceUseRequestPriority>(this, DeviceUseRequestPriority.URGENT);
        if ("stat".equals(codeString))
          return new Enumeration<DeviceUseRequestPriority>(this, DeviceUseRequestPriority.STAT);
        if ("asap".equals(codeString))
          return new Enumeration<DeviceUseRequestPriority>(this, DeviceUseRequestPriority.ASAP);
        throw new FHIRException("Unknown DeviceUseRequestPriority code '"+codeString+"'");
        }
    public String toCode(DeviceUseRequestPriority code) {
      if (code == DeviceUseRequestPriority.ROUTINE)
        return "routine";
      if (code == DeviceUseRequestPriority.URGENT)
        return "urgent";
      if (code == DeviceUseRequestPriority.STAT)
        return "stat";
      if (code == DeviceUseRequestPriority.ASAP)
        return "asap";
      return "?";
      }
    public String toSystem(DeviceUseRequestPriority code) {
      return code.getSystem();
      }
    }

    /**
     * Indicates the site on the subject's body where the device should be used ( i.e. the target site).
     */
    @Child(name = "bodySite", type = {CodeableConcept.class, BodySite.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Target body site", formalDefinition="Indicates the site on the subject's body where the device should be used ( i.e. the target site)." )
    protected Type bodySite;

    /**
     * The status of the request.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | planned | requested | received | accepted | in-progress | completed | suspended | rejected | aborted", formalDefinition="The status of the request." )
    protected Enumeration<DeviceUseRequestStatus> status;

    /**
     * The details of the device  to be used.
     */
    @Child(name = "device", type = {Device.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Device requested", formalDefinition="The details of the device  to be used." )
    protected Reference device;

    /**
     * The actual object that is the target of the reference (The details of the device  to be used.)
     */
    protected Device deviceTarget;

    /**
     * An encounter that provides additional context in which this request is made.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter motivating request", formalDefinition="An encounter that provides additional context in which this request is made." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (An encounter that provides additional context in which this request is made.)
     */
    protected Encounter encounterTarget;

    /**
     * Identifiers assigned to this order by the orderer or by the receiver.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Request identifier", formalDefinition="Identifiers assigned to this order by the orderer or by the receiver." )
    protected List<Identifier> identifier;

    /**
     * Reason or justification for the use of this device.
     */
    @Child(name = "indication", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reason for request", formalDefinition="Reason or justification for the use of this device." )
    protected List<CodeableConcept> indication;

    /**
     * Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.
     */
    @Child(name = "notes", type = {StringType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Notes or comments", formalDefinition="Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement." )
    protected List<StringType> notes;

    /**
     * The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%.
     */
    @Child(name = "prnReason", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="PRN", formalDefinition="The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%." )
    protected List<CodeableConcept> prnReason;

    /**
     * The time when the request was made.
     */
    @Child(name = "orderedOn", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When ordered", formalDefinition="The time when the request was made." )
    protected DateTimeType orderedOn;

    /**
     * The time at which the request was made/recorded.
     */
    @Child(name = "recordedOn", type = {DateTimeType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When recorded", formalDefinition="The time at which the request was made/recorded." )
    protected DateTimeType recordedOn;

    /**
     * The patient who will use the device.
     */
    @Child(name = "subject", type = {Patient.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Focus of request", formalDefinition="The patient who will use the device." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who will use the device.)
     */
    protected Patient subjectTarget;

    /**
     * The timing schedule for the use of the device The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
     */
    @Child(name = "timing", type = {Timing.class, Period.class, DateTimeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Schedule for use", formalDefinition="The timing schedule for the use of the device The Schedule data type allows many different expressions, for example. \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"." )
    protected Type timing;

    /**
     * Characterizes how quickly the  use of device must be initiated. Includes concepts such as stat, urgent, routine.
     */
    @Child(name = "priority", type = {CodeType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="routine | urgent | stat | asap", formalDefinition="Characterizes how quickly the  use of device must be initiated. Includes concepts such as stat, urgent, routine." )
    protected Enumeration<DeviceUseRequestPriority> priority;

    private static final long serialVersionUID = 1208477058L;

  /**
   * Constructor
   */
    public DeviceUseRequest() {
      super();
    }

  /**
   * Constructor
   */
    public DeviceUseRequest(Reference device, Reference subject) {
      super();
      this.device = device;
      this.subject = subject;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the device should be used ( i.e. the target site).)
     */
    public Type getBodySite() { 
      return this.bodySite;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the device should be used ( i.e. the target site).)
     */
    public CodeableConcept getBodySiteCodeableConcept() throws FHIRException { 
      if (!(this.bodySite instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.bodySite.getClass().getName()+" was encountered");
      return (CodeableConcept) this.bodySite;
    }

    public boolean hasBodySiteCodeableConcept() { 
      return this.bodySite instanceof CodeableConcept;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the device should be used ( i.e. the target site).)
     */
    public Reference getBodySiteReference() throws FHIRException { 
      if (!(this.bodySite instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.bodySite.getClass().getName()+" was encountered");
      return (Reference) this.bodySite;
    }

    public boolean hasBodySiteReference() { 
      return this.bodySite instanceof Reference;
    }

    public boolean hasBodySite() { 
      return this.bodySite != null && !this.bodySite.isEmpty();
    }

    /**
     * @param value {@link #bodySite} (Indicates the site on the subject's body where the device should be used ( i.e. the target site).)
     */
    public DeviceUseRequest setBodySite(Type value) { 
      this.bodySite = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DeviceUseRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DeviceUseRequestStatus>(new DeviceUseRequestStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DeviceUseRequest setStatusElement(Enumeration<DeviceUseRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the request.
     */
    public DeviceUseRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the request.
     */
    public DeviceUseRequest setStatus(DeviceUseRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<DeviceUseRequestStatus>(new DeviceUseRequestStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #device} (The details of the device  to be used.)
     */
    public Reference getDevice() { 
      if (this.device == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.device");
        else if (Configuration.doAutoCreate())
          this.device = new Reference(); // cc
      return this.device;
    }

    public boolean hasDevice() { 
      return this.device != null && !this.device.isEmpty();
    }

    /**
     * @param value {@link #device} (The details of the device  to be used.)
     */
    public DeviceUseRequest setDevice(Reference value) { 
      this.device = value;
      return this;
    }

    /**
     * @return {@link #device} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The details of the device  to be used.)
     */
    public Device getDeviceTarget() { 
      if (this.deviceTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.device");
        else if (Configuration.doAutoCreate())
          this.deviceTarget = new Device(); // aa
      return this.deviceTarget;
    }

    /**
     * @param value {@link #device} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The details of the device  to be used.)
     */
    public DeviceUseRequest setDeviceTarget(Device value) { 
      this.deviceTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (An encounter that provides additional context in which this request is made.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (An encounter that provides additional context in which this request is made.)
     */
    public DeviceUseRequest setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An encounter that provides additional context in which this request is made.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An encounter that provides additional context in which this request is made.)
     */
    public DeviceUseRequest setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
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
    public DeviceUseRequest addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #indication} (Reason or justification for the use of this device.)
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
     * @return {@link #indication} (Reason or justification for the use of this device.)
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
    public DeviceUseRequest addIndication(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return this;
    }

    /**
     * @return {@link #notes} (Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
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
     * @return {@link #notes} (Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
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
     * @param value {@link #notes} (Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
     */
    public DeviceUseRequest addNotes(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.notes == null)
        this.notes = new ArrayList<StringType>();
      this.notes.add(t);
      return this;
    }

    /**
     * @param value {@link #notes} (Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
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
     * @return {@link #prnReason} (The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%.)
     */
    public List<CodeableConcept> getPrnReason() { 
      if (this.prnReason == null)
        this.prnReason = new ArrayList<CodeableConcept>();
      return this.prnReason;
    }

    public boolean hasPrnReason() { 
      if (this.prnReason == null)
        return false;
      for (CodeableConcept item : this.prnReason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #prnReason} (The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%.)
     */
    // syntactic sugar
    public CodeableConcept addPrnReason() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.prnReason == null)
        this.prnReason = new ArrayList<CodeableConcept>();
      this.prnReason.add(t);
      return t;
    }

    // syntactic sugar
    public DeviceUseRequest addPrnReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.prnReason == null)
        this.prnReason = new ArrayList<CodeableConcept>();
      this.prnReason.add(t);
      return this;
    }

    /**
     * @return {@link #orderedOn} (The time when the request was made.). This is the underlying object with id, value and extensions. The accessor "getOrderedOn" gives direct access to the value
     */
    public DateTimeType getOrderedOnElement() { 
      if (this.orderedOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.orderedOn");
        else if (Configuration.doAutoCreate())
          this.orderedOn = new DateTimeType(); // bb
      return this.orderedOn;
    }

    public boolean hasOrderedOnElement() { 
      return this.orderedOn != null && !this.orderedOn.isEmpty();
    }

    public boolean hasOrderedOn() { 
      return this.orderedOn != null && !this.orderedOn.isEmpty();
    }

    /**
     * @param value {@link #orderedOn} (The time when the request was made.). This is the underlying object with id, value and extensions. The accessor "getOrderedOn" gives direct access to the value
     */
    public DeviceUseRequest setOrderedOnElement(DateTimeType value) { 
      this.orderedOn = value;
      return this;
    }

    /**
     * @return The time when the request was made.
     */
    public Date getOrderedOn() { 
      return this.orderedOn == null ? null : this.orderedOn.getValue();
    }

    /**
     * @param value The time when the request was made.
     */
    public DeviceUseRequest setOrderedOn(Date value) { 
      if (value == null)
        this.orderedOn = null;
      else {
        if (this.orderedOn == null)
          this.orderedOn = new DateTimeType();
        this.orderedOn.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #recordedOn} (The time at which the request was made/recorded.). This is the underlying object with id, value and extensions. The accessor "getRecordedOn" gives direct access to the value
     */
    public DateTimeType getRecordedOnElement() { 
      if (this.recordedOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.recordedOn");
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
     * @param value {@link #recordedOn} (The time at which the request was made/recorded.). This is the underlying object with id, value and extensions. The accessor "getRecordedOn" gives direct access to the value
     */
    public DeviceUseRequest setRecordedOnElement(DateTimeType value) { 
      this.recordedOn = value;
      return this;
    }

    /**
     * @return The time at which the request was made/recorded.
     */
    public Date getRecordedOn() { 
      return this.recordedOn == null ? null : this.recordedOn.getValue();
    }

    /**
     * @param value The time at which the request was made/recorded.
     */
    public DeviceUseRequest setRecordedOn(Date value) { 
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
     * @return {@link #subject} (The patient who will use the device.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient who will use the device.)
     */
    public DeviceUseRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who will use the device.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient(); // aa
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who will use the device.)
     */
    public DeviceUseRequest setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #timing} (The timing schedule for the use of the device The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Type getTiming() { 
      return this.timing;
    }

    /**
     * @return {@link #timing} (The timing schedule for the use of the device The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
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
     * @return {@link #timing} (The timing schedule for the use of the device The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
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
     * @return {@link #timing} (The timing schedule for the use of the device The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
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
     * @param value {@link #timing} (The timing schedule for the use of the device The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public DeviceUseRequest setTiming(Type value) { 
      this.timing = value;
      return this;
    }

    /**
     * @return {@link #priority} (Characterizes how quickly the  use of device must be initiated. Includes concepts such as stat, urgent, routine.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Enumeration<DeviceUseRequestPriority> getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Enumeration<DeviceUseRequestPriority>(new DeviceUseRequestPriorityEnumFactory()); // bb
      return this.priority;
    }

    public boolean hasPriorityElement() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Characterizes how quickly the  use of device must be initiated. Includes concepts such as stat, urgent, routine.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public DeviceUseRequest setPriorityElement(Enumeration<DeviceUseRequestPriority> value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return Characterizes how quickly the  use of device must be initiated. Includes concepts such as stat, urgent, routine.
     */
    public DeviceUseRequestPriority getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value Characterizes how quickly the  use of device must be initiated. Includes concepts such as stat, urgent, routine.
     */
    public DeviceUseRequest setPriority(DeviceUseRequestPriority value) { 
      if (value == null)
        this.priority = null;
      else {
        if (this.priority == null)
          this.priority = new Enumeration<DeviceUseRequestPriority>(new DeviceUseRequestPriorityEnumFactory());
        this.priority.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("bodySite[x]", "CodeableConcept|Reference(BodySite)", "Indicates the site on the subject's body where the device should be used ( i.e. the target site).", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("status", "code", "The status of the request.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("device", "Reference(Device)", "The details of the device  to be used.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "An encounter that provides additional context in which this request is made.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order by the orderer or by the receiver.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("indication", "CodeableConcept", "Reason or justification for the use of this device.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("notes", "string", "Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("prnReason", "CodeableConcept", "The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%.", 0, java.lang.Integer.MAX_VALUE, prnReason));
        childrenList.add(new Property("orderedOn", "dateTime", "The time when the request was made.", 0, java.lang.Integer.MAX_VALUE, orderedOn));
        childrenList.add(new Property("recordedOn", "dateTime", "The time at which the request was made/recorded.", 0, java.lang.Integer.MAX_VALUE, recordedOn));
        childrenList.add(new Property("subject", "Reference(Patient)", "The patient who will use the device.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("timing[x]", "Timing|Period|dateTime", "The timing schedule for the use of the device The Schedule data type allows many different expressions, for example. \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\".", 0, java.lang.Integer.MAX_VALUE, timing));
        childrenList.add(new Property("priority", "code", "Characterizes how quickly the  use of device must be initiated. Includes concepts such as stat, urgent, routine.", 0, java.lang.Integer.MAX_VALUE, priority));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("bodySite[x]"))
          this.bodySite = (Type) value; // Type
        else if (name.equals("status"))
          this.status = new DeviceUseRequestStatusEnumFactory().fromType(value); // Enumeration<DeviceUseRequestStatus>
        else if (name.equals("device"))
          this.device = castToReference(value); // Reference
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("indication"))
          this.getIndication().add(castToCodeableConcept(value));
        else if (name.equals("notes"))
          this.getNotes().add(castToString(value));
        else if (name.equals("prnReason"))
          this.getPrnReason().add(castToCodeableConcept(value));
        else if (name.equals("orderedOn"))
          this.orderedOn = castToDateTime(value); // DateTimeType
        else if (name.equals("recordedOn"))
          this.recordedOn = castToDateTime(value); // DateTimeType
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("timing[x]"))
          this.timing = (Type) value; // Type
        else if (name.equals("priority"))
          this.priority = new DeviceUseRequestPriorityEnumFactory().fromType(value); // Enumeration<DeviceUseRequestPriority>
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("bodySiteCodeableConcept")) {
          this.bodySite = new CodeableConcept();
          return this.bodySite;
        }
        else if (name.equals("bodySiteReference")) {
          this.bodySite = new Reference();
          return this.bodySite;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceUseRequest.status");
        }
        else if (name.equals("device")) {
          this.device = new Reference();
          return this.device;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("indication")) {
          return addIndication();
        }
        else if (name.equals("notes")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceUseRequest.notes");
        }
        else if (name.equals("prnReason")) {
          return addPrnReason();
        }
        else if (name.equals("orderedOn")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceUseRequest.orderedOn");
        }
        else if (name.equals("recordedOn")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceUseRequest.recordedOn");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
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
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceUseRequest.priority");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DeviceUseRequest";

  }

      public DeviceUseRequest copy() {
        DeviceUseRequest dst = new DeviceUseRequest();
        copyValues(dst);
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        dst.status = status == null ? null : status.copy();
        dst.device = device == null ? null : device.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
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
        if (prnReason != null) {
          dst.prnReason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : prnReason)
            dst.prnReason.add(i.copy());
        };
        dst.orderedOn = orderedOn == null ? null : orderedOn.copy();
        dst.recordedOn = recordedOn == null ? null : recordedOn.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.timing = timing == null ? null : timing.copy();
        dst.priority = priority == null ? null : priority.copy();
        return dst;
      }

      protected DeviceUseRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DeviceUseRequest))
          return false;
        DeviceUseRequest o = (DeviceUseRequest) other;
        return compareDeep(bodySite, o.bodySite, true) && compareDeep(status, o.status, true) && compareDeep(device, o.device, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(identifier, o.identifier, true) && compareDeep(indication, o.indication, true)
           && compareDeep(notes, o.notes, true) && compareDeep(prnReason, o.prnReason, true) && compareDeep(orderedOn, o.orderedOn, true)
           && compareDeep(recordedOn, o.recordedOn, true) && compareDeep(subject, o.subject, true) && compareDeep(timing, o.timing, true)
           && compareDeep(priority, o.priority, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DeviceUseRequest))
          return false;
        DeviceUseRequest o = (DeviceUseRequest) other;
        return compareValues(status, o.status, true) && compareValues(notes, o.notes, true) && compareValues(orderedOn, o.orderedOn, true)
           && compareValues(recordedOn, o.recordedOn, true) && compareValues(priority, o.priority, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (bodySite == null || bodySite.isEmpty()) && (status == null || status.isEmpty())
           && (device == null || device.isEmpty()) && (encounter == null || encounter.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (indication == null || indication.isEmpty()) && (notes == null || notes.isEmpty()) && (prnReason == null || prnReason.isEmpty())
           && (orderedOn == null || orderedOn.isEmpty()) && (recordedOn == null || recordedOn.isEmpty())
           && (subject == null || subject.isEmpty()) && (timing == null || timing.isEmpty()) && (priority == null || priority.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DeviceUseRequest;
   }

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="DeviceUseRequest.subject", description="Search by subject", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:subject").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="DeviceUseRequest.subject", description="Search by subject - a patient", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:patient").toLocked();

 /**
   * Search parameter: <b>device</b>
   * <p>
   * Description: <b>Device requested</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.device</b><br>
   * </p>
   */
  @SearchParamDefinition(name="device", path="DeviceUseRequest.device", description="Device requested", type="reference" )
  public static final String SP_DEVICE = "device";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>device</b>
   * <p>
   * Description: <b>Device requested</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.device</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEVICE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEVICE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:device</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEVICE = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:device").toLocked();


}

