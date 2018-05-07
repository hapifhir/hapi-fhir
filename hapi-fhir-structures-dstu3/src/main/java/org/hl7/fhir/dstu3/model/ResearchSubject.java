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
 * A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.
 */
@ResourceDef(name="ResearchSubject", profile="http://hl7.org/fhir/Profile/ResearchSubject")
public class ResearchSubject extends DomainResource {

    public enum ResearchSubjectStatus {
        /**
         * The subject has been identified as a potential participant in the study but has not yet agreed to participate
         */
        CANDIDATE, 
        /**
         * The subject has agreed to participate in the study but has not yet begun performing any action within the study
         */
        ENROLLED, 
        /**
         * The subject is currently being monitored and/or subject to treatment as part of the study
         */
        ACTIVE, 
        /**
         * The subject has temporarily discontinued monitoring/treatment as part of the study
         */
        SUSPENDED, 
        /**
         * The subject has permanently ended participation in the study prior to completion of the intended monitoring/treatment
         */
        WITHDRAWN, 
        /**
         * All intended monitoring/treatment of the subject has been completed and their engagement with the study is now ended
         */
        COMPLETED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ResearchSubjectStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("candidate".equals(codeString))
          return CANDIDATE;
        if ("enrolled".equals(codeString))
          return ENROLLED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("withdrawn".equals(codeString))
          return WITHDRAWN;
        if ("completed".equals(codeString))
          return COMPLETED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ResearchSubjectStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CANDIDATE: return "candidate";
            case ENROLLED: return "enrolled";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case WITHDRAWN: return "withdrawn";
            case COMPLETED: return "completed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CANDIDATE: return "http://hl7.org/fhir/research-subject-status";
            case ENROLLED: return "http://hl7.org/fhir/research-subject-status";
            case ACTIVE: return "http://hl7.org/fhir/research-subject-status";
            case SUSPENDED: return "http://hl7.org/fhir/research-subject-status";
            case WITHDRAWN: return "http://hl7.org/fhir/research-subject-status";
            case COMPLETED: return "http://hl7.org/fhir/research-subject-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CANDIDATE: return "The subject has been identified as a potential participant in the study but has not yet agreed to participate";
            case ENROLLED: return "The subject has agreed to participate in the study but has not yet begun performing any action within the study";
            case ACTIVE: return "The subject is currently being monitored and/or subject to treatment as part of the study";
            case SUSPENDED: return "The subject has temporarily discontinued monitoring/treatment as part of the study";
            case WITHDRAWN: return "The subject has permanently ended participation in the study prior to completion of the intended monitoring/treatment";
            case COMPLETED: return "All intended monitoring/treatment of the subject has been completed and their engagement with the study is now ended";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CANDIDATE: return "Candidate";
            case ENROLLED: return "Enrolled";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case WITHDRAWN: return "Withdrawn";
            case COMPLETED: return "Completed";
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
        if ("enrolled".equals(codeString))
          return ResearchSubjectStatus.ENROLLED;
        if ("active".equals(codeString))
          return ResearchSubjectStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return ResearchSubjectStatus.SUSPENDED;
        if ("withdrawn".equals(codeString))
          return ResearchSubjectStatus.WITHDRAWN;
        if ("completed".equals(codeString))
          return ResearchSubjectStatus.COMPLETED;
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
        if ("enrolled".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.ENROLLED);
        if ("active".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.SUSPENDED);
        if ("withdrawn".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.WITHDRAWN);
        if ("completed".equals(codeString))
          return new Enumeration<ResearchSubjectStatus>(this, ResearchSubjectStatus.COMPLETED);
        throw new FHIRException("Unknown ResearchSubjectStatus code '"+codeString+"'");
        }
    public String toCode(ResearchSubjectStatus code) {
      if (code == ResearchSubjectStatus.CANDIDATE)
        return "candidate";
      if (code == ResearchSubjectStatus.ENROLLED)
        return "enrolled";
      if (code == ResearchSubjectStatus.ACTIVE)
        return "active";
      if (code == ResearchSubjectStatus.SUSPENDED)
        return "suspended";
      if (code == ResearchSubjectStatus.WITHDRAWN)
        return "withdrawn";
      if (code == ResearchSubjectStatus.COMPLETED)
        return "completed";
      return "?";
      }
    public String toSystem(ResearchSubjectStatus code) {
      return code.getSystem();
      }
    }

    /**
     * Identifiers assigned to this research study by the sponsor or other systems.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier for research subject", formalDefinition="Identifiers assigned to this research study by the sponsor or other systems." )
    protected Identifier identifier;

    /**
     * The current state of the subject.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="candidate | enrolled | active | suspended | withdrawn | completed", formalDefinition="The current state of the subject." )
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

    private static final long serialVersionUID = -1730128953L;

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
     * @return {@link #identifier} (Identifiers assigned to this research study by the sponsor or other systems.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchSubject.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Identifiers assigned to this research study by the sponsor or other systems.)
     */
    public ResearchSubject setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this research study by the sponsor or other systems.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The current state of the subject.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("period", "Period", "The dates the subject began and ended their participation in the study.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("study", "Reference(ResearchStudy)", "Reference to the study the subject is participating in.", 0, java.lang.Integer.MAX_VALUE, study));
        childrenList.add(new Property("individual", "Reference(Patient)", "The record of the person or animal who is involved in the study.", 0, java.lang.Integer.MAX_VALUE, individual));
        childrenList.add(new Property("assignedArm", "string", "The name of the arm in the study the subject is expected to follow as part of this study.", 0, java.lang.Integer.MAX_VALUE, assignedArm));
        childrenList.add(new Property("actualArm", "string", "The name of the arm in the study the subject actually followed as part of this study.", 0, java.lang.Integer.MAX_VALUE, actualArm));
        childrenList.add(new Property("consent", "Reference(Consent)", "A record of the patient's informed agreement to participate in the study.", 0, java.lang.Integer.MAX_VALUE, consent));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
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
          this.identifier = castToIdentifier(value); // Identifier
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
          this.identifier = castToIdentifier(value); // Identifier
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
        case -1618432855:  return getIdentifier(); 
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
          this.identifier = new Identifier();
          return this.identifier;
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
        dst.identifier = identifier == null ? null : identifier.copy();
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ResearchSubject))
          return false;
        ResearchSubject o = (ResearchSubject) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(period, o.period, true)
           && compareDeep(study, o.study, true) && compareDeep(individual, o.individual, true) && compareDeep(assignedArm, o.assignedArm, true)
           && compareDeep(actualArm, o.actualArm, true) && compareDeep(consent, o.consent, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ResearchSubject))
          return false;
        ResearchSubject o = (ResearchSubject) other;
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
   * Description: <b>Business Identifier for research subject</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchSubject.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ResearchSubject.identifier", description="Business Identifier for research subject", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for research subject</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchSubject.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

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
   * Description: <b>candidate | enrolled | active | suspended | withdrawn | completed</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchSubject.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ResearchSubject.status", description="candidate | enrolled | active | suspended | withdrawn | completed", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>candidate | enrolled | active | suspended | withdrawn | completed</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchSubject.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

