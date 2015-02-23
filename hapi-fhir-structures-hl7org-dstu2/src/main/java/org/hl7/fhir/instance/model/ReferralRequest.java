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
 * Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organisation.
 */
@ResourceDef(name="ReferralRequest", profile="http://hl7.org/fhir/Profile/ReferralRequest")
public class ReferralRequest extends DomainResource {

    public enum Referralstatus {
        /**
         * A draft referral that has yet to be send.
         */
        DRAFT, 
        /**
         * The referral has been transmitted, but not yet acknowledged by the recipient.
         */
        SENT, 
        /**
         * The referral has been acknowledged by the recipient, and is in the process of being actioned.
         */
        ACTIVE, 
        /**
         * The referral has been cancelled without being completed. For example it is no longer needed.
         */
        CANCELLED, 
        /**
         * The recipient has declined to accept the referral.
         */
        REFUSED, 
        /**
         * The referral has been completely actioned.
         */
        COMPLETED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Referralstatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("sent".equals(codeString))
          return SENT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("refused".equals(codeString))
          return REFUSED;
        if ("completed".equals(codeString))
          return COMPLETED;
        throw new Exception("Unknown Referralstatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case SENT: return "sent";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case REFUSED: return "refused";
            case COMPLETED: return "completed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "";
            case SENT: return "";
            case ACTIVE: return "";
            case CANCELLED: return "";
            case REFUSED: return "";
            case COMPLETED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "A draft referral that has yet to be send.";
            case SENT: return "The referral has been transmitted, but not yet acknowledged by the recipient.";
            case ACTIVE: return "The referral has been acknowledged by the recipient, and is in the process of being actioned.";
            case CANCELLED: return "The referral has been cancelled without being completed. For example it is no longer needed.";
            case REFUSED: return "The recipient has declined to accept the referral.";
            case COMPLETED: return "The referral has been completely actioned.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "draft";
            case SENT: return "sent";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case REFUSED: return "refused";
            case COMPLETED: return "completed";
            default: return "?";
          }
        }
    }

  public static class ReferralstatusEnumFactory implements EnumFactory<Referralstatus> {
    public Referralstatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return Referralstatus.DRAFT;
        if ("sent".equals(codeString))
          return Referralstatus.SENT;
        if ("active".equals(codeString))
          return Referralstatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return Referralstatus.CANCELLED;
        if ("refused".equals(codeString))
          return Referralstatus.REFUSED;
        if ("completed".equals(codeString))
          return Referralstatus.COMPLETED;
        throw new IllegalArgumentException("Unknown Referralstatus code '"+codeString+"'");
        }
    public String toCode(Referralstatus code) {
      if (code == Referralstatus.DRAFT)
        return "draft";
      if (code == Referralstatus.SENT)
        return "sent";
      if (code == Referralstatus.ACTIVE)
        return "active";
      if (code == Referralstatus.CANCELLED)
        return "cancelled";
      if (code == Referralstatus.REFUSED)
        return "refused";
      if (code == Referralstatus.COMPLETED)
        return "completed";
      return "?";
      }
    }

    /**
     * The workflow status of the referral or transfer of care request.
     */
    @Child(name = "status", type = {CodeType.class}, order = 0, min = 1, max = 1)
    @Description(shortDefinition="draft | sent | active | cancelled | refused | completed", formalDefinition="The workflow status of the referral or transfer of care request." )
    protected Enumeration<Referralstatus> status;

    /**
     * Business Id that uniquely identifies the referral/care transfer request instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 1, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Identifier of request", formalDefinition="Business Id that uniquely identifies the referral/care transfer request instance." )
    protected List<Identifier> identifier;

    /**
     * An indication of the type of referral (or where applicable the type of transfer of care) request.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Referral/Transition of care request type", formalDefinition="An indication of the type of referral (or where applicable the type of transfer of care) request." )
    protected CodeableConcept type;

    /**
     * Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.
     */
    @Child(name = "specialty", type = {CodeableConcept.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="The clinical specialty (discipline) that the referral is requested for", formalDefinition="Indication of the clinical domain or discipline to which the referral or transfer of care request is sent." )
    protected CodeableConcept specialty;

    /**
     * An indication of the urgency of referral (or where applicable the type of transfer of care) request.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Urgency of referral / transfer of care request", formalDefinition="An indication of the urgency of referral (or where applicable the type of transfer of care) request." )
    protected CodeableConcept priority;

    /**
     * The patient who is the subject of a referral or transfer of care request.
     */
    @Child(name = "patient", type = {Patient.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Patient referred to care or transfer", formalDefinition="The patient who is the subject of a referral or transfer of care request." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient who is the subject of a referral or transfer of care request.)
     */
    protected Patient patientTarget;

    /**
     * The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral).
     */
    @Child(name = "requester", type = {Practitioner.class, Organization.class, Patient.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="Requester of referral / transfer of care", formalDefinition="The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral)." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral).)
     */
    protected Resource requesterTarget;

    /**
     * The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.
     */
    @Child(name = "recipient", type = {Practitioner.class, Organization.class}, order = 7, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Receiver of referral / transfer of care request", formalDefinition="The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.)
     */
    protected List<Resource> recipientTarget;


    /**
     * The encounter at which the request for referral or transfer of care is initiated.
     */
    @Child(name = "encounter", type = {Encounter.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Encounter", formalDefinition="The encounter at which the request for referral or transfer of care is initiated." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter at which the request for referral or transfer of care is initiated.)
     */
    protected Encounter encounterTarget;

    /**
     * Date/DateTime the request for referral or transfer of care is sent by the author.
     */
    @Child(name = "dateSent", type = {DateTimeType.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Date referral/transfer of care request is sent", formalDefinition="Date/DateTime the request for referral or transfer of care is sent by the author." )
    protected DateTimeType dateSent;

    /**
     * Description of clinical condition indicating why referral/transfer of care is requested.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Reason for referral / Transfer of care request", formalDefinition="Description of clinical condition indicating why referral/transfer of care is requested." )
    protected CodeableConcept reason;

    /**
     * The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.
     */
    @Child(name = "description", type = {StringType.class}, order = 11, min = 0, max = 1)
    @Description(shortDefinition="A textual description of the referral", formalDefinition="The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary." )
    protected StringType description;

    /**
     * The service(s) that is/are requested to be provided to the patient.
     */
    @Child(name = "serviceRequested", type = {CodeableConcept.class}, order = 12, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Service(s) requested", formalDefinition="The service(s) that is/are requested to be provided to the patient." )
    protected List<CodeableConcept> serviceRequested;

    /**
     * Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.
     */
    @Child(name = "supportingInformation", type = {}, order = 13, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Additonal information to support referral or transfer of care request", formalDefinition="Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care." )
    protected List<Reference> supportingInformation;
    /**
     * The actual objects that are the target of the reference (Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.)
     */
    protected List<Resource> supportingInformationTarget;


    /**
     * The period of time within which the services identified in the referral/transfer of care is specified or required to occur.
     */
    @Child(name = "fulfillmentTime", type = {Period.class}, order = 14, min = 0, max = 1)
    @Description(shortDefinition="Requested service(s) fulfillment time", formalDefinition="The period of time within which the services identified in the referral/transfer of care is specified or required to occur." )
    protected Period fulfillmentTime;

    private static final long serialVersionUID = -1139252216L;

    public ReferralRequest() {
      super();
    }

    public ReferralRequest(Enumeration<Referralstatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #status} (The workflow status of the referral or transfer of care request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<Referralstatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<Referralstatus>(new ReferralstatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The workflow status of the referral or transfer of care request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ReferralRequest setStatusElement(Enumeration<Referralstatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The workflow status of the referral or transfer of care request.
     */
    public Referralstatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The workflow status of the referral or transfer of care request.
     */
    public ReferralRequest setStatus(Referralstatus value) { 
        if (this.status == null)
          this.status = new Enumeration<Referralstatus>(new ReferralstatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #identifier} (Business Id that uniquely identifies the referral/care transfer request instance.)
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
     * @return {@link #identifier} (Business Id that uniquely identifies the referral/care transfer request instance.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
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
     * @return {@link #specialty} (Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.)
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
     * @param value {@link #specialty} (Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.)
     */
    public ReferralRequest setSpecialty(CodeableConcept value) { 
      this.specialty = value;
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
     * @return {@link #requester} (The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral).)
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
     * @param value {@link #requester} (The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral).)
     */
    public ReferralRequest setRequester(Reference value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #requester} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral).)
     */
    public Resource getRequesterTarget() { 
      return this.requesterTarget;
    }

    /**
     * @param value {@link #requester} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral).)
     */
    public ReferralRequest setRequesterTarget(Resource value) { 
      this.requesterTarget = value;
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

    /**
     * @return {@link #recipient} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.)
     */
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #encounter} (The encounter at which the request for referral or transfer of care is initiated.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The encounter at which the request for referral or transfer of care is initiated.)
     */
    public ReferralRequest setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter at which the request for referral or transfer of care is initiated.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter at which the request for referral or transfer of care is initiated.)
     */
    public ReferralRequest setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #dateSent} (Date/DateTime the request for referral or transfer of care is sent by the author.). This is the underlying object with id, value and extensions. The accessor "getDateSent" gives direct access to the value
     */
    public DateTimeType getDateSentElement() { 
      if (this.dateSent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.dateSent");
        else if (Configuration.doAutoCreate())
          this.dateSent = new DateTimeType(); // bb
      return this.dateSent;
    }

    public boolean hasDateSentElement() { 
      return this.dateSent != null && !this.dateSent.isEmpty();
    }

    public boolean hasDateSent() { 
      return this.dateSent != null && !this.dateSent.isEmpty();
    }

    /**
     * @param value {@link #dateSent} (Date/DateTime the request for referral or transfer of care is sent by the author.). This is the underlying object with id, value and extensions. The accessor "getDateSent" gives direct access to the value
     */
    public ReferralRequest setDateSentElement(DateTimeType value) { 
      this.dateSent = value;
      return this;
    }

    /**
     * @return Date/DateTime the request for referral or transfer of care is sent by the author.
     */
    public Date getDateSent() { 
      return this.dateSent == null ? null : this.dateSent.getValue();
    }

    /**
     * @param value Date/DateTime the request for referral or transfer of care is sent by the author.
     */
    public ReferralRequest setDateSent(Date value) { 
      if (value == null)
        this.dateSent = null;
      else {
        if (this.dateSent == null)
          this.dateSent = new DateTimeType();
        this.dateSent.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #reason} (Description of clinical condition indicating why referral/transfer of care is requested.)
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
     * @param value {@link #reason} (Description of clinical condition indicating why referral/transfer of care is requested.)
     */
    public ReferralRequest setReason(CodeableConcept value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #description} (The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
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
     * @param value {@link #description} (The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ReferralRequest setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.
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
     * @return {@link #serviceRequested} (The service(s) that is/are requested to be provided to the patient.)
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
     * @return {@link #serviceRequested} (The service(s) that is/are requested to be provided to the patient.)
     */
    // syntactic sugar
    public CodeableConcept addServiceRequested() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.serviceRequested == null)
        this.serviceRequested = new ArrayList<CodeableConcept>();
      this.serviceRequested.add(t);
      return t;
    }

    /**
     * @return {@link #supportingInformation} (Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.)
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
     * @return {@link #supportingInformation} (Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.)
     */
    // syntactic sugar
    public Reference addSupportingInformation() { //3
      Reference t = new Reference();
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return t;
    }

    /**
     * @return {@link #supportingInformation} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.)
     */
    public List<Resource> getSupportingInformationTarget() { 
      if (this.supportingInformationTarget == null)
        this.supportingInformationTarget = new ArrayList<Resource>();
      return this.supportingInformationTarget;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("status", "code", "The workflow status of the referral or transfer of care request.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("identifier", "Identifier", "Business Id that uniquely identifies the referral/care transfer request instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "CodeableConcept", "An indication of the type of referral (or where applicable the type of transfer of care) request.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("specialty", "CodeableConcept", "Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.", 0, java.lang.Integer.MAX_VALUE, specialty));
        childrenList.add(new Property("priority", "CodeableConcept", "An indication of the urgency of referral (or where applicable the type of transfer of care) request.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient who is the subject of a referral or transfer of care request.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("requester", "Reference(Practitioner|Organization|Patient)", "The healthcare provider or provider organization who/which initaited the referral/transfer of care request. Can also be  Patient (a self referral).", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("recipient", "Reference(Practitioner|Organization)", "The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter at which the request for referral or transfer of care is initiated.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("dateSent", "dateTime", "Date/DateTime the request for referral or transfer of care is sent by the author.", 0, java.lang.Integer.MAX_VALUE, dateSent));
        childrenList.add(new Property("reason", "CodeableConcept", "Description of clinical condition indicating why referral/transfer of care is requested.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("description", "string", "The reason gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("serviceRequested", "CodeableConcept", "The service(s) that is/are requested to be provided to the patient.", 0, java.lang.Integer.MAX_VALUE, serviceRequested));
        childrenList.add(new Property("supportingInformation", "Reference(Any)", "Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.", 0, java.lang.Integer.MAX_VALUE, supportingInformation));
        childrenList.add(new Property("fulfillmentTime", "Period", "The period of time within which the services identified in the referral/transfer of care is specified or required to occur.", 0, java.lang.Integer.MAX_VALUE, fulfillmentTime));
      }

      public ReferralRequest copy() {
        ReferralRequest dst = new ReferralRequest();
        copyValues(dst);
        dst.status = status == null ? null : status.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.specialty = specialty == null ? null : specialty.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.requester = requester == null ? null : requester.copy();
        if (recipient != null) {
          dst.recipient = new ArrayList<Reference>();
          for (Reference i : recipient)
            dst.recipient.add(i.copy());
        };
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.dateSent = dateSent == null ? null : dateSent.copy();
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
        dst.fulfillmentTime = fulfillmentTime == null ? null : fulfillmentTime.copy();
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
        return compareDeep(status, o.status, true) && compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true)
           && compareDeep(specialty, o.specialty, true) && compareDeep(priority, o.priority, true) && compareDeep(patient, o.patient, true)
           && compareDeep(requester, o.requester, true) && compareDeep(recipient, o.recipient, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(dateSent, o.dateSent, true) && compareDeep(reason, o.reason, true) && compareDeep(description, o.description, true)
           && compareDeep(serviceRequested, o.serviceRequested, true) && compareDeep(supportingInformation, o.supportingInformation, true)
           && compareDeep(fulfillmentTime, o.fulfillmentTime, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ReferralRequest))
          return false;
        ReferralRequest o = (ReferralRequest) other;
        return compareValues(status, o.status, true) && compareValues(dateSent, o.dateSent, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (status == null || status.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (type == null || type.isEmpty()) && (specialty == null || specialty.isEmpty()) && (priority == null || priority.isEmpty())
           && (patient == null || patient.isEmpty()) && (requester == null || requester.isEmpty()) && (recipient == null || recipient.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (dateSent == null || dateSent.isEmpty())
           && (reason == null || reason.isEmpty()) && (description == null || description.isEmpty())
           && (serviceRequested == null || serviceRequested.isEmpty()) && (supportingInformation == null || supportingInformation.isEmpty())
           && (fulfillmentTime == null || fulfillmentTime.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ReferralRequest;
   }

  @SearchParamDefinition(name="specialty", path="ReferralRequest.specialty", description="The specialty that the referral is for", type="token" )
  public static final String SP_SPECIALTY = "specialty";
  @SearchParamDefinition(name = "patient", path = "ReferralRequest.patient", description = "Who the referral is about", type = "reference")
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="recipient", path="ReferralRequest.recipient", description="The person that the referral was sent to", type="reference" )
  public static final String SP_RECIPIENT = "recipient";
  @SearchParamDefinition(name = "type", path = "ReferralRequest.type", description = "The type of the referral", type = "token")
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name = "priority", path = "ReferralRequest.priority", description = "The priority assigned to the referral", type = "token")
  public static final String SP_PRIORITY = "priority";
  @SearchParamDefinition(name = "status", path = "ReferralRequest.status", description = "The status of the referral", type = "token")
  public static final String SP_STATUS = "status";

}

