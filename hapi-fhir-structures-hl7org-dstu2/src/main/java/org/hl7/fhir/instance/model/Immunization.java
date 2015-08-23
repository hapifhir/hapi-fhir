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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * Immunization event information.
 */
@ResourceDef(name="Immunization", profile="http://hl7.org/fhir/Profile/Immunization")
public class Immunization extends DomainResource {

    @Block()
    public static class ImmunizationExplanationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reasons why a vaccine was administered.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Why immunization occurred", formalDefinition="Reasons why a vaccine was administered." )
        protected List<CodeableConcept> reason;

        /**
         * Reason why a vaccine was not administered.
         */
        @Child(name = "reasonNotGiven", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Why immunization did not occur", formalDefinition="Reason why a vaccine was not administered." )
        protected List<CodeableConcept> reasonNotGiven;

        private static final long serialVersionUID = -539821866L;

    /*
     * Constructor
     */
      public ImmunizationExplanationComponent() {
        super();
      }

        /**
         * @return {@link #reason} (Reasons why a vaccine was administered.)
         */
        public List<CodeableConcept> getReason() { 
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          return this.reason;
        }

        public boolean hasReason() { 
          if (this.reason == null)
            return false;
          for (CodeableConcept item : this.reason)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #reason} (Reasons why a vaccine was administered.)
         */
    // syntactic sugar
        public CodeableConcept addReason() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          this.reason.add(t);
          return t;
        }

    // syntactic sugar
        public ImmunizationExplanationComponent addReason(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          this.reason.add(t);
          return this;
        }

        /**
         * @return {@link #reasonNotGiven} (Reason why a vaccine was not administered.)
         */
        public List<CodeableConcept> getReasonNotGiven() { 
          if (this.reasonNotGiven == null)
            this.reasonNotGiven = new ArrayList<CodeableConcept>();
          return this.reasonNotGiven;
        }

        public boolean hasReasonNotGiven() { 
          if (this.reasonNotGiven == null)
            return false;
          for (CodeableConcept item : this.reasonNotGiven)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #reasonNotGiven} (Reason why a vaccine was not administered.)
         */
    // syntactic sugar
        public CodeableConcept addReasonNotGiven() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.reasonNotGiven == null)
            this.reasonNotGiven = new ArrayList<CodeableConcept>();
          this.reasonNotGiven.add(t);
          return t;
        }

    // syntactic sugar
        public ImmunizationExplanationComponent addReasonNotGiven(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.reasonNotGiven == null)
            this.reasonNotGiven = new ArrayList<CodeableConcept>();
          this.reasonNotGiven.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("reason", "CodeableConcept", "Reasons why a vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("reasonNotGiven", "CodeableConcept", "Reason why a vaccine was not administered.", 0, java.lang.Integer.MAX_VALUE, reasonNotGiven));
        }

