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
 * Used to record and send details about a request for referral service or transfer of a patient to the care of another provider or provider organization.
 */
@ResourceDef(name="ReferralRequest", profile="http://hl7.org/fhir/Profile/ReferralRequest")
public class ReferralRequest extends DomainResource {

    public enum ReferralRequestStatus {
        /**
         * The request has been created but is not yet complete or ready for action
         */
        DRAFT, 
        /**
         * The request is ready to be acted upon
         */
        ACTIVE, 
        /**
         * The authorization/request to act has been temporarily withdrawn but is expected to resume in the future
         */
        SUSPENDED, 
        /**
         * The authorization/request to act has been terminated prior to the full completion of the intended actions.  No further activity should occur.
         */
        CANCELLED, 
        /**
         * Activity against the request has been sufficiently completed to the satisfaction of the requester
         */
        COMPLETED, 
        /**
         * This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".)
         */
        ENTEREDINERROR, 
        /**
         * The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for "other" . One of the listed statuses is presumed to apply,  but the system creating the request doesn't know.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ReferralRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ReferralRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case CANCELLED: return "cancelled";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/request-status";
            case ACTIVE: return "http://hl7.org/fhir/request-status";
            case SUSPENDED: return "http://hl7.org/fhir/request-status";
            case CANCELLED: return "http://hl7.org/fhir/request-status";
            case COMPLETED: return "http://hl7.org/fhir/request-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/request-status";
            case UNKNOWN: return "http://hl7.org/fhir/request-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The request has been created but is not yet complete or ready for action";
            case ACTIVE: return "The request is ready to be acted upon";
            case SUSPENDED: return "The authorization/request to act has been temporarily withdrawn but is expected to resume in the future";
            case CANCELLED: return "The authorization/request to act has been terminated prior to the full completion of the intended actions.  No further activity should occur.";
            case COMPLETED: return "Activity against the request has been sufficiently completed to the satisfaction of the requester";
            case ENTEREDINERROR: return "This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".)";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for \"other\" . One of the listed statuses is presumed to apply,  but the system creating the request doesn't know.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case CANCELLED: return "Cancelled";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class ReferralRequestStatusEnumFactory implements EnumFactory<ReferralRequestStatus> {
    public ReferralRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ReferralRequestStatus.DRAFT;
        if ("active".equals(codeString))
          return ReferralRequestStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return ReferralRequestStatus.SUSPENDED;
        if ("cancelled".equals(codeString))
          return ReferralRequestStatus.CANCELLED;
        if ("completed".equals(codeString))
          return ReferralRequestStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ReferralRequestStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return ReferralRequestStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown ReferralRequestStatus code '"+codeString+"'");
        }
        public Enumeration<ReferralRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ReferralRequestStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ReferralRequestStatus>(this, ReferralRequestStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<ReferralRequestStatus>(this, ReferralRequestStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<ReferralRequestStatus>(this, ReferralRequestStatus.SUSPENDED);
        if ("cancelled".equals(codeString))
          return new Enumeration<ReferralRequestStatus>(this, ReferralRequestStatus.CANCELLED);
        if ("completed".equals(codeString))
          return new Enumeration<ReferralRequestStatus>(this, ReferralRequestStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ReferralRequestStatus>(this, ReferralRequestStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<ReferralRequestStatus>(this, ReferralRequestStatus.UNKNOWN);
        throw new FHIRException("Unknown ReferralRequestStatus code '"+codeString+"'");
        }
    public String toCode(ReferralRequestStatus code) {
      if (code == ReferralRequestStatus.DRAFT)
        return "draft";
      if (code == ReferralRequestStatus.ACTIVE)
        return "active";
      if (code == ReferralRequestStatus.SUSPENDED)
        return "suspended";
      if (code == ReferralRequestStatus.CANCELLED)
        return "cancelled";
      if (code == ReferralRequestStatus.COMPLETED)
        return "completed";
      if (code == ReferralRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ReferralRequestStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(ReferralRequestStatus code) {
      return code.getSystem();
      }
    }

    public enum ReferralCategory {
        /**
         * The request is a suggestion made by someone/something that doesn't have an intention to ensure it occurs and without providing an authorization to act
         */
        PROPOSAL, 
        /**
         * The request represents an intension to ensure something occurs without providing an authorization for others to act
         */
        PLAN, 
        /**
         * The request represents a request/demand and authorization for action
         */
        ORDER, 
        /**
         * The request represents an original authorization for action
         */
        ORIGINALORDER, 
        /**
         * The request represents an automatically generated supplemental authorization for action based on a parent authorization together with initial results of the action taken against that parent authorization
         */
        REFLEXORDER, 
        /**
         * The request represents the view of an authorization instantiated by a fulfilling system representing the details of the fulfiller's intention to act upon a submitted order
         */
        FILLERORDER, 
        /**
         * An order created in fulfillment of a broader order that represents the authorization for a single activity occurrence.  E.g. The administration of a single dose of a drug.
         */
        INSTANCEORDER, 
        /**
         * The request represents a component or option for a RequestGroup that establishes timing, conditionality and/or other constraints among a set of requests.

Refer to [[[RequestGroup]]] for additional information on how this status is used
         */
        OPTION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ReferralCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposal".equals(codeString))
          return PROPOSAL;
        if ("plan".equals(codeString))
          return PLAN;
        if ("order".equals(codeString))
          return ORDER;
        if ("original-order".equals(codeString))
          return ORIGINALORDER;
        if ("reflex-order".equals(codeString))
          return REFLEXORDER;
        if ("filler-order".equals(codeString))
          return FILLERORDER;
        if ("instance-order".equals(codeString))
          return INSTANCEORDER;
        if ("option".equals(codeString))
          return OPTION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ReferralCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSAL: return "proposal";
            case PLAN: return "plan";
            case ORDER: return "order";
            case ORIGINALORDER: return "original-order";
            case REFLEXORDER: return "reflex-order";
            case FILLERORDER: return "filler-order";
            case INSTANCEORDER: return "instance-order";
            case OPTION: return "option";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSAL: return "http://hl7.org/fhir/request-intent";
            case PLAN: return "http://hl7.org/fhir/request-intent";
            case ORDER: return "http://hl7.org/fhir/request-intent";
            case ORIGINALORDER: return "http://hl7.org/fhir/request-intent";
            case REFLEXORDER: return "http://hl7.org/fhir/request-intent";
            case FILLERORDER: return "http://hl7.org/fhir/request-intent";
            case INSTANCEORDER: return "http://hl7.org/fhir/request-intent";
            case OPTION: return "http://hl7.org/fhir/request-intent";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSAL: return "The request is a suggestion made by someone/something that doesn't have an intention to ensure it occurs and without providing an authorization to act";
            case PLAN: return "The request represents an intension to ensure something occurs without providing an authorization for others to act";
            case ORDER: return "The request represents a request/demand and authorization for action";
            case ORIGINALORDER: return "The request represents an original authorization for action";
            case REFLEXORDER: return "The request represents an automatically generated supplemental authorization for action based on a parent authorization together with initial results of the action taken against that parent authorization";
            case FILLERORDER: return "The request represents the view of an authorization instantiated by a fulfilling system representing the details of the fulfiller's intention to act upon a submitted order";
            case INSTANCEORDER: return "An order created in fulfillment of a broader order that represents the authorization for a single activity occurrence.  E.g. The administration of a single dose of a drug.";
            case OPTION: return "The request represents a component or option for a RequestGroup that establishes timing, conditionality and/or other constraints among a set of requests.\n\nRefer to [[[RequestGroup]]] for additional information on how this status is used";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSAL: return "Proposal";
            case PLAN: return "Plan";
            case ORDER: return "Order";
            case ORIGINALORDER: return "Original Order";
            case REFLEXORDER: return "Reflex Order";
            case FILLERORDER: return "Filler Order";
            case INSTANCEORDER: return "Instance Order";
            case OPTION: return "Option";
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
        if ("order".equals(codeString))
          return ReferralCategory.ORDER;
        if ("original-order".equals(codeString))
          return ReferralCategory.ORIGINALORDER;
        if ("reflex-order".equals(codeString))
          return ReferralCategory.REFLEXORDER;
        if ("filler-order".equals(codeString))
          return ReferralCategory.FILLERORDER;
        if ("instance-order".equals(codeString))
          return ReferralCategory.INSTANCEORDER;
        if ("option".equals(codeString))
          return ReferralCategory.OPTION;
        throw new IllegalArgumentException("Unknown ReferralCategory code '"+codeString+"'");
        }
        public Enumeration<ReferralCategory> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ReferralCategory>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposal".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.PROPOSAL);
        if ("plan".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.PLAN);
        if ("order".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.ORDER);
        if ("original-order".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.ORIGINALORDER);
        if ("reflex-order".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.REFLEXORDER);
        if ("filler-order".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.FILLERORDER);
        if ("instance-order".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.INSTANCEORDER);
        if ("option".equals(codeString))
          return new Enumeration<ReferralCategory>(this, ReferralCategory.OPTION);
        throw new FHIRException("Unknown ReferralCategory code '"+codeString+"'");
        }
    public String toCode(ReferralCategory code) {
      if (code == ReferralCategory.PROPOSAL)
        return "proposal";
      if (code == ReferralCategory.PLAN)
        return "plan";
      if (code == ReferralCategory.ORDER)
        return "order";
      if (code == ReferralCategory.ORIGINALORDER)
        return "original-order";
      if (code == ReferralCategory.REFLEXORDER)
        return "reflex-order";
      if (code == ReferralCategory.FILLERORDER)
        return "filler-order";
      if (code == ReferralCategory.INSTANCEORDER)
        return "instance-order";
      if (code == ReferralCategory.OPTION)
        return "option";
      return "?";
      }
    public String toSystem(ReferralCategory code) {
      return code.getSystem();
      }
    }

    public enum ReferralPriority {
        /**
         * The request has normal priority
         */
        ROUTINE, 
        /**
         * The request should be actioned promptly - higher priority than routine
         */
        URGENT, 
        /**
         * The request should be actioned as soon as possible - higher priority than urgent
         */
        ASAP, 
        /**
         * The request should be actioned immediately - highest possible priority.  E.g. an emergency
         */
        STAT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ReferralPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return ROUTINE;
        if ("urgent".equals(codeString))
          return URGENT;
        if ("asap".equals(codeString))
          return ASAP;
        if ("stat".equals(codeString))
          return STAT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ReferralPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ROUTINE: return "routine";
            case URGENT: return "urgent";
            case ASAP: return "asap";
            case STAT: return "stat";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ROUTINE: return "http://hl7.org/fhir/request-priority";
            case URGENT: return "http://hl7.org/fhir/request-priority";
            case ASAP: return "http://hl7.org/fhir/request-priority";
            case STAT: return "http://hl7.org/fhir/request-priority";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ROUTINE: return "The request has normal priority";
            case URGENT: return "The request should be actioned promptly - higher priority than routine";
            case ASAP: return "The request should be actioned as soon as possible - higher priority than urgent";
            case STAT: return "The request should be actioned immediately - highest possible priority.  E.g. an emergency";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ROUTINE: return "Routine";
            case URGENT: return "Urgent";
            case ASAP: return "ASAP";
            case STAT: return "STAT";
            default: return "?";
          }
        }
    }

  public static class ReferralPriorityEnumFactory implements EnumFactory<ReferralPriority> {
    public ReferralPriority fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return ReferralPriority.ROUTINE;
        if ("urgent".equals(codeString))
          return ReferralPriority.URGENT;
        if ("asap".equals(codeString))
          return ReferralPriority.ASAP;
        if ("stat".equals(codeString))
          return ReferralPriority.STAT;
        throw new IllegalArgumentException("Unknown ReferralPriority code '"+codeString+"'");
        }
        public Enumeration<ReferralPriority> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ReferralPriority>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("routine".equals(codeString))
          return new Enumeration<ReferralPriority>(this, ReferralPriority.ROUTINE);
        if ("urgent".equals(codeString))
          return new Enumeration<ReferralPriority>(this, ReferralPriority.URGENT);
        if ("asap".equals(codeString))
          return new Enumeration<ReferralPriority>(this, ReferralPriority.ASAP);
        if ("stat".equals(codeString))
          return new Enumeration<ReferralPriority>(this, ReferralPriority.STAT);
        throw new FHIRException("Unknown ReferralPriority code '"+codeString+"'");
        }
    public String toCode(ReferralPriority code) {
      if (code == ReferralPriority.ROUTINE)
        return "routine";
      if (code == ReferralPriority.URGENT)
        return "urgent";
      if (code == ReferralPriority.ASAP)
        return "asap";
      if (code == ReferralPriority.STAT)
        return "stat";
      return "?";
      }
    public String toSystem(ReferralPriority code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ReferralRequestRequesterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The device, practitioner, etc. who initiated the request.
         */
        @Child(name = "agent", type = {Practitioner.class, Organization.class, Patient.class, RelatedPerson.class, Device.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Individual making the request", formalDefinition="The device, practitioner, etc. who initiated the request." )
        protected Reference agent;

        /**
         * The actual object that is the target of the reference (The device, practitioner, etc. who initiated the request.)
         */
        protected Resource agentTarget;

        /**
         * The organization the device or practitioner was acting on behalf of.
         */
        @Child(name = "onBehalfOf", type = {Organization.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Organization agent is acting for", formalDefinition="The organization the device or practitioner was acting on behalf of." )
        protected Reference onBehalfOf;

        /**
         * The actual object that is the target of the reference (The organization the device or practitioner was acting on behalf of.)
         */
        protected Organization onBehalfOfTarget;

        private static final long serialVersionUID = -71453027L;

    /**
     * Constructor
     */
      public ReferralRequestRequesterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ReferralRequestRequesterComponent(Reference agent) {
        super();
        this.agent = agent;
      }

        /**
         * @return {@link #agent} (The device, practitioner, etc. who initiated the request.)
         */
        public Reference getAgent() { 
          if (this.agent == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ReferralRequestRequesterComponent.agent");
            else if (Configuration.doAutoCreate())
              this.agent = new Reference(); // cc
          return this.agent;
        }

        public boolean hasAgent() { 
          return this.agent != null && !this.agent.isEmpty();
        }

        /**
         * @param value {@link #agent} (The device, practitioner, etc. who initiated the request.)
         */
        public ReferralRequestRequesterComponent setAgent(Reference value) { 
          this.agent = value;
          return this;
        }

        /**
         * @return {@link #agent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who initiated the request.)
         */
        public Resource getAgentTarget() { 
          return this.agentTarget;
        }

        /**
         * @param value {@link #agent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who initiated the request.)
         */
        public ReferralRequestRequesterComponent setAgentTarget(Resource value) { 
          this.agentTarget = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} (The organization the device or practitioner was acting on behalf of.)
         */
        public Reference getOnBehalfOf() { 
          if (this.onBehalfOf == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ReferralRequestRequesterComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOf = new Reference(); // cc
          return this.onBehalfOf;
        }

        public boolean hasOnBehalfOf() { 
          return this.onBehalfOf != null && !this.onBehalfOf.isEmpty();
        }

        /**
         * @param value {@link #onBehalfOf} (The organization the device or practitioner was acting on behalf of.)
         */
        public ReferralRequestRequesterComponent setOnBehalfOf(Reference value) { 
          this.onBehalfOf = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public Organization getOnBehalfOfTarget() { 
          if (this.onBehalfOfTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ReferralRequestRequesterComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOfTarget = new Organization(); // aa
          return this.onBehalfOfTarget;
        }

        /**
         * @param value {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public ReferralRequestRequesterComponent setOnBehalfOfTarget(Organization value) { 
          this.onBehalfOfTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("agent", "Reference(Practitioner|Organization|Patient|RelatedPerson|Device)", "The device, practitioner, etc. who initiated the request.", 0, java.lang.Integer.MAX_VALUE, agent));
          childrenList.add(new Property("onBehalfOf", "Reference(Organization)", "The organization the device or practitioner was acting on behalf of.", 0, java.lang.Integer.MAX_VALUE, onBehalfOf));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : new Base[] {this.agent}; // Reference
        case -14402964: /*onBehalfOf*/ return this.onBehalfOf == null ? new Base[0] : new Base[] {this.onBehalfOf}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 92750597: // agent
          this.agent = castToReference(value); // Reference
          return value;
        case -14402964: // onBehalfOf
          this.onBehalfOf = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("agent")) {
          this.agent = castToReference(value); // Reference
        } else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92750597:  return getAgent(); 
        case -14402964:  return getOnBehalfOf(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92750597: /*agent*/ return new String[] {"Reference"};
        case -14402964: /*onBehalfOf*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("agent")) {
          this.agent = new Reference();
          return this.agent;
        }
        else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = new Reference();
          return this.onBehalfOf;
        }
        else
          return super.addChild(name);
      }

      public ReferralRequestRequesterComponent copy() {
        ReferralRequestRequesterComponent dst = new ReferralRequestRequesterComponent();
        copyValues(dst);
        dst.agent = agent == null ? null : agent.copy();
        dst.onBehalfOf = onBehalfOf == null ? null : onBehalfOf.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ReferralRequestRequesterComponent))
          return false;
        ReferralRequestRequesterComponent o = (ReferralRequestRequesterComponent) other;
        return compareDeep(agent, o.agent, true) && compareDeep(onBehalfOf, o.onBehalfOf, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ReferralRequestRequesterComponent))
          return false;
        ReferralRequestRequesterComponent o = (ReferralRequestRequesterComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(agent, onBehalfOf);
      }

  public String fhirType() {
    return "ReferralRequest.requester";

  }

  }

    /**
     * Business identifier that uniquely identifies the referral/care transfer request instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="Business identifier that uniquely identifies the referral/care transfer request instance." )
    protected List<Identifier> identifier;

    /**
     * A protocol, guideline, orderset or other definition that is adhered to in whole or in part by this request.
     */
    @Child(name = "definition", type = {ActivityDefinition.class, PlanDefinition.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Instantiates protocol or definition", formalDefinition="A protocol, guideline, orderset or other definition that is adhered to in whole or in part by this request." )
    protected List<Reference> definition;
    /**
     * The actual objects that are the target of the reference (A protocol, guideline, orderset or other definition that is adhered to in whole or in part by this request.)
     */
    protected List<Resource> definitionTarget;


    /**
     * Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.
     */
    @Child(name = "basedOn", type = {ReferralRequest.class, CarePlan.class, ProcedureRequest.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Request fulfilled by this request", formalDefinition="Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * Completed or terminated request(s) whose function is taken by this new request.
     */
    @Child(name = "replaces", type = {ReferralRequest.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Request(s) replaced by this request", formalDefinition="Completed or terminated request(s) whose function is taken by this new request." )
    protected List<Reference> replaces;
    /**
     * The actual objects that are the target of the reference (Completed or terminated request(s) whose function is taken by this new request.)
     */
    protected List<ReferralRequest> replacesTarget;


    /**
     * The business identifier of the logical "grouping" request/order that this referral is a part of.
     */
    @Child(name = "groupIdentifier", type = {Identifier.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Composite request this is part of", formalDefinition="The business identifier of the logical \"grouping\" request/order that this referral is a part of." )
    protected Identifier groupIdentifier;

    /**
     * The status of the authorization/intention reflected by the referral request record.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | suspended | cancelled | completed | entered-in-error | unknown", formalDefinition="The status of the authorization/intention reflected by the referral request record." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-status")
    protected Enumeration<ReferralRequestStatus> status;

    /**
     * Distinguishes the "level" of authorization/demand implicit in this request.
     */
    @Child(name = "intent", type = {CodeType.class}, order=6, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposal | plan | order", formalDefinition="Distinguishes the \"level\" of authorization/demand implicit in this request." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-intent")
    protected Enumeration<ReferralCategory> intent;

    /**
     * An indication of the type of referral (or where applicable the type of transfer of care) request.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Referral/Transition of care request type", formalDefinition="An indication of the type of referral (or where applicable the type of transfer of care) request." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/referral-type")
    protected CodeableConcept type;

    /**
     * An indication of the urgency of referral (or where applicable the type of transfer of care) request.
     */
    @Child(name = "priority", type = {CodeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Urgency of referral / transfer of care request", formalDefinition="An indication of the urgency of referral (or where applicable the type of transfer of care) request." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-priority")
    protected Enumeration<ReferralPriority> priority;

    /**
     * The service(s) that is/are requested to be provided to the patient.  For example: cardiac pacemaker insertion.
     */
    @Child(name = "serviceRequested", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Actions requested as part of the referral", formalDefinition="The service(s) that is/are requested to be provided to the patient.  For example: cardiac pacemaker insertion." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/c80-practice-codes")
    protected List<CodeableConcept> serviceRequested;

    /**
     * The patient who is the subject of a referral or transfer of care request.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient referred to care or transfer", formalDefinition="The patient who is the subject of a referral or transfer of care request." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who is the subject of a referral or transfer of care request.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter at which the request for referral or transfer of care is initiated.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Originating encounter", formalDefinition="The encounter at which the request for referral or transfer of care is initiated." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter at which the request for referral or transfer of care is initiated.)
     */
    protected Resource contextTarget;

    /**
     * The period of time within which the services identified in the referral/transfer of care is specified or required to occur.
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the service(s) requested in the referral should occur", formalDefinition="The period of time within which the services identified in the referral/transfer of care is specified or required to occur." )
    protected Type occurrence;

    /**
     * Date/DateTime of creation for draft requests and date of activation for active requests.
     */
    @Child(name = "authoredOn", type = {DateTimeType.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date of creation/activation", formalDefinition="Date/DateTime of creation for draft requests and date of activation for active requests." )
    protected DateTimeType authoredOn;

    /**
     * The individual who initiated the request and has responsibility for its activation.
     */
    @Child(name = "requester", type = {}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/what is requesting service", formalDefinition="The individual who initiated the request and has responsibility for its activation." )
    protected ReferralRequestRequesterComponent requester;

    /**
     * Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.  For example: Cardiology Gastroenterology Diabetology.
     */
    @Child(name = "specialty", type = {CodeableConcept.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The clinical specialty (discipline) that the referral is requested for", formalDefinition="Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.  For example: Cardiology Gastroenterology Diabetology." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/practitioner-specialty")
    protected CodeableConcept specialty;

    /**
     * The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.
     */
    @Child(name = "recipient", type = {Practitioner.class, Organization.class, HealthcareService.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Receiver of referral / transfer of care request", formalDefinition="The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.)
     */
    protected List<Resource> recipientTarget;


    /**
     * Description of clinical condition indicating why referral/transfer of care is requested.  For example:  Pathological Anomalies, Disabled (physical or mental),  Behavioral Management.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reason for referral / transfer of care request", formalDefinition="Description of clinical condition indicating why referral/transfer of care is requested.  For example:  Pathological Anomalies, Disabled (physical or mental),  Behavioral Management." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-findings")
    protected List<CodeableConcept> reasonCode;

    /**
     * Indicates another resource whose existence justifies this request.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Why is service needed?", formalDefinition="Indicates another resource whose existence justifies this request." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Indicates another resource whose existence justifies this request.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.
     */
    @Child(name = "description", type = {StringType.class}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A textual description of the referral", formalDefinition="The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary." )
    protected StringType description;

    /**
     * Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.
     */
    @Child(name = "supportingInfo", type = {Reference.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additonal information to support referral or transfer of care request", formalDefinition="Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan." )
    protected List<Reference> supportingInfo;
    /**
     * The actual objects that are the target of the reference (Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.)
     */
    protected List<Resource> supportingInfoTarget;


    /**
     * Comments made about the referral request by any of the participants.
     */
    @Child(name = "note", type = {Annotation.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about referral request", formalDefinition="Comments made about the referral request by any of the participants." )
    protected List<Annotation> note;

    /**
     * Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the resource.
     */
    @Child(name = "relevantHistory", type = {Provenance.class}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Key events in history of request", formalDefinition="Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the resource." )
    protected List<Reference> relevantHistory;
    /**
     * The actual objects that are the target of the reference (Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the resource.)
     */
    protected List<Provenance> relevantHistoryTarget;


    private static final long serialVersionUID = -404424161L;

  /**
   * Constructor
   */
    public ReferralRequest() {
      super();
    }

  /**
   * Constructor
   */
    public ReferralRequest(Enumeration<ReferralRequestStatus> status, Enumeration<ReferralCategory> intent, Reference subject) {
      super();
      this.status = status;
      this.intent = intent;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Business identifier that uniquely identifies the referral/care transfer request instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setIdentifier(List<Identifier> theIdentifier) { 
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

    public ReferralRequest addIdentifier(Identifier t) { //3
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
     * @return {@link #definition} (A protocol, guideline, orderset or other definition that is adhered to in whole or in part by this request.)
     */
    public List<Reference> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setDefinition(List<Reference> theDefinition) { 
      this.definition = theDefinition;
      return this;
    }

    public boolean hasDefinition() { 
      if (this.definition == null)
        return false;
      for (Reference item : this.definition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDefinition() { //3
      Reference t = new Reference();
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return t;
    }

    public ReferralRequest addDefinition(Reference t) { //3
      if (t == null)
        return this;
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #definition}, creating it if it does not already exist
     */
    public Reference getDefinitionFirstRep() { 
      if (getDefinition().isEmpty()) {
        addDefinition();
      }
      return getDefinition().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getDefinitionTarget() { 
      if (this.definitionTarget == null)
        this.definitionTarget = new ArrayList<Resource>();
      return this.definitionTarget;
    }

    /**
     * @return {@link #basedOn} (Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setBasedOn(List<Reference> theBasedOn) { 
      this.basedOn = theBasedOn;
      return this;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    public ReferralRequest addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #basedOn}, creating it if it does not already exist
     */
    public Reference getBasedOnFirstRep() { 
      if (getBasedOn().isEmpty()) {
        addBasedOn();
      }
      return getBasedOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<Resource>();
      return this.basedOnTarget;
    }

    /**
     * @return {@link #replaces} (Completed or terminated request(s) whose function is taken by this new request.)
     */
    public List<Reference> getReplaces() { 
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      return this.replaces;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setReplaces(List<Reference> theReplaces) { 
      this.replaces = theReplaces;
      return this;
    }

    public boolean hasReplaces() { 
      if (this.replaces == null)
        return false;
      for (Reference item : this.replaces)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReplaces() { //3
      Reference t = new Reference();
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      this.replaces.add(t);
      return t;
    }

    public ReferralRequest addReplaces(Reference t) { //3
      if (t == null)
        return this;
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      this.replaces.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #replaces}, creating it if it does not already exist
     */
    public Reference getReplacesFirstRep() { 
      if (getReplaces().isEmpty()) {
        addReplaces();
      }
      return getReplaces().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<ReferralRequest> getReplacesTarget() { 
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<ReferralRequest>();
      return this.replacesTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ReferralRequest addReplacesTarget() { 
      ReferralRequest r = new ReferralRequest();
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<ReferralRequest>();
      this.replacesTarget.add(r);
      return r;
    }

    /**
     * @return {@link #groupIdentifier} (The business identifier of the logical "grouping" request/order that this referral is a part of.)
     */
    public Identifier getGroupIdentifier() { 
      if (this.groupIdentifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.groupIdentifier");
        else if (Configuration.doAutoCreate())
          this.groupIdentifier = new Identifier(); // cc
      return this.groupIdentifier;
    }

    public boolean hasGroupIdentifier() { 
      return this.groupIdentifier != null && !this.groupIdentifier.isEmpty();
    }

    /**
     * @param value {@link #groupIdentifier} (The business identifier of the logical "grouping" request/order that this referral is a part of.)
     */
    public ReferralRequest setGroupIdentifier(Identifier value) { 
      this.groupIdentifier = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the authorization/intention reflected by the referral request record.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ReferralRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ReferralRequestStatus>(new ReferralRequestStatusEnumFactory()); // bb
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
    public ReferralRequest setStatusElement(Enumeration<ReferralRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the authorization/intention reflected by the referral request record.
     */
    public ReferralRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the authorization/intention reflected by the referral request record.
     */
    public ReferralRequest setStatus(ReferralRequestStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ReferralRequestStatus>(new ReferralRequestStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #intent} (Distinguishes the "level" of authorization/demand implicit in this request.). This is the underlying object with id, value and extensions. The accessor "getIntent" gives direct access to the value
     */
    public Enumeration<ReferralCategory> getIntentElement() { 
      if (this.intent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.intent");
        else if (Configuration.doAutoCreate())
          this.intent = new Enumeration<ReferralCategory>(new ReferralCategoryEnumFactory()); // bb
      return this.intent;
    }

    public boolean hasIntentElement() { 
      return this.intent != null && !this.intent.isEmpty();
    }

    public boolean hasIntent() { 
      return this.intent != null && !this.intent.isEmpty();
    }

    /**
     * @param value {@link #intent} (Distinguishes the "level" of authorization/demand implicit in this request.). This is the underlying object with id, value and extensions. The accessor "getIntent" gives direct access to the value
     */
    public ReferralRequest setIntentElement(Enumeration<ReferralCategory> value) { 
      this.intent = value;
      return this;
    }

    /**
     * @return Distinguishes the "level" of authorization/demand implicit in this request.
     */
    public ReferralCategory getIntent() { 
      return this.intent == null ? null : this.intent.getValue();
    }

    /**
     * @param value Distinguishes the "level" of authorization/demand implicit in this request.
     */
    public ReferralRequest setIntent(ReferralCategory value) { 
        if (this.intent == null)
          this.intent = new Enumeration<ReferralCategory>(new ReferralCategoryEnumFactory());
        this.intent.setValue(value);
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
     * @return {@link #priority} (An indication of the urgency of referral (or where applicable the type of transfer of care) request.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Enumeration<ReferralPriority> getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Enumeration<ReferralPriority>(new ReferralPriorityEnumFactory()); // bb
      return this.priority;
    }

    public boolean hasPriorityElement() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (An indication of the urgency of referral (or where applicable the type of transfer of care) request.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public ReferralRequest setPriorityElement(Enumeration<ReferralPriority> value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return An indication of the urgency of referral (or where applicable the type of transfer of care) request.
     */
    public ReferralPriority getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value An indication of the urgency of referral (or where applicable the type of transfer of care) request.
     */
    public ReferralRequest setPriority(ReferralPriority value) { 
      if (value == null)
        this.priority = null;
      else {
        if (this.priority == null)
          this.priority = new Enumeration<ReferralPriority>(new ReferralPriorityEnumFactory());
        this.priority.setValue(value);
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setServiceRequested(List<CodeableConcept> theServiceRequested) { 
      this.serviceRequested = theServiceRequested;
      return this;
    }

    public boolean hasServiceRequested() { 
      if (this.serviceRequested == null)
        return false;
      for (CodeableConcept item : this.serviceRequested)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addServiceRequested() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.serviceRequested == null)
        this.serviceRequested = new ArrayList<CodeableConcept>();
      this.serviceRequested.add(t);
      return t;
    }

    public ReferralRequest addServiceRequested(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.serviceRequested == null)
        this.serviceRequested = new ArrayList<CodeableConcept>();
      this.serviceRequested.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #serviceRequested}, creating it if it does not already exist
     */
    public CodeableConcept getServiceRequestedFirstRep() { 
      if (getServiceRequested().isEmpty()) {
        addServiceRequested();
      }
      return getServiceRequested().get(0);
    }

    /**
     * @return {@link #subject} (The patient who is the subject of a referral or transfer of care request.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient who is the subject of a referral or transfer of care request.)
     */
    public ReferralRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who is the subject of a referral or transfer of care request.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who is the subject of a referral or transfer of care request.)
     */
    public ReferralRequest setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
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
     * @return {@link #occurrence} (The period of time within which the services identified in the referral/transfer of care is specified or required to occur.)
     */
    public Type getOccurrence() { 
      return this.occurrence;
    }

    /**
     * @return {@link #occurrence} (The period of time within which the services identified in the referral/transfer of care is specified or required to occur.)
     */
    public DateTimeType getOccurrenceDateTimeType() throws FHIRException { 
      if (!(this.occurrence instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (DateTimeType) this.occurrence;
    }

    public boolean hasOccurrenceDateTimeType() { 
      return this.occurrence instanceof DateTimeType;
    }

    /**
     * @return {@link #occurrence} (The period of time within which the services identified in the referral/transfer of care is specified or required to occur.)
     */
    public Period getOccurrencePeriod() throws FHIRException { 
      if (!(this.occurrence instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Period) this.occurrence;
    }

    public boolean hasOccurrencePeriod() { 
      return this.occurrence instanceof Period;
    }

    public boolean hasOccurrence() { 
      return this.occurrence != null && !this.occurrence.isEmpty();
    }

    /**
     * @param value {@link #occurrence} (The period of time within which the services identified in the referral/transfer of care is specified or required to occur.)
     */
    public ReferralRequest setOccurrence(Type value) { 
      this.occurrence = value;
      return this;
    }

    /**
     * @return {@link #authoredOn} (Date/DateTime of creation for draft requests and date of activation for active requests.). This is the underlying object with id, value and extensions. The accessor "getAuthoredOn" gives direct access to the value
     */
    public DateTimeType getAuthoredOnElement() { 
      if (this.authoredOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.authoredOn");
        else if (Configuration.doAutoCreate())
          this.authoredOn = new DateTimeType(); // bb
      return this.authoredOn;
    }

    public boolean hasAuthoredOnElement() { 
      return this.authoredOn != null && !this.authoredOn.isEmpty();
    }

    public boolean hasAuthoredOn() { 
      return this.authoredOn != null && !this.authoredOn.isEmpty();
    }

    /**
     * @param value {@link #authoredOn} (Date/DateTime of creation for draft requests and date of activation for active requests.). This is the underlying object with id, value and extensions. The accessor "getAuthoredOn" gives direct access to the value
     */
    public ReferralRequest setAuthoredOnElement(DateTimeType value) { 
      this.authoredOn = value;
      return this;
    }

    /**
     * @return Date/DateTime of creation for draft requests and date of activation for active requests.
     */
    public Date getAuthoredOn() { 
      return this.authoredOn == null ? null : this.authoredOn.getValue();
    }

    /**
     * @param value Date/DateTime of creation for draft requests and date of activation for active requests.
     */
    public ReferralRequest setAuthoredOn(Date value) { 
      if (value == null)
        this.authoredOn = null;
      else {
        if (this.authoredOn == null)
          this.authoredOn = new DateTimeType();
        this.authoredOn.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #requester} (The individual who initiated the request and has responsibility for its activation.)
     */
    public ReferralRequestRequesterComponent getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ReferralRequest.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new ReferralRequestRequesterComponent(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (The individual who initiated the request and has responsibility for its activation.)
     */
    public ReferralRequest setRequester(ReferralRequestRequesterComponent value) { 
      this.requester = value;
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setRecipient(List<Reference> theRecipient) { 
      this.recipient = theRecipient;
      return this;
    }

    public boolean hasRecipient() { 
      if (this.recipient == null)
        return false;
      for (Reference item : this.recipient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addRecipient() { //3
      Reference t = new Reference();
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return t;
    }

    public ReferralRequest addRecipient(Reference t) { //3
      if (t == null)
        return this;
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #recipient}, creating it if it does not already exist
     */
    public Reference getRecipientFirstRep() { 
      if (getRecipient().isEmpty()) {
        addRecipient();
      }
      return getRecipient().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #reasonCode} (Description of clinical condition indicating why referral/transfer of care is requested.  For example:  Pathological Anomalies, Disabled (physical or mental),  Behavioral Management.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setReasonCode(List<CodeableConcept> theReasonCode) { 
      this.reasonCode = theReasonCode;
      return this;
    }

    public boolean hasReasonCode() { 
      if (this.reasonCode == null)
        return false;
      for (CodeableConcept item : this.reasonCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return t;
    }

    public ReferralRequest addReasonCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonCode}, creating it if it does not already exist
     */
    public CodeableConcept getReasonCodeFirstRep() { 
      if (getReasonCode().isEmpty()) {
        addReasonCode();
      }
      return getReasonCode().get(0);
    }

    /**
     * @return {@link #reasonReference} (Indicates another resource whose existence justifies this request.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setReasonReference(List<Reference> theReasonReference) { 
      this.reasonReference = theReasonReference;
      return this;
    }

    public boolean hasReasonReference() { 
      if (this.reasonReference == null)
        return false;
      for (Reference item : this.reasonReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReasonReference() { //3
      Reference t = new Reference();
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return t;
    }

    public ReferralRequest addReasonReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonReference}, creating it if it does not already exist
     */
    public Reference getReasonReferenceFirstRep() { 
      if (getReasonReference().isEmpty()) {
        addReasonReference();
      }
      return getReasonReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getReasonReferenceTarget() { 
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Resource>();
      return this.reasonReferenceTarget;
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
     * @return {@link #supportingInfo} (Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.)
     */
    public List<Reference> getSupportingInfo() { 
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      return this.supportingInfo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setSupportingInfo(List<Reference> theSupportingInfo) { 
      this.supportingInfo = theSupportingInfo;
      return this;
    }

    public boolean hasSupportingInfo() { 
      if (this.supportingInfo == null)
        return false;
      for (Reference item : this.supportingInfo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSupportingInfo() { //3
      Reference t = new Reference();
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      this.supportingInfo.add(t);
      return t;
    }

    public ReferralRequest addSupportingInfo(Reference t) { //3
      if (t == null)
        return this;
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      this.supportingInfo.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supportingInfo}, creating it if it does not already exist
     */
    public Reference getSupportingInfoFirstRep() { 
      if (getSupportingInfo().isEmpty()) {
        addSupportingInfo();
      }
      return getSupportingInfo().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSupportingInfoTarget() { 
      if (this.supportingInfoTarget == null)
        this.supportingInfoTarget = new ArrayList<Resource>();
      return this.supportingInfoTarget;
    }

    /**
     * @return {@link #note} (Comments made about the referral request by any of the participants.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public ReferralRequest addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

    /**
     * @return {@link #relevantHistory} (Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the resource.)
     */
    public List<Reference> getRelevantHistory() { 
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      return this.relevantHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ReferralRequest setRelevantHistory(List<Reference> theRelevantHistory) { 
      this.relevantHistory = theRelevantHistory;
      return this;
    }

    public boolean hasRelevantHistory() { 
      if (this.relevantHistory == null)
        return false;
      for (Reference item : this.relevantHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addRelevantHistory() { //3
      Reference t = new Reference();
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      this.relevantHistory.add(t);
      return t;
    }

    public ReferralRequest addRelevantHistory(Reference t) { //3
      if (t == null)
        return this;
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      this.relevantHistory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relevantHistory}, creating it if it does not already exist
     */
    public Reference getRelevantHistoryFirstRep() { 
      if (getRelevantHistory().isEmpty()) {
        addRelevantHistory();
      }
      return getRelevantHistory().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Provenance> getRelevantHistoryTarget() { 
      if (this.relevantHistoryTarget == null)
        this.relevantHistoryTarget = new ArrayList<Provenance>();
      return this.relevantHistoryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Provenance addRelevantHistoryTarget() { 
      Provenance r = new Provenance();
      if (this.relevantHistoryTarget == null)
        this.relevantHistoryTarget = new ArrayList<Provenance>();
      this.relevantHistoryTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Business identifier that uniquely identifies the referral/care transfer request instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("definition", "Reference(ActivityDefinition|PlanDefinition)", "A protocol, guideline, orderset or other definition that is adhered to in whole or in part by this request.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("basedOn", "Reference(ReferralRequest|CarePlan|ProcedureRequest)", "Indicates any plans, proposals or orders that this request is intended to satisfy - in whole or in part.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("replaces", "Reference(ReferralRequest)", "Completed or terminated request(s) whose function is taken by this new request.", 0, java.lang.Integer.MAX_VALUE, replaces));
        childrenList.add(new Property("groupIdentifier", "Identifier", "The business identifier of the logical \"grouping\" request/order that this referral is a part of.", 0, java.lang.Integer.MAX_VALUE, groupIdentifier));
        childrenList.add(new Property("status", "code", "The status of the authorization/intention reflected by the referral request record.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("intent", "code", "Distinguishes the \"level\" of authorization/demand implicit in this request.", 0, java.lang.Integer.MAX_VALUE, intent));
        childrenList.add(new Property("type", "CodeableConcept", "An indication of the type of referral (or where applicable the type of transfer of care) request.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("priority", "code", "An indication of the urgency of referral (or where applicable the type of transfer of care) request.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("serviceRequested", "CodeableConcept", "The service(s) that is/are requested to be provided to the patient.  For example: cardiac pacemaker insertion.", 0, java.lang.Integer.MAX_VALUE, serviceRequested));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The patient who is the subject of a referral or transfer of care request.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter at which the request for referral or transfer of care is initiated.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("occurrence[x]", "dateTime|Period", "The period of time within which the services identified in the referral/transfer of care is specified or required to occur.", 0, java.lang.Integer.MAX_VALUE, occurrence));
        childrenList.add(new Property("authoredOn", "dateTime", "Date/DateTime of creation for draft requests and date of activation for active requests.", 0, java.lang.Integer.MAX_VALUE, authoredOn));
        childrenList.add(new Property("requester", "", "The individual who initiated the request and has responsibility for its activation.", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("specialty", "CodeableConcept", "Indication of the clinical domain or discipline to which the referral or transfer of care request is sent.  For example: Cardiology Gastroenterology Diabetology.", 0, java.lang.Integer.MAX_VALUE, specialty));
        childrenList.add(new Property("recipient", "Reference(Practitioner|Organization|HealthcareService)", "The healthcare provider(s) or provider organization(s) who/which is to receive the referral/transfer of care request.", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "Description of clinical condition indicating why referral/transfer of care is requested.  For example:  Pathological Anomalies, Disabled (physical or mental),  Behavioral Management.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("reasonReference", "Reference(Condition|Observation)", "Indicates another resource whose existence justifies this request.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("description", "string", "The reason element gives a short description of why the referral is being made, the description expands on this to support a more complete clinical summary.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("supportingInfo", "Reference(Any)", "Any additional (administrative, financial or clinical) information required to support request for referral or transfer of care.  For example: Presenting problems/chief complaints Medical History Family History Alerts Allergy/Intolerance and Adverse Reactions Medications Observations/Assessments (may include cognitive and fundtional assessments) Diagnostic Reports Care Plan.", 0, java.lang.Integer.MAX_VALUE, supportingInfo));
        childrenList.add(new Property("note", "Annotation", "Comments made about the referral request by any of the participants.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("relevantHistory", "Reference(Provenance)", "Links to Provenance records for past versions of this resource or fulfilling request or event resources that identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the resource.", 0, java.lang.Integer.MAX_VALUE, relevantHistory));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : this.definition.toArray(new Base[this.definition.size()]); // Reference
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -430332865: /*replaces*/ return this.replaces == null ? new Base[0] : this.replaces.toArray(new Base[this.replaces.size()]); // Reference
        case -445338488: /*groupIdentifier*/ return this.groupIdentifier == null ? new Base[0] : new Base[] {this.groupIdentifier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ReferralRequestStatus>
        case -1183762788: /*intent*/ return this.intent == null ? new Base[0] : new Base[] {this.intent}; // Enumeration<ReferralCategory>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Enumeration<ReferralPriority>
        case 190229561: /*serviceRequested*/ return this.serviceRequested == null ? new Base[0] : this.serviceRequested.toArray(new Base[this.serviceRequested.size()]); // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case -1500852503: /*authoredOn*/ return this.authoredOn == null ? new Base[0] : new Base[] {this.authoredOn}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // ReferralRequestRequesterComponent
        case -1694759682: /*specialty*/ return this.specialty == null ? new Base[0] : new Base[] {this.specialty}; // CodeableConcept
        case 820081177: /*recipient*/ return this.recipient == null ? new Base[0] : this.recipient.toArray(new Base[this.recipient.size()]); // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 1922406657: /*supportingInfo*/ return this.supportingInfo == null ? new Base[0] : this.supportingInfo.toArray(new Base[this.supportingInfo.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 1538891575: /*relevantHistory*/ return this.relevantHistory == null ? new Base[0] : this.relevantHistory.toArray(new Base[this.relevantHistory.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1014418093: // definition
          this.getDefinition().add(castToReference(value)); // Reference
          return value;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          return value;
        case -430332865: // replaces
          this.getReplaces().add(castToReference(value)); // Reference
          return value;
        case -445338488: // groupIdentifier
          this.groupIdentifier = castToIdentifier(value); // Identifier
          return value;
        case -892481550: // status
          value = new ReferralRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ReferralRequestStatus>
          return value;
        case -1183762788: // intent
          value = new ReferralCategoryEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<ReferralCategory>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1165461084: // priority
          value = new ReferralPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<ReferralPriority>
          return value;
        case 190229561: // serviceRequested
          this.getServiceRequested().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case 1687874001: // occurrence
          this.occurrence = castToType(value); // Type
          return value;
        case -1500852503: // authoredOn
          this.authoredOn = castToDateTime(value); // DateTimeType
          return value;
        case 693933948: // requester
          this.requester = (ReferralRequestRequesterComponent) value; // ReferralRequestRequesterComponent
          return value;
        case -1694759682: // specialty
          this.specialty = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 820081177: // recipient
          this.getRecipient().add(castToReference(value)); // Reference
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 1922406657: // supportingInfo
          this.getSupportingInfo().add(castToReference(value)); // Reference
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case 1538891575: // relevantHistory
          this.getRelevantHistory().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("definition")) {
          this.getDefinition().add(castToReference(value));
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("replaces")) {
          this.getReplaces().add(castToReference(value));
        } else if (name.equals("groupIdentifier")) {
          this.groupIdentifier = castToIdentifier(value); // Identifier
        } else if (name.equals("status")) {
          value = new ReferralRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ReferralRequestStatus>
        } else if (name.equals("intent")) {
          value = new ReferralCategoryEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<ReferralCategory>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("priority")) {
          value = new ReferralPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<ReferralPriority>
        } else if (name.equals("serviceRequested")) {
          this.getServiceRequested().add(castToCodeableConcept(value));
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("occurrence[x]")) {
          this.occurrence = castToType(value); // Type
        } else if (name.equals("authoredOn")) {
          this.authoredOn = castToDateTime(value); // DateTimeType
        } else if (name.equals("requester")) {
          this.requester = (ReferralRequestRequesterComponent) value; // ReferralRequestRequesterComponent
        } else if (name.equals("specialty")) {
          this.specialty = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("recipient")) {
          this.getRecipient().add(castToReference(value));
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("supportingInfo")) {
          this.getSupportingInfo().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("relevantHistory")) {
          this.getRelevantHistory().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1014418093:  return addDefinition(); 
        case -332612366:  return addBasedOn(); 
        case -430332865:  return addReplaces(); 
        case -445338488:  return getGroupIdentifier(); 
        case -892481550:  return getStatusElement();
        case -1183762788:  return getIntentElement();
        case 3575610:  return getType(); 
        case -1165461084:  return getPriorityElement();
        case 190229561:  return addServiceRequested(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -2022646513:  return getOccurrence(); 
        case 1687874001:  return getOccurrence(); 
        case -1500852503:  return getAuthoredOnElement();
        case 693933948:  return getRequester(); 
        case -1694759682:  return getSpecialty(); 
        case 820081177:  return addRecipient(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case -1724546052:  return getDescriptionElement();
        case 1922406657:  return addSupportingInfo(); 
        case 3387378:  return addNote(); 
        case 1538891575:  return addRelevantHistory(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -430332865: /*replaces*/ return new String[] {"Reference"};
        case -445338488: /*groupIdentifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1183762788: /*intent*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1165461084: /*priority*/ return new String[] {"code"};
        case 190229561: /*serviceRequested*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case 1687874001: /*occurrence*/ return new String[] {"dateTime", "Period"};
        case -1500852503: /*authoredOn*/ return new String[] {"dateTime"};
        case 693933948: /*requester*/ return new String[] {};
        case -1694759682: /*specialty*/ return new String[] {"CodeableConcept"};
        case 820081177: /*recipient*/ return new String[] {"Reference"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 1922406657: /*supportingInfo*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case 1538891575: /*relevantHistory*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("definition")) {
          return addDefinition();
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("replaces")) {
          return addReplaces();
        }
        else if (name.equals("groupIdentifier")) {
          this.groupIdentifier = new Identifier();
          return this.groupIdentifier;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ReferralRequest.status");
        }
        else if (name.equals("intent")) {
          throw new FHIRException("Cannot call addChild on a primitive type ReferralRequest.intent");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type ReferralRequest.priority");
        }
        else if (name.equals("serviceRequested")) {
          return addServiceRequested();
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("occurrenceDateTime")) {
          this.occurrence = new DateTimeType();
          return this.occurrence;
        }
        else if (name.equals("occurrencePeriod")) {
          this.occurrence = new Period();
          return this.occurrence;
        }
        else if (name.equals("authoredOn")) {
          throw new FHIRException("Cannot call addChild on a primitive type ReferralRequest.authoredOn");
        }
        else if (name.equals("requester")) {
          this.requester = new ReferralRequestRequesterComponent();
          return this.requester;
        }
        else if (name.equals("specialty")) {
          this.specialty = new CodeableConcept();
          return this.specialty;
        }
        else if (name.equals("recipient")) {
          return addRecipient();
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ReferralRequest.description");
        }
        else if (name.equals("supportingInfo")) {
          return addSupportingInfo();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("relevantHistory")) {
          return addRelevantHistory();
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
        if (definition != null) {
          dst.definition = new ArrayList<Reference>();
          for (Reference i : definition)
            dst.definition.add(i.copy());
        };
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        if (replaces != null) {
          dst.replaces = new ArrayList<Reference>();
          for (Reference i : replaces)
            dst.replaces.add(i.copy());
        };
        dst.groupIdentifier = groupIdentifier == null ? null : groupIdentifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.intent = intent == null ? null : intent.copy();
        dst.type = type == null ? null : type.copy();
        dst.priority = priority == null ? null : priority.copy();
        if (serviceRequested != null) {
          dst.serviceRequested = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : serviceRequested)
            dst.serviceRequested.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        dst.authoredOn = authoredOn == null ? null : authoredOn.copy();
        dst.requester = requester == null ? null : requester.copy();
        dst.specialty = specialty == null ? null : specialty.copy();
        if (recipient != null) {
          dst.recipient = new ArrayList<Reference>();
          for (Reference i : recipient)
            dst.recipient.add(i.copy());
        };
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        if (reasonReference != null) {
          dst.reasonReference = new ArrayList<Reference>();
          for (Reference i : reasonReference)
            dst.reasonReference.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (supportingInfo != null) {
          dst.supportingInfo = new ArrayList<Reference>();
          for (Reference i : supportingInfo)
            dst.supportingInfo.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (relevantHistory != null) {
          dst.relevantHistory = new ArrayList<Reference>();
          for (Reference i : relevantHistory)
            dst.relevantHistory.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(definition, o.definition, true)
           && compareDeep(basedOn, o.basedOn, true) && compareDeep(replaces, o.replaces, true) && compareDeep(groupIdentifier, o.groupIdentifier, true)
           && compareDeep(status, o.status, true) && compareDeep(intent, o.intent, true) && compareDeep(type, o.type, true)
           && compareDeep(priority, o.priority, true) && compareDeep(serviceRequested, o.serviceRequested, true)
           && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true) && compareDeep(occurrence, o.occurrence, true)
           && compareDeep(authoredOn, o.authoredOn, true) && compareDeep(requester, o.requester, true) && compareDeep(specialty, o.specialty, true)
           && compareDeep(recipient, o.recipient, true) && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(description, o.description, true) && compareDeep(supportingInfo, o.supportingInfo, true)
           && compareDeep(note, o.note, true) && compareDeep(relevantHistory, o.relevantHistory, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ReferralRequest))
          return false;
        ReferralRequest o = (ReferralRequest) other;
        return compareValues(status, o.status, true) && compareValues(intent, o.intent, true) && compareValues(priority, o.priority, true)
           && compareValues(authoredOn, o.authoredOn, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, definition, basedOn
          , replaces, groupIdentifier, status, intent, type, priority, serviceRequested
          , subject, context, occurrence, authoredOn, requester, specialty, recipient, reasonCode
          , reasonReference, description, supportingInfo, note, relevantHistory);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ReferralRequest;
   }

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>Individual making the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.requester.agent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="ReferralRequest.requester.agent", description="Individual making the request", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>Individual making the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.requester.agent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("ReferralRequest:requester").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ReferralRequest.identifier", description="Business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

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
   * Search parameter: <b>replaces</b>
   * <p>
   * Description: <b>Request(s) replaced by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.replaces</b><br>
   * </p>
   */
  @SearchParamDefinition(name="replaces", path="ReferralRequest.replaces", description="Request(s) replaced by this request", type="reference", target={ReferralRequest.class } )
  public static final String SP_REPLACES = "replaces";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>replaces</b>
   * <p>
   * Description: <b>Request(s) replaced by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.replaces</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REPLACES = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REPLACES);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:replaces</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REPLACES = new ca.uhn.fhir.model.api.Include("ReferralRequest:replaces").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Patient referred to care or transfer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="ReferralRequest.subject", description="Patient referred to care or transfer", type="reference", target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Patient referred to care or transfer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("ReferralRequest:subject").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Originating encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="ReferralRequest.context", description="Originating encounter", type="reference", target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Originating encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("ReferralRequest:encounter").toLocked();

 /**
   * Search parameter: <b>authored-on</b>
   * <p>
   * Description: <b>Creation or activation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ReferralRequest.authoredOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authored-on", path="ReferralRequest.authoredOn", description="Creation or activation date", type="date" )
  public static final String SP_AUTHORED_ON = "authored-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authored-on</b>
   * <p>
   * Description: <b>Creation or activation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ReferralRequest.authoredOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHORED_ON = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHORED_ON);

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
   * Search parameter: <b>intent</b>
   * <p>
   * Description: <b>Proposal, plan or order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.intent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="intent", path="ReferralRequest.intent", description="Proposal, plan or order", type="token" )
  public static final String SP_INTENT = "intent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>intent</b>
   * <p>
   * Description: <b>Proposal, plan or order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.intent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam INTENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_INTENT);

 /**
   * Search parameter: <b>group-identifier</b>
   * <p>
   * Description: <b>Part of common request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.groupIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="group-identifier", path="ReferralRequest.groupIdentifier", description="Part of common request", type="token" )
  public static final String SP_GROUP_IDENTIFIER = "group-identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>group-identifier</b>
   * <p>
   * Description: <b>Part of common request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.groupIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam GROUP_IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_GROUP_IDENTIFIER);

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>Request being fulfilled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="ReferralRequest.basedOn", description="Request being fulfilled", type="reference", target={CarePlan.class, ProcedureRequest.class, ReferralRequest.class } )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>Request being fulfilled</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("ReferralRequest:based-on").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who the referral is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ReferralRequest.subject", description="Who the referral is about", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who the referral is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ReferralRequest:patient").toLocked();

 /**
   * Search parameter: <b>service</b>
   * <p>
   * Description: <b>Actions requested as part of the referral</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.serviceRequested</b><br>
   * </p>
   */
  @SearchParamDefinition(name="service", path="ReferralRequest.serviceRequested", description="Actions requested as part of the referral", type="token" )
  public static final String SP_SERVICE = "service";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>service</b>
   * <p>
   * Description: <b>Actions requested as part of the referral</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ReferralRequest.serviceRequested</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SERVICE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SERVICE);

 /**
   * Search parameter: <b>occurrence-date</b>
   * <p>
   * Description: <b>When the service(s) requested in the referral should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ReferralRequest.occurrence[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="occurrence-date", path="ReferralRequest.occurrence", description="When the service(s) requested in the referral should occur", type="date" )
  public static final String SP_OCCURRENCE_DATE = "occurrence-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>occurrence-date</b>
   * <p>
   * Description: <b>When the service(s) requested in the referral should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ReferralRequest.occurrence[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam OCCURRENCE_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_OCCURRENCE_DATE);

 /**
   * Search parameter: <b>recipient</b>
   * <p>
   * Description: <b>The person that the referral was sent to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.recipient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recipient", path="ReferralRequest.recipient", description="The person that the referral was sent to", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={HealthcareService.class, Organization.class, Practitioner.class } )
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

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Part of encounter or episode of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="ReferralRequest.context", description="Part of encounter or episode of care", type="reference", target={Encounter.class, EpisodeOfCare.class } )
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
   * Search parameter: <b>definition</b>
   * <p>
   * Description: <b>Instantiates protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="definition", path="ReferralRequest.definition", description="Instantiates protocol or definition", type="reference", target={ActivityDefinition.class, PlanDefinition.class } )
  public static final String SP_DEFINITION = "definition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>definition</b>
   * <p>
   * Description: <b>Instantiates protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ReferralRequest.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEFINITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEFINITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ReferralRequest:definition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEFINITION = new ca.uhn.fhir.model.api.Include("ReferralRequest:definition").toLocked();

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


}

