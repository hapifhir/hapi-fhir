package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

//START SNIPPET: patientDef

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

import java.util.ArrayList;
import java.util.List;

/**
 * Definition class for adding extensions to the built-in
 * Patient resource type.
 * 
 * Note the "profile" attribute below, which indicates the URL/ID of the
 * profile implemented by this resource. You are not required to supply this,
 * but if you do it will be automatically populated in the resource meta
 * tag if the resource is returned by a server.
 */
@ResourceDef(name="Patient", profile="http://example.com/StructureDefinition/mypatient")
public class MyPatient extends Patient {

   private static final long serialVersionUID = 1L;

   /**
	 * Each extension is defined in a field. Any valid HAPI Data Type
	 * can be used for the field type. Note that the [name=""] attribute
	 * in the @Child annotation needs to match the name for the bean accessor
	 * and mutator methods.
	 */
	@Child(name="petName")	
	@Extension(url="http://example.com/dontuse#petname", definedLocally=false, isModifier=false)
	@Description(shortDefinition="The name of the patient's favourite pet")
	private StringType myPetName;

	/**
	 * The second example extension uses a List type to provide
	 * repeatable values. Note that a [max=] value has been placed in
	 * the @Child annotation.
	 * 
	 * Note also that this extension is a modifier extension
	 */
	@Child(name="importantDates", max=Child.MAX_UNLIMITED)	
	@Extension(url="http://example.com/dontuse#importantDates", definedLocally=false, isModifier=true)
	@Description(shortDefinition="Some dates of note for this patient")
	private List<DateTimeType> myImportantDates;

	/**
	 * It is important to override the isEmpty() method, adding a check for any
	 * newly added fields. 
	 */
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(myPetName, myImportantDates);
	}
	
	/********
	 * Accessors and mutators follow
	 * 
	 * IMPORTANT:
	 * Each extension is required to have an getter/accessor and a setter/mutator.
	 * You are highly recommended to create getters which create instances if they
	 * do not already exist, since this is how the rest of the HAPI FHIR API works. 
	 ********/
	
	/** Getter for important dates */
	public List<DateTimeType> getImportantDates() {
		if (myImportantDates==null) {
			myImportantDates = new ArrayList<DateTimeType>();
		}
		return myImportantDates;
	}

	/** Getter for pet name */
	public StringType getPetName() {
		if (myPetName == null) {
			myPetName = new StringType();
		}
		return myPetName;
	}

	/** Setter for important dates */
	public void setImportantDates(List<DateTimeType> theImportantDates) {
		myImportantDates = theImportantDates;
	}

	/** Setter for pet name */
	public void setPetName(StringType thePetName) {
		myPetName = thePetName;
	}

}
//END SNIPPET: patientDef
