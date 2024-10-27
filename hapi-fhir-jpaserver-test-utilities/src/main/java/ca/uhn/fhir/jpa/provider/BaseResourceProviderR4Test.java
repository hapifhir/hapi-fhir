/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeProvider;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexProvider;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.data.IPartitionDao;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerConfigurerExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.cors.CorsConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ContextConfiguration(classes = ServerConfiguration.class)
public abstract class BaseResourceProviderR4Test extends BaseJpaR4Test {

	@RegisterExtension
	protected static HttpClientExtension ourHttpClient = new HttpClientExtension();

	protected int myPort;
	protected String myServerBase;
	protected IGenericClient myClient;

	@Autowired
	@RegisterExtension
	protected RestfulServerExtension myServer;

	@RegisterExtension
	protected RestfulServerConfigurerExtension myServerConfigurer = new RestfulServerConfigurerExtension(() -> myServer)
			.withServerBeforeAll(s -> {
				s.registerProviders(myResourceProviders.createProviders());
				s.setDefaultResponseEncoding(EncodingEnum.XML);
				s.setDefaultPrettyPrint(false);

				myFhirContext.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

				s.registerProvider(mySystemProvider);
				s.registerProvider(myBinaryAccessProvider);
				s.registerProvider(myAppCtx.getBean(BulkDataExportProvider.class));
				s.registerProvider(myAppCtx.getBean(DeleteExpungeProvider.class));
				s.registerProvider(myAppCtx.getBean(DiffProvider.class));
				s.registerProvider(myAppCtx.getBean(GraphQLProvider.class));
				s.registerProvider(myAppCtx.getBean(ProcessMessageProvider.class));
				s.registerProvider(myAppCtx.getBean(ReindexProvider.class));
				s.registerProvider(myAppCtx.getBean(SubscriptionTriggeringProvider.class));
				s.registerProvider(myAppCtx.getBean(TerminologyUploaderProvider.class));
				s.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class));

				s.setPagingProvider(myAppCtx.getBean(DatabaseBackedPagingProvider.class));

				JpaCapabilityStatementProvider confProvider = new JpaCapabilityStatementProvider(
						s, mySystemDao, myStorageSettings, mySearchParamRegistry, myValidationSupport);
				confProvider.setImplementationDescription("THIS IS THE DESC");
				s.setServerConformanceProvider(confProvider);

				// Register a CORS filter
				CorsConfiguration config = new CorsConfiguration();
				CorsInterceptor corsInterceptor = new CorsInterceptor(config);
				config.addAllowedHeader("Accept");
				config.addAllowedHeader("Access-Control-Request-Headers");
				config.addAllowedHeader("Access-Control-Request-Method");
				config.addAllowedHeader("Cache-Control");
				config.addAllowedHeader("Content-Type");
				config.addAllowedHeader("Origin");
				config.addAllowedHeader("Prefer");
				config.addAllowedHeader("x-fhir-starter");
				config.addAllowedHeader("X-Requested-With");
				config.addAllowedOrigin("*");
				config.addExposedHeader("Location");
				config.addExposedHeader("Content-Location");
				config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
				s.registerInterceptor(corsInterceptor);
			})
			.withServerBeforeEach(s -> {
				myPort = myServer.getPort();
				myServerBase = myServer.getBaseUrl();
				myClient = myServer.getFhirClient();

				myClient.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof LoggingInterceptor);
				if (shouldLogClient()) {
					myClient.registerInterceptor(new LoggingInterceptor());
				}
			});

	@Autowired
	protected SubscriptionLoader mySubscriptionLoader;

	@Autowired
	protected DaoRegistry myDaoRegistry;

	@Autowired
	protected IPartitionDao myPartitionDao;

	@Autowired
	protected ResourceCountCache myResourceCountsCache;

	public BaseResourceProviderR4Test() {
		super();
	}

	@AfterEach
	public void after() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		myServer.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	protected boolean shouldLogClient() {
		return true;
	}

	protected List<String> toNameList(Bundle resp) {
		List<String> names = new ArrayList<>();
		for (BundleEntryComponent next : resp.getEntry()) {
			Patient nextPt = (Patient) next.getResource();
			String nextStr = nextPt.getName().size() > 0
					? nextPt.getName().get(0).getGivenAsSingleString() + " "
							+ nextPt.getName().get(0).getFamily()
					: "";
			if (isNotBlank(nextStr)) {
				names.add(nextStr);
			}
		}
		return names;
	}

	public static int getNumberOfParametersByName(Parameters theParameters, String theName) {
		int retVal = 0;

		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				retVal++;
			}
		}

		return retVal;
	}

	public static ParametersParameterComponent getParameterByName(Parameters theParameters, String theName) {
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				return param;
			}
		}

		return new ParametersParameterComponent();
	}

	public static List<ParametersParameterComponent> getParametersByName(Parameters theParameters, String theName) {
		List<ParametersParameterComponent> params = new ArrayList<>();
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				params.add(param);
			}
		}

		return params;
	}

	public static ParametersParameterComponent getPartByName(
			ParametersParameterComponent theParameter, String theName) {
		for (ParametersParameterComponent part : theParameter.getPart()) {
			if (part.getName().equals(theName)) {
				return part;
			}
		}

		return new ParametersParameterComponent();
	}

	public static boolean hasParameterByName(Parameters theParameters, String theName) {
		for (ParametersParameterComponent param : theParameters.getParameter()) {
			if (param.getName().equals(theName)) {
				return true;
			}
		}

		return false;
	}

	protected List<String> searchAndReturnUnqualifiedVersionlessIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedVersionlessIdValues(bundle);
			ourLog.debug("Observation: \n"
					+ myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		}

		return ids;
	}
}
