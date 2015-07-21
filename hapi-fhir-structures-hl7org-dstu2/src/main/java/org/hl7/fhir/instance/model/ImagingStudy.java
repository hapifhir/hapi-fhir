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

// Generated on Tue, Jul 21, 2015 10:37-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * Representation of the content produced in a DICOM imaging study. A study comprises a set of Series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A Series is of only one modality (e.g., X-ray, CT, MR, ultrasound), but a Study may have multiple Series of different modalities.
 */
@ResourceDef(name="ImagingStudy", profile="http://hl7.org/fhir/Profile/ImagingStudy")
public class ImagingStudy extends DomainResource {

    public enum InstanceAvailability {
        /**
         * null
         */
        ONLINE, 
        /**
         * null
         */
        OFFLINE, 
        /**
         * null
         */
        NEARLINE, 
        /**
         * null
         */
        UNAVAILABLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static InstanceAvailability fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ONLINE".equals(codeString))
          return ONLINE;
        if ("OFFLINE".equals(codeString))
          return OFFLINE;
        if ("NEARLINE".equals(codeString))
          return NEARLINE;
        if ("UNAVAILABLE".equals(codeString))
          return UNAVAILABLE;
        throw new Exception("Unknown InstanceAvailability code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ONLINE: return "ONLINE";
            case OFFLINE: return "OFFLINE";
            case NEARLINE: return "NEARLINE";
            case UNAVAILABLE: return "UNAVAILABLE";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ONLINE: return "http://nema.org/dicom/dicm";
            case OFFLINE: return "http://nema.org/dicom/dicm";
            case NEARLINE: return "http://nema.org/dicom/dicm";
            case UNAVAILABLE: return "http://nema.org/dicom/dicm";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ONLINE: return "";
            case OFFLINE: return "";
            case NEARLINE: return "";
            case UNAVAILABLE: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ONLINE: return "ONLINE";
            case OFFLINE: return "OFFLINE";
            case NEARLINE: return "NEARLINE";
            case UNAVAILABLE: return "UNAVAILABLE";
            default: return "?";
          }
        }
    }

  public static class InstanceAvailabilityEnumFactory implements EnumFactory<InstanceAvailability> {
    public InstanceAvailability fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ONLINE".equals(codeString))
          return InstanceAvailability.ONLINE;
        if ("OFFLINE".equals(codeString))
          return InstanceAvailability.OFFLINE;
        if ("NEARLINE".equals(codeString))
          return InstanceAvailability.NEARLINE;
        if ("UNAVAILABLE".equals(codeString))
          return InstanceAvailability.UNAVAILABLE;
        throw new IllegalArgumentException("Unknown InstanceAvailability code '"+codeString+"'");
        }
    public String toCode(InstanceAvailability code) {
      if (code == InstanceAvailability.ONLINE)
        return "ONLINE";
      if (code == InstanceAvailability.OFFLINE)
        return "OFFLINE";
      if (code == InstanceAvailability.NEARLINE)
        return "NEARLINE";
      if (code == InstanceAvailability.UNAVAILABLE)
        return "UNAVAILABLE";
      return "?";
      }
    }

    @Block()
    public static class ImagingStudySeriesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The Numeric identifier of this series in the study.
         */
        @Child(name = "number", type = {UnsignedIntType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Numeric identifier of this series (0020,0011)", formalDefinition="The Numeric identifier of this series in the study." )
        protected UnsignedIntType number;

        /**
         * The modality of this series sequence.
         */
        @Child(name = "modality", type = {Coding.class}, order=2, min=1, max=1)
        @Description(shortDefinition="The modality of the instances in the series (0008,0060)", formalDefinition="The modality of this series sequence." )
        protected Coding modality;

        /**
         * Formal identifier for this series.
         */
        @Child(name = "uid", type = {OidType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Formal identifier for this series (0020,000E)", formalDefinition="Formal identifier for this series." )
        protected OidType uid;

        /**
         * A description of the series.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="A description of the series (0008,103E)", formalDefinition="A description of the series." )
        protected StringType description;

        /**
         * Number of SOP Instances in Series.
         */
        @Child(name = "numberOfInstances", type = {UnsignedIntType.class}, order=5, min=1, max=1)
        @Description(shortDefinition="Number of Series Related Instances (0020,1209)", formalDefinition="Number of SOP Instances in Series." )
        protected UnsignedIntType numberOfInstances;

        /**
         * Availability of series (online, offline or nearline).
         */
        @Child(name = "availability", type = {CodeType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056)", formalDefinition="Availability of series (online, offline or nearline)." )
        protected Enumeration<InstanceAvailability> availability;

        /**
         * URI/URL specifying the location of the referenced series using WADO-RS.
         */
        @Child(name = "url", type = {UriType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Location of the referenced instance(s) (0008,1115 in 0008,1190)", formalDefinition="URI/URL specifying the location of the referenced series using WADO-RS." )
        protected UriType url;

        /**
         * Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed.
         */
        @Child(name = "bodySite", type = {Coding.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Body part examined (Map from 0018,0015)", formalDefinition="Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed." )
        protected Coding bodySite;

        /**
         * Laterality if bodySite is paired anatomic structure and laterality is not pre-coordinated in bodySite code, map from (0020, 0060).
         */
        @Child(name = "laterality", type = {Coding.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Body part laterality", formalDefinition="Laterality if bodySite is paired anatomic structure and laterality is not pre-coordinated in bodySite code, map from (0020, 0060)." )
        protected Coding laterality;

        /**
         * The date and time when the series was started.
         */
        @Child(name = "dateTime", type = {DateTimeType.class}, order=10, min=0, max=1)
        @Description(shortDefinition="When the series started", formalDefinition="The date and time when the series was started." )
        protected DateTimeType dateTime;

        /**
         * A single SOP Instance within the series, e.g., an image, or presentation state.
         */
        @Child(name = "instance", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="A single instance taken from a patient (image or other)", formalDefinition="A single SOP Instance within the series, e.g., an image, or presentation state." )
        protected List<ImagingStudySeriesInstanceComponent> instance;

        private static final long serialVersionUID = -873161275L;

    /*
     * Constructor
     */
      public ImagingStudySeriesComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImagingStudySeriesComponent(Coding modality, OidType uid, UnsignedIntType numberOfInstances) {
        super();
        this.modality = modality;
        this.uid = uid;
        this.numberOfInstances = numberOfInstances;
      }

        /**
         * @return {@link #number} (The Numeric identifier of this series in the study.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public UnsignedIntType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new UnsignedIntType(); // bb
          return this.number;
        }

        public boolean hasNumberElement() { 
          return this.number != null && !this.number.isEmpty();
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (The Numeric identifier of this series in the study.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public ImagingStudySeriesComponent setNumberElement(UnsignedIntType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return The Numeric identifier of this series in the study.
         */
        public int getNumber() { 
          return this.number == null || this.number.isEmpty() ? 0 : this.number.getValue();
        }

        /**
         * @param value The Numeric identifier of this series in the study.
         */
        public ImagingStudySeriesComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new UnsignedIntType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #modality} (The modality of this series sequence.)
         */
        public Coding getModality() { 
          if (this.modality == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.modality");
            else if (Configuration.doAutoCreate())
              this.modality = new Coding(); // cc
          return this.modality;
        }

        public boolean hasModality() { 
          return this.modality != null && !this.modality.isEmpty();
        }

        /**
         * @param value {@link #modality} (The modality of this series sequence.)
         */
        public ImagingStudySeriesComponent setModality(Coding value) { 
          this.modality = value;
          return this;
        }

        /**
         * @return {@link #uid} (Formal identifier for this series.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.uid");
            else if (Configuration.doAutoCreate())
              this.uid = new OidType(); // bb
          return this.uid;
        }

        public boolean hasUidElement() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        public boolean hasUid() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        /**
         * @param value {@link #uid} (Formal identifier for this series.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public ImagingStudySeriesComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return Formal identifier for this series.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value Formal identifier for this series.
         */
        public ImagingStudySeriesComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (A description of the series.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A description of the series.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImagingStudySeriesComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A description of the series.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A description of the series.
         */
        public ImagingStudySeriesComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #numberOfInstances} (Number of SOP Instances in Series.). This is the underlying object with id, value and extensions. The accessor "getNumberOfInstances" gives direct access to the value
         */
        public UnsignedIntType getNumberOfInstancesElement() { 
          if (this.numberOfInstances == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.numberOfInstances");
            else if (Configuration.doAutoCreate())
              this.numberOfInstances = new UnsignedIntType(); // bb
          return this.numberOfInstances;
        }

        public boolean hasNumberOfInstancesElement() { 
          return this.numberOfInstances != null && !this.numberOfInstances.isEmpty();
        }

        public boolean hasNumberOfInstances() { 
          return this.numberOfInstances != null && !this.numberOfInstances.isEmpty();
        }

        /**
         * @param value {@link #numberOfInstances} (Number of SOP Instances in Series.). This is the underlying object with id, value and extensions. The accessor "getNumberOfInstances" gives direct access to the value
         */
        public ImagingStudySeriesComponent setNumberOfInstancesElement(UnsignedIntType value) { 
          this.numberOfInstances = value;
          return this;
        }

        /**
         * @return Number of SOP Instances in Series.
         */
        public int getNumberOfInstances() { 
          return this.numberOfInstances == null || this.numberOfInstances.isEmpty() ? 0 : this.numberOfInstances.getValue();
        }

        /**
         * @param value Number of SOP Instances in Series.
         */
        public ImagingStudySeriesComponent setNumberOfInstances(int value) { 
            if (this.numberOfInstances == null)
              this.numberOfInstances = new UnsignedIntType();
            this.numberOfInstances.setValue(value);
          return this;
        }

        /**
         * @return {@link #availability} (Availability of series (online, offline or nearline).). This is the underlying object with id, value and extensions. The accessor "getAvailability" gives direct access to the value
         */
        public Enumeration<InstanceAvailability> getAvailabilityElement() { 
          if (this.availability == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.availability");
            else if (Configuration.doAutoCreate())
              this.availability = new Enumeration<InstanceAvailability>(new InstanceAvailabilityEnumFactory()); // bb
          return this.availability;
        }

        public boolean hasAvailabilityElement() { 
          return this.availability != null && !this.availability.isEmpty();
        }

        public boolean hasAvailability() { 
          return this.availability != null && !this.availability.isEmpty();
        }

        /**
         * @param value {@link #availability} (Availability of series (online, offline or nearline).). This is the underlying object with id, value and extensions. The accessor "getAvailability" gives direct access to the value
         */
        public ImagingStudySeriesComponent setAvailabilityElement(Enumeration<InstanceAvailability> value) { 
          this.availability = value;
          return this;
        }

        /**
         * @return Availability of series (online, offline or nearline).
         */
        public InstanceAvailability getAvailability() { 
          return this.availability == null ? null : this.availability.getValue();
        }

        /**
         * @param value Availability of series (online, offline or nearline).
         */
        public ImagingStudySeriesComponent setAvailability(InstanceAvailability value) { 
          if (value == null)
            this.availability = null;
          else {
            if (this.availability == null)
              this.availability = new Enumeration<InstanceAvailability>(new InstanceAvailabilityEnumFactory());
            this.availability.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #url} (URI/URL specifying the location of the referenced series using WADO-RS.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.url");
            else if (Configuration.doAutoCreate())
              this.url = new UriType(); // bb
          return this.url;
        }

        public boolean hasUrlElement() { 
          return this.url != null && !this.url.isEmpty();
        }

        public boolean hasUrl() { 
          return this.url != null && !this.url.isEmpty();
        }

        /**
         * @param value {@link #url} (URI/URL specifying the location of the referenced series using WADO-RS.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public ImagingStudySeriesComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return URI/URL specifying the location of the referenced series using WADO-RS.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value URI/URL specifying the location of the referenced series using WADO-RS.
         */
        public ImagingStudySeriesComponent setUrl(String value) { 
          if (Utilities.noString(value))
            this.url = null;
          else {
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #bodySite} (Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed.)
         */
        public Coding getBodySite() { 
          if (this.bodySite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.bodySite");
            else if (Configuration.doAutoCreate())
              this.bodySite = new Coding(); // cc
          return this.bodySite;
        }

        public boolean hasBodySite() { 
          return this.bodySite != null && !this.bodySite.isEmpty();
        }

        /**
         * @param value {@link #bodySite} (Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed.)
         */
        public ImagingStudySeriesComponent setBodySite(Coding value) { 
          this.bodySite = value;
          return this;
        }

        /**
         * @return {@link #laterality} (Laterality if bodySite is paired anatomic structure and laterality is not pre-coordinated in bodySite code, map from (0020, 0060).)
         */
        public Coding getLaterality() { 
          if (this.laterality == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.laterality");
            else if (Configuration.doAutoCreate())
              this.laterality = new Coding(); // cc
          return this.laterality;
        }

        public boolean hasLaterality() { 
          return this.laterality != null && !this.laterality.isEmpty();
        }

        /**
         * @param value {@link #laterality} (Laterality if bodySite is paired anatomic structure and laterality is not pre-coordinated in bodySite code, map from (0020, 0060).)
         */
        public ImagingStudySeriesComponent setLaterality(Coding value) { 
          this.laterality = value;
          return this;
        }

        /**
         * @return {@link #dateTime} (The date and time when the series was started.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public DateTimeType getDateTimeElement() { 
          if (this.dateTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.dateTime");
            else if (Configuration.doAutoCreate())
              this.dateTime = new DateTimeType(); // bb
          return this.dateTime;
        }

        public boolean hasDateTimeElement() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        public boolean hasDateTime() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        /**
         * @param value {@link #dateTime} (The date and time when the series was started.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public ImagingStudySeriesComponent setDateTimeElement(DateTimeType value) { 
          this.dateTime = value;
          return this;
        }

        /**
         * @return The date and time when the series was started.
         */
        public Date getDateTime() { 
          return this.dateTime == null ? null : this.dateTime.getValue();
        }

        /**
         * @param value The date and time when the series was started.
         */
        public ImagingStudySeriesComponent setDateTime(Date value) { 
          if (value == null)
            this.dateTime = null;
          else {
            if (this.dateTime == null)
              this.dateTime = new DateTimeType();
            this.dateTime.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #instance} (A single SOP Instance within the series, e.g., an image, or presentation state.)
         */
        public List<ImagingStudySeriesInstanceComponent> getInstance() { 
          if (this.instance == null)
            this.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          return this.instance;
        }

        public boolean hasInstance() { 
          if (this.instance == null)
            return false;
          for (ImagingStudySeriesInstanceComponent item : this.instance)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #instance} (A single SOP Instance within the series, e.g., an image, or presentation state.)
         */
    // syntactic sugar
        public ImagingStudySeriesInstanceComponent addInstance() { //3
          ImagingStudySeriesInstanceComponent t = new ImagingStudySeriesInstanceComponent();
          if (this.instance == null)
            this.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          this.instance.add(t);
          return t;
        }

    // syntactic sugar
        public ImagingStudySeriesComponent addInstance(ImagingStudySeriesInstanceComponent t) { //3
          if (t == null)
            return this;
          if (this.instance == null)
            this.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          this.instance.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("number", "unsignedInt", "The Numeric identifier of this series in the study.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("modality", "Coding", "The modality of this series sequence.", 0, java.lang.Integer.MAX_VALUE, modality));
          childrenList.add(new Property("uid", "oid", "Formal identifier for this series.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("description", "string", "A description of the series.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("numberOfInstances", "unsignedInt", "Number of SOP Instances in Series.", 0, java.lang.Integer.MAX_VALUE, numberOfInstances));
          childrenList.add(new Property("availability", "code", "Availability of series (online, offline or nearline).", 0, java.lang.Integer.MAX_VALUE, availability));
          childrenList.add(new Property("url", "uri", "URI/URL specifying the location of the referenced series using WADO-RS.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("bodySite", "Coding", "Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed.", 0, java.lang.Integer.MAX_VALUE, bodySite));
          childrenList.add(new Property("laterality", "Coding", "Laterality if bodySite is paired anatomic structure and laterality is not pre-coordinated in bodySite code, map from (0020, 0060).", 0, java.lang.Integer.MAX_VALUE, laterality));
          childrenList.add(new Property("dateTime", "dateTime", "The date and time when the series was started.", 0, java.lang.Integer.MAX_VALUE, dateTime));
          childrenList.add(new Property("instance", "", "A single SOP Instance within the series, e.g., an image, or presentation state.", 0, java.lang.Integer.MAX_VALUE, instance));
        }

      public ImagingStudySeriesComponent copy() {
        ImagingStudySeriesComponent dst = new ImagingStudySeriesComponent();
        copyValues(dst);
        dst.number = number == null ? null : number.copy();
        dst.modality = modality == null ? null : modality.copy();
        dst.uid = uid == null ? null : uid.copy();
        dst.description = description == null ? null : description.copy();
        dst.numberOfInstances = numberOfInstances == null ? null : numberOfInstances.copy();
        dst.availability = availability == null ? null : availability.copy();
        dst.url = url == null ? null : url.copy();
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        dst.laterality = laterality == null ? null : laterality.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        if (instance != null) {
          dst.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          for (ImagingStudySeriesInstanceComponent i : instance)
            dst.instance.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingStudySeriesComponent))
          return false;
        ImagingStudySeriesComponent o = (ImagingStudySeriesComponent) other;
        return compareDeep(number, o.number, true) && compareDeep(modality, o.modality, true) && compareDeep(uid, o.uid, true)
           && compareDeep(description, o.description, true) && compareDeep(numberOfInstances, o.numberOfInstances, true)
           && compareDeep(availability, o.availability, true) && compareDeep(url, o.url, true) && compareDeep(bodySite, o.bodySite, true)
           && compareDeep(laterality, o.laterality, true) && compareDeep(dateTime, o.dateTime, true) && compareDeep(instance, o.instance, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingStudySeriesComponent))
          return false;
        ImagingStudySeriesComponent o = (ImagingStudySeriesComponent) other;
        return compareValues(number, o.number, true) && compareValues(uid, o.uid, true) && compareValues(description, o.description, true)
           && compareValues(numberOfInstances, o.numberOfInstances, true) && compareValues(availability, o.availability, true)
           && compareValues(url, o.url, true) && compareValues(dateTime, o.dateTime, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (number == null || number.isEmpty()) && (modality == null || modality.isEmpty())
           && (uid == null || uid.isEmpty()) && (description == null || description.isEmpty()) && (numberOfInstances == null || numberOfInstances.isEmpty())
           && (availability == null || availability.isEmpty()) && (url == null || url.isEmpty()) && (bodySite == null || bodySite.isEmpty())
           && (laterality == null || laterality.isEmpty()) && (dateTime == null || dateTime.isEmpty())
           && (instance == null || instance.isEmpty());
      }

  }

    @Block()
    public static class ImagingStudySeriesInstanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The number of this image in the series.
         */
        @Child(name = "number", type = {UnsignedIntType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="The number of this instance in the series (0020,0013)", formalDefinition="The number of this image in the series." )
        protected UnsignedIntType number;

        /**
         * Formal identifier for this image.
         */
        @Child(name = "uid", type = {OidType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Formal identifier for this instance (0008,0018)", formalDefinition="Formal identifier for this image." )
        protected OidType uid;

        /**
         * DICOM Image type.
         */
        @Child(name = "sopclass", type = {OidType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="DICOM class type (0008,0016)", formalDefinition="DICOM Image type." )
        protected OidType sopclass;

        /**
         * A human-friendly SOP Class name.
         */
        @Child(name = "type", type = {StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Type of instance (image etc) (0004,1430)", formalDefinition="A human-friendly SOP Class name." )
        protected StringType type;

        /**
         * The description of the instance.
         */
        @Child(name = "title", type = {StringType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Description (0070,0080 | 0040,A043 > 0008,0104 | 0042,0010 | 0008,0008)", formalDefinition="The description of the instance." )
        protected StringType title;

        /**
         * Content of the instance or a rendering thereof (e.g., a JPEG of an image, or an XML of a structured report). May be represented by inline encoding, or by a URL reference to a WADO-RS service that makes the instance available. Multiple content attachments may be used for alternate representations of the instance.
         */
        @Child(name = "content", type = {Attachment.class}, order=6, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Content of the instance", formalDefinition="Content of the instance or a rendering thereof (e.g., a JPEG of an image, or an XML of a structured report). May be represented by inline encoding, or by a URL reference to a WADO-RS service that makes the instance available. Multiple content attachments may be used for alternate representations of the instance." )
        protected List<Attachment> content;

        private static final long serialVersionUID = 264997991L;

    /*
     * Constructor
     */
      public ImagingStudySeriesInstanceComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImagingStudySeriesInstanceComponent(OidType uid, OidType sopclass) {
        super();
        this.uid = uid;
        this.sopclass = sopclass;
      }

        /**
         * @return {@link #number} (The number of this image in the series.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public UnsignedIntType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new UnsignedIntType(); // bb
          return this.number;
        }

        public boolean hasNumberElement() { 
          return this.number != null && !this.number.isEmpty();
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (The number of this image in the series.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setNumberElement(UnsignedIntType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return The number of this image in the series.
         */
        public int getNumber() { 
          return this.number == null || this.number.isEmpty() ? 0 : this.number.getValue();
        }

        /**
         * @param value The number of this image in the series.
         */
        public ImagingStudySeriesInstanceComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new UnsignedIntType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #uid} (Formal identifier for this image.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.uid");
            else if (Configuration.doAutoCreate())
              this.uid = new OidType(); // bb
          return this.uid;
        }

        public boolean hasUidElement() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        public boolean hasUid() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        /**
         * @param value {@link #uid} (Formal identifier for this image.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return Formal identifier for this image.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value Formal identifier for this image.
         */
        public ImagingStudySeriesInstanceComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #sopclass} (DICOM Image type.). This is the underlying object with id, value and extensions. The accessor "getSopclass" gives direct access to the value
         */
        public OidType getSopclassElement() { 
          if (this.sopclass == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.sopclass");
            else if (Configuration.doAutoCreate())
              this.sopclass = new OidType(); // bb
          return this.sopclass;
        }

        public boolean hasSopclassElement() { 
          return this.sopclass != null && !this.sopclass.isEmpty();
        }

        public boolean hasSopclass() { 
          return this.sopclass != null && !this.sopclass.isEmpty();
        }

        /**
         * @param value {@link #sopclass} (DICOM Image type.). This is the underlying object with id, value and extensions. The accessor "getSopclass" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setSopclassElement(OidType value) { 
          this.sopclass = value;
          return this;
        }

        /**
         * @return DICOM Image type.
         */
        public String getSopclass() { 
          return this.sopclass == null ? null : this.sopclass.getValue();
        }

        /**
         * @param value DICOM Image type.
         */
        public ImagingStudySeriesInstanceComponent setSopclass(String value) { 
            if (this.sopclass == null)
              this.sopclass = new OidType();
            this.sopclass.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (A human-friendly SOP Class name.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StringType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new StringType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A human-friendly SOP Class name.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setTypeElement(StringType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return A human-friendly SOP Class name.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value A human-friendly SOP Class name.
         */
        public ImagingStudySeriesInstanceComponent setType(String value) { 
          if (Utilities.noString(value))
            this.type = null;
          else {
            if (this.type == null)
              this.type = new StringType();
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #title} (The description of the instance.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.title");
            else if (Configuration.doAutoCreate())
              this.title = new StringType(); // bb
          return this.title;
        }

        public boolean hasTitleElement() { 
          return this.title != null && !this.title.isEmpty();
        }

        public boolean hasTitle() { 
          return this.title != null && !this.title.isEmpty();
        }

        /**
         * @param value {@link #title} (The description of the instance.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The description of the instance.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The description of the instance.
         */
        public ImagingStudySeriesInstanceComponent setTitle(String value) { 
          if (Utilities.noString(value))
            this.title = null;
          else {
            if (this.title == null)
              this.title = new StringType();
            this.title.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #content} (Content of the instance or a rendering thereof (e.g., a JPEG of an image, or an XML of a structured report). May be represented by inline encoding, or by a URL reference to a WADO-RS service that makes the instance available. Multiple content attachments may be used for alternate representations of the instance.)
         */
        public List<Attachment> getContent() { 
          if (this.content == null)
            this.content = new ArrayList<Attachment>();
          return this.content;
        }

        public boolean hasContent() { 
          if (this.content == null)
            return false;
          for (Attachment item : this.content)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #content} (Content of the instance or a rendering thereof (e.g., a JPEG of an image, or an XML of a structured report). May be represented by inline encoding, or by a URL reference to a WADO-RS service that makes the instance available. Multiple content attachments may be used for alternate representations of the instance.)
         */
    // syntactic sugar
        public Attachment addContent() { //3
          Attachment t = new Attachment();
          if (this.content == null)
            this.content = new ArrayList<Attachment>();
          this.content.add(t);
          return t;
        }

    // syntactic sugar
        public ImagingStudySeriesInstanceComponent addContent(Attachment t) { //3
          if (t == null)
            return this;
          if (this.content == null)
            this.content = new ArrayList<Attachment>();
          this.content.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("number", "unsignedInt", "The number of this image in the series.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("uid", "oid", "Formal identifier for this image.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("sopclass", "oid", "DICOM Image type.", 0, java.lang.Integer.MAX_VALUE, sopclass));
          childrenList.add(new Property("type", "string", "A human-friendly SOP Class name.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("title", "string", "The description of the instance.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("content", "Attachment", "Content of the instance or a rendering thereof (e.g., a JPEG of an image, or an XML of a structured report). May be represented by inline encoding, or by a URL reference to a WADO-RS service that makes the instance available. Multiple content attachments may be used for alternate representations of the instance.", 0, java.lang.Integer.MAX_VALUE, content));
        }

      public ImagingStudySeriesInstanceComponent copy() {
        ImagingStudySeriesInstanceComponent dst = new ImagingStudySeriesInstanceComponent();
        copyValues(dst);
        dst.number = number == null ? null : number.copy();
        dst.uid = uid == null ? null : uid.copy();
        dst.sopclass = sopclass == null ? null : sopclass.copy();
        dst.type = type == null ? null : type.copy();
        dst.title = title == null ? null : title.copy();
        if (content != null) {
          dst.content = new ArrayList<Attachment>();
          for (Attachment i : content)
            dst.content.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingStudySeriesInstanceComponent))
          return false;
        ImagingStudySeriesInstanceComponent o = (ImagingStudySeriesInstanceComponent) other;
        return compareDeep(number, o.number, true) && compareDeep(uid, o.uid, true) && compareDeep(sopclass, o.sopclass, true)
           && compareDeep(type, o.type, true) && compareDeep(title, o.title, true) && compareDeep(content, o.content, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingStudySeriesInstanceComponent))
          return false;
        ImagingStudySeriesInstanceComponent o = (ImagingStudySeriesInstanceComponent) other;
        return compareValues(number, o.number, true) && compareValues(uid, o.uid, true) && compareValues(sopclass, o.sopclass, true)
           && compareValues(type, o.type, true) && compareValues(title, o.title, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (number == null || number.isEmpty()) && (uid == null || uid.isEmpty())
           && (sopclass == null || sopclass.isEmpty()) && (type == null || type.isEmpty()) && (title == null || title.isEmpty())
           && (content == null || content.isEmpty());
      }

  }

    /**
     * Date and Time the study started. Timezone Offset From UTC.
     */
    @Child(name = "started", type = {DateTimeType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="When the study was started (0008,0020)+(0008,0030)", formalDefinition="Date and Time the study started. Timezone Offset From UTC." )
    protected DateTimeType started;

    /**
     * The patient imaged in the study.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=1, max=1)
    @Description(shortDefinition="Who the images are of (0010/*)", formalDefinition="The patient imaged in the study." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient imaged in the study.)
     */
    protected Patient patientTarget;

    /**
     * Formal identifier for the study.
     */
    @Child(name = "uid", type = {OidType.class}, order=2, min=1, max=1)
    @Description(shortDefinition="Formal identifier for the study (0020,000D)", formalDefinition="Formal identifier for the study." )
    protected OidType uid;

    /**
     * Accession Number is an identifier related to some aspect of imaging workflow and data management, and usage may vary across different institutions. 
See for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf).
     */
    @Child(name = "accession", type = {Identifier.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Related workflow identifier ('Accession Number') (0008,0050)", formalDefinition="Accession Number is an identifier related to some aspect of imaging workflow and data management, and usage may vary across different institutions. \nSee for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf)." )
    protected Identifier accession;

    /**
     * Other identifiers for the study.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Other identifiers for the study (0020,0010)", formalDefinition="Other identifiers for the study." )
    protected List<Identifier> identifier;

    /**
     * A list of the diagnostic orders that resulted in this imaging study being performed.
     */
    @Child(name = "order", type = {DiagnosticOrder.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Order(s) that caused this study to be performed", formalDefinition="A list of the diagnostic orders that resulted in this imaging study being performed." )
    protected List<Reference> order;
    /**
     * The actual objects that are the target of the reference (A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    protected List<DiagnosticOrder> orderTarget;


    /**
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).
     */
    @Child(name = "modalityList", type = {Coding.class}, order=6, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="All series.modality if actual acquisition modalities", formalDefinition="A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)." )
    protected List<Coding> modalityList;

    /**
     * The requesting/referring physician.
     */
    @Child(name = "referrer", type = {Practitioner.class}, order=7, min=0, max=1)
    @Description(shortDefinition="Referring physician (0008,0090)", formalDefinition="The requesting/referring physician." )
    protected Reference referrer;

    /**
     * The actual object that is the target of the reference (The requesting/referring physician.)
     */
    protected Practitioner referrerTarget;

    /**
     * Availability of study (online, offline or nearline).
     */
    @Child(name = "availability", type = {CodeType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="ONLINE | OFFLINE | NEARLINE | UNAVAILABLE (0008,0056)", formalDefinition="Availability of study (online, offline or nearline)." )
    protected Enumeration<InstanceAvailability> availability;

    /**
     * WADO-RS resource where Study is available.
     */
    @Child(name = "url", type = {UriType.class}, order=9, min=0, max=1)
    @Description(shortDefinition="Retrieve URI (0008,1190)", formalDefinition="WADO-RS resource where Study is available." )
    protected UriType url;

    /**
     * Number of Series in Study.
     */
    @Child(name = "numberOfSeries", type = {UnsignedIntType.class}, order=10, min=1, max=1)
    @Description(shortDefinition="Number of Study Related Series (0020,1206)", formalDefinition="Number of Series in Study." )
    protected UnsignedIntType numberOfSeries;

    /**
     * Number of SOP Instances in Study.
     */
    @Child(name = "numberOfInstances", type = {UnsignedIntType.class}, order=11, min=1, max=1)
    @Description(shortDefinition="Number of Study Related Instances (0020,1208)", formalDefinition="Number of SOP Instances in Study." )
    protected UnsignedIntType numberOfInstances;

    /**
     * Diagnoses etc provided with request.
     */
    @Child(name = "clinicalInformation", type = {StringType.class}, order=12, min=0, max=1)
    @Description(shortDefinition="Diagnoses etc with request (0040,1002)", formalDefinition="Diagnoses etc provided with request." )
    protected StringType clinicalInformation;

    /**
     * Type of procedure performed.
     */
    @Child(name = "procedure", type = {Procedure.class}, order=13, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Type of procedure performed (0008,1032)", formalDefinition="Type of procedure performed." )
    protected List<Reference> procedure;
    /**
     * The actual objects that are the target of the reference (Type of procedure performed.)
     */
    protected List<Procedure> procedureTarget;


    /**
     * Who read study and interpreted the images.
     */
    @Child(name = "interpreter", type = {Practitioner.class}, order=14, min=0, max=1)
    @Description(shortDefinition="Who interpreted images (0008,1060)", formalDefinition="Who read study and interpreted the images." )
    protected Reference interpreter;

    /**
     * The actual object that is the target of the reference (Who read study and interpreted the images.)
     */
    protected Practitioner interpreterTarget;

    /**
     * Institution-generated description or classification of the Study performed.
     */
    @Child(name = "description", type = {StringType.class}, order=15, min=0, max=1)
    @Description(shortDefinition="Institution-generated description (0008,1030)", formalDefinition="Institution-generated description or classification of the Study performed." )
    protected StringType description;

    /**
     * Each study has one or more series of image instances.
     */
    @Child(name = "series", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Each study has one or more series of instances", formalDefinition="Each study has one or more series of image instances." )
    protected List<ImagingStudySeriesComponent> series;

    private static final long serialVersionUID = -367817262L;

  /*
   * Constructor
   */
    public ImagingStudy() {
      super();
    }

  /*
   * Constructor
   */
    public ImagingStudy(Reference patient, OidType uid, UnsignedIntType numberOfSeries, UnsignedIntType numberOfInstances) {
      super();
      this.patient = patient;
      this.uid = uid;
      this.numberOfSeries = numberOfSeries;
      this.numberOfInstances = numberOfInstances;
    }

    /**
     * @return {@link #started} (Date and Time the study started. Timezone Offset From UTC.). This is the underlying object with id, value and extensions. The accessor "getStarted" gives direct access to the value
     */
    public DateTimeType getStartedElement() { 
      if (this.started == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.started");
        else if (Configuration.doAutoCreate())
          this.started = new DateTimeType(); // bb
      return this.started;
    }

    public boolean hasStartedElement() { 
      return this.started != null && !this.started.isEmpty();
    }

    public boolean hasStarted() { 
      return this.started != null && !this.started.isEmpty();
    }

    /**
     * @param value {@link #started} (Date and Time the study started. Timezone Offset From UTC.). This is the underlying object with id, value and extensions. The accessor "getStarted" gives direct access to the value
     */
    public ImagingStudy setStartedElement(DateTimeType value) { 
      this.started = value;
      return this;
    }

    /**
     * @return Date and Time the study started. Timezone Offset From UTC.
     */
    public Date getStarted() { 
      return this.started == null ? null : this.started.getValue();
    }

    /**
     * @param value Date and Time the study started. Timezone Offset From UTC.
     */
    public ImagingStudy setStarted(Date value) { 
      if (value == null)
        this.started = null;
      else {
        if (this.started == null)
          this.started = new DateTimeType();
        this.started.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #patient} (The patient imaged in the study.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient imaged in the study.)
     */
    public ImagingStudy setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient imaged in the study.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient imaged in the study.)
     */
    public ImagingStudy setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #uid} (Formal identifier for the study.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public OidType getUidElement() { 
      if (this.uid == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.uid");
        else if (Configuration.doAutoCreate())
          this.uid = new OidType(); // bb
      return this.uid;
    }

    public boolean hasUidElement() { 
      return this.uid != null && !this.uid.isEmpty();
    }

    public boolean hasUid() { 
      return this.uid != null && !this.uid.isEmpty();
    }

    /**
     * @param value {@link #uid} (Formal identifier for the study.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public ImagingStudy setUidElement(OidType value) { 
      this.uid = value;
      return this;
    }

    /**
     * @return Formal identifier for the study.
     */
    public String getUid() { 
      return this.uid == null ? null : this.uid.getValue();
    }

    /**
     * @param value Formal identifier for the study.
     */
    public ImagingStudy setUid(String value) { 
        if (this.uid == null)
          this.uid = new OidType();
        this.uid.setValue(value);
      return this;
    }

    /**
     * @return {@link #accession} (Accession Number is an identifier related to some aspect of imaging workflow and data management, and usage may vary across different institutions. 
See for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf).)
     */
    public Identifier getAccession() { 
      if (this.accession == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.accession");
        else if (Configuration.doAutoCreate())
          this.accession = new Identifier(); // cc
      return this.accession;
    }

    public boolean hasAccession() { 
      return this.accession != null && !this.accession.isEmpty();
    }

    /**
     * @param value {@link #accession} (Accession Number is an identifier related to some aspect of imaging workflow and data management, and usage may vary across different institutions. 
See for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf).)
     */
    public ImagingStudy setAccession(Identifier value) { 
      this.accession = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Other identifiers for the study.)
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
     * @return {@link #identifier} (Other identifiers for the study.)
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
    public ImagingStudy addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #order} (A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    public List<Reference> getOrder() { 
      if (this.order == null)
        this.order = new ArrayList<Reference>();
      return this.order;
    }

    public boolean hasOrder() { 
      if (this.order == null)
        return false;
      for (Reference item : this.order)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #order} (A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    // syntactic sugar
    public Reference addOrder() { //3
      Reference t = new Reference();
      if (this.order == null)
        this.order = new ArrayList<Reference>();
      this.order.add(t);
      return t;
    }

    // syntactic sugar
    public ImagingStudy addOrder(Reference t) { //3
      if (t == null)
        return this;
      if (this.order == null)
        this.order = new ArrayList<Reference>();
      this.order.add(t);
      return this;
    }

    /**
     * @return {@link #order} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    public List<DiagnosticOrder> getOrderTarget() { 
      if (this.orderTarget == null)
        this.orderTarget = new ArrayList<DiagnosticOrder>();
      return this.orderTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #order} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A list of the diagnostic orders that resulted in this imaging study being performed.)
     */
    public DiagnosticOrder addOrderTarget() { 
      DiagnosticOrder r = new DiagnosticOrder();
      if (this.orderTarget == null)
        this.orderTarget = new ArrayList<DiagnosticOrder>();
      this.orderTarget.add(r);
      return r;
    }

    /**
     * @return {@link #modalityList} (A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).)
     */
    public List<Coding> getModalityList() { 
      if (this.modalityList == null)
        this.modalityList = new ArrayList<Coding>();
      return this.modalityList;
    }

    public boolean hasModalityList() { 
      if (this.modalityList == null)
        return false;
      for (Coding item : this.modalityList)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #modalityList} (A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).)
     */
    // syntactic sugar
    public Coding addModalityList() { //3
      Coding t = new Coding();
      if (this.modalityList == null)
        this.modalityList = new ArrayList<Coding>();
      this.modalityList.add(t);
      return t;
    }

    // syntactic sugar
    public ImagingStudy addModalityList(Coding t) { //3
      if (t == null)
        return this;
      if (this.modalityList == null)
        this.modalityList = new ArrayList<Coding>();
      this.modalityList.add(t);
      return this;
    }

    /**
     * @return {@link #referrer} (The requesting/referring physician.)
     */
    public Reference getReferrer() { 
      if (this.referrer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.referrer");
        else if (Configuration.doAutoCreate())
          this.referrer = new Reference(); // cc
      return this.referrer;
    }

    public boolean hasReferrer() { 
      return this.referrer != null && !this.referrer.isEmpty();
    }

    /**
     * @param value {@link #referrer} (The requesting/referring physician.)
     */
    public ImagingStudy setReferrer(Reference value) { 
      this.referrer = value;
      return this;
    }

    /**
     * @return {@link #referrer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The requesting/referring physician.)
     */
    public Practitioner getReferrerTarget() { 
      if (this.referrerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.referrer");
        else if (Configuration.doAutoCreate())
          this.referrerTarget = new Practitioner(); // aa
      return this.referrerTarget;
    }

    /**
     * @param value {@link #referrer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The requesting/referring physician.)
     */
    public ImagingStudy setReferrerTarget(Practitioner value) { 
      this.referrerTarget = value;
      return this;
    }

    /**
     * @return {@link #availability} (Availability of study (online, offline or nearline).). This is the underlying object with id, value and extensions. The accessor "getAvailability" gives direct access to the value
     */
    public Enumeration<InstanceAvailability> getAvailabilityElement() { 
      if (this.availability == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.availability");
        else if (Configuration.doAutoCreate())
          this.availability = new Enumeration<InstanceAvailability>(new InstanceAvailabilityEnumFactory()); // bb
      return this.availability;
    }

    public boolean hasAvailabilityElement() { 
      return this.availability != null && !this.availability.isEmpty();
    }

    public boolean hasAvailability() { 
      return this.availability != null && !this.availability.isEmpty();
    }

    /**
     * @param value {@link #availability} (Availability of study (online, offline or nearline).). This is the underlying object with id, value and extensions. The accessor "getAvailability" gives direct access to the value
     */
    public ImagingStudy setAvailabilityElement(Enumeration<InstanceAvailability> value) { 
      this.availability = value;
      return this;
    }

    /**
     * @return Availability of study (online, offline or nearline).
     */
    public InstanceAvailability getAvailability() { 
      return this.availability == null ? null : this.availability.getValue();
    }

    /**
     * @param value Availability of study (online, offline or nearline).
     */
    public ImagingStudy setAvailability(InstanceAvailability value) { 
      if (value == null)
        this.availability = null;
      else {
        if (this.availability == null)
          this.availability = new Enumeration<InstanceAvailability>(new InstanceAvailabilityEnumFactory());
        this.availability.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #url} (WADO-RS resource where Study is available.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (WADO-RS resource where Study is available.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ImagingStudy setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return WADO-RS resource where Study is available.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value WADO-RS resource where Study is available.
     */
    public ImagingStudy setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #numberOfSeries} (Number of Series in Study.). This is the underlying object with id, value and extensions. The accessor "getNumberOfSeries" gives direct access to the value
     */
    public UnsignedIntType getNumberOfSeriesElement() { 
      if (this.numberOfSeries == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.numberOfSeries");
        else if (Configuration.doAutoCreate())
          this.numberOfSeries = new UnsignedIntType(); // bb
      return this.numberOfSeries;
    }

    public boolean hasNumberOfSeriesElement() { 
      return this.numberOfSeries != null && !this.numberOfSeries.isEmpty();
    }

    public boolean hasNumberOfSeries() { 
      return this.numberOfSeries != null && !this.numberOfSeries.isEmpty();
    }

    /**
     * @param value {@link #numberOfSeries} (Number of Series in Study.). This is the underlying object with id, value and extensions. The accessor "getNumberOfSeries" gives direct access to the value
     */
    public ImagingStudy setNumberOfSeriesElement(UnsignedIntType value) { 
      this.numberOfSeries = value;
      return this;
    }

    /**
     * @return Number of Series in Study.
     */
    public int getNumberOfSeries() { 
      return this.numberOfSeries == null || this.numberOfSeries.isEmpty() ? 0 : this.numberOfSeries.getValue();
    }

    /**
     * @param value Number of Series in Study.
     */
    public ImagingStudy setNumberOfSeries(int value) { 
        if (this.numberOfSeries == null)
          this.numberOfSeries = new UnsignedIntType();
        this.numberOfSeries.setValue(value);
      return this;
    }

    /**
     * @return {@link #numberOfInstances} (Number of SOP Instances in Study.). This is the underlying object with id, value and extensions. The accessor "getNumberOfInstances" gives direct access to the value
     */
    public UnsignedIntType getNumberOfInstancesElement() { 
      if (this.numberOfInstances == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.numberOfInstances");
        else if (Configuration.doAutoCreate())
          this.numberOfInstances = new UnsignedIntType(); // bb
      return this.numberOfInstances;
    }

    public boolean hasNumberOfInstancesElement() { 
      return this.numberOfInstances != null && !this.numberOfInstances.isEmpty();
    }

    public boolean hasNumberOfInstances() { 
      return this.numberOfInstances != null && !this.numberOfInstances.isEmpty();
    }

    /**
     * @param value {@link #numberOfInstances} (Number of SOP Instances in Study.). This is the underlying object with id, value and extensions. The accessor "getNumberOfInstances" gives direct access to the value
     */
    public ImagingStudy setNumberOfInstancesElement(UnsignedIntType value) { 
      this.numberOfInstances = value;
      return this;
    }

    /**
     * @return Number of SOP Instances in Study.
     */
    public int getNumberOfInstances() { 
      return this.numberOfInstances == null || this.numberOfInstances.isEmpty() ? 0 : this.numberOfInstances.getValue();
    }

    /**
     * @param value Number of SOP Instances in Study.
     */
    public ImagingStudy setNumberOfInstances(int value) { 
        if (this.numberOfInstances == null)
          this.numberOfInstances = new UnsignedIntType();
        this.numberOfInstances.setValue(value);
      return this;
    }

    /**
     * @return {@link #clinicalInformation} (Diagnoses etc provided with request.). This is the underlying object with id, value and extensions. The accessor "getClinicalInformation" gives direct access to the value
     */
    public StringType getClinicalInformationElement() { 
      if (this.clinicalInformation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.clinicalInformation");
        else if (Configuration.doAutoCreate())
          this.clinicalInformation = new StringType(); // bb
      return this.clinicalInformation;
    }

    public boolean hasClinicalInformationElement() { 
      return this.clinicalInformation != null && !this.clinicalInformation.isEmpty();
    }

    public boolean hasClinicalInformation() { 
      return this.clinicalInformation != null && !this.clinicalInformation.isEmpty();
    }

    /**
     * @param value {@link #clinicalInformation} (Diagnoses etc provided with request.). This is the underlying object with id, value and extensions. The accessor "getClinicalInformation" gives direct access to the value
     */
    public ImagingStudy setClinicalInformationElement(StringType value) { 
      this.clinicalInformation = value;
      return this;
    }

    /**
     * @return Diagnoses etc provided with request.
     */
    public String getClinicalInformation() { 
      return this.clinicalInformation == null ? null : this.clinicalInformation.getValue();
    }

    /**
     * @param value Diagnoses etc provided with request.
     */
    public ImagingStudy setClinicalInformation(String value) { 
      if (Utilities.noString(value))
        this.clinicalInformation = null;
      else {
        if (this.clinicalInformation == null)
          this.clinicalInformation = new StringType();
        this.clinicalInformation.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #procedure} (Type of procedure performed.)
     */
    public List<Reference> getProcedure() { 
      if (this.procedure == null)
        this.procedure = new ArrayList<Reference>();
      return this.procedure;
    }

    public boolean hasProcedure() { 
      if (this.procedure == null)
        return false;
      for (Reference item : this.procedure)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #procedure} (Type of procedure performed.)
     */
    // syntactic sugar
    public Reference addProcedure() { //3
      Reference t = new Reference();
      if (this.procedure == null)
        this.procedure = new ArrayList<Reference>();
      this.procedure.add(t);
      return t;
    }

    // syntactic sugar
    public ImagingStudy addProcedure(Reference t) { //3
      if (t == null)
        return this;
      if (this.procedure == null)
        this.procedure = new ArrayList<Reference>();
      this.procedure.add(t);
      return this;
    }

    /**
     * @return {@link #procedure} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Type of procedure performed.)
     */
    public List<Procedure> getProcedureTarget() { 
      if (this.procedureTarget == null)
        this.procedureTarget = new ArrayList<Procedure>();
      return this.procedureTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #procedure} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Type of procedure performed.)
     */
    public Procedure addProcedureTarget() { 
      Procedure r = new Procedure();
      if (this.procedureTarget == null)
        this.procedureTarget = new ArrayList<Procedure>();
      this.procedureTarget.add(r);
      return r;
    }

    /**
     * @return {@link #interpreter} (Who read study and interpreted the images.)
     */
    public Reference getInterpreter() { 
      if (this.interpreter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.interpreter");
        else if (Configuration.doAutoCreate())
          this.interpreter = new Reference(); // cc
      return this.interpreter;
    }

    public boolean hasInterpreter() { 
      return this.interpreter != null && !this.interpreter.isEmpty();
    }

    /**
     * @param value {@link #interpreter} (Who read study and interpreted the images.)
     */
    public ImagingStudy setInterpreter(Reference value) { 
      this.interpreter = value;
      return this;
    }

    /**
     * @return {@link #interpreter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who read study and interpreted the images.)
     */
    public Practitioner getInterpreterTarget() { 
      if (this.interpreterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.interpreter");
        else if (Configuration.doAutoCreate())
          this.interpreterTarget = new Practitioner(); // aa
      return this.interpreterTarget;
    }

    /**
     * @param value {@link #interpreter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who read study and interpreted the images.)
     */
    public ImagingStudy setInterpreterTarget(Practitioner value) { 
      this.interpreterTarget = value;
      return this;
    }

    /**
     * @return {@link #description} (Institution-generated description or classification of the Study performed.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (Institution-generated description or classification of the Study performed.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImagingStudy setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Institution-generated description or classification of the Study performed.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Institution-generated description or classification of the Study performed.
     */
    public ImagingStudy setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #series} (Each study has one or more series of image instances.)
     */
    public List<ImagingStudySeriesComponent> getSeries() { 
      if (this.series == null)
        this.series = new ArrayList<ImagingStudySeriesComponent>();
      return this.series;
    }

    public boolean hasSeries() { 
      if (this.series == null)
        return false;
      for (ImagingStudySeriesComponent item : this.series)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #series} (Each study has one or more series of image instances.)
     */
    // syntactic sugar
    public ImagingStudySeriesComponent addSeries() { //3
      ImagingStudySeriesComponent t = new ImagingStudySeriesComponent();
      if (this.series == null)
        this.series = new ArrayList<ImagingStudySeriesComponent>();
      this.series.add(t);
      return t;
    }

    // syntactic sugar
    public ImagingStudy addSeries(ImagingStudySeriesComponent t) { //3
      if (t == null)
        return this;
      if (this.series == null)
        this.series = new ArrayList<ImagingStudySeriesComponent>();
      this.series.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("started", "dateTime", "Date and Time the study started. Timezone Offset From UTC.", 0, java.lang.Integer.MAX_VALUE, started));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient imaged in the study.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("uid", "oid", "Formal identifier for the study.", 0, java.lang.Integer.MAX_VALUE, uid));
        childrenList.add(new Property("accession", "Identifier", "Accession Number is an identifier related to some aspect of imaging workflow and data management, and usage may vary across different institutions. \nSee for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf).", 0, java.lang.Integer.MAX_VALUE, accession));
        childrenList.add(new Property("identifier", "Identifier", "Other identifiers for the study.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("order", "Reference(DiagnosticOrder)", "A list of the diagnostic orders that resulted in this imaging study being performed.", 0, java.lang.Integer.MAX_VALUE, order));
        childrenList.add(new Property("modalityList", "Coding", "A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).", 0, java.lang.Integer.MAX_VALUE, modalityList));
        childrenList.add(new Property("referrer", "Reference(Practitioner)", "The requesting/referring physician.", 0, java.lang.Integer.MAX_VALUE, referrer));
        childrenList.add(new Property("availability", "code", "Availability of study (online, offline or nearline).", 0, java.lang.Integer.MAX_VALUE, availability));
        childrenList.add(new Property("url", "uri", "WADO-RS resource where Study is available.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("numberOfSeries", "unsignedInt", "Number of Series in Study.", 0, java.lang.Integer.MAX_VALUE, numberOfSeries));
        childrenList.add(new Property("numberOfInstances", "unsignedInt", "Number of SOP Instances in Study.", 0, java.lang.Integer.MAX_VALUE, numberOfInstances));
        childrenList.add(new Property("clinicalInformation", "string", "Diagnoses etc provided with request.", 0, java.lang.Integer.MAX_VALUE, clinicalInformation));
        childrenList.add(new Property("procedure", "Reference(Procedure)", "Type of procedure performed.", 0, java.lang.Integer.MAX_VALUE, procedure));
        childrenList.add(new Property("interpreter", "Reference(Practitioner)", "Who read study and interpreted the images.", 0, java.lang.Integer.MAX_VALUE, interpreter));
        childrenList.add(new Property("description", "string", "Institution-generated description or classification of the Study performed.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("series", "", "Each study has one or more series of image instances.", 0, java.lang.Integer.MAX_VALUE, series));
      }

      public ImagingStudy copy() {
        ImagingStudy dst = new ImagingStudy();
        copyValues(dst);
        dst.started = started == null ? null : started.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.uid = uid == null ? null : uid.copy();
        dst.accession = accession == null ? null : accession.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (order != null) {
          dst.order = new ArrayList<Reference>();
          for (Reference i : order)
            dst.order.add(i.copy());
        };
        if (modalityList != null) {
          dst.modalityList = new ArrayList<Coding>();
          for (Coding i : modalityList)
            dst.modalityList.add(i.copy());
        };
        dst.referrer = referrer == null ? null : referrer.copy();
        dst.availability = availability == null ? null : availability.copy();
        dst.url = url == null ? null : url.copy();
        dst.numberOfSeries = numberOfSeries == null ? null : numberOfSeries.copy();
        dst.numberOfInstances = numberOfInstances == null ? null : numberOfInstances.copy();
        dst.clinicalInformation = clinicalInformation == null ? null : clinicalInformation.copy();
        if (procedure != null) {
          dst.procedure = new ArrayList<Reference>();
          for (Reference i : procedure)
            dst.procedure.add(i.copy());
        };
        dst.interpreter = interpreter == null ? null : interpreter.copy();
        dst.description = description == null ? null : description.copy();
        if (series != null) {
          dst.series = new ArrayList<ImagingStudySeriesComponent>();
          for (ImagingStudySeriesComponent i : series)
            dst.series.add(i.copy());
        };
        return dst;
      }

      protected ImagingStudy typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingStudy))
          return false;
        ImagingStudy o = (ImagingStudy) other;
        return compareDeep(started, o.started, true) && compareDeep(patient, o.patient, true) && compareDeep(uid, o.uid, true)
           && compareDeep(accession, o.accession, true) && compareDeep(identifier, o.identifier, true) && compareDeep(order, o.order, true)
           && compareDeep(modalityList, o.modalityList, true) && compareDeep(referrer, o.referrer, true) && compareDeep(availability, o.availability, true)
           && compareDeep(url, o.url, true) && compareDeep(numberOfSeries, o.numberOfSeries, true) && compareDeep(numberOfInstances, o.numberOfInstances, true)
           && compareDeep(clinicalInformation, o.clinicalInformation, true) && compareDeep(procedure, o.procedure, true)
           && compareDeep(interpreter, o.interpreter, true) && compareDeep(description, o.description, true)
           && compareDeep(series, o.series, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingStudy))
          return false;
        ImagingStudy o = (ImagingStudy) other;
        return compareValues(started, o.started, true) && compareValues(uid, o.uid, true) && compareValues(availability, o.availability, true)
           && compareValues(url, o.url, true) && compareValues(numberOfSeries, o.numberOfSeries, true) && compareValues(numberOfInstances, o.numberOfInstances, true)
           && compareValues(clinicalInformation, o.clinicalInformation, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (started == null || started.isEmpty()) && (patient == null || patient.isEmpty())
           && (uid == null || uid.isEmpty()) && (accession == null || accession.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (order == null || order.isEmpty()) && (modalityList == null || modalityList.isEmpty())
           && (referrer == null || referrer.isEmpty()) && (availability == null || availability.isEmpty())
           && (url == null || url.isEmpty()) && (numberOfSeries == null || numberOfSeries.isEmpty())
           && (numberOfInstances == null || numberOfInstances.isEmpty()) && (clinicalInformation == null || clinicalInformation.isEmpty())
           && (procedure == null || procedure.isEmpty()) && (interpreter == null || interpreter.isEmpty())
           && (description == null || description.isEmpty()) && (series == null || series.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImagingStudy;
   }

  @SearchParamDefinition(name="uid", path="ImagingStudy.series.instance.uid", description="Formal identifier for this instance (0008,0018)", type="token" )
  public static final String SP_UID = "uid";
  @SearchParamDefinition(name="study", path="ImagingStudy.uid", description="The study id for the image", type="token" )
  public static final String SP_STUDY = "study";
  @SearchParamDefinition(name="dicom-class", path="ImagingStudy.series.instance.sopclass", description="DICOM class type (0008,0016)", type="token" )
  public static final String SP_DICOMCLASS = "dicom-class";
  @SearchParamDefinition(name="modality", path="ImagingStudy.series.modality", description="The modality of the image", type="token" )
  public static final String SP_MODALITY = "modality";
  @SearchParamDefinition(name="bodysite", path="ImagingStudy.series.bodySite", description="Body part examined (Map from 0018,0015)", type="token" )
  public static final String SP_BODYSITE = "bodysite";
  @SearchParamDefinition(name="patient", path="ImagingStudy.patient", description="Who the study is about", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="series", path="ImagingStudy.series.uid", description="The series id for the image", type="token" )
  public static final String SP_SERIES = "series";
  @SearchParamDefinition(name="started", path="ImagingStudy.started", description="When the study was started", type="date" )
  public static final String SP_STARTED = "started";
  @SearchParamDefinition(name="accession", path="ImagingStudy.accession", description="The accession id for the image", type="token" )
  public static final String SP_ACCESSION = "accession";
  @SearchParamDefinition(name="order", path="ImagingStudy.order", description="The order for the image", type="reference" )
  public static final String SP_ORDER = "order";

}

