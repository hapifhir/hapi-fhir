/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.dao.TransactionProcessorVersionAdapterDstu2;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.jpa.term.TermVersionAdapterSvcDstu2;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Import({FhirContextDstu2Config.class, GeneratedDaoAndResourceProviderConfigDstu2.class, JpaConfig.class})
public class JpaDstu2Config {
	@Bean
	public ITransactionProcessorVersionAdapter transactionProcessorVersionFacade() {
		return new TransactionProcessorVersionAdapterDstu2();
	}

	@Bean
	public ITermVersionAdapterSvc translationAdaptorVersion() {
		return new TermVersionAdapterSvcDstu2();
	}

	@Bean(name = "mySystemDaoDstu2")
	public IFhirSystemDao<Bundle, MetaDt> systemDaoDstu2() {
		ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2 retVal = new ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu2")
	public JpaSystemProvider<Bundle, MetaDt> systemProviderDstu2(FhirContext theFhirContext) {
		JpaSystemProvider<Bundle, MetaDt> retVal = new ca.uhn.fhir.jpa.provider.JpaSystemProvider<>();
		retVal.setDao(systemDaoDstu2());
		retVal.setContext(theFhirContext);
		return retVal;
	}
}
