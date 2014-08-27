















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


import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.Include;
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
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>MedicationStatement</b> Resource
 * (Administration of medication to a patient)
 *
 * <p>
 * <b>Definition:</b>
 * A record of medication being taken by a patient, or that the medication has been given to a patient where the record is the result of a report from the patient or another clinician
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/MedicationStatement">http://hl7.org/fhir/profiles/MedicationStatement</a> 
 * </p>
 *
 */
@ResourceDef(name="MedicationStatement", profile="http://hl7.org/fhir/profiles/MedicationStatement", id="medicationstatement")
public class MedicationStatement extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>device</b>
	 * <p>
	 * Description: <b>Return administrations with this administration device identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationStatement.device</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="device", path="MedicationStatement.device", description="Return administrations with this administration device identity", type="reference"  )
	public static final String SP_DEVICE = "device";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>device</b>
	 * <p>
	 * Description: <b>Return administrations with this administration device identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationStatement.device</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam DEVICE = new ReferenceClientParam(SP_DEVICE);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationStatement.device</b>".
	 */
	public static final Include INCLUDE_DEVICE = new Include("MedicationStatement.device");

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return administrations with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationStatement.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="MedicationStatement.identifier", description="Return administrations with this external identity", type="token"  )
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return administrations with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationStatement.identifier</b><br/>
	 * </p>
	 */
	public static final TokenClientParam IDENTIFIER = new TokenClientParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Code for medicine or text in medicine name</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationStatement.medication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="medication", path="MedicationStatement.medication", description="Code for medicine or text in medicine name", type="reference"  )
	public static final String SP_MEDICATION = "medication";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Code for medicine or text in medicine name</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationStatement.medication</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam MEDICATION = new ReferenceClientParam(SP_MEDICATION);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationStatement.medication</b>".
	 */
	public static final Include INCLUDE_MEDICATION = new Include("MedicationStatement.medication");

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list administrations  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationStatement.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="MedicationStatement.patient", description="The identity of a patient to list administrations  for", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list administrations  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationStatement.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>MedicationStatement.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("MedicationStatement.patient");

	/**
	 * Search parameter constant for <b>when-given</b>
	 * <p>
	 * Description: <b>Date of administration</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationStatement.whenGiven</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="when-given", path="MedicationStatement.whenGiven", description="Date of administration", type="date"  )
	public static final String SP_WHEN_GIVEN = "when-given";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>when-given</b>
	 * <p>
	 * Description: <b>Date of administration</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationStatement.whenGiven</b><br/>
	 * </p>
	 */
	public static final DateClientParam WHEN_GIVEN = new DateClientParam(SP_WHEN_GIVEN);


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External Identifier",
		formalDefinition="External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="patient", order=1, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who was/is taking medication",
		formalDefinition="The person or animal who is /was taking the medication."
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="wasNotGiven", type=BooleanDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="True if medication is/was not being taken",
		formalDefinition="Set this to true if the record is saying that the medication was NOT taken."
	)
	private BooleanDt myWasNotGiven;
	
	@Child(name="reasonNotGiven", type=CodeableConceptDt.class, order=3, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="True if asserting medication was not given",
		formalDefinition="A code indicating why the medication was not taken."
	)
	private java.util.List<CodeableConceptDt> myReasonNotGiven;
	
	@Child(name="whenGiven", type=PeriodDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Over what period was medication consumed?",
		formalDefinition="The interval of time during which it is being asserted that the patient was taking the medication."
	)
	private PeriodDt myWhenGiven;
	
	@Child(name="medication", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Medication.class	})
	@Description(
		shortDefinition="What medication was taken?",
		formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications."
	)
	private ResourceReferenceDt myMedication;
	
	@Child(name="device", order=6, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="E.g. infusion pump",
		formalDefinition="An identifier or a link to a resource that identifies a device used in administering the medication to the patient."
	)
	private java.util.List<ResourceReferenceDt> myDevice;
	
	@Child(name="dosage", order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Details of how medication was taken",
		formalDefinition="Indicates how the medication is/was used by the patient"
	)
	private java.util.List<Dosage> myDosage;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myPatient,  myWasNotGiven,  myReasonNotGiven,  myWhenGiven,  myMedication,  myDevice,  myDosage);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myPatient, myWasNotGiven, myReasonNotGiven, myWhenGiven, myMedication, myDevice, myDosage);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External Identifier).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public MedicationStatement setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (External Identifier),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public MedicationStatement addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External Identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public MedicationStatement addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>patient</b> (Who was/is taking medication).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or animal who is /was taking the medication.
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Who was/is taking medication)
	 *
     * <p>
     * <b>Definition:</b>
     * The person or animal who is /was taking the medication.
     * </p> 
	 */
	public MedicationStatement setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>wasNotGiven</b> (True if medication is/was not being taken).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT taken.
     * </p> 
	 */
	public BooleanDt getWasNotGiven() {  
		if (myWasNotGiven == null) {
			myWasNotGiven = new BooleanDt();
		}
		return myWasNotGiven;
	}

	/**
	 * Sets the value(s) for <b>wasNotGiven</b> (True if medication is/was not being taken)
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT taken.
     * </p> 
	 */
	public MedicationStatement setWasNotGiven(BooleanDt theValue) {
		myWasNotGiven = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>wasNotGiven</b> (True if medication is/was not being taken)
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT taken.
     * </p> 
	 */
	public MedicationStatement setWasNotGiven( boolean theBoolean) {
		myWasNotGiven = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reasonNotGiven</b> (True if asserting medication was not given).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the medication was not taken.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getReasonNotGiven() {  
		if (myReasonNotGiven == null) {
			myReasonNotGiven = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myReasonNotGiven;
	}

	/**
	 * Sets the value(s) for <b>reasonNotGiven</b> (True if asserting medication was not given)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the medication was not taken.
     * </p> 
	 */
	public MedicationStatement setReasonNotGiven(java.util.List<CodeableConceptDt> theValue) {
		myReasonNotGiven = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>reasonNotGiven</b> (True if asserting medication was not given)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the medication was not taken.
     * </p> 
	 */
	public CodeableConceptDt addReasonNotGiven() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getReasonNotGiven().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>reasonNotGiven</b> (True if asserting medication was not given),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the medication was not taken.
     * </p> 
	 */
	public CodeableConceptDt getReasonNotGivenFirstRep() {
		if (getReasonNotGiven().isEmpty()) {
			return addReasonNotGiven();
		}
		return getReasonNotGiven().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>whenGiven</b> (Over what period was medication consumed?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The interval of time during which it is being asserted that the patient was taking the medication.
     * </p> 
	 */
	public PeriodDt getWhenGiven() {  
		if (myWhenGiven == null) {
			myWhenGiven = new PeriodDt();
		}
		return myWhenGiven;
	}

	/**
	 * Sets the value(s) for <b>whenGiven</b> (Over what period was medication consumed?)
	 *
     * <p>
     * <b>Definition:</b>
     * The interval of time during which it is being asserted that the patient was taking the medication.
     * </p> 
	 */
	public MedicationStatement setWhenGiven(PeriodDt theValue) {
		myWhenGiven = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>medication</b> (What medication was taken?).
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
	 * Sets the value(s) for <b>medication</b> (What medication was taken?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public MedicationStatement setMedication(ResourceReferenceDt theValue) {
		myMedication = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>device</b> (E.g. infusion pump).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier or a link to a resource that identifies a device used in administering the medication to the patient.
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDevice() {  
		if (myDevice == null) {
			myDevice = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDevice;
	}

	/**
	 * Sets the value(s) for <b>device</b> (E.g. infusion pump)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier or a link to a resource that identifies a device used in administering the medication to the patient.
     * </p> 
	 */
	public MedicationStatement setDevice(java.util.List<ResourceReferenceDt> theValue) {
		myDevice = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>device</b> (E.g. infusion pump)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier or a link to a resource that identifies a device used in administering the medication to the patient.
     * </p> 
	 */
	public ResourceReferenceDt addDevice() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDevice().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>dosage</b> (Details of how medication was taken).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is/was used by the patient
     * </p> 
	 */
	public java.util.List<Dosage> getDosage() {  
		if (myDosage == null) {
			myDosage = new java.util.ArrayList<Dosage>();
		}
		return myDosage;
	}

	/**
	 * Sets the value(s) for <b>dosage</b> (Details of how medication was taken)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is/was used by the patient
     * </p> 
	 */
	public MedicationStatement setDosage(java.util.List<Dosage> theValue) {
		myDosage = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dosage</b> (Details of how medication was taken)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is/was used by the patient
     * </p> 
	 */
	public Dosage addDosage() {
		Dosage newType = new Dosage();
		getDosage().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dosage</b> (Details of how medication was taken),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is/was used by the patient
     * </p> 
	 */
	public Dosage getDosageFirstRep() {
		if (getDosage().isEmpty()) {
			return addDosage();
		}
		return getDosage().get(0); 
	}
  
	/**
	 * Block class for child element: <b>MedicationStatement.dosage</b> (Details of how medication was taken)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how the medication is/was used by the patient
     * </p> 
	 */
	@Block()	
	public static class Dosage extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="timing", type=ScheduleDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="When/how often was medication taken?",
		formalDefinition="The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\""
	)
	private ScheduleDt myTiming;
	
	@Child(name="asNeeded", order=1, min=0, max=1, type={
		BooleanDt.class, 		CodeableConceptDt.class	})
	@Description(
		shortDefinition="Take \"as needed\" f(or x)",
		formalDefinition="If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication"
	)
	private IDatatype myAsNeeded;
	
	@Child(name="site", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Where on body was medication administered?",
		formalDefinition="A coded specification of the anatomic site where the medication first enters the body"
	)
	private CodeableConceptDt mySite;
	
	@Child(name="route", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="How did the medication enter the body?",
		formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject."
	)
	private CodeableConceptDt myRoute;
	
	@Child(name="method", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Technique used to administer medication",
		formalDefinition="A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration."
	)
	private CodeableConceptDt myMethod;
	
	@Child(name="quantity", type=QuantityDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Amount administered in one dose",
		formalDefinition="The amount of therapeutic or other substance given at one administration event."
	)
	private QuantityDt myQuantity;
	
	@Child(name="rate", type=RatioDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Dose quantity per unit of time",
		formalDefinition="Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours."
	)
	private RatioDt myRate;
	
	@Child(name="maxDosePerPeriod", type=RatioDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Maximum dose that was consumed per unit of time",
		formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours."
	)
	private RatioDt myMaxDosePerPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myTiming,  myAsNeeded,  mySite,  myRoute,  myMethod,  myQuantity,  myRate,  myMaxDosePerPeriod);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myTiming, myAsNeeded, mySite, myRoute, myMethod, myQuantity, myRate, myMaxDosePerPeriod);
	}

	/**
	 * Gets the value(s) for <b>timing</b> (When/how often was medication taken?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"
     * </p> 
	 */
	public ScheduleDt getTiming() {  
		if (myTiming == null) {
			myTiming = new ScheduleDt();
		}
		return myTiming;
	}

	/**
	 * Sets the value(s) for <b>timing</b> (When/how often was medication taken?)
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"
     * </p> 
	 */
	public Dosage setTiming(ScheduleDt theValue) {
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
	public Dosage setAsNeeded(IDatatype theValue) {
		myAsNeeded = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>site</b> (Where on body was medication administered?).
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
	 * Sets the value(s) for <b>site</b> (Where on body was medication administered?)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first enters the body
     * </p> 
	 */
	public Dosage setSite(CodeableConceptDt theValue) {
		mySite = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>route</b> (How did the medication enter the body?).
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
	 * Sets the value(s) for <b>route</b> (How did the medication enter the body?)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.
     * </p> 
	 */
	public Dosage setRoute(CodeableConceptDt theValue) {
		myRoute = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>method</b> (Technique used to administer medication).
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
	 * Sets the value(s) for <b>method</b> (Technique used to administer medication)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public Dosage setMethod(CodeableConceptDt theValue) {
		myMethod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>quantity</b> (Amount administered in one dose).
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
	 * Sets the value(s) for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public Dosage setQuantity(QuantityDt theValue) {
		myQuantity = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public Dosage setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public Dosage setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public Dosage setQuantity( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public Dosage setQuantity( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myQuantity = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public Dosage setQuantity( double theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of therapeutic or other substance given at one administration event.
     * </p> 
	 */
	public Dosage setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>rate</b> (Dose quantity per unit of time).
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
	 * Sets the value(s) for <b>rate</b> (Dose quantity per unit of time)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.
     * </p> 
	 */
	public Dosage setRate(RatioDt theValue) {
		myRate = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>maxDosePerPeriod</b> (Maximum dose that was consumed per unit of time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public RatioDt getMaxDosePerPeriod() {  
		if (myMaxDosePerPeriod == null) {
			myMaxDosePerPeriod = new RatioDt();
		}
		return myMaxDosePerPeriod;
	}

	/**
	 * Sets the value(s) for <b>maxDosePerPeriod</b> (Maximum dose that was consumed per unit of time)
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public Dosage setMaxDosePerPeriod(RatioDt theValue) {
		myMaxDosePerPeriod = theValue;
		return this;
	}

  

	}




}