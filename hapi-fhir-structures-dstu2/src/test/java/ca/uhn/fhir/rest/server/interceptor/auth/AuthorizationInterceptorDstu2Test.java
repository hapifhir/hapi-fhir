package ca.uhn.fhir.rest.server.interceptor.auth;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.method.IRequestOperationCallback;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class AuthorizationInterceptorDstu2Test {

	private static final String ERR403 = "{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"processing\",\"diagnostics\":\"Access denied by default policy (no applicable rules)\"}]}";
	private static CloseableHttpClient ourClient;
	private static String ourConditionalCreateId;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static boolean ourHitMethod;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AuthorizationInterceptorDstu2Test.class);
	private static int ourPort;
	private static List<IResource> ourReturn;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@Before
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		for (IServerInterceptor next : new ArrayList<IServerInterceptor>(ourServlet.getInterceptors())) {
			ourServlet.unregisterInterceptor(next);
		}
		ourReturn = null;
		ourHitMethod = false;
		ourConditionalCreateId = "1123";
	}

	private HttpEntity createFhirResourceEntity(IBaseResource theResource) {
		String out = ourCtx.newJsonParser().encodeResourceToString(theResource);
		return new StringEntity(out, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8"));
	}

	private IResource createObservation(Integer theId, String theSubjectId) {
		Observation retVal = new Observation();
		if (theId != null) {
			retVal.setId(new IdDt("Observation", (long) theId));
		}
		retVal.getCode().setText("OBS");
		retVal.setSubject(new ResourceReferenceDt(theSubjectId));
		return retVal;
	}

	private IResource createPatient(Integer theId) {
		Patient retVal = new Patient();
		if (theId != null) {
			retVal.setId(new IdDt("Patient", (long) theId));
		}
		retVal.addName().addFamily("FAM");
		return retVal;
	}

	private String extractResponseAndClose(HttpResponse status) throws IOException {
		if (status.getEntity() == null) {
			return null;
		}
		String responseContent;
		responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		return responseContent;
	}

	@Test
	public void testAllowAll() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.allowAll("Default Rule")
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Rule 1"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testBatchWhenOnlyTransactionAllowed() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.build();
				//@formatter:on
			}
		});

		Bundle input = new Bundle();
		input.setType(BundleTypeEnum.BATCH);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(HTTPVerbEnum.POST);

		Bundle output = new Bundle();
		output.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		output.addEntry().getResponse().setLocation("/Patient/1");

		HttpPost httpPost;
		HttpResponse status;

		ourReturn = Arrays.asList((IResource) output);
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testOperationByInstanceOfTypeAllowed() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class)
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = Arrays.asList();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Encounter/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);
		
	}

	@Test
	public void testOperationByInstanceOfTypeWithInvalidReturnValue() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;
		
		// With a return value we don't allow
		ourReturn = Arrays.asList(createPatient(222));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		// With a return value we do
		ourReturn = Arrays.asList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);
		
	}

	@Test
	public void testOperationByInstanceOfTypeWithReturnValue() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class)
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Bundle"));
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(true, ourHitMethod);

		ourReturn = Arrays.asList();
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Encounter/1/$everything");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("OperationOutcome"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(false, ourHitMethod);
	}

	@Test
	public void testBatchWhenTransactionReadDenied() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.build();
				//@formatter:on
			}
		});

		Bundle input = new Bundle();
		input.setType(BundleTypeEnum.BATCH);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(HTTPVerbEnum.POST);

		Bundle output = new Bundle();
		output.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		output.addEntry().setResource(createPatient(2));

		HttpPost httpPost;
		HttpResponse status;

		ourReturn = Arrays.asList((IResource) output);
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testBatchWhenTransactionWrongBundleType() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.build();
				//@formatter:on
			}
		});

		Bundle input = new Bundle();
		input.setType(BundleTypeEnum.COLLECTION);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(HTTPVerbEnum.POST);

		Bundle output = new Bundle();
		output.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		output.addEntry().setResource(createPatient(1));

		HttpPost httpPost;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList((IResource) output);
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
	}

	@Test
	public void testDeleteByCompartment() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").delete().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(1));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testDenyAll() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("Default Rule")
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$validate");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testMetadataAllow() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").metadata()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Arrays.asList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testMetadataDeny() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.deny("Rule 1").metadata()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Arrays.asList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testOperationAnyName() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("RULE 1").operation().withAnyName().onServer().andThen()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		// Server
		ourHitMethod = false;
		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testOperationInstanceLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onInstance(new IdDt("http://example.com/Patient/1/_history/2")).andThen()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong instance
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

	}

	@Test
	public void testOperationNotAllowedWithWritePermissiom() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("RULE 1").write().allResources().withAnyId().andThen()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// System
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationServerLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onServer().andThen()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testOperationTypeLevel() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onType(Patient.class).andThen()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		// Server
		ourHitMethod = false;
		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Type
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Wrong type
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Wrong name
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		// Instance
		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/$opName");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testReadByAnyId() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).withAnyId()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1/_history/222");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentRight() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Arrays.asList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentWrong() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testTransactionWriteGood() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.build();
				//@formatter:on
			}
		});

		Bundle input = new Bundle();
		input.setType(BundleTypeEnum.TRANSACTION);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(HTTPVerbEnum.PUT);

		Bundle output = new Bundle();
		output.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		output.addEntry().getResponse().setLocation("/Patient/1");

		HttpPost httpPost;
		HttpResponse status;

		ourReturn = Arrays.asList((IResource) output);
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testWriteByCompartmentCreate() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 1b").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1123")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		// Conditional
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.addHeader("If-None-Exist", "Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/2")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/1")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentCreateConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").createConditional().resourcesOfType(Patient.class)
					.build();
				//@formatter:on
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.addHeader(Constants.HEADER_IF_NONE_EXIST, "foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDeleteConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").deleteConditional().resourcesOfType(Patient.class)
					.build();
				//@formatter:on
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourReturn = Arrays.asList(createPatient(1));

		ourHitMethod = false;
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient?foo=bar");
		status = ourClient.execute(httpDelete);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDeleteConditionalWithoutDirectMatch() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 2").deleteConditional().resourcesOfType(Patient.class)
					.build();
				//@formatter:on
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourReturn = Arrays.asList(createPatient(1));

		ourHitMethod = false;
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient?foo=bar");
		status = ourClient.execute(httpDelete);
		String response = extractResponseAndClose(status);
		ourLog.info(response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentDoesntAllowDelete() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(1));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentUpdate() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		String response;
		HttpResponse status;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/2");
		httpPost.setEntity(createFhirResourceEntity(createPatient(2)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/1");
		httpPost.setEntity(createFhirResourceEntity(createPatient(1)));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		// Conditional
		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(1)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(99)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/10");
		httpPost.setEntity(createFhirResourceEntity(createObservation(10, "Patient/1")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/10");
		httpPost.setEntity(createFhirResourceEntity(createObservation(10, "Patient/2")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(ERR403, response);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToInvalid() throws Exception {
		ourConditionalCreateId = "1123";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().resourcesOfType(Patient.class)
					.build();
				//@formatter:on
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToValid() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().resourcesOfType(Patient.class)
					.build();
				//@formatter:on
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/12")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertFalse(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToValidAllTypes() throws Exception {
		ourConditionalCreateId = "1";

		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().allResources()
					.build();
				//@formatter:on
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation?foo=bar");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/12")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(403, status.getStatusLine().getStatusCode());
		assertEquals(ERR403, response);
		assertTrue(ourHitMethod);

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {

		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patProvider = new DummyPatientResourceProvider();
		DummyObservationResourceProvider obsProv = new DummyObservationResourceProvider();
		DummyEncounterResourceProvider encProv = new DummyEncounterResourceProvider();
		PlainProvider plainProvider = new PlainProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setFhirContext(ourCtx);
		ourServlet.setResourceProviders(patProvider, obsProv, encProv);
		ourServlet.setPlainProviders(plainProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl) {
			ourHitMethod = true;
			theResource.setId("Observation/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(@IdParam IdDt theId) {
			ourHitMethod = true;
			MethodOutcome retVal = new MethodOutcome();
			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Observation.class;
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation(@IdParam IdDt theId) {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Read(version = true)
		public Observation read(@IdParam IdDt theId) {
			ourHitMethod = true;
			return (Observation) ourReturn.get(0);
		}

		@Search()
		public List<IResource> search() {
			ourHitMethod = true;
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdDt theId, @ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			ourHitMethod = true;

			if (isNotBlank(theConditionalUrl)) {
				IdDt actual = new IdDt("Observation", ourConditionalCreateId);
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, actual);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(actual);
			} else {
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, theResource);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(theId.withVersion("2"));
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

	}

	public static class DummyEncounterResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Encounter.class;
		}
		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdDt theId) {
			ourHitMethod = true;
			Bundle retVal = new Bundle();
			for (IResource next : ourReturn) {
				retVal.addEntry().setResource(next);
			}
			return retVal;
		}
	}
	
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {

			if (isNotBlank(theConditionalUrl)) {
				IdDt actual = new IdDt("Patient", ourConditionalCreateId);
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, actual);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.CREATE);
			} else {
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, theResource);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.CREATE);
			}

			ourHitMethod = true;
			theResource.setId("Patient/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(IRequestOperationCallback theRequestOperationCallback, @IdParam IdDt theId, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			ourHitMethod = true;
			for (IBaseResource next : ourReturn) {
				theRequestOperationCallback.resourceDeleted(next);
			}
			MethodOutcome retVal = new MethodOutcome();
			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation(@IdParam IdDt theId) {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdDt theId) {
			ourHitMethod = true;
			Bundle retVal = new Bundle();
			for (IResource next : ourReturn) {
				retVal.addEntry().setResource(next);
			}
			return retVal;
		}

		@Operation(name = "opName2", idempotent = true)
		public Parameters operation2(@IdParam IdDt theId) {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Read(version = true)
		public Patient read(@IdParam IdDt theId) {
			ourHitMethod = true;
			return (Patient) ourReturn.get(0);
		}

		@Search()
		public List<IResource> search() {
			ourHitMethod = true;
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdDt theId, @ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			ourHitMethod = true;

			if (isNotBlank(theConditionalUrl)) {
				IdDt actual = new IdDt("Patient", ourConditionalCreateId);
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, actual);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(actual);
			} else {
				ActionRequestDetails subRequest = new ActionRequestDetails(theRequestDetails, theResource);
				subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
				theResource.setId(theId.withVersion("2"));
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(theResource);
			return retVal;
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @IdParam IdDt theId, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding,
				@Validate.Mode ValidationModeEnum theMode, @Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			ourHitMethod = true;
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
				@Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			ourHitMethod = true;
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

	}

	public static class PlainProvider {

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			ourHitMethod = true;
			return (Parameters) new Parameters().setId("1");
		}

		@Transaction()
		public Bundle search(@TransactionParam Bundle theInput) {
			ourHitMethod = true;
			return (Bundle) ourReturn.get(0);
		}

	}

}
