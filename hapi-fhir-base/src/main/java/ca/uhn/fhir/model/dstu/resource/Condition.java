















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
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.AgeDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.ConditionRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConditionStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateParam;
import ca.uhn.fhir.rest.gclient.Include;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.TokenParam;


/**
 * HAPI/FHIR <b>Condition</b> Resource
 * (Detailed information about conditions, problems or diagnoses)
 *
 * <p>
 * <b>Definition:</b>
 * Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a Diagnosis during an Encounter; populating a problem List or a Summary Statement, such as a Discharge Summary
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Condition">http://hl7.org/fhir/profiles/Condition</a> 
 * </p>
 *
 */
@ResourceDef(name="Condition", profile="http://hl7.org/fhir/profiles/Condition", id="condition")
public class Condition extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>Code for the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="Condition.code", description="Code for the condition", type="token")
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>Code for the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.code</b><br/>
	 * </p>
	 */
	public static final TokenParam CODE = new TokenParam(SP_CODE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Condition.status", description="The status of the condition", type="token")
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.status</b><br/>
	 * </p>
	 */
	public static final TokenParam STATUS = new TokenParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>severity</b>
	 * <p>
	 * Description: <b>The severity of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.severity</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="severity", path="Condition.severity", description="The severity of the condition", type="token")
	public static final String SP_SEVERITY = "severity";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>severity</b>
	 * <p>
	 * Description: <b>The severity of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.severity</b><br/>
	 * </p>
	 */
	public static final TokenParam SEVERITY = new TokenParam(SP_SEVERITY);

	/**
	 * Search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b>The category of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.category</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="category", path="Condition.category", description="The category of the condition", type="token")
	public static final String SP_CATEGORY = "category";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b>The category of the condition</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.category</b><br/>
	 * </p>
	 */
	public static final TokenParam CATEGORY = new TokenParam(SP_CATEGORY);

	/**
	 * Search parameter constant for <b>onset</b>
	 * <p>
	 * Description: <b>When the Condition started (if started on a date)</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Condition.onset[x]</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="onset", path="Condition.onset[x]", description="When the Condition started (if started on a date)", type="date")
	public static final String SP_ONSET = "onset";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>onset</b>
	 * <p>
	 * Description: <b>When the Condition started (if started on a date)</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Condition.onset[x]</b><br/>
	 * </p>
	 */
	public static final DateParam ONSET = new DateParam(SP_ONSET);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Condition.subject", description="", type="reference")
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceParam SUBJECT = new ReferenceParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Condition.subject");

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="Condition.encounter", description="", type="reference")
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceParam ENCOUNTER = new ReferenceParam(SP_ENCOUNTER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("Condition.encounter");

	/**
	 * Search parameter constant for <b>asserter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.asserter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="asserter", path="Condition.asserter", description="", type="reference")
	public static final String SP_ASSERTER = "asserter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>asserter</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.asserter</b><br/>
	 * </p>
	 */
	public static final ReferenceParam ASSERTER = new ReferenceParam(SP_ASSERTER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.asserter</b>".
	 */
	public static final Include INCLUDE_ASSERTER = new Include("Condition.asserter");

	/**
	 * Search parameter constant for <b>date-asserted</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Condition.dateAsserted</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date-asserted", path="Condition.dateAsserted", description="", type="date")
	public static final String SP_DATE_ASSERTED = "date-asserted";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date-asserted</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Condition.dateAsserted</b><br/>
	 * </p>
	 */
	public static final DateParam DATE_ASSERTED = new DateParam(SP_DATE_ASSERTED);

	/**
	 * Search parameter constant for <b>evidence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.evidence.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="evidence", path="Condition.evidence.code", description="", type="token")
	public static final String SP_EVIDENCE = "evidence";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>evidence</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.evidence.code</b><br/>
	 * </p>
	 */
	public static final TokenParam EVIDENCE = new TokenParam(SP_EVIDENCE);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.location.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Condition.location.code", description="", type="token")
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.location.code</b><br/>
	 * </p>
	 */
	public static final TokenParam LOCATION = new TokenParam(SP_LOCATION);

	/**
	 * Search parameter constant for <b>related-item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.relatedItem.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="related-item", path="Condition.relatedItem.target", description="", type="reference")
	public static final String SP_RELATED_ITEM = "related-item";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>related-item</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Condition.relatedItem.target</b><br/>
	 * </p>
	 */
	public static final ReferenceParam RELATED_ITEM = new ReferenceParam(SP_RELATED_ITEM);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Condition.relatedItem.target</b>".
	 */
	public static final Include INCLUDE_RELATEDITEM_TARGET = new Include("Condition.relatedItem.target");

	/**
	 * Search parameter constant for <b>stage</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.stage.summary</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="stage", path="Condition.stage.summary", description="", type="token")
	public static final String SP_STAGE = "stage";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>stage</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.stage.summary</b><br/>
	 * </p>
	 */
	public static final TokenParam STAGE = new TokenParam(SP_STAGE);

	/**
	 * Search parameter constant for <b>related-code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.relatedItem.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="related-code", path="Condition.relatedItem.code", description="", type="token")
	public static final String SP_RELATED_CODE = "related-code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>related-code</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Condition.relatedItem.code</b><br/>
	 * </p>
	 */
	public static final TokenParam RELATED_CODE = new TokenParam(SP_RELATED_CODE);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this condition",
		formalDefinition="This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who has the condition?",
		formalDefinition="Indicates the patient who the condition record is associated with"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="encounter", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Encounter.class	})
	@Description(
		shortDefinition="Encounter when condition first asserted",
		formalDefinition="Encounter during which the condition was first asserted"
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="asserter", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Person who asserts this condition",
		formalDefinition="Person who takes responsibility for asserting the existence of the condition as part of the electronic record"
	)
	private ResourceReferenceDt myAsserter;
	
	@Child(name="dateAsserted", type=DateDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="When first detected/suspected/entered",
		formalDefinition="Estimated or actual date the condition/problem/diagnosis was first detected/suspected"
	)
	private DateDt myDateAsserted;
	
	@Child(name="code", type=CodeableConceptDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Identification of the condition, problem or diagnosis",
		formalDefinition="Identification of the condition, problem or diagnosis."
	)
	private CodeableConceptDt myCode;
	
	@Child(name="category", type=CodeableConceptDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="E.g. complaint | symptom | finding | diagnosis",
		formalDefinition="A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis"
	)
	private CodeableConceptDt myCategory;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="provisional | working | confirmed | refuted",
		formalDefinition="The clinical status of the condition"
	)
	private BoundCodeDt<ConditionStatusEnum> myStatus;
	
	@Child(name="certainty", type=CodeableConceptDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Degree of confidence",
		formalDefinition="The degree of confidence that this condition is correct"
	)
	private CodeableConceptDt myCertainty;
	
	@Child(name="severity", type=CodeableConceptDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Subjective severity of condition",
		formalDefinition="A subjective assessment of the severity of the condition as evaluated by the clinician."
	)
	private CodeableConceptDt mySeverity;
	
	@Child(name="onset", order=10, min=0, max=1, type={
		DateDt.class, 		AgeDt.class	})
	@Description(
		shortDefinition="Estimated or actual date, or age",
		formalDefinition="Estimated or actual date the condition began, in the opinion of the clinician"
	)
	private IDatatype myOnset;
	
	@Child(name="abatement", order=11, min=0, max=1, type={
		DateDt.class, 		AgeDt.class, 		BooleanDt.class	})
	@Description(
		shortDefinition="If/when in resolution/remission",
		formalDefinition="The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate."
	)
	private IDatatype myAbatement;
	
	@Child(name="stage", order=12, min=0, max=1)	
	@Description(
		shortDefinition="Stage/grade, usually assessed formally",
		formalDefinition="Clinical stage or grade of a condition. May include formal severity assessments"
	)
	private Stage myStage;
	
	@Child(name="evidence", order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Supporting evidence",
		formalDefinition="Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed"
	)
	private java.util.List<Evidence> myEvidence;
	
	@Child(name="location", order=14, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Anatomical location, if relevant",
		formalDefinition="The anatomical location where this condition manifests itself"
	)
	private java.util.List<Location> myLocation;
	
	@Child(name="relatedItem", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Causes or precedents for this Condition",
		formalDefinition="Further conditions, problems, diagnoses, procedures or events that are related in some way to this condition, or the substance that caused/triggered this Condition"
	)
	private java.util.List<RelatedItem> myRelatedItem;
	
	@Child(name="notes", type=StringDt.class, order=16, min=0, max=1)	
	@Description(
		shortDefinition="Additional information about the Condition",
		formalDefinition="Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis"
	)
	private StringDt myNotes;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  mySubject,  myEncounter,  myAsserter,  myDateAsserted,  myCode,  myCategory,  myStatus,  myCertainty,  mySeverity,  myOnset,  myAbatement,  myStage,  myEvidence,  myLocation,  myRelatedItem,  myNotes);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, mySubject, myEncounter, myAsserter, myDateAsserted, myCode, myCategory, myStatus, myCertainty, mySeverity, myOnset, myAbatement, myStage, myEvidence, myLocation, myRelatedItem, myNotes);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this condition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Ids for this condition)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public Condition setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this condition)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Ids for this condition),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this condition)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Condition addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this condition)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Condition addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Who has the condition?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the patient who the condition record is associated with
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who has the condition?)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the patient who the condition record is associated with
     * </p> 
	 */
	public Condition setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>encounter</b> (Encounter when condition first asserted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Encounter during which the condition was first asserted
     * </p> 
	 */
	public ResourceReferenceDt getEncounter() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}

	/**
	 * Sets the value(s) for <b>encounter</b> (Encounter when condition first asserted)
	 *
     * <p>
     * <b>Definition:</b>
     * Encounter during which the condition was first asserted
     * </p> 
	 */
	public Condition setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>asserter</b> (Person who asserts this condition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Person who takes responsibility for asserting the existence of the condition as part of the electronic record
     * </p> 
	 */
	public ResourceReferenceDt getAsserter() {  
		return myAsserter;
	}

	/**
	 * Sets the value(s) for <b>asserter</b> (Person who asserts this condition)
	 *
     * <p>
     * <b>Definition:</b>
     * Person who takes responsibility for asserting the existence of the condition as part of the electronic record
     * </p> 
	 */
	public Condition setAsserter(ResourceReferenceDt theValue) {
		myAsserter = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dateAsserted</b> (When first detected/suspected/entered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected
     * </p> 
	 */
	public DateDt getDateAsserted() {  
		if (myDateAsserted == null) {
			myDateAsserted = new DateDt();
		}
		return myDateAsserted;
	}

	/**
	 * Sets the value(s) for <b>dateAsserted</b> (When first detected/suspected/entered)
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected
     * </p> 
	 */
	public Condition setDateAsserted(DateDt theValue) {
		myDateAsserted = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dateAsserted</b> (When first detected/suspected/entered)
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected
     * </p> 
	 */
	public Condition setDateAsserted( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateAsserted = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateAsserted</b> (When first detected/suspected/entered)
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected
     * </p> 
	 */
	public Condition setDateAssertedWithDayPrecision( Date theDate) {
		myDateAsserted = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Identification of the condition, problem or diagnosis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the condition, problem or diagnosis.
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Identification of the condition, problem or diagnosis)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the condition, problem or diagnosis.
     * </p> 
	 */
	public Condition setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>category</b> (E.g. complaint | symptom | finding | diagnosis).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis
     * </p> 
	 */
	public CodeableConceptDt getCategory() {  
		if (myCategory == null) {
			myCategory = new CodeableConceptDt();
		}
		return myCategory;
	}

	/**
	 * Sets the value(s) for <b>category</b> (E.g. complaint | symptom | finding | diagnosis)
	 *
     * <p>
     * <b>Definition:</b>
     * A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis
     * </p> 
	 */
	public Condition setCategory(CodeableConceptDt theValue) {
		myCategory = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>status</b> (provisional | working | confirmed | refuted).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical status of the condition
     * </p> 
	 */
	public BoundCodeDt<ConditionStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ConditionStatusEnum>(ConditionStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (provisional | working | confirmed | refuted)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical status of the condition
     * </p> 
	 */
	public Condition setStatus(BoundCodeDt<ConditionStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (provisional | working | confirmed | refuted)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical status of the condition
     * </p> 
	 */
	public Condition setStatus(ConditionStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>certainty</b> (Degree of confidence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The degree of confidence that this condition is correct
     * </p> 
	 */
	public CodeableConceptDt getCertainty() {  
		if (myCertainty == null) {
			myCertainty = new CodeableConceptDt();
		}
		return myCertainty;
	}

	/**
	 * Sets the value(s) for <b>certainty</b> (Degree of confidence)
	 *
     * <p>
     * <b>Definition:</b>
     * The degree of confidence that this condition is correct
     * </p> 
	 */
	public Condition setCertainty(CodeableConceptDt theValue) {
		myCertainty = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>severity</b> (Subjective severity of condition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A subjective assessment of the severity of the condition as evaluated by the clinician.
     * </p> 
	 */
	public CodeableConceptDt getSeverity() {  
		if (mySeverity == null) {
			mySeverity = new CodeableConceptDt();
		}
		return mySeverity;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (Subjective severity of condition)
	 *
     * <p>
     * <b>Definition:</b>
     * A subjective assessment of the severity of the condition as evaluated by the clinician.
     * </p> 
	 */
	public Condition setSeverity(CodeableConceptDt theValue) {
		mySeverity = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>onset[x]</b> (Estimated or actual date, or age).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition began, in the opinion of the clinician
     * </p> 
	 */
	public IDatatype getOnset() {  
		return myOnset;
	}

	/**
	 * Sets the value(s) for <b>onset[x]</b> (Estimated or actual date, or age)
	 *
     * <p>
     * <b>Definition:</b>
     * Estimated or actual date the condition began, in the opinion of the clinician
     * </p> 
	 */
	public Condition setOnset(IDatatype theValue) {
		myOnset = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>abatement[x]</b> (If/when in resolution/remission).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.
     * </p> 
	 */
	public IDatatype getAbatement() {  
		return myAbatement;
	}

	/**
	 * Sets the value(s) for <b>abatement[x]</b> (If/when in resolution/remission)
	 *
     * <p>
     * <b>Definition:</b>
     * The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.
     * </p> 
	 */
	public Condition setAbatement(IDatatype theValue) {
		myAbatement = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>stage</b> (Stage/grade, usually assessed formally).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical stage or grade of a condition. May include formal severity assessments
     * </p> 
	 */
	public Stage getStage() {  
		if (myStage == null) {
			myStage = new Stage();
		}
		return myStage;
	}

	/**
	 * Sets the value(s) for <b>stage</b> (Stage/grade, usually assessed formally)
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical stage or grade of a condition. May include formal severity assessments
     * </p> 
	 */
	public Condition setStage(Stage theValue) {
		myStage = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>evidence</b> (Supporting evidence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	public java.util.List<Evidence> getEvidence() {  
		if (myEvidence == null) {
			myEvidence = new java.util.ArrayList<Evidence>();
		}
		return myEvidence;
	}

	/**
	 * Sets the value(s) for <b>evidence</b> (Supporting evidence)
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	public Condition setEvidence(java.util.List<Evidence> theValue) {
		myEvidence = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>evidence</b> (Supporting evidence)
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	public Evidence addEvidence() {
		Evidence newType = new Evidence();
		getEvidence().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>evidence</b> (Supporting evidence),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	public Evidence getEvidenceFirstRep() {
		if (getEvidence().isEmpty()) {
			return addEvidence();
		}
		return getEvidence().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>location</b> (Anatomical location, if relevant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	public java.util.List<Location> getLocation() {  
		if (myLocation == null) {
			myLocation = new java.util.ArrayList<Location>();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (Anatomical location, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	public Condition setLocation(java.util.List<Location> theValue) {
		myLocation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>location</b> (Anatomical location, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	public Location addLocation() {
		Location newType = new Location();
		getLocation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>location</b> (Anatomical location, if relevant),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	public Location getLocationFirstRep() {
		if (getLocation().isEmpty()) {
			return addLocation();
		}
		return getLocation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>relatedItem</b> (Causes or precedents for this Condition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events that are related in some way to this condition, or the substance that caused/triggered this Condition
     * </p> 
	 */
	public java.util.List<RelatedItem> getRelatedItem() {  
		if (myRelatedItem == null) {
			myRelatedItem = new java.util.ArrayList<RelatedItem>();
		}
		return myRelatedItem;
	}

	/**
	 * Sets the value(s) for <b>relatedItem</b> (Causes or precedents for this Condition)
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events that are related in some way to this condition, or the substance that caused/triggered this Condition
     * </p> 
	 */
	public Condition setRelatedItem(java.util.List<RelatedItem> theValue) {
		myRelatedItem = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>relatedItem</b> (Causes or precedents for this Condition)
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events that are related in some way to this condition, or the substance that caused/triggered this Condition
     * </p> 
	 */
	public RelatedItem addRelatedItem() {
		RelatedItem newType = new RelatedItem();
		getRelatedItem().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relatedItem</b> (Causes or precedents for this Condition),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events that are related in some way to this condition, or the substance that caused/triggered this Condition
     * </p> 
	 */
	public RelatedItem getRelatedItemFirstRep() {
		if (getRelatedItem().isEmpty()) {
			return addRelatedItem();
		}
		return getRelatedItem().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>notes</b> (Additional information about the Condition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis
     * </p> 
	 */
	public StringDt getNotes() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	/**
	 * Sets the value(s) for <b>notes</b> (Additional information about the Condition)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis
     * </p> 
	 */
	public Condition setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>notes</b> (Additional information about the Condition)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis
     * </p> 
	 */
	public Condition setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Condition.stage</b> (Stage/grade, usually assessed formally)
	 *
     * <p>
     * <b>Definition:</b>
     * Clinical stage or grade of a condition. May include formal severity assessments
     * </p> 
	 */
	@Block()	
	public static class Stage extends BaseElement implements IResourceBlock {
	
	@Child(name="summary", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Simple summary (disease specific)",
		formalDefinition="A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific"
	)
	private CodeableConceptDt mySummary;
	
	@Child(name="assessment", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="Formal record of assessment",
		formalDefinition="Reference to a formal record of the evidence on which the staging assessment is based"
	)
	private java.util.List<ResourceReferenceDt> myAssessment;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySummary,  myAssessment);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySummary, myAssessment);
	}

	/**
	 * Gets the value(s) for <b>summary</b> (Simple summary (disease specific)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific
     * </p> 
	 */
	public CodeableConceptDt getSummary() {  
		if (mySummary == null) {
			mySummary = new CodeableConceptDt();
		}
		return mySummary;
	}

	/**
	 * Sets the value(s) for <b>summary</b> (Simple summary (disease specific))
	 *
     * <p>
     * <b>Definition:</b>
     * A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific
     * </p> 
	 */
	public Stage setSummary(CodeableConceptDt theValue) {
		mySummary = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>assessment</b> (Formal record of assessment).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a formal record of the evidence on which the staging assessment is based
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAssessment() {  
		if (myAssessment == null) {
			myAssessment = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAssessment;
	}

	/**
	 * Sets the value(s) for <b>assessment</b> (Formal record of assessment)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a formal record of the evidence on which the staging assessment is based
     * </p> 
	 */
	public Stage setAssessment(java.util.List<ResourceReferenceDt> theValue) {
		myAssessment = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>assessment</b> (Formal record of assessment)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a formal record of the evidence on which the staging assessment is based
     * </p> 
	 */
	public ResourceReferenceDt addAssessment() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAssessment().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Condition.evidence</b> (Supporting evidence)
	 *
     * <p>
     * <b>Definition:</b>
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed
     * </p> 
	 */
	@Block()	
	public static class Evidence extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Manifestation/symptom",
		formalDefinition="A manifestation or symptom that led to the recording of this condition"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="detail", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="Supporting information found elsewhere",
		formalDefinition="Links to other relevant information, including pathology reports"
	)
	private java.util.List<ResourceReferenceDt> myDetail;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDetail);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myDetail);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Manifestation/symptom).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A manifestation or symptom that led to the recording of this condition
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Manifestation/symptom)
	 *
     * <p>
     * <b>Definition:</b>
     * A manifestation or symptom that led to the recording of this condition
     * </p> 
	 */
	public Evidence setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>detail</b> (Supporting information found elsewhere).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Links to other relevant information, including pathology reports
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDetail() {  
		if (myDetail == null) {
			myDetail = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> (Supporting information found elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * Links to other relevant information, including pathology reports
     * </p> 
	 */
	public Evidence setDetail(java.util.List<ResourceReferenceDt> theValue) {
		myDetail = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>detail</b> (Supporting information found elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * Links to other relevant information, including pathology reports
     * </p> 
	 */
	public ResourceReferenceDt addDetail() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDetail().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Condition.location</b> (Anatomical location, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The anatomical location where this condition manifests itself
     * </p> 
	 */
	@Block()	
	public static class Location extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Location - may include laterality",
		formalDefinition="Code that identifies the structural location"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="detail", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Precise location details",
		formalDefinition="Detailed anatomical location information"
	)
	private StringDt myDetail;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDetail);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myDetail);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Location - may include laterality).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the structural location
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Location - may include laterality)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the structural location
     * </p> 
	 */
	public Location setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>detail</b> (Precise location details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed anatomical location information
     * </p> 
	 */
	public StringDt getDetail() {  
		if (myDetail == null) {
			myDetail = new StringDt();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> (Precise location details)
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed anatomical location information
     * </p> 
	 */
	public Location setDetail(StringDt theValue) {
		myDetail = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>detail</b> (Precise location details)
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed anatomical location information
     * </p> 
	 */
	public Location setDetail( String theString) {
		myDetail = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Condition.relatedItem</b> (Causes or precedents for this Condition)
	 *
     * <p>
     * <b>Definition:</b>
     * Further conditions, problems, diagnoses, procedures or events that are related in some way to this condition, or the substance that caused/triggered this Condition
     * </p> 
	 */
	@Block()	
	public static class RelatedItem extends BaseElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="due-to | following",
		formalDefinition="The type of relationship that this condition has to the related item"
	)
	private BoundCodeDt<ConditionRelationshipTypeEnum> myType;
	
	@Child(name="code", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Relationship target by means of a predefined code",
		formalDefinition="Code that identifies the target of this relationship. The code takes the place of a detailed instance target"
	)
	private CodeableConceptDt myCode;
	
	@Child(name="target", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Condition.class, 		ca.uhn.fhir.model.dstu.resource.Procedure.class, 		ca.uhn.fhir.model.dstu.resource.MedicationAdministration.class, 		ca.uhn.fhir.model.dstu.resource.Immunization.class, 		ca.uhn.fhir.model.dstu.resource.MedicationStatement.class	})
	@Description(
		shortDefinition="Relationship target resource",
		formalDefinition="Target of the relationship"
	)
	private ResourceReferenceDt myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myCode,  myTarget);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myCode, myTarget);
	}

	/**
	 * Gets the value(s) for <b>type</b> (due-to | following).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this condition has to the related item
     * </p> 
	 */
	public BoundCodeDt<ConditionRelationshipTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<ConditionRelationshipTypeEnum>(ConditionRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (due-to | following)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this condition has to the related item
     * </p> 
	 */
	public RelatedItem setType(BoundCodeDt<ConditionRelationshipTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (due-to | following)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this condition has to the related item
     * </p> 
	 */
	public RelatedItem setType(ConditionRelationshipTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>code</b> (Relationship target by means of a predefined code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the target of this relationship. The code takes the place of a detailed instance target
     * </p> 
	 */
	public CodeableConceptDt getCode() {  
		if (myCode == null) {
			myCode = new CodeableConceptDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Relationship target by means of a predefined code)
	 *
     * <p>
     * <b>Definition:</b>
     * Code that identifies the target of this relationship. The code takes the place of a detailed instance target
     * </p> 
	 */
	public RelatedItem setCode(CodeableConceptDt theValue) {
		myCode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> (Relationship target resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Target of the relationship
     * </p> 
	 */
	public ResourceReferenceDt getTarget() {  
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (Relationship target resource)
	 *
     * <p>
     * <b>Definition:</b>
     * Target of the relationship
     * </p> 
	 */
	public RelatedItem setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}

  

	}




}
