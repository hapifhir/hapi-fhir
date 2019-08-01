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
 * The findings and interpretation of diagnostic  tests performed on patients, groups of patients, devices, and locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic reports.
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
         * The report has been modified subsequent to being Final, and is complete and verified by an authorized person
         */
        CORRECTED, 
        /**
         * The report has been modified subsequent to being Final, and is complete and verified by an authorized person. New content has been added, but existing content hasn't changed.
         */
        APPENDED, 
        /**
         * The report is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
         */
        CANCELLED, 
        /**
         * The report has been withdrawn following a previous final release.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DiagnosticReportStatus fromCode(String codeString) throws FHIRException {
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
        if ("appended".equals(codeString))
          return APPENDED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown DiagnosticReportStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REGISTERED: return "registered";
            case PARTIAL: return "partial";
            case FINAL: return "final";
            case CORRECTED: return "corrected";
            case APPENDED: return "appended";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REGISTERED: return "http://hl7.org/fhir/diagnostic-report-status";
            case PARTIAL: return "http://hl7.org/fhir/diagnostic-report-status";
            case FINAL: return "http://hl7.org/fhir/diagnostic-report-status";
            case CORRECTED: return "http://hl7.org/fhir/diagnostic-report-status";
            case APPENDED: return "http://hl7.org/fhir/diagnostic-report-status";
            case CANCELLED: return "http://hl7.org/fhir/diagnostic-report-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/diagnostic-report-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REGISTERED: return "The existence of the report is registered, but there is nothing yet available.";
            case PARTIAL: return "This is a partial (e.g. initial, interim or preliminary) report: data in the report may be incomplete or unverified.";
            case FINAL: return "The report is complete and verified by an authorized person.";
            case CORRECTED: return "The report has been modified subsequent to being Final, and is complete and verified by an authorized person";
            case APPENDED: return "The report has been modified subsequent to being Final, and is complete and verified by an authorized person. New content has been added, but existing content hasn't changed.";
            case CANCELLED: return "The report is unavailable because the measurement was not started or not completed (also sometimes called \"aborted\").";
            case ENTEREDINERROR: return "The report has been withdrawn following a previous final release.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REGISTERED: return "Registered";
            case PARTIAL: return "Partial";
            case FINAL: return "Final";
            case CORRECTED: return "Corrected";
            case APPENDED: return "Appended";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
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
        if ("appended".equals(codeString))
          return DiagnosticReportStatus.APPENDED;
        if ("cancelled".equals(codeString))
          return DiagnosticReportStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return DiagnosticReportStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown DiagnosticReportStatus code '"+codeString+"'");
        }
        public Enumeration<DiagnosticReportStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("registered".equals(codeString))
          return new Enumeration<DiagnosticReportStatus>(this, DiagnosticReportStatus.REGISTERED);
        if ("partial".equals(codeString))
          return new Enumeration<DiagnosticReportStatus>(this, DiagnosticReportStatus.PARTIAL);
        if ("final".equals(codeString))
          return new Enumeration<DiagnosticReportStatus>(this, DiagnosticReportStatus.FINAL);
        if ("corrected".equals(codeString))
          return new Enumeration<DiagnosticReportStatus>(this, DiagnosticReportStatus.CORRECTED);
        if ("appended".equals(codeString))
          return new Enumeration<DiagnosticReportStatus>(this, DiagnosticReportStatus.APPENDED);
        if ("cancelled".equals(codeString))
          return new Enumeration<DiagnosticReportStatus>(this, DiagnosticReportStatus.CANCELLED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<DiagnosticReportStatus>(this, DiagnosticReportStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown DiagnosticReportStatus code '"+codeString+"'");
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
      if (code == DiagnosticReportStatus.APPENDED)
        return "appended";
      if (code == DiagnosticReportStatus.CANCELLED)
        return "cancelled";
      if (code == DiagnosticReportStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(DiagnosticReportStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DiagnosticReportImageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features.
         */
        @Child(name = "comment", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Comment about the image (e.g. explanation)", formalDefinition="A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw the viewer's attention to important features." )
        protected StringType comment;

        /**
         * Reference to the image source.
         */
        @Child(name = "link", type = {Media.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to the image source", formalDefinition="Reference to the image source." )
        protected Reference link;

        /**
         * The actual object that is the target of the reference (Reference to the image source.)
         */
        protected Media linkTarget;

        private static final long serialVersionUID = 935791940L;

    /**
     * Constructor
     */
      public DiagnosticReportImageComponent() {
        super();
      }

    /**
     * Constructor
     */
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        case 3321850: /*link*/ return this.link == null ? new Base[0] : new Base[] {this.link}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          break;
        case 3321850: // link
          this.link = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("comment"))
          this.comment = castToString(value); // StringType
        else if (name.equals("link"))
          this.link = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 950398559: throw new FHIRException("Cannot make property comment as it is not a complex type"); // StringType
        case 3321850:  return getLink(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type DiagnosticReport.comment");
        }
        else if (name.equals("link")) {
          this.link = new Reference();
          return this.link;
        }
        else
          return super.addChild(name);
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

  public String fhirType() {
    return "DiagnosticReport.image";

  }

  }

    /**
     * The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Id for external references to this report", formalDefinition="The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider." )
    protected List<Identifier> identifier;

    /**
     * The status of the diagnostic report as a whole.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="registered | partial | final | corrected | appended | cancelled | entered-in-error", formalDefinition="The status of the diagnostic report as a whole." )
    protected Enumeration<DiagnosticReportStatus> status;

    /**
     * A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Service category", formalDefinition="A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes." )
    protected CodeableConcept category;

    /**
     * A code or name that describes this diagnostic report.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name/Code for this diagnostic report", formalDefinition="A code or name that describes this diagnostic report." )
    protected CodeableConcept code;

    /**
     * The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Device.class, Location.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the report, usually, but not always, the patient", formalDefinition="The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.)
     */
    protected Resource subjectTarget;

    /**
     * The link to the health care event (encounter) when the order was made.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Health care event when test ordered", formalDefinition="The link to the health care event (encounter) when the order was made." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The link to the health care event (encounter) when the order was made.)
     */
    protected Encounter encounterTarget;

    /**
     * The time or time-period the observed values are related to. When the subject of the report is a patient, this is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.
     */
    @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Clinically Relevant time/time-period for report", formalDefinition="The time or time-period the observed values are related to. When the subject of the report is a patient, this is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself." )
    protected Type effective;

    /**
     * The date and time that this version of the report was released from the source diagnostic service.
     */
    @Child(name = "issued", type = {InstantType.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="DateTime this version was released", formalDefinition="The date and time that this version of the report was released from the source diagnostic service." )
    protected InstantType issued;

    /**
     * The diagnostic service that is responsible for issuing the report.
     */
    @Child(name = "performer", type = {Practitioner.class, Organization.class}, order=8, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible Diagnostic Service", formalDefinition="The diagnostic service that is responsible for issuing the report." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (The diagnostic service that is responsible for issuing the report.)
     */
    protected Resource performerTarget;

    /**
     * Details concerning a test or procedure requested.
     */
    @Child(name = "request", type = {DiagnosticOrder.class, ProcedureRequest.class, ReferralRequest.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What was requested", formalDefinition="Details concerning a test or procedure requested." )
    protected List<Reference> request;
    /**
     * The actual objects that are the target of the reference (Details concerning a test or procedure requested.)
     */
    protected List<Resource> requestTarget;


    /**
     * Details about the specimens on which this diagnostic report is based.
     */
    @Child(name = "specimen", type = {Specimen.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Specimens this report is based on", formalDefinition="Details about the specimens on which this diagnostic report is based." )
    protected List<Reference> specimen;
    /**
     * The actual objects that are the target of the reference (Details about the specimens on which this diagnostic report is based.)
     */
    protected List<Specimen> specimenTarget;


    /**
     * Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. "atomic" results), or they can be grouping observations that include references to other members of the group (e.g. "panels").
     */
    @Child(name = "result", type = {Observation.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Observations - simple, or complex nested groups", formalDefinition="Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \"atomic\" results), or they can be grouping observations that include references to other members of the group (e.g. \"panels\")." )
    protected List<Reference> result;
    /**
     * The actual objects that are the target of the reference (Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. "atomic" results), or they can be grouping observations that include references to other members of the group (e.g. "panels").)
     */
    protected List<Observation> resultTarget;


    /**
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.
     */
    @Child(name = "imagingStudy", type = {ImagingStudy.class, ImagingObjectSelection.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reference to full details of imaging associated with the diagnostic report", formalDefinition="One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images." )
    protected List<Reference> imagingStudy;
    /**
     * The actual objects that are the target of the reference (One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.)
     */
    protected List<Resource> imagingStudyTarget;


    /**
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).
     */
    @Child(name = "image", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Key images associated with this report", formalDefinition="A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest)." )
    protected List<DiagnosticReportImageComponent> image;

    /**
     * Concise and clinically contextualized narrative interpretation of the diagnostic report.
     */
    @Child(name = "conclusion", type = {StringType.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Clinical Interpretation of test results", formalDefinition="Concise and clinically contextualized narrative interpretation of the diagnostic report." )
    protected StringType conclusion;

    /**
     * Codes for the conclusion.
     */
    @Child(name = "codedDiagnosis", type = {CodeableConcept.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Codes for the conclusion", formalDefinition="Codes for the conclusion." )
    protected List<CodeableConcept> codedDiagnosis;

    /**
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.
     */
    @Child(name = "presentedForm", type = {Attachment.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Entire report as issued", formalDefinition="Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent." )
    protected List<Attachment> presentedForm;

    private static final long serialVersionUID = 920334551L;

  /**
   * Constructor
   */
    public DiagnosticReport() {
      super();
    }

  /**
   * Constructor
   */
    public DiagnosticReport(Enumeration<DiagnosticReportStatus> status, CodeableConcept code, Reference subject, Type effective, InstantType issued, Reference performer) {
      super();
      this.status = status;
      this.code = code;
      this.subject = subject;
      this.effective = effective;
      this.issued = issued;
      this.performer = performer;
    }

    /**
     * @return {@link #identifier} (The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider.)
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
     * @return {@link #identifier} (The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider.)
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
    public DiagnosticReport addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
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
     * @return {@link #category} (A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.)
     */
    public DiagnosticReport setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #code} (A code or name that describes this diagnostic report.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A code or name that describes this diagnostic report.)
     */
    public DiagnosticReport setCode(CodeableConcept value) { 
      this.code = value;
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
     * @return {@link #effective} (The time or time-period the observed values are related to. When the subject of the report is a patient, this is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Type getEffective() { 
      return this.effective;
    }

    /**
     * @return {@link #effective} (The time or time-period the observed values are related to. When the subject of the report is a patient, this is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.)
     */
    public DateTimeType getEffectiveDateTimeType() throws FHIRException { 
      if (!(this.effective instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (DateTimeType) this.effective;
    }

    public boolean hasEffectiveDateTimeType() { 
      return this.effective instanceof DateTimeType;
    }

    /**
     * @return {@link #effective} (The time or time-period the observed values are related to. When the subject of the report is a patient, this is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Period getEffectivePeriod() throws FHIRException { 
      if (!(this.effective instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (Period) this.effective;
    }

    public boolean hasEffectivePeriod() { 
      return this.effective instanceof Period;
    }

    public boolean hasEffective() { 
      return this.effective != null && !this.effective.isEmpty();
    }

    /**
     * @param value {@link #effective} (The time or time-period the observed values are related to. When the subject of the report is a patient, this is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.)
     */
    public DiagnosticReport setEffective(Type value) { 
      this.effective = value;
      return this;
    }

    /**
     * @return {@link #issued} (The date and time that this version of the report was released from the source diagnostic service.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public InstantType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticReport.issued");
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
     * @param value {@link #issued} (The date and time that this version of the report was released from the source diagnostic service.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public DiagnosticReport setIssuedElement(InstantType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return The date and time that this version of the report was released from the source diagnostic service.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value The date and time that this version of the report was released from the source diagnostic service.
     */
    public DiagnosticReport setIssued(Date value) { 
        if (this.issued == null)
          this.issued = new InstantType();
        this.issued.setValue(value);
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
     * @return {@link #request} (Details concerning a test or procedure requested.)
     */
    public List<Reference> getRequest() { 
      if (this.request == null)
        this.request = new ArrayList<Reference>();
      return this.request;
    }

    public boolean hasRequest() { 
      if (this.request == null)
        return false;
      for (Reference item : this.request)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #request} (Details concerning a test or procedure requested.)
     */
    // syntactic sugar
    public Reference addRequest() { //3
      Reference t = new Reference();
      if (this.request == null)
        this.request = new ArrayList<Reference>();
      this.request.add(t);
      return t;
    }

    // syntactic sugar
    public DiagnosticReport addRequest(Reference t) { //3
      if (t == null)
        return this;
      if (this.request == null)
        this.request = new ArrayList<Reference>();
      this.request.add(t);
      return this;
    }

    /**
     * @return {@link #request} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Details concerning a test or procedure requested.)
     */
    public List<Resource> getRequestTarget() { 
      if (this.requestTarget == null)
        this.requestTarget = new ArrayList<Resource>();
      return this.requestTarget;
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

    // syntactic sugar
    public DiagnosticReport addSpecimen(Reference t) { //3
      if (t == null)
        return this;
      if (this.specimen == null)
        this.specimen = new ArrayList<Reference>();
      this.specimen.add(t);
      return this;
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

    // syntactic sugar
    public DiagnosticReport addResult(Reference t) { //3
      if (t == null)
        return this;
      if (this.result == null)
        this.result = new ArrayList<Reference>();
      this.result.add(t);
      return this;
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

    // syntactic sugar
    public DiagnosticReport addImagingStudy(Reference t) { //3
      if (t == null)
        return this;
      if (this.imagingStudy == null)
        this.imagingStudy = new ArrayList<Reference>();
      this.imagingStudy.add(t);
      return this;
    }

    /**
     * @return {@link #imagingStudy} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.)
     */
    public List<Resource> getImagingStudyTarget() { 
      if (this.imagingStudyTarget == null)
        this.imagingStudyTarget = new ArrayList<Resource>();
      return this.imagingStudyTarget;
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

    // syntactic sugar
    public DiagnosticReport addImage(DiagnosticReportImageComponent t) { //3
      if (t == null)
        return this;
      if (this.image == null)
        this.image = new ArrayList<DiagnosticReportImageComponent>();
      this.image.add(t);
      return this;
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

    // syntactic sugar
    public DiagnosticReport addCodedDiagnosis(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.codedDiagnosis == null)
        this.codedDiagnosis = new ArrayList<CodeableConcept>();
      this.codedDiagnosis.add(t);
      return this;
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

    // syntactic sugar
    public DiagnosticReport addPresentedForm(Attachment t) { //3
      if (t == null)
        return this;
      if (this.presentedForm == null)
        this.presentedForm = new ArrayList<Attachment>();
      this.presentedForm.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The local ID assigned to the report by the order filler, usually by the Information System of the diagnostic service provider.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the diagnostic report as a whole.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("code", "CodeableConcept", "A code or name that describes this diagnostic report.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Location)", "The subject of the report. Usually, but not always, this is a patient. However diagnostic services also perform analyses on specimens collected from a variety of other sources.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The link to the health care event (encounter) when the order was made.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("effective[x]", "dateTime|Period", "The time or time-period the observed values are related to. When the subject of the report is a patient, this is usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is not known, only the date/time itself.", 0, java.lang.Integer.MAX_VALUE, effective));
        childrenList.add(new Property("issued", "instant", "The date and time that this version of the report was released from the source diagnostic service.", 0, java.lang.Integer.MAX_VALUE, issued));
        childrenList.add(new Property("performer", "Reference(Practitioner|Organization)", "The diagnostic service that is responsible for issuing the report.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("request", "Reference(DiagnosticOrder|ProcedureRequest|ReferralRequest)", "Details concerning a test or procedure requested.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("specimen", "Reference(Specimen)", "Details about the specimens on which this diagnostic report is based.", 0, java.lang.Integer.MAX_VALUE, specimen));
        childrenList.add(new Property("result", "Reference(Observation)", "Observations that are part of this diagnostic report. Observations can be simple name/value pairs (e.g. \"atomic\" results), or they can be grouping observations that include references to other members of the group (e.g. \"panels\").", 0, java.lang.Integer.MAX_VALUE, result));
        childrenList.add(new Property("imagingStudy", "Reference(ImagingStudy|ImagingObjectSelection)", "One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this information to provide views of the source images.", 0, java.lang.Integer.MAX_VALUE, imagingStudy));
        childrenList.add(new Property("image", "", "A list of key images associated with this report. The images are generally created during the diagnostic process, and may be directly of the patient, or of treated specimens (i.e. slides of interest).", 0, java.lang.Integer.MAX_VALUE, image));
        childrenList.add(new Property("conclusion", "string", "Concise and clinically contextualized narrative interpretation of the diagnostic report.", 0, java.lang.Integer.MAX_VALUE, conclusion));
        childrenList.add(new Property("codedDiagnosis", "CodeableConcept", "Codes for the conclusion.", 0, java.lang.Integer.MAX_VALUE, codedDiagnosis));
        childrenList.add(new Property("presentedForm", "Attachment", "Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but they SHALL be semantically equivalent.", 0, java.lang.Integer.MAX_VALUE, presentedForm));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<DiagnosticReportStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        case -1179159893: /*issued*/ return this.issued == null ? new Base[0] : new Base[] {this.issued}; // InstantType
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : this.request.toArray(new Base[this.request.size()]); // Reference
        case -2132868344: /*specimen*/ return this.specimen == null ? new Base[0] : this.specimen.toArray(new Base[this.specimen.size()]); // Reference
        case -934426595: /*result*/ return this.result == null ? new Base[0] : this.result.toArray(new Base[this.result.size()]); // Reference
        case -814900911: /*imagingStudy*/ return this.imagingStudy == null ? new Base[0] : this.imagingStudy.toArray(new Base[this.imagingStudy.size()]); // Reference
        case 100313435: /*image*/ return this.image == null ? new Base[0] : this.image.toArray(new Base[this.image.size()]); // DiagnosticReportImageComponent
        case -1731259873: /*conclusion*/ return this.conclusion == null ? new Base[0] : new Base[] {this.conclusion}; // StringType
        case -1364269926: /*codedDiagnosis*/ return this.codedDiagnosis == null ? new Base[0] : this.codedDiagnosis.toArray(new Base[this.codedDiagnosis.size()]); // CodeableConcept
        case 230090366: /*presentedForm*/ return this.presentedForm == null ? new Base[0] : this.presentedForm.toArray(new Base[this.presentedForm.size()]); // Attachment
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -892481550: // status
          this.status = new DiagnosticReportStatusEnumFactory().fromType(value); // Enumeration<DiagnosticReportStatus>
          break;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          break;
        case -1468651097: // effective
          this.effective = (Type) value; // Type
          break;
        case -1179159893: // issued
          this.issued = castToInstant(value); // InstantType
          break;
        case 481140686: // performer
          this.performer = castToReference(value); // Reference
          break;
        case 1095692943: // request
          this.getRequest().add(castToReference(value)); // Reference
          break;
        case -2132868344: // specimen
          this.getSpecimen().add(castToReference(value)); // Reference
          break;
        case -934426595: // result
          this.getResult().add(castToReference(value)); // Reference
          break;
        case -814900911: // imagingStudy
          this.getImagingStudy().add(castToReference(value)); // Reference
          break;
        case 100313435: // image
          this.getImage().add((DiagnosticReportImageComponent) value); // DiagnosticReportImageComponent
          break;
        case -1731259873: // conclusion
          this.conclusion = castToString(value); // StringType
          break;
        case -1364269926: // codedDiagnosis
          this.getCodedDiagnosis().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 230090366: // presentedForm
          this.getPresentedForm().add(castToAttachment(value)); // Attachment
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new DiagnosticReportStatusEnumFactory().fromType(value); // Enumeration<DiagnosticReportStatus>
        else if (name.equals("category"))
          this.category = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("effective[x]"))
          this.effective = (Type) value; // Type
        else if (name.equals("issued"))
          this.issued = castToInstant(value); // InstantType
        else if (name.equals("performer"))
          this.performer = castToReference(value); // Reference
        else if (name.equals("request"))
          this.getRequest().add(castToReference(value));
        else if (name.equals("specimen"))
          this.getSpecimen().add(castToReference(value));
        else if (name.equals("result"))
          this.getResult().add(castToReference(value));
        else if (name.equals("imagingStudy"))
          this.getImagingStudy().add(castToReference(value));
        else if (name.equals("image"))
          this.getImage().add((DiagnosticReportImageComponent) value);
        else if (name.equals("conclusion"))
          this.conclusion = castToString(value); // StringType
        else if (name.equals("codedDiagnosis"))
          this.getCodedDiagnosis().add(castToCodeableConcept(value));
        else if (name.equals("presentedForm"))
          this.getPresentedForm().add(castToAttachment(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<DiagnosticReportStatus>
        case 50511102:  return getCategory(); // CodeableConcept
        case 3059181:  return getCode(); // CodeableConcept
        case -1867885268:  return getSubject(); // Reference
        case 1524132147:  return getEncounter(); // Reference
        case 247104889:  return getEffective(); // Type
        case -1179159893: throw new FHIRException("Cannot make property issued as it is not a complex type"); // InstantType
        case 481140686:  return getPerformer(); // Reference
        case 1095692943:  return addRequest(); // Reference
        case -2132868344:  return addSpecimen(); // Reference
        case -934426595:  return addResult(); // Reference
        case -814900911:  return addImagingStudy(); // Reference
        case 100313435:  return addImage(); // DiagnosticReportImageComponent
        case -1731259873: throw new FHIRException("Cannot make property conclusion as it is not a complex type"); // StringType
        case -1364269926:  return addCodedDiagnosis(); // CodeableConcept
        case 230090366:  return addPresentedForm(); // Attachment
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type DiagnosticReport.status");
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("effectiveDateTime")) {
          this.effective = new DateTimeType();
          return this.effective;
        }
        else if (name.equals("effectivePeriod")) {
          this.effective = new Period();
          return this.effective;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type DiagnosticReport.issued");
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("request")) {
          return addRequest();
        }
        else if (name.equals("specimen")) {
          return addSpecimen();
        }
        else if (name.equals("result")) {
          return addResult();
        }
        else if (name.equals("imagingStudy")) {
          return addImagingStudy();
        }
        else if (name.equals("image")) {
          return addImage();
        }
        else if (name.equals("conclusion")) {
          throw new FHIRException("Cannot call addChild on a primitive type DiagnosticReport.conclusion");
        }
        else if (name.equals("codedDiagnosis")) {
          return addCodedDiagnosis();
        }
        else if (name.equals("presentedForm")) {
          return addPresentedForm();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DiagnosticReport";

  }

      public DiagnosticReport copy() {
        DiagnosticReport dst = new DiagnosticReport();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.effective = effective == null ? null : effective.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.performer = performer == null ? null : performer.copy();
        if (request != null) {
          dst.request = new ArrayList<Reference>();
          for (Reference i : request)
            dst.request.add(i.copy());
        };
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(code, o.code, true) && compareDeep(subject, o.subject, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(effective, o.effective, true) && compareDeep(issued, o.issued, true) && compareDeep(performer, o.performer, true)
           && compareDeep(request, o.request, true) && compareDeep(specimen, o.specimen, true) && compareDeep(result, o.result, true)
           && compareDeep(imagingStudy, o.imagingStudy, true) && compareDeep(image, o.image, true) && compareDeep(conclusion, o.conclusion, true)
           && compareDeep(codedDiagnosis, o.codedDiagnosis, true) && compareDeep(presentedForm, o.presentedForm, true)
          ;
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
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (category == null || category.isEmpty()) && (code == null || code.isEmpty()) && (subject == null || subject.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (effective == null || effective.isEmpty())
           && (issued == null || issued.isEmpty()) && (performer == null || performer.isEmpty()) && (request == null || request.isEmpty())
           && (specimen == null || specimen.isEmpty()) && (result == null || result.isEmpty()) && (imagingStudy == null || imagingStudy.isEmpty())
           && (image == null || image.isEmpty()) && (conclusion == null || conclusion.isEmpty()) && (codedDiagnosis == null || codedDiagnosis.isEmpty())
           && (presentedForm == null || presentedForm.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DiagnosticReport;
   }

 /**
   * Search parameter: <b>result</b>
   * <p>
   * Description: <b>Link to an atomic result (observation resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.result</b><br>
   * </p>
   */
  @SearchParamDefinition(name="result", path="DiagnosticReport.result", description="Link to an atomic result (observation resource)", type="reference" )
  public static final String SP_RESULT = "result";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>result</b>
   * <p>
   * Description: <b>Link to an atomic result (observation resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.result</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESULT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESULT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticReport:result</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESULT = new ca.uhn.fhir.model.api.Include("DiagnosticReport:result").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="DiagnosticReport.status", description="The status of the report", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The subject of the report</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="DiagnosticReport.subject", description="The subject of the report", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The subject of the report</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticReport:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("DiagnosticReport:subject").toLocked();

 /**
   * Search parameter: <b>issued</b>
   * <p>
   * Description: <b>When the report was issued</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DiagnosticReport.issued</b><br>
   * </p>
   */
  @SearchParamDefinition(name="issued", path="DiagnosticReport.issued", description="When the report was issued", type="date" )
  public static final String SP_ISSUED = "issued";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>issued</b>
   * <p>
   * Description: <b>When the report was issued</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DiagnosticReport.issued</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ISSUED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ISSUED);

 /**
   * Search parameter: <b>diagnosis</b>
   * <p>
   * Description: <b>A coded diagnosis on the report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.codedDiagnosis</b><br>
   * </p>
   */
  @SearchParamDefinition(name="diagnosis", path="DiagnosticReport.codedDiagnosis", description="A coded diagnosis on the report", type="token" )
  public static final String SP_DIAGNOSIS = "diagnosis";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>diagnosis</b>
   * <p>
   * Description: <b>A coded diagnosis on the report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.codedDiagnosis</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam DIAGNOSIS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_DIAGNOSIS);

 /**
   * Search parameter: <b>image</b>
   * <p>
   * Description: <b>A reference to the image source.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.image.link</b><br>
   * </p>
   */
  @SearchParamDefinition(name="image", path="DiagnosticReport.image.link", description="A reference to the image source.", type="reference" )
  public static final String SP_IMAGE = "image";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>image</b>
   * <p>
   * Description: <b>A reference to the image source.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.image.link</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam IMAGE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_IMAGE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticReport:image</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_IMAGE = new ca.uhn.fhir.model.api.Include("DiagnosticReport:image").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>The Encounter when the order was made</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="DiagnosticReport.encounter", description="The Encounter when the order was made", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>The Encounter when the order was made</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticReport:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("DiagnosticReport:encounter").toLocked();

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>The code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="DiagnosticReport.code", description="The code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>The code for the report as a whole, as opposed to codes for the atomic results, which are the names on the observation resource referred to from the result</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The clinically relevant time of the report</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DiagnosticReport.effective[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="DiagnosticReport.effective", description="The clinically relevant time of the report", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The clinically relevant time of the report</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DiagnosticReport.effective[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Which diagnostic discipline/department created the report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="DiagnosticReport.category", description="Which diagnostic discipline/department created the report", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Which diagnostic discipline/department created the report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The subject of the report if a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="DiagnosticReport.subject", description="The subject of the report if a patient", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The subject of the report if a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticReport:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("DiagnosticReport:patient").toLocked();

 /**
   * Search parameter: <b>request</b>
   * <p>
   * Description: <b>Reference to the test or procedure request.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.request</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request", path="DiagnosticReport.request", description="Reference to the test or procedure request.", type="reference" )
  public static final String SP_REQUEST = "request";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request</b>
   * <p>
   * Description: <b>Reference to the test or procedure request.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.request</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticReport:request</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST = new ca.uhn.fhir.model.api.Include("DiagnosticReport:request").toLocked();

 /**
   * Search parameter: <b>specimen</b>
   * <p>
   * Description: <b>The specimen details</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.specimen</b><br>
   * </p>
   */
  @SearchParamDefinition(name="specimen", path="DiagnosticReport.specimen", description="The specimen details", type="reference" )
  public static final String SP_SPECIMEN = "specimen";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
   * <p>
   * Description: <b>The specimen details</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.specimen</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SPECIMEN = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SPECIMEN);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticReport:specimen</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SPECIMEN = new ca.uhn.fhir.model.api.Include("DiagnosticReport:specimen").toLocked();

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Who was the source of the report (organization)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="DiagnosticReport.performer", description="Who was the source of the report (organization)", type="reference" )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>Who was the source of the report (organization)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticReport.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticReport:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("DiagnosticReport:performer").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>An identifier for the report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DiagnosticReport.identifier", description="An identifier for the report", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>An identifier for the report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticReport.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

