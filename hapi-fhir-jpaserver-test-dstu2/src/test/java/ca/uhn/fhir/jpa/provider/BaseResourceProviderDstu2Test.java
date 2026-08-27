package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.dao.dstu2.BaseJpaDstu2Test;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
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
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@ContextConfiguration(classes = ServerConfiguration.class)
public abstract class BaseResourceProviderDstu2Test extends BaseJpaDstu2Test {
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
			s.registerProvider(mySystemProvider);
			s.setDefaultResponseEncoding(EncodingEnum.XML);
			s.setDefaultPrettyPrint(false);

			myFhirContext.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
			s.registerProvider(myAppCtx.getBean(ProcessMessageProvider.class));
			s.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class));

			JpaConformanceProviderDstu2 confProvider = new JpaConformanceProviderDstu2(s, mySystemDao, myStorageSettings);
			confProvider.setImplementationDescription("THIS IS THE DESC");
			s.setServerConformanceProvider(confProvider);

			DatabaseBackedPagingProvider pagingProvider = myAppCtx.getBean(DatabaseBackedPagingProvider.class);
			s.setPagingProvider(pagingProvider);

		}).withServerBeforeEach(s -> {
			myPort = myServer.getPort();
			myServerBase = myServer.getBaseUrl();
			myClient = myServer.getFhirClient();
		});

	public BaseResourceProviderDstu2Test() {
		super();
	}


	@AfterEach
	public void after() throws Exception {
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
	}

	protected List<IIdType> toIdListUnqualifiedVersionless(Bundle found) {
		List<IIdType> list = new ArrayList<>();
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
