















package ca.uhn.fhir.model.dstu.resource;


import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeableConceptDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;


/**
 * HAPI/FHIR <b>Person</b> Resource
 * (Person involved in healthcare)
 *
 * <p>
 * <b>Definition:</b>
 * A person who is involved in the healthcare process
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to track persons potentially across multiple roles
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/Person">http://hl7.org/fhir/profiles/Person</a> 
 * </p>
 *
 */
@ResourceDef(name="Person", profile="http://hl7.org/fhir/profiles/Person", id="person")
public class Person extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>identifier</b>
	 * <p>
	 * Description: <b>A patient Identifier</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>identifier</b><br/>
	 * </p>
	 */
	public static final String SP_IDENTIFIER = "identifier";

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b>a portion of name in any name part</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>name</b><br/>
	 * </p>
	 */
	public static final String SP_NAME = "name";

	/**
	 * Search parameter constant for <b>phonetic</b>
	 * <p>
	 * Description: <b>a portion of name using some kind of phonetic matching algorithm</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>phonetic</b><br/>
	 * </p>
	 */
	public static final String SP_PHONETIC = "phonetic";

	/**
	 * Search parameter constant for <b>telecom</b>
	 * <p>
	 * Description: <b>the value in any kind of contact</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>telecom</b><br/>
	 * </p>
	 */
	public static final String SP_TELECOM = "telecom";

	/**
	 * Search parameter constant for <b>address</b>
	 * <p>
	 * Description: <b>an address in any kind of address/part</b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>address</b><br/>
	 * </p>
	 */
	public static final String SP_ADDRESS = "address";

	/**
	 * Search parameter constant for <b>gender</b>
	 * <p>
	 * Description: <b>gender of the person</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>gender</b><br/>
	 * </p>
	 */
	public static final String SP_GENDER = "gender";

	/**
	 * Search parameter constant for <b>language</b>
	 * <p>
	 * Description: <b>language code (irrespective of use value)</b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>language</b><br/>
	 * </p>
	 */
	public static final String SP_LANGUAGE = "language";

	/**
	 * Search parameter constant for <b>birthdate</b>
	 * <p>
	 * Description: <b>the patient's date of birth</b><br/>
	 * Type: <b>date</b><br/>
	 * Path: <b>birthdate</b><br/>
	 * </p>
	 */
	public static final String SP_BIRTHDATE = "birthdate";


	@Child(name="identifier", type=IdentifierDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A Human identifier for this person",
		formalDefinition="Identifier for a person within a particular scope."
	)
	private java.util.List<IdentifierDt> myIdentifier;
	
	@Child(name="name", type=HumanNameDt.class, order=1, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A name associated with the person",
		formalDefinition="A name associated with the person"
	)
	private java.util.List<HumanNameDt> myName;
	
	@Child(name="telecom", type=ContactDt.class, order=2, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="A contact detail for the person",
		formalDefinition="A contact detail for the person, e.g. a telephone number or an email address."
	)
	private java.util.List<ContactDt> myTelecom;
	
	@Child(name="gender", type=CodingDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Administrative Gender",
		formalDefinition="Administrative Gender"
	)
	private CodingDt myGender;
	
	@Child(name="birthDate", type=DateTimeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="The birth date for the person",
		formalDefinition="The birth date for the person."
	)
	private DateTimeDt myBirthDate;
	
	@Child(name="deceased", type=BooleanDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Indicates if the Person is deceased or not",
		formalDefinition="Indicates if the Person is deceased or not"
	)
	private BooleanDt myDeceased;
	
	@Child(name="address", type=AddressDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="One or more addresses for the person",
		formalDefinition="One or more addresses for the person"
	)
	private java.util.List<AddressDt> myAddress;
	
	@Child(name="maritalStatus", type=CodeableConceptDt.class, order=7, min=0, max=1)	
	@Description(
		shortDefinition="Marital (civil) status of the person",
		formalDefinition="This field contains the patient's marital (civil) status."
	)
	private BoundCodeableConceptDt<MaritalStatusCodesEnum> myMaritalStatus;
	
	@Child(name="language", order=8, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="The person's  proficiency level of a language",
		formalDefinition="A language spoken by the person, with proficiency"
	)
	private java.util.List<Language> myLanguageElement;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myIdentifier,  myName,  myTelecom,  myGender,  myBirthDate,  myDeceased,  myAddress,  myMaritalStatus,  myLanguageElement);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myIdentifier, myName, myTelecom, myGender, myBirthDate, myDeceased, myAddress, myMaritalStatus, myLanguageElement);
	}

	/**
	 * Gets the value(s) for <b>identifier</b> (A Human identifier for this person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
	 */
	public java.util.List<IdentifierDt> getIdentifier() {  
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		return myIdentifier;
	}

	/**
	 * Sets the value(s) for <b>identifier</b> (A Human identifier for this person)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
	 */
	public Person setIdentifier(java.util.List<IdentifierDt> theValue) {
		myIdentifier = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>identifier</b> (A Human identifier for this person)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
	 */
	public IdentifierDt addIdentifier() {
		IdentifierDt newType = new IdentifierDt();
		getIdentifier().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>identifier</b> (A Human identifier for this person),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
	 */
	public IdentifierDt getIdentifierFirstRep() {
		if (getIdentifier().isEmpty()) {
			return addIdentifier();
		}
		return getIdentifier().get(0); 
	}
 	/**
	 * Adds a new value for <b>identifier</b> (A Human identifier for this person)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Person addIdentifier( IdentifierUseEnum theUse,  String theSystem,  String theValue,  String theLabel) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theUse, theSystem, theValue, theLabel));
		return this; 
	}

	/**
	 * Adds a new value for <b>identifier</b> (A Human identifier for this person)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifier for a person within a particular scope.
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public Person addIdentifier( String theSystem,  String theValue) {
		if (myIdentifier == null) {
			myIdentifier = new java.util.ArrayList<IdentifierDt>();
		}
		myIdentifier.add(new IdentifierDt(theSystem, theValue));
		return this; 
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
	public java.util.List<HumanNameDt> getName() {  
		if (myName == null) {
			myName = new java.util.ArrayList<HumanNameDt>();
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
	public Person setName(java.util.List<HumanNameDt> theValue) {
		myName = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>name</b> (A name associated with the person)
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the person
     * </p> 
	 */
	public HumanNameDt addName() {
		HumanNameDt newType = new HumanNameDt();
		getName().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>name</b> (A name associated with the person),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A name associated with the person
     * </p> 
	 */
	public HumanNameDt getNameFirstRep() {
		if (getName().isEmpty()) {
			return addName();
		}
		return getName().get(0); 
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
	public Person setTelecom(java.util.List<ContactDt> theValue) {
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
	 * Gets the value(s) for <b>gender</b> (Administrative Gender).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender
     * </p> 
	 */
	public CodingDt getGender() {  
		if (myGender == null) {
			myGender = new CodingDt();
		}
		return myGender;
	}

	/**
	 * Sets the value(s) for <b>gender</b> (Administrative Gender)
	 *
     * <p>
     * <b>Definition:</b>
     * Administrative Gender
     * </p> 
	 */
	public Person setGender(CodingDt theValue) {
		myGender = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>birthDate</b> (The birth date for the person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The birth date for the person.
     * </p> 
	 */
	public DateTimeDt getBirthDate() {  
		if (myBirthDate == null) {
			myBirthDate = new DateTimeDt();
		}
		return myBirthDate;
	}

	/**
	 * Sets the value(s) for <b>birthDate</b> (The birth date for the person)
	 *
     * <p>
     * <b>Definition:</b>
     * The birth date for the person.
     * </p> 
	 */
	public Person setBirthDate(DateTimeDt theValue) {
		myBirthDate = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>birthDate</b> (The birth date for the person)
	 *
     * <p>
     * <b>Definition:</b>
     * The birth date for the person.
     * </p> 
	 */
	public Person setBirthDate( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myBirthDate = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>birthDate</b> (The birth date for the person)
	 *
     * <p>
     * <b>Definition:</b>
     * The birth date for the person.
     * </p> 
	 */
	public Person setBirthDateWithSecondsPrecision( Date theDate) {
		myBirthDate = new DateTimeDt(theDate); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>deceased</b> (Indicates if the Person is deceased or not).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the Person is deceased or not
     * </p> 
	 */
	public BooleanDt getDeceased() {  
		if (myDeceased == null) {
			myDeceased = new BooleanDt();
		}
		return myDeceased;
	}

	/**
	 * Sets the value(s) for <b>deceased</b> (Indicates if the Person is deceased or not)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the Person is deceased or not
     * </p> 
	 */
	public Person setDeceased(BooleanDt theValue) {
		myDeceased = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>deceased</b> (Indicates if the Person is deceased or not)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates if the Person is deceased or not
     * </p> 
	 */
	public Person setDeceased( boolean theBoolean) {
		myDeceased = new BooleanDt(theBoolean); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>address</b> (One or more addresses for the person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * One or more addresses for the person
     * </p> 
	 */
	public java.util.List<AddressDt> getAddress() {  
		if (myAddress == null) {
			myAddress = new java.util.ArrayList<AddressDt>();
		}
		return myAddress;
	}

	/**
	 * Sets the value(s) for <b>address</b> (One or more addresses for the person)
	 *
     * <p>
     * <b>Definition:</b>
     * One or more addresses for the person
     * </p> 
	 */
	public Person setAddress(java.util.List<AddressDt> theValue) {
		myAddress = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>address</b> (One or more addresses for the person)
	 *
     * <p>
     * <b>Definition:</b>
     * One or more addresses for the person
     * </p> 
	 */
	public AddressDt addAddress() {
		AddressDt newType = new AddressDt();
		getAddress().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>address</b> (One or more addresses for the person),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * One or more addresses for the person
     * </p> 
	 */
	public AddressDt getAddressFirstRep() {
		if (getAddress().isEmpty()) {
			return addAddress();
		}
		return getAddress().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>maritalStatus</b> (Marital (civil) status of the person).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains the patient's marital (civil) status.
     * </p> 
	 */
	public BoundCodeableConceptDt<MaritalStatusCodesEnum> getMaritalStatus() {  
		if (myMaritalStatus == null) {
			myMaritalStatus = new BoundCodeableConceptDt<MaritalStatusCodesEnum>(MaritalStatusCodesEnum.VALUESET_BINDER);
		}
		return myMaritalStatus;
	}

	/**
	 * Sets the value(s) for <b>maritalStatus</b> (Marital (civil) status of the person)
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains the patient's marital (civil) status.
     * </p> 
	 */
	public Person setMaritalStatus(BoundCodeableConceptDt<MaritalStatusCodesEnum> theValue) {
		myMaritalStatus = theValue;
		return this;
	}

	/**
	 * Sets the value(s) for <b>maritalStatus</b> (Marital (civil) status of the person)
	 *
     * <p>
     * <b>Definition:</b>
     * This field contains the patient's marital (civil) status.
     * </p> 
	 */
	public Person setMaritalStatus(MaritalStatusCodesEnum theValue) {
		getMaritalStatus().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>language</b> (The person's  proficiency level of a language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A language spoken by the person, with proficiency
     * </p> 
	 */
	public java.util.List<Language> getLanguageElement() {  
		if (myLanguageElement == null) {
			myLanguageElement = new java.util.ArrayList<Language>();
		}
		return myLanguageElement;
	}

	/**
	 * Sets the value(s) for <b>language</b> (The person's  proficiency level of a language)
	 *
     * <p>
     * <b>Definition:</b>
     * A language spoken by the person, with proficiency
     * </p> 
	 */
	public Person setLanguageElement(java.util.List<Language> theValue) {
		myLanguageElement = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>language</b> (The person's  proficiency level of a language)
	 *
     * <p>
     * <b>Definition:</b>
     * A language spoken by the person, with proficiency
     * </p> 
	 */
	public Language addLanguageElement() {
		Language newType = new Language();
		getLanguageElement().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>language</b> (The person's  proficiency level of a language),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * A language spoken by the person, with proficiency
     * </p> 
	 */
	public Language getLanguageElementFirstRep() {
		if (getLanguageElement().isEmpty()) {
			return addLanguageElement();
		}
		return getLanguageElement().get(0); 
	}
  
	/**
	 * Block class for child element: <b>Person.language</b> (The person's  proficiency level of a language)
	 *
     * <p>
     * <b>Definition:</b>
     * A language spoken by the person, with proficiency
     * </p> 
	 */
	@Block(name="Person.language")	
	public static class Language extends BaseElement implements IResourceBlock {
	
	@Child(name="language", type=CodeableConceptDt.class, order=0, min=1, max=1)	
	@Description(
		shortDefinition="Language with optional region",
		formalDefinition="The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case. E.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English"
	)
	private CodeableConceptDt myLanguageElement;
	
	@Child(name="mode", type=CodeableConceptDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Language method of expression",
		formalDefinition="A value representing the person's method of expression of this language. Examples: expressed spoken, expressed written, expressed signed, received spoken, received written, received signed"
	)
	private CodeableConceptDt myMode;
	
	@Child(name="proficiencyLevel", type=CodeableConceptDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Proficiency level of the language",
		formalDefinition="A code that describes how well the language is spoken"
	)
	private CodeableConceptDt myProficiencyLevel;
	
	@Child(name="preference", type=BooleanDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Language preference indicator",
		formalDefinition="Indicates whether or not the Person prefers this language (over other languages he masters up a certain level)"
	)
	private BooleanDt myPreference;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLanguageElement,  myMode,  myProficiencyLevel,  myPreference);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLanguageElement, myMode, myProficiencyLevel, myPreference);
	}

	/**
	 * Gets the value(s) for <b>language</b> (Language with optional region).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case. E.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English
     * </p> 
	 */
	public CodeableConceptDt getLanguageElement() {  
		if (myLanguageElement == null) {
			myLanguageElement = new CodeableConceptDt();
		}
		return myLanguageElement;
	}

	/**
	 * Sets the value(s) for <b>language</b> (Language with optional region)
	 *
     * <p>
     * <b>Definition:</b>
     * The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case. E.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English
     * </p> 
	 */
	public Language setLanguageElement(CodeableConceptDt theValue) {
		myLanguageElement = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>mode</b> (Language method of expression).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A value representing the person's method of expression of this language. Examples: expressed spoken, expressed written, expressed signed, received spoken, received written, received signed
     * </p> 
	 */
	public CodeableConceptDt getMode() {  
		if (myMode == null) {
			myMode = new CodeableConceptDt();
		}
		return myMode;
	}

	/**
	 * Sets the value(s) for <b>mode</b> (Language method of expression)
	 *
     * <p>
     * <b>Definition:</b>
     * A value representing the person's method of expression of this language. Examples: expressed spoken, expressed written, expressed signed, received spoken, received written, received signed
     * </p> 
	 */
	public Language setMode(CodeableConceptDt theValue) {
		myMode = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>proficiencyLevel</b> (Proficiency level of the language).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A code that describes how well the language is spoken
     * </p> 
	 */
	public CodeableConceptDt getProficiencyLevel() {  
		if (myProficiencyLevel == null) {
			myProficiencyLevel = new CodeableConceptDt();
		}
		return myProficiencyLevel;
	}

	/**
	 * Sets the value(s) for <b>proficiencyLevel</b> (Proficiency level of the language)
	 *
     * <p>
     * <b>Definition:</b>
     * A code that describes how well the language is spoken
     * </p> 
	 */
	public Language setProficiencyLevel(CodeableConceptDt theValue) {
		myProficiencyLevel = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>preference</b> (Language preference indicator).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether or not the Person prefers this language (over other languages he masters up a certain level)
     * </p> 
	 */
	public BooleanDt getPreference() {  
		if (myPreference == null) {
			myPreference = new BooleanDt();
		}
		return myPreference;
	}

	/**
	 * Sets the value(s) for <b>preference</b> (Language preference indicator)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether or not the Person prefers this language (over other languages he masters up a certain level)
     * </p> 
	 */
	public Language setPreference(BooleanDt theValue) {
		myPreference = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>preference</b> (Language preference indicator)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates whether or not the Person prefers this language (over other languages he masters up a certain level)
     * </p> 
	 */
	public Language setPreference( boolean theBoolean) {
		myPreference = new BooleanDt(theBoolean); 
		return this; 
	}

 

	}




}