package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep3HandleHierarchyTest.renderHierarchy;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep4HandleAnswerListsTest {

	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	private IJobDataSink<ImportLoincFileSetJson> myDataSink;
	@Mock
	private IJobStepExecutionServices myJobExecutionServices;
	@Mock
	private JobDefinition<LoincJobImportParameters> myJobDefinition;
	@Mock
	private IFhirResourceDaoValueSet<ValueSet> myValueSetDao;

	@InjectMocks
	private ImportLoincStep4HandleAnswerLists mySvc;

	@Captor
	private ArgumentCaptor<IBaseResource> myCodeSystemCaptor;
	@Captor
	private ArgumentCaptor<ImportLoincFileSetJson> myFileSetCaptor;
	@Captor
	private ArgumentCaptor<ValueSet> myValueSetCaptor;


	@Test
	void testProcess() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/AnswerFile/AnswerList.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkAttachmentIdForCurrentStepId("my-chunk-attachment-id");
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));
		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String hierarchy = renderHierarchy(cs);
		String expected = """
			-LL1000-0
			-LA13825-7
			-LA13838-0
			-LA13892-7
			-LL1001-8
			-LA6270-8
			-LA13836-4
			-LA13834-9
			-LA13853-9
			-LA13860-4
			-LA13827-3
			-LA4389-8
			-LL1892-0
			""";
		assertEquals(expected, hierarchy);

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
		assertThat(myFileSetCaptor.getAllValues().get(0).getResourcesToActivate()).containsExactlyInAnyOrder(
			"ValueSet/LL1892-0-1.234", "ValueSet/LL1001-8-1.234", "ValueSet/LL1000-0-1.234"
		);

		verify(myValueSetDao, times(3)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LL1000-0-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LL1000-0", vs.getUrl());
		assertEquals("1.234", vs.getVersion());

		String valueSetCompose = renderValueSetCompose(vs);
		expected = """
			http://loinc.org
			  LA13825-7
			  LA13838-0
			  LA13892-7
			""";
		assertEquals(expected, valueSetCompose);
	}

	@Test
	void testProcessValueSetAlreadyExists() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/AnswerFile/AnswerList.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myJobExecutionServices.newRequestDetails(any())).thenReturn(new SystemRequestDetails());

		ValueSet existing = new ValueSet();
		existing.setId("ValueSet/LL1000-0-1.234/_history/1");
		existing.getMeta().setVersionId("1");
		existing.getCompose().addInclude().setSystem("http://loinc.org").addConcept().setCode("EXISTING-1");
		when(myValueSetDao.read(eq(new IdType("LL1000-0-1.234")), any(RequestDetails.class))).thenReturn(existing);
		when(myValueSetDao.read(eq(new IdType("LL1001-8-1.234")), any(RequestDetails.class))).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1001-8-1.234")));
		when(myValueSetDao.read(eq(new IdType("LL1892-0-1.234")), any(RequestDetails.class))).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1001-8-1.234")));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkAttachmentIdForCurrentStepId("my-chunk-attachment-id");
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));
		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
		assertThat(myFileSetCaptor.getAllValues().get(0).getResourcesToActivate()).containsExactlyInAnyOrder(
			"ValueSet/LL1892-0-1.234", "ValueSet/LL1001-8-1.234", "ValueSet/LL1000-0-1.234"
		);

		verify(myValueSetDao, times(3)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LL1000-0-1.234", vs.getIdElement().getIdPart());
		assertEquals("1", vs.getIdElement().getVersionIdPart());
		assertEquals("1", vs.getMeta().getVersionId());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			http://loinc.org
			  EXISTING-1
			  LA13825-7
			  LA13838-0
			  LA13892-7
			""";
		assertEquals(expected, valueSetCompose);
	}

	public static String renderValueSetCompose(ValueSet theVs) {
		StringBuilder builder = new StringBuilder();

		List<ValueSet.ConceptSetComponent> include = theVs.getCompose().getInclude();
		for (ValueSet.ConceptSetComponent next : include) {
			builder.append(next.getSystem()).append("\n");
			for (ValueSet.ConceptReferenceComponent concept : next.getConcept()) {
				builder.append("  ").append(concept.getCode()).append("\n");
			}
		}

		return builder.toString();
	}


}
