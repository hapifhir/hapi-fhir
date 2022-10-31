package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.dstu2.BaseJpaDstu2Test;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.subscription.match.config.WebsocketDispatcherConfig;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerConfigurerExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.server.SpringContextGrabbingTestExecutionListener;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseResourceProviderDstu2Test extends BaseJpaDstu2Test {

	@RegisterExtension
	protected static HttpClientExtension ourHttpClient = new HttpClientExtension();
	protected static int ourPort;
	protected static String ourServerBase;
	@Autowired
	protected static PlatformTransactionManager ourTxManager;
	protected static IGenericClient ourClient;
	@RegisterExtension
	protected static RestfulServerExtension ourServer = new RestfulServerExtension(FhirContext.forDstu2Cached())
		.keepAliveBetweenTests()
		.withValidationMode(ServerValidationModeEnum.NEVER)
		.withContextPath("/fhir")
		.withServletPath("/context/*")
		.withSpringWebsocketSupport(WEBSOCKET_CONTEXT, WebsocketDispatcherConfig.class);

	@RegisterExtension
	protected RestfulServerConfigurerExtension myServerConfigurer = new RestfulServerConfigurerExtension(ourServer)
		.withServer(s -> {
			s.registerProviders(myResourceProviders.createProviders());
			s.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
			s.registerProvider(mySystemProvider);
			s.setDefaultResponseEncoding(EncodingEnum.XML);
			s.setDefaultPrettyPrint(false);

			s.registerProvider(myAppCtx.getBean(ProcessMessageProvider.class));

			JpaConformanceProviderDstu2 confProvider = new JpaConformanceProviderDstu2(s, mySystemDao, myDaoConfig);
			confProvider.setImplementationDescription("THIS IS THE DESC");
			s.setServerConformanceProvider(confProvider);

			DatabaseBackedPagingProvider pagingProvider = myAppCtx.getBean(DatabaseBackedPagingProvider.class);
			s.setPagingProvider(pagingProvider);

			// TODO: JA-2 These don't need to be static variables, should just inline all of the uses of these
			ourPort = ourServer.getPort();
			ourServerBase = ourServer.getBaseUrl();
			ourClient = ourServer.getFhirClient();

		});

	public BaseResourceProviderDstu2Test() {
		super();
	}


	@AfterEach
	public void after() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
	}

	protected List<IdDt> toIdListUnqualifiedVersionless(Bundle found) {
		List<IdDt> list = new ArrayList<>();
		for (Entry next : found.getEntry()) {
			list.add(next.getResource().getId().toUnqualifiedVersionless());
		}
		return list;
	}

	protected List<String> toNameList(Bundle resp) {
		List<String> names = new ArrayList<>();
		for (Entry next : resp.getEntry()) {
			Patient nextPt = (Patient) next.getResource();
			String nextStr = nextPt.getNameFirstRep().getGivenAsSingleString() + " " + nextPt.getNameFirstRep().getFamilyAsSingleString();
			if (isNotBlank(nextStr)) {
				names.add(nextStr);
			}
		}
		return names;
	}

}
