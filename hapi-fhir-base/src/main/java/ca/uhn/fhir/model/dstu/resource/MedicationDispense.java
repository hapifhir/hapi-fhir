















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
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.RatioDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.composite.ScheduleDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.MedicationDispenseStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>MedicationDispense</b> Resource
 * (Dispensing a medication to a named patient)
 *
 * <p>
 * <b>Definition:</b>
 * Dispensing a medication to a named patient.  This includes a description of the supply provided and the instructions for administering the medication.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/MedicationDispense">http://hl7.org/fhir/profiles/MedicationDispense</a> 
 * </p>
 *
 */
@ResourceDef(name="MedicationDispense", profile="http://hl7.org/fhir/profiles/MedicationDispense", id="medicationdispense")
public class MedicationDispense extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>destination</b>
	 * <p>
	 * Description: <b>Return dispenses that should be sent to a secific destination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.dispense.destination</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="destination", path="MedicationDispense.dispense.destination", description="Return dispenses that should be sent to a secific destination", type="reference"  )
	public static final String SP_DESTINATION = "destination";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>destination</b>
	 * <p>
	 * Description: <b>Return dispenses that should be sent to a secific destination</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.dispense.destination</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam DESTINATION = new ReferenceClientParam(SP_DESTINATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationDispense.dispense.destination</b>".
	 */
	public static final Include INCLUDE_DISPENSE_DESTINATION = new Include("MedicationDispense.dispense.destination");

	/**
	 * Search parameter constant for <b>dispenser</b>
	 * <p>
	 * Description: <b>Return all dispenses performed by a specific indiividual</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.dispenser</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="dispenser", path="MedicationDispense.dispenser", description="Return all dispenses performed by a specific indiividual", type="reference"  )
	public static final String SP_DISPENSER = "dispenser";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>dispenser</b>
	 * <p>
	 * Description: <b>Return all dispenses performed by a specific indiividual</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.dispenser</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam DISPENSER = new ReferenceClientParam(SP_DISPENSER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationDispense.dispenser</b>".
	 */
	public static final Include INCLUDE_DISPENSER = new Include("MedicationDispense.dispenser");

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return dispenses with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationDispense.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="MedicationDispense.identifier", description="Return dispenses with this external identity", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return dispenses with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationDispense.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Returns dispenses of this medicine</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.dispense.medication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="medication", path="MedicationDispense.dispense.medication", description="Returns dispenses of this medicine", type="reference"  )
	public static final String SP_MEDICATION = "medication";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Returns dispenses of this medicine</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.dispense.medication</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam MEDICATION = new ReferenceClientParam(SP_MEDICATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationDispense.dispense.medication</b>".
	 */
	public static final Include INCLUDE_DISPENSE_MEDICATION = new Include("MedicationDispense.dispense.medication");

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list dispenses  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="MedicationDispense.patient", description="The identity of a patient to list dispenses  for", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list dispenses  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationDispense.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("MedicationDispense.patient");

	/**
	 * Search parameter constant for <b>prescription</b>
	 * <p>
	 * Description: <b>The identity of a prescription to list dispenses from</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.authorizingPrescription</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="prescription", path="MedicationDispense.authorizingPrescription", description="The identity of a prescription to list dispenses from", type="reference"  )
	public static final String SP_PRESCRIPTION = "prescription";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>prescription</b>
	 * <p>
	 * Description: <b>The identity of a prescription to list dispenses from</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.authorizingPrescription</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PRESCRIPTION = new ReferenceClientParam(SP_PRESCRIPTION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationDispense.authorizingPrescription</b>".
	 */
	public static final Include INCLUDE_AUTHORIZINGPRESCRIPTION = new Include("MedicationDispense.authorizingPrescription");

	/**
	 * Search parameter constant for <b>responsibleparty</b>
	 * <p>
	 * Description: <b>Return all dispenses with the specified responsible party</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.substitution.responsibleParty</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="responsibleparty", path="MedicationDispense.substitution.responsibleParty", description="Return all dispenses with the specified responsible party", type="reference"  )
	public static final String SP_RESPONSIBLEPARTY = "responsibleparty";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>responsibleparty</b>
	 * <p>
	 * Description: <b>Return all dispenses with the specified responsible party</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationDispense.substitution.responsibleParty</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam RESPONSIBLEPARTY = new ReferenceClientParam(SP_RESPONSIBLEPARTY);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationDispense.substitution.responsibleParty</b>".
	 */
	public static final Include INCLUDE_SUBSTITUTION_RESPONSIBLEPARTY = new Include("MedicationDispense.substitution.responsibleParty");

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Status of the dispense</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationDispense.dispense.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="MedicationDispense.dispense.status", description="Status of the dispense", type="token"  )
	public static final String SP_STATUS = "status";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>Status of the dispense</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationDispense.dispense.status</b><br/>
	 * </p>
	 */
	public static final TokenClientParam STATUS = new TokenClientParam(SP_STATUS);

	/**
	 * Search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>Return all dispenses of a specific type</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationDispense.dispense.type</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="type", path="MedicationDispense.dispense.type", description="Return all dispenses of a specific type", type="token"  )
	public static final String SP_TYPE = "type";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>type</b>
	 * <p>
	 * Description: <b>Return all dispenses of a specific type</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationDispense.dispense.type</b><br/>
	 * </p>
	 */
	public static final TokenClientParam TYPE = new TokenClientParam(SP_TYPE);

	/**
	 * Search parameter constant for <b>whenhandedover</b>
	 * <p>
	 * Description: <b>Date when medication handed over to patient (outpatient setting), or supplied to ward or clinic (inpatient setting)</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationDispense.dispense.whenHandedOver</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="whenhandedover", path="MedicationDispense.dispense.whenHandedOver", description="Date when medication handed over to patient (outpatient setting), or supplied to ward or clinic (inpatient setting)", type="date"  )
	public static final String SP_WHENHANDEDOVER = "whenhandedover";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>whenhandedover</b>
	 * <p>
	 * Description: <b>Date when medication handed over to patient (outpatient setting), or supplied to ward or clinic (inpatient setting)</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationDispense.dispense.whenHandedOver</b><br/>
	 * </p>
	 */
	public static final DateClientParam WHENHANDEDOVER = new DateClientParam(SP_WHENHANDEDOVER);

	/**
	 * Search parameter constant for <b>whenprepared</b>
	 * <p>
	 * Description: <b>Date when medication prepared</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationDispense.dispense.whenPrepared</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="whenprepared", path="MedicationDispense.dispense.whenPrepared", description="Date when medication prepared", type="date"  )
	public static final String SP_WHENPREPARED = "whenprepared";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>whenprepared</b>
	 * <p>
	 * Description: <b>Date when medication prepared</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationDispense.dispense.whenPrepared</b><br/>
	 * </p>
	 */
	public static final DateClientParam WHENPREPARED = new DateClientParam(SP_WHENPREPARED);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="External identifier",
		formalDefinition="Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR."
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="in progress | on hold | completed | entered in error | stopped",
		formalDefinition="A code specifying the state of the set of dispense events."
	)
	private BoundCodeDt<MedicationDispenseStatusEnum> myStatus;
	
	@Child(name="patient", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who the dispense is for",
		formalDefinition="A link to a resource representing the person to whom the medication will be given."
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="dispenser", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Practitioner responsible for dispensing medication",
		formalDefinition="The individual responsible for dispensing the medication"
	)
	private ResourceReferenceDt myDispenser;
	
	@Child(name="authorizingPrescription", order=4, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.MedicationPrescription.class	})
	@Description(
		shortDefinition="Medication order that authorizes the dispense",
		formalDefinition="Indicates the medication order that is being dispensed against."
	)
	private java.util.List<ResourceReferenceDt> myAuthorizingPrescription;
	
	@Child(name="dispense", order=5, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Details for individual dispensed medicationdetails",
		formalDefinition="Indicates the details of the dispense event such as the days supply and quantity of medication dispensed."
	)
	private java.util.List<Dispense> myDispense;
	
	@Child(name="substitution", order=6, min=0, max=1)	
	@Description(
		shortDefinition="Deals with substitution of one medicine for another",
		formalDefinition="Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why."
	)
	private Substitution mySubstitution;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myPatient,  myDispenser,  myAuthorizingPrescription,  myDispense,  mySubstitution);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myPatient, myDispenser, myAuthorizingPrescription, myDispense, mySubstitution);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.
     * </p> 
	 */
	public MedicationDispense setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.
     * </p> 
	 */
	public MedicationDispense setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.
     * </p> 
	 */
	public MedicationDispense setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the set of dispense events.
     * </p> 
	 */
	public BoundCodeDt<MedicationDispenseStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<MedicationDispenseStatusEnum>(MedicationDispenseStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the set of dispense events.
     * </p> 
	 */
	public MedicationDispense setStatus(BoundCodeDt<MedicationDispenseStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the set of dispense events.
     * </p> 
	 */
	public MedicationDispense setStatus(MedicationDispenseStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> (Who the dispense is for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A link to a resource representing the person to whom the medication will be given.
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Who the dispense is for)
	 *
     * <p>
     * <b>Definition:</b>
     * A link to a resource representing the person to whom the medication will be given.
     * </p> 
	 */
	public MedicationDispense setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>dispenser</b> (Practitioner responsible for dispensing medication).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The individual responsible for dispensing the medication
     * </p> 
	 */
	public ResourceReferenceDt getDispenser() {  
		if (myDispenser == null) {
			myDispenser = new ResourceReferenceDt();
		}
		return myDispenser;
	}

	/**
	 * Sets the value(s) for <b>dispenser</b> (Practitioner responsible for dispensing medication)
	 *
     * <p>
     * <b>Definition:</b>
     * The individual responsible for dispensing the medication
     * </p> 
	 */
	public MedicationDispense setDispenser(ResourceReferenceDt theValue) {
		myDispenser = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>authorizingPrescription</b> (Medication order that authorizes the dispense).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the medication order that is being dispensed against.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getAuthorizingPrescription() {  
		if (myAuthorizingPrescription == null) {
			myAuthorizingPrescription = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myAuthorizingPrescription;
	}

	/**
	 * Sets the value(s) for <b>authorizingPrescription</b> (Medication order that authorizes the dispense)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the medication order that is being dispensed against.
     * </p> 
	 */
	public MedicationDispense setAuthorizingPrescription(java.util.List<ResourceReferenceDt> theValue) {
		myAuthorizingPrescription = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>authorizingPrescription</b> (Medication order that authorizes the dispense)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the medication order that is being dispensed against.
     * </p> 
	 */
	public ResourceReferenceDt addAuthorizingPrescription() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getAuthorizingPrescription().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>dispense</b> (Details for individual dispensed medicationdetails).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of medication dispensed.
     * </p> 
	 */
	public java.util.List<Dispense> getDispense() {  
		if (myDispense == null) {
			myDispense = new java.util.ArrayList<Dispense>();
		}
		return myDispense;
	}

	/**
	 * Sets the value(s) for <b>dispense</b> (Details for individual dispensed medicationdetails)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of medication dispensed.
     * </p> 
	 */
	public MedicationDispense setDispense(java.util.List<Dispense> theValue) {
		myDispense = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dispense</b> (Details for individual dispensed medicationdetails)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of medication dispensed.
     * </p> 
	 */
	public Dispense addDispense() {
		Dispense newType = new Dispense();
		getDispense().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dispense</b> (Details for individual dispensed medicationdetails),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of medication dispensed.
     * </p> 
	 */
	public Dispense getDispenseFirstRep() {
		if (getDispense().isEmpty()) {
			return addDispense();
		}
		return getDispense().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>substitution</b> (Deals with substitution of one medicine for another).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.
     * </p> 
	 */
	public Substitution getSubstitution() {  
		if (mySubstitution == null) {
			mySubstitution = new Substitution();
		}
		return mySubstitution;
	}

	/**
	 * Sets the value(s) for <b>substitution</b> (Deals with substitution of one medicine for another)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.
     * </p> 
	 */
	public MedicationDispense setSubstitution(Substitution theValue) {
		mySubstitution = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>MedicationDispense.dispense</b> (Details for individual dispensed medicationdetails)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the details of the dispense event such as the days supply and quantity of medication dispensed.
     * </p> 
	 */
	@Block()	
	public static class Dispense extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="External identifier for individual item",
		formalDefinition="Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR."
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="in progress | on hold | completed | entered in error | stopped",
		formalDefinition="A code specifying the state of the dispense event."
	)
	private BoundCodeDt<MedicationDispenseStatusEnum> myStatus;
	
	@Child(name="type", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Trial fill, partial fill, emergency fill, etc.",
		formalDefinition="Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc."
	)
	private CodeableConceptDt myType;
	
	@Child(name="quantity", type=QuantityDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Amount dispensed",
		formalDefinition="The amount of medication that has been dispensed. Includes unit of measure."
	)
	private QuantityDt myQuantity;
	
	@Child(name="medication", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Medication.class	})
	@Description(
		shortDefinition="What medication was supplied",
		formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications."
	)
	private ResourceReferenceDt myMedication;
	
	@Child(name="whenPrepared", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Dispense processing time",
		formalDefinition="The time when the dispensed product was packaged and reviewed."
	)
	private DateTimeDt myWhenPrepared;
	
	@Child(name="whenHandedOver", type=DateTimeDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Handover time",
		formalDefinition="The time the dispensed product was provided to the patient or their representative."
	)
	private DateTimeDt myWhenHandedOver;
	
	@Child(name="destination", order=7, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Location.class	})
	@Description(
		shortDefinition="Where the medication was sent",
		formalDefinition="Identification of the facility/location where the medication was shipped to, as part of the dispense event."
	)
	private ResourceReferenceDt myDestination;
	
	@Child(name="receiver", order=8, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who collected the medication",
		formalDefinition="Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional"
	)
	private java.util.List<ResourceReferenceDt> myReceiver;
	
	@Child(name="dosage", order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Medicine administration instructions to the patient/carer",
		formalDefinition="Indicates how the medication is to be used by the patient"
	)
	private java.util.List<DispenseDosage> myDosage;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myType,  myQuantity,  myMedication,  myWhenPrepared,  myWhenHandedOver,  myDestination,  myReceiver,  myDosage);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myType, myQuantity, myMedication, myWhenPrepared, myWhenHandedOver, myDestination, myReceiver, myDosage);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External identifier for individual item).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR.
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External identifier for individual item)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR.
     * </p> 
	 */
	public Dispense setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (External identifier for individual item)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR.
     * </p> 
	 */
	public Dispense setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (External identifier for individual item)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR.
     * </p> 
	 */
	public Dispense setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public BoundCodeDt<MedicationDispenseStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<MedicationDispenseStatusEnum>(MedicationDispenseStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public Dispense setStatus(BoundCodeDt<MedicationDispenseStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the state of the dispense event.
     * </p> 
	 */
	public Dispense setStatus(MedicationDispenseStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Trial fill, partial fill, emergency fill, etc.).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Trial fill, partial fill, emergency fill, etc.)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     * </p> 
	 */
	public Dispense setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>quantity</b> (Amount dispensed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of medication that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of medication that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of medication that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of medication that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of medication that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of medication that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of medication that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( double theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount dispensed)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of medication that has been dispensed. Includes unit of measure.
     * </p> 
	 */
	public Dispense setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>medication</b> (What medication was supplied).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public ResourceReferenceDt getMedication() {  
		if (myMedication == null) {
			myMedication = new ResourceReferenceDt();
		}
		return myMedication;
	}

	/**
	 * Sets the value(s) for <b>medication</b> (What medication was supplied)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public Dispense setMedication(ResourceReferenceDt theValue) {
		myMedication = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>whenPrepared</b> (Dispense processing time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the dispensed product was packaged and reviewed.
     * </p> 
	 */
	public DateTimeDt getWhenPrepared() {  
		if (myWhenPrepared == null) {
			myWhenPrepared = new DateTimeDt();
		}
		return myWhenPrepared;
	}

	/**
	 * Sets the value(s) for <b>whenPrepared</b> (Dispense processing time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the dispensed product was packaged and reviewed.
     * </p> 
	 */
	public Dispense setWhenPrepared(DateTimeDt theValue) {
		myWhenPrepared = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>whenPrepared</b> (Dispense processing time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the dispensed product was packaged and reviewed.
     * </p> 
	 */
	public Dispense setWhenPrepared( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myWhenPrepared = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>whenPrepared</b> (Dispense processing time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time when the dispensed product was packaged and reviewed.
     * </p> 
	 */
	public Dispense setWhenPreparedWithSecondsPrecision( Date theDate) {
		myWhenPrepared = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>whenHandedOver</b> (Handover time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed product was provided to the patient or their representative.
     * </p> 
	 */
	public DateTimeDt getWhenHandedOver() {  
		if (myWhenHandedOver == null) {
			myWhenHandedOver = new DateTimeDt();
		}
		return myWhenHandedOver;
	}

	/**
	 * Sets the value(s) for <b>whenHandedOver</b> (Handover time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed product was provided to the patient or their representative.
     * </p> 
	 */
	public Dispense setWhenHandedOver(DateTimeDt theValue) {
		myWhenHandedOver = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>whenHandedOver</b> (Handover time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed product was provided to the patient or their representative.
     * </p> 
	 */
	public Dispense setWhenHandedOver( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myWhenHandedOver = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>whenHandedOver</b> (Handover time)
	 *
     * <p>
     * <b>Definition:</b>
     * The time the dispensed product was provided to the patient or their representative.
     * </p> 
	 */
	public Dispense setWhenHandedOverWithSecondsPrecision( Date theDate) {
		myWhenHandedOver = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>destination</b> (Where the medication was sent).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the facility/location where the medication was shipped to, as part of the dispense event.
     * </p> 
	 */
	public ResourceReferenceDt getDestination() {  
		if (myDestination == null) {
			myDestination = new ResourceReferenceDt();
		}
		return myDestination;
	}

	/**
	 * Sets the value(s) for <b>destination</b> (Where the medication was sent)
	 *
     * <p>
     * <b>Definition:</b>
     * Identification of the facility/location where the medication was shipped to, as part of the dispense event.
     * </p> 
	 */
	public Dispense setDestination(ResourceReferenceDt theValue) {
		myDestination = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>receiver</b> (Who collected the medication).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getReceiver() {  
		if (myReceiver == null) {
			myReceiver = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myReceiver;
	}

	/**
	 * Sets the value(s) for <b>receiver</b> (Who collected the medication)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional
     * </p> 
	 */
	public Dispense setReceiver(java.util.List<ResourceReferenceDt> theValue) {
		myReceiver = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>receiver</b> (Who collected the medication)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional
     * </p> 
	 */
	public ResourceReferenceDt addReceiver() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getReceiver().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>dosage</b> (Medicine administration instructions to the patient/carer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	public java.util.List<DispenseDosage> getDosage() {  
		if (myDosage == null) {
			myDosage = new java.util.ArrayList<DispenseDosage>();
		}
		return myDosage;
	}

	/**
	 * Sets the value(s) for <b>dosage</b> (Medicine administration instructions to the patient/carer)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	public Dispense setDosage(java.util.List<DispenseDosage> theValue) {
		myDosage = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dosage</b> (Medicine administration instructions to the patient/carer)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	public DispenseDosage addDosage() {
		DispenseDosage newType = new DispenseDosage();
		getDosage().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dosage</b> (Medicine administration instructions to the patient/carer),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	public DispenseDosage getDosageFirstRep() {
		if (getDosage().isEmpty()) {
			return addDosage();
		}
		return getDosage().get(0); 
	}
  

	}

	/**
	 * Block class for child element: <b>MedicationDispense.dispense.dosage</b> (Medicine administration instructions to the patient/carer)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is to be used by the patient
     * </p> 
	 */
	@Block()	
	public static class DispenseDosage extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="additionalInstructions", type=CodeableConceptDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="E.g. \"Take with food\"",
		formalDefinition="Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded."
	)
	private CodeableConceptDt myAdditionalInstructions;
	
	@Child(name="timing", order=1, min=0, max=1, type={
		DateTimeDt.class, 		PeriodDt.class, 		ScheduleDt.class	})
	@Description(
		shortDefinition="When medication should be administered",
		formalDefinition="The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\""
	)
	private IDatatype myTiming;
	
	@Child(name="asNeeded", order=2, min=0, max=1, type={
		BooleanDt.class, 		CodeableConceptDt.class	})
	@Description(
		shortDefinition="Take \"as needed\" f(or x)",
		formalDefinition="If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication"
	)
	private IDatatype myAsNeeded;
	
	@Child(name="site", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Body site to administer to",
		formalDefinition="A coded specification of the anatomic site where the medication first enters the body"
	)
	private CodeableConceptDt mySite;
	
	@Child(name="route", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="How drug should enter body",
		formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject."
	)
	private CodeableConceptDt myRoute;
	
	@Child(name="method", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Technique for administering medication",
		formalDefinition="A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration."
	)
	private CodeableConceptDt myMethod;
	
	@Child(name="quantity", type=QuantityDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Amount of medication per dose",
		formalDefinition="The amount of therapeutic or other substance given at one administration event."
	)
	private QuantityDt myQuantity;
	
	@Child(name="rate", type=RatioDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Amount of medication per unit of time",
		formalDefinition="Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours."
	)
	private RatioDt myRate;
	
	@Child(name="maxDosePerPeriod", type=RatioDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Upper limit on medication per unit of time",
		formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours."
	)
	private RatioDt myMaxDosePerPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myAdditionalInstructions,  myTiming,  myAsNeeded,  mySite,  myRoute,  myMethod,  myQuantity,  myRate,  myMaxDosePerPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myAdditionalInstructions, myTiming, myAsNeeded, mySite, myRoute, myMethod, myQuantity, myRate, myMaxDosePerPeriod);
	}

	/**
	 * Gets the value(s) for <b>additionalInstructions</b> (E.g. \"Take with food\").
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded.
     * </p> 
	 */
	public CodeableConceptDt getAdditionalInstructions() {  
		if (myAdditionalInstructions == null) {
			myAdditionalInstructions = new CodeableConceptDt();
		}
		return myAdditionalInstructions;
	}

	/**
	 * Sets the value(s) for <b>additionalInstructions</b> (E.g. \"Take with food\")
	 *
     * <p>
     * <b>Definition:</b>
     * Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded.
     * </p> 
	 */
	public DispenseDosage setAdditionalInstructions(CodeableConceptDt theValue) {
		myAdditionalInstructions = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>timing[x]</b> (When medication should be administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"
     * </p> 
	 */
	public IDatatype getTiming() {  
		return myTiming;
	}

	/**
	 * Sets the value(s) for <b>timing[x]</b> (When medication should be administered)
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"
     * </p> 
	 */
	public DispenseDosage setTiming(IDatatype theValue) {
		myTiming = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>asNeeded[x]</b> (Take \"as needed\" f(or x)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication
     * </p> 
	 */
	public IDatatype getAsNeeded() {  
		return myAsNeeded;
	}

	/**
	 * Sets the value(s) for <b>asNeeded[x]</b> (Take \"as needed\" f(or x))
	 *
     * <p>
     * <b>Definition:</b>
     * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication
     * </p> 
	 */
	public DispenseDosage setAsNeeded(IDatatype theValue) {
		myAsNeeded = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>site</b> (Body site to administer to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first enters the body
     * </p> 
	 */
	public CodeableConceptDt getSite() {  
		if (mySite == null) {
			mySite = new CodeableConceptDt();
		}
		return mySite;
	}

	/**
	 * Sets the value(s) for <b>site</b> (Body site to administer to)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first enters the body
     * </p> 
	 */
	public DispenseDosage setSite(CodeableConceptDt theValue) {
		mySite = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>route</b> (How drug should enter body).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.
     * </p> 
	 */
	public CodeableConceptDt getRoute() {  
		if (myRoute == null) {
			myRoute = new CodeableConceptDt();
		}
		return myRoute;
	}

	/**
	 * Sets the value(s) for <b>route</b> (How drug should enter body)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.
     * </p> 
	 */
	public DispenseDosage setRoute(CodeableConceptDt theValue) {
		myRoute = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>method</b> (Technique for administering medication).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public CodeableConceptDt getMethod() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
		}
		return myMethod;
	}

	/**
	 * Sets the value(s) for <b>method</b> (Technique for administering medication)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public DispenseDosage setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>quantity</b> (Amount of medication per dose).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public QuantityDt getQuantity() {  
		if (myQuantity == null) {
			myQuantity = new QuantityDt();
		}
		return myQuantity;
	}

	/**
	 * Sets the value(s) for <b>quantity</b> (Amount of medication per dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public DispenseDosage setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (Amount of medication per dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public DispenseDosage setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of medication per dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public DispenseDosage setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of medication per dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public DispenseDosage setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of medication per dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public DispenseDosage setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of medication per dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public DispenseDosage setQuantity( double theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount of medication per dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public DispenseDosage setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>rate</b> (Amount of medication per unit of time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.
     * </p> 
	 */
	public RatioDt getRate() {  
		if (myRate == null) {
			myRate = new RatioDt();
		}
		return myRate;
	}

	/**
	 * Sets the value(s) for <b>rate</b> (Amount of medication per unit of time)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.
     * </p> 
	 */
	public DispenseDosage setRate(RatioDt theValue) {
		myRate = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>maxDosePerPeriod</b> (Upper limit on medication per unit of time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.
     * </p> 
	 */
	public RatioDt getMaxDosePerPeriod() {  
		if (myMaxDosePerPeriod == null) {
			myMaxDosePerPeriod = new RatioDt();
		}
		return myMaxDosePerPeriod;
	}

	/**
	 * Sets the value(s) for <b>maxDosePerPeriod</b> (Upper limit on medication per unit of time)
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.
     * </p> 
	 */
	public DispenseDosage setMaxDosePerPeriod(RatioDt theValue) {
		myMaxDosePerPeriod = theValue;
		return this;
	}

  

	}



	/**
	 * Block class for child element: <b>MedicationDispense.substitution</b> (Deals with substitution of one medicine for another)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.
     * </p> 
	 */
	@Block()	
	public static class Substitution extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="type", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Type of substitiution",
		formalDefinition="A code signifying whether a different drug was dispensed from what was prescribed."
	)
	private CodeableConceptDt myType;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Why was substitution made",
		formalDefinition="Indicates the reason for the substitution of (or lack of substitution) from what was prescribed."
	)
	private java.util.List<CodeableConceptDt> myReason;
	
	@Child(name="responsibleParty", order=2, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who is responsible for the substitution",
		formalDefinition="The person or organization that has primary responsibility for the substitution"
	)
	private java.util.List<ResourceReferenceDt> myResponsibleParty;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myType,  myReason,  myResponsibleParty);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myType, myReason, myResponsibleParty);
	}

	/**
	 * Gets the value(s) for <b>type</b> (Type of substitiution).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code signifying whether a different drug was dispensed from what was prescribed.
     * </p> 
	 */
	public CodeableConceptDt getType() {  
		if (myType == null) {
			myType = new CodeableConceptDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Type of substitiution)
	 *
     * <p>
     * <b>Definition:</b>
     * A code signifying whether a different drug was dispensed from what was prescribed.
     * </p> 
	 */
	public Substitution setType(CodeableConceptDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reason</b> (Why was substitution made).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getReason() {  
		if (myReason == null) {
			myReason = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (Why was substitution made)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.
     * </p> 
	 */
	public Substitution setReason(java.util.List<CodeableConceptDt> theValue) {
		myReason = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>reason</b> (Why was substitution made)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.
     * </p> 
	 */
	public CodeableConceptDt addReason() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getReason().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>reason</b> (Why was substitution made),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.
     * </p> 
	 */
	public CodeableConceptDt getReasonFirstRep() {
		if (getReason().isEmpty()) {
			return addReason();
		}
		return getReason().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>responsibleParty</b> (Who is responsible for the substitution).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or organization that has primary responsibility for the substitution
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getResponsibleParty() {  
		if (myResponsibleParty == null) {
			myResponsibleParty = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myResponsibleParty;
	}

	/**
	 * Sets the value(s) for <b>responsibleParty</b> (Who is responsible for the substitution)
	 *
     * <p>
     * <b>Definition:</b>
     * The person or organization that has primary responsibility for the substitution
     * </p> 
	 */
	public Substitution setResponsibleParty(java.util.List<ResourceReferenceDt> theValue) {
		myResponsibleParty = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>responsibleParty</b> (Who is responsible for the substitution)
	 *
     * <p>
     * <b>Definition:</b>
     * The person or organization that has primary responsibility for the substitution
     * </p> 
	 */
	public ResourceReferenceDt addResponsibleParty() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getResponsibleParty().add(newType);
		return newType; 
	}
  

	}




}