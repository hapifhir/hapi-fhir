package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.hapi.fhir.cdshooks.api.ICdsMethod;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CdsServiceCacheTest {
	private static final String SERVICE_ID1 = "service id 1";
	private static final String SERVICE_ID2 = "service id 2";
	private static final String SERVICE_ID3 = "service id 3";
	private static final String HOOK_SERVICE_ID1 = "hook service id 1";
	private static final String HOOK_SERVICE_ID2 = "hook service id 2";
	private static final String HOOK_SERVICE_ID3 = "hook service id 3";
	private static final String SERVICE_GROUP_ID = "service group id";
	@RegisterExtension
	final LogbackTestExtension myLogCapture = new LogbackTestExtension((Logger) CdsServiceCache.ourLog, Level.ERROR);
	@InjectMocks
	private CdsServiceCache myFixture;

	@Test
	void registerDynamicServiceShouldRegisterServiceWhenServiceNotRegistered() {
		// setup
		final Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction = withFunction();
		final CdsServiceJson cdsServiceJson = withCdsServiceJson(HOOK_SERVICE_ID1);
		// execute
		myFixture.registerDynamicService(SERVICE_ID1, serviceFunction, cdsServiceJson, true, SERVICE_GROUP_ID);
		// validate
		assertThat(myFixture.myServiceMap).hasSize(1).containsKey(SERVICE_ID1);
		final CdsDynamicPrefetchableServiceMethod cdsMethod = (CdsDynamicPrefetchableServiceMethod) myFixture.myServiceMap.get(SERVICE_ID1);
		assertThat(cdsMethod.getFunction()).isEqualTo(serviceFunction);
		assertThat(cdsMethod.getCdsServiceJson()).isEqualTo(cdsServiceJson);
		assertThat(cdsMethod.isAllowAutoFhirClientPrefetch()).isTrue();
		assertThat(myFixture.myCdsServiceJson.getServices()).hasSize(1).contains(cdsServiceJson);
		assertThat(myFixture.myGroups).hasSize(1).containsKey(SERVICE_GROUP_ID);
		assertThat(myFixture.myGroups.get(SERVICE_GROUP_ID)).hasSize(1).contains(SERVICE_ID1);
	}

	@Test
	void registerDynamicServiceShouldNotRegisterServiceWhenServiceAlreadyRegistered() {
		// setup
		final Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction = withFunction();
		final CdsServiceJson cdsServiceJson = withCdsServiceJson(HOOK_SERVICE_ID1);
		final Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction2 = withFunction();
		final CdsServiceJson cdsServiceJson2 = withCdsServiceJson(HOOK_SERVICE_ID2);
		final String expectedLogMessage = "CDS service with serviceId: " + SERVICE_ID1 +" for serviceGroupId: " + SERVICE_GROUP_ID + ", already exists. It will not be overwritten!";
		// execute
		myFixture.registerDynamicService(SERVICE_ID1, serviceFunction, cdsServiceJson, true, SERVICE_GROUP_ID);
		myFixture.registerDynamicService(SERVICE_ID1, serviceFunction2, cdsServiceJson2, false, SERVICE_GROUP_ID);
		// validate
		assertThat(myFixture.myServiceMap).hasSize(1).containsKey(SERVICE_ID1);
		final CdsDynamicPrefetchableServiceMethod cdsMethod = (CdsDynamicPrefetchableServiceMethod) myFixture.myServiceMap.get(SERVICE_ID1);
		assertThat(cdsMethod.getFunction()).isEqualTo(serviceFunction);
		assertThat(cdsMethod.getCdsServiceJson()).isEqualTo(cdsServiceJson);
		assertThat(cdsMethod.isAllowAutoFhirClientPrefetch()).isTrue();
		assertThat(myFixture.myCdsServiceJson.getServices()).hasSize(1).contains(cdsServiceJson);
		assertThat(myFixture.myGroups).hasSize(1).containsKey(SERVICE_GROUP_ID);
		assertThat(myFixture.myGroups.get(SERVICE_GROUP_ID)).hasSize(1).contains(SERVICE_ID1);
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasErrorMessage(expectedLogMessage);
	}

	@Test
	void unregisterServiceMethodShouldReturnsServiceWhenServiceRegistered() {
		// setup
		final Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction = withFunction();
		final CdsServiceJson cdsServiceJson = withCdsServiceJson(HOOK_SERVICE_ID1);
		myFixture.registerDynamicService(SERVICE_ID1, serviceFunction, cdsServiceJson, true, SERVICE_GROUP_ID);
		// execute
		final CdsDynamicPrefetchableServiceMethod cdsMethod = (CdsDynamicPrefetchableServiceMethod) myFixture.unregisterServiceMethod(SERVICE_ID1, SERVICE_GROUP_ID);
		// validate
		assertThat(myFixture.myServiceMap).isEmpty();
		assertThat(cdsMethod.getFunction()).isEqualTo(serviceFunction);
		assertThat(cdsMethod.getCdsServiceJson()).isEqualTo(cdsServiceJson);
		assertThat(cdsMethod.isAllowAutoFhirClientPrefetch()).isTrue();
		assertThat(myFixture.myCdsServiceJson.getServices()).isEmpty();
	}

	@Test
	void unregisterServiceMethodShouldReturnNullWhenServiceNotRegistered() {
		// setup
		final String expectedLogMessage = "CDS service with serviceId: " + SERVICE_ID1 + " for serviceGroupId: " + SERVICE_GROUP_ID + ", is not registered. Nothing to remove!";
		// execute
		final ICdsMethod actual = myFixture.unregisterServiceMethod(SERVICE_ID1, SERVICE_GROUP_ID);
		// validate
		assertThat(actual).isNull();
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasErrorMessage(expectedLogMessage);
	}

	@Test
	void unregisterServices() {
		// setup
		final List<Function<CdsServiceRequestJson, CdsServiceResponseJson>> serviceFunctions = withFunctions();
		final List<CdsServiceJson> cdsServiceJsons = withCdsServiceJsons();
		final List<String> keys = List.of(SERVICE_ID1, SERVICE_ID2, SERVICE_ID3);
		for (int i = 0; i < keys.size() ; i++) {
			myFixture.registerDynamicService(keys.get(i), serviceFunctions.get(i), cdsServiceJsons.get(i), true, SERVICE_GROUP_ID);
		}
		assertThat(myFixture.myGroups.keySet()).hasSize(1).containsExactly(SERVICE_GROUP_ID);
		assertThat(myFixture.myGroups.get(SERVICE_GROUP_ID)).containsExactlyInAnyOrderElementsOf(keys);
		// execute
		myFixture.unregisterServices(SERVICE_GROUP_ID);
		// validate
		assertThat(myFixture.myServiceMap).isEmpty();
		assertThat(myFixture.myGroups).isEmpty();
	}

	@Test
	void unregisterServicesWithInvalidServiceGroupID() {
		// execute
		myFixture.unregisterServices(SERVICE_GROUP_ID);
		// validate
		LogbackTestExtensionAssert.assertThat(myLogCapture)
			.hasErrorMessage("CDS services for serviceGroupId: " + SERVICE_GROUP_ID + ", are not registered. Nothing to remove!");
	}

	@Nonnull
	private List<Function<CdsServiceRequestJson, CdsServiceResponseJson>> withFunctions() {
		final Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction1 = withFunction();
		final Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction2 = withFunction();
		final Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction3 = withFunction();
		return List.of(serviceFunction1, serviceFunction2, serviceFunction3);
	}

	@Nonnull
	private List<CdsServiceJson> withCdsServiceJsons() {
		final CdsServiceJson cdsServiceJson1 = withCdsServiceJson(HOOK_SERVICE_ID1);
		final CdsServiceJson cdsServiceJson2 = withCdsServiceJson(HOOK_SERVICE_ID2);
		final CdsServiceJson cdsServiceJson3 = withCdsServiceJson(HOOK_SERVICE_ID3);
		return List.of(cdsServiceJson1, cdsServiceJson2, cdsServiceJson3);
	}

	@Nonnull
	private CdsServiceJson withCdsServiceJson(@Nonnull String theHookServiceId) {
		final CdsServiceJson cdsServiceJson = new CdsServiceJson();
		cdsServiceJson.setId(theHookServiceId);
		return cdsServiceJson;
	}

	@Nonnull
	private Function<CdsServiceRequestJson, CdsServiceResponseJson> withFunction() {
		return (CdsServiceRequestJson theCdsServiceRequestJson) -> {
			final CdsServiceResponseJson cdsServiceResponseJson = new CdsServiceResponseJson();
			final CdsServiceResponseCardJson card = new CdsServiceResponseCardJson();
			cdsServiceResponseJson.addCard(card);
			return cdsServiceResponseJson;
		};
	}
}
