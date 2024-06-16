package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.api.IpsContext;
import ca.uhn.fhir.jpa.ips.api.Section;
import ca.uhn.fhir.jpa.ips.jpa.DefaultJpaIpsGenerationStrategy;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.test.utilities.HtmlUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DeviceUseStatement;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.htmlunit.html.HtmlTableRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.ips.generator.IpsGenerationR4Test.findEntryResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test verifies various IPS generation logic without using a full
 * JPA backend.
 */
@ExtendWith(MockitoExtension.class)
public class IpsGeneratorSvcImplTest {

	public static final String MEDICATION_ID = "Medication/tyl";
	public static final String MEDICATION_STATEMENT_ID = "MedicationStatement/meds";
	public static final String MEDICATION_STATEMENT_ID2 = "MedicationStatement/meds2";
	public static final String PATIENT_ID = "Patient/123";
	public static final String ENCOUNTER_ID = "Encounter/encounter";
	private static final List<Class<? extends IBaseResource>> RESOURCE_TYPES = Lists.newArrayList(
		AllergyIntolerance.class,
		CarePlan.class,
		Condition.class,
		Consent.class,
		ClinicalImpression.class,
		DeviceUseStatement.class,
		DiagnosticReport.class,
		Immunization.class,
		MedicationRequest.class,
		MedicationStatement.class,
		MedicationAdministration.class,
		MedicationDispense.class,
		Observation.class,
		Patient.class,
		Procedure.class
	);
	private static final Logger ourLog = LoggerFactory.getLogger(IpsGeneratorSvcImplTest.class);
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final DaoRegistry myDaoRegistry = new DaoRegistry(myFhirContext);
	private IIpsGeneratorSvc mySvc;
	private DefaultJpaIpsGenerationStrategy myStrategy;

	@BeforeEach
	public void beforeEach() {
		myDaoRegistry.setResourceDaos(Collections.emptyList());
	}

	private void initializeGenerationStrategy() {
		initializeGenerationStrategy(List.of());
	}

	private void initializeGenerationStrategy(List<Function<Section, Section>> theGlobalSectionCustomizers) {
		myStrategy = new DefaultJpaIpsGenerationStrategy() {
			@Override
			public IIdType massageResourceId(@Nullable IpsContext theIpsContext, @Nonnull IBaseResource theResource) {
				return IdType.newRandomUuid();
			}
		};

		myStrategy.setFhirContext(myFhirContext);
		myStrategy.setDaoRegistry(myDaoRegistry);

		if (theGlobalSectionCustomizers != null) {
			for (var next : theGlobalSectionCustomizers) {
				myStrategy.addGlobalSectionCustomizer(next);
			}
		}
		mySvc = new IpsGeneratorSvcImpl(myFhirContext, myStrategy);
	}

	@Test
	public void testGenerateIps() {
		// Setup
		initializeGenerationStrategy();
		registerResourceDaosForSmallPatientSet();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new TokenParam("http://foo", "bar"), null);

		// Verify
		ourLog.info("Generated IPS:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		List<String> contentResourceTypes = toEntryResourceTypeStrings(outcome);
		assertThat(contentResourceTypes).as(contentResourceTypes.toString()).containsExactly("Composition", "Patient", "AllergyIntolerance", "MedicationStatement", "MedicationStatement", "MedicationStatement", "Condition", "Condition", "Condition", "Organization");

		Composition composition = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section;

		// Allergy and Intolerances has no content
		section = composition.getSection().get(0);
		assertEquals("Allergies and Intolerances", section.getTitle());
		assertThat(section.getText().getDivAsString()).contains("No information about allergies");

		// Medication Summary has a resource
		section = composition.getSection().get(1);
		assertEquals("Medication List", section.getTitle());
		assertThat(section.getText().getDivAsString()).contains("Oral use");

		// Composition itself should also have a narrative
		String compositionNarrative = composition.getText().getDivAsString();
		ourLog.info("Composition narrative: {}", compositionNarrative);
		assertThat(compositionNarrative).isEqualTo(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\"><h1>International Patient Summary Document</h1></div>"
		);
	}

