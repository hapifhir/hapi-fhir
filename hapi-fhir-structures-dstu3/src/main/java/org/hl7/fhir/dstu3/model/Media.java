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
 * A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by direct reference.
 */
@ResourceDef(name="Media", profile="http://hl7.org/fhir/Profile/Media")
public class Media extends DomainResource {

    public enum DigitalMediaType {
        /**
         * The media consists of one or more unmoving images, including photographs, computer-generated graphs and charts, and scanned documents
         */
        PHOTO, 
        /**
         * The media consists of a series of frames that capture a moving image
         */
        VIDEO, 
        /**
         * The media consists of a sound recording
         */
        AUDIO, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DigitalMediaType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("photo".equals(codeString))
          return PHOTO;
        if ("video".equals(codeString))
          return VIDEO;
        if ("audio".equals(codeString))
          return AUDIO;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DigitalMediaType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PHOTO: return "photo";
            case VIDEO: return "video";
            case AUDIO: return "audio";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PHOTO: return "http://hl7.org/fhir/digital-media-type";
            case VIDEO: return "http://hl7.org/fhir/digital-media-type";
            case AUDIO: return "http://hl7.org/fhir/digital-media-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PHOTO: return "The media consists of one or more unmoving images, including photographs, computer-generated graphs and charts, and scanned documents";
            case VIDEO: return "The media consists of a series of frames that capture a moving image";
            case AUDIO: return "The media consists of a sound recording";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PHOTO: return "Photo";
            case VIDEO: return "Video";
            case AUDIO: return "Audio";
            default: return "?";
          }
        }
    }

  public static class DigitalMediaTypeEnumFactory implements EnumFactory<DigitalMediaType> {
    public DigitalMediaType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("photo".equals(codeString))
          return DigitalMediaType.PHOTO;
        if ("video".equals(codeString))
          return DigitalMediaType.VIDEO;
        if ("audio".equals(codeString))
          return DigitalMediaType.AUDIO;
        throw new IllegalArgumentException("Unknown DigitalMediaType code '"+codeString+"'");
        }
        public Enumeration<DigitalMediaType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DigitalMediaType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("photo".equals(codeString))
          return new Enumeration<DigitalMediaType>(this, DigitalMediaType.PHOTO);
        if ("video".equals(codeString))
          return new Enumeration<DigitalMediaType>(this, DigitalMediaType.VIDEO);
        if ("audio".equals(codeString))
          return new Enumeration<DigitalMediaType>(this, DigitalMediaType.AUDIO);
        throw new FHIRException("Unknown DigitalMediaType code '"+codeString+"'");
        }
    public String toCode(DigitalMediaType code) {
      if (code == DigitalMediaType.PHOTO)
        return "photo";
      if (code == DigitalMediaType.VIDEO)
        return "video";
      if (code == DigitalMediaType.AUDIO)
        return "audio";
      return "?";
      }
    public String toSystem(DigitalMediaType code) {
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
    @Child(name = "basedOn", type = {ProcedureRequest.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Procedure that caused this media to be created", formalDefinition="A procedure that is fulfilled in whole or in part by the creation of this media." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (A procedure that is fulfilled in whole or in part by the creation of this media.)
     */
    protected List<ProcedureRequest> basedOnTarget;


    /**
     * Whether the media is a photo (still image), an audio recording, or a video recording.
     */
    @Child(name = "type", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="photo | video | audio", formalDefinition="Whether the media is a photo (still image), an audio recording, or a video recording." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/digital-media-type")
    protected Enumeration<DigitalMediaType> type;

    /**
     * Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.
     */
    @Child(name = "subtype", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of acquisition equipment/process", formalDefinition="Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/digital-media-subtype")
    protected CodeableConcept subtype;

    /**
     * The name of the imaging view e.g. Lateral or Antero-posterior (AP).
     */
    @Child(name = "view", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Imaging view, e.g. Lateral or Antero-posterior", formalDefinition="The name of the imaging view e.g. Lateral or Antero-posterior (AP)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/media-view")
    protected CodeableConcept view;

    /**
     * Who/What this Media is a record of.
     */
    @Child(name = "subject", type = {Patient.class, Practitioner.class, Group.class, Device.class, Specimen.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/What this Media is a record of", formalDefinition="Who/What this Media is a record of." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who/What this Media is a record of.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter or episode of care that establishes the context for this media.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter / Episode associated with media", formalDefinition="The encounter or episode of care that establishes the context for this media." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter or episode of care that establishes the context for this media.)
     */
    protected Resource contextTarget;

    /**
     * The date and time(s) at which the media was collected.
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When Media was collected", formalDefinition="The date and time(s) at which the media was collected." )
    protected Type occurrence;

    /**
     * The person who administered the collection of the image.
     */
    @Child(name = "operator", type = {Practitioner.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The person who generated the image", formalDefinition="The person who administered the collection of the image." )
    protected Reference operator;

    /**
     * The actual object that is the target of the reference (The person who administered the collection of the image.)
     */
    protected Practitioner operatorTarget;

    /**
     * Describes why the event occurred in coded or textual form.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Why was event performed?", formalDefinition="Describes why the event occurred in coded or textual form." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-reason")
    protected List<CodeableConcept> reasonCode;

    /**
     * Indicates the site on the subject's body where the media was collected (i.e. the target site).
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Body part in media", formalDefinition="Indicates the site on the subject's body where the media was collected (i.e. the target site)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected CodeableConcept bodySite;

    /**
     * The device used to collect the media.
     */
    @Child(name = "device", type = {Device.class, DeviceMetric.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Observing Device", formalDefinition="The device used to collect the media." )
    protected Reference device;

    /**
     * The actual object that is the target of the reference (The device used to collect the media.)
     */
    protected Resource deviceTarget;

    /**
     * Height of the image in pixels (photo/video).
     */
    @Child(name = "height", type = {PositiveIntType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Height of the image in pixels (photo/video)", formalDefinition="Height of the image in pixels (photo/video)." )
    protected PositiveIntType height;

    /**
     * Width of the image in pixels (photo/video).
     */
    @Child(name = "width", type = {PositiveIntType.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Width of the image in pixels (photo/video)", formalDefinition="Width of the image in pixels (photo/video)." )
    protected PositiveIntType width;

    /**
     * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.
     */
    @Child(name = "frames", type = {PositiveIntType.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of frames if > 1 (photo)", formalDefinition="The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required." )
    protected PositiveIntType frames;

    /**
     * The duration of the recording in seconds - for audio and video.
     */
    @Child(name = "duration", type = {UnsignedIntType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Length in seconds (audio / video)", formalDefinition="The duration of the recording in seconds - for audio and video." )
    protected UnsignedIntType duration;

    /**
     * The actual content of the media - inline or by direct reference to the media source file.
     */
    @Child(name = "content", type = {Attachment.class}, order=16, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Actual Media - reference or data", formalDefinition="The actual content of the media - inline or by direct reference to the media source file." )
    protected Attachment content;

    /**
     * Comments made about the media by the performer, subject or other participants.
     */
    @Child(name = "note", type = {Annotation.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the media", formalDefinition="Comments made about the media by the performer, subject or other participants." )
    protected List<Annotation> note;

    private static final long serialVersionUID = -831317677L;

  /**
   * Constructor
   */
    public Media() {
      super();
    }

  /**
   * Constructor
   */
    public Media(Enumeration<DigitalMediaType> type, Attachment content) {
      super();
      this.type = type;
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
    public List<ProcedureRequest> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<ProcedureRequest>();
      return this.basedOnTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ProcedureRequest addBasedOnTarget() { 
      ProcedureRequest r = new ProcedureRequest();
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<ProcedureRequest>();
      this.basedOnTarget.add(r);
      return r;
    }

    /**
     * @return {@link #type} (Whether the media is a photo (still image), an audio recording, or a video recording.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<DigitalMediaType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<DigitalMediaType>(new DigitalMediaTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Whether the media is a photo (still image), an audio recording, or a video recording.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Media setTypeElement(Enumeration<DigitalMediaType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Whether the media is a photo (still image), an audio recording, or a video recording.
     */
    public DigitalMediaType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Whether the media is a photo (still image), an audio recording, or a video recording.
     */
    public Media setType(DigitalMediaType value) { 
        if (this.type == null)
          this.type = new Enumeration<DigitalMediaType>(new DigitalMediaTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #subtype} (Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.)
     */
    public CodeableConcept getSubtype() { 
      if (this.subtype == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.subtype");
        else if (Configuration.doAutoCreate())
          this.subtype = new CodeableConcept(); // cc
      return this.subtype;
    }

    public boolean hasSubtype() { 
      return this.subtype != null && !this.subtype.isEmpty();
    }

    /**
     * @param value {@link #subtype} (Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.)
     */
    public Media setSubtype(CodeableConcept value) { 
      this.subtype = value;
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
     * @return {@link #occurrence} (The date and time(s) at which the media was collected.)
     */
    public Type getOccurrence() { 
      return this.occurrence;
    }

    /**
     * @return {@link #occurrence} (The date and time(s) at which the media was collected.)
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
     * @return {@link #occurrence} (The date and time(s) at which the media was collected.)
     */
    public Period getOccurrencePeriod() throws FHIRException { 
      if (!(this.occurrence instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Period) this.occurrence;
    }

    public boolean hasOccurrencePeriod() { 
      return this.occurrence instanceof Period;
    }

    public boolean hasOccurrence() { 
      return this.occurrence != null && !this.occurrence.isEmpty();
    }

    /**
     * @param value {@link #occurrence} (The date and time(s) at which the media was collected.)
     */
    public Media setOccurrence(Type value) { 
      this.occurrence = value;
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
    public Practitioner getOperatorTarget() { 
      if (this.operatorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.operator");
        else if (Configuration.doAutoCreate())
          this.operatorTarget = new Practitioner(); // aa
      return this.operatorTarget;
    }

    /**
     * @param value {@link #operator} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person who administered the collection of the image.)
     */
    public Media setOperatorTarget(Practitioner value) { 
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
     * @return {@link #bodySite} (Indicates the site on the subject's body where the media was collected (i.e. the target site).)
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
     * @param value {@link #bodySite} (Indicates the site on the subject's body where the media was collected (i.e. the target site).)
     */
    public Media setBodySite(CodeableConcept value) { 
      this.bodySite = value;
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
    public UnsignedIntType getDurationElement() { 
      if (this.duration == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Media.duration");
        else if (Configuration.doAutoCreate())
          this.duration = new UnsignedIntType(); // bb
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
    public Media setDurationElement(UnsignedIntType value) { 
      this.duration = value;
      return this;
    }

    /**
     * @return The duration of the recording in seconds - for audio and video.
     */
    public int getDuration() { 
      return this.duration == null || this.duration.isEmpty() ? 0 : this.duration.getValue();
    }

    /**
     * @param value The duration of the recording in seconds - for audio and video.
     */
    public Media setDuration(int value) { 
        if (this.duration == null)
          this.duration = new UnsignedIntType();
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("basedOn", "Reference(ProcedureRequest)", "A procedure that is fulfilled in whole or in part by the creation of this media.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("type", "code", "Whether the media is a photo (still image), an audio recording, or a video recording.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subtype", "CodeableConcept", "Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.", 0, java.lang.Integer.MAX_VALUE, subtype));
        childrenList.add(new Property("view", "CodeableConcept", "The name of the imaging view e.g. Lateral or Antero-posterior (AP).", 0, java.lang.Integer.MAX_VALUE, view));
        childrenList.add(new Property("subject", "Reference(Patient|Practitioner|Group|Device|Specimen)", "Who/What this Media is a record of.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter or episode of care that establishes the context for this media.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("occurrence[x]", "dateTime|Period", "The date and time(s) at which the media was collected.", 0, java.lang.Integer.MAX_VALUE, occurrence));
        childrenList.add(new Property("operator", "Reference(Practitioner)", "The person who administered the collection of the image.", 0, java.lang.Integer.MAX_VALUE, operator));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "Describes why the event occurred in coded or textual form.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("bodySite", "CodeableConcept", "Indicates the site on the subject's body where the media was collected (i.e. the target site).", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("device", "Reference(Device|DeviceMetric)", "The device used to collect the media.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("height", "positiveInt", "Height of the image in pixels (photo/video).", 0, java.lang.Integer.MAX_VALUE, height));
        childrenList.add(new Property("width", "positiveInt", "Width of the image in pixels (photo/video).", 0, java.lang.Integer.MAX_VALUE, width));
        childrenList.add(new Property("frames", "positiveInt", "The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.", 0, java.lang.Integer.MAX_VALUE, frames));
        childrenList.add(new Property("duration", "unsignedInt", "The duration of the recording in seconds - for audio and video.", 0, java.lang.Integer.MAX_VALUE, duration));
        childrenList.add(new Property("content", "Attachment", "The actual content of the media - inline or by direct reference to the media source file.", 0, java.lang.Integer.MAX_VALUE, content));
        childrenList.add(new Property("note", "Annotation", "Comments made about the media by the performer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<DigitalMediaType>
        case -1867567750: /*subtype*/ return this.subtype == null ? new Base[0] : new Base[] {this.subtype}; // CodeableConcept
        case 3619493: /*view*/ return this.view == null ? new Base[0] : new Base[] {this.view}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case -500553564: /*operator*/ return this.operator == null ? new Base[0] : new Base[] {this.operator}; // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // CodeableConcept
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : new Base[] {this.device}; // Reference
        case -1221029593: /*height*/ return this.height == null ? new Base[0] : new Base[] {this.height}; // PositiveIntType
        case 113126854: /*width*/ return this.width == null ? new Base[0] : new Base[] {this.width}; // PositiveIntType
        case -1266514778: /*frames*/ return this.frames == null ? new Base[0] : new Base[] {this.frames}; // PositiveIntType
        case -1992012396: /*duration*/ return this.duration == null ? new Base[0] : new Base[] {this.duration}; // UnsignedIntType
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
        case 3575610: // type
          value = new DigitalMediaTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<DigitalMediaType>
          return value;
        case -1867567750: // subtype
          this.subtype = castToCodeableConcept(value); // CodeableConcept
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
        case 1687874001: // occurrence
          this.occurrence = castToType(value); // Type
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
          this.duration = castToUnsignedInt(value); // UnsignedIntType
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
        } else if (name.equals("type")) {
          value = new DigitalMediaTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<DigitalMediaType>
        } else if (name.equals("subtype")) {
          this.subtype = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("view")) {
          this.view = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("occurrence[x]")) {
          this.occurrence = castToType(value); // Type
        } else if (name.equals("operator")) {
          this.operator = castToReference(value); // Reference
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("bodySite")) {
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("device")) {
          this.device = castToReference(value); // Reference
        } else if (name.equals("height")) {
          this.height = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("width")) {
          this.width = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("frames")) {
          this.frames = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("duration")) {
          this.duration = castToUnsignedInt(value); // UnsignedIntType
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
        case 3575610:  return getTypeElement();
        case -1867567750:  return getSubtype(); 
        case 3619493:  return getView(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -2022646513:  return getOccurrence(); 
        case 1687874001:  return getOccurrence(); 
        case -500553564:  return getOperator(); 
        case 722137681:  return addReasonCode(); 
        case 1702620169:  return getBodySite(); 
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
        case 3575610: /*type*/ return new String[] {"code"};
        case -1867567750: /*subtype*/ return new String[] {"CodeableConcept"};
        case 3619493: /*view*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case 1687874001: /*occurrence*/ return new String[] {"dateTime", "Period"};
        case -500553564: /*operator*/ return new String[] {"Reference"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case -1335157162: /*device*/ return new String[] {"Reference"};
        case -1221029593: /*height*/ return new String[] {"positiveInt"};
        case 113126854: /*width*/ return new String[] {"positiveInt"};
        case -1266514778: /*frames*/ return new String[] {"positiveInt"};
        case -1992012396: /*duration*/ return new String[] {"unsignedInt"};
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
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Media.type");
        }
        else if (name.equals("subtype")) {
          this.subtype = new CodeableConcept();
          return this.subtype;
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
        else if (name.equals("occurrenceDateTime")) {
          this.occurrence = new DateTimeType();
          return this.occurrence;
        }
        else if (name.equals("occurrencePeriod")) {
          this.occurrence = new Period();
          return this.occurrence;
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
        dst.type = type == null ? null : type.copy();
        dst.subtype = subtype == null ? null : subtype.copy();
        dst.view = view == null ? null : view.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        dst.operator = operator == null ? null : operator.copy();
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        dst.bodySite = bodySite == null ? null : bodySite.copy();
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Media))
          return false;
        Media o = (Media) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(basedOn, o.basedOn, true) && compareDeep(type, o.type, true)
           && compareDeep(subtype, o.subtype, true) && compareDeep(view, o.view, true) && compareDeep(subject, o.subject, true)
           && compareDeep(context, o.context, true) && compareDeep(occurrence, o.occurrence, true) && compareDeep(operator, o.operator, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(bodySite, o.bodySite, true) && compareDeep(device, o.device, true)
           && compareDeep(height, o.height, true) && compareDeep(width, o.width, true) && compareDeep(frames, o.frames, true)
           && compareDeep(duration, o.duration, true) && compareDeep(content, o.content, true) && compareDeep(note, o.note, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Media))
          return false;
        Media o = (Media) other;
        return compareValues(type, o.type, true) && compareValues(height, o.height, true) && compareValues(width, o.width, true)
           && compareValues(frames, o.frames, true) && compareValues(duration, o.duration, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, basedOn, type
          , subtype, view, subject, context, occurrence, operator, reasonCode, bodySite
          , device, height, width, frames, duration, content, note);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Media;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When Media was collected</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Media.occurrence[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Media.occurrence", description="When Media was collected", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When Media was collected</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Media.occurrence[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

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
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>Date attachment was first created</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Media.content.creation</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="Media.content.creation", description="Date attachment was first created", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>Date attachment was first created</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Media.content.creation</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who/What this Media is a record of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Media.subject", description="Who/What this Media is a record of", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Group.class, Patient.class, Practitioner.class, Specimen.class } )
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
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>photo | video | audio</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Media.type", description="photo | video | audio", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>photo | video | audio</b><br>
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
  @SearchParamDefinition(name="operator", path="Media.operator", description="The person who generated the image", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
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
   * Description: <b>Body part in media</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.bodySite</b><br>
   * </p>
   */
  @SearchParamDefinition(name="site", path="Media.bodySite", description="Body part in media", type="token" )
  public static final String SP_SITE = "site";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>site</b>
   * <p>
   * Description: <b>Body part in media</b><br>
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
  @SearchParamDefinition(name="based-on", path="Media.basedOn", description="Procedure that caused this media to be created", type="reference", target={ProcedureRequest.class } )
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
   * Search parameter: <b>subtype</b>
   * <p>
   * Description: <b>The type of acquisition equipment/process</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.subtype</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subtype", path="Media.subtype", description="The type of acquisition equipment/process", type="token" )
  public static final String SP_SUBTYPE = "subtype";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subtype</b>
   * <p>
   * Description: <b>The type of acquisition equipment/process</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Media.subtype</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SUBTYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SUBTYPE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who/What this Media is a record of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Media.subject", description="Who/What this Media is a record of", type="reference", target={Patient.class } )
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


}

