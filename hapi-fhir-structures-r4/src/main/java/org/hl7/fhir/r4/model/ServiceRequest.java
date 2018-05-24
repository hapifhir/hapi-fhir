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
 * A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
 */
@ResourceDef(name="ServiceRequest", profile="http://hl7.org/fhir/Profile/ServiceRequest")
public class ServiceRequest extends DomainResource {

    public enum ServiceRequestStatus {
        /**
         * The request has been created but is not yet complete or ready for action
         */
        DRAFT, 
        /**
         * The request is in force and ready to be acted upon
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
         * The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for "other" . One of the listed statuses is presumed to apply,  but the system creating the request does not know.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ServiceRequestStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown ServiceRequestStatus code '"+codeString+"'");
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
            case ACTIVE: return "The request is in force and ready to be acted upon";
            case SUSPENDED: return "The authorization/request to act has been temporarily withdrawn but is expected to resume in the future";
            case CANCELLED: return "The authorization/request to act has been terminated prior to the full completion of the intended actions.  No further activity should occur.";
            case COMPLETED: return "Activity against the request has been sufficiently completed to the satisfaction of the requester";
            case ENTEREDINERROR: return "This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".)";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for \"other\" . One of the listed statuses is presumed to apply,  but the system creating the request does not know.";
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

  public static class ServiceRequestStatusEnumFactory implements EnumFactory<ServiceRequestStatus> {
    public ServiceRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ServiceRequestStatus.DRAFT;
        if ("active".equals(codeString))
          return ServiceRequestStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return ServiceRequestStatus.SUSPENDED;
        if ("cancelled".equals(codeString))
          return ServiceRequestStatus.CANCELLED;
        if ("completed".equals(codeString))
          return ServiceRequestStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ServiceRequestStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return ServiceRequestStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown ServiceRequestStatus code '"+codeString+"'");
        }
        public Enumeration<ServiceRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ServiceRequestStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ServiceRequestStatus>(this, ServiceRequestStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<ServiceRequestStatus>(this, ServiceRequestStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<ServiceRequestStatus>(this, ServiceRequestStatus.SUSPENDED);
        if ("cancelled".equals(codeString))
          return new Enumeration<ServiceRequestStatus>(this, ServiceRequestStatus.CANCELLED);
        if ("completed".equals(codeString))
          return new Enumeration<ServiceRequestStatus>(this, ServiceRequestStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ServiceRequestStatus>(this, ServiceRequestStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<ServiceRequestStatus>(this, ServiceRequestStatus.UNKNOWN);
        throw new FHIRException("Unknown ServiceRequestStatus code '"+codeString+"'");
        }
    public String toCode(ServiceRequestStatus code) {
      if (code == ServiceRequestStatus.DRAFT)
        return "draft";
      if (code == ServiceRequestStatus.ACTIVE)
        return "active";
      if (code == ServiceRequestStatus.SUSPENDED)
        return "suspended";
      if (code == ServiceRequestStatus.CANCELLED)
        return "cancelled";
      if (code == ServiceRequestStatus.COMPLETED)
        return "completed";
      if (code == ServiceRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ServiceRequestStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(ServiceRequestStatus code) {
      return code.getSystem();
      }
    }

    public enum ServiceRequestIntent {
        /**
         * The request is a suggestion made by someone/something that does not have an intention to ensure it occurs and without providing an authorization to act
         */
        PROPOSAL, 
        /**
         * The request represents an intention to ensure something occurs without providing an authorization for others to act
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
         * The request represents a component or option for a RequestGroup that establishes timing, conditionality and/or other constraints among a set of requests.  Refer to [[[RequestGroup]]] for additional information on how this status is used
         */
        OPTION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ServiceRequestIntent fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown ServiceRequestIntent code '"+codeString+"'");
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
            case PROPOSAL: return "The request is a suggestion made by someone/something that does not have an intention to ensure it occurs and without providing an authorization to act";
            case PLAN: return "The request represents an intention to ensure something occurs without providing an authorization for others to act";
            case ORDER: return "The request represents a request/demand and authorization for action";
            case ORIGINALORDER: return "The request represents an original authorization for action";
            case REFLEXORDER: return "The request represents an automatically generated supplemental authorization for action based on a parent authorization together with initial results of the action taken against that parent authorization";
            case FILLERORDER: return "The request represents the view of an authorization instantiated by a fulfilling system representing the details of the fulfiller's intention to act upon a submitted order";
            case INSTANCEORDER: return "An order created in fulfillment of a broader order that represents the authorization for a single activity occurrence.  E.g. The administration of a single dose of a drug.";
            case OPTION: return "The request represents a component or option for a RequestGroup that establishes timing, conditionality and/or other constraints among a set of requests.  Refer to [[[RequestGroup]]] for additional information on how this status is used";
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

