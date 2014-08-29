















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
import ca.uhn.fhir.model.dstu.valueset.CausalityExpectationEnum;
import ca.uhn.fhir.model.dstu.valueset.ExposureTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ReactionSeverityEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>AdverseReaction</b> Resource
 * (Specific reactions to a substance)
 *
 * <p>
 * <b>Definition:</b>
 * Records an unexpected reaction suspected to be related to the exposure of the reaction subject to a substance
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Used to track reactions when it is unknown the exact cause but there's a desire to flag/track potential causes.  Also used to capture reactions that are significant for inclusion in the health record or as evidence for an allergy or intolerance.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/AdverseReaction">http://hl7.org/fhir/profiles/AdverseReaction</a> 
 * </p>
 *
 */
@ResourceDef(name="AdverseReaction", profile="http://hl7.org/fhir/profiles/AdverseReaction", id="adversereaction")
public class AdverseReaction extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>symptom</b>
	 * <p>
	 * Description: <b>One of the symptoms of the reaction</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReaction.symptom.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="symptom", path="AdverseReaction.symptom.code", description="One of the symptoms of the reaction", type="token"  )
	public static final String SP_SYMPTOM = "symptom";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>symptom</b>
	 * <p>
	 * Description: <b>One of the symptoms of the reaction</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>AdverseReaction.symptom.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SYMPTOM = new TokenClientParam(SP_SYMPTOM);

	/**
	 * Search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b>The name or code of the substance that produces the sensitivity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReaction.exposure.substance</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="substance", path="AdverseReaction.exposure.substance", description="The name or code of the substance that produces the sensitivity", type="reference"  )
	public static final String SP_SUBSTANCE = "substance";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>substance</b>
	 * <p>
	 * Description: <b>The name or code of the substance that produces the sensitivity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReaction.exposure.substance</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBSTANCE = new ReferenceClientParam(SP_SUBSTANCE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReaction.exposure.substance</b>".
	 */
	public static final Include INCLUDE_EXPOSURE_SUBSTANCE = new Include("AdverseReaction.exposure.substance");

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The date of the reaction</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReaction.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="AdverseReaction.date", description="The date of the reaction", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The date of the reaction</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>AdverseReaction.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the sensitivity is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReaction.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="AdverseReaction.subject", description="The subject that the sensitivity is about", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject that the sensitivity is about</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>AdverseReaction.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>AdverseReaction.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("AdverseReaction.subject");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this adverse reaction",
		formalDefinition="This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="date", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="When the reaction occurred",
		formalDefinition="The date (and possibly time) when the reaction began"
	)
	private DateTimeDt myDate;
	
	@Child(name="subject", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who had the reaction",
		formalDefinition="The subject of the adverse reaction"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="didNotOccurFlag", type=BooleanDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Indicates lack of reaction",
		formalDefinition="If true, indicates that no reaction occurred."
	)
	private BooleanDt myDidNotOccurFlag;
	
	@Child(name="recorder", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who recorded the reaction",
		formalDefinition="Identifies the individual responsible for the information in the reaction record."
	)
	private ResourceReferenceDt myRecorder;
	
	@Child(name="symptom", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="What was reaction?",
		formalDefinition="The signs and symptoms that were observed as part of the reaction"
	)
	private java.util.List<Symptom> mySymptom;
	
	@Child(name="exposure", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Suspected substance",
		formalDefinition="An exposure to a substance that preceded a reaction occurrence"
	)
	private java.util.List<Exposure> myExposure;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDate,  mySubject,  myDidNotOccurFlag,  myRecorder,  mySymptom,  myExposure);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDate, mySubject, myDidNotOccurFlag, myRecorder, mySymptom, myExposure);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this adverse reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Ids for this adverse reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public AdverseReaction setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this adverse reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Ids for this adverse reaction),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this adverse reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public AdverseReaction addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this adverse reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this reaction that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public AdverseReaction addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (When the reaction occurred).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the reaction began
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (When the reaction occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the reaction began
     * </p> 
	 */
	public AdverseReaction setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (When the reaction occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the reaction began
     * </p> 
	 */
	public AdverseReaction setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When the reaction occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * The date (and possibly time) when the reaction began
     * </p> 
	 */
	public AdverseReaction setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Who had the reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the adverse reaction
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who had the reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the adverse reaction
     * </p> 
	 */
	public AdverseReaction setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>didNotOccurFlag</b> (Indicates lack of reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that no reaction occurred.
     * </p> 
	 */
	public BooleanDt getDidNotOccurFlag() {  
		if (myDidNotOccurFlag == null) {
			myDidNotOccurFlag = new BooleanDt();
		}
		return myDidNotOccurFlag;
	}

	/**
	 * Sets the value(s) for <b>didNotOccurFlag</b> (Indicates lack of reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that no reaction occurred.
     * </p> 
	 */
	public AdverseReaction setDidNotOccurFlag(BooleanDt theValue) {
		myDidNotOccurFlag = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>didNotOccurFlag</b> (Indicates lack of reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that no reaction occurred.
     * </p> 
	 */
	public AdverseReaction setDidNotOccurFlag( boolean theBoolean) {
		myDidNotOccurFlag = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>recorder</b> (Who recorded the reaction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the individual responsible for the information in the reaction record.
     * </p> 
	 */
	public ResourceReferenceDt getRecorder() {  
		if (myRecorder == null) {
			myRecorder = new ResourceReferenceDt();
		}
		return myRecorder;
	}

	/**
	 * Sets the value(s) for <b>recorder</b> (Who recorded the reaction)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the individual responsible for the information in the reaction record.
     * </p> 
	 */
	public AdverseReaction setRecorder(ResourceReferenceDt theValue) {
		myRecorder = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>symptom</b> (What was reaction?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	public java.util.List<Symptom> getSymptom() {  
		if (mySymptom == null) {
			mySymptom = new java.util.ArrayList<Symptom>();
		}
		return mySymptom;
	}

	/**
	 * Sets the value(s) for <b>symptom</b> (What was reaction?)
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	public AdverseReaction setSymptom(java.util.List<Symptom> theValue) {
		mySymptom = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>symptom</b> (What was reaction?)
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	public Symptom addSymptom() {
		Symptom newType = new Symptom();
		getSymptom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>symptom</b> (What was reaction?),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	public Symptom getSymptomFirstRep() {
		if (getSymptom().isEmpty()) {
			return addSymptom();
		}
		return getSymptom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>exposure</b> (Suspected substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	public java.util.List<Exposure> getExposure() {  
		if (myExposure == null) {
			myExposure = new java.util.ArrayList<Exposure>();
		}
		return myExposure;
	}

	/**
	 * Sets the value(s) for <b>exposure</b> (Suspected substance)
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	public AdverseReaction setExposure(java.util.List<Exposure> theValue) {
		myExposure = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>exposure</b> (Suspected substance)
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	public Exposure addExposure() {
		Exposure newType = new Exposure();
		getExposure().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>exposure</b> (Suspected substance),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	public Exposure getExposureFirstRep() {
		if (getExposure().isEmpty()) {
			return addExposure();
		}
		return getExposure().get(0); 
	}
  
	/**
	 * Block class for child element: <b>AdverseReaction.symptom</b> (What was reaction?)
	 *
     * <p>
     * <b>Definition:</b>
     * The signs and symptoms that were observed as part of the reaction
     * </p> 
	 */
	@Block()	
	public static class Symptom extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="E.g. Rash, vomiting",
		formalDefinition="Indicates the specific sign or symptom that was observed"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="severity", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="severe | serious | moderate | minor",
		formalDefinition="The severity of the sign or symptom"
	)
	private BoundCodeDt<ReactionSeverityEnum> mySeverity;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  mySeverity);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, mySeverity);
	}

	/**
	 * Gets the value(s) for <b>code</b> (E.g. Rash, vomiting).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the specific sign or symptom that was observed
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (E.g. Rash, vomiting)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the specific sign or symptom that was observed
     * </p> 
	 */
	public Symptom setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>severity</b> (severe | serious | moderate | minor).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The severity of the sign or symptom
     * </p> 
	 */
	public BoundCodeDt<ReactionSeverityEnum> getSeverity() {  
		if (mySeverity == null) {
			mySeverity = new BoundCodeDt<ReactionSeverityEnum>(ReactionSeverityEnum.VALUESET_BINDER);
		}
		return mySeverity;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (severe | serious | moderate | minor)
	 *
     * <p>
     * <b>Definition:</b>
     * The severity of the sign or symptom
     * </p> 
	 */
	public Symptom setSeverity(BoundCodeDt<ReactionSeverityEnum> theValue) {
		mySeverity = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (severe | serious | moderate | minor)
	 *
     * <p>
     * <b>Definition:</b>
     * The severity of the sign or symptom
     * </p> 
	 */
	public Symptom setSeverity(ReactionSeverityEnum theValue) {
		getSeverity().setValueAsEnum(theValue);
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>AdverseReaction.exposure</b> (Suspected substance)
	 *
     * <p>
     * <b>Definition:</b>
     * An exposure to a substance that preceded a reaction occurrence
     * </p> 
	 */
	@Block()	
	public static class Exposure extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="date", type=DateTimeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="When the exposure occurred",
		formalDefinition="Identifies the initial date of the exposure that is suspected to be related to the reaction"
	)
	private DateTimeDt myDate;
	
	@Child(name="type", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="drugadmin | immuniz | coincidental",
		formalDefinition="The type of exposure: Drug Administration, Immunization, Coincidental"
	)
	private BoundCodeDt<ExposureTypeEnum> myType;
	
	@Child(name="causalityExpectation", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="likely | unlikely | confirmed | unknown",
		formalDefinition="A statement of how confident that the recorder was that this exposure caused the reaction"
	)
	private BoundCodeDt<CausalityExpectationEnum> myCausalityExpectation;
	
	@Child(name="substance", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Substance.class	})
	@Description(
		shortDefinition="Presumed causative substance",
		formalDefinition="Substance that is presumed to have caused the adverse reaction"
	)
	private ResourceReferenceDt mySubstance;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDate,  myType,  myCausalityExpectation,  mySubstance);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDate, myType, myCausalityExpectation, mySubstance);
	}

	/**
	 * Gets the value(s) for <b>date</b> (When the exposure occurred).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the initial date of the exposure that is suspected to be related to the reaction
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (When the exposure occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the initial date of the exposure that is suspected to be related to the reaction
     * </p> 
	 */
	public Exposure setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (When the exposure occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the initial date of the exposure that is suspected to be related to the reaction
     * </p> 
	 */
	public Exposure setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (When the exposure occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the initial date of the exposure that is suspected to be related to the reaction
     * </p> 
	 */
	public Exposure setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (drugadmin | immuniz | coincidental).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of exposure: Drug Administration, Immunization, Coincidental
     * </p> 
	 */
	public BoundCodeDt<ExposureTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<ExposureTypeEnum>(ExposureTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (drugadmin | immuniz | coincidental)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of exposure: Drug Administration, Immunization, Coincidental
     * </p> 
	 */
	public Exposure setType(BoundCodeDt<ExposureTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (drugadmin | immuniz | coincidental)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of exposure: Drug Administration, Immunization, Coincidental
     * </p> 
	 */
	public Exposure setType(ExposureTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>causalityExpectation</b> (likely | unlikely | confirmed | unknown).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A statement of how confident that the recorder was that this exposure caused the reaction
     * </p> 
	 */
	public BoundCodeDt<CausalityExpectationEnum> getCausalityExpectation() {  
		if (myCausalityExpectation == null) {
			myCausalityExpectation = new BoundCodeDt<CausalityExpectationEnum>(CausalityExpectationEnum.VALUESET_BINDER);
		}
		return myCausalityExpectation;
	}

	/**
	 * Sets the value(s) for <b>causalityExpectation</b> (likely | unlikely | confirmed | unknown)
	 *
     * <p>
     * <b>Definition:</b>
     * A statement of how confident that the recorder was that this exposure caused the reaction
     * </p> 
	 */
	public Exposure setCausalityExpectation(BoundCodeDt<CausalityExpectationEnum> theValue) {
		myCausalityExpectation = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>causalityExpectation</b> (likely | unlikely | confirmed | unknown)
	 *
     * <p>
     * <b>Definition:</b>
     * A statement of how confident that the recorder was that this exposure caused the reaction
     * </p> 
	 */
	public Exposure setCausalityExpectation(CausalityExpectationEnum theValue) {
		getCausalityExpectation().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>substance</b> (Presumed causative substance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Substance that is presumed to have caused the adverse reaction
     * </p> 
	 */
	public ResourceReferenceDt getSubstance() {  
		if (mySubstance == null) {
			mySubstance = new ResourceReferenceDt();
		}
		return mySubstance;
	}

	/**
	 * Sets the value(s) for <b>substance</b> (Presumed causative substance)
	 *
     * <p>
     * <b>Definition:</b>
     * Substance that is presumed to have caused the adverse reaction
     * </p> 
	 */
	public Exposure setSubstance(ResourceReferenceDt theValue) {
		mySubstance = theValue;
		return this;
	}

  

	}




}
