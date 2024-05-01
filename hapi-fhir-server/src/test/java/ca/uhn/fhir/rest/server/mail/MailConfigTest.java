package ca.uhn.fhir.rest.server.mail;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotSame;

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
		assertThat(actual).isEqualTo(SMTP_HOST_NAME);
	}

	@Test
	public void testGetSmtpPort() {
		// execute
		final int actual = fixture.getSmtpPort();
		// validate
		assertThat(actual).isEqualTo(SMTP_PORT);
	}

	@Test
	public void testGetSmtpUsername() {
		// execute
		final String actual = fixture.getSmtpUsername();
		// validate
		assertThat(actual).isEqualTo(SMTP_USERNAME);
	}

	@Test
	public void testGetSmtpPassword() {
		// execute
		final String actual = fixture.getSmtpPassword();
		// validate
		assertThat(actual).isEqualTo(SMTP_PASSWORD);
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
		assertThat(fixture).isNotNull().isEqualTo(fixture);
		assertThat(fixture).isSameAs(fixture);
		assertThat(other).isEqualTo(fixture);
		assertNotSame(fixture, other);
		assertThat(other.hashCode()).isEqualTo(fixture.hashCode());
		assertThat(other.toString()).isNotEqualTo(fixture.toString());
	}

	@Test
	public void testSetSmtpUsername() {
		// execute & validate
		assertThat(fixture.setSmtpUsername("xyz").getSmtpUsername()).isEqualTo("xyz");
		assertNull(fixture.setSmtpUsername(null).getSmtpUsername());
		assertNull(fixture.setSmtpUsername("").getSmtpUsername());
		assertNull(fixture.setSmtpUsername("  ").getSmtpUsername());
	}

	@Test
	public void testSetSmtpPassword() {
		// execute & validate
		assertThat(fixture.setSmtpPassword("xyz").getSmtpPassword()).isEqualTo("xyz");
		assertNull(fixture.setSmtpPassword(null).getSmtpPassword());
		assertNull(fixture.setSmtpPassword("").getSmtpPassword());
		assertNull(fixture.setSmtpPassword("  ").getSmtpPassword());
	}

}
