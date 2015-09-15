















package ca.uhn.fhir.model.dstu2.resource;

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
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.ElementDefinitionDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.valueset.ConformanceResourceStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.ExtensionContextEnum;
import ca.uhn.fhir.model.dstu2.valueset.StructureDefinitionKindEnum;
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
import ca.uhn.fhir.rest.gclient.UriClientParam;


/**
 * HAPI/FHIR <b>StructureDefinition</b> Resource
 * (conformance.content)
 *
 * <p>
 * <b>Definition:</b>
 * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions, and constraints on resources and data types
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/StructureDefinition">http://hl7.org/fhir/profiles/StructureDefinition</a> 
 * </p>
 *
 */
@ResourceDef(name="StructureDefinition", profile="http://hl7.org/fhir/profiles/StructureDefinition", id="structuredefinition")
public class StructureDefinition extends ca.uhn.fhir.model.dstu2.resource.BaseResource
    implements  IResource     {

	/**
	 * Search parameter constant for <b>url</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>StructureDefinition.url</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="url", path="StructureDefinition.url", description="", type="uri"  )
	public static final String SP_URL = "url";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>url</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>StructureDefinition.url</b><br>
	 * </p>
	 */
	public static final UriClientParam URL = new UriClientParam(SP_URL);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.identifier</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="StructureDefinition.identifier", description="The identifier of the profile", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.identifier</b><br>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.version</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="version", path="StructureDefinition.version", description="The version identifier of the profile", type="token"  )
	public static final String SP_VERSION = "version";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.version</b><br>
	 * </p>
	 */
	public static final TokenClientParam VERSION = new TokenClientParam(SP_VERSION);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.name</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="StructureDefinition.name", description="Name of the profile", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.name</b><br>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.publisher</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="publisher", path="StructureDefinition.publisher", description="Name of the publisher of the profile", type="string"  )
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.publisher</b><br>
	 * </p>
	 */
	public static final StringClientParam PUBLISHER = new StringClientParam(SP_PUBLISHER);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.description</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="StructureDefinition.description", description="Text search in the description of the profile", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the profile</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.description</b><br>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.status</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="StructureDefinition.status", description="The current status of the profile", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.status</b><br>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The profile publication date</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>StructureDefinition.date</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="StructureDefinition.date", description="The profile publication date", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The profile publication date</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>StructureDefinition.date</b><br>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code for the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.code</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="StructureDefinition.code", description="A code for the profile", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code for the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.code</b><br>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>valueset</b>
	 * <p>
	 * Description: <b>A vocabulary binding reference</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>StructureDefinition.snapshot.element.binding.valueSet[x]</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="valueset", path="StructureDefinition.snapshot.element.binding.valueSet[x]", description="A vocabulary binding reference", type="reference"  )
	public static final String SP_VALUESET = "valueset";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>valueset</b>
	 * <p>
	 * Description: <b>A vocabulary binding reference</b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>StructureDefinition.snapshot.element.binding.valueSet[x]</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam VALUESET = new ReferenceClientParam(SP_VALUESET);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.constrainedType</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="StructureDefinition.constrainedType", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.constrainedType</b><br>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>kind</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.kind</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="kind", path="StructureDefinition.kind", description="", type="token"  )
	public static final String SP_KIND = "kind";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>kind</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.kind</b><br>
	 * </p>
	 */
	public static final TokenClientParam KIND = new TokenClientParam(SP_KIND);

	/**
	 * Search parameter constant for <b>path</b>
	 * <p>
	 * Description: <b>A path that is constrained in the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.snapshot.element.path | StructureDefinition.differential.element.path </b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="path", path="StructureDefinition.snapshot.element.path | StructureDefinition.differential.element.path ", description="A path that is constrained in the profile", type="token"  )
	public static final String SP_PATH = "path";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>path</b>
	 * <p>
	 * Description: <b>A path that is constrained in the profile</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.snapshot.element.path | StructureDefinition.differential.element.path </b><br>
	 * </p>
	 */
	public static final TokenClientParam PATH = new TokenClientParam(SP_PATH);

	/**
	 * Search parameter constant for <b>context</b>
	 * <p>
	 * Description: <b>A use context assigned to the structure</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.useContext</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="context", path="StructureDefinition.useContext", description="A use context assigned to the structure", type="token"  )
	public static final String SP_CONTEXT = "context";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>context</b>
	 * <p>
	 * Description: <b>A use context assigned to the structure</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.useContext</b><br>
	 * </p>
	 */
	public static final TokenClientParam CONTEXT = new TokenClientParam(SP_CONTEXT);

	/**
	 * Search parameter constant for <b>display</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.display</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="display", path="StructureDefinition.display", description="", type="string"  )
	public static final String SP_DISPLAY = "display";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>display</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.display</b><br>
	 * </p>
	 */
	public static final StringClientParam DISPLAY = new StringClientParam(SP_DISPLAY);

	/**
	 * Search parameter constant for <b>experimental</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.experimental</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="experimental", path="StructureDefinition.experimental", description="", type="token"  )
	public static final String SP_EXPERIMENTAL = "experimental";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>experimental</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.experimental</b><br>
	 * </p>
	 */
	public static final TokenClientParam EXPERIMENTAL = new TokenClientParam(SP_EXPERIMENTAL);

	/**
	 * Search parameter constant for <b>abstract</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.abstract</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="abstract", path="StructureDefinition.abstract", description="", type="token"  )
	public static final String SP_ABSTRACT = "abstract";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>abstract</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.abstract</b><br>
	 * </p>
	 */
	public static final TokenClientParam ABSTRACT = new TokenClientParam(SP_ABSTRACT);

	/**
	 * Search parameter constant for <b>context-type</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.contextType</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="context-type", path="StructureDefinition.contextType", description="", type="token"  )
	public static final String SP_CONTEXT_TYPE = "context-type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>context-type</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.contextType</b><br>
	 * </p>
	 */
	public static final TokenClientParam CONTEXT_TYPE = new TokenClientParam(SP_CONTEXT_TYPE);

	/**
	 * Search parameter constant for <b>ext-context</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.context</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="ext-context", path="StructureDefinition.context", description="", type="string"  )
	public static final String SP_EXT_CONTEXT = "ext-context";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>ext-context</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>StructureDefinition.context</b><br>
	 * </p>
	 */
	public static final StringClientParam EXT_CONTEXT = new StringClientParam(SP_EXT_CONTEXT);

	/**
	 * Search parameter constant for <b>base</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>StructureDefinition.base</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="base", path="StructureDefinition.base", description="", type="uri"  )
	public static final String SP_BASE = "base";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>base</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>StructureDefinition.base</b><br>
	 * </p>
	 */
	public static final UriClientParam BASE = new UriClientParam(SP_BASE);

	/**
	 * Search parameter constant for <b>base-path</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.snapshot.element.base.path | StructureDefinition.differential.element.base.path </b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="base-path", path="StructureDefinition.snapshot.element.base.path | StructureDefinition.differential.element.base.path ", description="", type="token"  )
	public static final String SP_BASE_PATH = "base-path";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>base-path</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>StructureDefinition.snapshot.element.base.path | StructureDefinition.differential.element.base.path </b><br>
	 * </p>
	 */
	public static final TokenClientParam BASE_PATH = new TokenClientParam(SP_BASE_PATH);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>StructureDefinition:valueset</b>".
	 */
	public static final Include INCLUDE_VALUESET = new Include("StructureDefinition:valueset");


	@Child(name="url", type=UriDt.class, order=0, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="id",
		formalDefinition="An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published"
	)
	private UriDt myUrl;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="id",
		formalDefinition="Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=2, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="id.version",
		formalDefinition="The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=3, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language name identifying the StructureDefinition"
	)
	private StringDt myName;
	
	@Child(name="display", type=StringDt.class, order=4, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Defined so that applications can use this name when displaying the value of the extension to the user"
	)
	private StringDt myDisplay;
	
	@Child(name="status", type=CodeDt.class, order=5, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="status",
		formalDefinition="The status of the StructureDefinition"
	)
	private BoundCodeDt<ConformanceResourceStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=6, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="class",
		formalDefinition="This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="publisher", type=StringDt.class, order=7, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="who.witness",
		formalDefinition="The name of the individual or organization that published the structure definition"
	)
	private StringDt myPublisher;
	
	@Child(name="contact", order=8, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Contacts to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<Contact> myContact;
	
	@Child(name="date", type=DateTimeDt.class, order=9, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="when.recorded",
		formalDefinition="The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes"
	)
	private DateTimeDt myDate;
	
	@Child(name="description", type=StringDt.class, order=10, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language description of the StructureDefinition and its use"
	)
	private StringDt myDescription;
	
	@Child(name="useContext", type=CodeableConceptDt.class, order=11, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions."
	)
	private java.util.List<CodeableConceptDt> myUseContext;
	
	@Child(name="requirements", type=StringDt.class, order=12, min=0, max=1, summary=false, modifier=false)	
	@Description(
		shortDefinition="why",
		formalDefinition="Explains why this structure definition is needed and why it's been constrained as it has"
	)
	private StringDt myRequirements;
	
	@Child(name="copyright", type=StringDt.class, order=13, min=0, max=1, summary=false, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings"
	)
	private StringDt myCopyright;
	
	@Child(name="code", type=CodingDt.class, order=14, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of templates."
	)
	private java.util.List<CodingDt> myCode;
	
	@Child(name="fhirVersion", type=IdDt.class, order=15, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version"
	)
	private IdDt myFhirVersion;
	
	@Child(name="mapping", order=16, min=0, max=Child.MAX_UNLIMITED, summary=false, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="An external specification that the content is mapped to"
	)
	private java.util.List<Mapping> myMapping;
	
	@Child(name="kind", type=CodeDt.class, order=17, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Defines the kind of structure that this definition is describing"
	)
	private BoundCodeDt<StructureDefinitionKindEnum> myKind;
	
	@Child(name="constrainedType", type=CodeDt.class, order=18, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure"
	)
	private CodeDt myConstrainedType;
	
	@Child(name="abstract", type=BooleanDt.class, order=19, min=1, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type"
	)
	private BooleanDt myAbstract;
	
	@Child(name="contextType", type=CodeDt.class, order=20, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="If this is an extension, Identifies the context within FHIR resources where the extension can be used"
	)
	private BoundCodeDt<ExtensionContextEnum> myContextType;
	
	@Child(name="context", type=StringDt.class, order=21, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies the types of resource or data type elements to which the extension can be applied"
	)
	private java.util.List<StringDt> myContext;
	
	@Child(name="base", type=UriDt.class, order=22, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="An absolute URI that is the base structure from which this set of constraints is derived"
	)
	private UriDt myBase;
	
	@Child(name="snapshot", order=23, min=0, max=1, summary=false, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base StructureDefinition"
	)
	private Snapshot mySnapshot;
	
	@Child(name="differential", order=24, min=0, max=1, summary=false, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies"
	)
	private Differential myDifferential;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myUrl,  myIdentifier,  myVersion,  myName,  myDisplay,  myStatus,  myExperimental,  myPublisher,  myContact,  myDate,  myDescription,  myUseContext,  myRequirements,  myCopyright,  myCode,  myFhirVersion,  myMapping,  myKind,  myConstrainedType,  myAbstract,  myContextType,  myContext,  myBase,  mySnapshot,  myDifferential);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myUrl, myIdentifier, myVersion, myName, myDisplay, myStatus, myExperimental, myPublisher, myContact, myDate, myDescription, myUseContext, myRequirements, myCopyright, myCode, myFhirVersion, myMapping, myKind, myConstrainedType, myAbstract, myContextType, myContext, myBase, mySnapshot, myDifferential);
	}

	/**
	 * Gets the value(s) for <b>url</b> (id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published
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
     * An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published
     * </p> 
	 */
	public String getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published
     * </p> 
	 */
	public StructureDefinition setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published
     * </p> 
	 */
	public StructureDefinition setUrl( String theUri) {
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
     * Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)
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
     * Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)
     * </p> 
	 */
	public StructureDefinition setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>identifier</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>identifier</b> (id)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)
	 * </p>
	 * @param theValue The identifier to add (must not be <code>null</code>)
	 */
	public StructureDefinition addIdentifier(IdentifierDt theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getIdentifier().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (id),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)
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
     * The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually
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
     * The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually
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
     * The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually
     * </p> 
	 */
	public StructureDefinition setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> (id.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually
     * </p> 
	 */
	public StructureDefinition setVersion( String theString) {
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
     * A free text natural language name identifying the StructureDefinition
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
     * A free text natural language name identifying the StructureDefinition
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
     * A free text natural language name identifying the StructureDefinition
     * </p> 
	 */
	public StructureDefinition setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the StructureDefinition
     * </p> 
	 */
	public StructureDefinition setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user
     * </p> 
	 */
	public StringDt getDisplayElement() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user
     * </p> 
	 */
	public String getDisplay() {  
		return getDisplayElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user
     * </p> 
	 */
	public StructureDefinition setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Defined so that applications can use this name when displaying the value of the extension to the user
     * </p> 
	 */
	public StructureDefinition setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the StructureDefinition
     * </p> 
	 */
	public BoundCodeDt<ConformanceResourceStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ConformanceResourceStatusEnum>(ConformanceResourceStatusEnum.VALUESET_BINDER);
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
     * The status of the StructureDefinition
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
     * The status of the StructureDefinition
     * </p> 
	 */
	public StructureDefinition setStatus(BoundCodeDt<ConformanceResourceStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (status)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the StructureDefinition
     * </p> 
	 */
	public StructureDefinition setStatus(ConformanceResourceStatusEnum theValue) {
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
     * This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
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
     * This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
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
     * This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public StructureDefinition setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>experimental</b> (class)
	 *
     * <p>
     * <b>Definition:</b>
     * This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public StructureDefinition setExperimental( boolean theBoolean) {
		myExperimental = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>publisher</b> (who.witness).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the structure definition
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
     * The name of the individual or organization that published the structure definition
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
     * The name of the individual or organization that published the structure definition
     * </p> 
	 */
	public StructureDefinition setPublisher(StringDt theValue) {
		myPublisher = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>publisher</b> (who.witness)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the structure definition
     * </p> 
	 */
	public StructureDefinition setPublisher( String theString) {
		myPublisher = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contact</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public java.util.List<Contact> getContact() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<Contact>();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public StructureDefinition setContact(java.util.List<Contact> theValue) {
		myContact = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public Contact addContact() {
		Contact newType = new Contact();
		getContact().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>contact</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Contacts to assist a user in finding and communicating with the publisher
	 * </p>
	 * @param theValue The contact to add (must not be <code>null</code>)
	 */
	public StructureDefinition addContact(Contact theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getContact().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>contact</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public Contact getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>date</b> (when.recorded).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes
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
     * The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes
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
     * The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes
     * </p> 
	 */
	public StructureDefinition setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> (when.recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes
     * </p> 
	 */
	public StructureDefinition setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (when.recorded)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes
     * </p> 
	 */
	public StructureDefinition setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the StructureDefinition and its use
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
     * A free text natural language description of the StructureDefinition and its use
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
     * A free text natural language description of the StructureDefinition and its use
     * </p> 
	 */
	public StructureDefinition setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the StructureDefinition and its use
     * </p> 
	 */
	public StructureDefinition setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>useContext</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getUseContext() {  
		if (myUseContext == null) {
			myUseContext = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myUseContext;
	}

	/**
	 * Sets the value(s) for <b>useContext</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions.
     * </p> 
	 */
	public StructureDefinition setUseContext(java.util.List<CodeableConceptDt> theValue) {
		myUseContext = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>useContext</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions.
     * </p> 
	 */
	public CodeableConceptDt addUseContext() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getUseContext().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>useContext</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions.
	 * </p>
	 * @param theValue The useContext to add (must not be <code>null</code>)
	 */
	public StructureDefinition addUseContext(CodeableConceptDt theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getUseContext().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>useContext</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions.
     * </p> 
	 */
	public CodeableConceptDt getUseContextFirstRep() {
		if (getUseContext().isEmpty()) {
			return addUseContext();
		}
		return getUseContext().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>requirements</b> (why).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this structure definition is needed and why it's been constrained as it has
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
     * Explains why this structure definition is needed and why it's been constrained as it has
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
     * Explains why this structure definition is needed and why it's been constrained as it has
     * </p> 
	 */
	public StructureDefinition setRequirements(StringDt theValue) {
		myRequirements = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>requirements</b> (why)
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this structure definition is needed and why it's been constrained as it has
     * </p> 
	 */
	public StructureDefinition setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>copyright</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings
     * </p> 
	 */
	public StringDt getCopyrightElement() {  
		if (myCopyright == null) {
			myCopyright = new StringDt();
		}
		return myCopyright;
	}

	
	/**
	 * Gets the value(s) for <b>copyright</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings
     * </p> 
	 */
	public String getCopyright() {  
		return getCopyrightElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>copyright</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings
     * </p> 
	 */
	public StructureDefinition setCopyright(StringDt theValue) {
		myCopyright = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>copyright</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings
     * </p> 
	 */
	public StructureDefinition setCopyright( String theString) {
		myCopyright = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> ().
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
	 * Sets the value(s) for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     * </p> 
	 */
	public StructureDefinition setCode(java.util.List<CodingDt> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>code</b> ()
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
	 * Adds a given new value for <b>code</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
	 * </p>
	 * @param theValue The code to add (must not be <code>null</code>)
	 */
	public StructureDefinition addCode(CodingDt theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getCode().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>code</b> (),
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
	 * Gets the value(s) for <b>fhirVersion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version
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
     * The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version
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
     * The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version
     * </p> 
	 */
	public StructureDefinition setFhirVersion(IdDt theValue) {
		myFhirVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>fhirVersion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is $version$ for this version
     * </p> 
	 */
	public StructureDefinition setFhirVersion( String theId) {
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
	public StructureDefinition setMapping(java.util.List<Mapping> theValue) {
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
	 * Adds a given new value for <b>mapping</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * An external specification that the content is mapped to
	 * </p>
	 * @param theValue The mapping to add (must not be <code>null</code>)
	 */
	public StructureDefinition addMapping(Mapping theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getMapping().add(theValue);
		return this;
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
	 * Gets the value(s) for <b>kind</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Defines the kind of structure that this definition is describing
     * </p> 
	 */
	public BoundCodeDt<StructureDefinitionKindEnum> getKindElement() {  
		if (myKind == null) {
			myKind = new BoundCodeDt<StructureDefinitionKindEnum>(StructureDefinitionKindEnum.VALUESET_BINDER);
		}
		return myKind;
	}

	
	/**
	 * Gets the value(s) for <b>kind</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Defines the kind of structure that this definition is describing
     * </p> 
	 */
	public String getKind() {  
		return getKindElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>kind</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Defines the kind of structure that this definition is describing
     * </p> 
	 */
	public StructureDefinition setKind(BoundCodeDt<StructureDefinitionKindEnum> theValue) {
		myKind = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>kind</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Defines the kind of structure that this definition is describing
     * </p> 
	 */
	public StructureDefinition setKind(StructureDefinitionKindEnum theValue) {
		getKindElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>constrainedType</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure
     * </p> 
	 */
	public CodeDt getConstrainedTypeElement() {  
		if (myConstrainedType == null) {
			myConstrainedType = new CodeDt();
		}
		return myConstrainedType;
	}

	
	/**
	 * Gets the value(s) for <b>constrainedType</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure
     * </p> 
	 */
	public String getConstrainedType() {  
		return getConstrainedTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>constrainedType</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure
     * </p> 
	 */
	public StructureDefinition setConstrainedType(CodeDt theValue) {
		myConstrainedType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>constrainedType</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure
     * </p> 
	 */
	public StructureDefinition setConstrainedType( String theCode) {
		myConstrainedType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>abstract</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type
     * </p> 
	 */
	public BooleanDt getAbstractElement() {  
		if (myAbstract == null) {
			myAbstract = new BooleanDt();
		}
		return myAbstract;
	}

	
	/**
	 * Gets the value(s) for <b>abstract</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type
     * </p> 
	 */
	public Boolean getAbstract() {  
		return getAbstractElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>abstract</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type
     * </p> 
	 */
	public StructureDefinition setAbstract(BooleanDt theValue) {
		myAbstract = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>abstract</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type
     * </p> 
	 */
	public StructureDefinition setAbstract( boolean theBoolean) {
		myAbstract = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contextType</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this is an extension, Identifies the context within FHIR resources where the extension can be used
     * </p> 
	 */
	public BoundCodeDt<ExtensionContextEnum> getContextTypeElement() {  
		if (myContextType == null) {
			myContextType = new BoundCodeDt<ExtensionContextEnum>(ExtensionContextEnum.VALUESET_BINDER);
		}
		return myContextType;
	}

	
	/**
	 * Gets the value(s) for <b>contextType</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this is an extension, Identifies the context within FHIR resources where the extension can be used
     * </p> 
	 */
	public String getContextType() {  
		return getContextTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>contextType</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If this is an extension, Identifies the context within FHIR resources where the extension can be used
     * </p> 
	 */
	public StructureDefinition setContextType(BoundCodeDt<ExtensionContextEnum> theValue) {
		myContextType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>contextType</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If this is an extension, Identifies the context within FHIR resources where the extension can be used
     * </p> 
	 */
	public StructureDefinition setContextType(ExtensionContextEnum theValue) {
		getContextTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>context</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public java.util.List<StringDt> getContext() {  
		if (myContext == null) {
			myContext = new java.util.ArrayList<StringDt>();
		}
		return myContext;
	}

	/**
	 * Sets the value(s) for <b>context</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public StructureDefinition setContext(java.util.List<StringDt> theValue) {
		myContext = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>context</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public StringDt addContext() {
		StringDt newType = new StringDt();
		getContext().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>context</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Identifies the types of resource or data type elements to which the extension can be applied
	 * </p>
	 * @param theValue The context to add (must not be <code>null</code>)
	 */
	public StructureDefinition addContext(StringDt theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getContext().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>context</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
	 */
	public StringDt getContextFirstRep() {
		if (getContext().isEmpty()) {
			return addContext();
		}
		return getContext().get(0); 
	}
 	/**
	 * Adds a new value for <b>context</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the types of resource or data type elements to which the extension can be applied
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public StructureDefinition addContext( String theString) {
		if (myContext == null) {
			myContext = new java.util.ArrayList<StringDt>();
		}
		myContext.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>base</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI that is the base structure from which this set of constraints is derived
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
     * An absolute URI that is the base structure from which this set of constraints is derived
     * </p> 
	 */
	public String getBase() {  
		return getBaseElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>base</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI that is the base structure from which this set of constraints is derived
     * </p> 
	 */
	public StructureDefinition setBase(UriDt theValue) {
		myBase = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>base</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI that is the base structure from which this set of constraints is derived
     * </p> 
	 */
	public StructureDefinition setBase( String theUri) {
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
     * A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base StructureDefinition
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
     * A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base StructureDefinition
     * </p> 
	 */
	public StructureDefinition setSnapshot(Snapshot theValue) {
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
     * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies
     * </p> 
	 */
	public Differential getDifferential() {  
		if (myDifferential == null) {
			myDifferential = new Differential();
		}
		return myDifferential;
	}

	/**
	 * Sets the value(s) for <b>differential</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies
     * </p> 
	 */
	public StructureDefinition setDifferential(Differential theValue) {
		myDifferential = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>StructureDefinition.contact</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	@Block()	
	public static class Contact 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=0, max=1, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="The name of an individual to contact regarding the structure definition"
	)
	private StringDt myName;
	
	@Child(name="telecom", type=ContactPointDt.class, order=1, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="Contact details for individual (if a name was provided) or the publisher"
	)
	private java.util.List<ContactPointDt> myTelecom;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myTelecom);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myTelecom);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of an individual to contact regarding the structure definition
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
     * The name of an individual to contact regarding the structure definition
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
     * The name of an individual to contact regarding the structure definition
     * </p> 
	 */
	public Contact setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The name of an individual to contact regarding the structure definition
     * </p> 
	 */
	public Contact setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>telecom</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for individual (if a name was provided) or the publisher
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
     * Contact details for individual (if a name was provided) or the publisher
     * </p> 
	 */
	public Contact setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for individual (if a name was provided) or the publisher
     * </p> 
	 */
	public ContactPointDt addTelecom() {
		ContactPointDt newType = new ContactPointDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>telecom</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Contact details for individual (if a name was provided) or the publisher
	 * </p>
	 * @param theValue The telecom to add (must not be <code>null</code>)
	 */
	public Contact addTelecom(ContactPointDt theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getTelecom().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contact details for individual (if a name was provided) or the publisher
     * </p> 
	 */
	public ContactPointDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  


	}


	/**
	 * Block class for child element: <b>StructureDefinition.mapping</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An external specification that the content is mapped to
     * </p> 
	 */
	@Block()	
	public static class Mapping 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identity", type=IdDt.class, order=0, min=1, max=1, summary=false, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="An Internal id that is used to identify this mapping set when specific mappings are made"
	)
	private IdDt myIdentity;
	
	@Child(name="uri", type=UriDt.class, order=1, min=0, max=1, summary=false, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="An absolute URI that identifies the specification that this mapping is expressed to"
	)
	private UriDt myUri;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1, summary=false, modifier=false)	
	@Description(
		shortDefinition="",
		formalDefinition="A name for the specification that is being mapped to"
	)
	private StringDt myName;
	
	@Child(name="comments", type=StringDt.class, order=3, min=0, max=1, summary=false, modifier=false)	
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
     * An absolute URI that identifies the specification that this mapping is expressed to
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
     * An absolute URI that identifies the specification that this mapping is expressed to
     * </p> 
	 */
	public String getUri() {  
		return getUriElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>uri</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI that identifies the specification that this mapping is expressed to
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
     * An absolute URI that identifies the specification that this mapping is expressed to
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
	 * Block class for child element: <b>StructureDefinition.snapshot</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base StructureDefinition
     * </p> 
	 */
	@Block()	
	public static class Snapshot 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="element", type=ElementDefinitionDt.class, order=0, min=1, max=Child.MAX_UNLIMITED, summary=false, modifier=false)	
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
	 * Adds a given new value for <b>element</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Captures constraints on each element within the resource
	 * </p>
	 * @param theValue The element to add (must not be <code>null</code>)
	 */
	public Snapshot addElement(ElementDefinitionDt theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getElement().add(theValue);
		return this;
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


	/**
	 * Block class for child element: <b>StructureDefinition.differential</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies
     * </p> 
	 */
	@Block()	
	public static class Differential 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="element", type=ElementDefinitionDt.class, order=0, min=1, max=Child.MAX_UNLIMITED, summary=false, modifier=false)	
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
	public Differential setElement(java.util.List<ElementDefinitionDt> theValue) {
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
	 * Adds a given new value for <b>element</b> ()
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Captures constraints on each element within the resource
	 * </p>
	 * @param theValue The element to add (must not be <code>null</code>)
	 */
	public Differential addElement(ElementDefinitionDt theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getElement().add(theValue);
		return this;
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
        return "StructureDefinition";
    }
    
    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU2;
    }


}
