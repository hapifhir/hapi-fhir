package ca.uhn.hapi.fhir.sql.hibernatesvc;

import com.google.common.annotations.VisibleForTesting;
import org.hibernate.service.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A hibernate {@link Service} which provides the HAPI FHIR Storage Settings.
 */
public class HapiHibernateDialectSettingsService implements Service {

	private boolean myTrimConditionalIdsFromPrimaryKeys;

	public boolean isTrimConditionalIdsFromPrimaryKeys() {
		return myTrimConditionalIdsFromPrimaryKeys;
	}

	public void setTrimConditionalIdsFromPrimaryKeys(boolean theTrimConditionalIdsFromPrimaryKeys) {
		myTrimConditionalIdsFromPrimaryKeys = theTrimConditionalIdsFromPrimaryKeys;
	}

	@VisibleForTesting
	public static Boolean getLastTrimConditionalIdsFromPrimaryKeysForUnitTest() {
		return myLastTrimConditionalIdsFromPrimaryKeysForUnitTest.get();
	}
}
