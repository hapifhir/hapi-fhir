package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingInterceptor;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingRuleBuilder;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.ExtensionConstants;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.testcontainers.shaded.com.google.common.collect.HashMultimap;
import org.testcontainers.shaded.com.google.common.collect.Multimap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class ServerR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerR4Test.class);

	@Autowired
	private IFhirResourceDao<CapabilityStatement> myCapabilityStatementDao;

	@Test
	public void testCapabilityStatementValidates() throws IOException {
		HttpGet get = new HttpGet(myServerBase + "/metadata?_pretty=true&_format=json");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);

			ourLog.debug(respString);

			CapabilityStatement cs = myFhirContext.newJsonParser().parseResource(CapabilityStatement.class, respString);

			try {
				myCapabilityStatementDao.validate(cs, null, respString, EncodingEnum.JSON, null, null, null);
			} catch (PreconditionFailedException e) {
				ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
				fail();
			}
		}
	}

	private static class DemoValidationInterceptor extends RepositoryValidatingInterceptor {

		@Autowired
		private FhirContext myFhirContext;

		@Autowired
		private ApplicationContext myApplicationContext;

		public DemoValidationInterceptor(FhirContext theFhirContext, ApplicationContext theContext) {
			super();
			myFhirContext = theFhirContext;
			myApplicationContext = theContext;
		}

		public void start() {
			setFhirContext(myFhirContext);

			// Ask the application context for a new Rule Builder
			RepositoryValidatingRuleBuilder ruleBuilder =
				myApplicationContext.getBean(RepositoryValidatingRuleBuilder.class);

			// we only want the basic validation profiles
			ruleBuilder
				.forResourcesOfType("Patient")
				.requireValidationToDeclaredProfiles();

			// Create the ruleset and pass it to the interceptor
			setRules(ruleBuilder.build());
		}
	}

	@Test
	public void validationTest_invalidResource_createAndValidateShouldBehaveTheSame() throws IOException {
		// we have to manually create this because it's in the servlet context,
		// and we only spin up application context
		DemoValidationInterceptor validatingInterceptor = new DemoValidationInterceptor(myFhirContext, myApplicationContext);
		validatingInterceptor.start();

		myServer.getInterceptorService().registerInterceptor(validatingInterceptor);

		/*
		 * This is an invalid patient resource.
		 * Patient.contact.name has a cardinality of
		 * 0..1 (so an array should fail).
		 *
		 * Our parser can easily handle this (and doesn't care about
		 * the cardinality), but our endpoints should.
		 */
		String patientStr;
		{
			patientStr = """
				{
					"resourceType": "Patient",
					"id": "P1212",
					"contact": [{
						"name": [{
							"use": "official",
							"family": "Simpson",
							"given": ["Homer" ]
						}]
					}],
					"text": {
						"status": "additional",
						"div": "<div>a div element</div>"
					}
				}
				""";
		}

		IParser parser = myFhirContext.newJsonParser();
		try {
			StringEntity entity = new StringEntity(patientStr, StandardCharsets.UTF_8);

			OperationOutcome validationOutcome;
			OperationOutcome createOutcome;

			HttpPost post = new HttpPost(myServerBase + "/Patient/$validate");
			post.addHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_FHIR_JSON_NEW);
			post.setEntity(entity);
			try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
				assertEquals(HttpStatus.SC_OK, resp.getStatusLine().getStatusCode());

				validationOutcome = getOutcome(resp, parser);
			}

			HttpPut put = new HttpPut(myServerBase + "/Patient/P1212");
			put.addHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_FHIR_JSON_NEW);
			put.setEntity(entity);
			try (CloseableHttpResponse resp = ourHttpClient.execute(put)) {
				assertEquals(HttpStatus.SC_PRECONDITION_FAILED, resp.getStatusLine().getStatusCode());

				createOutcome = getOutcome(resp, parser);
			}

			assertNotNull(validationOutcome);
			assertNotNull(createOutcome);

			assertEquals(validationOutcome.getIssue().size(), createOutcome.getIssue().size());

			Multimap<OperationOutcome.IssueSeverity, String> severityToIssue = HashMultimap.create();
			validationOutcome.getIssue()
				.forEach(issue -> {
					severityToIssue.put(issue.getSeverity(), issue.getDiagnostics());
				});
			createOutcome.getIssue()
				.forEach(issue -> {
					assertTrue(severityToIssue.containsEntry(issue.getSeverity(), issue.getDiagnostics()));
				});
		} finally {
			myServer.getInterceptorService().unregisterInterceptor(validatingInterceptor);
		}
	}

	private OperationOutcome getOutcome(CloseableHttpResponse theResponse, IParser theParser) throws IOException {
		String content = IOUtils.toString(theResponse.getEntity().getContent(), StandardCharsets.UTF_8);

		return theParser.parseResource(OperationOutcome.class, content);
	}

	/**
	 * See #519
	 */
	@Test
	public void saveIdParamOnlyAppearsOnce() throws IOException {
		HttpGet get = new HttpGet(myServerBase + "/metadata?_pretty=true&_format=xml");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			ourLog.info(resp.toString());
			assertEquals(200, resp.getStatusLine().getStatusCode());

			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.debug(respString);

			CapabilityStatement cs = myFhirContext.newXmlParser().parseResource(CapabilityStatement.class, respString);

			for (CapabilityStatementRestResourceComponent nextResource : cs.getRest().get(0).getResource()) {
				ourLog.info("Testing resource: " + nextResource.getType());
				Set<String> sps = new HashSet<String>();
				for (CapabilityStatementRestResourceSearchParamComponent nextSp : nextResource.getSearchParam()) {
					if (sps.add(nextSp.getName()) == false) {
						fail("Duplicate search parameter " + nextSp.getName() + " for resource " + nextResource.getType());
					}
				}

				if (!sps.contains("_id")) {
					fail("No search parameter _id for resource " + nextResource.getType());
				}
			}
		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}
	}


	@Test
	public void testMetadataIncludesResourceCounts() {
		Patient p = new Patient();
		p.setActive(true);
		myClient.create().resource(p).execute();

		/*
		 * Initial fetch after a clear should return
		 * no results
		 */
		myResourceCountsCache.clear();

		CapabilityStatement capabilityStatement = myClient
			.capabilities()
			.ofType(CapabilityStatement.class)
			.execute();

		Extension patientCountExt = capabilityStatement
			.getRest()
			.get(0)
			.getResource()
			.stream()
			.filter(t -> t.getType().equals("Patient"))
			.findFirst()
			.orElseThrow(() -> new InternalErrorException("No patient"))
			.getExtensionByUrl(ExtensionConstants.CONF_RESOURCE_COUNT);
		assertNull(patientCountExt);

		/*
		 * Now run a background pass (the update
		 * method is called by the scheduler normally)
		 */
		myResourceCountsCache.update();

		capabilityStatement = myClient
			.capabilities()
			.ofType(CapabilityStatement.class)
			.execute();

		patientCountExt = capabilityStatement
			.getRest()
			.get(0)
			.getResource()
			.stream()
			.filter(t -> t.getType().equals("Patient"))
			.findFirst()
			.orElseThrow(() -> new InternalErrorException("No patient"))
			.getExtensionByUrl(ExtensionConstants.CONF_RESOURCE_COUNT);
		assertEquals("1", patientCountExt.getValueAsPrimitive().getValueAsString());

	}


	@ParameterizedTest
	@ValueSource(strings = {"x-request-id", "X-Request-Id", "X-Request-ID", "X-REQUEST-ID"})
	public void testXRequestIdHeaderRetainsCase(String theXRequestIdHeaderKey) throws Exception {
		HttpGet get = new HttpGet(myServerBase + "/Patient");
		String xRequestIdHeaderValue = "abc123";
		get.addHeader(theXRequestIdHeaderKey, xRequestIdHeaderValue);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			assertEquals(200, response.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.debug(responseContent);

			List<Header> xRequestIdHeaders = Arrays.stream(response.getAllHeaders())
				.filter(header -> theXRequestIdHeaderKey.equals(header.getName()))
				.toList();

			assertEquals(1, xRequestIdHeaders.size());
			assertEquals(xRequestIdHeaderValue, xRequestIdHeaders.get(0).getValue());
		}
	}
}
