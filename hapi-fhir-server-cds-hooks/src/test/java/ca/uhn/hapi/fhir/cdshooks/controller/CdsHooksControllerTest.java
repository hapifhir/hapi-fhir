package ca.uhn.hapi.fhir.cdshooks.controller;

import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeebackOutcomeEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeedbackJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.config.CdsHooksConfig;
import ca.uhn.hapi.fhir.cdshooks.config.TestCdsHooksConfig;
import ca.uhn.hapi.fhir.cdshooks.custom.extensions.model.RequestExtension;
import ca.uhn.hapi.fhir.cdshooks.svc.prefetch.CdsPrefetchFhirClientSvc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CdsHooksConfig.class, TestCdsHooksConfig.class})
public class CdsHooksControllerTest {
	public static final String TEST_FHIR_SERVER = "http://localhost:9999/";
	public static final String TEST_PATIENT_ID = "P2401";
	public static final String TEST_USER_ID = "Practitioner/FREDDY";

	public static final String TEST_HOOK_INSTANCE = UUID.randomUUID().toString();
	public static final String OUTCOME_TIMESTAMP = "2020-12-16";
	private static final String TEST_KEY = "CdsServiceRegistryImplTest.testKey";
	private static final String TEST_SERVICE_ID = "CdsServiceRegistryImplTest.testServiceId";
	private final String MODULE_ID = "moduleId";

	@Autowired
	ICdsServiceRegistry myCdsHooksRegistry;
	@Autowired
	ICdsServiceRegistry myCdsServiceRegistry;
	@Autowired
	@Qualifier(CdsHooksConfig.CDS_HOOKS_OBJECT_MAPPER_FACTORY)
	protected ObjectMapper myObjectMapper;

	@MockBean
	CdsPrefetchFhirClientSvc myCdsPrefetchFhirClientSvc;


	MockMvc myMockMvc;

	@BeforeEach
	public void before() {
		myMockMvc = MockMvcBuilders.standaloneSetup(new CdsHooksController(myCdsHooksRegistry))
			.setMessageConverters(new MappingJackson2HttpMessageConverter(myObjectMapper))
			.build();
	}

