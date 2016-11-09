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

// Generated on Sat, Nov 5, 2016 10:42-0400 for FHIR v1.7.0

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
 * Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a common context.  A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple series of different modalities.
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
         * added to help the parsers with the generic types
         */
        NULL;
        public static InstanceAvailability fromCode(String codeString) throws FHIRException {
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown InstanceAvailability code '"+codeString+"'");
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
        public Enumeration<InstanceAvailability> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("ONLINE".equals(codeString))
          return new Enumeration<InstanceAvailability>(this, InstanceAvailability.ONLINE);
        if ("OFFLINE".equals(codeString))
          return new Enumeration<InstanceAvailability>(this, InstanceAvailability.OFFLINE);
        if ("NEARLINE".equals(codeString))
          return new Enumeration<InstanceAvailability>(this, InstanceAvailability.NEARLINE);
        if ("UNAVAILABLE".equals(codeString))
          return new Enumeration<InstanceAvailability>(this, InstanceAvailability.UNAVAILABLE);
        throw new FHIRException("Unknown InstanceAvailability code '"+codeString+"'");
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
    public String toSystem(InstanceAvailability code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class StudyBaseLocationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The service type for accessing (e.g., retrieving, viewing) the DICOM instances.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="WADO-RS | WADO-URI | IID", formalDefinition="The service type for accessing (e.g., retrieving, viewing) the DICOM instances." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/dWebType")
        protected Coding type;

        /**
         * The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.baseLocation.type.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Study access URL", formalDefinition="The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.baseLocation.type." )
        protected UriType url;

        private static final long serialVersionUID = 1242620527L;

    /**
     * Constructor
     */
      public StudyBaseLocationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StudyBaseLocationComponent(Coding type, UriType url) {
        super();
        this.type = type;
        this.url = url;
      }

        /**
         * @return {@link #type} (The service type for accessing (e.g., retrieving, viewing) the DICOM instances.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyBaseLocationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The service type for accessing (e.g., retrieving, viewing) the DICOM instances.)
         */
        public StudyBaseLocationComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #url} (The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.baseLocation.type.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyBaseLocationComponent.url");
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
         * @param value {@link #url} (The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.baseLocation.type.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public StudyBaseLocationComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.baseLocation.type.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.baseLocation.type.
         */
        public StudyBaseLocationComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "The service type for accessing (e.g., retrieving, viewing) the DICOM instances.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("url", "uri", "The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.baseLocation.type.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // Coding
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.url");
        }
        else
          return super.addChild(name);
      }

      public StudyBaseLocationComponent copy() {
        StudyBaseLocationComponent dst = new StudyBaseLocationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StudyBaseLocationComponent))
          return false;
        StudyBaseLocationComponent o = (StudyBaseLocationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StudyBaseLocationComponent))
          return false;
        StudyBaseLocationComponent o = (StudyBaseLocationComponent) other;
        return compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, url);
      }

  public String fhirType() {
    return "ImagingStudy.baseLocation";

  }

  }

    @Block()
    public static class ImagingStudySeriesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Formal identifier for this series.
         */
        @Child(name = "uid", type = {OidType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Formal DICOM identifier for this series", formalDefinition="Formal identifier for this series." )
        protected OidType uid;

        /**
         * The Numeric identifier of this series in the study.
         */
        @Child(name = "number", type = {UnsignedIntType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Numeric identifier of this series", formalDefinition="The Numeric identifier of this series in the study." )
        protected UnsignedIntType number;

        /**
         * The modality of this series sequence.
         */
        @Child(name = "modality", type = {Coding.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The modality of the instances in the series", formalDefinition="The modality of this series sequence." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/dicom-cid29")
        protected Coding modality;

        /**
         * A description of the series.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A short human readable summary of the series", formalDefinition="A description of the series." )
        protected StringType description;

        /**
         * Number of SOP Instances in Series.
         */
        @Child(name = "numberOfInstances", type = {UnsignedIntType.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Number of Series Related Instances", formalDefinition="Number of SOP Instances in Series." )
        protected UnsignedIntType numberOfInstances;

        /**
         * Availability of series (online, offline or nearline).
         */
        @Child(name = "availability", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="ONLINE | OFFLINE | NEARLINE | UNAVAILABLE", formalDefinition="Availability of series (online, offline or nearline)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/instance-availability")
        protected Enumeration<InstanceAvailability> availability;

        /**
         * Methods of accessing (e.g. retrieving) the series.
         */
        @Child(name = "baseLocation", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Series access endpoint", formalDefinition="Methods of accessing (e.g. retrieving) the series." )
        protected List<SeriesBaseLocationComponent> baseLocation;

        /**
         * Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed CT.
         */
        @Child(name = "bodySite", type = {Coding.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Body part examined", formalDefinition="Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed CT." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
        protected Coding bodySite;

        /**
         * Laterality if body site is paired anatomic structure and laterality is not pre-coordinated in body site code.
         */
        @Child(name = "laterality", type = {Coding.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Body part laterality", formalDefinition="Laterality if body site is paired anatomic structure and laterality is not pre-coordinated in body site code." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/bodysite-laterality")
        protected Coding laterality;

        /**
         * The date and time the series was started.
         */
        @Child(name = "started", type = {DateTimeType.class}, order=10, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When the series started", formalDefinition="The date and time the series was started." )
        protected DateTimeType started;

        /**
         * A single SOP Instance within the series, e.g. an image, or presentation state.
         */
        @Child(name = "instance", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A single SOP instance from the series", formalDefinition="A single SOP Instance within the series, e.g. an image, or presentation state." )
        protected List<ImagingStudySeriesInstanceComponent> instance;

        private static final long serialVersionUID = 1092135078L;

    /**
     * Constructor
     */
      public ImagingStudySeriesComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImagingStudySeriesComponent(OidType uid, Coding modality, UnsignedIntType numberOfInstances) {
        super();
        this.uid = uid;
        this.modality = modality;
        this.numberOfInstances = numberOfInstances;
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
         * @return {@link #baseLocation} (Methods of accessing (e.g. retrieving) the series.)
         */
        public List<SeriesBaseLocationComponent> getBaseLocation() { 
          if (this.baseLocation == null)
            this.baseLocation = new ArrayList<SeriesBaseLocationComponent>();
          return this.baseLocation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImagingStudySeriesComponent setBaseLocation(List<SeriesBaseLocationComponent> theBaseLocation) { 
          this.baseLocation = theBaseLocation;
          return this;
        }

        public boolean hasBaseLocation() { 
          if (this.baseLocation == null)
            return false;
          for (SeriesBaseLocationComponent item : this.baseLocation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SeriesBaseLocationComponent addBaseLocation() { //3
          SeriesBaseLocationComponent t = new SeriesBaseLocationComponent();
          if (this.baseLocation == null)
            this.baseLocation = new ArrayList<SeriesBaseLocationComponent>();
          this.baseLocation.add(t);
          return t;
        }

        public ImagingStudySeriesComponent addBaseLocation(SeriesBaseLocationComponent t) { //3
          if (t == null)
            return this;
          if (this.baseLocation == null)
            this.baseLocation = new ArrayList<SeriesBaseLocationComponent>();
          this.baseLocation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #baseLocation}, creating it if it does not already exist
         */
        public SeriesBaseLocationComponent getBaseLocationFirstRep() { 
          if (getBaseLocation().isEmpty()) {
            addBaseLocation();
          }
          return getBaseLocation().get(0);
        }

        /**
         * @return {@link #bodySite} (Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed CT.)
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
         * @param value {@link #bodySite} (Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed CT.)
         */
        public ImagingStudySeriesComponent setBodySite(Coding value) { 
          this.bodySite = value;
          return this;
        }

        /**
         * @return {@link #laterality} (Laterality if body site is paired anatomic structure and laterality is not pre-coordinated in body site code.)
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
         * @param value {@link #laterality} (Laterality if body site is paired anatomic structure and laterality is not pre-coordinated in body site code.)
         */
        public ImagingStudySeriesComponent setLaterality(Coding value) { 
          this.laterality = value;
          return this;
        }

        /**
         * @return {@link #started} (The date and time the series was started.). This is the underlying object with id, value and extensions. The accessor "getStarted" gives direct access to the value
         */
        public DateTimeType getStartedElement() { 
          if (this.started == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesComponent.started");
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
         * @param value {@link #started} (The date and time the series was started.). This is the underlying object with id, value and extensions. The accessor "getStarted" gives direct access to the value
         */
        public ImagingStudySeriesComponent setStartedElement(DateTimeType value) { 
          this.started = value;
          return this;
        }

        /**
         * @return The date and time the series was started.
         */
        public Date getStarted() { 
          return this.started == null ? null : this.started.getValue();
        }

        /**
         * @param value The date and time the series was started.
         */
        public ImagingStudySeriesComponent setStarted(Date value) { 
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
         * @return {@link #instance} (A single SOP Instance within the series, e.g. an image, or presentation state.)
         */
        public List<ImagingStudySeriesInstanceComponent> getInstance() { 
          if (this.instance == null)
            this.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          return this.instance;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImagingStudySeriesComponent setInstance(List<ImagingStudySeriesInstanceComponent> theInstance) { 
          this.instance = theInstance;
          return this;
        }

        public boolean hasInstance() { 
          if (this.instance == null)
            return false;
          for (ImagingStudySeriesInstanceComponent item : this.instance)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImagingStudySeriesInstanceComponent addInstance() { //3
          ImagingStudySeriesInstanceComponent t = new ImagingStudySeriesInstanceComponent();
          if (this.instance == null)
            this.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          this.instance.add(t);
          return t;
        }

        public ImagingStudySeriesComponent addInstance(ImagingStudySeriesInstanceComponent t) { //3
          if (t == null)
            return this;
          if (this.instance == null)
            this.instance = new ArrayList<ImagingStudySeriesInstanceComponent>();
          this.instance.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #instance}, creating it if it does not already exist
         */
        public ImagingStudySeriesInstanceComponent getInstanceFirstRep() { 
          if (getInstance().isEmpty()) {
            addInstance();
          }
          return getInstance().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uid", "oid", "Formal identifier for this series.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("number", "unsignedInt", "The Numeric identifier of this series in the study.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("modality", "Coding", "The modality of this series sequence.", 0, java.lang.Integer.MAX_VALUE, modality));
          childrenList.add(new Property("description", "string", "A description of the series.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("numberOfInstances", "unsignedInt", "Number of SOP Instances in Series.", 0, java.lang.Integer.MAX_VALUE, numberOfInstances));
          childrenList.add(new Property("availability", "code", "Availability of series (online, offline or nearline).", 0, java.lang.Integer.MAX_VALUE, availability));
          childrenList.add(new Property("baseLocation", "", "Methods of accessing (e.g. retrieving) the series.", 0, java.lang.Integer.MAX_VALUE, baseLocation));
          childrenList.add(new Property("bodySite", "Coding", "Body part examined. See  DICOM Part 16 Annex L for the mapping from DICOM to Snomed CT.", 0, java.lang.Integer.MAX_VALUE, bodySite));
          childrenList.add(new Property("laterality", "Coding", "Laterality if body site is paired anatomic structure and laterality is not pre-coordinated in body site code.", 0, java.lang.Integer.MAX_VALUE, laterality));
          childrenList.add(new Property("started", "dateTime", "The date and time the series was started.", 0, java.lang.Integer.MAX_VALUE, started));
          childrenList.add(new Property("instance", "", "A single SOP Instance within the series, e.g. an image, or presentation state.", 0, java.lang.Integer.MAX_VALUE, instance));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : new Base[] {this.number}; // UnsignedIntType
        case -622722335: /*modality*/ return this.modality == null ? new Base[0] : new Base[] {this.modality}; // Coding
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1043544226: /*numberOfInstances*/ return this.numberOfInstances == null ? new Base[0] : new Base[] {this.numberOfInstances}; // UnsignedIntType
        case 1997542747: /*availability*/ return this.availability == null ? new Base[0] : new Base[] {this.availability}; // Enumeration<InstanceAvailability>
        case 231778726: /*baseLocation*/ return this.baseLocation == null ? new Base[0] : this.baseLocation.toArray(new Base[this.baseLocation.size()]); // SeriesBaseLocationComponent
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // Coding
        case -170291817: /*laterality*/ return this.laterality == null ? new Base[0] : new Base[] {this.laterality}; // Coding
        case -1897185151: /*started*/ return this.started == null ? new Base[0] : new Base[] {this.started}; // DateTimeType
        case 555127957: /*instance*/ return this.instance == null ? new Base[0] : this.instance.toArray(new Base[this.instance.size()]); // ImagingStudySeriesInstanceComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          break;
        case -1034364087: // number
          this.number = castToUnsignedInt(value); // UnsignedIntType
          break;
        case -622722335: // modality
          this.modality = castToCoding(value); // Coding
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -1043544226: // numberOfInstances
          this.numberOfInstances = castToUnsignedInt(value); // UnsignedIntType
          break;
        case 1997542747: // availability
          this.availability = new InstanceAvailabilityEnumFactory().fromType(value); // Enumeration<InstanceAvailability>
          break;
        case 231778726: // baseLocation
          this.getBaseLocation().add((SeriesBaseLocationComponent) value); // SeriesBaseLocationComponent
          break;
        case 1702620169: // bodySite
          this.bodySite = castToCoding(value); // Coding
          break;
        case -170291817: // laterality
          this.laterality = castToCoding(value); // Coding
          break;
        case -1897185151: // started
          this.started = castToDateTime(value); // DateTimeType
          break;
        case 555127957: // instance
          this.getInstance().add((ImagingStudySeriesInstanceComponent) value); // ImagingStudySeriesInstanceComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("uid"))
          this.uid = castToOid(value); // OidType
        else if (name.equals("number"))
          this.number = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("modality"))
          this.modality = castToCoding(value); // Coding
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("numberOfInstances"))
          this.numberOfInstances = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("availability"))
          this.availability = new InstanceAvailabilityEnumFactory().fromType(value); // Enumeration<InstanceAvailability>
        else if (name.equals("baseLocation"))
          this.getBaseLocation().add((SeriesBaseLocationComponent) value);
        else if (name.equals("bodySite"))
          this.bodySite = castToCoding(value); // Coding
        else if (name.equals("laterality"))
          this.laterality = castToCoding(value); // Coding
        else if (name.equals("started"))
          this.started = castToDateTime(value); // DateTimeType
        else if (name.equals("instance"))
          this.getInstance().add((ImagingStudySeriesInstanceComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case -1034364087: throw new FHIRException("Cannot make property number as it is not a complex type"); // UnsignedIntType
        case -622722335:  return getModality(); // Coding
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -1043544226: throw new FHIRException("Cannot make property numberOfInstances as it is not a complex type"); // UnsignedIntType
        case 1997542747: throw new FHIRException("Cannot make property availability as it is not a complex type"); // Enumeration<InstanceAvailability>
        case 231778726:  return addBaseLocation(); // SeriesBaseLocationComponent
        case 1702620169:  return getBodySite(); // Coding
        case -170291817:  return getLaterality(); // Coding
        case -1897185151: throw new FHIRException("Cannot make property started as it is not a complex type"); // DateTimeType
        case 555127957:  return addInstance(); // ImagingStudySeriesInstanceComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.uid");
        }
        else if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.number");
        }
        else if (name.equals("modality")) {
          this.modality = new Coding();
          return this.modality;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.description");
        }
        else if (name.equals("numberOfInstances")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.numberOfInstances");
        }
        else if (name.equals("availability")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.availability");
        }
        else if (name.equals("baseLocation")) {
          return addBaseLocation();
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new Coding();
          return this.bodySite;
        }
        else if (name.equals("laterality")) {
          this.laterality = new Coding();
          return this.laterality;
        }
        else if (name.equals("started")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.started");
        }
        else if (name.equals("instance")) {
          return addInstance();
        }
        else
          return super.addChild(name);
      }

      public ImagingStudySeriesComponent copy() {
        ImagingStudySeriesComponent dst = new ImagingStudySeriesComponent();
        copyValues(dst);
        dst.uid = uid == null ? null : uid.copy();
        dst.number = number == null ? null : number.copy();
        dst.modality = modality == null ? null : modality.copy();
        dst.description = description == null ? null : description.copy();
        dst.numberOfInstances = numberOfInstances == null ? null : numberOfInstances.copy();
        dst.availability = availability == null ? null : availability.copy();
        if (baseLocation != null) {
          dst.baseLocation = new ArrayList<SeriesBaseLocationComponent>();
          for (SeriesBaseLocationComponent i : baseLocation)
            dst.baseLocation.add(i.copy());
        };
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        dst.laterality = laterality == null ? null : laterality.copy();
        dst.started = started == null ? null : started.copy();
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
        return compareDeep(uid, o.uid, true) && compareDeep(number, o.number, true) && compareDeep(modality, o.modality, true)
           && compareDeep(description, o.description, true) && compareDeep(numberOfInstances, o.numberOfInstances, true)
           && compareDeep(availability, o.availability, true) && compareDeep(baseLocation, o.baseLocation, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(laterality, o.laterality, true) && compareDeep(started, o.started, true)
           && compareDeep(instance, o.instance, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingStudySeriesComponent))
          return false;
        ImagingStudySeriesComponent o = (ImagingStudySeriesComponent) other;
        return compareValues(uid, o.uid, true) && compareValues(number, o.number, true) && compareValues(description, o.description, true)
           && compareValues(numberOfInstances, o.numberOfInstances, true) && compareValues(availability, o.availability, true)
           && compareValues(started, o.started, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(uid, number, modality, description
          , numberOfInstances, availability, baseLocation, bodySite, laterality, started, instance
          );
      }

  public String fhirType() {
    return "ImagingStudy.series";

  }

  }

    @Block()
    public static class SeriesBaseLocationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The service type for accessing (e.g., retrieving) the DICOM instances.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="WADO-RS | WADO-URI | IID", formalDefinition="The service type for accessing (e.g., retrieving) the DICOM instances." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/dWebType")
        protected Coding type;

        /**
         * The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.series.baseLocation.type.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Series access URL", formalDefinition="The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.series.baseLocation.type." )
        protected UriType url;

        private static final long serialVersionUID = 1242620527L;

    /**
     * Constructor
     */
      public SeriesBaseLocationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SeriesBaseLocationComponent(Coding type, UriType url) {
        super();
        this.type = type;
        this.url = url;
      }

        /**
         * @return {@link #type} (The service type for accessing (e.g., retrieving) the DICOM instances.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SeriesBaseLocationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The service type for accessing (e.g., retrieving) the DICOM instances.)
         */
        public SeriesBaseLocationComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #url} (The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.series.baseLocation.type.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SeriesBaseLocationComponent.url");
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
         * @param value {@link #url} (The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.series.baseLocation.type.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public SeriesBaseLocationComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.series.baseLocation.type.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.series.baseLocation.type.
         */
        public SeriesBaseLocationComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "The service type for accessing (e.g., retrieving) the DICOM instances.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("url", "uri", "The service URL for accessing the study. The interpretation of the URL depends on the type of the service specified in ImagingManifest.study.series.baseLocation.type.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // Coding
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.url");
        }
        else
          return super.addChild(name);
      }

      public SeriesBaseLocationComponent copy() {
        SeriesBaseLocationComponent dst = new SeriesBaseLocationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SeriesBaseLocationComponent))
          return false;
        SeriesBaseLocationComponent o = (SeriesBaseLocationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SeriesBaseLocationComponent))
          return false;
        SeriesBaseLocationComponent o = (SeriesBaseLocationComponent) other;
        return compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, url);
      }

  public String fhirType() {
    return "ImagingStudy.series.baseLocation";

  }

  }

    @Block()
    public static class ImagingStudySeriesInstanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Formal identifier for this image or other content.
         */
        @Child(name = "uid", type = {OidType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Formal DICOM identifier for this instance", formalDefinition="Formal identifier for this image or other content." )
        protected OidType uid;

        /**
         * The number of instance in the series.
         */
        @Child(name = "number", type = {UnsignedIntType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The number of this instance in the series", formalDefinition="The number of instance in the series." )
        protected UnsignedIntType number;

        /**
         * DICOM instance  type.
         */
        @Child(name = "sopClass", type = {OidType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="DICOM class type", formalDefinition="DICOM instance  type." )
        protected OidType sopClass;

        /**
         * The description of the instance.
         */
        @Child(name = "title", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of instance", formalDefinition="The description of the instance." )
        protected StringType title;

        private static final long serialVersionUID = -771526344L;

    /**
     * Constructor
     */
      public ImagingStudySeriesInstanceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImagingStudySeriesInstanceComponent(OidType uid, OidType sopClass) {
        super();
        this.uid = uid;
        this.sopClass = sopClass;
      }

        /**
         * @return {@link #uid} (Formal identifier for this image or other content.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
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
         * @param value {@link #uid} (Formal identifier for this image or other content.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return Formal identifier for this image or other content.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value Formal identifier for this image or other content.
         */
        public ImagingStudySeriesInstanceComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #number} (The number of instance in the series.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
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
         * @param value {@link #number} (The number of instance in the series.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setNumberElement(UnsignedIntType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return The number of instance in the series.
         */
        public int getNumber() { 
          return this.number == null || this.number.isEmpty() ? 0 : this.number.getValue();
        }

        /**
         * @param value The number of instance in the series.
         */
        public ImagingStudySeriesInstanceComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new UnsignedIntType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #sopClass} (DICOM instance  type.). This is the underlying object with id, value and extensions. The accessor "getSopClass" gives direct access to the value
         */
        public OidType getSopClassElement() { 
          if (this.sopClass == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImagingStudySeriesInstanceComponent.sopClass");
            else if (Configuration.doAutoCreate())
              this.sopClass = new OidType(); // bb
          return this.sopClass;
        }

        public boolean hasSopClassElement() { 
          return this.sopClass != null && !this.sopClass.isEmpty();
        }

        public boolean hasSopClass() { 
          return this.sopClass != null && !this.sopClass.isEmpty();
        }

        /**
         * @param value {@link #sopClass} (DICOM instance  type.). This is the underlying object with id, value and extensions. The accessor "getSopClass" gives direct access to the value
         */
        public ImagingStudySeriesInstanceComponent setSopClassElement(OidType value) { 
          this.sopClass = value;
          return this;
        }

        /**
         * @return DICOM instance  type.
         */
        public String getSopClass() { 
          return this.sopClass == null ? null : this.sopClass.getValue();
        }

        /**
         * @param value DICOM instance  type.
         */
        public ImagingStudySeriesInstanceComponent setSopClass(String value) { 
            if (this.sopClass == null)
              this.sopClass = new OidType();
            this.sopClass.setValue(value);
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uid", "oid", "Formal identifier for this image or other content.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("number", "unsignedInt", "The number of instance in the series.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("sopClass", "oid", "DICOM instance  type.", 0, java.lang.Integer.MAX_VALUE, sopClass));
          childrenList.add(new Property("title", "string", "The description of the instance.", 0, java.lang.Integer.MAX_VALUE, title));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : new Base[] {this.number}; // UnsignedIntType
        case 1560041540: /*sopClass*/ return this.sopClass == null ? new Base[0] : new Base[] {this.sopClass}; // OidType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          break;
        case -1034364087: // number
          this.number = castToUnsignedInt(value); // UnsignedIntType
          break;
        case 1560041540: // sopClass
          this.sopClass = castToOid(value); // OidType
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("uid"))
          this.uid = castToOid(value); // OidType
        else if (name.equals("number"))
          this.number = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("sopClass"))
          this.sopClass = castToOid(value); // OidType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case -1034364087: throw new FHIRException("Cannot make property number as it is not a complex type"); // UnsignedIntType
        case 1560041540: throw new FHIRException("Cannot make property sopClass as it is not a complex type"); // OidType
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.uid");
        }
        else if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.number");
        }
        else if (name.equals("sopClass")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.sopClass");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.title");
        }
        else
          return super.addChild(name);
      }

      public ImagingStudySeriesInstanceComponent copy() {
        ImagingStudySeriesInstanceComponent dst = new ImagingStudySeriesInstanceComponent();
        copyValues(dst);
        dst.uid = uid == null ? null : uid.copy();
        dst.number = number == null ? null : number.copy();
        dst.sopClass = sopClass == null ? null : sopClass.copy();
        dst.title = title == null ? null : title.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingStudySeriesInstanceComponent))
          return false;
        ImagingStudySeriesInstanceComponent o = (ImagingStudySeriesInstanceComponent) other;
        return compareDeep(uid, o.uid, true) && compareDeep(number, o.number, true) && compareDeep(sopClass, o.sopClass, true)
           && compareDeep(title, o.title, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingStudySeriesInstanceComponent))
          return false;
        ImagingStudySeriesInstanceComponent o = (ImagingStudySeriesInstanceComponent) other;
        return compareValues(uid, o.uid, true) && compareValues(number, o.number, true) && compareValues(sopClass, o.sopClass, true)
           && compareValues(title, o.title, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(uid, number, sopClass, title
          );
      }

  public String fhirType() {
    return "ImagingStudy.series.instance";

  }

  }

    /**
     * Formal identifier for the study.
     */
    @Child(name = "uid", type = {OidType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Formal DICOM identifier for the study", formalDefinition="Formal identifier for the study." )
    protected OidType uid;

    /**
     * Accession Number is an identifier related to some aspect of imaging workflow and data management. Usage may vary across different institutions.  See for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf).
     */
    @Child(name = "accession", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Related workflow identifier (\"Accession Number\")", formalDefinition="Accession Number is an identifier related to some aspect of imaging workflow and data management. Usage may vary across different institutions.  See for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf)." )
    protected Identifier accession;

    /**
     * Other identifiers for the study.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Other identifiers for the study", formalDefinition="Other identifiers for the study." )
    protected List<Identifier> identifier;

    /**
     * Availability of study (online, offline or nearline).
     */
    @Child(name = "availability", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="ONLINE | OFFLINE | NEARLINE | UNAVAILABLE", formalDefinition="Availability of study (online, offline or nearline)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/instance-availability")
    protected Enumeration<InstanceAvailability> availability;

    /**
     * A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).
     */
    @Child(name = "modalityList", type = {Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="All series modality if actual acquisition modalities", formalDefinition="A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/dicom-cid29")
    protected List<Coding> modalityList;

    /**
     * The patient imaged in the study.
     */
    @Child(name = "patient", type = {Patient.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the images are of", formalDefinition="The patient imaged in the study." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient imaged in the study.)
     */
    protected Patient patientTarget;

    /**
     * The encounter at which the request is initiated.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Originating context", formalDefinition="The encounter at which the request is initiated." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter at which the request is initiated.)
     */
    protected Resource contextTarget;

    /**
     * Date and Time the study started.
     */
    @Child(name = "started", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the study was started", formalDefinition="Date and Time the study started." )
    protected DateTimeType started;

    /**
     * A list of the diagnostic requests that resulted in this imaging study being performed.
     */
    @Child(name = "basedOn", type = {ReferralRequest.class, CarePlan.class, DiagnosticRequest.class, ProcedureRequest.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Request fulfilled", formalDefinition="A list of the diagnostic requests that resulted in this imaging study being performed." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (A list of the diagnostic requests that resulted in this imaging study being performed.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * The requesting/referring physician.
     */
    @Child(name = "referrer", type = {Practitioner.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Referring physician", formalDefinition="The requesting/referring physician." )
    protected Reference referrer;

    /**
     * The actual object that is the target of the reference (The requesting/referring physician.)
     */
    protected Practitioner referrerTarget;

    /**
     * Who read the study and interpreted the images or other content.
     */
    @Child(name = "interpreter", type = {Practitioner.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who interpreted images", formalDefinition="Who read the study and interpreted the images or other content." )
    protected List<Reference> interpreter;
    /**
     * The actual objects that are the target of the reference (Who read the study and interpreted the images or other content.)
     */
    protected List<Practitioner> interpreterTarget;


    /**
     * Methods of accessing  (e.g., retrieving, viewing) the study.
     */
    @Child(name = "baseLocation", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Study access service endpoint", formalDefinition="Methods of accessing  (e.g., retrieving, viewing) the study." )
    protected List<StudyBaseLocationComponent> baseLocation;

    /**
     * Number of Series in Study.
     */
    @Child(name = "numberOfSeries", type = {UnsignedIntType.class}, order=12, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of Study Related Series", formalDefinition="Number of Series in Study." )
    protected UnsignedIntType numberOfSeries;

    /**
     * Number of SOP Instances in Study.
     */
    @Child(name = "numberOfInstances", type = {UnsignedIntType.class}, order=13, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of Study Related Instances", formalDefinition="Number of SOP Instances in Study." )
    protected UnsignedIntType numberOfInstances;

    /**
     * Type of procedure performed.
     */
    @Child(name = "procedure", type = {Procedure.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Type of procedure performed", formalDefinition="Type of procedure performed." )
    protected List<Reference> procedure;
    /**
     * The actual objects that are the target of the reference (Type of procedure performed.)
     */
    protected List<Procedure> procedureTarget;


    /**
     * Description of clinical codition indicating why the ImagingStudy was requested.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for study", formalDefinition="Description of clinical codition indicating why the ImagingStudy was requested." )
    protected CodeableConcept reason;

    /**
     * Institution-generated description or classification of the Study performed.
     */
    @Child(name = "description", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Institution-generated description", formalDefinition="Institution-generated description or classification of the Study performed." )
    protected StringType description;

    /**
     * Each study has one or more series of images or other content.
     */
    @Child(name = "series", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Each study has one or more series of instances", formalDefinition="Each study has one or more series of images or other content." )
    protected List<ImagingStudySeriesComponent> series;

    private static final long serialVersionUID = -1223643343L;

  /**
   * Constructor
   */
    public ImagingStudy() {
      super();
    }

  /**
   * Constructor
   */
    public ImagingStudy(OidType uid, Reference patient, UnsignedIntType numberOfSeries, UnsignedIntType numberOfInstances) {
      super();
      this.uid = uid;
      this.patient = patient;
      this.numberOfSeries = numberOfSeries;
      this.numberOfInstances = numberOfInstances;
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
     * @return {@link #accession} (Accession Number is an identifier related to some aspect of imaging workflow and data management. Usage may vary across different institutions.  See for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf).)
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
     * @param value {@link #accession} (Accession Number is an identifier related to some aspect of imaging workflow and data management. Usage may vary across different institutions.  See for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf).)
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImagingStudy setIdentifier(List<Identifier> theIdentifier) { 
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

    public ImagingStudy addIdentifier(Identifier t) { //3
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
     * @return {@link #modalityList} (A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).)
     */
    public List<Coding> getModalityList() { 
      if (this.modalityList == null)
        this.modalityList = new ArrayList<Coding>();
      return this.modalityList;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImagingStudy setModalityList(List<Coding> theModalityList) { 
      this.modalityList = theModalityList;
      return this;
    }

    public boolean hasModalityList() { 
      if (this.modalityList == null)
        return false;
      for (Coding item : this.modalityList)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Coding addModalityList() { //3
      Coding t = new Coding();
      if (this.modalityList == null)
        this.modalityList = new ArrayList<Coding>();
      this.modalityList.add(t);
      return t;
    }

    public ImagingStudy addModalityList(Coding t) { //3
      if (t == null)
        return this;
      if (this.modalityList == null)
        this.modalityList = new ArrayList<Coding>();
      this.modalityList.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #modalityList}, creating it if it does not already exist
     */
    public Coding getModalityListFirstRep() { 
      if (getModalityList().isEmpty()) {
        addModalityList();
      }
      return getModalityList().get(0);
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
     * @return {@link #context} (The encounter at which the request is initiated.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The encounter at which the request is initiated.)
     */
    public ImagingStudy setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter at which the request is initiated.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter at which the request is initiated.)
     */
    public ImagingStudy setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #started} (Date and Time the study started.). This is the underlying object with id, value and extensions. The accessor "getStarted" gives direct access to the value
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
     * @param value {@link #started} (Date and Time the study started.). This is the underlying object with id, value and extensions. The accessor "getStarted" gives direct access to the value
     */
    public ImagingStudy setStartedElement(DateTimeType value) { 
      this.started = value;
      return this;
    }

    /**
     * @return Date and Time the study started.
     */
    public Date getStarted() { 
      return this.started == null ? null : this.started.getValue();
    }

    /**
     * @param value Date and Time the study started.
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
     * @return {@link #basedOn} (A list of the diagnostic requests that resulted in this imaging study being performed.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImagingStudy setBasedOn(List<Reference> theBasedOn) { 
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

    public ImagingStudy addBasedOn(Reference t) { //3
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
     * @return {@link #interpreter} (Who read the study and interpreted the images or other content.)
     */
    public List<Reference> getInterpreter() { 
      if (this.interpreter == null)
        this.interpreter = new ArrayList<Reference>();
      return this.interpreter;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImagingStudy setInterpreter(List<Reference> theInterpreter) { 
      this.interpreter = theInterpreter;
      return this;
    }

    public boolean hasInterpreter() { 
      if (this.interpreter == null)
        return false;
      for (Reference item : this.interpreter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addInterpreter() { //3
      Reference t = new Reference();
      if (this.interpreter == null)
        this.interpreter = new ArrayList<Reference>();
      this.interpreter.add(t);
      return t;
    }

    public ImagingStudy addInterpreter(Reference t) { //3
      if (t == null)
        return this;
      if (this.interpreter == null)
        this.interpreter = new ArrayList<Reference>();
      this.interpreter.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #interpreter}, creating it if it does not already exist
     */
    public Reference getInterpreterFirstRep() { 
      if (getInterpreter().isEmpty()) {
        addInterpreter();
      }
      return getInterpreter().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Practitioner> getInterpreterTarget() { 
      if (this.interpreterTarget == null)
        this.interpreterTarget = new ArrayList<Practitioner>();
      return this.interpreterTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Practitioner addInterpreterTarget() { 
      Practitioner r = new Practitioner();
      if (this.interpreterTarget == null)
        this.interpreterTarget = new ArrayList<Practitioner>();
      this.interpreterTarget.add(r);
      return r;
    }

    /**
     * @return {@link #baseLocation} (Methods of accessing  (e.g., retrieving, viewing) the study.)
     */
    public List<StudyBaseLocationComponent> getBaseLocation() { 
      if (this.baseLocation == null)
        this.baseLocation = new ArrayList<StudyBaseLocationComponent>();
      return this.baseLocation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImagingStudy setBaseLocation(List<StudyBaseLocationComponent> theBaseLocation) { 
      this.baseLocation = theBaseLocation;
      return this;
    }

    public boolean hasBaseLocation() { 
      if (this.baseLocation == null)
        return false;
      for (StudyBaseLocationComponent item : this.baseLocation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public StudyBaseLocationComponent addBaseLocation() { //3
      StudyBaseLocationComponent t = new StudyBaseLocationComponent();
      if (this.baseLocation == null)
        this.baseLocation = new ArrayList<StudyBaseLocationComponent>();
      this.baseLocation.add(t);
      return t;
    }

    public ImagingStudy addBaseLocation(StudyBaseLocationComponent t) { //3
      if (t == null)
        return this;
      if (this.baseLocation == null)
        this.baseLocation = new ArrayList<StudyBaseLocationComponent>();
      this.baseLocation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #baseLocation}, creating it if it does not already exist
     */
    public StudyBaseLocationComponent getBaseLocationFirstRep() { 
      if (getBaseLocation().isEmpty()) {
        addBaseLocation();
      }
      return getBaseLocation().get(0);
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
     * @return {@link #procedure} (Type of procedure performed.)
     */
    public List<Reference> getProcedure() { 
      if (this.procedure == null)
        this.procedure = new ArrayList<Reference>();
      return this.procedure;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImagingStudy setProcedure(List<Reference> theProcedure) { 
      this.procedure = theProcedure;
      return this;
    }

    public boolean hasProcedure() { 
      if (this.procedure == null)
        return false;
      for (Reference item : this.procedure)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addProcedure() { //3
      Reference t = new Reference();
      if (this.procedure == null)
        this.procedure = new ArrayList<Reference>();
      this.procedure.add(t);
      return t;
    }

    public ImagingStudy addProcedure(Reference t) { //3
      if (t == null)
        return this;
      if (this.procedure == null)
        this.procedure = new ArrayList<Reference>();
      this.procedure.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #procedure}, creating it if it does not already exist
     */
    public Reference getProcedureFirstRep() { 
      if (getProcedure().isEmpty()) {
        addProcedure();
      }
      return getProcedure().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Procedure> getProcedureTarget() { 
      if (this.procedureTarget == null)
        this.procedureTarget = new ArrayList<Procedure>();
      return this.procedureTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Procedure addProcedureTarget() { 
      Procedure r = new Procedure();
      if (this.procedureTarget == null)
        this.procedureTarget = new ArrayList<Procedure>();
      this.procedureTarget.add(r);
      return r;
    }

    /**
     * @return {@link #reason} (Description of clinical codition indicating why the ImagingStudy was requested.)
     */
    public CodeableConcept getReason() { 
      if (this.reason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingStudy.reason");
        else if (Configuration.doAutoCreate())
          this.reason = new CodeableConcept(); // cc
      return this.reason;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (Description of clinical codition indicating why the ImagingStudy was requested.)
     */
    public ImagingStudy setReason(CodeableConcept value) { 
      this.reason = value;
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
     * @return {@link #series} (Each study has one or more series of images or other content.)
     */
    public List<ImagingStudySeriesComponent> getSeries() { 
      if (this.series == null)
        this.series = new ArrayList<ImagingStudySeriesComponent>();
      return this.series;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImagingStudy setSeries(List<ImagingStudySeriesComponent> theSeries) { 
      this.series = theSeries;
      return this;
    }

    public boolean hasSeries() { 
      if (this.series == null)
        return false;
      for (ImagingStudySeriesComponent item : this.series)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImagingStudySeriesComponent addSeries() { //3
      ImagingStudySeriesComponent t = new ImagingStudySeriesComponent();
      if (this.series == null)
        this.series = new ArrayList<ImagingStudySeriesComponent>();
      this.series.add(t);
      return t;
    }

    public ImagingStudy addSeries(ImagingStudySeriesComponent t) { //3
      if (t == null)
        return this;
      if (this.series == null)
        this.series = new ArrayList<ImagingStudySeriesComponent>();
      this.series.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #series}, creating it if it does not already exist
     */
    public ImagingStudySeriesComponent getSeriesFirstRep() { 
      if (getSeries().isEmpty()) {
        addSeries();
      }
      return getSeries().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("uid", "oid", "Formal identifier for the study.", 0, java.lang.Integer.MAX_VALUE, uid));
        childrenList.add(new Property("accession", "Identifier", "Accession Number is an identifier related to some aspect of imaging workflow and data management. Usage may vary across different institutions.  See for instance [IHE Radiology Technical Framework Volume 1 Appendix A](http://www.ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Rev13.0_Vol1_FT_2014-07-30.pdf).", 0, java.lang.Integer.MAX_VALUE, accession));
        childrenList.add(new Property("identifier", "Identifier", "Other identifiers for the study.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("availability", "code", "Availability of study (online, offline or nearline).", 0, java.lang.Integer.MAX_VALUE, availability));
        childrenList.add(new Property("modalityList", "Coding", "A list of all the Series.ImageModality values that are actual acquisition modalities, i.e. those in the DICOM Context Group 29 (value set OID 1.2.840.10008.6.1.19).", 0, java.lang.Integer.MAX_VALUE, modalityList));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient imaged in the study.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter at which the request is initiated.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("started", "dateTime", "Date and Time the study started.", 0, java.lang.Integer.MAX_VALUE, started));
        childrenList.add(new Property("basedOn", "Reference(ReferralRequest|CarePlan|DiagnosticRequest|ProcedureRequest)", "A list of the diagnostic requests that resulted in this imaging study being performed.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("referrer", "Reference(Practitioner)", "The requesting/referring physician.", 0, java.lang.Integer.MAX_VALUE, referrer));
        childrenList.add(new Property("interpreter", "Reference(Practitioner)", "Who read the study and interpreted the images or other content.", 0, java.lang.Integer.MAX_VALUE, interpreter));
        childrenList.add(new Property("baseLocation", "", "Methods of accessing  (e.g., retrieving, viewing) the study.", 0, java.lang.Integer.MAX_VALUE, baseLocation));
        childrenList.add(new Property("numberOfSeries", "unsignedInt", "Number of Series in Study.", 0, java.lang.Integer.MAX_VALUE, numberOfSeries));
        childrenList.add(new Property("numberOfInstances", "unsignedInt", "Number of SOP Instances in Study.", 0, java.lang.Integer.MAX_VALUE, numberOfInstances));
        childrenList.add(new Property("procedure", "Reference(Procedure)", "Type of procedure performed.", 0, java.lang.Integer.MAX_VALUE, procedure));
        childrenList.add(new Property("reason", "CodeableConcept", "Description of clinical codition indicating why the ImagingStudy was requested.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("description", "string", "Institution-generated description or classification of the Study performed.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("series", "", "Each study has one or more series of images or other content.", 0, java.lang.Integer.MAX_VALUE, series));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case -2115028956: /*accession*/ return this.accession == null ? new Base[0] : new Base[] {this.accession}; // Identifier
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1997542747: /*availability*/ return this.availability == null ? new Base[0] : new Base[] {this.availability}; // Enumeration<InstanceAvailability>
        case -1030238433: /*modalityList*/ return this.modalityList == null ? new Base[0] : this.modalityList.toArray(new Base[this.modalityList.size()]); // Coding
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -1897185151: /*started*/ return this.started == null ? new Base[0] : new Base[] {this.started}; // DateTimeType
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -722568161: /*referrer*/ return this.referrer == null ? new Base[0] : new Base[] {this.referrer}; // Reference
        case -2008009094: /*interpreter*/ return this.interpreter == null ? new Base[0] : this.interpreter.toArray(new Base[this.interpreter.size()]); // Reference
        case 231778726: /*baseLocation*/ return this.baseLocation == null ? new Base[0] : this.baseLocation.toArray(new Base[this.baseLocation.size()]); // StudyBaseLocationComponent
        case 1920000407: /*numberOfSeries*/ return this.numberOfSeries == null ? new Base[0] : new Base[] {this.numberOfSeries}; // UnsignedIntType
        case -1043544226: /*numberOfInstances*/ return this.numberOfInstances == null ? new Base[0] : new Base[] {this.numberOfInstances}; // UnsignedIntType
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : this.procedure.toArray(new Base[this.procedure.size()]); // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -905838985: /*series*/ return this.series == null ? new Base[0] : this.series.toArray(new Base[this.series.size()]); // ImagingStudySeriesComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          break;
        case -2115028956: // accession
          this.accession = castToIdentifier(value); // Identifier
          break;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 1997542747: // availability
          this.availability = new InstanceAvailabilityEnumFactory().fromType(value); // Enumeration<InstanceAvailability>
          break;
        case -1030238433: // modalityList
          this.getModalityList().add(castToCoding(value)); // Coding
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          break;
        case -1897185151: // started
          this.started = castToDateTime(value); // DateTimeType
          break;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          break;
        case -722568161: // referrer
          this.referrer = castToReference(value); // Reference
          break;
        case -2008009094: // interpreter
          this.getInterpreter().add(castToReference(value)); // Reference
          break;
        case 231778726: // baseLocation
          this.getBaseLocation().add((StudyBaseLocationComponent) value); // StudyBaseLocationComponent
          break;
        case 1920000407: // numberOfSeries
          this.numberOfSeries = castToUnsignedInt(value); // UnsignedIntType
          break;
        case -1043544226: // numberOfInstances
          this.numberOfInstances = castToUnsignedInt(value); // UnsignedIntType
          break;
        case -1095204141: // procedure
          this.getProcedure().add(castToReference(value)); // Reference
          break;
        case -934964668: // reason
          this.reason = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -905838985: // series
          this.getSeries().add((ImagingStudySeriesComponent) value); // ImagingStudySeriesComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("uid"))
          this.uid = castToOid(value); // OidType
        else if (name.equals("accession"))
          this.accession = castToIdentifier(value); // Identifier
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("availability"))
          this.availability = new InstanceAvailabilityEnumFactory().fromType(value); // Enumeration<InstanceAvailability>
        else if (name.equals("modalityList"))
          this.getModalityList().add(castToCoding(value));
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("context"))
          this.context = castToReference(value); // Reference
        else if (name.equals("started"))
          this.started = castToDateTime(value); // DateTimeType
        else if (name.equals("basedOn"))
          this.getBasedOn().add(castToReference(value));
        else if (name.equals("referrer"))
          this.referrer = castToReference(value); // Reference
        else if (name.equals("interpreter"))
          this.getInterpreter().add(castToReference(value));
        else if (name.equals("baseLocation"))
          this.getBaseLocation().add((StudyBaseLocationComponent) value);
        else if (name.equals("numberOfSeries"))
          this.numberOfSeries = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("numberOfInstances"))
          this.numberOfInstances = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("procedure"))
          this.getProcedure().add(castToReference(value));
        else if (name.equals("reason"))
          this.reason = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("series"))
          this.getSeries().add((ImagingStudySeriesComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case -2115028956:  return getAccession(); // Identifier
        case -1618432855:  return addIdentifier(); // Identifier
        case 1997542747: throw new FHIRException("Cannot make property availability as it is not a complex type"); // Enumeration<InstanceAvailability>
        case -1030238433:  return addModalityList(); // Coding
        case -791418107:  return getPatient(); // Reference
        case 951530927:  return getContext(); // Reference
        case -1897185151: throw new FHIRException("Cannot make property started as it is not a complex type"); // DateTimeType
        case -332612366:  return addBasedOn(); // Reference
        case -722568161:  return getReferrer(); // Reference
        case -2008009094:  return addInterpreter(); // Reference
        case 231778726:  return addBaseLocation(); // StudyBaseLocationComponent
        case 1920000407: throw new FHIRException("Cannot make property numberOfSeries as it is not a complex type"); // UnsignedIntType
        case -1043544226: throw new FHIRException("Cannot make property numberOfInstances as it is not a complex type"); // UnsignedIntType
        case -1095204141:  return addProcedure(); // Reference
        case -934964668:  return getReason(); // CodeableConcept
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -905838985:  return addSeries(); // ImagingStudySeriesComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.uid");
        }
        else if (name.equals("accession")) {
          this.accession = new Identifier();
          return this.accession;
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("availability")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.availability");
        }
        else if (name.equals("modalityList")) {
          return addModalityList();
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("started")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.started");
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("referrer")) {
          this.referrer = new Reference();
          return this.referrer;
        }
        else if (name.equals("interpreter")) {
          return addInterpreter();
        }
        else if (name.equals("baseLocation")) {
          return addBaseLocation();
        }
        else if (name.equals("numberOfSeries")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.numberOfSeries");
        }
        else if (name.equals("numberOfInstances")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.numberOfInstances");
        }
        else if (name.equals("procedure")) {
          return addProcedure();
        }
        else if (name.equals("reason")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingStudy.description");
        }
        else if (name.equals("series")) {
          return addSeries();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImagingStudy";

  }

      public ImagingStudy copy() {
        ImagingStudy dst = new ImagingStudy();
        copyValues(dst);
        dst.uid = uid == null ? null : uid.copy();
        dst.accession = accession == null ? null : accession.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.availability = availability == null ? null : availability.copy();
        if (modalityList != null) {
          dst.modalityList = new ArrayList<Coding>();
          for (Coding i : modalityList)
            dst.modalityList.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.context = context == null ? null : context.copy();
        dst.started = started == null ? null : started.copy();
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        dst.referrer = referrer == null ? null : referrer.copy();
        if (interpreter != null) {
          dst.interpreter = new ArrayList<Reference>();
          for (Reference i : interpreter)
            dst.interpreter.add(i.copy());
        };
        if (baseLocation != null) {
          dst.baseLocation = new ArrayList<StudyBaseLocationComponent>();
          for (StudyBaseLocationComponent i : baseLocation)
            dst.baseLocation.add(i.copy());
        };
        dst.numberOfSeries = numberOfSeries == null ? null : numberOfSeries.copy();
        dst.numberOfInstances = numberOfInstances == null ? null : numberOfInstances.copy();
        if (procedure != null) {
          dst.procedure = new ArrayList<Reference>();
          for (Reference i : procedure)
            dst.procedure.add(i.copy());
        };
        dst.reason = reason == null ? null : reason.copy();
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
        return compareDeep(uid, o.uid, true) && compareDeep(accession, o.accession, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(availability, o.availability, true) && compareDeep(modalityList, o.modalityList, true)
           && compareDeep(patient, o.patient, true) && compareDeep(context, o.context, true) && compareDeep(started, o.started, true)
           && compareDeep(basedOn, o.basedOn, true) && compareDeep(referrer, o.referrer, true) && compareDeep(interpreter, o.interpreter, true)
           && compareDeep(baseLocation, o.baseLocation, true) && compareDeep(numberOfSeries, o.numberOfSeries, true)
           && compareDeep(numberOfInstances, o.numberOfInstances, true) && compareDeep(procedure, o.procedure, true)
           && compareDeep(reason, o.reason, true) && compareDeep(description, o.description, true) && compareDeep(series, o.series, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingStudy))
          return false;
        ImagingStudy o = (ImagingStudy) other;
        return compareValues(uid, o.uid, true) && compareValues(availability, o.availability, true) && compareValues(started, o.started, true)
           && compareValues(numberOfSeries, o.numberOfSeries, true) && compareValues(numberOfInstances, o.numberOfInstances, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(uid, accession, identifier
          , availability, modalityList, patient, context, started, basedOn, referrer, interpreter
          , baseLocation, numberOfSeries, numberOfInstances, procedure, reason, description
          , series);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImagingStudy;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Other identifiers for the Study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ImagingStudy.identifier", description="Other identifiers for the Study", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Other identifiers for the Study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>reason</b>
   * <p>
   * Description: <b>The reason for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.reason</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reason", path="ImagingStudy.reason", description="The reason for the study", type="token" )
  public static final String SP_REASON = "reason";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reason</b>
   * <p>
   * Description: <b>The reason for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.reason</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REASON = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REASON);

 /**
   * Search parameter: <b>study</b>
   * <p>
   * Description: <b>The study identifier for the image</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingStudy.uid</b><br>
   * </p>
   */
  @SearchParamDefinition(name="study", path="ImagingStudy.uid", description="The study identifier for the image", type="uri" )
  public static final String SP_STUDY = "study";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>study</b>
   * <p>
   * Description: <b>The study identifier for the image</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingStudy.uid</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam STUDY = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_STUDY);

 /**
   * Search parameter: <b>dicom-class</b>
   * <p>
   * Description: <b>The type of the instance</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingStudy.series.instance.sopClass</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dicom-class", path="ImagingStudy.series.instance.sopClass", description="The type of the instance", type="uri" )
  public static final String SP_DICOM_CLASS = "dicom-class";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dicom-class</b>
   * <p>
   * Description: <b>The type of the instance</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingStudy.series.instance.sopClass</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam DICOM_CLASS = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_DICOM_CLASS);

 /**
   * Search parameter: <b>modality</b>
   * <p>
   * Description: <b>The modality of the series</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.series.modality</b><br>
   * </p>
   */
  @SearchParamDefinition(name="modality", path="ImagingStudy.series.modality", description="The modality of the series", type="token" )
  public static final String SP_MODALITY = "modality";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>modality</b>
   * <p>
   * Description: <b>The modality of the series</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.series.modality</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam MODALITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_MODALITY);

 /**
   * Search parameter: <b>bodysite</b>
   * <p>
   * Description: <b>The body site studied</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.series.bodySite</b><br>
   * </p>
   */
  @SearchParamDefinition(name="bodysite", path="ImagingStudy.series.bodySite", description="The body site studied", type="token" )
  public static final String SP_BODYSITE = "bodysite";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>bodysite</b>
   * <p>
   * Description: <b>The body site studied</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.series.bodySite</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam BODYSITE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_BODYSITE);

 /**
   * Search parameter: <b>started</b>
   * <p>
   * Description: <b>When the study was started</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImagingStudy.started</b><br>
   * </p>
   */
  @SearchParamDefinition(name="started", path="ImagingStudy.started", description="When the study was started", type="date" )
  public static final String SP_STARTED = "started";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>started</b>
   * <p>
   * Description: <b>When the study was started</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImagingStudy.started</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam STARTED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_STARTED);

 /**
   * Search parameter: <b>accession</b>
   * <p>
   * Description: <b>The accession identifier for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.accession</b><br>
   * </p>
   */
  @SearchParamDefinition(name="accession", path="ImagingStudy.accession", description="The accession identifier for the study", type="token" )
  public static final String SP_ACCESSION = "accession";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>accession</b>
   * <p>
   * Description: <b>The accession identifier for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingStudy.accession</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACCESSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACCESSION);

 /**
   * Search parameter: <b>uid</b>
   * <p>
   * Description: <b>The instance unique identifier</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingStudy.series.instance.uid</b><br>
   * </p>
   */
  @SearchParamDefinition(name="uid", path="ImagingStudy.series.instance.uid", description="The instance unique identifier", type="uri" )
  public static final String SP_UID = "uid";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>uid</b>
   * <p>
   * Description: <b>The instance unique identifier</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingStudy.series.instance.uid</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam UID = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_UID);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who the study is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingStudy.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ImagingStudy.patient", description="Who the study is about", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who the study is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingStudy.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingStudy:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ImagingStudy:patient").toLocked();

 /**
   * Search parameter: <b>series</b>
   * <p>
   * Description: <b>The identifier of the series of images</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingStudy.series.uid</b><br>
   * </p>
   */
  @SearchParamDefinition(name="series", path="ImagingStudy.series.uid", description="The identifier of the series of images", type="uri" )
  public static final String SP_SERIES = "series";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>series</b>
   * <p>
   * Description: <b>The identifier of the series of images</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingStudy.series.uid</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam SERIES = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_SERIES);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>The context of the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingStudy.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="ImagingStudy.context", description="The context of the study", type="reference", target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>The context of the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingStudy.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingStudy:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("ImagingStudy:context").toLocked();

 /**
   * Search parameter: <b>basedon</b>
   * <p>
   * Description: <b>The order for the image</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingStudy.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="basedon", path="ImagingStudy.basedOn", description="The order for the image", type="reference", target={CarePlan.class, DiagnosticRequest.class, ProcedureRequest.class, ReferralRequest.class } )
  public static final String SP_BASEDON = "basedon";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>basedon</b>
   * <p>
   * Description: <b>The order for the image</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingStudy.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASEDON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASEDON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingStudy:basedon</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASEDON = new ca.uhn.fhir.model.api.Include("ImagingStudy:basedon").toLocked();


}

