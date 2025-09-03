/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.svc.IMdmClearHelperSvc;
import ca.uhn.fhir.jpa.bulk.mdm.MdmClearHelperSvcImpl;
import ca.uhn.fhir.jpa.dao.mdm.JpaMdmLinkImplFactory;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkDaoJpaImpl;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.dao.IMdmLinkImplFactory;
import ca.uhn.fhir.mdm.svc.BulkExportMDMEidMatchOnlyResourceExpander;
import ca.uhn.fhir.mdm.svc.BulkExportMDMResourceExpander;
import ca.uhn.fhir.mdm.svc.MdmEidMatchOnlyLinkExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmLinkExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmLinkExpandersHolder;
import ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MdmJpaConfig {

	@Bean
	public MdmLinkExpandersHolder mdmLinkExpandSvcHolder(
			IMdmLinkExpandSvc theMdmLinkExpandSvc,
			MdmEidMatchOnlyLinkExpandSvc theMdmEidMatchOnlyLinkExpandSvc,
			BulkExportMDMEidMatchOnlyResourceExpander theBulkExportMdmEidMatchOnlyResourceExpander,
			BulkExportMDMResourceExpander theBulkExportMdmResourceExpander,
			FhirContext theFhirContext) {
		return new MdmLinkExpandersHolder(
				theMdmLinkExpandSvc,
				theMdmEidMatchOnlyLinkExpandSvc,
				theBulkExportMdmResourceExpander,
				theBulkExportMdmEidMatchOnlyResourceExpander,
				theFhirContext);
	}

	@Bean
	// @Qualifier("mdmEidMatchOnlyLinkExpandSvc")
	public MdmEidMatchOnlyLinkExpandSvc mdmEidMatchOnlyLinkExpandSvc(DaoRegistry theDaoRegistry) {
		return new MdmEidMatchOnlyLinkExpandSvc(theDaoRegistry);
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
	public BulkExportMDMResourceExpander bulkExportMDMResourceExpander() {
		return new BulkExportMDMResourceExpander();
	}

	@Bean
	public BulkExportMDMEidMatchOnlyResourceExpander bulkExportMDMEidMatchOnlyResourceExpander() {
		return new BulkExportMDMEidMatchOnlyResourceExpander();
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
