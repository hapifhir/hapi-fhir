















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;
import ca.uhn.fhir.model.dstu.valueset.*;

/**
 * HAPI/FHIR <b>Conformance</b> Resource
 * (A conformance statement)
 *
 * <p>
 * <b>Definition:</b>
 * A conformance statement is a set of requirements for a desired implementation or a description of how a target application fulfills those requirements in a particular implementation
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="Conformance", profile="http://hl7.org/fhir/profiles/Conformance", id="conformance")
public class Conformance extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.identifier</b><br/>
	 * </p>
	 */
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.version</b><br/>
	 * </p>
	 */
	public static final String SP_VERSION = "version";

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the conformance statement</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.name</b><br/>
	 * </p>
	 */
	public static final String SP_NAME = "name";

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the conformance statement</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.publisher</b><br/>
	 * </p>
	 */
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the conformance statement</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.description</b><br/>
	 * </p>
	 */
	public static final String SP_DESCRIPTION = "description";

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.status</b><br/>
	 * </p>
	 */
	public static final String SP_STATUS = "status";

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The conformance statement publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Conformance.date</b><br/>
	 * </p>
	 */
	public static final String SP_DATE = "date";

	/**
	 * Search parameter constant for <b>software</b>
	 * <p>
	 * Description: <b>Part of a the name of a software application</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.software.name</b><br/>
	 * </p>
	 */
	public static final String SP_SOFTWARE = "software";

	/**
	 * Search parameter constant for <b>fhirversion</b>
	 * <p>
	 * Description: <b>The version of FHIR</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.version</b><br/>
	 * </p>
	 */
	public static final String SP_FHIRVERSION = "fhirversion";

	/**
	 * Search parameter constant for <b>resource</b>
	 * <p>
	 * Description: <b>Name of a resource mentioned in a conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.rest.resource.type</b><br/>
	 * </p>
	 */
	public static final String SP_RESOURCE = "resource";

	/**
	 * Search parameter constant for <b>event</b>
	 * <p>
	 * Description: <b>Event code in a conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.messaging.event.code</b><br/>
	 * </p>
	 */
	public static final String SP_EVENT = "event";

	/**
	 * Search parameter constant for <b>mode</b>
	 * <p>
	 * Description: <b>Mode - restful (server/client) or messaging (sender/receiver)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.rest.mode</b><br/>
	 * </p>
	 */
	public static final String SP_MODE = "mode";

	/**
	 * Search parameter constant for <b>profile</b>
	 * <p>
	 * Description: <b>A profile id invoked in a conformance statement</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Conformance.rest.resource.profile</b><br/>
	 * </p>
	 */
	public static final String SP_PROFILE = "profile";

	/**
	 * Search parameter constant for <b>format</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.format</b><br/>
	 * </p>
	 */
	public static final String SP_FORMAT = "format";

	/**
	 * Search parameter constant for <b>security</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.rest.security</b><br/>
	 * </p>
	 */
	public static final String SP_SECURITY = "security";

	/**
	 * Search parameter constant for <b>supported-profile</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Conformance.profile</b><br/>
	 * </p>
	 */
	public static final String SP_SUPPORTED_PROFILE = "supported-profile";


	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Logical id to reference this statement",
		formalDefinition="The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)"
	)
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Logical id for this version of the statement",
		formalDefinition="The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Informal name for this conformance statement",
		formalDefinition="A free text natural language name identifying the conformance statement"
	)
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Publishing Organization",
		formalDefinition="Name of Organization publishing this conformance statement"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contacts for Organization",
		formalDefinition="Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc."
	)
	private List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Human description of the conformance statement",
		formalDefinition="A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP"
	)
	private StringDt myDescription;
	
	@Child(name="status", type=CodeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="draft | active | retired",
		formalDefinition="The status of this conformance statement"
	)
	private BoundCodeDt<ConformanceStatementStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="If for testing purposes, not real usage",
		formalDefinition="A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="date", type=DateTimeDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="Publication Date",
		formalDefinition="The date when the conformance statement was published"
	)
	private DateTimeDt myDate;
	
	@Child(name="software", order=9, min=0, max=1)	
	@Description(
		shortDefinition="Software that is covered by this conformance statement",
		formalDefinition="Software that is covered by this conformance statement.  It is used when the profile describes the capabilities of a particular software version, independent of an installation."
	)
	private Software mySoftware;
	
	@Child(name="implementation", order=10, min=0, max=1)	
	@Description(
		shortDefinition="If this describes a specific instance",
		formalDefinition="Identifies a specific implementation instance that is described by the conformance statement - i.e. a particular installation, rather than the capabilities of a software program"
	)
	private Implementation myImplementation;
	
	@Child(name="fhirVersion", type=IdDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="FHIR Version",
		formalDefinition="The version of the FHIR specification on which this conformance statement is based"
	)
	private IdDt myFhirVersion;
	
	@Child(name="acceptUnknown", type=BooleanDt.class, order=12, min=1, max=1)	
	@Description(
		shortDefinition="True if application accepts unknown elements",
		formalDefinition="A flag that indicates whether the application accepts unknown elements as part of a resource."
	)
	private BooleanDt myAcceptUnknown;
	
	@Child(name="format", type=CodeDt.class, order=13, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="formats supported (xml | json | mime type)",
		formalDefinition="A list of the formats supported by this implementation"
	)
	private List<CodeDt> myFormat;
	
	@Child(name="profile", order=14, min=0, max=Child.MAX_UNLIMITED, type={
		Profile.class,
	})
	@Description(
		shortDefinition="Profiles supported by the system",
		formalDefinition="A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of recourses, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile."
	)
	private List<ResourceReferenceDt> myProfile;
	
	@Child(name="rest", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="If the endpoint is a RESTful one",
		formalDefinition="A definition of the restful capabilities of the solution, if any"
	)
	private List<Rest> myRest;
	
	@Child(name="messaging", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="If messaging is supported",
		formalDefinition="A description of the messaging capabilities of the solution"
	)
	private List<Messaging> myMessaging;
	
	@Child(name="document", order=17, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Document definition",
		formalDefinition="A document definition"
	)
	private List<Document> myDocument;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myName,  myPublisher,  myTelecom,  myDescription,  myStatus,  myExperimental,  myDate,  mySoftware,  myImplementation,  myFhirVersion,  myAcceptUnknown,  myFormat,  myProfile,  myRest,  myMessaging,  myDocument);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Logical id to reference this statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Logical id to reference this statement)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
	}


 	/**
	 * Sets the value for <b>identifier</b> (Logical id to reference this statement)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Logical id for this version of the statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Logical id for this version of the statement)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}


 	/**
	 * Sets the value for <b>version</b> (Logical id for this version of the statement)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion( String theString) {
		myVersion = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (Informal name for this conformance statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the conformance statement
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Informal name for this conformance statement)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the conformance statement
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Informal name for this conformance statement)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the conformance statement
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>publisher</b> (Publishing Organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Organization publishing this conformance statement
     * </p> 
	 */
	public StringDt getPublisher() {  
		if (myPublisher == null) {
			myPublisher = new StringDt();
		}
		return myPublisher;
	}

	/**
	 * Sets the value(s) for <b>publisher</b> (Publishing Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Organization publishing this conformance statement
     * </p> 
	 */
	public void setPublisher(StringDt theValue) {
		myPublisher = theValue;
	}


 	/**
	 * Sets the value for <b>publisher</b> (Publishing Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Organization publishing this conformance statement
     * </p> 
	 */
	public void setPublisher( String theString) {
		myPublisher = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>telecom</b> (Contacts for Organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
	 */
	public List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (Contacts for Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (Contacts for Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (Human description of the conformance statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Human description of the conformance statement)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}


 	/**
	 * Sets the value for <b>description</b> (Human description of the conformance statement)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (draft | active | retired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this conformance statement
     * </p> 
	 */
	public BoundCodeDt<ConformanceStatementStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ConformanceStatementStatusEnum>(ConformanceStatementStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this conformance statement
     * </p> 
	 */
	public void setStatus(BoundCodeDt<ConformanceStatementStatusEnum> theValue) {
		myStatus = theValue;
	}


	/**
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this conformance statement
     * </p> 
	 */
	public void setStatus(ConformanceStatementStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>experimental</b> (If for testing purposes, not real usage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {  
		if (myExperimental == null) {
			myExperimental = new BooleanDt();
		}
		return myExperimental;
	}

	/**
	 * Sets the value(s) for <b>experimental</b> (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
	}


 	/**
	 * Sets the value for <b>experimental</b> (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental( Boolean theBoolean) {
		myExperimental = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (Publication Date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the conformance statement was published
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Publication Date)
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the conformance statement was published
     * </p> 
	 */
	public void setDate(DateTimeDt theValue) {
		myDate = theValue;
	}


 	/**
	 * Sets the value for <b>date</b> (Publication Date)
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the conformance statement was published
     * </p> 
	 */
	public void setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
	}

	/**
	 * Sets the value for <b>date</b> (Publication Date)
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the conformance statement was published
     * </p> 
	 */
	public void setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
	}

 
	/**
	 * Gets the value(s) for <b>software</b> (Software that is covered by this conformance statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Software that is covered by this conformance statement.  It is used when the profile describes the capabilities of a particular software version, independent of an installation.
     * </p> 
	 */
	public Software getSoftware() {  
		if (mySoftware == null) {
			mySoftware = new Software();
		}
		return mySoftware;
	}

	/**
	 * Sets the value(s) for <b>software</b> (Software that is covered by this conformance statement)
	 *
     * <p>
     * <b>Definition:</b>
     * Software that is covered by this conformance statement.  It is used when the profile describes the capabilities of a particular software version, independent of an installation.
     * </p> 
	 */
	public void setSoftware(Software theValue) {
		mySoftware = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>implementation</b> (If this describes a specific instance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific implementation instance that is described by the conformance statement - i.e. a particular installation, rather than the capabilities of a software program
     * </p> 
	 */
	public Implementation getImplementation() {  
		if (myImplementation == null) {
			myImplementation = new Implementation();
		}
		return myImplementation;
	}

	/**
	 * Sets the value(s) for <b>implementation</b> (If this describes a specific instance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific implementation instance that is described by the conformance statement - i.e. a particular installation, rather than the capabilities of a software program
     * </p> 
	 */
	public void setImplementation(Implementation theValue) {
		myImplementation = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>fhirVersion</b> (FHIR Version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this conformance statement is based
     * </p> 
	 */
	public IdDt getFhirVersion() {  
		if (myFhirVersion == null) {
			myFhirVersion = new IdDt();
		}
		return myFhirVersion;
	}

	/**
	 * Sets the value(s) for <b>fhirVersion</b> (FHIR Version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this conformance statement is based
     * </p> 
	 */
	public void setFhirVersion(IdDt theValue) {
		myFhirVersion = theValue;
	}


 	/**
	 * Sets the value for <b>fhirVersion</b> (FHIR Version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this conformance statement is based
     * </p> 
	 */
	public void setFhirVersion( String theId) {
		myFhirVersion = new IdDt(theId); 
	}

 
	/**
	 * Gets the value(s) for <b>acceptUnknown</b> (True if application accepts unknown elements).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag that indicates whether the application accepts unknown elements as part of a resource.
     * </p> 
	 */
	public BooleanDt getAcceptUnknown() {  
		if (myAcceptUnknown == null) {
			myAcceptUnknown = new BooleanDt();
		}
		return myAcceptUnknown;
	}

	/**
	 * Sets the value(s) for <b>acceptUnknown</b> (True if application accepts unknown elements)
	 *
     * <p>
     * <b>Definition:</b>
     * A flag that indicates whether the application accepts unknown elements as part of a resource.
     * </p> 
	 */
	public void setAcceptUnknown(BooleanDt theValue) {
		myAcceptUnknown = theValue;
	}


 	/**
	 * Sets the value for <b>acceptUnknown</b> (True if application accepts unknown elements)
	 *
     * <p>
     * <b>Definition:</b>
     * A flag that indicates whether the application accepts unknown elements as part of a resource.
     * </p> 
	 */
	public void setAcceptUnknown( Boolean theBoolean) {
		myAcceptUnknown = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>format</b> (formats supported (xml | json | mime type)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the formats supported by this implementation
     * </p> 
	 */
	public List<CodeDt> getFormat() {  
		if (myFormat == null) {
			myFormat = new ArrayList<CodeDt>();
		}
		return myFormat;
	}

	/**
	 * Sets the value(s) for <b>format</b> (formats supported (xml | json | mime type))
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the formats supported by this implementation
     * </p> 
	 */
	public void setFormat(List<CodeDt> theValue) {
		myFormat = theValue;
	}

	/**
	 * Adds and returns a new value for <b>format</b> (formats supported (xml | json | mime type))
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the formats supported by this implementation
     * </p> 
	 */
	public CodeDt addFormat() {
		CodeDt newType = new CodeDt();
		getFormat().add(newType);
		return newType; 
	}

 	/**
	 * Adds a new value for <b>format</b> (formats supported (xml | json | mime type))
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the formats supported by this implementation
     * </p> 
	 */
	public void addFormat( String theCode) {
		if (myFormat == null) {
			myFormat = new ArrayList<CodeDt>();
		}
		myFormat.add(new CodeDt(theCode)); 
	}

 
	/**
	 * Gets the value(s) for <b>profile</b> (Profiles supported by the system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of recourses, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile.
     * </p> 
	 */
	public List<ResourceReferenceDt> getProfile() {  
		if (myProfile == null) {
			myProfile = new ArrayList<ResourceReferenceDt>();
		}
		return myProfile;
	}

	/**
	 * Sets the value(s) for <b>profile</b> (Profiles supported by the system)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of recourses, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile.
     * </p> 
	 */
	public void setProfile(List<ResourceReferenceDt> theValue) {
		myProfile = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>rest</b> (If the endpoint is a RESTful one).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of the restful capabilities of the solution, if any
     * </p> 
	 */
	public List<Rest> getRest() {  
		if (myRest == null) {
			myRest = new ArrayList<Rest>();
		}
		return myRest;
	}

	/**
	 * Sets the value(s) for <b>rest</b> (If the endpoint is a RESTful one)
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of the restful capabilities of the solution, if any
     * </p> 
	 */
	public void setRest(List<Rest> theValue) {
		myRest = theValue;
	}

	/**
	 * Adds and returns a new value for <b>rest</b> (If the endpoint is a RESTful one)
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of the restful capabilities of the solution, if any
     * </p> 
	 */
	public Rest addRest() {
		Rest newType = new Rest();
		getRest().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>messaging</b> (If messaging is supported).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the messaging capabilities of the solution
     * </p> 
	 */
	public List<Messaging> getMessaging() {  
		if (myMessaging == null) {
			myMessaging = new ArrayList<Messaging>();
		}
		return myMessaging;
	}

	/**
	 * Sets the value(s) for <b>messaging</b> (If messaging is supported)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the messaging capabilities of the solution
     * </p> 
	 */
	public void setMessaging(List<Messaging> theValue) {
		myMessaging = theValue;
	}

	/**
	 * Adds and returns a new value for <b>messaging</b> (If messaging is supported)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the messaging capabilities of the solution
     * </p> 
	 */
	public Messaging addMessaging() {
		Messaging newType = new Messaging();
		getMessaging().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>document</b> (Document definition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A document definition
     * </p> 
	 */
	public List<Document> getDocument() {  
		if (myDocument == null) {
			myDocument = new ArrayList<Document>();
		}
		return myDocument;
	}

	/**
	 * Sets the value(s) for <b>document</b> (Document definition)
	 *
     * <p>
     * <b>Definition:</b>
     * A document definition
     * </p> 
	 */
	public void setDocument(List<Document> theValue) {
		myDocument = theValue;
	}

	/**
	 * Adds and returns a new value for <b>document</b> (Document definition)
	 *
     * <p>
     * <b>Definition:</b>
     * A document definition
     * </p> 
	 */
	public Document addDocument() {
		Document newType = new Document();
		getDocument().add(newType);
		return newType; 
	}

  
	/**
	 * Block class for child element: <b>Conformance.software</b> (Software that is covered by this conformance statement)
	 *
     * <p>
     * <b>Definition:</b>
     * Software that is covered by this conformance statement.  It is used when the profile describes the capabilities of a particular software version, independent of an installation.
     * </p> 
	 */
	@Block(name="Conformance.software")	
	public static class Software extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="A name the software is known by",
		formalDefinition="Name software is known by"
	)
	private StringDt myName;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Version covered by this statement",
		formalDefinition="The version identifier for the software covered by this statement"
	)
	private StringDt myVersion;
	
	@Child(name="releaseDate", type=DateTimeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Date this version released",
		formalDefinition="Date this version of the software released"
	)
	private DateTimeDt myReleaseDate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myVersion,  myReleaseDate);
	}

	/**
	 * Gets the value(s) for <b>name</b> (A name the software is known by).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name software is known by
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (A name the software is known by)
	 *
     * <p>
     * <b>Definition:</b>
     * Name software is known by
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (A name the software is known by)
	 *
     * <p>
     * <b>Definition:</b>
     * Name software is known by
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Version covered by this statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version identifier for the software covered by this statement
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Version covered by this statement)
	 *
     * <p>
     * <b>Definition:</b>
     * The version identifier for the software covered by this statement
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}


 	/**
	 * Sets the value for <b>version</b> (Version covered by this statement)
	 *
     * <p>
     * <b>Definition:</b>
     * The version identifier for the software covered by this statement
     * </p> 
	 */
	public void setVersion( String theString) {
		myVersion = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>releaseDate</b> (Date this version released).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public DateTimeDt getReleaseDate() {  
		if (myReleaseDate == null) {
			myReleaseDate = new DateTimeDt();
		}
		return myReleaseDate;
	}

	/**
	 * Sets the value(s) for <b>releaseDate</b> (Date this version released)
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public void setReleaseDate(DateTimeDt theValue) {
		myReleaseDate = theValue;
	}


 	/**
	 * Sets the value for <b>releaseDate</b> (Date this version released)
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public void setReleaseDateWithSecondsPrecision( Date theDate) {
		myReleaseDate = new DateTimeDt(theDate); 
	}

	/**
	 * Sets the value for <b>releaseDate</b> (Date this version released)
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public void setReleaseDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myReleaseDate = new DateTimeDt(theDate, thePrecision); 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.implementation</b> (If this describes a specific instance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific implementation instance that is described by the conformance statement - i.e. a particular installation, rather than the capabilities of a software program
     * </p> 
	 */
	@Block(name="Conformance.implementation")	
	public static class Implementation extends BaseElement implements IResourceBlock {
	
	@Child(name="description", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Describes this specific instance",
		formalDefinition="Information about the specific installation that this conformance statement relates to"
	)
	private StringDt myDescription;
	
	@Child(name="url", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Base URL for the installation",
		formalDefinition="A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces."
	)
	private UriDt myUrl;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDescription,  myUrl);
	}

	/**
	 * Gets the value(s) for <b>description</b> (Describes this specific instance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the specific installation that this conformance statement relates to
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Describes this specific instance)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the specific installation that this conformance statement relates to
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}


 	/**
	 * Sets the value for <b>description</b> (Describes this specific instance)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the specific installation that this conformance statement relates to
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>url</b> (Base URL for the installation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
     * </p> 
	 */
	public UriDt getUrl() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	/**
	 * Sets the value(s) for <b>url</b> (Base URL for the installation)
	 *
     * <p>
     * <b>Definition:</b>
     * A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
     * </p> 
	 */
	public void setUrl(UriDt theValue) {
		myUrl = theValue;
	}


 	/**
	 * Sets the value for <b>url</b> (Base URL for the installation)
	 *
     * <p>
     * <b>Definition:</b>
     * A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
     * </p> 
	 */
	public void setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.rest</b> (If the endpoint is a RESTful one)
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of the restful capabilities of the solution, if any
     * </p> 
	 */
	@Block(name="Conformance.rest")	
	public static class Rest extends BaseElement implements IResourceBlock {
	
	@Child(name="mode", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="client | server",
		formalDefinition="Identifies whether this portion of the statement is describing ability to initiate or receive restful operations"
	)
	private BoundCodeDt<RestfulConformanceModeEnum> myMode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="General description of implementation",
		formalDefinition="Information about the system's restful capabilities that apply across all applications, such as security"
	)
	private StringDt myDocumentation;
	
	@Child(name="security", order=2, min=0, max=1)	
	@Description(
		shortDefinition="Information about security of implementation",
		formalDefinition="Information about security of implementation"
	)
	private RestSecurity mySecurity;
	
	@Child(name="resource", order=3, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Resource served on the REST interface",
		formalDefinition="A specification of the restful capabilities of the solution for a specific resource type"
	)
	private List<RestResource> myResource;
	
	@Child(name="operation", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="What operations are supported?",
		formalDefinition="A specification of restful operations supported by the system"
	)
	private List<RestOperation> myOperation;
	
	@Child(name="query", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Definition of a named query",
		formalDefinition="Definition of a named query and its parameters and their meaning"
	)
	private List<RestQuery> myQuery;
	
	@Child(name="documentMailbox", type=UriDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="How documents are accepted in /Mailbox",
		formalDefinition="A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose"
	)
	private List<UriDt> myDocumentMailbox;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMode,  myDocumentation,  mySecurity,  myResource,  myOperation,  myQuery,  myDocumentMailbox);
	}

	/**
	 * Gets the value(s) for <b>mode</b> (client | server).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies whether this portion of the statement is describing ability to initiate or receive restful operations
     * </p> 
	 */
	public BoundCodeDt<RestfulConformanceModeEnum> getMode() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<RestfulConformanceModeEnum>(RestfulConformanceModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (client | server)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies whether this portion of the statement is describing ability to initiate or receive restful operations
     * </p> 
	 */
	public void setMode(BoundCodeDt<RestfulConformanceModeEnum> theValue) {
		myMode = theValue;
	}


	/**
	 * Sets the value(s) for <b>mode</b> (client | server)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies whether this portion of the statement is describing ability to initiate or receive restful operations
     * </p> 
	 */
	public void setMode(RestfulConformanceModeEnum theValue) {
		getMode().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (General description of implementation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the system's restful capabilities that apply across all applications, such as security
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (General description of implementation)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the system's restful capabilities that apply across all applications, such as security
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (General description of implementation)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the system's restful capabilities that apply across all applications, such as security
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>security</b> (Information about security of implementation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about security of implementation
     * </p> 
	 */
	public RestSecurity getSecurity() {  
		if (mySecurity == null) {
			mySecurity = new RestSecurity();
		}
		return mySecurity;
	}

	/**
	 * Sets the value(s) for <b>security</b> (Information about security of implementation)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about security of implementation
     * </p> 
	 */
	public void setSecurity(RestSecurity theValue) {
		mySecurity = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>resource</b> (Resource served on the REST interface).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the restful capabilities of the solution for a specific resource type
     * </p> 
	 */
	public List<RestResource> getResource() {  
		if (myResource == null) {
			myResource = new ArrayList<RestResource>();
		}
		return myResource;
	}

	/**
	 * Sets the value(s) for <b>resource</b> (Resource served on the REST interface)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the restful capabilities of the solution for a specific resource type
     * </p> 
	 */
	public void setResource(List<RestResource> theValue) {
		myResource = theValue;
	}

	/**
	 * Adds and returns a new value for <b>resource</b> (Resource served on the REST interface)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the restful capabilities of the solution for a specific resource type
     * </p> 
	 */
	public RestResource addResource() {
		RestResource newType = new RestResource();
		getResource().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>operation</b> (What operations are supported?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public List<RestOperation> getOperation() {  
		if (myOperation == null) {
			myOperation = new ArrayList<RestOperation>();
		}
		return myOperation;
	}

	/**
	 * Sets the value(s) for <b>operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public void setOperation(List<RestOperation> theValue) {
		myOperation = theValue;
	}

	/**
	 * Adds and returns a new value for <b>operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public RestOperation addOperation() {
		RestOperation newType = new RestOperation();
		getOperation().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>query</b> (Definition of a named query).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public List<RestQuery> getQuery() {  
		if (myQuery == null) {
			myQuery = new ArrayList<RestQuery>();
		}
		return myQuery;
	}

	/**
	 * Sets the value(s) for <b>query</b> (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public void setQuery(List<RestQuery> theValue) {
		myQuery = theValue;
	}

	/**
	 * Adds and returns a new value for <b>query</b> (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public RestQuery addQuery() {
		RestQuery newType = new RestQuery();
		getQuery().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>documentMailbox</b> (How documents are accepted in /Mailbox).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose
     * </p> 
	 */
	public List<UriDt> getDocumentMailbox() {  
		if (myDocumentMailbox == null) {
			myDocumentMailbox = new ArrayList<UriDt>();
		}
		return myDocumentMailbox;
	}

	/**
	 * Sets the value(s) for <b>documentMailbox</b> (How documents are accepted in /Mailbox)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose
     * </p> 
	 */
	public void setDocumentMailbox(List<UriDt> theValue) {
		myDocumentMailbox = theValue;
	}

	/**
	 * Adds and returns a new value for <b>documentMailbox</b> (How documents are accepted in /Mailbox)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose
     * </p> 
	 */
	public UriDt addDocumentMailbox() {
		UriDt newType = new UriDt();
		getDocumentMailbox().add(newType);
		return newType; 
	}

 	/**
	 * Adds a new value for <b>documentMailbox</b> (How documents are accepted in /Mailbox)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose
     * </p> 
	 */
	public void addDocumentMailbox( String theUri) {
		if (myDocumentMailbox == null) {
			myDocumentMailbox = new ArrayList<UriDt>();
		}
		myDocumentMailbox.add(new UriDt(theUri)); 
	}

 

	}

	/**
	 * Block class for child element: <b>Conformance.rest.security</b> (Information about security of implementation)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about security of implementation
     * </p> 
	 */
	@Block(name="Conformance.rest.security")	
	public static class RestSecurity extends BaseElement implements IResourceBlock {
	
	@Child(name="cors", type=BooleanDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Adds CORS Headers (http://enable-cors.org/)",
		formalDefinition="Server adds CORS headers when responding to requests - this enables javascript applications to yuse the server"
	)
	private BooleanDt myCors;
	
	@Child(name="service", type=CodeableConceptDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="OAuth | OAuth2 | NTLM | Basic | Kerberos",
		formalDefinition="Types of security services are supported/required by the system"
	)
	private List<BoundCodeableConceptDt<RestfulSecurityServiceEnum>> myService;
	
	@Child(name="description", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="General description of how security works",
		formalDefinition="General description of how security works"
	)
	private StringDt myDescription;
	
	@Child(name="certificate", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Certificates associated with security profiles",
		formalDefinition="Certificates associated with security profiles"
	)
	private List<RestSecurityCertificate> myCertificate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCors,  myService,  myDescription,  myCertificate);
	}

	/**
	 * Gets the value(s) for <b>cors</b> (Adds CORS Headers (http://enable-cors.org/)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to yuse the server
     * </p> 
	 */
	public BooleanDt getCors() {  
		if (myCors == null) {
			myCors = new BooleanDt();
		}
		return myCors;
	}

	/**
	 * Sets the value(s) for <b>cors</b> (Adds CORS Headers (http://enable-cors.org/))
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to yuse the server
     * </p> 
	 */
	public void setCors(BooleanDt theValue) {
		myCors = theValue;
	}


 	/**
	 * Sets the value for <b>cors</b> (Adds CORS Headers (http://enable-cors.org/))
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to yuse the server
     * </p> 
	 */
	public void setCors( Boolean theBoolean) {
		myCors = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public List<BoundCodeableConceptDt<RestfulSecurityServiceEnum>> getService() {  
		if (myService == null) {
			myService = new ArrayList<BoundCodeableConceptDt<RestfulSecurityServiceEnum>>();
		}
		return myService;
	}

	/**
	 * Sets the value(s) for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public void setService(List<BoundCodeableConceptDt<RestfulSecurityServiceEnum>> theValue) {
		myService = theValue;
	}


	/**
	 * Add a value for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public void addService(RestfulSecurityServiceEnum theValue) {
		getService().add(new BoundCodeableConceptDt<RestfulSecurityServiceEnum>(RestfulSecurityServiceEnum.VALUESET_BINDER, theValue));
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public void setService(RestfulSecurityServiceEnum theValue) {
		getService().clear();
		addService(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>description</b> (General description of how security works).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * General description of how security works
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (General description of how security works)
	 *
     * <p>
     * <b>Definition:</b>
     * General description of how security works
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}


 	/**
	 * Sets the value for <b>description</b> (General description of how security works)
	 *
     * <p>
     * <b>Definition:</b>
     * General description of how security works
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>certificate</b> (Certificates associated with security profiles).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Certificates associated with security profiles
     * </p> 
	 */
	public List<RestSecurityCertificate> getCertificate() {  
		if (myCertificate == null) {
			myCertificate = new ArrayList<RestSecurityCertificate>();
		}
		return myCertificate;
	}

	/**
	 * Sets the value(s) for <b>certificate</b> (Certificates associated with security profiles)
	 *
     * <p>
     * <b>Definition:</b>
     * Certificates associated with security profiles
     * </p> 
	 */
	public void setCertificate(List<RestSecurityCertificate> theValue) {
		myCertificate = theValue;
	}

	/**
	 * Adds and returns a new value for <b>certificate</b> (Certificates associated with security profiles)
	 *
     * <p>
     * <b>Definition:</b>
     * Certificates associated with security profiles
     * </p> 
	 */
	public RestSecurityCertificate addCertificate() {
		RestSecurityCertificate newType = new RestSecurityCertificate();
		getCertificate().add(newType);
		return newType; 
	}

  

	}

	/**
	 * Block class for child element: <b>Conformance.rest.security.certificate</b> (Certificates associated with security profiles)
	 *
     * <p>
     * <b>Definition:</b>
     * Certificates associated with security profiles
     * </p> 
	 */
	@Block(name="Conformance.rest.security.certificate")	
	public static class RestSecurityCertificate extends BaseElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Mime type for certificate",
		formalDefinition="Mime type for certificate"
	)
	private CodeDt myType;
	
	@Child(name="blob", type=Base64BinaryDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Actual certificate",
		formalDefinition="Actual certificate"
	)
	private Base64BinaryDt myBlob;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myBlob);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Mime type for certificate).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Mime type for certificate
     * </p> 
	 */
	public CodeDt getType() {  
		if (myType == null) {
			myType = new CodeDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Mime type for certificate)
	 *
     * <p>
     * <b>Definition:</b>
     * Mime type for certificate
     * </p> 
	 */
	public void setType(CodeDt theValue) {
		myType = theValue;
	}


 	/**
	 * Sets the value for <b>type</b> (Mime type for certificate)
	 *
     * <p>
     * <b>Definition:</b>
     * Mime type for certificate
     * </p> 
	 */
	public void setType( String theCode) {
		myType = new CodeDt(theCode); 
	}

 
	/**
	 * Gets the value(s) for <b>blob</b> (Actual certificate).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Actual certificate
     * </p> 
	 */
	public Base64BinaryDt getBlob() {  
		if (myBlob == null) {
			myBlob = new Base64BinaryDt();
		}
		return myBlob;
	}

	/**
	 * Sets the value(s) for <b>blob</b> (Actual certificate)
	 *
     * <p>
     * <b>Definition:</b>
     * Actual certificate
     * </p> 
	 */
	public void setBlob(Base64BinaryDt theValue) {
		myBlob = theValue;
	}


 	/**
	 * Sets the value for <b>blob</b> (Actual certificate)
	 *
     * <p>
     * <b>Definition:</b>
     * Actual certificate
     * </p> 
	 */
	public void setBlob( byte[] theBytes) {
		myBlob = new Base64BinaryDt(theBytes); 
	}

 

	}



	/**
	 * Block class for child element: <b>Conformance.rest.resource</b> (Resource served on the REST interface)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the restful capabilities of the solution for a specific resource type
     * </p> 
	 */
	@Block(name="Conformance.rest.resource")	
	public static class RestResource extends BaseElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="A resource type that is supported",
		formalDefinition="A type of resource exposed via the restful interface"
	)
	private BoundCodeDt<ResourceTypeEnum> myType;
	
	@Child(name="profile", order=1, min=0, max=1, type={
		Profile.class,
	})
	@Description(
		shortDefinition="What structural features are supported",
		formalDefinition="A specification of the profile that describes the solution's support for the resource, including any constraints on cardinality, bindings, lengths or other limitations"
	)
	private ResourceReferenceDt myProfile;
	
	@Child(name="operation", order=2, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="What operations are supported?",
		formalDefinition="Identifies a restful operation supported by the solution"
	)
	private List<RestResourceOperation> myOperation;
	
	@Child(name="readHistory", type=BooleanDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Whether vRead can return past versions",
		formalDefinition="A flag for whether the server is able to return past versions as part of the vRead operation"
	)
	private BooleanDt myReadHistory;
	
	@Child(name="updateCreate", type=BooleanDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="If allows/uses update to a new location",
		formalDefinition="A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server"
	)
	private BooleanDt myUpdateCreate;
	
	@Child(name="searchInclude", type=StringDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="_include values supported by the server",
		formalDefinition="A list of _include values supported by the server"
	)
	private List<StringDt> mySearchInclude;
	
	@Child(name="searchParam", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Additional search params defined",
		formalDefinition="Additional search parameters for implementations to support and/or make use of"
	)
	private List<RestResourceSearchParam> mySearchParam;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myProfile,  myOperation,  myReadHistory,  myUpdateCreate,  mySearchInclude,  mySearchParam);
	}

	/**
	 * Gets the value(s) for <b>type</b> (A resource type that is supported).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A type of resource exposed via the restful interface
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (A resource type that is supported)
	 *
     * <p>
     * <b>Definition:</b>
     * A type of resource exposed via the restful interface
     * </p> 
	 */
	public void setType(BoundCodeDt<ResourceTypeEnum> theValue) {
		myType = theValue;
	}


	/**
	 * Sets the value(s) for <b>type</b> (A resource type that is supported)
	 *
     * <p>
     * <b>Definition:</b>
     * A type of resource exposed via the restful interface
     * </p> 
	 */
	public void setType(ResourceTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>profile</b> (What structural features are supported).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the profile that describes the solution's support for the resource, including any constraints on cardinality, bindings, lengths or other limitations
     * </p> 
	 */
	public ResourceReferenceDt getProfile() {  
		if (myProfile == null) {
			myProfile = new ResourceReferenceDt();
		}
		return myProfile;
	}

	/**
	 * Sets the value(s) for <b>profile</b> (What structural features are supported)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the profile that describes the solution's support for the resource, including any constraints on cardinality, bindings, lengths or other limitations
     * </p> 
	 */
	public void setProfile(ResourceReferenceDt theValue) {
		myProfile = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>operation</b> (What operations are supported?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public List<RestResourceOperation> getOperation() {  
		if (myOperation == null) {
			myOperation = new ArrayList<RestResourceOperation>();
		}
		return myOperation;
	}

	/**
	 * Sets the value(s) for <b>operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public void setOperation(List<RestResourceOperation> theValue) {
		myOperation = theValue;
	}

	/**
	 * Adds and returns a new value for <b>operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public RestResourceOperation addOperation() {
		RestResourceOperation newType = new RestResourceOperation();
		getOperation().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>readHistory</b> (Whether vRead can return past versions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag for whether the server is able to return past versions as part of the vRead operation
     * </p> 
	 */
	public BooleanDt getReadHistory() {  
		if (myReadHistory == null) {
			myReadHistory = new BooleanDt();
		}
		return myReadHistory;
	}

	/**
	 * Sets the value(s) for <b>readHistory</b> (Whether vRead can return past versions)
	 *
     * <p>
     * <b>Definition:</b>
     * A flag for whether the server is able to return past versions as part of the vRead operation
     * </p> 
	 */
	public void setReadHistory(BooleanDt theValue) {
		myReadHistory = theValue;
	}


 	/**
	 * Sets the value for <b>readHistory</b> (Whether vRead can return past versions)
	 *
     * <p>
     * <b>Definition:</b>
     * A flag for whether the server is able to return past versions as part of the vRead operation
     * </p> 
	 */
	public void setReadHistory( Boolean theBoolean) {
		myReadHistory = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>updateCreate</b> (If allows/uses update to a new location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server
     * </p> 
	 */
	public BooleanDt getUpdateCreate() {  
		if (myUpdateCreate == null) {
			myUpdateCreate = new BooleanDt();
		}
		return myUpdateCreate;
	}

	/**
	 * Sets the value(s) for <b>updateCreate</b> (If allows/uses update to a new location)
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server
     * </p> 
	 */
	public void setUpdateCreate(BooleanDt theValue) {
		myUpdateCreate = theValue;
	}


 	/**
	 * Sets the value for <b>updateCreate</b> (If allows/uses update to a new location)
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server
     * </p> 
	 */
	public void setUpdateCreate( Boolean theBoolean) {
		myUpdateCreate = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>searchInclude</b> (_include values supported by the server).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of _include values supported by the server
     * </p> 
	 */
	public List<StringDt> getSearchInclude() {  
		if (mySearchInclude == null) {
			mySearchInclude = new ArrayList<StringDt>();
		}
		return mySearchInclude;
	}

	/**
	 * Sets the value(s) for <b>searchInclude</b> (_include values supported by the server)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of _include values supported by the server
     * </p> 
	 */
	public void setSearchInclude(List<StringDt> theValue) {
		mySearchInclude = theValue;
	}

	/**
	 * Adds and returns a new value for <b>searchInclude</b> (_include values supported by the server)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of _include values supported by the server
     * </p> 
	 */
	public StringDt addSearchInclude() {
		StringDt newType = new StringDt();
		getSearchInclude().add(newType);
		return newType; 
	}

 	/**
	 * Adds a new value for <b>searchInclude</b> (_include values supported by the server)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of _include values supported by the server
     * </p> 
	 */
	public void addSearchInclude( String theString) {
		if (mySearchInclude == null) {
			mySearchInclude = new ArrayList<StringDt>();
		}
		mySearchInclude.add(new StringDt(theString)); 
	}

 
	/**
	 * Gets the value(s) for <b>searchParam</b> (Additional search params defined).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public List<RestResourceSearchParam> getSearchParam() {  
		if (mySearchParam == null) {
			mySearchParam = new ArrayList<RestResourceSearchParam>();
		}
		return mySearchParam;
	}

	/**
	 * Sets the value(s) for <b>searchParam</b> (Additional search params defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public void setSearchParam(List<RestResourceSearchParam> theValue) {
		mySearchParam = theValue;
	}

	/**
	 * Adds and returns a new value for <b>searchParam</b> (Additional search params defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public RestResourceSearchParam addSearchParam() {
		RestResourceSearchParam newType = new RestResourceSearchParam();
		getSearchParam().add(newType);
		return newType; 
	}

  

	}

	/**
	 * Block class for child element: <b>Conformance.rest.resource.operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	@Block(name="Conformance.rest.resource.operation")	
	public static class RestResourceOperation extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="read | vread | update | delete | history-instance | validate | history-type | create | search-type",
		formalDefinition="Coded identifier of the operation, supported by the system resource"
	)
	private BoundCodeDt<RestfulOperationTypeEnum> myCode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Anything special about operation behavior",
		formalDefinition="Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'"
	)
	private StringDt myDocumentation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDocumentation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (read | vread | update | delete | history-instance | validate | history-type | create | search-type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public BoundCodeDt<RestfulOperationTypeEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<RestfulOperationTypeEnum>(RestfulOperationTypeEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (read | vread | update | delete | history-instance | validate | history-type | create | search-type)
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public void setCode(BoundCodeDt<RestfulOperationTypeEnum> theValue) {
		myCode = theValue;
	}


	/**
	 * Sets the value(s) for <b>code</b> (read | vread | update | delete | history-instance | validate | history-type | create | search-type)
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public void setCode(RestfulOperationTypeEnum theValue) {
		getCode().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (Anything special about operation behavior).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Anything special about operation behavior)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (Anything special about operation behavior)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.rest.resource.searchParam</b> (Additional search params defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	@Block(name="Conformance.rest.resource.searchParam")	
	public static class RestResourceSearchParam extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Name of search parameter",
		formalDefinition="The name of the search parameter used in the interface"
	)
	private StringDt myName;
	
	@Child(name="definition", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Source of definition for parameter",
		formalDefinition="A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter"
	)
	private UriDt myDefinition;
	
	@Child(name="type", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="number | date | string | token | reference | composite | quantity",
		formalDefinition="The type of value a search parameter refers to, and how the content is interpreted"
	)
	private BoundCodeDt<SearchParamTypeEnum> myType;
	
	@Child(name="documentation", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Server-specific usage",
		formalDefinition="This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms."
	)
	private StringDt myDocumentation;
	
	@Child(name="target", type=CodeDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Types of resource (if a resource reference)",
		formalDefinition="Types of resource (if a resource is referenced)"
	)
	private List<BoundCodeDt<ResourceTypeEnum>> myTarget;
	
	@Child(name="chain", type=StringDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Chained names supported",
		formalDefinition=""
	)
	private List<StringDt> myChain;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myDefinition,  myType,  myDocumentation,  myTarget,  myChain);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Name of search parameter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the search parameter used in the interface
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name of search parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the search parameter used in the interface
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Name of search parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the search parameter used in the interface
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> (Source of definition for parameter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter
     * </p> 
	 */
	public UriDt getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new UriDt();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (Source of definition for parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter
     * </p> 
	 */
	public void setDefinition(UriDt theValue) {
		myDefinition = theValue;
	}


 	/**
	 * Sets the value for <b>definition</b> (Source of definition for parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter
     * </p> 
	 */
	public void setDefinition( String theUri) {
		myDefinition = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (number | date | string | token | reference | composite | quantity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public BoundCodeDt<SearchParamTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<SearchParamTypeEnum>(SearchParamTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (number | date | string | token | reference | composite | quantity)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public void setType(BoundCodeDt<SearchParamTypeEnum> theValue) {
		myType = theValue;
	}


	/**
	 * Sets the value(s) for <b>type</b> (number | date | string | token | reference | composite | quantity)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public void setType(SearchParamTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (Server-specific usage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Server-specific usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (Server-specific usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>target</b> (Types of resource (if a resource reference)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public List<BoundCodeDt<ResourceTypeEnum>> getTarget() {  
		if (myTarget == null) {
			myTarget = new ArrayList<BoundCodeDt<ResourceTypeEnum>>();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (Types of resource (if a resource reference))
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public void setTarget(List<BoundCodeDt<ResourceTypeEnum>> theValue) {
		myTarget = theValue;
	}


	/**
	 * Add a value for <b>target</b> (Types of resource (if a resource reference))
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public void addTarget(ResourceTypeEnum theValue) {
		getTarget().add(new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER, theValue));
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>target</b> (Types of resource (if a resource reference))
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public void setTarget(ResourceTypeEnum theValue) {
		getTarget().clear();
		addTarget(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>chain</b> (Chained names supported).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public List<StringDt> getChain() {  
		if (myChain == null) {
			myChain = new ArrayList<StringDt>();
		}
		return myChain;
	}

	/**
	 * Sets the value(s) for <b>chain</b> (Chained names supported)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setChain(List<StringDt> theValue) {
		myChain = theValue;
	}

	/**
	 * Adds and returns a new value for <b>chain</b> (Chained names supported)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt addChain() {
		StringDt newType = new StringDt();
		getChain().add(newType);
		return newType; 
	}

 	/**
	 * Adds a new value for <b>chain</b> (Chained names supported)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void addChain( String theString) {
		if (myChain == null) {
			myChain = new ArrayList<StringDt>();
		}
		myChain.add(new StringDt(theString)); 
	}

 

	}



	/**
	 * Block class for child element: <b>Conformance.rest.operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	@Block(name="Conformance.rest.operation")	
	public static class RestOperation extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="transaction | search-system | history-system",
		formalDefinition="A coded identifier of the operation, supported by the system"
	)
	private BoundCodeDt<RestfulOperationSystemEnum> myCode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Anything special about operation behavior",
		formalDefinition="Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented"
	)
	private StringDt myDocumentation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDocumentation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (transaction | search-system | history-system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public BoundCodeDt<RestfulOperationSystemEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<RestfulOperationSystemEnum>(RestfulOperationSystemEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (transaction | search-system | history-system)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public void setCode(BoundCodeDt<RestfulOperationSystemEnum> theValue) {
		myCode = theValue;
	}


	/**
	 * Sets the value(s) for <b>code</b> (transaction | search-system | history-system)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public void setCode(RestfulOperationSystemEnum theValue) {
		getCode().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (Anything special about operation behavior).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Anything special about operation behavior)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (Anything special about operation behavior)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.rest.query</b> (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	@Block(name="Conformance.rest.query")	
	public static class RestQuery extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Special named queries (_query=)",
		formalDefinition="The name of a query, which is used in the _query parameter when the query is called"
	)
	private StringDt myName;
	
	@Child(name="definition", type=UriDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Where query is defined",
		formalDefinition="Identifies the custom query, defined either in FHIR core or another profile"
	)
	private UriDt myDefinition;
	
	@Child(name="documentation", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Additional usage guidance",
		formalDefinition="Additional information about how the query functions in this particular implementation"
	)
	private StringDt myDocumentation;
	
	@Child(name="parameter", type=RestResourceSearchParam.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Parameter for the named query",
		formalDefinition="Identifies which of the parameters for the named query are supported"
	)
	private List<RestResourceSearchParam> myParameter;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myDefinition,  myDocumentation,  myParameter);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Special named queries (_query=)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the _query parameter when the query is called
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Special named queries (_query=))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the _query parameter when the query is called
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}


 	/**
	 * Sets the value for <b>name</b> (Special named queries (_query=))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the _query parameter when the query is called
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> (Where query is defined).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the custom query, defined either in FHIR core or another profile
     * </p> 
	 */
	public UriDt getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new UriDt();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (Where query is defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the custom query, defined either in FHIR core or another profile
     * </p> 
	 */
	public void setDefinition(UriDt theValue) {
		myDefinition = theValue;
	}


 	/**
	 * Sets the value for <b>definition</b> (Where query is defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the custom query, defined either in FHIR core or another profile
     * </p> 
	 */
	public void setDefinition( String theUri) {
		myDefinition = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>documentation</b> (Additional usage guidance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how the query functions in this particular implementation
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Additional usage guidance)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how the query functions in this particular implementation
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (Additional usage guidance)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how the query functions in this particular implementation
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>parameter</b> (Parameter for the named query).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies which of the parameters for the named query are supported
     * </p> 
	 */
	public List<RestResourceSearchParam> getParameter() {  
		if (myParameter == null) {
			myParameter = new ArrayList<RestResourceSearchParam>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> (Parameter for the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies which of the parameters for the named query are supported
     * </p> 
	 */
	public void setParameter(List<RestResourceSearchParam> theValue) {
		myParameter = theValue;
	}

	/**
	 * Adds and returns a new value for <b>parameter</b> (Parameter for the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies which of the parameters for the named query are supported
     * </p> 
	 */
	public RestResourceSearchParam addParameter() {
		RestResourceSearchParam newType = new RestResourceSearchParam();
		getParameter().add(newType);
		return newType; 
	}

  

	}



	/**
	 * Block class for child element: <b>Conformance.messaging</b> (If messaging is supported)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the messaging capabilities of the solution
     * </p> 
	 */
	@Block(name="Conformance.messaging")	
	public static class Messaging extends BaseElement implements IResourceBlock {
	
	@Child(name="endpoint", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Actual endpoint being described",
		formalDefinition="An address to which messages and/or replies are to be sent."
	)
	private UriDt myEndpoint;
	
	@Child(name="reliableCache", type=IntegerDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Reliable Message Cache Length",
		formalDefinition="Length if the receiver's reliable messaging cache (if a receiver) or how long the cache length on the receiver should be (if a sender)"
	)
	private IntegerDt myReliableCache;
	
	@Child(name="documentation", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Messaging interface behavior details",
		formalDefinition="Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner."
	)
	private StringDt myDocumentation;
	
	@Child(name="event", order=3, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Declare support for this event",
		formalDefinition="A description of the solution's support for an event at this end point."
	)
	private List<MessagingEvent> myEvent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myEndpoint,  myReliableCache,  myDocumentation,  myEvent);
	}

	/**
	 * Gets the value(s) for <b>endpoint</b> (Actual endpoint being described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An address to which messages and/or replies are to be sent.
     * </p> 
	 */
	public UriDt getEndpoint() {  
		if (myEndpoint == null) {
			myEndpoint = new UriDt();
		}
		return myEndpoint;
	}

	/**
	 * Sets the value(s) for <b>endpoint</b> (Actual endpoint being described)
	 *
     * <p>
     * <b>Definition:</b>
     * An address to which messages and/or replies are to be sent.
     * </p> 
	 */
	public void setEndpoint(UriDt theValue) {
		myEndpoint = theValue;
	}


 	/**
	 * Sets the value for <b>endpoint</b> (Actual endpoint being described)
	 *
     * <p>
     * <b>Definition:</b>
     * An address to which messages and/or replies are to be sent.
     * </p> 
	 */
	public void setEndpoint( String theUri) {
		myEndpoint = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>reliableCache</b> (Reliable Message Cache Length).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public IntegerDt getReliableCache() {  
		if (myReliableCache == null) {
			myReliableCache = new IntegerDt();
		}
		return myReliableCache;
	}

	/**
	 * Sets the value(s) for <b>reliableCache</b> (Reliable Message Cache Length)
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public void setReliableCache(IntegerDt theValue) {
		myReliableCache = theValue;
	}


 	/**
	 * Sets the value for <b>reliableCache</b> (Reliable Message Cache Length)
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public void setReliableCache( Integer theInteger) {
		myReliableCache = new IntegerDt(theInteger); 
	}

 
	/**
	 * Gets the value(s) for <b>documentation</b> (Messaging interface behavior details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner.
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Messaging interface behavior details)
	 *
     * <p>
     * <b>Definition:</b>
     * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner.
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (Messaging interface behavior details)
	 *
     * <p>
     * <b>Definition:</b>
     * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner.
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>event</b> (Declare support for this event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the solution's support for an event at this end point.
     * </p> 
	 */
	public List<MessagingEvent> getEvent() {  
		if (myEvent == null) {
			myEvent = new ArrayList<MessagingEvent>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (Declare support for this event)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the solution's support for an event at this end point.
     * </p> 
	 */
	public void setEvent(List<MessagingEvent> theValue) {
		myEvent = theValue;
	}

	/**
	 * Adds and returns a new value for <b>event</b> (Declare support for this event)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the solution's support for an event at this end point.
     * </p> 
	 */
	public MessagingEvent addEvent() {
		MessagingEvent newType = new MessagingEvent();
		getEvent().add(newType);
		return newType; 
	}

  

	}

	/**
	 * Block class for child element: <b>Conformance.messaging.event</b> (Declare support for this event)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the solution's support for an event at this end point.
     * </p> 
	 */
	@Block(name="Conformance.messaging.event")	
	public static class MessagingEvent extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodingDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Event type",
		formalDefinition="A coded identifier of a supported messaging event"
	)
	private CodingDt myCode;
	
	@Child(name="category", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Consequence | Currency | Notification",
		formalDefinition="The impact of the content of the message"
	)
	private BoundCodeDt<MessageSignificanceCategoryEnum> myCategory;
	
	@Child(name="mode", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="sender | receiver",
		formalDefinition="The mode of this event declaration - whether application is sender or receiver"
	)
	private BoundCodeDt<ConformanceEventModeEnum> myMode;
	
	@Child(name="protocol", type=CodingDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="http | ftp | mllp +",
		formalDefinition="A list of the messaging transport protocol(s) identifiers, supported by this endpoint"
	)
	private List<CodingDt> myProtocol;
	
	@Child(name="focus", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Resource that's focus of message",
		formalDefinition="A resource associated with the event.  This is the resource that defines the event."
	)
	private BoundCodeDt<ResourceTypeEnum> myFocus;
	
	@Child(name="request", order=5, min=1, max=1, type={
		Profile.class,
	})
	@Description(
		shortDefinition="Profile that describes the request",
		formalDefinition="Information about the request for this event"
	)
	private ResourceReferenceDt myRequest;
	
	@Child(name="response", order=6, min=1, max=1, type={
		Profile.class,
	})
	@Description(
		shortDefinition="Profile that describes the response",
		formalDefinition="Information about the response for this event"
	)
	private ResourceReferenceDt myResponse;
	
	@Child(name="documentation", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Endpoint-specific event documentation",
		formalDefinition="Guidance on how this event is handled, such as internal system trigger points, business rules, etc."
	)
	private StringDt myDocumentation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myCategory,  myMode,  myProtocol,  myFocus,  myRequest,  myResponse,  myDocumentation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Event type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of a supported messaging event
     * </p> 
	 */
	public CodingDt getCode() {  
		if (myCode == null) {
			myCode = new CodingDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Event type)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of a supported messaging event
     * </p> 
	 */
	public void setCode(CodingDt theValue) {
		myCode = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>category</b> (Consequence | Currency | Notification).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The impact of the content of the message
     * </p> 
	 */
	public BoundCodeDt<MessageSignificanceCategoryEnum> getCategory() {  
		if (myCategory == null) {
			myCategory = new BoundCodeDt<MessageSignificanceCategoryEnum>(MessageSignificanceCategoryEnum.VALUESET_BINDER);
		}
		return myCategory;
	}

	/**
	 * Sets the value(s) for <b>category</b> (Consequence | Currency | Notification)
	 *
     * <p>
     * <b>Definition:</b>
     * The impact of the content of the message
     * </p> 
	 */
	public void setCategory(BoundCodeDt<MessageSignificanceCategoryEnum> theValue) {
		myCategory = theValue;
	}


	/**
	 * Sets the value(s) for <b>category</b> (Consequence | Currency | Notification)
	 *
     * <p>
     * <b>Definition:</b>
     * The impact of the content of the message
     * </p> 
	 */
	public void setCategory(MessageSignificanceCategoryEnum theValue) {
		getCategory().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>mode</b> (sender | receiver).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mode of this event declaration - whether application is sender or receiver
     * </p> 
	 */
	public BoundCodeDt<ConformanceEventModeEnum> getMode() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<ConformanceEventModeEnum>(ConformanceEventModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (sender | receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * The mode of this event declaration - whether application is sender or receiver
     * </p> 
	 */
	public void setMode(BoundCodeDt<ConformanceEventModeEnum> theValue) {
		myMode = theValue;
	}


	/**
	 * Sets the value(s) for <b>mode</b> (sender | receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * The mode of this event declaration - whether application is sender or receiver
     * </p> 
	 */
	public void setMode(ConformanceEventModeEnum theValue) {
		getMode().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>protocol</b> (http | ftp | mllp +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the messaging transport protocol(s) identifiers, supported by this endpoint
     * </p> 
	 */
	public List<CodingDt> getProtocol() {  
		if (myProtocol == null) {
			myProtocol = new ArrayList<CodingDt>();
		}
		return myProtocol;
	}

	/**
	 * Sets the value(s) for <b>protocol</b> (http | ftp | mllp +)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the messaging transport protocol(s) identifiers, supported by this endpoint
     * </p> 
	 */
	public void setProtocol(List<CodingDt> theValue) {
		myProtocol = theValue;
	}

	/**
	 * Adds and returns a new value for <b>protocol</b> (http | ftp | mllp +)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the messaging transport protocol(s) identifiers, supported by this endpoint
     * </p> 
	 */
	public CodingDt addProtocol() {
		CodingDt newType = new CodingDt();
		getProtocol().add(newType);
		return newType; 
	}

  
	/**
	 * Gets the value(s) for <b>focus</b> (Resource that's focus of message).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A resource associated with the event.  This is the resource that defines the event.
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> getFocus() {  
		if (myFocus == null) {
			myFocus = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER);
		}
		return myFocus;
	}

	/**
	 * Sets the value(s) for <b>focus</b> (Resource that's focus of message)
	 *
     * <p>
     * <b>Definition:</b>
     * A resource associated with the event.  This is the resource that defines the event.
     * </p> 
	 */
	public void setFocus(BoundCodeDt<ResourceTypeEnum> theValue) {
		myFocus = theValue;
	}


	/**
	 * Sets the value(s) for <b>focus</b> (Resource that's focus of message)
	 *
     * <p>
     * <b>Definition:</b>
     * A resource associated with the event.  This is the resource that defines the event.
     * </p> 
	 */
	public void setFocus(ResourceTypeEnum theValue) {
		getFocus().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>request</b> (Profile that describes the request).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the request for this event
     * </p> 
	 */
	public ResourceReferenceDt getRequest() {  
		if (myRequest == null) {
			myRequest = new ResourceReferenceDt();
		}
		return myRequest;
	}

	/**
	 * Sets the value(s) for <b>request</b> (Profile that describes the request)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the request for this event
     * </p> 
	 */
	public void setRequest(ResourceReferenceDt theValue) {
		myRequest = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>response</b> (Profile that describes the response).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the response for this event
     * </p> 
	 */
	public ResourceReferenceDt getResponse() {  
		if (myResponse == null) {
			myResponse = new ResourceReferenceDt();
		}
		return myResponse;
	}

	/**
	 * Sets the value(s) for <b>response</b> (Profile that describes the response)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the response for this event
     * </p> 
	 */
	public void setResponse(ResourceReferenceDt theValue) {
		myResponse = theValue;
	}


  
	/**
	 * Gets the value(s) for <b>documentation</b> (Endpoint-specific event documentation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Endpoint-specific event documentation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (Endpoint-specific event documentation)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 

	}



	/**
	 * Block class for child element: <b>Conformance.document</b> (Document definition)
	 *
     * <p>
     * <b>Definition:</b>
     * A document definition
     * </p> 
	 */
	@Block(name="Conformance.document")	
	public static class Document extends BaseElement implements IResourceBlock {
	
	@Child(name="mode", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="producer | consumer",
		formalDefinition="Mode of this document declaration - whether application is producer or consumer"
	)
	private BoundCodeDt<DocumentModeEnum> myMode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Description of document support",
		formalDefinition="A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc."
	)
	private StringDt myDocumentation;
	
	@Child(name="profile", order=2, min=1, max=1, type={
		Profile.class,
	})
	@Description(
		shortDefinition="Constraint on a resource used in the document",
		formalDefinition="A constraint on a resource used in the document"
	)
	private ResourceReferenceDt myProfile;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMode,  myDocumentation,  myProfile);
	}

	/**
	 * Gets the value(s) for <b>mode</b> (producer | consumer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Mode of this document declaration - whether application is producer or consumer
     * </p> 
	 */
	public BoundCodeDt<DocumentModeEnum> getMode() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<DocumentModeEnum>(DocumentModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (producer | consumer)
	 *
     * <p>
     * <b>Definition:</b>
     * Mode of this document declaration - whether application is producer or consumer
     * </p> 
	 */
	public void setMode(BoundCodeDt<DocumentModeEnum> theValue) {
		myMode = theValue;
	}


	/**
	 * Sets the value(s) for <b>mode</b> (producer | consumer)
	 *
     * <p>
     * <b>Definition:</b>
     * Mode of this document declaration - whether application is producer or consumer
     * </p> 
	 */
	public void setMode(DocumentModeEnum theValue) {
		getMode().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (Description of document support).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc.
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Description of document support)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc.
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}


 	/**
	 * Sets the value for <b>documentation</b> (Description of document support)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc.
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>profile</b> (Constraint on a resource used in the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint on a resource used in the document
     * </p> 
	 */
	public ResourceReferenceDt getProfile() {  
		if (myProfile == null) {
			myProfile = new ResourceReferenceDt();
		}
		return myProfile;
	}

	/**
	 * Sets the value(s) for <b>profile</b> (Constraint on a resource used in the document)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint on a resource used in the document
     * </p> 
	 */
	public void setProfile(ResourceReferenceDt theValue) {
		myProfile = theValue;
	}


  

	}




}