/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.hapi.fhir.cdshooks.api.CdsService;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServiceFeedback;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServicePrefetch;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This bean creates a customer-defined spring context which will
 * in-turn create the various CDS Hooks services that will be
 * supported by this endpoint
 * <p>
 * Note that this class is created as a Spring bean, but it
 * does not use Autowiring! It needs to initialize before autowiring
 * is complete so that other beans can use the stuff it creates.
 */
public class CdsHooksContextBooter {
	protected static final Logger ourLog = LoggerFactory.getLogger(CdsHooksContextBooter.class);
	protected static final String CDS_SERVICES_BEAN_NAME = "cdsServices";
	protected Class<?> myDefinitionsClass;
	protected AnnotationConfigApplicationContext myAppCtx;

	protected List<Object> myCdsServiceBeans = new ArrayList<>();
	protected final CdsServiceCache myCdsServiceCache = new CdsServiceCache();

	public void setDefinitionsClass(Class<?> theDefinitionsClass) {
		myDefinitionsClass = theDefinitionsClass;
	}

	public CdsServiceCache buildCdsServiceCache() {
		for (Object serviceBean : myCdsServiceBeans) {
			extractCdsServices(serviceBean);
		}
		return myCdsServiceCache;
	}

	protected void extractCdsServices(Object theServiceBean) {
		Method[] methods = theServiceBean.getClass().getMethods();
		// Sort alphabetically so service list output is deterministic (to ensure GET /cds-services is idempotent).
		// This also simplifies testing :-)
		List<Method> sortedMethods = Arrays.stream(methods)
				.sorted(Comparator.comparing(Method::getName))
				.collect(Collectors.toList());
		for (Method method : sortedMethods) {
			if (method.isAnnotationPresent(CdsService.class)) {
				CdsService annotation = method.getAnnotation(CdsService.class);
				CdsServiceJson cdsServiceJson = new CdsServiceJson();
				cdsServiceJson.setId(annotation.value());
				cdsServiceJson.setHook(annotation.hook());
				cdsServiceJson.setDescription(annotation.description());
				cdsServiceJson.setTitle(annotation.title());
				cdsServiceJson.setExtension(serializeExtensions(annotation.extension(), annotation.extensionClass()));
				cdsServiceJson.setExtensionClass(annotation.extensionClass());
				for (CdsServicePrefetch prefetch : annotation.prefetch()) {
					cdsServiceJson.addPrefetch(prefetch.value(), prefetch.query());
					cdsServiceJson.addSource(prefetch.value(), prefetch.source());
				}
				myCdsServiceCache.registerService(
						cdsServiceJson.getId(),
						theServiceBean,
						method,
						cdsServiceJson,
						annotation.allowAutoFhirClientPrefetch());
			}
			if (method.isAnnotationPresent(CdsServiceFeedback.class)) {
				CdsServiceFeedback annotation = method.getAnnotation(CdsServiceFeedback.class);
				myCdsServiceCache.registerFeedback(annotation.value(), theServiceBean, method);
			}
		}
	}

	CdsHooksExtension serializeExtensions(String theExtension, Class<? extends CdsHooksExtension> theClass) {
		if (StringUtils.isEmpty(theExtension)) {
			return null;
		}
		try {
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(theExtension, theClass);
		} catch (JsonProcessingException e) {
			final String message = String.format("Invalid JSON: %s", e.getMessage());
			ourLog.debug(message);
			throw new UnprocessableEntityException(Msg.code(2378) + message);
		}
	}

	public void start() {
		if (myDefinitionsClass == null) {
			ourLog.info("No application context defined");
			return;
		}
		ourLog.info("Starting Spring ApplicationContext for class: {}", myDefinitionsClass);

		myAppCtx = new AnnotationConfigApplicationContext();
		myAppCtx.register(myDefinitionsClass);
		myAppCtx.refresh();

		try {
			if (myAppCtx.containsBean(CDS_SERVICES_BEAN_NAME)) {
				myCdsServiceBeans = (List<Object>) myAppCtx.getBean(CDS_SERVICES_BEAN_NAME, List.class);
			} else {
				ourLog.info("Context has no bean named {}", CDS_SERVICES_BEAN_NAME);
			}

			if (myCdsServiceBeans.isEmpty()) {
				throw new ConfigurationException(Msg.code(2379)
						+ "No CDS Services found in the context (need bean called " + CDS_SERVICES_BEAN_NAME + ")");
			}

		} catch (ConfigurationException e) {
			stop();
			throw e;
		} catch (Exception e) {
			stop();
			throw new ConfigurationException(Msg.code(2393) + e.getMessage(), e);
		}
	}

	@PreDestroy
	public void stop() {
		if (myAppCtx != null) {
			ourLog.info("Shutting down CDS Hooks Application context");
			myAppCtx.close();
			myAppCtx = null;
		}
	}
}
