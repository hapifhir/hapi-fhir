















package ca.uhn.fhir.model.dstu.resource;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.composite.*;

/**
 * HAPI/FHIR <b>Profile</b> Resource
 * (Resource Profile)
 *
 * <p>
 * <b>Definition:</b>
 * A Resource Profile - a statement of use of one or more FHIR Resources.  It may include constraints on Resources and Data Types, Terminology Binding Statements and Extension Definitions
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@ResourceDef(name="Profile")
public class Profile extends BaseElement implements IResource {

	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=0, max=1)	
	private StringDt myDescription;
	
	@Child(name="code", type=CodingDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodingDt> myCode;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	private CodeDt myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	private BooleanDt myExperimental;
	
	@Child(name="date", type=DateTimeDt.class, order=9, min=0, max=1)	
	private DateTimeDt myDate;
	
	@Child(name="requirements", type=StringDt.class, order=10, min=0, max=1)	
	private StringDt myRequirements;
	
	@Child(name="fhirVersion", type=IdDt.class, order=11, min=0, max=1)	
	private IdDt myFhirVersion;
	
	@Child(name="mapping", order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<Mapping> myMapping;
	
	@Child(name="structure", order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<Structure> myStructure;
	
	@Child(name="extensionDefn", order=14, min=0, max=Child.MAX_UNLIMITED)	
	private List<ExtensionDefn> myExtensionDefn;
	
	@Child(name="query", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<Query> myQuery;
	
	/**
	 * Gets the value(s) for <b>identifier</b> (Logical id to reference this profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Logical id to reference this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>identifier</b> (Logical id to reference this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public void setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>version</b> (Logical id for this version of the profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Logical id for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>version</b> (Logical id for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public void setVersion( String theString) {
		myVersion = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>name</b> (Informal name for this profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Informal name for this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Informal name for this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>publisher</b> (Name of the publisher (Organization or individual)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public StringDt getPublisher() {  
		if (myPublisher == null) {
			myPublisher = new StringDt();
		}
		return myPublisher;
	}

	/**
	 * Sets the value(s) for <b>publisher</b> (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public void setPublisher(StringDt theValue) {
		myPublisher = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>publisher</b> (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public void setPublisher( String theString) {
		myPublisher = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>telecom</b> (Contact information of the publisher).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public void setTelecom(List<ContactDt> theValue) {
		myTelecom = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>description</b> (Natural language description of the profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Natural language description of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>description</b> (Natural language description of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>code</b> (Assist with indexing and finding).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public List<CodingDt> getCode() {  
		if (myCode == null) {
			myCode = new ArrayList<CodingDt>();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Assist with indexing and finding)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public void setCode(List<CodingDt> theValue) {
		myCode = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>status</b> (draft | active | retired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public CodeDt getStatus() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public void setStatus(CodeDt theValue) {
		myStatus = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>experimental</b> (If for testing purposes, not real usage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
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
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public void setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>date</b> (Date for this version of the profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public void setDate(DateTimeDt theValue) {
		myDate = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>date</b> (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public void setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
	}
 
	/**
	 * Gets the value(s) for <b>requirements</b> (Scope and Usage this profile is for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public StringDt getRequirements() {  
		if (myRequirements == null) {
			myRequirements = new StringDt();
		}
		return myRequirements;
	}

	/**
	 * Sets the value(s) for <b>requirements</b> (Scope and Usage this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public void setRequirements(StringDt theValue) {
		myRequirements = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>requirements</b> (Scope and Usage this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public void setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>fhirVersion</b> (FHIR Version this profile targets).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public IdDt getFhirVersion() {  
		if (myFhirVersion == null) {
			myFhirVersion = new IdDt();
		}
		return myFhirVersion;
	}

	/**
	 * Sets the value(s) for <b>fhirVersion</b> (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public void setFhirVersion(IdDt theValue) {
		myFhirVersion = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>fhirVersion</b> (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public void setFhirVersion( String theId) {
		myFhirVersion = new IdDt(theId); 
	}
 
	/**
	 * Gets the value(s) for <b>mapping</b> (External specification that the content is mapped to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public List<Mapping> getMapping() {  
		if (myMapping == null) {
			myMapping = new ArrayList<Mapping>();
		}
		return myMapping;
	}

	/**
	 * Sets the value(s) for <b>mapping</b> (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public void setMapping(List<Mapping> theValue) {
		myMapping = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>structure</b> (A constraint on a resource or a data type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public List<Structure> getStructure() {  
		if (myStructure == null) {
			myStructure = new ArrayList<Structure>();
		}
		return myStructure;
	}

	/**
	 * Sets the value(s) for <b>structure</b> (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public void setStructure(List<Structure> theValue) {
		myStructure = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>extensionDefn</b> (Definition of an extension).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public List<ExtensionDefn> getExtensionDefn() {  
		if (myExtensionDefn == null) {
			myExtensionDefn = new ArrayList<ExtensionDefn>();
		}
		return myExtensionDefn;
	}

	/**
	 * Sets the value(s) for <b>extensionDefn</b> (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public void setExtensionDefn(List<ExtensionDefn> theValue) {
		myExtensionDefn = theValue;
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
	public List<Query> getQuery() {  
		if (myQuery == null) {
			myQuery = new ArrayList<Query>();
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
	public void setQuery(List<Query> theValue) {
		myQuery = theValue;
	}
	
 
	/**
	 * Block class for child element: <b>Profile.mapping</b> (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	@Block(name="Profile.mapping")	
	public static class Mapping extends BaseElement implements IResourceBlock {
	
	@Child(name="identity", type=IdDt.class, order=0, min=1, max=1)	
	private IdDt myIdentity;
	
	@Child(name="uri", type=UriDt.class, order=1, min=0, max=1)	
	private UriDt myUri;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="comments", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myComments;
	
	/**
	 * Gets the value(s) for <b>identity</b> (Internal id when this mapping is used).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public IdDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new IdDt();
		}
		return myIdentity;
	}

	/**
	 * Sets the value(s) for <b>identity</b> (Internal id when this mapping is used)
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public void setIdentity(IdDt theValue) {
		myIdentity = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>identity</b> (Internal id when this mapping is used)
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public void setIdentity( String theId) {
		myIdentity = new IdDt(theId); 
	}
 
	/**
	 * Gets the value(s) for <b>uri</b> (Identifies what this mapping refers to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public UriDt getUri() {  
		if (myUri == null) {
			myUri = new UriDt();
		}
		return myUri;
	}

	/**
	 * Sets the value(s) for <b>uri</b> (Identifies what this mapping refers to)
	 *
     * <p>
     * <b>Definition:</b>
     * A URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public void setUri(UriDt theValue) {
		myUri = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>name</b> (Names what this mapping refers to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Names what this mapping refers to)
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Names what this mapping refers to)
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>comments</b> (Versions, Issues, Scope limitations etc).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public StringDt getComments() {  
		if (myComments == null) {
			myComments = new StringDt();
		}
		return myComments;
	}

	/**
	 * Sets the value(s) for <b>comments</b> (Versions, Issues, Scope limitations etc)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public void setComments(StringDt theValue) {
		myComments = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>comments</b> (Versions, Issues, Scope limitations etc)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public void setComments( String theString) {
		myComments = new StringDt(theString); 
	}
 

	}


	/**
	 * Block class for child element: <b>Profile.structure</b> (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	@Block(name="Profile.structure")	
	public static class Structure extends BaseElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	private CodeDt myType;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="publish", type=BooleanDt.class, order=2, min=0, max=1)	
	private BooleanDt myPublish;
	
	@Child(name="purpose", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myPurpose;
	
	@Child(name="element", order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<StructureElement> myElement;
	
	@Child(name="searchParam", order=5, min=0, max=Child.MAX_UNLIMITED)	
	private List<StructureSearchParam> mySearchParam;
	
	/**
	 * Gets the value(s) for <b>type</b> (The Resource or Data Type being described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Resource or Data type being described
     * </p> 
	 */
	public CodeDt getType() {  
		if (myType == null) {
			myType = new CodeDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (The Resource or Data Type being described)
	 *
     * <p>
     * <b>Definition:</b>
     * The Resource or Data type being described
     * </p> 
	 */
	public void setType(CodeDt theValue) {
		myType = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>name</b> (Name for this particular structure (reference target)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this resource constraint statement (to refer to it from other resource constraints - from Profile.structure.element.definition.type.profile)
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name for this particular structure (reference target))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this resource constraint statement (to refer to it from other resource constraints - from Profile.structure.element.definition.type.profile)
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Name for this particular structure (reference target))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this resource constraint statement (to refer to it from other resource constraints - from Profile.structure.element.definition.type.profile)
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>publish</b> (This definition is published (i.e. for validation)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This definition of a profile on a structure is published as a formal statement. Some structural definitions might be defined purely for internal use within the profile, and not intended to be used outside that context
     * </p> 
	 */
	public BooleanDt getPublish() {  
		if (myPublish == null) {
			myPublish = new BooleanDt();
		}
		return myPublish;
	}

	/**
	 * Sets the value(s) for <b>publish</b> (This definition is published (i.e. for validation))
	 *
     * <p>
     * <b>Definition:</b>
     * This definition of a profile on a structure is published as a formal statement. Some structural definitions might be defined purely for internal use within the profile, and not intended to be used outside that context
     * </p> 
	 */
	public void setPublish(BooleanDt theValue) {
		myPublish = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>purpose</b> (Human summary: why describe this resource?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human summary: why describe this resource?
     * </p> 
	 */
	public StringDt getPurpose() {  
		if (myPurpose == null) {
			myPurpose = new StringDt();
		}
		return myPurpose;
	}

	/**
	 * Sets the value(s) for <b>purpose</b> (Human summary: why describe this resource?)
	 *
     * <p>
     * <b>Definition:</b>
     * Human summary: why describe this resource?
     * </p> 
	 */
	public void setPurpose(StringDt theValue) {
		myPurpose = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>purpose</b> (Human summary: why describe this resource?)
	 *
     * <p>
     * <b>Definition:</b>
     * Human summary: why describe this resource?
     * </p> 
	 */
	public void setPurpose( String theString) {
		myPurpose = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>element</b> (Definition of elements in the resource (if no profile)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	public List<StructureElement> getElement() {  
		if (myElement == null) {
			myElement = new ArrayList<StructureElement>();
		}
		return myElement;
	}

	/**
	 * Sets the value(s) for <b>element</b> (Definition of elements in the resource (if no profile))
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	public void setElement(List<StructureElement> theValue) {
		myElement = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>searchParam</b> (Search params defined).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public List<StructureSearchParam> getSearchParam() {  
		if (mySearchParam == null) {
			mySearchParam = new ArrayList<StructureSearchParam>();
		}
		return mySearchParam;
	}

	/**
	 * Sets the value(s) for <b>searchParam</b> (Search params defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public void setSearchParam(List<StructureSearchParam> theValue) {
		mySearchParam = theValue;
	}
	
 

	}

	/**
	 * Block class for child element: <b>Profile.structure.element</b> (Definition of elements in the resource (if no profile))
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	@Block(name="Profile.structure.element")	
	public static class StructureElement extends BaseElement implements IResourceBlock {
	
	@Child(name="path", type=StringDt.class, order=0, min=1, max=1)	
	private StringDt myPath;
	
	@Child(name="representation", type=CodeDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeDt> myRepresentation;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="slicing", order=3, min=0, max=1)	
	private StructureElementSlicing mySlicing;
	
	@Child(name="definition", order=4, min=0, max=1)	
	private StructureElementDefinition myDefinition;
	
	/**
	 * Gets the value(s) for <b>path</b> (The path of the element (see the formal definitions)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource
     * </p> 
	 */
	public StringDt getPath() {  
		if (myPath == null) {
			myPath = new StringDt();
		}
		return myPath;
	}

	/**
	 * Sets the value(s) for <b>path</b> (The path of the element (see the formal definitions))
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource
     * </p> 
	 */
	public void setPath(StringDt theValue) {
		myPath = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>path</b> (The path of the element (see the formal definitions))
	 *
     * <p>
     * <b>Definition:</b>
     * The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name of the resource
     * </p> 
	 */
	public void setPath( String theString) {
		myPath = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>representation</b> (How this element is represented in instances).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public List<CodeDt> getRepresentation() {  
		if (myRepresentation == null) {
			myRepresentation = new ArrayList<CodeDt>();
		}
		return myRepresentation;
	}

	/**
	 * Sets the value(s) for <b>representation</b> (How this element is represented in instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case
     * </p> 
	 */
	public void setRepresentation(List<CodeDt> theValue) {
		myRepresentation = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>name</b> (Name for this particular element definition (reference target)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using Profile.structure.element.definition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name for this particular element definition (reference target))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using Profile.structure.element.definition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Name for this particular element definition (reference target))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of this element definition (to refer to it from other element definitions using Profile.structure.element.definition.nameReference). This is a unique name referring to a specific set of constraints applied to this element. One use of this is to provide a name to different slices of the same element
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>slicing</b> (This element is sliced - slices follow).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	public StructureElementSlicing getSlicing() {  
		if (mySlicing == null) {
			mySlicing = new StructureElementSlicing();
		}
		return mySlicing;
	}

	/**
	 * Sets the value(s) for <b>slicing</b> (This element is sliced - slices follow)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	public void setSlicing(StructureElementSlicing theValue) {
		mySlicing = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>definition</b> (More specific definition of the element ).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the content of the element to provide a more specific definition than that contained for the element in the base resource
     * </p> 
	 */
	public StructureElementDefinition getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new StructureElementDefinition();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (More specific definition of the element )
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the content of the element to provide a more specific definition than that contained for the element in the base resource
     * </p> 
	 */
	public void setDefinition(StructureElementDefinition theValue) {
		myDefinition = theValue;
	}
	
 

	}

	/**
	 * Block class for child element: <b>Profile.structure.element.slicing</b> (This element is sliced - slices follow)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that the element is sliced into a set of alternative definitions (there are multiple definitions on a single element in the base resource). The set of slices is any elements that come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path terminates the set)
     * </p> 
	 */
	@Block(name="Profile.structure.element.slicing")	
	public static class StructureElementSlicing extends BaseElement implements IResourceBlock {
	
	@Child(name="discriminator", type=IdDt.class, order=0, min=1, max=1)	
	private IdDt myDiscriminator;
	
	@Child(name="ordered", type=BooleanDt.class, order=1, min=1, max=1)	
	private BooleanDt myOrdered;
	
	@Child(name="rules", type=CodeDt.class, order=2, min=1, max=1)	
	private CodeDt myRules;
	
	/**
	 * Gets the value(s) for <b>discriminator</b> (Element that used to distinguish the slices).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child element is used to discriminate between the slices when processing an instance. The value of the child element in the instance SHALL completely distinguish which slice the element in the resource matches based on the allowed values for that element in each of the slices
     * </p> 
	 */
	public IdDt getDiscriminator() {  
		if (myDiscriminator == null) {
			myDiscriminator = new IdDt();
		}
		return myDiscriminator;
	}

	/**
	 * Sets the value(s) for <b>discriminator</b> (Element that used to distinguish the slices)
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child element is used to discriminate between the slices when processing an instance. The value of the child element in the instance SHALL completely distinguish which slice the element in the resource matches based on the allowed values for that element in each of the slices
     * </p> 
	 */
	public void setDiscriminator(IdDt theValue) {
		myDiscriminator = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>discriminator</b> (Element that used to distinguish the slices)
	 *
     * <p>
     * <b>Definition:</b>
     * Designates which child element is used to discriminate between the slices when processing an instance. The value of the child element in the instance SHALL completely distinguish which slice the element in the resource matches based on the allowed values for that element in each of the slices
     * </p> 
	 */
	public void setDiscriminator( String theId) {
		myDiscriminator = new IdDt(theId); 
	}
 
	/**
	 * Gets the value(s) for <b>ordered</b> (If elements must be in same order as slices).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the matching elements have to occur in the same order as defined in the profile
     * </p> 
	 */
	public BooleanDt getOrdered() {  
		if (myOrdered == null) {
			myOrdered = new BooleanDt();
		}
		return myOrdered;
	}

	/**
	 * Sets the value(s) for <b>ordered</b> (If elements must be in same order as slices)
	 *
     * <p>
     * <b>Definition:</b>
     * If the matching elements have to occur in the same order as defined in the profile
     * </p> 
	 */
	public void setOrdered(BooleanDt theValue) {
		myOrdered = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>rules</b> (closed | open | openAtEnd).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end
     * </p> 
	 */
	public CodeDt getRules() {  
		if (myRules == null) {
			myRules = new CodeDt();
		}
		return myRules;
	}

	/**
	 * Sets the value(s) for <b>rules</b> (closed | open | openAtEnd)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that additional slices are only allowed at the end
     * </p> 
	 */
	public void setRules(CodeDt theValue) {
		myRules = theValue;
	}
	
 

	}


	/**
	 * Block class for child element: <b>Profile.structure.element.definition</b> (More specific definition of the element )
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the content of the element to provide a more specific definition than that contained for the element in the base resource
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition")	
	public static class StructureElementDefinition extends BaseElement implements IResourceBlock {
	
	@Child(name="short", type=StringDt.class, order=0, min=1, max=1)	
	private StringDt myShort;
	
	@Child(name="formal", type=StringDt.class, order=1, min=1, max=1)	
	private StringDt myFormal;
	
	@Child(name="comments", type=StringDt.class, order=2, min=0, max=1)	
	private StringDt myComments;
	
	@Child(name="requirements", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myRequirements;
	
	@Child(name="synonym", type=StringDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<StringDt> mySynonym;
	
	@Child(name="min", type=IntegerDt.class, order=5, min=1, max=1)	
	private IntegerDt myMin;
	
	@Child(name="max", type=StringDt.class, order=6, min=1, max=1)	
	private StringDt myMax;
	
	@Child(name="type", order=7, min=0, max=Child.MAX_UNLIMITED)	
	private List<StructureElementDefinitionType> myType;
	
	@Child(name="nameReference", type=StringDt.class, order=8, min=0, max=1)	
	private StringDt myNameReference;
	
	@Child(name="value", type=IDatatype.class, order=9, min=0, max=1)	
	private IDatatype myValue;
	
	@Child(name="example", type=IDatatype.class, order=10, min=0, max=1)	
	private IDatatype myExample;
	
	@Child(name="maxLength", type=IntegerDt.class, order=11, min=0, max=1)	
	private IntegerDt myMaxLength;
	
	@Child(name="condition", type=IdDt.class, order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<IdDt> myCondition;
	
	@Child(name="constraint", order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<StructureElementDefinitionConstraint> myConstraint;
	
	@Child(name="mustSupport", type=BooleanDt.class, order=14, min=0, max=1)	
	private BooleanDt myMustSupport;
	
	@Child(name="isModifier", type=BooleanDt.class, order=15, min=1, max=1)	
	private BooleanDt myIsModifier;
	
	@Child(name="binding", order=16, min=0, max=1)	
	private StructureElementDefinitionBinding myBinding;
	
	@Child(name="mapping", order=17, min=0, max=Child.MAX_UNLIMITED)	
	private List<StructureElementDefinitionMapping> myMapping;
	
	/**
	 * Gets the value(s) for <b>short</b> (Concise definition for xml presentation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public StringDt getShort() {  
		if (myShort == null) {
			myShort = new StringDt();
		}
		return myShort;
	}

	/**
	 * Sets the value(s) for <b>short</b> (Concise definition for xml presentation)
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public void setShort(StringDt theValue) {
		myShort = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>short</b> (Concise definition for xml presentation)
	 *
     * <p>
     * <b>Definition:</b>
     * A concise definition that  is shown in the generated XML format that summarizes profiles (used throughout the specification)
     * </p> 
	 */
	public void setShort( String theString) {
		myShort = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>formal</b> (Full formal definition in human language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     *  The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public StringDt getFormal() {  
		if (myFormal == null) {
			myFormal = new StringDt();
		}
		return myFormal;
	}

	/**
	 * Sets the value(s) for <b>formal</b> (Full formal definition in human language)
	 *
     * <p>
     * <b>Definition:</b>
     *  The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public void setFormal(StringDt theValue) {
		myFormal = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>formal</b> (Full formal definition in human language)
	 *
     * <p>
     * <b>Definition:</b>
     *  The definition SHALL be consistent with the base definition, but convey the meaning of the element in the particular context of use of the resource
     * </p> 
	 */
	public void setFormal( String theString) {
		myFormal = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>comments</b> (Comments about the use of this element).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public StringDt getComments() {  
		if (myComments == null) {
			myComments = new StringDt();
		}
		return myComments;
	}

	/**
	 * Sets the value(s) for <b>comments</b> (Comments about the use of this element)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public void setComments(StringDt theValue) {
		myComments = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>comments</b> (Comments about the use of this element)
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public void setComments( String theString) {
		myComments = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>requirements</b> (Why is this needed?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public StringDt getRequirements() {  
		if (myRequirements == null) {
			myRequirements = new StringDt();
		}
		return myRequirements;
	}

	/**
	 * Sets the value(s) for <b>requirements</b> (Why is this needed?)
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public void setRequirements(StringDt theValue) {
		myRequirements = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>requirements</b> (Why is this needed?)
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public void setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>synonym</b> (Other names).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public List<StringDt> getSynonym() {  
		if (mySynonym == null) {
			mySynonym = new ArrayList<StringDt>();
		}
		return mySynonym;
	}

	/**
	 * Sets the value(s) for <b>synonym</b> (Other names)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public void setSynonym(List<StringDt> theValue) {
		mySynonym = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>synonym</b> (Other names)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public void addSynonym( String theString) {
		if (mySynonym == null) {
			mySynonym = new ArrayList<StringDt>();
		}
		mySynonym.add(new StringDt(theString)); 
	}
 
	/**
	 * Gets the value(s) for <b>min</b> (Minimum Cardinality).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this element SHALL appear in the instance
     * </p> 
	 */
	public IntegerDt getMin() {  
		if (myMin == null) {
			myMin = new IntegerDt();
		}
		return myMin;
	}

	/**
	 * Sets the value(s) for <b>min</b> (Minimum Cardinality)
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this element SHALL appear in the instance
     * </p> 
	 */
	public void setMin(IntegerDt theValue) {
		myMin = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>max</b> (Maximum Cardinality (a number or *)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public StringDt getMax() {  
		if (myMax == null) {
			myMax = new StringDt();
		}
		return myMax;
	}

	/**
	 * Sets the value(s) for <b>max</b> (Maximum Cardinality (a number or *))
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public void setMax(StringDt theValue) {
		myMax = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>max</b> (Maximum Cardinality (a number or *))
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the instance
     * </p> 
	 */
	public void setMax( String theString) {
		myMax = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>type</b> (Data type and Profile for this element).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	public List<StructureElementDefinitionType> getType() {  
		if (myType == null) {
			myType = new ArrayList<StructureElementDefinitionType>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Data type and Profile for this element)
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	public void setType(List<StructureElementDefinitionType> theValue) {
		myType = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>nameReference</b> (To another element constraint (by element.name)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element 
     * </p> 
	 */
	public StringDt getNameReference() {  
		if (myNameReference == null) {
			myNameReference = new StringDt();
		}
		return myNameReference;
	}

	/**
	 * Sets the value(s) for <b>nameReference</b> (To another element constraint (by element.name))
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element 
     * </p> 
	 */
	public void setNameReference(StringDt theValue) {
		myNameReference = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>nameReference</b> (To another element constraint (by element.name))
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the name of a slice defined elsewhere in the profile whose constraints should be applied to the current element 
     * </p> 
	 */
	public void setNameReference( String theString) {
		myNameReference = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>value[x]</b> (Fixed value: [as defined for a primitive type]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a primitive value that SHALL hold for this element in the instance
     * </p> 
	 */
	public IDatatype getValue() {  
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value[x]</b> (Fixed value: [as defined for a primitive type])
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a primitive value that SHALL hold for this element in the instance
     * </p> 
	 */
	public void setValue(IDatatype theValue) {
		myValue = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>example[x]</b> (Example value: [as defined for type]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An example value for this element
     * </p> 
	 */
	public IDatatype getExample() {  
		return myExample;
	}

	/**
	 * Sets the value(s) for <b>example[x]</b> (Example value: [as defined for type])
	 *
     * <p>
     * <b>Definition:</b>
     * An example value for this element
     * </p> 
	 */
	public void setExample(IDatatype theValue) {
		myExample = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>maxLength</b> (Length for strings).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation
     * </p> 
	 */
	public IntegerDt getMaxLength() {  
		if (myMaxLength == null) {
			myMaxLength = new IntegerDt();
		}
		return myMaxLength;
	}

	/**
	 * Sets the value(s) for <b>maxLength</b> (Length for strings)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation
     * </p> 
	 */
	public void setMaxLength(IntegerDt theValue) {
		myMaxLength = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>condition</b> (Reference to invariant about presence).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public List<IdDt> getCondition() {  
		if (myCondition == null) {
			myCondition = new ArrayList<IdDt>();
		}
		return myCondition;
	}

	/**
	 * Sets the value(s) for <b>condition</b> (Reference to invariant about presence)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public void setCondition(List<IdDt> theValue) {
		myCondition = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>condition</b> (Reference to invariant about presence)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance
     * </p> 
	 */
	public void addCondition( String theId) {
		if (myCondition == null) {
			myCondition = new ArrayList<IdDt>();
		}
		myCondition.add(new IdDt(theId)); 
	}
 
	/**
	 * Gets the value(s) for <b>constraint</b> (Condition that must evaluate to true).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	public List<StructureElementDefinitionConstraint> getConstraint() {  
		if (myConstraint == null) {
			myConstraint = new ArrayList<StructureElementDefinitionConstraint>();
		}
		return myConstraint;
	}

	/**
	 * Sets the value(s) for <b>constraint</b> (Condition that must evaluate to true)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	public void setConstraint(List<StructureElementDefinitionConstraint> theValue) {
		myConstraint = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>mustSupport</b> (If the element must supported).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public BooleanDt getMustSupport() {  
		if (myMustSupport == null) {
			myMustSupport = new BooleanDt();
		}
		return myMustSupport;
	}

	/**
	 * Sets the value(s) for <b>mustSupport</b> (If the element must supported)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, conformant resource authors SHALL be capable of providing a value for the element and resource consumers SHALL be capable of extracting and doing something useful with the data element.  If false, the element may be ignored and not supported
     * </p> 
	 */
	public void setMustSupport(BooleanDt theValue) {
		myMustSupport = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>isModifier</b> (If this modifies the meaning of other elements).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p> 
	 */
	public BooleanDt getIsModifier() {  
		if (myIsModifier == null) {
			myIsModifier = new BooleanDt();
		}
		return myIsModifier;
	}

	/**
	 * Sets the value(s) for <b>isModifier</b> (If this modifies the meaning of other elements)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p> 
	 */
	public void setIsModifier(BooleanDt theValue) {
		myIsModifier = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>binding</b> (ValueSet details if this is coded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	public StructureElementDefinitionBinding getBinding() {  
		if (myBinding == null) {
			myBinding = new StructureElementDefinitionBinding();
		}
		return myBinding;
	}

	/**
	 * Sets the value(s) for <b>binding</b> (ValueSet details if this is coded)
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	public void setBinding(StructureElementDefinitionBinding theValue) {
		myBinding = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>mapping</b> (Map element to another set of definitions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public List<StructureElementDefinitionMapping> getMapping() {  
		if (myMapping == null) {
			myMapping = new ArrayList<StructureElementDefinitionMapping>();
		}
		return myMapping;
	}

	/**
	 * Sets the value(s) for <b>mapping</b> (Map element to another set of definitions)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public void setMapping(List<StructureElementDefinitionMapping> theValue) {
		myMapping = theValue;
	}
	
 

	}

	/**
	 * Block class for child element: <b>Profile.structure.element.definition.type</b> (Data type and Profile for this element)
	 *
     * <p>
     * <b>Definition:</b>
     * The data type or resource that the value of this element is permitted to be
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition.type")	
	public static class StructureElementDefinitionType extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	private CodeDt myCode;
	
	@Child(name="profile", type=UriDt.class, order=1, min=0, max=1)	
	private UriDt myProfile;
	
	@Child(name="aggregation", type=CodeDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeDt> myAggregation;
	
	/**
	 * Gets the value(s) for <b>code</b> (Name of Data type or Resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getCode() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Name of Data type or Resource)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public void setCode(CodeDt theValue) {
		myCode = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>profile</b> (Profile.structure to apply).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile
     * </p> 
	 */
	public UriDt getProfile() {  
		if (myProfile == null) {
			myProfile = new UriDt();
		}
		return myProfile;
	}

	/**
	 * Sets the value(s) for <b>profile</b> (Profile.structure to apply)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a profile that SHALL hold for resources or datatypes referenced as the type of this element. Can be a local reference - to another structure in this profile, or a reference to a structure in another profile
     * </p> 
	 */
	public void setProfile(UriDt theValue) {
		myProfile = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>aggregation</b> (contained | referenced | bundled - how aggregated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public List<CodeDt> getAggregation() {  
		if (myAggregation == null) {
			myAggregation = new ArrayList<CodeDt>();
		}
		return myAggregation;
	}

	/**
	 * Sets the value(s) for <b>aggregation</b> (contained | referenced | bundled - how aggregated)
	 *
     * <p>
     * <b>Definition:</b>
     * If the type is a reference to another resource, how the resource is or can be aggreated - is it a contained resource, or a reference, and if the context is a bundle, is it included in the bundle
     * </p> 
	 */
	public void setAggregation(List<CodeDt> theValue) {
		myAggregation = theValue;
	}
	
 

	}


	/**
	 * Block class for child element: <b>Profile.structure.element.definition.constraint</b> (Condition that must evaluate to true)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the context of the instance
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition.constraint")	
	public static class StructureElementDefinitionConstraint extends BaseElement implements IResourceBlock {
	
	@Child(name="key", type=IdDt.class, order=0, min=1, max=1)	
	private IdDt myKey;
	
	@Child(name="name", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myName;
	
	@Child(name="severity", type=CodeDt.class, order=2, min=1, max=1)	
	private CodeDt mySeverity;
	
	@Child(name="human", type=StringDt.class, order=3, min=1, max=1)	
	private StringDt myHuman;
	
	@Child(name="xpath", type=StringDt.class, order=4, min=1, max=1)	
	private StringDt myXpath;
	
	/**
	 * Gets the value(s) for <b>key</b> (Target of 'condition' reference above).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public IdDt getKey() {  
		if (myKey == null) {
			myKey = new IdDt();
		}
		return myKey;
	}

	/**
	 * Sets the value(s) for <b>key</b> (Target of 'condition' reference above)
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public void setKey(IdDt theValue) {
		myKey = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>key</b> (Target of 'condition' reference above)
	 *
     * <p>
     * <b>Definition:</b>
     * Allows identification of which elements have their cardinalities impacted by the constraint.  Will not be referenced for constraints that do not affect cardinality
     * </p> 
	 */
	public void setKey( String theId) {
		myKey = new IdDt(theId); 
	}
 
	/**
	 * Gets the value(s) for <b>name</b> (Short human label).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Short human label)
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Short human label)
	 *
     * <p>
     * <b>Definition:</b>
     * Used to label the constraint in OCL or in short displays incapable of displaying the full human description
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>severity</b> (error | warning).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the impact constraint violation has on the conformance of the instance
     * </p> 
	 */
	public CodeDt getSeverity() {  
		if (mySeverity == null) {
			mySeverity = new CodeDt();
		}
		return mySeverity;
	}

	/**
	 * Sets the value(s) for <b>severity</b> (error | warning)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the impact constraint violation has on the conformance of the instance
     * </p> 
	 */
	public void setSeverity(CodeDt theValue) {
		mySeverity = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>human</b> (Human description of constraint).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated 
     * </p> 
	 */
	public StringDt getHuman() {  
		if (myHuman == null) {
			myHuman = new StringDt();
		}
		return myHuman;
	}

	/**
	 * Sets the value(s) for <b>human</b> (Human description of constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated 
     * </p> 
	 */
	public void setHuman(StringDt theValue) {
		myHuman = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>human</b> (Human description of constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * Text that can be used to describe the constraint in messages identifying that the constraint has been violated 
     * </p> 
	 */
	public void setHuman( String theString) {
		myHuman = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>xpath</b> (XPath expression of constraint).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * XPath expression of constraint
     * </p> 
	 */
	public StringDt getXpath() {  
		if (myXpath == null) {
			myXpath = new StringDt();
		}
		return myXpath;
	}

	/**
	 * Sets the value(s) for <b>xpath</b> (XPath expression of constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * XPath expression of constraint
     * </p> 
	 */
	public void setXpath(StringDt theValue) {
		myXpath = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>xpath</b> (XPath expression of constraint)
	 *
     * <p>
     * <b>Definition:</b>
     * XPath expression of constraint
     * </p> 
	 */
	public void setXpath( String theString) {
		myXpath = new StringDt(theString); 
	}
 

	}


	/**
	 * Block class for child element: <b>Profile.structure.element.definition.binding</b> (ValueSet details if this is coded)
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition.binding")	
	public static class StructureElementDefinitionBinding extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	private StringDt myName;
	
	@Child(name="isExtensible", type=BooleanDt.class, order=1, min=1, max=1)	
	private BooleanDt myIsExtensible;
	
	@Child(name="conformance", type=CodeDt.class, order=2, min=0, max=1)	
	private CodeDt myConformance;
	
	@Child(name="description", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myDescription;
	
	@Child(name="reference", order=4, min=0, max=1, choice=@Choice(types= {
		UriDt.class,
		ValueSet.class,
	}))	
	private IDatatype myReference;
	
	/**
	 * Gets the value(s) for <b>name</b> (Descriptive Name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Descriptive Name)
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Descriptive Name)
	 *
     * <p>
     * <b>Definition:</b>
     * A descriptive name for this - can be useful for generating implementation artifacts
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>isExtensible</b> (Can additional codes be used?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public BooleanDt getIsExtensible() {  
		if (myIsExtensible == null) {
			myIsExtensible = new BooleanDt();
		}
		return myIsExtensible;
	}

	/**
	 * Sets the value(s) for <b>isExtensible</b> (Can additional codes be used?)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public void setIsExtensible(BooleanDt theValue) {
		myIsExtensible = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>conformance</b> (required | preferred | example).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public CodeDt getConformance() {  
		if (myConformance == null) {
			myConformance = new CodeDt();
		}
		return myConformance;
	}

	/**
	 * Sets the value(s) for <b>conformance</b> (required | preferred | example)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public void setConformance(CodeDt theValue) {
		myConformance = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>description</b> (Human explanation of the value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Human explanation of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public void setDescription(StringDt theValue) {
		myDescription = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>description</b> (Human explanation of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public void setDescription( String theString) {
		myDescription = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>reference[x]</b> (Source of value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set or external definition that identifies the set of codes to be used
     * </p> 
	 */
	public IDatatype getReference() {  
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference[x]</b> (Source of value set)
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set or external definition that identifies the set of codes to be used
     * </p> 
	 */
	public void setReference(IDatatype theValue) {
		myReference = theValue;
	}
	
 

	}


	/**
	 * Block class for child element: <b>Profile.structure.element.definition.mapping</b> (Map element to another set of definitions)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	@Block(name="Profile.structure.element.definition.mapping")	
	public static class StructureElementDefinitionMapping extends BaseElement implements IResourceBlock {
	
	@Child(name="identity", type=IdDt.class, order=0, min=1, max=1)	
	private IdDt myIdentity;
	
	@Child(name="map", type=StringDt.class, order=1, min=1, max=1)	
	private StringDt myMap;
	
	/**
	 * Gets the value(s) for <b>identity</b> (Reference to mapping declaration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public IdDt getIdentity() {  
		if (myIdentity == null) {
			myIdentity = new IdDt();
		}
		return myIdentity;
	}

	/**
	 * Sets the value(s) for <b>identity</b> (Reference to mapping declaration)
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public void setIdentity(IdDt theValue) {
		myIdentity = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>identity</b> (Reference to mapping declaration)
	 *
     * <p>
     * <b>Definition:</b>
     * An internal reference to the definition of a mapping
     * </p> 
	 */
	public void setIdentity( String theId) {
		myIdentity = new IdDt(theId); 
	}
 
	/**
	 * Gets the value(s) for <b>map</b> (Details of the mapping).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public StringDt getMap() {  
		if (myMap == null) {
			myMap = new StringDt();
		}
		return myMap;
	}

	/**
	 * Sets the value(s) for <b>map</b> (Details of the mapping)
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public void setMap(StringDt theValue) {
		myMap = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>map</b> (Details of the mapping)
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public void setMap( String theString) {
		myMap = new StringDt(theString); 
	}
 

	}




	/**
	 * Block class for child element: <b>Profile.structure.searchParam</b> (Search params defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	@Block(name="Profile.structure.searchParam")	
	public static class StructureSearchParam extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	private StringDt myName;
	
	@Child(name="type", type=CodeDt.class, order=1, min=1, max=1)	
	private CodeDt myType;
	
	@Child(name="documentation", type=StringDt.class, order=2, min=1, max=1)	
	private StringDt myDocumentation;
	
	@Child(name="xpath", type=StringDt.class, order=3, min=0, max=1)	
	private StringDt myXpath;
	
	@Child(name="target", type=CodeDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	private List<CodeDt> myTarget;
	
	/**
	 * Gets the value(s) for <b>name</b> (Name of search parameter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the standard or custom search parameter
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
     * The name of the standard or custom search parameter
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Name of search parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the standard or custom search parameter
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
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
	public CodeDt getType() {  
		if (myType == null) {
			myType = new CodeDt();
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
	public void setType(CodeDt theValue) {
		myType = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>documentation</b> (Contents and meaning of search parameter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification for search parameters. For standard parameters, provides additional information on how the parameter is used in this solution.  For custom parameters, provides a description of what the parameter does
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Contents and meaning of search parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification for search parameters. For standard parameters, provides additional information on how the parameter is used in this solution.  For custom parameters, provides a description of what the parameter does
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>documentation</b> (Contents and meaning of search parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification for search parameters. For standard parameters, provides additional information on how the parameter is used in this solution.  For custom parameters, provides a description of what the parameter does
     * </p> 
	 */
	public void setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>xpath</b> (XPath that extracts the parameter set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression that returns a set of elements for the search parameter
     * </p> 
	 */
	public StringDt getXpath() {  
		if (myXpath == null) {
			myXpath = new StringDt();
		}
		return myXpath;
	}

	/**
	 * Sets the value(s) for <b>xpath</b> (XPath that extracts the parameter set)
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression that returns a set of elements for the search parameter
     * </p> 
	 */
	public void setXpath(StringDt theValue) {
		myXpath = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>xpath</b> (XPath that extracts the parameter set)
	 *
     * <p>
     * <b>Definition:</b>
     * An XPath expression that returns a set of elements for the search parameter
     * </p> 
	 */
	public void setXpath( String theString) {
		myXpath = new StringDt(theString); 
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
	public List<CodeDt> getTarget() {  
		if (myTarget == null) {
			myTarget = new ArrayList<CodeDt>();
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
	public void setTarget(List<CodeDt> theValue) {
		myTarget = theValue;
	}
	
 

	}



	/**
	 * Block class for child element: <b>Profile.extensionDefn</b> (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	@Block(name="Profile.extensionDefn")	
	public static class ExtensionDefn extends BaseElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=1, min=0, max=1)	
	private StringDt myDisplay;
	
	@Child(name="contextType", type=CodeDt.class, order=2, min=1, max=1)	
	private CodeDt myContextType;
	
	@Child(name="context", type=StringDt.class, order=3, min=1, max=Child.MAX_UNLIMITED)	
	private List<StringDt> myContext;
	
	@Child(name="definition", type=StructureElementDefinition.class, order=4, min=1, max=1)	
	private StructureElementDefinition myDefinition;
	
	/**
	 * Gets the value(s) for <b>code</b> (Identifies the extension in this profile).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique code (within the profile) used to identify the extension
     * </p> 
	 */
	public CodeDt getCode() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Identifies the extension in this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique code (within the profile) used to identify the extension
     * </p> 
	 */
	public void setCode(CodeDt theValue) {
		myCode = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>display</b> (Use this name when displaying the value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user 
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (Use this name when displaying the value)
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user 
     * </p> 
	 */
	public void setDisplay(StringDt theValue) {
		myDisplay = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>display</b> (Use this name when displaying the value)
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user 
     * </p> 
	 */
	public void setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>contextType</b> (resource | datatype | mapping | extension).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of context to which the extension applies
     * </p> 
	 */
	public CodeDt getContextType() {  
		if (myContextType == null) {
			myContextType = new CodeDt();
		}
		return myContextType;
	}

	/**
	 * Sets the value(s) for <b>contextType</b> (resource | datatype | mapping | extension)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the type of context to which the extension applies
     * </p> 
	 */
	public void setContextType(CodeDt theValue) {
		myContextType = theValue;
	}
	
 
	/**
	 * Gets the value(s) for <b>context</b> (Where the extension can be used in instances).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public List<StringDt> getContext() {  
		if (myContext == null) {
			myContext = new ArrayList<StringDt>();
		}
		return myContext;
	}

	/**
	 * Sets the value(s) for <b>context</b> (Where the extension can be used in instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public void setContext(List<StringDt> theValue) {
		myContext = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>context</b> (Where the extension can be used in instances)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public void addContext( String theString) {
		if (myContext == null) {
			myContext = new ArrayList<StringDt>();
		}
		myContext.add(new StringDt(theString)); 
	}
 
	/**
	 * Gets the value(s) for <b>definition</b> (Definition of the extension and its content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the extension and its content
     * </p> 
	 */
	public StructureElementDefinition getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new StructureElementDefinition();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (Definition of the extension and its content)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of the extension and its content
     * </p> 
	 */
	public void setDefinition(StructureElementDefinition theValue) {
		myDefinition = theValue;
	}
	
 

	}


	/**
	 * Block class for child element: <b>Profile.query</b> (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	@Block(name="Profile.query")	
	public static class Query extends BaseElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	private StringDt myName;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=1, max=1)	
	private StringDt myDocumentation;
	
	@Child(name="parameter", type=StructureSearchParam.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	private List<StructureSearchParam> myParameter;
	
	/**
	 * Gets the value(s) for <b>name</b> (Special named queries (_query=)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the URI from Conformance statements declaring use of the query.  Typically this will also be the name for the _query parameter when the query is called, though in some cases it may be aliased by a server to avoid collisions
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
     * The name of a query, which is used in the URI from Conformance statements declaring use of the query.  Typically this will also be the name for the _query parameter when the query is called, though in some cases it may be aliased by a server to avoid collisions
     * </p> 
	 */
	public void setName(StringDt theValue) {
		myName = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>name</b> (Special named queries (_query=))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the URI from Conformance statements declaring use of the query.  Typically this will also be the name for the _query parameter when the query is called, though in some cases it may be aliased by a server to avoid collisions
     * </p> 
	 */
	public void setName( String theString) {
		myName = new StringDt(theString); 
	}
 
	/**
	 * Gets the value(s) for <b>documentation</b> (Describes the named query).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the query - the functionality it offers, and considerations about how it functions and to use it
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Describes the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the query - the functionality it offers, and considerations about how it functions and to use it
     * </p> 
	 */
	public void setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
	}
	
	/**
	 * Sets the value(s) for <b>documentation</b> (Describes the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Description of the query - the functionality it offers, and considerations about how it functions and to use it
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
     * A parameter of a named query
     * </p> 
	 */
	public List<StructureSearchParam> getParameter() {  
		if (myParameter == null) {
			myParameter = new ArrayList<StructureSearchParam>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> (Parameter for the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * A parameter of a named query
     * </p> 
	 */
	public void setParameter(List<StructureSearchParam> theValue) {
		myParameter = theValue;
	}
	
 

	}




}