package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.HapiMigrator;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.MigrationResult;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.taskdef.InitializeSchemaTask;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.LoggingExtension;
import ca.uhn.fhir.util.VersionEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HapiFhirJpaMigrationTasksTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiFhirJpaMigrationTasksTest.class);
	private static final String MIGRATION_TABLE_NAME = "HFJ_FLY_MIGRATOR";
	private final BasicDataSource myDataSource = newDataSource();
	private final JdbcTemplate myJdbcTemplate = new JdbcTemplate(myDataSource);
	private final DriverTypeEnum.ConnectionProperties myConnection = DriverTypeEnum.H2_EMBEDDED.newConnectionProperties(myDataSource);

	@RegisterExtension
	private LoggingExtension myLoggingExtension = new LoggingExtension();

	@Test
	public void testCreate_NonPartitionedIds() throws SQLException {
		HapiFhirJpaMigrationTasks tasks = new HapiFhirJpaMigrationTasks(Collections.emptySet());

		HapiMigrator migrator = new HapiMigrator(MIGRATION_TABLE_NAME, myDataSource, DriverTypeEnum.H2_EMBEDDED);
		migrator.addTasks(tasks.getAllTasks(VersionEnum.values()));
		migrator.createMigrationTableIfRequired();

		MigrationResult outcome = migrator.migrate();
		assertEquals(0, outcome.changes);
		assertEquals(1, outcome.succeededTasks.size());
		assertEquals(0, outcome.failedTasks.size());

		Set<String> columns = JdbcUtils.getPrimaryKeyColumns(myConnection, "HFJ_RESOURCE");
		assertThat(new ArrayList<>(columns)).asList().containsExactlyInAnyOrder("RES_ID");
	}

	@Test
	public void testCreate_PartitionedIds() throws SQLException {
		HapiFhirJpaMigrationTasks tasks = new HapiFhirJpaMigrationTasks(Set.of(HapiFhirJpaMigrationTasks.FlagEnum.DB_PARTITION_MODE.getCommandLineValue()));

		HapiMigrator migrator = new HapiMigrator(MIGRATION_TABLE_NAME, myDataSource, DriverTypeEnum.H2_EMBEDDED);
		migrator.addTasks(tasks.getAllTasks(VersionEnum.values()));
		migrator.createMigrationTableIfRequired();

		MigrationResult outcome = migrator.migrate();
		assertEquals(0, outcome.changes);
		assertEquals(1, outcome.succeededTasks.size());
		assertEquals(0, outcome.failedTasks.size());

		Set<String> columns = JdbcUtils.getPrimaryKeyColumns(myConnection, "HFJ_RESOURCE");
		assertThat(new ArrayList<>(columns)).asList().containsExactlyInAnyOrder("RES_ID", "PARTITION_ID");
	}

	/**
	 * Verify migration task 20240617.4 which creates hashes on the unique combo
	 * search param table if they aren't already present. Hash columns were only
	 * added in 7.4.0 so this backfills them.
	 */
	@Test
	public void testCreateUniqueComboParamHashes() {
		/*
		 * Setup
		 */

		// Create migrator and initialize schema using a static version
		// of the schema from the 7.2.0 release
		HapiFhirJpaMigrationTasks tasks = new HapiFhirJpaMigrationTasks(Set.of());
		HapiMigrator migrator = new HapiMigrator(MIGRATION_TABLE_NAME, myDataSource, DriverTypeEnum.H2_EMBEDDED);
		migrator.addTask(new InitializeSchemaTask("7.2.0", "20180115.0",
			new SchemaInitializationProvider(
				"HAPI FHIR", "/jpa_h2_schema_720", "HFJ_RESOURCE", true)));

		migrator.createMigrationTableIfRequired();
		migrator.migrate();

		// Run a second time to run the 7.4.0 migrations
		MigrationTaskList allTasks = tasks.getAllTasks(VersionEnum.V7_3_0, VersionEnum.V7_4_0);
		migrator.addAllTasksForUnitTest(allTasks);
		migrator.migrate();

		// Create a unique index row with no hashes populated
		insertRow_ResourceTable();
		insertRow_ResourceIndexedComboStringUnique();

		/*
		 * Execute
		 */

		// Remove the task we're testing from the migrator history, so it runs again
		assertEquals(1, myJdbcTemplate.update("DELETE FROM " + MIGRATION_TABLE_NAME + " WHERE version = ?", "7.4.0.20240625.40"));

		// Run the migrator
		ourLog.info("About to run the migrator a second time");
		MigrationResult migrationResult = migrator.migrate();
		assertEquals(1, migrationResult.succeededTasks.size());
		assertEquals(0, migrationResult.failedTasks.size());

		/*
		 * Verify
		 */

		List<Map<String, Object>> rows = myJdbcTemplate.query("SELECT * FROM HFJ_IDX_CMP_STRING_UNIQ", new ColumnMapRowMapper());
		assertEquals(1, rows.size());
		Map<String, Object> row = rows.get(0);
		assertThat(row.get("HASH_COMPLETE")).as(row::toString).isEqualTo(-5443017569618195896L);
		assertThat(row.get("HASH_COMPLETE_2")).as(row::toString).isEqualTo(-1513800680307323438L);
	}

	@Test
	@Disabled("This will be enabled in a subsequent PR")
	public void testVerifyDbpm_ExistingNonPartitionedSchema_PartitionedMode() {
		HapiFhirJpaMigrationTasks tasks;
		HapiMigrator migrator;

		// Setup: Initialize the database in legacy mode
		tasks = new HapiFhirJpaMigrationTasks(Set.of());
		migrator = new HapiMigrator(MIGRATION_TABLE_NAME, myDataSource, DriverTypeEnum.H2_EMBEDDED);
		migrator.createMigrationTableIfRequired();
		migrator.addAllTasksForUnitTest(tasks.getAllTasks(VersionEnum.values()));
		migrator.migrate();

		// Test: Run a second time but with DB partitioned mode enabled
		tasks = new HapiFhirJpaMigrationTasks(Set.of(HapiFhirJpaMigrationTasks.FlagEnum.DB_PARTITION_MODE.getCommandLineValue()));
		migrator = new HapiMigrator(MIGRATION_TABLE_NAME, myDataSource, DriverTypeEnum.H2_EMBEDDED);
		migrator.createMigrationTableIfRequired();
		migrator.addAllTasksForUnitTest(tasks.getAllTasks(VersionEnum.values()));
		try {
			migrator.migrate();
			fail();
		} catch (Exception e) {
			// Verify: We should fail
			assertEquals("AA", e.toString());
		}

	}
	@Test
	public void testGetResourceTypeData() {
		HapiFhirJpaMigrationTasks tasks = new HapiFhirJpaMigrationTasks(Collections.emptySet());

		String result = tasks.getResourceTypeData(DriverTypeEnum.H2_EMBEDDED);
		ourLog.debug(result);
		assertThat(result).isEqualTo("(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Account'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ActivityDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ActorDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AdministrableProductDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AdverseEvent'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AllergyIntolerance'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Appointment'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AppointmentResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ArtifactAssessment'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AuditEvent'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Basic'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Binary'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'BiologicallyDerivedProduct'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'BiologicallyDerivedProductDispense'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'BodySite'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'BodyStructure'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Bundle'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CapabilityStatement'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CarePlan'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CareTeam'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CatalogEntry'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ChargeItem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ChargeItemDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Citation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Claim'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ClaimResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ClinicalImpression'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ClinicalUseDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CodeSystem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Communication'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CommunicationRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CompartmentDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Composition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ConceptMap'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Condition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ConditionDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Conformance'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Consent'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Contract'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Coverage'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CoverageEligibilityRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CoverageEligibilityResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DataElement'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DetectedIssue'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Device'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceAssociation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceComponent'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceDispense'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceMetric'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceUsage'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceUseRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceUseStatement'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DiagnosticOrder'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DiagnosticReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DocumentManifest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DocumentReference'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EffectEvidenceSynthesis'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EligibilityRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EligibilityResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Encounter'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EncounterHistory'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Endpoint'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EnrollmentRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EnrollmentResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EpisodeOfCare'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EventDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Evidence'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EvidenceReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EvidenceVariable'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ExampleScenario'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ExpansionProfile'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ExplanationOfBenefit'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'FamilyMemberHistory'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Flag'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'FormularyItem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'GenomicStudy'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Goal'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'GraphDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Group'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'GuidanceResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'HealthcareService'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImagingManifest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImagingObjectSelection'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImagingSelection'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImagingStudy'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Immunization'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImmunizationEvaluation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImmunizationRecommendation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImplementationGuide'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Ingredient'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'InsurancePlan'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'InventoryItem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'InventoryReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Invoice'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Library'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Linkage'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'List'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Location'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ManufacturedItemDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Measure'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MeasureReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Media'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Medication'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationAdministration'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationDispense'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationKnowledge'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationOrder'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationStatement'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProduct'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductAuthorization'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductContraindication'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductIndication'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductIngredient'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductInteraction'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductManufactured'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductPackaged'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductPharmaceutical'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductUndesirableEffect'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MessageDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MessageHeader'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MolecularSequence'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'NamingSystem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'NutritionIntake'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'NutritionOrder'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'NutritionProduct'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Observation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ObservationDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'OperationDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'OperationOutcome'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Order'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'OrderResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Organization'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'OrganizationAffiliation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PackagedProductDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Parameters'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Patient'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PaymentNotice'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PaymentReconciliation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Permission'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Person'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PlanDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Practitioner'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PractitionerRole'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Procedure'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ProcedureRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ProcessRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ProcessResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Provenance'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Questionnaire'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'QuestionnaireResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ReferralRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RegulatedAuthorization'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RelatedPerson'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RequestGroup'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RequestOrchestration'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Requirements'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ResearchDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ResearchElementDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ResearchStudy'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ResearchSubject'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RiskAssessment'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RiskEvidenceSynthesis'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Schedule'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SearchParameter'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Sequence'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ServiceDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ServiceRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Slot'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Specimen'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SpecimenDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'StructureDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'StructureMap'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Subscription'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubscriptionStatus'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubscriptionTopic'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Substance'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceNucleicAcid'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstancePolymer'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceProtein'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceReferenceInformation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceSourceMaterial'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceSpecification'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SupplyDelivery'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SupplyRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Task'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'TerminologyCapabilities'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'TestPlan'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'TestReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'TestScript'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Transport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ValueSet'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'VerificationResult'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'VisionPrescription')");

		result = tasks.getResourceTypeData(DriverTypeEnum.MSSQL_2012);
		ourLog.debug(result);
		assertThat(result).isEqualTo("(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Account'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ActivityDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ActorDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AdministrableProductDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AdverseEvent'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AllergyIntolerance'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Appointment'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AppointmentResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ArtifactAssessment'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'AuditEvent'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Basic'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Binary'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'BiologicallyDerivedProduct'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'BiologicallyDerivedProductDispense'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'BodySite'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'BodyStructure'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Bundle'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CapabilityStatement'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CarePlan'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CareTeam'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CatalogEntry'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ChargeItem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ChargeItemDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Citation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Claim'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ClaimResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ClinicalImpression'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ClinicalUseDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CodeSystem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Communication'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CommunicationRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CompartmentDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Composition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ConceptMap'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Condition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ConditionDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Conformance'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Consent'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Contract'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Coverage'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CoverageEligibilityRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'CoverageEligibilityResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DataElement'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DetectedIssue'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Device'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceAssociation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceComponent'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceDispense'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceMetric'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceUsage'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceUseRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DeviceUseStatement'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DiagnosticOrder'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DiagnosticReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DocumentManifest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'DocumentReference'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EffectEvidenceSynthesis'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EligibilityRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EligibilityResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Encounter'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EncounterHistory'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Endpoint'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EnrollmentRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EnrollmentResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EpisodeOfCare'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EventDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Evidence'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EvidenceReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'EvidenceVariable'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ExampleScenario'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ExpansionProfile'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ExplanationOfBenefit'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'FamilyMemberHistory'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Flag'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'FormularyItem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'GenomicStudy'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Goal'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'GraphDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Group'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'GuidanceResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'HealthcareService'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImagingManifest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImagingObjectSelection'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImagingSelection'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImagingStudy'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Immunization'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImmunizationEvaluation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImmunizationRecommendation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ImplementationGuide'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Ingredient'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'InsurancePlan'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'InventoryItem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'InventoryReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Invoice'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Library'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Linkage'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'List'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Location'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ManufacturedItemDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Measure'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MeasureReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Media'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Medication'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationAdministration'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationDispense'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationKnowledge'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationOrder'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicationStatement'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProduct'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductAuthorization'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductContraindication'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductIndication'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductIngredient'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductInteraction'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductManufactured'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductPackaged'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductPharmaceutical'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MedicinalProductUndesirableEffect'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MessageDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MessageHeader'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'MolecularSequence'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'NamingSystem'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'NutritionIntake'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'NutritionOrder'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'NutritionProduct'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Observation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ObservationDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'OperationDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'OperationOutcome'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Order'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'OrderResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Organization'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'OrganizationAffiliation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PackagedProductDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Parameters'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Patient'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PaymentNotice'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PaymentReconciliation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Permission'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Person'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PlanDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Practitioner'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'PractitionerRole'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Procedure'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ProcedureRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ProcessRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ProcessResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Provenance'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Questionnaire'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'QuestionnaireResponse'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ReferralRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RegulatedAuthorization'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RelatedPerson'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RequestGroup'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RequestOrchestration'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Requirements'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ResearchDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ResearchElementDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ResearchStudy'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ResearchSubject'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RiskAssessment'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'RiskEvidenceSynthesis'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Schedule'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SearchParameter'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Sequence'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ServiceDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ServiceRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Slot'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Specimen'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SpecimenDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'StructureDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'StructureMap'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Subscription'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubscriptionStatus'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubscriptionTopic'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Substance'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceDefinition'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceNucleicAcid'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstancePolymer'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceProtein'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceReferenceInformation'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceSourceMaterial'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SubstanceSpecification'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SupplyDelivery'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'SupplyRequest'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Task'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'TerminologyCapabilities'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'TestPlan'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'TestReport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'TestScript'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'Transport'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'ValueSet'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'VerificationResult'),(NEXT VALUE FOR SEQ_RESOURCE_TYPE,'VisionPrescription')");

		result = tasks.getResourceTypeData(DriverTypeEnum.POSTGRES_9_4);
		ourLog.debug(result);
		assertThat(result).isEqualTo("(NEXTVAL('SEQ_RESOURCE_TYPE'),'Account'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ActivityDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ActorDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'AdministrableProductDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'AdverseEvent'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'AllergyIntolerance'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Appointment'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'AppointmentResponse'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ArtifactAssessment'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'AuditEvent'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Basic'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Binary'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'BiologicallyDerivedProduct'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'BiologicallyDerivedProductDispense'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'BodySite'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'BodyStructure'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Bundle'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'CapabilityStatement'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'CarePlan'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'CareTeam'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'CatalogEntry'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ChargeItem'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ChargeItemDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Citation'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Claim'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ClaimResponse'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ClinicalImpression'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ClinicalUseDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'CodeSystem'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Communication'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'CommunicationRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'CompartmentDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Composition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ConceptMap'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Condition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ConditionDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Conformance'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Consent'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Contract'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Coverage'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'CoverageEligibilityRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'CoverageEligibilityResponse'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DataElement'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DetectedIssue'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Device'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DeviceAssociation'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DeviceComponent'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DeviceDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DeviceDispense'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DeviceMetric'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DeviceRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DeviceUsage'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DeviceUseRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DeviceUseStatement'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DiagnosticOrder'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DiagnosticReport'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DocumentManifest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'DocumentReference'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EffectEvidenceSynthesis'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EligibilityRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EligibilityResponse'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Encounter'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EncounterHistory'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Endpoint'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EnrollmentRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EnrollmentResponse'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EpisodeOfCare'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EventDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Evidence'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EvidenceReport'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'EvidenceVariable'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ExampleScenario'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ExpansionProfile'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ExplanationOfBenefit'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'FamilyMemberHistory'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Flag'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'FormularyItem'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'GenomicStudy'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Goal'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'GraphDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Group'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'GuidanceResponse'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'HealthcareService'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ImagingManifest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ImagingObjectSelection'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ImagingSelection'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ImagingStudy'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Immunization'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ImmunizationEvaluation'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ImmunizationRecommendation'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ImplementationGuide'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Ingredient'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'InsurancePlan'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'InventoryItem'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'InventoryReport'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Invoice'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Library'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Linkage'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'List'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Location'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ManufacturedItemDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Measure'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MeasureReport'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Media'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Medication'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicationAdministration'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicationDispense'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicationKnowledge'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicationOrder'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicationRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicationStatement'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProduct'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductAuthorization'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductContraindication'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductIndication'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductIngredient'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductInteraction'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductManufactured'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductPackaged'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductPharmaceutical'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MedicinalProductUndesirableEffect'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MessageDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MessageHeader'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'MolecularSequence'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'NamingSystem'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'NutritionIntake'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'NutritionOrder'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'NutritionProduct'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Observation'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ObservationDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'OperationDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'OperationOutcome'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Order'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'OrderResponse'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Organization'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'OrganizationAffiliation'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'PackagedProductDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Parameters'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Patient'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'PaymentNotice'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'PaymentReconciliation'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Permission'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Person'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'PlanDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Practitioner'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'PractitionerRole'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Procedure'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ProcedureRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ProcessRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ProcessResponse'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Provenance'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Questionnaire'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'QuestionnaireResponse'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ReferralRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'RegulatedAuthorization'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'RelatedPerson'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'RequestGroup'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'RequestOrchestration'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Requirements'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ResearchDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ResearchElementDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ResearchStudy'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ResearchSubject'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'RiskAssessment'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'RiskEvidenceSynthesis'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Schedule'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SearchParameter'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Sequence'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ServiceDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ServiceRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Slot'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Specimen'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SpecimenDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'StructureDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'StructureMap'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Subscription'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SubscriptionStatus'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SubscriptionTopic'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Substance'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SubstanceDefinition'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SubstanceNucleicAcid'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SubstancePolymer'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SubstanceProtein'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SubstanceReferenceInformation'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SubstanceSourceMaterial'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SubstanceSpecification'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SupplyDelivery'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'SupplyRequest'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Task'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'TerminologyCapabilities'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'TestPlan'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'TestReport'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'TestScript'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'Transport'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'ValueSet'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'VerificationResult'),(NEXTVAL('SEQ_RESOURCE_TYPE'),'VisionPrescription')");

		result = tasks.getResourceTypeData(DriverTypeEnum.ORACLE_12C);
		ourLog.debug(result);
		assertThat(result).isEqualTo("'Account,ActivityDefinition,ActorDefinition,AdministrableProductDefinition,AdverseEvent,AllergyIntolerance,Appointment,AppointmentResponse,ArtifactAssessment,AuditEvent,Basic,Binary,BiologicallyDerivedProduct,BiologicallyDerivedProductDispense,BodySite,BodyStructure,Bundle,CapabilityStatement,CarePlan,CareTeam,CatalogEntry,ChargeItem,ChargeItemDefinition,Citation,Claim,ClaimResponse,ClinicalImpression,ClinicalUseDefinition,CodeSystem,Communication,CommunicationRequest,CompartmentDefinition,Composition,ConceptMap,Condition,ConditionDefinition,Conformance,Consent,Contract,Coverage,CoverageEligibilityRequest,CoverageEligibilityResponse,DataElement,DetectedIssue,Device,DeviceAssociation,DeviceComponent,DeviceDefinition,DeviceDispense,DeviceMetric,DeviceRequest,DeviceUsage,DeviceUseRequest,DeviceUseStatement,DiagnosticOrder,DiagnosticReport,DocumentManifest,DocumentReference,EffectEvidenceSynthesis,EligibilityRequest,EligibilityResponse,Encounter,EncounterHistory,Endpoint,EnrollmentRequest,EnrollmentResponse,EpisodeOfCare,EventDefinition,Evidence,EvidenceReport,EvidenceVariable,ExampleScenario,ExpansionProfile,ExplanationOfBenefit,FamilyMemberHistory,Flag,FormularyItem,GenomicStudy,Goal,GraphDefinition,Group,GuidanceResponse,HealthcareService,ImagingManifest,ImagingObjectSelection,ImagingSelection,ImagingStudy,Immunization,ImmunizationEvaluation,ImmunizationRecommendation,ImplementationGuide,Ingredient,InsurancePlan,InventoryItem,InventoryReport,Invoice,Library,Linkage,List,Location,ManufacturedItemDefinition,Measure,MeasureReport,Media,Medication,MedicationAdministration,MedicationDispense,MedicationKnowledge,MedicationOrder,MedicationRequest,MedicationStatement,MedicinalProduct,MedicinalProductAuthorization,MedicinalProductContraindication,MedicinalProductDefinition,MedicinalProductIndication,MedicinalProductIngredient,MedicinalProductInteraction,MedicinalProductManufactured,MedicinalProductPackaged,MedicinalProductPharmaceutical,MedicinalProductUndesirableEffect,MessageDefinition,MessageHeader,MolecularSequence,NamingSystem,NutritionIntake,NutritionOrder,NutritionProduct,Observation,ObservationDefinition,OperationDefinition,OperationOutcome,Order,OrderResponse,Organization,OrganizationAffiliation,PackagedProductDefinition,Parameters,Patient,PaymentNotice,PaymentReconciliation,Permission,Person,PlanDefinition,Practitioner,PractitionerRole,Procedure,ProcedureRequest,ProcessRequest,ProcessResponse,Provenance,Questionnaire,QuestionnaireResponse,ReferralRequest,RegulatedAuthorization,RelatedPerson,RequestGroup,RequestOrchestration,Requirements,ResearchDefinition,ResearchElementDefinition,ResearchStudy,ResearchSubject,RiskAssessment,RiskEvidenceSynthesis,Schedule,SearchParameter,Sequence,ServiceDefinition,ServiceRequest,Slot,Specimen,SpecimenDefinition,StructureDefinition,StructureMap,Subscription,SubscriptionStatus,SubscriptionTopic,Substance,SubstanceDefinition,SubstanceNucleicAcid,SubstancePolymer,SubstanceProtein,SubstanceReferenceInformation,SubstanceSourceMaterial,SubstanceSpecification,SupplyDelivery,SupplyRequest,Task,TerminologyCapabilities,TestPlan,TestReport,TestScript,Transport,ValueSet,VerificationResult,VisionPrescription'");

		result = tasks.getResourceTypeData(DriverTypeEnum.MYSQL_5_7);
		ourLog.debug(result);
		assertThat(result).isEqualTo("('1','Account'),('2','ActivityDefinition'),('3','ActorDefinition'),('4','AdministrableProductDefinition'),('5','AdverseEvent'),('6','AllergyIntolerance'),('7','Appointment'),('8','AppointmentResponse'),('9','ArtifactAssessment'),('10','AuditEvent'),('11','Basic'),('12','Binary'),('13','BiologicallyDerivedProduct'),('14','BiologicallyDerivedProductDispense'),('15','BodySite'),('16','BodyStructure'),('17','Bundle'),('18','CapabilityStatement'),('19','CarePlan'),('20','CareTeam'),('21','CatalogEntry'),('22','ChargeItem'),('23','ChargeItemDefinition'),('24','Citation'),('25','Claim'),('26','ClaimResponse'),('27','ClinicalImpression'),('28','ClinicalUseDefinition'),('29','CodeSystem'),('30','Communication'),('31','CommunicationRequest'),('32','CompartmentDefinition'),('33','Composition'),('34','ConceptMap'),('35','Condition'),('36','ConditionDefinition'),('37','Conformance'),('38','Consent'),('39','Contract'),('40','Coverage'),('41','CoverageEligibilityRequest'),('42','CoverageEligibilityResponse'),('43','DataElement'),('44','DetectedIssue'),('45','Device'),('46','DeviceAssociation'),('47','DeviceComponent'),('48','DeviceDefinition'),('49','DeviceDispense'),('50','DeviceMetric'),('51','DeviceRequest'),('52','DeviceUsage'),('53','DeviceUseRequest'),('54','DeviceUseStatement'),('55','DiagnosticOrder'),('56','DiagnosticReport'),('57','DocumentManifest'),('58','DocumentReference'),('59','EffectEvidenceSynthesis'),('60','EligibilityRequest'),('61','EligibilityResponse'),('62','Encounter'),('63','EncounterHistory'),('64','Endpoint'),('65','EnrollmentRequest'),('66','EnrollmentResponse'),('67','EpisodeOfCare'),('68','EventDefinition'),('69','Evidence'),('70','EvidenceReport'),('71','EvidenceVariable'),('72','ExampleScenario'),('73','ExpansionProfile'),('74','ExplanationOfBenefit'),('75','FamilyMemberHistory'),('76','Flag'),('77','FormularyItem'),('78','GenomicStudy'),('79','Goal'),('80','GraphDefinition'),('81','Group'),('82','GuidanceResponse'),('83','HealthcareService'),('84','ImagingManifest'),('85','ImagingObjectSelection'),('86','ImagingSelection'),('87','ImagingStudy'),('88','Immunization'),('89','ImmunizationEvaluation'),('90','ImmunizationRecommendation'),('91','ImplementationGuide'),('92','Ingredient'),('93','InsurancePlan'),('94','InventoryItem'),('95','InventoryReport'),('96','Invoice'),('97','Library'),('98','Linkage'),('99','List'),('100','Location'),('101','ManufacturedItemDefinition'),('102','Measure'),('103','MeasureReport'),('104','Media'),('105','Medication'),('106','MedicationAdministration'),('107','MedicationDispense'),('108','MedicationKnowledge'),('109','MedicationOrder'),('110','MedicationRequest'),('111','MedicationStatement'),('112','MedicinalProduct'),('113','MedicinalProductAuthorization'),('114','MedicinalProductContraindication'),('115','MedicinalProductDefinition'),('116','MedicinalProductIndication'),('117','MedicinalProductIngredient'),('118','MedicinalProductInteraction'),('119','MedicinalProductManufactured'),('120','MedicinalProductPackaged'),('121','MedicinalProductPharmaceutical'),('122','MedicinalProductUndesirableEffect'),('123','MessageDefinition'),('124','MessageHeader'),('125','MolecularSequence'),('126','NamingSystem'),('127','NutritionIntake'),('128','NutritionOrder'),('129','NutritionProduct'),('130','Observation'),('131','ObservationDefinition'),('132','OperationDefinition'),('133','OperationOutcome'),('134','Order'),('135','OrderResponse'),('136','Organization'),('137','OrganizationAffiliation'),('138','PackagedProductDefinition'),('139','Parameters'),('140','Patient'),('141','PaymentNotice'),('142','PaymentReconciliation'),('143','Permission'),('144','Person'),('145','PlanDefinition'),('146','Practitioner'),('147','PractitionerRole'),('148','Procedure'),('149','ProcedureRequest'),('150','ProcessRequest'),('151','ProcessResponse'),('152','Provenance'),('153','Questionnaire'),('154','QuestionnaireResponse'),('155','ReferralRequest'),('156','RegulatedAuthorization'),('157','RelatedPerson'),('158','RequestGroup'),('159','RequestOrchestration'),('160','Requirements'),('161','ResearchDefinition'),('162','ResearchElementDefinition'),('163','ResearchStudy'),('164','ResearchSubject'),('165','RiskAssessment'),('166','RiskEvidenceSynthesis'),('167','Schedule'),('168','SearchParameter'),('169','Sequence'),('170','ServiceDefinition'),('171','ServiceRequest'),('172','Slot'),('173','Specimen'),('174','SpecimenDefinition'),('175','StructureDefinition'),('176','StructureMap'),('177','Subscription'),('178','SubscriptionStatus'),('179','SubscriptionTopic'),('180','Substance'),('181','SubstanceDefinition'),('182','SubstanceNucleicAcid'),('183','SubstancePolymer'),('184','SubstanceProtein'),('185','SubstanceReferenceInformation'),('186','SubstanceSourceMaterial'),('187','SubstanceSpecification'),('188','SupplyDelivery'),('189','SupplyRequest'),('190','Task'),('191','TerminologyCapabilities'),('192','TestPlan'),('193','TestReport'),('194','TestScript'),('195','Transport'),('196','ValueSet'),('197','VerificationResult'),('198','VisionPrescription')");

		result = tasks.getResourceTypeData(DriverTypeEnum.MARIADB_10_1);
		ourLog.debug(result);
		assertThat(result).isEqualTo("('1','Account'),('2','ActivityDefinition'),('3','ActorDefinition'),('4','AdministrableProductDefinition'),('5','AdverseEvent'),('6','AllergyIntolerance'),('7','Appointment'),('8','AppointmentResponse'),('9','ArtifactAssessment'),('10','AuditEvent'),('11','Basic'),('12','Binary'),('13','BiologicallyDerivedProduct'),('14','BiologicallyDerivedProductDispense'),('15','BodySite'),('16','BodyStructure'),('17','Bundle'),('18','CapabilityStatement'),('19','CarePlan'),('20','CareTeam'),('21','CatalogEntry'),('22','ChargeItem'),('23','ChargeItemDefinition'),('24','Citation'),('25','Claim'),('26','ClaimResponse'),('27','ClinicalImpression'),('28','ClinicalUseDefinition'),('29','CodeSystem'),('30','Communication'),('31','CommunicationRequest'),('32','CompartmentDefinition'),('33','Composition'),('34','ConceptMap'),('35','Condition'),('36','ConditionDefinition'),('37','Conformance'),('38','Consent'),('39','Contract'),('40','Coverage'),('41','CoverageEligibilityRequest'),('42','CoverageEligibilityResponse'),('43','DataElement'),('44','DetectedIssue'),('45','Device'),('46','DeviceAssociation'),('47','DeviceComponent'),('48','DeviceDefinition'),('49','DeviceDispense'),('50','DeviceMetric'),('51','DeviceRequest'),('52','DeviceUsage'),('53','DeviceUseRequest'),('54','DeviceUseStatement'),('55','DiagnosticOrder'),('56','DiagnosticReport'),('57','DocumentManifest'),('58','DocumentReference'),('59','EffectEvidenceSynthesis'),('60','EligibilityRequest'),('61','EligibilityResponse'),('62','Encounter'),('63','EncounterHistory'),('64','Endpoint'),('65','EnrollmentRequest'),('66','EnrollmentResponse'),('67','EpisodeOfCare'),('68','EventDefinition'),('69','Evidence'),('70','EvidenceReport'),('71','EvidenceVariable'),('72','ExampleScenario'),('73','ExpansionProfile'),('74','ExplanationOfBenefit'),('75','FamilyMemberHistory'),('76','Flag'),('77','FormularyItem'),('78','GenomicStudy'),('79','Goal'),('80','GraphDefinition'),('81','Group'),('82','GuidanceResponse'),('83','HealthcareService'),('84','ImagingManifest'),('85','ImagingObjectSelection'),('86','ImagingSelection'),('87','ImagingStudy'),('88','Immunization'),('89','ImmunizationEvaluation'),('90','ImmunizationRecommendation'),('91','ImplementationGuide'),('92','Ingredient'),('93','InsurancePlan'),('94','InventoryItem'),('95','InventoryReport'),('96','Invoice'),('97','Library'),('98','Linkage'),('99','List'),('100','Location'),('101','ManufacturedItemDefinition'),('102','Measure'),('103','MeasureReport'),('104','Media'),('105','Medication'),('106','MedicationAdministration'),('107','MedicationDispense'),('108','MedicationKnowledge'),('109','MedicationOrder'),('110','MedicationRequest'),('111','MedicationStatement'),('112','MedicinalProduct'),('113','MedicinalProductAuthorization'),('114','MedicinalProductContraindication'),('115','MedicinalProductDefinition'),('116','MedicinalProductIndication'),('117','MedicinalProductIngredient'),('118','MedicinalProductInteraction'),('119','MedicinalProductManufactured'),('120','MedicinalProductPackaged'),('121','MedicinalProductPharmaceutical'),('122','MedicinalProductUndesirableEffect'),('123','MessageDefinition'),('124','MessageHeader'),('125','MolecularSequence'),('126','NamingSystem'),('127','NutritionIntake'),('128','NutritionOrder'),('129','NutritionProduct'),('130','Observation'),('131','ObservationDefinition'),('132','OperationDefinition'),('133','OperationOutcome'),('134','Order'),('135','OrderResponse'),('136','Organization'),('137','OrganizationAffiliation'),('138','PackagedProductDefinition'),('139','Parameters'),('140','Patient'),('141','PaymentNotice'),('142','PaymentReconciliation'),('143','Permission'),('144','Person'),('145','PlanDefinition'),('146','Practitioner'),('147','PractitionerRole'),('148','Procedure'),('149','ProcedureRequest'),('150','ProcessRequest'),('151','ProcessResponse'),('152','Provenance'),('153','Questionnaire'),('154','QuestionnaireResponse'),('155','ReferralRequest'),('156','RegulatedAuthorization'),('157','RelatedPerson'),('158','RequestGroup'),('159','RequestOrchestration'),('160','Requirements'),('161','ResearchDefinition'),('162','ResearchElementDefinition'),('163','ResearchStudy'),('164','ResearchSubject'),('165','RiskAssessment'),('166','RiskEvidenceSynthesis'),('167','Schedule'),('168','SearchParameter'),('169','Sequence'),('170','ServiceDefinition'),('171','ServiceRequest'),('172','Slot'),('173','Specimen'),('174','SpecimenDefinition'),('175','StructureDefinition'),('176','StructureMap'),('177','Subscription'),('178','SubscriptionStatus'),('179','SubscriptionTopic'),('180','Substance'),('181','SubstanceDefinition'),('182','SubstanceNucleicAcid'),('183','SubstancePolymer'),('184','SubstanceProtein'),('185','SubstanceReferenceInformation'),('186','SubstanceSourceMaterial'),('187','SubstanceSpecification'),('188','SupplyDelivery'),('189','SupplyRequest'),('190','Task'),('191','TerminologyCapabilities'),('192','TestPlan'),('193','TestReport'),('194','TestScript'),('195','Transport'),('196','ValueSet'),('197','VerificationResult'),('198','VisionPrescription')");
	}

	private void insertRow_ResourceIndexedComboStringUnique() {
		myJdbcTemplate.execute(
			"""
				insert into
				HFJ_IDX_CMP_STRING_UNIQ (
				  PID,
				  RES_ID,
				  IDX_STRING)
				values (1, 1, 'Patient?foo=bar')
				""");
	}

	private void insertRow_ResourceTable() {
		myJdbcTemplate.execute(
			"""
					insert into
					HFJ_RESOURCE (
							RES_DELETED_AT,
							RES_VERSION,
							FHIR_ID,
							HAS_TAGS,
							RES_PUBLISHED,
							RES_UPDATED,
							SP_HAS_LINKS,
							HASH_SHA256,
							SP_INDEX_STATUS,
							RES_LANGUAGE,
							SP_CMPSTR_UNIQ_PRESENT,
							SP_COORDS_PRESENT,
							SP_DATE_PRESENT,
							SP_NUMBER_PRESENT,
							SP_QUANTITY_PRESENT,
							SP_STRING_PRESENT,
							SP_TOKEN_PRESENT,
							SP_URI_PRESENT,
							SP_QUANTITY_NRML_PRESENT,
							RES_TYPE,
							RES_VER,
							RES_ID)
							values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""",
			new AbstractLobCreatingPreparedStatementCallback(new DefaultLobHandler()) {
				@Override
				protected void setValues(@Nonnull PreparedStatement thePs, @Nonnull LobCreator theLobCreator) throws SQLException {
					int i = 1;
					thePs.setNull(i++, Types.TIMESTAMP);
					thePs.setString(i++, "R4");
					thePs.setString(i++, "ABC"); // FHIR_ID
					thePs.setBoolean(i++, false);
					thePs.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
					thePs.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
					thePs.setBoolean(i++, false);
					thePs.setNull(i++, Types.VARCHAR);
					thePs.setLong(i++, 1L);
					thePs.setNull(i++, Types.VARCHAR);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false);
					thePs.setBoolean(i++, false); // SP_QUANTITY_NRML_PRESENT
					thePs.setString(i++, "Patient");
					thePs.setLong(i++, 1L);
					thePs.setLong(i, 1L); // RES_ID
				}
			});
	}

	@BeforeAll
	public static void beforeEach() {
		HapiSystemProperties.enableUnitTestMode();
	}

	static BasicDataSource newDataSource() {
		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.h2.Driver());
		retVal.setUrl("jdbc:h2:mem:test_migration-" + UUID.randomUUID() + ";CASE_INSENSITIVE_IDENTIFIERS=TRUE;");
		retVal.setMaxWait(Duration.ofMillis(30000));
		retVal.setUsername("");
		retVal.setPassword("");
		retVal.setMaxTotal(5);

		return retVal;
	}

}
