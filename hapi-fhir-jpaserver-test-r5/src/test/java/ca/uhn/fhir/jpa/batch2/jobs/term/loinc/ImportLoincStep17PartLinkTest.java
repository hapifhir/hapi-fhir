package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
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

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep3HandleHierarchyTest.renderHierarchy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep17PartLinkTest extends BaseImportLoincStepTest {

	@InjectMocks
	private ImportLoincStep17PartLink mySvc;


	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/PartFile/LoincPartLink.csv";
		mockFetchAttachment(classpath);
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()));

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String hierarchy = renderHierarchy(cs, true);
		String expected = """
			-10013-1
			  -Property[COMPONENT: {"system":"http://loinc.org","code":"LP31101-6","display":"R' wave amplitude.lead I"}
			  -Property[PROPERTY: {"system":"http://loinc.org","code":"LP6802-5","display":"Elpot"}
			  -Property[TIME_ASPCT: {"system":"http://loinc.org","code":"LP6960-1","display":"Pt"}
			  -Property[SYSTEM: {"system":"http://loinc.org","code":"LP7289-4","display":"Heart"}
			  -Property[SCALE_TYP: {"system":"http://loinc.org","code":"LP7753-9","display":"Qn"}
			  -Property[METHOD_TYP: {"system":"http://loinc.org","code":"LP6244-0","display":"EKG"}
			  -Property[analyte: {"system":"http://loinc.org","code":"LP31101-6","display":"R' wave amplitude.lead I"}
			  -Property[time-core: {"system":"http://loinc.org","code":"LP6960-1","display":"Pt"}
			  -Property[STATUS: STRING_PART_VALUE
			""";
		assertEquals(expected, hierarchy);

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
	}

}
