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
}
