











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
	private List<Source> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
		Substance.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private Collection myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<Treatment> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<Container> myContainer;
	
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
	public List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new ArrayList<IdentifierDt>();
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
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
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
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
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
	public List<Source> getSource() {  
		if (mySource == null) {
			mySource = new ArrayList<Source>();
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
	public void setSource(List<Source> theValue) {
		mySource = theValue;
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
	public ResourceReference getSubject() {  
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
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
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
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
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
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>receivedTime</b> (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTimeWithSecondsPrecision( Date theDate) {
		myReceivedTime = new DateTimeDt(theDate); 
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
	public void setCollection(Collection theValue) {
		myCollection = theValue;
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
	public List<Treatment> getTreatment() {  
		if (myTreatment == null) {
			myTreatment = new ArrayList<Treatment>();
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
	public void setTreatment(List<Treatment> theValue) {
		myTreatment = theValue;
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
	public List<Container> getContainer() {  
		if (myContainer == null) {
			myContainer = new ArrayList<Container>();
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
	public void setContainer(List<Container> theValue) {
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
	private List<Source> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
		Substance.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private Collection myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<Treatment> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<Container> myContainer;
	
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
	public List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new ArrayList<IdentifierDt>();
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
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
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
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
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
	public List<Source> getSource() {  
		if (mySource == null) {
			mySource = new ArrayList<Source>();
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
	public void setSource(List<Source> theValue) {
		mySource = theValue;
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
	public ResourceReference getSubject() {  
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
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
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
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
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
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>receivedTime</b> (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTimeWithSecondsPrecision( Date theDate) {
		myReceivedTime = new DateTimeDt(theDate); 
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
	public void setCollection(Collection theValue) {
		myCollection = theValue;
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
	public List<Treatment> getTreatment() {  
		if (myTreatment == null) {
			myTreatment = new ArrayList<Treatment>();
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
	public void setTreatment(List<Treatment> theValue) {
		myTreatment = theValue;
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
	public List<Container> getContainer() {  
		if (myContainer == null) {
			myContainer = new ArrayList<Container>();
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
	public void setContainer(List<Container> theValue) {
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
	private List<Source> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
		Substance.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private Collection myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<Treatment> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<Container> myContainer;
	
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
	public List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new ArrayList<IdentifierDt>();
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
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
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
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
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
	public List<Source> getSource() {  
		if (mySource == null) {
			mySource = new ArrayList<Source>();
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
	public void setSource(List<Source> theValue) {
		mySource = theValue;
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
	public ResourceReference getSubject() {  
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
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
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
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
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
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>receivedTime</b> (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTimeWithSecondsPrecision( Date theDate) {
		myReceivedTime = new DateTimeDt(theDate); 
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
	public void setCollection(Collection theValue) {
		myCollection = theValue;
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
	public List<Treatment> getTreatment() {  
		if (myTreatment == null) {
			myTreatment = new ArrayList<Treatment>();
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
	public void setTreatment(List<Treatment> theValue) {
		myTreatment = theValue;
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
	public List<Container> getContainer() {  
		if (myContainer == null) {
			myContainer = new ArrayList<Container>();
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
	public void setContainer(List<Container> theValue) {
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
	private List<Source> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
		Substance.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private Collection myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<Treatment> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<Container> myContainer;
	
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
	public List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new ArrayList<IdentifierDt>();
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
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
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
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
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
	public List<Source> getSource() {  
		if (mySource == null) {
			mySource = new ArrayList<Source>();
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
	public void setSource(List<Source> theValue) {
		mySource = theValue;
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
	public ResourceReference getSubject() {  
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
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
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
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
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
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>receivedTime</b> (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTimeWithSecondsPrecision( Date theDate) {
		myReceivedTime = new DateTimeDt(theDate); 
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
	public void setCollection(Collection theValue) {
		myCollection = theValue;
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
	public List<Treatment> getTreatment() {  
		if (myTreatment == null) {
			myTreatment = new ArrayList<Treatment>();
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
	public void setTreatment(List<Treatment> theValue) {
		myTreatment = theValue;
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
	public List<Container> getContainer() {  
		if (myContainer == null) {
			myContainer = new ArrayList<Container>();
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
	public void setContainer(List<Container> theValue) {
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
	private List<Source> mySource;
	
	@Child(name="subject", order=3, min=1, max=1)
	@ChildResource(types= {
		Patient.class,
		Group.class,
		Device.class,
		Substance.class,
	})	
	private ResourceReference mySubject;
	
	@Child(name="accessionIdentifier", type=IdentifierDt.class, order=4, min=0, max=1)	
	private IdentifierDt myAccessionIdentifier;
	
	@Child(name="receivedTime", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myReceivedTime;
	
	@Child(name="collection", order=6, min=1, max=1)	
	private Collection myCollection;
	
	@Child(name="treatment", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<Treatment> myTreatment;
	
	@Child(name="container", order=8, min=0, max=Child.MAX_UNLIMITED)	
	private List<Container> myContainer;
	
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
	public List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new ArrayList<IdentifierDt>();
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
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
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
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
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
	public List<Source> getSource() {  
		if (mySource == null) {
			mySource = new ArrayList<Source>();
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
	public void setSource(List<Source> theValue) {
		mySource = theValue;
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
	public ResourceReference getSubject() {  
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
	public void setSubject(ResourceReference theValue) {
		mySubject = theValue;
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
	public void setAccessionIdentifier(IdentifierDt theValue) {
		myAccessionIdentifier = theValue;
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
	public void setReceivedTime(DateTimeDt theValue) {
		myReceivedTime = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>receivedTime</b> (The time when specimen was received for processing)
	 *
     * <p>
     * <b>Definition:</b>
     * Time when specimen was received for processing or testing
     * </p> 
	 */
	public void setReceivedTimeWithSecondsPrecision( Date theDate) {
		myReceivedTime = new DateTimeDt(theDate); 
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
	public void setCollection(Collection theValue) {
		myCollection = theValue;
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
	public List<Treatment> getTreatment() {  
		if (myTreatment == null) {
			myTreatment = new ArrayList<Treatment>();
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
	public void setTreatment(List<Treatment> theValue) {
		myTreatment = theValue;
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
	public List<Container> getContainer() {  
		if (myContainer == null) {
			myContainer = new ArrayList<Container>();
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
	public void setContainer(List<Container> theValue) {
		myContainer = theValue;
	}
	
 
	}



}