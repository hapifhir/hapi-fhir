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
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. The managing organization assumes a level of responsibility for the patient during this time.
 */
@ResourceDef(name="EpisodeOfCare", profile="http://hl7.org/fhir/Profile/EpisodeOfCare")
public class EpisodeOfCare extends DomainResource {

    public enum EpisodeOfCareStatus {
        /**
         * This episode of care is planned to start at the date specified in the period.start. During this status an organization may perform assessments to determine if they are eligible to receive services, or be organizing to make resources available to provide care services.
         */
        PLANNED, 
        /**
         * This episode has been placed on a waitlist, pending the episode being made active (or cancelled).
         */
        WAITLIST, 
        /**
         * This episode of care is current.
         */
        ACTIVE, 
        /**
         * This episode of care is on hold, the organization has limited responsibility for the patient (such as while on respite).
         */
        ONHOLD, 
        /**
         * This episode of care is finished at the organization is not expecting to be providing care to the patient. Can also be known as "closed", "completed" or other similar terms.
         */
        FINISHED, 
        /**
         * The episode of care was cancelled, or withdrawn from service, often selected during the planned stage as the patient may have gone elsewhere, or the circumstances have changed and the organization is unable to provide the care. It indicates that services terminated outside the planned/expected workflow.
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EpisodeOfCareStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("waitlist".equals(codeString))
          return WAITLIST;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("onhold".equals(codeString))
          return ONHOLD;
        if ("finished".equals(codeString))
          return FINISHED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new FHIRException("Unknown EpisodeOfCareStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case WAITLIST: return "waitlist";
            case ACTIVE: return "active";
            case ONHOLD: return "onhold";
            case FINISHED: return "finished";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PLANNED: return "http://hl7.org/fhir/episode-of-care-status";
            case WAITLIST: return "http://hl7.org/fhir/episode-of-care-status";
            case ACTIVE: return "http://hl7.org/fhir/episode-of-care-status";
            case ONHOLD: return "http://hl7.org/fhir/episode-of-care-status";
            case FINISHED: return "http://hl7.org/fhir/episode-of-care-status";
            case CANCELLED: return "http://hl7.org/fhir/episode-of-care-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "This episode of care is planned to start at the date specified in the period.start. During this status an organization may perform assessments to determine if they are eligible to receive services, or be organizing to make resources available to provide care services.";
            case WAITLIST: return "This episode has been placed on a waitlist, pending the episode being made active (or cancelled).";
            case ACTIVE: return "This episode of care is current.";
            case ONHOLD: return "This episode of care is on hold, the organization has limited responsibility for the patient (such as while on respite).";
            case FINISHED: return "This episode of care is finished at the organization is not expecting to be providing care to the patient. Can also be known as \"closed\", \"completed\" or other similar terms.";
            case CANCELLED: return "The episode of care was cancelled, or withdrawn from service, often selected during the planned stage as the patient may have gone elsewhere, or the circumstances have changed and the organization is unable to provide the care. It indicates that services terminated outside the planned/expected workflow.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "Planned";
            case WAITLIST: return "Waitlist";
            case ACTIVE: return "Active";
            case ONHOLD: return "On Hold";
            case FINISHED: return "Finished";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
        }
    }

  public static class EpisodeOfCareStatusEnumFactory implements EnumFactory<EpisodeOfCareStatus> {
    public EpisodeOfCareStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return EpisodeOfCareStatus.PLANNED;
        if ("waitlist".equals(codeString))
          return EpisodeOfCareStatus.WAITLIST;
        if ("active".equals(codeString))
          return EpisodeOfCareStatus.ACTIVE;
        if ("onhold".equals(codeString))
          return EpisodeOfCareStatus.ONHOLD;
        if ("finished".equals(codeString))
          return EpisodeOfCareStatus.FINISHED;
        if ("cancelled".equals(codeString))
          return EpisodeOfCareStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown EpisodeOfCareStatus code '"+codeString+"'");
        }
        public Enumeration<EpisodeOfCareStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("planned".equals(codeString))
          return new Enumeration<EpisodeOfCareStatus>(this, EpisodeOfCareStatus.PLANNED);
        if ("waitlist".equals(codeString))
          return new Enumeration<EpisodeOfCareStatus>(this, EpisodeOfCareStatus.WAITLIST);
        if ("active".equals(codeString))
          return new Enumeration<EpisodeOfCareStatus>(this, EpisodeOfCareStatus.ACTIVE);
        if ("onhold".equals(codeString))
          return new Enumeration<EpisodeOfCareStatus>(this, EpisodeOfCareStatus.ONHOLD);
        if ("finished".equals(codeString))
          return new Enumeration<EpisodeOfCareStatus>(this, EpisodeOfCareStatus.FINISHED);
        if ("cancelled".equals(codeString))
          return new Enumeration<EpisodeOfCareStatus>(this, EpisodeOfCareStatus.CANCELLED);
        throw new FHIRException("Unknown EpisodeOfCareStatus code '"+codeString+"'");
        }
    public String toCode(EpisodeOfCareStatus code) {
      if (code == EpisodeOfCareStatus.PLANNED)
        return "planned";
      if (code == EpisodeOfCareStatus.WAITLIST)
        return "waitlist";
      if (code == EpisodeOfCareStatus.ACTIVE)
        return "active";
      if (code == EpisodeOfCareStatus.ONHOLD)
        return "onhold";
      if (code == EpisodeOfCareStatus.FINISHED)
        return "finished";
      if (code == EpisodeOfCareStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    public String toSystem(EpisodeOfCareStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class EpisodeOfCareStatusHistoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * planned | waitlist | active | onhold | finished | cancelled.
         */
        @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="planned | waitlist | active | onhold | finished | cancelled", formalDefinition="planned | waitlist | active | onhold | finished | cancelled." )
        protected Enumeration<EpisodeOfCareStatus> status;

