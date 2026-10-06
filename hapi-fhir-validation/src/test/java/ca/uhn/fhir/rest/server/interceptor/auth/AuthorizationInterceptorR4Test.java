package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.auth.CompartmentSearchParameterModifications;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.GraphQL;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class AuthorizationInterceptorR4Test extends BaseValidationTestWithInlineMocks {
	private static final String DENIED_BY_DEFAULT_POLICY = "Access denied by default policy";
	private static final String DENIED_NO_APPLICABLE_RULES = DENIED_BY_DEFAULT_POLICY + " (no applicable rules)";
	private static final String ERR403 = "{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"processing\",\"diagnostics\":\"" + Msg.code(334) + DENIED_NO_APPLICABLE_RULES + "\"}]}";
	private static String ourConditionalCreateId;
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static boolean ourHitMethod;
	private static List<Resource> ourReturn;
	private static List<IBaseResource> ourDeleted;

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new DummyObservationResourceProvider())
		.registerProvider(new DummyOrganizationResourceProvider())
		.registerProvider(new DummyEncounterResourceProvider())
		.registerProvider(new DummyCarePlanResourceProvider())
		.registerProvider(new DummyDiagnosticReportResourceProvider())
		.registerProvider(new DummyDeviceResourceProvider())
		.registerProvider(new DummyGroupResourceProvider())
		.registerProvider(new DummyServiceRequestResourceProvider())
		.registerProvider(new DummyConsentResourceProvider())
		.registerProvider(new PlainProvider())
		.setDefaultResponseEncoding(EncodingEnum.JSON)
		.withPagingProvider(new FifoMemoryPagingProvider(10))
		.setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		ourServer.getInterceptorService().unregisterAllInterceptors();
		ourServer.getRestfulServer().setTenantIdentificationStrategy(null);
		ourReturn = null;
		ourDeleted = null;
		ourHitMethod = false;
		ourConditionalCreateId = "1123";
	}

	@AfterEach
	public void after() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);
	}

	private Resource createCarePlan(Integer theId, String theSubjectId) {
		CarePlan retVal = new CarePlan();
		if (theId != null) {
			retVal.setId(new IdType("CarePlan", (long) theId));
		}
		retVal.setSubject(new Reference("Patient/" + theSubjectId));
		return retVal;
	}

	private Resource createDiagnosticReport(Integer theId, String theSubjectId) {
		DiagnosticReport retVal = new DiagnosticReport();
		if (theId != null) {
			retVal.setId(new IdType("DiagnosticReport", (long) theId));
		}
		retVal.getCode().setText("OBS");
		retVal.setSubject(new Reference(theSubjectId));
		return retVal;
	}

	private Observation createObservation(Integer theId, String theSubjectId) {
		Observation retVal = new Observation();
		if (theId != null) {
			retVal.setId(new IdType("Observation", (long) theId));
		}
		retVal.getCode().setText("OBS");
		retVal.setSubject(new Reference(theSubjectId));

		if (theSubjectId != null && theSubjectId.startsWith("#")) {
			Patient p = new Patient();
			p.setId(theSubjectId);
			p.setActive(true);
			retVal.addContained(p);
		}

		return retVal;
	}

	private Organization createOrganization(int theIndex) {
		Organization retVal = new Organization();
		retVal.setId("" + theIndex);
		retVal.setName("Org " + theIndex);
		return retVal;
	}

	private Patient createPatient(Integer theId) {
		Patient retVal = new Patient();
		if (theId != null) {
			retVal.setId(new IdType("Patient", (long) theId));
		}
		retVal.addName().setFamily("FAM");
		return retVal;
	}

	private Patient createPatient(Integer theId, int theVersion) {
		Patient retVal = createPatient(theId);
		retVal.setId(retVal.getIdElement().withVersion(Integer.toString(theVersion)));
		return retVal;
	}

	private Bundle createTransactionWithPlaceholdersRequestBundle() {
		// Create a input that will be used as a transaction
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);

		String encounterId = "123-123";
		String encounterSystem = "http://our.internal.code.system/encounter";
		Encounter encounter = new Encounter();

		encounter.addIdentifier(new Identifier().setValue(encounterId)
			.setSystem(encounterSystem));

		encounter.setStatus(Encounter.EncounterStatus.FINISHED);

		Patient p = new Patient()
			.addIdentifier(new Identifier().setValue("321-321").setSystem("http://our.internal.code.system/patient"));
		p.setId(IdDt.newRandomUuid());

		// add patient to input so its created
		input.addEntry()
			.setFullUrl(p.getId())
			.setResource(p)
			.getRequest()
			.setUrl("Patient")
			.setMethod(Bundle.HTTPVerb.POST);

		Reference patientRef = new Reference(p.getId());

		encounter.setSubject(patientRef);
		Condition condition = new Condition()
			.setCode(new CodeableConcept().addCoding(
				new Coding("http://hl7.org/fhir/icd-10", "S53.40", "FOREARM SPRAIN / STRAIN")))
			.setSubject(patientRef);

		condition.setId(IdDt.newRandomUuid());

		// add condition to input so its created
		input.addEntry()
			.setFullUrl(condition.getId())
			.setResource(condition)
			.getRequest()
			.setUrl("Condition")
			.setMethod(Bundle.HTTPVerb.POST);

		Encounter.DiagnosisComponent dc = new Encounter.DiagnosisComponent();

		dc.setCondition(new Reference(condition.getId()));
		encounter.addDiagnosis(dc);
		CodeableConcept reason = new CodeableConcept();
		reason.setText("SLIPPED ON FLOOR,PAIN L) ELBOW");
		encounter.addReasonCode(reason);

		// add encounter to input so its created
		input.addEntry()
			.setResource(encounter)
			.getRequest()
			.setUrl("Encounter")
			.setIfNoneExist("identifier=" + encounterSystem + "|" + encounterId)
			.setMethod(Bundle.HTTPVerb.POST);
		return input;
	}

	private Bundle createTransactionWithPlaceholdersResponseBundle() {
		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry()
			.setResource(new Patient().setActive(true)) // don't give this an ID
			.getResponse().setLocation("/Patient/1");
		return output;
	}

	@Test
	public void testAllowAll() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.allowAll("Default Rule")
					.build();
			}
		});

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));

		ourServer.fhirRequest("/Observation/10").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Rule 1");
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$validate").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	/**
	 * A GET to the base URL isn't valid, but the interceptor should allow it
	 */
	@Test
	public void testGetRoot() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allowAll()
					.build();
			}
		});

		ourServer.fhirRequest("/").get().assertStatus(400);
	}

	@Test
	public void testAllowAllForTenant() {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType(Patient.class).withAnyId().forTenantIds("TENANTA").andThen()
					.allowAll("Default Rule")
					.build();
			}
		});

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/TENANTA/Observation/10").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/TENANTA/Patient/1").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Rule 1");
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/TENANTA/Patient/1/$validate").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testDeviceIsNativelyInPatientCompartmentForAuthorizationPurposes() {
		//Given
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				List<IdType> relatedIds = new ArrayList<>();
				relatedIds.add(new IdType("Patient/123"));
				return new RuleBuilder()
					.allow().read().allResources()
					.inCompartment("Patient", relatedIds)
					.andThen().denyAll()
					.build();
			}
		});

		Patient patient;
		patient = new Patient();
		patient.setId("Patient/123");
		Device d = new Device();
		d.getPatient().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(d);

		ourServer.fhirRequest("/Device/124456").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testCustomCompartmentSpsOnMultipleInstances() {
		//Given
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				List<IdType> relatedIds = new ArrayList<>();
				relatedIds.add(new IdType("Patient/123"));
				relatedIds.add(new IdType("Patient/456"));
				return new RuleBuilder()
					.allow().read().allResources()
					.inCompartment("Patient", relatedIds)
					.andThen().denyAll()
					.build();
			}
		});

		Patient patient;
		patient = new Patient();
		patient.setId("Patient/123");
		Device d = new Device();
		d.getPatient().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(d);

		ourServer.fhirRequest("/Device/124456").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void rules_withSPLimitations_works() {
		// setup
		String patientId = "Patient/123";
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				CompartmentSearchParameterModifications specialCases = new CompartmentSearchParameterModifications();
				specialCases.addSPToOmitFromCompartment("group", "member");
				List<IdType> relatedIds = new ArrayList<>();
				relatedIds.add(new IdType(patientId));
				return new RuleBuilder()
					.allow().read().allResources()
					.inModifiedCompartment("Patient", relatedIds, specialCases)
					.andThen().denyAll()
					.build();
			}
		});

		Patient patient = new Patient();
		patient.setId(patientId);

		Group group = new Group();
		group.addMember()
			.setEntity(new Reference(patientId));

		ourHitMethod = false;
		ourReturn = Collections.singletonList(patient);

		String urlToTest = "/Group?member.entity=" + patientId;
		ourServer.fhirRequest(urlToTest).get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testNonsenseParametersThrowAtRuntime() {
		//Given
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				CompartmentSearchParameterModifications compartmentSearchParameterModifications = new CompartmentSearchParameterModifications();
				compartmentSearchParameterModifications.addSPToIncludeInCompartment("device", "garbage");
				List<IdType> relatedIds = new ArrayList<>();
				relatedIds.add(new IdType("Patient/123"));
				return new RuleBuilder()
					.allow().read().allResources()
					.inModifiedCompartment("Patient", relatedIds, compartmentSearchParameterModifications)
					.andThen().denyAll()
					.build();
			}
		});

		Patient patient;
		patient = new Patient();
		patient.setId("Patient/123");
		Device d = new Device();
		d.getPatient().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(d);

		ourServer.fhirRequest("/Device/").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testAllowByCompartmentUsingUnqualifiedIds() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().allow()
					.read().resourcesOfType(CarePlan.class)
					.inCompartment("Patient", new IdType("Patient/123"))
					.andThen().denyAll()
					.build();
			}
		});

		Patient patient;
		CarePlan carePlan;

		// Unqualified
		patient = new Patient();
		patient.setId("123");
		carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(carePlan);
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Qualified
		patient = new Patient();
		patient.setId("Patient/123");
		carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(carePlan);
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Wrong one
		patient = new Patient();
		patient.setId("456");
		carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setResource(patient);

		ourHitMethod = false;
		ourReturn = Collections.singletonList(carePlan);
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();


		patient = new Patient();
		patient.setId("Patient/123");
		carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setResource(patient);
	}

	/**
	 * #528
	 */
	@Test
	public void testAllowByCompartmentWithAnyType() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a"))
					.andThen().denyAll()
					.build();
			}
		});

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testAllowByCompartmentWithAnyTypeWithTenantId() {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).forTenantIds("TENANTA")
					.andThen().denyAll()
					.build();
			}
		});

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		ourServer.fhirRequest("/TENANTA/CarePlan/135154").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		ourServer.fhirRequest("/TENANTA/CarePlan/135154").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

	}

	/**
	 * #528
	 */
	@Test
	public void testAllowByCompartmentWithType() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().allow("Rule 1").read().resourcesOfType(CarePlan.class).inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().denyAll()
					.build();
			}
		});


		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testBatchWhenOnlyTransactionAllowed() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.BATCH);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().getResponse().setLocation("/Patient/1");

		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		ourServer.fhirRequest("/").post(input).assertStatus(200);
	}

	@Test
	public void testBatchWhenTransactionReadDenied() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.BATCH);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().setResource(createPatient(2));

		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		ourServer.fhirRequest("/").post(input).assertStatus(403);
	}

	@Test
	public void testCodeIn_Search_BanList() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType("Observation").withCodeInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.allowAll()
					.build();
			}
		});

		Observation observation;

		// Banned code present
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Rule 1");
		assertThat(ourHitMethod).isTrue();

		// Acceptable code present
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Both Unacceptable and Acceptable code present
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Rule 1");
		assertThat(ourHitMethod).isTrue();

	}


	@Test
	public void testCodeIn_Search_AllowList() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType("Observation").withCodeInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.build();
			}
		});

		Observation observation;
		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// No acceptable code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isTrue();

		// Both Unacceptable and Acceptable code present
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Allowed code present - Search
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// No acceptable code present - Search
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isTrue();

	}


	@Test
	public void testCodeNotIn_AllowSearch() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType("Observation").withCodeNotInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.build();
			}
		});

		Observation observation;
		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isTrue();

		// No acceptable code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Both Unacceptable and Acceptable code present - Should not pass since one of the codes is in the VS
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testCodeNotIn_DenySearch() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType("Observation").withCodeNotInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.allowAll()
					.build();
			}
		});

		Observation observation;
		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// No acceptable code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Rule 1");
		assertThat(ourHitMethod).isTrue();

		// Both Unacceptable and Acceptable code present - Should not pass since one of the codes is in the VS
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("foo");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

		// No acceptable codesystem present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://blah")
			.setCode("foo");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Rule 1");
		assertThat(ourHitMethod).isTrue();

	}

	/**
	 * Even if everything is allow, let's be safe and deny if the ValueSet can't be validated at all
	 */
	@Test
	public void testCodeNotIn_DenySearch_UnableToValidateValueSet() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType("Observation").withCodeNotInValueSet("code", "http://foo").andThen()
					.allowAll()
					.build();
			}
		});

		Observation observation;
		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();
	}


	@Test
	public void testCodeIn_TransactionCreate() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().transaction().withAnyOperation().andApplyNormalRules().andThen()
					.deny("Rule 1").write().resourcesOfType("Observation").withCodeInValueSet("code", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.allowAll()
					.build();
			}
		});

		Observation observation;

		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input
			.addEntry()
			.setResource(observation)
			.getRequest()
			.setUrl("/Observation")
			.setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().setResource(createPatient(1));

		// Transaction with resource containing banned code
		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		ourServer.fhirRequest("/").post(input)
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Rule 1");

		// Transaction with resource containing acceptable code
		observation.getCode().getCoding().clear();
		observation.getCode().addCoding().setSystem("http://foo").setCode("bar");
		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		ourServer.fhirRequest("/").post(input).assertStatus(200);
	}

	@Test
	public void testCodeIn_InvalidSearchParam() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType("Observation").withCodeInValueSet("blah", "http://hl7.org/fhir/ValueSet/administrative-gender").andThen()
					.build();
			}
		});

		Observation observation;
		// Allowed code present - Read
		ourHitMethod = false;
		observation = createObservation(10, "Patient/2");
		observation
			.getCode()
			.addCoding()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.setCode("male");
		ourReturn = Collections.singletonList(observation);
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(500)
			.assertBodyContains("HAPI-2025: Unknown SearchParameter for resource Observation: blah");
		assertThat(ourHitMethod).isTrue();
	}


	@Test
	public void testBatchWhenTransactionWrongBundleType() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.COLLECTION);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.POST);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().setResource(createPatient(1));

		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		ourServer.fhirRequest("/").post(input).assertStatus(422);
	}

	@Test
	public void testDeleteInCompartmentWithO() {
		// setup
		String patientId = "Patient/123";
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				CompartmentSearchParameterModifications specialCases = new CompartmentSearchParameterModifications();
				specialCases.addSPToOmitFromCompartment("group", "member");
				List<IdType> relatedIds = new ArrayList<>();
				relatedIds.add(new IdType(patientId));
				return new RuleBuilder()
					.allow().delete().allResources()
					.inModifiedCompartment("Patient", relatedIds, specialCases)
					.andThen().denyAll()
					.build();
			}
		});

		createPatient(123);
		Group group = new Group();
		group.setId("Group/456");
		group.addMember()
			.setEntity(new Reference(patientId));
		ourDeleted = List.of(group);
		ourHitMethod = false;

		ourServer.fhirRequest("/Group?member=" + patientId).delete().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testDeleteByCompartment() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").delete().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/2").delete().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(1));
		ourServer.fhirRequest("/Patient/1").delete().assertStatus(204);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testDeleteByCompartmentUsingTransaction() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").delete().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow().transaction().withAnyOperation().andApplyNormalRules().andThen()
					.build();
			}
		});

		Bundle responseBundle = new Bundle();
		responseBundle.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		ourHitMethod = false;
		bundle.getEntry().clear();
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createPatient(2));
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Patient/2");

		ourServer.fhirRequest("/").post(bundle).assertStatus(403);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		bundle.getEntry().clear();
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Patient/1");
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createPatient(1));

		ourServer.fhirRequest("/").post(bundle).assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		bundle.getEntry().clear();
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Observation?subject=Patient/2");
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createObservation(99, "Patient/2"));

		ourServer.fhirRequest("/").post(bundle).assertStatus(403);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		bundle.getEntry().clear();
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.DELETE).setUrl("Observation?subject=Patient/1");
		ourReturn = Collections.singletonList(responseBundle);
		ourDeleted = Collections.singletonList(createObservation(99, "Patient/1"));

		ourServer.fhirRequest("/").post(bundle).assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testDeleteByType() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).withAnyId().andThen()
					.build();
			}
		});

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));

		ourServer.fhirRequest("/Patient/1").delete().assertStatus(204);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));

		ourServer.fhirRequest("/Observation/1").delete().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	/**
	 * #528
	 */
	@Test
	public void testDenyActionsNotOnTenant() {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().denyAll().notForTenantIds("TENANTA", "TENANTB").build();
			}
		});


		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/TENANTA/Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/TENANTC/Patient/1").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: (unnamed rule)");
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testDenyAll() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("Default Rule")
					.build();
			}
		});

	ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Default Rule");
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$validate").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Default Rule");
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$opName").get()
			.assertStatus(403)
			.assertBodyContains("Access denied by rule: Default Rule");
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testDenyAllByDefault() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
					.build();
			}
		});

	ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$validate").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();
	}

	/**
	 * #528
	 */
	@Test
	public void testDenyByCompartmentWithAnyType() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().deny("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().allowAll().build();
			}
		});



		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

	}

	/**
	 * #528
	 */
	@Test
	public void testDenyByCompartmentWithType() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder().deny("Rule 1").read().resourcesOfType(CarePlan.class).inCompartment("Patient", new IdType("Patient/845bd9f1-3635-4866-a6c8-1ca085df5c1a")).andThen().allowAll()
					.build();
			}
		});



		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "845bd9f1-3635-4866-a6c8-1ca085df5c1a"));
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createCarePlan(10, "FOO"));
		ourServer.fhirRequest("/CarePlan/135154").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testHistoryWithReadAll() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().withAnyId()
					.build();
			}
		});



		ourReturn = Collections.singletonList(createPatient(2, 1));

		ourHitMethod = false;
		ourServer.fhirRequest("/_history").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/_history").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/_history").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testInvalidInstanceIds() {
		try {
			new RuleBuilder().allow("Rule 1").write().instance((String) null);
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance("");
			fail();		} catch (IllegalArgumentException e) {
			assertEquals("theId must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance("Observation/");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theId must contain an ID part", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType());
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId.getValue() must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType(""));
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId.getValue() must not be null or empty", e.getMessage());
		}
		try {
			new RuleBuilder().allow("Rule 1").write().instance(new IdType("Observation", (String) null));
			fail();
		} catch (NullPointerException e) {
			assertEquals("theId must contain an ID part", e.getMessage());
		}
	}

	@Test
	public void testMetadataAllow() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").metadata()
					.build();
			}
		});



		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/metadata").get().assertStatus(200);
	}

	@Test
	public void testMetadataDeny() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.deny("Rule 1").metadata()
					.build();
			}
		});



		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/metadata").get().assertStatus(403);
	}

	@Test
	public void testOperationAnyName() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().withAnyName().onServer().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});



		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));

		ourServer.fhirRequest("/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testOperationAppliesAtAnyLevel() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").atAnyLevel().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Instance Version
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/_history/2/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testOperationAppliesAtAnyLevelWrongOpName() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opNameBadOp").atAnyLevel().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/$opName").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/$opName").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/$opName").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		// Instance Version
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/_history/2/$opName").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testOperationByInstanceOfTypeAllowed() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andRequireExplicitResponseAuthorization()
					.build();
			}
		});


		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$everything").get()
			.assertStatus(200)
			.assertBodyContains("Bundle");
		assertThat(ourHitMethod).isTrue();

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		ourServer.fhirRequest("/Encounter/1/$everything").get()
			.assertStatus(403)
			.assertBodyContains("OperationOutcome");
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testOperationByInstanceOfTypeWithInvalidReturnValue() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andRequireExplicitResponseAuthorization().andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});


		// With a return value we don't allow
		ourReturn = Collections.singletonList(createPatient(222));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$everything").get()
			.assertStatus(403)
			.assertBodyContains("OperationOutcome");
		assertThat(ourHitMethod).isTrue();

		// With a return value we do
		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$everything").get()
			.assertStatus(200)
			.assertBodyContains("Bundle");
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testOperationByInstanceOfTypeWithReturnValue() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andRequireExplicitResponseAuthorization()
					.build();
			}
		});


		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$everything").get()
			.assertStatus(200)
			.assertBodyContains("Bundle");
		assertThat(ourHitMethod).isTrue();

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		ourServer.fhirRequest("/Encounter/1/$everything").get()
			.assertStatus(403)
			.assertBodyContains("OperationOutcome");
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testOperationInstanceLevel() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onInstance(new IdType("http://example.com/Patient/1/_history/2")).andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});


		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Wrong instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/2/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testOperationInstanceLevelAnyInstance() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onAnyInstance().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Another Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Observation/2/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/2/$opName2").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testOperationNotAllowedWithWritePermissiom() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").write().allResources().withAnyId().andThen()
					.build();
			}
		});


		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// System
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/$opName").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/$opName").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/123/$opName").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testOperationServerLevel() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onServer().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testOperationTypeLevel() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onType(Patient.class).andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

		// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Wrong type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Observation/1/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/$opName2").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testOperationTypeLevelWildcard() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onAnyType().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Another type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Observation/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Wrong name
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/$opName2").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/Patient/1/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testOperationTypeLevelWithOperationMethodHavingOptionalIdParam() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onType(Organization.class).andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});

