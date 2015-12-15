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

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.dao.IFhirResourceDao;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirSystemDao;
import ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu1;
import ca.uhn.fhir.jpa.dao.IJpaFhirSystemDao;
import ca.uhn.fhir.jpa.dao.JpaDaoFactory;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;

@Configuration
public class BaseDstu1Config extends BaseConfig {

	private static FhirContext ourFhirContextDstu1;

	@Bean(name = "myFhirContextDstu1")
	@Lazy
	public FhirContext fhirContextDstu1() {
		if (ourFhirContextDstu1 == null) {
			ourFhirContextDstu1 = FhirContext.forDstu1();
		}
		return ourFhirContextDstu1;
	}
	
	@Bean
	@Primary
	public FhirContext defaultFhirContext() {
		return fhirContextDstu1();
	}
	
	private static JpaDaoFactory myDaoFactoryDstu1 = null;
	
	@Bean(name="myDaoFactoryDstu1")
	public IDaoFactory fhirDaoFactoryDstu1() {
		if (null == myDaoFactoryDstu1) {
			myDaoFactoryDstu1 = new JpaDaoFactory();
			myDaoFactoryDstu1.setFhirContext(defaultFhirContext());
		}
		return myDaoFactoryDstu1;
	}

	private static IJpaFhirSystemDao<List<IResource>, MetaDt> mySystemDaoDstu1 = null;
	
	@Bean(name = "mySystemDaoDstu1", autowire = Autowire.BY_NAME)
	public IJpaFhirSystemDao<List<IResource>, MetaDt> fhirSystemDaoDstu1() {
		if (null == mySystemDaoDstu1) {
			mySystemDaoDstu1 = new FhirSystemDaoDstu1();
			((FhirSystemDaoDstu1)mySystemDaoDstu1).setContext(defaultFhirContext());
			((FhirSystemDaoDstu1)mySystemDaoDstu1).setDaoFactory(fhirDaoFactoryDstu1());
		}
		return mySystemDaoDstu1;
	}

}
