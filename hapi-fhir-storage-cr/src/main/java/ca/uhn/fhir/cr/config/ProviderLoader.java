/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class ProviderLoader {
	private static final Logger myLogger = LoggerFactory.getLogger(ProviderLoader.class);
	private final ApplicationContext myApplicationContext;
	private final ProviderSelector myProviderSelector;
	private final RestfulServer myRestfulServer;

	public ProviderLoader(
			RestfulServer theRestfulServer,
			ApplicationContext theApplicationContext,
			ProviderSelector theProviderSelector) {
		myApplicationContext = theApplicationContext;
		myProviderSelector = theProviderSelector;
		myRestfulServer = theRestfulServer;
	}

	@EventListener(ContextRefreshedEvent.class)
	public void loadProviders() {
		var type = myProviderSelector.getProviderType();
		if (type == null) {
			throw new ConfigurationException(Msg.code(1653) + "Provider not supported for the current FHIR version");
		}
		for (Class<?> op : type) {
			myLogger.info("loading provider: {}", op);
			myRestfulServer.registerProvider(myApplicationContext.getBean(op));
		}
	}
}
