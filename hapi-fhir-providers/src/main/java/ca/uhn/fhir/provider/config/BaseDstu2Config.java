package ca.uhn.fhir.provider.config;

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

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.provider.impl.SystemProviderDstu2;

@Configuration
public class BaseDstu2Config extends BaseConfig {

	@Bean(name = "mySystemProviderDstu2")
	public SystemProviderDstu2 systemProviderDstu2() {
		SystemProviderDstu2 retVal = new SystemProviderDstu2();
		retVal.setSystemDaoFactory(fhirDaoFactory());
		return retVal;
	}
	
	// DAO Factory gets wired from DAO implementation bundle
	@Resource(name="myDaoFactoryDstu2")
	private IDaoFactory myDaoFactoryDstu2;

	public IDaoFactory fhirDaoFactory() {
		return myDaoFactoryDstu2;
	}

}
