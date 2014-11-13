















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
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.dstu.valueset.FilterOperatorEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.ValueSetStatusEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>ValueSet</b> Resource
 * (A set of codes drawn from one or more code systems)
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
public class ValueSet extends BaseResource implements IResource {

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


	@Child(name="identifier", type=StringDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Logical id to reference this value set",
		formalDefinition="The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)"
	)
	private StringDt myIdentifier;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Logical id for this version of the value set",
		formalDefinition="The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp"
	)
	private StringDt myVersion;
	
	@Child(name="name", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Informal name for this value set",
		formalDefinition="A free text natural language name describing the value set"
	)
	private StringDt myName;
	
	@Child(name="publisher", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Name of the publisher (Organization or individual)",
		formalDefinition="The name of the individual or organization that published the value set"
	)
	private StringDt myPublisher;
	
	@Child(name="telecom", type=ContactDt.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contact information of the publisher",
		formalDefinition="Contacts of the publisher to assist a user in finding and communicating with the publisher"
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="description", type=StringDt.class, order=5, min=1, max=1)	
	@Description(
		shortDefinition="Human language description of the value set",
		formalDefinition="A free text natural language description of the use of the value set - reason for definition, conditions of use, etc."
	)
	private StringDt myDescription;
	
	@Child(name="copyright", type=StringDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="About the value set or its content",
		formalDefinition="A copyright statement relating to the value set and/or its contents"
	)
	private StringDt myCopyright;
	
	@Child(name="status", type=CodeDt.class, order=7, min=1, max=1)	
	@Description(
		shortDefinition="draft | active | retired",
		formalDefinition="The status of the value set"
	)
	private BoundCodeDt<ValueSetStatusEnum> myStatus;
	
	@Child(name="experimental", type=BooleanDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="If for testing purposes, not real usage",
		formalDefinition="This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage"
	)
	private BooleanDt myExperimental;
	
	@Child(name="extensible", type=BooleanDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Whether this is intended to be used with an extensible binding",
		formalDefinition="Whether this is intended to be used with an extensible binding or not"
	)
	private BooleanDt myExtensible;
	
	@Child(name="date", type=DateTimeDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Date for given status",
		formalDefinition="The date that the value set status was last changed"
	)
	private DateTimeDt myDate;
	
	@Child(name="define", order=11, min=0, max=1)	
	@Description(
		shortDefinition="When value set defines its own codes",
		formalDefinition=""
	)
	private Define myDefine;
	
	@Child(name="compose", order=12, min=0, max=1)	
	@Description(
		shortDefinition="When value set includes codes from elsewhere",
		formalDefinition=""
	)
	private Compose myCompose;
	
	@Child(name="expansion", order=13, min=0, max=1)	
	@Description(
		shortDefinition="When value set is an expansion",
		formalDefinition=""
	)
	private Expansion myExpansion;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myVersion,  myName,  myPublisher,  myTelecom,  myDescription,  myCopyright,  myStatus,  myExperimental,  myExtensible,  myDate,  myDefine,  myCompose,  myExpansion);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myVersion, myName, myPublisher, myTelecom, myDescription, myCopyright, myStatus, myExperimental, myExtensible, myDate, myDefine, myCompose, myExpansion);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (Logical id to reference this value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public StringDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new StringDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public ValueSet setIdentifier(StringDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (Logical id to reference this value set)
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this value set when it is referenced in a specification, model, design or an instance (should be globally unique OID, UUID, or URI)
     * </p> 
	 */
	public ValueSet setIdentifier( String theString) {
		myIdentifier = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Logical id for this version of the value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually and the value should be a timestamp
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Logical id for this version of the value set)
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
	 * Sets the value for <b>version</b> (Logical id for this version of the value set)
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
	 * Gets the value(s) for <b>name</b> (Informal name for this value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language name describing the value set
     * </p> 
	 */
	public StringDt getName() {  
		if (myName == null) {
			myName = new StringDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Informal name for this value set)
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
	 * Sets the value for <b>name</b> (Informal name for this value set)
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
	 * Gets the value(s) for <b>publisher</b> (Name of the publisher (Organization or individual)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the individual or organization that published the value set
     * </p> 
	 */
	public StringDt getPublisher() {  
		if (myPublisher == null) {
			myPublisher = new StringDt();
		}
		return myPublisher;
	}

	/**
	 * Sets the value(s) for <b>publisher</b> (Name of the publisher (Organization or individual))
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
	 * Sets the value for <b>publisher</b> (Name of the publisher (Organization or individual))
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
	 * Gets the value(s) for <b>telecom</b> (Contact information of the publisher).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ValueSet setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (Contact information of the publisher),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
	 */
	public ContactDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
 	/**
	 * Adds a new value for <b>telecom</b> (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ValueSet addTelecom( ContactUseEnum theContactUse,  String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theContactUse, theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>telecom</b> (Contact information of the publisher)
	 *
     * <p>
     * <b>Definition:</b>
     * Contacts of the publisher to assist a user in finding and communicating with the publisher
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ValueSet addTelecom( String theValue) {
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		myTelecom.add(new ContactDt(theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>description</b> (Human language description of the value set).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public StringDt getDescription() {  
		if (myDescription == null) {
			myDescription = new StringDt();
		}
		return myDescription;
	}

	/**
	 * Sets the value(s) for <b>description</b> (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public ValueSet setDescription(StringDt theValue) {
		myDescription = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>description</b> (Human language description of the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * A free text natural language description of the use of the value set - reason for definition, conditions of use, etc.
     * </p> 
	 */
	public ValueSet setDescription( String theString) {
		myDescription = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>copyright</b> (About the value set or its content).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public StringDt getCopyright() {  
		if (myCopyright == null) {
			myCopyright = new StringDt();
		}
		return myCopyright;
	}

	/**
	 * Sets the value(s) for <b>copyright</b> (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public ValueSet setCopyright(StringDt theValue) {
		myCopyright = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>copyright</b> (About the value set or its content)
	 *
     * <p>
     * <b>Definition:</b>
     * A copyright statement relating to the value set and/or its contents
     * </p> 
	 */
	public ValueSet setCopyright( String theString) {
		myCopyright = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (draft | active | retired).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public BoundCodeDt<ValueSetStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<ValueSetStatusEnum>(ValueSetStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
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
	 * Sets the value(s) for <b>status</b> (draft | active | retired)
	 *
     * <p>
     * <b>Definition:</b>
     * The status of the value set
     * </p> 
	 */
	public ValueSet setStatus(ValueSetStatusEnum theValue) {
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
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
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
     * This valueset was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage
     * </p> 
	 */
	public ValueSet setExperimental(BooleanDt theValue) {
		myExperimental = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>experimental</b> (If for testing purposes, not real usage)
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
	 * Gets the value(s) for <b>extensible</b> (Whether this is intended to be used with an extensible binding).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this is intended to be used with an extensible binding or not
     * </p> 
	 */
	public BooleanDt getExtensible() {  
		if (myExtensible == null) {
			myExtensible = new BooleanDt();
		}
		return myExtensible;
	}

	/**
	 * Sets the value(s) for <b>extensible</b> (Whether this is intended to be used with an extensible binding)
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
	 * Sets the value for <b>extensible</b> (Whether this is intended to be used with an extensible binding)
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
	 * Gets the value(s) for <b>date</b> (Date for given status).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date that the value set status was last changed
     * </p> 
	 */
	public DateTimeDt getDate() {  
		if (myDate == null) {
			myDate = new DateTimeDt();
		}
		return myDate;
	}

	/**
	 * Sets the value(s) for <b>date</b> (Date for given status)
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
	 * Sets the value for <b>date</b> (Date for given status)
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
	 * Sets the value for <b>date</b> (Date for given status)
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
	 * Gets the value(s) for <b>define</b> (When value set defines its own codes).
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
	 * Sets the value(s) for <b>define</b> (When value set defines its own codes)
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
	 * Gets the value(s) for <b>compose</b> (When value set includes codes from elsewhere).
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
	 * Sets the value(s) for <b>compose</b> (When value set includes codes from elsewhere)
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
	 * Gets the value(s) for <b>expansion</b> (When value set is an expansion).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Expansion getExpansion() {  
		if (myExpansion == null) {
			myExpansion = new Expansion();
		}
		return myExpansion;
	}

	/**
	 * Sets the value(s) for <b>expansion</b> (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ValueSet setExpansion(Expansion theValue) {
		myExpansion = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>ValueSet.define</b> (When value set defines its own codes)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Define extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="URI to identify the code system",
		formalDefinition=""
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Version of this system",
		formalDefinition="The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked"
	)
	private StringDt myVersion;
	
	@Child(name="caseSensitive", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="If code comparison is case sensitive",
		formalDefinition="If code comparison is case sensitive when codes within this system are compared to each other"
	)
	private BooleanDt myCaseSensitive;
	
	@Child(name="concept", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Concepts in the code system",
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
	 * Gets the value(s) for <b>system</b> (URI to identify the code system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getSystem() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (URI to identify the code system)
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
	 * Sets the value for <b>system</b> (URI to identify the code system)
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
	 * Gets the value(s) for <b>version</b> (Version of this system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of this code system that defines the codes. Note that the version is optional because a well maintained code system does not suffer from versioning, and therefore the version does not need to be maintained. However many code systems are not well maintained, and the version needs to be defined and tracked
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Version of this system)
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
	 * Sets the value for <b>version</b> (Version of this system)
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
	 * Gets the value(s) for <b>caseSensitive</b> (If code comparison is case sensitive).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If code comparison is case sensitive when codes within this system are compared to each other
     * </p> 
	 */
	public BooleanDt getCaseSensitive() {  
		if (myCaseSensitive == null) {
			myCaseSensitive = new BooleanDt();
		}
		return myCaseSensitive;
	}

	/**
	 * Sets the value(s) for <b>caseSensitive</b> (If code comparison is case sensitive)
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
	 * Sets the value for <b>caseSensitive</b> (If code comparison is case sensitive)
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
	 * Gets the value(s) for <b>concept</b> (Concepts in the code system).
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
	 * Sets the value(s) for <b>concept</b> (Concepts in the code system)
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
	 * Adds and returns a new value for <b>concept</b> (Concepts in the code system)
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
	 * Gets the first repetition for <b>concept</b> (Concepts in the code system),
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
	 * Block class for child element: <b>ValueSet.define.concept</b> (Concepts in the code system)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class DefineConcept extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="code", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Code that identifies concept",
		formalDefinition=""
	)
	private CodeDt myCode;
	
	@Child(name="abstract", type=BooleanDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="If this code is not for use as a real concept",
		formalDefinition="If this code is not for use as a real concept"
	)
	private BooleanDt myAbstract;
	
	@Child(name="display", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Text to Display to the user",
		formalDefinition=""
	)
	private StringDt myDisplay;
	
	@Child(name="definition", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Formal Definition",
		formalDefinition="The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept"
	)
	private StringDt myDefinition;
	
	@Child(name="concept", type=DefineConcept.class, order=4, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Child Concepts (is-a / contains)",
		formalDefinition=""
	)
	private java.util.List<DefineConcept> myConcept;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myCode,  myAbstract,  myDisplay,  myDefinition,  myConcept);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myCode, myAbstract, myDisplay, myDefinition, myConcept);
	}

	/**
	 * Gets the value(s) for <b>code</b> (Code that identifies concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getCode() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Code that identifies concept)
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
	 * Sets the value for <b>code</b> (Code that identifies concept)
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
	 * Gets the value(s) for <b>abstract</b> (If this code is not for use as a real concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If this code is not for use as a real concept
     * </p> 
	 */
	public BooleanDt getAbstract() {  
		if (myAbstract == null) {
			myAbstract = new BooleanDt();
		}
		return myAbstract;
	}

	/**
	 * Sets the value(s) for <b>abstract</b> (If this code is not for use as a real concept)
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
	 * Sets the value for <b>abstract</b> (If this code is not for use as a real concept)
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
	 * Gets the value(s) for <b>display</b> (Text to Display to the user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (Text to Display to the user)
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
	 * Sets the value for <b>display</b> (Text to Display to the user)
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
	 * Gets the value(s) for <b>definition</b> (Formal Definition).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The formal definition of the concept. Formal definitions are not required, because of the prevalence of legacy systems without them, but they are highly recommended, as without them there is no formal meaning associated with the concept
     * </p> 
	 */
	public StringDt getDefinition() {  
		if (myDefinition == null) {
			myDefinition = new StringDt();
		}
		return myDefinition;
	}

	/**
	 * Sets the value(s) for <b>definition</b> (Formal Definition)
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
	 * Sets the value for <b>definition</b> (Formal Definition)
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
	 * Gets the value(s) for <b>concept</b> (Child Concepts (is-a / contains)).
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
	 * Sets the value(s) for <b>concept</b> (Child Concepts (is-a / contains))
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
	 * Adds and returns a new value for <b>concept</b> (Child Concepts (is-a / contains))
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
	 * Gets the first repetition for <b>concept</b> (Child Concepts (is-a / contains)),
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
	 * Block class for child element: <b>ValueSet.compose</b> (When value set includes codes from elsewhere)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Compose extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="import", type=UriDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Import the contents of another value set",
		formalDefinition="Includes the contents of the referenced value set as a part of the contents of this value set"
	)
	private java.util.List<UriDt> myImport;
	
	@Child(name="include", order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Include one or more codes from a code system",
		formalDefinition="Include one or more codes from a code system"
	)
	private java.util.List<ComposeInclude> myInclude;
	
	@Child(name="exclude", type=ComposeInclude.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Explicitly exclude codes",
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
	 * Gets the value(s) for <b>import</b> (Import the contents of another value set).
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
	 * Sets the value(s) for <b>import</b> (Import the contents of another value set)
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
	 * Adds and returns a new value for <b>import</b> (Import the contents of another value set)
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
	 * Gets the first repetition for <b>import</b> (Import the contents of another value set),
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
	 * Adds a new value for <b>import</b> (Import the contents of another value set)
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
	 * Gets the value(s) for <b>include</b> (Include one or more codes from a code system).
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
	 * Sets the value(s) for <b>include</b> (Include one or more codes from a code system)
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
	 * Adds and returns a new value for <b>include</b> (Include one or more codes from a code system)
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
	 * Gets the first repetition for <b>include</b> (Include one or more codes from a code system),
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
	 * Gets the value(s) for <b>exclude</b> (Explicitly exclude codes).
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
	 * Sets the value(s) for <b>exclude</b> (Explicitly exclude codes)
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
	 * Adds and returns a new value for <b>exclude</b> (Explicitly exclude codes)
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
	 * Gets the first repetition for <b>exclude</b> (Explicitly exclude codes),
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
	 * Block class for child element: <b>ValueSet.compose.include</b> (Include one or more codes from a code system)
	 *
     * <p>
     * <b>Definition:</b>
     * Include one or more codes from a code system
     * </p> 
	 */
	@Block()	
	public static class ComposeInclude extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="The system the codes come from",
		formalDefinition="The code system from which the selected codes come from"
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Specific version of the code system referred to",
		formalDefinition="The version of the code system that the codes are selected from"
	)
	private StringDt myVersion;
	
	@Child(name="code", type=CodeDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Code or concept from system",
		formalDefinition="Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance"
	)
	private java.util.List<CodeDt> myCode;
	
	@Child(name="filter", order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Select codes/concepts by their properties (including relationships)",
		formalDefinition="Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true."
	)
	private java.util.List<ComposeIncludeFilter> myFilter;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myVersion,  myCode,  myFilter);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myVersion, myCode, myFilter);
	}

	/**
	 * Gets the value(s) for <b>system</b> (The system the codes come from).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code system from which the selected codes come from
     * </p> 
	 */
	public UriDt getSystem() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (The system the codes come from)
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
	 * Sets the value for <b>system</b> (The system the codes come from)
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
	 * Gets the value(s) for <b>version</b> (Specific version of the code system referred to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system that the codes are selected from
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Specific version of the code system referred to)
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
	 * Sets the value for <b>version</b> (Specific version of the code system referred to)
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
	 * Gets the value(s) for <b>code</b> (Code or concept from system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance
     * </p> 
	 */
	public java.util.List<CodeDt> getCode() {  
		if (myCode == null) {
			myCode = new java.util.ArrayList<CodeDt>();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Code or concept from system)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance
     * </p> 
	 */
	public ComposeInclude setCode(java.util.List<CodeDt> theValue) {
		myCode = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>code</b> (Code or concept from system)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance
     * </p> 
	 */
	public CodeDt addCode() {
		CodeDt newType = new CodeDt();
		getCode().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>code</b> (Code or concept from system),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance
     * </p> 
	 */
	public CodeDt getCodeFirstRep() {
		if (getCode().isEmpty()) {
			return addCode();
		}
		return getCode().get(0); 
	}
 	/**
	 * Adds a new value for <b>code</b> (Code or concept from system)
	 *
     * <p>
     * <b>Definition:</b>
     * Specifies a code or concept to be included or excluded. The list of codes is considered ordered, though the order may not have any particular significance
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public ComposeInclude addCode( String theCode) {
		if (myCode == null) {
			myCode = new java.util.ArrayList<CodeDt>();
		}
		myCode.add(new CodeDt(theCode));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>filter</b> (Select codes/concepts by their properties (including relationships)).
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
	 * Sets the value(s) for <b>filter</b> (Select codes/concepts by their properties (including relationships))
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
	 * Adds and returns a new value for <b>filter</b> (Select codes/concepts by their properties (including relationships))
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
	 * Gets the first repetition for <b>filter</b> (Select codes/concepts by their properties (including relationships)),
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
	 * Block class for child element: <b>ValueSet.compose.include.filter</b> (Select codes/concepts by their properties (including relationships))
	 *
     * <p>
     * <b>Definition:</b>
     * Select concepts by specify a matching criteria based on the properties (including relationships) defined by the system. If multiple filters are specified, they SHALL all be true.
     * </p> 
	 */
	@Block()	
	public static class ComposeIncludeFilter extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="property", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="",
		formalDefinition="A code that identifies a property defined in the code system"
	)
	private CodeDt myProperty;
	
	@Child(name="op", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="= | is-a | is-not-a | regex | in | not in",
		formalDefinition="The kind of operation to perform as a part of the filter criteria"
	)
	private BoundCodeDt<FilterOperatorEnum> myOp;
	
	@Child(name="value", type=CodeDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Code from the system, or regex criteria",
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
	 * Gets the value(s) for <b>property</b> ().
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that identifies a property defined in the code system
     * </p> 
	 */
	public CodeDt getProperty() {  
		if (myProperty == null) {
			myProperty = new CodeDt();
		}
		return myProperty;
	}

	/**
	 * Sets the value(s) for <b>property</b> ()
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
	 * Sets the value for <b>property</b> ()
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
	 * Gets the value(s) for <b>op</b> (= | is-a | is-not-a | regex | in | not in).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public BoundCodeDt<FilterOperatorEnum> getOp() {  
		if (myOp == null) {
			myOp = new BoundCodeDt<FilterOperatorEnum>(FilterOperatorEnum.VALUESET_BINDER);
		}
		return myOp;
	}

	/**
	 * Sets the value(s) for <b>op</b> (= | is-a | is-not-a | regex | in | not in)
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
	 * Sets the value(s) for <b>op</b> (= | is-a | is-not-a | regex | in | not in)
	 *
     * <p>
     * <b>Definition:</b>
     * The kind of operation to perform as a part of the filter criteria
     * </p> 
	 */
	public ComposeIncludeFilter setOp(FilterOperatorEnum theValue) {
		getOp().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>value</b> (Code from the system, or regex criteria).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The match value may be either a code defined by the system, or a string value which is used a regex match on the literal string of the property value
     * </p> 
	 */
	public CodeDt getValue() {  
		if (myValue == null) {
			myValue = new CodeDt();
		}
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value</b> (Code from the system, or regex criteria)
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
	 * Sets the value for <b>value</b> (Code from the system, or regex criteria)
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
	 * Block class for child element: <b>ValueSet.expansion</b> (When value set is an expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Expansion extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Uniquely identifies this expansion",
		formalDefinition="An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so"
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="timestamp", type=InstantDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Time valueset expansion happened",
		formalDefinition=""
	)
	private InstantDt myTimestamp;
	
	@Child(name="contains", order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Codes in the value set",
		formalDefinition=""
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
	 * Gets the value(s) for <b>identifier</b> (Uniquely identifies this expansion).
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
	 * Sets the value(s) for <b>identifier</b> (Uniquely identifies this expansion)
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
	 * Sets the value for <b>identifier</b> (Uniquely identifies this expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public Expansion setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (Uniquely identifies this expansion)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that uniquely identifies this expansion of the valueset. Systems may re-use the same identifier as long as the expansion and the definition remain the same, but are not required to do so
     * </p> 
	 */
	public Expansion setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>timestamp</b> (Time valueset expansion happened).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public InstantDt getTimestamp() {  
		if (myTimestamp == null) {
			myTimestamp = new InstantDt();
		}
		return myTimestamp;
	}

	/**
	 * Sets the value(s) for <b>timestamp</b> (Time valueset expansion happened)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Expansion setTimestamp(InstantDt theValue) {
		myTimestamp = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>timestamp</b> (Time valueset expansion happened)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Expansion setTimestamp( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myTimestamp = new InstantDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>timestamp</b> (Time valueset expansion happened)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Expansion setTimestampWithMillisPrecision( Date theDate) {
		myTimestamp = new InstantDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contains</b> (Codes in the value set).
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
	 * Sets the value(s) for <b>contains</b> (Codes in the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Expansion setContains(java.util.List<ExpansionContains> theValue) {
		myContains = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>contains</b> (Codes in the value set)
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
	 * Gets the first repetition for <b>contains</b> (Codes in the value set),
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

	/**
	 * Block class for child element: <b>ValueSet.expansion.contains</b> (Codes in the value set)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class ExpansionContains extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="system", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="System value for the code",
		formalDefinition=""
	)
	private UriDt mySystem;
	
	@Child(name="code", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Code - if blank, this is not a choosable code",
		formalDefinition=""
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="User display for the concept",
		formalDefinition=""
	)
	private StringDt myDisplay;
	
	@Child(name="contains", type=ExpansionContains.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Codes contained in this concept",
		formalDefinition=""
	)
	private java.util.List<ExpansionContains> myContains;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myCode,  myDisplay,  myContains);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySystem, myCode, myDisplay, myContains);
	}

	/**
	 * Gets the value(s) for <b>system</b> (System value for the code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getSystem() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (System value for the code)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExpansionContains setSystem(UriDt theValue) {
		mySystem = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>system</b> (System value for the code)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ExpansionContains setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Code - if blank, this is not a choosable code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getCode() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Code - if blank, this is not a choosable code)
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
	 * Sets the value for <b>code</b> (Code - if blank, this is not a choosable code)
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
	 * Gets the value(s) for <b>display</b> (User display for the concept).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (User display for the concept)
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
	 * Sets the value for <b>display</b> (User display for the concept)
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
	 * Gets the value(s) for <b>contains</b> (Codes contained in this concept).
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
	 * Sets the value(s) for <b>contains</b> (Codes contained in this concept)
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
	 * Adds and returns a new value for <b>contains</b> (Codes contained in this concept)
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
	 * Gets the first repetition for <b>contains</b> (Codes contained in this concept),
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
        return ValueSet.class.getName();
    }

}