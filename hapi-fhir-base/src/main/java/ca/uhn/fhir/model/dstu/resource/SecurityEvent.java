















package ca.uhn.fhir.model.dstu.resource;


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
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventActionEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectRoleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectSensitivityEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventParticipantNetworkTypeEnum;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>SecurityEvent</b> Resource
 * (Event record kept for security purposes)
 *
 * <p>
 * <b>Definition:</b>
 * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/SecurityEvent">http://hl7.org/fhir/profiles/SecurityEvent</a> 
 * </p>
 *
 */
@ResourceDef(name="SecurityEvent", profile="http://hl7.org/fhir/profiles/SecurityEvent", id="securityevent")
public class SecurityEvent extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="SecurityEvent.event.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>action</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.action</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="action", path="SecurityEvent.event.action", description="", type="token"  )
	public static final String SP_ACTION = "action";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>action</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.action</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ACTION = new TokenClientParam(SP_ACTION);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SecurityEvent.event.dateTime</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="SecurityEvent.event.dateTime", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>SecurityEvent.event.dateTime</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>subtype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.subtype</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subtype", path="SecurityEvent.event.subtype", description="", type="token"  )
	public static final String SP_SUBTYPE = "subtype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subtype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.event.subtype</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SUBTYPE = new TokenClientParam(SP_SUBTYPE);

	/**
	 * Search parameter constant for <b>user</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.userId</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="user", path="SecurityEvent.participant.userId", description="", type="token"  )
	public static final String SP_USER = "user";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>user</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.userId</b><br/>
	 * </p>
	 */
	public static final TokenClientParam USER = new TokenClientParam(SP_USER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityEvent.participant.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="SecurityEvent.participant.name", description="", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityEvent.participant.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.network.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="address", path="SecurityEvent.participant.network.identifier", description="", type="token"  )
	public static final String SP_ADDRESS = "address";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.network.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ADDRESS = new TokenClientParam(SP_ADDRESS);

	/**
	 * Search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.source.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="source", path="SecurityEvent.source.identifier", description="", type="token"  )
	public static final String SP_SOURCE = "source";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>source</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.source.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SOURCE = new TokenClientParam(SP_SOURCE);

	/**
	 * Search parameter constant for <b>site</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.source.site</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="site", path="SecurityEvent.source.site", description="", type="token"  )
	public static final String SP_SITE = "site";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>site</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.source.site</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SITE = new TokenClientParam(SP_SITE);

	/**
	 * Search parameter constant for <b>object-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.object.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="object-type", path="SecurityEvent.object.type", description="", type="token"  )
	public static final String SP_OBJECT_TYPE = "object-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>object-type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.object.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam OBJECT_TYPE = new TokenClientParam(SP_OBJECT_TYPE);

	/**
	 * Search parameter constant for <b>identity</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.object.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identity", path="SecurityEvent.object.identifier", description="", type="token"  )
	public static final String SP_IDENTITY = "identity";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identity</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.object.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTITY = new TokenClientParam(SP_IDENTITY);

	/**
	 * Search parameter constant for <b>reference</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SecurityEvent.object.reference</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reference", path="SecurityEvent.object.reference", description="", type="reference"  )
	public static final String SP_REFERENCE = "reference";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reference</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>SecurityEvent.object.reference</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam REFERENCE = new ReferenceClientParam(SP_REFERENCE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>SecurityEvent.object.reference</b>".
	 */
	public static final Include INCLUDE_OBJECT_REFERENCE = new Include("SecurityEvent.object.reference");

	/**
	 * Search parameter constant for <b>desc</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityEvent.object.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="desc", path="SecurityEvent.object.name", description="", type="string"  )
	public static final String SP_DESC = "desc";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>desc</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>SecurityEvent.object.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESC = new StringClientParam(SP_DESC);

	/**
	 * Search parameter constant for <b>patientid</b>
	 * <p>
	 * Description: <b>The id of the patient (one of multiple kinds of participations)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patientid", path="", description="The id of the patient (one of multiple kinds of participations)", type="token"  )
	public static final String SP_PATIENTID = "patientid";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patientid</b>
	 * <p>
	 * Description: <b>The id of the patient (one of multiple kinds of participations)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final TokenClientParam PATIENTID = new TokenClientParam(SP_PATIENTID);

	/**
	 * Search parameter constant for <b>altid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.altId</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="altid", path="SecurityEvent.participant.altId", description="", type="token"  )
	public static final String SP_ALTID = "altid";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>altid</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>SecurityEvent.participant.altId</b><br/>
	 * </p>
	 */
	public static final TokenClientParam ALTID = new TokenClientParam(SP_ALTID);


	@Child(name="event", order=0, min=1, max=1)	
	@Description(
		shortDefinition="What was done",
		formalDefinition="Identifies the name, action type, time, and disposition of the audited event"
	)
	private Event myEvent;
	
	@Child(name="participant", order=1, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A person, a hardware device or software process",
		formalDefinition=""
	)
	private java.util.List<Participant> myParticipant;
	
	@Child(name="source", order=2, min=1, max=1)	
	@Description(
		shortDefinition="Application systems and processes",
		formalDefinition=""
	)
	private Source mySource;
	
	@Child(name="object", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Specific instances of data or objects that have been accessed",
		formalDefinition="Specific instances of data or objects that have been accessed"
	)
	private java.util.List<Object> myObject;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myEvent,  myParticipant,  mySource,  myObject);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myEvent, myParticipant, mySource, myObject);
	}

	/**
	 * Gets the value(s) for <b>event</b> (What was done).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name, action type, time, and disposition of the audited event
     * </p> 
	 */
	public Event getEvent() {  
		if (myEvent == null) {
			myEvent = new Event();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (What was done)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name, action type, time, and disposition of the audited event
     * </p> 
	 */
	public SecurityEvent setEvent(Event theValue) {
		myEvent = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>participant</b> (A person, a hardware device or software process).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Participant> getParticipant() {  
		if (myParticipant == null) {
			myParticipant = new java.util.ArrayList<Participant>();
		}
		return myParticipant;
	}

	/**
	 * Sets the value(s) for <b>participant</b> (A person, a hardware device or software process)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public SecurityEvent setParticipant(java.util.List<Participant> theValue) {
		myParticipant = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>participant</b> (A person, a hardware device or software process)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant addParticipant() {
		Participant newType = new Participant();
		getParticipant().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>participant</b> (A person, a hardware device or software process),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Participant getParticipantFirstRep() {
		if (getParticipant().isEmpty()) {
			return addParticipant();
		}
		return getParticipant().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>source</b> (Application systems and processes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Source getSource() {  
		if (mySource == null) {
			mySource = new Source();
		}
		return mySource;
	}

	/**
	 * Sets the value(s) for <b>source</b> (Application systems and processes)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public SecurityEvent setSource(Source theValue) {
		mySource = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>object</b> (Specific instances of data or objects that have been accessed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	public java.util.List<Object> getObject() {  
		if (myObject == null) {
			myObject = new java.util.ArrayList<Object>();
		}
		return myObject;
	}

	/**
	 * Sets the value(s) for <b>object</b> (Specific instances of data or objects that have been accessed)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	public SecurityEvent setObject(java.util.List<Object> theValue) {
		myObject = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>object</b> (Specific instances of data or objects that have been accessed)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	public Object addObject() {
		Object newType = new Object();
		getObject().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>object</b> (Specific instances of data or objects that have been accessed),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	public Object getObjectFirstRep() {
		if (getObject().isEmpty()) {
			return addObject();
		}
		return getObject().get(0); 
	}
  
	/**
	 * Block class for child element: <b>SecurityEvent.event</b> (What was done)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name, action type, time, and disposition of the audited event
     * </p> 
	 */
	@Block()	
	public static class Event extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Type/identifier of event",
		formalDefinition="Identifier for a family of the event"
	)
	private CodeableConceptDt myType;
	
	@Child(name="subtype", type=CodeableConceptDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="More specific type/id for the event",
		formalDefinition="Identifier for the category of event"
	)
	private java.util.List<CodeableConceptDt> mySubtype;
	
	@Child(name="action", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Type of action performed during the event",
		formalDefinition="Indicator for type of action performed during the event that generated the audit"
	)
	private BoundCodeDt<SecurityEventActionEnum> myAction;
	
	@Child(name="dateTime", type=InstantDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Time when the event occurred on source",
		formalDefinition="The time when the event occurred on the source"
	)
	private InstantDt myDateTime;
	
	@Child(name="outcome", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Whether the event succeeded or failed",
		formalDefinition="Indicates whether the event succeeded or failed"
	)
	private BoundCodeDt<SecurityEventOutcomeEnum> myOutcome;
	
	@Child(name="outcomeDesc", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Description of the event outcome",
		formalDefinition="A free text description of the outcome of the event"
	)
	private StringDt myOutcomeDesc;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  mySubtype,  myAction,  myDateTime,  myOutcome,  myOutcomeDesc);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, mySubtype, myAction, myDateTime, myOutcome, myOutcomeDesc);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Type/identifier of event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a family of the event
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Type/identifier of event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a family of the event
     * </p> 
	 */
	public Event setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>subtype</b> (More specific type/id for the event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the category of event
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getSubtype() {  
		if (mySubtype == null) {
			mySubtype = new java.util.ArrayList<CodeableConceptDt>();
		}
		return mySubtype;
	}

	/**
	 * Sets the value(s) for <b>subtype</b> (More specific type/id for the event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the category of event
     * </p> 
	 */
	public Event setSubtype(java.util.List<CodeableConceptDt> theValue) {
		mySubtype = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>subtype</b> (More specific type/id for the event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the category of event
     * </p> 
	 */
	public CodeableConceptDt addSubtype() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getSubtype().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>subtype</b> (More specific type/id for the event),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the category of event
     * </p> 
	 */
	public CodeableConceptDt getSubtypeFirstRep() {
		if (getSubtype().isEmpty()) {
			return addSubtype();
		}
		return getSubtype().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>action</b> (Type of action performed during the event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator for type of action performed during the event that generated the audit
     * </p> 
	 */
	public BoundCodeDt<SecurityEventActionEnum> getAction() {  
		if (myAction == null) {
			myAction = new BoundCodeDt<SecurityEventActionEnum>(SecurityEventActionEnum.VALUESET_BINDER);
		}
		return myAction;
	}

	/**
	 * Sets the value(s) for <b>action</b> (Type of action performed during the event)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator for type of action performed during the event that generated the audit
     * </p> 
	 */
	public Event setAction(BoundCodeDt<SecurityEventActionEnum> theValue) {
		myAction = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>action</b> (Type of action performed during the event)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator for type of action performed during the event that generated the audit
     * </p> 
	 */
	public Event setAction(SecurityEventActionEnum theValue) {
		getAction().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dateTime</b> (Time when the event occurred on source).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the event occurred on the source
     * </p> 
	 */
	public InstantDt getDateTime() {  
		if (myDateTime == null) {
			myDateTime = new InstantDt();
		}
		return myDateTime;
	}

	/**
	 * Sets the value(s) for <b>dateTime</b> (Time when the event occurred on source)
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the event occurred on the source
     * </p> 
	 */
	public Event setDateTime(InstantDt theValue) {
		myDateTime = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dateTime</b> (Time when the event occurred on source)
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the event occurred on the source
     * </p> 
	 */
	public Event setDateTime( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDateTime = new InstantDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>dateTime</b> (Time when the event occurred on source)
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the event occurred on the source
     * </p> 
	 */
	public Event setDateTimeWithMillisPrecision( Date theDate) {
		myDateTime = new InstantDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>outcome</b> (Whether the event succeeded or failed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the event succeeded or failed
     * </p> 
	 */
	public BoundCodeDt<SecurityEventOutcomeEnum> getOutcome() {  
		if (myOutcome == null) {
			myOutcome = new BoundCodeDt<SecurityEventOutcomeEnum>(SecurityEventOutcomeEnum.VALUESET_BINDER);
		}
		return myOutcome;
	}

	/**
	 * Sets the value(s) for <b>outcome</b> (Whether the event succeeded or failed)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the event succeeded or failed
     * </p> 
	 */
	public Event setOutcome(BoundCodeDt<SecurityEventOutcomeEnum> theValue) {
		myOutcome = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>outcome</b> (Whether the event succeeded or failed)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the event succeeded or failed
     * </p> 
	 */
	public Event setOutcome(SecurityEventOutcomeEnum theValue) {
		getOutcome().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>outcomeDesc</b> (Description of the event outcome).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text description of the outcome of the event
     * </p> 
	 */
	public StringDt getOutcomeDesc() {  
		if (myOutcomeDesc == null) {
			myOutcomeDesc = new StringDt();
		}
		return myOutcomeDesc;
	}

	/**
	 * Sets the value(s) for <b>outcomeDesc</b> (Description of the event outcome)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text description of the outcome of the event
     * </p> 
	 */
	public Event setOutcomeDesc(StringDt theValue) {
		myOutcomeDesc = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>outcomeDesc</b> (Description of the event outcome)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text description of the outcome of the event
     * </p> 
	 */
	public Event setOutcomeDesc( String theString) {
		myOutcomeDesc = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>SecurityEvent.participant</b> (A person, a hardware device or software process)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Participant extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="role", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="User roles (e.g. local RBAC codes)",
		formalDefinition="Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context"
	)
	private java.util.List<CodeableConceptDt> myRole;
	
	@Child(name="reference", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Direct reference to resource",
		formalDefinition="Direct reference to a resource that identifies the participant"
	)
	private ResourceReferenceDt myReference;
	
	@Child(name="userId", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Unique identifier for the user",
		formalDefinition="Unique identifier for the user actively participating in the event"
	)
	private StringDt myUserId;
	
	@Child(name="altId", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Alternative User id e.g. authentication",
		formalDefinition="Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available"
	)
	private StringDt myAltId;
	
	@Child(name="name", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Human-meaningful name for the user",
		formalDefinition="Human-meaningful name for the user"
	)
	private StringDt myName;
	
	@Child(name="requestor", type=BooleanDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Whether user is initiator",
		formalDefinition="Indicator that the user is or is not the requestor, or initiator, for the event being audited."
	)
	private BooleanDt myRequestor;
	
	@Child(name="media", type=CodingDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Type of media",
		formalDefinition="Type of media involved. Used when the event is about exporting/importing onto media"
	)
	private CodingDt myMedia;
	
	@Child(name="network", order=7, min=0, max=1)	
	@Description(
		shortDefinition="Logical network location for application activity",
		formalDefinition="Logical network location for application activity, if the activity has a network location"
	)
	private ParticipantNetwork myNetwork;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRole,  myReference,  myUserId,  myAltId,  myName,  myRequestor,  myMedia,  myNetwork);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRole, myReference, myUserId, myAltId, myName, myRequestor, myMedia, myNetwork);
	}

	/**
	 * Gets the value(s) for <b>role</b> (User roles (e.g. local RBAC codes)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getRole() {  
		if (myRole == null) {
			myRole = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (User roles (e.g. local RBAC codes))
	 *
     * <p>
     * <b>Definition:</b>
     * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context
     * </p> 
	 */
	public Participant setRole(java.util.List<CodeableConceptDt> theValue) {
		myRole = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>role</b> (User roles (e.g. local RBAC codes))
	 *
     * <p>
     * <b>Definition:</b>
     * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context
     * </p> 
	 */
	public CodeableConceptDt addRole() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getRole().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>role</b> (User roles (e.g. local RBAC codes)),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context
     * </p> 
	 */
	public CodeableConceptDt getRoleFirstRep() {
		if (getRole().isEmpty()) {
			return addRole();
		}
		return getRole().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>reference</b> (Direct reference to resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Direct reference to a resource that identifies the participant
     * </p> 
	 */
	public ResourceReferenceDt getReference() {  
		if (myReference == null) {
			myReference = new ResourceReferenceDt();
		}
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference</b> (Direct reference to resource)
	 *
     * <p>
     * <b>Definition:</b>
     * Direct reference to a resource that identifies the participant
     * </p> 
	 */
	public Participant setReference(ResourceReferenceDt theValue) {
		myReference = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>userId</b> (Unique identifier for the user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for the user actively participating in the event
     * </p> 
	 */
	public StringDt getUserId() {  
		if (myUserId == null) {
			myUserId = new StringDt();
		}
		return myUserId;
	}

	/**
	 * Sets the value(s) for <b>userId</b> (Unique identifier for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for the user actively participating in the event
     * </p> 
	 */
	public Participant setUserId(StringDt theValue) {
		myUserId = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>userId</b> (Unique identifier for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * Unique identifier for the user actively participating in the event
     * </p> 
	 */
	public Participant setUserId( String theString) {
		myUserId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>altId</b> (Alternative User id e.g. authentication).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available
     * </p> 
	 */
	public StringDt getAltId() {  
		if (myAltId == null) {
			myAltId = new StringDt();
		}
		return myAltId;
	}

	/**
	 * Sets the value(s) for <b>altId</b> (Alternative User id e.g. authentication)
	 *
     * <p>
     * <b>Definition:</b>
     * Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available
     * </p> 
	 */
	public Participant setAltId(StringDt theValue) {
		myAltId = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>altId</b> (Alternative User id e.g. authentication)
	 *
     * <p>
     * <b>Definition:</b>
     * Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available
     * </p> 
	 */
	public Participant setAltId( String theString) {
		myAltId = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Human-meaningful name for the user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-meaningful name for the user
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Human-meaningful name for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-meaningful name for the user
     * </p> 
	 */
	public Participant setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Human-meaningful name for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-meaningful name for the user
     * </p> 
	 */
	public Participant setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>requestor</b> (Whether user is initiator).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
     * </p> 
	 */
	public BooleanDt getRequestor() {  
		if (myRequestor == null) {
			myRequestor = new BooleanDt();
		}
		return myRequestor;
	}

	/**
	 * Sets the value(s) for <b>requestor</b> (Whether user is initiator)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
     * </p> 
	 */
	public Participant setRequestor(BooleanDt theValue) {
		myRequestor = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>requestor</b> (Whether user is initiator)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
     * </p> 
	 */
	public Participant setRequestor( boolean theBoolean) {
		myRequestor = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>media</b> (Type of media).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Type of media involved. Used when the event is about exporting/importing onto media
     * </p> 
	 */
	public CodingDt getMedia() {  
		if (myMedia == null) {
			myMedia = new CodingDt();
		}
		return myMedia;
	}

	/**
	 * Sets the value(s) for <b>media</b> (Type of media)
	 *
     * <p>
     * <b>Definition:</b>
     * Type of media involved. Used when the event is about exporting/importing onto media
     * </p> 
	 */
	public Participant setMedia(CodingDt theValue) {
		myMedia = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>network</b> (Logical network location for application activity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Logical network location for application activity, if the activity has a network location
     * </p> 
	 */
	public ParticipantNetwork getNetwork() {  
		if (myNetwork == null) {
			myNetwork = new ParticipantNetwork();
		}
		return myNetwork;
	}

	/**
	 * Sets the value(s) for <b>network</b> (Logical network location for application activity)
	 *
     * <p>
     * <b>Definition:</b>
     * Logical network location for application activity, if the activity has a network location
     * </p> 
	 */
	public Participant setNetwork(ParticipantNetwork theValue) {
		myNetwork = theValue;
		return this;
	}

  

	}

	/**
	 * Block class for child element: <b>SecurityEvent.participant.network</b> (Logical network location for application activity)
	 *
     * <p>
     * <b>Definition:</b>
     * Logical network location for application activity, if the activity has a network location
     * </p> 
	 */
	@Block()	
	public static class ParticipantNetwork extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Identifier for the network access point of the user device",
		formalDefinition="An identifier for the network access point of the user device for the audit event"
	)
	private StringDt myIdentifier;
	
	@Child(name="type", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="The type of network access point",
		formalDefinition="An identifier for the type of network access point that originated the audit event"
	)
	private BoundCodeDt<SecurityEventParticipantNetworkTypeEnum> myType;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myType);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myType);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Identifier for the network access point of the user device).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the network access point of the user device for the audit event
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Identifier for the network access point of the user device)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the network access point of the user device for the audit event
     * </p> 
	 */
	public ParticipantNetwork setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Identifier for the network access point of the user device)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the network access point of the user device for the audit event
     * </p> 
	 */
	public ParticipantNetwork setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (The type of network access point).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the type of network access point that originated the audit event
     * </p> 
	 */
	public BoundCodeDt<SecurityEventParticipantNetworkTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<SecurityEventParticipantNetworkTypeEnum>(SecurityEventParticipantNetworkTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (The type of network access point)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the type of network access point that originated the audit event
     * </p> 
	 */
	public ParticipantNetwork setType(BoundCodeDt<SecurityEventParticipantNetworkTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (The type of network access point)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier for the type of network access point that originated the audit event
     * </p> 
	 */
	public ParticipantNetwork setType(SecurityEventParticipantNetworkTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  

	}



	/**
	 * Block class for child element: <b>SecurityEvent.source</b> (Application systems and processes)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Source extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="site", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Logical source location within the enterprise",
		formalDefinition="Logical source location within the healthcare enterprise network"
	)
	private StringDt mySite;
	
	@Child(name="identifier", type=StringDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="The id of source where event originated",
		formalDefinition="Identifier of the source where the event originated"
	)
	private StringDt myIdentifier;
	
	@Child(name="type", type=CodingDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="The type of source where event originated",
		formalDefinition="Code specifying the type of source where event originated"
	)
	private java.util.List<CodingDt> myType;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySite,  myIdentifier,  myType);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySite, myIdentifier, myType);
	}

	/**
	 * Gets the value(s) for <b>site</b> (Logical source location within the enterprise).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Logical source location within the healthcare enterprise network
     * </p> 
	 */
	public StringDt getSite() {  
		if (mySite == null) {
			mySite = new StringDt();
		}
		return mySite;
	}

	/**
	 * Sets the value(s) for <b>site</b> (Logical source location within the enterprise)
	 *
     * <p>
     * <b>Definition:</b>
     * Logical source location within the healthcare enterprise network
     * </p> 
	 */
	public Source setSite(StringDt theValue) {
		mySite = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>site</b> (Logical source location within the enterprise)
	 *
     * <p>
     * <b>Definition:</b>
     * Logical source location within the healthcare enterprise network
     * </p> 
	 */
	public Source setSite( String theString) {
		mySite = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> (The id of source where event originated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the source where the event originated
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (The id of source where event originated)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the source where the event originated
     * </p> 
	 */
	public Source setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (The id of source where event originated)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier of the source where the event originated
     * </p> 
	 */
	public Source setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (The type of source where event originated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code specifying the type of source where event originated
     * </p> 
	 */
	public java.util.List<CodingDt> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<CodingDt>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (The type of source where event originated)
	 *
     * <p>
     * <b>Definition:</b>
     * Code specifying the type of source where event originated
     * </p> 
	 */
	public Source setType(java.util.List<CodingDt> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>type</b> (The type of source where event originated)
	 *
     * <p>
     * <b>Definition:</b>
     * Code specifying the type of source where event originated
     * </p> 
	 */
	public CodingDt addType() {
		CodingDt newType = new CodingDt();
		getType().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>type</b> (The type of source where event originated),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Code specifying the type of source where event originated
     * </p> 
	 */
	public CodingDt getTypeFirstRep() {
		if (getType().isEmpty()) {
			return addType();
		}
		return getType().get(0); 
	}
  

	}


	/**
	 * Block class for child element: <b>SecurityEvent.object</b> (Specific instances of data or objects that have been accessed)
	 *
     * <p>
     * <b>Definition:</b>
     * Specific instances of data or objects that have been accessed
     * </p> 
	 */
	@Block()	
	public static class Object extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Specific instance of object (e.g. versioned)",
		formalDefinition="Identifies a specific instance of the participant object. The reference should always be version specific"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="reference", order=1, min=0, max=1, type={
		IResource.class	})
	@Description(
		shortDefinition="Specific instance of resource (e.g. versioned)",
		formalDefinition="Identifies a specific instance of the participant object. The reference should always be version specific"
	)
	private ResourceReferenceDt myReference;
	
	@Child(name="type", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Object type being audited",
		formalDefinition="Object type being audited"
	)
	private BoundCodeDt<SecurityEventObjectTypeEnum> myType;
	
	@Child(name="role", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Functional application role of Object",
		formalDefinition="Code representing the functional application role of Participant Object being audited"
	)
	private BoundCodeDt<SecurityEventObjectRoleEnum> myRole;
	
	@Child(name="lifecycle", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Life-cycle stage for the object",
		formalDefinition="Identifier for the data life-cycle stage for the participant object"
	)
	private BoundCodeDt<SecurityEventObjectLifecycleEnum> myLifecycle;
	
	@Child(name="sensitivity", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Policy-defined sensitivity for the object",
		formalDefinition="Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics"
	)
	private BoundCodeableConceptDt<SecurityEventObjectSensitivityEnum> mySensitivity;
	
	@Child(name="name", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Instance-specific descriptor for Object",
		formalDefinition="An instance-specific descriptor of the Participant Object ID audited, such as a person's name"
	)
	private StringDt myName;
	
	@Child(name="description", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Descriptive text",
		formalDefinition="Text that describes the object in more detail"
	)
	private StringDt myDescription;
	
	@Child(name="query", type=Base64BinaryDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Actual query for object",
		formalDefinition="The actual query for a query-type participant object"
	)
	private Base64BinaryDt myQuery;
	
	@Child(name="detail", order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Additional Information about the Object",
		formalDefinition=""
	)
	private java.util.List<ObjectDetail> myDetail;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myReference,  myType,  myRole,  myLifecycle,  mySensitivity,  myName,  myDescription,  myQuery,  myDetail);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myReference, myType, myRole, myLifecycle, mySensitivity, myName, myDescription, myQuery, myDetail);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Specific instance of object (e.g. versioned)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific instance of the participant object. The reference should always be version specific
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Specific instance of object (e.g. versioned))
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific instance of the participant object. The reference should always be version specific
     * </p> 
	 */
	public Object setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Specific instance of object (e.g. versioned))
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific instance of the participant object. The reference should always be version specific
     * </p> 
	 */
	public Object setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Specific instance of object (e.g. versioned))
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific instance of the participant object. The reference should always be version specific
     * </p> 
	 */
	public Object setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reference</b> (Specific instance of resource (e.g. versioned)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific instance of the participant object. The reference should always be version specific
     * </p> 
	 */
	public ResourceReferenceDt getReference() {  
		if (myReference == null) {
			myReference = new ResourceReferenceDt();
		}
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference</b> (Specific instance of resource (e.g. versioned))
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific instance of the participant object. The reference should always be version specific
     * </p> 
	 */
	public Object setReference(ResourceReferenceDt theValue) {
		myReference = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Object type being audited).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Object type being audited
     * </p> 
	 */
	public BoundCodeDt<SecurityEventObjectTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<SecurityEventObjectTypeEnum>(SecurityEventObjectTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Object type being audited)
	 *
     * <p>
     * <b>Definition:</b>
     * Object type being audited
     * </p> 
	 */
	public Object setType(BoundCodeDt<SecurityEventObjectTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Object type being audited)
	 *
     * <p>
     * <b>Definition:</b>
     * Object type being audited
     * </p> 
	 */
	public Object setType(SecurityEventObjectTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>role</b> (Functional application role of Object).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code representing the functional application role of Participant Object being audited
     * </p> 
	 */
	public BoundCodeDt<SecurityEventObjectRoleEnum> getRole() {  
		if (myRole == null) {
			myRole = new BoundCodeDt<SecurityEventObjectRoleEnum>(SecurityEventObjectRoleEnum.VALUESET_BINDER);
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (Functional application role of Object)
	 *
     * <p>
     * <b>Definition:</b>
     * Code representing the functional application role of Participant Object being audited
     * </p> 
	 */
	public Object setRole(BoundCodeDt<SecurityEventObjectRoleEnum> theValue) {
		myRole = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>role</b> (Functional application role of Object)
	 *
     * <p>
     * <b>Definition:</b>
     * Code representing the functional application role of Participant Object being audited
     * </p> 
	 */
	public Object setRole(SecurityEventObjectRoleEnum theValue) {
		getRole().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>lifecycle</b> (Life-cycle stage for the object).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the data life-cycle stage for the participant object
     * </p> 
	 */
	public BoundCodeDt<SecurityEventObjectLifecycleEnum> getLifecycle() {  
		if (myLifecycle == null) {
			myLifecycle = new BoundCodeDt<SecurityEventObjectLifecycleEnum>(SecurityEventObjectLifecycleEnum.VALUESET_BINDER);
		}
		return myLifecycle;
	}

	/**
	 * Sets the value(s) for <b>lifecycle</b> (Life-cycle stage for the object)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the data life-cycle stage for the participant object
     * </p> 
	 */
	public Object setLifecycle(BoundCodeDt<SecurityEventObjectLifecycleEnum> theValue) {
		myLifecycle = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>lifecycle</b> (Life-cycle stage for the object)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for the data life-cycle stage for the participant object
     * </p> 
	 */
	public Object setLifecycle(SecurityEventObjectLifecycleEnum theValue) {
		getLifecycle().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>sensitivity</b> (Policy-defined sensitivity for the object).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics
     * </p> 
	 */
	public BoundCodeableConceptDt<SecurityEventObjectSensitivityEnum> getSensitivity() {  
		if (mySensitivity == null) {
			mySensitivity = new BoundCodeableConceptDt<SecurityEventObjectSensitivityEnum>(SecurityEventObjectSensitivityEnum.VALUESET_BINDER);
		}
		return mySensitivity;
	}

	/**
	 * Sets the value(s) for <b>sensitivity</b> (Policy-defined sensitivity for the object)
	 *
     * <p>
     * <b>Definition:</b>
     * Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics
     * </p> 
	 */
	public Object setSensitivity(BoundCodeableConceptDt<SecurityEventObjectSensitivityEnum> theValue) {
		mySensitivity = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>sensitivity</b> (Policy-defined sensitivity for the object)
	 *
     * <p>
     * <b>Definition:</b>
     * Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics
     * </p> 
	 */
	public Object setSensitivity(SecurityEventObjectSensitivityEnum theValue) {
		getSensitivity().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>name</b> (Instance-specific descriptor for Object).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An instance-specific descriptor of the Participant Object ID audited, such as a person's name
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Instance-specific descriptor for Object)
	 *
     * <p>
     * <b>Definition:</b>
     * An instance-specific descriptor of the Participant Object ID audited, such as a person's name
     * </p> 
	 */
	public Object setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Instance-specific descriptor for Object)
	 *
     * <p>
     * <b>Definition:</b>
     * An instance-specific descriptor of the Participant Object ID audited, such as a person's name
     * </p> 
	 */
	public Object setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (Descriptive text).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Text that describes the object in more detail
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Descriptive text)
	 *
     * <p>
     * <b>Definition:</b>
     * Text that describes the object in more detail
     * </p> 
	 */
	public Object setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Descriptive text)
	 *
     * <p>
     * <b>Definition:</b>
     * Text that describes the object in more detail
     * </p> 
	 */
	public Object setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>query</b> (Actual query for object).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The actual query for a query-type participant object
     * </p> 
	 */
	public Base64BinaryDt getQuery() {  
		if (myQuery == null) {
			myQuery = new Base64BinaryDt();
		}
		return myQuery;
	}

	/**
	 * Sets the value(s) for <b>query</b> (Actual query for object)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual query for a query-type participant object
     * </p> 
	 */
	public Object setQuery(Base64BinaryDt theValue) {
		myQuery = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>query</b> (Actual query for object)
	 *
     * <p>
     * <b>Definition:</b>
     * The actual query for a query-type participant object
     * </p> 
	 */
	public Object setQuery( byte[] theBytes) {
		myQuery = new Base64BinaryDt(theBytes); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>detail</b> (Additional Information about the Object).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ObjectDetail> getDetail() {  
		if (myDetail == null) {
			myDetail = new java.util.ArrayList<ObjectDetail>();
		}
		return myDetail;
	}

	/**
	 * Sets the value(s) for <b>detail</b> (Additional Information about the Object)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Object setDetail(java.util.List<ObjectDetail> theValue) {
		myDetail = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>detail</b> (Additional Information about the Object)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail addDetail() {
		ObjectDetail newType = new ObjectDetail();
		getDetail().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>detail</b> (Additional Information about the Object),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail getDetailFirstRep() {
		if (getDetail().isEmpty()) {
			return addDetail();
		}
		return getDetail().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>SecurityEvent.object.detail</b> (Additional Information about the Object)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class ObjectDetail extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="type", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Name of the property",
		formalDefinition=""
	)
	private StringDt myType;
	
	@Child(name="value", type=Base64BinaryDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Property value",
		formalDefinition=""
	)
	private Base64BinaryDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myValue);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Name of the property).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getType() {  
		if (myType == null) {
			myType = new StringDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Name of the property)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail setType(StringDt theValue) {
		myType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>type</b> (Name of the property)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail setType( String theString) {
		myType = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>value</b> (Property value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Base64BinaryDt getValue() {  
		if (myValue == null) {
			myValue = new Base64BinaryDt();
		}
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value</b> (Property value)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail setValue(Base64BinaryDt theValue) {
		myValue = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>value</b> (Property value)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ObjectDetail setValue( byte[] theBytes) {
		myValue = new Base64BinaryDt(theBytes); 
		return this; 
	}

 

	}





}