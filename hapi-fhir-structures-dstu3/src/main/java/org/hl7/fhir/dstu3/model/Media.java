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
         * added to help the parsers
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
          if (code == null || code.isEmpty())
            return null;
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
     * Whether the media is a photo (still image), an audio recording, or a video recording.
     */
    @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="photo | video | audio", formalDefinition="Whether the media is a photo (still image), an audio recording, or a video recording." )
    protected Enumeration<DigitalMediaType> type;

    /**
     * Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.
     */
    @Child(name = "subtype", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of acquisition equipment/process", formalDefinition="Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality." )
    protected CodeableConcept subtype;

    /**
     * The name of the imaging view e.g. Lateral or Antero-posterior (AP).
     */
    @Child(name = "view", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Imaging view, e.g. Lateral or Antero-posterior", formalDefinition="The name of the imaging view e.g. Lateral or Antero-posterior (AP)." )
    protected CodeableConcept view;

    /**
     * Who/What this Media is a record of.
     */
    @Child(name = "subject", type = {Patient.class, Practitioner.class, Group.class, Device.class, Specimen.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/What this Media is a record of", formalDefinition="Who/What this Media is a record of." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who/What this Media is a record of.)
     */
    protected Resource subjectTarget;

    /**
     * The person who administered the collection of the image.
     */
    @Child(name = "operator", type = {Practitioner.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The person who generated the image", formalDefinition="The person who administered the collection of the image." )
    protected Reference operator;

    /**
     * The actual object that is the target of the reference (The person who administered the collection of the image.)
     */
    protected Practitioner operatorTarget;

    /**
     * The name of the device / manufacturer of the device  that was used to make the recording.
     */
    @Child(name = "deviceName", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the device/manufacturer", formalDefinition="The name of the device / manufacturer of the device  that was used to make the recording." )
    protected StringType deviceName;

    /**
     * Height of the image in pixels (photo/video).
     */
    @Child(name = "height", type = {PositiveIntType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Height of the image in pixels (photo/video)", formalDefinition="Height of the image in pixels (photo/video)." )
    protected PositiveIntType height;

    /**
     * Width of the image in pixels (photo/video).
     */
    @Child(name = "width", type = {PositiveIntType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Width of the image in pixels (photo/video)", formalDefinition="Width of the image in pixels (photo/video)." )
    protected PositiveIntType width;

    /**
     * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.
     */
    @Child(name = "frames", type = {PositiveIntType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of frames if > 1 (photo)", formalDefinition="The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required." )
    protected PositiveIntType frames;

    /**
     * The duration of the recording in seconds - for audio and video.
     */
    @Child(name = "duration", type = {UnsignedIntType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Length in seconds (audio / video)", formalDefinition="The duration of the recording in seconds - for audio and video." )
    protected UnsignedIntType duration;

    /**
     * The actual content of the media - inline or by direct reference to the media source file.
     */
    @Child(name = "content", type = {Attachment.class}, order=11, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Actual Media - reference or data", formalDefinition="The actual content of the media - inline or by direct reference to the media source file." )
    protected Attachment content;

    private static final long serialVersionUID = -2144305643L;

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

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.)
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
    public Media addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers associated with the image - these may include identifiers for the image itself, identifiers for the context of its collection (e.g. series ids) and context ids such as accession numbers or other workflow identifiers.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "code", "Whether the media is a photo (still image), an audio recording, or a video recording.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subtype", "CodeableConcept", "Details of the type of the media - usually, how it was acquired (what type of device). If images sourced from a DICOM system, are wrapped in a Media resource, then this is the modality.", 0, java.lang.Integer.MAX_VALUE, subtype));
        childrenList.add(new Property("view", "CodeableConcept", "The name of the imaging view e.g. Lateral or Antero-posterior (AP).", 0, java.lang.Integer.MAX_VALUE, view));
        childrenList.add(new Property("subject", "Reference(Patient|Practitioner|Group|Device|Specimen)", "Who/What this Media is a record of.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("operator", "Reference(Practitioner)", "The person who administered the collection of the image.", 0, java.lang.Integer.MAX_VALUE, operator));
        childrenList.add(new Property("deviceName", "string", "The name of the device / manufacturer of the device  that was used to make the recording.", 0, java.lang.Integer.MAX_VALUE, deviceName));
        childrenList.add(new Property("height", "positiveInt", "Height of the image in pixels (photo/video).", 0, java.lang.Integer.MAX_VALUE, height));
        childrenList.add(new Property("width", "positiveInt", "Width of the image in pixels (photo/video).", 0, java.lang.Integer.MAX_VALUE, width));
        childrenList.add(new Property("frames", "positiveInt", "The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif. If there is more than one frame, this SHALL have a value in order to alert interface software that a multi-frame capable rendering widget is required.", 0, java.lang.Integer.MAX_VALUE, frames));
        childrenList.add(new Property("duration", "unsignedInt", "The duration of the recording in seconds - for audio and video.", 0, java.lang.Integer.MAX_VALUE, duration));
        childrenList.add(new Property("content", "Attachment", "The actual content of the media - inline or by direct reference to the media source file.", 0, java.lang.Integer.MAX_VALUE, content));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("type"))
          this.type = new DigitalMediaTypeEnumFactory().fromType(value); // Enumeration<DigitalMediaType>
        else if (name.equals("subtype"))
          this.subtype = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("view"))
          this.view = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("operator"))
          this.operator = castToReference(value); // Reference
        else if (name.equals("deviceName"))
          this.deviceName = castToString(value); // StringType
        else if (name.equals("height"))
          this.height = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("width"))
          this.width = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("frames"))
          this.frames = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("duration"))
          this.duration = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("content"))
          this.content = castToAttachment(value); // Attachment
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
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
        else if (name.equals("operator")) {
          this.operator = new Reference();
          return this.operator;
        }
        else if (name.equals("deviceName")) {
          throw new FHIRException("Cannot call addChild on a primitive type Media.deviceName");
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
        dst.type = type == null ? null : type.copy();
        dst.subtype = subtype == null ? null : subtype.copy();
        dst.view = view == null ? null : view.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.operator = operator == null ? null : operator.copy();
        dst.deviceName = deviceName == null ? null : deviceName.copy();
        dst.height = height == null ? null : height.copy();
        dst.width = width == null ? null : width.copy();
        dst.frames = frames == null ? null : frames.copy();
        dst.duration = duration == null ? null : duration.copy();
        dst.content = content == null ? null : content.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(subtype, o.subtype, true)
           && compareDeep(view, o.view, true) && compareDeep(subject, o.subject, true) && compareDeep(operator, o.operator, true)
           && compareDeep(deviceName, o.deviceName, true) && compareDeep(height, o.height, true) && compareDeep(width, o.width, true)
           && compareDeep(frames, o.frames, true) && compareDeep(duration, o.duration, true) && compareDeep(content, o.content, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Media))
          return false;
        Media o = (Media) other;
        return compareValues(type, o.type, true) && compareValues(deviceName, o.deviceName, true) && compareValues(height, o.height, true)
           && compareValues(width, o.width, true) && compareValues(frames, o.frames, true) && compareValues(duration, o.duration, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (subtype == null || subtype.isEmpty()) && (view == null || view.isEmpty()) && (subject == null || subject.isEmpty())
           && (operator == null || operator.isEmpty()) && (deviceName == null || deviceName.isEmpty())
           && (height == null || height.isEmpty()) && (width == null || width.isEmpty()) && (frames == null || frames.isEmpty())
           && (duration == null || duration.isEmpty()) && (content == null || content.isEmpty());
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
  @SearchParamDefinition(name="subject", path="Media.subject", description="Who/What this Media is a record of", type="reference" )
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
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who/What this Media is a record of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Media.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Media.subject", description="Who/What this Media is a record of", type="reference" )
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
  @SearchParamDefinition(name="operator", path="Media.operator", description="The person who generated the image", type="reference" )
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


}

