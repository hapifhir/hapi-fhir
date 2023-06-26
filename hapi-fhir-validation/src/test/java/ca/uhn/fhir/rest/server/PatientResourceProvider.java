
package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.UriAndListParam;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;

import java.util.Set;

// import ca.uhn.fhir.model.dstu.resource.Binary;
// import ca.uhn.fhir.model.dstu2.resource.Bundle;
// import ca.uhn.fhir.model.api.Bundle;


public class PatientResourceProvider implements IResourceProvider
	{

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

	@Search()
	public IBundleProvider search(
			javax.servlet.http.HttpServletRequest theServletRequest,
	
			@Description(shortDefinition="The resource identity")
			@OptionalParam(name="_id")
			StringAndListParam theId, 

			@Description(shortDefinition="The resource language")
			@OptionalParam(name="_language")
			StringAndListParam theResourceLanguage, 

			@Description(shortDefinition="Search the contents of the resource's data using a fulltext search")
			@OptionalParam(name=Constants.PARAM_CONTENT)
			StringAndListParam theFtContent, 

			@Description(shortDefinition="Search the contents of the resource's narrative using a fulltext search")
			@OptionalParam(name=Constants.PARAM_TEXT)
			StringAndListParam theFtText, 

			@Description(shortDefinition="Search for resources which have the given tag")
			@OptionalParam(name=Constants.PARAM_TAG)
			TokenAndListParam theSearchForTag, 

			@Description(shortDefinition="Search for resources which have the given security labels")
			@OptionalParam(name=Constants.PARAM_SECURITY)
			TokenAndListParam theSearchForSecurity, 
  
			@Description(shortDefinition="Search for resources which have the given profile")
			@OptionalParam(name=Constants.PARAM_PROFILE)
			UriAndListParam theSearchForProfile, 

  
			@Description(shortDefinition="A patient identifier")
			@OptionalParam(name="identifier")
			TokenAndListParam theIdentifier, 
  
			@Description(shortDefinition="A portion of either family or given name of the patient")
			@OptionalParam(name="name")
			StringAndListParam theName, 
  
			@Description(shortDefinition="A portion of the family name of the patient")
			@OptionalParam(name="family")
			StringAndListParam theFamily, 
  
			@Description(shortDefinition="A portion of the given name of the patient")
			@OptionalParam(name="given")
			StringAndListParam theGiven, 
  
			@Description(shortDefinition="A portion of either family or given name using some kind of phonetic matching algorithm")
			@OptionalParam(name="phonetic")
			StringAndListParam thePhonetic, 
  
			@Description(shortDefinition="The value in any kind of telecom details of the patient")
			@OptionalParam(name="telecom")
			TokenAndListParam theTelecom, 
  
			@Description(shortDefinition="A value in a phone contact")
			@OptionalParam(name="phone")
			TokenAndListParam thePhone, 
  
			@Description(shortDefinition="A value in an email contact")
			@OptionalParam(name="email")
			TokenAndListParam theEmail, 
  
			@Description(shortDefinition="An address in any kind of address/part of the patient")
			@OptionalParam(name="address")
			StringAndListParam theAddress, 
  
			@Description(shortDefinition="A city specified in an address")
			@OptionalParam(name="address-city")
			StringAndListParam theAddress_city, 
  
			@Description(shortDefinition="A state specified in an address")
			@OptionalParam(name="address-state")
			StringAndListParam theAddress_state, 
  
			@Description(shortDefinition="A postalCode specified in an address")
			@OptionalParam(name="address-postalcode")
			StringAndListParam theAddress_postalcode, 
  
			@Description(shortDefinition="A country specified in an address")
			@OptionalParam(name="address-country")
			StringAndListParam theAddress_country, 
  
			@Description(shortDefinition="A use code specified in an address")
			@OptionalParam(name="address-use")
			TokenAndListParam theAddress_use, 
  
			@Description(shortDefinition="Gender of the patient")
			@OptionalParam(name="gender")
			TokenAndListParam theGender, 
  
			@Description(shortDefinition="Language code (irrespective of use value)")
			@OptionalParam(name="language")
			TokenAndListParam theLanguage, 
  
			@Description(shortDefinition="The patient's date of birth")
			@OptionalParam(name="birthdate")
			DateRangeParam theBirthdate, 
  
			@Description(shortDefinition="The organization at which this person is a patient")
			@OptionalParam(name="organization", targetTypes={  Organization.class   } )
			ReferenceAndListParam theOrganization, 
  
			@Description(shortDefinition="Patient's nominated care provider, could be a care manager, not the organization that manages the record")
			@OptionalParam(name="careprovider", targetTypes={  Organization.class ,   Practitioner.class   } )
			ReferenceAndListParam theCareprovider, 
  
			@Description(shortDefinition="Whether the patient record is active")
			@OptionalParam(name="active")
			TokenAndListParam theActive, 
  
			@Description(shortDefinition="The species for animal patients")
			@OptionalParam(name="animal-species")
			TokenAndListParam theAnimal_species, 
  
			@Description(shortDefinition="The breed for animal patients")
			@OptionalParam(name="animal-breed")
			TokenAndListParam theAnimal_breed, 
  
			@Description(shortDefinition="All patients linked to the given patient")
			@OptionalParam(name="link", targetTypes={  Patient.class   } )
			ReferenceAndListParam theLink, 
  
			@Description(shortDefinition="This patient has been marked as deceased, or as a death date entered")
			@OptionalParam(name="deceased")
			TokenAndListParam theDeceased, 
  
			@Description(shortDefinition="The date of death has been provided and satisfies this search value")
			@OptionalParam(name="deathdate")
			DateRangeParam theDeathdate, 

			@IncludeParam(reverse=true)
			Set<Include> theRevIncludes,
			@Description(shortDefinition="Only return resources which were last updated as specified by the given range")
			@OptionalParam(name="_lastUpdated")
			DateRangeParam theLastUpdated, 

			@IncludeParam(allow= {
					"Patient:careprovider" , 					"Patient:link" , 					"Patient:organization" , 						"Patient:careprovider" , 					"Patient:link" , 					"Patient:organization" , 						"Patient:careprovider" , 					"Patient:link" , 					"Patient:organization" 					, "*"
			}) 
			Set<Include> theIncludes,
			
			@Sort 
			SortSpec theSort,
			
			@Count
			Integer theCount
	) {
		return null;
	}

}
