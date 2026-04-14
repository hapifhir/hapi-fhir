package ca.uhn.fhir.storage.test;

/*-
 * #%L
 * hapi-fhir-storage-test-utilities
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.BaseTransactionProcessor;
import ca.uhn.fhir.jpa.dao.IdSubstitutionMap;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BaseTransactionProcessorTest {

	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	private PlatformTransactionManager myTransactionManager;

	@Test
	void testPerformIdSubstitutionsInMatchUrl_MatchAtStart() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "Patient?foo=urn:uuid:1234&bar=baz");
		assertEquals("Patient?foo=Patient/123&bar=baz", outcome);
	}

	/**
	 * Make sure versioned targets get the version stripped
	 */
	@Test
	void testPerformIdSubstitutionsInMatchUrl_MatchAtEnd() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:7ea4f3a6-d2a3-4105-9f31-374d525085d4"), new IdType("Patient/123/_history/1"));
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "Patient?name=FAMILY1&organization=urn%3Auuid%3A7ea4f3a6-d2a3-4105-9f31-374d525085d4");
		assertEquals("Patient?name=FAMILY1&organization=Patient/123", outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_MatchEscapedParam() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "Patient?foo=" + UrlUtil.escapeUrlParam("urn:uuid:1234") + "&bar=baz");
		assertEquals("Patient?foo=Patient/123&bar=baz", outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_MatchInParamNameShouldntBeReplaced() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "Patient?urn:uuid:1234=foo&bar=baz");
		assertEquals("Patient?urn:uuid:1234=foo&bar=baz", outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_NoParams() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String input = "Patient";
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, input);
		assertEquals(input, outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_UnterminatedParams() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/123"));
		String input = "Patient?foo&bar=&baz";
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, input);
		assertEquals(input, outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_ReplaceMultiple() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("urn:uuid:1234"), new IdType("Patient/abcdefghijklmnopqrstuvwxyz0123456789"));
		String input = "Patient?foo=urn:uuid:1234&bar=urn:uuid:1234&baz=urn:uuid:1234";
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, input);
		String expected = "Patient?foo=Patient/abcdefghijklmnopqrstuvwxyz0123456789&bar=Patient/abcdefghijklmnopqrstuvwxyz0123456789&baz=Patient/abcdefghijklmnopqrstuvwxyz0123456789";
		assertEquals(expected, outcome);
	}

	@Test
	void testPerformIdSubstitutionsInMatchUrl_NonUrnSubstitution() {
		IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("Patient/123"), new IdType("Patient/456"));
		String input = "Patient?foo=Patient/123";
		String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, input);
		assertEquals(input, outcome);
	}

	@Test
	void testUnqualifiedMatchUrlStart_RegexPatternMatches() {
		String matchUrl = "patient-first-identifier=MRN%7C123456789";
		boolean matchResult = BaseTransactionProcessor.UNQUALIFIED_MATCH_URL_START.matcher(matchUrl).find();
		assertThat(matchResult).as("Failed to find a Regex match using Url '" + matchUrl + "'").isTrue();
	}

	@Test
	void identifierSubstitutionNoQuestionMark() {
		final IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("Task/urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea"), new IdType("Task/1/history/1"));
		idSubstitutions.put(new IdType("urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea"), new IdType("Task/1/_history/1"));
		final String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "identifier=http://tempuri.org|2&based-on=urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea");
		assertEquals("identifier=http://tempuri.org|2&based-on=Task/1", outcome);
	}

	@Test
	void identifierSubstitutionYesQuestionMar() {
		final IdSubstitutionMap idSubstitutions = new IdSubstitutionMap();
		idSubstitutions.put(new IdType("Task/urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea"), new IdType("Task/1/history/1"));
		idSubstitutions.put(new IdType("urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea"), new IdType("Task/1/_history/1"));
		final String outcome = BaseTransactionProcessor.performIdSubstitutionsInMatchUrl(idSubstitutions, "?identifier=http://tempuri.org|2&based-on=urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea");
		assertEquals("?identifier=http://tempuri.org|2&based-on=Task/1", outcome);
	}

	@Test
	void testOperation_UnsupportedOperation() {
		BaseTransactionProcessor svc = newTransactionProcessor();

		Bundle request = new Bundle();
		request.setType(Bundle.BundleType.TRANSACTION);
		request.addEntry()
			.setResource(new Parameters())
			.getRequest()
			.setUrl("Patient/A/$summary")
			.setMethod(Bundle.HTTPVerb.POST);

		assertThatThrownBy(()->svc.transaction(new SystemRequestDetails(), request, false))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage("HAPI-2899: Operation $summary is not supported in a FHIR transaction");

	}


	@Test
	void testOperation_MetaAdd_NoPayload() {
		BaseTransactionProcessor svc = newTransactionProcessor();

		Bundle request = new Bundle();
		request.setType(Bundle.BundleType.TRANSACTION);
		request.addEntry()
				.getRequest()
					.setUrl("Patient/A/$meta-add")
					.setMethod(Bundle.HTTPVerb.POST);

		assertThatThrownBy(()->svc.transaction(new SystemRequestDetails(), request, false))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage("HAPI-0543: Missing required resource in Bundle.entry[0].resource for operation POST");
	}

	@Test
	void testOperation_MetaAdd_PayloadHasNoMeta() {
		BaseTransactionProcessor svc = newTransactionProcessor();

		Parameters payload = new Parameters();

		Bundle request = new Bundle();
		request.setType(Bundle.BundleType.TRANSACTION);
		request.addEntry()
				.setResource(payload)
				.getRequest()
					.setUrl("Patient/A/$meta-add")
					.setMethod(Bundle.HTTPVerb.POST);

		assertThatThrownBy(()->svc.transaction(new SystemRequestDetails(), request, false))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage("HAPI-2898: $meta-add request must have a parameter named 'meta' of type 'Meta'");
	}

	@Test
	void testOperation_MetaAdd_PayloadIsWrongType() {
		BaseTransactionProcessor svc = newTransactionProcessor();

		Basic payload = new Basic();

		Bundle request = new Bundle();
		request.setType(Bundle.BundleType.TRANSACTION);
		request.addEntry()
				.setResource(payload)
				.getRequest()
					.setUrl("Patient/A/$meta-add")
					.setMethod(Bundle.HTTPVerb.POST);

		assertThatThrownBy(()->svc.transaction(new SystemRequestDetails(), request, false))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage("HAPI-2898: $meta-add request must have a parameter named 'meta' of type 'Meta'");
	}

	@Test
	void testOperation_MetaAdd_PayloadBodyIsWrongType() {
		BaseTransactionProcessor svc = newTransactionProcessor();

		Parameters payload = new Parameters();
		payload.addParameter("meta", new StringType("HI"));

		Bundle request = new Bundle();
		request.setType(Bundle.BundleType.TRANSACTION);
		request.addEntry()
				.setResource(payload)
				.getRequest()
					.setUrl("Patient/A/$meta-add")
					.setMethod(Bundle.HTTPVerb.POST);

		assertThatThrownBy(()->svc.transaction(new SystemRequestDetails(), request, false))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessage("HAPI-2898: $meta-add request must have a parameter named 'meta' of type 'Meta'");
	}


	@ParameterizedTest
	@CsvSource(textBlock = """
		Patient/A/$meta-add    , Patient , A , $meta-add
		Patient                ,         ,   ,
		Patient/A              ,         ,   ,
		A/$meta-add            ,         ,   ,
		$meta-add              ,         ,   ,
		/$meta-add             ,         ,   ,
		                       ,         ,   ,
		""")
	void testParseUrlForOperationInvocation(String theUrl, String theType, String theId, String theOperationName) {
		// Setup
		BaseTransactionProcessor svc = newTransactionProcessor();

		// Test
		Optional<BaseTransactionProcessor.ParsedRequestOperation> actual = svc.parseUrlForOperationInvocation(theUrl);

		// Verify
		if (isNotBlank(theOperationName)) {
			assertThat(actual).isNotEmpty();
			BaseTransactionProcessor.ParsedRequestOperation parsed = actual.orElseThrow();
			assertEquals(theType, parsed.targetInstance().getResourceType());
			assertEquals(theId, parsed.targetInstance().getIdPart());
			assertEquals(theOperationName, parsed.operationName());
		} else {
			assertThat(actual).isEmpty();
		}
	}

	@Nonnull
	private BaseTransactionProcessor newTransactionProcessor() {
		BaseTransactionProcessor svc = new MyTransactionProcessor();
		svc.setContext(FhirContext.forR4Cached());
		svc.setInterceptorBroadcasterForUnitTest(myInterceptorBroadcaster);
		svc.setTxManager(myTransactionManager);
		svc.setVersionAdapter(new TransactionProcessorVersionAdapterR4());
		svc.setStorageSettings(new JpaStorageSettings());
		svc.setPartitionSettingsForUnitTest(new PartitionSettings());
		svc.setHapiTransactionService(new NonTransactionalHapiTransactionService());
		return svc;
	}

	private static class MyTransactionProcessor extends BaseTransactionProcessor {
		@Override
		protected void flushSession(@Nonnull TransactionDetails theTransactionDetails, Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome) {

		}
	}
}
