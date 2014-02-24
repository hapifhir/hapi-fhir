











package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;

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
 */
@ResourceDef(name="Specimen")
public class Specimen implements IResource {

	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="source", order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
		
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private IDatatype myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myContainer;
	
	/**
	 * Gets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public CodeableConceptDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
	}
	
	/**
	 * Gets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public List<IDatatype> getSource() {
		return mySource;
	}

	/**
	 * Sets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public void setSource(List<IDatatype> theValue) {
		mySource = theValue;
	}
	
	/**
	 * Gets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public ResourceReference getSubject() {
		return mySubject;
	}

	/**
	 * Sets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
	}
	
	/**
	 * Gets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public IdentifierDt getAccessionIdentifier() {
		return myAccessionIdentifier;
	}

	/**
	 * Sets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public DateTimeDt getReceivedTime() {
		return myReceivedTime;
	}

	/**
	 * Sets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Gets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public IDatatype getCollection() {
		return myCollection;
	}

	/**
	 * Sets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public void setCollection(IDatatype theValue) {
		myCollection = theValue;
	}
	
	/**
	 * Gets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public List<IDatatype> getTreatment() {
		return myTreatment;
	}

	/**
	 * Sets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public void setTreatment(List<IDatatype> theValue) {
		myTreatment = theValue;
	}
	
	/**
	 * Gets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public List<IDatatype> getContainer() {
		return myContainer;
	}

	/**
	 * Sets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public void setContainer(List<IDatatype> theValue) {
		myContainer = theValue;
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
	public static class Source implements IResourceBlock {
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="source", order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private IDatatype myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myContainer;
	
	/**
	 * Gets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public CodeableConceptDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
	}
	
	/**
	 * Gets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public List<IDatatype> getSource() {
		return mySource;
	}

	/**
	 * Sets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public void setSource(List<IDatatype> theValue) {
		mySource = theValue;
	}
	
	/**
	 * Gets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public ResourceReference getSubject() {
		return mySubject;
	}

	/**
	 * Sets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
	}
	
	/**
	 * Gets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public IdentifierDt getAccessionIdentifier() {
		return myAccessionIdentifier;
	}

	/**
	 * Sets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public DateTimeDt getReceivedTime() {
		return myReceivedTime;
	}

	/**
	 * Sets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Gets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public IDatatype getCollection() {
		return myCollection;
	}

	/**
	 * Sets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public void setCollection(IDatatype theValue) {
		myCollection = theValue;
	}
	
	/**
	 * Gets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public List<IDatatype> getTreatment() {
		return myTreatment;
	}

	/**
	 * Sets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public void setTreatment(List<IDatatype> theValue) {
		myTreatment = theValue;
	}
	
	/**
	 * Gets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public List<IDatatype> getContainer() {
		return myContainer;
	}

	/**
	 * Sets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public void setContainer(List<IDatatype> theValue) {
		myContainer = theValue;
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
	public static class Collection implements IResourceBlock {
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="source", order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private IDatatype myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myContainer;
	
	/**
	 * Gets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public CodeableConceptDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
	}
	
	/**
	 * Gets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public List<IDatatype> getSource() {
		return mySource;
	}

	/**
	 * Sets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public void setSource(List<IDatatype> theValue) {
		mySource = theValue;
	}
	
	/**
	 * Gets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public ResourceReference getSubject() {
		return mySubject;
	}

	/**
	 * Sets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
	}
	
	/**
	 * Gets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public IdentifierDt getAccessionIdentifier() {
		return myAccessionIdentifier;
	}

	/**
	 * Sets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public DateTimeDt getReceivedTime() {
		return myReceivedTime;
	}

	/**
	 * Sets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Gets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public IDatatype getCollection() {
		return myCollection;
	}

	/**
	 * Sets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public void setCollection(IDatatype theValue) {
		myCollection = theValue;
	}
	
	/**
	 * Gets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public List<IDatatype> getTreatment() {
		return myTreatment;
	}

	/**
	 * Sets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public void setTreatment(List<IDatatype> theValue) {
		myTreatment = theValue;
	}
	
	/**
	 * Gets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public List<IDatatype> getContainer() {
		return myContainer;
	}

	/**
	 * Sets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public void setContainer(List<IDatatype> theValue) {
		myContainer = theValue;
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
	public static class Treatment implements IResourceBlock {
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="source", order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private IDatatype myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myContainer;
	
	/**
	 * Gets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public CodeableConceptDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
	}
	
	/**
	 * Gets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public List<IDatatype> getSource() {
		return mySource;
	}

	/**
	 * Sets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public void setSource(List<IDatatype> theValue) {
		mySource = theValue;
	}
	
	/**
	 * Gets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public ResourceReference getSubject() {
		return mySubject;
	}

	/**
	 * Sets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
	}
	
	/**
	 * Gets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public IdentifierDt getAccessionIdentifier() {
		return myAccessionIdentifier;
	}

	/**
	 * Sets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public DateTimeDt getReceivedTime() {
		return myReceivedTime;
	}

	/**
	 * Sets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Gets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public IDatatype getCollection() {
		return myCollection;
	}

	/**
	 * Sets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public void setCollection(IDatatype theValue) {
		myCollection = theValue;
	}
	
	/**
	 * Gets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public List<IDatatype> getTreatment() {
		return myTreatment;
	}

	/**
	 * Sets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public void setTreatment(List<IDatatype> theValue) {
		myTreatment = theValue;
	}
	
	/**
	 * Gets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public List<IDatatype> getContainer() {
		return myContainer;
	}

	/**
	 * Sets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public void setContainer(List<IDatatype> theValue) {
		myContainer = theValue;
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
	public static class Container implements IResourceBlock {
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="type", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="source", order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private IDatatype myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myContainer;
	
	/**
	 * Gets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Id for specimen
     * </p> 
	 */
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public CodeableConceptDt getType() {
		return myType;
	}

