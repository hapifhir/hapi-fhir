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

// Generated on Tue, Dec 2, 2014 21:09+1100 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A set of DICOM SOP Instances of a patient, selected for some application purpose, e.g., quality assurance, teaching, conference, consulting, etc.  Objects selected can be from different studies, but must be of the same patient.
 */
@ResourceDef(name="ImagingObjectSelection", profile="http://hl7.org/fhir/Profile/ImagingObjectSelection")
public class ImagingObjectSelection extends DomainResource {

    @Block()
    public static class StudyComponent extends BackboneElement {
        /**
         * Study instance uid of the SOP instances in the selection.
         */
        @Child(name="uid", type={OidType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Study instance uid", formalDefinition="Study instance uid of the SOP instances in the selection." )
        protected OidType uid;

        /**
         * The DICOM Application Entity Title where the study can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the study, not only those in the selection.
         */
        @Child(name="retrieveAETitle", type={IdType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="AE Title where may be retrieved", formalDefinition="The DICOM Application Entity Title where the study can be retrieved.\nNote that this AE Title is provided to retrieve all SOP instances of the study, not only those in the selection." )
        protected IdType retrieveAETitle;

        /**
         * WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.
         */
        @Child(name="retrieveUrl", type={UriType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Retrieve URL", formalDefinition="WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection." )
        protected UriType retrieveUrl;

        /**
         * Series indetity and locating information of the DICOM SOP instances in the selection.
         */
        @Child(name="series", type={}, order=4, min=1, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Series identity of the selected instances", formalDefinition="Series indetity and locating information of the DICOM SOP instances in the selection." )
        protected List<SeriesComponent> series;

        private static final long serialVersionUID = -1655229615L;

      public StudyComponent() {
        super();
      }

      public StudyComponent(OidType uid) {
        super();
        this.uid = uid;
      }

        /**
         * @return {@link #uid} (Study instance uid of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyComponent.uid");
            else if (Configuration.doAutoCreate())
              this.uid = new OidType();
          return this.uid;
        }

        public boolean hasUidElement() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        public boolean hasUid() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        /**
         * @param value {@link #uid} (Study instance uid of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public StudyComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return Study instance uid of the SOP instances in the selection.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value Study instance uid of the SOP instances in the selection.
         */
        public StudyComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #retrieveAETitle} (The DICOM Application Entity Title where the study can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the study, not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getRetrieveAETitle" gives direct access to the value
         */
        public IdType getRetrieveAETitleElement() { 
          if (this.retrieveAETitle == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyComponent.retrieveAETitle");
            else if (Configuration.doAutoCreate())
              this.retrieveAETitle = new IdType();
          return this.retrieveAETitle;
        }

        public boolean hasRetrieveAETitleElement() { 
          return this.retrieveAETitle != null && !this.retrieveAETitle.isEmpty();
        }

        public boolean hasRetrieveAETitle() { 
          return this.retrieveAETitle != null && !this.retrieveAETitle.isEmpty();
        }

        /**
         * @param value {@link #retrieveAETitle} (The DICOM Application Entity Title where the study can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the study, not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getRetrieveAETitle" gives direct access to the value
         */
        public StudyComponent setRetrieveAETitleElement(IdType value) { 
          this.retrieveAETitle = value;
          return this;
        }

        /**
         * @return The DICOM Application Entity Title where the study can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the study, not only those in the selection.
         */
        public String getRetrieveAETitle() { 
          return this.retrieveAETitle == null ? null : this.retrieveAETitle.getValue();
        }

        /**
         * @param value The DICOM Application Entity Title where the study can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the study, not only those in the selection.
         */
        public StudyComponent setRetrieveAETitle(String value) { 
          if (Utilities.noString(value))
            this.retrieveAETitle = null;
          else {
            if (this.retrieveAETitle == null)
              this.retrieveAETitle = new IdType();
            this.retrieveAETitle.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #retrieveUrl} (WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getRetrieveUrl" gives direct access to the value
         */
        public UriType getRetrieveUrlElement() { 
          if (this.retrieveUrl == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StudyComponent.retrieveUrl");
            else if (Configuration.doAutoCreate())
              this.retrieveUrl = new UriType();
          return this.retrieveUrl;
        }

        public boolean hasRetrieveUrlElement() { 
          return this.retrieveUrl != null && !this.retrieveUrl.isEmpty();
        }

        public boolean hasRetrieveUrl() { 
          return this.retrieveUrl != null && !this.retrieveUrl.isEmpty();
        }

        /**
         * @param value {@link #retrieveUrl} (WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getRetrieveUrl" gives direct access to the value
         */
        public StudyComponent setRetrieveUrlElement(UriType value) { 
          this.retrieveUrl = value;
          return this;
        }

        /**
         * @return WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.
         */
        public String getRetrieveUrl() { 
          return this.retrieveUrl == null ? null : this.retrieveUrl.getValue();
        }

        /**
         * @param value WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.
         */
        public StudyComponent setRetrieveUrl(String value) { 
          if (Utilities.noString(value))
            this.retrieveUrl = null;
          else {
            if (this.retrieveUrl == null)
              this.retrieveUrl = new UriType();
            this.retrieveUrl.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #series} (Series indetity and locating information of the DICOM SOP instances in the selection.)
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
         * @return {@link #series} (Series indetity and locating information of the DICOM SOP instances in the selection.)
         */
    // syntactic sugar
        public SeriesComponent addSeries() { //3
          SeriesComponent t = new SeriesComponent();
          if (this.series == null)
            this.series = new ArrayList<SeriesComponent>();
          this.series.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uid", "oid", "Study instance uid of the SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("retrieveAETitle", "id", "The DICOM Application Entity Title where the study can be retrieved.\nNote that this AE Title is provided to retrieve all SOP instances of the study, not only those in the selection.", 0, java.lang.Integer.MAX_VALUE, retrieveAETitle));
          childrenList.add(new Property("retrieveUrl", "uri", "WADO-RS URL to retrieve the study. Note that this URL retrieves all SOP instances of the study, not only those in the selection.", 0, java.lang.Integer.MAX_VALUE, retrieveUrl));
          childrenList.add(new Property("series", "", "Series indetity and locating information of the DICOM SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, series));
        }

      public StudyComponent copy() {
        StudyComponent dst = new StudyComponent();
        copyValues(dst);
        dst.uid = uid == null ? null : uid.copy();
        dst.retrieveAETitle = retrieveAETitle == null ? null : retrieveAETitle.copy();
        dst.retrieveUrl = retrieveUrl == null ? null : retrieveUrl.copy();
        if (series != null) {
          dst.series = new ArrayList<SeriesComponent>();
          for (SeriesComponent i : series)
            dst.series.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (uid == null || uid.isEmpty()) && (retrieveAETitle == null || retrieveAETitle.isEmpty())
           && (retrieveUrl == null || retrieveUrl.isEmpty()) && (series == null || series.isEmpty())
          ;
      }

  }

    @Block()
    public static class SeriesComponent extends BackboneElement {
        /**
         * Series instance uid of the SOP instances in the selection.
         */
        @Child(name="uid", type={OidType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Series instance uid", formalDefinition="Series instance uid of the SOP instances in the selection." )
        protected OidType uid;

        /**
         * The DICOM Application Entity Title where the series can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the series not only those in the selection.
         */
        @Child(name="retrieveAETitle", type={IdType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="AE Title where may be retrieved", formalDefinition="The DICOM Application Entity Title where the series can be retrieved.\nNote that this AE Title is provided to retrieve all SOP instances of the series not only those in the selection." )
        protected IdType retrieveAETitle;

        /**
         * WADO-RS URL to retrieve the series Note that this URL retrieves all SOP instances of the series not only those in the selection.
         */
        @Child(name="retrieveUrl", type={UriType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Retrieve URL", formalDefinition="WADO-RS URL to retrieve the series Note that this URL retrieves all SOP instances of the series not only those in the selection." )
        protected UriType retrieveUrl;

        /**
         * Identity and locating information of the selected DICOM SOP instances.
         */
        @Child(name="instance", type={}, order=4, min=1, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="The selected instance", formalDefinition="Identity and locating information of the selected DICOM SOP instances." )
        protected List<InstanceComponent> instance;

        private static final long serialVersionUID = -156906991L;

      public SeriesComponent() {
        super();
      }

      public SeriesComponent(OidType uid) {
        super();
        this.uid = uid;
      }

        /**
         * @return {@link #uid} (Series instance uid of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SeriesComponent.uid");
            else if (Configuration.doAutoCreate())
              this.uid = new OidType();
          return this.uid;
        }

        public boolean hasUidElement() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        public boolean hasUid() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        /**
         * @param value {@link #uid} (Series instance uid of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public SeriesComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return Series instance uid of the SOP instances in the selection.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value Series instance uid of the SOP instances in the selection.
         */
        public SeriesComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #retrieveAETitle} (The DICOM Application Entity Title where the series can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the series not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getRetrieveAETitle" gives direct access to the value
         */
        public IdType getRetrieveAETitleElement() { 
          if (this.retrieveAETitle == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SeriesComponent.retrieveAETitle");
            else if (Configuration.doAutoCreate())
              this.retrieveAETitle = new IdType();
          return this.retrieveAETitle;
        }

        public boolean hasRetrieveAETitleElement() { 
          return this.retrieveAETitle != null && !this.retrieveAETitle.isEmpty();
        }

        public boolean hasRetrieveAETitle() { 
          return this.retrieveAETitle != null && !this.retrieveAETitle.isEmpty();
        }

        /**
         * @param value {@link #retrieveAETitle} (The DICOM Application Entity Title where the series can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the series not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getRetrieveAETitle" gives direct access to the value
         */
        public SeriesComponent setRetrieveAETitleElement(IdType value) { 
          this.retrieveAETitle = value;
          return this;
        }

        /**
         * @return The DICOM Application Entity Title where the series can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the series not only those in the selection.
         */
        public String getRetrieveAETitle() { 
          return this.retrieveAETitle == null ? null : this.retrieveAETitle.getValue();
        }

        /**
         * @param value The DICOM Application Entity Title where the series can be retrieved.
Note that this AE Title is provided to retrieve all SOP instances of the series not only those in the selection.
         */
        public SeriesComponent setRetrieveAETitle(String value) { 
          if (Utilities.noString(value))
            this.retrieveAETitle = null;
          else {
            if (this.retrieveAETitle == null)
              this.retrieveAETitle = new IdType();
            this.retrieveAETitle.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #retrieveUrl} (WADO-RS URL to retrieve the series Note that this URL retrieves all SOP instances of the series not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getRetrieveUrl" gives direct access to the value
         */
        public UriType getRetrieveUrlElement() { 
          if (this.retrieveUrl == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SeriesComponent.retrieveUrl");
            else if (Configuration.doAutoCreate())
              this.retrieveUrl = new UriType();
          return this.retrieveUrl;
        }

        public boolean hasRetrieveUrlElement() { 
          return this.retrieveUrl != null && !this.retrieveUrl.isEmpty();
        }

        public boolean hasRetrieveUrl() { 
          return this.retrieveUrl != null && !this.retrieveUrl.isEmpty();
        }

        /**
         * @param value {@link #retrieveUrl} (WADO-RS URL to retrieve the series Note that this URL retrieves all SOP instances of the series not only those in the selection.). This is the underlying object with id, value and extensions. The accessor "getRetrieveUrl" gives direct access to the value
         */
        public SeriesComponent setRetrieveUrlElement(UriType value) { 
          this.retrieveUrl = value;
          return this;
        }

        /**
         * @return WADO-RS URL to retrieve the series Note that this URL retrieves all SOP instances of the series not only those in the selection.
         */
        public String getRetrieveUrl() { 
          return this.retrieveUrl == null ? null : this.retrieveUrl.getValue();
        }

        /**
         * @param value WADO-RS URL to retrieve the series Note that this URL retrieves all SOP instances of the series not only those in the selection.
         */
        public SeriesComponent setRetrieveUrl(String value) { 
          if (Utilities.noString(value))
            this.retrieveUrl = null;
          else {
            if (this.retrieveUrl == null)
              this.retrieveUrl = new UriType();
            this.retrieveUrl.setValue(value);
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uid", "oid", "Series instance uid of the SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("retrieveAETitle", "id", "The DICOM Application Entity Title where the series can be retrieved.\nNote that this AE Title is provided to retrieve all SOP instances of the series not only those in the selection.", 0, java.lang.Integer.MAX_VALUE, retrieveAETitle));
          childrenList.add(new Property("retrieveUrl", "uri", "WADO-RS URL to retrieve the series Note that this URL retrieves all SOP instances of the series not only those in the selection.", 0, java.lang.Integer.MAX_VALUE, retrieveUrl));
          childrenList.add(new Property("instance", "", "Identity and locating information of the selected DICOM SOP instances.", 0, java.lang.Integer.MAX_VALUE, instance));
        }

      public SeriesComponent copy() {
        SeriesComponent dst = new SeriesComponent();
        copyValues(dst);
        dst.uid = uid == null ? null : uid.copy();
        dst.retrieveAETitle = retrieveAETitle == null ? null : retrieveAETitle.copy();
        dst.retrieveUrl = retrieveUrl == null ? null : retrieveUrl.copy();
        if (instance != null) {
          dst.instance = new ArrayList<InstanceComponent>();
          for (InstanceComponent i : instance)
            dst.instance.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (uid == null || uid.isEmpty()) && (retrieveAETitle == null || retrieveAETitle.isEmpty())
           && (retrieveUrl == null || retrieveUrl.isEmpty()) && (instance == null || instance.isEmpty())
          ;
      }

  }

    @Block()
    public static class InstanceComponent extends BackboneElement {
        /**
         * SOP class uid of the selected instance.
         */
        @Child(name="sopClass", type={OidType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="SOP class uid of instance", formalDefinition="SOP class uid of the selected instance." )
        protected OidType sopClass;

        /**
         * SOP Instance uid of the selected instance.
         */
        @Child(name="uid", type={OidType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Uid of the selected instance", formalDefinition="SOP Instance uid of the selected instance." )
        protected OidType uid;

        /**
         * The DICOM Application Entity Title where the DICOM SOP instance can be retrieved.
         */
        @Child(name="retrieveAETitle", type={IdType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="AE Title where may be retrieved", formalDefinition="The DICOM Application Entity Title where the DICOM SOP instance can be retrieved." )
        protected IdType retrieveAETitle;

        /**
         * WADO-RS URL to retrieve the DICOM SOP Instance.
         */
        @Child(name="retrieveUrl", type={UriType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Retrieve URL", formalDefinition="WADO-RS URL to retrieve the DICOM SOP Instance." )
        protected UriType retrieveUrl;

        private static final long serialVersionUID = 1148429294L;

      public InstanceComponent() {
        super();
      }

      public InstanceComponent(OidType sopClass, OidType uid) {
        super();
        this.sopClass = sopClass;
        this.uid = uid;
      }

        /**
         * @return {@link #sopClass} (SOP class uid of the selected instance.). This is the underlying object with id, value and extensions. The accessor "getSopClass" gives direct access to the value
         */
        public OidType getSopClassElement() { 
          if (this.sopClass == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InstanceComponent.sopClass");
            else if (Configuration.doAutoCreate())
              this.sopClass = new OidType();
          return this.sopClass;
        }

        public boolean hasSopClassElement() { 
          return this.sopClass != null && !this.sopClass.isEmpty();
        }

        public boolean hasSopClass() { 
          return this.sopClass != null && !this.sopClass.isEmpty();
        }

        /**
         * @param value {@link #sopClass} (SOP class uid of the selected instance.). This is the underlying object with id, value and extensions. The accessor "getSopClass" gives direct access to the value
         */
        public InstanceComponent setSopClassElement(OidType value) { 
          this.sopClass = value;
          return this;
        }

        /**
         * @return SOP class uid of the selected instance.
         */
        public String getSopClass() { 
          return this.sopClass == null ? null : this.sopClass.getValue();
        }

        /**
         * @param value SOP class uid of the selected instance.
         */
        public InstanceComponent setSopClass(String value) { 
            if (this.sopClass == null)
              this.sopClass = new OidType();
            this.sopClass.setValue(value);
          return this;
        }

        /**
         * @return {@link #uid} (SOP Instance uid of the selected instance.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public OidType getUidElement() { 
          if (this.uid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InstanceComponent.uid");
            else if (Configuration.doAutoCreate())
              this.uid = new OidType();
          return this.uid;
        }

        public boolean hasUidElement() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        public boolean hasUid() { 
          return this.uid != null && !this.uid.isEmpty();
        }

        /**
         * @param value {@link #uid} (SOP Instance uid of the selected instance.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
         */
        public InstanceComponent setUidElement(OidType value) { 
          this.uid = value;
          return this;
        }

        /**
         * @return SOP Instance uid of the selected instance.
         */
        public String getUid() { 
          return this.uid == null ? null : this.uid.getValue();
        }

        /**
         * @param value SOP Instance uid of the selected instance.
         */
        public InstanceComponent setUid(String value) { 
            if (this.uid == null)
              this.uid = new OidType();
            this.uid.setValue(value);
          return this;
        }

        /**
         * @return {@link #retrieveAETitle} (The DICOM Application Entity Title where the DICOM SOP instance can be retrieved.). This is the underlying object with id, value and extensions. The accessor "getRetrieveAETitle" gives direct access to the value
         */
        public IdType getRetrieveAETitleElement() { 
          if (this.retrieveAETitle == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InstanceComponent.retrieveAETitle");
            else if (Configuration.doAutoCreate())
              this.retrieveAETitle = new IdType();
          return this.retrieveAETitle;
        }

        public boolean hasRetrieveAETitleElement() { 
          return this.retrieveAETitle != null && !this.retrieveAETitle.isEmpty();
        }

        public boolean hasRetrieveAETitle() { 
          return this.retrieveAETitle != null && !this.retrieveAETitle.isEmpty();
        }

        /**
         * @param value {@link #retrieveAETitle} (The DICOM Application Entity Title where the DICOM SOP instance can be retrieved.). This is the underlying object with id, value and extensions. The accessor "getRetrieveAETitle" gives direct access to the value
         */
        public InstanceComponent setRetrieveAETitleElement(IdType value) { 
          this.retrieveAETitle = value;
          return this;
        }

        /**
         * @return The DICOM Application Entity Title where the DICOM SOP instance can be retrieved.
         */
        public String getRetrieveAETitle() { 
          return this.retrieveAETitle == null ? null : this.retrieveAETitle.getValue();
        }

        /**
         * @param value The DICOM Application Entity Title where the DICOM SOP instance can be retrieved.
         */
        public InstanceComponent setRetrieveAETitle(String value) { 
          if (Utilities.noString(value))
            this.retrieveAETitle = null;
          else {
            if (this.retrieveAETitle == null)
              this.retrieveAETitle = new IdType();
            this.retrieveAETitle.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #retrieveUrl} (WADO-RS URL to retrieve the DICOM SOP Instance.). This is the underlying object with id, value and extensions. The accessor "getRetrieveUrl" gives direct access to the value
         */
        public UriType getRetrieveUrlElement() { 
          if (this.retrieveUrl == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InstanceComponent.retrieveUrl");
            else if (Configuration.doAutoCreate())
              this.retrieveUrl = new UriType();
          return this.retrieveUrl;
        }

        public boolean hasRetrieveUrlElement() { 
          return this.retrieveUrl != null && !this.retrieveUrl.isEmpty();
        }

        public boolean hasRetrieveUrl() { 
          return this.retrieveUrl != null && !this.retrieveUrl.isEmpty();
        }

        /**
         * @param value {@link #retrieveUrl} (WADO-RS URL to retrieve the DICOM SOP Instance.). This is the underlying object with id, value and extensions. The accessor "getRetrieveUrl" gives direct access to the value
         */
        public InstanceComponent setRetrieveUrlElement(UriType value) { 
          this.retrieveUrl = value;
          return this;
        }

        /**
         * @return WADO-RS URL to retrieve the DICOM SOP Instance.
         */
        public String getRetrieveUrl() { 
          return this.retrieveUrl == null ? null : this.retrieveUrl.getValue();
        }

        /**
         * @param value WADO-RS URL to retrieve the DICOM SOP Instance.
         */
        public InstanceComponent setRetrieveUrl(String value) { 
          if (Utilities.noString(value))
            this.retrieveUrl = null;
          else {
            if (this.retrieveUrl == null)
              this.retrieveUrl = new UriType();
            this.retrieveUrl.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sopClass", "oid", "SOP class uid of the selected instance.", 0, java.lang.Integer.MAX_VALUE, sopClass));
          childrenList.add(new Property("uid", "oid", "SOP Instance uid of the selected instance.", 0, java.lang.Integer.MAX_VALUE, uid));
          childrenList.add(new Property("retrieveAETitle", "id", "The DICOM Application Entity Title where the DICOM SOP instance can be retrieved.", 0, java.lang.Integer.MAX_VALUE, retrieveAETitle));
          childrenList.add(new Property("retrieveUrl", "uri", "WADO-RS URL to retrieve the DICOM SOP Instance.", 0, java.lang.Integer.MAX_VALUE, retrieveUrl));
        }

      public InstanceComponent copy() {
        InstanceComponent dst = new InstanceComponent();
        copyValues(dst);
        dst.sopClass = sopClass == null ? null : sopClass.copy();
        dst.uid = uid == null ? null : uid.copy();
        dst.retrieveAETitle = retrieveAETitle == null ? null : retrieveAETitle.copy();
        dst.retrieveUrl = retrieveUrl == null ? null : retrieveUrl.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sopClass == null || sopClass.isEmpty()) && (uid == null || uid.isEmpty())
           && (retrieveAETitle == null || retrieveAETitle.isEmpty()) && (retrieveUrl == null || retrieveUrl.isEmpty())
          ;
      }

  }

    /**
     * Instance UID of the DICOM KOS SOP Instances represenetd in this resource.
     */
    @Child(name="uid", type={OidType.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="Instance UID", formalDefinition="Instance UID of the DICOM KOS SOP Instances represenetd in this resource." )
    protected OidType uid;

    /**
     * A patient resource reference which is the patient subject of all DICOM SOP Instances in this key object selection.
     */
    @Child(name="patient", type={Patient.class}, order=0, min=1, max=1)
    @Description(shortDefinition="Patient of the selected objects", formalDefinition="A patient resource reference which is the patient subject of all DICOM SOP Instances in this key object selection." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A patient resource reference which is the patient subject of all DICOM SOP Instances in this key object selection.)
     */
    protected Patient patientTarget;

    /**
     * The reason for, or significance of, the selection of objects referenced in the resource.
     */
    @Child(name="title", type={CodeableConcept.class}, order=1, min=1, max=1)
    @Description(shortDefinition="Reason for selection", formalDefinition="The reason for, or significance of, the selection of objects referenced in the resource." )
    protected CodeableConcept title;

    /**
     * Text description of the DICOM SOP instances selected in the key object selection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.
     */
    @Child(name="description", type={StringType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Description text", formalDefinition="Text description of the DICOM SOP instances selected in the key object selection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection." )
    protected StringType description;

    /**
     * Author of key object selection. It can be a human authtor or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attached in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.
     */
    @Child(name="author", type={Practitioner.class, Device.class, Organization.class, Patient.class, RelatedPerson.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Author (human or machine)", formalDefinition="Author of key object selection. It can be a human authtor or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attached in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Author of key object selection. It can be a human authtor or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attached in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    protected Resource authorTarget;

    /**
     * Date and time when the key object selection was authored. Note that this is the date and time the DICOM SOP instances in the selection were selected (selection decision making). It is different from the creation date and time of the selection resource.
     */
    @Child(name="authoringTime", type={DateTimeType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Authoring time of the selection", formalDefinition="Date and time when the key object selection was authored. Note that this is the date and time the DICOM SOP instances in the selection were selected (selection decision making). It is different from the creation date and time of the selection resource." )
    protected DateTimeType authoringTime;

    /**
     * Study identity and locating information of the DICOM SOP instances in the selection.
     */
    @Child(name="study", type={}, order=5, min=1, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Study identity of the selected instances", formalDefinition="Study identity and locating information of the DICOM SOP instances in the selection." )
    protected List<StudyComponent> study;

    private static final long serialVersionUID = -1961832713L;

    public ImagingObjectSelection() {
      super();
    }

    public ImagingObjectSelection(OidType uid, Reference patient, CodeableConcept title) {
      super();
      this.uid = uid;
      this.patient = patient;
      this.title = title;
    }

    /**
     * @return {@link #uid} (Instance UID of the DICOM KOS SOP Instances represenetd in this resource.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public OidType getUidElement() { 
      if (this.uid == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.uid");
        else if (Configuration.doAutoCreate())
          this.uid = new OidType();
      return this.uid;
    }

    public boolean hasUidElement() { 
      return this.uid != null && !this.uid.isEmpty();
    }

    public boolean hasUid() { 
      return this.uid != null && !this.uid.isEmpty();
    }

    /**
     * @param value {@link #uid} (Instance UID of the DICOM KOS SOP Instances represenetd in this resource.). This is the underlying object with id, value and extensions. The accessor "getUid" gives direct access to the value
     */
    public ImagingObjectSelection setUidElement(OidType value) { 
      this.uid = value;
      return this;
    }

    /**
     * @return Instance UID of the DICOM KOS SOP Instances represenetd in this resource.
     */
    public String getUid() { 
      return this.uid == null ? null : this.uid.getValue();
    }

    /**
     * @param value Instance UID of the DICOM KOS SOP Instances represenetd in this resource.
     */
    public ImagingObjectSelection setUid(String value) { 
        if (this.uid == null)
          this.uid = new OidType();
        this.uid.setValue(value);
      return this;
    }

    /**
     * @return {@link #patient} (A patient resource reference which is the patient subject of all DICOM SOP Instances in this key object selection.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference();
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (A patient resource reference which is the patient subject of all DICOM SOP Instances in this key object selection.)
     */
    public ImagingObjectSelection setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A patient resource reference which is the patient subject of all DICOM SOP Instances in this key object selection.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient();
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A patient resource reference which is the patient subject of all DICOM SOP Instances in this key object selection.)
     */
    public ImagingObjectSelection setPatientTarget(Patient value) { 
      this.patientTarget = value;
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
          this.title = new CodeableConcept();
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
     * @return {@link #description} (Text description of the DICOM SOP instances selected in the key object selection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType();
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (Text description of the DICOM SOP instances selected in the key object selection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImagingObjectSelection setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Text description of the DICOM SOP instances selected in the key object selection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Text description of the DICOM SOP instances selected in the key object selection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.
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
     * @return {@link #author} (Author of key object selection. It can be a human authtor or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attached in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference();
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Author of key object selection. It can be a human authtor or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attached in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public ImagingObjectSelection setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Author of key object selection. It can be a human authtor or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attached in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Author of key object selection. It can be a human authtor or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attached in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.)
     */
    public ImagingObjectSelection setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #authoringTime} (Date and time when the key object selection was authored. Note that this is the date and time the DICOM SOP instances in the selection were selected (selection decision making). It is different from the creation date and time of the selection resource.). This is the underlying object with id, value and extensions. The accessor "getAuthoringTime" gives direct access to the value
     */
    public DateTimeType getAuthoringTimeElement() { 
      if (this.authoringTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImagingObjectSelection.authoringTime");
        else if (Configuration.doAutoCreate())
          this.authoringTime = new DateTimeType();
      return this.authoringTime;
    }

    public boolean hasAuthoringTimeElement() { 
      return this.authoringTime != null && !this.authoringTime.isEmpty();
    }

    public boolean hasAuthoringTime() { 
      return this.authoringTime != null && !this.authoringTime.isEmpty();
    }

    /**
     * @param value {@link #authoringTime} (Date and time when the key object selection was authored. Note that this is the date and time the DICOM SOP instances in the selection were selected (selection decision making). It is different from the creation date and time of the selection resource.). This is the underlying object with id, value and extensions. The accessor "getAuthoringTime" gives direct access to the value
     */
    public ImagingObjectSelection setAuthoringTimeElement(DateTimeType value) { 
      this.authoringTime = value;
      return this;
    }

    /**
     * @return Date and time when the key object selection was authored. Note that this is the date and time the DICOM SOP instances in the selection were selected (selection decision making). It is different from the creation date and time of the selection resource.
     */
    public DateAndTime getAuthoringTime() { 
      return this.authoringTime == null ? null : this.authoringTime.getValue();
    }

    /**
     * @param value Date and time when the key object selection was authored. Note that this is the date and time the DICOM SOP instances in the selection were selected (selection decision making). It is different from the creation date and time of the selection resource.
     */
    public ImagingObjectSelection setAuthoringTime(DateAndTime value) { 
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("uid", "oid", "Instance UID of the DICOM KOS SOP Instances represenetd in this resource.", 0, java.lang.Integer.MAX_VALUE, uid));
        childrenList.add(new Property("patient", "Reference(Patient)", "A patient resource reference which is the patient subject of all DICOM SOP Instances in this key object selection.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("title", "CodeableConcept", "The reason for, or significance of, the selection of objects referenced in the resource.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("description", "string", "Text description of the DICOM SOP instances selected in the key object selection. This should be aligned with the content of the title element, and can provide further explanation of the SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("author", "Reference(Practitioner|Device|Organization|Patient|RelatedPerson)", "Author of key object selection. It can be a human authtor or a device which made the decision of the SOP instances selected. For example, a radiologist selected a set of imaging SOP instances to attached in a diagnostic report, and a CAD application may author a selection to describe SOP instances it used to generate a detection conclusion.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("authoringTime", "dateTime", "Date and time when the key object selection was authored. Note that this is the date and time the DICOM SOP instances in the selection were selected (selection decision making). It is different from the creation date and time of the selection resource.", 0, java.lang.Integer.MAX_VALUE, authoringTime));
        childrenList.add(new Property("study", "", "Study identity and locating information of the DICOM SOP instances in the selection.", 0, java.lang.Integer.MAX_VALUE, study));
      }

      public ImagingObjectSelection copy() {
        ImagingObjectSelection dst = new ImagingObjectSelection();
        copyValues(dst);
        dst.uid = uid == null ? null : uid.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.title = title == null ? null : title.copy();
        dst.description = description == null ? null : description.copy();
        dst.author = author == null ? null : author.copy();
        dst.authoringTime = authoringTime == null ? null : authoringTime.copy();
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

      public boolean isEmpty() {
        return super.isEmpty() && (uid == null || uid.isEmpty()) && (patient == null || patient.isEmpty())
           && (title == null || title.isEmpty()) && (description == null || description.isEmpty()) && (author == null || author.isEmpty())
           && (authoringTime == null || authoringTime.isEmpty()) && (study == null || study.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImagingObjectSelection;
   }

  @SearchParamDefinition(name="selected-study", path="ImagingObjectSelection.study.uid", description="Study selected in key DICOM object selection", type="token" )
  public static final String SP_SELECTEDSTUDY = "selected-study";
  @SearchParamDefinition(name="author", path="ImagingObjectSelection.author", description="Author of key DICOM object selection", type="reference" )
  public static final String SP_AUTHOR = "author";
  @SearchParamDefinition(name="title", path="ImagingObjectSelection.title", description="Title of key DICOM object selection", type="string" )
  public static final String SP_TITLE = "title";
  @SearchParamDefinition(name="patient", path="ImagingObjectSelection.patient", description="Subject of key DICOM object selection", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="authoring-time", path="ImagingObjectSelection.authoringTime", description="Time of key DICOM object selection authoring", type="date" )
  public static final String SP_AUTHORINGTIME = "authoring-time";
  @SearchParamDefinition(name="identifier", path="ImagingObjectSelection.uid", description="UID of key DICOM object selection", type="token" )
  public static final String SP_IDENTIFIER = "identifier";

}

