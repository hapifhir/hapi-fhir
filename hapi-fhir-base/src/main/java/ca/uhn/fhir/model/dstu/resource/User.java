















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

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;


/**
 * HAPI/FHIR <b>User</b> Resource
 * (A user authorized to use the system)
 *
 * <p>
 * <b>Definition:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 *
 * <p>
 * <b>Profile Definition:</b>
 * <a href="http://hl7.org/fhir/profiles/User">http://hl7.org/fhir/profiles/User</a> 
 * </p>
 *
 */
@ResourceDef(name="User", profile="http://hl7.org/fhir/profiles/User", id="user")
public class User extends BaseResource implements IResource {

	/**
	 * Search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>User.name</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="name", path="User.name", description="", type="string"  )
	public static final String SP_NAME = "name";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>name</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>User.name</b><br/>
	 * </p>
	 */
	public static final StringClientParam NAME = new StringClientParam(SP_NAME);

	/**
	 * Search parameter constant for <b>provider</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>User.provider</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="provider", path="User.provider", description="", type="token"  )
	public static final String SP_PROVIDER = "provider";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>provider</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>User.provider</b><br/>
	 * </p>
	 */
	public static final TokenClientParam PROVIDER = new TokenClientParam(SP_PROVIDER);

	/**
	 * Search parameter constant for <b>login</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>User.login</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="login", path="User.login", description="", type="string"  )
	public static final String SP_LOGIN = "login";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>login</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>string</b><br/>
	 * Path: <b>User.login</b><br/>
	 * </p>
	 */
	public static final StringClientParam LOGIN = new StringClientParam(SP_LOGIN);

	/**
	 * Search parameter constant for <b>level</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>User.level</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="level", path="User.level", description="", type="token"  )
	public static final String SP_LEVEL = "level";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>level</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>token</b><br/>
	 * Path: <b>User.level</b><br/>
	 * </p>
	 */
	public static final TokenClientParam LEVEL = new TokenClientParam(SP_LEVEL);

	/**
	 * Search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>User.patient</b><br/>
	 * </p>
	 */
	@SearchParamDefinition(name="patient", path="User.patient", description="", type="reference"  )
	public static final String SP_PATIENT = "patient";

	/**
	 * <b>Fluent Client</b> search parameter constant for <b>patient</b>
	 * <p>
	 * Description: <b></b><br/>
	 * Type: <b>reference</b><br/>
	 * Path: <b>User.patient</b><br/>
	 * </p>
	 */
	public static final ReferenceClientParam PATIENT = new ReferenceClientParam(SP_PATIENT);

	/**
	 * Constant for fluent queries to be used to add include statements. Specifies
	 * the path value of "<b>User.patient</b>".
	 */
	public static final Include INCLUDE_PATIENT = new Include("User.patient");


	@Child(name="name", type=HumanNameDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="The name of this user",
		formalDefinition=""
	)
	private HumanNameDt myName;
	
	@Child(name="provider", type=UriDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Which system authenticates the user. Blanks = internally authenticated",
		formalDefinition=""
	)
	private UriDt myProvider;
	
	@Child(name="login", type=StringDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="The login by which this user is known",
		formalDefinition=""
	)
	private StringDt myLogin;
	
	@Child(name="password", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="If internal login, the password hash (SHA 256, Hex, lowercase)",
		formalDefinition=""
	)
	private StringDt myPassword;
	
	@Child(name="level", type=CodeDt.class, order=4, min=1, max=1)	
	@Description(
		shortDefinition="The level access for this user",
		formalDefinition=""
	)
	private CodeDt myLevel;
	
	@Child(name="sessionLength", type=IntegerDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="How long a session lasts for",
		formalDefinition=""
	)
	private IntegerDt mySessionLength;
	
	@Child(name="contact", type=ContactDt.class, order=6, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Contact details for the user",
		formalDefinition=""
	)
	private java.util.List<ContactDt> myContact;
	
