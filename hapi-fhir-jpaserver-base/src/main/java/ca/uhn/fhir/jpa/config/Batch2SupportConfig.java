/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.expunge.ResourceTableFKProvider;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.delete.batch2.DeleteExpungeSqlBuilder;
import ca.uhn.fhir.jpa.delete.batch2.DeleteExpungeSvcImpl;
import ca.uhn.fhir.jpa.reindex.Batch2DaoSvcImpl;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class Batch2SupportConfig {

	@Bean
	public IBatch2DaoSvc batch2DaoSvc(
			IResourceTableDao theResourceTableDao,
			MatchUrlService theMatchUrlService,
			DaoRegistry theDaoRegistry,
			FhirContext theFhirContext,
			IHapiTransactionService theTransactionService) {
		return new Batch2DaoSvcImpl(
				theResourceTableDao, theMatchUrlService, theDaoRegistry, theFhirContext, theTransactionService);
	}

	@Bean
	public IDeleteExpungeSvc deleteExpungeSvc(
			EntityManager theEntityManager,
			DeleteExpungeSqlBuilder theDeleteExpungeSqlBuilder,
			@Autowired(required = false) IFulltextSearchSvc theFullTextSearchSvc) {
		return new DeleteExpungeSvcImpl(theEntityManager, theDeleteExpungeSqlBuilder, theFullTextSearchSvc);
	}

	@Bean
	DeleteExpungeSqlBuilder deleteExpungeSqlBuilder(
			ResourceTableFKProvider theResourceTableFKProvider,
			JpaStorageSettings theStorageSettings,
			IIdHelperService theIdHelper,
			IResourceLinkDao theResourceLinkDao) {
		return new DeleteExpungeSqlBuilder(
				theResourceTableFKProvider, theStorageSettings, theIdHelper, theResourceLinkDao);
	}
}
