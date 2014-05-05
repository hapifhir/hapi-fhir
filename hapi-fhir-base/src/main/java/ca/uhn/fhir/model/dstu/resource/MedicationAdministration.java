















package ca.uhn.fhir.model.dstu.resource;

/*
 * #%L
 * HAPI FHIR Library
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

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
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
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.MedicationAdministrationStatusEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;


/**
 * HAPI/FHIR <b>MedicationAdministration</b> Resource
 * (Administration of medication to a patient)
 *
 * <p>
 * <b>Definition:</b>
 * Describes the event of a patient being given a dose of a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/MedicationAdministration">http://hl7.org/fhir/profiles/MedicationAdministration</a> 
 * </p>
 *
 */
@ResourceDef(name="MedicationAdministration", profile="http://hl7.org/fhir/profiles/MedicationAdministration", id="medicationadministration")
public class MedicationAdministration extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>device</b>
	 * <p>
	 * Description: <b>Return administrations with this administration device identity</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.device</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="device", path="MedicationAdministration.device", description="Return administrations with this administration device identity")
	public static final String SP_DEVICE = "device";

	/**
	 * Search parameter constant for <b>encounter</b>
	 * <p>
	 * Description: <b>Return administrations that share this encounter</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.encounter</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="encounter", path="MedicationAdministration.encounter", description="Return administrations that share this encounter")
	public static final String SP_ENCOUNTER = "encounter";

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>Return administrations with this external identity</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="MedicationAdministration.identifier", description="Return administrations with this external identity")
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>medication</b>
	 * <p>
	 * Description: <b>Return administrations of this medication</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.medication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="medication", path="MedicationAdministration.medication", description="Return administrations of this medication")
	public static final String SP_MEDICATION = "medication";

	/**
	 * Search parameter constant for <b>notgiven</b>
	 * <p>
	 * Description: <b>Administrations that were not made</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.wasNotGiven</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="notgiven", path="MedicationAdministration.wasNotGiven", description="Administrations that were not made")
	public static final String SP_NOTGIVEN = "notgiven";

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b>The identity of a patient to list administrations  for</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="MedicationAdministration.patient", description="The identity of a patient to list administrations  for")
	public static final String SP_PATIENT = "patient";

	/**
	 * Search parameter constant for <b>prescription</b>
	 * <p>
	 * Description: <b>The identity of a prescription to list administrations from</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>MedicationAdministration.prescription</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="prescription", path="MedicationAdministration.prescription", description="The identity of a prescription to list administrations from")
	public static final String SP_PRESCRIPTION = "prescription";

	/**
	 * Search parameter constant for <b>status</b>
	 * <p>
	 * Description: <b>MedicationAdministration event status (for example one of active/paused/completed/nullified)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>MedicationAdministration.status</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="status", path="MedicationAdministration.status", description="MedicationAdministration event status (for example one of active/paused/completed/nullified)")
	public static final String SP_STATUS = "status";

	/**
	 * Search parameter constant for <b>whengiven</b>
	 * <p>
	 * Description: <b>Date of administration</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>MedicationAdministration.whenGiven</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="whengiven", path="MedicationAdministration.whenGiven", description="Date of administration")
	public static final String SP_WHENGIVEN = "whengiven";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="External identifier",
		formalDefinition="External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="status", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="in progress | on hold | completed | entered in error | stopped",
		formalDefinition="Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way."
	)
	private BoundCodeDt<MedicationAdministrationStatusEnum> myStatus;
	
	@Child(name="patient", order=2, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Who received medication?",
		formalDefinition="The person or animal to whom the medication was given."
	)
	private ResourceReferenceDt myPatient;
	
	@Child(name="practitioner", order=3, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Who administered substance?",
		formalDefinition="The individual who was responsible for giving the medication to the patient."
	)
	private ResourceReferenceDt myPractitioner;
	
	@Child(name="encounter", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Encounter.class	})
	@Description(
		shortDefinition="Encounter administered as part of",
		formalDefinition="The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of."
	)
	private ResourceReferenceDt myEncounter;
	
	@Child(name="prescription", order=5, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.MedicationPrescription.class	})
	@Description(
		shortDefinition="Order administration performed against",
		formalDefinition="The original request, instruction or authority to perform the administration."
	)
	private ResourceReferenceDt myPrescription;
	
	@Child(name="wasNotGiven", type=BooleanDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="True if medication not administered",
		formalDefinition="Set this to true if the record is saying that the medication was NOT administered."
	)
	private BooleanDt myWasNotGiven;
	
	@Child(name="reasonNotGiven", type=CodeableConceptDt.class, order=7, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Reason administration not performed",
		formalDefinition="A code indicating why the administration was not performed."
	)
	private java.util.List<CodeableConceptDt> myReasonNotGiven;
	
	@Child(name="whenGiven", type=PeriodDt.class, order=8, min=1, max=1)	
	@Description(
		shortDefinition="Start and end time of administration",
		formalDefinition="An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same."
	)
	private PeriodDt myWhenGiven;
	
	@Child(name="medication", order=9, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Medication.class	})
	@Description(
		shortDefinition="What was administered?",
		formalDefinition="Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications."
	)
	private ResourceReferenceDt myMedication;
	
	@Child(name="device", order=10, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Device.class	})
	@Description(
		shortDefinition="Device used to administer",
		formalDefinition="The device used in administering the medication to the patient.  E.g. a particular infusion pump"
	)
	private java.util.List<ResourceReferenceDt> myDevice;
	
	@Child(name="dosage", order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Medicine administration instructions to the patient/carer",
		formalDefinition="Provides details of how much of the medication was administered"
	)
	private java.util.List<Dosage> myDosage;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myStatus,  myPatient,  myPractitioner,  myEncounter,  myPrescription,  myWasNotGiven,  myReasonNotGiven,  myWhenGiven,  myMedication,  myDevice,  myDosage);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myStatus, myPatient, myPractitioner, myEncounter, myPrescription, myWasNotGiven, myReasonNotGiven, myWhenGiven, myMedication, myDevice, myDosage);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (External identifier).
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
	 * Sets the value(s) for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
	 */
	public MedicationAdministration setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (External identifier)
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
	 * Gets the first repetition for <b>identifier</b> (External identifier),
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
	 * Adds a new value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public MedicationAdministration addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (External identifier)
	 *
     * <p>
     * <b>Definition:</b>
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public MedicationAdministration addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public BoundCodeDt<MedicationAdministrationStatusEnum> getStatus() {  
		if (myStatus == null) {
			myStatus = new BoundCodeDt<MedicationAdministrationStatusEnum>(MedicationAdministrationStatusEnum.VALUESET_BINDER);
		}
		return myStatus;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped)
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public MedicationAdministration setStatus(BoundCodeDt<MedicationAdministrationStatusEnum> theValue) {
		myStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>status</b> (in progress | on hold | completed | entered in error | stopped)
	 *
     * <p>
     * <b>Definition:</b>
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     * </p> 
	 */
	public MedicationAdministration setStatus(MedicationAdministrationStatusEnum theValue) {
		getStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> (Who received medication?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The person or animal to whom the medication was given.
     * </p> 
	 */
	public ResourceReferenceDt getPatient() {  
		if (myPatient == null) {
			myPatient = new ResourceReferenceDt();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Who received medication?)
	 *
     * <p>
     * <b>Definition:</b>
     * The person or animal to whom the medication was given.
     * </p> 
	 */
	public MedicationAdministration setPatient(ResourceReferenceDt theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>practitioner</b> (Who administered substance?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The individual who was responsible for giving the medication to the patient.
     * </p> 
	 */
	public ResourceReferenceDt getPractitioner() {  
		if (myPractitioner == null) {
			myPractitioner = new ResourceReferenceDt();
		}
		return myPractitioner;
	}

	/**
	 * Sets the value(s) for <b>practitioner</b> (Who administered substance?)
	 *
     * <p>
     * <b>Definition:</b>
     * The individual who was responsible for giving the medication to the patient.
     * </p> 
	 */
	public MedicationAdministration setPractitioner(ResourceReferenceDt theValue) {
		myPractitioner = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>encounter</b> (Encounter administered as part of).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of.
     * </p> 
	 */
	public ResourceReferenceDt getEncounter() {  
		if (myEncounter == null) {
			myEncounter = new ResourceReferenceDt();
		}
		return myEncounter;
	}

	/**
	 * Sets the value(s) for <b>encounter</b> (Encounter administered as part of)
	 *
     * <p>
     * <b>Definition:</b>
     * The visit or admission the or other contact between patient and health care provider the medication administration was performed as part of.
     * </p> 
	 */
	public MedicationAdministration setEncounter(ResourceReferenceDt theValue) {
		myEncounter = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>prescription</b> (Order administration performed against).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The original request, instruction or authority to perform the administration.
     * </p> 
	 */
	public ResourceReferenceDt getPrescription() {  
		if (myPrescription == null) {
			myPrescription = new ResourceReferenceDt();
		}
		return myPrescription;
	}

	/**
	 * Sets the value(s) for <b>prescription</b> (Order administration performed against)
	 *
     * <p>
     * <b>Definition:</b>
     * The original request, instruction or authority to perform the administration.
     * </p> 
	 */
	public MedicationAdministration setPrescription(ResourceReferenceDt theValue) {
		myPrescription = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>wasNotGiven</b> (True if medication not administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public BooleanDt getWasNotGiven() {  
		if (myWasNotGiven == null) {
			myWasNotGiven = new BooleanDt();
		}
		return myWasNotGiven;
	}

	/**
	 * Sets the value(s) for <b>wasNotGiven</b> (True if medication not administered)
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public MedicationAdministration setWasNotGiven(BooleanDt theValue) {
		myWasNotGiven = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>wasNotGiven</b> (True if medication not administered)
	 *
     * <p>
     * <b>Definition:</b>
     * Set this to true if the record is saying that the medication was NOT administered.
     * </p> 
	 */
	public MedicationAdministration setWasNotGiven( boolean theBoolean) {
		myWasNotGiven = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>reasonNotGiven</b> (Reason administration not performed).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getReasonNotGiven() {  
		if (myReasonNotGiven == null) {
			myReasonNotGiven = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myReasonNotGiven;
	}

	/**
	 * Sets the value(s) for <b>reasonNotGiven</b> (Reason administration not performed)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public MedicationAdministration setReasonNotGiven(java.util.List<CodeableConceptDt> theValue) {
		myReasonNotGiven = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>reasonNotGiven</b> (Reason administration not performed)
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public CodeableConceptDt addReasonNotGiven() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getReasonNotGiven().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>reasonNotGiven</b> (Reason administration not performed),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A code indicating why the administration was not performed.
     * </p> 
	 */
	public CodeableConceptDt getReasonNotGivenFirstRep() {
		if (getReasonNotGiven().isEmpty()) {
			return addReasonNotGiven();
		}
		return getReasonNotGiven().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>whenGiven</b> (Start and end time of administration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same.
     * </p> 
	 */
	public PeriodDt getWhenGiven() {  
		if (myWhenGiven == null) {
			myWhenGiven = new PeriodDt();
		}
		return myWhenGiven;
	}

	/**
	 * Sets the value(s) for <b>whenGiven</b> (Start and end time of administration)
	 *
     * <p>
     * <b>Definition:</b>
     * An interval of time during which the administration took place.  For many administrations, such as swallowing a tablet the lower and upper values of the interval will be the same.
     * </p> 
	 */
	public MedicationAdministration setWhenGiven(PeriodDt theValue) {
		myWhenGiven = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>medication</b> (What was administered?).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public ResourceReferenceDt getMedication() {  
		if (myMedication == null) {
			myMedication = new ResourceReferenceDt();
		}
		return myMedication;
	}

	/**
	 * Sets the value(s) for <b>medication</b> (What was administered?)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p> 
	 */
	public MedicationAdministration setMedication(ResourceReferenceDt theValue) {
		myMedication = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>device</b> (Device used to administer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getDevice() {  
		if (myDevice == null) {
			myDevice = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myDevice;
	}

	/**
	 * Sets the value(s) for <b>device</b> (Device used to administer)
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public MedicationAdministration setDevice(java.util.List<ResourceReferenceDt> theValue) {
		myDevice = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>device</b> (Device used to administer)
	 *
     * <p>
     * <b>Definition:</b>
     * The device used in administering the medication to the patient.  E.g. a particular infusion pump
     * </p> 
	 */
	public ResourceReferenceDt addDevice() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getDevice().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>dosage</b> (Medicine administration instructions to the patient/carer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public java.util.List<Dosage> getDosage() {  
		if (myDosage == null) {
			myDosage = new java.util.ArrayList<Dosage>();
		}
		return myDosage;
	}

	/**
	 * Sets the value(s) for <b>dosage</b> (Medicine administration instructions to the patient/carer)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public MedicationAdministration setDosage(java.util.List<Dosage> theValue) {
		myDosage = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>dosage</b> (Medicine administration instructions to the patient/carer)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public Dosage addDosage() {
		Dosage newType = new Dosage();
		getDosage().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>dosage</b> (Medicine administration instructions to the patient/carer),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	public Dosage getDosageFirstRep() {
		if (getDosage().isEmpty()) {
			return addDosage();
		}
		return getDosage().get(0); 
	}
  
	/**
	 * Block class for child element: <b>MedicationAdministration.dosage</b> (Medicine administration instructions to the patient/carer)
	 *
     * <p>
     * <b>Definition:</b>
     * Provides details of how much of the medication was administered
     * </p> 
	 */
	@Block()	
	public static class Dosage extends BaseElement implements IResourceBlock {
	
	@Child(name="timing", order=0, min=0, max=1, type={
		DateTimeDt.class, 		PeriodDt.class	})
	@Description(
		shortDefinition="When dose(s) were given",
		formalDefinition="The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)"
	)
	private IDatatype myTiming;
	
	@Child(name="asNeeded", order=1, min=0, max=1, type={
		BooleanDt.class, 		CodeableConceptDt.class	})
	@Description(
		shortDefinition="Take \"as needed\" f(or x)",
		formalDefinition="If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication"
	)
	private IDatatype myAsNeeded;
	
	@Child(name="site", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Body site administered to",
		formalDefinition="A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\""
	)
	private CodeableConceptDt mySite;
	
	@Child(name="route", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Path of substance into body",
		formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc."
	)
	private CodeableConceptDt myRoute;
	
	@Child(name="method", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="How drug was administered",
		formalDefinition="A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration."
	)
	private CodeableConceptDt myMethod;
	
	@Child(name="quantity", type=QuantityDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Amount administered in one dose",
		formalDefinition="The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection."
	)
	private QuantityDt myQuantity;
	
	@Child(name="rate", type=RatioDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="Dose quantity per unit of time",
		formalDefinition="Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity."
	)
	private RatioDt myRate;
	
	@Child(name="maxDosePerPeriod", type=RatioDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Total dose that was consumed per unit of time",
		formalDefinition="The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours."
	)
	private RatioDt myMaxDosePerPeriod;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myTiming,  myAsNeeded,  mySite,  myRoute,  myMethod,  myQuantity,  myRate,  myMaxDosePerPeriod);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myTiming, myAsNeeded, mySite, myRoute, myMethod, myQuantity, myRate, myMaxDosePerPeriod);
	}

	/**
	 * Gets the value(s) for <b>timing[x]</b> (When dose(s) were given).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)
     * </p> 
	 */
	public IDatatype getTiming() {  
		return myTiming;
	}

	/**
	 * Sets the value(s) for <b>timing[x]</b> (When dose(s) were given)
	 *
     * <p>
     * <b>Definition:</b>
     * The timing schedule for giving the medication to the patient.  This may be a single time point (using dateTime) or it may be a start and end dateTime (Period)
     * </p> 
	 */
	public Dosage setTiming(IDatatype theValue) {
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
	 * Gets the value(s) for <b>site</b> (Body site administered to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\"
     * </p> 
	 */
	public CodeableConceptDt getSite() {  
		if (mySite == null) {
			mySite = new CodeableConceptDt();
		}
		return mySite;
	}

	/**
	 * Sets the value(s) for <b>site</b> (Body site administered to)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded specification of the anatomic site where the medication first entered the body.  E.g. \"left arm\"
     * </p> 
	 */
	public Dosage setSite(CodeableConceptDt theValue) {
		mySite = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>route</b> (Path of substance into body).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc.
     * </p> 
	 */
	public CodeableConceptDt getRoute() {  
		if (myRoute == null) {
			myRoute = new CodeableConceptDt();
		}
		return myRoute;
	}

	/**
	 * Sets the value(s) for <b>route</b> (Path of substance into body)
	 *
     * <p>
     * <b>Definition:</b>
     * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.   E.g. topical, intravenous, etc.
     * </p> 
	 */
	public Dosage setRoute(CodeableConceptDt theValue) {
		myRoute = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>method</b> (How drug was administered).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
     * </p> 
	 */
	public CodeableConceptDt getMethod() {  
		if (myMethod == null) {
			myMethod = new CodeableConceptDt();
		}
		return myMethod;
	}

	/**
	 * Sets the value(s) for <b>method</b> (How drug was administered)
	 *
     * <p>
     * <b>Definition:</b>
     * A coded value indicating the method by which the medication was introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
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
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
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
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
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
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
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
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
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
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity( long theValue) {
		myQuantity = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>quantity</b> (Amount administered in one dose)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
     * </p> 
	 */
	public Dosage setQuantity( double theValue) {
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
     * Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity.
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
     * Identifies the speed with which the medication was introduced into the patient. Typically the rate for an infusion e.g. 200ml in 2 hours.  May also expressed as a rate per unit of time such as 100ml per hour - the duration is then not specified, or is specified in the quantity.
     * </p> 
	 */
	public Dosage setRate(RatioDt theValue) {
		myRate = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>maxDosePerPeriod</b> (Total dose that was consumed per unit of time).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public RatioDt getMaxDosePerPeriod() {  
		if (myMaxDosePerPeriod == null) {
			myMaxDosePerPeriod = new RatioDt();
		}
		return myMaxDosePerPeriod;
	}

	/**
	 * Sets the value(s) for <b>maxDosePerPeriod</b> (Total dose that was consumed per unit of time)
	 *
     * <p>
     * <b>Definition:</b>
     * The maximum total quantity of a therapeutic substance that was administered to the patient over the specified period of time. E.g. 1000mg in 24 hours.
     * </p> 
	 */
	public Dosage setMaxDosePerPeriod(RatioDt theValue) {
		myMaxDosePerPeriod = theValue;
		return this;
	}

  

	}




}
