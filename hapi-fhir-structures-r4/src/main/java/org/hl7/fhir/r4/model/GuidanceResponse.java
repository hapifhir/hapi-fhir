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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

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
 * A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.
 */
@ResourceDef(name="GuidanceResponse", profile="http://hl7.org/fhir/Profile/GuidanceResponse")
public class GuidanceResponse extends DomainResource {

    public enum GuidanceResponseStatus {
        /**
         * The request was processed successfully.
         */
        SUCCESS, 
        /**
         * The request was processed successfully, but more data may result in a more complete evaluation.
         */
        DATAREQUESTED, 
        /**
         * The request was processed, but more data is required to complete the evaluation.
         */
        DATAREQUIRED, 
        /**
         * The request is currently being processed.
         */
        INPROGRESS, 
        /**
         * The request was not processed successfully.
         */
        FAILURE, 
        /**
         * The response was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GuidanceResponseStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("success".equals(codeString))
          return SUCCESS;
        if ("data-requested".equals(codeString))
          return DATAREQUESTED;
        if ("data-required".equals(codeString))
          return DATAREQUIRED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("failure".equals(codeString))
          return FAILURE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SUCCESS: return "success";
            case DATAREQUESTED: return "data-requested";
            case DATAREQUIRED: return "data-required";
            case INPROGRESS: return "in-progress";
            case FAILURE: return "failure";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SUCCESS: return "http://hl7.org/fhir/guidance-response-status";
            case DATAREQUESTED: return "http://hl7.org/fhir/guidance-response-status";
            case DATAREQUIRED: return "http://hl7.org/fhir/guidance-response-status";
            case INPROGRESS: return "http://hl7.org/fhir/guidance-response-status";
            case FAILURE: return "http://hl7.org/fhir/guidance-response-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/guidance-response-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SUCCESS: return "The request was processed successfully.";
            case DATAREQUESTED: return "The request was processed successfully, but more data may result in a more complete evaluation.";
            case DATAREQUIRED: return "The request was processed, but more data is required to complete the evaluation.";
            case INPROGRESS: return "The request is currently being processed.";
            case FAILURE: return "The request was not processed successfully.";
            case ENTEREDINERROR: return "The response was entered in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SUCCESS: return "Success";
            case DATAREQUESTED: return "Data Requested";
            case DATAREQUIRED: return "Data Required";
            case INPROGRESS: return "In Progress";
            case FAILURE: return "Failure";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class GuidanceResponseStatusEnumFactory implements EnumFactory<GuidanceResponseStatus> {
    public GuidanceResponseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("success".equals(codeString))
          return GuidanceResponseStatus.SUCCESS;
        if ("data-requested".equals(codeString))
          return GuidanceResponseStatus.DATAREQUESTED;
        if ("data-required".equals(codeString))
          return GuidanceResponseStatus.DATAREQUIRED;
        if ("in-progress".equals(codeString))
          return GuidanceResponseStatus.INPROGRESS;
        if ("failure".equals(codeString))
          return GuidanceResponseStatus.FAILURE;
        if ("entered-in-error".equals(codeString))
          return GuidanceResponseStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
        public Enumeration<GuidanceResponseStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GuidanceResponseStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("success".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.SUCCESS);
        if ("data-requested".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.DATAREQUESTED);
        if ("data-required".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.DATAREQUIRED);
        if ("in-progress".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.INPROGRESS);
        if ("failure".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.FAILURE);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
    public String toCode(GuidanceResponseStatus code) {
      if (code == GuidanceResponseStatus.SUCCESS)
        return "success";
      if (code == GuidanceResponseStatus.DATAREQUESTED)
        return "data-requested";
      if (code == GuidanceResponseStatus.DATAREQUIRED)
        return "data-required";
      if (code == GuidanceResponseStatus.INPROGRESS)
        return "in-progress";
      if (code == GuidanceResponseStatus.FAILURE)
        return "failure";
      if (code == GuidanceResponseStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(GuidanceResponseStatus code) {
      return code.getSystem();
      }
    }

    /**
     * The identifier of the request associated with this response. If an identifier was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.
     */
    @Child(name = "requestIdentifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The identifier of the request associated with this response, if any", formalDefinition="The identifier of the request associated with this response. If an identifier was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario." )
    protected Identifier requestIdentifier;

