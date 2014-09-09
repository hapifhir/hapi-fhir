















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
import ca.uhn.fhir.model.api.IDatatype;
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
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireGroupNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireNameEnum;
import ca.uhn.fhir.model.dstu.valueset.QuestionnaireStatusEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>Questionnaire</b> Resource
 * (A structured set of questions and their answers)
 *
 * <p>
 * <b>Definition:</b>
 * A structured set of questions and their answers. The Questionnaire may contain questions, answers or both. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * To support structured, hierarchical registration of data gathered using digital forms and other questionnaires.
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Questionnaire">http://hl7.org/fhir/profiles/Questionnaire</a> 
 * </p>
 *
 */
@ResourceDef(name="Questionnaire", profile="http://hl7.org/fhir/profiles/Questionnaire", id="questionnaire")
public class Questionnaire extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Questionnaire.status", description="The status of the questionnaire", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>authored</b>
	 * <p>
	 * Description: <b>When the questionnaire was authored</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Questionnaire.authored</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="authored", path="Questionnaire.authored", description="When the questionnaire was authored", type="date"  )
	public static final String SP_AUTHORED = "authored";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>authored</b>
	 * <p>
	 * Description: <b>When the questionnaire was authored</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Questionnaire.authored</b><br/>
	 * </p>
	 */
	public static final DateClientParam AUTHORED = new DateClientParam(SP_AUTHORED);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Questionnaire.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="Questionnaire.subject", description="The subject of the questionnaire", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Questionnaire.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Questionnaire.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("Questionnaire.subject");

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>The author of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Questionnaire.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="Questionnaire.author", description="The author of the questionnaire", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b>The author of the questionnaire</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Questionnaire.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Questionnaire.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("Questionnaire.author");

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>An identifier for the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Questionnaire.identifier", description="An identifier for the questionnaire", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>An identifier for the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Questionnaire.name", description="Name of the questionnaire", type="token"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the questionnaire</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Questionnaire.name</b><br/>
	 * </p>
	 */
	public static final TokenClientParam NAME = new TokenClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Encounter during which questionnaire was authored</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Questionnaire.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="Questionnaire.encounter", description="Encounter during which questionnaire was authored", type="reference"  )
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Encounter during which questionnaire was authored</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Questionnaire.encounter</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam ENCOUNTER = new ReferenceClientParam(SP_ENCOUNTER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Questionnaire.encounter</b>".
	 */
	public static final Include INCLUDE_ENCOUNTER = new Include("Questionnaire.encounter");


	@Child(name="status", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="draft | published | retired | in progress | completed | amended",
		formalDefinition="The lifecycle status of the questionnaire as a whole."
	)
	private BoundCodeDt<QuestionnaireStatusEnum> myStatus;
	
	@Child(name="authored", type=DateTimeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Date this version was authored",
		formalDefinition="The date and/or time that this version of the questionnaire was authored"
	)
	private DateTimeDt myAuthored;
	
	@Child(name="subject", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="The subject of the questions",
		formalDefinition="The subject of the questionnaires: this is the patient that the answers apply to, but this person is not necessarily the source of information"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="author", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Person who received and recorded the answers",
		formalDefinition="Person who received the answers to the questions in the Questionnaire and recorded them in the system"
	)
	private ResourceReferenceDt myAuthor;
	
	@Child(name="source", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="The person who answered the questions",
		formalDefinition="The person who answered the questions about the subject. Only used when this is not the subject him/herself"
	)
	private ResourceReferenceDt mySource;
	
	@Child(name="name", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Name/code for a predefined list of questions",
		formalDefinition="Structured name for a predefined list of questions this questionnaire is responding to"
	)
	private BoundCodeableConceptDt<QuestionnaireNameEnum> myName;
	
	@Child(name="identifier", type=IdentifierDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this questionnaire",
		formalDefinition="This records identifiers associated with this question/answer set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="encounter", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Encounter.class	})
	@Description(
		shortDefinition="Primary encounter during which the answers were collected",
		formalDefinition="Encounter during which this questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers."
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="group", order=8, min=0, max=1)	
	@Description(
		shortDefinition="Grouped questions",
		formalDefinition="A group of questions to a possibly similarly grouped set of questions in the questionnaire"
	)
	private Group myGroup;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStatus,  myAuthored,  mySubject,  myAuthor,  mySource,  myName,  myIdentifier,  myEncounter,  myGroup);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStatus, myAuthored, mySubject, myAuthor, mySource, myName, myIdentifier, myEncounter, myGroup);
	}

	/**
	 * Gets the value(s) for <b>status</b> (draft | published | retired | in progress | completed | amended).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire as a whole.
     * </p> 
	 */
	public BoundCodeDt<QuestionnaireStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<QuestionnaireStatusEnum>(QuestionnaireStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (draft | published | retired | in progress | completed | amended)
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire as a whole.
     * </p> 
	 */
	public Questionnaire setStatus(BoundCodeDt<QuestionnaireStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (draft | published | retired | in progress | completed | amended)
	 *
     * <p>
     * <b>Definition:</b>
     * The lifecycle status of the questionnaire as a whole.
     * </p> 
	 */
	public Questionnaire setStatus(QuestionnaireStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>authored</b> (Date this version was authored).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the questionnaire was authored
     * </p> 
	 */
	public DateTimeDt getAuthored() {  
		if (myAuthored == null) {
			myAuthored = new DateTimeDt();
		}
		return myAuthored;
	}

	/**
	 * Sets the value(s) for <b>authored</b> (Date this version was authored)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the questionnaire was authored
     * </p> 
	 */
	public Questionnaire setAuthored(DateTimeDt theValue) {
		myAuthored = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>authored</b> (Date this version was authored)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the questionnaire was authored
     * </p> 
	 */
	public Questionnaire setAuthoredWithSecondsPrecision( Date theDate) {
		myAuthored = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>authored</b> (Date this version was authored)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and/or time that this version of the questionnaire was authored
     * </p> 
	 */
	public Questionnaire setAuthored( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myAuthored = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (The subject of the questions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the questionnaires: this is the patient that the answers apply to, but this person is not necessarily the source of information
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (The subject of the questions)
	 *
     * <p>
     * <b>Definition:</b>
     * The subject of the questionnaires: this is the patient that the answers apply to, but this person is not necessarily the source of information
     * </p> 
	 */
	public Questionnaire setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>author</b> (Person who received and recorded the answers).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Person who received the answers to the questions in the Questionnaire and recorded them in the system
     * </p> 
	 */
	public ResourceReferenceDt getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new ResourceReferenceDt();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (Person who received and recorded the answers)
	 *
     * <p>
     * <b>Definition:</b>
     * Person who received the answers to the questions in the Questionnaire and recorded them in the system
     * </p> 
	 */
	public Questionnaire setAuthor(ResourceReferenceDt theValue) {
		myAuthor = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>source</b> (The person who answered the questions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who answered the questions about the subject. Only used when this is not the subject him/herself
     * </p> 
	 */
	public ResourceReferenceDt getSource() {  
		if (mySource == null) {
			mySource = new ResourceReferenceDt();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (The person who answered the questions)
	 *
     * <p>
     * <b>Definition:</b>
     * The person who answered the questions about the subject. Only used when this is not the subject him/herself
     * </p> 
	 */
	public Questionnaire setSource(ResourceReferenceDt theValue) {
		mySource = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>name</b> (Name/code for a predefined list of questions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Structured name for a predefined list of questions this questionnaire is responding to
     * </p> 
	 */
	public BoundCodeableConceptDt<QuestionnaireNameEnum> getName() {  
		if (myName == null) {
			myName = new BoundCodeableConceptDt<QuestionnaireNameEnum>(QuestionnaireNameEnum.VALUESET_BINDER);
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name/code for a predefined list of questions)
	 *
     * <p>
     * <b>Definition:</b>
     * Structured name for a predefined list of questions this questionnaire is responding to
     * </p> 
	 */
	public Questionnaire setName(BoundCodeableConceptDt<QuestionnaireNameEnum> theValue) {
		myName = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name/code for a predefined list of questions)
	 *
     * <p>
     * <b>Definition:</b>
     * Structured name for a predefined list of questions this questionnaire is responding to
     * </p> 
	 */
	public Questionnaire setName(QuestionnaireNameEnum theValue) {
		getName().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this questionnaire).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this question/answer set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Ids for this questionnaire)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this question/answer set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public Questionnaire setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this questionnaire)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this question/answer set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Ids for this questionnaire),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this question/answer set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this questionnaire)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this question/answer set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Questionnaire addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this questionnaire)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this question/answer set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Questionnaire addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>encounter</b> (Primary encounter during which the answers were collected).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Encounter during which this questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.
     * </p> 
	 */
	public ResourceReferenceDt getEncounter() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}

	/**
	 * Sets the value(s) for <b>encounter</b> (Primary encounter during which the answers were collected)
	 *
     * <p>
     * <b>Definition:</b>
     * Encounter during which this questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.
     * </p> 
	 */
	public Questionnaire setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>group</b> (Grouped questions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A group of questions to a possibly similarly grouped set of questions in the questionnaire
     * </p> 
	 */
	public Group getGroup() {  
		if (myGroup == null) {
			myGroup = new Group();
		}
		return myGroup;
	}

	/**
	 * Sets the value(s) for <b>group</b> (Grouped questions)
	 *
     * <p>
     * <b>Definition:</b>
     * A group of questions to a possibly similarly grouped set of questions in the questionnaire
     * </p> 
	 */
	public Questionnaire setGroup(Group theValue) {
		myGroup = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>Questionnaire.group</b> (Grouped questions)
	 *
     * <p>
     * <b>Definition:</b>
     * A group of questions to a possibly similarly grouped set of questions in the questionnaire
     * </p> 
	 */
	@Block()	
	public static class Group extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Code or name of the section on a questionnaire",
		formalDefinition="Structured name for a section of a predefined list of questions this questionnaire is responding to."
	)
	private BoundCodeableConceptDt<QuestionnaireGroupNameEnum> myName;
	
	@Child(name="header", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Text that is displayed above the contents of the group",
		formalDefinition=""
	)
	private StringDt myHeader;
	
	@Child(name="text", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Additional text for the group",
		formalDefinition="Additional text for the group, used for display purposes"
	)
	private StringDt myText;
	
	@Child(name="subject", order=3, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="The subject this group's answers are about",
		formalDefinition="More specific subject this section's answers are about, details the subject given in Questionnaire"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="group", type=Group.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Nested questionnaire group",
		formalDefinition="A sub-group within a group. The ordering of groups within this group is relevant"
	)
	private java.util.List<Group> myGroup;
	
	@Child(name="question", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Questions in this group",
		formalDefinition="Set of questions within this group. The order of questions within the group is relevant"
	)
	private java.util.List<GroupQuestion> myQuestion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myHeader,  myText,  mySubject,  myGroup,  myQuestion);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myHeader, myText, mySubject, myGroup, myQuestion);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Code or name of the section on a questionnaire).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Structured name for a section of a predefined list of questions this questionnaire is responding to.
     * </p> 
	 */
	public BoundCodeableConceptDt<QuestionnaireGroupNameEnum> getName() {  
		if (myName == null) {
			myName = new BoundCodeableConceptDt<QuestionnaireGroupNameEnum>(QuestionnaireGroupNameEnum.VALUESET_BINDER);
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Code or name of the section on a questionnaire)
	 *
     * <p>
     * <b>Definition:</b>
     * Structured name for a section of a predefined list of questions this questionnaire is responding to.
     * </p> 
	 */
	public Group setName(BoundCodeableConceptDt<QuestionnaireGroupNameEnum> theValue) {
		myName = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Code or name of the section on a questionnaire)
	 *
     * <p>
     * <b>Definition:</b>
     * Structured name for a section of a predefined list of questions this questionnaire is responding to.
     * </p> 
	 */
	public Group setName(QuestionnaireGroupNameEnum theValue) {
		getName().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>header</b> (Text that is displayed above the contents of the group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getHeader() {  
		if (myHeader == null) {
			myHeader = new StringDt();
		}
		return myHeader;
	}

	/**
	 * Sets the value(s) for <b>header</b> (Text that is displayed above the contents of the group)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Group setHeader(StringDt theValue) {
		myHeader = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>header</b> (Text that is displayed above the contents of the group)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Group setHeader( String theString) {
		myHeader = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>text</b> (Additional text for the group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text for the group, used for display purposes
     * </p> 
	 */
	public StringDt getText() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	/**
	 * Sets the value(s) for <b>text</b> (Additional text for the group)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text for the group, used for display purposes
     * </p> 
	 */
	public Group setText(StringDt theValue) {
		myText = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>text</b> (Additional text for the group)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional text for the group, used for display purposes
     * </p> 
	 */
	public Group setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (The subject this group's answers are about).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * More specific subject this section's answers are about, details the subject given in Questionnaire
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (The subject this group's answers are about)
	 *
     * <p>
     * <b>Definition:</b>
     * More specific subject this section's answers are about, details the subject given in Questionnaire
     * </p> 
	 */
	public Group setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>group</b> (Nested questionnaire group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A sub-group within a group. The ordering of groups within this group is relevant
     * </p> 
	 */
	public java.util.List<Group> getGroup() {  
		if (myGroup == null) {
			myGroup = new java.util.ArrayList<Group>();
		}
		return myGroup;
	}

	/**
	 * Sets the value(s) for <b>group</b> (Nested questionnaire group)
	 *
     * <p>
     * <b>Definition:</b>
     * A sub-group within a group. The ordering of groups within this group is relevant
     * </p> 
	 */
	public Group setGroup(java.util.List<Group> theValue) {
		myGroup = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>group</b> (Nested questionnaire group)
	 *
     * <p>
     * <b>Definition:</b>
     * A sub-group within a group. The ordering of groups within this group is relevant
     * </p> 
	 */
	public Group addGroup() {
		Group newType = new Group();
		getGroup().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>group</b> (Nested questionnaire group),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A sub-group within a group. The ordering of groups within this group is relevant
     * </p> 
	 */
	public Group getGroupFirstRep() {
		if (getGroup().isEmpty()) {
			return addGroup();
		}
		return getGroup().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>question</b> (Questions in this group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	public java.util.List<GroupQuestion> getQuestion() {  
		if (myQuestion == null) {
			myQuestion = new java.util.ArrayList<GroupQuestion>();
		}
		return myQuestion;
	}

	/**
	 * Sets the value(s) for <b>question</b> (Questions in this group)
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	public Group setQuestion(java.util.List<GroupQuestion> theValue) {
		myQuestion = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>question</b> (Questions in this group)
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	public GroupQuestion addQuestion() {
		GroupQuestion newType = new GroupQuestion();
		getQuestion().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>question</b> (Questions in this group),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	public GroupQuestion getQuestionFirstRep() {
		if (getQuestion().isEmpty()) {
			return addQuestion();
		}
		return getQuestion().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>Questionnaire.group.question</b> (Questions in this group)
	 *
     * <p>
     * <b>Definition:</b>
     * Set of questions within this group. The order of questions within the group is relevant
     * </p> 
	 */
	@Block()	
	public static class GroupQuestion extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Code or name of the question",
		formalDefinition="Structured name for the question that identifies this question within the Questionnaire or Group"
	)
	private CodeableConceptDt myName;
	
	@Child(name="text", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Text of the question as it is shown to the user",
		formalDefinition=""
	)
	private StringDt myText;
	
	@Child(name="answer", order=2, min=0, max=1, type={
		DecimalDt.class, 		IntegerDt.class, 		BooleanDt.class, 		DateDt.class, 		StringDt.class, 		DateTimeDt.class, 		InstantDt.class	})
	@Description(
		shortDefinition="Single-valued answer to the question",
		formalDefinition="Single-valued answer to the question"
	)
	private IDatatype myAnswer;
	
	@Child(name="choice", type=CodingDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Selected options",
		formalDefinition="Selections made by the user from the list of options"
	)
	private java.util.List<CodingDt> myChoice;
	
	@Child(name="options", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.ValueSet.class	})
	@Description(
		shortDefinition="Valueset containing the possible options",
		formalDefinition="Reference to a valueset containing the possible options"
	)
	private ResourceReferenceDt myOptions;
	
	@Child(name="data", type=IDatatype.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Structured answer",
		formalDefinition="Structured answer in the form of a FHIR Resource or datatype"
	)
	private IDatatype myData;
	
	@Child(name="remarks", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Remarks about the answer given",
		formalDefinition="The remark contains information about the answer given. This is additional information about the answer the author wishes to convey, but should not be used to contain information that is part of the answer itself."
	)
	private StringDt myRemarks;
	
	@Child(name="group", type=Group.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Nested questionnaire group",
		formalDefinition="Nested group, containing nested question for this question. The order of groups within the question is relevant"
	)
	private java.util.List<Group> myGroup;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myText,  myAnswer,  myChoice,  myOptions,  myData,  myRemarks,  myGroup);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myText, myAnswer, myChoice, myOptions, myData, myRemarks, myGroup);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Code or name of the question).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Structured name for the question that identifies this question within the Questionnaire or Group
     * </p> 
	 */
	public CodeableConceptDt getName() {  
		if (myName == null) {
			myName = new CodeableConceptDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Code or name of the question)
	 *
     * <p>
     * <b>Definition:</b>
     * Structured name for the question that identifies this question within the Questionnaire or Group
     * </p> 
	 */
	public GroupQuestion setName(CodeableConceptDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>text</b> (Text of the question as it is shown to the user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getText() {  
		if (myText == null) {
			myText = new StringDt();
		}
		return myText;
	}

	/**
	 * Sets the value(s) for <b>text</b> (Text of the question as it is shown to the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public GroupQuestion setText(StringDt theValue) {
		myText = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>text</b> (Text of the question as it is shown to the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public GroupQuestion setText( String theString) {
		myText = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>answer[x]</b> (Single-valued answer to the question).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Single-valued answer to the question
     * </p> 
	 */
	public IDatatype getAnswer() {  
		return myAnswer;
	}

	/**
	 * Sets the value(s) for <b>answer[x]</b> (Single-valued answer to the question)
	 *
     * <p>
     * <b>Definition:</b>
     * Single-valued answer to the question
     * </p> 
	 */
	public GroupQuestion setAnswer(IDatatype theValue) {
		myAnswer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>choice</b> (Selected options).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Selections made by the user from the list of options
     * </p> 
	 */
	public java.util.List<CodingDt> getChoice() {  
		if (myChoice == null) {
			myChoice = new java.util.ArrayList<CodingDt>();
		}
		return myChoice;
	}

	/**
	 * Sets the value(s) for <b>choice</b> (Selected options)
	 *
     * <p>
     * <b>Definition:</b>
     * Selections made by the user from the list of options
     * </p> 
	 */
	public GroupQuestion setChoice(java.util.List<CodingDt> theValue) {
		myChoice = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>choice</b> (Selected options)
	 *
     * <p>
     * <b>Definition:</b>
     * Selections made by the user from the list of options
     * </p> 
	 */
	public CodingDt addChoice() {
		CodingDt newType = new CodingDt();
		getChoice().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>choice</b> (Selected options),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Selections made by the user from the list of options
     * </p> 
	 */
	public CodingDt getChoiceFirstRep() {
		if (getChoice().isEmpty()) {
			return addChoice();
		}
		return getChoice().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>options</b> (Valueset containing the possible options).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a valueset containing the possible options
     * </p> 
	 */
	public ResourceReferenceDt getOptions() {  
		if (myOptions == null) {
			myOptions = new ResourceReferenceDt();
		}
		return myOptions;
	}

	/**
	 * Sets the value(s) for <b>options</b> (Valueset containing the possible options)
	 *
     * <p>
     * <b>Definition:</b>
     * Reference to a valueset containing the possible options
     * </p> 
	 */
	public GroupQuestion setOptions(ResourceReferenceDt theValue) {
		myOptions = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>data[x]</b> (Structured answer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Structured answer in the form of a FHIR Resource or datatype
     * </p> 
	 */
	public IDatatype getData() {  
		return myData;
	}

	/**
	 * Sets the value(s) for <b>data[x]</b> (Structured answer)
	 *
     * <p>
     * <b>Definition:</b>
     * Structured answer in the form of a FHIR Resource or datatype
     * </p> 
	 */
	public GroupQuestion setData(IDatatype theValue) {
		myData = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>remarks</b> (Remarks about the answer given).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The remark contains information about the answer given. This is additional information about the answer the author wishes to convey, but should not be used to contain information that is part of the answer itself.
     * </p> 
	 */
	public StringDt getRemarks() {  
		if (myRemarks == null) {
			myRemarks = new StringDt();
		}
		return myRemarks;
	}

	/**
	 * Sets the value(s) for <b>remarks</b> (Remarks about the answer given)
	 *
     * <p>
     * <b>Definition:</b>
     * The remark contains information about the answer given. This is additional information about the answer the author wishes to convey, but should not be used to contain information that is part of the answer itself.
     * </p> 
	 */
	public GroupQuestion setRemarks(StringDt theValue) {
		myRemarks = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>remarks</b> (Remarks about the answer given)
	 *
     * <p>
     * <b>Definition:</b>
     * The remark contains information about the answer given. This is additional information about the answer the author wishes to convey, but should not be used to contain information that is part of the answer itself.
     * </p> 
	 */
	public GroupQuestion setRemarks( String theString) {
		myRemarks = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>group</b> (Nested questionnaire group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Nested group, containing nested question for this question. The order of groups within the question is relevant
     * </p> 
	 */
	public java.util.List<Group> getGroup() {  
		if (myGroup == null) {
			myGroup = new java.util.ArrayList<Group>();
		}
		return myGroup;
	}

	/**
	 * Sets the value(s) for <b>group</b> (Nested questionnaire group)
	 *
     * <p>
     * <b>Definition:</b>
     * Nested group, containing nested question for this question. The order of groups within the question is relevant
     * </p> 
	 */
	public GroupQuestion setGroup(java.util.List<Group> theValue) {
		myGroup = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>group</b> (Nested questionnaire group)
	 *
     * <p>
     * <b>Definition:</b>
     * Nested group, containing nested question for this question. The order of groups within the question is relevant
     * </p> 
	 */
	public Group addGroup() {
		Group newType = new Group();
		getGroup().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>group</b> (Nested questionnaire group),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Nested group, containing nested question for this question. The order of groups within the question is relevant
     * </p> 
	 */
	public Group getGroupFirstRep() {
		if (getGroup().isEmpty()) {
			return addGroup();
		}
		return getGroup().get(0); 
	}
  

	}





}
