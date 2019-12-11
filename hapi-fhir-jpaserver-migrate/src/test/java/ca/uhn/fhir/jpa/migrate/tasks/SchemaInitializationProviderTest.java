package ca.uhn.fhir.jpa.migrate.tasks;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SchemaInitializationProviderTest {
	@Test
	public void testClean() {
		assertEquals("foo\n)", SchemaInitializationProvider.clean("foo,\n)"));
		assertEquals("foo\n  )", SchemaInitializationProvider.clean("foo,\n  )"));
		assertEquals("foo,bar\n  )", SchemaInitializationProvider.clean("foo,bar\n  )"));
	}

	@Test
	public void testStripComments() {
		String input = "no comment\n" +
			" --spacecomment \n" +
			"so like definitely no comment\n" +
			"-- nospace comment\n";

		String expectedOutput = "no comment\n" +
			"so like definitely no comment\n";

		assertEquals(expectedOutput, SchemaInitializationProvider.preProcessLines(input));
	}

	@Test
	public void testStripQuartzDelete() {
		String input = "delete from qrtz_paused_trigger_grps;\n" +
			"delete from qrtz_locks;\n" +
			"delete from qrtz_scheduler_state;\n" +
			"\n" +
			"drop table qrtz_calendars;\n" +
			"drop table qrtz_fired_triggers;\n";

		String expectedOutput = "\n";

		assertEquals(expectedOutput, SchemaInitializationProvider.preProcessLines(input));
	}
}
