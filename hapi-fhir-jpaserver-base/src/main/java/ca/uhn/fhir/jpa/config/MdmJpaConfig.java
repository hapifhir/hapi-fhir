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
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.IMdmClearHelperSvc;
import ca.uhn.fhir.jpa.bulk.mdm.MdmClearHelperSvcImpl;
import ca.uhn.fhir.jpa.dao.mdm.JpaMdmLinkImplFactory;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkDaoJpaImpl;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmModeEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.dao.IMdmLinkImplFactory;
import ca.uhn.fhir.mdm.svc.BulkExportMdmEidMatchOnlyResourceExpander;
import ca.uhn.fhir.mdm.svc.BulkExportMdmResourceExpander;
import ca.uhn.fhir.mdm.svc.IBulkExportMdmResourceExpander;
import ca.uhn.fhir.mdm.svc.MdmEidMatchOnlyExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmExpansionCacheSvc;
import ca.uhn.fhir.mdm.svc.MdmLinkExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MdmJpaConfig {

	@Bean
	public MdmEidMatchOnlyExpandSvc mdmEidMatchOnlyLinkExpandSvc(
			IMdmSettings theMdmSettings, DaoRegistry theDaoRegistry, FhirContext theFhirContext) {
		MdmEidMatchOnlyExpandSvc mdmEidMatchOnlyExpandSvc = new MdmEidMatchOnlyExpandSvc(theDaoRegistry);
		mdmEidMatchOnlyExpandSvc.setMyEidHelper(new EIDHelper(theFhirContext, theMdmSettings));
		return mdmEidMatchOnlyExpandSvc;
	}

	@Bean
	@Primary
	public IMdmLinkExpandSvc mdmLinkExpandSvc(
			IMdmSettings theMdmSettings, DaoRegistry theDaoRegistry, FhirContext theFhirContext) {
		IMdmLinkExpandSvc res;
		if (isEidMatchOnly(theMdmSettings)) {
			res = mdmEidMatchOnlyLinkExpandSvc(theMdmSettings, theDaoRegistry, theFhirContext);
		} else {
			res = new MdmLinkExpandSvc();
		}
		return res;
	}

	@Bean
	public IBulkExportMdmResourceExpander bulkExportMdmResourceExpander(
			MdmExpansionCacheSvc theMdmExpansionCacheSvc,
			IMdmLinkDao theMdmLinkDao,
			IIdHelperService<JpaPid> theIdHelperService,
			DaoRegistry theDaoRegistry,
			FhirContext theFhirContext,
			MdmEidMatchOnlyExpandSvc theMdmEidMatchOnlyLinkExpandSvc,
			IMdmSettings theMdmSettings) {

		IBulkExportMdmResourceExpander res;
		if (isEidMatchOnly(theMdmSettings)) {
			res = new BulkExportMdmEidMatchOnlyResourceExpander(
					theDaoRegistry, theMdmEidMatchOnlyLinkExpandSvc, theFhirContext, theIdHelperService);
		} else {
			res = new BulkExportMdmResourceExpander(
					theMdmExpansionCacheSvc, theMdmLinkDao, theIdHelperService, theDaoRegistry, theFhirContext);
		}
		return res;
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
	public IMdmLinkImplFactory<MdmLink> mdmLinkImplFactory() {
		return new JpaMdmLinkImplFactory();
	}

	@Bean
	public IMdmClearHelperSvc<JpaPid> helperSvc(IDeleteExpungeSvc<JpaPid> theDeleteExpungeSvc) {
		return new MdmClearHelperSvcImpl(theDeleteExpungeSvc);
	}

	private boolean isEidMatchOnly(IMdmSettings theMdmSettings) {
		return theMdmSettings.getMode() == MdmModeEnum.MATCH_ONLY
				&& theMdmSettings.getMdmRules() != null
				&& theMdmSettings.getMdmRules().getEnterpriseEIDSystems() != null
				&& !theMdmSettings.getMdmRules().getEnterpriseEIDSystems().isEmpty();
	}
}
