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
 * Describes the event of a patient being administered a vaccination or a record of a vaccination as reported by a patient, a clinician or another party and may include vaccine reaction information and what vaccination protocol was followed.
 */
@ResourceDef(name="Immunization", profile="http://hl7.org/fhir/Profile/Immunization")
public class Immunization extends DomainResource {

    @Block()
    public static class ImmunizationExplanationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reasons why a vaccine was administered.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Why immunization occurred", formalDefinition="Reasons why a vaccine was administered." )
        protected List<CodeableConcept> reason;

        /**
         * Reason why a vaccine was not administered.
         */
        @Child(name = "reasonNotGiven", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Why immunization did not occur", formalDefinition="Reason why a vaccine was not administered." )
        protected List<CodeableConcept> reasonNotGiven;

        private static final long serialVersionUID = -539821866L;

    /**
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // CodeableConcept
        case 2101123790: /*reasonNotGiven*/ return this.reasonNotGiven == null ? new Base[0] : this.reasonNotGiven.toArray(new Base[this.reasonNotGiven.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -934964668: // reason
          this.getReason().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 2101123790: // reasonNotGiven
          this.getReasonNotGiven().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reason"))
          this.getReason().add(castToCodeableConcept(value));
        else if (name.equals("reasonNotGiven"))
          this.getReasonNotGiven().add(castToCodeableConcept(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -934964668:  return addReason(); // CodeableConcept
        case 2101123790:  return addReasonNotGiven(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("reasonNotGiven")) {
          return addReasonNotGiven();
        }
        else
          return super.addChild(name);
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

  public String fhirType() {
    return "Immunization.explanation";

  }

  }

    @Block()
    public static class ImmunizationReactionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Date of reaction to the immunization.
         */
        @Child(name = "date", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When reaction started", formalDefinition="Date of reaction to the immunization." )
        protected DateTimeType date;

        /**
         * Details of the reaction.
         */
        @Child(name = "detail", type = {Observation.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional information on reaction", formalDefinition="Details of the reaction." )
        protected Reference detail;

        /**
         * The actual object that is the target of the reference (Details of the reaction.)
         */
        protected Observation detailTarget;

        /**
         * Self-reported indicator.
         */
        @Child(name = "reported", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Indicates self-reported reaction", formalDefinition="Self-reported indicator." )
        protected BooleanType reported;

        private static final long serialVersionUID = -1297668556L;

    /**
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : new Base[] {this.detail}; // Reference
        case -427039533: /*reported*/ return this.reported == null ? new Base[0] : new Base[] {this.reported}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case -1335224239: // detail
          this.detail = castToReference(value); // Reference
          break;
        case -427039533: // reported
          this.reported = castToBoolean(value); // BooleanType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("detail"))
          this.detail = castToReference(value); // Reference
        else if (name.equals("reported"))
          this.reported = castToBoolean(value); // BooleanType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case -1335224239:  return getDetail(); // Reference
        case -427039533: throw new FHIRException("Cannot make property reported as it is not a complex type"); // BooleanType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.date");
        }
        else if (name.equals("detail")) {
          this.detail = new Reference();
          return this.detail;
        }
        else if (name.equals("reported")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.reported");
        }
        else
          return super.addChild(name);
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

  public String fhirType() {
    return "Immunization.reaction";

  }

  }

    @Block()
    public static class ImmunizationVaccinationProtocolComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Nominal position in a series.
         */
        @Child(name = "doseSequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Dose number within series", formalDefinition="Nominal position in a series." )
        protected PositiveIntType doseSequence;

        /**
         * Contains the description about the protocol under which the vaccine was administered.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Details of vaccine protocol", formalDefinition="Contains the description about the protocol under which the vaccine was administered." )
        protected StringType description;

        /**
         * Indicates the authority who published the protocol.  E.g. ACIP.
         */
        @Child(name = "authority", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who is responsible for protocol", formalDefinition="Indicates the authority who published the protocol.  E.g. ACIP." )
        protected Reference authority;

        /**
         * The actual object that is the target of the reference (Indicates the authority who published the protocol.  E.g. ACIP.)
         */
        protected Organization authorityTarget;

        /**
         * One possible path to achieve presumed immunity against a disease - within the context of an authority.
         */
        @Child(name = "series", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name of vaccine series", formalDefinition="One possible path to achieve presumed immunity against a disease - within the context of an authority." )
        protected StringType series;

        /**
         * The recommended number of doses to achieve immunity.
         */
        @Child(name = "seriesDoses", type = {PositiveIntType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Recommended number of doses for immunity", formalDefinition="The recommended number of doses to achieve immunity." )
        protected PositiveIntType seriesDoses;

        /**
         * The targeted disease.
         */
        @Child(name = "targetDisease", type = {CodeableConcept.class}, order=6, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Disease immunized against", formalDefinition="The targeted disease." )
        protected List<CodeableConcept> targetDisease;

        /**
         * Indicates if the immunization event should "count" against  the protocol.
         */
        @Child(name = "doseStatus", type = {CodeableConcept.class}, order=7, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Indicates if dose counts towards immunity", formalDefinition="Indicates if the immunization event should \"count\" against  the protocol." )
        protected CodeableConcept doseStatus;

        /**
         * Provides an explanation as to why an immunization event should or should not count against the protocol.
         */
        @Child(name = "doseStatusReason", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Why dose does (not) count", formalDefinition="Provides an explanation as to why an immunization event should or should not count against the protocol." )
        protected CodeableConcept doseStatusReason;

        private static final long serialVersionUID = 386814037L;

    /**
     * Constructor
     */
      public ImmunizationVaccinationProtocolComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImmunizationVaccinationProtocolComponent(PositiveIntType doseSequence, CodeableConcept doseStatus) {
        super();
        this.doseSequence = doseSequence;
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
         * @return {@link #authority} (Indicates the authority who published the protocol.  E.g. ACIP.)
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
         * @param value {@link #authority} (Indicates the authority who published the protocol.  E.g. ACIP.)
         */
        public ImmunizationVaccinationProtocolComponent setAuthority(Reference value) { 
          this.authority = value;
          return this;
        }

        /**
         * @return {@link #authority} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol.  E.g. ACIP.)
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
         * @param value {@link #authority} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol.  E.g. ACIP.)
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
         * @return {@link #targetDisease} (The targeted disease.)
         */
        public List<CodeableConcept> getTargetDisease() { 
          if (this.targetDisease == null)
            this.targetDisease = new ArrayList<CodeableConcept>();
          return this.targetDisease;
        }

        public boolean hasTargetDisease() { 
          if (this.targetDisease == null)
            return false;
          for (CodeableConcept item : this.targetDisease)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #targetDisease} (The targeted disease.)
         */
    // syntactic sugar
        public CodeableConcept addTargetDisease() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.targetDisease == null)
            this.targetDisease = new ArrayList<CodeableConcept>();
          this.targetDisease.add(t);
          return t;
        }

    // syntactic sugar
        public ImmunizationVaccinationProtocolComponent addTargetDisease(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.targetDisease == null)
            this.targetDisease = new ArrayList<CodeableConcept>();
          this.targetDisease.add(t);
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
         * @return {@link #doseStatusReason} (Provides an explanation as to why an immunization event should or should not count against the protocol.)
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
         * @param value {@link #doseStatusReason} (Provides an explanation as to why an immunization event should or should not count against the protocol.)
         */
        public ImmunizationVaccinationProtocolComponent setDoseStatusReason(CodeableConcept value) { 
          this.doseStatusReason = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("doseSequence", "positiveInt", "Nominal position in a series.", 0, java.lang.Integer.MAX_VALUE, doseSequence));
          childrenList.add(new Property("description", "string", "Contains the description about the protocol under which the vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("authority", "Reference(Organization)", "Indicates the authority who published the protocol.  E.g. ACIP.", 0, java.lang.Integer.MAX_VALUE, authority));
          childrenList.add(new Property("series", "string", "One possible path to achieve presumed immunity against a disease - within the context of an authority.", 0, java.lang.Integer.MAX_VALUE, series));
          childrenList.add(new Property("seriesDoses", "positiveInt", "The recommended number of doses to achieve immunity.", 0, java.lang.Integer.MAX_VALUE, seriesDoses));
          childrenList.add(new Property("targetDisease", "CodeableConcept", "The targeted disease.", 0, java.lang.Integer.MAX_VALUE, targetDisease));
          childrenList.add(new Property("doseStatus", "CodeableConcept", "Indicates if the immunization event should \"count\" against  the protocol.", 0, java.lang.Integer.MAX_VALUE, doseStatus));
          childrenList.add(new Property("doseStatusReason", "CodeableConcept", "Provides an explanation as to why an immunization event should or should not count against the protocol.", 0, java.lang.Integer.MAX_VALUE, doseStatusReason));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 550933246: /*doseSequence*/ return this.doseSequence == null ? new Base[0] : new Base[] {this.doseSequence}; // PositiveIntType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 1475610435: /*authority*/ return this.authority == null ? new Base[0] : new Base[] {this.authority}; // Reference
        case -905838985: /*series*/ return this.series == null ? new Base[0] : new Base[] {this.series}; // StringType
        case -1936727105: /*seriesDoses*/ return this.seriesDoses == null ? new Base[0] : new Base[] {this.seriesDoses}; // PositiveIntType
        case -319593813: /*targetDisease*/ return this.targetDisease == null ? new Base[0] : this.targetDisease.toArray(new Base[this.targetDisease.size()]); // CodeableConcept
        case -745826705: /*doseStatus*/ return this.doseStatus == null ? new Base[0] : new Base[] {this.doseStatus}; // CodeableConcept
        case 662783379: /*doseStatusReason*/ return this.doseStatusReason == null ? new Base[0] : new Base[] {this.doseStatusReason}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 550933246: // doseSequence
          this.doseSequence = castToPositiveInt(value); // PositiveIntType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 1475610435: // authority
          this.authority = castToReference(value); // Reference
          break;
        case -905838985: // series
          this.series = castToString(value); // StringType
          break;
        case -1936727105: // seriesDoses
          this.seriesDoses = castToPositiveInt(value); // PositiveIntType
          break;
        case -319593813: // targetDisease
          this.getTargetDisease().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -745826705: // doseStatus
          this.doseStatus = castToCodeableConcept(value); // CodeableConcept
          break;
        case 662783379: // doseStatusReason
          this.doseStatusReason = castToCodeableConcept(value); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("doseSequence"))
          this.doseSequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("authority"))
          this.authority = castToReference(value); // Reference
        else if (name.equals("series"))
          this.series = castToString(value); // StringType
        else if (name.equals("seriesDoses"))
          this.seriesDoses = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("targetDisease"))
          this.getTargetDisease().add(castToCodeableConcept(value));
        else if (name.equals("doseStatus"))
          this.doseStatus = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("doseStatusReason"))
          this.doseStatusReason = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 550933246: throw new FHIRException("Cannot make property doseSequence as it is not a complex type"); // PositiveIntType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 1475610435:  return getAuthority(); // Reference
        case -905838985: throw new FHIRException("Cannot make property series as it is not a complex type"); // StringType
        case -1936727105: throw new FHIRException("Cannot make property seriesDoses as it is not a complex type"); // PositiveIntType
        case -319593813:  return addTargetDisease(); // CodeableConcept
        case -745826705:  return getDoseStatus(); // CodeableConcept
        case 662783379:  return getDoseStatusReason(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("doseSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.doseSequence");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.description");
        }
        else if (name.equals("authority")) {
          this.authority = new Reference();
          return this.authority;
        }
        else if (name.equals("series")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.series");
        }
        else if (name.equals("seriesDoses")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.seriesDoses");
        }
        else if (name.equals("targetDisease")) {
          return addTargetDisease();
        }
        else if (name.equals("doseStatus")) {
          this.doseStatus = new CodeableConcept();
          return this.doseStatus;
        }
        else if (name.equals("doseStatusReason")) {
          this.doseStatusReason = new CodeableConcept();
          return this.doseStatusReason;
        }
        else
          return super.addChild(name);
      }

      public ImmunizationVaccinationProtocolComponent copy() {
        ImmunizationVaccinationProtocolComponent dst = new ImmunizationVaccinationProtocolComponent();
        copyValues(dst);
        dst.doseSequence = doseSequence == null ? null : doseSequence.copy();
        dst.description = description == null ? null : description.copy();
        dst.authority = authority == null ? null : authority.copy();
        dst.series = series == null ? null : series.copy();
        dst.seriesDoses = seriesDoses == null ? null : seriesDoses.copy();
        if (targetDisease != null) {
          dst.targetDisease = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : targetDisease)
            dst.targetDisease.add(i.copy());
        };
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
           && compareDeep(targetDisease, o.targetDisease, true) && compareDeep(doseStatus, o.doseStatus, true)
           && compareDeep(doseStatusReason, o.doseStatusReason, true);
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
           && (targetDisease == null || targetDisease.isEmpty()) && (doseStatus == null || doseStatus.isEmpty())
           && (doseStatusReason == null || doseStatusReason.isEmpty());
      }

  public String fhirType() {
    return "Immunization.vaccinationProtocol";

  }

  }

    /**
     * A unique identifier assigned to this immunization record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business identifier", formalDefinition="A unique identifier assigned to this immunization record." )
    protected List<Identifier> identifier;

    /**
     * Indicates the current status of the vaccination event.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | on-hold | completed | entered-in-error | stopped", formalDefinition="Indicates the current status of the vaccination event." )
    protected CodeType status;

    /**
     * Date vaccine administered or was to be administered.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Vaccination administration date", formalDefinition="Date vaccine administered or was to be administered." )
    protected DateTimeType date;

    /**
     * Vaccine that was administered or was to be administered.
     */
    @Child(name = "vaccineCode", type = {CodeableConcept.class}, order=3, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Vaccine product administered", formalDefinition="Vaccine that was administered or was to be administered." )
    protected CodeableConcept vaccineCode;

    /**
     * The patient who either received or did not receive the immunization.
     */
    @Child(name = "patient", type = {Patient.class}, order=4, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Who was immunized", formalDefinition="The patient who either received or did not receive the immunization." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient who either received or did not receive the immunization.)
     */
    protected Patient patientTarget;

    /**
     * Indicates if the vaccination was or was not given.
     */
    @Child(name = "wasNotGiven", type = {BooleanType.class}, order=5, min=1, max=1, modifier=true, summary=false)
    @Description(shortDefinition="Flag for whether immunization was given", formalDefinition="Indicates if the vaccination was or was not given." )
    protected BooleanType wasNotGiven;

    /**
     * True if this administration was reported rather than directly administered.
     */
    @Child(name = "reported", type = {BooleanType.class}, order=6, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Indicates a self-reported record", formalDefinition="True if this administration was reported rather than directly administered." )
    protected BooleanType reported;

    /**
     * Clinician who administered the vaccine.
     */
    @Child(name = "performer", type = {Practitioner.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Who administered vaccine", formalDefinition="Clinician who administered the vaccine." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (Clinician who administered the vaccine.)
     */
    protected Practitioner performerTarget;

    /**
     * Clinician who ordered the vaccination.
     */
    @Child(name = "requester", type = {Practitioner.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Who ordered vaccination", formalDefinition="Clinician who ordered the vaccination." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (Clinician who ordered the vaccination.)
     */
    protected Practitioner requesterTarget;

    /**
     * The visit or admission or other contact between patient and health care provider the immunization was performed as part of.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Encounter administered as part of", formalDefinition="The visit or admission or other contact between patient and health care provider the immunization was performed as part of." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The visit or admission or other contact between patient and health care provider the immunization was performed as part of.)
     */
    protected Encounter encounterTarget;

    /**
     * Name of vaccine manufacturer.
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Vaccine manufacturer", formalDefinition="Name of vaccine manufacturer." )
    protected Reference manufacturer;

    /**
     * The actual object that is the target of the reference (Name of vaccine manufacturer.)
     */
    protected Organization manufacturerTarget;

    /**
     * The service delivery location where the vaccine administration occurred.
     */
    @Child(name = "location", type = {Location.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Where vaccination occurred", formalDefinition="The service delivery location where the vaccine administration occurred." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The service delivery location where the vaccine administration occurred.)
     */
    protected Location locationTarget;

    /**
     * Lot number of the  vaccine product.
     */
    @Child(name = "lotNumber", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Vaccine lot number", formalDefinition="Lot number of the  vaccine product." )
    protected StringType lotNumber;

    /**
     * Date vaccine batch expires.
     */
    @Child(name = "expirationDate", type = {DateType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Vaccine expiration date", formalDefinition="Date vaccine batch expires." )
    protected DateType expirationDate;

    /**
     * Body site where vaccine was administered.
     */
    @Child(name = "site", type = {CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Body site vaccine  was administered", formalDefinition="Body site where vaccine was administered." )
    protected CodeableConcept site;

    /**
     * The path by which the vaccine product is taken into the body.
     */
    @Child(name = "route", type = {CodeableConcept.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How vaccine entered body", formalDefinition="The path by which the vaccine product is taken into the body." )
    protected CodeableConcept route;

    /**
     * The quantity of vaccine product that was administered.
     */
    @Child(name = "doseQuantity", type = {SimpleQuantity.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Amount of vaccine administered", formalDefinition="The quantity of vaccine product that was administered." )
    protected SimpleQuantity doseQuantity;

    /**
     * Extra information about the immunization that is not conveyed by the other attributes.
     */
    @Child(name = "note", type = {Annotation.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Vaccination notes", formalDefinition="Extra information about the immunization that is not conveyed by the other attributes." )
    protected List<Annotation> note;

    /**
     * Reasons why a vaccine was or was not administered.
     */
    @Child(name = "explanation", type = {}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Administration/non-administration reasons", formalDefinition="Reasons why a vaccine was or was not administered." )
    protected ImmunizationExplanationComponent explanation;

    /**
     * Categorical data indicating that an adverse event is associated in time to an immunization.
     */
    @Child(name = "reaction", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Details of a reaction that follows immunization", formalDefinition="Categorical data indicating that an adverse event is associated in time to an immunization." )
    protected List<ImmunizationReactionComponent> reaction;

    /**
     * Contains information about the protocol(s) under which the vaccine was administered.
     */
    @Child(name = "vaccinationProtocol", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What protocol was followed", formalDefinition="Contains information about the protocol(s) under which the vaccine was administered." )
    protected List<ImmunizationVaccinationProtocolComponent> vaccinationProtocol;

    private static final long serialVersionUID = 898786200L;

  /**
   * Constructor
   */
    public Immunization() {
      super();
    }

  /**
   * Constructor
   */
    public Immunization(CodeType status, CodeableConcept vaccineCode, Reference patient, BooleanType wasNotGiven, BooleanType reported) {
      super();
      this.status = status;
      this.vaccineCode = vaccineCode;
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
     * @return {@link #status} (Indicates the current status of the vaccination event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CodeType getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.status");
        else if (Configuration.doAutoCreate())
          this.status = new CodeType(); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates the current status of the vaccination event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Immunization setStatusElement(CodeType value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates the current status of the vaccination event.
     */
    public String getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates the current status of the vaccination event.
     */
    public Immunization setStatus(String value) { 
        if (this.status == null)
          this.status = new CodeType();
        this.status.setValue(value);
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
     * @return {@link #vaccineCode} (Vaccine that was administered or was to be administered.)
     */
    public CodeableConcept getVaccineCode() { 
      if (this.vaccineCode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Immunization.vaccineCode");
        else if (Configuration.doAutoCreate())
          this.vaccineCode = new CodeableConcept(); // cc
      return this.vaccineCode;
    }

    public boolean hasVaccineCode() { 
      return this.vaccineCode != null && !this.vaccineCode.isEmpty();
    }

    /**
     * @param value {@link #vaccineCode} (Vaccine that was administered or was to be administered.)
     */
    public Immunization setVaccineCode(CodeableConcept value) { 
      this.vaccineCode = value;
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
     * @return {@link #note} (Extra information about the immunization that is not conveyed by the other attributes.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #note} (Extra information about the immunization that is not conveyed by the other attributes.)
     */
    // syntactic sugar
    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    // syntactic sugar
    public Immunization addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
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
        childrenList.add(new Property("status", "code", "Indicates the current status of the vaccination event.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("date", "dateTime", "Date vaccine administered or was to be administered.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("vaccineCode", "CodeableConcept", "Vaccine that was administered or was to be administered.", 0, java.lang.Integer.MAX_VALUE, vaccineCode));
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
        childrenList.add(new Property("note", "Annotation", "Extra information about the immunization that is not conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("explanation", "", "Reasons why a vaccine was or was not administered.", 0, java.lang.Integer.MAX_VALUE, explanation));
        childrenList.add(new Property("reaction", "", "Categorical data indicating that an adverse event is associated in time to an immunization.", 0, java.lang.Integer.MAX_VALUE, reaction));
        childrenList.add(new Property("vaccinationProtocol", "", "Contains information about the protocol(s) under which the vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, vaccinationProtocol));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 664556354: /*vaccineCode*/ return this.vaccineCode == null ? new Base[0] : new Base[] {this.vaccineCode}; // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -1050911117: /*wasNotGiven*/ return this.wasNotGiven == null ? new Base[0] : new Base[] {this.wasNotGiven}; // BooleanType
        case -427039533: /*reported*/ return this.reported == null ? new Base[0] : new Base[] {this.reported}; // BooleanType
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : new Base[] {this.manufacturer}; // Reference
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case 462547450: /*lotNumber*/ return this.lotNumber == null ? new Base[0] : new Base[] {this.lotNumber}; // StringType
        case -668811523: /*expirationDate*/ return this.expirationDate == null ? new Base[0] : new Base[] {this.expirationDate}; // DateType
        case 3530567: /*site*/ return this.site == null ? new Base[0] : new Base[] {this.site}; // CodeableConcept
        case 108704329: /*route*/ return this.route == null ? new Base[0] : new Base[] {this.route}; // CodeableConcept
        case -2083618872: /*doseQuantity*/ return this.doseQuantity == null ? new Base[0] : new Base[] {this.doseQuantity}; // SimpleQuantity
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1105867239: /*explanation*/ return this.explanation == null ? new Base[0] : new Base[] {this.explanation}; // ImmunizationExplanationComponent
        case -867509719: /*reaction*/ return this.reaction == null ? new Base[0] : this.reaction.toArray(new Base[this.reaction.size()]); // ImmunizationReactionComponent
        case -179633155: /*vaccinationProtocol*/ return this.vaccinationProtocol == null ? new Base[0] : this.vaccinationProtocol.toArray(new Base[this.vaccinationProtocol.size()]); // ImmunizationVaccinationProtocolComponent
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
          this.status = castToCode(value); // CodeType
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case 664556354: // vaccineCode
          this.vaccineCode = castToCodeableConcept(value); // CodeableConcept
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -1050911117: // wasNotGiven
          this.wasNotGiven = castToBoolean(value); // BooleanType
          break;
        case -427039533: // reported
          this.reported = castToBoolean(value); // BooleanType
          break;
        case 481140686: // performer
          this.performer = castToReference(value); // Reference
          break;
        case 693933948: // requester
          this.requester = castToReference(value); // Reference
          break;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          break;
        case -1969347631: // manufacturer
          this.manufacturer = castToReference(value); // Reference
          break;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          break;
        case 462547450: // lotNumber
          this.lotNumber = castToString(value); // StringType
          break;
        case -668811523: // expirationDate
          this.expirationDate = castToDate(value); // DateType
          break;
        case 3530567: // site
          this.site = castToCodeableConcept(value); // CodeableConcept
          break;
        case 108704329: // route
          this.route = castToCodeableConcept(value); // CodeableConcept
          break;
        case -2083618872: // doseQuantity
          this.doseQuantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case -1105867239: // explanation
          this.explanation = (ImmunizationExplanationComponent) value; // ImmunizationExplanationComponent
          break;
        case -867509719: // reaction
          this.getReaction().add((ImmunizationReactionComponent) value); // ImmunizationReactionComponent
          break;
        case -179633155: // vaccinationProtocol
          this.getVaccinationProtocol().add((ImmunizationVaccinationProtocolComponent) value); // ImmunizationVaccinationProtocolComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = castToCode(value); // CodeType
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("vaccineCode"))
          this.vaccineCode = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("wasNotGiven"))
          this.wasNotGiven = castToBoolean(value); // BooleanType
        else if (name.equals("reported"))
          this.reported = castToBoolean(value); // BooleanType
        else if (name.equals("performer"))
          this.performer = castToReference(value); // Reference
        else if (name.equals("requester"))
          this.requester = castToReference(value); // Reference
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("manufacturer"))
          this.manufacturer = castToReference(value); // Reference
        else if (name.equals("location"))
          this.location = castToReference(value); // Reference
        else if (name.equals("lotNumber"))
          this.lotNumber = castToString(value); // StringType
        else if (name.equals("expirationDate"))
          this.expirationDate = castToDate(value); // DateType
        else if (name.equals("site"))
          this.site = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("route"))
          this.route = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("doseQuantity"))
          this.doseQuantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("explanation"))
          this.explanation = (ImmunizationExplanationComponent) value; // ImmunizationExplanationComponent
        else if (name.equals("reaction"))
          this.getReaction().add((ImmunizationReactionComponent) value);
        else if (name.equals("vaccinationProtocol"))
          this.getVaccinationProtocol().add((ImmunizationVaccinationProtocolComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // CodeType
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case 664556354:  return getVaccineCode(); // CodeableConcept
        case -791418107:  return getPatient(); // Reference
        case -1050911117: throw new FHIRException("Cannot make property wasNotGiven as it is not a complex type"); // BooleanType
        case -427039533: throw new FHIRException("Cannot make property reported as it is not a complex type"); // BooleanType
        case 481140686:  return getPerformer(); // Reference
        case 693933948:  return getRequester(); // Reference
        case 1524132147:  return getEncounter(); // Reference
        case -1969347631:  return getManufacturer(); // Reference
        case 1901043637:  return getLocation(); // Reference
        case 462547450: throw new FHIRException("Cannot make property lotNumber as it is not a complex type"); // StringType
        case -668811523: throw new FHIRException("Cannot make property expirationDate as it is not a complex type"); // DateType
        case 3530567:  return getSite(); // CodeableConcept
        case 108704329:  return getRoute(); // CodeableConcept
        case -2083618872:  return getDoseQuantity(); // SimpleQuantity
        case 3387378:  return addNote(); // Annotation
        case -1105867239:  return getExplanation(); // ImmunizationExplanationComponent
        case -867509719:  return addReaction(); // ImmunizationReactionComponent
        case -179633155:  return addVaccinationProtocol(); // ImmunizationVaccinationProtocolComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.status");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.date");
        }
        else if (name.equals("vaccineCode")) {
          this.vaccineCode = new CodeableConcept();
          return this.vaccineCode;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("wasNotGiven")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.wasNotGiven");
        }
        else if (name.equals("reported")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.reported");
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("requester")) {
          this.requester = new Reference();
          return this.requester;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("manufacturer")) {
          this.manufacturer = new Reference();
          return this.manufacturer;
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("lotNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.lotNumber");
        }
        else if (name.equals("expirationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Immunization.expirationDate");
        }
        else if (name.equals("site")) {
          this.site = new CodeableConcept();
          return this.site;
        }
        else if (name.equals("route")) {
          this.route = new CodeableConcept();
          return this.route;
        }
        else if (name.equals("doseQuantity")) {
          this.doseQuantity = new SimpleQuantity();
          return this.doseQuantity;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("explanation")) {
          this.explanation = new ImmunizationExplanationComponent();
          return this.explanation;
        }
        else if (name.equals("reaction")) {
          return addReaction();
        }
        else if (name.equals("vaccinationProtocol")) {
          return addVaccinationProtocol();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Immunization";

  }

      public Immunization copy() {
        Immunization dst = new Immunization();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.date = date == null ? null : date.copy();
        dst.vaccineCode = vaccineCode == null ? null : vaccineCode.copy();
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
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(date, o.date, true)
           && compareDeep(vaccineCode, o.vaccineCode, true) && compareDeep(patient, o.patient, true) && compareDeep(wasNotGiven, o.wasNotGiven, true)
           && compareDeep(reported, o.reported, true) && compareDeep(performer, o.performer, true) && compareDeep(requester, o.requester, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(manufacturer, o.manufacturer, true)
           && compareDeep(location, o.location, true) && compareDeep(lotNumber, o.lotNumber, true) && compareDeep(expirationDate, o.expirationDate, true)
           && compareDeep(site, o.site, true) && compareDeep(route, o.route, true) && compareDeep(doseQuantity, o.doseQuantity, true)
           && compareDeep(note, o.note, true) && compareDeep(explanation, o.explanation, true) && compareDeep(reaction, o.reaction, true)
           && compareDeep(vaccinationProtocol, o.vaccinationProtocol, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Immunization))
          return false;
        Immunization o = (Immunization) other;
        return compareValues(status, o.status, true) && compareValues(date, o.date, true) && compareValues(wasNotGiven, o.wasNotGiven, true)
           && compareValues(reported, o.reported, true) && compareValues(lotNumber, o.lotNumber, true) && compareValues(expirationDate, o.expirationDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (date == null || date.isEmpty()) && (vaccineCode == null || vaccineCode.isEmpty()) && (patient == null || patient.isEmpty())
           && (wasNotGiven == null || wasNotGiven.isEmpty()) && (reported == null || reported.isEmpty())
           && (performer == null || performer.isEmpty()) && (requester == null || requester.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (manufacturer == null || manufacturer.isEmpty())
           && (location == null || location.isEmpty()) && (lotNumber == null || lotNumber.isEmpty())
           && (expirationDate == null || expirationDate.isEmpty()) && (site == null || site.isEmpty())
           && (route == null || route.isEmpty()) && (doseQuantity == null || doseQuantity.isEmpty())
           && (note == null || note.isEmpty()) && (explanation == null || explanation.isEmpty()) && (reaction == null || reaction.isEmpty())
           && (vaccinationProtocol == null || vaccinationProtocol.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Immunization;
   }

 /**
   * Search parameter: <b>reaction</b>
   * <p>
   * Description: <b>Additional information on reaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.reaction.detail</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reaction", path="Immunization.reaction.detail", description="Additional information on reaction", type="reference" )
  public static final String SP_REACTION = "reaction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reaction</b>
   * <p>
   * Description: <b>Additional information on reaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.reaction.detail</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REACTION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REACTION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Immunization:reaction</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REACTION = new ca.uhn.fhir.model.api.Include("Immunization:reaction").toLocked();

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>The practitioner who ordered the vaccination</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.requester</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="Immunization.requester", description="The practitioner who ordered the vaccination", type="reference" )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>The practitioner who ordered the vaccination</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.requester</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Immunization:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("Immunization:requester").toLocked();

 /**
   * Search parameter: <b>dose-sequence</b>
   * <p>
   * Description: <b>Dose number within series</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Immunization.vaccinationProtocol.doseSequence</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dose-sequence", path="Immunization.vaccinationProtocol.doseSequence", description="Dose number within series", type="number" )
  public static final String SP_DOSE_SEQUENCE = "dose-sequence";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dose-sequence</b>
   * <p>
   * Description: <b>Dose number within series</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Immunization.vaccinationProtocol.doseSequence</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam DOSE_SEQUENCE = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_DOSE_SEQUENCE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Immunization event status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Immunization.status", description="Immunization event status", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Immunization event status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>The service delivery location or facility in which the vaccine was / was to be administered</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Immunization.location", description="The service delivery location or facility in which the vaccine was / was to be administered", type="reference" )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>The service delivery location or facility in which the vaccine was / was to be administered</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Immunization:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("Immunization:location").toLocked();

 /**
   * Search parameter: <b>reason</b>
   * <p>
   * Description: <b>Why immunization occurred</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.explanation.reason</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reason", path="Immunization.explanation.reason", description="Why immunization occurred", type="token" )
  public static final String SP_REASON = "reason";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reason</b>
   * <p>
   * Description: <b>Why immunization occurred</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.explanation.reason</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REASON = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REASON);

 /**
   * Search parameter: <b>reaction-date</b>
   * <p>
   * Description: <b>When reaction started</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Immunization.reaction.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reaction-date", path="Immunization.reaction.date", description="When reaction started", type="date" )
  public static final String SP_REACTION_DATE = "reaction-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reaction-date</b>
   * <p>
   * Description: <b>When reaction started</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Immunization.reaction.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam REACTION_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_REACTION_DATE);

 /**
   * Search parameter: <b>notgiven</b>
   * <p>
   * Description: <b>Administrations which were not given</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.wasNotGiven</b><br>
   * </p>
   */
  @SearchParamDefinition(name="notgiven", path="Immunization.wasNotGiven", description="Administrations which were not given", type="token" )
  public static final String SP_NOTGIVEN = "notgiven";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>notgiven</b>
   * <p>
   * Description: <b>Administrations which were not given</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.wasNotGiven</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam NOTGIVEN = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_NOTGIVEN);

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Vaccination  (non)-Administration Date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Immunization.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Immunization.date", description="Vaccination  (non)-Administration Date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Vaccination  (non)-Administration Date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Immunization.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>reason-not-given</b>
   * <p>
   * Description: <b>Explanation of reason vaccination was not administered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.explanation.reasonNotGiven</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reason-not-given", path="Immunization.explanation.reasonNotGiven", description="Explanation of reason vaccination was not administered", type="token" )
  public static final String SP_REASON_NOT_GIVEN = "reason-not-given";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reason-not-given</b>
   * <p>
   * Description: <b>Explanation of reason vaccination was not administered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.explanation.reasonNotGiven</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REASON_NOT_GIVEN = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REASON_NOT_GIVEN);

 /**
   * Search parameter: <b>vaccine-code</b>
   * <p>
   * Description: <b>Vaccine Product Administered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.vaccineCode</b><br>
   * </p>
   */
  @SearchParamDefinition(name="vaccine-code", path="Immunization.vaccineCode", description="Vaccine Product Administered", type="token" )
  public static final String SP_VACCINE_CODE = "vaccine-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>vaccine-code</b>
   * <p>
   * Description: <b>Vaccine Product Administered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.vaccineCode</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VACCINE_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VACCINE_CODE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The patient for the vaccination record</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Immunization.patient", description="The patient for the vaccination record", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The patient for the vaccination record</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Immunization:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Immunization:patient").toLocked();

 /**
   * Search parameter: <b>lot-number</b>
   * <p>
   * Description: <b>Vaccine Lot Number</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Immunization.lotNumber</b><br>
   * </p>
   */
  @SearchParamDefinition(name="lot-number", path="Immunization.lotNumber", description="Vaccine Lot Number", type="string" )
  public static final String SP_LOT_NUMBER = "lot-number";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>lot-number</b>
   * <p>
   * Description: <b>Vaccine Lot Number</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Immunization.lotNumber</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam LOT_NUMBER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_LOT_NUMBER);

 /**
   * Search parameter: <b>manufacturer</b>
   * <p>
   * Description: <b>Vaccine Manufacturer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.manufacturer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="manufacturer", path="Immunization.manufacturer", description="Vaccine Manufacturer", type="reference" )
  public static final String SP_MANUFACTURER = "manufacturer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
   * <p>
   * Description: <b>Vaccine Manufacturer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.manufacturer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MANUFACTURER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MANUFACTURER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Immunization:manufacturer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MANUFACTURER = new ca.uhn.fhir.model.api.Include("Immunization:manufacturer").toLocked();

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>The practitioner who administered the vaccination</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="Immunization.performer", description="The practitioner who administered the vaccination", type="reference" )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>The practitioner who administered the vaccination</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Immunization.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Immunization:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("Immunization:performer").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Immunization.identifier", description="Business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Immunization.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

