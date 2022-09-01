package ca.uhn.fhir.rest.param;

import static org.junit.jupiter.api.Assertions.*;

import ca.uhn.fhir.context.FhirContext;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class StringParamTest {

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
		StringParam input = new StringParam("foo", true);
		
		assertTrue(input.equals(input));
		assertFalse(input.equals(null));
		assertFalse(input.equals(""));
		assertFalse(input.equals(new StringParam("foo", false)));
	}

	@Test
	public void doSetValueAsQueryToken_withCustomSearchParameterAndNicknameQualifier_enablesNicknameExpansion(){
		String customSearchParamName = "someCustomSearchParameter";
		StringParam stringParam = new StringParam();
		stringParam.doSetValueAsQueryToken(myContext, customSearchParamName, ":nickname", "John");
		assertNicknameQualifierSearchParameterIsValid(stringParam, "John");
		assertNicknameWarningLogged(true);
	}

	@ParameterizedTest
	@ValueSource(strings = {"name", "given"})
	public void doSetValueAsQueryToken_withPredefinedSearchParametersAndNicknameQualifier_enablesNicknameExpansion(String theSearchParameterName){
		StringParam stringParam = new StringParam();
		stringParam.doSetValueAsQueryToken(myContext, theSearchParameterName, ":nickname", "John");
		assertNicknameQualifierSearchParameterIsValid(stringParam, "John");
		assertNicknameWarningLogged(false);
	}

	private void assertNicknameQualifierSearchParameterIsValid(StringParam theStringParam, String theExpectedValue){
		assertTrue(theStringParam.isNicknameExpand());
		assertFalse(theStringParam.isExact());
		assertFalse(theStringParam.isContains());
		assertEquals(theExpectedValue, theStringParam.getValue());
	}

	private void assertNicknameWarningLogged(boolean theWasLogged){
		String expectedMessage = ":nickname qualifier was assigned to a search parameter other than one of the intended parameters \"name\" and \"given\"";
		Level expectedLevel = Level.DEBUG;
		List<ILoggingEvent> warningLogs = myListAppender
			.list
			.stream()
			.filter(event -> expectedMessage.equals(event.getFormattedMessage()))
			.filter(event -> expectedLevel.equals(event.getLevel()))
			.collect(Collectors.toList());

		if (theWasLogged) {
			assertEquals(1, warningLogs.size());
		} else {
			assertTrue(warningLogs.isEmpty());
		}
	}
	
}