	@Test
	public void testAllergyIntolerance_OnsetTypes() throws IOException {
		// Setup Patient
		initializeGenerationStrategy();
		registerPatientDaoWithRead();

		AllergyIntolerance allergy1 = new AllergyIntolerance();
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(allergy1, BundleEntrySearchModeEnum.MATCH);
		allergy1.setId("AllergyIntolerance/1");
		allergy1.getCode().addCoding().setCode("123").setDisplay("Some Code");
		allergy1.addReaction().addNote().setTime(new Date());
		allergy1.setOnset(new DateTimeType("2020-02-03T11:22:33Z"));
		AllergyIntolerance allergy2 = new AllergyIntolerance();
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(allergy2, BundleEntrySearchModeEnum.MATCH);
		allergy2.setId("AllergyIntolerance/2");
		allergy2.getCode().addCoding().setCode("123").setDisplay("Some Code");
		allergy2.addReaction().addNote().setTime(new Date());
		allergy2.setOnset(new StringType("Some Onset"));
		AllergyIntolerance allergy3 = new AllergyIntolerance();
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(allergy3, BundleEntrySearchModeEnum.MATCH);
		allergy3.setId("AllergyIntolerance/3");
		allergy3.getCode().addCoding().setCode("123").setDisplay("Some Code");
		allergy3.addReaction().addNote().setTime(new Date());
		allergy3.setOnset(null);
		IFhirResourceDao<AllergyIntolerance> allergyDao = registerResourceDaoWithNoData(AllergyIntolerance.class);
		when(allergyDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(allergy1, allergy2, allergy3)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		// Verify
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = findSection(compositions, DefaultJpaIpsGenerationStrategy.SECTION_CODE_ALLERGY_INTOLERANCE);

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertThat(tables).hasSize(1);
		HtmlTable table = (HtmlTable) tables.get(0);
		int onsetIndex = 6;
		assertEquals("Onset", table.getHeader().getRows().get(0).getCell(onsetIndex).asNormalizedText());
		assertEquals(new DateTimeType("2020-02-03T11:22:33Z").getValue().toString(), table.getBodies().get(0).getRows().get(0).getCell(onsetIndex).asNormalizedText());
		assertEquals("Some Onset", table.getBodies().get(0).getRows().get(1).getCell(onsetIndex).asNormalizedText());
		assertEquals("", table.getBodies().get(0).getRows().get(2).getCell(onsetIndex).asNormalizedText());
	}

	@Test
	public void testAllergyIntolerance_MissingElements() throws IOException {
		// Setup Patient
		initializeGenerationStrategy();
		registerPatientDaoWithRead();

		AllergyIntolerance allergy = new AllergyIntolerance();
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(allergy, BundleEntrySearchModeEnum.MATCH);
		allergy.setId("AllergyIntolerance/1");
		allergy.getCode().addCoding().setCode("123").setDisplay("Some Code");
		allergy.addReaction().addNote().setTime(new Date());
		IFhirResourceDao<AllergyIntolerance> allergyDao = registerResourceDaoWithNoData(AllergyIntolerance.class);
		when(allergyDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(allergy)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		// Verify
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = findSection(compositions, DefaultJpaIpsGenerationStrategy.SECTION_CODE_ALLERGY_INTOLERANCE);

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertThat(tables).hasSize(1);
	}

	@Test
	public void testMedicationSummary_MedicationStatementWithMedicationReference() throws IOException {
		// Setup Patient
		initializeGenerationStrategy();
		registerPatientDaoWithRead();

		// Setup Medication + MedicationStatement
		Medication medication = createSecondaryMedication(MEDICATION_ID);
		MedicationStatement medicationStatement = createPrimaryMedicationStatement(MEDICATION_ID, MEDICATION_STATEMENT_ID);
		IFhirResourceDao<MedicationStatement> medicationStatementDao = registerResourceDaoWithNoData(MedicationStatement.class);
		when(medicationStatementDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(medicationStatement, medication)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		// Verify Bundle Contents
		List<String> contentResourceTypes = toEntryResourceTypeStrings(outcome);
		assertThat(contentResourceTypes).as(contentResourceTypes.toString()).containsExactly("Composition", "Patient", "AllergyIntolerance", "MedicationStatement", "Medication", "Condition", "Organization");
		MedicationStatement actualMedicationStatement = (MedicationStatement) outcome.getEntry().get(3).getResource();
		Medication actualMedication = (Medication) outcome.getEntry().get(4).getResource();
		assertThat(actualMedication.getId()).startsWith("urn:uuid:");
		assertEquals(actualMedication.getId(), actualMedicationStatement.getMedicationReference().getReference());

		// Verify
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = findSection(compositions, DefaultJpaIpsGenerationStrategy.SECTION_CODE_MEDICATION_SUMMARY);

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertThat(tables).hasSize(1);
		HtmlTable table = (HtmlTable) tables.get(0);
		HtmlTableRow row = table.getBodies().get(0).getRows().get(0);
		assertEquals("Tylenol", row.getCell(0).asNormalizedText());
		assertEquals("Active", row.getCell(1).asNormalizedText());
		assertEquals("Oral", row.getCell(2).asNormalizedText());
		assertEquals("DAW", row.getCell(3).asNormalizedText());
		assertThat(row.getCell(4).asNormalizedText()).contains("2023");
	}

	@Test
	public void testMedicationSummary_MedicationRequestWithNoMedication() throws IOException {
		// Setup Patient
		initializeGenerationStrategy();
		registerPatientDaoWithRead();

		// Setup Medication + MedicationStatement
		MedicationRequest medicationRequest = new MedicationRequest();
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(medicationRequest, BundleEntrySearchModeEnum.MATCH);
		medicationRequest.setId(MEDICATION_STATEMENT_ID);
		medicationRequest.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
		IFhirResourceDao<MedicationRequest> medicationRequestDao = registerResourceDaoWithNoData(MedicationRequest.class);
		when(medicationRequestDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(medicationRequest)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		// Verify
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = findSection(compositions, DefaultJpaIpsGenerationStrategy.SECTION_CODE_MEDICATION_SUMMARY);

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertThat(tables).hasSize(1);
		HtmlTable table = (HtmlTable) tables.get(0);
		HtmlTableRow row = table.getBodies().get(0).getRows().get(0);
		assertEquals("", row.getCell(0).asNormalizedText());
		assertEquals("Active", row.getCell(1).asNormalizedText());
		assertEquals("", row.getCell(2).asNormalizedText());
		assertEquals("", row.getCell(3).asNormalizedText());
	}

	@Test
	public void testMedicationSummary_DuplicateSecondaryResources() {
		// Setup Patient
		initializeGenerationStrategy(
			List.of(t->Section.newBuilder(t).withNoInfoGenerator(null).build())
		);
		registerPatientDaoWithRead();

		// Setup Medication + MedicationStatement
		Medication medication = createSecondaryMedication(MEDICATION_ID);
		MedicationStatement medicationStatement = createPrimaryMedicationStatement(MEDICATION_ID, MEDICATION_STATEMENT_ID);
		Medication medication2 = createSecondaryMedication(MEDICATION_ID); // same ID again (could happen if we span multiple pages of results)
		MedicationStatement medicationStatement2 = createPrimaryMedicationStatement(MEDICATION_ID, MEDICATION_STATEMENT_ID2);
		IFhirResourceDao<MedicationStatement> medicationStatementDao = registerResourceDaoWithNoData(MedicationStatement.class);
		when(medicationStatementDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(medicationStatement, medication, medicationStatement2, medication2)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		// Verify Bundle Contents
		List<String> contentResourceTypes = toEntryResourceTypeStrings(outcome);
		assertThat(contentResourceTypes).as(contentResourceTypes.toString()).containsExactly("Composition", "Patient", "MedicationStatement", "Medication", "MedicationStatement", "Organization");

	}

	/**
	 * Make sure that if a resource is added as a secondary resource but then gets included as a
	 * primary resource, we include it.
	 */
	@Test
	public void testMedicationSummary_ResourceAppearsAsSecondaryThenPrimary() throws IOException {
		// Setup Patient
		initializeGenerationStrategy(
			List.of(t->Section.newBuilder(t).withNoInfoGenerator(null).build())
		);
		registerPatientDaoWithRead();

		// Setup Medication + MedicationStatement
		Medication medication = createSecondaryMedication(MEDICATION_ID);
		MedicationStatement medicationStatement = createPrimaryMedicationStatement(MEDICATION_ID, MEDICATION_STATEMENT_ID);
		medicationStatement.addDerivedFrom().setReference(MEDICATION_STATEMENT_ID2);
		MedicationStatement medicationStatement2 = createPrimaryMedicationStatement(MEDICATION_ID, MEDICATION_STATEMENT_ID2);
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(medicationStatement2, BundleEntrySearchModeEnum.INCLUDE);
		MedicationStatement medicationStatement3 = createPrimaryMedicationStatement(MEDICATION_ID, MEDICATION_STATEMENT_ID2);
		IFhirResourceDao<MedicationStatement> medicationStatementDao = registerResourceDaoWithNoData(MedicationStatement.class);
		when(medicationStatementDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(medicationStatement, medication, medicationStatement2, medicationStatement3)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		// Verify Bundle Contents
		List<String> contentResourceTypes = toEntryResourceTypeStrings(outcome);
		assertThat(contentResourceTypes).as(contentResourceTypes.toString()).containsExactly("Composition", "Patient", "MedicationStatement", "Medication", "MedicationStatement", "Organization");

		// Verify narrative - should have 2 rows (one for each primary MedicationStatement)
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = findSection(compositions, DefaultJpaIpsGenerationStrategy.SECTION_CODE_MEDICATION_SUMMARY);

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertEquals(1, tables.size());
		HtmlTable table = (HtmlTable) tables.get(0);
		assertEquals(2, table.getBodies().get(0).getRows().size());
	}

	/**
	 * If there is no contents in one of the 2 medication summary tables it should be
	 * omitted
	 */
	@Test
	public void testMedicationSummary_OmitMedicationRequestTable() throws IOException {
		// Setup Patient
		initializeGenerationStrategy(
			List.of(t->Section.newBuilder(t).withNoInfoGenerator(null).build())
		);
		registerPatientDaoWithRead();

		// Setup Medication + MedicationStatement
		Medication medication = createSecondaryMedication(MEDICATION_ID);
		MedicationStatement medicationStatement = createPrimaryMedicationStatement(MEDICATION_ID, MEDICATION_STATEMENT_ID);
		medicationStatement.addDerivedFrom().setReference(MEDICATION_STATEMENT_ID2);
		MedicationStatement medicationStatement2 = createPrimaryMedicationStatement(MEDICATION_ID, MEDICATION_STATEMENT_ID2);
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(medicationStatement2, BundleEntrySearchModeEnum.INCLUDE);
		MedicationStatement medicationStatement3 = createPrimaryMedicationStatement(MEDICATION_ID, MEDICATION_STATEMENT_ID2);
		IFhirResourceDao<MedicationStatement> medicationStatementDao = registerResourceDaoWithNoData(MedicationStatement.class);
		when(medicationStatementDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(medicationStatement, medication, medicationStatement2, medicationStatement3)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		// Verify narrative - should have 2 rows (one for each primary MedicationStatement)
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = findSection(compositions, DefaultJpaIpsGenerationStrategy.SECTION_CODE_MEDICATION_SUMMARY);

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertEquals(1, tables.size());
		HtmlTable table = (HtmlTable) tables.get(0);
		assertEquals(2, table.getBodies().get(0).getRows().size());
	}

	@Test
	public void testMedicalDevices_DeviceUseStatementWithDevice() throws IOException {
		// Setup Patient
		initializeGenerationStrategy();
		registerPatientDaoWithRead();

		// Setup Medication + MedicationStatement
		Device device = new Device();
		device.setId(new IdType("Device/pm"));
		device.getType().addCoding().setDisplay("Pacemaker");
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(device, BundleEntrySearchModeEnum.INCLUDE);

		DeviceUseStatement deviceUseStatement = new DeviceUseStatement();
		deviceUseStatement.setId("DeviceUseStatement/dus");
		deviceUseStatement.setDevice(new Reference("Device/pm"));
		deviceUseStatement.setStatus(DeviceUseStatement.DeviceUseStatementStatus.ACTIVE);
		deviceUseStatement.addNote().setText("This is some note text");
		deviceUseStatement.setRecordedOnElement(new DateTimeType("2023-01-01T12:22:33Z"));
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(deviceUseStatement, BundleEntrySearchModeEnum.MATCH);

		IFhirResourceDao<DeviceUseStatement> deviceUseStatementDao = registerResourceDaoWithNoData(DeviceUseStatement.class);
		when(deviceUseStatementDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(deviceUseStatement, device)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		// Verify
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = findSection(compositions, DefaultJpaIpsGenerationStrategy.SECTION_CODE_MEDICAL_DEVICES);

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertThat(tables).hasSize(1);
		HtmlTable table = (HtmlTable) tables.get(0);
		HtmlTableRow row = table.getBodies().get(0).getRows().get(0);
		assertEquals("Pacemaker", row.getCell(0).asNormalizedText());
		assertEquals("ACTIVE", row.getCell(1).asNormalizedText());
		assertEquals("This is some note text", row.getCell(2).asNormalizedText());
	}

	@Test
	public void testImmunizations() throws IOException {
		// Setup Patient
		initializeGenerationStrategy();
		registerPatientDaoWithRead();

		// Setup Medication + MedicationStatement
		Organization org = new Organization();
		org.setId(new IdType("Organization/pfizer"));
		org.setName("Pfizer Inc");
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(org, BundleEntrySearchModeEnum.INCLUDE);

		Immunization immunization = new Immunization();
		immunization.setId("Immunization/imm");
		immunization.getVaccineCode().addCoding().setDisplay("SpikeVax");
		immunization.setStatus(Immunization.ImmunizationStatus.COMPLETED);
		immunization.addProtocolApplied().setDoseNumber(new PositiveIntType(2));
		immunization.addProtocolApplied().setDoseNumber(new PositiveIntType(4));
		immunization.setManufacturer(new Reference("Organization/pfizer"));
		immunization.setLotNumber("35");
		immunization.addNote().setText("Hello World");
		immunization.setOccurrence(new DateTimeType("2023-01-01T11:22:33Z"));
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(immunization, BundleEntrySearchModeEnum.MATCH);

		IFhirResourceDao<Immunization> deviceUseStatementDao = registerResourceDaoWithNoData(Immunization.class);
		when(deviceUseStatementDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(immunization, org)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		// Verify
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = findSection(compositions, DefaultJpaIpsGenerationStrategy.SECTION_CODE_IMMUNIZATIONS);

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertThat(tables).hasSize(1);
		HtmlTable table = (HtmlTable) tables.get(0);
		HtmlTableRow row = table.getBodies().get(0).getRows().get(0);
		assertEquals("SpikeVax", row.getCell(0).asNormalizedText());
		assertEquals("COMPLETED", row.getCell(1).asNormalizedText());
		assertEquals("2 , 4", row.getCell(2).asNormalizedText());
		assertEquals("Pfizer Inc", row.getCell(3).asNormalizedText());
		assertEquals("35", row.getCell(4).asNormalizedText());
		assertEquals("Hello World", row.getCell(5).asNormalizedText());
		assertThat(row.getCell(6).asNormalizedText()).contains("2023");
	}

	@Test
	public void testReferencesUpdatedInSecondaryInclusions() {
		// Setup Patient
		initializeGenerationStrategy();
		registerPatientDaoWithRead();

		// Setup Medication + MedicationStatement
		Encounter encounter = new Encounter();
		encounter.setId(new IdType(ENCOUNTER_ID));
		encounter.setSubject(new Reference(PATIENT_ID));
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(encounter, BundleEntrySearchModeEnum.INCLUDE);

		Condition conditionActive = new Condition();
		conditionActive.setId("Condition/conditionActive");
		conditionActive.getClinicalStatus().addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")
			.setCode("active");
		conditionActive.setSubject(new Reference(PATIENT_ID));
		conditionActive.setEncounter(new Reference(ENCOUNTER_ID));
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(conditionActive, BundleEntrySearchModeEnum.MATCH);

		Condition conditionResolved = new Condition();
		conditionResolved.setId("Condition/conditionResolved");
		conditionResolved.getClinicalStatus().addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")
			.setCode("resolved");
		conditionResolved.setSubject(new Reference(PATIENT_ID));
		conditionResolved.setEncounter(new Reference(ENCOUNTER_ID));
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(conditionResolved, BundleEntrySearchModeEnum.MATCH);

		// Conditions will be loaded from two sections (problem list and illness history) so
		// we return an active condition the first time and a resolved one the second
		IFhirResourceDao<Condition> conditionDao = registerResourceDaoWithNoData(Condition.class);
		when(conditionDao.search(any(), any())).thenReturn(
			new SimpleBundleProvider(Lists.newArrayList(conditionActive, encounter)),
			new SimpleBundleProvider(Lists.newArrayList(conditionResolved, encounter))
		);

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify cross-references
		Patient addedPatient = findEntryResource(outcome, Patient.class, 0, 1);
		assertThat(addedPatient.getId()).startsWith("urn:uuid:");
		Condition addedCondition = findEntryResource(outcome, Condition.class, 0, 2);
		assertThat(addedCondition.getId()).startsWith("urn:uuid:");
		Condition addedCondition2 = findEntryResource(outcome, Condition.class, 1, 2);
		assertThat(addedCondition2.getId()).startsWith("urn:uuid:");
		Encounter addedEncounter = findEntryResource(outcome, Encounter.class, 0, 1);
		assertThat(addedEncounter.getId()).startsWith("urn:uuid:");
		MedicationStatement addedMedicationStatement = findEntryResource(outcome, MedicationStatement.class, 0, 1);
		assertThat(addedMedicationStatement.getId()).startsWith("urn:uuid:");
		assertEquals("no-medication-info", addedMedicationStatement.getMedicationCodeableConcept().getCodingFirstRep().getCode());
		assertEquals(addedPatient.getId(), addedCondition.getSubject().getReference());
		assertEquals(addedEncounter.getId(), addedCondition.getEncounter().getReference());
		assertEquals(addedPatient.getId(), addedEncounter.getSubject().getReference());
		assertEquals(addedPatient.getId(), addedMedicationStatement.getSubject().getReference());

		// Verify sections
		ourLog.info("Resource: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		verify(conditionDao, times(2)).search(any(), any());
		Composition composition = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent problemListSection = findSection(composition, DefaultJpaIpsGenerationStrategy.SECTION_CODE_PROBLEM_LIST);
		assertEquals(addedCondition.getId(), problemListSection.getEntry().get(0).getReference());
		assertEquals(1, problemListSection.getEntry().size());
		Composition.SectionComponent illnessHistorySection = findSection(composition, DefaultJpaIpsGenerationStrategy.SECTION_CODE_ILLNESS_HISTORY);
		assertEquals(addedCondition2.getId(), illnessHistorySection.getEntry().get(0).getReference());
		assertEquals(1, illnessHistorySection.getEntry().size());
	}

	@Test
	public void testPatientIsReturnedAsAnIncludeResource() {
		// Setup Patient
		initializeGenerationStrategy();
		registerPatientDaoWithRead();

		// Setup Condition
		Condition conditionActive = new Condition();
		conditionActive.setId("Condition/conditionActive");
		conditionActive.getClinicalStatus().addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")
			.setCode("active");
		conditionActive.setSubject(new Reference(PATIENT_ID));
		conditionActive.setEncounter(new Reference(ENCOUNTER_ID));
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(conditionActive, BundleEntrySearchModeEnum.MATCH);

		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(patient, BundleEntrySearchModeEnum.INCLUDE);

		IFhirResourceDao<Condition> conditionDao = registerResourceDaoWithNoData(Condition.class);
		when(conditionDao.search(any(), any())).thenReturn(
			new SimpleBundleProvider(Lists.newArrayList(conditionActive, patient)),
			new SimpleBundleProvider()
		);

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType(PATIENT_ID), null);

		List<String> resources = outcome
			.getEntry()
			.stream()
			.map(t -> t.getResource().getResourceType().name())
			.collect(Collectors.toList());
		assertThat(resources).as(resources.toString()).containsExactly("Composition", "Patient", "AllergyIntolerance", "MedicationStatement", "Condition", "Organization");
	}

	@Test
	public void testSelectGenerator() {
		IIpsGenerationStrategy strategy1 = mock(IIpsGenerationStrategy.class);
		when(strategy1.getBundleProfile()).thenReturn("http://1");
		IIpsGenerationStrategy strategy2 = mock(IIpsGenerationStrategy.class);
		when(strategy2.getBundleProfile()).thenReturn("http://2");
		IpsGeneratorSvcImpl svc = new IpsGeneratorSvcImpl(myFhirContext, List.of(strategy1, strategy2));

		assertSame(strategy1, svc.selectGenerationStrategy("http://1"));
		assertSame(strategy1, svc.selectGenerationStrategy(null));
		assertSame(strategy1, svc.selectGenerationStrategy("http://foo"));
		assertSame(strategy2, svc.selectGenerationStrategy("http://2"));
	}

	@Nonnull
	private Composition.SectionComponent findSection(Composition compositions, String theSectionCode) {
		return compositions
			.getSection()
			.stream()
			.filter(t -> t.getCode().getCodingFirstRep().getCode().equals(theSectionCode))
			.findFirst()
			.orElseThrow();
	}

	private void registerPatientDaoWithRead() {
		IFhirResourceDao<Patient> patientDao = registerResourceDaoWithNoData(Patient.class);
		Patient patient = new Patient();
		patient.setId(PATIENT_ID);
		when(patientDao.read(any(), any(RequestDetails.class))).thenReturn(patient);
	}

	private void registerRemainingResourceDaos() {
		for (var next : RESOURCE_TYPES) {
			if (!myDaoRegistry.isResourceTypeSupported(myFhirContext.getResourceType(next))) {
				IFhirResourceDao<? extends IBaseResource> dao = registerResourceDaoWithNoData(next);
				when(dao.search(any(), any())).thenReturn(new SimpleBundleProvider());
			}
		}
	}

	private IBundleProvider bundleProviderWithAllOfType(Bundle theSourceData, Class<? extends IBaseResource> theType) {
		List<Resource> resources = theSourceData
			.getEntry()
			.stream()
			.filter(t -> t.getResource() != null && theType.isAssignableFrom(t.getResource().getClass()))
			.map(Bundle.BundleEntryComponent::getResource)
			.collect(Collectors.toList());
		resources.forEach(t -> ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(t, BundleEntrySearchModeEnum.MATCH));
		return new SimpleBundleProvider(resources);
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	private <T extends IBaseResource> IFhirResourceDao<T> registerResourceDaoWithNoData(@Nonnull Class<T> theType) {
		IFhirResourceDao<T> dao = mock(IFhirResourceDao.class);
		when(dao.getResourceType()).thenReturn(theType);
		myDaoRegistry.register(dao);
		return dao;
	}

	@SuppressWarnings("rawtypes")
	private void registerResourceDaosForSmallPatientSet() {
		Bundle sourceData = ClasspathUtil.loadCompressedResource(myFhirContext, Bundle.class, "/small-patient-everything.json.gz");

		for (var nextType : IpsGeneratorSvcImplTest.RESOURCE_TYPES) {
			IFhirResourceDao dao = registerResourceDaoWithNoData(nextType);
			when(dao.search(any(), any())).thenReturn(bundleProviderWithAllOfType(sourceData, nextType));
		}

	}

	@Nonnull
	private static List<String> toEntryResourceTypeStrings(Bundle outcome) {
		return outcome
			.getEntry()
			.stream()
			.map(t -> t.getResource().getResourceType().name())
			.collect(Collectors.toList());
	}

	@Nonnull
	private static Medication createSecondaryMedication(String theMedicationId) {
		Medication medication = new Medication();
		medication.setId(new IdType(theMedicationId));
		medication.getCode().addCoding().setDisplay("Tylenol");
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(medication, BundleEntrySearchModeEnum.INCLUDE);
		return medication;
	}

	@Nonnull
	private static MedicationStatement createPrimaryMedicationStatement(String theMedicationId, String medicationStatementId) {
		MedicationStatement medicationStatement = new MedicationStatement();
		medicationStatement.setId(medicationStatementId);
		medicationStatement.setMedication(new Reference(theMedicationId));
		medicationStatement.setStatus(MedicationStatement.MedicationStatementStatus.ACTIVE);
		medicationStatement.getDosageFirstRep().getRoute().addCoding().setDisplay("Oral");
		medicationStatement.getDosageFirstRep().setText("DAW");
		medicationStatement.setEffective(new DateTimeType("2023-01-01T11:22:33Z"));
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(medicationStatement, BundleEntrySearchModeEnum.MATCH);
		return medicationStatement;
	}

}
