















package ca.uhn.fhir.model.dev.resource;

import java.net.URI;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IDatatype;
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
import ca.uhn.fhir.model.dev.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.composite.ContactPointDt;
import ca.uhn.fhir.model.dev.composite.IdentifierDt;
import ca.uhn.fhir.model.dev.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dev.valueset.DataElementGranularityEnum;
import ca.uhn.fhir.model.dev.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dev.valueset.ResourceDataElementStatusEnum;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>DataElement</b> Resource
 * ()
 *
 * <p>
 * <b>Definition:</b>
 * The formal description of a single piece of information that can be gathered and reported.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DataElement">http://hl7.org/fhir/profiles/DataElement</a> 
 * </p>
 *
 */
@ResourceDef(name="DataElement", profile="http://hl7.org/fhir/profiles/DataElement")
public class DataElement 
    extends  BaseResource     implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the data element</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DataElement.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DataElement.identifier", description="The identifier of the data element", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>The identifier of the data element</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DataElement.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the data element</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DataElement.version</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="version", path="DataElement.version", description="The version identifier of the data element", type="string"  )
	public static final String SP_VERSION = "version";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>version</b>
	 * <p>
	 * Description: <b>The version identifier of the data element</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DataElement.version</b><br/>
	 * </p>
	 */
	public static final StringClientParam VERSION = new StringClientParam(SP_VERSION);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the data element</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DataElement.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="DataElement.name", description="Name of the data element", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>Name of the data element</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DataElement.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the data element</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DataElement.publisher</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="publisher", path="DataElement.publisher", description="Name of the publisher of the data element", type="string"  )
	public static final String SP_PUBLISHER = "publisher";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
	 * <p>
	 * Description: <b>Name of the publisher of the data element</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DataElement.publisher</b><br/>
	 * </p>
	 */
	public static final StringClientParam PUBLISHER = new StringClientParam(SP_PUBLISHER);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the data element</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DataElement.definition</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="DataElement.definition", description="Text search in the description of the data element", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b>Text search in the description of the data element</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DataElement.definition</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the data element</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DataElement.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DataElement.status", description="The current status of the data element", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>The current status of the data element</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DataElement.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The data element publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DataElement.date</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="date", path="DataElement.date", description="The data element publication date", type="date"  )
	public static final String SP_DATE = "date";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>date</b>
	 * <p>
	 * Description: <b>The data element publication date</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DataElement.date</b><br/>
	 * </p>
	 */
	public static final DateClientParam DATE = new DateClientParam(SP_DATE);

	/**
	 * Search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code for the data element (server may choose to do subsumption)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DataElement.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="code", path="DataElement.code", description="A code for the data element (server may choose to do subsumption)", type="token"  )
	public static final String SP_CODE = "code";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>code</b>
	 * <p>
	 * Description: <b>A code for the data element (server may choose to do subsumption)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DataElement.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CODE = new TokenClientParam(SP_CODE);

	/**
	 * Search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b>A category assigned to the data element (server may choose to do subsumption)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DataElement.category</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="category", path="DataElement.category", description="A category assigned to the data element (server may choose to do subsumption)", type="token"  )
	public static final String SP_CATEGORY = "category";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>category</b>
	 * <p>
	 * Description: <b>A category assigned to the data element (server may choose to do subsumption)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DataElement.category</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CATEGORY = new TokenClientParam(SP_CATEGORY);


	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DataElement.category</b>".
	 */
	public static final Include INCLUDE_CATEGORY = new Include("DataElement.category");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DataElement.code</b>".
	 */
	public static final Include INCLUDE_CODE = new Include("DataElement.code");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DataElement.date</b>".
	 */
	public static final Include INCLUDE_DATE = new Include("DataElement.date");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DataElement.definition</b>".
	 */
	public static final Include INCLUDE_DEFINITION = new Include("DataElement.definition");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DataElement.identifier</b>".
	 */
	public static final Include INCLUDE_IDENTIFIER = new Include("DataElement.identifier");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DataElement.name</b>".
	 */
	public static final Include INCLUDE_NAME = new Include("DataElement.name");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DataElement.publisher</b>".
	 */
	public static final Include INCLUDE_PUBLISHER = new Include("DataElement.publisher");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DataElement.status</b>".
	 */
	public static final Include INCLUDE_STATUS = new Include("DataElement.status");

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DataElement.version</b>".
	 */
	public static final Include INCLUDE_VERSION = new Include("DataElement.version");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier that is used to identify this data element when it is referenced in a Profile, Questionnaire or an instance"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually."
	)
	private StringDt myVersion;
	
	@Child(name="publisher", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Details of the individual or organization who accepts responsibility for publishing the data element"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactPointDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Contact details to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<ContactPointDt> myTelecom;
	
	@Child(name="status", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="ResourceDataElementStatus",
		formalDefinition="The status of the data element"
	)
	private BoundCodeDt<ResourceDataElementStatusEnum> myStatus;
	
	@Child(name="date", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired)"
	)
	private DateTimeDt myDate;
	
	@Child(name="name", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used."
	)
	private StringDt myName;
	
	@Child(name="category", type=CodeableConceptDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions."
	)
	private java.util.List<CodeableConceptDt> myCategory;
	
	@Child(name="granularity", type=CodeDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="DataElementGranularity",
		formalDefinition="Identifies how precise the data element is in its definition"
	)
	private BoundCodeDt<DataElementGranularityEnum> myGranularity;
	
	@Child(name="code", type=CodingDt.class, order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="A code that provides the meaning for a data element according to a particular terminology"
	)
	private java.util.List<CodingDt> myCode;
	
	@Child(name="question", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey)"
	)
	private StringDt myQuestion;
	
	@Child(name="label", type=StringDt.class, order=11, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form."
	)
	private StringDt myLabel;
	
	@Child(name="definition", type=StringDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Provides a complete explanation of the meaning of the data element for human readability"
	)
	private StringDt myDefinition;
	
	@Child(name="comments", type=StringDt.class, order=13, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc."
	)
	private StringDt myComments;
	
	@Child(name="requirements", type=StringDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Explains why this element is needed and why it's been constrained as it has"
	)
	private StringDt myRequirements;
	
	@Child(name="synonym", type=StringDt.class, order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies additional names by which this element might also be known"
	)
	private java.util.List<StringDt> mySynonym;
	
	@Child(name="type", type=CodeDt.class, order=16, min=0, max=1)	
	@Description(
		shortDefinition="DataType",
		formalDefinition="The FHIR data type that is the type for data that corresponds to this data element"
	)
	private BoundCodeDt<DataTypeEnum> myType;
	
	@Child(name="example", type=IDatatype.class, order=17, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A sample value for this element demonstrating the type of information that would typically be captured."
	)
	private IDatatype myExample;
	
	@Child(name="maxLength", type=IntegerDt.class, order=18, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Indicates the shortest length that SHALL be supported by conformant instances without truncation"
	)
	private IntegerDt myMaxLength;
	
	@Child(name="units", order=19, min=0, max=1, type={
		CodeableConceptDt.class, 		ValueSet.class	})
	@Description(
		shortDefinition="Units",
		formalDefinition="Identifies the units of measure in which the data element should be captured or expressed."
	)
	private IDatatype myUnits;
	
	@Child(name="binding", order=20, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Binds to a value set if this element is coded (code, Coding, CodeableConcept)"
	)
	private Binding myBinding;
	
	@Child(name="mapping", order=21, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="",
		formalDefinition="Identifies a concept from an external specification that roughly corresponds to this element"
	)
	private java.util.List<Mapping> myMapping;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myPublisher,  myTelecom,  myStatus,  myDate,  myName,  myCategory,  myGranularity,  myCode,  myQuestion,  myLabel,  myDefinition,  myComments,  myRequirements,  mySynonym,  myType,  myExample,  myMaxLength,  myUnits,  myBinding,  myMapping);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myVersion, myPublisher, myTelecom, myStatus, myDate, myName, myCategory, myGranularity, myCode, myQuestion, myLabel, myDefinition, myComments, myRequirements, mySynonym, myType, myExample, myMaxLength, myUnits, myBinding, myMapping);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this data element when it is referenced in a Profile, Questionnaire or an instance
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
     * The identifier that is used to identify this data element when it is referenced in a Profile, Questionnaire or an instance
     * </p> 
	 */
	public DataElement setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>version</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
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
     * The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
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
     * The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
     * </p> 
	 */
	public DataElement setVersion(StringDt theValue) {
		myVersion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>version</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the data element when it is referenced in a Profile, Questionnaire or instance. This is an arbitrary value managed by the definition author manually.
     * </p> 
	 */
	public DataElement setVersion( String theString) {
		myVersion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>publisher</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the data element
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
     * Details of the individual or organization who accepts responsibility for publishing the data element
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
     * Details of the individual or organization who accepts responsibility for publishing the data element
     * </p> 
	 */
	public DataElement setPublisher(StringDt theValue) {
		myPublisher = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>publisher</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Details of the individual or organization who accepts responsibility for publishing the data element
     * </p> 
	 */
	public DataElement setPublisher( String theString) {
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
	public DataElement setTelecom(java.util.List<ContactPointDt> theValue) {
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
	 * Gets the value(s) for <b>status</b> (ResourceDataElementStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the data element
     * </p> 
	 */
	public BoundCodeDt<ResourceDataElementStatusEnum> getStatusElement() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ResourceDataElementStatusEnum>(ResourceDataElementStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	
	/**
	 * Gets the value(s) for <b>status</b> (ResourceDataElementStatus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the data element
     * </p> 
	 */
	public String getStatus() {  
		return getStatusElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>status</b> (ResourceDataElementStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the data element
     * </p> 
	 */
	public DataElement setStatus(BoundCodeDt<ResourceDataElementStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>status</b> (ResourceDataElementStatus)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the data element
     * </p> 
	 */
	public DataElement setStatus(ResourceDataElementStatusEnum theValue) {
		getStatusElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>date</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired)
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
     * The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired)
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
     * The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired)
     * </p> 
	 */
	public DataElement setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired)
     * </p> 
	 */
	public DataElement setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the status for this business version of the data element became effective.  (I.e. Date the draft was created, date element became active or date element became retired)
     * </p> 
	 */
	public DataElement setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
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
     * The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
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
     * The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
     * </p> 
	 */
	public DataElement setName(StringDt theValue) {
		myName = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>name</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The term used by humans to refer to the data element.  Should ideally be unique within the context in which the data element is expected to be used.
     * </p> 
	 */
	public DataElement setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>category</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getCategory() {  
		if (myCategory == null) {
			myCategory = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myCategory;
	}

	/**
	 * Sets the value(s) for <b>category</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions.
     * </p> 
	 */
	public DataElement setCategory(java.util.List<CodeableConceptDt> theValue) {
		myCategory = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>category</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions.
     * </p> 
	 */
	public CodeableConceptDt addCategory() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getCategory().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>category</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A set of terms from external terminologies that may be used to assist with indexing and searching of data element definitions.
     * </p> 
	 */
	public CodeableConceptDt getCategoryFirstRep() {
		if (getCategory().isEmpty()) {
			return addCategory();
		}
		return getCategory().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>granularity</b> (DataElementGranularity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies how precise the data element is in its definition
     * </p> 
	 */
	public BoundCodeDt<DataElementGranularityEnum> getGranularityElement() {  
		if (myGranularity == null) {
			myGranularity = new BoundCodeDt<DataElementGranularityEnum>(DataElementGranularityEnum.VALUESET_BINDER);
		}
		return myGranularity;
	}

	
	/**
	 * Gets the value(s) for <b>granularity</b> (DataElementGranularity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies how precise the data element is in its definition
     * </p> 
	 */
	public String getGranularity() {  
		return getGranularityElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>granularity</b> (DataElementGranularity)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies how precise the data element is in its definition
     * </p> 
	 */
	public DataElement setGranularity(BoundCodeDt<DataElementGranularityEnum> theValue) {
		myGranularity = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>granularity</b> (DataElementGranularity)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies how precise the data element is in its definition
     * </p> 
	 */
	public DataElement setGranularity(DataElementGranularityEnum theValue) {
		getGranularityElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>code</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that provides the meaning for a data element according to a particular terminology
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
     * A code that provides the meaning for a data element according to a particular terminology
     * </p> 
	 */
	public DataElement setCode(java.util.List<CodingDt> theValue) {
		myCode = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>code</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A code that provides the meaning for a data element according to a particular terminology
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
     * A code that provides the meaning for a data element according to a particular terminology
     * </p> 
	 */
	public CodingDt getCodeFirstRep() {
		if (getCode().isEmpty()) {
			return addCode();
		}
		return getCode().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>question</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey)
     * </p> 
	 */
	public StringDt getQuestionElement() {  
		if (myQuestion == null) {
			myQuestion = new StringDt();
		}
		return myQuestion;
	}

	
	/**
	 * Gets the value(s) for <b>question</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey)
     * </p> 
	 */
	public String getQuestion() {  
		return getQuestionElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>question</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey)
     * </p> 
	 */
	public DataElement setQuestion(StringDt theValue) {
		myQuestion = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>question</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The default/suggested phrasing to use when prompting a human to capture the data element in question form (e.g. In a survey)
     * </p> 
	 */
	public DataElement setQuestion( String theString) {
		myQuestion = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>label</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     * </p> 
	 */
	public StringDt getLabelElement() {  
		if (myLabel == null) {
			myLabel = new StringDt();
		}
		return myLabel;
	}

	
	/**
	 * Gets the value(s) for <b>label</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     * </p> 
	 */
	public String getLabel() {  
		return getLabelElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>label</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     * </p> 
	 */
	public DataElement setLabel(StringDt theValue) {
		myLabel = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>label</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * The text to display beside the element indicating its meaning or to use to prompt for the element in a user display or form.
     * </p> 
	 */
	public DataElement setLabel( String theString) {
		myLabel = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a complete explanation of the meaning of the data element for human readability
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
     * Provides a complete explanation of the meaning of the data element for human readability
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
     * Provides a complete explanation of the meaning of the data element for human readability
     * </p> 
	 */
	public DataElement setDefinition(StringDt theValue) {
		myDefinition = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>definition</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Provides a complete explanation of the meaning of the data element for human readability
     * </p> 
	 */
	public DataElement setDefinition( String theString) {
		myDefinition = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>comments</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
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
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
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
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public DataElement setComments(StringDt theValue) {
		myComments = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>comments</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Comments about the use of the element, including notes about how to use the data properly, exceptions to proper use, etc.
     * </p> 
	 */
	public DataElement setComments( String theString) {
		myComments = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>requirements</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public StringDt getRequirementsElement() {  
		if (myRequirements == null) {
			myRequirements = new StringDt();
		}
		return myRequirements;
	}

	
	/**
	 * Gets the value(s) for <b>requirements</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public String getRequirements() {  
		return getRequirementsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>requirements</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public DataElement setRequirements(StringDt theValue) {
		myRequirements = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>requirements</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Explains why this element is needed and why it's been constrained as it has
     * </p> 
	 */
	public DataElement setRequirements( String theString) {
		myRequirements = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>synonym</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public java.util.List<StringDt> getSynonym() {  
		if (mySynonym == null) {
			mySynonym = new java.util.ArrayList<StringDt>();
		}
		return mySynonym;
	}

	/**
	 * Sets the value(s) for <b>synonym</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public DataElement setSynonym(java.util.List<StringDt> theValue) {
		mySynonym = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>synonym</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public StringDt addSynonym() {
		StringDt newType = new StringDt();
		getSynonym().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>synonym</b> (),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
	 */
	public StringDt getSynonymFirstRep() {
		if (getSynonym().isEmpty()) {
			return addSynonym();
		}
		return getSynonym().get(0); 
	}
 	/**
	 * Adds a new value for <b>synonym</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies additional names by which this element might also be known
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DataElement addSynonym( String theString) {
		if (mySynonym == null) {
			mySynonym = new java.util.ArrayList<StringDt>();
		}
		mySynonym.add(new StringDt(theString));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>type</b> (DataType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The FHIR data type that is the type for data that corresponds to this data element
     * </p> 
	 */
	public BoundCodeDt<DataTypeEnum> getTypeElement() {  
		if (myType == null) {
			myType = new BoundCodeDt<DataTypeEnum>(DataTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	
	/**
	 * Gets the value(s) for <b>type</b> (DataType).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The FHIR data type that is the type for data that corresponds to this data element
     * </p> 
	 */
	public String getType() {  
		return getTypeElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>type</b> (DataType)
	 *
     * <p>
     * <b>Definition:</b>
     * The FHIR data type that is the type for data that corresponds to this data element
     * </p> 
	 */
	public DataElement setType(BoundCodeDt<DataTypeEnum> theValue) {
		myType = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>type</b> (DataType)
	 *
     * <p>
     * <b>Definition:</b>
     * The FHIR data type that is the type for data that corresponds to this data element
     * </p> 
	 */
	public DataElement setType(DataTypeEnum theValue) {
		getTypeElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>example[x]</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A sample value for this element demonstrating the type of information that would typically be captured.
     * </p> 
	 */
	public IDatatype getExample() {  
		return myExample;
	}

	/**
	 * Sets the value(s) for <b>example[x]</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * A sample value for this element demonstrating the type of information that would typically be captured.
     * </p> 
	 */
	public DataElement setExample(IDatatype theValue) {
		myExample = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>maxLength</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation
     * </p> 
	 */
	public IntegerDt getMaxLengthElement() {  
		if (myMaxLength == null) {
			myMaxLength = new IntegerDt();
		}
		return myMaxLength;
	}

	
	/**
	 * Gets the value(s) for <b>maxLength</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation
     * </p> 
	 */
	public Integer getMaxLength() {  
		return getMaxLengthElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>maxLength</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation
     * </p> 
	 */
	public DataElement setMaxLength(IntegerDt theValue) {
		myMaxLength = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>maxLength</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the shortest length that SHALL be supported by conformant instances without truncation
     * </p> 
	 */
	public DataElement setMaxLength( int theInteger) {
		myMaxLength = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>units[x]</b> (Units).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the units of measure in which the data element should be captured or expressed.
     * </p> 
	 */
	public IDatatype getUnits() {  
		return myUnits;
	}

	/**
	 * Sets the value(s) for <b>units[x]</b> (Units)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the units of measure in which the data element should be captured or expressed.
     * </p> 
	 */
	public DataElement setUnits(IDatatype theValue) {
		myUnits = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>binding</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	public Binding getBinding() {  
		if (myBinding == null) {
			myBinding = new Binding();
		}
		return myBinding;
	}

	/**
	 * Sets the value(s) for <b>binding</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	public DataElement setBinding(Binding theValue) {
		myBinding = theValue;
		return this;
	}
	
	

  
	/**
	 * Gets the value(s) for <b>mapping</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
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
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public DataElement setMapping(java.util.List<Mapping> theValue) {
		myMapping = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>mapping</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
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
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	public Mapping getMappingFirstRep() {
		if (getMapping().isEmpty()) {
			return addMapping();
		}
		return getMapping().get(0); 
	}
  
	/**
	 * Block class for child element: <b>DataElement.binding</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept)
     * </p> 
	 */
	@Block()	
	public static class Binding 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="isExtensible", type=BooleanDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone"
	)
	private BooleanDt myIsExtensible;
	
	@Child(name="conformance", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="BindingConformance",
		formalDefinition="Indicates the degree of conformance expectations associated with this binding"
	)
	private BoundCodeDt<BindingConformanceEnum> myConformance;
	
	@Child(name="description", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Describes the intended use of this particular set of codes"
	)
	private StringDt myDescription;
	
	@Child(name="valueSet", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dev.resource.ValueSet.class	})
	@Description(
		shortDefinition="",
		formalDefinition="Points to the value set that identifies the set of codes to be used"
	)
	private ResourceReferenceDt myValueSet;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIsExtensible,  myConformance,  myDescription,  myValueSet);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIsExtensible, myConformance, myDescription, myValueSet);
	}

	/**
	 * Gets the value(s) for <b>isExtensible</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public BooleanDt getIsExtensibleElement() {  
		if (myIsExtensible == null) {
			myIsExtensible = new BooleanDt();
		}
		return myIsExtensible;
	}

	
	/**
	 * Gets the value(s) for <b>isExtensible</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public Boolean getIsExtensible() {  
		return getIsExtensibleElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>isExtensible</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public Binding setIsExtensible(BooleanDt theValue) {
		myIsExtensible = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>isExtensible</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, then conformant systems may use additional codes or (where the data type permits) text alone to convey concepts not covered by the set of codes identified in the binding.  If false, then conformant systems are constrained to the provided codes alone
     * </p> 
	 */
	public Binding setIsExtensible( boolean theBoolean) {
		myIsExtensible = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>conformance</b> (BindingConformance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public BoundCodeDt<BindingConformanceEnum> getConformanceElement() {  
		if (myConformance == null) {
			myConformance = new BoundCodeDt<BindingConformanceEnum>(BindingConformanceEnum.VALUESET_BINDER);
		}
		return myConformance;
	}

	
	/**
	 * Gets the value(s) for <b>conformance</b> (BindingConformance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public String getConformance() {  
		return getConformanceElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>conformance</b> (BindingConformance)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public Binding setConformance(BoundCodeDt<BindingConformanceEnum> theValue) {
		myConformance = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>conformance</b> (BindingConformance)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the degree of conformance expectations associated with this binding
     * </p> 
	 */
	public Binding setConformance(BindingConformanceEnum theValue) {
		getConformanceElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>description</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
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
     * Describes the intended use of this particular set of codes
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
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public Binding setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>description</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Describes the intended use of this particular set of codes
     * </p> 
	 */
	public Binding setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>valueSet</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set that identifies the set of codes to be used
     * </p> 
	 */
	public ResourceReferenceDt getValueSet() {  
		if (myValueSet == null) {
			myValueSet = new ResourceReferenceDt();
		}
		return myValueSet;
	}

	/**
	 * Sets the value(s) for <b>valueSet</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Points to the value set that identifies the set of codes to be used
     * </p> 
	 */
	public Binding setValueSet(ResourceReferenceDt theValue) {
		myValueSet = theValue;
		return this;
	}
	
	

  

	}


	/**
	 * Block class for child element: <b>DataElement.mapping</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a concept from an external specification that roughly corresponds to this element
     * </p> 
	 */
	@Block()	
	public static class Mapping 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="uri", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A URI that identifies the specification that this mapping is expressed to"
	)
	private UriDt myUri;
	
	@Child(name="definitional", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element."
	)
	private BooleanDt myDefinitional;
	
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
	
	@Child(name="map", type=StringDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="Expresses what part of the target specification corresponds to this element"
	)
	private StringDt myMap;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myUri,  myDefinitional,  myName,  myComments,  myMap);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myUri, myDefinitional, myName, myComments, myMap);
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
	 * Gets the value(s) for <b>definitional</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.
     * </p> 
	 */
	public BooleanDt getDefinitionalElement() {  
		if (myDefinitional == null) {
			myDefinitional = new BooleanDt();
		}
		return myDefinitional;
	}

	
	/**
	 * Gets the value(s) for <b>definitional</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.
     * </p> 
	 */
	public Boolean getDefinitional() {  
		return getDefinitionalElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>definitional</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.
     * </p> 
	 */
	public Mapping setDefinitional(BooleanDt theValue) {
		myDefinitional = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>definitional</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * If true, indicates that the official meaning of the data element is exactly equivalent to the mapped element.
     * </p> 
	 */
	public Mapping setDefinitional( boolean theBoolean) {
		myDefinitional = new BooleanDt(theBoolean); 
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

 
	/**
	 * Gets the value(s) for <b>map</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public StringDt getMapElement() {  
		if (myMap == null) {
			myMap = new StringDt();
		}
		return myMap;
	}

	
	/**
	 * Gets the value(s) for <b>map</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public String getMap() {  
		return getMapElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>map</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public Mapping setMap(StringDt theValue) {
		myMap = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>map</b> ()
	 *
     * <p>
     * <b>Definition:</b>
     * Expresses what part of the target specification corresponds to this element
     * </p> 
	 */
	public Mapping setMap( String theString) {
		myMap = new StringDt(theString); 
		return this; 
	}

 

	}




    @Override
    public String getResourceName() {
        return "DataElement";
    }
    
    public ca.uhn.fhir.context.FhirVersionEnum getStructureFhirVersionEnum() {
    	return ca.uhn.fhir.context.FhirVersionEnum.DEV;
    }

}
