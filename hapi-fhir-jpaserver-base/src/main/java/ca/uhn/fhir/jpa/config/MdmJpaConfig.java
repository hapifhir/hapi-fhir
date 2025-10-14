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
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.dao.IMdmLinkImplFactory;
import ca.uhn.fhir.mdm.svc.DisabledMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmEidMatchOnlyExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmLinkExpandSvc;
import ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(IMdmSettings.class)
public class MdmJpaConfig {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmJpaConfig.class);

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

	/**
	 * Based on the rules laid out in the {@link IMdmSettings} file, construct an {@link IMdmLinkExpandSvc} that is suitable
	 */
	@Bean
	public IMdmLinkExpandSvc mdmLinkExpandSvc(
			EIDHelper theEidHelper,
			IMdmSettings theMdmSettings,
			DaoRegistry theDaoRegistry,
			FhirContext theFhirContext,
			IIdHelperService theIdHelperService) {
		if (theMdmSettings == null) {
			return new DisabledMdmLinkExpandSvc();
		}
		if (theMdmSettings.supportsLinkBasedExpansion()) {
			return new MdmLinkExpandSvc();
		} else if (theMdmSettings.supportsEidBasedExpansion()) {
			return new MdmEidMatchOnlyExpandSvc(theDaoRegistry, theFhirContext, theIdHelperService, theEidHelper);
		}
		return new DisabledMdmLinkExpandSvc();
	}

	@Bean
	EIDHelper eidHelper(FhirContext theFhirContext, @Nullable IMdmSettings theMdmSettings) {
		if (theMdmSettings == null) {
			ourLog.warn("Loading up an EID helper without an IMdmSetting bean defined! MDM will _not_ work!");
		}
		return new EIDHelper(theFhirContext, theMdmSettings);
	}

	@Bean
	public IMdmClearHelperSvc<JpaPid> mdmClearHelperSvc(IDeleteExpungeSvc<JpaPid> theDeleteExpungeSvc) {
		return new MdmClearHelperSvcImpl(theDeleteExpungeSvc);
	}
}
