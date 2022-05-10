package ca.uhn.fhir.mdm.provider;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class MdmProviderLoader {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;
	@Autowired
	private MdmControllerHelper myMdmControllerHelper;
	@Autowired
	private IMdmControllerSvc myMdmControllerSvc;
	@Autowired
	private IMdmSubmitSvc myMdmSubmitSvc;
	@Autowired
	private IMdmSettings myMdmSettings;

	private BaseMdmProvider myMdmProvider;

	public void loadProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
			case R4:
				myResourceProviderFactory.addSupplier(() -> {
					myMdmProvider = new MdmProviderDstu3Plus(myFhirContext,
						myMdmControllerSvc,
						myMdmControllerHelper,
						myMdmSubmitSvc,
						myMdmSettings);
					return myMdmProvider;
				});
				break;
			default:
				throw new ConfigurationException(Msg.code(1497) + "MDM not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}
	}

	@PreDestroy
	public void unloadProvider() {
		myResourceProviderFactory.removeSupplier(() -> myMdmProvider);
	}
}

