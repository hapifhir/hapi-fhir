















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

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.OrderOutcomeStatusEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>OrderResponse</b> Resource
 * (A response to an order)
 *
 * <p>
 * <b>Definition:</b>
 * A response to an order
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/OrderResponse">http://hl7.org/fhir/profiles/OrderResponse</a> 
 * </p>
 *
 */
@ResourceDef(name="OrderResponse", profile="http://hl7.org/fhir/profiles/OrderResponse", id="orderresponse")
public class OrderResponse extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>request</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>OrderResponse.request</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="request", path="OrderResponse.request", description="", type="reference"  )
	public static final String SP_REQUEST = "request";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>request</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>OrderResponse.request</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam REQUEST = new ReferenceClientParam(SP_REQUEST);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OrderResponse.request</b>".
	 */
	public static final Include INCLUDE_REQUEST = new Include("OrderResponse.request");

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>OrderResponse.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="OrderResponse.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>OrderResponse.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>who</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>OrderResponse.who</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="who", path="OrderResponse.who", description="", type="reference"  )
	public static final String SP_WHO = "who";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>who</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>OrderResponse.who</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam WHO = new ReferenceClientParam(SP_WHO);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OrderResponse.who</b>".
	 */
	public static final Include INCLUDE_WHO = new Include("OrderResponse.who");

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>OrderResponse.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="OrderResponse.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>OrderResponse.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>fulfillment</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>OrderResponse.fulfillment</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="fulfillment", path="OrderResponse.fulfillment", description="", type="reference"  )
	public static final String SP_FULFILLMENT = "fulfillment";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>fulfillment</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>OrderResponse.fulfillment</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam FULFILLMENT = new ReferenceClientParam(SP_FULFILLMENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OrderResponse.fulfillment</b>".
	 */
	public static final Include INCLUDE_FULFILLMENT = new Include("OrderResponse.fulfillment");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Identifiers assigned to this order by the orderer or by the receiver",
		formalDefinition="Identifiers assigned to this order. The identifiers are usually assigned by the system responding to the order, but they may be provided or added to by other systems"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="request", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Order.class	})
	@Description(
		shortDefinition="The order that this is a response to",
		formalDefinition="A reference to the order that this is in response to"
	)
	private ResourceReferenceDt myRequest;
	
	@Child(name="date", type=DateTimeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="When the response was made",
		formalDefinition="The date and time at which this order response was made (created/posted)"
	)
	private DateTimeDt myDate;
	
	@Child(name="who", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Who made the response",
		formalDefinition="The person, organization, or device credited with making the response"
	)
	private ResourceReferenceDt myWho;
	
	@Child(name="authority", order=4, min=0, max=1, type={
		CodeableConceptDt.class, 		IResource.class	})
	@Description(
		shortDefinition="If required by policy",
		formalDefinition="A reference to an authority policy that is the reason for the response. Usually this is used when the order is rejected, to provide a reason for rejection"
	)
	private IDatatype myAuthority;
	
	@Child(name="code", type=CodeDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="pending | review | rejected | error | accepted | cancelled | replaced | aborted | complete",
		formalDefinition="What this response says about the status of the original order"
	)
	private BoundCodeDt<OrderOutcomeStatusEnum> myCode;
	
	@Child(name="description", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Additional description of the response",
		formalDefinition="Additional description about the response - e.g. a text description provided by a human user when making decisions about the order"
	)
	private StringDt myDescription;
	
	@Child(name="fulfillment", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="Details of the outcome of performing the order",
		formalDefinition="Links to resources that provide details of the outcome of performing the order. E.g. Diagnostic Reports in a response that is made to an order that referenced a diagnostic order"
	)
	private java.util.List<ResourceReferenceDt> myFulfillment;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myRequest,  myDate,  myWho,  myAuthority,  myCode,  myDescription,  myFulfillment);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myRequest, myDate, myWho, myAuthority, myCode, myDescription, myFulfillment);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order. The identifiers are usually assigned by the system responding to the order, but they may be provided or added to by other systems
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order. The identifiers are usually assigned by the system responding to the order, but they may be provided or added to by other systems
     * </p> 
	 */
	public OrderResponse setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order. The identifiers are usually assigned by the system responding to the order, but they may be provided or added to by other systems
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order. The identifiers are usually assigned by the system responding to the order, but they may be provided or added to by other systems
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order. The identifiers are usually assigned by the system responding to the order, but they may be provided or added to by other systems
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public OrderResponse addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Identifiers assigned to this order by the orderer or by the receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order. The identifiers are usually assigned by the system responding to the order, but they may be provided or added to by other systems
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public OrderResponse addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>request</b> (The order that this is a response to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the order that this is in response to
     * </p> 
	 */
	public ResourceReferenceDt getRequest() {  
		if (myRequest == null) {
			myRequest = new ResourceReferenceDt();
		}
		return myRequest;
	}

	/**
	 * Sets the value(s) for <b>request</b> (The order that this is a response to)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to the order that this is in response to
     * </p> 
	 */
	public OrderResponse setRequest(ResourceReferenceDt theValue) {
		myRequest = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>date</b> (When the response was made).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time at which this order response was made (created/posted)
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (When the response was made)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time at which this order response was made (created/posted)
     * </p> 
	 */
	public OrderResponse setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (When the response was made)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time at which this order response was made (created/posted)
     * </p> 
	 */
	public OrderResponse setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When the response was made)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time at which this order response was made (created/posted)
     * </p> 
	 */
	public OrderResponse setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>who</b> (Who made the response).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person, organization, or device credited with making the response
     * </p> 
	 */
	public ResourceReferenceDt getWho() {  
		if (myWho == null) {
			myWho = new ResourceReferenceDt();
		}
		return myWho;
	}

	/**
	 * Sets the value(s) for <b>who</b> (Who made the response)
	 *
     * <p>
     * <b>Definition:</b>
     * The person, organization, or device credited with making the response
     * </p> 
	 */
	public OrderResponse setWho(ResourceReferenceDt theValue) {
		myWho = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>authority[x]</b> (If required by policy).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an authority policy that is the reason for the response. Usually this is used when the order is rejected, to provide a reason for rejection
     * </p> 
	 */
	public IDatatype getAuthority() {  
		return myAuthority;
	}

	/**
	 * Sets the value(s) for <b>authority[x]</b> (If required by policy)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an authority policy that is the reason for the response. Usually this is used when the order is rejected, to provide a reason for rejection
     * </p> 
	 */
	public OrderResponse setAuthority(IDatatype theValue) {
		myAuthority = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>code</b> (pending | review | rejected | error | accepted | cancelled | replaced | aborted | complete).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * What this response says about the status of the original order
     * </p> 
	 */
	public BoundCodeDt<OrderOutcomeStatusEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<OrderOutcomeStatusEnum>(OrderOutcomeStatusEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (pending | review | rejected | error | accepted | cancelled | replaced | aborted | complete)
	 *
     * <p>
     * <b>Definition:</b>
     * What this response says about the status of the original order
     * </p> 
	 */
	public OrderResponse setCode(BoundCodeDt<OrderOutcomeStatusEnum> theValue) {
		myCode = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>code</b> (pending | review | rejected | error | accepted | cancelled | replaced | aborted | complete)
	 *
     * <p>
     * <b>Definition:</b>
     * What this response says about the status of the original order
     * </p> 
	 */
	public OrderResponse setCode(OrderOutcomeStatusEnum theValue) {
		getCode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Additional description of the response).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional description about the response - e.g. a text description provided by a human user when making decisions about the order
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Additional description of the response)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional description about the response - e.g. a text description provided by a human user when making decisions about the order
     * </p> 
	 */
	public OrderResponse setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Additional description of the response)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional description about the response - e.g. a text description provided by a human user when making decisions about the order
     * </p> 
	 */
	public OrderResponse setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>fulfillment</b> (Details of the outcome of performing the order).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Links to resources that provide details of the outcome of performing the order. E.g. Diagnostic Reports in a response that is made to an order that referenced a diagnostic order
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getFulfillment() {  
		if (myFulfillment == null) {
			myFulfillment = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myFulfillment;
	}

	/**
	 * Sets the value(s) for <b>fulfillment</b> (Details of the outcome of performing the order)
	 *
     * <p>
     * <b>Definition:</b>
     * Links to resources that provide details of the outcome of performing the order. E.g. Diagnostic Reports in a response that is made to an order that referenced a diagnostic order
     * </p> 
	 */
	public OrderResponse setFulfillment(java.util.List<ResourceReferenceDt> theValue) {
		myFulfillment = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>fulfillment</b> (Details of the outcome of performing the order)
	 *
     * <p>
     * <b>Definition:</b>
     * Links to resources that provide details of the outcome of performing the order. E.g. Diagnostic Reports in a response that is made to an order that referenced a diagnostic order
     * </p> 
	 */
	public ResourceReferenceDt addFulfillment() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getFulfillment().add(newType);
		return newType; 
	}
  


}
