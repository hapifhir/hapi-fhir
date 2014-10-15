















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ResponseTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;


/**
 * HAPI/FHIR <b>MessageHeader</b> Resource
 * (A resource that describes a message that is exchanged between systems)
 *
 * <p>
 * <b>Definition:</b>
 * The header for a message exchange that is either requesting or responding to an action.  The resource(s) that are the subject of the action as well as other Information related to the action are typically transmitted in a bundle in which the MessageHeader resource instance is the first resource in the bundle
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Many implementations are not prepared to use REST and need a messaging based infrastructure
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/MessageHeader">http://hl7.org/fhir/profiles/MessageHeader</a> 
 * </p>
 *
 */
@ResourceDef(name="MessageHeader", profile="http://hl7.org/fhir/profiles/MessageHeader", id="messageheader")
public class MessageHeader extends BaseResource implements IResource {


	@Child(name="identifier", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Id of this message",
		formalDefinition="The identifier of this message"
	)
	private IdDt myIdentifier;
	
	@Child(name="timestamp", type=InstantDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Time that the message was sent",
		formalDefinition="The time that the message was sent"
	)
	private InstantDt myTimestamp;
	
	@Child(name="event", type=CodingDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Code for the event this message represents",
		formalDefinition="Code that identifies the event this message represents and connects it with it's definition. Events defined as part of the FHIR specification have the system value \"http://hl7.org/fhir/message-type\""
	)
	private CodingDt myEvent;
	
	@Child(name="response", order=3, min=0, max=1)	
	@Description(
		shortDefinition="If this is a reply to prior message",
		formalDefinition="Information about the message that this message is a response to.  Only present if this message is a response."
	)
	private Response myResponse;
	
	@Child(name="source", order=4, min=1, max=1)	
	@Description(
		shortDefinition="Message Source Application",
		formalDefinition="The source application from which this message originated"
	)
	private Source mySource;
	
	@Child(name="destination", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Message Destination Application(s)",
		formalDefinition="The destination application which the message is intended for"
	)
	private java.util.List<Destination> myDestination;
	
	@Child(name="enterer", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="The source of the data entry",
		formalDefinition="The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions"
	)
	private ResourceReferenceDt myEnterer;
	
	@Child(name="author", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="The source of the decision",
		formalDefinition="The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="receiver", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Intended \"real-world\" recipient for the data",
		formalDefinition="Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient."
	)
	private ResourceReferenceDt myReceiver;
	
