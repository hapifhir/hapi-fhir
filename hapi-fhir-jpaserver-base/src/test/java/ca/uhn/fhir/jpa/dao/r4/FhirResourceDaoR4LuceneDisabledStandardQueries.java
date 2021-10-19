package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.TestR4WithLuceneDisabledConfig;
import ca.uhn.fhir.jpa.dao.BaseDAODateSearchTest;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4WithLuceneDisabledConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FhirResourceDaoR4LuceneDisabledStandardQueries extends BaseJpaTest {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4LuceneDisabledStandardQueries.class);
	@Autowired
	PlatformTransactionManager myTxManager;
	@Autowired
	FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myObservationDaoR4")
	IFhirResourceDao<Observation> myObservationDao;

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Override
	protected FhirContext getContext() {
		return myFhirCtx;
	}

	@Nested
	public class DateSearchTests extends BaseDAODateSearchTest {
		@Override
		protected FhirContext getMyFhirCtx() {
			return myFhirCtx;
		}

		@Override
		protected <T> T doInTransaction(TransactionCallback<T> theCallback) {
			return new TransactionTemplate(myTxManager).execute(
				theCallback
			);
		}

		@Override
		protected IFhirResourceDao<Observation> getObservationDao() {
			return myObservationDao;
		}
	}

}
