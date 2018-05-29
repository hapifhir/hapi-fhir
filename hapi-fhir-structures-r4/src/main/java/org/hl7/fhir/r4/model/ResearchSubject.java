package org.hl7.fhir.r4.model;

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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

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
 * A physical entity which is the primary unit of operational and/or administrative interest in a study.
 */
@ResourceDef(name="ResearchSubject", profile="http://hl7.org/fhir/Profile/ResearchSubject")
public class ResearchSubject extends DomainResource {

    public enum ResearchSubjectStatus {
        /**
         * An identified person that can be considered for inclusion in a study.
         */
        CANDIDATE, 
        /**
         * A person that has met the eligibility criteria for inclusion in a study.
         */
        ELIGIBLE, 
        /**
         * A person is no longer receiving study intervention and/or being evaluated with tests and procedures according to the protocol, but they are being monitored on a protocol-prescribed schedule.
         */
        FOLLOWUP, 
        /**
         * A person who did not meet one or more criteria required for participation in a study is considered to have failed screening or
is ineligible for the study.
         */
        INELIGIBLE, 
        /**
         * A person for whom registration was not completed
         */
        NOTREGISTERED, 
        /**
         * A person that has ended their participation on a study either because their treatment/observation is complete or through not
responding, withdrawal, non-compliance and/or adverse event.
         */
        OFFSTUDY, 
        /**
         * A person that is enrolled or registered on a study.
         */
        ONSTUDY, 
        /**
         * The person is receiving the treatment or participating in an activity (e.g. yoga, diet, etc.) that the study is evaluating.
         */
        ONSTUDYINTERVENTION, 
        /**
         * The subject is being evaluated via tests and assessments according to the study calendar, but is not receiving any intervention. Note that this state is study-dependent and might not exist in all studies.  A synonym for this is "short-term follow-up".
         */
        ONSTUDYOBSERVATION, 
        /**
         * A person is pre-registered for a study.
         */
        PENDINGONSTUDY, 
        /**
         * A person that is potentially eligible for participation in the study.
         */
        POTENTIALCANDIDATE, 
        /**
         * A person who is being evaluated for eligibility for a study.
         */
        SCREENING, 
        /**
         * The person has withdrawn their participation in the study before registration.
         */
        WITHDRAWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ResearchSubjectStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("candidate".equals(codeString))
          return CANDIDATE;
        if ("eligible".equals(codeString))
          return ELIGIBLE;
        if ("follow-up".equals(codeString))
          return FOLLOWUP;
        if ("ineligible".equals(codeString))
          return INELIGIBLE;
        if ("not-registered".equals(codeString))
          return NOTREGISTERED;
        if ("off-study".equals(codeString))
          return OFFSTUDY;
        if ("on-study".equals(codeString))
          return ONSTUDY;
        if ("on-study-intervention".equals(codeString))
          return ONSTUDYINTERVENTION;
        if ("on-study-observation".equals(codeString))
          return ONSTUDYOBSERVATION;
        if ("pending-on-study".equals(codeString))
          return PENDINGONSTUDY;
        if ("potential-candidate".equals(codeString))
          return POTENTIALCANDIDATE;
        if ("screening".equals(codeString))
          return SCREENING;
        if ("withdrawn".equals(codeString))
          return WITHDRAWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ResearchSubjectStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CANDIDATE: return "candidate";
            case ELIGIBLE: return "eligible";
            case FOLLOWUP: return "follow-up";
            case INELIGIBLE: return "ineligible";
            case NOTREGISTERED: return "not-registered";
            case OFFSTUDY: return "off-study";
            case ONSTUDY: return "on-study";
            case ONSTUDYINTERVENTION: return "on-study-intervention";
            case ONSTUDYOBSERVATION: return "on-study-observation";
            case PENDINGONSTUDY: return "pending-on-study";
            case POTENTIALCANDIDATE: return "potential-candidate";
            case SCREENING: return "screening";
            case WITHDRAWN: return "withdrawn";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CANDIDATE: return "http://hl7.org/fhir/research-subject-status";
            case ELIGIBLE: return "http://hl7.org/fhir/research-subject-status";
            case FOLLOWUP: return "http://hl7.org/fhir/research-subject-status";
            case INELIGIBLE: return "http://hl7.org/fhir/research-subject-status";
            case NOTREGISTERED: return "http://hl7.org/fhir/research-subject-status";
            case OFFSTUDY: return "http://hl7.org/fhir/research-subject-status";
            case ONSTUDY: return "http://hl7.org/fhir/research-subject-status";
            case ONSTUDYINTERVENTION: return "http://hl7.org/fhir/research-subject-status";
            case ONSTUDYOBSERVATION: return "http://hl7.org/fhir/research-subject-status";
            case PENDINGONSTUDY: return "http://hl7.org/fhir/research-subject-status";
            case POTENTIALCANDIDATE: return "http://hl7.org/fhir/research-subject-status";
            case SCREENING: return "http://hl7.org/fhir/research-subject-status";
            case WITHDRAWN: return "http://hl7.org/fhir/research-subject-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CANDIDATE: return "An identified person that can be considered for inclusion in a study.";
            case ELIGIBLE: return "A person that has met the eligibility criteria for inclusion in a study.";
            case FOLLOWUP: return "A person is no longer receiving study intervention and/or being evaluated with tests and procedures according to the protocol, but they are being monitored on a protocol-prescribed schedule.";
            case INELIGIBLE: return "A person who did not meet one or more criteria required for participation in a study is considered to have failed screening or\nis ineligible for the study.";
            case NOTREGISTERED: return "A person for whom registration was not completed";
            case OFFSTUDY: return "A person that has ended their participation on a study either because their treatment/observation is complete or through not\nresponding, withdrawal, non-compliance and/or adverse event.";
            case ONSTUDY: return "A person that is enrolled or registered on a study.";
            case ONSTUDYINTERVENTION: return "The person is receiving the treatment or participating in an activity (e.g. yoga, diet, etc.) that the study is evaluating.";
            case ONSTUDYOBSERVATION: return "The subject is being evaluated via tests and assessments according to the study calendar, but is not receiving any intervention. Note that this state is study-dependent and might not exist in all studies.  A synonym for this is \"short-term follow-up\".";
            case PENDINGONSTUDY: return "A person is pre-registered for a study.";
            case POTENTIALCANDIDATE: return "A person that is potentially eligible for participation in the study.";
            case SCREENING: return "A person who is being evaluated for eligibility for a study.";
            case WITHDRAWN: return "The person has withdrawn their participation in the study before registration.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CANDIDATE: return "Candidate";
            case ELIGIBLE: return "Eligible";
            case FOLLOWUP: return "Follow-up";
            case INELIGIBLE: return "Ineligible";
            case NOTREGISTERED: return "Not Registered";
            case OFFSTUDY: return "Off-study";
            case ONSTUDY: return "On-study";
            case ONSTUDYINTERVENTION: return "On-study-intervention";
            case ONSTUDYOBSERVATION: return "On-study-observation";
            case PENDINGONSTUDY: return "Pending on-study";
            case POTENTIALCANDIDATE: return "Potential Candidate";
            case SCREENING: return "Screening";
            case WITHDRAWN: return "Withdrawn";
            default: return "?";
          }
        }
    }

  public static class ResearchSubjectStatusEnumFactory implements EnumFactory<ResearchSubjectStatus> {
    public ResearchSubjectStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("candidate".equals(codeString))
          return ResearchSubjectStatus.CANDIDATE;
        if ("eligible".equals(codeString))
          return ResearchSubjectStatus.ELIGIBLE;
        if ("follow-up".equals(codeString))
          return ResearchSubjectStatus.FOLLOWUP;
        if ("ineligible".equals(codeString))
          return ResearchSubjectStatus.INELIGIBLE;
        if ("not-registered".equals(codeString))
          return ResearchSubjectStatus.NOTREGISTERED;
        if ("off-study".equals(codeString))
          return ResearchSubjectStatus.OFFSTUDY;
        if ("on-study".equals(codeString))
          return ResearchSubjectStatus.ONSTUDY;
        if ("on-study-intervention".equals(codeString))
          return ResearchSubjectStatus.ONSTUDYINTERVENTION;
        if ("on-study-observation".equals(codeString))
          return ResearchSubjectStatus.ONSTUDYOBSERVATION;
        if ("pending-on-study".equals(codeString))
          return ResearchSubjectStatus.PENDINGONSTUDY;
        if ("potential-candidate".equals(codeString))
          return ResearchSubjectStatus.POTENTIALCANDIDATE;
        if ("screening".equals(codeString))
          return ResearchSubjectStatus.SCREENING;
        if ("withdrawn".equals(codeString))
          return ResearchSubjectStatus.WITHDRAWN;
        throw new IllegalArgumentException("Unknown ResearchSubjectStatus code '"+codeString+"'");
        }
        public Enumeration<ResearchSubjectStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ResearchSubjectStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("candidate".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.CANDIDATE);
        if ("eligible".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.ELIGIBLE);
        if ("follow-up".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.FOLLOWUP);
        if ("ineligible".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.INELIGIBLE);
        if ("not-registered".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.NOTREGISTERED);
        if ("off-study".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.OFFSTUDY);
        if ("on-study".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.ONSTUDY);
        if ("on-study-intervention".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.ONSTUDYINTERVENTION);
        if ("on-study-observation".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.ONSTUDYOBSERVATION);
        if ("pending-on-study".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.PENDINGONSTUDY);
        if ("potential-candidate".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.POTENTIALCANDIDATE);
        if ("screening".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.SCREENING);
        if ("withdrawn".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.WITHDRAWN);
        throw new FHIRException("Unknown ResearchSubjectStatus code '"+codeString+"'");
        }
    public String toCode(ResearchSubjectStatus code) {
      if (code == ResearchSubjectStatus.CANDIDATE)
        return "candidate";
      if (code == ResearchSubjectStatus.ELIGIBLE)
        return "eligible";
      if (code == ResearchSubjectStatus.FOLLOWUP)
        return "follow-up";
      if (code == ResearchSubjectStatus.INELIGIBLE)
        return "ineligible";
      if (code == ResearchSubjectStatus.NOTREGISTERED)
        return "not-registered";
      if (code == ResearchSubjectStatus.OFFSTUDY)
        return "off-study";
      if (code == ResearchSubjectStatus.ONSTUDY)
        return "on-study";
      if (code == ResearchSubjectStatus.ONSTUDYINTERVENTION)
        return "on-study-intervention";
      if (code == ResearchSubjectStatus.ONSTUDYOBSERVATION)
        return "on-study-observation";
      if (code == ResearchSubjectStatus.PENDINGONSTUDY)
        return "pending-on-study";
      if (code == ResearchSubjectStatus.POTENTIALCANDIDATE)
        return "potential-candidate";
      if (code == ResearchSubjectStatus.SCREENING)
        return "screening";
      if (code == ResearchSubjectStatus.WITHDRAWN)
        return "withdrawn";
      return "?";
      }
    public String toSystem(ResearchSubjectStatus code) {
      return code.getSystem();
      }
    }

    /**
     * Identifiers assigned to this research subject for a study.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier for research subject in a study", formalDefinition="Identifiers assigned to this research subject for a study." )
    protected List<Identifier> identifier;

    /**
     * The current state of the subject.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="candidate | eligible | follow-up | ineligible | not-registered | off-study | on-study | on-study-intervention | on-study-observation | pending-on-study | potential-candidate | screening | withdrawn", formalDefinition="The current state of the subject." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/research-subject-status")
    protected Enumeration<ResearchSubjectStatus> status;

    /**
     * The dates the subject began and ended their participation in the study.
     */
    @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Start and end of participation", formalDefinition="The dates the subject began and ended their participation in the study." )
    protected Period period;

    /**
     * Reference to the study the subject is participating in.
     */
    @Child(name = "study", type = {ResearchStudy.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Study subject is part of", formalDefinition="Reference to the study the subject is participating in." )
    protected Reference study;

    /**
     * The actual object that is the target of the reference (Reference to the study the subject is participating in.)
     */
    protected ResearchStudy studyTarget;

    /**
     * The record of the person or animal who is involved in the study.
     */
    @Child(name = "individual", type = {Patient.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who is part of study", formalDefinition="The record of the person or animal who is involved in the study." )
    protected Reference individual;

    /**
     * The actual object that is the target of the reference (The record of the person or animal who is involved in the study.)
     */
    protected Patient individualTarget;

    /**
     * The name of the arm in the study the subject is expected to follow as part of this study.
     */
    @Child(name = "assignedArm", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What path should be followed", formalDefinition="The name of the arm in the study the subject is expected to follow as part of this study." )
    protected StringType assignedArm;

    /**
     * The name of the arm in the study the subject actually followed as part of this study.
     */
    @Child(name = "actualArm", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What path was followed", formalDefinition="The name of the arm in the study the subject actually followed as part of this study." )
    protected StringType actualArm;

    /**
     * A record of the patient's informed agreement to participate in the study.
     */
    @Child(name = "consent", type = {Consent.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Agreement to participate in study", formalDefinition="A record of the patient's informed agreement to participate in the study." )
    protected Reference consent;

    /**
     * The actual object that is the target of the reference (A record of the patient's informed agreement to participate in the study.)
     */
    protected Consent consentTarget;

    private static final long serialVersionUID = -884133739L;

  /**
   * Constructor
   */
    public ResearchSubject() {
      super();
    }

  /**
   * Constructor
   */
    public ResearchSubject(Enumeration<ResearchSubjectStatus> status, Reference study, Reference individual) {
      super();
      this.status = status;
      this.study = study;
      this.individual = individual;
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this research subject for a study.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchSubject setIdentifier(List<Identifier> theIdentifier) { 
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

    public ResearchSubject addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (The current state of the subject.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ResearchSubjectStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ResearchSubjectStatus>(new ResearchSubjectStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The current state of the subject.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ResearchSubject setStatusElement(Enumeration<ResearchSubjectStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The current state of the subject.
     */
    public ResearchSubjectStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The current state of the subject.
     */
    public ResearchSubject setStatus(ResearchSubjectStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ResearchSubjectStatus>(new ResearchSubjectStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #period} (The dates the subject began and ended their participation in the study.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The dates the subject began and ended their participation in the study.)
     */
    public ResearchSubject setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #study} (Reference to the study the subject is participating in.)
     */
    public Reference getStudy() { 
      if (this.study == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.study");
        else if (Configuration.doAutoCreate())
          this.study = new Reference(); // cc
      return this.study;
    }

    public boolean hasStudy() { 
      return this.study != null && !this.study.isEmpty();
    }

    /**
     * @param value {@link #study} (Reference to the study the subject is participating in.)
     */
    public ResearchSubject setStudy(Reference value) { 
      this.study = value;
      return this;
    }

    /**
     * @return {@link #study} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the study the subject is participating in.)
     */
    public ResearchStudy getStudyTarget() { 
      if (this.studyTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.study");
        else if (Configuration.doAutoCreate())
          this.studyTarget = new ResearchStudy(); // aa
      return this.studyTarget;
    }

    /**
     * @param value {@link #study} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the study the subject is participating in.)
     */
    public ResearchSubject setStudyTarget(ResearchStudy value) { 
      this.studyTarget = value;
      return this;
    }

    /**
     * @return {@link #individual} (The record of the person or animal who is involved in the study.)
     */
    public Reference getIndividual() { 
      if (this.individual == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.individual");
        else if (Configuration.doAutoCreate())
          this.individual = new Reference(); // cc
      return this.individual;
    }

    public boolean hasIndividual() { 
      return this.individual != null && !this.individual.isEmpty();
    }

    /**
     * @param value {@link #individual} (The record of the person or animal who is involved in the study.)
     */
    public ResearchSubject setIndividual(Reference value) { 
      this.individual = value;
      return this;
    }

    /**
     * @return {@link #individual} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The record of the person or animal who is involved in the study.)
     */
    public Patient getIndividualTarget() { 
      if (this.individualTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.individual");
        else if (Configuration.doAutoCreate())
          this.individualTarget = new Patient(); // aa
      return this.individualTarget;
    }

    /**
     * @param value {@link #individual} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The record of the person or animal who is involved in the study.)
     */
    public ResearchSubject setIndividualTarget(Patient value) { 
      this.individualTarget = value;
      return this;
    }

    /**
     * @return {@link #assignedArm} (The name of the arm in the study the subject is expected to follow as part of this study.). This is the underlying object with id, value and extensions. The accessor "getAssignedArm" gives direct access to the value
     */
    public StringType getAssignedArmElement() { 
      if (this.assignedArm == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.assignedArm");
        else if (Configuration.doAutoCreate())
          this.assignedArm = new StringType(); // bb
      return this.assignedArm;
    }

    public boolean hasAssignedArmElement() { 
      return this.assignedArm != null && !this.assignedArm.isEmpty();
    }

    public boolean hasAssignedArm() { 
      return this.assignedArm != null && !this.assignedArm.isEmpty();
    }

    /**
     * @param value {@link #assignedArm} (The name of the arm in the study the subject is expected to follow as part of this study.). This is the underlying object with id, value and extensions. The accessor "getAssignedArm" gives direct access to the value
     */
    public ResearchSubject setAssignedArmElement(StringType value) { 
      this.assignedArm = value;
      return this;
    }

    /**
     * @return The name of the arm in the study the subject is expected to follow as part of this study.
     */
    public String getAssignedArm() { 
      return this.assignedArm == null ? null : this.assignedArm.getValue();
    }

    /**
     * @param value The name of the arm in the study the subject is expected to follow as part of this study.
     */
    public ResearchSubject setAssignedArm(String value) { 
      if (Utilities.noString(value))
        this.assignedArm = null;
      else {
        if (this.assignedArm == null)
          this.assignedArm = new StringType();
        this.assignedArm.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #actualArm} (The name of the arm in the study the subject actually followed as part of this study.). This is the underlying object with id, value and extensions. The accessor "getActualArm" gives direct access to the value
     */
    public StringType getActualArmElement() { 
      if (this.actualArm == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.actualArm");
        else if (Configuration.doAutoCreate())
          this.actualArm = new StringType(); // bb
      return this.actualArm;
    }

    public boolean hasActualArmElement() { 
      return this.actualArm != null && !this.actualArm.isEmpty();
    }

    public boolean hasActualArm() { 
      return this.actualArm != null && !this.actualArm.isEmpty();
    }

    /**
     * @param value {@link #actualArm} (The name of the arm in the study the subject actually followed as part of this study.). This is the underlying object with id, value and extensions. The accessor "getActualArm" gives direct access to the value
     */
    public ResearchSubject setActualArmElement(StringType value) { 
      this.actualArm = value;
      return this;
    }

    /**
     * @return The name of the arm in the study the subject actually followed as part of this study.
     */
    public String getActualArm() { 
      return this.actualArm == null ? null : this.actualArm.getValue();
    }

    /**
     * @param value The name of the arm in the study the subject actually followed as part of this study.
     */
    public ResearchSubject setActualArm(String value) { 
      if (Utilities.noString(value))
        this.actualArm = null;
      else {
        if (this.actualArm == null)
          this.actualArm = new StringType();
        this.actualArm.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #consent} (A record of the patient's informed agreement to participate in the study.)
     */
    public Reference getConsent() { 
      if (this.consent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.consent");
        else if (Configuration.doAutoCreate())
          this.consent = new Reference(); // cc
      return this.consent;
    }

    public boolean hasConsent() { 
      return this.consent != null && !this.consent.isEmpty();
    }

    /**
     * @param value {@link #consent} (A record of the patient's informed agreement to participate in the study.)
     */
    public ResearchSubject setConsent(Reference value) { 
      this.consent = value;
      return this;
    }

    /**
     * @return {@link #consent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A record of the patient's informed agreement to participate in the study.)
     */
    public Consent getConsentTarget() { 
      if (this.consentTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.consent");
        else if (Configuration.doAutoCreate())
          this.consentTarget = new Consent(); // aa
      return this.consentTarget;
    }

    /**
     * @param value {@link #consent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A record of the patient's informed agreement to participate in the study.)
     */
    public ResearchSubject setConsentTarget(Consent value) { 
      this.consentTarget = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Identifiers assigned to this research subject for a study.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The current state of the subject.", 0, 1, status));
        children.add(new Property("period", "Period", "The dates the subject began and ended their participation in the study.", 0, 1, period));
        children.add(new Property("study", "Reference(ResearchStudy)", "Reference to the study the subject is participating in.", 0, 1, study));
        children.add(new Property("individual", "Reference(Patient)", "The record of the person or animal who is involved in the study.", 0, 1, individual));
        children.add(new Property("assignedArm", "string", "The name of the arm in the study the subject is expected to follow as part of this study.", 0, 1, assignedArm));
        children.add(new Property("actualArm", "string", "The name of the arm in the study the subject actually followed as part of this study.", 0, 1, actualArm));
        children.add(new Property("consent", "Reference(Consent)", "A record of the patient's informed agreement to participate in the study.", 0, 1, consent));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifiers assigned to this research subject for a study.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The current state of the subject.", 0, 1, status);
        case -991726143: /*period*/  return new Property("period", "Period", "The dates the subject began and ended their participation in the study.", 0, 1, period);
        case 109776329: /*study*/  return new Property("study", "Reference(ResearchStudy)", "Reference to the study the subject is participating in.", 0, 1, study);
        case -46292327: /*individual*/  return new Property("individual", "Reference(Patient)", "The record of the person or animal who is involved in the study.", 0, 1, individual);
        case 1741912494: /*assignedArm*/  return new Property("assignedArm", "string", "The name of the arm in the study the subject is expected to follow as part of this study.", 0, 1, assignedArm);
        case 528827886: /*actualArm*/  return new Property("actualArm", "string", "The name of the arm in the study the subject actually followed as part of this study.", 0, 1, actualArm);
        case 951500826: /*consent*/  return new Property("consent", "Reference(Consent)", "A record of the patient's informed agreement to participate in the study.", 0, 1, consent);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ResearchSubjectStatus>
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 109776329: /*study*/ return this.study == null ? new Base[0] : new Base[] {this.study}; // Reference
        case -46292327: /*individual*/ return this.individual == null ? new Base[0] : new Base[] {this.individual}; // Reference
        case 1741912494: /*assignedArm*/ return this.assignedArm == null ? new Base[0] : new Base[] {this.assignedArm}; // StringType
        case 528827886: /*actualArm*/ return this.actualArm == null ? new Base[0] : new Base[] {this.actualArm}; // StringType
        case 951500826: /*consent*/ return this.consent == null ? new Base[0] : new Base[] {this.consent}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new ResearchSubjectStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ResearchSubjectStatus>
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 109776329: // study
          this.study = castToReference(value); // Reference
          return value;
        case -46292327: // individual
          this.individual = castToReference(value); // Reference
          return value;
        case 1741912494: // assignedArm
          this.assignedArm = castToString(value); // StringType
          return value;
        case 528827886: // actualArm
          this.actualArm = castToString(value); // StringType
          return value;
        case 951500826: // consent
          this.consent = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ResearchSubjectStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ResearchSubjectStatus>
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("study")) {
          this.study = castToReference(value); // Reference
        } else if (name.equals("individual")) {
          this.individual = castToReference(value); // Reference
        } else if (name.equals("assignedArm")) {
          this.assignedArm = castToString(value); // StringType
        } else if (name.equals("actualArm")) {
          this.actualArm = castToString(value); // StringType
        } else if (name.equals("consent")) {
          this.consent = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -991726143:  return getPeriod(); 
        case 109776329:  return getStudy(); 
        case -46292327:  return getIndividual(); 
        case 1741912494:  return getAssignedArmElement();
        case 528827886:  return getActualArmElement();
        case 951500826:  return getConsent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 109776329: /*study*/ return new String[] {"Reference"};
        case -46292327: /*individual*/ return new String[] {"Reference"};
        case 1741912494: /*assignedArm*/ return new String[] {"string"};
        case 528827886: /*actualArm*/ return new String[] {"string"};
        case 951500826: /*consent*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchSubject.status");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("study")) {
          this.study = new Reference();
          return this.study;
        }
        else if (name.equals("individual")) {
          this.individual = new Reference();
          return this.individual;
        }
        else if (name.equals("assignedArm")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchSubject.assignedArm");
        }
        else if (name.equals("actualArm")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchSubject.actualArm");
        }
        else if (name.equals("consent")) {
          this.consent = new Reference();
          return this.consent;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ResearchSubject";

  }

      public ResearchSubject copy() {
        ResearchSubject dst = new ResearchSubject();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.period = period == null ? null : period.copy();
        dst.study = study == null ? null : study.copy();
        dst.individual = individual == null ? null : individual.copy();
        dst.assignedArm = assignedArm == null ? null : assignedArm.copy();
        dst.actualArm = actualArm == null ? null : actualArm.copy();
        dst.consent = consent == null ? null : consent.copy();
        return dst;
      }

      protected ResearchSubject typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ResearchSubject))
          return false;
        ResearchSubject o = (ResearchSubject) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(period, o.period, true)
           && compareDeep(study, o.study, true) && compareDeep(individual, o.individual, true) && compareDeep(assignedArm, o.assignedArm, true)
           && compareDeep(actualArm, o.actualArm, true) && compareDeep(consent, o.consent, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ResearchSubject))
          return false;
        ResearchSubject o = (ResearchSubject) other_;
        return compareValues(status, o.status, true) && compareValues(assignedArm, o.assignedArm, true) && compareValues(actualArm, o.actualArm, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, period
          , study, individual, assignedArm, actualArm, consent);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ResearchSubject;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Start and end of participation</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchSubject.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ResearchSubject.period", description="Start and end of participation", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Start and end of participation</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchSubject.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for research subject in a study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchSubject.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ResearchSubject.identifier", description="Business Identifier for research subject in a study", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for research subject in a study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchSubject.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>study</b>
   * <p>
   * Description: <b>Study subject is part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchSubject.study</b><br>
   * </p>
   */
  @SearchParamDefinition(name="study", path="ResearchSubject.study", description="Study subject is part of", type="reference", target={ResearchStudy.class } )
  public static final String SP_STUDY = "study";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>study</b>
   * <p>
   * Description: <b>Study subject is part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchSubject.study</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam STUDY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_STUDY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchSubject:study</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_STUDY = new ca.uhn.fhir.model.api.Include("ResearchSubject:study").toLocked();

 /**
   * Search parameter: <b>individual</b>
   * <p>
   * Description: <b>Who is part of study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchSubject.individual</b><br>
   * </p>
   */
  @SearchParamDefinition(name="individual", path="ResearchSubject.individual", description="Who is part of study", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_INDIVIDUAL = "individual";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>individual</b>
   * <p>
   * Description: <b>Who is part of study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchSubject.individual</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INDIVIDUAL = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INDIVIDUAL);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchSubject:individual</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INDIVIDUAL = new ca.uhn.fhir.model.api.Include("ResearchSubject:individual").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who is part of study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchSubject.individual</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ResearchSubject.individual", description="Who is part of study", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who is part of study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchSubject.individual</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchSubject:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ResearchSubject:patient").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>candidate | eligible | follow-up | ineligible | not-registered | off-study | on-study | on-study-intervention | on-study-observation | pending-on-study | potential-candidate | screening | withdrawn</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchSubject.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ResearchSubject.status", description="candidate | eligible | follow-up | ineligible | not-registered | off-study | on-study | on-study-intervention | on-study-observation | pending-on-study | potential-candidate | screening | withdrawn", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>candidate | eligible | follow-up | ineligible | not-registered | off-study | on-study | on-study-intervention | on-study-observation | pending-on-study | potential-candidate | screening | withdrawn</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchSubject.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

