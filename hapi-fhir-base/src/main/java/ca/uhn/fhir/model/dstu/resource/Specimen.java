















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;

import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;
import ca.uhn.fhir.model.dstu.valueset.*;

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
public class Specimen extends BaseResource implements IResource {

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
	public static class Source extends BaseElement implements IResourceBlock {
	
	@Child(name="relationship", type=CodeDt.class, order=0, min=1, max=1)	
	private BoundCodeDt<HierarchicalRelationshipTypeEnum> myRelationship;
	
	@Child(name="target", order=1, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Specimen.class,
	})	
	private List<ResourceReference> myTarget;
	
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
	public void setRelationship(BoundCodeDt<HierarchicalRelationshipTypeEnum> theValue) {
		myRelationship = theValue;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (parent | child)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this relationship is to a parent or to a child
     * </p> 
	 */
	public void setRelationship(HierarchicalRelationshipTypeEnum theValue) {
		getRelationship().setValueAsEnum(theValue);
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
	public List<ResourceReference> getTarget() {  
		if (myTarget == null) {
			myTarget = new ArrayList<ResourceReference>();
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
	public void setTarget(List<ResourceReference> theValue) {
		myTarget = theValue;
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
	
	@Child(name="collector", order=0, min=0, max=1)
	@ChildResource(types= {
		Practitioner.class,
	})	
	private ResourceReference myCollector;
	
	@Child(name="comment", type=StringDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> myComment;
	
	@Child(name="collected", order=2, min=0, max=1, choice=@Choice(types= {
		DateTimeDt.class,
		PeriodDt.class,
	}))	
	private IDatatype myCollected;
	
	@Child(name="quantity", type=QuantityDt.class, order=3, min=0, max=1)	
	private QuantityDt myQuantity;
	
	@Child(name="method", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	private CodeableConceptDt myMethod;
	
	@Child(name="sourceSite", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	private CodeableConceptDt mySourceSite;
	
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
	public ResourceReference getCollector() {  
		if (myCollector == null) {
			myCollector = new ResourceReference();
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
	public void setCollector(ResourceReference theValue) {
		myCollector = theValue;
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
	public List<StringDt> getComment() {  
		if (myComment == null) {
			myComment = new ArrayList<StringDt>();
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
	public void setComment(List<StringDt> theValue) {
		myComment = theValue;
	}

 	/**
	 * Sets the value(s) for <b>comment</b> (Collector comments)
	 *
     * <p>
     * <b>Definition:</b>
     * To communicate any details or issues encountered during the specimen collection procedure.
     * </p> 
	 */
	public void addComment( String theString) {
		if (myComment == null) {
			myComment = new ArrayList<StringDt>();
		}
		myComment.add(new StringDt(theString)); 
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
	public void setCollected(IDatatype theValue) {
		myCollected = theValue;
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
	public void setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
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
	public CodeableConceptDt getMethod() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
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
	public void setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
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
	public void setSourceSite(CodeableConceptDt theValue) {
		mySourceSite = theValue;
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
	private StringDt myDescription;
	
	@Child(name="procedure", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	private CodeableConceptDt myProcedure;
	
	@Child(name="additive", order=2, min=0, max=Child.MAX_UNLIMITED)
	@ChildResource(types= {
		Substance.class,
	})	
	private List<ResourceReference> myAdditive;
	
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
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}

 	/**
	 * Sets the value(s) for <b>description</b> (Textual description of procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
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
	public CodeableConceptDt getProcedure() {  
		if (myProcedure == null) {
			myProcedure = new CodeableConceptDt();
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
	public void setProcedure(CodeableConceptDt theValue) {
		myProcedure = theValue;
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
	public List<ResourceReference> getAdditive() {  
		if (myAdditive == null) {
			myAdditive = new ArrayList<ResourceReference>();
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
	public void setAdditive(List<ResourceReference> theValue) {
		myAdditive = theValue;
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
	private List<IdentifierDt> myIdentifier;
	
	@Child(name="description", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myDescription;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	private CodeableConceptDt myType;
	
	@Child(name="capacity", type=QuantityDt.class, order=3, min=0, max=1)	
	private QuantityDt myCapacity;
	
	@Child(name="specimenQuantity", type=QuantityDt.class, order=4, min=0, max=1)	
	private QuantityDt mySpecimenQuantity;
	
	@Child(name="additive", order=5, min=0, max=1)
	@ChildResource(types= {
		Substance.class,
	})	
	private ResourceReference myAdditive;
	
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
	public List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new ArrayList<IdentifierDt>();
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
	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
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
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}

 	/**
	 * Sets the value(s) for <b>description</b> (Textual description of the container)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
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
	public void setType(CodeableConceptDt theValue) {
		myType = theValue;
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
	public void setCapacity(QuantityDt theValue) {
		myCapacity = theValue;
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
	public void setSpecimenQuantity(QuantityDt theValue) {
		mySpecimenQuantity = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>additive</b> (Additive associated with container ).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additive associated with the container
     * </p> 
	 */
	public ResourceReference getAdditive() {  
		if (myAdditive == null) {
			myAdditive = new ResourceReference();
		}
		return myAdditive;
	}

	/**
	 * Sets the value(s) for <b>additive</b> (Additive associated with container )
	 *
     * <p>
     * <b>Definition:</b>
     * Additive associated with the container
     * </p> 
	 */
	public void setAdditive(ResourceReference theValue) {
		myAdditive = theValue;
	}

  

	}




}