// Server
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Organization/2"));
		ourServer.fhirRequest("/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createOrganization(2));
		ourServer.fhirRequest("/Organization/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Wrong type
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createOrganization(2));
		ourServer.fhirRequest("/Observation/1/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();

		// Instance
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createOrganization(2));
		ourServer.fhirRequest("/Organization/1/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testOperationTypeLevelWithTenant() {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("opName").onType(Patient.class).andRequireExplicitResponseAuthorization().forTenantIds("TENANTA").andThen()
					.build();
			}
		});

// Right Tenant
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));
		ourServer.fhirRequest("/TENANTA/Patient/$opName").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Wrong Tenant
		ourHitMethod = false;
		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourServer.fhirRequest("/TENANTC/Patient/$opName").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_BY_DEFAULT_POLICY);
		assertThat(ourHitMethod).isFalse();
	}


	@Test
	public void testOperationTypeLevelDifferentBodyType() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("RULE 1").operation().named("process-message").onServer().andRequireExplicitResponseAuthorization().andThen()
					.build();
			}
		});


		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.MESSAGE);

		// With body
		ourHitMethod = false;
		ourServer.fhirRequest("/$process-message").post(input).assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// With body
		ourHitMethod = false;
		ourServer.fhirRequest("/$process-message").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testOperationWithTester() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").operation().named("everything").onInstancesOfType(Patient.class).andRequireExplicitResponseAuthorization().withTester(null /* null should be ignored */).withTester(new IAuthRuleTester() {
						@Override
						public boolean matches(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IIdType theInputResourceId, IBaseResource theInputResource) {
							return theInputResourceId.getIdPart().equals("1");
						}
					})
					.build();
			}
		});

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$everything").get()
			.assertStatus(200)
			.assertBodyContains("Bundle");
		assertThat(ourHitMethod).isTrue();

		ourReturn = new ArrayList<>();
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/2/$everything").get()
			.assertStatus(403)
			.assertBodyContains("OperationOutcome");
		assertThat(ourHitMethod).isFalse();
	}

	// This test is of dubious value since it does NOT exercise DAO code.  It simply exercises the AuthorizationInterceptor.
	// In functional testing or with a more realistic integration test, this scenario, namely having ONLY a FHIR_PATCH
	// role, will result in a failure to update the resource.
	@Test
	public void testPatchAllowed() {
		Observation obs = new Observation();
		obs.setSubject(new Reference("Patient/999"));

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().patch().allRequests().andThen()
					.build();
			}
		});

		String patchBody = "[\n" +
			"     { \"op\": \"replace\", \"path\": \"Observation/status\", \"value\": \"amended\" }\n" +
			"     ]";
		ourServer.fhirRequest("/Observation/123").patch(patchBody).assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testPatchNotAllowed() {
		Observation obs = new Observation();
		obs.setSubject(new Reference("Patient/999"));

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().metadata().andThen()
					.build();
			}
		});

		String patchBody = "[\n" +
			"     { \"op\": \"replace\", \"path\": \"Observation/status\", \"value\": \"amended\" }\n" +
			"     ]";
		ourServer.fhirRequest("/Observation/123").patch(patchBody).assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testGraphQLAllowed() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").graphQL().any().andThen()
					.build();
			}
		});




		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$graphql?query=" + UrlUtil.escapeUrlParam("{name}")).get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testGraphQLDenied() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.build();
			}
		});




		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/$graphql?query=" + UrlUtil.escapeUrlParam("{name}")).get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testReadByAnyId() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).withAnyId()
					.build();
			}
		});

ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/_history/222").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isFalse();

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testReadByAnyIdWithTenantId() {
		ourServer.getRestfulServer().setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).withAnyId().forTenantIds("TENANTA")
					.build();
			}
		});

ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/TENANTA/Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/TENANTB/Patient/1").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isFalse();

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/TENANTA/Patient/1/_history/222").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/TENANTA/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isFalse();

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/TENANTA/Patient").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/TENANTA/Patient").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testReadByAnyIdWithTester() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).withAnyId().withTester(new IAuthRuleTester() {
						@Override
						public boolean matches(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IIdType theInputResourceId, IBaseResource theInputResource) {
							return theInputResourceId != null && theInputResourceId.getIdPart().equals("1");
						}
					})
					.build();
			}
		});

ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1/_history/222").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/10").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isFalse();

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isFalse();

	}


	@Test
	public void testReadByTypeWithAnyId() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(ServiceRequest.class).withAnyId().andThen()
					.build();
			}
		});

		ourReturn = Collections.singletonList(new Consent().setDateTime(new Date()).setId("Consent/123"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Consent").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourReturn = Collections.singletonList(new ServiceRequest().setAuthoredOn(new Date()).setId("ServiceRequest/123"));
		ourHitMethod = false;
		ourServer.fhirRequest("/ServiceRequest").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}


	@Test
	public void testReadByCompartmentReadByIdParam() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});




		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?_id=Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?_id=1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?_id=Patient/2").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testReadByCompartmentReadByPatientParam() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});




		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/DiagnosticReport?patient=Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/DiagnosticReport?patient=1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/DiagnosticReport?patient=Patient/2").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/DiagnosticReport?subject=Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/DiagnosticReport?subject=1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createDiagnosticReport(1, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/DiagnosticReport?subject=Patient/2").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testReadByCompartmentRight() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});




		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createObservation(10, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testReadByCompartmentWrongAllTypesProactiveBlockEnabledNoResponse() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		}.setFlags());