    /**
     * Allows a service to provide  unique, business identifiers for the response.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="Allows a service to provide  unique, business identifiers for the response." )
    protected List<Identifier> identifier;

    /**
     * An identifier, CodeableConcept or canonical reference to the guidance that was requested.
     */
    @Child(name = "module", type = {UriType.class, CanonicalType.class, CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What guidance was requested", formalDefinition="An identifier, CodeableConcept or canonical reference to the guidance that was requested." )
    protected Type module;

    /**
     * The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="success | data-requested | data-required | in-progress | failure | entered-in-error", formalDefinition="The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/guidance-response-status")
    protected Enumeration<GuidanceResponseStatus> status;

    /**
     * The patient for which the request was processed.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Patient the request was performed for", formalDefinition="The patient for which the request was processed." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient for which the request was processed.)
     */
    protected Resource subjectTarget;

    /**
     * Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Encounter or Episode during which the response was returned", formalDefinition="Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    protected Resource contextTarget;

    /**
     * Indicates when the guidance response was processed.
     */
    @Child(name = "occurrenceDateTime", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the guidance response was processed", formalDefinition="Indicates when the guidance response was processed." )
    protected DateTimeType occurrenceDateTime;

    /**
     * Provides a reference to the device that performed the guidance.
     */
    @Child(name = "performer", type = {Device.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Device returning the guidance", formalDefinition="Provides a reference to the device that performed the guidance." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (Provides a reference to the device that performed the guidance.)
     */
    protected Device performerTarget;

    /**
     * Describes the reason for the guidance response in coded or textual form.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Why guidance is needed", formalDefinition="Describes the reason for the guidance response in coded or textual form." )
    protected List<CodeableConcept> reasonCode;

    /**
     * Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class, DiagnosticReport.class, DocumentReference.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Why guidance is needed", formalDefinition="Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * Provides a mechanism to communicate additional information about the response.
     */
    @Child(name = "note", type = {Annotation.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional notes about the response", formalDefinition="Provides a mechanism to communicate additional information about the response." )
    protected List<Annotation> note;

    /**
     * Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.
     */
    @Child(name = "evaluationMessage", type = {OperationOutcome.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Messages resulting from the evaluation of the artifact or artifacts", formalDefinition="Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element." )
    protected List<Reference> evaluationMessage;
    /**
     * The actual objects that are the target of the reference (Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.)
     */
    protected List<OperationOutcome> evaluationMessageTarget;


    /**
     * The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.
     */
    @Child(name = "outputParameters", type = {Parameters.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The output parameters of the evaluation, if any", formalDefinition="The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element." )
    protected Reference outputParameters;

    /**
     * The actual object that is the target of the reference (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    protected Parameters outputParametersTarget;

    /**
     * The actions, if any, produced by the evaluation of the artifact.
     */
    @Child(name = "result", type = {CarePlan.class, RequestGroup.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Proposed actions, if any", formalDefinition="The actions, if any, produced by the evaluation of the artifact." )
    protected Reference result;

    /**
     * The actual object that is the target of the reference (The actions, if any, produced by the evaluation of the artifact.)
     */
    protected Resource resultTarget;

    /**
     * If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.
     */
    @Child(name = "dataRequirement", type = {DataRequirement.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional required data", formalDefinition="If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data." )
    protected List<DataRequirement> dataRequirement;

    private static final long serialVersionUID = 879877474L;

  /**
   * Constructor
   */
    public GuidanceResponse() {
      super();
    }

  /**
   * Constructor
   */
    public GuidanceResponse(Type module, Enumeration<GuidanceResponseStatus> status) {
      super();
      this.module = module;
      this.status = status;
    }

    /**
     * @return {@link #requestIdentifier} (The identifier of the request associated with this response. If an identifier was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.)
     */
    public Identifier getRequestIdentifier() { 
      if (this.requestIdentifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.requestIdentifier");
        else if (Configuration.doAutoCreate())
          this.requestIdentifier = new Identifier(); // cc
      return this.requestIdentifier;
    }

    public boolean hasRequestIdentifier() { 
      return this.requestIdentifier != null && !this.requestIdentifier.isEmpty();
    }

    /**
     * @param value {@link #requestIdentifier} (The identifier of the request associated with this response. If an identifier was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.)
     */
    public GuidanceResponse setRequestIdentifier(Identifier value) { 
      this.requestIdentifier = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Allows a service to provide  unique, business identifiers for the response.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GuidanceResponse setIdentifier(List<Identifier> theIdentifier) { 
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

    public GuidanceResponse addIdentifier(Identifier t) { //3
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
     * @return {@link #module} (An identifier, CodeableConcept or canonical reference to the guidance that was requested.)
     */
    public Type getModule() { 
      return this.module;
    }

    /**
     * @return {@link #module} (An identifier, CodeableConcept or canonical reference to the guidance that was requested.)
     */
    public UriType getModuleUriType() throws FHIRException { 
      if (this.module == null)
        return null;
      if (!(this.module instanceof UriType))
        throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.module.getClass().getName()+" was encountered");
      return (UriType) this.module;
    }

    public boolean hasModuleUriType() { 
      return this != null && this.module instanceof UriType;
    }

    /**
     * @return {@link #module} (An identifier, CodeableConcept or canonical reference to the guidance that was requested.)
     */
    public CanonicalType getModuleCanonicalType() throws FHIRException { 
      if (this.module == null)
        return null;
      if (!(this.module instanceof CanonicalType))
        throw new FHIRException("Type mismatch: the type CanonicalType was expected, but "+this.module.getClass().getName()+" was encountered");
      return (CanonicalType) this.module;
    }

    public boolean hasModuleCanonicalType() { 
      return this != null && this.module instanceof CanonicalType;
    }

    /**
     * @return {@link #module} (An identifier, CodeableConcept or canonical reference to the guidance that was requested.)
     */
    public CodeableConcept getModuleCodeableConcept() throws FHIRException { 
      if (this.module == null)
        return null;
      if (!(this.module instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.module.getClass().getName()+" was encountered");
      return (CodeableConcept) this.module;
    }

    public boolean hasModuleCodeableConcept() { 
      return this != null && this.module instanceof CodeableConcept;
    }

    public boolean hasModule() { 
      return this.module != null && !this.module.isEmpty();
    }

    /**
     * @param value {@link #module} (An identifier, CodeableConcept or canonical reference to the guidance that was requested.)
     */
    public GuidanceResponse setModule(Type value) { 
      if (value != null && !(value instanceof UriType || value instanceof CanonicalType || value instanceof CodeableConcept))
        throw new Error("Not the right type for GuidanceResponse.module[x]: "+value.fhirType());
      this.module = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<GuidanceResponseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<GuidanceResponseStatus>(new GuidanceResponseStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public GuidanceResponse setStatusElement(Enumeration<GuidanceResponseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.
     */
    public GuidanceResponseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.
     */
    public GuidanceResponse setStatus(GuidanceResponseStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<GuidanceResponseStatus>(new GuidanceResponseStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (The patient for which the request was processed.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient for which the request was processed.)
     */
    public GuidanceResponse setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient for which the request was processed.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient for which the request was processed.)
     */
    public GuidanceResponse setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    public GuidanceResponse setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.)
     */
    public GuidanceResponse setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #occurrenceDateTime} (Indicates when the guidance response was processed.). This is the underlying object with id, value and extensions. The accessor "getOccurrenceDateTime" gives direct access to the value
     */
    public DateTimeType getOccurrenceDateTimeElement() { 
      if (this.occurrenceDateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.occurrenceDateTime");
        else if (Configuration.doAutoCreate())
          this.occurrenceDateTime = new DateTimeType(); // bb
      return this.occurrenceDateTime;
    }

    public boolean hasOccurrenceDateTimeElement() { 
      return this.occurrenceDateTime != null && !this.occurrenceDateTime.isEmpty();
    }

    public boolean hasOccurrenceDateTime() { 
      return this.occurrenceDateTime != null && !this.occurrenceDateTime.isEmpty();
    }

    /**
     * @param value {@link #occurrenceDateTime} (Indicates when the guidance response was processed.). This is the underlying object with id, value and extensions. The accessor "getOccurrenceDateTime" gives direct access to the value
     */
    public GuidanceResponse setOccurrenceDateTimeElement(DateTimeType value) { 
      this.occurrenceDateTime = value;
      return this;
    }

    /**
     * @return Indicates when the guidance response was processed.
     */
    public Date getOccurrenceDateTime() { 
      return this.occurrenceDateTime == null ? null : this.occurrenceDateTime.getValue();
    }

    /**
     * @param value Indicates when the guidance response was processed.
     */
    public GuidanceResponse setOccurrenceDateTime(Date value) { 
      if (value == null)
        this.occurrenceDateTime = null;
      else {
        if (this.occurrenceDateTime == null)
          this.occurrenceDateTime = new DateTimeType();
        this.occurrenceDateTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #performer} (Provides a reference to the device that performed the guidance.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (Provides a reference to the device that performed the guidance.)
     */
    public GuidanceResponse setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Provides a reference to the device that performed the guidance.)
     */
    public Device getPerformerTarget() { 
      if (this.performerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.performer");
        else if (Configuration.doAutoCreate())
          this.performerTarget = new Device(); // aa
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Provides a reference to the device that performed the guidance.)
     */
    public GuidanceResponse setPerformerTarget(Device value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #reasonCode} (Describes the reason for the guidance response in coded or textual form.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GuidanceResponse setReasonCode(List<CodeableConcept> theReasonCode) { 
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

    public GuidanceResponse addReasonCode(CodeableConcept t) { //3
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
     * @return {@link #reasonReference} (Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GuidanceResponse setReasonReference(List<Reference> theReasonReference) { 
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

    public GuidanceResponse addReasonReference(Reference t) { //3
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
     * @return {@link #note} (Provides a mechanism to communicate additional information about the response.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GuidanceResponse setNote(List<Annotation> theNote) { 
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

    public GuidanceResponse addNote(Annotation t) { //3
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
     * @return {@link #evaluationMessage} (Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.)
     */
    public List<Reference> getEvaluationMessage() { 
      if (this.evaluationMessage == null)
        this.evaluationMessage = new ArrayList<Reference>();
      return this.evaluationMessage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GuidanceResponse setEvaluationMessage(List<Reference> theEvaluationMessage) { 
      this.evaluationMessage = theEvaluationMessage;
      return this;
    }

    public boolean hasEvaluationMessage() { 
      if (this.evaluationMessage == null)
        return false;
      for (Reference item : this.evaluationMessage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEvaluationMessage() { //3
      Reference t = new Reference();
      if (this.evaluationMessage == null)
        this.evaluationMessage = new ArrayList<Reference>();
      this.evaluationMessage.add(t);
      return t;
    }

    public GuidanceResponse addEvaluationMessage(Reference t) { //3
      if (t == null)
        return this;
      if (this.evaluationMessage == null)
        this.evaluationMessage = new ArrayList<Reference>();
      this.evaluationMessage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #evaluationMessage}, creating it if it does not already exist
     */
    public Reference getEvaluationMessageFirstRep() { 
      if (getEvaluationMessage().isEmpty()) {
        addEvaluationMessage();
      }
      return getEvaluationMessage().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<OperationOutcome> getEvaluationMessageTarget() { 
      if (this.evaluationMessageTarget == null)
        this.evaluationMessageTarget = new ArrayList<OperationOutcome>();
      return this.evaluationMessageTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public OperationOutcome addEvaluationMessageTarget() { 
      OperationOutcome r = new OperationOutcome();
      if (this.evaluationMessageTarget == null)
        this.evaluationMessageTarget = new ArrayList<OperationOutcome>();
      this.evaluationMessageTarget.add(r);
      return r;
    }

    /**
     * @return {@link #outputParameters} (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public Reference getOutputParameters() { 
      if (this.outputParameters == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.outputParameters");
        else if (Configuration.doAutoCreate())
          this.outputParameters = new Reference(); // cc
      return this.outputParameters;
    }

    public boolean hasOutputParameters() { 
      return this.outputParameters != null && !this.outputParameters.isEmpty();
    }

    /**
     * @param value {@link #outputParameters} (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public GuidanceResponse setOutputParameters(Reference value) { 
      this.outputParameters = value;
      return this;
    }

    /**
     * @return {@link #outputParameters} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public Parameters getOutputParametersTarget() { 
      if (this.outputParametersTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.outputParameters");
        else if (Configuration.doAutoCreate())
          this.outputParametersTarget = new Parameters(); // aa
      return this.outputParametersTarget;
    }

    /**
     * @param value {@link #outputParameters} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public GuidanceResponse setOutputParametersTarget(Parameters value) { 
      this.outputParametersTarget = value;
      return this;
    }

    /**
     * @return {@link #result} (The actions, if any, produced by the evaluation of the artifact.)
     */
    public Reference getResult() { 
      if (this.result == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.result");
        else if (Configuration.doAutoCreate())
          this.result = new Reference(); // cc
      return this.result;
    }

    public boolean hasResult() { 
      return this.result != null && !this.result.isEmpty();
    }

    /**
     * @param value {@link #result} (The actions, if any, produced by the evaluation of the artifact.)
     */
    public GuidanceResponse setResult(Reference value) { 
      this.result = value;
      return this;
    }

    /**
     * @return {@link #result} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The actions, if any, produced by the evaluation of the artifact.)
     */
    public Resource getResultTarget() { 
      return this.resultTarget;
    }

    /**
     * @param value {@link #result} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The actions, if any, produced by the evaluation of the artifact.)
     */
    public GuidanceResponse setResultTarget(Resource value) { 
      this.resultTarget = value;
      return this;
    }

    /**
     * @return {@link #dataRequirement} (If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.)
     */
    public List<DataRequirement> getDataRequirement() { 
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      return this.dataRequirement;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GuidanceResponse setDataRequirement(List<DataRequirement> theDataRequirement) { 
      this.dataRequirement = theDataRequirement;
      return this;
    }

    public boolean hasDataRequirement() { 
      if (this.dataRequirement == null)
        return false;
      for (DataRequirement item : this.dataRequirement)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DataRequirement addDataRequirement() { //3
      DataRequirement t = new DataRequirement();
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return t;
    }

    public GuidanceResponse addDataRequirement(DataRequirement t) { //3
      if (t == null)
        return this;
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dataRequirement}, creating it if it does not already exist
     */
    public DataRequirement getDataRequirementFirstRep() { 
      if (getDataRequirement().isEmpty()) {
        addDataRequirement();
      }
      return getDataRequirement().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("requestIdentifier", "Identifier", "The identifier of the request associated with this response. If an identifier was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.", 0, 1, requestIdentifier));
        children.add(new Property("identifier", "Identifier", "Allows a service to provide  unique, business identifiers for the response.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("module[x]", "uri|canonical|CodeableConcept", "An identifier, CodeableConcept or canonical reference to the guidance that was requested.", 0, 1, module));
        children.add(new Property("status", "code", "The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.", 0, 1, status));
        children.add(new Property("subject", "Reference(Patient|Group)", "The patient for which the request was processed.", 0, 1, subject));
        children.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.", 0, 1, context));
        children.add(new Property("occurrenceDateTime", "dateTime", "Indicates when the guidance response was processed.", 0, 1, occurrenceDateTime));
        children.add(new Property("performer", "Reference(Device)", "Provides a reference to the device that performed the guidance.", 0, 1, performer));
        children.add(new Property("reasonCode", "CodeableConcept", "Describes the reason for the guidance response in coded or textual form.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        children.add(new Property("reasonReference", "Reference(Condition|Observation|DiagnosticReport|DocumentReference)", "Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        children.add(new Property("note", "Annotation", "Provides a mechanism to communicate additional information about the response.", 0, java.lang.Integer.MAX_VALUE, note));
        children.add(new Property("evaluationMessage", "Reference(OperationOutcome)", "Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.", 0, java.lang.Integer.MAX_VALUE, evaluationMessage));
        children.add(new Property("outputParameters", "Reference(Parameters)", "The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.", 0, 1, outputParameters));
        children.add(new Property("result", "Reference(CarePlan|RequestGroup)", "The actions, if any, produced by the evaluation of the artifact.", 0, 1, result));
        children.add(new Property("dataRequirement", "DataRequirement", "If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.", 0, java.lang.Integer.MAX_VALUE, dataRequirement));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -354233192: /*requestIdentifier*/  return new Property("requestIdentifier", "Identifier", "The identifier of the request associated with this response. If an identifier was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.", 0, 1, requestIdentifier);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Allows a service to provide  unique, business identifiers for the response.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -1552083308: /*module[x]*/  return new Property("module[x]", "uri|canonical|CodeableConcept", "An identifier, CodeableConcept or canonical reference to the guidance that was requested.", 0, 1, module);
        case -1068784020: /*module*/  return new Property("module[x]", "uri|canonical|CodeableConcept", "An identifier, CodeableConcept or canonical reference to the guidance that was requested.", 0, 1, module);
        case -1552089248: /*moduleUri*/  return new Property("module[x]", "uri|canonical|CodeableConcept", "An identifier, CodeableConcept or canonical reference to the guidance that was requested.", 0, 1, module);
        case -1153656856: /*moduleCanonical*/  return new Property("module[x]", "uri|canonical|CodeableConcept", "An identifier, CodeableConcept or canonical reference to the guidance that was requested.", 0, 1, module);
        case -1157899371: /*moduleCodeableConcept*/  return new Property("module[x]", "uri|canonical|CodeableConcept", "An identifier, CodeableConcept or canonical reference to the guidance that was requested.", 0, 1, module);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.", 0, 1, status);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Group)", "The patient for which the request was processed.", 0, 1, subject);
        case 951530927: /*context*/  return new Property("context", "Reference(Encounter|EpisodeOfCare)", "Allows the context of the guidance response to be provided if available. In a service context, this would likely be unavailable.", 0, 1, context);
        case -298443636: /*occurrenceDateTime*/  return new Property("occurrenceDateTime", "dateTime", "Indicates when the guidance response was processed.", 0, 1, occurrenceDateTime);
        case 481140686: /*performer*/  return new Property("performer", "Reference(Device)", "Provides a reference to the device that performed the guidance.", 0, 1, performer);
        case 722137681: /*reasonCode*/  return new Property("reasonCode", "CodeableConcept", "Describes the reason for the guidance response in coded or textual form.", 0, java.lang.Integer.MAX_VALUE, reasonCode);
        case -1146218137: /*reasonReference*/  return new Property("reasonReference", "Reference(Condition|Observation|DiagnosticReport|DocumentReference)", "Indicates the reason the request was initiated. This is typically provided as a parameter to the evaluation and echoed by the service, although for some use cases, such as subscription- or event-based scenarios, it may provide an indication of the cause for the response.", 0, java.lang.Integer.MAX_VALUE, reasonReference);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Provides a mechanism to communicate additional information about the response.", 0, java.lang.Integer.MAX_VALUE, note);
        case 1081619755: /*evaluationMessage*/  return new Property("evaluationMessage", "Reference(OperationOutcome)", "Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.", 0, java.lang.Integer.MAX_VALUE, evaluationMessage);
        case 525609419: /*outputParameters*/  return new Property("outputParameters", "Reference(Parameters)", "The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.", 0, 1, outputParameters);
        case -934426595: /*result*/  return new Property("result", "Reference(CarePlan|RequestGroup)", "The actions, if any, produced by the evaluation of the artifact.", 0, 1, result);
        case 629147193: /*dataRequirement*/  return new Property("dataRequirement", "DataRequirement", "If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.", 0, java.lang.Integer.MAX_VALUE, dataRequirement);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -354233192: /*requestIdentifier*/ return this.requestIdentifier == null ? new Base[0] : new Base[] {this.requestIdentifier}; // Identifier
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1068784020: /*module*/ return this.module == null ? new Base[0] : new Base[] {this.module}; // Type
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<GuidanceResponseStatus>
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -298443636: /*occurrenceDateTime*/ return this.occurrenceDateTime == null ? new Base[0] : new Base[] {this.occurrenceDateTime}; // DateTimeType
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 1081619755: /*evaluationMessage*/ return this.evaluationMessage == null ? new Base[0] : this.evaluationMessage.toArray(new Base[this.evaluationMessage.size()]); // Reference
        case 525609419: /*outputParameters*/ return this.outputParameters == null ? new Base[0] : new Base[] {this.outputParameters}; // Reference
        case -934426595: /*result*/ return this.result == null ? new Base[0] : new Base[] {this.result}; // Reference
        case 629147193: /*dataRequirement*/ return this.dataRequirement == null ? new Base[0] : this.dataRequirement.toArray(new Base[this.dataRequirement.size()]); // DataRequirement
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -354233192: // requestIdentifier
          this.requestIdentifier = castToIdentifier(value); // Identifier
          return value;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1068784020: // module
          this.module = castToType(value); // Type
          return value;
        case -892481550: // status
          value = new GuidanceResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<GuidanceResponseStatus>
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case -298443636: // occurrenceDateTime
          this.occurrenceDateTime = castToDateTime(value); // DateTimeType
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
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case 1081619755: // evaluationMessage
          this.getEvaluationMessage().add(castToReference(value)); // Reference
          return value;
        case 525609419: // outputParameters
          this.outputParameters = castToReference(value); // Reference
          return value;
        case -934426595: // result
          this.result = castToReference(value); // Reference
          return value;
        case 629147193: // dataRequirement
          this.getDataRequirement().add(castToDataRequirement(value)); // DataRequirement
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("requestIdentifier")) {
          this.requestIdentifier = castToIdentifier(value); // Identifier
        } else if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("module[x]")) {
          this.module = castToType(value); // Type
        } else if (name.equals("status")) {
          value = new GuidanceResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<GuidanceResponseStatus>
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("occurrenceDateTime")) {
          this.occurrenceDateTime = castToDateTime(value); // DateTimeType
        } else if (name.equals("performer")) {
          this.performer = castToReference(value); // Reference
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("evaluationMessage")) {
          this.getEvaluationMessage().add(castToReference(value));
        } else if (name.equals("outputParameters")) {
          this.outputParameters = castToReference(value); // Reference
        } else if (name.equals("result")) {
          this.result = castToReference(value); // Reference
        } else if (name.equals("dataRequirement")) {
          this.getDataRequirement().add(castToDataRequirement(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -354233192:  return getRequestIdentifier(); 
        case -1618432855:  return addIdentifier(); 
        case -1552083308:  return getModule(); 
        case -1068784020:  return getModule(); 
        case -892481550:  return getStatusElement();
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -298443636:  return getOccurrenceDateTimeElement();
        case 481140686:  return getPerformer(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case 3387378:  return addNote(); 
        case 1081619755:  return addEvaluationMessage(); 
        case 525609419:  return getOutputParameters(); 
        case -934426595:  return getResult(); 
        case 629147193:  return addDataRequirement(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -354233192: /*requestIdentifier*/ return new String[] {"Identifier"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1068784020: /*module*/ return new String[] {"uri", "canonical", "CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case -298443636: /*occurrenceDateTime*/ return new String[] {"dateTime"};
        case 481140686: /*performer*/ return new String[] {"Reference"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case 1081619755: /*evaluationMessage*/ return new String[] {"Reference"};
        case 525609419: /*outputParameters*/ return new String[] {"Reference"};
        case -934426595: /*result*/ return new String[] {"Reference"};
        case 629147193: /*dataRequirement*/ return new String[] {"DataRequirement"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("requestIdentifier")) {
          this.requestIdentifier = new Identifier();
          return this.requestIdentifier;
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("moduleUri")) {
          this.module = new UriType();
          return this.module;
        }
        else if (name.equals("moduleCanonical")) {
          this.module = new CanonicalType();
          return this.module;
        }
        else if (name.equals("moduleCodeableConcept")) {
          this.module = new CodeableConcept();
          return this.module;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.status");
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
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.occurrenceDateTime");
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
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("evaluationMessage")) {
          return addEvaluationMessage();
        }
        else if (name.equals("outputParameters")) {
          this.outputParameters = new Reference();
          return this.outputParameters;
        }
        else if (name.equals("result")) {
          this.result = new Reference();
          return this.result;
        }
        else if (name.equals("dataRequirement")) {
          return addDataRequirement();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "GuidanceResponse";

  }

      public GuidanceResponse copy() {
        GuidanceResponse dst = new GuidanceResponse();
        copyValues(dst);
        dst.requestIdentifier = requestIdentifier == null ? null : requestIdentifier.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.module = module == null ? null : module.copy();
        dst.status = status == null ? null : status.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrenceDateTime = occurrenceDateTime == null ? null : occurrenceDateTime.copy();
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
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (evaluationMessage != null) {
          dst.evaluationMessage = new ArrayList<Reference>();
          for (Reference i : evaluationMessage)
            dst.evaluationMessage.add(i.copy());
        };
        dst.outputParameters = outputParameters == null ? null : outputParameters.copy();
        dst.result = result == null ? null : result.copy();
        if (dataRequirement != null) {
          dst.dataRequirement = new ArrayList<DataRequirement>();
          for (DataRequirement i : dataRequirement)
            dst.dataRequirement.add(i.copy());
        };
        return dst;
      }

      protected GuidanceResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof GuidanceResponse))
          return false;
        GuidanceResponse o = (GuidanceResponse) other_;
        return compareDeep(requestIdentifier, o.requestIdentifier, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(module, o.module, true) && compareDeep(status, o.status, true) && compareDeep(subject, o.subject, true)
           && compareDeep(context, o.context, true) && compareDeep(occurrenceDateTime, o.occurrenceDateTime, true)
           && compareDeep(performer, o.performer, true) && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(note, o.note, true) && compareDeep(evaluationMessage, o.evaluationMessage, true)
           && compareDeep(outputParameters, o.outputParameters, true) && compareDeep(result, o.result, true)
           && compareDeep(dataRequirement, o.dataRequirement, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof GuidanceResponse))
          return false;
        GuidanceResponse o = (GuidanceResponse) other_;
        return compareValues(status, o.status, true) && compareValues(occurrenceDateTime, o.occurrenceDateTime, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(requestIdentifier, identifier
          , module, status, subject, context, occurrenceDateTime, performer, reasonCode
          , reasonReference, note, evaluationMessage, outputParameters, result, dataRequirement
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.GuidanceResponse;
   }

 /**
   * Search parameter: <b>request</b>
   * <p>
   * Description: <b>The identifier of the request associated with the response</b><br>
   * Type: <b>token</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="request", path="", description="The identifier of the request associated with the response", type="token" )
  public static final String SP_REQUEST = "request";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request</b>
   * <p>
   * Description: <b>The identifier of the request associated with the response</b><br>
   * Type: <b>token</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUEST = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUEST);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the guidance response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GuidanceResponse.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="GuidanceResponse.identifier", description="The identifier of the guidance response", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identifier of the guidance response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GuidanceResponse.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for guidance response results</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>GuidanceResponse.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="GuidanceResponse.subject.where(resolve() is Patient)", description="The identity of a patient to search for guidance response results", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to search for guidance response results</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>GuidanceResponse.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>GuidanceResponse:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("GuidanceResponse:patient").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The subject that the guidance response is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>GuidanceResponse.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="GuidanceResponse.subject", description="The subject that the guidance response is about", type="reference", target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The subject that the guidance response is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>GuidanceResponse.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>GuidanceResponse:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("GuidanceResponse:subject").toLocked();


}