	/**
	 * Sets the value(s) for type (Kind of material that forms the specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Kind of material that forms the specimen
     * </p> 
	 */
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
	}
	
	/**
	 * Gets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public List<IDatatype> getSource() {
		return mySource;
	}

	/**
	 * Sets the value(s) for source (Parent of specimen)
	 *
     * <p>
     * <b>Definition:</b>
     * Parent specimen from which the focal specimen was a component
     * </p> 
	 */
	public void setSource(List<IDatatype> theValue) {
		mySource = theValue;
	}
	
	/**
	 * Gets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public ResourceReference getSubject() {
		return mySubject;
	}

	/**
	 * Sets the value(s) for subject (Where the specimen came from. This may be the patient(s) or from the environment or  a device)
	 *
     * <p>
     * <b>Definition:</b>
     * ${child.definition}
     * </p> 
	 */
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
	}
	
	/**
	 * Gets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public IdentifierDt getAccessionIdentifier() {
		return myAccessionIdentifier;
	}

	/**
	 * Sets the value(s) for accessionIdentifier (Identifier assigned by the lab)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures. 
     * </p> 
	 */
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
	}
	
	/**
	 * Gets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public DateTimeDt getReceivedTime() {
		return myReceivedTime;
	}

	/**
	 * Sets the value(s) for receivedTime (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Gets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public IDatatype getCollection() {
		return myCollection;
	}

	/**
	 * Sets the value(s) for collection (Collection details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning the specimen collection
     * </p> 
	 */
	public void setCollection(IDatatype theValue) {
		myCollection = theValue;
	}
	
	/**
	 * Gets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public List<IDatatype> getTreatment() {
		return myTreatment;
	}

	/**
	 * Sets the value(s) for treatment (Treatment and processing step details)
	 *
     * <p>
     * <b>Definition:</b>
     * Details concerning treatment and processing steps for the specimen
     * </p> 
	 */
	public void setTreatment(List<IDatatype> theValue) {
		myTreatment = theValue;
	}
	
	/**
	 * Gets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public List<IDatatype> getContainer() {
		return myContainer;
	}

	/**
	 * Sets the value(s) for container (Direct container of specimen (tube/slide, etc))
	 *
     * <p>
     * <b>Definition:</b>
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here. 
     * </p> 
	 */
	public void setContainer(List<IDatatype> theValue) {
		myContainer = theValue;
	}
	
	}



}