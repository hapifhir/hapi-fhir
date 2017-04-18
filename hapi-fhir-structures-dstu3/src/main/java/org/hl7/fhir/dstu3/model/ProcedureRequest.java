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
 * A record of a request for diagnostic investigations, treatments, or operations to be performed.
 */
@ResourceDef(name="ProcedureRequest", profile="http://hl7.org/fhir/Profile/ProcedureRequest")
public class ProcedureRequest extends DomainResource {

    public enum ProcedureRequestStatus {
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
        public static ProcedureRequestStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown ProcedureRequestStatus code '"+codeString+"'");
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

  public static class ProcedureRequestStatusEnumFactory implements EnumFactory<ProcedureRequestStatus> {
    public ProcedureRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ProcedureRequestStatus.DRAFT;
        if ("active".equals(codeString))
          return ProcedureRequestStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return ProcedureRequestStatus.SUSPENDED;
        if ("cancelled".equals(codeString))
          return ProcedureRequestStatus.CANCELLED;
        if ("completed".equals(codeString))
          return ProcedureRequestStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ProcedureRequestStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return ProcedureRequestStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown ProcedureRequestStatus code '"+codeString+"'");
        }
        public Enumeration<ProcedureRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ProcedureRequestStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.SUSPENDED);
        if ("cancelled".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.CANCELLED);
        if ("completed".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.UNKNOWN);
        throw new FHIRException("Unknown ProcedureRequestStatus code '"+codeString+"'");
        }
    public String toCode(ProcedureRequestStatus code) {
      if (code == ProcedureRequestStatus.DRAFT)
        return "draft";
      if (code == ProcedureRequestStatus.ACTIVE)
        return "active";
      if (code == ProcedureRequestStatus.SUSPENDED)
        return "suspended";
      if (code == ProcedureRequestStatus.CANCELLED)
        return "cancelled";
      if (code == ProcedureRequestStatus.COMPLETED)
        return "completed";
      if (code == ProcedureRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ProcedureRequestStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(ProcedureRequestStatus code) {
      return code.getSystem();
      }
    }

    public enum ProcedureRequestIntent {
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
        public static ProcedureRequestIntent fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown ProcedureRequestIntent code '"+codeString+"'");
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

  public static class ProcedureRequestIntentEnumFactory implements EnumFactory<ProcedureRequestIntent> {
    public ProcedureRequestIntent fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposal".equals(codeString))
          return ProcedureRequestIntent.PROPOSAL;
        if ("plan".equals(codeString))
          return ProcedureRequestIntent.PLAN;
        if ("order".equals(codeString))
          return ProcedureRequestIntent.ORDER;
        if ("original-order".equals(codeString))
          return ProcedureRequestIntent.ORIGINALORDER;
        if ("reflex-order".equals(codeString))
          return ProcedureRequestIntent.REFLEXORDER;
        if ("filler-order".equals(codeString))
          return ProcedureRequestIntent.FILLERORDER;
        if ("instance-order".equals(codeString))
          return ProcedureRequestIntent.INSTANCEORDER;
        if ("option".equals(codeString))
          return ProcedureRequestIntent.OPTION;
        throw new IllegalArgumentException("Unknown ProcedureRequestIntent code '"+codeString+"'");
        }
        public Enumeration<ProcedureRequestIntent> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ProcedureRequestIntent>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposal".equals(codeString))
          return new Enumeration<ProcedureRequestIntent>(this, ProcedureRequestIntent.PROPOSAL);
        if ("plan".equals(codeString))
          return new Enumeration<ProcedureRequestIntent>(this, ProcedureRequestIntent.PLAN);
        if ("order".equals(codeString))
          return new Enumeration<ProcedureRequestIntent>(this, ProcedureRequestIntent.ORDER);
        if ("original-order".equals(codeString))
          return new Enumeration<ProcedureRequestIntent>(this, ProcedureRequestIntent.ORIGINALORDER);
        if ("reflex-order".equals(codeString))
          return new Enumeration<ProcedureRequestIntent>(this, ProcedureRequestIntent.REFLEXORDER);
        if ("filler-order".equals(codeString))
          return new Enumeration<ProcedureRequestIntent>(this, ProcedureRequestIntent.FILLERORDER);
        if ("instance-order".equals(codeString))
          return new Enumeration<ProcedureRequestIntent>(this, ProcedureRequestIntent.INSTANCEORDER);
        if ("option".equals(codeString))
          return new Enumeration<ProcedureRequestIntent>(this, ProcedureRequestIntent.OPTION);
        throw new FHIRException("Unknown ProcedureRequestIntent code '"+codeString+"'");
        }
    public String toCode(ProcedureRequestIntent code) {
      if (code == ProcedureRequestIntent.PROPOSAL)
        return "proposal";
      if (code == ProcedureRequestIntent.PLAN)
        return "plan";
      if (code == ProcedureRequestIntent.ORDER)
        return "order";
      if (code == ProcedureRequestIntent.ORIGINALORDER)
        return "original-order";
      if (code == ProcedureRequestIntent.REFLEXORDER)
        return "reflex-order";
      if (code == ProcedureRequestIntent.FILLERORDER)
        return "filler-order";
      if (code == ProcedureRequestIntent.INSTANCEORDER)
        return "instance-order";
      if (code == ProcedureRequestIntent.OPTION)
        return "option";
      return "?";
      }
    public String toSystem(ProcedureRequestIntent code) {
      return code.getSystem();
      }
    }

    public enum ProcedureRequestPriority {
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
        public static ProcedureRequestPriority fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown ProcedureRequestPriority code '"+codeString+"'");
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

  public static class ProcedureRequestPriorityEnumFactory implements EnumFactory<ProcedureRequestPriority> {
    public ProcedureRequestPriority fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return ProcedureRequestPriority.ROUTINE;
        if ("urgent".equals(codeString))
          return ProcedureRequestPriority.URGENT;
        if ("asap".equals(codeString))
          return ProcedureRequestPriority.ASAP;
        if ("stat".equals(codeString))
          return ProcedureRequestPriority.STAT;
        throw new IllegalArgumentException("Unknown ProcedureRequestPriority code '"+codeString+"'");
        }
        public Enumeration<ProcedureRequestPriority> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ProcedureRequestPriority>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("routine".equals(codeString))
          return new Enumeration<ProcedureRequestPriority>(this, ProcedureRequestPriority.ROUTINE);
        if ("urgent".equals(codeString))
          return new Enumeration<ProcedureRequestPriority>(this, ProcedureRequestPriority.URGENT);
        if ("asap".equals(codeString))
          return new Enumeration<ProcedureRequestPriority>(this, ProcedureRequestPriority.ASAP);
        if ("stat".equals(codeString))
          return new Enumeration<ProcedureRequestPriority>(this, ProcedureRequestPriority.STAT);
        throw new FHIRException("Unknown ProcedureRequestPriority code '"+codeString+"'");
        }
    public String toCode(ProcedureRequestPriority code) {
      if (code == ProcedureRequestPriority.ROUTINE)
        return "routine";
      if (code == ProcedureRequestPriority.URGENT)
        return "urgent";
      if (code == ProcedureRequestPriority.ASAP)
        return "asap";
      if (code == ProcedureRequestPriority.STAT)
        return "stat";
      return "?";
      }
    public String toSystem(ProcedureRequestPriority code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ProcedureRequestRequesterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The device, practitioner or organization who initiated the request.
         */
        @Child(name = "agent", type = {Device.class, Practitioner.class, Organization.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Individual making the request", formalDefinition="The device, practitioner or organization who initiated the request." )
        protected Reference agent;

        /**
         * The actual object that is the target of the reference (The device, practitioner or organization who initiated the request.)
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
      public ProcedureRequestRequesterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProcedureRequestRequesterComponent(Reference agent) {
        super();
        this.agent = agent;
      }

        /**
         * @return {@link #agent} (The device, practitioner or organization who initiated the request.)
         */
        public Reference getAgent() { 
          if (this.agent == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureRequestRequesterComponent.agent");
            else if (Configuration.doAutoCreate())
              this.agent = new Reference(); // cc
          return this.agent;
        }

        public boolean hasAgent() { 
          return this.agent != null && !this.agent.isEmpty();
        }

        /**
         * @param value {@link #agent} (The device, practitioner or organization who initiated the request.)
         */
        public ProcedureRequestRequesterComponent setAgent(Reference value) { 
          this.agent = value;
          return this;
        }

        /**
         * @return {@link #agent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device, practitioner or organization who initiated the request.)
         */
        public Resource getAgentTarget() { 
          return this.agentTarget;
        }

        /**
         * @param value {@link #agent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device, practitioner or organization who initiated the request.)
         */
        public ProcedureRequestRequesterComponent setAgentTarget(Resource value) { 
          this.agentTarget = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} (The organization the device or practitioner was acting on behalf of.)
         */
        public Reference getOnBehalfOf() { 
          if (this.onBehalfOf == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureRequestRequesterComponent.onBehalfOf");
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
        public ProcedureRequestRequesterComponent setOnBehalfOf(Reference value) { 
          this.onBehalfOf = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public Organization getOnBehalfOfTarget() { 
          if (this.onBehalfOfTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureRequestRequesterComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOfTarget = new Organization(); // aa
          return this.onBehalfOfTarget;
        }

        /**
         * @param value {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public ProcedureRequestRequesterComponent setOnBehalfOfTarget(Organization value) { 
          this.onBehalfOfTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("agent", "Reference(Device|Practitioner|Organization)", "The device, practitioner or organization who initiated the request.", 0, java.lang.Integer.MAX_VALUE, agent));
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

      public ProcedureRequestRequesterComponent copy() {
        ProcedureRequestRequesterComponent dst = new ProcedureRequestRequesterComponent();
        copyValues(dst);
        dst.agent = agent == null ? null : agent.copy();
        dst.onBehalfOf = onBehalfOf == null ? null : onBehalfOf.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedureRequestRequesterComponent))
          return false;
        ProcedureRequestRequesterComponent o = (ProcedureRequestRequesterComponent) other;
        return compareDeep(agent, o.agent, true) && compareDeep(onBehalfOf, o.onBehalfOf, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedureRequestRequesterComponent))
          return false;
        ProcedureRequestRequesterComponent o = (ProcedureRequestRequesterComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(agent, onBehalfOf);
      }

  public String fhirType() {
    return "ProcedureRequest.requester";

  }

  }

    /**
     * Identifiers assigned to this order instance by the orderer and/or the receiver and/or order fulfiller.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifiers assigned to this order", formalDefinition="Identifiers assigned to this order instance by the orderer and/or the receiver and/or order fulfiller." )
    protected List<Identifier> identifier;

    /**
     * Protocol or definition followed by this request.
     */
    @Child(name = "definition", type = {ActivityDefinition.class, PlanDefinition.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Protocol or definition", formalDefinition="Protocol or definition followed by this request." )
    protected List<Reference> definition;
    /**
     * The actual objects that are the target of the reference (Protocol or definition followed by this request.)
     */
    protected List<Resource> definitionTarget;


    /**
     * Plan/proposal/order fulfilled by this request.
     */
    @Child(name = "basedOn", type = {Reference.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What request fulfills", formalDefinition="Plan/proposal/order fulfilled by this request." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (Plan/proposal/order fulfilled by this request.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * The request takes the place of the referenced completed or terminated request(s).
     */
    @Child(name = "replaces", type = {Reference.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What request replaces", formalDefinition="The request takes the place of the referenced completed or terminated request(s)." )
    protected List<Reference> replaces;
    /**
     * The actual objects that are the target of the reference (The request takes the place of the referenced completed or terminated request(s).)
     */
    protected List<Resource> replacesTarget;


    /**
     * A shared identifier common to all procedure or diagnostic requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier.
     */
    @Child(name = "requisition", type = {Identifier.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Composite Request ID", formalDefinition="A shared identifier common to all procedure or diagnostic requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier." )
    protected Identifier requisition;

    /**
     * The status of the order.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | suspended | completed | entered-in-error | cancelled", formalDefinition="The status of the order." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-status")
    protected Enumeration<ProcedureRequestStatus> status;

    /**
     * Whether the request is a proposal, plan, an original order or a reflex order.
     */
    @Child(name = "intent", type = {CodeType.class}, order=6, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposal | plan | order +", formalDefinition="Whether the request is a proposal, plan, an original order or a reflex order." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-intent")
    protected Enumeration<ProcedureRequestIntent> intent;

    /**
     * Indicates how quickly the ProcedureRequest should be addressed with respect to other requests.
     */
    @Child(name = "priority", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="routine | urgent | asap | stat", formalDefinition="Indicates how quickly the ProcedureRequest should be addressed with respect to other requests." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-priority")
    protected Enumeration<ProcedureRequestPriority> priority;

    /**
     * Set this to true if the record is saying that the procedure should NOT be performed.
     */
    @Child(name = "doNotPerform", type = {BooleanType.class}, order=8, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="True if procedure should not be performed", formalDefinition="Set this to true if the record is saying that the procedure should NOT be performed." )
    protected BooleanType doNotPerform;

    /**
     * A code that classifies the procedure for searching, sorting and display purposes (e.g. "Surgical Procedure").
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Classification of procedure", formalDefinition="A code that classifies the procedure for searching, sorting and display purposes (e.g. \"Surgical Procedure\")." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-category")
    protected List<CodeableConcept> category;

    /**
     * A code that identifies a particular procedure, diagnostic investigation, or panel of investigations, that have been requested.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What is being requested/ordered", formalDefinition="A code that identifies a particular procedure, diagnostic investigation, or panel of investigations, that have been requested." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-code")
    protected CodeableConcept code;

    /**
     * On whom or what the procedure or diagnostic is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Location.class, Device.class}, order=11, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Individual the service is ordered for", formalDefinition="On whom or what the procedure or diagnostic is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (On whom or what the procedure or diagnostic is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    protected Resource subjectTarget;

    /**
     * An encounter or episode of care that provides additional information about the healthcare context in which this request is made.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter or Episode during which request was created", formalDefinition="An encounter or episode of care that provides additional information about the healthcare context in which this request is made." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    protected Resource contextTarget;

    /**
     * The date/time at which the diagnostic testing should occur.
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class, Timing.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When procedure should occur", formalDefinition="The date/time at which the diagnostic testing should occur." )
    protected Type occurrence;

    /**
     * If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.  For example "pain", "on flare-up", etc.
     */
    @Child(name = "asNeeded", type = {BooleanType.class, CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Preconditions for procedure or diagnostic", formalDefinition="If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.  For example \"pain\", \"on flare-up\", etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-as-needed-reason")
    protected Type asNeeded;

    /**
     * When the request transitioned to being actionable.
     */
    @Child(name = "authoredOn", type = {DateTimeType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date request signed", formalDefinition="When the request transitioned to being actionable." )
    protected DateTimeType authoredOn;

    /**
     * The individual who initiated the request and has responsibility for its activation.
     */
    @Child(name = "requester", type = {}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/what is requesting procedure or diagnostic", formalDefinition="The individual who initiated the request and has responsibility for its activation." )
    protected ProcedureRequestRequesterComponent requester;

    /**
     * Desired type of performer for doing the diagnostic testing.
     */
    @Child(name = "performerType", type = {CodeableConcept.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Performer role", formalDefinition="Desired type of performer for doing the diagnostic testing." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/participant-role")
    protected CodeableConcept performerType;

    /**
     * The desired perfomer for doing the diagnostic testing.  For example, the surgeon, dermatopathologist, endoscopist, etc.
     */
    @Child(name = "performer", type = {Practitioner.class, Organization.class, Patient.class, Device.class, RelatedPerson.class, HealthcareService.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Requested perfomer", formalDefinition="The desired perfomer for doing the diagnostic testing.  For example, the surgeon, dermatopathologist, endoscopist, etc." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (The desired perfomer for doing the diagnostic testing.  For example, the surgeon, dermatopathologist, endoscopist, etc.)
     */
    protected Resource performerTarget;

    /**
     * An explanation or justification for why this diagnostic investigation is being requested in coded or textual form.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Explanation/Justification for test", formalDefinition="An explanation or justification for why this diagnostic investigation is being requested in coded or textual form.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-reason")
    protected List<CodeableConcept> reasonCode;

    /**
     * Indicates another resource that provides a justification for why this diagnostic investigation is being requested.   May relate to the resources referred to in supportingInformation.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Explanation/Justification for test", formalDefinition="Indicates another resource that provides a justification for why this diagnostic investigation is being requested.   May relate to the resources referred to in supportingInformation." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Indicates another resource that provides a justification for why this diagnostic investigation is being requested.   May relate to the resources referred to in supportingInformation.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * Additional clinical information about the patient or specimen that may influence the procedure or diagnostics or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as "ask at order entry questions (AOEs)".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements.
     */
    @Child(name = "supportingInfo", type = {Reference.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional clinical information", formalDefinition="Additional clinical information about the patient or specimen that may influence the procedure or diagnostics or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as \"ask at order entry questions (AOEs)\".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements." )
    protected List<Reference> supportingInfo;
    /**
     * The actual objects that are the target of the reference (Additional clinical information about the patient or specimen that may influence the procedure or diagnostics or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as "ask at order entry questions (AOEs)".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements.)
     */
    protected List<Resource> supportingInfoTarget;


    /**
     * One or more specimens that the laboratory procedure will use.
     */
    @Child(name = "specimen", type = {Specimen.class}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Procedure Samples", formalDefinition="One or more specimens that the laboratory procedure will use." )
    protected List<Reference> specimen;
    /**
     * The actual objects that are the target of the reference (One or more specimens that the laboratory procedure will use.)
     */
    protected List<Specimen> specimenTarget;


    /**
     * Anatomic location where the procedure should be performed. This is the target site.
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Location on Body", formalDefinition="Anatomic location where the procedure should be performed. This is the target site." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected List<CodeableConcept> bodySite;

    /**
     * Any other notes and comments made about the service request. For example, letting provider know that "patient hates needles" or other provider instructions.
     */
    @Child(name = "note", type = {Annotation.class}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments", formalDefinition="Any other notes and comments made about the service request. For example, letting provider know that \"patient hates needles\" or other provider instructions." )
    protected List<Annotation> note;

    /**
     * Key events in the history of the request.
     */
    @Child(name = "relevantHistory", type = {Provenance.class}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Request provenance", formalDefinition="Key events in the history of the request." )
    protected List<Reference> relevantHistory;
    /**
     * The actual objects that are the target of the reference (Key events in the history of the request.)
     */
    protected List<Provenance> relevantHistoryTarget;


    private static final long serialVersionUID = 184396216L;

  /**
   * Constructor
   */
    public ProcedureRequest() {
      super();
    }

  /**
   * Constructor
   */
    public ProcedureRequest(Enumeration<ProcedureRequestStatus> status, Enumeration<ProcedureRequestIntent> intent, CodeableConcept code, Reference subject) {
      super();
      this.status = status;
      this.intent = intent;
      this.code = code;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this order instance by the orderer and/or the receiver and/or order fulfiller.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setIdentifier(List<Identifier> theIdentifier) { 
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

    public ProcedureRequest addIdentifier(Identifier t) { //3
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
     * @return {@link #definition} (Protocol or definition followed by this request.)
     */
    public List<Reference> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setDefinition(List<Reference> theDefinition) { 
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

    public ProcedureRequest addDefinition(Reference t) { //3
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
     * @return {@link #basedOn} (Plan/proposal/order fulfilled by this request.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setBasedOn(List<Reference> theBasedOn) { 
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

    public ProcedureRequest addBasedOn(Reference t) { //3
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
     * @return {@link #replaces} (The request takes the place of the referenced completed or terminated request(s).)
     */
    public List<Reference> getReplaces() { 
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      return this.replaces;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setReplaces(List<Reference> theReplaces) { 
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

    public ProcedureRequest addReplaces(Reference t) { //3
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
    public List<Resource> getReplacesTarget() { 
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<Resource>();
      return this.replacesTarget;
    }

    /**
     * @return {@link #requisition} (A shared identifier common to all procedure or diagnostic requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier.)
     */
    public Identifier getRequisition() { 
      if (this.requisition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.requisition");
        else if (Configuration.doAutoCreate())
          this.requisition = new Identifier(); // cc
      return this.requisition;
    }

    public boolean hasRequisition() { 
      return this.requisition != null && !this.requisition.isEmpty();
    }

    /**
     * @param value {@link #requisition} (A shared identifier common to all procedure or diagnostic requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier.)
     */
    public ProcedureRequest setRequisition(Identifier value) { 
      this.requisition = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ProcedureRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ProcedureRequestStatus>(new ProcedureRequestStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ProcedureRequest setStatusElement(Enumeration<ProcedureRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the order.
     */
    public ProcedureRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the order.
     */
    public ProcedureRequest setStatus(ProcedureRequestStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ProcedureRequestStatus>(new ProcedureRequestStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #intent} (Whether the request is a proposal, plan, an original order or a reflex order.). This is the underlying object with id, value and extensions. The accessor "getIntent" gives direct access to the value
     */
    public Enumeration<ProcedureRequestIntent> getIntentElement() { 
      if (this.intent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.intent");
        else if (Configuration.doAutoCreate())
          this.intent = new Enumeration<ProcedureRequestIntent>(new ProcedureRequestIntentEnumFactory()); // bb
      return this.intent;
    }

    public boolean hasIntentElement() { 
      return this.intent != null && !this.intent.isEmpty();
    }

    public boolean hasIntent() { 
      return this.intent != null && !this.intent.isEmpty();
    }

    /**
     * @param value {@link #intent} (Whether the request is a proposal, plan, an original order or a reflex order.). This is the underlying object with id, value and extensions. The accessor "getIntent" gives direct access to the value
     */
    public ProcedureRequest setIntentElement(Enumeration<ProcedureRequestIntent> value) { 
      this.intent = value;
      return this;
    }

    /**
     * @return Whether the request is a proposal, plan, an original order or a reflex order.
     */
    public ProcedureRequestIntent getIntent() { 
      return this.intent == null ? null : this.intent.getValue();
    }

    /**
     * @param value Whether the request is a proposal, plan, an original order or a reflex order.
     */
    public ProcedureRequest setIntent(ProcedureRequestIntent value) { 
        if (this.intent == null)
          this.intent = new Enumeration<ProcedureRequestIntent>(new ProcedureRequestIntentEnumFactory());
        this.intent.setValue(value);
      return this;
    }

    /**
     * @return {@link #priority} (Indicates how quickly the ProcedureRequest should be addressed with respect to other requests.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Enumeration<ProcedureRequestPriority> getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Enumeration<ProcedureRequestPriority>(new ProcedureRequestPriorityEnumFactory()); // bb
      return this.priority;
    }

    public boolean hasPriorityElement() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Indicates how quickly the ProcedureRequest should be addressed with respect to other requests.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public ProcedureRequest setPriorityElement(Enumeration<ProcedureRequestPriority> value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return Indicates how quickly the ProcedureRequest should be addressed with respect to other requests.
     */
    public ProcedureRequestPriority getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value Indicates how quickly the ProcedureRequest should be addressed with respect to other requests.
     */
    public ProcedureRequest setPriority(ProcedureRequestPriority value) { 
      if (value == null)
        this.priority = null;
      else {
        if (this.priority == null)
          this.priority = new Enumeration<ProcedureRequestPriority>(new ProcedureRequestPriorityEnumFactory());
        this.priority.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #doNotPerform} (Set this to true if the record is saying that the procedure should NOT be performed.). This is the underlying object with id, value and extensions. The accessor "getDoNotPerform" gives direct access to the value
     */
    public BooleanType getDoNotPerformElement() { 
      if (this.doNotPerform == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.doNotPerform");
        else if (Configuration.doAutoCreate())
          this.doNotPerform = new BooleanType(); // bb
      return this.doNotPerform;
    }

    public boolean hasDoNotPerformElement() { 
      return this.doNotPerform != null && !this.doNotPerform.isEmpty();
    }

    public boolean hasDoNotPerform() { 
      return this.doNotPerform != null && !this.doNotPerform.isEmpty();
    }

    /**
     * @param value {@link #doNotPerform} (Set this to true if the record is saying that the procedure should NOT be performed.). This is the underlying object with id, value and extensions. The accessor "getDoNotPerform" gives direct access to the value
     */
    public ProcedureRequest setDoNotPerformElement(BooleanType value) { 
      this.doNotPerform = value;
      return this;
    }

    /**
     * @return Set this to true if the record is saying that the procedure should NOT be performed.
     */
    public boolean getDoNotPerform() { 
      return this.doNotPerform == null || this.doNotPerform.isEmpty() ? false : this.doNotPerform.getValue();
    }

    /**
     * @param value Set this to true if the record is saying that the procedure should NOT be performed.
     */
    public ProcedureRequest setDoNotPerform(boolean value) { 
        if (this.doNotPerform == null)
          this.doNotPerform = new BooleanType();
        this.doNotPerform.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (A code that classifies the procedure for searching, sorting and display purposes (e.g. "Surgical Procedure").)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setCategory(List<CodeableConcept> theCategory) { 
      this.category = theCategory;
      return this;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    public ProcedureRequest addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
     */
    public CodeableConcept getCategoryFirstRep() { 
      if (getCategory().isEmpty()) {
        addCategory();
      }
      return getCategory().get(0);
    }

    /**
     * @return {@link #code} (A code that identifies a particular procedure, diagnostic investigation, or panel of investigations, that have been requested.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A code that identifies a particular procedure, diagnostic investigation, or panel of investigations, that have been requested.)
     */
    public ProcedureRequest setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #subject} (On whom or what the procedure or diagnostic is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (On whom or what the procedure or diagnostic is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public ProcedureRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (On whom or what the procedure or diagnostic is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (On whom or what the procedure or diagnostic is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public ProcedureRequest setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    public ProcedureRequest setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    public ProcedureRequest setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
     */
    public Type getOccurrence() { 
      return this.occurrence;
    }

    /**
     * @return {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
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
     * @return {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
     */
    public Period getOccurrencePeriod() throws FHIRException { 
      if (!(this.occurrence instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Period) this.occurrence;
    }

    public boolean hasOccurrencePeriod() { 
      return this.occurrence instanceof Period;
    }

    /**
     * @return {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
     */
    public Timing getOccurrenceTiming() throws FHIRException { 
      if (!(this.occurrence instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Timing) this.occurrence;
    }

    public boolean hasOccurrenceTiming() { 
      return this.occurrence instanceof Timing;
    }

    public boolean hasOccurrence() { 
      return this.occurrence != null && !this.occurrence.isEmpty();
    }

    /**
     * @param value {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
     */
    public ProcedureRequest setOccurrence(Type value) { 
      this.occurrence = value;
      return this;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.  For example "pain", "on flare-up", etc.)
     */
    public Type getAsNeeded() { 
      return this.asNeeded;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.  For example "pain", "on flare-up", etc.)
     */
    public BooleanType getAsNeededBooleanType() throws FHIRException { 
      if (!(this.asNeeded instanceof BooleanType))
        throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (BooleanType) this.asNeeded;
    }

    public boolean hasAsNeededBooleanType() { 
      return this.asNeeded instanceof BooleanType;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.  For example "pain", "on flare-up", etc.)
     */
    public CodeableConcept getAsNeededCodeableConcept() throws FHIRException { 
      if (!(this.asNeeded instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (CodeableConcept) this.asNeeded;
    }

    public boolean hasAsNeededCodeableConcept() { 
      return this.asNeeded instanceof CodeableConcept;
    }

    public boolean hasAsNeeded() { 
      return this.asNeeded != null && !this.asNeeded.isEmpty();
    }

    /**
     * @param value {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.  For example "pain", "on flare-up", etc.)
     */
    public ProcedureRequest setAsNeeded(Type value) { 
      this.asNeeded = value;
      return this;
    }

    /**
     * @return {@link #authoredOn} (When the request transitioned to being actionable.). This is the underlying object with id, value and extensions. The accessor "getAuthoredOn" gives direct access to the value
     */
    public DateTimeType getAuthoredOnElement() { 
      if (this.authoredOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.authoredOn");
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
     * @param value {@link #authoredOn} (When the request transitioned to being actionable.). This is the underlying object with id, value and extensions. The accessor "getAuthoredOn" gives direct access to the value
     */
    public ProcedureRequest setAuthoredOnElement(DateTimeType value) { 
      this.authoredOn = value;
      return this;
    }

    /**
     * @return When the request transitioned to being actionable.
     */
    public Date getAuthoredOn() { 
      return this.authoredOn == null ? null : this.authoredOn.getValue();
    }

    /**
     * @param value When the request transitioned to being actionable.
     */
    public ProcedureRequest setAuthoredOn(Date value) { 
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
    public ProcedureRequestRequesterComponent getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new ProcedureRequestRequesterComponent(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (The individual who initiated the request and has responsibility for its activation.)
     */
    public ProcedureRequest setRequester(ProcedureRequestRequesterComponent value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #performerType} (Desired type of performer for doing the diagnostic testing.)
     */
    public CodeableConcept getPerformerType() { 
      if (this.performerType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.performerType");
        else if (Configuration.doAutoCreate())
          this.performerType = new CodeableConcept(); // cc
      return this.performerType;
    }

    public boolean hasPerformerType() { 
      return this.performerType != null && !this.performerType.isEmpty();
    }

    /**
     * @param value {@link #performerType} (Desired type of performer for doing the diagnostic testing.)
     */
    public ProcedureRequest setPerformerType(CodeableConcept value) { 
      this.performerType = value;
      return this;
    }

    /**
     * @return {@link #performer} (The desired perfomer for doing the diagnostic testing.  For example, the surgeon, dermatopathologist, endoscopist, etc.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (The desired perfomer for doing the diagnostic testing.  For example, the surgeon, dermatopathologist, endoscopist, etc.)
     */
    public ProcedureRequest setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The desired perfomer for doing the diagnostic testing.  For example, the surgeon, dermatopathologist, endoscopist, etc.)
     */
    public Resource getPerformerTarget() { 
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The desired perfomer for doing the diagnostic testing.  For example, the surgeon, dermatopathologist, endoscopist, etc.)
     */
    public ProcedureRequest setPerformerTarget(Resource value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #reasonCode} (An explanation or justification for why this diagnostic investigation is being requested in coded or textual form.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setReasonCode(List<CodeableConcept> theReasonCode) { 
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

    public ProcedureRequest addReasonCode(CodeableConcept t) { //3
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
     * @return {@link #reasonReference} (Indicates another resource that provides a justification for why this diagnostic investigation is being requested.   May relate to the resources referred to in supportingInformation.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setReasonReference(List<Reference> theReasonReference) { 
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

    public ProcedureRequest addReasonReference(Reference t) { //3
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
     * @return {@link #supportingInfo} (Additional clinical information about the patient or specimen that may influence the procedure or diagnostics or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as "ask at order entry questions (AOEs)".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements.)
     */
    public List<Reference> getSupportingInfo() { 
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      return this.supportingInfo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setSupportingInfo(List<Reference> theSupportingInfo) { 
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

    public ProcedureRequest addSupportingInfo(Reference t) { //3
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
     * @return {@link #specimen} (One or more specimens that the laboratory procedure will use.)
     */
    public List<Reference> getSpecimen() { 
      if (this.specimen == null)
        this.specimen = new ArrayList<Reference>();
      return this.specimen;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setSpecimen(List<Reference> theSpecimen) { 
      this.specimen = theSpecimen;
      return this;
    }

    public boolean hasSpecimen() { 
      if (this.specimen == null)
        return false;
      for (Reference item : this.specimen)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSpecimen() { //3
      Reference t = new Reference();
      if (this.specimen == null)
        this.specimen = new ArrayList<Reference>();
      this.specimen.add(t);
      return t;
    }

    public ProcedureRequest addSpecimen(Reference t) { //3
      if (t == null)
        return this;
      if (this.specimen == null)
        this.specimen = new ArrayList<Reference>();
      this.specimen.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #specimen}, creating it if it does not already exist
     */
    public Reference getSpecimenFirstRep() { 
      if (getSpecimen().isEmpty()) {
        addSpecimen();
      }
      return getSpecimen().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Specimen> getSpecimenTarget() { 
      if (this.specimenTarget == null)
        this.specimenTarget = new ArrayList<Specimen>();
      return this.specimenTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Specimen addSpecimenTarget() { 
      Specimen r = new Specimen();
      if (this.specimenTarget == null)
        this.specimenTarget = new ArrayList<Specimen>();
      this.specimenTarget.add(r);
      return r;
    }

    /**
     * @return {@link #bodySite} (Anatomic location where the procedure should be performed. This is the target site.)
     */
    public List<CodeableConcept> getBodySite() { 
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      return this.bodySite;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setBodySite(List<CodeableConcept> theBodySite) { 
      this.bodySite = theBodySite;
      return this;
    }

    public boolean hasBodySite() { 
      if (this.bodySite == null)
        return false;
      for (CodeableConcept item : this.bodySite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addBodySite() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return t;
    }

    public ProcedureRequest addBodySite(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #bodySite}, creating it if it does not already exist
     */
    public CodeableConcept getBodySiteFirstRep() { 
      if (getBodySite().isEmpty()) {
        addBodySite();
      }
      return getBodySite().get(0);
    }

    /**
     * @return {@link #note} (Any other notes and comments made about the service request. For example, letting provider know that "patient hates needles" or other provider instructions.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setNote(List<Annotation> theNote) { 
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

    public ProcedureRequest addNote(Annotation t) { //3
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
     * @return {@link #relevantHistory} (Key events in the history of the request.)
     */
    public List<Reference> getRelevantHistory() { 
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      return this.relevantHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcedureRequest setRelevantHistory(List<Reference> theRelevantHistory) { 
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

    public ProcedureRequest addRelevantHistory(Reference t) { //3
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
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order instance by the orderer and/or the receiver and/or order fulfiller.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("definition", "Reference(ActivityDefinition|PlanDefinition)", "Protocol or definition followed by this request.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("basedOn", "Reference(Any)", "Plan/proposal/order fulfilled by this request.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("replaces", "Reference(Any)", "The request takes the place of the referenced completed or terminated request(s).", 0, java.lang.Integer.MAX_VALUE, replaces));
        childrenList.add(new Property("requisition", "Identifier", "A shared identifier common to all procedure or diagnostic requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier.", 0, java.lang.Integer.MAX_VALUE, requisition));
        childrenList.add(new Property("status", "code", "The status of the order.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("intent", "code", "Whether the request is a proposal, plan, an original order or a reflex order.", 0, java.lang.Integer.MAX_VALUE, intent));
        childrenList.add(new Property("priority", "code", "Indicates how quickly the ProcedureRequest should be addressed with respect to other requests.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("doNotPerform", "boolean", "Set this to true if the record is saying that the procedure should NOT be performed.", 0, java.lang.Integer.MAX_VALUE, doNotPerform));
        childrenList.add(new Property("category", "CodeableConcept", "A code that classifies the procedure for searching, sorting and display purposes (e.g. \"Surgical Procedure\").", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("code", "CodeableConcept", "A code that identifies a particular procedure, diagnostic investigation, or panel of investigations, that have been requested.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Location|Device)", "On whom or what the procedure or diagnostic is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "An encounter or episode of care that provides additional information about the healthcare context in which this request is made.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("occurrence[x]", "dateTime|Period|Timing", "The date/time at which the diagnostic testing should occur.", 0, java.lang.Integer.MAX_VALUE, occurrence));
        childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.  For example \"pain\", \"on flare-up\", etc.", 0, java.lang.Integer.MAX_VALUE, asNeeded));
        childrenList.add(new Property("authoredOn", "dateTime", "When the request transitioned to being actionable.", 0, java.lang.Integer.MAX_VALUE, authoredOn));
        childrenList.add(new Property("requester", "", "The individual who initiated the request and has responsibility for its activation.", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("performerType", "CodeableConcept", "Desired type of performer for doing the diagnostic testing.", 0, java.lang.Integer.MAX_VALUE, performerType));
        childrenList.add(new Property("performer", "Reference(Practitioner|Organization|Patient|Device|RelatedPerson|HealthcareService)", "The desired perfomer for doing the diagnostic testing.  For example, the surgeon, dermatopathologist, endoscopist, etc.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "An explanation or justification for why this diagnostic investigation is being requested in coded or textual form.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("reasonReference", "Reference(Condition|Observation)", "Indicates another resource that provides a justification for why this diagnostic investigation is being requested.   May relate to the resources referred to in supportingInformation.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("supportingInfo", "Reference(Any)", "Additional clinical information about the patient or specimen that may influence the procedure or diagnostics or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as \"ask at order entry questions (AOEs)\".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements.", 0, java.lang.Integer.MAX_VALUE, supportingInfo));
        childrenList.add(new Property("specimen", "Reference(Specimen)", "One or more specimens that the laboratory procedure will use.", 0, java.lang.Integer.MAX_VALUE, specimen));
        childrenList.add(new Property("bodySite", "CodeableConcept", "Anatomic location where the procedure should be performed. This is the target site.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("note", "Annotation", "Any other notes and comments made about the service request. For example, letting provider know that \"patient hates needles\" or other provider instructions.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("relevantHistory", "Reference(Provenance)", "Key events in the history of the request.", 0, java.lang.Integer.MAX_VALUE, relevantHistory));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : this.definition.toArray(new Base[this.definition.size()]); // Reference
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -430332865: /*replaces*/ return this.replaces == null ? new Base[0] : this.replaces.toArray(new Base[this.replaces.size()]); // Reference
        case 395923612: /*requisition*/ return this.requisition == null ? new Base[0] : new Base[] {this.requisition}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ProcedureRequestStatus>
        case -1183762788: /*intent*/ return this.intent == null ? new Base[0] : new Base[] {this.intent}; // Enumeration<ProcedureRequestIntent>
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Enumeration<ProcedureRequestPriority>
        case -1788508167: /*doNotPerform*/ return this.doNotPerform == null ? new Base[0] : new Base[] {this.doNotPerform}; // BooleanType
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case -1432923513: /*asNeeded*/ return this.asNeeded == null ? new Base[0] : new Base[] {this.asNeeded}; // Type
        case -1500852503: /*authoredOn*/ return this.authoredOn == null ? new Base[0] : new Base[] {this.authoredOn}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // ProcedureRequestRequesterComponent
        case -901444568: /*performerType*/ return this.performerType == null ? new Base[0] : new Base[] {this.performerType}; // CodeableConcept
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 1922406657: /*supportingInfo*/ return this.supportingInfo == null ? new Base[0] : this.supportingInfo.toArray(new Base[this.supportingInfo.size()]); // Reference
        case -2132868344: /*specimen*/ return this.specimen == null ? new Base[0] : this.specimen.toArray(new Base[this.specimen.size()]); // Reference
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : this.bodySite.toArray(new Base[this.bodySite.size()]); // CodeableConcept
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
        case 395923612: // requisition
          this.requisition = castToIdentifier(value); // Identifier
          return value;
        case -892481550: // status
          value = new ProcedureRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ProcedureRequestStatus>
          return value;
        case -1183762788: // intent
          value = new ProcedureRequestIntentEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<ProcedureRequestIntent>
          return value;
        case -1165461084: // priority
          value = new ProcedureRequestPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<ProcedureRequestPriority>
          return value;
        case -1788508167: // doNotPerform
          this.doNotPerform = castToBoolean(value); // BooleanType
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
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
        case -1432923513: // asNeeded
          this.asNeeded = castToType(value); // Type
          return value;
        case -1500852503: // authoredOn
          this.authoredOn = castToDateTime(value); // DateTimeType
          return value;
        case 693933948: // requester
          this.requester = (ProcedureRequestRequesterComponent) value; // ProcedureRequestRequesterComponent
          return value;
        case -901444568: // performerType
          this.performerType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 481140686: // performer
          this.performer = castToReference(value); // Reference
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case 1922406657: // supportingInfo
          this.getSupportingInfo().add(castToReference(value)); // Reference
          return value;
        case -2132868344: // specimen
          this.getSpecimen().add(castToReference(value)); // Reference
          return value;
        case 1702620169: // bodySite
          this.getBodySite().add(castToCodeableConcept(value)); // CodeableConcept
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
        } else if (name.equals("requisition")) {
          this.requisition = castToIdentifier(value); // Identifier
        } else if (name.equals("status")) {
          value = new ProcedureRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ProcedureRequestStatus>
        } else if (name.equals("intent")) {
          value = new ProcedureRequestIntentEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<ProcedureRequestIntent>
        } else if (name.equals("priority")) {
          value = new ProcedureRequestPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<ProcedureRequestPriority>
        } else if (name.equals("doNotPerform")) {
          this.doNotPerform = castToBoolean(value); // BooleanType
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("occurrence[x]")) {
          this.occurrence = castToType(value); // Type
        } else if (name.equals("asNeeded[x]")) {
          this.asNeeded = castToType(value); // Type
        } else if (name.equals("authoredOn")) {
          this.authoredOn = castToDateTime(value); // DateTimeType
        } else if (name.equals("requester")) {
          this.requester = (ProcedureRequestRequesterComponent) value; // ProcedureRequestRequesterComponent
        } else if (name.equals("performerType")) {
          this.performerType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("performer")) {
          this.performer = castToReference(value); // Reference
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("supportingInfo")) {
          this.getSupportingInfo().add(castToReference(value));
        } else if (name.equals("specimen")) {
          this.getSpecimen().add(castToReference(value));
        } else if (name.equals("bodySite")) {
          this.getBodySite().add(castToCodeableConcept(value));
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
        case 395923612:  return getRequisition(); 
        case -892481550:  return getStatusElement();
        case -1183762788:  return getIntentElement();
        case -1165461084:  return getPriorityElement();
        case -1788508167:  return getDoNotPerformElement();
        case 50511102:  return addCategory(); 
        case 3059181:  return getCode(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -2022646513:  return getOccurrence(); 
        case 1687874001:  return getOccurrence(); 
        case -544329575:  return getAsNeeded(); 
        case -1432923513:  return getAsNeeded(); 
        case -1500852503:  return getAuthoredOnElement();
        case 693933948:  return getRequester(); 
        case -901444568:  return getPerformerType(); 
        case 481140686:  return getPerformer(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case 1922406657:  return addSupportingInfo(); 
        case -2132868344:  return addSpecimen(); 
        case 1702620169:  return addBodySite(); 
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
        case 395923612: /*requisition*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1183762788: /*intent*/ return new String[] {"code"};
        case -1165461084: /*priority*/ return new String[] {"code"};
        case -1788508167: /*doNotPerform*/ return new String[] {"boolean"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case 1687874001: /*occurrence*/ return new String[] {"dateTime", "Period", "Timing"};
        case -1432923513: /*asNeeded*/ return new String[] {"boolean", "CodeableConcept"};
        case -1500852503: /*authoredOn*/ return new String[] {"dateTime"};
        case 693933948: /*requester*/ return new String[] {};
        case -901444568: /*performerType*/ return new String[] {"CodeableConcept"};
        case 481140686: /*performer*/ return new String[] {"Reference"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case 1922406657: /*supportingInfo*/ return new String[] {"Reference"};
        case -2132868344: /*specimen*/ return new String[] {"Reference"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
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
        else if (name.equals("requisition")) {
          this.requisition = new Identifier();
          return this.requisition;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcedureRequest.status");
        }
        else if (name.equals("intent")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcedureRequest.intent");
        }
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcedureRequest.priority");
        }
        else if (name.equals("doNotPerform")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcedureRequest.doNotPerform");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
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
        else if (name.equals("occurrenceTiming")) {
          this.occurrence = new Timing();
          return this.occurrence;
        }
        else if (name.equals("asNeededBoolean")) {
          this.asNeeded = new BooleanType();
          return this.asNeeded;
        }
        else if (name.equals("asNeededCodeableConcept")) {
          this.asNeeded = new CodeableConcept();
          return this.asNeeded;
        }
        else if (name.equals("authoredOn")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcedureRequest.authoredOn");
        }
        else if (name.equals("requester")) {
          this.requester = new ProcedureRequestRequesterComponent();
          return this.requester;
        }
        else if (name.equals("performerType")) {
          this.performerType = new CodeableConcept();
          return this.performerType;
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("supportingInfo")) {
          return addSupportingInfo();
        }
        else if (name.equals("specimen")) {
          return addSpecimen();
        }
        else if (name.equals("bodySite")) {
          return addBodySite();
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
    return "ProcedureRequest";

  }

      public ProcedureRequest copy() {
        ProcedureRequest dst = new ProcedureRequest();
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
        dst.requisition = requisition == null ? null : requisition.copy();
        dst.status = status == null ? null : status.copy();
        dst.intent = intent == null ? null : intent.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.doNotPerform = doNotPerform == null ? null : doNotPerform.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        dst.asNeeded = asNeeded == null ? null : asNeeded.copy();
        dst.authoredOn = authoredOn == null ? null : authoredOn.copy();
        dst.requester = requester == null ? null : requester.copy();
        dst.performerType = performerType == null ? null : performerType.copy();
        dst.performer = performer == null ? null : performer.copy();
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
        if (supportingInfo != null) {
          dst.supportingInfo = new ArrayList<Reference>();
          for (Reference i : supportingInfo)
            dst.supportingInfo.add(i.copy());
        };
        if (specimen != null) {
          dst.specimen = new ArrayList<Reference>();
          for (Reference i : specimen)
            dst.specimen.add(i.copy());
        };
        if (bodySite != null) {
          dst.bodySite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : bodySite)
            dst.bodySite.add(i.copy());
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

      protected ProcedureRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedureRequest))
          return false;
        ProcedureRequest o = (ProcedureRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(definition, o.definition, true)
           && compareDeep(basedOn, o.basedOn, true) && compareDeep(replaces, o.replaces, true) && compareDeep(requisition, o.requisition, true)
           && compareDeep(status, o.status, true) && compareDeep(intent, o.intent, true) && compareDeep(priority, o.priority, true)
           && compareDeep(doNotPerform, o.doNotPerform, true) && compareDeep(category, o.category, true) && compareDeep(code, o.code, true)
           && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true) && compareDeep(occurrence, o.occurrence, true)
           && compareDeep(asNeeded, o.asNeeded, true) && compareDeep(authoredOn, o.authoredOn, true) && compareDeep(requester, o.requester, true)
           && compareDeep(performerType, o.performerType, true) && compareDeep(performer, o.performer, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(supportingInfo, o.supportingInfo, true) && compareDeep(specimen, o.specimen, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(note, o.note, true) && compareDeep(relevantHistory, o.relevantHistory, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedureRequest))
          return false;
        ProcedureRequest o = (ProcedureRequest) other;
        return compareValues(status, o.status, true) && compareValues(intent, o.intent, true) && compareValues(priority, o.priority, true)
           && compareValues(doNotPerform, o.doNotPerform, true) && compareValues(authoredOn, o.authoredOn, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, definition, basedOn
          , replaces, requisition, status, intent, priority, doNotPerform, category, code
          , subject, context, occurrence, asNeeded, authoredOn, requester, performerType
          , performer, reasonCode, reasonReference, supportingInfo, specimen, bodySite, note
          , relevantHistory);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ProcedureRequest;
   }

 /**
   * Search parameter: <b>authored</b>
   * <p>
   * Description: <b>Date request signed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ProcedureRequest.authoredOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authored", path="ProcedureRequest.authoredOn", description="Date request signed", type="date" )
  public static final String SP_AUTHORED = "authored";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authored</b>
   * <p>
   * Description: <b>Date request signed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ProcedureRequest.authoredOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHORED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHORED);

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>Individual making the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.requester.agent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="ProcedureRequest.requester.agent", description="Individual making the request", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Organization.class, Practitioner.class } )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>Individual making the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.requester.agent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("ProcedureRequest:requester").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Identifiers assigned to this order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ProcedureRequest.identifier", description="Identifiers assigned to this order", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Identifiers assigned to this order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>What is being requested/ordered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="ProcedureRequest.code", description="What is being requested/ordered", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>What is being requested/ordered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Requested perfomer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="ProcedureRequest.performer", description="Requested perfomer", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, HealthcareService.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>Requested perfomer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("ProcedureRequest:performer").toLocked();

 /**
   * Search parameter: <b>requisition</b>
   * <p>
   * Description: <b>Composite Request ID</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.requisition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requisition", path="ProcedureRequest.requisition", description="Composite Request ID", type="token" )
  public static final String SP_REQUISITION = "requisition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requisition</b>
   * <p>
   * Description: <b>Composite Request ID</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.requisition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUISITION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUISITION);

 /**
   * Search parameter: <b>replaces</b>
   * <p>
   * Description: <b>What request replaces</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.replaces</b><br>
   * </p>
   */
  @SearchParamDefinition(name="replaces", path="ProcedureRequest.replaces", description="What request replaces", type="reference" )
  public static final String SP_REPLACES = "replaces";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>replaces</b>
   * <p>
   * Description: <b>What request replaces</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.replaces</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REPLACES = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REPLACES);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:replaces</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REPLACES = new ca.uhn.fhir.model.api.Include("ProcedureRequest:replaces").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="ProcedureRequest.subject", description="Search by subject", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Device.class, Group.class, Location.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("ProcedureRequest:subject").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>An encounter in which this request is made</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="ProcedureRequest.context", description="An encounter in which this request is made", type="reference", target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>An encounter in which this request is made</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("ProcedureRequest:encounter").toLocked();

 /**
   * Search parameter: <b>occurrence</b>
   * <p>
   * Description: <b>When procedure should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ProcedureRequest.occurrence[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="occurrence", path="ProcedureRequest.occurrence", description="When procedure should occur", type="date" )
  public static final String SP_OCCURRENCE = "occurrence";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>occurrence</b>
   * <p>
   * Description: <b>When procedure should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ProcedureRequest.occurrence[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam OCCURRENCE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_OCCURRENCE);

 /**
   * Search parameter: <b>priority</b>
   * <p>
   * Description: <b>routine | urgent | asap | stat</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.priority</b><br>
   * </p>
   */
  @SearchParamDefinition(name="priority", path="ProcedureRequest.priority", description="routine | urgent | asap | stat", type="token" )
  public static final String SP_PRIORITY = "priority";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>priority</b>
   * <p>
   * Description: <b>routine | urgent | asap | stat</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.priority</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PRIORITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PRIORITY);

 /**
   * Search parameter: <b>intent</b>
   * <p>
   * Description: <b>proposal | plan | order +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.intent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="intent", path="ProcedureRequest.intent", description="proposal | plan | order +", type="token" )
  public static final String SP_INTENT = "intent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>intent</b>
   * <p>
   * Description: <b>proposal | plan | order +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.intent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam INTENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_INTENT);

 /**
   * Search parameter: <b>performer-type</b>
   * <p>
   * Description: <b>Performer role</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.performerType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer-type", path="ProcedureRequest.performerType", description="Performer role", type="token" )
  public static final String SP_PERFORMER_TYPE = "performer-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer-type</b>
   * <p>
   * Description: <b>Performer role</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.performerType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PERFORMER_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PERFORMER_TYPE);

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>What request fulfills</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="ProcedureRequest.basedOn", description="What request fulfills", type="reference" )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>What request fulfills</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("ProcedureRequest:based-on").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ProcedureRequest.subject", description="Search by subject - a patient", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ProcedureRequest:patient").toLocked();

 /**
   * Search parameter: <b>specimen</b>
   * <p>
   * Description: <b>Specimen to be tested</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.specimen</b><br>
   * </p>
   */
  @SearchParamDefinition(name="specimen", path="ProcedureRequest.specimen", description="Specimen to be tested", type="reference", target={Specimen.class } )
  public static final String SP_SPECIMEN = "specimen";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
   * <p>
   * Description: <b>Specimen to be tested</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.specimen</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SPECIMEN = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SPECIMEN);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:specimen</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SPECIMEN = new ca.uhn.fhir.model.api.Include("ProcedureRequest:specimen").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Encounter or Episode during which request was created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="ProcedureRequest.context", description="Encounter or Episode during which request was created", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Encounter or Episode during which request was created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("ProcedureRequest:context").toLocked();

 /**
   * Search parameter: <b>definition</b>
   * <p>
   * Description: <b>Protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="definition", path="ProcedureRequest.definition", description="Protocol or definition", type="reference", target={ActivityDefinition.class, PlanDefinition.class } )
  public static final String SP_DEFINITION = "definition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>definition</b>
   * <p>
   * Description: <b>Protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEFINITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEFINITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:definition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEFINITION = new ca.uhn.fhir.model.api.Include("ProcedureRequest:definition").toLocked();

 /**
   * Search parameter: <b>body-site</b>
   * <p>
   * Description: <b>Where procedure is going to be done</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.bodySite</b><br>
   * </p>
   */
  @SearchParamDefinition(name="body-site", path="ProcedureRequest.bodySite", description="Where procedure is going to be done", type="token" )
  public static final String SP_BODY_SITE = "body-site";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>body-site</b>
   * <p>
   * Description: <b>Where procedure is going to be done</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.bodySite</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam BODY_SITE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_BODY_SITE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | active | suspended | completed | entered-in-error | cancelled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ProcedureRequest.status", description="draft | active | suspended | completed | entered-in-error | cancelled", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | active | suspended | completed | entered-in-error | cancelled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

