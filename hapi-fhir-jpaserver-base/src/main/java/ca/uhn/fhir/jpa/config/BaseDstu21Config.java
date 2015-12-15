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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.dao.IFhirResourceDao;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.dao.ISearchDao;
import ca.uhn.fhir.jpa.dao.FhirSearchDao;
import ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu21;
import ca.uhn.fhir.jpa.dao.IJpaFhirSystemDao;
import ca.uhn.fhir.jpa.dao.JpaDaoFactory;
import ca.uhn.fhir.model.dstu21.composite.MetaDt;
import ca.uhn.fhir.model.dstu21.resource.Bundle;

@Configuration
@EnableTransactionManagement
public class BaseDstu21Config extends BaseConfig {

	private static FhirContext ourFhirContextDstu21;
	private static FhirContext ourFhirContextDstu2Hl7Org;

	@Bean(name = "myFhirContextDstu21")
	@Lazy
	public FhirContext fhirContextDstu21() {
		if (ourFhirContextDstu21 == null) {
			ourFhirContextDstu21 = FhirContext.forDstu2_1();
		}
		return ourFhirContextDstu21;
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
		return fhirContextDstu21();
	}
	
	private static JpaDaoFactory myDaoFactoryDstu21 = null;
	
	@Bean(name="myDaoFactoryDstu21")
	public IDaoFactory fhirDaoFactoryDstu21() {
		if (null == myDaoFactoryDstu21) {
			myDaoFactoryDstu21 = new JpaDaoFactory();
			myDaoFactoryDstu21.setFhirContext(defaultFhirContext());
		}
		return myDaoFactoryDstu21;
	}

	private static IJpaFhirSystemDao<Bundle, MetaDt> mySystemDaoDstu21 = null;
	
	@Bean(name = "mySystemDaoDstu21", autowire = Autowire.BY_NAME)
	public IJpaFhirSystemDao<Bundle, MetaDt> fhirSystemDaoDstu21() {
		if (null == mySystemDaoDstu21) {
			mySystemDaoDstu21 = new FhirSystemDaoDstu21();
			((FhirSystemDaoDstu21)mySystemDaoDstu21).setContext(defaultFhirContext());
			((FhirSystemDaoDstu21)mySystemDaoDstu21).setDaoFactory(fhirDaoFactoryDstu21());
		}
		return mySystemDaoDstu21;
	}

	private static FhirSearchDao mySearchDaoDstu21 = null;

	@Bean(name="mySearchDaoDstu21", autowire = Autowire.BY_NAME)
	public ISearchDao fhirSearchDaoDstu21() {
		if (null == mySearchDaoDstu21) { 
			mySearchDaoDstu21 = new FhirSearchDao();
			mySearchDaoDstu21.setContext(defaultFhirContext());
			mySearchDaoDstu21.setDaoFactory(fhirDaoFactoryDstu21());
		}
		return mySearchDaoDstu21;
	}

	@Bean(name = "myJpaValidationSupportDstu21", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.IJpaValidationSupport jpaValidationSupportDstu21() {
		ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu21 retVal = new ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu21();
		return retVal;
	}

}
