package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.mdm.svc.testmodels.BlockRuleTestCase;
import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockListRuleJson;
import ca.uhn.fhir.mdm.blocklist.models.BlockFieldBlockRuleEnum;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
	private static Collection<BlockRuleTestCase> getTestCases() {
		IParser parser = ourFhirContext.newJsonParser();

		List<BlockRuleTestCase> data = new ArrayList<>();

		/*
		 * String
		 *
		 * Exact block on: name.given && name.family
		 * Patient with only blocked names
		 * isBlocked = true
		 *
		 * Basic block case
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
				.setFhirPath("name.family")
				.setBlockedValue("Doe");
			rule.addBlockListField()
				.setFhirPath("name.given")
				.setBlockedValue("Jane");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					true
				)
			);
		}

		/*
		 * String/Identifier
		 *
		 * Exact block on: identifier.system && identifier.value
		 * Patient with more than 1 identifier with matching system
		 * isBlocked = false
		 *
		 * Exact match on system means only 1 matching system should be found,
		 * but there are 2 (so no block)
		 */
		{
			// block on identifier
			String patientStr = """
				 			{
				 				"resourceType": "Patient",
				 				"identifier": [{
				 					"system": "urn:oid:1.2.36.146.595.217.0.1",
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
				.setFhirPath("identifier.system")
				.setBlockedValue("urn:oid:1.2.36.146.595.217.0.1");
			rule.addBlockListField()
				.setFhirPath("identifier.value")
				.setBlockedValue("12345");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					false
				)
			);
		}

		/*
		 * String/Identifier
		 *
		 * Block on patient with any identifier.system and identifier.value
		 * Patient with an identifier with blocked system
		 * isBlocked = true
		 *
		 * Block on any system or value means if any system matches, it blocks.
		 */
		{
			// block on identifier
			String patientStr = """
				 			{
				 				"resourceType": "Patient",
				 				"identifier": [{
				 					"system": "urn:oid:1.2.36.146.595.217.0.1",
				 					"value": "44444"
				 				}, {
				 					"system": "urn:oid:2.2.22.146.595.217.0.1",
				 					"value": "12345"
				 				}]
				 			}
				""";

			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("identifier.system")
				.setBlockedValue("urn:oid:1.2.36.146.595.217.0.1")
				.setBlockRule(BlockFieldBlockRuleEnum.ANY);
			rule.addBlockListField()
				.setFhirPath("identifier.value")
				.setBlockedValue("12345")
				.setBlockRule(BlockFieldBlockRuleEnum.ANY);
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					true
				)
			);
		}

		/*
		 * String/Identifier
		 *
		 * Block on patient with any identifier.system and identifier.value
		 * Patient with no identifier
		 * isBlocked = false
		 *
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
				.setBlockedValue("urn:oid:1.2.36.146.595.217.0.1")
				.setBlockRule(BlockFieldBlockRuleEnum.ANY);
			rule.addBlockListField()
				.setFhirPath("identifier.value")
				.setBlockedValue("12345")
				.setBlockRule(BlockFieldBlockRuleEnum.ANY);
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					blockListJson,
					parser.parseResource(patientStr),
					false
				)
			);
		}

		/*
		 * DateTime
		 *
		 * Block on patient with exact match on deceasedDateTime
		 * Patient with matching deceasedDateTime
		 * isBlocked = true
		 *
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
				.setFhirPath("deceasedDateTime")
				.setBlockedValue("2000-01-01");
			blockListJson.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				blockListJson,
				parser.parseResource(patientStr),
				true
			));
		}

		/*
		 * DateTime
		 *
		 * Block on patient with exact match on deceasedDateTime
		 * Patient with deceasedBoolean
		 * isBlocked = false
		 *
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
				.setFhirPath("deceasedDateTime")
				.setBlockedValue("2000-01-01");
			blockListJson.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				blockListJson,
				parser.parseResource(patientStr),
				false
			));
		}

		/*
		 * String
		 *
		 * Block on Patient with EXACT match on name.family and name.given
		 * Patient with 2 names: one with name blocked name.given, the other with blocked name.family
		 * isBlocked = false
		 *
		 * Block is on exact name.family. But since there are multiple
		 * name.family values, mdm is not blocked.
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"name": [{
				     	   "family": "doe",
				   			"given": [
				            	 "trixie"
				   			]
				 			}, {
				 				"family": "smith",
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
				.setFhirPath("name.family")
				.setBlockedValue("Doe");
			rule.addBlockListField()
				.setFhirPath("name.given")
				.setBlockedValue("Jane");
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					false
				)
			);
		}

		/*
		 * Block on Patient with EXACT match on name.family and ANY name.given
		 * Patient with 2 names: one with name blocked name.given (with no family name),
		 * 		the other with blocked on name.family (with no given name)
		 * isBlocked = true
		 *
		 * Blocking is on exact name.family and any name.given.
		 * There is only one name.family and it is blocked.
		 * There are multiple name.given, and one of them is blocked.
		 * This means that mdm will be blocked (even though the
		 * name.given is not in the same name object as the blocked name.family)
		 * Unintuitive, but corect.
		 */
		{
			String patientStr = """
				{
					"resourceType": "Patient",
					"name": [{
				     	   "family": "doe",
				     	   "given": [
				     	   	"yui"
				     	   ]
				 			}, {
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
				.setFhirPath("name.family")
				.setBlockedValue("Doe");
			rule.addBlockListField()
				.setFhirPath("name.given")
				.setBlockedValue("Jane")
				.setBlockRule(BlockFieldBlockRuleEnum.ANY);
			blockListJson.addBlockListRule(rule);
			data.add(
				new BlockRuleTestCase(
					blockListJson,
					parser.parseResource(Patient.class, patientStr),
					true
				)
			);
		}

		/*
		 * Code (Enum)
		 *
		 * Block on Patient with EXACT match on link.type.
		 * Patient with a link with blocked type
		 * isBlocked = true
		 *
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
				.setFhirPath("link.type")
				.setBlockedValue("seealso");
			blockListJson.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				blockListJson,
				parser.parseResource(patientStr),
				true
			));
		}

		/*
		 * CodableConcept
		 *
		 * Block on Patient with EXACT match on maritalStatus.coding.code
		 * Patient with a link with blocked maritalStatus
		 * isBlocked = true
		 *
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
				.setFhirPath("maritalStatus.coding.code")
				.setBlockedValue("m");
			blockListJson.addBlockListRule(rule);
			rule.addBlockListField()
				.setFhirPath("maritalStatus.coding.system")
				.setBlockedValue("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus");
			data.add(new BlockRuleTestCase(
				blockListJson,
				parser.parseResource(patientStr),
				true
			));
		}

		/*
		 * Boolean (trivial, but completions sake)
		 *
		 * Block on Patient with match on active
		 * Patient with blocked active value
		 * isBlocked = true
		 *
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
				json,
				parser.parseResource(patientStr),
				true
			));
		}

		/*
		 * String
		 *
		 * Block on EXACT name.family
		 * Patient with different name.famiy
		 * isBlocked = false
		 *
		 * Patient does not have blocked name.family,
		 * and so mdm is not blocked
		 */
		{
			String patientStr = """
    				{
    					"resourceType": "Patient",
    					"name": [{
    						"family": "smith"
    					}]
    				}
				""";
			BlockListJson json = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("name.family")
				.setBlockedValue("doe");
			json.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				json,
				parser.parseResource(patientStr),
				false
			));
		}

		/*
		 * String
		 *
		 * Block on ANY name.family
		 * Patient with no matching name.family
		 * isBlocked = false
		 *
		 * Patient does not have blocked name.family,
		 * and so mdm is not blocked
		 */
		{
			String patientStr = """
    				{
    					"resourceType": "Patient",
    					"name": [{
    						"family": "smith"
    					}]
    				}
				""";
			BlockListJson json = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("name.family")
				.setBlockedValue("doe")
				.setBlockRule(BlockFieldBlockRuleEnum.ANY);
			json.addBlockListRule(rule);
			data.add(new BlockRuleTestCase(
				json,
				parser.parseResource(patientStr),
				false
			));
		}

		return data;
	}

	@ParameterizedTest
	@MethodSource("getTestCases")
	public void isMdmMatchingBlocked_givenResourceAndRules_returnsExpectedValue(BlockRuleTestCase theTestCase) {
		// setup
		BlockListJson blockList = theTestCase.getBlockRule();
		IBaseResource patient = theTestCase.getPatientResource();
		boolean expected = theTestCase.isExpectedBlockResult();

		// when
		when(myRuleProvider.getBlocklistRules())
			.thenReturn(blockList);

		// test
		assertEquals(expected, myRuleEvaluationSvc.isMdmMatchingBlocked((IAnyResource) patient));
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

	@Test
	public void isMdmMatchingBlocked_unknownBlockRule_returnsTrue() {
		// setup
		Patient patient = new Patient();
		patient.addName()
			.addGiven("doe")
			.addGiven("jane");
		BlockListJson blockListJson = new BlockListJson();
		BlockListRuleJson rule = new BlockListRuleJson();
		rule.setResourceType("Patient");


		// when

	}
}
