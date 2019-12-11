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
		String expectedOutput = "no comment\n\n" +
			"so like definitely no comment\n\n";
		assertEquals(expectedOutput, SchemaInitializationProvider.stripComments(input));
	}
}
