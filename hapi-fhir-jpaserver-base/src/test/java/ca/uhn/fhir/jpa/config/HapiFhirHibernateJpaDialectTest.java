package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hibernate.HibernateException;
import org.hibernate.PersistentObjectException;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import javax.persistence.PersistenceException;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

public class HapiFhirHibernateJpaDialectTest {

	private HapiFhirHibernateJpaDialect mySvc;

	@BeforeEach
	public void before() {
		mySvc = new HapiFhirHibernateJpaDialect(new HapiLocalizer());
	}

	@Test
	public void testConvertHibernateAccessException() {
		DataAccessException outcome = mySvc.convertHibernateAccessException(new ConstraintViolationException("this is a message", new SQLException("reason"), "IDX_FOO"));
		assertThat(outcome.getMessage(), containsString("this is a message"));

		try {
			mySvc.convertHibernateAccessException(new ConstraintViolationException("this is a message", new SQLException("reason"), ForcedId.IDX_FORCEDID_TYPE_FID));
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), containsString("The operation has failed with a client-assigned ID constraint failure"));
		}

		try {
			outcome = mySvc.convertHibernateAccessException(new StaleStateException("this is a message"));
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), containsString("The operation has failed with a version constraint failure"));
		}

		outcome = mySvc.convertHibernateAccessException(new HibernateException("this is a message"));
		assertThat(outcome.getMessage(), containsString("HibernateException: this is a message"));

	}

	@Test
	public void testTranslate() {
		RuntimeException outcome = mySvc.translate(new PersistentObjectException("FOO"), "message");
		assertEquals("FOO", outcome.getMessage());

		try {
			PersistenceException exception = new PersistenceException("a message", new ConstraintViolationException("this is a message", new SQLException("reason"), ForcedId.IDX_FORCEDID_TYPE_FID));
			mySvc.translate(exception, "a message");
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), containsString("The operation has failed with a client-assigned ID constraint failure"));
		}

	}

}
