package ca.uhn.fhir.jpa.empi.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiSearchParameterLoader {
	public static final String EMPI_PERSON_ASSURANCE_SEARCH_PARAMETER_ID = "person-assurance";
	public static final String EMPI_PERSON_ACTIVE_SEARCH_PARAMETER_ID = "person-active";
	@Autowired
	public FhirContext myFhirContext;
	@Autowired
	public DaoRegistry myDaoRegistry;

	synchronized public void daoUpdateEmpiSearchParameters() {
		IBaseResource personAssurance;
		IBaseResource personActive;
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				personAssurance = buildAssuranceEmpiSearchParameterDstu3();
				personActive = buildActiveEmpiSearchParameterDstu3();
				break;
			case R4:
				personAssurance = buildAssuranceEmpiSearchParameterR4();
				personActive = buildActiveEmpiSearchParameterR4();
				break;
			default:
				throw new ConfigurationException("EMPI not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}

		IFhirResourceDao<IBaseResource> searchParameterDao = myDaoRegistry.getResourceDao("SearchParameter");
		searchParameterDao.update(personAssurance);
		searchParameterDao.update(personActive);
	}

	private org.hl7.fhir.dstu3.model.SearchParameter buildAssuranceEmpiSearchParameterDstu3() {
		org.hl7.fhir.dstu3.model.SearchParameter retval = new org.hl7.fhir.dstu3.model.SearchParameter();
		retval.setId(EMPI_PERSON_ASSURANCE_SEARCH_PARAMETER_ID);
		retval.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		retval.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_MDM_MANAGED).setCode(EmpiConstants.CODE_HAPI_MDM_MANAGED);
		retval.setCode("assurance");
		retval.addBase("Person");
		retval.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		retval.setDescription("The assurance level of the link on a Person");
		retval.setExpression("Person.link.assurance");
		return retval;
	}

	private SearchParameter buildAssuranceEmpiSearchParameterR4() {
		SearchParameter retval = new SearchParameter();
		retval.setId(EMPI_PERSON_ASSURANCE_SEARCH_PARAMETER_ID);
		retval.setStatus(Enumerations.PublicationStatus.ACTIVE);
		retval.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_MDM_MANAGED).setCode(EmpiConstants.CODE_HAPI_MDM_MANAGED);
		retval.setCode("assurance");
		retval.addBase("Person");
		retval.setType(Enumerations.SearchParamType.TOKEN);
		retval.setDescription("The assurance level of the link on a Person");
		retval.setExpression("Person.link.assurance");
		return retval;
	}

	private org.hl7.fhir.dstu3.model.SearchParameter buildActiveEmpiSearchParameterDstu3() {
		org.hl7.fhir.dstu3.model.SearchParameter retval = new org.hl7.fhir.dstu3.model.SearchParameter();
		retval.setId(EMPI_PERSON_ACTIVE_SEARCH_PARAMETER_ID);
		retval.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		retval.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_MDM_MANAGED).setCode(EmpiConstants.CODE_HAPI_MDM_MANAGED);
		retval.setCode("active");
		retval.addBase("Person");
		retval.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.TOKEN);
		retval.setDescription("The active status of a Person");
		retval.setExpression("Person.active");
		return retval;
	}

	private SearchParameter buildActiveEmpiSearchParameterR4() {
		SearchParameter retval = new SearchParameter();
		retval.setId(EMPI_PERSON_ACTIVE_SEARCH_PARAMETER_ID);
		retval.setStatus(Enumerations.PublicationStatus.ACTIVE);
		retval.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_MDM_MANAGED).setCode(EmpiConstants.CODE_HAPI_MDM_MANAGED);
		retval.setCode("active");
		retval.addBase("Person");
		retval.setType(Enumerations.SearchParamType.TOKEN);
		retval.setDescription("The active status of a Person");
		retval.setExpression("Person.active");
		return retval;
	}
}
