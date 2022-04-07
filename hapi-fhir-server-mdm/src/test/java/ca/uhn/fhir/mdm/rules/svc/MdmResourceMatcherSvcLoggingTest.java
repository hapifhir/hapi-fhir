package ca.uhn.fhir.mdm.rules.svc;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.log.Logs;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MdmResourceMatcherSvcLoggingTest extends BaseMdmRulesR4Test {
	private MdmResourceMatcherSvc myMdmResourceMatcherSvc;
	private Patient myJohn;
	private Patient myJohny;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		when(mySearchParamRetriever.getActiveSearchParam("Patient", "birthdate")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Practitioner", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Medication", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "active")).thenReturn(mock(RuntimeSearchParam.class));

		myMdmResourceMatcherSvc = buildMatcher(buildActiveBirthdateIdRules());

		myJohn = buildJohn();
		myJohny = buildJohny();

		myJohn.addName().setFamily("LastName");
		myJohny.addName().setFamily("DifferentLastName");

	}

	@Test
	public void testMatchWillProvideLogsAboutSuccessOnDebugLevel(){
		Logger logger = (Logger) Logs.getMdmTroubleshootingLog();

		MemoryAppender memoryAppender = createAndAssignMemoryAppender(logger);

		MdmMatchOutcome result = myMdmResourceMatcherSvc.match(myJohn, myJohny);
		assertNotNull(result);

		assertTrue(memoryAppender.contains("Evaluating match", Level.DEBUG));
		assertTrue(memoryAppender.contains("No Match: Matcher patient-last did not match.", Level.DEBUG));
		assertTrue(memoryAppender.contains("Match: Successfully matched Matcher patient-given.", Level.DEBUG));
	}

	

	protected MemoryAppender createAndAssignMemoryAppender(Logger theLogger){

		MemoryAppender memoryAppender = new MemoryAppender();
		memoryAppender.setContext(theLogger.getLoggerContext());
		theLogger.addAppender(memoryAppender);
		memoryAppender.start();

		return memoryAppender;
	}

	public static class MemoryAppender extends ListAppender<ILoggingEvent>{
		public void reset() {
			this.list.clear();
		}

		public boolean contains(String string, Level level) {
			return this.list.stream()
				.anyMatch(event -> event.toString().contains(string)
					&& event.getLevel().equals(level));
		}

	}

}