        /**
         * The period during this EpisodeOfCare that the specific status applied.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Period for the status", formalDefinition="The period during this EpisodeOfCare that the specific status applied." )
        protected Period period;

        private static final long serialVersionUID = -1192432864L;

    /**
     * Constructor
     */
      public EpisodeOfCareStatusHistoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public EpisodeOfCareStatusHistoryComponent(Enumeration<EpisodeOfCareStatus> status, Period period) {
        super();
        this.status = status;
        this.period = period;
      }

        /**
         * @return {@link #status} (planned | waitlist | active | onhold | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<EpisodeOfCareStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EpisodeOfCareStatusHistoryComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<EpisodeOfCareStatus>(new EpisodeOfCareStatusEnumFactory()); // bb
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (planned | waitlist | active | onhold | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public EpisodeOfCareStatusHistoryComponent setStatusElement(Enumeration<EpisodeOfCareStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return planned | waitlist | active | onhold | finished | cancelled.
         */
        public EpisodeOfCareStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value planned | waitlist | active | onhold | finished | cancelled.
         */
        public EpisodeOfCareStatusHistoryComponent setStatus(EpisodeOfCareStatus value) { 
            if (this.status == null)
              this.status = new Enumeration<EpisodeOfCareStatus>(new EpisodeOfCareStatusEnumFactory());
            this.status.setValue(value);
          return this;
        }

