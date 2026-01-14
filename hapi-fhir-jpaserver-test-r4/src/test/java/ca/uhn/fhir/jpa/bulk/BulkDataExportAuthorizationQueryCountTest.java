package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.batch2.jobs.export.BulkExportAppCtx;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthResourceResolver;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRuleBuilderRuleBulkExport;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;

import ca.uhn.fhir.util.Batch2JobDefinitionConstants;

import jakarta.annotation.Nonnull;

import java.util.List;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;

import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = BulkDataExportAuthorizationQueryCountTest.MockBulkExportJobConfig.class)
public class BulkDataExportAuthorizationQueryCountTest extends BaseResourceProviderR4Test {
	@RegisterExtension
	public static final RestfulServerExtension ourPatientExportServer = new RestfulServerExtension(FhirContext.forR4Cached())
		.registerInterceptor(buildAuthInterceptorWithPatientExportByFilterPermissions(BulkExportJobParameters.ExportStyle.PATIENT))
		.keepAliveBetweenTests();

	@RegisterExtension
	public static final RestfulServerExtension ourGroupExportServer = new RestfulServerExtension(FhirContext.forR4Cached())
		.registerInterceptor(buildAuthInterceptorWithPatientExportByFilterPermissions(BulkExportJobParameters.ExportStyle.GROUP))
		.keepAliveBetweenTests();

	private static final Identifier AUTH_IDENTIFIER = new Identifier().setSystem("foo").setValue("bar");
	private static final Identifier UNAUTH_IDENTIFIER = new Identifier().setSystem("abc").setValue("123");
	private static IAuthResourceResolver ourAuthResourceResolver;

	@Autowired
	public void setAuthResourceResolver(IAuthResourceResolver theResourceResolver) {
		ourAuthResourceResolver = theResourceResolver;
	}

	@BeforeAll
	public static void beforeAll(@Autowired BulkDataExportProvider theBulkExportProvider) {
		ourPatientExportServer.registerProvider(theBulkExportProvider);
		ourGroupExportServer.registerProvider(theBulkExportProvider);
	}

	@BeforeEach
	public void beforeEach() {
		myStorageSettings.setEnableTaskBulkExportJobExecution(true);
		ignoreCapturingBatchTableSelectQueries();
	}

	@AfterEach
	public void afterEach() {
		myStorageSettings.setEnableTaskBulkExportJobExecution(new JpaStorageSettings().isEnableTaskBulkExportJobExecution());
		myCaptureQueriesListener.setSelectQueryInclusionCriteria(CircularQueueCaptureQueriesListener.DEFAULT_SELECT_INCLUSION_CRITERIA);
	}

	@ParameterizedTest
	@MethodSource("paramsIdentifierAuthorizedUnauthorized")
	public void testGroupBulkExport_exportInstance(Identifier theIdentifier) {
		// Given
		createPatientWithIdAndIdentifier("Patient/p1", AUTH_IDENTIFIER);
		Long groupPid = createGroupWithIdentifier(theIdentifier);

		// When
		myCaptureQueriesListener.clear();

		Parameters parameters = buildExportParameters();
		performInstanceBulkExportAndAwaitCompletion(ourGroupExportServer.getFhirClient(), "Group/g1", parameters);

		// Then
		assertAuthorizationSelectQueriesCaptured(List.of(groupPid));
	}

	@ParameterizedTest
	@MethodSource("paramsIdentifierAuthorizedUnauthorized")
	public void testPatientBulkExport_exportInstance(Identifier theIdentifier) {
		// Given
		Long patientPid = createPatientWithIdAndIdentifier("Patient/p1", theIdentifier);

		// When
		myCaptureQueriesListener.clear();

		Parameters parameters = buildExportParameters();
		performInstanceBulkExportAndAwaitCompletion(ourPatientExportServer.getFhirClient(), "Patient/p1", parameters);

		// Then
		assertAuthorizationSelectQueriesCaptured(List.of(patientPid));
	}

	@Test
	public void testPatientBulkExport_exportMultipleInstancesAuthorized() {
		// Given
		Long patientPid = createPatientWithIdAndIdentifier("Patient/p1", AUTH_IDENTIFIER);
		Long patientPid2 = createPatientWithIdAndIdentifier("Patient/p2", AUTH_IDENTIFIER);

		// When
		myCaptureQueriesListener.clear();

		Parameters parameters = buildExportParameters("Patient/p1", "Patient/p2");
		performTypeBulkExportAndAwaitCompletion(ourPatientExportServer.getFhirClient(), "Patient", parameters);

		// Then
		assertAuthorizationSelectQueriesCaptured(List.of(patientPid, patientPid2));
	}

