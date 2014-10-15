















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


import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.AgeDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.RangeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;


/**
 * HAPI/FHIR <b>FamilyHistory</b> Resource
 * (Information about patient's relatives, relevant for patient)
 *
 * <p>
 * <b>Definition:</b>
 * Significant health events and conditions for people related to the subject relevant in the context of care for the subject
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/FamilyHistory">http://hl7.org/fhir/profiles/FamilyHistory</a> 
 * </p>
 *
 */
@ResourceDef(name="FamilyHistory", profile="http://hl7.org/fhir/profiles/FamilyHistory", id="familyhistory")
public class FamilyHistory extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of a subject to list family history items for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>FamilyHistory.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="FamilyHistory.subject", description="The identity of a subject to list family history items for", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of a subject to list family history items for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>FamilyHistory.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>FamilyHistory.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("FamilyHistory.subject");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Id(s) for this record",
		formalDefinition="This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Patient history is about",
		formalDefinition="The person who this history concerns"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="note", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Additional details not covered elsewhere",
		formalDefinition="Conveys information about family history not specific to individual relations."
	)
	private StringDt myNote;
	
	@Child(name="relation", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Relative described by history",
		formalDefinition="The related person. Each FamilyHistory resource contains the entire family history for a single person."
	)
	private java.util.List<Relation> myRelation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  mySubject,  myNote,  myRelation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, mySubject, myNote, myRelation);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Id(s) for this record).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Id(s) for this record)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public FamilyHistory setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Id(s) for this record)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Id(s) for this record),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Id(s) for this record)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public FamilyHistory addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Id(s) for this record)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this family history record that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public FamilyHistory addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Patient history is about).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person who this history concerns
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Patient history is about)
	 *
     * <p>
     * <b>Definition:</b>
     * The person who this history concerns
     * </p> 
	 */
	public FamilyHistory setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>note</b> (Additional details not covered elsewhere).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public StringDt getNote() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}

	/**
	 * Sets the value(s) for <b>note</b> (Additional details not covered elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public FamilyHistory setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>note</b> (Additional details not covered elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * Conveys information about family history not specific to individual relations.
     * </p> 
	 */
	public FamilyHistory setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>relation</b> (Relative described by history).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public java.util.List<Relation> getRelation() {  
		if (myRelation == null) {
			myRelation = new java.util.ArrayList<Relation>();
		}
		return myRelation;
	}

	/**
	 * Sets the value(s) for <b>relation</b> (Relative described by history)
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public FamilyHistory setRelation(java.util.List<Relation> theValue) {
		myRelation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>relation</b> (Relative described by history)
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public Relation addRelation() {
		Relation newType = new Relation();
		getRelation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relation</b> (Relative described by history),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	public Relation getRelationFirstRep() {
		if (getRelation().isEmpty()) {
			return addRelation();
		}
		return getRelation().get(0); 
	}
  
	/**
	 * Block class for child element: <b>FamilyHistory.relation</b> (Relative described by history)
	 *
     * <p>
     * <b>Definition:</b>
     * The related person. Each FamilyHistory resource contains the entire family history for a single person.
     * </p> 
	 */
	@Block()	
	public static class Relation extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="The family member described",
		formalDefinition="This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\""
	)
	private StringDt myName;
	
	@Child(name="relationship", type=CodeableConceptDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Relationship to the subject",
		formalDefinition="The type of relationship this person has to the patient (father, mother, brother etc.)"
	)
	private CodeableConceptDt myRelationship;
	
	@Child(name="born", order=2, min=0, max=1, type={
		PeriodDt.class, 		DateDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="(approximate) date of birth",
		formalDefinition="The actual or approximate date of birth of the relative"
	)
	private IDatatype myBorn;
	
	@Child(name="deceased", order=3, min=0, max=1, type={
		BooleanDt.class, 		AgeDt.class, 		RangeDt.class, 		DateDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="Dead? How old/when?",
		formalDefinition="If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set."
	)
	private IDatatype myDeceased;
	
	@Child(name="note", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="General note about related person",
		formalDefinition="This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible."
	)
	private StringDt myNote;
	
	@Child(name="condition", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Condition that the related person had",
		formalDefinition="The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition."
	)
	private java.util.List<RelationCondition> myCondition;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myRelationship,  myBorn,  myDeceased,  myNote,  myCondition);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myRelationship, myBorn, myDeceased, myNote, myCondition);
	}

	/**
	 * Gets the value(s) for <b>name</b> (The family member described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\"
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (The family member described)
	 *
     * <p>
     * <b>Definition:</b>
     * This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\"
     * </p> 
	 */
	public Relation setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (The family member described)
	 *
     * <p>
     * <b>Definition:</b>
     * This will either be a name or a description.  E.g. \"Aunt Susan\", \"my cousin with the red hair\"
     * </p> 
	 */
	public Relation setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>relationship</b> (Relationship to the subject).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship this person has to the patient (father, mother, brother etc.)
     * </p> 
	 */
	public CodeableConceptDt getRelationship() {  
		if (myRelationship == null) {
			myRelationship = new CodeableConceptDt();
		}
		return myRelationship;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (Relationship to the subject)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship this person has to the patient (father, mother, brother etc.)
     * </p> 
	 */
	public Relation setRelationship(CodeableConceptDt theValue) {
		myRelationship = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>born[x]</b> ((approximate) date of birth).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual or approximate date of birth of the relative
     * </p> 
	 */
	public IDatatype getBorn() {  
		return myBorn;
	}

	/**
	 * Sets the value(s) for <b>born[x]</b> ((approximate) date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual or approximate date of birth of the relative
     * </p> 
	 */
	public Relation setBorn(IDatatype theValue) {
		myBorn = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>deceased[x]</b> (Dead? How old/when?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set.
     * </p> 
	 */
	public IDatatype getDeceased() {  
		return myDeceased;
	}

	/**
	 * Sets the value(s) for <b>deceased[x]</b> (Dead? How old/when?)
	 *
     * <p>
     * <b>Definition:</b>
     * If this resource is indicating that the related person is deceased, then an indicator of whether the person is deceased (yes) or not (no) or the age or age range or description of age at death - can be indicated here. If the reason for death is known, then it can be indicated in the outcome code of the condition - in this case the deceased property should still be set.
     * </p> 
	 */
	public Relation setDeceased(IDatatype theValue) {
		myDeceased = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>note</b> (General note about related person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public StringDt getNote() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}

	/**
	 * Sets the value(s) for <b>note</b> (General note about related person)
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public Relation setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>note</b> (General note about related person)
	 *
     * <p>
     * <b>Definition:</b>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in the condition property, but this is not always possible.
     * </p> 
	 */
	public Relation setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>condition</b> (Condition that the related person had).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public java.util.List<RelationCondition> getCondition() {  
		if (myCondition == null) {
			myCondition = new java.util.ArrayList<RelationCondition>();
		}
		return myCondition;
	}

	/**
	 * Sets the value(s) for <b>condition</b> (Condition that the related person had)
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public Relation setCondition(java.util.List<RelationCondition> theValue) {
		myCondition = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>condition</b> (Condition that the related person had)
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public RelationCondition addCondition() {
		RelationCondition newType = new RelationCondition();
		getCondition().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>condition</b> (Condition that the related person had),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	public RelationCondition getConditionFirstRep() {
		if (getCondition().isEmpty()) {
			return addCondition();
		}
		return getCondition().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>FamilyHistory.relation.condition</b> (Condition that the related person had)
	 *
     * <p>
     * <b>Definition:</b>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to represent more than one condition per resource, though there is nothing stopping multiple resources - one per condition.
     * </p> 
	 */
	@Block()	
	public static class RelationCondition extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Condition suffered by relation",
		formalDefinition="The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system"
	)
	private CodeableConceptDt myType;
	
	@Child(name="outcome", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="deceased | permanent disability | etc.",
		formalDefinition="Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation."
	)
	private CodeableConceptDt myOutcome;
	
	@Child(name="onset", order=2, min=0, max=1, type={
		AgeDt.class, 		RangeDt.class, 		StringDt.class	})
	@Description(
		shortDefinition="When condition first manifested",
		formalDefinition="Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence."
	)
	private IDatatype myOnset;
	
	@Child(name="note", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Extra information about condition",
		formalDefinition="An area where general notes can be placed about this specific condition."
	)
	private StringDt myNote;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myOutcome,  myOnset,  myNote);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myOutcome, myOnset, myNote);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Condition suffered by relation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Condition suffered by relation)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 'cancer' depending on how much is known about the condition and the capabilities of the creating system
     * </p> 
	 */
	public RelationCondition setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>outcome</b> (deceased | permanent disability | etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.
     * </p> 
	 */
	public CodeableConceptDt getOutcome() {  
		if (myOutcome == null) {
			myOutcome = new CodeableConceptDt();
		}
		return myOutcome;
	}

	/**
	 * Sets the value(s) for <b>outcome</b> (deceased | permanent disability | etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates what happened as a result of this condition.  If the condition resulted in death, deceased date is captured on the relation.
     * </p> 
	 */
	public RelationCondition setOutcome(CodeableConceptDt theValue) {
		myOutcome = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>onset[x]</b> (When condition first manifested).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.
     * </p> 
	 */
	public IDatatype getOnset() {  
		return myOnset;
	}

	/**
	 * Sets the value(s) for <b>onset[x]</b> (When condition first manifested)
	 *
     * <p>
     * <b>Definition:</b>
     * Either the age of onset, range of approximate age or descriptive string can be recorded.  For conditions with multiple occurrences, this describes the first known occurrence.
     * </p> 
	 */
	public RelationCondition setOnset(IDatatype theValue) {
		myOnset = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>note</b> (Extra information about condition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public StringDt getNote() {  
		if (myNote == null) {
			myNote = new StringDt();
		}
		return myNote;
	}

	/**
	 * Sets the value(s) for <b>note</b> (Extra information about condition)
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public RelationCondition setNote(StringDt theValue) {
		myNote = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>note</b> (Extra information about condition)
	 *
     * <p>
     * <b>Definition:</b>
     * An area where general notes can be placed about this specific condition.
     * </p> 
	 */
	public RelationCondition setNote( String theString) {
		myNote = new StringDt(theString); 
		return this; 
	}

 

	}

	@Override
	public ResourceTypeEnum getResourceType() {
		return ResourceTypeEnum.FAMILYHISTORY;
	}





}
