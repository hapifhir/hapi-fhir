















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
import ca.uhn.fhir.model.dev.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dev.valueset.OperationKindEnum;
import ca.uhn.fhir.model.dev.valueset.OperationParameterUseEnum;
import ca.uhn.fhir.model.dev.valueset.ResourceProfileStatusEnum;
import ca.uhn.fhir.model.dev.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>OperationDefinition</b> Resource
 * (infrastructure)
 *
 * <p>
 * <b>Definition:</b>
 * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search interaction)
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/OperationDefinition">http://hl7.org/fhir/profiles/OperationDefinition</a> 
 * </p>
 *
 */
@ResourceDef(name="OperationDefinition", profile="http://hl7.org/fhir/profiles/OperationDefinition", id="operationdefinition")
public class OperationDefinition 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.identifier</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="OperationDefinition.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.identifier</b><br>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.version</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="version", path="OperationDefinition.version", description="", type="token"  )
	public static final String SP_VERSION = "version";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.version</b><br>
	 * </p>
	 */
	public static final TokenClientParam VERSION = new TokenClientParam(SP_VERSION);

	/**
	 * Search parameter constant for <b>title</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>OperationDefinition.title</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="title", path="OperationDefinition.title", description="", type="string"  )
	public static final String SP_TITLE = "title";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>title</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>OperationDefinition.title</b><br>
	 * </p>
	 */
	public static final StringClientParam TITLE = new StringClientParam(SP_TITLE);

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>OperationDefinition.publisher</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="publisher", path="OperationDefinition.publisher", description="", type="string"  )
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>OperationDefinition.publisher</b><br>
	 * </p>
	 */
	public static final StringClientParam PUBLISHER = new StringClientParam(SP_PUBLISHER);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.code</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="OperationDefinition.code", description="", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.code</b><br>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.status</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="OperationDefinition.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.status</b><br>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>OperationDefinition.date</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="OperationDefinition.date", description="", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>OperationDefinition.date</b><br>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>kind</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.kind</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="kind", path="OperationDefinition.kind", description="", type="token"  )
	public static final String SP_KIND = "kind";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>kind</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.kind</b><br>
	 * </p>
	 */
	public static final TokenClientParam KIND = new TokenClientParam(SP_KIND);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.name</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="OperationDefinition.name", description="", type="token"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.name</b><br>
	 * </p>
	 */
	public static final TokenClientParam NAME = new TokenClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>base</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>OperationDefinition.base</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="base", path="OperationDefinition.base", description="", type="reference"  )
	public static final String SP_BASE = "base";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>base</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>OperationDefinition.base</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam BASE = new ReferenceClientParam(SP_BASE);

	/**
	 * Search parameter constant for <b>system</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.system</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="system", path="OperationDefinition.system", description="", type="token"  )
	public static final String SP_SYSTEM = "system";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>system</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.system</b><br>
	 * </p>
	 */
	public static final TokenClientParam SYSTEM = new TokenClientParam(SP_SYSTEM);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.type</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="OperationDefinition.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.type</b><br>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>instance</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.instance</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="instance", path="OperationDefinition.instance", description="", type="token"  )
	public static final String SP_INSTANCE = "instance";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>instance</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>OperationDefinition.instance</b><br>
	 * </p>
	 */
	public static final TokenClientParam INSTANCE = new TokenClientParam(SP_INSTANCE);

	/**
	 * Search parameter constant for <b>profile</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>OperationDefinition.parameter.profile</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="profile", path="OperationDefinition.parameter.profile", description="", type="reference"  )
	public static final String SP_PROFILE = "profile";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>profile</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>reference</b><br>
	 * Path: <b>OperationDefinition.parameter.profile</b><br>
	 * </p>
	 */
	public static final ReferenceClientParam PROFILE = new ReferenceClientParam(SP_PROFILE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.base</b>".
	 */
	public static final Include INCLUDE_BASE = new Include("OperationDefinition.base");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.code</b>".
	 */
	public static final Include INCLUDE_CODE = new Include("OperationDefinition.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("OperationDefinition.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("OperationDefinition.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.instance</b>".
	 */
	public static final Include INCLUDE_INSTANCE = new Include("OperationDefinition.instance");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.kind</b>".
	 */
	public static final Include INCLUDE_KIND = new Include("OperationDefinition.kind");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("OperationDefinition.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.parameter.profile</b>".
	 */
	public static final Include INCLUDE_PARAMETER_PROFILE = new Include("OperationDefinition.parameter.profile");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.publisher</b>".
	 */
	public static final Include INCLUDE_PUBLISHER = new Include("OperationDefinition.publisher");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("OperationDefinition.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.system</b>".
	 */
	public static final Include INCLUDE_SYSTEM = new Include("OperationDefinition.system");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.title</b>".
	 */
	public static final Include INCLUDE_TITLE = new Include("OperationDefinition.title");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.type</b>".
	 */
	public static final Include INCLUDE_TYPE = new Include("OperationDefinition.type");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>OperationDefinition.version</b>".
	 */
	public static final Include INCLUDE_VERSION = new Include("OperationDefinition.version");


	@Child(name="identifier", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="id",
		formalDefinition="The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)"
	)
	private UriDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="id.version",
		formalDefinition="The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="title", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language name identifying the Profile"
	)
	private StringDt myTitle;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="who.witness",
		formalDefinition="Details of the individual or organization who accepts responsibility for publishing the profile"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactPointDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Contact details to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language description of the profile and its use"
	)
	private StringDt myDescription;
	
	@Child(name="code", type=CodingDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of templates."
	)
	private java.util.List<CodingDt> myCode;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="status",
		formalDefinition="The status of the profile"
	)
	private BoundCodeDt<ResourceProfileStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="date", type=DateTimeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="when.recorded",
		formalDefinition="The date that this version of the profile was published"
	)
	private DateTimeDt myDate;
	
	@Child(name="kind", type=CodeDt.class, order=10, min=1, max=1)	
	@Description(
		shortDefinition="class",
		formalDefinition="Whether this is operation or named query"
	)
	private BoundCodeDt<OperationKindEnum> myKind;
	
	@Child(name="name", type=CodeDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The name used to invoke the operation"
	)
	private CodeDt myName;
	
	@Child(name="notes", type=StringDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional information about how to use this operation or named query"
	)
	private StringDt myNotes;
	
	@Child(name="base", order=13, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.OperationDefinition.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Indicates that this operation definition is a constraining profile on the base"
	)
	private ResourceReferenceDt myBase;
	
	@Child(name="system", type=BooleanDt.class, order=14, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context)"
	)
	private BooleanDt mySystem;
	
	@Child(name="type", type=CodeDt.class, order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context)"
	)
	private java.util.List<BoundCodeDt<ResourceTypeEnum>> myType;
	
	@Child(name="instance", type=BooleanDt.class, order=16, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates whether this operation can be invoked on a particular instance of one of the given types"
	)
	private BooleanDt myInstance;
	
	@Child(name="parameter", order=17, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The parameters for the operation/query"
	)
	private java.util.List<Parameter> myParameter;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myTitle,  myPublisher,  myTelecom,  myDescription,  myCode,  myStatus,  myExperimental,  myDate,  myKind,  myName,  myNotes,  myBase,  mySystem,  myType,  myInstance,  myParameter);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myVersion, myTitle, myPublisher, myTelecom, myDescription, myCode, myStatus, myExperimental, myDate, myKind, myName, myNotes, myBase, mySystem, myType, myInstance, myParameter);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public UriDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new UriDt();
		}
		return myIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>identifier</b> (id).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public URI getIdentifier() {  
		return getIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public OperationDefinition setIdentifier(UriDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identifier</b> (id)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this operation definition when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public OperationDefinition setIdentifier( String theUri) {
		myIdentifier = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (id.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
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
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
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
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public OperationDefinition setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> (id.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public OperationDefinition setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>title</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public StringDt getTitleElement() {  
		if (myTitle == null) {
			myTitle = new StringDt();
		}
		return myTitle;
	}

	
	/**
	 * Gets the value(s) for <b>title</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public String getTitle() {  
		return getTitleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>title</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public OperationDefinition setTitle(StringDt theValue) {
		myTitle = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>title</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the Profile
     * </p> 
	 */
	public OperationDefinition setTitle( String theString) {
		myTitle = new StringDt(theString); 
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
	public OperationDefinition setPublisher(StringDt theValue) {
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
	public OperationDefinition setPublisher( String theString) {
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
	public OperationDefinition setTelecom(java.util.List<ContactPointDt> theValue) {
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
	public OperationDefinition setDescription(StringDt theValue) {
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
	public OperationDefinition setDescription( String theString) {
		myDescription = new StringDt(theString); 
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
	public OperationDefinition setCode(java.util.List<CodingDt> theValue) {
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
	public OperationDefinition setStatus(BoundCodeDt<ResourceProfileStatusEnum> theValue) {
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
	public OperationDefinition setStatus(ResourceProfileStatusEnum theValue) {
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
	 * Gets the value(s) for <b>experimental</b> ().
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
	 * Sets the value(s) for <b>experimental</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public OperationDefinition setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>experimental</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public OperationDefinition setExperimental( boolean theBoolean) {
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
	public OperationDefinition setDate(DateTimeDt theValue) {
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
	public OperationDefinition setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
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
	public OperationDefinition setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>kind</b> (class).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is operation or named query
     * </p> 
	 */
	public BoundCodeDt<OperationKindEnum> getKindElement() {  
		if (myKind == null) {
			myKind = new BoundCodeDt<OperationKindEnum>(OperationKindEnum.VALUESET_BINDER);
		}
		return myKind;
	}

	
	/**
	 * Gets the value(s) for <b>kind</b> (class).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is operation or named query
     * </p> 
	 */
	public String getKind() {  
		return getKindElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>kind</b> (class)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is operation or named query
     * </p> 
	 */
	public OperationDefinition setKind(BoundCodeDt<OperationKindEnum> theValue) {
		myKind = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>kind</b> (class)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is operation or named query
     * </p> 
	 */
	public OperationDefinition setKind(OperationKindEnum theValue) {
		getKindElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name used to invoke the operation
     * </p> 
	 */
	public CodeDt getNameElement() {  
		if (myName == null) {
			myName = new CodeDt();
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
     * The name used to invoke the operation
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
     * The name used to invoke the operation
     * </p> 
	 */
	public OperationDefinition setName(CodeDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The name used to invoke the operation
     * </p> 
	 */
	public OperationDefinition setName( String theCode) {
		myName = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how to use this operation or named query
     * </p> 
	 */
	public StringDt getNotesElement() {  
		if (myNotes == null) {
			myNotes = new StringDt();
		}
		return myNotes;
	}

	
	/**
	 * Gets the value(s) for <b>notes</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how to use this operation or named query
     * </p> 
	 */
	public String getNotes() {  
		return getNotesElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how to use this operation or named query
     * </p> 
	 */
	public OperationDefinition setNotes(StringDt theValue) {
		myNotes = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>notes</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how to use this operation or named query
     * </p> 
	 */
	public OperationDefinition setNotes( String theString) {
		myNotes = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>base</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this operation definition is a constraining profile on the base
     * </p> 
	 */
	public ResourceReferenceDt getBase() {  
		if (myBase == null) {
			myBase = new ResourceReferenceDt();
		}
		return myBase;
	}

	/**
	 * Sets the value(s) for <b>base</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this operation definition is a constraining profile on the base
     * </p> 
	 */
	public OperationDefinition setBase(ResourceReferenceDt theValue) {
		myBase = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>system</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public BooleanDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new BooleanDt();
		}
		return mySystem;
	}

	
	/**
	 * Gets the value(s) for <b>system</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public Boolean getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public OperationDefinition setSystem(BooleanDt theValue) {
		mySystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public OperationDefinition setSystem( boolean theBoolean) {
		mySystem = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public java.util.List<BoundCodeDt<ResourceTypeEnum>> getType() {  
		if (myType == null) {
			myType = new java.util.ArrayList<BoundCodeDt<ResourceTypeEnum>>();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public OperationDefinition setType(java.util.List<BoundCodeDt<ResourceTypeEnum>> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Add a value for <b>type</b> () using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> addType(ResourceTypeEnum theValue) {
		BoundCodeDt<ResourceTypeEnum> retVal = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER, theValue);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>type</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> getTypeFirstRep() {
		if (getType().size() == 0) {
			addType();
		}
		return getType().get(0);
	}

	/**
	 * Add a value for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> addType() {
		BoundCodeDt<ResourceTypeEnum> retVal = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER);
		getType().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type level (e.g. without needing to choose a resource type for the context)
     * </p> 
	 */
	public OperationDefinition setType(ResourceTypeEnum theValue) {
		getType().clear();
		addType(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>instance</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation can be invoked on a particular instance of one of the given types
     * </p> 
	 */
	public BooleanDt getInstanceElement() {  
		if (myInstance == null) {
			myInstance = new BooleanDt();
		}
		return myInstance;
	}

	
	/**
	 * Gets the value(s) for <b>instance</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation can be invoked on a particular instance of one of the given types
     * </p> 
	 */
	public Boolean getInstance() {  
		return getInstanceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>instance</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation can be invoked on a particular instance of one of the given types
     * </p> 
	 */
	public OperationDefinition setInstance(BooleanDt theValue) {
		myInstance = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>instance</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether this operation can be invoked on a particular instance of one of the given types
     * </p> 
	 */
	public OperationDefinition setInstance( boolean theBoolean) {
		myInstance = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>parameter</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The parameters for the operation/query
     * </p> 
	 */
	public java.util.List<Parameter> getParameter() {  
		if (myParameter == null) {
			myParameter = new java.util.ArrayList<Parameter>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The parameters for the operation/query
     * </p> 
	 */
	public OperationDefinition setParameter(java.util.List<Parameter> theValue) {
		myParameter = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>parameter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The parameters for the operation/query
     * </p> 
	 */
	public Parameter addParameter() {
		Parameter newType = new Parameter();
		getParameter().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>parameter</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The parameters for the operation/query
     * </p> 
	 */
	public Parameter getParameterFirstRep() {
		if (getParameter().isEmpty()) {
			return addParameter();
		}
		return getParameter().get(0); 
	}
  
	/**
	 * Block class for child element: <b>OperationDefinition.parameter</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The parameters for the operation/query
     * </p> 
	 */
	@Block()	
	public static class Parameter 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The name of used to identify the parameter"
	)
	private CodeDt myName;
	
	@Child(name="use", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether this is an input or an output parameter"
	)
	private BoundCodeDt<OperationParameterUseEnum> myUse;
	
	@Child(name="min", type=IntegerDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The minimum number of times this parameter SHALL appear in the request or response"
	)
	private IntegerDt myMin;
	
	@Child(name="max", type=StringDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The maximum number of times this element is permitted to appear in the request or response"
	)
	private StringDt myMax;
	
	@Child(name="documentation", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Describes the meaning or use of this parameter"
	)
	private StringDt myDocumentation;
	
	@Child(name="type", type=CodeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The type for this parameter"
	)
	private CodeDt myType;
	
	@Child(name="profile", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Profile.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A profile the specifies the rules that this parameter must conform to"
	)
	private ResourceReferenceDt myProfile;
	
	@Child(name="part", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The parts of a Tuple Parameter"
	)
	private java.util.List<ParameterPart> myPart;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myUse,  myMin,  myMax,  myDocumentation,  myType,  myProfile,  myPart);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myUse, myMin, myMax, myDocumentation, myType, myProfile, myPart);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of used to identify the parameter
     * </p> 
	 */
	public CodeDt getNameElement() {  
		if (myName == null) {
			myName = new CodeDt();
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
     * The name of used to identify the parameter
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
     * The name of used to identify the parameter
     * </p> 
	 */
	public Parameter setName(CodeDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The name of used to identify the parameter
     * </p> 
	 */
	public Parameter setName( String theCode) {
		myName = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>use</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is an input or an output parameter
     * </p> 
	 */
	public BoundCodeDt<OperationParameterUseEnum> getUseElement() {  
		if (myUse == null) {
			myUse = new BoundCodeDt<OperationParameterUseEnum>(OperationParameterUseEnum.VALUESET_BINDER);
		}
		return myUse;
	}

	
	/**
	 * Gets the value(s) for <b>use</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is an input or an output parameter
     * </p> 
	 */
	public String getUse() {  
		return getUseElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>use</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is an input or an output parameter
     * </p> 
	 */
	public Parameter setUse(BoundCodeDt<OperationParameterUseEnum> theValue) {
		myUse = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>use</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is an input or an output parameter
     * </p> 
	 */
	public Parameter setUse(OperationParameterUseEnum theValue) {
		getUseElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>min</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this parameter SHALL appear in the request or response
     * </p> 
	 */
	public IntegerDt getMinElement() {  
		if (myMin == null) {
			myMin = new IntegerDt();
		}
		return myMin;
	}

	
	/**
	 * Gets the value(s) for <b>min</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this parameter SHALL appear in the request or response
     * </p> 
	 */
	public Integer getMin() {  
		return getMinElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>min</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this parameter SHALL appear in the request or response
     * </p> 
	 */
	public Parameter setMin(IntegerDt theValue) {
		myMin = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>min</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this parameter SHALL appear in the request or response
     * </p> 
	 */
	public Parameter setMin( int theInteger) {
		myMin = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>max</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the request or response
     * </p> 
	 */
	public StringDt getMaxElement() {  
		if (myMax == null) {
			myMax = new StringDt();
		}
		return myMax;
	}

	
	/**
	 * Gets the value(s) for <b>max</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the request or response
     * </p> 
	 */
	public String getMax() {  
		return getMaxElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>max</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the request or response
     * </p> 
	 */
	public Parameter setMax(StringDt theValue) {
		myMax = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>max</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the request or response
     * </p> 
	 */
	public Parameter setMax( String theString) {
		myMax = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the meaning or use of this parameter
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
     * Describes the meaning or use of this parameter
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
     * Describes the meaning or use of this parameter
     * </p> 
	 */
	public Parameter setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the meaning or use of this parameter
     * </p> 
	 */
	public Parameter setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type for this parameter
     * </p> 
	 */
	public CodeDt getTypeElement() {  
		if (myType == null) {
			myType = new CodeDt();
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
     * The type for this parameter
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
     * The type for this parameter
     * </p> 
	 */
	public Parameter setType(CodeDt theValue) {
		myType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The type for this parameter
     * </p> 
	 */
	public Parameter setType( String theCode) {
		myType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>profile</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A profile the specifies the rules that this parameter must conform to
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
     * A profile the specifies the rules that this parameter must conform to
     * </p> 
	 */
	public Parameter setProfile(ResourceReferenceDt theValue) {
		myProfile = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>part</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The parts of a Tuple Parameter
     * </p> 
	 */
	public java.util.List<ParameterPart> getPart() {  
		if (myPart == null) {
			myPart = new java.util.ArrayList<ParameterPart>();
		}
		return myPart;
	}

	/**
	 * Sets the value(s) for <b>part</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The parts of a Tuple Parameter
     * </p> 
	 */
	public Parameter setPart(java.util.List<ParameterPart> theValue) {
		myPart = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>part</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The parts of a Tuple Parameter
     * </p> 
	 */
	public ParameterPart addPart() {
		ParameterPart newType = new ParameterPart();
		getPart().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>part</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The parts of a Tuple Parameter
     * </p> 
	 */
	public ParameterPart getPartFirstRep() {
		if (getPart().isEmpty()) {
			return addPart();
		}
		return getPart().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>OperationDefinition.parameter.part</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The parts of a Tuple Parameter
     * </p> 
	 */
	@Block()	
	public static class ParameterPart 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The name of used to identify the parameter"
	)
	private CodeDt myName;
	
	@Child(name="min", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The minimum number of times this parameter SHALL appear in the request or response"
	)
	private IntegerDt myMin;
	
	@Child(name="max", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The maximum number of times this element is permitted to appear in the request or response"
	)
	private StringDt myMax;
	
	@Child(name="documentation", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Describes the meaning or use of this parameter"
	)
	private StringDt myDocumentation;
	
	@Child(name="type", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The type for this parameter"
	)
	private CodeDt myType;
	
	@Child(name="profile", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.Profile.class	})
	@Description(
		shortDefinition="",
		formalDefinition="A profile the specifies the rules that this parameter must conform to"
	)
	private ResourceReferenceDt myProfile;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myMin,  myMax,  myDocumentation,  myType,  myProfile);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myMin, myMax, myDocumentation, myType, myProfile);
	}

	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of used to identify the parameter
     * </p> 
	 */
	public CodeDt getNameElement() {  
		if (myName == null) {
			myName = new CodeDt();
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
     * The name of used to identify the parameter
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
     * The name of used to identify the parameter
     * </p> 
	 */
	public ParameterPart setName(CodeDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The name of used to identify the parameter
     * </p> 
	 */
	public ParameterPart setName( String theCode) {
		myName = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>min</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this parameter SHALL appear in the request or response
     * </p> 
	 */
	public IntegerDt getMinElement() {  
		if (myMin == null) {
			myMin = new IntegerDt();
		}
		return myMin;
	}

	
	/**
	 * Gets the value(s) for <b>min</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this parameter SHALL appear in the request or response
     * </p> 
	 */
	public Integer getMin() {  
		return getMinElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>min</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this parameter SHALL appear in the request or response
     * </p> 
	 */
	public ParameterPart setMin(IntegerDt theValue) {
		myMin = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>min</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The minimum number of times this parameter SHALL appear in the request or response
     * </p> 
	 */
	public ParameterPart setMin( int theInteger) {
		myMin = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>max</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the request or response
     * </p> 
	 */
	public StringDt getMaxElement() {  
		if (myMax == null) {
			myMax = new StringDt();
		}
		return myMax;
	}

	
	/**
	 * Gets the value(s) for <b>max</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the request or response
     * </p> 
	 */
	public String getMax() {  
		return getMaxElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>max</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the request or response
     * </p> 
	 */
	public ParameterPart setMax(StringDt theValue) {
		myMax = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>max</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum number of times this element is permitted to appear in the request or response
     * </p> 
	 */
	public ParameterPart setMax( String theString) {
		myMax = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>documentation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the meaning or use of this parameter
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
     * Describes the meaning or use of this parameter
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
     * Describes the meaning or use of this parameter
     * </p> 
	 */
	public ParameterPart setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>documentation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the meaning or use of this parameter
     * </p> 
	 */
	public ParameterPart setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type for this parameter
     * </p> 
	 */
	public CodeDt getTypeElement() {  
		if (myType == null) {
			myType = new CodeDt();
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
     * The type for this parameter
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
     * The type for this parameter
     * </p> 
	 */
	public ParameterPart setType(CodeDt theValue) {
		myType = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>type</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The type for this parameter
     * </p> 
	 */
	public ParameterPart setType( String theCode) {
		myType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>profile</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A profile the specifies the rules that this parameter must conform to
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
     * A profile the specifies the rules that this parameter must conform to
     * </p> 
	 */
	public ParameterPart setProfile(ResourceReferenceDt theValue) {
		myProfile = theValue;
		return this;
	}
	
	

  

	}





    @Override
    public String getResourceName() {
        return "OperationDefinition";
    }
    
    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DEV;
    }

}
