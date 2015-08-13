















package ca.uhn.fhir.model.dstu2.resource;

import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.gclient.UriClientParam;


/**
 * HAPI/FHIR <b>ValueSet</b> Resource
 * (ValueSet)
 *
 * <p>
 * <b>Definition:</b>
 * A value set specifies a set of codes drawn from one or more code systems
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/ValueSet">http://hl7.org/fhir/profiles/ValueSet</a> 
 * </p>
 *
 */
@ResourceDef(name="ValueSet", profile="http://hl7.org/fhir/profiles/ValueSet", id="valueset")
public class ValueSet extends ca.uhn.fhir.model.dstu2.resource.BaseResource
    implements  IResource     {

	/**
	 * Search parameter constant for <b>url</b>
	 * <p>
	 * Description: <b>The logical url for the value set</b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>ValueSet.url</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="url", path="ValueSet.url", description="The logical url for the value set", type="uri"  )
	public static final String SP_URL = "url";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>url</b>
	 * <p>
	 * Description: <b>The logical url for the value set</b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>ValueSet.url</b><br>
	 * </p>
	 */
	public static final UriClientParam URL = new UriClientParam(SP_URL);

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier for the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.identifier</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="ValueSet.identifier", description="The identifier for the value set", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier for the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.identifier</b><br>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.version</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="version", path="ValueSet.version", description="The version identifier of the value set", type="token"  )
	public static final String SP_VERSION = "version";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.version</b><br>
	 * </p>
	 */
	public static final TokenClientParam VERSION = new TokenClientParam(SP_VERSION);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the value set</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>ValueSet.name</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="ValueSet.name", description="The name of the value set", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the value set</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>ValueSet.name</b><br>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the value set</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>ValueSet.publisher</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="publisher", path="ValueSet.publisher", description="Name of the publisher of the value set", type="string"  )
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the value set</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>ValueSet.publisher</b><br>
	 * </p>
	 */
	public static final StringClientParam PUBLISHER = new StringClientParam(SP_PUBLISHER);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the value set</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>ValueSet.description</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="ValueSet.description", description="Text search in the description of the value set", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the value set</b><br>
	 * Type: <b>string</b><br>
	 * Path: <b>ValueSet.description</b><br>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.status</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="ValueSet.status", description="The status of the value set", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.status</b><br>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The value set publication date</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>ValueSet.date</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="ValueSet.date", description="The value set publication date", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The value set publication date</b><br>
	 * Type: <b>date</b><br>
	 * Path: <b>ValueSet.date</b><br>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>system</b>
	 * <p>
	 * Description: <b>The system for any codes defined by this value set</b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>ValueSet.codeSystem.system</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="system", path="ValueSet.codeSystem.system", description="The system for any codes defined by this value set", type="uri"  )
	public static final String SP_SYSTEM = "system";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>system</b>
	 * <p>
	 * Description: <b>The system for any codes defined by this value set</b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>ValueSet.codeSystem.system</b><br>
	 * </p>
	 */
	public static final UriClientParam SYSTEM = new UriClientParam(SP_SYSTEM);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code defined in the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.codeSystem.concept.code</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="ValueSet.codeSystem.concept.code", description="A code defined in the value set", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code defined in the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.codeSystem.concept.code</b><br>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>reference</b>
	 * <p>
	 * Description: <b>A code system included or excluded in the value set or an imported value set</b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>ValueSet.compose.include.system</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="reference", path="ValueSet.compose.include.system", description="A code system included or excluded in the value set or an imported value set", type="uri"  )
	public static final String SP_REFERENCE = "reference";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reference</b>
	 * <p>
	 * Description: <b>A code system included or excluded in the value set or an imported value set</b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>ValueSet.compose.include.system</b><br>
	 * </p>
	 */
	public static final UriClientParam REFERENCE = new UriClientParam(SP_REFERENCE);

	/**
	 * Search parameter constant for <b>context</b>
	 * <p>
	 * Description: <b>A use context assigned to the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.useContext</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="context", path="ValueSet.useContext", description="A use context assigned to the value set", type="token"  )
	public static final String SP_CONTEXT = "context";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>context</b>
	 * <p>
	 * Description: <b>A use context assigned to the value set</b><br>
	 * Type: <b>token</b><br>
	 * Path: <b>ValueSet.useContext</b><br>
	 * </p>
	 */
	public static final TokenClientParam CONTEXT = new TokenClientParam(SP_CONTEXT);

	/**
	 * Search parameter constant for <b>expansion</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>ValueSet.expansion.identifier</b><br>
	 * </p>
	 */
	@SearchParamDefinition(name="expansion", path="ValueSet.expansion.identifier", description="", type="uri"  )
	public static final String SP_EXPANSION = "expansion";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>expansion</b>
	 * <p>
	 * Description: <b></b><br>
	 * Type: <b>uri</b><br>
	 * Path: <b>ValueSet.expansion.identifier</b><br>
	 * </p>
	 */
	public static final UriClientParam EXPANSION = new UriClientParam(SP_EXPANSION);



	@Child(name="url", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.url",
		formalDefinition="An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published"
	)
	private UriDt myUrl;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.identifier",
		formalDefinition="Formal identifier that is used to identify this value set when it is represented in other formats, or referenced in a specification, model, design or an instance."
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.version",
		formalDefinition="Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.name",
		formalDefinition="A free text natural language name describing the value set"
	)
	private StringDt myName;
	
	@Child(name="status", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.status",
		formalDefinition="The status of the value set"
	)
	private CodeDt myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.experimental",
		formalDefinition="This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="publisher", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.publisher",
		formalDefinition="The name of the individual or organization that published the value set"
	)
	private StringDt myPublisher;
	
	@Child(name="contact", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.contact",
		formalDefinition="Contacts to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<Contact> myContact;
	
	@Child(name="date", type=DateTimeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.date",
		formalDefinition="The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition')"
	)
	private DateTimeDt myDate;
	
	@Child(name="lockedDate", type=DateDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.lockedDate",
		formalDefinition="If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date"
	)
	private DateDt myLockedDate;
	
	@Child(name="description", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.description",
		formalDefinition="A free text natural language description of the use of the value set - reason for definition, \"the semantic space\" to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set."
	)
	private StringDt myDescription;
	
	@Child(name="useContext", type=CodeableConceptDt.class, order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.useContext",
		formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions."
	)
	private java.util.List<CodeableConceptDt> myUseContext;
	
	@Child(name="immutable", type=BooleanDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.immutable",
		formalDefinition="If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change"
	)
	private BooleanDt myImmutable;
	
	@Child(name="requirements", type=StringDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.requirements",
		formalDefinition="Explains why this value set is needed and why it's been constrained as it has"
	)
	private StringDt myRequirements;
	
	@Child(name="copyright", type=StringDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.copyright",
		formalDefinition="A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set"
	)
	private StringDt myCopyright;
	
	@Child(name="extensible", type=BooleanDt.class, order=15, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.extensible",
		formalDefinition="Whether this is intended to be used with an extensible binding or not"
	)
	private BooleanDt myExtensible;
	
	@Child(name="codeSystem", order=16, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem",
		formalDefinition="A definition of an code system, inlined into the value set (as a packaging convenience). Note that the inline code system may be used from other value sets by referring to it's (codeSystem.system) directly"
	)
	private CodeSystem myCodeSystem;
	
	@Child(name="compose", order=17, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.compose",
		formalDefinition="A set of criteria that provide the content logical definition of the value set by including or excluding codes from outside this value set"
	)
	private Compose myCompose;
	
	@Child(name="expansion", order=18, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion",
		formalDefinition="A value set can also be \"expanded\", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed"
	)
	private Expansion myExpansion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myUrl,  myIdentifier,  myVersion,  myName,  myStatus,  myExperimental,  myPublisher,  myContact,  myDate,  myLockedDate,  myDescription,  myUseContext,  myImmutable,  myRequirements,  myCopyright,  myExtensible,  myCodeSystem,  myCompose,  myExpansion);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myUrl, myIdentifier, myVersion, myName, myStatus, myExperimental, myPublisher, myContact, myDate, myLockedDate, myDescription, myUseContext, myImmutable, myRequirements, myCopyright, myExtensible, myCodeSystem, myCompose, myExpansion);
	}

	/**
	 * Gets the value(s) for <b>url</b> (ValueSet.url).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published
     * </p> 
	 */
	public UriDt getUrlElement() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	
	/**
	 * Gets the value(s) for <b>url</b> (ValueSet.url).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published
     * </p> 
	 */
	public String getUrl() {  
		return getUrlElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>url</b> (ValueSet.url)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published
     * </p> 
	 */
	public ValueSet setUrl(UriDt theValue) {
		myUrl = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>url</b> (ValueSet.url)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URL that is used to identify this value set when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this value set is (or will be) published
     * </p> 
	 */
	public ValueSet setUrl( String theUri) {
		myUrl = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> (ValueSet.identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier that is used to identify this value set when it is represented in other formats, or referenced in a specification, model, design or an instance.
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (ValueSet.identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Formal identifier that is used to identify this value set when it is represented in other formats, or referenced in a specification, model, design or an instance.
     * </p> 
	 */
	public ValueSet setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>version</b> (ValueSet.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> (ValueSet.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> (ValueSet.version)
	 *
     * <p>
     * <b>Definition:</b>
     * Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public ValueSet setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> (ValueSet.version)
	 *
     * <p>
     * <b>Definition:</b>
     * Used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public ValueSet setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (ValueSet.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (ValueSet.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (ValueSet.name)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public ValueSet setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (ValueSet.name)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public ValueSet setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (ValueSet.status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public CodeDt getStatusElement() {  
		if (myStatus == null) {
			myStatus = new CodeDt();
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (ValueSet.status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (ValueSet.status)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public ValueSet setStatus(CodeDt theValue) {
		myStatus = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>status</b> (ValueSet.status)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public ValueSet setStatus( String theCode) {
		myStatus = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>experimental</b> (ValueSet.experimental).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public BooleanDt getExperimentalElement() {  
		if (myExperimental == null) {
			myExperimental = new BooleanDt();
		}
		return myExperimental;
	}

	
	/**
	 * Gets the value(s) for <b>experimental</b> (ValueSet.experimental).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public Boolean getExperimental() {  
		return getExperimentalElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>experimental</b> (ValueSet.experimental)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public ValueSet setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>experimental</b> (ValueSet.experimental)
	 *
     * <p>
     * <b>Definition:</b>
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public ValueSet setExperimental( boolean theBoolean) {
		myExperimental = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>publisher</b> (ValueSet.publisher).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public StringDt getPublisherElement() {  
		if (myPublisher == null) {
			myPublisher = new StringDt();
		}
		return myPublisher;
	}

	
	/**
	 * Gets the value(s) for <b>publisher</b> (ValueSet.publisher).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public String getPublisher() {  
		return getPublisherElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>publisher</b> (ValueSet.publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public ValueSet setPublisher(StringDt theValue) {
		myPublisher = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>publisher</b> (ValueSet.publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public ValueSet setPublisher( String theString) {
		myPublisher = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contact</b> (ValueSet.contact).
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
	 * Sets the value(s) for <b>contact</b> (ValueSet.contact)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ValueSet setContact(java.util.List<Contact> theValue) {
		myContact = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contact</b> (ValueSet.contact)
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
	 * Adds a given new value for <b>contact</b> (ValueSet.contact)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Contacts to assist a user in finding and communicating with the publisher
	 * </p>
	 * @param theValue The contact to add (must not be <code>null</code>)
	 */
	public ValueSet addContact(Contact theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getContact().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>contact</b> (ValueSet.contact),
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
	 * Gets the value(s) for <b>date</b> (ValueSet.date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition')
     * </p> 
	 */
	public DateTimeDt getDateElement() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	
	/**
	 * Gets the value(s) for <b>date</b> (ValueSet.date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition')
     * </p> 
	 */
	public Date getDate() {  
		return getDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>date</b> (ValueSet.date)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition')
     * </p> 
	 */
	public ValueSet setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> (ValueSet.date)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition')
     * </p> 
	 */
	public ValueSet setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (ValueSet.date)
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the implementation guide changes (e.g. the 'content logical definition')
     * </p> 
	 */
	public ValueSet setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>lockedDate</b> (ValueSet.lockedDate).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date
     * </p> 
	 */
	public DateDt getLockedDateElement() {  
		if (myLockedDate == null) {
			myLockedDate = new DateDt();
		}
		return myLockedDate;
	}

	
	/**
	 * Gets the value(s) for <b>lockedDate</b> (ValueSet.lockedDate).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date
     * </p> 
	 */
	public Date getLockedDate() {  
		return getLockedDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>lockedDate</b> (ValueSet.lockedDate)
	 *
     * <p>
     * <b>Definition:</b>
     * If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date
     * </p> 
	 */
	public ValueSet setLockedDate(DateDt theValue) {
		myLockedDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>lockedDate</b> (ValueSet.lockedDate)
	 *
     * <p>
     * <b>Definition:</b>
     * If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date
     * </p> 
	 */
	public ValueSet setLockedDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myLockedDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>lockedDate</b> (ValueSet.lockedDate)
	 *
     * <p>
     * <b>Definition:</b>
     * If a Locked Date is defined, then the Content Logical Definition must be evaluated using the current version of all referenced code system(s) and value sets as of the Locked Date
     * </p> 
	 */
	public ValueSet setLockedDateWithDayPrecision( Date theDate) {
		myLockedDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (ValueSet.description).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, \&quot;the semantic space\&quot; to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.
     * </p> 
	 */
	public StringDt getDescriptionElement() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	
	/**
	 * Gets the value(s) for <b>description</b> (ValueSet.description).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, \&quot;the semantic space\&quot; to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.
     * </p> 
	 */
	public String getDescription() {  
		return getDescriptionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>description</b> (ValueSet.description)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, \&quot;the semantic space\&quot; to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.
     * </p> 
	 */
	public ValueSet setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> (ValueSet.description)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, \&quot;the semantic space\&quot; to be included in the value set, conditions of use, etc. The description may include a list of expected usages for the value set and can also describe the approach taken to build the value set.
     * </p> 
	 */
	public ValueSet setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>useContext</b> (ValueSet.useContext).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getUseContext() {  
		if (myUseContext == null) {
			myUseContext = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myUseContext;
	}

	/**
	 * Sets the value(s) for <b>useContext</b> (ValueSet.useContext)
	 *
     * <p>
     * <b>Definition:</b>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions.
     * </p> 
	 */
	public ValueSet setUseContext(java.util.List<CodeableConceptDt> theValue) {
		myUseContext = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>useContext</b> (ValueSet.useContext)
	 *
     * <p>
     * <b>Definition:</b>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions.
     * </p> 
	 */
	public CodeableConceptDt addUseContext() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getUseContext().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>useContext</b> (ValueSet.useContext)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions.
	 * </p>
	 * @param theValue The useContext to add (must not be <code>null</code>)
	 */
	public ValueSet addUseContext(CodeableConceptDt theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getUseContext().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>useContext</b> (ValueSet.useContext),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of value set definitions.
     * </p> 
	 */
	public CodeableConceptDt getUseContextFirstRep() {
		if (getUseContext().isEmpty()) {
			return addUseContext();
		}
		return getUseContext().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>immutable</b> (ValueSet.immutable).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change
     * </p> 
	 */
	public BooleanDt getImmutableElement() {  
		if (myImmutable == null) {
			myImmutable = new BooleanDt();
		}
		return myImmutable;
	}

	
	/**
	 * Gets the value(s) for <b>immutable</b> (ValueSet.immutable).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change
     * </p> 
	 */
	public Boolean getImmutable() {  
		return getImmutableElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>immutable</b> (ValueSet.immutable)
	 *
     * <p>
     * <b>Definition:</b>
     * If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change
     * </p> 
	 */
	public ValueSet setImmutable(BooleanDt theValue) {
		myImmutable = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>immutable</b> (ValueSet.immutable)
	 *
     * <p>
     * <b>Definition:</b>
     * If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change
     * </p> 
	 */
	public ValueSet setImmutable( boolean theBoolean) {
		myImmutable = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>requirements</b> (ValueSet.requirements).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this value set is needed and why it's been constrained as it has
     * </p> 
	 */
	public StringDt getRequirementsElement() {  
		if (myRequirements == null) {
			myRequirements = new StringDt();
		}
		return myRequirements;
	}

	
	/**
	 * Gets the value(s) for <b>requirements</b> (ValueSet.requirements).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this value set is needed and why it's been constrained as it has
     * </p> 
	 */
	public String getRequirements() {  
		return getRequirementsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>requirements</b> (ValueSet.requirements)
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this value set is needed and why it's been constrained as it has
     * </p> 
	 */
	public ValueSet setRequirements(StringDt theValue) {
		myRequirements = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>requirements</b> (ValueSet.requirements)
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this value set is needed and why it's been constrained as it has
     * </p> 
	 */
	public ValueSet setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>copyright</b> (ValueSet.copyright).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set
     * </p> 
	 */
	public StringDt getCopyrightElement() {  
		if (myCopyright == null) {
			myCopyright = new StringDt();
		}
		return myCopyright;
	}

	
	/**
	 * Gets the value(s) for <b>copyright</b> (ValueSet.copyright).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set
     * </p> 
	 */
	public String getCopyright() {  
		return getCopyrightElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>copyright</b> (ValueSet.copyright)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set
     * </p> 
	 */
	public ValueSet setCopyright(StringDt theValue) {
		myCopyright = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>copyright</b> (ValueSet.copyright)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the value set
     * </p> 
	 */
	public ValueSet setCopyright( String theString) {
		myCopyright = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>extensible</b> (ValueSet.extensible).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public BooleanDt getExtensibleElement() {  
		if (myExtensible == null) {
			myExtensible = new BooleanDt();
		}
		return myExtensible;
	}

	
	/**
	 * Gets the value(s) for <b>extensible</b> (ValueSet.extensible).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public Boolean getExtensible() {  
		return getExtensibleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>extensible</b> (ValueSet.extensible)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public ValueSet setExtensible(BooleanDt theValue) {
		myExtensible = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>extensible</b> (ValueSet.extensible)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public ValueSet setExtensible( boolean theBoolean) {
		myExtensible = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>codeSystem</b> (ValueSet.codeSystem).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of an code system, inlined into the value set (as a packaging convenience). Note that the inline code system may be used from other value sets by referring to it's (codeSystem.system) directly
     * </p> 
	 */
	public CodeSystem getCodeSystem() {  
		if (myCodeSystem == null) {
			myCodeSystem = new CodeSystem();
		}
		return myCodeSystem;
	}

	/**
	 * Sets the value(s) for <b>codeSystem</b> (ValueSet.codeSystem)
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of an code system, inlined into the value set (as a packaging convenience). Note that the inline code system may be used from other value sets by referring to it's (codeSystem.system) directly
     * </p> 
	 */
	public ValueSet setCodeSystem(CodeSystem theValue) {
		myCodeSystem = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>compose</b> (ValueSet.compose).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of criteria that provide the content logical definition of the value set by including or excluding codes from outside this value set
     * </p> 
	 */
	public Compose getCompose() {  
		if (myCompose == null) {
			myCompose = new Compose();
		}
		return myCompose;
	}

	/**
	 * Sets the value(s) for <b>compose</b> (ValueSet.compose)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of criteria that provide the content logical definition of the value set by including or excluding codes from outside this value set
     * </p> 
	 */
	public ValueSet setCompose(Compose theValue) {
		myCompose = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>expansion</b> (ValueSet.expansion).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A value set can also be \&quot;expanded\&quot;, where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed
     * </p> 
	 */
	public Expansion getExpansion() {  
		if (myExpansion == null) {
			myExpansion = new Expansion();
		}
		return myExpansion;
	}

	/**
	 * Sets the value(s) for <b>expansion</b> (ValueSet.expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * A value set can also be \&quot;expanded\&quot;, where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed
     * </p> 
	 */
	public ValueSet setExpansion(Expansion theValue) {
		myExpansion = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>ValueSet.contact</b> (ValueSet.contact)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	@Block()	
	public static class Contact 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.contact.name",
		formalDefinition="The name of an individual to contact regarding the value set"
	)
	private StringDt myName;
	
	@Child(name="telecom", type=ContactPointDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.contact.telecom",
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
	 * Gets the value(s) for <b>name</b> (ValueSet.contact.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of an individual to contact regarding the value set
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (ValueSet.contact.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of an individual to contact regarding the value set
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (ValueSet.contact.name)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of an individual to contact regarding the value set
     * </p> 
	 */
	public Contact setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (ValueSet.contact.name)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of an individual to contact regarding the value set
     * </p> 
	 */
	public Contact setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>telecom</b> (ValueSet.contact.telecom).
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
	 * Sets the value(s) for <b>telecom</b> (ValueSet.contact.telecom)
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
	 * Adds and returns a new value for <b>telecom</b> (ValueSet.contact.telecom)
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
	 * Adds a given new value for <b>telecom</b> (ValueSet.contact.telecom)
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
	 * Gets the first repetition for <b>telecom</b> (ValueSet.contact.telecom),
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
	 * Block class for child element: <b>ValueSet.codeSystem</b> (ValueSet.codeSystem)
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of an code system, inlined into the value set (as a packaging convenience). Note that the inline code system may be used from other value sets by referring to it's (codeSystem.system) directly
     * </p> 
	 */
	@Block()	
	public static class CodeSystem 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.system",
		formalDefinition="An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system"
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.version",
		formalDefinition="The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked"
	)
	private StringDt myVersion;
	
	@Child(name="caseSensitive", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.caseSensitive",
		formalDefinition="If code comparison is case sensitive when codes within this system are compared to each other"
	)
	private BooleanDt myCaseSensitive;
	
	@Child(name="concept", order=3, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept",
		formalDefinition="Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are"
	)
	private java.util.List<CodeSystemConcept> myConcept;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myVersion,  myCaseSensitive,  myConcept);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myVersion, myCaseSensitive, myConcept);
	}

	/**
	 * Gets the value(s) for <b>system</b> (ValueSet.codeSystem.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system
     * </p> 
	 */
	public UriDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	
	/**
	 * Gets the value(s) for <b>system</b> (ValueSet.codeSystem.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system
     * </p> 
	 */
	public String getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> (ValueSet.codeSystem.system)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system
     * </p> 
	 */
	public CodeSystem setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>system</b> (ValueSet.codeSystem.system)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI that is used to reference this code system, including in [Coding]{datatypes.html#Coding}.system
     * </p> 
	 */
	public CodeSystem setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (ValueSet.codeSystem.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> (ValueSet.codeSystem.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> (ValueSet.codeSystem.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public CodeSystem setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> (ValueSet.codeSystem.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public CodeSystem setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>caseSensitive</b> (ValueSet.codeSystem.caseSensitive).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public BooleanDt getCaseSensitiveElement() {  
		if (myCaseSensitive == null) {
			myCaseSensitive = new BooleanDt();
		}
		return myCaseSensitive;
	}

	
	/**
	 * Gets the value(s) for <b>caseSensitive</b> (ValueSet.codeSystem.caseSensitive).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public Boolean getCaseSensitive() {  
		return getCaseSensitiveElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>caseSensitive</b> (ValueSet.codeSystem.caseSensitive)
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public CodeSystem setCaseSensitive(BooleanDt theValue) {
		myCaseSensitive = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>caseSensitive</b> (ValueSet.codeSystem.caseSensitive)
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public CodeSystem setCaseSensitive( boolean theBoolean) {
		myCaseSensitive = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>concept</b> (ValueSet.codeSystem.concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are
     * </p> 
	 */
	public java.util.List<CodeSystemConcept> getConcept() {  
		if (myConcept == null) {
			myConcept = new java.util.ArrayList<CodeSystemConcept>();
		}
		return myConcept;
	}

	/**
	 * Sets the value(s) for <b>concept</b> (ValueSet.codeSystem.concept)
	 *
     * <p>
     * <b>Definition:</b>
     * Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are
     * </p> 
	 */
	public CodeSystem setConcept(java.util.List<CodeSystemConcept> theValue) {
		myConcept = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>concept</b> (ValueSet.codeSystem.concept)
	 *
     * <p>
     * <b>Definition:</b>
     * Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are
     * </p> 
	 */
	public CodeSystemConcept addConcept() {
		CodeSystemConcept newType = new CodeSystemConcept();
		getConcept().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>concept</b> (ValueSet.codeSystem.concept)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are
	 * </p>
	 * @param theValue The concept to add (must not be <code>null</code>)
	 */
	public CodeSystem addConcept(CodeSystemConcept theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getConcept().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>concept</b> (ValueSet.codeSystem.concept),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are
     * </p> 
	 */
	public CodeSystemConcept getConceptFirstRep() {
		if (getConcept().isEmpty()) {
			return addConcept();
		}
		return getConcept().get(0); 
	}
  


	}

	/**
	 * Block class for child element: <b>ValueSet.codeSystem.concept</b> (ValueSet.codeSystem.concept)
	 *
     * <p>
     * <b>Definition:</b>
     * Concepts that are in the code system. The concept definitions are inherently heirarchical, but the definitions must be consulted to determine what the meaning of the heirachical relationships are
     * </p> 
	 */
	@Block()	
	public static class CodeSystemConcept 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept.code",
		formalDefinition="A code - a text symbol - that uniquely identifies the concept within the code system"
	)
	private CodeDt myCode;
	
	@Child(name="abstract", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept.abstract",
		formalDefinition="If this code is not for use as a real concept"
	)
	private BooleanDt myAbstract;
	
	@Child(name="display", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept.display",
		formalDefinition="A human readable string that is the recommended default way to present this concept to a user"
	)
	private StringDt myDisplay;
	
	@Child(name="definition", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept.definition",
		formalDefinition="The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept"
	)
	private StringDt myDefinition;
	
	@Child(name="designation", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept.designation",
		formalDefinition="Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc"
	)
	private java.util.List<CodeSystemConceptDesignation> myDesignation;
	
	@Child(name="concept", type=CodeSystemConcept.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept.concept",
		formalDefinition="Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts"
	)
	private java.util.List<CodeSystemConcept> myConcept;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myAbstract,  myDisplay,  myDefinition,  myDesignation,  myConcept);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myAbstract, myDisplay, myDefinition, myDesignation, myConcept);
	}

	/**
	 * Gets the value(s) for <b>code</b> (ValueSet.codeSystem.concept.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code - a text symbol - that uniquely identifies the concept within the code system
     * </p> 
	 */
	public CodeDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (ValueSet.codeSystem.concept.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code - a text symbol - that uniquely identifies the concept within the code system
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (ValueSet.codeSystem.concept.code)
	 *
     * <p>
     * <b>Definition:</b>
     * A code - a text symbol - that uniquely identifies the concept within the code system
     * </p> 
	 */
	public CodeSystemConcept setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> (ValueSet.codeSystem.concept.code)
	 *
     * <p>
     * <b>Definition:</b>
     * A code - a text symbol - that uniquely identifies the concept within the code system
     * </p> 
	 */
	public CodeSystemConcept setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>abstract</b> (ValueSet.codeSystem.concept.abstract).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public BooleanDt getAbstractElement() {  
		if (myAbstract == null) {
			myAbstract = new BooleanDt();
		}
		return myAbstract;
	}

	
	/**
	 * Gets the value(s) for <b>abstract</b> (ValueSet.codeSystem.concept.abstract).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public Boolean getAbstract() {  
		return getAbstractElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>abstract</b> (ValueSet.codeSystem.concept.abstract)
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public CodeSystemConcept setAbstract(BooleanDt theValue) {
		myAbstract = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>abstract</b> (ValueSet.codeSystem.concept.abstract)
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public CodeSystemConcept setAbstract( boolean theBoolean) {
		myAbstract = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (ValueSet.codeSystem.concept.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human readable string that is the recommended default way to present this concept to a user
     * </p> 
	 */
	public StringDt getDisplayElement() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	
	/**
	 * Gets the value(s) for <b>display</b> (ValueSet.codeSystem.concept.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human readable string that is the recommended default way to present this concept to a user
     * </p> 
	 */
	public String getDisplay() {  
		return getDisplayElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>display</b> (ValueSet.codeSystem.concept.display)
	 *
     * <p>
     * <b>Definition:</b>
     * A human readable string that is the recommended default way to present this concept to a user
     * </p> 
	 */
	public CodeSystemConcept setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> (ValueSet.codeSystem.concept.display)
	 *
     * <p>
     * <b>Definition:</b>
     * A human readable string that is the recommended default way to present this concept to a user
     * </p> 
	 */
	public CodeSystemConcept setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> (ValueSet.codeSystem.concept.definition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public StringDt getDefinitionElement() {  
		if (myDefinition == null) {
			myDefinition = new StringDt();
		}
		return myDefinition;
	}

	
	/**
	 * Gets the value(s) for <b>definition</b> (ValueSet.codeSystem.concept.definition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public String getDefinition() {  
		return getDefinitionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>definition</b> (ValueSet.codeSystem.concept.definition)
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public CodeSystemConcept setDefinition(StringDt theValue) {
		myDefinition = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>definition</b> (ValueSet.codeSystem.concept.definition)
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. The value set resource does not make formal definitions required, because of the prevalence of legacy systems. However, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public CodeSystemConcept setDefinition( String theString) {
		myDefinition = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>designation</b> (ValueSet.codeSystem.concept.designation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc
     * </p> 
	 */
	public java.util.List<CodeSystemConceptDesignation> getDesignation() {  
		if (myDesignation == null) {
			myDesignation = new java.util.ArrayList<CodeSystemConceptDesignation>();
		}
		return myDesignation;
	}

	/**
	 * Sets the value(s) for <b>designation</b> (ValueSet.codeSystem.concept.designation)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc
     * </p> 
	 */
	public CodeSystemConcept setDesignation(java.util.List<CodeSystemConceptDesignation> theValue) {
		myDesignation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>designation</b> (ValueSet.codeSystem.concept.designation)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc
     * </p> 
	 */
	public CodeSystemConceptDesignation addDesignation() {
		CodeSystemConceptDesignation newType = new CodeSystemConceptDesignation();
		getDesignation().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>designation</b> (ValueSet.codeSystem.concept.designation)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc
	 * </p>
	 * @param theValue The designation to add (must not be <code>null</code>)
	 */
	public CodeSystemConcept addDesignation(CodeSystemConceptDesignation theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getDesignation().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>designation</b> (ValueSet.codeSystem.concept.designation),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc
     * </p> 
	 */
	public CodeSystemConceptDesignation getDesignationFirstRep() {
		if (getDesignation().isEmpty()) {
			return addDesignation();
		}
		return getDesignation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>concept</b> (ValueSet.codeSystem.concept.concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts
     * </p> 
	 */
	public java.util.List<CodeSystemConcept> getConcept() {  
		if (myConcept == null) {
			myConcept = new java.util.ArrayList<CodeSystemConcept>();
		}
		return myConcept;
	}

	/**
	 * Sets the value(s) for <b>concept</b> (ValueSet.codeSystem.concept.concept)
	 *
     * <p>
     * <b>Definition:</b>
     * Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts
     * </p> 
	 */
	public CodeSystemConcept setConcept(java.util.List<CodeSystemConcept> theValue) {
		myConcept = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>concept</b> (ValueSet.codeSystem.concept.concept)
	 *
     * <p>
     * <b>Definition:</b>
     * Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts
     * </p> 
	 */
	public CodeSystemConcept addConcept() {
		CodeSystemConcept newType = new CodeSystemConcept();
		getConcept().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>concept</b> (ValueSet.codeSystem.concept.concept)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts
	 * </p>
	 * @param theValue The concept to add (must not be <code>null</code>)
	 */
	public CodeSystemConcept addConcept(CodeSystemConcept theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getConcept().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>concept</b> (ValueSet.codeSystem.concept.concept),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Child Concepts - a heirarchy of concepts. The nature of the relationships is variable (is-a / contains / categorises) and can only be determined by examining the definitions of the concepts
     * </p> 
	 */
	public CodeSystemConcept getConceptFirstRep() {
		if (getConcept().isEmpty()) {
			return addConcept();
		}
		return getConcept().get(0); 
	}
  


	}

	/**
	 * Block class for child element: <b>ValueSet.codeSystem.concept.designation</b> (ValueSet.codeSystem.concept.designation)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc
     * </p> 
	 */
	@Block()	
	public static class CodeSystemConceptDesignation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="language", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept.designation.language",
		formalDefinition="The language this designation is defined for"
	)
	private CodeDt myLanguage;
	
	@Child(name="use", type=CodingDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept.designation.use",
		formalDefinition="A code that details how this designation would be used"
	)
	private CodingDt myUse;
	
	@Child(name="value", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.codeSystem.concept.designation.value",
		formalDefinition="The text value for this designation"
	)
	private StringDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLanguage,  myUse,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLanguage, myUse, myValue);
	}

	/**
	 * Gets the value(s) for <b>language</b> (ValueSet.codeSystem.concept.designation.language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The language this designation is defined for
     * </p> 
	 */
	public CodeDt getLanguageElement() {  
		if (myLanguage == null) {
			myLanguage = new CodeDt();
		}
		return myLanguage;
	}

	
	/**
	 * Gets the value(s) for <b>language</b> (ValueSet.codeSystem.concept.designation.language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The language this designation is defined for
     * </p> 
	 */
	public String getLanguage() {  
		return getLanguageElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>language</b> (ValueSet.codeSystem.concept.designation.language)
	 *
     * <p>
     * <b>Definition:</b>
     * The language this designation is defined for
     * </p> 
	 */
	public CodeSystemConceptDesignation setLanguage(CodeDt theValue) {
		myLanguage = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>language</b> (ValueSet.codeSystem.concept.designation.language)
	 *
     * <p>
     * <b>Definition:</b>
     * The language this designation is defined for
     * </p> 
	 */
	public CodeSystemConceptDesignation setLanguage( String theCode) {
		myLanguage = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>use</b> (ValueSet.codeSystem.concept.designation.use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that details how this designation would be used
     * </p> 
	 */
	public CodingDt getUse() {  
		if (myUse == null) {
			myUse = new CodingDt();
		}
		return myUse;
	}

	/**
	 * Sets the value(s) for <b>use</b> (ValueSet.codeSystem.concept.designation.use)
	 *
     * <p>
     * <b>Definition:</b>
     * A code that details how this designation would be used
     * </p> 
	 */
	public CodeSystemConceptDesignation setUse(CodingDt theValue) {
		myUse = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>value</b> (ValueSet.codeSystem.concept.designation.value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The text value for this designation
     * </p> 
	 */
	public StringDt getValueElement() {  
		if (myValue == null) {
			myValue = new StringDt();
		}
		return myValue;
	}

	
	/**
	 * Gets the value(s) for <b>value</b> (ValueSet.codeSystem.concept.designation.value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The text value for this designation
     * </p> 
	 */
	public String getValue() {  
		return getValueElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>value</b> (ValueSet.codeSystem.concept.designation.value)
	 *
     * <p>
     * <b>Definition:</b>
     * The text value for this designation
     * </p> 
	 */
	public CodeSystemConceptDesignation setValue(StringDt theValue) {
		myValue = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>value</b> (ValueSet.codeSystem.concept.designation.value)
	 *
     * <p>
     * <b>Definition:</b>
     * The text value for this designation
     * </p> 
	 */
	public CodeSystemConceptDesignation setValue( String theString) {
		myValue = new StringDt(theString); 
		return this; 
	}

 


	}




	/**
	 * Block class for child element: <b>ValueSet.compose</b> (ValueSet.compose)
	 *
     * <p>
     * <b>Definition:</b>
     * A set of criteria that provide the content logical definition of the value set by including or excluding codes from outside this value set
     * </p> 
	 */
	@Block()	
	public static class Compose 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="import", type=UriDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.compose.import",
		formalDefinition="Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri"
	)
	private java.util.List<UriDt> myImport;
	
	@Child(name="include", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.compose.include",
		formalDefinition="Include one or more codes from a code system"
	)
	private java.util.List<ComposeInclude> myInclude;
	
	@Child(name="exclude", type=ComposeInclude.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.compose.exclude",
		formalDefinition="Exclude one or more codes from the value set"
	)
	private java.util.List<ComposeInclude> myExclude;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myImport,  myInclude,  myExclude);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myImport, myInclude, myExclude);
	}

	/**
	 * Gets the value(s) for <b>import</b> (ValueSet.compose.import).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri
     * </p> 
	 */
	public java.util.List<UriDt> getImport() {  
		if (myImport == null) {
			myImport = new java.util.ArrayList<UriDt>();
		}
		return myImport;
	}

	/**
	 * Sets the value(s) for <b>import</b> (ValueSet.compose.import)
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri
     * </p> 
	 */
	public Compose setImport(java.util.List<UriDt> theValue) {
		myImport = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>import</b> (ValueSet.compose.import)
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri
     * </p> 
	 */
	public UriDt addImport() {
		UriDt newType = new UriDt();
		getImport().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>import</b> (ValueSet.compose.import)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri
	 * </p>
	 * @param theValue The import to add (must not be <code>null</code>)
	 */
	public Compose addImport(UriDt theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getImport().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>import</b> (ValueSet.compose.import),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri
     * </p> 
	 */
	public UriDt getImportFirstRep() {
		if (getImport().isEmpty()) {
			return addImport();
		}
		return getImport().get(0); 
	}
 	/**
	 * Adds a new value for <b>import</b> (ValueSet.compose.import)
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set. This is an absolute URI that is a reference to ValueSet.uri
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Compose addImport( String theUri) {
		if (myImport == null) {
			myImport = new java.util.ArrayList<UriDt>();
		}
		myImport.add(new UriDt(theUri));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>include</b> (ValueSet.compose.include).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	public java.util.List<ComposeInclude> getInclude() {  
		if (myInclude == null) {
			myInclude = new java.util.ArrayList<ComposeInclude>();
		}
		return myInclude;
	}

	/**
	 * Sets the value(s) for <b>include</b> (ValueSet.compose.include)
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	public Compose setInclude(java.util.List<ComposeInclude> theValue) {
		myInclude = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>include</b> (ValueSet.compose.include)
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	public ComposeInclude addInclude() {
		ComposeInclude newType = new ComposeInclude();
		getInclude().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>include</b> (ValueSet.compose.include)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Include one or more codes from a code system
	 * </p>
	 * @param theValue The include to add (must not be <code>null</code>)
	 */
	public Compose addInclude(ComposeInclude theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getInclude().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>include</b> (ValueSet.compose.include),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	public ComposeInclude getIncludeFirstRep() {
		if (getInclude().isEmpty()) {
			return addInclude();
		}
		return getInclude().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>exclude</b> (ValueSet.compose.exclude).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Exclude one or more codes from the value set
     * </p> 
	 */
	public java.util.List<ComposeInclude> getExclude() {  
		if (myExclude == null) {
			myExclude = new java.util.ArrayList<ComposeInclude>();
		}
		return myExclude;
	}

	/**
	 * Sets the value(s) for <b>exclude</b> (ValueSet.compose.exclude)
	 *
     * <p>
     * <b>Definition:</b>
     * Exclude one or more codes from the value set
     * </p> 
	 */
	public Compose setExclude(java.util.List<ComposeInclude> theValue) {
		myExclude = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>exclude</b> (ValueSet.compose.exclude)
	 *
     * <p>
     * <b>Definition:</b>
     * Exclude one or more codes from the value set
     * </p> 
	 */
	public ComposeInclude addExclude() {
		ComposeInclude newType = new ComposeInclude();
		getExclude().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>exclude</b> (ValueSet.compose.exclude)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Exclude one or more codes from the value set
	 * </p>
	 * @param theValue The exclude to add (must not be <code>null</code>)
	 */
	public Compose addExclude(ComposeInclude theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getExclude().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>exclude</b> (ValueSet.compose.exclude),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Exclude one or more codes from the value set
     * </p> 
	 */
	public ComposeInclude getExcludeFirstRep() {
		if (getExclude().isEmpty()) {
			return addExclude();
		}
		return getExclude().get(0); 
	}
  


	}

	/**
	 * Block class for child element: <b>ValueSet.compose.include</b> (ValueSet.compose.include)
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	@Block()	
	public static class ComposeInclude 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.compose.include.system",
		formalDefinition="An absolute URI which is the code system from which the selected codes come from"
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.compose.include.version",
		formalDefinition="The version of the code system that the codes are selected from"
	)
	private StringDt myVersion;
	
	@Child(name="concept", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.compose.include.concept",
		formalDefinition="Specifies a concept to be included or excluded."
	)
	private java.util.List<ComposeIncludeConcept> myConcept;
	
	@Child(name="filter", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.compose.include.filter",
		formalDefinition="Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true."
	)
	private java.util.List<ComposeIncludeFilter> myFilter;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myVersion,  myConcept,  myFilter);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myVersion, myConcept, myFilter);
	}

	/**
	 * Gets the value(s) for <b>system</b> (ValueSet.compose.include.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI which is the code system from which the selected codes come from
     * </p> 
	 */
	public UriDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	
	/**
	 * Gets the value(s) for <b>system</b> (ValueSet.compose.include.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI which is the code system from which the selected codes come from
     * </p> 
	 */
	public String getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> (ValueSet.compose.include.system)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI which is the code system from which the selected codes come from
     * </p> 
	 */
	public ComposeInclude setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>system</b> (ValueSet.compose.include.system)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI which is the code system from which the selected codes come from
     * </p> 
	 */
	public ComposeInclude setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (ValueSet.compose.include.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system that the codes are selected from
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> (ValueSet.compose.include.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system that the codes are selected from
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> (ValueSet.compose.include.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system that the codes are selected from
     * </p> 
	 */
	public ComposeInclude setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> (ValueSet.compose.include.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system that the codes are selected from
     * </p> 
	 */
	public ComposeInclude setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>concept</b> (ValueSet.compose.include.concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a concept to be included or excluded.
     * </p> 
	 */
	public java.util.List<ComposeIncludeConcept> getConcept() {  
		if (myConcept == null) {
			myConcept = new java.util.ArrayList<ComposeIncludeConcept>();
		}
		return myConcept;
	}

	/**
	 * Sets the value(s) for <b>concept</b> (ValueSet.compose.include.concept)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a concept to be included or excluded.
     * </p> 
	 */
	public ComposeInclude setConcept(java.util.List<ComposeIncludeConcept> theValue) {
		myConcept = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>concept</b> (ValueSet.compose.include.concept)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a concept to be included or excluded.
     * </p> 
	 */
	public ComposeIncludeConcept addConcept() {
		ComposeIncludeConcept newType = new ComposeIncludeConcept();
		getConcept().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>concept</b> (ValueSet.compose.include.concept)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Specifies a concept to be included or excluded.
	 * </p>
	 * @param theValue The concept to add (must not be <code>null</code>)
	 */
	public ComposeInclude addConcept(ComposeIncludeConcept theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getConcept().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>concept</b> (ValueSet.compose.include.concept),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a concept to be included or excluded.
     * </p> 
	 */
	public ComposeIncludeConcept getConceptFirstRep() {
		if (getConcept().isEmpty()) {
			return addConcept();
		}
		return getConcept().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>filter</b> (ValueSet.compose.include.filter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	public java.util.List<ComposeIncludeFilter> getFilter() {  
		if (myFilter == null) {
			myFilter = new java.util.ArrayList<ComposeIncludeFilter>();
		}
		return myFilter;
	}

	/**
	 * Sets the value(s) for <b>filter</b> (ValueSet.compose.include.filter)
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	public ComposeInclude setFilter(java.util.List<ComposeIncludeFilter> theValue) {
		myFilter = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>filter</b> (ValueSet.compose.include.filter)
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	public ComposeIncludeFilter addFilter() {
		ComposeIncludeFilter newType = new ComposeIncludeFilter();
		getFilter().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>filter</b> (ValueSet.compose.include.filter)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
	 * </p>
	 * @param theValue The filter to add (must not be <code>null</code>)
	 */
	public ComposeInclude addFilter(ComposeIncludeFilter theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getFilter().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>filter</b> (ValueSet.compose.include.filter),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	public ComposeIncludeFilter getFilterFirstRep() {
		if (getFilter().isEmpty()) {
			return addFilter();
		}
		return getFilter().get(0); 
	}
  


	}

	/**
	 * Block class for child element: <b>ValueSet.compose.include.concept</b> (ValueSet.compose.include.concept)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a concept to be included or excluded.
     * </p> 
	 */
	@Block()	
	public static class ComposeIncludeConcept 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.compose.include.concept.code",
		formalDefinition="Specifies a code for the concept to be included or excluded"
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.compose.include.concept.display",
		formalDefinition="The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system"
	)
	private StringDt myDisplay;
	
	@Child(name="designation", type=CodeSystemConceptDesignation.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.compose.include.concept.designation",
		formalDefinition="Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc"
	)
	private java.util.List<CodeSystemConceptDesignation> myDesignation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDisplay,  myDesignation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myDisplay, myDesignation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (ValueSet.compose.include.concept.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code for the concept to be included or excluded
     * </p> 
	 */
	public CodeDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (ValueSet.compose.include.concept.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code for the concept to be included or excluded
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (ValueSet.compose.include.concept.code)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code for the concept to be included or excluded
     * </p> 
	 */
	public ComposeIncludeConcept setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> (ValueSet.compose.include.concept.code)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code for the concept to be included or excluded
     * </p> 
	 */
	public ComposeIncludeConcept setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (ValueSet.compose.include.concept.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system
     * </p> 
	 */
	public StringDt getDisplayElement() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	
	/**
	 * Gets the value(s) for <b>display</b> (ValueSet.compose.include.concept.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system
     * </p> 
	 */
	public String getDisplay() {  
		return getDisplayElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>display</b> (ValueSet.compose.include.concept.display)
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system
     * </p> 
	 */
	public ComposeIncludeConcept setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> (ValueSet.compose.include.concept.display)
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system
     * </p> 
	 */
	public ComposeIncludeConcept setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>designation</b> (ValueSet.compose.include.concept.designation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc
     * </p> 
	 */
	public java.util.List<CodeSystemConceptDesignation> getDesignation() {  
		if (myDesignation == null) {
			myDesignation = new java.util.ArrayList<CodeSystemConceptDesignation>();
		}
		return myDesignation;
	}

	/**
	 * Sets the value(s) for <b>designation</b> (ValueSet.compose.include.concept.designation)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc
     * </p> 
	 */
	public ComposeIncludeConcept setDesignation(java.util.List<CodeSystemConceptDesignation> theValue) {
		myDesignation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>designation</b> (ValueSet.compose.include.concept.designation)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc
     * </p> 
	 */
	public CodeSystemConceptDesignation addDesignation() {
		CodeSystemConceptDesignation newType = new CodeSystemConceptDesignation();
		getDesignation().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>designation</b> (ValueSet.compose.include.concept.designation)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc
	 * </p>
	 * @param theValue The designation to add (must not be <code>null</code>)
	 */
	public ComposeIncludeConcept addDesignation(CodeSystemConceptDesignation theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getDesignation().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>designation</b> (ValueSet.compose.include.concept.designation),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for this concept when used in this value set - other languages, aliases, specialized purposes, used for particular purposes, etc
     * </p> 
	 */
	public CodeSystemConceptDesignation getDesignationFirstRep() {
		if (getDesignation().isEmpty()) {
			return addDesignation();
		}
		return getDesignation().get(0); 
	}
  


	}


	/**
	 * Block class for child element: <b>ValueSet.compose.include.filter</b> (ValueSet.compose.include.filter)
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	@Block()	
	public static class ComposeIncludeFilter 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="property", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.compose.include.filter.property",
		formalDefinition="A code that identifies a property defined in the code system"
	)
	private CodeDt myProperty;
	
	@Child(name="op", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.compose.include.filter.op",
		formalDefinition="The kind of operation to perform as a part of the filter criteria"
	)
	private CodeDt myOp;
	
	@Child(name="value", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.compose.include.filter.value",
		formalDefinition="The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value"
	)
	private CodeDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myProperty,  myOp,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myProperty, myOp, myValue);
	}

	/**
	 * Gets the value(s) for <b>property</b> (ValueSet.compose.include.filter.property).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a property defined in the code system
     * </p> 
	 */
	public CodeDt getPropertyElement() {  
		if (myProperty == null) {
			myProperty = new CodeDt();
		}
		return myProperty;
	}

	
	/**
	 * Gets the value(s) for <b>property</b> (ValueSet.compose.include.filter.property).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a property defined in the code system
     * </p> 
	 */
	public String getProperty() {  
		return getPropertyElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>property</b> (ValueSet.compose.include.filter.property)
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a property defined in the code system
     * </p> 
	 */
	public ComposeIncludeFilter setProperty(CodeDt theValue) {
		myProperty = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>property</b> (ValueSet.compose.include.filter.property)
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a property defined in the code system
     * </p> 
	 */
	public ComposeIncludeFilter setProperty( String theCode) {
		myProperty = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>op</b> (ValueSet.compose.include.filter.op).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public CodeDt getOpElement() {  
		if (myOp == null) {
			myOp = new CodeDt();
		}
		return myOp;
	}

	
	/**
	 * Gets the value(s) for <b>op</b> (ValueSet.compose.include.filter.op).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public String getOp() {  
		return getOpElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>op</b> (ValueSet.compose.include.filter.op)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public ComposeIncludeFilter setOp(CodeDt theValue) {
		myOp = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>op</b> (ValueSet.compose.include.filter.op)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public ComposeIncludeFilter setOp( String theCode) {
		myOp = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>value</b> (ValueSet.compose.include.filter.value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value
     * </p> 
	 */
	public CodeDt getValueElement() {  
		if (myValue == null) {
			myValue = new CodeDt();
		}
		return myValue;
	}

	
	/**
	 * Gets the value(s) for <b>value</b> (ValueSet.compose.include.filter.value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value
     * </p> 
	 */
	public String getValue() {  
		return getValueElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>value</b> (ValueSet.compose.include.filter.value)
	 *
     * <p>
     * <b>Definition:</b>
     * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value
     * </p> 
	 */
	public ComposeIncludeFilter setValue(CodeDt theValue) {
		myValue = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>value</b> (ValueSet.compose.include.filter.value)
	 *
     * <p>
     * <b>Definition:</b>
     * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value
     * </p> 
	 */
	public ComposeIncludeFilter setValue( String theCode) {
		myValue = new CodeDt(theCode); 
		return this; 
	}

 


	}




	/**
	 * Block class for child element: <b>ValueSet.expansion</b> (ValueSet.expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * A value set can also be \"expanded\", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed
     * </p> 
	 */
	@Block()	
	public static class Expansion 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.identifier",
		formalDefinition="An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so"
	)
	private UriDt myIdentifier;
	
	@Child(name="timestamp", type=DateTimeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.timestamp",
		formalDefinition="The time at which the expansion was produced by the expanding system."
	)
	private DateTimeDt myTimestamp;
	
	@Child(name="total", type=IntegerDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.total",
		formalDefinition="The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter"
	)
	private IntegerDt myTotal;
	
	@Child(name="offset", type=IntegerDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.offset",
		formalDefinition="If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present"
	)
	private IntegerDt myOffset;
	
	@Child(name="parameter", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.expansion.parameter",
		formalDefinition="A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion"
	)
	private java.util.List<ExpansionParameter> myParameter;
	
	@Child(name="contains", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.expansion.contains",
		formalDefinition="The codes that are contained in the value set expansion"
	)
	private java.util.List<ExpansionContains> myContains;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myTimestamp,  myTotal,  myOffset,  myParameter,  myContains);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myTimestamp, myTotal, myOffset, myParameter, myContains);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (ValueSet.expansion.identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public UriDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new UriDt();
		}
		return myIdentifier;
	}

	
	/**
	 * Gets the value(s) for <b>identifier</b> (ValueSet.expansion.identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public String getIdentifier() {  
		return getIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (ValueSet.expansion.identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public Expansion setIdentifier(UriDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identifier</b> (ValueSet.expansion.identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public Expansion setIdentifier( String theUri) {
		myIdentifier = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>timestamp</b> (ValueSet.expansion.timestamp).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time at which the expansion was produced by the expanding system.
     * </p> 
	 */
	public DateTimeDt getTimestampElement() {  
		if (myTimestamp == null) {
			myTimestamp = new DateTimeDt();
		}
		return myTimestamp;
	}

	
	/**
	 * Gets the value(s) for <b>timestamp</b> (ValueSet.expansion.timestamp).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time at which the expansion was produced by the expanding system.
     * </p> 
	 */
	public Date getTimestamp() {  
		return getTimestampElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>timestamp</b> (ValueSet.expansion.timestamp)
	 *
     * <p>
     * <b>Definition:</b>
     * The time at which the expansion was produced by the expanding system.
     * </p> 
	 */
	public Expansion setTimestamp(DateTimeDt theValue) {
		myTimestamp = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>timestamp</b> (ValueSet.expansion.timestamp)
	 *
     * <p>
     * <b>Definition:</b>
     * The time at which the expansion was produced by the expanding system.
     * </p> 
	 */
	public Expansion setTimestamp( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myTimestamp = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>timestamp</b> (ValueSet.expansion.timestamp)
	 *
     * <p>
     * <b>Definition:</b>
     * The time at which the expansion was produced by the expanding system.
     * </p> 
	 */
	public Expansion setTimestampWithSecondsPrecision( Date theDate) {
		myTimestamp = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>total</b> (ValueSet.expansion.total).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter
     * </p> 
	 */
	public IntegerDt getTotalElement() {  
		if (myTotal == null) {
			myTotal = new IntegerDt();
		}
		return myTotal;
	}

	
	/**
	 * Gets the value(s) for <b>total</b> (ValueSet.expansion.total).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter
     * </p> 
	 */
	public Integer getTotal() {  
		return getTotalElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>total</b> (ValueSet.expansion.total)
	 *
     * <p>
     * <b>Definition:</b>
     * The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter
     * </p> 
	 */
	public Expansion setTotal(IntegerDt theValue) {
		myTotal = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>total</b> (ValueSet.expansion.total)
	 *
     * <p>
     * <b>Definition:</b>
     * The total nober of concepts in the expansion. If the number of concept nodes in this resource is less than the stated number, then the server can return more using the offset parameter
     * </p> 
	 */
	public Expansion setTotal( int theInteger) {
		myTotal = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>offset</b> (ValueSet.expansion.offset).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present
     * </p> 
	 */
	public IntegerDt getOffsetElement() {  
		if (myOffset == null) {
			myOffset = new IntegerDt();
		}
		return myOffset;
	}

	
	/**
	 * Gets the value(s) for <b>offset</b> (ValueSet.expansion.offset).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present
     * </p> 
	 */
	public Integer getOffset() {  
		return getOffsetElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>offset</b> (ValueSet.expansion.offset)
	 *
     * <p>
     * <b>Definition:</b>
     * If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present
     * </p> 
	 */
	public Expansion setOffset(IntegerDt theValue) {
		myOffset = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>offset</b> (ValueSet.expansion.offset)
	 *
     * <p>
     * <b>Definition:</b>
     * If paging is being used, the offset at which this resource starts - e.g. this resource is a partial view into the expansion. If paging is not being used, this element SHALL not be present
     * </p> 
	 */
	public Expansion setOffset( int theInteger) {
		myOffset = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>parameter</b> (ValueSet.expansion.parameter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion
     * </p> 
	 */
	public java.util.List<ExpansionParameter> getParameter() {  
		if (myParameter == null) {
			myParameter = new java.util.ArrayList<ExpansionParameter>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> (ValueSet.expansion.parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion
     * </p> 
	 */
	public Expansion setParameter(java.util.List<ExpansionParameter> theValue) {
		myParameter = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>parameter</b> (ValueSet.expansion.parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion
     * </p> 
	 */
	public ExpansionParameter addParameter() {
		ExpansionParameter newType = new ExpansionParameter();
		getParameter().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>parameter</b> (ValueSet.expansion.parameter)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion
	 * </p>
	 * @param theValue The parameter to add (must not be <code>null</code>)
	 */
	public Expansion addParameter(ExpansionParameter theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getParameter().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>parameter</b> (ValueSet.expansion.parameter),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion
     * </p> 
	 */
	public ExpansionParameter getParameterFirstRep() {
		if (getParameter().isEmpty()) {
			return addParameter();
		}
		return getParameter().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>contains</b> (ValueSet.expansion.contains).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The codes that are contained in the value set expansion
     * </p> 
	 */
	public java.util.List<ExpansionContains> getContains() {  
		if (myContains == null) {
			myContains = new java.util.ArrayList<ExpansionContains>();
		}
		return myContains;
	}

	/**
	 * Sets the value(s) for <b>contains</b> (ValueSet.expansion.contains)
	 *
     * <p>
     * <b>Definition:</b>
     * The codes that are contained in the value set expansion
     * </p> 
	 */
	public Expansion setContains(java.util.List<ExpansionContains> theValue) {
		myContains = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contains</b> (ValueSet.expansion.contains)
	 *
     * <p>
     * <b>Definition:</b>
     * The codes that are contained in the value set expansion
     * </p> 
	 */
	public ExpansionContains addContains() {
		ExpansionContains newType = new ExpansionContains();
		getContains().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>contains</b> (ValueSet.expansion.contains)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * The codes that are contained in the value set expansion
	 * </p>
	 * @param theValue The contains to add (must not be <code>null</code>)
	 */
	public Expansion addContains(ExpansionContains theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getContains().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>contains</b> (ValueSet.expansion.contains),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The codes that are contained in the value set expansion
     * </p> 
	 */
	public ExpansionContains getContainsFirstRep() {
		if (getContains().isEmpty()) {
			return addContains();
		}
		return getContains().get(0); 
	}
  


	}

	/**
	 * Block class for child element: <b>ValueSet.expansion.parameter</b> (ValueSet.expansion.parameter)
	 *
     * <p>
     * <b>Definition:</b>
     * A Parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to check whether the expansion is suitable for a particular purpose, or to pick the correct expansion
     * </p> 
	 */
	@Block()	
	public static class ExpansionParameter 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.parameter.name",
		formalDefinition="The name of the parameter"
	)
	private StringDt myName;
	
	@Child(name="value", order=1, min=0, max=1, type={
		StringDt.class, 		BooleanDt.class, 		IntegerDt.class, 		DecimalDt.class, 		UriDt.class, 		CodeDt.class	})
	@Description(
		shortDefinition="ValueSet.expansion.parameter.value[x]",
		formalDefinition="The value of the parameter"
	)
	private IDatatype myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myValue);
	}

	/**
	 * Gets the value(s) for <b>name</b> (ValueSet.expansion.parameter.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the parameter
     * </p> 
	 */
	public StringDt getNameElement() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	
	/**
	 * Gets the value(s) for <b>name</b> (ValueSet.expansion.parameter.name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the parameter
     * </p> 
	 */
	public String getName() {  
		return getNameElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>name</b> (ValueSet.expansion.parameter.name)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the parameter
     * </p> 
	 */
	public ExpansionParameter setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> (ValueSet.expansion.parameter.name)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the parameter
     * </p> 
	 */
	public ExpansionParameter setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>value[x]</b> (ValueSet.expansion.parameter.value[x]).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the parameter
     * </p> 
	 */
	public IDatatype getValue() {  
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value[x]</b> (ValueSet.expansion.parameter.value[x])
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the parameter
     * </p> 
	 */
	public ExpansionParameter setValue(IDatatype theValue) {
		myValue = theValue;
		return this;
	}
	
	

  


	}


	/**
	 * Block class for child element: <b>ValueSet.expansion.contains</b> (ValueSet.expansion.contains)
	 *
     * <p>
     * <b>Definition:</b>
     * The codes that are contained in the value set expansion
     * </p> 
	 */
	@Block()	
	public static class ExpansionContains 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.contains.system",
		formalDefinition="An absolute URI which is the code system in which the code for this item in the expansion is defined"
	)
	private UriDt mySystem;
	
	@Child(name="abstract", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.contains.abstract",
		formalDefinition="If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value"
	)
	private BooleanDt myAbstract;
	
	@Child(name="version", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.contains.version",
		formalDefinition="The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence"
	)
	private StringDt myVersion;
	
	@Child(name="code", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.contains.code",
		formalDefinition="The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set"
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="ValueSet.expansion.contains.display",
		formalDefinition="The recommended display for this item in the expansion"
	)
	private StringDt myDisplay;
	
	@Child(name="contains", type=ExpansionContains.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="ValueSet.expansion.contains.contains",
		formalDefinition="Other codes and entries contained under this entry in the heirarchy"
	)
	private java.util.List<ExpansionContains> myContains;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myAbstract,  myVersion,  myCode,  myDisplay,  myContains);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myAbstract, myVersion, myCode, myDisplay, myContains);
	}

	/**
	 * Gets the value(s) for <b>system</b> (ValueSet.expansion.contains.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI which is the code system in which the code for this item in the expansion is defined
     * </p> 
	 */
	public UriDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	
	/**
	 * Gets the value(s) for <b>system</b> (ValueSet.expansion.contains.system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI which is the code system in which the code for this item in the expansion is defined
     * </p> 
	 */
	public String getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> (ValueSet.expansion.contains.system)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI which is the code system in which the code for this item in the expansion is defined
     * </p> 
	 */
	public ExpansionContains setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>system</b> (ValueSet.expansion.contains.system)
	 *
     * <p>
     * <b>Definition:</b>
     * An absolute URI which is the code system in which the code for this item in the expansion is defined
     * </p> 
	 */
	public ExpansionContains setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>abstract</b> (ValueSet.expansion.contains.abstract).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value
     * </p> 
	 */
	public BooleanDt getAbstractElement() {  
		if (myAbstract == null) {
			myAbstract = new BooleanDt();
		}
		return myAbstract;
	}

	
	/**
	 * Gets the value(s) for <b>abstract</b> (ValueSet.expansion.contains.abstract).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value
     * </p> 
	 */
	public Boolean getAbstract() {  
		return getAbstractElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>abstract</b> (ValueSet.expansion.contains.abstract)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value
     * </p> 
	 */
	public ExpansionContains setAbstract(BooleanDt theValue) {
		myAbstract = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>abstract</b> (ValueSet.expansion.contains.abstract)
	 *
     * <p>
     * <b>Definition:</b>
     * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value
     * </p> 
	 */
	public ExpansionContains setAbstract( boolean theBoolean) {
		myAbstract = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (ValueSet.expansion.contains.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence
     * </p> 
	 */
	public StringDt getVersionElement() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	
	/**
	 * Gets the value(s) for <b>version</b> (ValueSet.expansion.contains.version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence
     * </p> 
	 */
	public String getVersion() {  
		return getVersionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>version</b> (ValueSet.expansion.contains.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence
     * </p> 
	 */
	public ExpansionContains setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> (ValueSet.expansion.contains.version)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence
     * </p> 
	 */
	public ExpansionContains setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (ValueSet.expansion.contains.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set
     * </p> 
	 */
	public CodeDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (ValueSet.expansion.contains.code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (ValueSet.expansion.contains.code)
	 *
     * <p>
     * <b>Definition:</b>
     * The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set
     * </p> 
	 */
	public ExpansionContains setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> (ValueSet.expansion.contains.code)
	 *
     * <p>
     * <b>Definition:</b>
     * The code for this item in the expansion heirarchy. If this code is missing the entry in the heirarchy is a place holder (abstract) and doesn't represent a valid code in the value set
     * </p> 
	 */
	public ExpansionContains setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (ValueSet.expansion.contains.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended display for this item in the expansion
     * </p> 
	 */
	public StringDt getDisplayElement() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	
	/**
	 * Gets the value(s) for <b>display</b> (ValueSet.expansion.contains.display).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended display for this item in the expansion
     * </p> 
	 */
	public String getDisplay() {  
		return getDisplayElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>display</b> (ValueSet.expansion.contains.display)
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended display for this item in the expansion
     * </p> 
	 */
	public ExpansionContains setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> (ValueSet.expansion.contains.display)
	 *
     * <p>
     * <b>Definition:</b>
     * The recommended display for this item in the expansion
     * </p> 
	 */
	public ExpansionContains setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contains</b> (ValueSet.expansion.contains.contains).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Other codes and entries contained under this entry in the heirarchy
     * </p> 
	 */
	public java.util.List<ExpansionContains> getContains() {  
		if (myContains == null) {
			myContains = new java.util.ArrayList<ExpansionContains>();
		}
		return myContains;
	}

	/**
	 * Sets the value(s) for <b>contains</b> (ValueSet.expansion.contains.contains)
	 *
     * <p>
     * <b>Definition:</b>
     * Other codes and entries contained under this entry in the heirarchy
     * </p> 
	 */
	public ExpansionContains setContains(java.util.List<ExpansionContains> theValue) {
		myContains = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contains</b> (ValueSet.expansion.contains.contains)
	 *
     * <p>
     * <b>Definition:</b>
     * Other codes and entries contained under this entry in the heirarchy
     * </p> 
	 */
	public ExpansionContains addContains() {
		ExpansionContains newType = new ExpansionContains();
		getContains().add(newType);
		return newType; 
	}

	/**
	 * Adds a given new value for <b>contains</b> (ValueSet.expansion.contains.contains)
	 *
	 * <p>
	 * <b>Definition:</b>
	 * Other codes and entries contained under this entry in the heirarchy
	 * </p>
	 * @param theValue The contains to add (must not be <code>null</code>)
	 */
	public ExpansionContains addContains(ExpansionContains theValue) {
		if (theValue == null) {
			throw new NullPointerException("theValue must not be null");
		}
		getContains().add(theValue);
		return this;
	}

	/**
	 * Gets the first repetition for <b>contains</b> (ValueSet.expansion.contains.contains),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Other codes and entries contained under this entry in the heirarchy
     * </p> 
	 */
	public ExpansionContains getContainsFirstRep() {
		if (getContains().isEmpty()) {
			return addContains();
		}
		return getContains().get(0); 
	}
  


	}





    @Override
    public String getResourceName() {
        return "ValueSet";
    }
    
    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DSTU2;
    }


}