	@ParameterizedTest
	@MethodSource("paramsIdentifierAuthorizedUnauthorized")
	public void testPatientBulkExport_exportType(Identifier theIdentifier) {
		// Given
		createPatientWithIdAndIdentifier("Patient/p1", theIdentifier);

		// When
		myCaptureQueriesListener.clear();

		Parameters parameters = buildExportParameters();
		performTypeBulkExportAndAwaitCompletion(ourPatientExportServer.getFhirClient(), "Patient", parameters);

		// Then
		assertNoAuthorizationSelectQueriesCaptured(1);
	}

	public static Stream<Arguments> paramsIdentifierAuthorizedUnauthorized() {
		return Stream.of(
			Arguments.of(AUTH_IDENTIFIER),
			Arguments.of(UNAUTH_IDENTIFIER)
		);
	}

	private Long createPatientWithIdAndIdentifier(String thePatientFhirId, Identifier theIdentifier) {
		Patient p = new Patient();
		p.setId(thePatientFhirId);
		p.addIdentifier(theIdentifier);
		p.setActive(true);
		return (Long) myPatientDao.update(p, mySrd).getPersistentId().getId();
	}

	private Long createGroupWithIdentifier(Identifier theIdentifier) {
		Group group = new Group();
		group.setId("g1");
		group.addIdentifier(theIdentifier);
		group.addMember().setEntity(new Reference("Patient/p1"));
		return (Long) myGroupDao.update(group, mySrd).getPersistentId().getId();
	}

	private static Parameters buildExportParameters(String... theIds) {
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("_type").setValue(new StringType("Patient"));
		parameters.addParameter().setName("_outputFormat").setValue(new StringType("application/fhir+ndjson"));
		for (String id : theIds) {
			parameters.addParameter().setName("patient").setValue(new StringType(id));
		}
		return parameters;
	}

	private void performInstanceBulkExportAndAwaitCompletion(IGenericClient theClient, String theResourceId, Parameters theParams) {
		MethodOutcome outcome =  theClient.operation().onInstance(theResourceId).named("$export").withParameters(theParams)
			.withAdditionalHeader("Prefer", "respond-async")
			.withAdditionalHeader("cache-control", "no-cache")
			.returnMethodOutcome()
			.execute();

		String jobId = outcome.getFirstResponseHeader("content-location").get().split("=")[1];
		myBatch2JobHelper.awaitJobCompletion(jobId);
	}

	private void performTypeBulkExportAndAwaitCompletion(IGenericClient theClient, String theResourceType, Parameters theParams) {
		MethodOutcome outcome =  theClient.operation().onType(theResourceType).named("$export").withParameters(theParams)
			.withAdditionalHeader("Prefer", "respond-async")
			.withAdditionalHeader("cache-control", "no-cache")
			.returnMethodOutcome()
			.execute();

		String jobId = outcome.getFirstResponseHeader("content-location").get().split("=")[1];
		myBatch2JobHelper.awaitJobCompletion(jobId);
	}

	private void assertAuthorizationSelectQueriesCaptured(List<Long> theExpectedQueriedResourcePids) {
		/*
		 * The AuthorizationInterceptor uses a AuthResourceResolver which queries the DB for the requested Patient
		 * then uses a matcher to match the Patient against the permissioned query filter
		 *
		 * Breakdown expected SELECT queries:
		 * 2 per resource for validating the target exists before exporting (resolve PID, resolve resource)
		 * 2 (total) for resolving the resource for authorization (resolve PID, resolve resource)
		 */
		int numberOfResources = theExpectedQueriedResourcePids.size();

		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2 * numberOfResources + 2, myCaptureQueriesListener.countSelectQueries());

