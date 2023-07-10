package ca.uhn.hapi.fhir.cdshooks.controller;/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2017 Simpatico Intelligent Systems Inc
 * %%
 * All rights reserved.
 * #L%
 */

import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.config.CdsHooksConfig;
import ca.uhn.hapi.fhir.cdshooks.config.TestCdsHooksConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CdsHooksConfig.class, TestCdsHooksConfig.class})
public class CdsHooksControllerTest {
	public static final String TEST_HOOK_INSTANCE = UUID.randomUUID().toString();
	public static final String OUTCOME_TIMESTAMP = "2020-12-16";
	private static final Logger ourLog = LoggerFactory.getLogger(CdsHooksControllerTest.class);
	private static final String TEST_KEY = "CdsServiceRegistryImplTest.testKey";
	private static final String TEST_SERVICE_ID = "CdsServiceRegistryImplTest.testServiceId";
	private final String MODULE_ID = "moduleId";

	@Autowired
	ICdsServiceRegistry myCdsHooksRegistry;

	MockMvc myMockMvc;

	@BeforeEach
	public void before() {
		myMockMvc = MockMvcBuilders.standaloneSetup(new CdsHooksController(myCdsHooksRegistry)).build();
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
			.andExpect(jsonPath("services[2].prefetch." + GreeterCdsService.TEST_HOOK_PREFETCH_PATIENT_KEY).value(GreeterCdsService.TEST_HOOK_PREFETCH_PATIENT_QUERY));
	}

