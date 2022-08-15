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
	public void testMatchWillProvideLogsAboutSuccessOnTraceLevel() {
		Logger logger = (Logger) Logs.getMdmTroubleshootingLog();
		logger.setLevel(Level.TRACE);

		MemoryAppender memoryAppender = createAndAssignMemoryAppender(logger);

		MdmMatchOutcome result = myMdmResourceMatcherSvc.match(myJohn, myJohny);
		assertNotNull(result);

		//this test assumes, that the defined algorithm for calculating scores doesn't change
		assertTrue(memoryAppender.contains("No match: Matcher patient-last did not match (score: 0.4", Level.TRACE));
		assertTrue(memoryAppender.contains("Match: Successfully matched matcher patient-given with score 0.8", Level.TRACE));
	}

	@Test
	public void testMatchWillProvideSummaryOnMatchingSuccessForEachField() {
		Patient someoneElse = buildSomeoneElse();
		Logger logger = (Logger) Logs.getMdmTroubleshootingLog();
		logger.setLevel(Level.TRACE);

		MemoryAppender memoryAppender = createAndAssignMemoryAppender(logger);

		MdmMatchOutcome result = myMdmResourceMatcherSvc.match(myJohn, someoneElse);
		assertNotNull(result);

		assertTrue(memoryAppender.contains("NO_MATCH Patient/", Level.DEBUG));
		assertTrue(memoryAppender.contains("Field matcher results:\npatient-given: NO\npatient-last: YES", Level.TRACE));
	}

	protected Patient buildSomeoneElse() {
		Patient patient = new Patient();
		patient.addName().addGiven("SomeOneElse");
		patient.addName().setFamily("LastName");
		patient.setId("Patient/3");
		return patient;
	}


	protected MemoryAppender createAndAssignMemoryAppender(Logger theLogger) {

		MemoryAppender memoryAppender = new MemoryAppender();
		memoryAppender.setContext(theLogger.getLoggerContext());
		theLogger.addAppender(memoryAppender);
		memoryAppender.start();

		return memoryAppender;
	}

	public static class MemoryAppender extends ListAppender<ILoggingEvent> {
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
