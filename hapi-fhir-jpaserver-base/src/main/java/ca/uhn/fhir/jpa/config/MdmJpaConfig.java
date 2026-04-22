/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.IMdmClearHelperSvc;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkExportMdmEidMatchOnlyResourceExpander;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkExportMdmFullResourceExpander;
import ca.uhn.fhir.jpa.bulk.mdm.MdmClearHelperSvcImpl;
import ca.uhn.fhir.jpa.dao.mdm.JpaMdmLinkImplFactory;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkDaoJpaImpl;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.dao.IMdmLinkImplFactory;
import ca.uhn.fhir.mdm.svc.IBulkExportMdmEidMatchOnlyResourceExpander;
import ca.uhn.fhir.mdm.svc.IBulkExportMdmFullResourceExpander;
import ca.uhn.fhir.mdm.svc.MdmEidMatchOnlyExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmExpandersHolder;
import ca.uhn.fhir.mdm.svc.MdmExpansionCacheSvc;
import ca.uhn.fhir.mdm.svc.MdmLinkExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MdmJpaConfig {

	@Bean
	public MdmExpandersHolder mdmLinkExpandSvcHolder(
			FhirContext theFhirContext,
			IMdmLinkExpandSvc theMdmLinkExpandSvc,
			MdmEidMatchOnlyExpandSvc theMdmEidMatchOnlyLinkExpandSvc,
			IBulkExportMdmEidMatchOnlyResourceExpander<JpaPid> theBulkExportMdmEidMatchOnlyResourceExpander,
			IBulkExportMdmFullResourceExpander<JpaPid> theBulkExportMdmFullResourceExpander) {
		return new MdmExpandersHolder(
				theFhirContext,
				theMdmLinkExpandSvc,
				theMdmEidMatchOnlyLinkExpandSvc,
				theBulkExportMdmFullResourceExpander,
				theBulkExportMdmEidMatchOnlyResourceExpander);
	}

	@Bean
	public MdmEidMatchOnlyExpandSvc mdmEidMatchOnlyLinkExpandSvc(DaoRegistry theDaoRegistry) {
		return new MdmEidMatchOnlyExpandSvc(theDaoRegistry);
	}

	@Bean
	@Primary
	public IMdmLinkExpandSvc mdmLinkExpandSvc() {
		return new MdmLinkExpandSvc();
	}

	@Bean
	public MdmSearchExpansionSvc mdmSearchExpansionSvc() {
		return new MdmSearchExpansionSvc();
	}

	@Bean
	public IMdmLinkDao<JpaPid, MdmLink> mdmLinkDao() {
		return new MdmLinkDaoJpaImpl();
	}

	@Bean
	public BulkExportMdmFullResourceExpander bulkExportMDMResourceExpander(
			MdmExpansionCacheSvc theMdmExpansionCacheSvc,
			IMdmLinkDao theMdmLinkDao,
			IIdHelperService<JpaPid> theIdHelperService,
			DaoRegistry theDaoRegistry,
			FhirContext theFhirContext) {
		return new BulkExportMdmFullResourceExpander(
				theMdmExpansionCacheSvc, theMdmLinkDao, theIdHelperService, theDaoRegistry, theFhirContext);
	}

	@Bean
	public BulkExportMdmEidMatchOnlyResourceExpander bulkExportMDMEidMatchOnlyResourceExpander(
			DaoRegistry theDaoRegistry,
			MdmEidMatchOnlyExpandSvc theMdmEidMatchOnlyLinkExpandSvc,
			FhirContext theFhirContext,
			IIdHelperService<JpaPid> theIdHelperService) {
		return new BulkExportMdmEidMatchOnlyResourceExpander(
				theDaoRegistry, theMdmEidMatchOnlyLinkExpandSvc, theFhirContext, theIdHelperService);
	}

	@Bean
	public IMdmLinkImplFactory<MdmLink> mdmLinkImplFactory() {
		return new JpaMdmLinkImplFactory();
	}

	@Bean
	public IMdmClearHelperSvc<JpaPid> helperSvc(IDeleteExpungeSvc<JpaPid> theDeleteExpungeSvc) {
		return new MdmClearHelperSvcImpl(theDeleteExpungeSvc);
	}
}
