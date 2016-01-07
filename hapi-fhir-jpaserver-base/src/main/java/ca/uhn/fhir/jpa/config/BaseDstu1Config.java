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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;

@Configuration
public class BaseDstu1Config extends BaseConfig {

	@Bean
	@Primary
	public FhirContext defaultFhirContext() {
		return fhirContextDstu1();
	}

	@Bean(name = "mySystemDaoDstu1", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.IFhirSystemDao<List<IResource>, MetaDt> fhirSystemDaoDstu1() {
		ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu1 retVal = new ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu1();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu1")
	public ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1 systemDaoDstu1() {
		ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1 retVal = new ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu1();
		retVal.setDao(fhirSystemDaoDstu1());
		return retVal;
	}

}
