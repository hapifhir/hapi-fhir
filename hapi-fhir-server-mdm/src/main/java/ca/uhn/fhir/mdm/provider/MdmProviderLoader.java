/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.provider;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

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

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	private Supplier<Object> myMdmProviderSupplier;
	private Supplier<Object> myMdmHistoryProviderSupplier;

	public void loadProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
			case R4:
			case R5:
				// We store the supplier so that removeSupplier works properly
				myMdmProviderSupplier = () -> new MdmProviderDstu3Plus(
						myFhirContext,
						myMdmControllerSvc,
						myMdmControllerHelper,
						myMdmSubmitSvc,
						myInterceptorBroadcaster,
						myMdmSettings);
				// We store the supplier so that removeSupplier works properly
				myResourceProviderFactory.addSupplier(myMdmProviderSupplier);
				if (myStorageSettings.isNonResourceDbHistoryEnabled()) {
					myMdmHistoryProviderSupplier = () -> new MdmLinkHistoryProviderDstu3Plus(
							myFhirContext, myMdmControllerSvc, myInterceptorBroadcaster);
					myResourceProviderFactory.addSupplier(myMdmHistoryProviderSupplier);
				}
				break;
			default:
				throw new ConfigurationException(Msg.code(1497) + "MDM not supported for FHIR version "
						+ myFhirContext.getVersion().getVersion());
		}
	}

	@PreDestroy
	public void unloadProvider() {
		if (myMdmProviderSupplier != null) {
			myResourceProviderFactory.removeSupplier(myMdmProviderSupplier);
		}
		if (myMdmHistoryProviderSupplier != null) {
			myResourceProviderFactory.removeSupplier(myMdmHistoryProviderSupplier);
		}
	}
}
