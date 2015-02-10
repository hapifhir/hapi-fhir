















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
import ca.uhn.fhir.model.dev.composite.BoundCodeableConceptDt;
import ca.uhn.fhir.model.dev.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.composite.ContactPointDt;
import ca.uhn.fhir.model.dev.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dev.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dev.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dev.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dev.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ResourceVersionPolicyEnum;
import ca.uhn.fhir.model.dev.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dev.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dev.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dev.valueset.SystemRestfulInteractionEnum;
import ca.uhn.fhir.model.dev.valueset.TypeRestfulInteractionEnum;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>Conformance</b> Resource
 * ()
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
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Conformance">http://hl7.org/fhir/profiles/Conformance</a> 
 * </p>
 *
 */
@ResourceDef(name="Conformance", profile="http://hl7.org/fhir/profiles/Conformance")
public class Conformance 
    extends  ca.uhn.fhir.model.base.resource.BaseConformance     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Conformance.identifier", description="The identifier of the conformance statement", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.version</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="version", path="Conformance.version", description="The version identifier of the conformance statement", type="token"  )
	public static final String SP_VERSION = "version";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.version</b><br/>
	 * </p>
	 */
	public static final TokenClientParam VERSION = new TokenClientParam(SP_VERSION);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the conformance statement</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Conformance.name", description="Name of the conformance statement", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the conformance statement</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the conformance statement</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.publisher</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="publisher", path="Conformance.publisher", description="Name of the publisher of the conformance statement", type="string"  )
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the conformance statement</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.publisher</b><br/>
	 * </p>
	 */
	public static final StringClientParam PUBLISHER = new StringClientParam(SP_PUBLISHER);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the conformance statement</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.description</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="Conformance.description", description="Text search in the description of the conformance statement", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the conformance statement</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.description</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="Conformance.status", description="The current status of the conformance statement", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The conformance statement publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Conformance.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="Conformance.date", description="The conformance statement publication date", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The conformance statement publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Conformance.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>software</b>
	 * <p>
	 * Description: <b>Part of a the name of a software application</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.software.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="software", path="Conformance.software.name", description="Part of a the name of a software application", type="string"  )
	public static final String SP_SOFTWARE = "software";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>software</b>
	 * <p>
	 * Description: <b>Part of a the name of a software application</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Conformance.software.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam SOFTWARE = new StringClientParam(SP_SOFTWARE);

	/**
	 * Search parameter constant for <b>fhirversion</b>
	 * <p>
	 * Description: <b>The version of FHIR</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.version</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="fhirversion", path="Conformance.version", description="The version of FHIR", type="token"  )
	public static final String SP_FHIRVERSION = "fhirversion";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>fhirversion</b>
	 * <p>
	 * Description: <b>The version of FHIR</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.version</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FHIRVERSION = new TokenClientParam(SP_FHIRVERSION);

	/**
	 * Search parameter constant for <b>resource</b>
	 * <p>
	 * Description: <b>Name of a resource mentioned in a conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.rest.resource.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="resource", path="Conformance.rest.resource.type", description="Name of a resource mentioned in a conformance statement", type="token"  )
	public static final String SP_RESOURCE = "resource";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>resource</b>
	 * <p>
	 * Description: <b>Name of a resource mentioned in a conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.rest.resource.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam RESOURCE = new TokenClientParam(SP_RESOURCE);

	/**
	 * Search parameter constant for <b>event</b>
	 * <p>
	 * Description: <b>Event code in a conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.messaging.event.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event", path="Conformance.messaging.event.code", description="Event code in a conformance statement", type="token"  )
	public static final String SP_EVENT = "event";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event</b>
	 * <p>
	 * Description: <b>Event code in a conformance statement</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.messaging.event.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EVENT = new TokenClientParam(SP_EVENT);

	/**
	 * Search parameter constant for <b>mode</b>
	 * <p>
	 * Description: <b>Mode - restful (server/client) or messaging (sender/receiver)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.rest.mode</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="mode", path="Conformance.rest.mode", description="Mode - restful (server/client) or messaging (sender/receiver)", type="token"  )
	public static final String SP_MODE = "mode";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>mode</b>
	 * <p>
	 * Description: <b>Mode - restful (server/client) or messaging (sender/receiver)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.rest.mode</b><br/>
	 * </p>
	 */
	public static final TokenClientParam MODE = new TokenClientParam(SP_MODE);

	/**
	 * Search parameter constant for <b>profile</b>
	 * <p>
	 * Description: <b>A profile id invoked in a conformance statement</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Conformance.rest.resource.profile</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="profile", path="Conformance.rest.resource.profile", description="A profile id invoked in a conformance statement", type="reference"  )
	public static final String SP_PROFILE = "profile";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>profile</b>
	 * <p>
	 * Description: <b>A profile id invoked in a conformance statement</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Conformance.rest.resource.profile</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PROFILE = new ReferenceClientParam(SP_PROFILE);

	/**
	 * Search parameter constant for <b>format</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.format</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="format", path="Conformance.format", description="", type="token"  )
	public static final String SP_FORMAT = "format";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>format</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.format</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FORMAT = new TokenClientParam(SP_FORMAT);

	/**
	 * Search parameter constant for <b>security</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.rest.security</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="security", path="Conformance.rest.security", description="", type="token"  )
	public static final String SP_SECURITY = "security";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>security</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Conformance.rest.security</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SECURITY = new TokenClientParam(SP_SECURITY);

	/**
	 * Search parameter constant for <b>supported-profile</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Conformance.profile</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="supported-profile", path="Conformance.profile", description="", type="reference"  )
	public static final String SP_SUPPORTED_PROFILE = "supported-profile";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>supported-profile</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Conformance.profile</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUPPORTED_PROFILE = new ReferenceClientParam(SP_SUPPORTED_PROFILE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("Conformance.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.description</b>".
	 */
	public static final Include INCLUDE_DESCRIPTION = new Include("Conformance.description");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.format</b>".
	 */
	public static final Include INCLUDE_FORMAT = new Include("Conformance.format");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("Conformance.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.messaging.event.code</b>".
	 */
	public static final Include INCLUDE_MESSAGING_EVENT_CODE = new Include("Conformance.messaging.event.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("Conformance.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.profile</b>".
	 */
	public static final Include INCLUDE_PROFILE = new Include("Conformance.profile");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.publisher</b>".
	 */
	public static final Include INCLUDE_PUBLISHER = new Include("Conformance.publisher");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.rest.mode</b>".
	 */
	public static final Include INCLUDE_REST_MODE = new Include("Conformance.rest.mode");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.rest.resource.profile</b>".
	 */
	public static final Include INCLUDE_REST_RESOURCE_PROFILE = new Include("Conformance.rest.resource.profile");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.rest.resource.type</b>".
	 */
	public static final Include INCLUDE_REST_RESOURCE_TYPE = new Include("Conformance.rest.resource.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.rest.security</b>".
	 */
	public static final Include INCLUDE_REST_SECURITY = new Include("Conformance.rest.security");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.software.name</b>".
	 */
	public static final Include INCLUDE_SOFTWARE_NAME = new Include("Conformance.software.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("Conformance.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.version</b>".
	 */
	public static final Include INCLUDE_VERSION = new Include("Conformance.version");


	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)"
	)
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language name identifying the conformance statement"
	)
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Name of Organization publishing this conformance statement"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactPointDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc."
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP"
	)
	private StringDt myDescription;
	
	@Child(name="status", type=CodeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="ConformanceStatementStatus",
		formalDefinition="The status of this conformance statement"
	)
	private BoundCodeDt<ConformanceStatementStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="date", type=DateTimeDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date  (and optionally time) when the conformance statement was published"
	)
	private DateTimeDt myDate;
	
	@Child(name="software", order=9, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Software that is covered by this conformance statement.  It is used when the profile describes the capabilities of a particular software version, independent of an installation."
	)
	private Software mySoftware;
	
	@Child(name="implementation", order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a specific implementation instance that is described by the conformance statement - i.e. a particular installation, rather than the capabilities of a software program"
	)
	private Implementation myImplementation;
	
	@Child(name="fhirVersion", type=IdDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The version of the FHIR specification on which this conformance statement is based"
	)
	private IdDt myFhirVersion;
	
	@Child(name="acceptUnknown", type=BooleanDt.class, order=12, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A flag that indicates whether the application accepts unknown elements as part of a resource."
	)
	private BooleanDt myAcceptUnknown;
	
	@Child(name="format", type=CodeDt.class, order=13, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="MimeType",
		formalDefinition="A list of the formats supported by this implementation"
	)
	private java.util.List<CodeDt> myFormat;
	
	@Child(name="profile", order=14, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dev.resource.Profile.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of resources, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile."
	)
	private java.util.List<ResourceReferenceDt> myProfile;
	
	@Child(name="rest", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A definition of the restful capabilities of the solution, if any"
	)
	private java.util.List<Rest> myRest;
	
	@Child(name="messaging", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A description of the messaging capabilities of the solution"
	)
	private java.util.List<Messaging> myMessaging;
	
	@Child(name="document", order=17, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A document definition"
	)
	private java.util.List<Document> myDocument;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myName,  myPublisher,  myTelecom,  myDescription,  myStatus,  myExperimental,  myDate,  mySoftware,  myImplementation,  myFhirVersion,  myAcceptUnknown,  myFormat,  myProfile,  myRest,  myMessaging,  myDocument);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myVersion, myName, myPublisher, myTelecom, myDescription, myStatus, myExperimental, myDate, mySoftware, myImplementation, myFhirVersion, myAcceptUnknown, myFormat, myProfile, myRest, myMessaging, myDocument);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public String getIdentifier() {  
		return getIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public Conformance setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public Conformance setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public Conformance setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public Conformance setVersion( String theString) {
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
     * A free text natural language name identifying the conformance statement
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
     * A free text natural language name identifying the conformance statement
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
     * A free text natural language name identifying the conformance statement
     * </p> 
	 */
	public Conformance setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the conformance statement
     * </p> 
	 */
	public Conformance setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>publisher</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Organization publishing this conformance statement
     * </p> 
	 */
	public StringDt getPublisherElement() {  
		if (myPublisher == null) {
			myPublisher = new StringDt();
		}
		return myPublisher;
	}

	
	/**
	 * Gets the value(s) for <b>publisher</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Organization publishing this conformance statement
     * </p> 
	 */
	public String getPublisher() {  
		return getPublisherElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>publisher</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Organization publishing this conformance statement
     * </p> 
	 */
	public Conformance setPublisher(StringDt theValue) {
		myPublisher = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>publisher</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name of Organization publishing this conformance statement
     * </p> 
	 */
	public Conformance setPublisher( String theString) {
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
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
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
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
	 */
	public Conformance setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
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
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
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
     * A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP
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
     * A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP
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
     * A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP
     * </p> 
	 */
	public Conformance setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP
     * </p> 
	 */
	public Conformance setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (ConformanceStatementStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this conformance statement
     * </p> 
	 */
	public BoundCodeDt<ConformanceStatementStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ConformanceStatementStatusEnum>(ConformanceStatementStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (ConformanceStatementStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this conformance statement
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (ConformanceStatementStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this conformance statement
     * </p> 
	 */
	public Conformance setStatus(BoundCodeDt<ConformanceStatementStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (ConformanceStatementStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this conformance statement
     * </p> 
	 */
	public Conformance setStatus(ConformanceStatementStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>experimental</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimentalElement() {  
		if (myExperimental == null) {
			myExperimental = new BooleanDt();
		}
		return myExperimental;
	}

	
	/**
	 * Gets the value(s) for <b>experimental</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public Boolean getExperimental() {  
		return getExperimentalElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>experimental</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public Conformance setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>experimental</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public Conformance setExperimental( boolean theBoolean) {
		myExperimental = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date  (and optionally time) when the conformance statement was published
     * </p> 
	 */
	public DateTimeDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date  (and optionally time) when the conformance statement was published
     * </p> 
	 */
	public Date getDate() {  
		return getDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date  (and optionally time) when the conformance statement was published
     * </p> 
	 */
	public Conformance setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date  (and optionally time) when the conformance statement was published
     * </p> 
	 */
	public Conformance setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date  (and optionally time) when the conformance statement was published
     * </p> 
	 */
	public Conformance setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>software</b> ().
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
	 * Sets the value(s) for <b>software</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Software that is covered by this conformance statement.  It is used when the profile describes the capabilities of a particular software version, independent of an installation.
     * </p> 
	 */
	public Conformance setSoftware(Software theValue) {
		mySoftware = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>implementation</b> ().
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
	 * Sets the value(s) for <b>implementation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific implementation instance that is described by the conformance statement - i.e. a particular installation, rather than the capabilities of a software program
     * </p> 
	 */
	public Conformance setImplementation(Implementation theValue) {
		myImplementation = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>fhirVersion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this conformance statement is based
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
     * The version of the FHIR specification on which this conformance statement is based
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
     * The version of the FHIR specification on which this conformance statement is based
     * </p> 
	 */
	public Conformance setFhirVersion(IdDt theValue) {
		myFhirVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>fhirVersion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this conformance statement is based
     * </p> 
	 */
	public Conformance setFhirVersion( String theId) {
		myFhirVersion = new IdDt(theId); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>acceptUnknown</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag that indicates whether the application accepts unknown elements as part of a resource.
     * </p> 
	 */
	public BooleanDt getAcceptUnknownElement() {  
		if (myAcceptUnknown == null) {
			myAcceptUnknown = new BooleanDt();
		}
		return myAcceptUnknown;
	}

	
	/**
	 * Gets the value(s) for <b>acceptUnknown</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag that indicates whether the application accepts unknown elements as part of a resource.
     * </p> 
	 */
	public Boolean getAcceptUnknown() {  
		return getAcceptUnknownElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>acceptUnknown</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A flag that indicates whether the application accepts unknown elements as part of a resource.
     * </p> 
	 */
	public Conformance setAcceptUnknown(BooleanDt theValue) {
		myAcceptUnknown = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>acceptUnknown</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A flag that indicates whether the application accepts unknown elements as part of a resource.
     * </p> 
	 */
	public Conformance setAcceptUnknown( boolean theBoolean) {
		myAcceptUnknown = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>format</b> (MimeType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the formats supported by this implementation
     * </p> 
	 */
	public java.util.List<CodeDt> getFormat() {  
		if (myFormat == null) {
			myFormat = new java.util.ArrayList<CodeDt>();
		}
		return myFormat;
	}

	/**
	 * Sets the value(s) for <b>format</b> (MimeType)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the formats supported by this implementation
     * </p> 
	 */
	public Conformance setFormat(java.util.List<CodeDt> theValue) {
		myFormat = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>format</b> (MimeType)
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
	 * Gets the first repetition for <b>format</b> (MimeType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the formats supported by this implementation
     * </p> 
	 */
	public CodeDt getFormatFirstRep() {
		if (getFormat().isEmpty()) {
			return addFormat();
		}
		return getFormat().get(0); 
	}
 	/**
	 * Adds a new value for <b>format</b> (MimeType)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the formats supported by this implementation
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Conformance addFormat( String theCode) {
		if (myFormat == null) {
			myFormat = new java.util.ArrayList<CodeDt>();
		}
		myFormat.add(new CodeDt(theCode));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>profile</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of resources, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getProfile() {  
		if (myProfile == null) {
			myProfile = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myProfile;
	}

	/**
	 * Sets the value(s) for <b>profile</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of resources, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile.
     * </p> 
	 */
	public Conformance setProfile(java.util.List<ResourceReferenceDt> theValue) {
		myProfile = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>profile</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of resources, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile.
     * </p> 
	 */
	public ResourceReferenceDt addProfile() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getProfile().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>rest</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of the restful capabilities of the solution, if any
     * </p> 
	 */
	public java.util.List<Rest> getRest() {  
		if (myRest == null) {
			myRest = new java.util.ArrayList<Rest>();
		}
		return myRest;
	}

	/**
	 * Sets the value(s) for <b>rest</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of the restful capabilities of the solution, if any
     * </p> 
	 */
	public Conformance setRest(java.util.List<Rest> theValue) {
		myRest = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>rest</b> ()
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
	 * Gets the first repetition for <b>rest</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of the restful capabilities of the solution, if any
     * </p> 
	 */
	public Rest getRestFirstRep() {
		if (getRest().isEmpty()) {
			return addRest();
		}
		return getRest().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>messaging</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the messaging capabilities of the solution
     * </p> 
	 */
	public java.util.List<Messaging> getMessaging() {  
		if (myMessaging == null) {
			myMessaging = new java.util.ArrayList<Messaging>();
		}
		return myMessaging;
	}

	/**
	 * Sets the value(s) for <b>messaging</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the messaging capabilities of the solution
     * </p> 
	 */
	public Conformance setMessaging(java.util.List<Messaging> theValue) {
		myMessaging = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>messaging</b> ()
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
	 * Gets the first repetition for <b>messaging</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the messaging capabilities of the solution
     * </p> 
	 */
	public Messaging getMessagingFirstRep() {
		if (getMessaging().isEmpty()) {
			return addMessaging();
		}
		return getMessaging().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>document</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A document definition
     * </p> 
	 */
	public java.util.List<Document> getDocument() {  
		if (myDocument == null) {
			myDocument = new java.util.ArrayList<Document>();
		}
		return myDocument;
	}

	/**
	 * Sets the value(s) for <b>document</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A document definition
     * </p> 
	 */
	public Conformance setDocument(java.util.List<Document> theValue) {
		myDocument = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>document</b> ()
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
	 * Gets the first repetition for <b>document</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A document definition
     * </p> 
	 */
	public Document getDocumentFirstRep() {
		if (getDocument().isEmpty()) {
			return addDocument();
		}
		return getDocument().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Conformance.software</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Software that is covered by this conformance statement.  It is used when the profile describes the capabilities of a particular software version, independent of an installation.
     * </p> 
	 */
	@Block()	
	public static class Software 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Name software is known by"
	)
	private StringDt myName;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The version identifier for the software covered by this statement"
	)
	private StringDt myVersion;
	
	@Child(name="releaseDate", type=DateTimeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Date this version of the software released"
	)
	private DateTimeDt myReleaseDate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myVersion,  myReleaseDate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myVersion, myReleaseDate);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name software is known by
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
     * Name software is known by
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
     * Name software is known by
     * </p> 
	 */
	public Software setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Name software is known by
     * </p> 
	 */
	public Software setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version identifier for the software covered by this statement
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version identifier for the software covered by this statement
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version identifier for the software covered by this statement
     * </p> 
	 */
	public Software setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version identifier for the software covered by this statement
     * </p> 
	 */
	public Software setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>releaseDate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public DateTimeDt getReleaseDateElement() {  
		if (myReleaseDate == null) {
			myReleaseDate = new DateTimeDt();
		}
		return myReleaseDate;
	}

	
	/**
	 * Gets the value(s) for <b>releaseDate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public Date getReleaseDate() {  
		return getReleaseDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>releaseDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public Software setReleaseDate(DateTimeDt theValue) {
		myReleaseDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>releaseDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public Software setReleaseDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myReleaseDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>releaseDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public Software setReleaseDateWithSecondsPrecision( Date theDate) {
		myReleaseDate = new DateTimeDt(theDate); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.implementation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific implementation instance that is described by the conformance statement - i.e. a particular installation, rather than the capabilities of a software program
     * </p> 
	 */
	@Block()	
	public static class Implementation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="description", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Information about the specific installation that this conformance statement relates to"
	)
	private StringDt myDescription;
	
	@Child(name="url", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces."
	)
	private UriDt myUrl;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myDescription,  myUrl);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myDescription, myUrl);
	}

	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the specific installation that this conformance statement relates to
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
     * Information about the specific installation that this conformance statement relates to
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
     * Information about the specific installation that this conformance statement relates to
     * </p> 
	 */
	public Implementation setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the specific installation that this conformance statement relates to
     * </p> 
	 */
	public Implementation setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
     * </p> 
	 */
	public UriDt getUrlElement() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	
	/**
	 * Gets the value(s) for <b>url</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
     * </p> 
	 */
	public URI getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
     * </p> 
	 */
	public Implementation setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
     * </p> 
	 */
	public Implementation setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.rest</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of the restful capabilities of the solution, if any
     * </p> 
	 */
	@Block()	
	public static class Rest 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="mode", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="RestfulConformanceMode",
		formalDefinition="Identifies whether this portion of the statement is describing ability to initiate or receive restful operations"
	)
	private BoundCodeDt<RestfulConformanceModeEnum> myMode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Information about the system's restful capabilities that apply across all applications, such as security"
	)
	private StringDt myDocumentation;
	
	@Child(name="security", order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Information about security of implementation"
	)
	private RestSecurity mySecurity;
	
	@Child(name="resource", order=3, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A specification of the restful capabilities of the solution for a specific resource type"
	)
	private java.util.List<RestResource> myResource;
	
	@Child(name="interaction", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A specification of restful operations supported by the system"
	)
	private java.util.List<RestInteraction> myInteraction;
	
	@Child(name="operation", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Definition of an operation or a named query and with its parameters and their meaning and type"
	)
	private java.util.List<RestOperation> myOperation;
	
	@Child(name="documentMailbox", type=UriDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose"
	)
	private java.util.List<UriDt> myDocumentMailbox;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMode,  myDocumentation,  mySecurity,  myResource,  myInteraction,  myOperation,  myDocumentMailbox);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMode, myDocumentation, mySecurity, myResource, myInteraction, myOperation, myDocumentMailbox);
	}

	/**
	 * Gets the value(s) for <b>mode</b> (RestfulConformanceMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies whether this portion of the statement is describing ability to initiate or receive restful operations
     * </p> 
	 */
	public BoundCodeDt<RestfulConformanceModeEnum> getModeElement() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<RestfulConformanceModeEnum>(RestfulConformanceModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	
	/**
	 * Gets the value(s) for <b>mode</b> (RestfulConformanceMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies whether this portion of the statement is describing ability to initiate or receive restful operations
     * </p> 
	 */
	public String getMode() {  
		return getModeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>mode</b> (RestfulConformanceMode)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies whether this portion of the statement is describing ability to initiate or receive restful operations
     * </p> 
	 */
	public Rest setMode(BoundCodeDt<RestfulConformanceModeEnum> theValue) {
		myMode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>mode</b> (RestfulConformanceMode)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies whether this portion of the statement is describing ability to initiate or receive restful operations
     * </p> 
	 */
	public Rest setMode(RestfulConformanceModeEnum theValue) {
		getModeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the system's restful capabilities that apply across all applications, such as security
     * </p> 
	 */
	public StringDt getDocumentationElement() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the system's restful capabilities that apply across all applications, such as security
     * </p> 
	 */
	public String getDocumentation() {  
		return getDocumentationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the system's restful capabilities that apply across all applications, such as security
     * </p> 
	 */
	public Rest setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the system's restful capabilities that apply across all applications, such as security
     * </p> 
	 */
	public Rest setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>security</b> ().
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
	 * Sets the value(s) for <b>security</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about security of implementation
     * </p> 
	 */
	public Rest setSecurity(RestSecurity theValue) {
		mySecurity = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>resource</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the restful capabilities of the solution for a specific resource type
     * </p> 
	 */
	public java.util.List<RestResource> getResource() {  
		if (myResource == null) {
			myResource = new java.util.ArrayList<RestResource>();
		}
		return myResource;
	}

	/**
	 * Sets the value(s) for <b>resource</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the restful capabilities of the solution for a specific resource type
     * </p> 
	 */
	public Rest setResource(java.util.List<RestResource> theValue) {
		myResource = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>resource</b> ()
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
	 * Gets the first repetition for <b>resource</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the restful capabilities of the solution for a specific resource type
     * </p> 
	 */
	public RestResource getResourceFirstRep() {
		if (getResource().isEmpty()) {
			return addResource();
		}
		return getResource().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>interaction</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public java.util.List<RestInteraction> getInteraction() {  
		if (myInteraction == null) {
			myInteraction = new java.util.ArrayList<RestInteraction>();
		}
		return myInteraction;
	}

	/**
	 * Sets the value(s) for <b>interaction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public Rest setInteraction(java.util.List<RestInteraction> theValue) {
		myInteraction = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>interaction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public RestInteraction addInteraction() {
		RestInteraction newType = new RestInteraction();
		getInteraction().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>interaction</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public RestInteraction getInteractionFirstRep() {
		if (getInteraction().isEmpty()) {
			return addInteraction();
		}
		return getInteraction().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>operation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of an operation or a named query and with its parameters and their meaning and type
     * </p> 
	 */
	public java.util.List<RestOperation> getOperation() {  
		if (myOperation == null) {
			myOperation = new java.util.ArrayList<RestOperation>();
		}
		return myOperation;
	}

	/**
	 * Sets the value(s) for <b>operation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of an operation or a named query and with its parameters and their meaning and type
     * </p> 
	 */
	public Rest setOperation(java.util.List<RestOperation> theValue) {
		myOperation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>operation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of an operation or a named query and with its parameters and their meaning and type
     * </p> 
	 */
	public RestOperation addOperation() {
		RestOperation newType = new RestOperation();
		getOperation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>operation</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of an operation or a named query and with its parameters and their meaning and type
     * </p> 
	 */
	public RestOperation getOperationFirstRep() {
		if (getOperation().isEmpty()) {
			return addOperation();
		}
		return getOperation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>documentMailbox</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose
     * </p> 
	 */
	public java.util.List<UriDt> getDocumentMailbox() {  
		if (myDocumentMailbox == null) {
			myDocumentMailbox = new java.util.ArrayList<UriDt>();
		}
		return myDocumentMailbox;
	}

	/**
	 * Sets the value(s) for <b>documentMailbox</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose
     * </p> 
	 */
	public Rest setDocumentMailbox(java.util.List<UriDt> theValue) {
		myDocumentMailbox = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>documentMailbox</b> ()
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
	 * Gets the first repetition for <b>documentMailbox</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose
     * </p> 
	 */
	public UriDt getDocumentMailboxFirstRep() {
		if (getDocumentMailbox().isEmpty()) {
			return addDocumentMailbox();
		}
		return getDocumentMailbox().get(0); 
	}
 	/**
	 * Adds a new value for <b>documentMailbox</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Rest addDocumentMailbox( String theUri) {
		if (myDocumentMailbox == null) {
			myDocumentMailbox = new java.util.ArrayList<UriDt>();
		}
		myDocumentMailbox.add(new UriDt(theUri));
		return this; 
	}

 

	}

	/**
	 * Block class for child element: <b>Conformance.rest.security</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about security of implementation
     * </p> 
	 */
	@Block()	
	public static class RestSecurity 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="cors", type=BooleanDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Server adds CORS headers when responding to requests - this enables javascript applications to use the server"
	)
	private BooleanDt myCors;
	
	@Child(name="service", type=CodeableConceptDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="RestfulSecurityService",
		formalDefinition="Types of security services are supported/required by the system"
	)
	private java.util.List<BoundCodeableConceptDt<RestfulSecurityServiceEnum>> myService;
	
	@Child(name="description", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="General description of how security works"
	)
	private StringDt myDescription;
	
	@Child(name="certificate", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Certificates associated with security profiles"
	)
	private java.util.List<RestSecurityCertificate> myCertificate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCors,  myService,  myDescription,  myCertificate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCors, myService, myDescription, myCertificate);
	}

	/**
	 * Gets the value(s) for <b>cors</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to use the server
     * </p> 
	 */
	public BooleanDt getCorsElement() {  
		if (myCors == null) {
			myCors = new BooleanDt();
		}
		return myCors;
	}

	
	/**
	 * Gets the value(s) for <b>cors</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to use the server
     * </p> 
	 */
	public Boolean getCors() {  
		return getCorsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>cors</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to use the server
     * </p> 
	 */
	public RestSecurity setCors(BooleanDt theValue) {
		myCors = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>cors</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to use the server
     * </p> 
	 */
	public RestSecurity setCors( boolean theBoolean) {
		myCors = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>service</b> (RestfulSecurityService).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt<RestfulSecurityServiceEnum>> getService() {  
		if (myService == null) {
			myService = new java.util.ArrayList<BoundCodeableConceptDt<RestfulSecurityServiceEnum>>();
		}
		return myService;
	}

	/**
	 * Sets the value(s) for <b>service</b> (RestfulSecurityService)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public RestSecurity setService(java.util.List<BoundCodeableConceptDt<RestfulSecurityServiceEnum>> theValue) {
		myService = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>service</b> (RestfulSecurityService) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public BoundCodeableConceptDt<RestfulSecurityServiceEnum> addService(RestfulSecurityServiceEnum theValue) {
		BoundCodeableConceptDt<RestfulSecurityServiceEnum> retVal = new BoundCodeableConceptDt<RestfulSecurityServiceEnum>(RestfulSecurityServiceEnum.VALUESET_BINDER, theValue);
		getService().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>service</b> (RestfulSecurityService),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public BoundCodeableConceptDt<RestfulSecurityServiceEnum> getServiceFirstRep() {
		if (getService().size() == 0) {
			addService();
		}
		return getService().get(0);
	}

	/**
	 * Add a value for <b>service</b> (RestfulSecurityService)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public BoundCodeableConceptDt<RestfulSecurityServiceEnum> addService() {
		BoundCodeableConceptDt<RestfulSecurityServiceEnum> retVal = new BoundCodeableConceptDt<RestfulSecurityServiceEnum>(RestfulSecurityServiceEnum.VALUESET_BINDER);
		getService().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>service</b> (RestfulSecurityService)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public RestSecurity setService(RestfulSecurityServiceEnum theValue) {
		getService().clear();
		addService(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * General description of how security works
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
     * General description of how security works
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
     * General description of how security works
     * </p> 
	 */
	public RestSecurity setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * General description of how security works
     * </p> 
	 */
	public RestSecurity setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>certificate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Certificates associated with security profiles
     * </p> 
	 */
	public java.util.List<RestSecurityCertificate> getCertificate() {  
		if (myCertificate == null) {
			myCertificate = new java.util.ArrayList<RestSecurityCertificate>();
		}
		return myCertificate;
	}

	/**
	 * Sets the value(s) for <b>certificate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Certificates associated with security profiles
     * </p> 
	 */
	public RestSecurity setCertificate(java.util.List<RestSecurityCertificate> theValue) {
		myCertificate = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>certificate</b> ()
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

	/**
	 * Gets the first repetition for <b>certificate</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Certificates associated with security profiles
     * </p> 
	 */
	public RestSecurityCertificate getCertificateFirstRep() {
		if (getCertificate().isEmpty()) {
			return addCertificate();
		}
		return getCertificate().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>Conformance.rest.security.certificate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Certificates associated with security profiles
     * </p> 
	 */
	@Block()	
	public static class RestSecurityCertificate 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="MimeType",
		formalDefinition="Mime type for certificate"
	)
	private CodeDt myType;
	
	@Child(name="blob", type=Base64BinaryDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Actual certificate"
	)
	private Base64BinaryDt myBlob;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myBlob);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myBlob);
	}

	/**
	 * Gets the value(s) for <b>type</b> (MimeType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Mime type for certificate
     * </p> 
	 */
	public CodeDt getTypeElement() {  
		if (myType == null) {
			myType = new CodeDt();
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (MimeType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Mime type for certificate
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (MimeType)
	 *
     * <p>
     * <b>Definition:</b>
     * Mime type for certificate
     * </p> 
	 */
	public RestSecurityCertificate setType(CodeDt theValue) {
		myType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>type</b> (MimeType)
	 *
     * <p>
     * <b>Definition:</b>
     * Mime type for certificate
     * </p> 
	 */
	public RestSecurityCertificate setType( String theCode) {
		myType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>blob</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Actual certificate
     * </p> 
	 */
	public Base64BinaryDt getBlobElement() {  
		if (myBlob == null) {
			myBlob = new Base64BinaryDt();
		}
		return myBlob;
	}

	
	/**
	 * Gets the value(s) for <b>blob</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Actual certificate
     * </p> 
	 */
	public byte[] getBlob() {  
		return getBlobElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>blob</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Actual certificate
     * </p> 
	 */
	public RestSecurityCertificate setBlob(Base64BinaryDt theValue) {
		myBlob = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>blob</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Actual certificate
     * </p> 
	 */
	public RestSecurityCertificate setBlob( byte[] theBytes) {
		myBlob = new Base64BinaryDt(theBytes); 
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>Conformance.rest.resource</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the restful capabilities of the solution for a specific resource type
     * </p> 
	 */
	@Block()	
	public static class RestResource 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ResourceType",
		formalDefinition="A type of resource exposed via the restful interface"
	)
	private BoundCodeDt<ResourceTypeEnum> myType;
	
	@Child(name="profile", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Profile.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A specification of the profile that describes the solution's support for the resource, including any constraints on cardinality, bindings, lengths or other limitations"
	)
	private ResourceReferenceDt myProfile;
	
	@Child(name="interaction", order=2, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a restful operation supported by the solution"
	)
	private java.util.List<RestResourceInteraction> myInteraction;
	
	@Child(name="versioning", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="ResourceVersionPolicy",
		formalDefinition="Thi field is set to true to specify that the system does not support (server) or use (client) versioning for this resource type. If this is not set to true, the server must at least correctly track and populate the versionId meta-property on resources"
	)
	private BoundCodeDt<ResourceVersionPolicyEnum> myVersioning;
	
	@Child(name="readHistory", type=BooleanDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A flag for whether the server is able to return past versions as part of the vRead operation"
	)
	private BooleanDt myReadHistory;
	
	@Child(name="updateCreate", type=BooleanDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server"
	)
	private BooleanDt myUpdateCreate;
	
	@Child(name="searchInclude", type=StringDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A list of _include values supported by the server"
	)
	private java.util.List<StringDt> mySearchInclude;
	
	@Child(name="searchParam", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional search parameters for implementations to support and/or make use of"
	)
	private java.util.List<RestResourceSearchParam> mySearchParam;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myProfile,  myInteraction,  myVersioning,  myReadHistory,  myUpdateCreate,  mySearchInclude,  mySearchParam);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myProfile, myInteraction, myVersioning, myReadHistory, myUpdateCreate, mySearchInclude, mySearchParam);
	}

	/**
	 * Gets the value(s) for <b>type</b> (ResourceType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A type of resource exposed via the restful interface
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (ResourceType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A type of resource exposed via the restful interface
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (ResourceType)
	 *
     * <p>
     * <b>Definition:</b>
     * A type of resource exposed via the restful interface
     * </p> 
	 */
	public RestResource setType(BoundCodeDt<ResourceTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (ResourceType)
	 *
     * <p>
     * <b>Definition:</b>
     * A type of resource exposed via the restful interface
     * </p> 
	 */
	public RestResource setType(ResourceTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>profile</b> ().
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
	 * Sets the value(s) for <b>profile</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the profile that describes the solution's support for the resource, including any constraints on cardinality, bindings, lengths or other limitations
     * </p> 
	 */
	public RestResource setProfile(ResourceReferenceDt theValue) {
		myProfile = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>interaction</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public java.util.List<RestResourceInteraction> getInteraction() {  
		if (myInteraction == null) {
			myInteraction = new java.util.ArrayList<RestResourceInteraction>();
		}
		return myInteraction;
	}

	/**
	 * Sets the value(s) for <b>interaction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public RestResource setInteraction(java.util.List<RestResourceInteraction> theValue) {
		myInteraction = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>interaction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public RestResourceInteraction addInteraction() {
		RestResourceInteraction newType = new RestResourceInteraction();
		getInteraction().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>interaction</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public RestResourceInteraction getInteractionFirstRep() {
		if (getInteraction().isEmpty()) {
			return addInteraction();
		}
		return getInteraction().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>versioning</b> (ResourceVersionPolicy).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Thi field is set to true to specify that the system does not support (server) or use (client) versioning for this resource type. If this is not set to true, the server must at least correctly track and populate the versionId meta-property on resources
     * </p> 
	 */
	public BoundCodeDt<ResourceVersionPolicyEnum> getVersioningElement() {  
		if (myVersioning == null) {
			myVersioning = new BoundCodeDt<ResourceVersionPolicyEnum>(ResourceVersionPolicyEnum.VALUESET_BINDER);
		}
		return myVersioning;
	}

	
	/**
	 * Gets the value(s) for <b>versioning</b> (ResourceVersionPolicy).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Thi field is set to true to specify that the system does not support (server) or use (client) versioning for this resource type. If this is not set to true, the server must at least correctly track and populate the versionId meta-property on resources
     * </p> 
	 */
	public String getVersioning() {  
		return getVersioningElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>versioning</b> (ResourceVersionPolicy)
	 *
     * <p>
     * <b>Definition:</b>
     * Thi field is set to true to specify that the system does not support (server) or use (client) versioning for this resource type. If this is not set to true, the server must at least correctly track and populate the versionId meta-property on resources
     * </p> 
	 */
	public RestResource setVersioning(BoundCodeDt<ResourceVersionPolicyEnum> theValue) {
		myVersioning = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>versioning</b> (ResourceVersionPolicy)
	 *
     * <p>
     * <b>Definition:</b>
     * Thi field is set to true to specify that the system does not support (server) or use (client) versioning for this resource type. If this is not set to true, the server must at least correctly track and populate the versionId meta-property on resources
     * </p> 
	 */
	public RestResource setVersioning(ResourceVersionPolicyEnum theValue) {
		getVersioningElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>readHistory</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag for whether the server is able to return past versions as part of the vRead operation
     * </p> 
	 */
	public BooleanDt getReadHistoryElement() {  
		if (myReadHistory == null) {
			myReadHistory = new BooleanDt();
		}
		return myReadHistory;
	}

	
	/**
	 * Gets the value(s) for <b>readHistory</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag for whether the server is able to return past versions as part of the vRead operation
     * </p> 
	 */
	public Boolean getReadHistory() {  
		return getReadHistoryElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>readHistory</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A flag for whether the server is able to return past versions as part of the vRead operation
     * </p> 
	 */
	public RestResource setReadHistory(BooleanDt theValue) {
		myReadHistory = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>readHistory</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A flag for whether the server is able to return past versions as part of the vRead operation
     * </p> 
	 */
	public RestResource setReadHistory( boolean theBoolean) {
		myReadHistory = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>updateCreate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server
     * </p> 
	 */
	public BooleanDt getUpdateCreateElement() {  
		if (myUpdateCreate == null) {
			myUpdateCreate = new BooleanDt();
		}
		return myUpdateCreate;
	}

	
	/**
	 * Gets the value(s) for <b>updateCreate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server
     * </p> 
	 */
	public Boolean getUpdateCreate() {  
		return getUpdateCreateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>updateCreate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server
     * </p> 
	 */
	public RestResource setUpdateCreate(BooleanDt theValue) {
		myUpdateCreate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>updateCreate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server
     * </p> 
	 */
	public RestResource setUpdateCreate( boolean theBoolean) {
		myUpdateCreate = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>searchInclude</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of _include values supported by the server
     * </p> 
	 */
	public java.util.List<StringDt> getSearchInclude() {  
		if (mySearchInclude == null) {
			mySearchInclude = new java.util.ArrayList<StringDt>();
		}
		return mySearchInclude;
	}

	/**
	 * Sets the value(s) for <b>searchInclude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of _include values supported by the server
     * </p> 
	 */
	public RestResource setSearchInclude(java.util.List<StringDt> theValue) {
		mySearchInclude = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>searchInclude</b> ()
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
	 * Gets the first repetition for <b>searchInclude</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of _include values supported by the server
     * </p> 
	 */
	public StringDt getSearchIncludeFirstRep() {
		if (getSearchInclude().isEmpty()) {
			return addSearchInclude();
		}
		return getSearchInclude().get(0); 
	}
 	/**
	 * Adds a new value for <b>searchInclude</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A list of _include values supported by the server
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public RestResource addSearchInclude( String theString) {
		if (mySearchInclude == null) {
			mySearchInclude = new java.util.ArrayList<StringDt>();
		}
		mySearchInclude.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>searchParam</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public java.util.List<RestResourceSearchParam> getSearchParam() {  
		if (mySearchParam == null) {
			mySearchParam = new java.util.ArrayList<RestResourceSearchParam>();
		}
		return mySearchParam;
	}

	/**
	 * Sets the value(s) for <b>searchParam</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public RestResource setSearchParam(java.util.List<RestResourceSearchParam> theValue) {
		mySearchParam = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>searchParam</b> ()
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

	/**
	 * Gets the first repetition for <b>searchParam</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	public RestResourceSearchParam getSearchParamFirstRep() {
		if (getSearchParam().isEmpty()) {
			return addSearchParam();
		}
		return getSearchParam().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>Conformance.rest.resource.interaction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	@Block()	
	public static class RestResourceInteraction 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="TypeRestfulInteraction",
		formalDefinition="Coded identifier of the operation, supported by the system resource"
	)
	private BoundCodeDt<TypeRestfulInteractionEnum> myCode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'"
	)
	private StringDt myDocumentation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDocumentation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myDocumentation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (TypeRestfulInteraction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public BoundCodeDt<TypeRestfulInteractionEnum> getCodeElement() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<TypeRestfulInteractionEnum>(TypeRestfulInteractionEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (TypeRestfulInteraction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (TypeRestfulInteraction)
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public RestResourceInteraction setCode(BoundCodeDt<TypeRestfulInteractionEnum> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>code</b> (TypeRestfulInteraction)
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public RestResourceInteraction setCode(TypeRestfulInteractionEnum theValue) {
		getCodeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public StringDt getDocumentationElement() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public String getDocumentation() {  
		return getDocumentationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public RestResourceInteraction setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public RestResourceInteraction setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.rest.resource.searchParam</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	@Block()	
	public static class RestResourceSearchParam 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The name of the search parameter used in the interface"
	)
	private StringDt myName;
	
	@Child(name="definition", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter"
	)
	private UriDt myDefinition;
	
	@Child(name="type", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="SearchParamType",
		formalDefinition="The type of value a search parameter refers to, and how the content is interpreted"
	)
	private BoundCodeDt<SearchParamTypeEnum> myType;
	
	@Child(name="documentation", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms."
	)
	private StringDt myDocumentation;
	
	@Child(name="target", type=CodeDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ResourceType",
		formalDefinition="Types of resource (if a resource is referenced)"
	)
	private java.util.List<BoundCodeDt<ResourceTypeEnum>> myTarget;
	
	@Child(name="chain", type=StringDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<StringDt> myChain;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myDefinition,  myType,  myDocumentation,  myTarget,  myChain);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myDefinition, myType, myDocumentation, myTarget, myChain);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the search parameter used in the interface
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
     * The name of the search parameter used in the interface
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
     * The name of the search parameter used in the interface
     * </p> 
	 */
	public RestResourceSearchParam setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the search parameter used in the interface
     * </p> 
	 */
	public RestResourceSearchParam setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter
     * </p> 
	 */
	public UriDt getDefinitionElement() {  
		if (myDefinition == null) {
			myDefinition = new UriDt();
		}
		return myDefinition;
	}

	
	/**
	 * Gets the value(s) for <b>definition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter
     * </p> 
	 */
	public URI getDefinition() {  
		return getDefinitionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>definition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter
     * </p> 
	 */
	public RestResourceSearchParam setDefinition(UriDt theValue) {
		myDefinition = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>definition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter
     * </p> 
	 */
	public RestResourceSearchParam setDefinition( String theUri) {
		myDefinition = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (SearchParamType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public BoundCodeDt<SearchParamTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<SearchParamTypeEnum>(SearchParamTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (SearchParamType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (SearchParamType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public RestResourceSearchParam setType(BoundCodeDt<SearchParamTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (SearchParamType)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public RestResourceSearchParam setType(SearchParamTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
     * </p> 
	 */
	public StringDt getDocumentationElement() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
     * </p> 
	 */
	public String getDocumentation() {  
		return getDocumentationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
     * </p> 
	 */
	public RestResourceSearchParam setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
     * </p> 
	 */
	public RestResourceSearchParam setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>target</b> (ResourceType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public java.util.List<BoundCodeDt<ResourceTypeEnum>> getTarget() {  
		if (myTarget == null) {
			myTarget = new java.util.ArrayList<BoundCodeDt<ResourceTypeEnum>>();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (ResourceType)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public RestResourceSearchParam setTarget(java.util.List<BoundCodeDt<ResourceTypeEnum>> theValue) {
		myTarget = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>target</b> (ResourceType) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> addTarget(ResourceTypeEnum theValue) {
		BoundCodeDt<ResourceTypeEnum> retVal = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER, theValue);
		getTarget().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>target</b> (ResourceType),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> getTargetFirstRep() {
		if (getTarget().size() == 0) {
			addTarget();
		}
		return getTarget().get(0);
	}

	/**
	 * Add a value for <b>target</b> (ResourceType)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> addTarget() {
		BoundCodeDt<ResourceTypeEnum> retVal = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER);
		getTarget().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>target</b> (ResourceType)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of resource (if a resource is referenced)
     * </p> 
	 */
	public RestResourceSearchParam setTarget(ResourceTypeEnum theValue) {
		getTarget().clear();
		addTarget(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>chain</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<StringDt> getChain() {  
		if (myChain == null) {
			myChain = new java.util.ArrayList<StringDt>();
		}
		return myChain;
	}

	/**
	 * Sets the value(s) for <b>chain</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public RestResourceSearchParam setChain(java.util.List<StringDt> theValue) {
		myChain = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>chain</b> ()
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
	 * Gets the first repetition for <b>chain</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getChainFirstRep() {
		if (getChain().isEmpty()) {
			return addChain();
		}
		return getChain().get(0); 
	}
 	/**
	 * Adds a new value for <b>chain</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public RestResourceSearchParam addChain( String theString) {
		if (myChain == null) {
			myChain = new java.util.ArrayList<StringDt>();
		}
		myChain.add(new StringDt(theString));
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>Conformance.rest.interaction</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	@Block()	
	public static class RestInteraction 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="SystemRestfulInteraction",
		formalDefinition="A coded identifier of the operation, supported by the system"
	)
	private BoundCodeDt<SystemRestfulInteractionEnum> myCode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented"
	)
	private StringDt myDocumentation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDocumentation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myDocumentation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (SystemRestfulInteraction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public BoundCodeDt<SystemRestfulInteractionEnum> getCodeElement() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<SystemRestfulInteractionEnum>(SystemRestfulInteractionEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (SystemRestfulInteraction).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (SystemRestfulInteraction)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public RestInteraction setCode(BoundCodeDt<SystemRestfulInteractionEnum> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>code</b> (SystemRestfulInteraction)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public RestInteraction setCode(SystemRestfulInteractionEnum theValue) {
		getCodeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public StringDt getDocumentationElement() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public String getDocumentation() {  
		return getDocumentationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public RestInteraction setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public RestInteraction setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.rest.operation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of an operation or a named query and with its parameters and their meaning and type
     * </p> 
	 */
	@Block()	
	public static class RestOperation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The name of a query, which is used in the _query parameter when the query is called"
	)
	private StringDt myName;
	
	@Child(name="definition", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.OperationDefinition.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Where the formal definition can be found"
	)
	private ResourceReferenceDt myDefinition;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myDefinition);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myDefinition);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the _query parameter when the query is called
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
     * The name of a query, which is used in the _query parameter when the query is called
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
     * The name of a query, which is used in the _query parameter when the query is called
     * </p> 
	 */
	public RestOperation setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the _query parameter when the query is called
     * </p> 
	 */
	public RestOperation setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Where the formal definition can be found
     * </p> 
	 */
	public ResourceReferenceDt getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new ResourceReferenceDt();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Where the formal definition can be found
     * </p> 
	 */
	public RestOperation setDefinition(ResourceReferenceDt theValue) {
		myDefinition = theValue;
		return this;
	}
	
	

  

	}



	/**
	 * Block class for child element: <b>Conformance.messaging</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the messaging capabilities of the solution
     * </p> 
	 */
	@Block()	
	public static class Messaging 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="endpoint", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An address to which messages and/or replies are to be sent."
	)
	private UriDt myEndpoint;
	
	@Child(name="reliableCache", type=IntegerDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender)"
	)
	private IntegerDt myReliableCache;
	
	@Child(name="documentation", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner."
	)
	private StringDt myDocumentation;
	
	@Child(name="event", order=3, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A description of the solution's support for an event at this end point."
	)
	private java.util.List<MessagingEvent> myEvent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myEndpoint,  myReliableCache,  myDocumentation,  myEvent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myEndpoint, myReliableCache, myDocumentation, myEvent);
	}

	/**
	 * Gets the value(s) for <b>endpoint</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An address to which messages and/or replies are to be sent.
     * </p> 
	 */
	public UriDt getEndpointElement() {  
		if (myEndpoint == null) {
			myEndpoint = new UriDt();
		}
		return myEndpoint;
	}

	
	/**
	 * Gets the value(s) for <b>endpoint</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An address to which messages and/or replies are to be sent.
     * </p> 
	 */
	public URI getEndpoint() {  
		return getEndpointElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>endpoint</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An address to which messages and/or replies are to be sent.
     * </p> 
	 */
	public Messaging setEndpoint(UriDt theValue) {
		myEndpoint = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>endpoint</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An address to which messages and/or replies are to be sent.
     * </p> 
	 */
	public Messaging setEndpoint( String theUri) {
		myEndpoint = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reliableCache</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public IntegerDt getReliableCacheElement() {  
		if (myReliableCache == null) {
			myReliableCache = new IntegerDt();
		}
		return myReliableCache;
	}

	
	/**
	 * Gets the value(s) for <b>reliableCache</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public Integer getReliableCache() {  
		return getReliableCacheElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>reliableCache</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public Messaging setReliableCache(IntegerDt theValue) {
		myReliableCache = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>reliableCache</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public Messaging setReliableCache( int theInteger) {
		myReliableCache = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner.
     * </p> 
	 */
	public StringDt getDocumentationElement() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner.
     * </p> 
	 */
	public String getDocumentation() {  
		return getDocumentationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner.
     * </p> 
	 */
	public Messaging setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner.
     * </p> 
	 */
	public Messaging setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>event</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the solution's support for an event at this end point.
     * </p> 
	 */
	public java.util.List<MessagingEvent> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<MessagingEvent>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the solution's support for an event at this end point.
     * </p> 
	 */
	public Messaging setEvent(java.util.List<MessagingEvent> theValue) {
		myEvent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>event</b> ()
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

	/**
	 * Gets the first repetition for <b>event</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the solution's support for an event at this end point.
     * </p> 
	 */
	public MessagingEvent getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>Conformance.messaging.event</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the solution's support for an event at this end point.
     * </p> 
	 */
	@Block()	
	public static class MessagingEvent 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodingDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="MessageEvent",
		formalDefinition="A coded identifier of a supported messaging event"
	)
	private CodingDt myCode;
	
	@Child(name="category", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="MessageSignificanceCategory",
		formalDefinition="The impact of the content of the message"
	)
	private BoundCodeDt<MessageSignificanceCategoryEnum> myCategory;
	
	@Child(name="mode", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="ConformanceEventMode",
		formalDefinition="The mode of this event declaration - whether application is sender or receiver"
	)
	private BoundCodeDt<ConformanceEventModeEnum> myMode;
	
	@Child(name="protocol", type=CodingDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="MessageTransport",
		formalDefinition="A list of the messaging transport protocol(s) identifiers, supported by this endpoint"
	)
	private java.util.List<CodingDt> myProtocol;
	
	@Child(name="focus", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="ResourceType",
		formalDefinition="A resource associated with the event.  This is the resource that defines the event."
	)
	private BoundCodeDt<ResourceTypeEnum> myFocus;
	
	@Child(name="request", order=5, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Profile.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Information about the request for this event"
	)
	private ResourceReferenceDt myRequest;
	
	@Child(name="response", order=6, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Profile.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Information about the response for this event"
	)
	private ResourceReferenceDt myResponse;
	
	@Child(name="documentation", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Guidance on how this event is handled, such as internal system trigger points, business rules, etc."
	)
	private StringDt myDocumentation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myCategory,  myMode,  myProtocol,  myFocus,  myRequest,  myResponse,  myDocumentation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myCategory, myMode, myProtocol, myFocus, myRequest, myResponse, myDocumentation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (MessageEvent).
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
	 * Sets the value(s) for <b>code</b> (MessageEvent)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of a supported messaging event
     * </p> 
	 */
	public MessagingEvent setCode(CodingDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>category</b> (MessageSignificanceCategory).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The impact of the content of the message
     * </p> 
	 */
	public BoundCodeDt<MessageSignificanceCategoryEnum> getCategoryElement() {  
		if (myCategory == null) {
			myCategory = new BoundCodeDt<MessageSignificanceCategoryEnum>(MessageSignificanceCategoryEnum.VALUESET_BINDER);
		}
		return myCategory;
	}

	
	/**
	 * Gets the value(s) for <b>category</b> (MessageSignificanceCategory).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The impact of the content of the message
     * </p> 
	 */
	public String getCategory() {  
		return getCategoryElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>category</b> (MessageSignificanceCategory)
	 *
     * <p>
     * <b>Definition:</b>
     * The impact of the content of the message
     * </p> 
	 */
	public MessagingEvent setCategory(BoundCodeDt<MessageSignificanceCategoryEnum> theValue) {
		myCategory = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>category</b> (MessageSignificanceCategory)
	 *
     * <p>
     * <b>Definition:</b>
     * The impact of the content of the message
     * </p> 
	 */
	public MessagingEvent setCategory(MessageSignificanceCategoryEnum theValue) {
		getCategoryElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>mode</b> (ConformanceEventMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mode of this event declaration - whether application is sender or receiver
     * </p> 
	 */
	public BoundCodeDt<ConformanceEventModeEnum> getModeElement() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<ConformanceEventModeEnum>(ConformanceEventModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	
	/**
	 * Gets the value(s) for <b>mode</b> (ConformanceEventMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mode of this event declaration - whether application is sender or receiver
     * </p> 
	 */
	public String getMode() {  
		return getModeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>mode</b> (ConformanceEventMode)
	 *
     * <p>
     * <b>Definition:</b>
     * The mode of this event declaration - whether application is sender or receiver
     * </p> 
	 */
	public MessagingEvent setMode(BoundCodeDt<ConformanceEventModeEnum> theValue) {
		myMode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>mode</b> (ConformanceEventMode)
	 *
     * <p>
     * <b>Definition:</b>
     * The mode of this event declaration - whether application is sender or receiver
     * </p> 
	 */
	public MessagingEvent setMode(ConformanceEventModeEnum theValue) {
		getModeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>protocol</b> (MessageTransport).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the messaging transport protocol(s) identifiers, supported by this endpoint
     * </p> 
	 */
	public java.util.List<CodingDt> getProtocol() {  
		if (myProtocol == null) {
			myProtocol = new java.util.ArrayList<CodingDt>();
		}
		return myProtocol;
	}

	/**
	 * Sets the value(s) for <b>protocol</b> (MessageTransport)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the messaging transport protocol(s) identifiers, supported by this endpoint
     * </p> 
	 */
	public MessagingEvent setProtocol(java.util.List<CodingDt> theValue) {
		myProtocol = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>protocol</b> (MessageTransport)
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
	 * Gets the first repetition for <b>protocol</b> (MessageTransport),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of the messaging transport protocol(s) identifiers, supported by this endpoint
     * </p> 
	 */
	public CodingDt getProtocolFirstRep() {
		if (getProtocol().isEmpty()) {
			return addProtocol();
		}
		return getProtocol().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>focus</b> (ResourceType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A resource associated with the event.  This is the resource that defines the event.
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> getFocusElement() {  
		if (myFocus == null) {
			myFocus = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER);
		}
		return myFocus;
	}

	
	/**
	 * Gets the value(s) for <b>focus</b> (ResourceType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A resource associated with the event.  This is the resource that defines the event.
     * </p> 
	 */
	public String getFocus() {  
		return getFocusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>focus</b> (ResourceType)
	 *
     * <p>
     * <b>Definition:</b>
     * A resource associated with the event.  This is the resource that defines the event.
     * </p> 
	 */
	public MessagingEvent setFocus(BoundCodeDt<ResourceTypeEnum> theValue) {
		myFocus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>focus</b> (ResourceType)
	 *
     * <p>
     * <b>Definition:</b>
     * A resource associated with the event.  This is the resource that defines the event.
     * </p> 
	 */
	public MessagingEvent setFocus(ResourceTypeEnum theValue) {
		getFocusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>request</b> ().
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
	 * Sets the value(s) for <b>request</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the request for this event
     * </p> 
	 */
	public MessagingEvent setRequest(ResourceReferenceDt theValue) {
		myRequest = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>response</b> ().
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
	 * Sets the value(s) for <b>response</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the response for this event
     * </p> 
	 */
	public MessagingEvent setResponse(ResourceReferenceDt theValue) {
		myResponse = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
     * </p> 
	 */
	public StringDt getDocumentationElement() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
     * </p> 
	 */
	public String getDocumentation() {  
		return getDocumentationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
     * </p> 
	 */
	public MessagingEvent setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
     * </p> 
	 */
	public MessagingEvent setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>Conformance.document</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A document definition
     * </p> 
	 */
	@Block()	
	public static class Document 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="mode", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="DocumentMode",
		formalDefinition="Mode of this document declaration - whether application is producer or consumer"
	)
	private BoundCodeDt<DocumentModeEnum> myMode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc."
	)
	private StringDt myDocumentation;
	
	@Child(name="profile", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dev.resource.Profile.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A constraint on a resource used in the document"
	)
	private ResourceReferenceDt myProfile;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMode,  myDocumentation,  myProfile);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMode, myDocumentation, myProfile);
	}

	/**
	 * Gets the value(s) for <b>mode</b> (DocumentMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Mode of this document declaration - whether application is producer or consumer
     * </p> 
	 */
	public BoundCodeDt<DocumentModeEnum> getModeElement() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<DocumentModeEnum>(DocumentModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	
	/**
	 * Gets the value(s) for <b>mode</b> (DocumentMode).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Mode of this document declaration - whether application is producer or consumer
     * </p> 
	 */
	public String getMode() {  
		return getModeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>mode</b> (DocumentMode)
	 *
     * <p>
     * <b>Definition:</b>
     * Mode of this document declaration - whether application is producer or consumer
     * </p> 
	 */
	public Document setMode(BoundCodeDt<DocumentModeEnum> theValue) {
		myMode = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>mode</b> (DocumentMode)
	 *
     * <p>
     * <b>Definition:</b>
     * Mode of this document declaration - whether application is producer or consumer
     * </p> 
	 */
	public Document setMode(DocumentModeEnum theValue) {
		getModeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc.
     * </p> 
	 */
	public StringDt getDocumentationElement() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc.
     * </p> 
	 */
	public String getDocumentation() {  
		return getDocumentationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc.
     * </p> 
	 */
	public Document setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc.
     * </p> 
	 */
	public Document setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>profile</b> ().
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
	 * Sets the value(s) for <b>profile</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A constraint on a resource used in the document
     * </p> 
	 */
	public Document setProfile(ResourceReferenceDt theValue) {
		myProfile = theValue;
		return this;
	}
	
	

  

	}




    @Override
    public String getResourceName() {
        return "Conformance";
    }
    
    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DEV;
    }

}
