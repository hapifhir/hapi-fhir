















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
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>Claim</b> Resource
 * (A claim)
 *
 * <p>
 * <b>Definition:</b>
 * A claim
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Claim">http://hl7.org/fhir/profiles/Claim</a> 
 * </p>
 *
 */
@ResourceDef(name="Claim", profile="http://hl7.org/fhir/profiles/Claim", id="claim")
public class Claim extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>number</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Claim.number</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="number", path="Claim.number", description="", type="token")
	public static final String SP_NUMBER = "number";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>number</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>Claim.number</b><br/>
	 * </p>
	 */
	public static final TokenClientParam NUMBER = new TokenClientParam(SP_NUMBER);


	@Child(name="number", type=IdentifierDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Claim/Invoice number",
		formalDefinition="The claim issuer and claim number"
	)
	private IdentifierDt myNumber;
	
	@Child(name="servicedate", type=DateDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Date of Service",
		formalDefinition="The date when the enclosed suite of services were performed or completed"
	)
	private DateDt myServicedate;
	
	@Child(name="provider", order=2, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Responsible practitioner",
		formalDefinition="The practitioner who is responsible for the services rendered to the patient"
	)
	private ResourceReferenceDt myProvider;
	
	@Child(name="billingProvider", order=3, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Provider who is the payee",
		formalDefinition="The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned)"
	)
	private ResourceReferenceDt myBillingProvider;
	
	@Child(name="referrer", order=4, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Practitioner.class	})
	@Description(
		shortDefinition="Provider who is the payee",
		formalDefinition=""
	)
	private ResourceReferenceDt myReferrer;
	
	@Child(name="reason", type=CodeableConceptDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Referal Reason",
		formalDefinition="The reason code for the referral"
	)
	private CodeableConceptDt myReason;
	
	@Child(name="patient", order=6, min=1, max=1)	
	@Description(
		shortDefinition="Patient Details",
		formalDefinition="Patient Details."
	)
	private Patient myPatient;
	
	@Child(name="coverage", order=7, min=0, max=1)	
	@Description(
		shortDefinition="Insurance or medical plan",
		formalDefinition="Financial instrument by which payment information for health care"
	)
	private Coverage myCoverage;
	
	@Child(name="exception", type=CodeableConceptDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="Eligibility exceptions",
		formalDefinition="Factors which may influence the appicability of coverage"
	)
	private CodeableConceptDt myException;
	
	@Child(name="relationship", type=CodeableConceptDt.class, order=9, min=0, max=1)	
	@Description(
		shortDefinition="Patient relationship to subscriber",
		formalDefinition="The relationship of the patient to the subscriber"
	)
	private CodeableConceptDt myRelationship;
	
	@Child(name="school", type=StringDt.class, order=10, min=0, max=1)	
	@Description(
		shortDefinition="Name of School",
		formalDefinition="Name of school"
	)
	private StringDt mySchool;
	
	@Child(name="service", order=11, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Service Details",
		formalDefinition=""
	)
	private java.util.List<Service> myService;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myNumber,  myServicedate,  myProvider,  myBillingProvider,  myReferrer,  myReason,  myPatient,  myCoverage,  myException,  myRelationship,  mySchool,  myService);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myNumber, myServicedate, myProvider, myBillingProvider, myReferrer, myReason, myPatient, myCoverage, myException, myRelationship, mySchool, myService);
	}

	/**
	 * Gets the value(s) for <b>number</b> (Claim/Invoice number).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The claim issuer and claim number
     * </p> 
	 */
	public IdentifierDt getNumber() {  
		if (myNumber == null) {
			myNumber = new IdentifierDt();
		}
		return myNumber;
	}

	/**
	 * Sets the value(s) for <b>number</b> (Claim/Invoice number)
	 *
     * <p>
     * <b>Definition:</b>
     * The claim issuer and claim number
     * </p> 
	 */
	public Claim setNumber(IdentifierDt theValue) {
		myNumber = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>number</b> (Claim/Invoice number)
	 *
     * <p>
     * <b>Definition:</b>
     * The claim issuer and claim number
     * </p> 
	 */
	public Claim setNumber( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myNumber = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>number</b> (Claim/Invoice number)
	 *
     * <p>
     * <b>Definition:</b>
     * The claim issuer and claim number
     * </p> 
	 */
	public Claim setNumber( String theSystem,  String theValue) {
		myNumber = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>servicedate</b> (Date of Service).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the enclosed suite of services were performed or completed
     * </p> 
	 */
	public DateDt getServicedate() {  
		if (myServicedate == null) {
			myServicedate = new DateDt();
		}
		return myServicedate;
	}

	/**
	 * Sets the value(s) for <b>servicedate</b> (Date of Service)
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the enclosed suite of services were performed or completed
     * </p> 
	 */
	public Claim setServicedate(DateDt theValue) {
		myServicedate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>servicedate</b> (Date of Service)
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the enclosed suite of services were performed or completed
     * </p> 
	 */
	public Claim setServicedateWithDayPrecision( Date theDate) {
		myServicedate = new DateDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>servicedate</b> (Date of Service)
	 *
     * <p>
     * <b>Definition:</b>
     * The date when the enclosed suite of services were performed or completed
     * </p> 
	 */
	public Claim setServicedate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myServicedate = new DateDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>provider</b> (Responsible practitioner).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner who is responsible for the services rendered to the patient
     * </p> 
	 */
	public ResourceReferenceDt getProvider() {  
		if (myProvider == null) {
			myProvider = new ResourceReferenceDt();
		}
		return myProvider;
	}

	/**
	 * Sets the value(s) for <b>provider</b> (Responsible practitioner)
	 *
     * <p>
     * <b>Definition:</b>
     * The practitioner who is responsible for the services rendered to the patient
     * </p> 
	 */
	public Claim setProvider(ResourceReferenceDt theValue) {
		myProvider = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>billingProvider</b> (Provider who is the payee).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned)
     * </p> 
	 */
	public ResourceReferenceDt getBillingProvider() {  
		if (myBillingProvider == null) {
			myBillingProvider = new ResourceReferenceDt();
		}
		return myBillingProvider;
	}

	/**
	 * Sets the value(s) for <b>billingProvider</b> (Provider who is the payee)
	 *
     * <p>
     * <b>Definition:</b>
     * The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned)
     * </p> 
	 */
	public Claim setBillingProvider(ResourceReferenceDt theValue) {
		myBillingProvider = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>referrer</b> (Provider who is the payee).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt getReferrer() {  
		if (myReferrer == null) {
			myReferrer = new ResourceReferenceDt();
		}
		return myReferrer;
	}

	/**
	 * Sets the value(s) for <b>referrer</b> (Provider who is the payee)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Claim setReferrer(ResourceReferenceDt theValue) {
		myReferrer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>reason</b> (Referal Reason).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The reason code for the referral
     * </p> 
	 */
	public CodeableConceptDt getReason() {  
		if (myReason == null) {
			myReason = new CodeableConceptDt();
		}
		return myReason;
	}

	/**
	 * Sets the value(s) for <b>reason</b> (Referal Reason)
	 *
     * <p>
     * <b>Definition:</b>
     * The reason code for the referral
     * </p> 
	 */
	public Claim setReason(CodeableConceptDt theValue) {
		myReason = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>patient</b> (Patient Details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Patient Details.
     * </p> 
	 */
	public Patient getPatient() {  
		if (myPatient == null) {
			myPatient = new Patient();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Patient Details)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient Details.
     * </p> 
	 */
	public Claim setPatient(Patient theValue) {
		myPatient = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>coverage</b> (Insurance or medical plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Financial instrument by which payment information for health care
     * </p> 
	 */
	public Coverage getCoverage() {  
		if (myCoverage == null) {
			myCoverage = new Coverage();
		}
		return myCoverage;
	}

	/**
	 * Sets the value(s) for <b>coverage</b> (Insurance or medical plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Financial instrument by which payment information for health care
     * </p> 
	 */
	public Claim setCoverage(Coverage theValue) {
		myCoverage = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>exception</b> (Eligibility exceptions).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Factors which may influence the appicability of coverage
     * </p> 
	 */
	public CodeableConceptDt getException() {  
		if (myException == null) {
			myException = new CodeableConceptDt();
		}
		return myException;
	}

	/**
	 * Sets the value(s) for <b>exception</b> (Eligibility exceptions)
	 *
     * <p>
     * <b>Definition:</b>
     * Factors which may influence the appicability of coverage
     * </p> 
	 */
	public Claim setException(CodeableConceptDt theValue) {
		myException = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>relationship</b> (Patient relationship to subscriber).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The relationship of the patient to the subscriber
     * </p> 
	 */
	public CodeableConceptDt getRelationship() {  
		if (myRelationship == null) {
			myRelationship = new CodeableConceptDt();
		}
		return myRelationship;
	}

	/**
	 * Sets the value(s) for <b>relationship</b> (Patient relationship to subscriber)
	 *
     * <p>
     * <b>Definition:</b>
     * The relationship of the patient to the subscriber
     * </p> 
	 */
	public Claim setRelationship(CodeableConceptDt theValue) {
		myRelationship = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>school</b> (Name of School).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Name of school
     * </p> 
	 */
	public StringDt getSchool() {  
		if (mySchool == null) {
			mySchool = new StringDt();
		}
		return mySchool;
	}

	/**
	 * Sets the value(s) for <b>school</b> (Name of School)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of school
     * </p> 
	 */
	public Claim setSchool(StringDt theValue) {
		mySchool = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>school</b> (Name of School)
	 *
     * <p>
     * <b>Definition:</b>
     * Name of school
     * </p> 
	 */
	public Claim setSchool( String theString) {
		mySchool = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>service</b> (Service Details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<Service> getService() {  
		if (myService == null) {
			myService = new java.util.ArrayList<Service>();
		}
		return myService;
	}

	/**
	 * Sets the value(s) for <b>service</b> (Service Details)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Claim setService(java.util.List<Service> theValue) {
		myService = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>service</b> (Service Details)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Service addService() {
		Service newType = new Service();
		getService().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>service</b> (Service Details),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Service getServiceFirstRep() {
		if (getService().isEmpty()) {
			return addService();
		}
		return getService().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Claim.patient</b> (Patient Details)
	 *
     * <p>
     * <b>Definition:</b>
     * Patient Details.
     * </p> 
	 */
	@Block()	
	public static class Patient extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=HumanNameDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Patient name",
		formalDefinition="The name of the PolicyHolder"
	)
	private HumanNameDt myName;
	
	@Child(name="address", type=AddressDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Patient address",
		formalDefinition="The mailing address, typically home, of the PolicyHolder"
	)
	private AddressDt myAddress;
	
	@Child(name="birthdate", type=DateDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Patient date of birth",
		formalDefinition="The date of birth of the PolicyHolder"
	)
	private DateDt myBirthdate;
	
	@Child(name="gender", type=CodingDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Gender",
		formalDefinition="Gender."
	)
	private CodingDt myGender;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myAddress,  myBirthdate,  myGender);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myAddress, myBirthdate, myGender);
	}

	/**
	 * Gets the value(s) for <b>name</b> (Patient name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the PolicyHolder
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (Patient name)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the PolicyHolder
     * </p> 
	 */
	public Patient setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>address</b> (Patient address).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mailing address, typically home, of the PolicyHolder
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (Patient address)
	 *
     * <p>
     * <b>Definition:</b>
     * The mailing address, typically home, of the PolicyHolder
     * </p> 
	 */
	public Patient setAddress(AddressDt theValue) {
		myAddress = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>birthdate</b> (Patient date of birth).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public DateDt getBirthdate() {  
		if (myBirthdate == null) {
			myBirthdate = new DateDt();
		}
		return myBirthdate;
	}

	/**
	 * Sets the value(s) for <b>birthdate</b> (Patient date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public Patient setBirthdate(DateDt theValue) {
		myBirthdate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>birthdate</b> (Patient date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public Patient setBirthdateWithDayPrecision( Date theDate) {
		myBirthdate = new DateDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>birthdate</b> (Patient date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public Patient setBirthdate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myBirthdate = new DateDt(theDate, thePrecision); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>gender</b> (Gender).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Gender.
     * </p> 
	 */
	public CodingDt getGender() {  
		if (myGender == null) {
			myGender = new CodingDt();
		}
		return myGender;
	}

	/**
	 * Sets the value(s) for <b>gender</b> (Gender)
	 *
     * <p>
     * <b>Definition:</b>
     * Gender.
     * </p> 
	 */
	public Patient setGender(CodingDt theValue) {
		myGender = theValue;
		return this;
	}

  

	}


	/**
	 * Block class for child element: <b>Claim.coverage</b> (Insurance or medical plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Financial instrument by which payment information for health care
     * </p> 
	 */
	@Block()	
	public static class Coverage extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="issuer", order=0, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.Organization.class	})
	@Description(
		shortDefinition="An identifier for the plan issuer",
		formalDefinition="The program or plan underwriter or payor."
	)
	private ResourceReferenceDt myIssuer;
	
	@Child(name="period", type=PeriodDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Coverage start and end dates",
		formalDefinition="Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force."
	)
	private PeriodDt myPeriod;
	
	@Child(name="type", type=CodingDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Type of coverage",
		formalDefinition="The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health."
	)
	private CodingDt myType;
	
	@Child(name="identifier", type=IdentifierDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="The primary coverage ID",
		formalDefinition="The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID."
	)
	private IdentifierDt myIdentifier;
	
	@Child(name="group", type=IdentifierDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="An identifier for the group",
		formalDefinition="Todo"
	)
	private IdentifierDt myGroup;
	
	@Child(name="plan", type=IdentifierDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="An identifier for the plan",
		formalDefinition="Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID."
	)
	private IdentifierDt myPlan;
	
	@Child(name="subplan", type=IdentifierDt.class, order=6, min=0, max=1)	
	@Description(
		shortDefinition="An identifier for the subsection of the plan",
		formalDefinition="Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID."
	)
	private IdentifierDt mySubplan;
	
	@Child(name="dependent", type=IntegerDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="The dependent number",
		formalDefinition="A unique identifier for a dependent under the coverage."
	)
	private IntegerDt myDependent;
	
	@Child(name="sequence", type=IntegerDt.class, order=8, min=0, max=1)	
	@Description(
		shortDefinition="The plan instance or sequence counter",
		formalDefinition="An optional counter for a particular instance of the identified coverage which increments upon each renewal."
	)
	private IntegerDt mySequence;
	
	@Child(name="subscriber", order=9, min=0, max=1)	
	@Description(
		shortDefinition="Planholder information",
		formalDefinition="Th demographics for the individual in whose name the insurance coverage is issued."
	)
	private CoverageSubscriber mySubscriber;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIssuer,  myPeriod,  myType,  myIdentifier,  myGroup,  myPlan,  mySubplan,  myDependent,  mySequence,  mySubscriber);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIssuer, myPeriod, myType, myIdentifier, myGroup, myPlan, mySubplan, myDependent, mySequence, mySubscriber);
	}

	/**
	 * Gets the value(s) for <b>issuer</b> (An identifier for the plan issuer).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The program or plan underwriter or payor.
     * </p> 
	 */
	public ResourceReferenceDt getIssuer() {  
		if (myIssuer == null) {
			myIssuer = new ResourceReferenceDt();
		}
		return myIssuer;
	}

	/**
	 * Sets the value(s) for <b>issuer</b> (An identifier for the plan issuer)
	 *
     * <p>
     * <b>Definition:</b>
     * The program or plan underwriter or payor.
     * </p> 
	 */
	public Coverage setIssuer(ResourceReferenceDt theValue) {
		myIssuer = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>period</b> (Coverage start and end dates).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.
     * </p> 
	 */
	public PeriodDt getPeriod() {  
		if (myPeriod == null) {
			myPeriod = new PeriodDt();
		}
		return myPeriod;
	}

	/**
	 * Sets the value(s) for <b>period</b> (Coverage start and end dates)
	 *
     * <p>
     * <b>Definition:</b>
     * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.
     * </p> 
	 */
	public Coverage setPeriod(PeriodDt theValue) {
		myPeriod = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>type</b> (Type of coverage).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.
     * </p> 
	 */
	public CodingDt getType() {  
		if (myType == null) {
			myType = new CodingDt();
		}
		return myType;
	}

	/**
	 * Sets the value(s) for <b>type</b> (Type of coverage)
	 *
     * <p>
     * <b>Definition:</b>
     * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.
     * </p> 
	 */
	public Coverage setType(CodingDt theValue) {
		myType = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>identifier</b> (The primary coverage ID).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public IdentifierDt getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new IdentifierDt();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (The primary coverage ID)
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public Coverage setIdentifier(IdentifierDt theValue) {
		myIdentifier = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>identifier</b> (The primary coverage ID)
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public Coverage setIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myIdentifier = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>identifier</b> (The primary coverage ID)
	 *
     * <p>
     * <b>Definition:</b>
     * The main (and possibly only) identifier for the coverage - often referred to as a Subscriber Id, Certificate number or Personal Health Number or Case ID.
     * </p> 
	 */
	public Coverage setIdentifier( String theSystem,  String theValue) {
		myIdentifier = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>group</b> (An identifier for the group).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public IdentifierDt getGroup() {  
		if (myGroup == null) {
			myGroup = new IdentifierDt();
		}
		return myGroup;
	}

	/**
	 * Sets the value(s) for <b>group</b> (An identifier for the group)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Coverage setGroup(IdentifierDt theValue) {
		myGroup = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>group</b> (An identifier for the group)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Coverage setGroup( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myGroup = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>group</b> (An identifier for the group)
	 *
     * <p>
     * <b>Definition:</b>
     * Todo
     * </p> 
	 */
	public Coverage setGroup( String theSystem,  String theValue) {
		myGroup = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>plan</b> (An identifier for the plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public IdentifierDt getPlan() {  
		if (myPlan == null) {
			myPlan = new IdentifierDt();
		}
		return myPlan;
	}

	/**
	 * Sets the value(s) for <b>plan</b> (An identifier for the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setPlan(IdentifierDt theValue) {
		myPlan = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>plan</b> (An identifier for the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setPlan( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		myPlan = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>plan</b> (An identifier for the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     * </p> 
	 */
	public Coverage setPlan( String theSystem,  String theValue) {
		myPlan = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subplan</b> (An identifier for the subsection of the plan).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public IdentifierDt getSubplan() {  
		if (mySubplan == null) {
			mySubplan = new IdentifierDt();
		}
		return mySubplan;
	}

	/**
	 * Sets the value(s) for <b>subplan</b> (An identifier for the subsection of the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public Coverage setSubplan(IdentifierDt theValue) {
		mySubplan = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>subplan</b> (An identifier for the subsection of the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public Coverage setSubplan( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		mySubplan = new IdentifierDt(theUse, theSystem, theValue, theLabel); 
		return this; 
	}

	/**
	 * Sets the value for <b>subplan</b> (An identifier for the subsection of the plan)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     * </p> 
	 */
	public Coverage setSubplan( String theSystem,  String theValue) {
		mySubplan = new IdentifierDt(theSystem, theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>dependent</b> (The dependent number).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public IntegerDt getDependent() {  
		if (myDependent == null) {
			myDependent = new IntegerDt();
		}
		return myDependent;
	}

	/**
	 * Sets the value(s) for <b>dependent</b> (The dependent number)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public Coverage setDependent(IntegerDt theValue) {
		myDependent = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>dependent</b> (The dependent number)
	 *
     * <p>
     * <b>Definition:</b>
     * A unique identifier for a dependent under the coverage.
     * </p> 
	 */
	public Coverage setDependent( int theInteger) {
		myDependent = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>sequence</b> (The plan instance or sequence counter).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public IntegerDt getSequence() {  
		if (mySequence == null) {
			mySequence = new IntegerDt();
		}
		return mySequence;
	}

	/**
	 * Sets the value(s) for <b>sequence</b> (The plan instance or sequence counter)
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public Coverage setSequence(IntegerDt theValue) {
		mySequence = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>sequence</b> (The plan instance or sequence counter)
	 *
     * <p>
     * <b>Definition:</b>
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     * </p> 
	 */
	public Coverage setSequence( int theInteger) {
		mySequence = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>subscriber</b> (Planholder information).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Th demographics for the individual in whose name the insurance coverage is issued.
     * </p> 
	 */
	public CoverageSubscriber getSubscriber() {  
		if (mySubscriber == null) {
			mySubscriber = new CoverageSubscriber();
		}
		return mySubscriber;
	}

	/**
	 * Sets the value(s) for <b>subscriber</b> (Planholder information)
	 *
     * <p>
     * <b>Definition:</b>
     * Th demographics for the individual in whose name the insurance coverage is issued.
     * </p> 
	 */
	public Coverage setSubscriber(CoverageSubscriber theValue) {
		mySubscriber = theValue;
		return this;
	}

  

	}

	/**
	 * Block class for child element: <b>Claim.coverage.subscriber</b> (Planholder information)
	 *
     * <p>
     * <b>Definition:</b>
     * Th demographics for the individual in whose name the insurance coverage is issued.
     * </p> 
	 */
	@Block()	
	public static class CoverageSubscriber extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="name", type=HumanNameDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="PolicyHolder name",
		formalDefinition="The name of the PolicyHolder"
	)
	private HumanNameDt myName;
	
	@Child(name="address", type=AddressDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="PolicyHolder address",
		formalDefinition="The mailing address, typically home, of the PolicyHolder"
	)
	private AddressDt myAddress;
	
	@Child(name="birthdate", type=DateDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="PolicyHolder date of birth",
		formalDefinition="The date of birth of the PolicyHolder"
	)
	private DateDt myBirthdate;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myAddress,  myBirthdate);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myAddress, myBirthdate);
	}

	/**
	 * Gets the value(s) for <b>name</b> (PolicyHolder name).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the PolicyHolder
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (PolicyHolder name)
	 *
     * <p>
     * <b>Definition:</b>
     * The name of the PolicyHolder
     * </p> 
	 */
	public CoverageSubscriber setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>address</b> (PolicyHolder address).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The mailing address, typically home, of the PolicyHolder
     * </p> 
	 */
	public AddressDt getAddress() {  
		if (myAddress == null) {
			myAddress = new AddressDt();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (PolicyHolder address)
	 *
     * <p>
     * <b>Definition:</b>
     * The mailing address, typically home, of the PolicyHolder
     * </p> 
	 */
	public CoverageSubscriber setAddress(AddressDt theValue) {
		myAddress = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>birthdate</b> (PolicyHolder date of birth).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public DateDt getBirthdate() {  
		if (myBirthdate == null) {
			myBirthdate = new DateDt();
		}
		return myBirthdate;
	}

	/**
	 * Sets the value(s) for <b>birthdate</b> (PolicyHolder date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public CoverageSubscriber setBirthdate(DateDt theValue) {
		myBirthdate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>birthdate</b> (PolicyHolder date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public CoverageSubscriber setBirthdateWithDayPrecision( Date theDate) {
		myBirthdate = new DateDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>birthdate</b> (PolicyHolder date of birth)
	 *
     * <p>
     * <b>Definition:</b>
     * The date of birth of the PolicyHolder
     * </p> 
	 */
	public CoverageSubscriber setBirthdate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myBirthdate = new DateDt(theDate, thePrecision); 
		return this; 
	}

 

	}



	/**
	 * Block class for child element: <b>Claim.service</b> (Service Details)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class Service extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="service", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Professional service code",
		formalDefinition="The code for the professional service provided."
	)
	private CodeableConceptDt myService;
	
	@Child(name="instance", type=IntegerDt.class, order=1, min=1, max=1)	
	@Description(
		shortDefinition="Service instance",
		formalDefinition="A service line item."
	)
	private IntegerDt myInstance;
	
	@Child(name="fee", type=DecimalDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Professional fee",
		formalDefinition=""
	)
	private DecimalDt myFee;
	
	@Child(name="location", type=CodeableConceptDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Service Location",
		formalDefinition="Code for the oral cavity, tooth, quadrant, sextant or arch."
	)
	private CodeableConceptDt myLocation;
	
	@Child(name="surface", type=CodeableConceptDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Tooth surface",
		formalDefinition="Tooth surface(s) involved in the restoration."
	)
	private CodeableConceptDt mySurface;
	
	@Child(name="lab", order=5, min=0, max=1)	
	@Description(
		shortDefinition="Lab Details",
		formalDefinition=""
	)
	private ServiceLab myLab;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myService,  myInstance,  myFee,  myLocation,  mySurface,  myLab);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myService, myInstance, myFee, myLocation, mySurface, myLab);
	}

	/**
	 * Gets the value(s) for <b>service</b> (Professional service code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code for the professional service provided.
     * </p> 
	 */
	public CodeableConceptDt getService() {  
		if (myService == null) {
			myService = new CodeableConceptDt();
		}
		return myService;
	}

	/**
	 * Sets the value(s) for <b>service</b> (Professional service code)
	 *
     * <p>
     * <b>Definition:</b>
     * The code for the professional service provided.
     * </p> 
	 */
	public Service setService(CodeableConceptDt theValue) {
		myService = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>instance</b> (Service instance).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A service line item.
     * </p> 
	 */
	public IntegerDt getInstance() {  
		if (myInstance == null) {
			myInstance = new IntegerDt();
		}
		return myInstance;
	}

	/**
	 * Sets the value(s) for <b>instance</b> (Service instance)
	 *
     * <p>
     * <b>Definition:</b>
     * A service line item.
     * </p> 
	 */
	public Service setInstance(IntegerDt theValue) {
		myInstance = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>instance</b> (Service instance)
	 *
     * <p>
     * <b>Definition:</b>
     * A service line item.
     * </p> 
	 */
	public Service setInstance( int theInteger) {
		myInstance = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>fee</b> (Professional fee).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public DecimalDt getFee() {  
		if (myFee == null) {
			myFee = new DecimalDt();
		}
		return myFee;
	}

	/**
	 * Sets the value(s) for <b>fee</b> (Professional fee)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Service setFee(DecimalDt theValue) {
		myFee = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>fee</b> (Professional fee)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Service setFee( long theValue) {
		myFee = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>fee</b> (Professional fee)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Service setFee( double theValue) {
		myFee = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>fee</b> (Professional fee)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Service setFee( java.math.BigDecimal theValue) {
		myFee = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>location</b> (Service Location).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Code for the oral cavity, tooth, quadrant, sextant or arch.
     * </p> 
	 */
	public CodeableConceptDt getLocation() {  
		if (myLocation == null) {
			myLocation = new CodeableConceptDt();
		}
		return myLocation;
	}

	/**
	 * Sets the value(s) for <b>location</b> (Service Location)
	 *
     * <p>
     * <b>Definition:</b>
     * Code for the oral cavity, tooth, quadrant, sextant or arch.
     * </p> 
	 */
	public Service setLocation(CodeableConceptDt theValue) {
		myLocation = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>surface</b> (Tooth surface).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Tooth surface(s) involved in the restoration.
     * </p> 
	 */
	public CodeableConceptDt getSurface() {  
		if (mySurface == null) {
			mySurface = new CodeableConceptDt();
		}
		return mySurface;
	}

	/**
	 * Sets the value(s) for <b>surface</b> (Tooth surface)
	 *
     * <p>
     * <b>Definition:</b>
     * Tooth surface(s) involved in the restoration.
     * </p> 
	 */
	public Service setSurface(CodeableConceptDt theValue) {
		mySurface = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>lab</b> (Lab Details).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ServiceLab getLab() {  
		if (myLab == null) {
			myLab = new ServiceLab();
		}
		return myLab;
	}

	/**
	 * Sets the value(s) for <b>lab</b> (Lab Details)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public Service setLab(ServiceLab theValue) {
		myLab = theValue;
		return this;
	}

  

	}

	/**
	 * Block class for child element: <b>Claim.service.lab</b> (Lab Details)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	@Block()	
	public static class ServiceLab extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="service", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Lab service code",
		formalDefinition="The code for the lab service provided."
	)
	private CodeableConceptDt myService;
	
	@Child(name="fee", type=DecimalDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Lab fee",
		formalDefinition="The amount to reimbuse for a laboratory service."
	)
	private DecimalDt myFee;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myService,  myFee);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myService, myFee);
	}

	/**
	 * Gets the value(s) for <b>service</b> (Lab service code).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The code for the lab service provided.
     * </p> 
	 */
	public CodeableConceptDt getService() {  
		if (myService == null) {
			myService = new CodeableConceptDt();
		}
		return myService;
	}

	/**
	 * Sets the value(s) for <b>service</b> (Lab service code)
	 *
     * <p>
     * <b>Definition:</b>
     * The code for the lab service provided.
     * </p> 
	 */
	public ServiceLab setService(CodeableConceptDt theValue) {
		myService = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>fee</b> (Lab fee).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The amount to reimbuse for a laboratory service.
     * </p> 
	 */
	public DecimalDt getFee() {  
		if (myFee == null) {
			myFee = new DecimalDt();
		}
		return myFee;
	}

	/**
	 * Sets the value(s) for <b>fee</b> (Lab fee)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount to reimbuse for a laboratory service.
     * </p> 
	 */
	public ServiceLab setFee(DecimalDt theValue) {
		myFee = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>fee</b> (Lab fee)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount to reimbuse for a laboratory service.
     * </p> 
	 */
	public ServiceLab setFee( long theValue) {
		myFee = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>fee</b> (Lab fee)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount to reimbuse for a laboratory service.
     * </p> 
	 */
	public ServiceLab setFee( double theValue) {
		myFee = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>fee</b> (Lab fee)
	 *
     * <p>
     * <b>Definition:</b>
     * The amount to reimbuse for a laboratory service.
     * </p> 
	 */
	public ServiceLab setFee( java.math.BigDecimal theValue) {
		myFee = new DecimalDt(theValue); 
		return this; 
	}

 

	}





}
