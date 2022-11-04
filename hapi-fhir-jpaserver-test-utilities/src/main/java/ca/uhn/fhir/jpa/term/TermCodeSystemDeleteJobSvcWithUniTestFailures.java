package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.api.TermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;

import java.util.concurrent.atomic.AtomicBoolean;

public class TermCodeSystemDeleteJobSvcWithUniTestFailures extends TermCodeSystemDeleteJobSvc implements ITermCodeSystemDeleteJobSvc {


	private static final AtomicBoolean ourFailNextDeleteCodeSystemVersion = new AtomicBoolean(false);

	/**
	 * This is here for unit tests only
	 */
	@VisibleForTesting
	public static void setFailNextDeleteCodeSystemVersion(boolean theFailNextDeleteCodeSystemVersion) {
		ourFailNextDeleteCodeSystemVersion.set(theFailNextDeleteCodeSystemVersion);
	}


	@Override
	public void deleteCodeSystemVersion(long theVersionPid) {
		// Force a failure for unit tests
		if (ourFailNextDeleteCodeSystemVersion.getAndSet(false)) {
			throw new InternalErrorException("Unit test exception");
		}

		super.deleteCodeSystemVersion(theVersionPid);
	}


	}
