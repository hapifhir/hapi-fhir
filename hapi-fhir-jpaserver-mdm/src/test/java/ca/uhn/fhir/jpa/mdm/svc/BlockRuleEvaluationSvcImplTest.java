package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.mdm.svc.testmodels.BlockRuleTestCase;
import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockListRuleJson;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
public class BlockRuleEvaluationSvcImplTest {
	private static final Logger ourLog = getLogger(BlockRuleEvaluationSvcImplTest.class);

	@Mock
	private IBlockListRuleProvider myRuleProvider;

	@Spy
	private static FhirContext ourFhirContext = FhirContext.forR4Cached();

	@InjectMocks
	private BlockRuleEvaluationSvcImpl myRuleEvaluationSvc;

	/**
	 * Provides a list of test cases to maximize coverage
	 * and type matching.
	 */
	private static Collection<BlockRuleTestCase> getTestCasesFhir() {
		IParser parser = ourFhirContext.newJsonParser();

		List<BlockRuleTestCase> data = new ArrayList<>();

		/*
		 * Basic start case
		 * Blocking on single() name that is jane doe
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"name": [{
				     	   "family": "doe",
				   			"given": [
				            	 "jane"
				   			]
				 			}]
				}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("name.single().family")
				.setBlockedValue("Doe");
			rule.addBlockListField()
				.setFhirPath("name.single().given.first()")
				.setBlockedValue("Jane");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					"Basic happy path test - Block on Jane Doe",
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					true
				)
			);
		}

		/*
		 * Blocking on official name with "Jane Doe".
		 * Patient has multiple given names on official name
		 * Mdm is not blocked (no unique value found to compare)
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"name": [{
				     	   "family": "smith",
				   		"given": [
				         	"jane"
				   		]
				 	},
				 	{
				 			"family": "doe",
				 			"use": "official",
				 			"given": [
				 				"trixie",
				 				"janet",
				 				"jane"
				 			]
				 	}]
				}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("name.where(use = 'official').family")
				.setBlockedValue("Doe");
			rule.addBlockListField()
				.setFhirPath("name.where(use = 'official').given")
				.setBlockedValue("Jane");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					"Blocking on official name 'Jane Doe'",
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					false
				)
			);
		}

		/*
		 * Blocking on extension
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"extension": [{
						"url": "http://localhost/test",
						"valueString": "example"
					}]
				}
			""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("extension.where(url = 'http://localhost/test').value.first()")
				.setBlockedValue("example");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					"Blocking on extension value",
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					true
				)
			);
		}

		/*
		 * Block on identifier with specific system and value
		 * Patient contains specific identifier (and others)
		 * Mdm is blocked
		 */
		{
			String patientStr = """
				 			{
				 				"resourceType": "Patient",
				 				"identifier": [{
				 					"system": "urn:oid:2.2.36.146.595.217.0.1",
				 					"value": "23456"
				 				}, {
				 					"system": "urn:oid:1.2.36.146.595.217.0.1",
				 					"value": "12345"
				 				}]
				 			}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("identifier.where(system = 'urn:oid:1.2.36.146.595.217.0.1').value")
				.setBlockedValue("12345");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					"Blocking on identifier with specific system and value",
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					true
				)
			);
		}

		/*
		 * Block on first identifier with provided system
		 * and value.
		 */
		{
			String patientStr = """
				 			{
				 				"resourceType": "Patient",
				 				"identifier": [{
				 					"system": "urn:oid:1.2.36.146.595.217.0.1",
				 					"value": "44444"
				 				}, {
				 					"system": "urn:oid:1.2.36.146.595.217.0.1",
				 					"value": "12345"
				 				}]
				 			}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("identifier.first().system")
				.setBlockedValue("urn:oid:1.2.36.146.595.217.0.1");
			rule.addBlockListField()
				.setFhirPath("identifier.first().value")
				.setBlockedValue("12345");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					"Block on first identifier with value and system",
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					false
				)
			);
		}

		/*
		 * Blocked fields do not exist on resource, so mdm is not blocked
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"name": [{
				     	   "family": "doe",
				   			"given": [
				            	 "jane"
				   			]
				 			}]
				}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("identifier.system")
				.setBlockedValue("urn:oid:1.2.36.146.595.217.0.1");
			rule.addBlockListField()
				.setFhirPath("identifier.value")
				.setBlockedValue("12345");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					"Blocking on field that doesn't exist",
					blockListJson,
					parser.parseResource(patientStr),
					false
				)
			);
		}

		/*
		 * DateTime
		 * multi-type field is blocked on specific date; resource has matching
		 * specific date so mdm is blocked
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"name": [{
				     	   "family": "doe",
				   			"given": [
				            	 "jane"
				   			]
				 			}],
				 			"deceasedDateTime": "2000-01-01"
				}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("deceased.value")
				.setBlockedValue("2000-01-01");
			blockListJson.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				"Blocking on multi-type field (date or boolean) with specific date value.",
				blockListJson,
				parser.parseResource(patientStr),
				true
			));
		}

		/*
		 * DateTime
		 * Block is specified on multi-type datetime. Resource
		 * has a multi-type boolean value, so MDM is not blocked
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"name": [{
				     	   "family": "doe",
				   			"given": [
				            	 "jane"
				   			]
				 			}],
				 			"deceasedBoolean": true
				}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("deceased.value")
				.setBlockedValue("2000-01-01");
			blockListJson.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				"Blocking on multi-value (boolean, date) value on date value when actual value is boolean",
				blockListJson,
				parser.parseResource(patientStr),
				false
			));
		}

		/*
		 * Code (Enum)
		 * Blocking is on exact link.type value.
		 * Patient has this exact enum value and so mdm is blocked.
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"name": [{
				     	   "family": "hirasawa",
				     	   "given": [
				     	   	"yui"
				     	   ]
				 			}],
				 			"link": [{
				 				"type": "seealso"
				 			}]
				}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("link.first().type")
				.setBlockedValue("seealso");
			blockListJson.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				"Blocking on link.type value (an enum)",
				blockListJson,
				parser.parseResource(patientStr),
				true
			));
		}

		/*
		 * CodableConcept
		 * Blocking is on exact maritalStatus.coding.code.
		 * Patient has this value, so the value is blocked.
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"name": [{
				     	   "family": "jetson",
				     	   "given": [
				     	   	"jane"
				     	   ]
				 			}],
				 			"maritalStatus": {
				 				"coding": [{
				 					"system": "http://terminology.hl7.org/CodeSystem/v3-MaritalStatus",
				 					"code": "M"
				 				}]
				 			}
				}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("maritalStatus.coding.where(system = 'http://terminology.hl7.org/CodeSystem/v3-MaritalStatus').code")
				.setBlockedValue("m");
			blockListJson.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				"Blocking on maritalStatus with specific system and blocked value",
				blockListJson,
				parser.parseResource(patientStr),
				true
			));
		}

		/*
		 * Boolean (trivial, but completions sake)
		 * Blocking on active = true.
		 * Patient is active, so mdm is blocked.
		 */
		{
			String patientStr = """
    				{
    					"resourceType": "Patient",
    					"active": true
    				}
				""";
			BlockListJson json = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("active")
				.setBlockedValue("true");
			json.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				"Blocking on boolean field",
				json,
				parser.parseResource(patientStr),
				true
			));
		}

		/*
		 * Blocking using 'single()' when no single value exists
		 */
		{
			String patientStr = """
				 			{
				 				"resourceType": "Patient",
				 				"identifier": [{
				 					"system": "urn:oid:1.2.36.146.595.217.0.1",
				 					"value": "44444"
				 				}, {
				 					"system": "urn:oid:1.2.36.146.595.217.0.1",
				 					"value": "12345"
				 				}]
				 			}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("identifier.single().system")
				.setBlockedValue("urn:oid:1.2.36.146.595.217.0.1");
			rule.addBlockListField()
				.setFhirPath("identifier.single().value")
				.setBlockedValue("12345");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					"Block on single() identifier with multiple present identifiers",
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					false
				)
			);
		}

		/*
		 * Block attempt on non-primitive value
		 */
		{
			String patientStr = """
				 			{
				 				"resourceType": "Patient",
				 				"identifier": [{
				 					"system": "urn:oid:1.2.36.146.595.217.0.1",
				 					"value": "12345"
				 				}]
				 			}
				""";
			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("identifier")
				.setBlockedValue("urn:oid:1.2.36.146.595.217.0.1");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					"Block on identifier field (non-primitive)",
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					false
				)
			);
		}

		return data;
	}

	@ParameterizedTest
	@MethodSource("getTestCasesFhir")
	public void isMdmMatchingBlocked_givenResourceAndRules_returnsExpectedValue(BlockRuleTestCase theTestCase) {
		ourLog.info(theTestCase.getId());

		// setup
		BlockListJson blockList = theTestCase.getBlockRule();
		IBaseResource patient = theTestCase.getPatientResource();
		boolean expected = theTestCase.isExpectedBlockResult();

		// when
		when(myRuleProvider.getBlocklistRules())
			.thenReturn(blockList);

		// test
		assertThat(myRuleEvaluationSvc.isMdmMatchingBlocked((IAnyResource) patient)).as(theTestCase.getId()).isEqualTo(expected);
	}

	@Test
	public void isMdmMatchingBlocked_noBlockRules_returnsFalse() {
		// setup
		Patient patient = new Patient();
		patient.addName()
			.setFamily("Doe")
			.addGiven("Jane");

		// test
		assertFalse(myRuleEvaluationSvc.isMdmMatchingBlocked(patient));
	}
}
