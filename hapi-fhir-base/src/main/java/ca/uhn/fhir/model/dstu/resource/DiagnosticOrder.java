















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
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderPriorityEnum;
import ca.uhn.fhir.model.dstu.valueset.DiagnosticOrderStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.CompositeClientParam;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>DiagnosticOrder</b> Resource
 * (A request for a diagnostic service)
 *
 * <p>
 * <b>Definition:</b>
 * A request for a diagnostic investigation service to be performed
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DiagnosticOrder">http://hl7.org/fhir/profiles/DiagnosticOrder</a> 
 * </p>
 *
 */
@ResourceDef(name="DiagnosticOrder", profile="http://hl7.org/fhir/profiles/DiagnosticOrder", id="diagnosticorder")
public class DiagnosticOrder extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>actor</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.event.actor | DiagnosticOrder.item.event.actor</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="actor", path="DiagnosticOrder.event.actor | DiagnosticOrder.item.event.actor", description="", type="reference"  )
	public static final String SP_ACTOR = "actor";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>actor</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.event.actor | DiagnosticOrder.item.event.actor</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ACTOR = new ReferenceClientParam(SP_ACTOR);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.event.actor</b>".
	 */
	public static final Include INCLUDE_EVENT_ACTOR = new Include("DiagnosticOrder.event.actor");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.item.event.actor</b>".
	 */
	public static final Include INCLUDE_ITEM_EVENT_ACTOR = new Include("DiagnosticOrder.item.event.actor");

	/**
	 * Search parameter constant for <b>bodysite</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.bodySite</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="bodysite", path="DiagnosticOrder.item.bodySite", description="", type="token"  )
	public static final String SP_BODYSITE = "bodysite";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>bodysite</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.bodySite</b><br/>
	 * </p>
	 */
	public static final TokenClientParam BODYSITE = new TokenClientParam(SP_BODYSITE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="DiagnosticOrder.item.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>event-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticOrder.event.dateTime</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event-date", path="DiagnosticOrder.event.dateTime", description="", type="date"  )
	public static final String SP_EVENT_DATE = "event-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticOrder.event.dateTime</b><br/>
	 * </p>
	 */
	public static final DateClientParam EVENT_DATE = new DateClientParam(SP_EVENT_DATE);

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="DiagnosticOrder.encounter", description="", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("DiagnosticOrder.encounter");

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DiagnosticOrder.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>item-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticOrder.item.event.dateTime</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item-date", path="DiagnosticOrder.item.event.dateTime", description="", type="date"  )
	public static final String SP_ITEM_DATE = "item-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DiagnosticOrder.item.event.dateTime</b><br/>
	 * </p>
	 */
	public static final DateClientParam ITEM_DATE = new DateClientParam(SP_ITEM_DATE);

	/**
	 * Search parameter constant for <b>item-past-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.event.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item-past-status", path="DiagnosticOrder.item.event.status", description="", type="token"  )
	public static final String SP_ITEM_PAST_STATUS = "item-past-status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item-past-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.event.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ITEM_PAST_STATUS = new TokenClientParam(SP_ITEM_PAST_STATUS);

	/**
	 * Search parameter constant for <b>item-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item-status", path="DiagnosticOrder.item.status", description="", type="token"  )
	public static final String SP_ITEM_STATUS = "item-status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.item.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ITEM_STATUS = new TokenClientParam(SP_ITEM_STATUS);

	/**
	 * Search parameter constant for <b>orderer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.orderer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="orderer", path="DiagnosticOrder.orderer", description="", type="reference"  )
	public static final String SP_ORDERER = "orderer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>orderer</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.orderer</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ORDERER = new ReferenceClientParam(SP_ORDERER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.orderer</b>".
	 */
	public static final Include INCLUDE_ORDERER = new Include("DiagnosticOrder.orderer");

	/**
	 * Search parameter constant for <b>event-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.event.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event-status", path="DiagnosticOrder.event.status", description="", type="token"  )
	public static final String SP_EVENT_STATUS = "event-status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event-status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.event.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EVENT_STATUS = new TokenClientParam(SP_EVENT_STATUS);

	/**
	 * Search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.specimen | DiagnosticOrder.item.specimen</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="specimen", path="DiagnosticOrder.specimen | DiagnosticOrder.item.specimen", description="", type="reference"  )
	public static final String SP_SPECIMEN = "specimen";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>specimen</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.specimen | DiagnosticOrder.item.specimen</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SPECIMEN = new ReferenceClientParam(SP_SPECIMEN);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.specimen</b>".
	 */
	public static final Include INCLUDE_SPECIMEN = new Include("DiagnosticOrder.specimen");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.item.specimen</b>".
	 */
	public static final Include INCLUDE_ITEM_SPECIMEN = new Include("DiagnosticOrder.item.specimen");

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DiagnosticOrder.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DiagnosticOrder.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DiagnosticOrder.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DiagnosticOrder.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DiagnosticOrder.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DiagnosticOrder.subject");

	/**
	 * Search parameter constant for <b>item-past-status-item-date</b>
	 * <p>
	 * Description: <b>A combination of item-past-status and item-date</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>item-past-status & item-date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="item-past-status-item-date", path="item-past-status & item-date", description="A combination of item-past-status and item-date", type="composite"  , compositeOf={  "item-past-status",  "item-date" }  )
	public static final String SP_ITEM_PAST_STATUS_ITEM_DATE = "item-past-status-item-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>item-past-status-item-date</b>
	 * <p>
	 * Description: <b>A combination of item-past-status and item-date</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>item-past-status & item-date</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<TokenClientParam, DateClientParam> ITEM_PAST_STATUS_ITEM_DATE = new CompositeClientParam<TokenClientParam, DateClientParam>(SP_ITEM_PAST_STATUS_ITEM_DATE);

	/**
	 * Search parameter constant for <b>event-status-event-date</b>
	 * <p>
	 * Description: <b>A combination of past-status and date</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>event-status & event-date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event-status-event-date", path="event-status & event-date", description="A combination of past-status and date", type="composite"  , compositeOf={  "event-status",  "event-date" }  )
	public static final String SP_EVENT_STATUS_EVENT_DATE = "event-status-event-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event-status-event-date</b>
	 * <p>
	 * Description: <b>A combination of past-status and date</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>event-status & event-date</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<TokenClientParam, DateClientParam> EVENT_STATUS_EVENT_DATE = new CompositeClientParam<TokenClientParam, DateClientParam>(SP_EVENT_STATUS_EVENT_DATE);


	@Child(name="subject", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Location.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Who and/or what test is about",
		formalDefinition="Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="orderer", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who ordered the test",
		formalDefinition="The practitioner that holds legal responsibility for ordering the investigation"
	)
	private ResourceReferenceDt myOrderer;
	
	@Child(name="identifier", type=IdentifierDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Identifiers assigned to this order",
		formalDefinition="Identifiers assigned to this order by the order or by the receiver"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="encounter", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Encounter.class	})
	@Description(
		shortDefinition="The encounter that this diagnostic order is associated with",
		formalDefinition="An encounter that provides additional informaton about the healthcare context in which this request is made"
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="clinicalNotes", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Explanation/Justification for test",
		formalDefinition="An explanation or justification for why this diagnostic investigation is being requested"
	)
	private StringDt myClinicalNotes;
	
	@Child(name="specimen", order=5, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Specimen.class	})
	@Description(
		shortDefinition="If the whole order relates to specific specimens",
		formalDefinition="One or more specimens that the diagnostic investigation is about"
	)
	private java.util.List<ResourceReferenceDt> mySpecimen;
	
	@Child(name="status", type=CodeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="requested | received | accepted | in progress | review | completed | suspended | rejected | failed",
		formalDefinition="The status of the order"
	)
	private BoundCodeDt<DiagnosticOrderStatusEnum> myStatus;
	
	@Child(name="priority", type=CodeDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="routine | urgent | stat | asap",
		formalDefinition="The clinical priority associated with this order"
	)
	private BoundCodeDt<DiagnosticOrderPriorityEnum> myPriority;
	
	@Child(name="event", order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A list of events of interest in the lifecycle",
		formalDefinition="A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed"
	)
	private java.util.List<Event> myEvent;
	
	@Child(name="item", order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="The items the orderer requested",
		formalDefinition="The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested"
	)
	private java.util.List<Item> myItem;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySubject,  myOrderer,  myIdentifier,  myEncounter,  myClinicalNotes,  mySpecimen,  myStatus,  myPriority,  myEvent,  myItem);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySubject, myOrderer, myIdentifier, myEncounter, myClinicalNotes, mySpecimen, myStatus, myPriority, myEvent, myItem);
	}

	/**
	 * Gets the value(s) for <b>subject</b> (Who and/or what test is about).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who and/or what test is about)
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)
     * </p> 
	 */
	public DiagnosticOrder setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>orderer</b> (Who ordered the test).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner that holds legal responsibility for ordering the investigation
     * </p> 
	 */
	public ResourceReferenceDt getOrderer() {  
		if (myOrderer == null) {
			myOrderer = new ResourceReferenceDt();
		}
		return myOrderer;
	}

	/**
	 * Sets the value(s) for <b>orderer</b> (Who ordered the test)
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner that holds legal responsibility for ordering the investigation
     * </p> 
	 */
	public DiagnosticOrder setOrderer(ResourceReferenceDt theValue) {
		myOrderer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> (Identifiers assigned to this order).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order by the order or by the receiver
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Identifiers assigned to this order)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order by the order or by the receiver
     * </p> 
	 */
	public DiagnosticOrder setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Identifiers assigned to this order)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order by the order or by the receiver
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Identifiers assigned to this order),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order by the order or by the receiver
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Identifiers assigned to this order)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order by the order or by the receiver
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DiagnosticOrder addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Identifiers assigned to this order)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifiers assigned to this order by the order or by the receiver
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DiagnosticOrder addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>encounter</b> (The encounter that this diagnostic order is associated with).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An encounter that provides additional informaton about the healthcare context in which this request is made
     * </p> 
	 */
	public ResourceReferenceDt getEncounter() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}

	/**
	 * Sets the value(s) for <b>encounter</b> (The encounter that this diagnostic order is associated with)
	 *
     * <p>
     * <b>Definition:</b>
     * An encounter that provides additional informaton about the healthcare context in which this request is made
     * </p> 
	 */
	public DiagnosticOrder setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>clinicalNotes</b> (Explanation/Justification for test).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An explanation or justification for why this diagnostic investigation is being requested
     * </p> 
	 */
	public StringDt getClinicalNotes() {  
		if (myClinicalNotes == null) {
			myClinicalNotes = new StringDt();
		}
		return myClinicalNotes;
	}

	/**
	 * Sets the value(s) for <b>clinicalNotes</b> (Explanation/Justification for test)
	 *
     * <p>
     * <b>Definition:</b>
     * An explanation or justification for why this diagnostic investigation is being requested
     * </p> 
	 */
	public DiagnosticOrder setClinicalNotes(StringDt theValue) {
		myClinicalNotes = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>clinicalNotes</b> (Explanation/Justification for test)
	 *
     * <p>
     * <b>Definition:</b>
     * An explanation or justification for why this diagnostic investigation is being requested
     * </p> 
	 */
	public DiagnosticOrder setClinicalNotes( String theString) {
		myClinicalNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>specimen</b> (If the whole order relates to specific specimens).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One or more specimens that the diagnostic investigation is about
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for <b>specimen</b> (If the whole order relates to specific specimens)
	 *
     * <p>
     * <b>Definition:</b>
     * One or more specimens that the diagnostic investigation is about
     * </p> 
	 */
	public DiagnosticOrder setSpecimen(java.util.List<ResourceReferenceDt> theValue) {
		mySpecimen = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>specimen</b> (If the whole order relates to specific specimens)
	 *
     * <p>
     * <b>Definition:</b>
     * One or more specimens that the diagnostic investigation is about
     * </p> 
	 */
	public ResourceReferenceDt addSpecimen() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSpecimen().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>status</b> (requested | received | accepted | in progress | review | completed | suspended | rejected | failed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the order
     * </p> 
	 */
	public BoundCodeDt<DiagnosticOrderStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DiagnosticOrderStatusEnum>(DiagnosticOrderStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | received | accepted | in progress | review | completed | suspended | rejected | failed)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the order
     * </p> 
	 */
	public DiagnosticOrder setStatus(BoundCodeDt<DiagnosticOrderStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | received | accepted | in progress | review | completed | suspended | rejected | failed)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the order
     * </p> 
	 */
	public DiagnosticOrder setStatus(DiagnosticOrderStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>priority</b> (routine | urgent | stat | asap).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical priority associated with this order
     * </p> 
	 */
	public BoundCodeDt<DiagnosticOrderPriorityEnum> getPriority() {  
		if (myPriority == null) {
			myPriority = new BoundCodeDt<DiagnosticOrderPriorityEnum>(DiagnosticOrderPriorityEnum.VALUESET_BINDER);
		}
		return myPriority;
	}

	/**
	 * Sets the value(s) for <b>priority</b> (routine | urgent | stat | asap)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical priority associated with this order
     * </p> 
	 */
	public DiagnosticOrder setPriority(BoundCodeDt<DiagnosticOrderPriorityEnum> theValue) {
		myPriority = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>priority</b> (routine | urgent | stat | asap)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical priority associated with this order
     * </p> 
	 */
	public DiagnosticOrder setPriority(DiagnosticOrderPriorityEnum theValue) {
		getPriority().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>event</b> (A list of events of interest in the lifecycle).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	public java.util.List<Event> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<Event>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (A list of events of interest in the lifecycle)
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	public DiagnosticOrder setEvent(java.util.List<Event> theValue) {
		myEvent = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>event</b> (A list of events of interest in the lifecycle)
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	public Event addEvent() {
		Event newType = new Event();
		getEvent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>event</b> (A list of events of interest in the lifecycle),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	public Event getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>item</b> (The items the orderer requested).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	public java.util.List<Item> getItem() {  
		if (myItem == null) {
			myItem = new java.util.ArrayList<Item>();
		}
		return myItem;
	}

	/**
	 * Sets the value(s) for <b>item</b> (The items the orderer requested)
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	public DiagnosticOrder setItem(java.util.List<Item> theValue) {
		myItem = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>item</b> (The items the orderer requested)
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	public Item addItem() {
		Item newType = new Item();
		getItem().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>item</b> (The items the orderer requested),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	public Item getItemFirstRep() {
		if (getItem().isEmpty()) {
			return addItem();
		}
		return getItem().get(0); 
	}
  
	/**
	 * Block class for child element: <b>DiagnosticOrder.event</b> (A list of events of interest in the lifecycle)
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed
     * </p> 
	 */
	@Block()	
	public static class Event extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="status", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="requested | received | accepted | in progress | review | completed | suspended | rejected | failed",
		formalDefinition="The status for the event"
	)
	private BoundCodeDt<DiagnosticOrderStatusEnum> myStatus;
	
	@Child(name="description", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="More information about the event and it's context",
		formalDefinition="Additional information about the event that occurred - e.g. if the status remained unchanged"
	)
	private CodeableConceptDt myDescription;
	
	@Child(name="dateTime", type=DateTimeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="The date at which the event happened",
		formalDefinition="The date/time at which the event occurred"
	)
	private DateTimeDt myDateTime;
	
	@Child(name="actor", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Who recorded or did this",
		formalDefinition="The person who was responsible for performing or recording the action"
	)
	private ResourceReferenceDt myActor;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStatus,  myDescription,  myDateTime,  myActor);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStatus, myDescription, myDateTime, myActor);
	}

	/**
	 * Gets the value(s) for <b>status</b> (requested | received | accepted | in progress | review | completed | suspended | rejected | failed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status for the event
     * </p> 
	 */
	public BoundCodeDt<DiagnosticOrderStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DiagnosticOrderStatusEnum>(DiagnosticOrderStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | received | accepted | in progress | review | completed | suspended | rejected | failed)
	 *
     * <p>
     * <b>Definition:</b>
     * The status for the event
     * </p> 
	 */
	public Event setStatus(BoundCodeDt<DiagnosticOrderStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | received | accepted | in progress | review | completed | suspended | rejected | failed)
	 *
     * <p>
     * <b>Definition:</b>
     * The status for the event
     * </p> 
	 */
	public Event setStatus(DiagnosticOrderStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (More information about the event and it's context).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about the event that occurred - e.g. if the status remained unchanged
     * </p> 
	 */
	public CodeableConceptDt getDescription() {  
		if (myDescription == null) {
			myDescription = new CodeableConceptDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (More information about the event and it's context)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about the event that occurred - e.g. if the status remained unchanged
     * </p> 
	 */
	public Event setDescription(CodeableConceptDt theValue) {
		myDescription = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dateTime</b> (The date at which the event happened).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date/time at which the event occurred
     * </p> 
	 */
	public DateTimeDt getDateTime() {  
		if (myDateTime == null) {
			myDateTime = new DateTimeDt();
		}
		return myDateTime;
	}

	/**
	 * Sets the value(s) for <b>dateTime</b> (The date at which the event happened)
	 *
     * <p>
     * <b>Definition:</b>
     * The date/time at which the event occurred
     * </p> 
	 */
	public Event setDateTime(DateTimeDt theValue) {
		myDateTime = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dateTime</b> (The date at which the event happened)
	 *
     * <p>
     * <b>Definition:</b>
     * The date/time at which the event occurred
     * </p> 
	 */
	public Event setDateTimeWithSecondsPrecision( Date theDate) {
		myDateTime = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateTime</b> (The date at which the event happened)
	 *
     * <p>
     * <b>Definition:</b>
     * The date/time at which the event occurred
     * </p> 
	 */
	public Event setDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>actor</b> (Who recorded or did this).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who was responsible for performing or recording the action
     * </p> 
	 */
	public ResourceReferenceDt getActor() {  
		if (myActor == null) {
			myActor = new ResourceReferenceDt();
		}
		return myActor;
	}

	/**
	 * Sets the value(s) for <b>actor</b> (Who recorded or did this)
	 *
     * <p>
     * <b>Definition:</b>
     * The person who was responsible for performing or recording the action
     * </p> 
	 */
	public Event setActor(ResourceReferenceDt theValue) {
		myActor = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>DiagnosticOrder.item</b> (The items the orderer requested)
	 *
     * <p>
     * <b>Definition:</b>
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested
     * </p> 
	 */
	@Block()	
	public static class Item extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Code to indicate the item (test or panel) being ordered",
		formalDefinition="A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="specimen", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Specimen.class	})
	@Description(
		shortDefinition="If this item relates to specific specimens",
		formalDefinition="If the item is related to a specific speciment"
	)
	private java.util.List<ResourceReferenceDt> mySpecimen;
	
	@Child(name="bodySite", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Location of requested test (if applicable)",
		formalDefinition="Anatomical location where the request test should be performed"
	)
	private CodeableConceptDt myBodySite;
	
	@Child(name="status", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="requested | received | accepted | in progress | review | completed | suspended | rejected | failed",
		formalDefinition="The status of this individual item within the order"
	)
	private BoundCodeDt<DiagnosticOrderStatusEnum> myStatus;
	
	@Child(name="event", type=Event.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Events specific to this item",
		formalDefinition="A summary of the events of interest that have occurred as this item of the request is processed"
	)
	private java.util.List<Event> myEvent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  mySpecimen,  myBodySite,  myStatus,  myEvent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, mySpecimen, myBodySite, myStatus, myEvent);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Code to indicate the item (test or panel) being ordered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Code to indicate the item (test or panel) being ordered)
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested
     * </p> 
	 */
	public Item setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>specimen</b> (If this item relates to specific specimens).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the item is related to a specific speciment
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSpecimen() {  
		if (mySpecimen == null) {
			mySpecimen = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySpecimen;
	}

	/**
	 * Sets the value(s) for <b>specimen</b> (If this item relates to specific specimens)
	 *
     * <p>
     * <b>Definition:</b>
     * If the item is related to a specific speciment
     * </p> 
	 */
	public Item setSpecimen(java.util.List<ResourceReferenceDt> theValue) {
		mySpecimen = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>specimen</b> (If this item relates to specific specimens)
	 *
     * <p>
     * <b>Definition:</b>
     * If the item is related to a specific speciment
     * </p> 
	 */
	public ResourceReferenceDt addSpecimen() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSpecimen().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>bodySite</b> (Location of requested test (if applicable)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Anatomical location where the request test should be performed
     * </p> 
	 */
	public CodeableConceptDt getBodySite() {  
		if (myBodySite == null) {
			myBodySite = new CodeableConceptDt();
		}
		return myBodySite;
	}

	/**
	 * Sets the value(s) for <b>bodySite</b> (Location of requested test (if applicable))
	 *
     * <p>
     * <b>Definition:</b>
     * Anatomical location where the request test should be performed
     * </p> 
	 */
	public Item setBodySite(CodeableConceptDt theValue) {
		myBodySite = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>status</b> (requested | received | accepted | in progress | review | completed | suspended | rejected | failed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this individual item within the order
     * </p> 
	 */
	public BoundCodeDt<DiagnosticOrderStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DiagnosticOrderStatusEnum>(DiagnosticOrderStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | received | accepted | in progress | review | completed | suspended | rejected | failed)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this individual item within the order
     * </p> 
	 */
	public Item setStatus(BoundCodeDt<DiagnosticOrderStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (requested | received | accepted | in progress | review | completed | suspended | rejected | failed)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this individual item within the order
     * </p> 
	 */
	public Item setStatus(DiagnosticOrderStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>event</b> (Events specific to this item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as this item of the request is processed
     * </p> 
	 */
	public java.util.List<Event> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<Event>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (Events specific to this item)
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as this item of the request is processed
     * </p> 
	 */
	public Item setEvent(java.util.List<Event> theValue) {
		myEvent = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>event</b> (Events specific to this item)
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as this item of the request is processed
     * </p> 
	 */
	public Event addEvent() {
		Event newType = new Event();
		getEvent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>event</b> (Events specific to this item),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A summary of the events of interest that have occurred as this item of the request is processed
     * </p> 
	 */
	public Event getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  

	}


	@Override
	public ResourceTypeEnum getResourceType() {
		return ResourceTypeEnum.DIAGNOSTICORDER;
	}




}
