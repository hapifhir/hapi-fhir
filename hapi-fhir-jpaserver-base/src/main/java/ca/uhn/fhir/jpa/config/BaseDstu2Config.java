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

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.dao.ISearchDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.FhirSearchDao;
import ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2;
import ca.uhn.fhir.jpa.dao.IJpaFhirSystemDao;
import ca.uhn.fhir.jpa.dao.JpaDaoFactory;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;

@Configuration
@EnableTransactionManagement
public class BaseDstu2Config extends BaseConfig {

	private static FhirContext ourFhirContextDstu2;
	private static FhirContext ourFhirContextDstu2Hl7Org;

	@Bean(name = "myFhirContextDstu2")
	@Lazy
	public FhirContext fhirContextDstu2() {
		if (ourFhirContextDstu2 == null) {
			ourFhirContextDstu2 = FhirContext.forDstu2();
		}
		return ourFhirContextDstu2;
	}

	@Bean(name = "myFhirContextDstu2Hl7Org")
	@Lazy
	public FhirContext fhirContextDstu2Hl7Org() {
		if (ourFhirContextDstu2Hl7Org == null) {
			ourFhirContextDstu2Hl7Org = FhirContext.forDstu2Hl7Org();
		}
		return ourFhirContextDstu2Hl7Org;
	}

	@Bean
	@Primary
	public FhirContext defaultFhirContext() {
		return fhirContextDstu2();
	}
	
	private static JpaDaoFactory myDaoFactoryDstu2 = null;
	
	@Bean(name="myDaoFactoryDstu2")
	public IDaoFactory fhirDaoFactoryDstu2() {
		if (null == myDaoFactoryDstu2) {
			myDaoFactoryDstu2 = new JpaDaoFactory();
			myDaoFactoryDstu2.setFhirContext(defaultFhirContext());
		}
		return myDaoFactoryDstu2;
	}

	private static IJpaFhirSystemDao<Bundle, MetaDt> mySystemDaoDstu2 = null;
	
	@Bean(name = "mySystemDaoDstu2", autowire = Autowire.BY_NAME)
	public IJpaFhirSystemDao<Bundle, MetaDt> fhirSystemDaoDstu2() {
		if (null == mySystemDaoDstu2) {
			mySystemDaoDstu2 = new FhirSystemDaoDstu2();
			((FhirSystemDaoDstu2)mySystemDaoDstu2).setContext(defaultFhirContext());
			((FhirSystemDaoDstu2)mySystemDaoDstu2).setDaoFactory(fhirDaoFactoryDstu2());
		}
		return mySystemDaoDstu2;
	}

	private static FhirSearchDao mySearchDaoDstu2 = null;

	@Bean(name="mySearchDaoDstu2", autowire = Autowire.BY_NAME)
	public ISearchDao fhirSearchDaoDstu2() {
		if (null == mySearchDaoDstu2) { 
			mySearchDaoDstu2 = new FhirSearchDao();
			mySearchDaoDstu2.setContext(defaultFhirContext());
			mySearchDaoDstu2.setDaoFactory(fhirDaoFactoryDstu2());
		}
		return mySearchDaoDstu2;
	}

	@Bean(name = "myJpaValidationSupportDstu2", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.IJpaValidationSupport jpaValidationSupportDstu2() {
		ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2 retVal = new ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2();
		return retVal;
	}

}
