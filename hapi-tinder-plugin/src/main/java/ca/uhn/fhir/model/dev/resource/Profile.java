















package ca.uhn.fhir.model.dev.resource;

import java.net.URI;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
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
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.composite.ContactPointDt;
import ca.uhn.fhir.model.dev.composite.ElementDefinitionDt;
import ca.uhn.fhir.model.dev.composite.IdentifierDt;
import ca.uhn.fhir.model.dev.valueset.FHIRDefinedTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>Profile</b> Resource
 * (infrastructure)
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
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Profile">http://hl7.org/fhir/profiles/Profile</a> 
 * </p>
 *
 */
@ResourceDef(name="Profile", profile="http://hl7.org/fhir/profiles/Profile", id="profile")
public class Profile 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.identifier</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Profile.identifier", description="The identifier of the profile", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.identifier</b><br>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.version</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="version", path="Profile.version", description="The version identifier of the profile", type="token"  )
	public static final String SP_VERSION = "version";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.version</b><br>
	 * </p>
	 */
	public static final TokenClientParam VERSION = new TokenClientParam(SP_VERSION);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Profile.name</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Profile.name", description="Name of the profile", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Profile.name</b><br>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Profile.publisher</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="publisher", path="Profile.publisher", description="Name of the publisher of the profile", type="string"  )
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Profile.publisher</b><br>
	 * </p>
	 */
	public static final StringClientParam PUBLISHER = new StringClientParam(SP_PUBLISHER);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Profile.description</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="Profile.description", description="Text search in the description of the profile", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>Profile.description</b><br>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.status</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Profile.status", description="The current status of the profile", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.status</b><br>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The profile publication date</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>Profile.date</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Profile.date", description="The profile publication date", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The profile publication date</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>Profile.date</b><br>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code for the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.code</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="Profile.code", description="A code for the profile", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code for the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.code</b><br>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>valueset</b>
	 * <p>
	 * Description: <b>A vocabulary binding code</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Profile.snapshot.element.binding.reference[x]</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="valueset", path="Profile.snapshot.element.binding.reference[x]", description="A vocabulary binding code", type="reference"  )
	public static final String SP_VALUESET = "valueset";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>valueset</b>
	 * <p>
	 * Description: <b>A vocabulary binding code</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>Profile.snapshot.element.binding.reference[x]</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam VALUESET = new ReferenceClientParam(SP_VALUESET);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>Type of resource that is constrained in the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.type</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="Profile.type", description="Type of resource that is constrained in the profile", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>Type of resource that is constrained in the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.type</b><br>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>url</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.url</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="url", path="Profile.url", description="", type="token"  )
	public static final String SP_URL = "url";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>url</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>Profile.url</b><br>
	 * </p>
	 */
	public static final TokenClientParam URL = new TokenClientParam(SP_URL);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.code</b>".
	 */
	public static final Include INCLUDE_CODE = new Include("Profile.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("Profile.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.description</b>".
	 */
	public static final Include INCLUDE_DESCRIPTION = new Include("Profile.description");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Profile.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("Profile.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.publisher</b>".
	 */
	public static final Include INCLUDE_PUBLISHER = new Include("Profile.publisher");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.snapshot.element.binding.reference[x]</b>".
	 */
	public static final Include INCLUDE_SNAPSHOT_ELEMENT_BINDING_REFERENCE = new Include("Profile.snapshot.element.binding.reference[x]");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Profile.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("Profile.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.url</b>".
	 */
	public static final Include INCLUDE_URL = new Include("Profile.url");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Profile.version</b>".
	 */
	public static final Include INCLUDE_VERSION = new Include("Profile.version");


	@Child(name="url", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="id",
		formalDefinition="The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems"
	)
	private UriDt myUrl;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="id",
		formalDefinition="Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="id.version",
		formalDefinition="The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language name identifying the Profile"
	)
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="who.witness",
		formalDefinition="Details of the individual or organization who accepts responsibility for publishing the profile"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactPointDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Contact details to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language description of the profile and its use"
	)
	private StringDt myDescription;
	
	@Child(name="code", type=CodingDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="class",
		formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of templates."
	)
	private java.util.List<CodingDt> myCode;
	
	@Child(name="status", type=CodeDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="status",
		formalDefinition="The status of the profile"
	)
	private BoundCodeDt<ResourceProfileStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="class",
		formalDefinition="This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="date", type=DateTimeDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="when.recorded",
		formalDefinition="The date that this version of the profile was published"
	)
	private DateTimeDt myDate;
	
	@Child(name="requirements", type=StringDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="why",
		formalDefinition="The Scope and Usage that this profile was created to meet"
	)
	private StringDt myRequirements;
	
	@Child(name="fhirVersion", type=IdDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version"
	)
	private IdDt myFhirVersion;
	
	@Child(name="mapping", order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="An external specification that the content is mapped to"
	)
	private java.util.List<Mapping> myMapping;
	
	@Child(name="type", type=CodeDt.class, order=14, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The Resource or Data type being described"
	)
	private BoundCodeDt<FHIRDefinedTypeEnum> myType;
	
	@Child(name="base", type=UriDt.class, order=15, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The structure that is the base on which this set of constraints is derived from."
	)
	private UriDt myBase;
	
	@Child(name="snapshot", order=16, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base profile"
	)
	private Snapshot mySnapshot;
	
	@Child(name="differential", type=Snapshot.class, order=17, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A differential view is expressed relative to the base profile - a statement of differences that it applies"
	)
	private Snapshot myDifferential;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myUrl,  myIdentifier,  myVersion,  myName,  myPublisher,  myTelecom,  myDescription,  myCode,  myStatus,  myExperimental,  myDate,  myRequirements,  myFhirVersion,  myMapping,  myType,  myBase,  mySnapshot,  myDifferential);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myUrl, myIdentifier, myVersion, myName, myPublisher, myTelecom, myDescription, myCode, myStatus, myExperimental, myDate, myRequirements, myFhirVersion, myMapping, myType, myBase, mySnapshot, myDifferential);
	}

	/**
	 * Gets the value(s) for <b>url</b> (id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems
     * </p> 
	 */
	public UriDt getUrlElement() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	
	/**
	 * Gets the value(s) for <b>url</b> (id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems
     * </p> 
	 */
	public URI getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems
     * </p> 
	 */
	public Profile setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems
     * </p> 
	 */
	public Profile setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> (id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)
     * </p> 
	 */
	public Profile setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (id),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>version</b> (id.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> (id.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> (id.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually
     * </p> 
	 */
	public Profile setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> (id.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually
     * </p> 
	 */
	public Profile setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public Profile setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public Profile setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>publisher</b> (who.witness).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public StringDt getPublisherElement() {  
		if (myPublisher == null) {
			myPublisher = new StringDt();
		}
		return myPublisher;
	}

	
	/**
	 * Gets the value(s) for <b>publisher</b> (who.witness).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public String getPublisher() {  
		return getPublisherElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>publisher</b> (who.witness)
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public Profile setPublisher(StringDt theValue) {
		myPublisher = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>publisher</b> (who.witness)
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the profile
     * </p> 
	 */
	public Profile setPublisher( String theString) {
		myPublisher = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>telecom</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public java.util.List<ContactPointDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactPointDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public Profile setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ContactPointDt addTelecom() {
		ContactPointDt newType = new ContactPointDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ContactPointDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public StringDt getDescriptionElement() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public String getDescription() {  
		return getDescriptionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public Profile setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the profile and its use
     * </p> 
	 */
	public Profile setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (class).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public java.util.List<CodingDt> getCode() {  
		if (myCode == null) {
			myCode = new java.util.ArrayList<CodingDt>();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (class)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public Profile setCode(java.util.List<CodingDt> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>code</b> (class)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public CodingDt addCode() {
		CodingDt newType = new CodingDt();
		getCode().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>code</b> (class),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public CodingDt getCodeFirstRep() {
		if (getCode().isEmpty()) {
			return addCode();
		}
		return getCode().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>status</b> (status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public BoundCodeDt<ResourceProfileStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ResourceProfileStatusEnum>(ResourceProfileStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (status)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public Profile setStatus(BoundCodeDt<ResourceProfileStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (status)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the profile
     * </p> 
	 */
	public Profile setStatus(ResourceProfileStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>experimental</b> (class).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimentalElement() {  
		if (myExperimental == null) {
			myExperimental = new BooleanDt();
		}
		return myExperimental;
	}

	
	/**
	 * Gets the value(s) for <b>experimental</b> (class).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public Boolean getExperimental() {  
		return getExperimentalElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>experimental</b> (class)
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public Profile setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>experimental</b> (class)
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public Profile setExperimental( boolean theBoolean) {
		myExperimental = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> (when.recorded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public DateTimeDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	
	/**
	 * Gets the value(s) for <b>date</b> (when.recorded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public Date getDate() {  
		return getDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>date</b> (when.recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public Profile setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> (when.recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public Profile setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (when.recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the profile was published
     * </p> 
	 */
	public Profile setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>requirements</b> (why).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public StringDt getRequirementsElement() {  
		if (myRequirements == null) {
			myRequirements = new StringDt();
		}
		return myRequirements;
	}

	
	/**
	 * Gets the value(s) for <b>requirements</b> (why).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public String getRequirements() {  
		return getRequirementsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>requirements</b> (why)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public Profile setRequirements(StringDt theValue) {
		myRequirements = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>requirements</b> (why)
	 *
     * <p>
     * <b>Definition:</b>
     * The Scope and Usage that this profile was created to meet
     * </p> 
	 */
	public Profile setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>fhirVersion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version
     * </p> 
	 */
	public IdDt getFhirVersionElement() {  
		if (myFhirVersion == null) {
			myFhirVersion = new IdDt();
		}
		return myFhirVersion;
	}

	
	/**
	 * Gets the value(s) for <b>fhirVersion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version
     * </p> 
	 */
	public String getFhirVersion() {  
		return getFhirVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>fhirVersion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version
     * </p> 
	 */
	public Profile setFhirVersion(IdDt theValue) {
		myFhirVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>fhirVersion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version
     * </p> 
	 */
	public Profile setFhirVersion( String theId) {
		myFhirVersion = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>mapping</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public java.util.List<Mapping> getMapping() {  
		if (myMapping == null) {
			myMapping = new java.util.ArrayList<Mapping>();
		}
		return myMapping;
	}

	/**
	 * Sets the value(s) for <b>mapping</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public Profile setMapping(java.util.List<Mapping> theValue) {
		myMapping = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>mapping</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public Mapping addMapping() {
		Mapping newType = new Mapping();
		getMapping().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>mapping</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	public Mapping getMappingFirstRep() {
		if (getMapping().isEmpty()) {
			return addMapping();
		}
		return getMapping().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Resource or Data type being described
     * </p> 
	 */
	public BoundCodeDt<FHIRDefinedTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<FHIRDefinedTypeEnum>(FHIRDefinedTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The Resource or Data type being described
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Resource or Data type being described
     * </p> 
	 */
	public Profile setType(BoundCodeDt<FHIRDefinedTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The Resource or Data type being described
     * </p> 
	 */
	public Profile setType(FHIRDefinedTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>base</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The structure that is the base on which this set of constraints is derived from.
     * </p> 
	 */
	public UriDt getBaseElement() {  
		if (myBase == null) {
			myBase = new UriDt();
		}
		return myBase;
	}

	
	/**
	 * Gets the value(s) for <b>base</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The structure that is the base on which this set of constraints is derived from.
     * </p> 
	 */
	public URI getBase() {  
		return getBaseElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>base</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The structure that is the base on which this set of constraints is derived from.
     * </p> 
	 */
	public Profile setBase(UriDt theValue) {
		myBase = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>base</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The structure that is the base on which this set of constraints is derived from.
     * </p> 
	 */
	public Profile setBase( String theUri) {
		myBase = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>snapshot</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base profile
     * </p> 
	 */
	public Snapshot getSnapshot() {  
		if (mySnapshot == null) {
			mySnapshot = new Snapshot();
		}
		return mySnapshot;
	}

	/**
	 * Sets the value(s) for <b>snapshot</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base profile
     * </p> 
	 */
	public Profile setSnapshot(Snapshot theValue) {
		mySnapshot = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>differential</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A differential view is expressed relative to the base profile - a statement of differences that it applies
     * </p> 
	 */
	public Snapshot getDifferential() {  
		if (myDifferential == null) {
			myDifferential = new Snapshot();
		}
		return myDifferential;
	}

	/**
	 * Sets the value(s) for <b>differential</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A differential view is expressed relative to the base profile - a statement of differences that it applies
     * </p> 
	 */
	public Profile setDifferential(Snapshot theValue) {
		myDifferential = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>Profile.mapping</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	@Block()	
	public static class Mapping 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identity", type=IdDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An Internal id that is used to identify this mapping set when specific mappings are made"
	)
	private IdDt myIdentity;
	
	@Child(name="uri", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A URI that identifies the specification that this mapping is expressed to"
	)
	private UriDt myUri;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A name for the specification that is being mapped to"
	)
	private StringDt myName;
	
	@Child(name="comments", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage"
	)
	private StringDt myComments;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentity,  myUri,  myName,  myComments);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentity, myUri, myName, myComments);
	}

	/**
	 * Gets the value(s) for <b>identity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public IdDt getIdentityElement() {  
		if (myIdentity == null) {
			myIdentity = new IdDt();
		}
		return myIdentity;
	}

	
	/**
	 * Gets the value(s) for <b>identity</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public String getIdentity() {  
		return getIdentityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public Mapping setIdentity(IdDt theValue) {
		myIdentity = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identity</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An Internal id that is used to identify this mapping set when specific mappings are made
     * </p> 
	 */
	public Mapping setIdentity( String theId) {
		myIdentity = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>uri</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public UriDt getUriElement() {  
		if (myUri == null) {
			myUri = new UriDt();
		}
		return myUri;
	}

	
	/**
	 * Gets the value(s) for <b>uri</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public URI getUri() {  
		return getUriElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>uri</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public Mapping setUri(UriDt theValue) {
		myUri = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>uri</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public Mapping setUri( String theUri) {
		myUri = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public Mapping setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A name for the specification that is being mapped to
     * </p> 
	 */
	public Mapping setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>comments</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public StringDt getCommentsElement() {  
		if (myComments == null) {
			myComments = new StringDt();
		}
		return myComments;
	}

	
	/**
	 * Gets the value(s) for <b>comments</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public String getComments() {  
		return getCommentsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>comments</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public Mapping setComments(StringDt theValue) {
		myComments = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>comments</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage
     * </p> 
	 */
	public Mapping setComments( String theString) {
		myComments = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Profile.snapshot</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base profile
     * </p> 
	 */
	@Block()	
	public static class Snapshot 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="element", type=ElementDefinitionDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Captures constraints on each element within the resource"
	)
	private java.util.List<ElementDefinitionDt> myElement;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myElement);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myElement);
	}

	/**
	 * Gets the value(s) for <b>element</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	public java.util.List<ElementDefinitionDt> getElement() {  
		if (myElement == null) {
			myElement = new java.util.ArrayList<ElementDefinitionDt>();
		}
		return myElement;
	}

	/**
	 * Sets the value(s) for <b>element</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	public Snapshot setElement(java.util.List<ElementDefinitionDt> theValue) {
		myElement = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>element</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	public ElementDefinitionDt addElement() {
		ElementDefinitionDt newType = new ElementDefinitionDt();
		getElement().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>element</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Captures constraints on each element within the resource
     * </p> 
	 */
	public ElementDefinitionDt getElementFirstRep() {
		if (getElement().isEmpty()) {
			return addElement();
		}
		return getElement().get(0); 
	}
  

	}




    @Override
    public String getResourceName() {
        return "Profile";
    }
    
    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DEV;
    }

}