	@Test
	void testCdsServices() throws Exception {
		myMockMvc
			.perform(get(CdsHooksController.BASE))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("services[2].hook").value(GreeterCdsService.TEST_HOOK))
			.andExpect(jsonPath("services[2].description").value(GreeterCdsService.TEST_HOOK_DESCRIPTION))
			.andExpect(jsonPath("services[2].title").value(GreeterCdsService.TEST_HOOK_TITLE))
			.andExpect(jsonPath("services[2].id").value(GreeterCdsService.TEST_HOOK_STRING_ID))
			.andExpect(jsonPath("services[2].prefetch." + GreeterCdsService.TEST_HOOK_PREFETCH_USER_KEY).value(GreeterCdsService.TEST_HOOK_PREFETCH_USER_QUERY))
			.andExpect(jsonPath("services[2].prefetch." + GreeterCdsService.TEST_HOOK_PREFETCH_PATIENT_KEY).value(GreeterCdsService.TEST_HOOK_PREFETCH_PATIENT_QUERY))
			.andExpect(jsonPath("services[5].extension.example-client-conformance").value("http://hooks.example.org/fhir/102/Conformance/patientview"));
	}

	@Test
	void testExampleFeedback() throws Exception {
		// setup
		final CdsServiceFeedbackJson request = new CdsServiceFeedbackJson();
		request.setCard(TEST_HOOK_INSTANCE);
		request.setOutcome(CdsServiceFeebackOutcomeEnum.accepted);
		request.setOutcomeTimestamp(OUTCOME_TIMESTAMP);
		final String requestBody = myObjectMapper.writeValueAsString(request);
		// execute
		final MvcResult actual = myMockMvc
			.perform(post(CdsHooksController.BASE + "/example-service/feedback").contentType(MediaType.APPLICATION_JSON).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		// validate
		final CdsServiceFeedbackJson cdsServiceFeedbackJson = myObjectMapper.readValue(actual.getResponse().getContentAsString(), CdsServiceFeedbackJson.class);
		assertThat(cdsServiceFeedbackJson.getAcceptedSuggestions()).hasSize(1);
		assertThat(cdsServiceFeedbackJson).usingRecursiveComparison().ignoringFields("myAcceptedSuggestions").isEqualTo(request);
	}

	@Test
	void testCallHelloWorld() throws Exception {
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.setHookInstance(TEST_HOOK_INSTANCE);
		request.setHook(HelloWorldService.TEST_HOOK);
		request.setFhirServer(TEST_FHIR_SERVER);

		String requestBody = myObjectMapper.writeValueAsString(request);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + HelloWorldService.TEST_HOOK_WORLD_ID).contentType(MediaType.APPLICATION_JSON).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value("Hello World!"))
			.andExpect(jsonPath("cards[0].indicator").value("warning"))
			.andExpect(jsonPath("cards[0].source.label").value("World Greeter"))
			.andExpect(jsonPath("cards[0].detail").value("This is a test.  Do not be alarmed."))
		;
	}


	@Test
	void testCallHelloUniverse() throws Exception {
		RequestExtension requestExtension = new RequestExtension();
		requestExtension.setConfigItem("request-config-item");

		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.setExtension(requestExtension);
		request.setFhirServer(TEST_FHIR_SERVER);
		request.setHook(HelloWorldService.TEST_HOOK_UNIVERSE_ID);


		String requestBody = myObjectMapper.writeValueAsString(request);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + HelloWorldService.TEST_HOOK_UNIVERSE_ID).contentType(MediaType.APPLICATION_JSON).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value("Hello Universe!"))
			.andExpect(jsonPath("cards[0].indicator").value("critical"))
			.andExpect(jsonPath("cards[0].extension.example-config-item").value("request-config-item"));
	}

	@Test
	void testCallPlayback() throws Exception {
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.setHookInstance(TEST_HOOK_INSTANCE);
		request.setHook(HelloWorldService.TEST_HOOK);
		request.setFhirServer(TEST_FHIR_SERVER);

		String requestBody = myObjectMapper.writeValueAsString(request);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + HelloWorldService.TEST_HOOK_PLAYBACK_ID).contentType(MediaType.APPLICATION_JSON).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value("FhirServer: " + TEST_FHIR_SERVER + " Hook: " + HelloWorldService.TEST_HOOK + " Hook Instance: " + TEST_HOOK_INSTANCE))
			.andExpect(jsonPath("cards[0].indicator").value("critical"))
		;
	}

	@Test
	void testHelloWorldFeedback() throws Exception {
		// setup
		final CdsServiceFeedbackJson request = new CdsServiceFeedbackJson();
		request.setCard(TEST_HOOK_INSTANCE);
		request.setOutcome(CdsServiceFeebackOutcomeEnum.accepted);
		request.setOutcomeTimestamp(OUTCOME_TIMESTAMP);
		String requestBody = myObjectMapper.writeValueAsString(request);
		TestServerAppCtx.ourHelloWorldService.setExpectedCount(1);
		// execute
		MvcResult actual = myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + HelloWorldService.TEST_HOOK_WORLD_ID + "/feedback").contentType(MediaType.APPLICATION_JSON).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		// validate
		final CdsServiceFeedbackJson cdsServiceFeedbackJson = myObjectMapper.readValue(actual.getResponse().getContentAsString(), CdsServiceFeedbackJson.class);
		assertThat(cdsServiceFeedbackJson.getAcceptedSuggestions()).hasSize(1);
		assertThat(cdsServiceFeedbackJson).usingRecursiveComparison().ignoringFields("myAcceptedSuggestions").isEqualTo(request);
		TestServerAppCtx.ourHelloWorldService.awaitExpected();
	}

	@Test
	void testCallDynamicallyRegisteredService() throws Exception {
		// Register cds hook
		Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction = (CdsServiceRequestJson theCdsServiceRequestJson) -> {
			CdsServiceResponseJson retval = new CdsServiceResponseJson();
			CdsServiceResponseCardJson card = new CdsServiceResponseCardJson();
			card.setSummary(TEST_KEY);
			retval.addCard(card);
			return retval;
		};
		CdsServiceJson cdsServiceJson = new CdsServiceJson();
		cdsServiceJson.setId(TEST_SERVICE_ID);
		boolean allowAutoFhirClientPrefetch = false;

		myCdsServiceRegistry.registerService(TEST_SERVICE_ID, serviceFunction, cdsServiceJson, allowAutoFhirClientPrefetch, MODULE_ID);

		// Call hook
		String hookInstance = UUID.randomUUID().toString();
		CdsServiceRequestJson request = buildRequest(hookInstance);
		String requestBody = myObjectMapper.writeValueAsString(request);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + TEST_SERVICE_ID).contentType(MediaType.APPLICATION_JSON).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value(TEST_KEY))
		;
	}


	@Test
	void testEmptyCardsResponse() throws Exception {
		//setup
		final String expected = "{ \"cards\" : [ ]}";
		final Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction = (CdsServiceRequestJson theCdsServiceRequestJson) -> new CdsServiceResponseJson();
		myCdsServiceRegistry.unregisterService(TEST_SERVICE_ID, MODULE_ID);
		myCdsServiceRegistry.registerService(TEST_SERVICE_ID, serviceFunction, new CdsServiceJson().setId(TEST_SERVICE_ID), false, MODULE_ID);
		final CdsServiceRequestJson request = buildRequest(UUID.randomUUID().toString());
		final String requestBody = myObjectMapper.writeValueAsString(request);
		//execute
		final MvcResult result = myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + TEST_SERVICE_ID).contentType(MediaType.APPLICATION_JSON).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		//validate
		final String actual = result.getResponse().getContentAsString();
		assertEquals(prettyJson(expected), prettyJson(actual));
	}


	@Nonnull
	protected CdsServiceRequestJson buildRequest(String hookInstance) {
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.setHookInstance(hookInstance);
		request.setHook(GreeterCdsService.TEST_HOOK);
		request.addContext(GreeterCdsService.TEST_HOOK_CONTEXT_PATIENTID_KEY, TEST_PATIENT_ID);
		request.addContext(GreeterCdsService.TEST_HOOK_CONTEXT_USERID_KEY, TEST_USER_ID);
		request.setFhirServer(TEST_FHIR_SERVER);
		return request;
	}

	public static String prettyJson(String theInput) {
		JsonNode input = JsonUtil.deserialize(theInput, JsonNode.class);
		return JsonUtil.serialize(input, true);
	}

}
