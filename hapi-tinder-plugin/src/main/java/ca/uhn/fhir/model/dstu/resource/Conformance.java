















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
import ca.uhn.fhir.model.base.resource.BaseConformance;
import ca.uhn.fhir.model.dstu.composite.BoundCodeableConceptDt_;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.ConformanceEventModeEnum;
import ca.uhn.fhir.model.dstu.valueset.ConformanceStatementStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.dstu.valueset.DocumentModeEnum;
import ca.uhn.fhir.model.dstu.valueset.MessageSignificanceCategoryEnum;
import ca.uhn.fhir.model.dstu.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulConformanceModeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
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
 * (A conformance statement)
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
@ResourceDef(name="Conformance", profile="http://hl7.org/fhir/profiles/Conformance", id="conformance")
public class Conformance extends BaseConformance implements IResource {

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
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Conformance.rest.resource.profile</b>".
	 */
	public static final Include INCLUDE_REST_RESOURCE_PROFILE = new Include("Conformance.rest.resource.profile");

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
	 * the path value of "<b>Conformance.profile</b>".
	 */
	public static final Include INCLUDE_PROFILE = new Include("Conformance.profile");


	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Logical id to reference this statement",
		formalDefinition="The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)"
	)
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Logical id for this version of the statement",
		formalDefinition="The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Informal name for this conformance statement",
		formalDefinition="A free text natural language name identifying the conformance statement"
	)
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Publishing Organization",
		formalDefinition="Name of Organization publishing this conformance statement"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contacts for Organization",
		formalDefinition="Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc."
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Human description of the conformance statement",
		formalDefinition="A free text natural language description of the conformance statement and its use. Typically, this is used when the profile describes a desired rather than an actual solution, for example as a formal expression of requirements as part of an RFP"
	)
	private StringDt myDescription;
	
	@Child(name="status", type=CodeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="draft | active | retired",
		formalDefinition="The status of this conformance statement"
	)
	private BoundCodeDt<ConformanceStatementStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="If for testing purposes, not real usage",
		formalDefinition="A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="date", type=DateTimeDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="Publication Date",
		formalDefinition="The date when the conformance statement was published"
	)
	private DateTimeDt myDate;
	
	@Child(name="software", order=9, min=0, max=1)	
	@Description(
		shortDefinition="Software that is covered by this conformance statement",
		formalDefinition="Software that is covered by this conformance statement.  It is used when the profile describes the capabilities of a particular software version, independent of an installation."
	)
	private Software mySoftware;
	
	@Child(name="implementation", order=10, min=0, max=1)	
	@Description(
		shortDefinition="If this describes a specific instance",
		formalDefinition="Identifies a specific implementation instance that is described by the conformance statement - i.e. a particular installation, rather than the capabilities of a software program"
	)
	private Implementation myImplementation;
	
	@Child(name="fhirVersion", type=IdDt.class, order=11, min=1, max=1)	
	@Description(
		shortDefinition="FHIR Version",
		formalDefinition="The version of the FHIR specification on which this conformance statement is based"
	)
	private IdDt myFhirVersion;
	
	@Child(name="acceptUnknown", type=BooleanDt.class, order=12, min=1, max=1)	
	@Description(
		shortDefinition="True if application accepts unknown elements",
		formalDefinition="A flag that indicates whether the application accepts unknown elements as part of a resource."
	)
	private BooleanDt myAcceptUnknown;
	
	@Child(name="format", type=CodeDt.class, order=13, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="formats supported (xml | json | mime type)",
		formalDefinition="A list of the formats supported by this implementation"
	)
	private java.util.List<CodeDt> myFormat;
	
	@Child(name="profile", order=14, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Profile.class	})
	@Description(
		shortDefinition="Profiles supported by the system",
		formalDefinition="A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of recourses, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile."
	)
	private java.util.List<ResourceReferenceDt> myProfile;
	
	@Child(name="rest", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="If the endpoint is a RESTful one",
		formalDefinition="A definition of the restful capabilities of the solution, if any"
	)
	private java.util.List<Rest> myRest;
	
	@Child(name="messaging", order=16, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="If messaging is supported",
		formalDefinition="A description of the messaging capabilities of the solution"
	)
	private java.util.List<Messaging> myMessaging;
	
	@Child(name="document", order=17, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Document definition",
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
	 * Gets the value(s) for <b>identifier</b> (Logical id to reference this statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this conformance statement when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Logical id to reference this statement)
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
	 * Sets the value for <b>identifier</b> (Logical id to reference this statement)
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
	 * Gets the value(s) for <b>version</b> (Logical id for this version of the statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the conformance statement when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Logical id for this version of the statement)
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
	 * Sets the value for <b>version</b> (Logical id for this version of the statement)
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
	 * Gets the value(s) for <b>name</b> (Informal name for this conformance statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name identifying the conformance statement
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Informal name for this conformance statement)
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
	 * Sets the value for <b>name</b> (Informal name for this conformance statement)
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
	 * Gets the value(s) for <b>publisher</b> (Publishing Organization).
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
	 * Sets the value(s) for <b>publisher</b> (Publishing Organization)
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
	 * Sets the value for <b>publisher</b> (Publishing Organization)
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
	 * Gets the value(s) for <b>telecom</b> (Contacts for Organization).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (Contacts for Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
	 */
	public Conformance setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (Contacts for Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (Contacts for Organization),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
	 */
	public ContactDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
 	/**
	 * Adds a new value for <b>telecom</b> (Contacts for Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Conformance addTelecom( ContactUseEnum theContactUse,  String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theContactUse, theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>telecom</b> (Contacts for Organization)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts for Organization relevant to this conformance statement.  The contacts may be a website, email, phone numbers, etc.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Conformance addTelecom( String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (Human description of the conformance statement).
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
	 * Sets the value(s) for <b>description</b> (Human description of the conformance statement)
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
	 * Sets the value for <b>description</b> (Human description of the conformance statement)
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
	 * Gets the value(s) for <b>status</b> (draft | active | retired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this conformance statement
     * </p> 
	 */
	public BoundCodeDt<ConformanceStatementStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ConformanceStatementStatusEnum>(ConformanceStatementStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
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
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of this conformance statement
     * </p> 
	 */
	public Conformance setStatus(ConformanceStatementStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>experimental</b> (If for testing purposes, not real usage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
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
     * A flag to indicate that this conformance statement is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public Conformance setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>experimental</b> (If for testing purposes, not real usage)
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
	 * Gets the value(s) for <b>date</b> (Publication Date).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the conformance statement was published
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Publication Date)
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the conformance statement was published
     * </p> 
	 */
	public Conformance setDate(DateTimeDt theValue) {
		myDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>date</b> (Publication Date)
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the conformance statement was published
     * </p> 
	 */
	public Conformance setDateWithSecondsPrecision( Date theDate) {
		myDate = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>date</b> (Publication Date)
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the conformance statement was published
     * </p> 
	 */
	public Conformance setDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>software</b> (Software that is covered by this conformance statement).
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
	 * Sets the value(s) for <b>software</b> (Software that is covered by this conformance statement)
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
	 * Gets the value(s) for <b>implementation</b> (If this describes a specific instance).
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
	 * Sets the value(s) for <b>implementation</b> (If this describes a specific instance)
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
	 * Gets the value(s) for <b>fhirVersion</b> (FHIR Version).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the FHIR specification on which this conformance statement is based
     * </p> 
	 */
	public IdDt getFhirVersion() {  
		if (myFhirVersion == null) {
			myFhirVersion = new IdDt();
		}
		return myFhirVersion;
	}

	/**
	 * Sets the value(s) for <b>fhirVersion</b> (FHIR Version)
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
	 * Sets the value for <b>fhirVersion</b> (FHIR Version)
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
	 * Gets the value(s) for <b>acceptUnknown</b> (True if application accepts unknown elements).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag that indicates whether the application accepts unknown elements as part of a resource.
     * </p> 
	 */
	public BooleanDt getAcceptUnknown() {  
		if (myAcceptUnknown == null) {
			myAcceptUnknown = new BooleanDt();
		}
		return myAcceptUnknown;
	}

	/**
	 * Sets the value(s) for <b>acceptUnknown</b> (True if application accepts unknown elements)
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
	 * Sets the value for <b>acceptUnknown</b> (True if application accepts unknown elements)
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
	 * Gets the value(s) for <b>format</b> (formats supported (xml | json | mime type)).
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
	 * Sets the value(s) for <b>format</b> (formats supported (xml | json | mime type))
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
	 * Adds and returns a new value for <b>format</b> (formats supported (xml | json | mime type))
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
	 * Gets the first repetition for <b>format</b> (formats supported (xml | json | mime type)),
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
	 * Adds a new value for <b>format</b> (formats supported (xml | json | mime type))
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
	 * Gets the value(s) for <b>profile</b> (Profiles supported by the system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of recourses, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getProfile() {  
		if (myProfile == null) {
			myProfile = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myProfile;
	}

	/**
	 * Sets the value(s) for <b>profile</b> (Profiles supported by the system)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of recourses, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile.
     * </p> 
	 */
	public Conformance setProfile(java.util.List<ResourceReferenceDt> theValue) {
		myProfile = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>profile</b> (Profiles supported by the system)
	 *
     * <p>
     * <b>Definition:</b>
     * A list of profiles supported by the system. For a server, \"supported by the system\" means the system hosts/produces a set of recourses, conformant to a particular profile, and allows its clients to search using this profile and to find appropriate data. For a client, it means the system will search by this profile and process data according to the guidance implicit in the profile.
     * </p> 
	 */
	public ResourceReferenceDt addProfile() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getProfile().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>rest</b> (If the endpoint is a RESTful one).
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
	 * Sets the value(s) for <b>rest</b> (If the endpoint is a RESTful one)
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
	 * Adds and returns a new value for <b>rest</b> (If the endpoint is a RESTful one)
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
	 * Gets the first repetition for <b>rest</b> (If the endpoint is a RESTful one),
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
	 * Gets the value(s) for <b>messaging</b> (If messaging is supported).
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
	 * Sets the value(s) for <b>messaging</b> (If messaging is supported)
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
	 * Adds and returns a new value for <b>messaging</b> (If messaging is supported)
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
	 * Gets the first repetition for <b>messaging</b> (If messaging is supported),
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
	 * Gets the value(s) for <b>document</b> (Document definition).
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
	 * Sets the value(s) for <b>document</b> (Document definition)
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
	 * Adds and returns a new value for <b>document</b> (Document definition)
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
	 * Gets the first repetition for <b>document</b> (Document definition),
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
	 * Block class for child element: <b>Conformance.software</b> (Software that is covered by this conformance statement)
	 *
     * <p>
     * <b>Definition:</b>
     * Software that is covered by this conformance statement.  It is used when the profile describes the capabilities of a particular software version, independent of an installation.
     * </p> 
	 */
	@Block()	
	public static class Software extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="A name the software is known by",
		formalDefinition="Name software is known by"
	)
	private StringDt myName;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Version covered by this statement",
		formalDefinition="The version identifier for the software covered by this statement"
	)
	private StringDt myVersion;
	
	@Child(name="releaseDate", type=DateTimeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Date this version released",
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
	 * Gets the value(s) for <b>name</b> (A name the software is known by).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name software is known by
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (A name the software is known by)
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
	 * Sets the value for <b>name</b> (A name the software is known by)
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
	 * Gets the value(s) for <b>version</b> (Version covered by this statement).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version identifier for the software covered by this statement
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Version covered by this statement)
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
	 * Sets the value for <b>version</b> (Version covered by this statement)
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
	 * Gets the value(s) for <b>releaseDate</b> (Date this version released).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Date this version of the software released
     * </p> 
	 */
	public DateTimeDt getReleaseDate() {  
		if (myReleaseDate == null) {
			myReleaseDate = new DateTimeDt();
		}
		return myReleaseDate;
	}

	/**
	 * Sets the value(s) for <b>releaseDate</b> (Date this version released)
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
	 * Sets the value for <b>releaseDate</b> (Date this version released)
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

	/**
	 * Sets the value for <b>releaseDate</b> (Date this version released)
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

 

	}


	/**
	 * Block class for child element: <b>Conformance.implementation</b> (If this describes a specific instance)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a specific implementation instance that is described by the conformance statement - i.e. a particular installation, rather than the capabilities of a software program
     * </p> 
	 */
	@Block()	
	public static class Implementation extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="description", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Describes this specific instance",
		formalDefinition="Information about the specific installation that this conformance statement relates to"
	)
	private StringDt myDescription;
	
	@Child(name="url", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Base URL for the installation",
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
	 * Gets the value(s) for <b>description</b> (Describes this specific instance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the specific installation that this conformance statement relates to
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Describes this specific instance)
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
	 * Sets the value for <b>description</b> (Describes this specific instance)
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
	 * Gets the value(s) for <b>url</b> (Base URL for the installation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A base URL for the implementation.  This forms the base for REST interfaces as well as the mailbox and document interfaces.
     * </p> 
	 */
	public UriDt getUrl() {  
		if (myUrl == null) {
			myUrl = new UriDt();
		}
		return myUrl;
	}

	/**
	 * Sets the value(s) for <b>url</b> (Base URL for the installation)
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
	 * Sets the value for <b>url</b> (Base URL for the installation)
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
	 * Block class for child element: <b>Conformance.rest</b> (If the endpoint is a RESTful one)
	 *
     * <p>
     * <b>Definition:</b>
     * A definition of the restful capabilities of the solution, if any
     * </p> 
	 */
	@Block()	
	public static class Rest extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="mode", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="client | server",
		formalDefinition="Identifies whether this portion of the statement is describing ability to initiate or receive restful operations"
	)
	private BoundCodeDt<RestfulConformanceModeEnum> myMode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="General description of implementation",
		formalDefinition="Information about the system's restful capabilities that apply across all applications, such as security"
	)
	private StringDt myDocumentation;
	
	@Child(name="security", order=2, min=0, max=1)	
	@Description(
		shortDefinition="Information about security of implementation",
		formalDefinition="Information about security of implementation"
	)
	private RestSecurity mySecurity;
	
	@Child(name="resource", order=3, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Resource served on the REST interface",
		formalDefinition="A specification of the restful capabilities of the solution for a specific resource type"
	)
	private java.util.List<RestResource> myResource;
	
	@Child(name="operation", order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="What operations are supported?",
		formalDefinition="A specification of restful operations supported by the system"
	)
	private java.util.List<RestOperation> myOperation;
	
	@Child(name="query", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Definition of a named query",
		formalDefinition="Definition of a named query and its parameters and their meaning"
	)
	private java.util.List<RestQuery> myQuery;
	
	@Child(name="documentMailbox", type=UriDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="How documents are accepted in /Mailbox",
		formalDefinition="A list of profiles that this server implements for accepting documents in the mailbox. If this list is empty, then documents are not accepted. The base specification has the profile identifier \"http://hl7.org/fhir/documents/mailbox\". Other specifications can declare their own identifier for this purpose"
	)
	private java.util.List<UriDt> myDocumentMailbox;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myMode,  myDocumentation,  mySecurity,  myResource,  myOperation,  myQuery,  myDocumentMailbox);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myMode, myDocumentation, mySecurity, myResource, myOperation, myQuery, myDocumentMailbox);
	}

	/**
	 * Gets the value(s) for <b>mode</b> (client | server).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies whether this portion of the statement is describing ability to initiate or receive restful operations
     * </p> 
	 */
	public BoundCodeDt<RestfulConformanceModeEnum> getMode() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<RestfulConformanceModeEnum>(RestfulConformanceModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (client | server)
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
	 * Sets the value(s) for <b>mode</b> (client | server)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies whether this portion of the statement is describing ability to initiate or receive restful operations
     * </p> 
	 */
	public Rest setMode(RestfulConformanceModeEnum theValue) {
		getMode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (General description of implementation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Information about the system's restful capabilities that apply across all applications, such as security
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (General description of implementation)
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
	 * Sets the value for <b>documentation</b> (General description of implementation)
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
	 * Gets the value(s) for <b>security</b> (Information about security of implementation).
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
	 * Sets the value(s) for <b>security</b> (Information about security of implementation)
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
	 * Gets the value(s) for <b>resource</b> (Resource served on the REST interface).
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
	 * Sets the value(s) for <b>resource</b> (Resource served on the REST interface)
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
	 * Adds and returns a new value for <b>resource</b> (Resource served on the REST interface)
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
	 * Gets the first repetition for <b>resource</b> (Resource served on the REST interface),
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
	 * Gets the value(s) for <b>operation</b> (What operations are supported?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public java.util.List<RestOperation> getOperation() {  
		if (myOperation == null) {
			myOperation = new java.util.ArrayList<RestOperation>();
		}
		return myOperation;
	}

	/**
	 * Sets the value(s) for <b>operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public Rest setOperation(java.util.List<RestOperation> theValue) {
		myOperation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public RestOperation addOperation() {
		RestOperation newType = new RestOperation();
		getOperation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>operation</b> (What operations are supported?),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	public RestOperation getOperationFirstRep() {
		if (getOperation().isEmpty()) {
			return addOperation();
		}
		return getOperation().get(0); 
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
	public java.util.List<RestQuery> getQuery() {  
		if (myQuery == null) {
			myQuery = new java.util.ArrayList<RestQuery>();
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
	public Rest setQuery(java.util.List<RestQuery> theValue) {
		myQuery = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>query</b> (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public RestQuery addQuery() {
		RestQuery newType = new RestQuery();
		getQuery().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>query</b> (Definition of a named query),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	public RestQuery getQueryFirstRep() {
		if (getQuery().isEmpty()) {
			return addQuery();
		}
		return getQuery().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>documentMailbox</b> (How documents are accepted in /Mailbox).
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
	 * Sets the value(s) for <b>documentMailbox</b> (How documents are accepted in /Mailbox)
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
	 * Adds and returns a new value for <b>documentMailbox</b> (How documents are accepted in /Mailbox)
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
	 * Gets the first repetition for <b>documentMailbox</b> (How documents are accepted in /Mailbox),
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
	 * Adds a new value for <b>documentMailbox</b> (How documents are accepted in /Mailbox)
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
	 * Block class for child element: <b>Conformance.rest.security</b> (Information about security of implementation)
	 *
     * <p>
     * <b>Definition:</b>
     * Information about security of implementation
     * </p> 
	 */
	@Block()	
	public static class RestSecurity extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="cors", type=BooleanDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Adds CORS Headers (http://enable-cors.org/)",
		formalDefinition="Server adds CORS headers when responding to requests - this enables javascript applications to yuse the server"
	)
	private BooleanDt myCors;
	
	@Child(name="service", type=CodeableConceptDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="OAuth | OAuth2 | NTLM | Basic | Kerberos",
		formalDefinition="Types of security services are supported/required by the system"
	)
	private java.util.List<BoundCodeableConceptDt_<RestfulSecurityServiceEnum>> myService;
	
	@Child(name="description", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="General description of how security works",
		formalDefinition="General description of how security works"
	)
	private StringDt myDescription;
	
	@Child(name="certificate", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Certificates associated with security profiles",
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
	 * Gets the value(s) for <b>cors</b> (Adds CORS Headers (http://enable-cors.org/)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to yuse the server
     * </p> 
	 */
	public BooleanDt getCors() {  
		if (myCors == null) {
			myCors = new BooleanDt();
		}
		return myCors;
	}

	/**
	 * Sets the value(s) for <b>cors</b> (Adds CORS Headers (http://enable-cors.org/))
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to yuse the server
     * </p> 
	 */
	public RestSecurity setCors(BooleanDt theValue) {
		myCors = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>cors</b> (Adds CORS Headers (http://enable-cors.org/))
	 *
     * <p>
     * <b>Definition:</b>
     * Server adds CORS headers when responding to requests - this enables javascript applications to yuse the server
     * </p> 
	 */
	public RestSecurity setCors( boolean theBoolean) {
		myCors = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public java.util.List<BoundCodeableConceptDt_<RestfulSecurityServiceEnum>> getService() {  
		if (myService == null) {
			myService = new java.util.ArrayList<BoundCodeableConceptDt_<RestfulSecurityServiceEnum>>();
		}
		return myService;
	}

	/**
	 * Sets the value(s) for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public RestSecurity setService(java.util.List<BoundCodeableConceptDt_<RestfulSecurityServiceEnum>> theValue) {
		myService = theValue;
		return this;
	}

	/**
	 * Add a value for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos) using an enumerated type. This
	 * is intended as a convenience method for situations where the FHIR defined ValueSets are mandatory
	 * or contain the desirable codes. If you wish to use codes other than those which are built-in, 
	 * you may also use the {@link #addType()} method.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public BoundCodeableConceptDt_<RestfulSecurityServiceEnum> addService(RestfulSecurityServiceEnum theValue) {
		BoundCodeableConceptDt_<RestfulSecurityServiceEnum> retVal = new BoundCodeableConceptDt_<RestfulSecurityServiceEnum>(RestfulSecurityServiceEnum.VALUESET_BINDER, theValue);
		getService().add(retVal);
		return retVal;
	}

	/**
	 * Gets the first repetition for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public BoundCodeableConceptDt_<RestfulSecurityServiceEnum> getServiceFirstRep() {
		if (getService().size() == 0) {
			addService();
		}
		return getService().get(0);
	}

	/**
	 * Add a value for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos)
	 *
     * <p>
     * <b>Definition:</b>
     * Types of security services are supported/required by the system
     * </p> 
	 */
	public BoundCodeableConceptDt_<RestfulSecurityServiceEnum> addService() {
		BoundCodeableConceptDt_<RestfulSecurityServiceEnum> retVal = new BoundCodeableConceptDt_<RestfulSecurityServiceEnum>(RestfulSecurityServiceEnum.VALUESET_BINDER);
		getService().add(retVal);
		return retVal;
	}

	/**
	 * Sets the value(s), and clears any existing value(s) for <b>service</b> (OAuth | OAuth2 | NTLM | Basic | Kerberos)
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
	 * Gets the value(s) for <b>description</b> (General description of how security works).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * General description of how security works
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (General description of how security works)
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
	 * Sets the value for <b>description</b> (General description of how security works)
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
	 * Gets the value(s) for <b>certificate</b> (Certificates associated with security profiles).
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
	 * Sets the value(s) for <b>certificate</b> (Certificates associated with security profiles)
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
	 * Adds and returns a new value for <b>certificate</b> (Certificates associated with security profiles)
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
	 * Gets the first repetition for <b>certificate</b> (Certificates associated with security profiles),
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
	 * Block class for child element: <b>Conformance.rest.security.certificate</b> (Certificates associated with security profiles)
	 *
     * <p>
     * <b>Definition:</b>
     * Certificates associated with security profiles
     * </p> 
	 */
	@Block()	
	public static class RestSecurityCertificate extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Mime type for certificate",
		formalDefinition="Mime type for certificate"
	)
	private CodeDt myType;
	
	@Child(name="blob", type=Base64BinaryDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Actual certificate",
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
	 * Gets the value(s) for <b>type</b> (Mime type for certificate).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Mime type for certificate
     * </p> 
	 */
	public CodeDt getType() {  
		if (myType == null) {
			myType = new CodeDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Mime type for certificate)
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
	 * Sets the value for <b>type</b> (Mime type for certificate)
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
	 * Gets the value(s) for <b>blob</b> (Actual certificate).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Actual certificate
     * </p> 
	 */
	public Base64BinaryDt getBlob() {  
		if (myBlob == null) {
			myBlob = new Base64BinaryDt();
		}
		return myBlob;
	}

	/**
	 * Sets the value(s) for <b>blob</b> (Actual certificate)
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
	 * Sets the value for <b>blob</b> (Actual certificate)
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
	 * Block class for child element: <b>Conformance.rest.resource</b> (Resource served on the REST interface)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of the restful capabilities of the solution for a specific resource type
     * </p> 
	 */
	@Block()	
	public static class RestResource extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="type", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="A resource type that is supported",
		formalDefinition="A type of resource exposed via the restful interface"
	)
	private BoundCodeDt<ResourceTypeEnum> myType;
	
	@Child(name="profile", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Profile.class	})
	@Description(
		shortDefinition="What structural features are supported",
		formalDefinition="A specification of the profile that describes the solution's support for the resource, including any constraints on cardinality, bindings, lengths or other limitations"
	)
	private ResourceReferenceDt myProfile;
	
	@Child(name="operation", order=2, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="What operations are supported?",
		formalDefinition="Identifies a restful operation supported by the solution"
	)
	private java.util.List<RestResourceOperation> myOperation;
	
	@Child(name="readHistory", type=BooleanDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Whether vRead can return past versions",
		formalDefinition="A flag for whether the server is able to return past versions as part of the vRead operation"
	)
	private BooleanDt myReadHistory;
	
	@Child(name="updateCreate", type=BooleanDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="If allows/uses update to a new location",
		formalDefinition="A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server"
	)
	private BooleanDt myUpdateCreate;
	
	@Child(name="searchInclude", type=StringDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="_include values supported by the server",
		formalDefinition="A list of _include values supported by the server"
	)
	private java.util.List<StringDt> mySearchInclude;
	
	@Child(name="searchParam", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Additional search params defined",
		formalDefinition="Additional search parameters for implementations to support and/or make use of"
	)
	private java.util.List<RestResourceSearchParam> mySearchParam;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myProfile,  myOperation,  myReadHistory,  myUpdateCreate,  mySearchInclude,  mySearchParam);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myProfile, myOperation, myReadHistory, myUpdateCreate, mySearchInclude, mySearchParam);
	}

	/**
	 * Gets the value(s) for <b>type</b> (A resource type that is supported).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A type of resource exposed via the restful interface
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (A resource type that is supported)
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
	 * Sets the value(s) for <b>type</b> (A resource type that is supported)
	 *
     * <p>
     * <b>Definition:</b>
     * A type of resource exposed via the restful interface
     * </p> 
	 */
	public RestResource setType(ResourceTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>profile</b> (What structural features are supported).
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
	 * Sets the value(s) for <b>profile</b> (What structural features are supported)
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
	 * Gets the value(s) for <b>operation</b> (What operations are supported?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public java.util.List<RestResourceOperation> getOperation() {  
		if (myOperation == null) {
			myOperation = new java.util.ArrayList<RestResourceOperation>();
		}
		return myOperation;
	}

	/**
	 * Sets the value(s) for <b>operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public RestResource setOperation(java.util.List<RestResourceOperation> theValue) {
		myOperation = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public RestResourceOperation addOperation() {
		RestResourceOperation newType = new RestResourceOperation();
		getOperation().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>operation</b> (What operations are supported?),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	public RestResourceOperation getOperationFirstRep() {
		if (getOperation().isEmpty()) {
			return addOperation();
		}
		return getOperation().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>readHistory</b> (Whether vRead can return past versions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag for whether the server is able to return past versions as part of the vRead operation
     * </p> 
	 */
	public BooleanDt getReadHistory() {  
		if (myReadHistory == null) {
			myReadHistory = new BooleanDt();
		}
		return myReadHistory;
	}

	/**
	 * Sets the value(s) for <b>readHistory</b> (Whether vRead can return past versions)
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
	 * Sets the value for <b>readHistory</b> (Whether vRead can return past versions)
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
	 * Gets the value(s) for <b>updateCreate</b> (If allows/uses update to a new location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A flag to indicate that the server allows the client to create new identities on the server. If the update operation is used (client) or allowed (server) to a new location where a resource doesn't already exist. This means that the server allows the client to create new identities on the server
     * </p> 
	 */
	public BooleanDt getUpdateCreate() {  
		if (myUpdateCreate == null) {
			myUpdateCreate = new BooleanDt();
		}
		return myUpdateCreate;
	}

	/**
	 * Sets the value(s) for <b>updateCreate</b> (If allows/uses update to a new location)
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
	 * Sets the value for <b>updateCreate</b> (If allows/uses update to a new location)
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
	 * Gets the value(s) for <b>searchInclude</b> (_include values supported by the server).
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
	 * Sets the value(s) for <b>searchInclude</b> (_include values supported by the server)
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
	 * Adds and returns a new value for <b>searchInclude</b> (_include values supported by the server)
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
	 * Gets the first repetition for <b>searchInclude</b> (_include values supported by the server),
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
	 * Adds a new value for <b>searchInclude</b> (_include values supported by the server)
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
	 * Gets the value(s) for <b>searchParam</b> (Additional search params defined).
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
	 * Sets the value(s) for <b>searchParam</b> (Additional search params defined)
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
	 * Adds and returns a new value for <b>searchParam</b> (Additional search params defined)
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
	 * Gets the first repetition for <b>searchParam</b> (Additional search params defined),
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
	 * Block class for child element: <b>Conformance.rest.resource.operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a restful operation supported by the solution
     * </p> 
	 */
	@Block()	
	public static class RestResourceOperation extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="read | vread | update | delete | history-instance | validate | history-type | create | search-type",
		formalDefinition="Coded identifier of the operation, supported by the system resource"
	)
	private BoundCodeDt<RestfulOperationTypeEnum> myCode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Anything special about operation behavior",
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
	 * Gets the value(s) for <b>code</b> (read | vread | update | delete | history-instance | validate | history-type | create | search-type).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public BoundCodeDt<RestfulOperationTypeEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<RestfulOperationTypeEnum>(RestfulOperationTypeEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (read | vread | update | delete | history-instance | validate | history-type | create | search-type)
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public RestResourceOperation setCode(BoundCodeDt<RestfulOperationTypeEnum> theValue) {
		myCode = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>code</b> (read | vread | update | delete | history-instance | validate | history-type | create | search-type)
	 *
     * <p>
     * <b>Definition:</b>
     * Coded identifier of the operation, supported by the system resource
     * </p> 
	 */
	public RestResourceOperation setCode(RestfulOperationTypeEnum theValue) {
		getCode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (Anything special about operation behavior).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Anything special about operation behavior)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public RestResourceOperation setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>documentation</b> (Anything special about operation behavior)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only allowed with version id' or 'creates permitted from pre-authorized certificates only'
     * </p> 
	 */
	public RestResourceOperation setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.rest.resource.searchParam</b> (Additional search params defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional search parameters for implementations to support and/or make use of
     * </p> 
	 */
	@Block()	
	public static class RestResourceSearchParam extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Name of search parameter",
		formalDefinition="The name of the search parameter used in the interface"
	)
	private StringDt myName;
	
	@Child(name="definition", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Source of definition for parameter",
		formalDefinition="A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter"
	)
	private UriDt myDefinition;
	
	@Child(name="type", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="number | date | string | token | reference | composite | quantity",
		formalDefinition="The type of value a search parameter refers to, and how the content is interpreted"
	)
	private BoundCodeDt<SearchParamTypeEnum> myType;
	
	@Child(name="documentation", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Server-specific usage",
		formalDefinition="This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms."
	)
	private StringDt myDocumentation;
	
	@Child(name="target", type=CodeDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Types of resource (if a resource reference)",
		formalDefinition="Types of resource (if a resource is referenced)"
	)
	private java.util.List<BoundCodeDt<ResourceTypeEnum>> myTarget;
	
	@Child(name="chain", type=StringDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Chained names supported",
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
	 * Gets the value(s) for <b>name</b> (Name of search parameter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the search parameter used in the interface
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Name of search parameter)
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
	 * Sets the value for <b>name</b> (Name of search parameter)
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
	 * Gets the value(s) for <b>definition</b> (Source of definition for parameter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A formal reference to where this parameter was first defined, so that a client can be confident of the meaning of the search parameter
     * </p> 
	 */
	public UriDt getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new UriDt();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (Source of definition for parameter)
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
	 * Sets the value for <b>definition</b> (Source of definition for parameter)
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
	 * Gets the value(s) for <b>type</b> (number | date | string | token | reference | composite | quantity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public BoundCodeDt<SearchParamTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<SearchParamTypeEnum>(SearchParamTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (number | date | string | token | reference | composite | quantity)
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
	 * Sets the value(s) for <b>type</b> (number | date | string | token | reference | composite | quantity)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of value a search parameter refers to, and how the content is interpreted
     * </p> 
	 */
	public RestResourceSearchParam setType(SearchParamTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (Server-specific usage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This allows documentation of any distinct behaviors about how the search parameter is used.  For example, text matching algorithms.
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Server-specific usage)
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
	 * Sets the value for <b>documentation</b> (Server-specific usage)
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
	 * Gets the value(s) for <b>target</b> (Types of resource (if a resource reference)).
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
	 * Sets the value(s) for <b>target</b> (Types of resource (if a resource reference))
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
	 * Add a value for <b>target</b> (Types of resource (if a resource reference)) using an enumerated type. This
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
	 * Gets the first repetition for <b>target</b> (Types of resource (if a resource reference)),
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
	 * Add a value for <b>target</b> (Types of resource (if a resource reference))
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
	 * Sets the value(s), and clears any existing value(s) for <b>target</b> (Types of resource (if a resource reference))
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
	 * Gets the value(s) for <b>chain</b> (Chained names supported).
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
	 * Sets the value(s) for <b>chain</b> (Chained names supported)
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
	 * Adds and returns a new value for <b>chain</b> (Chained names supported)
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
	 * Gets the first repetition for <b>chain</b> (Chained names supported),
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
	 * Adds a new value for <b>chain</b> (Chained names supported)
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
	 * Block class for child element: <b>Conformance.rest.operation</b> (What operations are supported?)
	 *
     * <p>
     * <b>Definition:</b>
     * A specification of restful operations supported by the system
     * </p> 
	 */
	@Block()	
	public static class RestOperation extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="transaction | search-system | history-system",
		formalDefinition="A coded identifier of the operation, supported by the system"
	)
	private BoundCodeDt<RestfulOperationSystemEnum> myCode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Anything special about operation behavior",
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
	 * Gets the value(s) for <b>code</b> (transaction | search-system | history-system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public BoundCodeDt<RestfulOperationSystemEnum> getCode() {  
		if (myCode == null) {
			myCode = new BoundCodeDt<RestfulOperationSystemEnum>(RestfulOperationSystemEnum.VALUESET_BINDER);
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (transaction | search-system | history-system)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public RestOperation setCode(BoundCodeDt<RestfulOperationSystemEnum> theValue) {
		myCode = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>code</b> (transaction | search-system | history-system)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded identifier of the operation, supported by the system
     * </p> 
	 */
	public RestOperation setCode(RestfulOperationSystemEnum theValue) {
		getCode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (Anything special about operation behavior).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Anything special about operation behavior)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public RestOperation setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>documentation</b> (Anything special about operation behavior)
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or information about system wide search is implemented
     * </p> 
	 */
	public RestOperation setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Conformance.rest.query</b> (Definition of a named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Definition of a named query and its parameters and their meaning
     * </p> 
	 */
	@Block()	
	public static class RestQuery extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=StringDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Special named queries (_query=)",
		formalDefinition="The name of a query, which is used in the _query parameter when the query is called"
	)
	private StringDt myName;
	
	@Child(name="definition", type=UriDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Where query is defined",
		formalDefinition="Identifies the custom query, defined either in FHIR core or another profile"
	)
	private UriDt myDefinition;
	
	@Child(name="documentation", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Additional usage guidance",
		formalDefinition="Additional information about how the query functions in this particular implementation"
	)
	private StringDt myDocumentation;
	
	@Child(name="parameter", type=RestResourceSearchParam.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Parameter for the named query",
		formalDefinition="Identifies which of the parameters for the named query are supported"
	)
	private java.util.List<RestResourceSearchParam> myParameter;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myDefinition,  myDocumentation,  myParameter);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myDefinition, myDocumentation, myParameter);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Special named queries (_query=)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the _query parameter when the query is called
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Special named queries (_query=))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the _query parameter when the query is called
     * </p> 
	 */
	public RestQuery setName(StringDt theValue) {
		myName = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>name</b> (Special named queries (_query=))
	 *
     * <p>
     * <b>Definition:</b>
     * The name of a query, which is used in the _query parameter when the query is called
     * </p> 
	 */
	public RestQuery setName( String theString) {
		myName = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>definition</b> (Where query is defined).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the custom query, defined either in FHIR core or another profile
     * </p> 
	 */
	public UriDt getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new UriDt();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (Where query is defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the custom query, defined either in FHIR core or another profile
     * </p> 
	 */
	public RestQuery setDefinition(UriDt theValue) {
		myDefinition = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>definition</b> (Where query is defined)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the custom query, defined either in FHIR core or another profile
     * </p> 
	 */
	public RestQuery setDefinition( String theUri) {
		myDefinition = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>documentation</b> (Additional usage guidance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how the query functions in this particular implementation
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Additional usage guidance)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how the query functions in this particular implementation
     * </p> 
	 */
	public RestQuery setDocumentation(StringDt theValue) {
		myDocumentation = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>documentation</b> (Additional usage guidance)
	 *
     * <p>
     * <b>Definition:</b>
     * Additional information about how the query functions in this particular implementation
     * </p> 
	 */
	public RestQuery setDocumentation( String theString) {
		myDocumentation = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>parameter</b> (Parameter for the named query).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies which of the parameters for the named query are supported
     * </p> 
	 */
	public java.util.List<RestResourceSearchParam> getParameter() {  
		if (myParameter == null) {
			myParameter = new java.util.ArrayList<RestResourceSearchParam>();
		}
		return myParameter;
	}

	/**
	 * Sets the value(s) for <b>parameter</b> (Parameter for the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies which of the parameters for the named query are supported
     * </p> 
	 */
	public RestQuery setParameter(java.util.List<RestResourceSearchParam> theValue) {
		myParameter = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>parameter</b> (Parameter for the named query)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies which of the parameters for the named query are supported
     * </p> 
	 */
	public RestResourceSearchParam addParameter() {
		RestResourceSearchParam newType = new RestResourceSearchParam();
		getParameter().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>parameter</b> (Parameter for the named query),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies which of the parameters for the named query are supported
     * </p> 
	 */
	public RestResourceSearchParam getParameterFirstRep() {
		if (getParameter().isEmpty()) {
			return addParameter();
		}
		return getParameter().get(0); 
	}
  

	}



	/**
	 * Block class for child element: <b>Conformance.messaging</b> (If messaging is supported)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the messaging capabilities of the solution
     * </p> 
	 */
	@Block()	
	public static class Messaging extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="endpoint", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Actual endpoint being described",
		formalDefinition="An address to which messages and/or replies are to be sent."
	)
	private UriDt myEndpoint;
	
	@Child(name="reliableCache", type=IntegerDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Reliable Message Cache Length",
		formalDefinition="Length if the receiver's reliable messaging cache (if a receiver) or how long the cache length on the receiver should be (if a sender)"
	)
	private IntegerDt myReliableCache;
	
	@Child(name="documentation", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Messaging interface behavior details",
		formalDefinition="Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner."
	)
	private StringDt myDocumentation;
	
	@Child(name="event", order=3, min=1, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Declare support for this event",
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
	 * Gets the value(s) for <b>endpoint</b> (Actual endpoint being described).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An address to which messages and/or replies are to be sent.
     * </p> 
	 */
	public UriDt getEndpoint() {  
		if (myEndpoint == null) {
			myEndpoint = new UriDt();
		}
		return myEndpoint;
	}

	/**
	 * Sets the value(s) for <b>endpoint</b> (Actual endpoint being described)
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
	 * Sets the value for <b>endpoint</b> (Actual endpoint being described)
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
	 * Gets the value(s) for <b>reliableCache</b> (Reliable Message Cache Length).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public IntegerDt getReliableCache() {  
		if (myReliableCache == null) {
			myReliableCache = new IntegerDt();
		}
		return myReliableCache;
	}

	/**
	 * Sets the value(s) for <b>reliableCache</b> (Reliable Message Cache Length)
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public Messaging setReliableCache(IntegerDt theValue) {
		myReliableCache = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>reliableCache</b> (Reliable Message Cache Length)
	 *
     * <p>
     * <b>Definition:</b>
     * Length if the receiver's reliable messaging cache (if a receiver) or how long the cache length on the receiver should be (if a sender)
     * </p> 
	 */
	public Messaging setReliableCache( int theInteger) {
		myReliableCache = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>documentation</b> (Messaging interface behavior details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the conformance statement.  For example, process for becoming an authorized messaging exchange partner.
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Messaging interface behavior details)
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
	 * Sets the value for <b>documentation</b> (Messaging interface behavior details)
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
	 * Gets the value(s) for <b>event</b> (Declare support for this event).
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
	 * Sets the value(s) for <b>event</b> (Declare support for this event)
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
	 * Adds and returns a new value for <b>event</b> (Declare support for this event)
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
	 * Gets the first repetition for <b>event</b> (Declare support for this event),
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
	 * Block class for child element: <b>Conformance.messaging.event</b> (Declare support for this event)
	 *
     * <p>
     * <b>Definition:</b>
     * A description of the solution's support for an event at this end point.
     * </p> 
	 */
	@Block()	
	public static class MessagingEvent extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodingDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Event type",
		formalDefinition="A coded identifier of a supported messaging event"
	)
	private CodingDt myCode;
	
	@Child(name="category", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Consequence | Currency | Notification",
		formalDefinition="The impact of the content of the message"
	)
	private BoundCodeDt<MessageSignificanceCategoryEnum> myCategory;
	
	@Child(name="mode", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="sender | receiver",
		formalDefinition="The mode of this event declaration - whether application is sender or receiver"
	)
	private BoundCodeDt<ConformanceEventModeEnum> myMode;
	
	@Child(name="protocol", type=CodingDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="http | ftp | mllp +",
		formalDefinition="A list of the messaging transport protocol(s) identifiers, supported by this endpoint"
	)
	private java.util.List<CodingDt> myProtocol;
	
	@Child(name="focus", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="Resource that's focus of message",
		formalDefinition="A resource associated with the event.  This is the resource that defines the event."
	)
	private BoundCodeDt<ResourceTypeEnum> myFocus;
	
	@Child(name="request", order=5, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Profile.class	})
	@Description(
		shortDefinition="Profile that describes the request",
		formalDefinition="Information about the request for this event"
	)
	private ResourceReferenceDt myRequest;
	
	@Child(name="response", order=6, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Profile.class	})
	@Description(
		shortDefinition="Profile that describes the response",
		formalDefinition="Information about the response for this event"
	)
	private ResourceReferenceDt myResponse;
	
	@Child(name="documentation", type=StringDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Endpoint-specific event documentation",
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
	 * Gets the value(s) for <b>code</b> (Event type).
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
	 * Sets the value(s) for <b>code</b> (Event type)
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
	 * Gets the value(s) for <b>category</b> (Consequence | Currency | Notification).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The impact of the content of the message
     * </p> 
	 */
	public BoundCodeDt<MessageSignificanceCategoryEnum> getCategory() {  
		if (myCategory == null) {
			myCategory = new BoundCodeDt<MessageSignificanceCategoryEnum>(MessageSignificanceCategoryEnum.VALUESET_BINDER);
		}
		return myCategory;
	}

	/**
	 * Sets the value(s) for <b>category</b> (Consequence | Currency | Notification)
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
	 * Sets the value(s) for <b>category</b> (Consequence | Currency | Notification)
	 *
     * <p>
     * <b>Definition:</b>
     * The impact of the content of the message
     * </p> 
	 */
	public MessagingEvent setCategory(MessageSignificanceCategoryEnum theValue) {
		getCategory().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>mode</b> (sender | receiver).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mode of this event declaration - whether application is sender or receiver
     * </p> 
	 */
	public BoundCodeDt<ConformanceEventModeEnum> getMode() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<ConformanceEventModeEnum>(ConformanceEventModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (sender | receiver)
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
	 * Sets the value(s) for <b>mode</b> (sender | receiver)
	 *
     * <p>
     * <b>Definition:</b>
     * The mode of this event declaration - whether application is sender or receiver
     * </p> 
	 */
	public MessagingEvent setMode(ConformanceEventModeEnum theValue) {
		getMode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>protocol</b> (http | ftp | mllp +).
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
	 * Sets the value(s) for <b>protocol</b> (http | ftp | mllp +)
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
	 * Adds and returns a new value for <b>protocol</b> (http | ftp | mllp +)
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
	 * Gets the first repetition for <b>protocol</b> (http | ftp | mllp +),
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
	 * Gets the value(s) for <b>focus</b> (Resource that's focus of message).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A resource associated with the event.  This is the resource that defines the event.
     * </p> 
	 */
	public BoundCodeDt<ResourceTypeEnum> getFocus() {  
		if (myFocus == null) {
			myFocus = new BoundCodeDt<ResourceTypeEnum>(ResourceTypeEnum.VALUESET_BINDER);
		}
		return myFocus;
	}

	/**
	 * Sets the value(s) for <b>focus</b> (Resource that's focus of message)
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
	 * Sets the value(s) for <b>focus</b> (Resource that's focus of message)
	 *
     * <p>
     * <b>Definition:</b>
     * A resource associated with the event.  This is the resource that defines the event.
     * </p> 
	 */
	public MessagingEvent setFocus(ResourceTypeEnum theValue) {
		getFocus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>request</b> (Profile that describes the request).
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
	 * Sets the value(s) for <b>request</b> (Profile that describes the request)
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
	 * Gets the value(s) for <b>response</b> (Profile that describes the response).
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
	 * Sets the value(s) for <b>response</b> (Profile that describes the response)
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
	 * Gets the value(s) for <b>documentation</b> (Endpoint-specific event documentation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Guidance on how this event is handled, such as internal system trigger points, business rules, etc.
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Endpoint-specific event documentation)
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
	 * Sets the value for <b>documentation</b> (Endpoint-specific event documentation)
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
	 * Block class for child element: <b>Conformance.document</b> (Document definition)
	 *
     * <p>
     * <b>Definition:</b>
     * A document definition
     * </p> 
	 */
	@Block()	
	public static class Document extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="mode", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="producer | consumer",
		formalDefinition="Mode of this document declaration - whether application is producer or consumer"
	)
	private BoundCodeDt<DocumentModeEnum> myMode;
	
	@Child(name="documentation", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Description of document support",
		formalDefinition="A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc."
	)
	private StringDt myDocumentation;
	
	@Child(name="profile", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Profile.class	})
	@Description(
		shortDefinition="Constraint on a resource used in the document",
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
	 * Gets the value(s) for <b>mode</b> (producer | consumer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Mode of this document declaration - whether application is producer or consumer
     * </p> 
	 */
	public BoundCodeDt<DocumentModeEnum> getMode() {  
		if (myMode == null) {
			myMode = new BoundCodeDt<DocumentModeEnum>(DocumentModeEnum.VALUESET_BINDER);
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (producer | consumer)
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
	 * Sets the value(s) for <b>mode</b> (producer | consumer)
	 *
     * <p>
     * <b>Definition:</b>
     * Mode of this document declaration - whether application is producer or consumer
     * </p> 
	 */
	public Document setMode(DocumentModeEnum theValue) {
		getMode().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>documentation</b> (Description of document support).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A description of how the application supports or uses the specified document profile.  For example, when are documents created, what action is taken with consumed documents, etc.
     * </p> 
	 */
	public StringDt getDocumentation() {  
		if (myDocumentation == null) {
			myDocumentation = new StringDt();
		}
		return myDocumentation;
	}

	/**
	 * Sets the value(s) for <b>documentation</b> (Description of document support)
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
	 * Sets the value for <b>documentation</b> (Description of document support)
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
	 * Gets the value(s) for <b>profile</b> (Constraint on a resource used in the document).
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
	 * Sets the value(s) for <b>profile</b> (Constraint on a resource used in the document)
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
		return Conformance.class.getName();
	}




}
