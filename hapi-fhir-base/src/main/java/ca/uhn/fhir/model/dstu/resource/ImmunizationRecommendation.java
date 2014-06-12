















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR Library
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

import ca.uhn.fhir.model.api.BaseElement;
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
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationDateCriterionCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ImmunizationRecommendationStatusCodesEnum;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateParam;
import ca.uhn.fhir.rest.gclient.NumberParam;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.TokenParam;


/**
 * HAPI/FHIR <b>ImmunizationRecommendation</b> Resource
 * (Immunization profile)
 *
 * <p>
 * <b>Definition:</b>
 * A patient's point-of-time immunization status and recommendation with optional supporting justification
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/ImmunizationRecommendation">http://hl7.org/fhir/profiles/ImmunizationRecommendation</a> 
 * </p>
 *
 */
@ResourceDef(name="ImmunizationRecommendation", profile="http://hl7.org/fhir/profiles/ImmunizationRecommendation", id="immunizationrecommendation")
public class ImmunizationRecommendation extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="ImmunizationRecommendation.subject", description="", type="reference")
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceParam SUBJECT = new ReferenceParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("ImmunizationRecommendation.subject");

	/**
	 * Search parameter constant for <b>vaccine-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.vaccineType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="vaccine-type", path="ImmunizationRecommendation.recommendation.vaccineType", description="", type="token")
	public static final String SP_VACCINE_TYPE = "vaccine-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>vaccine-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.vaccineType</b><br/>
	 * </p>
	 */
	public static final TokenParam VACCINE_TYPE = new TokenParam(SP_VACCINE_TYPE);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="ImmunizationRecommendation.identifier", description="", type="token")
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.identifier</b><br/>
	 * </p>
	 */
	public static final TokenParam IDENTIFIER = new TokenParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="ImmunizationRecommendation.recommendation.date", description="", type="date")
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.date</b><br/>
	 * </p>
	 */
	public static final DateParam DATE = new DateParam(SP_DATE);

	/**
	 * Search parameter constant for <b>dose-number</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.doseNumber</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dose-number", path="ImmunizationRecommendation.recommendation.doseNumber", description="", type="number")
	public static final String SP_DOSE_NUMBER = "dose-number";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dose-number</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.doseNumber</b><br/>
	 * </p>
	 */
	public static final NumberParam DOSE_NUMBER = new NumberParam(SP_DOSE_NUMBER);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.forecastStatus</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="ImmunizationRecommendation.recommendation.forecastStatus", description="", type="token")
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.forecastStatus</b><br/>
	 * </p>
	 */
	public static final TokenParam STATUS = new TokenParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>dose-sequence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.protocol.doseSequence</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dose-sequence", path="ImmunizationRecommendation.recommendation.protocol.doseSequence", description="", type="token")
	public static final String SP_DOSE_SEQUENCE = "dose-sequence";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dose-sequence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.protocol.doseSequence</b><br/>
	 * </p>
	 */
	public static final TokenParam DOSE_SEQUENCE = new TokenParam(SP_DOSE_SEQUENCE);

	/**
	 * Search parameter constant for <b>support</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.supportingImmunization</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="support", path="ImmunizationRecommendation.recommendation.supportingImmunization", description="", type="reference")
	public static final String SP_SUPPORT = "support";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>support</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.supportingImmunization</b><br/>
	 * </p>
	 */
	public static final ReferenceParam SUPPORT = new ReferenceParam(SP_SUPPORT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.recommendation.supportingImmunization</b>".
	 */
	public static final Include INCLUDE_RECOMMENDATION_SUPPORTINGIMMUNIZATION = new Include("ImmunizationRecommendation.recommendation.supportingImmunization");

	/**
	 * Search parameter constant for <b>information</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.supportingPatientInformation</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="information", path="ImmunizationRecommendation.recommendation.supportingPatientInformation", description="", type="reference")
	public static final String SP_INFORMATION = "information";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>information</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>ImmunizationRecommendation.recommendation.supportingPatientInformation</b><br/>
	 * </p>
	 */
	public static final ReferenceParam INFORMATION = new ReferenceParam(SP_INFORMATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ImmunizationRecommendation.recommendation.supportingPatientInformation</b>".
	 */
	public static final Include INCLUDE_RECOMMENDATION_SUPPORTINGPATIENTINFORMATION = new Include("ImmunizationRecommendation.recommendation.supportingPatientInformation");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Business identifier",
		formalDefinition="A unique identifier assigned to this particular recommendation record."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who this profile is for",
		formalDefinition="The patient who is the subject of the profile"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="recommendation", order=2, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Vaccine administration recommendations",
		formalDefinition="Vaccine administration recommendations"
	)
	private java.util.List<Recommendation> myRecommendation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  mySubject,  myRecommendation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, mySubject, myRecommendation);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Business identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this particular recommendation record.
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
     * A unique identifier assigned to this particular recommendation record.
     * </p> 
	 */
	public ImmunizationRecommendation setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Business identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier assigned to this particular recommendation record.
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
     * A unique identifier assigned to this particular recommendation record.
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
     * A unique identifier assigned to this particular recommendation record.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ImmunizationRecommendation addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
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
     * A unique identifier assigned to this particular recommendation record.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ImmunizationRecommendation addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Who this profile is for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The patient who is the subject of the profile
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The patient who is the subject of the profile
     * </p> 
	 */
	public ImmunizationRecommendation setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>recommendation</b> (Vaccine administration recommendations).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	public java.util.List<Recommendation> getRecommendation() {  
		if (myRecommendation == null) {
			myRecommendation = new java.util.ArrayList<Recommendation>();
		}
		return myRecommendation;
	}

	/**
	 * Sets the value(s) for <b>recommendation</b> (Vaccine administration recommendations)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	public ImmunizationRecommendation setRecommendation(java.util.List<Recommendation> theValue) {
		myRecommendation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>recommendation</b> (Vaccine administration recommendations)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	public Recommendation addRecommendation() {
		Recommendation newType = new Recommendation();
		getRecommendation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>recommendation</b> (Vaccine administration recommendations),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	public Recommendation getRecommendationFirstRep() {
		if (getRecommendation().isEmpty()) {
			return addRecommendation();
		}
		return getRecommendation().get(0); 
	}
  
	/**
	 * Block class for child element: <b>ImmunizationRecommendation.recommendation</b> (Vaccine administration recommendations)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration recommendations
     * </p> 
	 */
	@Block()	
	public static class Recommendation extends BaseElement implements IResourceBlock {
	
	@Child(name="date", type=DateTimeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Date recommendation created",
		formalDefinition="The date the immunization recommendation was created."
	)
	private DateTimeDt myDate;
	
	@Child(name="vaccineType", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Vaccine recommendation applies to",
		formalDefinition="Vaccine that pertains to the recommendation"
	)
	private CodeableConceptDt myVaccineType;
	
	@Child(name="doseNumber", type=IntegerDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Recommended dose number",
		formalDefinition="This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose)."
	)
	private IntegerDt myDoseNumber;
	
	@Child(name="forecastStatus", type=CodeableConceptDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Vaccine administration status",
		formalDefinition="Vaccine administration status"
	)
	private BoundCodeableConceptDt<ImmunizationRecommendationStatusCodesEnum> myForecastStatus;
	
	@Child(name="dateCriterion", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Dates governing proposed immunization",
		formalDefinition="Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc."
	)
	private java.util.List<RecommendationDateCriterion> myDateCriterion;
	
	@Child(name="protocol", order=5, min=0, max=1)	
	@Description(
		shortDefinition="Protocol used by recommendation",
		formalDefinition="Contains information about the protocol under which the vaccine was administered"
	)
	private RecommendationProtocol myProtocol;
	
	@Child(name="supportingImmunization", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Immunization.class	})
	@Description(
		shortDefinition="Past immunizations supporting recommendation",
		formalDefinition="Immunization event history that supports the status and recommendation"
	)
	private java.util.List<ResourceReferenceDt> mySupportingImmunization;
	
	@Child(name="supportingPatientInformation", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Observation.class, 		ca.uhn.fhir.model.dstu.resource.AdverseReaction.class, 		ca.uhn.fhir.model.dstu.resource.AllergyIntolerance.class	})
	@Description(
		shortDefinition="Patient observations supporting recommendation",
		formalDefinition="Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information."
	)
	private java.util.List<ResourceReferenceDt> mySupportingPatientInformation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDate,  myVaccineType,  myDoseNumber,  myForecastStatus,  myDateCriterion,  myProtocol,  mySupportingImmunization,  mySupportingPatientInformation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDate, myVaccineType, myDoseNumber, myForecastStatus, myDateCriterion, myProtocol, mySupportingImmunization, mySupportingPatientInformation);
	}

	/**
	 * Gets the value(s) for <b>date</b> (Date recommendation created).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date the immunization recommendation was created.
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Date recommendation created)
	 *
     * <p>
     * <b>Definition:</b>
     * The date the immunization recommendation was created.
     * </p> 
	 */
	public Recommendation setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (Date recommendation created)
	 *
     * <p>
     * <b>Definition:</b>
     * The date the immunization recommendation was created.
     * </p> 
	 */
	public Recommendation setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (Date recommendation created)
	 *
     * <p>
     * <b>Definition:</b>
     * The date the immunization recommendation was created.
     * </p> 
	 */
	public Recommendation setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>vaccineType</b> (Vaccine recommendation applies to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine that pertains to the recommendation
     * </p> 
	 */
	public CodeableConceptDt getVaccineType() {  
		if (myVaccineType == null) {
			myVaccineType = new CodeableConceptDt();
		}
		return myVaccineType;
	}

	/**
	 * Sets the value(s) for <b>vaccineType</b> (Vaccine recommendation applies to)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine that pertains to the recommendation
     * </p> 
	 */
	public Recommendation setVaccineType(CodeableConceptDt theValue) {
		myVaccineType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>doseNumber</b> (Recommended dose number).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
     * </p> 
	 */
	public IntegerDt getDoseNumber() {  
		if (myDoseNumber == null) {
			myDoseNumber = new IntegerDt();
		}
		return myDoseNumber;
	}

	/**
	 * Sets the value(s) for <b>doseNumber</b> (Recommended dose number)
	 *
     * <p>
     * <b>Definition:</b>
     * This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
     * </p> 
	 */
	public Recommendation setDoseNumber(IntegerDt theValue) {
		myDoseNumber = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>doseNumber</b> (Recommended dose number)
	 *
     * <p>
     * <b>Definition:</b>
     * This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
     * </p> 
	 */
	public Recommendation setDoseNumber( int theInteger) {
		myDoseNumber = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>forecastStatus</b> (Vaccine administration status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration status
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationRecommendationStatusCodesEnum> getForecastStatus() {  
		if (myForecastStatus == null) {
			myForecastStatus = new BoundCodeableConceptDt<ImmunizationRecommendationStatusCodesEnum>(ImmunizationRecommendationStatusCodesEnum.VALUESET_BINDER);
		}
		return myForecastStatus;
	}

	/**
	 * Sets the value(s) for <b>forecastStatus</b> (Vaccine administration status)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration status
     * </p> 
	 */
	public Recommendation setForecastStatus(BoundCodeableConceptDt<ImmunizationRecommendationStatusCodesEnum> theValue) {
		myForecastStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>forecastStatus</b> (Vaccine administration status)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine administration status
     * </p> 
	 */
	public Recommendation setForecastStatus(ImmunizationRecommendationStatusCodesEnum theValue) {
		getForecastStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dateCriterion</b> (Dates governing proposed immunization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	public java.util.List<RecommendationDateCriterion> getDateCriterion() {  
		if (myDateCriterion == null) {
			myDateCriterion = new java.util.ArrayList<RecommendationDateCriterion>();
		}
		return myDateCriterion;
	}

	/**
	 * Sets the value(s) for <b>dateCriterion</b> (Dates governing proposed immunization)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	public Recommendation setDateCriterion(java.util.List<RecommendationDateCriterion> theValue) {
		myDateCriterion = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dateCriterion</b> (Dates governing proposed immunization)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	public RecommendationDateCriterion addDateCriterion() {
		RecommendationDateCriterion newType = new RecommendationDateCriterion();
		getDateCriterion().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dateCriterion</b> (Dates governing proposed immunization),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	public RecommendationDateCriterion getDateCriterionFirstRep() {
		if (getDateCriterion().isEmpty()) {
			return addDateCriterion();
		}
		return getDateCriterion().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>protocol</b> (Protocol used by recommendation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol under which the vaccine was administered
     * </p> 
	 */
	public RecommendationProtocol getProtocol() {  
		if (myProtocol == null) {
			myProtocol = new RecommendationProtocol();
		}
		return myProtocol;
	}

	/**
	 * Sets the value(s) for <b>protocol</b> (Protocol used by recommendation)
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol under which the vaccine was administered
     * </p> 
	 */
	public Recommendation setProtocol(RecommendationProtocol theValue) {
		myProtocol = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>supportingImmunization</b> (Past immunizations supporting recommendation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Immunization event history that supports the status and recommendation
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSupportingImmunization() {  
		if (mySupportingImmunization == null) {
			mySupportingImmunization = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return mySupportingImmunization;
	}

	/**
	 * Sets the value(s) for <b>supportingImmunization</b> (Past immunizations supporting recommendation)
	 *
     * <p>
     * <b>Definition:</b>
     * Immunization event history that supports the status and recommendation
     * </p> 
	 */
	public Recommendation setSupportingImmunization(java.util.List<ResourceReferenceDt> theValue) {
		mySupportingImmunization = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>supportingImmunization</b> (Past immunizations supporting recommendation)
	 *
     * <p>
     * <b>Definition:</b>
     * Immunization event history that supports the status and recommendation
     * </p> 
	 */
	public ResourceReferenceDt addSupportingImmunization() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSupportingImmunization().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>supportingPatientInformation</b> (Patient observations supporting recommendation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getSupportingPatientInformation() {  
		return mySupportingPatientInformation;
	}

	/**
	 * Sets the value(s) for <b>supportingPatientInformation</b> (Patient observations supporting recommendation)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.
     * </p> 
	 */
	public Recommendation setSupportingPatientInformation(java.util.List<ResourceReferenceDt> theValue) {
		mySupportingPatientInformation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>supportingPatientInformation</b> (Patient observations supporting recommendation)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.
     * </p> 
	 */
	public ResourceReferenceDt addSupportingPatientInformation() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getSupportingPatientInformation().add(newType);
		return newType; 
	}
  

	}

	/**
	 * Block class for child element: <b>ImmunizationRecommendation.recommendation.dateCriterion</b> (Dates governing proposed immunization)
	 *
     * <p>
     * <b>Definition:</b>
     * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
     * </p> 
	 */
	@Block()	
	public static class RecommendationDateCriterion extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Type of date",
		formalDefinition="Date classification of recommendation - e.g. earliest date to give, latest date to give, etc."
	)
	private BoundCodeableConceptDt<ImmunizationRecommendationDateCriterionCodesEnum> myCode;
	
	@Child(name="value", type=DateTimeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Recommended date",
		formalDefinition="Date recommendation"
	)
	private DateTimeDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myValue);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Type of date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.
     * </p> 
	 */
	public BoundCodeableConceptDt<ImmunizationRecommendationDateCriterionCodesEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeableConceptDt<ImmunizationRecommendationDateCriterionCodesEnum>(ImmunizationRecommendationDateCriterionCodesEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Type of date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.
     * </p> 
	 */
	public RecommendationDateCriterion setCode(BoundCodeableConceptDt<ImmunizationRecommendationDateCriterionCodesEnum> theValue) {
		myCode = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Type of date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.
     * </p> 
	 */
	public RecommendationDateCriterion setCode(ImmunizationRecommendationDateCriterionCodesEnum theValue) {
		getCode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>value</b> (Recommended date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date recommendation
     * </p> 
	 */
	public DateTimeDt getValue() {  
		if (myValue == null) {
			myValue = new DateTimeDt();
		}
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value</b> (Recommended date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date recommendation
     * </p> 
	 */
	public RecommendationDateCriterion setValue(DateTimeDt theValue) {
		myValue = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>value</b> (Recommended date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date recommendation
     * </p> 
	 */
	public RecommendationDateCriterion setValue( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myValue = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>value</b> (Recommended date)
	 *
     * <p>
     * <b>Definition:</b>
     * Date recommendation
     * </p> 
	 */
	public RecommendationDateCriterion setValueWithSecondsPrecision( Date theDate) {
		myValue = new DateTimeDt(theDate); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>ImmunizationRecommendation.recommendation.protocol</b> (Protocol used by recommendation)
	 *
     * <p>
     * <b>Definition:</b>
     * Contains information about the protocol under which the vaccine was administered
     * </p> 
	 */
	@Block()	
	public static class RecommendationProtocol extends BaseElement implements IResourceBlock {
	
	@Child(name="doseSequence", type=IntegerDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Number of dose within sequence",
		formalDefinition="Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol."
	)
	private IntegerDt myDoseSequence;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Protocol details",
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
		shortDefinition="Name of vaccination series",
		formalDefinition="One possible path to achieve presumed immunity against a disease - within the context of an authority"
	)
	private StringDt mySeries;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDoseSequence,  myDescription,  myAuthority,  mySeries);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDoseSequence, myDescription, myAuthority, mySeries);
	}

	/**
	 * Gets the value(s) for <b>doseSequence</b> (Number of dose within sequence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
     * </p> 
	 */
	public IntegerDt getDoseSequence() {  
		if (myDoseSequence == null) {
			myDoseSequence = new IntegerDt();
		}
		return myDoseSequence;
	}

	/**
	 * Sets the value(s) for <b>doseSequence</b> (Number of dose within sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
     * </p> 
	 */
	public RecommendationProtocol setDoseSequence(IntegerDt theValue) {
		myDoseSequence = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>doseSequence</b> (Number of dose within sequence)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
     * </p> 
	 */
	public RecommendationProtocol setDoseSequence( int theInteger) {
		myDoseSequence = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (Protocol details).
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
	 * Sets the value(s) for <b>description</b> (Protocol details)
	 *
     * <p>
     * <b>Definition:</b>
     * Contains the description about the protocol under which the vaccine was administered
     * </p> 
	 */
	public RecommendationProtocol setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Protocol details)
	 *
     * <p>
     * <b>Definition:</b>
     * Contains the description about the protocol under which the vaccine was administered
     * </p> 
	 */
	public RecommendationProtocol setDescription( String theString) {
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
	public RecommendationProtocol setAuthority(ResourceReferenceDt theValue) {
		myAuthority = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>series</b> (Name of vaccination series).
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
	 * Sets the value(s) for <b>series</b> (Name of vaccination series)
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public RecommendationProtocol setSeries(StringDt theValue) {
		mySeries = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>series</b> (Name of vaccination series)
	 *
     * <p>
     * <b>Definition:</b>
     * One possible path to achieve presumed immunity against a disease - within the context of an authority
     * </p> 
	 */
	public RecommendationProtocol setSeries( String theString) {
		mySeries = new StringDt(theString); 
		return this; 
	}

 

	}





}
