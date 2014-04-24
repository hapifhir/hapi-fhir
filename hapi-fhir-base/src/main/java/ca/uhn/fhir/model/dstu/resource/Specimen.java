















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
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.HierarchicalRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu.valueset.SpecimenCollectionMethodEnum;
import ca.uhn.fhir.model.dstu.valueset.SpecimenTreatmentProcedureEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;


/**
 * HAPI/FHIR <b>Specimen</b> Resource
 * (Sample for analysis)
 *
 * <p>
 * <b>Definition:</b>
 * Sample for analysis
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Specimen">http://hl7.org/fhir/profiles/Specimen</a> 
 * </p>
 *
 */
@ResourceDef(name="Specimen", profile="http://hl7.org/fhir/profiles/Specimen", id="specimen")
public class Specimen extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The subject of the specimen</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Specimen.subject</b><br/>
	 * </p>
	 */
	public static final String SP_SUBJECT = "subject";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Identifier",
		formalDefinition="Id for specimen"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Kind of material that forms the specimen",
		formalDefinition="Kind of material that forms the specimen"
	)
	private CodeableConceptDt myType;
	
	@Child(name="source", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Parent of specimen",
		formalDefinition="Parent specimen from which the focal specimen was a component"
	)
	private java.util.List<Source> mySource;
	
	@Child(name="subject", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Substance.class	})
	@Description(
		shortDefinition="Where the specimen came from. This may be the patient(s) or from the environment or  a device",
		formalDefinition=""
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Identifier assigned by the lab",
		formalDefinition="The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures."
	)
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="The time when specimen was received for processing",
		formalDefinition="Time when specimen was received for processing or testing"
	)
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	@Description(
		shortDefinition="Collection details",
		formalDefinition="Details concerning the specimen collection"
	)
	private Collection myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Treatment and processing step details",
		formalDefinition="Details concerning treatment and processing steps for the specimen"
	)
	private java.util.List<Treatment> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Direct container of specimen (tube/slide, etc)",
		formalDefinition="The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here."
	)
	private java.util.List<Container> myContainer;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType,  mySource,  mySubject,  myAccessionIdentifier,  myReceivedTime,  myCollection,  myTreatment,  myContainer);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType, mySource, mySubject, myAccessionIdentifier, myReceivedTime, myCollection, myTreatment, myContainer);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public Specimen setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Identifier),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Specimen addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Specimen addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (Kind of material that forms the specimen).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public Specimen setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>source</b> (Parent of specimen).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public java.util.List<Source> getSource() {  
		if (mySource == null) {
			mySource = new java.util.ArrayList<Source>();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public Specimen setSource(java.util.List<Source> theValue) {
		mySource = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>source</b> (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public Source addSource() {
		Source newType = new Source();
		getSource().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>source</b> (Parent of specimen),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public Source getSourceFirstRep() {
		if (getSource().isEmpty()) {
			return addSource();
		}
		return getSource().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>subject</b> (Where the specimen came from. This may be the patient(s) or from the environment or  a device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Specimen setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>accessionIdentifier</b> (Identifier assigned by the lab).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.
     * </p> 
	 */
	public IdentifierDt getAccessionIdentifier() {  
		if (myAccessionIdentifier == null) {
			myAccessionIdentifier = new IdentifierDt();
		}
		return myAccessionIdentifier;
	}

	/**
	 * Sets the value(s) for <b>accessionIdentifier</b> (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.
     * </p> 
	 */
	public Specimen setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>accessionIdentifier</b> (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.
     * </p> 
	 */
	public Specimen setAccessionIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myAccessionIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>accessionIdentifier</b> (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.
     * </p> 
	 */
	public Specimen setAccessionIdentifier( String theSystem,  String theValue) {
		myAccessionIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>receivedTime</b> (The time when specimen was received for processing).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public DateTimeDt getReceivedTime() {  
		if (myReceivedTime == null) {
			myReceivedTime = new DateTimeDt();
		}
		return myReceivedTime;
	}

	/**
	 * Sets the value(s) for <b>receivedTime</b> (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public Specimen setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>receivedTime</b> (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public Specimen setReceivedTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myReceivedTime = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>receivedTime</b> (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public Specimen setReceivedTimeWithSecondsPrecision( Date theDate) {
		myReceivedTime = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>collection</b> (Collection details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public Collection getCollection() {  
		if (myCollection == null) {
			myCollection = new Collection();
		}
		return myCollection;
	}

	/**
	 * Sets the value(s) for <b>collection</b> (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public Specimen setCollection(Collection theValue) {
		myCollection = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>treatment</b> (Treatment and processing step details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public java.util.List<Treatment> getTreatment() {  
		if (myTreatment == null) {
			myTreatment = new java.util.ArrayList<Treatment>();
		}
		return myTreatment;
	}

	/**
	 * Sets the value(s) for <b>treatment</b> (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public Specimen setTreatment(java.util.List<Treatment> theValue) {
		myTreatment = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>treatment</b> (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public Treatment addTreatment() {
		Treatment newType = new Treatment();
		getTreatment().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>treatment</b> (Treatment and processing step details),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public Treatment getTreatmentFirstRep() {
		if (getTreatment().isEmpty()) {
			return addTreatment();
		}
		return getTreatment().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>container</b> (Direct container of specimen (tube/slide, etc)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	public java.util.List<Container> getContainer() {  
		if (myContainer == null) {
			myContainer = new java.util.ArrayList<Container>();
		}
		return myContainer;
	}

	/**
	 * Sets the value(s) for <b>container</b> (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	public Specimen setContainer(java.util.List<Container> theValue) {
		myContainer = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>container</b> (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	public Container addContainer() {
		Container newType = new Container();
		getContainer().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>container</b> (Direct container of specimen (tube/slide, etc)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	public Container getContainerFirstRep() {
		if (getContainer().isEmpty()) {
			return addContainer();
		}
		return getContainer().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Specimen.source</b> (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	@Block(name="Specimen.source")	
	public static class Source extends BaseElement implements IResourceBlock {
	
	@Child(name="relationship", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="parent | child",
		formalDefinition="Whether this relationship is to a parent or to a child"
	)
	private BoundCodeDt<HierarchicalRelationshipTypeEnum> myRelationship;
	
	@Child(name="target", order=1, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Specimen.class	})
	@Description(
		shortDefinition="The subject of the relationship",
		formalDefinition="The specimen resource that is the target of this relationship"
	)
	private java.util.List<ResourceReferenceDt> myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRelationship,  myTarget);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRelationship, myTarget);
	}

	/**
	 * Gets the value(s) for <b>relationship</b> (parent | child).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this relationship is to a parent or to a child
     * </p> 
	 */
	public BoundCodeDt<HierarchicalRelationshipTypeEnum> getRelationship() {  
		if (myRelationship == null) {
			myRelationship = new BoundCodeDt<HierarchicalRelationshipTypeEnum>(HierarchicalRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myRelationship;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (parent | child)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this relationship is to a parent or to a child
     * </p> 
	 */
	public Source setRelationship(BoundCodeDt<HierarchicalRelationshipTypeEnum> theValue) {
		myRelationship = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (parent | child)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this relationship is to a parent or to a child
     * </p> 
	 */
	public Source setRelationship(HierarchicalRelationshipTypeEnum theValue) {
		getRelationship().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> (The subject of the relationship).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen resource that is the target of this relationship
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getTarget() {  
		if (myTarget == null) {
			myTarget = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (The subject of the relationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen resource that is the target of this relationship
     * </p> 
	 */
	public Source setTarget(java.util.List<ResourceReferenceDt> theValue) {
		myTarget = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>target</b> (The subject of the relationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The specimen resource that is the target of this relationship
     * </p> 
	 */
	public ResourceReferenceDt addTarget() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getTarget().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Specimen.collection</b> (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	@Block(name="Specimen.collection")	
	public static class Collection extends BaseElement implements IResourceBlock {
	
	@Child(name="collector", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who collected the specimen",
		formalDefinition="Person who collected the specimen"
	)
	private ResourceReferenceDt myCollector;
	
	@Child(name="comment", type=StringDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Collector comments",
		formalDefinition="To communicate any details or issues encountered during the specimen collection procedure."
	)
	private java.util.List<StringDt> myComment;
	
	@Child(name="collected", order=2, min=0, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="Collection time",
		formalDefinition="Time when specimen was collected from subject - the physiologically relevant time"
	)
	private IDatatype myCollected;
	
	@Child(name="quantity", type=QuantityDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="The quantity of specimen collected",
		formalDefinition="The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample"
	)
	private QuantityDt myQuantity;
	
	@Child(name="method", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Technique used to perform collection",
		formalDefinition="A coded value specifying the technique that is used to perform the procedure"
	)
	private BoundCodeableConceptDt<SpecimenCollectionMethodEnum> myMethod;
	
	@Child(name="sourceSite", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Anatomical collection site",
		formalDefinition="Anatomical location from which the specimen should be collected (if subject is a patient). This element is not used for environmental specimens."
	)
	private CodeableConceptDt mySourceSite;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCollector,  myComment,  myCollected,  myQuantity,  myMethod,  mySourceSite);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCollector, myComment, myCollected, myQuantity, myMethod, mySourceSite);
	}

	/**
	 * Gets the value(s) for <b>collector</b> (Who collected the specimen).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Person who collected the specimen
     * </p> 
	 */
	public ResourceReferenceDt getCollector() {  
		if (myCollector == null) {
			myCollector = new ResourceReferenceDt();
		}
		return myCollector;
	}

	/**
	 * Sets the value(s) for <b>collector</b> (Who collected the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Person who collected the specimen
     * </p> 
	 */
	public Collection setCollector(ResourceReferenceDt theValue) {
		myCollector = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>comment</b> (Collector comments).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
	 */
	public java.util.List<StringDt> getComment() {  
		if (myComment == null) {
			myComment = new java.util.ArrayList<StringDt>();
		}
		return myComment;
	}

	/**
	 * Sets the value(s) for <b>comment</b> (Collector comments)
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
	 */
	public Collection setComment(java.util.List<StringDt> theValue) {
		myComment = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>comment</b> (Collector comments)
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
	 */
	public StringDt addComment() {
		StringDt newType = new StringDt();
		getComment().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>comment</b> (Collector comments),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
	 */
	public StringDt getCommentFirstRep() {
		if (getComment().isEmpty()) {
			return addComment();
		}
		return getComment().get(0); 
	}
 	/**
	 * Adds a new value for <b>comment</b> (Collector comments)
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Collection addComment( String theString) {
		if (myComment == null) {
			myComment = new java.util.ArrayList<StringDt>();
		}
		myComment.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>collected[x]</b> (Collection time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was collected from subject - the physiologically relevant time
     * </p> 
	 */
	public IDatatype getCollected() {  
		return myCollected;
	}

	/**
	 * Sets the value(s) for <b>collected[x]</b> (Collection time)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was collected from subject - the physiologically relevant time
     * </p> 
	 */
	public Collection setCollected(IDatatype theValue) {
		myCollected = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>quantity</b> (The quantity of specimen collected).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (The quantity of specimen collected)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample
     * </p> 
	 */
	public Collection setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (The quantity of specimen collected)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample
     * </p> 
	 */
	public Collection setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (The quantity of specimen collected)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample
     * </p> 
	 */
	public Collection setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (The quantity of specimen collected)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample
     * </p> 
	 */
	public Collection setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (The quantity of specimen collected)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample
     * </p> 
	 */
	public Collection setQuantity( double theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>method</b> (Technique used to perform collection).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the technique that is used to perform the procedure
     * </p> 
	 */
	public BoundCodeableConceptDt<SpecimenCollectionMethodEnum> getMethod() {  
		if (myMethod == null) {
			myMethod = new BoundCodeableConceptDt<SpecimenCollectionMethodEnum>(SpecimenCollectionMethodEnum.VALUESET_BINDER);
		}
		return myMethod;
	}

	/**
	 * Sets the value(s) for <b>method</b> (Technique used to perform collection)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the technique that is used to perform the procedure
     * </p> 
	 */
	public Collection setMethod(BoundCodeableConceptDt<SpecimenCollectionMethodEnum> theValue) {
		myMethod = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>method</b> (Technique used to perform collection)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the technique that is used to perform the procedure
     * </p> 
	 */
	public Collection setMethod(SpecimenCollectionMethodEnum theValue) {
		getMethod().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sourceSite</b> (Anatomical collection site).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Anatomical location from which the specimen should be collected (if subject is a patient). This element is not used for environmental specimens.
     * </p> 
	 */
	public CodeableConceptDt getSourceSite() {  
		if (mySourceSite == null) {
			mySourceSite = new CodeableConceptDt();
		}
		return mySourceSite;
	}

	/**
	 * Sets the value(s) for <b>sourceSite</b> (Anatomical collection site)
	 *
     * <p>
     * <b>Definition:</b>
     * Anatomical location from which the specimen should be collected (if subject is a patient). This element is not used for environmental specimens.
     * </p> 
	 */
	public Collection setSourceSite(CodeableConceptDt theValue) {
		mySourceSite = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>Specimen.treatment</b> (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	@Block(name="Specimen.treatment")	
	public static class Treatment extends BaseElement implements IResourceBlock {
	
	@Child(name="description", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Textual description of procedure",
		formalDefinition=""
	)
	private StringDt myDescription;
	
	@Child(name="procedure", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Indicates the treatment or processing step  applied to the specimen",
		formalDefinition="A coded value specifying the procedure used to process the specimen"
	)
	private BoundCodeableConceptDt<SpecimenTreatmentProcedureEnum> myProcedure;
	
	@Child(name="additive", order=2, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Substance.class	})
	@Description(
		shortDefinition="Material used in the processing step",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myAdditive;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDescription,  myProcedure,  myAdditive);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDescription, myProcedure, myAdditive);
	}

	/**
	 * Gets the value(s) for <b>description</b> (Textual description of procedure).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Textual description of procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Treatment setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Textual description of procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Treatment setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>procedure</b> (Indicates the treatment or processing step  applied to the specimen).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the procedure used to process the specimen
     * </p> 
	 */
	public BoundCodeableConceptDt<SpecimenTreatmentProcedureEnum> getProcedure() {  
		if (myProcedure == null) {
			myProcedure = new BoundCodeableConceptDt<SpecimenTreatmentProcedureEnum>(SpecimenTreatmentProcedureEnum.VALUESET_BINDER);
		}
		return myProcedure;
	}

	/**
	 * Sets the value(s) for <b>procedure</b> (Indicates the treatment or processing step  applied to the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the procedure used to process the specimen
     * </p> 
	 */
	public Treatment setProcedure(BoundCodeableConceptDt<SpecimenTreatmentProcedureEnum> theValue) {
		myProcedure = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>procedure</b> (Indicates the treatment or processing step  applied to the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value specifying the procedure used to process the specimen
     * </p> 
	 */
	public Treatment setProcedure(SpecimenTreatmentProcedureEnum theValue) {
		getProcedure().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>additive</b> (Material used in the processing step).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAdditive() {  
		if (myAdditive == null) {
			myAdditive = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAdditive;
	}

	/**
	 * Sets the value(s) for <b>additive</b> (Material used in the processing step)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Treatment setAdditive(java.util.List<ResourceReferenceDt> theValue) {
		myAdditive = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>additive</b> (Material used in the processing step)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addAdditive() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAdditive().add(newType);
		return newType; 
	}
  

	}


	/**
	 * Block class for child element: <b>Specimen.container</b> (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     * </p> 
	 */
	@Block(name="Specimen.container")	
	public static class Container extends BaseElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Id for the container",
		formalDefinition="Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Textual description of the container",
		formalDefinition=""
	)
	private StringDt myDescription;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Kind of container directly associated with specimen",
		formalDefinition="The type of container associated with the specimen (e.g. slide, aliquot, etc)"
	)
	private CodeableConceptDt myType;
	
	@Child(name="capacity", type=QuantityDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Container volume or size",
		formalDefinition="The capacity (volume or other measure) the container may contain."
	)
	private QuantityDt myCapacity;
	
	@Child(name="specimenQuantity", type=QuantityDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Quantity of specimen within container",
		formalDefinition="The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type"
	)
	private QuantityDt mySpecimenQuantity;
	
	@Child(name="additive", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Substance.class	})
	@Description(
		shortDefinition="Additive associated with container",
		formalDefinition="Additive associated with the container"
	)
	private ResourceReferenceDt myAdditive;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myDescription,  myType,  myCapacity,  mySpecimenQuantity,  myAdditive);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myDescription, myType, myCapacity, mySpecimenQuantity, myAdditive);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Id for the container).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Id for the container)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances
     * </p> 
	 */
	public Container setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Id for the container)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Id for the container),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Id for the container)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Container addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Id for the container)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Container addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (Textual description of the container).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Textual description of the container)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Container setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Textual description of the container)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Container setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (Kind of container directly associated with specimen).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of container associated with the specimen (e.g. slide, aliquot, etc)
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Kind of container directly associated with specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of container associated with the specimen (e.g. slide, aliquot, etc)
     * </p> 
	 */
	public Container setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>capacity</b> (Container volume or size).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The capacity (volume or other measure) the container may contain.
     * </p> 
	 */
	public QuantityDt getCapacity() {  
		if (myCapacity == null) {
			myCapacity = new QuantityDt();
		}
		return myCapacity;
	}

	/**
	 * Sets the value(s) for <b>capacity</b> (Container volume or size)
	 *
     * <p>
     * <b>Definition:</b>
     * The capacity (volume or other measure) the container may contain.
     * </p> 
	 */
	public Container setCapacity(QuantityDt theValue) {
		myCapacity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>capacity</b> (Container volume or size)
	 *
     * <p>
     * <b>Definition:</b>
     * The capacity (volume or other measure) the container may contain.
     * </p> 
	 */
	public Container setCapacity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myCapacity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>capacity</b> (Container volume or size)
	 *
     * <p>
     * <b>Definition:</b>
     * The capacity (volume or other measure) the container may contain.
     * </p> 
	 */
	public Container setCapacity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myCapacity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>capacity</b> (Container volume or size)
	 *
     * <p>
     * <b>Definition:</b>
     * The capacity (volume or other measure) the container may contain.
     * </p> 
	 */
	public Container setCapacity( long theValue) {
		myCapacity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>capacity</b> (Container volume or size)
	 *
     * <p>
     * <b>Definition:</b>
     * The capacity (volume or other measure) the container may contain.
     * </p> 
	 */
	public Container setCapacity( double theValue) {
		myCapacity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>specimenQuantity</b> (Quantity of specimen within container).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type
     * </p> 
	 */
	public QuantityDt getSpecimenQuantity() {  
		if (mySpecimenQuantity == null) {
			mySpecimenQuantity = new QuantityDt();
		}
		return mySpecimenQuantity;
	}

	/**
	 * Sets the value(s) for <b>specimenQuantity</b> (Quantity of specimen within container)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type
     * </p> 
	 */
	public Container setSpecimenQuantity(QuantityDt theValue) {
		mySpecimenQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>specimenQuantity</b> (Quantity of specimen within container)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type
     * </p> 
	 */
	public Container setSpecimenQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		mySpecimenQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>specimenQuantity</b> (Quantity of specimen within container)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type
     * </p> 
	 */
	public Container setSpecimenQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		mySpecimenQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>specimenQuantity</b> (Quantity of specimen within container)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type
     * </p> 
	 */
	public Container setSpecimenQuantity( long theValue) {
		mySpecimenQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>specimenQuantity</b> (Quantity of specimen within container)
	 *
     * <p>
     * <b>Definition:</b>
     * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type
     * </p> 
	 */
	public Container setSpecimenQuantity( double theValue) {
		mySpecimenQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>additive</b> (Additive associated with container).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additive associated with the container
     * </p> 
	 */
	public ResourceReferenceDt getAdditive() {  
		if (myAdditive == null) {
			myAdditive = new ResourceReferenceDt();
		}
		return myAdditive;
	}

	/**
	 * Sets the value(s) for <b>additive</b> (Additive associated with container)
	 *
     * <p>
     * <b>Definition:</b>
     * Additive associated with the container
     * </p> 
	 */
	public Container setAdditive(ResourceReferenceDt theValue) {
		myAdditive = theValue;
		return this;
	}

  

	}




}
