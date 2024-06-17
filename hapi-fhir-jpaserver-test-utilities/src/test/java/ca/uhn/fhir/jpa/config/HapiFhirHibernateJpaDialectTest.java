package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import jakarta.persistence.PersistenceException;
import org.hibernate.HibernateException;
import org.hibernate.PersistentObjectException;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HapiFhirHibernateJpaDialectTest {

	private HapiFhirHibernateJpaDialect mySvc;

	@BeforeEach
	public void before() {
		mySvc = new HapiFhirHibernateJpaDialect(new HapiLocalizer());
	}

	@Test
	public void testConvertHibernateAccessException() {
		DataAccessException outcome = mySvc.convertHibernateAccessException(new ConstraintViolationException("this is a message", new SQLException("reason"), "IDX_FOO"));
		assertThat(outcome.getMessage()).contains("this is a message");

		try {
			mySvc.convertHibernateAccessException(new ConstraintViolationException("this is a message", new SQLException("reason"), ResourceTable.IDX_RES_TYPE_FHIR_ID));
			fail("");
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("The operation has failed with a client-assigned ID constraint failure");
		}

		try {
			outcome = mySvc.convertHibernateAccessException(new StaleStateException("this is a message"));
			fail("");
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("The operation has failed with a version constraint failure");
		}

		try {
			mySvc.convertHibernateAccessException(new ConstraintViolationException("this is a message", new SQLException("reason"), ResourceSearchUrlEntity.RES_SEARCH_URL_COLUMN_NAME));
			fail("");
		} catch (DataIntegrityViolationException e) {
			assertThat(e.getMessage()).contains(ResourceSearchUrlEntity.RES_SEARCH_URL_COLUMN_NAME);
		}

		outcome = mySvc.convertHibernateAccessException(new HibernateException("this is a message"));
		assertThat(outcome.getMessage()).contains("this is a message");

	}

	@Test
	public void testTranslate() {
		RuntimeException outcome = mySvc.translate(new PersistentObjectException("FOO"), "message");
		assertEquals("FOO", outcome.getMessage());

		try {
			PersistenceException exception = new PersistenceException("a message", new ConstraintViolationException("this is a message", new SQLException("reason"), ResourceTable.IDX_RES_TYPE_FHIR_ID));
			mySvc.translate(exception, "a message");
			fail("");
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("The operation has failed with a client-assigned ID constraint failure");
		}

	}

}
