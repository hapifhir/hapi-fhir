















package ca.uhn.fhir.model.dstu.resource;


import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ProcedureRelationshipTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;


/**
 * HAPI/FHIR <b>Procedure</b> Resource
 * (An action that is performed on a patient)
 *
 * <p>
 * <b>Definition:</b>
 * An action that is performed on a patient. This can be a physical 'thing' like an operation, or less invasive like counseling or hypnotherapy
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Procedure">http://hl7.org/fhir/profiles/Procedure</a> 
 * </p>
 *
 */
@ResourceDef(name="Procedure", profile="http://hl7.org/fhir/profiles/Procedure", id="procedure")
public class Procedure extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>Type of procedure</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Procedure.type</b><br/>
	 * </p>
	 */
	public static final String SP_TYPE = "type";

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b>The identity of a patient to list procedures  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Procedure.subject</b><br/>
	 * </p>
	 */
	public static final String SP_SUBJECT = "subject";

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The date the procedure was performed on</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Procedure.date</b><br/>
	 * </p>
	 */
	public static final String SP_DATE = "date";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Ids for this procedure",
		formalDefinition="This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class,
	})
	@Description(
		shortDefinition="Who procedure was performed on",
		formalDefinition="The person on whom the procedure was performed"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Identification of the procedure",
		formalDefinition="The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded"
	)
	private CodeableConceptDt myType;
	
	@Child(name="bodySite", type=CodeableConceptDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Precise location details",
		formalDefinition="Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion"
	)
	private java.util.List<CodeableConceptDt> myBodySite;
	
	@Child(name="indication", type=CodeableConceptDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Reason procedure performed",
		formalDefinition="The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text"
	)
	private java.util.List<CodeableConceptDt> myIndication;
	
	@Child(name="performer", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="The people who performed the procedure",
		formalDefinition="Limited to 'real' people rather than equipment"
	)
	private java.util.List<Performer> myPerformer;
	
	@Child(name="date", type=PeriodDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="The date the procedure was performed",
		formalDefinition="The dates over which the procedure was performed. Allows a period to support complex procedures that span more that one date, and also allows for the length of the procedure to be captured."
	)
	private PeriodDt myDate;
	
	@Child(name="encounter", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Encounter.class,
	})
	@Description(
		shortDefinition="The encounter when procedure performed",
		formalDefinition="The encounter during which the procedure was performed"
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="outcome", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="What was result of procedure?",
		formalDefinition="What was the outcome of the procedure - did it resolve reasons why the procedure was performed?"
	)
	private StringDt myOutcome;
	
	@Child(name="report", order=9, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.DiagnosticReport.class,
	})
	@Description(
		shortDefinition="Any report that results from the procedure",
		formalDefinition="This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies"
	)
	private java.util.List<ResourceReferenceDt> myReport;
	
	@Child(name="complication", type=CodeableConceptDt.class, order=10, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Complication following the procedure",
		formalDefinition="Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues"
	)
	private java.util.List<CodeableConceptDt> myComplication;
	
	@Child(name="followUp", type=StringDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="Instructions for follow up",
		formalDefinition="If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used"
	)
	private StringDt myFollowUp;
	
	@Child(name="relatedItem", order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A procedure that is related to this one",
		formalDefinition="Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure"
	)
	private java.util.List<RelatedItem> myRelatedItem;
	
	@Child(name="notes", type=StringDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="Additional information about procedure",
		formalDefinition="Any other notes about the procedure - e.g. the operative notes"
	)
	private StringDt myNotes;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  mySubject,  myType,  myBodySite,  myIndication,  myPerformer,  myDate,  myEncounter,  myOutcome,  myReport,  myComplication,  myFollowUp,  myRelatedItem,  myNotes);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, mySubject, myType, myBodySite, myIndication, myPerformer, myDate, myEncounter, myOutcome, myReport, myComplication, myFollowUp, myRelatedItem, myNotes);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Ids for this procedure).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Ids for this procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public Procedure setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Ids for this procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Ids for this procedure),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Procedure addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Ids for this procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Procedure addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Who procedure was performed on).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person on whom the procedure was performed
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who procedure was performed on)
	 *
     * <p>
     * <b>Definition:</b>
     * The person on whom the procedure was performed
     * </p> 
	 */
	public Procedure setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Identification of the procedure).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Identification of the procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded
     * </p> 
	 */
	public Procedure setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>bodySite</b> (Precise location details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getBodySite() {  
		if (myBodySite == null) {
			myBodySite = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myBodySite;
	}

	/**
	 * Sets the value(s) for <b>bodySite</b> (Precise location details)
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion
     * </p> 
	 */
	public Procedure setBodySite(java.util.List<CodeableConceptDt> theValue) {
		myBodySite = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>bodySite</b> (Precise location details)
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion
     * </p> 
	 */
	public CodeableConceptDt addBodySite() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getBodySite().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>bodySite</b> (Precise location details),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion
     * </p> 
	 */
	public CodeableConceptDt getBodySiteFirstRep() {
		if (getBodySite().isEmpty()) {
			return addBodySite();
		}
		return getBodySite().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>indication</b> (Reason procedure performed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getIndication() {  
		if (myIndication == null) {
			myIndication = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myIndication;
	}

	/**
	 * Sets the value(s) for <b>indication</b> (Reason procedure performed)
	 *
     * <p>
     * <b>Definition:</b>
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text
     * </p> 
	 */
	public Procedure setIndication(java.util.List<CodeableConceptDt> theValue) {
		myIndication = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>indication</b> (Reason procedure performed)
	 *
     * <p>
     * <b>Definition:</b>
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text
     * </p> 
	 */
	public CodeableConceptDt addIndication() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getIndication().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>indication</b> (Reason procedure performed),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text
     * </p> 
	 */
	public CodeableConceptDt getIndicationFirstRep() {
		if (getIndication().isEmpty()) {
			return addIndication();
		}
		return getIndication().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>performer</b> (The people who performed the procedure).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	public java.util.List<Performer> getPerformer() {  
		if (myPerformer == null) {
			myPerformer = new java.util.ArrayList<Performer>();
		}
		return myPerformer;
	}

	/**
	 * Sets the value(s) for <b>performer</b> (The people who performed the procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	public Procedure setPerformer(java.util.List<Performer> theValue) {
		myPerformer = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>performer</b> (The people who performed the procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	public Performer addPerformer() {
		Performer newType = new Performer();
		getPerformer().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>performer</b> (The people who performed the procedure),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	public Performer getPerformerFirstRep() {
		if (getPerformer().isEmpty()) {
			return addPerformer();
		}
		return getPerformer().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>date</b> (The date the procedure was performed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The dates over which the procedure was performed. Allows a period to support complex procedures that span more that one date, and also allows for the length of the procedure to be captured.
     * </p> 
	 */
	public PeriodDt getDate() {  
		if (myDate == null) {
			myDate = new PeriodDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (The date the procedure was performed)
	 *
     * <p>
     * <b>Definition:</b>
     * The dates over which the procedure was performed. Allows a period to support complex procedures that span more that one date, and also allows for the length of the procedure to be captured.
     * </p> 
	 */
	public Procedure setDate(PeriodDt theValue) {
		myDate = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>encounter</b> (The encounter when procedure performed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The encounter during which the procedure was performed
     * </p> 
	 */
	public ResourceReferenceDt getEncounter() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}

	/**
	 * Sets the value(s) for <b>encounter</b> (The encounter when procedure performed)
	 *
     * <p>
     * <b>Definition:</b>
     * The encounter during which the procedure was performed
     * </p> 
	 */
	public Procedure setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>outcome</b> (What was result of procedure?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * What was the outcome of the procedure - did it resolve reasons why the procedure was performed?
     * </p> 
	 */
	public StringDt getOutcome() {  
		if (myOutcome == null) {
			myOutcome = new StringDt();
		}
		return myOutcome;
	}

	/**
	 * Sets the value(s) for <b>outcome</b> (What was result of procedure?)
	 *
     * <p>
     * <b>Definition:</b>
     * What was the outcome of the procedure - did it resolve reasons why the procedure was performed?
     * </p> 
	 */
	public Procedure setOutcome(StringDt theValue) {
		myOutcome = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>outcome</b> (What was result of procedure?)
	 *
     * <p>
     * <b>Definition:</b>
     * What was the outcome of the procedure - did it resolve reasons why the procedure was performed?
     * </p> 
	 */
	public Procedure setOutcome( String theString) {
		myOutcome = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>report</b> (Any report that results from the procedure).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getReport() {  
		if (myReport == null) {
			myReport = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myReport;
	}

	/**
	 * Sets the value(s) for <b>report</b> (Any report that results from the procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies
     * </p> 
	 */
	public Procedure setReport(java.util.List<ResourceReferenceDt> theValue) {
		myReport = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>report</b> (Any report that results from the procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies
     * </p> 
	 */
	public ResourceReferenceDt addReport() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getReport().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>complication</b> (Complication following the procedure).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getComplication() {  
		if (myComplication == null) {
			myComplication = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myComplication;
	}

	/**
	 * Sets the value(s) for <b>complication</b> (Complication following the procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues
     * </p> 
	 */
	public Procedure setComplication(java.util.List<CodeableConceptDt> theValue) {
		myComplication = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>complication</b> (Complication following the procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues
     * </p> 
	 */
	public CodeableConceptDt addComplication() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getComplication().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>complication</b> (Complication following the procedure),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues
     * </p> 
	 */
	public CodeableConceptDt getComplicationFirstRep() {
		if (getComplication().isEmpty()) {
			return addComplication();
		}
		return getComplication().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>followUp</b> (Instructions for follow up).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used
     * </p> 
	 */
	public StringDt getFollowUp() {  
		if (myFollowUp == null) {
			myFollowUp = new StringDt();
		}
		return myFollowUp;
	}

	/**
	 * Sets the value(s) for <b>followUp</b> (Instructions for follow up)
	 *
     * <p>
     * <b>Definition:</b>
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used
     * </p> 
	 */
	public Procedure setFollowUp(StringDt theValue) {
		myFollowUp = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>followUp</b> (Instructions for follow up)
	 *
     * <p>
     * <b>Definition:</b>
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used
     * </p> 
	 */
	public Procedure setFollowUp( String theString) {
		myFollowUp = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>relatedItem</b> (A procedure that is related to this one).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	public java.util.List<RelatedItem> getRelatedItem() {  
		if (myRelatedItem == null) {
			myRelatedItem = new java.util.ArrayList<RelatedItem>();
		}
		return myRelatedItem;
	}

	/**
	 * Sets the value(s) for <b>relatedItem</b> (A procedure that is related to this one)
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	public Procedure setRelatedItem(java.util.List<RelatedItem> theValue) {
		myRelatedItem = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>relatedItem</b> (A procedure that is related to this one)
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	public RelatedItem addRelatedItem() {
		RelatedItem newType = new RelatedItem();
		getRelatedItem().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relatedItem</b> (A procedure that is related to this one),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	public RelatedItem getRelatedItemFirstRep() {
		if (getRelatedItem().isEmpty()) {
			return addRelatedItem();
		}
		return getRelatedItem().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>notes</b> (Additional information about procedure).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Any other notes about the procedure - e.g. the operative notes
     * </p> 
	 */
	public StringDt getNotes() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	/**
	 * Sets the value(s) for <b>notes</b> (Additional information about procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * Any other notes about the procedure - e.g. the operative notes
     * </p> 
	 */
	public Procedure setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>notes</b> (Additional information about procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * Any other notes about the procedure - e.g. the operative notes
     * </p> 
	 */
	public Procedure setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Procedure.performer</b> (The people who performed the procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * Limited to 'real' people rather than equipment
     * </p> 
	 */
	@Block(name="Procedure.performer")	
	public static class Performer extends BaseElement implements IResourceBlock {
	
	@Child(name="person", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class,
	})
	@Description(
		shortDefinition="The reference to the practitioner",
		formalDefinition="The practitioner who was involved in the procedure"
	)
	private ResourceReferenceDt myPerson;
	
	@Child(name="role", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="The role the person was in",
		formalDefinition="E.g. surgeon, anaethetist, endoscopist"
	)
	private CodeableConceptDt myRole;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myPerson,  myRole);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myPerson, myRole);
	}

	/**
	 * Gets the value(s) for <b>person</b> (The reference to the practitioner).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner who was involved in the procedure
     * </p> 
	 */
	public ResourceReferenceDt getPerson() {  
		if (myPerson == null) {
			myPerson = new ResourceReferenceDt();
		}
		return myPerson;
	}

	/**
	 * Sets the value(s) for <b>person</b> (The reference to the practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner who was involved in the procedure
     * </p> 
	 */
	public Performer setPerson(ResourceReferenceDt theValue) {
		myPerson = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>role</b> (The role the person was in).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * E.g. surgeon, anaethetist, endoscopist
     * </p> 
	 */
	public CodeableConceptDt getRole() {  
		if (myRole == null) {
			myRole = new CodeableConceptDt();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (The role the person was in)
	 *
     * <p>
     * <b>Definition:</b>
     * E.g. surgeon, anaethetist, endoscopist
     * </p> 
	 */
	public Performer setRole(CodeableConceptDt theValue) {
		myRole = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>Procedure.relatedItem</b> (A procedure that is related to this one)
	 *
     * <p>
     * <b>Definition:</b>
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure
     * </p> 
	 */
	@Block(name="Procedure.relatedItem")	
	public static class RelatedItem extends BaseElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="caused-by | because-of",
		formalDefinition="The nature of the relationship"
	)
	private BoundCodeDt<ProcedureRelationshipTypeEnum> myType;
	
	@Child(name="target", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.AdverseReaction.class,
		ca.uhn.fhir.model.dstu.resource.AllergyIntolerance.class,
		ca.uhn.fhir.model.dstu.resource.CarePlan.class,
		ca.uhn.fhir.model.dstu.resource.Condition.class,
		ca.uhn.fhir.model.dstu.resource.DeviceObservationReport.class,
		ca.uhn.fhir.model.dstu.resource.DiagnosticReport.class,
		ca.uhn.fhir.model.dstu.resource.FamilyHistory.class,
		ca.uhn.fhir.model.dstu.resource.ImagingStudy.class,
		ca.uhn.fhir.model.dstu.resource.Immunization.class,
		ca.uhn.fhir.model.dstu.resource.ImmunizationRecommendation.class,
		ca.uhn.fhir.model.dstu.resource.MedicationAdministration.class,
		ca.uhn.fhir.model.dstu.resource.MedicationDispense.class,
		ca.uhn.fhir.model.dstu.resource.MedicationPrescription.class,
		ca.uhn.fhir.model.dstu.resource.MedicationStatement.class,
		ca.uhn.fhir.model.dstu.resource.Observation.class,
		ca.uhn.fhir.model.dstu.resource.Procedure.class,
	})
	@Description(
		shortDefinition="The related item - e.g. a procedure",
		formalDefinition=""
	)
	private ResourceReferenceDt myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myTarget);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myTarget);
	}

	/**
	 * Gets the value(s) for <b>type</b> (caused-by | because-of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship
     * </p> 
	 */
	public BoundCodeDt<ProcedureRelationshipTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<ProcedureRelationshipTypeEnum>(ProcedureRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (caused-by | because-of)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship
     * </p> 
	 */
	public RelatedItem setType(BoundCodeDt<ProcedureRelationshipTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (caused-by | because-of)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship
     * </p> 
	 */
	public RelatedItem setType(ProcedureRelationshipTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> (The related item - e.g. a procedure).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getTarget() {  
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (The related item - e.g. a procedure)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public RelatedItem setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}

  

	}




}