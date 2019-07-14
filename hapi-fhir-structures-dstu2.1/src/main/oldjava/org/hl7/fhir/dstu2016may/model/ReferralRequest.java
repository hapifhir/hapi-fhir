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
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organization.
 */
@ResourceDef(name="ReferralRequest", profile="http://hl7.org/fhir/Profile/ReferralRequest")
public class ReferralRequest extends DomainResource {

    public enum ReferralStatus {
        /**
         * A draft referral that has yet to be send.
         */
        DRAFT, 
        /**
         * The referral is complete and is ready for fulfillment.
         */
        ACTIVE, 
        /**
         * The referral has been cancelled without being completed. For example it is no longer needed.
         */
        CANCELLED, 
        /**
         * The referral has been completely actioned.
         */
        COMPLETED, 
        /**
         * This referral record should never have existed, though it's possible some degree of real-world activity or decisions may have been taken due to its existence
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ReferralStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown ReferralStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/referralstatus";
            case ACTIVE: return "http://hl7.org/fhir/referralstatus";
            case CANCELLED: return "http://hl7.org/fhir/referralstatus";
            case COMPLETED: return "http://hl7.org/fhir/referralstatus";
            case ENTEREDINERROR: return "http://hl7.org/fhir/referralstatus";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "A draft referral that has yet to be send.";
            case ACTIVE: return "The referral is complete and is ready for fulfillment.";
            case CANCELLED: return "The referral has been cancelled without being completed. For example it is no longer needed.";
            case COMPLETED: return "The referral has been completely actioned.";
            case ENTEREDINERROR: return "This referral record should never have existed, though it's possible some degree of real-world activity or decisions may have been taken due to its existence";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case CANCELLED: return "Cancelled";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class ReferralStatusEnumFactory implements EnumFactory<ReferralStatus> {
    public ReferralStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ReferralStatus.DRAFT;
        if ("active".equals(codeString))
          return ReferralStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return ReferralStatus.CANCELLED;
        if ("completed".equals(codeString))
          return ReferralStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ReferralStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ReferralStatus code '"+codeString+"'");
        }
        public Enumeration<ReferralStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ReferralStatus>(this, ReferralStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<ReferralStatus>(this, ReferralStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<ReferralStatus>(this, ReferralStatus.CANCELLED);
        if ("completed".equals(codeString))
          return new Enumeration<ReferralStatus>(this, ReferralStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ReferralStatus>(this, ReferralStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ReferralStatus code '"+codeString+"'");
        }
    public String toCode(ReferralStatus code) {
      if (code == ReferralStatus.DRAFT)
        return "draft";
      if (code == ReferralStatus.ACTIVE)
        return "active";
      if (code == ReferralStatus.CANCELLED)
        return "cancelled";
      if (code == ReferralStatus.COMPLETED)
        return "completed";
      if (code == ReferralStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ReferralStatus code) {
      return code.getSystem();
      }
    }

    public enum ReferralCategory {
        /**
         * The referral request represents a suggestion or recommendation that a referral be made.
         */
        PROPOSAL, 
        /**
         * The referral request represents an intention by the author to make a referral, but no actual referral has yet been made/authorized.
         */
        PLAN, 
        /**
         * This is an actual referral request which, when active, will have the authorizations needed to allow it to be actioned.
         */
        REQUEST, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ReferralCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposal".equals(codeString))
          return PROPOSAL;
        if ("plan".equals(codeString))
          return PLAN;
        if ("request".equals(codeString))
          return REQUEST;
        throw new FHIRException("Unknown ReferralCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSAL: return "proposal";
            case PLAN: return "plan";
            case REQUEST: return "request";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSAL: return "http://hl7.org/fhir/referralcategory";
            case PLAN: return "http://hl7.org/fhir/referralcategory";
            case REQUEST: return "http://hl7.org/fhir/referralcategory";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSAL: return "The referral request represents a suggestion or recommendation that a referral be made.";
            case PLAN: return "The referral request represents an intention by the author to make a referral, but no actual referral has yet been made/authorized.";
            case REQUEST: return "This is an actual referral request which, when active, will have the authorizations needed to allow it to be actioned.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSAL: return "Proposal";
            case PLAN: return "Plan";
            case REQUEST: return "Request";
            default: return "?";
          }
        }
    }

  public static class ReferralCategoryEnumFactory implements EnumFactory<ReferralCategory> {
    public ReferralCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposal".equals(codeString))
          return ReferralCategory.PROPOSAL;
        if ("plan".equals(codeString))
          return ReferralCategory.PLAN;
        if ("request".equals(codeString))
          return ReferralCategory.REQUEST;
        throw new IllegalArgumentException("Unknown ReferralCategory code '"+codeString+"'");
        }
        public Enumeration<ReferralCategory> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposal".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.PROPOSAL);
        if ("plan".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.PLAN);
        if ("request".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.REQUEST);
        throw new FHIRException("Unknown ReferralCategory code '"+codeString+"'");
        }
    public String toCode(ReferralCategory code) {
      if (code == ReferralCategory.PROPOSAL)
        return "proposal";
      if (code == ReferralCategory.PLAN)
        return "plan";
      if (code == ReferralCategory.REQUEST)
        return "request";
      return "?";
      }
    public String toSystem(ReferralCategory code) {
      return code.getSystem();
      }
    }

    /**
     * Business identifier that uniquely identifies the referral/care transfer request instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business identifier", formalDefinition="Business identifier that uniquely identifies the referral/care transfer request instance." )
    protected List<Identifier> identifier;

    /**
     * Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.
     */
    @Child(name = "basedOn", type = {ReferralRequest.class, CarePlan.class, DiagnosticOrder.class, ProcedureRequest.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Request fulfilled by this request", formalDefinition="Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * The business identifier of the logical "grouping" request/order that this referral is a part of.
     */
    @Child(name = "parent", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Composite request this is part of", formalDefinition="The business identifier of the logical \"grouping\" request/order that this referral is a part of." )
    protected Identifier parent;

    /**
     * The status of the authorization/intention reflected by the referral request record.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | cancelled | completed | entered-in-error", formalDefinition="The status of the authorization/intention reflected by the referral request record." )
    protected Enumeration<ReferralStatus> status;

    /**
     * Distinguishes the "level" of authorization/demand implicit in this request.
     */
    @Child(name = "category", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposal | plan | request", formalDefinition="Distinguishes the \"level\" of authorization/demand implicit in this request." )
    protected Enumeration<ReferralCategory> category;

    /**
     * An indication of the type of referral (or where applicable the type of transfer of care) request.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Referral/Transition of care request type", formalDefinition="An indication of the type of referral (or where applicable the type of transfer of care) request." )
    protected CodeableConcept type;

    /**
     * An indication of the urgency of referral (or where applicable the type of transfer of care) request.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Urgency of referral / transfer of care request", formalDefinition="An indication of the urgency of referral (or where applicable the type of transfer of care) request." )
    protected CodeableConcept priority;

    /**
     * The patient who is the subject of a referral or transfer of care request.
     */
    @Child(name = "patient", type = {Patient.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient referred to care or transfer", formalDefinition="The patient who is the subject of a referral or transfer of care request." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient who is the subject of a referral or transfer of care request.)
     */
    protected Patient patientTarget;

    /**
     * The encounter at which the request for referral or transfer of care is initiated.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Originating encounter", formalDefinition="The encounter at which the request for referral or transfer of care is initiated." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter at which the request for referral or transfer of care is initiated.)
     */
    protected Resource contextTarget;

    /**
     * The period of time within which the services identified in the referral/transfer of care is specified or required to occur.
     */
    @Child(name = "fulfillmentTime", type = {Period.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Requested service(s) fulfillment time", formalDefinition="The period of time within which the services identified in the referral/transfer of care is specified or required to occur." )
    protected Period fulfillmentTime;

    /**
     * Date/DateTime of creation for draft requests and date of activation for active requests.
     */
    @Child(name = "authored", type = {DateTimeType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date of creation/activation", formalDefinition="Date/DateTime of creation for draft requests and date of activation for active requests." )
    protected DateTimeType authored;

    /**
     * The healthcare provider or provider organization who/which initiated the referral/transfer of care request. Can also be  Patient (a self referral).
     */
    @Child(name = "requester", type = {Practitioner.class, Organization.class, Patient.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Requester of referral / transfer of care", formalDefinition="The healthcare provider or provider organization who/which initiated the referral/transfer of care request. Can also be  Patient (a self referral)." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (The healthcare provider or provider organization who/which initiated the referral/transfer of care request. Can also be  Patient (a self referral).)
     */
    protected Resource requesterTarget;

    /**
     * Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.  For example: Cardiology Gastroenterology Diabetology.
     */
    @Child(name = "specialty", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The clinical specialty (discipline) that the referral is requested for", formalDefinition="Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.  For example: Cardiology Gastroenterology Diabetology." )
    protected CodeableConcept specialty;

    /**
     * The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.
     */
    @Child(name = "recipient", type = {Practitioner.class, Organization.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Receiver of referral / transfer of care request", formalDefinition="The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.)
     */
    protected List<Resource> recipientTarget;


    /**
     * Description of clinical condition indicating why referral/transfer of care is requested.  For example:  Pathological Anomalies, Disabled (physical or mental),  Behavioral Management.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for referral / transfer of care request", formalDefinition="Description of clinical condition indicating why referral/transfer of care is requested.  For example:  Pathological Anomalies, Disabled (physical or mental),  Behavioral Management." )
    protected CodeableConcept reason;

    /**
     * The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.
     */
    @Child(name = "description", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A textual description of the referral", formalDefinition="The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary." )
    protected StringType description;

    /**
     * The service(s) that is/are requested to be provided to the patient.  For example: cardiac pacemaker insertion.
     */
    @Child(name = "serviceRequested", type = {CodeableConcept.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Actions requested as part of the referral", formalDefinition="The service(s) that is/are requested to be provided to the patient.  For example: cardiac pacemaker insertion." )
    protected List<CodeableConcept> serviceRequested;

    /**
     * Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.
     */
    @Child(name = "supportingInformation", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additonal information to support referral or transfer of care request", formalDefinition="Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan." )
    protected List<Reference> supportingInformation;
    /**
     * The actual objects that are the target of the reference (Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.)
     */
    protected List<Resource> supportingInformationTarget;


    private static final long serialVersionUID = -1030392098L;

  /**
   * Constructor
   */
    public ReferralRequest() {
      super();
    }

  /**
   * Constructor
   */
    public ReferralRequest(Enumeration<ReferralStatus> status, Enumeration<ReferralCategory> category) {
      super();
      this.status = status;
      this.category = category;
    }

    /**
     * @return {@link #identifier} (Business identifier that uniquely identifies the referral/care transfer request instance.)
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
     * @return {@link #identifier} (Business identifier that uniquely identifies the referral/care transfer request instance.)
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
    public ReferralRequest addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #basedOn} (Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #basedOn} (Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.)
     */
    // syntactic sugar
    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    // syntactic sugar
    public ReferralRequest addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return {@link #basedOn} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.)
     */
    public List<Resource> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<Resource>();
      return this.basedOnTarget;
    }

    /**
     * @return {@link #parent} (The business identifier of the logical "grouping" request/order that this referral is a part of.)
     */
    public Identifier getParent() { 
      if (this.parent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.parent");
        else if (Configuration.doAutoCreate())
          this.parent = new Identifier(); // cc
      return this.parent;
    }

    public boolean hasParent() { 
      return this.parent != null && !this.parent.isEmpty();
    }

    /**
     * @param value {@link #parent} (The business identifier of the logical "grouping" request/order that this referral is a part of.)
     */
    public ReferralRequest setParent(Identifier value) { 
      this.parent = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the authorization/intention reflected by the referral request record.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ReferralStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ReferralStatus>(new ReferralStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the authorization/intention reflected by the referral request record.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ReferralRequest setStatusElement(Enumeration<ReferralStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the authorization/intention reflected by the referral request record.
     */
    public ReferralStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the authorization/intention reflected by the referral request record.
     */
    public ReferralRequest setStatus(ReferralStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ReferralStatus>(new ReferralStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (Distinguishes the "level" of authorization/demand implicit in this request.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public Enumeration<ReferralCategory> getCategoryElement() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.category");
        else if (Configuration.doAutoCreate())
          this.category = new Enumeration<ReferralCategory>(new ReferralCategoryEnumFactory()); // bb
      return this.category;
    }

    public boolean hasCategoryElement() { 
      return this.category != null && !this.category.isEmpty();
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Distinguishes the "level" of authorization/demand implicit in this request.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public ReferralRequest setCategoryElement(Enumeration<ReferralCategory> value) { 
      this.category = value;
      return this;
    }

    /**
     * @return Distinguishes the "level" of authorization/demand implicit in this request.
     */
    public ReferralCategory getCategory() { 
      return this.category == null ? null : this.category.getValue();
    }

    /**
     * @param value Distinguishes the "level" of authorization/demand implicit in this request.
     */
    public ReferralRequest setCategory(ReferralCategory value) { 
        if (this.category == null)
          this.category = new Enumeration<ReferralCategory>(new ReferralCategoryEnumFactory());
        this.category.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (An indication of the type of referral (or where applicable the type of transfer of care) request.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (An indication of the type of referral (or where applicable the type of transfer of care) request.)
     */
    public ReferralRequest setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #priority} (An indication of the urgency of referral (or where applicable the type of transfer of care) request.)
     */
    public CodeableConcept getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new CodeableConcept(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (An indication of the urgency of referral (or where applicable the type of transfer of care) request.)
     */
    public ReferralRequest setPriority(CodeableConcept value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #patient} (The patient who is the subject of a referral or transfer of care request.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient who is the subject of a referral or transfer of care request.)
     */
    public ReferralRequest setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who is the subject of a referral or transfer of care request.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who is the subject of a referral or transfer of care request.)
     */
    public ReferralRequest setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (The encounter at which the request for referral or transfer of care is initiated.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The encounter at which the request for referral or transfer of care is initiated.)
     */
    public ReferralRequest setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter at which the request for referral or transfer of care is initiated.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter at which the request for referral or transfer of care is initiated.)
     */
    public ReferralRequest setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #fulfillmentTime} (The period of time within which the services identified in the referral/transfer of care is specified or required to occur.)
     */
    public Period getFulfillmentTime() { 
      if (this.fulfillmentTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.fulfillmentTime");
        else if (Configuration.doAutoCreate())
          this.fulfillmentTime = new Period(); // cc
      return this.fulfillmentTime;
    }

    public boolean hasFulfillmentTime() { 
      return this.fulfillmentTime != null && !this.fulfillmentTime.isEmpty();
    }

    /**
     * @param value {@link #fulfillmentTime} (The period of time within which the services identified in the referral/transfer of care is specified or required to occur.)
     */
    public ReferralRequest setFulfillmentTime(Period value) { 
      this.fulfillmentTime = value;
      return this;
    }

    /**
     * @return {@link #authored} (Date/DateTime of creation for draft requests and date of activation for active requests.). This is the underlying object with id, value and extensions. The accessor "getAuthored" gives direct access to the value
     */
    public DateTimeType getAuthoredElement() { 
      if (this.authored == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.authored");
        else if (Configuration.doAutoCreate())
          this.authored = new DateTimeType(); // bb
      return this.authored;
    }

    public boolean hasAuthoredElement() { 
      return this.authored != null && !this.authored.isEmpty();
    }

    public boolean hasAuthored() { 
      return this.authored != null && !this.authored.isEmpty();
    }

    /**
     * @param value {@link #authored} (Date/DateTime of creation for draft requests and date of activation for active requests.). This is the underlying object with id, value and extensions. The accessor "getAuthored" gives direct access to the value
     */
    public ReferralRequest setAuthoredElement(DateTimeType value) { 
      this.authored = value;
      return this;
    }

    /**
     * @return Date/DateTime of creation for draft requests and date of activation for active requests.
     */
    public Date getAuthored() { 
      return this.authored == null ? null : this.authored.getValue();
    }

    /**
     * @param value Date/DateTime of creation for draft requests and date of activation for active requests.
     */
    public ReferralRequest setAuthored(Date value) { 
      if (value == null)
        this.authored = null;
      else {
        if (this.authored == null)
          this.authored = new DateTimeType();
        this.authored.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #requester} (The healthcare provider or provider organization who/which initiated the referral/transfer of care request. Can also be  Patient (a self referral).)
     */
    public Reference getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new Reference(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (The healthcare provider or provider organization who/which initiated the referral/transfer of care request. Can also be  Patient (a self referral).)
     */
    public ReferralRequest setRequester(Reference value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #requester} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare provider or provider organization who/which initiated the referral/transfer of care request. Can also be  Patient (a self referral).)
     */
    public Resource getRequesterTarget() { 
      return this.requesterTarget;
    }

    /**
     * @param value {@link #requester} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare provider or provider organization who/which initiated the referral/transfer of care request. Can also be  Patient (a self referral).)
     */
    public ReferralRequest setRequesterTarget(Resource value) { 
      this.requesterTarget = value;
      return this;
    }

    /**
     * @return {@link #specialty} (Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.  For example: Cardiology Gastroenterology Diabetology.)
     */
    public CodeableConcept getSpecialty() { 
      if (this.specialty == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.specialty");
        else if (Configuration.doAutoCreate())
          this.specialty = new CodeableConcept(); // cc
      return this.specialty;
    }

    public boolean hasSpecialty() { 
      return this.specialty != null && !this.specialty.isEmpty();
    }

    /**
     * @param value {@link #specialty} (Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.  For example: Cardiology Gastroenterology Diabetology.)
     */
    public ReferralRequest setSpecialty(CodeableConcept value) { 
      this.specialty = value;
      return this;
    }

    /**
     * @return {@link #recipient} (The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.)
     */
    public List<Reference> getRecipient() { 
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      return this.recipient;
    }

    public boolean hasRecipient() { 
      if (this.recipient == null)
        return false;
      for (Reference item : this.recipient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #recipient} (The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.)
     */
    // syntactic sugar
    public Reference addRecipient() { //3
      Reference t = new Reference();
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return t;
    }

    // syntactic sugar
    public ReferralRequest addRecipient(Reference t) { //3
      if (t == null)
        return this;
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return this;
    }

    /**
     * @return {@link #recipient} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.)
     */
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #reason} (Description of clinical condition indicating why referral/transfer of care is requested.  For example:  Pathological Anomalies, Disabled (physical or mental),  Behavioral Management.)
     */
    public CodeableConcept getReason() { 
      if (this.reason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.reason");
        else if (Configuration.doAutoCreate())
          this.reason = new CodeableConcept(); // cc
      return this.reason;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (Description of clinical condition indicating why referral/transfer of care is requested.  For example:  Pathological Anomalies, Disabled (physical or mental),  Behavioral Management.)
     */
    public ReferralRequest setReason(CodeableConcept value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #description} (The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.description");
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
     * @param value {@link #description} (The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ReferralRequest setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.
     */
    public ReferralRequest setDescription(String value) { 
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
     * @return {@link #serviceRequested} (The service(s) that is/are requested to be provided to the patient.  For example: cardiac pacemaker insertion.)
     */
    public List<CodeableConcept> getServiceRequested() { 
      if (this.serviceRequested == null)
        this.serviceRequested = new ArrayList<CodeableConcept>();
      return this.serviceRequested;
    }

    public boolean hasServiceRequested() { 
      if (this.serviceRequested == null)
        return false;
      for (CodeableConcept item : this.serviceRequested)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #serviceRequested} (The service(s) that is/are requested to be provided to the patient.  For example: cardiac pacemaker insertion.)
     */
    // syntactic sugar
    public CodeableConcept addServiceRequested() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.serviceRequested == null)
        this.serviceRequested = new ArrayList<CodeableConcept>();
      this.serviceRequested.add(t);
      return t;
    }

    // syntactic sugar
    public ReferralRequest addServiceRequested(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.serviceRequested == null)
        this.serviceRequested = new ArrayList<CodeableConcept>();
      this.serviceRequested.add(t);
      return this;
    }

    /**
     * @return {@link #supportingInformation} (Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.)
     */
    public List<Reference> getSupportingInformation() { 
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      return this.supportingInformation;
    }

    public boolean hasSupportingInformation() { 
      if (this.supportingInformation == null)
        return false;
      for (Reference item : this.supportingInformation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #supportingInformation} (Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.)
     */
    // syntactic sugar
    public Reference addSupportingInformation() { //3
      Reference t = new Reference();
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return t;
    }

    // syntactic sugar
    public ReferralRequest addSupportingInformation(Reference t) { //3
      if (t == null)
        return this;
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return this;
    }

    /**
     * @return {@link #supportingInformation} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.)
     */
    public List<Resource> getSupportingInformationTarget() { 
      if (this.supportingInformationTarget == null)
        this.supportingInformationTarget = new ArrayList<Resource>();
      return this.supportingInformationTarget;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Business identifier that uniquely identifies the referral/care transfer request instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("basedOn", "Reference(ReferralRequest|CarePlan|DiagnosticOrder|ProcedureRequest)", "Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("parent", "Identifier", "The business identifier of the logical \"grouping\" request/order that this referral is a part of.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("status", "code", "The status of the authorization/intention reflected by the referral request record.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "code", "Distinguishes the \"level\" of authorization/demand implicit in this request.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("type", "CodeableConcept", "An indication of the type of referral (or where applicable the type of transfer of care) request.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("priority", "CodeableConcept", "An indication of the urgency of referral (or where applicable the type of transfer of care) request.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient who is the subject of a referral or transfer of care request.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter at which the request for referral or transfer of care is initiated.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("fulfillmentTime", "Period", "The period of time within which the services identified in the referral/transfer of care is specified or required to occur.", 0, java.lang.Integer.MAX_VALUE, fulfillmentTime));
        childrenList.add(new Property("authored", "dateTime", "Date/DateTime of creation for draft requests and date of activation for active requests.", 0, java.lang.Integer.MAX_VALUE, authored));
        childrenList.add(new Property("requester", "Reference(Practitioner|Organization|Patient)", "The healthcare provider or provider organization who/which initiated the referral/transfer of care request. Can also be  Patient (a self referral).", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("specialty", "CodeableConcept", "Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.  For example: Cardiology Gastroenterology Diabetology.", 0, java.lang.Integer.MAX_VALUE, specialty));
        childrenList.add(new Property("recipient", "Reference(Practitioner|Organization)", "The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("reason", "CodeableConcept", "Description of clinical condition indicating why referral/transfer of care is requested.  For example:  Pathological Anomalies, Disabled (physical or mental),  Behavioral Management.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("description", "string", "The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("serviceRequested", "CodeableConcept", "The service(s) that is/are requested to be provided to the patient.  For example: cardiac pacemaker insertion.", 0, java.lang.Integer.MAX_VALUE, serviceRequested));
        childrenList.add(new Property("supportingInformation", "Reference(Any)", "Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.", 0, java.lang.Integer.MAX_VALUE, supportingInformation));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -995424086: /*parent*/ return this.parent == null ? new Base[0] : new Base[] {this.parent}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ReferralStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Enumeration<ReferralCategory>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1098185163: /*fulfillmentTime*/ return this.fulfillmentTime == null ? new Base[0] : new Base[] {this.fulfillmentTime}; // Period
        case 1433073514: /*authored*/ return this.authored == null ? new Base[0] : new Base[] {this.authored}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // Reference
        case -1694759682: /*specialty*/ return this.specialty == null ? new Base[0] : new Base[] {this.specialty}; // CodeableConcept
        case 820081177: /*recipient*/ return this.recipient == null ? new Base[0] : this.recipient.toArray(new Base[this.recipient.size()]); // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 190229561: /*serviceRequested*/ return this.serviceRequested == null ? new Base[0] : this.serviceRequested.toArray(new Base[this.serviceRequested.size()]); // CodeableConcept
        case -1248768647: /*supportingInformation*/ return this.supportingInformation == null ? new Base[0] : this.supportingInformation.toArray(new Base[this.supportingInformation.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          break;
        case -995424086: // parent
          this.parent = castToIdentifier(value); // Identifier
          break;
        case -892481550: // status
          this.status = new ReferralStatusEnumFactory().fromType(value); // Enumeration<ReferralStatus>
          break;
        case 50511102: // category
          this.category = new ReferralCategoryEnumFactory().fromType(value); // Enumeration<ReferralCategory>
          break;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1165461084: // priority
          this.priority = castToCodeableConcept(value); // CodeableConcept
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          break;
        case 1098185163: // fulfillmentTime
          this.fulfillmentTime = castToPeriod(value); // Period
          break;
        case 1433073514: // authored
          this.authored = castToDateTime(value); // DateTimeType
          break;
        case 693933948: // requester
          this.requester = castToReference(value); // Reference
          break;
        case -1694759682: // specialty
          this.specialty = castToCodeableConcept(value); // CodeableConcept
          break;
        case 820081177: // recipient
          this.getRecipient().add(castToReference(value)); // Reference
          break;
        case -934964668: // reason
          this.reason = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 190229561: // serviceRequested
          this.getServiceRequested().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1248768647: // supportingInformation
          this.getSupportingInformation().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("basedOn"))
          this.getBasedOn().add(castToReference(value));
        else if (name.equals("parent"))
          this.parent = castToIdentifier(value); // Identifier
        else if (name.equals("status"))
          this.status = new ReferralStatusEnumFactory().fromType(value); // Enumeration<ReferralStatus>
        else if (name.equals("category"))
          this.category = new ReferralCategoryEnumFactory().fromType(value); // Enumeration<ReferralCategory>
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("priority"))
          this.priority = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("context"))
          this.context = castToReference(value); // Reference
        else if (name.equals("fulfillmentTime"))
          this.fulfillmentTime = castToPeriod(value); // Period
        else if (name.equals("authored"))
          this.authored = castToDateTime(value); // DateTimeType
        else if (name.equals("requester"))
          this.requester = castToReference(value); // Reference
        else if (name.equals("specialty"))
          this.specialty = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("recipient"))
          this.getRecipient().add(castToReference(value));
        else if (name.equals("reason"))
          this.reason = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("serviceRequested"))
          this.getServiceRequested().add(castToCodeableConcept(value));
        else if (name.equals("supportingInformation"))
          this.getSupportingInformation().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -332612366:  return addBasedOn(); // Reference
        case -995424086:  return getParent(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ReferralStatus>
        case 50511102: throw new FHIRException("Cannot make property category as it is not a complex type"); // Enumeration<ReferralCategory>
        case 3575610:  return getType(); // CodeableConcept
        case -1165461084:  return getPriority(); // CodeableConcept
        case -791418107:  return getPatient(); // Reference
        case 951530927:  return getContext(); // Reference
        case 1098185163:  return getFulfillmentTime(); // Period
        case 1433073514: throw new FHIRException("Cannot make property authored as it is not a complex type"); // DateTimeType
        case 693933948:  return getRequester(); // Reference
        case -1694759682:  return getSpecialty(); // CodeableConcept
        case 820081177:  return addRecipient(); // Reference
        case -934964668:  return getReason(); // CodeableConcept
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 190229561:  return addServiceRequested(); // CodeableConcept
        case -1248768647:  return addSupportingInformation(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("parent")) {
          this.parent = new Identifier();
          return this.parent;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ReferralRequest.status");
        }
        else if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type ReferralRequest.category");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("fulfillmentTime")) {
          this.fulfillmentTime = new Period();
          return this.fulfillmentTime;
        }
        else if (name.equals("authored")) {
          throw new FHIRException("Cannot call addChild on a primitive type ReferralRequest.authored");
        }
        else if (name.equals("requester")) {
          this.requester = new Reference();
          return this.requester;
        }
        else if (name.equals("specialty")) {
          this.specialty = new CodeableConcept();
          return this.specialty;
        }
        else if (name.equals("recipient")) {
          return addRecipient();
        }
        else if (name.equals("reason")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ReferralRequest.description");
        }
        else if (name.equals("serviceRequested")) {
          return addServiceRequested();
        }
        else if (name.equals("supportingInformation")) {
          return addSupportingInformation();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ReferralRequest";

  }

      public ReferralRequest copy() {
        ReferralRequest dst = new ReferralRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        dst.parent = parent == null ? null : parent.copy();
        dst.status = status == null ? null : status.copy();
        dst.category = category == null ? null : category.copy();
        dst.type = type == null ? null : type.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.context = context == null ? null : context.copy();
        dst.fulfillmentTime = fulfillmentTime == null ? null : fulfillmentTime.copy();
        dst.authored = authored == null ? null : authored.copy();
        dst.requester = requester == null ? null : requester.copy();
        dst.specialty = specialty == null ? null : specialty.copy();
        if (recipient != null) {
          dst.recipient = new ArrayList<Reference>();
          for (Reference i : recipient)
            dst.recipient.add(i.copy());
        };
        dst.reason = reason == null ? null : reason.copy();
        dst.description = description == null ? null : description.copy();
        if (serviceRequested != null) {
          dst.serviceRequested = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : serviceRequested)
            dst.serviceRequested.add(i.copy());
        };
        if (supportingInformation != null) {
          dst.supportingInformation = new ArrayList<Reference>();
          for (Reference i : supportingInformation)
            dst.supportingInformation.add(i.copy());
        };
        return dst;
      }

      protected ReferralRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ReferralRequest))
          return false;
        ReferralRequest o = (ReferralRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(basedOn, o.basedOn, true) && compareDeep(parent, o.parent, true)
           && compareDeep(status, o.status, true) && compareDeep(category, o.category, true) && compareDeep(type, o.type, true)
           && compareDeep(priority, o.priority, true) && compareDeep(patient, o.patient, true) && compareDeep(context, o.context, true)
           && compareDeep(fulfillmentTime, o.fulfillmentTime, true) && compareDeep(authored, o.authored, true)
           && compareDeep(requester, o.requester, true) && compareDeep(specialty, o.specialty, true) && compareDeep(recipient, o.recipient, true)
           && compareDeep(reason, o.reason, true) && compareDeep(description, o.description, true) && compareDeep(serviceRequested, o.serviceRequested, true)
           && compareDeep(supportingInformation, o.supportingInformation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ReferralRequest))
          return false;
        ReferralRequest o = (ReferralRequest) other;
        return compareValues(status, o.status, true) && compareValues(category, o.category, true) && compareValues(authored, o.authored, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (basedOn == null || basedOn.isEmpty())
           && (parent == null || parent.isEmpty()) && (status == null || status.isEmpty()) && (category == null || category.isEmpty())
           && (type == null || type.isEmpty()) && (priority == null || priority.isEmpty()) && (patient == null || patient.isEmpty())
           && (context == null || context.isEmpty()) && (fulfillmentTime == null || fulfillmentTime.isEmpty())
           && (authored == null || authored.isEmpty()) && (requester == null || requester.isEmpty())
           && (specialty == null || specialty.isEmpty()) && (recipient == null || recipient.isEmpty())
           && (reason == null || reason.isEmpty()) && (description == null || description.isEmpty())
           && (serviceRequested == null || serviceRequested.isEmpty()) && (supportingInformation == null || supportingInformation.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ReferralRequest;
   }

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Proposal, plan or request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="ReferralRequest.category", description="Proposal, plan or request", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Proposal, plan or request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>Requester of referral / transfer of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.requester</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="ReferralRequest.requester", description="Requester of referral / transfer of care", type="reference" )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>Requester of referral / transfer of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.requester</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("ReferralRequest:requester").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who the referral is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ReferralRequest.patient", description="Who the referral is about", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who the referral is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ReferralRequest:patient").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the referral</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ReferralRequest.status", description="The status of the referral", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the referral</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>priority</b>
   * <p>
   * Description: <b>The priority assigned to the referral</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.priority</b><br>
   * </p>
   */
  @SearchParamDefinition(name="priority", path="ReferralRequest.priority", description="The priority assigned to the referral", type="token" )
  public static final String SP_PRIORITY = "priority";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>priority</b>
   * <p>
   * Description: <b>The priority assigned to the referral</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.priority</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PRIORITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PRIORITY);

 /**
   * Search parameter: <b>basedon</b>
   * <p>
   * Description: <b>Request being fulfilled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="basedon", path="ReferralRequest.basedOn", description="Request being fulfilled", type="reference" )
  public static final String SP_BASEDON = "basedon";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>basedon</b>
   * <p>
   * Description: <b>Request being fulfilled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASEDON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASEDON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:basedon</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASEDON = new ca.uhn.fhir.model.api.Include("ReferralRequest:basedon").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Part of encounter or episode of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="ReferralRequest.context", description="Part of encounter or episode of care", type="reference" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Part of encounter or episode of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("ReferralRequest:context").toLocked();

 /**
   * Search parameter: <b>parent</b>
   * <p>
   * Description: <b>Part of common request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.parent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="parent", path="ReferralRequest.parent", description="Part of common request", type="token" )
  public static final String SP_PARENT = "parent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>parent</b>
   * <p>
   * Description: <b>Part of common request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.parent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PARENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PARENT);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The type of the referral</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="ReferralRequest.type", description="The type of the referral", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The type of the referral</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Creation or activation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ReferralRequest.authored</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ReferralRequest.authored", description="Creation or activation date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Creation or activation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ReferralRequest.authored</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>specialty</b>
   * <p>
   * Description: <b>The specialty that the referral is for</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.specialty</b><br>
   * </p>
   */
  @SearchParamDefinition(name="specialty", path="ReferralRequest.specialty", description="The specialty that the referral is for", type="token" )
  public static final String SP_SPECIALTY = "specialty";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>specialty</b>
   * <p>
   * Description: <b>The specialty that the referral is for</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.specialty</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SPECIALTY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SPECIALTY);

 /**
   * Search parameter: <b>recipient</b>
   * <p>
   * Description: <b>The person that the referral was sent to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.recipient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recipient", path="ReferralRequest.recipient", description="The person that the referral was sent to", type="reference" )
  public static final String SP_RECIPIENT = "recipient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>recipient</b>
   * <p>
   * Description: <b>The person that the referral was sent to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.recipient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECIPIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECIPIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:recipient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECIPIENT = new ca.uhn.fhir.model.api.Include("ReferralRequest:recipient").toLocked();


}

