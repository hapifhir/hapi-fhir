package ca.uhn.fhir.cql.provider;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CqlProviderLoader {
	private static final Logger myLogger = LoggerFactory.getLogger(CqlProviderLoader.class);
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;
	@Autowired
	private CqlProviderFactory myCqlProviderFactory;
	@Autowired
	private ApplicationContext myApplicationContext;

	private Lock myProviderRegistrationMutex = new ReentrantLock();
	private boolean myStarted;
	private final List<Object> myPlainProviders = new ArrayList<>();
	private final List<IResourceProvider> myResourceProviders = new ArrayList<>();

	public void loadProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				myResourceProviderFactory.addSupplier(() -> buildDstu3Provider());
				break;
			case R4:
				myResourceProviderFactory.addSupplier(() -> buildR4Provider());
				break;
			default:
				throw new ConfigurationException("CQL not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}
	}

	@VisibleForTesting
	org.opencds.cqf.dstu3.providers.MeasureOperationsProvider buildDstu3Provider() {
		return myCqlProviderFactory.getMeasureOperationsProviderDstu3();
	}

	@VisibleForTesting
	org.opencds.cqf.r4.providers.MeasureOperationsProvider buildR4Provider() {
		return myCqlProviderFactory.getMeasureOperationsProviderR4();
	}
}
