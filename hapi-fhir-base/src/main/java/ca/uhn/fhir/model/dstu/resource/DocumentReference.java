















package ca.uhn.fhir.model.dstu.resource;


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
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.DocumentRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.CompositeClientParam;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.NumberClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>DocumentReference</b> Resource
 * (A reference to a document)
 *
 * <p>
 * <b>Definition:</b>
 * A reference to a document
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/DocumentReference">http://hl7.org/fhir/profiles/DocumentReference</a> 
 * </p>
 *
 */
@ResourceDef(name="DocumentReference", profile="http://hl7.org/fhir/profiles/DocumentReference", id="documentreference")
public class DocumentReference extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.masterIdentifier | DocumentReference.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="DocumentReference.masterIdentifier | DocumentReference.identifier", description="", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.masterIdentifier | DocumentReference.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.subject</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="subject", path="DocumentReference.subject", description="", type="reference"  )
	public static final String SP_SUBJECT = "subject";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>subject</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.subject</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam SUBJECT = new ReferenceClientParam(SP_SUBJECT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.subject</b>".
	 */
	public static final Include INCLUDE_SUBJECT = new Include("DocumentReference.subject");

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="DocumentReference.type", description="", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.class</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="class", path="DocumentReference.class", description="", type="token"  )
	public static final String SP_CLASS = "class";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>class</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.class</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CLASS = new TokenClientParam(SP_CLASS);

	/**
	 * Search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.author</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="author", path="DocumentReference.author", description="", type="reference"  )
	public static final String SP_AUTHOR = "author";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>author</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.author</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHOR = new ReferenceClientParam(SP_AUTHOR);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.author</b>".
	 */
	public static final Include INCLUDE_AUTHOR = new Include("DocumentReference.author");

	/**
	 * Search parameter constant for <b>custodian</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.custodian</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="custodian", path="DocumentReference.custodian", description="", type="reference"  )
	public static final String SP_CUSTODIAN = "custodian";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>custodian</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.custodian</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam CUSTODIAN = new ReferenceClientParam(SP_CUSTODIAN);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.custodian</b>".
	 */
	public static final Include INCLUDE_CUSTODIAN = new Include("DocumentReference.custodian");

	/**
	 * Search parameter constant for <b>authenticator</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.authenticator</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="authenticator", path="DocumentReference.authenticator", description="", type="reference"  )
	public static final String SP_AUTHENTICATOR = "authenticator";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>authenticator</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.authenticator</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam AUTHENTICATOR = new ReferenceClientParam(SP_AUTHENTICATOR);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.authenticator</b>".
	 */
	public static final Include INCLUDE_AUTHENTICATOR = new Include("DocumentReference.authenticator");

	/**
	 * Search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.created</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="created", path="DocumentReference.created", description="", type="date"  )
	public static final String SP_CREATED = "created";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>created</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.created</b><br/>
	 * </p>
	 */
	public static final DateClientParam CREATED = new DateClientParam(SP_CREATED);

	/**
	 * Search parameter constant for <b>indexed</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.indexed</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="indexed", path="DocumentReference.indexed", description="", type="date"  )
	public static final String SP_INDEXED = "indexed";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>indexed</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.indexed</b><br/>
	 * </p>
	 */
	public static final DateClientParam INDEXED = new DateClientParam(SP_INDEXED);

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="DocumentReference.status", description="", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>relatesto</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.relatesTo.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="relatesto", path="DocumentReference.relatesTo.target", description="", type="reference"  )
	public static final String SP_RELATESTO = "relatesto";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>relatesto</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>DocumentReference.relatesTo.target</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RELATESTO = new ReferenceClientParam(SP_RELATESTO);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>DocumentReference.relatesTo.target</b>".
	 */
	public static final Include INCLUDE_RELATESTO_TARGET = new Include("DocumentReference.relatesTo.target");

	/**
	 * Search parameter constant for <b>relation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.relatesTo.code</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="relation", path="DocumentReference.relatesTo.code", description="", type="token"  )
	public static final String SP_RELATION = "relation";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>relation</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.relatesTo.code</b><br/>
	 * </p>
	 */
	public static final TokenClientParam RELATION = new TokenClientParam(SP_RELATION);

	/**
	 * Search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentReference.description</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="description", path="DocumentReference.description", description="", type="string"  )
	public static final String SP_DESCRIPTION = "description";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>description</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentReference.description</b><br/>
	 * </p>
	 */
	public static final StringClientParam DESCRIPTION = new StringClientParam(SP_DESCRIPTION);

	/**
	 * Search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.confidentiality</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="confidentiality", path="DocumentReference.confidentiality", description="", type="token"  )
	public static final String SP_CONFIDENTIALITY = "confidentiality";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>confidentiality</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.confidentiality</b><br/>
	 * </p>
	 */
	public static final TokenClientParam CONFIDENTIALITY = new TokenClientParam(SP_CONFIDENTIALITY);

	/**
	 * Search parameter constant for <b>language</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.primaryLanguage</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="language", path="DocumentReference.primaryLanguage", description="", type="token"  )
	public static final String SP_LANGUAGE = "language";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>language</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.primaryLanguage</b><br/>
	 * </p>
	 */
	public static final TokenClientParam LANGUAGE = new TokenClientParam(SP_LANGUAGE);

	/**
	 * Search parameter constant for <b>format</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.format</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="format", path="DocumentReference.format", description="", type="token"  )
	public static final String SP_FORMAT = "format";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>format</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.format</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FORMAT = new TokenClientParam(SP_FORMAT);

	/**
	 * Search parameter constant for <b>size</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>DocumentReference.size</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="size", path="DocumentReference.size", description="", type="number"  )
	public static final String SP_SIZE = "size";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>size</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>number</b><br/>
	 * Path: <b>DocumentReference.size</b><br/>
	 * </p>
	 */
	public static final NumberClientParam SIZE = new NumberClientParam(SP_SIZE);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentReference.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="DocumentReference.location", description="", type="string"  )
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>DocumentReference.location</b><br/>
	 * </p>
	 */
	public static final StringClientParam LOCATION = new StringClientParam(SP_LOCATION);

	/**
	 * Search parameter constant for <b>event</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.context.event</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="event", path="DocumentReference.context.event", description="", type="token"  )
	public static final String SP_EVENT = "event";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>event</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.context.event</b><br/>
	 * </p>
	 */
	public static final TokenClientParam EVENT = new TokenClientParam(SP_EVENT);

	/**
	 * Search parameter constant for <b>period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.context.period</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="period", path="DocumentReference.context.period", description="", type="date"  )
	public static final String SP_PERIOD = "period";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>period</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>DocumentReference.context.period</b><br/>
	 * </p>
	 */
	public static final DateClientParam PERIOD = new DateClientParam(SP_PERIOD);

	/**
	 * Search parameter constant for <b>facility</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.context.facilityType</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="facility", path="DocumentReference.context.facilityType", description="", type="token"  )
	public static final String SP_FACILITY = "facility";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>facility</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>DocumentReference.context.facilityType</b><br/>
	 * </p>
	 */
	public static final TokenClientParam FACILITY = new TokenClientParam(SP_FACILITY);

	/**
	 * Search parameter constant for <b>relatesto-relation</b>
	 * <p>
	 * Description: <b>Combination of relation and relatesTo</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>relatesto & relation</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="relatesto-relation", path="relatesto & relation", description="Combination of relation and relatesTo", type="composite"  , compositeOf={  "relatesto",  "relation" }  )
	public static final String SP_RELATESTO_RELATION = "relatesto-relation";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>relatesto-relation</b>
	 * <p>
	 * Description: <b>Combination of relation and relatesTo</b><br/>
	 * Type: <b>composite</b><br/>
	 * Path: <b>relatesto & relation</b><br/>
	 * </p>
	 */
	public static final CompositeClientParam<ReferenceClientParam, TokenClientParam> RELATESTO_RELATION = new CompositeClientParam<ReferenceClientParam, TokenClientParam>(SP_RELATESTO_RELATION);


	@Child(name="masterIdentifier", type=IdentifierDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Master Version Specific Identifier",
		formalDefinition="Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document"
	)
	private IdentifierDt myMasterIdentifier;
	
	@Child(name="identifier", type=IdentifierDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Other identifiers for the document",
		formalDefinition="Other identifiers associated with the document, including version independent, source record and workflow related identifiers"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="subject", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Group.class, 		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Who|what is the subject of the document",
		formalDefinition="Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure)"
	)
	private ResourceReferenceDt mySubject;
	
	@Child(name="type", type=CodeableConceptDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="What kind of document this is (LOINC if possible)",
		formalDefinition="Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.)"
	)
	private CodeableConceptDt myType;
	
	@Child(name="class", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Categorization of Document",
		formalDefinition="A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type"
	)
	private CodeableConceptDt myClassElement;
	
	@Child(name="author", order=5, min=1, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Device.class, 		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.RelatedPerson.class	})
	@Description(
		shortDefinition="Who and/or what authored the document",
		formalDefinition="Identifies who is responsible for adding the information to the document"
	)
	private java.util.List<ResourceReferenceDt> myAuthor;
	
	@Child(name="custodian", order=6, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Org which maintains the document",
		formalDefinition="Identifies the organization or group who is responsible for ongoing maintenance of and access to the document"
	)
	private ResourceReferenceDt myCustodian;
	
	@Child(name="policyManager", type=UriDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Manages access policies for the document",
		formalDefinition="A reference to a domain or server that manages policies under which the document is accessed and/or made available"
	)
	private UriDt myPolicyManager;
	
	@Child(name="authenticator", order=8, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class, 		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Who/What authenticated the document",
		formalDefinition="Which person or organization authenticates that this document is valid"
	)
	private ResourceReferenceDt myAuthenticator;
	
	@Child(name="created", type=DateTimeDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Document creation time",
		formalDefinition="When the document was created"
	)
	private DateTimeDt myCreated;
	
	@Child(name="indexed", type=InstantDt.class, order=10, min=1, max=1)	
	@Description(
		shortDefinition="When this document reference created",
		formalDefinition="When the document reference was created"
	)
	private InstantDt myIndexed;
	
	@Child(name="status", type=CodeDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="current | superceded | entered in error",
		formalDefinition="The status of this document reference"
	)
	private BoundCodeDt<DocumentReferenceStatusEnum> myStatus;
	
	@Child(name="docStatus", type=CodeableConceptDt.class, order=12, min=0, max=1)	
	@Description(
		shortDefinition="preliminary | final | appended | amended | entered in error",
		formalDefinition="The status of the underlying document"
	)
	private CodeableConceptDt myDocStatus;
	
	@Child(name="relatesTo", order=13, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Relationships to other documents",
		formalDefinition="Relationships that this document has with other document references that already exist"
	)
	private java.util.List<RelatesTo> myRelatesTo;
	
	@Child(name="description", type=StringDt.class, order=14, min=0, max=1)	
	@Description(
		shortDefinition="Human-readable description (title)",
		formalDefinition="Human-readable description of the source document. This is sometimes known as the \"title\""
	)
	private StringDt myDescription;
	
	@Child(name="confidentiality", type=CodeableConceptDt.class, order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Sensitivity of source document",
		formalDefinition="A code specifying the level of confidentiality of the XDS Document"
	)
	private java.util.List<CodeableConceptDt> myConfidentiality;
	
	@Child(name="primaryLanguage", type=CodeDt.class, order=16, min=0, max=1)	
	@Description(
		shortDefinition="The marked primary language for the document",
		formalDefinition="The primary language in which the source document is written"
	)
	private CodeDt myPrimaryLanguage;
	
	@Child(name="mimeType", type=CodeDt.class, order=17, min=1, max=1)	
	@Description(
		shortDefinition="Mime type, + maybe character encoding",
		formalDefinition="The mime type of the source document"
	)
	private CodeDt myMimeType;
	
	@Child(name="format", type=UriDt.class, order=18, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Format/content rules for the document",
		formalDefinition="An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType"
	)
	private java.util.List<UriDt> myFormat;
	
	@Child(name="size", type=IntegerDt.class, order=19, min=0, max=1)	
	@Description(
		shortDefinition="Size of the document in bytes",
		formalDefinition="The size of the source document this reference refers to in bytes"
	)
	private IntegerDt mySize;
	
	@Child(name="hash", type=StringDt.class, order=20, min=0, max=1)	
	@Description(
		shortDefinition="HexBinary representation of SHA1",
		formalDefinition="A hash of the source document to ensure that changes have not occurred"
	)
	private StringDt myHash;
	
	@Child(name="location", type=UriDt.class, order=21, min=0, max=1)	
	@Description(
		shortDefinition="Where to access the document",
		formalDefinition="A url at which the document can be accessed"
	)
	private UriDt myLocation;
	
	@Child(name="service", order=22, min=0, max=1)	
	@Description(
		shortDefinition="If access is not fully described by location",
		formalDefinition="A description of a service call that can be used to retrieve the document"
	)
	private Service myService;
	
	@Child(name="context", order=23, min=0, max=1)	
	@Description(
		shortDefinition="Clinical context of document",
		formalDefinition="The clinical context in which the document was prepared"
	)
	private Context myContext;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMasterIdentifier,  myIdentifier,  mySubject,  myType,  myClassElement,  myAuthor,  myCustodian,  myPolicyManager,  myAuthenticator,  myCreated,  myIndexed,  myStatus,  myDocStatus,  myRelatesTo,  myDescription,  myConfidentiality,  myPrimaryLanguage,  myMimeType,  myFormat,  mySize,  myHash,  myLocation,  myService,  myContext);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMasterIdentifier, myIdentifier, mySubject, myType, myClassElement, myAuthor, myCustodian, myPolicyManager, myAuthenticator, myCreated, myIndexed, myStatus, myDocStatus, myRelatesTo, myDescription, myConfidentiality, myPrimaryLanguage, myMimeType, myFormat, mySize, myHash, myLocation, myService, myContext);
	}

	/**
	 * Gets the value(s) for <b>masterIdentifier</b> (Master Version Specific Identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document
     * </p> 
	 */
	public IdentifierDt getMasterIdentifier() {  
		if (myMasterIdentifier == null) {
			myMasterIdentifier = new IdentifierDt();
		}
		return myMasterIdentifier;
	}

	/**
	 * Sets the value(s) for <b>masterIdentifier</b> (Master Version Specific Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document
     * </p> 
	 */
	public DocumentReference setMasterIdentifier(IdentifierDt theValue) {
		myMasterIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>masterIdentifier</b> (Master Version Specific Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document
     * </p> 
	 */
	public DocumentReference setMasterIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myMasterIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>masterIdentifier</b> (Master Version Specific Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document
     * </p> 
	 */
	public DocumentReference setMasterIdentifier( String theSystem,  String theValue) {
		myMasterIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>identifier</b> (Other identifiers for the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Other identifiers for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public DocumentReference setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (Other identifiers for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (Other identifiers for the document),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (Other identifiers for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DocumentReference addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (Other identifiers for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DocumentReference addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subject</b> (Who|what is the subject of the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure)
     * </p> 
	 */
	public ResourceReferenceDt getSubject() {  
		if (mySubject == null) {
			mySubject = new ResourceReferenceDt();
		}
		return mySubject;
	}

	/**
	 * Sets the value(s) for <b>subject</b> (Who|what is the subject of the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure)
     * </p> 
	 */
	public DocumentReference setSubject(ResourceReferenceDt theValue) {
		mySubject = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (What kind of document this is (LOINC if possible)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.)
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (What kind of document this is (LOINC if possible))
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.)
     * </p> 
	 */
	public DocumentReference setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>class</b> (Categorization of Document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type
     * </p> 
	 */
	public CodeableConceptDt getClassElement() {  
		if (myClassElement == null) {
			myClassElement = new CodeableConceptDt();
		}
		return myClassElement;
	}

	/**
	 * Sets the value(s) for <b>class</b> (Categorization of Document)
	 *
     * <p>
     * <b>Definition:</b>
     * A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type
     * </p> 
	 */
	public DocumentReference setClassElement(CodeableConceptDt theValue) {
		myClassElement = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>author</b> (Who and/or what authored the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthor() {  
		if (myAuthor == null) {
			myAuthor = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAuthor;
	}

	/**
	 * Sets the value(s) for <b>author</b> (Who and/or what authored the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public DocumentReference setAuthor(java.util.List<ResourceReferenceDt> theValue) {
		myAuthor = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>author</b> (Who and/or what authored the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies who is responsible for adding the information to the document
     * </p> 
	 */
	public ResourceReferenceDt addAuthor() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthor().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>custodian</b> (Org which maintains the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the document
     * </p> 
	 */
	public ResourceReferenceDt getCustodian() {  
		if (myCustodian == null) {
			myCustodian = new ResourceReferenceDt();
		}
		return myCustodian;
	}

	/**
	 * Sets the value(s) for <b>custodian</b> (Org which maintains the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the document
     * </p> 
	 */
	public DocumentReference setCustodian(ResourceReferenceDt theValue) {
		myCustodian = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>policyManager</b> (Manages access policies for the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a domain or server that manages policies under which the document is accessed and/or made available
     * </p> 
	 */
	public UriDt getPolicyManager() {  
		if (myPolicyManager == null) {
			myPolicyManager = new UriDt();
		}
		return myPolicyManager;
	}

	/**
	 * Sets the value(s) for <b>policyManager</b> (Manages access policies for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a domain or server that manages policies under which the document is accessed and/or made available
     * </p> 
	 */
	public DocumentReference setPolicyManager(UriDt theValue) {
		myPolicyManager = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>policyManager</b> (Manages access policies for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * A reference to a domain or server that manages policies under which the document is accessed and/or made available
     * </p> 
	 */
	public DocumentReference setPolicyManager( String theUri) {
		myPolicyManager = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>authenticator</b> (Who/What authenticated the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Which person or organization authenticates that this document is valid
     * </p> 
	 */
	public ResourceReferenceDt getAuthenticator() {  
		if (myAuthenticator == null) {
			myAuthenticator = new ResourceReferenceDt();
		}
		return myAuthenticator;
	}

	/**
	 * Sets the value(s) for <b>authenticator</b> (Who/What authenticated the document)
	 *
     * <p>
     * <b>Definition:</b>
     * Which person or organization authenticates that this document is valid
     * </p> 
	 */
	public DocumentReference setAuthenticator(ResourceReferenceDt theValue) {
		myAuthenticator = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>created</b> (Document creation time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document was created
     * </p> 
	 */
	public DateTimeDt getCreated() {  
		if (myCreated == null) {
			myCreated = new DateTimeDt();
		}
		return myCreated;
	}

	/**
	 * Sets the value(s) for <b>created</b> (Document creation time)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document was created
     * </p> 
	 */
	public DocumentReference setCreated(DateTimeDt theValue) {
		myCreated = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>created</b> (Document creation time)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document was created
     * </p> 
	 */
	public DocumentReference setCreated( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myCreated = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>created</b> (Document creation time)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document was created
     * </p> 
	 */
	public DocumentReference setCreatedWithSecondsPrecision( Date theDate) {
		myCreated = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>indexed</b> (When this document reference created).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When the document reference was created
     * </p> 
	 */
	public InstantDt getIndexed() {  
		if (myIndexed == null) {
			myIndexed = new InstantDt();
		}
		return myIndexed;
	}

	/**
	 * Sets the value(s) for <b>indexed</b> (When this document reference created)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document reference was created
     * </p> 
	 */
	public DocumentReference setIndexed(InstantDt theValue) {
		myIndexed = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>indexed</b> (When this document reference created)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document reference was created
     * </p> 
	 */
	public DocumentReference setIndexed( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myIndexed = new InstantDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>indexed</b> (When this document reference created)
	 *
     * <p>
     * <b>Definition:</b>
     * When the document reference was created
     * </p> 
	 */
	public DocumentReference setIndexedWithMillisPrecision( Date theDate) {
		myIndexed = new InstantDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (current | superceded | entered in error).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document reference
     * </p> 
	 */
	public BoundCodeDt<DocumentReferenceStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<DocumentReferenceStatusEnum>(DocumentReferenceStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (current | superceded | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document reference
     * </p> 
	 */
	public DocumentReference setStatus(BoundCodeDt<DocumentReferenceStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (current | superceded | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this document reference
     * </p> 
	 */
	public DocumentReference setStatus(DocumentReferenceStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>docStatus</b> (preliminary | final | appended | amended | entered in error).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the underlying document
     * </p> 
	 */
	public CodeableConceptDt getDocStatus() {  
		if (myDocStatus == null) {
			myDocStatus = new CodeableConceptDt();
		}
		return myDocStatus;
	}

	/**
	 * Sets the value(s) for <b>docStatus</b> (preliminary | final | appended | amended | entered in error)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the underlying document
     * </p> 
	 */
	public DocumentReference setDocStatus(CodeableConceptDt theValue) {
		myDocStatus = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>relatesTo</b> (Relationships to other documents).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	public java.util.List<RelatesTo> getRelatesTo() {  
		if (myRelatesTo == null) {
			myRelatesTo = new java.util.ArrayList<RelatesTo>();
		}
		return myRelatesTo;
	}

	/**
	 * Sets the value(s) for <b>relatesTo</b> (Relationships to other documents)
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	public DocumentReference setRelatesTo(java.util.List<RelatesTo> theValue) {
		myRelatesTo = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>relatesTo</b> (Relationships to other documents)
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	public RelatesTo addRelatesTo() {
		RelatesTo newType = new RelatesTo();
		getRelatesTo().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relatesTo</b> (Relationships to other documents),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	public RelatesTo getRelatesToFirstRep() {
		if (getRelatesTo().isEmpty()) {
			return addRelatesTo();
		}
		return getRelatesTo().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>description</b> (Human-readable description (title)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Human-readable description (title))
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentReference setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Human-readable description (title))
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the source document. This is sometimes known as the \"title\"
     * </p> 
	 */
	public DocumentReference setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>confidentiality</b> (Sensitivity of source document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of the XDS Document
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getConfidentiality() {  
		if (myConfidentiality == null) {
			myConfidentiality = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myConfidentiality;
	}

	/**
	 * Sets the value(s) for <b>confidentiality</b> (Sensitivity of source document)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of the XDS Document
     * </p> 
	 */
	public DocumentReference setConfidentiality(java.util.List<CodeableConceptDt> theValue) {
		myConfidentiality = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>confidentiality</b> (Sensitivity of source document)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of the XDS Document
     * </p> 
	 */
	public CodeableConceptDt addConfidentiality() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getConfidentiality().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>confidentiality</b> (Sensitivity of source document),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the level of confidentiality of the XDS Document
     * </p> 
	 */
	public CodeableConceptDt getConfidentialityFirstRep() {
		if (getConfidentiality().isEmpty()) {
			return addConfidentiality();
		}
		return getConfidentiality().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>primaryLanguage</b> (The marked primary language for the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The primary language in which the source document is written
     * </p> 
	 */
	public CodeDt getPrimaryLanguage() {  
		if (myPrimaryLanguage == null) {
			myPrimaryLanguage = new CodeDt();
		}
		return myPrimaryLanguage;
	}

	/**
	 * Sets the value(s) for <b>primaryLanguage</b> (The marked primary language for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * The primary language in which the source document is written
     * </p> 
	 */
	public DocumentReference setPrimaryLanguage(CodeDt theValue) {
		myPrimaryLanguage = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>primaryLanguage</b> (The marked primary language for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * The primary language in which the source document is written
     * </p> 
	 */
	public DocumentReference setPrimaryLanguage( String theCode) {
		myPrimaryLanguage = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>mimeType</b> (Mime type, + maybe character encoding).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mime type of the source document
     * </p> 
	 */
	public CodeDt getMimeType() {  
		if (myMimeType == null) {
			myMimeType = new CodeDt();
		}
		return myMimeType;
	}

	/**
	 * Sets the value(s) for <b>mimeType</b> (Mime type, + maybe character encoding)
	 *
     * <p>
     * <b>Definition:</b>
     * The mime type of the source document
     * </p> 
	 */
	public DocumentReference setMimeType(CodeDt theValue) {
		myMimeType = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>mimeType</b> (Mime type, + maybe character encoding)
	 *
     * <p>
     * <b>Definition:</b>
     * The mime type of the source document
     * </p> 
	 */
	public DocumentReference setMimeType( String theCode) {
		myMimeType = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>format</b> (Format/content rules for the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
	 */
	public java.util.List<UriDt> getFormat() {  
		if (myFormat == null) {
			myFormat = new java.util.ArrayList<UriDt>();
		}
		return myFormat;
	}

	/**
	 * Sets the value(s) for <b>format</b> (Format/content rules for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
	 */
	public DocumentReference setFormat(java.util.List<UriDt> theValue) {
		myFormat = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>format</b> (Format/content rules for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
	 */
	public UriDt addFormat() {
		UriDt newType = new UriDt();
		getFormat().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>format</b> (Format/content rules for the document),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
	 */
	public UriDt getFormatFirstRep() {
		if (getFormat().isEmpty()) {
			return addFormat();
		}
		return getFormat().get(0); 
	}
 	/**
	 * Adds a new value for <b>format</b> (Format/content rules for the document)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public DocumentReference addFormat( String theUri) {
		if (myFormat == null) {
			myFormat = new java.util.ArrayList<UriDt>();
		}
		myFormat.add(new UriDt(theUri));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>size</b> (Size of the document in bytes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The size of the source document this reference refers to in bytes
     * </p> 
	 */
	public IntegerDt getSize() {  
		if (mySize == null) {
			mySize = new IntegerDt();
		}
		return mySize;
	}

	/**
	 * Sets the value(s) for <b>size</b> (Size of the document in bytes)
	 *
     * <p>
     * <b>Definition:</b>
     * The size of the source document this reference refers to in bytes
     * </p> 
	 */
	public DocumentReference setSize(IntegerDt theValue) {
		mySize = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>size</b> (Size of the document in bytes)
	 *
     * <p>
     * <b>Definition:</b>
     * The size of the source document this reference refers to in bytes
     * </p> 
	 */
	public DocumentReference setSize( int theInteger) {
		mySize = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>hash</b> (HexBinary representation of SHA1).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A hash of the source document to ensure that changes have not occurred
     * </p> 
	 */
	public StringDt getHash() {  
		if (myHash == null) {
			myHash = new StringDt();
		}
		return myHash;
	}

	/**
	 * Sets the value(s) for <b>hash</b> (HexBinary representation of SHA1)
	 *
     * <p>
     * <b>Definition:</b>
     * A hash of the source document to ensure that changes have not occurred
     * </p> 
	 */
	public DocumentReference setHash(StringDt theValue) {
		myHash = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>hash</b> (HexBinary representation of SHA1)
	 *
     * <p>
     * <b>Definition:</b>
     * A hash of the source document to ensure that changes have not occurred
     * </p> 
	 */
	public DocumentReference setHash( String theString) {
		myHash = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>location</b> (Where to access the document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A url at which the document can be accessed
     * </p> 
	 */
	public UriDt getLocation() {  
		if (myLocation == null) {
			myLocation = new UriDt();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (Where to access the document)
	 *
     * <p>
     * <b>Definition:</b>
     * A url at which the document can be accessed
     * </p> 
	 */
	public DocumentReference setLocation(UriDt theValue) {
		myLocation = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>location</b> (Where to access the document)
	 *
     * <p>
     * <b>Definition:</b>
     * A url at which the document can be accessed
     * </p> 
	 */
	public DocumentReference setLocation( String theUri) {
		myLocation = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>service</b> (If access is not fully described by location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of a service call that can be used to retrieve the document
     * </p> 
	 */
	public Service getService() {  
		if (myService == null) {
			myService = new Service();
		}
		return myService;
	}

	/**
	 * Sets the value(s) for <b>service</b> (If access is not fully described by location)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of a service call that can be used to retrieve the document
     * </p> 
	 */
	public DocumentReference setService(Service theValue) {
		myService = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>context</b> (Clinical context of document).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical context in which the document was prepared
     * </p> 
	 */
	public Context getContext() {  
		if (myContext == null) {
			myContext = new Context();
		}
		return myContext;
	}

	/**
	 * Sets the value(s) for <b>context</b> (Clinical context of document)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical context in which the document was prepared
     * </p> 
	 */
	public DocumentReference setContext(Context theValue) {
		myContext = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>DocumentReference.relatesTo</b> (Relationships to other documents)
	 *
     * <p>
     * <b>Definition:</b>
     * Relationships that this document has with other document references that already exist
     * </p> 
	 */
	@Block()	
	public static class RelatesTo extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="replaces | transforms | signs | appends",
		formalDefinition="The type of relationship that this document has with anther document"
	)
	private BoundCodeDt<DocumentRelationshipTypeEnum> myCode;
	
	@Child(name="target", order=1, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.DocumentReference.class	})
	@Description(
		shortDefinition="Target of the relationship",
		formalDefinition="The target document of this relationship"
	)
	private ResourceReferenceDt myTarget;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myTarget);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myTarget);
	}

	/**
	 * Gets the value(s) for <b>code</b> (replaces | transforms | signs | appends).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this document has with anther document
     * </p> 
	 */
	public BoundCodeDt<DocumentRelationshipTypeEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<DocumentRelationshipTypeEnum>(DocumentRelationshipTypeEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (replaces | transforms | signs | appends)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this document has with anther document
     * </p> 
	 */
	public RelatesTo setCode(BoundCodeDt<DocumentRelationshipTypeEnum> theValue) {
		myCode = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>code</b> (replaces | transforms | signs | appends)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of relationship that this document has with anther document
     * </p> 
	 */
	public RelatesTo setCode(DocumentRelationshipTypeEnum theValue) {
		getCode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>target</b> (Target of the relationship).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The target document of this relationship
     * </p> 
	 */
	public ResourceReferenceDt getTarget() {  
		if (myTarget == null) {
			myTarget = new ResourceReferenceDt();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (Target of the relationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The target document of this relationship
     * </p> 
	 */
	public RelatesTo setTarget(ResourceReferenceDt theValue) {
		myTarget = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>DocumentReference.service</b> (If access is not fully described by location)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of a service call that can be used to retrieve the document
     * </p> 
	 */
	@Block()	
	public static class Service extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Type of service (i.e. XDS.b)",
		formalDefinition="The type of the service that can be used to access the documents"
	)
	private CodeableConceptDt myType;
	
	@Child(name="address", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Where service is located (usually a URL)",
		formalDefinition="Where the service end-point is located"
	)
	private StringDt myAddress;
	
	@Child(name="parameter", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Service call parameters",
		formalDefinition="A list of named parameters that is used in the service call"
	)
	private java.util.List<ServiceParameter> myParameter;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myAddress,  myParameter);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myAddress, myParameter);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Type of service (i.e. XDS.b)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the service that can be used to access the documents
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Type of service (i.e. XDS.b))
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the service that can be used to access the documents
     * </p> 
	 */
	public Service setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>address</b> (Where service is located (usually a URL)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Where the service end-point is located
     * </p> 
	 */
	public StringDt getAddress() {  
		if (myAddress == null) {
			myAddress = new StringDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (Where service is located (usually a URL))
	 *
     * <p>
     * <b>Definition:</b>
     * Where the service end-point is located
     * </p> 
	 */
	public Service setAddress(StringDt theValue) {
		myAddress = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>address</b> (Where service is located (usually a URL))
	 *
     * <p>
     * <b>Definition:</b>
     * Where the service end-point is located
     * </p> 
	 */
	public Service setAddress( String theString) {
		myAddress = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>parameter</b> (Service call parameters).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	public java.util.List<ServiceParameter> getParameter() {  
		if (myParameter == null) {
			myParameter = new java.util.ArrayList<ServiceParameter>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> (Service call parameters)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	public Service setParameter(java.util.List<ServiceParameter> theValue) {
		myParameter = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>parameter</b> (Service call parameters)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	public ServiceParameter addParameter() {
		ServiceParameter newType = new ServiceParameter();
		getParameter().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>parameter</b> (Service call parameters),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	public ServiceParameter getParameterFirstRep() {
		if (getParameter().isEmpty()) {
			return addParameter();
		}
		return getParameter().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>DocumentReference.service.parameter</b> (Service call parameters)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of named parameters that is used in the service call
     * </p> 
	 */
	@Block()	
	public static class ServiceParameter extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Parameter name in service call",
		formalDefinition="The name of a parameter"
	)
	private StringDt myName;
	
	@Child(name="value", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Parameter value for the name",
		formalDefinition="The value of the named parameter"
	)
	private StringDt myValue;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myValue);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myValue);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Parameter name in service call).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a parameter
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Parameter name in service call)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a parameter
     * </p> 
	 */
	public ServiceParameter setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Parameter name in service call)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a parameter
     * </p> 
	 */
	public ServiceParameter setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>value</b> (Parameter value for the name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the named parameter
     * </p> 
	 */
	public StringDt getValue() {  
		if (myValue == null) {
			myValue = new StringDt();
		}
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value</b> (Parameter value for the name)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the named parameter
     * </p> 
	 */
	public ServiceParameter setValue(StringDt theValue) {
		myValue = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>value</b> (Parameter value for the name)
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the named parameter
     * </p> 
	 */
	public ServiceParameter setValue( String theString) {
		myValue = new StringDt(theString); 
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>DocumentReference.context</b> (Clinical context of document)
	 *
     * <p>
     * <b>Definition:</b>
     * The clinical context in which the document was prepared
     * </p> 
	 */
	@Block()	
	public static class Context extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="event", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Main Clinical Acts Documented",
		formalDefinition="This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act"
	)
	private java.util.List<CodeableConceptDt> myEvent;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Time of service that is being documented",
		formalDefinition="The time period over which the service that is described by the document was provided"
	)
	private PeriodDt myPeriod;
	
	@Child(name="facilityType", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Kind of facility where patient was seen",
		formalDefinition="The kind of facility where the patient was seen"
	)
	private CodeableConceptDt myFacilityType;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myEvent,  myPeriod,  myFacilityType);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myEvent, myPeriod, myFacilityType);
	}

	/**
	 * Gets the value(s) for <b>event</b> (Main Clinical Acts Documented).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (Main Clinical Acts Documented)
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act
     * </p> 
	 */
	public Context setEvent(java.util.List<CodeableConceptDt> theValue) {
		myEvent = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>event</b> (Main Clinical Acts Documented)
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act
     * </p> 
	 */
	public CodeableConceptDt addEvent() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getEvent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>event</b> (Main Clinical Acts Documented),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act
     * </p> 
	 */
	public CodeableConceptDt getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>period</b> (Time of service that is being documented).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time period over which the service that is described by the document was provided
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (Time of service that is being documented)
	 *
     * <p>
     * <b>Definition:</b>
     * The time period over which the service that is described by the document was provided
     * </p> 
	 */
	public Context setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>facilityType</b> (Kind of facility where patient was seen).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of facility where the patient was seen
     * </p> 
	 */
	public CodeableConceptDt getFacilityType() {  
		if (myFacilityType == null) {
			myFacilityType = new CodeableConceptDt();
		}
		return myFacilityType;
	}

	/**
	 * Sets the value(s) for <b>facilityType</b> (Kind of facility where patient was seen)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of facility where the patient was seen
     * </p> 
	 */
	public Context setFacilityType(CodeableConceptDt theValue) {
		myFacilityType = theValue;
		return this;
	}

  

	}




}