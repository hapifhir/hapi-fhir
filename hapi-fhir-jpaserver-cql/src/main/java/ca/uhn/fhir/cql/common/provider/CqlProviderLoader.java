package ca.uhn.fhir.cql.common.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CqlProviderLoader {
	private static final Logger myLogger = LoggerFactory.getLogger(CqlProviderLoader.class);
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;
	@Autowired
	private CqlProviderFactory myCqlProviderFactory;

	@PostConstruct
	public void loadProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
			case R4:
				myLogger.info("Registering CQL Provider");
				myResourceProviderFactory.addSupplier(() -> myCqlProviderFactory.getMeasureOperationsProvider());
				break;
			default:
				throw new ConfigurationException(Msg.code(1653) + "CQL not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}
	}
}
