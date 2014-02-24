











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
	
	@Child(name="fhirVersion", type=IdentifierDt.class, order=11, min=0, max=1)	
	private IdentifierDt myFhirVersion;
	
	@Child(name="mapping", order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myMapping;
	
	@Child(name="structure", order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myStructure;
	
	@Child(name="extensionDefn", order=14, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myExtensionDefn;
	
	@Child(name="query", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myQuery;
	
	/**
	 * Gets the value(s) for identifier (Logical id to reference this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Logical id to reference this profile)
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
	 * Gets the value(s) for version (Logical id for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {
		return myVersion;
	}

	/**
	 * Sets the value(s) for version (Logical id for this version of the profile)
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
	 * Gets the value(s) for name (Informal name for this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Informal name for this profile)
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
	 * Gets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public StringDt getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value(s) for publisher (Name of the publisher (Organization or individual))
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
	 * Gets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact information of the publisher)
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
	 * Gets the value(s) for description (Natural language description of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Natural language description of the profile)
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
	 * Gets the value(s) for code (Assist with indexing and finding)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public List<CodingDt> getCode() {
		return myCode;
	}

	/**
	 * Sets the value(s) for code (Assist with indexing and finding)
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
	 * Gets the value(s) for status (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (draft | active | retired)
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
	 * Gets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {
		return myExperimental;
	}

	/**
	 * Sets the value(s) for experimental (If for testing purposes, not real usage)
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
	 * Gets the value(s) for date (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public DateTimeDt getDate() {
		return myDate;
	}

	/**
	 * Sets the value(s) for date (Date for this version of the profile)
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
	 * Gets the value(s) for requirements (Scope and Usage this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public StringDt getRequirements() {
		return myRequirements;
	}

	/**
	 * Sets the value(s) for requirements (Scope and Usage this profile is for)
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
	 * Gets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public IdentifierDt getFhirVersion() {
		return myFhirVersion;
	}

	/**
	 * Sets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public void setFhirVersion(IdentifierDt theValue) {
		myFhirVersion = theValue;
	}
	
	/**
	 * Gets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public List<IDatatype> getMapping() {
		return myMapping;
	}

	/**
	 * Sets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public void setMapping(List<IDatatype> theValue) {
		myMapping = theValue;
	}
	
	/**
	 * Gets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public List<IDatatype> getStructure() {
		return myStructure;
	}

	/**
	 * Sets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public void setStructure(List<IDatatype> theValue) {
		myStructure = theValue;
	}
	
	/**
	 * Gets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public List<IDatatype> getExtensionDefn() {
		return myExtensionDefn;
	}

	/**
	 * Sets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public void setExtensionDefn(List<IDatatype> theValue) {
		myExtensionDefn = theValue;
	}
	
	/**
	 * Gets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public List<IDatatype> getQuery() {
		return myQuery;
	}

	/**
	 * Sets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public void setQuery(List<IDatatype> theValue) {
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
	
	@Child(name="fhirVersion", type=IdentifierDt.class, order=11, min=0, max=1)	
	private IdentifierDt myFhirVersion;
	
	@Child(name="mapping", order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myMapping;
	
	@Child(name="structure", order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myStructure;
	
	@Child(name="extensionDefn", order=14, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myExtensionDefn;
	
	@Child(name="query", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myQuery;
	
	/**
	 * Gets the value(s) for identifier (Logical id to reference this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Logical id to reference this profile)
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
	 * Gets the value(s) for version (Logical id for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {
		return myVersion;
	}

	/**
	 * Sets the value(s) for version (Logical id for this version of the profile)
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
	 * Gets the value(s) for name (Informal name for this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Informal name for this profile)
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
	 * Gets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public StringDt getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value(s) for publisher (Name of the publisher (Organization or individual))
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
	 * Gets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact information of the publisher)
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
	 * Gets the value(s) for description (Natural language description of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Natural language description of the profile)
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
	 * Gets the value(s) for code (Assist with indexing and finding)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public List<CodingDt> getCode() {
		return myCode;
	}

	/**
	 * Sets the value(s) for code (Assist with indexing and finding)
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
	 * Gets the value(s) for status (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (draft | active | retired)
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
	 * Gets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {
		return myExperimental;
	}

	/**
	 * Sets the value(s) for experimental (If for testing purposes, not real usage)
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
	 * Gets the value(s) for date (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public DateTimeDt getDate() {
		return myDate;
	}

	/**
	 * Sets the value(s) for date (Date for this version of the profile)
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
	 * Gets the value(s) for requirements (Scope and Usage this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public StringDt getRequirements() {
		return myRequirements;
	}

	/**
	 * Sets the value(s) for requirements (Scope and Usage this profile is for)
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
	 * Gets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public IdentifierDt getFhirVersion() {
		return myFhirVersion;
	}

	/**
	 * Sets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public void setFhirVersion(IdentifierDt theValue) {
		myFhirVersion = theValue;
	}
	
	/**
	 * Gets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public List<IDatatype> getMapping() {
		return myMapping;
	}

	/**
	 * Sets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public void setMapping(List<IDatatype> theValue) {
		myMapping = theValue;
	}
	
	/**
	 * Gets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public List<IDatatype> getStructure() {
		return myStructure;
	}

	/**
	 * Sets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public void setStructure(List<IDatatype> theValue) {
		myStructure = theValue;
	}
	
	/**
	 * Gets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public List<IDatatype> getExtensionDefn() {
		return myExtensionDefn;
	}

	/**
	 * Sets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public void setExtensionDefn(List<IDatatype> theValue) {
		myExtensionDefn = theValue;
	}
	
	/**
	 * Gets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public List<IDatatype> getQuery() {
		return myQuery;
	}

	/**
	 * Sets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public void setQuery(List<IDatatype> theValue) {
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
	
	@Child(name="fhirVersion", type=IdentifierDt.class, order=11, min=0, max=1)	
	private IdentifierDt myFhirVersion;
	
	@Child(name="mapping", order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myMapping;
	
	@Child(name="structure", order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myStructure;
	
	@Child(name="extensionDefn", order=14, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myExtensionDefn;
	
	@Child(name="query", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myQuery;
	
	/**
	 * Gets the value(s) for identifier (Logical id to reference this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Logical id to reference this profile)
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
	 * Gets the value(s) for version (Logical id for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {
		return myVersion;
	}

	/**
	 * Sets the value(s) for version (Logical id for this version of the profile)
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
	 * Gets the value(s) for name (Informal name for this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Informal name for this profile)
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
	 * Gets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public StringDt getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value(s) for publisher (Name of the publisher (Organization or individual))
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
	 * Gets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact information of the publisher)
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
	 * Gets the value(s) for description (Natural language description of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Natural language description of the profile)
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
	 * Gets the value(s) for code (Assist with indexing and finding)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public List<CodingDt> getCode() {
		return myCode;
	}

	/**
	 * Sets the value(s) for code (Assist with indexing and finding)
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
	 * Gets the value(s) for status (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (draft | active | retired)
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
	 * Gets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {
		return myExperimental;
	}

	/**
	 * Sets the value(s) for experimental (If for testing purposes, not real usage)
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
	 * Gets the value(s) for date (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public DateTimeDt getDate() {
		return myDate;
	}

	/**
	 * Sets the value(s) for date (Date for this version of the profile)
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
	 * Gets the value(s) for requirements (Scope and Usage this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public StringDt getRequirements() {
		return myRequirements;
	}

	/**
	 * Sets the value(s) for requirements (Scope and Usage this profile is for)
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
	 * Gets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public IdentifierDt getFhirVersion() {
		return myFhirVersion;
	}

	/**
	 * Sets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public void setFhirVersion(IdentifierDt theValue) {
		myFhirVersion = theValue;
	}
	
	/**
	 * Gets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public List<IDatatype> getMapping() {
		return myMapping;
	}

	/**
	 * Sets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public void setMapping(List<IDatatype> theValue) {
		myMapping = theValue;
	}
	
	/**
	 * Gets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public List<IDatatype> getStructure() {
		return myStructure;
	}

	/**
	 * Sets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public void setStructure(List<IDatatype> theValue) {
		myStructure = theValue;
	}
	
	/**
	 * Gets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public List<IDatatype> getExtensionDefn() {
		return myExtensionDefn;
	}

	/**
	 * Sets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public void setExtensionDefn(List<IDatatype> theValue) {
		myExtensionDefn = theValue;
	}
	
	/**
	 * Gets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public List<IDatatype> getQuery() {
		return myQuery;
	}

	/**
	 * Sets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public void setQuery(List<IDatatype> theValue) {
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
	
	@Child(name="fhirVersion", type=IdentifierDt.class, order=11, min=0, max=1)	
	private IdentifierDt myFhirVersion;
	
	@Child(name="mapping", order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myMapping;
	
	@Child(name="structure", order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myStructure;
	
	@Child(name="extensionDefn", order=14, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myExtensionDefn;
	
	@Child(name="query", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myQuery;
	
	/**
	 * Gets the value(s) for identifier (Logical id to reference this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Logical id to reference this profile)
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
	 * Gets the value(s) for version (Logical id for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {
		return myVersion;
	}

	/**
	 * Sets the value(s) for version (Logical id for this version of the profile)
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
	 * Gets the value(s) for name (Informal name for this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Informal name for this profile)
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
	 * Gets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public StringDt getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value(s) for publisher (Name of the publisher (Organization or individual))
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
	 * Gets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact information of the publisher)
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
	 * Gets the value(s) for description (Natural language description of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Natural language description of the profile)
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
	 * Gets the value(s) for code (Assist with indexing and finding)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public List<CodingDt> getCode() {
		return myCode;
	}

	/**
	 * Sets the value(s) for code (Assist with indexing and finding)
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
	 * Gets the value(s) for status (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (draft | active | retired)
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
	 * Gets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {
		return myExperimental;
	}

	/**
	 * Sets the value(s) for experimental (If for testing purposes, not real usage)
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
	 * Gets the value(s) for date (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public DateTimeDt getDate() {
		return myDate;
	}

	/**
	 * Sets the value(s) for date (Date for this version of the profile)
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
	 * Gets the value(s) for requirements (Scope and Usage this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public StringDt getRequirements() {
		return myRequirements;
	}

	/**
	 * Sets the value(s) for requirements (Scope and Usage this profile is for)
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
	 * Gets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public IdentifierDt getFhirVersion() {
		return myFhirVersion;
	}

	/**
	 * Sets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public void setFhirVersion(IdentifierDt theValue) {
		myFhirVersion = theValue;
	}
	
	/**
	 * Gets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public List<IDatatype> getMapping() {
		return myMapping;
	}

	/**
	 * Sets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public void setMapping(List<IDatatype> theValue) {
		myMapping = theValue;
	}
	
	/**
	 * Gets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public List<IDatatype> getStructure() {
		return myStructure;
	}

	/**
	 * Sets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public void setStructure(List<IDatatype> theValue) {
		myStructure = theValue;
	}
	
	/**
	 * Gets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public List<IDatatype> getExtensionDefn() {
		return myExtensionDefn;
	}

	/**
	 * Sets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public void setExtensionDefn(List<IDatatype> theValue) {
		myExtensionDefn = theValue;
	}
	
	/**
	 * Gets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public List<IDatatype> getQuery() {
		return myQuery;
	}

	/**
	 * Sets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public void setQuery(List<IDatatype> theValue) {
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
	
	@Child(name="fhirVersion", type=IdentifierDt.class, order=11, min=0, max=1)	
	private IdentifierDt myFhirVersion;
	
	@Child(name="mapping", order=12, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myMapping;
	
	@Child(name="structure", order=13, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myStructure;
	
	@Child(name="extensionDefn", order=14, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myExtensionDefn;
	
	@Child(name="query", order=15, min=0, max=Child.MAX_UNLIMITED)	
	private List<IDatatype> myQuery;
	
	/**
	 * Gets the value(s) for identifier (Logical id to reference this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this profile when it is referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for identifier (Logical id to reference this profile)
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
	 * Gets the value(s) for version (Logical id for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {
		return myVersion;
	}

	/**
	 * Sets the value(s) for version (Logical id for this version of the profile)
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
	 * Gets the value(s) for name (Informal name for this profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public StringDt getName() {
		return myName;
	}

	/**
	 * Sets the value(s) for name (Informal name for this profile)
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
	 * Gets the value(s) for publisher (Name of the publisher (Organization or individual))
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public StringDt getPublisher() {
		return myPublisher;
	}

	/**
	 * Sets the value(s) for publisher (Name of the publisher (Organization or individual))
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
	 * Gets the value(s) for telecom (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public List<ContactDt> getTelecom() {
		return myTelecom;
	}

	/**
	 * Sets the value(s) for telecom (Contact information of the publisher)
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
	 * Gets the value(s) for description (Natural language description of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public StringDt getDescription() {
		return myDescription;
	}

	/**
	 * Sets the value(s) for description (Natural language description of the profile)
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
	 * Gets the value(s) for code (Assist with indexing and finding)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public List<CodingDt> getCode() {
		return myCode;
	}

	/**
	 * Sets the value(s) for code (Assist with indexing and finding)
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
	 * Gets the value(s) for status (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public CodeDt getStatus() {
		return myStatus;
	}

	/**
	 * Sets the value(s) for status (draft | active | retired)
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
	 * Gets the value(s) for experimental (If for testing purposes, not real usage)
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimental() {
		return myExperimental;
	}

	/**
	 * Sets the value(s) for experimental (If for testing purposes, not real usage)
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
	 * Gets the value(s) for date (Date for this version of the profile)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public DateTimeDt getDate() {
		return myDate;
	}

	/**
	 * Sets the value(s) for date (Date for this version of the profile)
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
	 * Gets the value(s) for requirements (Scope and Usage this profile is for)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public StringDt getRequirements() {
		return myRequirements;
	}

	/**
	 * Sets the value(s) for requirements (Scope and Usage this profile is for)
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
	 * Gets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public IdentifierDt getFhirVersion() {
		return myFhirVersion;
	}

	/**
	 * Sets the value(s) for fhirVersion (FHIR Version this profile targets)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based
     * </p> 
	 */
	public void setFhirVersion(IdentifierDt theValue) {
		myFhirVersion = theValue;
	}
	
	/**
	 * Gets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public List<IDatatype> getMapping() {
		return myMapping;
	}

	/**
	 * Sets the value(s) for mapping (External specification that the content is mapped to)
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public void setMapping(List<IDatatype> theValue) {
		myMapping = theValue;
	}
	
	/**
	 * Gets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public List<IDatatype> getStructure() {
		return myStructure;
	}

	/**
	 * Sets the value(s) for structure (A constraint on a resource or a data type)
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint statement about what contents a resource or data type may have
     * </p> 
	 */
	public void setStructure(List<IDatatype> theValue) {
		myStructure = theValue;
	}
	
	/**
	 * Gets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public List<IDatatype> getExtensionDefn() {
		return myExtensionDefn;
	}

	/**
	 * Sets the value(s) for extensionDefn (Definition of an extension)
	 *
     * <p>
     * <b>Definition:</b>
     * An extension defined as part of the profile
     * </p> 
	 */
	public void setExtensionDefn(List<IDatatype> theValue) {
		myExtensionDefn = theValue;
	}
	
	/**
	 * Gets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public List<IDatatype> getQuery() {
		return myQuery;
	}

	/**
	 * Sets the value(s) for query (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public void setQuery(List<IDatatype> theValue) {
		myQuery = theValue;
	}
	
	}



}