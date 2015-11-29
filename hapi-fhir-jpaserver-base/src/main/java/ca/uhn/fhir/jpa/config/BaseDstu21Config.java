package ca.uhn.fhir.jpa.config;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.FhirSearchDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.ISearchDao;

@Configuration
@EnableTransactionManagement
public class BaseDstu21Config extends BaseConfig {

	@Bean
	@Primary
	public FhirContext defaultFhirContext() {
		return fhirContextDstu21();
	}

	@Bean(name = "mySystemDaoDstu21", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<ca.uhn.fhir.model.dstu21.resource.Bundle, ca.uhn.fhir.model.dstu21.composite.MetaDt> systemDaoDstu21() {
		ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu21 retVal = new ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu21();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu21")
	public ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu21 systemProviderDstu2() {
		ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu21 retVal = new ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu21();
		retVal.setDao(systemDaoDstu21());
		return retVal;
	}

	@Bean(name = "myJpaValidationSupportDstu21", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.IJpaValidationSupport jpaValidationSupportDstu2() {
		ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu21 retVal = new ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu21();
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public ISearchDao searchDao() {
		FhirSearchDao searchDao = new FhirSearchDao();
		return searchDao;
	}

}