  public static class ServiceRequestIntentEnumFactory implements EnumFactory<ServiceRequestIntent> {
    public ServiceRequestIntent fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposal".equals(codeString))
          return ServiceRequestIntent.PROPOSAL;
        if ("plan".equals(codeString))
          return ServiceRequestIntent.PLAN;
        if ("order".equals(codeString))
          return ServiceRequestIntent.ORDER;
        if ("original-order".equals(codeString))
          return ServiceRequestIntent.ORIGINALORDER;
        if ("reflex-order".equals(codeString))
          return ServiceRequestIntent.REFLEXORDER;
        if ("filler-order".equals(codeString))
          return ServiceRequestIntent.FILLERORDER;
        if ("instance-order".equals(codeString))
          return ServiceRequestIntent.INSTANCEORDER;
        if ("option".equals(codeString))
          return ServiceRequestIntent.OPTION;
        throw new IllegalArgumentException("Unknown ServiceRequestIntent code '"+codeString+"'");
        }
        public Enumeration<ServiceRequestIntent> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ServiceRequestIntent>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposal".equals(codeString))
          return new Enumeration<ServiceRequestIntent>(this, ServiceRequestIntent.PROPOSAL);
        if ("plan".equals(codeString))
          return new Enumeration<ServiceRequestIntent>(this, ServiceRequestIntent.PLAN);
        if ("order".equals(codeString))
          return new Enumeration<ServiceRequestIntent>(this, ServiceRequestIntent.ORDER);
        if ("original-order".equals(codeString))
          return new Enumeration<ServiceRequestIntent>(this, ServiceRequestIntent.ORIGINALORDER);
        if ("reflex-order".equals(codeString))
          return new Enumeration<ServiceRequestIntent>(this, ServiceRequestIntent.REFLEXORDER);
        if ("filler-order".equals(codeString))
          return new Enumeration<ServiceRequestIntent>(this, ServiceRequestIntent.FILLERORDER);
        if ("instance-order".equals(codeString))
          return new Enumeration<ServiceRequestIntent>(this, ServiceRequestIntent.INSTANCEORDER);
        if ("option".equals(codeString))
          return new Enumeration<ServiceRequestIntent>(this, ServiceRequestIntent.OPTION);
        throw new FHIRException("Unknown ServiceRequestIntent code '"+codeString+"'");
        }
    public String toCode(ServiceRequestIntent code) {
      if (code == ServiceRequestIntent.PROPOSAL)
        return "proposal";
      if (code == ServiceRequestIntent.PLAN)
        return "plan";
      if (code == ServiceRequestIntent.ORDER)
        return "order";
      if (code == ServiceRequestIntent.ORIGINALORDER)
        return "original-order";
      if (code == ServiceRequestIntent.REFLEXORDER)
        return "reflex-order";
      if (code == ServiceRequestIntent.FILLERORDER)
        return "filler-order";
      if (code == ServiceRequestIntent.INSTANCEORDER)
        return "instance-order";
      if (code == ServiceRequestIntent.OPTION)
        return "option";
      return "?";
      }
    public String toSystem(ServiceRequestIntent code) {
      return code.getSystem();
      }
    }

    public enum ServiceRequestPriority {
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
        public static ServiceRequestPriority fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown ServiceRequestPriority code '"+codeString+"'");
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

  public static class ServiceRequestPriorityEnumFactory implements EnumFactory<ServiceRequestPriority> {
    public ServiceRequestPriority fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return ServiceRequestPriority.ROUTINE;
        if ("urgent".equals(codeString))
          return ServiceRequestPriority.URGENT;
        if ("asap".equals(codeString))
          return ServiceRequestPriority.ASAP;
        if ("stat".equals(codeString))
          return ServiceRequestPriority.STAT;
        throw new IllegalArgumentException("Unknown ServiceRequestPriority code '"+codeString+"'");
        }
        public Enumeration<ServiceRequestPriority> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ServiceRequestPriority>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("routine".equals(codeString))
          return new Enumeration<ServiceRequestPriority>(this, ServiceRequestPriority.ROUTINE);
        if ("urgent".equals(codeString))
          return new Enumeration<ServiceRequestPriority>(this, ServiceRequestPriority.URGENT);
        if ("asap".equals(codeString))
          return new Enumeration<ServiceRequestPriority>(this, ServiceRequestPriority.ASAP);
        if ("stat".equals(codeString))
          return new Enumeration<ServiceRequestPriority>(this, ServiceRequestPriority.STAT);
        throw new FHIRException("Unknown ServiceRequestPriority code '"+codeString+"'");
        }
    public String toCode(ServiceRequestPriority code) {
      if (code == ServiceRequestPriority.ROUTINE)
        return "routine";
      if (code == ServiceRequestPriority.URGENT)
        return "urgent";
      if (code == ServiceRequestPriority.ASAP)
        return "asap";
      if (code == ServiceRequestPriority.STAT)
        return "stat";
      return "?";
      }
    public String toSystem(ServiceRequestPriority code) {
      return code.getSystem();
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
    @Child(name = "instantiates", type = {UriType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Protocol or definition", formalDefinition="Protocol or definition followed by this request." )
    protected List<UriType> instantiates;

    /**
     * Plan/proposal/order fulfilled by this request.
     */
    @Child(name = "basedOn", type = {CarePlan.class, ServiceRequest.class, MedicationRequest.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What request fulfills", formalDefinition="Plan/proposal/order fulfilled by this request." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (Plan/proposal/order fulfilled by this request.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * The request takes the place of the referenced completed or terminated request(s).
     */
    @Child(name = "replaces", type = {ServiceRequest.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What request replaces", formalDefinition="The request takes the place of the referenced completed or terminated request(s)." )
    protected List<Reference> replaces;
    /**
     * The actual objects that are the target of the reference (The request takes the place of the referenced completed or terminated request(s).)
     */
    protected List<ServiceRequest> replacesTarget;


    /**
     * A shared identifier common to all service requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier.
     */
    @Child(name = "requisition", type = {Identifier.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Composite Request ID", formalDefinition="A shared identifier common to all service requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier." )
    protected Identifier requisition;

    /**
     * The status of the order.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | suspended | completed | entered-in-error | cancelled", formalDefinition="The status of the order." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-status")
    protected Enumeration<ServiceRequestStatus> status;

    /**
     * Whether the request is a proposal, plan, an original order or a reflex order.
     */
    @Child(name = "intent", type = {CodeType.class}, order=6, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposal | plan | order +", formalDefinition="Whether the request is a proposal, plan, an original order or a reflex order." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-intent")
    protected Enumeration<ServiceRequestIntent> intent;

    /**
     * A code that classifies the service for searching, sorting and display purposes (e.g. "Surgical Procedure").
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Classification of service", formalDefinition="A code that classifies the service for searching, sorting and display purposes (e.g. \"Surgical Procedure\")." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/servicerequest-category")
    protected List<CodeableConcept> category;

    /**
     * Indicates how quickly the ServiceRequest should be addressed with respect to other requests.
     */
    @Child(name = "priority", type = {CodeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="routine | urgent | asap | stat", formalDefinition="Indicates how quickly the ServiceRequest should be addressed with respect to other requests." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-priority")
    protected Enumeration<ServiceRequestPriority> priority;

    /**
     * Set this to true if the record is saying that the service/procedure should NOT be performed.
     */
    @Child(name = "doNotPerform", type = {BooleanType.class}, order=9, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="True if service/procedure should not be performed", formalDefinition="Set this to true if the record is saying that the service/procedure should NOT be performed." )
    protected BooleanType doNotPerform;

    /**
     * A code that identifies a particular service (i.e., procedure, diagnostic investigation, or panel of investigations) that have been requested.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What is being requested/ordered", formalDefinition="A code that identifies a particular service (i.e., procedure, diagnostic investigation, or panel of investigations) that have been requested." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-code")
    protected CodeableConcept code;

    /**
     * Additional details and instructions about the how the services are to be delivered.   For example, and order for a urinary catheter may have an order detail for an external or indwelling catheter, or an order for a bandage may require additional instructions specifying how the bandage should be applied.
     */
    @Child(name = "orderDetail", type = {CodeableConcept.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional order information", formalDefinition="Additional details and instructions about the how the services are to be delivered.   For example, and order for a urinary catheter may have an order detail for an external or indwelling catheter, or an order for a bandage may require additional instructions specifying how the bandage should be applied." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/servicerequest-orderdetail")
    protected List<CodeableConcept> orderDetail;

    /**
     * On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Location.class, Device.class}, order=12, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Individual the service is ordered for", formalDefinition="On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    protected Resource subjectTarget;

    /**
     * An encounter or episode of care that provides additional information about the healthcare context in which this request is made.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter or Episode during which request was created", formalDefinition="An encounter or episode of care that provides additional information about the healthcare context in which this request is made." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    protected Resource contextTarget;

    /**
     * The date/time at which the requested service should occur.
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class, Timing.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When service should occur", formalDefinition="The date/time at which the requested service should occur." )
    protected Type occurrence;

    /**
     * If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example "pain", "on flare-up", etc.
     */
    @Child(name = "asNeeded", type = {BooleanType.class, CodeableConcept.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Preconditions for service", formalDefinition="If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example \"pain\", \"on flare-up\", etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-as-needed-reason")
    protected Type asNeeded;

    /**
     * When the request transitioned to being actionable.
     */
    @Child(name = "authoredOn", type = {DateTimeType.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date request signed", formalDefinition="When the request transitioned to being actionable." )
    protected DateTimeType authoredOn;

    /**
     * The individual who initiated the request and has responsibility for its activation.
     */
    @Child(name = "requester", type = {Practitioner.class, PractitionerRole.class, Organization.class, Patient.class, RelatedPerson.class, Device.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/what is requesting service", formalDefinition="The individual who initiated the request and has responsibility for its activation." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (The individual who initiated the request and has responsibility for its activation.)
     */
    protected Resource requesterTarget;

    /**
     * Desired type of performer for doing the requested service.
     */
    @Child(name = "performerType", type = {CodeableConcept.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Performer role", formalDefinition="Desired type of performer for doing the requested service." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/participant-role")
    protected CodeableConcept performerType;

    /**
     * The desired performer for doing the requested service.  For example, the surgeon, dermatopathologist, endoscopist, etc.
     */
    @Child(name = "performer", type = {Practitioner.class, PractitionerRole.class, Organization.class, CareTeam.class, HealthcareService.class, Patient.class, Device.class, RelatedPerson.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Requested performer", formalDefinition="The desired performer for doing the requested service.  For example, the surgeon, dermatopathologist, endoscopist, etc." )
    protected List<Reference> performer;
    /**
     * The actual objects that are the target of the reference (The desired performer for doing the requested service.  For example, the surgeon, dermatopathologist, endoscopist, etc.)
     */
    protected List<Resource> performerTarget;


    /**
     * An explanation or justification for why this service is being requested in coded or textual form.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Explanation/Justification for procedure or service", formalDefinition="An explanation or justification for why this service is being requested in coded or textual form.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-reason")
    protected List<CodeableConcept> reasonCode;

    /**
     * Indicates another resource that provides a justification for why this service is being requested.   May relate to the resources referred to in supportingInformation.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class, DiagnosticReport.class, DocumentReference.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Explanation/Justification for service or service", formalDefinition="Indicates another resource that provides a justification for why this service is being requested.   May relate to the resources referred to in supportingInformation." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Indicates another resource that provides a justification for why this service is being requested.   May relate to the resources referred to in supportingInformation.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be needed for delivering the requested service.
     */
    @Child(name = "insurance", type = {Coverage.class, ClaimResponse.class}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Associated insurance coverage", formalDefinition="Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be needed for delivering the requested service." )
    protected List<Reference> insurance;
    /**
     * The actual objects that are the target of the reference (Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be needed for delivering the requested service.)
     */
    protected List<Resource> insuranceTarget;


    /**
     * Additional clinical information about the patient or specimen that may influence the services or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as "ask at order entry questions (AOEs)".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements.
     */
    @Child(name = "supportingInfo", type = {Reference.class}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional clinical information", formalDefinition="Additional clinical information about the patient or specimen that may influence the services or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as \"ask at order entry questions (AOEs)\".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements." )
    protected List<Reference> supportingInfo;
    /**
     * The actual objects that are the target of the reference (Additional clinical information about the patient or specimen that may influence the services or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as "ask at order entry questions (AOEs)".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements.)
     */
    protected List<Resource> supportingInfoTarget;


    /**
     * One or more specimens that the laboratory procedure will use.
     */
    @Child(name = "specimen", type = {Specimen.class}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Procedure Samples", formalDefinition="One or more specimens that the laboratory procedure will use." )
    protected List<Reference> specimen;
    /**
     * The actual objects that are the target of the reference (One or more specimens that the laboratory procedure will use.)
     */
    protected List<Specimen> specimenTarget;


    /**
     * Anatomic location where the procedure should be performed. This is the target site.
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Location on Body", formalDefinition="Anatomic location where the procedure should be performed. This is the target site." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected List<CodeableConcept> bodySite;

    /**
     * Any other notes and comments made about the service request. For example, internal billing notes.
     */
    @Child(name = "note", type = {Annotation.class}, order=26, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments", formalDefinition="Any other notes and comments made about the service request. For example, internal billing notes." )
    protected List<Annotation> note;

    /**
     * Instructions in terms that are understood by the patient or consumer.
     */
    @Child(name = "patientInstruction", type = {StringType.class}, order=27, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient or consumer-oriented instructions", formalDefinition="Instructions in terms that are understood by the patient or consumer." )
    protected StringType patientInstruction;

    /**
     * Key events in the history of the request.
     */
    @Child(name = "relevantHistory", type = {Provenance.class}, order=28, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Request provenance", formalDefinition="Key events in the history of the request." )
    protected List<Reference> relevantHistory;
    /**
     * The actual objects that are the target of the reference (Key events in the history of the request.)
     */
    protected List<Provenance> relevantHistoryTarget;


    private static final long serialVersionUID = 1742384872L;

  /**
   * Constructor
   */
    public ServiceRequest() {
      super();
    }

  /**
   * Constructor
   */
    public ServiceRequest(Enumeration<ServiceRequestStatus> status, Enumeration<ServiceRequestIntent> intent, Reference subject) {
      super();
      this.status = status;
      this.intent = intent;
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
    public ServiceRequest setIdentifier(List<Identifier> theIdentifier) { 
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

    public ServiceRequest addIdentifier(Identifier t) { //3
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
     * @return {@link #instantiates} (Protocol or definition followed by this request.)
     */
    public List<UriType> getInstantiates() { 
      if (this.instantiates == null)
        this.instantiates = new ArrayList<UriType>();
      return this.instantiates;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ServiceRequest setInstantiates(List<UriType> theInstantiates) { 
      this.instantiates = theInstantiates;
      return this;
    }

    public boolean hasInstantiates() { 
      if (this.instantiates == null)
        return false;
      for (UriType item : this.instantiates)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #instantiates} (Protocol or definition followed by this request.)
     */
    public UriType addInstantiatesElement() {//2 
      UriType t = new UriType();
      if (this.instantiates == null)
        this.instantiates = new ArrayList<UriType>();
      this.instantiates.add(t);
      return t;
    }

    /**
     * @param value {@link #instantiates} (Protocol or definition followed by this request.)
     */
    public ServiceRequest addInstantiates(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.instantiates == null)
        this.instantiates = new ArrayList<UriType>();
      this.instantiates.add(t);
      return this;
    }

    /**
     * @param value {@link #instantiates} (Protocol or definition followed by this request.)
     */
    public boolean hasInstantiates(String value) { 
      if (this.instantiates == null)
        return false;
      for (UriType v : this.instantiates)
        if (v.getValue().equals(value)) // uri
          return true;
      return false;
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
    public ServiceRequest setBasedOn(List<Reference> theBasedOn) { 
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

    public ServiceRequest addBasedOn(Reference t) { //3
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
    public ServiceRequest setReplaces(List<Reference> theReplaces) { 
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

    public ServiceRequest addReplaces(Reference t) { //3
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
    public List<ServiceRequest> getReplacesTarget() { 
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<ServiceRequest>();
      return this.replacesTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ServiceRequest addReplacesTarget() { 
      ServiceRequest r = new ServiceRequest();
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<ServiceRequest>();
      this.replacesTarget.add(r);
      return r;
    }

    /**
     * @return {@link #requisition} (A shared identifier common to all service requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier.)
     */
    public Identifier getRequisition() { 
      if (this.requisition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.requisition");
        else if (Configuration.doAutoCreate())
          this.requisition = new Identifier(); // cc
      return this.requisition;
    }

    public boolean hasRequisition() { 
      return this.requisition != null && !this.requisition.isEmpty();
    }

    /**
     * @param value {@link #requisition} (A shared identifier common to all service requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier.)
     */
    public ServiceRequest setRequisition(Identifier value) { 
      this.requisition = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ServiceRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ServiceRequestStatus>(new ServiceRequestStatusEnumFactory()); // bb
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
    public ServiceRequest setStatusElement(Enumeration<ServiceRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the order.
     */
    public ServiceRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the order.
     */
    public ServiceRequest setStatus(ServiceRequestStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ServiceRequestStatus>(new ServiceRequestStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #intent} (Whether the request is a proposal, plan, an original order or a reflex order.). This is the underlying object with id, value and extensions. The accessor "getIntent" gives direct access to the value
     */
    public Enumeration<ServiceRequestIntent> getIntentElement() { 
      if (this.intent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.intent");
        else if (Configuration.doAutoCreate())
          this.intent = new Enumeration<ServiceRequestIntent>(new ServiceRequestIntentEnumFactory()); // bb
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
    public ServiceRequest setIntentElement(Enumeration<ServiceRequestIntent> value) { 
      this.intent = value;
      return this;
    }

    /**
     * @return Whether the request is a proposal, plan, an original order or a reflex order.
     */
    public ServiceRequestIntent getIntent() { 
      return this.intent == null ? null : this.intent.getValue();
    }

    /**
     * @param value Whether the request is a proposal, plan, an original order or a reflex order.
     */
    public ServiceRequest setIntent(ServiceRequestIntent value) { 
        if (this.intent == null)
          this.intent = new Enumeration<ServiceRequestIntent>(new ServiceRequestIntentEnumFactory());
        this.intent.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (A code that classifies the service for searching, sorting and display purposes (e.g. "Surgical Procedure").)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ServiceRequest setCategory(List<CodeableConcept> theCategory) { 
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

    public ServiceRequest addCategory(CodeableConcept t) { //3
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
     * @return {@link #priority} (Indicates how quickly the ServiceRequest should be addressed with respect to other requests.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Enumeration<ServiceRequestPriority> getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Enumeration<ServiceRequestPriority>(new ServiceRequestPriorityEnumFactory()); // bb
      return this.priority;
    }

    public boolean hasPriorityElement() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Indicates how quickly the ServiceRequest should be addressed with respect to other requests.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public ServiceRequest setPriorityElement(Enumeration<ServiceRequestPriority> value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return Indicates how quickly the ServiceRequest should be addressed with respect to other requests.
     */
    public ServiceRequestPriority getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value Indicates how quickly the ServiceRequest should be addressed with respect to other requests.
     */
    public ServiceRequest setPriority(ServiceRequestPriority value) { 
      if (value == null)
        this.priority = null;
      else {
        if (this.priority == null)
          this.priority = new Enumeration<ServiceRequestPriority>(new ServiceRequestPriorityEnumFactory());
        this.priority.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #doNotPerform} (Set this to true if the record is saying that the service/procedure should NOT be performed.). This is the underlying object with id, value and extensions. The accessor "getDoNotPerform" gives direct access to the value
     */
    public BooleanType getDoNotPerformElement() { 
      if (this.doNotPerform == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.doNotPerform");
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
     * @param value {@link #doNotPerform} (Set this to true if the record is saying that the service/procedure should NOT be performed.). This is the underlying object with id, value and extensions. The accessor "getDoNotPerform" gives direct access to the value
     */
    public ServiceRequest setDoNotPerformElement(BooleanType value) { 
      this.doNotPerform = value;
      return this;
    }

    /**
     * @return Set this to true if the record is saying that the service/procedure should NOT be performed.
     */
    public boolean getDoNotPerform() { 
      return this.doNotPerform == null || this.doNotPerform.isEmpty() ? false : this.doNotPerform.getValue();
    }

    /**
     * @param value Set this to true if the record is saying that the service/procedure should NOT be performed.
     */
    public ServiceRequest setDoNotPerform(boolean value) { 
        if (this.doNotPerform == null)
          this.doNotPerform = new BooleanType();
        this.doNotPerform.setValue(value);
      return this;
    }

    /**
     * @return {@link #code} (A code that identifies a particular service (i.e., procedure, diagnostic investigation, or panel of investigations) that have been requested.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A code that identifies a particular service (i.e., procedure, diagnostic investigation, or panel of investigations) that have been requested.)
     */
    public ServiceRequest setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #orderDetail} (Additional details and instructions about the how the services are to be delivered.   For example, and order for a urinary catheter may have an order detail for an external or indwelling catheter, or an order for a bandage may require additional instructions specifying how the bandage should be applied.)
     */
    public List<CodeableConcept> getOrderDetail() { 
      if (this.orderDetail == null)
        this.orderDetail = new ArrayList<CodeableConcept>();
      return this.orderDetail;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ServiceRequest setOrderDetail(List<CodeableConcept> theOrderDetail) { 
      this.orderDetail = theOrderDetail;
      return this;
    }

    public boolean hasOrderDetail() { 
      if (this.orderDetail == null)
        return false;
      for (CodeableConcept item : this.orderDetail)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addOrderDetail() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.orderDetail == null)
        this.orderDetail = new ArrayList<CodeableConcept>();
      this.orderDetail.add(t);
      return t;
    }

    public ServiceRequest addOrderDetail(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.orderDetail == null)
        this.orderDetail = new ArrayList<CodeableConcept>();
      this.orderDetail.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #orderDetail}, creating it if it does not already exist
     */
    public CodeableConcept getOrderDetailFirstRep() { 
      if (getOrderDetail().isEmpty()) {
        addOrderDetail();
      }
      return getOrderDetail().get(0);
    }

    /**
     * @return {@link #subject} (On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public ServiceRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public ServiceRequest setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.context");
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
    public ServiceRequest setContext(Reference value) { 
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
    public ServiceRequest setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #occurrence} (The date/time at which the requested service should occur.)
     */
    public Type getOccurrence() { 
      return this.occurrence;
    }

    /**
     * @return {@link #occurrence} (The date/time at which the requested service should occur.)
     */
    public DateTimeType getOccurrenceDateTimeType() throws FHIRException { 
      if (this.occurrence == null)
        return null;
      if (!(this.occurrence instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (DateTimeType) this.occurrence;
    }

    public boolean hasOccurrenceDateTimeType() { 
      return this != null && this.occurrence instanceof DateTimeType;
    }

    /**
     * @return {@link #occurrence} (The date/time at which the requested service should occur.)
     */
    public Period getOccurrencePeriod() throws FHIRException { 
      if (this.occurrence == null)
        return null;
      if (!(this.occurrence instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Period) this.occurrence;
    }

    public boolean hasOccurrencePeriod() { 
      return this != null && this.occurrence instanceof Period;
    }

    /**
     * @return {@link #occurrence} (The date/time at which the requested service should occur.)
     */
    public Timing getOccurrenceTiming() throws FHIRException { 
      if (this.occurrence == null)
        return null;
      if (!(this.occurrence instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Timing) this.occurrence;
    }

    public boolean hasOccurrenceTiming() { 
      return this != null && this.occurrence instanceof Timing;
    }

    public boolean hasOccurrence() { 
      return this.occurrence != null && !this.occurrence.isEmpty();
    }

    /**
     * @param value {@link #occurrence} (The date/time at which the requested service should occur.)
     */
    public ServiceRequest setOccurrence(Type value) { 
      if (value != null && !(value instanceof DateTimeType || value instanceof Period || value instanceof Timing))
        throw new Error("Not the right type for ServiceRequest.occurrence[x]: "+value.fhirType());
      this.occurrence = value;
      return this;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example "pain", "on flare-up", etc.)
     */
    public Type getAsNeeded() { 
      return this.asNeeded;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example "pain", "on flare-up", etc.)
     */
    public BooleanType getAsNeededBooleanType() throws FHIRException { 
      if (this.asNeeded == null)
        return null;
      if (!(this.asNeeded instanceof BooleanType))
        throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (BooleanType) this.asNeeded;
    }

    public boolean hasAsNeededBooleanType() { 
      return this != null && this.asNeeded instanceof BooleanType;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example "pain", "on flare-up", etc.)
     */
    public CodeableConcept getAsNeededCodeableConcept() throws FHIRException { 
      if (this.asNeeded == null)
        return null;
      if (!(this.asNeeded instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (CodeableConcept) this.asNeeded;
    }

    public boolean hasAsNeededCodeableConcept() { 
      return this != null && this.asNeeded instanceof CodeableConcept;
    }

    public boolean hasAsNeeded() { 
      return this.asNeeded != null && !this.asNeeded.isEmpty();
    }

    /**
     * @param value {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example "pain", "on flare-up", etc.)
     */
    public ServiceRequest setAsNeeded(Type value) { 
      if (value != null && !(value instanceof BooleanType || value instanceof CodeableConcept))
        throw new Error("Not the right type for ServiceRequest.asNeeded[x]: "+value.fhirType());
      this.asNeeded = value;
      return this;
    }

    /**
     * @return {@link #authoredOn} (When the request transitioned to being actionable.). This is the underlying object with id, value and extensions. The accessor "getAuthoredOn" gives direct access to the value
     */
    public DateTimeType getAuthoredOnElement() { 
      if (this.authoredOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.authoredOn");
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
    public ServiceRequest setAuthoredOnElement(DateTimeType value) { 
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
    public ServiceRequest setAuthoredOn(Date value) { 
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
    public Reference getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new Reference(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (The individual who initiated the request and has responsibility for its activation.)
     */
    public ServiceRequest setRequester(Reference value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #requester} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual who initiated the request and has responsibility for its activation.)
     */
    public Resource getRequesterTarget() { 
      return this.requesterTarget;
    }

    /**
     * @param value {@link #requester} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual who initiated the request and has responsibility for its activation.)
     */
    public ServiceRequest setRequesterTarget(Resource value) { 
      this.requesterTarget = value;
      return this;
    }

    /**
     * @return {@link #performerType} (Desired type of performer for doing the requested service.)
     */
    public CodeableConcept getPerformerType() { 
      if (this.performerType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.performerType");
        else if (Configuration.doAutoCreate())
          this.performerType = new CodeableConcept(); // cc
      return this.performerType;
    }

    public boolean hasPerformerType() { 
      return this.performerType != null && !this.performerType.isEmpty();
    }

    /**
     * @param value {@link #performerType} (Desired type of performer for doing the requested service.)
     */
    public ServiceRequest setPerformerType(CodeableConcept value) { 
      this.performerType = value;
      return this;
    }

    /**
     * @return {@link #performer} (The desired performer for doing the requested service.  For example, the surgeon, dermatopathologist, endoscopist, etc.)
     */
    public List<Reference> getPerformer() { 
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      return this.performer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ServiceRequest setPerformer(List<Reference> thePerformer) { 
      this.performer = thePerformer;
      return this;
    }

    public boolean hasPerformer() { 
      if (this.performer == null)
        return false;
      for (Reference item : this.performer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPerformer() { //3
      Reference t = new Reference();
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      this.performer.add(t);
      return t;
    }

    public ServiceRequest addPerformer(Reference t) { //3
      if (t == null)
        return this;
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      this.performer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #performer}, creating it if it does not already exist
     */
    public Reference getPerformerFirstRep() { 
      if (getPerformer().isEmpty()) {
        addPerformer();
      }
      return getPerformer().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getPerformerTarget() { 
      if (this.performerTarget == null)
        this.performerTarget = new ArrayList<Resource>();
      return this.performerTarget;
    }

    /**
     * @return {@link #reasonCode} (An explanation or justification for why this service is being requested in coded or textual form.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ServiceRequest setReasonCode(List<CodeableConcept> theReasonCode) { 
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

    public ServiceRequest addReasonCode(CodeableConcept t) { //3
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
     * @return {@link #reasonReference} (Indicates another resource that provides a justification for why this service is being requested.   May relate to the resources referred to in supportingInformation.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ServiceRequest setReasonReference(List<Reference> theReasonReference) { 
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

    public ServiceRequest addReasonReference(Reference t) { //3
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
     * @return {@link #insurance} (Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be needed for delivering the requested service.)
     */
    public List<Reference> getInsurance() { 
      if (this.insurance == null)
        this.insurance = new ArrayList<Reference>();
      return this.insurance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ServiceRequest setInsurance(List<Reference> theInsurance) { 
      this.insurance = theInsurance;
      return this;
    }

    public boolean hasInsurance() { 
      if (this.insurance == null)
        return false;
      for (Reference item : this.insurance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addInsurance() { //3
      Reference t = new Reference();
      if (this.insurance == null)
        this.insurance = new ArrayList<Reference>();
      this.insurance.add(t);
      return t;
    }

    public ServiceRequest addInsurance(Reference t) { //3
      if (t == null)
        return this;
      if (this.insurance == null)
        this.insurance = new ArrayList<Reference>();
      this.insurance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #insurance}, creating it if it does not already exist
     */
    public Reference getInsuranceFirstRep() { 
      if (getInsurance().isEmpty()) {
        addInsurance();
      }
      return getInsurance().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getInsuranceTarget() { 
      if (this.insuranceTarget == null)
        this.insuranceTarget = new ArrayList<Resource>();
      return this.insuranceTarget;
    }

    /**
     * @return {@link #supportingInfo} (Additional clinical information about the patient or specimen that may influence the services or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as "ask at order entry questions (AOEs)".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements.)
     */
    public List<Reference> getSupportingInfo() { 
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      return this.supportingInfo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ServiceRequest setSupportingInfo(List<Reference> theSupportingInfo) { 
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

    public ServiceRequest addSupportingInfo(Reference t) { //3
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
    public ServiceRequest setSpecimen(List<Reference> theSpecimen) { 
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

    public ServiceRequest addSpecimen(Reference t) { //3
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
    public ServiceRequest setBodySite(List<CodeableConcept> theBodySite) { 
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

    public ServiceRequest addBodySite(CodeableConcept t) { //3
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
     * @return {@link #note} (Any other notes and comments made about the service request. For example, internal billing notes.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ServiceRequest setNote(List<Annotation> theNote) { 
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

    public ServiceRequest addNote(Annotation t) { //3
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
     * @return {@link #patientInstruction} (Instructions in terms that are understood by the patient or consumer.). This is the underlying object with id, value and extensions. The accessor "getPatientInstruction" gives direct access to the value
     */
    public StringType getPatientInstructionElement() { 
      if (this.patientInstruction == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ServiceRequest.patientInstruction");
        else if (Configuration.doAutoCreate())
          this.patientInstruction = new StringType(); // bb
      return this.patientInstruction;
    }

    public boolean hasPatientInstructionElement() { 
      return this.patientInstruction != null && !this.patientInstruction.isEmpty();
    }

    public boolean hasPatientInstruction() { 
      return this.patientInstruction != null && !this.patientInstruction.isEmpty();
    }

    /**
     * @param value {@link #patientInstruction} (Instructions in terms that are understood by the patient or consumer.). This is the underlying object with id, value and extensions. The accessor "getPatientInstruction" gives direct access to the value
     */
    public ServiceRequest setPatientInstructionElement(StringType value) { 
      this.patientInstruction = value;
      return this;
    }

    /**
     * @return Instructions in terms that are understood by the patient or consumer.
     */
    public String getPatientInstruction() { 
      return this.patientInstruction == null ? null : this.patientInstruction.getValue();
    }

    /**
     * @param value Instructions in terms that are understood by the patient or consumer.
     */
    public ServiceRequest setPatientInstruction(String value) { 
      if (Utilities.noString(value))
        this.patientInstruction = null;
      else {
        if (this.patientInstruction == null)
          this.patientInstruction = new StringType();
        this.patientInstruction.setValue(value);
      }
      return this;
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
    public ServiceRequest setRelevantHistory(List<Reference> theRelevantHistory) { 
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

    public ServiceRequest addRelevantHistory(Reference t) { //3
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

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Identifiers assigned to this order instance by the orderer and/or the receiver and/or order fulfiller.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("instantiates", "uri", "Protocol or definition followed by this request.", 0, java.lang.Integer.MAX_VALUE, instantiates));
        children.add(new Property("basedOn", "Reference(CarePlan|ServiceRequest|MedicationRequest)", "Plan/proposal/order fulfilled by this request.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        children.add(new Property("replaces", "Reference(ServiceRequest)", "The request takes the place of the referenced completed or terminated request(s).", 0, java.lang.Integer.MAX_VALUE, replaces));
        children.add(new Property("requisition", "Identifier", "A shared identifier common to all service requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier.", 0, 1, requisition));
        children.add(new Property("status", "code", "The status of the order.", 0, 1, status));
        children.add(new Property("intent", "code", "Whether the request is a proposal, plan, an original order or a reflex order.", 0, 1, intent));
        children.add(new Property("category", "CodeableConcept", "A code that classifies the service for searching, sorting and display purposes (e.g. \"Surgical Procedure\").", 0, java.lang.Integer.MAX_VALUE, category));
        children.add(new Property("priority", "code", "Indicates how quickly the ServiceRequest should be addressed with respect to other requests.", 0, 1, priority));
        children.add(new Property("doNotPerform", "boolean", "Set this to true if the record is saying that the service/procedure should NOT be performed.", 0, 1, doNotPerform));
        children.add(new Property("code", "CodeableConcept", "A code that identifies a particular service (i.e., procedure, diagnostic investigation, or panel of investigations) that have been requested.", 0, 1, code));
        children.add(new Property("orderDetail", "CodeableConcept", "Additional details and instructions about the how the services are to be delivered.   For example, and order for a urinary catheter may have an order detail for an external or indwelling catheter, or an order for a bandage may require additional instructions specifying how the bandage should be applied.", 0, java.lang.Integer.MAX_VALUE, orderDetail));
        children.add(new Property("subject", "Reference(Patient|Group|Location|Device)", "On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).", 0, 1, subject));
        children.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "An encounter or episode of care that provides additional information about the healthcare context in which this request is made.", 0, 1, context));
        children.add(new Property("occurrence[x]", "dateTime|Period|Timing", "The date/time at which the requested service should occur.", 0, 1, occurrence));
        children.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example \"pain\", \"on flare-up\", etc.", 0, 1, asNeeded));
        children.add(new Property("authoredOn", "dateTime", "When the request transitioned to being actionable.", 0, 1, authoredOn));
        children.add(new Property("requester", "Reference(Practitioner|PractitionerRole|Organization|Patient|RelatedPerson|Device)", "The individual who initiated the request and has responsibility for its activation.", 0, 1, requester));
        children.add(new Property("performerType", "CodeableConcept", "Desired type of performer for doing the requested service.", 0, 1, performerType));
        children.add(new Property("performer", "Reference(Practitioner|PractitionerRole|Organization|CareTeam|HealthcareService|Patient|Device|RelatedPerson)", "The desired performer for doing the requested service.  For example, the surgeon, dermatopathologist, endoscopist, etc.", 0, java.lang.Integer.MAX_VALUE, performer));
        children.add(new Property("reasonCode", "CodeableConcept", "An explanation or justification for why this service is being requested in coded or textual form.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        children.add(new Property("reasonReference", "Reference(Condition|Observation|DiagnosticReport|DocumentReference)", "Indicates another resource that provides a justification for why this service is being requested.   May relate to the resources referred to in supportingInformation.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        children.add(new Property("insurance", "Reference(Coverage|ClaimResponse)", "Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be needed for delivering the requested service.", 0, java.lang.Integer.MAX_VALUE, insurance));
        children.add(new Property("supportingInfo", "Reference(Any)", "Additional clinical information about the patient or specimen that may influence the services or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as \"ask at order entry questions (AOEs)\".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements.", 0, java.lang.Integer.MAX_VALUE, supportingInfo));
        children.add(new Property("specimen", "Reference(Specimen)", "One or more specimens that the laboratory procedure will use.", 0, java.lang.Integer.MAX_VALUE, specimen));
        children.add(new Property("bodySite", "CodeableConcept", "Anatomic location where the procedure should be performed. This is the target site.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        children.add(new Property("note", "Annotation", "Any other notes and comments made about the service request. For example, internal billing notes.", 0, java.lang.Integer.MAX_VALUE, note));
        children.add(new Property("patientInstruction", "string", "Instructions in terms that are understood by the patient or consumer.", 0, 1, patientInstruction));
        children.add(new Property("relevantHistory", "Reference(Provenance)", "Key events in the history of the request.", 0, java.lang.Integer.MAX_VALUE, relevantHistory));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifiers assigned to this order instance by the orderer and/or the receiver and/or order fulfiller.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -246883639: /*instantiates*/  return new Property("instantiates", "uri", "Protocol or definition followed by this request.", 0, java.lang.Integer.MAX_VALUE, instantiates);
        case -332612366: /*basedOn*/  return new Property("basedOn", "Reference(CarePlan|ServiceRequest|MedicationRequest)", "Plan/proposal/order fulfilled by this request.", 0, java.lang.Integer.MAX_VALUE, basedOn);
        case -430332865: /*replaces*/  return new Property("replaces", "Reference(ServiceRequest)", "The request takes the place of the referenced completed or terminated request(s).", 0, java.lang.Integer.MAX_VALUE, replaces);
        case 395923612: /*requisition*/  return new Property("requisition", "Identifier", "A shared identifier common to all service requests that were authorized more or less simultaneously by a single author, representing the composite or group identifier.", 0, 1, requisition);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the order.", 0, 1, status);
        case -1183762788: /*intent*/  return new Property("intent", "code", "Whether the request is a proposal, plan, an original order or a reflex order.", 0, 1, intent);
        case 50511102: /*category*/  return new Property("category", "CodeableConcept", "A code that classifies the service for searching, sorting and display purposes (e.g. \"Surgical Procedure\").", 0, java.lang.Integer.MAX_VALUE, category);
        case -1165461084: /*priority*/  return new Property("priority", "code", "Indicates how quickly the ServiceRequest should be addressed with respect to other requests.", 0, 1, priority);
        case -1788508167: /*doNotPerform*/  return new Property("doNotPerform", "boolean", "Set this to true if the record is saying that the service/procedure should NOT be performed.", 0, 1, doNotPerform);
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A code that identifies a particular service (i.e., procedure, diagnostic investigation, or panel of investigations) that have been requested.", 0, 1, code);
        case 1187338559: /*orderDetail*/  return new Property("orderDetail", "CodeableConcept", "Additional details and instructions about the how the services are to be delivered.   For example, and order for a urinary catheter may have an order detail for an external or indwelling catheter, or an order for a bandage may require additional instructions specifying how the bandage should be applied.", 0, java.lang.Integer.MAX_VALUE, orderDetail);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Group|Location|Device)", "On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).", 0, 1, subject);
        case 951530927: /*context*/  return new Property("context", "Reference(Encounter|EpisodeOfCare)", "An encounter or episode of care that provides additional information about the healthcare context in which this request is made.", 0, 1, context);
        case -2022646513: /*occurrence[x]*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "The date/time at which the requested service should occur.", 0, 1, occurrence);
        case 1687874001: /*occurrence*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "The date/time at which the requested service should occur.", 0, 1, occurrence);
        case -298443636: /*occurrenceDateTime*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "The date/time at which the requested service should occur.", 0, 1, occurrence);
        case 1397156594: /*occurrencePeriod*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "The date/time at which the requested service should occur.", 0, 1, occurrence);
        case 1515218299: /*occurrenceTiming*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "The date/time at which the requested service should occur.", 0, 1, occurrence);
        case -544329575: /*asNeeded[x]*/  return new Property("asNeeded[x]", "boolean|CodeableConcept", "If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example \"pain\", \"on flare-up\", etc.", 0, 1, asNeeded);
        case -1432923513: /*asNeeded*/  return new Property("asNeeded[x]", "boolean|CodeableConcept", "If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example \"pain\", \"on flare-up\", etc.", 0, 1, asNeeded);
        case -591717471: /*asNeededBoolean*/  return new Property("asNeeded[x]", "boolean|CodeableConcept", "If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example \"pain\", \"on flare-up\", etc.", 0, 1, asNeeded);
        case 1556420122: /*asNeededCodeableConcept*/  return new Property("asNeeded[x]", "boolean|CodeableConcept", "If a CodeableConcept is present, it indicates the pre-condition for performing the service.  For example \"pain\", \"on flare-up\", etc.", 0, 1, asNeeded);
        case -1500852503: /*authoredOn*/  return new Property("authoredOn", "dateTime", "When the request transitioned to being actionable.", 0, 1, authoredOn);
        case 693933948: /*requester*/  return new Property("requester", "Reference(Practitioner|PractitionerRole|Organization|Patient|RelatedPerson|Device)", "The individual who initiated the request and has responsibility for its activation.", 0, 1, requester);
        case -901444568: /*performerType*/  return new Property("performerType", "CodeableConcept", "Desired type of performer for doing the requested service.", 0, 1, performerType);
        case 481140686: /*performer*/  return new Property("performer", "Reference(Practitioner|PractitionerRole|Organization|CareTeam|HealthcareService|Patient|Device|RelatedPerson)", "The desired performer for doing the requested service.  For example, the surgeon, dermatopathologist, endoscopist, etc.", 0, java.lang.Integer.MAX_VALUE, performer);
        case 722137681: /*reasonCode*/  return new Property("reasonCode", "CodeableConcept", "An explanation or justification for why this service is being requested in coded or textual form.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.", 0, java.lang.Integer.MAX_VALUE, reasonCode);
        case -1146218137: /*reasonReference*/  return new Property("reasonReference", "Reference(Condition|Observation|DiagnosticReport|DocumentReference)", "Indicates another resource that provides a justification for why this service is being requested.   May relate to the resources referred to in supportingInformation.", 0, java.lang.Integer.MAX_VALUE, reasonReference);
        case 73049818: /*insurance*/  return new Property("insurance", "Reference(Coverage|ClaimResponse)", "Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be needed for delivering the requested service.", 0, java.lang.Integer.MAX_VALUE, insurance);
        case 1922406657: /*supportingInfo*/  return new Property("supportingInfo", "Reference(Any)", "Additional clinical information about the patient or specimen that may influence the services or their interpretations.     This information includes diagnosis, clinical findings and other observations.  In laboratory ordering these are typically referred to as \"ask at order entry questions (AOEs)\".  This includes observations explicitly requested by the producer (filler) to provide context or supporting information needed to complete the order. For example,  reporting the amount of inspired oxygen for blood gas measurements.", 0, java.lang.Integer.MAX_VALUE, supportingInfo);
        case -2132868344: /*specimen*/  return new Property("specimen", "Reference(Specimen)", "One or more specimens that the laboratory procedure will use.", 0, java.lang.Integer.MAX_VALUE, specimen);
        case 1702620169: /*bodySite*/  return new Property("bodySite", "CodeableConcept", "Anatomic location where the procedure should be performed. This is the target site.", 0, java.lang.Integer.MAX_VALUE, bodySite);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Any other notes and comments made about the service request. For example, internal billing notes.", 0, java.lang.Integer.MAX_VALUE, note);
        case 737543241: /*patientInstruction*/  return new Property("patientInstruction", "string", "Instructions in terms that are understood by the patient or consumer.", 0, 1, patientInstruction);
        case 1538891575: /*relevantHistory*/  return new Property("relevantHistory", "Reference(Provenance)", "Key events in the history of the request.", 0, java.lang.Integer.MAX_VALUE, relevantHistory);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -246883639: /*instantiates*/ return this.instantiates == null ? new Base[0] : this.instantiates.toArray(new Base[this.instantiates.size()]); // UriType
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -430332865: /*replaces*/ return this.replaces == null ? new Base[0] : this.replaces.toArray(new Base[this.replaces.size()]); // Reference
        case 395923612: /*requisition*/ return this.requisition == null ? new Base[0] : new Base[] {this.requisition}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ServiceRequestStatus>
        case -1183762788: /*intent*/ return this.intent == null ? new Base[0] : new Base[] {this.intent}; // Enumeration<ServiceRequestIntent>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Enumeration<ServiceRequestPriority>
        case -1788508167: /*doNotPerform*/ return this.doNotPerform == null ? new Base[0] : new Base[] {this.doNotPerform}; // BooleanType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 1187338559: /*orderDetail*/ return this.orderDetail == null ? new Base[0] : this.orderDetail.toArray(new Base[this.orderDetail.size()]); // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case -1432923513: /*asNeeded*/ return this.asNeeded == null ? new Base[0] : new Base[] {this.asNeeded}; // Type
        case -1500852503: /*authoredOn*/ return this.authoredOn == null ? new Base[0] : new Base[] {this.authoredOn}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // Reference
        case -901444568: /*performerType*/ return this.performerType == null ? new Base[0] : new Base[] {this.performerType}; // CodeableConcept
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : this.performer.toArray(new Base[this.performer.size()]); // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 73049818: /*insurance*/ return this.insurance == null ? new Base[0] : this.insurance.toArray(new Base[this.insurance.size()]); // Reference
        case 1922406657: /*supportingInfo*/ return this.supportingInfo == null ? new Base[0] : this.supportingInfo.toArray(new Base[this.supportingInfo.size()]); // Reference
        case -2132868344: /*specimen*/ return this.specimen == null ? new Base[0] : this.specimen.toArray(new Base[this.specimen.size()]); // Reference
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : this.bodySite.toArray(new Base[this.bodySite.size()]); // CodeableConcept
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 737543241: /*patientInstruction*/ return this.patientInstruction == null ? new Base[0] : new Base[] {this.patientInstruction}; // StringType
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
        case -246883639: // instantiates
          this.getInstantiates().add(castToUri(value)); // UriType
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
          value = new ServiceRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ServiceRequestStatus>
          return value;
        case -1183762788: // intent
          value = new ServiceRequestIntentEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<ServiceRequestIntent>
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1165461084: // priority
          value = new ServiceRequestPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<ServiceRequestPriority>
          return value;
        case -1788508167: // doNotPerform
          this.doNotPerform = castToBoolean(value); // BooleanType
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1187338559: // orderDetail
          this.getOrderDetail().add(castToCodeableConcept(value)); // CodeableConcept
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
          this.requester = castToReference(value); // Reference
          return value;
        case -901444568: // performerType
          this.performerType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 481140686: // performer
          this.getPerformer().add(castToReference(value)); // Reference
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case 73049818: // insurance
          this.getInsurance().add(castToReference(value)); // Reference
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
        case 737543241: // patientInstruction
          this.patientInstruction = castToString(value); // StringType
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
        } else if (name.equals("instantiates")) {
          this.getInstantiates().add(castToUri(value));
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("replaces")) {
          this.getReplaces().add(castToReference(value));
        } else if (name.equals("requisition")) {
          this.requisition = castToIdentifier(value); // Identifier
        } else if (name.equals("status")) {
          value = new ServiceRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ServiceRequestStatus>
        } else if (name.equals("intent")) {
          value = new ServiceRequestIntentEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<ServiceRequestIntent>
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("priority")) {
          value = new ServiceRequestPriorityEnumFactory().fromType(castToCode(value));
          this.priority = (Enumeration) value; // Enumeration<ServiceRequestPriority>
        } else if (name.equals("doNotPerform")) {
          this.doNotPerform = castToBoolean(value); // BooleanType
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("orderDetail")) {
          this.getOrderDetail().add(castToCodeableConcept(value));
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
          this.requester = castToReference(value); // Reference
        } else if (name.equals("performerType")) {
          this.performerType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("performer")) {
          this.getPerformer().add(castToReference(value));
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("insurance")) {
          this.getInsurance().add(castToReference(value));
        } else if (name.equals("supportingInfo")) {
          this.getSupportingInfo().add(castToReference(value));
        } else if (name.equals("specimen")) {
          this.getSpecimen().add(castToReference(value));
        } else if (name.equals("bodySite")) {
          this.getBodySite().add(castToCodeableConcept(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("patientInstruction")) {
          this.patientInstruction = castToString(value); // StringType
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
        case -246883639:  return addInstantiatesElement();
        case -332612366:  return addBasedOn(); 
        case -430332865:  return addReplaces(); 
        case 395923612:  return getRequisition(); 
        case -892481550:  return getStatusElement();
        case -1183762788:  return getIntentElement();
        case 50511102:  return addCategory(); 
        case -1165461084:  return getPriorityElement();
        case -1788508167:  return getDoNotPerformElement();
        case 3059181:  return getCode(); 
        case 1187338559:  return addOrderDetail(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -2022646513:  return getOccurrence(); 
        case 1687874001:  return getOccurrence(); 
        case -544329575:  return getAsNeeded(); 
        case -1432923513:  return getAsNeeded(); 
        case -1500852503:  return getAuthoredOnElement();
        case 693933948:  return getRequester(); 
        case -901444568:  return getPerformerType(); 
        case 481140686:  return addPerformer(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case 73049818:  return addInsurance(); 
        case 1922406657:  return addSupportingInfo(); 
        case -2132868344:  return addSpecimen(); 
        case 1702620169:  return addBodySite(); 
        case 3387378:  return addNote(); 
        case 737543241:  return getPatientInstructionElement();
        case 1538891575:  return addRelevantHistory(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -246883639: /*instantiates*/ return new String[] {"uri"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -430332865: /*replaces*/ return new String[] {"Reference"};
        case 395923612: /*requisition*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1183762788: /*intent*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case -1165461084: /*priority*/ return new String[] {"code"};
        case -1788508167: /*doNotPerform*/ return new String[] {"boolean"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 1187338559: /*orderDetail*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case 1687874001: /*occurrence*/ return new String[] {"dateTime", "Period", "Timing"};
        case -1432923513: /*asNeeded*/ return new String[] {"boolean", "CodeableConcept"};
        case -1500852503: /*authoredOn*/ return new String[] {"dateTime"};
        case 693933948: /*requester*/ return new String[] {"Reference"};
        case -901444568: /*performerType*/ return new String[] {"CodeableConcept"};
        case 481140686: /*performer*/ return new String[] {"Reference"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case 73049818: /*insurance*/ return new String[] {"Reference"};
        case 1922406657: /*supportingInfo*/ return new String[] {"Reference"};
        case -2132868344: /*specimen*/ return new String[] {"Reference"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case 737543241: /*patientInstruction*/ return new String[] {"string"};
        case 1538891575: /*relevantHistory*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("instantiates")) {
          throw new FHIRException("Cannot call addChild on a primitive type ServiceRequest.instantiates");
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
          throw new FHIRException("Cannot call addChild on a primitive type ServiceRequest.status");
        }
        else if (name.equals("intent")) {
          throw new FHIRException("Cannot call addChild on a primitive type ServiceRequest.intent");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type ServiceRequest.priority");
        }
        else if (name.equals("doNotPerform")) {
          throw new FHIRException("Cannot call addChild on a primitive type ServiceRequest.doNotPerform");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("orderDetail")) {
          return addOrderDetail();
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
          throw new FHIRException("Cannot call addChild on a primitive type ServiceRequest.authoredOn");
        }
        else if (name.equals("requester")) {
          this.requester = new Reference();
          return this.requester;
        }
        else if (name.equals("performerType")) {
          this.performerType = new CodeableConcept();
          return this.performerType;
        }
        else if (name.equals("performer")) {
          return addPerformer();
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("insurance")) {
          return addInsurance();
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
        else if (name.equals("patientInstruction")) {
          throw new FHIRException("Cannot call addChild on a primitive type ServiceRequest.patientInstruction");
        }
        else if (name.equals("relevantHistory")) {
          return addRelevantHistory();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ServiceRequest";

  }

      public ServiceRequest copy() {
        ServiceRequest dst = new ServiceRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (instantiates != null) {
          dst.instantiates = new ArrayList<UriType>();
          for (UriType i : instantiates)
            dst.instantiates.add(i.copy());
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
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.priority = priority == null ? null : priority.copy();
        dst.doNotPerform = doNotPerform == null ? null : doNotPerform.copy();
        dst.code = code == null ? null : code.copy();
        if (orderDetail != null) {
          dst.orderDetail = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : orderDetail)
            dst.orderDetail.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        dst.asNeeded = asNeeded == null ? null : asNeeded.copy();
        dst.authoredOn = authoredOn == null ? null : authoredOn.copy();
        dst.requester = requester == null ? null : requester.copy();
        dst.performerType = performerType == null ? null : performerType.copy();
        if (performer != null) {
          dst.performer = new ArrayList<Reference>();
          for (Reference i : performer)
            dst.performer.add(i.copy());
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
        if (insurance != null) {
          dst.insurance = new ArrayList<Reference>();
          for (Reference i : insurance)
            dst.insurance.add(i.copy());
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
        dst.patientInstruction = patientInstruction == null ? null : patientInstruction.copy();
        if (relevantHistory != null) {
          dst.relevantHistory = new ArrayList<Reference>();
          for (Reference i : relevantHistory)
            dst.relevantHistory.add(i.copy());
        };
        return dst;
      }

      protected ServiceRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ServiceRequest))
          return false;
        ServiceRequest o = (ServiceRequest) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(instantiates, o.instantiates, true)
           && compareDeep(basedOn, o.basedOn, true) && compareDeep(replaces, o.replaces, true) && compareDeep(requisition, o.requisition, true)
           && compareDeep(status, o.status, true) && compareDeep(intent, o.intent, true) && compareDeep(category, o.category, true)
           && compareDeep(priority, o.priority, true) && compareDeep(doNotPerform, o.doNotPerform, true) && compareDeep(code, o.code, true)
           && compareDeep(orderDetail, o.orderDetail, true) && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true)
           && compareDeep(occurrence, o.occurrence, true) && compareDeep(asNeeded, o.asNeeded, true) && compareDeep(authoredOn, o.authoredOn, true)
           && compareDeep(requester, o.requester, true) && compareDeep(performerType, o.performerType, true)
           && compareDeep(performer, o.performer, true) && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(insurance, o.insurance, true) && compareDeep(supportingInfo, o.supportingInfo, true)
           && compareDeep(specimen, o.specimen, true) && compareDeep(bodySite, o.bodySite, true) && compareDeep(note, o.note, true)
           && compareDeep(patientInstruction, o.patientInstruction, true) && compareDeep(relevantHistory, o.relevantHistory, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ServiceRequest))
          return false;
        ServiceRequest o = (ServiceRequest) other_;
        return compareValues(instantiates, o.instantiates, true) && compareValues(status, o.status, true) && compareValues(intent, o.intent, true)
           && compareValues(priority, o.priority, true) && compareValues(doNotPerform, o.doNotPerform, true) && compareValues(authoredOn, o.authoredOn, true)
           && compareValues(patientInstruction, o.patientInstruction, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, instantiates, basedOn
          , replaces, requisition, status, intent, category, priority, doNotPerform, code
          , orderDetail, subject, context, occurrence, asNeeded, authoredOn, requester, performerType
          , performer, reasonCode, reasonReference, insurance, supportingInfo, specimen, bodySite
          , note, patientInstruction, relevantHistory);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ServiceRequest;
   }

 /**
   * Search parameter: <b>authored</b>
   * <p>
   * Description: <b>Date request signed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ServiceRequest.authoredOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authored", path="ServiceRequest.authoredOn", description="Date request signed", type="date" )
  public static final String SP_AUTHORED = "authored";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authored</b>
   * <p>
   * Description: <b>Date request signed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ServiceRequest.authoredOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHORED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHORED);

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>Who/what is requesting service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.requester</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="ServiceRequest.requester", description="Who/what is requesting service", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>Who/what is requesting service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.requester</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ServiceRequest:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("ServiceRequest:requester").toLocked();

 /**
   * Search parameter: <b>instantiates</b>
   * <p>
   * Description: <b>Protocol or definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ServiceRequest.instantiates</b><br>
   * </p>
   */
  @SearchParamDefinition(name="instantiates", path="ServiceRequest.instantiates", description="Protocol or definition", type="uri" )
  public static final String SP_INSTANTIATES = "instantiates";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>instantiates</b>
   * <p>
   * Description: <b>Protocol or definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ServiceRequest.instantiates</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam INSTANTIATES = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_INSTANTIATES);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Identifiers assigned to this order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ServiceRequest.identifier", description="Identifiers assigned to this order", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Identifiers assigned to this order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>What is being requested/ordered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="ServiceRequest.code", description="What is being requested/ordered", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>What is being requested/ordered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Requested performer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="ServiceRequest.performer", description="Requested performer", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={CareTeam.class, Device.class, HealthcareService.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>Requested performer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ServiceRequest:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("ServiceRequest:performer").toLocked();

 /**
   * Search parameter: <b>requisition</b>
   * <p>
   * Description: <b>Composite Request ID</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.requisition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requisition", path="ServiceRequest.requisition", description="Composite Request ID", type="token" )
  public static final String SP_REQUISITION = "requisition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requisition</b>
   * <p>
   * Description: <b>Composite Request ID</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.requisition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUISITION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUISITION);

 /**
   * Search parameter: <b>replaces</b>
   * <p>
   * Description: <b>What request replaces</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.replaces</b><br>
   * </p>
   */
  @SearchParamDefinition(name="replaces", path="ServiceRequest.replaces", description="What request replaces", type="reference", target={ServiceRequest.class } )
  public static final String SP_REPLACES = "replaces";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>replaces</b>
   * <p>
   * Description: <b>What request replaces</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.replaces</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REPLACES = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REPLACES);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ServiceRequest:replaces</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REPLACES = new ca.uhn.fhir.model.api.Include("ServiceRequest:replaces").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="ServiceRequest.subject", description="Search by subject", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Device.class, Group.class, Location.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ServiceRequest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("ServiceRequest:subject").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>An encounter in which this request is made</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="ServiceRequest.context", description="An encounter in which this request is made", type="reference", target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>An encounter in which this request is made</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ServiceRequest:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("ServiceRequest:encounter").toLocked();

 /**
   * Search parameter: <b>occurrence</b>
   * <p>
   * Description: <b>When service should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ServiceRequest.occurrence[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="occurrence", path="ServiceRequest.occurrence", description="When service should occur", type="date" )
  public static final String SP_OCCURRENCE = "occurrence";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>occurrence</b>
   * <p>
   * Description: <b>When service should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ServiceRequest.occurrence[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam OCCURRENCE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_OCCURRENCE);

 /**
   * Search parameter: <b>priority</b>
   * <p>
   * Description: <b>routine | urgent | asap | stat</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.priority</b><br>
   * </p>
   */
  @SearchParamDefinition(name="priority", path="ServiceRequest.priority", description="routine | urgent | asap | stat", type="token" )
  public static final String SP_PRIORITY = "priority";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>priority</b>
   * <p>
   * Description: <b>routine | urgent | asap | stat</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.priority</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PRIORITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PRIORITY);

 /**
   * Search parameter: <b>intent</b>
   * <p>
   * Description: <b>proposal | plan | order +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.intent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="intent", path="ServiceRequest.intent", description="proposal | plan | order +", type="token" )
  public static final String SP_INTENT = "intent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>intent</b>
   * <p>
   * Description: <b>proposal | plan | order +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.intent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam INTENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_INTENT);

 /**
   * Search parameter: <b>performer-type</b>
   * <p>
   * Description: <b>Performer role</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.performerType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer-type", path="ServiceRequest.performerType", description="Performer role", type="token" )
  public static final String SP_PERFORMER_TYPE = "performer-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer-type</b>
   * <p>
   * Description: <b>Performer role</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.performerType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PERFORMER_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PERFORMER_TYPE);

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>What request fulfills</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="ServiceRequest.basedOn", description="What request fulfills", type="reference", target={CarePlan.class, MedicationRequest.class, ServiceRequest.class } )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>What request fulfills</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ServiceRequest:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("ServiceRequest:based-on").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ServiceRequest.subject", description="Search by subject - a patient", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ServiceRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ServiceRequest:patient").toLocked();

 /**
   * Search parameter: <b>specimen</b>
   * <p>
   * Description: <b>Specimen to be tested</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.specimen</b><br>
   * </p>
   */
  @SearchParamDefinition(name="specimen", path="ServiceRequest.specimen", description="Specimen to be tested", type="reference", target={Specimen.class } )
  public static final String SP_SPECIMEN = "specimen";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
   * <p>
   * Description: <b>Specimen to be tested</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.specimen</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SPECIMEN = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SPECIMEN);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ServiceRequest:specimen</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SPECIMEN = new ca.uhn.fhir.model.api.Include("ServiceRequest:specimen").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Encounter or Episode during which request was created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="ServiceRequest.context", description="Encounter or Episode during which request was created", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Encounter or Episode during which request was created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ServiceRequest.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ServiceRequest:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("ServiceRequest:context").toLocked();

 /**
   * Search parameter: <b>body-site</b>
   * <p>
   * Description: <b>Where procedure is going to be done</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.bodySite</b><br>
   * </p>
   */
  @SearchParamDefinition(name="body-site", path="ServiceRequest.bodySite", description="Where procedure is going to be done", type="token" )
  public static final String SP_BODY_SITE = "body-site";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>body-site</b>
   * <p>
   * Description: <b>Where procedure is going to be done</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.bodySite</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam BODY_SITE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_BODY_SITE);

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Classification of service</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="ServiceRequest.category", description="Classification of service", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Classification of service</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | active | suspended | completed | entered-in-error | cancelled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ServiceRequest.status", description="draft | active | suspended | completed | entered-in-error | cancelled", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | active | suspended | completed | entered-in-error | cancelled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ServiceRequest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

