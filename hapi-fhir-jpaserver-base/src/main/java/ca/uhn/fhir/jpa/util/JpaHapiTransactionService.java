package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

public class JpaHapiTransactionService extends HapiTransactionService {

	private volatile Boolean myCustomIsolationSupported;

	@Override
	public boolean isCustomIsolationSupported() {
		if (myCustomIsolationSupported == null) {
			if (myTransactionManager instanceof JpaTransactionManager) {
				JpaDialect jpaDialect = ((JpaTransactionManager) myTransactionManager).getJpaDialect();
				myCustomIsolationSupported = (jpaDialect instanceof HibernateJpaDialect);
			} else {
				myCustomIsolationSupported = false;
			}
		}
		return myCustomIsolationSupported;
	}

}
