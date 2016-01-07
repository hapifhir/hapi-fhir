package ca.uhn.fhir.jpa.config;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import ca.uhn.fhir.model.dstu2.composite.MetaDt;

@Configuration
@EnableTransactionManagement
public class BaseDstu2Config extends BaseConfig {

	@Bean
	@Primary
	public FhirContext defaultFhirContext() {
		return fhirContextDstu2();
	}

	@Bean(name = "mySystemDaoDstu2", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<ca.uhn.fhir.model.dstu2.resource.Bundle, MetaDt> systemDaoDstu2() {
		ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2 retVal = new ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu2")
	public ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 systemProviderDstu2() {
		ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 retVal = new ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2();
		retVal.setDao(systemDaoDstu2());
		return retVal;
	}

	@Bean(name = "myJpaValidationSupportDstu2", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.IJpaValidationSupportDstu2 jpaValidationSupportDstu2() {
		ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2 retVal = new ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2();
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public ISearchDao searchDao() {
		FhirSearchDao searchDao = new FhirSearchDao();
		return searchDao;
	}

}
