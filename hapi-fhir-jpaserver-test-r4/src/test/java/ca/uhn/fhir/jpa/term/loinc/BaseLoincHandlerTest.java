package ca.uhn.fhir.jpa.term.loinc;

import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseLoincHandlerTest {

	public static final String LOINC_NUMBER = "test-loinc-number";
	public static final String DISPLAY_NAME = "test-display-name";
	public static final String TEST_VERSION = "2.69";

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private ValueSet myValueSet;

	@Mock
	private ValueSet.ConceptSetComponent myInclude;

	@Mock
	private ValueSet.ConceptReferenceComponent myConceptReferenceComponent;

	private Map<String, Integer> headerMap;
	private CSVRecord recordWithHeader;


	/**
	 * Need to setup a real parser because we can't mock final classes (without PowerMockito)
	 */
	private void setupCSVParser(Map<String, String> headerValues) throws Exception {
		final String[] headers = headerValues.keySet().toArray(new String[0]);
		final String[] values = headerValues.values().toArray(new String[0]);
		final String rowData = StringUtils.join(values, ',');

		try (final CSVParser parser = CSVFormat.DEFAULT.parse(new StringReader(rowData))) {
			parser.iterator().next();
		}
		try (final CSVParser parser = CSVFormat.DEFAULT.withHeader(headers).parse(new StringReader(rowData))) {
			recordWithHeader = parser.iterator().next();
			headerMap = parser.getHeaderMap();
		}
	}


	@Nested
	public class WithVersion {

		@Test
		void Top2000LabResultsHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LOINC #", "test-loinc-number");
			recordDataMap.put("Long Common Name", "test-Long-Common-Names");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new BaseLoincTop2000LabResultsHandler(
				null, Lists.newArrayList(), null, null, null,
				Lists.newArrayList(), new Properties(), null);
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getVersion()).thenReturn(TEST_VERSION);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude).setVersion(Mockito.notNull());
		}

		@Test
		void LoincDocumentOntologyHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LoincNumber", "test-LoincNumber");
			recordDataMap.put("PartNumber", "test-PartNumber");
			recordDataMap.put("PartTypeName", "Document.Role");
			recordDataMap.put("PartSequenceOrder", "test-PartSequenceOrder");
			recordDataMap.put("PartName", "test-PartName");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new LoincDocumentOntologyHandler(Maps.newHashMap(), null,
				Lists.newArrayList(), Lists.newArrayList(), new Properties(), null);
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getVersion()).thenReturn(TEST_VERSION);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude).setVersion(Mockito.notNull());
		}


		@Test
		void LoincGroupTermsFileHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LoincNumber", "test-LoincNumber");
			recordDataMap.put("GroupId", "test-GroupId");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new LoincGroupTermsFileHandler(
				Maps.newHashMap(), Lists.newArrayList(), Lists.newArrayList(), new Properties());
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getVersion()).thenReturn(TEST_VERSION);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude).setVersion(Mockito.notNull());
		}


		@Test
		void LoincImagingDocumentCodeHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LOINC_NUM", "test-loinc-number");
			recordDataMap.put("LONG_COMMON_NAME", "test-Long-Common-Names");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new LoincImagingDocumentCodeHandler(
				Maps.newHashMap(), Lists.newArrayList(), Lists.newArrayList(), new Properties());
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getVersion()).thenReturn(TEST_VERSION);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude).setVersion(Mockito.notNull());
		}


		@Test
		void LoincUniversalOrderSetHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LOINC_NUM", "test-loinc-number");
			recordDataMap.put("LONG_COMMON_NAME", "test-Long-Common-Names");
			recordDataMap.put("ORDER_OBS", "test-ORDER_OBS");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new LoincUniversalOrderSetHandler(
				Maps.newHashMap(), Lists.newArrayList(), Lists.newArrayList(), new Properties());
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getVersion()).thenReturn(TEST_VERSION);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude).setVersion(Mockito.notNull());
		}	}

	@Nested
	public class WithoutVersion {

		@Test
		void Top2000LabResultsHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LOINC #", "test-loinc-number");
			recordDataMap.put("Long Common Name", "test-Long-Common-Names");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new BaseLoincTop2000LabResultsHandler(Maps.newHashMap(), Lists.newArrayList(),
				null, null, null, Lists.newArrayList(), new Properties(), null);
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude, never()).setVersion(any());
		}

		@Test
		void LoincDocumentOntologyHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LoincNumber", "test-LoincNumber");
			recordDataMap.put("PartNumber", "test-PartNumber");
			recordDataMap.put("PartTypeName", "Document.Role");
			recordDataMap.put("PartSequenceOrder", "test-PartSequenceOrder");
			recordDataMap.put("PartName", "test-PartName");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new LoincDocumentOntologyHandler(Maps.newHashMap(), null,
				Lists.newArrayList(), Lists.newArrayList(), new Properties(), null);
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude, never()).setVersion(any());
		}

		@Test
		void LoincGroupTermsFileHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LoincNumber", "test-LoincNumber");
			recordDataMap.put("GroupId", "test-GroupId");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new LoincGroupTermsFileHandler(
				Maps.newHashMap(), Lists.newArrayList(), Lists.newArrayList(), new Properties());
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude, never()).setVersion(any());
		}


		@Test
		void LoincImagingDocumentCodeHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LOINC_NUM", "test-loinc-number");
			recordDataMap.put("LONG_COMMON_NAME", "test-Long-Common-Names");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new LoincImagingDocumentCodeHandler(
				Maps.newHashMap(), Lists.newArrayList(), Lists.newArrayList(), new Properties());
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude, never()).setVersion(any());
		}


		@Test
		void LoincUniversalOrderSetHandlerTest() throws Exception {
			Map<String, String> recordDataMap = Maps.newHashMap();
			recordDataMap.put("LOINC_NUM", "test-loinc-number");
			recordDataMap.put("LONG_COMMON_NAME", "test-Long-Common-Names");
			recordDataMap.put("ORDER_OBS", "test-ORDER_OBS");
			setupCSVParser(recordDataMap);

			BaseLoincHandler loincHandler = new LoincUniversalOrderSetHandler(
				Maps.newHashMap(), Lists.newArrayList(), Lists.newArrayList(), new Properties());
			BaseLoincHandler spiedLoincHandler = spy(loincHandler);

			when(spiedLoincHandler.getValueSet(any(), any(), any(), any())).thenReturn(myValueSet);
			when(myValueSet.getCompose().addInclude()).thenReturn(myInclude);
			when(myInclude.addConcept()).thenReturn(myConceptReferenceComponent);
			when(myConceptReferenceComponent.setCode(any())).thenReturn(myConceptReferenceComponent);

			spiedLoincHandler.accept(recordWithHeader);

			Mockito.verify(myInclude, never()).setVersion(any());
		}

	}




}
