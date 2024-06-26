package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SpecialParamTest {

	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(StringParam.class);
	private ListAppender<ILoggingEvent> myListAppender = new ListAppender<>();

	@Mock
	private FhirContext myContext;

	@BeforeEach
	public void beforeEach(){
		myListAppender = new ListAppender<>();
		myListAppender.start();
		ourLog.addAppender(myListAppender);
	}

	@AfterEach
	public void afterEach(){
		myListAppender.stop();
	}

	@Test
	public void testEquals() {
		SpecialParam specialParam = new SpecialParam();
		specialParam.setValueAsQueryToken(myContext, Constants.PARAM_TEXT, Constants.PARAMQUALIFIER_STRING_CONTAINS, "my-test-value");

		SpecialParam specialParam2 = new SpecialParam();
		specialParam2.setValueAsQueryToken(myContext, Constants.PARAM_TEXT, Constants.PARAMQUALIFIER_STRING_CONTAINS, "my-test-value");
		assertThat(specialParam).isEqualTo(specialParam2);
	}

	@Test
	public void testContainsOnlyWorksForSpecificParams() {
		SpecialParam specialParamText = new SpecialParam();
		specialParamText.setValueAsQueryToken(myContext, Constants.PARAM_TEXT, Constants.PARAMQUALIFIER_STRING_CONTAINS, "my-test-value");
		assertTrue(specialParamText.isContains());

		SpecialParam specialParamContent = new SpecialParam();
		specialParamContent.setValueAsQueryToken(myContext, Constants.PARAM_CONTENT, Constants.PARAMQUALIFIER_STRING_CONTAINS, "my-test-value");
		assertTrue(specialParamContent.isContains());

		SpecialParam nonTextSpecialParam = new SpecialParam();
		nonTextSpecialParam.setValueAsQueryToken(myContext, "name", Constants.PARAMQUALIFIER_STRING_CONTAINS, "my-test-value");
		assertFalse(nonTextSpecialParam.isContains());
	}


}
