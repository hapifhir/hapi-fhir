package ca.uhn.hapi.fhir.cdshooks.svc;

/*
 * LFJT3 — "Looking Forward to Jackson Tools 3"
 * =============================================
 * This test file has been migrated to Jackson 3 (tools.jackson).
 * Contains Jackson 3 tests for:
 *   - BaseCdsMethod (Jackson: encodeRequest → writeValueAsString)
 *   - BaseDynamicCdsServiceMethod (no Jackson in the class itself — documented)
 *   - CdsConfigServiceImpl (no Jackson behavior — documented)
 *   - CdsServiceRegistryImpl (Jackson: buildResponseFromString, buildFeedbackFromString)
 */

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeedbackJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CdsSvcJacksonTest {

	// ── MAPPER FACTORY ──────────────────────────────────────────────────
	// Jackson 3: JsonMapper.builder().build()
	private JsonMapper createMapper() {
		return JsonMapper.builder().build();
	}
	// ── END MAPPER FACTORY ──────────────────────────────────────────────

	// ═════════════════════════════════════════════════════════════════════════
	// 1. BaseCdsMethod — Jackson: encodeRequest → writeValueAsString
	//    The Jackson-sensitive path: when the CDS service method parameter type
	//    is String, BaseCdsMethod.encodeRequest() serializes the IModelJson to
	//    a JSON string via ObjectMapper.writeValueAsString().
	// ═════════════════════════════════════════════════════════════════════════

	/** Minimal IModelJson for testing. */
	static class TestRequestJson implements IModelJson {
		@JsonProperty("hookInstance")
		// @JsonProperty stays on com.fasterxml.jackson.annotation in Jackson 3
		private String hookInstance;

		TestRequestJson(String hookInstance) { this.hookInstance = hookInstance; }
		public String getHookInstance() { return hookInstance; }
	}

	/** Concrete service bean that accepts a String parameter. */
	static class StringParamService {
		public String handle(String theRequest) { return theRequest; }
	}

	/** Concrete service bean that accepts a typed parameter. */
	static class TypedParamService {
		public Object handle(TestRequestJson theRequest) { return theRequest; }
	}

	@Nested
	@DisplayName("BaseCdsMethod — encodeRequest (String parameter path)")
	class BaseCdsMethodTests {

		@Test
		@DisplayName("When method param is String, IModelJson is serialized to JSON string")
		void invoke_stringParam_serializesRequestToJson() throws Exception {
			JsonMapper mapper = createMapper();
			StringParamService bean = new StringParamService();
			Method method = StringParamService.class.getMethod("handle", String.class);

			// Use a concrete anonymous subclass since BaseCdsMethod is abstract
			BaseCdsMethod svc = new BaseCdsMethod(bean, method) {};

			TestRequestJson request = new TestRequestJson("uuid-001");
			Object result = svc.invoke(mapper, request, "test-service");

			// The result should be a JSON string containing the hookInstance
			assertThat(result).isInstanceOf(String.class);
			assertThat((String) result).contains("uuid-001");
		}

		@Test
		@DisplayName("Serialized string is valid JSON")
		void invoke_stringParam_resultIsValidJson() throws Exception {
			JsonMapper mapper = createMapper();
			StringParamService bean = new StringParamService();
			Method method = StringParamService.class.getMethod("handle", String.class);
			BaseCdsMethod svc = new BaseCdsMethod(bean, method) {};

			TestRequestJson request = new TestRequestJson("uuid-002");
			String result = (String) svc.invoke(mapper, request, "test-service");

			// Verify the string is parseable as JSON
			assertDoesNotThrow(() -> mapper.readTree(result));
		}

		@Test
		@DisplayName("When method param is typed (not String), IModelJson is passed directly")
		void invoke_typedParam_passesRequestDirectly() throws Exception {
			JsonMapper mapper = createMapper();
			TypedParamService bean = new TypedParamService();
			Method method = TypedParamService.class.getMethod("handle", TestRequestJson.class);
			BaseCdsMethod svc = new BaseCdsMethod(bean, method) {};

			TestRequestJson request = new TestRequestJson("uuid-typed");
			Object result = svc.invoke(mapper, request, "test-service");

			// Passed directly — not serialized to String
			assertThat(result).isSameAs(request);
		}
	}

	// ═════════════════════════════════════════════════════════════════════════
	// 2. BaseDynamicCdsServiceMethod
	//    No Jackson imports in the class itself — the Function<> does the work.
	//    LFJT3 NOTE: No changes required in BaseDynamicCdsServiceMethod.java
	//    for the LFJT3 uplift. The ObjectMapper parameter is typed as
	//    com.fasterxml.jackson.databind.ObjectMapper → tools.jackson.databind.ObjectMapper
	//    but the class logic doesn't call any Jackson API directly.
	// ═════════════════════════════════════════════════════════════════════════

	@Nested
	@DisplayName("BaseDynamicCdsServiceMethod — function invocation")
	class BaseDynamicCdsServiceMethodTests {

		@Test
		@DisplayName("Registered function is invoked and returns response")
		void invoke_delegatesToFunction() throws Exception {
			JsonMapper mapper = createMapper();

			CdsServiceResponseJson expectedResponse = new CdsServiceResponseJson();
			CdsServiceResponseCardJson card = new CdsServiceResponseCardJson();
			card.setSummary("Dynamic Response");
			expectedResponse.addCard(card);

			BaseDynamicCdsServiceMethod svc =
				new BaseDynamicCdsServiceMethod(req -> expectedResponse) {};

			CdsServiceRequestJson request = new CdsServiceRequestJson();
			Object result = svc.invoke(mapper, request, "dynamic-service");

			assertThat(result).isSameAs(expectedResponse);
		}

		@Test
		@DisplayName("getFunction returns the registered function")
		void getFunction_returnsRegisteredFunction() {
			java.util.function.Function<CdsServiceRequestJson, CdsServiceResponseJson> fn =
				req -> new CdsServiceResponseJson();

			BaseDynamicCdsServiceMethod svc = new BaseDynamicCdsServiceMethod(fn) {};

			assertThat(svc.getFunction()).isSameAs(fn);
		}
	}

	// ═════════════════════════════════════════════════════════════════════════
	// 3. CdsConfigServiceImpl
	//    Pure bean — stores ObjectMapper reference, no Jackson API calls.
	//    LFJT3 NOTE: Only import changes (ObjectMapper package rename).
	//    No behavior changes needed.
	// ═════════════════════════════════════════════════════════════════════════

	@Nested
	@DisplayName("CdsConfigServiceImpl — ObjectMapper delegation")
	class CdsConfigServiceImplTests {

		@Test
		@DisplayName("getJsonMapper returns the mapper passed to the constructor")
		void getJsonMapper_returnsConstructorArg() {
			JsonMapper mapper = createMapper();
			CdsConfigServiceImpl svc = new CdsConfigServiceImpl(
				ca.uhn.fhir.context.FhirContext.forR4(), mapper, null, null);

			assertThat(svc.getJsonMapper()).isSameAs(mapper);
		}

		@Test
		@DisplayName("getFhirContext returns the context passed to the constructor")
		void getFhirContext_returnsConstructorArg() {
			ca.uhn.fhir.context.FhirContext ctx = ca.uhn.fhir.context.FhirContext.forR4();
			CdsConfigServiceImpl svc = new CdsConfigServiceImpl(ctx, createMapper(), null, null);

			assertThat(svc.getFhirContext()).isSameAs(ctx);
		}
	}

	// ═════════════════════════════════════════════════════════════════════════
	// 4. CdsServiceRegistryImpl — Jackson paths:
	//    encodeServiceResponse(String) → ObjectMapper.readValue → CdsServiceResponseJson
	//    encodeFeedbackResponse(String) → ObjectMapper.readValue → CdsServiceFeedbackJson
	// ═════════════════════════════════════════════════════════════════════════

	@Nested
	@DisplayName("CdsServiceRegistryImpl — Jackson encode paths")
	class CdsServiceRegistryImplJacksonTests {

		private CdsServiceRegistryImpl buildRegistryImpl() {
			JsonMapper mapper = createMapper();
			CdsHooksContextBooter booter = mock(CdsHooksContextBooter.class);
			when(booter.buildCdsServiceCache()).thenReturn(new CdsServiceCache());

			ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchSvc prefetchSvc =
				mock(ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchSvc.class);

			ca.uhn.hapi.fhir.cdshooks.serializer.CdsServiceRequestJsonDeserializer deserializer =
				mock(ca.uhn.hapi.fhir.cdshooks.serializer.CdsServiceRequestJsonDeserializer.class);

			CdsServiceRegistryImpl registry = new CdsServiceRegistryImpl(
				booter, prefetchSvc, mapper, deserializer);
			registry.init();
			return registry;
		}

		@Test
		@DisplayName("encodeServiceResponse: String result is deserialized to CdsServiceResponseJson")
		void encodeServiceResponse_stringResult_returnsDeserializedObject() throws Exception {
			CdsServiceRegistryImpl registry = buildRegistryImpl();
			JsonMapper mapper = createMapper();

			CdsServiceResponseJson expected = new CdsServiceResponseJson();
			CdsServiceResponseCardJson card = new CdsServiceResponseCardJson();
			card.setSummary("Test Card");
			expected.addCard(card);

			// Serialize the response as it would come back from a String-returning service method
			String jsonResponse = mapper.writeValueAsString(expected);

			CdsServiceResponseJson result = registry.encodeServiceResponse("test-svc", jsonResponse);

			assertNotNull(result);
			assertThat(result.getCards()).hasSize(1);
			assertThat(result.getCards().get(0).getSummary()).isEqualTo("Test Card");
		}

		@Test
		@DisplayName("encodeServiceResponse: CdsServiceResponseJson result is returned directly (no Jackson)")
		void encodeServiceResponse_typedResult_returnedDirectly() {
			CdsServiceRegistryImpl registry = buildRegistryImpl();

			CdsServiceResponseJson response = new CdsServiceResponseJson();
			CdsServiceResponseCardJson card = new CdsServiceResponseCardJson();
			card.setSummary("Direct");
			response.addCard(card);

			CdsServiceResponseJson result = registry.encodeServiceResponse("test-svc", response);

			assertThat(result).isSameAs(response);
		}

		@Test
		@DisplayName("encodeServiceResponse: invalid JSON string throws ConfigurationException")
		void encodeServiceResponse_invalidJson_throwsConfigurationException() {
			CdsServiceRegistryImpl registry = buildRegistryImpl();

			assertThrows(ConfigurationException.class,
				() -> registry.encodeServiceResponse("test-svc", "not valid json"));
		}

		@Test
		@DisplayName("encodeFeedbackResponse: String result is deserialized to CdsServiceFeedbackJson")
		void encodeFeedbackResponse_stringResult_returnsDeserializedObject() throws Exception {
			CdsServiceRegistryImpl registry = buildRegistryImpl();
			JsonMapper mapper = createMapper();

			CdsServiceFeedbackJson expected = new CdsServiceFeedbackJson();
			expected.setCard("card-uuid-123");

			String jsonResponse = mapper.writeValueAsString(expected);

			CdsServiceFeedbackJson result = registry.encodeFeedbackResponse("test-svc", jsonResponse);

			assertNotNull(result);
			assertThat(result.getCard()).isEqualTo("card-uuid-123");
		}

		@Test
		@DisplayName("encodeFeedbackResponse: invalid JSON string throws RuntimeException")
		void encodeFeedbackResponse_invalidJson_throwsRuntimeException() {
			CdsServiceRegistryImpl registry = buildRegistryImpl();

			assertThrows(RuntimeException.class,
				() -> registry.encodeFeedbackResponse("test-svc", "{bad json}"));
		}
	}
}
