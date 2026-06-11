package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.custom.CodeSystemToCustomCsvConverter;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TerminologyLoaderSvcCustomTest extends BaseLoaderTest {
	private TermLoaderSvcImpl mySvc;

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	private ZipCollectionBuilder myFiles;
	@Captor
	private ArgumentCaptor<CustomTerminologySet> myCustomTerminologySetCaptor;
	@Mock
	private ITermDeferredStorageSvc myTermDeferredStorageSvc;

	@BeforeEach
	public void before() {
		mySvc = TermLoaderSvcImpl.withoutProxyCheck(myTermDeferredStorageSvc, myTermCodeSystemStorageSvc);

		myFiles = new ZipCollectionBuilder();
	}

	@Test
	public void testDeltaAdd_ByCsv() throws IOException {

		myFiles.addFileText(loadResource("/custom_term/concepts.csv"), "concepts.csv");
		myFiles.addFileText(loadResource("/custom_term/hierarchy.csv"), "hierarchy.csv");
		myFiles.addFileText(loadResource("/custom_term/properties.csv"), "properties.csv");

		UploadStatistics stats = new UploadStatistics(100, new IdType("CodeSystem/100"));
		when(myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(eq("http://foo/system"), any())).thenReturn(stats);

		UploadStatistics outcome = mySvc.loadDeltaAdd("http://foo/system", myFiles.getFiles(), mySrd);
		assertThat(outcome).isSameAs(stats);

		verify(myTermCodeSystemStorageSvc, times(1)).applyDeltaCodeSystemsAdd(eq("http://foo/system"), myCustomTerminologySetCaptor.capture());
		CustomTerminologySet set = myCustomTerminologySetCaptor.getValue();

		// Root concepts
		assertThat(set.getRootConcepts()).hasSize(2);
		assertEquals("CHEM", set.getRootConcepts().get(0).getCode());
		assertEquals("Chemistry", set.getRootConcepts().get(0).getDisplay());
		assertEquals("MICRO", set.getRootConcepts().get(1).getCode());
		assertEquals("Microbiology", set.getRootConcepts().get(1).getDisplay());

		// Child concepts
		assertThat(set.getRootConcepts().get(0).getChildren()).hasSize(2);
		assertEquals("HB", set.getRootConcepts().get(0).getChildren().get(0).getChild().getCode());
		assertEquals("Hemoglobin", set.getRootConcepts().get(0).getChildren().get(0).getChild().getDisplay());
		assertNull(set.getRootConcepts().get(0).getChildren().get(0).getChild().getSequence());
		assertEquals("NEUT", set.getRootConcepts().get(0).getChildren().get(1).getChild().getCode());
		assertEquals("Neutrophils", set.getRootConcepts().get(0).getChildren().get(1).getChild().getDisplay());

		// Properties
		TermConcept hbCode = set.getRootConcepts().get(0).getChildren().get(0).getChild();
		assertEquals("HB", hbCode.getCode());
		assertEquals(TermConceptPropertyTypeEnum.STRING, hbCode.getPropertyType("color"));
		assertEquals("red", hbCode.getStringProperty("color"));
		assertEquals(TermConceptPropertyTypeEnum.CODING, hbCode.getPropertyType("loinc_equiv"));
		assertEquals("http://loinc.org", hbCode.getCodingProperties("loinc_equiv").get(0).getSystem());
		assertEquals("1-2345", hbCode.getCodingProperties("loinc_equiv").get(0).getCode());
		assertEquals(TermConceptPropertyTypeEnum.INTEGER, hbCode.getPropertyType("sequence"));
		assertEquals("25", hbCode.getPrimitiveProperty("sequence"));
		assertEquals(TermConceptPropertyTypeEnum.BOOLEAN, hbCode.getPropertyType("archived"));
		assertEquals("false", hbCode.getPrimitiveProperty("archived"));
		assertEquals(TermConceptPropertyTypeEnum.DATETIME, hbCode.getPropertyType("created"));
		assertEquals("2022-01-01", hbCode.getPrimitiveProperty("created"));
		assertEquals(TermConceptPropertyTypeEnum.DECIMAL, hbCode.getPropertyType("k_score"));
		assertEquals("1.23", hbCode.getPrimitiveProperty("k_score"));

	}

	@ParameterizedTest
	@EnumSource(value = TermConceptPropertyTypeEnum.class)
	void testDeltaAdd_ByCodeSystem_PropertyCoding(TermConceptPropertyTypeEnum thePropertyType) {

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo/system");
		CodeSystem.ConceptDefinitionComponent concept = codeSystem.addConcept();
		concept.setCode("CHEM");
		concept.setDisplay("Chemistry");
		CodeSystem.ConceptPropertyComponent prop = concept.addProperty();
		prop.setCode("prop-code");
		switch (thePropertyType) {
			case CODE -> prop.setValue(new CodeType("code-value"));
			case STRING -> prop.setValue(new StringType("string-value"));
			case CODING -> prop.setValue(new Coding("http://foo/prop-value-system", "prop-value-code", "prop-value-display"));
			case BOOLEAN -> prop.setValue(new BooleanType(true));
			case INTEGER -> prop.setValue(new IntegerType(123));
			case DECIMAL -> prop.setValue(new DecimalType(1.23));
			case DATETIME -> prop.setValue(new DateTimeType("2023-01-01"));
			default -> throw new IllegalStateException("Unexpected value: " + thePropertyType);
		}

		CodeSystemToCustomCsvConverter converter = new CodeSystemToCustomCsvConverter(myFhirContext);
		myFiles.getFiles().addAll(converter.convertCodeSystemsToFileDescriptors(List.of(codeSystem)));

		UploadStatistics stats = new UploadStatistics(100, new IdType("CodeSystem/100"));
		when(myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(eq("http://foo/system"), any())).thenReturn(stats);

		UploadStatistics outcome = mySvc.loadDeltaAdd("http://foo/system", myFiles.getFiles(), mySrd);
		assertThat(outcome).isSameAs(stats);

		verify(myTermCodeSystemStorageSvc, times(1)).applyDeltaCodeSystemsAdd(eq("http://foo/system"), myCustomTerminologySetCaptor.capture());
		CustomTerminologySet set = myCustomTerminologySetCaptor.getValue();

		// Root concepts
		assertThat(set.getRootConcepts()).hasSize(1);
		TermConcept termConcept = set.getRootConcepts().get(0);
		assertEquals("CHEM", termConcept.getCode());
		assertEquals("Chemistry", termConcept.getDisplay());

		assertEquals(1, termConcept.getProperties().size());
		TermConceptProperty property = termConcept.getProperties().iterator().next();
		assertEquals("prop-code", property.getKey());
		assertEquals(thePropertyType, property.getType());

		switch (thePropertyType) {
			case CODING -> {
				assertEquals("http://foo/prop-value-system", property.getCodeSystem());
				assertEquals("prop-value-code", property.getValue());
				assertEquals("prop-value-display", property.getDisplay());
			}
			case STRING -> assertEquals("string-value", property.getValue());
			case BOOLEAN -> assertEquals("true", property.getValue());
			case INTEGER -> assertEquals("123", property.getValue());
			case DECIMAL -> assertEquals("1.23", property.getValue());
			case DATETIME -> assertEquals("2023-01-01", property.getValue());
			case CODE -> assertEquals("code-value", property.getValue());
			default -> throw new IllegalStateException("Unexpected value: " + thePropertyType);
		}


	}


	@Test
	public void testDeltaRemove() throws IOException {

		myFiles.addFileText(loadResource("/custom_term/concepts.csv"), "concepts.csv");

		// Hierarchy should be ignored for remove, but we'll add one just
		// to make sure it's ignored..
		myFiles.addFileText(loadResource("/custom_term/hierarchy.csv"), "hierarchy.csv");

		UploadStatistics stats = new UploadStatistics(100, new IdType("CodeSystem/100"));
		when(myTermCodeSystemStorageSvc.applyDeltaCodeSystemsRemove(eq("http://foo/system"), any())).thenReturn(stats);

		UploadStatistics outcome = mySvc.loadDeltaRemove("http://foo/system", myFiles.getFiles(), mySrd);
		assertThat(outcome).isSameAs(stats);

		verify(myTermCodeSystemStorageSvc, times(1)).applyDeltaCodeSystemsRemove(eq("http://foo/system"), myCustomTerminologySetCaptor.capture());
		CustomTerminologySet set = myCustomTerminologySetCaptor.getValue();

		// Root concepts
		assertThat(set.getRootConcepts()).hasSize(5);
		assertEquals("CHEM", set.getRootConcepts().get(0).getCode());
		assertEquals("Chemistry", set.getRootConcepts().get(0).getDisplay());
		assertEquals("HB", set.getRootConcepts().get(1).getCode());
		assertEquals("Hemoglobin", set.getRootConcepts().get(1).getDisplay());
		assertEquals("NEUT", set.getRootConcepts().get(2).getCode());
		assertEquals("Neutrophils", set.getRootConcepts().get(2).getDisplay());

	}


}
