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
 * A manifest of a set of DICOM Service-Object Pair Instances (SOP Instances).  The referenced SOP Instances (images or other content) are for a single patient, and may be from one or more studies. The referenced SOP Instances have been selected for a purpose, such as quality assurance, conference, or consult. Reflecting that range of purposes, typical ImagingExcerpt resources may include all SOP Instances in a study (perhaps for sharing through a Health Information Exchange); key images from multiple studies (for reference by a referring or treating physician); a multi-frame ultrasound instance ("cine" video clip) and a set of measurements taken from that instance (for inclusion in a teaching file); and so on.
 */
@ResourceDef(name="ImagingExcerpt", profile="http://hl7.org/fhir/Profile/ImagingExcerpt")
public class ImagingExcerpt extends DomainResource {

    public enum DWebType {
        /**
         * Web Access to DICOM Persistent Objects - RESTful Services
         */
        WADORS, 
        /**
         * Web Access to DICOM Persistent Objects - URI
         */
        WADOURI, 
        /**
         * IHE - Invoke Image Display Profile
         */
        IID, 
        /**
         * Web Access to DICOM Persistent Objects - Web Services
         */
        WADOWS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DWebType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("WADO-RS".equals(codeString))
          return WADORS;
        if ("WADO-URI".equals(codeString))
          return WADOURI;
        if ("IID".equals(codeString))
          return IID;
        if ("WADO-WS".equals(codeString))
          return WADOWS;
        throw new FHIRException("Unknown DWebType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case WADORS: return "WADO-RS";
            case WADOURI: return "WADO-URI";
            case IID: return "IID";
            case WADOWS: return "WADO-WS";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case WADORS: return "http://hl7.org/fhir/dWebType";
            case WADOURI: return "http://hl7.org/fhir/dWebType";
            case IID: return "http://hl7.org/fhir/dWebType";
            case WADOWS: return "http://hl7.org/fhir/dWebType";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case WADORS: return "Web Access to DICOM Persistent Objects - RESTful Services";
            case WADOURI: return "Web Access to DICOM Persistent Objects - URI";
            case IID: return "IHE - Invoke Image Display Profile";
            case WADOWS: return "Web Access to DICOM Persistent Objects - Web Services";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case WADORS: return "WADO-RS";
            case WADOURI: return "WADO-URI";
            case IID: return "IID";
            case WADOWS: return "WADO-WS";
            default: return "?";
          }
        }
    }

  public static class DWebTypeEnumFactory implements EnumFactory<DWebType> {
    public DWebType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("WADO-RS".equals(codeString))
          return DWebType.WADORS;
        if ("WADO-URI".equals(codeString))
          return DWebType.WADOURI;
        if ("IID".equals(codeString))
          return DWebType.IID;
        if ("WADO-WS".equals(codeString))
          return DWebType.WADOWS;
        throw new IllegalArgumentException("Unknown DWebType code '"+codeString+"'");
        }
        public Enumeration<DWebType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("WADO-RS".equals(codeString))
          return new Enumeration<DWebType>(this, DWebType.WADORS);
        if ("WADO-URI".equals(codeString))
          return new Enumeration<DWebType>(this, DWebType.WADOURI);
        if ("IID".equals(codeString))
          return new Enumeration<DWebType>(this, DWebType.IID);
        if ("WADO-WS".equals(codeString))
          return new Enumeration<DWebType>(this, DWebType.WADOWS);
        throw new FHIRException("Unknown DWebType code '"+codeString+"'");
        }
    public String toCode(DWebType code) {
      if (code == DWebType.WADORS)
        return "WADO-RS";
      if (code == DWebType.WADOURI)
        return "WADO-URI";
      if (code == DWebType.IID)
        return "IID";
      if (code == DWebType.WADOWS)
        return "WADO-WS";
      return "?";
      }
    public String toSystem(DWebType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class StudyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Study instance UID of the SOP instances in the selection.
         */
        @Child(name = "uid", type = {OidType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Study instance UID", formalDefinition="Study instance UID of the SOP instances in the selection." )
        protected OidType uid;

        /**
         * Reference to the Imaging Study in FHIR form.
         */
        @Child(name = "imagingStudy", type = {ImagingStudy.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to ImagingStudy", formalDefinition="Reference to the Imaging Study in FHIR form." )
        protected Reference imagingStudy;

        /**
         * The actual object that is the target of the reference (Reference to the Imaging Study in FHIR form.)
         */
        protected ImagingStudy imagingStudyTarget;

        /**
         * Methods of accessing using DICOM web technologies.
         */
        @Child(name = "dicom", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dicom web access", formalDefinition="Methods of accessing using DICOM web technologies." )
        protected List<StudyDicomComponent> dicom;

        /**
         * A set of viewable reference images of various  types.
         */
        @Child(name = "viewable", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Viewable format", formalDefinition="A set of viewable reference images of various  types." )
        protected List<StudyViewableComponent> viewable;

        /**
         * Series identity and locating information of the DICOM SOP instances in the selection.
         */
        @Child(name = "series", type = {}, order=5, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Series identity of the selected instances", formalDefinition="Series identity and locating information of the DICOM SOP instances in the selection." )
        protected List<SeriesComponent> series;

        private static final long serialVersionUID = 1674060080L;

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
         * @return {@link #dicom} (Methods of accessing using DICOM web technologies.)
         */
        public List<StudyDicomComponent> getDicom() { 
          if (this.dicom == null)
            this.dicom = new ArrayList<StudyDicomComponent>();
          return this.dicom;
        }

        public boolean hasDicom() { 
          if (this.dicom == null)
            return false;
          for (StudyDicomComponent item : this.dicom)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dicom} (Methods of accessing using DICOM web technologies.)
         */
    // syntactic sugar
        public StudyDicomComponent addDicom() { //3
          StudyDicomComponent t = new StudyDicomComponent();
          if (this.dicom == null)
            this.dicom = new ArrayList<StudyDicomComponent>();
          this.dicom.add(t);
          return t;
        }

    // syntactic sugar
        public StudyComponent addDicom(StudyDicomComponent t) { //3
          if (t == null)
            return this;
          if (this.dicom == null)
            this.dicom = new ArrayList<StudyDicomComponent>();
          this.dicom.add(t);
          return this;
        }

        /**
         * @return {@link #viewable} (A set of viewable reference images of various  types.)
         */
        public List<StudyViewableComponent> getViewable() { 
          if (this.viewable == null)
            this.viewable = new ArrayList<StudyViewableComponent>();
          return this.viewable;
        }

        public boolean hasViewable() { 
          if (this.viewable == null)
            return false;
          for (StudyViewableComponent item : this.viewable)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #viewable} (A set of viewable reference images of various  types.)
         */
    // syntactic sugar
        public StudyViewableComponent addViewable() { //3
          StudyViewableComponent t = new StudyViewableComponent();
          if (this.viewable == null)
            this.viewable = new ArrayList<StudyViewableComponent>();
          this.viewable.add(t);
          return t;
        }

    // syntactic sugar
        public StudyComponent addViewable(StudyViewableComponent t) { //3
          if (t == null)
            return this;
          if (this.viewable == null)
            this.viewable = new ArrayList<StudyViewableComponent>();
          this.viewable.add(t);
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
          childrenList.add(new Property("imagingStudy", "Reference(ImagingStudy)", "Reference to the Imaging Study in FHIR form.", 0, java.lang.Integer.MAX_VALUE, imagingStudy));
          childrenList.add(new Property("dicom", "", "Methods of accessing using DICOM web technologies.", 0, java.lang.Integer.MAX_VALUE, dicom));
          childrenList.add(new Property("viewable", "", "A set of viewable reference images of various  types.", 0, java.lang.Integer.MAX_VALUE, viewable));
          childrenList.add(new Property("series", "", "Series identity and locating information of the DICOM SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, series));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case -814900911: /*imagingStudy*/ return this.imagingStudy == null ? new Base[0] : new Base[] {this.imagingStudy}; // Reference
        case 95578844: /*dicom*/ return this.dicom == null ? new Base[0] : this.dicom.toArray(new Base[this.dicom.size()]); // StudyDicomComponent
        case 1196225919: /*viewable*/ return this.viewable == null ? new Base[0] : this.viewable.toArray(new Base[this.viewable.size()]); // StudyViewableComponent
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
        case -814900911: // imagingStudy
          this.imagingStudy = castToReference(value); // Reference
          break;
        case 95578844: // dicom
          this.getDicom().add((StudyDicomComponent) value); // StudyDicomComponent
          break;
        case 1196225919: // viewable
          this.getViewable().add((StudyViewableComponent) value); // StudyViewableComponent
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
        else if (name.equals("imagingStudy"))
          this.imagingStudy = castToReference(value); // Reference
        else if (name.equals("dicom"))
          this.getDicom().add((StudyDicomComponent) value);
        else if (name.equals("viewable"))
          this.getViewable().add((StudyViewableComponent) value);
        else if (name.equals("series"))
          this.getSeries().add((SeriesComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case -814900911:  return getImagingStudy(); // Reference
        case 95578844:  return addDicom(); // StudyDicomComponent
        case 1196225919:  return addViewable(); // StudyViewableComponent
        case -905838985:  return addSeries(); // SeriesComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.uid");
        }
        else if (name.equals("imagingStudy")) {
          this.imagingStudy = new Reference();
          return this.imagingStudy;
        }
        else if (name.equals("dicom")) {
          return addDicom();
        }
        else if (name.equals("viewable")) {
          return addViewable();
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
        dst.imagingStudy = imagingStudy == null ? null : imagingStudy.copy();
        if (dicom != null) {
          dst.dicom = new ArrayList<StudyDicomComponent>();
          for (StudyDicomComponent i : dicom)
            dst.dicom.add(i.copy());
        };
        if (viewable != null) {
          dst.viewable = new ArrayList<StudyViewableComponent>();
          for (StudyViewableComponent i : viewable)
            dst.viewable.add(i.copy());
        };
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
        return compareDeep(uid, o.uid, true) && compareDeep(imagingStudy, o.imagingStudy, true) && compareDeep(dicom, o.dicom, true)
           && compareDeep(viewable, o.viewable, true) && compareDeep(series, o.series, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StudyComponent))
          return false;
        StudyComponent o = (StudyComponent) other;
        return compareValues(uid, o.uid, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (uid == null || uid.isEmpty()) && (imagingStudy == null || imagingStudy.isEmpty())
           && (dicom == null || dicom.isEmpty()) && (viewable == null || viewable.isEmpty()) && (series == null || series.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImagingExcerpt.study";

  }

  }

    @Block()
    public static class StudyDicomComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Access type for DICOM web.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="WADO-RS | WADO-URI | IID | WADO-WS", formalDefinition="Access type for DICOM web." )
        protected Enumeration<DWebType> type;

        /**
         * The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Retrieve study URL", formalDefinition="The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol." )
        protected UriType url;

        private static final long serialVersionUID = 1661664416L;

    /**
     * Constructor
     */
      public StudyDicomComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StudyDicomComponent(Enumeration<DWebType> type, UriType url) {
        super();
        this.type = type;
        this.url = url;
      }

        /**
         * @return {@link #type} (Access type for DICOM web.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<DWebType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyDicomComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<DWebType>(new DWebTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Access type for DICOM web.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StudyDicomComponent setTypeElement(Enumeration<DWebType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Access type for DICOM web.
         */
        public DWebType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Access type for DICOM web.
         */
        public StudyDicomComponent setType(DWebType value) { 
            if (this.type == null)
              this.type = new Enumeration<DWebType>(new DWebTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyDicomComponent.url");
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
         * @param value {@link #url} (The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public StudyDicomComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.
         */
        public StudyDicomComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Access type for DICOM web.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("url", "uri", "The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<DWebType>
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new DWebTypeEnumFactory().fromType(value); // Enumeration<DWebType>
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
          this.type = new DWebTypeEnumFactory().fromType(value); // Enumeration<DWebType>
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<DWebType>
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.type");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.url");
        }
        else
          return super.addChild(name);
      }

      public StudyDicomComponent copy() {
        StudyDicomComponent dst = new StudyDicomComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StudyDicomComponent))
          return false;
        StudyDicomComponent o = (StudyDicomComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StudyDicomComponent))
          return false;
        StudyDicomComponent o = (StudyDicomComponent) other;
        return compareValues(type, o.type, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (url == null || url.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImagingExcerpt.study.dicom";

  }

  }

    @Block()
    public static class StudyViewableComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.
         */
        @Child(name = "contentType", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Mime type of the content, with charset etc.", formalDefinition="Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate." )
        protected CodeType contentType;

        /**
         * Height of the image in pixels (photo/video).
         */
        @Child(name = "height", type = {PositiveIntType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Height of the image in pixels (photo/video)", formalDefinition="Height of the image in pixels (photo/video)." )
        protected PositiveIntType height;

        /**
         * Width of the image in pixels (photo/video).
         */
        @Child(name = "width", type = {PositiveIntType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Width of the image in pixels (photo/video)", formalDefinition="Width of the image in pixels (photo/video)." )
        protected PositiveIntType width;

        /**
         * The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif.
         */
        @Child(name = "frames", type = {PositiveIntType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Number of frames if > 1 (photo)", formalDefinition="The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif." )
        protected PositiveIntType frames;

        /**
         * The duration of the recording in seconds - for audio and video.
         */
        @Child(name = "duration", type = {UnsignedIntType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Length in seconds (audio / video)", formalDefinition="The duration of the recording in seconds - for audio and video." )
        protected UnsignedIntType duration;

        /**
         * The number of bytes of data that make up this attachment.
         */
        @Child(name = "size", type = {UnsignedIntType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Number of bytes of content (if url provided)", formalDefinition="The number of bytes of data that make up this attachment." )
        protected UnsignedIntType size;

        /**
         * A label or set of text to display in place of the data.
         */
        @Child(name = "title", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Label to display in place of the data", formalDefinition="A label or set of text to display in place of the data." )
        protected StringType title;

        /**
         * A location where the data can be accessed.
         */
        @Child(name = "url", type = {UriType.class}, order=8, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Uri where the data can be found", formalDefinition="A location where the data can be accessed." )
        protected UriType url;

        private static final long serialVersionUID = -2135689428L;

    /**
     * Constructor
     */
      public StudyViewableComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StudyViewableComponent(CodeType contentType, UriType url) {
        super();
        this.contentType = contentType;
        this.url = url;
      }

        /**
         * @return {@link #contentType} (Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public CodeType getContentTypeElement() { 
          if (this.contentType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyViewableComponent.contentType");
            else if (Configuration.doAutoCreate())
              this.contentType = new CodeType(); // bb
          return this.contentType;
        }

        public boolean hasContentTypeElement() { 
          return this.contentType != null && !this.contentType.isEmpty();
        }

        public boolean hasContentType() { 
          return this.contentType != null && !this.contentType.isEmpty();
        }

        /**
         * @param value {@link #contentType} (Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public StudyViewableComponent setContentTypeElement(CodeType value) { 
          this.contentType = value;
          return this;
        }

        /**
         * @return Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.
         */
        public String getContentType() { 
          return this.contentType == null ? null : this.contentType.getValue();
        }

        /**
         * @param value Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.
         */
        public StudyViewableComponent setContentType(String value) { 
            if (this.contentType == null)
              this.contentType = new CodeType();
            this.contentType.setValue(value);
          return this;
        }

        /**
         * @return {@link #height} (Height of the image in pixels (photo/video).). This is the underlying object with id, value and extensions. The accessor "getHeight" gives direct access to the value
         */
        public PositiveIntType getHeightElement() { 
          if (this.height == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyViewableComponent.height");
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
        public StudyViewableComponent setHeightElement(PositiveIntType value) { 
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
        public StudyViewableComponent setHeight(int value) { 
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
              throw new Error("Attempt to auto-create StudyViewableComponent.width");
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
        public StudyViewableComponent setWidthElement(PositiveIntType value) { 
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
        public StudyViewableComponent setWidth(int value) { 
            if (this.width == null)
              this.width = new PositiveIntType();
            this.width.setValue(value);
          return this;
        }

        /**
         * @return {@link #frames} (The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif.). This is the underlying object with id, value and extensions. The accessor "getFrames" gives direct access to the value
         */
        public PositiveIntType getFramesElement() { 
          if (this.frames == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyViewableComponent.frames");
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
         * @param value {@link #frames} (The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif.). This is the underlying object with id, value and extensions. The accessor "getFrames" gives direct access to the value
         */
        public StudyViewableComponent setFramesElement(PositiveIntType value) { 
          this.frames = value;
          return this;
        }

        /**
         * @return The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif.
         */
        public int getFrames() { 
          return this.frames == null || this.frames.isEmpty() ? 0 : this.frames.getValue();
        }

        /**
         * @param value The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif.
         */
        public StudyViewableComponent setFrames(int value) { 
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
              throw new Error("Attempt to auto-create StudyViewableComponent.duration");
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
        public StudyViewableComponent setDurationElement(UnsignedIntType value) { 
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
        public StudyViewableComponent setDuration(int value) { 
            if (this.duration == null)
              this.duration = new UnsignedIntType();
            this.duration.setValue(value);
          return this;
        }

        /**
         * @return {@link #size} (The number of bytes of data that make up this attachment.). This is the underlying object with id, value and extensions. The accessor "getSize" gives direct access to the value
         */
        public UnsignedIntType getSizeElement() { 
          if (this.size == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyViewableComponent.size");
            else if (Configuration.doAutoCreate())
              this.size = new UnsignedIntType(); // bb
          return this.size;
        }

        public boolean hasSizeElement() { 
          return this.size != null && !this.size.isEmpty();
        }

        public boolean hasSize() { 
          return this.size != null && !this.size.isEmpty();
        }

        /**
         * @param value {@link #size} (The number of bytes of data that make up this attachment.). This is the underlying object with id, value and extensions. The accessor "getSize" gives direct access to the value
         */
        public StudyViewableComponent setSizeElement(UnsignedIntType value) { 
          this.size = value;
          return this;
        }

        /**
         * @return The number of bytes of data that make up this attachment.
         */
        public int getSize() { 
          return this.size == null || this.size.isEmpty() ? 0 : this.size.getValue();
        }

        /**
         * @param value The number of bytes of data that make up this attachment.
         */
        public StudyViewableComponent setSize(int value) { 
            if (this.size == null)
              this.size = new UnsignedIntType();
            this.size.setValue(value);
          return this;
        }

        /**
         * @return {@link #title} (A label or set of text to display in place of the data.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyViewableComponent.title");
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
         * @param value {@link #title} (A label or set of text to display in place of the data.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StudyViewableComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return A label or set of text to display in place of the data.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value A label or set of text to display in place of the data.
         */
        public StudyViewableComponent setTitle(String value) { 
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
         * @return {@link #url} (A location where the data can be accessed.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyViewableComponent.url");
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
         * @param value {@link #url} (A location where the data can be accessed.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public StudyViewableComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return A location where the data can be accessed.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value A location where the data can be accessed.
         */
        public StudyViewableComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("contentType", "code", "Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.", 0, java.lang.Integer.MAX_VALUE, contentType));
          childrenList.add(new Property("height", "positiveInt", "Height of the image in pixels (photo/video).", 0, java.lang.Integer.MAX_VALUE, height));
          childrenList.add(new Property("width", "positiveInt", "Width of the image in pixels (photo/video).", 0, java.lang.Integer.MAX_VALUE, width));
          childrenList.add(new Property("frames", "positiveInt", "The number of frames in a photo. This is used with a multi-page fax, or an imaging acquisition context that takes multiple slices in a single image, or an animated gif.", 0, java.lang.Integer.MAX_VALUE, frames));
          childrenList.add(new Property("duration", "unsignedInt", "The duration of the recording in seconds - for audio and video.", 0, java.lang.Integer.MAX_VALUE, duration));
          childrenList.add(new Property("size", "unsignedInt", "The number of bytes of data that make up this attachment.", 0, java.lang.Integer.MAX_VALUE, size));
          childrenList.add(new Property("title", "string", "A label or set of text to display in place of the data.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("url", "uri", "A location where the data can be accessed.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -389131437: /*contentType*/ return this.contentType == null ? new Base[0] : new Base[] {this.contentType}; // CodeType
        case -1221029593: /*height*/ return this.height == null ? new Base[0] : new Base[] {this.height}; // PositiveIntType
        case 113126854: /*width*/ return this.width == null ? new Base[0] : new Base[] {this.width}; // PositiveIntType
        case -1266514778: /*frames*/ return this.frames == null ? new Base[0] : new Base[] {this.frames}; // PositiveIntType
        case -1992012396: /*duration*/ return this.duration == null ? new Base[0] : new Base[] {this.duration}; // UnsignedIntType
        case 3530753: /*size*/ return this.size == null ? new Base[0] : new Base[] {this.size}; // UnsignedIntType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -389131437: // contentType
          this.contentType = castToCode(value); // CodeType
          break;
        case -1221029593: // height
          this.height = castToPositiveInt(value); // PositiveIntType
          break;
        case 113126854: // width
          this.width = castToPositiveInt(value); // PositiveIntType
          break;
        case -1266514778: // frames
          this.frames = castToPositiveInt(value); // PositiveIntType
          break;
        case -1992012396: // duration
          this.duration = castToUnsignedInt(value); // UnsignedIntType
          break;
        case 3530753: // size
          this.size = castToUnsignedInt(value); // UnsignedIntType
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("contentType"))
          this.contentType = castToCode(value); // CodeType
        else if (name.equals("height"))
          this.height = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("width"))
          this.width = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("frames"))
          this.frames = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("duration"))
          this.duration = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("size"))
          this.size = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -389131437: throw new FHIRException("Cannot make property contentType as it is not a complex type"); // CodeType
        case -1221029593: throw new FHIRException("Cannot make property height as it is not a complex type"); // PositiveIntType
        case 113126854: throw new FHIRException("Cannot make property width as it is not a complex type"); // PositiveIntType
        case -1266514778: throw new FHIRException("Cannot make property frames as it is not a complex type"); // PositiveIntType
        case -1992012396: throw new FHIRException("Cannot make property duration as it is not a complex type"); // UnsignedIntType
        case 3530753: throw new FHIRException("Cannot make property size as it is not a complex type"); // UnsignedIntType
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentType")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.contentType");
        }
        else if (name.equals("height")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.height");
        }
        else if (name.equals("width")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.width");
        }
        else if (name.equals("frames")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.frames");
        }
        else if (name.equals("duration")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.duration");
        }
        else if (name.equals("size")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.size");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.title");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.url");
        }
        else
          return super.addChild(name);
      }

      public StudyViewableComponent copy() {
        StudyViewableComponent dst = new StudyViewableComponent();
        copyValues(dst);
        dst.contentType = contentType == null ? null : contentType.copy();
        dst.height = height == null ? null : height.copy();
        dst.width = width == null ? null : width.copy();
        dst.frames = frames == null ? null : frames.copy();
        dst.duration = duration == null ? null : duration.copy();
        dst.size = size == null ? null : size.copy();
        dst.title = title == null ? null : title.copy();
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StudyViewableComponent))
          return false;
        StudyViewableComponent o = (StudyViewableComponent) other;
        return compareDeep(contentType, o.contentType, true) && compareDeep(height, o.height, true) && compareDeep(width, o.width, true)
           && compareDeep(frames, o.frames, true) && compareDeep(duration, o.duration, true) && compareDeep(size, o.size, true)
           && compareDeep(title, o.title, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StudyViewableComponent))
          return false;
        StudyViewableComponent o = (StudyViewableComponent) other;
        return compareValues(contentType, o.contentType, true) && compareValues(height, o.height, true) && compareValues(width, o.width, true)
           && compareValues(frames, o.frames, true) && compareValues(duration, o.duration, true) && compareValues(size, o.size, true)
           && compareValues(title, o.title, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (contentType == null || contentType.isEmpty()) && (height == null || height.isEmpty())
           && (width == null || width.isEmpty()) && (frames == null || frames.isEmpty()) && (duration == null || duration.isEmpty())
           && (size == null || size.isEmpty()) && (title == null || title.isEmpty()) && (url == null || url.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImagingExcerpt.study.viewable";

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
         * Methods of accessing using DICOM web technologies.
         */
        @Child(name = "dicom", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dicom web access", formalDefinition="Methods of accessing using DICOM web technologies." )
        protected List<SeriesDicomComponent> dicom;

        /**
         * Identity and locating information of the selected DICOM SOP instances.
         */
        @Child(name = "instance", type = {}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The selected instance", formalDefinition="Identity and locating information of the selected DICOM SOP instances." )
        protected List<InstanceComponent> instance;

        private static final long serialVersionUID = 1845643577L;

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
         * @return {@link #dicom} (Methods of accessing using DICOM web technologies.)
         */
        public List<SeriesDicomComponent> getDicom() { 
          if (this.dicom == null)
            this.dicom = new ArrayList<SeriesDicomComponent>();
          return this.dicom;
        }

        public boolean hasDicom() { 
          if (this.dicom == null)
            return false;
          for (SeriesDicomComponent item : this.dicom)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dicom} (Methods of accessing using DICOM web technologies.)
         */
    // syntactic sugar
        public SeriesDicomComponent addDicom() { //3
          SeriesDicomComponent t = new SeriesDicomComponent();
          if (this.dicom == null)
            this.dicom = new ArrayList<SeriesDicomComponent>();
          this.dicom.add(t);
          return t;
        }

    // syntactic sugar
        public SeriesComponent addDicom(SeriesDicomComponent t) { //3
          if (t == null)
            return this;
          if (this.dicom == null)
            this.dicom = new ArrayList<SeriesDicomComponent>();
          this.dicom.add(t);
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
          childrenList.add(new Property("dicom", "", "Methods of accessing using DICOM web technologies.", 0, java.lang.Integer.MAX_VALUE, dicom));
          childrenList.add(new Property("instance", "", "Identity and locating information of the selected DICOM SOP instances.", 0, java.lang.Integer.MAX_VALUE, instance));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case 95578844: /*dicom*/ return this.dicom == null ? new Base[0] : this.dicom.toArray(new Base[this.dicom.size()]); // SeriesDicomComponent
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
        case 95578844: // dicom
          this.getDicom().add((SeriesDicomComponent) value); // SeriesDicomComponent
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
        else if (name.equals("dicom"))
          this.getDicom().add((SeriesDicomComponent) value);
        else if (name.equals("instance"))
          this.getInstance().add((InstanceComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case 95578844:  return addDicom(); // SeriesDicomComponent
        case 555127957:  return addInstance(); // InstanceComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.uid");
        }
        else if (name.equals("dicom")) {
          return addDicom();
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
        if (dicom != null) {
          dst.dicom = new ArrayList<SeriesDicomComponent>();
          for (SeriesDicomComponent i : dicom)
            dst.dicom.add(i.copy());
        };
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
        return compareDeep(uid, o.uid, true) && compareDeep(dicom, o.dicom, true) && compareDeep(instance, o.instance, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SeriesComponent))
          return false;
        SeriesComponent o = (SeriesComponent) other;
        return compareValues(uid, o.uid, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (uid == null || uid.isEmpty()) && (dicom == null || dicom.isEmpty())
           && (instance == null || instance.isEmpty());
      }

  public String fhirType() {
    return "ImagingExcerpt.study.series";

  }

  }

    @Block()
    public static class SeriesDicomComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Access type for DICOM web.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="WADO-RS | WADO-URI | IID | WADO-WS", formalDefinition="Access type for DICOM web." )
        protected Enumeration<DWebType> type;

        /**
         * The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Retrieve study URL", formalDefinition="The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol." )
        protected UriType url;

        private static final long serialVersionUID = 1661664416L;

    /**
     * Constructor
     */
      public SeriesDicomComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SeriesDicomComponent(Enumeration<DWebType> type, UriType url) {
        super();
        this.type = type;
        this.url = url;
      }

        /**
         * @return {@link #type} (Access type for DICOM web.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<DWebType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SeriesDicomComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<DWebType>(new DWebTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Access type for DICOM web.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public SeriesDicomComponent setTypeElement(Enumeration<DWebType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Access type for DICOM web.
         */
        public DWebType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Access type for DICOM web.
         */
        public SeriesDicomComponent setType(DWebType value) { 
            if (this.type == null)
              this.type = new Enumeration<DWebType>(new DWebTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SeriesDicomComponent.url");
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
         * @param value {@link #url} (The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public SeriesDicomComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.
         */
        public SeriesDicomComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Access type for DICOM web.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("url", "uri", "The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<DWebType>
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new DWebTypeEnumFactory().fromType(value); // Enumeration<DWebType>
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
          this.type = new DWebTypeEnumFactory().fromType(value); // Enumeration<DWebType>
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<DWebType>
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.type");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.url");
        }
        else
          return super.addChild(name);
      }

      public SeriesDicomComponent copy() {
        SeriesDicomComponent dst = new SeriesDicomComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SeriesDicomComponent))
          return false;
        SeriesDicomComponent o = (SeriesDicomComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SeriesDicomComponent))
          return false;
        SeriesDicomComponent o = (SeriesDicomComponent) other;
        return compareValues(type, o.type, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (url == null || url.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImagingExcerpt.study.series.dicom";

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
         * Methods of accessing using DICOM web technologies.
         */
        @Child(name = "dicom", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dicom web access", formalDefinition="Methods of accessing using DICOM web technologies." )
        protected List<InstanceDicomComponent> dicom;

        /**
         * The specific frame reference within a multi-frame object.
         */
        @Child(name = "frameNumbers", type = {UnsignedIntType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Frame reference number", formalDefinition="The specific frame reference within a multi-frame object." )
        protected List<UnsignedIntType> frameNumbers;

        private static final long serialVersionUID = 1372440557L;

    /**
     * Constructor
     */
      public InstanceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InstanceComponent(OidType sopClass, OidType uid) {
        super();
        this.sopClass = sopClass;
        this.uid = uid;
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
         * @return {@link #dicom} (Methods of accessing using DICOM web technologies.)
         */
        public List<InstanceDicomComponent> getDicom() { 
          if (this.dicom == null)
            this.dicom = new ArrayList<InstanceDicomComponent>();
          return this.dicom;
        }

        public boolean hasDicom() { 
          if (this.dicom == null)
            return false;
          for (InstanceDicomComponent item : this.dicom)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dicom} (Methods of accessing using DICOM web technologies.)
         */
    // syntactic sugar
        public InstanceDicomComponent addDicom() { //3
          InstanceDicomComponent t = new InstanceDicomComponent();
          if (this.dicom == null)
            this.dicom = new ArrayList<InstanceDicomComponent>();
          this.dicom.add(t);
          return t;
        }

    // syntactic sugar
        public InstanceComponent addDicom(InstanceDicomComponent t) { //3
          if (t == null)
            return this;
          if (this.dicom == null)
            this.dicom = new ArrayList<InstanceDicomComponent>();
          this.dicom.add(t);
          return this;
        }

        /**
         * @return {@link #frameNumbers} (The specific frame reference within a multi-frame object.)
         */
        public List<UnsignedIntType> getFrameNumbers() { 
          if (this.frameNumbers == null)
            this.frameNumbers = new ArrayList<UnsignedIntType>();
          return this.frameNumbers;
        }

        public boolean hasFrameNumbers() { 
          if (this.frameNumbers == null)
            return false;
          for (UnsignedIntType item : this.frameNumbers)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #frameNumbers} (The specific frame reference within a multi-frame object.)
         */
    // syntactic sugar
        public UnsignedIntType addFrameNumbersElement() {//2 
          UnsignedIntType t = new UnsignedIntType();
          if (this.frameNumbers == null)
            this.frameNumbers = new ArrayList<UnsignedIntType>();
          this.frameNumbers.add(t);
          return t;
        }

        /**
         * @param value {@link #frameNumbers} (The specific frame reference within a multi-frame object.)
         */
        public InstanceComponent addFrameNumbers(int value) { //1
          UnsignedIntType t = new UnsignedIntType();
          t.setValue(value);
          if (this.frameNumbers == null)
            this.frameNumbers = new ArrayList<UnsignedIntType>();
          this.frameNumbers.add(t);
          return this;
        }

        /**
         * @param value {@link #frameNumbers} (The specific frame reference within a multi-frame object.)
         */
        public boolean hasFrameNumbers(int value) { 
          if (this.frameNumbers == null)
            return false;
          for (UnsignedIntType v : this.frameNumbers)
            if (v.equals(value)) // unsignedInt
              return true;
          return false;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sopClass", "oid", "SOP class UID of the selected instance.", 0, java.lang.Integer.MAX_VALUE, sopClass));
          childrenList.add(new Property("uid", "oid", "SOP Instance UID of the selected instance.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("dicom", "", "Methods of accessing using DICOM web technologies.", 0, java.lang.Integer.MAX_VALUE, dicom));
          childrenList.add(new Property("frameNumbers", "unsignedInt", "The specific frame reference within a multi-frame object.", 0, java.lang.Integer.MAX_VALUE, frameNumbers));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1560041540: /*sopClass*/ return this.sopClass == null ? new Base[0] : new Base[] {this.sopClass}; // OidType
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case 95578844: /*dicom*/ return this.dicom == null ? new Base[0] : this.dicom.toArray(new Base[this.dicom.size()]); // InstanceDicomComponent
        case -144148451: /*frameNumbers*/ return this.frameNumbers == null ? new Base[0] : this.frameNumbers.toArray(new Base[this.frameNumbers.size()]); // UnsignedIntType
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
        case 95578844: // dicom
          this.getDicom().add((InstanceDicomComponent) value); // InstanceDicomComponent
          break;
        case -144148451: // frameNumbers
          this.getFrameNumbers().add(castToUnsignedInt(value)); // UnsignedIntType
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
        else if (name.equals("dicom"))
          this.getDicom().add((InstanceDicomComponent) value);
        else if (name.equals("frameNumbers"))
          this.getFrameNumbers().add(castToUnsignedInt(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1560041540: throw new FHIRException("Cannot make property sopClass as it is not a complex type"); // OidType
        case 115792: throw new FHIRException("Cannot make property uid as it is not a complex type"); // OidType
        case 95578844:  return addDicom(); // InstanceDicomComponent
        case -144148451: throw new FHIRException("Cannot make property frameNumbers as it is not a complex type"); // UnsignedIntType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sopClass")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.sopClass");
        }
        else if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.uid");
        }
        else if (name.equals("dicom")) {
          return addDicom();
        }
        else if (name.equals("frameNumbers")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.frameNumbers");
        }
        else
          return super.addChild(name);
      }

      public InstanceComponent copy() {
        InstanceComponent dst = new InstanceComponent();
        copyValues(dst);
        dst.sopClass = sopClass == null ? null : sopClass.copy();
        dst.uid = uid == null ? null : uid.copy();
        if (dicom != null) {
          dst.dicom = new ArrayList<InstanceDicomComponent>();
          for (InstanceDicomComponent i : dicom)
            dst.dicom.add(i.copy());
        };
        if (frameNumbers != null) {
          dst.frameNumbers = new ArrayList<UnsignedIntType>();
          for (UnsignedIntType i : frameNumbers)
            dst.frameNumbers.add(i.copy());
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
        return compareDeep(sopClass, o.sopClass, true) && compareDeep(uid, o.uid, true) && compareDeep(dicom, o.dicom, true)
           && compareDeep(frameNumbers, o.frameNumbers, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof InstanceComponent))
          return false;
        InstanceComponent o = (InstanceComponent) other;
        return compareValues(sopClass, o.sopClass, true) && compareValues(uid, o.uid, true) && compareValues(frameNumbers, o.frameNumbers, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sopClass == null || sopClass.isEmpty()) && (uid == null || uid.isEmpty())
           && (dicom == null || dicom.isEmpty()) && (frameNumbers == null || frameNumbers.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImagingExcerpt.study.series.instance";

  }

  }

    @Block()
    public static class InstanceDicomComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Access type for DICOM web.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="WADO-RS | WADO-URI | IID | WADO-WS", formalDefinition="Access type for DICOM web." )
        protected Enumeration<DWebType> type;

        /**
         * The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Retrieve study URL", formalDefinition="The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol." )
        protected UriType url;

        private static final long serialVersionUID = 1661664416L;

    /**
     * Constructor
     */
      public InstanceDicomComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InstanceDicomComponent(Enumeration<DWebType> type, UriType url) {
        super();
        this.type = type;
        this.url = url;
      }

        /**
         * @return {@link #type} (Access type for DICOM web.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<DWebType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InstanceDicomComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<DWebType>(new DWebTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Access type for DICOM web.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public InstanceDicomComponent setTypeElement(Enumeration<DWebType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Access type for DICOM web.
         */
        public DWebType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Access type for DICOM web.
         */
        public InstanceDicomComponent setType(DWebType value) { 
            if (this.type == null)
              this.type = new Enumeration<DWebType>(new DWebTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InstanceDicomComponent.url");
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
         * @param value {@link #url} (The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public InstanceDicomComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.
         */
        public InstanceDicomComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Access type for DICOM web.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("url", "uri", "The source system root URL / base URL, from which all content can be retrieved using the specified DICOM protocol.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<DWebType>
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new DWebTypeEnumFactory().fromType(value); // Enumeration<DWebType>
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
          this.type = new DWebTypeEnumFactory().fromType(value); // Enumeration<DWebType>
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<DWebType>
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.type");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.url");
        }
        else
          return super.addChild(name);
      }

      public InstanceDicomComponent copy() {
        InstanceDicomComponent dst = new InstanceDicomComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof InstanceDicomComponent))
          return false;
        InstanceDicomComponent o = (InstanceDicomComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof InstanceDicomComponent))
          return false;
        InstanceDicomComponent o = (InstanceDicomComponent) other;
        return compareValues(type, o.type, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (url == null || url.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImagingExcerpt.study.series.instance.dicom";

  }

  }

    /**
     * Unique identifier of the DICOM Key Object Selection (KOS) representation.
     */
    @Child(name = "uid", type = {OidType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Instance UID", formalDefinition="Unique identifier of the DICOM Key Object Selection (KOS) representation." )
    protected OidType uid;

    /**
     * A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingExcerpt.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient of the selected objects", formalDefinition="A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingExcerpt." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingExcerpt.)
     */
    protected Patient patientTarget;

    /**
     * Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).
     */
    @Child(name = "authoringTime", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time when the imaging object selection was created", formalDefinition="Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image)." )
    protected DateTimeType authoringTime;

    /**
     * Author of ImagingExcerpt. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.
     */
    @Child(name = "author", type = {Practitioner.class, Device.class, Organization.class, Patient.class, RelatedPerson.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Author (human or machine)", formalDefinition="Author of ImagingExcerpt. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Author of ImagingExcerpt. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    protected Resource authorTarget;

    /**
     * The reason for, or significance of, the selection of objects referenced in the resource.
     */
    @Child(name = "title", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for selection", formalDefinition="The reason for, or significance of, the selection of objects referenced in the resource." )
    protected CodeableConcept title;

    /**
     * Text description of the DICOM SOP instances selected in the ImagingExcerpt. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.
     */
    @Child(name = "description", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Description text", formalDefinition="Text description of the DICOM SOP instances selected in the ImagingExcerpt. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection." )
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
    public ImagingExcerpt() {
      super();
    }

  /**
   * Constructor
   */
    public ImagingExcerpt(OidType uid, Reference patient, CodeableConcept title) {
      super();
      this.uid = uid;
      this.patient = patient;
      this.title = title;
    }

    /**
     * @return {@link #uid} (Unique identifier of the DICOM Key Object Selection (KOS) representation.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public OidType getUidElement() { 
      if (this.uid == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingExcerpt.uid");
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
     * @param value {@link #uid} (Unique identifier of the DICOM Key Object Selection (KOS) representation.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public ImagingExcerpt setUidElement(OidType value) { 
      this.uid = value;
      return this;
    }

    /**
     * @return Unique identifier of the DICOM Key Object Selection (KOS) representation.
     */
    public String getUid() { 
      return this.uid == null ? null : this.uid.getValue();
    }

    /**
     * @param value Unique identifier of the DICOM Key Object Selection (KOS) representation.
     */
    public ImagingExcerpt setUid(String value) { 
        if (this.uid == null)
          this.uid = new OidType();
        this.uid.setValue(value);
      return this;
    }

    /**
     * @return {@link #patient} (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingExcerpt.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingExcerpt.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingExcerpt.)
     */
    public ImagingExcerpt setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingExcerpt.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingExcerpt.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingExcerpt.)
     */
    public ImagingExcerpt setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #authoringTime} (Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).). This is the underlying object with id, value and extensions. The accessor "getAuthoringTime" gives direct access to the value
     */
    public DateTimeType getAuthoringTimeElement() { 
      if (this.authoringTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingExcerpt.authoringTime");
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
    public ImagingExcerpt setAuthoringTimeElement(DateTimeType value) { 
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
    public ImagingExcerpt setAuthoringTime(Date value) { 
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
     * @return {@link #author} (Author of ImagingExcerpt. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingExcerpt.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Author of ImagingExcerpt. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public ImagingExcerpt setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Author of ImagingExcerpt. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Author of ImagingExcerpt. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public ImagingExcerpt setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #title} (The reason for, or significance of, the selection of objects referenced in the resource.)
     */
    public CodeableConcept getTitle() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingExcerpt.title");
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
    public ImagingExcerpt setTitle(CodeableConcept value) { 
      this.title = value;
      return this;
    }

    /**
     * @return {@link #description} (Text description of the DICOM SOP instances selected in the ImagingExcerpt. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingExcerpt.description");
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
     * @param value {@link #description} (Text description of the DICOM SOP instances selected in the ImagingExcerpt. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImagingExcerpt setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Text description of the DICOM SOP instances selected in the ImagingExcerpt. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Text description of the DICOM SOP instances selected in the ImagingExcerpt. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.
     */
    public ImagingExcerpt setDescription(String value) { 
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
    public ImagingExcerpt addStudy(StudyComponent t) { //3
      if (t == null)
        return this;
      if (this.study == null)
        this.study = new ArrayList<StudyComponent>();
      this.study.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("uid", "oid", "Unique identifier of the DICOM Key Object Selection (KOS) representation.", 0, java.lang.Integer.MAX_VALUE, uid));
        childrenList.add(new Property("patient", "Reference(Patient)", "A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingExcerpt.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("authoringTime", "dateTime", "Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).", 0, java.lang.Integer.MAX_VALUE, authoringTime));
        childrenList.add(new Property("author", "Reference(Practitioner|Device|Organization|Patient|RelatedPerson)", "Author of ImagingExcerpt. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("title", "CodeableConcept", "The reason for, or significance of, the selection of objects referenced in the resource.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("description", "string", "Text description of the DICOM SOP instances selected in the ImagingExcerpt. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, description));
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
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.uid");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("authoringTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.authoringTime");
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
          throw new FHIRException("Cannot call addChild on a primitive type ImagingExcerpt.description");
        }
        else if (name.equals("study")) {
          return addStudy();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImagingExcerpt";

  }

      public ImagingExcerpt copy() {
        ImagingExcerpt dst = new ImagingExcerpt();
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

      protected ImagingExcerpt typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingExcerpt))
          return false;
        ImagingExcerpt o = (ImagingExcerpt) other;
        return compareDeep(uid, o.uid, true) && compareDeep(patient, o.patient, true) && compareDeep(authoringTime, o.authoringTime, true)
           && compareDeep(author, o.author, true) && compareDeep(title, o.title, true) && compareDeep(description, o.description, true)
           && compareDeep(study, o.study, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingExcerpt))
          return false;
        ImagingExcerpt o = (ImagingExcerpt) other;
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
    return ResourceType.ImagingExcerpt;
   }

 /**
   * Search parameter: <b>selected-study</b>
   * <p>
   * Description: <b>Study selected in key DICOM object selection</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingExcerpt.study.uid</b><br>
   * </p>
   */
  @SearchParamDefinition(name="selected-study", path="ImagingExcerpt.study.uid", description="Study selected in key DICOM object selection", type="uri" )
  public static final String SP_SELECTED_STUDY = "selected-study";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>selected-study</b>
   * <p>
   * Description: <b>Study selected in key DICOM object selection</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingExcerpt.study.uid</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam SELECTED_STUDY = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_SELECTED_STUDY);

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>Author of key DICOM object selection</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingExcerpt.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="ImagingExcerpt.author", description="Author of key DICOM object selection", type="reference" )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>Author of key DICOM object selection</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingExcerpt.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingExcerpt:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("ImagingExcerpt:author").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Title of key DICOM object selection</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingExcerpt.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ImagingExcerpt.title", description="Title of key DICOM object selection", type="token" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Title of key DICOM object selection</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingExcerpt.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TITLE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TITLE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Subject of key DICOM object selection</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingExcerpt.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ImagingExcerpt.patient", description="Subject of key DICOM object selection", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Subject of key DICOM object selection</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingExcerpt.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingExcerpt:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ImagingExcerpt:patient").toLocked();

 /**
   * Search parameter: <b>authoring-time</b>
   * <p>
   * Description: <b>Time of key DICOM object selection authoring</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImagingExcerpt.authoringTime</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authoring-time", path="ImagingExcerpt.authoringTime", description="Time of key DICOM object selection authoring", type="date" )
  public static final String SP_AUTHORING_TIME = "authoring-time";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authoring-time</b>
   * <p>
   * Description: <b>Time of key DICOM object selection authoring</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImagingExcerpt.authoringTime</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHORING_TIME = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHORING_TIME);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>UID of key DICOM object selection</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingExcerpt.uid</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ImagingExcerpt.uid", description="UID of key DICOM object selection", type="uri" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>UID of key DICOM object selection</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingExcerpt.uid</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_IDENTIFIER);


}