        /**
         * @return {@link #period} (The period during this EpisodeOfCare that the specific status applied.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EpisodeOfCareStatusHistoryComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The period during this EpisodeOfCare that the specific status applied.)
         */
        public EpisodeOfCareStatusHistoryComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("status", "code", "planned | waitlist | active | onhold | finished | cancelled.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("period", "Period", "The period during this EpisodeOfCare that the specific status applied.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EpisodeOfCareStatus>
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -892481550: // status
          this.status = new EpisodeOfCareStatusEnumFactory().fromType(value); // Enumeration<EpisodeOfCareStatus>
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("status"))
          this.status = new EpisodeOfCareStatusEnumFactory().fromType(value); // Enumeration<EpisodeOfCareStatus>
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<EpisodeOfCareStatus>
        case -991726143:  return getPeriod(); // Period
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type EpisodeOfCare.status");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else
          return super.addChild(name);
      }

      public EpisodeOfCareStatusHistoryComponent copy() {
        EpisodeOfCareStatusHistoryComponent dst = new EpisodeOfCareStatusHistoryComponent();
        copyValues(dst);
        dst.status = status == null ? null : status.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EpisodeOfCareStatusHistoryComponent))
          return false;
        EpisodeOfCareStatusHistoryComponent o = (EpisodeOfCareStatusHistoryComponent) other;
        return compareDeep(status, o.status, true) && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EpisodeOfCareStatusHistoryComponent))
          return false;
        EpisodeOfCareStatusHistoryComponent o = (EpisodeOfCareStatusHistoryComponent) other;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (status == null || status.isEmpty()) && (period == null || period.isEmpty())
          ;
      }

  public String fhirType() {
    return "EpisodeOfCare.statusHistory";

  }

  }

    /**
     * Identifier(s) by which this EpisodeOfCare is known.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Identifier(s) for the EpisodeOfCare", formalDefinition="Identifier(s) by which this EpisodeOfCare is known." )
    protected List<Identifier> identifier;

    /**
     * planned | waitlist | active | onhold | finished | cancelled.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="planned | waitlist | active | onhold | finished | cancelled", formalDefinition="planned | waitlist | active | onhold | finished | cancelled." )
    protected Enumeration<EpisodeOfCareStatus> status;

    /**
     * The history of statuses that the EpisodeOfCare has been through (without requiring processing the history of the resource).
     */
    @Child(name = "statusHistory", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Past list of status codes", formalDefinition="The history of statuses that the EpisodeOfCare has been through (without requiring processing the history of the resource)." )
    protected List<EpisodeOfCareStatusHistoryComponent> statusHistory;

    /**
     * A classification of the type of episode of care; e.g. specialist referral, disease management, type of funded care.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Type/class  - e.g. specialist referral, disease management", formalDefinition="A classification of the type of episode of care; e.g. specialist referral, disease management, type of funded care." )
    protected List<CodeableConcept> type;

    /**
     * A list of conditions/problems/diagnoses that this episode of care is intended to be providing care for.
     */
    @Child(name = "condition", type = {Condition.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Conditions/problems/diagnoses this episode of care is for", formalDefinition="A list of conditions/problems/diagnoses that this episode of care is intended to be providing care for." )
    protected List<Reference> condition;
    /**
     * The actual objects that are the target of the reference (A list of conditions/problems/diagnoses that this episode of care is intended to be providing care for.)
     */
    protected List<Condition> conditionTarget;


    /**
     * The patient that this EpisodeOfCare applies to.
     */
    @Child(name = "patient", type = {Patient.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient for this episode of care", formalDefinition="The patient that this EpisodeOfCare applies to." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient that this EpisodeOfCare applies to.)
     */
    protected Patient patientTarget;

    /**
     * The organization that has assumed the specific responsibilities for the specified duration.
     */
    @Child(name = "managingOrganization", type = {Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization that assumes care", formalDefinition="The organization that has assumed the specific responsibilities for the specified duration." )
    protected Reference managingOrganization;

    /**
     * The actual object that is the target of the reference (The organization that has assumed the specific responsibilities for the specified duration.)
     */
    protected Organization managingOrganizationTarget;

    /**
     * The interval during which the managing organization assumes the defined responsibility.
     */
    @Child(name = "period", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Interval during responsibility is assumed", formalDefinition="The interval during which the managing organization assumes the defined responsibility." )
    protected Period period;

    /**
     * Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.
     */
    @Child(name = "referralRequest", type = {ReferralRequest.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Originating Referral Request(s)", formalDefinition="Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals." )
    protected List<Reference> referralRequest;
    /**
     * The actual objects that are the target of the reference (Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.)
     */
    protected List<ReferralRequest> referralRequestTarget;


    /**
     * The practitioner that is the care manager/care co-ordinator for this patient.
     */
    @Child(name = "careManager", type = {Practitioner.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Care manager/care co-ordinator for the patient", formalDefinition="The practitioner that is the care manager/care co-ordinator for this patient." )
    protected Reference careManager;

    /**
     * The actual object that is the target of the reference (The practitioner that is the care manager/care co-ordinator for this patient.)
     */
    protected Practitioner careManagerTarget;

    /**
     * The list of practitioners that may be facilitating this episode of care for specific purposes.
     */
    @Child(name = "team", type = {CareTeam.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Other practitioners facilitating this episode of care", formalDefinition="The list of practitioners that may be facilitating this episode of care for specific purposes." )
    protected List<Reference> team;
    /**
     * The actual objects that are the target of the reference (The list of practitioners that may be facilitating this episode of care for specific purposes.)
     */
    protected List<CareTeam> teamTarget;


    private static final long serialVersionUID = 922419354L;

  /**
   * Constructor
   */
    public EpisodeOfCare() {
      super();
    }

  /**
   * Constructor
   */
    public EpisodeOfCare(Enumeration<EpisodeOfCareStatus> status, Reference patient) {
      super();
      this.status = status;
      this.patient = patient;
    }

    /**
     * @return {@link #identifier} (Identifier(s) by which this EpisodeOfCare is known.)
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
     * @return {@link #identifier} (Identifier(s) by which this EpisodeOfCare is known.)
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
    public EpisodeOfCare addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #status} (planned | waitlist | active | onhold | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<EpisodeOfCareStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EpisodeOfCare.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EpisodeOfCareStatus>(new EpisodeOfCareStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (planned | waitlist | active | onhold | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public EpisodeOfCare setStatusElement(Enumeration<EpisodeOfCareStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return planned | waitlist | active | onhold | finished | cancelled.
     */
    public EpisodeOfCareStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value planned | waitlist | active | onhold | finished | cancelled.
     */
    public EpisodeOfCare setStatus(EpisodeOfCareStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<EpisodeOfCareStatus>(new EpisodeOfCareStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #statusHistory} (The history of statuses that the EpisodeOfCare has been through (without requiring processing the history of the resource).)
     */
    public List<EpisodeOfCareStatusHistoryComponent> getStatusHistory() { 
      if (this.statusHistory == null)
        this.statusHistory = new ArrayList<EpisodeOfCareStatusHistoryComponent>();
      return this.statusHistory;
    }

    public boolean hasStatusHistory() { 
      if (this.statusHistory == null)
        return false;
      for (EpisodeOfCareStatusHistoryComponent item : this.statusHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #statusHistory} (The history of statuses that the EpisodeOfCare has been through (without requiring processing the history of the resource).)
     */
    // syntactic sugar
    public EpisodeOfCareStatusHistoryComponent addStatusHistory() { //3
      EpisodeOfCareStatusHistoryComponent t = new EpisodeOfCareStatusHistoryComponent();
      if (this.statusHistory == null)
        this.statusHistory = new ArrayList<EpisodeOfCareStatusHistoryComponent>();
      this.statusHistory.add(t);
      return t;
    }

    // syntactic sugar
    public EpisodeOfCare addStatusHistory(EpisodeOfCareStatusHistoryComponent t) { //3
      if (t == null)
        return this;
      if (this.statusHistory == null)
        this.statusHistory = new ArrayList<EpisodeOfCareStatusHistoryComponent>();
      this.statusHistory.add(t);
      return this;
    }

    /**
     * @return {@link #type} (A classification of the type of episode of care; e.g. specialist referral, disease management, type of funded care.)
     */
    public List<CodeableConcept> getType() { 
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      return this.type;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (CodeableConcept item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #type} (A classification of the type of episode of care; e.g. specialist referral, disease management, type of funded care.)
     */
    // syntactic sugar
    public CodeableConcept addType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return t;
    }

    // syntactic sugar
    public EpisodeOfCare addType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return this;
    }

    /**
     * @return {@link #condition} (A list of conditions/problems/diagnoses that this episode of care is intended to be providing care for.)
     */
    public List<Reference> getCondition() { 
      if (this.condition == null)
        this.condition = new ArrayList<Reference>();
      return this.condition;
    }

    public boolean hasCondition() { 
      if (this.condition == null)
        return false;
      for (Reference item : this.condition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #condition} (A list of conditions/problems/diagnoses that this episode of care is intended to be providing care for.)
     */
    // syntactic sugar
    public Reference addCondition() { //3
      Reference t = new Reference();
      if (this.condition == null)
        this.condition = new ArrayList<Reference>();
      this.condition.add(t);
      return t;
    }

    // syntactic sugar
    public EpisodeOfCare addCondition(Reference t) { //3
      if (t == null)
        return this;
      if (this.condition == null)
        this.condition = new ArrayList<Reference>();
      this.condition.add(t);
      return this;
    }

    /**
     * @return {@link #condition} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A list of conditions/problems/diagnoses that this episode of care is intended to be providing care for.)
     */
    public List<Condition> getConditionTarget() { 
      if (this.conditionTarget == null)
        this.conditionTarget = new ArrayList<Condition>();
      return this.conditionTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #condition} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A list of conditions/problems/diagnoses that this episode of care is intended to be providing care for.)
     */
    public Condition addConditionTarget() { 
      Condition r = new Condition();
      if (this.conditionTarget == null)
        this.conditionTarget = new ArrayList<Condition>();
      this.conditionTarget.add(r);
      return r;
    }

    /**
     * @return {@link #patient} (The patient that this EpisodeOfCare applies to.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EpisodeOfCare.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient that this EpisodeOfCare applies to.)
     */
    public EpisodeOfCare setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient that this EpisodeOfCare applies to.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EpisodeOfCare.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient that this EpisodeOfCare applies to.)
     */
    public EpisodeOfCare setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #managingOrganization} (The organization that has assumed the specific responsibilities for the specified duration.)
     */
    public Reference getManagingOrganization() { 
      if (this.managingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EpisodeOfCare.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganization = new Reference(); // cc
      return this.managingOrganization;
    }

    public boolean hasManagingOrganization() { 
      return this.managingOrganization != null && !this.managingOrganization.isEmpty();
    }

    /**
     * @param value {@link #managingOrganization} (The organization that has assumed the specific responsibilities for the specified duration.)
     */
    public EpisodeOfCare setManagingOrganization(Reference value) { 
      this.managingOrganization = value;
      return this;
    }

    /**
     * @return {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization that has assumed the specific responsibilities for the specified duration.)
     */
    public Organization getManagingOrganizationTarget() { 
      if (this.managingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EpisodeOfCare.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganizationTarget = new Organization(); // aa
      return this.managingOrganizationTarget;
    }

    /**
     * @param value {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization that has assumed the specific responsibilities for the specified duration.)
     */
    public EpisodeOfCare setManagingOrganizationTarget(Organization value) { 
      this.managingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #period} (The interval during which the managing organization assumes the defined responsibility.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EpisodeOfCare.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The interval during which the managing organization assumes the defined responsibility.)
     */
    public EpisodeOfCare setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #referralRequest} (Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.)
     */
    public List<Reference> getReferralRequest() { 
      if (this.referralRequest == null)
        this.referralRequest = new ArrayList<Reference>();
      return this.referralRequest;
    }

    public boolean hasReferralRequest() { 
      if (this.referralRequest == null)
        return false;
      for (Reference item : this.referralRequest)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #referralRequest} (Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.)
     */
    // syntactic sugar
    public Reference addReferralRequest() { //3
      Reference t = new Reference();
      if (this.referralRequest == null)
        this.referralRequest = new ArrayList<Reference>();
      this.referralRequest.add(t);
      return t;
    }

    // syntactic sugar
    public EpisodeOfCare addReferralRequest(Reference t) { //3
      if (t == null)
        return this;
      if (this.referralRequest == null)
        this.referralRequest = new ArrayList<Reference>();
      this.referralRequest.add(t);
      return this;
    }

    /**
     * @return {@link #referralRequest} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.)
     */
    public List<ReferralRequest> getReferralRequestTarget() { 
      if (this.referralRequestTarget == null)
        this.referralRequestTarget = new ArrayList<ReferralRequest>();
      return this.referralRequestTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #referralRequest} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.)
     */
    public ReferralRequest addReferralRequestTarget() { 
      ReferralRequest r = new ReferralRequest();
      if (this.referralRequestTarget == null)
        this.referralRequestTarget = new ArrayList<ReferralRequest>();
      this.referralRequestTarget.add(r);
      return r;
    }

    /**
     * @return {@link #careManager} (The practitioner that is the care manager/care co-ordinator for this patient.)
     */
    public Reference getCareManager() { 
      if (this.careManager == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EpisodeOfCare.careManager");
        else if (Configuration.doAutoCreate())
          this.careManager = new Reference(); // cc
      return this.careManager;
    }

    public boolean hasCareManager() { 
      return this.careManager != null && !this.careManager.isEmpty();
    }

    /**
     * @param value {@link #careManager} (The practitioner that is the care manager/care co-ordinator for this patient.)
     */
    public EpisodeOfCare setCareManager(Reference value) { 
      this.careManager = value;
      return this;
    }

    /**
     * @return {@link #careManager} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner that is the care manager/care co-ordinator for this patient.)
     */
    public Practitioner getCareManagerTarget() { 
      if (this.careManagerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EpisodeOfCare.careManager");
        else if (Configuration.doAutoCreate())
          this.careManagerTarget = new Practitioner(); // aa
      return this.careManagerTarget;
    }

    /**
     * @param value {@link #careManager} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner that is the care manager/care co-ordinator for this patient.)
     */
    public EpisodeOfCare setCareManagerTarget(Practitioner value) { 
      this.careManagerTarget = value;
      return this;
    }

    /**
     * @return {@link #team} (The list of practitioners that may be facilitating this episode of care for specific purposes.)
     */
    public List<Reference> getTeam() { 
      if (this.team == null)
        this.team = new ArrayList<Reference>();
      return this.team;
    }

    public boolean hasTeam() { 
      if (this.team == null)
        return false;
      for (Reference item : this.team)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #team} (The list of practitioners that may be facilitating this episode of care for specific purposes.)
     */
    // syntactic sugar
    public Reference addTeam() { //3
      Reference t = new Reference();
      if (this.team == null)
        this.team = new ArrayList<Reference>();
      this.team.add(t);
      return t;
    }

    // syntactic sugar
    public EpisodeOfCare addTeam(Reference t) { //3
      if (t == null)
        return this;
      if (this.team == null)
        this.team = new ArrayList<Reference>();
      this.team.add(t);
      return this;
    }

    /**
     * @return {@link #team} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The list of practitioners that may be facilitating this episode of care for specific purposes.)
     */
    public List<CareTeam> getTeamTarget() { 
      if (this.teamTarget == null)
        this.teamTarget = new ArrayList<CareTeam>();
      return this.teamTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #team} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The list of practitioners that may be facilitating this episode of care for specific purposes.)
     */
    public CareTeam addTeamTarget() { 
      CareTeam r = new CareTeam();
      if (this.teamTarget == null)
        this.teamTarget = new ArrayList<CareTeam>();
      this.teamTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier(s) by which this EpisodeOfCare is known.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "planned | waitlist | active | onhold | finished | cancelled.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("statusHistory", "", "The history of statuses that the EpisodeOfCare has been through (without requiring processing the history of the resource).", 0, java.lang.Integer.MAX_VALUE, statusHistory));
        childrenList.add(new Property("type", "CodeableConcept", "A classification of the type of episode of care; e.g. specialist referral, disease management, type of funded care.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("condition", "Reference(Condition)", "A list of conditions/problems/diagnoses that this episode of care is intended to be providing care for.", 0, java.lang.Integer.MAX_VALUE, condition));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient that this EpisodeOfCare applies to.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("managingOrganization", "Reference(Organization)", "The organization that has assumed the specific responsibilities for the specified duration.", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
        childrenList.add(new Property("period", "Period", "The interval during which the managing organization assumes the defined responsibility.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("referralRequest", "Reference(ReferralRequest)", "Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.", 0, java.lang.Integer.MAX_VALUE, referralRequest));
        childrenList.add(new Property("careManager", "Reference(Practitioner)", "The practitioner that is the care manager/care co-ordinator for this patient.", 0, java.lang.Integer.MAX_VALUE, careManager));
        childrenList.add(new Property("team", "Reference(CareTeam)", "The list of practitioners that may be facilitating this episode of care for specific purposes.", 0, java.lang.Integer.MAX_VALUE, team));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EpisodeOfCareStatus>
        case -986695614: /*statusHistory*/ return this.statusHistory == null ? new Base[0] : this.statusHistory.toArray(new Base[this.statusHistory.size()]); // EpisodeOfCareStatusHistoryComponent
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : this.condition.toArray(new Base[this.condition.size()]); // Reference
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -2058947787: /*managingOrganization*/ return this.managingOrganization == null ? new Base[0] : new Base[] {this.managingOrganization}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -310299598: /*referralRequest*/ return this.referralRequest == null ? new Base[0] : this.referralRequest.toArray(new Base[this.referralRequest.size()]); // Reference
        case -1147746468: /*careManager*/ return this.careManager == null ? new Base[0] : new Base[] {this.careManager}; // Reference
        case 3555933: /*team*/ return this.team == null ? new Base[0] : this.team.toArray(new Base[this.team.size()]); // Reference
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
          this.status = new EpisodeOfCareStatusEnumFactory().fromType(value); // Enumeration<EpisodeOfCareStatus>
          break;
        case -986695614: // statusHistory
          this.getStatusHistory().add((EpisodeOfCareStatusHistoryComponent) value); // EpisodeOfCareStatusHistoryComponent
          break;
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -861311717: // condition
          this.getCondition().add(castToReference(value)); // Reference
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -2058947787: // managingOrganization
          this.managingOrganization = castToReference(value); // Reference
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case -310299598: // referralRequest
          this.getReferralRequest().add(castToReference(value)); // Reference
          break;
        case -1147746468: // careManager
          this.careManager = castToReference(value); // Reference
          break;
        case 3555933: // team
          this.getTeam().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new EpisodeOfCareStatusEnumFactory().fromType(value); // Enumeration<EpisodeOfCareStatus>
        else if (name.equals("statusHistory"))
          this.getStatusHistory().add((EpisodeOfCareStatusHistoryComponent) value);
        else if (name.equals("type"))
          this.getType().add(castToCodeableConcept(value));
        else if (name.equals("condition"))
          this.getCondition().add(castToReference(value));
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("managingOrganization"))
          this.managingOrganization = castToReference(value); // Reference
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("referralRequest"))
          this.getReferralRequest().add(castToReference(value));
        else if (name.equals("careManager"))
          this.careManager = castToReference(value); // Reference
        else if (name.equals("team"))
          this.getTeam().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<EpisodeOfCareStatus>
        case -986695614:  return addStatusHistory(); // EpisodeOfCareStatusHistoryComponent
        case 3575610:  return addType(); // CodeableConcept
        case -861311717:  return addCondition(); // Reference
        case -791418107:  return getPatient(); // Reference
        case -2058947787:  return getManagingOrganization(); // Reference
        case -991726143:  return getPeriod(); // Period
        case -310299598:  return addReferralRequest(); // Reference
        case -1147746468:  return getCareManager(); // Reference
        case 3555933:  return addTeam(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type EpisodeOfCare.status");
        }
        else if (name.equals("statusHistory")) {
          return addStatusHistory();
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("condition")) {
          return addCondition();
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("managingOrganization")) {
          this.managingOrganization = new Reference();
          return this.managingOrganization;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("referralRequest")) {
          return addReferralRequest();
        }
        else if (name.equals("careManager")) {
          this.careManager = new Reference();
          return this.careManager;
        }
        else if (name.equals("team")) {
          return addTeam();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "EpisodeOfCare";

  }

      public EpisodeOfCare copy() {
        EpisodeOfCare dst = new EpisodeOfCare();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        if (statusHistory != null) {
          dst.statusHistory = new ArrayList<EpisodeOfCareStatusHistoryComponent>();
          for (EpisodeOfCareStatusHistoryComponent i : statusHistory)
            dst.statusHistory.add(i.copy());
        };
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        if (condition != null) {
          dst.condition = new ArrayList<Reference>();
          for (Reference i : condition)
            dst.condition.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.managingOrganization = managingOrganization == null ? null : managingOrganization.copy();
        dst.period = period == null ? null : period.copy();
        if (referralRequest != null) {
          dst.referralRequest = new ArrayList<Reference>();
          for (Reference i : referralRequest)
            dst.referralRequest.add(i.copy());
        };
        dst.careManager = careManager == null ? null : careManager.copy();
        if (team != null) {
          dst.team = new ArrayList<Reference>();
          for (Reference i : team)
            dst.team.add(i.copy());
        };
        return dst;
      }

      protected EpisodeOfCare typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EpisodeOfCare))
          return false;
        EpisodeOfCare o = (EpisodeOfCare) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(statusHistory, o.statusHistory, true)
           && compareDeep(type, o.type, true) && compareDeep(condition, o.condition, true) && compareDeep(patient, o.patient, true)
           && compareDeep(managingOrganization, o.managingOrganization, true) && compareDeep(period, o.period, true)
           && compareDeep(referralRequest, o.referralRequest, true) && compareDeep(careManager, o.careManager, true)
           && compareDeep(team, o.team, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EpisodeOfCare))
          return false;
        EpisodeOfCare o = (EpisodeOfCare) other;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (statusHistory == null || statusHistory.isEmpty()) && (type == null || type.isEmpty())
           && (condition == null || condition.isEmpty()) && (patient == null || patient.isEmpty()) && (managingOrganization == null || managingOrganization.isEmpty())
           && (period == null || period.isEmpty()) && (referralRequest == null || referralRequest.isEmpty())
           && (careManager == null || careManager.isEmpty()) && (team == null || team.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.EpisodeOfCare;
   }

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The organization that has assumed the specific responsibilities of this EpisodeOfCare</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.managingOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="EpisodeOfCare.managingOrganization", description="The organization that has assumed the specific responsibilities of this EpisodeOfCare", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The organization that has assumed the specific responsibilities of this EpisodeOfCare</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.managingOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EpisodeOfCare:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("EpisodeOfCare:organization").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Patient for this episode of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="EpisodeOfCare.patient", description="Patient for this episode of care", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Patient for this episode of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EpisodeOfCare:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("EpisodeOfCare:patient").toLocked();

 /**
   * Search parameter: <b>condition</b>
   * <p>
   * Description: <b>Conditions/problems/diagnoses this episode of care is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.condition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="condition", path="EpisodeOfCare.condition", description="Conditions/problems/diagnoses this episode of care is for", type="reference" )
  public static final String SP_CONDITION = "condition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>condition</b>
   * <p>
   * Description: <b>Conditions/problems/diagnoses this episode of care is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.condition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONDITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONDITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EpisodeOfCare:condition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONDITION = new ca.uhn.fhir.model.api.Include("EpisodeOfCare:condition").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the Episode of Care as provided (does not check the status history collection)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EpisodeOfCare.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="EpisodeOfCare.status", description="The current status of the Episode of Care as provided (does not check the status history collection)", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the Episode of Care as provided (does not check the status history collection)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EpisodeOfCare.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>care-manager</b>
   * <p>
   * Description: <b>Care manager/care co-ordinator for the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.careManager</b><br>
   * </p>
   */
  @SearchParamDefinition(name="care-manager", path="EpisodeOfCare.careManager", description="Care manager/care co-ordinator for the patient", type="reference" )
  public static final String SP_CARE_MANAGER = "care-manager";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>care-manager</b>
   * <p>
   * Description: <b>Care manager/care co-ordinator for the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.careManager</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CARE_MANAGER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CARE_MANAGER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EpisodeOfCare:care-manager</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CARE_MANAGER = new ca.uhn.fhir.model.api.Include("EpisodeOfCare:care-manager").toLocked();

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Type/class  - e.g. specialist referral, disease management</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EpisodeOfCare.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="EpisodeOfCare.type", description="Type/class  - e.g. specialist referral, disease management", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Type/class  - e.g. specialist referral, disease management</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EpisodeOfCare.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The provided date search value falls within the episode of care's period</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EpisodeOfCare.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="EpisodeOfCare.period", description="The provided date search value falls within the episode of care's period", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The provided date search value falls within the episode of care's period</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EpisodeOfCare.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>incomingreferral</b>
   * <p>
   * Description: <b>Incoming Referral Request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.referralRequest</b><br>
   * </p>
   */
  @SearchParamDefinition(name="incomingreferral", path="EpisodeOfCare.referralRequest", description="Incoming Referral Request", type="reference" )
  public static final String SP_INCOMINGREFERRAL = "incomingreferral";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>incomingreferral</b>
   * <p>
   * Description: <b>Incoming Referral Request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EpisodeOfCare.referralRequest</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INCOMINGREFERRAL = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INCOMINGREFERRAL);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EpisodeOfCare:incomingreferral</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INCOMINGREFERRAL = new ca.uhn.fhir.model.api.Include("EpisodeOfCare:incomingreferral").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Identifier(s) for the EpisodeOfCare</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EpisodeOfCare.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="EpisodeOfCare.identifier", description="Identifier(s) for the EpisodeOfCare", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Identifier(s) for the EpisodeOfCare</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EpisodeOfCare.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