ourReturn = Collections.emptyList();
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/2").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").get().assertStatus(404);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/CarePlan/10").get().assertStatus(404);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/_history").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/_history").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/999/_history").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testReadByCompartmentWrongProactiveBlockDisabled() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags(AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS));

ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/2").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isFalse();

		ourReturn = Collections.singletonList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createCarePlan(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/CarePlan/10").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isFalse();

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient").get()
			.assertStatus(403)
			.assertBodyContains(DENIED_NO_APPLICABLE_RULES);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testReadByCompartmentWrongProactiveBlockDisabledNoResponse() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags(AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS));

ourReturn = Collections.emptyList();
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/2").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").get().assertStatus(404);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/CarePlan/10").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testReadByCompartmentWrongProactiveBlockEnabledNoResponse() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags());

ourReturn = Collections.emptyList();
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/2").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").get().assertStatus(404);
		assertThat(ourHitMethod).isTrue();

		// CarePlan could potentially be in the Patient/1 compartment but we don't
		// have any rules explicitly allowing CarePlan so it's blocked
		ourHitMethod = false;
		ourServer.fhirRequest("/CarePlan/10").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/_history").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/_history").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/999/_history").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testReadByCompartmentDoesntAllowContained() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		}.setFlags());

// Read with allowed subject
		ourReturn = Lists.newArrayList(createObservation(10, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Read with contained
		ourReturn = Lists.newArrayList(createObservation(10, "#1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

		// Read with contained
		Observation obs = createObservation(10, null);
		obs.setSubject(new Reference(new Patient().setActive(true)));
		ourReturn = Lists.newArrayList(obs);
		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").get().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testReadByInstance() {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().instance("Observation/900").andThen()
					.allow("Rule 1").read().instance("901").andThen()
					.build();
			}
		});

		ourReturn = Collections.singletonList(createObservation(900, "Patient/1"));
		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/900").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createPatient(901));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/901").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1?_format=json").get()
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testReadByInstanceAllowsTargetedSearch() {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				RuleBuilder ruleBuilder = new RuleBuilder();
				ruleBuilder.allow().read().instance("Patient/900").andThen();
				ruleBuilder.allow().read().instance("Patient/700").andThen();
				return ruleBuilder.build();
			}
		});



		ourReturn = Collections.singletonList(createPatient(900));

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?_id=900").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?_id=Patient/900").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?_id=901").get()
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?_id=Patient/901").get()
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		// technically this is invalid, but just in case...
		ourServer.fhirRequest("/Observation?_id=Patient/901").get()
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation?_id=901").get()
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?_id=Patient/900,Patient/700").get().assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?_id=900,777").get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testReadPageRight() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		Bundle respBundle;

		ourReturn = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			ourReturn.add(createObservation(i, "Patient/1"));
		}

		ourHitMethod = false;
		HttpTestResponse response = ourServer.fhirRequest("/Observation?_count=5&_format=json&subject=Patient/1").get();
		response.assertStatus(200);
		assertThat(ourHitMethod).isTrue();
		String responseBody = response.getBody();
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseBody);
		assertThat(respBundle.getEntry()).hasSize(5);
		assertEquals(10, respBundle.getTotal());
		assertEquals("Observation/0", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNotNull(respBundle.getLink("next"));

		// Load next page

		ourHitMethod = false;
		String nextUrl = respBundle.getLink("next").getUrl().replace(ourServer.getBaseUrl(), "");
		HttpTestResponse responseNext = ourServer.fhirRequest(nextUrl).get();
		responseNext.assertStatus(200);
		String responseBodyNext = responseNext.getBody();
		assertThat(ourHitMethod).isFalse();
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseBodyNext);
		assertThat(respBundle.getEntry()).hasSize(5);
		assertEquals(10, respBundle.getTotal());
		assertEquals("Observation/5", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNull(respBundle.getLink("next"));

	}

	@Test
	public void testReadPageWrong() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		Bundle respBundle;

		ourReturn = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			ourReturn.add(createObservation(i, "Patient/1"));
		}
		for (int i = 5; i < 10; i++) {
			ourReturn.add(createObservation(i, "Patient/2"));
		}

		ourHitMethod = false;
		HttpTestResponse response = ourServer.fhirRequest("/Observation?_count=5&_format=json&subject=Patient/1").get();
		response.assertStatus(200);
		assertThat(ourHitMethod).isTrue();
		respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, response.getBody());
		assertThat(respBundle.getEntry()).hasSize(5);
		assertEquals(10, respBundle.getTotal());
		assertEquals("Observation/0", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
		assertNotNull(respBundle.getLink("next"));

		// Load next page
		ourHitMethod = false;
		String nextUrl = respBundle.getLink("next").getUrl().replace(ourServer.getBaseUrl(), "");
		ourServer.fhirRequest(nextUrl).get().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testTransactionWithSearch() {

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("read patient").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});

		// Request is a transaction with 1 search
		Bundle requestBundle = new Bundle();
		requestBundle.setType(Bundle.BundleType.TRANSACTION);
		String patientId = "10000003857";
		Bundle.BundleEntryComponent bundleEntryComponent = requestBundle.addEntry();
		Bundle.BundleEntryRequestComponent bundleEntryRequestComponent = new Bundle.BundleEntryRequestComponent();
		bundleEntryRequestComponent.setMethod(Bundle.HTTPVerb.GET);
		bundleEntryRequestComponent.setUrl(ResourceType.Patient + "?identifier=" + patientId);
		bundleEntryComponent.setRequest(bundleEntryRequestComponent);

		/*
		 * Response is a transaction response containing the search results
		 */
		Bundle searchResponseBundle = new Bundle();
		Patient patent = new Patient();
		patent.setActive(true);
		patent.setId("Patient/123");
		searchResponseBundle.addEntry().setResource(patent);

		Bundle responseBundle = new Bundle();
		responseBundle
			.addEntry()
			.setResource(searchResponseBundle);
		ourReturn = Collections.singletonList(responseBundle);

		ourServer.fhirRequest("/").post(requestBundle).assertStatus(200);

	}

	@Test
	public void testTransactionWithNoBundleType() {

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("read patient").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});

		// Request is a transaction with 1 search
		Bundle requestBundle = new Bundle();
		String patientId = "10000003857";
		Bundle.BundleEntryComponent bundleEntryComponent = requestBundle.addEntry();
		Bundle.BundleEntryRequestComponent bundleEntryRequestComponent = new Bundle.BundleEntryRequestComponent();
		bundleEntryRequestComponent.setMethod(Bundle.HTTPVerb.GET);
		bundleEntryRequestComponent.setUrl(ResourceType.Patient + "?identifier=" + patientId);
		bundleEntryComponent.setRequest(bundleEntryRequestComponent);

		ourServer.fhirRequest("/").post(requestBundle)
			.assertStatus(422)
			.assertBodyContains("Invalid request Bundle.type value for transaction: \\\"\\\"");
	}

	/**
	 * See #762
	 */
	@Test
	public void testTransactionWithPlaceholderIdsResponseAuthorized() {

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("read patient").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.allow("write patient").write().resourcesOfType(Patient.class).withAnyId().andThen()
					.allow("write encounter").write().resourcesOfType(Encounter.class).withAnyId().andThen()
					.allow("write condition").write().resourcesOfType(Condition.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});

		Bundle input = createTransactionWithPlaceholdersRequestBundle();
		Bundle output = createTransactionWithPlaceholdersResponseBundle();

		ourReturn = Collections.singletonList(output);
		ourServer.fhirRequest("/").post(input).assertStatus(200);
	}

	/**
	 * See #762
	 */
	@Test
	public void testTransactionWithPlaceholderIdsResponseUnauthorized() {

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("write patient").write().resourcesOfType(Patient.class).withAnyId().andThen()
					.allow("write encounter").write().resourcesOfType(Encounter.class).withAnyId().andThen()
					.allow("write condition").write().resourcesOfType(Condition.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});

		Bundle input = createTransactionWithPlaceholdersRequestBundle();
		Bundle output = createTransactionWithPlaceholdersResponseBundle();

		ourReturn = Collections.singletonList(output);
		ourServer.fhirRequest("/").post(input).assertStatus(403);
	}

	@Test
	public void testTransactionWriteGood() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdType("Patient/1")).andThen()
					.build();
			}
		});

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient").setMethod(Bundle.HTTPVerb.PUT);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		output.addEntry().getResponse().setLocation("/Patient/1");



		ourReturn = Collections.singletonList(output);
		ourHitMethod = false;
		ourServer.fhirRequest("/").post(input).assertStatus(200);
	}

	@Test
	void transactionWithPatchOnExistingPatient_writeOnlyPermissions_returnsForbidden() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("write patient").write().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("deny all")
					.build();
			}
		});
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry().getRequest().setUrl("Patient/1").setMethod(Bundle.HTTPVerb.PATCH);

		Bundle output = new Bundle();
		output.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		Patient echoedPatient = new Patient();
		echoedPatient.setActive(true);
		echoedPatient.addIdentifier().setValue("SECRET-MRN");
		output.addEntry().setResource(echoedPatient).getResponse().setLocation("/Patient/1");

		ourReturn = Collections.singletonList(output);
		// a transaction response must not disclose an embedded resource the caller cannot read
		ourServer.fhirRequest("/").post(input)
			.assertStatus(403)
			.assertBodyDoesNotContain("SECRET-MRN");
	}

		@Test
	public void testWriteByCompartmentCreate() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 1b").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1123")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		ourServer.fhirRequest("/Patient").post(createPatient(null))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		// Conditional
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient")
			.withHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?foo=bar")
			.post(createPatient(null))
				.assertStatus(403)
				.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation")
			.post(createObservation(null, "Patient/2"))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation").post(createObservation(null, "Patient/1")).assertStatus(201);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testWriteByCompartmentCreateConditionalResolvesToValid() {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").createConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient")
			.withHeader(Constants.HEADER_IF_NONE_EXIST, "foo=bar")
			.post(createPatient(null))
			.assertStatus(201);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testWriteByCompartmentDeleteConditionalResolvesToValid() {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").delete().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").deleteConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		ourReturn = Collections.singletonList(createPatient(1));

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?foo=bar").delete().assertStatus(204);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testWriteByCompartmentDeleteConditionalWithoutDirectMatch() {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 2").deleteConditional().resourcesOfType(Patient.class).andThen()
					.allow().delete().instance(new IdType("Patient/2")).andThen()
					.build();
			}
		});

		// Wrong resource
		ourReturn = Collections.singletonList(createPatient(1));
		ourHitMethod = false;

		ourServer.fhirRequest("/Patient?foo=bar").delete().assertStatus(403);
		assertThat(ourHitMethod).isTrue();

		// Right resource
		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;

		ourServer.fhirRequest("/Patient?foo=bar").delete().assertStatus(204);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testWriteByCompartmentDoesntAllowDelete() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(2));

		ourServer.fhirRequest("/Patient/2").delete().assertStatus(403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourReturn = Collections.singletonList(createPatient(1));

		ourServer.fhirRequest("/Patient/1").delete().assertStatus(403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testWriteByCompartmentUpdate() {
		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1"))
					.build();
			}
		});

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/2").put(createPatient(2))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/1").put(createPatient(1)).assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		// Conditional
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?foo=bar").put(createPatient(null))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		// this case simulates the situation where the user provided id matches the rules but the actual resolution of
		// the conditional url matched to another resource. As a result, the operation is allowed at the
		// SERVER_INCOMING_REQUEST_PRE_HANDLED pointcut but denied at STORAGE_PRESTORAGE_RESOURCE_CREATED.
		// Note that in real DAO, this would be caught earlier with HAPI-2279; however, even if it does not, the
		// AuthorizationInterceptor can still catch it.
		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?foo=bar").put(createPatient(1))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?foo=bar").put(createPatient(99))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").put(createObservation(10, "Patient/1")).assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/10").put(createObservation(10, "Patient/2"))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();
	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToInvalid() {
		ourConditionalCreateId = "1123";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?foo=bar").put(createPatient(null))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isTrue();

	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToValid() {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().resourcesOfType(Patient.class)
					.build();
			}
		});

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?foo=bar").put(createPatient(null)).assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation?foo=bar").put(createObservation(null, "Patient/12"))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testWriteByCompartmentUpdateConditionalResolvesToValidAllTypes() {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdType("Patient/1")).andThen()
					.allow("Rule 3").updateConditional().allResources()
					.build();
			}
		});

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient?foo=bar").put(createPatient(null)).assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation?foo=bar").put(createObservation(null, "Patient/12"))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testWriteByInstance() {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").write().instance("Observation/900").andThen()
					.allow("Rule 1").write().instance("901").andThen()
					.build();
			}
		});

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/900").put(createObservation(900, "Patient/12")).assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation/901").put(createObservation(901, "Patient/12")).assertStatus(200);
		assertThat(ourHitMethod).isTrue();

		ourHitMethod = false;
		ourServer.fhirRequest("/Observation")
			.post(createObservation(null, "Patient/900"))
			.assertStatus(403)
			.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient")
			.post(createPatient(null))
				.assertStatus(403)
				.assertBodyEquals(ERR403);
		assertThat(ourHitMethod).isFalse();

	}

	@Test
	public void testWritePatchByInstance() {
		ourConditionalCreateId = "1";

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("Rule 1").patch().allRequests().andThen()
					.allow("Rule 1").write().instance("Patient/900").andThen()
					.build();
			}
		});

		String input = "[ { \"op\": \"replace\", \"path\": \"/gender\", \"value\": \"male\" }  ]";

		ourHitMethod = false;
		ourServer.fhirRequest("/Patient/900").patch(input).assertStatus(200);
		assertThat(ourHitMethod).isTrue();
	}

	@Test
	public void testToListOfResourcesAndExcludeContainer_withSearchSetContainingDocumentBundles_onlyRecursesOneLevelDeep() {
		Patient patient = createPatient(1);
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(new Composition());
		bundle.addEntry().setResource(patient);
		Bundle searchSet = new Bundle();
		searchSet.setType(Bundle.BundleType.SEARCHSET);
		searchSet.addEntry().setResource(bundle);

		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setResourceName("Bundle");

		List<IBaseResource> resources = AuthorizationInterceptor.toListOfResourcesAndExcludeContainerUnlessStandalone(searchSet, ourCtx, requestDetails);
		assertEquals(1, resources.size());
		assertTrue(resources.contains(bundle));
	}

	@Test
	public void testToListOfResourcesAndExcludeContainer_withSearchSetContainingPatients_returnsPatients() {
		Patient patient1 = createPatient(1);
		Patient patient2 = createPatient(2);
		Bundle searchSet = new Bundle();
		searchSet.setType(Bundle.BundleType.SEARCHSET);
		searchSet.addEntry().setResource(patient1);
		searchSet.addEntry().setResource(patient2);

		RequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setResourceName("Patient");

		List<IBaseResource> resources = AuthorizationInterceptor.toListOfResourcesAndExcludeContainerUnlessStandalone(searchSet, ourCtx, requestDetails);
		assertEquals(2, resources.size());
		assertTrue(resources.contains(patient1));
		assertTrue(resources.contains(patient2));
	}

	@ParameterizedTest
	@MethodSource("provideArgumentsForToListOfResourcesAndExcludeContainerUnlessStandalone")
	public void givenAsearchRequestWithOperationOutcomeIntheResponse_whenToListOfResourcesAndExcludeContainerUnlessStandalone_thenReturnNoOperationOutcome(
		IBaseResource theResource, RequestDetails theRequestDetails, int theExpectedListSize
	) {
		List<IBaseResource> resources = AuthorizationInterceptor.toListOfResourcesAndExcludeContainerUnlessStandalone(theResource, ourCtx, theRequestDetails);
		assertEquals(theExpectedListSize, resources.size());
	}

	private static Stream<Arguments> provideArgumentsForToListOfResourcesAndExcludeContainerUnlessStandalone() {
		Stream.Builder<Arguments> retVal = Stream.builder();
	AuthorizationInterceptor.REST_OPERATIONS_TO_EXCLUDE_SECURITY_FOR_OPERATION_OUTCOME.forEach((restOperationType)->{
		RequestDetails firstRequestDetails = new SystemRequestDetails();
		RequestDetails secondRequestDetails = new SystemRequestDetails();
		firstRequestDetails.setResourceName("Patient");
		firstRequestDetails.setRestOperationType(restOperationType);
		secondRequestDetails.setResourceName("OperationOutcome");
		secondRequestDetails.setRestOperationType(restOperationType);

		OperationOutcome firstResponse = new OperationOutcome();
		OperationOutcome secondResponse = new OperationOutcome();
		firstResponse.addIssue().
			setSeverity(OperationOutcome.IssueSeverity.INFORMATION).
			setCode(OperationOutcome.IssueType.INFORMATIONAL);
		secondResponse.addIssue().
			setSeverity(OperationOutcome.IssueSeverity.ERROR)
			.setCode(OperationOutcome.IssueType.CODEINVALID);

		retVal.add(Arguments.of(firstResponse, firstRequestDetails, 0));
		retVal.add(Arguments.of(secondResponse, secondRequestDetails, 1));
	});
		return retVal.build();
	}

	@ParameterizedTest
	@EnumSource(value = Bundle.BundleType.class, names = {"DOCUMENT", "MESSAGE", "COLLECTION"})
	public void testShouldExamineBundleResources_withBundleRequestAndStandAloneBundleType_returnsFalse(Bundle.BundleType theBundleType) {
		Bundle bundle = new Bundle();
		bundle.setType(theBundleType);
		assertFalse(AuthorizationInterceptor.shouldExamineChildResources(bundle, ourCtx));
	}

	@ParameterizedTest
	@EnumSource(value = Bundle.BundleType.class, names = {"DOCUMENT", "MESSAGE", "COLLECTION"}, mode= EnumSource.Mode.EXCLUDE)
	public void testShouldExamineBundleResources_withBundleRequestAndNonStandAloneBundleType_returnsTrue(Bundle.BundleType theBundleType) {
		Bundle bundle = new Bundle();
		bundle.setType(theBundleType);
		assertTrue(AuthorizationInterceptor.shouldExamineChildResources(bundle, ourCtx));
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyCarePlanResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CarePlan.class;
		}

		@Read(version = true)
		public CarePlan read(@IdParam IdType theId) {
			markHitMethod();
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (CarePlan) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search() {
			markHitMethod();
			return ourReturn;
		}
	}

	@SuppressWarnings("unused")
	public static class DummyEncounterResourceProvider implements IResourceProvider {

		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdType theId) {
			markHitMethod();
			Bundle retVal = new Bundle();
			for (Resource next : ourReturn) {
				retVal.addEntry().setResource(next);
			}
			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Encounter.class;
		}
	}

	public static class DummyOrganizationResourceProvider implements IResourceProvider {


		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Organization.class;
		}

		/**
		 * This should come before operation1
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation0(@IdParam(optional = true) IdType theId) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

	}

	public static class DummyDiagnosticReportResourceProvider implements IResourceProvider {


		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return DiagnosticReport.class;
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "subject") ReferenceParam theSubject,
			@OptionalParam(name = "patient") ReferenceParam thePatient
		) {
			markHitMethod();
			return ourReturn;
		}
	}

	public static class DummyGroupResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Group.class;
		}


		@Read(version = true)
		public Group read(@IdParam IdType theId) {
			markHitMethod();
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (Group) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "member.entity") ReferenceParam thePatientRef
		) {
			markHitMethod();
			return ourReturn;
		}

		@Delete()
		public MethodOutcome delete(
			@IdParam IIdType theResource,
			@ConditionalUrlParam String theConditional,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			ServletRequestDetails theServletRequestDetails,
			RequestDetails theRequestDetails
		) {
			if (isNotBlank(theConditional)) {
				HookParams params = new HookParams();
				params.add(IBaseResource.class, ourDeleted.get(0));
				params.add(RequestDetails.class, theRequestDetails);
				params.addIfMatchesType(ServletRequestDetails.class, theServletRequestDetails);
				params.add(TransactionDetails.class, new TransactionDetails());

				theInterceptorBroadcaster
					.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, params);
			}

			markHitMethod();
			return new MethodOutcome();
		}
	}

	public static class DummyDeviceResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Device.class;
		}

		@Read(version = true)
		public Device read(@IdParam IdType theId) {
			markHitMethod();
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (Device) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "patient") ReferenceParam thePatient
		) {
			markHitMethod();
			return ourReturn;
		}
	}

	@SuppressWarnings("unused")
	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl) {
			markHitMethod();
			theResource.setId("Observation/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(@IdParam IdType theId) {
			markHitMethod();
			return new MethodOutcome();
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}

		/**
		 * This should come before operation1
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation0(@IdParam IdType theId) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		/**
		 * This should come after operation0
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation1() {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Patch
		public MethodOutcome patch(@IdParam IdType theId, PatchTypeEnum thePatchType, @ResourceParam String theBody) {
			markHitMethod();
			return new MethodOutcome().setId(theId.withVersion("2"));
		}

		@Read(version = true)
		public Observation read(@IdParam IdType theId) {
			markHitMethod();
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (Observation) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "_id") TokenAndListParam theIds,
			@OptionalParam(name = "subject") ReferenceParam theSubject) {
			markHitMethod();
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Observation theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			markHitMethod();

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Observation", ourConditionalCreateId);
				theResource.setId(actual);
			} else {
				theResource.setId(theId.withVersion("2"));
			}

			{
				HookParams params = new HookParams();
				params.add(IBaseResource.class, theResource);
				params.add(RequestDetails.class, theRequestDetails);
				params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				params.add(TransactionDetails.class, new TransactionDetails());
				params.add(RequestPartitionId.class, RequestPartitionId.fromPartitionId(null));
				ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, params);
			}

			{
				HookParams params = new HookParams();
				params.add(RequestDetails.class, theRequestDetails);
				params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				params.add(IPreResourceShowDetails.class, new SimplePreResourceShowDetails(theResource));
				ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESHOW_RESOURCES, params);
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(theResource);
			return retVal;
		}

	}

	public static class DummyServiceRequestResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ServiceRequest.class;
		}

		@Search
		public List<Resource> search() {
			assert ourReturn != null;
			markHitMethod();
			return ourReturn;
		}

	}

	public static class DummyConsentResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Consent.class;
		}

		@Search
		public List<Resource> search() {
			assert ourReturn != null;
			markHitMethod();
			return ourReturn;
		}

	}

	@SuppressWarnings("unused")
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {

			markHitMethod();
			theResource.setId("Patient/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);

			HookParams params = new HookParams();
			params.add(RequestDetails.class, theRequestDetails);
			params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			params.add(IPreResourceShowDetails.class, new SimplePreResourceShowDetails(theResource));
			ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESHOW_RESOURCES, params);

			return retVal;
		}

		@Delete()
		public MethodOutcome delete(IInterceptorBroadcaster theRequestOperationCallback, @IdParam IdType theId, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			markHitMethod();
			for (IBaseResource next : ourReturn) {
				HookParams params = new HookParams()
					.add(IBaseResource.class, next)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
					.add(TransactionDetails.class, new TransactionDetails());
				theRequestOperationCallback.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, params);
			}
			return new MethodOutcome();
		}

		@Operation(name = "everything", idempotent = true)
		public Bundle everything(@IdParam IdType theId) {
			markHitMethod();
			Bundle retVal = new Bundle();
			for (Resource next : ourReturn) {
				retVal.addEntry().setResource(next);
			}
			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@History()
		public List<Resource> history() {
			markHitMethod();
			return (ourReturn);
		}

		@History()
		public List<Resource> history(@IdParam IdType theId) {
			markHitMethod();
			return (ourReturn);
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation0(@IdParam IdType theId) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		/**
		 * More generic method second to make sure that the
		 * other method takes precedence
		 */
		@Operation(name = "opName", idempotent = true)
		public Parameters operation1() {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName2", idempotent = true)
		public Parameters operation2(@IdParam IdType theId) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "opName2", idempotent = true)
		public Parameters operation2() {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Patch()
		public MethodOutcome patch(@IdParam IdType theId, @ResourceParam String theResource, PatchTypeEnum thePatchType) {
			markHitMethod();

			return new MethodOutcome();
		}

		@Read(version = true)
		public Patient read(@IdParam IdType theId) {
			markHitMethod();
			if (ourReturn.isEmpty()) {
				throw new ResourceNotFoundException(theId);
			}
			return (Patient) ourReturn.get(0);
		}

		@Search()
		public List<Resource> search(@OptionalParam(name = "_id") TokenAndListParam theIdParam) {
			markHitMethod();
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient theResource, @ConditionalUrlParam String theConditionalUrl, RequestDetails theRequestDetails) {
			markHitMethod();

			if (isNotBlank(theConditionalUrl)) {
				IdType actual = new IdType("Patient", ourConditionalCreateId);
				theResource.setId(actual);
			} else {
				theResource.setId(theId.withVersion("2"));
			}

			{
				HookParams params = new HookParams();
				params.add(IBaseResource.class, theResource);
				params.add(RequestDetails.class, theRequestDetails);
				params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				params.add(TransactionDetails.class, new TransactionDetails());
				params.add(RequestPartitionId.class, RequestPartitionId.fromPartitionId(null));
				ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, params);
			}
			{
				HookParams params = new HookParams();
				params.add(RequestDetails.class, theRequestDetails);
				params.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				params.add(IPreResourceShowDetails.class, new SimplePreResourceShowDetails(theResource));
				ourServer.getInterceptorService().callHooks(Pointcut.STORAGE_PRESHOW_RESOURCES, params);
			}

			MethodOutcome retVal = new MethodOutcome();
			retVal.setResource(theResource);
			return retVal;
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @IdParam IdType theId, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding,
												@Validate.Mode ValidationModeEnum theMode, @Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			markHitMethod();
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient theResource, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
												@Validate.Profile String theProfile, RequestDetails theRequestDetails) {
			markHitMethod();
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OK");
			return new MethodOutcome(oo);
		}

	}

	private static void markHitMethod() {
		ourHitMethod = true;
	}

	public static class PlainProvider {

		@History()
		public List<Resource> history() {
			markHitMethod();
			return (ourReturn);
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "process-message", idempotent = true)
		public Parameters processMessage(@OperationParam(name = "content") Bundle theInput) {
			markHitMethod();
			return (Parameters) new Parameters().setId("1");
		}

		@GraphQL
		public String processGraphQlRequest(ServletRequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQueryUrl String theQuery) {
			markHitMethod();
			return "{'foo':'bar'}";
		}

		@Transaction()
		public Bundle search(ServletRequestDetails theRequestDetails, IInterceptorBroadcaster theInterceptorBroadcaster, @TransactionParam Bundle theInput) {
			markHitMethod();
			if (ourDeleted != null) {
				for (IBaseResource next : ourDeleted) {
					HookParams params = new HookParams()
						.add(IBaseResource.class, next)
						.add(RequestDetails.class, theRequestDetails)
						.add(ServletRequestDetails.class, theRequestDetails)
						.add(TransactionDetails.class, new TransactionDetails());
					theInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, params);
				}
			}
			return (Bundle) ourReturn.get(0);
		}

	}
}
