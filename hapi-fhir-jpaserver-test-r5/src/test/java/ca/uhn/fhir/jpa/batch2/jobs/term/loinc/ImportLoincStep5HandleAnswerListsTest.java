package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep5HandleAnswerListsTest extends BaseImportLoincStepTest {

	@InjectMocks
	private ImportLoincStep5HandleAnswerLists mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/AnswerFile/AnswerList.csv";
		mockFetchAttachment(classpath);
		mockFetchPropertiesFileAttachmentNotFound();
		mockFetchJobMetadataAttachment();
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()));
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

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

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> ResourcesToActivate[ValueSet/LL1000-0-1.234, ValueSet/LL1001-8-1.234, ValueSet/LL1892-0-1.234]",
			"finalize-import -> RecordsAdded: From[step-1] Counts[valueSetsAdded=3,valueSetCodesAdded=10]"
		);

		verify(myValueSetDao, times(3)).create(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LL1000-0-1.234", vs.getUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE));
		assertEquals("LL1000-0-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LL1000-0", vs.getUrl());
		assertEquals("1.234", vs.getVersion());

		String valueSetCompose = renderValueSetCompose(vs);
		expected = """
			INCLUDE:
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
		String classpath = "loinc-ver/v269/AccessoryFiles/AnswerFile/AnswerList.csv";
		mockFetchAttachment(classpath);
		mockFetchPropertiesFileAttachmentNotFound();
		mockFetchJobMetadataAttachment();
		when(myJobExecutionServices.newRequestDetails(any())).thenReturn(new SystemRequestDetails());
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()).incrementConceptsAddedCount());

		ValueSet existing = new ValueSet();
		existing.setId("ValueSet/LL1000-0-1.234/_history/1");
		existing.getMeta().setVersionId("1");
		existing.getCompose().addInclude().setSystem("http://loinc.org").addConcept().setCode("EXISTING-1");
		when(myValueSetDao.read(eq(new IdType("LL1000-0-1.234")), any(RequestDetails.class))).thenReturn(existing);
		when(myValueSetDao.read(eq(new IdType("LL1001-8-1.234")), any(RequestDetails.class))).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1001-8-1.234")));
		when(myValueSetDao.read(eq(new IdType("LL1892-0-1.234")), any(RequestDetails.class))).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1001-8-1.234")));
		when(myValueSetDao.update(any(), any(RequestDetails.class))).thenReturn(new DaoMethodOutcome());
		mockDaoRegistryValueSet();
		mockDaoRegistryConceptMap();

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> ResourcesToActivate[ValueSet/LL1000-0-1.234, ValueSet/LL1001-8-1.234, ValueSet/LL1892-0-1.234]",
			"finalize-import -> RecordsAdded: From[step-1] Counts[conceptsAdded=1,valueSetsAdded=2,valueSetCodesAdded=10,otherChanges=1]"
		);

		verify(myValueSetDao, times(2)).create(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LL1001-8-1.234", vs.getIdElement().getIdPart());
		assertEquals("LL1001-8-1.234", vs.getUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE));
		assertNull(vs.getIdElement().getVersionIdPart());
		assertNull(vs.getMeta().getVersionId());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			INCLUDE:
			http://loinc.org
			  LA6270-8
			  LA13836-4
			  LA13834-9
			  LA13853-9
			  LA13860-4
			  LA13827-3
			  LA4389-8
			""";
		assertEquals(expected, valueSetCompose);

		verify(myValueSetDao, times(1)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		allValueSets = myValueSetCaptor.getAllValues();
		vs = allValueSets.get(2);
		assertEquals("LL1000-0-1.234", vs.getIdElement().getIdPart());
		assertNull(vs.getUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE));
		assertEquals("1", vs.getIdElement().getVersionIdPart());
		assertEquals("1", vs.getMeta().getVersionId());

		 valueSetCompose = renderValueSetCompose(vs);
		 expected = """
			INCLUDE:
			http://loinc.org
			  EXISTING-1
			  LA13825-7
			  LA13838-0
			  LA13892-7
			""";
		assertEquals(expected, valueSetCompose);
	}

}
