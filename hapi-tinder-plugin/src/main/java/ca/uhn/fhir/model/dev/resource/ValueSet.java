















package ca.uhn.fhir.model.dev.resource;

import java.net.URI;
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
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.composite.ContactPointDt;
import ca.uhn.fhir.model.dev.composite.IdentifierDt;
import ca.uhn.fhir.model.dev.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dev.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>ValueSet</b> Resource
 * ()
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
public class ValueSet 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="ValueSet.identifier", description="The identifier of the value set", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.version</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="version", path="ValueSet.version", description="The version identifier of the value set", type="token"  )
	public static final String SP_VERSION = "version";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.version</b><br/>
	 * </p>
	 */
	public static final TokenClientParam VERSION = new TokenClientParam(SP_VERSION);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the value set</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ValueSet.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="ValueSet.name", description="The name of the value set", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>The name of the value set</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ValueSet.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the value set</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ValueSet.publisher</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="publisher", path="ValueSet.publisher", description="Name of the publisher of the value set", type="string"  )
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the value set</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ValueSet.publisher</b><br/>
	 * </p>
	 */
	public static final StringClientParam PUBLISHER = new StringClientParam(SP_PUBLISHER);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the value set</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ValueSet.description</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="ValueSet.description", description="Text search in the description of the value set", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the value set</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>ValueSet.description</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="ValueSet.status", description="The status of the value set", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The status of the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The value set publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ValueSet.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="ValueSet.date", description="The value set publication date", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The value set publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>ValueSet.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>system</b>
	 * <p>
	 * Description: <b>The system for any codes defined by this value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.define.system</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="system", path="ValueSet.define.system", description="The system for any codes defined by this value set", type="token"  )
	public static final String SP_SYSTEM = "system";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>system</b>
	 * <p>
	 * Description: <b>The system for any codes defined by this value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.define.system</b><br/>
	 * </p>
	 */
	public static final TokenClientParam SYSTEM = new TokenClientParam(SP_SYSTEM);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code defined in the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.define.concept.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="ValueSet.define.concept.code", description="A code defined in the value set", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code defined in the value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.define.concept.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>reference</b>
	 * <p>
	 * Description: <b>A code system included or excluded in the value set or an imported value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.compose.include.system</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="reference", path="ValueSet.compose.include.system", description="A code system included or excluded in the value set or an imported value set", type="token"  )
	public static final String SP_REFERENCE = "reference";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>reference</b>
	 * <p>
	 * Description: <b>A code system included or excluded in the value set or an imported value set</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>ValueSet.compose.include.system</b><br/>
	 * </p>
	 */
	public static final TokenClientParam REFERENCE = new TokenClientParam(SP_REFERENCE);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.compose.include.system</b>".
	 */
	public static final Include INCLUDE_COMPOSE_INCLUDE_SYSTEM = new Include("ValueSet.compose.include.system");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("ValueSet.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.define.concept.code</b>".
	 */
	public static final Include INCLUDE_DEFINE_CONCEPT_CODE = new Include("ValueSet.define.concept.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.define.system</b>".
	 */
	public static final Include INCLUDE_DEFINE_SYSTEM = new Include("ValueSet.define.system");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.description</b>".
	 */
	public static final Include INCLUDE_DESCRIPTION = new Include("ValueSet.description");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("ValueSet.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("ValueSet.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.publisher</b>".
	 */
	public static final Include INCLUDE_PUBLISHER = new Include("ValueSet.publisher");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("ValueSet.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>ValueSet.version</b>".
	 */
	public static final Include INCLUDE_VERSION = new Include("ValueSet.version");


	@Child(name="identifier", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)"
	)
	private UriDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language name describing the value set"
	)
	private StringDt myName;
	
	@Child(name="purpose", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This should describe \"the semantic space\" to be included in the value set. This can also describe the approach taken to build the value set."
	)
	private StringDt myPurpose;
	
	@Child(name="immutable", type=BooleanDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If this is set to 'true', then no new versions of the content logical definition can be created.  Note: Other metadata might still change"
	)
	private BooleanDt myImmutable;
	
	@Child(name="publisher", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The name of the individual or organization that published the value set"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactPointDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Contacts of the publisher to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set"
	)
	private StringDt myDescription;
	
	@Child(name="copyright", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set"
	)
	private StringDt myCopyright;
	
	@Child(name="status", type=CodeDt.class, order=9, min=1, max=1)	
	@Description(
		shortDefinition="ValueSetStatus",
		formalDefinition="The status of the value set"
	)
	private BoundCodeDt<ValueSetStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="extensible", type=BooleanDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Whether this is intended to be used with an extensible binding or not"
	)
	private BooleanDt myExtensible;
	
	@Child(name="date", type=DateTimeDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date that the value set status was last changed"
	)
	private DateTimeDt myDate;
	
	@Child(name="stableDate", type=DateDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date."
	)
	private DateDt myStableDate;
	
	@Child(name="define", order=14, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private Define myDefine;
	
	@Child(name="compose", order=15, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private Compose myCompose;
	
	@Child(name="expansion", order=16, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A value set can also be \"expanded\", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed"
	)
	private Expansion myExpansion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myName,  myPurpose,  myImmutable,  myPublisher,  myTelecom,  myDescription,  myCopyright,  myStatus,  myExperimental,  myExtensible,  myDate,  myStableDate,  myDefine,  myCompose,  myExpansion);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myVersion, myName, myPurpose, myImmutable, myPublisher, myTelecom, myDescription, myCopyright, myStatus, myExperimental, myExtensible, myDate, myStableDate, myDefine, myCompose, myExpansion);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public UriDt getIdentifierElement() {  
		if (myIdentifier == null) {
			myIdentifier = new UriDt();
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
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public URI getIdentifier() {  
		return getIdentifierElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public ValueSet setIdentifier(UriDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public ValueSet setIdentifier( String theUri) {
		myIdentifier = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
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
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
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
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public ValueSet setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public ValueSet setVersion( String theString) {
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
	 * Gets the value(s) for <b>name</b> ().
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
	 * Sets the value(s) for <b>name</b> ()
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
	 * Sets the value for <b>name</b> ()
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
	 * Gets the value(s) for <b>purpose</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This should describe \"the semantic space\" to be included in the value set. This can also describe the approach taken to build the value set.
     * </p> 
	 */
	public StringDt getPurposeElement() {  
		if (myPurpose == null) {
			myPurpose = new StringDt();
		}
		return myPurpose;
	}

	
	/**
	 * Gets the value(s) for <b>purpose</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This should describe \"the semantic space\" to be included in the value set. This can also describe the approach taken to build the value set.
     * </p> 
	 */
	public String getPurpose() {  
		return getPurposeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>purpose</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This should describe \"the semantic space\" to be included in the value set. This can also describe the approach taken to build the value set.
     * </p> 
	 */
	public ValueSet setPurpose(StringDt theValue) {
		myPurpose = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>purpose</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * This should describe \"the semantic space\" to be included in the value set. This can also describe the approach taken to build the value set.
     * </p> 
	 */
	public ValueSet setPurpose( String theString) {
		myPurpose = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>immutable</b> ().
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
	 * Gets the value(s) for <b>immutable</b> ().
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
	 * Sets the value(s) for <b>immutable</b> ()
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
	 * Sets the value for <b>immutable</b> ()
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
	 * Gets the value(s) for <b>publisher</b> ().
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
	 * Gets the value(s) for <b>publisher</b> ().
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
	 * Sets the value(s) for <b>publisher</b> ()
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
	 * Sets the value for <b>publisher</b> ()
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
	 * Gets the value(s) for <b>telecom</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
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
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ValueSet setTelecom(java.util.List<ContactPointDt> theValue) {
		myTelecom = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>telecom</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
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
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
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
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set
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
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set
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
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set
     * </p> 
	 */
	public ValueSet setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc. The description may include a list of expected usages for the value set
     * </p> 
	 */
	public ValueSet setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>copyright</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set
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
     * A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set
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
     * A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set
     * </p> 
	 */
	public ValueSet setCopyright(StringDt theValue) {
		myCopyright = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>copyright</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents. These are generally legal restrictions on the use and publishing of the value set
     * </p> 
	 */
	public ValueSet setCopyright( String theString) {
		myCopyright = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (ValueSetStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public BoundCodeDt<ValueSetStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ValueSetStatusEnum>(ValueSetStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (ValueSetStatus).
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
	 * Sets the value(s) for <b>status</b> (ValueSetStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public ValueSet setStatus(BoundCodeDt<ValueSetStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (ValueSetStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public ValueSet setStatus(ValueSetStatusEnum theValue) {
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
	 * Gets the value(s) for <b>experimental</b> ().
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
	 * Sets the value(s) for <b>experimental</b> ()
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
	 * Sets the value for <b>experimental</b> ()
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
	 * Gets the value(s) for <b>extensible</b> ().
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
	 * Gets the value(s) for <b>extensible</b> ().
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
	 * Sets the value(s) for <b>extensible</b> ()
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
	 * Sets the value for <b>extensible</b> ()
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
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
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
     * The date that the value set status was last changed
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
     * The date that the value set status was last changed
     * </p> 
	 */
	public ValueSet setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public ValueSet setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public ValueSet setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>stableDate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.
     * </p> 
	 */
	public DateDt getStableDateElement() {  
		if (myStableDate == null) {
			myStableDate = new DateDt();
		}
		return myStableDate;
	}

	
	/**
	 * Gets the value(s) for <b>stableDate</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.
     * </p> 
	 */
	public Date getStableDate() {  
		return getStableDateElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>stableDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.
     * </p> 
	 */
	public ValueSet setStableDate(DateDt theValue) {
		myStableDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>stableDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.
     * </p> 
	 */
	public ValueSet setStableDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myStableDate = new DateDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>stableDate</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If a Stability Date is expanded by evaluating the Content Logical Definition using the current version of all referenced code system(s) and value sets as of the Stability Date.
     * </p> 
	 */
	public ValueSet setStableDateWithDayPrecision( Date theDate) {
		myStableDate = new DateDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>define</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Define getDefine() {  
		if (myDefine == null) {
			myDefine = new Define();
		}
		return myDefine;
	}

	/**
	 * Sets the value(s) for <b>define</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ValueSet setDefine(Define theValue) {
		myDefine = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>compose</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Compose getCompose() {  
		if (myCompose == null) {
			myCompose = new Compose();
		}
		return myCompose;
	}

	/**
	 * Sets the value(s) for <b>compose</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ValueSet setCompose(Compose theValue) {
		myCompose = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>expansion</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A value set can also be \"expanded\", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed
     * </p> 
	 */
	public Expansion getExpansion() {  
		if (myExpansion == null) {
			myExpansion = new Expansion();
		}
		return myExpansion;
	}

	/**
	 * Sets the value(s) for <b>expansion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A value set can also be \"expanded\", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed
     * </p> 
	 */
	public ValueSet setExpansion(Expansion theValue) {
		myExpansion = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>ValueSet.define</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Define 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked"
	)
	private StringDt myVersion;
	
	@Child(name="caseSensitive", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If code comparison is case sensitive when codes within this system are compared to each other"
	)
	private BooleanDt myCaseSensitive;
	
	@Child(name="concept", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<DefineConcept> myConcept;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myVersion,  myCaseSensitive,  myConcept);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myVersion, myCaseSensitive, myConcept);
	}

	/**
	 * Gets the value(s) for <b>system</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new UriDt();
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
     * 
     * </p> 
	 */
	public URI getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Define setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Define setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
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
	 * Gets the value(s) for <b>version</b> ().
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
	 * Sets the value(s) for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public Define setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public Define setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>caseSensitive</b> ().
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
	 * Gets the value(s) for <b>caseSensitive</b> ().
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
	 * Sets the value(s) for <b>caseSensitive</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public Define setCaseSensitive(BooleanDt theValue) {
		myCaseSensitive = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>caseSensitive</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public Define setCaseSensitive( boolean theBoolean) {
		myCaseSensitive = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>concept</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<DefineConcept> getConcept() {  
		if (myConcept == null) {
			myConcept = new java.util.ArrayList<DefineConcept>();
		}
		return myConcept;
	}

	/**
	 * Sets the value(s) for <b>concept</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Define setConcept(java.util.List<DefineConcept> theValue) {
		myConcept = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>concept</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept addConcept() {
		DefineConcept newType = new DefineConcept();
		getConcept().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>concept</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept getConceptFirstRep() {
		if (getConcept().isEmpty()) {
			return addConcept();
		}
		return getConcept().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>ValueSet.define.concept</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class DefineConcept 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="!",
		formalDefinition=""
	)
	private CodeDt myCode;
	
	@Child(name="abstract", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If this code is not for use as a real concept"
	)
	private BooleanDt myAbstract;
	
	@Child(name="display", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myDisplay;
	
	@Child(name="definition", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept"
	)
	private StringDt myDefinition;
	
	@Child(name="designation", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc"
	)
	private java.util.List<DefineConceptDesignation> myDesignation;
	
	@Child(name="concept", type=DefineConcept.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private java.util.List<DefineConcept> myConcept;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myAbstract,  myDisplay,  myDefinition,  myDesignation,  myConcept);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myAbstract, myDisplay, myDefinition, myDesignation, myConcept);
	}

	/**
	 * Gets the value(s) for <b>code</b> (!).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (!).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (!)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> (!)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>abstract</b> ().
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
	 * Gets the value(s) for <b>abstract</b> ().
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
	 * Sets the value(s) for <b>abstract</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public DefineConcept setAbstract(BooleanDt theValue) {
		myAbstract = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>abstract</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public DefineConcept setAbstract( boolean theBoolean) {
		myAbstract = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
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
     * 
     * </p> 
	 */
	public DefineConcept setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public StringDt getDefinitionElement() {  
		if (myDefinition == null) {
			myDefinition = new StringDt();
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
     * The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public String getDefinition() {  
		return getDefinitionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>definition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public DefineConcept setDefinition(StringDt theValue) {
		myDefinition = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>definition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public DefineConcept setDefinition( String theString) {
		myDefinition = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>designation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc
     * </p> 
	 */
	public java.util.List<DefineConceptDesignation> getDesignation() {  
		if (myDesignation == null) {
			myDesignation = new java.util.ArrayList<DefineConceptDesignation>();
		}
		return myDesignation;
	}

	/**
	 * Sets the value(s) for <b>designation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc
     * </p> 
	 */
	public DefineConcept setDesignation(java.util.List<DefineConceptDesignation> theValue) {
		myDesignation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>designation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc
     * </p> 
	 */
	public DefineConceptDesignation addDesignation() {
		DefineConceptDesignation newType = new DefineConceptDesignation();
		getDesignation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>designation</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc
     * </p> 
	 */
	public DefineConceptDesignation getDesignationFirstRep() {
		if (getDesignation().isEmpty()) {
			return addDesignation();
		}
		return getDesignation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>concept</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<DefineConcept> getConcept() {  
		if (myConcept == null) {
			myConcept = new java.util.ArrayList<DefineConcept>();
		}
		return myConcept;
	}

	/**
	 * Sets the value(s) for <b>concept</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept setConcept(java.util.List<DefineConcept> theValue) {
		myConcept = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>concept</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept addConcept() {
		DefineConcept newType = new DefineConcept();
		getConcept().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>concept</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DefineConcept getConceptFirstRep() {
		if (getConcept().isEmpty()) {
			return addConcept();
		}
		return getConcept().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>ValueSet.define.concept.designation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for the concept - other languages, aliases, specialised purposes, used for particular purposes, etc
     * </p> 
	 */
	@Block()	
	public static class DefineConceptDesignation 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="language", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Language",
		formalDefinition="The language this designation is defined for"
	)
	private CodeDt myLanguage;
	
	@Child(name="use", type=CodingDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="ConceptDesignationUse",
		formalDefinition="A code that details how this designation would be used"
	)
	private CodingDt myUse;
	
	@Child(name="value", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>language</b> (Language).
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
	 * Gets the value(s) for <b>language</b> (Language).
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
	 * Sets the value(s) for <b>language</b> (Language)
	 *
     * <p>
     * <b>Definition:</b>
     * The language this designation is defined for
     * </p> 
	 */
	public DefineConceptDesignation setLanguage(CodeDt theValue) {
		myLanguage = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>language</b> (Language)
	 *
     * <p>
     * <b>Definition:</b>
     * The language this designation is defined for
     * </p> 
	 */
	public DefineConceptDesignation setLanguage( String theCode) {
		myLanguage = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>use</b> (ConceptDesignationUse).
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
	 * Sets the value(s) for <b>use</b> (ConceptDesignationUse)
	 *
     * <p>
     * <b>Definition:</b>
     * A code that details how this designation would be used
     * </p> 
	 */
	public DefineConceptDesignation setUse(CodingDt theValue) {
		myUse = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>value</b> ().
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
	 * Gets the value(s) for <b>value</b> ().
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
	 * Sets the value(s) for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The text value for this designation
     * </p> 
	 */
	public DefineConceptDesignation setValue(StringDt theValue) {
		myValue = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>value</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The text value for this designation
     * </p> 
	 */
	public DefineConceptDesignation setValue( String theString) {
		myValue = new StringDt(theString); 
		return this; 
	}

 

	}




	/**
	 * Block class for child element: <b>ValueSet.compose</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Compose 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="import", type=UriDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Includes the contents of the referenced value set as a part of the contents of this value set"
	)
	private java.util.List<UriDt> myImport;
	
	@Child(name="include", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Include one or more codes from a code system"
	)
	private java.util.List<ComposeInclude> myInclude;
	
	@Child(name="exclude", type=ComposeInclude.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>import</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set
     * </p> 
	 */
	public java.util.List<UriDt> getImport() {  
		if (myImport == null) {
			myImport = new java.util.ArrayList<UriDt>();
		}
		return myImport;
	}

	/**
	 * Sets the value(s) for <b>import</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set
     * </p> 
	 */
	public Compose setImport(java.util.List<UriDt> theValue) {
		myImport = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>import</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set
     * </p> 
	 */
	public UriDt addImport() {
		UriDt newType = new UriDt();
		getImport().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>import</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set
     * </p> 
	 */
	public UriDt getImportFirstRep() {
		if (getImport().isEmpty()) {
			return addImport();
		}
		return getImport().get(0); 
	}
 	/**
	 * Adds a new value for <b>import</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Includes the contents of the referenced value set as a part of the contents of this value set
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
	 * Gets the value(s) for <b>include</b> ().
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
	 * Sets the value(s) for <b>include</b> ()
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
	 * Adds and returns a new value for <b>include</b> ()
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
	 * Gets the first repetition for <b>include</b> (),
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
	 * Gets the value(s) for <b>exclude</b> ().
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
	 * Sets the value(s) for <b>exclude</b> ()
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
	 * Adds and returns a new value for <b>exclude</b> ()
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
	 * Gets the first repetition for <b>exclude</b> (),
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
	 * Block class for child element: <b>ValueSet.compose.include</b> ()
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
		shortDefinition="",
		formalDefinition="The code system from which the selected codes come from"
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The version of the code system that the codes are selected from"
	)
	private StringDt myVersion;
	
	@Child(name="concept", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Specifies a concept to be included or excluded."
	)
	private java.util.List<ComposeIncludeConcept> myConcept;
	
	@Child(name="filter", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
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
	 * Gets the value(s) for <b>system</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code system from which the selected codes come from
     * </p> 
	 */
	public UriDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new UriDt();
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
     * The code system from which the selected codes come from
     * </p> 
	 */
	public URI getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The code system from which the selected codes come from
     * </p> 
	 */
	public ComposeInclude setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The code system from which the selected codes come from
     * </p> 
	 */
	public ComposeInclude setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> ().
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
	 * Gets the value(s) for <b>version</b> ().
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
	 * Sets the value(s) for <b>version</b> ()
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
	 * Sets the value for <b>version</b> ()
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
	 * Gets the value(s) for <b>concept</b> ().
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
	 * Sets the value(s) for <b>concept</b> ()
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
	 * Adds and returns a new value for <b>concept</b> ()
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
	 * Gets the first repetition for <b>concept</b> (),
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
	 * Gets the value(s) for <b>filter</b> ().
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
	 * Sets the value(s) for <b>filter</b> ()
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
	 * Adds and returns a new value for <b>filter</b> ()
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
	 * Gets the first repetition for <b>filter</b> (),
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
	 * Block class for child element: <b>ValueSet.compose.include.concept</b> ()
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
		shortDefinition="!",
		formalDefinition="Specifies a code for the concept to be included or excluded"
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The text to display to the user for this concept in the context of this valueset. If no display is provided, then applications using the value set use the display specified for the code by the system"
	)
	private StringDt myDisplay;
	
	@Child(name="designation", type=DefineConceptDesignation.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc"
	)
	private java.util.List<DefineConceptDesignation> myDesignation;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myDisplay,  myDesignation);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myDisplay, myDesignation);
	}

	/**
	 * Gets the value(s) for <b>code</b> (!).
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
	 * Gets the value(s) for <b>code</b> (!).
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
	 * Sets the value(s) for <b>code</b> (!)
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
	 * Sets the value for <b>code</b> (!)
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
	 * Gets the value(s) for <b>display</b> ().
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
	 * Gets the value(s) for <b>display</b> ().
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
	 * Sets the value(s) for <b>display</b> ()
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
	 * Sets the value for <b>display</b> ()
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
	 * Gets the value(s) for <b>designation</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc
     * </p> 
	 */
	public java.util.List<DefineConceptDesignation> getDesignation() {  
		if (myDesignation == null) {
			myDesignation = new java.util.ArrayList<DefineConceptDesignation>();
		}
		return myDesignation;
	}

	/**
	 * Sets the value(s) for <b>designation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc
     * </p> 
	 */
	public ComposeIncludeConcept setDesignation(java.util.List<DefineConceptDesignation> theValue) {
		myDesignation = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>designation</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc
     * </p> 
	 */
	public DefineConceptDesignation addDesignation() {
		DefineConceptDesignation newType = new DefineConceptDesignation();
		getDesignation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>designation</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional representations for this concept when used in this value set - other languages, aliases, specialised purposes, used for particular purposes, etc
     * </p> 
	 */
	public DefineConceptDesignation getDesignationFirstRep() {
		if (getDesignation().isEmpty()) {
			return addDesignation();
		}
		return getDesignation().get(0); 
	}
  

	}


	/**
	 * Block class for child element: <b>ValueSet.compose.include.filter</b> ()
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
		shortDefinition="!",
		formalDefinition="A code that identifies a property defined in the code system"
	)
	private CodeDt myProperty;
	
	@Child(name="op", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="FilterOperator",
		formalDefinition="The kind of operation to perform as a part of the filter criteria"
	)
	private BoundCodeDt<FilterOperatorEnum> myOp;
	
	@Child(name="value", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="!",
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
	 * Gets the value(s) for <b>property</b> (!).
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
	 * Gets the value(s) for <b>property</b> (!).
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
	 * Sets the value(s) for <b>property</b> (!)
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
	 * Sets the value for <b>property</b> (!)
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
	 * Gets the value(s) for <b>op</b> (FilterOperator).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public BoundCodeDt<FilterOperatorEnum> getOpElement() {  
		if (myOp == null) {
			myOp = new BoundCodeDt<FilterOperatorEnum>(FilterOperatorEnum.VALUESET_BINDER);
		}
		return myOp;
	}

	
	/**
	 * Gets the value(s) for <b>op</b> (FilterOperator).
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
	 * Sets the value(s) for <b>op</b> (FilterOperator)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public ComposeIncludeFilter setOp(BoundCodeDt<FilterOperatorEnum> theValue) {
		myOp = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>op</b> (FilterOperator)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public ComposeIncludeFilter setOp(FilterOperatorEnum theValue) {
		getOpElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>value</b> (!).
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
	 * Gets the value(s) for <b>value</b> (!).
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
	 * Sets the value(s) for <b>value</b> (!)
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
	 * Sets the value for <b>value</b> (!)
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
	 * Block class for child element: <b>ValueSet.expansion</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A value set can also be \"expanded\", where the value set is turned into a simple collection of enumerated codes. This element holds the expansion, if it has been performed
     * </p> 
	 */
	@Block()	
	public static class Expansion 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="timestamp", type=DateTimeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The time at which the expansion was produced by the expanding system."
	)
	private DateTimeDt myTimestamp;
	
	@Child(name="contains", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="The codes that are contained in the value set expansion"
	)
	private java.util.List<ExpansionContains> myContains;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myTimestamp,  myContains);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myTimestamp, myContains);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public Expansion setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>timestamp</b> ().
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
	 * Gets the value(s) for <b>timestamp</b> ().
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
	 * Sets the value(s) for <b>timestamp</b> ()
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
	 * Sets the value for <b>timestamp</b> ()
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
	 * Sets the value for <b>timestamp</b> ()
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
	 * Gets the value(s) for <b>contains</b> ().
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
	 * Sets the value(s) for <b>contains</b> ()
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
	 * Adds and returns a new value for <b>contains</b> ()
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
	 * Gets the first repetition for <b>contains</b> (),
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
	 * Block class for child element: <b>ValueSet.expansion.contains</b> ()
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
		shortDefinition="",
		formalDefinition="The system in which the code for this item in the expansion is defined"
	)
	private UriDt mySystem;
	
	@Child(name="abstract", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code directly as a proper value"
	)
	private BooleanDt myAbstract;
	
	@Child(name="version", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The version of this code system that defined this code and/or display. This should only be used with code systems that do not enforce concept permanence"
	)
	private StringDt myVersion;
	
	@Child(name="code", type=CodeDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="!",
		formalDefinition=""
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition=""
	)
	private StringDt myDisplay;
	
	@Child(name="contains", type=ExpansionContains.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition=""
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
	 * Gets the value(s) for <b>system</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The system in which the code for this item in the expansion is defined
     * </p> 
	 */
	public UriDt getSystemElement() {  
		if (mySystem == null) {
			mySystem = new UriDt();
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
     * The system in which the code for this item in the expansion is defined
     * </p> 
	 */
	public URI getSystem() {  
		return getSystemElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The system in which the code for this item in the expansion is defined
     * </p> 
	 */
	public ExpansionContains setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>system</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The system in which the code for this item in the expansion is defined
     * </p> 
	 */
	public ExpansionContains setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>abstract</b> ().
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
	 * Gets the value(s) for <b>abstract</b> ().
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
	 * Sets the value(s) for <b>abstract</b> ()
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
	 * Sets the value for <b>abstract</b> ()
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
	 * Gets the value(s) for <b>version</b> ().
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
	 * Gets the value(s) for <b>version</b> ().
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
	 * Sets the value(s) for <b>version</b> ()
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
	 * Sets the value for <b>version</b> ()
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
	 * Gets the value(s) for <b>code</b> (!).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getCodeElement() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	
	/**
	 * Gets the value(s) for <b>code</b> (!).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public String getCode() {  
		return getCodeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>code</b> (!)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExpansionContains setCode(CodeDt theValue) {
		myCode = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>code</b> (!)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExpansionContains setCode( String theCode) {
		myCode = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
     * 
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
     * 
     * </p> 
	 */
	public ExpansionContains setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>display</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExpansionContains setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contains</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ExpansionContains> getContains() {  
		if (myContains == null) {
			myContains = new java.util.ArrayList<ExpansionContains>();
		}
		return myContains;
	}

	/**
	 * Sets the value(s) for <b>contains</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExpansionContains setContains(java.util.List<ExpansionContains> theValue) {
		myContains = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>contains</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExpansionContains addContains() {
		ExpansionContains newType = new ExpansionContains();
		getContains().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>contains</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
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
    	return ca.uhn.fhir.context.FhirVersionEnum.DEV;
    }

}
