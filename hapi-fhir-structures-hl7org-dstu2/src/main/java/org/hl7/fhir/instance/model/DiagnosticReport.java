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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretation, and formatted representation of diagnostic reports.
 */
@ResourceDef(name="DiagnosticReport", profile="http://hl7.org/fhir/Profile/DiagnosticReport")
public class DiagnosticReport extends DomainResource {

    public enum DiagnosticReportStatus {
        /**
         * The existence of the report is registered, but there is nothing yet available.
         */
        REGISTERED, 
        /**
         * This is a partial (e.g. initial, interim or preliminary) report: data in the report may be incomplete or unverified.
         */
        PARTIAL, 
        /**
         * The report is complete and verified by an authorized person.
         */
        FINAL, 
        /**
         * The report has been modified subsequent to being Final, and is complete and verified by an authorized person.
         */
        CORRECTED, 
        /**
         * The report has been modified subsequent to being Final, and is complete and verified by an authorized person, and data has been changed.
         */
        AMENDED, 
        /**
         * The report has been modified subsequent to being Final, and is complete and verified by an authorized person. New content has been added, but existing content hasn't changed.
         */
        APPENDED, 
        /**
         * The report is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
         */
        CANCELLED, 
        /**
         * The report has been withdrawn following previous Final release.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DiagnosticReportStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return REGISTERED;
        if ("partial".equals(codeString))
          return PARTIAL;
        if ("final".equals(codeString))
          return FINAL;
        if ("corrected".equals(codeString))
          return CORRECTED;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("appended".equals(codeString))
          return APPENDED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown DiagnosticReportStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REGISTERED: return "registered";
            case PARTIAL: return "partial";
            case FINAL: return "final";
            case CORRECTED: return "corrected";
            case AMENDED: return "amended";
            case APPENDED: return "appended";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REGISTERED: return "";
            case PARTIAL: return "";
            case FINAL: return "";
            case CORRECTED: return "";
            case AMENDED: return "";
            case APPENDED: return "";
            case CANCELLED: return "";
            case ENTEREDINERROR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REGISTERED: return "The existence of the report is registered, but there is nothing yet available.";
            case PARTIAL: return "This is a partial (e.g. initial, interim or preliminary) report: data in the report may be incomplete or unverified.";
            case FINAL: return "The report is complete and verified by an authorized person.";
            case CORRECTED: return "The report has been modified subsequent to being Final, and is complete and verified by an authorized person.";
            case AMENDED: return "The report has been modified subsequent to being Final, and is complete and verified by an authorized person, and data has been changed.";
            case APPENDED: return "The report has been modified subsequent to being Final, and is complete and verified by an authorized person. New content has been added, but existing content hasn't changed.";
            case CANCELLED: return "The report is unavailable because the measurement was not started or not completed (also sometimes called 'aborted').";
            case ENTEREDINERROR: return "The report has been withdrawn following previous Final release.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REGISTERED: return "registered";
            case PARTIAL: return "partial";
            case FINAL: return "final";
            case CORRECTED: return "corrected";
            case AMENDED: return "amended";
            case APPENDED: return "appended";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
    }

  public static class DiagnosticReportStatusEnumFactory implements EnumFactory<DiagnosticReportStatus> {
    public DiagnosticReportStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return DiagnosticReportStatus.REGISTERED;
        if ("partial".equals(codeString))
          return DiagnosticReportStatus.PARTIAL;
        if ("final".equals(codeString))
          return DiagnosticReportStatus.FINAL;
        if ("corrected".equals(codeString))
          return DiagnosticReportStatus.CORRECTED;
        if ("amended".equals(codeString))
          return DiagnosticReportStatus.AMENDED;
        if ("appended".equals(codeString))
          return DiagnosticReportStatus.APPENDED;
        if ("cancelled".equals(codeString))
          return DiagnosticReportStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return DiagnosticReportStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown DiagnosticReportStatus code '"+codeString+"'");
        }
    public String toCode(DiagnosticReportStatus code) {
      if (code == DiagnosticReportStatus.REGISTERED)
        return "registered";
      if (code == DiagnosticReportStatus.PARTIAL)
        return "partial";
      if (code == DiagnosticReportStatus.FINAL)
        return "final";
      if (code == DiagnosticReportStatus.CORRECTED)
        return "corrected";
      if (code == DiagnosticReportStatus.AMENDED)
        return "amended";
      if (code == DiagnosticReportStatus.APPENDED)
        return "appended";
      if (code == DiagnosticReportStatus.CANCELLED)
        return "cancelled";
      if (code == DiagnosticReportStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    }

    @Block()
    public static class DiagnosticReportImageComponent extends BackboneElement {
        /**
         * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.
         */
        @Child(name="comment", type={StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Comment about the image (e.g. explanation)", formalDefinition="A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features." )
        protected StringType comment;

        /**
         * Reference to the image source.
         */
        @Child(name="link", type={Media.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Reference to the image source", formalDefinition="Reference to the image source." )
        protected Reference link;

        /**
         * The actual object that is the target of the reference (Reference to the image source.)
         */
        protected Media linkTarget;

        private static final long serialVersionUID = 935791940L;

      public DiagnosticReportImageComponent() {
        super();
      }

      public DiagnosticReportImageComponent(Reference link) {
        super();
        this.link = link;
      }

        /**
         * @return {@link #comment} (A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public StringType getCommentElement() { 
          if (this.comment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticReportImageComponent.comment");
            else if (Configuration.doAutoCreate())
              this.comment = new StringType(); // bb
          return this.comment;
        }

        public boolean hasCommentElement() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        public boolean hasComment() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        /**
         * @param value {@link #comment} (A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public DiagnosticReportImageComponent setCommentElement(StringType value) { 
          this.comment = value;
          return this;
        }

        /**
         * @return A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.
         */
        public String getComment() { 
          return this.comment == null ? null : this.comment.getValue();
        }

        /**
         * @param value A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.
         */
        public DiagnosticReportImageComponent setComment(String value) { 
          if (Utilities.noString(value))
            this.comment = null;
          else {
            if (this.comment == null)
              this.comment = new StringType();
            this.comment.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #link} (Reference to the image source.)
         */
        public Reference getLink() { 
          if (this.link == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticReportImageComponent.link");
            else if (Configuration.doAutoCreate())
              this.link = new Reference(); // cc
          return this.link;
        }

        public boolean hasLink() { 
          return this.link != null && !this.link.isEmpty();
        }

        /**
         * @param value {@link #link} (Reference to the image source.)
         */
        public DiagnosticReportImageComponent setLink(Reference value) { 
          this.link = value;
          return this;
        }

        /**
         * @return {@link #link} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the image source.)
         */
        public Media getLinkTarget() { 
          if (this.linkTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticReportImageComponent.link");
            else if (Configuration.doAutoCreate())
              this.linkTarget = new Media(); // aa
          return this.linkTarget;
        }

        /**
         * @param value {@link #link} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the image source.)
         */
        public DiagnosticReportImageComponent setLinkTarget(Media value) { 
          this.linkTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("comment", "string", "A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.", 0, java.lang.Integer.MAX_VALUE, comment));
          childrenList.add(new Property("link", "Reference(Media)", "Reference to the image source.", 0, java.lang.Integer.MAX_VALUE, link));
        }

      public DiagnosticReportImageComponent copy() {
        DiagnosticReportImageComponent dst = new DiagnosticReportImageComponent();
        copyValues(dst);
        dst.comment = comment == null ? null : comment.copy();
        dst.link = link == null ? null : link.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DiagnosticReportImageComponent))
          return false;
        DiagnosticReportImageComponent o = (DiagnosticReportImageComponent) other;
        return compareDeep(comment, o.comment, true) && compareDeep(link, o.link, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DiagnosticReportImageComponent))
          return false;
        DiagnosticReportImageComponent o = (DiagnosticReportImageComponent) other;
        return compareValues(comment, o.comment, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (comment == null || comment.isEmpty()) && (link == null || link.isEmpty())
          ;
      }

  }

    /**
     * A code or name that describes this diagnostic report.
     */
    @Child(name = "name", type = {CodeableConcept.class}, order = 0, min = 1, max = 1)
    @Description(shortDefinition="Name/Code for this diagnostic report", formalDefinition="A code or name that describes this diagnostic report." )
    protected CodeableConcept name;

    /**
     * The status of the diagnostic report as a whole.
     */
    @Child(name = "status", type = {CodeType.class}, order = 1, min = 1, max = 1)
    @Description(shortDefinition="registered | partial | final | corrected +", formalDefinition="The status of the diagnostic report as a whole." )
    protected Enumeration<DiagnosticReportStatus> status;

    /**
     * The date and/or time that this version of the report was released from the source diagnostic service.
     */
    @Child(name = "issued", type = {DateTimeType.class}, order = 2, min = 1, max = 1)
    @Description(shortDefinition="Date this version was released", formalDefinition="The date and/or time that this version of the report was released from the source diagnostic service." )
    protected DateTimeType issued;

    /**
     * The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Device.class, Location.class}, order = 3, min = 1, max = 1)
    @Description(shortDefinition="The subject of the report, usually, but not always, the patient", formalDefinition="The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.)
     */
    protected Resource subjectTarget;

    /**
     * The diagnostic service that is responsible for issuing the report.
     */
    @Child(name = "performer", type = {Practitioner.class, Organization.class}, order = 4, min = 1, max = 1)
    @Description(shortDefinition="Responsible Diagnostic Service", formalDefinition="The diagnostic service that is responsible for issuing the report." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (The diagnostic service that is responsible for issuing the report.)
     */
    protected Resource performerTarget;

    /**
     * The link to the health care event (encounter) when the order was made.
     */
    @Child(name = "encounter", type = {Encounter.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Health care event when test ordered", formalDefinition="The link to the health care event (encounter) when the order was made." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The link to the health care event (encounter) when the order was made.)
     */
    protected Encounter encounterTarget;

    /**
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="Id for external references to this report", formalDefinition="The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider." )
    protected Identifier identifier;

    /**
     * Details concerning a test requested.
     */
    @Child(name = "requestDetail", type = {DiagnosticOrder.class}, order = 7, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="What was requested", formalDefinition="Details concerning a test requested." )
    protected List<Reference> requestDetail;
    /**
     * The actual objects that are the target of the reference (Details concerning a test requested.)
     */
    protected List<DiagnosticOrder> requestDetailTarget;


    /**
     * The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI.
     */
    @Child(name = "serviceCategory", type = {CodeableConcept.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Biochemistry, Hematology etc.", formalDefinition="The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI." )
    protected CodeableConcept serviceCategory;

    /**
     * The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.
     */
    @Child(name = "diagnostic", type = {DateTimeType.class, Period.class}, order = 9, min = 1, max = 1)
    @Description(shortDefinition="Physiologically Relevant time/time-period for report", formalDefinition="The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself." )
    protected Type diagnostic;

    /**
     * Details about the specimens on which this diagnostic report is based.
     */
    @Child(name = "specimen", type = {Specimen.class}, order = 10, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Specimens this report is based on", formalDefinition="Details about the specimens on which this diagnostic report is based." )
    protected List<Reference> specimen;
    /**
     * The actual objects that are the target of the reference (Details about the specimens on which this diagnostic report is based.)
     */
    protected List<Specimen> specimenTarget;


    /**
     * Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. "atomic" results), or they can be grouping observations that include references to other members of the group (e.g. "panels").
     */
    @Child(name = "result", type = {Observation.class}, order = 11, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Observations - simple, or complex nested groups", formalDefinition="Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. 'atomic' results), or they can be grouping observations that include references to other members of the group (e.g. 'panels')." )
    protected List<Reference> result;
    /**
     * The actual objects that are the target of the reference (Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. "atomic" results), or they can be grouping observations that include references to other members of the group (e.g. "panels").)
     */
    protected List<Observation> resultTarget;


    /**
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.
     */
    @Child(name = "imagingStudy", type = {ImagingStudy.class}, order = 12, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Reference to full details of imaging associated with the diagnostic report", formalDefinition="One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images." )
    protected List<Reference> imagingStudy;
    /**
     * The actual objects that are the target of the reference (One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.)
     */
    protected List<ImagingStudy> imagingStudyTarget;


    /**
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).
     */
    @Child(name = "image", type = {}, order = 13, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Key images associated with this report", formalDefinition="A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)." )
    protected List<DiagnosticReportImageComponent> image;

    /**
     * Concise and clinically contextualized narrative interpretation of the diagnostic report.
     */
    @Child(name = "conclusion", type = {StringType.class}, order = 14, min = 0, max = 1)
    @Description(shortDefinition="Clinical Interpretation of test results", formalDefinition="Concise and clinically contextualized narrative interpretation of the diagnostic report." )
    protected StringType conclusion;

    /**
     * Codes for the conclusion.
     */
    @Child(name = "codedDiagnosis", type = {CodeableConcept.class}, order = 15, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Codes for the conclusion", formalDefinition="Codes for the conclusion." )
    protected List<CodeableConcept> codedDiagnosis;

    /**
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     */
    @Child(name = "presentedForm", type = {Attachment.class}, order = 16, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Entire Report as issued", formalDefinition="Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent." )
    protected List<Attachment> presentedForm;

    private static final long serialVersionUID = -1237974006L;

    public DiagnosticReport() {
      super();
    }

    public DiagnosticReport(CodeableConcept name, Enumeration<DiagnosticReportStatus> status, DateTimeType issued, Reference subject, Reference performer, Type diagnostic) {
      super();
      this.name = name;
      this.status = status;
      this.issued = issued;
      this.subject = subject;
      this.performer = performer;
      this.diagnostic = diagnostic;
    }

    /**
     * @return {@link #name} (A code or name that describes this diagnostic report.)
     */
    public CodeableConcept getName() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.name");
        else if (Configuration.doAutoCreate())
          this.name = new CodeableConcept(); // cc
      return this.name;
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A code or name that describes this diagnostic report.)
     */
    public DiagnosticReport setName(CodeableConcept value) { 
      this.name = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the diagnostic report as a whole.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DiagnosticReportStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DiagnosticReportStatus>(new DiagnosticReportStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the diagnostic report as a whole.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DiagnosticReport setStatusElement(Enumeration<DiagnosticReportStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the diagnostic report as a whole.
     */
    public DiagnosticReportStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the diagnostic report as a whole.
     */
    public DiagnosticReport setStatus(DiagnosticReportStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<DiagnosticReportStatus>(new DiagnosticReportStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #issued} (The date and/or time that this version of the report was released from the source diagnostic service.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public DateTimeType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new DateTimeType(); // bb
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (The date and/or time that this version of the report was released from the source diagnostic service.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public DiagnosticReport setIssuedElement(DateTimeType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return The date and/or time that this version of the report was released from the source diagnostic service.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value The date and/or time that this version of the report was released from the source diagnostic service.
     */
    public DiagnosticReport setIssued(Date value) { 
        if (this.issued == null)
          this.issued = new DateTimeType();
        this.issued.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.)
     */
    public DiagnosticReport setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.)
     */
    public DiagnosticReport setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #performer} (The diagnostic service that is responsible for issuing the report.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (The diagnostic service that is responsible for issuing the report.)
     */
    public DiagnosticReport setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The diagnostic service that is responsible for issuing the report.)
     */
    public Resource getPerformerTarget() { 
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The diagnostic service that is responsible for issuing the report.)
     */
    public DiagnosticReport setPerformerTarget(Resource value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The link to the health care event (encounter) when the order was made.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The link to the health care event (encounter) when the order was made.)
     */
    public DiagnosticReport setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The link to the health care event (encounter) when the order was made.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The link to the health care event (encounter) when the order was made.)
     */
    public DiagnosticReport setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #identifier} (The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider.)
     */
    public DiagnosticReport setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #requestDetail} (Details concerning a test requested.)
     */
    public List<Reference> getRequestDetail() { 
      if (this.requestDetail == null)
        this.requestDetail = new ArrayList<Reference>();
      return this.requestDetail;
    }

    public boolean hasRequestDetail() { 
      if (this.requestDetail == null)
        return false;
      for (Reference item : this.requestDetail)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #requestDetail} (Details concerning a test requested.)
     */
    // syntactic sugar
    public Reference addRequestDetail() { //3
      Reference t = new Reference();
      if (this.requestDetail == null)
        this.requestDetail = new ArrayList<Reference>();
      this.requestDetail.add(t);
      return t;
    }

    /**
     * @return {@link #requestDetail} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Details concerning a test requested.)
     */
    public List<DiagnosticOrder> getRequestDetailTarget() { 
      if (this.requestDetailTarget == null)
        this.requestDetailTarget = new ArrayList<DiagnosticOrder>();
      return this.requestDetailTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #requestDetail} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Details concerning a test requested.)
     */
    public DiagnosticOrder addRequestDetailTarget() { 
      DiagnosticOrder r = new DiagnosticOrder();
      if (this.requestDetailTarget == null)
        this.requestDetailTarget = new ArrayList<DiagnosticOrder>();
      this.requestDetailTarget.add(r);
      return r;
    }

    /**
     * @return {@link #serviceCategory} (The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI.)
     */
    public CodeableConcept getServiceCategory() { 
      if (this.serviceCategory == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.serviceCategory");
        else if (Configuration.doAutoCreate())
          this.serviceCategory = new CodeableConcept(); // cc
      return this.serviceCategory;
    }

    public boolean hasServiceCategory() { 
      return this.serviceCategory != null && !this.serviceCategory.isEmpty();
    }

    /**
     * @param value {@link #serviceCategory} (The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI.)
     */
    public DiagnosticReport setServiceCategory(CodeableConcept value) { 
      this.serviceCategory = value;
      return this;
    }

    /**
     * @return {@link #diagnostic} (The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Type getDiagnostic() { 
      return this.diagnostic;
    }

    /**
     * @return {@link #diagnostic} (The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.)
     */
    public DateTimeType getDiagnosticDateTimeType() throws Exception { 
      if (!(this.diagnostic instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.diagnostic.getClass().getName()+" was encountered");
      return (DateTimeType) this.diagnostic;
    }

    /**
     * @return {@link #diagnostic} (The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Period getDiagnosticPeriod() throws Exception { 
      if (!(this.diagnostic instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.diagnostic.getClass().getName()+" was encountered");
      return (Period) this.diagnostic;
    }

    public boolean hasDiagnostic() { 
      return this.diagnostic != null && !this.diagnostic.isEmpty();
    }

    /**
     * @param value {@link #diagnostic} (The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.)
     */
    public DiagnosticReport setDiagnostic(Type value) { 
      this.diagnostic = value;
      return this;
    }

    /**
     * @return {@link #specimen} (Details about the specimens on which this diagnostic report is based.)
     */
    public List<Reference> getSpecimen() { 
      if (this.specimen == null)
        this.specimen = new ArrayList<Reference>();
      return this.specimen;
    }

    public boolean hasSpecimen() { 
      if (this.specimen == null)
        return false;
      for (Reference item : this.specimen)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #specimen} (Details about the specimens on which this diagnostic report is based.)
     */
    // syntactic sugar
    public Reference addSpecimen() { //3
      Reference t = new Reference();
      if (this.specimen == null)
        this.specimen = new ArrayList<Reference>();
      this.specimen.add(t);
      return t;
    }

    /**
     * @return {@link #specimen} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Details about the specimens on which this diagnostic report is based.)
     */
    public List<Specimen> getSpecimenTarget() { 
      if (this.specimenTarget == null)
        this.specimenTarget = new ArrayList<Specimen>();
      return this.specimenTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #specimen} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Details about the specimens on which this diagnostic report is based.)
     */
    public Specimen addSpecimenTarget() { 
      Specimen r = new Specimen();
      if (this.specimenTarget == null)
        this.specimenTarget = new ArrayList<Specimen>();
      this.specimenTarget.add(r);
      return r;
    }

    /**
     * @return {@link #result} (Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. "atomic" results), or they can be grouping observations that include references to other members of the group (e.g. "panels").)
     */
    public List<Reference> getResult() { 
      if (this.result == null)
        this.result = new ArrayList<Reference>();
      return this.result;
    }

    public boolean hasResult() { 
      if (this.result == null)
        return false;
      for (Reference item : this.result)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #result} (Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. "atomic" results), or they can be grouping observations that include references to other members of the group (e.g. "panels").)
     */
    // syntactic sugar
    public Reference addResult() { //3
      Reference t = new Reference();
      if (this.result == null)
        this.result = new ArrayList<Reference>();
      this.result.add(t);
      return t;
    }

    /**
     * @return {@link #result} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. "atomic" results), or they can be grouping observations that include references to other members of the group (e.g. "panels").)
     */
    public List<Observation> getResultTarget() { 
      if (this.resultTarget == null)
        this.resultTarget = new ArrayList<Observation>();
      return this.resultTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #result} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. "atomic" results), or they can be grouping observations that include references to other members of the group (e.g. "panels").)
     */
    public Observation addResultTarget() { 
      Observation r = new Observation();
      if (this.resultTarget == null)
        this.resultTarget = new ArrayList<Observation>();
      this.resultTarget.add(r);
      return r;
    }

    /**
     * @return {@link #imagingStudy} (One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.)
     */
    public List<Reference> getImagingStudy() { 
      if (this.imagingStudy == null)
        this.imagingStudy = new ArrayList<Reference>();
      return this.imagingStudy;
    }

    public boolean hasImagingStudy() { 
      if (this.imagingStudy == null)
        return false;
      for (Reference item : this.imagingStudy)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #imagingStudy} (One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.)
     */
    // syntactic sugar
    public Reference addImagingStudy() { //3
      Reference t = new Reference();
      if (this.imagingStudy == null)
        this.imagingStudy = new ArrayList<Reference>();
      this.imagingStudy.add(t);
      return t;
    }

    /**
     * @return {@link #imagingStudy} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.)
     */
    public List<ImagingStudy> getImagingStudyTarget() { 
      if (this.imagingStudyTarget == null)
        this.imagingStudyTarget = new ArrayList<ImagingStudy>();
      return this.imagingStudyTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #imagingStudy} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.)
     */
    public ImagingStudy addImagingStudyTarget() { 
      ImagingStudy r = new ImagingStudy();
      if (this.imagingStudyTarget == null)
        this.imagingStudyTarget = new ArrayList<ImagingStudy>();
      this.imagingStudyTarget.add(r);
      return r;
    }

    /**
     * @return {@link #image} (A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).)
     */
    public List<DiagnosticReportImageComponent> getImage() { 
      if (this.image == null)
        this.image = new ArrayList<DiagnosticReportImageComponent>();
      return this.image;
    }

    public boolean hasImage() { 
      if (this.image == null)
        return false;
      for (DiagnosticReportImageComponent item : this.image)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #image} (A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).)
     */
    // syntactic sugar
    public DiagnosticReportImageComponent addImage() { //3
      DiagnosticReportImageComponent t = new DiagnosticReportImageComponent();
      if (this.image == null)
        this.image = new ArrayList<DiagnosticReportImageComponent>();
      this.image.add(t);
      return t;
    }

    /**
     * @return {@link #conclusion} (Concise and clinically contextualized narrative interpretation of the diagnostic report.). This is the underlying object with id, value and extensions. The accessor "getConclusion" gives direct access to the value
     */
    public StringType getConclusionElement() { 
      if (this.conclusion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.conclusion");
        else if (Configuration.doAutoCreate())
          this.conclusion = new StringType(); // bb
      return this.conclusion;
    }

    public boolean hasConclusionElement() { 
      return this.conclusion != null && !this.conclusion.isEmpty();
    }

    public boolean hasConclusion() { 
      return this.conclusion != null && !this.conclusion.isEmpty();
    }

    /**
     * @param value {@link #conclusion} (Concise and clinically contextualized narrative interpretation of the diagnostic report.). This is the underlying object with id, value and extensions. The accessor "getConclusion" gives direct access to the value
     */
    public DiagnosticReport setConclusionElement(StringType value) { 
      this.conclusion = value;
      return this;
    }

    /**
     * @return Concise and clinically contextualized narrative interpretation of the diagnostic report.
     */
    public String getConclusion() { 
      return this.conclusion == null ? null : this.conclusion.getValue();
    }

    /**
     * @param value Concise and clinically contextualized narrative interpretation of the diagnostic report.
     */
    public DiagnosticReport setConclusion(String value) { 
      if (Utilities.noString(value))
        this.conclusion = null;
      else {
        if (this.conclusion == null)
          this.conclusion = new StringType();
        this.conclusion.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #codedDiagnosis} (Codes for the conclusion.)
     */
    public List<CodeableConcept> getCodedDiagnosis() { 
      if (this.codedDiagnosis == null)
        this.codedDiagnosis = new ArrayList<CodeableConcept>();
      return this.codedDiagnosis;
    }

    public boolean hasCodedDiagnosis() { 
      if (this.codedDiagnosis == null)
        return false;
      for (CodeableConcept item : this.codedDiagnosis)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #codedDiagnosis} (Codes for the conclusion.)
     */
    // syntactic sugar
    public CodeableConcept addCodedDiagnosis() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.codedDiagnosis == null)
        this.codedDiagnosis = new ArrayList<CodeableConcept>();
      this.codedDiagnosis.add(t);
      return t;
    }

    /**
     * @return {@link #presentedForm} (Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.)
     */
    public List<Attachment> getPresentedForm() { 
      if (this.presentedForm == null)
        this.presentedForm = new ArrayList<Attachment>();
      return this.presentedForm;
    }

    public boolean hasPresentedForm() { 
      if (this.presentedForm == null)
        return false;
      for (Attachment item : this.presentedForm)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #presentedForm} (Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.)
     */
    // syntactic sugar
    public Attachment addPresentedForm() { //3
      Attachment t = new Attachment();
      if (this.presentedForm == null)
        this.presentedForm = new ArrayList<Attachment>();
      this.presentedForm.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("name", "CodeableConcept", "A code or name that describes this diagnostic report.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the diagnostic report as a whole.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("issued", "dateTime", "The date and/or time that this version of the report was released from the source diagnostic service.", 0, java.lang.Integer.MAX_VALUE, issued));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Location)", "The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("performer", "Reference(Practitioner|Organization)", "The diagnostic service that is responsible for issuing the report.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The link to the health care event (encounter) when the order was made.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("identifier", "Identifier", "The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("requestDetail", "Reference(DiagnosticOrder)", "Details concerning a test requested.", 0, java.lang.Integer.MAX_VALUE, requestDetail));
        childrenList.add(new Property("serviceCategory", "CodeableConcept", "The section of the diagnostic service that performs the examination e.g. biochemistry, hematology, MRI.", 0, java.lang.Integer.MAX_VALUE, serviceCategory));
        childrenList.add(new Property("diagnostic[x]", "dateTime|Period", "The time or time-period the observed values are related to. This is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.", 0, java.lang.Integer.MAX_VALUE, diagnostic));
        childrenList.add(new Property("specimen", "Reference(Specimen)", "Details about the specimens on which this diagnostic report is based.", 0, java.lang.Integer.MAX_VALUE, specimen));
        childrenList.add(new Property("result", "Reference(Observation)", "Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. 'atomic' results), or they can be grouping observations that include references to other members of the group (e.g. 'panels').", 0, java.lang.Integer.MAX_VALUE, result));
        childrenList.add(new Property("imagingStudy", "Reference(ImagingStudy)", "One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.", 0, java.lang.Integer.MAX_VALUE, imagingStudy));
        childrenList.add(new Property("image", "", "A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).", 0, java.lang.Integer.MAX_VALUE, image));
        childrenList.add(new Property("conclusion", "string", "Concise and clinically contextualized narrative interpretation of the diagnostic report.", 0, java.lang.Integer.MAX_VALUE, conclusion));
        childrenList.add(new Property("codedDiagnosis", "CodeableConcept", "Codes for the conclusion.", 0, java.lang.Integer.MAX_VALUE, codedDiagnosis));
        childrenList.add(new Property("presentedForm", "Attachment", "Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.", 0, java.lang.Integer.MAX_VALUE, presentedForm));
      }

      public DiagnosticReport copy() {
        DiagnosticReport dst = new DiagnosticReport();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.performer = performer == null ? null : performer.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        if (requestDetail != null) {
          dst.requestDetail = new ArrayList<Reference>();
          for (Reference i : requestDetail)
            dst.requestDetail.add(i.copy());
        };
        dst.serviceCategory = serviceCategory == null ? null : serviceCategory.copy();
        dst.diagnostic = diagnostic == null ? null : diagnostic.copy();
        if (specimen != null) {
          dst.specimen = new ArrayList<Reference>();
          for (Reference i : specimen)
            dst.specimen.add(i.copy());
        };
        if (result != null) {
          dst.result = new ArrayList<Reference>();
          for (Reference i : result)
            dst.result.add(i.copy());
        };
        if (imagingStudy != null) {
          dst.imagingStudy = new ArrayList<Reference>();
          for (Reference i : imagingStudy)
            dst.imagingStudy.add(i.copy());
        };
        if (image != null) {
          dst.image = new ArrayList<DiagnosticReportImageComponent>();
          for (DiagnosticReportImageComponent i : image)
            dst.image.add(i.copy());
        };
        dst.conclusion = conclusion == null ? null : conclusion.copy();
        if (codedDiagnosis != null) {
          dst.codedDiagnosis = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : codedDiagnosis)
            dst.codedDiagnosis.add(i.copy());
        };
        if (presentedForm != null) {
          dst.presentedForm = new ArrayList<Attachment>();
          for (Attachment i : presentedForm)
            dst.presentedForm.add(i.copy());
        };
        return dst;
      }

      protected DiagnosticReport typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DiagnosticReport))
          return false;
        DiagnosticReport o = (DiagnosticReport) other;
        return compareDeep(name, o.name, true) && compareDeep(status, o.status, true) && compareDeep(issued, o.issued, true)
           && compareDeep(subject, o.subject, true) && compareDeep(performer, o.performer, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(identifier, o.identifier, true) && compareDeep(requestDetail, o.requestDetail, true)
           && compareDeep(serviceCategory, o.serviceCategory, true) && compareDeep(diagnostic, o.diagnostic, true)
           && compareDeep(specimen, o.specimen, true) && compareDeep(result, o.result, true) && compareDeep(imagingStudy, o.imagingStudy, true)
           && compareDeep(image, o.image, true) && compareDeep(conclusion, o.conclusion, true) && compareDeep(codedDiagnosis, o.codedDiagnosis, true)
           && compareDeep(presentedForm, o.presentedForm, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DiagnosticReport))
          return false;
        DiagnosticReport o = (DiagnosticReport) other;
        return compareValues(status, o.status, true) && compareValues(issued, o.issued, true) && compareValues(conclusion, o.conclusion, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (status == null || status.isEmpty())
           && (issued == null || issued.isEmpty()) && (subject == null || subject.isEmpty()) && (performer == null || performer.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (requestDetail == null || requestDetail.isEmpty()) && (serviceCategory == null || serviceCategory.isEmpty())
           && (diagnostic == null || diagnostic.isEmpty()) && (specimen == null || specimen.isEmpty())
           && (result == null || result.isEmpty()) && (imagingStudy == null || imagingStudy.isEmpty())
           && (image == null || image.isEmpty()) && (conclusion == null || conclusion.isEmpty()) && (codedDiagnosis == null || codedDiagnosis.isEmpty())
           && (presentedForm == null || presentedForm.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DiagnosticReport;
   }

  @SearchParamDefinition(name = "date", path = "DiagnosticReport.diagnostic[x]", description = "The clinically relevant time of the report", type = "date")
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name = "identifier", path = "DiagnosticReport.identifier", description = "An identifier for the report", type = "token")
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name = "image", path = "DiagnosticReport.image.link", description = "Reference to the image source", type = "reference")
  public static final String SP_IMAGE = "image";
  @SearchParamDefinition(name = "request", path = "DiagnosticReport.requestDetail", description = "What was requested", type = "reference")
  public static final String SP_REQUEST = "request";
  @SearchParamDefinition(name = "performer", path = "DiagnosticReport.performer", description = "Who was the source of the report (organization)", type = "reference")
  public static final String SP_PERFORMER = "performer";
  @SearchParamDefinition(name="subject", path="DiagnosticReport.subject", description="The subject of the report", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="diagnosis", path="DiagnosticReport.codedDiagnosis", description="A coded diagnosis on the report", type="token" )
  public static final String SP_DIAGNOSIS = "diagnosis";
  @SearchParamDefinition(name="encounter", path="DiagnosticReport.encounter", description="The Encounter when the order was made", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name = "result", path = "DiagnosticReport.result", description = "Link to an atomic result (observation resource)", type = "reference")
  public static final String SP_RESULT = "result";
  @SearchParamDefinition(name = "service", path = "DiagnosticReport.serviceCategory", description = "Which diagnostic discipline/department created the report", type = "token")
  public static final String SP_SERVICE = "service";
  @SearchParamDefinition(name="patient", path="DiagnosticReport.subject", description="The subject of the report if a patient", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="specimen", path="DiagnosticReport.specimen", description="The specimen details", type="reference" )
  public static final String SP_SPECIMEN = "specimen";
  @SearchParamDefinition(name="name", path="DiagnosticReport.name", description="The name of the report (e.g. the code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result)", type="token" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name = "issued", path = "DiagnosticReport.issued", description = "When the report was issued", type = "date")
  public static final String SP_ISSUED = "issued";
  @SearchParamDefinition(name = "status", path = "DiagnosticReport.status", description = "The status of the report", type = "token")
  public static final String SP_STATUS = "status";

}

