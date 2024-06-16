/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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
package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.svc.MdmSurvivorshipSvcImpl;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MdmSurvivorshipConfig {

	@Autowired
	protected FhirContext myFhirContext;

	@Autowired
	protected DaoRegistry myDaoRegistry;

	@Autowired
	private IMdmSettings myMdmSettings;

	@Autowired
	private EIDHelper myEIDHelper;

	@Autowired
	private MdmPartitionHelper myMdmPartitionHelper;

	@Autowired
	private IMdmLinkQuerySvc myMdmLinkQuerySvc;

	@Autowired
	private IIdHelperService<?> myIIdHelperService;

	@Autowired
	private HapiTransactionService myTransactionService;

	@Bean
	public IMdmSurvivorshipService mdmSurvivorshipService() {
		return new MdmSurvivorshipSvcImpl(
				myFhirContext,
				goldenResourceHelper(),
				myDaoRegistry,
				myMdmLinkQuerySvc,
				myIIdHelperService,
				myTransactionService);
	}

	@Bean
	public GoldenResourceHelper goldenResourceHelper() {
		// do not make this depend on IMdmSurvivorshipService
		return new GoldenResourceHelper(myFhirContext, myMdmSettings, myEIDHelper, myMdmPartitionHelper);
	}
}
