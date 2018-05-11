package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

public class HapiFhirHibernateJpaDialect extends HibernateJpaDialect {

	private HapiLocalizer myLocalizer;

	/**
	 * Constructor
	 */
	public HapiFhirHibernateJpaDialect(HapiLocalizer theLocalizer) {
		myLocalizer = theLocalizer;
	}

	@Override
	protected DataAccessException convertHibernateAccessException(HibernateException theException) {
		if (theException instanceof ConstraintViolationException) {
			if (ResourceHistoryTable.IDX_RESVER_ID_VER.equals(((ConstraintViolationException) theException).getConstraintName())) {
				throw new PreconditionFailedException(myLocalizer.getMessage(HapiFhirHibernateJpaDialect.class, "resourceVersionConstraintFailure"));
			}
		}

		return super.convertHibernateAccessException(theException);
	}

}