	@Child(name="responsible", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Final responsibility for event",
		formalDefinition="The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party"
	)
	private ResourceReferenceDt myResponsible;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Cause of event",
		formalDefinition="Coded indication of the cause for the event - indicates  a reason for the occurance of the event that is a focus of this message"
	)
	private CodeableConceptDt myReason;
	
	@Child(name="data", order=11, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="The actual content of the message",
		formalDefinition="The actual data of the message - a reference to the root/focus class of the event."
	)
	private java.util.List<ResourceReferenceDt> myData;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myTimestamp,  myEvent,  myResponse,  mySource,  myDestination,  myEnterer,  myAuthor,  myReceiver,  myResponsible,  myReason,  myData);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myTimestamp, myEvent, myResponse, mySource, myDestination, myEnterer, myAuthor, myReceiver, myResponsible, myReason, myData);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Id of this message).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier of this message
     * </p> 
	 */
	public IdDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Id of this message)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier of this message
     * </p> 
	 */
	public MessageHeader setIdentifier(IdDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Id of this message)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier of this message
     * </p> 
	 */
	public MessageHeader setIdentifier( String theId) {
		myIdentifier = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>timestamp</b> (Time that the message was sent).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time that the message was sent
     * </p> 
	 */
	public InstantDt getTimestamp() {  
		if (myTimestamp == null) {
			myTimestamp = new InstantDt();
		}
		return myTimestamp;
	}

	/**
	 * Sets the value(s) for <b>timestamp</b> (Time that the message was sent)
	 *
     * <p>
     * <b>Definition:</b>
     * The time that the message was sent
     * </p> 
	 */
	public MessageHeader setTimestamp(InstantDt theValue) {
		myTimestamp = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>timestamp</b> (Time that the message was sent)
	 *
     * <p>
     * <b>Definition:</b>
     * The time that the message was sent
     * </p> 
	 */
	public MessageHeader setTimestamp( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myTimestamp = new InstantDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>timestamp</b> (Time that the message was sent)
	 *
     * <p>
     * <b>Definition:</b>
     * The time that the message was sent
     * </p> 
	 */
	public MessageHeader setTimestampWithMillisPrecision( Date theDate) {
		myTimestamp = new InstantDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>event</b> (Code for the event this message represents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the event this message represents and connects it with it's definition. Events defined as part of the FHIR specification have the system value \"http://hl7.org/fhir/message-type\"
     * </p> 
	 */
	public CodingDt getEvent() {  
		if (myEvent == null) {
			myEvent = new CodingDt();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (Code for the event this message represents)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the event this message represents and connects it with it's definition. Events defined as part of the FHIR specification have the system value \"http://hl7.org/fhir/message-type\"
     * </p> 
	 */
	public MessageHeader setEvent(CodingDt theValue) {
		myEvent = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>response</b> (If this is a reply to prior message).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the message that this message is a response to.  Only present if this message is a response.
     * </p> 
	 */
	public Response getResponse() {  
		if (myResponse == null) {
			myResponse = new Response();
		}
		return myResponse;
	}

	/**
	 * Sets the value(s) for <b>response</b> (If this is a reply to prior message)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the message that this message is a response to.  Only present if this message is a response.
     * </p> 
	 */
	public MessageHeader setResponse(Response theValue) {
		myResponse = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>source</b> (Message Source Application).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The source application from which this message originated
     * </p> 
	 */
	public Source getSource() {  
		if (mySource == null) {
			mySource = new Source();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (Message Source Application)
	 *
     * <p>
     * <b>Definition:</b>
     * The source application from which this message originated
     * </p> 
	 */
	public MessageHeader setSource(Source theValue) {
		mySource = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>destination</b> (Message Destination Application(s)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	public java.util.List<Destination> getDestination() {  
		if (myDestination == null) {
			myDestination = new java.util.ArrayList<Destination>();
		}
		return myDestination;
	}

	/**
	 * Sets the value(s) for <b>destination</b> (Message Destination Application(s))
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	public MessageHeader setDestination(java.util.List<Destination> theValue) {
		myDestination = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>destination</b> (Message Destination Application(s))
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	public Destination addDestination() {
		Destination newType = new Destination();
		getDestination().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>destination</b> (Message Destination Application(s)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	public Destination getDestinationFirstRep() {
		if (getDestination().isEmpty()) {
			return addDestination();
		}
		return getDestination().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>enterer</b> (The source of the data entry).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions
     * </p> 
	 */
	public ResourceReferenceDt getEnterer() {  
		if (myEnterer == null) {
			myEnterer = new ResourceReferenceDt();
		}
		return myEnterer;
	}

	/**
	 * Sets the value(s) for <b>enterer</b> (The source of the data entry)
	 *
     * <p>
     * <b>Definition:</b>
     * The person or device that performed the data entry leading to this message. Where there is more than one candidate, pick the most proximal to the message. Can provide other enterers in extensions
     * </p> 
	 */
	public MessageHeader setEnterer(ResourceReferenceDt theValue) {
		myEnterer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>author</b> (The source of the decision).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new ResourceReferenceDt();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (The source of the decision)
	 *
     * <p>
     * <b>Definition:</b>
     * The logical author of the message - the person or device that decided the described event should happen. Where there is more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions
     * </p> 
	 */
	public MessageHeader setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>receiver</b> (Intended \"real-world\" recipient for the data).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.
     * </p> 
	 */
	public ResourceReferenceDt getReceiver() {  
		if (myReceiver == null) {
			myReceiver = new ResourceReferenceDt();
		}
		return myReceiver;
	}

	/**
	 * Sets the value(s) for <b>receiver</b> (Intended \"real-world\" recipient for the data)
	 *
     * <p>
     * <b>Definition:</b>
     * Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific application isn't sufficient.
     * </p> 
	 */
	public MessageHeader setReceiver(ResourceReferenceDt theValue) {
		myReceiver = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>responsible</b> (Final responsibility for event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party
     * </p> 
	 */
	public ResourceReferenceDt getResponsible() {  
		if (myResponsible == null) {
			myResponsible = new ResourceReferenceDt();
		}
		return myResponsible;
	}

	/**
	 * Sets the value(s) for <b>responsible</b> (Final responsibility for event)
	 *
     * <p>
     * <b>Definition:</b>
     * The person or organization that accepts overall responsibility for the contents of the message. The implication is that the message event happened under the policies of the responsible party
     * </p> 
	 */
	public MessageHeader setResponsible(ResourceReferenceDt theValue) {
		myResponsible = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reason</b> (Cause of event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coded indication of the cause for the event - indicates  a reason for the occurance of the event that is a focus of this message
     * </p> 
	 */
	public CodeableConceptDt getReason() {  
		if (myReason == null) {
			myReason = new CodeableConceptDt();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (Cause of event)
	 *
     * <p>
     * <b>Definition:</b>
     * Coded indication of the cause for the event - indicates  a reason for the occurance of the event that is a focus of this message
     * </p> 
	 */
	public MessageHeader setReason(CodeableConceptDt theValue) {
		myReason = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>data</b> (The actual content of the message).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the message - a reference to the root/focus class of the event.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getData() {  
		if (myData == null) {
			myData = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myData;
	}

	/**
	 * Sets the value(s) for <b>data</b> (The actual content of the message)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the message - a reference to the root/focus class of the event.
     * </p> 
	 */
	public MessageHeader setData(java.util.List<ResourceReferenceDt> theValue) {
		myData = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>data</b> (The actual content of the message)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual data of the message - a reference to the root/focus class of the event.
     * </p> 
	 */
	public ResourceReferenceDt addData() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getData().add(newType);
		return newType; 
	}
  
	/**
	 * Block class for child element: <b>MessageHeader.response</b> (If this is a reply to prior message)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the message that this message is a response to.  Only present if this message is a response.
     * </p> 
	 */
	@Block()	
	public static class Response extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Id of original message",
		formalDefinition="The id of the message that this message is a response to"
	)
	private IdDt myIdentifier;
	
	@Child(name="code", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="ok | transient-error | fatal-error",
		formalDefinition="Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not"
	)
	private BoundCodeDt<ResponseTypeEnum> myCode;
	
	@Child(name="details", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.OperationOutcome.class	})
	@Description(
		shortDefinition="Specific list of hints/warnings/errors",
		formalDefinition="Full details of any issues found in the message"
	)
	private ResourceReferenceDt myDetails;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myCode,  myDetails);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myCode, myDetails);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Id of original message).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The id of the message that this message is a response to
     * </p> 
	 */
	public IdDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Id of original message)
	 *
     * <p>
     * <b>Definition:</b>
     * The id of the message that this message is a response to
     * </p> 
	 */
	public Response setIdentifier(IdDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Id of original message)
	 *
     * <p>
     * <b>Definition:</b>
     * The id of the message that this message is a response to
     * </p> 
	 */
	public Response setIdentifier( String theId) {
		myIdentifier = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (ok | transient-error | fatal-error).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not
     * </p> 
	 */
	public BoundCodeDt<ResponseTypeEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<ResponseTypeEnum>(ResponseTypeEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (ok | transient-error | fatal-error)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not
     * </p> 
	 */
	public Response setCode(BoundCodeDt<ResponseTypeEnum> theValue) {
		myCode = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>code</b> (ok | transient-error | fatal-error)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be resent or not
     * </p> 
	 */
	public Response setCode(ResponseTypeEnum theValue) {
		getCode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>details</b> (Specific list of hints/warnings/errors).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Full details of any issues found in the message
     * </p> 
	 */
	public ResourceReferenceDt getDetails() {  
		if (myDetails == null) {
			myDetails = new ResourceReferenceDt();
		}
		return myDetails;
	}

	/**
	 * Sets the value(s) for <b>details</b> (Specific list of hints/warnings/errors)
	 *
     * <p>
     * <b>Definition:</b>
     * Full details of any issues found in the message
     * </p> 
	 */
	public Response setDetails(ResourceReferenceDt theValue) {
		myDetails = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>MessageHeader.source</b> (Message Source Application)
	 *
     * <p>
     * <b>Definition:</b>
     * The source application from which this message originated
     * </p> 
	 */
	@Block()	
	public static class Source extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Name of system",
		formalDefinition="Human-readable name for the target system"
	)
	private StringDt myName;
	
	@Child(name="software", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Name of software running the system",
		formalDefinition="May include configuration or other information useful in debugging."
	)
	private StringDt mySoftware;
	
	@Child(name="version", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Version of software running",
		formalDefinition="Can convey versions of multiple systems in situations where a message passes through multiple hands."
	)
	private StringDt myVersion;
	
	@Child(name="contact", type=ContactDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Human contact for problems",
		formalDefinition="An e-mail, phone, website or other contact point to use to resolve issues with message communications."
	)
	private ContactDt myContact;
	
	@Child(name="endpoint", type=UriDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Actual message source address or id",
		formalDefinition="Identifies the routing target to send acknowledgements to."
	)
	private UriDt myEndpoint;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  mySoftware,  myVersion,  myContact,  myEndpoint);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, mySoftware, myVersion, myContact, myEndpoint);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Name of system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the target system
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name of system)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the target system
     * </p> 
	 */
	public Source setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name of system)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the target system
     * </p> 
	 */
	public Source setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>software</b> (Name of software running the system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * May include configuration or other information useful in debugging.
     * </p> 
	 */
	public StringDt getSoftware() {  
		if (mySoftware == null) {
			mySoftware = new StringDt();
		}
		return mySoftware;
	}

	/**
	 * Sets the value(s) for <b>software</b> (Name of software running the system)
	 *
     * <p>
     * <b>Definition:</b>
     * May include configuration or other information useful in debugging.
     * </p> 
	 */
	public Source setSoftware(StringDt theValue) {
		mySoftware = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>software</b> (Name of software running the system)
	 *
     * <p>
     * <b>Definition:</b>
     * May include configuration or other information useful in debugging.
     * </p> 
	 */
	public Source setSoftware( String theString) {
		mySoftware = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Version of software running).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Can convey versions of multiple systems in situations where a message passes through multiple hands.
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Version of software running)
	 *
     * <p>
     * <b>Definition:</b>
     * Can convey versions of multiple systems in situations where a message passes through multiple hands.
     * </p> 
	 */
	public Source setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>version</b> (Version of software running)
	 *
     * <p>
     * <b>Definition:</b>
     * Can convey versions of multiple systems in situations where a message passes through multiple hands.
     * </p> 
	 */
	public Source setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contact</b> (Human contact for problems).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
     * </p> 
	 */
	public ContactDt getContact() {  
		if (myContact == null) {
			myContact = new ContactDt();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> (Human contact for problems)
	 *
     * <p>
     * <b>Definition:</b>
     * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
     * </p> 
	 */
	public Source setContact(ContactDt theValue) {
		myContact = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>contact</b> (Human contact for problems)
	 *
     * <p>
     * <b>Definition:</b>
     * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
     * </p> 
	 */
	public Source setContact( ContactUseEnum theContactUse,  String theValue) {
		myContact = new ContactDt(theContactUse, theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>contact</b> (Human contact for problems)
	 *
     * <p>
     * <b>Definition:</b>
     * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
     * </p> 
	 */
	public Source setContact( String theValue) {
		myContact = new ContactDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>endpoint</b> (Actual message source address or id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the routing target to send acknowledgements to.
     * </p> 
	 */
	public UriDt getEndpoint() {  
		if (myEndpoint == null) {
			myEndpoint = new UriDt();
		}
		return myEndpoint;
	}

	/**
	 * Sets the value(s) for <b>endpoint</b> (Actual message source address or id)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the routing target to send acknowledgements to.
     * </p> 
	 */
	public Source setEndpoint(UriDt theValue) {
		myEndpoint = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>endpoint</b> (Actual message source address or id)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the routing target to send acknowledgements to.
     * </p> 
	 */
	public Source setEndpoint( String theUri) {
		myEndpoint = new UriDt(theUri); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>MessageHeader.destination</b> (Message Destination Application(s))
	 *
     * <p>
     * <b>Definition:</b>
     * The destination application which the message is intended for
     * </p> 
	 */
	@Block()	
	public static class Destination extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Name of system",
		formalDefinition="Human-readable name for the source system"
	)
	private StringDt myName;
	
	@Child(name="target", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Particular delivery destination within the destination",
		formalDefinition="Identifies the target end system in situations where the initial message transmission is to an intermediary system."
	)
	private ResourceReferenceDt myTarget;
	
	@Child(name="endpoint", type=UriDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Actual destination address or id",
		formalDefinition="Indicates where the message should be routed to."
	)
	private UriDt myEndpoint;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myTarget,  myEndpoint);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myTarget, myEndpoint);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Name of system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the source system
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name of system)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the source system
     * </p> 
	 */
	public Destination setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Name of system)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable name for the source system
     * </p> 
	 */
	public Destination setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>target</b> (Particular delivery destination within the destination).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the target end system in situations where the initial message transmission is to an intermediary system.
     * </p> 
	 */
	public ResourceReferenceDt getTarget() {  
		if (myTarget == null) {
			myTarget = new ResourceReferenceDt();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (Particular delivery destination within the destination)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the target end system in situations where the initial message transmission is to an intermediary system.
     * </p> 
	 */
	public Destination setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>endpoint</b> (Actual destination address or id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where the message should be routed to.
     * </p> 
	 */
	public UriDt getEndpoint() {  
		if (myEndpoint == null) {
			myEndpoint = new UriDt();
		}
		return myEndpoint;
	}

	/**
	 * Sets the value(s) for <b>endpoint</b> (Actual destination address or id)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where the message should be routed to.
     * </p> 
	 */
	public Destination setEndpoint(UriDt theValue) {
		myEndpoint = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>endpoint</b> (Actual destination address or id)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates where the message should be routed to.
     * </p> 
	 */
	public Destination setEndpoint( String theUri) {
		myEndpoint = new UriDt(theUri); 
		return this; 
	}

 

	}

	@Override
	public ResourceTypeEnum getResourceType() {
		return ResourceTypeEnum.MESSAGEHEADER;
	}


}
