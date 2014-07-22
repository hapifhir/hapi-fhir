















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
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.ProvenanceEntityRoleEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>Provenance</b> Resource
 * (Who, What, When for a set of resources)
 *
 * <p>
 * <b>Definition:</b>
 * Provenance information that describes the activity that led to the creation of a set of resources. This information can be used to help determine their reliability or trace where the information in them came from. The focus of the provenance resource is record keeping, audit and traceability, and not explicit statements of clinical significance
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Provenance">http://hl7.org/fhir/profiles/Provenance</a> 
 * </p>
 *
 */
@ResourceDef(name="Provenance", profile="http://hl7.org/fhir/profiles/Provenance", id="provenance")
public class Provenance extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Provenance.target</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="target", path="Provenance.target", description="", type="reference")
	public static final String SP_TARGET = "target";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>target</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Provenance.target</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam TARGET = new ReferenceClientParam(SP_TARGET);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Provenance.target</b>".
	 */
	public static final Include INCLUDE_TARGET = new Include("Provenance.target");

	/**
	 * Search parameter constant for <b>start</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Provenance.period.start</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="start", path="Provenance.period.start", description="", type="date")
	public static final String SP_START = "start";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>start</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Provenance.period.start</b><br/>
	 * </p>
	 */
	public static final DateClientParam START = new DateClientParam(SP_START);

	/**
	 * Search parameter constant for <b>end</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Provenance.period.end</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="end", path="Provenance.period.end", description="", type="date")
	public static final String SP_END = "end";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>end</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Provenance.period.end</b><br/>
	 * </p>
	 */
	public static final DateClientParam END = new DateClientParam(SP_END);

	/**
	 * Search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Provenance.location</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="location", path="Provenance.location", description="", type="reference")
	public static final String SP_LOCATION = "location";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>location</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Provenance.location</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam LOCATION = new ReferenceClientParam(SP_LOCATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Provenance.location</b>".
	 */
	public static final Include INCLUDE_LOCATION = new Include("Provenance.location");

	/**
	 * Search parameter constant for <b>party</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Provenance.agent.reference</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="party", path="Provenance.agent.reference", description="", type="token")
	public static final String SP_PARTY = "party";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>party</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Provenance.agent.reference</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PARTY = new TokenClientParam(SP_PARTY);

	/**
	 * Search parameter constant for <b>partytype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Provenance.agent.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="partytype", path="Provenance.agent.type", description="", type="token")
	public static final String SP_PARTYTYPE = "partytype";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>partytype</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Provenance.agent.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PARTYTYPE = new TokenClientParam(SP_PARTYTYPE);


	@Child(name="target", order=0, min=1, max=Child.MAX_UNLIMITED, type={
		IResource.class	})
	@Description(
		shortDefinition="Target resource(s) (usually version specific)",
		formalDefinition="The resource(s) that were generated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity"
	)
	private java.util.List<ResourceReferenceDt> myTarget;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="When the activity occurred",
		formalDefinition="The period during which the activity occurred"
	)
	private PeriodDt myPeriod;
	
	@Child(name="recorded", type=InstantDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="When the activity was recorded / updated",
		formalDefinition="The instant of time at which the activity was recorded"
	)
	private InstantDt myRecorded;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Reason the activity is occurring",
		formalDefinition="The reason that the activity was taking place"
	)
	private CodeableConceptDt myReason;
	
	@Child(name="location", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Where the activity occurred, if relevant",
		formalDefinition="Where the activity occurred, if relevant"
	)
	private ResourceReferenceDt myLocation;
	
	@Child(name="policy", type=UriDt.class, order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Policy or plan the activity was defined by",
		formalDefinition="Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc."
	)
	private java.util.List<UriDt> myPolicy;
	
	@Child(name="agent", order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Person, organization, records, etc. involved in creating resource",
		formalDefinition="An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility"
	)
	private java.util.List<Agent> myAgent;
	
	@Child(name="entity", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="An entity used in this activity",
		formalDefinition="An entity used in this activity"
	)
	private java.util.List<Entity> myEntity;
	
	@Child(name="integritySignature", type=StringDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Base64 signature (DigSig) - integrity check",
		formalDefinition="A digital signature on the target resource(s). The signature should match a Provenance.agent.reference in the provenance resource. The signature is only added to support checking cryptographic integrity of the resource, and not to represent workflow and clinical aspects of the signing process, or to support non-repudiation."
	)
	private StringDt myIntegritySignature;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myTarget,  myPeriod,  myRecorded,  myReason,  myLocation,  myPolicy,  myAgent,  myEntity,  myIntegritySignature);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myTarget, myPeriod, myRecorded, myReason, myLocation, myPolicy, myAgent, myEntity, myIntegritySignature);
	}

	/**
	 * Gets the value(s) for <b>target</b> (Target resource(s) (usually version specific)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The resource(s) that were generated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getTarget() {  
		if (myTarget == null) {
			myTarget = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myTarget;
	}

	/**
	 * Sets the value(s) for <b>target</b> (Target resource(s) (usually version specific))
	 *
     * <p>
     * <b>Definition:</b>
     * The resource(s) that were generated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity
     * </p> 
	 */
	public Provenance setTarget(java.util.List<ResourceReferenceDt> theValue) {
		myTarget = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>target</b> (Target resource(s) (usually version specific))
	 *
     * <p>
     * <b>Definition:</b>
     * The resource(s) that were generated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity
     * </p> 
	 */
	public ResourceReferenceDt addTarget() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getTarget().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>period</b> (When the activity occurred).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the activity occurred
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (When the activity occurred)
	 *
     * <p>
     * <b>Definition:</b>
     * The period during which the activity occurred
     * </p> 
	 */
	public Provenance setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>recorded</b> (When the activity was recorded / updated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The instant of time at which the activity was recorded
     * </p> 
	 */
	public InstantDt getRecorded() {  
		if (myRecorded == null) {
			myRecorded = new InstantDt();
		}
		return myRecorded;
	}

	/**
	 * Sets the value(s) for <b>recorded</b> (When the activity was recorded / updated)
	 *
     * <p>
     * <b>Definition:</b>
     * The instant of time at which the activity was recorded
     * </p> 
	 */
	public Provenance setRecorded(InstantDt theValue) {
		myRecorded = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>recorded</b> (When the activity was recorded / updated)
	 *
     * <p>
     * <b>Definition:</b>
     * The instant of time at which the activity was recorded
     * </p> 
	 */
	public Provenance setRecorded( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myRecorded = new InstantDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>recorded</b> (When the activity was recorded / updated)
	 *
     * <p>
     * <b>Definition:</b>
     * The instant of time at which the activity was recorded
     * </p> 
	 */
	public Provenance setRecordedWithMillisPrecision( Date theDate) {
		myRecorded = new InstantDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reason</b> (Reason the activity is occurring).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The reason that the activity was taking place
     * </p> 
	 */
	public CodeableConceptDt getReason() {  
		if (myReason == null) {
			myReason = new CodeableConceptDt();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (Reason the activity is occurring)
	 *
     * <p>
     * <b>Definition:</b>
     * The reason that the activity was taking place
     * </p> 
	 */
	public Provenance setReason(CodeableConceptDt theValue) {
		myReason = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>location</b> (Where the activity occurred, if relevant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Where the activity occurred, if relevant
     * </p> 
	 */
	public ResourceReferenceDt getLocation() {  
		if (myLocation == null) {
			myLocation = new ResourceReferenceDt();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (Where the activity occurred, if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * Where the activity occurred, if relevant
     * </p> 
	 */
	public Provenance setLocation(ResourceReferenceDt theValue) {
		myLocation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>policy</b> (Policy or plan the activity was defined by).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
	 */
	public java.util.List<UriDt> getPolicy() {  
		if (myPolicy == null) {
			myPolicy = new java.util.ArrayList<UriDt>();
		}
		return myPolicy;
	}

	/**
	 * Sets the value(s) for <b>policy</b> (Policy or plan the activity was defined by)
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
	 */
	public Provenance setPolicy(java.util.List<UriDt> theValue) {
		myPolicy = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>policy</b> (Policy or plan the activity was defined by)
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
	 */
	public UriDt addPolicy() {
		UriDt newType = new UriDt();
		getPolicy().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>policy</b> (Policy or plan the activity was defined by),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
	 */
	public UriDt getPolicyFirstRep() {
		if (getPolicy().isEmpty()) {
			return addPolicy();
		}
		return getPolicy().get(0); 
	}
 	/**
	 * Adds a new value for <b>policy</b> (Policy or plan the activity was defined by)
	 *
     * <p>
     * <b>Definition:</b>
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Provenance addPolicy( String theUri) {
		if (myPolicy == null) {
			myPolicy = new java.util.ArrayList<UriDt>();
		}
		myPolicy.add(new UriDt(theUri));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>agent</b> (Person, organization, records, etc. involved in creating resource).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	public java.util.List<Agent> getAgent() {  
		if (myAgent == null) {
			myAgent = new java.util.ArrayList<Agent>();
		}
		return myAgent;
	}

	/**
	 * Sets the value(s) for <b>agent</b> (Person, organization, records, etc. involved in creating resource)
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	public Provenance setAgent(java.util.List<Agent> theValue) {
		myAgent = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>agent</b> (Person, organization, records, etc. involved in creating resource)
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	public Agent addAgent() {
		Agent newType = new Agent();
		getAgent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>agent</b> (Person, organization, records, etc. involved in creating resource),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	public Agent getAgentFirstRep() {
		if (getAgent().isEmpty()) {
			return addAgent();
		}
		return getAgent().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>entity</b> (An entity used in this activity).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	public java.util.List<Entity> getEntity() {  
		if (myEntity == null) {
			myEntity = new java.util.ArrayList<Entity>();
		}
		return myEntity;
	}

	/**
	 * Sets the value(s) for <b>entity</b> (An entity used in this activity)
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	public Provenance setEntity(java.util.List<Entity> theValue) {
		myEntity = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>entity</b> (An entity used in this activity)
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	public Entity addEntity() {
		Entity newType = new Entity();
		getEntity().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>entity</b> (An entity used in this activity),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	public Entity getEntityFirstRep() {
		if (getEntity().isEmpty()) {
			return addEntity();
		}
		return getEntity().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>integritySignature</b> (Base64 signature (DigSig) - integrity check).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A digital signature on the target resource(s). The signature should match a Provenance.agent.reference in the provenance resource. The signature is only added to support checking cryptographic integrity of the resource, and not to represent workflow and clinical aspects of the signing process, or to support non-repudiation.
     * </p> 
	 */
	public StringDt getIntegritySignature() {  
		if (myIntegritySignature == null) {
			myIntegritySignature = new StringDt();
		}
		return myIntegritySignature;
	}

	/**
	 * Sets the value(s) for <b>integritySignature</b> (Base64 signature (DigSig) - integrity check)
	 *
     * <p>
     * <b>Definition:</b>
     * A digital signature on the target resource(s). The signature should match a Provenance.agent.reference in the provenance resource. The signature is only added to support checking cryptographic integrity of the resource, and not to represent workflow and clinical aspects of the signing process, or to support non-repudiation.
     * </p> 
	 */
	public Provenance setIntegritySignature(StringDt theValue) {
		myIntegritySignature = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>integritySignature</b> (Base64 signature (DigSig) - integrity check)
	 *
     * <p>
     * <b>Definition:</b>
     * A digital signature on the target resource(s). The signature should match a Provenance.agent.reference in the provenance resource. The signature is only added to support checking cryptographic integrity of the resource, and not to represent workflow and clinical aspects of the signing process, or to support non-repudiation.
     * </p> 
	 */
	public Provenance setIntegritySignature( String theString) {
		myIntegritySignature = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Provenance.agent</b> (Person, organization, records, etc. involved in creating resource)
	 *
     * <p>
     * <b>Definition:</b>
     * An agent takes a role in an activity such that the agent can be assigned some degree of responsibility for the activity taking place. An agent can be a person, a piece of software, an inanimate object, an organization, or other entities that may be ascribed responsibility
     * </p> 
	 */
	@Block()	
	public static class Agent extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="role", type=CodingDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="e.g. author | overseer | enterer | attester | source | cc: +",
		formalDefinition="The role that the participant played"
	)
	private CodingDt myRole;
	
	@Child(name="type", type=CodingDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="e.g. Resource | Person | Application | Record | Document +",
		formalDefinition="The type of the participant"
	)
	private CodingDt myType;
	
	@Child(name="reference", type=UriDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Identity of agent (urn or url)",
		formalDefinition="Identity of participant. May be a logical or physical uri and maybe absolute or relative"
	)
	private UriDt myReference;
	
	@Child(name="display", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Human description of participant",
		formalDefinition="Human-readable description of the participant"
	)
	private StringDt myDisplay;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRole,  myType,  myReference,  myDisplay);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRole, myType, myReference, myDisplay);
	}

	/**
	 * Gets the value(s) for <b>role</b> (e.g. author | overseer | enterer | attester | source | cc: +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The role that the participant played
     * </p> 
	 */
	public CodingDt getRole() {  
		if (myRole == null) {
			myRole = new CodingDt();
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (e.g. author | overseer | enterer | attester | source | cc: +)
	 *
     * <p>
     * <b>Definition:</b>
     * The role that the participant played
     * </p> 
	 */
	public Agent setRole(CodingDt theValue) {
		myRole = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (e.g. Resource | Person | Application | Record | Document +).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the participant
     * </p> 
	 */
	public CodingDt getType() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (e.g. Resource | Person | Application | Record | Document +)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the participant
     * </p> 
	 */
	public Agent setType(CodingDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reference</b> (Identity of agent (urn or url)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public UriDt getReference() {  
		if (myReference == null) {
			myReference = new UriDt();
		}
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference</b> (Identity of agent (urn or url))
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public Agent setReference(UriDt theValue) {
		myReference = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>reference</b> (Identity of agent (urn or url))
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public Agent setReference( String theUri) {
		myReference = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (Human description of participant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the participant
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (Human description of participant)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the participant
     * </p> 
	 */
	public Agent setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>display</b> (Human description of participant)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the participant
     * </p> 
	 */
	public Agent setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 

	}


	/**
	 * Block class for child element: <b>Provenance.entity</b> (An entity used in this activity)
	 *
     * <p>
     * <b>Definition:</b>
     * An entity used in this activity
     * </p> 
	 */
	@Block()	
	public static class Entity extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="role", type=CodeDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="derivation | revision | quotation | source",
		formalDefinition="How the entity was used during the activity"
	)
	private BoundCodeDt<ProvenanceEntityRoleEnum> myRole;
	
	@Child(name="type", type=CodingDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Resource Type, or something else",
		formalDefinition="The type of the entity. If the entity is a resource, then this is a resource type"
	)
	private CodingDt myType;
	
	@Child(name="reference", type=UriDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Identity of participant (urn or url)",
		formalDefinition="Identity of participant. May be a logical or physical uri and maybe absolute or relative"
	)
	private UriDt myReference;
	
	@Child(name="display", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Human description of participant",
		formalDefinition="Human-readable description of the entity"
	)
	private StringDt myDisplay;
	
	@Child(name="agent", type=Agent.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Entity is attributed to this agent",
		formalDefinition="The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity"
	)
	private Agent myAgent;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRole,  myType,  myReference,  myDisplay,  myAgent);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRole, myType, myReference, myDisplay, myAgent);
	}

	/**
	 * Gets the value(s) for <b>role</b> (derivation | revision | quotation | source).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the entity was used during the activity
     * </p> 
	 */
	public BoundCodeDt<ProvenanceEntityRoleEnum> getRole() {  
		if (myRole == null) {
			myRole = new BoundCodeDt<ProvenanceEntityRoleEnum>(ProvenanceEntityRoleEnum.VALUESET_BINDER);
		}
		return myRole;
	}

	/**
	 * Sets the value(s) for <b>role</b> (derivation | revision | quotation | source)
	 *
     * <p>
     * <b>Definition:</b>
     * How the entity was used during the activity
     * </p> 
	 */
	public Entity setRole(BoundCodeDt<ProvenanceEntityRoleEnum> theValue) {
		myRole = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>role</b> (derivation | revision | quotation | source)
	 *
     * <p>
     * <b>Definition:</b>
     * How the entity was used during the activity
     * </p> 
	 */
	public Entity setRole(ProvenanceEntityRoleEnum theValue) {
		getRole().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Resource Type, or something else).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the entity. If the entity is a resource, then this is a resource type
     * </p> 
	 */
	public CodingDt getType() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Resource Type, or something else)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of the entity. If the entity is a resource, then this is a resource type
     * </p> 
	 */
	public Entity setType(CodingDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reference</b> (Identity of participant (urn or url)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public UriDt getReference() {  
		if (myReference == null) {
			myReference = new UriDt();
		}
		return myReference;
	}

	/**
	 * Sets the value(s) for <b>reference</b> (Identity of participant (urn or url))
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public Entity setReference(UriDt theValue) {
		myReference = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>reference</b> (Identity of participant (urn or url))
	 *
     * <p>
     * <b>Definition:</b>
     * Identity of participant. May be a logical or physical uri and maybe absolute or relative
     * </p> 
	 */
	public Entity setReference( String theUri) {
		myReference = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (Human description of participant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the entity
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (Human description of participant)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the entity
     * </p> 
	 */
	public Entity setDisplay(StringDt theValue) {
		myDisplay = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>display</b> (Human description of participant)
	 *
     * <p>
     * <b>Definition:</b>
     * Human-readable description of the entity
     * </p> 
	 */
	public Entity setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>agent</b> (Entity is attributed to this agent).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity
     * </p> 
	 */
	public Agent getAgent() {  
		if (myAgent == null) {
			myAgent = new Agent();
		}
		return myAgent;
	}

	/**
	 * Sets the value(s) for <b>agent</b> (Entity is attributed to this agent)
	 *
     * <p>
     * <b>Definition:</b>
     * The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity
     * </p> 
	 */
	public Entity setAgent(Agent theValue) {
		myAgent = theValue;
		return this;
	}

  

	}




}
