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
 * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement.
 */
@ResourceDef(name="Composition", profile="http://hl7.org/fhir/Profile/Composition")
public class Composition extends DomainResource {

    public enum CompositionStatus {
        /**
         * This is a preliminary composition or document (also known as initial or interim). The content may be incomplete or unverified.
         */
        PRELIMINARY, 
        /**
         * The composition or document is complete and verified by an appropriate person, and no further work is planned.
         */
        FINAL, 
        /**
         * The composition or document has been modified subsequent to being released as "final", and is complete and verified by an authorized person. The modifications added new information to the composition or document, but did not revise existing content.
         */
        APPENDED, 
        /**
         * The composition or document has been modified subsequent to being released as "final", and is complete and verified by an authorized person.
         */
        AMENDED, 
        /**
         * The composition or document was originally created/issued in error, and this is an amendment that marks that the entire series should not be considered as valid.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CompositionStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preliminary".equals(codeString))
          return PRELIMINARY;
        if ("final".equals(codeString))
          return FINAL;
        if ("appended".equals(codeString))
          return APPENDED;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown CompositionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRELIMINARY: return "preliminary";
            case FINAL: return "final";
            case APPENDED: return "appended";
            case AMENDED: return "amended";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PRELIMINARY: return "";
            case FINAL: return "";
            case APPENDED: return "";
            case AMENDED: return "";
            case ENTEREDINERROR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PRELIMINARY: return "This is a preliminary composition or document (also known as initial or interim). The content may be incomplete or unverified.";
            case FINAL: return "The composition or document is complete and verified by an appropriate person, and no further work is planned.";
            case APPENDED: return "The composition or document has been modified subsequent to being released as 'final', and is complete and verified by an authorized person. The modifications added new information to the composition or document, but did not revise existing content.";
            case AMENDED: return "The composition or document has been modified subsequent to being released as 'final', and is complete and verified by an authorized person.";
            case ENTEREDINERROR: return "The composition or document was originally created/issued in error, and this is an amendment that marks that the entire series should not be considered as valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRELIMINARY: return "preliminary";
            case FINAL: return "final";
            case APPENDED: return "appended";
            case AMENDED: return "amended";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
    }

  public static class CompositionStatusEnumFactory implements EnumFactory<CompositionStatus> {
    public CompositionStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preliminary".equals(codeString))
          return CompositionStatus.PRELIMINARY;
        if ("final".equals(codeString))
          return CompositionStatus.FINAL;
        if ("appended".equals(codeString))
          return CompositionStatus.APPENDED;
        if ("amended".equals(codeString))
          return CompositionStatus.AMENDED;
        if ("entered-in-error".equals(codeString))
          return CompositionStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown CompositionStatus code '"+codeString+"'");
        }
    public String toCode(CompositionStatus code) {
      if (code == CompositionStatus.PRELIMINARY)
        return "preliminary";
      if (code == CompositionStatus.FINAL)
        return "final";
      if (code == CompositionStatus.APPENDED)
        return "appended";
      if (code == CompositionStatus.AMENDED)
        return "amended";
      if (code == CompositionStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    }

    public enum CompositionAttestationMode {
        /**
         * The person authenticated the content in their personal capacity.
         */
        PERSONAL, 
        /**
         * The person authenticated the content in their professional capacity.
         */
        PROFESSIONAL, 
        /**
         * The person authenticated the content and accepted legal responsibility for its content.
         */
        LEGAL, 
        /**
         * The organization authenticated the content as consistent with their policies and procedures.
         */
        OFFICIAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CompositionAttestationMode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("personal".equals(codeString))
          return PERSONAL;
        if ("professional".equals(codeString))
          return PROFESSIONAL;
        if ("legal".equals(codeString))
          return LEGAL;
        if ("official".equals(codeString))
          return OFFICIAL;
        throw new Exception("Unknown CompositionAttestationMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PERSONAL: return "personal";
            case PROFESSIONAL: return "professional";
            case LEGAL: return "legal";
            case OFFICIAL: return "official";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PERSONAL: return "";
            case PROFESSIONAL: return "";
            case LEGAL: return "";
            case OFFICIAL: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PERSONAL: return "The person authenticated the content in their personal capacity.";
            case PROFESSIONAL: return "The person authenticated the content in their professional capacity.";
            case LEGAL: return "The person authenticated the content and accepted legal responsibility for its content.";
            case OFFICIAL: return "The organization authenticated the content as consistent with their policies and procedures.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PERSONAL: return "personal";
            case PROFESSIONAL: return "professional";
            case LEGAL: return "legal";
            case OFFICIAL: return "official";
            default: return "?";
          }
        }
    }

  public static class CompositionAttestationModeEnumFactory implements EnumFactory<CompositionAttestationMode> {
    public CompositionAttestationMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("personal".equals(codeString))
          return CompositionAttestationMode.PERSONAL;
        if ("professional".equals(codeString))
          return CompositionAttestationMode.PROFESSIONAL;
        if ("legal".equals(codeString))
          return CompositionAttestationMode.LEGAL;
        if ("official".equals(codeString))
          return CompositionAttestationMode.OFFICIAL;
        throw new IllegalArgumentException("Unknown CompositionAttestationMode code '"+codeString+"'");
        }
    public String toCode(CompositionAttestationMode code) {
      if (code == CompositionAttestationMode.PERSONAL)
        return "personal";
      if (code == CompositionAttestationMode.PROFESSIONAL)
        return "professional";
      if (code == CompositionAttestationMode.LEGAL)
        return "legal";
      if (code == CompositionAttestationMode.OFFICIAL)
        return "official";
      return "?";
      }
    }

    @Block()
    public static class CompositionAttesterComponent extends BackboneElement {
        /**
         * The type of attestation the authenticator offers.
         */
        @Child(name="mode", type={CodeType.class}, order=1, min=1, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="personal | professional | legal | official", formalDefinition="The type of attestation the authenticator offers." )
        protected List<Enumeration<CompositionAttestationMode>> mode;

        /**
         * When composition was attested by the party.
         */
        @Child(name="time", type={DateTimeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="When composition attested", formalDefinition="When composition was attested by the party." )
        protected DateTimeType time;

        /**
         * Who attested the composition in the specified way.
         */
        @Child(name="party", type={Patient.class, Practitioner.class, Organization.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Who attested the composition", formalDefinition="Who attested the composition in the specified way." )
        protected Reference party;

        /**
         * The actual object that is the target of the reference (Who attested the composition in the specified way.)
         */
        protected Resource partyTarget;

        private static final long serialVersionUID = -436604745L;

      public CompositionAttesterComponent() {
        super();
      }

        /**
         * @return {@link #mode} (The type of attestation the authenticator offers.)
         */
        public List<Enumeration<CompositionAttestationMode>> getMode() { 
          if (this.mode == null)
            this.mode = new ArrayList<Enumeration<CompositionAttestationMode>>();
          return this.mode;
        }

        public boolean hasMode() { 
          if (this.mode == null)
            return false;
          for (Enumeration<CompositionAttestationMode> item : this.mode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #mode} (The type of attestation the authenticator offers.)
         */
    // syntactic sugar
        public Enumeration<CompositionAttestationMode> addModeElement() {//2 
          Enumeration<CompositionAttestationMode> t = new Enumeration<CompositionAttestationMode>(new CompositionAttestationModeEnumFactory());
          if (this.mode == null)
            this.mode = new ArrayList<Enumeration<CompositionAttestationMode>>();
          this.mode.add(t);
          return t;
        }

        /**
         * @param value {@link #mode} (The type of attestation the authenticator offers.)
         */
        public CompositionAttesterComponent addMode(CompositionAttestationMode value) { //1
          Enumeration<CompositionAttestationMode> t = new Enumeration<CompositionAttestationMode>(new CompositionAttestationModeEnumFactory());
          t.setValue(value);
          if (this.mode == null)
            this.mode = new ArrayList<Enumeration<CompositionAttestationMode>>();
          this.mode.add(t);
          return this;
        }

        /**
         * @param value {@link #mode} (The type of attestation the authenticator offers.)
         */
        public boolean hasMode(CompositionAttestationMode value) { 
          if (this.mode == null)
            return false;
          for (Enumeration<CompositionAttestationMode> v : this.mode)
            if (v.equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #time} (When composition was attested by the party.). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
         */
        public DateTimeType getTimeElement() { 
          if (this.time == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompositionAttesterComponent.time");
            else if (Configuration.doAutoCreate())
              this.time = new DateTimeType(); // bb
          return this.time;
        }

        public boolean hasTimeElement() { 
          return this.time != null && !this.time.isEmpty();
        }

        public boolean hasTime() { 
          return this.time != null && !this.time.isEmpty();
        }

        /**
         * @param value {@link #time} (When composition was attested by the party.). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
         */
        public CompositionAttesterComponent setTimeElement(DateTimeType value) { 
          this.time = value;
          return this;
        }

        /**
         * @return When composition was attested by the party.
         */
        public Date getTime() { 
          return this.time == null ? null : this.time.getValue();
        }

        /**
         * @param value When composition was attested by the party.
         */
        public CompositionAttesterComponent setTime(Date value) { 
          if (value == null)
            this.time = null;
          else {
            if (this.time == null)
              this.time = new DateTimeType();
            this.time.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #party} (Who attested the composition in the specified way.)
         */
        public Reference getParty() { 
          if (this.party == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompositionAttesterComponent.party");
            else if (Configuration.doAutoCreate())
              this.party = new Reference(); // cc
          return this.party;
        }

        public boolean hasParty() { 
          return this.party != null && !this.party.isEmpty();
        }

        /**
         * @param value {@link #party} (Who attested the composition in the specified way.)
         */
        public CompositionAttesterComponent setParty(Reference value) { 
          this.party = value;
          return this;
        }

        /**
         * @return {@link #party} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who attested the composition in the specified way.)
         */
        public Resource getPartyTarget() { 
          return this.partyTarget;
        }

        /**
         * @param value {@link #party} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who attested the composition in the specified way.)
         */
        public CompositionAttesterComponent setPartyTarget(Resource value) { 
          this.partyTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("mode", "code", "The type of attestation the authenticator offers.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("time", "dateTime", "When composition was attested by the party.", 0, java.lang.Integer.MAX_VALUE, time));
          childrenList.add(new Property("party", "Reference(Patient|Practitioner|Organization)", "Who attested the composition in the specified way.", 0, java.lang.Integer.MAX_VALUE, party));
        }

      public CompositionAttesterComponent copy() {
        CompositionAttesterComponent dst = new CompositionAttesterComponent();
        copyValues(dst);
        if (mode != null) {
          dst.mode = new ArrayList<Enumeration<CompositionAttestationMode>>();
          for (Enumeration<CompositionAttestationMode> i : mode)
            dst.mode.add(i.copy());
        };
        dst.time = time == null ? null : time.copy();
        dst.party = party == null ? null : party.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CompositionAttesterComponent))
          return false;
        CompositionAttesterComponent o = (CompositionAttesterComponent) other;
        return compareDeep(mode, o.mode, true) && compareDeep(time, o.time, true) && compareDeep(party, o.party, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompositionAttesterComponent))
          return false;
        CompositionAttesterComponent o = (CompositionAttesterComponent) other;
        return compareValues(mode, o.mode, true) && compareValues(time, o.time, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (mode == null || mode.isEmpty()) && (time == null || time.isEmpty())
           && (party == null || party.isEmpty());
      }

  }

    @Block()
    public static class CompositionEventComponent extends BackboneElement {
        /**
         * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure being documented is necessarily a "History and Physical" act.
         */
        @Child(name="code", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Code(s) that apply to the event being documented", formalDefinition="This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a 'History and Physical Report' in which the procedure being documented is necessarily a 'History and Physical' act." )
        protected List<CodeableConcept> code;

        /**
         * The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time.
         */
        @Child(name="period", type={Period.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The period covered by the documentation", formalDefinition="The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time." )
        protected Period period;

        /**
         * Full details for the event(s) the composition/documentation consents.
         */
        @Child(name="detail", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Full details for the event(s) the composition consents", formalDefinition="Full details for the event(s) the composition/documentation consents." )
        protected List<Reference> detail;
        /**
         * The actual objects that are the target of the reference (Full details for the event(s) the composition/documentation consents.)
         */
        protected List<Resource> detailTarget;


        private static final long serialVersionUID = -1581379774L;

      public CompositionEventComponent() {
        super();
      }

        /**
         * @return {@link #code} (This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure being documented is necessarily a "History and Physical" act.)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (CodeableConcept item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #code} (This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure being documented is necessarily a "History and Physical" act.)
         */
    // syntactic sugar
        public CodeableConcept addCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return t;
        }

        /**
         * @return {@link #period} (The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompositionEventComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time.)
         */
        public CompositionEventComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #detail} (Full details for the event(s) the composition/documentation consents.)
         */
        public List<Reference> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          return this.detail;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (Reference item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detail} (Full details for the event(s) the composition/documentation consents.)
         */
    // syntactic sugar
        public Reference addDetail() { //3
          Reference t = new Reference();
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          this.detail.add(t);
          return t;
        }

        /**
         * @return {@link #detail} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Full details for the event(s) the composition/documentation consents.)
         */
        public List<Resource> getDetailTarget() { 
          if (this.detailTarget == null)
            this.detailTarget = new ArrayList<Resource>();
          return this.detailTarget;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a 'History and Physical Report' in which the procedure being documented is necessarily a 'History and Physical' act.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("period", "Period", "The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("detail", "Reference(Any)", "Full details for the event(s) the composition/documentation consents.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      public CompositionEventComponent copy() {
        CompositionEventComponent dst = new CompositionEventComponent();
        copyValues(dst);
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        if (detail != null) {
          dst.detail = new ArrayList<Reference>();
          for (Reference i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CompositionEventComponent))
          return false;
        CompositionEventComponent o = (CompositionEventComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(period, o.period, true) && compareDeep(detail, o.detail, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompositionEventComponent))
          return false;
        CompositionEventComponent o = (CompositionEventComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (period == null || period.isEmpty())
           && (detail == null || detail.isEmpty());
      }

  }

    @Block()
    public static class SectionComponent extends BackboneElement {
        /**
         * The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.
         */
        @Child(name="title", type={StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Label for section (e.g. for ToC)", formalDefinition="The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents." )
        protected StringType title;

        /**
         * A code identifying the kind of content contained within the section. This must be consistent with the section title.
         */
        @Child(name="code", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Classification of section (recommended)", formalDefinition="A code identifying the kind of content contained within the section. This must be consistent with the section title." )
        protected CodeableConcept code;

        /**
         * A nested sub-section within this section.
         */
        @Child(name="section", type={SectionComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Nested Section", formalDefinition="A nested sub-section within this section." )
        protected List<SectionComponent> section;

        /**
         * The content (narrative and data) associated with the section.
         */
        @Child(name="content", type={}, order=4, min=0, max=1)
        @Description(shortDefinition="The Content of the section", formalDefinition="The content (narrative and data) associated with the section." )
        protected Reference content;

        /**
         * The actual object that is the target of the reference (The content (narrative and data) associated with the section.)
         */
        protected Resource contentTarget;

        private static final long serialVersionUID = -666692570L;

      public SectionComponent() {
        super();
      }

        /**
         * @return {@link #title} (The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SectionComponent.title");
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
         * @param value {@link #title} (The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public SectionComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.
         */
        public SectionComponent setTitle(String value) { 
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
         * @return {@link #code} (A code identifying the kind of content contained within the section. This must be consistent with the section title.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SectionComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code identifying the kind of content contained within the section. This must be consistent with the section title.)
         */
        public SectionComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #section} (A nested sub-section within this section.)
         */
        public List<SectionComponent> getSection() { 
          if (this.section == null)
            this.section = new ArrayList<SectionComponent>();
          return this.section;
        }

        public boolean hasSection() { 
          if (this.section == null)
            return false;
          for (SectionComponent item : this.section)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #section} (A nested sub-section within this section.)
         */
    // syntactic sugar
        public SectionComponent addSection() { //3
          SectionComponent t = new SectionComponent();
          if (this.section == null)
            this.section = new ArrayList<SectionComponent>();
          this.section.add(t);
          return t;
        }

        /**
         * @return {@link #content} (The content (narrative and data) associated with the section.)
         */
        public Reference getContent() { 
          if (this.content == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SectionComponent.content");
            else if (Configuration.doAutoCreate())
              this.content = new Reference(); // cc
          return this.content;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (The content (narrative and data) associated with the section.)
         */
        public SectionComponent setContent(Reference value) { 
          this.content = value;
          return this;
        }

        /**
         * @return {@link #content} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The content (narrative and data) associated with the section.)
         */
        public Resource getContentTarget() { 
          return this.contentTarget;
        }

        /**
         * @param value {@link #content} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The content (narrative and data) associated with the section.)
         */
        public SectionComponent setContentTarget(Resource value) { 
          this.contentTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("title", "string", "The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("code", "CodeableConcept", "A code identifying the kind of content contained within the section. This must be consistent with the section title.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("section", "@Composition.section", "A nested sub-section within this section.", 0, java.lang.Integer.MAX_VALUE, section));
          childrenList.add(new Property("content", "Reference(Any)", "The content (narrative and data) associated with the section.", 0, java.lang.Integer.MAX_VALUE, content));
        }

      public SectionComponent copy() {
        SectionComponent dst = new SectionComponent();
        copyValues(dst);
        dst.title = title == null ? null : title.copy();
        dst.code = code == null ? null : code.copy();
        if (section != null) {
          dst.section = new ArrayList<SectionComponent>();
          for (SectionComponent i : section)
            dst.section.add(i.copy());
        };
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SectionComponent))
          return false;
        SectionComponent o = (SectionComponent) other;
        return compareDeep(title, o.title, true) && compareDeep(code, o.code, true) && compareDeep(section, o.section, true)
           && compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SectionComponent))
          return false;
        SectionComponent o = (SectionComponent) other;
        return compareValues(title, o.title, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (title == null || title.isEmpty()) && (code == null || code.isEmpty())
           && (section == null || section.isEmpty()) && (content == null || content.isEmpty());
      }

  }

    /**
     * Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = 1)
    @Description(shortDefinition="Logical identifier of composition (version-independent)", formalDefinition="Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time." )
    protected Identifier identifier;

    /**
     * The composition editing time, when the composition was last logically changed by the author.
     */
    @Child(name = "date", type = {DateTimeType.class}, order = 1, min = 1, max = 1)
    @Description(shortDefinition="Composition editing time", formalDefinition="The composition editing time, when the composition was last logically changed by the author." )
    protected DateTimeType date;

    /**
     * Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order = 2, min = 1, max = 1)
    @Description(shortDefinition="Kind of composition (LOINC if possible)", formalDefinition="Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition." )
    protected CodeableConcept type;

    /**
     * A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type.
     */
    @Child(name = "class_", type = {CodeableConcept.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Categorization of Composition", formalDefinition="A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type." )
    protected CodeableConcept class_;

    /**
     * Official human-readable label for the composition.
     */
    @Child(name = "title", type = {StringType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Human Readable name/title", formalDefinition="Official human-readable label for the composition." )
    protected StringType title;

    /**
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.
     */
    @Child(name = "status", type = {CodeType.class}, order = 5, min = 1, max = 1)
    @Description(shortDefinition="preliminary | final | appended | amended | entered-in-error", formalDefinition="The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document." )
    protected Enumeration<CompositionStatus> status;

    /**
     * The code specifying the level of confidentiality of the Composition.
     */
    @Child(name = "confidentiality", type = {Coding.class}, order = 6, min = 1, max = 1)
    @Description(shortDefinition="As defined by affinity domain", formalDefinition="The code specifying the level of confidentiality of the Composition." )
    protected Coding confidentiality;

    /**
     * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).
     */
    @Child(name = "subject", type = {Patient.class, Practitioner.class, Group.class, Device.class, Location.class}, order = 7, min = 1, max = 1)
    @Description(shortDefinition="Who and/or what the composition is about", formalDefinition="Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    protected Resource subjectTarget;

    /**
     * Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.).
     */
    @Child(name = "author", type = {Practitioner.class, Device.class, Patient.class, RelatedPerson.class}, order = 8, min = 1, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Who and/or what authored the composition", formalDefinition="Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.)." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.).)
     */
    protected List<Resource> authorTarget;


    /**
     * A participant who has attested to the accuracy of the composition/document.
     */
    @Child(name = "attester", type = {}, order = 9, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Attests to accuracy of composition", formalDefinition="A participant who has attested to the accuracy of the composition/document." )
    protected List<CompositionAttesterComponent> attester;

    /**
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.
     */
    @Child(name = "custodian", type = {Organization.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Org which maintains the composition", formalDefinition="Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information." )
    protected Reference custodian;

    /**
     * The actual object that is the target of the reference (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    protected Organization custodianTarget;

    /**
     * The clinical service, such as a colonoscopy or an appendectomy, being documented.
     */
    @Child(name = "event", type = {}, order = 11, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="The clinical service(s) being documented", formalDefinition="The clinical service, such as a colonoscopy or an appendectomy, being documented." )
    protected List<CompositionEventComponent> event;

    /**
     * Describes the clinical encounter or type of care this documentation is associated with.
     */
    @Child(name = "encounter", type = {Encounter.class}, order = 12, min = 0, max = 1)
    @Description(shortDefinition="Context of the conposition", formalDefinition="Describes the clinical encounter or type of care this documentation is associated with." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    protected Encounter encounterTarget;

    /**
     * The root of the sections that make up the composition.
     */
    @Child(name = "section", type = {}, order = 13, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Composition is broken into sections", formalDefinition="The root of the sections that make up the composition." )
    protected List<SectionComponent> section;

    private static final long serialVersionUID = 2112290467L;

    public Composition() {
      super();
    }

    public Composition(DateTimeType date, CodeableConcept type, Enumeration<CompositionStatus> status, Coding confidentiality, Reference subject) {
      super();
      this.date = date;
      this.type = type;
      this.status = status;
      this.confidentiality = confidentiality;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time.)
     */
    public Composition setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #date} (The composition editing time, when the composition was last logically changed by the author.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The composition editing time, when the composition was last logically changed by the author.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Composition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The composition editing time, when the composition was last logically changed by the author.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The composition editing time, when the composition was last logically changed by the author.
     */
    public Composition setDate(Date value) { 
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition.)
     */
    public Composition setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #class_} (A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type.)
     */
    public CodeableConcept getClass_() { 
      if (this.class_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.class_");
        else if (Configuration.doAutoCreate())
          this.class_ = new CodeableConcept(); // cc
      return this.class_;
    }

    public boolean hasClass_() { 
      return this.class_ != null && !this.class_.isEmpty();
    }

    /**
     * @param value {@link #class_} (A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type.)
     */
    public Composition setClass_(CodeableConcept value) { 
      this.class_ = value;
      return this;
    }

    /**
     * @return {@link #title} (Official human-readable label for the composition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.title");
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
     * @param value {@link #title} (Official human-readable label for the composition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public Composition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return Official human-readable label for the composition.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value Official human-readable label for the composition.
     */
    public Composition setTitle(String value) { 
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
     * @return {@link #status} (The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<CompositionStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<CompositionStatus>(new CompositionStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Composition setStatusElement(Enumeration<CompositionStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.
     */
    public CompositionStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.
     */
    public Composition setStatus(CompositionStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<CompositionStatus>(new CompositionStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #confidentiality} (The code specifying the level of confidentiality of the Composition.)
     */
    public Coding getConfidentiality() { 
      if (this.confidentiality == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.confidentiality");
        else if (Configuration.doAutoCreate())
          this.confidentiality = new Coding(); // cc
      return this.confidentiality;
    }

    public boolean hasConfidentiality() { 
      return this.confidentiality != null && !this.confidentiality.isEmpty();
    }

    /**
     * @param value {@link #confidentiality} (The code specifying the level of confidentiality of the Composition.)
     */
    public Composition setConfidentiality(Coding value) { 
      this.confidentiality = value;
      return this;
    }

    /**
     * @return {@link #subject} (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    public Composition setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    public Composition setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #author} (Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.).)
     */
    public List<Reference> getAuthor() { 
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      return this.author;
    }

    public boolean hasAuthor() { 
      if (this.author == null)
        return false;
      for (Reference item : this.author)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #author} (Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.).)
     */
    // syntactic sugar
    public Reference addAuthor() { //3
      Reference t = new Reference();
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return t;
    }

    /**
     * @return {@link #author} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.).)
     */
    public List<Resource> getAuthorTarget() { 
      if (this.authorTarget == null)
        this.authorTarget = new ArrayList<Resource>();
      return this.authorTarget;
    }

    /**
     * @return {@link #attester} (A participant who has attested to the accuracy of the composition/document.)
     */
    public List<CompositionAttesterComponent> getAttester() { 
      if (this.attester == null)
        this.attester = new ArrayList<CompositionAttesterComponent>();
      return this.attester;
    }

    public boolean hasAttester() { 
      if (this.attester == null)
        return false;
      for (CompositionAttesterComponent item : this.attester)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #attester} (A participant who has attested to the accuracy of the composition/document.)
     */
    // syntactic sugar
    public CompositionAttesterComponent addAttester() { //3
      CompositionAttesterComponent t = new CompositionAttesterComponent();
      if (this.attester == null)
        this.attester = new ArrayList<CompositionAttesterComponent>();
      this.attester.add(t);
      return t;
    }

    /**
     * @return {@link #custodian} (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    public Reference getCustodian() { 
      if (this.custodian == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.custodian");
        else if (Configuration.doAutoCreate())
          this.custodian = new Reference(); // cc
      return this.custodian;
    }

    public boolean hasCustodian() { 
      return this.custodian != null && !this.custodian.isEmpty();
    }

    /**
     * @param value {@link #custodian} (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    public Composition setCustodian(Reference value) { 
      this.custodian = value;
      return this;
    }

    /**
     * @return {@link #custodian} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    public Organization getCustodianTarget() { 
      if (this.custodianTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.custodian");
        else if (Configuration.doAutoCreate())
          this.custodianTarget = new Organization(); // aa
      return this.custodianTarget;
    }

    /**
     * @param value {@link #custodian} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    public Composition setCustodianTarget(Organization value) { 
      this.custodianTarget = value;
      return this;
    }

    /**
     * @return {@link #event} (The clinical service, such as a colonoscopy or an appendectomy, being documented.)
     */
    public List<CompositionEventComponent> getEvent() { 
      if (this.event == null)
        this.event = new ArrayList<CompositionEventComponent>();
      return this.event;
    }

    public boolean hasEvent() { 
      if (this.event == null)
        return false;
      for (CompositionEventComponent item : this.event)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #event} (The clinical service, such as a colonoscopy or an appendectomy, being documented.)
     */
    // syntactic sugar
    public CompositionEventComponent addEvent() { //3
      CompositionEventComponent t = new CompositionEventComponent();
      if (this.event == null)
        this.event = new ArrayList<CompositionEventComponent>();
      this.event.add(t);
      return t;
    }

    /**
     * @return {@link #encounter} (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    public Composition setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    public Composition setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #section} (The root of the sections that make up the composition.)
     */
    public List<SectionComponent> getSection() { 
      if (this.section == null)
        this.section = new ArrayList<SectionComponent>();
      return this.section;
    }

    public boolean hasSection() { 
      if (this.section == null)
        return false;
      for (SectionComponent item : this.section)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #section} (The root of the sections that make up the composition.)
     */
    // syntactic sugar
    public SectionComponent addSection() { //3
      SectionComponent t = new SectionComponent();
      if (this.section == null)
        this.section = new ArrayList<SectionComponent>();
      this.section.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Logical Identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("date", "dateTime", "The composition editing time, when the composition was last logically changed by the author.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("type", "CodeableConcept", "Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("class", "CodeableConcept", "A categorization for the type of the composition. This may be implied by or derived from the code specified in the Composition Type.", 0, java.lang.Integer.MAX_VALUE, class_));
        childrenList.add(new Property("title", "string", "Official human-readable label for the composition.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("confidentiality", "Coding", "The code specifying the level of confidentiality of the Composition.", 0, java.lang.Integer.MAX_VALUE, confidentiality));
        childrenList.add(new Property("subject", "Reference(Patient|Practitioner|Group|Device|Location)", "Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("author", "Reference(Practitioner|Device|Patient|RelatedPerson)", "Identifies who is responsible for the information in the composition.  (Not necessarily who typed it in.).", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("attester", "", "A participant who has attested to the accuracy of the composition/document.", 0, java.lang.Integer.MAX_VALUE, attester));
        childrenList.add(new Property("custodian", "Reference(Organization)", "Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.", 0, java.lang.Integer.MAX_VALUE, custodian));
        childrenList.add(new Property("event", "", "The clinical service, such as a colonoscopy or an appendectomy, being documented.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "Describes the clinical encounter or type of care this documentation is associated with.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("section", "", "The root of the sections that make up the composition.", 0, java.lang.Integer.MAX_VALUE, section));
      }

      public Composition copy() {
        Composition dst = new Composition();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.date = date == null ? null : date.copy();
        dst.type = type == null ? null : type.copy();
        dst.class_ = class_ == null ? null : class_.copy();
        dst.title = title == null ? null : title.copy();
        dst.status = status == null ? null : status.copy();
        dst.confidentiality = confidentiality == null ? null : confidentiality.copy();
        dst.subject = subject == null ? null : subject.copy();
        if (author != null) {
          dst.author = new ArrayList<Reference>();
          for (Reference i : author)
            dst.author.add(i.copy());
        };
        if (attester != null) {
          dst.attester = new ArrayList<CompositionAttesterComponent>();
          for (CompositionAttesterComponent i : attester)
            dst.attester.add(i.copy());
        };
        dst.custodian = custodian == null ? null : custodian.copy();
        if (event != null) {
          dst.event = new ArrayList<CompositionEventComponent>();
          for (CompositionEventComponent i : event)
            dst.event.add(i.copy());
        };
        dst.encounter = encounter == null ? null : encounter.copy();
        if (section != null) {
          dst.section = new ArrayList<SectionComponent>();
          for (SectionComponent i : section)
            dst.section.add(i.copy());
        };
        return dst;
      }

      protected Composition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Composition))
          return false;
        Composition o = (Composition) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(date, o.date, true) && compareDeep(type, o.type, true)
           && compareDeep(class_, o.class_, true) && compareDeep(title, o.title, true) && compareDeep(status, o.status, true)
           && compareDeep(confidentiality, o.confidentiality, true) && compareDeep(subject, o.subject, true)
           && compareDeep(author, o.author, true) && compareDeep(attester, o.attester, true) && compareDeep(custodian, o.custodian, true)
           && compareDeep(event, o.event, true) && compareDeep(encounter, o.encounter, true) && compareDeep(section, o.section, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Composition))
          return false;
        Composition o = (Composition) other;
        return compareValues(date, o.date, true) && compareValues(title, o.title, true) && compareValues(status, o.status, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (date == null || date.isEmpty())
           && (type == null || type.isEmpty()) && (class_ == null || class_.isEmpty()) && (title == null || title.isEmpty())
           && (status == null || status.isEmpty()) && (confidentiality == null || confidentiality.isEmpty())
           && (subject == null || subject.isEmpty()) && (author == null || author.isEmpty()) && (attester == null || attester.isEmpty())
           && (custodian == null || custodian.isEmpty()) && (event == null || event.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (section == null || section.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Composition;
   }

  @SearchParamDefinition(name="date", path="Composition.date", description="Composition editing time", type="date" )
  public static final String SP_DATE = "date";
    @SearchParamDefinition(name = "identifier", path = "Composition.identifier", description = "Logical identifier of composition (version-independent)", type = "token")
    public static final String SP_IDENTIFIER = "identifier";
    @SearchParamDefinition(name = "period", path = "Composition.event.period", description = "The period covered by the documentation", type = "date")
    public static final String SP_PERIOD = "period";
    @SearchParamDefinition(name = "subject", path = "Composition.subject", description = "Who and/or what the composition is about", type = "reference")
    public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="author", path="Composition.author", description="Who and/or what authored the composition", type="reference" )
  public static final String SP_AUTHOR = "author";
    @SearchParamDefinition(name = "confidentiality", path = "Composition.confidentiality", description = "As defined by affinity domain", type = "token")
    public static final String SP_CONFIDENTIALITY = "confidentiality";
    @SearchParamDefinition(name = "section-code", path = "Composition.section.code", description = "Classification of section (recommended)", type = "token")
    public static final String SP_SECTIONCODE = "section-code";
    @SearchParamDefinition(name = "section", path = "Composition.section.content", description = "The Content of the section", type = "reference")
    public static final String SP_SECTION = "section";
    @SearchParamDefinition(name = "type", path = "Composition.type", description = "Kind of composition (LOINC if possible)", type = "token")
    public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="title", path="Composition.title", description="Human Readable name/title", type="string" )
  public static final String SP_TITLE = "title";
  @SearchParamDefinition(name="attester", path="Composition.attester.party", description="Who attested the composition", type="reference" )
  public static final String SP_ATTESTER = "attester";
    @SearchParamDefinition(name = "patient", path = "Composition.subject", description = "Who and/or what the composition is about", type = "reference")
    public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="context", path="Composition.event.code", description="Code(s) that apply to the event being documented", type="token" )
  public static final String SP_CONTEXT = "context";
    @SearchParamDefinition(name = "class", path = "Composition.class", description = "Categorization of Composition", type = "token")
    public static final String SP_CLASS = "class";
    @SearchParamDefinition(name = "status", path = "Composition.status", description = "preliminary | final | appended | amended | entered-in-error", type = "token")
    public static final String SP_STATUS = "status";

}

