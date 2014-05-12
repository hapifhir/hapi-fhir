















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


import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BaseResource;
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
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.AnimalSpeciesEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.LinkTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.gclient.DateParam;
import ca.uhn.fhir.rest.gclient.Include;
import ca.uhn.fhir.rest.gclient.ReferenceParam;
import ca.uhn.fhir.rest.gclient.StringParam;
import ca.uhn.fhir.rest.gclient.TokenParam;


/**
 * HAPI/FHIR <b>Patient</b> Resource
 * (Information about a person or animal receiving health care services)
 *
 * <p>
 * <b>Definition:</b>
 * Demographics and other administrative information about a person or animal receiving care or other health-related services
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Tracking patient is the center of the healthcare process
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Patient">http://hl7.org/fhir/profiles/Patient</a> 
 * </p>
 *
 */
@ResourceDef(name="Patient", profile="http://hl7.org/fhir/profiles/Patient", id="patient")
public class Patient extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>A patient identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.identifier</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="identifier", path="Patient.identifier", description="A patient identifier")
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>A patient identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.identifier</b><br/>
	 * </p>
	 */
	public static final TokenParam IDENTIFIER = new TokenParam(SP_IDENTIFIER);

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of either family or given name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="Patient.name", description="A portion of either family or given name of the patient")
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>A portion of either family or given name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name</b><br/>
	 * </p>
	 */
	public static final StringParam NAME = new StringParam(SP_NAME);

	/**
	 * Search parameter constant for <b>family</b>
	 * <p>
	 * Description: <b>A portion of the family name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name.family</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="family", path="Patient.name.family", description="A portion of the family name of the patient")
	public static final String SP_FAMILY = "family";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>family</b>
	 * <p>
	 * Description: <b>A portion of the family name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name.family</b><br/>
	 * </p>
	 */
	public static final StringParam FAMILY = new StringParam(SP_FAMILY);

	/**
	 * Search parameter constant for <b>given</b>
	 * <p>
	 * Description: <b>A portion of the given name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name.given</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="given", path="Patient.name.given", description="A portion of the given name of the patient")
	public static final String SP_GIVEN = "given";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>given</b>
	 * <p>
	 * Description: <b>A portion of the given name of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.name.given</b><br/>
	 * </p>
	 */
	public static final StringParam GIVEN = new StringParam(SP_GIVEN);

	/**
	 * Search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="phonetic", path="", description="A portion of either family or given name using some kind of phonetic matching algorithm")
	public static final String SP_PHONETIC = "phonetic";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b></b><br/>
	 * </p>
	 */
	public static final StringParam PHONETIC = new StringParam(SP_PHONETIC);

	/**
	 * Search parameter constant for <b>telecom</b>
	 * <p>
	 * Description: <b>The value in any kind of telecom details of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.telecom</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="telecom", path="Patient.telecom", description="The value in any kind of telecom details of the patient")
	public static final String SP_TELECOM = "telecom";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>telecom</b>
	 * <p>
	 * Description: <b>The value in any kind of telecom details of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.telecom</b><br/>
	 * </p>
	 */
	public static final StringParam TELECOM = new StringParam(SP_TELECOM);

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>An address in any kind of address/part of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.address</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="address", path="Patient.address", description="An address in any kind of address/part of the patient")
	public static final String SP_ADDRESS = "address";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>An address in any kind of address/part of the patient</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>Patient.address</b><br/>
	 * </p>
	 */
	public static final StringParam ADDRESS = new StringParam(SP_ADDRESS);

	/**
	 * Search parameter constant for <b>gender</b>
	 * <p>
	 * Description: <b>Gender of the patient</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.gender</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="gender", path="Patient.gender", description="Gender of the patient")
	public static final String SP_GENDER = "gender";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>gender</b>
	 * <p>
	 * Description: <b>Gender of the patient</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.gender</b><br/>
	 * </p>
	 */
	public static final TokenParam GENDER = new TokenParam(SP_GENDER);

	/**
	 * Search parameter constant for <b>language</b>
	 * <p>
	 * Description: <b>Language code (irrespective of use value)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.communication</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="language", path="Patient.communication", description="Language code (irrespective of use value)")
	public static final String SP_LANGUAGE = "language";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>language</b>
	 * <p>
	 * Description: <b>Language code (irrespective of use value)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.communication</b><br/>
	 * </p>
	 */
	public static final TokenParam LANGUAGE = new TokenParam(SP_LANGUAGE);

	/**
	 * Search parameter constant for <b>birthdate</b>
	 * <p>
	 * Description: <b>The patient's date of birth</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Patient.birthDate</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="birthdate", path="Patient.birthDate", description="The patient's date of birth")
	public static final String SP_BIRTHDATE = "birthdate";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>birthdate</b>
	 * <p>
	 * Description: <b>The patient's date of birth</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>Patient.birthDate</b><br/>
	 * </p>
	 */
	public static final DateParam BIRTHDATE = new DateParam(SP_BIRTHDATE);

	/**
	 * Search parameter constant for <b>provider</b>
	 * <p>
	 * Description: <b>The organization at which this person is a patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.managingOrganization</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="provider", path="Patient.managingOrganization", description="The organization at which this person is a patient")
	public static final String SP_PROVIDER = "provider";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>provider</b>
	 * <p>
	 * Description: <b>The organization at which this person is a patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.managingOrganization</b><br/>
	 * </p>
	 */
	public static final ReferenceParam PROVIDER = new ReferenceParam(SP_PROVIDER);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.managingOrganization</b>".
	 */
	public static final Include INCLUDE_MANAGINGORGANIZATION = new Include("Patient.managingOrganization");

	/**
	 * Search parameter constant for <b>active</b>
	 * <p>
	 * Description: <b>Whether the patient record is active</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.active</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="active", path="Patient.active", description="Whether the patient record is active")
	public static final String SP_ACTIVE = "active";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>active</b>
	 * <p>
	 * Description: <b>Whether the patient record is active</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.active</b><br/>
	 * </p>
	 */
	public static final TokenParam ACTIVE = new TokenParam(SP_ACTIVE);

	/**
	 * Search parameter constant for <b>animal-species</b>
	 * <p>
	 * Description: <b>The species for animal patients</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.animal.species</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="animal-species", path="Patient.animal.species", description="The species for animal patients")
	public static final String SP_ANIMAL_SPECIES = "animal-species";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>animal-species</b>
	 * <p>
	 * Description: <b>The species for animal patients</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.animal.species</b><br/>
	 * </p>
	 */
	public static final TokenParam ANIMAL_SPECIES = new TokenParam(SP_ANIMAL_SPECIES);

	/**
	 * Search parameter constant for <b>animal-breed</b>
	 * <p>
	 * Description: <b>The breed for animal patients</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.animal.breed</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="animal-breed", path="Patient.animal.breed", description="The breed for animal patients")
	public static final String SP_ANIMAL_BREED = "animal-breed";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>animal-breed</b>
	 * <p>
	 * Description: <b>The breed for animal patients</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Patient.animal.breed</b><br/>
	 * </p>
	 */
	public static final TokenParam ANIMAL_BREED = new TokenParam(SP_ANIMAL_BREED);

	/**
	 * Search parameter constant for <b>link</b>
	 * <p>
	 * Description: <b>All patients linked to the given patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.link.other</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="link", path="Patient.link.other", description="All patients linked to the given patient")
	public static final String SP_LINK = "link";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>link</b>
	 * <p>
	 * Description: <b>All patients linked to the given patient</b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>Patient.link.other</b><br/>
	 * </p>
	 */
	public static final ReferenceParam LINK = new ReferenceParam(SP_LINK);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>Patient.link.other</b>".
	 */
	public static final Include INCLUDE_LINK_OTHER = new Include("Patient.link.other");


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="An identifier for the person as this patient",
		formalDefinition="An identifier that applies to this person as a patient"
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A name associated with the patient",
		formalDefinition="A name associated with the individual."
	)
	private java.util.List<HumanNameDt> myName;
	
	@Child(name="telecom", type=ContactDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A contact detail for the individual",
		formalDefinition="A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted."
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="gender", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Gender for administrative purposes",
		formalDefinition="Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes."
	)
	private BoundCodeableConceptDt<AdministrativeGenderCodesEnum> myGender;
	
	@Child(name="birthDate", type=DateTimeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="The date and time of birth for the individual",
		formalDefinition="The date and time of birth for the individual"
	)
	private DateTimeDt myBirthDate;
	
	@Child(name="deceased", order=5, min=0, max=1, type={
		BooleanDt.class, 		DateTimeDt.class	})
	@Description(
		shortDefinition="Indicates if the individual is deceased or not",
		formalDefinition="Indicates if the individual is deceased or not"
	)
	private IDatatype myDeceased;
	
	@Child(name="address", type=AddressDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Addresses for the individual",
		formalDefinition="Addresses for the individual"
	)
	private java.util.List<AddressDt> myAddress;
	
	@Child(name="maritalStatus", type=CodeableConceptDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Marital (civil) status of a person",
		formalDefinition="This field contains a patient's most recent marital (civil) status."
	)
	private BoundCodeableConceptDt<MaritalStatusCodesEnum> myMaritalStatus;
	
	@Child(name="multipleBirth", order=8, min=0, max=1, type={
		BooleanDt.class, 		IntegerDt.class	})
	@Description(
		shortDefinition="Whether patient is part of a multiple birth",
		formalDefinition="Indicates whether the patient is part of a multiple or indicates the actual birth order."
	)
	private IDatatype myMultipleBirth;
	
	@Child(name="photo", type=AttachmentDt.class, order=9, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Image of the person",
		formalDefinition="Image of the person"
	)
	private java.util.List<AttachmentDt> myPhoto;
	
	@Child(name="contact", order=10, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A contact party (e.g. guardian, partner, friend) for the patient",
		formalDefinition="A contact party (e.g. guardian, partner, friend) for the patient"
	)
	private java.util.List<Contact> myContact;
	
	@Child(name="animal", order=11, min=0, max=1)	
	@Description(
		shortDefinition="If this patient is an animal (non-human)",
		formalDefinition="This element has a value if the patient is an animal"
	)
	private Animal myAnimal;
	
	@Child(name="communication", type=CodeableConceptDt.class, order=12, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Languages which may be used to communicate with the patient about his or her health",
		formalDefinition="Languages which may be used to communicate with the patient about his or her health"
	)
	private java.util.List<CodeableConceptDt> myCommunication;
	
	@Child(name="careProvider", order=13, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class, 		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Patient's nominated care provider",
		formalDefinition="Patient's nominated care provider"
	)
	private java.util.List<ResourceReferenceDt> myCareProvider;
	
	@Child(name="managingOrganization", order=14, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Organization that is the custodian of the patient record",
		formalDefinition="Organization that is the custodian of the patient record"
	)
	private ResourceReferenceDt myManagingOrganization;
	
	@Child(name="link", order=15, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Link to another patient resource that concerns the same actual person",
		formalDefinition="Link to another patient resource that concerns the same actual person"
	)
	private java.util.List<Link> myLink;
	
	@Child(name="active", type=BooleanDt.class, order=16, min=0, max=1)	
	@Description(
		shortDefinition="Whether this patient's record is in active use",
		formalDefinition="Whether this patient record is in active use"
	)
	private BooleanDt myActive;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myTelecom,  myGender,  myBirthDate,  myDeceased,  myAddress,  myMaritalStatus,  myMultipleBirth,  myPhoto,  myContact,  myAnimal,  myCommunication,  myCareProvider,  myManagingOrganization,  myLink,  myActive);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myTelecom, myGender, myBirthDate, myDeceased, myAddress, myMaritalStatus, myMultipleBirth, myPhoto, myContact, myAnimal, myCommunication, myCareProvider, myManagingOrganization, myLink, myActive);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (An identifier for the person as this patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (An identifier for the person as this patient)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
     * </p> 
	 */
	public Patient setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (An identifier for the person as this patient)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (An identifier for the person as this patient),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (An identifier for the person as this patient)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Patient addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (An identifier for the person as this patient)
	 *
     * <p>
     * <b>Definition:</b>
     * An identifier that applies to this person as a patient
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Patient addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>name</b> (A name associated with the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the individual.
     * </p> 
	 */
	public java.util.List<HumanNameDt> getName() {  
		if (myName == null) {
			myName = new java.util.ArrayList<HumanNameDt>();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (A name associated with the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the individual.
     * </p> 
	 */
	public Patient setName(java.util.List<HumanNameDt> theValue) {
		myName = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>name</b> (A name associated with the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the individual.
     * </p> 
	 */
	public HumanNameDt addName() {
		HumanNameDt newType = new HumanNameDt();
		getName().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>name</b> (A name associated with the patient),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the individual.
     * </p> 
	 */
	public HumanNameDt getNameFirstRep() {
		if (getName().isEmpty()) {
			return addName();
		}
		return getName().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>telecom</b> (A contact detail for the individual).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (A contact detail for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
     * </p> 
	 */
	public Patient setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (A contact detail for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (A contact detail for the individual),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
     * </p> 
	 */
	public ContactDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>gender</b> (Gender for administrative purposes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public BoundCodeableConceptDt<AdministrativeGenderCodesEnum> getGender() {  
		if (myGender == null) {
			myGender = new BoundCodeableConceptDt<AdministrativeGenderCodesEnum>(AdministrativeGenderCodesEnum.VALUESET_BINDER);
		}
		return myGender;
	}

	/**
	 * Sets the value(s) for <b>gender</b> (Gender for administrative purposes)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Patient setGender(BoundCodeableConceptDt<AdministrativeGenderCodesEnum> theValue) {
		myGender = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>gender</b> (Gender for administrative purposes)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Patient setGender(AdministrativeGenderCodesEnum theValue) {
		getGender().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>birthDate</b> (The date and time of birth for the individual).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the individual
     * </p> 
	 */
	public DateTimeDt getBirthDate() {  
		if (myBirthDate == null) {
			myBirthDate = new DateTimeDt();
		}
		return myBirthDate;
	}

	/**
	 * Sets the value(s) for <b>birthDate</b> (The date and time of birth for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the individual
     * </p> 
	 */
	public Patient setBirthDate(DateTimeDt theValue) {
		myBirthDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>birthDate</b> (The date and time of birth for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the individual
     * </p> 
	 */
	public Patient setBirthDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myBirthDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>birthDate</b> (The date and time of birth for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * The date and time of birth for the individual
     * </p> 
	 */
	public Patient setBirthDateWithSecondsPrecision( Date theDate) {
		myBirthDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>deceased[x]</b> (Indicates if the individual is deceased or not).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the individual is deceased or not
     * </p> 
	 */
	public IDatatype getDeceased() {  
		return myDeceased;
	}

	/**
	 * Sets the value(s) for <b>deceased[x]</b> (Indicates if the individual is deceased or not)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the individual is deceased or not
     * </p> 
	 */
	public Patient setDeceased(IDatatype theValue) {
		myDeceased = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>address</b> (Addresses for the individual).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Addresses for the individual
     * </p> 
	 */
	public java.util.List<AddressDt> getAddress() {  
		if (myAddress == null) {
			myAddress = new java.util.ArrayList<AddressDt>();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (Addresses for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * Addresses for the individual
     * </p> 
	 */
	public Patient setAddress(java.util.List<AddressDt> theValue) {
		myAddress = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>address</b> (Addresses for the individual)
	 *
     * <p>
     * <b>Definition:</b>
     * Addresses for the individual
     * </p> 
	 */
	public AddressDt addAddress() {
		AddressDt newType = new AddressDt();
		getAddress().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>address</b> (Addresses for the individual),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Addresses for the individual
     * </p> 
	 */
	public AddressDt getAddressFirstRep() {
		if (getAddress().isEmpty()) {
			return addAddress();
		}
		return getAddress().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>maritalStatus</b> (Marital (civil) status of a person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains a patient's most recent marital (civil) status.
     * </p> 
	 */
	public BoundCodeableConceptDt<MaritalStatusCodesEnum> getMaritalStatus() {  
		if (myMaritalStatus == null) {
			myMaritalStatus = new BoundCodeableConceptDt<MaritalStatusCodesEnum>(MaritalStatusCodesEnum.VALUESET_BINDER);
		}
		return myMaritalStatus;
	}

	/**
	 * Sets the value(s) for <b>maritalStatus</b> (Marital (civil) status of a person)
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains a patient's most recent marital (civil) status.
     * </p> 
	 */
	public Patient setMaritalStatus(BoundCodeableConceptDt<MaritalStatusCodesEnum> theValue) {
		myMaritalStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>maritalStatus</b> (Marital (civil) status of a person)
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains a patient's most recent marital (civil) status.
     * </p> 
	 */
	public Patient setMaritalStatus(MaritalStatusCodesEnum theValue) {
		getMaritalStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>multipleBirth[x]</b> (Whether patient is part of a multiple birth).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the patient is part of a multiple or indicates the actual birth order.
     * </p> 
	 */
	public IDatatype getMultipleBirth() {  
		return myMultipleBirth;
	}

	/**
	 * Sets the value(s) for <b>multipleBirth[x]</b> (Whether patient is part of a multiple birth)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether the patient is part of a multiple or indicates the actual birth order.
     * </p> 
	 */
	public Patient setMultipleBirth(IDatatype theValue) {
		myMultipleBirth = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>photo</b> (Image of the person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public java.util.List<AttachmentDt> getPhoto() {  
		if (myPhoto == null) {
			myPhoto = new java.util.ArrayList<AttachmentDt>();
		}
		return myPhoto;
	}

	/**
	 * Sets the value(s) for <b>photo</b> (Image of the person)
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public Patient setPhoto(java.util.List<AttachmentDt> theValue) {
		myPhoto = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>photo</b> (Image of the person)
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public AttachmentDt addPhoto() {
		AttachmentDt newType = new AttachmentDt();
		getPhoto().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>photo</b> (Image of the person),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Image of the person
     * </p> 
	 */
	public AttachmentDt getPhotoFirstRep() {
		if (getPhoto().isEmpty()) {
			return addPhoto();
		}
		return getPhoto().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>contact</b> (A contact party (e.g. guardian, partner, friend) for the patient).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public java.util.List<Contact> getContact() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<Contact>();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> (A contact party (e.g. guardian, partner, friend) for the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public Patient setContact(java.util.List<Contact> theValue) {
		myContact = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>contact</b> (A contact party (e.g. guardian, partner, friend) for the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public Contact addContact() {
		Contact newType = new Contact();
		getContact().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>contact</b> (A contact party (e.g. guardian, partner, friend) for the patient),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	public Contact getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>animal</b> (If this patient is an animal (non-human)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This element has a value if the patient is an animal
     * </p> 
	 */
	public Animal getAnimal() {  
		if (myAnimal == null) {
			myAnimal = new Animal();
		}
		return myAnimal;
	}

	/**
	 * Sets the value(s) for <b>animal</b> (If this patient is an animal (non-human))
	 *
     * <p>
     * <b>Definition:</b>
     * This element has a value if the patient is an animal
     * </p> 
	 */
	public Patient setAnimal(Animal theValue) {
		myAnimal = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>communication</b> (Languages which may be used to communicate with the patient about his or her health).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Languages which may be used to communicate with the patient about his or her health
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getCommunication() {  
		if (myCommunication == null) {
			myCommunication = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myCommunication;
	}

	/**
	 * Sets the value(s) for <b>communication</b> (Languages which may be used to communicate with the patient about his or her health)
	 *
     * <p>
     * <b>Definition:</b>
     * Languages which may be used to communicate with the patient about his or her health
     * </p> 
	 */
	public Patient setCommunication(java.util.List<CodeableConceptDt> theValue) {
		myCommunication = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>communication</b> (Languages which may be used to communicate with the patient about his or her health)
	 *
     * <p>
     * <b>Definition:</b>
     * Languages which may be used to communicate with the patient about his or her health
     * </p> 
	 */
	public CodeableConceptDt addCommunication() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getCommunication().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>communication</b> (Languages which may be used to communicate with the patient about his or her health),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Languages which may be used to communicate with the patient about his or her health
     * </p> 
	 */
	public CodeableConceptDt getCommunicationFirstRep() {
		if (getCommunication().isEmpty()) {
			return addCommunication();
		}
		return getCommunication().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>careProvider</b> (Patient's nominated care provider).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Patient's nominated care provider
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getCareProvider() {  
		return myCareProvider;
	}

	/**
	 * Sets the value(s) for <b>careProvider</b> (Patient's nominated care provider)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient's nominated care provider
     * </p> 
	 */
	public Patient setCareProvider(java.util.List<ResourceReferenceDt> theValue) {
		myCareProvider = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>careProvider</b> (Patient's nominated care provider)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient's nominated care provider
     * </p> 
	 */
	public ResourceReferenceDt addCareProvider() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getCareProvider().add(newType);
		return newType; 
	}
  
	/**
	 * Gets the value(s) for <b>managingOrganization</b> (Organization that is the custodian of the patient record).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that is the custodian of the patient record
     * </p> 
	 */
	public ResourceReferenceDt getManagingOrganization() {  
		if (myManagingOrganization == null) {
			myManagingOrganization = new ResourceReferenceDt();
		}
		return myManagingOrganization;
	}

	/**
	 * Sets the value(s) for <b>managingOrganization</b> (Organization that is the custodian of the patient record)
	 *
     * <p>
     * <b>Definition:</b>
     * Organization that is the custodian of the patient record
     * </p> 
	 */
	public Patient setManagingOrganization(ResourceReferenceDt theValue) {
		myManagingOrganization = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>link</b> (Link to another patient resource that concerns the same actual person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public java.util.List<Link> getLink() {  
		if (myLink == null) {
			myLink = new java.util.ArrayList<Link>();
		}
		return myLink;
	}

	/**
	 * Sets the value(s) for <b>link</b> (Link to another patient resource that concerns the same actual person)
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public Patient setLink(java.util.List<Link> theValue) {
		myLink = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>link</b> (Link to another patient resource that concerns the same actual person)
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public Link addLink() {
		Link newType = new Link();
		getLink().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>link</b> (Link to another patient resource that concerns the same actual person),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	public Link getLinkFirstRep() {
		if (getLink().isEmpty()) {
			return addLink();
		}
		return getLink().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>active</b> (Whether this patient's record is in active use).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this patient record is in active use
     * </p> 
	 */
	public BooleanDt getActive() {  
		if (myActive == null) {
			myActive = new BooleanDt();
		}
		return myActive;
	}

	/**
	 * Sets the value(s) for <b>active</b> (Whether this patient's record is in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this patient record is in active use
     * </p> 
	 */
	public Patient setActive(BooleanDt theValue) {
		myActive = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>active</b> (Whether this patient's record is in active use)
	 *
     * <p>
     * <b>Definition:</b>
     * Whether this patient record is in active use
     * </p> 
	 */
	public Patient setActive( boolean theBoolean) {
		myActive = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Block class for child element: <b>Patient.contact</b> (A contact party (e.g. guardian, partner, friend) for the patient)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact party (e.g. guardian, partner, friend) for the patient
     * </p> 
	 */
	@Block()	
	public static class Contact extends BaseElement implements IResourceBlock {
	
	@Child(name="relationship", type=CodeableConceptDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="The kind of relationship",
		formalDefinition="The nature of the relationship between the patient and the contact person"
	)
	private java.util.List<CodeableConceptDt> myRelationship;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="A name associated with the person",
		formalDefinition="A name associated with the person"
	)
	private HumanNameDt myName;
	
	@Child(name="telecom", type=ContactDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A contact detail for the person",
		formalDefinition="A contact detail for the person, e.g. a telephone number or an email address."
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="address", type=AddressDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Address for the contact person",
		formalDefinition="Address for the contact person"
	)
	private AddressDt myAddress;
	
	@Child(name="gender", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Gender for administrative purposes",
		formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes."
	)
	private BoundCodeableConceptDt<AdministrativeGenderCodesEnum> myGender;
	
	@Child(name="organization", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="Organization that is associated with the contact",
		formalDefinition="Organization on behalf of which the contact is acting or for which the contact is working."
	)
	private ResourceReferenceDt myOrganization;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myRelationship,  myName,  myTelecom,  myAddress,  myGender,  myOrganization);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myRelationship, myName, myTelecom, myAddress, myGender, myOrganization);
	}

	/**
	 * Gets the value(s) for <b>relationship</b> (The kind of relationship).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between the patient and the contact person
     * </p> 
	 */
	public java.util.List<CodeableConceptDt> getRelationship() {  
		if (myRelationship == null) {
			myRelationship = new java.util.ArrayList<CodeableConceptDt>();
		}
		return myRelationship;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (The kind of relationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between the patient and the contact person
     * </p> 
	 */
	public Contact setRelationship(java.util.List<CodeableConceptDt> theValue) {
		myRelationship = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>relationship</b> (The kind of relationship)
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between the patient and the contact person
     * </p> 
	 */
	public CodeableConceptDt addRelationship() {
		CodeableConceptDt newType = new CodeableConceptDt();
		getRelationship().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>relationship</b> (The kind of relationship),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * The nature of the relationship between the patient and the contact person
     * </p> 
	 */
	public CodeableConceptDt getRelationshipFirstRep() {
		if (getRelationship().isEmpty()) {
			return addRelationship();
		}
		return getRelationship().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>name</b> (A name associated with the person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the person
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (A name associated with the person)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the person
     * </p> 
	 */
	public Contact setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>telecom</b> (A contact detail for the person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the person, e.g. a telephone number or an email address.
     * </p> 
	 */
	public java.util.List<ContactDt> getTelecom() {  
		if (myTelecom == null) {
			myTelecom = new java.util.ArrayList<ContactDt>();
		}
		return myTelecom;
	}

	/**
	 * Sets the value(s) for <b>telecom</b> (A contact detail for the person)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the person, e.g. a telephone number or an email address.
     * </p> 
	 */
	public Contact setTelecom(java.util.List<ContactDt> theValue) {
		myTelecom = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>telecom</b> (A contact detail for the person)
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the person, e.g. a telephone number or an email address.
     * </p> 
	 */
	public ContactDt addTelecom() {
		ContactDt newType = new ContactDt();
		getTelecom().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>telecom</b> (A contact detail for the person),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A contact detail for the person, e.g. a telephone number or an email address.
     * </p> 
	 */
	public ContactDt getTelecomFirstRep() {
		if (getTelecom().isEmpty()) {
			return addTelecom();
		}
		return getTelecom().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>address</b> (Address for the contact person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Address for the contact person
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (Address for the contact person)
	 *
     * <p>
     * <b>Definition:</b>
     * Address for the contact person
     * </p> 
	 */
	public Contact setAddress(AddressDt theValue) {
		myAddress = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>gender</b> (Gender for administrative purposes).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public BoundCodeableConceptDt<AdministrativeGenderCodesEnum> getGender() {  
		if (myGender == null) {
			myGender = new BoundCodeableConceptDt<AdministrativeGenderCodesEnum>(AdministrativeGenderCodesEnum.VALUESET_BINDER);
		}
		return myGender;
	}

	/**
	 * Sets the value(s) for <b>gender</b> (Gender for administrative purposes)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Contact setGender(BoundCodeableConceptDt<AdministrativeGenderCodesEnum> theValue) {
		myGender = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>gender</b> (Gender for administrative purposes)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     * </p> 
	 */
	public Contact setGender(AdministrativeGenderCodesEnum theValue) {
		getGender().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>organization</b> (Organization that is associated with the contact).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Organization on behalf of which the contact is acting or for which the contact is working.
     * </p> 
	 */
	public ResourceReferenceDt getOrganization() {  
		if (myOrganization == null) {
			myOrganization = new ResourceReferenceDt();
		}
		return myOrganization;
	}

	/**
	 * Sets the value(s) for <b>organization</b> (Organization that is associated with the contact)
	 *
     * <p>
     * <b>Definition:</b>
     * Organization on behalf of which the contact is acting or for which the contact is working.
     * </p> 
	 */
	public Contact setOrganization(ResourceReferenceDt theValue) {
		myOrganization = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>Patient.animal</b> (If this patient is an animal (non-human))
	 *
     * <p>
     * <b>Definition:</b>
     * This element has a value if the patient is an animal
     * </p> 
	 */
	@Block()	
	public static class Animal extends BaseElement implements IResourceBlock {
	
	@Child(name="species", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="E.g. Dog, Cow",
		formalDefinition="Identifies the high level categorization of the kind of animal"
	)
	private BoundCodeableConceptDt<AnimalSpeciesEnum> mySpecies;
	
	@Child(name="breed", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="E.g. Poodle, Angus",
		formalDefinition="Identifies the detailed categorization of the kind of animal."
	)
	private CodeableConceptDt myBreed;
	
	@Child(name="genderStatus", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="E.g. Neutered, Intact",
		formalDefinition="Indicates the current state of the animal's reproductive organs"
	)
	private CodeableConceptDt myGenderStatus;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySpecies,  myBreed,  myGenderStatus);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, mySpecies, myBreed, myGenderStatus);
	}

	/**
	 * Gets the value(s) for <b>species</b> (E.g. Dog, Cow).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the high level categorization of the kind of animal
     * </p> 
	 */
	public BoundCodeableConceptDt<AnimalSpeciesEnum> getSpecies() {  
		if (mySpecies == null) {
			mySpecies = new BoundCodeableConceptDt<AnimalSpeciesEnum>(AnimalSpeciesEnum.VALUESET_BINDER);
		}
		return mySpecies;
	}

	/**
	 * Sets the value(s) for <b>species</b> (E.g. Dog, Cow)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the high level categorization of the kind of animal
     * </p> 
	 */
	public Animal setSpecies(BoundCodeableConceptDt<AnimalSpeciesEnum> theValue) {
		mySpecies = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>species</b> (E.g. Dog, Cow)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the high level categorization of the kind of animal
     * </p> 
	 */
	public Animal setSpecies(AnimalSpeciesEnum theValue) {
		getSpecies().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>breed</b> (E.g. Poodle, Angus).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the detailed categorization of the kind of animal.
     * </p> 
	 */
	public CodeableConceptDt getBreed() {  
		if (myBreed == null) {
			myBreed = new CodeableConceptDt();
		}
		return myBreed;
	}

	/**
	 * Sets the value(s) for <b>breed</b> (E.g. Poodle, Angus)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the detailed categorization of the kind of animal.
     * </p> 
	 */
	public Animal setBreed(CodeableConceptDt theValue) {
		myBreed = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>genderStatus</b> (E.g. Neutered, Intact).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the current state of the animal's reproductive organs
     * </p> 
	 */
	public CodeableConceptDt getGenderStatus() {  
		if (myGenderStatus == null) {
			myGenderStatus = new CodeableConceptDt();
		}
		return myGenderStatus;
	}

	/**
	 * Sets the value(s) for <b>genderStatus</b> (E.g. Neutered, Intact)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates the current state of the animal's reproductive organs
     * </p> 
	 */
	public Animal setGenderStatus(CodeableConceptDt theValue) {
		myGenderStatus = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>Patient.link</b> (Link to another patient resource that concerns the same actual person)
	 *
     * <p>
     * <b>Definition:</b>
     * Link to another patient resource that concerns the same actual person
     * </p> 
	 */
	@Block()	
	public static class Link extends BaseElement implements IResourceBlock {
	
	@Child(name="other", order=0, min=1, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="The other patient resource that the link refers to",
		formalDefinition="The other patient resource that the link refers to"
	)
	private ResourceReferenceDt myOther;
	
	@Child(name="type", type=CodeDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="replace | refer | seealso - type of link",
		formalDefinition="The type of link between this patient resource and another patient resource."
	)
	private BoundCodeDt<LinkTypeEnum> myType;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myOther,  myType);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myOther, myType);
	}

	/**
	 * Gets the value(s) for <b>other</b> (The other patient resource that the link refers to).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The other patient resource that the link refers to
     * </p> 
	 */
	public ResourceReferenceDt getOther() {  
		if (myOther == null) {
			myOther = new ResourceReferenceDt();
		}
		return myOther;
	}

	/**
	 * Sets the value(s) for <b>other</b> (The other patient resource that the link refers to)
	 *
     * <p>
     * <b>Definition:</b>
     * The other patient resource that the link refers to
     * </p> 
	 */
	public Link setOther(ResourceReferenceDt theValue) {
		myOther = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (replace | refer | seealso - type of link).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of link between this patient resource and another patient resource.
     * </p> 
	 */
	public BoundCodeDt<LinkTypeEnum> getType() {  
		if (myType == null) {
			myType = new BoundCodeDt<LinkTypeEnum>(LinkTypeEnum.VALUESET_BINDER);
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (replace | refer | seealso - type of link)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of link between this patient resource and another patient resource.
     * </p> 
	 */
	public Link setType(BoundCodeDt<LinkTypeEnum> theValue) {
		myType = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>type</b> (replace | refer | seealso - type of link)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of link between this patient resource and another patient resource.
     * </p> 
	 */
	public Link setType(LinkTypeEnum theValue) {
		getType().setValueAsEnum(theValue);
		return this;
	}

  

	}




}
