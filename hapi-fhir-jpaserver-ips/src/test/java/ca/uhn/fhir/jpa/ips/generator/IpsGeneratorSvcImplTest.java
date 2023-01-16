package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.api.IpsSectionEnum;
import ca.uhn.fhir.jpa.ips.api.SectionRegistry;
import ca.uhn.fhir.jpa.ips.strategy.DefaultIpsGenerationStrategy;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.test.utilities.HtmlUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IpsGeneratorSvcImplTest {

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
	private SectionRegistry mySectionRegistry;

	@BeforeEach
	public void beforeEach() {
		myDaoRegistry.setResourceDaos(Collections.emptyList());

		IIpsGenerationStrategy strategy = new DefaultIpsGenerationStrategy();
		mySectionRegistry = strategy.getSectionRegistry();
		mySvc = new IpsGeneratorSvcImpl(myFhirContext, strategy, myDaoRegistry);
	}

	@Test
	public void testGenerateIps() {
		// Setup
		registerResourceDaosForSmallPatientSet();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new TokenParam("http://foo", "bar"));

		// Verify
		ourLog.info("Generated IPS:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		List<String> contentResourceTypes = toEntryResourceTypeStrings(outcome);
		assertThat(contentResourceTypes.toString(), contentResourceTypes,
			Matchers.contains("Composition", "Patient", "AllergyIntolerance", "MedicationStatement", "MedicationStatement", "MedicationStatement", "Condition", "Condition", "Condition", "Organization"));

		Composition composition = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section;

		// Allergy and Intolerances has no content
		section = composition.getSection().get(0);
		assertEquals("Allergies and Intolerances", section.getTitle());
		assertThat(section.getText().getDivAsString(),
			containsString("No information about allergies"));

		// Medication Summary has a resource
		section = composition.getSection().get(1);
		assertEquals("Medication List", section.getTitle());
		assertThat(section.getText().getDivAsString(),
			containsString("Oral use"));
	}

	@Nonnull
	private static List<String> toEntryResourceTypeStrings(Bundle outcome) {
		List<String> contentResourceTypes = outcome
			.getEntry()
			.stream()
			.map(t -> t.getResource().getResourceType().name())
			.collect(Collectors.toList());
		return contentResourceTypes;
	}


	@Test
	public void testMedicationSummary_MedicationStatementWithMedicationReference() throws IOException {
		// Setup Patient
		IFhirResourceDao<Patient> patientDao = registerResourceDaoWithNoData(Patient.class);
		Patient patient = new Patient();
		patient.setId("Patient/123");
		when(patientDao.read(any(), any())).thenReturn(patient);

		// Setup Medication + MedicationStatement
		Medication medication = new Medication();
		medication.setId(new IdType("Medication/tyl"));
		medication.getCode().addCoding().setDisplay("Tylenol");
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(medication, BundleEntrySearchModeEnum.INCLUDE);

		MedicationStatement medicationStatement = new MedicationStatement();
		medicationStatement.setId("MedicationStatement/meds");
		medicationStatement.setMedication(new Reference("Medication/tyl"));
		medicationStatement.setStatus(MedicationStatement.MedicationStatementStatus.ACTIVE);
		medicationStatement.getDosageFirstRep().getRoute().addCoding().setDisplay("Oral");
		medicationStatement.getDosageFirstRep().setText("DAW");
		medicationStatement.setEffective(new DateTimeType("2023-01-01T11:22:33Z"));
		ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(medicationStatement, BundleEntrySearchModeEnum.MATCH);

		IFhirResourceDao<MedicationStatement> medicationStatementDao = registerResourceDaoWithNoData(MedicationStatement.class);
		when(medicationStatementDao.search(any(), any())).thenReturn(new SimpleBundleProvider(Lists.newArrayList(medicationStatement, medication)));

		registerRemainingResourceDaos();

		// Test
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType("Patient/123"));

		// Verify Bundle Contents
		List<String> contentResourceTypes = toEntryResourceTypeStrings(outcome);
		assertThat(contentResourceTypes.toString(), contentResourceTypes,
			Matchers.contains("Composition", "Patient", "AllergyIntolerance", "MedicationStatement", "Medication", "Condition", "Organization"));
		MedicationStatement actualMedicationStatement = (MedicationStatement) outcome.getEntry().get(3).getResource();
		Medication actualMedication = (Medication) outcome.getEntry().get(4).getResource();
		assertThat(actualMedication.getId(), startsWith("urn:uuid:"));
		assertEquals(actualMedication.getId(), actualMedicationStatement.getMedicationReference().getReference());

		// Verify
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = compositions
			.getSection()
			.stream()
			.filter(t -> t.getTitle().equals(mySectionRegistry.getSection(IpsSectionEnum.MEDICATION_SUMMARY).getTitle()))
			.findFirst()
			.orElseThrow();

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertEquals(2, tables.size()); // FIXME: should be 1
		HtmlTable table = (HtmlTable) tables.get(1);
		HtmlTableRow row = table.getBodies().get(0).getRows().get(0);
		assertEquals("Tylenol", row.getCell(0).asNormalizedText());
		assertEquals("Active", row.getCell(1).asNormalizedText());
		assertEquals("Oral", row.getCell(2).asNormalizedText());
		assertEquals("DAW", row.getCell(3).asNormalizedText());
		assertThat(row.getCell(4).asNormalizedText(), containsString("2023"));
	}

	@Test
	public void testMedicalDevices_DeviceUseStatementWithDevice() throws IOException {
		// Setup Patient
		IFhirResourceDao<Patient> patientDao = registerResourceDaoWithNoData(Patient.class);
		Patient patient = new Patient();
		patient.setId("Patient/123");
		when(patientDao.read(any(), any())).thenReturn(patient);

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
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType("Patient/123"));

		// Verify
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = compositions
			.getSection()
			.stream()
			.filter(t -> t.getTitle().equals(mySectionRegistry.getSection(IpsSectionEnum.MEDICAL_DEVICES).getTitle()))
			.findFirst()
			.orElseThrow();

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertEquals(1, tables.size());
		HtmlTable table = (HtmlTable) tables.get(0);
		HtmlTableRow row = table.getBodies().get(0).getRows().get(0);
		assertEquals("Pacemaker", row.getCell(0).asNormalizedText());
		assertEquals("ACTIVE", row.getCell(1).asNormalizedText());
		assertEquals("This is some note text", row.getCell(2).asNormalizedText());
	}

	@Test
	public void testImmunizations() throws IOException {
		// Setup Patient
		IFhirResourceDao<Patient> patientDao = registerResourceDaoWithNoData(Patient.class);
		Patient patient = new Patient();
		patient.setId("Patient/123");
		when(patientDao.read(any(), any())).thenReturn(patient);

		// Setup Medication + MedicationStatement
		Organization org = new Organization();
		org.setId(new IdType("Organization/pfizer"));
		org.setName("Pfizer");
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
		Bundle outcome = (Bundle) mySvc.generateIps(new SystemRequestDetails(), new IdType("Patient/123"));

		// Verify
		Composition compositions = (Composition) outcome.getEntry().get(0).getResource();
		Composition.SectionComponent section = compositions
			.getSection()
			.stream()
			.filter(t -> t.getTitle().equals(mySectionRegistry.getSection(IpsSectionEnum.IMMUNIZATIONS).getTitle()))
			.findFirst()
			.orElseThrow();

		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(section.getText().getDivAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());

		DomNodeList<DomElement> tables = narrativeHtml.getElementsByTagName("table");
		assertEquals(1, tables.size());
		HtmlTable table = (HtmlTable) tables.get(0);
		HtmlTableRow row = table.getBodies().get(0).getRows().get(0);
		assertEquals("SpikeVax", row.getCell(0).asNormalizedText());
		assertEquals("COMPLETED", row.getCell(1).asNormalizedText());
		assertEquals("2 , 4", row.getCell(2).asNormalizedText());
		assertEquals("Pfizer", row.getCell(3).asNormalizedText());
		assertEquals("35", row.getCell(4).asNormalizedText());
		assertEquals("Hello World", row.getCell(5).asNormalizedText());
		assertThat(row.getCell(6).asNormalizedText(), containsString("2023"));
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
		resources.forEach(t->ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(t, BundleEntrySearchModeEnum.MATCH));
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

}
