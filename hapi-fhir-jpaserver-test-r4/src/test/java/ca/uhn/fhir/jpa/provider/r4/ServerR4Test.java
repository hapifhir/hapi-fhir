package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingInterceptor;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingRuleBuilder;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.test.utilities.HttpTestHeader;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.util.ExtensionConstants;
import org.apache.http.HttpStatus;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.testcontainers.shaded.com.google.common.collect.HashMultimap;
import org.testcontainers.shaded.com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.ArrayList;
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
		String respString = myServer.fhirRequest("/metadata?_pretty=true&_format=json").get().assertStatus(200).getBody();

		ourLog.debug(respString);

		CapabilityStatement cs = myFhirContext.newJsonParser().parseResource(CapabilityStatement.class, respString);

		try {
			myCapabilityStatementDao.validate(cs, null, respString, EncodingEnum.JSON, null, null, null);
		} catch (PreconditionFailedException e) {
			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			fail();
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

	static List<Arguments> validationTestParameters() {
		@Language("JSON")
		String patientStr;
		List<Arguments> arguments = new ArrayList<>();

		// 1 The full resource from bug report
		{
			patientStr = """
				{
				      "resourceType" : "Patient",
				      "id" : "P12312",
				      "meta" : {
				        "profile" : ["http://hl7.org/fhir/StructureDefinition/Patient"]
				      },
				      "extension" : [ {
				        "url" : "http://hl7.org/fhir/StructureDefinition/us-core-ethnicity",
				        "extension" : [ {
				          "url" : "ombCategory",
				          "valueCoding" : {
				            "code" : "2186-5",
				            "display" : "Not Hispanic or Latino",
				            "system" : "urn:oid:2.16.840.1.113883.6.238"
				          }
				        }, {
				          "url" : "text",
				          "valueString" : "Non-Hisp"
				        } ]
				      }, {
				        "url" : "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race",
				        "extension" : [ {
				          "url" : "ombCategory",
				          "valueCoding" : {
				            "code" : "2054-5",
				            "display" : "Black or African American",
				            "system" : "urn:oid:2.16.840.1.113883.6.238"
				          }
				        }, {
				          "url" : "text",
				          "valueString" : "Black"
				        } ]
				      }, {
				        "url" : "http://hl7.org/fhir/us/core/StructureDefinition/us-core-birthsex",
				        "valueCode" : "M"
				      } ],
				      "communication" : [ {
				        "language" : {
				          "coding" : [ {
				            "code" : "en",
				            "display" : "English",
				            "system" : "urn:ietf:bcp:47"
				          }, {
				            "code" : "ENG",
				            "display" : "English",
				            "system" : "http://fkcfhir.org/fhir/CodeSystem/fmc-language-cs"
				          } ],
				          "text" : "EN"
				        },
				        "preferred" : true
				      } ],
				      "telecom" : [ {
				        "system" : "phone",
				        "value" : "393-342-2312"
				      } ],
				      "identifier" : [ {
				        "system" : "http://hl7.org/fhir/sid/us-ssn",
				        "type" : {
				          "coding" : [ {
				            "system" : "http://terminology.hl7.org/CodeSystem/v2-0203",
				            "code" : "SS",
				            "display" : "Social Security Number"
				          } ],
				          "text" : "Social Security Number"
				        },
				        "value" : "12133121"
				      }, {
				        "system" : "urn:oid:2.16.840.1.113883.3.7418.2.1",
				        "type" : {
				          "coding" : [ {
				            "system" : "http://terminology.hl7.org/CodeSystem/v2-0203",
				            "code" : "MR",
				            "display" : "Medical record number"
				          } ],
				          "text" : "Medical record number"
				        },
				        "value" : "12312"
				      } ],
				      "name" : [ {
				        "use" : "official",
				        "family" : "WEIHE",
				        "given" : [ "FLOREZ,A" ],
				        "period" : {
				          "start" : "2020-12-16T00:00:00-04:00"
				        }
				      } ],
				      "gender" : "male",
				      "birthDate" : "1955-09-19",
				      "active" : true,
				      "address" : [ {
				        "type" : "postal",
				        "line" : [ "1553 SUMMIT STREET" ],
				        "city" : "DAVENPORT",
				        "state" : "IA",
				        "postalCode" : "52809",
				        "country" : "USA",
				        "period" : {
				          "start" : "2020-12-16T00:00:00-04:00"
				        }
				      }, {
				        "type" : "physical",
				        "use" : "home",
				        "line" : [ "1553 SUMMIT STREET" ],
				        "city" : "DAVENPORT",
				        "state" : "IA",
				        "postalCode" : "52809",
				        "country" : "USA",
				        "period" : {
				          "start" : "2020-12-16T00:00:00-04:00"
				        }
				      } ],
				      "maritalStatus" : [ {
				        "coding" : [ {
				          "code" : "S",
				          "display" : "Never Married",
				          "system" : "http://terminology.hl7.org/CodeSystem/v3-MaritalStatus"
				        } ],
				        "text" : "S"
				      } ],
				      "contact" : [
				        {
				        "relationship" : [ {
				          "coding" : [ {
				            "code" : "PRN",
				            "display" : "parent",
				            "system" : "http://terminology.hl7.org/CodeSystem/v3-RoleCode"
				          } ],
				          "text" : "Parnt"
				        } ],
				        "name" : [ {
				          "use" : "official",
				          "family" : "PRESTIDGE",
				          "given" : [ "HEINEMAN" ]
				        } ],
				        "address" : [ {
				          "type" : "postal",
				          "line" : [ "1553 SUMMIT STREET" ],
				          "city" : "DAVENPORT",
				          "state" : "IA",
				          "postalCode" : "52809",
				          "country" : "USA",
				          "period" : {
				            "start" : "2020-12-16T00:00:00-04:00"
				          }
				        }, {
				          "type" : "physical",
				          "use" : "home",
				          "line" : [ "1553 SUMMIT STREET" ],
				          "city" : "DAVENPORT",
				          "state" : "IA",
				          "postalCode" : "52809",
				          "country" : "USA",
				          "period" : {
				            "start" : "2020-12-16T00:00:00-04:00"
				          }
				        } ],
				        "extension" : [ {
				          "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-type",
				          "valueCodeableConcept" : {
				            "coding" : [ {
				              "system" : "http://fkcfhir.org/fhir/CodeSystem/fmc-patient-contact-type-cs",
				              "code" : "PRIMARY",
				              "display" : "Primary Contact"
				            } ],
				            "text" : "Emergency"
				          }
				        } ]
				      },
				      {
				        "relationship" : [ {
				          "coding" : [ {
				            "code" : "E",
				            "display" : "Employer",
				            "system" : "http://terminology.hl7.org/CodeSystem/v2-0131"
				          } ],
				          "text" : "EMP"
				        } ],
				        "address" : [ {
				          "type" : "postal",
				          "line" : [ "1553 SUMMIT STREET" ],
				          "city" : "DAVENPORT",
				          "state" : "IA",
				          "postalCode" : "52809",
				          "country" : "USA",
				          "period" : {
				            "start" : "2020-12-16T00:00:00-04:00"
				          }
				        }, {
				          "type" : "physical",
				          "use" : "home",
				          "line" : [ "1553 SUMMIT STREET" ],
				          "city" : "DAVENPORT",
				          "state" : "IA",
				          "postalCode" : "52809",
				          "country" : "USA",
				          "period" : {
				            "start" : "2020-12-16T00:00:00-04:00"
				          }
				        } ],
				        "extension" : [ {
				          "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-type",
				          "valueCodeableConcept" : {
				            "coding" : [ {
				              "system" : "http://fkcfhir.org/fhir/CodeSystem/fmc-patient-contact-type-cs",
				              "code" : "EMPLOYER",
				              "display" : "Employer"
				            } ]
				          }
				        }, {
				          "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-primary-emp-ind",
				          "valueBoolean" : false
				        }, {
				          "url" : "http://fkcfhir.org/fhir/StructureDefinition/fmc-patient-contact-emp-status",
				          "valueString" : "jobStatus"
				        }]
				      } ]
				    }
				""";
			arguments.add(
				Arguments.of(patientStr, "P12312")
			);
		}

		// 2 A sample resource
		{
			/*
			 * This is an invalid patient resource.
			 * Patient.contact.name has a cardinality of
			 * 0..1 (so an array should fail).
			 *
			 * Our parser can easily handle this (and doesn't care about
			 * the cardinality), but our endpoints should.
			 */
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
			arguments.add(
				Arguments.of(patientStr, "P1212")
			);
		}

		return arguments;
	}

	@ParameterizedTest
	@MethodSource("validationTestParameters")
	public void validationTest_invalidResourceWithLenientParsing_createAndValidateShouldParse(String thePatientStr, String theId) throws IOException {
		/*
		 * We also require a lenient error handler (the default case).
		 * BaseJpaR4Test.before resets this to a StrictErrorHandler,
		 * which breaks this test, but also means we don't have to reset it.
		 */
		myFhirContext.setParserErrorHandler(new LenientErrorHandler());

		IParser parser = myFhirContext.newJsonParser();

		DemoValidationInterceptor validatingInterceptor = new DemoValidationInterceptor(myFhirContext, myApplicationContext);
		validatingInterceptor.start();

		myServer.getInterceptorService().registerInterceptor(validatingInterceptor);
		try {
			OperationOutcome validationOutcome = getOutcome(
				myServer.fhirRequest("/Patient/$validate")
					.post(thePatientStr, Constants.CT_FHIR_JSON_NEW)
					.assertStatus(HttpStatus.SC_OK),
				parser);

			OperationOutcome createOutcome = getOutcome(
				myServer.fhirRequest("/Patient/" + theId)
					.put(thePatientStr, Constants.CT_FHIR_JSON_NEW)
					.assertStatus(HttpStatus.SC_PRECONDITION_FAILED),
				parser);

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

	private OperationOutcome getOutcome(HttpTestResponse theResponse, IParser theParser) {
		return theParser.parseResource(OperationOutcome.class, theResponse.getBody());
	}

	/**
	 * See #519
	 */
	@Test
	public void saveIdParamOnlyAppearsOnce() throws IOException {
		HttpTestResponse resp = myServer.fhirRequest("/metadata?_pretty=true&_format=xml").get();
		ourLog.info(resp.toString());
		resp.assertStatus(200);

		String respString = resp.getBody();
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
	@ValueSource(strings = {Constants.HEADER_REQUEST_ID, Constants.HEADER_REQUEST_ID, Constants.HEADER_REQUEST_ID, Constants.HEADER_REQUEST_ID})
	public void testXRequestIdHeaderRetainsCase(String theXRequestIdHeaderKey) throws Exception {
		String xRequestIdHeaderValue = "abc123";

		HttpTestResponse response = myServer.fhirRequest("/Patient").withHeader(theXRequestIdHeaderKey, xRequestIdHeaderValue).get().assertStatus(200);

		ourLog.debug(response.getBody());

		List<HttpTestHeader> xRequestIdHeaders = response.getAllHeaders().stream()
			.filter(header -> theXRequestIdHeaderKey.equals(header.name()))
			.toList();

		assertEquals(1, xRequestIdHeaders.size());
		assertEquals(xRequestIdHeaderValue, xRequestIdHeaders.get(0).value());
	}
}
