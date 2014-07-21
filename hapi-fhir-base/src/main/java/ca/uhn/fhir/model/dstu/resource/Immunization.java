















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
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationReasonCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRouteCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateParam;
import ca.uhn.fhir.rest.gclient.NumberParam;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.StringParam;
import ca.uhn.fhir.rest.gclient.TokenParam;


/**
 * HAPI/FHIR <b>Immunization</b> Resource
 * (Immunization event information)
 *
 * <p>
 * <b>Definition:</b>
 * Immunization event information
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Immunization">http://hl7.org/fhir/profiles/Immunization</a> 
 * </p>
 *
 */
@ResourceDef(name="Immunization", profile="http://hl7.org/fhir/profiles/Immunization", id="immunization")
public class Immunization extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Vaccination  Administration / Refusal Date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Immunization.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Immunization.date", description="Vaccination  Administration / Refusal Date", type="date")
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>Vaccination  Administration / Refusal Date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Immunization.date</b><br/>
	 * </p>
	 */
	public static final DateParam DATE = new DateParam(SP_DATE);

	/**
	 * Search parameter constant for <b>dose-sequence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Immunization.vaccinationProtocol.doseSequence</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dose-sequence", path="Immunization.vaccinationProtocol.doseSequence", description="", type="number")
	public static final String SP_DOSE_SEQUENCE = "dose-sequence";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dose-sequence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>Immunization.vaccinationProtocol.doseSequence</b><br/>
	 * </p>
	 */
	public static final NumberParam DOSE_SEQUENCE = new NumberParam(SP_DOSE_SEQUENCE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Immunization.identifier", description="", type="token")
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.identifier</b><br/>
	 * </p>
	 */
	public static final TokenParam IDENTIFIER = new TokenParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>The service delivery location or facility in which the vaccine was / was to be administered</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Immunization.location", description="The service delivery location or facility in which the vaccine was / was to be administered", type="reference")
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b>The service delivery location or facility in which the vaccine was / was to be administered</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.location</b><br/>
	 * </p>
	 */
	public static final ReferenceParam LOCATION = new ReferenceParam(SP_LOCATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.location</b>".
	 */
	public static final Include INCLUDE_LOCATION = new Include("Immunization.location");

	/**
	 * Search parameter constant for <b>lot-number</b>
	 * <p>
	 * Description: <b>Vaccine Lot Number</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Immunization.lotNumber</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="lot-number", path="Immunization.lotNumber", description="Vaccine Lot Number", type="string")
	public static final String SP_LOT_NUMBER = "lot-number";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>lot-number</b>
	 * <p>
	 * Description: <b>Vaccine Lot Number</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Immunization.lotNumber</b><br/>
	 * </p>
	 */
	public static final StringParam LOT_NUMBER = new StringParam(SP_LOT_NUMBER);

	/**
	 * Search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b>Vaccine Manufacturer</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.manufacturer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="manufacturer", path="Immunization.manufacturer", description="Vaccine Manufacturer", type="reference")
	public static final String SP_MANUFACTURER = "manufacturer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
	 * <p>
	 * Description: <b>Vaccine Manufacturer</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.manufacturer</b><br/>
	 * </p>
	 */
	public static final ReferenceParam MANUFACTURER = new ReferenceParam(SP_MANUFACTURER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.manufacturer</b>".
	 */
	public static final Include INCLUDE_MANUFACTURER = new Include("Immunization.manufacturer");

	/**
	 * Search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>The practitioner who administered the vaccination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.performer</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="performer", path="Immunization.performer", description="The practitioner who administered the vaccination", type="reference")
	public static final String SP_PERFORMER = "performer";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>performer</b>
	 * <p>
	 * Description: <b>The practitioner who administered the vaccination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.performer</b><br/>
	 * </p>
	 */
	public static final ReferenceParam PERFORMER = new ReferenceParam(SP_PERFORMER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.performer</b>".
	 */
	public static final Include INCLUDE_PERFORMER = new Include("Immunization.performer");

	/**
	 * Search parameter constant for <b>reaction</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.reaction.detail</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reaction", path="Immunization.reaction.detail", description="", type="reference")
	public static final String SP_REACTION = "reaction";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reaction</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.reaction.detail</b><br/>
	 * </p>
	 */
	public static final ReferenceParam REACTION = new ReferenceParam(SP_REACTION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.reaction.detail</b>".
	 */
	public static final Include INCLUDE_REACTION_DETAIL = new Include("Immunization.reaction.detail");

	/**
	 * Search parameter constant for <b>reaction-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Immunization.reaction.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reaction-date", path="Immunization.reaction.date", description="", type="date")
	public static final String SP_REACTION_DATE = "reaction-date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reaction-date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Immunization.reaction.date</b><br/>
	 * </p>
	 */
	public static final DateParam REACTION_DATE = new DateParam(SP_REACTION_DATE);

	/**
	 * Search parameter constant for <b>reason</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.explanation.reason</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reason", path="Immunization.explanation.reason", description="", type="token")
	public static final String SP_REASON = "reason";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reason</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.explanation.reason</b><br/>
	 * </p>
	 */
	public static final TokenParam REASON = new TokenParam(SP_REASON);

	/**
	 * Search parameter constant for <b>refusal-reason</b>
	 * <p>
	 * Description: <b>Explanation of refusal / exemption</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.explanation.refusalReason</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="refusal-reason", path="Immunization.explanation.refusalReason", description="Explanation of refusal / exemption", type="token")
	public static final String SP_REFUSAL_REASON = "refusal-reason";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>refusal-reason</b>
	 * <p>
	 * Description: <b>Explanation of refusal / exemption</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.explanation.refusalReason</b><br/>
	 * </p>
	 */
	public static final TokenParam REFUSAL_REASON = new TokenParam(SP_REFUSAL_REASON);

	/**
	 * Search parameter constant for <b>refused</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.refusedIndicator</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="refused", path="Immunization.refusedIndicator", description="", type="token")
	public static final String SP_REFUSED = "refused";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>refused</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.refusedIndicator</b><br/>
	 * </p>
	 */
	public static final TokenParam REFUSED = new TokenParam(SP_REFUSED);

	/**
	 * Search parameter constant for <b>requester</b>
	 * <p>
	 * Description: <b>The practitioner who ordered the vaccination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.requester</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="requester", path="Immunization.requester", description="The practitioner who ordered the vaccination", type="reference")
	public static final String SP_REQUESTER = "requester";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>requester</b>
	 * <p>
	 * Description: <b>The practitioner who ordered the vaccination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.requester</b><br/>
	 * </p>
	 */
	public static final ReferenceParam REQUESTER = new ReferenceParam(SP_REQUESTER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.requester</b>".
	 */
	public static final Include INCLUDE_REQUESTER = new Include("Immunization.requester");

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the vaccination event / refusal</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Immunization.subject", description="The subject of the vaccination event / refusal", type="reference")
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the vaccination event / refusal</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Immunization.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceParam SUBJECT = new ReferenceParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Immunization.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Immunization.subject");

	/**
	 * Search parameter constant for <b>vaccine-type</b>
	 * <p>
	 * Description: <b>Vaccine Product Type Administered</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.vaccineType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="vaccine-type", path="Immunization.vaccineType", description="Vaccine Product Type Administered", type="token")
	public static final String SP_VACCINE_TYPE = "vaccine-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>vaccine-type</b>
	 * <p>
	 * Description: <b>Vaccine Product Type Administered</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Immunization.vaccineType</b><br/>
	 * </p>
	 */
	public static final TokenParam VACCINE_TYPE = new TokenParam(SP_VACCINE_TYPE);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Business identifier",
		formalDefinition="A unique identifier assigned to this adverse reaction record."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="date", type=DateTimeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Vaccination administration date",
		formalDefinition="Date vaccine administered or was to be administered"
	)
	private DateTimeDt myDate;
	
	@Child(name="vaccineType", type=CodeableConceptDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Vaccine product administered",
		formalDefinition="Vaccine that was administered or was to be administered"
	)
	private CodeableConceptDt myVaccineType;
	
	@Child(name="subject", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who was immunized?",
		formalDefinition="The patient to whom the vaccine was to be administered"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="refusedIndicator", type=BooleanDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Was immunization refused?",
		formalDefinition="Indicates if the vaccination was refused."
	)
	private BooleanDt myRefusedIndicator;
	
	@Child(name="reported", type=BooleanDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Is this a self-reported record?",
		formalDefinition="True if this administration was reported rather than directly administered."
	)
	private BooleanDt myReported;
	
	@Child(name="performer", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who administered vaccine?",
		formalDefinition="Clinician who administered the vaccine"
	)
	private ResourceReferenceDt myPerformer;
	
	@Child(name="requester", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who ordered vaccination?",
		formalDefinition="Clinician who ordered the vaccination"
	)
	private ResourceReferenceDt myRequester;
	
	@Child(name="manufacturer", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Vaccine manufacturer",
		formalDefinition="Name of vaccine manufacturer"
	)
	private ResourceReferenceDt myManufacturer;
	
	@Child(name="location", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Where did vaccination occur?",
		formalDefinition="The service delivery location where the vaccine administration occurred."
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="lotNumber", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Vaccine lot number",
		formalDefinition="Lot number of the  vaccine product"
	)
	private StringDt myLotNumber;
	
	@Child(name="expirationDate", type=DateDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Vaccine expiration date",
		formalDefinition="Date vaccine batch expires"
	)
	private DateDt myExpirationDate;
	
	@Child(name="site", type=CodeableConceptDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="Body site vaccine  was administered",
		formalDefinition="Body site where vaccine was administered"
	)
	private CodeableConceptDt mySite;
	
	@Child(name="route", type=CodeableConceptDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="How vaccine entered body",
		formalDefinition="The path by which the vaccine product is taken into the body."
	)
	private BoundCodeableConceptDt<ImmunizationRouteCodesEnum> myRoute;
	
	@Child(name="doseQuantity", type=QuantityDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="Amount of vaccine administered",
		formalDefinition="The quantity of vaccine product that was administered"
	)
	private QuantityDt myDoseQuantity;
	
	@Child(name="explanation", order=15, min=0, max=1)	
	@Description(
		shortDefinition="Administration / refusal reasons",
		formalDefinition="Reasons why a vaccine was administered or refused"
	)
	private Explanation myExplanation;
	
	@Child(name="reaction", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Details of a reaction that follows immunization",
		formalDefinition="Categorical data indicating that an adverse event is associated in time to an immunization"
	)
	private java.util.List<Reaction> myReaction;
	
	@Child(name="vaccinationProtocol", order=17, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="What protocol was followed",
		formalDefinition="Contains information about the protocol(s) under which the vaccine was administered"
	)
	private java.util.List<VaccinationProtocol> myVaccinationProtocol;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDate,  myVaccineType,  mySubject,  myRefusedIndicator,  myReported,  myPerformer,  myRequester,  myManufacturer,  myLocation,  myLotNumber,  myExpirationDate,  mySite,  myRoute,  myDoseQuantity,  myExplanation,  myReaction,  myVaccinationProtocol);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDate, myVaccineType, mySubject, myRefusedIndicator, myReported, myPerformer, myRequester, myManufacturer, myLocation, myLotNumber, myExpirationDate, mySite, myRoute, myDoseQuantity, myExplanation, myReaction, myVaccinationProtocol);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Business identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this adverse reaction record.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this adverse reaction record.
     * </p> 
	 */
	public Immunization setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this adverse reaction record.
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Business identifier),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this adverse reaction record.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this adverse reaction record.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Immunization addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this adverse reaction record.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Immunization addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (Vaccination administration date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine administered or was to be administered
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Vaccination administration date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine administered or was to be administered
     * </p> 
	 */
	public Immunization setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (Vaccination administration date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine administered or was to be administered
     * </p> 
	 */
	public Immunization setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (Vaccination administration date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine administered or was to be administered
     * </p> 
	 */
	public Immunization setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>vaccineType</b> (Vaccine product administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine that was administered or was to be administered
     * </p> 
	 */
	public CodeableConceptDt getVaccineType() {  
		if (myVaccineType == null) {
			myVaccineType = new CodeableConceptDt();
		}
		return myVaccineType;
	}

	/**
	 * Sets the value(s) for <b>vaccineType</b> (Vaccine product administered)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine that was administered or was to be administered
     * </p> 
	 */
	public Immunization setVaccineType(CodeableConceptDt theValue) {
		myVaccineType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subject</b> (Who was immunized?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient to whom the vaccine was to be administered
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who was immunized?)
	 *
     * <p>
     * <b>Definition:</b>
     * The patient to whom the vaccine was to be administered
     * </p> 
	 */
	public Immunization setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>refusedIndicator</b> (Was immunization refused?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the vaccination was refused.
     * </p> 
	 */
	public BooleanDt getRefusedIndicator() {  
		if (myRefusedIndicator == null) {
			myRefusedIndicator = new BooleanDt();
		}
		return myRefusedIndicator;
	}

	/**
	 * Sets the value(s) for <b>refusedIndicator</b> (Was immunization refused?)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the vaccination was refused.
     * </p> 
	 */
	public Immunization setRefusedIndicator(BooleanDt theValue) {
		myRefusedIndicator = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>refusedIndicator</b> (Was immunization refused?)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the vaccination was refused.
     * </p> 
	 */
	public Immunization setRefusedIndicator( boolean theBoolean) {
		myRefusedIndicator = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reported</b> (Is this a self-reported record?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * True if this administration was reported rather than directly administered.
     * </p> 
	 */
	public BooleanDt getReported() {  
		if (myReported == null) {
			myReported = new BooleanDt();
		}
		return myReported;
	}

	/**
	 * Sets the value(s) for <b>reported</b> (Is this a self-reported record?)
	 *
     * <p>
     * <b>Definition:</b>
     * True if this administration was reported rather than directly administered.
     * </p> 
	 */
	public Immunization setReported(BooleanDt theValue) {
		myReported = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>reported</b> (Is this a self-reported record?)
	 *
     * <p>
     * <b>Definition:</b>
     * True if this administration was reported rather than directly administered.
     * </p> 
	 */
	public Immunization setReported( boolean theBoolean) {
		myReported = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>performer</b> (Who administered vaccine?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinician who administered the vaccine
     * </p> 
	 */
	public ResourceReferenceDt getPerformer() {  
		if (myPerformer == null) {
			myPerformer = new ResourceReferenceDt();
		}
		return myPerformer;
	}

	/**
	 * Sets the value(s) for <b>performer</b> (Who administered vaccine?)
	 *
     * <p>
     * <b>Definition:</b>
     * Clinician who administered the vaccine
     * </p> 
	 */
	public Immunization setPerformer(ResourceReferenceDt theValue) {
		myPerformer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>requester</b> (Who ordered vaccination?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinician who ordered the vaccination
     * </p> 
	 */
	public ResourceReferenceDt getRequester() {  
		if (myRequester == null) {
			myRequester = new ResourceReferenceDt();
		}
		return myRequester;
	}

	/**
	 * Sets the value(s) for <b>requester</b> (Who ordered vaccination?)
	 *
     * <p>
     * <b>Definition:</b>
     * Clinician who ordered the vaccination
     * </p> 
	 */
	public Immunization setRequester(ResourceReferenceDt theValue) {
		myRequester = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>manufacturer</b> (Vaccine manufacturer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of vaccine manufacturer
     * </p> 
	 */
	public ResourceReferenceDt getManufacturer() {  
		if (myManufacturer == null) {
			myManufacturer = new ResourceReferenceDt();
		}
		return myManufacturer;
	}

	/**
	 * Sets the value(s) for <b>manufacturer</b> (Vaccine manufacturer)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of vaccine manufacturer
     * </p> 
	 */
	public Immunization setManufacturer(ResourceReferenceDt theValue) {
		myManufacturer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>location</b> (Where did vaccination occur?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The service delivery location where the vaccine administration occurred.
     * </p> 
	 */
	public ResourceReferenceDt getLocation() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (Where did vaccination occur?)
	 *
     * <p>
     * <b>Definition:</b>
     * The service delivery location where the vaccine administration occurred.
     * </p> 
	 */
	public Immunization setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>lotNumber</b> (Vaccine lot number).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number of the  vaccine product
     * </p> 
	 */
	public StringDt getLotNumber() {  
		if (myLotNumber == null) {
			myLotNumber = new StringDt();
		}
		return myLotNumber;
	}

	/**
	 * Sets the value(s) for <b>lotNumber</b> (Vaccine lot number)
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number of the  vaccine product
     * </p> 
	 */
	public Immunization setLotNumber(StringDt theValue) {
		myLotNumber = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>lotNumber</b> (Vaccine lot number)
	 *
     * <p>
     * <b>Definition:</b>
     * Lot number of the  vaccine product
     * </p> 
	 */
	public Immunization setLotNumber( String theString) {
		myLotNumber = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>expirationDate</b> (Vaccine expiration date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine batch expires
     * </p> 
	 */
	public DateDt getExpirationDate() {  
		if (myExpirationDate == null) {
			myExpirationDate = new DateDt();
		}
		return myExpirationDate;
	}

	/**
	 * Sets the value(s) for <b>expirationDate</b> (Vaccine expiration date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine batch expires
     * </p> 
	 */
	public Immunization setExpirationDate(DateDt theValue) {
		myExpirationDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>expirationDate</b> (Vaccine expiration date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine batch expires
     * </p> 
	 */
	public Immunization setExpirationDateWithDayPrecision( Date theDate) {
		myExpirationDate = new DateDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>expirationDate</b> (Vaccine expiration date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date vaccine batch expires
     * </p> 
	 */
	public Immunization setExpirationDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myExpirationDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>site</b> (Body site vaccine  was administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Body site where vaccine was administered
     * </p> 
	 */
	public CodeableConceptDt getSite() {  
		if (mySite == null) {
			mySite = new CodeableConceptDt();
		}
		return mySite;
	}

	/**
	 * Sets the value(s) for <b>site</b> (Body site vaccine  was administered)
	 *
     * <p>
     * <b>Definition:</b>
     * Body site where vaccine was administered
     * </p> 
	 */
	public Immunization setSite(CodeableConceptDt theValue) {
		mySite = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>route</b> (How vaccine entered body).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The path by which the vaccine product is taken into the body.
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationRouteCodesEnum> getRoute() {  
		if (myRoute == null) {
			myRoute = new BoundCodeableConceptDt<ImmunizationRouteCodesEnum>(ImmunizationRouteCodesEnum.VALUESET_BINDER);
		}
		return myRoute;
	}

	/**
	 * Sets the value(s) for <b>route</b> (How vaccine entered body)
	 *
     * <p>
     * <b>Definition:</b>
     * The path by which the vaccine product is taken into the body.
     * </p> 
	 */
	public Immunization setRoute(BoundCodeableConceptDt<ImmunizationRouteCodesEnum> theValue) {
		myRoute = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>route</b> (How vaccine entered body)
	 *
     * <p>
     * <b>Definition:</b>
     * The path by which the vaccine product is taken into the body.
     * </p> 
	 */
	public Immunization setRoute(ImmunizationRouteCodesEnum theValue) {
		getRoute().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>doseQuantity</b> (Amount of vaccine administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public QuantityDt getDoseQuantity() {  
		if (myDoseQuantity == null) {
			myDoseQuantity = new QuantityDt();
		}
		return myDoseQuantity;
	}

	/**
	 * Sets the value(s) for <b>doseQuantity</b> (Amount of vaccine administered)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public Immunization setDoseQuantity(QuantityDt theValue) {
		myDoseQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>doseQuantity</b> (Amount of vaccine administered)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public Immunization setDoseQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myDoseQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>doseQuantity</b> (Amount of vaccine administered)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public Immunization setDoseQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myDoseQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>doseQuantity</b> (Amount of vaccine administered)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public Immunization setDoseQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myDoseQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>doseQuantity</b> (Amount of vaccine administered)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public Immunization setDoseQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myDoseQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>doseQuantity</b> (Amount of vaccine administered)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public Immunization setDoseQuantity( double theValue) {
		myDoseQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>doseQuantity</b> (Amount of vaccine administered)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of vaccine product that was administered
     * </p> 
	 */
	public Immunization setDoseQuantity( long theValue) {
		myDoseQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>explanation</b> (Administration / refusal reasons).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered or refused
     * </p> 
	 */
	public Explanation getExplanation() {  
		if (myExplanation == null) {
			myExplanation = new Explanation();
		}
		return myExplanation;
	}

	/**
	 * Sets the value(s) for <b>explanation</b> (Administration / refusal reasons)
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered or refused
     * </p> 
	 */
	public Immunization setExplanation(Explanation theValue) {
		myExplanation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reaction</b> (Details of a reaction that follows immunization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	public java.util.List<Reaction> getReaction() {  
		if (myReaction == null) {
			myReaction = new java.util.ArrayList<Reaction>();
		}
		return myReaction;
	}

	/**
	 * Sets the value(s) for <b>reaction</b> (Details of a reaction that follows immunization)
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	public Immunization setReaction(java.util.List<Reaction> theValue) {
		myReaction = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>reaction</b> (Details of a reaction that follows immunization)
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	public Reaction addReaction() {
		Reaction newType = new Reaction();
		getReaction().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>reaction</b> (Details of a reaction that follows immunization),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	public Reaction getReactionFirstRep() {
		if (getReaction().isEmpty()) {
			return addReaction();
		}
		return getReaction().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>vaccinationProtocol</b> (What protocol was followed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	public java.util.List<VaccinationProtocol> getVaccinationProtocol() {  
		if (myVaccinationProtocol == null) {
			myVaccinationProtocol = new java.util.ArrayList<VaccinationProtocol>();
		}
		return myVaccinationProtocol;
	}

	/**
	 * Sets the value(s) for <b>vaccinationProtocol</b> (What protocol was followed)
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	public Immunization setVaccinationProtocol(java.util.List<VaccinationProtocol> theValue) {
		myVaccinationProtocol = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>vaccinationProtocol</b> (What protocol was followed)
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	public VaccinationProtocol addVaccinationProtocol() {
		VaccinationProtocol newType = new VaccinationProtocol();
		getVaccinationProtocol().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>vaccinationProtocol</b> (What protocol was followed),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	public VaccinationProtocol getVaccinationProtocolFirstRep() {
		if (getVaccinationProtocol().isEmpty()) {
			return addVaccinationProtocol();
		}
		return getVaccinationProtocol().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Immunization.explanation</b> (Administration / refusal reasons)
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered or refused
     * </p> 
	 */
	@Block()	
	public static class Explanation extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="reason", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Why immunization occurred",
		formalDefinition="Reasons why a vaccine was administered"
	)
	private java.util.List<BoundCodeableConceptDt<ImmunizationReasonCodesEnum>> myReason;
	
	@Child(name="refusalReason", type=CodeableConceptDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Why immunization did not occur",
		formalDefinition="Refusal or exemption reasons"
	)
	private java.util.List<CodeableConceptDt> myRefusalReason;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myReason,  myRefusalReason);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myReason, myRefusalReason);
	}

	/**
	 * Gets the value(s) for <b>reason</b> (Why immunization occurred).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<ImmunizationReasonCodesEnum>> getReason() {  
		if (myReason == null) {
			myReason = new java.util.ArrayList<BoundCodeableConceptDt<ImmunizationReasonCodesEnum>>();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (Why immunization occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public Explanation setReason(java.util.List<BoundCodeableConceptDt<ImmunizationReasonCodesEnum>> theValue) {
		myReason = theValue;
		return this;
	}

	/**
	 * Add a value for <b>reason</b> (Why immunization occurred) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationReasonCodesEnum> addReason(ImmunizationReasonCodesEnum theValue) {
		BoundCodeableConceptDt<ImmunizationReasonCodesEnum> retVal = new BoundCodeableConceptDt<ImmunizationReasonCodesEnum>(ImmunizationReasonCodesEnum.VALUESET_BINDER, theValue);
		getReason().add(retVal);
		return retVal;
	}

	/**
	 * Add a value for <b>reason</b> (Why immunization occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationReasonCodesEnum> addReason() {
		BoundCodeableConceptDt<ImmunizationReasonCodesEnum> retVal = new BoundCodeableConceptDt<ImmunizationReasonCodesEnum>(ImmunizationReasonCodesEnum.VALUESET_BINDER);
		getReason().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>reason</b> (Why immunization occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * Reasons why a vaccine was administered
     * </p> 
	 */
	public Explanation setReason(ImmunizationReasonCodesEnum theValue) {
		getReason().clear();
		addReason(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>refusalReason</b> (Why immunization did not occur).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Refusal or exemption reasons
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getRefusalReason() {  
		if (myRefusalReason == null) {
			myRefusalReason = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myRefusalReason;
	}

	/**
	 * Sets the value(s) for <b>refusalReason</b> (Why immunization did not occur)
	 *
     * <p>
     * <b>Definition:</b>
     * Refusal or exemption reasons
     * </p> 
	 */
	public Explanation setRefusalReason(java.util.List<CodeableConceptDt> theValue) {
		myRefusalReason = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>refusalReason</b> (Why immunization did not occur)
	 *
     * <p>
     * <b>Definition:</b>
     * Refusal or exemption reasons
     * </p> 
	 */
	public CodeableConceptDt addRefusalReason() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getRefusalReason().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>refusalReason</b> (Why immunization did not occur),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Refusal or exemption reasons
     * </p> 
	 */
	public CodeableConceptDt getRefusalReasonFirstRep() {
		if (getRefusalReason().isEmpty()) {
			return addRefusalReason();
		}
		return getRefusalReason().get(0); 
	}
  

	}


	/**
	 * Block class for child element: <b>Immunization.reaction</b> (Details of a reaction that follows immunization)
	 *
     * <p>
     * <b>Definition:</b>
     * Categorical data indicating that an adverse event is associated in time to an immunization
     * </p> 
	 */
	@Block()	
	public static class Reaction extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="date", type=DateTimeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="When did reaction start?",
		formalDefinition="Date of reaction to the immunization"
	)
	private DateTimeDt myDate;
	
	@Child(name="detail", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.AdverseReaction.class, 		ca.uhn.fhir.model.dstu.resource.Observation.class	})
	@Description(
		shortDefinition="Additional information on reaction",
		formalDefinition="Details of the reaction"
	)
	private ResourceReferenceDt myDetail;
	
	@Child(name="reported", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Was reaction self-reported?",
		formalDefinition="Self-reported indicator"
	)
	private BooleanDt myReported;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDate,  myDetail,  myReported);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDate, myDetail, myReported);
	}

	/**
	 * Gets the value(s) for <b>date</b> (When did reaction start?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date of reaction to the immunization
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (When did reaction start?)
	 *
     * <p>
     * <b>Definition:</b>
     * Date of reaction to the immunization
     * </p> 
	 */
	public Reaction setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (When did reaction start?)
	 *
     * <p>
     * <b>Definition:</b>
     * Date of reaction to the immunization
     * </p> 
	 */
	public Reaction setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When did reaction start?)
	 *
     * <p>
     * <b>Definition:</b>
     * Date of reaction to the immunization
     * </p> 
	 */
	public Reaction setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>detail</b> (Additional information on reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the reaction
     * </p> 
	 */
	public ResourceReferenceDt getDetail() {  
		if (myDetail == null) {
			myDetail = new ResourceReferenceDt();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> (Additional information on reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the reaction
     * </p> 
	 */
	public Reaction setDetail(ResourceReferenceDt theValue) {
		myDetail = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reported</b> (Was reaction self-reported?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Self-reported indicator
     * </p> 
	 */
	public BooleanDt getReported() {  
		if (myReported == null) {
			myReported = new BooleanDt();
		}
		return myReported;
	}

	/**
	 * Sets the value(s) for <b>reported</b> (Was reaction self-reported?)
	 *
     * <p>
     * <b>Definition:</b>
     * Self-reported indicator
     * </p> 
	 */
	public Reaction setReported(BooleanDt theValue) {
		myReported = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>reported</b> (Was reaction self-reported?)
	 *
     * <p>
     * <b>Definition:</b>
     * Self-reported indicator
     * </p> 
	 */
	public Reaction setReported( boolean theBoolean) {
		myReported = new BooleanDt(theBoolean); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Immunization.vaccinationProtocol</b> (What protocol was followed)
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol(s) under which the vaccine was administered
     * </p> 
	 */
	@Block()	
	public static class VaccinationProtocol extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="doseSequence", type=IntegerDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="What dose number within series?",
		formalDefinition="Nominal position in a series"
	)
	private IntegerDt myDoseSequence;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Details of vaccine protocol",
		formalDefinition="Contains the description about the protocol under which the vaccine was administered"
	)
	private StringDt myDescription;
	
	@Child(name="authority", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Who is responsible for protocol",
		formalDefinition="Indicates the authority who published the protocol?  E.g. ACIP"
	)
	private ResourceReferenceDt myAuthority;
	
	@Child(name="series", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Name of vaccine series",
		formalDefinition="One possible path to achieve presumed immunity against a disease - within the context of an authority"
	)
	private StringDt mySeries;
	
	@Child(name="seriesDoses", type=IntegerDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Recommended number of doses for immunity",
		formalDefinition="The recommended number of doses to achieve immunity."
	)
	private IntegerDt mySeriesDoses;
	
	@Child(name="doseTarget", type=CodeableConceptDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Disease immunized against",
		formalDefinition="The targeted disease"
	)
	private CodeableConceptDt myDoseTarget;
	
	@Child(name="doseStatus", type=CodeableConceptDt.class, order=6, min=1, max=1)	
	@Description(
		shortDefinition="Does dose count towards immunity?",
		formalDefinition="Indicates if the immunization event should \"count\" against  the protocol."
	)
	private CodeableConceptDt myDoseStatus;
	
	@Child(name="doseStatusReason", type=CodeableConceptDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Why does does count/not count?",
		formalDefinition="Provides an explanation as to why a immunization event should or should not count against the protocol."
	)
	private CodeableConceptDt myDoseStatusReason;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDoseSequence,  myDescription,  myAuthority,  mySeries,  mySeriesDoses,  myDoseTarget,  myDoseStatus,  myDoseStatusReason);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDoseSequence, myDescription, myAuthority, mySeries, mySeriesDoses, myDoseTarget, myDoseStatus, myDoseStatusReason);
	}

	/**
	 * Gets the value(s) for <b>doseSequence</b> (What dose number within series?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Nominal position in a series
     * </p> 
	 */
	public IntegerDt getDoseSequence() {  
		if (myDoseSequence == null) {
			myDoseSequence = new IntegerDt();
		}
		return myDoseSequence;
	}

	/**
	 * Sets the value(s) for <b>doseSequence</b> (What dose number within series?)
	 *
     * <p>
     * <b>Definition:</b>
     * Nominal position in a series
     * </p> 
	 */
	public VaccinationProtocol setDoseSequence(IntegerDt theValue) {
		myDoseSequence = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>doseSequence</b> (What dose number within series?)
	 *
     * <p>
     * <b>Definition:</b>
     * Nominal position in a series
     * </p> 
	 */
	public VaccinationProtocol setDoseSequence( int theInteger) {
		myDoseSequence = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (Details of vaccine protocol).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contains the description about the protocol under which the vaccine was administered
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Details of vaccine protocol)
	 *
     * <p>
     * <b>Definition:</b>
     * Contains the description about the protocol under which the vaccine was administered
     * </p> 
	 */
	public VaccinationProtocol setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Details of vaccine protocol)
	 *
     * <p>
     * <b>Definition:</b>
     * Contains the description about the protocol under which the vaccine was administered
     * </p> 
	 */
	public VaccinationProtocol setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>authority</b> (Who is responsible for protocol).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the authority who published the protocol?  E.g. ACIP
     * </p> 
	 */
	public ResourceReferenceDt getAuthority() {  
		if (myAuthority == null) {
			myAuthority = new ResourceReferenceDt();
		}
		return myAuthority;
	}

	/**
	 * Sets the value(s) for <b>authority</b> (Who is responsible for protocol)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the authority who published the protocol?  E.g. ACIP
     * </p> 
	 */
	public VaccinationProtocol setAuthority(ResourceReferenceDt theValue) {
		myAuthority = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>series</b> (Name of vaccine series).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public StringDt getSeries() {  
		if (mySeries == null) {
			mySeries = new StringDt();
		}
		return mySeries;
	}

	/**
	 * Sets the value(s) for <b>series</b> (Name of vaccine series)
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public VaccinationProtocol setSeries(StringDt theValue) {
		mySeries = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>series</b> (Name of vaccine series)
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public VaccinationProtocol setSeries( String theString) {
		mySeries = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>seriesDoses</b> (Recommended number of doses for immunity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended number of doses to achieve immunity.
     * </p> 
	 */
	public IntegerDt getSeriesDoses() {  
		if (mySeriesDoses == null) {
			mySeriesDoses = new IntegerDt();
		}
		return mySeriesDoses;
	}

	/**
	 * Sets the value(s) for <b>seriesDoses</b> (Recommended number of doses for immunity)
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended number of doses to achieve immunity.
     * </p> 
	 */
	public VaccinationProtocol setSeriesDoses(IntegerDt theValue) {
		mySeriesDoses = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>seriesDoses</b> (Recommended number of doses for immunity)
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended number of doses to achieve immunity.
     * </p> 
	 */
	public VaccinationProtocol setSeriesDoses( int theInteger) {
		mySeriesDoses = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>doseTarget</b> (Disease immunized against).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The targeted disease
     * </p> 
	 */
	public CodeableConceptDt getDoseTarget() {  
		if (myDoseTarget == null) {
			myDoseTarget = new CodeableConceptDt();
		}
		return myDoseTarget;
	}

	/**
	 * Sets the value(s) for <b>doseTarget</b> (Disease immunized against)
	 *
     * <p>
     * <b>Definition:</b>
     * The targeted disease
     * </p> 
	 */
	public VaccinationProtocol setDoseTarget(CodeableConceptDt theValue) {
		myDoseTarget = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>doseStatus</b> (Does dose count towards immunity?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the immunization event should \"count\" against  the protocol.
     * </p> 
	 */
	public CodeableConceptDt getDoseStatus() {  
		if (myDoseStatus == null) {
			myDoseStatus = new CodeableConceptDt();
		}
		return myDoseStatus;
	}

	/**
	 * Sets the value(s) for <b>doseStatus</b> (Does dose count towards immunity?)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the immunization event should \"count\" against  the protocol.
     * </p> 
	 */
	public VaccinationProtocol setDoseStatus(CodeableConceptDt theValue) {
		myDoseStatus = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>doseStatusReason</b> (Why does does count/not count?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides an explanation as to why a immunization event should or should not count against the protocol.
     * </p> 
	 */
	public CodeableConceptDt getDoseStatusReason() {  
		if (myDoseStatusReason == null) {
			myDoseStatusReason = new CodeableConceptDt();
		}
		return myDoseStatusReason;
	}

	/**
	 * Sets the value(s) for <b>doseStatusReason</b> (Why does does count/not count?)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides an explanation as to why a immunization event should or should not count against the protocol.
     * </p> 
	 */
	public VaccinationProtocol setDoseStatusReason(CodeableConceptDt theValue) {
		myDoseStatusReason = theValue;
		return this;
	}

  

	}




}
