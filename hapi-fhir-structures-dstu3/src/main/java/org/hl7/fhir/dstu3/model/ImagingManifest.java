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
 * A text description of the DICOM SOP instances selected in the ImagingManifest; or the reason for, or significance of, the selection.
 */
@ResourceDef(name="ImagingManifest", profile="http://hl7.org/fhir/Profile/ImagingManifest")
public class ImagingManifest extends DomainResource {

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
         * The network service providing access (e.g., query, view, or retrieval) for the study. See implementation notes for information about using DICOM endpoints. A study-level endpoint applies to each series in the study, unless overridden by a series-level endpoint with the same Endpoint.type.
         */
        @Child(name = "endpoint", type = {Endpoint.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Study access service endpoint", formalDefinition="The network service providing access (e.g., query, view, or retrieval) for the study. See implementation notes for information about using DICOM endpoints. A study-level endpoint applies to each series in the study, unless overridden by a series-level endpoint with the same Endpoint.type." )
        protected List<Reference> endpoint;
        /**
         * The actual objects that are the target of the reference (The network service providing access (e.g., query, view, or retrieval) for the study. See implementation notes for information about using DICOM endpoints. A study-level endpoint applies to each series in the study, unless overridden by a series-level endpoint with the same Endpoint.type.)
         */
        protected List<Endpoint> endpointTarget;


        /**
         * Series identity and locating information of the DICOM SOP instances in the selection.
         */
        @Child(name = "series", type = {}, order=4, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Series identity of the selected instances", formalDefinition="Series identity and locating information of the DICOM SOP instances in the selection." )
        protected List<SeriesComponent> series;

        private static final long serialVersionUID = -538170921L;

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
         * @return {@link #endpoint} (The network service providing access (e.g., query, view, or retrieval) for the study. See implementation notes for information about using DICOM endpoints. A study-level endpoint applies to each series in the study, unless overridden by a series-level endpoint with the same Endpoint.type.)
         */
        public List<Reference> getEndpoint() { 
          if (this.endpoint == null)
            this.endpoint = new ArrayList<Reference>();
          return this.endpoint;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public StudyComponent setEndpoint(List<Reference> theEndpoint) { 
          this.endpoint = theEndpoint;
          return this;
        }

        public boolean hasEndpoint() { 
          if (this.endpoint == null)
            return false;
          for (Reference item : this.endpoint)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addEndpoint() { //3
          Reference t = new Reference();
          if (this.endpoint == null)
            this.endpoint = new ArrayList<Reference>();
          this.endpoint.add(t);
          return t;
        }

        public StudyComponent addEndpoint(Reference t) { //3
          if (t == null)
            return this;
          if (this.endpoint == null)
            this.endpoint = new ArrayList<Reference>();
          this.endpoint.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #endpoint}, creating it if it does not already exist
         */
        public Reference getEndpointFirstRep() { 
          if (getEndpoint().isEmpty()) {
            addEndpoint();
          }
          return getEndpoint().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Endpoint> getEndpointTarget() { 
          if (this.endpointTarget == null)
            this.endpointTarget = new ArrayList<Endpoint>();
          return this.endpointTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Endpoint addEndpointTarget() { 
          Endpoint r = new Endpoint();
          if (this.endpointTarget == null)
            this.endpointTarget = new ArrayList<Endpoint>();
          this.endpointTarget.add(r);
          return r;
        }

        /**
         * @return {@link #series} (Series identity and locating information of the DICOM SOP instances in the selection.)
         */
        public List<SeriesComponent> getSeries() { 
          if (this.series == null)
            this.series = new ArrayList<SeriesComponent>();
          return this.series;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public StudyComponent setSeries(List<SeriesComponent> theSeries) { 
          this.series = theSeries;
          return this;
        }

        public boolean hasSeries() { 
          if (this.series == null)
            return false;
          for (SeriesComponent item : this.series)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SeriesComponent addSeries() { //3
          SeriesComponent t = new SeriesComponent();
          if (this.series == null)
            this.series = new ArrayList<SeriesComponent>();
          this.series.add(t);
          return t;
        }

        public StudyComponent addSeries(SeriesComponent t) { //3
          if (t == null)
            return this;
          if (this.series == null)
            this.series = new ArrayList<SeriesComponent>();
          this.series.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #series}, creating it if it does not already exist
         */
        public SeriesComponent getSeriesFirstRep() { 
          if (getSeries().isEmpty()) {
            addSeries();
          }
          return getSeries().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uid", "oid", "Study instance UID of the SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("imagingStudy", "Reference(ImagingStudy)", "Reference to the Imaging Study in FHIR form.", 0, java.lang.Integer.MAX_VALUE, imagingStudy));
          childrenList.add(new Property("endpoint", "Reference(Endpoint)", "The network service providing access (e.g., query, view, or retrieval) for the study. See implementation notes for information about using DICOM endpoints. A study-level endpoint applies to each series in the study, unless overridden by a series-level endpoint with the same Endpoint.type.", 0, java.lang.Integer.MAX_VALUE, endpoint));
          childrenList.add(new Property("series", "", "Series identity and locating information of the DICOM SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, series));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case -814900911: /*imagingStudy*/ return this.imagingStudy == null ? new Base[0] : new Base[] {this.imagingStudy}; // Reference
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : this.endpoint.toArray(new Base[this.endpoint.size()]); // Reference
        case -905838985: /*series*/ return this.series == null ? new Base[0] : this.series.toArray(new Base[this.series.size()]); // SeriesComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          return value;
        case -814900911: // imagingStudy
          this.imagingStudy = castToReference(value); // Reference
          return value;
        case 1741102485: // endpoint
          this.getEndpoint().add(castToReference(value)); // Reference
          return value;
        case -905838985: // series
          this.getSeries().add((SeriesComponent) value); // SeriesComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("uid")) {
          this.uid = castToOid(value); // OidType
        } else if (name.equals("imagingStudy")) {
          this.imagingStudy = castToReference(value); // Reference
        } else if (name.equals("endpoint")) {
          this.getEndpoint().add(castToReference(value));
        } else if (name.equals("series")) {
          this.getSeries().add((SeriesComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792:  return getUidElement();
        case -814900911:  return getImagingStudy(); 
        case 1741102485:  return addEndpoint(); 
        case -905838985:  return addSeries(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return new String[] {"oid"};
        case -814900911: /*imagingStudy*/ return new String[] {"Reference"};
        case 1741102485: /*endpoint*/ return new String[] {"Reference"};
        case -905838985: /*series*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingManifest.uid");
        }
        else if (name.equals("imagingStudy")) {
          this.imagingStudy = new Reference();
          return this.imagingStudy;
        }
        else if (name.equals("endpoint")) {
          return addEndpoint();
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
        if (endpoint != null) {
          dst.endpoint = new ArrayList<Reference>();
          for (Reference i : endpoint)
            dst.endpoint.add(i.copy());
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
        return compareDeep(uid, o.uid, true) && compareDeep(imagingStudy, o.imagingStudy, true) && compareDeep(endpoint, o.endpoint, true)
           && compareDeep(series, o.series, true);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(uid, imagingStudy, endpoint
          , series);
      }

  public String fhirType() {
    return "ImagingManifest.study";

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
         * The network service providing access (e.g., query, view, or retrieval) for this series. See implementation notes for information about using DICOM endpoints. A series-level endpoint, if present, has precedence over a study-level endpoint with the same Endpoint.type.
         */
        @Child(name = "endpoint", type = {Endpoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Series access endpoint", formalDefinition="The network service providing access (e.g., query, view, or retrieval) for this series. See implementation notes for information about using DICOM endpoints. A series-level endpoint, if present, has precedence over a study-level endpoint with the same Endpoint.type." )
        protected List<Reference> endpoint;
        /**
         * The actual objects that are the target of the reference (The network service providing access (e.g., query, view, or retrieval) for this series. See implementation notes for information about using DICOM endpoints. A series-level endpoint, if present, has precedence over a study-level endpoint with the same Endpoint.type.)
         */
        protected List<Endpoint> endpointTarget;


        /**
         * Identity and locating information of the selected DICOM SOP instances.
         */
        @Child(name = "instance", type = {}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The selected instance", formalDefinition="Identity and locating information of the selected DICOM SOP instances." )
        protected List<InstanceComponent> instance;

        private static final long serialVersionUID = -1682136598L;

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
         * @return {@link #endpoint} (The network service providing access (e.g., query, view, or retrieval) for this series. See implementation notes for information about using DICOM endpoints. A series-level endpoint, if present, has precedence over a study-level endpoint with the same Endpoint.type.)
         */
        public List<Reference> getEndpoint() { 
          if (this.endpoint == null)
            this.endpoint = new ArrayList<Reference>();
          return this.endpoint;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SeriesComponent setEndpoint(List<Reference> theEndpoint) { 
          this.endpoint = theEndpoint;
          return this;
        }

        public boolean hasEndpoint() { 
          if (this.endpoint == null)
            return false;
          for (Reference item : this.endpoint)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addEndpoint() { //3
          Reference t = new Reference();
          if (this.endpoint == null)
            this.endpoint = new ArrayList<Reference>();
          this.endpoint.add(t);
          return t;
        }

        public SeriesComponent addEndpoint(Reference t) { //3
          if (t == null)
            return this;
          if (this.endpoint == null)
            this.endpoint = new ArrayList<Reference>();
          this.endpoint.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #endpoint}, creating it if it does not already exist
         */
        public Reference getEndpointFirstRep() { 
          if (getEndpoint().isEmpty()) {
            addEndpoint();
          }
          return getEndpoint().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Endpoint> getEndpointTarget() { 
          if (this.endpointTarget == null)
            this.endpointTarget = new ArrayList<Endpoint>();
          return this.endpointTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Endpoint addEndpointTarget() { 
          Endpoint r = new Endpoint();
          if (this.endpointTarget == null)
            this.endpointTarget = new ArrayList<Endpoint>();
          this.endpointTarget.add(r);
          return r;
        }

        /**
         * @return {@link #instance} (Identity and locating information of the selected DICOM SOP instances.)
         */
        public List<InstanceComponent> getInstance() { 
          if (this.instance == null)
            this.instance = new ArrayList<InstanceComponent>();
          return this.instance;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SeriesComponent setInstance(List<InstanceComponent> theInstance) { 
          this.instance = theInstance;
          return this;
        }

        public boolean hasInstance() { 
          if (this.instance == null)
            return false;
          for (InstanceComponent item : this.instance)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public InstanceComponent addInstance() { //3
          InstanceComponent t = new InstanceComponent();
          if (this.instance == null)
            this.instance = new ArrayList<InstanceComponent>();
          this.instance.add(t);
          return t;
        }

        public SeriesComponent addInstance(InstanceComponent t) { //3
          if (t == null)
            return this;
          if (this.instance == null)
            this.instance = new ArrayList<InstanceComponent>();
          this.instance.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #instance}, creating it if it does not already exist
         */
        public InstanceComponent getInstanceFirstRep() { 
          if (getInstance().isEmpty()) {
            addInstance();
          }
          return getInstance().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uid", "oid", "Series instance UID of the SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("endpoint", "Reference(Endpoint)", "The network service providing access (e.g., query, view, or retrieval) for this series. See implementation notes for information about using DICOM endpoints. A series-level endpoint, if present, has precedence over a study-level endpoint with the same Endpoint.type.", 0, java.lang.Integer.MAX_VALUE, endpoint));
          childrenList.add(new Property("instance", "", "Identity and locating information of the selected DICOM SOP instances.", 0, java.lang.Integer.MAX_VALUE, instance));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : this.endpoint.toArray(new Base[this.endpoint.size()]); // Reference
        case 555127957: /*instance*/ return this.instance == null ? new Base[0] : this.instance.toArray(new Base[this.instance.size()]); // InstanceComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          return value;
        case 1741102485: // endpoint
          this.getEndpoint().add(castToReference(value)); // Reference
          return value;
        case 555127957: // instance
          this.getInstance().add((InstanceComponent) value); // InstanceComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("uid")) {
          this.uid = castToOid(value); // OidType
        } else if (name.equals("endpoint")) {
          this.getEndpoint().add(castToReference(value));
        } else if (name.equals("instance")) {
          this.getInstance().add((InstanceComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792:  return getUidElement();
        case 1741102485:  return addEndpoint(); 
        case 555127957:  return addInstance(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 115792: /*uid*/ return new String[] {"oid"};
        case 1741102485: /*endpoint*/ return new String[] {"Reference"};
        case 555127957: /*instance*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingManifest.uid");
        }
        else if (name.equals("endpoint")) {
          return addEndpoint();
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
        if (endpoint != null) {
          dst.endpoint = new ArrayList<Reference>();
          for (Reference i : endpoint)
            dst.endpoint.add(i.copy());
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
        return compareDeep(uid, o.uid, true) && compareDeep(endpoint, o.endpoint, true) && compareDeep(instance, o.instance, true)
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(uid, endpoint, instance
          );
      }

  public String fhirType() {
    return "ImagingManifest.study.series";

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

        private static final long serialVersionUID = -885780004L;

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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sopClass", "oid", "SOP class UID of the selected instance.", 0, java.lang.Integer.MAX_VALUE, sopClass));
          childrenList.add(new Property("uid", "oid", "SOP Instance UID of the selected instance.", 0, java.lang.Integer.MAX_VALUE, uid));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1560041540: /*sopClass*/ return this.sopClass == null ? new Base[0] : new Base[] {this.sopClass}; // OidType
        case 115792: /*uid*/ return this.uid == null ? new Base[0] : new Base[] {this.uid}; // OidType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1560041540: // sopClass
          this.sopClass = castToOid(value); // OidType
          return value;
        case 115792: // uid
          this.uid = castToOid(value); // OidType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sopClass")) {
          this.sopClass = castToOid(value); // OidType
        } else if (name.equals("uid")) {
          this.uid = castToOid(value); // OidType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1560041540:  return getSopClassElement();
        case 115792:  return getUidElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1560041540: /*sopClass*/ return new String[] {"oid"};
        case 115792: /*uid*/ return new String[] {"oid"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sopClass")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingManifest.sopClass");
        }
        else if (name.equals("uid")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingManifest.uid");
        }
        else
          return super.addChild(name);
      }

      public InstanceComponent copy() {
        InstanceComponent dst = new InstanceComponent();
        copyValues(dst);
        dst.sopClass = sopClass == null ? null : sopClass.copy();
        dst.uid = uid == null ? null : uid.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof InstanceComponent))
          return false;
        InstanceComponent o = (InstanceComponent) other;
        return compareDeep(sopClass, o.sopClass, true) && compareDeep(uid, o.uid, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof InstanceComponent))
          return false;
        InstanceComponent o = (InstanceComponent) other;
        return compareValues(sopClass, o.sopClass, true) && compareValues(uid, o.uid, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sopClass, uid);
      }

  public String fhirType() {
    return "ImagingManifest.study.series.instance";

  }

  }

    /**
     * Unique identifier of the DICOM Key Object Selection (KOS) that this resource represents.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="SOP Instance UID", formalDefinition="Unique identifier of the DICOM Key Object Selection (KOS) that this resource represents." )
    protected Identifier identifier;

    /**
     * A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingManifest.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient of the selected objects", formalDefinition="A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingManifest." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingManifest.)
     */
    protected Patient patientTarget;

    /**
     * Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).
     */
    @Child(name = "authoringTime", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time when the selection of instances was made", formalDefinition="Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image)." )
    protected DateTimeType authoringTime;

    /**
     * Author of ImagingManifest. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.
     */
    @Child(name = "author", type = {Practitioner.class, Device.class, Organization.class, Patient.class, RelatedPerson.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Author (human or machine)", formalDefinition="Author of ImagingManifest. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Author of ImagingManifest. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    protected Resource authorTarget;

    /**
     * Free text narrative description of the ImagingManifest.  
The value may be derived from the DICOM Standard Part 16, CID-7010 descriptions (e.g. Best in Set, Complete Study Content). Note that those values cover the wide range of uses of the DICOM Key Object Selection object, several of which are not supported by ImagingManifest. Specifically, there is no expected behavior associated with descriptions that suggest referenced images be removed or not used.
     */
    @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Description text", formalDefinition="Free text narrative description of the ImagingManifest.  \nThe value may be derived from the DICOM Standard Part 16, CID-7010 descriptions (e.g. Best in Set, Complete Study Content). Note that those values cover the wide range of uses of the DICOM Key Object Selection object, several of which are not supported by ImagingManifest. Specifically, there is no expected behavior associated with descriptions that suggest referenced images be removed or not used." )
    protected StringType description;

    /**
     * Study identity and locating information of the DICOM SOP instances in the selection.
     */
    @Child(name = "study", type = {}, order=5, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Study identity of the selected instances", formalDefinition="Study identity and locating information of the DICOM SOP instances in the selection." )
    protected List<StudyComponent> study;

    private static final long serialVersionUID = 245941978L;

  /**
   * Constructor
   */
    public ImagingManifest() {
      super();
    }

  /**
   * Constructor
   */
    public ImagingManifest(Reference patient) {
      super();
      this.patient = patient;
    }

    /**
     * @return {@link #identifier} (Unique identifier of the DICOM Key Object Selection (KOS) that this resource represents.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingManifest.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Unique identifier of the DICOM Key Object Selection (KOS) that this resource represents.)
     */
    public ImagingManifest setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #patient} (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingManifest.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingManifest.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingManifest.)
     */
    public ImagingManifest setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingManifest.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingManifest.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingManifest.)
     */
    public ImagingManifest setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #authoringTime} (Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).). This is the underlying object with id, value and extensions. The accessor "getAuthoringTime" gives direct access to the value
     */
    public DateTimeType getAuthoringTimeElement() { 
      if (this.authoringTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingManifest.authoringTime");
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
    public ImagingManifest setAuthoringTimeElement(DateTimeType value) { 
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
    public ImagingManifest setAuthoringTime(Date value) { 
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
     * @return {@link #author} (Author of ImagingManifest. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingManifest.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Author of ImagingManifest. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public ImagingManifest setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Author of ImagingManifest. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Author of ImagingManifest. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public ImagingManifest setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #description} (Free text narrative description of the ImagingManifest.  
The value may be derived from the DICOM Standard Part 16, CID-7010 descriptions (e.g. Best in Set, Complete Study Content). Note that those values cover the wide range of uses of the DICOM Key Object Selection object, several of which are not supported by ImagingManifest. Specifically, there is no expected behavior associated with descriptions that suggest referenced images be removed or not used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingManifest.description");
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
     * @param value {@link #description} (Free text narrative description of the ImagingManifest.  
The value may be derived from the DICOM Standard Part 16, CID-7010 descriptions (e.g. Best in Set, Complete Study Content). Note that those values cover the wide range of uses of the DICOM Key Object Selection object, several of which are not supported by ImagingManifest. Specifically, there is no expected behavior associated with descriptions that suggest referenced images be removed or not used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImagingManifest setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Free text narrative description of the ImagingManifest.  
The value may be derived from the DICOM Standard Part 16, CID-7010 descriptions (e.g. Best in Set, Complete Study Content). Note that those values cover the wide range of uses of the DICOM Key Object Selection object, several of which are not supported by ImagingManifest. Specifically, there is no expected behavior associated with descriptions that suggest referenced images be removed or not used.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Free text narrative description of the ImagingManifest.  
The value may be derived from the DICOM Standard Part 16, CID-7010 descriptions (e.g. Best in Set, Complete Study Content). Note that those values cover the wide range of uses of the DICOM Key Object Selection object, several of which are not supported by ImagingManifest. Specifically, there is no expected behavior associated with descriptions that suggest referenced images be removed or not used.
     */
    public ImagingManifest setDescription(String value) { 
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImagingManifest setStudy(List<StudyComponent> theStudy) { 
      this.study = theStudy;
      return this;
    }

    public boolean hasStudy() { 
      if (this.study == null)
        return false;
      for (StudyComponent item : this.study)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public StudyComponent addStudy() { //3
      StudyComponent t = new StudyComponent();
      if (this.study == null)
        this.study = new ArrayList<StudyComponent>();
      this.study.add(t);
      return t;
    }

    public ImagingManifest addStudy(StudyComponent t) { //3
      if (t == null)
        return this;
      if (this.study == null)
        this.study = new ArrayList<StudyComponent>();
      this.study.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #study}, creating it if it does not already exist
     */
    public StudyComponent getStudyFirstRep() { 
      if (getStudy().isEmpty()) {
        addStudy();
      }
      return getStudy().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique identifier of the DICOM Key Object Selection (KOS) that this resource represents.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "A patient resource reference which is the patient subject of all DICOM SOP Instances in this ImagingManifest.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("authoringTime", "dateTime", "Date and time when the selection of the referenced instances were made. It is (typically) different from the creation date of the selection resource, and from dates associated with the referenced instances (e.g. capture time of the referenced image).", 0, java.lang.Integer.MAX_VALUE, authoringTime));
        childrenList.add(new Property("author", "Reference(Practitioner|Device|Organization|Patient|RelatedPerson)", "Author of ImagingManifest. It can be a human author or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attach in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("description", "string", "Free text narrative description of the ImagingManifest.  \nThe value may be derived from the DICOM Standard Part 16, CID-7010 descriptions (e.g. Best in Set, Complete Study Content). Note that those values cover the wide range of uses of the DICOM Key Object Selection object, several of which are not supported by ImagingManifest. Specifically, there is no expected behavior associated with descriptions that suggest referenced images be removed or not used.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("study", "", "Study identity and locating information of the DICOM SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, study));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -1724532252: /*authoringTime*/ return this.authoringTime == null ? new Base[0] : new Base[] {this.authoringTime}; // DateTimeType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 109776329: /*study*/ return this.study == null ? new Base[0] : this.study.toArray(new Base[this.study.size()]); // StudyComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case -1724532252: // authoringTime
          this.authoringTime = castToDateTime(value); // DateTimeType
          return value;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 109776329: // study
          this.getStudy().add((StudyComponent) value); // StudyComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("authoringTime")) {
          this.authoringTime = castToDateTime(value); // DateTimeType
        } else if (name.equals("author")) {
          this.author = castToReference(value); // Reference
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("study")) {
          this.getStudy().add((StudyComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -791418107:  return getPatient(); 
        case -1724532252:  return getAuthoringTimeElement();
        case -1406328437:  return getAuthor(); 
        case -1724546052:  return getDescriptionElement();
        case 109776329:  return addStudy(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case -1724532252: /*authoringTime*/ return new String[] {"dateTime"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 109776329: /*study*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("authoringTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingManifest.authoringTime");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImagingManifest.description");
        }
        else if (name.equals("study")) {
          return addStudy();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImagingManifest";

  }

      public ImagingManifest copy() {
        ImagingManifest dst = new ImagingManifest();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.authoringTime = authoringTime == null ? null : authoringTime.copy();
        dst.author = author == null ? null : author.copy();
        dst.description = description == null ? null : description.copy();
        if (study != null) {
          dst.study = new ArrayList<StudyComponent>();
          for (StudyComponent i : study)
            dst.study.add(i.copy());
        };
        return dst;
      }

      protected ImagingManifest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImagingManifest))
          return false;
        ImagingManifest o = (ImagingManifest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(patient, o.patient, true) && compareDeep(authoringTime, o.authoringTime, true)
           && compareDeep(author, o.author, true) && compareDeep(description, o.description, true) && compareDeep(study, o.study, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImagingManifest))
          return false;
        ImagingManifest o = (ImagingManifest) other;
        return compareValues(authoringTime, o.authoringTime, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, patient, authoringTime
          , author, description, study);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImagingManifest;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>UID of the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingManifest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ImagingManifest.identifier", description="UID of the ImagingManifest (or a DICOM Key Object Selection which it represents)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>UID of the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImagingManifest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>endpoint</b>
   * <p>
   * Description: <b>The endpoint for the study or series</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingManifest.study.endpoint, ImagingManifest.study.series.endpoint</b><br>
   * </p>
   */
  @SearchParamDefinition(name="endpoint", path="ImagingManifest.study.endpoint | ImagingManifest.study.series.endpoint", description="The endpoint for the study or series", type="reference", target={Endpoint.class } )
  public static final String SP_ENDPOINT = "endpoint";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>endpoint</b>
   * <p>
   * Description: <b>The endpoint for the study or series</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingManifest.study.endpoint, ImagingManifest.study.series.endpoint</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENDPOINT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENDPOINT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingManifest:endpoint</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENDPOINT = new ca.uhn.fhir.model.api.Include("ImagingManifest:endpoint").toLocked();

 /**
   * Search parameter: <b>authoring-time</b>
   * <p>
   * Description: <b>Time of the ImagingManifest (or a DICOM Key Object Selection which it represents) authoring</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImagingManifest.authoringTime</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authoring-time", path="ImagingManifest.authoringTime", description="Time of the ImagingManifest (or a DICOM Key Object Selection which it represents) authoring", type="date" )
  public static final String SP_AUTHORING_TIME = "authoring-time";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authoring-time</b>
   * <p>
   * Description: <b>Time of the ImagingManifest (or a DICOM Key Object Selection which it represents) authoring</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImagingManifest.authoringTime</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHORING_TIME = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHORING_TIME);

 /**
   * Search parameter: <b>selected-study</b>
   * <p>
   * Description: <b>Study selected in the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingManifest.study.uid</b><br>
   * </p>
   */
  @SearchParamDefinition(name="selected-study", path="ImagingManifest.study.uid", description="Study selected in the ImagingManifest (or a DICOM Key Object Selection which it represents)", type="uri" )
  public static final String SP_SELECTED_STUDY = "selected-study";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>selected-study</b>
   * <p>
   * Description: <b>Study selected in the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImagingManifest.study.uid</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam SELECTED_STUDY = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_SELECTED_STUDY);

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>Author of the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingManifest.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="ImagingManifest.author", description="Author of the ImagingManifest (or a DICOM Key Object Selection which it represents)", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>Author of the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingManifest.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingManifest:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("ImagingManifest:author").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Subject of the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingManifest.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ImagingManifest.patient", description="Subject of the ImagingManifest (or a DICOM Key Object Selection which it represents)", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Subject of the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingManifest.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingManifest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ImagingManifest:patient").toLocked();

 /**
   * Search parameter: <b>imaging-study</b>
   * <p>
   * Description: <b>ImagingStudy resource selected in the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingManifest.study.imagingStudy</b><br>
   * </p>
   */
  @SearchParamDefinition(name="imaging-study", path="ImagingManifest.study.imagingStudy", description="ImagingStudy resource selected in the ImagingManifest (or a DICOM Key Object Selection which it represents)", type="reference", target={ImagingStudy.class } )
  public static final String SP_IMAGING_STUDY = "imaging-study";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>imaging-study</b>
   * <p>
   * Description: <b>ImagingStudy resource selected in the ImagingManifest (or a DICOM Key Object Selection which it represents)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImagingManifest.study.imagingStudy</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam IMAGING_STUDY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_IMAGING_STUDY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImagingManifest:imaging-study</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_IMAGING_STUDY = new ca.uhn.fhir.model.api.Include("ImagingManifest:imaging-study").toLocked();


}

