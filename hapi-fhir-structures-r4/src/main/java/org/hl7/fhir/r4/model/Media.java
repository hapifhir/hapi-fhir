package org.hl7.fhir.r4.model;

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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

import java.util.*;

import java.math.*;
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
 * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
 */
@ResourceDef(name="Media", profile="http://hl7.org/fhir/Profile/Media")
public class Media extends DomainResource {

    public enum MediaStatus {
        /**
         * The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation).  Preparation stages may be tracked for billing purposes.
         */
        PREPARATION, 
        /**
         * The event is currently occurring.
         */
        INPROGRESS, 
        /**
         * The event was terminated prior to any activity beyond preparation.  I.e. The 'main' activity has not yet begun.  The boundary between preparatory and the 'main' activity is context-specific.
         */
        NOTDONE, 
        /**
         * The event has been temporarily stopped but is expected to resume in the future.
         */
        ONHOLD, 
        /**
         * The event was terminated prior to the full completion of the intended activity but after at least some of the 'main' activity (beyond preparation) has occurred.
         */
        STOPPED, 
        /**
         * The event has now concluded.
         */
        COMPLETED, 
        /**
         * This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".).
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
        public static MediaStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preparation".equals(codeString))
          return PREPARATION;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("not-done".equals(codeString))
          return NOTDONE;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("stopped".equals(codeString))
          return STOPPED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MediaStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PREPARATION: return "preparation";
            case INPROGRESS: return "in-progress";
            case NOTDONE: return "not-done";
            case ONHOLD: return "on-hold";
            case STOPPED: return "stopped";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PREPARATION: return "http://hl7.org/fhir/event-status";
            case INPROGRESS: return "http://hl7.org/fhir/event-status";
            case NOTDONE: return "http://hl7.org/fhir/event-status";
            case ONHOLD: return "http://hl7.org/fhir/event-status";
            case STOPPED: return "http://hl7.org/fhir/event-status";
            case COMPLETED: return "http://hl7.org/fhir/event-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/event-status";
            case UNKNOWN: return "http://hl7.org/fhir/event-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PREPARATION: return "The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation).  Preparation stages may be tracked for billing purposes.";
            case INPROGRESS: return "The event is currently occurring.";
            case NOTDONE: return "The event was terminated prior to any activity beyond preparation.  I.e. The 'main' activity has not yet begun.  The boundary between preparatory and the 'main' activity is context-specific.";
            case ONHOLD: return "The event has been temporarily stopped but is expected to resume in the future.";
            case STOPPED: return "The event was terminated prior to the full completion of the intended activity but after at least some of the 'main' activity (beyond preparation) has occurred.";
            case COMPLETED: return "The event has now concluded.";
            case ENTEREDINERROR: return "This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".).";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, it's just not known which one.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PREPARATION: return "Preparation";
            case INPROGRESS: return "In Progress";
            case NOTDONE: return "Not Done";
            case ONHOLD: return "On Hold";
            case STOPPED: return "Stopped";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class MediaStatusEnumFactory implements EnumFactory<MediaStatus> {
    public MediaStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preparation".equals(codeString))
          return MediaStatus.PREPARATION;
        if ("in-progress".equals(codeString))
          return MediaStatus.INPROGRESS;
        if ("not-done".equals(codeString))
          return MediaStatus.NOTDONE;
        if ("on-hold".equals(codeString))
          return MediaStatus.ONHOLD;
        if ("stopped".equals(codeString))
          return MediaStatus.STOPPED;
        if ("completed".equals(codeString))
          return MediaStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return MediaStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return MediaStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown MediaStatus code '"+codeString+"'");
        }
        public Enumeration<MediaStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MediaStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("preparation".equals(codeString))
          return new Enumeration<MediaStatus>(this, MediaStatus.PREPARATION);
        if ("in-progress".equals(codeString))
          return new Enumeration<MediaStatus>(this, MediaStatus.INPROGRESS);
        if ("not-done".equals(codeString))
          return new Enumeration<MediaStatus>(this, MediaStatus.NOTDONE);
        if ("on-hold".equals(codeString))
          return new Enumeration<MediaStatus>(this, MediaStatus.ONHOLD);
        if ("stopped".equals(codeString))
          return new Enumeration<MediaStatus>(this, MediaStatus.STOPPED);
        if ("completed".equals(codeString))
          return new Enumeration<MediaStatus>(this, MediaStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<MediaStatus>(this, MediaStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<MediaStatus>(this, MediaStatus.UNKNOWN);
        throw new FHIRException("Unknown MediaStatus code '"+codeString+"'");
        }
    public String toCode(MediaStatus code) {
      if (code == MediaStatus.PREPARATION)
        return "preparation";
      if (code == MediaStatus.INPROGRESS)
        return "in-progress";
      if (code == MediaStatus.NOTDONE)
        return "not-done";
      if (code == MediaStatus.ONHOLD)
        return "on-hold";
      if (code == MediaStatus.STOPPED)
        return "stopped";
      if (code == MediaStatus.COMPLETED)
        return "completed";
      if (code == MediaStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == MediaStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(MediaStatus code) {
      return code.getSystem();
      }
    }

    /**
     * Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifier(s) for the image", formalDefinition="Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers." )
    protected List<Identifier> identifier;

    /**
     * A procedure that is fulfilled in whole or in part by the creation of this media.
     */
    @Child(name = "basedOn", type = {ServiceRequest.class, CarePlan.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Procedure that caused this media to be created", formalDefinition="A procedure that is fulfilled in whole or in part by the creation of this media." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (A procedure that is fulfilled in whole or in part by the creation of this media.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * A larger event of which this particular event is a component or step.
     */
    @Child(name = "partOf", type = {Reference.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of referenced event", formalDefinition="A larger event of which this particular event is a component or step." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (A larger event of which this particular event is a component or step.)
     */
    protected List<Resource> partOfTarget;


    /**
     * The current state of the {{title}}.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error | unknown", formalDefinition="The current state of the {{title}}." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/event-status")
    protected Enumeration<MediaStatus> status;

    /**
     * A code that classifies whether the media is an image, video or audio recording or some other media category.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Classification of media as image, video, or audio", formalDefinition="A code that classifies whether the media is an image, video or audio recording or some other media category." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/media-type")
    protected CodeableConcept type;

    /**
     * Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.
     */
    @Child(name = "modality", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of acquisition equipment/process", formalDefinition="Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/media-modality")
    protected CodeableConcept modality;

    /**
     * The name of the imaging view e.g. Lateral or Antero-posterior (AP).
     */
    @Child(name = "view", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Imaging view, e.g. Lateral or Antero-posterior", formalDefinition="The name of the imaging view e.g. Lateral or Antero-posterior (AP)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/media-view")
    protected CodeableConcept view;

    /**
     * Who/What this Media is a record of.
     */
    @Child(name = "subject", type = {Patient.class, Practitioner.class, Group.class, Device.class, Specimen.class, Location.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/What this Media is a record of", formalDefinition="Who/What this Media is a record of." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who/What this Media is a record of.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter or episode of care that establishes the context for this media.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter / Episode associated with media", formalDefinition="The encounter or episode of care that establishes the context for this media." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter or episode of care that establishes the context for this media.)
     */
    protected Resource contextTarget;

    /**
     * The date and time(s) at which the media was collected.
     */
    @Child(name = "created", type = {DateTimeType.class, Period.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When Media was collected", formalDefinition="The date and time(s) at which the media was collected." )
    protected Type created;

    /**
     * The date and time this version of the media was made available to providers, typically after having been reviewed.
     */
    @Child(name = "issued", type = {InstantType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date/Time this version was made available", formalDefinition="The date and time this version of the media was made available to providers, typically after having been reviewed." )
    protected InstantType issued;

    /**
     * The person who administered the collection of the image.
     */
    @Child(name = "operator", type = {Practitioner.class, PractitionerRole.class, Organization.class, CareTeam.class, Patient.class, Device.class, RelatedPerson.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The person who generated the image", formalDefinition="The person who administered the collection of the image." )
    protected Reference operator;

    /**
     * The actual object that is the target of the reference (The person who administered the collection of the image.)
     */
    protected Resource operatorTarget;

    /**
     * Describes why the event occurred in coded or textual form.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Why was event performed?", formalDefinition="Describes why the event occurred in coded or textual form." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-reason")
    protected List<CodeableConcept> reasonCode;

    /**
     * Indicates the site on the subject's body where the observation was made (i.e. the target site).
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Observed body part", formalDefinition="Indicates the site on the subject's body where the observation was made (i.e. the target site)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected CodeableConcept bodySite;

    /**
     * The name of the device / manufacturer of the device  that was used to make the recording.
     */
    @Child(name = "deviceName", type = {StringType.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the device/manufacturer", formalDefinition="The name of the device / manufacturer of the device  that was used to make the recording." )
    protected StringType deviceName;

    /**
     * The device used to collect the media.
     */
    @Child(name = "device", type = {Device.class, DeviceMetric.class, Device.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Observing Device", formalDefinition="The device used to collect the media." )
    protected Reference device;

    /**
     * The actual object that is the target of the reference (The device used to collect the media.)
     */
    protected Resource deviceTarget;

    /**
     * Height of the image in pixels (photo/video).
     */
    @Child(name = "height", type = {PositiveIntType.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Height of the image in pixels (photo/video)", formalDefinition="Height of the image in pixels (photo/video)." )
    protected PositiveIntType height;

    /**
     * Width of the image in pixels (photo/video).
     */
    @Child(name = "width", type = {PositiveIntType.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Width of the image in pixels (photo/video)", formalDefinition="Width of the image in pixels (photo/video)." )
    protected PositiveIntType width;

    /**
     * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.
     */
    @Child(name = "frames", type = {PositiveIntType.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of frames if > 1 (photo)", formalDefinition="The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required." )
    protected PositiveIntType frames;

    /**
     * The duration of the recording in seconds - for audio and video.
     */
    @Child(name = "duration", type = {DecimalType.class}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Length in seconds (audio / video)", formalDefinition="The duration of the recording in seconds - for audio and video." )
    protected DecimalType duration;

    /**
     * The actual content of the media - inline or by direct reference to the media source file.
     */
    @Child(name = "content", type = {Attachment.class}, order=20, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Actual Media - reference or data", formalDefinition="The actual content of the media - inline or by direct reference to the media source file." )
    protected Attachment content;

    /**
     * Comments made about the media by the performer, subject or other participants.
     */
    @Child(name = "note", type = {Annotation.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the media", formalDefinition="Comments made about the media by the performer, subject or other participants." )
    protected List<Annotation> note;

    private static final long serialVersionUID = 413873607L;

  /**
   * Constructor
   */
    public Media() {
      super();
    }

  /**
   * Constructor
   */
    public Media(Enumeration<MediaStatus> status, Attachment content) {
      super();
      this.status = status;
      this.content = content;
    }

    /**
     * @return {@link #identifier} (Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Media setIdentifier(List<Identifier> theIdentifier) { 
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

    public Media addIdentifier(Identifier t) { //3
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
     * @return {@link #basedOn} (A procedure that is fulfilled in whole or in part by the creation of this media.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Media setBasedOn(List<Reference> theBasedOn) { 
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

    public Media addBasedOn(Reference t) { //3
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
    public List<Resource> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<Resource>();
      return this.basedOnTarget;
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
    public Media setPartOf(List<Reference> thePartOf) { 
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

    public Media addPartOf(Reference t) { //3
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
     * @return {@link #status} (The current state of the {{title}}.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MediaStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MediaStatus>(new MediaStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The current state of the {{title}}.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Media setStatusElement(Enumeration<MediaStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The current state of the {{title}}.
     */
    public MediaStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The current state of the {{title}}.
     */
    public Media setStatus(MediaStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<MediaStatus>(new MediaStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (A code that classifies whether the media is an image, video or audio recording or some other media category.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (A code that classifies whether the media is an image, video or audio recording or some other media category.)
     */
    public Media setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #modality} (Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.)
     */
    public CodeableConcept getModality() { 
      if (this.modality == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.modality");
        else if (Configuration.doAutoCreate())
          this.modality = new CodeableConcept(); // cc
      return this.modality;
    }

    public boolean hasModality() { 
      return this.modality != null && !this.modality.isEmpty();
    }

    /**
     * @param value {@link #modality} (Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.)
     */
    public Media setModality(CodeableConcept value) { 
      this.modality = value;
      return this;
    }

    /**
     * @return {@link #view} (The name of the imaging view e.g. Lateral or Antero-posterior (AP).)
     */
    public CodeableConcept getView() { 
      if (this.view == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.view");
        else if (Configuration.doAutoCreate())
          this.view = new CodeableConcept(); // cc
      return this.view;
    }

    public boolean hasView() { 
      return this.view != null && !this.view.isEmpty();
    }

    /**
     * @param value {@link #view} (The name of the imaging view e.g. Lateral or Antero-posterior (AP).)
     */
    public Media setView(CodeableConcept value) { 
      this.view = value;
      return this;
    }

    /**
     * @return {@link #subject} (Who/What this Media is a record of.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Who/What this Media is a record of.)
     */
    public Media setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who/What this Media is a record of.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who/What this Media is a record of.)
     */
    public Media setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (The encounter or episode of care that establishes the context for this media.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The encounter or episode of care that establishes the context for this media.)
     */
    public Media setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter or episode of care that establishes the context for this media.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter or episode of care that establishes the context for this media.)
     */
    public Media setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #created} (The date and time(s) at which the media was collected.)
     */
    public Type getCreated() { 
      return this.created;
    }

    /**
     * @return {@link #created} (The date and time(s) at which the media was collected.)
     */
    public DateTimeType getCreatedDateTimeType() throws FHIRException { 
      if (this.created == null)
        return null;
      if (!(this.created instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.created.getClass().getName()+" was encountered");
      return (DateTimeType) this.created;
    }

    public boolean hasCreatedDateTimeType() { 
      return this != null && this.created instanceof DateTimeType;
    }

    /**
     * @return {@link #created} (The date and time(s) at which the media was collected.)
     */
    public Period getCreatedPeriod() throws FHIRException { 
      if (this.created == null)
        return null;
      if (!(this.created instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.created.getClass().getName()+" was encountered");
      return (Period) this.created;
    }

    public boolean hasCreatedPeriod() { 
      return this != null && this.created instanceof Period;
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (The date and time(s) at which the media was collected.)
     */
    public Media setCreated(Type value) { 
      if (value != null && !(value instanceof DateTimeType || value instanceof Period))
        throw new Error("Not the right type for Media.created[x]: "+value.fhirType());
      this.created = value;
      return this;
    }

    /**
     * @return {@link #issued} (The date and time this version of the media was made available to providers, typically after having been reviewed.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public InstantType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new InstantType(); // bb
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (The date and time this version of the media was made available to providers, typically after having been reviewed.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public Media setIssuedElement(InstantType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return The date and time this version of the media was made available to providers, typically after having been reviewed.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value The date and time this version of the media was made available to providers, typically after having been reviewed.
     */
    public Media setIssued(Date value) { 
      if (value == null)
        this.issued = null;
      else {
        if (this.issued == null)
          this.issued = new InstantType();
        this.issued.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #operator} (The person who administered the collection of the image.)
     */
    public Reference getOperator() { 
      if (this.operator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.operator");
        else if (Configuration.doAutoCreate())
          this.operator = new Reference(); // cc
      return this.operator;
    }

    public boolean hasOperator() { 
      return this.operator != null && !this.operator.isEmpty();
    }

    /**
     * @param value {@link #operator} (The person who administered the collection of the image.)
     */
    public Media setOperator(Reference value) { 
      this.operator = value;
      return this;
    }

    /**
     * @return {@link #operator} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person who administered the collection of the image.)
     */
    public Resource getOperatorTarget() { 
      return this.operatorTarget;
    }

    /**
     * @param value {@link #operator} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person who administered the collection of the image.)
     */
    public Media setOperatorTarget(Resource value) { 
      this.operatorTarget = value;
      return this;
    }

    /**
     * @return {@link #reasonCode} (Describes why the event occurred in coded or textual form.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Media setReasonCode(List<CodeableConcept> theReasonCode) { 
      this.reasonCode = theReasonCode;
      return this;
    }

    public boolean hasReasonCode() { 
      if (this.reasonCode == null)
        return false;
      for (CodeableConcept item : this.reasonCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return t;
    }

    public Media addReasonCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonCode}, creating it if it does not already exist
     */
    public CodeableConcept getReasonCodeFirstRep() { 
      if (getReasonCode().isEmpty()) {
        addReasonCode();
      }
      return getReasonCode().get(0);
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the observation was made (i.e. the target site).)
     */
    public CodeableConcept getBodySite() { 
      if (this.bodySite == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.bodySite");
        else if (Configuration.doAutoCreate())
          this.bodySite = new CodeableConcept(); // cc
      return this.bodySite;
    }

    public boolean hasBodySite() { 
      return this.bodySite != null && !this.bodySite.isEmpty();
    }

    /**
     * @param value {@link #bodySite} (Indicates the site on the subject's body where the observation was made (i.e. the target site).)
     */
    public Media setBodySite(CodeableConcept value) { 
      this.bodySite = value;
      return this;
    }

    /**
     * @return {@link #deviceName} (The name of the device / manufacturer of the device  that was used to make the recording.). This is the underlying object with id, value and extensions. The accessor "getDeviceName" gives direct access to the value
     */
    public StringType getDeviceNameElement() { 
      if (this.deviceName == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.deviceName");
        else if (Configuration.doAutoCreate())
          this.deviceName = new StringType(); // bb
      return this.deviceName;
    }

    public boolean hasDeviceNameElement() { 
      return this.deviceName != null && !this.deviceName.isEmpty();
    }

    public boolean hasDeviceName() { 
      return this.deviceName != null && !this.deviceName.isEmpty();
    }

    /**
     * @param value {@link #deviceName} (The name of the device / manufacturer of the device  that was used to make the recording.). This is the underlying object with id, value and extensions. The accessor "getDeviceName" gives direct access to the value
     */
    public Media setDeviceNameElement(StringType value) { 
      this.deviceName = value;
      return this;
    }

    /**
     * @return The name of the device / manufacturer of the device  that was used to make the recording.
     */
    public String getDeviceName() { 
      return this.deviceName == null ? null : this.deviceName.getValue();
    }

    /**
     * @param value The name of the device / manufacturer of the device  that was used to make the recording.
     */
    public Media setDeviceName(String value) { 
      if (Utilities.noString(value))
        this.deviceName = null;
      else {
        if (this.deviceName == null)
          this.deviceName = new StringType();
        this.deviceName.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #device} (The device used to collect the media.)
     */
    public Reference getDevice() { 
      if (this.device == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.device");
        else if (Configuration.doAutoCreate())
          this.device = new Reference(); // cc
      return this.device;
    }

    public boolean hasDevice() { 
      return this.device != null && !this.device.isEmpty();
    }

    /**
     * @param value {@link #device} (The device used to collect the media.)
     */
    public Media setDevice(Reference value) { 
      this.device = value;
      return this;
    }

    /**
     * @return {@link #device} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device used to collect the media.)
     */
    public Resource getDeviceTarget() { 
      return this.deviceTarget;
    }

    /**
     * @param value {@link #device} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device used to collect the media.)
     */
    public Media setDeviceTarget(Resource value) { 
      this.deviceTarget = value;
      return this;
    }

    /**
     * @return {@link #height} (Height of the image in pixels (photo/video).). This is the underlying object with id, value and extensions. The accessor "getHeight" gives direct access to the value
     */
    public PositiveIntType getHeightElement() { 
      if (this.height == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.height");
        else if (Configuration.doAutoCreate())
          this.height = new PositiveIntType(); // bb
      return this.height;
    }

    public boolean hasHeightElement() { 
      return this.height != null && !this.height.isEmpty();
    }

    public boolean hasHeight() { 
      return this.height != null && !this.height.isEmpty();
    }

    /**
     * @param value {@link #height} (Height of the image in pixels (photo/video).). This is the underlying object with id, value and extensions. The accessor "getHeight" gives direct access to the value
     */
    public Media setHeightElement(PositiveIntType value) { 
      this.height = value;
      return this;
    }

    /**
     * @return Height of the image in pixels (photo/video).
     */
    public int getHeight() { 
      return this.height == null || this.height.isEmpty() ? 0 : this.height.getValue();
    }

    /**
     * @param value Height of the image in pixels (photo/video).
     */
    public Media setHeight(int value) { 
        if (this.height == null)
          this.height = new PositiveIntType();
        this.height.setValue(value);
      return this;
    }

    /**
     * @return {@link #width} (Width of the image in pixels (photo/video).). This is the underlying object with id, value and extensions. The accessor "getWidth" gives direct access to the value
     */
    public PositiveIntType getWidthElement() { 
      if (this.width == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.width");
        else if (Configuration.doAutoCreate())
          this.width = new PositiveIntType(); // bb
      return this.width;
    }

    public boolean hasWidthElement() { 
      return this.width != null && !this.width.isEmpty();
    }

    public boolean hasWidth() { 
      return this.width != null && !this.width.isEmpty();
    }

    /**
     * @param value {@link #width} (Width of the image in pixels (photo/video).). This is the underlying object with id, value and extensions. The accessor "getWidth" gives direct access to the value
     */
    public Media setWidthElement(PositiveIntType value) { 
      this.width = value;
      return this;
    }

    /**
     * @return Width of the image in pixels (photo/video).
     */
    public int getWidth() { 
      return this.width == null || this.width.isEmpty() ? 0 : this.width.getValue();
    }

    /**
     * @param value Width of the image in pixels (photo/video).
     */
    public Media setWidth(int value) { 
        if (this.width == null)
          this.width = new PositiveIntType();
        this.width.setValue(value);
      return this;
    }

    /**
     * @return {@link #frames} (The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.). This is the underlying object with id, value and extensions. The accessor "getFrames" gives direct access to the value
     */
    public PositiveIntType getFramesElement() { 
      if (this.frames == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.frames");
        else if (Configuration.doAutoCreate())
          this.frames = new PositiveIntType(); // bb
      return this.frames;
    }

    public boolean hasFramesElement() { 
      return this.frames != null && !this.frames.isEmpty();
    }

    public boolean hasFrames() { 
      return this.frames != null && !this.frames.isEmpty();
    }

    /**
     * @param value {@link #frames} (The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.). This is the underlying object with id, value and extensions. The accessor "getFrames" gives direct access to the value
     */
    public Media setFramesElement(PositiveIntType value) { 
      this.frames = value;
      return this;
    }

    /**
     * @return The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.
     */
    public int getFrames() { 
      return this.frames == null || this.frames.isEmpty() ? 0 : this.frames.getValue();
    }

    /**
     * @param value The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.
     */
    public Media setFrames(int value) { 
        if (this.frames == null)
          this.frames = new PositiveIntType();
        this.frames.setValue(value);
      return this;
    }

    /**
     * @return {@link #duration} (The duration of the recording in seconds - for audio and video.). This is the underlying object with id, value and extensions. The accessor "getDuration" gives direct access to the value
     */
    public DecimalType getDurationElement() { 
      if (this.duration == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.duration");
        else if (Configuration.doAutoCreate())
          this.duration = new DecimalType(); // bb
      return this.duration;
    }

    public boolean hasDurationElement() { 
      return this.duration != null && !this.duration.isEmpty();
    }

    public boolean hasDuration() { 
      return this.duration != null && !this.duration.isEmpty();
    }

    /**
     * @param value {@link #duration} (The duration of the recording in seconds - for audio and video.). This is the underlying object with id, value and extensions. The accessor "getDuration" gives direct access to the value
     */
    public Media setDurationElement(DecimalType value) { 
      this.duration = value;
      return this;
    }

    /**
     * @return The duration of the recording in seconds - for audio and video.
     */
    public BigDecimal getDuration() { 
      return this.duration == null ? null : this.duration.getValue();
    }

    /**
     * @param value The duration of the recording in seconds - for audio and video.
     */
    public Media setDuration(BigDecimal value) { 
      if (value == null)
        this.duration = null;
      else {
        if (this.duration == null)
          this.duration = new DecimalType();
        this.duration.setValue(value);
      }
      return this;
    }

    /**
     * @param value The duration of the recording in seconds - for audio and video.
     */
    public Media setDuration(long value) { 
          this.duration = new DecimalType();
        this.duration.setValue(value);
      return this;
    }

    /**
     * @param value The duration of the recording in seconds - for audio and video.
     */
    public Media setDuration(double value) { 
          this.duration = new DecimalType();
        this.duration.setValue(value);
      return this;
    }

    /**
     * @return {@link #content} (The actual content of the media - inline or by direct reference to the media source file.)
     */
    public Attachment getContent() { 
      if (this.content == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.content");
        else if (Configuration.doAutoCreate())
          this.content = new Attachment(); // cc
      return this.content;
    }

    public boolean hasContent() { 
      return this.content != null && !this.content.isEmpty();
    }

    /**
     * @param value {@link #content} (The actual content of the media - inline or by direct reference to the media source file.)
     */
    public Media setContent(Attachment value) { 
      this.content = value;
      return this;
    }

    /**
     * @return {@link #note} (Comments made about the media by the performer, subject or other participants.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Media setNote(List<Annotation> theNote) { 
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

    public Media addNote(Annotation t) { //3
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

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("basedOn", "Reference(ServiceRequest|CarePlan)", "A procedure that is fulfilled in whole or in part by the creation of this media.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        children.add(new Property("partOf", "Reference(Any)", "A larger event of which this particular event is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        children.add(new Property("status", "code", "The current state of the {{title}}.", 0, 1, status));
        children.add(new Property("type", "CodeableConcept", "A code that classifies whether the media is an image, video or audio recording or some other media category.", 0, 1, type));
        children.add(new Property("modality", "CodeableConcept", "Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.", 0, 1, modality));
        children.add(new Property("view", "CodeableConcept", "The name of the imaging view e.g. Lateral or Antero-posterior (AP).", 0, 1, view));
        children.add(new Property("subject", "Reference(Patient|Practitioner|Group|Device|Specimen|Location)", "Who/What this Media is a record of.", 0, 1, subject));
        children.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter or episode of care that establishes the context for this media.", 0, 1, context));
        children.add(new Property("created[x]", "dateTime|Period", "The date and time(s) at which the media was collected.", 0, 1, created));
        children.add(new Property("issued", "instant", "The date and time this version of the media was made available to providers, typically after having been reviewed.", 0, 1, issued));
        children.add(new Property("operator", "Reference(Practitioner|PractitionerRole|Organization|CareTeam|Patient|Device|RelatedPerson)", "The person who administered the collection of the image.", 0, 1, operator));
        children.add(new Property("reasonCode", "CodeableConcept", "Describes why the event occurred in coded or textual form.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        children.add(new Property("bodySite", "CodeableConcept", "Indicates the site on the subject's body where the observation was made (i.e. the target site).", 0, 1, bodySite));
        children.add(new Property("deviceName", "string", "The name of the device / manufacturer of the device  that was used to make the recording.", 0, 1, deviceName));
        children.add(new Property("device", "Reference(Device|DeviceMetric|Device)", "The device used to collect the media.", 0, 1, device));
        children.add(new Property("height", "positiveInt", "Height of the image in pixels (photo/video).", 0, 1, height));
        children.add(new Property("width", "positiveInt", "Width of the image in pixels (photo/video).", 0, 1, width));
        children.add(new Property("frames", "positiveInt", "The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.", 0, 1, frames));
        children.add(new Property("duration", "decimal", "The duration of the recording in seconds - for audio and video.", 0, 1, duration));
        children.add(new Property("content", "Attachment", "The actual content of the media - inline or by direct reference to the media source file.", 0, 1, content));
        children.add(new Property("note", "Annotation", "Comments made about the media by the performer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -332612366: /*basedOn*/  return new Property("basedOn", "Reference(ServiceRequest|CarePlan)", "A procedure that is fulfilled in whole or in part by the creation of this media.", 0, java.lang.Integer.MAX_VALUE, basedOn);
        case -995410646: /*partOf*/  return new Property("partOf", "Reference(Any)", "A larger event of which this particular event is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf);
        case -892481550: /*status*/  return new Property("status", "code", "The current state of the {{title}}.", 0, 1, status);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "A code that classifies whether the media is an image, video or audio recording or some other media category.", 0, 1, type);
        case -622722335: /*modality*/  return new Property("modality", "CodeableConcept", "Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.", 0, 1, modality);
        case 3619493: /*view*/  return new Property("view", "CodeableConcept", "The name of the imaging view e.g. Lateral or Antero-posterior (AP).", 0, 1, view);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Practitioner|Group|Device|Specimen|Location)", "Who/What this Media is a record of.", 0, 1, subject);
        case 951530927: /*context*/  return new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter or episode of care that establishes the context for this media.", 0, 1, context);
        case 1369676952: /*created[x]*/  return new Property("created[x]", "dateTime|Period", "The date and time(s) at which the media was collected.", 0, 1, created);
        case 1028554472: /*created*/  return new Property("created[x]", "dateTime|Period", "The date and time(s) at which the media was collected.", 0, 1, created);
        case -1968526685: /*createdDateTime*/  return new Property("created[x]", "dateTime|Period", "The date and time(s) at which the media was collected.", 0, 1, created);
        case 1525027529: /*createdPeriod*/  return new Property("created[x]", "dateTime|Period", "The date and time(s) at which the media was collected.", 0, 1, created);
        case -1179159893: /*issued*/  return new Property("issued", "instant", "The date and time this version of the media was made available to providers, typically after having been reviewed.", 0, 1, issued);
        case -500553564: /*operator*/  return new Property("operator", "Reference(Practitioner|PractitionerRole|Organization|CareTeam|Patient|Device|RelatedPerson)", "The person who administered the collection of the image.", 0, 1, operator);
        case 722137681: /*reasonCode*/  return new Property("reasonCode", "CodeableConcept", "Describes why the event occurred in coded or textual form.", 0, java.lang.Integer.MAX_VALUE, reasonCode);
        case 1702620169: /*bodySite*/  return new Property("bodySite", "CodeableConcept", "Indicates the site on the subject's body where the observation was made (i.e. the target site).", 0, 1, bodySite);
        case 780988929: /*deviceName*/  return new Property("deviceName", "string", "The name of the device / manufacturer of the device  that was used to make the recording.", 0, 1, deviceName);
        case -1335157162: /*device*/  return new Property("device", "Reference(Device|DeviceMetric|Device)", "The device used to collect the media.", 0, 1, device);
        case -1221029593: /*height*/  return new Property("height", "positiveInt", "Height of the image in pixels (photo/video).", 0, 1, height);
        case 113126854: /*width*/  return new Property("width", "positiveInt", "Width of the image in pixels (photo/video).", 0, 1, width);
        case -1266514778: /*frames*/  return new Property("frames", "positiveInt", "The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.", 0, 1, frames);
        case -1992012396: /*duration*/  return new Property("duration", "decimal", "The duration of the recording in seconds - for audio and video.", 0, 1, duration);
        case 951530617: /*content*/  return new Property("content", "Attachment", "The actual content of the media - inline or by direct reference to the media source file.", 0, 1, content);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Comments made about the media by the performer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MediaStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -622722335: /*modality*/ return this.modality == null ? new Base[0] : new Base[] {this.modality}; // CodeableConcept
        case 3619493: /*view*/ return this.view == null ? new Base[0] : new Base[] {this.view}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // Type
        case -1179159893: /*issued*/ return this.issued == null ? new Base[0] : new Base[] {this.issued}; // InstantType
        case -500553564: /*operator*/ return this.operator == null ? new Base[0] : new Base[] {this.operator}; // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // CodeableConcept
        case 780988929: /*deviceName*/ return this.deviceName == null ? new Base[0] : new Base[] {this.deviceName}; // StringType
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : new Base[] {this.device}; // Reference
        case -1221029593: /*height*/ return this.height == null ? new Base[0] : new Base[] {this.height}; // PositiveIntType
        case 113126854: /*width*/ return this.width == null ? new Base[0] : new Base[] {this.width}; // PositiveIntType
        case -1266514778: /*frames*/ return this.frames == null ? new Base[0] : new Base[] {this.frames}; // PositiveIntType
        case -1992012396: /*duration*/ return this.duration == null ? new Base[0] : new Base[] {this.duration}; // DecimalType
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Attachment
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
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new MediaStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MediaStatus>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -622722335: // modality
          this.modality = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3619493: // view
          this.view = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case 1028554472: // created
          this.created = castToType(value); // Type
          return value;
        case -1179159893: // issued
          this.issued = castToInstant(value); // InstantType
          return value;
        case -500553564: // operator
          this.operator = castToReference(value); // Reference
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1702620169: // bodySite
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 780988929: // deviceName
          this.deviceName = castToString(value); // StringType
          return value;
        case -1335157162: // device
          this.device = castToReference(value); // Reference
          return value;
        case -1221029593: // height
          this.height = castToPositiveInt(value); // PositiveIntType
          return value;
        case 113126854: // width
          this.width = castToPositiveInt(value); // PositiveIntType
          return value;
        case -1266514778: // frames
          this.frames = castToPositiveInt(value); // PositiveIntType
          return value;
        case -1992012396: // duration
          this.duration = castToDecimal(value); // DecimalType
          return value;
        case 951530617: // content
          this.content = castToAttachment(value); // Attachment
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
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new MediaStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MediaStatus>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modality")) {
          this.modality = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("view")) {
          this.view = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("created[x]")) {
          this.created = castToType(value); // Type
        } else if (name.equals("issued")) {
          this.issued = castToInstant(value); // InstantType
        } else if (name.equals("operator")) {
          this.operator = castToReference(value); // Reference
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("bodySite")) {
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("deviceName")) {
          this.deviceName = castToString(value); // StringType
        } else if (name.equals("device")) {
          this.device = castToReference(value); // Reference
        } else if (name.equals("height")) {
          this.height = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("width")) {
          this.width = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("frames")) {
          this.frames = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("duration")) {
          this.duration = castToDecimal(value); // DecimalType
        } else if (name.equals("content")) {
          this.content = castToAttachment(value); // Attachment
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
        case -332612366:  return addBasedOn(); 
        case -995410646:  return addPartOf(); 
        case -892481550:  return getStatusElement();
        case 3575610:  return getType(); 
        case -622722335:  return getModality(); 
        case 3619493:  return getView(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case 1369676952:  return getCreated(); 
        case 1028554472:  return getCreated(); 
        case -1179159893:  return getIssuedElement();
        case -500553564:  return getOperator(); 
        case 722137681:  return addReasonCode(); 
        case 1702620169:  return getBodySite(); 
        case 780988929:  return getDeviceNameElement();
        case -1335157162:  return getDevice(); 
        case -1221029593:  return getHeightElement();
        case 113126854:  return getWidthElement();
        case -1266514778:  return getFramesElement();
        case -1992012396:  return getDurationElement();
        case 951530617:  return getContent(); 
        case 3387378:  return addNote(); 
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
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -622722335: /*modality*/ return new String[] {"CodeableConcept"};
        case 3619493: /*view*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case 1028554472: /*created*/ return new String[] {"dateTime", "Period"};
        case -1179159893: /*issued*/ return new String[] {"instant"};
        case -500553564: /*operator*/ return new String[] {"Reference"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case 780988929: /*deviceName*/ return new String[] {"string"};
        case -1335157162: /*device*/ return new String[] {"Reference"};
        case -1221029593: /*height*/ return new String[] {"positiveInt"};
        case 113126854: /*width*/ return new String[] {"positiveInt"};
        case -1266514778: /*frames*/ return new String[] {"positiveInt"};
        case -1992012396: /*duration*/ return new String[] {"decimal"};
        case 951530617: /*content*/ return new String[] {"Attachment"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("partOf")) {
          return addPartOf();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Media.status");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("modality")) {
          this.modality = new CodeableConcept();
          return this.modality;
        }
        else if (name.equals("view")) {
          this.view = new CodeableConcept();
          return this.view;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("createdDateTime")) {
          this.created = new DateTimeType();
          return this.created;
        }
        else if (name.equals("createdPeriod")) {
          this.created = new Period();
          return this.created;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type Media.issued");
        }
        else if (name.equals("operator")) {
          this.operator = new Reference();
          return this.operator;
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new CodeableConcept();
          return this.bodySite;
        }
        else if (name.equals("deviceName")) {
          throw new FHIRException("Cannot call addChild on a primitive type Media.deviceName");
        }
        else if (name.equals("device")) {
          this.device = new Reference();
          return this.device;
        }
        else if (name.equals("height")) {
          throw new FHIRException("Cannot call addChild on a primitive type Media.height");
        }
        else if (name.equals("width")) {
          throw new FHIRException("Cannot call addChild on a primitive type Media.width");
        }
        else if (name.equals("frames")) {
          throw new FHIRException("Cannot call addChild on a primitive type Media.frames");
        }
        else if (name.equals("duration")) {
          throw new FHIRException("Cannot call addChild on a primitive type Media.duration");
        }
        else if (name.equals("content")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Media";

  }

      public Media copy() {
        Media dst = new Media();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
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
        dst.type = type == null ? null : type.copy();
        dst.modality = modality == null ? null : modality.copy();
        dst.view = view == null ? null : view.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.created = created == null ? null : created.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.operator = operator == null ? null : operator.copy();
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        dst.deviceName = deviceName == null ? null : deviceName.copy();
        dst.device = device == null ? null : device.copy();
        dst.height = height == null ? null : height.copy();
        dst.width = width == null ? null : width.copy();
        dst.frames = frames == null ? null : frames.copy();
        dst.duration = duration == null ? null : duration.copy();
        dst.content = content == null ? null : content.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        return dst;
      }

      protected Media typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Media))
          return false;
        Media o = (Media) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(basedOn, o.basedOn, true) && compareDeep(partOf, o.partOf, true)
           && compareDeep(status, o.status, true) && compareDeep(type, o.type, true) && compareDeep(modality, o.modality, true)
           && compareDeep(view, o.view, true) && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true)
           && compareDeep(created, o.created, true) && compareDeep(issued, o.issued, true) && compareDeep(operator, o.operator, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(bodySite, o.bodySite, true) && compareDeep(deviceName, o.deviceName, true)
           && compareDeep(device, o.device, true) && compareDeep(height, o.height, true) && compareDeep(width, o.width, true)
           && compareDeep(frames, o.frames, true) && compareDeep(duration, o.duration, true) && compareDeep(content, o.content, true)
           && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Media))
          return false;
        Media o = (Media) other_;
        return compareValues(status, o.status, true) && compareValues(issued, o.issued, true) && compareValues(deviceName, o.deviceName, true)
           && compareValues(height, o.height, true) && compareValues(width, o.width, true) && compareValues(frames, o.frames, true)
           && compareValues(duration, o.duration, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, basedOn, partOf
          , status, type, modality, view, subject, context, created, issued, operator
          , reasonCode, bodySite, deviceName, device, height, width, frames, duration
          , content, note);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Media;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Identifier(s) for the image</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Media.identifier", description="Identifier(s) for the image", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Identifier(s) for the image</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>modality</b>
   * <p>
   * Description: <b>The type of acquisition equipment/process</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.modality</b><br>
   * </p>
   */
  @SearchParamDefinition(name="modality", path="Media.modality", description="The type of acquisition equipment/process", type="token" )
  public static final String SP_MODALITY = "modality";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>modality</b>
   * <p>
   * Description: <b>The type of acquisition equipment/process</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.modality</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam MODALITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_MODALITY);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who/What this Media is a record of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Media.subject", description="Who/What this Media is a record of", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Group.class, Location.class, Patient.class, Practitioner.class, Specimen.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Who/What this Media is a record of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Media:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Media:subject").toLocked();

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>When Media was collected</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Media.created[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="Media.created", description="When Media was collected", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>When Media was collected</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Media.created[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Classification of media as image, video, or audio</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Media.type", description="Classification of media as image, video, or audio", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Classification of media as image, video, or audio</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>operator</b>
   * <p>
   * Description: <b>The person who generated the image</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.operator</b><br>
   * </p>
   */
  @SearchParamDefinition(name="operator", path="Media.operator", description="The person who generated the image", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={CareTeam.class, Device.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_OPERATOR = "operator";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>operator</b>
   * <p>
   * Description: <b>The person who generated the image</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.operator</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam OPERATOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_OPERATOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Media:operator</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_OPERATOR = new ca.uhn.fhir.model.api.Include("Media:operator").toLocked();

 /**
   * Search parameter: <b>view</b>
   * <p>
   * Description: <b>Imaging view, e.g. Lateral or Antero-posterior</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.view</b><br>
   * </p>
   */
  @SearchParamDefinition(name="view", path="Media.view", description="Imaging view, e.g. Lateral or Antero-posterior", type="token" )
  public static final String SP_VIEW = "view";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>view</b>
   * <p>
   * Description: <b>Imaging view, e.g. Lateral or Antero-posterior</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.view</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VIEW = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VIEW);

 /**
   * Search parameter: <b>site</b>
   * <p>
   * Description: <b>Observed body part</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.bodySite</b><br>
   * </p>
   */
  @SearchParamDefinition(name="site", path="Media.bodySite", description="Observed body part", type="token" )
  public static final String SP_SITE = "site";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>site</b>
   * <p>
   * Description: <b>Observed body part</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.bodySite</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SITE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SITE);

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>Procedure that caused this media to be created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="Media.basedOn", description="Procedure that caused this media to be created", type="reference", target={CarePlan.class, ServiceRequest.class } )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>Procedure that caused this media to be created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Media:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("Media:based-on").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who/What this Media is a record of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Media.subject.where(resolve() is Patient)", description="Who/What this Media is a record of", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who/What this Media is a record of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Media:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Media:patient").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Encounter / Episode associated with media</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="Media.context", description="Encounter / Episode associated with media", type="reference", target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Encounter / Episode associated with media</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Media:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("Media:context").toLocked();

 /**
   * Search parameter: <b>device</b>
   * <p>
   * Description: <b>Observing Device</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.device</b><br>
   * </p>
   */
  @SearchParamDefinition(name="device", path="Media.device", description="Observing Device", type="reference", target={Device.class, DeviceMetric.class } )
  public static final String SP_DEVICE = "device";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>device</b>
   * <p>
   * Description: <b>Observing Device</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.device</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEVICE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEVICE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Media:device</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEVICE = new ca.uhn.fhir.model.api.Include("Media:device").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Media.status", description="preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error | unknown", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

