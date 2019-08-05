package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A manifest of a set of DICOM Service-Object Pair Instances (SOP Instances).  The referenced SOP Instances (images or other content) are for a single patient, and may be from one or more studies. The referenced SOP Instances have been selected for a purpose, such as quality assurance, conference, or consult. Reflecting that range of purposes, typical ImagingObjectSelection resources may include all SOP Instances in a study (perhaps for sharing through a Health Information Exchange); key images from multiple studies (for reference by a referring or treating physician); a multi-frame ultrasound instance ("cine" video clip) and a set of measurements taken from that instance (for inclusion in a teaching file); and so on.
 */
@ResourceDef(name="ImagingObjectSelection", profile="http://hl7.org/fhir/Profile/ImagingObjectSelection")
public class ImagingObjectSelection extends DomainResource {

    @Block()
    public static class StudyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Study instance UID of the SOP instances in the selection.
         */
        @Child(name = "uid", type = {OidType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Study instance UID", formalDefinition="Study instance UID of the SOP instances in the selection." )
        protected OidType uid;

        /**
         * WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Retrieve study URL", formalDefinition="WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection." )
        protected UriType url;

        /**
         * Reference to the Imaging Study in FHIR form.
         */
        @Child(name = "imagingStudy", type = {ImagingStudy.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to ImagingStudy", formalDefinition="Reference to the Imaging Study in FHIR form." )
        protected Reference imagingStudy;

        /**
         * The actual object that is the target of the reference (Reference to the Imaging Study in FHIR form.)
         */
        protected ImagingStudy imagingStudyTarget;

        /**
         * Series identity and locating information of the DICOM SOP instances in the selection.
         */
        @Child(name = "series", type = {}, order=4, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Series identity of the selected instances", formalDefinition="Series identity and locating information of the DICOM SOP instances in the selection." )
        protected List<SeriesComponent> series;

        private static final long serialVersionUID = 341246743L;

    /**
     * Constructor
     */
      public StudyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StudyComponent(OidType uid) {
        super();
        this.uid = uid;
      }

        /**
         * @return {@link #uid} (Study instance UID of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyComponent.uid");
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
         * @param value {@link #uid} (Study instance UID of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public StudyComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return Study instance UID of the SOP instances in the selection.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value Study instance UID of the SOP instances in the selection.
         */
        public StudyComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyComponent.url");
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
         * @param value {@link #url} (WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public StudyComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.
         */
        public StudyComponent setUrl(String value) { 
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
         * @return {@link #imagingStudy} (Reference to the Imaging Study in FHIR form.)
         */
        public Reference getImagingStudy() { 
          if (this.imagingStudy == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyComponent.imagingStudy");
            else if (Configuration.doAutoCreate())
              this.imagingStudy = new Reference(); // cc
          return this.imagingStudy;
        }

        public boolean hasImagingStudy() { 
          return this.imagingStudy != null && !this.imagingStudy.isEmpty();
        }

        /**
         * @param value {@link #imagingStudy} (Reference to the Imaging Study in FHIR form.)
         */
        public StudyComponent setImagingStudy(Reference value) { 
          this.imagingStudy = value;
          return this;
        }

        /**
         * @return {@link #imagingStudy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the Imaging Study in FHIR form.)
         */
        public ImagingStudy getImagingStudyTarget() { 
          if (this.imagingStudyTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyComponent.imagingStudy");
            else if (Configuration.doAutoCreate())
              this.imagingStudyTarget = new ImagingStudy(); // aa
          return this.imagingStudyTarget;
        }

        /**
         * @param value {@link #imagingStudy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the Imaging Study in FHIR form.)
         */
        public StudyComponent setImagingStudyTarget(ImagingStudy value) { 
          this.imagingStudyTarget = value;
          return this;
        }

        /**
         * @return {@link #series} (Series identity and locating information of the DICOM SOP instances in the selection.)
         */
        public List<SeriesComponent> getSeries() { 
          if (this.series == null)
            this.series = new ArrayList<SeriesComponent>();
          return this.series;
        }

        public boolean hasSeries() { 
          if (this.series == null)
            return false;
          for (SeriesComponent item : this.series)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #series} (Series identity and locating information of the DICOM SOP instances in the selection.)
         */
    // syntactic sugar
        public SeriesComponent addSeries() { //3
          SeriesComponent t = new SeriesComponent();
          if (this.series == null)
            this.series = new ArrayList<SeriesComponent>();
          this.series.add(t);
          return t;
        }

    // syntactic sugar
        public StudyComponent addSeries(SeriesComponent t) { //3
          if (t == null)
            return this;
          if (this.series == null)
            this.series = new ArrayList<SeriesComponent>();
          this.series.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uid", "oid", "Study instance UID of the SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("url", "uri", "WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("imagingStudy", "Reference(ImagingStudy)", "Reference to the Imaging Study in FHIR form.", 0, java.lang.Integer.MAX_VALUE, imagingStudy));
          childrenList.add(new Property("series", "", "Series identity and locating information of the DICOM SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, series));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -814900911: /*imagingStudy*/ return this.imagingStudy == null ? new Base[0] : new Base[] {this.imagingStudy}; // Reference
        case -905838985: /*series*/ return this.series == null ? new Base[0] : this.series.toArray(new Base[this.series.size()]); // SeriesComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case -814900911: // imagingStudy
          this.imagingStudy = castToReference(value); // Reference
          break;
        case -905838985: // series
          this.getSeries().add((SeriesComponent) value); // SeriesComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("uid"))
          this.uid = castToOid(value); // OidType
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("imagingStudy"))
          this.imagingStudy = castToReference(value); // Reference
        else if (name.equals("series"))
          this.getSeries().add((SeriesComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case -814900911:  return getImagingStudy(); // Reference
        case -905838985:  return addSeries(); // SeriesComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.uid");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.url");
        }
        else if (name.equals("imagingStudy")) {
          this.imagingStudy = new Reference();
          return this.imagingStudy;
        }
        else if (name.equals("series")) {
          return addSeries();
        }
        else
          return super.addChild(name);
      }

      public StudyComponent copy() {
        StudyComponent dst = new StudyComponent();
        copyValues(dst);
        dst.uid = uid == null ? null : uid.copy();
        dst.url = url == null ? null : url.copy();
        dst.imagingStudy = imagingStudy == null ? null : imagingStudy.copy();
        if (series != null) {
          dst.series = new ArrayList<SeriesComponent>();
          for (SeriesComponent i : series)
            dst.series.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StudyComponent))
          return false;
        StudyComponent o = (StudyComponent) other;
        return compareDeep(uid, o.uid, true) && compareDeep(url, o.url, true) && compareDeep(imagingStudy, o.imagingStudy, true)
           && compareDeep(series, o.series, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StudyComponent))
          return false;
        StudyComponent o = (StudyComponent) other;
        return compareValues(uid, o.uid, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (uid == null || uid.isEmpty()) && (url == null || url.isEmpty()) && (imagingStudy == null || imagingStudy.isEmpty())
           && (series == null || series.isEmpty());
      }

  public String fhirType() {
    return "ImagingObjectSelection.study";

  }

  }

    @Block()
    public static class SeriesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Series instance UID of the SOP instances in the selection.
         */
        @Child(name = "uid", type = {OidType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Series instance UID", formalDefinition="Series instance UID of the SOP instances in the selection." )
        protected OidType uid;

        /**
         * WADO-RS URL to retrieve the series. Note that this URL retrieves all SOP instances of the series not only those in the selection.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Retrieve series URL", formalDefinition="WADO-RS URL to retrieve the series. Note that this URL retrieves all SOP instances of the series not only those in the selection." )
        protected UriType url;

        /**
         * Identity and locating information of the selected DICOM SOP instances.
         */
        @Child(name = "instance", type = {}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The selected instance", formalDefinition="Identity and locating information of the selected DICOM SOP instances." )
        protected List<InstanceComponent> instance;

        private static final long serialVersionUID = 229247770L;

    /**
     * Constructor
     */
      public SeriesComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SeriesComponent(OidType uid) {
        super();
        this.uid = uid;
      }

        /**
         * @return {@link #uid} (Series instance UID of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SeriesComponent.uid");
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
         * @param value {@link #uid} (Series instance UID of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public SeriesComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return Series instance UID of the SOP instances in the selection.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value Series instance UID of the SOP instances in the selection.
         */
        public SeriesComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (WADO-RS URL to retrieve the series. Note that this URL retrieves all SOP instances of the series not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SeriesComponent.url");
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
         * @param value {@link #url} (WADO-RS URL to retrieve the series. Note that this URL retrieves all SOP instances of the series not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public SeriesComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return WADO-RS URL to retrieve the series. Note that this URL retrieves all SOP instances of the series not only those in the selection.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value WADO-RS URL to retrieve the series. Note that this URL retrieves all SOP instances of the series not only those in the selection.
         */
        public SeriesComponent setUrl(String value) { 
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
         * @return {@link #instance} (Identity and locating information of the selected DICOM SOP instances.)
         */
        public List<InstanceComponent> getInstance() { 
          if (this.instance == null)
            this.instance = new ArrayList<InstanceComponent>();
          return this.instance;
        }

        public boolean hasInstance() { 
          if (this.instance == null)
            return false;
          for (InstanceComponent item : this.instance)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #instance} (Identity and locating information of the selected DICOM SOP instances.)
         */
    // syntactic sugar
        public InstanceComponent addInstance() { //3
          InstanceComponent t = new InstanceComponent();
          if (this.instance == null)
            this.instance = new ArrayList<InstanceComponent>();
          this.instance.add(t);
          return t;
        }

    // syntactic sugar
        public SeriesComponent addInstance(InstanceComponent t) { //3
          if (t == null)
            return this;
          if (this.instance == null)
            this.instance = new ArrayList<InstanceComponent>();
          this.instance.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uid", "oid", "Series instance UID of the SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("url", "uri", "WADO-RS URL to retrieve the series. Note that this URL retrieves all SOP instances of the series not only those in the selection.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("instance", "", "Identity and locating information of the selected DICOM SOP instances.", 0, java.lang.Integer.MAX_VALUE, instance));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 555127957: /*instance*/ return this.instance == null ? new Base[0] : this.instance.toArray(new Base[this.instance.size()]); // InstanceComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 555127957: // instance
          this.getInstance().add((InstanceComponent) value); // InstanceComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("uid"))
          this.uid = castToOid(value); // OidType
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("instance"))
          this.getInstance().add((InstanceComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 555127957:  return addInstance(); // InstanceComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.uid");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.url");
        }
        else if (name.equals("instance")) {
          return addInstance();
        }
        else
          return super.addChild(name);
      }

      public SeriesComponent copy() {
        SeriesComponent dst = new SeriesComponent();
        copyValues(dst);
        dst.uid = uid == null ? null : uid.copy();
        dst.url = url == null ? null : url.copy();
        if (instance != null) {
          dst.instance = new ArrayList<InstanceComponent>();
          for (InstanceComponent i : instance)
            dst.instance.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SeriesComponent))
          return false;
        SeriesComponent o = (SeriesComponent) other;
        return compareDeep(uid, o.uid, true) && compareDeep(url, o.url, true) && compareDeep(instance, o.instance, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SeriesComponent))
          return false;
        SeriesComponent o = (SeriesComponent) other;
        return compareValues(uid, o.uid, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (uid == null || uid.isEmpty()) && (url == null || url.isEmpty()) && (instance == null || instance.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImagingObjectSelection.study.series";

  }

  }

    @Block()
    public static class InstanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * SOP class UID of the selected instance.
         */
        @Child(name = "sopClass", type = {OidType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="SOP class UID of instance", formalDefinition="SOP class UID of the selected instance." )
        protected OidType sopClass;

        /**
         * SOP Instance UID of the selected instance.
         */
        @Child(name = "uid", type = {OidType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Selected instance UID", formalDefinition="SOP Instance UID of the selected instance." )
        protected OidType uid;

        /**
         * WADO-RS URL to retrieve the DICOM SOP Instance.
         */
        @Child(name = "url", type = {UriType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Retrieve instance URL", formalDefinition="WADO-RS URL to retrieve the DICOM SOP Instance." )
        protected UriType url;

        /**
         * Identity and location information of the frames in the selected instance.
         */
        @Child(name = "frame", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The frame set", formalDefinition="Identity and location information of the frames in the selected instance." )
        protected List<FramesComponent> frame;

        private static final long serialVersionUID = -1609681911L;

    /**
     * Constructor
     */
      public InstanceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InstanceComponent(OidType sopClass, OidType uid, UriType url) {
        super();
        this.sopClass = sopClass;
        this.uid = uid;
        this.url = url;
      }

        /**
         * @return {@link #sopClass} (SOP class UID of the selected instance.). This is the underlying object with id, value and extensions. The accessor "getSopClass" gives direct access to the value
         */
        public OidType getSopClassElement() { 
          if (this.sopClass == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InstanceComponent.sopClass");
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
         * @param value {@link #sopClass} (SOP class UID of the selected instance.). This is the underlying object with id, value and extensions. The accessor "getSopClass" gives direct access to the value
         */
        public InstanceComponent setSopClassElement(OidType value) { 
          this.sopClass = value;
          return this;
        }

        /**
         * @return SOP class UID of the selected instance.
         */
        public String getSopClass() { 
          return this.sopClass == null ? null : this.sopClass.getValue();
        }

        /**
         * @param value SOP class UID of the selected instance.
         */
        public InstanceComponent setSopClass(String value) { 
            if (this.sopClass == null)
              this.sopClass = new OidType();
            this.sopClass.setValue(value);
          return this;
        }

        /**
         * @return {@link #uid} (SOP Instance UID of the selected instance.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InstanceComponent.uid");
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
         * @param value {@link #uid} (SOP Instance UID of the selected instance.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public InstanceComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return SOP Instance UID of the selected instance.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value SOP Instance UID of the selected instance.
         */
        public InstanceComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (WADO-RS URL to retrieve the DICOM SOP Instance.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InstanceComponent.url");
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
         * @param value {@link #url} (WADO-RS URL to retrieve the DICOM SOP Instance.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public InstanceComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return WADO-RS URL to retrieve the DICOM SOP Instance.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value WADO-RS URL to retrieve the DICOM SOP Instance.
         */
        public InstanceComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        /**
         * @return {@link #frame} (Identity and location information of the frames in the selected instance.)
         */
        public List<FramesComponent> getFrame() { 
          if (this.frame == null)
            this.frame = new ArrayList<FramesComponent>();
          return this.frame;
        }

        public boolean hasFrame() { 
          if (this.frame == null)
            return false;
          for (FramesComponent item : this.frame)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #frame} (Identity and location information of the frames in the selected instance.)
         */
    // syntactic sugar
        public FramesComponent addFrame() { //3
          FramesComponent t = new FramesComponent();
          if (this.frame == null)
            this.frame = new ArrayList<FramesComponent>();
          this.frame.add(t);
          return t;
        }

    // syntactic sugar
        public InstanceComponent addFrame(FramesComponent t) { //3
          if (t == null)
            return this;
          if (this.frame == null)
            this.frame = new ArrayList<FramesComponent>();
          this.frame.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sopClass", "oid", "SOP class UID of the selected instance.", 0, java.lang.Integer.MAX_VALUE, sopClass));
          childrenList.add(new Property("uid", "oid", "SOP Instance UID of the selected instance.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("url", "uri", "WADO-RS URL to retrieve the DICOM SOP Instance.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("frame", "", "Identity and location information of the frames in the selected instance.", 0, java.lang.Integer.MAX_VALUE, frame));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1560041540: /*sopClass*/ return this.sopClass == null ? new Base[0] : new Base[] {this.sopClass}; // OidType
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 97692013: /*frame*/ return this.frame == null ? new Base[0] : this.frame.toArray(new Base[this.frame.size()]); // FramesComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1560041540: // sopClass
          this.sopClass = castToOid(value); // OidType
          break;
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 97692013: // frame
          this.getFrame().add((FramesComponent) value); // FramesComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sopClass"))
          this.sopClass = castToOid(value); // OidType
        else if (name.equals("uid"))
          this.uid = castToOid(value); // OidType
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("frame"))
          this.getFrame().add((FramesComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1560041540: throw new FHIRException("Cannot make property sopClass as it is not a complex type"); // OidType
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 97692013:  return addFrame(); // FramesComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sopClass")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.sopClass");
        }
        else if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.uid");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.url");
        }
        else if (name.equals("frame")) {
          return addFrame();
        }
        else
          return super.addChild(name);
      }

      public InstanceComponent copy() {
        InstanceComponent dst = new InstanceComponent();
        copyValues(dst);
        dst.sopClass = sopClass == null ? null : sopClass.copy();
        dst.uid = uid == null ? null : uid.copy();
        dst.url = url == null ? null : url.copy();
        if (frame != null) {
          dst.frame = new ArrayList<FramesComponent>();
          for (FramesComponent i : frame)
            dst.frame.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof InstanceComponent))
          return false;
        InstanceComponent o = (InstanceComponent) other;
        return compareDeep(sopClass, o.sopClass, true) && compareDeep(uid, o.uid, true) && compareDeep(url, o.url, true)
           && compareDeep(frame, o.frame, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof InstanceComponent))
          return false;
        InstanceComponent o = (InstanceComponent) other;
        return compareValues(sopClass, o.sopClass, true) && compareValues(uid, o.uid, true) && compareValues(url, o.url, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sopClass == null || sopClass.isEmpty()) && (uid == null || uid.isEmpty())
           && (url == null || url.isEmpty()) && (frame == null || frame.isEmpty());
      }

  public String fhirType() {
    return "ImagingObjectSelection.study.series.instance";

  }

  }

    @Block()
    public static class FramesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The specific frame reference within a multi-frame object.
         */
        @Child(name = "number", type = {UnsignedIntType.class}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Frame reference number", formalDefinition="The specific frame reference within a multi-frame object." )
        protected List<UnsignedIntType> number;

        /**
         * WADO-RS URL to retrieve the DICOM frames.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Retrieve frame URL", formalDefinition="WADO-RS URL to retrieve the DICOM frames." )
        protected UriType url;

        private static final long serialVersionUID = 236505178L;

    /**
     * Constructor
     */
      public FramesComponent() {
        super();
      }

    /**
     * Constructor
     */
      public FramesComponent(UriType url) {
        super();
        this.url = url;
      }

        /**
         * @return {@link #number} (The specific frame reference within a multi-frame object.)
         */
        public List<UnsignedIntType> getNumber() { 
          if (this.number == null)
            this.number = new ArrayList<UnsignedIntType>();
          return this.number;
        }

        public boolean hasNumber() { 
          if (this.number == null)
            return false;
          for (UnsignedIntType item : this.number)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #number} (The specific frame reference within a multi-frame object.)
         */
    // syntactic sugar
        public UnsignedIntType addNumberElement() {//2 
          UnsignedIntType t = new UnsignedIntType();
          if (this.number == null)
            this.number = new ArrayList<UnsignedIntType>();
          this.number.add(t);
          return t;
        }

        /**
         * @param value {@link #number} (The specific frame reference within a multi-frame object.)
         */
        public FramesComponent addNumber(int value) { //1
          UnsignedIntType t = new UnsignedIntType();
          t.setValue(value);
          if (this.number == null)
            this.number = new ArrayList<UnsignedIntType>();
          this.number.add(t);
          return this;
        }

        /**
         * @param value {@link #number} (The specific frame reference within a multi-frame object.)
         */
        public boolean hasNumber(int value) { 
          if (this.number == null)
            return false;
          for (UnsignedIntType v : this.number)
            if (v.equals(value)) // unsignedInt
              return true;
          return false;
        }

        /**
         * @return {@link #url} (WADO-RS URL to retrieve the DICOM frames.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create FramesComponent.url");
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
         * @param value {@link #url} (WADO-RS URL to retrieve the DICOM frames.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public FramesComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return WADO-RS URL to retrieve the DICOM frames.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value WADO-RS URL to retrieve the DICOM frames.
         */
        public FramesComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("number", "unsignedInt", "The specific frame reference within a multi-frame object.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("url", "uri", "WADO-RS URL to retrieve the DICOM frames.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : this.number.toArray(new Base[this.number.size()]); // UnsignedIntType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1034364087: // number
          this.getNumber().add(castToUnsignedInt(value)); // UnsignedIntType
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("number"))
          this.getNumber().add(castToUnsignedInt(value));
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087: throw new FHIRException("Cannot make property number as it is not a complex type"); // UnsignedIntType
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.number");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.url");
        }
        else
          return super.addChild(name);
      }

      public FramesComponent copy() {
        FramesComponent dst = new FramesComponent();
        copyValues(dst);
        if (number != null) {
          dst.number = new ArrayList<UnsignedIntType>();
          for (UnsignedIntType i : number)
            dst.number.add(i.copy());
        };
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof FramesComponent))
          return false;
        FramesComponent o = (FramesComponent) other;
        return compareDeep(number, o.number, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof FramesComponent))
          return false;
        FramesComponent o = (FramesComponent) other;
        return compareValues(number, o.number, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (number == null || number.isEmpty()) && (url == null || url.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImagingObjectSelection.study.series.instance.frame";

  }

  }

    /**
     * Instance UID of the DICOM KOS SOP Instances represented in this resource.
     */
    @Child(name = "uid", type = {OidType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Instance UID", formalDefinition="Instance UID of the DICOM KOS SOP Instances represented in this resource." )
    protected OidType uid;

    /**
     * A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingObjectSelection.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient of the selected objects", formalDefinition="A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingObjectSelection." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingObjectSelection.)
     */
    protected Patient patientTarget;

    /**
     * Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).
     */
    @Child(name = "authoringTime", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time when the imaging object selection was created", formalDefinition="Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image)." )
    protected DateTimeType authoringTime;

    /**
     * Author of ImagingObjectSelection. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.
     */
    @Child(name = "author", type = {Practitioner.class, Device.class, Organization.class, Patient.class, RelatedPerson.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Author (human or machine)", formalDefinition="Author of ImagingObjectSelection. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Author of ImagingObjectSelection. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    protected Resource authorTarget;

    /**
     * The reason for, or significance of, the selection of objects referenced in the resource.
     */
    @Child(name = "title", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for selection", formalDefinition="The reason for, or significance of, the selection of objects referenced in the resource." )
    protected CodeableConcept title;

    /**
     * Text description of the DICOM SOP instances selected in the ImagingObjectSelection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.
     */
    @Child(name = "description", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Description text", formalDefinition="Text description of the DICOM SOP instances selected in the ImagingObjectSelection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection." )
    protected StringType description;

    /**
     * Study identity and locating information of the DICOM SOP instances in the selection.
     */
    @Child(name = "study", type = {}, order=6, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Study identity of the selected instances", formalDefinition="Study identity and locating information of the DICOM SOP instances in the selection." )
    protected List<StudyComponent> study;

    private static final long serialVersionUID = 1428713335L;

  /**
   * Constructor
   */
    public ImagingObjectSelection() {
      super();
    }

  /**
   * Constructor
   */
    public ImagingObjectSelection(OidType uid, Reference patient, CodeableConcept title) {
      super();
      this.uid = uid;
      this.patient = patient;
      this.title = title;
    }

    /**
     * @return {@link #uid} (Instance UID of the DICOM KOS SOP Instances represented in this resource.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public OidType getUidElement() { 
      if (this.uid == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.uid");
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
     * @param value {@link #uid} (Instance UID of the DICOM KOS SOP Instances represented in this resource.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public ImagingObjectSelection setUidElement(OidType value) { 
      this.uid = value;
      return this;
    }

    /**
     * @return Instance UID of the DICOM KOS SOP Instances represented in this resource.
     */
    public String getUid() { 
      return this.uid == null ? null : this.uid.getValue();
    }

    /**
     * @param value Instance UID of the DICOM KOS SOP Instances represented in this resource.
     */
    public ImagingObjectSelection setUid(String value) { 
        if (this.uid == null)
          this.uid = new OidType();
        this.uid.setValue(value);
      return this;
    }

    /**
     * @return {@link #patient} (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingObjectSelection.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingObjectSelection.)
     */
    public ImagingObjectSelection setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingObjectSelection.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingObjectSelection.)
     */
    public ImagingObjectSelection setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #authoringTime} (Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).). This is the underlying object with id, value and extensions. The accessor "getAuthoringTime" gives direct access to the value
     */
    public DateTimeType getAuthoringTimeElement() { 
      if (this.authoringTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.authoringTime");
        else if (Configuration.doAutoCreate())
          this.authoringTime = new DateTimeType(); // bb
      return this.authoringTime;
    }

    public boolean hasAuthoringTimeElement() { 
      return this.authoringTime != null && !this.authoringTime.isEmpty();
    }

    public boolean hasAuthoringTime() { 
      return this.authoringTime != null && !this.authoringTime.isEmpty();
    }

    /**
     * @param value {@link #authoringTime} (Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).). This is the underlying object with id, value and extensions. The accessor "getAuthoringTime" gives direct access to the value
     */
    public ImagingObjectSelection setAuthoringTimeElement(DateTimeType value) { 
      this.authoringTime = value;
      return this;
    }

    /**
     * @return Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).
     */
    public Date getAuthoringTime() { 
      return this.authoringTime == null ? null : this.authoringTime.getValue();
    }

    /**
     * @param value Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).
     */
    public ImagingObjectSelection setAuthoringTime(Date value) { 
      if (value == null)
        this.authoringTime = null;
      else {
        if (this.authoringTime == null)
          this.authoringTime = new DateTimeType();
        this.authoringTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #author} (Author of ImagingObjectSelection. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Author of ImagingObjectSelection. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public ImagingObjectSelection setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Author of ImagingObjectSelection. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Author of ImagingObjectSelection. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public ImagingObjectSelection setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #title} (The reason for, or significance of, the selection of objects referenced in the resource.)
     */
    public CodeableConcept getTitle() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.title");
        else if (Configuration.doAutoCreate())
          this.title = new CodeableConcept(); // cc
      return this.title;
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (The reason for, or significance of, the selection of objects referenced in the resource.)
     */
    public ImagingObjectSelection setTitle(CodeableConcept value) { 
      this.title = value;
      return this;
    }

    /**
     * @return {@link #description} (Text description of the DICOM SOP instances selected in the ImagingObjectSelection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.description");
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
     * @param value {@link #description} (Text description of the DICOM SOP instances selected in the ImagingObjectSelection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImagingObjectSelection setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Text description of the DICOM SOP instances selected in the ImagingObjectSelection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Text description of the DICOM SOP instances selected in the ImagingObjectSelection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.
     */
    public ImagingObjectSelection setDescription(String value) { 
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
     * @return {@link #study} (Study identity and locating information of the DICOM SOP instances in the selection.)
     */
    public List<StudyComponent> getStudy() { 
      if (this.study == null)
        this.study = new ArrayList<StudyComponent>();
      return this.study;
    }

    public boolean hasStudy() { 
      if (this.study == null)
        return false;
      for (StudyComponent item : this.study)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #study} (Study identity and locating information of the DICOM SOP instances in the selection.)
     */
    // syntactic sugar
    public StudyComponent addStudy() { //3
      StudyComponent t = new StudyComponent();
      if (this.study == null)
        this.study = new ArrayList<StudyComponent>();
      this.study.add(t);
      return t;
    }

    // syntactic sugar
    public ImagingObjectSelection addStudy(StudyComponent t) { //3
      if (t == null)
        return this;
      if (this.study == null)
        this.study = new ArrayList<StudyComponent>();
      this.study.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("uid", "oid", "Instance UID of the DICOM KOS SOP Instances represented in this resource.", 0, java.lang.Integer.MAX_VALUE, uid));
        childrenList.add(new Property("patient", "Reference(Patient)", "A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingObjectSelection.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("authoringTime", "dateTime", "Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).", 0, java.lang.Integer.MAX_VALUE, authoringTime));
        childrenList.add(new Property("author", "Reference(Practitioner|Device|Organization|Patient|RelatedPerson)", "Author of ImagingObjectSelection. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("title", "CodeableConcept", "The reason for, or significance of, the selection of objects referenced in the resource.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("description", "string", "Text description of the DICOM SOP instances selected in the ImagingObjectSelection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("study", "", "Study identity and locating information of the DICOM SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, study));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -1724532252: /*authoringTime*/ return this.authoringTime == null ? new Base[0] : new Base[] {this.authoringTime}; // DateTimeType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 109776329: /*study*/ return this.study == null ? new Base[0] : this.study.toArray(new Base[this.study.size()]); // StudyComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -1724532252: // authoringTime
          this.authoringTime = castToDateTime(value); // DateTimeType
          break;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          break;
        case 110371416: // title
          this.title = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 109776329: // study
          this.getStudy().add((StudyComponent) value); // StudyComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("uid"))
          this.uid = castToOid(value); // OidType
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("authoringTime"))
          this.authoringTime = castToDateTime(value); // DateTimeType
        else if (name.equals("author"))
          this.author = castToReference(value); // Reference
        else if (name.equals("title"))
          this.title = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("study"))
          this.getStudy().add((StudyComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case -791418107:  return getPatient(); // Reference
        case -1724532252: throw new FHIRException("Cannot make property authoringTime as it is not a complex type"); // DateTimeType
        case -1406328437:  return getAuthor(); // Reference
        case 110371416:  return getTitle(); // CodeableConcept
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 109776329:  return addStudy(); // StudyComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.uid");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("authoringTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.authoringTime");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("title")) {
          this.title = new CodeableConcept();
          return this.title;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingObjectSelection.description");
        }
        else if (name.equals("study")) {
          return addStudy();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImagingObjectSelection";

  }

      public ImagingObjectSelection copy() {
        ImagingObjectSelection dst = new ImagingObjectSelection();
        copyValues(dst);
        dst.uid = uid == null ? null : uid.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.authoringTime = authoringTime == null ? null : authoringTime.copy();
        dst.author = author == null ? null : author.copy();
        dst.title = title == null ? null : title.copy();
        dst.description = description == null ? null : description.copy();
        if (study != null) {
          dst.study = new ArrayList<StudyComponent>();
          for (StudyComponent i : study)
            dst.study.add(i.copy());
        };
        return dst;
      }

      protected ImagingObjectSelection typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingObjectSelection))
          return false;
        ImagingObjectSelection o = (ImagingObjectSelection) other;
        return compareDeep(uid, o.uid, true) && compareDeep(patient, o.patient, true) && compareDeep(authoringTime, o.authoringTime, true)
           && compareDeep(author, o.author, true) && compareDeep(title, o.title, true) && compareDeep(description, o.description, true)
           && compareDeep(study, o.study, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingObjectSelection))
          return false;
        ImagingObjectSelection o = (ImagingObjectSelection) other;
        return compareValues(uid, o.uid, true) && compareValues(authoringTime, o.authoringTime, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (uid == null || uid.isEmpty()) && (patient == null || patient.isEmpty())
           && (authoringTime == null || authoringTime.isEmpty()) && (author == null || author.isEmpty())
           && (title == null || title.isEmpty()) && (description == null || description.isEmpty()) && (study == null || study.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImagingObjectSelection;
   }

 /**
   * Search parameter: <b>selected-study</b>
   * <p>
   * Description: <b>Study selected in key DICOM object selection</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingObjectSelection.study.uid</b><br>
   * </p>
   */
  @SearchParamDefinition(name="selected-study", path="ImagingObjectSelection.study.uid", description="Study selected in key DICOM object selection", type="uri" )
  public static final String SP_SELECTED_STUDY = "selected-study";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>selected-study</b>
   * <p>
   * Description: <b>Study selected in key DICOM object selection</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingObjectSelection.study.uid</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam SELECTED_STUDY = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_SELECTED_STUDY);

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>Author of key DICOM object selection</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingObjectSelection.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="ImagingObjectSelection.author", description="Author of key DICOM object selection", type="reference" )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>Author of key DICOM object selection</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingObjectSelection.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingObjectSelection:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("ImagingObjectSelection:author").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Title of key DICOM object selection</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingObjectSelection.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ImagingObjectSelection.title", description="Title of key DICOM object selection", type="token" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Title of key DICOM object selection</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingObjectSelection.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TITLE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TITLE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Subject of key DICOM object selection</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingObjectSelection.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ImagingObjectSelection.patient", description="Subject of key DICOM object selection", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Subject of key DICOM object selection</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingObjectSelection.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingObjectSelection:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ImagingObjectSelection:patient").toLocked();

 /**
   * Search parameter: <b>authoring-time</b>
   * <p>
   * Description: <b>Time of key DICOM object selection authoring</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImagingObjectSelection.authoringTime</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authoring-time", path="ImagingObjectSelection.authoringTime", description="Time of key DICOM object selection authoring", type="date" )
  public static final String SP_AUTHORING_TIME = "authoring-time";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authoring-time</b>
   * <p>
   * Description: <b>Time of key DICOM object selection authoring</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImagingObjectSelection.authoringTime</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHORING_TIME = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHORING_TIME);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>UID of key DICOM object selection</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingObjectSelection.uid</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ImagingObjectSelection.uid", description="UID of key DICOM object selection", type="uri" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>UID of key DICOM object selection</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingObjectSelection.uid</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_IDENTIFIER);


}