	@Child(name="patient", order=7, min=0, max=Child.MAX_UNLIMITED, type={
		ca.uhn.fhir.model.dstu.resource.Patient.class	})
	@Description(
		shortDefinition="Patient compartments the user has access to (if level is patient/family)",
		formalDefinition=""
	)
	private java.util.List<ResourceReferenceDt> myPatient;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myName,  myProvider,  myLogin,  myPassword,  myLevel,  mySessionLength,  myContact,  myPatient);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myName, myProvider, myLogin, myPassword, myLevel, mySessionLength, myContact, myPatient);
	}

	/**
	 * Gets the value(s) for <b>name</b> (The name of this user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public HumanNameDt getName() {  
		if (myName == null) {
			myName = new HumanNameDt();
		}
		return myName;
	}

	/**
	 * Sets the value(s) for <b>name</b> (The name of this user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setName(HumanNameDt theValue) {
		myName = theValue;
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>provider</b> (Which system authenticates the user. Blanks = internally authenticated).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public UriDt getProvider() {  
		if (myProvider == null) {
			myProvider = new UriDt();
		}
		return myProvider;
	}

	/**
	 * Sets the value(s) for <b>provider</b> (Which system authenticates the user. Blanks = internally authenticated)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setProvider(UriDt theValue) {
		myProvider = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>provider</b> (Which system authenticates the user. Blanks = internally authenticated)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setProvider( String theUri) {
		myProvider = new UriDt(theUri); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>login</b> (The login by which this user is known).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getLogin() {  
		if (myLogin == null) {
			myLogin = new StringDt();
		}
		return myLogin;
	}

	/**
	 * Sets the value(s) for <b>login</b> (The login by which this user is known)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setLogin(StringDt theValue) {
		myLogin = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>login</b> (The login by which this user is known)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setLogin( String theString) {
		myLogin = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>password</b> (If internal login, the password hash (SHA 256, Hex, lowercase)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public StringDt getPassword() {  
		if (myPassword == null) {
			myPassword = new StringDt();
		}
		return myPassword;
	}

	/**
	 * Sets the value(s) for <b>password</b> (If internal login, the password hash (SHA 256, Hex, lowercase))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setPassword(StringDt theValue) {
		myPassword = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>password</b> (If internal login, the password hash (SHA 256, Hex, lowercase))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setPassword( String theString) {
		myPassword = new StringDt(theString); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>level</b> (The level access for this user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public CodeDt getLevel() {  
		if (myLevel == null) {
			myLevel = new CodeDt();
		}
		return myLevel;
	}

	/**
	 * Sets the value(s) for <b>level</b> (The level access for this user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setLevel(CodeDt theValue) {
		myLevel = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>level</b> (The level access for this user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setLevel( String theCode) {
		myLevel = new CodeDt(theCode); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>sessionLength</b> (How long a session lasts for).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public IntegerDt getSessionLength() {  
		if (mySessionLength == null) {
			mySessionLength = new IntegerDt();
		}
		return mySessionLength;
	}

	/**
	 * Sets the value(s) for <b>sessionLength</b> (How long a session lasts for)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setSessionLength(IntegerDt theValue) {
		mySessionLength = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>sessionLength</b> (How long a session lasts for)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setSessionLength( int theInteger) {
		mySessionLength = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>contact</b> (Contact details for the user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ContactDt> getContact() {  
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		return myContact;
	}

	/**
	 * Sets the value(s) for <b>contact</b> (Contact details for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setContact(java.util.List<ContactDt> theValue) {
		myContact = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>contact</b> (Contact details for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ContactDt addContact() {
		ContactDt newType = new ContactDt();
		getContact().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>contact</b> (Contact details for the user),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ContactDt getContactFirstRep() {
		if (getContact().isEmpty()) {
			return addContact();
		}
		return getContact().get(0); 
	}
 	/**
	 * Adds a new value for <b>contact</b> (Contact details for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public User addContact( ContactUseEnum theContactUse,  String theValue) {
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		myContact.add(new ContactDt(theContactUse, theValue));
		return this; 
	}

	/**
	 * Adds a new value for <b>contact</b> (Contact details for the user)
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
     *
     * @return Returns a reference to this object, to allow for simple chaining.
	 */
	public User addContact( String theValue) {
		if (myContact == null) {
			myContact = new java.util.ArrayList<ContactDt>();
		}
		myContact.add(new ContactDt(theValue));
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>patient</b> (Patient compartments the user has access to (if level is patient/family)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public java.util.List<ResourceReferenceDt> getPatient() {  
		if (myPatient == null) {
			myPatient = new java.util.ArrayList<ResourceReferenceDt>();
		}
		return myPatient;
	}

	/**
	 * Sets the value(s) for <b>patient</b> (Patient compartments the user has access to (if level is patient/family))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public User setPatient(java.util.List<ResourceReferenceDt> theValue) {
		myPatient = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>patient</b> (Patient compartments the user has access to (if level is patient/family))
	 *
     * <p>
     * <b>Definition:</b>
     * 
     * </p> 
	 */
	public ResourceReferenceDt addPatient() {
		ResourceReferenceDt newType = new ResourceReferenceDt();
		getPatient().add(newType);
		return newType; 
	}
  


}