      public ImmunizationExplanationComponent copy() {
        ImmunizationExplanationComponent dst = new ImmunizationExplanationComponent();
        copyValues(dst);
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        if (reasonNotGiven != null) {
          dst.reasonNotGiven = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonNotGiven)
            dst.reasonNotGiven.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImmunizationExplanationComponent))
          return false;
        ImmunizationExplanationComponent o = (ImmunizationExplanationComponent) other;
        return compareDeep(reason, o.reason, true) && compareDeep(reasonNotGiven, o.reasonNotGiven, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImmunizationExplanationComponent))
          return false;
        ImmunizationExplanationComponent o = (ImmunizationExplanationComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (reason == null || reason.isEmpty()) && (reasonNotGiven == null || reasonNotGiven.isEmpty())
          ;
      }

  }

    @Block()
    public static class ImmunizationReactionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Date of reaction to the immunization.
         */
        @Child(name = "date", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When did reaction start?", formalDefinition="Date of reaction to the immunization." )
        protected DateTimeType date;

        /**
         * Details of the reaction.
         */
        @Child(name = "detail", type = {Observation.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Additional information on reaction", formalDefinition="Details of the reaction." )
        protected Reference detail;

        /**
         * The actual object that is the target of the reference (Details of the reaction.)
         */
        protected Observation detailTarget;

        /**
         * Self-reported indicator.
         */
        @Child(name = "reported", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Was reaction self-reported?", formalDefinition="Self-reported indicator." )
        protected BooleanType reported;

        private static final long serialVersionUID = -1297668556L;

    /*
     * Constructor
     */
      public ImmunizationReactionComponent() {
        super();
      }

        /**
         * @return {@link #date} (Date of reaction to the immunization.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateTimeType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationReactionComponent.date");
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
         * @param value {@link #date} (Date of reaction to the immunization.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public ImmunizationReactionComponent setDateElement(DateTimeType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Date of reaction to the immunization.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Date of reaction to the immunization.
         */
        public ImmunizationReactionComponent setDate(Date value) { 
          if (value == null)
            this.date = null;
          else {
            if (this.date == null)
              this.date = new DateTimeType();
            this.date.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #detail} (Details of the reaction.)
         */
        public Reference getDetail() { 
          if (this.detail == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationReactionComponent.detail");
            else if (Configuration.doAutoCreate())
              this.detail = new Reference(); // cc
          return this.detail;
        }

        public boolean hasDetail() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        /**
         * @param value {@link #detail} (Details of the reaction.)
         */
        public ImmunizationReactionComponent setDetail(Reference value) { 
          this.detail = value;
          return this;
        }

        /**
         * @return {@link #detail} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Details of the reaction.)
         */
        public Observation getDetailTarget() { 
          if (this.detailTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationReactionComponent.detail");
            else if (Configuration.doAutoCreate())
              this.detailTarget = new Observation(); // aa
          return this.detailTarget;
        }

        /**
         * @param value {@link #detail} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Details of the reaction.)
         */
        public ImmunizationReactionComponent setDetailTarget(Observation value) { 
          this.detailTarget = value;
          return this;
        }

        /**
         * @return {@link #reported} (Self-reported indicator.). This is the underlying object with id, value and extensions. The accessor "getReported" gives direct access to the value
         */
        public BooleanType getReportedElement() { 
          if (this.reported == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationReactionComponent.reported");
            else if (Configuration.doAutoCreate())
              this.reported = new BooleanType(); // bb
          return this.reported;
        }

        public boolean hasReportedElement() { 
          return this.reported != null && !this.reported.isEmpty();
        }

        public boolean hasReported() { 
          return this.reported != null && !this.reported.isEmpty();
        }

        /**
         * @param value {@link #reported} (Self-reported indicator.). This is the underlying object with id, value and extensions. The accessor "getReported" gives direct access to the value
         */
        public ImmunizationReactionComponent setReportedElement(BooleanType value) { 
          this.reported = value;
          return this;
        }

        /**
         * @return Self-reported indicator.
         */
        public boolean getReported() { 
          return this.reported == null || this.reported.isEmpty() ? false : this.reported.getValue();
        }

        /**
         * @param value Self-reported indicator.
         */
        public ImmunizationReactionComponent setReported(boolean value) { 
            if (this.reported == null)
              this.reported = new BooleanType();
            this.reported.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("date", "dateTime", "Date of reaction to the immunization.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("detail", "Reference(Observation)", "Details of the reaction.", 0, java.lang.Integer.MAX_VALUE, detail));
          childrenList.add(new Property("reported", "boolean", "Self-reported indicator.", 0, java.lang.Integer.MAX_VALUE, reported));
        }

      public ImmunizationReactionComponent copy() {
        ImmunizationReactionComponent dst = new ImmunizationReactionComponent();
        copyValues(dst);
        dst.date = date == null ? null : date.copy();
        dst.detail = detail == null ? null : detail.copy();
        dst.reported = reported == null ? null : reported.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImmunizationReactionComponent))
          return false;
        ImmunizationReactionComponent o = (ImmunizationReactionComponent) other;
        return compareDeep(date, o.date, true) && compareDeep(detail, o.detail, true) && compareDeep(reported, o.reported, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImmunizationReactionComponent))
          return false;
        ImmunizationReactionComponent o = (ImmunizationReactionComponent) other;
        return compareValues(date, o.date, true) && compareValues(reported, o.reported, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (date == null || date.isEmpty()) && (detail == null || detail.isEmpty())
           && (reported == null || reported.isEmpty());
      }

  }

    @Block()
    public static class ImmunizationVaccinationProtocolComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Nominal position in a series.
         */
        @Child(name = "doseSequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="What dose number within series?", formalDefinition="Nominal position in a series." )
        protected PositiveIntType doseSequence;

        /**
         * Contains the description about the protocol under which the vaccine was administered.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Details of vaccine protocol", formalDefinition="Contains the description about the protocol under which the vaccine was administered." )
        protected StringType description;

        /**
         * Indicates the authority who published the protocol?  E.g. ACIP.
         */
        @Child(name = "authority", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Who is responsible for protocol", formalDefinition="Indicates the authority who published the protocol?  E.g. ACIP." )
        protected Reference authority;

        /**
         * The actual object that is the target of the reference (Indicates the authority who published the protocol?  E.g. ACIP.)
         */
        protected Organization authorityTarget;

        /**
         * One possible path to achieve presumed immunity against a disease - within the context of an authority.
         */
        @Child(name = "series", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of vaccine series", formalDefinition="One possible path to achieve presumed immunity against a disease - within the context of an authority." )
        protected StringType series;

        /**
         * The recommended number of doses to achieve immunity.
         */
        @Child(name = "seriesDoses", type = {PositiveIntType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Recommended number of doses for immunity", formalDefinition="The recommended number of doses to achieve immunity." )
        protected PositiveIntType seriesDoses;

        /**
         * The targeted disease.
         */
        @Child(name = "doseTarget", type = {CodeableConcept.class}, order=6, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Disease immunized against", formalDefinition="The targeted disease." )
        protected CodeableConcept doseTarget;

        /**
         * Indicates if the immunization event should "count" against  the protocol.
         */
        @Child(name = "doseStatus", type = {CodeableConcept.class}, order=7, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Does dose count towards immunity?", formalDefinition="Indicates if the immunization event should \"count\" against  the protocol." )
        protected CodeableConcept doseStatus;

        /**
         * Provides an explanation as to why a immunization event should or should not count against the protocol.
         */
        @Child(name = "doseStatusReason", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Why does does count/not count?", formalDefinition="Provides an explanation as to why a immunization event should or should not count against the protocol." )
        protected CodeableConcept doseStatusReason;

        private static final long serialVersionUID = -783437472L;

    /*
     * Constructor
     */
      public ImmunizationVaccinationProtocolComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImmunizationVaccinationProtocolComponent(PositiveIntType doseSequence, CodeableConcept doseTarget, CodeableConcept doseStatus) {
        super();
        this.doseSequence = doseSequence;
        this.doseTarget = doseTarget;
        this.doseStatus = doseStatus;
      }

        /**
         * @return {@link #doseSequence} (Nominal position in a series.). This is the underlying object with id, value and extensions. The accessor "getDoseSequence" gives direct access to the value
         */
        public PositiveIntType getDoseSequenceElement() { 
          if (this.doseSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationVaccinationProtocolComponent.doseSequence");
            else if (Configuration.doAutoCreate())
              this.doseSequence = new PositiveIntType(); // bb
          return this.doseSequence;
        }

        public boolean hasDoseSequenceElement() { 
          return this.doseSequence != null && !this.doseSequence.isEmpty();
        }

        public boolean hasDoseSequence() { 
          return this.doseSequence != null && !this.doseSequence.isEmpty();
        }

        /**
         * @param value {@link #doseSequence} (Nominal position in a series.). This is the underlying object with id, value and extensions. The accessor "getDoseSequence" gives direct access to the value
         */
        public ImmunizationVaccinationProtocolComponent setDoseSequenceElement(PositiveIntType value) { 
          this.doseSequence = value;
          return this;
        }

        /**
         * @return Nominal position in a series.
         */
        public int getDoseSequence() { 
          return this.doseSequence == null || this.doseSequence.isEmpty() ? 0 : this.doseSequence.getValue();
        }

        /**
         * @param value Nominal position in a series.
         */
        public ImmunizationVaccinationProtocolComponent setDoseSequence(int value) { 
            if (this.doseSequence == null)
              this.doseSequence = new PositiveIntType();
            this.doseSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Contains the description about the protocol under which the vaccine was administered.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationVaccinationProtocolComponent.description");
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
         * @param value {@link #description} (Contains the description about the protocol under which the vaccine was administered.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImmunizationVaccinationProtocolComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Contains the description about the protocol under which the vaccine was administered.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Contains the description about the protocol under which the vaccine was administered.
         */
        public ImmunizationVaccinationProtocolComponent setDescription(String value) { 
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
         * @return {@link #authority} (Indicates the authority who published the protocol?  E.g. ACIP.)
         */
        public Reference getAuthority() { 
          if (this.authority == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationVaccinationProtocolComponent.authority");
            else if (Configuration.doAutoCreate())
              this.authority = new Reference(); // cc
          return this.authority;
        }

        public boolean hasAuthority() { 
          return this.authority != null && !this.authority.isEmpty();
        }

        /**
         * @param value {@link #authority} (Indicates the authority who published the protocol?  E.g. ACIP.)
         */
        public ImmunizationVaccinationProtocolComponent setAuthority(Reference value) { 
          this.authority = value;
          return this;
        }

        /**
         * @return {@link #authority} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol?  E.g. ACIP.)
         */
        public Organization getAuthorityTarget() { 
          if (this.authorityTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationVaccinationProtocolComponent.authority");
            else if (Configuration.doAutoCreate())
              this.authorityTarget = new Organization(); // aa
          return this.authorityTarget;
        }

        /**
         * @param value {@link #authority} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol?  E.g. ACIP.)
         */
        public ImmunizationVaccinationProtocolComponent setAuthorityTarget(Organization value) { 
          this.authorityTarget = value;
          return this;
        }

        /**
         * @return {@link #series} (One possible path to achieve presumed immunity against a disease - within the context of an authority.). This is the underlying object with id, value and extensions. The accessor "getSeries" gives direct access to the value
         */
        public StringType getSeriesElement() { 
          if (this.series == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationVaccinationProtocolComponent.series");
            else if (Configuration.doAutoCreate())
              this.series = new StringType(); // bb
          return this.series;
        }

        public boolean hasSeriesElement() { 
          return this.series != null && !this.series.isEmpty();
        }

        public boolean hasSeries() { 
          return this.series != null && !this.series.isEmpty();
        }

        /**
         * @param value {@link #series} (One possible path to achieve presumed immunity against a disease - within the context of an authority.). This is the underlying object with id, value and extensions. The accessor "getSeries" gives direct access to the value
         */
        public ImmunizationVaccinationProtocolComponent setSeriesElement(StringType value) { 
          this.series = value;
          return this;
        }

        /**
         * @return One possible path to achieve presumed immunity against a disease - within the context of an authority.
         */
        public String getSeries() { 
          return this.series == null ? null : this.series.getValue();
        }

        /**
         * @param value One possible path to achieve presumed immunity against a disease - within the context of an authority.
         */
        public ImmunizationVaccinationProtocolComponent setSeries(String value) { 
          if (Utilities.noString(value))
            this.series = null;
          else {
            if (this.series == null)
              this.series = new StringType();
            this.series.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #seriesDoses} (The recommended number of doses to achieve immunity.). This is the underlying object with id, value and extensions. The accessor "getSeriesDoses" gives direct access to the value
         */
        public PositiveIntType getSeriesDosesElement() { 
          if (this.seriesDoses == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationVaccinationProtocolComponent.seriesDoses");
            else if (Configuration.doAutoCreate())
              this.seriesDoses = new PositiveIntType(); // bb
          return this.seriesDoses;
        }

        public boolean hasSeriesDosesElement() { 
          return this.seriesDoses != null && !this.seriesDoses.isEmpty();
        }

        public boolean hasSeriesDoses() { 
          return this.seriesDoses != null && !this.seriesDoses.isEmpty();
        }

        /**
         * @param value {@link #seriesDoses} (The recommended number of doses to achieve immunity.). This is the underlying object with id, value and extensions. The accessor "getSeriesDoses" gives direct access to the value
         */
        public ImmunizationVaccinationProtocolComponent setSeriesDosesElement(PositiveIntType value) { 
          this.seriesDoses = value;
          return this;
        }

        /**
         * @return The recommended number of doses to achieve immunity.
         */
        public int getSeriesDoses() { 
          return this.seriesDoses == null || this.seriesDoses.isEmpty() ? 0 : this.seriesDoses.getValue();
        }

        /**
         * @param value The recommended number of doses to achieve immunity.
         */
        public ImmunizationVaccinationProtocolComponent setSeriesDoses(int value) { 
            if (this.seriesDoses == null)
              this.seriesDoses = new PositiveIntType();
            this.seriesDoses.setValue(value);
          return this;
        }

        /**
         * @return {@link #doseTarget} (The targeted disease.)
         */
        public CodeableConcept getDoseTarget() { 
          if (this.doseTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationVaccinationProtocolComponent.doseTarget");
            else if (Configuration.doAutoCreate())
              this.doseTarget = new CodeableConcept(); // cc
          return this.doseTarget;
        }

        public boolean hasDoseTarget() { 
          return this.doseTarget != null && !this.doseTarget.isEmpty();
        }

        /**
         * @param value {@link #doseTarget} (The targeted disease.)
         */
        public ImmunizationVaccinationProtocolComponent setDoseTarget(CodeableConcept value) { 
          this.doseTarget = value;
          return this;
        }

        /**
         * @return {@link #doseStatus} (Indicates if the immunization event should "count" against  the protocol.)
         */
        public CodeableConcept getDoseStatus() { 
          if (this.doseStatus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationVaccinationProtocolComponent.doseStatus");
            else if (Configuration.doAutoCreate())
              this.doseStatus = new CodeableConcept(); // cc
          return this.doseStatus;
        }

        public boolean hasDoseStatus() { 
          return this.doseStatus != null && !this.doseStatus.isEmpty();
        }

        /**
         * @param value {@link #doseStatus} (Indicates if the immunization event should "count" against  the protocol.)
         */
        public ImmunizationVaccinationProtocolComponent setDoseStatus(CodeableConcept value) { 
          this.doseStatus = value;
          return this;
        }

        /**
         * @return {@link #doseStatusReason} (Provides an explanation as to why a immunization event should or should not count against the protocol.)
         */
        public CodeableConcept getDoseStatusReason() { 
          if (this.doseStatusReason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationVaccinationProtocolComponent.doseStatusReason");
            else if (Configuration.doAutoCreate())
              this.doseStatusReason = new CodeableConcept(); // cc
          return this.doseStatusReason;
        }

        public boolean hasDoseStatusReason() { 
          return this.doseStatusReason != null && !this.doseStatusReason.isEmpty();
        }

        /**
         * @param value {@link #doseStatusReason} (Provides an explanation as to why a immunization event should or should not count against the protocol.)
         */
        public ImmunizationVaccinationProtocolComponent setDoseStatusReason(CodeableConcept value) { 
          this.doseStatusReason = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("doseSequence", "positiveInt", "Nominal position in a series.", 0, java.lang.Integer.MAX_VALUE, doseSequence));
          childrenList.add(new Property("description", "string", "Contains the description about the protocol under which the vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("authority", "Reference(Organization)", "Indicates the authority who published the protocol?  E.g. ACIP.", 0, java.lang.Integer.MAX_VALUE, authority));
          childrenList.add(new Property("series", "string", "One possible path to achieve presumed immunity against a disease - within the context of an authority.", 0, java.lang.Integer.MAX_VALUE, series));
          childrenList.add(new Property("seriesDoses", "positiveInt", "The recommended number of doses to achieve immunity.", 0, java.lang.Integer.MAX_VALUE, seriesDoses));
          childrenList.add(new Property("doseTarget", "CodeableConcept", "The targeted disease.", 0, java.lang.Integer.MAX_VALUE, doseTarget));
          childrenList.add(new Property("doseStatus", "CodeableConcept", "Indicates if the immunization event should \"count\" against  the protocol.", 0, java.lang.Integer.MAX_VALUE, doseStatus));
          childrenList.add(new Property("doseStatusReason", "CodeableConcept", "Provides an explanation as to why a immunization event should or should not count against the protocol.", 0, java.lang.Integer.MAX_VALUE, doseStatusReason));
        }

      public ImmunizationVaccinationProtocolComponent copy() {
        ImmunizationVaccinationProtocolComponent dst = new ImmunizationVaccinationProtocolComponent();
        copyValues(dst);
        dst.doseSequence = doseSequence == null ? null : doseSequence.copy();
        dst.description = description == null ? null : description.copy();
        dst.authority = authority == null ? null : authority.copy();
        dst.series = series == null ? null : series.copy();
        dst.seriesDoses = seriesDoses == null ? null : seriesDoses.copy();
        dst.doseTarget = doseTarget == null ? null : doseTarget.copy();
        dst.doseStatus = doseStatus == null ? null : doseStatus.copy();
        dst.doseStatusReason = doseStatusReason == null ? null : doseStatusReason.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImmunizationVaccinationProtocolComponent))
          return false;
        ImmunizationVaccinationProtocolComponent o = (ImmunizationVaccinationProtocolComponent) other;
        return compareDeep(doseSequence, o.doseSequence, true) && compareDeep(description, o.description, true)
           && compareDeep(authority, o.authority, true) && compareDeep(series, o.series, true) && compareDeep(seriesDoses, o.seriesDoses, true)
           && compareDeep(doseTarget, o.doseTarget, true) && compareDeep(doseStatus, o.doseStatus, true) && compareDeep(doseStatusReason, o.doseStatusReason, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImmunizationVaccinationProtocolComponent))
          return false;
        ImmunizationVaccinationProtocolComponent o = (ImmunizationVaccinationProtocolComponent) other;
        return compareValues(doseSequence, o.doseSequence, true) && compareValues(description, o.description, true)
           && compareValues(series, o.series, true) && compareValues(seriesDoses, o.seriesDoses, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (doseSequence == null || doseSequence.isEmpty()) && (description == null || description.isEmpty())
           && (authority == null || authority.isEmpty()) && (series == null || series.isEmpty()) && (seriesDoses == null || seriesDoses.isEmpty())
           && (doseTarget == null || doseTarget.isEmpty()) && (doseStatus == null || doseStatus.isEmpty())
           && (doseStatusReason == null || doseStatusReason.isEmpty());
      }

  }

    /**
     * A unique identifier assigned to this immunization record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="A unique identifier assigned to this immunization record." )
    protected List<Identifier> identifier;

    /**
     * Date vaccine administered or was to be administered.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Vaccination administration date", formalDefinition="Date vaccine administered or was to be administered." )
    protected DateTimeType date;

    /**
     * Vaccine that was administered or was to be administered.
     */
    @Child(name = "vaccineType", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Vaccine product administered", formalDefinition="Vaccine that was administered or was to be administered." )
    protected CodeableConcept vaccineType;

    /**
     * The patient who either received or did not receive the immunization.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who was immunized?", formalDefinition="The patient who either received or did not receive the immunization." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient who either received or did not receive the immunization.)
     */
    protected Patient patientTarget;

    /**
     * Indicates if the vaccination was or was not given.
     */
    @Child(name = "wasNotGiven", type = {BooleanType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="Flag for whether immunization was given", formalDefinition="Indicates if the vaccination was or was not given." )
    protected BooleanType wasNotGiven;

    /**
     * True if this administration was reported rather than directly administered.
     */
    @Child(name = "reported", type = {BooleanType.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Is this a self-reported record?", formalDefinition="True if this administration was reported rather than directly administered." )
    protected BooleanType reported;

    /**
     * Clinician who administered the vaccine.
     */
    @Child(name = "performer", type = {Practitioner.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who administered vaccine?", formalDefinition="Clinician who administered the vaccine." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (Clinician who administered the vaccine.)
     */
    protected Practitioner performerTarget;

    /**
     * Clinician who ordered the vaccination.
     */
    @Child(name = "requester", type = {Practitioner.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who ordered vaccination?", formalDefinition="Clinician who ordered the vaccination." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (Clinician who ordered the vaccination.)
     */
    protected Practitioner requesterTarget;

    /**
     * The visit or admission or other contact between patient and health care provider the immunization was performed as part of.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter administered as part of", formalDefinition="The visit or admission or other contact between patient and health care provider the immunization was performed as part of." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The visit or admission or other contact between patient and health care provider the immunization was performed as part of.)
     */
    protected Encounter encounterTarget;

    /**
     * Name of vaccine manufacturer.
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Vaccine manufacturer", formalDefinition="Name of vaccine manufacturer." )
    protected Reference manufacturer;

    /**
     * The actual object that is the target of the reference (Name of vaccine manufacturer.)
     */
    protected Organization manufacturerTarget;

    /**
     * The service delivery location where the vaccine administration occurred.
     */
    @Child(name = "location", type = {Location.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where did vaccination occur?", formalDefinition="The service delivery location where the vaccine administration occurred." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The service delivery location where the vaccine administration occurred.)
     */
    protected Location locationTarget;

    /**
     * Lot number of the  vaccine product.
     */
    @Child(name = "lotNumber", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Vaccine lot number", formalDefinition="Lot number of the  vaccine product." )
    protected StringType lotNumber;

    /**
     * Date vaccine batch expires.
     */
    @Child(name = "expirationDate", type = {DateType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Vaccine expiration date", formalDefinition="Date vaccine batch expires." )
    protected DateType expirationDate;

    /**
     * Body site where vaccine was administered.
     */
    @Child(name = "site", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Body site vaccine  was administered", formalDefinition="Body site where vaccine was administered." )
    protected CodeableConcept site;

    /**
     * The path by which the vaccine product is taken into the body.
     */
    @Child(name = "route", type = {CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="How vaccine entered body", formalDefinition="The path by which the vaccine product is taken into the body." )
    protected CodeableConcept route;

    /**
     * The quantity of vaccine product that was administered.
     */
    @Child(name = "doseQuantity", type = {SimpleQuantity.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Amount of vaccine administered", formalDefinition="The quantity of vaccine product that was administered." )
    protected SimpleQuantity doseQuantity;

    /**
     * Reasons why a vaccine was or was not administered.
     */
    @Child(name = "explanation", type = {}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Administration / non-administration reasons", formalDefinition="Reasons why a vaccine was or was not administered." )
    protected ImmunizationExplanationComponent explanation;

    /**
     * Categorical data indicating that an adverse event is associated in time to an immunization.
     */
    @Child(name = "reaction", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Details of a reaction that follows immunization", formalDefinition="Categorical data indicating that an adverse event is associated in time to an immunization." )
    protected List<ImmunizationReactionComponent> reaction;

    /**
     * Contains information about the protocol(s) under which the vaccine was administered.
     */
    @Child(name = "vaccinationProtocol", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What protocol was followed", formalDefinition="Contains information about the protocol(s) under which the vaccine was administered." )
    protected List<ImmunizationVaccinationProtocolComponent> vaccinationProtocol;

    private static final long serialVersionUID = -224069771L;

  /*
   * Constructor
   */
    public Immunization() {
      super();
    }

  /*
   * Constructor
   */
    public Immunization(DateTimeType date, CodeableConcept vaccineType, Reference patient, BooleanType wasNotGiven, BooleanType reported) {
      super();
      this.date = date;
      this.vaccineType = vaccineType;
      this.patient = patient;
      this.wasNotGiven = wasNotGiven;
      this.reported = reported;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this immunization record.)
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
     * @return {@link #identifier} (A unique identifier assigned to this immunization record.)
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
    public Immunization addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #date} (Date vaccine administered or was to be administered.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.date");
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
     * @param value {@link #date} (Date vaccine administered or was to be administered.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Immunization setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return Date vaccine administered or was to be administered.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value Date vaccine administered or was to be administered.
     */
    public Immunization setDate(Date value) { 
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      return this;
    }

    /**
     * @return {@link #vaccineType} (Vaccine that was administered or was to be administered.)
     */
    public CodeableConcept getVaccineType() { 
      if (this.vaccineType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.vaccineType");
        else if (Configuration.doAutoCreate())
          this.vaccineType = new CodeableConcept(); // cc
      return this.vaccineType;
    }

    public boolean hasVaccineType() { 
      return this.vaccineType != null && !this.vaccineType.isEmpty();
    }

    /**
     * @param value {@link #vaccineType} (Vaccine that was administered or was to be administered.)
     */
    public Immunization setVaccineType(CodeableConcept value) { 
      this.vaccineType = value;
      return this;
    }

    /**
     * @return {@link #patient} (The patient who either received or did not receive the immunization.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient who either received or did not receive the immunization.)
     */
    public Immunization setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who either received or did not receive the immunization.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who either received or did not receive the immunization.)
     */
    public Immunization setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #wasNotGiven} (Indicates if the vaccination was or was not given.). This is the underlying object with id, value and extensions. The accessor "getWasNotGiven" gives direct access to the value
     */
    public BooleanType getWasNotGivenElement() { 
      if (this.wasNotGiven == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.wasNotGiven");
        else if (Configuration.doAutoCreate())
          this.wasNotGiven = new BooleanType(); // bb
      return this.wasNotGiven;
    }

    public boolean hasWasNotGivenElement() { 
      return this.wasNotGiven != null && !this.wasNotGiven.isEmpty();
    }

    public boolean hasWasNotGiven() { 
      return this.wasNotGiven != null && !this.wasNotGiven.isEmpty();
    }

    /**
     * @param value {@link #wasNotGiven} (Indicates if the vaccination was or was not given.). This is the underlying object with id, value and extensions. The accessor "getWasNotGiven" gives direct access to the value
     */
    public Immunization setWasNotGivenElement(BooleanType value) { 
      this.wasNotGiven = value;
      return this;
    }

    /**
     * @return Indicates if the vaccination was or was not given.
     */
    public boolean getWasNotGiven() { 
      return this.wasNotGiven == null || this.wasNotGiven.isEmpty() ? false : this.wasNotGiven.getValue();
    }

    /**
     * @param value Indicates if the vaccination was or was not given.
     */
    public Immunization setWasNotGiven(boolean value) { 
        if (this.wasNotGiven == null)
          this.wasNotGiven = new BooleanType();
        this.wasNotGiven.setValue(value);
      return this;
    }

    /**
     * @return {@link #reported} (True if this administration was reported rather than directly administered.). This is the underlying object with id, value and extensions. The accessor "getReported" gives direct access to the value
     */
    public BooleanType getReportedElement() { 
      if (this.reported == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.reported");
        else if (Configuration.doAutoCreate())
          this.reported = new BooleanType(); // bb
      return this.reported;
    }

    public boolean hasReportedElement() { 
      return this.reported != null && !this.reported.isEmpty();
    }

    public boolean hasReported() { 
      return this.reported != null && !this.reported.isEmpty();
    }

    /**
     * @param value {@link #reported} (True if this administration was reported rather than directly administered.). This is the underlying object with id, value and extensions. The accessor "getReported" gives direct access to the value
     */
    public Immunization setReportedElement(BooleanType value) { 
      this.reported = value;
      return this;
    }

    /**
     * @return True if this administration was reported rather than directly administered.
     */
    public boolean getReported() { 
      return this.reported == null || this.reported.isEmpty() ? false : this.reported.getValue();
    }

    /**
     * @param value True if this administration was reported rather than directly administered.
     */
    public Immunization setReported(boolean value) { 
        if (this.reported == null)
          this.reported = new BooleanType();
        this.reported.setValue(value);
      return this;
    }

    /**
     * @return {@link #performer} (Clinician who administered the vaccine.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (Clinician who administered the vaccine.)
     */
    public Immunization setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Clinician who administered the vaccine.)
     */
    public Practitioner getPerformerTarget() { 
      if (this.performerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.performer");
        else if (Configuration.doAutoCreate())
          this.performerTarget = new Practitioner(); // aa
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Clinician who administered the vaccine.)
     */
    public Immunization setPerformerTarget(Practitioner value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #requester} (Clinician who ordered the vaccination.)
     */
    public Reference getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new Reference(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (Clinician who ordered the vaccination.)
     */
    public Immunization setRequester(Reference value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #requester} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Clinician who ordered the vaccination.)
     */
    public Practitioner getRequesterTarget() { 
      if (this.requesterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.requester");
        else if (Configuration.doAutoCreate())
          this.requesterTarget = new Practitioner(); // aa
      return this.requesterTarget;
    }

    /**
     * @param value {@link #requester} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Clinician who ordered the vaccination.)
     */
    public Immunization setRequesterTarget(Practitioner value) { 
      this.requesterTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The visit or admission or other contact between patient and health care provider the immunization was performed as part of.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The visit or admission or other contact between patient and health care provider the immunization was performed as part of.)
     */
    public Immunization setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The visit or admission or other contact between patient and health care provider the immunization was performed as part of.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The visit or admission or other contact between patient and health care provider the immunization was performed as part of.)
     */
    public Immunization setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #manufacturer} (Name of vaccine manufacturer.)
     */
    public Reference getManufacturer() { 
      if (this.manufacturer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.manufacturer");
        else if (Configuration.doAutoCreate())
          this.manufacturer = new Reference(); // cc
      return this.manufacturer;
    }

    public boolean hasManufacturer() { 
      return this.manufacturer != null && !this.manufacturer.isEmpty();
    }

    /**
     * @param value {@link #manufacturer} (Name of vaccine manufacturer.)
     */
    public Immunization setManufacturer(Reference value) { 
      this.manufacturer = value;
      return this;
    }

    /**
     * @return {@link #manufacturer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Name of vaccine manufacturer.)
     */
    public Organization getManufacturerTarget() { 
      if (this.manufacturerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.manufacturer");
        else if (Configuration.doAutoCreate())
          this.manufacturerTarget = new Organization(); // aa
      return this.manufacturerTarget;
    }

    /**
     * @param value {@link #manufacturer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Name of vaccine manufacturer.)
     */
    public Immunization setManufacturerTarget(Organization value) { 
      this.manufacturerTarget = value;
      return this;
    }

    /**
     * @return {@link #location} (The service delivery location where the vaccine administration occurred.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (The service delivery location where the vaccine administration occurred.)
     */
    public Immunization setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The service delivery location where the vaccine administration occurred.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The service delivery location where the vaccine administration occurred.)
     */
    public Immunization setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #lotNumber} (Lot number of the  vaccine product.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
     */
    public StringType getLotNumberElement() { 
      if (this.lotNumber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.lotNumber");
        else if (Configuration.doAutoCreate())
          this.lotNumber = new StringType(); // bb
      return this.lotNumber;
    }

    public boolean hasLotNumberElement() { 
      return this.lotNumber != null && !this.lotNumber.isEmpty();
    }

    public boolean hasLotNumber() { 
      return this.lotNumber != null && !this.lotNumber.isEmpty();
    }

    /**
     * @param value {@link #lotNumber} (Lot number of the  vaccine product.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
     */
    public Immunization setLotNumberElement(StringType value) { 
      this.lotNumber = value;
      return this;
    }

    /**
     * @return Lot number of the  vaccine product.
     */
    public String getLotNumber() { 
      return this.lotNumber == null ? null : this.lotNumber.getValue();
    }

    /**
     * @param value Lot number of the  vaccine product.
     */
    public Immunization setLotNumber(String value) { 
      if (Utilities.noString(value))
        this.lotNumber = null;
      else {
        if (this.lotNumber == null)
          this.lotNumber = new StringType();
        this.lotNumber.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #expirationDate} (Date vaccine batch expires.). This is the underlying object with id, value and extensions. The accessor "getExpirationDate" gives direct access to the value
     */
    public DateType getExpirationDateElement() { 
      if (this.expirationDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.expirationDate");
        else if (Configuration.doAutoCreate())
          this.expirationDate = new DateType(); // bb
      return this.expirationDate;
    }

    public boolean hasExpirationDateElement() { 
      return this.expirationDate != null && !this.expirationDate.isEmpty();
    }

    public boolean hasExpirationDate() { 
      return this.expirationDate != null && !this.expirationDate.isEmpty();
    }

    /**
     * @param value {@link #expirationDate} (Date vaccine batch expires.). This is the underlying object with id, value and extensions. The accessor "getExpirationDate" gives direct access to the value
     */
    public Immunization setExpirationDateElement(DateType value) { 
      this.expirationDate = value;
      return this;
    }

    /**
     * @return Date vaccine batch expires.
     */
    public Date getExpirationDate() { 
      return this.expirationDate == null ? null : this.expirationDate.getValue();
    }

    /**
     * @param value Date vaccine batch expires.
     */
    public Immunization setExpirationDate(Date value) { 
      if (value == null)
        this.expirationDate = null;
      else {
        if (this.expirationDate == null)
          this.expirationDate = new DateType();
        this.expirationDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #site} (Body site where vaccine was administered.)
     */
    public CodeableConcept getSite() { 
      if (this.site == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.site");
        else if (Configuration.doAutoCreate())
          this.site = new CodeableConcept(); // cc
      return this.site;
    }

    public boolean hasSite() { 
      return this.site != null && !this.site.isEmpty();
    }

    /**
     * @param value {@link #site} (Body site where vaccine was administered.)
     */
    public Immunization setSite(CodeableConcept value) { 
      this.site = value;
      return this;
    }

    /**
     * @return {@link #route} (The path by which the vaccine product is taken into the body.)
     */
    public CodeableConcept getRoute() { 
      if (this.route == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.route");
        else if (Configuration.doAutoCreate())
          this.route = new CodeableConcept(); // cc
      return this.route;
    }

    public boolean hasRoute() { 
      return this.route != null && !this.route.isEmpty();
    }

    /**
     * @param value {@link #route} (The path by which the vaccine product is taken into the body.)
     */
    public Immunization setRoute(CodeableConcept value) { 
      this.route = value;
      return this;
    }

    /**
     * @return {@link #doseQuantity} (The quantity of vaccine product that was administered.)
     */
    public SimpleQuantity getDoseQuantity() { 
      if (this.doseQuantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.doseQuantity");
        else if (Configuration.doAutoCreate())
          this.doseQuantity = new SimpleQuantity(); // cc
      return this.doseQuantity;
    }

    public boolean hasDoseQuantity() { 
      return this.doseQuantity != null && !this.doseQuantity.isEmpty();
    }

    /**
     * @param value {@link #doseQuantity} (The quantity of vaccine product that was administered.)
     */
    public Immunization setDoseQuantity(SimpleQuantity value) { 
      this.doseQuantity = value;
      return this;
    }

    /**
     * @return {@link #explanation} (Reasons why a vaccine was or was not administered.)
     */
    public ImmunizationExplanationComponent getExplanation() { 
      if (this.explanation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.explanation");
        else if (Configuration.doAutoCreate())
          this.explanation = new ImmunizationExplanationComponent(); // cc
      return this.explanation;
    }

    public boolean hasExplanation() { 
      return this.explanation != null && !this.explanation.isEmpty();
    }

    /**
     * @param value {@link #explanation} (Reasons why a vaccine was or was not administered.)
     */
    public Immunization setExplanation(ImmunizationExplanationComponent value) { 
      this.explanation = value;
      return this;
    }

    /**
     * @return {@link #reaction} (Categorical data indicating that an adverse event is associated in time to an immunization.)
     */
    public List<ImmunizationReactionComponent> getReaction() { 
      if (this.reaction == null)
        this.reaction = new ArrayList<ImmunizationReactionComponent>();
      return this.reaction;
    }

    public boolean hasReaction() { 
      if (this.reaction == null)
        return false;
      for (ImmunizationReactionComponent item : this.reaction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reaction} (Categorical data indicating that an adverse event is associated in time to an immunization.)
     */
    // syntactic sugar
    public ImmunizationReactionComponent addReaction() { //3
      ImmunizationReactionComponent t = new ImmunizationReactionComponent();
      if (this.reaction == null)
        this.reaction = new ArrayList<ImmunizationReactionComponent>();
      this.reaction.add(t);
      return t;
    }

    // syntactic sugar
    public Immunization addReaction(ImmunizationReactionComponent t) { //3
      if (t == null)
        return this;
      if (this.reaction == null)
        this.reaction = new ArrayList<ImmunizationReactionComponent>();
      this.reaction.add(t);
      return this;
    }

    /**
     * @return {@link #vaccinationProtocol} (Contains information about the protocol(s) under which the vaccine was administered.)
     */
    public List<ImmunizationVaccinationProtocolComponent> getVaccinationProtocol() { 
      if (this.vaccinationProtocol == null)
        this.vaccinationProtocol = new ArrayList<ImmunizationVaccinationProtocolComponent>();
      return this.vaccinationProtocol;
    }

    public boolean hasVaccinationProtocol() { 
      if (this.vaccinationProtocol == null)
        return false;
      for (ImmunizationVaccinationProtocolComponent item : this.vaccinationProtocol)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #vaccinationProtocol} (Contains information about the protocol(s) under which the vaccine was administered.)
     */
    // syntactic sugar
    public ImmunizationVaccinationProtocolComponent addVaccinationProtocol() { //3
      ImmunizationVaccinationProtocolComponent t = new ImmunizationVaccinationProtocolComponent();
      if (this.vaccinationProtocol == null)
        this.vaccinationProtocol = new ArrayList<ImmunizationVaccinationProtocolComponent>();
      this.vaccinationProtocol.add(t);
      return t;
    }

    // syntactic sugar
    public Immunization addVaccinationProtocol(ImmunizationVaccinationProtocolComponent t) { //3
      if (t == null)
        return this;
      if (this.vaccinationProtocol == null)
        this.vaccinationProtocol = new ArrayList<ImmunizationVaccinationProtocolComponent>();
      this.vaccinationProtocol.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique identifier assigned to this immunization record.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("date", "dateTime", "Date vaccine administered or was to be administered.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("vaccineType", "CodeableConcept", "Vaccine that was administered or was to be administered.", 0, java.lang.Integer.MAX_VALUE, vaccineType));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient who either received or did not receive the immunization.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("wasNotGiven", "boolean", "Indicates if the vaccination was or was not given.", 0, java.lang.Integer.MAX_VALUE, wasNotGiven));
        childrenList.add(new Property("reported", "boolean", "True if this administration was reported rather than directly administered.", 0, java.lang.Integer.MAX_VALUE, reported));
        childrenList.add(new Property("performer", "Reference(Practitioner)", "Clinician who administered the vaccine.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("requester", "Reference(Practitioner)", "Clinician who ordered the vaccination.", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The visit or admission or other contact between patient and health care provider the immunization was performed as part of.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("manufacturer", "Reference(Organization)", "Name of vaccine manufacturer.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        childrenList.add(new Property("location", "Reference(Location)", "The service delivery location where the vaccine administration occurred.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("lotNumber", "string", "Lot number of the  vaccine product.", 0, java.lang.Integer.MAX_VALUE, lotNumber));
        childrenList.add(new Property("expirationDate", "date", "Date vaccine batch expires.", 0, java.lang.Integer.MAX_VALUE, expirationDate));
        childrenList.add(new Property("site", "CodeableConcept", "Body site where vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, site));
        childrenList.add(new Property("route", "CodeableConcept", "The path by which the vaccine product is taken into the body.", 0, java.lang.Integer.MAX_VALUE, route));
        childrenList.add(new Property("doseQuantity", "SimpleQuantity", "The quantity of vaccine product that was administered.", 0, java.lang.Integer.MAX_VALUE, doseQuantity));
        childrenList.add(new Property("explanation", "", "Reasons why a vaccine was or was not administered.", 0, java.lang.Integer.MAX_VALUE, explanation));
        childrenList.add(new Property("reaction", "", "Categorical data indicating that an adverse event is associated in time to an immunization.", 0, java.lang.Integer.MAX_VALUE, reaction));
        childrenList.add(new Property("vaccinationProtocol", "", "Contains information about the protocol(s) under which the vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, vaccinationProtocol));
      }

      public Immunization copy() {
        Immunization dst = new Immunization();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.vaccineType = vaccineType == null ? null : vaccineType.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.wasNotGiven = wasNotGiven == null ? null : wasNotGiven.copy();
        dst.reported = reported == null ? null : reported.copy();
        dst.performer = performer == null ? null : performer.copy();
        dst.requester = requester == null ? null : requester.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.manufacturer = manufacturer == null ? null : manufacturer.copy();
        dst.location = location == null ? null : location.copy();
        dst.lotNumber = lotNumber == null ? null : lotNumber.copy();
        dst.expirationDate = expirationDate == null ? null : expirationDate.copy();
        dst.site = site == null ? null : site.copy();
        dst.route = route == null ? null : route.copy();
        dst.doseQuantity = doseQuantity == null ? null : doseQuantity.copy();
        dst.explanation = explanation == null ? null : explanation.copy();
        if (reaction != null) {
          dst.reaction = new ArrayList<ImmunizationReactionComponent>();
          for (ImmunizationReactionComponent i : reaction)
            dst.reaction.add(i.copy());
        };
        if (vaccinationProtocol != null) {
          dst.vaccinationProtocol = new ArrayList<ImmunizationVaccinationProtocolComponent>();
          for (ImmunizationVaccinationProtocolComponent i : vaccinationProtocol)
            dst.vaccinationProtocol.add(i.copy());
        };
        return dst;
      }

      protected Immunization typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Immunization))
          return false;
        Immunization o = (Immunization) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(date, o.date, true) && compareDeep(vaccineType, o.vaccineType, true)
           && compareDeep(patient, o.patient, true) && compareDeep(wasNotGiven, o.wasNotGiven, true) && compareDeep(reported, o.reported, true)
           && compareDeep(performer, o.performer, true) && compareDeep(requester, o.requester, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(manufacturer, o.manufacturer, true) && compareDeep(location, o.location, true) && compareDeep(lotNumber, o.lotNumber, true)
           && compareDeep(expirationDate, o.expirationDate, true) && compareDeep(site, o.site, true) && compareDeep(route, o.route, true)
           && compareDeep(doseQuantity, o.doseQuantity, true) && compareDeep(explanation, o.explanation, true)
           && compareDeep(reaction, o.reaction, true) && compareDeep(vaccinationProtocol, o.vaccinationProtocol, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Immunization))
          return false;
        Immunization o = (Immunization) other;
        return compareValues(date, o.date, true) && compareValues(wasNotGiven, o.wasNotGiven, true) && compareValues(reported, o.reported, true)
           && compareValues(lotNumber, o.lotNumber, true) && compareValues(expirationDate, o.expirationDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (date == null || date.isEmpty())
           && (vaccineType == null || vaccineType.isEmpty()) && (patient == null || patient.isEmpty())
           && (wasNotGiven == null || wasNotGiven.isEmpty()) && (reported == null || reported.isEmpty())
           && (performer == null || performer.isEmpty()) && (requester == null || requester.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (manufacturer == null || manufacturer.isEmpty())
           && (location == null || location.isEmpty()) && (lotNumber == null || lotNumber.isEmpty())
           && (expirationDate == null || expirationDate.isEmpty()) && (site == null || site.isEmpty())
           && (route == null || route.isEmpty()) && (doseQuantity == null || doseQuantity.isEmpty())
           && (explanation == null || explanation.isEmpty()) && (reaction == null || reaction.isEmpty())
           && (vaccinationProtocol == null || vaccinationProtocol.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Immunization;
   }

  @SearchParamDefinition(name="date", path="Immunization.date", description="Vaccination  (non)-Administration Date", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="requester", path="Immunization.requester", description="The practitioner who ordered the vaccination", type="reference" )
  public static final String SP_REQUESTER = "requester";
  @SearchParamDefinition(name="identifier", path="Immunization.identifier", description="Business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="reason", path="Immunization.explanation.reason", description="Why immunization occurred", type="token" )
  public static final String SP_REASON = "reason";
  @SearchParamDefinition(name="performer", path="Immunization.performer", description="The practitioner who administered the vaccination", type="reference" )
  public static final String SP_PERFORMER = "performer";
  @SearchParamDefinition(name="reaction", path="Immunization.reaction.detail", description="Additional information on reaction", type="reference" )
  public static final String SP_REACTION = "reaction";
  @SearchParamDefinition(name="lot-number", path="Immunization.lotNumber", description="Vaccine Lot Number", type="string" )
  public static final String SP_LOTNUMBER = "lot-number";
  @SearchParamDefinition(name="subject", path="Immunization.patient", description="The patient for the vaccination record", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="vaccine-type", path="Immunization.vaccineType", description="Vaccine Product Type Administered", type="token" )
  public static final String SP_VACCINETYPE = "vaccine-type";
  @SearchParamDefinition(name="notgiven", path="Immunization.wasNotGiven", description="Administrations which were not given", type="token" )
  public static final String SP_NOTGIVEN = "notgiven";
  @SearchParamDefinition(name="manufacturer", path="Immunization.manufacturer", description="Vaccine Manufacturer", type="reference" )
  public static final String SP_MANUFACTURER = "manufacturer";
  @SearchParamDefinition(name="dose-sequence", path="Immunization.vaccinationProtocol.doseSequence", description="What dose number within series?", type="number" )
  public static final String SP_DOSESEQUENCE = "dose-sequence";
  @SearchParamDefinition(name="patient", path="Immunization.patient", description="The patient for the vaccination record", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="reason-not-given", path="Immunization.explanation.reasonNotGiven", description="Explanation of reason vaccination was not administered", type="token" )
  public static final String SP_REASONNOTGIVEN = "reason-not-given";
  @SearchParamDefinition(name="location", path="Immunization.location", description="The service delivery location or facility in which the vaccine was / was to be administered", type="reference" )
  public static final String SP_LOCATION = "location";
  @SearchParamDefinition(name="reaction-date", path="Immunization.reaction.date", description="When did reaction start?", type="date" )
  public static final String SP_REACTIONDATE = "reaction-date";

}

