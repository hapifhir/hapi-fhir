











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
public class Profile implements IResource {

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
	public static class Mapping implements IResourceBlock {
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
	public static class Structure implements IResourceBlock {
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
	public static class ExtensionDefn implements IResourceBlock {
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
	public static class Query implements IResourceBlock {
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
	
 
	}



}