		// Verify the SELECT queries the actual expected resource PID
		List<String> queries = myCaptureQueriesListener.getSelectQueries().stream().map(t -> t.getSql(true, false)).toList();
		assertThat(queries).allMatch(query -> theExpectedQueriedResourcePids.stream().map(String::valueOf).anyMatch(query::contains));
	}

	private void assertNoAuthorizationSelectQueriesCaptured(int theNumberOfResources) {
		// Expect only 2 select queries per resource for
		// validating the target exists before exporting (resolve PID, resolve resource)
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2 * theNumberOfResources , myCaptureQueriesListener.countSelectQueries());
	}

	/**
	 * Ignore capturing SELECT queries to the batch tables (BT2_...)
	 */
	private void ignoreCapturingBatchTableSelectQueries() {
		myCaptureQueriesListener.setSelectQueryInclusionCriteria(CircularQueueCaptureQueriesListener.DEFAULT_SELECT_INCLUSION_CRITERIA.and(t -> !t.contains("BT2_")));
	}

	/**
	 * Build an AuthorizationInterceptor with permissions to export by filter and export type (Group/Patient)
	 * @param theExportType the export type (Group/Patient)
	 * @return AuthorizationInterceptor with appropriate permissions to export
	 */
	private static AuthorizationInterceptor buildAuthInterceptorWithPatientExportByFilterPermissions(BulkExportJobParameters.ExportStyle theExportType) {
		Class<? extends IBaseResource> resourceType = theExportType.equals(BulkExportJobParameters.ExportStyle.PATIENT) ? Patient.class : Group.class;
		return new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				RuleBuilder ruleBuilder = new RuleBuilder();

				// Allow export with filter rule
				IAuthRuleBuilderRuleBulkExport bulkExportWithFilterRuleBuilder = ruleBuilder.allow()
					.bulkExport();

				if (theExportType.equals(BulkExportJobParameters.ExportStyle.PATIENT)) {
					bulkExportWithFilterRuleBuilder.patientExportOnFilter("?identifier=foo|bar").andThen();
				} else {
					bulkExportWithFilterRuleBuilder.groupExportOnFilter("?identifier=foo|bar").andThen();
				}

				// Allow running type export operation on Patients
				ruleBuilder.allow()
					.operation()
					.named(ProviderConstants.OPERATION_EXPORT)
					.onType(resourceType)
					.andAllowAllResponses()
					.andThen();

				// Allow running instance export operation on Patients
				ruleBuilder.allow()
					.operation()
					.named(ProviderConstants.OPERATION_EXPORT)
					.onInstancesOfType(resourceType)
					.andAllowAllResponses()
					.andThen();

				// Allow polling export status
				ruleBuilder.allow()
					.operation()
					.named(ProviderConstants.OPERATION_EXPORT_POLL_STATUS)
					.onServer()
					.andAllowAllResponses()
					.andThen();

				return ruleBuilder.build();
			}

			@Override
			public IAuthResourceResolver getAuthResourceResolver() {
				return ourAuthResourceResolver;
			}
		};
	}

	/**
	 * Mock configuration class with empty steps that overrides the definition of a bulk export job.
	 * This allows us to analyze the query counts for the work before the bulk export runs without actually running
	 * bulk export. If bulk export starts before getting the query count, then there could be extra queries counted.
	 */
	@Configuration
	public static class MockBulkExportJobConfig extends BulkExportAppCtx {
		@Bean
		@Override
		public JobDefinition bulkExportJobDefinition() {
			JobDefinition.Builder<IModelJson, VoidModel> builder = JobDefinition.newBuilder();
			builder.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			builder.setJobDescription("Mock FHIR Bulk Export");
			builder.setJobDefinitionVersion(1);

			return builder.setParametersType(BulkExportJobParameters.class)
				// validator
				.setParametersValidator(bulkExportJobParametersValidator())
				.gatedExecution()
				.addFirstStep("stepA", "mock step A", ResourceIdList.class, new MockStepA())
				.addLastStep("stepB", "mock step B", new MockStepB())
				.build();
		}

		@Bean
		@Override
		public JobDefinition bulkExportJobV2Definition() {
			JobDefinition.Builder<IModelJson, VoidModel> builder = JobDefinition.newBuilder();
			builder.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
			builder.setJobDescription("Mock FHIR Bulk Export");
			builder.setJobDefinitionVersion(2);

			return builder.setParametersType(BulkExportJobParameters.class)
				// validator
				.setParametersValidator(bulkExportJobParametersValidator())
				.gatedExecution()
				.addFirstStep("stepA", "mock step A", ResourceIdList.class, new MockStepA())
				.addLastStep("stepB", "mock step B", new MockStepB())
				.build();
		}

		// Mock steps that do nothing.
		// At least 2 steps are required to define a job.
		private static class MockStepA implements IFirstJobStepWorker<BulkExportJobParameters, ResourceIdList> {
			@Nonnull
			@Override
			public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<ResourceIdList> theDataSink) throws JobExecutionFailedException {
				return new RunOutcome(1);
			}
		}

		private static class MockStepB implements IJobStepWorker<BulkExportJobParameters, ResourceIdList, VoidModel> {
			@Nonnull
			@Override
			public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, ResourceIdList> theStepExecutionDetails, @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {
				return new RunOutcome(1);
			}
		}
	}
}