	/**
	@Test
	@WithUserDetails(HAPPY_USER)
	void testCallWithString() throws Exception {

		String hookInstance = UUID.randomUUID().toString();

		String requestBody = getRequestBodyWithPrefetch(hookInstance);

		TestServerAppCtx.ourGreeterCdsService.setExpectedCount(1);
		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + GreeterCdsService.TEST_HOOK_STRING_ID).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value("Hello Billy Idol and Freddy Mercury"))
			.andExpect(jsonPath("cards[0].indicator").value(GreeterCdsService.TEST_HOOK_RESPONSE_INDICATOR.toString().toLowerCase()))
			.andExpect(jsonPath("cards[0].source.label").value(GreeterCdsService.TEST_HOOK_RESPONSE_SOURCE_LABEL))
			.andExpect(jsonPath("cards[0].detail").value(GreeterCdsService.TEST_HOOK_RESPONSE_DETAIL))
		;
		Object calledWithObject = PointcutLatch.getLatchInvocationParameter(TestServerAppCtx.ourGreeterCdsService.awaitExpected());
		assertThat(calledWithObject, instanceOf(String.class));
		String json = (String) calledWithObject;

		ourLog.debug(json);
		CdsServiceRequestJson calledWith = myObjectMapper.readValue(json, CdsServiceRequestJson.class);
		assertEquals(hookInstance, calledWith.getHookInstance());
		assertEquals(GreeterCdsService.TEST_HOOK, calledWith.getHook());
		assertEquals(TEST_FHIR_SERVER, calledWith.getFhirServer());
		assertEquals(TEST_PATIENT_ID, calledWith.getContext().getString(GreeterCdsService.TEST_HOOK_CONTEXT_PATIENTID_KEY));
		assertEquals(TEST_USER_ID, calledWith.getContext().getString(GreeterCdsService.TEST_HOOK_CONTEXT_USERID_KEY));
	}


	@Test
	@WithUserDetails(HAPPY_USER)
	void testCallWithJson() throws Exception {
		String hookInstance = UUID.randomUUID().toString();
		String requestBody = getRequestBodyWithPrefetch(hookInstance);

		TestServerAppCtx.ourGreeterCdsService.setExpectedCount(1);
		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + GreeterCdsService.TEST_HOOK_OBJECT_ID).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value("Hello Billy Idol and Freddy Mercury"))
			.andExpect(jsonPath("cards[0].indicator").value(GreeterCdsService.TEST_HOOK_RESPONSE_INDICATOR.toString().toLowerCase()))
			.andExpect(jsonPath("cards[0].source.label").value(GreeterCdsService.TEST_HOOK_RESPONSE_SOURCE_LABEL))
		;
		Object calledWithObject = PointcutLatch.getLatchInvocationParameter(TestServerAppCtx.ourGreeterCdsService.awaitExpected());
		assertThat(calledWithObject, instanceOf(CdsServiceRequestJson.class));
		CdsServiceRequestJson calledWith = (CdsServiceRequestJson) calledWithObject;
		assertEquals(hookInstance, calledWith.getHookInstance());
		assertEquals(GreeterCdsService.TEST_HOOK, calledWith.getHook());
		assertEquals(TEST_FHIR_SERVER, calledWith.getFhirServer());
		assertEquals(TEST_PATIENT_ID, calledWith.getContext().getString(GreeterCdsService.TEST_HOOK_CONTEXT_PATIENTID_KEY));
		assertEquals(TEST_USER_ID, calledWith.getContext().getString(GreeterCdsService.TEST_HOOK_CONTEXT_USERID_KEY));
	}

	@Test
	@WithUserDetails(HAPPY_USER)
	void testExampleService() throws Exception {
		String hookInstance = UUID.randomUUID().toString();
		String requestBody = getRequestBodyWithPrefetch(hookInstance);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/example-service").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value("Hello Billy Idol"))
			.andExpect(jsonPath("cards[0].indicator").value(CdsServiceIndicatorEnum.INFO.toString().toLowerCase()))
			.andExpect(jsonPath("cards[0].source.label").value("Smile CDR"))
		;
	}

	@Test
	@WithUserDetails(HAPPY_USER)
	void testExampleFeedback() throws Exception {
		CdsServiceFeedbackJson request = new CdsServiceFeedbackJson();
		request.setCard(TEST_HOOK_INSTANCE);
		request.setOutcome(CdsServiceFeebackOutcomeEnum.accepted);
		request.setOutcomeTimestamp(OUTCOME_TIMESTAMP);

		String requestBody = myObjectMapper.writeValueAsString(request);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/example-service/feedback").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("message").value("Thank you for your feedback dated " + OUTCOME_TIMESTAMP + "!"))
		;
	}

	@Test
	@WithUserDetails(HAPPY_USER)
	void testCallHelloWorld() throws Exception {
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.setHookInstance(TEST_HOOK_INSTANCE);
		request.setHook(HelloWorldService.TEST_HOOK);
		request.setFhirServer(TEST_FHIR_SERVER);

		String requestBody = myObjectMapper.writeValueAsString(request);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + HelloWorldService.TEST_HOOK_WORLD_ID).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
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
	@WithUserDetails(HAPPY_USER)
	void testCallHelloUniverse() throws Exception {
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.setFhirServer(TEST_FHIR_SERVER);

		String requestBody = myObjectMapper.writeValueAsString(request);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + HelloWorldService.TEST_HOOK_UNIVERSE_ID).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value("Hello Universe!"))
			.andExpect(jsonPath("cards[0].indicator").value("critical"))
		;
	}

	@Test
	@WithUserDetails(HAPPY_USER)
	void testCallPlayback() throws Exception {
		CdsServiceRequestJson request = new CdsServiceRequestJson();
		request.setHookInstance(TEST_HOOK_INSTANCE);
		request.setHook(HelloWorldService.TEST_HOOK);
		request.setFhirServer(TEST_FHIR_SERVER);

		String requestBody = myObjectMapper.writeValueAsString(request);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + HelloWorldService.TEST_HOOK_PLAYBACK_ID).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value("FhirServer: " + TEST_FHIR_SERVER + " Hook: " + HelloWorldService.TEST_HOOK + " Hook Instance: " + TEST_HOOK_INSTANCE))
			.andExpect(jsonPath("cards[0].indicator").value("critical"))
		;
	}

	@Test
	@WithUserDetails(HAPPY_USER)
	void testHelloWorldFeedback() throws Exception {
		CdsServiceFeedbackJson request = new CdsServiceFeedbackJson();
		request.setCard(TEST_HOOK_INSTANCE);
		request.setOutcome(CdsServiceFeebackOutcomeEnum.accepted);
		request.setOutcomeTimestamp(OUTCOME_TIMESTAMP);

		String requestBody = myObjectMapper.writeValueAsString(request);

		TestServerAppCtx.ourHelloWorldService.setExpectedCount(1);
		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + HelloWorldService.TEST_HOOK_WORLD_ID + "/feedback").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("message").value("Thank you for your feedback dated " + OUTCOME_TIMESTAMP + "!"))
		;
		TestServerAppCtx.ourHelloWorldService.awaitExpected();
	}

	@Test
	@WithUserDetails(HAPPY_USER)
	void testGreeterServiceWithFhirClientPrefetch() throws Exception {
		String hookInstance = UUID.randomUUID().toString();
		CdsServiceRequestJson request = buildRequest(hookInstance, true, false);
		String requestBody = myObjectMapper.writeValueAsString(request);

		when(myCdsPrefetchFhirClientSvc.resourceFromUrl(any(), eq("Patient/" + TEST_PATIENT_ID))).thenReturn(buildTestPatient());
		when(myCdsPrefetchFhirClientSvc.resourceFromUrl(any(), eq(TEST_USER_ID))).thenReturn(buildTestPractitioner());

		TestServerAppCtx.ourGreeterCdsService.setExpectedCount(1);
		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + GreeterCdsService.TEST_HOOK_OBJECT_ID).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value("Hello Billy Idol and Freddy Mercury"))
			.andExpect(jsonPath("cards[0].indicator").value(CdsServiceIndicatorEnum.INFO.toString().toLowerCase()))
			.andExpect(jsonPath("cards[0].source.label").value(GreeterCdsService.TEST_HOOK_RESPONSE_SOURCE_LABEL))
		;
		TestServerAppCtx.ourGreeterCdsService.awaitExpected();
	}

	@Test
	@WithUserDetails(HAPPY_USER)
	void testParsingRequestBody_withMalformedCdsServiceRequestJson_returns400() throws Exception {
		// given
		String hookInstance = UUID.randomUUID().toString();
		CdsServiceRequestJson request = buildRequest(hookInstance, false, true);
		String badParamRequestBody = badRequestObjectMapper().writeValueAsString(request);

		// when
		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + GreeterCdsService.TEST_HOOK_OBJECT_ID).contentType(MediaType.APPLICATION_JSON_UTF8).content(badParamRequestBody))
			.andDo(print())
			// then
			.andExpect(status().is4xxClientError());

	}

	@Test
	@WithUserDetails(HAPPY_USER)
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
		CdsServiceRequestJson request = buildRequest(hookInstance, true, false);
		String requestBody = myObjectMapper.writeValueAsString(request);

		myMockMvc
			.perform(post(CdsHooksController.BASE + "/" + TEST_SERVICE_ID).contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("cards[0].summary").value(TEST_KEY))
		;
	}


	@Test
	@WithUserDetails(HAPPY_USER)
	void testEmptyCardsResponse() throws Exception {
		//setup
		final String expected = "{ \"cards\" : [ ]}";
		final Function<CdsServiceRequestJson, CdsServiceResponseJson> serviceFunction = (CdsServiceRequestJson theCdsServiceRequestJson) -> new CdsServiceResponseJson();
		myCdsServiceRegistry.unregisterService(TEST_SERVICE_ID, MODULE_ID);
		myCdsServiceRegistry.registerService(TEST_SERVICE_ID, serviceFunction, new CdsServiceJson().setId(TEST_SERVICE_ID), false, MODULE_ID);
		final CdsServiceRequestJson request = buildRequest(UUID.randomUUID().toString(), true, false);
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
		assertEquals(prettyJson(expected),prettyJson(actual));
	}

	private ObjectMapper badRequestObjectMapper(){

		SimpleModule module = new SimpleModule();
		module.addSerializer(CdsServiceRequestJson.class, new StdSerializer<>(CdsServiceRequestJson.class) {
			@Override
			public void serialize(CdsServiceRequestJson theCdsServiceRequestJson, JsonGenerator theJsonGenerator, SerializerProvider theSerializerProvider) throws IOException {
				theJsonGenerator.writeStartObject();
				theJsonGenerator.writeStringField("hookInstance", theCdsServiceRequestJson.getHookInstance());
				theJsonGenerator.writeStringField("hook", theCdsServiceRequestJson.getHookInstance());
				theJsonGenerator.writeFieldName("prefetch");
				theJsonGenerator.writeStartObject();
				theJsonGenerator.writeFieldName("patient");
				theJsonGenerator.writeStartObject();
				theJsonGenerator.writeEndObject();
				theJsonGenerator.writeEndObject();
				theJsonGenerator.writeEndObject();
			}
		});

		return new ObjectMapper().registerModule(module);
	}
**/
}
