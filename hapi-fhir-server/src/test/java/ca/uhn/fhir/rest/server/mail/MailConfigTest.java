package ca.uhn.fhir.rest.server.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MailConfigTest {

	private static final String SMTP_HOST_NAME = "SMTP_HOST_NAME";
	private static final Integer SMTP_PORT = 1025;
	private static final String SMTP_USERNAME = "SMTP_USERNAME";
	private static final String SMTP_PASSWORD = "SMTP_PASSWORD";

	private MailConfig fixture;

	@BeforeEach
	public void setUp() {
		fixture = withMainConfig();
	}

	private MailConfig withMainConfig() {
		return new MailConfig()
			.setSmtpHostname(SMTP_HOST_NAME)
			.setSmtpPort(SMTP_PORT)
			.setSmtpUsername(SMTP_USERNAME)
			.setSmtpPassword(SMTP_PASSWORD)
			.setSmtpUseStartTLS(true);
	}

	@Test
	public void testGetSmtpHostname() {
		// execute
		final String actual = fixture.getSmtpHostname();
		// validate
		assertEquals(SMTP_HOST_NAME, actual);
	}

	@Test
	public void testGetSmtpPort() {
		// execute
		final int actual = fixture.getSmtpPort();
		// validate
		assertEquals(SMTP_PORT, actual);
	}

	@Test
	public void testGetSmtpUsername() {
		// execute
		final String actual = fixture.getSmtpUsername();
		// validate
		assertEquals(SMTP_USERNAME, actual);
	}

	@Test
	public void testGetSmtpPassword() {
		// execute
		final String actual = fixture.getSmtpPassword();
		// validate
		assertEquals(SMTP_PASSWORD, actual);
	}

	@Test
	public void testIsSmtpUseStartTLS() {
		// execute
		final boolean actual = fixture.isSmtpUseStartTLS();
		// validate
		assertTrue(actual);
	}

	@Test
	public void testEquality() {
		// setup
		final MailConfig other = withMainConfig();
		// execute & validate
		assertEquals(fixture, fixture);
		assertSame(fixture, fixture);
		assertEquals(fixture, other);
		assertNotSame(fixture, other);
		assertEquals(fixture.hashCode(), other.hashCode());
		assertNotEquals(fixture.toString(), other.toString());
		assertNotEquals(fixture, null);
	}

	@Test
	public void testSetSmtpUsername() {
		// execute & validate
		assertEquals("xyz", fixture.setSmtpUsername("xyz").getSmtpUsername());
		assertNull(fixture.setSmtpUsername(null).getSmtpUsername());
		assertNull(fixture.setSmtpUsername("").getSmtpUsername());
		assertNull(fixture.setSmtpUsername("  ").getSmtpUsername());
	}

	@Test
	public void testSetSmtpPassword() {
		// execute & validate
		assertEquals("xyz", fixture.setSmtpPassword("xyz").getSmtpPassword());
		assertNull(fixture.setSmtpPassword(null).getSmtpPassword());
		assertNull(fixture.setSmtpPassword("").getSmtpPassword());
		assertNull(fixture.setSmtpPassword("  ").getSmtpPassword());
	}

}
