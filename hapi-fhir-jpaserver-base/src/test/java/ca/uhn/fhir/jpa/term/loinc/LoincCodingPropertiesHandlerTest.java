package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.term.loinc.LoincCodingPropertiesHandler.ASK_AT_ORDER_ENTRY_PROP_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincCodingPropertiesHandler.ASSOCIATED_OBSERVATIONS_PROP_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincCodingPropertiesHandler.LOINC_NUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoincCodingPropertiesHandlerTest {

	private static final String CODE_A = "code-A";

	private static LoincCodingPropertiesHandler testedHandler;

	@Mock private CSVRecord myCsvRecord;
	@Mock private TermConcept myTargetTermConcept;
	@Mock private TermConcept myRefTermConcept1;
	@Mock private TermConcept myRefTermConcept2;

	private final Map<String, TermConcept> myCode2concept = new HashMap<>();
	private final Map<String, CodeSystem.PropertyType> myPropertyNameTypeMap = new HashMap<>();


	@BeforeEach
	void setUp() {
		myCode2concept.put(CODE_A, myTargetTermConcept);
		testedHandler = new LoincCodingPropertiesHandler(myCode2concept, myPropertyNameTypeMap);
	}


	@Test
	void not_any_property_valid_does_nothing() {
		myPropertyNameTypeMap.put("prop_1", CodeSystem.PropertyType.CODING); // uninteresting property name
		myPropertyNameTypeMap.put(ASK_AT_ORDER_ENTRY_PROP_NAME, CodeSystem.PropertyType.STRING); // wrong property type

		testedHandler.accept(myCsvRecord);

		verify(myTargetTermConcept, never()).addPropertyCoding(anyString(), anyString(), anyString(), anyString());
	}


	@Test
	void record_no_loinc_num_property_does_nothing() {
		myPropertyNameTypeMap.put(ASK_AT_ORDER_ENTRY_PROP_NAME, CodeSystem.PropertyType.CODING); // wrong property type
		when(myCsvRecord.get(LOINC_NUM)).thenReturn(null);

		testedHandler.accept(myCsvRecord);

		verify(myTargetTermConcept, never()).addPropertyCoding(anyString(), anyString(), anyString(), anyString());
	}


	@Test
	void no_property_valid_value_does_nothing() {
		myPropertyNameTypeMap.put(ASK_AT_ORDER_ENTRY_PROP_NAME, CodeSystem.PropertyType.CODING); // wrong property type
		when(myCsvRecord.get(LOINC_NUM)).thenReturn(CODE_A);

		testedHandler.accept(myCsvRecord);

		verify(myTargetTermConcept, never()).addPropertyCoding(anyString(), anyString(), anyString(), anyString());
	}


	@ParameterizedTest
	@ValueSource(strings = {ASK_AT_ORDER_ENTRY_PROP_NAME, ASSOCIATED_OBSERVATIONS_PROP_NAME})
	void each_tested_record_prop_creates_term_concept_prop(String thePropName) {
		myPropertyNameTypeMap.put(thePropName, CodeSystem.PropertyType.CODING);
		when(myCsvRecord.get(LOINC_NUM)).thenReturn(CODE_A);
		myCode2concept.put(CODE_A, myTargetTermConcept);
		myCode2concept.put("ref-code-01", myRefTermConcept1);
		myCode2concept.put("ref-code-02", myRefTermConcept2);
		lenient().when(myCsvRecord.get(thePropName)).thenReturn("ref-code-01; ref-code-02");
		when(myRefTermConcept1.getDisplay()).thenReturn("display-value-01");
		when(myRefTermConcept2.getDisplay()).thenReturn("display-value-02");

		testedHandler.accept(myCsvRecord);

		verify(myTargetTermConcept, times(1)).addPropertyCoding(
			thePropName, ITermLoaderSvc.LOINC_URI, "ref-code-01", "display-value-01");

		verify(myTargetTermConcept, times(1)).addPropertyCoding(
			thePropName, ITermLoaderSvc.LOINC_URI, "ref-code-02", "display-value-02");
	}


	@ParameterizedTest
	@ValueSource(strings = {ASK_AT_ORDER_ENTRY_PROP_NAME, ASSOCIATED_OBSERVATIONS_PROP_NAME})
	void each_tested_record_prop_not_existing_target_is_logged(String thePropName) {
		myPropertyNameTypeMap.put(thePropName, CodeSystem.PropertyType.CODING);
		when(myCsvRecord.get(LOINC_NUM)).thenReturn(CODE_A);
		myCode2concept.put(CODE_A, myTargetTermConcept);
		myCode2concept.put("ref-code-01", myRefTermConcept1);
		lenient().when(myCsvRecord.get(thePropName)).thenReturn("ref-code-01; ref-code-02");
		when(myRefTermConcept1.getDisplay()).thenReturn("display-value-01");

		Logger testLogger = (Logger) LoggerFactory.getLogger(LoincCodingPropertiesHandler.class);
		ListAppender<ILoggingEvent> testListAppender = addTestLogAppenderForClass(testLogger);

		try {
			// call method under test
			testedHandler.accept(myCsvRecord);

			// JUnit assertions
			List<ILoggingEvent> logsList = testListAppender.list;
			assertThat(logsList).hasSize(1);
			assertEquals(Level.ERROR, logsList.get(0).getLevel());
			assertThat(logsList.get(0).getFormattedMessage()).startsWith("Couldn't find TermConcept for code: 'ref-code-02'");
			assertThat(logsList.get(0).getFormattedMessage()).contains(thePropName);

		} finally {
			testLogger.detachAppender(testListAppender);
		}
	}


	private ListAppender<ILoggingEvent> addTestLogAppenderForClass(Logger theLogger) {
		// create and start a ListAppender
		ListAppender<ILoggingEvent> testListAppender = new ListAppender<>();
		testListAppender.start();

		// add the appender to the logger
		theLogger.addAppender(testListAppender);
		
		return testListAppender;
	}

}
